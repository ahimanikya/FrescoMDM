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
public class DomainRelationshipsObject {
    private String domain;
    private ObjectView primaryObject;
    private List<RelationshipsObject> relationshipsObjects;    
    
    public DomainRelationshipsObject(){        
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
    
    public List<RelationshipsObject> getRelationshipsObjects(){        
        return relationshipsObjects;
    }
    
    public void setRelationshipsObjects(List<RelationshipsObject> relationshipsObjects){        
        this.relationshipsObjects = relationshipsObjects;
    }

    public void setRelationshipsObject(RelationshipsObject relationshipsObject){        
        if (relationshipsObjects == null) {
            relationshipsObjects = new ArrayList<RelationshipsObject>();
        }
        relationshipsObjects.add(relationshipsObject);
    }
    
    public void addRelationshipView(RelationshipDefView relationshipDef, RelationshipView relationship) {
       RelationshipsObject relationshipsObject = null;
       for (RelationshipsObject rObject : relationshipsObjects) {
            if (relationshipDef.equals(rObject.getRelationshipDefView())) {
                relationshipsObject = rObject;
                break;
            }
       }
       if (relationshipsObject == null) {
           relationshipsObject = new RelationshipsObject(relationshipDef);
       }
       relationshipsObject.setRelationshipView(relationship);
    }
    
    public List<RelationshipView> getRelationshipView(RelationshipDefView relationshipDef) {
        RelationshipsObject relationshipsObject = null;
        for (RelationshipsObject rObject : relationshipsObjects) {
            if (relationshipDef.equals(rObject.getRelationshipDefView())) {
                relationshipsObject = rObject;
                break;
            }
        }   
        return relationshipsObject != null ? relationshipsObject.getRelationshipViews() : null;
    }
    
}
