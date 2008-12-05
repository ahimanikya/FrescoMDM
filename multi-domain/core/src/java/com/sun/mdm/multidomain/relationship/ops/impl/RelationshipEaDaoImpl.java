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
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            stmt.setString(index++, dto.getIsIncluded() ? "T" : "F");
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
     * Updates a single row in the relationship_ea table.
     */
    public void update(long pk, RelationshipEaDto dto) throws RelationshipEaDaoException {

        PreparedStatement stmt = null;
        try {
            //Build UPDATE SQL
            UpdateBuilder updateBld = new UpdateBuilder();
            updateBld.setTable(RELATIONSHIP_EA.getTableName());
            for (RELATIONSHIP_EA ea : RELATIONSHIP_EA.values()) {
                if (ea.columnName.equalsIgnoreCase(RELATIONSHIP_DEF.getPKColumName())) {
                    updateBld.addCriteria(ea.columnName);
                } else {
                    updateBld.addColumns(ea.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(updateBld);
            stmt = mConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setLong(index++, dto.getEaId());
            stmt.setLong(index++, dto.getRelationshipDefId());
            stmt.setString(index++, dto.getAttributeName());
            stmt.setString(index++, dto.getColumnName());
            stmt.setString(index++, dto.getColumnType());
            stmt.setString(index++, dto.getDefaultValue());
            stmt.setString(index++, dto.getIsSearchable() ? "Y" : "N");
            stmt.setString(index++, dto.getIsRequired() ? "Y" : "N");
            stmt.setString(index++, dto.getIsIncluded() ? "Y" : "N");
            stmt.setLong(10, pk);
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
                    deleteBld.addCriteria(rd.columnName);
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
