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
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.relationship.RelationshipDef.DirectionMode;
import com.sun.mdm.multidomain.relationship.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEavDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import com.sun.mdm.multidomain.sql.InCriteria;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.JoinCriteria;
import com.sun.mdm.multidomain.sql.MatchCriteria;
import com.sun.mdm.multidomain.sql.OR;
import com.sun.mdm.multidomain.sql.OrderBy;
import com.sun.mdm.multidomain.sql.Parameter;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import java.util.Date;
import static com.sun.mdm.multidomain.sql.DBSchema.*;

/**
 *
 * @author David Peh
 */
public class RelationshipDaoImpl extends AbstractDAO implements RelationshipDao {

    /**
     * The factory class for this DAO has two versions of the create() method - one that
    takes no arguments and one that takes a Connection argument. If the Connection version
    is chosen then the connection will be stored in this attribute and will be used by all
    calls to this DAO, otherwise a new Connection will be allocated for each operation.
     */
    private Connection userConn;
    protected int maxRows;
    private long mPrimaryKey = 0;
    private long mRelationshipId = 0;

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
    public RelationshipDaoImpl(final Connection userConn) {
        this.userConn = userConn;
    }

    /**
     * Inserts a new row in the RelationshipDto table.
     */
    public long insert(Relationship rel) throws RelationshipDaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            InsertBuilder insert = new InsertBuilder();
            insert.setTable(RELATIONSHIP.getTableName());
            for (RELATIONSHIP r : RELATIONSHIP.values()) {
                insert.addColumns(r.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(insert);
            stmt = userConn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setNull(index++, java.sql.Types.NULL);
            stmt.setLong(index++, rel.getRelationshipDef().getId());
            stmt.setString(index++, rel.getSourceEUID());
            stmt.setString(index++, rel.getTargetEUID());
            Date dt = rel.getEffectiveFromDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);
            dt = rel.getEffectiveToDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);
            dt = rel.getPurgeDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);
            int rows = stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }

            /* Relationship Extend Attributes Values */
            if (rel.getAttributes().isEmpty() == false) {
                RelationshipEavDto attDto = new RelationshipEavDto();
                attDto.setRelationshipId(mPrimaryKey);
                attDto.setAttributes(rel.getAttributes());
                new RelationshipEavDaoImpl(userConn).insert(attDto);
            }
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Updates a single row in the Relationship table.
     */
    public int update(Relationship rel) throws RelationshipDaoException {
        PreparedStatement stmt = null;

        try {
            UpdateBuilder update = new UpdateBuilder();
            update.setTable(RELATIONSHIP.getTableName());
            for (RELATIONSHIP r : RELATIONSHIP.values()) {
                if (r.columnName.equalsIgnoreCase(RELATIONSHIP.getPKColumName())) {
                    update.addCriteria(new Parameter(r.columnName));
                } else {
                    update.addColumns(r.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(update);
            stmt = userConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setLong(index++, rel.getRelationshipDef().getId());
            stmt.setString(index++, rel.getSourceEUID());
            stmt.setString(index++, rel.getTargetEUID());
            Date dt = rel.getEffectiveFromDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);
            dt = rel.getEffectiveToDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);
            dt = rel.getPurgeDate();
            stmt.setTimestamp(index++, dt != null ? new java.sql.Timestamp(dt.getTime()) : null);

            stmt.setLong(index++, rel.getRelationshipId());  // Primary key column
            int rows = stmt.executeUpdate();

            /* RelationshipDef Extend Attributes */
            if (rel.getAttributes().isEmpty() == false) {
                RelationshipEavDto attDto = new RelationshipEavDto();
                attDto.setRelationshipId(rel.getRelationshipId());
                attDto.setAttributes(rel.getAttributes());
                new RelationshipEavDaoImpl(userConn).update(attDto);
            }
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Deletes a single row in the RelationshipDto table.
     */
    public void delete(long relId) throws RelationshipDaoException {
        PreparedStatement stmt = null;

        try {
            // Delete the extended attributes
            new RelationshipEavDaoImpl(userConn).delete(relId);

            // Build DELETE SQL for RELATIONSHIP table
            DeleteBuilder relDelete = new DeleteBuilder();
            relDelete.setTable(RELATIONSHIP.getTableName());
            relDelete.addCriteria(new Parameter(RELATIONSHIP.getPKColumName()));
            String sqlStr = SQLBuilder.buildSQL(relDelete);
            stmt = userConn.prepareStatement(sqlStr);
            stmt.setLong(1, relId);
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public List<Relationship> searchByEuids(String sourceEUID, String targetEUID) throws RelationshipDaoException {

        try {

            Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP.RELATIONSHIP_DEF_ID.prefixedColumnName);
            //Criteria c2 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
            Criteria c2 = new Parameter(RELATIONSHIP.SOURCE_EUID.prefixedColumnName);
            Criteria c3 = new Parameter(RELATIONSHIP.TARGET_EUID.prefixedColumnName);
            String sqlStr = buildSelectRelationshipDef(new AND(c1, c2, c3));

            PreparedStatement stmt = userConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setString(index++, sourceEUID);
            stmt.setString(index, targetEUID);
            ResultSet rs = stmt.executeQuery();

            RelationshipDef relDef = fetchRelationshipDef(rs);

            Criteria c5 = new MatchCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");

            sqlStr = buildSelectRelationship(relDef, c5);
            // prepare statement
            stmt = userConn.prepareStatement(sqlStr);

            stmt.setLong(1, mRelationshipId);
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();

            // fetch the results
            return fetchRelationships(rs, relDef);
        } catch (Exception _e) {
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public List<Relationship> searchByDomainEuid(String domain, String euid) throws RelationshipDaoException {

        ResultSet rs = null;

        try {
            Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP.RELATIONSHIP_DEF_ID.prefixedColumnName);
            Criteria c2 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
            Criteria c3 = new Parameter(RELATIONSHIP_DEF.SOURCE_DOMAIN.prefixedColumnName);
            Criteria c4 = new Parameter(RELATIONSHIP.SOURCE_EUID.prefixedColumnName);
            Criteria c5 = new Parameter(RELATIONSHIP_DEF.TARGET_DOMAIN.prefixedColumnName);
            Criteria c6 = new Parameter(RELATIONSHIP.TARGET_EUID.prefixedColumnName);
            String sqlStr = buildSelectRelationshipDef(new AND(c1, c2, new OR(new AND(c3, c4), new AND(c5, c6))));

            PreparedStatement stmt1 = userConn.prepareStatement(sqlStr);
            int index = 1;
            stmt1.setString(index++, domain);
            stmt1.setString(index++, euid);
            stmt1.setString(index++, domain);
            stmt1.setString(index++, euid);
            rs = stmt1.executeQuery();
            RelationshipDef relDef = fetchRelationshipDef(rs);

            Criteria c7 = new Parameter(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName);

            sqlStr = buildSelectRelationship(relDef, c7);
            // prepare statement
            PreparedStatement stmt2 = userConn.prepareStatement(sqlStr);


            stmt2.setLong(1, mRelationshipId);
            //stmt.setMaxRows(maxRows);

            rs = stmt2.executeQuery();

            // fetch the results
            return fetchRelationships(rs, relDef);
        } catch (Exception _e) {
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    /**
     * Returns all rows from the RelationshipDto table that match the criteria
     *
     */
    public List<Relationship> searchByDomainEuid(Map<String, Set<String>> sourceMap, Map<String, Set<String>> targetMap) throws RelationshipDaoException {

        ResultSet rs = null;
        List<Relationship> relList = new ArrayList<Relationship>();

        Criteria c1 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP.RELATIONSHIP_DEF_ID.prefixedColumnName);
        Criteria c2 = new MatchCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        Criteria c3 = new Parameter(RELATIONSHIP_DEF.SOURCE_DOMAIN.prefixedColumnName);
        Criteria c4 = new Parameter(RELATIONSHIP_DEF.TARGET_DOMAIN.prefixedColumnName);

        try {
            Iterator srcMapIter = sourceMap.keySet().iterator();
            while (srcMapIter.hasNext()) {
                String srcDomain = (String) srcMapIter.next();
                Set srcEuidSet = sourceMap.get(srcDomain);
                Iterator srcEuidIter = srcEuidSet.iterator();
                List<String> srcEuidList = new ArrayList<String>();
                while (srcEuidIter.hasNext()) {
                    srcEuidList.add(stringValue((String) srcEuidIter.next()));
                }
                Iterator tgtMapIter = targetMap.keySet().iterator();
                while (tgtMapIter.hasNext()) {
                    String tgtDomain = (String) tgtMapIter.next();
                    Set tgtEuidSet = targetMap.get(tgtDomain);
                    Iterator tgtEuidIter = tgtEuidSet.iterator();
                    List<String> tgtEuidList = new ArrayList<String>();
                    while (tgtEuidIter.hasNext()) {
                        tgtEuidList.add(stringValue((String) tgtEuidIter.next()));
                    }
                    Criteria c5 = new InCriteria(RELATIONSHIP.SOURCE_EUID.prefixedColumnName, srcEuidList.toArray(new String[0]));
                    Criteria c6 = new InCriteria(RELATIONSHIP.TARGET_EUID.prefixedColumnName, tgtEuidList.toArray(new String[0]));
                    String sqlStr = buildSelectRelationshipDef(new AND(c1, c2, new AND(new AND(c3, c5), new AND(c4, c6))));
                    PreparedStatement stmt1 = userConn.prepareStatement(sqlStr);
                    int index = 1;
                    stmt1.setString(index++, srcDomain);
                    stmt1.setString(index++, tgtDomain);
                    rs = stmt1.executeQuery();
                    RelationshipDef relDef = fetchRelationshipDef(rs);
                    if (relDef != null) {
                        Criteria c7 = new MatchCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, "?");

                        sqlStr = buildSelectRelationship(relDef, c7);
                        // prepare statement
                        PreparedStatement stmt2 = userConn.prepareStatement(sqlStr);
                        stmt2.setLong(1, mRelationshipId);
                        //stmt.setMaxRows(maxRows);

                        rs = stmt2.executeQuery();

                        relList.addAll(fetchRelationships(rs, relDef));
                    }
                }
            }
            // fetch the results
            return relList;
        } catch (Exception _e) {
            throw new RelationshipDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    private String stringValue(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append('\'');
        sb.append(str);
        sb.append('\'');
        return sb.toString();
    }

    private String buildSelectRelationship(RelationshipDef relDef, Criteria crit) {

        SelectBuilder select = new SelectBuilder();
        // Assign SELECT tables
        select.setTable(RELATIONSHIP.getTableName());
        // Assign OUTER JOIN tables
        String[] joinTables = new String[]{RELATIONSHIP_EAV.getTableName()};
        // Add Relationship columns to the SELECT list
        for (RELATIONSHIP rel : RELATIONSHIP.values()) {
            select.addColumns(rel.prefixedColumnName);
        }
        for (RELATIONSHIP_EAV ea : RELATIONSHIP_EAV.values()) {
            select.addColumns(ea.prefixedColumnName);
        }
        // Add Extended-Attribute columns to the SELECT list
        for (Attribute attr : relDef.getAttributes()) {
            select.addColumns(attr.getColumnName());
        }
        // Add WHERE criteria
        //Criteria c1 = new MatchCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, MatchCriteria.OPERATOR.EQUALS, RELATIONSHIP_EAV.RELATIONSHIP_ID.prefixedColumnName);
        select.addCriteria(crit);
        // Add OUTER JOIN criteria
        Criteria j1 = new JoinCriteria(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, JoinCriteria.OPERATOR.EQUALS, RELATIONSHIP_EAV.RELATIONSHIP_ID.prefixedColumnName);
        select.addJoin(joinTables, JoinCriteria.JOIN_TYPE.LEFTJOIN, j1);
        // Add Order By Clause
        select.addOrderBy(new OrderBy(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName, OrderBy.ORDER.ASC));
        String sqlStr = SQLBuilder.buildSQL(select);
        return sqlStr;
    }

    private String buildSelectRelationshipDef(Criteria crit) {
        SelectBuilder select = new SelectBuilder();
        // Assign SELECT tables
        select.setTable(RELATIONSHIP.getTableName(), RELATIONSHIP_DEF.getTableName());
        // Assign OUTER JOIN tables
        String[] joinTables = new String[]{RELATIONSHIP_EA.getTableName()};

        // Add SELECT list
        select.addColumns(RELATIONSHIP.RELATIONSHIP_ID.prefixedColumnName);
        for (RELATIONSHIP_DEF rdef : RELATIONSHIP_DEF.values()) {
            select.addColumns(rdef.prefixedColumnName);
        }
        for (RELATIONSHIP_EA relEA : RELATIONSHIP_EA.values()) {
            select.addColumns(relEA.prefixedColumnName);
        }
        // Add WHERE criteria
        select.addCriteria(crit);
        // Add OUTER JOIN
        Criteria j1 = new JoinCriteria(RELATIONSHIP_DEF.RELATIONSHIP_DEF_ID.prefixedColumnName, JoinCriteria.OPERATOR.EQUALS, RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
        select.addJoin(joinTables, JoinCriteria.JOIN_TYPE.LEFTJOIN, j1);

        String sqlStr = SQLBuilder.buildSQL(select);
        return sqlStr;
    }

    /**
     * Fetches multiple rows from the result set
     */
    private List<Relationship> fetchRelationships(ResultSet rs, RelationshipDef relDef) throws SQLException {
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
    private void populateRel(ResultSet rs, Relationship rel, List<Attribute> attrList) throws SQLException {

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
        rel.setAttributes(attrValueList);
    }

    /**
     * Populates a RelationshipDef object with data from a ResultSet
     */
    private RelationshipDef fetchRelationshipDef(ResultSet rs) throws SQLException {
        RelationshipDef relDef = null;
        while (rs.next()) {
            if (rs.isFirst()) {
                if (mRelationshipId == 0) {
                    mRelationshipId = rs.getLong(RELATIONSHIP.RELATIONSHIP_ID.columnName);
                }
                relDef = new RelationshipDef();
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

            long eaID = rs.getLong(RELATIONSHIP_EA.EA_ID.columnName);
            if (eaID > 0) {
                Attribute attr = new Attribute();
                attr.setId(eaID);
                attr.setName(rs.getString(RELATIONSHIP_EA.ATTRIBUTE_NAME.columnName));
                attr.setColumnName(rs.getString(RELATIONSHIP_EA.COLUMN_NAME.columnName));
                attr.setType(AttributeType.valueOf(rs.getString(RELATIONSHIP_EA.COLUMN_TYPE.columnName)));
                attr.setDefaultValue(rs.getString(RELATIONSHIP_EA.DEFAULT_VALUE.columnName));
                attr.setIsSearchable(rs.getString(RELATIONSHIP_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("T") ? true : false);
                attr.setIsRequired(rs.getString(RELATIONSHIP_EA.IS_REQUIRED.columnName).equalsIgnoreCase("T") ? true : false);
                relDef.getAttributes().add(attr);
            }
        }
        return relDef;
    }
}
