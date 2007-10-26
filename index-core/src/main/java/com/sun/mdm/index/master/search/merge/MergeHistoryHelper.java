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
package com.sun.mdm.index.master.search.merge;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.objects.TransactionObject;

/**
 * Helper class to traverse merge history
 */
public class MergeHistoryHelper {
    
    private final TransactionMgr mTrans;
    
    /** Creates a new instance of MergeHistoryHelper
     * @param transMgr handle to transaction manager
     */
    public MergeHistoryHelper(TransactionMgr transMgr) {
        mTrans = transMgr;
    }
    
    //Order by DESC because want earliest transaction at 
    //the bottom of result set
    /** Select string
     */    
    private static final String SELECT_MERGE_TRANS =
        "SELECT M.MERGED_EUID, M.KEPT_EUID, M.MERGE_TRANSACTIONNUM, T.TIMESTAMP "
            + "FROM SBYN_MERGE m, SBYN_TRANSACTION T "
            + "WHERE M.KEPT_EUID = ? "
            + "AND M.MERGE_TRANSACTIONNUM = T.TRANSACTIONNUMBER "
            + "AND M.UNMERGE_TRANSACTIONNUM IS NULL "
            + "ORDER BY T.TIMESTAMP DESC"; 
            

    // select string with timestamp
    private static final String SELECT_MERGE_TRANS_TIMESTAMP =
        "SELECT M.MERGED_EUID, M.KEPT_EUID, M.MERGE_TRANSACTIONNUM, T.TIMESTAMP "
            + "FROM SBYN_MERGE M, SBYN_TRANSACTION T "
            + "WHERE M.KEPT_EUID = ? "
            + "AND M.MERGE_TRANSACTIONNUM = T.TRANSACTIONNUMBER "
            + "AND M.UNMERGE_TRANSACTIONNUM IS NULL "
            + "AND T.TIMESTAMP < ? "
            + "ORDER BY T.TIMESTAMP DESC"; 
    
    /** Select string
     */    
    private static final String SELECT_ACTIVE_EUID = 
        "SELECT M.KEPT_EUID, M.MERGE_TRANSACTIONNUM, T.TIMESTAMP " 
            + "FROM SBYN_MERGE M, SBYN_TRANSACTION T " 
            + "WHERE M.MERGED_EUID = ? "
            + "AND M.MERGE_TRANSACTIONNUM = T.TRANSACTIONNUMBER "
            + "AND M.UNMERGE_TRANSACTIONNUM IS NULL "     
            + "ORDER BY T.TIMESTAMP DESC"; 


    // select string with timestamp
    // most recent merge is at the top of the list
    private static final String SELECT_ACTIVE_EUID_TIMESTAMP =
        "SELECT M.KEPT_EUID, M.MERGE_TRANSACTIONNUM, T.TIMESTAMP  " 
            + "FROM SBYN_MERGE M, SBYN_TRANSACTION T " 
            + "WHERE M.MERGED_EUID = ? "
            + "AND M.MERGE_TRANSACTIONNUM = T.TRANSACTIONNUMBER "
            + "AND M.UNMERGE_TRANSACTIONNUM IS NULL "
            + "AND T.TIMESTAMP > ?"
            + "ORDER BY T.TIMESTAMP DESC"; 
    
