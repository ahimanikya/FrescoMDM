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
package com.sun.mdm.multidomain.relationship;

import com.sun.mdm.index.objects.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
        
/**
 * MultiObject class.
 * Contains top level source object for primary domain, and the top level 
 * objects from other domains to which source object has a relationship.
 * @author SwaranjitDua
 */
public class MultiObject implements Serializable {
    private ObjectNode sourceDomainObject;	
    private List<RelationshipDomain> relationshipDomains = new ArrayList<RelationshipDomain>();
	
    
    /**
     * Create an instance of MultiObject.
     */
    public MultiObject(){
    }
    
     /**
     * Create an instance of MultiObject.
     */
    public MultiObject(ObjectNode sourceDomainObject){
        this.sourceDomainObject = sourceDomainObject; 
    }
         
    /**
     * get Primary Domain Object
     * @return primaryDomainObject
     */
    public ObjectNode getSourceDomainObject() {
    	return sourceDomainObject;
    }
    
    /**
     * sets Primary Domain object
     * @param object primary domain object
     */
    public void setSourceDomainObject(ObjectNode object) {
    	this.sourceDomainObject = object;
    }

    /**
     * Get an array of RelationshipDomain objects.
     * @return RelationshipDomain[] An array of RelationshipDomain objects.
     */
    public List<RelationshipDomain> getRelationshipDomains() {        
        return relationshipDomains;
    }
    
    /**
     * Set an array of RelationshipDomain objects.
     * @param relationshipDomains An array of RelationshipDomain objects.
     */
    public void setRelationshipDomains(List<RelationshipDomain> relationshipDomains) {        
        this.relationshipDomains = relationshipDomains;
    }
    
    public void addRelationshipDomain(RelationshipDomain reldomain) {
        relationshipDomains.add(reldomain);
    }
    
    /**
     * Get RelationshipDomain object.
     * @param domain
     * @return RelationshipDomain.
     */
    public RelationshipDomain getRelationshipDomain(String domain) {
        RelationshipDomain relationshipDomain = null;
        for (RelationshipDomain relDomain : relationshipDomains) {
            if(domain.equals(relDomain.getDomain())) {
                 relationshipDomain = relDomain;
            }
        }
        return  relationshipDomain;
    }
       
    /*  
    public int getRelationshipTypeCount() {	
    }
    
    public int getRelationshipCount(RelationshipType type) {	
    }
    */
    
    /**
     * RelationshipDomain class. 
     * This represents a relationship between source domain in MultiObject to target domain
     * in this class. 
     * This is encapsulated within MultiObject.
     */
    public static class RelationshipDomain implements Serializable {
        private String domain;
        private List<RelationshipObject> relationshipObjects = new ArrayList<RelationshipObject>();
        
        /**
         * Public constructor
         */
        public RelationshipDomain(String domain) {
            this.domain = domain;
        }
        
        /**
         * get Domain
         * @return
         */
        public String getDomain() {
        	return domain;
        }
        
        /**
         *  set Domain
         * @param domain
         */
        
        public void setDomain(String domain) {
        	this.domain = domain;
        }
        
        public void addRelationshipObejct(RelationshipObject relobject) {
            this.relationshipObjects.add(relobject);
        }
          
        /**
         * Set an array of RelationshipObject.
         * @param relationshipObjects Array of RelationshipObject.
         */
        public void setRelationshipObjects(List<RelationshipObject> relationshipObjects) {
            this.relationshipObjects = relationshipObjects;
        }
        
        /**
         * Get an array of RelationshipObject.
         * @return RelationshipObject[] Array of RelationshipObject.
         */
        public List<RelationshipObject> getRelationshipObjects() {
            return relationshipObjects;
        }
        
        /*
        public int getRelationshipTypeCount() {
        	
        }
        
        public int getRelationshipCount(RelationshipType type) {
        	
        }
        */
                  
    }
            
    /**
     * RelationshipObject class. 
     * This represents a relationship between source object in MultiObject to targetObject
     * in this class. 
     * This is encapsulated within MultiObject.
     */
    public static class RelationshipObject implements Serializable {
        private ObjectNode targetObject;
        private Relationship relationship;
        
        /**
         * Public constructor
         */
        public RelationshipObject(Relationship relationship, ObjectNode object) {
            this. relationship = relationship;
            this.targetObject = object;
        }
        
        /**
         * Set target object node.
         * @param targetObject
         */
        public void setTargetObject(ObjectNode targetObject) {
            this.targetObject = targetObject;
        }
        /**
         * Get target domain object node.
         * @return target object node 
         */
        public ObjectNode getTargetObject() {
            return targetObject;
        }  
        
        /**
         * Set relationship instance.
         * @param relationship instance
         */
        public void setRelationship(Relationship relationship) {
            this.relationship = relationship;
        }
        
        /**
         * Get relationship instance.
         * @return relationship instance
         */
        public Relationship getRelationship() {
            return relationship;
        }     
    } 
}
