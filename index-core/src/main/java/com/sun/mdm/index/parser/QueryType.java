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
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;


/**
 * @author kkao
 * @version
 */

/*
 * Parse query.xml (Candidate Select)
 */
public class QueryType {
    private final String mFmt = "                [QueryType]";
    private final String mTagConfiguration = "Configuration";
    private final String mTagQueryBuilderConfig = "QueryBuilderConfig";
    private final String mTagModuleName = "module-name";
    private final String mTagParserClass = "parser-class";
    private final String mTagQueryBuilder = "query-builder";
    private final String mTagConfig = "config";
    private final String mTagBlockDefinition = "block-definition";
    private final String mTagNumber = "number";
    private final String mTagHint = "hint";
    private final String mTagBlockRule = "block-rule";
    private final String mTagEquals = "equals";
    private final String mTagNotEquals = "not-equals";
    private final String mTagEqualsUpper = "equals-upper";    
    private final String mTagEqualsFirst = "equals-first";
    private final String mTagGreaterThanOrEqual = "greater-than-or-equal";
    private final String mTagLessThanOrEqual = "less-than-or-equal";
    private final String mTagRange = "range";    
    private final String mTagConstant = "constant";    
    private final String mTagField = "field";
    private final String mTagSource = "source";
    private final String mTagDefault = "default"; 
    private final String mTagLowerBound = "lower-bound";    
    private final String mTagUpperBound = "upper-bound";        
    private final String mTagOption = "option";
    
    private final String mTagAlphaSearch = "ALPHA-SEARCH";
    private final String mTagPhoneticSearch = "PHONETIC-SEARCH";
    private final String mTagBlockerSearch = "BLOCKER-SEARCH";
    private final String mTagBlockerQueryBuilder = "com.sun.mdm.index.querybuilder.BlockerQueryBuilder";
    
    private final String mKeyUseWildcard = "UseWildcard";
    
    public static final String DEFALUT_BASIC_QUERY_BUILDER_CLASS = "com.sun.mdm.index.querybuilder.BasicQueryBuilder";
    public static final String DEFALUT_BASIC_QUERY_BUILDER_PARSER_CLASS = "com.sun.mdm.index.configurator.impl.querybuilder.KeyValueConfiguration";
    
    public static final String DEFALUT_BLOCKER_QUERY_BUILDER_CLASS = "com.sun.mdm.index.querybuilder.BlockerQueryBuilder";
    public static final String DEFALUT_BLOCKER_QUERY_BUILDER_PARSER_CLASS = "com.sun.mdm.index.configurator.impl.blocker.BlockerConfig";

    private QueryBuilderConfig mQueryBuilderConfig = new QueryBuilderConfig();
    private ArrayList mAlBlockingSources = new ArrayList();
    private ArrayList mAlBlockOnFields = new ArrayList();
    private boolean mModified = false;
    

    /**
     * @return ArrayList ret QueryBuilders
     */
    public ArrayList getQueryBuilders() {
        return mQueryBuilderConfig.alQueryBuilders;
    }

    /**
     *@param String queryBuilderName
     *@return QueryBuilder
     */
    public QueryBuilder getQueryBuilderByName(String queryBuilderName) {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getQueryBuilderByName(queryBuilderName);
        return queryBuilder;
    }
    
    /**
     *@param String queryBuilderName
     */
    public void deleteQueryBuilderByName(String queryBuilderName) {
        mQueryBuilderConfig.deleteQueryBuilderByName(queryBuilderName);
    }
    
    /**
     * @return ArrayList ret ArrayList
     */
    public ArrayList getBlockingSources() {
        return mAlBlockingSources;
    }
    
    /**
     * @param name BlockDefinition number (i.e. ID0)
     * @return ArrayList ret ArrayList of BlockRule
     */
    public BlockRule getBlockRuleOfBlockDefinition(String queryBuilderName, String blockDefinitionName) {
        BlockDefinition blockDefinitionFound = getBlockDefinitionByName(queryBuilderName, blockDefinitionName);
        if (blockDefinitionFound != null) {
            return blockDefinitionFound.getBlockRule();
        }
        return null;
    }
    
    /*
     *@return BlockDefinition by name (number)
     */
    public BlockDefinition getBlockDefinitionByName(String queryBuilderName, String blockDefinitionName) {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getQueryBuilderByName(queryBuilderName);
        if (queryBuilder != null) {
            return queryBuilder.getBlockDefinition(blockDefinitionName);
        }
        return null;
    }
    
