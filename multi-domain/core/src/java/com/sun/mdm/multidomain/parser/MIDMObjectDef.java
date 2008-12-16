/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import com.sun.mdm.multidomain.parser.SearchOptions.Parameter;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import net.java.hulp.i18n.Logger;
import java.util.HashMap;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;


import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
/**
 *
 * @author wee
 */
public class MIDMObjectDef {
    
    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.parser.MIDMObjectDef");
    //private static transient final Localizer mLocalizer = Localizer.get();

    
    public static final String LID = "local-id";
    
    private static final String SEARCH_RESULT_PAGES = "search-result-pages";
      
    private static final String SEARCH_RESULT_LIST_PAGE = "search-result-list-page";
    
    private static final String SCREEN_TITLE = "screen-title";
    
    private static final String SEARCH_RESULT_ID = "search-result-id";
    
    private static final String PAGE_SIZE = "item-per-page";
    
    private static final String MAX_RECORDS = "max-result-size";
    
    private static final String FIELD_GROUP = "field-group";
    
    private static final String DESCRIPTION = "description";
    
    private static final String FIELD_REF = "field-ref";
    
    private static final String SEARCH_SCREEN_ORDER = "search-screen-order";
    
    private static final String DISPLAY_NAME = "display-name";
    
    private static final String NODE_NAME = "name";
    
    private static final String NODE_DISPLAY_ORDER = "display-order";
    
    private static final String MERGE_MUST_DELETE = "merge-must-delete";
    
    private static final String FIELD_NODE = "field";
    
    public static final String DOMAIN_MIDM_NODES = "domain-nodes";
    
    private static final String INPUT_MASK = "input-mask";
    private static final String VALUE_MASK = "value-mask";
    private static final String KEY_TYPE = "key-type";
    private static final String IS_SENSITIVE = "is-sensitive";
    private static final String MAX_LENGTH = "max-length";
    private static final String GUI_TYPE = "gui-type";
    private static final String VALUE_LIST = "value-list";
    private static final String VALUE_TYPE = "value-type";
    public static final String FIELD_DELIM = ".";
    

    
    private DocumentBuilder builder;
    
    private DomainForWebManager mDomain = null;
    
    private HashMap objNodeConfigMap = new HashMap();
    
    private String rootObjectName = null;
    
    //private ObjectNode objNode = null;
    
