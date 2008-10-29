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
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.association.Domain;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.core.context.JndiResource;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;


public class MDConfigManager {

    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.MDConfigManager");
    private static transient final Localizer mLocalizer = Localizer.get();
    
	private static MDConfigManager mInstance = null;	// instance
    public static final String FIELD_DELIM = ".";
    private static String DATEFORMAT;
	private static ObjectSensitivePlugIn mObjectSensitivePlugIn;
    
	private HashMap<Integer, ScreenObject> mScreens;
	private HashMap<String, RelationshipScreenConfig> mRelationshipScreenConfigs;
	private HashMap<String, DomainScreenConfig> mDomainScreenConfigs;       // Screen configurations for all domains
	private Integer mInitialScreenID;	                // ID of the initial screen
	
	
	public MDConfigManager() throws ConfigException {
	    init();
	}

	public MDConfigManager init() throws ConfigException {    //  initializes the config manager
        synchronized (MDConfigManager.class) {
            if (mInstance != null) {
                return mInstance;
            }
    	    mScreens = new HashMap<Integer, ScreenObject>();
    	    mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
    	    mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       
    	    // RESUME HERE
    	    // Initialize the MDConfigManager
    	    
//            String dateFormat = MetaDataService.getDateFormat();
            String dateFormat = "MM/dd/yyyy";
            
            if (dateFormat == null || dateFormat.trim().length() == 0) {
                mLogger.info(mLocalizer.x("CFG001: The date format is not defined in object definition file."));
                DATEFORMAT = "MM/dd/yyyy";
            } else {
                if (isDateFormatValid(dateFormat)) {
                    DATEFORMAT = dateFormat;
                } else {
                    throw new ConfigException(mLocalizer.t("CFG532: Date format '{0}" + 
                            "' in object definition configuration is not supported.", dateFormat));
                }
            }
            mLogger.info(mLocalizer.x("CFG002: The date format is {0}", DATEFORMAT));
    	    
    	    return mInstance;
    	}
	}  

	public static MDConfigManager getInstance() {  //  obtains config manager, initializes if necessary
	    return mInstance;
	}

	public void reinitialize() {  //  forces reinitialization of the config manager
        synchronized (MDConfigManager.class) {
    	    mScreens = new HashMap<Integer, ScreenObject>();
    	    mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
    	    mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       
    	    // RESUME HERE
    	    // Initialize the MDConfigManager
    	}
	}

    // add an entry into the DomainScreenConfig hashmap
    
    public void addDomainScreenConfig(DomainScreenConfig dsc) throws Exception {  
        if (dsc == null) {
            throw new Exception(mLocalizer.t("CFG506: Domain Screen Configuration cannot be null."));
        }
        String domainName = dsc.getDomain().getName();
        if (mDomainScreenConfigs.containsKey(domainName)) {
            throw new Exception(mLocalizer.t("CFG519: Domain screen configuration " + 
                                             "cannot be added because it conflicts " +
                                             "with the name of an existing domain: {0}.", 
                                             domainName));
        }
        mDomainScreenConfigs.put(domainName, dsc);
    }
    
    // remove an entry from the DomainScreenConfig hashmap
    
    public void removeDomainScreenConfig(String domainName) throws Exception {  
        if (domainName == null) {
            throw new Exception(mLocalizer.t("CFG509: Domain name cannot be null."));
        }
        mDomainScreenConfigs.remove(domainName);
    }
    
    //  retrieves a Domain Screen Configuration
    
