/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */
package com.sun.mdm.multidomain.hierarchy.ops.dao;

import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEavDto;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.*;

public interface HierarchyNodeEavDao {

    /**
     * Inserts a new row in the hierarchy_eav table.
     */
    public long insert(HierarchyNodeEavDto dto) throws HierarchyEavDaoException;

    /**
     * Updates a single row in the hierarchy_eav table.
     */
    public void update(long pk, HierarchyNodeEavDto dto) throws HierarchyEavDaoException;

    /**
     * Deletes a single row in the hierarchy_eav table.
     */
    public void delete(long pk) throws HierarchyEavDaoException;

    /**
     * Sets the value of maxRows
     */
    public void setMaxRows(int maxRows);

    /**
     * Gets the value of maxRows
     */
    public int getMaxRows();
}