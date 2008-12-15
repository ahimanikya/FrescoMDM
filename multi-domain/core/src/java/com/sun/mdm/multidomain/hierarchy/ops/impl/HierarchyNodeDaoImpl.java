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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sun.mdm.multidomain.hierarchy.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyDefDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyDaoException;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.MatchCriteria;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_DEF;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_NODE;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_NODE_EA;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_NODE_EAV;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.sql.NullCriteria;
import com.sun.mdm.multidomain.sql.Parameter;

/**
 *
 * @author David Peh
 */
public class HierarchyNodeDaoImpl extends AbstractDAO implements HierarchyNodeDao {

    private Connection userConn;
    private int maxRows;
    private long mPrimaryKey = 0;

    /**
     * Method 'HierarchyNodeDaoImpl'
     *
     */
    public HierarchyNodeDaoImpl() {
    }

    /**
     * Method 'HierarchyNodeDaoImpl'
     *
     * @param userConn
     */
    public HierarchyNodeDaoImpl(final Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Inserts a new row in the HierarchyNode table.
     *
     * @param  hierNode the hierarchy-node object to be persisted in the database
     * @return the primary key for the inserted row
     */
    public long insert(HierarchyNode hierNode) throws HierarchyDaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Build the INSERT SQL.
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(HIERARCHY_NODE.getTableName());
            for (HIERARCHY_NODE hier : HIERARCHY_NODE.values()) {
                builder.addColumns(hier.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr);
            int index = 1;
            /* set primary key column to null */
            stmt.setNull(index++, java.sql.Types.NULL);
            stmt.setLong(index++, hierNode.getHierarchyDef().getId());
            stmt.setLong(index++, hierNode.getParentNodeID());
            stmt.setString(index++, hierNode.getEUID());
            stmt.setString(index++, hierNode.getParentEUID());
            stmt.setTimestamp(index++, (java.sql.Timestamp) hierNode.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) hierNode.getEffectiveToDate());
            int rows = stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the HierarchyDto table.
     */
    public void update(long pk, HierarchyNodeDto dto) throws HierarchyDaoException {
        PreparedStatement stmt = null;

        try {
            int index = 1;
            stmt.setLong(index++, dto.getHierarchyNodeId());
            //stmt.setString(index++, dto.getSourceEuid());
            //  stmt.setString(index++, dto.getTargetEuid());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            // stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getPurgeDate());
            stmt.setLong(14, pk);
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Deletes a single row in the HierarchyDto table.
     */
    public void delete(long pk) throws HierarchyDaoException {
        PreparedStatement stmt = null;
        try {
            // stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setLong(1, pk);
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    public List<HierarchyNodeDto> search(String euid) throws HierarchyDaoException {
        try {
            HierarchyDefDto hierDef = getHierarchyDef(euid);
            List<HierarchyNodeDto> hierNodes = getHierarchyNodes(hierDef, euid);
            return hierNodes;
        } catch (Exception _e) {
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    public List<HierarchyNodeDto> findChildren(long nodeId) throws HierarchyDaoException {
        try {
            HierarchyDefDto hierDef = getHierarchyDef(nodeId);
            List<HierarchyNodeDto> hierNodes = getChildNodes(hierDef, nodeId);
            return hierNodes;
        } catch (Exception _e) {
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public HierarchyNodeDto findNode(long nodeId) throws HierarchyDaoException {
        try {
            HierarchyDefDto hierDef = getHierarchyDef(nodeId);
            HierarchyNodeDto hierNode = getHierarchyNode(hierDef, nodeId);
            return hierNode;
        } catch (Exception _e) {
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public List<HierarchyNodeDto> findAncestors(long nodeId) throws HierarchyDaoException {
        try {
            HierarchyDefDto hierDef = getHierarchyDef(nodeId);
            List<HierarchyNodeDto> hierNodes = getHierarchyNodes(hierDef, nodeId);
            HierarchyNodeDto child = hierNodes.get(0);
            List<HierarchyNodeDto> parentNodes = new ArrayList<HierarchyNodeDto>();
            getParentNodes(child, hierDef, parentNodes);
            return parentNodes;
        } catch (Exception _e) {
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public HierarchyNodeDto getRootHierarchyNode(long hierarchyDefId) throws HierarchyDaoException {
        HierarchyDefDto hierDef = null;
        HierarchyNodeDto hierNode = null;

        try {
            hierDef = getHierarchyDef(hierarchyDefId);
            hierNode = getRootNode(hierDef);
        } catch (SQLException e) {
            throw new HierarchyDaoException("Exception: " + e.getMessage(), e);
        }

        return hierNode;
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
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    private void getParentNodes(HierarchyNodeDto child, HierarchyDefDto hierDef, List<HierarchyNodeDto> parentNodes) throws HierarchyDaoException {
        // declare variables
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            long parentNodeId = child.getParentNodeId();
            if (parentNodeId > 0 && child.getParentEuid() != null) {
                SelectBuilder builder = new SelectBuilder();
                builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName());

                for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
                    builder.addColumns(hierNode.prefixedColumnName);
                }
                for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
                    builder.addColumns(ea.prefixedColumnName);
                }
                for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
                    builder.addColumns(eaDto.getColumnName());
                }
                Criteria c1 = new Parameter(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
                Criteria c2 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
                builder.addCriteria(new AND(c1, c2));
                String sqlStr = SQLBuilder.buildSQL(builder);
                stmt = userConn.prepareStatement(sqlStr);
                stmt = conn.prepareStatement(sqlStr);
                stmt.setLong(1, parentNodeId);

                rs = stmt.executeQuery();
                HierarchyNodeDto node = fetchSingleResult(rs, hierDef);

                parentNodes.add(node);

                //recursion
                getParentNodes(node, hierDef, parentNodes);
            }

        } catch (Exception _e) {
            throw new HierarchyDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    private List<HierarchyNodeDto> getChildNodes(HierarchyDefDto hierDef, long nodeId) throws SQLException {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName());

        for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
            builder.addColumns(hierNode.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
            builder.addColumns(eaDto.getColumnName());
        }
        Criteria c1 = new Parameter(HIERARCHY_NODE.PARENT_NODE_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2));
        String sqlStr = SQLBuilder.buildSQL(builder);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        // prepare statement
        stmt = userConn.prepareStatement(sqlStr);

        stmt.setLong(1, nodeId);
        //stmt.setMaxRows(maxRows);

        ResultSet rs = stmt.executeQuery();
        return fetchMultiResults(rs, hierDef);
    }

    private HierarchyDefDto getHierarchyDef(String euid) throws SQLException {
        SelectBuilder selectBld = new SelectBuilder();
        String hierDefTable = HIERARCHY_DEF.getTableName();
        String hierEATable = HIERARCHY_NODE_EA.getTableName();
        String hierNodeTable = HIERARCHY_NODE.getTableName();

        selectBld.setTable(hierDefTable, hierNodeTable, hierEATable);
        selectBld.addColumns(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
        for (HIERARCHY_DEF hierDef : HIERARCHY_DEF.values()) {
            selectBld.addColumns(hierDef.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EA hierEA : HIERARCHY_NODE_EA.values()) {
            selectBld.addColumns(hierEA.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c3 = new Parameter(HIERARCHY_NODE.EUID.prefixedColumnName);
        selectBld.addCriteria(new AND(c1, c2, c3));
        String sqlStr = SQLBuilder.buildSQL(selectBld);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        int index = 1;
        stmt.setString(index++, euid);
        ResultSet rs = stmt.executeQuery();
        HierarchyDefDto hierDefDto = new HierarchyDefDto();

        while (rs.next()) {
            if (rs.isFirst()) {
                if (mPrimaryKey == 0) {
                    mPrimaryKey = rs.getLong(HIERARCHY_NODE.HIERARCHY_NODE_ID.columnName);
                }
                populateHierDefDto(rs, hierDefDto);
            }
            HierarchyNodeEaDto hierNodeEa = new HierarchyNodeEaDto();
            populateHierNodeEaDto(rs, hierNodeEa);
            hierDefDto.getAttributeDefs().add(hierNodeEa);
        }
        return hierDefDto;
    }

    /**
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    private HierarchyNodeDto getHierarchyNode(HierarchyDefDto hierDef, long nodeId) throws SQLException {
        // declare variables
        final boolean isConnSupplied = (userConn != null);

        Connection conn = isConnSupplied ? userConn : ResourceManager.getConnection();
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName());

        for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
            builder.addColumns(hierNode.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
            builder.addColumns(eaDto.getColumnName());
        }
        Criteria c1 = new Parameter(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2));
        String sqlStr = SQLBuilder.buildSQL(builder);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        stmt = conn.prepareStatement(sqlStr);
        stmt.setLong(1, nodeId);

        ResultSet rs = stmt.executeQuery();
        HierarchyNodeDto node = fetchSingleResult(rs, hierDef);

        return node;
    }

    private HierarchyNodeDto getRootNode(HierarchyDefDto hierDef) throws SQLException {
        // declare variables
        final boolean isConnSupplied = (userConn != null);

        Connection conn = isConnSupplied ? userConn : ResourceManager.getConnection();
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName(), HIERARCHY_NODE_EA.getTableName());

        for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
            builder.addColumns(hierNode.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
            builder.addColumns(eaDto.getColumnName());
        }
        Criteria c1 = new NullCriteria(HIERARCHY_NODE.PARENT_NODE_ID.prefixedColumnName, NullCriteria.OPERATOR.ISNULL);
        Criteria c2 = new Parameter(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c3 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2, c3));
        String sqlStr = SQLBuilder.buildSQL(builder);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        stmt = conn.prepareStatement(sqlStr);
        int index = 1;
        //stmt.setNull(index++, Types.NULL);
        stmt.setLong(index++, hierDef.getHierarchyDefId());

        ResultSet rs = stmt.executeQuery();
        HierarchyNodeDto node = fetchSingleResult(rs, hierDef);

        return node;
    }

    private List<HierarchyNodeDto> getHierarchyNodes(HierarchyDefDto hierDef, long nodeId) throws SQLException {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName());

        for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
            builder.addColumns(hierNode.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
            builder.addColumns(eaDto.getColumnName());
        }
        Criteria c1 = new Parameter(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2));
        String sqlStr = SQLBuilder.buildSQL(builder);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        // prepare statement
        stmt = userConn.prepareStatement(sqlStr);

        stmt.setLong(1, nodeId);
        //stmt.setMaxRows(maxRows);

        ResultSet rs = stmt.executeQuery();
        return fetchMultiResults(rs, hierDef);
    }

    // public HierarchyNodeDto getParentNode(String euid) throws HierarchyDaoException {
    // }
    private HierarchyDefDto getHierarchyDef(long nodeId) throws SQLException {
        SelectBuilder selectBld = new SelectBuilder();
        String hierDefTable = HIERARCHY_DEF.getTableName();
        String hierEATable = HIERARCHY_NODE_EA.getTableName();
        String hierNodeTable = HIERARCHY_NODE.getTableName();

        selectBld.setTable(hierDefTable, hierNodeTable, hierEATable);
        selectBld.addColumns(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
        for (HIERARCHY_DEF hierDef : HIERARCHY_DEF.values()) {
            selectBld.addColumns(hierDef.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EA hierEA : HIERARCHY_NODE_EA.values()) {
            selectBld.addColumns(hierEA.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.prefixedColumnName);
        Criteria c3 = new Parameter(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName);
        selectBld.addCriteria(new AND(c1, c2, c3));
        String sqlStr = SQLBuilder.buildSQL(selectBld);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        int index = 1;
        stmt.setLong(index++, nodeId);
        ResultSet rs = stmt.executeQuery();
        HierarchyDefDto hierDefDto = new HierarchyDefDto();

        while (rs.next()) {
            if (rs.isFirst()) {
                if (mPrimaryKey == 0) {
                    mPrimaryKey = rs.getLong(HIERARCHY_NODE.HIERARCHY_NODE_ID.columnName);
                }
                populateHierDefDto(rs, hierDefDto);
            }
            HierarchyNodeEaDto hierNodeEa = new HierarchyNodeEaDto();
            populateHierNodeEaDto(rs, hierNodeEa);
            hierDefDto.getAttributeDefs().add(hierNodeEa);
        }
        return hierDefDto;
    }

    private List<HierarchyNodeDto> getHierarchyNodes(HierarchyDefDto hierDef, String euid) throws SQLException {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(HIERARCHY_NODE.getTableName(), HIERARCHY_NODE_EAV.getTableName());

        for (HIERARCHY_NODE hierNode : HIERARCHY_NODE.values()) {
            builder.addColumns(hierNode.prefixedColumnName);
        }
        for (HIERARCHY_NODE_EAV ea : HIERARCHY_NODE_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (HierarchyNodeEaDto eaDto : hierDef.getAttributeDefs()) {
            builder.addColumns(eaDto.getColumnName());
        }
        Criteria c1 = new Parameter(HIERARCHY_NODE.EUID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(HIERARCHY_NODE.HIERARCHY_NODE_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, HIERARCHY_NODE_EAV.HIERARCHY_NODE_ID.prefixedColumnName);
        builder.addCriteria(new AND(c1, c2));
        String sqlStr = SQLBuilder.buildSQL(builder);
        PreparedStatement stmt = userConn.prepareStatement(sqlStr);
        // prepare statement
        stmt = userConn.prepareStatement(sqlStr);

        stmt.setString(1, euid);
        //stmt.setMaxRows(maxRows);

        ResultSet rs = stmt.executeQuery();
        return fetchMultiResults(rs, hierDef);
    }

    /**
     * Fetches a single row from the result set
     */
    private HierarchyNodeDto fetchSingleResult(ResultSet rs, HierarchyDefDto hierDef) throws SQLException {
        if (rs.next()) {
            HierarchyNodeDto dto = new HierarchyNodeDto();
            populateHierNodeDto(rs, dto, hierDef.getAttributeDefs());
            return dto;
        } else {
            return null;
        }

    }

    /**
     * Fetches multiple rows from the result set
     */
    private List<HierarchyNodeDto> fetchMultiResults(ResultSet rs, HierarchyDefDto hierDef) throws SQLException {
        ArrayList<HierarchyNodeDto> resultList = new ArrayList<HierarchyNodeDto>();

        while (rs.next()) {
            HierarchyNodeDto dto = new HierarchyNodeDto();
            populateHierNodeDto(rs, dto, hierDef.getAttributeDefs());
            resultList.add(dto);
        }

        HierarchyNodeDto ret[] = new HierarchyNodeDto[resultList.size()];

        resultList.toArray(ret);

        return resultList;
    }

    /**
     * Populates a Hierarchy_Def DTO with data from a ResultSet
     */
    private void populateHierDefDto(ResultSet rs, HierarchyDefDto hierDefDto) throws SQLException {
        hierDefDto.setHierarchyDefId(rs.getLong(HIERARCHY_DEF.HIERARCHY_DEF_ID.columnName));
        hierDefDto.setHierarchyName(rs.getString(HIERARCHY_DEF.HIERARCHY_NAME.columnName));
        hierDefDto.setDescription(rs.getString(HIERARCHY_DEF.DESCRIPTION.columnName));
        hierDefDto.setDomain(rs.getString(HIERARCHY_DEF.DOMAIN.columnName));
        hierDefDto.setEffectiveFromDate(rs.getDate(HIERARCHY_DEF.EFFECTIVE_FROM_DATE.columnName));
        hierDefDto.setEffectiveToDate(rs.getDate(HIERARCHY_DEF.EFFECTIVE_TO_DATE.columnName));
        hierDefDto.setEffectiveFromReq(rs.getString(HIERARCHY_DEF.EFFECTIVE_FROM_REQ.columnName));
        hierDefDto.setEffectiveToReq(rs.getString(HIERARCHY_DEF.EFFECTIVE_TO_REQ.columnName));
        hierDefDto.setEffectiveFromInc(rs.getString(HIERARCHY_DEF.EFFECTIVE_FROM_INCL.columnName));
        hierDefDto.setEffectiveToInc(rs.getString(HIERARCHY_DEF.EFFECTIVE_TO_INCL.columnName));
        hierDefDto.setPlugIn(rs.getString(HIERARCHY_DEF.PLUGIN.columnName));
    }

    /**
     * Populates a Hierarchy_Node DTO with data from a ResultSet
     */
    private void populateHierNodeDto(ResultSet rs, HierarchyNodeDto hierNodeDto, List<HierarchyNodeEaDto> attrDefList) throws SQLException {
        hierNodeDto.setHierarchyDefId(rs.getLong(HIERARCHY_NODE.HIERARCHY_DEF_ID.columnName));
        hierNodeDto.setHierarchyNodeId(rs.getLong(HIERARCHY_NODE.HIERARCHY_NODE_ID.columnName));
        hierNodeDto.setParentNodeId(rs.getLong(HIERARCHY_NODE.PARENT_NODE_ID.columnName));
        hierNodeDto.setEuid(rs.getString(HIERARCHY_NODE.EUID.columnName));
        hierNodeDto.setParentEuid(rs.getString(HIERARCHY_NODE.PARENT_EUID.columnName));
        hierNodeDto.setEffectiveFromDate(rs.getDate(HIERARCHY_NODE.EFFECTIVE_FROM_DATE.columnName));
        hierNodeDto.setEffectiveToDate(rs.getDate(HIERARCHY_NODE.EFFECTIVE_TO_DATE.columnName));
        Map attrValueList = hierNodeDto.getHierarchyAttributes();
        for (HierarchyNodeEaDto attr : attrDefList) {
            AttributeType attrType = AttributeType.valueOf(attr.getColumnType());
            String attrValue = null;
            switch (attrType) {
                case BOOLEAN:
                    attrValue = rs.getString(attr.getColumnName()).equalsIgnoreCase("T") ? "TRUE" : "FALSE";
                    break;
                case FLOAT:
                    attrValue = Float.toString(rs.getFloat(attr.getColumnName()));
                    break;
                case DATE:
                    attrValue = rs.getTimestamp(attr.getColumnName()).toString();
                    break;
                case STRING:
                case CHAR:
                    attrValue = rs.getString(attr.getColumnName());
                    break;
                case INT:
                    attrValue = Long.toString(rs.getLong(attr.getColumnName()));
                    break;
                default:
            }
            attrValueList.put(attr, attrValue);
        }
    }

    /**
     * Populates a Hierarchy_Def DTO with data from a ResultSet
     */
    private void populateHierNodeEaDto(ResultSet rs, HierarchyNodeEaDto hierNodeEa) throws SQLException {
        hierNodeEa.setEaId(rs.getLong(HIERARCHY_NODE_EA.EA_ID.columnName));
        hierNodeEa.setHierarchyDefId(rs.getLong(HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.columnName));
        hierNodeEa.setAttributeName(rs.getString(HIERARCHY_NODE_EA.ATTRIBUTE_NAME.columnName));
        hierNodeEa.setColumnName(rs.getString(HIERARCHY_NODE_EA.COLUMN_NAME.columnName));
        hierNodeEa.setColumnType(rs.getString(HIERARCHY_NODE_EA.COLUMN_TYPE.columnName));
        hierNodeEa.setDefaultValue(rs.getString(HIERARCHY_NODE_EA.DEFAULT_VALUE.columnName));
        hierNodeEa.setIsSearchable(rs.getString(HIERARCHY_NODE_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("T") ? true : false);
        hierNodeEa.setIsRequired(rs.getString(HIERARCHY_NODE_EA.IS_REQUIRED.columnName).equalsIgnoreCase("T") ? true : false);
    }

    /**
     * Resets the modified attributes in the DTO
     */
    private void reset(HierarchyDefDto dto) {
    }
}
