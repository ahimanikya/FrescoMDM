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

import net.java.hulp.i18n.Logger;

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.services.core.MultiDomainService;
import com.sun.mdm.multidomain.services.core.ServiceException;

import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
import com.sun.mdm.multidomain.services.query.RelationshipObject;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * HierarchyManager class.
 * @author cye
 */
public class HierarchyManager implements ServiceManager {
	private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.HierarchyManager");
	private static Localizer localizer = Localizer.getInstance();

	private MultiDomainService multiDomainService;
	
    /**
     * HierarchyManager class.
     */
    public HierarchyManager() {
    }
    
    /**
     * Create a instance of HierarchyManager with the given MultiDomainService. 
     * @param multiDomainService
     * @throws ServiceException
     */
    public HierarchyManager (MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    }
    
    /**
     * Get a list of hierarchy types for the given domain name.
     * @param domain
     * @return a list of hierarchy types.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */    
    public List<RelationshipType> getHierarchyTypes(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                
    }
    
    /**
     * Add a new HierarchyType.
     * @param hierarchyType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void addType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Update an existing HierarchyType.
     * @param hierarchyType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Delete a HierarchyType.
     * @param hierarchyType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void deleteType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }   
    
    /**
     * Get a total count of hierarchy relationship types for the given domain.
     * @param domain
     * @return count of hierarchy relationship type
     * @throws ServiceException
     */
    public int getRelationshipTypeCount(String domain) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain
     * @return list of relationship type
     * @throws ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String domain) throws ServiceException {
    	List<RelationshipType> hierarchyTypes = null;
    	return hierarchyTypes;
    }
    
    /**
     * Get a total count of hierarchy relationship instances for the given relationship type.
     * @param relationshipType
     * @return count of hierarchy relationship instances
     * @throws ServiceException
     */
    public int getRelationshipCount(RelationshipType relationshipType) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of hierarchy relationship instances for the given relationship type.
     * @param relationshipType
     * @return list of hierarchy relationship instances
     * @throws ServiceException
     */
    public List<Relationship> getRelationships(RelationshipType relationshipType) throws ServiceException {
    	List<Relationship> hierarchys = null;
    	return hierarchys;
    }
       
    /**
     * Add a new hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUID
     * @param hierarchy
     * @return an id of a newly added hierarchy instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addHierarchy(String domain, String parentEUID, String childEUID, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Add a new hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUIDs
     * @param hierarchy
     * @return an id of a newly added hierarchy instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addHierarchy(String domain, String parentEUID, List<String> childEUIDs, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }    
    
    /**
     * Update an existing hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUID
     * @param hierarchy
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateHierarchy(String domain, String parentEUID, String childEUID, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update an existing hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUID
     * @param hierarchy
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateHierarchy(String domain, String parentEUID, List<String> childEUIDs, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
    
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUID
     * @param hierarchyName
     * @returnan id of a newly deleted hierarchy instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String deleteHierarchy(String domain, String parentEUID, String childEUID, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }     
 
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain
     * @param parentEUID
     * @param childEUID
     * @param hierarchyName
     * @returnan id of a newly deleted hierarchy instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */    
    public String deleteHierarchy(String domain, String parentEUID, List<String> childEUIDs, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Search hierarchy instances for the given domain.
     * @param domain
     * @param queryObject
     * @param queryFilte
     * @return a list of hierarchy instances
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipObject> searchHierarchys(String domain, SearchCriteria queryObject, SearchOptions queryFilte) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
}
