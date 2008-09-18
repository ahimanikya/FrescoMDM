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
package com.sun.mdm.multidomain.services.query;

import java.util.Map;
import java.util.HashMap;

import com.sun.mdm.index.objects.SystemObject;

import com.sun.mdm.multidomain.relationship.Relationship;

/**
 * QueryObject class.
 * @author cye
 */
public class SearchCriteria {
    private Relationship relationship;	
    private Map<String, SystemObject> systemObjects;
     
    /**
     * QueryObject constructor.
     */
    public SearchCriteria() {
    	systemObjects = new HashMap<String, SystemObject>();
    }
    
    /**
     * Set relationtship instance.
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
}