    /*
     *@return array of BlockDefinition listed in Blocker-Search
     */
    public ArrayList getAllBlockDefinitions() {
        ArrayList alAllBlockDefinitions = new ArrayList();
        for (int i = 0; i < mQueryBuilderConfig.alQueryBuilders.size(); i++) {
            QueryBuilder queryBuilder = (QueryBuilder) mQueryBuilderConfig.alQueryBuilders.get(i);
            if (queryBuilder.config.alBlockDefinitions != null) {
                alAllBlockDefinitions.addAll(queryBuilder.config.alBlockDefinitions);
            }
        }
        return alAllBlockDefinitions;
    }
    
    /*
     *@return array of BlockDefinition listed in Blocker-Search
     */
    public ArrayList getBlockDefinitions(String queryBuilderName) {
        for (int i = 0; i < mQueryBuilderConfig.alQueryBuilders.size(); i++) {
            QueryBuilder queryBuilder = (QueryBuilder) mQueryBuilderConfig.alQueryBuilders.get(i);
            if (queryBuilder.getQueryBuilderName().equals(queryBuilderName)) {
                return queryBuilder.getSelectedBlockDefinitions();
            }
        }
        return null;
    }

    /**
     * @return ArrayList ret ArrayList
     */
    public ArrayList getBlockOnFields() {
        return mAlBlockOnFields;
    }
    
    /**
     * The fields with the blocking set to true
     * To uniquely identify the fields, the entry is composed with
     * parentName.fieldName, eg. Person.FirstName, Alias.FirstName
     */
    private void setBlockOnFields() {
        if (mAlBlockingSources != null) {
            for (int i = 0; i < mAlBlockingSources.size(); i++) {
                String token = (String) mAlBlockingSources.get(i);
                String parent = null;
                int l = token.indexOf('.');
                parent = token.substring(0, l+1);
                int j = token.lastIndexOf('.');
                int k = token.lastIndexOf('_');
                if (j > 0) {
                    String fieldName = parent + token.substring(j+1);
                    if (fieldName != null && !mAlBlockOnFields.contains(fieldName)) {
                        mAlBlockOnFields.add(fieldName);
                    }
                }
                /*
                if (k > j) { // It is a "Generated" field, e.g. FirstName_Phon
                    // get the origin
                    String fieldNameOrg = parent + token.substring(j+1, k);
                    if (!mAlBlockOnFields.contains(fieldNameOrg)) {
                        mAlBlockOnFields.add(fieldNameOrg);
                    }
                }
                */
            }
        }
    }
    
