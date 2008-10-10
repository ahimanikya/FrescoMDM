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

import com.sun.mdm.multidomain.relationship.RelationshipType;

import com.sun.mdm.multidomain.services.control.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.core.ServiceException;     

/**
 * RelationshipTypeHandler class.
 * @author cye
 */
public class RelationshipTypeHandler {

    /**
     * Create an instance of RelationshipTypeHandler.
     */
    public RelationshipTypeHandler() {        
    }
    
    /**
     * Get all relationshiptypes for the given source domain and target domain name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipType> List of RelationshipType.
     */
    public List<RelationshipType> getRelationshipTypes(String sourceDomain, String targetDomain) {                
        List<RelationshipType> types = null;
        try {
            RelationshipManager relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
            types = relationshipManager.getRelationshipTypes(sourceDomain, targetDomain);
        } catch(ServiceException sex) {
            types = new ArrayList<RelationshipType>();
        }
        return types;
    }

    /**
     * Create a new relationship type.
     * @param relationshipType RelationshipType.
     * @return String Relationship Identifier which is newly added.
     */
    public String addRelationshipType(RelationshipType relationshipType) {        
        String relationshipTypeId = null;
        try {
            RelationshipManager relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
            relationshipTypeId = relationshipManager.addType(relationshipType);
        } catch(ServiceException sex) {
        }
        return relationshipTypeId;        
    }
    
    /**
     * Delete an existing RelationshipType.
     * @param relationshipType RelationshipType.
     */
    public void deleteRelationshipType(RelationshipType relationshipType) {        
        try {
            RelationshipManager relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
            relationshipManager.deleteType(relationshipType);
        } catch(ServiceException sex) {
        }        
    }
        
}
