/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.sun.mdm.multidomain.hierarchy.ops.dao;

import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.*;

public interface HierarchyNodeDao
{
	/** 
	 * Inserts a new row in the HierarchyNodeDto table.
	 */
	public long insert(HierarchyNode hierNode) throws HierarchyDaoException;

	/** 
	 * Updates a single row in the HierarchyNodeDto table.
	 */
	public void update(long pk, HierarchyNodeDto dto) throws HierarchyDaoException;

	/** 
	 * Deletes a single row in the HierarchyNodeDto table.
	 */
	public void delete(long pk) throws HierarchyDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

}
