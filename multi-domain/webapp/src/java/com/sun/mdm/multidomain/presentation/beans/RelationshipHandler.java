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
import com.sun.mdm.multidomain.services.model.RelationshipSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;

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
    public List<RelationshipView> searchRelationships(DomainSearch sourceDomainSearch, 
                                                      DomainSearch targetDomainSearch, 
                                                      RelationshipSearch relationshipSearch)
        throws ServiceException { 
        return relationshipManager.searchRelationships(sourceDomainSearch, 
                                                       targetDomainSearch, 
                                                       relationshipSearch);
        
    }
    public RelationshipComposite getRelationship(RelationshipView relationship)
        throws ServiceException {   
        return relationshipManager.getRelationship(relationship);
    }      
    public List<ObjectView> searchEnterprises(DomainSearch domainSearch)
        throws ServiceException {
         return relationshipManager.searchEnterprises(domainSearch);    
    } 
    public ObjectRecord getEnterprise(ObjectView object)
        throws ServiceException {
        return relationshipManager.getEnterprise(object);
    }  
    public String addRelationship(RelationshipRecord relationship)
        throws ServiceException {     
        return  relationshipManager.addRelationship(relationship);
    }
    public void deleteRelationship(RelationshipView relationship)
        throws ServiceException {
        relationshipManager.deleteRelationship(relationship);
    }
    public void updateRelationship(RelationshipRecord relationship)
        throws ServiceException {
        relationshipManager.updateRelationship(relationship);
    }
}
