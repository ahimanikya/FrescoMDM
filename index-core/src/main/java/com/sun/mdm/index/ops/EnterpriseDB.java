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

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
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


/**
 * @author gzheng
 * OPS class for EnterpriseObject
 */
public final class EnterpriseDB extends ObjectPersistenceService {
    private static String mDeleteString = null;
    private static String mInsertString = null;
    private static String mSelectString = null;
    private static String mUpdateString = null;

    private transient final Localizer mLocalizer = Localizer.get();
    
    // SQL strings for JDBC operations.
    
    static {
        mSelectString =
              "       select \n" 
            + "               euid, \n" 
            + "               systemcode, \n" 
            + "               lid \n" 
            + "       from \n" 
            + "               sbyn_enterprise \n" 
            + "       where \n" 
            + "               euid = ? \n";

        mInsertString =
              "       insert into sbyn_enterprise \n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               systemcode, \n" 
            + "               lid \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               ? \n" 
            + "       ) \n";

        mUpdateString =
              "       update sbyn_enterprise \n" 
            + "       set \n" 
            + "               euid = ?, \n" 
            + "               systemcode = ?, \n" 
            + "               lid = ?, \n" 
            + "       where \n" 
            + "               euid = ? \n";

        mDeleteString =
              "       delete from sbyn_enterprise \n" 
            + "       where \n" 
            + "               euid = ? and \n" 
            + "               systemcode = ? and \n" 
            + "               lid = ? \n";
    }

    /** default constructor
     * @exception OPSException OPSException
     */
    public EnterpriseDB() throws OPSException {
        super();
    }

    /** Retrieves an EUID-SystemCode-LocalID association from database by its EUID.
     * @param conn JDBC connection.
     * @param opsmap EntityObject ops.
     * @param euid EUID.
     * @param sysdb SystemObjectDB instance to access the database.
     * @param sbrdb SystemSBRDB instance to access the database.
     * @throws OPSException if an error occurs.
     * @return EnterpriseObject
     */
    public EnterpriseObject get(Connection conn, HashMap opsmap, String euid,
                                SystemObjectDB sysdb, SystemSBRDB sbrdb) 
            throws OPSException {
                
        EnterpriseObject ret = null;

        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            stmt = getStatement(mSelectString, conn);
            stmt.setString(1, euid);

            rset = stmt.executeQuery();

            while (rset.next()) {
                if (ret == null) {
                    ret = new EnterpriseObject();
                    ret.setEUID(euid);

                    SBR sbrret = sbrdb.get(conn, opsmap, euid);
                    ret.addChild(sbrret);
                }

                String systemcode = rset.getString("SYSTEMCODE");
                String lid = rset.getString("LID");

                SystemObject so = sysdb.get(conn, opsmap, systemcode, lid);

                ret.addChild(so);
            }

            if (ret != null) {
                ret.resetAll();
            }

        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params = addobject(params, euid);

            String sql = sql2str(mSelectString, params);
            throw new OPSException(mLocalizer.t("OPS501: Could not retrieve " + 
                                        "an EUID-SystemCode-LocalID association " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS502: Could not retrieve " + 
                                        "an EnterpriseObject: {0}", e));
        } catch (OPSException e) {
            throw new OPSException(mLocalizer.t("OPS503: Could not retrieve " + 
                                        "an EnterpriseObject: {0}", e));
        } finally {
        	try {
        	    if (rset != null) {
        	        rset.close();
        	    }
        	    
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS504: Could not close " + 
                                        "an SQL statement or result set: {0}", e));
            }
        }

        return ret;
    }

    /** Persists a new EUID-SystemObject-LocalID association into database.
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param systemcode System Code.
     * @param lid Local ID.
     * @throws OPSException if an error occurs.
     */
    public void create(Connection conn, String euid, String systemcode,
                       String lid) throws OPSException {
                        
        // executes insert SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mInsertString, conn);
            stmt.setString(1, euid);
            stmt.setString(2, systemcode);
            stmt.setString(3, lid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params = addobject(params, euid);
            params = addobject(params, systemcode);
            params = addobject(params, lid);

            String sql = sql2str(mInsertString, params);
            throw new OPSException(mLocalizer.t("OPS505: Could not create " + 
                                        "an EUID-SystemCode-LocalID association " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS506: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /** Removes an existing EUID-SystemCode-LocalID association from database.
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param systemcode System Code.
     * @param lid Local ID.
     * @throws OPSException if an error occurs.
     */
    public void remove(Connection conn, String euid, String systemcode,
                       String lid) throws OPSException {

        // executes delete SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mDeleteString, conn);
            stmt.setString(1, euid);
            stmt.setString(2, systemcode);
            stmt.setString(3, lid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params = addobject(params, euid);
            params = addobject(params, systemcode);
            params = addobject(params, lid);

            String sql = sql2str(mDeleteString, params);
            throw new OPSException(mLocalizer.t("OPS507: Could not delete " + 
                                        "an EUID-SystemCode-LocalID association " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS508: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }

    /** Updates an existing EUID-SystemCode-LocalID association in the database
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param systemcode System Code.
     * @param lid Local ID.
     * @throws OPSException if an error occurs.
     */
    public void update(Connection conn, String euid, String systemcode,
                       String lid) throws OPSException {

        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mUpdateString, conn);
            stmt.setString(1, euid);
            stmt.setString(2, systemcode);
            stmt.setString(3, lid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params = addobject(params, euid);
            params = addobject(params, systemcode);
            params = addobject(params, lid);

            String sql = sql2str(mUpdateString, params);
            throw new OPSException(mLocalizer.t("OPS509: Could not update " + 
                                        "an EUID-SystemCode-LocalID association " + 
                                        "with this SQL statement: {0}: {1}", 
                                        sql, e));
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException(mLocalizer.t("OPS519: Could not close " + 
                                        "an SQL statement: {0}", e));
            }
        }
    }
}
