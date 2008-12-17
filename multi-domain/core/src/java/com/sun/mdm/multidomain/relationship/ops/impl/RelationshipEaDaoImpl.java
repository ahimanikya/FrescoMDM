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

import com.sun.mdm.multidomain.relationship.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEaDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.AND;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.Parameter;
import com.sun.mdm.multidomain.sql.Criteria;
import com.sun.mdm.multidomain.sql.OrderBy;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;

import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 *
 * @author David Peh
 */
public class RelationshipEaDaoImpl extends AbstractDAO implements RelationshipEaDao {

    protected Connection mConn;
    protected int maxRows;
    private long mPrimaryKey = 0;

    /**
     * Method 'RelationshipEaDaoImpl'
     *
     */
    public RelationshipEaDaoImpl() {
    }

    /**
     * Method 'RelationshipEaDaoImpl'
     *
     * @param userConn
     */
    public RelationshipEaDaoImpl(final Connection conn) {
        this.mConn = conn;
    }

    /** 
     * Inserts a new row in the relationship_ea table.
     */
    public long insert(RelationshipEaDto dto) throws RelationshipEaDaoException {
        PreparedStatement stmt = null;
        try {
            // Build INSERT SQL
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(RELATIONSHIP_EA.getTableName());
            for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
                builder.addColumns(ea.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = mConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setNull(index++, java.sql.Types.INTEGER);
            stmt.setLong(index++, dto.getRelationshipDefId());
            stmt.setString(index++, dto.getAttributeName());
            stmt.setString(index++, dto.getColumnName());
            stmt.setString(index++, dto.getColumnType());
            stmt.setString(index++, dto.getDefaultValue());
            stmt.setString(index++, dto.getIsSearchable() ? "T" : "F");
            stmt.setString(index++, dto.getIsRequired() ? "T" : "F");
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipEaDaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Search Relationship extended attributes.
     * @param dto RelationshipEaDto.
     * @return List<Attribute> List of Attribute.
     * @throws RelationshipEaDaoException
     */
    public List<RelationshipEaDto> search(RelationshipEaDto dto) 
        throws RelationshipEaDaoException {
        List<RelationshipEaDto> attributes = new ArrayList<RelationshipEaDto>();        
        PreparedStatement stmt = null;
        try {
            //Build SELECT SQL
            SelectBuilder selectBld = new SelectBuilder();
            selectBld.setTable(RELATIONSHIP_EA.getTableName());     
            
            for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
                selectBld.addColumns(ea.prefixedColumnName);
            }
            
            //Add WHERE criteria
            Criteria c1 = new Parameter(RELATIONSHIP_EA.RELATIONSHIP_DEF_ID.prefixedColumnName);
            Criteria c2 = new Parameter(RELATIONSHIP_EA.ATTRIBUTE_NAME.prefixedColumnName);
            Criteria c3 = new Parameter(RELATIONSHIP_EA.COLUMN_NAME.prefixedColumnName);            
            selectBld.addCriteria(new AND(c1, c2, c3));
            // Add Order By Clause
            selectBld.addOrderBy(new OrderBy(RELATIONSHIP_EA.EA_ID.prefixedColumnName, OrderBy.ORDER.ASC));
            // Build a complete SELECT SQL
            String sqlStr = SQLBuilder.buildSQL(selectBld);
            stmt = mConn.prepareStatement(sqlStr);
            
            int index = 1;
            stmt.setLong(index++, dto.getRelationshipDefId());
            stmt.setString(index++, dto.getAttributeName());            
            stmt.setString(index, dto.getColumnName());
            ResultSet rs = stmt.executeQuery();     
            RelationshipEaDto[] rEaDtos = fetchMultiResults(rs);
            attributes = Arrays.asList(rEaDtos);
            
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipEaDaoException("Exception: " + _e.getMessage(), _e);
        }
        return attributes;
    }
                        
    /**
     * Updates a single row in the relationship_ea table.
     */
    public void update(RelationshipEaDto dto) throws RelationshipEaDaoException {

        PreparedStatement stmt = null;
        try {
            //Build UPDATE SQL
            UpdateBuilder updateBld = new UpdateBuilder();
            updateBld.setTable(RELATIONSHIP_EA.getTableName());
            Parameter p1 = null, p2 = null;
            for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
                switch (ea) {
                    case RELATIONSHIP_DEF_ID:
                        p1 = new Parameter(ea.columnName);
                        break;
                    case ATTRIBUTE_NAME:
                        p2 = new Parameter(ea.columnName);
                        break;
                    case EA_ID:
                        // DO NOT update primary key
                        break;
                    default:
                        updateBld.addColumns(ea.columnName);
                }
            }
            updateBld.addCriteria(new AND(p1, p2));
            String sqlStr = SQLBuilder.buildSQL(updateBld);
            stmt = mConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setString(index++, dto.getColumnName());
            stmt.setString(index++, dto.getColumnType());
            stmt.setString(index++, dto.getDefaultValue());
            stmt.setString(index++, dto.getIsSearchable() ? "T" : "F");
            stmt.setString(index++, dto.getIsRequired() ? "T" : "F");
            stmt.setLong(index++, dto.getRelationshipDefId());
            stmt.setString(index++, dto.getAttributeName());
            int rows = stmt.executeUpdate();

        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipEaDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Deletes a single row in the relationship_ea table.
     */
    public void delete(long pk) throws RelationshipEaDaoException {

        PreparedStatement stmt = null;
        try {
            // Build DELETE SQL
            DeleteBuilder deleteBld = new DeleteBuilder();
            deleteBld.setTable(RELATIONSHIP_DEF.getTableName());
            for (RELATIONSHIP_DEF rd : RELATIONSHIP_DEF.values()) {
                if (rd.columnName.equalsIgnoreCase(RELATIONSHIP_DEF.getPKColumName())) {
                    deleteBld.addCriteria(new Parameter(rd.columnName));
                }
            }
            String sqlStr = SQLBuilder.buildSQL(deleteBld);
            stmt = mConn.prepareStatement(sqlStr);
            stmt.setLong(1, pk);
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RelationshipEaDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);

        }

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
    protected RelationshipEaDto fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            RelationshipEaDto dto = new RelationshipEaDto();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Fetches multiple rows from the result set
     */
    protected RelationshipEaDto[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            RelationshipEaDto dto = new RelationshipEaDto();
            populateDto(dto, rs);
            resultList.add(dto);
        }
        RelationshipEaDto ret[] = new RelationshipEaDto[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    protected void populateDto(RelationshipEaDto dto, ResultSet rs) throws SQLException {
    }
}