    private void parseUseWildcard(Node node, QueryBuilder queryBuilder) {
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                if (mTagConfig.equals(name3)) {
                    NodeList nl4 = nl3.item(i3).getChildNodes();
                    for (int i4 = 0; i4 < nl4.getLength(); i4++) {
                        if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                            String name4 = ((Element) nl4.item(i4)).getTagName();
                            if (mTagOption.equals(name4)) {
                                NamedNodeMap nnm = nl4.item(i4).getAttributes();
                                if (nnm != null) {
                                    Node key = nnm.getNamedItem("key");
                                    Node value = nnm.getNamedItem("value");
                                    try {
                                        String optionKey = key.getNodeValue(); // "UseWildcard"
                                        String optionValue = value.getNodeValue(); // "true" or "false"
                                        queryBuilder.setOptionKey(optionKey);
                                        queryBuilder.setOptionValue(optionValue);
                                        queryBuilder.setUseWildcard(optionValue.equals("true"));
                                    } catch (DOMException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private String parseBoundType(Node node) {
        String type = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node typeNode = nnm.getNamedItem("type"); // constant, offset
            try {
                type = typeNode.getNodeValue();
            } catch (DOMException ex) {
                ex.printStackTrace();
            }
        }
        return type;
    }
    
    /*
        <range>
          <field>Enterprise.SystemSBR.RSTest.intParent</field>
          <source>RSTest.intParent</source>
          <default>
            <lower-bound type="constant">-8</lower-bound>
            <upper-bound type="offset">8</upper-bound>
          </default>
        </range>
     */
    private void parseDefault(Node node, BlockBy blockBy) {
        NodeList nl8 = node.getChildNodes();
        for (int i8 = 0; i8 < nl8.getLength(); i8++) {
            if (nl8.item(i8).getNodeType() == Node.ELEMENT_NODE) {
                String name8 = ((Element) nl8.item(i8)).getTagName();
                
                String ss8 = Utils.getStrElementValue(nl8.item(i8));
                if (mTagUpperBound.equals(name8)) {
                    blockBy.setUpperBoundType(parseBoundType(nl8.item(i8)));
                    blockBy.setUpperBoundValue(ss8);
                } else if (mTagLowerBound.equals(name8)) {
                    blockBy.setLowerBoundType(parseBoundType(nl8.item(i8)));
                    blockBy.setLowerBoundValue(ss8);
                }
            }
        }
    }
    
    private BlockBy ParseBlockBy(String operator, Node node) {
        BlockBy blockBy = new BlockBy();
        blockBy.setOperator(operator);
        NodeList nl7 = node.getChildNodes();
        for (int i7 = 0; i7 < nl7.getLength(); i7++) {
            if (nl7.item(i7).getNodeType() == Node.ELEMENT_NODE) {
                String name7 = ((Element) nl7.item(i7)).getTagName();
                String ss = Utils.getStrElementValue(nl7.item(i7));
                if (mTagField.equals(name7)) {
                    blockBy.setField(ss);
                } else if (mTagConstant.equals(name7)) {
                    blockBy.setConstant(ss);
                } else if (mTagSource.equals(name7)) {
                    blockBy.setSource(ss);
                    mAlBlockingSources.add(ss);
                } else if (mTagDefault.equals(name7)) {
                    parseDefault(nl7.item(i7), blockBy);
                }
            }
        }
        return blockBy;
    }
    
    private BlockRule parseBlockRule(Node node) {
        BlockRule blockRule = new BlockRule();
        NodeList nl6 = node.getChildNodes();
        for (int i6 = 0; i6 < nl6.getLength(); i6++) {
            if (nl6.item(i6).getNodeType() == Node.ELEMENT_NODE) {
                String operator = ((Element) nl6.item(i6)).getTagName();
                //ToDo - recursive for <or>, <and> blocks
                if (mTagEquals.equals(operator) || 
                    mTagNotEquals.equals(operator) || 
                    mTagEqualsUpper.equals(operator) || 
                    mTagGreaterThanOrEqual.equals(operator) ||
                    mTagLessThanOrEqual.equals(operator) || 
                    mTagEqualsFirst.indexOf(operator) >= 0 || 
                    mTagRange.equals(operator)) {
                    blockRule.addBlockBy(ParseBlockBy(operator, nl6.item(i6)));
                }
            }
        }
        return blockRule;
    }
    
    private BlockDefinition parseBlockDefinition(Node node, String queryBuilderName) {
        BlockDefinition blockDefinition = new BlockDefinition(queryBuilderName);
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node number = nnm.getNamedItem(mTagNumber); // "ID0", etc.
            try {
                String name = number.getNodeValue();
                blockDefinition.setName(name);
            } catch (DOMException ex) {
                ex.printStackTrace();
            }
        }
        
        NodeList nl5 = node.getChildNodes();
        for (int i5 = 0; i5 < nl5.getLength(); i5++) {
            if (nl5.item(i5).getNodeType() == Node.ELEMENT_NODE) {
                String name5 = ((Element) nl5.item(i5)).getTagName();
                if (mTagHint.equals(name5)) {
                    String hint = Utils.getStrElementValue(nl5.item(i5));
                    if (hint != null) {
                        blockDefinition.setHint(hint);
                    }
                } else if (mTagBlockRule.equals(name5)) {
                    BlockRule blockRule = parseBlockRule(nl5.item(i5));
                    blockDefinition.setBlockRule(blockRule);
                }
            }
        }
        return blockDefinition;
    }

    private void parseConfig(Node node, QueryBuilder queryBuilder) {
        ArrayList alBlockDefinitions = new ArrayList();
        NodeList nl3 = node.getChildNodes();
        for (int i3 = 0; i3 < nl3.getLength(); i3++) {
            if (nl3.item(i3).getNodeType() == Node.ELEMENT_NODE) {
                String name3 = ((Element) nl3.item(i3)).getTagName();
                if (mTagConfig.equals(name3)) {
                    NodeList nl4 = nl3.item(i3).getChildNodes();
                    for (int i4 = 0; i4 < nl4.getLength(); i4++) {
                        if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                            String name4 = ((Element) nl4.item(i4)).getTagName();
                            if (mTagBlockDefinition.equals(name4)) {
                                BlockDefinition blockDefinition = parseBlockDefinition(nl4.item(i4), queryBuilder.queryBuilderName);
                                alBlockDefinitions.add(blockDefinition);
                            }
                        }
                    }
                }
            }
        }
        queryBuilder.config.alBlockDefinitions = alBlockDefinitions;
    }
    
    private void parseQueryBuilder(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            QueryBuilder queryBuilder = mQueryBuilderConfig.getNewQueryBuilder();
            Node tmp = nnm.getNamedItem("name");
            if (tmp != null) {
                queryBuilder.setQueryBuilderName(tmp.getNodeValue());
            }
            tmp = nnm.getNamedItem("class");
            if (tmp != null) {
                queryBuilder.setQueryBuilderClass(tmp.getNodeValue());
            }
            tmp = nnm.getNamedItem("parser-class");
            if (tmp != null) {
                queryBuilder.setParserClass(tmp.getNodeValue());
            }
            tmp = nnm.getNamedItem("standardize");
            if (tmp != null) {
                String standardize = tmp.getNodeValue();
                queryBuilder.setStandardize(standardize.equals("true"));
            }
            tmp = nnm.getNamedItem("phoneticize");
            if (tmp != null) {
                String phoneticize = tmp.getNodeValue();
                queryBuilder.setPhoneticize(phoneticize.equals("true"));
            }
            try {
                // get useWildcard option
                parseUseWildcard(node, queryBuilder); 
                //queryBuilder.setUseWildcard(useWildcard);
                // get block definitions
                if (queryBuilder.isBlockerSearch()) {  // Blocker Search
                    parseConfig(node, queryBuilder);
                }
            } catch (DOMException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    void parseQueryBuilderConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem(mTagModuleName);
            if (tmp != null) {
                mQueryBuilderConfig.moduleName = tmp.getNodeValue();
            }
            tmp = nnm.getNamedItem(mTagParserClass);
            if (tmp != null) {
                mQueryBuilderConfig.parserClass = tmp.getNodeValue();
            }
        }

        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagQueryBuilder.equals(name2)) {
                    parseQueryBuilder(nl2.item(i2));
                }
            }
        }
    }
    
