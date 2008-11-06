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

import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipObject;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.core.ServiceException;     

/**
 * RelationshipHandler class.
 * @author cye
 */
public class RelationshipHandler {

    private RelationshipManager relationshipManager;
    
    /**
     * Create an instance of RelationshipHandler.
     */
    public RelationshipHandler() 
        throws ServiceException {  
        relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();        
    } 
    
    /**
     * Search relationships for the given domain and relationship search options and criteria.
     * @param sourceDomainSearch Source domain search options and criteria.
     * @param targetDomainSearch Target domain search options and criteria.
     * @param relationshipSearch Relationship search options and criteria.
     * @return List<RelationshipView> A list of relationship view which summarizes relationhip.
     * @throws ServiceException Thrown if an error occurs during processing. 
     */
    public List<RelationshipView> searchRelationships(DomainSearch sourceDomainSearch, 
                                                      DomainSearch targetDomainSearch, 
                                                      RelationshipSearch relationshipSearch)
        throws ServiceException { 
        return relationshipManager.searchRelationships(sourceDomainSearch, 
                                                       targetDomainSearch, 
                                                       relationshipSearch);        
    }
    
    /**
     * Search all relationships  for the given domain search options and criteria.
     * @param domainSearch Domain search options and criteria.
     * @return DomainRelationshipObject A list of all relationship views for the given domain record.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public DomainRelationshipObject searchDomainRelationshipObjects(DomainSearch domainSearch)
        throws ServiceException {
        return relationshipManager.searchDomainRelationshipObjects(domainSearch);        
    }

    /**
     * Get the detail of a relationship object for the given summarized relationship view.
     * @param relationship RelationshipView
     * @return RelationshipComposite Detail of relationship object.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public RelationshipComposite getRelationship(RelationshipView relationship)
        throws ServiceException {   
        return relationshipManager.getRelationship(relationship);
    }
    
    /**
     * Search master index enterprise objects for the given domain search options and criteria.
     * @param domainSearch Domain search options and criteria.
     * @return List<ObjectView> A list of object view which is a summary of object node.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<ObjectView> searchEnterprises(DomainSearch domainSearch)
        throws ServiceException {
         return relationshipManager.searchEnterprises(domainSearch);    
    }
    
    /**
     * Get the detail of a object node for the given summarized object view.
     * @param object Object view.
     * @return ObjectRecord Detailed object node.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public ObjectRecord getEnterprise(ObjectView object)
        throws ServiceException {
        return relationshipManager.getEnterprise(object);
    }
    
    /**
     * Add a new relationship for the given relationship information.
     * @param relationship The relationship information.
     * @return String Relationship Id if a new relationship is created successfully.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationship(RelationshipRecord relationship)
        throws ServiceException {     
        return  relationshipManager.addRelationship(relationship);
    }
    
    /**
     * Delete an existing relationship the given relationship information.
     * @param relationship The relationship information.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationship(RelationshipView relationship)
        throws ServiceException {
        relationshipManager.deleteRelationship(relationship);
    }
    
    /**
     * Update an existing relationship the given relationship information.
     * @param relationship The relationship information.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationship(RelationshipRecord relationship)
        throws ServiceException {
        relationshipManager.updateRelationship(relationship);
    }
}
