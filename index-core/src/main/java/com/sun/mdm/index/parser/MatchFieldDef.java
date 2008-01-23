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
import org.w3c.dom.DOMException;


/**
 * @author gzheng
 * @version
 */
public class MatchFieldDef {
    private final String mFmt = "                [MatchField]";
    private final String mTagConfiguration = "Configuration";
    private final String mTagMEFAConfig = "MEFAConfig";
    private final String mTagBlockPicker = "block-picker";
    private final String mTagPassController = "pass-controller";
    private final String mTagStandardizerApi = "standardizer-api";
    private final String mTagStandardizerConfig = "standardizer-config";
    private final String mTagMatcherApi = "matcher-api";
    private final String mTagMatcherConfig = "matcher-config";
    private final String mTagClassName = "class-name";
    
    private final String mTagPhoneticEncodersConfig = "PhoneticEncodersConfig";
    // Standardization
    private final String mTagStandardizationConfig = "StandardizationConfig";
    private final String mTagStandardizeSystemObject = "standardize-system-object";
    private final String mTagSystemObjectName = "system-object-name";
    private final String mTagStructuresToNormalize = "structures-to-normalize";
    private final String mTagUnnormalizedSourceFields = "unnormalized-source-fields";
	private final String mTagSourceMapping = "source-mapping";
	private final String mTagUnnormalizedSourceFieldName = "unnormalized-source-field-name";
	private final String mTagStandardizedObjectFieldId = "standardized-object-field-id";
	private final String mTagNormalizationTargets = "normalization-targets";
	private final String mTagTargetMapping = "target-mapping";
	private final String mTagStandardizedTargetFieldName = "standardized-target-field-name";

    private final String mTagFreeFormTextsToStandardize = "free-form-texts-to-standardize";
    private final String mTagGroup = "group";
    private final String mTagStandardizationType = "standardization-type";
    private final String mTagDomainSelector = "domain-selector";
    private final String mTagUnstandardizedSourceFields = "unstandardized-source-fields";
    private final String mTagUnstandardizedSourceFieldName = "unstandardized-source-field-name";
    private final String mTagStandardizationTargets = "standardization-targets";
    
    private final String mTagPhoneticizeFields = "phoneticize-fields";
    private final String mTagPhoneticizeField = "phoneticize-field";
    private final String mTagUnphoneticizedSourceFieldName = "unphoneticized-source-field-name";
    private final String mTagPhoneticizedTargetFieldName = "phoneticized-target-field-name";
    private final String mTagEncodingType = "encoding-type";
    
    private final int iNormalizationGroup = 0;
    private final int iFreeFormGroup = iNormalizationGroup + 1;
    
    private final String mTagLocaleMaps = "locale-maps";
    private final String mTagLocaleFieldName = "locale-field-name";
    private final String mTagLocaleCodes = "locale-codes";
    private final String mTagValue = "value";
    private final String mTagLocale = "locale";
    
    // Matching
    private final String mTagMatchingConfig = "MatchingConfig";
    private final String mTagMatchSystemObject = "match-system-object";
    private final String mTagObjectName = "object-name";
    private final String mTagMatchColumns = "match-columns";
    private final String mTagMatchColumn = "match-column";
    private final String mTagColumnName = "column-name";
    private final String mTagMatchType = "match-type";
    private final String mTagModuleName = "module-name";
    private final String mTagParserClass = "parser-class";
    private final String mTagEncoder = "encoder";
    private final String mTagEncoderImplementationClass = "encoder-implementation-class";
    
    public static final String MULTIPLE_DOMAIN_SELECTOR = "com.sun.mdm.index.matching.impl.MultiDomainSelector";
    public static final String SINGLE_DOMAIN_SELECTOR_AU = "com.sun.mdm.index.matching.impl.SingleDomainSelectorAU";
    public static final String SINGLE_DOMAIN_SELECTOR_FR = "com.sun.mdm.index.matching.impl.SingleDomainSelectorFR";
    public static final String SINGLE_DOMAIN_SELECTOR_UK = "com.sun.mdm.index.matching.impl.SingleDomainSelectorUK";
    public static final String SINGLE_DOMAIN_SELECTOR_US = "com.sun.mdm.index.matching.impl.SingleDomainSelectorUS";
    
    private boolean mModified = false;
    
    private StandardizationConfig mStandardizationConfig = new StandardizationConfig();
    private MEFAConfig mMEFAConfig = new MEFAConfig();    
    private MatchingConfig mMatchingConfig = new MatchingConfig();
    private PhoneticEncodersConfig mPhoneticEncodersConfig;
    
    private void parseSourceMapping(Node node, NormalizationGroup group) {
        NodeList nl7 = node.getChildNodes();
        for (int i7 = 0; i7 < nl7.getLength(); i7++) {
            if (nl7.item(i7).getNodeType() == Node.ELEMENT_NODE) {
                String name7 = ((Element) nl7.item(i7)).getTagName();
                String value = Utils.getStrElementValue(nl7.item(i7));
                if (mTagStandardizedObjectFieldId.equals(name7)) {
                    group.setUnnormalizedFieldId(value);
                } else if (mTagUnnormalizedSourceFieldName.equals(name7)) {
                    group.setUnnormalizedSourceFieldName(value);
                }
            }
        }
    }

