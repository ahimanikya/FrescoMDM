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
package com.sun.mdm.index.configurator.impl.standardization;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Handles the parsing of the Standardization configuration, defining how to
 * standardize (including phoneticization) objects.
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class StandardizationConfiguration implements ConfigurationInfo {

    /** Module Name to use with the Configuration Service to load the 
     * Standardization configuration 
     */
    public static final String STANDARDIZATION = "Standardization";

    private HashMap sysObjs;
    private Document doc;

    private String moduleName;
    private String parserClass;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    


    /** Creates new StandardizationConfiguration instance. */
    public StandardizationConfiguration() {
    }

    /**
     * Getter for ModuleType attribute.
     *
     * @return the module type of this configuration info
     */
    public String getModuleType() {
        return STANDARDIZATION;
    }

    /**
     * Getter for SystemObjectStandardization attribute.
     *
     * @param sysObjName The qualified name of the system object to retrieve the 
     * standardization information for.
     * @return the standardization information for a specific system object.
     */
    public SystemObjectStandardization getSystemObjectStandardization(String sysObjName) {
        SystemObjectStandardization standardization 
                = (SystemObjectStandardization) sysObjs.get(sysObjName);
        return standardization;
    }

    /** Finish.
     *
     * @return result code.
     */

    public int finish() {
        return 0;
    }

    /** Initialize.
     *
     * @return result code.
     */
    public int init() {
        return 0;
    }

    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node)
            throws ConfigurationException {
        try {
            doc = node.getOwnerDocument();
            Element elem = (Element) node;

            sysObjs = new java.util.HashMap();

            moduleName = elem.getAttribute(Attributes.MODULE_NAME);
            parserClass = elem.getAttribute(Attributes.PARSER_CLASS);

            // Parse the standardization definition for each system object
            NodeList standSystemObjects = elem.getElementsByTagName(Tags.STANDARDIZE_SYSTEM_OBJECT);
            int noOfElements = standSystemObjects.getLength();
            for (int elementCount = 0; elementCount < noOfElements; elementCount++) {
                Element elementToParse = (Element) standSystemObjects.item(elementCount);
                SystemObjectStandardization sysObjStand = parseStandardizeSystemObject(elementToParse);
                sysObjs.put(sysObjStand.getQualifiedName(), sysObjStand);
            }
            mLogger.info(mLocalizer.x("CFG013: Standardization Configuration: the SystemObjects mappings are: {0}", LogUtil.mapToString(sysObjs)));
            mLogger.info(mLocalizer.x("CFG014: Standardization Configuration: the moduleName is: {0}", moduleName));
            mLogger.info(mLocalizer.x("CFG015: Standardization Configuration: the parserClass is: {0}", parserClass));
        } catch (Exception ex) {
            throw new ConfigurationException(mLocalizer.t("CFG542: Could not parse " + 
                                    "the StandardizationConfiguration XML node: {0}", ex));
        }
    }

    /**
     * Returns the #text value of each XML node in the passed in NodeList.
     *
     * @param nodeList XML nodeList.
     * @return ArrayList of #text values as a String object.
     */
    private java.util.ArrayList getEachStrElementValue(NodeList nodeList) {
        java.util.ArrayList result = new java.util.ArrayList();

        if (nodeList != null) {
            for (int nodeCount = 0; nodeCount < nodeList.getLength(); nodeCount++) {
                Node aNode = nodeList.item(nodeCount);
                result.add(getStrElementValue(aNode));
            }
        }

        return result;
    }

    /**
     * Returns the #text value of the first XML node in the passed in NodeList.
     *
     * @param nodeList XML nodeList.
     * @return #text value as a String object, null if didn't exist.
     */
    private String getFirstStrElementValue(NodeList nodeList) {
        String result = null;

        if (nodeList != null && nodeList.getLength() > 0) {
            Node aNode = nodeList.item(0);
            result = getStrElementValue(aNode);
        }
        return result;
    }

    /**
     * Returns the #text value of an XML node.
     *
     * @param node XML node.
     * @return #text value as a String object, null if it doesn't exist.
     */
    private String getStrElementValue(Node node) {
        String result = null;
        Node tnode = node.getFirstChild();
        if (tnode != null) {
            result = tnode.getNodeValue();
        }
        return result;
    }

    /**
     * Parses a XML element node representing a unphoneticized field for
     * standardization.
     *
     * @param fieldElement XML element representing a "phoneticize-field" node.
     * @throws ConfigurationException if the configuration could not be parsed.
     * @return a PhoneticizeField object with the configuration of which fields
     * to phoneticize and how.
     */
    private PhoneticizeField parsePhoneticizeField(Element fieldElement)
            throws ConfigurationException {
        PhoneticizeField phoneticizeField = null;

        if (fieldElement != null) {
            NodeList sourceFieldNodes = fieldElement.getElementsByTagName(Tags.UNPHONETICIZED_SOURCE_FIELD_NAME);
            String sourceFieldName = getFirstStrElementValue(sourceFieldNodes);

            NodeList fieldIDNodes = fieldElement.getElementsByTagName(Tags.PHONETICIZED_OBJECT_FIELD_ID);
            String phoneticizerFieldIDName = getFirstStrElementValue(fieldIDNodes);

            NodeList targetFieldNodes = fieldElement.getElementsByTagName(Tags.PHONETICIZED_TARGET_FIELD_NAME);
            String targetFieldName = getFirstStrElementValue(targetFieldNodes);

            NodeList encodingTypeNodes = fieldElement.getElementsByTagName(Tags.ENCODING_TYPE);
            String encodingTypeName = getFirstStrElementValue(encodingTypeNodes);

            SystemObjectField sourceField = new SystemObjectField(sourceFieldName);
            SystemObjectField targetField = new SystemObjectField(targetFieldName);
            
            phoneticizeField = new PhoneticizeField(encodingTypeName, 
                phoneticizerFieldIDName, sourceField, targetField);
        }

        return phoneticizeField;
    }

    /**
     * Parses a XML element node representing a pre-parsed field group
     * (structure-to-normalize) for normalization.
     *
     * @param groupElement the xml element representing a 'structure-to-normalize' element.
     * @throws ConfigurationException if the configuration could not be parsed.
     * @return a PreparsedFieldGroup object defining which fields to normalize and how.
     */
    private PreparsedFieldGroup parsePreParsedGroup(Element groupElement)
            throws ConfigurationException {
        PreparsedFieldGroup fieldGroup = null;
        LinkedHashMap sourceFieldsDirectMap = new java.util.LinkedHashMap();
        LinkedHashMap standardizationTargets = new java.util.LinkedHashMap();
        HashMap localeMappings = new HashMap();

        NodeList fieldsMapping = groupElement.getElementsByTagName(Tags.FIELDS_MAPPING);

        String standardizationType =
                groupElement.getAttribute(Attributes.STANDARDIZATION_TYPE);
        String concatenationDelimiter =
                groupElement.getAttribute(Attributes.CONCATENATION_DELIMITER);
        String domainSelector =
                groupElement.getAttribute(Attributes.DOMAIN_SELECTOR);
        
        String localeFieldName = null;
        NodeList localeFieldNodes = groupElement.getElementsByTagName(Tags.LOCALE_FIELD_NAME);
        
        if (localeFieldNodes != null && localeFieldNodes.getLength() > 0) {
            localeFieldName = getFirstStrElementValue(localeFieldNodes);
            NodeList localeMapsElements = groupElement.getElementsByTagName(Tags.LOCALE_MAPS);
            parseLocaleMaps(localeMapsElements, localeMappings);
        }
        
        NodeList normSource = groupElement.getElementsByTagName(Tags.UNNORMALIZED_SOURCE_FIELDS);
        Element normSourceElement = null;
        if (normSource != null && normSource.getLength() > 0) {
            normSourceElement = (Element) normSource.item(0);
        }

        NodeList normTargets = groupElement.getElementsByTagName(Tags.NORMALIZATION_TARGETS);
        Element normTargetsElement = null;
        if (normTargets != null && normTargets.getLength() > 0) {
            normTargetsElement = (Element) normTargets.item(0);
        }

        if (normSourceElement != null) {
            NodeList sourceMapping = normSourceElement.getElementsByTagName(Tags.SOURCE_MAPPING);

            int noOfMappings = sourceMapping.getLength();
            for (int mappingCount = 0; mappingCount < noOfMappings; mappingCount++) {
                Element mapping = (Element) sourceMapping.item(mappingCount);

                NodeList sourceNodes = mapping.getElementsByTagName(Tags.UNNORMALIZED_SOURCE_FIELD_NAME);
                String source = getFirstStrElementValue(sourceNodes);

                NodeList standObjFieldIDNodes = mapping.getElementsByTagName(Tags.STANDARDIZED_OBJECT_FIELD_ID);
                String standObjFieldID = getFirstStrElementValue(standObjFieldIDNodes);

                // SystemObjectField -> Standardized object field ID
                try  {
                    sourceFieldsDirectMap.put(new SystemObjectField(source), standObjFieldID);
                }  catch (ConfigurationException e)  {
                    mLogger.severe(mLocalizer.x("CFG040: Standardization Configuration: " +
                                                "invalid XML source for an unnormalized " +
                                                "field name: {0}" , source));
                }
            }
        }
        if (normTargetsElement != null) {
            NodeList targetMapping = normTargetsElement.getElementsByTagName(Tags.TARGET_MAPPING);

            int noOfMappings = targetMapping.getLength();
            for (int mappingCount = 0; mappingCount < noOfMappings; mappingCount++) {
                Element mapping = (Element) targetMapping.item(mappingCount);

                NodeList standObjFieldIDNodes = mapping.getElementsByTagName(Tags.STANDARDIZED_OBJECT_FIELD_ID);
                String standObjFieldID = getFirstStrElementValue(standObjFieldIDNodes);

                NodeList targetNodes = mapping.getElementsByTagName(Tags.STANDARDIZED_TARGET_FIELD_NAME);
                String target = getFirstStrElementValue(targetNodes);

                // Standardized object field ID -> SystemObjectField
                standardizationTargets.put(standObjFieldID, new SystemObjectField(target));
            }
        }
        
        try {
            if (localeFieldName != null && localeFieldName.length() > 0) {
                SystemObjectField localeField = new SystemObjectField(localeFieldName);
                fieldGroup = new PreparsedFieldGroup(sourceFieldsDirectMap, 
                    concatenationDelimiter, standardizationTargets, localeMappings, 
                    standardizationType, domainSelector, localeField);
            } else {
                fieldGroup = new PreparsedFieldGroup(sourceFieldsDirectMap, 
                    concatenationDelimiter, standardizationTargets, localeMappings, 
                    standardizationType, domainSelector, null);
            }
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG543: Could not parse " + 
                                    "the PreParsedGroup configuration XML node: {0}", e));
        }
        return fieldGroup;
    }
    
    /*
     * Parses the XML nodes locale maps node and stores the values into a hash table.
     *
     * @param localeMapElements  Locale map elements to parse.
     * @param localeHashMap  Hash map containing the locale mappings.
     */
    private void parseLocaleMaps(NodeList localeMapsElements, HashMap localeHashMap) {
        if (localeMapsElements == null || localeMapsElements.getLength() == 0) {
            return;
        }
        Element localeElement = (Element) localeMapsElements.item(0);

        NodeList localeCodeElements = localeElement.getElementsByTagName(Tags.LOCALE_CODES);
        int noOfElements = localeCodeElements.getLength();
        for (int elementCount = 0; elementCount < noOfElements; elementCount++) {
            Element localeCodeElement = (Element) localeCodeElements.item(elementCount);
            NodeList localeValueNodes = localeCodeElement.getElementsByTagName(Tags.LOCALE_VALUES);
            String localeValue = getFirstStrElementValue(localeValueNodes);

            NodeList localeNodes = localeCodeElement.getElementsByTagName(Tags.LOCALE);
            String locale = getFirstStrElementValue(localeNodes);
            localeHashMap.put(localeValue, locale);
        }
        return;
    }


    /**
     * Parses a XML element node representing a system object standardization
     * definition.
     *
     * @param sysObjStandElement The xml element representing a standardize-system-object.
     * @throws ConfigurationException if the configuration could not be parsed.
     * @return a SystemObjectStandardization object defining how to standardize a given 
     * SystemObject.
     */
    private SystemObjectStandardization parseStandardizeSystemObject(Element sysObjStandElement)
            throws ConfigurationException {
                
        SystemObjectStandardization sysObjStand = null;
        String sysObjName = null;
        ArrayList preParsed = new java.util.ArrayList();
        ArrayList unParsed = new java.util.ArrayList();
        ArrayList phoneticize = new java.util.ArrayList();

        NodeList sysObjNameNodes = sysObjStandElement.getElementsByTagName(Tags.SYSTEM_OBJECT_NAME);
        sysObjName = getFirstStrElementValue(sysObjNameNodes);

        NodeList preParsedGroups = sysObjStandElement.getElementsByTagName(Tags.PRE_PARSED_FIELD_GROUPS);
        if (preParsedGroups != null && preParsedGroups.getLength() > 0) {
            Element groupsElement = (Element) preParsedGroups.item(0);

            NodeList preParsedNodes = groupsElement.getElementsByTagName(Tags.GROUP);
            int noOfPreParsed = preParsedNodes.getLength();

            for (int preParsedCount = 0; preParsedCount < noOfPreParsed; preParsedCount++) {
                PreparsedFieldGroup group = parsePreParsedGroup((Element) preParsedNodes.item(preParsedCount));
                preParsed.add(group);
            }
        }

        NodeList unParsedGroups = sysObjStandElement.getElementsByTagName(Tags.UNPARSED_FIELD_GROUPS);

        if (unParsedGroups != null && unParsedGroups.getLength() > 0) {
            Element groupsElement = (Element) unParsedGroups.item(0);

            NodeList unParsedNodes = groupsElement.getElementsByTagName(Tags.GROUP);
            int noOfUnParsed = unParsedNodes.getLength();
            for (int unParsedCount = 0; unParsedCount < noOfUnParsed; unParsedCount++) {
                UnparsedFieldGroup group = parseUnParsedGroup((Element) unParsedNodes.item(unParsedCount));
                unParsed.add(group);
            }
        }

        NodeList fields = sysObjStandElement.getElementsByTagName(Tags.PHONETICIZE_FIELDS);
        if (fields != null && fields.getLength() > 0) {
            Element fieldsElement = (Element) fields.item(0);

            NodeList phoneticizeNodes = fieldsElement.getElementsByTagName(Tags.PHONETICIZE_FIELD);
            int noOfPhoneticize = phoneticizeNodes.getLength();
            for (int phoneticizeCount = 0; phoneticizeCount < noOfPhoneticize; phoneticizeCount++) {
                PhoneticizeField field = parsePhoneticizeField((Element) phoneticizeNodes.item(phoneticizeCount));
                phoneticize.add(field);
            }
        }

        sysObjStand = new SystemObjectStandardization(phoneticize, preParsed, 
            unParsed, sysObjName);        
        
        return sysObjStand;
    }

    /**
     * Parses a XML element node representing a unparsed field group for
     * standardization.
     *
     * @param groupElement An XML element representing a free-form-text-to-standardize element.
     * @throws ConfigurationException if the configuration could not be parsed.
     * @return a UnparsedFieldGroup object defining how to standardize a field group.
     */
    private UnparsedFieldGroup parseUnParsedGroup(Element groupElement)
        throws ConfigurationException {
        UnparsedFieldGroup fieldGroup = null;
        String concatenationDelimiter = null;
        ArrayList sourceFields = new java.util.ArrayList();
        LinkedHashMap standardizationTargets = new java.util.LinkedHashMap();
        HashMap localeMappings = new HashMap();
        
        String standardizationType =
                groupElement.getAttribute(Attributes.STANDARDIZATION_TYPE);
        String domainSelector =
                groupElement.getAttribute(Attributes.DOMAIN_SELECTOR);
        
        NodeList localeFieldNodes = groupElement.getElementsByTagName(Tags.LOCALE_FIELD_NAME);
        String localeFieldName = null;
        if (localeFieldNodes != null && localeFieldNodes.getLength() > 0) {
            localeFieldName = getFirstStrElementValue(localeFieldNodes);
            NodeList localeMapsElements = groupElement.getElementsByTagName(Tags.LOCALE_MAPS);
            parseLocaleMaps(localeMapsElements, localeMappings);
        }
        
        NodeList localeMaps = groupElement.getElementsByTagName(Tags.LOCALE_MAPS);
        String localeMap = getFirstStrElementValue(localeMaps);
        
        NodeList standTargets = groupElement.getElementsByTagName(Tags.STANDARDIZATION_TARGETS);
        Element standTargetsElement = null;
        if (standTargets != null && standTargets.getLength() > 0) {
            standTargetsElement = (Element) standTargets.item(0);
        }

        NodeList sourceFieldsNodes = groupElement.getElementsByTagName(Tags.UNSTANDARDIZED_SOURCE_FIELD_NAME);
        Element sourceFieldsElement = null;
        if (sourceFieldsNodes != null && sourceFieldsNodes.getLength() > 0) {
            sourceFieldsElement = (Element) sourceFieldsNodes.item(0);
        }
        java.util.ArrayList sourceFieldNames = getEachStrElementValue(sourceFieldsNodes);

        java.util.Iterator fieldsIter = sourceFieldNames.iterator();
        while (fieldsIter.hasNext()) {
            String fieldName = (String) fieldsIter.next();
            sourceFields.add(new SystemObjectField(fieldName));
        }

        if (standTargetsElement != null) {
            NodeList targetMapping = standTargetsElement.getElementsByTagName(Tags.TARGET_MAPPING);

            int noOfMappings = targetMapping.getLength();
            for (int mappingCount = 0; mappingCount < noOfMappings; mappingCount++) {
                Element mapping = (Element) targetMapping.item(mappingCount);

                NodeList standObjFieldIDNodes = mapping.getElementsByTagName(Tags.STANDARDIZED_OBJECT_FIELD_ID);
                String standObjFieldID = getFirstStrElementValue(standObjFieldIDNodes);

                NodeList targetNodes = mapping.getElementsByTagName(Tags.STANDARDIZED_TARGET_FIELD_NAME);
                String target = getFirstStrElementValue(targetNodes);

                // Standardized object field ID -> SystemObjectField
                standardizationTargets.put(standObjFieldID, new SystemObjectField(target));
            }
        }

        try {
            if (localeFieldName != null && localeFieldName.length() > 0) {
                SystemObjectField localeField = new SystemObjectField(localeFieldName);
                fieldGroup = new UnparsedFieldGroup(sourceFields, concatenationDelimiter,
                    standardizationTargets, localeMappings, standardizationType, domainSelector, localeField);
            } else {
                fieldGroup = new UnparsedFieldGroup(sourceFields, concatenationDelimiter,
                    standardizationTargets, localeMappings, standardizationType, domainSelector, null);
            }
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG544: Could not parse " + 
                                    "the UnParsedGroup configuration XML node: {0}", e));
        }
            
        return fieldGroup;
    }

    /**
     * Attributes that correspond to the XML attribute names.
     */
    public static final class Attributes { 
        /** name attribute */
        public static final String NAME = "name";
        /** module name attribute */
        public static final String MODULE_NAME = "module-name";
        /** parser class attribute */
        public static final String PARSER_CLASS = "parser-class";
        /** standardization type attribute */
        public static final String STANDARDIZATION_TYPE = "standardization-type";
        /** concatenation delimiter attribute */
        public static final String CONCATENATION_DELIMITER = "concatenation-delimiter";
        /** domain selector attribute */
        public static final String DOMAIN_SELECTOR = "domain-selector";
    }

    /**
     * Tags that correspond to the XML node names.
     */
    public static final class Tags {
        /** standardize system object tag */
        public static final String STANDARDIZE_SYSTEM_OBJECT = "standardize-system-object";
        /** structures to normalize tag */
        public static final String PRE_PARSED_FIELD_GROUPS = "structures-to-normalize";
        /** free form text to standardize tag */
        public static final String UNPARSED_FIELD_GROUPS = "free-form-texts-to-standardize";
        /** unnormalize source fields tag */
        public static final String UNNORMALIZED_SOURCE_FIELDS = "unnormalized-source-fields";
        /** source mapping tag */
        public static final String SOURCE_MAPPING = "source-mapping";
        /** unnormalized source field name tag */
        public static final String UNNORMALIZED_SOURCE_FIELD_NAME = "unnormalized-source-field-name";
        /** normalization targets tag */
        public static final String NORMALIZATION_TARGETS = "normalization-targets";
        /** phoneticize fields tag */
        public static final String PHONETICIZE_FIELDS = "phoneticize-fields";
        /** group tag */
        public static final String GROUP = "group";
        /** phoneticize field tag */
        public static final String PHONETICIZE_FIELD = "phoneticize-field";
        /** fields mapping tag */
        public static final String FIELDS_MAPPING = "fields-mapping";
        /** unstandardized source fields tag */
        public static final String UNSTANDARDIZED_SOURCE_FIELDS = "unstandardized-source-fields";
        /** standardization targets tag */
        public static final String STANDARDIZATION_TARGETS = "standardization-targets";
        /** system object name tag */
        public static final String SYSTEM_OBJECT_NAME = "system-object-name";
        /** unphoneticized source field name tag */
        public static final String UNPHONETICIZED_SOURCE_FIELD_NAME = "unphoneticized-source-field-name";
        /** phoneticized object field id tag */
        public static final String PHONETICIZED_OBJECT_FIELD_ID = "phoneticized-object-field-id";
        /** phoneticized target field name tag */
        public static final String PHONETICIZED_TARGET_FIELD_NAME = "phoneticized-target-field-name";
        /** encoding type tag */
        public static final String ENCODING_TYPE = "encoding-type";
        /** unstandardized source field name tag */
        public static final String UNSTANDARDIZED_SOURCE_FIELD_NAME = "unstandardized-source-field-name";
        /** target mapping tag */
        public static final String TARGET_MAPPING = "target-mapping";
        /** standardized object field id tag */
        public static final String STANDARDIZED_OBJECT_FIELD_ID = "standardized-object-field-id";
        /** standardized target field name tag */
        public static final String STANDARDIZED_TARGET_FIELD_NAME = "standardized-target-field-name";
        /** locale field name tag */
        public static final String LOCALE_FIELD_NAME = "locale-field-name";
        /** locale maps tag */
        public static final String LOCALE_MAPS = "locale-maps";
        /** locale codes tag */
        public static final String LOCALE_CODES = "locale-codes";
        /** locale value tag */
        public static final String LOCALE_VALUES = "value";
        /** locale name tag */
        public static final String LOCALE = "locale";
    }

}
