/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */
package com.sun.mdm.multidomain.relationship.ops.dao;

import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDefDto;
import com.sun.mdm.multidomain.relationship.ops.exceptions.*;

public interface RelationshipDefDao {

    /**
     * Inserts a new row in the relationship_def table.
     */
    public long insert(RelationshipDefDto dto) throws RelationshipDefDaoException;

    /**
     * Updates a single row in the relationship_def table.
     */
    public int update(RelationshipDefDto dto) throws RelationshipDefDaoException;

    /**
     * Deletes a single row in the relationship_def table.
     */
    public void delete(long pk) throws RelationshipDefDaoException;

    /**
     * Sets the value of maxRows
     */
    public void setMaxRows(int maxRows);

    /**
     * Gets the value of maxRows
     */
    public int getMaxRows();
}