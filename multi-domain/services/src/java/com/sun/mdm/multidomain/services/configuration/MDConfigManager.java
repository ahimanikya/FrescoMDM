/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
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
import com.sun.mdm.multidomain.services.configuration.Domain;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.HashMap;
import java.util.ArrayList;


public class MDConfigManager {

    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.MDConfigManager");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    public static final String FIELD_DELIM = ".";
    
	private HashMap<Integer, ScreenObject> mScreens;
	private HashMap<String, RelationshipScreenConfig> mRelationshipScreenConfigs;
	private HashMap<String, DomainScreenConfig> mDomainScreenConfigs;       // Screen configurations for all domains
	private MDConfigManager mInstance;	// instance
	private Integer mInitialScreenID;	// ID of the initial screen
	private ObjectSensitivePlugIn mSecurityPlugIn;
	
	
	public MDConfigManager() {
	}

	public void init() {    //  initializes the config manager
	}  

	public MDConfigManager getInstance() {  //  obtains config manager, initializes if necessary
	    return mInstance;
	}

	public void reinitialize() {  //  forces reinitialization of the config manager
	}

    // add an entry into the DomainScreenConfig hashmap
    
    public void addDomainScreenConfig(DomainScreenConfig dsc) throws Exception {  
        if (dsc == null) {
            throw new Exception(mLocalizer.t("CFG506: Domain Screen Configuration cannot be null."));
        }
        // RESUME HERE
//        String domainName = dsc.getDomain().getName();
//        mDomainScreenConfigs.put(domainName, dsc);
    }
    
    // remove an entry from the DomainScreenConfig hashmap
    
    public void removeDomainScreenConfig(String domainName) throws Exception {  
        if (domainName == null) {
            throw new Exception(mLocalizer.t("CFG509: Domain name cannot be null."));
        }
        mDomainScreenConfigs.remove(domainName);
    }
    
    //  retrieves a Domain
    
	public Domain getDomain(String domainName) throws Exception {  
        if (domainName == null || domainName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG504: Domain name cannot be null nor an empty string."));
        }
        DomainScreenConfig dsc = mDomainScreenConfigs.get(domainName);
        if (dsc== null) {
            throw new Exception(mLocalizer.t("CFG505: The domain named \"{0}\" could not be located.", domainName));
        }
        Domain domain = dsc.getDomain();
        return domain;
	}
	
    // add an entry into the ScreenObject hashmap
    
    public void addScreen(ScreenObject screenObject) throws Exception {  
        if (screenObject == null) {
            throw new Exception(mLocalizer.t("CFG507: ScreenObject cannot be null."));
        }
        Integer screenID = screenObject.getID();
        mScreens.put(screenID, screenObject);
    }
    
	public HashMap<Integer, ScreenObject> getScreens() {  //  returns hashmap of all top-level screen objects (id is the key)
	    return mScreens;
	}

    // removes an entry from the ScreenObject hashmap
    
    public void removeScreen(Integer screenID) throws Exception {  
        mScreens.remove(screenID);
    }
    
    // add an entry into the Relationship hashmap
    // I'm not sure this will work--relationships are defined within a domain
    
    public void addRelationshipScreenConfig(RelationshipScreenConfig rsc) throws Exception {  
        if (rsc == null) {
            throw new Exception(mLocalizer.t("CFG508: Relationship Screen Configuration cannot be null."));
        }
        // RESUME HERE--retrieve the ScreenID from the ScreenObject
        String relationshipName = null;
        mRelationshipScreenConfigs.put(relationshipName, rsc);
    }
    
	public HashMap<String, RelationshipScreenConfig> getRelationships() {  //  returns hashmap of all Relationship instances (sourceDomain.name+targetDomain.name+relationshipName is the key)
	    return mRelationshipScreenConfigs;
	}

	public HashMap<String, DomainScreenConfig> getDomainScreenConfigs(){  //  returns hashmap of all Domain Screen Configuration instances (domainName is the key)
	    return mDomainScreenConfigs;
	}
	
	//  returns a screen with the matching ID

	public ScreenObject getScreen(Integer screenID) throws Exception {  
        ScreenObject scrObj = (ScreenObject) mScreens.get(screenID);
        if (scrObj == null) {
            if (mScreens.containsKey(screenID) == false) {
                throw new Exception(mLocalizer.t("CFG503: Screen ID not found: {0}", screenID));
            }
        } 
        return scrObj;
	}

	public String getMasterControllerJndi() {  //  return the jndi name for the master controller
	    return null;
	}

	public ObjectSensitivePlugIn getSecurityPlugIn() {  //  return the handle to the security plug-in
	    return mSecurityPlugIn;
	}

	public ArrayList<Relationship> getRelationshipsForDomain(String sourceDomainName, String targetDomainName) {  //  retrieves Relationships for a source and target domain
	    return null;
	}

	public ArrayList<Relationship> getRelationshipsForDomain(String domainName) {  //  retrieves all Relationships for a domain (can be source or target)
	    return null;
	}

	public ArrayList<Relationship> getDomainsForRelationship(String relationshipName) {  //  retrieves Domains for a relationship (domain can be source or target). 
	    return null;
	}

	public Relationship getRelationship(String relationshipName, String sourceDomainName, String targetDomainName)  { //  retrieves a Relationship
	    return null;
    }

    
}
