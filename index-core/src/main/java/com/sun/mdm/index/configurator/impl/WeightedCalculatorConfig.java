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
import com.sun.mdm.index.survivor.impl.SurvivorQuality;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/** Weighted survivor strategy configuration object
 *
 * @author  ckuo
 * @version $Revision: 1.1 $
 */
public class WeightedCalculatorConfig implements ConfigurationInfo {
    /** module name */    
    static final String ATR_MODULE_NAME = "module-name";
    /** tag for parsing XML */    
    static final String ATR_PARSER_CLASS = "parser-class";
    /** tag for parsing XML */    
    public static final String MODULE_NAME = "WeightedSurvivorCalculator";
    /** tag for parsing XML */    
    public static final String TAG_CANDIDATE_FIELD = "candidate-field";
    /** tag for parsing XML */    
    public static final String ATR_NAME = "name";
    /** tag for parsing XML */    
    public static final String TAG_PARAMETER = "parameter";
    /** tag for parsing XML */    
    public static final String TAG_QUALITY = "quality";
    /** tag for parsing XML */    
    public static final String TAG_PREFERENCE = "preference";
    /** tag for parsing XML */    
    public static final String TAG_UTILITY = "utility";
    /** tag for parsing XML */    
    public static final String TAG_SOURCE = "source";
    /** tag for parsing XML */    
    static final String TAG_DEFAULT_PARAMETER = "default-parameters";
    
    /** configuration for weighted strategy */    
    private Map mConfig;
    /** module name */    
    private String mModuleName;
    /** parser class */    
    private String mParserClass;
    /** reference to DOM document */    
    private Document mDoc;

    /** default rules */
    private Collection mDefaultRules;
    
    private final Logger logger = LogUtil.getLogger(this);
    
    
    /** Creates new WeightedCalculatorConfig */
    public WeightedCalculatorConfig() {
        mConfig = new LinkedHashMap();
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

    /** Returns the #text value of an XML node.
     *
     * @param node XML node.
     * @return #text value as a String object.
     */
    private String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();

        if (tnode != null) {
            return tnode.getNodeValue();
        } else {
            return null;
        }
    }

    /** Return the list of rules.
     *
     * @param candidateField Candidate name to retrieve the rules for.
     * @return collection of rules.
     */    
    public Collection getRules(String candidateField) {
        return (Collection) mConfig.get(candidateField);
    }

    /** Return the default list of rules.
     *
     * @return default collection of rules
     */    
    public Collection getDefaultRules() {
        return mDefaultRules;
    }

    /** Parses a XML element node representing a parameter list.
     *
     * @param parameters NodeList for parameters.
     * @return a collection of StrategyParameter objects.
     */
    private Collection parseParameters(NodeList parameters) {
        
        // parameter instances collection
        Collection paramInsts = new ArrayList(); 

        // for each parameter
        int paramCount = parameters.getLength();

        for (int i = 0; i < paramCount; i++) {
            Element parameter = (Element) parameters.item(i);

            String quality = null;
            String utility = null;
            String preference = null;
            String source = null;

            // parameter specifics : name, value, type
            NodeList paramSpec = parameter.getChildNodes(); 
            int paramSpecCount = paramSpec.getLength();

            for (int j = 0; j < paramSpecCount; j++) {
                // get parameter name, value, and type
                Node node = (Node) paramSpec.item(j);

                String nodeName = node.getNodeName();

                if (nodeName.equals(TAG_QUALITY)) {
                    // get parameter-name
                    quality = getStrElementValue(node);
                }

                if (nodeName.equals(TAG_PREFERENCE)) {
                    // get parameter-value
                    preference = getStrElementValue(node);
                }

                if (nodeName.equals(TAG_UTILITY)) {
                    // get parameter-type
                    utility = getStrElementValue(node);
                }

                if (nodeName.equals(TAG_SOURCE)) {
                    // get parameter-type
                    source = getStrElementValue(node);
                }
            }

            // create parameter object
            SurvivorQuality p = new SurvivorQuality(quality, preference,
                                                    source, utility);

            // store parameter in collection
            paramInsts.add(p);
        }

        return paramInsts;
    }

    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node) throws ConfigurationException {
        Element elem = (Element) node;

        mModuleName = elem.getAttribute(ATR_MODULE_NAME);
        mParserClass = elem.getAttribute(ATR_PARSER_CLASS);

        NodeList candidateFields = elem.getElementsByTagName(TAG_CANDIDATE_FIELD);
        int len = candidateFields.getLength();

        for (int i = 0; i < len; i++) {
            Element candidate = (Element) candidateFields.item(i);
            String candidateName = candidate.getAttribute(ATR_NAME);

            NodeList parameters = candidate.getElementsByTagName(TAG_PARAMETER);
            Collection parameterInsts = parseParameters(parameters);
            mConfig.put(candidateName, parameterInsts);
        }
        
        NodeList defaultRules = elem.getElementsByTagName(TAG_DEFAULT_PARAMETER);
        len = defaultRules.getLength();
        for (int i = 0; i < len; i++) {
            Element defaultParameter = (Element) defaultRules.item(i);

            NodeList parameters = defaultParameter.getElementsByTagName(TAG_PARAMETER);
            Collection parameterInsts = parseParameters(parameters);
            mDefaultRules = parameterInsts;
        }
        
        logger.info("WeightedCalculatorConfig:WeightStrategy::" + mConfig);
    }

    /** Return String representing the module type
     *
     * @return return String representing the module type
     */
    public String getModuleType() {
        return "WeightedCalculator";
    }
}
