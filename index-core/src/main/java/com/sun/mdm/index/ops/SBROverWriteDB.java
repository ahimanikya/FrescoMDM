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

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;

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
public final class SBROverWriteDB extends ObjectPersistenceService {
    private static String mDeleteString;
    private static String mInsertStringForInt;
    private static String mInsertStringForBool;
    private static String mInsertStringForString;
    private static String mInsertStringForByte;
    private static String mInsertStringForCharacter;    
    private static String mInsertStringForLong;
    private static String mInsertStringForDate;
    private static String mInsertStringForFloat;
    private static String mInsertStringForTimeStamp;
    private static String mSelectString;
    private static String mUpdateStringForInt;
    private static String mUpdateStringForBool;
    private static String mUpdateStringForString;
    private static String mUpdateStringForByte;
    private static String mUpdateStringForCharacter;
    private static String mUpdateStringForLong;
    private static String mUpdateStringForDate;
    private static String mUpdateStringForFloat;
    private static String mUpdateStringForTimeStamp;

    static {
        mSelectString =
              "       select \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_INT_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_BOOL_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_STRING_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_BYTE_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_LONG_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_DATE_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_FLOAT_STRING + "data, \n"
            + "               " + ObjectField.OBJECTMETA_TIMESTAMP_STRING + "data \n"
            + "       from \n" 
            + "               sbyn_overwrite \n" 
            + "       where \n" 
            + "               euid = ? \n";
            
        mInsertStringForInt =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_INT_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_INT_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForBool =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_BOOL_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_BOOL_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForString =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_STRING_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_STRING_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForByte =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_BYTE_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_BYTE_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        // We insert character type to byte type since we cannot change
        // create database script
        mInsertStringForCharacter =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_BYTE_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_CHAR_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";
        
        mInsertStringForLong =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_LONG_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_LONG_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForDate =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_DATE_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_DATE_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForFloat =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_FLOAT_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_FLOAT_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mInsertStringForTimeStamp =
              "       insert into sbyn_overwrite\n" 
            + "       ( \n" 
            + "               euid, \n" 
            + "               path, \n" 
            + "               type, \n" 
            + "               " + ObjectField.OBJECTMETA_TIMESTAMP_STRING + "data \n" 
            + "       ) \n" 
            + "       values \n" 
            + "       ( \n" 
            + "               ?, \n" 
            + "               ?, \n" 
            + "               '" + ObjectField.OBJECTMETA_TIMESTAMP_STRING + "', \n" 
            + "               ?  \n" 
            + "       ) \n";

        mUpdateStringForInt =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_INT_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForBool =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_BOOL_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForString =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_STRING_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForByte =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_BYTE_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        // We update character type to byte type since we cannot change
        // create database script
        mUpdateStringForCharacter =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_BYTE_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";
        
        mUpdateStringForLong =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_LONG_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForDate =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_DATE_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForFloat =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_FLOAT_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mUpdateStringForTimeStamp =
              "       update sbyn_overwrite         \n" 
            + "       set \n" 
            + "               " + ObjectField.OBJECTMETA_TIMESTAMP_STRING + "data = ? \n" 
            + "       where \n" 
            + "               euid = ? and \n"
            + "               path = ? \n";

        mDeleteString =
              "       delete from sbyn_overwrite         \n" 
            + "       where \n" 
            + "               euid = ? and  \n"
            + "               path = ?\n";
    }

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
    
    /**
     * default constructor
     *
     * @throws OPSException if an error occurs.
     */
    public SBROverWriteDB() throws OPSException {
        super();
    }

