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

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * @author gzheng
 * OPS class for SystemObject records
 */
public final class SystemObjectDB extends ObjectPersistenceService {
    private static String mDeleteString;
    private static String mInsertString;
    private static String mSelectString;
    private static String mUpdateString;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    
    static {
        mSelectString =
              "       select \n" 
            + "               SYSTEMCODE, \n" 
            + "               LID, \n" 
            + "               CHILDTYPE, \n" 
            + "               CREATEUSER, \n" 
            + "               CREATEFUNCTION, \n" 
            + "               CREATEDATE, \n" 
            + "               UPDATEUSER, \n" 
            + "               UPDATEFUNCTION, \n" 
            + "               UPDATEDATE, \n" 
            + "               STATUS \n" 
            + "       from \n" 
            + "               SBYN_SYSTEMOBJECT \n" 
            + "       where \n" 
            + "               SYSTEMCODE = ? and \n" 
            + "               LID = ? \n";

        mInsertString =
              "       insert into SBYN_SYSTEMOBJECT         \n" 
            + "       ( \n" 
            + "               SYSTEMCODE, \n" 
            + "               LID, \n" 
            + "               CHILDTYPE, \n" 
            + "               CREATEUSER, \n" 
            + "               CREATEFUNCTION, \n" 
            + "               CREATEDATE, \n" 
            + "               UPDATEUSER, \n" 
            + "               UPDATEFUNCTION, \n" 
            + "               UPDATEDATE, \n" 
            + "               STATUS \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?  \n" 
            + "       ) \n";

        mUpdateString =
              "       update SBYN_SYSTEMOBJECT         \n" 
            + "       set \n" 
            + "               SYSTEMCODE = ?, \n" 
            + "               LID = ?, \n" 
            + "               CHILDTYPE = ?, \n" 
            + "               CREATEUSER = ?, \n" 
            + "               CREATEFUNCTION = ?, \n" 
            + "               CREATEDATE = ?, \n" 
            + "               UPDATEUSER = ?, \n" 
            + "               UPDATEFUNCTION = ?, \n" 
            + "               UPDATEDATE = ?, \n" 
            + "               STATUS = ? \n" 
            + "       where \n" 
            + "               SYSTEMCODE = ? and \n" 
            + "               LID = ? \n";

        mDeleteString =
              "       delete from SBYN_SYSTEMOBJECT         \n" 
            + "       where \n" 
            + "               SYSTEMCODE = ? and \n" 
            + "               LID = ? \n";
    }

    /**
     * default constructor
     *
     * @throws OPSException if an error occurred.
     */
    public SystemObjectDB() throws OPSException {
        super();
    }

    /**
     * Retrieves SystemObject record and its children from database.
     *
     * @param conn JDBC connection.
     * @param opsmap Map of entity ops handles.
     * @param systemcode System code.
     * @param lid Local ID.
     * @throws OPSException if an error occurred.
     * @return SystemObject
     */
    public SystemObject get(Connection conn, HashMap opsmap, 
                            String systemcode, String lid) throws OPSException {
        SystemObject ret = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Retrieving SystemObject with system code: " + systemcode + ", LID: " + lid );
            }
            stmt = getStatement(mSelectString, conn);
            ArrayList alist = null;
            stmt.setString(1, systemcode);
            stmt.setString(2, lid);

            rset = stmt.executeQuery();

            while (rset.next()) {
                if (null == alist) {
                    alist = new ArrayList();
                }

                SystemObject sysobj = new SystemObject();
                sysobj.setSystemCode(rset.getString("SYSTEMCODE"));
                sysobj.setLID(rset.getString("LID"));
                sysobj.setChildType(rset.getString("CHILDTYPE"));
                sysobj.setCreateUser(rset.getString("CREATEUSER"));
                sysobj.setCreateFunction(rset.getString("CREATEFUNCTION"));
                java.sql.Timestamp t = rset.getTimestamp("CREATEDATE");
                if (t != null) {
                    sysobj.setCreateDateTime(new Date(t.getTime()));
                }
                sysobj.setUpdateUser(rset.getString("UPDATEUSER"));
                sysobj.setUpdateFunction(rset.getString("UPDATEFUNCTION"));
                t = rset.getTimestamp("UPDATEDATE");
                if (t != null) {
                    sysobj.setUpdateDateTime(new Date(t.getTime()));
                }
                sysobj.setValue("Status", (rset.getString("STATUS")));

                EntityOPS enops = (EntityOPS) opsmap.get(sysobj.getChildType() + "DB");
                String[] keys = new String[2];
                keys[0] = systemcode;
                keys[1] = lid;

                ArrayList objlist = enops.get(conn, opsmap, keys);

                if (null != objlist) {
                    ObjectNode childobj = (ObjectNode) objlist.get(0);
                    sysobj.setObject(childobj);
                }

                alist.add(sysobj);
            }

