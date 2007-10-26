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

import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.ops.exception.UnsupportedDataType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

import com.sun.mdm.index.objects.metadata.ObjectFactory;

/**
 * @author gzheng
 * base Object Persistence Service class
 */
public class ObjectPersistenceService implements java.io.Serializable {
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DBDATEFORMAT = "yyyy-MM-dd hh24:MI:ss";
    private static final Logger LOGGER = LogUtil.getLogger("com.sun.mdm.index.ops.ObjectPersistenceService");    
    private static final int ORACLE = 1;
    private static final int SQLSERVER = 2;
    private static final String SQLServer = "SQL Server";
    
    private int dbType = ORACLE;
    
    private BlobHelper mBlobHelper = null;
    
    /**
     * Default constructor: jdbc connection is obtained here (future
     * enhancements to be done for performance
     *
     * @throws OPSException if an error occurs.
     */
    public ObjectPersistenceService() throws OPSException {
        String db = ObjectFactory.getDatabase();
        if (db.equals(SQLServer)) {
            dbType = SQLSERVER;
        }
        
        mBlobHelper = BlobHelper.getBlobHelper(db);
        if (mBlobHelper == null) {
            throw new OPSException("Failed to obtain an instance of BlobHelper for " + dbType);
        }
    }

    /**
     * Gets a PreparedStatement from an SQL statement.
     * @param sqlstr SQL statement.
     * @param conn JDBC connection.
     * @return PreparedStatement
     * @throws OPSException if an error occurs.
     */
    public PreparedStatement getStatement(String sqlstr, Connection conn)
        throws OPSException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sqlstr);
        } catch (SQLException ex) {
            throw new OPSException(ex);
        }

        return stmt;
    }

    /**
     * Adds boolean data to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param boolean Value to be added
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, boolean b) {
        list.add(Boolean.valueOf(b));
        
        return (list);
    }

    /**
     * Adds int data to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param i Value to be added.
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, int i) {
        list.add(new Integer(i));

        return (list);
    }

    /**
     * Adds float data to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param float Value to be added.
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, float f) {
        list.add(new Float(f));

        return (list);
    }

    /**
     * adds double data to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param d Value to be added.
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, double d) {
        list.add(new Double(d));

        return (list);
    }

    /**
     * adds byte data to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param b
     * @b byte value
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, byte b) {
        list.add(new Byte(b));

        return (list);
    }

    /**
     * adds an Object to an ArrayList
     *
     * @param list ArrayList to be updated.
     * @param o Value to be added.
     * @return updated ArrayList
     */
    static ArrayList addobject(ArrayList list, Object o) {
        list.add(o);

        return (list);
    }

    /**
     * Generates a SQL Statement string from its template and parameter list.
     *
     * @param err SQL statement template string.
     * @param ArrayList of params to be bound to the statement string.
     * @return SQL String.
     */
    public static String sql2str(String err, ArrayList list) {
        String ret = null;

        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                Object param = list.get(i);
                int pos = err.indexOf('?');

                if (null == param) {
                    ret = err.substring(0, pos - 1) + "null" + err.substring(pos + 1, err.length());
                } else if (param instanceof String || param instanceof java.util.Date) {
                    ret = err.substring(0, pos - 1) 
                        + " '" + param.toString() + "'" + err.substring(pos + 1, err.length());
                } else {
                    ret = err.substring(0, pos - 1) + " " + param.toString() + err.substring(pos + 1, err.length());
                }

                err = ret;
            }
        }

        return "\nSQL Statement: \n" + ret + "\n";
    }

    /**
     * Formats a String for output by checking the length.
     *
     * @param str String to format.
     * @return String formatted string.
     */
    static String strfmt(String str) {
        final int strlen = 45;

        String ret = str;

        if (str.length() > strlen) {
            ret = str.substring(0, strlen) + "...";
        }

        return ret;
    }

    /**
     * Gets value from a ResultSet by Column name and type.
     *
     * @param rs ResultSet.
     * @param column Column name.
     * @param type Column type.
     * @throws OPSException if an error occurs.
     * @return Object containing the column value from the ResultSet.
     */
    public Object getValue(ResultSet rs, String column, String type)
            throws OPSException {
        Object ret = null;

        try {
            if (type.equals("String")) {
                ret = rs.getString(column.toUpperCase());
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Boolean")) {
                ret = Boolean.valueOf((rs.getBoolean(column.toUpperCase())));
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Integer")) {
                ret = new Integer(rs.getInt(column.toUpperCase()));
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Long")) {
                ret = new Long(rs.getLong(column.toUpperCase()));
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Float")) {
                ret = new Float(rs.getFloat(column.toUpperCase()));
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Byte")) {
                ret = new Byte(rs.getByte(column.toUpperCase()));               
                if (rs.wasNull()) {
                    ret = null;
                }
            } else if (type.equals("Character")) {      
                String value = rs.getString(column.toUpperCase());
                if (value != null && !rs.wasNull()) {
                    ret = new Character(value.charAt(0));
                }
            } else if (type.equals("Date") && dbType == ORACLE) {
                java.sql.Date d = rs.getDate(column.toUpperCase());
                if (d != null) {
                    ret = new java.util.Date(d.getTime());
                } else {
                    ret = null;
                }
            // SQL server supports only TimeStamp which includes Date too
            } else if (type.equals("Date") && dbType == SQLSERVER) {
                java.sql.Timestamp t = rs.getTimestamp(column.toUpperCase());
                if (t != null) {
                    ret = new java.util.Date(t.getTime());
                } else {
                    ret = null;
                }
            } else if ( type.equals("Timestamp")) {
                java.sql.Timestamp t = rs.getTimestamp(column.toUpperCase());
                if (t != null) {
                    ret = t; 
                } else {
                    ret = null;
                }
            } else if (type.equals("Blob")) {
                ret = mBlobHelper.getValue(rs, column);
            } else {
                throw new UnsupportedDataType();
            }
        } catch (SQLException ex) {
            throw new OPSException(ex);
        }

        return ret;
    }

    /**
     * Sets a blob value by ResultSet and Column name. 
     *
     * @param rs ResultSet.
     * @param column Column name.
     * @param value The value to be set.
     * @throws OPSException if an error is encountered.
     */
    public void setParamBlob(ResultSet rs, String column, Object value) 
            throws OPSException {
        BlobHelperParameters bhp = new BlobHelperParameters();
        bhp.setResultSet(rs);
        bhp.setColumnName(column);
        mBlobHelper.setParamBlob(bhp, value);
    
    }

    /**
     * Sets a blob value by PreparedStatement and Column index
     *
     * @param rs ResultSet.
     * @param column Column name.
     * @param value The value to be set.
     * @throws OPSException if an error is encountered.
     */
    public void setParamBlob(PreparedStatement ps, int columnIndex, Object value) 
            throws OPSException {
        BlobHelperParameters bhp = new BlobHelperParameters();
        bhp.setPreparedStatement(ps);
        bhp.setColumnIndex(columnIndex);
        mBlobHelper.setParamBlob(bhp, value);
    }
    
    /**
     * Binds parameters to a PreparedStatement by position and type.
     *
     * @param stmt PreparedStatement handle.
     * @param pos PreparedStatement parameter index position.
     * @param type Type of the parameter.
     * @param value The parameter to be bounded.
     * @throws OPSException if an error is encountered.
     */
    public void setParam(PreparedStatement stmt, int pos, String type,
                         Object value) throws OPSException {
        try {
            if (null != value) {
                if (type.equals("String") && value instanceof String) {
                    stmt.setString(pos, (String) value);
                } else if (value instanceof Boolean) {
                    stmt.setInt(pos, ((Boolean) value).booleanValue() ? 1 : 0);
                } else if (value instanceof Integer) {
                    stmt.setInt(pos, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    stmt.setLong(pos, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    stmt.setFloat(pos, ((Float) value).floatValue());
                } else if (value instanceof Byte) {
                    stmt.setByte(pos, ((Byte) value).byteValue());
                } else if (value instanceof Character) {
                    stmt.setObject(pos, ((Character) value).toString(), java.sql.Types.CHAR);
                } else if ((type.equals("Date") && dbType == ORACLE) && value instanceof java.util.Date) {
                    stmt.setDate(pos,
                        new java.sql.Date(((java.util.Date) value).getTime()));
                } else if (type.equals("Timestamp") && value instanceof java.sql.Timestamp) {
                    stmt.setTimestamp(pos,(Timestamp)value);
                } else if ( ((type.equals("Date") && dbType == SQLSERVER) ||type.equals("Timestamp")) && value instanceof java.util.Date) {
                    stmt.setTimestamp(pos,
                        new java.sql.Timestamp(
                            ((java.util.Date) value).getTime()));
                } else {
                    throw new UnsupportedDataType();
                }
            } else {
                if (type.equals("String")) {
                    stmt.setNull(pos, java.sql.Types.VARCHAR);
                } else if (type.equals("Boolean")) {
                    stmt.setNull(pos, java.sql.Types.NUMERIC);
                } else if (type.equals("Integer")) {
                    stmt.setNull(pos, java.sql.Types.NUMERIC);
                } else if (type.equals("Long")) {
                    stmt.setNull(pos, java.sql.Types.NUMERIC);
                } else if (type.equals("Float")) {
                    stmt.setNull(pos, java.sql.Types.NUMERIC);
                } else if (type.equals("Byte")) {
                    stmt.setNull(pos, java.sql.Types.CHAR);
                } else if (type.equals("Character")) {
                    stmt.setNull(pos, java.sql.Types.CHAR);
                } else if (type.equals("Date") && dbType == ORACLE) {
                    stmt.setNull(pos, java.sql.Types.DATE);
                } else if ((type.equals("Date") && dbType == SQLSERVER) || type.equals("Timestamp")) {
                    stmt.setNull(pos, java.sql.Types.TIMESTAMP);
                } else if (type.equals("Blob")) {
                    stmt.setNull(pos, java.sql.Types.BLOB);
                } else {
                    throw new UnsupportedDataType("Type = " + type);
                }
            }
        } catch (SQLException ex) {
            throw new OPSException("Error in binding parameter (" 
                + pos + ") of '" + type + "': " + value + ": " + ex.getMessage());
        }
    }

    /**
     * Finalizer.
     */
    public void finalize() {
    }

    /**
     * Initialization.
     *
     * @throws OPSException if an error occurs.
     */
    public void init() throws OPSException {
    }
        
    /**
     * Message logger.
     * @param msg Message to log.
     */
    protected static void log(String msg) {
        LOGGER.debug(msg);
    }
    
}