    /** This method populates the MergeHistoryNode object based on the root EUID
     * that is passed to it.  The main table that is used to determine the merge
     * history is SBYN_MERGETRANS.  To demonstrate how this function is used, an
     * example will be used.  Suppose the following merges have been performed:
     *
     * F merged to C (C survives) at time 1
     * B merged to A (A survives) at time 2
     * C merged to A (A survives) at time 3
     * G merged to D (D survives) at time 4
     * A merged to D (D survives) at time 5
     * D merged to E (E survives) at time 6
     *
     * The mrg trans table will appear as follows:
     *
     *   Dest         Source      TransId
     * (survivor)   (merged)
     * ---------------------------------------------
     *      C       F           1
     *      A       B           2
     *      A       C           3
     *      D       G           4
     *      D       A           5
     *      E       D           6
     *
     * The desired merge tree is:
     *
     *  E
     *  |--E
     *  |--D
     *     |--D
     *     |  |--D
     *     |  |--G
     *     |--A
     *       |--A
     *       |  |--A
     *       |  |--B
     *       |--C
     *          |--C
     *          |--F
     *
     * In order to translate the sbyn_mrg_trans table into this tree,
     * a list was created to hold EUID's and handles for both merged and
     * survivor records.  The code in this event populates the list as
     * follows:
     *
     *                  1   2   3   4   5   6   7
     *              ----------------------------------------------------------
     * Dest_EUID        E   E   D   D   A   A   C
     * Source_UID               D   A   G   C   B   F
     *
     * To achieve this result the following algorithm was used:
     *
     * 1) Search mrg trans for all EUID's that equal root
     * 2) Populate the list with the survivor and merged EUIDs of the result set
     *
     *                  1   2   3   4   5   6   7
     *              ----------------------------------------------------------
     * Dest_EUID        E   E
     * Source_EUID          D
     *
     * 3) Now search for all EUID's in mrg trans that equal the merged EUID
     *
     * 4) Populate the list with the survivor and merged EUID's of the
     * result set (sorted by transaction id)
     *
     *                  1   2   3   4   5   6   7
     *              ----------------------------------------------------------
     * Dest_EUID        E   E   D   D
     * Source_EUID          D   A   G
     *
     * 5) Now search for all EUID's in mrg trans that equal the added merged
     * EUID's (A, G)
     *
     * 6) Populate the list with the survivor and merged UID's of the
     * result set (sorted by transaction id)
     *
     *                  1   2   3   4   5   6   7
     *              ----------------------------------------------------------
     * Dest_EUID        E   E   D   D   A   A
     * Source_EUID          D   A   G   C   B
     *
     * 5) Now search for all EUID's in mrg trans that equal the added merged
     * EUID's (C, B)
     *
     * 6) Populate the list with the survivor and merged EUID's of the
     * result set (sorted by transaction id)
     *
     *                  1   2   3   4   5   6   7
     *              ----------------------------------------------------------
     * Dest_EUID        E   E   D   D   A   A   C
     * Source_EUID          D   A   G   C   B   F
     *
     *
     * 7) The above algorithm is executed as a loop until no new records
     * are added to list
     *
     * 8) Do While list pointer <= size of list
     *      Set list pointer to next row.
     *      Find record in list where (survivor EUID or merged EUID = the
     *      survivor EUID of the current row) AND list_row < current row.
     *      IF survivor EUID = the survivor of the EUID of the current row THEN
     *         Add the item as a child of the survivor UEID
     *      ELSE
     *        Add the item as a child of the merged EUID
     *      END IF
     * @param con Connection handle
     * @param euid EUID
     * @throws SQLException An error occured.
     * @throws OPSException An error occured.
     * @return Merge history
     */
    public MergeHistoryNode getMergeHistory(Connection con, String euid)
            throws SQLException, OPSException {
        
        euid = getActiveEUID(con, euid);
        
        ArrayList mergeNodeList = new ArrayList();
        MergeHistoryNode node;
        
        int liBegin = 0;
        PreparedStatement ps1 = con.prepareStatement(SELECT_MERGE_TRANS);
        PreparedStatement ps2 = con.prepareStatement(SELECT_MERGE_TRANS_TIMESTAMP);
        PreparedStatement ps;
        ResultSet rs = null;

        try {        
            //Prime the pump with a dummy first node
            mergeNodeList.add(new MergeHistoryNode(euid, euid));
            String transID = null;
            Timestamp ts = null;
            boolean firstNode = true;
            while (liBegin < mergeNodeList.size()) {
                Iterator i = 
                    subIterator(mergeNodeList, liBegin, mergeNodeList.size());
                while (i.hasNext()) {
                    node = (MergeHistoryNode) i.next();
                    if (firstNode == true) {
                        ps = ps1;
                        ps.setString(1, node.getSourceNode().getEUID());
                        firstNode = false;
                    } else {
                        ps = ps2;
                        ps.setString(1, node.getSourceNode().getEUID());
                        ps.setTimestamp(2, ts);
                    }

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String sourceEUID = rs.getString(1);
                        String destEUID = rs.getString(2);
                        transID = rs.getString(3);
                        ts = rs.getTimestamp(4);
                        node = new MergeHistoryNode(destEUID, sourceEUID);
                        TransactionObject transObj = mTrans.findTransactionLog(con, transID);
                        node.setTransactionObject(transObj);
                        //Find parent of this node
                        for (int j = mergeNodeList.size() - 1; j > 0; j--) {
                            MergeHistoryNode parentNode = 
                                (MergeHistoryNode) mergeNodeList.get(j);
                            String sourceNodeEUID = parentNode.getSourceNode().getEUID();
                            if (sourceNodeEUID != null && sourceNodeEUID.equals(destEUID)) {
                                parentNode.setSourceNode(node);
                                node.setParentNode(parentNode);
                                break;
                            } else  {
                                String destNodeEUID = parentNode.getDestinationNode().getEUID();
                                if (destNodeEUID != null && destNodeEUID.equals(destEUID)) {
                                    parentNode.setDestinationNode(node);
                                    node.setParentNode(parentNode);
                                    break;
                                }
                            }
                        }
                        mergeNodeList.add(node);
                    }
                    rs.close();
                    liBegin++;
                }
            }
            
            //If there was merge history, return the node at the second position
            //since the first position was a dummy node.
            if (mergeNodeList.size() > 1) {
                node = (MergeHistoryNode) mergeNodeList.get(1);
            } else {
                node = null;
            } 
        } catch (SQLException e) {
            throw e;
        } catch (OPSException  e) {
            throw e;
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return node;
    }
    