    public String parseMIdmNodeObject(InputSource input) throws Exception {
        String midmNodeStr = null;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        Element root = doc.getDocumentElement();        
        NodeList children = root.getChildNodes();
        Node element = null;

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                element = children.item(i);
                 String name = ((Element) children.item(i)).getTagName();
                if (!name.equals("node")) {
                    root.removeChild(element);
                }
            }
        }
        
        //root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "schema/MultiDomainWebManager.xsd");
        root.removeAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "schema/midm.xsd");
        root.removeAttribute("xmlns:xsi"); 
        root.removeAttribute("xsi:noNamespaceSchemaLocation");
        //root.removeAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation");
        byte[] webXml = transformToXML(doc);

        return new String(webXml);
    }
    
    public String writeToString(Element rootMIDM) throws IOException, Exception {
        //XMLWriterUtil xmlDoc = new XMLWriterUtil();
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        xmldoc = impl.createDocument(null, DOMAIN_MIDM_NODES, null);

        Element root = xmldoc.getDocumentElement();
        NodeList children = rootMIDM.getChildNodes();
        Node element = null;

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                element = children.item(i);
                 String name = ((Element) children.item(i)).getTagName();
                if (!name.equals("node")) {
                    rootMIDM.removeChild(element);
                    //Node tempNode = root.a
                    //root.appendChild((Node) children.item(i));
                }
                //break;
            }
        }
        
        /**
            
        if (null != element && ((Element) element).getTagName().equals("midm") && element.hasChildNodes()) {
            NodeList nl1 = element.getChildNodes();
            for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) children.item(i1)).getTagName();
                    if (name.equals("node")) {
                        root.appendChild(nl1.item(i1));
                    }
                }
            }
        }
         */ 
    
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "schema/MultiDomainWebManager.xsd");
        byte[] webXml = transformToXML(xmldoc);
        return new String(webXml);

    }
    
    public byte[] transformToXML(Document xmldoc) throws Exception {
        DOMConfiguration domConfig = xmldoc.getDomConfig();
        DOMSource domSource = new DOMSource(xmldoc);
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute("indent-number", 4);  
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(new OutputStreamWriter(out, "UTF-8"));
        serializer.transform(domSource, result);
        return out.toByteArray();
        
    }

    
    
    public DomainForWebManager parseMIDMNode(InputSource input) throws Exception {
        
        //mDomain = domain;

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        builder = docBuilderFactory.newDocumentBuilder();

        Document doc = builder.parse(input);
        Element root = doc.getDocumentElement();

        NodeList children = root.getChildNodes();
        ChildElementIterator itr = new ChildElementIterator(root);

        boolean isRootNode = false;
        while ( itr.hasNext() ) {
            Element element = (Element)itr.next();
            if (element.getTagName().startsWith("node")) {
                try {
                    ObjectNode objNodeConfig = buildObjectNode(element);
                    if (!isRootNode) {
                        mDomain = new DomainForWebManager(objNodeConfig.getName());
                        rootObjectName = objNodeConfig.getName();
                        isRootNode = true;
                    }

                    // build a node
                    objNodeConfigMap.put(objNodeConfig.getName(), objNodeConfig);
                } catch (Exception ex) {
                    //throw new Exception(mLocalizer.t("SRC508: Error occurred in node definition: {0}",
                  //          ex.getMessage()));
                    throw new Exception("SRC508: Error occurred in node definition: {0}" +  ex.getMessage());
                }
            } else if (element.getTagName().equalsIgnoreCase("relationships")) {
                try {
                    buildObjectNodeRelationship(element);
                } catch (Exception ex) {
                    throw new Exception("SRC509: Error in relationship definition: {0}" +  ex.getMessage());
                }
            } else if (element.getTagName().equalsIgnoreCase("gui-definition")) {
                try {
                    ChildElementIterator guiIter = new ChildElementIterator(element);

                    while (guiIter.hasNext()) {
                        Element guiElement = (Element) guiIter.next();
                        if (guiElement.getTagName().equalsIgnoreCase("page-definition") || guiElement.getTagName().equalsIgnoreCase("system-display-name-overrides")) {

                            // Assigning this element to global variable which will be useful in getScreenObjectFromScreenName()
                            //pageDefElement= guiElement;                        
                            buildQwsConfig(guiElement);
                        }
                    }
                } catch (Exception ex) {
                    //throw new Exception(mLocalizer.t("SRC510: Error in GUI definition: {0}", ex.getMessage()));
                    throw new Exception("SRC510: Error in GUI definition: {0}" + ex.getMessage());
                }
            }

        }
        
        return mDomain;
        
        
    }
    
    private ObjectNode buildObjectNode(Element element) throws IOException, Exception {

        String objName = NodeUtil.getChildNodeText(element, NODE_NAME);
        //mDomain = new DomainForWebManager(objName);
        
        String attr = element.getAttribute(NODE_DISPLAY_ORDER);
        String attr1 = element.getAttribute(MERGE_MUST_DELETE);
        int order = 0;
        boolean mustDelete=false;
        if (attr != null && !attr.equals("")) {
            order = Integer.parseInt(attr);
        }
        if (attr1 != null && attr1.equalsIgnoreCase("true") ) {
            mustDelete = true ;
        }

        // skip "node-" and get to the node name
        ObjectNode objNodeConfig = new ObjectNode(objName);
        objNodeConfig.setDisplayOrder(order);
        objNodeConfig.setMustDelete(mustDelete);

        
        ChildElementIterator itr = new ChildElementIterator(element);
        while ( itr.hasNext() ) {
            Element e;
            Element field = (Element)itr.next();
            String fieldName = null;
            String nodeTag = field.getTagName();
            // process the fields
            if (nodeTag.equalsIgnoreCase(FIELD_NODE)) {
                try {

                    fieldName = NodeUtil.getChildNodeText(field, NODE_NAME); 
                    String displayName = NodeUtil.getChildNodeText(field, DISPLAY_NAME);
                    String inputMask = NodeUtil.getChildNodeText(field, INPUT_MASK);
                    String valueMask = NodeUtil.getChildNodeText(field, VALUE_MASK);
                    boolean keyType = false;
                    String keyTypeString = NodeUtil.getChildNodeText(field, KEY_TYPE);
                    if ((keyTypeString != null) && keyTypeString.equals("true")) {
                        keyType = true;
                    }

                    boolean isSensitive = false;
                    String isSensitiveString = NodeUtil.getChildNodeText(field, IS_SENSITIVE);
                    if ((isSensitiveString != null) && isSensitiveString.equals("true")) {
                        isSensitive = true;
                    }

                    // optional field
                    String displayOrder = "0";
                    String displayOrderString = NodeUtil.getChildNodeText(field, NODE_DISPLAY_ORDER);
                    if (displayOrderString != null) {
                        displayOrder = displayOrderString;
                    }
                    int idisplayOrder = Integer.parseInt(displayOrder);

                    String maxLength = "32";
                    String maxLengthString = NodeUtil.getChildNodeText(field, MAX_LENGTH);
                    // optional field
                    if (maxLengthString != null) {
                        maxLength = maxLengthString;
                    }

                    int imaxLength = Integer.parseInt(maxLength);

                    if (inputMask != null) {
                        imaxLength = inputMask.length();
                    }

                    String guiType = NodeUtil.getChildNodeText(field, GUI_TYPE);

                    String valueList = null;            
                    String valueListString = NodeUtil.getChildNodeText(field, VALUE_LIST);

                    if (valueListString != null) {
                        valueList = valueListString;
                    }

                    // optional field
                    String valueTypeStr = "String";
                    String valueTypeString = NodeUtil.getChildNodeText(field, VALUE_TYPE);
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
                        //throw new IOException(mLocalizer.t("SRC506: Invalid value mask [{0}] for input mask [{1}]", valueMask, inputMask));
                          throw new IOException("SRC506: Invalid value mask [{0}] for input mask [{1}]" +  valueMask + inputMask);
                      }
                      //fieldConfig.setValueMask(valueMask);
                    }

                    objNodeConfig.addFieldConfig(fieldConfig);

                } catch (Exception ex) {
                    //throw new Exception(mLocalizer.t("SRC513: Error occurred while building object node config for field name = {0}", fieldName));
                    throw new Exception("SRC513: Error occurred while building object node config for field name = {0}" +  fieldName);
                }
            }
        }

        return objNodeConfig;
    }
    
    private int getMetaType(String valueTypeStr) {
        if (valueTypeStr.equalsIgnoreCase("Int")) {
            //return ObjectField.OBJECTMETA_INT_TYPE;
            return 0;
        }

        if (valueTypeStr.equalsIgnoreCase("Boolean")) {
            //return ObjectField.OBJECTMETA_BOOL_TYPE;
            return 1;
        }

        if (valueTypeStr.equalsIgnoreCase("String")) {
            //return ObjectField.OBJECTMETA_STRING_TYPE;
            return 2;
        }

        if (valueTypeStr.equalsIgnoreCase("Byte")) {
            //return ObjectField.OBJECTMETA_BYTE_TYPE;
            return 3;
        }

        if (valueTypeStr.equalsIgnoreCase("Character")) {
            //return ObjectField.OBJECTMETA_CHAR_TYPE;
            return 4;
        }

        if (valueTypeStr.equalsIgnoreCase("Long")) {
            //return ObjectField.OBJECTMETA_LONG_TYPE;
            return 5;
        }

        if (valueTypeStr.equalsIgnoreCase("Blob")) {
            //return ObjectField.OBJECTMETA_BLOB_TYPE;
            return 6;
        }

        if (valueTypeStr.equalsIgnoreCase("Date")) {
            //return ObjectField.OBJECTMETA_DATE_TYPE;
            return 7;
        }

        if (valueTypeStr.equalsIgnoreCase("Float")) {
            //return ObjectField.OBJECTMETA_FLOAT_TYPE;
            return 8;
        }

        if (valueTypeStr.equalsIgnoreCase("Time")) {
            //return ObjectField.OBJECTMETA_TIMESTAMP_TYPE;
            return 9;
        }

        return -1;
    }

    
    public static String getDateInputMask() {
        String format = "MM/DD/y";
        String dateMask = format.replace('M', 'D');
        dateMask = dateMask.replace('d', 'D');
        dateMask = dateMask.replace('y', 'D');
        return dateMask;
    }    
    
    /**
     * Description of the Method
     *
     * @param element Description of the Parameter
     */
    private void buildQwsConfig(Element element) throws Exception {
        
        //Integer initialScreenID = new Integer(DEFAULT_INITIAL_SCREEN_ID);     
        String initialScreenName = null;
        ChildElementIterator qwsIter = new ChildElementIterator(element);
        while ( qwsIter.hasNext() ) {
            Element qwsElement = (Element) qwsIter.next();
            if (element.getTagName().startsWith("node")) {
              try {
                ObjectNode objNode = buildObjectNode(element);

                // build a node
                
                objNodeConfigMap.put(objNode.getName(), objNode);
              } catch (Exception ex) {
                  //throw new Exception(mLocalizer.t("SRC508: Error occurred in node definition: {0}", 
                  //                                  ex.getMessage()));
                  throw new Exception("SRC508: Error occurred in node definition: {0}" + 
                                                    ex.getMessage());
              }
            } else if (element.getTagName().equalsIgnoreCase("relationships")) {
              try {
                buildObjectNodeRelationship(element);
              } catch (Exception ex) {
                  //throw new Exception(mLocalizer.t("SRC509: Error in relationship definition: {0}", 
                  //                                  ex.getMessage()));
                  throw new Exception("SRC509: Error in relationship definition: {0}" +  
                                                    ex.getMessage());
              }
            } else  if (qwsElement.getTagName().equalsIgnoreCase("record-details")) {
                try {
                    buildConfigBlock(qwsElement, "record-details");
                } catch (Exception ex) {
                    //throw new Exception(mLocalizer.t("SRC515: Error in record details configuration: {0}", ex.getMessage()));
                    throw new Exception("SRC515: Error in record details configuration: {0}" +  ex.getMessage());
                }
            } 
        }
    }
    
    /**
    public String getConfigurableQwsValue(Object key, String defaultValue)  {
        String value = (String) mConfigurableQwsValues.get(key);
        if (value == null)  {
            return defaultValue;
        } else {
            return value;
        }
    }
     */ 
    
    
    private void buildConfigBlock(Element element, String configType) 
            throws Exception {
                
        /**
        String rootObjName = NodeUtil.getChildNodeText(element, ROOT_OBJ);
        ObjectNode objNodeConfig = (ObjectNode) objNodeConfigMap.get(rootObjName);
        String title = NodeUtil.getChildNodeText(element, TAB_NAME);
        Integer screenID = Integer.valueOf(NodeUtil.getChildNodeText(element, SCREEN_ID));
        String displayOrder = NodeUtil.getChildNodeText(element, DISPLAY_ORDER);
         */ 
        int pageSize = 0;
        int maxRecords = 0;
        int searchResultID = 0;
        int searchScreenOrder = 0;
        String description = new String();
        
        // Process all search pages.
        Element e = (Element)element.getElementsByTagName("search-pages").item(0);
        

        
        // If no simple search pages are found, continue processing.  
        // Some screens, such as Assumed Match, contain automatically-generated
        // search critera that the user cannot modify.  There can be only 
        // one such search page per tab.  This situation occurs if the <search-pages>
        // tag is not found.
        boolean simpleSearchPageFound = false;
        ChildElementIterator itr = null;
        if (e != null) {
            itr = new ChildElementIterator(e);
        }

        ArrayList<SimpleSearchType> searchScreensConfig = mDomain.getSearchType();
        ArrayList<SearchDetail> searchResultsConfig = mDomain.getSearchDetail();
        ArrayList<RecordDetail> recordDetailConfig = new ArrayList<RecordDetail>();
        while (itr != null && itr.hasNext() ) {
            Element sscElement = (Element)itr.next();
        
            if (sscElement.getTagName().equalsIgnoreCase("simple-search-page")) {
                searchScreensConfig.add(buildSearchScreenConfig(sscElement, configType));

                //searchScreensConfig.add(ssconfig);
                simpleSearchPageFound = true;
            }
        }        

        // Process search result list page
        Element searchResultsElement 
                = (Element)element.getElementsByTagName(SEARCH_RESULT_PAGES).item(0);
        
        itr = new ChildElementIterator(searchResultsElement);
        while (itr.hasNext() ) {
            Element srcElement = (Element)itr.next();
        
            if (srcElement.getTagName().equalsIgnoreCase(SEARCH_RESULT_LIST_PAGE)) {
                    searchResultsConfig.add(buildSearchResultsConfig(srcElement, configType));
            }
        }        
        
        //create Record Detail from Node.
        ObjectNode objNode = (ObjectNode) objNodeConfigMap.get(rootObjectName);
        
        //ObjectNode[] childNodes = objNode.getChildConfigs();
        
        FieldConfig[] fieldconfig = objNode.getFieldConfigs();
        RecordDetail recDetail = new RecordDetail(1, rootObjectName);
        FieldGroup newRecGroup = new FieldGroup();
        newRecGroup.setDescription(objNode.getName());
        recDetail.addFieldGroup(newRecGroup);
        ArrayList<FieldGroup.FieldRef> recordDetailRefs = newRecGroup.getFieldRefs();
        for (int i = 0; i < fieldconfig.length; i++) {
            FieldConfig field = fieldconfig[i];
            String epath = FieldConfig.toEpathStyleString(field.getFullFieldName());
            newRecGroup.addFieldRef(newRecGroup.createFieldRef(epath));
        }
        
        ObjectNode[] childrenNode = objNode.getChildConfigs();
        for (ObjectNode childNode : childrenNode) {
            FieldConfig[] childfieldconfig = childNode.getFieldConfigs();
            FieldGroup newChildGroup = new FieldGroup();
            recDetail.addFieldGroup(newChildGroup);
            newChildGroup.setDescription(childNode.getName());
            ArrayList<FieldGroup.FieldRef> childDetailRefs = newChildGroup.getFieldRefs();
            for (int i = 0; i < childfieldconfig.length; i++) {
                FieldConfig field = childfieldconfig[i];
                String epath = FieldConfig.toEpathStyleString(field.getFullName());
                newChildGroup.addFieldRef(newChildGroup.createFieldRef(epath));
            }
        
            
        }
        mDomain.addRecordDetail(recDetail);
        

        
        //since there is no record summary in MIDM, just default to the first search result.

        if (searchResultsConfig.size() > 0 )  {
            SearchDetail searchDetail = searchResultsConfig.get(0);
            for (FieldGroup group : searchDetail.getFieldGroups()) {
                FieldGroup newGroup = new FieldGroup();
                newGroup.setDescription(group.getDescription());
                ArrayList<FieldGroup.FieldRef> fieldRefs = group.getFieldRefs();
                for (FieldGroup.FieldRef field : fieldRefs) {
                    newGroup.addFieldRef(newGroup.createFieldRef(field.getFieldName()));
                }        
                mDomain.addRecordSummary(newGroup);
            }
        }
        
        

    }
 
    private void buildObjectNodeRelationship(Element element) throws Exception {
        // load object definition
        //ObjectFactory.init();

        String objName = NodeUtil.getChildNodeText(element,"name");
        ObjectNode objNode = (ObjectNode) objNodeConfigMap.get(objName);
        
        // get required and updateable attributes from object definition
        FieldConfig[] fieldconfig = objNode.getFieldConfigs();
        /**
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
         */ 

        ChildElementIterator itr = new ChildElementIterator(element,"children");
        while ( itr.hasNext() ) {
            Element childElement = (Element)itr.next();
            String childName = NodeUtil.getNodeText(childElement);
        
         try {
            ObjectNode childConfig = (ObjectNode) objNodeConfigMap.get(childName);
            
            // get required and updateable attributes from object definition
            fieldconfig = childConfig.getFieldConfigs();

            for (int i = 0; i < fieldconfig.length; i++) {
                fieldconfig[i].setRootObject(objName);
            }
            
            childConfig.setRootNode(false);
            childConfig.setParentNode(objNode);

            // This is not a root object
            objNode.addChildConfig((ObjectNode) (objNodeConfigMap.get(
                    childName)));
          } catch (Exception ex) {
            //throw new Exception(mLocalizer.t("SRC514: Error occurred while building object " + 
            //                                 "relationship for childName={0}", childName));
            throw new Exception("SRC514: Error occurred while building object " + 
                                             "relationship for childName={0}" +  childName);
          }
        }

        // Now add EUID to any root object
        for (Iterator iter = objNodeConfigMap.keySet().iterator();
                iter.hasNext();) {
            ObjectNode obj = (ObjectNode) objNodeConfigMap.get((String) iter.next());

            if (obj.isRootNode()) {
                FieldConfig fieldConfig;
                fieldConfig = new FieldConfig(null, obj.getName(), "EUID", "EUID",
                        "TextBox", 32, 2);
                fieldConfig.setDisplayOrder(0);
                fieldConfig.setKeyType(true);
                fieldConfig.setValueList(null);
                obj.addFieldConfig(fieldConfig);
            }
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
    private SearchDetail buildSearchResultsConfig(Element element, String configType) 
                throws Exception {
                    
        //boolean showLID = getAttributeBooleanValue(element, SHOW_LID);
        //boolean showEUID = getAttributeBooleanValue(element, SHOW_EUID);
        //boolean showCreateDate = getAttributeBooleanValue(element, SHOW_CREATE_DATE);
        //boolean showCreateTime = getAttributeBooleanValue(element, SHOW_CREATE_TIME);
       // boolean showStatus = getAttributeBooleanValue(element, SHOW_STATUS);

        
        String screenTitle = NodeUtil.getChildNodeText(element, SCREEN_TITLE);
        String searchResultID = NodeUtil.getChildNodeText(element, SEARCH_RESULT_ID);
        if (screenTitle == null || screenTitle.length() == 0) {
            screenTitle = "Search Result " + searchResultID;
        }
        
        String itemPerPage = NodeUtil.getChildNodeText(element, PAGE_SIZE);
        
        String maxResultSize = null;
        String maxResultSizeString  = NodeUtil.getChildNodeText(element, MAX_RECORDS);
        if (maxResultSizeString != null) {
            maxResultSize = maxResultSizeString;
        }

        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        SearchDetail searchResult = new SearchDetail(Integer.parseInt(searchResultID), Integer.parseInt(itemPerPage)
                                        , Integer.parseInt(maxResultSize), 
                                        1, screenTitle, fieldGroups );
        
        
        //ArrayList fieldConfigGroup = new ArrayList();
        
        // Create FieldConfig objects for the metadata fields.  Sometimes, these
        // fields are configurable by the user.  Other times, they are mandatory.
        ChildElementIterator itr = new ChildElementIterator(element, FIELD_GROUP);
        while ( itr.hasNext() ) {
            FieldGroup group = new FieldGroup();
            Element fgrpElement = (Element)itr.next();
            String description = NodeUtil.getChildNodeText(fgrpElement, DESCRIPTION);
            group.setDescription(description);
            ChildElementIterator itr2 = new ChildElementIterator(fgrpElement, FIELD_REF);
            while ( itr2.hasNext() ) {
                Element fieldRefElement = (Element)itr2.next();

                String frefName = NodeUtil.getNodeText(fieldRefElement);
                FieldGroup.FieldRef fieldRef = group.createFieldRef(frefName);
                group.addFieldRef(fieldRef);
                //fgconfig.addFieldRef(frefName, "");  // empty string for result screens
            }
            fieldGroups.add(group);
        }
        
        return searchResult;

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
    private SimpleSearchType buildSearchScreenConfig(Element element, String configType) 
                throws Exception {
                    
        //SimpleSearchType searchPage = new SimpleSearchType();
        
       // SimpleSearchType searchScreen = new SimpleSearchType();
        
        FieldGroup group = null;
        String screenTitle = NodeUtil.getChildNodeText(element, SCREEN_TITLE);

        // Metadata fields.  Some of these may be required for 
        // certain search screens.
        
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
        
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        SimpleSearchType simpleSearchType = new SimpleSearchType(screenTitle,
                          searchResultID, instruction, searchScreenOrder, fieldGroups);
        SearchOptions  searchOpt = new SearchOptions();

        
        ChildElementIterator itr = new ChildElementIterator(element, FIELD_GROUP);
        while ( itr.hasNext() ) {
            Element fgrpElement = (Element)itr.next();
            
            String description = NodeUtil.getChildNodeText(fgrpElement, DESCRIPTION);
            //EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(description,rootNodeConfig);
            
            group = new FieldGroup();
            group.setDescription(description);
            
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
                    group.addFieldRef(group.createFieldRef(frefName));
                } else {
                    group.addFieldRef(group.createFieldRef(frefName));
                }
            }
            
            fieldGroups.add(group);
            
        }

        ChildElementIterator itr2 = new ChildElementIterator(element,"search-option");
        String displayName = new String();
        String queryBuilderName = new String();
        String candidateThreshStr = new String();
        int candidateThreshold = 0;
        boolean isWeighted = false;
        ArrayList<Parameter> nameValuePairs = new ArrayList<Parameter>();
        
        while ( itr2.hasNext() ) {
            Element o = (Element)itr2.next();
        
            displayName = NodeUtil.getChildNodeText(o, DISPLAY_NAME);
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
                searchOpt.addParameter(n, v);
            }
        }
        searchOpt.setQueryBuilder(queryBuilderName);
        searchOpt.setWeighted(isWeighted);
        simpleSearchType.setSearchOption(searchOpt);
        
        return simpleSearchType;

    }

}
