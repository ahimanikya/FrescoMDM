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
package com.sun.mdm.index.configurator.impl;

import java.util.HashMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Handles the parsing of the Match Engine Functional Area configuration (MEFA)
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class MEFAConfiguration implements ConfigurationInfo {

    /** Module Name to use with the Configuration Service to load the MEFA configuration */
    public static final String MEFA = "MEFA";

    /** HashMap from the component name to the configured implementation class */
    private HashMap componentImplClassNames;

    private Document doc;

    private String moduleName;
    private String parserClass;
    private final Logger logger = LogUtil.getLogger(this);
    

    /**
     * Creates new MEFAConfiguration
     */
    public MEFAConfiguration() {
    }


    /**
     * Getter for BlockPickerImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @return implementation class name for the block picker
     */
    public String getBlockPickerImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.BLOCK_PICKER);
        return implClass;
    }


    /**
     * Getter for ComponentImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @param componentName The name of the component to get the implementation class for.
     * @return implementation class name for the requested component.
     */
    public String getComponentImplClassName(String componentName) {
        String implClass = (String) componentImplClassNames.get(componentName);
        return implClass;
    }


    /**
     * Getter for MatcherAPIImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @return implementation class name for the Matcher API.
     */
    public String getMatcherAPIImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.MATCHER_API);
        return implClass;
    }


    /**
     * Getter for MatcherConfigImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @return implementation class name for the Matcher Configuration.
     */
    public String getMatcherConfigImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.MATCH_ENGINE_CONFIG);
        return implClass;
    }


    /**
     * Getter for ModuleType attribute of the MEFAConfiguration
     * object.
     *
     * @return the type of this configuration module.
     */
    public String getModuleType() {
        return MEFA;
    }


    /**
     * Getter for PassControllerImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @return implementation class name for the PassController.
     */
    public String getPassControllerImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.PASS_CONTROLLER);
        return implClass;
    }


    /**
     * Getter for StandardizerAPIImplClassName attribute of the
     * MEFAConfiguration object.
     *
     * @return implementation class name for the Stardardizer API.
     */
    public String getStandardizerAPIImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.STANDARDIZER_API);
        return implClass;
    }


    /**
     * Getter for StandardizerConfigImplClassName attribute of
     * the MEFAConfiguration object.
     *
     * @return implementation class name for the Standardizer Configuration.
     */
    public String getStandardizerConfigImplClassName() {
        String implClass 
                = (String) componentImplClassNames.get(Components.STANDARDIZER_ENGINE_CONFIG);
        return implClass;
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

            componentImplClassNames = new java.util.HashMap();

            moduleName = elem.getAttribute(Attributes.MODULE_NAME);
            parserClass = elem.getAttribute(Attributes.PARSER_CLASS);

            // Parse the MEFA configuration for each component
            String blockPickerImplClass = null;
            String passControllerImplClass = null;
            String standardizerAPIImplClass = null;
            String standardizerCfgImplClass = null;
            String matcherAPIImplClass = null;
            String matcherCfgImplClass = null;

            NodeList blockPickerElements = elem.getElementsByTagName(Tags.BLOCK_PICKER);
            if (blockPickerElements != null && blockPickerElements.getLength() > 0) {
                Element blockPickerElement = (Element) blockPickerElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = blockPickerElement.getElementsByTagName(Tags.CLASS_NAME);
                blockPickerImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.BLOCK_PICKER, blockPickerImplClass);

            NodeList passControllerElements = elem.getElementsByTagName(Tags.PASS_CONTROLLER);
            if (passControllerElements != null && passControllerElements.getLength() > 0) {
                Element passControllerElement = (Element) passControllerElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = passControllerElement.getElementsByTagName(Tags.CLASS_NAME);
                passControllerImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.PASS_CONTROLLER, passControllerImplClass);

            NodeList standardizerAPIElements = elem.getElementsByTagName(Tags.STANDARDIZER_API);
            if (standardizerAPIElements != null && standardizerAPIElements.getLength() > 0) {
                Element standardizerAPIElement = (Element) standardizerAPIElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = standardizerAPIElement.getElementsByTagName(Tags.CLASS_NAME);
                standardizerAPIImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.STANDARDIZER_API, standardizerAPIImplClass);

            NodeList standardizerCfgElements = elem.getElementsByTagName(Tags.STANDARDIZER_CONFIG);
            if (standardizerCfgElements != null && standardizerCfgElements.getLength() > 0) {
                Element standardizerCfgElement = (Element) standardizerCfgElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = standardizerCfgElement.getElementsByTagName(Tags.CLASS_NAME);
                standardizerCfgImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.STANDARDIZER_ENGINE_CONFIG, standardizerCfgImplClass);

            NodeList matcherAPIElements = elem.getElementsByTagName(Tags.MATCHER_API);
            if (matcherAPIElements != null && matcherAPIElements.getLength() > 0) {
                Element matcherAPIElement = (Element) matcherAPIElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = matcherAPIElement.getElementsByTagName(Tags.CLASS_NAME);
                matcherAPIImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.MATCHER_API, matcherAPIImplClass);

            NodeList matcherCfgElements = elem.getElementsByTagName(Tags.MATCHER_CONFIG);
            if (matcherCfgElements != null && matcherCfgElements.getLength() > 0) {
                Element matcherCfgElement = (Element) matcherCfgElements.item(0);
                // Defined in the XSD to be one item

                NodeList classNameElements = matcherCfgElement.getElementsByTagName(Tags.CLASS_NAME);
                matcherCfgImplClass = getFirstStrElementValue(classNameElements);
            }
            componentImplClassNames.put(Components.MATCH_ENGINE_CONFIG, matcherCfgImplClass);

            logger.info("MEFA Configuration:\n" + LogUtil.mapToString(componentImplClassNames));
            
        } catch (Exception ex) {
            throw new ConfigurationException("Failed to parse MEFA configuration:" + ex.getMessage(), ex);
        }
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
     * Attributes that correspond to the XML attribute names.
     */
    public static final class Attributes {
        /** name attribute */
        public static final String NAME = "name";
        /** module name attribute */
        public static final String MODULE_NAME = "module-name";
        /** parser class attribute */
        public static final String PARSER_CLASS = "parser-class";
    }


    /**
     * Tags that correspond to the XML node names.
     */
    public static final class Tags {
        /** block picker tag */
        public static final String BLOCK_PICKER = "block-picker";
        /** pass controller tag */
        public static final String PASS_CONTROLLER = "pass-controller";
        /** standardizer api tag */
        public static final String STANDARDIZER_API = "standardizer-api";
        /** standardizer configuration tag */
        public static final String STANDARDIZER_CONFIG = "standardizer-config";
        /** matcher api tag */
        public static final String MATCHER_API = "matcher-api";
        /** matcher config tag */
        public static final String MATCHER_CONFIG = "matcher-config";
        /** class name tag */
        public static final String CLASS_NAME = "class-name";

    }


    /**
     * ComponentNames which identify the available MEFA components
     */
    public static class Components {
        /** MEFA component name for block picker*/
        public static final String BLOCK_PICKER = "BLOCK_PICKER";
        /** MEFA component name for pass controller*/
        public static final String PASS_CONTROLLER = "PASS_CONTROLLER";
        /** MEFA component name for standardizer api*/
        public static final String STANDARDIZER_API = "STANDARDIZER_API";
        /** MEFA component name for matcher api*/
        public static final String MATCHER_API = "MATCHER_API";
        /** MEFA component name for match engine configuration*/
        public static final String MATCH_ENGINE_CONFIG = "MATCH_ENGINE_CONFIG";
        /** MEFA component name for standardizer engine configuration*/
        public static final String STANDARDIZER_ENGINE_CONFIG = "STANDARDIZER_ENGINE_CONFIG";
    }

}
