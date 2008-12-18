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
import com.sun.mdm.multidomain.parser.MultiDomainModel;
import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.parser.DomainRecordID;
import com.sun.mdm.multidomain.parser.DomainForWebManager;
import com.sun.mdm.multidomain.parser.DomainsForWebManager;
import com.sun.mdm.multidomain.parser.PageDefinition;
import com.sun.mdm.multidomain.parser.PageRelationType;
import com.sun.mdm.multidomain.parser.SimpleSearchType;
import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.ChildElementIterator;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.SearchOptions;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.WebDefinition;
import com.sun.mdm.multidomain.parser.ConfigurationFiles;
import com.sun.mdm.multidomain.parser.RelationshipProperty;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.core.context.JndiResource;
import com.sun.mdm.multidomain.services.security.util.NodeUtil;
import com.sun.mdm.multidomain.parser.JNDIResources;
import com.sun.mdm.multidomain.parser.RelationshipJDNIResources;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.services.model.Domain;
import com.sun.mdm.multidomain.services.model.ObjectDefinition;
import com.sun.mdm.multidomain.services.core.ObjectFactory;
import com.sun.mdm.multidomain.services.core.ObjectFactoryImpl;
import com.sun.mdm.multidomain.services.core.ObjectNodeFactoryImpl;
import com.sun.mdm.multidomain.services.core.ObjectFactoryRegistry;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Properties;
import java.io.InputStream;
import java.io.BufferedReader;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MDConfigManager {

    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.MDConfigManager");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    public static final String FIELD_DELIM = ".";
    public static final int DEFAULT_RECORD_SUMMARY_ID = 1;
    public static final int DEFAULT_RECORD_DETAILS_ID = 1;
    private static final String DOMAIN_CONFIG_DIR = "Domains";
    private static final String DOMAIN_CONFIG_FILE = "domain.xml";
    
    // basic node configuration constants 
        
    private static final String NODE_TAG = "node";
    private static final String NODE_NAME = "name";
    private static final String FIELD_NODE = "field";
    private static final String DISPLAY_NAME = "display-name";
    private static final String INPUT_MASK = "input-mask";
    private static final String VALUE_MASK = "value-mask";
    private static final String KEY_TYPE = "key-type";
    private static final String IS_SENSITIVE = "is-sensitive";
    private static final String NODE_DISPLAY_ORDER = "display-order";
    private static final String MAX_LENGTH = "max-length";
    private static final String GUI_TYPE = "gui-type";
    private static final String VALUE_LIST = "value-list";
    private static final String VALUE_TYPE = "value-type";
    private static final String MERGE_MUST_DELETE = "merge-must-delete";
    
    private static HashMap<Integer, ScreenObject> mScreens;
    private static HashMap<String, RelationshipScreenConfig> mRelationshipScreenConfigs;
    private static HashMap<String, DomainScreenConfig> mDomainScreenConfigs;       // Screen configurations for all domains
    private static HashMap<String, HashMap> mDomainObjNodeConfigMap;  // hashmap of domains and objNodeConfigMaps
    private static DocumentBuilder builder;
    private static String DEFAULT_MDWM_DATEFORMAT = "MM/dd/yyyy";    // default value
    private static String mDateFormat = null;
    private static MDConfigManager mInstance = null;	// instance
    private static Integer mInitialScreenID;	                // ID of the initial screen
    private static ObjectSensitivePlugIn mObjectSensitivePlugIn;
    private static MultiDomainWebManager mDWMInstance = null;
    private static MultiDomainModel mMDMInstance = null;
    private static Properties mJNDIProperties = null;
    private static boolean TBD = true;


    /** Constructor for MDConfigManager
     * 
     * @throws ConfigException if an error is encountered.
     */
    private MDConfigManager() throws ConfigException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG568: Could not initialize " +
                                                   "the DocumentBuilderFactory: {0}", 
                                                   e.getMessage()));
        }
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
                
                // read the MultiDomainWebManager configuration XML file
                mDWMInstance = getMultiDomainWebManager();
                
                mInstance = new MDConfigManager();
                mScreens = new HashMap<Integer, ScreenObject>();
                mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
                mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       
                mDomainObjNodeConfigMap = new HashMap<String, HashMap>();


                configureDomains();
                configureRelationships();
                configureHierarchies();
                configureGroups();
                configurePageDefinitions();

                // read the MultiDomainModel configuration XML file
                mMDMInstance = getMultiDomainModel();
                configureDateFormats();
                configureJNDIProperties();
                
                return mInstance;
            }
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG555: Could not initialize the " +
                                                   "MultiDomanConfigManager: {0}", 
                                                   e.getMessage()));
        }
    }  

    /**
     * Forces a re-initialization of the the MDConfigManager.  
     *
     * @return the initialized instance of the MDConfigManager
     * @throws ConfigException if an error is encountered
     */
    public static MDConfigManager reinitialize() throws ConfigException {  
        try {
            synchronized (MDConfigManager.class) {
                if (mInstance != null) {
                    return mInstance;
                }

                // read the MultiDomainWebManager configuration XML file
                mDWMInstance = getMultiDomainWebManager();
                
                mScreens = new HashMap<Integer, ScreenObject>();
                mRelationshipScreenConfigs = new HashMap<String, RelationshipScreenConfig>();
                mDomainScreenConfigs = new HashMap<String, DomainScreenConfig>();       

                configureDomains();
                configureRelationships();
                configureHierarchies();
                configureGroups();
                configurePageDefinitions();

                // read the MultiDomainModel configuration XML file
                mMDMInstance = getMultiDomainModel();
                configureDateFormats();
                configureJNDIProperties();
                return mInstance;
            }
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG556: Could not re-initialize the " +
                                                   "MultiDomanConfigManager: {0}", 
                                                   e.getMessage()));
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
        
    /** Parses MultiDomainModel.xml.
     *
     * @throws ConfigException if an exception is encountered.
     * @return the MultiDomainModel
     */    
    public static MultiDomainModel getMultiDomainModel() throws ConfigException {
        
        InputStream in;
        String configFileName = ConfigurationFiles.MULTI_DOMAIN_MODEL_XML;
        try {
            in = MDConfigManager.class.getClassLoader().getResourceAsStream(configFileName);
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG573: Could not open the " +
                                                   "MultiDomanModel configuration " +
                                                   "file ({0}): {1}", 
                                                   configFileName, 
                                                   e.getMessage()));
        }
        try {
            InputSource source = new InputSource(in);
            return (Utils.parseMultiDomainModel(source));
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG552: Could not parse the " +
                                                   "MultiDomanWebManager configuration " +
                                                   "file ({0}): {1}", 
                                                   configFileName, 
                                                   e.getMessage()));
        }
    }


    /** Parses MultiDomainWebManager.xml.
     *
     * @throws ConfigException if an exception is encountered.
     * @return the MultiDomainWebManager
     */    
    public static MultiDomainWebManager getMultiDomainWebManager() throws ConfigException {
        
        InputStream in;
        String configFileName = ConfigurationFiles.RELATIONSHIP_WEB_MANAGER_XML;
        try {
            in = MDConfigManager.class.getClassLoader().getResourceAsStream(configFileName);
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG551: Could not open the " +
                                                   "MultiDomanWebManager configuration " +
                                                   "file ({0}): {1}", 
                                                   configFileName, 
                                                   e.getMessage()));
        }
        try {
            InputSource source = new InputSource(in);
            return (Utils.parseMultiDomainWebManager(source));
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG552: Could not parse the " +
                                                   "MultiDomanWebManager configuration " +
                                                   "file ({0}): {1}", 
                                                   configFileName, 
                                                   e.getMessage()));
        }
    }

    /**
     * Configures the domains by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureDomains() throws ConfigException {
        DomainsForWebManager dsFWM = mDWMInstance.getDomains();
        
        ArrayList<DomainForWebManager> domains = dsFWM.getDomains();
        for (DomainForWebManager domainFWM : domains) {
            DomainScreenConfig domainScreenConfig = new DomainScreenConfig();
            
            String domainName = domainFWM.getDomainName();
            com.sun.mdm.multidomain.services.model.Domain domain =
                    new com.sun.mdm.multidomain.services.model.Domain(domainName);
            domainScreenConfig.setDomain(domain);

            // read the nodes from domain_midm.xml
            convertDomainMIDMConfig(domainName);
            SummaryLabel summaryLabel = convertSummaryLabel(domainName, domainFWM.getRecordID());
            domainScreenConfig.setSummaryLabel(summaryLabel);

            ArrayList<SearchScreenConfig> sSC = 
                     convertSearchType(domainName, domainFWM.getSearchType());
            domainScreenConfig.setSearchScreenConfigs(sSC);
            
            ArrayList<SearchResultsConfig> sRC = 
                    convertSearchDetail(domainName, domainFWM.getSearchDetail());
            domainScreenConfig.setSearchResultsConfigs(sRC);

            ArrayList<SearchResultsSummaryConfig> sRSC = 
                    convertSearchResultsSummary(domainName, domainFWM.getRecordSummaryFields());
            domainScreenConfig.setSearchResultsSummaryConfigs(sRSC);
            
            ArrayList<SearchResultDetailsConfig> sRDC = 
                    convertRecordDetailList(domainName, domainFWM.getRecordDetailList());
            domainScreenConfig.setSearchResultDetailsConfigs(sRDC);

            mInstance.addDomainScreenConfig(domainScreenConfig);
        }
    }
    
    /**
     * Read and parse the node configuration portion of the MIDM.xml file
     * for a specific domain.
     *
     * @param domainName  Domain name.
     * @throws ConfigException if an error is encountered.
     */
    private static void convertDomainMIDMConfig(String domainName) throws ConfigException {   
        
        if (domainName == null || domainName.length() == 0) {
            throw new ConfigException(mLocalizer.t("CFG557: The domain name " +
                                                   "cannot be null or zero length."));
        }
        
        // open the copied portion of the MIDM.xml file for this domain.
        
        String fileName = DOMAIN_CONFIG_DIR + "/" + domainName + "/" + DOMAIN_CONFIG_FILE;
        InputStream input;
        try {
            input = MDConfigManager.class.getClassLoader().getResourceAsStream(fileName);
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG558: Could not open file {0}: {1}", 
                                fileName, e.getMessage()));
        }
        
        try {
            Document doc = builder.parse(input);
            Element root = doc.getDocumentElement();

            ChildElementIterator itr = new ChildElementIterator(root);
            HashMap<String, ObjectNodeConfig> objNodeConfigMap = new HashMap<String, ObjectNodeConfig>();

            while ( itr.hasNext() ) {
                Element element = (Element)itr.next();

                if (element.getTagName().startsWith(NODE_TAG)) {
                  try {
                    ObjectNodeConfig objNodeConfig = buildObjectNodeConfig(element, domainName);

                    // build a node
                    objNodeConfigMap.put(objNodeConfig.getName(), objNodeConfig);
                  } catch (Exception ex) {
                      throw new Exception(mLocalizer.t("CFG559: Error occurred in node definition: {0}", 
                                                        ex.getMessage()));
                  }
                } 
            }
            mDomainObjNodeConfigMap.put(domainName, objNodeConfigMap);
            input.close();
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG569: Could not parse the " +
                                                   "MIDM configuration for this domain ({0}): {1}", 
                                                   domainName, e.getMessage()));
        }
            
    }

    /**
     * Builds the object node configuration
     *
     * @param element The XML element to processes.
     * @param domainName  The domain name.
     * @return An object representing the configuration for a domain.
     * @throws ConfigException if an error is encountered.
     */
    
    private static ObjectNodeConfig buildObjectNodeConfig(Element element, String domainName) throws ConfigException {

        String objName;
        String attr;
        String attr1;
        try {
            objName = NodeUtil.getChildNodeText(element, NODE_NAME);
            attr = NodeUtil.getChildNodeText(element, NODE_DISPLAY_ORDER);
            attr1 = NodeUtil.getChildNodeText(element, MERGE_MUST_DELETE);
        }  catch (Exception e)   {
            throw new ConfigException(mLocalizer.t("CFG570: Could not build the object " +
                                                   "node configuration: {0}", 
                                                   e.getMessage()));
        }
        int order = 0;
        boolean mustDelete = false;
        if (attr != null && !attr.equals("")) {
            order = Integer.parseInt(attr);
        }
        if (attr1 != null && attr1.equalsIgnoreCase("true") ) {
            mustDelete = true ;
        }

        // skip "node-" and get to the node name
        ObjectNodeConfig objNodeConfig = new ObjectNodeConfig(objName);
        objNodeConfig.setDisplayOrder(order);
        objNodeConfig.setMustDelete(mustDelete);

        
        ChildElementIterator itr = new ChildElementIterator(element);
        while ( itr.hasNext() ) {
            Element e = (Element)itr.next();

            // process the fields

            if (e.getTagName().equalsIgnoreCase(FIELD_NODE)) {
                String fieldName = null;
                try {

                    fieldName = NodeUtil.getChildNodeText(e, NODE_NAME); 
                    String displayName = NodeUtil.getChildNodeText(e, DISPLAY_NAME);
                    String inputMask = NodeUtil.getChildNodeText(e, INPUT_MASK);
                    String valueMask = NodeUtil.getChildNodeText(e, VALUE_MASK);
                    boolean keyType = false;
                    String keyTypeString = NodeUtil.getChildNodeText(e, KEY_TYPE);
                    if ((keyTypeString != null) && keyTypeString.equals("true")) {
                        keyType = true;
                    }

                    boolean isSensitive = false;
                    String isSensitiveString = NodeUtil.getChildNodeText(e, IS_SENSITIVE);
                    if ((isSensitiveString != null) && isSensitiveString.equals("true")) {
                        isSensitive = true;
                    }

                    // optional field
                    String displayOrder = "0";
                    String displayOrderString = NodeUtil.getChildNodeText(e, NODE_DISPLAY_ORDER);
                    if (displayOrderString != null) {
                        displayOrder = displayOrderString;
                    }
                    int idisplayOrder = Integer.parseInt(displayOrder);

                    String maxLength = "32";
                    String maxLengthString = NodeUtil.getChildNodeText(e, MAX_LENGTH);
                    // optional field
                    if (maxLengthString != null) {
                        maxLength = maxLengthString;
                    }

                    int imaxLength = Integer.parseInt(maxLength);

                    if (inputMask != null) {
                        imaxLength = inputMask.length();
                    }

                    String guiType = NodeUtil.getChildNodeText(e, GUI_TYPE);

                    String valueList = null;            
                    String valueListString = NodeUtil.getChildNodeText(e, VALUE_LIST);

                    if (valueListString != null) {
                        valueList = valueListString;
                    }

                    // optional field
                    String valueTypeStr = "String";
                    String valueTypeString = NodeUtil.getChildNodeText(e, VALUE_TYPE);
                    if (valueTypeString != null) {
                        valueTypeStr = valueTypeString;

                        // if value type is date set the date input mask automatically, based on date format
                        if (valueTypeStr.equals("date")) {
                            inputMask = getDateInputMask(domainName);
                        }
                    }

                    int valueType = getMetaType(valueTypeStr);
                    FieldConfig fieldConfig = new FieldConfig(domainName, objName, fieldName,
                            displayName, guiType, imaxLength, valueType);
                    fieldConfig.setDisplayOrder(idisplayOrder);
                    fieldConfig.setKeyType(keyType);
                    fieldConfig.setSensitive(isSensitive);
                    fieldConfig.setValueList(valueList);
                    fieldConfig.setInputMask(inputMask);

                    if (valueMask != null) {
                      if (inputMask == null || valueMask.length() != inputMask.length()) {
                        throw new ConfigException(mLocalizer.t("CFG560: Invalid value mask [{0}] for input mask [{1}]", valueMask, inputMask));
                      }
                      fieldConfig.setValueMask(valueMask);
                    }

                    objNodeConfig.addFieldConfig(fieldConfig);

                } catch (Exception ex) {
                    throw new ConfigException(mLocalizer.t("CFG561: Error occurred while building object node config for field name = {0}", fieldName));
                }
            }
        }

        return objNodeConfig;
    }
    
    /**
     * Converts the Summary Label information retrieved from the information from the parser.
     *
     * @param domainName  Domain name.
     * @param domainRecordID  Record ID for the domain.
     * @throws ConfigException if an error is encountered
     */
     
    private static SummaryLabel convertSummaryLabel(String domainName, DomainRecordID domainRecordID ) 
            throws ConfigException {
        
        if (domainRecordID == null || domainName == null || domainName.length() == 0) {
            return null;
        }
        boolean showEUID = domainRecordID.isMShowEUID();
        FieldGroup fg = domainRecordID.getFieldGroup();
        ArrayList<FieldConfig> fieldConfigs = createFieldConfig(domainName, fg.getFieldRefs());
        SummaryLabel summaryLabel = new SummaryLabel(showEUID, fieldConfigs);
        return summaryLabel;
    }

    /**
     * Converts the Search Screen information retrieved from the information from the parser.
     *
     * @param domainName  Domain name.
     * @param sST  ArrayList of SimpleSearchType objects containing the search configurations.
     * @return ArrayList of SearchScreenConfig objects that define the search configurations.
     * @throws ConfigException if an error is encountered
     */
     
    private static ArrayList<SearchScreenConfig> convertSearchType(String domainName, 
                                                                   ArrayList<SimpleSearchType> sST) 
            throws ConfigException {
        if (sST == null || sST.size() == 0 || domainName == null || domainName.length() == 0) {
            return null;
        }

        ArrayList<SearchScreenConfig> searchScreenConfigs = 
                new ArrayList<SearchScreenConfig>();
        for (SimpleSearchType sSTObj : sST) {
            
            String screenTitle = sSTObj.getScreenTitle();
            String instruction = sSTObj.getInstruction();
            int searchResultID = sSTObj.getScreenResultID();
            int screenOrder = sSTObj.getScreenOrder();
            boolean showEUID = false;
            boolean showLID = false;
            ArrayList<FieldGroup> sSTObjObjFieldGroup = sSTObj.getFieldGroups();
            SearchOptions sSTObjSearchOpt = sSTObj.getSearchOption();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(domainName, sSTObjObjFieldGroup);
            SearchScreenOptions options  = convertSearchOptions(sSTObjSearchOpt);
            
            // retrieve the root object
            HashMap<String, ObjectNodeConfig> objNodeConfigs = null;
            objNodeConfigs = mDomainObjNodeConfigMap.get(domainName);
            
            if (objNodeConfigs == null) {
                throw new ConfigException(mLocalizer.t("CFG562: Could not retrieve the " +
                                                       "object node configurations for " +
                                                       "this domain ({0})", 
                                                       domainName));
            }
            try {
                ObjectNodeConfig rootObj = objNodeConfigs.get(domainName);

                SearchScreenConfig sSCObj = new SearchScreenConfig(rootObj, screenTitle, 
                                                                   instruction, searchResultID, 
                                                                   screenOrder, showEUID, 
                                                                   showLID, options, 
                                                                   fieldConfigGroups);
                searchScreenConfigs.add(sSCObj);
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG574: Could not convert the  " +
                                                       "search type for " +
                                                       "this domain ({0}): {1}", 
                                                       domainName, e.getMessage()));
            }
        }
        return searchScreenConfigs;
    }

    /**
     * Converts an ArrayList of FieldGroup objects to an ArrayList of FieldConfigGroup 
     * objects.  The FieldConfigGroup objects are retrieved from the parser.  Domain name
     * may be null if this is invoked from the page definition handling code.
     *
     * @param domainName  Domain name.
     * @param fieldGroups An ArrayList of FieldGroup objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<FieldConfigGroup> convertFieldGroups(String domainName, ArrayList<FieldGroup> fieldGroups) 
            throws ConfigException {
        if (fieldGroups == null || fieldGroups.size() == 0) {
            return null;
        }
        ArrayList<FieldConfigGroup> fieldConfigGroups = new ArrayList<FieldConfigGroup>();
        
        for (FieldGroup fg : fieldGroups) {
            FieldConfigGroup fcg = convertFieldGroup(domainName, fg);
            fieldConfigGroups.add(fcg);
        }
        return fieldConfigGroups;
    }

    /**
     * Converts a FieldGroup object to a FieldConfigGroup object
     *
     * @param domainName  Domain name.
     * @param fieldGroups A FieldGroup objects to convert.
     * @throws ConfigException if an error is encountered
     */    
     
    private static FieldConfigGroup convertFieldGroup(String domainName, FieldGroup fieldGroup) 
            throws ConfigException {
        if (fieldGroup == null) {
            return null;
        }
        String description = fieldGroup.getDescription();
        
        // pass field refs
        ArrayList<FieldConfig> fieldConfigs = createFieldConfig(domainName, fieldGroup.getFieldRefs());
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
     * Domain name may be null if this is invoked from the page definition handling code.
     *
     * @param domainName  Domain name.
     * @param fieldRefs An ArrayList of FieldCodeRef objects to convert.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<FieldConfig> createFieldConfig(String domainName, ArrayList<FieldGroup.FieldRef> fieldRefs) throws ConfigException {
        if (fieldRefs == null) { 
            return null;
        }
        ArrayList<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();
        for (FieldGroup.FieldRef field : fieldRefs) {
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
                  
            FieldConfig fieldConfig = new FieldConfig(domainName, objRef, name, 
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
        
        String valueTypeStr = "String";
        if (relFieldRef.getValueType() != null) {
            valueTypeStr = relFieldRef.getValueType();

            // if value type is date set the date input mask automatically, based on date format
            if (valueTypeStr.equals("date")) {
                inputMask = getDateInputMaskForMultiDomain();
            }
        }

        int valueType = getMetaType(valueTypeStr);
        
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
     * @param domainName  Domain name.
     * @param searchOptions A SearchOption instance to convert.
     * @return ArrayList of SearchResultsConfig objects containing the search option configuration.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<SearchResultsConfig> convertSearchDetail(String domainName, 
                                                                      ArrayList<SearchDetail> searchDetails) 
            throws ConfigException {
        if (searchDetails == null || searchDetails.size() == 0 || 
            domainName == null || domainName.length() == 0) {
            
            return null;
        }

        ArrayList<SearchResultsConfig> searchResultsConfigs = 
                new ArrayList<SearchResultsConfig>();
        for (SearchDetail searchDetail : searchDetails) {
            String displayName = searchDetail.getDisplayName();
            int searchResultsID = searchDetail.getSearchResultID();
            int searchResultsSummaryID = DEFAULT_RECORD_SUMMARY_ID;
            int searchResultDetailsID = searchDetail.getRecordDetailID();
            int pageSize = searchDetail.getItemPerPage();
            int maxRecords = searchDetail.getMaxResultSize();
            boolean showEUID = false;
            boolean showLID = false;
            ArrayList<FieldGroup> searchDetailsFieldGroup = searchDetail.getFieldGroups();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(domainName, searchDetailsFieldGroup);
            
            // retrieve the root object
            HashMap<String, ObjectNodeConfig> objNodeConfigs = null;
            objNodeConfigs = mDomainObjNodeConfigMap.get(domainName);
            if (objNodeConfigs == null) {
                throw new ConfigException(mLocalizer.t("CFG565: Could not retrieve the " +
                                                       "object node configurations for " +
                                                       "this domain ({0})", 
                                                       domainName));
            }
            try {
                ObjectNodeConfig rootObj = objNodeConfigs.get(domainName);
                SearchResultsConfig sRC = new SearchResultsConfig(rootObj, displayName, 
                                                   searchResultsID, searchResultsSummaryID, 
                                                   searchResultDetailsID, pageSize, 
                                                   maxRecords, showEUID, showLID, 
                                                   fieldConfigGroups);
                searchResultsConfigs.add(sRC);
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG544: Could not add the " +
                                                       "search result configuration " +
                                                       "named {0} and with " +
                                                       "searchResultsID = {1}: {2}", 
                                                       displayName, 
                                                       searchResultsID,
                                                       e.getMessage()));
            }
        }
        return searchResultsConfigs;
    }

    /**
     * Converts an ArrayList of Record Detail List objects to an ArrayList of Search 
     * Result Details Config objects.
     *
     * @param domainName  Domain name.
     * @param recordDetailList  List of RecordDetail objects containing the configuration
     * information for displaying the details of a record.
     * @return An ArrayList of SearchResultDetailsConfig containing the configuration
     * information for displaying the details of a record.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<SearchResultDetailsConfig> convertRecordDetailList(String domainName,
                                                                                ArrayList<RecordDetail> recordDetailList) 
            throws ConfigException {
    
        if (recordDetailList == null || recordDetailList.size() == 0 ||
            domainName == null || domainName.length() == 0) {
            return null;
        }
        
        ArrayList<SearchResultDetailsConfig> searchResultDetailsConfig
                                                         = new ArrayList<SearchResultDetailsConfig> ();
        for (RecordDetail recordDetail : recordDetailList) {
            
            int searchResultDetailID = recordDetail.getRecordDetailId();
            String displayName = recordDetail.getDisplayName();
            boolean showEUID = false;
            boolean showLID = false;
            
            ArrayList<FieldGroup> recordDetailsFieldGroup = recordDetail.getFieldGroups();
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(domainName, recordDetailsFieldGroup);
            
            // retrieve the root object
            HashMap<String, ObjectNodeConfig> objNodeConfigs = null;
            objNodeConfigs = mDomainObjNodeConfigMap.get(domainName);
            if (objNodeConfigs == null) {
                throw new ConfigException(mLocalizer.t("CFG567: Could not retrieve the " +
                                                       "object node configurations for " +
                                                       "this domain ({0})", 
                                                       domainName));
            }
            
            try {
                ObjectNodeConfig rootObj = objNodeConfigs.get(domainName);
                SearchResultDetailsConfig sRDC = 
                            new SearchResultDetailsConfig(rootObj, displayName, searchResultDetailID, 
                                                          showEUID, showLID, fieldConfigGroups);
                searchResultDetailsConfig.add(sRDC);
            } catch (Exception e) {
                throw new ConfigException(mLocalizer.t("CFG575: Could not add the " +
                                                       "search result details configuration " +
                                                       "named {1} and with " +
                                                       "searchResultDetailID = {1}: {2}", 
                                                       displayName, 
                                                       recordDetail.getRecordDetailId(), 
                                                       e.getMessage()));
            }
        }
        return searchResultDetailsConfig;
    }            


    /**
     * Converts an ArrayList of FieldGroup objects to an ArrayList of Search 
     * Result Summary Config objects.
     *
     * @param searchOptions A SearchOption instance to convert.
     * @param domainName  Domain name.
     * @return ArrayList of SearchResultsSummaryConfig objects that define the 
     * configuration for the summary of the search results.
     * @throws ConfigException if an error is encountered
     */    
    private static ArrayList<SearchResultsSummaryConfig> convertSearchResultsSummary(String domainName, 
                                                                                     ArrayList<FieldGroup> searchResultSummaries) 
            throws ConfigException {
    
        if (searchResultSummaries == null || searchResultSummaries.size() == 0 ||
            domainName == null || domainName.length() == 0) {
            return null;
        }

        // This ArrayList will have exactly one element
        ArrayList<SearchResultsSummaryConfig> searchResultsSummaryConfig
                                                         = new ArrayList<SearchResultsSummaryConfig> (1);
        ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(domainName, searchResultSummaries);
        
        boolean showEUID = false;
        boolean showLID = false;
        
        // retrieve the root object
        HashMap<String, ObjectNodeConfig> objNodeConfigs = null;
        objNodeConfigs = mDomainObjNodeConfigMap.get(domainName);
        if (objNodeConfigs == null) {
            throw new ConfigException(mLocalizer.t("CFG566: Could not retrieve the " +
                                                   "object node configurations for " +
                                                   "this domain ({0})", 
                                                   domainName));
        }
        try {
            ObjectNodeConfig rootObj = objNodeConfigs.get(domainName);
            SearchResultsSummaryConfig sRSC 
                    = new SearchResultsSummaryConfig(rootObj, DEFAULT_RECORD_SUMMARY_ID, 
                                                     DEFAULT_RECORD_DETAILS_ID,
                                                     showEUID,
                                                     showLID,
                                                     fieldConfigGroups);

            searchResultsSummaryConfig.add(sRSC);
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("CFG576: Could not add the " +
                                                   "search result summary configuration " +
                                                   "this domain ({0}): {1}", 
                                                   domainName, e.getMessage()));
        }
        return searchResultsSummaryConfig;
    }            

             
    /**
     * Configures the relationships by retrieving the information from the parser.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureRelationships() throws ConfigException {
        ArrayList<WebDefinition> relationshipTypes = mDWMInstance.getRelationshipTypes();
        for (WebDefinition relationshipType : relationshipTypes) {
            String sourceDomainName = relationshipType.getSourceDomain();
    	    String targetDomainName = relationshipType.getTargetDomain();
    	    RelationshipScreenConfig relationshipScreenConfig = null;
    	    if (!relationshipScreenConfigExists(sourceDomainName, targetDomainName)) {
                relationshipScreenConfig 
                        = new RelationshipScreenConfig(sourceDomainName, targetDomainName);
                 addRelationshipScreenConfig(relationshipScreenConfig);
            } else {
                relationshipScreenConfig = 
                        getRelationshipScreenConfig(sourceDomainName, targetDomainName);
            }
            String displayName = relationshipType.getDisplayName();
            String relationshipName = relationshipType.getName();
            
            ArrayList<RelationFieldReference> relTypePredefinedAttributes 
                    = relationshipType.getPredefinedFieldRefs();     	
            ArrayList<FieldConfig> predefinedAttributes 
                    = convertRelationFieldReferences(relTypePredefinedAttributes);
                    
            ArrayList<RelationFieldReference> relTypeExtendedAttributes 
                    = relationshipType.getExtendedRelFieldRefs();     	    
            ArrayList<FieldConfig> extendedAttributes 
                    = convertRelationFieldReferences(relTypeExtendedAttributes);
            Definition definition = mDWMInstance.getLinkType(relationshipName, 
                                                             sourceDomainName, 
                                                             targetDomainName);
            int id = -1;    // default value
            String relTypeId = definition.getRelTypeId();
            if (relTypeId != null && relTypeId.length() != 0 &&
                !relTypeId.equalsIgnoreCase("null")) {
                id = Integer.parseInt(relTypeId);
            }

            int direction = RelationshipDef.UNIDIRECTIONAL;     // default value
            String relDir = definition.getDirection();
            if (relDir != null && relDir.length() != 0 &&
                !relDir.equalsIgnoreCase("null")) {
                
                // direction differs slightly in the parser
                direction = Integer.parseInt(relDir) - 1;
            }
            RelationshipDef relDef = new RelationshipDef(relationshipName, 
                                                         sourceDomainName, 
                                                         targetDomainName,
                                                         direction,
                                                         id);
            
            RelationshipScreenConfigInstance rSCI 
                    = new RelationshipScreenConfigInstance(relDef,
                                                           relationshipName,
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
     * Configures the page definitions.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configurePageDefinitions() throws ConfigException {
        
        PageDefinition pageDef = mDWMInstance.getPageDefinitions();
        int initialScreenID = pageDef.getInitialScreenId();
        mInitialScreenID = initialScreenID;
        ArrayList<com.sun.mdm.multidomain.parser.ScreenDefinition> screenDefs = pageDef.getScreenDefs();
        
        for (com.sun.mdm.multidomain.parser.ScreenDefinition screenDef : screenDefs) {
            String viewPath = screenDef.getViewPath();
            String displayTitle = screenDef.getScreenTitle();
            int screenID = screenDef.getScreenId();
            int displayOrder = screenDef.getDisplayOrder();
            int pageSize = screenDef.getItemPerPage();
            int maxRecords = screenDef.getMaxItems();
            
            ArrayList<SearchScreenConfig> searchScreenConfigs = convertPageRelationTypes(screenDef.getPageRelationType());
            PageDefinition subPage = screenDef.getChildPage();
            int initialSubscreenID = subPage.getInitialScreenId();
            ArrayList<ScreenObject> subscreens = configurePageDefinitions(subPage);
    	    ScreenObject scrObj = new ScreenObject(screenID, displayTitle, viewPath,
                                                   displayOrder, initialSubscreenID, 
                                                   pageSize, maxRecords,
                                                   searchScreenConfigs,
                                                   subscreens);
            addScreen(scrObj);
        }

    }
    
    /**
     * Configures the date formats for the RelationshipWebManager.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureDateFormats() throws ConfigException {
        
        // Assign the date format.  If it is not found, the default format is used.
        mDateFormat = mMDMInstance.getDateFormat();
        if (mDateFormat == null) {
            mDateFormat = DEFAULT_MDWM_DATEFORMAT;
            mLogger.info(mLocalizer.x("CFG001: The date format is not defined in " + 
                                      "the MultiDomainModel configuration file. "  +
                                      "The date format is set to the default format: {0} ", 
                                      DEFAULT_MDWM_DATEFORMAT));
        }
    }
    
    /**
     * Configures the JNDI properties  for the RelationshipWebManager.
     *
     * @throws ConfigException if an error is encountered
     */
    private static void configureJNDIProperties() throws ConfigException {
        JNDIResources jndiResources = mDWMInstance.getJndiResources();
        List<RelationshipProperty> properties = jndiResources.getProperties();
        if (properties != null) {
            for (RelationshipProperty relationshipProp : properties) {
                String name =  relationshipProp.getPropertyName();
                String value = relationshipProp.getPropertyValue();
                if (mJNDIProperties == null) {
                    mJNDIProperties = new Properties();
                }
                mJNDIProperties.setProperty(name, value);
            }
        }
    }
    
    /**
     * Recursively configures the page definitions.
     * 
     * @param pageDef  Page definition to process.
     * @return an ArrayList of ScreenObjects for the subscreens.
     * @throws ConfigException if an error is encountered
     */
    private static ArrayList<ScreenObject> configurePageDefinitions(PageDefinition pageDef) 
            throws ConfigException {
        
        if (pageDef == null) {
            return null;
        }
        
        ArrayList<com.sun.mdm.multidomain.parser.ScreenDefinition> screenDefs = pageDef.getScreenDefs();
        ArrayList<ScreenObject> screenObjects = new ArrayList<ScreenObject> ();
        for (com.sun.mdm.multidomain.parser.ScreenDefinition screenDef : screenDefs) {
            String viewPath = screenDef.getViewPath();
            String displayTitle = screenDef.getScreenTitle();
            int screenID = screenDef.getScreenId();
            int displayOrder = screenDef.getDisplayOrder();
            int pageSize = screenDef.getItemPerPage();
            int maxRecords = screenDef.getMaxItems();
            
            ArrayList<SearchScreenConfig> searchScreenConfigs = convertPageRelationTypes(screenDef.getPageRelationType());
            PageDefinition subPage = screenDef.getChildPage();
            ArrayList<ScreenObject> subscreens = null;
            int initialSubscreenID = -1;
            if (subPage != null) {
                initialSubscreenID = subPage.getInitialScreenId();
                subscreens =  configurePageDefinitions(subPage);
            }
    	    ScreenObject scrObj = new ScreenObject(screenID, displayTitle, viewPath,
                                                   displayOrder, initialSubscreenID, 
                                                   pageSize, maxRecords,
                                                   searchScreenConfigs,
                                                   subscreens);
            screenObjects.add(scrObj);
        }
        return screenObjects;
    }
    
    /**
     * Converts an ArrayList of PageRelationType instances to an ArrayList
     * of SearchScreenConfig instances.
     * 
     * @param pageDef  Page definition to process.
     * @return an ArrayList of SearchScreenConfig instances for the subscreens.
     * @throws ConfigException if an error is encountered
     */
    private static ArrayList<SearchScreenConfig> convertPageRelationTypes(ArrayList<PageRelationType> pageRelationTypes) 
            throws ConfigException {
        
        if (pageRelationTypes == null || pageRelationTypes.size() == 0) {
            return null;
        }
        ArrayList<SearchScreenConfig> searchScreenConfigs = new ArrayList<SearchScreenConfig>();
        for (PageRelationType pageRelType : pageRelationTypes) {
            // RESUME HERE
            // clean this up
            ObjectNodeConfig rootObj = null;
            String screenTitle = null;
            String instruction = null;
            int searchResultID = 0;
            int screenOrder = 0;
            boolean showEUID = false;
            boolean showLID = false;
            boolean showCreateDate = false;
            boolean showCreateTime = false;
            boolean showStatus = false;
            SearchScreenOptions options = null;
            ArrayList<FieldConfigGroup> fieldConfigGroups = convertFieldGroups(null, pageRelType.getFieldGroups());
            SearchScreenConfig sSC = new SearchScreenConfig(rootObj, screenTitle, 
                                                            instruction, searchResultID, 
                                                            screenOrder, showEUID, 
                                                            showLID, showCreateDate, 
                                                            showCreateTime, showStatus, 
                                                            options, fieldConfigGroups);
            searchScreenConfigs.add(sSC);
        }
        return searchScreenConfigs;
    }
    
    /**
     * Retrieve all domain screen configurations from the DomainScreenConfig hashmap.
     *
     * @return  all the domain screen configurations
     */
    public static HashMap<String, DomainScreenConfig> getDomainScreenConfigs() {
        return mDomainScreenConfigs;
    } 
    
    /**
     * Retrieve all domain configurations from the DomainScreenConfig hashmap.
     *
     * @return  all the domain configurations
     */
    public static List<Domain> getDomains() {
        ArrayList<Domain> domains = new ArrayList<Domain>();
        Collection<DomainScreenConfig> domainScreenConfigs = mDomainScreenConfigs.values();
        for(DomainScreenConfig dsc : domainScreenConfigs) {
            Domain domain = dsc.getDomain();
            domains.add(domain);
        }
        return domains;
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
                        getRelationshipScreenConfig(sourceDomainName, 
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
    public static RelationshipScreenConfig getRelationshipScreenConfig(String sourceDomainName, String targetDomainName) 
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
	            getRelationshipScreenConfig(sourceDomainName, targetDomainName);
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
	    RelationshipScreenConfig rSC = getRelationshipScreenConfig(sourceDomainName, targetDomainName);
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
	public static HashMap<String, RelationshipScreenConfig> getRelationshipScreenConfigs() {  
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
	    RelationshipScreenConfig rSC = getRelationshipScreenConfig(sourceDomainName, targetDomainName);
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
                                                 "named \"{0}\" for the domain named \"{1}\": {2}", 
                                                 hierarchyName, domainName, e.getMessage()));
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
                                                 "named \"{0}\" for the domain named \"{1}\":{2}", 
                                                 hSC.getDisplayName(), domainName, e.getMessage()));
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
                                                 "named \"{0}\" for the domain named \"{1}\":{2}", 
                                                 hierarchyName, domainName, e.getMessage()));
        }
    }

    /** 
     * Retrieve the ObjectNodeConfig hashmap for a domain.  
     * 
     * @return The ObjectNodeConfig hashmap for a domain.
     */
    public static HashMap<String, ObjectNodeConfig> getObjectNodeConfig(String domainName) {
        if (domainName == null || domainName.length() == 0) {
            return null;
        }
        return (mDomainObjNodeConfigMap.get(domainName));
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
     * @param initialScreenID  The ID of the initial screen.
     * @throws ConfigException if an error is encountered
     */    
	public static void setInitialScreenID(Integer initialScreenID) {
	    mInitialScreenID = initialScreenID;
	}
	
    /**
     * Get JndiResource for the given resource id.
     * @param id Identifier of JndiResource.
     * @return JndiResource
     */    
    public static JndiResource getJndiResource(String id) 
        throws ConfigException {
        JndiResource jndiResource = null;
        if (id == null) {
            throw new ConfigException(mLocalizer.t("CFG600: Invalid JndiResource id:null"));
        }
        JNDIResources jndiResources = mDWMInstance.getJndiResources();
        List<RelationshipJDNIResources> rJndiResources = jndiResources.getJDNIResources();
        for (RelationshipJDNIResources rJndiResource : rJndiResources) {
            if (id.equals(rJndiResource.getID())) { 
                jndiResource = new JndiResource();
                jndiResource.setId(rJndiResource.getID());
                jndiResource.setName(rJndiResource.getJNDIName());
                jndiResource.setType(rJndiResource.getResourceType());
                jndiResource.setDescription(rJndiResource.getDescription());
                break;  
            } else if (id.equals(rJndiResource.getID())) {
                jndiResource = new JndiResource();
                jndiResource.setId(rJndiResource.getID());
                jndiResource.setName(rJndiResource.getJNDIName());
                jndiResource.setType(rJndiResource.getResourceType());
                jndiResource.setDescription(rJndiResource.getDescription());
                break;                  
            }
        }
        if (jndiResource == null) {
            throw new ConfigException(mLocalizer.t("CFG601: Failed to find JndiResource for the given id:{0}", id));
        }
        return jndiResource;
    }

    /**
     * Retrieves the JNDI Properties
     * 
     * @return JNDI Properties.
     */    
    public static Properties getJndiProperties() {
        return mJNDIProperties;
    }

    /**
     * Sets the security plug-in.
     *
     * @param plugIn  Security plug-in.
     */    
    public static void setObjectSensitivePlugIn(ObjectSensitivePlugIn plugIn) {  
        mObjectSensitivePlugIn = plugIn;
    }

    /**
     * Retrieves the security plug-in.
     *
     * @return  The security plug-in.
     */    
    public static ObjectSensitivePlugIn getObjectSensitivePlugIn() {  
        return mObjectSensitivePlugIn;
    }
    
   /**
    * Gets date format for multi-domain. 
    * @return the date format defined in multi-domain definition.
    */
    public static String getDateFormatForMultiDomain() {
        return mDateFormat;
    }

   /**
    * Gets date format for a specific domain.
    * 
    * @param domainName  Name of the domain.
    * @return the date format defined in object definition.
    * @throws ConfigException if an error is encountered.
    */    
    public static String getDateFormatForDomain(String domainName) 
            throws ConfigException {
        if (domainName == null || domainName.length() == 0) {
            throw new ConfigException(mLocalizer.t("CFG571: The domain name " +
                                                   "cannot be null or zero length."));
        }
        ObjectNodeFactoryImpl objectNodeFactory = (ObjectNodeFactoryImpl) ObjectFactoryRegistry.lookup(domainName);
        ObjectDefinition objDef = objectNodeFactory.getObjectDefinition();
        return (objDef.getDateFormat());
    }
    
    // For relationship date attribute  input mask,  to be simplified, should be one input mask and value mask for all predefined and extended date attributes. 
    public static String getDateInputMaskForMultiDomain() {
        String dateFormat = mDateFormat;
        if (dateFormat == null || dateFormat.length() == 0) {
            dateFormat = DEFAULT_MDWM_DATEFORMAT;
            mLogger.info(mLocalizer.x("CFG002: The date format is not defined in " + 
                                      "the MultiDomainModel configuration file. "  +
                                      "The date format is set to the default format: {0} ", 
                                      DEFAULT_MDWM_DATEFORMAT));
        }
        return (getDateInputMask(dateFormat));
    }
    
    // testing--raymond tam
    // is this needed?
    public static String getDateInputMaskForDomain(String domainName) 
            throws ConfigException {
        if (domainName == null || domainName.length() == 0) {
            throw new ConfigException(mLocalizer.t("CFG572: The name of the domain " +
                                                   "cannot be null or zero length."));
        }
        String dateFormat = getDateFormatForDomain(domainName);
        return (getDateInputMask(dateFormat));
    }
    
    /**
     * Gets the input mask of date in the MDConfigManager class
     *
     * @param dateFormat  Date format.
     * @return the input mask of date based on the date format
     */
    public static String getDateInputMask(String dateFormat) {
        if (dateFormat == null || dateFormat.length() == 0) {
            return null;
        }
        String format = dateFormat;
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
    
    private static int getMetaType(String valueTypeStr) {
        if (valueTypeStr.equalsIgnoreCase("Int")) {
            return ObjectField.OBJECTMETA_INT_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Boolean")) {
            return ObjectField.OBJECTMETA_BOOL_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("String")) {
            return ObjectField.OBJECTMETA_STRING_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Byte")) {
            return ObjectField.OBJECTMETA_BYTE_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Character")) {
            return ObjectField.OBJECTMETA_CHAR_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Long")) {
            return ObjectField.OBJECTMETA_LONG_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Blob")) {
            return ObjectField.OBJECTMETA_BLOB_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Date")) {
            return ObjectField.OBJECTMETA_DATE_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Float")) {
            return ObjectField.OBJECTMETA_FLOAT_TYPE;
        }

        if (valueTypeStr.equalsIgnoreCase("Time")) {
            return ObjectField.OBJECTMETA_TIMESTAMP_TYPE;
        }

        return ObjectField.OBJECTMETA_UNDEFINED_TYPE;
    }
    
    /**
     * Retrieve the name of the root object for a domain from an ArrayList of
     * FieldConfigGroup instances. 
     * 
     * @param fieldConfigGroups  ArrayList of FieldConfigGroup instances.
     * @returns  The name of the root object.  This will be in every FieldConfig object.
     * @throws ConfigException if an error is encountered.
     */
    private static String retrieveRootObjectName(ArrayList<FieldConfigGroup> fieldConfigGroups) 
            throws ConfigException {
        if (fieldConfigGroups == null || fieldConfigGroups.size() == 0) {
            throw new ConfigException(mLocalizer.t("CFG563: Could not retrieve the " +
                                                   "root object name because the fieldConfigGroups " +
                                                   "parameter is either null or an empty List."));
        }
        FieldConfigGroup fcg = fieldConfigGroups.get(0);
        ArrayList<FieldConfig> fieldConfigs = fcg.getFieldConfigs();
        if (fieldConfigs == null || fieldConfigs.size() == 0) {
            throw new ConfigException(mLocalizer.t("CFG564: Could not retrieve the " +
                                                   "root object name because the field configurations " +
                                                   "are either null or empty."));
        }
        FieldConfig fc = fieldConfigs.get(0);
        return (fc.getRootObj());
    }
    
}
