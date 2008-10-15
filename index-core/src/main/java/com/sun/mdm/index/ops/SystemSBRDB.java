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
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * @author gzheng
 * OPS class for SystemSBR records
 */
public final class SystemSBRDB extends ObjectPersistenceService {
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    private static String mDeleteString;
    private static String mInsertString;
    private static String mSelectString;
    private static String mUpdateString;

    static {
        mSelectString =
              "       SELECT \n" 
            + "               EUID, \n" 
            + "               CHILDTYPE, \n" 
            + "               CREATESYSTEM, \n" 
            + "               CREATEUSER, \n" 
            + "               CREATEFUNCTION, \n" 
            + "               CREATEDATE, \n" 
            + "               UPDATESYSTEM, \n" 
            + "               UPDATEUSER, \n" 
            + "               UPDATEFUNCTION, \n" 
            + "               UPDATEDATE, \n" 
            + "               REVISIONNUMBER, \n" 
            + "               STATUS \n"
            + "       FROM \n" 
            + "               SBYN_SYSTEMSBR \n" 
            + "       WHERE \n" 
            + "               EUID = ? \n";

        mInsertString =
              "       INSERT INTO SBYN_SYSTEMSBR         \n" 
            + "       ( \n" 
            + "               EUID, \n" 
            + "               CHILDTYPE, \n" 
            + "               CREATESYSTEM, \n" 
            + "               CREATEUSER, \n" 
            + "               CREATEFUNCTION, \n" 
            + "               CREATEDATE, \n" 
            + "               UPDATESYSTEM, \n" 
            + "               UPDATEUSER, \n" 
            + "               UPDATEFUNCTION, \n" 
            + "               UPDATEDATE, \n"
            + "               REVISIONNUMBER, \n"
            + "               STATUS \n" 
            + "       ) \n" 
            + "       VALUES \n" 
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
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ?  \n" 
            + "       ) \n";

        mUpdateString =
              "       UPDATE SBYN_SYSTEMSBR         \n" 
            + "       SET \n" 
            + "               EUID = ?, \n" 
            + "               CHILDTYPE = ?, \n" 
            + "               CREATESYSTEM = ?, \n" 
            + "               CREATEUSER = ?, \n" 
            + "               CREATEFUNCTION = ?, \n" 
            + "               CREATEDATE = ?, \n" 
            + "               UPDATESYSTEM = ?, \n" 
            + "               UPDATEUSER = ?, \n" 
            + "               UPDATEFUNCTION = ?, \n" 
            + "               UPDATEDATE = ?, \n" 
            + "               REVISIONNUMBER = ?, \n" 
            + "               STATUS = ? \n" 
            + "       WHERE \n" 
            + "               EUID = ? \n";

        mDeleteString =
              "       DELETE FROM SBYN_SYSTEMSBR         \n" 
            + "       WHERE \n" 
            + "               EUID = ? \n";
    }

    
    /**
     * default constructor
     *
     * @throws OPSException if an error occurred.
     */
    public SystemSBRDB() throws OPSException {
        super();
    }

