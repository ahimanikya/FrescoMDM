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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.mdm.multidomain.hierarchy.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyDefDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyDefDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyDefDaoException;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.MatchCriteria;
import com.sun.mdm.multidomain.sql.OrderBy;
import com.sun.mdm.multidomain.sql.Parameter;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;

/**
 *
 * @author David Peh
 */
public class HierarchyDefDaoImpl extends AbstractDAO implements HierarchyDefDao {

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
     * Method 'HierarchyDefDaoImpl'
     *
     */
    public HierarchyDefDaoImpl() {
    }

    /**
     * Method 'HierarchyDefDaoImpl'
     *
     * @param userConn
     */
    public HierarchyDefDaoImpl(final Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Inserts a new row in the hierarchy_def table.
     */
    public long insert(HierarchyDefDto dto) throws HierarchyDefDaoException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(HIERARCHY_DEF.getTableName());
            for (HIERARCHY_DEF hier : HIERARCHY_DEF.values()) {
                builder.addColumns(hier.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setLong(index++, 0);  // Primary key
            stmt.setString(index++, dto.getHierarchyName());
            stmt.setString(index++, dto.getDescription());
            stmt.setString(index++, dto.getDomain());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            stmt.setString(index++, dto.getEffectiveFromReq());
            stmt.setString(index++, dto.getEffectiveToReq());
            stmt.setString(index++, dto.getEffectiveFromInc());
            stmt.setString(index++, dto.getEffectiveToInc());

            stmt.setString(index++, dto.getPlugIn());

            int rows = stmt.executeUpdate();
            // retrieve values from auto-increment columns
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the hierarchy_def table.
     */
    public int update(HierarchyDefDto dto) throws HierarchyDefDaoException {
        PreparedStatement stmt = null;

        try {
            UpdateBuilder builder = new UpdateBuilder();
            builder.setTable(HIERARCHY_DEF.getTableName());
            for (HIERARCHY_DEF rel : HIERARCHY_DEF.values()) {
                switch (rel) {
                    case HIERARCHY_DEF_ID:
                        builder.addCriteria(new Parameter(rel.columnName));
                        break;
                    default:
                        builder.addColumns(rel.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, dto.getHierarchyName());
            stmt.setString(index++, dto.getDescription());
            stmt.setString(index++, dto.getDomain());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            stmt.setString(index++, dto.getEffectiveFromReq());
            stmt.setString(index++, dto.getEffectiveToReq());
            stmt.setString(index++, dto.getEffectiveFromInc());
            stmt.setString(index++, dto.getEffectiveToInc());

            /* set update SQL criteria */
            stmt.setLong(index++, dto.getHierarchyDefId());
            int rows = stmt.executeUpdate();

            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Deletes a single row in the hierarchy_def table.
     */
    public void delete(long relDefId) throws HierarchyDefDaoException {
        PreparedStatement stmt = null;

        try {
            // Build DELETE SQL for HIERARCHY_NODE_EA table
            DeleteBuilder deleteBld = new DeleteBuilder();
            deleteBld.setTable(HIERARCHY_NODE_EA.getTableName());
            Criteria c1 = new Parameter(HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.columnName);
            deleteBld.addCriteria(c1);
            String sqlStr = SQLBuilder.buildSQL(deleteBld);
            stmt = userConn.prepareStatement(sqlStr);
            stmt.setLong(1, relDefId);
            int rows = stmt.executeUpdate();

            // Build DELETE SQL for HIERARCHY_DEF table
            deleteBld = new DeleteBuilder();
            deleteBld.setTable(HIERARCHY_DEF.getTableName());
            Criteria c2 = new Parameter(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName);
            deleteBld.addCriteria(c2);
            sqlStr = SQLBuilder.buildSQL(deleteBld);
            stmt = userConn.prepareStatement(sqlStr);
            stmt.setLong(1, relDefId);
            rows = stmt.executeUpdate();

        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    public List<HierarchyDefDto> search(String domain) throws HierarchyDefDaoException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            // construct the SQL statement
            String sqlStr = BuildSelectByDomain();
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, domain);
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public HierarchyDefDto searchById(long hierarchyId) throws HierarchyDefDaoException {
        // declare variables
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // construct the SQL statement
            String sqlStr = BuildSelectById();
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setLong(index++, hierarchyId);
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();

            // fetch the results
            return fetchSingleResult(rs);
        } catch (Exception _e) {
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    private String BuildSelectByDomain() {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_DEF.getTableName(), HIERARCHY_NODE_EA.getTableName());

        for (HIERARCHY_DEF hier : HIERARCHY_DEF.values()) {
            builder.addColumns(hier.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EA ea : HIERARCHY_NODE_EA.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c2 = new Parameter(HIERARCHY_DEF.DOMAIN.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2));
        builder.addOrderBy(new OrderBy(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, OrderBy.ORDER.ASC));
        String sqlStr = SQLBuilder.buildSQL(builder);
        return sqlStr;
    }

    private String BuildSelectById() {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_DEF.getTableName(), HIERARCHY_NODE_EA.getTableName());

        for (HIERARCHY_DEF hier : HIERARCHY_DEF.values()) {
            builder.addColumns(hier.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EA ea : HIERARCHY_NODE_EA.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        builder.addCriteria(new AND(c1, c2));
        String sqlStr = SQLBuilder.buildSQL(builder);
        return sqlStr;
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
     * Fetches a single row from the result set
     */
    protected HierarchyDefDto fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            HierarchyDefDto dto = new HierarchyDefDto();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Fetches multiple rows from the result set
     */
    protected List<HierarchyDefDto> fetchMultiResults(ResultSet rs) throws SQLException {
        ArrayList<HierarchyDefDto> resultList = new ArrayList();

        long relDefId = -1;
        HierarchyDefDto hierDto = null;
        while (rs.next()) {
            if (relDefId != rs.getLong(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName)) {
                if (hierDto != null) {
                    resultList.add(hierDto);
                }
                relDefId = rs.getLong(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName);
                hierDto = new HierarchyDefDto();
                populateDto(hierDto, rs);
            }
            HierarchyNodeEaDto eaDto = new HierarchyNodeEaDto();
            populateDto(eaDto, rs);
            hierDto.getAttributeDefs().add(eaDto);
        }
        if (hierDto != null) {
            resultList.add(hierDto);
        }
        return resultList;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    private void populateDto(HierarchyDefDto hierDto, ResultSet rs) throws SQLException {
        /* Poplulate HierarchyDef DTO */
        hierDto.setHierarchyDefId(rs.getLong(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName));
        hierDto.setHierarchyName(rs.getString(HIERARCHY_DEF.HIERARCHY_NAME.columnName));
        hierDto.setDescription(rs.getString(HIERARCHY_DEF.DESCRIPTION.columnName));
        hierDto.setDomain(rs.getString(HIERARCHY_DEF.DOMAIN.columnName));
        hierDto.setEffectiveFromDate(rs.getDate(HIERARCHY_DEF.EFFECTIVE_FROM_DATE.columnName));
        hierDto.setEffectiveToDate(rs.getDate(HIERARCHY_DEF.EFFECTIVE_FROM_DATE.columnName));
        hierDto.setEffectiveFromReq(rs.getString(HIERARCHY_DEF.EFFECTIVE_FROM_REQ.columnName));
        hierDto.setEffectiveToReq(rs.getString(HIERARCHY_DEF.EFFECTIVE_TO_REQ.columnName));
        hierDto.setEffectiveFromInc(rs.getString(HIERARCHY_DEF.EFFECTIVE_FROM_INCL.columnName));
        hierDto.setEffectiveToInc(rs.getString(HIERARCHY_DEF.EFFECTIVE_TO_INCL.columnName));
        hierDto.setPlugIn(rs.getString(HIERARCHY_DEF.PLUGIN.columnName));
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    private void populateDto(HierarchyNodeEaDto dto, ResultSet rs) throws SQLException {
        dto.setHierarchyDefId(rs.getLong(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName));
        dto.setAttributeName(rs.getString(HIERARCHY_NODE_EA.ATTRIBUTE_NAME.columnName));
        dto.setColumnName(rs.getString(HIERARCHY_NODE_EA.COLUMN_NAME.columnName));
        dto.setColumnType(rs.getString(HIERARCHY_NODE_EA.COLUMN_TYPE.columnName));
        dto.setDefaultValue(rs.getString(HIERARCHY_NODE_EA.DEFAULT_VALUE.columnName));
        dto.setIsRequired(rs.getString(HIERARCHY_NODE_EA.IS_REQUIRED.columnName).equalsIgnoreCase("T") ? true : false);
        dto.setIsSearchable(rs.getString(HIERARCHY_NODE_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("T") ? true : false);
    }
}
