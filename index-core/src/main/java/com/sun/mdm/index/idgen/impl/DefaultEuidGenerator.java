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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.util.Localizer;

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
    private int mChecksumLength;
    private int mChunkSize; 
    private long mNextEUID = -1;
    
    /** Default constructor for DefaultEuidGenerator
     * @throws SEQException An exception occurred
     */
    public DefaultEuidGenerator() throws SEQException {
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
        } else if (parameterName.equals("ChunkSize")) {
            mChunkSize = ((Integer) value).intValue();
        } else {
            throw new SEQException(mLocalizer.t("IDG504: Unknown parameter: (0}", 
                                                parameterName));
        }            
    }

    /** Generate the next EUID
     *
     * @exception SEQException an error occurred
     * @return next EUID
     */    
    public synchronized String getNextEUID(Connection con) throws SEQException {
        String euid = null;

        try {
            if (mNextEUID != -1) {
                if (mNextEUID % mChunkSize == 0) {
                    mNextEUID = xgetNextEUID(con);
                }
                euid = String.valueOf(mNextEUID);
            } else {
                mNextEUID = xgetNextEUID(con);
                euid = String.valueOf(mNextEUID);
            }
        } catch (SEQException e) {
            throw e;
        }
        String retVal;
        String sChecksum;
        if (mChecksumLength > 0) {
            sChecksum = String.valueOf(getChecksum(mNextEUID));
            retVal = formatEUID(euid) + sChecksum;
        } else {
            retVal = formatEUID(euid);
        }
        mNextEUID++;
        
        
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
        try {
        	if (ObjectFactory.getDatabase().equalsIgnoreCase("Oracle")) {
        		nextValue = getSeqNoByFunction(conn);
        	} else {
        		nextValue = getSeqNoByProcedure(conn);        		
        	}
        } catch (Exception exp) {
            throw new SEQException(mLocalizer.t("IDG505: Could not retrieve the " + 
                                                "next EUID: (0}", exp));
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
    
    private String getChecksum(long value) {
        int mod = (int) Math.pow(10, mChecksumLength); 
        value = (1103515243 * value + 12345) & 0x7FFFFFFF;
        long ck = value % mod; 
        String retVal = String.valueOf(ck); 
        int padLength = mChecksumLength - retVal.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < padLength; i++) {
            sb.append('0');
        }
        sb.append(retVal);
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Checksum for " + value + " is " + sb); 
        }
        return sb.toString();
    }
    
}
