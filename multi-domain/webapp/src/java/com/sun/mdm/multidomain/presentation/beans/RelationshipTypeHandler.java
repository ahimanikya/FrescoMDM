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
package com.sun.mdm.multidomain.presentation.beans;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.sun.mdm.multidomain.relationship.RelationshipDef;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.core.ServiceException;     
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefinitionObject;

/**
 * RelationshipTypeHandler class.
 * @author cye
 */
public class RelationshipTypeHandler {

    private RelationshipManager relationshipManager;
    
    /**
     * Create an instance of RelationshipTypeHandler.
     */
    public RelationshipTypeHandler() 
        throws ServiceException { 
        relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
    }
    
    /**
     * Get all relationshiptypes for the given source domain and target domain name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipType> List of RelationshipType.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<RelationshipDef> getRelationshipTypes(String sourceDomain, String targetDomain) 
        throws ServiceException {                
        List<RelationshipDef> types = null;
        try {
            types = relationshipManager.getRelationshipDefs(sourceDomain, targetDomain);
        } catch(ServiceException sex) {
            throw sex;
        }
        if(types == null) {
            types = new ArrayList<RelationshipDef>();
        }
        return types;
    }

    public List<DomainRelationshipDefinitionObject> getDomainRelationshipDefinitionObjects(String domain)             
        throws ServiceException {
        List<DomainRelationshipDefinitionObject> relationshipDefs = null;
        try {
            relationshipDefs = relationshipManager.getDomainRelationshipDefinitionObjects(domain);
        } catch(ServiceException sex) {
            throw sex;
        }
        return relationshipDefs;
    }
    
        /**
     * Get all relationshiptypes for the given domain name.
     * @param domain Domain name.
     * @return List<RelationshipType> List of RelationshipType.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<RelationshipDef> getTypes(String domain) 
        throws ServiceException {                
        List<RelationshipDef> types = null;
        try {
            types = relationshipManager.getTypes(domain);
        } catch(ServiceException sex) {
            throw sex;
        }
        if(types == null) {
            types = new ArrayList<RelationshipDef>();
        }
        return types;
    }
    
    /**
     * Create a new relationship type.
     * @param relationshipType RelationshipType.
     * @return String Relationship Identifier which is newly added.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public String addRelationshipType(RelationshipDef relationshipDef) 
        throws ServiceException {        
        String relationshipTypeId = null;
        try {
            relationshipTypeId = relationshipManager.addType(relationshipDef);
        } catch(ServiceException sex) {
            throw sex;
        }
        return relationshipTypeId;        
    }
    
    /**
     * Delete an existing RelationshipType.
     * @param relationshipType RelationshipType.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public void deleteRelationshipType(RelationshipDef relationshipDef) 
        throws ServiceException {        
        try {
            relationshipManager.deleteType(relationshipDef);
        } catch(ServiceException sex) {
            throw sex;
        }        
    }
    
    /**
     * Get total number of RelationshipType for the given domain.
     * @param domain Domain name.
     * @return int Number of total relationshiptypes.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipTypeCount(String domain)
        throws ServiceException { 
        int count = 0;
        try {
            count = relationshipManager.getTypeCount(domain);
        } catch(ServiceException sex) {
            throw sex;
        }   
        return count;
    }
}
