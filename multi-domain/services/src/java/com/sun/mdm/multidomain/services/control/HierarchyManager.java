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

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.hierarchy.HierarchyObject;

import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * HierarchyManager class.
 * @author cye
 */
public class HierarchyManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.HierarchyManager");
    private static Localizer localizer = Localizer.getInstance();

    private MultiDomainService multiDomainService;
    private MultiDomainMetaService multiDomainMetaService;
	
    /**
     * HierarchyManager class.
     */
    public HierarchyManager() {
    }
    
    /**
     * Create a instance of HierarchyManager with the given MultiDomainMetaService and MultiDomainService. 
     * @param multiDomainMetaService MultiDomainMetaService.
     * @param multiDomainService MultiDomainService.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyManager (MultiDomainMetaService multiDomainMetaService, MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    	this.multiDomainMetaService = multiDomainMetaService;        
    }
    
    /**
     * Get a list of hierarchy types for the given domain name.
     * @param domain Domain name.
     * @return List<RelationshipType> Lst of hierarchy types.
     * @throws ServiceException Thrown if an error occurs during processing.
     */    
    public List<RelationshipType> getHierarchyTypes(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                
    }
    
    /**
     * Add a new HierarchyType.
     * @param hierarchyType RelationshipType.
     * @return String RelationshipType identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Update an existing HierarchyType.
     * @param hierarchyType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Delete an existing HierarchyType.
     * @param hierarchyType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteType(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }   
    
    /**
     * Get a total count of hierarchy relationship types for the given domain.
     * @param domain Domain name.
     * @return In Count of hierarchy relationship type
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getTypeCount(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");             
    }
    
    /**
     * Get a list of hierarchy types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipType> List of relationship type
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipType> getTypes(String domain) throws ServiceException {
    	List<RelationshipType> hierarchyTypes = null;
    	return hierarchyTypes;
    }
    
    /**
     * Get a total count of hierarchy relationship instances for the given relationship type.
     * @param hierarchyType RelationshipType.
     * @return int Count of hierarchy relationship instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipCount(RelationshipType hierarchyType) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");  
    }
    
    /**
     * Get a list of hierarchy relationship instances for the given relationship type.
     * @param hierarchyType RelationshipType.
     * @return List<Relationship> List of hierarchy relationship instances
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<Relationship> getRelationships(RelationshipType hierarchyType) throws ServiceException {
    	List<Relationship> hierarchys = null;
    	return hierarchys;
    }
       
    /**
     * Add a new hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchy Relationship.
     * @return String Hierarchy identifier of a newly added hierarchy instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchy(String domain, String parentEUID, String childEUID, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Add a new hierarchy instance between patent entity and children entities for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs.
     * @param hierarchy Relationship.
     * @return String Hierarchy identifier of a newly added hierarchy instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchy(String domain, String parentEUID, List<String> childEUIDs, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }    
    
    /**
     * Update an existing hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchy Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchy(String domain, String parentEUID, String childEUID, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update an existing hierarchy instance between patent entity and children entities for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs.
     * @param hierarchy Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchy(String domain, String parentEUID, List<String> childEUIDs, Relationship hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
    
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain Domain name. 
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchyName HierarchyName.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchy(String domain, String parentEUID, String childEUID, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }     
 
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs. 
     * @param hierarchyName HierarchyName.
     * @throws ServiceException Thrown if an error occurs during processing.
     */    
    public void deleteHierarchy(String domain, String parentEUID, List<String> childEUIDs, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
}
