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

import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.ObjectNode;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * GroupManager class.
 * @author cye
 */
public class GroupManager implements ServiceManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.GroupManager");
    private static Localizer localizer = Localizer.getInstance();
   
    private MultiDomainService multiDomainService;
    private MultiDomainMetaService multiDomainMetaService;
	
    /**
     * Create an instance of GroupManager.
     */
    public GroupManager() {
    }
    
    /**
     * Create a instance of GroupManager with the given MultiDomainMetaService and MultiDomainService.
     * @param multiDomainMetaService MultiDomainMetaService. 
     * @param multiDomainService MultiDomainService.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public GroupManager(MultiDomainMetaService multiDomainMetaService, MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    	this.multiDomainMetaService = multiDomainMetaService;        
    }
    
    /**
     * Get a list of group types for the given domain name.
     * @param domain Domain name.
     * @return a list of group types.
     * @throws ServiceException Thrown if an error occurs during processing.
     */    
    public List<RelationshipType> getGroupTypes(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                
    }
    
    /**
     * Add a new group type.
     * @param groupType RelationshipType.
     * @return String RelationshipType identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addType(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Update an existing group type.
     * @param groupType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateType(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Delete a group type.
     * @param groupType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteType(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }   
    
    /**
     * Get a total count of group relationship types for the given domain.
     * @param domain Domain name.
     * @return int Count of group relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getTypeCount(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Get a list of group relationship types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipType> List of group relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipType> getTypes(String domain) throws ServiceException {
    	List<RelationshipType> groups = null;
    	return groups;
    }
    
    /**
     * Get a total count of group instances for the given group type.
     * @param groupType RelationshipType.
     * @return int Count of group instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipCount(RelationshipType groupType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Get a list of group instances for the given group type.
     * @param groupType RelationshipType.
     * @return List<Relationship> List of group instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<Relationship> getRelationships(RelationshipType groupType) throws ServiceException {
    	List<Relationship> groups = null;
    	return groups;
    }
    
    /**
     * Add a new group for the given domain.
     * @param domain Domain name.
     * @param group Relationship.
     * @return String Group identifier of newly added group instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addGroup(String domain, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
  
    /**
     * Add a group instance for the given domain.
     * @param domain Domain name.
     * @param entityEUIDs List of entities EUIDs.
     * @param group Relationship.
     * @return String Group identifier of newly added group instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addGroup(String domain,List<String> entityEUIDs, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }    
    
    /**
     * Create group members.
     * @param groupId Group Identifier.
     * @param EUIDs An array of EUIDs.
     * @param relationship Group relationship.
     * @return String[] A list of relationshipIds that are assocated between the given groupId and EUIDs.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String[] createGroupMembers(String groupId, String EUIDs[], Relationship relationship)
        throws ServiceException {
         throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update a group instance for the given domain.
     * @param domain Domain name.
     * @param group Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateGroup(String domain, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update a group instance for the given domain.
     * @param domain Domain name.
     * @param entityEUID Entity EUID.
     * @param group Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateGroup(String domain, String entityEUID, Relationship group) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
   
    /**
     * Update a group relationship for the given EUID.
     * @param domain Domain name.
     * @param EUID EUID.
     * @param relationship Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateGroupMember(String domain, String EUID, Relationship relationship) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
    
    /**
     *  Get group members that are have relationship with the given groupId.
     * @param domain Domain name.
     * @param groupId Group Identifier.
     * @param fields A list of fields to retrieve for all the members EUIDs.
     * @return A list of object nodes that have relationship with the given groupId.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public ObjectNode[] getGroupMembers(String domain, String groupId, EPathArrayList fields) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");        
    }
    
    /**
     * Delete a group instance for the given domain and group name.
     * @param domain Domain name.
     * @param groupName Group name.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteGroup(String domain, String groupName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }     
    
    /**
     * Delete a group instance for the given domain and group name.
     * @param domain Domain name.
     * @param entityEUID Entity EUID.
     * @param groupName Group name.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteGroup(String domain, String entityEUID, String groupName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }  
    
    /**
     * Delete a EUID from the given group.
     * @param domain Domain name.
     * @param groupId Group Identifier.
     * @param EUID EUID.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteGroupMember(String domain, String groupId, String EUID)
        throws ServiceException {
        throw new ServiceException("Not Implemented Yet");        
    }
        
    /**
     * Search group instances for the given relationship.
     * @param relationship Relationship.
     * @return List<Relationship> List of group instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<Relationship> searchGroups(Relationship relationship) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
}
