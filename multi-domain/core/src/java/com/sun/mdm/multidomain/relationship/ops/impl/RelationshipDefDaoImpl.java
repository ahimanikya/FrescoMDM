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
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDefDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDefDaoException;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.InsertBuilder;
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
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_FROM_INCL.ordinal()+ 1, relDef.getEffectiveFromIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.EFFECTIVE_TO_INCL.ordinal() + 1, relDef.getEffectiveToIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.PURGE_DATE_INCL.ordinal() + 1, relDef.getPurgeDateIncluded() ? "T" : "F");
            stmt.setString(RELATIONSHIP_DEF.PLUGIN.ordinal() + 1, relDef.getPlugin());

            int rows = stmt.executeUpdate();
            // retrieve values from auto-increment columns
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
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
    public int update(RelationshipDefDto dto) throws RelationshipDefDaoException {
        PreparedStatement stmt = null;

        try {
            UpdateBuilder builder = new UpdateBuilder();
            builder.setTable(RELATIONSHIP_DEF.getTableName());
            for (RELATIONSHIP_DEF rel : RELATIONSHIP_DEF.values()) {
                switch (rel) {
                    case RELATIONSHIP_DEF_ID:
                        builder.addCriteria(rel.columnName);
                        break;
                    default:
                        builder.addColumns(rel.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = userConn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, dto.getRelationshipName());
            stmt.setString(index++, dto.getDescription());
            stmt.setString(index++, dto.getSourceDomain());
            stmt.setString(index++, dto.getTargetDomain());
            stmt.setString(index++, dto.getBidirectional());
            stmt.setString(index++, dto.getEffectiveFromReq());
            stmt.setString(index++, dto.getEffectiveToReq());
            stmt.setString(index++, dto.getPurgeDateReq());
            stmt.setString(index++, dto.getEffectiveToInc());
            stmt.setString(index++, dto.getEffectiveFromInc());
            stmt.setString(index++, dto.getPurgeDateInc());
            stmt.setString(index++, dto.getPlugIn());

            /* set update SQL criteria */
            stmt.setLong(index++, dto.getRelationshipDefId());
            int rows = stmt.executeUpdate();

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
        selBuilder.setTable(RELATIONSHIP_DEF.getTableName(), RELATIONSHIP_EA.getTableName());

        // Build  SELECT SQL
        for (RELATIONSHIP_DEF relDef : RELATIONSHIP_DEF.values()) {
            selBuilder.addColumns(relDef.prefixedColumnName);
        }
        // Build  SELECT SQL
        for (RELATIONSHIP_EA relEa : RELATIONSHIP_EA.values()) {
            selBuilder.addColumns(relEa.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        selBuilder.addCriteria(c1);
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
        selectBld.setTable(RELATIONSHIP_DEF.getTableName(), RELATIONSHIP_EA.getTableName());

        for (RELATIONSHIP_DEF rel : RELATIONSHIP_DEF.values()) {
            selectBld.addColumns(rel.prefixedColumnName);
        }
        for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
            selectBld.addColumns(ea.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        //Criteria c2 = new MatchCriteria(RELATIONSHIP_DEF.SOURCE_DOMAIN.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        //Criteria c3 = new MatchCriteria(RELATIONSHIP_DEF.TARGET_DOMAIN.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        Criteria c2 = new Parameter(RELATIONSHIP_DEF.SOURCE_DOMAIN.prefixedColumnName);
        Criteria c3 = new Parameter(RELATIONSHIP_DEF.TARGET_DOMAIN.prefixedColumnName);
        selectBld.addCriteria(new AND(c1, c2, c3));
        selectBld.addOrderBy(new OrderBy(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, OrderBy.ORDER.ASC));
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
        Attribute att = new Attribute();
        att.setId(rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName));
        att.setName(rs.getString(RELATIONSHIP_EA.ATTRIBUTE_NAME.columnName));
        att.setColumnName(rs.getString(RELATIONSHIP_EA.COLUMN_NAME.columnName));
        att.setType(AttributeType.valueOf(rs.getString(RELATIONSHIP_EA.COLUMN_TYPE.columnName).toUpperCase()));
        att.setDefaultValue(rs.getString(RELATIONSHIP_EA.DEFAULT_VALUE.columnName));
        att.setIsSearchable(rs.getString(RELATIONSHIP_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("T") ? true : false);
        att.setIsRequired(rs.getString(RELATIONSHIP_EA.IS_REQUIRED.columnName).equalsIgnoreCase("T") ? true : false);
        relDef.getAttributes().add(att);
    }
}
