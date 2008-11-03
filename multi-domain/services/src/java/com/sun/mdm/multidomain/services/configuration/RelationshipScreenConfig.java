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

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.HashMap;

public class RelationshipScreenConfig {

    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.RelationshipScreenConfig");
    private static transient final Localizer mLocalizer = Localizer.get();
 
	private String mSourceDomainName;		// name of the source domain
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
	    // RESUME HERE
	    return null;
	}

    // sets the name of the source domain
    
	public void setSourceDomainName(String sourceDomainName) throws Exception {	
	    if (sourceDomainName == null || sourceDomainName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG526: Domain name cannot be null " +
                                             "or an empty string."));
	    }
	    mSourceDomainName = sourceDomainName;
	}
	public String getSourceDomainName() {	// retrieves the name of the source domain
	    return mSourceDomainName;
	}

    // sets the name of the target domain
    
	public void setTargetDomainName(String targetDomainName) throws Exception {	
	    if (targetDomainName == null || targetDomainName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG527: Domain name cannot be null " +
                                             "or an empty string."));
	    }
	    mTargetDomainName = targetDomainName;
	}
	public String getTargetDomainName() {	// retrieves the name of the target domain
	    return mTargetDomainName;
	}
	
	public HashMap<String,  RelationshipScreenConfigInstance> getRelationships() {
	    return mRelationshipScreenConfigInstances;
	}
	
	// add a relationship screen configuration instance
	// RESUME HERE--add check if relationship already exists
	
	public void addRelScreenConfigInstance(RelationshipScreenConfigInstance rSCI) 
	        throws Exception {
	    if (rSCI == null) {
            throw new Exception(mLocalizer.t("CFG528: Relationship screen " +
                                             "configuration instance cannot be null."));
	    }
	    String relationshipName = rSCI.getRelationshipDef().getName();
	    mRelationshipScreenConfigInstances.put(relationshipName, rSCI);
	}

    // retrieves a relationshiop screen configuration object
	public RelationshipScreenConfigInstance getRelScreenConfigInstance(String relationshipName) 
	        throws Exception {
	            
	    if (relationshipName == null || relationshipName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG529: Relationship name cannot be null " +
                                             "or an empty string."));
	    }
	    return mRelationshipScreenConfigInstances.get(relationshipName);
	}
	
    // removes a relationship screen configuration instance
	public void removeRelScreenConfigInstance(String relationshipName) 
	        throws Exception {

	    if (relationshipName == null || relationshipName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG530: Relationship name cannot be null " +
                                             "or an empty string."));
	    }
	            
	    mRelationshipScreenConfigInstances.remove(relationshipName);
	}
}