    //UnnormalizedSourceFields
    private void parseUnnormalizedSourceFields(Node node, NormalizationGroup group) {
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String name6 = ((Element) nl6.item(i6)).getTagName();
                if (mTagSourceMapping.equals(name6)) {
                    parseSourceMapping(nl6.item(i6), group);
                }
            }
        }
    }
    
    private void parseNormalizationTargets(Node node, NormalizationGroup group) {
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String name6 = ((Element) nl6.item(i6)).getTagName();
                if (mTagTargetMapping.equals(name6)) {
                    TargetMapping targetMapping = parseTargetMapping(nl6.item(i6));
                    if (targetMapping != null) {
                        group.setNormalizedFieldId(targetMapping.getFieldId());
                        group.setNormalizedSourceFieldName(targetMapping.getFieldName());
                    }
                }
            }
        }
    }
    
    // For Standardization section
    private void parseUnstandardizedSourceFields(Node node, Group group) {
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String name6 = ((Element) nl6.item(i6)).getTagName();
                if (mTagUnstandardizedSourceFieldName.equals(name6)) {
                    String fieldName = Utils.getStrElementValue(nl6.item(i6));
                    group.addUnstandardizedSourceField(fieldName);
                }
            }
        }
    }
    
    private TargetMapping parseTargetMapping(Node node) {
        TargetMapping targetMapping = null;
        
        NodeList nl7 = node.getChildNodes();
        for (int i7 = 0; i7 < nl7.getLength(); i7++) {
            if (nl7.item(i7).getNodeType() == Node.ELEMENT_NODE) {
                String name7 = ((Element) nl7.item(i7)).getTagName();
                String ss = Utils.getStrElementValue(nl7.item(i7));
                if (mTagStandardizedObjectFieldId.equals(name7)) {
                    if (targetMapping == null) {
                        targetMapping = new TargetMapping();
                    }
                    targetMapping.fieldId = ss;
                }
                if (mTagStandardizedTargetFieldName.equals(name7)) {
                    if (targetMapping == null) {
                        targetMapping = new TargetMapping();
                    }
                    targetMapping.fieldName = ss;
                }
            }
        }
        return targetMapping;
    }

    private void parseStandardizationTargets(Node node, Group group) {
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String name6 = ((Element) nl6.item(i6)).getTagName();
                if (mTagTargetMapping.equals(name6)) {
                    group.addTargetMapping(parseTargetMapping(nl6.item(i6)));
                }
            }
        }
    }
    
    private LocaleCode parseLocaleCodes(Node node) {
        LocaleCode localeCode = null;
        
        NodeList nl7 = node.getChildNodes();
        for (int i7 = 0; i7 < nl7.getLength(); i7++) {
            if (nl7.item(i7).getNodeType() == Node.ELEMENT_NODE) {
                String name7 = ((Element) nl7.item(i7)).getTagName();
                String ss = Utils.getStrElementValue(nl7.item(i7));
                if (mTagValue.equals(name7)) {
                    if (localeCode == null) {
                        localeCode = new LocaleCode();
                    }
                    localeCode.value = ss;
                }
                if (mTagLocale.equals(name7)) {
                    if (localeCode == null) {
                        localeCode = new LocaleCode();
                    }
                    localeCode.locale = ss;
                }
            }
        }
        return localeCode;
    }
    
    private void parseLocaleMaps(Node node, Group group) {
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String name6 = ((Element) nl6.item(i6)).getTagName();
                if (mTagLocaleCodes.equals(name6)) {
                    group.addLocaleCode(parseLocaleCodes(nl6.item(i6)));
                }
            }
        }
    }
    
    
    private PhoneticizeField parsePhoneticizeField(Node node) {
        String sourceFieldName = ""; // Company.CompanyName_Name
        String targetFieldId = "";   // Company.CompanyName_NamePhon
        String encodingType = "";    // NYSIIS
       
        NodeList nl7 = node.getChildNodes();
        for (int i7 = 0; i7 < nl7.getLength(); i7++) {
            if (nl7.item(i7).getNodeType() == Node.ELEMENT_NODE) {
                String name7 = ((Element) nl7.item(i7)).getTagName();
                String value = Utils.getStrElementValue(nl7.item(i7));
                if (mTagPhoneticizedTargetFieldName.equals(name7)) {
                    targetFieldId = value;
                } else if (mTagUnphoneticizedSourceFieldName.equals(name7)) {
                    sourceFieldName = value;
                } else if (mTagEncodingType.equals(name7)) {
                    encodingType = value;
                }
            }
        }
        PhoneticizeField phoneticizeField = new PhoneticizeField(sourceFieldName, targetFieldId, encodingType);

        return phoneticizeField;
    }

    private void parsePhoneticizeFields(Node node) {
        NodeList nl5 = node.getChildNodes();
        for (int i5 = 0; i5 < nl5.getLength(); i5++) {
            if (nl5.item(i5).getNodeType() == Node.ELEMENT_NODE) {
                String name5 = ((Element) nl5.item(i5)).getTagName();
                if (mTagPhoneticizeField.equals(name5)) {
                    PhoneticizeField phoneticizeField = parsePhoneticizeField(nl5.item(i5));
                    if (phoneticizeField != null) {
                        mStandardizationConfig.standardizeSystemObject.addPhoneticizeField(phoneticizeField);
                    }
                }
            }
        }
    }
    
    private void parseGroup(int iGroup, Node node) {
        Group group = null;
        
        if (iGroup == iNormalizationGroup) {
            // mStandardizationConfig.standardizeSystemObject.alNormalizationGroups
            group = new NormalizationGroup();
            mStandardizationConfig.standardizeSystemObject.alNormalizationGroups.add(group);
        } else if (iGroup == iFreeFormGroup) {
            // mStandardizationConfig.standardizeSystemObject.alFreeFormGroups
            group = new FreeFormGroup();
            mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.add(group);
        }
        
        //mTagStandardizationType
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node typeNode = nnm.getNamedItem(mTagStandardizationType);
            Node domainSelectorNode = nnm.getNamedItem(mTagDomainSelector);
            try {
                if (typeNode != null) {
                    group.setStandardizationType(typeNode.getNodeValue());
                }
                if (domainSelectorNode != null) {
                    group.setDomainSelector(domainSelectorNode.getNodeValue());
                }
            } catch (DOMException ex) {
            }
        }
        NodeList nl5 = node.getChildNodes();
        for (int i5 = 0; i5 < nl5.getLength(); i5++) {
            if (nl5.item(i5).getNodeType() == Node.ELEMENT_NODE) {
                String name5 = ((Element) nl5.item(i5)).getTagName();
                if (mTagUnnormalizedSourceFields.equals(name5)) {
                    parseUnnormalizedSourceFields(nl5.item(i5), (NormalizationGroup) group);
                } else if (mTagNormalizationTargets.equals(name5)) {
                    parseNormalizationTargets(nl5.item(i5), (NormalizationGroup) group);
                } else if (mTagUnstandardizedSourceFields.equals(name5)) {
                    parseUnstandardizedSourceFields(nl5.item(i5), group);
                } else if (mTagStandardizationTargets.equals(name5)) {
                    parseStandardizationTargets(nl5.item(i5), group);
                } else if (mTagLocaleMaps.equals(name5)) {
                    parseLocaleMaps(nl5.item(i5), group);
                } else if (mTagLocaleFieldName.equals(name5)) {
                    String lFieldName = Utils.getStrElementValue(nl5.item(i5));
                    group.setLocaleFieldName(lFieldName);
                }
            }
        }
    }

    /**
     * parse
     * @param node Node
     */
    private void parseStandardizeSystemObject(Node node) {
        // mStandardizationConfig.standardizeSystemObject
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                int iGroup = -1;
                if (mTagSystemObjectName.equals(name3)) {
                    mStandardizationConfig.standardizeSystemObject.systemObjectName = Utils.getStrElementValue(nl3.item(i3));
                } else if (mTagStructuresToNormalize.equals(name3)) {
                    iGroup = iNormalizationGroup;
                } else if (mTagFreeFormTextsToStandardize.equals(name3)) {
                    iGroup = iFreeFormGroup;
                } else if (mTagPhoneticizeFields.equals(name3)) {
                    parsePhoneticizeFields(nl3.item(i3));
                }
                if (iGroup != -1) {
                    NodeList nl4 = nl3.item(i3).getChildNodes();
                    for (int i4 = 0; i4 < nl4.getLength(); i4++) {
                        if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                            String name4 = ((Element) nl4.item(i4)).getTagName();
                            if (mTagGroup.equals(name4)) {
                                parseGroup(iGroup, nl4.item(i4));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void parseStandardizationConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node moduleNameNode = nnm.getNamedItem(mTagModuleName);
            Node parserClassNode = nnm.getNamedItem(mTagParserClass);
            try {
                if (moduleNameNode != null) {
                    mStandardizationConfig.moduleName = moduleNameNode.getNodeValue();
                }
                if (parserClassNode != null) {
                    mStandardizationConfig.parserClass = parserClassNode.getNodeValue();
                }
            } catch (DOMException ex) {
            }
        }

        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagStandardizeSystemObject.equals(name2)) {
                    parseStandardizeSystemObject(nl2.item(i2));
                }
            }
        }
    }
    
    private String parseClassName(Node node) {
        String className = null;
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                if (mTagClassName.equals(name3)) {
                    className = Utils.getStrElementValue(nl3.item(i3));
                    break;
                }
            }
        }
        return className;
    }
    private void parseMEFAConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node moduleNameNode = nnm.getNamedItem(mTagModuleName);
            Node parserClassNode = nnm.getNamedItem(mTagParserClass);
            try {
                if (moduleNameNode != null) {
                    mMEFAConfig.moduleName = moduleNameNode.getNodeValue();
                }
                if (parserClassNode != null) {
                    mMEFAConfig.parserClass = parserClassNode.getNodeValue();
                }
            } catch (DOMException ex) {
            }
        }
        
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagBlockPicker.equals(name2)) {
                    mMEFAConfig.blockPicker.className = parseClassName(nl2.item(i2));
                } else if (mTagPassController.equals(name2)) {
                    mMEFAConfig.passController.className = parseClassName(nl2.item(i2));
                } else if (mTagStandardizerApi.equals(name2)) {
                    mMEFAConfig.standardizerApi.className = parseClassName(nl2.item(i2));
                } else if (mTagStandardizerConfig.equals(name2)) {
                    mMEFAConfig.standardizerConfig.className = parseClassName(nl2.item(i2));
                } else if (mTagMatcherApi.equals(name2)) {
                    mMEFAConfig.matcherApi.className = parseClassName(nl2.item(i2));
                } else if (mTagMatcherConfig.equals(name2)) {
                    mMEFAConfig.matcherConfig.className = parseClassName(nl2.item(i2));
                }
            }
        }

    }
    
    // Matching Section
    private void parseMatchColumn(Node node) {
        String columnName = "";
        String matchType = "";
        NodeList nl5 = node.getChildNodes();
        for (int i5 = 0; i5 < nl5.getLength(); i5++) {
            if (nl5.item(i5).getNodeType() == Node.ELEMENT_NODE) {
                String name5 = ((Element) nl5.item(i5)).getTagName();
                String value = Utils.getStrElementValue(nl5.item(i5));
                if (mTagColumnName.equals(name5)) {
                    columnName = value;
                } else if (mTagMatchType.equals(name5)) {
                    matchType = value;
                }
            }
        }

        mMatchingConfig.matchSystemObject.addMatchColumn(columnName, matchType);
    }
    
    private void parseMatchSystemObject(Node node) {
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                if (mTagObjectName.equals(name3)) {
                    mMatchingConfig.matchSystemObject.ObjectName = Utils.getStrElementValue(nl3.item(i3));
                } else if (mTagMatchColumns.equals(name3)) {
                    NodeList nl4 = nl3.item(i3).getChildNodes();
                    for (int i4 = 0; i4 < nl4.getLength(); i4++) {
                        if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                            String name4 = ((Element) nl4.item(i4)).getTagName();
                            if (mTagMatchColumn.equals(name4)) {
                                parseMatchColumn(nl4.item(i4));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void parseMatchingConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node moduleNameNode = nnm.getNamedItem(mTagModuleName);
            Node parserClassNode = nnm.getNamedItem(mTagParserClass);
            try {
                if (moduleNameNode != null) {
                    mMatchingConfig.moduleName = moduleNameNode.getNodeValue();
                }
                if (parserClassNode != null) {
                    mMatchingConfig.parserClass = parserClassNode.getNodeValue();
                }
            } catch (DOMException ex) {
            }
        }
        
        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagMatchSystemObject.equals(name2)) {
                    parseMatchSystemObject(nl2.item(i2));
                }
            }
        }
    }

    private void parsePhoneticEncodersConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node moduleNameNode = nnm.getNamedItem(mTagModuleName);
            Node parserClassNode = nnm.getNamedItem(mTagParserClass);            
            try {
                String moduleName = null;
                String parserClass = null;
                if (moduleNameNode != null) {
                    moduleName = moduleNameNode.getNodeValue();
                }
                if (parserClassNode != null) {
                    parserClass = parserClassNode.getNodeValue();
                }
                mPhoneticEncodersConfig = new PhoneticEncodersConfig(moduleName, parserClass);
                
                NodeList nl4 = node.getChildNodes();
                for (int i4 = 0; i4 < nl4.getLength(); i4++) {
                    if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                        String name4 = ((Element) nl4.item(i4)).getTagName();
                        if (mTagEncoder.equals(name4)) {
                            Encoder encoder = parseEncoder(nl4.item(i4));
                            mPhoneticEncodersConfig.addEncoder(encoder);
                        }
                    }
                }
            } catch (DOMException ex) {
            }
        }
        
    }
    
    private Encoder parseEncoder(Node node) {

        String encodingType = null;
        String encoderImplementationClass = null;
        NodeList nl5 = node.getChildNodes();
        for (int i5 = 0; i5 < nl5.getLength(); i5++) {
            if (nl5.item(i5).getNodeType() == Node.ELEMENT_NODE) {
                String name5 = ((Element) nl5.item(i5)).getTagName();
                String ss = Utils.getStrElementValue(nl5.item(i5));
                if (this.mTagEncodingType.equals(name5)) {
                    encodingType = ss;
                }
                if (mTagEncoderImplementationClass.equals(name5)) {
                    encoderImplementationClass = ss;
                }
            }
        }

        Encoder encoder = new Encoder(encodingType, encoderImplementationClass);
        
        return encoder;
    }
    
    // Start here
    public void parse(Node node) {
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
                        if (mTagStandardizationConfig.equals(name)) {
                            parseStandardizationConfig(nl1.item(i1));
                        } else if (mTagMatchingConfig.equals(name)) {
                            parseMatchingConfig(nl1.item(i1));
                        } else if (mTagMEFAConfig.equals(name)) {
                            parseMEFAConfig(nl1.item(i1));
                        } else if (mTagPhoneticEncodersConfig.equals(name)) {
                            parsePhoneticEncodersConfig(nl1.item(i1));
                        }
                    }
                }
            }
        }
    }


    /**
     * print
     */
    void print() {
    }
    
    public ArrayList getAllGroups() {
        ArrayList al = new ArrayList();
        if (mStandardizationConfig.standardizeSystemObject.alNormalizationGroups != null) {            
            for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alNormalizationGroups.size(); i++) {
                Group group = (Group) mStandardizationConfig.standardizeSystemObject.alNormalizationGroups.get(i);
                al.add(group);
            }
        }
        if (mStandardizationConfig.standardizeSystemObject.alFreeFormGroups != null) {            
            for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
                Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
                al.add(group);
            }
        }
        return al;
    }
    
    public ArrayList getNormalizationGroups() {
        return mStandardizationConfig.standardizeSystemObject.alNormalizationGroups;
    }
    
    /* Add NormalizationGroup
     */
    public void addNormalizationGroup(String standardizationType, String domainSelector, 
                                      String localeFieldName, ArrayList localeCodeRows,
                                      String unnormalizedFieldID, String unnormalizedSourceField,
                                      String normalizedFieldID, String normalizedTargetField) {
        NormalizationGroup group = new NormalizationGroup();
        group.setStandardizationType(standardizationType);
        group.setDomainSelector(domainSelector);
        group.setLocaleFieldName(localeFieldName);
        group.setLocaleCodes(localeCodeRows);
        group.setUnnormalizedFieldId(unnormalizedFieldID);
        group.setUnnormalizedSourceFieldName(unnormalizedSourceField);
        group.setNormalizedSourceFieldName(normalizedTargetField);
        group.setNormalizedFieldId(normalizedFieldID);
        mStandardizationConfig.standardizeSystemObject.alNormalizationGroups.add(group);
    }
    
    /* Delete NormalizationGroup by type, SourceMapping and TargetMapping
     */
    public void deleteNormalizationGroup(String standardizationType, 
                                         String unnormalizedFieldID, String unnormalizedSourceField,
                                         String normalizedFieldID, String normalizedTargetField) {
        NormalizationGroup group = getNormalizationGroup(standardizationType, 
                                                         unnormalizedFieldID, unnormalizedSourceField,
                                                         normalizedFieldID, normalizedTargetField);
        if (group != null) {
            mStandardizationConfig.standardizeSystemObject.alNormalizationGroups.remove(group);
        }
    }
    
    /* Get NormalizationGroup by type, SourceMapping and TargetMapping
     */
    public NormalizationGroup getNormalizationGroup(String standardizationType, 
                                                    String unnormalizedFieldID, String unnormalizedSourceField,
                                                    String normalizedFieldID, String normalizedTargetField) {
        ArrayList alNormalizationGroups = mStandardizationConfig.standardizeSystemObject.alNormalizationGroups;
        if (alNormalizationGroups != null) {            
            for (int i = 0; i < alNormalizationGroups.size(); i++) {
                NormalizationGroup group = (NormalizationGroup) alNormalizationGroups.get(i);
                if (group.getStandardizationType().equals(standardizationType) &&
                    group.getUnnormalizedSourceFieldName().equals(unnormalizedSourceField) &&
                    group.getNormalizedSourceFieldName().equals(normalizedTargetField)) {
                    return group;
                }

            }
        }
        return null;
    }
    
    public ArrayList getFreeFormGroups() {
        ArrayList al = new ArrayList();
        if (mStandardizationConfig.standardizeSystemObject.alFreeFormGroups != null) {            
            for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
                Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
                al.add(group);
            }
        }
        return al;
    }
    
    public FreeFormGroup getFreeFormGroup(String standardizationType) {
        if (mStandardizationConfig.standardizeSystemObject.alFreeFormGroups != null) {            
            for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
                FreeFormGroup group = (FreeFormGroup) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
                if (group.getStandardizationType().equals(standardizationType)) {
                    return group;
                }
            }
        }
        return null;
    }
    
    public void deleteFreeFormGroup(String standardizationType) {
        if (mStandardizationConfig.standardizeSystemObject.alFreeFormGroups != null) {            
            for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
                Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
                if (group.getStandardizationType().equals(standardizationType)) {
                    mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.remove(group);
                    break;
                }
            }
        }
    }
    
    
    public String getMatchFieldId(String fieldName) {
        String strId = null;
        for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
            Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
            TargetMapping targetMapping = group.getTargetMapping(fieldName);
            if (targetMapping != null) {
                strId = targetMapping.fieldId;
                break;
            }
        }
        return strId;
    }
     
    /*
     *@param String fieldName
     *
     *@return alFieldIDsSelected
     */
    public ArrayList getMatchFieldIdsSelected(String fieldName) {
        ArrayList alFieldIDsSelected = new ArrayList();
        String strId = null;
        for (int i = 0; i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
            Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
            ArrayList alMappings = group.getTargetMappings();
            for (int j = 0; j < alMappings.size(); j++) {
                TargetMapping targetMapping = (TargetMapping) alMappings.get(j);
                if (targetMapping != null && targetMapping.fieldName.equals(fieldName)) {
                    alFieldIDsSelected.add(targetMapping.fieldId);
                }
            }
        }
        return alFieldIDsSelected;
    }
     
    /*
     *@param String sourceField
     *
     *@return alTargetFieldsSelected
     */
    public ArrayList getMatchOutputFields(String type, String sourceField) {
        ArrayList alMatchOutputFields = new ArrayList();
        String strId = null;
        for (int i = 0; mStandardizationConfig.standardizeSystemObject.alFreeFormGroups != null && i < mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.size(); i++) {
            Group group = (Group) mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.get(i);
            if (group.getStandardizationType().equals(type)) {
                ArrayList alMappings = group.getTargetMappings();
                for (int j = 0; j < alMappings.size(); j++) {
                    TargetMapping targetMapping = (TargetMapping) alMappings.get(j);
                    if (targetMapping != null && (sourceField == null || targetMapping.fieldName.indexOf(sourceField) == 0)) {
                        if (!alMatchOutputFields.contains(targetMapping.fieldName)) {
                            alMatchOutputFields.add(targetMapping.fieldName);
                        }
                    }
                }
            }
        }
        return alMatchOutputFields;
    }
   
    /*
     *@return ArrayList of class MatchColumn
     */
    public ArrayList getMatchColumns() {
        return mMatchingConfig.matchSystemObject.alMatchColumns;
    }
    
    /*
     *@param columnName composite fieldName - Enterprise.SystemSBR.Person.FirstName_Std
     *@return match-type defined in <match-column>
     */
    public String getMatchType(String columnName) {
        if (mMatchingConfig.matchSystemObject.alMatchColumns != null) {
            for (int i=0; i < mMatchingConfig.matchSystemObject.alMatchColumns.size(); i++) {
                MatchColumn matchColumn = (MatchColumn) mMatchingConfig.matchSystemObject.alMatchColumns.get(i);
                if (matchColumn.columnName.equals(columnName)) {
                    return matchColumn.matchType;
                }
            }
        }
        return null;
    }
    
    /*
     *@param columnName composite fieldName - Enterprise.SystemSBR.Person.FirstName_Std
     *@param match-type defined in <match-column>
     */
    public void updateMatchType(String columnName, String matchType) {
        boolean bFound = false;
        if (mMatchingConfig.matchSystemObject.alMatchColumns != null) {
            for (int i=0; i < mMatchingConfig.matchSystemObject.alMatchColumns.size(); i++) {
                MatchColumn matchColumn = (MatchColumn) mMatchingConfig.matchSystemObject.alMatchColumns.get(i);
                if (matchColumn.columnName.equals(columnName)) {
                    if (matchType == null) {
                        // remove it
                        mMatchingConfig.matchSystemObject.alMatchColumns.remove(matchColumn);
                    } else if (!matchColumn.matchType.equals(matchType)) {
                        matchColumn.matchType = matchType;
                        /*
                        mMatchingConfig.matchSystemObject.alMatchColumns.remove(i);
                        if (mMatchingConfig.matchSystemObject.alMatchColumns.size() == 0) {
                            mMatchingConfig.matchSystemObject.alMatchColumns.add(matchColumn);
                        } else {
                            mMatchingConfig.matchSystemObject.alMatchColumns.add(i, matchColumn);
                        }
                        */
                    }
                    bFound = true;
                    break;
                }
            }
        }
        
        if (!bFound && matchType != null) {
            mMatchingConfig.matchSystemObject.addMatchColumn(columnName, matchType);
        }
    }

    public ArrayList getPhoneticizeFields() {
        return mStandardizationConfig.standardizeSystemObject.getPhoneticizeFields();
    }
    
    public PhoneticizeField getPhoneticizedField(String sourceFieldName, String targetFieldId, String encodingType) {
        return mStandardizationConfig.standardizeSystemObject.getPhoneticizedField(sourceFieldName, targetFieldId, encodingType);
    }

    public void addPhoneticizedField(String sourceFieldName, String targetFieldId, String encodingType) {
        mStandardizationConfig.standardizeSystemObject.addPhoneticizedField(sourceFieldName, targetFieldId, encodingType);
    }
    
    public void deletePhoneticizedField(String sourceFieldName, String targetFieldId, String encodingType) {
        PhoneticizeField phoneticizeField = getPhoneticizedField(sourceFieldName, targetFieldId, encodingType);
        if (phoneticizeField != null) {
            mStandardizationConfig.standardizeSystemObject.removePhoneticizeField(phoneticizeField);
        }
    }
    
    public class PhoneticizeField {
        String sourceFieldName; // Company.CompanyName_Name
        String targetFieldId;   // Company.CompanyName_NamePhon
        String encodingType;    // NYSIIS
        
        PhoneticizeField(String sourceFieldName, String targetFieldId, String encodingType) {
            this.sourceFieldName = sourceFieldName;
            this.targetFieldId = targetFieldId;
            this.encodingType = encodingType;
        }
        
        public String getSourceFieldName() {
            return sourceFieldName;
        }
        
        public void setSourceFieldName(String sourceFieldName) {
            this.sourceFieldName = sourceFieldName;
        }
        
        public String getTargetFieldId() {
            return targetFieldId;
        }
        
        public void setTargetFieldId(String targetFieldId) {
            this.targetFieldId = targetFieldId;
        }
        
        public String getEncodingType() {
            return encodingType;
        }
        
        public void setEncodingType(String encodingType) {
            this.encodingType = encodingType;
        }

    }
    
    /* Used in 
     * <unnormalized-source-fields>
     *   <source-mapping>
     *     <standardized-object-field-id>
     *     <unnormalized-source-field-name>
     */
    public class SourceMapping {
        String fieldId;     // e.g. LastName
        String fieldName;   // e.g. Person.LastName
        
        public String getFieldId() {
            return fieldId;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

    }

    /* Used in getMatchType()
     *<match-column>
     */
    public class MatchColumn {
        String columnName;  // e.g. Enterprise.SystemSBR.Person.FirstName_Std
        String matchType;   // e.g. FirstName
        
        MatchColumn(String columnName, String matchType) {
            this.columnName = columnName;
            this.matchType = matchType;
        }
        
        public String getColumnName() {
            return columnName;
        }
        
        public void setColumnName(String value) {
            columnName = value;
        }
        
        public String getMatchType() {
            return matchType;
        }
        
        public String getOriginalColumnName() {
            int i = columnName.indexOf("Enterprise.SystemSBR.");
            if (i == -1) {
                return columnName;
            }
            i += ("Enterprise.SystemSBR.").length();
            return columnName.substring(i);
        }
    }
    
    class MatchingConfig {
        String moduleName = "Matching";
        String parserClass = "com.sun.mdm.index.configurator.impl.matching.MatchingConfiguration";
        MatchSystemObject matchSystemObject = new MatchSystemObject();
        class MatchSystemObject {
            String ObjectName = "";
            ArrayList alMatchColumns = new ArrayList(); //of MatchColumn
            
            void addMatchColumn(String columnName, String matchType) {
                MatchColumn matchColumn = new MatchColumn(columnName, matchType);
                alMatchColumns.add(matchColumn);
            }
        }
    }
    
    class MEFAConfig {
        String moduleName = "MEFA";
        String parserClass = "com.sun.mdm.index.configurator.impl.MEFAConfiguration";
        BlockPicker blockPicker = new BlockPicker();
        PassController passController = new PassController();
        StandardizerApi standardizerApi = new StandardizerApi();
        StandardizerConfig standardizerConfig = new StandardizerConfig();
        MatcherApi matcherApi = new MatcherApi();
        MatcherConfig matcherConfig = new MatcherConfig();
        
        class BlockPicker {
            String className = "com.sun.mdm.index.matching.impl.PickAllBlocksAtOnce";
        }

        class PassController {
            String className = "com.sun.mdm.index.matching.impl.PassAllBlocks";
        }

        class StandardizerApi {
            String className = "com.sun.mdm.index.matching.adapter.SbmeStandardizerAdapter";
        }

        class StandardizerConfig {
            String className = "com.sun.mdm.index.matching.adapter.SbmeStandardizerAdapterConfig";
        }

        class MatcherApi {
            String className = "com.sun.mdm.index.matching.adapter.SbmeMatcherAdapter";
        }

        class MatcherConfig {
            String className = "com.sun.mdm.index.matching.adapter.SbmeMatcherAdapterConfig";
        }
    }

    public FreeFormGroup createFreeFormGroup(String standardizationType) {
        FreeFormGroup group = new FreeFormGroup();
        group.setStandardizationType(standardizationType);
        group.addLocaleCode("Default", "US");
        mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.add(group);
        return group;
    }   
    
    public void addTargetMappingFreeFormGroup(String standardizationType, String fieldId, String sourceField) {
        TargetMapping targetMapping = new TargetMapping();
        targetMapping.fieldId = fieldId;
        targetMapping.fieldName = sourceField;
        FreeFormGroup group = null;
        ArrayList alGroups = getFreeFormGroups();
        boolean bFound = false;
        for (int i=0; alGroups != null && i < alGroups.size(); i++) {
            group = (FreeFormGroup) alGroups.get(i);

            if (group != null) {
                String standType = group.getStandardizationType();
                if (standardizationType.equals(standType)) {
                    bFound = true;
                    break;
                } else {
                    group = null;
                }
            }
        }
        if (!bFound) {
            // create a new FreeFormGroup
            group = new FreeFormGroup();
            group.setStandardizationType(standardizationType);
            group.addLocaleCode("Default", "US");
            mStandardizationConfig.standardizeSystemObject.alFreeFormGroups.add(group);
        }
        if (group != null) {
            group.addTargetMapping(targetMapping);
        }
    }
    
    public void deleteTargetMapping(String standardizationType, String fieldId, String sourceField) {
        ArrayList alNormalizationGroups = getNormalizationGroups();
        for (int i=0; alNormalizationGroups != null && i < alNormalizationGroups.size(); i++) {
            NormalizationGroup normalizationGroup = (NormalizationGroup) alNormalizationGroups.get(i);

            if (normalizationGroup != null) {
                String standType = normalizationGroup.getStandardizationType();
                if (!standardizationType.equals(standType)) {
                    continue;
                }
                ArrayList alTargetMapping = normalizationGroup.getTargetMappings();
                if (alTargetMapping != null) {
                    for (int j=0; j<alTargetMapping.size(); j++) {
                        TargetMapping targetMapping = (TargetMapping) alTargetMapping.get(j);
                        String tid = targetMapping.getFieldId();
                        String tfn = targetMapping.getFieldName();
                        if (fieldId.equals(tid) && sourceField.equals(tfn)) {
                            alTargetMapping.remove(targetMapping);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /* Used in getMatchFieldId()
     *<target-mapping>
     */
    public class TargetMapping {
        String fieldId;     // e.g. HouseNumber
        String fieldName;   // e.g. Person.Address[*].AddressLine1_HouseNo
        
        public String getFieldId() {
            return fieldId;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

    }
    
    public static class LocaleCode {
        private String value;
        private String locale;

        public LocaleCode() {}
        
        public LocaleCode(String value, String locale) {
            this.value = value;
            this.locale = locale;
    	}
        
    	public String getValue() {
            return value;
    	}

    	public String getLocaleCode() {
            return locale;
    	}

    	public void setValue(String value) {
            this.value = value;
    	}

    	public void setLocaleCode(String locale) {
            this.locale = locale;
    	}
    }   

    
    public class Group {
        String standardizationType; // BusinessName, PersonName, etc.
        String domainSelector = "com.sun.mdm.index.matching.impl.MultiDomainSelector";
        String localeFieldName = "";
        ArrayList alLocaleCodes = new ArrayList();
        ArrayList alUnstandardizedSourceFieldNames = null;
        ArrayList alTargetMapping = null;

        void Group() {
        }
        
        public void setStandardizationType(String standardizationType) {
            this.standardizationType = standardizationType;
        }
        
        public String getStandardizationType() {
            return this.standardizationType;
        }
        
        public void setDomainSelector(String domainSelector) {
            this.domainSelector = domainSelector;
        }
        
        public String getDomainSelector() {
            return this.domainSelector;
        }
        
        public void setLocaleFieldName(String localeFieldName) {
            this.localeFieldName = localeFieldName;
        }
        
        public String getLocaleFieldName() {
            return localeFieldName;
        }
        
        public void setLocaleCodes(ArrayList alLocaleCodes) {
            this.alLocaleCodes = alLocaleCodes;
        }
        
        public ArrayList getLocaleCodes() {
            return alLocaleCodes;
        }
        
        public void addLocaleCode(LocaleCode localeCode) {
            alLocaleCodes.add(localeCode);
        }
            
        public void addLocaleCode(String value, String locale) {
            LocaleCode localeCode = new LocaleCode(value, locale);
            addLocaleCode(localeCode);
        }
        
        public void addUnstandardizedSourceField(String fieldName) {
            if (alUnstandardizedSourceFieldNames == null) {
                alUnstandardizedSourceFieldNames = new ArrayList();
            }
            alUnstandardizedSourceFieldNames.add(fieldName);
        }
        
        public ArrayList getUnstandardizedSourceFields() {
            return alUnstandardizedSourceFieldNames;
        }
        
        public void setUnstandardizedSourceFields(ArrayList alUnstandardizedSourceFieldNames) {
            this.alUnstandardizedSourceFieldNames = alUnstandardizedSourceFieldNames;
        }

        public void addTargetMapping(TargetMapping targetMapping) {
            if (alTargetMapping == null) {
                alTargetMapping = new ArrayList();
            }
            alTargetMapping.add(targetMapping);
        }
        
        public TargetMapping getTargetMapping(String fieldName) {
            if (alTargetMapping != null) {
                for (int i=0; i < alTargetMapping.size(); i++) {
                    TargetMapping targetMapping = (TargetMapping) alTargetMapping.get(i);
                    if (targetMapping.fieldName.equals(fieldName)) {
                        return targetMapping;
                    }
                }
            }
            return null;
        }
        
        public ArrayList getTargetMappings() {
            return alTargetMapping;
        }
        
        public void setTargetMappings(ArrayList alTargetMapping) {
            this.alTargetMapping = alTargetMapping;
        }
        
        public ArrayList getTargetMappingFieldIDsBySourceField(String sourceField) {
            ArrayList al = new ArrayList();
            if (alTargetMapping != null) {
                for (int i=0; i < alTargetMapping.size(); i++) {
                    TargetMapping targetMapping = (TargetMapping) alTargetMapping.get(i);
                    if (targetMapping.fieldName.indexOf(sourceField) == 0) {
                        al.add(targetMapping.getFieldId());
                    }
                }
            }
            return al;
        }
    }
    
    public class NormalizationGroup extends Group {
        String unnormalizedFieldID;
        String unnormalizedSourceField;
        String normalizedFieldID;
        String normalizedTargetField;

        NormalizationGroup () {
            super();
        }
        
        /*
            <unnormalized-source-fields>
                <source-mapping>
                    <unnormalized-source-field-name>Person.LastName</unnormalized-source-field-name>
                    <standardized-object-field-id>LastName</standardized-object-field-id>
                </source-mapping>
            </unnormalized-source-fields>
         */
        public void setUnnormalizedSourceFieldName(String value) {
            unnormalizedSourceField = value;
        }
        
        public String getUnnormalizedSourceFieldName() {
            return unnormalizedSourceField;
        }
        
        public void setUnnormalizedFieldId(String value) {
            unnormalizedFieldID = value;
        }
        
        public String getUnnormalizedFieldId() {
            return unnormalizedFieldID;
        }

        /*
            <normalization-targets>
                <target-mapping>
                    <standardized-target-field-name>Person.LastName_Std</standardized-target-field-name>
                    <standardized-object-field-id>LastName</standardized-object-field-id>
                </target-mapping>
            </normalization-targets>
         */
        public void setNormalizedSourceFieldName(String value) {
            normalizedTargetField = value;
        }
        
        public String getNormalizedSourceFieldName() {
            return normalizedTargetField;
        }
        
        public void setNormalizedFieldId(String value) {
            normalizedFieldID = value;
        }
        
        public String getNormalizedFieldId() {
            return normalizedFieldID;
        }
    }
    
    public class FreeFormGroup extends Group {
        
        FreeFormGroup () {
            super();
        }
        
    }
    
    /*
	<PhoneticEncodersConfig module-name="PhoneticEncoders" parser-class="com.sun.mdm.index.configurator.impl.PhoneticEncodersConfig">
		<encoder>
			<encoding-type>NYSIIS</encoding-type>
			<encoder-implementation-class>com.sun.mdm.index.phonetic.impl.Nysiis</encoder-implementation-class>
		</encoder>
		<encoder>
			<encoding-type>Soundex</encoding-type>
			<encoder-implementation-class>com.sun.mdm.index.phonetic.impl.Soundex</encoder-implementation-class>
		</encoder>
	</PhoneticEncodersConfig>
     */
    
    public PhoneticEncodersConfig getPhoneticEncodersConfig() {
        return mPhoneticEncodersConfig;
    }
    
    public Encoder getEncoderByEncodingType(String encodingType) {
        return mPhoneticEncodersConfig.getEncoderByEncodingType(encodingType);
    }
    
    public void deleteEncoderByEncodingType(String encodingType) {
        mPhoneticEncodersConfig.deleteEncoderByEncodingType(encodingType);
    }
    
    public void addEncoder(String encodingType, String encoderImplementationClass) {
        mPhoneticEncodersConfig.addEncoder(encodingType, encoderImplementationClass);
    }
    
    public ArrayList getEncoders() {
        return mPhoneticEncodersConfig.getEncoders();
    }
    
    public class Encoder {
        String encodingType;    // NYSIIS, Soundex, and user defined
        String encoderImplementationClass;
            
        public Encoder(String encodingType, String encoderImplementationClass) {
            this.encodingType = encodingType;
            this.encoderImplementationClass = encoderImplementationClass;
        }
            
        public String getEncodingType() {
            return encodingType;
        }
            
        public String getEncoderImplementationClass() {
            return encoderImplementationClass;
        }
    }
    
    public class PhoneticEncodersConfig {
        String moduleName = "PhoneticEncoders";
        String parserClass = "com.sun.mdm.index.configurator.impl.PhoneticEncodersConfig";
        ArrayList alEncoders = new ArrayList();
        
        public PhoneticEncodersConfig(String moduleName, String parserClass) {
            if (moduleName != null) {
                this.moduleName = moduleName;
            }
            if (parserClass != null) {
                this.parserClass = parserClass;
            }
        }
        
        public void setEncoders(ArrayList alEncoders) {
            this.alEncoders = alEncoders;
        }
        
        public ArrayList getEncoders() {
            return alEncoders;
        }
        
        public void addEncoder(Encoder encoder) {
            alEncoders.add(encoder);
        }
        
        public void addEncoder(String encodingType, String encoderImplementationClass) {
            Encoder encoder = new Encoder(encodingType, encoderImplementationClass);
            alEncoders.add(encoder);
        }
        
        public Encoder getEncoderByEncodingType(String encodingType) {
            if (alEncoders != null) {
                for (int i = 0; i < alEncoders.size(); i++) {
                    Encoder encoder = (Encoder) alEncoders.get(i);
                    if (encoder.getEncodingType().equals(encodingType)) {
                        return encoder;
                    }
                }
            }
            return null;
        }
        
        public void deleteEncoderByEncodingType(String encodingType) {
            Encoder encoder = getEncoderByEncodingType(encodingType);
            if (encoder != null) {
                alEncoders.remove(encoder);
            }
        }
    }
    
    class StandardizationConfig {
        String moduleName = "Standardization";
        String parserClass = "com.sun.mdm.index.configurator.impl.standardization.StandardizationConfiguration";
        StandardizeSystemObject standardizeSystemObject = new StandardizeSystemObject();

        
        class StandardizeSystemObject {
            String systemObjectName;
            ArrayList alNormalizationGroups = new ArrayList();
            ArrayList alFreeFormGroups = new ArrayList();
            ArrayList alPhoneticizeFields = new ArrayList();
            
            ArrayList getPhoneticizeFields() {
                return alPhoneticizeFields;
            }
            
            void addPhoneticizeField(PhoneticizeField phoneticizeField) {
                alPhoneticizeFields.add(phoneticizeField);
            }
            
            void addPhoneticizedField(String sourceFieldName, String targetFieldId, String encodingType) {
                PhoneticizeField phoneticizeField = new PhoneticizeField(sourceFieldName, targetFieldId, encodingType);
                alPhoneticizeFields.add(phoneticizeField);
            }
            
            void removePhoneticizeField(PhoneticizeField phoneticizeField) {
                alPhoneticizeFields.remove(phoneticizeField);
            }
            
            PhoneticizeField getPhoneticizedField(String sourceFieldName, String targetFieldId, String encodingType) {
                for (int i=0; i<alPhoneticizeFields.size(); i++) {
                    PhoneticizeField phoneticizeField = (PhoneticizeField) alPhoneticizeFields.get(i);
                    if (phoneticizeField.sourceFieldName.equals(sourceFieldName) &&
                        phoneticizeField.targetFieldId.equals(targetFieldId) &&
                        phoneticizeField.encodingType.equals(encodingType)) {
                        return phoneticizeField;
                    }
                }
                return null;
            }

        }
    }
    
    public void setModified(boolean flag) {
        mModified = flag;
    }    
    
    public boolean isModified() {
        return mModified;
    }
    //PhoneticEncodersConfig
    private String getPhoneticEncodersConfigXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagPhoneticEncodersConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mPhoneticEncodersConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mPhoneticEncodersConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        ArrayList alEncoders = mPhoneticEncodersConfig.getEncoders();
        for (int i=0; i< alEncoders.size(); i++) {
            Encoder encoder = (Encoder) alEncoders.get(i);
            bufSegment.append(Utils.TAB2 + Utils.startTag(mTagEncoder));
            bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagEncodingType) + 
                              encoder.getEncodingType() + Utils.endTag(mTagEncodingType));
            bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagEncoderImplementationClass) + 
                              encoder.getEncoderImplementationClass() + Utils.endTag(mTagEncoderImplementationClass));
            bufSegment.append(Utils.TAB2 + Utils.endTag(mTagEncoder));        
        }
        
        bufSegment.append(Utils.TAB + Utils.endTag(mTagPhoneticEncodersConfig));
        return bufSegment.toString();
    }
    
    private String getMEFAConfigXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagMEFAConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mMEFAConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mMEFAConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagBlockPicker));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.blockPicker.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagBlockPicker));  

        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagPassController));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.passController.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagPassController));  

        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagStandardizerApi));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.standardizerApi.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagStandardizerApi));  

        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagStandardizerConfig));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.standardizerConfig.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagStandardizerConfig));  

        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagMatcherApi));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.matcherApi.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagMatcherApi));  

        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagMatcherConfig));
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagClassName) + 
                          mMEFAConfig.matcherConfig.className + Utils.endTag(mTagClassName));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagMatcherConfig));  
        
        bufSegment.append(Utils.TAB + Utils.endTag(mTagMEFAConfig));
        return bufSegment.toString();
    }
    
    public String getMatchingConfigXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagMatchingConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mMatchingConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mMatchingConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        //mTagMatchSystemObject
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagMatchSystemObject));
            // mTagObjectName
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagObjectName) + 
                          mMatchingConfig.matchSystemObject.ObjectName + Utils.endTag(mTagObjectName));
            // match-columns
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagMatchColumns));
        for (int i=0; i<mMatchingConfig.matchSystemObject.alMatchColumns.size(); i++) {
            MatchColumn matchColumn = (MatchColumn) mMatchingConfig.matchSystemObject.alMatchColumns.get(i);
            bufSegment.append(Utils.TAB4 + Utils.startTag(mTagMatchColumn));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagColumnName) + 
                              matchColumn.columnName + Utils.endTag(mTagColumnName));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagMatchType) + 
                              matchColumn.matchType + Utils.endTag(mTagMatchType));
            bufSegment.append(Utils.TAB4 + Utils.endTag(mTagMatchColumn));        
        }
        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagMatchColumns));
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagMatchSystemObject));
        bufSegment.append(Utils.TAB + Utils.endTag(mTagMatchingConfig));
        return bufSegment.toString();
    }
    
    private String getPhoneticizeFieldsXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagPhoneticizeFields));
        ArrayList alPhoneticizeFields = mStandardizationConfig.standardizeSystemObject.getPhoneticizeFields();
        for (int i=0; i<alPhoneticizeFields.size(); i++) {
            PhoneticizeField phoneticizeField = (PhoneticizeField) alPhoneticizeFields.get(i);
            bufSegment.append(Utils.TAB4 + Utils.startTag(mTagPhoneticizeField));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagUnphoneticizedSourceFieldName) + 
                              phoneticizeField.sourceFieldName + Utils.endTag(mTagUnphoneticizedSourceFieldName));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagPhoneticizedTargetFieldName) + 
                              phoneticizeField.targetFieldId + Utils.endTag(mTagPhoneticizedTargetFieldName));
            bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagEncodingType) + 
                              phoneticizeField.encodingType + Utils.endTag(mTagEncodingType));
            bufSegment.append(Utils.TAB4 + Utils.endTag(mTagPhoneticizeField));
        }
                                
        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagPhoneticizeFields));        
        return bufSegment.toString();
    }
    
    
    private String getFreeFormGroupXML(FreeFormGroup group) {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB4 + "<" + mTagGroup);
        bufSegment.append(Utils.quoteAttribute(mTagStandardizationType, group.getStandardizationType()));
        bufSegment.append(Utils.quoteAttribute(mTagDomainSelector, group.getDomainSelector()));
        bufSegment.append(">" + Utils.LINE);
        
        bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagLocaleFieldName) + group.getLocaleFieldName() +
                          Utils.endTag(mTagLocaleFieldName));
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagLocaleMaps));
        ArrayList alLocaleCodes = group.alLocaleCodes;
        for (int i=0; i<alLocaleCodes.size(); i++) {
            LocaleCode localeCode = (LocaleCode) alLocaleCodes.get(i);
            bufSegment.append(Utils.TAB6 + Utils.startTag(mTagLocaleCodes));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagValue) + 
                              localeCode.value + Utils.endTag(mTagValue));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagLocale) + 
                              localeCode.locale + Utils.endTag(mTagLocale));
            bufSegment.append(Utils.TAB6 + Utils.endTag(mTagLocaleCodes));
        }
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagLocaleMaps));
        
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagUnstandardizedSourceFields));
        ArrayList alUnstandardizedSourceFieldNames = group.getUnstandardizedSourceFields();
        for (int j=0; j<alUnstandardizedSourceFieldNames.size(); j++) {
            String name = (String) alUnstandardizedSourceFieldNames.get(j);
            bufSegment.append(Utils.TAB6 + Utils.startTagNoLine(mTagUnstandardizedSourceFieldName) + 
                              name + Utils.endTag(mTagUnstandardizedSourceFieldName));
        }
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagUnstandardizedSourceFields));
                                        
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagStandardizationTargets));
        ArrayList alTargetMapping = group.getTargetMappings();
        for (int k=0; k<alTargetMapping.size(); k++) {
            TargetMapping targetMapping = (TargetMapping) alTargetMapping.get(k);
            bufSegment.append(Utils.TAB6 + Utils.startTag(mTagTargetMapping));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagStandardizedObjectFieldId) + 
                              targetMapping.getFieldId() + Utils.endTag(mTagStandardizedObjectFieldId));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagStandardizedTargetFieldName) + 
                              targetMapping.getFieldName() + Utils.endTag(mTagStandardizedTargetFieldName));
            bufSegment.append(Utils.TAB6 + Utils.endTag(mTagTargetMapping));
        }
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagStandardizationTargets));
                                                
        bufSegment.append(Utils.TAB4 + Utils.endTag(mTagGroup));

        return bufSegment.toString();
    }

    private String getFreeFormTextsToStandardizeXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagFreeFormTextsToStandardize));
        // bufSegment.append(getFreeFormGroupXML());
        ArrayList alGroups = mStandardizationConfig.standardizeSystemObject.alFreeFormGroups;
        for (int i=0; i<alGroups.size(); i++) {
            FreeFormGroup group = (FreeFormGroup) alGroups.get(i);
            bufSegment.append(getFreeFormGroupXML(group));
        }

        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagFreeFormTextsToStandardize));        
        return bufSegment.toString();
    }
    
    private String getNormalizeGroupXML(NormalizationGroup group) {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB4 + "<" + mTagGroup);
        bufSegment.append(Utils.quoteAttribute(mTagStandardizationType, group.getStandardizationType()));
        bufSegment.append(Utils.quoteAttribute(mTagDomainSelector, group.getDomainSelector()));
        bufSegment.append(">" + Utils.LINE);
        
        bufSegment.append(Utils.TAB5 + Utils.startTagNoLine(mTagLocaleFieldName) + group.getLocaleFieldName() +
                          Utils.endTag(mTagLocaleFieldName));
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagLocaleMaps));
        ArrayList alLocaleCodes = group.alLocaleCodes;
        for (int i=0; i<alLocaleCodes.size(); i++) {
            LocaleCode localeCode = (LocaleCode) alLocaleCodes.get(i);
            bufSegment.append(Utils.TAB6 + Utils.startTag(mTagLocaleCodes));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagValue) + 
                              localeCode.value + Utils.endTag(mTagValue));
            bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagLocale) + 
                              localeCode.locale + Utils.endTag(mTagLocale));
            bufSegment.append(Utils.TAB6 + Utils.endTag(mTagLocaleCodes));
        }
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagLocaleMaps));
        
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagUnnormalizedSourceFields));
        bufSegment.append(Utils.TAB6 + Utils.startTag(mTagSourceMapping));
        bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagUnnormalizedSourceFieldName) + 
                          group.getUnnormalizedSourceFieldName() + Utils.endTag(mTagUnnormalizedSourceFieldName));
        bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagStandardizedObjectFieldId) + 
                          group.getUnnormalizedFieldId() + Utils.endTag(mTagStandardizedObjectFieldId));
        bufSegment.append(Utils.TAB6 + Utils.endTag(mTagSourceMapping));
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagUnnormalizedSourceFields));
        
        bufSegment.append(Utils.TAB5 + Utils.startTag(mTagNormalizationTargets));
        bufSegment.append(Utils.TAB6 + Utils.startTag(mTagTargetMapping));
        bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagStandardizedObjectFieldId) + 
                          group.getNormalizedFieldId() + Utils.endTag(mTagStandardizedObjectFieldId));
        bufSegment.append(Utils.TAB7 + Utils.startTagNoLine(mTagStandardizedTargetFieldName) + 
                          group.getNormalizedSourceFieldName() + Utils.endTag(mTagStandardizedTargetFieldName));
        bufSegment.append(Utils.TAB6 + Utils.endTag(mTagTargetMapping));
        bufSegment.append(Utils.TAB5 + Utils.endTag(mTagNormalizationTargets));
                                                
        bufSegment.append(Utils.TAB4 + Utils.endTag(mTagGroup));

        return bufSegment.toString();
    }
    
    private String getStructuresToNormalizeXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB3 + Utils.startTag(mTagStructuresToNormalize));
        ArrayList alGroups = mStandardizationConfig.standardizeSystemObject.alNormalizationGroups;
        for (int i=0; i<alGroups.size(); i++) {
            NormalizationGroup group = (NormalizationGroup) alGroups.get(i);
            bufSegment.append(getNormalizeGroupXML(group));
        }
        bufSegment.append(Utils.TAB3 + Utils.endTag(mTagStructuresToNormalize));        
        return bufSegment.toString();
    }
    
    private String getStandardizationConfigXML() {
        StringBuffer bufSegment = new StringBuffer();
        bufSegment.append(Utils.TAB + "<" + mTagStandardizationConfig);
        
        bufSegment.append(Utils.quoteAttribute(mTagModuleName, mStandardizationConfig.moduleName));
        bufSegment.append(Utils.quoteAttribute(mTagParserClass, mStandardizationConfig.parserClass));
        bufSegment.append(">" + Utils.LINE);
        //mTagStandardizeSystemObject
        bufSegment.append(Utils.TAB2 + Utils.startTag(mTagStandardizeSystemObject));
            // mTagSystemObjectName
        bufSegment.append(Utils.TAB3 + Utils.startTagNoLine(mTagSystemObjectName) + 
                          mStandardizationConfig.standardizeSystemObject.systemObjectName + Utils.endTag(mTagSystemObjectName));
        bufSegment.append(getStructuresToNormalizeXML());
        bufSegment.append(getFreeFormTextsToStandardizeXML());
        bufSegment.append(getPhoneticizeFieldsXML());
        bufSegment.append(Utils.TAB2 + Utils.endTag(mTagStandardizeSystemObject));
        
        bufSegment.append(Utils.TAB + Utils.endTag(mTagStandardizationConfig));
        return bufSegment.toString();
    }
    
    /**
     *  return XML String for this QueryType
     *
     */
    
    public String getXMLString() {
        StringBuffer buffer = new StringBuffer();
        
        // @Todo change this hardcoded code to elements retrieved during parsing
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Utils.LINE);
        buffer.append("<Configuration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schema/mefa.xsd\">" + Utils.LINE);
        buffer.append(getStandardizationConfigXML());
        buffer.append(getMatchingConfigXML());
        buffer.append(getMEFAConfigXML());
        buffer.append(getPhoneticEncodersConfigXML());
        buffer.append(Utils.endTag(mTagConfiguration));        
        
        return buffer.toString();
    }
    
}
