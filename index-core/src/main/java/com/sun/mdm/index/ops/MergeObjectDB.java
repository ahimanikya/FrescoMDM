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
package com.sun.mdm.index.ops;

import com.sun.mdm.index.objects.MergeObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * @author gzheng
 * OPS class for TransactionLog objects
 */
public final class MergeObjectDB extends ObjectPersistenceService {
    private static String mInsertString;
    private static String mUpdateString;
    private static String mUpdateString2;   // handles unmerges with transaction ID
    private static String mUpdateString3;   // handles LID unmerges within the same EUID
    private static String mUpdateString4;   // handles EUID merges
    
    static {
        mInsertString =
              "       INSERT INTO SBYN_MERGE         \n" 
            + "       ( \n" 
            + "               MERGE_ID, \n" 
            + "               KEPT_EUID, \n" 
            + "               MERGED_EUID, \n" 
            + "               MERGE_TRANSACTIONNUM, \n" 
            + "               UNMERGE_TRANSACTIONNUM \n" 
            + "       ) \n" 
            + "       VALUES \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?  \n" 
            + "       ) \n";

        mUpdateString =
              "       UPDATE SBYN_MERGE         \n" 
            + "       SET \n" 
            + "               UNMERGE_TRANSACTIONNUM = ? \n" 
            + "       WHERE \n" 
            + "               UNMERGE_TRANSACTIONNUM IS NULL AND \n"
            + "               KEPT_EUID = ? AND \n"
            + "               MERGED_EUID = ? \n";
        mUpdateString2 =
              "       UPDATE SBYN_MERGE         \n" 
            + "       SET \n" 
            + "               UNMERGE_TRANSACTIONNUM = ? \n" 
            + "       WHERE \n" 
            + "               UNMERGE_TRANSACTIONNUM IS NULL AND \n"
            + "               MERGE_TRANSACTIONNUM = ? \n";  
        mUpdateString3 =
              "       UPDATE SBYN_MERGE         \n" 
            + "       SET \n" 
            + "               UNMERGE_TRANSACTIONNUM = ? \n" 
            + "       WHERE \n" 
            + "               UNMERGE_TRANSACTIONNUM IS NULL AND \n"
            + "               MERGE_TRANSACTIONNUM = ? AND \n"
            + "               KEPT_EUID = ? \n";
        mUpdateString4 =
              "       UPDATE SBYN_MERGE         \n" 
            + "       SET \n" 
            + "               UNMERGE_TRANSACTIONNUM = ? \n" 
            + "       WHERE \n" 
            + "               UNMERGE_TRANSACTIONNUM IS NULL AND \n"
            + "               MERGE_TRANSACTIONNUM = ? AND \n"
            + "               KEPT_EUID = ? AND \n"
            + "               MERGED_EUID = ? \n";

    }

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    
    /**
     * default constructor
     *
     * @throws OPSException if an error occurs.
     */
    public MergeObjectDB() throws OPSException {
        super();
    }