	public DomainScreenConfig getDomainScreenConfig(String domainName) throws Exception {  
        if (domainName == null || domainName.length() == 0) {
            throw new Exception(mLocalizer.t("CFG517: Domain name cannot be null or an empty string."));
        }
        DomainScreenConfig dSC = mDomainScreenConfigs.get(domainName);
        if (dSC == null) {
            throw new Exception(mLocalizer.t("CFG518: The domain named \"{0}\" could not be located.", domainName));
        }
        return dSC;
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
    
    public void addRelationshipScreenConfig(RelationshipScreenConfig rSC) throws Exception {  
        if (rSC == null) {
            throw new Exception(mLocalizer.t("CFG508: Relationship Screen Configuration cannot be null."));
        }
        String domainNames = rSC.getSourceDomainName() + rSC.getTargetDomainName();
        mRelationshipScreenConfigs.put(domainNames, rSC);
    }

    // remove an entry into the Relationship hashmap.  These are all the relationships for a source
    // and target domain.
    
    public void removeRelationshipScreenConfig(String sourceDomainName, String targetDomainName) throws Exception {  
        if (sourceDomainName == null || targetDomainName == null) {
            throw new Exception(mLocalizer.t("CFG513: Source Domain Name and " + 
                                             "Destination Domain Name may not be null."));
        }
        mRelationshipScreenConfigs.remove(sourceDomainName + targetDomainName);
    }

    // Retrieves all the relationships for a source and target domain.  sourceDOmainName + targetDomainName is the key
    
    public RelationshipScreenConfig getRelationshipScreenConfigs(String sourceDomainName, String targetDomainName) 
                        throws Exception {  
                            
        if (sourceDomainName == null || targetDomainName == null) {
            throw new Exception(mLocalizer.t("CFG514: Source Domain Name and " + 
                                             "Target Domain Name may not be null."));
        }
        return mRelationshipScreenConfigs.get(sourceDomainName + targetDomainName);
    }
    
    // add a relationship config instance    
   
    public void addRelationshipScreenConfigInstance(RelationshipScreenConfigInstance rSCI)  throws Exception {  

        if (rSCI == null) {
                
            throw new Exception(mLocalizer.t("CFG515: RelationshipScreenCOnfigInstance cannot be null."));
        }
        RelationshipType rel = rSCI.getRelationshipType();
        String sourceDomainName = rel.getSourceDomain();
        String targetDomainName = rel.getTargetDomain();
//        String relationshipName = null;
	    RelationshipScreenConfig rSC = 
	            getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
	    if (rSC != null) {
	        rSC.addRelScreenConfigInstance(rSCI);
	    } else {
                throw new Exception(mLocalizer.t("CFG516: Source Domain ({1}) and " +
                                                 "Target Domain ({2}) not found.", 
                                                 sourceDomainName,targetDomainName));       
        }
    }
    

    // remove a relationship config instance    
   
    public void removeRelationshipScreenConfigInstance(String sourceDomainName, String targetDomainName, 
	                                                   String relationshipName)  throws Exception {  

        if (sourceDomainName == null || targetDomainName == null || 
            relationshipName == null) {
                
            throw new Exception(mLocalizer.t("CFG511: Source Domain Name, " + 
                                             "Destination Domain Name and " + 
                                             "Relationship Name may not be null."));
        }
	    RelationshipScreenConfig rSC = getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
	    if (rSC != null) {
	        rSC.removeRelScreenConfigInstance(relationshipName);
	    } else {
                throw new Exception(mLocalizer.t("CFG512: Relationship ({0}) " +
                                                 "not found for Source Domain ({1}), " +
                                                 "Target Domain ({2}).", 
                                                 relationshipName, sourceDomainName,
                                                 targetDomainName));
	    }
    }
    
    //  returns hashmap of all Relationship instances 
	public HashMap<String, RelationshipScreenConfig> getRelationships() {  
	    return mRelationshipScreenConfigs;
	}

	
    //  returns a specific relationships for a source and target domain
	public RelationshipScreenConfigInstance
	        getRelationshipScreenConfig(String sourceDomainName, String targetDomainName, 
	                                     String relationshipName)  throws Exception {  
	    RelationshipScreenConfig rSC = getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
	    if (rSC != null) {
	        return rSC.getRelScreenConfigInstance(relationshipName);
	    } else {
                throw new Exception(mLocalizer.t("CFG510: Relationship ({0}) " +
                                                 "not found for Source Domain ({1}), " +
                                                 "Target Domain ({2}).", 
                                                 relationshipName, targetDomainName,
                                                 sourceDomainName));
	    }
	}
	
    // retrieves the HierarchyScreenConfig object with the matching name and domain
    
    public HierarchyScreenConfig getHierarchyScreenConfig(String domainName, String hierarchyName) 
            throws Exception {
        DomainScreenConfig dSC = getDomainScreenConfig(domainName);
        if (dSC == null)  {
            throw new Exception(mLocalizer.t("CFG520: The domain named \"{0}\" could " +
                                             "not be located for the hierarchy " + 
                                             "named \"{1}\".", domainName, hierarchyName));
        }
        HierarchyScreenConfig hSC = dSC.getHierarchyScreenConfig(hierarchyName);
        if (hSC == null)  {
            throw new Exception(mLocalizer.t("CFG521: Could not retrieve the hierarchy " +
                                             "named \"{0}\" for the domain named \"{1}\".", 
                                             hierarchyName, domainName));
        }
        
        return hSC;
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

	// testing--raymond tam
	// RESUME HERE
    public static JndiResource getJndiResource(String id, String name, String type, String description) {
        return null;
    }
    
	// testing--raymond tam
	// RESUME HERE
    public static Properties getJndiProperties() {
        return null;
    }

    //  set the handle to the security plug-in
	public static void setObjectSensitivePlugIn(ObjectSensitivePlugIn plugIn) {  
	    mObjectSensitivePlugIn = plugIn;
	}

    //  return the handle to the security plug-in
	public static ObjectSensitivePlugIn getObjectSensitivePlugIn() {  
	    return mObjectSensitivePlugIn;
    }
    
    /**
     * Gets the DATEFORMAT attribute of the ConfigManager class
     *
     * @return the date format defined in object definition
     */
    public static String getDateFormat() {
        return DATEFORMAT;
    }
    
    /**
     * Gets the input mask of date in the ConfigManager class
     *
     * @return the input mask of date based on the date format
     */
    public static String getDateInputMask() {
        String format = DATEFORMAT;
        String dateMask = format.replace('M', 'D');
        dateMask = dateMask.replace('d', 'D');
        dateMask = dateMask.replace('y', 'D');
        return dateMask;
    }
    
    // validate that the date format is supported or not
    private static boolean isDateFormatValid(String format) {
        int dotIdx = format.indexOf('.');
        int slashIdx = format.indexOf('/');
        int dashIdx = format.indexOf('-');
        if (dotIdx == -1 &&  slashIdx == -1 && dashIdx == -1) {
                return false;
        }
        
        if (dotIdx > -1) {
            format = format.replace('.', '/');
            return isValid(format);
        }
        if (slashIdx > -1) {
            return isValid(format);
        }
        if (dashIdx > -1) {
            format = format.replace('-', '/');
            return isValid(format);
        }
        return false;
    }
    
    private static boolean isValid(String format) {
        if (format.equals("MM/dd/yyyy")) {
            return true;
        } else if (format.equals("dd/MM/yyyy")) {
            return true;
        } else if (format.equals("yyyy/MM/dd")) {
            return true;
        } else if (format.equals("yyyy/dd/MM")) {
            return true;
        }
        return false;
    }
    

}