    /**
     * Retrieves SystemSBR and its children records from database by EUID
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @throws OPSException if an error occurred.
     * @return SBR record
     */
    public SBR get(Connection conn, HashMap opsmap, String euid)
        throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Retrieving SBR for EUID: " + euid );
        }

        SBR sbrobj = null;

        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            stmt = getStatement(mSelectString, conn);
            stmt.setString(1, euid);

            rset = stmt.executeQuery();

            while (rset.next()) {
                sbrobj = new SBR();
                sbrobj.setChildType(rset.getString("CHILDTYPE"));
                sbrobj.setCreateSystem(rset.getString("CREATESYSTEM"));
                sbrobj.setCreateUser(rset.getString("CREATEUSER"));
                sbrobj.setCreateFunction(rset.getString("CREATEFUNCTION"));
                sbrobj.setCreateDateTime((java.util.Date) rset.getTimestamp(
                        "CREATEDATE"));
                sbrobj.setUpdateSystem(rset.getString("UPDATESYSTEM"));
                sbrobj.setUpdateUser(rset.getString("UPDATEUSER"));
                sbrobj.setUpdateFunction(rset.getString("UPDATEFUNCTION"));
                sbrobj.setUpdateDateTime((java.util.Date) rset.getTimestamp("UPDATEDATE"));
                sbrobj.setRevisionNumber(rset.getInt("REVISIONNUMBER"));
                sbrobj.setStatus(rset.getString("STATUS"));

                EntityOPS enops = (EntityOPS) opsmap.get(sbrobj.getChildType() + "SBRDB");
                String[] keys = new String[1];
                keys[0] = euid;

                ArrayList childlist = enops.get(conn, opsmap, keys);

                if (null != childlist) {
                    ObjectNode childobj = (ObjectNode) childlist.get(0);
                    sbrobj.setObject(childobj);
                }

                SBROverWriteDB owdb = new SBROverWriteDB();
                childlist = owdb.get(conn, null, euid);
                if (null != childlist) {
                    sbrobj.addOverWrites(childlist);
                }

                sbrobj.resetAll();
            }

            stmt.close();
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(euid);

            String sql = sql2str(mSelectString, params);
            throw new OPSException(mLocalizer.t("OPS618: Could not update " + 
                                        "a SystemSBR record in the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS619: Could not update " + 
                                        "a SystemSBR record in the database: {0}", 
                                        e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS620: Could not update " + 
                                        "a SystemSBR record in the database: {0}", 
                                        e));
        } finally {
        	try {
        	    if (rset != null) {
        	        rset.close();
        	    }
        	    
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS621: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
        return sbrobj;
    }

    /**
     * Persists a new SystemSBR record and its children into database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param sbr SBR record to persist.
     * @throws OPSException if an error occurred.
     */
    public void create(Connection conn, HashMap opsmap, String euid, SBR sbr)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Creating SBR for EUID: " + euid);
        }

        // executes insert SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mInsertString, conn);
            setParam(stmt, 1, "String", euid);
            setParam(stmt, 2, "String", sbr.getChildType());
            setParam(stmt, 3, "String", sbr.getCreateSystem());
            setParam(stmt, 4, "String", sbr.getCreateUser());
            setParam(stmt, 5, "String", sbr.getCreateFunction());

            if (null != sbr.getCreateDateTime()) {
                setParam(stmt, 6, "Timestamp",
                    new java.sql.Date(sbr.getCreateDateTime().getTime()));
            } else {
                setParam(stmt, 6, "Date", null);
            }

            setParam(stmt, 7, "String", sbr.getUpdateSystem());
            setParam(stmt, 8, "String", sbr.getUpdateUser());
            setParam(stmt, 9, "String", sbr.getUpdateFunction());

            if (null != sbr.getUpdateDateTime()) {
                setParam(stmt, 10, "Timestamp",
                    new java.sql.Date(sbr.getUpdateDateTime().getTime()));
            } else {
                setParam(stmt, 10, "Date", null);
            }
            setParam(stmt, 11, "Integer", sbr.getRevisionNumber());
            setParam(stmt, 12, "String", sbr.getStatus());

            stmt.executeUpdate();

            ObjectNode childobj = (ObjectNode) sbr.getObject();

            if (null != childobj) {
                EntityOPS enops = (EntityOPS) opsmap.get(childobj.pGetTag() + "SBRDB");
                String[] keys = new String[1];
                keys[0] = euid;
                enops.create(conn, opsmap, keys, childobj);
            }
            
            ArrayList owlist = sbr.getOverWrites();
            if (null != owlist) {
                SBROverWriteDB owdb = new SBROverWriteDB();
                for (int i = 0; i < owlist.size(); i++) {
                    SBROverWrite ow = (SBROverWrite) owlist.get(i);
                    owdb.create(conn, null, euid, ow);
                }
            }

            stmt.close();
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, euid);
                params = addobject(params, sbr.getChildType());
                params = addobject(params, sbr.getCreateSystem());
                params = addobject(params, sbr.getCreateUser());
                params = addobject(params, sbr.getCreateFunction());

                if (null != sbr.getCreateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sbr.getCreateDateTime().getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sbr.getUpdateSystem());
                params = addobject(params, sbr.getUpdateUser());
                params = addobject(params, sbr.getUpdateFunction());

                if (null != sbr.getUpdateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sbr.getUpdateDateTime().getTime()));
                } else {
                    params = addobject(params, null);
                }
                params = addobject(params, sbr.getRevisionNumber());
                params = addobject(params, sbr.getStatus());

                String sql = sql2str(mInsertString, params);
                throw new OPSException(mLocalizer.t("OPS622: Could not create " + 
                                        "a SystemSBR record in the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS623: Could not create " + 
                                        "a SystemObject in the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS624: Could not create " + 
                                        "a SystemSBR record in the database: {0}", 
                                        e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS625: Could not create " + 
                                        "a SystemSBR record in the database: {0}", 
                                        e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS626: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /**
     * Removes an existing SystemSBR and its children records from database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param sbr SBR record
     * @throws OPSException if an error occurred.
     */
    public void remove(Connection conn, HashMap opsmap, String euid, SBR sbr)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Removing SBR for EUID: " + euid);
        }

        // executes delete SQL statement
        PreparedStatement stmt = null;
        try {
            ObjectNode childobj = (ObjectNode) sbr.getObject();

            if (null != childobj) {
                EntityOPS enops = (EntityOPS) opsmap.get(childobj.pGetTag() + "SBRDB");
                enops.remove(conn, opsmap, childobj);
            }
            
            ArrayList owlist = sbr.getOverWrites();
            if (null != owlist) {
                SBROverWriteDB owdb = new SBROverWriteDB();
                for (int i = 0; i < owlist.size(); i++) {
                    SBROverWrite ow = (SBROverWrite) owlist.get(i);
                    owdb.remove(conn, opsmap, euid, ow);
                }
            }
            

            stmt = getStatement(mDeleteString, conn);
            stmt.setString(1, euid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(euid);

            String sql = sql2str(mDeleteString, params);
            throw new OPSException(mLocalizer.t("OPS627: Could not create " + 
                                        "a SystemSBR record in the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS628: Could not create " + 
                                        "a SystemSBR record in the database: {0}", 
                                        e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS629: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /**
     * Updates an existing SystemSBR and its children records in the database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param sbr SBR record
     * @throws OPSException if an error occurred.
     */
    public void update(Connection conn, HashMap opsmap, String euid, SBR sbr)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Updating SBR for EUID: " + euid);
        }

        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            if (!sbr.isAdded()) {
                if (sbr.isUpdated()) {
                    stmt = getStatement(mUpdateString, conn);
                    setParam(stmt, 1, "String", euid);
                    setParam(stmt, 2, "String", sbr.getChildType());
                    setParam(stmt, 3, "String", sbr.getCreateSystem());
                    setParam(stmt, 4, "String", sbr.getCreateUser());
                    setParam(stmt, 5, "String", sbr.getCreateFunction());

                    if (null != sbr.getCreateDateTime()) {
                        setParam(stmt, 6, "Timestamp",
                            new java.sql.Date(sbr.getCreateDateTime().getTime()));
                    } else {
                        setParam(stmt, 6, "Date", null);
                    }

                    setParam(stmt, 7, "String", sbr.getUpdateSystem());
                    setParam(stmt, 8, "String", sbr.getUpdateUser());
                    setParam(stmt, 9, "String", sbr.getUpdateFunction());

                    if (null != sbr.getUpdateDateTime()) {
                        setParam(stmt, 10, "Timestamp",
                            new java.sql.Date(sbr.getUpdateDateTime().getTime()));
                    } else {
                        setParam(stmt, 10, "Date", null);
                    }

                    setParam(stmt, 11, "String", sbr.getRevisionNumber());
                    setParam(stmt, 12, "String", sbr.getStatus());
                    setParam(stmt, 13, "String", euid);

                    stmt.executeUpdate();
                    sbr.setUpdateFlag(false);
                    stmt.close();
                } else if (sbr.isRemoved()) {
                    this.remove(conn, opsmap, euid, sbr);
                }

                if (!sbr.isRemoved()) {
                    ArrayList list = sbr.pGetChildren();

                    if (null != list) {
                        for (int i = 0; i < list.size(); i++) {
                            ObjectNode childobj = (ObjectNode) list.get(i);
                            if (childobj.pGetTag().equals("SBROverWrite")) {
                                SBROverWriteDB owdb = new SBROverWriteDB();
                                owdb.update(conn, opsmap, euid, (SBROverWrite) childobj);
                            } else {
                                EntityOPS enops = (EntityOPS) opsmap.get(childobj.pGetTag() + "SBRDB");
                                String[] keys = new String[1];
                                keys[0] = euid;
                                enops.update(conn, opsmap, keys, childobj);
                            }
                            
                        }
                    }
                }
            } else {
                this.create(conn, opsmap, euid, sbr);
            }
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, euid);
                params = addobject(params, sbr.getChildType());
                params = addobject(params, sbr.getCreateSystem());
                params = addobject(params, sbr.getCreateUser());
                params = addobject(params, sbr.getCreateFunction());

                if (null != sbr.getCreateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sbr.getCreateDateTime().getTime()));
                } else {
                    params = addobject(params, null);
                }

                params = addobject(params, sbr.getUpdateSystem());
                params = addobject(params, sbr.getUpdateUser());
                params = addobject(params, sbr.getUpdateFunction());

                if (null != sbr.getUpdateDateTime()) {
                    params = addobject(params,
                            new java.sql.Date(sbr.getUpdateDateTime().getTime()));
                } else {
                    params = addobject(params, null);
                }
                params = addobject(params, sbr.getRevisionNumber());
                params = addobject(params, sbr.getStatus());

                params = addobject(params, euid);

                String sql = sql2str(mUpdateString, params);
                throw new OPSException(mLocalizer.t("OPS630: Could not update " + 
                                        "a SystemSBR record in the database " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS631: Could not create " + 
                                        "a SystemObject in the database " + 
                                        "due to an SQL error: {0}: {1}", 
                                        e, oe));
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS632: Could not update " + 
                                        "a SystemSBR record in the database: {0}", e)); 
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS633: Could not update " + 
                                        "a SystemSBR record in the database: {0}", e)); 
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS634: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
}
