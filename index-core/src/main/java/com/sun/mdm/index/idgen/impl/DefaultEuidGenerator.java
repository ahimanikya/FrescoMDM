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
package com.sun.mdm.index.idgen.impl;

import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.idgen.SEQException;
import com.sun.mdm.index.idgen.ChecksumCalculator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.ejb.sequence.SequenceEJB;
import com.sun.mdm.index.ejb.sequence.SequenceEJBLocal;
import com.sun.mdm.index.util.JNDINames;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Default EUID generator.
 * @author Dan Cidon
 */
public class DefaultEuidGenerator implements EuidGenerator {

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    private int mIdLength;
    private int mTrialCount = 5; //Need to read this from the EIUD config
    private int mChecksumLength;
    private int mChunkSize;
    private static Long mNextEUID = new Long(-1);
    private String mChecksumClassname = "com.sun.mdm.index.idgen.impl.DefaultChecksumCalculator";
    private ChecksumCalculator mChecksumCalculator = null;
    private static boolean sequeneceDatabaseAvailable = false;
    private static boolean isInitialized = false;
    private static String DatabaseType = ObjectFactory.getDatabase();
    private static SequenceEJBLocal mseqLocal = null;
    private static boolean insideContainer = true;

    /** Default constructor for DefaultEuidGenerator
     * @throws SEQException An exception occurred
     */
    public DefaultEuidGenerator() throws SEQException {
        
        if (!isInitialized && insideContainer) {
	        try {
	            Context init = new InitialContext();
	            mseqLocal = (SequenceEJBLocal) init.lookup(JNDINames.EJB_SEQUENCE);
	        } catch (NamingException ex) {
	                throw new SEQException("Could not Find Sequence EJB " + ex.getMessage());            
	        } catch (Exception ex) {
	            System.out.println("Failed to get Sequence EJB " + ex.getMessage());
	        }
	    	
	    	if ((mseqLocal.pingDatabase()).equals("Up")) {
	               sequeneceDatabaseAvailable = true;
		        if (mLogger.isLoggable(Level.FINE)) {
		                mLogger.fine("Sequence Connection Pool is Available");
		        } 
		}
	        else if (!DatabaseType.equalsIgnoreCase("Oracle")) { 
	        //Database is not Oracle and the Sequence Generator Pool is not available. Throw SQLException.
	            throw new SEQException(mLocalizer.t("IDG502A: A Sequence Generator Connection Pool required for " + DatabaseType +
	                                                " database was not found or not reachable"));	
	        }
	        isInitialized = true; 
	}
    }
    
    public static void setinsideLoader() {
    	
    	insideContainer = false;
    	
    }
    
    public static void setinsideContainer() {
    	
    	insideContainer = true;
    	
    }

    /** Parameters of the euid generator represented in the configuration XML
     * file are set using this method.
     *
     * @param parameterName parameter
     * @param value parameter value
     * @exception SEQException An error occurred
     */
    public void setParameter(String parameterName, Object value)
            throws SEQException {
        if (parameterName.equals("IdLength")) {
            mIdLength = ((Integer) value).intValue();
        } else if (parameterName.equals("ChecksumLength")) {
            mChecksumLength = ((Integer) value).intValue();
        } else if (parameterName.equals("ChecksumTry")) {
            mTrialCount = ((Integer) value).intValue();    
        } else if (parameterName.equals("ChunkSize")) {
            mChunkSize = ((Integer) value).intValue();
        } else if (parameterName.equals("ChecksumCalculatorClass")) {
            mChecksumClassname = (String) value;
            mChecksumCalculator = null;
        } else {
            throw new SEQException(mLocalizer.t("IDG504: Unknown parameter: {0}",
                    parameterName));
        }
    }

