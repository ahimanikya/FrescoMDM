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
package com.sun.mdm.index.assumedmatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.sun.mdm.index.idgen.CUIDManager;
import com.sun.mdm.index.decision.DecisionMakerStruct;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import com.sun.mdm.index.ops.DBAdapter;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.page.AssumedMatchPageAdapter;
import com.sun.mdm.index.page.PageAdapter;
import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.JNDINames;
import javax.naming.Context;
import javax.naming.InitialContext;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/** Data access class for potential assumedMatches
 * @author Dan Cidon
 */
public class AssumedMatchManager {
    
    /** Insert SQL
     */    
    private static final String INSERT_CLAUSE =
    "INSERT into SBYN_ASSUMEDMATCH (ASSUMEDMATCHID,EUID,"
    + "SYSTEMCODE,LID,WEIGHT,TRANSACTIONNUMBER) "
    + "VALUES (?,?,?,?,?,?)";
    
    /** Select SQL
     */    
    private static final String BASE_SELECT_CLAUSE =
    "SELECT " 
    + "a.ASSUMEDMATCHID, "
    + "a.WEIGHT, "
    + "a.EUID, "
    + "b.SYSTEMUSER, "
    + "b.TIMESTAMP, "
    + "a.SYSTEMCODE, "
    + "a.LID, "
    + "a.TRANSACTIONNUMBER "
    + "FROM SBYN_ASSUMEDMATCH a, SBYN_TRANSACTION b "
    + "WHERE a.TRANSACTIONNUMBER=b.TRANSACTIONNUMBER ";    
    
    /** Delete SQL
     */
    private static final String DELETE_CLAUSE =
    "DELETE from SBYN_ASSUMEDMATCH WHERE ASSUMEDMATCHID=?";
    
    private static String mNumberConversion = null;     // convert a string to a number
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    
     
     /** Creates a new instance of AssumedMatchManager
      */
    public AssumedMatchManager( ){
       
    }
    
