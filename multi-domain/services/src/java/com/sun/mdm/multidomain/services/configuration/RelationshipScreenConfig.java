/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.multidomain.relationship.Relationship;
import java.util.HashMap;

public class RelationshipScreenConfig {
	private String mSourceDomainName;		// name of the target domain
	private String mTargetDomainName;		// name of the target domain
	private HashMap<String,  RelationshipScreenConfigInstance> mRelationshipScreenConfigInstances;
	
	// This class represents all the relationships for a source and target domain.

    public RelationshipScreenConfig() {
        mRelationshipScreenConfigInstances = new HashMap<String,  RelationshipScreenConfigInstance>();
    }
    
    public RelationshipScreenConfig(String sourceDomainName, String targetDomainName) {
        mSourceDomainName = sourceDomainName;
        mTargetDomainName = targetDomainName;
        mRelationshipScreenConfigInstances = new HashMap<String,  RelationshipScreenConfigInstance>();
    }
    
	public Relationship getRelationship() {	// retrieves the Relationship object for this relationship
	    return null;
	}

	public void setSourceDomainName(String sourceDomainName) {	// sets the name of the source domain
	    mSourceDomainName = sourceDomainName;
	}
	public String getSourceDomainName() {	// retrieves the name of the source domain
	    return mSourceDomainName;
	}

	public void setTargetDomainName(String targetDomainName) {	// sets the name of the target domain
	    mTargetDomainName = targetDomainName;
	}
	public String getTargetDomainName() {	// retrieves the name of the target domain
	    return mTargetDomainName;
	}
	
	public HashMap<String,  RelationshipScreenConfigInstance> getRelationships() {
	    return mRelationshipScreenConfigInstances;
	}
	
	// add a relationship screen configuration instance
	
	public void addRelScreenConfigInstance(RelationshipScreenConfigInstance rSCObj) 
	        throws Exception {
	    // RESUME HERE--name should be passed in or retieved from rSCObj?
	    mRelationshipScreenConfigInstances.put(rSCObj.getRelationshipName(), rSCObj);
	}

    // retrieves a relationshiop screen configuration object
	public RelationshipScreenConfigInstance getRelScreenConfigInstance(String relationshipName) 
	        throws Exception {
	            
	    return mRelationshipScreenConfigInstances.get(relationshipName);
	}
	
    // removes a relationship screen configuration instance
	public void removeRelScreenConfigInstance(String relationshipName) 
	        throws Exception {
	            
	    mRelationshipScreenConfigInstances.remove(relationshipName);
	}
}
