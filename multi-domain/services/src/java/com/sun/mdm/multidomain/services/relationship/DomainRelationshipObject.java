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
package com.sun.mdm.multidomain.services.relationship;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.multidomain.services.model.ObjectView;

/**
 * DomainRelationshipObject class.
 * @author cye
 */
public class DomainRelationshipObject {
    private String domain;
    private ObjectView primaryObject;
    private Map<RelationshipDefinitionView, List<RelationshipView>> relationshipViews;    
    
    public DomainRelationshipObject(){        
    }
    public String getDomain(){        
        return domain;
    }
    public void setDomain(String domain){
        this.domain = domain;
    }
    public ObjectView getPrimaryObject() {
        return primaryObject;
    }
    public void setPrimaryObject(ObjectView primaryObject){        
        this.primaryObject = primaryObject;
    }
    public Map<RelationshipDefinitionView, List<RelationshipView>> getRelationshipViews(){        
        return relationshipViews;
    }
    public void setRelationshipViews(Map<RelationshipDefinitionView, List<RelationshipView>> relationshipViews){        
        this.relationshipViews = relationshipViews;
    }
    public void addRelationshipView(RelationshipDefinitionView relationshipDef, RelationshipView relationship) {
        if (relationshipViews == null) {
            relationshipViews = new HashMap<RelationshipDefinitionView, List<RelationshipView>>();            
        }
        List<RelationshipView> value = relationshipViews.get(relationshipDef);
        if(value == null) {
            value = new ArrayList<RelationshipView>();
            relationshipViews.put(relationshipDef, value);
            value = relationshipViews.get(relationshipDef);
        }
        value.add(relationship);
    }
    public List<RelationshipView> getRelationshipView(RelationshipDefinitionView relationshipDef) {
        List<RelationshipView> relationshipView = null;
         if (relationshipViews != null) {
            relationshipView = relationshipViews.get(relationshipDef);
         } else {
            relationshipView = new ArrayList<RelationshipView>();
         }
        return relationshipView;
    }
    
}