    /**
     * Retrieves SystemSBR and its children records from database by EUID.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @throws OPSException if an error occurs.
     * @return SBR record
     */
    public ArrayList get(Connection conn, HashMap opsmap, String euid)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Retrieving SBROverWrite for EUID: " + euid);
        }

        ArrayList owobj = null;

       PreparedStatement stmt = null;
       ResultSet rset = null;
       try {
            stmt = getStatement(mSelectString, conn);
            stmt.setString(1, euid);

            rset = stmt.executeQuery();

            while (rset.next()) {
                SBROverWrite ow = new SBROverWrite();
                ow.setPath(rset.getString("PATH"));
                String type = rset.getString("TYPE");
                // CHARACTERDATA has been mapped to BYTEDATA
                // in the SBYN_OVERWRITE table so that customers
                // do not need to run any "alter table" command
                // to upgrade.
                String typedata = null;
                if (type.equals("Character")) {
                	typedata = "BYTEDATA";
                } else {
                	typedata = type + "DATA";
                }                	
                ow.setData(this.getValue(rset, typedata, type));

                ow.resetAll();
                
                if (owobj == null) {
                    owobj = new ArrayList();
                }
                
                owobj.add(ow);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(euid);

            String sql = sql2str(mSelectString, params);
            throw new OPSException(sql + e.getMessage());
        } catch (ObjectException e) {
            throw new OPSException(e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        	    if (rset != null) {
        	        rset.close();
        	    }
        	    
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
        
        return owobj;
    }

    /**
     * Persists a new SystemSBR record and its children into database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param ow SBR overwrite object.
     * @throws OPSException if an error occurs.
     */
    public void create(Connection conn, HashMap opsmap, String euid, SBROverWrite ow)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Creating SBRObjectWrite for EUID: " + euid );
        }

        // executes insert SQL statement
        PreparedStatement stmt = null;
        try {
            String type = getSBROverWriteFieldType(ow);

            if (type.equals(ObjectField.OBJECTMETA_INT_STRING)) {
                stmt = getStatement(mInsertStringForInt, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_BOOL_STRING)) {
                stmt = getStatement(mInsertStringForBool, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_STRING_STRING)) {
                stmt = getStatement(mInsertStringForString, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_BYTE_STRING)) {
                stmt = getStatement(mInsertStringForByte, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_CHAR_STRING)) {
                stmt = getStatement(mInsertStringForCharacter, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_LONG_STRING)) {
                stmt = getStatement(mInsertStringForLong, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_DATE_STRING)) {
                stmt = getStatement(mInsertStringForDate, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_FLOAT_STRING)) {
                stmt = getStatement(mInsertStringForFloat, conn);
            } else if (type.equals(ObjectField.OBJECTMETA_TIMESTAMP_STRING)) {
                stmt = getStatement(mInsertStringForTimeStamp, conn);
            }
                                
            setParam(stmt, 1, "String", euid);
            setParam(stmt, 2, "String", ow.getPath());
            setParam(stmt, 3, type, ow.getData());

            stmt.executeUpdate();
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, euid);
                params = addobject(params, ow.getPath());
                params = addobject(params, ow.getData());

                String type = getSBROverWriteFieldType(ow);
                String sql = null;
                
                if (type.equals(ObjectField.OBJECTMETA_INT_STRING)) {
                    sql = sql2str(mInsertStringForInt, params);
                } else if (type.equals(ObjectField.OBJECTMETA_BOOL_STRING)) {
                    sql = sql2str(mInsertStringForBool, params);
                } else if (type.equals(ObjectField.OBJECTMETA_STRING_STRING)) {
                    sql = sql2str(mInsertStringForString, params);
                } else if (type.equals(ObjectField.OBJECTMETA_BYTE_STRING)) {
                    sql = sql2str(mInsertStringForByte, params);
                } else if (type.equals(ObjectField.OBJECTMETA_CHAR_STRING)) {
                    sql = sql2str(mInsertStringForCharacter, params);
                } else if (type.equals(ObjectField.OBJECTMETA_LONG_STRING)) {
                    sql = sql2str(mInsertStringForLong, params);
                } else if (type.equals(ObjectField.OBJECTMETA_DATE_STRING)) {
                    sql = sql2str(mInsertStringForDate, params);
                } else if (type.equals(ObjectField.OBJECTMETA_FLOAT_STRING)) {
                    sql = sql2str(mInsertStringForFloat, params);
                } else if (type.equals(ObjectField.OBJECTMETA_TIMESTAMP_STRING)) {
                    sql = sql2str(mInsertStringForTimeStamp, params);
                }    
                
                throw new OPSException(sql + e.getMessage());
            } catch (ObjectException oe) {
                throw new OPSException(oe.getMessage() + "\n" + sqlerr);
            }
        } catch (ObjectException e) {
            throw new OPSException(e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }

    /**
     * Removes an existing SystemSBR and its children records from database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param ow SBR overwrite object
     * @throws OPSException if an error occurs.
     */
    public void remove(Connection conn, HashMap opsmap, String euid, SBROverWrite ow)
            throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Removing SBROverWrite for EUID: " + euid);
        }

        // executes delete SQL statement
        PreparedStatement stmt = null;
        try {
            stmt = getStatement(mDeleteString, conn);
            stmt.setString(1, euid);
            stmt.setString(2, ow.getPath());

            stmt.executeUpdate();
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params.add(euid);
                params.add(ow.getPath());
                
                String sql = sql2str(mDeleteString, params);
                throw new OPSException(sql + e.getMessage());
            } catch (ObjectException oe) {
                throw new OPSException(oe.getMessage() + "\n" + sqlerr);
            }
        } catch (ObjectException e) {
            throw new OPSException(e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }

    /**
     * Updates an existing SystemSBR and its children records in the database.
     *
     * @param conn JDBC connection.
     * @param opsmap OPS hashmap.
     * @param euid EUID.
     * @param ow SBR overwrite object
     * @throws OPSException if an error occurs.
     */
    public void update(Connection conn, HashMap opsmap, String euid, SBROverWrite ow)
        throws OPSException {
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Updating SBROverWrite for EUID: " + euid);
        }

        // executes update SQL statement
        PreparedStatement stmt = null;
        try {
            if (!ow.isAdded()) {
                if (ow.isUpdated()) {
                    String type = getSBROverWriteFieldType(ow);
                    if (type.equals(ObjectField.OBJECTMETA_INT_STRING)) {
                        stmt = getStatement(mUpdateStringForInt, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_BOOL_STRING)) {
                        stmt = getStatement(mUpdateStringForBool, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_STRING_STRING)) {
                        stmt = getStatement(mUpdateStringForString, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_BYTE_STRING)) {
                        stmt = getStatement(mUpdateStringForByte, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_CHAR_STRING)) {
                        stmt = getStatement(mUpdateStringForCharacter, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_LONG_STRING)) {
                        stmt = getStatement(mUpdateStringForLong, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_DATE_STRING)) {
                        stmt = getStatement(mUpdateStringForDate, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_FLOAT_STRING)) {
                        stmt = getStatement(mUpdateStringForFloat, conn);
                    } else if (type.equals(ObjectField.OBJECTMETA_TIMESTAMP_STRING)) {
                        stmt = getStatement(mUpdateStringForTimeStamp, conn);
                    }
                    
                    setParam(stmt, 1, type, ow.getData());
                    setParam(stmt, 2, "String", euid);
                    setParam(stmt, 3, "String", ow.getPath());

                    stmt.executeUpdate();
                    ow.setUpdateFlag(false);
                } else if (ow.isRemoved()) {
                    this.remove(conn, opsmap, euid, ow);
                }
            } else {
                this.create(conn, opsmap, euid, ow);
            }
        } catch (SQLException e) {
            String sqlerr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = addobject(params, ow.getData());
                params = addobject(params, euid);
                params = addobject(params, ow.getPath());

                String type = getSBROverWriteFieldType(ow);
                String sql = null;
                
                if (type.equals(ObjectField.OBJECTMETA_INT_STRING)) {
                    sql = sql2str(mUpdateStringForInt, params);
                } else if (type.equals(ObjectField.OBJECTMETA_BOOL_STRING)) {
                    sql = sql2str(mUpdateStringForBool, params);
                } else if (type.equals(ObjectField.OBJECTMETA_STRING_STRING)) {
                    sql = sql2str(mUpdateStringForString, params);
                } else if (type.equals(ObjectField.OBJECTMETA_BYTE_STRING)) {
                    sql = sql2str(mUpdateStringForByte, params);
                } else if (type.equals(ObjectField.OBJECTMETA_CHAR_STRING)) {
                    sql = sql2str(mUpdateStringForCharacter, params);
                } else if (type.equals(ObjectField.OBJECTMETA_LONG_STRING)) {
                    sql = sql2str(mUpdateStringForLong, params);
                } else if (type.equals(ObjectField.OBJECTMETA_DATE_STRING)) {
                    sql = sql2str(mUpdateStringForDate, params);
                } else if (type.equals(ObjectField.OBJECTMETA_FLOAT_STRING)) {
                    sql = sql2str(mUpdateStringForFloat, params);
                } else if (type.equals(ObjectField.OBJECTMETA_TIMESTAMP_STRING)) {
                    sql = sql2str(mUpdateStringForTimeStamp, params);
                }
                
                throw new OPSException(sql + e.getMessage());
            } catch (ObjectException oe) {
                throw new OPSException(oe.getMessage() + "\n" + sqlerr);
            }
        } catch (ObjectException e) {
            throw new OPSException(e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        		if (stmt != null) {
            		stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }
    }
    
    /**
     * Gets the type of an SBR Ovewrite field.
     *
     * @param overwrite SBR overwrite object.
     * @throws OPSException if an error occurs.
     */
    private String getSBROverWriteFieldType(SBROverWrite overwrite) 
            throws ObjectException {
        String ret = null;
        ret = overwrite.getFieldType();
        
        if (ret == null) {
            // MetaDataService does not throw an exception on error
            if (ret == null) {
                throw new ObjectException("MetaDataService could not obtain " +
                "field type of: " + overwrite.getEPath());
            }
        }
        return ret;
    }
}
