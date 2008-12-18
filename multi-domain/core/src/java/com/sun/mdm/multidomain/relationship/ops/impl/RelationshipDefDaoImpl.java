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
package com.sun.mdm.multidomain.relationship.ops.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.relationship.RelationshipDef.DirectionMode;
import com.sun.mdm.multidomain.relationship.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDefDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDefDaoException;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.JoinCriteria;
import com.sun.mdm.multidomain.sql.MatchCriteria;
import com.sun.mdm.multidomain.sql.OrderBy;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import com.sun.mdm.multidomain.sql.Parameter;
import static com.sun.mdm.multidomain.sql.DBSchema.*;

/**
 *
 * @author David Peh
 */
public class RelationshipDefDaoImpl extends AbstractDAO implements RelationshipDefDao {

    protected Connection userConn;
    private long mPrimaryKey = 0;
    private int maxRows;

    /**
     * Method 'RelationshipDefDaoImpl'
     *
     */
    public RelationshipDefDaoImpl() {
    }

    /**
     * Method 'RelationshipDefDaoImpl'
     *
     * @param userConn
     */
    public RelationshipDefDaoImpl(final Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Inserts a new row in the relationship_def table.
     */
    public long insert(RelationshipDef relDef) throws RelationshipDefDaoException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Build INSERT SQL
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(RELATIONSHIP_DEF.getTableName());
            for (RELATIONSHIP_DEF rel : RELATIONSHIP_DEF.values()) {
                builder.addColumns(rel.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            stmt.setNull(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.ordinal() + 1, java.sql.Types.NULL);
            stmt.setString(RELATIONSHIP_DEF.RELATIONSHIP_NAME.ordinal() + 1, relDef.getName());
            stmt.setString(RELATIONSHIP_DEF.DESCRIPTION.ordinal() + 1, relDef.getDescription());
            stmt.setString(RELATIONSHIP_DEF.SOURCE_DOMAIN.ordinal() + 1, relDef.getSourceDomain());
            stmt.setString(RELATIONSHIP_DEF.TARGET_DOMAIN.ordinal() + 1, relDef.getTargetDomain());
            stmt.setString(RELATIONSHIP_DEF.BIDIRECTIONAL.ordinal() + 1, relDef.getDirection().IsBidirectional() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_FROM_REQ.ordinal() + 1, relDef.getEffectiveFromRequired() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_TO_REQ.ordinal() + 1, relDef.getEffectiveToRequired() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.PURGE_DATE_REQ.ordinal() + 1, relDef.getPurgeDateRequired() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_FROM_INCL.ordinal() + 1, relDef.getEffectiveFromIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_TO_INCL.ordinal() + 1, relDef.getEffectiveToIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.PURGE_DATE_INCL.ordinal() + 1, relDef.getPurgeDateIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.PLUGIN.ordinal() + 1, relDef.getPlugin());

            int rows = stmt.executeUpdate();
            // retrieve values from auto-increment columns
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }

            /* RelationshipDef Extend Attributes */
            RelationshipEaDto attDto = new RelationshipEaDto();
            ArrayList<Attribute> attrList = (ArrayList<Attribute>) relDef.getAttributes();
            RelationshipEaDaoImpl relEaDao = new RelationshipEaDaoImpl(userConn);
            for (Attribute att : attrList) {
                attDto.setRelationshipDefId(mPrimaryKey);
                attDto.setAttributeName(att.getName());
                attDto.setColumnName(att.getColumnName());
                attDto.setColumnType(att.getType().name());
                attDto.setDefaultValue(att.getDefaultValue());
                attDto.setIsRequired(att.getIsRequired());
                attDto.setIsSearchable(att.getIsSearchable());
                relEaDao.insert(attDto);
            }
            stmt.close();
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the relationship_def table.
     */
    public int update(RelationshipDef relDef) throws RelationshipDefDaoException {
        PreparedStatement stmt = null;

        try {

            UpdateBuilder builder = new UpdateBuilder();
            builder.setTable(RELATIONSHIP_DEF.getTableName());
            for (RELATIONSHIP_DEF rel : RELATIONSHIP_DEF.values()) {
                if (rel.columnName.equalsIgnoreCase(RELATIONSHIP_DEF.getPKColumName())) {

                    builder.addCriteria(new Parameter(rel.columnName));
                } else {
                    builder.addColumns(rel.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, relDef.getName());
            stmt.setString(index++, relDef.getDescription());
            stmt.setString(index++, relDef.getSourceDomain());
            stmt.setString(index++, relDef.getTargetDomain());
            stmt.setString(index++, relDef.getDirection().IsBidirectional() ? "T" : "F");
            stmt.setString(index++, relDef.getEffectiveFromRequired() ? "T" : "F");
            stmt.setString(index++, relDef.getEffectiveToRequired() ? "T" : "F");
            stmt.setString(index++, relDef.getPurgeDateRequired() ? "T" : "F");
            stmt.setString(index++, relDef.getEffectiveFromIncluded() ? "T" : "F");
            stmt.setString(index++, relDef.getEffectiveToIncluded() ? "T" : "F");
            stmt.setString(index++, relDef.getPurgeDateIncluded() ? "T" : "F");
            stmt.setString(index++, relDef.getPlugin());

            /* set primary key column */
            stmt.setLong(index++, relDef.getId());
            int rows = stmt.executeUpdate();

            /* RelationshipDef Extend Attributes */
            RelationshipEaDto attDto = new RelationshipEaDto();
            ArrayList<Attribute> attrList = (ArrayList<Attribute>) relDef.getAttributes();
            RelationshipEaDaoImpl relEaDao = new RelationshipEaDaoImpl(userConn);
            for (Attribute att : attrList) {
                attDto.setRelationshipDefId(relDef.getId());
                attDto.setAttributeName(att.getName());
                attDto.setColumnName(att.getColumnName());
                attDto.setColumnType(att.getType().name());
                attDto.setDefaultValue(att.getDefaultValue());
                attDto.setIsRequired(att.getIsRequired());
                attDto.setIsSearchable(att.getIsSearchable());                
                List<RelationshipEaDto> attrs = relEaDao.search(attDto);
                if (!attrs.isEmpty()) {
                    relEaDao.update(attDto);
                } else {
                    relEaDao.insert(attDto);
                }
            }
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Deletes a single row in the relationship_def table.
     */
    public void delete(long relDefId) throws RelationshipDefDaoException {
        PreparedStatement stmt = null;

        try {
            // Build DELELE SQL for RELATIONSHIP_EA table
            DeleteBuilder eaDelBld = new DeleteBuilder();
            eaDelBld.setTable(RELATIONSHIP_EA.getTableName());
            Criteria c1 = new Parameter(RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.columnName);
            eaDelBld.addCriteria(c1);
            String sqlStr = SQLBuilder.buildSQL(eaDelBld);
            stmt = userConn.prepareStatement(sqlStr);
            stmt.setLong(1, relDefId);
            int rows = stmt.executeUpdate();

            // Build DELETE SQL for RELATIONSHIP_DEF table
            DeleteBuilder relDelBld = new DeleteBuilder();
            relDelBld.setTable(RELATIONSHIP_DEF.getTableName());
            relDelBld.addCriteria(new Parameter(RELATIONSHIP_DEF.getPKColumName()));
            sqlStr = SQLBuilder.buildSQL(relDelBld);
            stmt = userConn.prepareStatement(sqlStr);
            stmt.setLong(1, relDefId);
            rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDefDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public List<RelationshipDef> search(String sourceDomain, String targetDomain) throws RelationshipDefDaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // construct the SQL statement
            String sqlStr = BuildSelectByDomains();
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, sourceDomain);
            stmt.setString(index, targetDomain);
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();
            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            throw new RelationshipDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public RelationshipDef[] getRelationshipDefs() throws RelationshipDefDaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SelectBuilder selBuilder = new SelectBuilder();
        // Assign SELECT tables
        selBuilder.setTable(RELATIONSHIP_DEF.getTableName(), RELATIONSHIP_EA.getTableName());

        // Assign column list
        for (RELATIONSHIP_DEF relDef : RELATIONSHIP_DEF.values()) {
            selBuilder.addColumns(relDef.prefixedColumnName);
        }
        // Assign column list
        for (RELATIONSHIP_EA relEa : RELATIONSHIP_EA.values()) {
            selBuilder.addColumns(relEa.prefixedColumnName);
        }
        //Add WHERE criteria
        Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        selBuilder.addCriteria(c1);
        // Build a complete SELECT SQL
        String sqlStr = SQLBuilder.buildSQL(selBuilder);
        try {
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            // fetch the results
            List<RelationshipDef> relDefs = fetchMultiResults(rs);

            return relDefs.toArray(new RelationshipDef[0]);

        } catch (Exception _e) {
            throw new RelationshipDefDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    private String BuildSelectByDomains() {
        SelectBuilder selectBld = new SelectBuilder();
        // Assign SELECT tables
        selectBld.setTable(RELATIONSHIP_DEF.getTableName());
        // Assign JOIN tables
        String[] joinTables = new String[]{RELATIONSHIP_EA.getTableName()};

        // Assign column list
        for (RELATIONSHIP_DEF rel : RELATIONSHIP_DEF.values()) {
            selectBld.addColumns(rel.prefixedColumnName);
        }
        for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
            selectBld.addColumns(ea.prefixedColumnName);
        }
        //Add WHERE criteria
        Criteria c1 = new Parameter(RELATIONSHIP_DEF.SOURCE_DOMAIN.prefixedColumnName);
        Criteria c2 = new Parameter(RELATIONSHIP_DEF.TARGET_DOMAIN.prefixedColumnName);
        selectBld.addCriteria(new AND(c1, c2));
        //Add JOIN criteria
        Criteria j1 = new JoinCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, JoinCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        selectBld.addJoin(joinTables, JoinCriteria.JOIN_TYPE.LEFTJOIN, j1);
        // Add Order By Clause
        selectBld.addOrderBy(new OrderBy(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, OrderBy.ORDER.ASC));
        // Build a complete SELECT SQL
        String sqlStr = SQLBuilder.buildSQL(selectBld);
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
     * Fetches multiple rows from the result set
     */
    protected List<RelationshipDef> fetchMultiResults(ResultSet rs) throws SQLException {
        ArrayList<RelationshipDef> resultList = new ArrayList();

        long relDefId = -1;
        RelationshipDef relDef = null;
        while (rs.next()) {
            if (relDefId != rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName)) {
                if (relDef != null) {
                    resultList.add(relDef);
                }
                relDefId = rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName);
                relDef = new RelationshipDef();
                populateRelDef(relDef, rs);
            }
            populateRelEa(relDef, rs);
        }
        if (relDef != null) {
            resultList.add(relDef);
        }
        return resultList;
    }

    /**
     * Populates a RelationshipDef object with data from a ResultSet
     */
    private void populateRelDef(RelationshipDef relDef, ResultSet rs) throws SQLException {
        /* Populate RelationshipDef object */
        relDef.setId(rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName));
        relDef.setName(rs.getString(RELATIONSHIP_DEF.RELATIONSHIP_NAME.columnName));
        relDef.setDescription(rs.getString(RELATIONSHIP_DEF.DESCRIPTION.columnName));
        relDef.setSourceDomain(rs.getString(RELATIONSHIP_DEF.SOURCE_DOMAIN.columnName));
        relDef.setTargetDomain(rs.getString(RELATIONSHIP_DEF.TARGET_DOMAIN.columnName));
        relDef.setDirection(rs.getString(RELATIONSHIP_DEF.BIDIRECTIONAL.columnName).equalsIgnoreCase("T") ? DirectionMode.BIDIRECTIONAL : DirectionMode.UNIDIRECTIONAL);
        relDef.setEffectiveFromRequired(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_FROM_REQ.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setEffectiveToRequired(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_TO_REQ.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setPurgeDateRequired(rs.getString(RELATIONSHIP_DEF.PURGE_DATE_REQ.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setEffectiveFromIncluded(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_FROM_INCL.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setEffectiveToIncluded(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_TO_INCL.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setPurgeDateIncluded(rs.getString(RELATIONSHIP_DEF.PURGE_DATE_INCL.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.setPlugin(rs.getString(RELATIONSHIP_DEF.PLUGIN.columnName));
    }

    /**
     * Populates a Relationship Extended Attributes with data from a ResultSet
     */
    private void populateRelEa(RelationshipDef relDef, ResultSet rs) throws SQLException {
        String strVal = rs.getString(RELATIONSHIP_EA.ATTRIBUTE_NAME.columnName);
        if (strVal == null) {
            return;
        }
        Attribute att = new Attribute();
        att.setName(strVal);
        att.setId(rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName));
        att.setName(rs.getString(RELATIONSHIP_EA.ATTRIBUTE_NAME.columnName));
        att.setColumnName(rs.getString(RELATIONSHIP_EA.COLUMN_NAME.columnName));
        strVal = rs.getString(RELATIONSHIP_EA.COLUMN_TYPE.columnName);
        if (strVal != null) {
            att.setType(AttributeType.valueOf(strVal.toUpperCase()));
        }
        att.setDefaultValue(rs.getString(RELATIONSHIP_EA.DEFAULT_VALUE.columnName));
        strVal = rs.getString(RELATIONSHIP_EA.IS_SEARCHABLE.columnName);
        if (strVal != null) {
            att.setIsSearchable(strVal.equalsIgnoreCase("T") ? true : false);
        }
        strVal = rs.getString(RELATIONSHIP_EA.IS_REQUIRED.columnName);
        if (strVal != null) {
            att.setIsRequired(strVal.equalsIgnoreCase("T") ? true : false);
        }
        relDef.getAttributes().add(att);
    }
}
