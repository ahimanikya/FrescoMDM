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

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.survivor.StrategyParameter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.logging.Level;

import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.LogUtil;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/** Survivor helper configuration object
 *
 * @author  ckuo
 * @version $Revision: 1.1 $
 */
public class SurvivorHelperConfig implements ConfigurationInfo {
    
    /** module type */    
    public static final String MODULE_NAME = "SurvivorHelper";
    
    /** xml tag for parsing */
    static final String TAG_HELPER_CLASS = "helper-class";
    /** xml tag for parsing */
    static final String TAG_MATCH_DEFINITIONS = "candidate-definitions";
    /** xml tag for parsing */
    static final String TAG_CANDIDATE_FIELD = "candidate-field";
    /** xml tag for parsing */
    static final String TAG_SYSTEM_FIELDS = "system-fields";

    /** xml tag for parsing */
    static final String TAG_FIELD_NAME = "field-name";
    /** xml tag for parsing */
    static final String TAG_FIELD_TYPE = "field-type";
    /** xml tag for parsing */
    static final String TAG_SURVIVOR_STRATEGY = "survivor-strategy";
    /** xml tag for parsing */
    static final String TAG_DEFAULT_SURVIVOR_STRATEGY = "default-survivor-strategy";
    /** xml tag for parsing */
    static final String TAG_SURVIVOR_CLASS = "strategy-class";
    /** xml tag for parsing */
    static final String TAG_PARAMETERS = "parameters";
    /** xml tag for parsing */
    static final String TAG_PARAMETER = "parameter";
    /** xml tag for parsing */
    static final String TAG_PARAMETER_NAME = "parameter-name";
    /** xml tag for parsing */
    static final String TAG_PARAMETER_TYPE = "parameter-type";
    /** xml tag for parsing */
    static final String TAG_PARAMETER_VALUE = "parameter-value";
    /** xml tag for parsing */
    static final String ATR_NAME = "name";
    /** xml tag for parsing */
    static final String ATR_MODULE_NAME = "module-name";
    /** xml tag for parsing */
    static final String ATR_PARSER_CLASS = "parser-class";

    // config parameters
    /** strategy cache */    
    private Map mStrategyCache;
    /** field cache */    
    private Map mFieldCache;
    /** helper class name */    
    private String mHelperClassName;
    /** default strategy */    
    private StrategyConfig mDefaultStrategy;
    /** module name */    
    private String mModuleName;
    /** parser class */    
    private String mParserClass;

    // misc
    /** DOM doc reference */    
    private Document mDoc;
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    

    /** Creates new UpdateManagerConfig */
    public SurvivorHelperConfig() {
        mStrategyCache = new Hashtable();
        mFieldCache = Collections.synchronizedMap(new HashMap());
        mHelperClassName = "";
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

    /** Return the list of fields.
     *
     * @return Return the list of fields
     */    
    public Map getFieldCache() {
        return mFieldCache;
    }

    /** Return the entire strategy cache.
     *
     * @return Return the entire strategy cache.
     */    
    public Map getStrategyCache() {
        return mStrategyCache;
    }

    /** Return the default strategy.
     *
     * @return Return the default strategy.
     */    
    public StrategyConfig getDefaultStrategy() {
        return mDefaultStrategy;
    }

    /** Set the default strategy.
     *
     * @param sc Strategy configuration for default strategy.
     */    
    public void setDefaultStrategy(StrategyConfig sc) {
        mDefaultStrategy = sc;
    }

    /** Return helper class name.
     *
     * @return Return helper class name.
     */    
    public String getHelperClassName() {
        return mHelperClassName;
    }

    /** Set helper class name.
     *
     * @param s helper class name
     */    
    public void setHelperClassName(String s) {
        mHelperClassName = s;
    }

    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node) throws ConfigurationException {
        try {
            // set the mDoc
            mDoc = node.getOwnerDocument();

            Element elem = (Element) node;

            mModuleName = elem.getAttribute(ATR_MODULE_NAME);
            mParserClass = elem.getAttribute(ATR_PARSER_CLASS);

            NodeList tempNodeList = null;

            // parse default strategy
            NodeList defaultStrategy = elem.getElementsByTagName(TAG_DEFAULT_SURVIVOR_STRATEGY);
            mDefaultStrategy = parseStrategy((Element) defaultStrategy.item(0));

            // parse helper class name
            tempNodeList = elem.getElementsByTagName(TAG_HELPER_CLASS);
            mHelperClassName = getStrElementValue(tempNodeList.item(0));

            NodeList matchDefs = elem.getElementsByTagName(TAG_MATCH_DEFINITIONS);

            if ((matchDefs == null) || (matchDefs.getLength() < 1)) {
                //not there, just end
                //must contains 1, defined in XSD
                throw new ConfigurationException(
                    "Missing candidate-definitions in the file");
            }

            Element matchDef = (Element) matchDefs.item(0);

            // parse each candidate fields
            NodeList candidateFields = matchDef.getElementsByTagName(TAG_CANDIDATE_FIELD);

            if (candidateFields == null) {
                // must contain 1+
                //not there, return error
                throw new ConfigurationException(
                    "Missing elements for candidate-field");
            }

            int candidateCount = candidateFields.getLength();

            for (int i = 0; i < candidateCount; i++) {
                Element candidate = (Element) candidateFields.item(i);

                String candidateName = candidate.getAttribute(ATR_NAME);
                tempNodeList = null;

                // get system-fields
                tempNodeList = candidate.getElementsByTagName(TAG_SYSTEM_FIELDS);
                Collection fieldInsts = null;
                if (tempNodeList != null && tempNodeList.getLength() > 0) {
                    // if exists, there is only 1
                    Element systemFields = (Element) tempNodeList.item(0); 

                    //parse system-fields for this candidate field
                    fieldInsts = parseSystemFields(systemFields);
                }
                mFieldCache.put(candidateName, fieldInsts);
                
                // get survivor-strategy
                tempNodeList = candidate.getElementsByTagName(TAG_SURVIVOR_STRATEGY);

                if ((tempNodeList != null) && (tempNodeList.getLength() > 0)) {
                    Element strategy = (Element) tempNodeList.item(0); // if exists, there is only 1
                    StrategyConfig sc = parseStrategy(strategy);

                    // store strategy instance in cache, using candidate field as key
                    mStrategyCache.put(candidateName, sc);
                }
            }
            mLogger.info(mLocalizer.x("CFG031: Survivor Helper Configuration: StrategyCache mappings are: {0}", LogUtil.mapToString(mStrategyCache)));
            mLogger.info(mLocalizer.x("CFG032: Survivor Helper Configuration: FieldCache mappings are: {0}", LogUtil.mapToString(mFieldCache)));
        } catch (Exception ex) {
            throw new ConfigurationException(ex);
        }
    }

