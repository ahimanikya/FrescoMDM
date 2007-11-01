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
package com.sun.mdm.index.parser;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;


/**
 * @author kkao
 * @version
 */
public class MasterType {
    private final String mTagConfiguration = "Configuration";
    private final String mTagModuleName = "module-name";
    private final String mTagParserClass = "parser-class";
    
    private final String mTagMasterControllerConfig = "MasterControllerConfig";
    private final String mTagUpdateMode = "update-mode";   // Optimistic
    private final String mTagMergedRecordUpdate = "merged-record-update";  // Disabled
    private final String mTagExecuteMatch = "execute-match";
    private final String mTagTransaction = "transaction";
    private final String mTagQueryBuilder = "query-builder";
    private final String mTagOption = "option";
    
    private final String mTagDecisionMakerConfig = "DecisionMakerConfig";
    private final String mTagDecisionMakerClass = "decision-maker-class";
    
    private final String mTagEuidGeneratorConfig = "EuidGeneratorConfig";
    private final String mTagEuidGeneratorClass = "euid-generator-class";

    private final String mTagParameters = "parameters";
    private final String mTagParameter = "parameter";
    private final String mTagParameterName = "parameter-name";
    private final String mTagParameterType = "parameter-type";
    private final String mTagParameterValue = "parameter-value";
    
    private boolean mModified = false;
    

    public static String IDLENGTH= "IdLength";
    public static String CHECKSUMLENGTH= "ChecksumLength";
    public static String CHUNKSIZE= "ChunkSize";
    

    public static String ONEEXACTMATCH= "OneExactMatch";
    public static String SAMESYSTEMMATCH= "SameSystemMatch";
    public static String DUPLICATETHRESHOLD = "DuplicateThreshold";
    public static String MATCHTHRESHOLD = "MatchThreshold";
    

    public MasterControllerConfig mMasterControllerConfig = new MasterControllerConfig();
    public DecisionMakerConfig mDecisionMakerConfig = new DecisionMakerConfig();
    public EuidGeneratorConfig mEuidGeneratorConfig = new EuidGeneratorConfig();
    
    /**
    *@param boolean flag
    */
    public void setModified(boolean flag) {
        mModified = flag;
    }
    
    /**
    *@return boolean flag
    */
    public boolean isModified() {
        return mModified;
    }
    
