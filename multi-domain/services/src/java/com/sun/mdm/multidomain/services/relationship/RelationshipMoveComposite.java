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

import java.util.List;

/**
 * RelationshipMoveComposite.
 * @author cye
 */
public class RelationshipMoveComposite {
    private RelationshipDefView fromRelationshipDef;
    private RelationshipDefView toRelationshipDef;
    private String domain;
    private String fromEUID;
    private String toEUID;
    private List<RelationshipView> relationships;

    public RelationshipMoveComposite() {        
    }
    
    public RelationshipDefView getFromRelationshipDef(){
        return fromRelationshipDef;
    }

    public void setFromRelationshipDef(RelationshipDefView fromRelationshipDef){
        this.fromRelationshipDef = fromRelationshipDef;
    }
    
    public RelationshipDefView getToRelationshipDef(){
        return toRelationshipDef;
    }
    
    public void seToRelationshipDef(RelationshipDefView toRelationshipDef){
        this.toRelationshipDef = toRelationshipDef;
    }
    
    public List<RelationshipView> getRelationships(){
        return relationships;
    }
    
    public void seRelationships(List<RelationshipView> relationships){
        this.relationships = relationships;
    }
    
    public String getDomain() {
        return domain;
    }
            
    public void setDomain(){
        this.domain = domain;
    }

    public String getFromEUID() {
        return fromEUID;
    }
            
    public void setFromEUID(){
        this.fromEUID = fromEUID;
    }

    public String getToEUID() {
        return toEUID;
    }
            
    public void setToEUID(){
        this.toEUID = toEUID;
    }
    
}
