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
package com.sun.mdm.index.idgen;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.HashMap;
import com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.master.ConnectionInvalidException;
import com.sun.mdm.index.objects.metadata.ObjectFactory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SeeBeyond</p>
 * @author Raed Haltam
 * @version 1.0
 */
public class CUIDManager {
    
    private static final int CHUNK_SIZE = 1000;
    private static final int ID_LENGTH = 20;    // for the legth of the unique ID field of tables
    private static HashMap mSeqCache;
    private static EuidGenerator mEuidGenerator;

    private static boolean isInitialized = false;

    private static void init() throws  SEQException {
        mSeqCache = new HashMap();

        
        try {

            
            EuidGeneratorConfiguration dmConfig = (EuidGeneratorConfiguration)
                    ConfigurationService.getInstance().getConfiguration(
                    EuidGeneratorConfiguration.EUID_GENERATOR);
            if (dmConfig == null) {
                throw new Exception("EUID generator configuration not found.");
            }
            mEuidGenerator = dmConfig.getEuidGenerator();        
            if (mEuidGenerator == null) {
                throw new Exception("EUID generator not defined.");
            }
            isInitialized = true;

        } catch (Exception ex) {
            throw new SEQException(ex);
        } 
    }

    private CUIDManager() {
    }


    /**
     * get next id
     * @param con database connection
     * @param seqName sequence name
     * @return String sequence id
     * @exception SEQException sequence exception
     */
    public static String getNextUID(Connection con, String seqName) throws  SEQException {
     
       String uid = null;
        
       synchronized (com.sun.mdm.index.idgen.CUIDManager.class) {
         if (!isInitialized) {
           init();
         }
       }       

         try {
            if (seqName.equals("EUID")) {
                uid = mEuidGenerator.getNextEUID(con);
            } else {
                if (mSeqCache.containsKey(seqName)) {
                    synchronized (mSeqCache) {
                        Integer seq = (Integer) mSeqCache.get(seqName);
                        int nextId = seq.intValue() + 1;
                        if (nextId % CHUNK_SIZE == 0) {
                            Integer nextChunk = xgetNextUID(con, seqName);
                            nextId = nextChunk.intValue();
                        } 
                        mSeqCache.put(seqName, new Integer(nextId));
                        uid = String.valueOf(nextId);
                    }
                } else {
                    synchronized (mSeqCache) {
                        Integer nextChunk = xgetNextUID(con, seqName);
                        //mSeqCache.put(seqName, new Integer(nextChunk.intValue() + 1));
                        mSeqCache.put(seqName, nextChunk);
                        uid = nextChunk.toString();
                    }
                }
                
                //  to set the leading 0 for the sorting of transaction number field.
                // Transaction manager internally sort on transaction number.
                int numOfLeadingZero = ID_LENGTH - uid.length();
                for (int i = 1; i <= numOfLeadingZero; i++) {
                    uid = "0" + uid;
                }
            }
        } catch (SEQException e) {
            throw e;
        }

        
        
        return uid;
    }

    private static Integer xgetNextUID(Connection con, String seqName)
        throws SEQException /* ,ConnectionInvalidException */{
        int nextValue;

        
        try {
            /* Prepare SP Call Statement.       */
            if (ObjectFactory.getDatabase().equalsIgnoreCase("Oracle")) {
            nextValue = getSeqNoByFunction(seqName, con);	
            } else {
                    nextValue = getSeqNoByProcedure(seqName, con);
            }
        } catch (SQLException exp) {
            throw new SEQException(exp.getMessage());
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
	private static int getSeqNoByFunction(String seqName, Connection connection) throws SQLException {
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
	private static int getSeqNoByProcedure(String seqName, Connection connection) throws SQLException {
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
}
