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
import com.sun.mdm.multidomain.services.core.ServiceLocator;

import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
import com.sun.mdm.multidomain.services.query.RelationshipObject;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * GroupManager class.
 * @author cye
 */
public class GroupManager implements ServiceManager {
	private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.GroupManager");
	private static Localizer localizer = Localizer.getInstance();
   
	private MultiDomainService multiDomainService;
	private ServiceLocator serviceLocator;
	
    /**
     * Create an instance of GroupManager.
     */
    public GroupManager() {
    }
    
    /**
     * Create a instance of GroupManager with the given MultiDomainService. 
     * @param multiDomainService
     * @throws ServiceException
     */
    public GroupManager(MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    }
    
    /**
     * Get a list of group types for the given domain name.
     * @param domain
     * @return a list of group types.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */    
    public List<RelationshipType> getGroupTypes(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                
    }
    
    /**
     * Add a new group type.
     * @param groupType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void addType(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Update an existing group type.
     * @param groupType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateType(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Delete a group type.
     * @param relationshipType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void deleteType(RelationshipType relationshipType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }   
    
    /**
     * Get a total count of group relationship types for the given domain.
     * @param domain
     * @return count of group relationship type
     * @throws ServiceException
     */
    public int getRelationshipTypeCount(String domain) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of group relationship types for the given domain.
     * @param domain
     * @return list of group relationship type
     * @throws ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String domain) throws ServiceException {
    	List<RelationshipType> groups = null;
    	return groups;
    }
    
    /**
     * Get a total count of group instances for the given group type.
     * @param relationshipType
     * @return count of group  instances
     * @throws ServiceException
     */
    public int getRelationshipCount(RelationshipType relationshipType) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of group instances for the given group type.
     * @param relationshipType
     * @return list of group instances
     * @throws ServiceException
     */
    public List<Relationship> getRelationships(RelationshipType relationshipType) throws ServiceException {
    	List<Relationship> groups = null;
    	return groups;
    }
    
    /**
     * Add a group for the given domain.
     * @param domain
     * @param group
     * @return an id of newly added group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addGroup(String domain, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
  
    /**
     * Add a group instance for the given domain.
     * @param domain
     * @param entityEUIDs
     * @param group
     * @return an id of newly added group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addGroup(String domain,List<String> entityEUIDs, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }    
    
    /**
     * Update a group instance for the given domain.
     * @param domain
     * @param group
     * @return an id of newly updated group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String updateGroup(String domain, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update a group instance for the given domain.
     * @param domain
     * @param entityEUID
     * @param group
     * @return an id of newly updated group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String updateGroup(String domain, String entityEUID, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
    
    /**
     * Delete a group instance.
     * @param domain
     * @param groupName
     * @return an id of newly deleted group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String deleteGroup(String domain, String groupName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }     
    
    /**
     * Delete a group instance.
     * @param domain
     * @param entityEUID
     * @param groupName
     * @return an id of newly deleted group instance
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String deleteGroup(String domain, String entityEUID, String groupName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }  
    
    /**
     * Search group instances for the given domain.
     * @param domain
     * @param queryObject
     * @param queryFilte
     * @return a list of group instances.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipObject> searchGroups(String domain, SearchCriteria queryObject, SearchOptions queryFilte) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
}
