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
   
import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;
import com.sun.mdm.multidomain.services.model.AttributeDefExt;

        
/**
 * DomainRelationshipDefinitionObject class.
 * @author cye
 */
public class DomainRelationshipDefsObject implements Iterator<RelationshipDefExt> {

    private List<RelationshipDefExt> relationshipDefs;
    private int position = 0;
    private int size;
    private String domain;

    public DomainRelationshipDefsObject() {
    }
    
    public DomainRelationshipDefsObject(String domain) {
        this.domain = domain;        
    }
    
    public DomainRelationshipDefsObject(String domain, List<RelationshipDefExt> relationshipDefs) {
        this.domain = domain;
        this.relationshipDefs = relationshipDefs;
    }
    
    public String getDomain(){
        return domain;
    }
    
    public void setDomain(String domain){
        this.domain = domain;
    }
    
    public List<RelationshipDefExt> getRelationshipDefss(){
        if (relationshipDefs == null) {
            relationshipDefs = new ArrayList<RelationshipDefExt>();
        }
        return relationshipDefs;
    }
    
    public void setRelationshipDefs(List<RelationshipDefExt> relationshipDefs){
        this.relationshipDefs = relationshipDefs;
    }
    
    public void add(RelationshipDefExt relationship) {
        if (relationshipDefs == null) {
            relationshipDefs = new ArrayList<RelationshipDefExt>();
        }        
        relationshipDefs.add(relationship);
    }
    
    public int getSize() {
        size = 0;
        if (relationshipDefs != null) {
            size  = relationshipDefs.size();
        }
        return size;
    }
    
    public boolean hasNext() {
        boolean has = false;
        if(relationshipDefs != null && 
           !relationshipDefs.isEmpty() && 
           relationshipDefs.size() > position) { 
           has = true;
        }  
        return has;
    }
    
    public RelationshipDefExt next() 
        throws NoSuchElementException {
        if(relationshipDefs != null && 
           !relationshipDefs.isEmpty() && 
           relationshipDefs.size() > position) { 
           RelationshipDefExt e = relationshipDefs.get(position);
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
           object instanceof DomainRelationshipDefsObject) {            
           if (this.domain != null &&
               this.domain.equals(((DomainRelationshipDefsObject)object).getDomain())) {
               return true;
           } else {
               return false;
           }
        } else {
            return false;
        }
    }    
}