    /**
     * parse
     * @param node Node
     */
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
                        if (mTagQueryBuilderConfig.equals(name)) {
                            parseQueryBuilderConfig(nl1.item(i1));
                        }
                    }
                }
            }
        }
        setBlockOnFields();
    }
    
    /**
     *@return QueryBuilder 
     */
    public QueryBuilder getNewQueryBuilder() {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getNewQueryBuilder();
        return queryBuilder;
    }
    
    /**
     *@param String queryBuilderName
     *@param String queryBuilderClass
     *@param String parserClass
     *@param boolean bStandardize
     *@param boolean bPhoneticize
     *@param String optionKey
     *@param boolean bUseWildcard
     *@param ArrayList alBlockingRulesSelected
     *@return QueryBuilder
     */
    public QueryBuilder createQueryBuilder(String queryBuilderName, 
                                 String queryBuilderClass,
                                 String parserClass, 
                                 boolean bStandardize,
                                 boolean bPhoneticize,
                                 String optionKey,
                                 String optionValue,
                                 boolean bUseWildcard,
                                 ArrayList alBlockingRulesSelected) {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getNewQueryBuilder();
        queryBuilder.setQueryBuilderName(queryBuilderName);
        queryBuilder.setQueryBuilderClass(queryBuilderClass);
        queryBuilder.setParserClass(parserClass);
        queryBuilder.setStandardize(bStandardize);
        queryBuilder.setPhoneticize(bPhoneticize);
        queryBuilder.setOptionKey(optionKey);
        queryBuilder.setOptionValue(optionValue);
        queryBuilder.setUseWildcard(bUseWildcard);
        queryBuilder.setSelectedBlockDefinitionNames(alBlockingRulesSelected);

        return queryBuilder;
    }
    
    /**
     *@param String queryBuilderName
     *@param String selectedBlockDefinitionName
     *@param String newBlockDefinitionName
     *@param String hint
     *@param BlockRule blockRule
     */
    public void updateBlockDefinition(String queryBuilderName, String selectedBlockDefinitionName, String newBlockDefinitionName, String hint, BlockRule blockRule) {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getQueryBuilderByName(queryBuilderName);
        BlockDefinition blockDefinition = queryBuilder.getBlockDefinition(selectedBlockDefinitionName);
        blockDefinition.setName(newBlockDefinitionName);
        blockDefinition.setHint(hint);
        blockDefinition.setBlockRule(blockRule);
    }
    
    /**
     *@param String queryBuilderName
     *@param String name
     *@param String hint
     *@param BlockRule blockRule
     *@return BlockDefinition
     */
    public BlockDefinition createBlockDefinition(String queryBuilderName, String name, String hint, BlockRule blockRule) {
        BlockDefinition blockDefinition = new BlockDefinition(queryBuilderName, name, hint, blockRule);
        QueryBuilder queryBuilder = mQueryBuilderConfig.getQueryBuilderByName(queryBuilderName);
        if (queryBuilder == null) {
            queryBuilder = mQueryBuilderConfig.getNewQueryBuilder();
            queryBuilder.queryBuilderName = queryBuilderName;
        }
        queryBuilder.addBlockDefinition(blockDefinition);
        // Update mAlBlockingSources
        ArrayList alBlockBys = blockDefinition.getBlockRule().getBlockBys();
        for (int i=0; i < alBlockBys.size(); i++) {
            BlockBy blockBy = (BlockBy) alBlockBys.get(i);
            if (!mAlBlockingSources.contains(blockBy.source)) {
                mAlBlockingSources.add(blockBy.source);
            }
        }
        return blockDefinition;
    }
    
    /**
     *@param String queryBuilderName
     *@param String name
     */
    public void deleteBlockDefinitionByName(String queryBuilderName, String name) {
        QueryBuilder queryBuilder = mQueryBuilderConfig.getQueryBuilderByName(queryBuilderName);
        if (queryBuilder != null) {
            queryBuilder.deleteBlockDefinition(name);
        }
    }
    
    public static class RangeDefault {
        String lowerBoundType;
        String lowerBoundValue;
        String upperBoundType;        
        String upperBoundValue;        
    }
    
    public static class BlockBy {
        String operator = "equals"; // range
        String field;
        String source = null;
        String constant = null;
        String lowerBoundType = null;  // constant, offset, (null for not defined)
        String lowerBoundValue = null; // date, int
        String upperBoundType = null;  // constant, offset, (null for not defined)
        String upperBoundValue = null; // date, int
        boolean useConstant = false;
        
        public BlockBy() {}
        
        public BlockBy(String field, String operator, String source, boolean useConstant,
                       String lowerBoundType, String lowerBoundValue,
                       String upperBoundType, String upperBoundValue) {
            this.field = field;
            this.operator = operator;
            if (useConstant) {
                this.constant = source;
            } else {
                this.source = source;
            }
            this.useConstant = useConstant;
            this.lowerBoundType = lowerBoundType;
            this.lowerBoundValue = lowerBoundValue;
            this.upperBoundType = upperBoundType;
            this.upperBoundValue = upperBoundValue;
        }
        
        public void setOperator(String operator) {
            this.operator = operator;
        }
        
        public void setField(String field) {
            this.field = field;
        }
        
        public void setSource(String source) {
            this.source = source;
            this.useConstant = false;
        }
        
        public void setConstant(String constant) {
            this.constant = constant;
            this.useConstant = true;
        }
        
        public void setLowerBoundType(String lowerBoundType) {
            this.lowerBoundType = lowerBoundType;
        }
        
        public void setLowerBoundValue(String lowerBoundValue) {
            this.lowerBoundValue = lowerBoundValue;
        }
        
        public void setUpperBoundType(String upperBoundType) {
            this.upperBoundType = upperBoundType;
        }
        
        public void setUpperBoundValue(String upperBoundValue) {
            this.upperBoundValue = upperBoundValue;
        }
        
        public String getOperator() {
            return operator;
        }
        
        public String getField() {
            return field;
        }
        
        public String getSource() {
            if (useConstant) {
                return constant;
            } else {
                return source;
            }
        }
        
        public String getConstant() {
            return constant;
        }
        
        public String getLowerBoundType() {
            return lowerBoundType;
        }
        
        public String getLowerBoundValue() {
            return lowerBoundValue;
        }
        
        public String getUpperBoundType() {
            return upperBoundType;
        }
        
        public String getUpperBoundValue() {
            return upperBoundValue;
        }
        
        public boolean useConstant() {
            return useConstant;
        }
        
    }

    public static class BlockRule {
        ArrayList alBlockBys;
        
        public void BlockRule() {
            
        }
        
        public void addBlockBy (BlockBy equals) {
            if (alBlockBys == null) {
                alBlockBys = new ArrayList();
            }
            alBlockBys.add(equals);
        }
         
        public void setBlockBys(ArrayList alBlockBys) {
            this.alBlockBys = alBlockBys;
        }
       
        public ArrayList getBlockBys() {
            return alBlockBys;
        }
    }
    
    public static class BlockDefinition {
        String queryBuilderName = null;
        String name = null;                    // e.g. ID0, ID1
        String hint = null;
        BlockRule blockRule = new BlockRule();  // ToDo
        
        public BlockDefinition(String queryBuilderName) {
            this.queryBuilderName = queryBuilderName;
        }
        
        public BlockDefinition(String queryBuilderName, String name, String hint, BlockRule blockRule) {
            this.queryBuilderName = queryBuilderName;
            this.name = name;
            this.hint = hint;
            this.blockRule = blockRule;
        }

        public String getQueryBuilderName() {
            return queryBuilderName;
        }
        
        public void setName(String number) {
            name = number;
        }
        
        public String getName() {
            return name;
        }
        
        public void setHint(String hint) {
            this.hint = hint;
        }
        
        public String getHint() {
            return this.hint;
        }
        
        void setBlockRule(BlockRule blockRule) {
            this.blockRule = blockRule;
        }
        
        public BlockRule getBlockRule() {
            return blockRule;
        }
    }
    
    class Config {
        ArrayList alBlockDefinitions = null; // array of BlockDefinition
        ArrayList alBlockDefinitionNames = null; // array of BlockDefinition names
    }
    
    public class QueryBuilder {
        String queryBuilderName;  // e.g. ALPHA-SEARCH, ALPHA-SEARCH, PHONETIC-SEARCH
        String queryBuilderClass = DEFALUT_BLOCKER_QUERY_BUILDER_CLASS;
        String parserClass = DEFALUT_BLOCKER_QUERY_BUILDER_PARSER_CLASS;
        boolean bStandardize = false;
        boolean bPhoneticize = false;
        // config for Basic search
        String optionKey = null;
        String optionValue = null;
        boolean bUseWildcard = false;
        // config for Blocker Search
        Config config = new Config();
        
        public void QueryBuilder(String queryBuilderName) {
            this.queryBuilderName = queryBuilderName;
        }   
        
        public void QueryBuilder(String queryBuilderName, 
                                 String queryBuilderClass,
                                 String parserClass, 
                                 boolean bStandardize,
                                 boolean bPhoneticize,
                                 String optionKey,
                                 String optionValue,
                                 boolean bUseWildcard,
                                 ArrayList alBlockDefinitionNames) {
            this.queryBuilderName = queryBuilderName;
            this.queryBuilderClass = queryBuilderClass;
            this.parserClass = parserClass;
            this.bStandardize = bStandardize;
            this.bPhoneticize = bPhoneticize;
            this.bUseWildcard = bUseWildcard;
            this.optionKey = optionKey;
            this.optionValue = optionValue;        
            this.config.alBlockDefinitionNames = alBlockDefinitionNames;
        }
        
        public void setQueryBuilderName(String name) {
            queryBuilderName = name;
        }
        
        public String getQueryBuilderName() {
            return queryBuilderName;
        }
        
        public void setQueryBuilderClass(String builderClass) {
            queryBuilderClass = builderClass;
        }
        
        public String getQueryBuilderClass() {
            return queryBuilderClass;
        }
        
        public void setParserClass(String parserClass) {
            this.parserClass = parserClass;
        }
        
        public String getParserClass() {
            return parserClass;
        }
        
        public void setStandardize(boolean flag) {
            bStandardize = flag;
        }
        
        public boolean getStandardize() {
            return bStandardize;
        }
        
        public void setPhoneticize(boolean flag) {
            bPhoneticize = flag;
        }
        
        public boolean getPhoneticize() {
            return bPhoneticize;
        }

        public void setOptionKey(String key) {
            optionKey = key;
        }
        
        public String getOptionKey() {
            return optionKey;
        }
        
        public void setOptionValue(String value) {
            optionValue = value;
        }
        
        public String getOptionValue() {
            return optionValue;
        }
        
        public void setUseWildcard(boolean flag) {
            bUseWildcard = flag;
        }
        
        public boolean isBlockerSearch() {
            return (optionKey == null);
        }
        
        public boolean getUseWildcard() {
            return bUseWildcard;
        }
        
        public void addBlockDefinition(BlockDefinition blockDefinition) {
            if (config.alBlockDefinitions == null) {
                config.alBlockDefinitions = new ArrayList();
            }
            config.alBlockDefinitions.add(blockDefinition);
        }

        public BlockDefinition getBlockDefinition(String blockDefinitionName) {
            for (int i=0; i < config.alBlockDefinitions.size(); i++) {
                BlockDefinition blockDefinition = (BlockDefinition) config.alBlockDefinitions.get(i);
                if (blockDefinitionName.equals(blockDefinition.getName())) {
                    return blockDefinition;
                }
            }
            return null;
        }
        
        public void deleteBlockDefinition(String blockDefinitionName) {
            BlockDefinition blockDefinition = null;
            for (int i=0; i < config.alBlockDefinitions.size(); i++) {
                blockDefinition = (BlockDefinition) config.alBlockDefinitions.get(i);
                if (blockDefinitionName.equals(blockDefinition.getName())) {
                    // Update mAlBlockingSources
                    ArrayList alBlockBys = blockDefinition.getBlockRule().getBlockBys();
                    for (int j=0; j < alBlockBys.size(); j++) {
                        BlockBy blockBy = (BlockBy) alBlockBys.get(j);
                        if (mAlBlockingSources.contains(blockBy.source)) {
                            mAlBlockingSources.remove(blockBy.source);
                        }
                    }

                    config.alBlockDefinitions.remove(i);
                    break;
                }
            }
        }

        public void setSelectedBlockDefinitions(ArrayList al) {
            config.alBlockDefinitions = al;
            ArrayList alNames = new ArrayList();
            
            for (int i=0; i < config.alBlockDefinitions.size(); i++) {
                BlockDefinition bd = (BlockDefinition) config.alBlockDefinitions.get(i);
                alNames.add(bd.getName());
            }
            setSelectedBlockDefinitionNames(alNames);
        }
        
        public ArrayList getSelectedBlockDefinitions() {
            return config.alBlockDefinitions;
        }
        
        public void setSelectedBlockDefinitionNames(ArrayList al) {
            config.alBlockDefinitionNames = al;
        }
        
        public ArrayList getSelectedBlockDefinitionNames() {
            config.alBlockDefinitionNames = new ArrayList();
            for (int i=0; config.alBlockDefinitions!= null && i < config.alBlockDefinitions.size(); i++) {
                BlockDefinition bd = (BlockDefinition) config.alBlockDefinitions.get(i);
                config.alBlockDefinitionNames.add(bd.getName());
            }
            return config.alBlockDefinitionNames;
        }
    }
    
    class QueryBuilderConfig {
        String moduleName = "QueryBuilder";
        String parserClass = "com.sun.mdm.index.configurator.impl.querybuilder.QueryBuilderConfiguration";
        ArrayList alQueryBuilders = new ArrayList(); // of QueryBuilder
        
        QueryBuilder getNewQueryBuilder() {
            QueryBuilder queryBuilder = new QueryBuilder();
            alQueryBuilders.add(queryBuilder);
            return queryBuilder;
        }
        
        QueryBuilder getQueryBuilderByName(String queryBuilderName) {
            for (int i=0; i < alQueryBuilders.size(); i++) {
                QueryBuilder queryBuilder = (QueryBuilder) alQueryBuilders.get(i);
                if (queryBuilderName.equals(queryBuilder.queryBuilderName)) {
                    return queryBuilder;
                }
            }
            return null;
        }
        
        void deleteQueryBuilderByName(String queryBuilderName) {
            QueryBuilder queryBuilder = getQueryBuilderByName(queryBuilderName);
            if (queryBuilder != null) {
                alQueryBuilders.remove(queryBuilder);
            }
        }
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
     *  return XML String for this QueryType
     *
     */
    
    public String getXMLString() {
        StringBuffer buffer = new StringBuffer();
        
        // @Todo change this hardcoded code to elements retrieved during parsing
        
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Utils.LINE);
        
        buffer.append("<Configuration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"sbyn:SeeBeyond/eView/schema/query.xsd\">" + Utils.LINE);
        buffer.append(Utils.TAB + "<" + mTagQueryBuilderConfig);
        
        buffer.append(Utils.quoteAttribute(mTagModuleName, mQueryBuilderConfig.moduleName));
        buffer.append(Utils.quoteAttribute(mTagParserClass, mQueryBuilderConfig.parserClass));
        buffer.append(">" + Utils.LINE);
               
        // Basic Search
        for ( int i = 0; i < mQueryBuilderConfig.alQueryBuilders.size(); i++) {
            QueryBuilder qbuilder = (QueryBuilder) mQueryBuilderConfig.alQueryBuilders.get(i);
            if (!qbuilder.isBlockerSearch()) {
                String qbString = getQueryBuilderXML(qbuilder);
                buffer.append(qbString);
            }
        }
        // Blocking queries
        for ( int i = 0; i < mQueryBuilderConfig.alQueryBuilders.size(); i++) {
            QueryBuilder qbuilder = (QueryBuilder) mQueryBuilderConfig.alQueryBuilders.get(i);
            if (qbuilder.isBlockerSearch()) {
                String qbString = getQueryBuilderXML(qbuilder);
                buffer.append(qbString);
            }
        }

        buffer.append(Utils.TAB + Utils.endTag(mTagQueryBuilderConfig));
        buffer.append(Utils.endTag(mTagConfiguration));        
        
        return buffer.toString();
    }

    
    public String getQueryBuilderXML(QueryBuilder querybuilder) {
        StringBuffer bufblockDef = new StringBuffer();
        
        String qbName = querybuilder.getQueryBuilderName();
        String qbClass = querybuilder.getQueryBuilderClass();
        String qbparser = querybuilder.getParserClass();
        String standardize = "false";
        String phoneticize = "false";
        if (querybuilder.getStandardize()) {
            standardize = "true";
        }
        
        if (querybuilder.getPhoneticize()) {
            phoneticize = "true";
        }
        bufblockDef.append(Utils.TAB2 + "<query-builder");
        bufblockDef.append(Utils.quoteAttribute("name", qbName));
        bufblockDef.append(Utils.quoteAttribute("class", qbClass));
        bufblockDef.append(Utils.quoteAttribute("parser-class", qbparser));
        bufblockDef.append(Utils.quoteAttribute("standardize", standardize));
        bufblockDef.append(Utils.quoteAttribute("phoneticize", phoneticize));
        bufblockDef.append(">" + Utils.LINE);
                
        bufblockDef.append(Utils.TAB3 + Utils.startTag(mTagConfig));
        if (querybuilder.optionKey != null) { // Basic Search
            bufblockDef.append(Utils.TAB4 + Utils.LEFT + mTagOption);
            bufblockDef.append(Utils.quoteAttribute( "key", querybuilder.optionKey));
            bufblockDef.append(Utils.quoteAttribute( "value", querybuilder.optionValue));
            bufblockDef.append("/>" + Utils.LINE);
        } else {                            // Blocker Search
            List list = querybuilder.config.alBlockDefinitions;
            for ( int i = 0; i < list.size(); i++ ) {
                BlockDefinition def = (BlockDefinition) list.get(i);
                String name = def.getName();
                bufblockDef.append(Utils.TAB4 + Utils.quoteElementAttribute(mTagBlockDefinition, mTagNumber, name) + Utils.LINE);
                bufblockDef.append(Utils.TAB5 + Utils.startTagNoLine(mTagHint));
                String hint = def.getHint();
                if (hint != null) {
                    bufblockDef.append(hint);
                }
                bufblockDef.append(Utils.endTag(mTagHint));
                BlockRule brule = def.getBlockRule();

                List blockBys = brule.getBlockBys();
                bufblockDef.append(Utils.TAB5 + Utils.startTag(mTagBlockRule));
                  
                for (int k = 0; k < blockBys.size(); k++) {
                    BlockBy blockBy = (BlockBy) blockBys.get(k);
                    String field = blockBy.getField();
                    String source = blockBy.getSource();
                    String operator = blockBy.getOperator();
                    bufblockDef.append(Utils.TAB6 + Utils.startTag(operator));
                    bufblockDef.append(Utils.TAB7 + "<" + mTagField + ">" + field + Utils.endTag(mTagField));
                    if (blockBy.useConstant()) {
                        bufblockDef.append(Utils.TAB7 + "<" + mTagConstant + ">" + source + Utils.endTag(mTagConstant));
                    } else {
                        bufblockDef.append(Utils.TAB7 + "<" + mTagSource + ">" + source + Utils.endTag(mTagSource));
                    }
                    if (operator.equals("range")) {
                        String lowerBoundtype = blockBy.getLowerBoundType();
                        String upperBoundtype = blockBy.getUpperBoundType();
                        if (lowerBoundtype != null || upperBoundtype != null) {
                            bufblockDef.append(Utils.TAB7 + Utils.startTag(mTagDefault));
                            if (lowerBoundtype != null && (lowerBoundtype.equals("constant") || lowerBoundtype.equals("offset"))) {
                                bufblockDef.append(Utils.TAB8 + Utils.LEFT + mTagLowerBound);
                                bufblockDef.append(Utils.quoteAttribute("type", lowerBoundtype) + ">");
                                bufblockDef.append(blockBy.getLowerBoundValue() + Utils.endTag(mTagLowerBound));
                            }
                            if (upperBoundtype != null && (upperBoundtype.equals("constant") || upperBoundtype.equals("offset"))) {
                                bufblockDef.append(Utils.TAB8 + Utils.LEFT + mTagUpperBound);
                                bufblockDef.append(Utils.quoteAttribute("type", upperBoundtype) + Utils.RIGHT);
                                bufblockDef.append(blockBy.getUpperBoundValue() + Utils.endTag(mTagUpperBound));
                            }
                            bufblockDef.append(Utils.TAB7 + Utils.endTag(mTagDefault));
                        }
                    }
                    bufblockDef.append(Utils.TAB6 + Utils.endTag(operator));
                }
                bufblockDef.append(Utils.TAB5 + Utils.endTag(mTagBlockRule));

                bufblockDef.append(Utils.TAB4 + Utils.endTag(mTagBlockDefinition));
            }
        }
        bufblockDef.append(Utils.TAB3 + Utils.endTag(mTagConfig));
        bufblockDef.append(Utils.TAB2 + Utils.endTag(mTagQueryBuilder));
        return bufblockDef.toString();
    }
}
