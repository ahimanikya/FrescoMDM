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
package com.sun.mdm.index.audit;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.idgen.CUIDManager;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.page.PageAdapter;
import com.sun.mdm.index.page.ArrayPageAdapter;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.ops.DBAdapter;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.util.JNDINames;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/** Class to perform audit-related SQL operations
 * @author sdua
 */
public class AuditManager {
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of AuditManager
     *
     */
    public AuditManager() {
       
    }


    /**
     * Add audit data to the database. 
     *
     * @param con Database connection handle.
     * @param auditObject auditObject.
     * @throws UserException if a user error is encountered.
     * @throws AuditException if any other type of error is encountered.
     */
    public void insertAuditLog(Connection con, AuditDataObject auditObject)
        throws AuditException, UserException {
        try {
            //Filter out all duplicates that are already in the db
            String sql = DBAdapter.getDBAdapterInstance().getAuditMgrInsertStmt();            
            PreparedStatement ps = con.prepareStatement(sql);
            java.sql.Date date = auditObject.getCreateDate();
            String auditId = CUIDManager.getNextUID(con, "AUDIT");

            ps.setString(1, auditId);
            ps.setString(2, auditObject.getPrimaryObjectType());
            ps.setString(3, auditObject.getEUID1());
            ps.setString(4, auditObject.getEUID2());
            ps.setString(5, auditObject.getFunction());
            ps.setString(6, auditObject.getDetail());
            ps.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
            ps.setString(8, auditObject.getCreateUser());

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("AuditObject: " + auditObject);
            }

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new AuditException(mLocalizer.t("AUD500: Audit Log record could not be inserted: {0}", e));
        }
    }


    /** retrieve audit data from the audit table
     *
     * @param con Database connection handle.
     * @param obj AuditSearchObject that contains search criteria.
     * @throws AuditException if an error is encountered.
     * @return AuditIterator object to iterate through audit records.
     */
    public AuditIterator lookupAuditLog(Connection con, AuditSearchObject obj)
        throws AuditException {
        PreparedStatement ps = null;
        ArrayList parameters = new ArrayList();
        StringBuffer sb;
        try {
            sb = new StringBuffer (DBAdapter.getDBAdapterInstance().getAuditMgrSelectStmt());
        } catch (Exception e) {
            throw new AuditException(mLocalizer.t("AUD501: Could not retrieve the " + 
                                                  "SELECT SQL statement for " + 
                                                  "an Audit Log lookup: {0}", e));
        }
        boolean andFlag = false;
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("SearchObj: " + obj);
        }

        try {
            if (obj.getEUID() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append("  ( a.EUID = ? or a.EUID_aux = ? ) ");
                parameters.add(obj.getEUID());
                parameters.add(obj.getEUID());
            }

            if (obj.getPrimaryObjectType() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append(" a.primary_object_type=?");
                parameters.add(obj.getPrimaryObjectType());
            }

            if (obj.getFunction() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                try {
                    sb.append(DBAdapter.getDBAdapterInstance().getAuditMgrOperationColumnName());
                } catch (Exception e) {
                    throw new AuditException(mLocalizer.t("AUD502: Could not retrieve the " + 
                                                  "SQL column name for " + 
                                                  "an Audit Log lookup: {0}", e));
                }

                parameters.add(obj.getFunction());
            }

            if (obj.getCreateUser() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append("  a.create_by=?");
                parameters.add(obj.getCreateUser());
            }

            if (obj.getCreateStartDate() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append("  a.create_date>=?");
                parameters.add(obj.getCreateStartDate());
            }

            if (obj.getCreateEndDate() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append("  a.create_date<=?");
                parameters.add(obj.getCreateEndDate());
            }

            if (obj.getDetail() != null) {
                if (!andFlag) {
                    sb.append(" where ");
                    andFlag = true;
                } else {
                    sb.append(" and ");
                }

                sb.append("  detail like ?");
                parameters.add(obj.getDetail());
            }

            String sqlString = sb.toString();

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Executing: " + sqlString);
            }

            ps = con.prepareStatement(sqlString);

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
        } catch (Exception e) {
            throw new AuditException(mLocalizer.t("AUD503: Could not retrieve the " + 
                                                  "Audit Log records: {0}", e));
        }

        return lookupAuditLog(con, ps, obj);
    }

    /** Retrieve audit data from the audit table
     *
     * @param con Database connection handle.
     * @param ps  PreparedStatement to retrieve the data from the database.
     * @param searchObj AuditSearchObject that contains search criteria.
     * @throws AuditException if an error is encountered.
     * @return AuditIterator object to iterate through audit records.
     */
    private AuditIterator lookupAuditLog(Connection con, PreparedStatement ps,
                                         AuditSearchObject searchObj)
        throws AuditException {
        try {
            ResultSet rs = ps.executeQuery();
            ArrayList auditList = new ArrayList();
            int count = 0;
            int maxElements = searchObj.getMaxElements();

            while (rs.next()) {
                if ((count >= maxElements) && (maxElements != 0)) {
                    break;
                }

                String id = rs.getString(1);
                String type = rs.getString(2);
                String euid1 = rs.getString(3);
                String euid2 = rs.getString(4);
                String function = rs.getString(5);
                String detail = rs.getString(6);
                java.sql.Timestamp timeStamp = rs.getTimestamp(7);
                long time = timeStamp.getTime();
                java.sql.Date createDate = new java.sql.Date(time);
                String createUser = rs.getString(8);

                AuditDataObject auditObject = new AuditDataObject(id, type,
                        euid1, euid2, function, detail, createDate, createUser);

                auditList.add(auditObject);
                count++;
            }

            rs.close();
            ps.close();

            //If result set is small enough, send all results to client without
            //going through PageData session bean
            AuditIterator retIterator = null;
            int pageSize = searchObj.getPageSize();
            if ((count < (2 * pageSize)) || (pageSize == 0)) {
                retIterator = new AuditIterator(auditList);
            } else {
                PageAdapter adapter = new ArrayPageAdapter(auditList);
                Context jndiContext = new InitialContext();
                PageData pd = (PageData)jndiContext.lookup(JNDINames.EJB_REF_PAGEDATA);
                pd.setPageAdapter(adapter);
                retIterator = new AuditIterator(pd, pageSize, count);
            }
                 

            return retIterator;
        } catch (Exception e) {
            throw new AuditException(mLocalizer.t("AUD504: Could not look up " + 
                                                  "Audit Log records: {0}", e));
        }
    }
}