    /** Returns the #text value of an XML node.
     *
     * @param node XML node
     * @return #text value as a String object
     */
    private String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();

        return tnode.getNodeValue();
    }

    /** Parses a XML element node representing a parameter list.
     *
     * @param parameterList XML element representing a "parameters" node.
     * @return a collection of StrategyParameter objects.
     */
    private Collection parseParameters(Element parameterList) {
        Collection paramInsts = new ArrayList(); // parameter instances collection

        // for each parameter
        NodeList parameters = parameterList.getElementsByTagName(TAG_PARAMETER);
        int paramCount = parameters.getLength();

        for (int i = 0; i < paramCount; i++) {
            Element parameter = (Element) parameters.item(i);

            String paramName = null;
            String paramValue = null;
            String paramType = null;

            // parameter specifics : name, value, type
            NodeList paramSpec = parameter.getChildNodes(); 
            int paramSpecCount = paramSpec.getLength();

            for (int j = 0; j < paramSpecCount; j++) {
                // get parameter name, value, and type
                Node node = (Node) paramSpec.item(j);

                String nodeName = node.getNodeName();

                if (nodeName.equals(TAG_PARAMETER_NAME)) {
                    // get parameter-name
                    paramName = getStrElementValue(node);
                }

                if (nodeName.equals(TAG_PARAMETER_VALUE)) {
                    // get parameter-value
                    paramValue = getStrElementValue(node);
                }

                if (nodeName.equals(TAG_PARAMETER_TYPE)) {
                    // get parameter-type
                    paramType = getStrElementValue(node);
                }
            }

            // create parameter object
            StrategyParameter p = new StrategyParameter(paramName, paramType,
                    paramValue);

            // store parameter in collection
            paramInsts.add(p);
        }

        return paramInsts;
    }

    /** Parses a XML element representing a list of system fields.
     *
     * @param systemFieldList An XML element representing a list of system fields.
     * @throws EPathException if an error occurs.
     * @return a collection of SystemField objects.
     */
    private Collection parseSystemFields(Element systemFieldList)
            throws EPathException {
                
        Collection fieldInsts = new ArrayList(); // epath collection

        // for each field-name in system-fields
        NodeList systemfields = systemFieldList.getChildNodes();
        int fieldCount = systemfields.getLength();

        for (int i = 0; i < fieldCount; i++) {
            String fieldName = null;

            Node node = (Node) systemfields.item(i);

            if (node.getNodeName().equals(TAG_FIELD_NAME)) {
                // get field-name
                fieldName = getStrElementValue(node);

                // create a EPath object
                // use parser to create, instead of new
                EPath e = EPathParser.parse(fieldName);

                // store field in collection
                fieldInsts.add(e);
            }
        }

        return fieldInsts;
    }

    /** Parse strategy node.
     *
     * @param strategyElement XML element for strategy.
     * @return strategy configuration.
     */    
    private StrategyConfig parseStrategy(Element strategyElement) {
        // get strategy-class in survivor-strategy
        NodeList tempNodeList = strategyElement.getElementsByTagName(TAG_SURVIVOR_CLASS);
        Element strategyClassElement = (Element) tempNodeList.item(0);
        String className = null;

        className = getStrElementValue(strategyClassElement);

        // get parameters in survivor-strategy
        tempNodeList = strategyElement.getElementsByTagName(TAG_PARAMETERS);

        Collection paramInsts = null;

        if ((tempNodeList != null) && (tempNodeList.getLength() > 0)) {
            Element parameterList = (Element) tempNodeList.item(0);

            //parse parameters for this strategy
            paramInsts = parseParameters(parameterList);
        }

        return new StrategyConfig(className, paramInsts);
    }

    /** Return the module type.
     *
     * @return the module type
     */    
    public String getModuleType() {
        return "SurvivorHelperConfig";
    }

    /** Strategy configuration class */    
    public class StrategyConfig {
        /** strategy class name */        
        String strategyClassName;
        /** initialization parameters */        
        Collection initParameters;

        /** default constructor */        
        public StrategyConfig() {
            strategyClassName = "";
            initParameters = new ArrayList();
        }

        /** Constructor.
         *
         * @param s Strategy class name.
         * @param c Initialization parameters.
         */        
        public StrategyConfig(String s, Collection c) {
            strategyClassName = s;
            initParameters = c;
        }

        /** Return strategy class name.
         *
         * @return return strategy class name.
         */        
        public String getStrategyClassName() {
            return strategyClassName;
        }

        /** Return initialization parameters.
         *
         * @return initialization parameters.
         */        
        public Collection getInitParameters() {
            return initParameters;
        }
    }
}