    /**
     * Persists a MergeObject into database
     *
     * @param conn JDBC connection.
     * @param mObj MergeObject to persist.
     * @throws OPSException if an error occurs.
     */
    public void create(Connection conn, MergeObject mObj)
        throws OPSException {
        // executes insert SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mInsertString, conn);
            setParam(stmt, 1, "String", mObj.getMergeID());
            setParam(stmt, 2, "String", mObj.getKeptEUID());
            setParam(stmt, 3, "String", mObj.getMergedEUID());
            setParam(stmt, 4, "String", mObj.getMergeTransactionNumber());
            setParam(stmt, 5, "String", mObj.getUnMergeTransactionNumber());

            stmt.executeUpdate();
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS539: Could not persist " + 
                                        "a MergeObject into the database: {0}", e));
        } catch (SQLException e) {
            String sqlErr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, mObj.getMergeID());                  
                params = addobject(params, mObj.getKeptEUID());                 
                params = addobject(params, mObj.getMergedEUID());               
                params = addobject(params, mObj.getMergeTransactionNumber());   
                params = addobject(params, mObj.getUnMergeTransactionNumber()); 

                String sql = sql2str(mInsertString, params);
                throw new OPSException(mLocalizer.t("OPS551: Could not persist " + 
                                        "a MergeObject into the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS540: Could not persist " + 
                                        "a MergeObject into the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS541: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }


    /**
     * Updates an existing MergeObject
     *
     * @param conn JDBC connection.
     * @param unmergetn Unmerge transaction number.
     * @param originalTransactionID Original transaction ID of the merge.
     * @throws OPSException if an error occurs.
     */
    
    public void update(Connection conn, String unmergetn, String originalTransactionID)
        throws OPSException {
        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mUpdateString2, conn);
            setParam(stmt, 1, "String", unmergetn);
            setParam(stmt, 2, "String", originalTransactionID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String sqlerr = e.getMessage();

            ArrayList params = new ArrayList();
            params = addobject(params, unmergetn);                 
            params = addobject(params, originalTransactionID);
            
            String sql = sql2str(mUpdateString2, params);
            throw new OPSException(mLocalizer.t("OPS542: Could not unmerge " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS543: Could not unmerge " + 
                                      "record with " +
                                      "transaction ID = {0}, original " + 
                                      "transaction ID = {1}: {2}", 
                                      unmergetn, originalTransactionID, e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS544: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
    
    /**
     * Updates an existing MergeObject.
     *
     * @param conn JDBC connection.
     * @param unmergetn Unmerge transaction number.
     * @param kepteuid Kept euid.
     * @param mergedeuid Merged euid.
     * @throws OPSException if an error occurs.
     */
    
    public void update(Connection conn, String unmergetn, 
                       String kepteuid, String mergedeuid)
        throws OPSException {
        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            update(conn, null, unmergetn, kepteuid, mergedeuid);
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS545: Could not unmerge " + 
                                      "record with " +
                                      "transaction ID = {0}, kept EUID = {1} " + 
                                      "merged EUID = {2}: {3}", 
                                      unmergetn, kepteuid, mergedeuid, e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS546: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
    
    /**
     * Updates an existing MergeObject.
     *
     * @param conn JDBC connection.
     * @param mergetn Original transaction number of the merge.
     * @param unmergetn Unmerge transaction number.
     * @param kepteuid Kept euid.
     * @param mergedeuid Merged euid.
     * @throws OPSException if an error occurs.
     */
    
    public void update(Connection conn, String mergetn, String unmergetn,  
                       String kepteuid, String mergedeuid)
        throws OPSException {
        // executes update SQL statement
        PreparedStatement stmt = null;
        boolean lidUnmergeFlag = false; 
        boolean euidMergeTransNumFlag = false;
        try {
            if (mergedeuid != null) {
                if (mergetn == null) {
                    stmt = getStatement(mUpdateString, conn);
                    setParam(stmt, 1, "String", unmergetn);
                    setParam(stmt, 2, "String", kepteuid);
                    setParam(stmt, 3, "String", mergedeuid);
                } else {
                    euidMergeTransNumFlag = true;
                    stmt = getStatement(mUpdateString4, conn);
                    setParam(stmt, 1, "String", unmergetn);
                    setParam(stmt, 2, "String", mergetn);
                    setParam(stmt, 3, "String", kepteuid);
                    setParam(stmt, 4, "String", mergedeuid);
                }
            } else {
                if (mergetn == null) {
                    throw new OPSException(mLocalizer.t("OPS547: Could not unmerge " + 
                                      "record.  The merge transaction number " + 
                                      "of the system merge is null"));
                }
                lidUnmergeFlag = true;
                stmt = getStatement(mUpdateString3, conn);
                setParam(stmt, 1, "String", unmergetn);
                setParam(stmt, 2, "String", mergetn);
                setParam(stmt, 3, "String", kepteuid);
            }
            
            stmt.executeUpdate();
        } catch (SQLException e) {

            String sqlerr = e.getMessage();

            ArrayList params = new ArrayList();
            String sql = new String();
            if (lidUnmergeFlag == false) {
                if (euidMergeTransNumFlag == false) {
                    params = addobject(params, unmergetn);                 
                    params = addobject(params, kepteuid);
                    params = addobject(params, mergedeuid);
                    sql = sql2str(mUpdateString, params);
                } else {
                    params = addobject(params, unmergetn);                 
                    params = addobject(params, mergetn);                 
                    params = addobject(params, kepteuid);
                    params = addobject(params, mergedeuid);
                    sql = sql2str(mUpdateString4, params);
                }
            } else {
                params = addobject(params, unmergetn);                 
                params = addobject(params, mergetn);
                params = addobject(params, kepteuid);
            
                sql = sql2str(mUpdateString3, params);
            }
            throw new OPSException(mLocalizer.t("OPS548: An SQL error " + 
                                        "occurred while executing this " +
                                        "SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS549: An OPS exception " + 
                                      "occurred: {0}", e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS550: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
}
