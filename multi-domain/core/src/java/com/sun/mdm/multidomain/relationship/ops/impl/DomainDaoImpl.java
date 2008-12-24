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
import com.sun.mdm.multidomain.relationship.ops.dao.DomainDao;
import com.sun.mdm.multidomain.relationship.Domain;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;
import com.sun.mdm.multidomain.sql.DeleteBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.Parameter;
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DomainDaoImpl extends AbstractDAO implements DomainDao {

    private Connection mConn;
    protected int maxRows;
    private long mPrimaryKey = 0;

    /**
     * Method 'DomainDaoImpl'
     *
     */
    public DomainDaoImpl() {
    }

    /**
     * Method 'DomainDaoImpl'
     *
     * @param userConn
     */
    public DomainDaoImpl(final Connection conn) {
        this.mConn = conn;
    }

    /** 
     * Inserts a new row in the DOMAINS table.
     */
    public long insert(Domain dto) throws DaoException {
        PreparedStatement stmt = null;
        try {
            // Build INSERT SQL
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(DOMAINS.getTableName());
            for (DOMAINS d : DOMAINS.values()) {
                builder.addColumns(d.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = mConn.prepareStatement(sqlStr);
            stmt.setString(DOMAINS.DOMAIN_NAME.ordinal() + 1, dto.getDomainName());
            stmt.setString(DOMAINS.CONTEXT_FACTORY.ordinal() + 1, dto.getContextFactory());
            stmt.setString(DOMAINS.URL.ordinal() + 1, dto.getUrl());
            stmt.setString(DOMAINS.PRINCIPAL.ordinal() + 1, dto.getPrincipal());
            stmt.setString(DOMAINS.CREDENTIAL.ordinal() + 1, dto.getCredential());
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new DaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public long getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Updates a single row in the DOMAINS table.
     */
    public int update(Domain dto) throws DaoException {

        PreparedStatement stmt = null;
        try {
            //Build UPDATE SQL
            UpdateBuilder updateBld = new UpdateBuilder();
            updateBld.setTable(DOMAINS.getTableName());
            for (DOMAINS d : DOMAINS.values()) {
                if (d.columnName.equalsIgnoreCase(DOMAINS.getPKColumName())) {
                    updateBld.addCriteria(new Parameter(d.columnName));
                } else {
                    updateBld.addColumns(d.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(updateBld);
            stmt = mConn.prepareStatement(sqlStr);
            int index = 1;
            stmt.setString(index++, dto.getContextFactory());
            stmt.setString(index++, dto.getUrl());
            stmt.setString(index++, dto.getPrincipal());
            stmt.setString(index++, dto.getCredential());
            // Primary key
            stmt.setString(index++, dto.getDomainName());

            int rows = stmt.executeUpdate();
            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new DaoException("Exception: " + _e.getMessage(), _e);
        }

    }

    /**
     * Deletes a single row in the DOMAINS table.
     */
    public void delete(Domain dto) throws DaoException {

        PreparedStatement stmt = null;
        try {
            // Build DELETE SQL
            DeleteBuilder deleteBld = new DeleteBuilder();
            deleteBld.setTable(DOMAINS.getTableName());
            for (DOMAINS d : DOMAINS.values()) {
                if (d.columnName.equalsIgnoreCase(DOMAINS.getPKColumName())) {
                    deleteBld.addCriteria(new Parameter(d.columnName));
                }
            }
            String sqlStr = SQLBuilder.buildSQL(deleteBld);
            stmt = mConn.prepareStatement(sqlStr);
            stmt.setString(1, dto.getDomainName());
            int rows = stmt.executeUpdate();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new DaoException("Exception: " + _e.getMessage(), _e);
        }
    }

    public Domain[] getDomains() throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Build SELECT SQL
            SelectBuilder builder = new SelectBuilder();
            builder.setTable(DOMAINS.getTableName());
            for (DOMAINS d : DOMAINS.values()) {
                builder.addColumns(d.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = mConn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new DaoException("Exception: " + _e.getMessage(), _e);
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
    protected Domain fetchSingleResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Domain dto = new Domain();
            populateDto(dto, rs);
            return dto;
        } else {
            return null;
        }
    }

    /**
     * Fetches multiple rows from the result set
     */
    protected Domain[] fetchMultiResults(ResultSet rs) throws SQLException {
        Collection resultList = new ArrayList();
        while (rs.next()) {
            Domain dto = new Domain();
            populateDto(dto, rs);
            resultList.add(dto);
        }
        Domain domains[] = new Domain[resultList.size()];
        resultList.toArray(domains);
        return domains;
    }

    /**
     * Populates a DTO with data from a ResultSet
     */
    protected void populateDto(Domain dto, ResultSet rs) throws SQLException {
        dto.setDomainName(rs.getString(DOMAINS.DOMAIN_NAME.columnName));
        dto.setContextFactory(rs.getString(DOMAINS.CONTEXT_FACTORY.columnName));
        dto.setUrl(rs.getString(DOMAINS.URL.columnName));
        dto.setPrincipal(rs.getString(DOMAINS.PRINCIPAL.columnName));
        dto.setCredential(rs.getString(DOMAINS.CREDENTIAL.columnName));
    }
}