    /** Search the merge tree and find the active EUID associated with the
     * merged EUID.
     * @return Returns EUID record that pairs with the merged EUID of a
     * merged event
     * @param con Connection handle to database
     * @param sourceEUID EUID of merged record
     * @throws SQLException An error occured.
     */
    public String getActiveEUID(Connection con, String sourceEUID)
    throws SQLException {
        String euid = sourceEUID;
        ResultSet rs = null;
        PreparedStatement selectActiveEUID = null;
        PreparedStatement selectActiveEUIDTimestamp = null;
        
        try {
            selectActiveEUID = con.prepareStatement(SELECT_ACTIVE_EUID);
            selectActiveEUIDTimestamp = con.prepareStatement(SELECT_ACTIVE_EUID_TIMESTAMP);
            
            selectActiveEUID.setString(1, sourceEUID);
            rs = selectActiveEUID.executeQuery();
            
            // If the uid is found, set the value
            if (rs.next()) {
              euid = rs.getString(1);
              Timestamp ts = rs.getTimestamp(3);
              euid = getActiveEUIDHelper(selectActiveEUIDTimestamp, euid, ts);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (selectActiveEUID != null) {
                    selectActiveEUID.close();
                }
                if (selectActiveEUIDTimestamp != null) {
                    selectActiveEUIDTimestamp.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return euid;
    }    
    
    /** Search the merge tree and find the active EUID associated with the
     * merged EUID.  The timestamp is used to prevent circular references.
     * @return Returns EUID record that pairs with the merged EUID of a
     * merged event
     * @param selectActiveEUIDTimestamp  Prepared statement for selcting the active
     * EUID.
     * @param sourceEUID EUID of merged record
     * @param ts Timestamp of the last of merge.
     * @throws SQLException An error occured.
     */
    public String getActiveEUIDHelper(PreparedStatement selectActiveEUIDTimestamp, 
                                      String sourceEUID, 
                                      Timestamp ts)
            throws SQLException {
                
        String euid = sourceEUID;
        ResultSet rs = null;
        
        try {        
            selectActiveEUIDTimestamp.setString(1, sourceEUID);
            selectActiveEUIDTimestamp.setTimestamp(2, ts);
            rs = selectActiveEUIDTimestamp.executeQuery();
    
            // If the uid is found, set the value
            if (rs.next()) {
                euid = rs.getString(1);
                String transactionNumber = rs.getString(2);
                euid = getActiveEUIDHelper(selectActiveEUIDTimestamp, euid, ts);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return euid;
    }    
    
    /** Get the sub iterator
     * @param list input list
     * @param liBegin start index
     * @param liEnd end index
     * @return sub iterator
     */    
    private Iterator subIterator(ArrayList list, int liBegin, int liEnd) {
        ArrayList newList = new ArrayList(list.subList(liBegin, liEnd));
        return newList.iterator();
    }
    
}
