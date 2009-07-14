/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.mdm.index.ejb.sequence;


import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.idgen.SEQException;

/**
 *
 * @author Parijat Kar
 */

@Stateless (mappedName="ejb/_EVIEW_OBJECT_TOKEN_SequenceEJB")
@Remote (SequenceEJBRemote.class)
@Local (SequenceEJBLocal.class)

@Resource(  name="jdbc/SEQDataSource",
                type=javax.sql.DataSource.class,
                mappedName="jdbc/_EVIEW_OBJECT_TOKEN_SequenceDataSource" )
                
@TransactionManagement(TransactionManagementType.BEAN)



public class SequenceEJB implements SequenceEJBRemote, SequenceEJBLocal {
    
    
    private SessionContext mSessionContext;
     
    private boolean initialized = false;
    private static final int CHUNK_SIZE = 1000;
    
    public static final int DB_UNKNOWN = -1;
    public static final int DB_ORACLE = 0;
    public static final int DB_SQLSERVER = 1;
    public static final int DB_MYSQL = 2;
    public static final int DB_AXION = 5;
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.idgen." + SequenceEJB.class.getName());
    private transient static final Localizer mLocalizer = Localizer.get();
    private SessionContext mcontext;
    private static int connectionCounter = 0;
    private final static String DB_PROP_KEY = "resJNDI";
    private final static String DB_PROP_FILE = "eviewdb.properties";
    private static DataSource mSeqDataSource = null;
    private static int mDBProduct = DB_UNKNOWN;
     
    public SequenceEJB() {
    }
    
    @Resource
    public void setSessionContext(SessionContext sessionContext){
    	  mSessionContext = sessionContext;
    }
    
    @PostConstruct
    public void initialize() throws Exception{       
              
    }
    
    
    @PreDestroy
    public void cleanUp(){    	
    }
    