    /** Add assumed matches to the database
     *
     * @param dms Decision maker struct object
     * @param systemCode New system code
     * @param lid New LID
     * @param con Connection handle
     * @param transactionId Current transaction id
     * @exception AssumedMatchException An error occured.
     */
    public void addAssumedMatch(Connection con, DecisionMakerStruct dms, 
    String systemCode, String lid, String transactionId)
    throws AssumedMatchException {
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Creating assumed match: EUID(" + dms.euid + "), SO(" +
                             systemCode + ", " + lid + "), weight(" + dms.weight + ")");
            }
            PreparedStatement ps = con.prepareStatement(INSERT_CLAUSE);
            String id = CUIDManager.getNextUID(con,  "ASSUMEDMATCH" );
            ps.setString(1, id);
            ps.setString(2, dms.euid);
            ps.setString(3, systemCode);
            ps.setString(4, lid);
            ps.setString(5, "" + dms.weight);
            ps.setString(6, transactionId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new AssumedMatchException(e);
        }
    }
    
    /** Delete assumed match from the database
     *
     * @param con Connection handle
     * @param assumedMatchId Assumed match id to be deleted
     * @exception AssumedMatchException An error occured.
     */
    public void deleteAssumedMatch(Connection con, String assumedMatchId)
    throws AssumedMatchException {
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Deleting assumed match record id: " + assumedMatchId);
            }
            PreparedStatement ps = con.prepareStatement(DELETE_CLAUSE);
            ps.setString(1, assumedMatchId);
            int retVal = ps.executeUpdate();
            ps.close();
            if (retVal != 1) {
                throw new AssumedMatchException("Invalid number of rows deleted: "
                + retVal);
            }               
        } catch (Exception e) {
            throw new AssumedMatchException(e);
        }
    }    
    
    /** Search for assumed matches.
     * @param obj Search object.
     * @param con Connection handle
     * @exception AssumedMatchException An error occured.
     * @return Iterator of search result.
     */
    public AssumedMatchIterator lookupAssumedMatches(
    Connection con, AssumedMatchSearchObject obj)
    throws AssumedMatchException {
        PreparedStatement ps = null;
        ArrayList parameters = new ArrayList();
        StringBuffer sb = new StringBuffer(BASE_SELECT_CLAUSE);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Assumed match search object: " + obj);
        }
        try {
            if (obj.getEUIDs() != null) {
                StringBuffer euidList = new StringBuffer();
                String[] euids = obj.getEUIDs();
                for (int i = 0; i < euids.length; i++) {
                    if (i != 0) {
                        euidList.append(",");
                    }
                    euidList.append('\'').append(euids[i]).append('\'');
                }
                String euidListString = euidList.toString();
                sb.append(" and a.EUID in (").append(euidListString);
                sb.append(")");
            }
            if (obj.getSystemCode() != null) {
                sb.append(" and a.SYSTEMCODE=?");
                parameters.add(obj.getSystemCode());
            }
            if (obj.getAssumedMatchId() != null) {
                sb.append(" and a.ASSUMEDMATCHID=?");
                parameters.add(obj.getAssumedMatchId());
            }
            if (obj.getLID() != null) {
                sb.append(" and a.LID=?");
                parameters.add(obj.getLID());
            }
            if (obj.getCreateUser() != null) {
                sb.append(" and b.SYSTEMUSER=?");
                parameters.add(obj.getCreateUser());
            }
            if (obj.getCreateStartDate() != null) {
                sb.append(" and b.TIMESTAMP>=?");
                parameters.add(obj.getCreateStartDate());
            }
            if (obj.getCreateEndDate() != null) {
                sb.append(" and b.TIMESTAMP<=?");
                parameters.add(obj.getCreateEndDate());
            }
            if (mNumberConversion == null) {
                mNumberConversion = 
                    DBAdapter.getDBAdapterInstance().getVarcharToNumberConversion("a.WEIGHT");
            }
            sb.append(" order by " 
                      + mNumberConversion
                      + " desc, a.EUID asc");            
                       
            String sqlString = sb.toString();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Assumed match SQL search: " + sqlString);
            }
            ps = con.prepareStatement(sqlString);
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
        } catch (SQLException e) {
            throw new AssumedMatchException(e);
        } catch (OPSException e) {
            throw new AssumedMatchException(e);
        }            
        return lookupAssumedMatches(con, ps, obj);
    }
    
      
    /** Search for assumed matches
     * @param con Connection handle
     * @param ps Prepared statement to execute
     * @param searchObj Search object
     * @throws AssumedMatchException An error occured.
     * @return Iterator of search results
     */    
    private AssumedMatchIterator lookupAssumedMatches (
        Connection con, PreparedStatement ps, 
        AssumedMatchSearchObject searchObj)
    throws AssumedMatchException {
        try {
            ResultSet rs = ps.executeQuery();
            ArrayList assumedMatches = new ArrayList();
            int count = 0;
            int maxElements = searchObj.getMaxElements();
            while (rs.next()) {
                if (maxElements != 0 && count >= maxElements) {
                    break;
                }
                String id = rs.getString(1);
                String weight = rs.getString(2);
                String euid = rs.getString(3);
                String createUser = rs.getString(4);
                Date createDate = rs.getTimestamp(5);
                String systemCode = rs.getString(6);
                String lid = rs.getString(7);
                String transactionNumber = rs.getString(8);
                AssumedMatchSummary dupSum = new AssumedMatchSummary(id, euid,
                systemCode, lid, createUser, createDate, transactionNumber);
                dupSum.setWeight(Float.parseFloat(weight));
                assumedMatches.add(dupSum);
                count++;
            }
            rs.close();
            ps.close();
            
            //If result set is small enough, send all results to client without
            //going through PageData session bean
            AssumedMatchIterator retIterator = null;
            int pageSize = searchObj.getPageSize();
            PageAdapter adapter = new AssumedMatchPageAdapter(assumedMatches, searchObj );
             // disable PageData due to serializable bug 6198
            if (count < 2 * pageSize) {
                ArrayList summaryRecordList = new ArrayList();
                while (adapter.hasNext()) {
                    summaryRecordList.add(adapter.next());
                } 
                retIterator = new AssumedMatchIterator(summaryRecordList);
                
            } else {
                Context jndiContext = new InitialContext();
                PageData pd = (PageData)jndiContext.lookup(JNDINames.EJB_REF_PAGEDATA);
                pd.setPageAdapter(adapter);
                retIterator = new AssumedMatchIterator(pd, pageSize, count);
            }
                 
            return retIterator;
        } catch (Exception e) {
            throw new AssumedMatchException(e);
        }
    }    
    
}
