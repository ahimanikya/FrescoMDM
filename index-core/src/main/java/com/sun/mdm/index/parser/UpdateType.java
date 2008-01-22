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
public class UpdateType {
    private final String mFmt = "                [UpdateType]";
    private final String mTagConfiguration = "Configuration";
    private final String mTagSurvivorHelperConfig = "SurvivorHelperConfig";
    private final String mTagHelperClass = "helper-class";
    private final String mTagDefaultSurvivorStrategy = "default-survivor-strategy";
    private final String mTagStrategyClass = "strategy-class";
    private final String mTagCandidateDefinitions = "candidate-definitions";
    private final String mTagCandidateField = "candidate-field";
    private final String mTagSurvivorStrategy = "survivor-strategy";
    //private final String mTagStrategyClass = "strategy-class";
    
    private final String mTagWeightedCalculator = "WeightedCalculator";
    private final String mTagDefaultParameters = "default-parameters";
    private final String mTagPreference = "preference";
    private final String mTagQuality = "quality";
    private final String mTagUtility = "utility";
    private final String mTagName = "name";    
    private final String mTagModuleName = "module-name";
    private final String mTagParserClass = "parser-class";
    
    private final String mTagParameters = "parameters";
    private final String mTagParameter = "parameter";
    private final String mTagParameterName = "parameter-name";
    private final String mTagParameterType = "parameter-type";
    private final String mTagParameterValue = "parameter-value";

    
    private final String mTagUpdateManagerConfig = "UpdateManagerConfig";
    private final String mTagEnterpriseMergePolicy = "EnterpriseMergePolicy";
    private final String mTagEnterpriseUnmergePolicy = "EnterpriseUnmergePolicy";
    private final String mTagEnterpriseUpdatePolicy = "EnterpriseUpdatePolicy";
    private final String mTagEnterpriseCreatePolicy = "EnterpriseCreatePolicy";
    private final String mTagSystemMergePolicy = "SystemMergePolicy";
    private final String mTagSystemUnmergePolicy = "SystemUnmergePolicy";
    private final String mTagUndoAssumeMatchPolicy = "UndoAssumeMatchPolicy";
    private final String mTagSkipUpdateIfNoChange = "SkipUpdateIfNoChange";

    
    private boolean mModified = false;
    private ArrayList mAlSourceSystems = new ArrayList();
    private SurvivorHelperConfig mSurvivorHelperConfig = new SurvivorHelperConfig();
    private UpdateManagerConfig mUpdateManagerConfig = new UpdateManagerConfig();
    private ArrayList mAlWeightedCalculator = new ArrayList();

    /**
     * Called by Application Editor when Source Systems changed
     *
     * @param ArrayList ret ArrayList
     */
    public void setSourceSystems(ArrayList alSourceSystems) {
           this.mAlSourceSystems = alSourceSystems;
    }
    