    /** Generate the next EUID
     *
     * @exception SEQException an error occurred
     * @return next EUID
     */
    public String getNextEUID(Connection con) throws SEQException {
        String euid = null;

        /*
         * Loop until a valid EUID is found. An upper limit could be placed
         * here, but that would be putting a restriction on the checksum
         * algorithm implementations.
         */
        boolean  validEUID = false;
        String retVal = null;
        int trialCount = 0;
        while (!validEUID && trialCount < mTrialCount) {
            try {
                if (((DatabaseType.equalsIgnoreCase("Oracle") &&
                         !sequeneceDatabaseAvailable)) || (!insideContainer)) { //If not inside container always use one connection
                    synchronized(mNextEUID) {
                            long val = mNextEUID.longValue();
                            if (val != -1) {
                                if (val % mChunkSize == 0) {
                                    val = xgetNextEUID(con);
                                }
                                euid = String.valueOf(val);
                            } else {
                                val = xgetNextEUID(con);
                                euid = String.valueOf(val);
                            }
                            mNextEUID = new Long(val + 1);
                    }
                } else {
                    synchronized(mNextEUID) {
                            long val = mNextEUID.longValue();
                            if (val != -1) {
                                if (val % mChunkSize == 0) {
                                    val = mseqLocal.xgetNextEUID(mChunkSize, DatabaseType);
                                }
                                euid = String.valueOf(val);
                            } else {
                                val = mseqLocal.xgetNextEUID(mChunkSize, DatabaseType);
                                euid = String.valueOf(val);
                            }
                            mNextEUID = new Long(val + 1);
                    }
                }
            } catch (SEQException e) {
                throw e;
            }
            
            
            String sChecksum;
            if (mChecksumLength > 0) {
                if (mChecksumCalculator == null) {
                    Class mChecksumCalculatorClass = null;
                    try {
                        mChecksumCalculatorClass = Class.forName(mChecksumClassname.trim());
                    } catch (Exception e) {
                        throw new SEQException(mLocalizer.t("IDG507: DefaultEuidGenerator " + 
                                    "could not load class {0}: {1}", mChecksumClassname, e));
                    }
                    try {
                        mChecksumCalculator = (ChecksumCalculator) mChecksumCalculatorClass.newInstance();
                    } catch (Exception e) {
                        throw new SEQException(mLocalizer.t("IDG508: Encountered an " + 
                                "error while retrieving the Checksum Calculator: {0}", e));
                    }
                    mChecksumCalculator.setParameter("ChecksumLength", Integer.valueOf(mChecksumLength));
                }
                sChecksum = mChecksumCalculator.calcChecksum(formatEUID(euid));
                if (sChecksum == null) {
                    // skip this EUID because no checksum could be calculated
                    //mNextEUID++;
                    trialCount++;
                    continue;
                }
                retVal = formatEUID(euid) + sChecksum;
            } else {
                retVal = formatEUID(euid);
            }
            //mNextEUID++;

            //return retVal;
            validEUID = true;
        }
        return retVal;
    }

    /** Get the EUID length
     * @return euid length
     *
     */
    public int getEUIDLength() {
        return mIdLength + mChecksumLength;
    }

    private void getDatabaseConnection() throws SEQException {
    }

    private long xgetNextEUID(Connection conn) throws SEQException {
        long nextValue;
        
        /*Note: This method only will be used by Oracle. Some code might be useless*/
        try {
            if (DatabaseType.equalsIgnoreCase("Oracle")) {
                nextValue = getSeqNoByFunction(conn);
            } else if (DatabaseType.equalsIgnoreCase("MySQL")) {
                nextValue = getSeqNoByMySQLFunction(conn);
            } else {
                nextValue = getSeqNoByProcedure(conn);
            }            
            	
        } catch (Exception exp) {
            throw new SEQException(mLocalizer.t("IDG509: Could not retrieve the " +
                    "next EUID: {0}", exp), exp);
        }
        return nextValue;
    }

    private long getSeqNoByFunction(Connection conn) throws SQLException {
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

    private long getSeqNoByMySQLFunction(Connection conn) throws SQLException {
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
    private long getSeqNoByProcedure(Connection conn) throws SQLException {
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

    private String formatEUID(String euid) throws SEQException {
        if (euid.length() > mIdLength) {
            throw new SEQException(mLocalizer.t("IDG506: ID length exceeded: {0}. " +
                    "Maximum length is {1}", euid.length(), mIdLength));
        }
        int padLength = mIdLength - euid.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < padLength; i++) {
            sb.append('0');
        }
        sb.append(euid);
        return sb.toString();
    }
}
