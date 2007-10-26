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
 * @author kkao
 * @version
 */
public class EDMType {
    private final String mFmt = "                [EDMType]";
    private final String mTagEDM = "edm";
    private final String mTagNode = "node-";
    private final String mAttrDisplayOrder = "display-order";
    private final String mTagField = "field-";              // -> EDMFieldDef
    private final String mTagDisplayName = "display-name";  // -> EDMFieldDef.displayName
    private final String mTagDisplayOrder = "display-order";// -> EDMFieldDef.displayOrder
    private final String mTagMaxLength = "max-length";      // -> EDMFieldDef.maxLength
    private final String mTagGuiType = "gui-type";          // -> EDMFieldDef.guiType
    private final String mTagValueType = "value-type";      // -> EDMFieldDef.valueType
    private final String mTagKeyType = "key-type";          // -> EDMFieldDef.keyType
    private final String mTagValueList = "value-list";      // -> EDMFieldDef.valueList
    private final String mTagValueMask = "value-mask";      // -> EDMFieldDef.valueMask
    private final String mTagInputMask = "input-mask";      // -> EDMFieldDef.inputMask
    
    private final String mTagImplDetails = "impl-details";
    private final String mTagMasterControllerJndiName = "master-controller-jndi-name";
    private final String mTagValidationServiceJndiName = "validation-service-jndi-name";
    private final String mTagUsercodeJndiName = "usercode-jndi-name";
    private final String mTagReportgeneratorJndiName = "reportgenerator-jndi-name";
    private final String mTagDebugFlag = "debug-flag";
    private final String mTagDebugDest = "debug-dest";
    private final String mTagEnableSecurity = "enable-security";
    
    private final String mTagGuiDefinition = "gui-definition";
    private final String mTagSystemDisplayNameOverrides = "system-display-name-overrides";
    private final String mTagLocalIdHeader = "local-id-header";
    private final String mTagLocalId = "local-id";
    private final String mTagPageDefinition = "page-definition";
    private final String mTagInitialScreen = "initial-screen";
    private final String mTagEoSearch = "eo-search";
    private final String mTagRootObject = "root-object";
    private final String mTagTabName = "tab-name";
    private final String mTagTabEntrance = "tab-entrance";
    private final String mTagSimpleSearchPage = "simple-search-page";
    private final String mTagScreenTitle = "screen-title";
    private final String mTagFieldPerRow = "field-per-row";
    private final String mTagShowEuid = "show-euid";
    private final String mTagShowLid = "show-lid";
    private final String mTagSearchOption = "search-option";
    //private final String mTagDisplayName = "display-name";
    private final String mTagQueryBuilder = "query-builder";
    private final String mTagWeighted = "weighted";
    private final String mTagCandidateThreshold = "candidate-threshold";
    private final String mTagParameter = "parameter";
    private final String mTagParameterName = "name";
    private final String mTagParameterValue = "value";

    private final String mTagFieldGroup = "field-group";    
    private final String mTagFieldRef = "field-ref";        // -> EDMFieldDef.usedInSearchScreen
    private final String mAttrRequired = "required";        // -> EDMFieldDef.requiredInSearchScreen
    private final String mTagDescription = "description";
    
    private final String mTagSearchResultListPage = "search-result-list-page";
    private final String mTagItemPerPage = "item-per-page";
    private final String mTagMaxResultSize = "max-result-size";
    private final String mTagEoViewPage = "eo-view-page";

    private final String mTagCreateEo = "create-eo";
    private final String mTagHistory = "history";
    private final String mTagXASearchPage = "xa-search-page";
    private final String mTagMatchingReview = "matching-review";
    private final String mTagPDSearchPage = "pd-search-page";
    
    private final String mTagReports = "reports";
    private final String mTagSearchPageFieldPerRow = "search-page-field-per-row";
    private final String mTagReport = "report";
    private final String mTagName = "name";
    private final String mTagTitle = "title";
    private final String mTagEnable = "enable";
    private final String mTagFields = "fields";
    private final String mTagAuditLog = "audit-log";
    private final String mTagAllowInsert = "allow-insert";

    public final String SEARCHSCREEN = "searchScreen";
    public final String SEARCHRESULT = "searchResult";
    public final String REPORT = "report";
    
    private String strDisplayName;
    private String strDisplayOrder;
    private String strMaxLength;
    private String strGuiType;
    private String strValueType;
    private String strKeyType;
    private String strValueList;
    private String strInputMask;
    private String strValueMask;

    private ArrayList mAlEDMNodes = null;
    private SystemDisplayNameOverrides mSystemDisplayNameOverrides;
    private ImplDetails mImplDetails = new ImplDetails();
    private PageDefinition mPageDefinition = new PageDefinition();
    private boolean mModified = false;

