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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.relationship.RelationshipDef.DirectionMode;
import com.sun.mdm.multidomain.relationship.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDefDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.MatchCriteria;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;

public class RelationshipDaoImpl extends AbstractDAO implements RelationshipDao {

    /**
     * The factory class for this DAO has two versions of the create() method - one that
    takes no arguments and one that takes a Connection argument. If the Connection version
    is chosen then the connection will be stored in this attribute and will be used by all
    calls to this DAO, otherwise a new Connection will be allocated for each operation.
     */
    protected java.sql.Connection userConn;
    /**
     * Finder methods will pass this value to the JDBC setMaxRows method
     */
    protected int maxRows;
    private long mPrimaryKey = 0;

    /**
     * Method 'RelationshipDaoImpl'
     *
     */
    public RelationshipDaoImpl() {
    }

    /**
     * Method 'RelationshipDaoImpl'
     *
     * @param userConn
     */
    public RelationshipDaoImpl(final java.sql.Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Inserts a new row in the RelationshipDto table.
     */
    public long insert(Relationship rel) throws RelationshipDaoException {
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(RELATIONSHIP.getTableName());
            for (RELATIONSHIP r : RELATIONSHIP.values()) {
                builder.addColumns(r.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = conn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setNull(index++, java.sql.Types.INTEGER);  // Primary key column
            stmt.setLong(index++, rel.getRelationshipDef().getId());
            stmt.setString(index++, rel.getSourceEUID());
            stmt.setString(index++, rel.getTargetEUID());
            stmt.setTimestamp(index++, (java.sql.Timestamp) rel.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) rel.getEffectiveToDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) rel.getPurgeDate());

            int rows = stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the RelationshipDto table.
     */
    public void update(long pk, RelationshipDto dto) throws RelationshipDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            //System.out.println("Executing " + SQL_UPDATE + " with DTO: " + dto);
            // stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setLong(index++, dto.getRelationshipId());
            stmt.setString(index++, dto.getSourceEuid());
            stmt.setString(index++, dto.getTargetEuid());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getPurgeDate());
            stmt.setLong(14, pk);
            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
        }
    }

    /**
     * Deletes a single row in the RelationshipDto table.
     */
    public void delete(long pk) throws RelationshipDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            //System.out.println("Executing " + SQL_DELETE + " with PK: " + pk);
            // stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setLong(1, pk);
            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
        }
    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public List<Relationship> search(String sourceEUID, String targetEUID) throws RelationshipDaoException {
        // declare variables

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            RelationshipDef relDef = getRelationshipDef(userConn, sourceEUID, targetEUID);
            String sqlStr = BuildSelectSQL(relDef);
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);

            stmt.setLong(1, getPrimaryKey());
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs, relDef);
        } catch (Exception _e) {
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    private RelationshipDef getRelationshipDef(Connection conn, String sourceEUID, String targetEUID) throws SQLException {
        SelectBuilder selectBld = new SelectBuilder();
        String relEATable = RELATIONSHIP_EA.getTableName();
        String relTable = RELATIONSHIP.getTableName();
        String relDefTable = RELATIONSHIP_DEF.getTableName();
        selectBld.setTable(relDefTable, relTable, relEATable);
        selectBld.addColumns(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName);
        for (RELATIONSHIP_DEF relDef : RELATIONSHIP_DEF.values()) {
            selectBld.addColumns(relDef.prefixedColumnName);
        }
        for (RELATIONSHIP_EA relEA : RELATIONSHIP_EA.values()) {
            selectBld.addColumns(relEA.prefixedColumnName);
        }
        Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP.RELATIONSHIP_DEF_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        Criteria c3 = new MatchCriteria(RELATIONSHIP.SOURCE_EUID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        Criteria c4 = new MatchCriteria(RELATIONSHIP.TARGET_EUID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        selectBld.addCriteria(new AND(c1, c2, c3, c4));
        String sqlStr = SQLBuilder.buildSQL(selectBld);
        PreparedStatement stmt = conn.prepareStatement(sqlStr);
        int index = 1;
        stmt.setString(index++, sourceEUID);
        stmt.setString(index, targetEUID);
        ResultSet rs = stmt.executeQuery();
        RelationshipDef relDef = new RelationshipDef();
        while (rs.next()) {
            if (rs.isFirst()) {
                if (mPrimaryKey == 0) {
                    mPrimaryKey = rs.getLong(RELATIONSHIP.RELATIONSHIP_ID.columnName);
                }
                relDef.setId(rs.getLong(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.columnName));
                relDef.setName(rs.getString(RELATIONSHIP_DEF.RELATIONSHIP_NAME.columnName));
                relDef.setDescription(rs.getString(RELATIONSHIP_DEF.DESCRIPTION.columnName));
                relDef.setSourceDomain(rs.getString(RELATIONSHIP_DEF.SOURCE_DOMAIN.columnName));
                relDef.setTargetDomain(rs.getString(RELATIONSHIP_DEF.TARGET_DOMAIN.columnName));
                relDef.setDirection(rs.getString(RELATIONSHIP_DEF.BIDIRECTIONAL.columnName).equalsIgnoreCase("T") ? DirectionMode.BIDIRECTIONAL : DirectionMode.UNIDIRECTIONAL);
                relDef.setEffectiveFromRequired(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_FROM_REQ.columnName).equalsIgnoreCase("T") ? true : false);
                relDef.setEffectiveToRequired(rs.getString(RELATIONSHIP_DEF.EFFECTIVE_TO_REQ.columnName).equalsIgnoreCase("T") ? true : false);
                relDef.setPurgeDateRequired(rs.getString(RELATIONSHIP_DEF.PURGE_DATE_REQ.columnName).equalsIgnoreCase("T") ? true : false);
                relDef.setPlugin(rs.getString(RELATIONSHIP_DEF.PLUGIN.columnName));
            }
            Attribute attr = new Attribute();
            attr.setId(rs.getLong(RELATIONSHIP_EA.EA_ID.columnName));
            attr.setName(rs.getString(RELATIONSHIP_EA.ATTRIBUTE_NAME.columnName));
            attr.setColumnName(rs.getString(RELATIONSHIP_EA.COLUMN_NAME.columnName));
            attr.setType(AttributeType.valueOf(rs.getString(RELATIONSHIP_EA.COLUMN_TYPE.columnName)));
            attr.setDefaultValue(rs.getString(RELATIONSHIP_EA.DEFAULT_VALUE.columnName));
            attr.setIsSearchable(rs.getString(RELATIONSHIP_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("Y") ? true : false);
            attr.setIsRequired(rs.getString(RELATIONSHIP_EA.IS_REQUIRED.columnName).equalsIgnoreCase("Y") ? true : false);
            attr.setIsIncluded(rs.getString(RELATIONSHIP_EA.IS_INCLUDED.columnName).equalsIgnoreCase("Y") ? true : false);
            relDef.getAttributes().add(attr);
        }
        return relDef;
    }

    private String BuildSelectSQL(RelationshipDef relDef) {
        SelectBuilder builder = new SelectBuilder();
        builder.setTable(RELATIONSHIP.getTableName(), RELATIONSHIP_EAV.getTableName());

        for (RELATIONSHIP rel : RELATIONSHIP.values()) {
            builder.addColumns(rel.prefixedColumnName);
        }
        for (RELATIONSHIP_EAV ea : RELATIONSHIP_EAV.values()) {
            builder.addColumns(ea.prefixedColumnName);
        }
        for (Attribute attr : relDef.getAttributes()) {
            builder.addColumns(attr.getColumnName());
        }
        Criteria c1 = new MatchCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");
        Criteria c2 = new MatchCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EAV.RELATIONSHIP_ID.prefixedColumnName);
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
     * Fetches multiple rows from the result set
     */
    protected List<Relationship> fetchMultiResults(ResultSet rs, RelationshipDef relDef) throws SQLException {
        ArrayList<Relationship> resultList = new ArrayList<Relationship>();
        while (rs.next()) {
            Relationship rel = new Relationship();
            populateRel(rs, rel, relDef.getAttributes());
            rel.setRelationshipDef(relDef);
            resultList.add(rel);
        }
        return resultList;
    }

    /**
     * Populates a Relationship object with data from a ResultSet
     */
    protected void populateRel(ResultSet rs, Relationship rel, List<Attribute> attrList) throws SQLException {

        rel.setRelationshipId(rs.getLong(RELATIONSHIP.RELATIONSHIP_ID.columnName));
        rel.setSourceEUID(rs.getString(RELATIONSHIP.SOURCE_EUID.columnName));
        rel.setTargetEUID(rs.getString(RELATIONSHIP.TARGET_EUID.columnName));
        rel.setEffectiveFromDate(rs.getDate(RELATIONSHIP.EFFECTIVE_FROM_DATE.columnName));
        rel.setEffectiveToDate(rs.getDate(RELATIONSHIP.EFFECTIVE_TO_DATE.columnName));
        rel.setPurgeDate(rs.getDate(RELATIONSHIP.PURGE_DATE.columnName));
        Map attrValueList = rel.getAttributes();
        for (Attribute attr : attrList) {
            String attrValue = null;
            switch (attr.getType()) {
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
}
