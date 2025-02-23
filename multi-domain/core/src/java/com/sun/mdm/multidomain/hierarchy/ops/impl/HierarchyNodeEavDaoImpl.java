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
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.hierarchy.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeEavDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEavDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.DBSchema.HIERARCHY_NODE_EAV;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.SQLBuilder;

/**
 *
 * @author David Peh
 */
public class HierarchyNodeEavDaoImpl extends AbstractDAO implements HierarchyNodeEavDao {

    /**
     * The factory class for this DAO has two versions of the create() method - one that
    takes no arguments and one that takes a Connection argument. If the Connection version
    is chosen then the connection will be stored in this attribute and will be used by all
    calls to this DAO, otherwise a new Connection will be allocated for each operation.
     */
    private Connection userConn;
    /**
     * Finder methods will pass this value to the JDBC setMaxRows method
     */
    protected int maxRows;

    /**

    private long mPrimaryKey = 0;

    /**
     * Inserts a new row in the HierarchyNode table.
     *
     * @param  hierEav the hierarchy extended-attribute-value object to be persisted in the database
     * @return the primary key for the inserted row
     */
    public long insert(HierarchyNodeEavDto dto) throws HierarchyEavDaoException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            InsertBuilder insertBld = new InsertBuilder();
            insertBld.setTable(HIERARCHY_NODE_EAV.getTableName());
            for (HIERARCHY_NODE_EAV eav : HIERARCHY_NODE_EAV.values()) {
                insertBld.addColumns(eav.columnName);
            }
            Iterator iter = dto.getAttributes().keySet().iterator();
            ArrayList<Attribute> attrList = new ArrayList<Attribute>();
            while (iter.hasNext()) {
                Attribute attr = (Attribute) iter.next();
                attrList.add(attr);
                insertBld.addColumns(attr.getColumnName());
            }
            String sql = SQLBuilder.buildSQL(insertBld);
            stmt = userConn.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, 0);
            stmt.setLong(index++, dto.getHierarchyNodeId());
            for (int i = 0; i < attrList.size(); i++) {
                Attribute attr = attrList.get(i);
                String strValue = (String) dto.getAttributes().get(attr);
                switch (attr.getType()) {
                    case BOOLEAN:
                    case CHAR:
                        stmt.setString(index++, strValue);
                        break;
                    case STRING:
                        stmt.setString(index++, strValue);
                        break;
                    case FLOAT:
                        float floatVal = Float.valueOf(strValue.trim()).floatValue();
                        stmt.setFloat(index++, floatVal);
                    case INT:
                        long longVal = Long.valueOf(strValue.trim()).longValue();
                        stmt.setLong(index++, longVal);
                        break;
                    case DATE:
                        stmt.setTimestamp(index++, java.sql.Timestamp.valueOf(strValue));
                        break;
                    default:
                }
            }
            int rows = stmt.executeUpdate();
            reset(dto);
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEavDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Deletes a single row in the hierarchy_eav table.
     */
    public void delete(long pk) throws HierarchyEavDaoException {

        PreparedStatement stmt = null;

        try {
            //stmt = conn.prepareStatement(SQL_DELETE);

            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyEavDaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Method 'HierarchyEavDaoImpl'
     *
     */
    public HierarchyNodeEavDaoImpl() {
    }

    /**
     * Method 'HierarchyEavDaoImpl'
     *
     * @param userConn
     */
    public HierarchyNodeEavDaoImpl(final java.sql.Connection userConn) {
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
        return "hierarchy_eav";
    }

    /**
     * Fetches a single row from the result set
     */
    protected HierarchyNodeEavDto fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            HierarchyNodeEavDto dto = new HierarchyNodeEavDto();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }

    }

    /**
     * Fetches multiple rows from the result set
     */
    protected HierarchyNodeEavDto[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            HierarchyNodeEavDto dto = new HierarchyNodeEavDto();
            populateDto(dto, rs);
            resultList.add(dto);
        }

        HierarchyNodeEavDto ret[] = new HierarchyNodeEavDto[resultList.size()];
        resultList.toArray(ret);
        return ret;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    protected void populateDto(HierarchyNodeEavDto dto, ResultSet rs) throws SQLException {
        //dto.setEavId(rs.getInt(COLUMN_EAV_ID));
        // dto.setHierarchyId(rs.getInt(COLUMN_RELATIONSHIP_ID));
    }

    /**
     * Resets the modified attributes in the DTO
     */
    protected void reset(HierarchyNodeEavDto dto) {
    }

    @Override
    public long getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(long pk, HierarchyNodeEavDto dto) throws HierarchyEavDaoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
