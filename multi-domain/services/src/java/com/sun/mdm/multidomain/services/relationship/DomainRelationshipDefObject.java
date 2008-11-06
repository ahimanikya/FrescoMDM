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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
        
import com.sun.mdm.multidomain.relationship.RelationshipDef;

/**
 * DomainRelationshipDefinitionObject class.
 * @author cye
 */
public class DomainRelationshipDefObject implements Iterator<RelationshipDef> {

    private List<RelationshipDef> relationshipDefinitions;
    private int position = 0;
    private int size;
    private String domain;

    public DomainRelationshipDefObject() {
    }
    
    public DomainRelationshipDefObject(String domain) {
        this.domain = domain;        
    }
    
    public DomainRelationshipDefObject(String domain, List<RelationshipDef> relationshipDefinitions) {
        this.domain = domain;
        this.relationshipDefinitions = relationshipDefinitions;
    }
    
    public String getDomain(){
        return domain;
    }
    
    public void setDomain(String domain){
        this.domain = domain;
    }
    
    public List<RelationshipDef> getRelationshipDefinitions(){
        if (relationshipDefinitions == null) {
            relationshipDefinitions = new ArrayList<RelationshipDef>();
        }
        return relationshipDefinitions;
    }
    
    public void setRelationshipDefinitions(List<RelationshipDef> relationshipDefinitions){
        this.relationshipDefinitions = relationshipDefinitions;
    }
    
    public void add(RelationshipDef relationship) {
        if (relationshipDefinitions == null) {
            relationshipDefinitions = new ArrayList<RelationshipDef>();
        }        
        relationshipDefinitions.add(relationship);
    }
    
    public int getSize() {
        size = 0;
        if (relationshipDefinitions != null) {
            size  = relationshipDefinitions.size();
        }
        return size;
    }
    
    public boolean hasNext() {
        boolean has = false;
        if(relationshipDefinitions != null && 
           !relationshipDefinitions.isEmpty() && 
           relationshipDefinitions.size() > position) { 
           has = true;
        }  
        return has;
    }
    
    public RelationshipDef next() 
        throws NoSuchElementException {
        if(relationshipDefinitions != null && 
           !relationshipDefinitions.isEmpty() && 
           relationshipDefinitions.size() > position) { 
           RelationshipDef e = relationshipDefinitions.get(position);
           position++;
           return e;
        } else {
            throw new NoSuchElementException();
        }        
    }
    
    public void remove() {
        throw new UnsupportedOperationException("not supported optional operation.");    
    }   
    
    @Override 
    public boolean equals(Object object) {
        if(object != null &&
           object instanceof DomainRelationshipDefObject) {            
           if (this.domain != null &&
               this.domain.equals(((DomainRelationshipDefObject)object).getDomain())) {
               return true;
           } else {
               return false;
           }
        } else {
            return false;
        }
    }    
}
