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

import com.sun.mdm.index.objects.SystemObject;

import com.sun.mdm.multidomain.relationship.Relationship;

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
    private Map<String, SystemObject> systemObjects;   

    /**
     * SearchCritera constructor.
     */
    public MultiDomainSearchCriteria() {
    	systemObjects = new HashMap<String, SystemObject>();
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
        return systemObjects != null ? systemObjects.size() : 0;
    }
    
    /**
     * Get system object for the given domain.
     * @param domain
     * @return system object
     */
    public SystemObject getSystemObject(String domain) {
        return systemObjects.get(domain);
    }
    
    /**
     * Get all system objects.
     * @return a list of system objects
     */
    public Map<String, SystemObject> getSystemObjects() {
        return systemObjects;
    }
    
    /**
     * Set system object.
     * @param domain
     * @param systemObject
     */
    public void setSystemObject(String domain, SystemObject systemObject) {
        systemObjects.put(domain,systemObject);
    }   
    
    /**
     * RangeSystemObecjt supports range search.
     */
    public static class RangeSystemObject {
        
        SystemObject systemObject;
        SystemObject systemObjectFrom;
        SystemObject systemObjectTo;
        
        public void setSystemObject(SystemObject systemObject) {
            this.systemObject = systemObject;
        }
        public SystemObject getSystemObject() {
            return systemObject;
        }
        public void setSystemObjectFrom(SystemObject systemObjectFrom) {
            this.systemObjectFrom = systemObjectFrom;
        }
        public SystemObject getSystemObjectFrom() {
            return systemObjectFrom;
        } 
        public void setSystemObjectTo(SystemObject systemObjectTo) {
            this.systemObjectTo = systemObjectTo;
        }
        public SystemObject getSystemObjectTo() {
            return systemObjectTo;
        }          
    }
}
