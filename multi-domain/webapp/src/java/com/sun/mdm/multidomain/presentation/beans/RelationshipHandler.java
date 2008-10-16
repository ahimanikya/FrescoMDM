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

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.MultiObject.RelationshipObject;

import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
import com.sun.mdm.multidomain.services.control.ServiceManagerFactory;
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
     * Get a list of relationships for the given search options and criteria.
     * @param searchOptions SearchOptions.
     * @param searchCriteria SearchCriteria.
     * @return List<RelationshipObject> List of RelationshipObject.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipObject> searchRelationships(SearchOptions searchOptions, SearchCriteria searchCriteria)
        throws ServiceException { 
        List<RelationshipObject> relationships = null;
        try {
            relationships = relationshipManager.searchRelationships(searchOptions, searchCriteria);
        } catch(ServiceException sex) {
            throw sex;
        }
        return relationships;
    }
 
    public int getRelationshipCount(String domain, ObjectNode entity)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
   
    public int getRelationshipCount(RelationshipType relationshipType)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
       
    public RelationshipObject searchRelationship(Relationship relationship)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
  
    public List<ObjectNode> searchEnterprises(String domain, EOSearchOptions searchOptions, EOSearchCriteria searchCriteria)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");    
    }
     
    public String addRelationship(Relationship relationship)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
    
    public void deleteRelationship(Relationship relationship)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
   
    public void updateRelationship(Relationship relationship)
        throws ServiceException {
        throw new  ServiceException ("Not Implemented Yet!");
    }
    
}
