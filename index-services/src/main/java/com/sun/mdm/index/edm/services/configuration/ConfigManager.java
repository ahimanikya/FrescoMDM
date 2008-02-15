/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)ConfigManager.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.objects.metadata.MetaDataService;

import com.sun.mdm.index.edm.services.configuration.util.ScreenUtil;
import com.sun.mdm.index.edm.services.configuration.util.TransactionUtil;
import com.sun.mdm.index.edm.services.configuration.util.AssumedMatchesUtil;
import com.sun.mdm.index.edm.services.configuration.util.AuditLogUtil;
import com.sun.mdm.index.edm.services.configuration.util.ReportUtil;
import com.sun.mdm.index.edm.services.configuration.util.NodeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.mdm.index.util.ObjectSensitivePlugIn;
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * Read EDM config xml
 *
 * @author rtam
 * @created August 8, 2007
 */
public class ConfigManager implements java.io.Serializable {
    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.service.configuration.ConfigManager");
    private static transient final Localizer mLocalizer = Localizer.get();
    public static final String CONFIG_FILENAME = "edm.xml";
    public static final String FIELD_DELIM = ".";
    
    public static final String DEFAULT_LID_NAME = "Local ID";
    public static final String DEFAULT_LID_HEADER_NAME = "Local ID";
    public static final String LID = "local-id";
    public static final String LID_HEADER = "local-id-header";

    private static final String PATH_HEAD = "Enterprise.SystemObject.";
    
    private static final String DUPLICATE_RECORDS = "duplicate-records";
    private static final String RECORD_DETAILS = "record-details";
    private static final String ASSUMED_MATCHES = "assumed-matches";
    private static final String TRANSACTIONS = "transactions";
    private static final String REPORTS = "reports";
    public static final String REPORT_NAME = "report-name";
    private static final String SOURCE_RECORD = "source-record";
    private static final String AUDIT_LOG = "audit-log";
    
    private static final String DASHBOARD = "dashboard";
    
    private static final String TAB_NAME = "tab-name";
    private static final String TAB_ENTRANCE = "tab-entrance";
    private static final String INITIAL_SCREEN_ID = "initial-screen-id";
    private static final String ROOT_OBJ = "root-object";
    private static final String SHOW_LID = "show-lid";
    private static final String SHOW_EUID = "show-euid";
    private static final String SHOW_STATUS = "show-status";
    private static final String SHOW_CREATE_DATE = "show-create-date";
    private static final String SHOW_CREATE_TIME = "show-create-time";
    private static final String SHOW_SYSTEM_USER = "show-system-user";
    private static final String SHOW_OPERATION = "show-operation";
    private static final String SHOW_TIMESTAMP = "show-timestamp";
    private static final String SCREEN_ID = "screen-id";
    private static final String PAGE_SIZE = "item-per-page";
    private static final String MAX_RECORDS = "max-result-size";
    private static final String DISPLAY_ORDER = "display-order";
    private static final String FIELD_GROUP = "field-group";
    private static final String FIELD_REF = "field-ref";
    private static final String INSTRUCTION = "instruction";
    private static final String DESCRIPTION = "description";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_MAX_RESULTS_SIZE = "50";
    private static final String DEFAULT_INITIAL_SCREEN_ID = "0";
    private static final String INITIAL_SCREEN = "initial-screen";
    private static final String SCREEN_TITLE = "screen-title";
    private static final String SEARCH_PAGES = "search-pages";
    private static final String SEARCH_RESULT_PAGES = "search-result-pages";
    private static final String SEARCH_RESULT_LIST_PAGE = "search-result-list-page";
    private static final String SIMPLE_SEARCH_PAGE = "simple-search-page";
    private static final String ADVANCED_SEARCH_PAGE = "advanced-search-page";
    private static final String SEARCH_RESULT_ID = "search-result-id";
    private static final String SEARCH_SCREEN_ORDER = "search-screen-order";
    private static final String SUBSCREEN_CONFIGS = "subscreen-configurations";
    private static final String SUBSCREEN = "subscreen";
    
    private static String DATEFORMAT;

    // report types
    
    private static final String POTENTIAL_DUPLICATE_REPORT = "Potential Duplicate";
    private static final String DEACTIVATED_REPORT= "Deactivated";
    private static final String MERGED_REPORT = "Merged";
    private static final String UNMERGED_REPORT= "Unmerged";
    private static final String UPDATE_REPORT= "Update";
    private static final String ASSUMED_MATCH_REPORT= "Assumed Match";
    private static final String ACTIVITY_REPORT = "Transaction Summary Report";
   
    // ArrayList of Integers identifying ScreenObjects with EUID fields.  
    private ArrayList screensWithEUIDFields = new ArrayList();
    // key = object name, value = the associated ObjectNodeConfig object
    private HashMap objNodeConfigMap = new HashMap();
    // key = screen ID, value = associated ScreenObject
    private HashMap mScreenMap = new HashMap();
    // key = ID, value = associated EDM value
    private HashMap mConfigurableQwsValues = new HashMap();
    private static ConfigManager instance = null;
    private boolean auditLog = false;
    private DocumentBuilder builder;

