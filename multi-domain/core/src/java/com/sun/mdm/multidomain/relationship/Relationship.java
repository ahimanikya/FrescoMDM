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

import com.sun.mdm.multidomain.association.Association;

/**
 * Relationship class. 
 * This represents a particular instance of a relationship.
 * Ex. Relationship between Patient "John Smith" and Doctor "Watkins".
 * The relationship is between by sourceEUID and targetEUID and uniquely represented by
 * relationshipID.
 * @author cye
 * @author SwaranjitDua
 */
public class Relationship extends Association {
    private String relationshipID;
    private String sourceEUID;
    private String targetEUID;
    
    
    /**
     * Create an instance of Relationship.
     */
    public Relationship() {    	
    }
    
    /**
     * Get relationship Id.
     * @return String Relationship Id.
     */
    public String getRelationshipID() {
    	return relationshipID;
    }
    
    /**
     * Set relationship Id.
     * @param relationshipID Relationship Id.
     */
    public void setRelationshipID(String relationshipID){
    	this.relationshipID = relationshipID;
    }
    
    /** 
     * Get Source EUID
     * @return sourceEUID
     */
    public String getSourceEUID() {
    	return sourceEUID;
    }
    
    /**
     *  set sourceEUID
     * @param sourceEUID
     */
    public void setSourceEUID(String sourceEUID) {
    	this.sourceEUID = sourceEUID;
    }
    
    /** 
     * Get Target EUID
     * @return targetEUID
     */
    public String getTargetEUID() {
    	return targetEUID;
    }
    
    /**
     *  set targetEUID
     * @param targetEUID
     */
    public void setTargetEUID(String targetEUID) {
    	this.targetEUID = targetEUID;
    }
}
