/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
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
import com.sun.mdm.multidomain.sql.SQLBuilder;
import com.sun.mdm.multidomain.sql.InsertBuilder;
import com.sun.mdm.multidomain.sql.SelectBuilder;
import com.sun.mdm.multidomain.sql.UpdateBuilder;
import static com.sun.mdm.multidomain.sql.DBSchema.*;

public class HierarchyDefDaoImpl extends AbstractDAO implements HierarchyDefDao {

    /**
     * The factory class for this DAO has two versions of the create() method - one that
    takes no arguments and one that takes a Connection argument. If the Connection version
    is chosen then the connection will be stored in this attribute and will be used by all
    calls to this DAO, otherwise a new Connection will be allocated for each operation.
     */
    protected java.sql.Connection userConn;
    private long mPrimaryKey = 0;
    private int maxRows;

    /**
     * Inserts a new row in the hierarchy_def table.
     */
    public long insert(HierarchyDefDto dto) throws HierarchyDefDaoException {

        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();
            InsertBuilder builder = new InsertBuilder();
            builder.setTable(HIERARCHY_DEF.getTableName());
            for (HIERARCHY_DEF hier : HIERARCHY_DEF.values()) {
                builder.addColumns(hier.columnName);
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            stmt = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setLong(index++, 0);
            stmt.setString(index++, dto.getHierarchyName());
            stmt.setString(index++, dto.getDescription());
            stmt.setString(index++, dto.getDomain());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            stmt.setString(index++, dto.getPlugIn());

            System.out.println("Executing " + sqlStr + " with DTO: " + dto);
            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
            // retrieve values from auto-increment columns
            rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                mPrimaryKey = rs.getLong(1);
            }
            return mPrimaryKey;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(rs);
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
     * Updates a single row in the hierarchy_def table.
     */
    public int update(HierarchyDefDto dto) throws HierarchyDefDaoException {

        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();
            UpdateBuilder builder = new UpdateBuilder();
            builder.setTable(HIERARCHY_DEF.getTableName());
            for (HIERARCHY_DEF rel : HIERARCHY_DEF.values()) {
                switch (rel) {
                    case HIERARCHY_DEF_ID:
                        builder.addCriteria(rel.columnName);
                        break;
                    default:
                        builder.addColumns(rel.columnName);
                }
            }
            String sqlStr = SQLBuilder.buildSQL(builder);
            System.out.println("Executing " + sqlStr + " with DTO: " + dto);
            stmt = conn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, dto.getHierarchyName());
            stmt.setString(index++, dto.getDescription());
            stmt.setString(index++, dto.getDomain());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveFromDate());
            stmt.setTimestamp(index++, (java.sql.Timestamp) dto.getEffectiveToDate());
            stmt.setString(index++, dto.getPlugIn());

            /* set update SQL criteria */
            stmt.setLong(index++, dto.getHierarchyDefId());
            int rows = stmt.executeUpdate();

            return rows;
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
        }
    }

    /**
     * Deletes a single row in the hierarchy_def table.
     */
    public void delete(long pk) throws HierarchyDefDaoException {
        long t1 = System.currentTimeMillis();
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();


            // stmt = conn.prepareStatement(SQL_DELETE);

            int rows = stmt.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(rows + " rows affected (" + (t2 - t1) + " ms)");
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
        }

    }

    /**
     * Returns all rows from the HierarchyDto table that match the criteria
     *
     */
    public List<HierarchyDefDto> search(String domain) throws HierarchyDefDaoException {
        // declare variables
        final boolean isConnSupplied = (userConn != null);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // get the user-specified connection or get a connection from the ResourceManager
            conn = isConnSupplied ? userConn : ResourceManager.getConnection();

            // construct the SQL statement

            String sqlStr = BuildSelectByDomain();
            System.out.println("Executing " + sqlStr);
            // prepare statement
            stmt = conn.prepareStatement(sqlStr);

            int index = 1;
            stmt.setString(index++, domain);
            //stmt.setMaxRows(maxRows);

            rs = stmt.executeQuery();

            // fetch the results
            return fetchMultiResults(rs);
        } catch (Exception _e) {
            throw new HierarchyDefDaoException("Exception: " + _e.getMessage(), _e);
        } finally {
            ResourceManager.close(rs);
            ResourceManager.close(stmt);
            if (!isConnSupplied) {
                ResourceManager.close(conn);
            }
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
        builder.addCriteria(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName, HIERARCHY_NODE_EA.HIERARCHY_DEF_ID.prefixedColumnName);
        builder.addCriteria(HIERARCHY_DEF.DOMAIN.prefixedColumnName, null);
        builder.addOrderBy(HIERARCHY_DEF.HIERARCHY_DEF_ID.prefixedColumnName);
        String sqlStr = SQLBuilder.buildSQL(builder);
        return sqlStr;
    }

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
    public HierarchyDefDaoImpl(final java.sql.Connection userConn) {
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
        dto.setIsSearchable(rs.getString(HIERARCHY_NODE_EA.IS_SEARCHABLE.columnName).equalsIgnoreCase("Y") ? true : false);
        dto.setIsRequired(rs.getString(HIERARCHY_NODE_EA.IS_REQUIRED.columnName).equalsIgnoreCase("Y") ? true : false);
        dto.setIsIncluded(rs.getString(HIERARCHY_NODE_EA.IS_INCLUDED.columnName).equalsIgnoreCase("Y") ? true : false);
    }
}