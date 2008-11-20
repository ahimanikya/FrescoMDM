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
package com.sun.mdm.multidomain.query;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import com.sun.mdm.index.objects.SystemObject;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;

/**
 * MultiDomainSearchCriteria class. This class is used to specify different 
 * search critera for relationships.
 * You can filter by Relationship field values, each domain system object field values.
 * The net filter is the union of all EUIDs that satisfy the Relationship and domain
 * search criterias.
 * @author SwaranjitDua
 * @author cye
 */
public class MultiDomainSearchCriteria {
    /**
     * Relationship defines search relationship attributes. 
     */
    private Relationship relationship;
    private String EUID;
    /**
     * Each domain has a system object that is used for search criteri
     * in that domain.
     */
    private Map<String, EOSearchCriteria> searchCriterias;   

    /**
     * SearchCritera constructor.
     */
    public MultiDomainSearchCriteria() {
    	searchCriterias = new HashMap<String, EOSearchCriteria>();
    }
    
    /**
     * Set relationtship instance. The non-values are used as filter criteria for 
     * search
     * @param relationship
     */
    public void setRelationship(Relationship relationship) {
    	this.relationship = relationship;
    }

    /**
     * Get relationship instance
     * @return relationship
     */
    public Relationship getRelationship() {
        return relationship;
    }    
    
    /**
     * set EUID. If EUID is set, then domain search criteria or Relatiolnship 
     * values are not used for search.
     * So this takes precedence over other search criteria. Only if EUID is not
     * set, then only domain search criteria and Relationship is considered for search.
     * @param euid
     */
    public void setEUID(String euid) {
        this.EUID = euid;
    }
    
    /**
     * get EUID
     * @return EUID
     */
    public String getEUID() {
        return EUID;
    }
        
    /**
     * Get size of query object.
     * @return size
     */
    public int size() {
        return searchCriterias != null ? searchCriterias.size() : 0;
    }
    
    public Set<String> getDomains() {
        return searchCriterias.keySet();
    }
    
    /**
     * Get system object for the given domain.
     * @param domain
     * @return system object
     */
    public EOSearchCriteria getDomainSearchCriteria(String domain) {
        return searchCriterias.get(domain);
    }
    
  
    
    /**
     * Set system object.
     * @param domain
     * @param systemObject
     */
    public void setDomainSearchCriteria(String domain, EOSearchCriteria searchCriteria) {
        searchCriterias.put(domain,searchCriteria);
    }   
    
}
