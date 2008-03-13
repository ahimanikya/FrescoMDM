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
    private final String mTagEDM = "edm";
    private final String mTagMIDM = "midm";
    private final String mTagNodeDash = "node-";
    private final String mTagNode = "node";
    private final String mAttrDisplayOrder = "display-order";
    private final String mTagFieldDash = "field-";          // -> EDMFieldDef
    private final String mTagField = "field";               // -> EDMFieldDef
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
    private final String mTagObjectSensitivePlugInClass = "object-sensitive-plug-in-class";
    
    private final String mTagGuiDefinition = "gui-definition";
    private final String mTagSystemDisplayNameOverrides = "system-display-name-overrides";
    private final String mTagLocalIdHeader = "local-id-header";
    private final String mTagLocalId = "local-id";
    private final String mTagPageDefinition = "page-definition";
    private final String mTagInitialScreen = "initial-screen";      //Classic
    //MI only
    private final String mTagInitialScreenID = "initial-screen-id";
    private final String mTagRecordDetails = "record-details";
    private final String mTagTransactions = "transactions";
    private final String mTagDuplicateRecords = "duplicate-records";
    private final String mTagAssumedMatches = "assumed-matches";
    private final String mTagSourceRecord = "source-record";
    private final String mTagScreenID = "screen-id";
    private final String mTagSearchPages = "search-pages";
    private final String mTagSearchPagesBlank = "<search-pages/>\n";
    private final String mTagSubscreenConfigurations = "subscreen-configurations";
    private final String mTagSubscreen = "subscreen";
    private final String mTagSearchResultPages = "search-result-pages";
    private final String mTagSearchResultPagesBlank = "<search-result-pages/>\n";
    private final String mTagSearchResultID = "search-result-id";
    private final String mTagSearchScreenOrder = "search-screen-order";
    private final String mTagInstruction = "instruction";
    private final String mTagReportName = "report-name";
    // end of MI only
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
    private final String mTagQueryBuilder = "query-builder";
    private final String mTagWeighted = "weighted";
    private final String mTagCandidateThreshold = "candidate-threshold";
    private final String mTagParameter = "parameter";
    private final String mTagParameterName = "name";
    private final String mTagParameterValue = "value";

    private final String mTagFieldGroup = "field-group";
    private final String mTagFieldGroupBlank = "<field-group/>\n";    
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
    
    private final String mTagReports = "reports";   // Classic and MI
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
    
    public final String mTagShowStatus = "show-status";
    public final String mTagShowCreateDate = "show-create-date";
    public final String mTagShowCreateTime = "show-create-time";
    public final String mTagShowTimeStamp = "show-timestamp";

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
    private SystemDisplayNameOverrides mSystemDisplayNameOverrides = null;
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
        if (mPageDefinition.history.xaSearchPage.fieldPerRow != null) {
            buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                          mPageDefinition.history.xaSearchPage.fieldPerRow + 
                          Utils.endTag(mTagFieldPerRow));
        }
        buffer.append(Utils.TAB4 + Utils.endTag(mTagXASearchPage));

        return buffer.toString();

    }
    
    void parseXASearchPage(Node node) {
        mPageDefinition.history.xaSearchPage.fieldPerRow = parseFieldPerRow(node);
    }
    
    String getHistoryXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagHistory));
        buffer.append(getPageTabXML(mPageDefinition.history.pageTab, Utils.TAB4));
        buffer.append(getXASearchPageXML());
        buffer.append(getSearchResultListPageXML(mPageDefinition.history.searchResultListPage, Utils.TAB5));
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
        mPageDefinition.createHistory();
        if (node.hasChildNodes()) {
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
                        parseSearchResultListPage(nl.item(i), mPageDefinition.history.addSearchResultListPage());
                    }
                }
            }
        }
    }
    
    String getPDSearchPageXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB4 + Utils.startTag(mTagPDSearchPage));
        if (mPageDefinition.matchReview.pdSearchPage.fieldPerRow != null) {
            buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                          mPageDefinition.matchReview.pdSearchPage.fieldPerRow + 
                          Utils.endTag(mTagFieldPerRow));
        }
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
        buffer.append(getPageTabXML(mPageDefinition.matchReview.commonBlock.pageTab, Utils.TAB4));
        buffer.append(getPDSearchPageXML());
        buffer.append(getSearchResultListPageXML(mPageDefinition.matchReview.commonBlock.searchResultListPage, Utils.TAB5));
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
    void parseCommonBlock(Node node, PageDefinition.CommonBlock commonBlock) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    
                    if (name.equals(mTagRootObject)) {
                        commonBlock.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        commonBlock.pageTab.tabName = value;
                    } else if (name.equals(mTagAllowInsert)) {
                        commonBlock.allowInsert = value;
                    } else if (name.equals(mTagScreenID)) {
                        commonBlock.screenID = value;
                    } else if (name.equals(mTagDisplayOrder)) {
                        commonBlock.displayOrder = value;
                    } else if (name.equals(mTagSearchPages)) {
                        parseSearchPages(nl.item(i), commonBlock);
                    } else if (name.equals(this.mTagSearchResultPages)) {
                        parseSearchResultPages(nl.item(i), commonBlock);
                    }
                }
            }
        }
    }

    void parseMatchingReview(Node node) {
        mPageDefinition.createMatchReview();
        parseCommonBlock(node, mPageDefinition.matchReview.commonBlock);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagPDSearchPage)) {
                        parsePDSearchPage(nl.item(i));
                    } else if (name.equals(mTagSearchResultListPage)) {
                        parseSearchResultListPage(nl.item(i), mPageDefinition.matchReview.commonBlock.addSearchResultListPage());
                    }
                }
            }
        }
    }
    
    String getAuditLogXML(boolean bMidm) {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagAuditLog));
        buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagAllowInsert) +
                            mPageDefinition.auditLog.allowInsert + 
                            Utils.endTag(mTagAllowInsert));
        if (bMidm) {
            buffer.append(getPageTabXML(mPageDefinition.auditLog.pageTab, Utils.TAB4, mPageDefinition.reports.commonBlock.pageTab.rootObject, "Audit Log"));
        }
        if (mPageDefinition.auditLog.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.auditLog.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.auditLog.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.auditLog.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }
        buffer.append(getSearchPagesXML(mPageDefinition.auditLog.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.auditLog.searchResultListPage, Utils.TAB4));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagAuditLog));

        return buffer.toString();

    }

    void parseSearchPages(Node node, PageDefinition.CommonBlock commonBlock) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagSimpleSearchPage)) {
                        PageDefinition.SimpleSearchPage simpleSearchPage = commonBlock.addSimpleSearchPage();
                        if (simpleSearchPage != null) {
                            parseSimpleSearchPage(nl.item(i), simpleSearchPage);
                        }
                    }
                }
            }
        }

    }
    
    void parseSearchResultPages(Node node, PageDefinition.CommonBlock commonBlock) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();

                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagSearchResultListPage)) {
                        PageDefinition.SearchResultListPage searchResultListPage = commonBlock.addSearchResultListPage();
                        parseSearchResultListPage(nl.item(i), searchResultListPage);
                    }
                }
            }
        }
    }
    
    void parseAuditLog(Node node) {
        mPageDefinition.createAuditLog();
        parseCommonBlock(node, mPageDefinition.auditLog);
    }
    
    void parseSubscreen(Node node, PageDefinition.Subscreen subscreen) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagEnable)) {
                        subscreen.enable = value;
                    } else if (name.equals(mTagRootObject)) {
                        subscreen.commonBlock.pageTab.rootObject = value;
                    } else if (name.equals(mTagTabName)) {
                        subscreen.commonBlock.pageTab.tabName = value;
                    } else if (name.equals(mTagReportName)) { // report-name
                        subscreen.reportName = value;
                    } else if (name.equals(mTagScreenID)) {
                        subscreen.commonBlock.screenID = value;
                    } else if (name.equals(mTagDisplayOrder)) {
                        subscreen.commonBlock.displayOrder = value;
                    } else if (name.equals(mTagSearchPages)) {
                        parseSearchPages(nl.item(i), subscreen.commonBlock);
                    } else if (name.equals(this.mTagSearchResultPages)) {
                        parseSearchResultPages(nl.item(i), subscreen.commonBlock);
                    }
                }
            }
        }
    }
    
    void parseSubscreenConfigurations(Node node, PageDefinition.SubscreenConfigurations subscreenConfigurations) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    if (name.equals(mTagSubscreen)) {
                        parseSubscreen(nl.item(i), subscreenConfigurations.addSubscreen());
                    }
                }
            }
        }
    }

    String getSubscreenXML(PageDefinition.Subscreen subscreen) {
        if (subscreen == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB5 + Utils.startTag(mTagSubscreen));
        buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagEnable) +
                      subscreen.enable + 
                      Utils.endTag(mTagEnable));
        buffer.append(getPageTabXML(subscreen.commonBlock.pageTab, Utils.TAB6));
        if (subscreen.reportName != null) {
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagReportName) +
                          subscreen.reportName + 
                          Utils.endTag(mTagReportName));
        }
        if (subscreen.commonBlock.screenID != null) {
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagScreenID) +
                          subscreen.commonBlock.screenID + 
                          Utils.endTag(mTagScreenID));
        }
        if (subscreen.commonBlock.displayOrder != null) {
            buffer.append(Utils.TAB6 + Utils.startTagNoLine(mTagDisplayOrder) +
                          subscreen.commonBlock.displayOrder + 
                          Utils.endTag(mTagDisplayOrder));
        }
        
        buffer.append(getSearchPagesXML(subscreen.commonBlock.alSimpleSearchPages, Utils.TAB6));

        buffer.append(getSearchResultPagesXML(subscreen.commonBlock.searchResultListPage, Utils.TAB6));

        buffer.append(Utils.TAB5 + Utils.endTag(mTagSubscreen));
        return buffer.toString();
    }

    String getSubscreenConfigurationsXML(PageDefinition.SubscreenConfigurations subscreenConfigurations) {
        if (subscreenConfigurations == null || subscreenConfigurations.alSubscreens == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB4 + Utils.startTag(mTagSubscreenConfigurations));
        for (int i=0; i < subscreenConfigurations.alSubscreens.size(); i++) {
            PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) subscreenConfigurations.alSubscreens.get(i);
            buffer.append(getSubscreenXML(subscreen));
        }
        buffer.append(Utils.TAB4 + Utils.endTag(mTagSubscreenConfigurations));
        return buffer.toString();
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
            buffer.append(getFieldRefXML(fieldRef, Utils.TAB6));
        }
        buffer.append(Utils.TAB5 + Utils.endTag(mTagFields));
        
        buffer.append(Utils.TAB4 + Utils.endTag(mTagReport));

        return buffer.toString();

    }
    
    void parseReport(Node node) {
        PageDefinition.Report report = mPageDefinition.reports.addReport();
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
    String getReportsXML(boolean bMidm) {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagReports));
        buffer.append(getPageTabXML(mPageDefinition.reports.commonBlock.pageTab, Utils.TAB4));
        if (mPageDefinition.reports.searchPageFieldPerRow != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagSearchPageFieldPerRow) + 
                          mPageDefinition.reports.searchPageFieldPerRow + 
                          Utils.endTag(mTagSearchPageFieldPerRow));
        }
        if (mPageDefinition.reports.commonBlock.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.reports.commonBlock.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.reports.commonBlock.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.reports.commonBlock.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }
        
        if (bMidm) {
            mPageDefinition.midmConverter.reports();
        }
        
        if (mPageDefinition.reports.alReport != null) {
            for (int i=0; i<mPageDefinition.reports.alReport.size(); i++) {
                PageDefinition.Report report = (PageDefinition.Report) mPageDefinition.reports.alReport.get(i);
                buffer.append(getReportXML(report));
            }
        }

        buffer.append(getSearchPagesXML(mPageDefinition.reports.commonBlock.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.reports.commonBlock.searchResultListPage, Utils.TAB4));

        buffer.append(getSubscreenConfigurationsXML((mPageDefinition.reports.subscreenConfigurations)));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagReports));

        return buffer.toString();

    }
    
    void parseReports(Node node) {
        mPageDefinition.createReports();
        parseCommonBlock(node, mPageDefinition.reports.commonBlock);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagSearchPageFieldPerRow)) {
                        mPageDefinition.reports.searchPageFieldPerRow = value;
                    } else if (name.equals(mTagReport)) {
                        parseReport(nl.item(i));
                    } else if (name.equals(mTagSubscreenConfigurations)) {
                        parseSubscreenConfigurations(nl.item(i), mPageDefinition.reports.subscreenConfigurations);
                    }
                }
            }
        }
    }
    
    void parseRecordDetails(Node node) {
        mPageDefinition.createRecordDetails();
        parseCommonBlock(node, mPageDefinition.recordDetails);
    }
    
    String getRecordDetailsXML() {
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB3 + Utils.startTag(mTagRecordDetails));
        buffer.append(getPageTabXML(mPageDefinition.recordDetails.pageTab, Utils.TAB4));
        if (mPageDefinition.recordDetails.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.recordDetails.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.recordDetails.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.recordDetails.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }

        buffer.append(getSearchPagesXML(mPageDefinition.recordDetails.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.recordDetails.searchResultListPage, Utils.TAB4));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagRecordDetails));

        return buffer.toString();
    }
    
    void parseTransactions(Node node) {
        mPageDefinition.createTransactions();
        parseCommonBlock(node, mPageDefinition.transactions);
    }
    
    String getTransactionsXML() {
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB3 + Utils.startTag(mTagTransactions));
        buffer.append(getPageTabXML(mPageDefinition.transactions.pageTab, Utils.TAB4));
        if (mPageDefinition.transactions.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.transactions.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.transactions.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.transactions.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }

        buffer.append(getSearchPagesXML(mPageDefinition.transactions.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.transactions.searchResultListPage, Utils.TAB4));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagTransactions));

        return buffer.toString();
    }
    
    void parseDuplicateRecords(Node node) {
        mPageDefinition.createDuplicateRecords();
        parseCommonBlock(node, mPageDefinition.duplicateRecords);
    }
    
    String getDuplicateRecordsXML() {
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB3 + Utils.startTag(mTagDuplicateRecords));
        buffer.append(getPageTabXML(mPageDefinition.duplicateRecords.pageTab, Utils.TAB4));
        if (mPageDefinition.duplicateRecords.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.duplicateRecords.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.duplicateRecords.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.duplicateRecords.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }

        buffer.append(getSearchPagesXML(mPageDefinition.duplicateRecords.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.duplicateRecords.searchResultListPage, Utils.TAB4));

        buffer.append(Utils.TAB3 + Utils.endTag(mTagDuplicateRecords));

        return buffer.toString();
    }
    
    void parseAssumedMatches(Node node) {
        mPageDefinition.createAssumedMatches();
        parseCommonBlock(node, mPageDefinition.assumedMatches);
    }
    
    String getAssumedMatchesXML() {
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB3 + Utils.startTag(mTagAssumedMatches));
        buffer.append(getPageTabXML(mPageDefinition.assumedMatches.pageTab, Utils.TAB4));
        if (mPageDefinition.assumedMatches.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.assumedMatches.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.assumedMatches.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.assumedMatches.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }

        buffer.append(getSearchPagesXML(mPageDefinition.assumedMatches.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.assumedMatches.searchResultListPage, Utils.TAB4));
        
        buffer.append(Utils.TAB3 + Utils.endTag(mTagAssumedMatches));

        return buffer.toString();
    }
    
    void parseSourceRecord(Node node) {
        mPageDefinition.createSourceRecord();
        parseCommonBlock(node, mPageDefinition.sourceRecord.commonBlock);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    if (name.equals(mTagSubscreenConfigurations)) {
                        parseSubscreenConfigurations(nl.item(i), mPageDefinition.sourceRecord.subscreenConfigurations);
                    }
                }
            }
        }

    }
    
    String getSourceRecordXML() {
        StringBuffer buffer = new StringBuffer();
	buffer.append(Utils.TAB3 + Utils.startTag(mTagSourceRecord));
        buffer.append(getPageTabXML(mPageDefinition.sourceRecord.commonBlock.pageTab, Utils.TAB4));
        if (mPageDefinition.sourceRecord.commonBlock.screenID != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagScreenID) +
                            mPageDefinition.sourceRecord.commonBlock.screenID + 
                            Utils.endTag(mTagScreenID));
        }
        if (mPageDefinition.sourceRecord.commonBlock.displayOrder != null) {
            buffer.append(Utils.TAB4 + Utils.startTagNoLine(mTagDisplayOrder) +
                            mPageDefinition.sourceRecord.commonBlock.displayOrder + 
                            Utils.endTag(mTagDisplayOrder));
        }

        buffer.append(getSearchPagesXML(mPageDefinition.sourceRecord.commonBlock.alSimpleSearchPages, Utils.TAB4));

        buffer.append(getSearchResultPagesXML(mPageDefinition.sourceRecord.commonBlock.searchResultListPage, Utils.TAB4));

        buffer.append(getSubscreenConfigurationsXML((mPageDefinition.sourceRecord.subscreenConfigurations)));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagSourceRecord));

        return buffer.toString();
    }
    
    String getCreateEoXML() {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagCreateEo));
        buffer.append(getPageTabXML(mPageDefinition.createEO.pageTab, Utils.TAB4));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagCreateEo));

        return buffer.toString();

    }

    void parseCreateEo(Node node) {
        mPageDefinition.createCreateEO();
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
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
        if (eoViewPage.fieldPerRow != null) {
            buffer.append(Utils.TAB5 + Utils.startTagNoLine(mTagFieldPerRow) + 
                          eoViewPage.fieldPerRow + 
                          Utils.endTag(mTagFieldPerRow));
        }
        buffer.append(Utils.TAB4 + Utils.endTag(mTagEoViewPage));
        return buffer.toString();
    }
    
    void parseEoViewPage(Node node) {
        mPageDefinition.eoSearch.eoViewPage.fieldPerRow = parseFieldPerRow(node);
    }

    String getSearchResultPagesXML(PageDefinition.SearchResultListPage searchResultListPage, String startTab) {
        if (searchResultListPage == null) {
            return startTab + mTagSearchResultPagesBlank;
        }
        StringBuffer buffer = new StringBuffer();
	buffer.append(startTab + Utils.startTag(mTagSearchResultPages));
        buffer.append(getSearchResultListPageXML(searchResultListPage, startTab + Utils.TAB));
        buffer.append(startTab + Utils.endTag(mTagSearchResultPages));
        return buffer.toString();
    }
    
    String getSearchResultListPageXML(PageDefinition.SearchResultListPage searchResultListPage, String startTab) {
        if (searchResultListPage == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(startTab + Utils.startTag(mTagSearchResultListPage));
        if (searchResultListPage.searchResultID != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagSearchResultID) + 
                          searchResultListPage.searchResultID + 
                          Utils.endTag(mTagSearchResultID));
        }
        if (searchResultListPage.itemPerPage != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagItemPerPage) + 
                          searchResultListPage.itemPerPage + 
                          Utils.endTag(mTagItemPerPage));
        }
        if (searchResultListPage.maxResultSize != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagMaxResultSize) + 
                          searchResultListPage.maxResultSize + 
                          Utils.endTag(mTagMaxResultSize));
        }
        if (searchResultListPage.showEuid != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowEuid) + 
                          searchResultListPage.showEuid + 
                          Utils.endTag(mTagShowEuid));
        }
        if (searchResultListPage.showLid != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowLid) + 
                          searchResultListPage.showLid + 
                          Utils.endTag(mTagShowLid));
        }
        if (searchResultListPage.showStatus != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowStatus) + 
                          searchResultListPage.showStatus + 
                          Utils.endTag(mTagShowStatus));
        }
        if (searchResultListPage.showCreateDate != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowCreateDate) + 
                          searchResultListPage.showCreateDate + 
                          Utils.endTag(mTagShowCreateDate));
        }
        if (searchResultListPage.showCreateTime != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowCreateTime) + 
                          searchResultListPage.showCreateTime + 
                          Utils.endTag(mTagShowCreateTime));
        }
        if (searchResultListPage.showTimeStamp != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowTimeStamp) + 
                          searchResultListPage.showTimeStamp + 
                          Utils.endTag(mTagShowTimeStamp));
        }

        if (searchResultListPage.alFieldGroup != null) {
            buffer.append(getSearchFieldGroupXML(searchResultListPage.alFieldGroup, startTab + Utils.TAB));
        } else {    // For classic eView
            for (int j=0; j < searchResultListPage.alFieldRef.size(); j++) {
                PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) searchResultListPage.alFieldRef.get(j);
                buffer.append(getFieldRefXML(fieldRef, Utils.TAB6));
            }
        }
        buffer.append(startTab + Utils.endTag(mTagSearchResultListPage));
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
                    } else if (tag.equals(mTagFieldGroup)) {
                        PageDefinition.FieldGroup fieldGroup = searchResultListPage.addFieldGroup();
                        parseSearchFieldGroup(nl.item(i), fieldGroup);
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
                    } else if (tag.equals(mTagSearchResultID)) {
                        searchResultListPage.searchResultID = value;
                    } else if (tag.equals(mTagShowEuid)) {
                        searchResultListPage.showEuid = value;
                    } else if (tag.equals(mTagShowLid)) {
                        searchResultListPage.showLid = value;
                    } else if (tag.equals(mTagShowStatus)) {
                        searchResultListPage.showStatus = value;
                    } else if (tag.equals(mTagShowCreateDate)) {
                        searchResultListPage.showCreateDate = value;
                    } else if (tag.equals(mTagShowCreateTime)) {
                        searchResultListPage.showCreateTime = value;
                    } else if (tag.equals(mTagShowTimeStamp)) {
                        searchResultListPage.showTimeStamp = value;
                    }
                }
            }
        }
    }
    
    String getSearchOptionXML(ArrayList alSearchOption) {
        if (alSearchOption == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < alSearchOption.size(); i++) {
            PageDefinition.SearchOption searchOption = (PageDefinition.SearchOption) alSearchOption.get(i);
            buffer.append(Utils.TAB6 + Utils.startTag(mTagSearchOption));
            buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagDisplayName) + 
                            searchOption.displayName + 
                            Utils.endTag(mTagDisplayName));
            buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagQueryBuilder) + 
                            searchOption.queryBuilder + 
                            Utils.endTag(mTagQueryBuilder));
            buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagWeighted) + 
                            searchOption.weighted + 
                            Utils.endTag(mTagWeighted));
            if (searchOption.candidateThreshold != null) {
                buffer.append(Utils.TAB7 + Utils.startTagNoLine(mTagCandidateThreshold) + 
                              searchOption.candidateThreshold + 
                              Utils.endTag(mTagCandidateThreshold));
            }
            
            for (int j=0; j < searchOption.alParameter.size(); j++) {
                PageDefinition.SearchOption.Parameter parameter = (PageDefinition.SearchOption.Parameter) searchOption.alParameter.get(j);
                buffer.append(Utils.TAB7 + Utils.startTag(mTagParameter));
                buffer.append(Utils.TAB8 + Utils.startTagNoLine(mTagParameterName) + 
                                parameter.name + 
                                Utils.endTag(mTagParameterName));
                buffer.append(Utils.TAB8 + Utils.startTagNoLine(mTagParameterValue) + 
                                parameter.value + 
                                Utils.endTag(mTagParameterValue));
                buffer.append(Utils.TAB7 + Utils.endTag(mTagParameter));
            }
            buffer.append(Utils.TAB6 + Utils.endTag(mTagSearchOption));
        }

        return buffer.toString();
    }
    
    void parseSearchOption(Node node, PageDefinition.SimpleSearchPage simpleSearchPage) {
        if (node.hasChildNodes()) {
            PageDefinition.SearchOption searchOption = simpleSearchPage.addSearchOption();
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
    
    String getFieldRefXML(PageDefinition.FieldRef fieldRef, String startTab) {
        StringBuffer buffer = new StringBuffer();
        if (fieldRef.required != null && fieldRef.required.equals("true")) {
            buffer.append(startTab + "<field-ref required=\"" + fieldRef.required + "\">" +
                            fieldRef.fieldName + 
                            Utils.endTag(mTagFieldRef));
        } else {
            buffer.append(startTab + Utils.startTagNoLine(mTagFieldRef) +
                            fieldRef.fieldName + 
                            Utils.endTag(mTagFieldRef));

        }

        return buffer.toString();
    }
    
    String getSearchFieldGroupXML(ArrayList alFieldGroup, String startTab) {
        if (alFieldGroup == null || alFieldGroup.size() == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < alFieldGroup.size(); i++) {
            PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(i);
            if ((fieldGroup.alFieldRef == null || fieldGroup.alFieldRef.size() <= 0) && fieldGroup.description == null) {
                buffer.append(startTab + mTagFieldGroupBlank);
            } else {
                buffer.append(startTab + Utils.startTag(mTagFieldGroup));
                if (fieldGroup.description != null) {
                    buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagDescription) + 
                                  fieldGroup.description + 
                                  Utils.endTag(mTagDescription));
                }
                for (int j=0; j < fieldGroup.alFieldRef.size(); j++) {
                    PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) fieldGroup.alFieldRef.get(j);
                    buffer.append(getFieldRefXML(fieldRef, startTab + Utils.TAB));
                }
                buffer.append(startTab + Utils.endTag(mTagFieldGroup));
            }
        }

        return buffer.toString();
    }
    
    void parseSearchFieldGroup(Node node, PageDefinition.FieldGroup fieldGroup) {
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
    
    String getSearchPagesXML(ArrayList alSimpleSearchPages, String startTab) {
        if (alSimpleSearchPages == null || alSimpleSearchPages.size() <= 0) {
            return startTab + mTagSearchPagesBlank;
        }
        StringBuffer buffer = new StringBuffer();
	buffer.append(startTab + Utils.startTag(mTagSearchPages));
        buffer.append(getAlSimpleSearchPageXML(alSimpleSearchPages, startTab + Utils.TAB));
        buffer.append(startTab + Utils.endTag(mTagSearchPages));

        return buffer.toString();
    }
    
    String getAlSimpleSearchPageXML(ArrayList alSimpleSearchPages, String startTab) {
        if (alSimpleSearchPages == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i < alSimpleSearchPages.size(); i++) {
            PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
            buffer.append(getSimpleSearchPageXML(simpleSearchPage, startTab));
        }
        return buffer.toString();
    }
    
    String getSimpleSearchPageXML(PageDefinition.SimpleSearchPage simpleSearchPage, String startTab) {
        if (simpleSearchPage == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        //mTagSimpleSearchPage
        buffer.append(startTab + Utils.startTag(mTagSimpleSearchPage));
        if (simpleSearchPage.screenTitle != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagScreenTitle) + 
                          simpleSearchPage.screenTitle + 
                          Utils.endTag(mTagScreenTitle));
        }
        if (simpleSearchPage.reportName != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagReportName) + 
                          simpleSearchPage.reportName + 
                          Utils.endTag(mTagReportName));
        }
        if (simpleSearchPage.fieldPerRow != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagFieldPerRow) + 
                          simpleSearchPage.fieldPerRow + 
                          Utils.endTag(mTagFieldPerRow));
        }
        if (simpleSearchPage.searchResultID != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagSearchResultID) + 
                          simpleSearchPage.searchResultID + 
                          Utils.endTag(mTagSearchResultID));
        }
        if (simpleSearchPage.searchScreenOrder != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagSearchScreenOrder) + 
                          simpleSearchPage.searchScreenOrder + 
                          Utils.endTag(mTagSearchScreenOrder));
        }
        if (simpleSearchPage.showEuid != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowEuid) + 
                          simpleSearchPage.showEuid + 
                          Utils.endTag(mTagShowEuid));
        }
        if (simpleSearchPage.showLid != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowLid) + 
                          simpleSearchPage.showLid + 
                          Utils.endTag(mTagShowLid));
        }
        if (simpleSearchPage.showStatus != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowStatus) + 
                          simpleSearchPage.showStatus + 
                          Utils.endTag(mTagShowStatus));
        }
        if (simpleSearchPage.showCreateDate != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowCreateDate) + 
                          simpleSearchPage.showCreateDate + 
                          Utils.endTag(mTagShowCreateDate));
        }
        if (simpleSearchPage.showCreateTime != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagShowCreateTime) + 
                          simpleSearchPage.showCreateTime + 
                          Utils.endTag(mTagShowCreateTime));
        }
        if (simpleSearchPage.instruction != null) {
            buffer.append(startTab + Utils.TAB + Utils.startTagNoLine(mTagInstruction) + 
                          simpleSearchPage.instruction + 
                          Utils.endTag(mTagInstruction));
        }
        buffer.append(getSearchFieldGroupXML(simpleSearchPage.alFieldGroup, startTab + Utils.TAB));
        buffer.append(getSearchOptionXML(simpleSearchPage.alSearchOption));
        buffer.append(startTab + Utils.endTag(mTagSimpleSearchPage));
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
                    } else if (name.equals(mTagReportName)) {
                        simpleSearchPage.reportName = value;
                    } else if (name.equals(mTagFieldPerRow)) {
                        simpleSearchPage.fieldPerRow = value;
                    } else if (name.equals(mTagShowEuid)) {
                        simpleSearchPage.showEuid = value;
                    } else if (name.equals(mTagShowLid)) {
                        simpleSearchPage.showLid = value;
                    } else if (name.equals(mTagFieldGroup)) {
                        PageDefinition.FieldGroup fieldGroup = simpleSearchPage.addFieldGroup();
                        parseSearchFieldGroup(nl.item(i), fieldGroup);
                    } else if (name.equals(mTagSearchOption)) {
                        parseSearchOption(nl.item(i), simpleSearchPage);
                    } else if (name.equals(mTagSearchResultID)) {
                        simpleSearchPage.searchResultID = value;
                    } else if (name.equals(mTagSearchResultID)) {
                        simpleSearchPage.searchResultID = value;
                    } else if (name.equals(mTagSearchScreenOrder)) {
                        simpleSearchPage.searchScreenOrder = value;
                    } else if (name.equals(mTagInstruction)) {
                        simpleSearchPage.instruction = value;
                    } else if (name.equals(mTagShowStatus)) {
                        simpleSearchPage.showStatus = value;
                    } else if (name.equals(mTagShowCreateDate)) {
                        simpleSearchPage.showCreateDate = value;
                    } else if (name.equals(mTagShowCreateTime)) {
                        simpleSearchPage.showCreateTime = value;
                    }
                }
            }
        }
    }
    
    // PageTab
    String getPageTabXML(PageDefinition.PageTab pageTab, String startTab) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(startTab + Utils.startTagNoLine(mTagRootObject) + 
                        ((pageTab.rootObject != null && !pageTab.rootObject.equals("null")) ? pageTab.rootObject : "")  + 
                        Utils.endTag(mTagRootObject));
        buffer.append(startTab + Utils.startTagNoLine(mTagTabName) + 
                        ((pageTab.tabName != null && !pageTab.tabName.equals("null")) ? pageTab.tabName : "")  + 
                        Utils.endTag(mTagTabName));
        if (pageTab.tabEntrance != null) {
            buffer.append(startTab + Utils.startTagNoLine(mTagTabEntrance) + 
                            pageTab.tabEntrance + 
                            Utils.endTag(mTagTabEntrance));
        }
        return buffer.toString();
    }
    
    // PageTab
    String getPageTabXML(PageDefinition.PageTab pageTab, String startTab, String rootObject, String tabName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(startTab + Utils.startTagNoLine(mTagRootObject) + 
                        ((pageTab.rootObject != null && !pageTab.rootObject.equals("null") && pageTab.rootObject.length() > 0) ? pageTab.rootObject : rootObject)  + 
                        Utils.endTag(mTagRootObject));
        buffer.append(startTab + Utils.startTagNoLine(mTagTabName) + 
                        ((pageTab.tabName != null && !pageTab.tabName.equals("null") && pageTab.tabName.length() > 0) ? pageTab.tabName : tabName)  + 
                        Utils.endTag(mTagTabName));
        if (pageTab.tabEntrance != null) {
            buffer.append(startTab + Utils.startTagNoLine(mTagTabEntrance) + 
                            pageTab.tabEntrance + 
                            Utils.endTag(mTagTabEntrance));
        }
        return buffer.toString();
    }
    
    //mTagCreateEo
    String getEoSearchXML() {    //mTagEoSearch
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB3 + Utils.startTag(mTagEoSearch));
        buffer.append(getPageTabXML(mPageDefinition.eoSearch.commonBlock.pageTab, Utils.TAB4));
        buffer.append(getAlSimpleSearchPageXML(mPageDefinition.eoSearch.commonBlock.alSimpleSearchPages, Utils.TAB5));
        buffer.append(getSearchResultListPageXML(mPageDefinition.eoSearch.commonBlock.searchResultListPage, Utils.TAB5));
        buffer.append(getEoViewPageXML(mPageDefinition.eoSearch.eoViewPage));
        buffer.append(Utils.TAB3 + Utils.endTag(mTagEoSearch));

        return buffer.toString();

    }
    
    void parseEoSearch(Node node) {
        mPageDefinition.createEOSearch();
        parseCommonBlock(node, mPageDefinition.eoSearch.commonBlock);
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (name.equals(mTagSimpleSearchPage)) {
                        PageDefinition.SimpleSearchPage simpleSearchPage = mPageDefinition.eoSearch.commonBlock.addSimpleSearchPage();
                        parseSimpleSearchPage(nl.item(i), simpleSearchPage);
                    } else if (name.equals(mTagSearchResultListPage)) {
                        parseSearchResultListPage(nl.item(i), mPageDefinition.eoSearch.commonBlock.addSearchResultListPage());
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
                    if (name.equals(mTagInitialScreenID)) {
                        mPageDefinition.initialScreenID = Utils.getStrElementValue(nl.item(i));
                    } else if (name.equals(mTagInitialScreen)) {
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
                    } else if (name.equals(mTagRecordDetails)) {
                        parseRecordDetails(nl.item(i));
                    } else if (name.equals(mTagTransactions)) {
                        parseTransactions(nl.item(i));
                    } else if (name.equals(mTagDuplicateRecords)) {
                        parseDuplicateRecords(nl.item(i));
                    } else if (name.equals(mTagAssumedMatches)) {
                        parseAssumedMatches(nl.item(i));
                    } else if (name.equals(mTagSourceRecord)) {
                        parseSourceRecord(nl.item(i));
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
    
    /* getPageDefinitionXML
     * 
     * @param boolean bMidm - Convert edm to midm
     * @return String - page-definition portion of edm.xml or midm.xml
     * 
     */
    String getPageDefinitionXML(boolean bMidm) {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB2 + Utils.startTag(mTagPageDefinition));
        if (bMidm && mPageDefinition.initialScreenID == null) {
            mPageDefinition.initialScreenID = "1";
        }
        
        if (mPageDefinition.initialScreenID != null) {
            // midm:mPageDefinition.initialScreenID
            buffer.append(Utils.TAB3 + Utils.startTagNoLine(mTagInitialScreenID) + 
                            mPageDefinition.initialScreenID + 
                            Utils.endTag(mTagInitialScreenID));

        }
        // edm:mPageDefinition.initialScreen
        if (!bMidm && mPageDefinition.initialScreen != null) {
            buffer.append(Utils.TAB3 + Utils.startTagNoLine(mTagInitialScreen) + 
                            mPageDefinition.initialScreen + 
                            Utils.endTag(mTagInitialScreen));
        }

        if (mPageDefinition.eoSearch != null ||
            mPageDefinition.createEO != null ||
            mPageDefinition.history != null ||
            mPageDefinition.matchReview != null ||
            (mPageDefinition.reports != null && mPageDefinition.reports.alReport != null)) {
            // Inform user the conversion is in process
        }
        
        // Classic EDM
        if (mPageDefinition.eoSearch != null) {
            if (bMidm) {
                mPageDefinition.midmConverter.eoSearch();
            } else {
                buffer.append(getEoSearchXML());
            }
        }
        
        if (mPageDefinition.createEO != null) {
            if (bMidm) {
                mPageDefinition.midmConverter.createEO();
            } else {
                buffer.append(getCreateEoXML());
            }
        }
        
        if (mPageDefinition.history != null) {
            if (bMidm) {
                mPageDefinition.midmConverter.history();
            } else {
                buffer.append(getHistoryXML());
            }
        }
        
        if (mPageDefinition.matchReview != null) {
            if (bMidm) {
                mPageDefinition.midmConverter.matchReview();
            } else {
                buffer.append(getMatchingReviewXML());
            }
        }
        
        // Master Index DM
        if (mPageDefinition.recordDetails != null) {
            buffer.append(getRecordDetailsXML());
        }
        
        if (mPageDefinition.transactions != null) {
            buffer.append(getTransactionsXML());
        }
        
        if (mPageDefinition.duplicateRecords != null) {
            buffer.append(getDuplicateRecordsXML());
        }
        
        if (mPageDefinition.assumedMatches != null) {
            buffer.append(getAssumedMatchesXML());
        }
        
        if (mPageDefinition.sourceRecord != null) {
            buffer.append(getSourceRecordXML());
        }
        
        // Classic EDM and Master Index DM
        if (mPageDefinition.reports != null) {
            buffer.append(getReportsXML(bMidm));
        }
        
        if (mPageDefinition.auditLog != null) {
            buffer.append(getAuditLogXML(bMidm));
        }

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
    
    /* getGuiDefinitionXML - called by com.sun.mdm.index.project.ui.applicationeditor.writers.EDMWriter
     * 
     * @param boolean bMidm - Convert edm to midm
     * @return String - gui-definition portion of edm.xml or midm.xml
     * 
     */
    public String getGuiDefinitionXML(boolean bMidm) {
        StringBuffer buffer = new StringBuffer();
        
	buffer.append(Utils.TAB + Utils.startTag(mTagGuiDefinition));
        if (!bMidm && mSystemDisplayNameOverrides != null) {
            buffer.append(getSystemDisplayNameOverridesXML());
        }
        buffer.append(getPageDefinitionXML(bMidm));
        buffer.append(Utils.TAB + Utils.endTag(mTagGuiDefinition));

        return buffer.toString();
    }
    
    /* getImplDetailsXML - called by com.sun.mdm.index.project.ui.applicationeditor.writers.EDMWriter
     * 
     * @return String - impl-details portion of edm.xml or midm.xml
     * 
     */
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
        
        if (mImplDetails.objectSensitivePlugInClass != null) {
            buffer.append(Utils.TAB2 + Utils.startTagNoLine(this.mTagObjectSensitivePlugInClass) + 
                            mImplDetails.objectSensitivePlugInClass + 
                            Utils.endTag(mTagObjectSensitivePlugInClass));
        }
        
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
        String objectSensitivePlugInClass = null;
        
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
                    } else if (name.equals(mTagObjectSensitivePlugInClass)) {
                        mImplDetails.objectSensitivePlugInClass = value;
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
    
    private EDMFieldDef parseMIDMFieldNode(Node node) {
        EDMFieldDef edmFieldDef = new EDMFieldDef();
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (mTagName.equals(tag)) {
                        edmFieldDef.setFieldName(value);
                    } else if (mTagDisplayName.equals(tag)) {
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
                    if (fieldName.startsWith(mTagFieldDash)) {
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
    
    private void parseMIDMNode(Node node) {
        if (mAlEDMNodes == null) {
            mAlEDMNodes = new ArrayList();
        }
        EDMNode edmNode = new EDMNode();

        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String tag = ((Element) nl.item(i)).getTagName();
                    String value = Utils.getStrElementValue(nl.item(i));
                    if (tag.equals(mTagName)) {
                        edmNode.setNodeName(value);
                    } else if (tag.equals(mAttrDisplayOrder)) {
                        edmNode.setDisplayOrder(value);
                    } else if (tag.equals(mTagField)) {
                        EDMFieldDef edmFieldDef = parseMIDMFieldNode(nl.item(i));
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
                     && (((Element) element).getTagName().equals(mTagEDM) || ((Element) element).getTagName().equals(mTagMIDM) )
                     && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (name.startsWith(mTagNodeDash)) {
                            parseEDMNode(nl1.item(i1));
                        } else if (name.equals(mTagNode)) {
                            parseMIDMNode(nl1.item(i1));
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
        
        ArrayList getFields() {
            return fields;
        }
        
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

    private boolean updateReferencedFieldInCommonBlock(PageDefinition.CommonBlock commonBlock, String oldName, String newName) {
        boolean bUpdated = false;
        boolean bRet = false;
        if (commonBlock != null) {
            ArrayList alSimpleSearchPages = commonBlock.alSimpleSearchPages;
            if (alSimpleSearchPages != null) {
                for (int i=0; alSimpleSearchPages != null && i < alSimpleSearchPages.size(); i++) {
                    PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
                    ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
                    for (int j=0; alFieldGroup!= null && j<alFieldGroup.size(); j++) {
                        PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                        bUpdated = mPageDefinition.updateReferencedField(oldName, newName, fieldGroup.alFieldRef);
                        if (bUpdated) {
                            bRet = true;
                        }
                    }
                }
            }
        
            if (commonBlock.searchResultListPage != null) {
                bUpdated = mPageDefinition.updateReferencedField(oldName, newName, commonBlock.searchResultListPage.alFieldRef);
                ArrayList alFieldGroup = commonBlock.searchResultListPage.alFieldGroup;
                for (int j=0; alFieldGroup!= null && j<alFieldGroup.size(); j++) {
                    PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                    bUpdated = mPageDefinition.updateReferencedField(oldName, newName, fieldGroup.alFieldRef);
                    if (bUpdated) {
                        bRet = true;
                    }
                }
            }
            if (bUpdated) {
                bRet = true;
            }
        }
        return bRet;
    }
    
    /* 
     *Update referenced field when it is renamed in OBject Definition
     *@param oldName
     *@param newName
     */
    public boolean updateReferencedField(String oldName, String newName) {
        boolean bUpdated;
        boolean bRet = false;
        
        if (mAlEDMNodes != null && mAlEDMNodes.size() > 0) {
            for (int i = 0; i < mAlEDMNodes.size(); i++) {
                EDMNode node = (EDMNode) mAlEDMNodes.get(i);
                ArrayList alFields = node.getFields();
                for (int j = 0; j < alFields.size(); j++) {
                    EDMFieldDef edmFieldDef = (EDMFieldDef) alFields.get(j);
                    if (edmFieldDef.getFieldName().equals(oldName)) {
                        edmFieldDef.setFieldName(newName);
                        bRet = true;
                        break;
                    }
                }
                if (bRet == true) {
                    break;
                }
            }
        }

        if (mPageDefinition.eoSearch != null) {
            bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.eoSearch.commonBlock, oldName, newName);
        }
        if (mPageDefinition.sourceRecord != null) {
            bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.sourceRecord.commonBlock, oldName, newName);            
            if (mPageDefinition.sourceRecord.subscreenConfigurations != null && mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens!= null) {
                for (int i=0; i < mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.size(); i++) {
                    PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.get(i);
                    bRet = bRet | updateReferencedFieldInCommonBlock(subscreen.commonBlock, oldName, newName);
                }
            }
        }
        if (mPageDefinition.matchReview != null) {
            bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.matchReview.commonBlock, oldName, newName);
        }
        bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.recordDetails, oldName, newName);
        bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.transactions, oldName, newName);
        bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.duplicateRecords, oldName, newName);
        bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.assumedMatches, oldName, newName);
        
        if (mPageDefinition.reports != null) {
            bRet = bRet | updateReferencedFieldInCommonBlock(mPageDefinition.reports.commonBlock, oldName, newName);
            if (mPageDefinition.reports.subscreenConfigurations != null && mPageDefinition.reports.subscreenConfigurations.alSubscreens!= null) {
                for (int i=0; i < mPageDefinition.reports.subscreenConfigurations.alSubscreens.size(); i++) {
                    PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.reports.subscreenConfigurations.alSubscreens.get(i);
                    bRet = bRet | updateReferencedFieldInCommonBlock(subscreen.commonBlock, oldName, newName);
                }
            }

            ArrayList alReports = mPageDefinition.reports.alReport;
            for (int j=0; j<alReports.size(); j++) {
                PageDefinition.Report report = (PageDefinition.Report) alReports.get(j);
                bUpdated = mPageDefinition.updateReferencedField(oldName, newName, report.alFieldRef);
                if (bUpdated) {
                    bRet = true;
                }
            }
        }
        return bRet;
    }

    private boolean removeReferencedFieldInCommonBlock(PageDefinition.CommonBlock commonBlock,  String fieldNamePath) {
        boolean bRet = false;
        if (commonBlock != null) {
            ArrayList alSimpleSearchPages = commonBlock.alSimpleSearchPages;
            for (int i=0; alSimpleSearchPages != null && i < alSimpleSearchPages.size(); i++) {
                PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
                ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
                for (int j=0; alFieldGroup != null && j<alFieldGroup.size(); j++) {
                    PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                    bRet = bRet | mPageDefinition.removeReferencedField(fieldNamePath, fieldGroup.alFieldRef);
                }
            }
        
            if (commonBlock.searchResultListPage != null) {
                bRet = bRet | mPageDefinition.removeReferencedField(fieldNamePath, commonBlock.searchResultListPage.alFieldRef);
                ArrayList alFieldGroup = commonBlock.searchResultListPage.alFieldGroup;
                for (int j=0; alFieldGroup!= null && j<alFieldGroup.size(); j++) {
                    PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                    bRet = bRet | mPageDefinition.removeReferencedField(fieldNamePath, fieldGroup.alFieldRef);
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

        if (mPageDefinition.eoSearch != null) {
            bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.eoSearch.commonBlock, fieldNamePath);
        }
        if (mPageDefinition.sourceRecord != null) {
            bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.sourceRecord.commonBlock, fieldNamePath);
            if (mPageDefinition.reports.subscreenConfigurations != null && mPageDefinition.reports.subscreenConfigurations.alSubscreens!= null) {
                for (int i=0; i < mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.size(); i++) {
                    PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.get(i);
                    bRet = bRet | removeReferencedFieldInCommonBlock(subscreen.commonBlock, fieldNamePath);
                }
            }
        }
        if (mPageDefinition.matchReview != null) {
            bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.matchReview.commonBlock, fieldNamePath);
        }
        bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.recordDetails, fieldNamePath);
        bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.transactions, fieldNamePath);
        bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.duplicateRecords, fieldNamePath);
        bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.assumedMatches, fieldNamePath);
               
        if (mPageDefinition.reports != null) {
            bRet = bRet | removeReferencedFieldInCommonBlock(mPageDefinition.reports.commonBlock, fieldNamePath);
            if (mPageDefinition.reports.subscreenConfigurations != null && mPageDefinition.reports.subscreenConfigurations.alSubscreens!= null) {
                for (int i=0; i < mPageDefinition.reports.subscreenConfigurations.alSubscreens.size(); i++) {
                    PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.reports.subscreenConfigurations.alSubscreens.get(i);
                    bRet = bRet | removeReferencedFieldInCommonBlock(subscreen.commonBlock, fieldNamePath);
                }
            }

            ArrayList alReports = mPageDefinition.reports.alReport;
            for (int j=0; j<alReports.size(); j++) {
                PageDefinition.Report report = (PageDefinition.Report) alReports.get(j);
                bRet = bRet | mPageDefinition.removeReferencedField(fieldNamePath, report.alFieldRef);
            }
        }
        return bRet;
    }
    
    private boolean removeReferencedQueryBuilderInCommonBlock(PageDefinition.CommonBlock commonBlock,  String queryBuilderName) {
        boolean bRet = false;
        if (commonBlock != null) {
            ArrayList alSimpleSearchPages = commonBlock.alSimpleSearchPages;
            if (alSimpleSearchPages != null) {
                for (int i=alSimpleSearchPages.size() - 1; i >= 0 ; i--) {
                    boolean bUpdated = false;
                    PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
                    if (simpleSearchPage.alSearchOption != null) {
                        for (int k = simpleSearchPage.alSearchOption.size() - 1; k >= 0; k--) {
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
                }
                if (bRet) {
                    this.setModified(true);
                }
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
        if (mPageDefinition.eoSearch != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.eoSearch.commonBlock, queryBuilderName);
        }
        if (mPageDefinition.sourceRecord != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.sourceRecord.commonBlock, queryBuilderName);
        }
        if (mPageDefinition.matchReview != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.matchReview.commonBlock, queryBuilderName);
        }
        if (mPageDefinition.reports != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.reports.commonBlock, queryBuilderName);
        }
        if (mPageDefinition.recordDetails != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.recordDetails, queryBuilderName);
        }
        if (mPageDefinition.transactions != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.transactions, queryBuilderName);
        }
        if (mPageDefinition.duplicateRecords != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.duplicateRecords, queryBuilderName);
        }
        if (mPageDefinition.assumedMatches != null) {
            bRet = bRet | removeReferencedQueryBuilderInCommonBlock(mPageDefinition.assumedMatches, queryBuilderName);
        }
        if (bRet) {
            this.setModified(true);
        }
        return bRet;
    }
    
    private boolean updateCheckedAttributesInCommonBlock(PageDefinition.CommonBlock commonBlock, String fieldNamePath, String attributeName, boolean checked, String required) {
        boolean bUpdated;
        boolean bRet = false;
        if (commonBlock != null) {
            if (attributeName.equals(SEARCHSCREEN)) {
                ArrayList alSimpleSearchPages = commonBlock.alSimpleSearchPages;
                for (int i=0; alSimpleSearchPages != null && alSimpleSearchPages!= null && i < alSimpleSearchPages.size(); i++) {
                    PageDefinition.SimpleSearchPage simpleSearchPage = (PageDefinition.SimpleSearchPage) alSimpleSearchPages.get(i);
                    ArrayList alFieldGroup = simpleSearchPage.alFieldGroup;
                    for (int j=0; alFieldGroup!= null && j<alFieldGroup.size(); j++) {
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
        
            if (attributeName.equals(SEARCHRESULT) && commonBlock.searchResultListPage != null) {
                bUpdated = mPageDefinition.updateCheckedAttributes(fieldNamePath, 
                                                                   commonBlock.searchResultListPage.alFieldRef, 
                                                                   checked,
                                                                   required);
                ArrayList alFieldGroup = commonBlock.searchResultListPage.alFieldGroup;
                for (int j=0; alFieldGroup!= null && j<alFieldGroup.size(); j++) {
                    PageDefinition.FieldGroup fieldGroup = (PageDefinition.FieldGroup) alFieldGroup.get(j);
                    bUpdated = mPageDefinition.updateCheckedAttributes(fieldNamePath, 
                                                                       fieldGroup.alFieldRef, 
                                                                       checked, 
                                                                       required);

                    if (bUpdated) {
                        bRet = true;
                    }
                }

                if (bUpdated) {
                    bRet = true;
                }
            }
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
        if (mPageDefinition.eoSearch != null) {
            bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.eoSearch.commonBlock, fieldNamePath, attributeName, checked, required);
        }
        if (mPageDefinition.sourceRecord != null) {
            bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.sourceRecord.commonBlock, fieldNamePath, attributeName, checked, required);
            if (mPageDefinition.sourceRecord.subscreenConfigurations != null && mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens!= null) {
                for (int i=0; i < mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.size(); i++) {
                    PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.sourceRecord.subscreenConfigurations.alSubscreens.get(i);
                    bRet = bRet | updateCheckedAttributesInCommonBlock(subscreen.commonBlock, fieldNamePath, attributeName, checked, required);
                }
            }
        }
        if (mPageDefinition.matchReview != null) {
            bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.matchReview.commonBlock, fieldNamePath, attributeName, checked, required);
        }
        bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.recordDetails, fieldNamePath, attributeName, checked, required);
        bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.transactions, fieldNamePath, attributeName, checked, required);
        bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.duplicateRecords, fieldNamePath, attributeName, checked, required);
        bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.assumedMatches, fieldNamePath, attributeName, checked, required);

        if (attributeName.equals(REPORT)) {
            if (mPageDefinition.reports != null) {
                bRet = bRet | updateCheckedAttributesInCommonBlock(mPageDefinition.reports.commonBlock, fieldNamePath, attributeName, checked, required);
                if (mPageDefinition.reports.subscreenConfigurations != null && mPageDefinition.reports.subscreenConfigurations.alSubscreens!= null) {
                    for (int i=0; i < mPageDefinition.reports.subscreenConfigurations.alSubscreens.size(); i++) {
                        PageDefinition.Subscreen subscreen = (PageDefinition.Subscreen) mPageDefinition.reports.subscreenConfigurations.alSubscreens.get(i);
                        bRet = bRet | updateCheckedAttributesInCommonBlock(subscreen.commonBlock, fieldNamePath, attributeName, checked, required);
                    }
                }

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
        }
        return bRet;
    }
    
    class PageDefinition {
        String initialScreenID = null;
        String initialScreen = "EO Search";
        EOSearch eoSearch = null;
        CreateEO createEO = null;
        MatchReview matchReview = null;
        History history = null;
        Reports reports = null;
        CommonBlock auditLog = null;
        CommonBlock recordDetails = null;
        CommonBlock transactions = null;
        CommonBlock duplicateRecords = null;
        CommonBlock assumedMatches = null;
        SourceRecord sourceRecord = null;
        MidmConverter midmConverter = new MidmConverter();
        
        String getInitialScreenID() {
            return initialScreenID;
        }
        
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
            if (alFieldRef != null) {
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
            }
            return bRet;
        }
            
        boolean removeReferencedField(String fieldNamePath, ArrayList alFieldRef) {
            boolean bRet = false;
            if (alFieldRef != null) {
                for (int i=alFieldRef.size() - 1; i>=0 && i < alFieldRef.size(); i--) {            
                    FieldRef fieldRef = (FieldRef) alFieldRef.get(i);
                    if (fieldRef.fieldName.equals(fieldNamePath)) {
                        alFieldRef.remove(i);
                        bRet = true;
                    }
                }
            }
            return bRet;
        }
                
        boolean updateCheckedAttributes(String fieldNamePath, ArrayList alFieldRef, boolean checked, String required) {
            boolean bRet = false;
            boolean bFound = false;
            if (alFieldRef != null) {
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
            }
            return bRet;
        }
        
        class MidmConverter {
            // Convert edm's eoSearch to midm's record-details
            void eoSearch() {
                //mPageDefinition.eoSearch = null;
            }
            
            void createEO() {
                //mPageDefinition.createEO = null;
            }
            
            void history() {
                //mPageDefinition.history = null;
            }
            
            void matchReview() {
                //mPageDefinition.matchReview = null;
            }
            
            // Convert edm's alReports to midm's subscreen-configurations sub-screens
            void reports() {
                if (reports.alReport != null) {
                    String rootObject = reports.commonBlock.pageTab.rootObject;
                    for (int i=0; i < reports.alReport.size(); i++) {
                        Report report = (Report) reports.alReport.get(i);
                        Subscreen subScreen = reports.subscreenConfigurations.addSubscreen();
                        subScreen.enable = report.enable;
                        subScreen.reportName = report.name;
                        subScreen.commonBlock.screenID = String.valueOf(i);
                        subScreen.commonBlock.displayOrder = String.valueOf(i + 1);
                        subScreen.commonBlock.pageTab.tabName = report.title;

                        SearchResultListPage searchResultListPage = subScreen.commonBlock.addSearchResultListPage();
                        for (int j=0; report.alFieldRef != null && j<report.alFieldRef.size(); j++) {
                            PageDefinition.FieldRef fieldRef = (PageDefinition.FieldRef) report.alFieldRef.get(j);
                            String parentName;
                            int idx = fieldRef.fieldName.indexOf(rootObject + ".");
                            if (idx >= 0) {
                                parentName = rootObject;
                                // find the field group for root object
                            } else {
                                idx = fieldRef.fieldName.lastIndexOf('.');
                                parentName = rootObject + "." + fieldRef.fieldName.substring(0, idx);
                                fieldRef.fieldName = rootObject + "." + fieldRef.fieldName;
                            }
                            FieldGroup fieldGroup = searchResultListPage.getFieldGroup(parentName);
                            fieldGroup.addFieldRef(fieldRef);
                        }
                    }
                    reports.alReport = null;
                }
            }
            
            void auditLog() {
                
            }
        }
    

        class PageTab {
            String rootObject;
            String tabName;
            String tabEntrance = null;
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
            String candidateThreshold = null; //200
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
            
            void addFieldRef(PageDefinition.FieldRef fieldRef) {
                alFieldRef.add(fieldRef);
            }
            
            String getParentName() {
                String name = "";
                if (alFieldRef != null && alFieldRef.size() > 0) {
                    PageDefinition.FieldRef fieldDef = (PageDefinition.FieldRef) alFieldRef.get(0);
                    int i = fieldDef.fieldName.indexOf('.');
                    if (i >=0) {
                        name = fieldDef.fieldName.substring(0, i);
                    }
                }
                return name;
            }
        }
        
        class SimpleSearchPage {
            String screenTitle = null;
            String reportName = null;
            String searchResultID = null;
            String searchScreenOrder = null;
            String instruction = null;
            String fieldPerRow = null;
            String showEuid = null;
            String showLid = null;
            ArrayList alFieldGroup = null;
            ArrayList alSearchOption = null;
            String showStatus = null;       // false
            String showCreateDate = null;   // false
            String showCreateTime = null;   // false
            
            FieldGroup addFieldGroup() {
                FieldGroup fieldGroup = new FieldGroup();
                if (alFieldGroup == null) {
                    alFieldGroup = new ArrayList();
                }
                alFieldGroup.add(fieldGroup);
                return fieldGroup;
            }
            
            SearchOption addSearchOption() {
                SearchOption searchOption = new SearchOption();
                if (alSearchOption == null) {
                    alSearchOption = new ArrayList();
                }
                alSearchOption.add(searchOption);
                return searchOption;
            }
        }
        
        class SearchResultListPage {
            String itemPerPage = null;
            String maxResultSize = null;
            String searchResultID = null;
            String showEuid = null;         // false
            String showLid = null;          // false
            String showStatus = null;       // false
            String showCreateDate = null;   // false
            String showCreateTime = null;   // false
            String showTimeStamp = null;   // false
            ArrayList alFieldRef = new ArrayList();
            ArrayList alFieldGroup = null;
            
            FieldGroup addFieldGroup() {
                FieldGroup fieldGroup = new FieldGroup();
                if (alFieldGroup == null) {
                    alFieldGroup = new ArrayList();
                }
                alFieldGroup.add(fieldGroup);
                return fieldGroup;
            }

            FieldGroup getFieldGroup(String fieldGroupName) {
                FieldGroup fieldGroup = null; // = new FieldGroup();
                for (int i=0; alFieldGroup != null && i < alFieldGroup.size(); i++) {
                    fieldGroup = (FieldGroup) alFieldGroup.get(i);
                    if (fieldGroup.getParentName().equals(fieldGroupName)) {
                        return fieldGroup;
                    }
                }
                return addFieldGroup();
            }
        }
        
        class CommonBlock {
            String screenID = null;
            String displayOrder = null;
            PageTab pageTab = new PageTab();;
            ArrayList alSimpleSearchPages = null;
            SearchResultListPage searchResultListPage = null;
            String allowInsert; // AuditLog only

            SimpleSearchPage addSimpleSearchPage() {
                SimpleSearchPage simpleSearchPage = new SimpleSearchPage();
                if (alSimpleSearchPages == null) {
                    alSimpleSearchPages = new ArrayList();
                }
                alSimpleSearchPages.add(simpleSearchPage);
                return simpleSearchPage;
            }
            
            SearchResultListPage addSearchResultListPage() {
                if (searchResultListPage == null) {
                    searchResultListPage = new SearchResultListPage();
                }
                return searchResultListPage;
            }
        }

        class EOViewPage {
            String fieldPerRow = null;
        }
        
        class PDSearchPage {
            String fieldPerRow = null;
        }
        
        class EOSearch {
            CommonBlock commonBlock = new CommonBlock();
            EOViewPage eoViewPage = new EOViewPage();
        }
        
        void createEOSearch() {
            eoSearch = new EOSearch();
        }
        
        class CreateEO {
            PageTab pageTab = new PageTab();;
        }
        
        void createCreateEO() {
            createEO = new CreateEO();
        }
        
        class XASearchPage {
            String fieldPerRow = null;
        }
        
        class History {
            PageTab pageTab = new PageTab();;
            XASearchPage xaSearchPage = new XASearchPage();
            SearchResultListPage searchResultListPage = null;
            
            SearchResultListPage addSearchResultListPage() {
                if (searchResultListPage == null) {
                    searchResultListPage = new SearchResultListPage();
                }
                return searchResultListPage;
            }

        }
        
        void createHistory() {
            history = new History();
        }
        
        class MatchReview {
            CommonBlock commonBlock = new CommonBlock();
            PDSearchPage pdSearchPage = new PDSearchPage();
        }
        
        void createMatchReview() {
            matchReview = new MatchReview();
        }
        
        class Subscreen {
            String enable;
            String reportName = null;  // Reports only
            CommonBlock commonBlock = new CommonBlock();
        }
        
        class SubscreenConfigurations {
            ArrayList alSubscreens = null;
            
            Subscreen addSubscreen() {
                Subscreen subscreen = new Subscreen();
                if (alSubscreens == null) {
                    alSubscreens = new ArrayList();
                }
                alSubscreens.add(subscreen);
                return subscreen;
            }
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
            CommonBlock commonBlock = new CommonBlock();
            String searchPageFieldPerRow = null;
            ArrayList alReport = new ArrayList();
            SubscreenConfigurations subscreenConfigurations = new SubscreenConfigurations();
            
            Report addReport() {
                Report report = new Report();
                alReport.add(report);
                return report;
            }
        }
        
        void createReports() {
            reports = new Reports();
        }
        
        void createAuditLog() {
            auditLog = new CommonBlock();
        }
               
        void createRecordDetails() {
            recordDetails = new CommonBlock();
        }
        
        void createTransactions() {
            transactions = new CommonBlock();
        }
        
        void createDuplicateRecords() {
            duplicateRecords = new CommonBlock();
        }
        
        void createAssumedMatches() {
            assumedMatches = new CommonBlock();
        }
        
        class SourceRecord {
            CommonBlock commonBlock = new CommonBlock();
            SubscreenConfigurations subscreenConfigurations = new SubscreenConfigurations();
        }
        
        void createSourceRecord() {
            sourceRecord = new SourceRecord();
        }
    }
}
