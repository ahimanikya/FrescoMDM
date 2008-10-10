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
package com.sun.mdm.multidomain.services.control;

import java.util.List;

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.services.core.ServiceException;

/**
 * Generic ServiceManager interface.
 * @author cye
 */
public interface ServiceManager {
 
	
    /**
     * Add a new relationship type.
     * @param relationshipType RelationshipType.
     * @return String RelationshipType identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addType(RelationshipType relationshipType) throws ServiceException;
  
    /**
     * Update an existing relationship type.
     * @param relationshipType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateType(RelationshipType relationshipType) throws ServiceException;
   
    /**
     * Delete a relationship type.
     * @param relationshipType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteType(RelationshipType relationshipType) throws ServiceException;

    /**
     * Get a total count of relationship types for the given domain.
     * @param domain Domain name.
     * @return int Count of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getTypeCount(String domain) throws ServiceException;
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipType> List of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipType> getTypes(String domain) throws ServiceException;
    
    /**
     * Get a total count of relationship instances for the given relationship type.
     * @param relationshipType RelationshipType.
     * @return int Count of relationship instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipCount(RelationshipType relationshipType) throws ServiceException;
    
    /**
     * Get a list of relationship instances for the given relationship type.
     * @param relationshipType RelationshipType.
     * @return List<Relationship> List of relationship instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<Relationship> getRelationships(RelationshipType relationshipType) throws ServiceException;
        
}