    /**
     * default constructor
     */
    public EDMType() {
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
    
    String getXASearchPageXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB4 + Utils.startTag(mTagXASearchPage));

        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                        mPageDefinition.history.xaSearchPage.fieldPerRow + 
                        Utils.endTag(mTagFieldPerRow));

        buffer.append(Utils.TAB4 + Utils.endTag(mTagXASearchPage));

        return buffer.toString();

    }
    
    void parseXASearchPage(Node node) {
        mPageDefinition.history.xaSearchPage.fieldPerRow = parseFieldPerRow(node);
    }
    
    //mTagHistory
    String getHistoryXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagHistory));
        buffer.append(getPageTabXML(mPageDefinition.history.pageTab));
        buffer.append(getXASearchPageXML());
        buffer.append(getSearchResultListPageXML(mPageDefinition.history.searchResultListPage));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagHistory));

        return buffer.toString();

    }
    
    /*
     <history>
        <root-object>Person</root-object>
        <tab-name>History</tab-name>
        <tab-entrance>/EnterXASearchAction.do</tab-entrance>
        <xa-search-page>
            <field-per-row>2</field-per-row>
        </xa-search-page>
        <search-result-list-page>
            <item-per-page>10</item-per-page>
            <max-result-size>100</max-result-size>
        </search-result-list-page>
    </history>

     */
    void parseHistory(Node node) {
        //mPageDefinition.history;
        if (node.hasChildNodes()) {
            //PageDefinition.EOSearch
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    // PageDefinition.PageTab
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagRootObject)) {
                        mPageDefinition.history.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        mPageDefinition.history.pageTab.tabName = value;
                    } else if (name.equals(mTagTabEntrance)) {
                        mPageDefinition.history.pageTab.tabEntrance = value;
                    } else if (name.equals(mTagXASearchPage)) {
                        parseXASearchPage(nl.item(i));
                    } else if (name.equals(mTagSearchResultListPage)) {
                        parseSearchResultListPage(nl.item(i), mPageDefinition.history.searchResultListPage);
                    }
                }
            }
        }
    }
    
    String getPDSearchPageXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB4 + Utils.startTag(mTagPDSearchPage));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                        mPageDefinition.matchReview.pdSearchPage.fieldPerRow + 
                        Utils.endTag(mTagFieldPerRow));
        buffer.append(Utils.TAB4 + Utils.endTag(mTagPDSearchPage));

        return buffer.toString();

    }
    
    void parsePDSearchPage(Node node) {
        mPageDefinition.matchReview.pdSearchPage.fieldPerRow = parseFieldPerRow(node);
    }
    
    //mTagMatchingReview
    String getMatchingReviewXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagMatchingReview));
        buffer.append(getPageTabXML(mPageDefinition.matchReview.pageTab));
        buffer.append(getPDSearchPageXML());
        buffer.append(getSearchResultListPageXML(mPageDefinition.matchReview.searchResultListPage));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagMatchingReview));

        return buffer.toString();
    }

    /*
        <matching-review>
            <root-object>Person</root-object>
            <tab-name>Matching Review</tab-name>
            <tab-entrance>/EnterPDSearchAction.do</tab-entrance>
            <pd-search-page>
                <field-per-row>2</field-per-row>
            </pd-search-page>
            <search-result-list-page>
                <item-per-page>10</item-per-page>
                <max-result-size>100</max-result-size>
            </search-result-list-page>
        </matching-review>

     */
    void parseMatchingReview(Node node) {
        //mPageDefinition.matchReview;
        if (node.hasChildNodes()) {
            //PageDefinition.EOSearch
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    // PageDefinition.PageTab
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagRootObject)) {
                        mPageDefinition.matchReview.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        mPageDefinition.matchReview.pageTab.tabName = value;
                    } else if (name.equals(mTagTabEntrance)) {
                        mPageDefinition.matchReview.pageTab.tabEntrance = value;
                    } else if (name.equals(mTagPDSearchPage)) {
                        parsePDSearchPage(nl.item(i));
                    } else if (name.equals(mTagSearchResultListPage)) {
                        parseSearchResultListPage(nl.item(i), mPageDefinition.matchReview.searchResultListPage);
                    }
                }
            }
        }
    }
    
    //mTagAuditLog
    String getAuditLogXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagAuditLog));
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagAllowInsert) +
                            mPageDefinition.auditLog.allowInsert + 
                            Utils.endTag(mTagAllowInsert));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagAuditLog));

        return buffer.toString();

    }

    void parseAuditLog(Node node) {
        mPageDefinition.auditLog.allowInsert = parseOneTag(node, mTagAllowInsert);
    }
    
    String getReportXML(PageDefinition.Report report) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(Utils.TAB4 + "<report name=\"" + report.name + "\" title=\"" +
                            report.title + "\">" + Utils.LINE);
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagEnable) +
                            report.enable + 
                            Utils.endTag(mTagEnable));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagMaxResultSize) +
                            report.maxResultSize + 
                            Utils.endTag(mTagMaxResultSize));
        
        buffer.append(Utils.TAB5 + Utils.startTag(mTagFields));
        for (int j=0; j < report.alFieldRef.size(); j++) {
            PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) report.alFieldRef.get(j);
            buffer.append(getFieldRefXML(fieldRef));
        }
        buffer.append(Utils.TAB5 + Utils.endTag(mTagFields));
        
        buffer.append(Utils.TAB4 + Utils.endTag(mTagReport));

        return buffer.toString();

    }
    void parseReport(Node node) {
        PageDefinition.Report report = mPageDefinition.reports.getReport();
        /* block being parsed
            <report name="Assumed Match" title="Assumed Match Report">
                <enable>true</enable>
                <max-result-size>2000</max-result-size>
                <fields>
                    <field-ref>Person.FirstName</field-ref>
                    <field-ref>Person.LastName</field-ref>
                    <field-ref>Person.SSN</field-ref>
                    <field-ref>Person.DOB</field-ref>
                    <field-ref>Address.AddressLine1</field-ref>
                    <field-ref>Address.AddressLine2</field-ref>
                    <field-ref>Phone.Phone</field-ref>
                </fields>
            </report>
        */
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attribute = nnm.getNamedItem(mTagName);
            if (attribute != null) {
                report.name = attribute.getNodeValue();
            }
            attribute = nnm.getNamedItem(mTagTitle);
            if (attribute != null) {
                report.title = attribute.getNodeValue();
            }
        }

        if (node.hasChildNodes()) {
            report.enable = parseOneTag(node, mTagEnable);
            report.maxResultSize = parseOneTag(node, mTagMaxResultSize);
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    if (mTagFields.equals(name)) {
                        NodeList nl1 = nl.item(i).getChildNodes();
                        for (int j = 0; j < nl1.getLength(); j++) {
                            if (nl1.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                String tag = ((Element) nl1.item(j)).getTagName();
                                String value = Utils.getStrElementValue(nl1.item(j));
                                if (mTagFieldRef.equals(tag)) {
                                    PageDefinition.FieldRef fieldRef = mPageDefinition.getFieldRef(report.alFieldRef);
                                    fieldRef.fieldName = value;
                                    int index = value.indexOf('.');
                                    String parentName = value.substring(0, index);
                                    String fieldName = value.substring(index + 1);
                                    EDMFieldDef edmFieldDef = getEDMFieldDef(parentName, fieldName);
                                    if (edmFieldDef != null) {
                                        edmFieldDef.report = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    //mTagReports
    String getReportsXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagReports));
        buffer.append(getPageTabXML(mPageDefinition.reports.pageTab));
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagSearchPageFieldPerRow) + 
                        mPageDefinition.reports.searchPageFieldPerRow + 
                        Utils.endTag(mTagSearchPageFieldPerRow));
        for (int i=0; i<mPageDefinition.reports.alReport.size(); i++) {
            PageDefinition.Report report = (PageDefinition.Report) mPageDefinition.reports.alReport.get(i);
            buffer.append(getReportXML(report));
        }
        buffer.append(Utils.TAB3 + Utils.endTag(mTagReports));

        return buffer.toString();

    }
    
    void parseReports(Node node) {
        //mPageDefinition.reports;
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    // PageDefinition.PageTab
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagRootObject)) {
                        mPageDefinition.reports.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        mPageDefinition.reports.pageTab.tabName = value;
                    } else if (name.equals(mTagTabEntrance)) {
                        mPageDefinition.reports.pageTab.tabEntrance = value;
                    } else if (name.equals(mTagSearchPageFieldPerRow)) {
                        mPageDefinition.reports.searchPageFieldPerRow = value;
                    } else if (name.equals(mTagReport)) {
                        parseReport(nl.item(i));
                    }
                }
            }
        }
    }
    
    //mTagCreateEo
    String getCreateEoXML() {    //mTagCreateEo
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagCreateEo));
        buffer.append(getPageTabXML(mPageDefinition.createEO.pageTab));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagCreateEo));

        return buffer.toString();

    }

    void parseCreateEo(Node node) {
        if (node.hasChildNodes()) {
            //PageDefinition.createEO
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    // PageDefinition.PageTab
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagRootObject)) {
                        mPageDefinition.createEO.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        mPageDefinition.createEO.pageTab.tabName = value;
                    } else if (name.equals(mTagTabEntrance)) {
                        mPageDefinition.createEO.pageTab.tabEntrance = value;
                    }
                }
            }
        }
    }
    
    String parseOneTag(Node node, String targetTag) {
        String valueRet = "";
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (targetTag.equals(tag)) {
                        valueRet = value;
                        break;
                    }
                }
            }
        }
        return valueRet;
    }
    
    String parseFieldPerRow(Node node) {
        return parseOneTag(node, mTagFieldPerRow);
    }
    
    String getEoViewPageXML(PageDefinition.EOViewPage eoViewPage) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Utils.TAB4 + Utils.startTag(mTagEoViewPage));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                        eoViewPage.fieldPerRow + 
                        Utils.endTag(mTagFieldPerRow));
        buffer.append(Utils.TAB4 + Utils.endTag(mTagEoViewPage));
        return buffer.toString();
    }
    
    void parseEoViewPage(Node node) {
        mPageDefinition.eoSearch.eoViewPage.fieldPerRow = parseFieldPerRow(node);
    }

    String getSearchResultListPageXML(PageDefinition.SearchResultListPage searchResultListPage) {
        if (searchResultListPage == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(Utils.TAB4 + Utils.startTag(mTagSearchResultListPage));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagItemPerPage) + 
                            searchResultListPage.itemPerPage + 
                            Utils.endTag(mTagItemPerPage));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagMaxResultSize) + 
                            searchResultListPage.maxResultSize + 
                            Utils.endTag(mTagMaxResultSize));
        
        for (int j=0; j < searchResultListPage.alFieldRef.size(); j++) {
            PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) searchResultListPage.alFieldRef.get(j);
            buffer.append(getFieldRefXML(fieldRef));
        }

        buffer.append(Utils.TAB4 + Utils.endTag(mTagSearchResultListPage));
        return buffer.toString();
    }
    
    void parseSearchResultListPage(Node node, PageDefinition.SearchResultListPage searchResultListPage) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (mTagItemPerPage.equals(tag)) {
                        searchResultListPage.itemPerPage = value;
                    } else if (mTagMaxResultSize.equals(tag)) {
                        searchResultListPage.maxResultSize = value;
                    } else if (mTagFieldRef.equals(tag)) {
                        PageDefinition.FieldRef fieldRef = mPageDefinition.getFieldRef(searchResultListPage.alFieldRef);
                        fieldRef.fieldName = value;
                        int index = value.indexOf('.');
                        String parentName = value.substring(0, index);
                        String fieldName = value.substring(index + 1);
                        EDMFieldDef edmFieldDef = getEDMFieldDef(parentName, fieldName);
                        if (edmFieldDef != null) {
                            edmFieldDef.setUsedInSearchResult(true);
                        }
                    }
                }
            }
        }
    }
    
    String getSearchOptionXML(ArrayList alSearchOption) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < alSearchOption.size(); i++) {
            PageDefinition.SearchOption searchOption = (PageDefinition.SearchOption) alSearchOption.get(i);
            buffer.append(Utils.TAB5 + Utils.startTag(mTagSearchOption));
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagDisplayName) + 
                            searchOption.displayName + 
                            Utils.endTag(mTagDisplayName));
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagQueryBuilder) + 
                            searchOption.queryBuilder + 
                            Utils.endTag(mTagQueryBuilder));
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagWeighted) + 
                            searchOption.weighted + 
                            Utils.endTag(mTagWeighted));
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagCandidateThreshold) + 
                            searchOption.candidateThreshold + 
                            Utils.endTag(mTagCandidateThreshold));
            
            for (int j=0; j < searchOption.alParameter.size(); j++) {
                PageDefinition.SearchOption.Parameter parameter = (PageDefinition.SearchOption.Parameter) searchOption.alParameter.get(j);
                buffer.append(Utils.TAB6 + Utils.startTag(mTagParameter));
                buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagParameterName) + 
                                parameter.name + 
                                Utils.endTag(mTagParameterName));
                buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagParameterValue) + 
                                parameter.value + 
                                Utils.endTag(mTagParameterValue));
                buffer.append(Utils.TAB6 + Utils.endTag(mTagParameter));
            }
            buffer.append(Utils.TAB5 + Utils.endTag(mTagSearchOption));
        }

        return buffer.toString();
    }
    
    void parseSearchOption(Node node, PageDefinition.SimpleSearchPage simpleSearchPage) {
        if (node.hasChildNodes()) {
            PageDefinition.SearchOption searchOption = simpleSearchPage.getSearchOption();
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (mTagDisplayName.equals(tag)) {
                        searchOption.displayName = value;
                    } else if (mTagQueryBuilder.equals(tag)) {
                        searchOption.queryBuilder = value;
                    } else if (mTagWeighted.equals(tag)) {
                        searchOption.weighted = value;
                    } else if (mTagCandidateThreshold.equals(tag)) {
                        searchOption.candidateThreshold = value;
                    } else if (mTagParameter.equals(tag)) {
                        NodeList nl2 = nl.item(i).getChildNodes();
                        PageDefinition.SearchOption.Parameter parameter = searchOption.getParameter();
                        for (int j = 0; j < nl2.getLength(); j++) {
                            if (nl2.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                String tag2 = ((Element) nl2.item(j)).getTagName();
                                String value2 = Utils.getStrElementValue(nl2.item(j));
                                if (mTagParameterName.equals(tag2)) {
                                    parameter.name = value2;
                                } else if (mTagParameterValue.equals(tag2)) {
                                    parameter.value = value2;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    String getFieldRefXML(PageDefinition.FieldRef fieldRef) {
        StringBuffer buffer = new StringBuffer();
        if (fieldRef.required != null) {
            buffer.append(Utils.TAB6 + "<field-ref required=\"" + fieldRef.required + "\">" +
                            fieldRef.fieldName + 
                            Utils.endTag(mTagFieldRef));
        } else {
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagFieldRef) +
                            fieldRef.fieldName + 
                            Utils.endTag(mTagFieldRef));

        }

        return buffer.toString();
    }
    
    String getSearchFieldGroupXML(ArrayList alFieldGroup) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < alFieldGroup.size(); i++) {
            PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(i);
            buffer.append(Utils.TAB5 + Utils.startTag(mTagFieldGroup));
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagDescription) + 
                            fieldGroup.description + 
                            Utils.endTag(mTagDescription));
            
            for (int j=0; j < fieldGroup.alFieldRef.size(); j++) {
                PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) fieldGroup.alFieldRef.get(j);
                buffer.append(getFieldRefXML(fieldRef));
            }
            buffer.append(Utils.TAB5 + Utils.endTag(mTagFieldGroup));
        }

        return buffer.toString();
    }
    
    void parseSearchFieldGroup(Node node, PageDefinition.SimpleSearchPage simpleSearchPage) {
        /*
                <field-group>
                    <description>Person</description>
                    <field-ref required="false">Person.FirstName</field-ref>
                    <field-ref required="false">Person.LastName</field-ref>
                    <field-ref required="false">Person.SSN</field-ref>
                </field-group>
         */
        //simpleSearchPage.alFieldGroup
        if (node.hasChildNodes()) {
            PageDefinition.FieldGroup fieldGroup = simpleSearchPage.getFieldGroup();
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (mTagDescription.equals(tag)) {
                        fieldGroup.description = value;
                    } else if (mTagFieldRef.equals(tag)) {
                        PageDefinition.FieldRef fieldRef = mPageDefinition.getFieldRef(fieldGroup.alFieldRef);
                        fieldRef.fieldName = value;
                        
                        int index = value.indexOf('.');
                        String parentName = value.substring(0, index);
                        String fieldName = value.substring(index + 1);
                        EDMFieldDef edmFieldDef = getEDMFieldDef(parentName, fieldName);
                        if (edmFieldDef != null) {
                            edmFieldDef.setUsedInSearchScreen(true);
                            NamedNodeMap nnm = nl.item(i).getAttributes();
                            if (nnm != null) {
                                String requiredValue = "false";
                                Node required = nnm.getNamedItem(mAttrRequired);
                                try {
                                    if (required != null) {
                                        requiredValue = required.getNodeValue(); // "true" or "false"
                                    }
                                    edmFieldDef.setRequiredInSearchScreen(requiredValue);
                                    fieldRef.required = requiredValue;
                                } catch (DOMException ex) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    String getSimpleSearchPageXML(PageDefinition.SimpleSearchPage simpleSearchPage) {
        if (simpleSearchPage == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        //mTagSimpleSearchPage
        buffer.append(Utils.TAB4 + Utils.startTag(mTagSimpleSearchPage));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagScreenTitle) + 
                        simpleSearchPage.screenTitle + 
                        Utils.endTag(mTagScreenTitle));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                        simpleSearchPage.fieldPerRow + 
                        Utils.endTag(mTagFieldPerRow));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagShowEuid) + 
                        simpleSearchPage.showEuid + 
                        Utils.endTag(mTagShowEuid));
        buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagShowLid) + 
                        simpleSearchPage.showLid + 
                        Utils.endTag(mTagShowLid));
        buffer.append(getSearchFieldGroupXML(simpleSearchPage.alFieldGroup));
        buffer.append(getSearchOptionXML(simpleSearchPage.alSearchOption));
        buffer.append(Utils.TAB4 + Utils.endTag(mTagSimpleSearchPage));
        return buffer.toString();

    }
    
    void parseSimpleSearchPage(Node node, PageDefinition.SimpleSearchPage simpleSearchPage) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagScreenTitle)) {
                        simpleSearchPage.screenTitle = value;
                    } else if (name.equals(mTagFieldPerRow)) {
                        simpleSearchPage.fieldPerRow = value;
                    } else if (name.equals(mTagShowEuid)) {
                        simpleSearchPage.showEuid = value;
                    } else if (name.equals(mTagShowLid)) {
                        simpleSearchPage.showLid = value;
                    } else if (name.equals(mTagFieldGroup)) {
                        parseSearchFieldGroup(nl.item(i), simpleSearchPage);
                    } else if (name.equals(mTagSearchOption)) {
                        parseSearchOption(nl.item(i), simpleSearchPage);
                    }
                }
            }
        }
    }
    
    // PageTab
    String getPageTabXML(PageDefinition.PageTab pageTab) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagRootObject) + 
                        pageTab.rootObject + 
                        Utils.endTag(mTagRootObject));
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagTabName) + 
                        pageTab.tabName + 
                        Utils.endTag(mTagTabName));
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagTabEntrance) + 
                        pageTab.tabEntrance + 
                        Utils.endTag(mTagTabEntrance));
        return buffer.toString();
    }
    
    //mTagCreateEo
    String getEoSearchXML() {    //mTagEoSearch
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagEoSearch));
        buffer.append(getPageTabXML(mPageDefinition.eoSearch.pageTab));
        ArrayList alSimpleSearchPages = mPageDefinition.eoSearch.alSimpleSearchPages;
        for (int i=0; i < alSimpleSearchPages.size(); i++) {
            PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
            buffer.append(getSimpleSearchPageXML(simpleSearchPage));
        }
        buffer.append(getSearchResultListPageXML(mPageDefinition.eoSearch.searchResultListPage));
        buffer.append(getEoViewPageXML(mPageDefinition.eoSearch.eoViewPage));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagEoSearch));

        return buffer.toString();

    }
    
    void parseEoSearch(Node node) {
        if (node.hasChildNodes()) {
            //PageDefinition.EOSearch
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    // PageDefinition.PageTab
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagRootObject)) {
                        mPageDefinition.eoSearch.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        mPageDefinition.eoSearch.pageTab.tabName = value;
                    } else if (name.equals(mTagTabEntrance)) {
                        mPageDefinition.eoSearch.pageTab.tabEntrance = value;
                    } else if (name.equals(mTagSimpleSearchPage)) {
                        PageDefinition.SimpleSearchPage simpleSearchPage = mPageDefinition.eoSearch.getSimpleSearchPage();
                        parseSimpleSearchPage(nl.item(i), simpleSearchPage);
                    } else if (name.equals(mTagSearchResultListPage)) {
                        parseSearchResultListPage(nl.item(i), mPageDefinition.eoSearch.searchResultListPage);
                    } else if (name.equals(mTagEoViewPage)) {
                        parseEoViewPage(nl.item(i));
                    }
                }
            }
        }
    }
    
    /**
     *       <local-id-header>National Health Service Number</local-id-header>
     *       <local-id>NHS Number</local-id>
     */
    void parseSystemDisplayNameOverrides(Node node) {
        if (node.hasChildNodes()) {
            mSystemDisplayNameOverrides = new SystemDisplayNameOverrides("", "");
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagLocalIdHeader)) {
                        mSystemDisplayNameOverrides.setLocalIdHeader(value);
                    } else if (name.equals(mTagLocalId)) {
                        mSystemDisplayNameOverrides.setLocalId(value);
                    }
                }
            }
        }

    }
    
    void parsePageDefinition(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    if (name.equals(mTagInitialScreen)) {
                        mPageDefinition.initialScreen = Utils.getStrElementValue(nl.item(i));
                    } else if (name.equals(mTagEoSearch)) {
                        parseEoSearch(nl.item(i));
                    } else if (name.equals(mTagCreateEo)) {
                        parseCreateEo(nl.item(i));
                    } else if (name.equals(mTagHistory)) {
                        parseHistory(nl.item(i)); 
                    } else if (name.equals(mTagMatchingReview)) {
                        parseMatchingReview(nl.item(i));
                    } else if (name.equals(mTagReports)) {
                        parseReports(nl.item(i));
                    } else if (name.equals(mTagAuditLog)) {
                        parseAuditLog(nl.item(i));
                    }
                }
            }
        }
    }
    
    void parseGuiDefinition(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    
                    if (name.equals(mTagSystemDisplayNameOverrides)) {
                        parseSystemDisplayNameOverrides(nl.item(i));
                    } else if (name.equals(mTagPageDefinition)) {
                        parsePageDefinition(nl.item(i));
                    }
                }
            }
        }
    }
    
    String getPageDefinitionXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB2 + Utils.startTag(mTagPageDefinition));
        // mPageDefinition.initialScreen
        buffer.append(Utils.TAB3 + Utils.startTagNoLine(mTagInitialScreen) + 
                        mPageDefinition.initialScreen + 
                        Utils.endTag(mTagInitialScreen));
        // mTagEoSearch
        buffer.append(getEoSearchXML());
        
        //mTagCreateEo
        buffer.append(getCreateEoXML());
        
        //mTagHistory
        buffer.append(getHistoryXML());
        
        //mTagMatchingReview)) {
        buffer.append(getMatchingReviewXML());
        
        //mTagReports)) {
        buffer.append(getReportsXML());
        
        //mTagAuditLog)) {
        buffer.append(getAuditLogXML());
        buffer.append(Utils.TAB2 + Utils.endTag(mTagPageDefinition));

        return buffer.toString();

    }
    
    String getSystemDisplayNameOverridesXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB2 + Utils.startTag(mTagSystemDisplayNameOverrides));
        buffer.append(Utils.TAB3 + Utils.startTagNoLine(mTagLocalIdHeader) + 
                        mSystemDisplayNameOverrides.getLocalIdHeader() + 
                        Utils.endTag(mTagLocalIdHeader));
        buffer.append(Utils.TAB3 + Utils.startTagNoLine(mTagLocalId) + 
                        mSystemDisplayNameOverrides.getLocalId() + 
                        Utils.endTag(mTagLocalId));
        buffer.append(Utils.TAB2 + Utils.endTag(mTagSystemDisplayNameOverrides));

        return buffer.toString();

    }
    
    public String getGuiDefinitionXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB + Utils.startTag(mTagGuiDefinition));
        if (mSystemDisplayNameOverrides != null) {
            buffer.append(getSystemDisplayNameOverridesXML());
        }
        buffer.append(getPageDefinitionXML());
        buffer.append(Utils.TAB + Utils.endTag(mTagGuiDefinition));

        return buffer.toString();
    }
    
    public String getImplDetailsXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB + Utils.startTag(mTagImplDetails));
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagMasterControllerJndiName) + 
                        mImplDetails.masterControllerJndiName + 
                        Utils.endTag(mTagMasterControllerJndiName));
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagValidationServiceJndiName) + 
                        mImplDetails.validationServiceJndiName + 
                        Utils.endTag(mTagValidationServiceJndiName));
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagUsercodeJndiName) + 
                        mImplDetails.usercodeJndiName + 
                        Utils.endTag(mTagUsercodeJndiName));
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagReportgeneratorJndiName) + 
                        mImplDetails.reportgeneratorJndiName + 
                        Utils.endTag(mTagReportgeneratorJndiName));
        
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagDebugFlag) + 
                        mImplDetails.debugFlag + 
                        Utils.endTag(mTagDebugFlag));
        
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagDebugDest) + 
                        mImplDetails.debugDest + 
                        Utils.endTag(mTagDebugDest));
        
        buffer.append(Utils.TAB2 + Utils.startTagNoLine(mTagEnableSecurity) + 
                        mImplDetails.enableSecurity + 
                        Utils.endTag(mTagEnableSecurity));

        buffer.append(Utils.TAB + Utils.endTag(mTagImplDetails));

        return buffer.toString();
    }
    /*
  <master-controller-jndi-name>ejb/PrisonerMasterController</master-controller-jndi-name>
  <validation-service-jndi-name>ejb/PrisonerCodeLookup</validation-service-jndi-name>
  <usercode-jndi-name>ejb/PrisonerUserCodeLookup</usercode-jndi-name>
  <reportgenerator-jndi-name>ejb/PrisonerReportGenerator</reportgenerator-jndi-name>
  <debug-flag>true</debug-flag>
  <debug-dest>console</debug-dest>
  <enable-security>false</enable-security>

     */
    class ImplDetails {
        String masterControllerJndiName = "ejb/PrisonerMasterController";
        String validationServiceJndiName = "ejb/PrisonerCodeLookup";
        String usercodeJndiName = "ejb/PrisonerMasterController";
        String reportgeneratorJndiName = "ejb/PrisonerReportGenerator";
        String debugFlag = "true";
        String debugDest = "console";
        String enableSecurity = "false";
        
        String getMasterControllerJndiName() {
            return masterControllerJndiName;
        }
        
        String getValidationServiceJndiName() {
            return validationServiceJndiName;
        }
        
        String getUsercodeJndiName() {
            return usercodeJndiName;
        }
        
        String getReportgeneratorJndiName() {
            return reportgeneratorJndiName;
        }
        
        String getDebugFlag() {
            return debugFlag;
        }
        
        String getDebugDest() {
            return debugDest;
        }
        
        String getEnableSecurity() {
            return enableSecurity;
        }
    }
    
    private void parseImplDetails(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagMasterControllerJndiName)) {
                        mImplDetails.masterControllerJndiName = value;
                    } else if (name.equals(mTagValidationServiceJndiName)) {
                        mImplDetails.validationServiceJndiName = value;
                    } else if (name.equals(mTagUsercodeJndiName)) {
                        mImplDetails.usercodeJndiName = value;
                    } else if (name.equals(mTagReportgeneratorJndiName)) {
                        mImplDetails.reportgeneratorJndiName = value;
                    } else if (name.equals(mTagDebugFlag)) {
                        mImplDetails.debugFlag = value;
                    } else if (name.equals(mTagDebugDest)) {
                        mImplDetails.debugDest = value;
                    } else if (name.equals(mTagEnableSecurity)) {
                        mImplDetails.enableSecurity = value;
                    }
                }
            }
        }

    }
    
    /** Get EDMFieldDef for field node
    *
    *@param String parentName - parent node
    *@param String fieldName -  field node
    */
    public EDMFieldDef getEDMFieldDef(String parentName, String fieldName) {
        EDMNode parentNode = getEDMNode(parentName);
        EDMFieldDef node = null;
        if (parentNode != null) {
            ArrayList fields = parentNode.fields;
        if (fields != null && fields.size() > 0) {
            for (int i = 0; i < fields.size(); i++) {
                EDMFieldDef tmp = (EDMFieldDef) fields.get(i);
                if (tmp.fieldName.equals(fieldName)) {
                    node = tmp;
                    break;
                }
            }
        }
        }
        return node;
    }
    
    EDMNode getEDMNode(String nodeName) {
        EDMNode node = null;
        if (mAlEDMNodes != null && mAlEDMNodes.size() > 0) {
            for (int i = 0; i < mAlEDMNodes.size(); i++) {
                EDMNode tmp = (EDMNode) mAlEDMNodes.get(i);
                if (tmp.getNodeName().equals(nodeName)) {
                    node = tmp;
                    break;
                }
            }
        }
        return node;
    }

    /**
     * @param node node
     *
     *    <field-Phone>
     *       <display-name>Phone</display-name>
     *       <display-order>2</display-order>
     *       <max-length>20</max-length>
     *       <gui-type>TextBox</gui-type>
     *       <value-type>string</value-type>
     *       <input-mask>(DDD) DDD-DDDD</input-mask>
     *       <value-mask>xDDDxxDDDxDDDD</value-mask>
     *       <key-type>false</key-type>
     *   </field-Phone>
     */
    
    private EDMFieldDef parseEDMFieldNode(String fieldName, Node node) {
        EDMFieldDef edmFieldDef = new EDMFieldDef(fieldName);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (mTagDisplayName.equals(tag)) {
                        edmFieldDef.setDisplayName(value);
                    } else if (mTagDisplayOrder.equals(tag)) {
                        edmFieldDef.setDisplayOrder(value);
                    } else if (mTagMaxLength.equals(tag)) {
                        edmFieldDef.setMaxLength(value);
                    } else if (mTagGuiType.equals(tag)) {
                        edmFieldDef.setGuiType(value);
                    } else if (mTagValueType.equals(tag)) {
                        edmFieldDef.setValueType(value);
                    } else if (mTagKeyType.equals(tag)) {
                        edmFieldDef.setKeyType(value);
                    } else if (mTagValueList.equals(tag)) {
                        edmFieldDef.setValueList(value);
                    } else if (mTagInputMask.equals(tag)) {
                        edmFieldDef.setInputMask(value);
                    } else if (mTagValueMask.equals(tag)) {
                        edmFieldDef.setValueMask(value);
                    }
                }
            }
        }
        return edmFieldDef;
    }
    
    private void parseEDMNode(Node node) {
        if (mAlEDMNodes == null) {
            mAlEDMNodes = new ArrayList();
        }
        String nodeName = node.getNodeName();
        int index = nodeName.indexOf('-');
        nodeName = nodeName.substring(index + 1);
        EDMNode edmNode = new EDMNode();
        edmNode.setNodeName(nodeName);
        NamedNodeMap nnm = node.getAttributes();
        String displayOrder = "0";
        if (nnm != null) {
            Node attrDisplayOrder = nnm.getNamedItem(mAttrDisplayOrder);
            if (attrDisplayOrder != null) {
                try {
                    displayOrder = attrDisplayOrder.getNodeValue();
                } catch (DOMException ex) {
                }
            }
        }
        edmNode.setDisplayOrder(displayOrder);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String fieldName = ((Element) nl.item(i)).getTagName();
                    if (fieldName.startsWith(mTagField)) {
                        int idx = fieldName.indexOf('-');
                        fieldName = fieldName.substring(idx + 1);
                        EDMFieldDef edmFieldDef = parseEDMFieldNode(fieldName, nl.item(i));
                        edmNode.addField(edmFieldDef);
                    }
                }
            }
        }
        mAlEDMNodes.add(edmNode);
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
                     && ((Element) element).getTagName().equals(mTagEDM)
                     && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (name.startsWith(mTagNode)) {
                            parseEDMNode(nl1.item(i1));
                        } else if (name.equals(mTagImplDetails)) {
                            parseImplDetails(nl1.item(i1));
                        } else if (name.equals(mTagGuiDefinition)) {
                            parseGuiDefinition(nl1.item(i1));
                        }
                    }
                }
            }
        }
    }


    class EDMNode {     // Primary object and SubObject node
        String nodeName;
        String displayOrder;
        ArrayList fields; //EDMFieldDef
        
        void addField(EDMFieldDef edmFieldDef) {
            if (fields == null) {
                fields = new ArrayList();
            }
            fields.add(edmFieldDef);
        }
        
        void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }
        
        String getNodeName() {
            return this.nodeName;
        }
        
        void setDisplayOrder(String displayOrder) {
            this.displayOrder = displayOrder;
        }
        
        String getDisplayOrder() {
            return this.displayOrder;
        }
    }
    
    SystemDisplayNameOverrides getmSystemDisplayNameOverrides() {
        return mSystemDisplayNameOverrides;
    }
    
    /*
  <system-display-name-overrides>
   <local-id-header>Prisoner ID</local-id-header>
   <local-id>Prisoner ID</local-id>
  </system-display-name-overrides>
     */
    class SystemDisplayNameOverrides {
        String localIdHeader;
        String localId;
        
        SystemDisplayNameOverrides(String localIdHeader, String localId) {
            this.localIdHeader = localIdHeader;
            this.localId = localId;
        }
        
        void setLocalIdHeader(String value) {
            this.localIdHeader = value;
        }
        
        void setLocalId(String value) {
            this.localId = value;
        }
        
        String getLocalIdHeader() {
            return this.localIdHeader;
        }
        
        String getLocalId() {
            return this.localId;
        }
        
    }
    
    /* 
     *Update referenced field when it is renamed in OBject Definition
     *@param oldName
     *@param newName
     */
    public boolean  updateReferencedField(String oldName, String newName) {
        boolean bUpdated;
        boolean bRet = false;
        ArrayList alSimpleSearchPages = mPageDefinition.eoSearch.alSimpleSearchPages;
        for (int i=0; i < alSimpleSearchPages.size(); i++) {
            PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
            ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
            for (int j=0; j<alFieldGroup.size(); j++) {
                PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                bUpdated = mPageDefinition.updateReferencedField(oldName, newName, fieldGroup.alFieldRef);
                if (bUpdated) {
                    bRet = true;
                }
            }
        }
        
        bUpdated = mPageDefinition.updateReferencedField(oldName, newName, mPageDefinition.eoSearch.searchResultListPage.alFieldRef);
        if (bUpdated) {
            bRet = true;
        }

        ArrayList alReports = mPageDefinition.reports.alReport;
        for (int j=0; j<alReports.size(); j++) {
            PageDefinition.Report report = (PageDefinition.Report) alReports.get(j);
            bUpdated = mPageDefinition.updateReferencedField(oldName, newName, report.alFieldRef);
            if (bUpdated) {
                bRet = true;
            }
        }
        return bRet;
    }

    /* 
     *Remove referenced field when it is deleted from OBject Definition
     *@param fieldNamePath
     */
    public boolean removeReferencedField(String fieldNamePath) {
        boolean bUpdated;
        boolean bRet = false;
        ArrayList alSimpleSearchPages = mPageDefinition.eoSearch.alSimpleSearchPages;
        for (int i=0; i < alSimpleSearchPages.size(); i++) {
            PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
            ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
            for (int j=0; j<alFieldGroup.size(); j++) {
                PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                bUpdated = mPageDefinition.removeReferencedField(fieldNamePath, fieldGroup.alFieldRef);
                if (bUpdated) {
                    bRet = true;
                }
            }
        }
        
        bUpdated = mPageDefinition.removeReferencedField(fieldNamePath, mPageDefinition.eoSearch.searchResultListPage.alFieldRef);
        if (bUpdated) {
            bRet = true;
        }
        ArrayList alReports = mPageDefinition.reports.alReport;
        for (int j=0; j<alReports.size(); j++) {
            PageDefinition.Report report = (PageDefinition.Report) alReports.get(j);
            bUpdated = mPageDefinition.removeReferencedField(fieldNamePath, report.alFieldRef);
            if (bUpdated) {
                bRet = true;
            }
        }
        return bRet;
    }
    
    /*
     *Remove simpleSearchPage that uses queryBuilderName being removed
     *@param queryBuilderName
     */
    public boolean removeReferencedQueryBuilder(String queryBuilderName) {
        boolean bRet = false;
        ArrayList alSimpleSearchPages = mPageDefinition.eoSearch.alSimpleSearchPages;
        for (int i=alSimpleSearchPages.size() - 1; i >= 0 ; i--) {
            boolean bUpdated = false;
            PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
            for (int k=simpleSearchPage.alSearchOption.size() - 1; k >= 0; k--) {
                PageDefinition.SearchOption searchOption = (PageDefinition.SearchOption) simpleSearchPage.alSearchOption.get(k);
                if (searchOption.queryBuilder.equals(queryBuilderName)) {
                    simpleSearchPage.alSearchOption.remove(k);
                    bUpdated = true;
                    break;
                }
            }
            if (bUpdated) {
                bRet = true;
                if (simpleSearchPage.alSearchOption == null || simpleSearchPage.alSearchOption.size() == 0) {
                    alSimpleSearchPages.remove(i);
                }
            }
        }
        if (bRet) {
            this.setModified(true);
        }
        return bRet;
    }
    
    /*
     * Update eosearch.simpleSearchPage, 
     * (eosearch. history. matchinReview.)searchResultListPage, 
     * reports
     *@param String fieldNamePath (Person.LastName, Address.AddressLine1)
     *@param String attributeName (searchScreen, searchResult, report)
     *@param boolean checked (true/false)
     *@param boolean required (true/false) only applicable for searchScreen
     */
    public boolean updateCheckedAttributes(String fieldNamePath, String attributeName, boolean checked, String required) {
        boolean bUpdated;
        boolean bRet = false;
        if (attributeName.equals(SEARCHSCREEN)) {
            ArrayList alSimpleSearchPages = mPageDefinition.eoSearch.alSimpleSearchPages;
            for (int i=0; i < alSimpleSearchPages.size(); i++) {
                PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
                ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
                for (int j=0; j<alFieldGroup.size(); j++) {
                    PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                    bUpdated = mPageDefinition.updateCheckedAttributes(fieldNamePath, 
                                                                       fieldGroup.alFieldRef, 
                                                                       checked, 
                                                                       required);
                    if (bUpdated) {
                        bRet = true;
                    }
                }
            }
        }
        
        if (attributeName.equals(SEARCHRESULT)) {
            bUpdated = mPageDefinition.updateCheckedAttributes(fieldNamePath, 
                                                               mPageDefinition.eoSearch.searchResultListPage.alFieldRef, 
                                                               checked,
                                                               required);
            if (bUpdated) {
                bRet = true;
            }
        }

        if (attributeName.equals(REPORT)) {        
            ArrayList alReports = mPageDefinition.reports.alReport;
            for (int j=0; j<alReports.size(); j++) {
                PageDefinition.Report report = (PageDefinition.Report) alReports.get(j);
                bUpdated = mPageDefinition.updateCheckedAttributes(fieldNamePath, 
                                                                   report.alFieldRef, 
                                                                   checked, 
                                                                   required);
                if (bUpdated) {
                    bRet = true;
                }
            }
        }
        return bRet;
    }
    
    class PageDefinition {
        String initialScreen = "EO Search";
        EOSearch eoSearch = new EOSearch();
        CreateEO createEO = new CreateEO();
        MatchReview matchReview = new MatchReview();
        History history = new History();
        Reports reports = new Reports();
        AuditLog auditLog = new AuditLog();
        
        String getInitialScreen() {
            return initialScreen;
        }
            
        FieldRef getFieldRef(ArrayList alFieldRef) {
            FieldRef fieldRef = new FieldRef();
            alFieldRef.add(fieldRef);
            return fieldRef;
        }
            
        boolean updateReferencedField(String oldName, String newName, ArrayList alFieldRef) {
            boolean bRet = false;
            for (int i=0; i < alFieldRef.size(); i++) {
                FieldRef fieldRef = (FieldRef) alFieldRef.get(i);
                if (fieldRef.fieldName.equals(oldName)) {
                    fieldRef.fieldName = newName;
                    bRet = true;
                } else {
                    int index = fieldRef.fieldName.indexOf(oldName);
                    if (index >= 0) {
                        String oldNameRegex = oldName.replaceAll("\\.", "\\\\."); 
                        String newValue = fieldRef.fieldName.replaceAll(oldNameRegex, newName);
                        fieldRef.fieldName = newValue;
                        bRet = true;
                    }
                }
            }
            return bRet;
        }
            
        boolean removeReferencedField(String fieldNamePath, ArrayList alFieldRef) {
            boolean bRet = false;
            for (int i=alFieldRef.size() - 1; i>=0 && i < alFieldRef.size(); i--) {            
                FieldRef fieldRef = (FieldRef) alFieldRef.get(i);
                if (fieldRef.fieldName.equals(fieldNamePath)) {
                    alFieldRef.remove(i);
                    bRet = true;
                }
            }
            return bRet;
        }
                
        boolean updateCheckedAttributes(String fieldNamePath, ArrayList alFieldRef, boolean checked, String required) {
            boolean bRet = false;
            boolean bFound = false;
            for (int i=alFieldRef.size() - 1; i>=0 && i < alFieldRef.size(); i--) {
                FieldRef fieldRef = (FieldRef) alFieldRef.get(i);
                if (fieldRef.fieldName.equals(fieldNamePath) && !checked) {
                    // remove it
                    alFieldRef.remove(i);
                    bRet = true;
                    break;
                }
            }
            if (checked && !bFound) {
                // Add it
                FieldRef fieldRef = getFieldRef(alFieldRef);
                fieldRef.fieldName = fieldNamePath;
                fieldRef.required = required;
                bRet = true;
            }
            return bRet;
        }

        class PageTab {
            String rootObject;
            String tabName;
            String tabEntrance;
        }
        
        class SearchOption {
            class Parameter {
                String name;
                String value = "";
            
                String getName() {
                    return name;
                }
                String getValue() {
                    return value;
                }
            }

            String displayName;
            String queryBuilder;
            String weighted; //"true"
            String candidateThreshold = "0"; //200
            ArrayList alParameter = new ArrayList();
            
            Parameter getParameter() {
                Parameter parameter = new Parameter();
                alParameter.add(parameter);
                return parameter;
            }
        }
        
        class FieldRef {
            String required = null;    // Optional for Report
            String fieldName;
        }
        
        class FieldGroup {
            String description;
            ArrayList alFieldRef = new ArrayList();            
        }
        
        class SimpleSearchPage {
            String screenTitle;
            String fieldPerRow = "2";
            String showEuid;
            String showLid;
            ArrayList alFieldGroup = new ArrayList();
            ArrayList alSearchOption = new ArrayList();
            
            FieldGroup getFieldGroup() {
                FieldGroup fieldGroup = new FieldGroup();
                alFieldGroup.add(fieldGroup);
                return fieldGroup;
            }
            
            SearchOption getSearchOption() {
                SearchOption searchOption = new SearchOption();
                alSearchOption.add(searchOption);
                return searchOption;
            }
        }
        
        class SearchResultListPage {
            String itemPerPage;
            String maxResultSize;
            ArrayList alFieldRef = new ArrayList(); // Optional
        }
        
        class EOViewPage {
            String fieldPerRow;
        }
        
        class PDSearchPage {
            String fieldPerRow;
        }
        
        class EOSearch {
            PageTab pageTab = new PageTab();;
            ArrayList alSimpleSearchPages = new ArrayList();
            SearchResultListPage searchResultListPage = new SearchResultListPage();
            EOViewPage eoViewPage = new EOViewPage();
            
            SimpleSearchPage getSimpleSearchPage() {
                SimpleSearchPage simpleSearchPage = new SimpleSearchPage();
                alSimpleSearchPages.add(simpleSearchPage);
                return simpleSearchPage;
            }
        }
        
        class CreateEO {
            PageTab pageTab = new PageTab();;
        }
        
        class XASearchPage {
            String fieldPerRow;
        }
        
        class History {
            PageTab pageTab = new PageTab();;
            XASearchPage xaSearchPage = new XASearchPage();
            SearchResultListPage searchResultListPage = new SearchResultListPage();
        }
        
        class MatchReview {
            PageTab pageTab = new PageTab();;
            PDSearchPage pdSearchPage = new PDSearchPage();
            SearchResultListPage searchResultListPage = new SearchResultListPage();
        }
        
        class Report {
            String name;
            String title;
            String enable;
            String maxResultSize;
            //fields
            ArrayList alFieldRef = new ArrayList(); // of FieldRef - Optional
            
            
        }
        
        class Reports {
            PageTab pageTab = new PageTab();;
            String searchPageFieldPerRow;
            ArrayList alReport = new ArrayList();
            
            Report getReport() {
                Report report = new Report();
                alReport.add(report);
                return report;
            }
        }
        
        class AuditLog {
            String allowInsert; // "true"
        }
    }
}
