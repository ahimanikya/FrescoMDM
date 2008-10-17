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
    
	private HashMap<Integer, ScreenObject> mScreens;		// each entry contains a ScreenObject
	private HashMap<String, Relationship> mRelationships;	// each entry contains a Relationship
	private HashMap<String, Domain> mDomains;		// each entry contains a Domain
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

    // add an entry into the ScreenObject hashmap
    
    public void addScreen(ScreenObject screenObject) throws Exception {  
        if (screenObject == null) {
            throw new Exception(mLocalizer.t("CFG507: ScreenObject cannot be null."));
        }
        // RESUME HERE--retrieve the ScreenID from the ScreenObject
        Integer screenID = new Integer(0);
        mScreens.put(screenID, screenObject);
    }
    
    // add an entry into the Relationship hashmap
    // I'm not sure this will work--relationships are defined within a domain
    
    public void addRelationship(Relationship relationship) throws Exception {  
        if (relationship == null) {
            throw new Exception(mLocalizer.t("CFG508: Relationship cannot be null."));
        }
        // RESUME HERE--retrieve the ScreenID from the ScreenObject
        String relationshipName = null;
        mRelationships.put(relationshipName, relationship);
    }
    
    // add an entry into the Domain hashmap
    
    public void addDomain(Domain domain) throws Exception {  
        if (domain == null) {
            throw new Exception(mLocalizer.t("CFG506: Domain cannot be null."));
        }
        String domainName = null;
        // RESUME HERE--retrieve the domainName from the domain
        mDomains.put(domainName, domain);
    }
    
	public HashMap<Integer, ScreenObject> getScreens() {  //  returns hashmap of all top-level screen objects (id is the key)
	    return mScreens;
	}

	public HashMap<String, Relationship> getRelationships() {  //  returns hashmap of all Relationship instances (sourceDomain.name+targetDomain.name+relationshipName is the key)
	    return mRelationships;
	}

	public HashMap<String, Domain> getDomains(){  //  returns hashmap of all Domain instances (domainName is the key)
	    return mDomains;
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

    //  retrieves a Domain
    
	public Domain getDomain(String domainName) throws Exception {  
        if (domainName == null || domainName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG504: Domain name cannot be null nor an empty string."));
        }
        Domain domain = mDomains.get(domainName);
        if (domain == null) {
            throw new Exception(mLocalizer.t("CFG505: The domain named \"{0}\" could not be located.", domainName));
        }
        return domain;
	}
    
}
