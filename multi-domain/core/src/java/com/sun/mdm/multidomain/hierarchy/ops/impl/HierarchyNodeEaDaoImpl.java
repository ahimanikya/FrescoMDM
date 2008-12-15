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
package com.sun.mdm.multidomain.hierarchy.ops.impl;

import com.sun.mdm.multidomain.hierarchy.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeEaDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_NODE_EA;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author David Peh
 */
public class HierarchyNodeEaDaoImpl extends AbstractDAO implements HierarchyNodeEaDao {

    /**
     * The factory class for this DAO has two versions of the create() method - one that
    takes no arguments and one that takes a Connection argument. If the Connection version
    is chosen then the connection will be stored in this attribute and will be used by all
    calls to this DAO, otherwise a new Connection will be allocated for each operation.
     */
    private Connection userConn;
    private long mPrimaryKey = 0;
    private int maxRows;

    /**
     * Inserts a new row in the hierarchy_ea table.
     */
    public long insert(HierarchyNodeEaDto dto) throws HierarchyEaDaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            InsertBuilder insertBld = new InsertBuilder();
            insertBld.setTable(HIERARCHY_NODE_EA.getTableName());
            for (HIERARCHY_NODE_EA ea : HIERARCHY_NODE_EA.values()) {
                insertBld.addColumns(ea.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(insertBld);
            stmt = userConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setLong(index++, dto.getEaId());
            stmt.setLong(index++, dto.getHierarchyDefId());
            stmt.setString(index++, dto.getAttributeName());
            stmt.setString(index++, dto.getColumnName());
            stmt.setString(index++, dto.getColumnType());
            stmt.setString(index++, dto.getDefaultValue());
            stmt.setString(index++, dto.getIsSearchable() ? "T" : "F");
            stmt.setString(index++, dto.getIsRequired() ? "T" : "F");
            int rows = stmt.executeUpdate();
            reset(dto);
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEaDaoException("Exception: " + _e.getMessage(), _e);

        }

    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the hierarchy_ea table.
     */
    public void update(long pk, HierarchyNodeEaDto dto) throws HierarchyEaDaoException {
        PreparedStatement stmt = null;

        try {
            // stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setLong(index++, dto.getEaId());
            stmt.setLong(index++, dto.getHierarchyDefId());
            stmt.setString(index++, dto.getAttributeName());
            stmt.setString(index++, dto.getColumnName());
            stmt.setString(index++, dto.getColumnType());
            stmt.setString(index++, dto.getDefaultValue());
            stmt.setString(index++, dto.getIsSearchable() ? "T" : "F");
            stmt.setString(index++, dto.getIsRequired() ? "T" : "F");
            stmt.setLong(10, pk);
            int rows = stmt.executeUpdate();
            reset(dto);
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEaDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Deletes a single row in the hierarchy_ea table.
     */
    public void delete(long pk) throws HierarchyEaDaoException {

        PreparedStatement stmt = null;

        try {

            // stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setLong(1, pk);
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEaDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Method 'HierarchyNodeEaDaoImpl'
     *
     */
    public HierarchyNodeEaDaoImpl() {
    }

    /**
     * Method 'HierarchyNodeEaDaoImpl'
     *
     * @param userConn
     */
    public HierarchyNodeEaDaoImpl(final java.sql.Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Sets the value of maxRows
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * Gets the value of maxRows
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Method 'getTableName'
     *
     * @return String
     */
    public String getTableName() {
        return "hierarchy_ea";
    }

    /**
     * Fetches a single row from the result set
     */
    protected HierarchyNodeEaDto fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            HierarchyNodeEaDto dto = new HierarchyNodeEaDto();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }

    }

    /**
     * Fetches multiple rows from the result set
     */
    protected HierarchyNodeEaDto[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            HierarchyNodeEaDto dto = new HierarchyNodeEaDto();
            populateDto(dto, rs);
            resultList.add(dto);
        }

        HierarchyNodeEaDto ret[] = new HierarchyNodeEaDto[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    protected void populateDto(HierarchyNodeEaDto dto, ResultSet rs) throws SQLException {
        /*
        dto.setEaId(rs.getInt(COLUMN_EA_ID));
        dto.setHierarchyDefId(rs.getInt(COLUMN_RELATIONSHIP_DEF_ID));
        dto.setAttributeName(rs.getString(COLUMN_ATTRIBUTE_NAME));
        dto.setColumnName(rs.getString(COLUMN_COLUMN_NAME));
        dto.setColumnType(rs.getString(COLUMN_COLUMN_TYPE));
        dto.setDefaultValue(rs.getString(COLUMN_DEFAULT_VALUE));
        dto.setIsSearchable(rs.getString(COLUMN_IS_SEARCHABLE).equalsIgnoreCase("T") ? true : false);
        dto.setIsRequired(rs.getString(COLUMN_IS_REQUIRED).equalsIgnoreCase("T") ? true : false);
        dto.setIsIncluded(rs.getString(COLUMN_IS_INCLUDED).equalsIgnoreCase("T") ? true : false);
         */
    }

    /**
     * Resets the modified attributes in the DTO
     */
    protected void reset(HierarchyNodeEaDto dto) {
    }

    /**
     * Returns all rows from the hierarchy_ea table that match the specified arbitrary SQL statement
     */
    public HierarchyNodeEaDto[] findByDynamicSelect(String sql, Object[] sqlParams) throws HierarchyEaDaoException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            // construct the SQL statement
            final String SQL = sql;


            System.out.println("Executing " + SQL);
            // prepare statement
            stmt = userConn.prepareStatement(SQL);
            stmt.setMaxRows(maxRows);

            // bind parameters
            for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
                stmt.setObject(i + 1, sqlParams[i]);
            }


            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEaDaoException("Exception: " + _e.getMessage(), _e);
        }
    }
}
