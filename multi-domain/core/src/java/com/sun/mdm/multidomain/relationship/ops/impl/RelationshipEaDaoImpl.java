/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.sun.mdm.multidomain.relationship.ops.impl;

import com.sun.mdm.multidomain.relationship.ops.dao.AbstractDAO;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEaDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;

import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RelationshipEaDaoImpl extends AbstractDAO implements RelationshipEaDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT EA_ID, RELATIONSHIP_DEF_ID, ATTRIBUTE_NAME, COLUMN_NAME, COLUMN_TYPE, DEFAULT_VALUE, IS_SEARCHABLE, IS_REQUIRED, IS_INCLUDED FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( EA_ID, RELATIONSHIP_DEF_ID, ATTRIBUTE_NAME, COLUMN_NAME, COLUMN_TYPE, DEFAULT_VALUE, IS_SEARCHABLE, IS_REQUIRED, IS_INCLUDED ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET EA_ID = ?, RELATIONSHIP_DEF_ID = ?, ATTRIBUTE_NAME = ?, COLUMN_NAME = ?, COLUMN_TYPE = ?, DEFAULT_VALUE = ?, IS_SEARCHABLE = ?, IS_REQUIRED = ?, IS_INCLUDED = ? WHERE EA_ID = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE EA_ID = ?";

	/** 
	 * Index of column EA_ID
	 */
	protected static final int COLUMN_EA_ID = 1;

	/** 
	 * Index of column RELATIONSHIP_DEF_ID
	 */
	protected static final int COLUMN_RELATIONSHIP_DEF_ID = 2;

	/** 
	 * Index of column ATTRIBUTE_NAME
	 */
	protected static final int COLUMN_ATTRIBUTE_NAME = 3;

	/** 
	 * Index of column COLUMN_NAME
	 */
	protected static final int COLUMN_COLUMN_NAME = 4;

	/** 
	 * Index of column COLUMN_TYPE
	 */
	protected static final int COLUMN_COLUMN_TYPE = 5;

	/** 
	 * Index of column DEFAULT_VALUE
	 */
	protected static final int COLUMN_DEFAULT_VALUE = 6;

	/** 
	 * Index of column IS_SEARCHABLE
	 */
	protected static final int COLUMN_IS_SEARCHABLE = 7;

	/** 
	 * Index of column IS_REQUIRED
	 */
	protected static final int COLUMN_IS_REQUIRED = 8;

	/** 
	 * Index of column IS_INCLUDED
	 */
	protected static final int COLUMN_IS_INCLUDED = 9;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 9;

	/** 
	 * Index of primary-key column EA_ID
	 */
	protected static final int PK_COLUMN_EA_ID = 1;

    private long mPrimaryKey = 0;

	/** 
	 * Inserts a new row in the relationship_ea table.
	 */
	public long insert(RelationshipEaDto dto) throws RelationshipEaDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_INSERT );
			int index = 1;
			stmt.setLong( index++, dto.getEaId() );
			stmt.setLong( index++, dto.getRelationshipDefId() );
			stmt.setString( index++, dto.getAttributeName() );
			stmt.setString( index++, dto.getColumnName() );
			stmt.setString( index++, dto.getColumnType() );
			stmt.setString( index++, dto.getDefaultValue() );
			stmt.setString( index++, dto.getIsSearchable() ? "Y" : "N");
			stmt.setString( index++, dto.getIsRequired() ? "Y" : "N");
			stmt.setString( index++, dto.getIsIncluded() ? "Y" : "N");
			System.out.println( "Executing " + SQL_INSERT + " with DTO: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return rows;
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new RelationshipEaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

      public long getPrimaryKey () {
        return mPrimaryKey;
    }

	/** 
	 * Updates a single row in the relationship_ea table.
	 */
	public void update(long pk, RelationshipEaDto dto) throws RelationshipEaDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_UPDATE + " with DTO: " + dto );
			stmt = conn.prepareStatement( SQL_UPDATE );
			int index=1;
			stmt.setLong( index++, dto.getEaId() );
			stmt.setLong( index++, dto.getRelationshipDefId() );
			stmt.setString( index++, dto.getAttributeName() );
			stmt.setString( index++, dto.getColumnName() );
			stmt.setString( index++, dto.getColumnType() );
			stmt.setString( index++, dto.getDefaultValue() );
			stmt.setString( index++, dto.getIsSearchable() ? "Y" : "N");
			stmt.setString( index++, dto.getIsRequired() ? "Y" : "N");
			stmt.setString( index++, dto.getIsIncluded()? "Y" : "N");
			stmt.setLong( 10, pk );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new RelationshipEaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the relationship_ea table.
	 */
	public void delete(long pk) throws RelationshipEaDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE + " with PK: " + pk );
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setLong( 1, pk );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new RelationshipEaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the relationship_ea table that matches the specified primary-key value.
	 */
	public RelationshipEaDto findByPrimaryKey(long pk) throws RelationshipEaDaoException
	{
		return findByPrimaryKey( pk);
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'EA_ID = :eaId'.
	 */
	public RelationshipEaDto findByPrimaryKey(int eaId) throws RelationshipEaDaoException
	{
		RelationshipEaDto ret[] = findByDynamicSelect( SQL_SELECT + " WHERE EA_ID = ?", new Object[] {  new Integer(eaId) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria ''.
	 */
	public RelationshipEaDto[] findAll() throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY EA_ID", null );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'RELATIONSHIP_DEF_ID = :relationshipDefId'.
	 */
	public RelationshipEaDto[] findByRelationshipDef(int relationshipDefId) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RELATIONSHIP_DEF_ID = ?", new Object[] {  new Integer(relationshipDefId) } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'EA_ID = :eaId'.
	 */
	public RelationshipEaDto[] findWhereEaIdEquals(int eaId) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE EA_ID = ? ORDER BY EA_ID", new Object[] {  new Integer(eaId) } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'RELATIONSHIP_DEF_ID = :relationshipDefId'.
	 */
	public RelationshipEaDto[] findWhereRelationshipDefIdEquals(int relationshipDefId) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RELATIONSHIP_DEF_ID = ? ORDER BY RELATIONSHIP_DEF_ID", new Object[] {  new Integer(relationshipDefId) } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'ATTRIBUTE_NAME = :attributeName'.
	 */
	public RelationshipEaDto[] findWhereAttributeNameEquals(String attributeName) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ATTRIBUTE_NAME = ? ORDER BY ATTRIBUTE_NAME", new Object[] { attributeName } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'COLUMN_NAME = :columnName'.
	 */
	public RelationshipEaDto[] findWhereColumnNameEquals(String columnName) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE COLUMN_NAME = ? ORDER BY COLUMN_NAME", new Object[] { columnName } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'COLUMN_TYPE = :columnType'.
	 */
	public RelationshipEaDto[] findWhereColumnTypeEquals(String columnType) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE COLUMN_TYPE = ? ORDER BY COLUMN_TYPE", new Object[] { columnType } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'DEFAULT_VALUE = :defaultValue'.
	 */
	public RelationshipEaDto[] findWhereDefaultValueEquals(String defaultValue) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DEFAULT_VALUE = ? ORDER BY DEFAULT_VALUE", new Object[] { defaultValue } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'IS_SEARCHABLE = :isSearchable'.
	 */
	public RelationshipEaDto[] findWhereIsSearchableEquals(String isSearchable) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IS_SEARCHABLE = ? ORDER BY IS_SEARCHABLE", new Object[] { isSearchable } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'IS_REQUIRED = :isRequired'.
	 */
	public RelationshipEaDto[] findWhereIsRequiredEquals(String isRequired) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IS_REQUIRED = ? ORDER BY IS_REQUIRED", new Object[] { isRequired } );
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the criteria 'IS_INCLUDED = :isIncluded'.
	 */
	public RelationshipEaDto[] findWhereIsIncludedEquals(String isIncluded) throws RelationshipEaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IS_INCLUDED = ? ORDER BY IS_INCLUDED", new Object[] { isIncluded } );
	}

	/**
	 * Method 'RelationshipEaDaoImpl'
	 * 
	 */
	public RelationshipEaDaoImpl()
	{
	}

	/**
	 * Method 'RelationshipEaDaoImpl'
	 * 
	 * @param userConn
	 */
	public RelationshipEaDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "relationship_ea";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected RelationshipEaDto fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			RelationshipEaDto dto = new RelationshipEaDto();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected RelationshipEaDto[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			RelationshipEaDto dto = new RelationshipEaDto();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		RelationshipEaDto ret[] = new RelationshipEaDto[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(RelationshipEaDto dto, ResultSet rs) throws SQLException
	{
		dto.setEaId( rs.getInt( COLUMN_EA_ID ) );
		dto.setRelationshipDefId( rs.getInt( COLUMN_RELATIONSHIP_DEF_ID ) );
		dto.setAttributeName( rs.getString( COLUMN_ATTRIBUTE_NAME ) );
		dto.setColumnName( rs.getString( COLUMN_COLUMN_NAME ) );
		dto.setColumnType( rs.getString( COLUMN_COLUMN_TYPE ) );
		dto.setDefaultValue( rs.getString( COLUMN_DEFAULT_VALUE ) );
		dto.setIsSearchable( rs.getString( COLUMN_IS_SEARCHABLE ).equalsIgnoreCase("Y") ? true : false );
		dto.setIsRequired( rs.getString( COLUMN_IS_REQUIRED ).equalsIgnoreCase("Y") ? true : false );
		dto.setIsIncluded( rs.getString( COLUMN_IS_INCLUDED ).equalsIgnoreCase("Y") ? true : false );
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(RelationshipEaDto dto)
	{
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the specified arbitrary SQL statement
	 */
	public RelationshipEaDto[] findByDynamicSelect(String sql, Object[] sqlParams) throws RelationshipEaDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new RelationshipEaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the relationship_ea table that match the specified arbitrary SQL statement
	 */
	public RelationshipEaDto[] findByDynamicWhere(String sql, Object[] sqlParams) throws RelationshipEaDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new RelationshipEaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

}