    private void parseQueryBuilder(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem("name");
            String queryBuilderName = tmp.getNodeValue();
        }
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String name = ((Element) nl.item(i)).getTagName();
                if (mTagOption.equals(name)) {
                    //parseOption(nl.item(i));
                }
            }
        }

    }
    
    private void parseExecuteMatch(Node node) {
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String name = ((Element) nl.item(i)).getTagName();
                if (mTagQueryBuilder.equals(name)) {
                    parseQueryBuilder(nl.item(i));
                }
            }
        }
    }

    public void setTransaction (String transaction) {
        mMasterControllerConfig.setTransaction(transaction);
    }

    public String getTransaction () {
        return mMasterControllerConfig.getTransaction();
    }
    
    class MasterControllerConfig {
        String moduleName = "MasterController";
        String parserClass = "com.sun.mdm.index.configurator.impl.master.MasterControllerConfiguration";
        String updateMode = "Pessimistic";
        String mergedRecordUpdate = "Disabled";
        String transaction = "LOCAL"; // CONTAINER, BEAN or LOCAL
        class ExecuteMatch {
            String queryBuilderName;  // e.g. ALPHA-SEARCH, ALPHA-SEARCH, PHONETIC-SEARCH
            String optionKey = "key";
            String optionValue = "value";        
        }
        
        public String getModuleName () {
            return moduleName;
        }
        
        public String getParserClass() {
            return parserClass;
        }
        
        public String getUpdateMode () {
            return updateMode;
        }
        
        public String getMergedRecordUpdate() {
            return mergedRecordUpdate;
        }
        
        public String getTransaction () {
            return transaction;
        }
        
        public void setTransaction (String transaction) {
            this.transaction = transaction;
        }

    }

    private void parseMasterControllerConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mMasterControllerConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mMasterControllerConfig.parserClass = tmp.getNodeValue();
            }
        }
        
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String name = ((Element) nl.item(i)).getTagName();
                if (mTagUpdateMode.equals(name)) {
                    mMasterControllerConfig.updateMode = Utils.getStrElementValue(nl.item(i));
                } else if (mTagMergedRecordUpdate.equals(name)) {
                    mMasterControllerConfig.mergedRecordUpdate = Utils.getStrElementValue(nl.item(i));
                } else if (mTagExecuteMatch.equals(name)) {
                    parseExecuteMatch(nl.item(i));
                } else if (mTagTransaction.equals(name)) {
                    mMasterControllerConfig.transaction = Utils.getStrElementValue(nl.item(i));
                }
            }
        }
    }
    
    /*
    <EuidGeneratorConfig module-name="EuidGenerator" parser-class="com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration">
        <euid-generator-class>com.sun.mdm.index.idgen.impl.DefaultEuidGenerator</euid-generator-class>
        <parameters>
            <parameter>
                <parameter-name>IdLength</parameter-name>
                <parameter-type>java.lang.Integer</parameter-type>
                <parameter-value>10</parameter-value>
            </parameter>
        </parameters>
    </EuidGeneratorConfig>
     */
    
    /*
    public class EuidGeneratorConfig {
        String euidGeneratorClass;
        ArrayList alParameters;

        public EuidGeneratorConfig(String euidGeneratorClass, ArrayList alParameters) {
            this.euidGeneratorClass = euidGeneratorClass;
            this.alParameters = alParameters;
        }
        
    }
    */

    class EuidGeneratorConfig {
        String moduleName = "EuidGenerator";
        String parserClass="com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration";
        String mEuidGeneratorClass = "com.sun.mdm.index.idgen.impl.DefaultEuidGenerator";
        ArrayList mAlParametersEuidGeneratorConfig = new ArrayList();
        
        public String getModuleName () {
            return moduleName;
        }
        
        public String getParserClass() {
            return parserClass;
        }
        
        public String getEuidGeneratorClass () {
            return mEuidGeneratorClass;
        }
        
        public ArrayList getParameters() {
            return mAlParametersEuidGeneratorConfig;
        }
    }
    
    private void parseEuidGeneratorConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mEuidGeneratorConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mEuidGeneratorConfig.parserClass = tmp.getNodeValue();
            }
        }

        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagEuidGeneratorClass.equals(name2)) {
                    mEuidGeneratorConfig.mEuidGeneratorClass = Utils.getStrElementValue(nl2.item(i2));
                } else if (mTagParameters.equals(name2)) {
                    NodeList nl3 = nl2.item(i2).getChildNodes();
                    for (int i3 = 0; i3 < nl3.getLength(); i3++) {
                        if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                            String name3 = ((Element) nl3.item(i3)).getTagName();
                            if (mTagParameter.equals(name3)) {
                                Utils.Parameter p = Utils.parseParameter(nl3.item(i3));
                                mEuidGeneratorConfig.mAlParametersEuidGeneratorConfig.add(p);
                            }
                        }
                    }
                }
            }
        }
    }

    class DecisionMakerConfig {
        String moduleName = "DecisionMaker";
        String parserClass="com.sun.mdm.index.configurator.impl.decision.DecisionMakerConfiguration";
        String mDecisionMakerClass = "com.sun.mdm.index.decision.impl.DefaultDecisionMaker";
        ArrayList mAlParametersDecisionMakerConfig = new ArrayList();
        
        public String getModuleName () {
            return moduleName;
        }
        
        public String getParserClass() {
            return parserClass;
        }
        
        public String getDecisionMakerClass () {
            return mDecisionMakerClass;
        }
        
        public ArrayList getParameters() {
            return mAlParametersDecisionMakerConfig;
        }

    }
    
    private void parseDecisionMakerConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mDecisionMakerConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mDecisionMakerConfig.parserClass = tmp.getNodeValue();
            }
        }

        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagDecisionMakerClass.equals(name2)) {
                    this.mDecisionMakerConfig.mDecisionMakerClass = Utils.getStrElementValue(nl2.item(i2));
                } else if (mTagParameters.equals(name2)) {
                    NodeList nl3 = nl2.item(i2).getChildNodes();
                    for (int i3 = 0; i3 < nl3.getLength(); i3++) {
                        if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                            String name3 = ((Element) nl3.item(i3)).getTagName();
                            if (mTagParameter.equals(name3)) {
                                Utils.Parameter p = Utils.parseParameter(nl3.item(i3));
                                mDecisionMakerConfig.mAlParametersDecisionMakerConfig.add(p);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * parse
     * @param node Node
     */
    void parse(Node node) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl1 = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl1.getLength(); i++) {
                if (nl1.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl1.item(i);
                    break;
                }
            }

            if (null != element
                     && ((Element) element).getTagName().equals(mTagConfiguration)
                     && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (mTagMasterControllerConfig.equals(name)) {
                            parseMasterControllerConfig(nl1.item(i1));
                        } else if (mTagDecisionMakerConfig.equals(name)) {
                            parseDecisionMakerConfig(nl1.item(i1));
                        } else if (mTagEuidGeneratorConfig.equals(name)) {
                            parseEuidGeneratorConfig(nl1.item(i1));
                        }
                    }
                }
            }
        }
    }
    
    /**
     *@return String DecisionMakerConfig.mDecisionMakerClass
     */
    public String getDecisionMakerClass() {
        return this.mDecisionMakerConfig.mDecisionMakerClass;
    }
    
    /**
     *@return Utils.Parameter 
     */
    public Utils.Parameter getDecisionMakerConfigParameterByName(String name) {
        Utils.Parameter retP = null;
        for (int i=0; i < mDecisionMakerConfig.mAlParametersDecisionMakerConfig.size(); i++) {
            Utils.Parameter p = (Utils.Parameter) mDecisionMakerConfig.mAlParametersDecisionMakerConfig.get(i);
            if (p.getName().equals(name)) {
                retP = p;
                break;
            }
        }

        return retP;
    }
    
    /**
     *@return String DecisionMakerConfig.EuidGeneratorConfig.mEuidGeneratorClass
     */
    public String getEuidGeneratorClass() {
        return this.mEuidGeneratorConfig.mEuidGeneratorClass;
    }
    
    /**
     *@return Utils.Parameter 
     */
    public Utils.Parameter getEuidGeneratorConfigParameterByName(String name) {
        Utils.Parameter retP = null;
        for (int i=0; i < mEuidGeneratorConfig.mAlParametersEuidGeneratorConfig.size(); i++) {
            Utils.Parameter p = (Utils.Parameter) mEuidGeneratorConfig.mAlParametersEuidGeneratorConfig.get(i);
            if (p.getName().equals(name)) {
                retP = p;
                break;
            }
        }

        return retP;
    }
  
    
    /**
     *  return XML String for this MasterType
     *
     */
    
    public String getXMLString() {
        StringBuffer buffer = new StringBuffer();
        
        // @Todo change this hardcoded code to elements retrieved during parsing
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Utils.LINE);
        buffer.append("<Configuration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sbyn:SeeBeyond/eView/schema/master.xsd\">" + Utils.LINE);
        
        buffer.append(getMasterControllerConfigXMLString());
        buffer.append(getDecisionMakerConfigXMLString());
        buffer.append(getEuidGeneratorConfigXMLString());
        buffer.append(Utils.endTag(mTagConfiguration));        
        
        return buffer.toString();
    }

    /*
        <MasterControllerConfig module-name="MasterController" parser-class="com.sun.mdm.index.configurator.impl.master.MasterControllerConfiguration">
            <!-- update mode shall be Pessimistic to enable matching on updates (I1170) -->
            <update-mode>Pessimistic</update-mode>
            <merged-record-update>Disabled</merged-record-update>
            <execute-match>
                <query-builder name="BLOCKER-SEARCH">
                    <!-- example key/value initialization pair -->
                    <option key="key" value="value"/>
                </query-builder>
            </execute-match>
        </MasterControllerConfig>
     */
    
    private String getMasterControllerConfigXMLString() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagMasterControllerConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mMasterControllerConfig.getModuleName()));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mMasterControllerConfig.getParserClass()));
        bufSegment.append(">" + Utils.LINE);
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagUpdateMode) + mMasterControllerConfig.getUpdateMode() + Utils.endTag(mTagUpdateMode));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagMergedRecordUpdate) + mMasterControllerConfig.getMergedRecordUpdate() + Utils.endTag(mTagMergedRecordUpdate));
        
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagExecuteMatch));
        // @Todo --
        bufSegment.append(Utils.TAB3 + "<query-builder name=\"BLOCKER-SEARCH\">" + Utils.LINE);
        bufSegment.append(Utils.TAB4 + "<!-- example key/value initialization pair -->" + Utils.LINE);
        bufSegment.append(Utils.TAB4 + "<option key=\"key\" value=\"value\"/>" + Utils.LINE);
        bufSegment.append(Utils.TAB3 + "</query-builder>" + Utils.LINE);
        // --
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagExecuteMatch));
        
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagTransaction) + mMasterControllerConfig.getTransaction() + Utils.endTag(mTagTransaction));
        bufSegment.append(Utils.TAB + "</" + mTagMasterControllerConfig + ">" + Utils.LINE);
        
        return bufSegment.toString();
    }

    private String getParameterXML(Utils.Parameter parameter) {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagParameter));
        bufSegment.append(Utils.TAB4 + "<parameter-name>" + parameter.getName() + "</parameter-name>" + Utils.LINE);
        bufSegment.append(Utils.TAB4 + "<parameter-type>" + parameter.getType() + "</parameter-type>" + Utils.LINE);
        bufSegment.append(Utils.TAB4 + "<parameter-value>" + parameter.getValue() + "</parameter-value>" + Utils.LINE);
        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagParameter));
        return bufSegment.toString();
    }
    
    /*
    <DecisionMakerConfig module-name="DecisionMaker" parser-class="com.sun.mdm.index.configurator.impl.decision.DecisionMakerConfiguration">
        <decision-maker-class>com.sun.mdm.index.decision.impl.DefaultDecisionMaker</decision-maker-class>
        <parameters>
            <parameter>
                <!--        ** OneExactMatch **
                = This parameter specifies logic for assumed matches. If OneExactMatch is set to true
                = and there is more than one record above the match threshold, then none of the elements
                = are considered an assumed match and all are flagged as potential duplicates.
                -->
                <parameter-name>OneExactMatch</parameter-name>
                <parameter-type>java.lang.Boolean</parameter-type>
                <!-- flag all records considered an assumed match as potential duplicates -->
                <parameter-value>true</parameter-value>
            </parameter>
        </parameters>
    </DecisionMakerConfig>

     */
    private String getDecisionMakerConfigXMLString() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagDecisionMakerConfig);
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mDecisionMakerConfig.getModuleName()));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mDecisionMakerConfig.getParserClass()));
        bufSegment.append(">" + Utils.LINE);
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagDecisionMakerClass) + mDecisionMakerConfig.getDecisionMakerClass() + Utils.endTag(mTagDecisionMakerClass));
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagParameters));
        // get parameter block
        for ( int i = 0; i < mDecisionMakerConfig.getParameters().size(); i++) {
            Utils.Parameter parameter = (Utils.Parameter) mDecisionMakerConfig.getParameters().get(i);
            String parameterString = getParameterXML(parameter);
            bufSegment.append(parameterString);
        }
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagParameters));
        bufSegment.append(Utils.TAB + "</" + mTagDecisionMakerConfig + ">" + Utils.LINE);
        
        return bufSegment.toString();
    }

    /*
    <EuidGeneratorConfig module-name="EuidGenerator" parser-class="com.sun.mdm.index.configurator.impl.idgen.EuidGeneratorConfiguration">
        <euid-generator-class>com.sun.mdm.index.idgen.impl.DefaultEuidGenerator</euid-generator-class>
        <parameters>
            <parameter>
                <parameter-name>IdLength</parameter-name>
                <parameter-type>java.lang.Integer</parameter-type>
                <parameter-value>10</parameter-value>
            </parameter>
        </parameters>
    </EuidGeneratorConfig>

     */
    private String getEuidGeneratorConfigXMLString() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagEuidGeneratorConfig);
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mEuidGeneratorConfig.getModuleName()));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mEuidGeneratorConfig.getParserClass()));
        bufSegment.append(">" + Utils.LINE);
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagEuidGeneratorClass) + mEuidGeneratorConfig.getEuidGeneratorClass() + Utils.endTag(mTagEuidGeneratorClass));
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagParameters));
        // get parameter block
        for ( int i = 0; i < mEuidGeneratorConfig.getParameters().size(); i++) {
            Utils.Parameter parameter = (Utils.Parameter) mEuidGeneratorConfig.getParameters().get(i);
            String parameterString = getParameterXML(parameter);
            bufSegment.append(parameterString);
        }
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagParameters));
        bufSegment.append(Utils.TAB + "</" + mTagEuidGeneratorConfig + ">" + Utils.LINE);
        
        return bufSegment.toString();
    }

}