    // misc fields under impl-details
    private String masterControllerJndi;
    private String validationServiceJndi;
    private String userCodeLookupJndi;
    private String reportGeneratorJndi;
    private boolean hasReportGeneratorJndi = false;
    private String debugFlag;
    private String debugDest;
    private String securityEnabled = null;
    private ObjectSensitivePlugIn security = null;
    
    
    // introduced new element which will be used while reading EDM.xml file.
    private Element pageDefElement = null;
    
    
    /**
     * Constructor for the ConfigManager object
     */
    private ConfigManager() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }


    /**
     * Return the name part of a full name e.g. Person.FirstName -> FirstName
     * @param val name/full name
     * @return fieldname
     */
    public static String getFieldName(String val) {
        int index = val.indexOf(FIELD_DELIM);

        if (index < 0) {
            return val;
        } else {
            return val.substring(index + 1);
        }
    }

    /**
     * Gets the instance attribute of the ConfigManager class
     * To be called before getInstance() is called.
     *
     * @return The instance value
     * @throws Exception if obtaining/initializing the instance failed
     */
    public static ConfigManager init() throws Exception {
        
        synchronized (ConfigManager.class) {
            if (instance != null) {
                return instance;
            }
            instance = new ConfigManager();
         
            ObjectFactory.init();
            String dateFormat = MetaDataService.getDateFormat();
            
            if (dateFormat == null || dateFormat.trim().length() == 0) {
                mLogger.info(mLocalizer.x("SRC001: The date format is not defined in object definition file."));
                DATEFORMAT = "MM/dd/yyyy";
            } else {
                if (isDateFormatValid(dateFormat)) {
                    DATEFORMAT = dateFormat;
                } else {
                    throw new Exception(mLocalizer.t("SRC500: Date format '{0}" + 
                            "' in object definition configuration is not supported.", dateFormat));
                }
            }
            mLogger.info(mLocalizer.x("SRC002: The date format is {0}", DATEFORMAT));
            
            try {
                instance.read();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRC501: Failed to read EDM configuration: {0}", e.getMessage()));
            }
            mLogger.info(mLocalizer.x("SRC003: Parsed EDM configuration file successfully."));
            return instance;
        }
    }
        
    /**
     * Gets the instance attribute of the ConfigManager class
     * To be called after getFirstInstance is called.
     *
     * @return The instance value
     */
    public static ConfigManager getInstance() {
      return instance;
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
    
    /**
     * The main program for the ConfigManager class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) throws Exception {
        ConfigManager.init();
    }


    /**
     * @todo Document: Getter for ObjectNodeConfig attribute of the
     *      ConfigManager object
     * @param objName the name of the object
     * @return object node config keyed by obj name
     */
    public ObjectNodeConfig getObjectNodeConfig(String objName) {
        return (ObjectNodeConfig) objNodeConfigMap.get(objName);
    }

    /**
     * Getter for all the root node names
     * @return root node names
     */
    public String[] getRootNodeNames() {
        ArrayList rootNodeNames = new ArrayList();
        
        Iterator iter = objNodeConfigMap.keySet().iterator();
        while (iter.hasNext()) {
            ObjectNodeConfig obj = (ObjectNodeConfig) objNodeConfigMap.get((String) iter.next());

            if (obj.isRootNode()) {
                rootNodeNames.add(obj.getName());
            }
        }
        
        return (String[]) rootNodeNames.toArray(new String[0]);
    }

    private int getMetaType(String valueTypeStr) {
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
     * Getter for the mScreenMap member.`
     *
     * @return  value of the mScreenMap mamber.
     */
    public HashMap getScreenMap() {
        return mScreenMap;
    }
    
    /**
     * Parses a configuration block and stores the configuration information.
     *
     * @param element  This is the element where the configuration block is located.
     * @param configType  This is the configuration type for the block.
     * @throws Exception if any errors are encountered.
     */
    private void buildConfigBlock(Element element, String configType) 
            throws Exception {
                
        String rootObjName = NodeUtil.getChildNodeText(element, ROOT_OBJ);
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) objNodeConfigMap.get(rootObjName);
        String title = NodeUtil.getChildNodeText(element, TAB_NAME);
        String entrance = NodeUtil.getChildNodeText(element, TAB_ENTRANCE);
        Integer screenID = Integer.valueOf(NodeUtil.getChildNodeText(element, SCREEN_ID));
        String displayOrder = NodeUtil.getChildNodeText(element, DISPLAY_ORDER);
        int pageSize = 0;
        int maxRecords = 0;
        int searchResultID = 0;
        int searchScreenOrder = 0;
        String description = new String();
        
        // Process all search pages.
        Element e = (Element)element.getElementsByTagName(SEARCH_PAGES).item(0);

        ChildElementIterator itr = new ChildElementIterator(e);

        ArrayList searchScreensConfig = new ArrayList();
        ArrayList searchResultsConfig = new ArrayList();
        while (itr.hasNext() ) {
            Element sscElement = (Element)itr.next();
        
            if (sscElement.getTagName().equalsIgnoreCase(SIMPLE_SEARCH_PAGE)) {
                SearchScreenConfig ssconfig 
                        = buildSearchScreenConfig(sscElement, 
                                                  objNodeConfig, 
                                                  configType);
                searchScreensConfig.add(ssconfig);
            }
        }        
        
        // Process search result list page
        Element searchResultsElement 
                = (Element)element.getElementsByTagName(SEARCH_RESULT_PAGES).item(0);
        
        itr = new ChildElementIterator(searchResultsElement);
        while (itr.hasNext() ) {
            Element srcElement = (Element)itr.next();
        
            if (srcElement.getTagName().equalsIgnoreCase(SEARCH_RESULT_LIST_PAGE)) {
                SearchResultsConfig srconfig 
                        = buildSearchResultsConfig(srcElement, 
                                                   objNodeConfig, 
                                                   configType);
                searchResultsConfig.add(srconfig);
            }
        }        
        ScreenObject scrObj = new ScreenObject(screenID, 
                                               title, 
                                               objNodeConfig, 
                                               Integer.parseInt(displayOrder),
                                               entrance,
                                               searchScreensConfig, 
                                               searchResultsConfig,
                                               null);
        
        // Check if the screenID has already been used.  If not, store in the
        // mScreenMap HashMap.  Otherwise, throw an exception
        if (mScreenMap.containsKey(screenID) == false) {
            mScreenMap.put(screenID, scrObj);
        } else {
            throw new Exception(mLocalizer.t("SRC502: Screen ID already in use: {0}", screenID));
        }
    }
    

    /** Returns the boolean value of the specified attribute.
     *
     * @param element  XML element where the attribute is located if
     * the attribute is present.
     * @param xmlTag  XML tag identifying the attribute.
     * @throws Exception if an error is encountered.
     * @returns  true if the attribute is set to true, false otherwise or 
     * if it has not been defined.
     */
    private boolean getAttributeBooleanValue(Element element, String xmlTag) 
            throws Exception {
        boolean val = false;
        if (xmlTag != null) {
            String valString = NodeUtil.getChildNodeText(element, xmlTag);
            if ((valString != null) && valString.equals("true")) {
                val = true;
            }
        }
        return val;
    }

    /** Returns ScreenObject identified by the screenID parameter.
     *
     * @param screenID identifying a ScreenObject.
     * @throws Exception if an error occurs
     */
    public ScreenObject getScreen(Integer screenID) throws Exception {
        ScreenObject scrObj = (ScreenObject) mScreenMap.get(screenID);
        if (scrObj == null) {
            if (mScreenMap.containsKey(screenID) == false) {
                throw new Exception(mLocalizer.t("SRC503: Screen ID not found: {0}", screenID));
            }
        } 
        return scrObj;
    }

    /** Returns ScreenObject representing the initial screen.
     *
     * @return  ScreenObject representing the initial screen.
     * @throws Exception if an error occurs
     */
    public ScreenObject getInitialScreen() throws Exception {
           
        if (mConfigurableQwsValues.containsKey(INITIAL_SCREEN_ID) == false) {
            throw new Exception(mLocalizer.t("SRC504: Error: initial screen ID has not been set."));
        } 
        Integer initialScreenID 
                = (Integer) mConfigurableQwsValues.get(INITIAL_SCREEN_ID);
        return getScreen(initialScreenID);
    }
    
    
    /**
     * Parses a block with subscreens and stores the configuration information.
     *
     * @param element  This is the element where the block is located.
     * @param configType  This is the configuration type for the block.
     * @throws Exception if any errors are encountered.
     */
    private void buildSubscreensBlock(Element element, String configType) 
            throws Exception {
                
        String rootObjName = NodeUtil.getChildNodeText(element, ROOT_OBJ);
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) objNodeConfigMap.get(rootObjName);
        String title = NodeUtil.getChildNodeText(element, TAB_NAME);
        String entrance = NodeUtil.getChildNodeText(element, TAB_ENTRANCE);
        Integer screenID = Integer.valueOf(NodeUtil.getChildNodeText(element, SCREEN_ID));
        String displayOrder = NodeUtil.getChildNodeText(element, DISPLAY_ORDER);

        // process subscreens
        Element sscElement = (Element)element.getElementsByTagName(SUBSCREEN_CONFIGS).item(0);
        String itemPerPage = NodeUtil.getChildNodeText(sscElement, PAGE_SIZE);
        ArrayList subscreensArrayList = new ArrayList();
        
        // iterate through all subscreen definitions
        ChildElementIterator itr = new ChildElementIterator(sscElement);
        while ( itr.hasNext() ) {        
            Element ssElement = (Element)itr.next();
        
            // Build ArrayList of subscreens.  If the subscreens have been 
            // disabled (for REPORTS), they are ignored.
            if (ssElement.getTagName().equalsIgnoreCase(SUBSCREEN)) {
                boolean enable = true;
                if (configType.compareTo(REPORTS) == 0) {
                    enable = getAttributeBooleanValue(ssElement, "enable");
                }
                if (enable == true) {
                    ScreenObject so = buildSubScreenConfigs(ssElement, configType);
                    subscreensArrayList.add(so);
                }
            }
        }
        ScreenObject scrObj = new ScreenObject(screenID, 
                                               title, 
                                               objNodeConfig, 
                                               Integer.parseInt(displayOrder),
                                               entrance,
                                               null, 
                                               null,
                                               subscreensArrayList);
        
        // Check if the screenID has already been used.  If not, store in the
        // mScreenMap HashMap.  Otherwise, throw an exception
        if (mScreenMap.containsKey(screenID) == false) {
            mScreenMap.put(screenID, scrObj);
        } else {
            throw new Exception(mLocalizer.t("SRC505: Screen ID already in use: {0}", screenID));
        }
    }

    /** Builds the subscreen configuration for a screen.  This section of the 
     * XML code is identified by the SUBSCREEN tag.  If a subscreen is disabled, 
     * it is ignored.
     *
     * @param element This is the XML element where the subscreen information
     * is located.
     * @param configType This is the type for the configuration block that
     * invoked this method.
     * @returns  a ScreenObject instance containing configuration information 
     * for the subscreen.
     */
    private ScreenObject buildSubScreenConfigs(Element element, String configType)
            throws Exception {
                
        String rootObjName = NodeUtil.getChildNodeText(element, ROOT_OBJ);
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) objNodeConfigMap.get(rootObjName);
        String title = NodeUtil.getChildNodeText(element, TAB_NAME);
        String entrance = NodeUtil.getChildNodeText(element, TAB_ENTRANCE);
        Integer screenID = Integer.valueOf(NodeUtil.getChildNodeText(element, SCREEN_ID));
        String displayOrder = NodeUtil.getChildNodeText(element, DISPLAY_ORDER);

        // Process all search pages.
        Element e = (Element)element.getElementsByTagName(SEARCH_PAGES).item(0);
        ChildElementIterator itr = new ChildElementIterator(e);

        ArrayList searchScreensConfig = new ArrayList();
        ArrayList searchResultsConfig = new ArrayList();
        // blockType defaults to the configType.  This will be changed later
        // for REPORTS, where the blockType is the specific type of report
        // that is being processed.
        String blockType = configType;
        while (itr.hasNext() ) {
            Element sscElement = (Element)itr.next();
        
            if (sscElement.getTagName().equalsIgnoreCase(SIMPLE_SEARCH_PAGE)) {
                // Change the configType to the actual name of the 
                // report for Reports.  Otherwise, leave it as is.
                if  (configType.compareTo(REPORTS) == 0) {
                    blockType = NodeUtil.getChildNodeText(element, REPORT_NAME);
                } 
                SearchScreenConfig ssconfig 
                        = buildSearchScreenConfig(sscElement, 
                                                  objNodeConfig,
                                                  blockType);
                searchScreensConfig.add(ssconfig);
            }
        }        
        
        // Process search result list page
        Element searchResultsElement 
                = (Element)element.getElementsByTagName(SEARCH_RESULT_PAGES).item(0);
        
        itr = new ChildElementIterator(searchResultsElement);
        while (itr.hasNext() ) {
            Element srcElement = (Element)itr.next();
            if (srcElement.getTagName().equalsIgnoreCase(SEARCH_RESULT_LIST_PAGE)) {
                SearchResultsConfig srconfig 
                        = buildSearchResultsConfig(srcElement, 
                                                   objNodeConfig,
                                                   blockType);
                searchResultsConfig.add(srconfig);
            }
        }        
        ScreenObject scrObj = new ScreenObject(screenID, 
                                               title, 
                                               objNodeConfig, 
                                               Integer.parseInt(displayOrder),
                                               entrance,
                                               searchScreensConfig, 
                                               searchResultsConfig,
                                               null);
        
        return scrObj;
    }
    
    /**
     * @return the audit log config obj
     */
    public boolean getAuditLogConfig() {
        return auditLog;
    }

    
    /**
     * Description of the Method
     *
     * @param element Description of the Parameter
     * @return Description of the Return Value
     */
    
    private ObjectNodeConfig buildObjectNodeConfig(Element element) throws IOException, Exception {

        String objName = element.getTagName().substring(5);

        String attr = element.getAttribute("display-order");
        String attr1 = element.getAttribute("merge-must-delete");
        int order = 0;
        boolean mustDelete=false;
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
            Element e;
            Element field = (Element)itr.next();

            String fieldName = field.getTagName().substring(6);

          try {

              // skip "field-" and get to the field name
              //String displayName = field.getChild("display-name").getText();
              String displayName = NodeUtil.getChildNodeText(field,"display-name");

              String inputMask = NodeUtil.getChildNodeText(field,"input-mask");
              
              String valueMask = NodeUtil.getChildNodeText(field,"value-mask");

                            
            boolean keyType = false;
            String keyTypeString = NodeUtil.getChildNodeText(field,"key-type");
            if ((keyTypeString != null) && keyTypeString.equals("true")) {
                keyType = true;
            }

            boolean isSensitive = false;
            String isSensitiveString = NodeUtil.getChildNodeText(field,"is-sensitive");
            if ((isSensitiveString != null) && isSensitiveString.equals("true")) {
                isSensitive = true;
            }

            // optional field
            String displayOrder = "0";
            String displayOrderString = NodeUtil.getChildNodeText(field,"display-order");
            if (displayOrderString != null) {
                displayOrder = displayOrderString;
            }
            int idisplayOrder = Integer.parseInt(displayOrder);
            
            String maxLength = "32";
            String maxLengthString = NodeUtil.getChildNodeText(field,"max-length");
            // optional field
            if (maxLengthString != null) {
                maxLength = maxLengthString;
            }

            int imaxLength = Integer.parseInt(maxLength);
            
            if (inputMask != null) {
                imaxLength = inputMask.length();
            }

            String guiType = NodeUtil.getChildNodeText(field,"gui-type");
            
            String valueList = null;            
            String valueListString = NodeUtil.getChildNodeText(field,"value-list");

            if (valueListString != null) {
                valueList = valueListString;
            }

            // optional field
            String valueTypeStr = "String";
            String valueTypeString = NodeUtil.getChildNodeText(field,"value-type");
            if (valueTypeString != null) {
                valueTypeStr = valueTypeString;
                
                // if value type is date set the date input mask automatically, based on date format
                if (valueTypeStr.equals("date")) {
                    inputMask = getDateInputMask();
                }
            }

            int valueType = getMetaType(valueTypeStr);
            FieldConfig fieldConfig = new FieldConfig(null, objName, fieldName,
                    displayName, guiType, imaxLength, valueType);
            fieldConfig.setDisplayOrder(idisplayOrder);
            fieldConfig.setKeyType(keyType);
            fieldConfig.setSensitive(isSensitive);
            fieldConfig.setValueList(valueList);
            fieldConfig.setInputMask(inputMask);

            if (valueMask != null) {
              if (inputMask == null || valueMask.length() != inputMask.length()) {
                throw new IOException(mLocalizer.t("SRC506: Invalid value mask [{0}] for input mask [{1}]", valueMask, inputMask));
              }
              fieldConfig.setValueMask(valueMask);
            }

            objNodeConfig.addFieldConfig(fieldConfig);
            
          } catch (Exception ex) {
            throw new Exception(mLocalizer.t("SRC513: Error occurred while building object node config for field name = {0}", fieldName));
          }
        }

        return objNodeConfig;
    }
    
    
    private void buildObjectNodeRelationship(Element element) throws Exception {
        // load object definition
        ObjectFactory.init();

        String objName = NodeUtil.getChildNodeText(element,"name");
        ObjectNodeConfig objNodeConfig = (ObjectNodeConfig) objNodeConfigMap.get(objName);
        
        // get required and updateable attributes from object definition
        FieldConfig[] fieldconfig = objNodeConfig.getFieldConfigs();
        for (int i = 0; i < fieldconfig.length; i++) {
            fieldconfig[i].setRequired(
                MetaDataService.isFieldRequired(PATH_HEAD + objName + "." + fieldconfig[i].getName()));
            fieldconfig[i].setUpdateable(
                MetaDataService.isFieldUpdateable(PATH_HEAD + objName + "." + fieldconfig[i].getName()));
            
            String userCode = MetaDataService.getUserCode(PATH_HEAD + objName + "." + fieldconfig[i].getName());
            if (userCode != null && userCode.length() > 0) {
                fieldconfig[i].setUserCode(userCode);
            }
            String constraintBy = MetaDataService.getConstraintBy(PATH_HEAD + objName + "." + fieldconfig[i].getName());
            if (constraintBy != null && constraintBy.length() > 0) {
                fieldconfig[i].setConstraintBy(constraintBy);
            }
        }

        ChildElementIterator itr = new ChildElementIterator(element,"children");
        while ( itr.hasNext() ) {
            Element childElement = (Element)itr.next();
            String childName = NodeUtil.getNodeText(childElement);
        
         try {
            ObjectNodeConfig childConfig = (ObjectNodeConfig) objNodeConfigMap.get(childName);
            
            // get required and updateable attributes from object definition
            fieldconfig = childConfig.getFieldConfigs();
            for (int i = 0; i < fieldconfig.length; i++) {
                fieldconfig[i].setRootObject(objName);
                fieldconfig[i].setRequired(MetaDataService.isFieldRequired(
                    PATH_HEAD + objName + "." + childName + "." + fieldconfig[i].getName()));
                // for child objects, don't allow updating key fields
                if (fieldconfig[i].isKeyType()) {
                    fieldconfig[i].setUpdateable(false);
                                                                } else {
                    fieldconfig[i].setUpdateable(MetaDataService.isFieldUpdateable(
                        PATH_HEAD + objName + "." + childName + "." + fieldconfig[i].getName()));
                                                                }
                
                String userCode = MetaDataService.getUserCode(
                    PATH_HEAD + objName + "." + childName + "." + fieldconfig[i].getName());
                if (userCode != null && userCode.length() > 0) {
                    fieldconfig[i].setUserCode(userCode);
                }
                String constraintBy = MetaDataService.getConstraintBy(
                    PATH_HEAD + objName + "." + childName + "." + fieldconfig[i].getName());
                if (constraintBy != null && constraintBy.length() > 0) {
                    fieldconfig[i].setConstraintBy(constraintBy);
                }
            }
            
            childConfig.setRootNode(false);
            childConfig.setParentNode(objNodeConfig);

            // This is not a root object
            objNodeConfig.addChildConfig((ObjectNodeConfig) (objNodeConfigMap.get(
                    childName)));
          } catch (Exception ex) {
            throw new Exception(mLocalizer.t("SRC514: Error occurred while building object " + 
                                             "relationship for childName={0}", childName));
          }
        }

        // Now add EUID to any root object
        for (Iterator iter = objNodeConfigMap.keySet().iterator();
                iter.hasNext();) {
            ObjectNodeConfig obj = (ObjectNodeConfig) objNodeConfigMap.get((String) iter.next());

            if (obj.isRootNode()) {
                FieldConfig fieldConfig;
                fieldConfig = new FieldConfig(null, obj.getName(), "EUID", "EUID",
                        "TextBox", 32, ObjectField.OBJECTMETA_STRING_TYPE);
                fieldConfig.setDisplayOrder(0);
                fieldConfig.setKeyType(true);
                fieldConfig.setValueList(null);
                obj.addFieldConfig(fieldConfig);
            }
        }
    }

    /**
     * Description of the Method
     *
     * @param element Description of the Parameter
     */
    private void buildQwsConfig(Element element) throws Exception {
        
        Integer initialScreenID = new Integer(DEFAULT_INITIAL_SCREEN_ID);     
        String initialScreenName = null;
        ChildElementIterator qwsIter = new ChildElementIterator(element);
        while ( qwsIter.hasNext() ) {
            Element qwsElement = (Element) qwsIter.next();

            if (qwsElement.getTagName().equalsIgnoreCase(LID)) {
                String lidOverrideValue = NodeUtil.getChildNodeText(element,LID);
                if (lidOverrideValue == null || lidOverrideValue.length() == 0) {
                    lidOverrideValue = DEFAULT_LID_NAME;
                    mLogger.info(mLocalizer.x("SRC004: LID override value is null or empty. " 
                                    + "Setting the LID value to: {0}", DEFAULT_LID_NAME));
                } else {
                    mLogger.info(mLocalizer.x("SRC005: Setting the LID override value to: ",
                                              lidOverrideValue));
                }
                mConfigurableQwsValues.put(LID, lidOverrideValue);
            }
            
            if (qwsElement.getTagName().equalsIgnoreCase(LID_HEADER)) {
                String lidHeaderOverrideValue = NodeUtil.getChildNodeText(element,LID_HEADER);
                if (lidHeaderOverrideValue == null || lidHeaderOverrideValue.length() == 0) {
                    lidHeaderOverrideValue = DEFAULT_LID_HEADER_NAME;
                    mLogger.info(mLocalizer.x("SRC006: LID_HEADER override value is null or empty. " 
                                    + "Setting the LID_HEADER value to: {0}", DEFAULT_LID_HEADER_NAME));
                } else {
                    mLogger.info(mLocalizer.x("SRC007: Setting the LID_HEADER override value to: {0}",
                                               lidHeaderOverrideValue));
                }
                mConfigurableQwsValues.put(LID_HEADER, lidHeaderOverrideValue);
            }
            if (qwsElement.getTagName().equalsIgnoreCase(INITIAL_SCREEN_ID)) {
                String screenIDVal = NodeUtil.getChildNodeText(element,INITIAL_SCREEN_ID);
                if (screenIDVal == null || screenIDVal.length() == 0) {
                    initialScreenID = new Integer(DEFAULT_INITIAL_SCREEN_ID);
                    mLogger.info(mLocalizer.x("SRC008: Initial screen ID information not found. " 
                                    + "The initial screen ID has been set to: {0}", 
                                    DEFAULT_INITIAL_SCREEN_ID));
                } else {
                    initialScreenID = Integer.valueOf(screenIDVal);
                    mLogger.info(mLocalizer.x("SRC009: Setting the initial screen ID to: {0}", 
                                              initialScreenID));
                }
                mConfigurableQwsValues.put(INITIAL_SCREEN_ID, initialScreenID);
            }

            if (qwsElement.getTagName().equalsIgnoreCase(RECORD_DETAILS)) {
                try {
                    buildConfigBlock(qwsElement, RECORD_DETAILS);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC515: Error in record details configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(SOURCE_RECORD)) {
                try {
                    buildSubscreensBlock(qwsElement, SOURCE_RECORD);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC516: Error in source record configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(TRANSACTIONS)) {
                try {
                    buildConfigBlock(qwsElement, TRANSACTIONS);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC517: Error in transaction configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(DUPLICATE_RECORDS)) {
                try {
                    buildConfigBlock(qwsElement, DUPLICATE_RECORDS);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC518: Error in duplicate records configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(REPORTS)) {
                if (!hasReportGeneratorJndi) {
                    throw new Exception(mLocalizer.t("SRC519: Error in report configuration: JNDI name of the report generator " +
                                        "must be specified if the report is configured"));
                }
                try {
                    buildSubscreensBlock(qwsElement, REPORTS);
                } catch (Exception ex) {
                      // logged at a higher level
                      throw new Exception(mLocalizer.t("SRC520: Error in matching configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(AUDIT_LOG)) {
                try {
                    String value = NodeUtil.getChildNodeText(element, "allow-insert");
                    // TODO 
                    // may need to move this to security manager in the future?
                    if (value != null && value.equals("true")) {
                            auditLog = true;
                    } else {
                            auditLog = false;
                    }
                    buildConfigBlock(qwsElement, AUDIT_LOG);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC521: Error in audit log configuration: {0}", ex.getMessage()));
                }
            } else if (qwsElement.getTagName().equalsIgnoreCase(ASSUMED_MATCHES)) {
                try {
                    buildConfigBlock(qwsElement, ASSUMED_MATCHES);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC522: Error in assumed matches configuration: {0}", ex.getMessage()));
                }
            }else if (qwsElement.getTagName().equalsIgnoreCase(DASHBOARD)) {
                try {
                    buildConfigBlock(qwsElement, DASHBOARD);
                } catch (Exception ex) {
                    // logged at a higher level
                    throw new Exception(mLocalizer.t("SRC523: Error in audit log configuration: {0}", ex.getMessage()));
                }
            }
        }
    }

    /**
     * Parses the search screen configuration block and stores the 
     * configuration information in a SearchScreenConfig object.
     *
     * @param element  This is the element where the search screen 
     * configuration block is located.
     * @param rootNodeConfig  This is the object containing the root node 
     * configuration information that is used to construct the SearchScreenConfig
     * object that is returned from this method.
     * @param configType This is the type for the configuration block that
     * invoked this method.
     * @return SearchScreenConfig object.
     * @throws Exception if any errors are encountered.
     */
    private SearchScreenConfig buildSearchScreenConfig(Element element,
                                                       ObjectNodeConfig rootNodeConfig,
                                                       String configType) 
                throws Exception {
        
        String screenTitle = NodeUtil.getChildNodeText(element, SCREEN_TITLE);

        // Metadata fields.  Some of these may be required for 
        // certain search screens.
        
        boolean showLID = getAttributeBooleanValue(element, SHOW_LID);
        boolean showEUID = getAttributeBooleanValue(element, SHOW_EUID);
        boolean showCreateDate = getAttributeBooleanValue(element, SHOW_CREATE_DATE);
        boolean showCreateTime = getAttributeBooleanValue(element, SHOW_CREATE_TIME);
        boolean showStatus = getAttributeBooleanValue(element, SHOW_STATUS);
        boolean showSystemUser = getAttributeBooleanValue(element, SHOW_SYSTEM_USER);
        boolean showOperation = getAttributeBooleanValue(element, SHOW_OPERATION);

        String searchResultIDStr = NodeUtil.getChildNodeText(element, SEARCH_RESULT_ID);
        int searchResultID = 0;     // Default value
        if (searchResultIDStr != null) {
            searchResultID = Integer.parseInt(searchResultIDStr);
        }
        
        String searchScreenOrderStr = NodeUtil.getChildNodeText(element, SEARCH_SCREEN_ORDER);
        int searchScreenOrder = 0;     // Default value
        if (searchScreenOrderStr != null) {
            searchScreenOrder = Integer.parseInt(searchScreenOrderStr);
        }
        
        String instruction = null;
        String instructionString = NodeUtil.getChildNodeText(element, "instruction");
        
        if (instructionString != null) {
            instruction = instructionString;
        }
        
        ArrayList fieldConfigGroup = new ArrayList();
        ArrayList searchFieldList = new ArrayList();
        
        // Create FieldConfig objects for the metadata fields.  Sometimes, these
        // fields are configurable by the user.  Other times, they are mandatory.
        if (configType.compareTo(DUPLICATE_RECORDS) == 0
                || configType.compareTo(RECORD_DETAILS) == 0 
                || configType.compareToIgnoreCase(SOURCE_RECORD) == 0 
                || configType.compareToIgnoreCase(POTENTIAL_DUPLICATE_REPORT) == 0) {
                    
            addMetadataSearchFields(rootNodeConfig, element, searchFieldList);
        } else if  (configType.compareTo(TRANSACTIONS) == 0) {
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            TransactionUtil.addTransactionSearchFields(rootNodeConfig, 
                                                       searchFieldList,
                                                       localIdDesignation,
                                                       showEUID,
                                                       showLID);
                                                       
        } else if  (configType.compareTo(ASSUMED_MATCHES) == 0) {
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            AssumedMatchesUtil.addAssumedMatchesSearchFields(rootNodeConfig, 
                                                             searchFieldList, 
                                                             localIdDesignation);
        } else if  (configType.compareTo(AUDIT_LOG) == 0) {
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            AuditLogUtil.addAuditLogSearchFields(rootNodeConfig, 
                                                 searchFieldList,
                                                 localIdDesignation,  
                                                 showEUID, 
                                                 showLID);
                                                 
        } else if (configType.compareToIgnoreCase(ACTIVITY_REPORT) == 0) {  
            ReportUtil.addActivityReportSearchFields(rootNodeConfig, 
                                                     searchFieldList);
        // Merged/Unmerged/Activate/Deactive
        } else if (configType.compareToIgnoreCase(DEACTIVATED_REPORT) == 0
                   || configType.compareToIgnoreCase(MERGED_REPORT) == 0 
                   || configType.compareToIgnoreCase(UNMERGED_REPORT) == 0 )  { 
                       
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            ReportUtil.addMergeReportSearchFields(rootNodeConfig, 
                                                  searchFieldList,
                                                  localIdDesignation);
        } else if (configType.compareToIgnoreCase(UPDATE_REPORT) == 0 ||
                   configType.compareToIgnoreCase(ASSUMED_MATCH_REPORT) == 0 )  { 
                       
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            ReportUtil.addUpdateReportSearchFields(rootNodeConfig, 
                                                   searchFieldList,
                                                   localIdDesignation);
        }         
        
        // create a FieldGroup for any metadata fields
        if (searchFieldList.size() > 0) {
            EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(null, rootNodeConfig);
            FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
            FieldConfigGroup fgc = new FieldConfigGroup(null, searchFieldList);
            fieldConfigGroup.add(fgc);
        }
        
        ChildElementIterator itr = new ChildElementIterator(element, FIELD_GROUP);
        while ( itr.hasNext() ) {
            Element fgrpElement = (Element)itr.next();
            
            String description = NodeUtil.getChildNodeText(fgrpElement, DESCRIPTION);
            EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(description,rootNodeConfig);
            
            ChildElementIterator itr2 = new ChildElementIterator(fgrpElement, FIELD_REF);
            while ( itr2.hasNext() ) {
                Element fieldRefElement = (Element)itr2.next();

                String frefName = NodeUtil.getNodeText(fieldRefElement);
                String required = "";
                String requireAttribute = fieldRefElement.getAttribute("required");
                if (requireAttribute != null && !requireAttribute.equals("") ) {
                    required = requireAttribute;
                }
                // RANGE_SEARCH: new choice attribute
                String choiceAttribute = fieldRefElement.getAttribute("choice");
                if (choiceAttribute != null && !choiceAttribute.equals("") ) {
                    String choice = choiceAttribute;                
                    fgconfig.addFieldRef(frefName, choice, required);
                } else {
                    fgconfig.addFieldRef(frefName, required);
                }
            }
            FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
            if (fconfigs.length > 0) {
                FieldConfigGroup fgc = new FieldConfigGroup(description, fconfigs);
                fieldConfigGroup.add(fgc);
            }
        }

        ChildElementIterator itr2 = new ChildElementIterator(element,"search-option");
        String displayName = new String();
        String queryBuilderName = new String();
        String candidateThreshStr = new String();
        int candidateThreshold = 0;
        boolean isWeighted = false;
        ArrayList nameValuePairs = new ArrayList();
        while ( itr2.hasNext() ) {
            Element o = (Element)itr2.next();
        
            displayName = NodeUtil.getChildNodeText(o, "display-name");
            queryBuilderName = NodeUtil.getChildNodeText(o, "query-builder");
            String weighted = NodeUtil.getChildNodeText(o, "weighted");
            candidateThreshStr = NodeUtil.getChildNodeText(o, "candidate-threshold");
            if (candidateThreshStr != null) {
                candidateThreshold = Integer.parseInt(candidateThreshStr);
            }

            if ((weighted != null) && weighted.equals("true")) {
                isWeighted = true;
            }

            ChildElementIterator pter = new ChildElementIterator(o, "parameter");
            while ( pter.hasNext() ) {
                Element p = (Element)pter.next();
                String n = NodeUtil.getChildNodeText(p,"name");
                String v = NodeUtil.getChildNodeText(p,"value");
                NameValuePair nvp = new NameValuePair(n, v);
                nameValuePairs.add(nvp);
            }
        }
        SearchScreenOptions sScreenOptions 
                = new SearchScreenOptions(displayName, queryBuilderName, 
                                        isWeighted, candidateThreshold,
                                        nameValuePairs);
        SearchScreenConfig searchScreenConfig 
                    = new SearchScreenConfig(rootNodeConfig, screenTitle, 
                                              instruction, searchResultID,
                                              searchScreenOrder,  
                                              showEUID, showLID, showCreateDate,
                                              showCreateTime, showStatus, 
                                              sScreenOptions,
                                              fieldConfigGroup);
        return searchScreenConfig;
    }

    /**
     * Adds the FieldConfig objects for the appropriate metadata fields for
     * search screens.
     *
     * @param rootNodeConfig  This is the object containing the root node 
     * configuration information.
     * @param element  This is the element where the search screen 
     * configuration block is located.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @throws Exception if any errors are encountered.
     */
    private void addMetadataSearchFields(ObjectNodeConfig rootNodeConfig, 
                                         Element element, 
                                         ArrayList searchFieldList) 
            throws Exception {
                                         
        boolean showLID = getAttributeBooleanValue(element, SHOW_LID);
        boolean showEUID = getAttributeBooleanValue(element, SHOW_EUID);
        boolean showCreateDate = getAttributeBooleanValue(element, SHOW_CREATE_DATE);
        boolean showCreateTime = getAttributeBooleanValue(element, SHOW_CREATE_TIME);
        boolean showStatus = getAttributeBooleanValue(element, SHOW_STATUS);
        boolean showSystemUser = getAttributeBooleanValue(element, SHOW_SYSTEM_USER);
        boolean showOperation = getAttributeBooleanValue(element, SHOW_OPERATION);
        boolean showTimestamp = getAttributeBooleanValue(element, SHOW_TIMESTAMP);
        
        if (showEUID) {
            ScreenUtil.addEUID(rootNodeConfig, searchFieldList);
        }
        
        if (showLID) {
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            ScreenUtil.addLID(rootNodeConfig, localIdDesignation, 
                              searchFieldList, ScreenUtil.SEARCH_SCREEN);
        }
        if (showCreateDate) {
            ScreenUtil.addCreateDate(rootNodeConfig, 
                                     searchFieldList, 
                                     ScreenUtil.SEARCH_SCREEN);
        }
        if (showCreateTime) {
            ScreenUtil.addCreateTime(rootNodeConfig, 
                                     searchFieldList, 
                                     ScreenUtil.SEARCH_SCREEN);
        }
        if (showStatus) {
            ScreenUtil.addStatus(rootNodeConfig, 
                                 searchFieldList, 
                                 ScreenUtil.SEARCH_SCREEN);
        }
        if (showSystemUser) {
            ScreenUtil.addSystemUser(rootNodeConfig, searchFieldList);
        }
        if (showOperation) {
            ScreenUtil.addOperation(rootNodeConfig, searchFieldList);
        }
        if (showTimestamp) {
            ScreenUtil.addTimestamp(rootNodeConfig, searchFieldList);
        }
    }
    
    /**
     * Adds the FieldConfig objects for the appropriate metadata fields for
     * search result screens.
     *
     * @param rootNodeConfig  This is the object containing the root node 
     * configuration information.
     * @param element  This is the element where the search screen 
     * configuration block is located.
     * @param resultFieldList This an ArrayList of FieldConfig objects
     * representing the result fields.
     * @throws Exception if any errors are encountered.
     */
    private void addMetadataResultFields(ObjectNodeConfig rootNodeConfig, 
                                         Element element, 
                                         ArrayList resultFieldList) 
            throws Exception {
                                         
        boolean showLID = getAttributeBooleanValue(element, SHOW_LID);
        boolean showEUID = getAttributeBooleanValue(element, SHOW_EUID);
        boolean showCreateDate = getAttributeBooleanValue(element, SHOW_CREATE_DATE);
        boolean showCreateTime = getAttributeBooleanValue(element, SHOW_CREATE_TIME);
        boolean showStatus = getAttributeBooleanValue(element, SHOW_STATUS);
        boolean showSystemUser = getAttributeBooleanValue(element, SHOW_SYSTEM_USER);
        boolean showOperation = getAttributeBooleanValue(element, SHOW_OPERATION);
        boolean showTimestamp = getAttributeBooleanValue(element, SHOW_TIMESTAMP);
        
        if (showEUID) {
            ScreenUtil.addEUID(rootNodeConfig, resultFieldList);
        }
        
        if (showLID) {
            String localIdDesignation 
                    = getConfigurableQwsValue(LID, "Local ID");
            ScreenUtil.addLID(rootNodeConfig, localIdDesignation, 
                              resultFieldList, ScreenUtil.RESULT_SCREEN);
        }
        if (showCreateDate) {
            ScreenUtil.addCreateDate(rootNodeConfig, 
                                     resultFieldList, 
                                     ScreenUtil.RESULT_SCREEN);
        }
        if (showCreateTime) {
            ScreenUtil.addCreateTime(rootNodeConfig, 
                                     resultFieldList, 
                                     ScreenUtil.RESULT_SCREEN);
        }
        if (showStatus) {
            ScreenUtil.addStatus(rootNodeConfig, 
                                 resultFieldList, 
                                 ScreenUtil.RESULT_SCREEN);
        }
        if (showSystemUser) {
            ScreenUtil.addSystemUser(rootNodeConfig, resultFieldList);
        }
        if (showOperation) {
            ScreenUtil.addOperation(rootNodeConfig, resultFieldList);
        }
        if (showTimestamp) {
            ScreenUtil.addTimestamp(rootNodeConfig, resultFieldList);
        }
    }
    
    /**
     * Parses the search results configuration block and stores the 
     * configuration information in a SearchResultsConfig object.
     *
     * @param element  This is the element where the search result
     * configuration block is located.
     * @param rootNodeConfig  This is the object containing the root node 
     * configuration information that is used to construct the 
     * SearchResultsConfig object that is returned.
     * @param configType This is the type for the configuration block that
     * invoked this method.
     * @return SearchResultsConfig object.
     * @throws Exception if any errors are encountered.
     */
    private SearchResultsConfig buildSearchResultsConfig(Element element,
                                                         ObjectNodeConfig rootNodeConfig,
                                                         String configType) 
                throws Exception {
                    
        boolean showLID = getAttributeBooleanValue(element, SHOW_LID);
        boolean showEUID = getAttributeBooleanValue(element, SHOW_EUID);
        boolean showCreateDate = getAttributeBooleanValue(element, SHOW_CREATE_DATE);
        boolean showCreateTime = getAttributeBooleanValue(element, SHOW_CREATE_TIME);
        boolean showStatus = getAttributeBooleanValue(element, SHOW_STATUS);

        String screenTitle = NodeUtil.getChildNodeText(element, SCREEN_TITLE);
        String searchResultID = NodeUtil.getChildNodeText(element, SEARCH_RESULT_ID);
        
        String itemPerPage = NodeUtil.getChildNodeText(element, PAGE_SIZE);
        if (itemPerPage == null) {
            itemPerPage = DEFAULT_PAGE_SIZE;
        }
        
        String maxResultSize = null;
        String maxResultSizeString  = NodeUtil.getChildNodeText(element, MAX_RECORDS);
        if (maxResultSizeString != null) {
            maxResultSize = maxResultSizeString;
        }

        if (maxResultSize == null) {
            maxResultSize = DEFAULT_MAX_RESULTS_SIZE;
        }

        ArrayList fieldConfigGroup = new ArrayList();
        
        // Create FieldConfig objects for the metadata fields.  Sometimes, these
        // fields are configurable by the user.  Other times, they are mandatory.
        if (configType.compareTo(TRANSACTIONS) == 0) {
            String localIdDesignation = getConfigurableQwsValue(LID, "LID");
            TransactionUtil.addTransactionResultFields(rootNodeConfig, 
                                                          fieldConfigGroup,
                                                          localIdDesignation);
        } else if (configType.compareTo(ASSUMED_MATCHES) == 0) {
            String localIdDesignation = getConfigurableQwsValue(LID, "LID");
            AssumedMatchesUtil.addAssumedMatchResultFields(rootNodeConfig, 
                                                           fieldConfigGroup,
                                                           localIdDesignation);
        } else if (configType.compareTo(AUDIT_LOG) == 0) {
            AuditLogUtil.addAuditLogResultFields(rootNodeConfig, 
                                                 fieldConfigGroup);
        } else if (configType.compareTo(SOURCE_RECORD) == 0
                   || configType.compareTo(RECORD_DETAILS) == 0 
                   || configType.compareToIgnoreCase(POTENTIAL_DUPLICATE_REPORT) == 0
                   || configType.compareTo(DUPLICATE_RECORDS) == 0) {
                    
            ArrayList searchResultsList = new ArrayList();
            addMetadataResultFields(rootNodeConfig, element, searchResultsList);
            if (searchResultsList.size() > 0) {
                EosFieldGroupConfig fgconfig 
                        = new EosFieldGroupConfig(null, rootNodeConfig);
                FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
                FieldConfigGroup fgc = new FieldConfigGroup(null, searchResultsList);
                fieldConfigGroup.add(fgc);
            }
        } 
        
        ChildElementIterator itr = new ChildElementIterator(element, FIELD_GROUP);
        while ( itr.hasNext() ) {
            Element fgrpElement = (Element)itr.next();
            
            String description = NodeUtil.getChildNodeText(fgrpElement, DESCRIPTION);
            EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(description,
                    rootNodeConfig);
            
            ChildElementIterator itr2 = new ChildElementIterator(fgrpElement, FIELD_REF);
            while ( itr2.hasNext() ) {
                Element fieldRefElement = (Element)itr2.next();

                String frefName = NodeUtil.getNodeText(fieldRefElement);
                fgconfig.addFieldRef(frefName, "");  // empty string for result screens
            }
            FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
            if (fconfigs.length != 0) {
                FieldConfigGroup fgc = new FieldConfigGroup(description, fconfigs);
                fieldConfigGroup.add(fgc);
            }
        }

        SearchResultsConfig sResultsConfig = new SearchResultsConfig(rootNodeConfig, 
                                                        Integer.parseInt(searchResultID),
                                                        Integer.parseInt(itemPerPage), 
                                                        Integer.parseInt(maxResultSize), 
                                                        false, false, 
                                                        fieldConfigGroup);
        
        return sResultsConfig;
    }

    /**
     * Checks if a key has a value in the mConfigurableQwsValues has map.
     * If so, then return it.  Otherwise, return defaultValue;
     *
     * @param defaultValue  Default value to use if a key does not have
     * a value in the mConfigurableQwsValues hash map.
     * @return configurable value if available, defaultValue otherwise.
     */
    public String getConfigurableQwsValue(Object key, String defaultValue)  {
        String value = (String) mConfigurableQwsValues.get(key);
        if (value == null)  {
            return defaultValue;
        } else {
            return value;
        }
    }
    
    /**
     * Reads the CONFIG_FILENAME file.
     *
     * @throws Exception if an error is encountered.
     */
    private void read() throws Exception {
        InputStream in;
        BufferedReader rdr;
        try {
            in = getClass().getClassLoader().getResourceAsStream(ConfigManager.CONFIG_FILENAME);
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRC507: Could not open file {0}: {1}", 
                                ConfigManager.CONFIG_FILENAME, e.getMessage()));
        }
        read(in);
        in.close();

    }

    /**
     * Read and parse the CONFIG_FILENAME file.
     *
     * @param input InputStream for CONFIG_FILENAME file.
     * @throws Exception if an error is encountered.
     */
    private void read(InputStream input) throws Exception {        
        Document doc = builder.parse(input);
        Element root = doc.getDocumentElement();

        NodeList children = root.getChildNodes();
        ChildElementIterator itr = new ChildElementIterator(root);

        while ( itr.hasNext() ) {
            Element element = (Element)itr.next();

            if (element.getTagName().startsWith("node-")) {
              try {
                ObjectNodeConfig objNodeConfig = buildObjectNodeConfig(element);

                // build a node
                objNodeConfigMap.put(objNodeConfig.getName(), objNodeConfig);
              } catch (Exception ex) {
                  throw new Exception(mLocalizer.t("SRC508: Error occurred in relationship definition: {0}", 
                                                    ex.getMessage()));
              }
            } else if (element.getTagName().equalsIgnoreCase("relationships")) {
              try {
                buildObjectNodeRelationship(element);
              } catch (Exception ex) {
                  throw new Exception(mLocalizer.t("SRC509: Error in relationship definition: {0}", 
                                                    ex.getMessage()));
              }
            } else if (element.getTagName().equalsIgnoreCase("gui-definition")) {
              try {
                  ChildElementIterator guiIter = new ChildElementIterator(element);
                                    
                  while ( guiIter.hasNext() ) {
                      Element guiElement = (Element)guiIter.next();
                    if (guiElement.getTagName().equalsIgnoreCase("page-definition")
                        || guiElement.getTagName().equalsIgnoreCase("system-display-name-overrides")) {
                        
                        // Assigning this element to global variable which will be useful in getScreenObjectFromScreenName()
                        pageDefElement= guiElement;                        
                        buildQwsConfig(guiElement);
                    }
                }
              } catch (Exception ex) {  // exception error already logged
                  throw new Exception(mLocalizer.t("SRC510: Error in GUI definition: {0}", ex.getMessage()));
              }
            } else if (element.getTagName().equalsIgnoreCase("impl-details")) {
              try {
                masterControllerJndi = NodeUtil.getChildNodeText(element,"master-controller-jndi-name");
                validationServiceJndi = NodeUtil.getChildNodeText(element,"validation-service-jndi-name");
                userCodeLookupJndi = NodeUtil.getChildNodeText(element,"usercode-jndi-name");
                
                String reportGeneratorJndiString = NodeUtil.getChildNodeText(element,"reportgenerator-jndi-name");
                if ( reportGeneratorJndiString != null) {
                    reportGeneratorJndi = reportGeneratorJndiString;
                    hasReportGeneratorJndi = true;
                }
                debugFlag = NodeUtil.getChildNodeText(element, "debug-flag");
                debugDest = NodeUtil.getChildNodeText(element,"debug-dest");

                // load the security class if any
                String securityClassName = null;
                
                
                NodeList list = element.getElementsByTagName("object-sensitive-plug-in-class");
        
                if ( list.getLength() > 0 )  {      
		          securityClassName = NodeUtil.getChildNodeText(element,"object-sensitive-plug-in-class");
		        }
        
                if(securityClassName != null) {
                    try {
                        //Class securityClass = Class.forName(securityClassName);
                        //security = (ObjectSensitivePlugIn)securityClass.newInstance();
                    } catch (Exception ex) {
                        throw new Exception(mLocalizer.t("SRC511: Error loading security plug-in class: {0}", 
                                                         ex.getMessage()));
                    }
                }
              } catch (Exception ex) {
                  throw new Exception(mLocalizer.t("SRC512: Error in implementation detail definition: {0}", 
                                                    ex.getMessage()));
              }

                securityEnabled = NodeUtil.getChildNodeText(element,"enable-security");
            }
        }
    }

    /**
     * @return the jndi name for the master controller
     */
    public String getMasterControllerJndi() {
        return masterControllerJndi;
    }
    
    /**
     * @return the jndi name for the report generator
     */
    public String getReportGeneratorJndi() {
        return reportGeneratorJndi;
    }

    /**
     * @return the jndi name for the code lookup service
     */
    public String getValidationServiceJndi() {
        return validationServiceJndi;
    }
    
    /**
     * @return the jndi name for the user code lookup service
     */
    public String getUserCodeLookupJndi() {
        return userCodeLookupJndi;
    }
    
    /**
     * @return the debug flag
     */
    public String getDebugFlag() {
        return debugFlag;
    }
    /**
     * @return the debug flag
     */
    public boolean getDebug() {
        return Boolean.valueOf(debugFlag).booleanValue();
    }
    /**
     * @return the debug destination string
     */
    public String getDebugDest() {
        return debugDest;
    }


    /**
     * @return the secEnabled flag
     */
    public boolean isSecurityEnabled() {
        return (securityEnabled == null  || securityEnabled.equalsIgnoreCase("true"));
    }

    /**
     * @return the handle to the security plug-in object
     */
    public ObjectSensitivePlugIn getSecurityPlugIn() {
        return security;
    }

    /**
     * Resets the length of the EUID field for all ScreenObjects that have
     * EUID fields.  Once a connection has been established with the 
     * MasterController, these ScreenObjects must be modified so that all EUID 
     * fields will have their field lengths set to the correct length as 
     * indicated by the MasterController.
     *
     * @param euidLength  This is the correct length of the EUID fields.
     */
    public void setEuidLength(int euidLength) throws Exception {
        Iterator iter = screensWithEUIDFields.iterator();
        while (iter.hasNext()) {
            Integer screenID = (Integer) iter.next();
            ScreenObject scrObj = getScreen(screenID);
            scrObj.setEuidLength(euidLength);
        }
    }
    /**
     * after reading EDM.xml file in read(InputStream input) <page-definition>
     * element assigned to pageDefElement. This method simply returns 
     * pageDefElement.
     * 
     */
    public Element getPageDefinitionElement(){
        return this.pageDefElement;
    }
    
    /* Returns the corresponding screenobject for given Screen name 
     * @param edmScreenName is the screen Name. Example: record-details,transactions,duplicate-records
     * @return  ScreenObjet of corresponding Screen name
     */ 
     public ScreenObject getScreenObjectFromScreenName(String edmScreenName) throws Exception {
        ChildElementIterator screenNameIterator = null;
        ScreenObject screenObject = null;
        Element guiElement = getPageDefinitionElement();
        screenNameIterator = new ChildElementIterator(guiElement);
        String screenID = null;
        try {
            while (screenNameIterator.hasNext()) {

                Element screenName = (Element) screenNameIterator.next();
                if (screenName.getTagName().equalsIgnoreCase(edmScreenName)) {
                    screenID = NodeUtil.getChildNodeText(screenName, "screen-id");                 
                }
            }
        } catch (Exception ex) {  // exception error already logged
            throw new Exception(mLocalizer.t("SRC532: The ScreenObject could " + 
                                    "not be retrieved from the screen name [{0}]: {1}", 
                                    edmScreenName, ex.getMessage()));
        }if (screenID != null) {
            screenObject = getScreen(new Integer(screenID));
        }
        return screenObject;
    }
/* Returns All the screen Objects
 * it read all the screen names from EDM.xml and retrieves all the screen ids 
 * then it returns the Screenobject.finally List of screen objects will get returned
 * @return  ArrayList of ScreenObjects
 */ 
      public ArrayList getAllScreenObjects() throws Exception {
        ChildElementIterator screenNameIterator = null;
        ScreenObject screenObject = null;
        Element guiElement = getPageDefinitionElement();
        screenNameIterator = new ChildElementIterator(guiElement);
        String screenID = null;
        ArrayList screnObjArrylist = null;
        try {
            screnObjArrylist = new ArrayList();
            while (screenNameIterator.hasNext()) {
                
                Element screenName = (Element) screenNameIterator.next();
                if (!screenName.getTagName().equalsIgnoreCase("initial-screen-id")){                    
                    screenID = NodeUtil.getChildNodeText(screenName, "screen-id");                    
                }
                
                if (screenID != null) {
                    screenObject = getScreen(new Integer(screenID));
                    screnObjArrylist.add(screenObject);
                }

            }
        } catch (Exception ex) {  // exception error already logged
            throw new Exception(mLocalizer.t("SRC533: All ScreenObject instances " + 
                                    "could not not be retrieved: {0}", 
                                    ex.getMessage()));
        }
        return screnObjArrylist;
    }
}
