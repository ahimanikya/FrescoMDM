/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.sun.mdm.multidomain.hierarchy.ops.dao;

import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.*;

public interface HierarchyNodeEaDao
{
	/** 
	 * Inserts a new row in the hierarchy_ea table.
	 */
	public long insert(HierarchyNodeEaDto dto) throws HierarchyEaDaoException;

	/** 
	 * Updates a single row in the hierarchy_ea table.
	 */
	public void update(long pk, HierarchyNodeEaDto dto) throws HierarchyEaDaoException;

	/** 
	 * Deletes a single row in the hierarchy_ea table.
	 */
	public void delete(long pk) throws HierarchyEaDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();
}