    /**
     * Obtains a connection to the database.
     *
     * @throws Exception if an error occurs.
     * @return Connection to the database.
     */
    private  Connection getConnection()
            throws Exception {

        // Check if mDataSource has been already cached.
        if (mSeqDataSource == null) {
            Context ctx = new InitialContext();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine(DB_PROP_FILE + "not found, using default JNDI for data source");
            }
            mSeqDataSource = (javax.sql.DataSource) ctx.lookup(com.sun.mdm.index.util.JNDINames.SEQ_DATASOURCE);

        }
        Connection con = mSeqDataSource.getConnection();
        con.setAutoCommit(false);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Obtained JDBC connection: " + con);
        }

        // Initialize the DB ID to make sure that
        // getDBProductID() always returns a valid ID
        initDBProductID(con);

        return con;
    }

    /**
     * Checks if a database can be reached.
     *
     * @return String indicating the status of the database.
     */
    public  String pingDatabase() {
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        String status = "Unknown";
        Connection conn = null;
        try {
            conn = getConnection();
            if ((getDBProductID() == DB_SQLSERVER) || (getDBProductID() == DB_MYSQL) || ((getDBProductID() == DB_AXION))) {
                //sql server use SELECT 'Something'
                stmt = conn.prepareStatement("select 'Up'");
            } else {
                //Oracle use SELECT 'Something' FROM DUAL
                stmt = conn.prepareStatement("select 'Up' from dual");
            }
            rSet = stmt.executeQuery();
            if (null != rSet) {
                if (rSet.next()) {
                    status = rSet.getString(1);
                }
            }
        } catch (Exception e) {
            status = "Down";
        } finally {
            try {
                try {
                    if (rSet != null) {
                        rSet.close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    mLogger.warn(mLocalizer.x("UTL001: ConnectionUtil.releaseConnection(): could not close JDBC connection: {0}", e.getMessage()));
                }
            } catch (Exception e) {
            }
        }
        return status;
    }

    /**
     * Sets up DBProductID for this installation to either DB_SQLSERVER, DB_MYSQL or
     * DB_ORACLE.
     *
     * @param con Database connection.
     * @throws Exception if an error is encountered.
     */
    public void initDBProductID(Connection con)
            throws Exception {

        if (mDBProduct == DB_UNKNOWN) { // not initialized yet

            DatabaseMetaData dbmd = con.getMetaData();
            String dbProductName = dbmd.getDatabaseProductName();

            if (dbProductName.endsWith("SQL Server")) {
                mDBProduct = DB_SQLSERVER;
            } else if (dbProductName.equals("AxionDB")) {
                mDBProduct = DB_AXION;
            } else if (dbProductName.equals("MySQL")) {
                mDBProduct = DB_MYSQL;
            } else {
                mDBProduct = DB_ORACLE;
            }
        }

    }

    /**
     * Return DBProductID.
     *
     * @param con Database connection.
     * @throws Exception if an error occurred.
     * @return  DB_SQLSERVER for an SQL Server database 
     * or DB_ORACLE for an Oracle database
     * or DB_MYSQL for a MySQL database
     * or DB_AXION for an Axion database
     */
    private int getDBProductID(Connection con)
            throws Exception {

        initDBProductID(con);

        return mDBProduct;
    }

    /** Get The ID of the database vendor.
     * 
     * @return  DB_SQLSERVER for an SQL Server database 
     * or DB_ORACLE for an Oracle database
     * or DB_MYSQL for a MySQL database
     * or DB_AXION for an Axion database
     */
    private int getDBProductID() {
        return mDBProduct;
    }
    
    public Integer xgetNextUID(String seqName, String DatabaseType)
            throws SEQException /* ,ConnectionInvalidException */ {
        int nextValue;
        
        Connection con = null;
        
        try {
            con = getConnection();
            /* Prepare SP Call Statement.       */
            if (DatabaseType.equalsIgnoreCase("Oracle")) {
                nextValue = getSeqNoByFunction(seqName, con);
            } else if (DatabaseType.equalsIgnoreCase("MySQL")) {
                nextValue = getSeqNoByMySQLFunction(seqName, con);
            } else {
                nextValue = getSeqNoByProcedure(seqName, con);
            }
            con.commit();            
        } catch (SQLException exp) {
            mLogger.info(mLocalizer.x("Failed in update or commit" + exp.getMessage()));
            throw new SEQException(mLocalizer.t("IDG503: Could not retrieve the next " +
                    "ID from the EUID generator: {0}", exp.getMessage()), exp);
        } catch (Exception e) {
            mLogger.info(mLocalizer.x("Failed in update or commit" + e.getMessage()));
            throw new SEQException(mLocalizer.t("IDG503: Could not retrieve the next " +
                    "ID from the EUID generator: {0}", e.getMessage()), e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    mLogger.info(mLocalizer.x("Failed in close" + e.getMessage()));
                    throw new SEQException(mLocalizer.t("UTL001: ConnectionUtil.releaseConnection(): could not close JDBC connection: {0}", e.getMessage()));
                }
            }
        }



        return new Integer(nextValue);
    }

    /**
     * Get a sequence number by calling SEQMGR function.
     * The sequence number in the database will be increased by CHUNK_SIZE
     * 
     * @param seqName name of the sequence
     * @param connection database connection
     * @return a sequence number
     * @throws SQLException
     */
    private int getSeqNoByFunction(String seqName, Connection connection) throws SQLException {
        int nextValue;
        /* Prepare SP Call Statement.       */
        String command = "{? = call SEQMGR(?, ?)}"; // 2 place holder + 1 return value

        CallableStatement cstmt = connection.prepareCall(command);

        // Register out parameters
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2, seqName);
        cstmt.setInt(3, CHUNK_SIZE);
        cstmt.execute();
        nextValue = cstmt.getInt(1);
        cstmt.close();
        return nextValue;
    }

    /**
     * Get a sequence number by calling SEQMGR stored procedure.
     * The sequence number in the database will be increased by CHUNK_SIZE
     * 
     * @param seqName name of the sequence
     * @param connection database connection
     * @return a sequence number
     * @throws SQLException
     */
    private int getSeqNoByProcedure(String seqName, Connection connection) throws SQLException {
        int nextValue;
        /* Prepare SP Call Statement.       */
        String command = "{call SEQMGR(?, ?, ?)}"; // 3 place holders

        CallableStatement cstmt = connection.prepareCall(command);

        // Register out parameters
        cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
        cstmt.setString(1, seqName);
        cstmt.setInt(2, CHUNK_SIZE);
        cstmt.execute();
        nextValue = cstmt.getInt(3);
        cstmt.close();
        return nextValue;
    }

    /**
     * Get a sequence number by calling SEQMGR MySQL function.
     * The sequence number in the database will be increased by CHUNK_SIZE
     * 
     * @param seqName name of the sequence
     * @param connection database connection
     * @return a sequence number
     * @throws SQLException
     */
    private int getSeqNoByMySQLFunction(String seqName, Connection connection) throws SQLException {
        int nextValue = 0;

        PreparedStatement stmt = connection.prepareStatement("select SEQMGR(?, ?)");

        stmt.setString(1, seqName);
        stmt.setInt(2, CHUNK_SIZE);
        ResultSet rs = stmt.executeQuery();
        try {
            if (rs.next()) {
                nextValue = rs.getInt(1);
            }
        } catch (SQLException se) {
            throw se;
        }
        rs.close();
        stmt.close();
        return nextValue;
    }
    
    public long xgetNextEUID(int mChunkSize, String DatabaseType) throws SEQException {
        long nextValue; 
        
        Connection conn = null;
        
        try {
            conn = getConnection();
            if (DatabaseType.equalsIgnoreCase("Oracle")) {
                nextValue = getSeqNoByFunction(mChunkSize, conn);
            } else if (DatabaseType.equalsIgnoreCase("MySQL")) {
                nextValue = getSeqNoByMySQLFunction(mChunkSize, conn);
            } else {
                nextValue = getSeqNoByProcedure(mChunkSize, conn);
            }
            conn.commit();            
        } catch (Exception exp) {
            mLogger.info(mLocalizer.x("Failed in update or commit"+ exp.getMessage()));
            throw new SEQException(mLocalizer.t("IDG505: Could not retrieve the " +
                    "next EUID: {0}", exp), exp);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    mLogger.info(mLocalizer.x("Failed in update or commit"+ e.getMessage()));
                    throw new SEQException(mLocalizer.t("UTL001: ConnectionUtil.releaseConnection(): could not close JDBC connection: {0}", e.getMessage()));
                }
            }
        }
        return nextValue;
    }

    private long getSeqNoByFunction(int mChunkSize, Connection conn) throws SQLException {
        long nextValue;
        /* Prepare SP Call Statement.       */
        String command = "{? = call SEQMGR(?, ?)}"; // 1 place holders + 1 return value

        CallableStatement cstmt = conn.prepareCall(command);

        // Register out parameters
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2, "EUID");
        cstmt.setInt(3, mChunkSize);
        cstmt.execute();
        nextValue = cstmt.getLong(1);
        cstmt.close();
        return nextValue;
    }

    private long getSeqNoByMySQLFunction(int mChunkSize, Connection conn) throws SQLException {
        long nextValue = 0;

        PreparedStatement stmt = conn.prepareStatement("select SEQMGR(?, ?)");

        stmt.setString(1, "EUID");
        stmt.setInt(2, mChunkSize);
        ResultSet rs = stmt.executeQuery();
        try {
            if (rs.next()) {
                nextValue = rs.getLong(1);
            }
        } catch (SQLException se) {
            throw se;
        }
        rs.close();
        stmt.close();
        return nextValue;
    }

    /**
     * Get a sequence number by calling SEQMGR stored procedure.
     * The sequence number in the database will be increased by CHUNK_SIZE
     * 
     * @param connection database connection
     * @return a sequence number
     * @throws SQLException
     */
    private long getSeqNoByProcedure(int mChunkSize, Connection conn) throws SQLException {
        long nextValue;
        /* Prepare SP Call Statement.       */
        String command = "{call SEQMGR(?, ?, ?)}"; // 1 place holders + 1 return value

        CallableStatement cstmt = conn.prepareCall(command);

        // Register out parameters
        cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
        cstmt.setString(1, "EUID");
        cstmt.setInt(2, mChunkSize);
        cstmt.execute();
        nextValue = cstmt.getLong(3);
        cstmt.close();
        return nextValue;
    }
}
