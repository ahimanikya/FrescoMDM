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

import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.core.ServiceException;     
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefsObject;

/**
 * RelationshipDefHandler class.
 * @author cye
 */
public class RelationshipDefHandler {

    private RelationshipManager relationshipManager;
    
    /**
     * Create an instance of RelationshipDefHandler.
     */
    public RelationshipDefHandler() 
        throws ServiceException { 
        relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
    }
    
    /**
     * Get all RelationshipDefs for the given source domain and target domain name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipDefExt> List of RelationshipDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<RelationshipDefExt> getRelationshipDefs(String sourceDomain, String targetDomain) 
        throws ServiceException {                
        List<RelationshipDefExt> types = null;
        try {
            types = relationshipManager.getRelationshipDefs(sourceDomain, targetDomain);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        if(types == null) {
            types = new ArrayList<RelationshipDefExt>();
        }
        return types;
    }

    /**
     * Get Relationship definition for the given name and source domains and target domain.
     * @param name RelationshipDef name.
     * @param sourceDomain Source domain.
     * @param targetDomain Target domain.
     * @return RelationshipDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public RelationshipDefExt getRelationshipDefByName(String name, String sourceDomain, String targetDomain) 
        throws ServiceException {                
        RelationshipDefExt type = null;
        try {
            type = relationshipManager.getRelationshipDefByName(name, sourceDomain, targetDomain);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return type;
    }

    /**
     * Get Relationship definition for the given relationship Id.
     * @param relationshipDefId RelationshipDef Identifier.
     * @return RelationshipDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public RelationshipDefExt getRelationshipDefById(long relationshipDefId) 
        throws ServiceException {                
        RelationshipDefExt type = null;
        try {
            type = relationshipManager.getRelationshipDefById(relationshipDefId);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return type;
    }
        
    public List<DomainRelationshipDefsObject> getDomainRelationshipDefsObjects(String domain)             
        throws ServiceException {
        List<DomainRelationshipDefsObject> relationshipDefs = null;
        try {
            relationshipDefs = relationshipManager.getDomainRelationshipDefsObjects(domain);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return relationshipDefs;
    }
    
        /**
     * Get all RelationshipDefs for the given domain name.
     * @param domain Domain name.
     * @return List<RelationshipDefExt> List of RelationshipDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public List<RelationshipDefExt> getDefs(String domain) 
        throws ServiceException {                
        List<RelationshipDefExt> types = null;
        try {
            types = relationshipManager.getRelationshipDefs(domain);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        if(types == null) {
            types = new ArrayList<RelationshipDefExt>();
        }
        return types;
    }
    
    /**
     * Create a new relationship type.
     * @param rDefExt RelationshipDefExt.
     * @return String Relationship Identifier which is newly added.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public String addRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {        
        String RelationshipDefId = null;
        try {
            RelationshipDefId = relationshipManager.addRelationshipDef(rDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return RelationshipDefId;        
    }
    
    /**
     * Update an existing relationship type.
     * @param rDefExt RelationshipDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public void updateRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {        
        try {
            relationshipManager.updateRelationshipDef(rDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }    
    }
    
    /**
     * Delete an existing RelationshipDef.
     * @param rDefExt RelationshipDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public void deleteRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {        
        try {
            relationshipManager.deleteRelationshipDef(rDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }        
    }
    
    /**
     * Get total number of RelationshipDef for the given domain.
     * @param domain Domain name.
     * @return int Number of total RelationshipDefs.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipDefCount(String domain)
        throws ServiceException { 
        int count = 0;
        try {
            count = relationshipManager.getRelationshipDefCount(domain);
        } catch(ServiceException svcex) {
            throw svcex;
        }   
        return count;
    }
}
