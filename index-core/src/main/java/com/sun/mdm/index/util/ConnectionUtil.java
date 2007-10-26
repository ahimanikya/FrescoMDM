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
package com.sun.mdm.index.util;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @author sdua
 */
public class ConnectionUtil {
    
    public static final int DB_UNKNOWN = -1;
    public static final int DB_ORACLE = 0;
    public static final int DB_SQLSERVER = 1;
    
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.util." + ConnectionUtil.class.getName());
    
    private SessionContext mcontext;
    private static int connectionCounter = 0;

    private final static String DB_PROP_KEY = "resJNDI";
    private final static String DB_PROP_FILE = "eviewdb.properties";
    private static DataSource mDataSource = null;
    private static int mDBProduct = DB_UNKNOWN;

    /**
     * Obtains a connection to the database.
     *
     * @throws Exception if an error occurs.
     * @return Connection to the database.
     */
    public static Connection getConnection()
        throws Exception {

        // Check if mDataSource has been already cached.
        if (mDataSource == null) {
            Context ctx = new InitialContext();
            mLogger.debug(DB_PROP_FILE + "not found, using default JNDI for data source");
            mDataSource = (javax.sql.DataSource)ctx.lookup(com.sun.mdm.index.util.JNDINames.BBE_DATASOURCE);
           
        } 
        Connection con = mDataSource.getConnection();
        mLogger.debug("in getConnection(): " + con); 
        
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
    public static String pingDatabase()  {
    	PreparedStatement stmt = null;
        ResultSet rSet = null;
        String status = "Unknown";
        Connection conn = null;
        try {
        	conn = getConnection();
        	if (getDBProductID()==DB_SQLSERVER){
        		//sql server use SELECT 'Something'
        		stmt = conn.prepareStatement("select 'Up'");
        	}else{
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
    			} catch ( Exception e ) {}  
    			try {
    				if (stmt != null) {
    					stmt.close();
    				}
    			} catch ( Exception e ) {}  
    			try {
    				if (conn != null) {
    					conn.close();
    				}
    			} catch ( Exception e ) {
                    mLogger.error("releaseConnection(): could not close JDBC connection", e);    			
    			}  
    		} catch (Exception e) {
    		}
        }
        return status;
    }
    
    /**
     * Sets up DBProductID for this installation to either DB_SQLSERVER or
     * DB_ORACLE.
     *
     * @param con Database connection.
     * @throws Exception if an error is encountered.
     */
    public static void initDBProductID(Connection con) 
    	throws Exception {
    	
    	if (mDBProduct == DB_UNKNOWN) { // not initialized yet
    	  DatabaseMetaData dbmd = con.getMetaData();
    	  String dbProductName = dbmd.getDatabaseProductName();
    	
    	  if (dbProductName.endsWith("SQL Server")) {
    		mDBProduct = DB_SQLSERVER;
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
     * or DB_ORACLE for an Oracle database.
     */
    
    public static int getDBProductID(Connection con) 
	throws Exception {
	
	  initDBProductID(con);	
	  
	  return mDBProduct;	
    }
    
    /** Get The ID of the database vendor.
     * 
     * @return  DB_SQLSERVER for an SQL Server database 
     * or DB_ORACLE for an Oracle database.
     */
    public static int getDBProductID() 
	{	
	  return mDBProduct;	
    }
    
}