            if (null != alist) {
                ret = (SystemObject) alist.get(0);
                ret.resetAll();
            }

        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params = addobject(params, systemcode);
            params = addobject(params, lid);

            String sql = sql2str(mSelectString, params);
            throw new OPSException(mLocalizer.t("OPS599: Could not retrieve " + 
                                        "a SystemObject from the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS600: Could not retrieve " + 
                                        "a SystemObject from the database: {0}", e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS601: Could not retrieve " + 
                                        "a SystemObject from the database: {0}", e));
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS602: Could not close " + 
                                        "an SQL statement or result set: {0}", e));
            }
        }
        return ret;
    }

    /**
     * Persists a new SystemObject and its children records into database.
     *
     * @param conn JDBC connection.
     * @param opsmap Map of entity ops handles.
     * @param sysobj SystemObject to persist.
     * @throws OPSException if an error occurred.
     */
    public void create(Connection conn, HashMap opsmap, SystemObject sysobj)
            throws OPSException {
                
        // executes insert SQL statement
        PreparedStatement stmt = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Creating SystemObject with system code: " + sysobj.getSystemCode() + ", LID: " + sysobj.getLID());
            }

            stmt = getStatement(mInsertString, conn);
            setParam(stmt, 1, "String", sysobj.getSystemCode());
            setParam(stmt, 2, "String", sysobj.getLID());
            setParam(stmt, 3, "String", sysobj.getChildType());
            setParam(stmt, 4, "String", sysobj.getCreateUser());
            setParam(stmt, 5, "String", sysobj.getCreateFunction());

            if (null != sysobj.getCreateDateTime()) {
                setParam(stmt, 6, "Timestamp",
                    new java.sql.Date(sysobj.getCreateDateTime().getTime()));
            } else {
                setParam(stmt, 6, "Date", null);
            }

            setParam(stmt, 7, "String", sysobj.getUpdateUser());
            setParam(stmt, 8, "String", sysobj.getUpdateFunction());

            if (null != sysobj.getUpdateDateTime()) {
                setParam(stmt, 9, "Timestamp",
                    new java.sql.Date(sysobj.getUpdateDateTime().getTime()));
            } else {
                setParam(stmt, 9, "Date", null);
            }

            setParam(stmt, 10, "String", sysobj.getStatus());

            stmt.executeUpdate();

            ObjectNode child = (ObjectNode) sysobj.getObject();

            if (null != child) {
                EntityOPS enops = (EntityOPS) opsmap.get(child.pGetTag() + "DB");
                String[] keys = new String[2];
                keys[0] = sysobj.getSystemCode();
                keys[1] = sysobj.getLID();
                enops.create(conn, opsmap, keys, child);
            }
            
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, sysobj.getSystemCode());
                params = addobject(params, sysobj.getLID());
                params = addobject(params, sysobj.getChildType());
                params = addobject(params, sysobj.getCreateUser());
                params = addobject(params, sysobj.getCreateFunction());

                if (null != sysobj.getCreateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sysobj.getCreateDateTime()
                                                     .getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sysobj.getUpdateUser());
                params = addobject(params, sysobj.getUpdateFunction());

                if (null != sysobj.getUpdateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sysobj.getUpdateDateTime()
                                                     .getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sysobj.getStatus());

                String sql = sql2str(mInsertString, params);
                throw new OPSException(mLocalizer.t("OPS603: Could not persist " + 
                                        "a SystemObject into the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS604: Could not persist " + 
                                        "a SystemObject into the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS605: Could not persist " + 
                                        "a SystemObject into the database: {0}", e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS606: Could not persist " + 
                                        "a SystemObject into the database: {0}", e));
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS607: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /**
     * Removes an existing SystemObject and its children records from database.
     *
     * @param conn JDBC connection.
     * @param opsmap Map of entity ops handles.
     * @param sysobj SystemObject to remove.
     * @throws OPSException if an error occurred.
     */
    public void remove(Connection conn, HashMap opsmap, SystemObject sysobj)
            throws OPSException {
                
        // executes delete SQL statement
        PreparedStatement stmt = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Removing SystemObject with system code: " + sysobj.getSystemCode() 
                    + ", LID: " + sysobj.getLID());
            }

            ObjectNode child = (ObjectNode) sysobj.getObject();

            if (null != child) {
                EntityOPS enops = (EntityOPS) opsmap.get(child.pGetTag() + "DB");
                enops.remove(conn, opsmap, child);
            }

            stmt = getStatement(mDeleteString, conn);
            stmt.setString(1, sysobj.getSystemCode());
            stmt.setString(2, sysobj.getLID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            String sqlerr = e.getMessage();

            try {
                ArrayList params = new ArrayList();
                params = addobject(params, sysobj.getSystemCode());
                params = addobject(params, sysobj.getLID());

                String sql = sql2str(mDeleteString, params);
                throw new OPSException(mLocalizer.t("OPS608: Could not remove " + 
                                        "a SystemObject from the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS609: Could not remove " + 
                                        "a SystemObject from the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS610: Could not remove " + 
                                        "a SystemObject from the database: {0}", e)); 
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS611: Could not remove " + 
                                        "a SystemObject from the database: {0}", e)); 
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS612: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /**
     * Updates an existing SystemObject and its chidlren records in the database.
     *
     * @param conn JDBC connection.
     * @param opsmap Map of entity ops handles.
     * @param sysobj SystemObject to update.
     * @throws OPSException if an error occurred.
     */
    public void update(Connection conn, HashMap opsmap, SystemObject sysobj)
            throws OPSException {
                
        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Updating SystemObject with system code: " + 
                             sysobj.getSystemCode() + ", LID: " + sysobj.getLID());
            }
            if (!sysobj.isAdded()) {
                if (sysobj.isUpdated()) {
                    stmt = getStatement(mUpdateString, conn);
                    setParam(stmt, 1, "String", sysobj.getSystemCode());
                    setParam(stmt, 2, "String", sysobj.getLID());
                    setParam(stmt, 3, "String", sysobj.getChildType());
                    setParam(stmt, 4, "String", sysobj.getCreateUser());
                    setParam(stmt, 5, "String", sysobj.getCreateFunction());

                    if (null != sysobj.getCreateDateTime()) {
                        setParam(stmt, 6, "Timestamp",
                            new java.sql.Date(sysobj.getCreateDateTime()
                                                     .getTime()));
                    } else {
                        setParam(stmt, 6, "Date", null);
                    }

                    setParam(stmt, 7, "String", sysobj.getUpdateUser());
                    setParam(stmt, 8, "String", sysobj.getUpdateFunction());

                    if (null != sysobj.getUpdateDateTime()) {
                        setParam(stmt, 9, "Timestamp",
                            new java.sql.Date(sysobj.getUpdateDateTime()
                                                     .getTime()));
                    } else {
                        setParam(stmt, 9, "Date", null);
                    }

                    setParam(stmt, 10, "String", sysobj.getStatus());
                    setParam(stmt, 11, "String", sysobj.getSystemCode());
                    setParam(stmt, 12, "String", sysobj.getLID());

                    stmt.executeUpdate();
                    sysobj.setUpdateFlag(false);
                } else if (sysobj.isRemoved()) {
                    this.remove(conn, opsmap, sysobj);
                }

                if (!sysobj.isRemoved()) {
                    ObjectNode child = (ObjectNode) sysobj.getObject();
                    
                    if (null != child) {
                        EntityOPS enops = (EntityOPS) opsmap.get(child.pGetTag() + "DB");
                        String[] keys = new String[2];
                        keys[0] = sysobj.getSystemCode();
                        keys[1] = sysobj.getLID();
                        enops.update(conn, opsmap, keys, child);
                    }
                }
            } else {
                this.create(conn, opsmap, sysobj);
            }

        } catch (SQLException e) {
            String sqlerr = e.getMessage();

            try {
                ArrayList params = new ArrayList();

                params = addobject(params, sysobj.getSystemCode());
                params = addobject(params, sysobj.getLID());
                params = addobject(params, sysobj.getChildType());
                params = addobject(params, sysobj.getCreateUser());
                params = addobject(params, sysobj.getCreateFunction());

                if (null != sysobj.getCreateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sysobj.getCreateDateTime()
                                                     .getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sysobj.getUpdateUser());
                params = addobject(params, sysobj.getUpdateFunction());

                if (null != sysobj.getUpdateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sysobj.getUpdateDateTime()
                                                     .getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sysobj.getStatus());
                params.add(sysobj.getSystemCode());
                params.add(sysobj.getLID());

                String sql = sql2str(mUpdateString, params);

                throw new OPSException(mLocalizer.t("OPS613: Could not update " + 
                                        "a SystemObject in the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS614: Could not update " + 
                                        "a SystemObject in the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS615: Could not update " + 
                                        "a SystemObject in the database: {0}", e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS616: Could not update " + 
                                        "a SystemObject in the database: {0}", e));
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS617: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
}
