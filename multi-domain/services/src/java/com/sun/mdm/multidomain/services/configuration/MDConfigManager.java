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

import com.sun.mdm.multidomain.parser.MultiDomainWebManager;
import com.sun.mdm.multidomain.parser.DomainForWebManager;
import com.sun.mdm.multidomain.parser.DomainsForWebManager;
import com.sun.mdm.multidomain.parser.SimpleSearchType;
import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.SearchOptions;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.core.context.JndiResource;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.association.Domain;
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
    
    public static final String FIELD_DELIM = ".";
    public static final int DEFAULT_RECORD_SUMMARY_ID = 1;
    public static final int DEFAULT_RECORD_DETAILS_ID = 1;
    
	private static HashMap<Integer, ScreenObject> mScreens;
	private static HashMap<String, RelationshipScreenConfig> mRelationshipScreenConfigs;
	private static HashMap<String, DomainScreenConfig> mDomainScreenConfigs;       // Screen configurations for all domains
    private static String DATEFORMAT;
	private static MDConfigManager mInstance = null;	// instance
	private static Integer mInitialScreenID;	                // ID of the initial screen
	private static ObjectSensitivePlugIn mObjectSensitivePlugIn;
	private static MultiDomainWebManager mDWMInstance = null;
	
	
	public MDConfigManager() throws ConfigException {
	    init();
	}

    /**
     * Initializes the MDConfigManager.  If it has already been initialized, it returns
     * the instance of the MDConfigManager
     *
     * @return the initialized instance of the MDConfigManager
     * @throws ConfigException if an error is encountered
     */
	public static MDConfigManager init() throws ConfigException {
	    try {
            synchronized (MDConfigManager.class) {
                if (mInstance != null) {
                    return mInstance;
                }
        	    mScreens = new HashMap<Integer, ScreenObject>();
        	    mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
        	    mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       
        	    
        	    mDWMInstance = new MultiDomainWebManager();
        	    
        	    configureDomains();
        	    configureRelationships();
        	    configureHierarchies();
        	    configureGroups();
        	    configureScreens();
        	    
        	    return mInstance;
        	}
        } catch (Exception e) {
            throw new ConfigException(e);
        }
	}  

    /**
     * Returns the instance of the MDConfigManager
     *
     * @return the initialized instance of the MDConfigManager
     * @throws ConfigException if an error is encountered
     */
	public static MDConfigManager getInstance() throws ConfigException {
	    return mInstance;
	}

    /**
     * Configures the domains by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureDomains() throws ConfigException {
        DomainsForWebManager dsFWM = mDWMInstance.getDomains();
        
        ArrayList<DomainForWebManager> domains = dsFWM.getDomains();
        for (DomainForWebManager domain : domains) {
            DomainScreenConfig domainScreenConfig = new DomainScreenConfig();
            
            // testing--raymond tam
            // RESUME HERE
            SummaryID summaryID = convertSummaryID();
            domainScreenConfig.setSummaryID(summaryID);

            ArrayList<SearchScreenConfig> sSC = 
                    convertSearchType(domain.getSearchType());
            domainScreenConfig.setSearchScreenConfigs(sSC);
            
            ArrayList<SearchResultsConfig> sRC = 
                    convertSearchDetail(domain.getSearchDetail());
            domainScreenConfig.setSearchResultsConfigs(sRC);


// testing--raymond tam
// RESUME HERE
//            ArrayList<SearchResultSummaryConfig> sRSC = 
//                    convertSearchSummaryConfigs(domain.getRecordDetailList());
//            domainScreenConfig.searchResultDetailsConfigs(sRSC);
            
            ArrayList<SearchResultDetailsConfig> sRDC = 
                    convertRecordDetailList(domain.getRecordDetailList());
            domainScreenConfig.setSearchResultDetailsConfigs(sRDC);
            
            // testing--raymond tam
            // RESUME HERE
            // domain types conflict
//            domainScreenConfig.setDomain(null);
            mInstance.addDomainScreenConfig(domainScreenConfig);
        }
    }


    /**
     * Converts the Summary ID information retrieved from the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
     
    private static SummaryID convertSummaryID() throws ConfigException {
        // testing--raymond tam
        // RESUME HERE
        return null;
    }

    /**
     * Converts the Search Screen information retrieved from the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
     
    private static ArrayList<SearchScreenConfig> convertSearchType(ArrayList<SimpleSearchType> sST) 
            throws ConfigException {
        if (sST == null || sST.size() == 0) {
            return null;
        }

        ArrayList<SearchScreenConfig> searchScreenConfigs = 
                new ArrayList<SearchScreenConfig>();
        for (SimpleSearchType sSTObj : sST) {
            
            ObjectNodeConfig rootObj = null;
            String screenTitle = sSTObj.getScreenTitle();
            String instruction = sSTObj.getInstruction();
            int searchResultID = sSTObj.getScreenResultID();
            int screenOrder = sSTObj.getScreenOrder();
            boolean showEUID = false;
            boolean showLID = false;
            ArrayList<FieldGroup> sSTObjObjFieldGroup = sSTObj.getFieldGroups();
            SearchOptions sSTObjSearchOpt = sSTObj.getSearchOption();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(sSTObjObjFieldGroup);
            SearchScreenOptions options  = convertSearchOptions(sSTObjSearchOpt);
            SearchScreenConfig sSCObj = new SearchScreenConfig(rootObj, screenTitle, 
                                                               instruction, searchResultID, 
                                                               screenOrder, showEUID, 
                                                               showLID, options, 
                                                               fieldConfigGroups);
            searchScreenConfigs.add(sSCObj);
        }
        return searchScreenConfigs;
    }

    /**
     * Converts an ArrayList of FieldGroup objects to an ArrayList of FieldConfigGroup 
     * objects.  The FieldConfigGroup objects are retrieved from the parser.
     *
     * @param fieldGroups An ArrayList of FieldGroup objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<FieldConfigGroup> convertFieldGroups(ArrayList<FieldGroup> fieldGroups) 
            throws ConfigException {
        if (fieldGroups == null || fieldGroups.size() == 0) {
            return null;
        }
        ArrayList<FieldConfigGroup> fieldConfigGroups = new ArrayList<FieldConfigGroup>();
        
        for (FieldGroup fg : fieldGroups) {
            FieldConfigGroup fcg = convertFieldGroup(fg);
            fieldConfigGroups.add(fcg);
        }
        return fieldConfigGroups;
    }

    /**
     * Converts a FieldGroup object to a FieldConfigGroup object
     *
     * @param fieldGroups A FieldGroup objects to convert.
     * @throws ConfigException if an error is encountered
     */    
     
    private static FieldConfigGroup convertFieldGroup(FieldGroup fieldGroup) 
            throws ConfigException {
        if (fieldGroup == null) {
            return null;
        }
        // testing--raymond tam
        // RESUME HERE
        // set the description for the FieldConfigGroup
        String description = null;
        
        
        // pass field refs
        ArrayList<FieldConfig> fieldConfigs = createFieldConfig(fieldGroup.getFieldRefs());
        FieldConfigGroup fieldConfigGroup = new FieldConfigGroup(description, fieldConfigs);
        return fieldConfigGroup;
    }

    /**
     * Converts an ArrayList of RelationFieldReference objects to an ArrayList
     * to an ArrayList of FieldConfig objects.
     *
     * @param fieldGroups A FieldGroup objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<FieldConfig> convertRelationFieldReferences(ArrayList<RelationFieldReference> relFieldRefs) 
            throws ConfigException {
    
        if (relFieldRefs == null) { 
            return null;
        }
        ArrayList<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();
        for (RelationFieldReference fieldRef : relFieldRefs) {
            FieldConfig fieldConfig = convertRelationFieldReference(fieldRef);
            fieldConfigs.add(fieldConfig);
        }
        return fieldConfigs;
    }
    
    /**
     * Converts an ArrayList of FieldRef objects to an ArrayList of FieldConfig objects.
     *
     * @param fieldRefs An ArrayList of FieldCodeRef objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    // RESUME HERE
    // need to pass in ObjectConfig
    private static ArrayList<FieldConfig> createFieldConfig(ArrayList<FieldGroup.FieldRef> fieldRefs) throws ConfigException {
        if (fieldRefs == null) { 
            return null;
        }
        ArrayList<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();
        for (FieldGroup.FieldRef field : fieldRefs) {
            String rootObj = null;
            String objRef = null;
            String name = field.getFieldName();
            String displayName = null;
            String guiType = null;
            int maxLength = 0;
            String valueList = null;
            String inputMask = null;
            String valueMask = null;
            int valueType = 0;
            boolean keyType = false;
            boolean isSensitive = false;
            int displayOrder = 0;
                  
            FieldConfig fieldConfig = new FieldConfig(rootObj, objRef, name, 
                                                      displayName, guiType, maxLength, 
                                                      valueList, inputMask, valueMask,
                                                      valueType, keyType, isSensitive,
                                                      displayOrder);
            fieldConfigs.add(fieldConfig);                                                      
        }
        return fieldConfigs;
    }
    
    /**
     * Converts a RelationFieldReference object to a FieldConfig object;
     *
     * @param fieldRefs An ArrayList of FieldCodeRef objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static FieldConfig convertRelationFieldReference(RelationFieldReference relFieldRef) 
            throws ConfigException {

// RESUME HERE
//        String objRef = relFieldRef.getObjectName();
        String objRef = null;
        boolean isSensitive = relFieldRef.isSensitive();
        String inputMask = relFieldRef.getInputMask();
        String name = relFieldRef.getFieldName();
        String displayName = relFieldRef.getFieldDisplayName();
        int displayOrder = relFieldRef.getDisplayOrder();
        int maxLength = relFieldRef.getMaxLength();
        String guiType = relFieldRef.getGuiType();
        String valueList = relFieldRef.getValueList();
        String valueMask = relFieldRef.getValueList();
        int valueType = Integer.parseInt(relFieldRef.getValueType());
        boolean keyType = relFieldRef.getKeyType();
                
        FieldConfig fieldConfig = new FieldConfig(null, objRef, name, displayName, 
                                                  guiType, maxLength, valueList,
                                                  inputMask, valueMask, valueType, 
                                                  keyType, isSensitive, displayOrder);
        return fieldConfig;                                                  
    }
    
    /**
     * Converts a SearchOption object to a Search Screen Options object
     *
     * @param searchOptions A SearchOption instance to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static SearchScreenOptions convertSearchOptions(SearchOptions searchOptions) throws ConfigException {
        if (searchOptions == null) {
            return null;
        }

        String displayName = null;    
        int candidateThreshold = -1;
        
        ArrayList<SearchOptions.Parameter> searchOptionsParameters = searchOptions.getParameterList();
        if (searchOptionsParameters == null) {
            return null;
        }
        
        String queryBuilder = searchOptions.getQueryBulder();   
        boolean isWeighted = searchOptions.getWeighted();    


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair> ();
        for(SearchOptions.Parameter parameter : searchOptionsParameters) {
            String paramName = parameter.getName();
            String value = parameter.getValue();
            NameValuePair nameValuePair = new NameValuePair(paramName, value);
            nameValuePairs.add(nameValuePair);
        }
            
        SearchScreenOptions sSO = new SearchScreenOptions(displayName, queryBuilder, 
                                                          isWeighted, candidateThreshold,
                                                          nameValuePairs);
        return sSO;
    }
    
    /**
     * Converts an ArrayList of Search Detail objects to Search Result Config objects
     *
     * @param searchOptions A SearchOption instance to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<SearchResultsConfig> convertSearchDetail(ArrayList<SearchDetail> searchDetails) 
            throws ConfigException {
        if (searchDetails == null || searchDetails.size() == 0) {
            return null;
        }

        ArrayList<SearchResultsConfig> searchResultsConfigs = 
                new ArrayList<SearchResultsConfig>();
        for (SearchDetail searchDetail : searchDetails) {
            ObjectNodeConfig rootObj = null;
            String displayName = searchDetail.getDisplayName();
            int searchResultsID = searchDetail.getSearchResultID();
            // testing--raymond tam
            // RESUME HERE
//            int searchResultsSummaryID = searchDetail.getSearchSummaryID();
            int searchResultsSummaryID = DEFAULT_RECORD_SUMMARY_ID;
            int searchResultDetailsID = searchDetail.getRecordDetailID();
            int pageSize = searchDetail.getItemPerPage();
            int maxRecords = searchDetail.getMaxResultSize();
            boolean showEUID = false;
            boolean showLID = false;
            ArrayList<FieldGroup> searchDetailsFieldGroup = searchDetail.getFieldGroups();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(searchDetailsFieldGroup);
            try {
                SearchResultsConfig sRC = new SearchResultsConfig(rootObj, displayName, 
                                                   searchResultsID, searchResultsSummaryID, 
                                                   searchResultDetailsID, pageSize, 
                                                   maxRecords, showEUID, showLID, 
                                                   fieldConfigGroups);
                searchResultsConfigs.add(sRC);
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG544: Could not add the " +
                                                       "search result configuration " +
                                                       "named {1} and with " +
                                                       "searchResultsID = {1}", 
                                                       searchResultsID));
            }
        }
        return searchResultsConfigs;
    }

    /**
     * Converts an ArrayList of Record Detail List objects to an ArrayList of Search 
     * Result Details Details Config objects.
     *
     * @param searchOptions A SearchOption instance to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<SearchResultDetailsConfig> convertRecordDetailList(ArrayList<RecordDetail> recordDetailList) 
            throws ConfigException {
    
        if (recordDetailList == null || recordDetailList.size() == 0) {
            return null;
        }
        
        ArrayList<SearchResultDetailsConfig> searchResultDetailsConfig
                                                         = new ArrayList<SearchResultDetailsConfig> ();
        for (RecordDetail recordDetail : recordDetailList) {
            
            // testing--raymond tam
            // RESUME HERE
            ObjectNodeConfig rootObj = null;
            int searchResultDetailID = recordDetail.getRecordDetailId();
            String displayName = recordDetail.getDisplayName();
            boolean showEUID = false;
            boolean showLID = false;
            
            ArrayList<FieldGroup> recordDetailsFieldGroup = recordDetail.getFieldGroups();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(recordDetailsFieldGroup);
    
            SearchResultDetailsConfig sRDC = 
                        new SearchResultDetailsConfig(rootObj, displayName, searchResultDetailID, 
                                                      showEUID, showLID, fieldConfigGroups);
            searchResultDetailsConfig.add(sRDC);
                                                                  
        }
        return searchResultDetailsConfig;
    }            

/*
    private ArrayList<SearchResultsSummaryConfig> convertSearchResultsSummaryConfigs(RecordDetail recordDetail) 
            throws ConfigException {
        // RESUME HERE
        // not yet implemented
    }            
*/
             
    /**
     * Configures the relationships by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureRelationships() throws ConfigException {
        ArrayList<RelationshipType> relationshipTypes = mDWMInstance.getRelationshipTypes();
        for (RelationshipType relationshipType : relationshipTypes) {
            String sourceDomainName = relationshipType.getSourceDomain();
    	    String targetDomainName = relationshipType.getTargetDomain();
    	    RelationshipScreenConfig relationshipScreenConfig = null;
    	    if (!relationshipScreenConfigExists(sourceDomainName, targetDomainName)) {
                relationshipScreenConfig 
                        = new RelationshipScreenConfig(sourceDomainName, targetDomainName);
                 addRelationshipScreenConfig(relationshipScreenConfig);
            } else {
                relationshipScreenConfig = 
                        getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
            }
            String displayName = relationshipType.getDisplayName();
            String relationshipName = relationshipType.getName();
            
            ArrayList<RelationFieldReference> relTypePredefinedAttributes 
                    = relationshipType.getFixedRelFieldRefs();     	
            ArrayList<FieldConfig> predefinedAttributes 
                    = convertRelationFieldReferences(relTypePredefinedAttributes);
                    
            ArrayList<RelationFieldReference> relTypeExtendedAttributes 
                    = relationshipType.getExtendedRelFieldRefs();     	    
            ArrayList<FieldConfig> extendedAttributes 
                    = convertRelationFieldReferences(relTypeExtendedAttributes);
            
            RelationshipScreenConfigInstance rSCI 
                    = new RelationshipScreenConfigInstance(relationshipName,
                                                           displayName, 
                                                           predefinedAttributes,
                                                           extendedAttributes);
            try {
                relationshipScreenConfig.addRelScreenConfigInstance(rSCI);
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG543: Could not add the " +
                                                       "relationship ({1}) for Source " +
                                                       "Domain ({2}) and Target Domain ({3}).", 
                                                       relationshipName, sourceDomainName,
                                                       targetDomainName));
            }
        }
    }
    
    /**
     * Configures the hierarchies by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureHierarchies() throws ConfigException {
        // RESUME HERE
        // not yet implemented
    }
    
    /**
     * Configures the groups by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureGroups() throws ConfigException {
        // RESUME HERE
        // not yet implemented
    }
    
    /**
     * Configures the the general screen definitions.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureScreens() throws ConfigException {
        // RESUME HERE
        // not yet implemented
        
        boolean initialScreenIDLocated = false;
        Integer initialScreenID = - 1;
        
/*        
        ArrayList<ScreenDef> screenDefintion = mDWMInstance.getScreenDefinitions();
        for (ScreenDef screenDefintion : screenDefinitionObject) {
            Integer id = screenDefinitionObject.getID();
            if (initialScreenIDLocated == false && id.compareTo(initialScreenID) < 0 ) {
                initialScreenID = id;
            }
          
            String displayTitle= screenDefinitionObject.getDisplayTitle();
            int displayOrder = screenDefinitionObject.getDisplayOrder();
            ArrayList<ScreenObject> subscreens = screenDefinitionObject.getSubscreens();
    	    ScreenObject scrObj = new ScreenObject(id, displayTitle, displayOrder, 
    	                                           null, null, subscreens);
            addScreen(scrObj);
        }

*/        
        // If default screen ID not found, set it to the screen with the lowest ID.

        setInitialScreenID(initialScreenID);
    }

    /**
     * Forces a re-initialization of the the MDConfigManager.  
     *
     * @return the initialized instance of the MDConfigManager
     * @throws ConfigException if an error is encountered
     */
	public static MDConfigManager reinitialize() throws ConfigException {  
        synchronized (MDConfigManager.class) {
    	    mScreens = new HashMap<Integer, ScreenObject>();
    	    mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
    	    mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       
    	    // RESUME HERE
    	    // Initialize the MDConfigManager
    	}
    	return mInstance;
	}

    /**
     * Add a domain screen configuration into the DomainScreenConfig hashmap.
     *
     * @param dsc  The domain screen configuration object to add.
     * @throws ConfigException if an error is encountered
     */    
    public static void addDomainScreenConfig(DomainScreenConfig dsc) throws ConfigException {  
        if (dsc == null) {
            throw new ConfigException(mLocalizer.t("CFG506: Domain Screen Configuration cannot be null."));
        }
        String domainName = dsc.getDomain().getName();
        if (mDomainScreenConfigs.containsKey(domainName)) {
            throw new ConfigException(mLocalizer.t("CFG519: Domain screen configuration " + 
                                             "cannot be added because it conflicts " +
                                             "with the name of an existing domain: {0}.", 
                                             domainName));
        }
        mDomainScreenConfigs.put(domainName, dsc);
    }
    
    
    /**
     * Remove a domain screen configuration from the DomainScreenConfig hashmap.
     *
     * @param domainName  The name of the domain screen configuration object to remove.
     * @throws ConfigException if an error is encountered
     */    
    public static void removeDomainScreenConfig(String domainName) throws ConfigException {  
        if (domainName == null) {
            throw new ConfigException(mLocalizer.t("CFG509: Domain name cannot be null."));
        }
        mDomainScreenConfigs.remove(domainName);
    }
    
    /**
     * Retrieves a domain screen configuration from the DomainScreenConfig hashmap.
     *
     * @param domainName  The name of the domain screen configuration object to retrieve.
     * @return A domain screen confgiration for a domain.
     * @throws ConfigException if an error is encountered
     */    
	public static DomainScreenConfig getDomainScreenConfig(String domainName) throws ConfigException {  
        if (domainName == null || domainName.length() == 0) {
            throw new ConfigException(mLocalizer.t("CFG517: Domain name cannot be null or an empty string."));
        }
        DomainScreenConfig dSC = mDomainScreenConfigs.get(domainName);
        if (dSC == null) {
            throw new ConfigException(mLocalizer.t("CFG518: The domain named \"{0}\" could not be located.", domainName));
        }
        return dSC;
	}
	
    /**
     * Retrieves a domain object.
     *
     * @param domainName  The name of the domain to retrieve.
     * @return A domain whose name matching the search criteria.
     * @throws ConfigException if an error is encountered
     */    
	public static Domain getDomain(String domainName) throws ConfigException {  
        if (domainName == null || domainName.length() == 0) {
            throw new ConfigException(mLocalizer.t("CFG504: Domain name cannot be null nor an empty string."));
        }
        DomainScreenConfig dsc = mDomainScreenConfigs.get(domainName);
        if (dsc== null) {
            throw new ConfigException(mLocalizer.t("CFG505: The domain named \"{0}\" could not be located.", domainName));
        }
        Domain domain = dsc.getDomain();
        return domain;
	}
	
    /**
     * Add an relationship screen configuration into the relationship hashmap
     *
     * @param rsc  The relationship screen configuration to add.
     * @throws ConfigException if an error is encountered
     */    
    public static void addRelationshipScreenConfig(RelationshipScreenConfig rSC) throws ConfigException {  
        if (rSC == null) {
            throw new ConfigException(mLocalizer.t("CFG508: Relationship Screen Configuration cannot be null."));
        }
        String domainNames = rSC.getSourceDomainName() + rSC.getTargetDomainName();
        mRelationshipScreenConfigs.put(domainNames, rSC);
    }

    /**
     * Removes all the relationships between a source and target domain.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @throws ConfigException if an error is encountered
     */    
    public static void removeRelationshipScreenConfig(String sourceDomainName, String targetDomainName) throws ConfigException {  
        if (sourceDomainName == null || targetDomainName == null) {
            throw new ConfigException(mLocalizer.t("CFG513: Source Domain Name and " + 
                                             "Destination Domain Name may not be null."));
        }
        mRelationshipScreenConfigs.remove(sourceDomainName + targetDomainName);
    }


    /**
     * Checks if a RelationshipScreenConfig object already exists for a source
     * and target domain.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @return true if a RelationshipScreenConfig object already exists, 
     * false otherwise.
     * @throws ConfigException if an error is encountered
     */    
    public static boolean relationshipScreenConfigExists(String sourceDomainName, 
                                                  String targetDomainName) 
                        throws ConfigException {  
        if (mRelationshipScreenConfigs.containsKey(sourceDomainName + targetDomainName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a RelationshipScreenConfigInstance object already exists for a source
     * and target domain and Relationship name.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @param relationshipName  The name of the relationship.
     * @return true if a RelationshipScreenConfigInstance object already exists, 
     * false otherwise.
     * @throws ConfigException if an error is encountered
     */    
    public static boolean relationshipScreenConfigInstanceExists(String sourceDomainName, 
                                                          String targetDomainName,
                                                          String relationshipName) 
                        throws ConfigException {  
        if (mRelationshipScreenConfigs.containsKey(sourceDomainName + targetDomainName)) {
            RelationshipScreenConfig rSCObj =
                        getRelationshipScreenConfigs(sourceDomainName, 
                                                       targetDomainName);
            try {
                RelationshipScreenConfigInstance rSCI = 
                            rSCObj.getRelScreenConfigInstance(relationshipName);
                if (rSCI != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG542: Could not retrieve the " +
                                                       "relationship ({1}) for Source " +
                                                       "Domain ({2}) and Target Domain ({3}).", 
                                                       relationshipName, sourceDomainName,
                                                       targetDomainName));
            }
        } else {
            return false;
        }
    }

    /**
     * Retrieves all the relationships for a source and target domain.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @return a RelationshipScreenConfig object contain the 
     * RelationshipScreenConfigInstance objects that represent the replationships.
     * @throws ConfigException if an error is encountered
     */    
    public static RelationshipScreenConfig getRelationshipScreenConfigs(String sourceDomainName, String targetDomainName) 
                        throws ConfigException {  
                            
        if (sourceDomainName == null || targetDomainName == null) {
            throw new ConfigException(mLocalizer.t("CFG514: Source Domain Name and " + 
                                             "Target Domain Name may not be null."));
        }
        return mRelationshipScreenConfigs.get(sourceDomainName + targetDomainName);
    }
    
    /**
     * Add a relationship configuration instance for a source and target domain.
     * Each relationship is represented by a unique instance of the 
     * RelationshipScreenConfigInstance object.
     *
     * @param rSCI  The RelationshipScreenConfig representing the relationship
     * configuration information to add.  
     * @throws ConfigException if an error is encountered
     */    
    public static void addRelationshipScreenConfigInstance(RelationshipScreenConfigInstance rSCI)  throws ConfigException {  

        if (rSCI == null) {
                
            throw new ConfigException(mLocalizer.t("CFG515: RelationshipScreenCOnfigInstance cannot be null."));
        }
        RelationshipDef rel = rSCI.getRelationshipDef();
        String sourceDomainName = rel.getSourceDomain();
        String targetDomainName = rel.getTargetDomain();
	    RelationshipScreenConfig rSC = 
	            getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
        try {	            
            if (rSC != null) {
                rSC.addRelScreenConfigInstance(rSCI);
            } else {
                throw new ConfigException(mLocalizer.t("CFG516: Source Domain ({0}) and " +
                                                       "Target Domain ({1}) not found.", 
                                                       sourceDomainName,targetDomainName));
            }
    	} catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG541: Source Domain ({0}) and " +
                                                       "Target Domain ({1}) not found.", 
                                                       sourceDomainName,targetDomainName));
    	}
    }
    

    /**
     * Remove a relationship configuration instance from a source and target domain.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @param relationshipName  The name of the relationship to remove.
     * @throws ConfigException if an error is encountered
     */    
    public static void removeRelationshipScreenConfigInstance(String sourceDomainName, String targetDomainName, 
	                                                   String relationshipName)  throws ConfigException {  

        if (sourceDomainName == null || targetDomainName == null || 
            relationshipName == null) {
                
            throw new ConfigException(mLocalizer.t("CFG511: Source Domain Name, " + 
                                             "Destination Domain Name and " + 
                                             "Relationship Name may not be null."));
        }
	    RelationshipScreenConfig rSC = getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
	    try {
    	    if (rSC != null) {
    	        rSC.removeRelScreenConfigInstance(relationshipName);
    	    } else {
                throw new ConfigException(mLocalizer.t("CFG512: Relationship ({0}) " +
                                                       "not found for Source Domain ({1}), " +
                                                       "Target Domain ({2}).", 
                                                       relationshipName, sourceDomainName,
                                                       targetDomainName));
    	    }
    	} catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG540: Relationship ({0}) " +
                                                   "not found for Source Domain ({1}), " +
                                                   "Target Domain ({2}).", 
                                                   relationshipName, sourceDomainName,
                                                   targetDomainName));
    	}
    }
    
    /**
     * Returns a hashmap of all RelationshipScreenConfig objects.
     *
     * @return a hashmap of all RelationshipScreenConfig objects.
     * @throws ConfigException if an error is encountered
     */    
	public static HashMap<String, RelationshipScreenConfig> getRelationships() {  
	    return mRelationshipScreenConfigs;
	}

    /**
     * Retrieves a specific relationship for a source and target domain.
     *
     * @param sourceDomainName  The name of the source domain.
     * @param targetDomainName  The name of the target domain.
     * @param relationshipName  The name of the relationship..
     * @return the RelationshipScreenConfigInstance representing the relationship.
     * @throws ConfigException if an error is encountered
     */    
	public static RelationshipScreenConfigInstance
	        getRelationshipScreenConfig(String sourceDomainName, String targetDomainName, 
	                                     String relationshipName)  throws ConfigException {  
	    RelationshipScreenConfig rSC = getRelationshipScreenConfigs(sourceDomainName, targetDomainName);
	    try {
    	    if (rSC != null) {
    	        return rSC.getRelScreenConfigInstance(relationshipName);
    	    } else {
                throw new ConfigException(mLocalizer.t("CFG510: Relationship ({0}) " +
                                                       "not found for Source Domain ({1}), " +
                                                       "Target Domain ({2}).", 
                                                       relationshipName, targetDomainName,
                                                       sourceDomainName));
    	    }
    	} catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG539: Relationship ({0}) " +
                                                   "not found for Source Domain ({1}), " +
                                                   "Target Domain ({2}).", 
                                                   relationshipName, targetDomainName,
                                                   sourceDomainName));
    	}
	}
	
    /**
     * Retrieves a specific hierarchy for a domain.
     *
     * @param domainName  The name of the domain.
     * @param hierarchyName  The name of the hierarchy.
     * @return the HierarchyScreenConfig instance representing the relationship.
     * @throws ConfigException if an error is encountered
     */    
    public static HierarchyScreenConfig getHierarchyScreenConfig(String domainName, String hierarchyName) 
            throws ConfigException {
        DomainScreenConfig dSC = getDomainScreenConfig(domainName);
        if (dSC == null)  {
            throw new ConfigException(mLocalizer.t("CFG520: The domain named \"{0}\" could " +
                                             "not be located for the hierarchy " + 
                                             "named \"{1}\".", domainName, hierarchyName));
        }
        try {
            HierarchyScreenConfig hSC = dSC.getHierarchyScreenConfig(hierarchyName);
            if (hSC == null)  {
                throw new ConfigException(mLocalizer.t("CFG521: Could not retrieve the hierarchy " +
                                                 "named \"{0}\" for the domain named \"{1}\".", 
                                                 hierarchyName, domainName));
            }
            
            return hSC;
        } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG538: Could not retrieve the hierarchy " +
                                                 "named \"{0}\" for the domain named \"{1}\".", 
                                                 hierarchyName, domainName));
        }
    }
	
    
    /**
     * Add a hierarchy screen configuration for a domain.
     *
     * @param domainName  The name of the domain.
     * @param hSC  The hierarchy screen configuration to add.
     * @throws ConfigException if an error is encountered
     */    
    public static void addHierarchyScreenConfig(String domainName, HierarchyScreenConfig hSC) 
            throws ConfigException {
        DomainScreenConfig dSC = getDomainScreenConfig(domainName);
        if (dSC == null)  {
            throw new ConfigException(mLocalizer.t("CFG545: The domain named \"{0}\" could " +
                                             "not be located.", domainName));
        }
        try {
            dSC.addHierarchyScreenConfig(hSC);
        } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG546: Could not add the hierarchy " +
                                                 "named \"{0}\" for the domain named \"{1}\".", 
                                                 hSC.getDisplayName(), domainName));
        }
    }
	
    /**
     * Remove a hierarchy screen configuration for a domain.
     *
     * @param domainName  The name of the domain.
     * @param hierarchyName  The name of the hierarchy screen configuration to remove.
     * @throws ConfigException if an error is encountered
     */    
    public static void removeHierarchyScreenConfig(String domainName, String hierarchyName) 
            throws ConfigException {
        DomainScreenConfig dSC = getDomainScreenConfig(domainName);
        if (dSC == null)  {
            throw new ConfigException(mLocalizer.t("CFG547: The domain named \"{0}\" could " +
                                             "not be located.", domainName));
        }
        try {
            dSC.removeHierarchyScreenConfig(hierarchyName);
        } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG548: Could not remove the hierarchy " +
                                                 "named \"{0}\" for the domain named \"{1}\".", 
                                                 hierarchyName, domainName));
        }
    }
	
    
    /**
     * Add a screen configuration.
     *
     * @param screenObject  Screen configuration to add.
     * @throws ConfigException if an error is encountered
     */    
    public static void addScreen(ScreenObject screenObject) throws ConfigException {  
        if (screenObject == null) {
            throw new ConfigException(mLocalizer.t("CFG507: ScreenObject cannot be null."));
        }
        Integer screenID = screenObject.getID();
        mScreens.put(screenID, screenObject);
    }
    
    /**
     * Returns all screen configurations.
     *
     * @return All screen configurations.
     * @throws ConfigException if an error is encountered
     */    
	public static HashMap<Integer, ScreenObject> getScreens() {  
	    return mScreens;
	}

    /**
     * Remove a screen configuration.
     *
     * @param screenID  Screen ID of the screen configuration to remove.
     * @throws ConfigException if an error is encountered
     */    
    public static void removeScreen(Integer screenID) throws ConfigException {  
        mScreens.remove(screenID);
    }
    
    /**
     * Retrieve a screen configuration.
     *
     * @param screenID  Screen ID of the screen configuration to retrieve.
     * @return Configuration for a screen.
     * @throws ConfigException if an error is encountered
     */    
	public static ScreenObject getScreen(Integer screenID) throws ConfigException {  
        ScreenObject scrObj = (ScreenObject) mScreens.get(screenID);
        if (scrObj == null) {
            if (mScreens.containsKey(screenID) == false) {
                throw new ConfigException(mLocalizer.t("CFG503: Screen ID not found: {0}", screenID));
            }
        } 
        return scrObj;
	}
	
    /**
     * Retrieve the ID of the initial screen.
     *
     * @return The ID of the initial screen.
     * @throws ConfigException if an error is encountered
     */    
	public static Integer getInitialScreenID() {
	    return mInitialScreenID;
	}
	
    /**
     * Sets the ID of the initial screen.
     *
     * @param The ID of the initial screen.
     * @throws ConfigException if an error is encountered
     */    
	public static void setInitialScreenID(Integer initialScreenID) {
	    mInitialScreenID = initialScreenID;
	}
	
	public String getMasterControllerJndi() {  //  return the jndi name for the master controller
	    return null;
	}

	
	// testing--raymond tam
	// RESUME HERE
    public static JndiResource getJndiResource(String id) {
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