    /**
     * @return ArrayList alSourceSystems
     */
    public ArrayList getSourceSystems() {
           return this.mAlSourceSystems;
    }
    
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
                        if (mTagSurvivorHelperConfig.equals(name)) {
                            parseSurvivorHelperConfig(nl1.item(i1));
                        } else if (mTagWeightedCalculator.equals(name)) {
                            parseWeightedCalculator(nl1.item(i1));
                        } else if (mTagUpdateManagerConfig.equals(name)) {
                            parseUpdateManagerConfig(nl1.item(i1));
                        }
                    }
                }
            }
        }
    }
    /*
      <candidate-field name="Person.Ethnic">
        <survivor-strategy>
        <strategy-class>com.sun.mdm.index.survivor.impl.WeightedSurvivorStrategy</strategy-class>
          <parameters>
            <parameter>
              <parameter-name>ConfigurationModuleName</parameter-name>
              <parameter-type>java.lang.String</parameter-type>
              <parameter-value>SpecialWeights</parameter-value>
            </parameter>
          </parameters>
        </survivor-strategy>
      </candidate-field>

     */
    class SurvivorStrategy {
        String strategyClass = "com.sun.mdm.index.survivor.impl.WeightedSurvivorStrategy";
        ArrayList alParameters = new ArrayList(); // of Utils.Parameter
                
        void setStrategyClass(String strategyClass) {
            this.strategyClass = strategyClass;
        }
        
        String getStrategyClass() {
            return strategyClass;
        }
        
        void addParameter(Utils.Parameter parameter) {
            alParameters.add(parameter);
        }
    }

    class CandidateField {
        String fieldName;
        SurvivorStrategy survivorStrategy = null;
        
        CandidateField(String fieldName) {
            this.fieldName = fieldName;
        }
        
        void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
        
        String getFieldName() {
            return fieldName;
        }
        
        void setSurvivorStrategy(SurvivorStrategy survivorStrategy) {
            this.survivorStrategy = survivorStrategy;
        }
        
        SurvivorStrategy getSurvivorStrategy() {
            return survivorStrategy;
        }
        
    }
    
    void parseCandidateField(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        String name = "";
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagName);
            if (tmp != null) {
                name = tmp.getNodeValue();
            }           
        } else {
            return;
        }
        
        CandidateField candidateField = new CandidateField(name);
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                String value = Utils.getStrElementValue(nl2.item(i2));
                if (mTagSurvivorStrategy.equals(name2)) {
                    SurvivorStrategy survivorStrategy = new SurvivorStrategy();
                    parseSurvivorStrategy(nl2.item(i2), survivorStrategy);
                    candidateField.setSurvivorStrategy(survivorStrategy);
                }
            }
        }
        mSurvivorHelperConfig.alCandidateFields.add(candidateField);
    }
    
    void parseCandidateDefinitions(Node node) {
        //mSurvivorHelperConfig.alCandidateFields;
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagCandidateField.equals(name2)) {
                    parseCandidateField(nl2.item(i2));
               }
            }
        }
    }
    
    void parseSurvivorStrategy(Node node, SurvivorStrategy survivorStrategy) {
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagStrategyClass.equals(name2)) {
                    survivorStrategy.setStrategyClass(Utils.getStrElementValue(nl2.item(i2)));
                } else if (mTagParameters.equals(name2)) {
                    NodeList nl3 = nl2.item(i2).getChildNodes();
                    for (int i3 = 0; i3 < nl3.getLength(); i3++) {
                        if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                            String name3 = ((Element) nl3.item(i3)).getTagName();
                            if (mTagParameter.equals(name3)) {
                                Utils.Parameter p = Utils.parseParameter(nl3.item(i3));
                                survivorStrategy.addParameter(p);
                            }
                        }
                    }
                }
            }
        }

    }
    
    /*
	<SurvivorHelperConfig module-name="SurvivorHelper" parser-class="com.sun.mdm.index.configurator.impl.SurvivorHelperConfig">
		<helper-class>com.sun.mdm.index.survivor.impl.DefaultSurvivorHelper</helper-class>
		<default-survivor-strategy>
			<strategy-class>com.sun.mdm.index.survivor.impl.WeightedSurvivorStrategy</strategy-class>
			<parameters>
				<parameter>
					<parameter-name>ConfigurationModuleName</parameter-name>
					<parameter-type>java.lang.String</parameter-type>
					<parameter-value>WeightedSurvivorCalculator</parameter-value>
				</parameter>
			</parameters>
		</default-survivor-strategy>
		<candidate-definitions>
                    <candidate-field name="Prisoner.LName"/>
                    <candidate-field name="Prisoner.LName_Phon"/>
                    <candidate-field name="Prisoner.FName"/>
                    <candidate-field name="Prisoner.FName_Phon"/>
                    <candidate-field name="Prisoner.DOB"/>
                    <candidate-field name="Prisoner.Gender"/>
                    <candidate-field name="Prisoner.PrisonerAdded"/>
                    <candidate-field name="Prisoner.Alias[*].*"/>
                    <candidate-field name="Prisoner.Crime[*].*"/>
		</candidate-definitions>
	</SurvivorHelperConfig>
     */
    void parseSurvivorHelperConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mSurvivorHelperConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mSurvivorHelperConfig.parserClass = tmp.getNodeValue();
            }
        }
        
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                String value = Utils.getStrElementValue(nl2.item(i2));
                if (mTagHelperClass.equals(name2)) {
                    mSurvivorHelperConfig.helperClass = value;
                } else if (mTagDefaultSurvivorStrategy.equals(name2)) {
                    parseSurvivorStrategy(nl2.item(i2), mSurvivorHelperConfig.defaultSurvivorStrategy);
                } else if (mTagCandidateDefinitions.equals(name2)) {
                    parseCandidateDefinitions(nl2.item(i2));
                }
            }
        }

    }

    class SurvivorHelperConfig {
	String moduleName = "SurvivorHelper";
        String parserClass = "com.sun.mdm.index.configurator.impl.SurvivorHelperConfig";
        String helperClass = "com.sun.mdm.index.survivor.impl.DefaultSurvivorHelper";
        SurvivorStrategy defaultSurvivorStrategy = new SurvivorStrategy();
        ArrayList alCandidateFields = new ArrayList(); // of CandidateField
    }
    
    
    private WeightedCalculator.ParameterWeightedCalculator parseParameter(Node node, WeightedCalculator weightedCalculator) {
        String preference = null;
        String quality = null;
        String utility = null;
        
        NodeList nl4 = node.getChildNodes();
        for (int i4 = 0; i4 < nl4.getLength(); i4++) {
            if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                String name4 = ((Element) nl4.item(i4)).getTagName();
                if (mTagPreference.equals(name4)) {
                    preference = Utils.getStrElementValue(nl4.item(i4));
                    if (null == mAlSourceSystems) {
                        mAlSourceSystems = new ArrayList();
                    }
                    mAlSourceSystems.add(preference);
                } else if (mTagQuality.equals(name4)) {
                    quality = Utils.getStrElementValue(nl4.item(i4));
                } else if (mTagUtility.equals(name4)) {
                    utility = Utils.getStrElementValue(nl4.item(i4));
                }
            }
        }
        WeightedCalculator.ParameterWeightedCalculator parameter = weightedCalculator.createParameter(preference, quality, utility);
        return parameter;
    }

    private void parseWeightedCalculatorCandidateField(Node node, String candidateField, WeightedCalculator weightedCalculator) {
        WeightedCalculator.CandidateFieldWeightedCalculator candidateFieldWeightedCalculator = weightedCalculator.createCandidateField(candidateField);
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                if (mTagParameter.equals(name3)) {
                    candidateFieldWeightedCalculator.alParameters.add(parseParameter(nl3.item(i3), weightedCalculator));
                }
            }
        }
    }
    
    void parseWeightedCalculator(Node node) {
        //weightedCalculator
        WeightedCalculator weightedCalculator = new WeightedCalculator();
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                weightedCalculator.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                weightedCalculator.parserClass = tmp.getNodeValue();
            }
        }
        mAlWeightedCalculator.add(weightedCalculator);
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                String candidateField = mTagDefaultParameters;
                if (mTagCandidateField.equals(name2)) {
                    NamedNodeMap nnmC = ((Node) nl2.item(i2)).getAttributes();
                    if (nnmC != null) {
                        Node tmp = nnmC.getNamedItem(mTagName);
                        if (tmp != null) {
                            candidateField = tmp.getNodeValue();
                        }
                    }
                    parseWeightedCalculatorCandidateField(nl2.item(i2), candidateField, weightedCalculator); 
                } else if (mTagDefaultParameters.equals(name2)) {
                    parseWeightedCalculatorCandidateField(nl2.item(i2), candidateField, weightedCalculator); 
                }
            }
        }
    }
    
    /*
	<WeightedCalculator module-name="WeightedSurvivorCalculator" parser-class="com.sun.mdm.index.configurator.impl.WeightedCalculatorConfig">
		<default-parameters>
        		<parameter>
                                <preference>AAA</preference>
				<quality>MostRecentModified</quality>
				<utility>75.0</utility>
			</parameter>
		</default-parameters>
	</WeightedCalculator>	

     */
    class WeightedCalculator {
        String moduleName = "WeightedSurvivorCalculator";
        String parserClass = "com.sun.mdm.index.configurator.impl.WeightedCalculatorConfig";
        ArrayList alCandidateFields = new ArrayList(); // of candidate-field
        
        class CandidateFieldWeightedCalculator {
            String candidateField;
            ArrayList alParameters = new ArrayList(); // of Parameter
        }
        
        CandidateFieldWeightedCalculator createCandidateField(String candidateField) {
            CandidateFieldWeightedCalculator candidateFieldWeightedCalculator = new CandidateFieldWeightedCalculator();
            candidateFieldWeightedCalculator.candidateField = candidateField;
            alCandidateFields.add(candidateFieldWeightedCalculator);
            return candidateFieldWeightedCalculator;
        }
        
        class ParameterWeightedCalculator {
            String preference = ""; // participating source system
            String quality = ""; //"MostRecentModified";
            String utility = ""; //"75.0";
        }
        
        ParameterWeightedCalculator createParameter(String preference, String quality, String utility) {
            ParameterWeightedCalculator p = new ParameterWeightedCalculator();
            p.preference = preference;
            p.quality = quality;
            p.utility = utility;
            return p;
        }
    }
    
    void parseUpdateManagerConfig(Node node) {
        //mUpdateManagerConfig
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mUpdateManagerConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mUpdateManagerConfig.parserClass = tmp.getNodeValue();
            }
        }
        
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                String value = Utils.getStrElementValue(nl2.item(i2));                
                if (mTagEnterpriseMergePolicy.equals(name2)) {
                    mUpdateManagerConfig.enterpriseMergePolicy = value;
                } else if (mTagEnterpriseUnmergePolicy.equals(name2)) {
                    mUpdateManagerConfig.enterpriseUnmergePolicy = value;
                } else if (mTagEnterpriseUpdatePolicy.equals(name2)) {
                    mUpdateManagerConfig.enterpriseUpdatePolicy = value;
                } else if (mTagEnterpriseCreatePolicy.equals(name2)) {
                    mUpdateManagerConfig.enterpriseCreatePolicy = value;
                } else if (mTagSystemMergePolicy.equals(name2)) {
                    mUpdateManagerConfig.systemMergePolicy = value;
                } else if (mTagSystemUnmergePolicy.equals(name2)) {
                    mUpdateManagerConfig.systemUnmergePolicy = value;
                } else if (mTagUndoAssumeMatchPolicy.equals(name2)) {
                    mUpdateManagerConfig.undoAssumeMatchPolicy = value;
                } else if (mTagSkipUpdateIfNoChange.equals(name2)) {
                    mUpdateManagerConfig.skipUpdateIfNoChange = value;
                }
            }
        }
    }
    
    /*
	<UpdateManagerConfig module-name="UpdateManager" parser-class="com.sun.mdm.index.configurator.impl.UpdateManagerConfig">
		<EnterpriseMergePolicy></EnterpriseMergePolicy>
		<EnterpriseUnmergePolicy></EnterpriseUnmergePolicy>
		<EnterpriseUpdatePolicy></EnterpriseUpdatePolicy>
		<EnterpriseCreatePolicy></EnterpriseCreatePolicy>
		<SystemMergePolicy></SystemMergePolicy>
		<SystemUnmergePolicy></SystemUnmergePolicy>
		<UndoAssumeMatchPolicy></UndoAssumeMatchPolicy>
		<SkipUpdateIfNoChange>true</SkipUpdateIfNoChange>
	</UpdateManagerConfig>
     */
    class UpdateManagerConfig {
        String moduleName = "UpdateManager";
        String parserClass = "com.sun.mdm.index.configurator.impl.UpdateManagerConfig";
        String enterpriseMergePolicy = "";
        String enterpriseUnmergePolicy = "";
        String enterpriseUpdatePolicy = "";
        String enterpriseCreatePolicy = "";
        String systemMergePolicy = "";
        String systemUnmergePolicy = "";
        String undoAssumeMatchPolicy = "";
        String skipUpdateIfNoChange = "true";
    }

    CandidateField getCandidateFieldByName(String fieldName) {
        CandidateField candidateField = null;
        ArrayList alCandidateFields = mSurvivorHelperConfig.alCandidateFields;
        
        for (int j=0; j < alCandidateFields.size(); j++) {
            candidateField = (CandidateField) alCandidateFields.get(j);
            if (candidateField.getFieldName().equals(fieldName)) {
                break;
            } else {
                candidateField = null;
            }
        }
        return candidateField;
    }
    
    private String getCandidateDefinitionsXML(ArrayList alCandidateFieldsPerObject) {
        //get this segment from ConfigGenerator
        ArrayList alCandidateFields = mSurvivorHelperConfig.alCandidateFields;
        CandidateField candidateField;
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagCandidateDefinitions));
        for (int i=0; i < alCandidateFieldsPerObject.size(); i++) {
            String fieldName = (String) alCandidateFieldsPerObject.get(i);
            candidateField = getCandidateFieldByName(fieldName);
            if (candidateField != null) {
                    bufSegment.append(Utils.TAB3 + "<" + mTagCandidateField);
                    bufSegment.append(Utils.quoteAttribute(mTagName, candidateField.getFieldName()));
                    bufSegment.append("/>" + Utils.LINE);

                    SurvivorStrategy survivorStrategy = candidateField.getSurvivorStrategy();
                    if (survivorStrategy != null) {
                        String strategyClass = survivorStrategy.getStrategyClass();
                        bufSegment.append(Utils.TAB4 + Utils.startTag(mTagSurvivorStrategy));
                        bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagStrategyClass) + mSurvivorHelperConfig.defaultSurvivorStrategy.getStrategyClass() + Utils.endTag(mTagStrategyClass));
                        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagParameters));
                        ArrayList alParameters = mSurvivorHelperConfig.defaultSurvivorStrategy.alParameters;
                        for (int k=0; k < alParameters.size(); k++) {
                            Utils.Parameter p = (Utils.Parameter) alParameters.get(k);
                            bufSegment.append(Utils.TAB6 + Utils.startTag(mTagParameter));
                            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagParameterName) + p.name + Utils.endTag(mTagParameterName));
                            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagParameterType) + p.type + Utils.endTag(mTagParameterType));
                            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagParameterValue) + p.value + Utils.endTag(mTagParameterValue));
                            bufSegment.append(Utils.TAB6 + Utils.endTag(mTagParameter));
                        }
                        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagParameters));
                        //--mSurvivorHelperConfig.defaultSurvivorStrategy.alParameters
                        bufSegment.append(Utils.TAB4 + Utils.endTag(mTagSurvivorStrategy));
                    }
            } else {
                // add it to mSurvivorHelperConfig.alCandidateFields
                candidateField = new CandidateField(fieldName);
                mSurvivorHelperConfig.alCandidateFields.add(candidateField);
                bufSegment.append(Utils.TAB3 + "<" + mTagCandidateField);
                bufSegment.append(Utils.quoteAttribute(mTagName, fieldName));
                bufSegment.append("/>" + Utils.LINE);

            }

        }
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagCandidateDefinitions));
        return bufSegment.toString();
    }
    
    private String getSurvivorHelperConfigXMLString(ArrayList alCandidateFieldsPerObject) {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagSurvivorHelperConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mSurvivorHelperConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mSurvivorHelperConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagHelperClass) + mSurvivorHelperConfig.helperClass + Utils.endTag(mTagHelperClass));
        
        //mSurvivorHelperConfig.defaultSurvivorStrategy --
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagDefaultSurvivorStrategy));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagStrategyClass) + mSurvivorHelperConfig.defaultSurvivorStrategy.getStrategyClass() + Utils.endTag(mTagStrategyClass));
        //mSurvivorHelperConfig.defaultSurvivorStrategy.alParameters --
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagParameters));
        ArrayList alParameters = mSurvivorHelperConfig.defaultSurvivorStrategy.alParameters;
        for (int i=0; i < alParameters.size(); i++) {
            Utils.Parameter p = (Utils.Parameter) alParameters.get(i);
            bufSegment.append(Utils.TAB4 + Utils.startTag(mTagParameter));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagParameterName) + p.name + Utils.endTag(mTagParameterName));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagParameterType) + p.type + Utils.endTag(mTagParameterType));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagParameterValue) + p.value + Utils.endTag(mTagParameterValue));
            bufSegment.append(Utils.TAB4 + Utils.endTag(mTagParameter));
        }
        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagParameters));
        //--mSurvivorHelperConfig.defaultSurvivorStrategy.alParameters
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagDefaultSurvivorStrategy));
        //--mSurvivorHelperConfig.defaultSurvivorStrategy
        
        // <candidate-definitions>
        bufSegment.append(getCandidateDefinitionsXML(alCandidateFieldsPerObject));
        
        bufSegment.append(Utils.TAB + "</" + mTagSurvivorHelperConfig + ">" + Utils.LINE);
        
        return bufSegment.toString();
    }
    
    String getWeightedCalculatorXMLString(WeightedCalculator weightedCalculator) {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagWeightedCalculator);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, weightedCalculator.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, weightedCalculator.parserClass));
        bufSegment.append(">" + Utils.LINE);
        
        ArrayList alCandidateFields = weightedCalculator.alCandidateFields;
        for (int i=0; i < alCandidateFields.size(); i++) {
            WeightedCalculator.CandidateFieldWeightedCalculator candidateFieldWeightedCalculator = (WeightedCalculator.CandidateFieldWeightedCalculator) alCandidateFields.get(i);
            if (candidateFieldWeightedCalculator.candidateField.equals(mTagDefaultParameters)) {
                //<default-parameters>
                bufSegment.append(Utils.TAB2 + Utils.startTag(mTagDefaultParameters));
            } else {
                // mTagCandidateField
                bufSegment.append(Utils.TAB2 + "<" + mTagCandidateField);
                bufSegment.append(Utils.quoteAttribute(mTagName, candidateFieldWeightedCalculator.candidateField));
                bufSegment.append(">" + Utils.LINE);
            }
            ArrayList alParameters = candidateFieldWeightedCalculator.alParameters;
            for (int j=0; j < alParameters.size(); j++) {
                WeightedCalculator.ParameterWeightedCalculator p = (WeightedCalculator.ParameterWeightedCalculator) alParameters.get(j);
                bufSegment.append(Utils.TAB3 + Utils.startTag(mTagParameter));
                bufSegment.append(Utils.TAB4 + Utils.startTagNoLine(mTagQuality) + p.quality + Utils.endTag(mTagQuality));
                if (p.preference != null) {
                    bufSegment.append(Utils.TAB4 + Utils.startTagNoLine(mTagPreference) + p.preference + Utils.endTag(mTagPreference));
                }
                bufSegment.append(Utils.TAB4 + Utils.startTagNoLine(mTagUtility) + p.utility + Utils.endTag(mTagUtility));
                bufSegment.append(Utils.TAB3 + Utils.endTag(mTagParameter));
            }
            if (candidateFieldWeightedCalculator.candidateField.equals(mTagDefaultParameters)) {
                bufSegment.append(Utils.TAB2 + Utils.endTag(mTagDefaultParameters));
            } else {
                bufSegment.append(Utils.TAB2 + Utils.endTag(mTagCandidateField));
            }
        }
        bufSegment.append(Utils.TAB + "</" + mTagWeightedCalculator + ">" + Utils.LINE);
        return bufSegment.toString();
    }
    
    String getUpdateManagerConfigXMLString() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagUpdateManagerConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mUpdateManagerConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mUpdateManagerConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagEnterpriseMergePolicy) + mUpdateManagerConfig.enterpriseMergePolicy + Utils.endTag(mTagEnterpriseMergePolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagEnterpriseUnmergePolicy) + mUpdateManagerConfig.enterpriseUnmergePolicy + Utils.endTag(mTagEnterpriseUnmergePolicy));  
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagEnterpriseUpdatePolicy) + mUpdateManagerConfig.enterpriseUpdatePolicy + Utils.endTag(mTagEnterpriseUpdatePolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagEnterpriseCreatePolicy) + mUpdateManagerConfig.enterpriseCreatePolicy + Utils.endTag(mTagEnterpriseCreatePolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagSystemMergePolicy) + mUpdateManagerConfig.systemMergePolicy + Utils.endTag(mTagSystemMergePolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagSystemUnmergePolicy) + mUpdateManagerConfig.systemUnmergePolicy + Utils.endTag(mTagSystemUnmergePolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagUndoAssumeMatchPolicy) + mUpdateManagerConfig.undoAssumeMatchPolicy + Utils.endTag(mTagUndoAssumeMatchPolicy));
        bufSegment.append(Utils.TAB2 + Utils.startTagNoLine(mTagSkipUpdateIfNoChange) + mUpdateManagerConfig.skipUpdateIfNoChange + Utils.endTag(mTagSkipUpdateIfNoChange));
       
        bufSegment.append(Utils.TAB + "</" + mTagUpdateManagerConfig + ">" + Utils.LINE);
        return bufSegment.toString();
    }
    
    /**
     *  return XML String for this UpdateType
     *
     */
    
    public String getXMLString(ArrayList alCandidateFieldsPerObject) {
        StringBuffer buffer = new StringBuffer();
        
        // @Todo change this hardcoded code to elements retrieved during parsing
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Utils.LINE);
        buffer.append("<Configuration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schema/update.xsd\">" + Utils.LINE);
        
        buffer.append(getSurvivorHelperConfigXMLString(alCandidateFieldsPerObject));
        for (int i=0; i < mAlWeightedCalculator.size(); i++) {
            WeightedCalculator weightedCalculator = (WeightedCalculator) mAlWeightedCalculator.get(i);
            buffer.append(getWeightedCalculatorXMLString(weightedCalculator));
        }
        buffer.append(getUpdateManagerConfigXMLString());
        buffer.append(Utils.endTag(mTagConfiguration));        
        
        return buffer.toString();
    }
    
    /* 
     *Update referenced field when it is changed from OBject Definition
     *@param fieldNamePath
     */
    public boolean updateReferencedField(String oldName, String newName, String oldNodeNameUpdateSub, String newNodeNameUpdateSub) {
        boolean bRet = false;
        for (int i=0; i<mSurvivorHelperConfig.alCandidateFields.size(); i++) {
            CandidateField candidateField = (CandidateField) mSurvivorHelperConfig.alCandidateFields.get(i);
            if (candidateField.fieldName.equals(oldName)) {
                candidateField.fieldName = newName;
                bRet = true;
            }
        }
        
        for (int l=0; l<mAlWeightedCalculator.size(); l++) {
            WeightedCalculator weightedCalculator = (WeightedCalculator) mAlWeightedCalculator.get(l);
            ArrayList alCandidateFields = weightedCalculator.alCandidateFields;
            for (int m=0; m<alCandidateFields.size(); m++) {
                WeightedCalculator.CandidateFieldWeightedCalculator candidateFieldWeightedCalculator = (WeightedCalculator.CandidateFieldWeightedCalculator) alCandidateFields.get(m);
                if (candidateFieldWeightedCalculator.candidateField.equals(oldName)) {
                    candidateFieldWeightedCalculator.candidateField = newName;
                    bRet = true;
                } else {
                    String oldValue = candidateFieldWeightedCalculator.candidateField;
                    int index = oldValue.indexOf(oldNodeNameUpdateSub);
                    if (index >= 0) {
                        bRet = true;
                        String newValue = oldValue.replaceAll(oldName, newNodeNameUpdateSub);
                        candidateFieldWeightedCalculator.candidateField = newValue;
                    }
                }
            }
        }
        return bRet;
    }
    
    /* 
     *Remove referenced field when it is deleted from OBject Definition
     *@param fieldNamePath
     */
    public boolean removeReferencedField(String fieldNamePath) {
        boolean bRet = false;
        for (int i=0; i<mSurvivorHelperConfig.alCandidateFields.size(); i++) {
            CandidateField candidateField = (CandidateField) mSurvivorHelperConfig.alCandidateFields.get(i);
            if (candidateField.fieldName.equals(fieldNamePath)) {
                mSurvivorHelperConfig.alCandidateFields.remove(i);
                bRet = true;
            }
        }
        
        for (int l=0; l<mAlWeightedCalculator.size(); l++) {
            WeightedCalculator weightedCalculator = (WeightedCalculator) mAlWeightedCalculator.get(l);
            ArrayList alCandidateFields = weightedCalculator.alCandidateFields;
            for (int m=alCandidateFields.size()-1; m >= 0; m--) {
                WeightedCalculator.CandidateFieldWeightedCalculator candidateFieldWeightedCalculator = (WeightedCalculator.CandidateFieldWeightedCalculator) alCandidateFields.get(m);
                if (candidateFieldWeightedCalculator.candidateField.equals(fieldNamePath)) {
                    alCandidateFields.remove(m);
                    bRet = true;
                }
            }        
        }
        return bRet;
    }
}
