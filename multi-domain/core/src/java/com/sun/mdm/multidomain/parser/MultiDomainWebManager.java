/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.parser;

import com.sun.mdm.multidomain.parser.SearchOptions.Parameter;
import java.util.ArrayList;

import org.w3c.dom.NodeList;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author wee
 */
public class MultiDomainWebManager {

    private DomainsForWebManager mDomains = new DomainsForWebManager();
    //private ArrayList<LinkType> mLinkTypes = new ArrayList<LinkType>();
    private ArrayList<WebDefinition> mWebDefintions = new ArrayList<WebDefinition>();
    private JNDIResources mJndiResources = null;
    //private PageDefinition mPageDefinition = new PageDefinition();
    private PageDefinition mPageDefinition = null;
    //private ArrayList<PageDefinition> mPageDefinitions = new ArrayList<PageDefinition>();
    private static String MULTIDOMAIN_WEB_MANAGER = "MultiDomainWebManager";
    
    private String validationStr = "";

    /**
     * Parse method will parse RelationshipWebManager.xml
     * @param node - input document
     */
    public DomainsForWebManager getDomains() {
        return this.mDomains;
    }

    /**public ArrayList<LinkType> getLinkTypes() {
    return this.mLinkTypes;
    }
     */    
    public ArrayList<WebDefinition> getRelationshipTypes() {
        return this.mWebDefintions;
        
    }
    
    public void addDefaultPageDefinitions() {
        if (mPageDefinition == null) {
            mPageDefinition = new PageDefinition();
            mPageDefinition.setInitialScreenId(0);
            
            ScreenDefinition relScreenDef = new ScreenDefinition();
            relScreenDef.setScreenId(mPageDefinition.getScreenDefs().size());
            relScreenDef.setDisplayOrder(mPageDefinition.getScreenDefs().size());
            relScreenDef.setScreenTitle(WebManagerProperties.mTAG_REL_SCREEN_IDENTIFIER);
            //relScreenDef.setItemPerPage(100);
            //relScreenDef.setMaxItems(100);
            relScreenDef.setIdentifier(WebManagerProperties.mTAG_REL_SCREEN_IDENTIFIER);
            relScreenDef.setViewPath("manage/relationship");
            mPageDefinition.addScreenDefition(relScreenDef);
            
            //adding subscreen
            PageDefinition subscreenDef = new PageDefinition();
            relScreenDef.setChildPage(subscreenDef);
            subscreenDef.setInitialScreenId(relScreenDef.getChildPage().getScreenDefs().size());
            
            ScreenDefinition maintainRelRef = new ScreenDefinition();
            maintainRelRef.setScreenId(relScreenDef.getChildPage().getScreenDefs().size());
            maintainRelRef.setDisplayOrder(relScreenDef.getChildPage().getScreenDefs().size());
            maintainRelRef.setScreenTitle(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_REL_SCREEN_IDENTIFIER);
            maintainRelRef.setItemPerPage(100);
            maintainRelRef.setMaxItems(100);
            maintainRelRef.setIdentifier(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER);
            maintainRelRef.setViewPath("manage/manage_relationship");
            subscreenDef.addScreenDefition(maintainRelRef);
            
            ScreenDefinition historyRelRef = new ScreenDefinition();
            historyRelRef.setScreenId(relScreenDef.getChildPage().getScreenDefs().size());
            historyRelRef.setDisplayOrder(relScreenDef.getChildPage().getScreenDefs().size());
            historyRelRef.setScreenTitle(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_REL_SCREEN_IDENTIFIER);
            historyRelRef.setItemPerPage(100);
            historyRelRef.setMaxItems(100);
            historyRelRef.setIdentifier(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER);
            historyRelRef.setViewPath("manage/history_relationship");
            subscreenDef.addScreenDefition(historyRelRef);

            ScreenDefinition hierSceenDef = new ScreenDefinition();
            hierSceenDef.setScreenId(mPageDefinition.getScreenDefs().size());
            hierSceenDef.setDisplayOrder(mPageDefinition.getScreenDefs().size());
            hierSceenDef.setScreenTitle(WebManagerProperties.mTAG_HIER_SCREEN_IDENTIFIER);
            //hierSceenDef.setItemPerPage(100);
            //hierSceenDef.setMaxItems(100);
            hierSceenDef.setIdentifier(WebManagerProperties.mTAG_HIER_SCREEN_IDENTIFIER);
            hierSceenDef.setViewPath("manage/hierarchy");
            mPageDefinition.addScreenDefition(hierSceenDef);

            PageDefinition subscreenHierDef = new PageDefinition();
            hierSceenDef.setChildPage(subscreenHierDef);
            subscreenHierDef.setInitialScreenId(hierSceenDef.getChildPage().getScreenDefs().size());
            
            ScreenDefinition maintainHierRef = new ScreenDefinition();
            maintainHierRef.setScreenId(hierSceenDef.getChildPage().getScreenDefs().size());
            maintainHierRef.setDisplayOrder(hierSceenDef.getChildPage().getScreenDefs().size());
            maintainHierRef.setScreenTitle(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_HIER_SCREEN_IDENTIFIER);
            maintainHierRef.setItemPerPage(100);
            maintainHierRef.setMaxItems(100);
            maintainHierRef.setIdentifier(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER);
            maintainHierRef.setViewPath("manage/manage_hierarchy");
            subscreenHierDef.addScreenDefition(maintainHierRef);
            
            ScreenDefinition historyHierRef = new ScreenDefinition();
            historyHierRef.setScreenId(hierSceenDef.getChildPage().getScreenDefs().size());
            historyHierRef.setDisplayOrder(hierSceenDef.getChildPage().getScreenDefs().size());
            historyHierRef.setScreenTitle(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_HIER_SCREEN_IDENTIFIER);
            historyHierRef.setItemPerPage(100);
            historyHierRef.setMaxItems(100);
            historyHierRef.setIdentifier(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER);
            historyHierRef.setViewPath("manage/history_hierarchy");
            subscreenHierDef.addScreenDefition(historyHierRef);

            
            ScreenDefinition groupSceenDef = new ScreenDefinition();
            groupSceenDef.setScreenId(mPageDefinition.getScreenDefs().size());
            groupSceenDef.setDisplayOrder(mPageDefinition.getScreenDefs().size());
            groupSceenDef.setScreenTitle(WebManagerProperties.mTAG_GROUP_SCREEN_IDENTIFIER);
            //groupSceenDef.setItemPerPage(100);
            //groupSceenDef.setMaxItems(100);
            groupSceenDef.setIdentifier(WebManagerProperties.mTAG_GROUP_SCREEN_IDENTIFIER);
            groupSceenDef.setViewPath("manage/group");
            mPageDefinition.addScreenDefition(groupSceenDef);

            PageDefinition subscreenGroupDef = new PageDefinition();
            groupSceenDef.setChildPage(subscreenGroupDef);
            subscreenGroupDef.setInitialScreenId(groupSceenDef.getChildPage().getScreenDefs().size());
            
            ScreenDefinition maintainGroupRef = new ScreenDefinition();
            maintainGroupRef.setScreenId(groupSceenDef.getChildPage().getScreenDefs().size());
            maintainGroupRef.setDisplayOrder(groupSceenDef.getChildPage().getScreenDefs().size());
            maintainGroupRef.setScreenTitle(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_GROUP_SCREEN_IDENTIFIER);
            maintainGroupRef.setItemPerPage(100);
            maintainGroupRef.setMaxItems(100);
            maintainGroupRef.setIdentifier(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER);
            maintainGroupRef.setViewPath("manage/manage_group");
            subscreenGroupDef.addScreenDefition(maintainGroupRef);
            
            ScreenDefinition historyGroupRef = new ScreenDefinition();
            historyGroupRef.setScreenId(groupSceenDef.getChildPage().getScreenDefs().size());
            historyGroupRef.setDisplayOrder(groupSceenDef.getChildPage().getScreenDefs().size());
            historyGroupRef.setScreenTitle(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_GROUP_SCREEN_IDENTIFIER);
            historyGroupRef.setItemPerPage(100);
            historyGroupRef.setMaxItems(100);
            historyGroupRef.setIdentifier(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER);
            historyGroupRef.setViewPath("manage/history_group");
            subscreenGroupDef.addScreenDefition(historyGroupRef);
            
            ScreenDefinition categorySceenDef = new ScreenDefinition();
            categorySceenDef.setScreenId(mPageDefinition.getScreenDefs().size());
            categorySceenDef.setDisplayOrder(mPageDefinition.getScreenDefs().size());
            categorySceenDef.setScreenTitle(WebManagerProperties.mTAG_CATEGORY_SCREEN_IDENTIFIER);
            //categorySceenDef.setItemPerPage(100);
            //categorySceenDef.setMaxItems(100);
            categorySceenDef.setIdentifier(WebManagerProperties.mTAG_CATEGORY_SCREEN_IDENTIFIER);
            categorySceenDef.setViewPath("manage/category");
            mPageDefinition.addScreenDefition(categorySceenDef);
            
            PageDefinition subscreenCategoryDef = new PageDefinition();
            categorySceenDef.setChildPage(subscreenCategoryDef);
            subscreenCategoryDef.setInitialScreenId(categorySceenDef.getChildPage().getScreenDefs().size());
            
            ScreenDefinition maintainCategoryRef = new ScreenDefinition();
            maintainCategoryRef.setScreenId(categorySceenDef.getChildPage().getScreenDefs().size());
            maintainCategoryRef.setDisplayOrder(categorySceenDef.getChildPage().getScreenDefs().size());
            maintainCategoryRef.setScreenTitle(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_CATEGORY_SCREEN_IDENTIFIER);
            maintainCategoryRef.setItemPerPage(100);
            maintainCategoryRef.setMaxItems(100);
            maintainCategoryRef.setIdentifier(WebManagerProperties.mTAG_MAINTAIN_SCREEN_IDENTIFIER);
            maintainCategoryRef.setViewPath("manage/manage_category");
            subscreenCategoryDef.addScreenDefition(maintainCategoryRef);
            
            ScreenDefinition historyCategoryRef = new ScreenDefinition();
            historyCategoryRef.setScreenId(categorySceenDef.getChildPage().getScreenDefs().size());
            historyCategoryRef.setDisplayOrder(categorySceenDef.getChildPage().getScreenDefs().size());
            historyCategoryRef.setScreenTitle(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER + " " + WebManagerProperties.mTAG_CATEGORY_SCREEN_IDENTIFIER);
            historyCategoryRef.setItemPerPage(100);
            historyCategoryRef.setMaxItems(100);
            historyCategoryRef.setIdentifier(WebManagerProperties.mTAG_HISTORY_SCREEN_IDENTIFIER);
            historyCategoryRef.setViewPath("manage/history_category");
            subscreenCategoryDef.addScreenDefition(historyCategoryRef);
        }
        
    }
    
    /** Retrieves the pages definitions 
     * @return Page definitions
     */
    public PageDefinition getPageDefinitions() {
        return this.mPageDefinition;
    }

    public JNDIResources getJndiResources() {
        if (mJndiResources == null) {
            mJndiResources = new JNDIResources();
        }
        return this.mJndiResources;
    }
    
    public Definition getLinkType(String name, String source, String target) {
        for (Definition type : mWebDefintions) {
            if (type.getName().equals(name) && type.getSourceDomain().equals(source) && type.getTargetDomain().equals(target)) {
                return type;
            }
        }
        return null;
    }
    
    public Definition getLinkType(String domain) {
        for (Definition type : mWebDefintions) {
            if (type.getSourceDomain().equals(domain) || type.getTargetDomain().equals(domain)) {
                return type;
            }
        }
        return null;
    }
    
    public PageDefinition getPageDefinition() {
        return this.mPageDefinition;
    }
    
    public boolean validateWebManagerXML() {
        boolean isValid = true;
        for (DomainForWebManager domain : mDomains.getDomains()) {
            if (!domain.isValidDomainXML()) {
                validationStr = domain.getValidationStr();
                return false;
            }
        }
        return isValid;
    }
    
    public Definition createWebDefinition(String name, String source, String target, String defintionType) {
        Definition linkType = new WebDefinition();
        linkType.setName(name);
        linkType.setType(defintionType);
        linkType.setSourceDomain(source);
        linkType.setTargetDomain(target);
        this.mWebDefintions.add((WebDefinition) linkType);
        
        return linkType;
    }   
    
    public void deleteWebDefinition(String name, String source, String target) {
        for (Definition type : mWebDefintions) {
            if (type.getName().equals(name) && type.getSourceDomain().equals(source) && type.getTargetDomain().equals(target)) {
                mWebDefintions.remove(type);
                return;
            }
        }
        
    }
    
    public void deleteWebDefinition(Definition webDefinition) {
        mWebDefintions.remove(webDefinition);
      
    }   
    
    public void addWebDefinition(Definition webDefinition) {
        mWebDefintions.add((WebDefinition) webDefinition);
    }


    public String writeToString() throws IOException, Exception {
        //XMLWriterUtil xmlDoc = new XMLWriterUtil();
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        xmldoc = impl.createDocument(null, MULTIDOMAIN_WEB_MANAGER, null);

        Element root = xmldoc.getDocumentElement();
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "schema/MultiDomainWebManager.xsd");
        root.appendChild(geDefinitionTypesToStr(xmldoc));
        root.appendChild(getPageDefinitionToStr(xmldoc));
        root.appendChild(getDomainsToStr(xmldoc));
        root.appendChild(getJndiResToStr(xmldoc));
        byte[] webXml = transformToXML(xmldoc);
        return new String(webXml);

    }
    
    public byte[] transformToXML(Document xmldoc) throws Exception {
        DOMConfiguration domConfig = xmldoc.getDomConfig();
        //domConfig.setParameter("format-pretty-print", "true");
        DOMSource domSource = new DOMSource(xmldoc);
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute("indent-number", 4);        
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(new OutputStreamWriter(out, "UTF-8"));
        serializer.transform(domSource, result);
        return out.toByteArray();
        
    }

    private Element geDefinitionTypesToStr(Document xmlDoc) throws Exception {
        
        Element relTypes = xmlDoc.createElement(WebManagerProperties.mTAG_DEFINITION_TYPES);
        for (WebDefinition relType : mWebDefintions) {
            //WebDefinition relType = (WebDefinition) linkType;
            String relTypeName = relType.getName();
            String destination = relType.getTargetDomain();
            String source = relType.getSource();
            String displayName = relType.getDisplayName();
            Element relTypeElm = xmlDoc.createElement(relType.getType());
            relTypeElm.setAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_SOURCE, source);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_DESTINATION, destination);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_NAME, relTypeName);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_RELATIONSHIP_DISPLAY_NAME, displayName);

            Element relFixedAttr = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_FIXED_ATTRS);
            
            relTypeElm.appendChild(relFixedAttr);
            getFieldAttrs(xmlDoc, relFixedAttr, relType.getPredefinedFieldRefs());

            if (relType.getExtendedRelFieldRefs() != null && relType.getExtendedRelFieldRefs().size() > 0) {
                Element relExtendedAttr = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_EXTENTED_ATTRS);
                relTypeElm.appendChild(relExtendedAttr);
                getFieldAttrs(xmlDoc, relExtendedAttr, relType.getExtendedRelFieldRefs());
            }
            relTypes.appendChild(relTypeElm);
            
            
        }
        
        return relTypes;
    }
    
    private void getFieldAttrs(Document xmlDoc, Node elmNode, ArrayList<RelationFieldReference> fieldRefs) {
        for (RelationFieldReference fieldRef : fieldRefs) {
            Element relTypeFieldElm = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_REF);
            elmNode.appendChild(relTypeFieldElm);
            Element eFieldName = xmlDoc.createElement(WebManagerProperties.mTAG_NAME);
            eFieldName.appendChild(xmlDoc.createTextNode(fieldRef.getFieldName()));
            Element eDisplayName = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_NAME);
            eDisplayName.appendChild(xmlDoc.createTextNode(fieldRef.getFieldDisplayName()));
            Element eMaxLen = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_MAX_LENGTH);
            eMaxLen.appendChild(xmlDoc.createTextNode(Integer.toString(fieldRef.getMaxLength())));
            Element eGuiType = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_GUI_TYPE);
            eGuiType.appendChild(xmlDoc.createTextNode(fieldRef.getGuiType()));
            Element eValueList = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_VALUE_LIST);
            if (fieldRef.getValueList() != null) {
                eGuiType.appendChild(xmlDoc.createTextNode(fieldRef.getValueList()));
            }
            Element eValueType = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_VALUE_TYPE);
            if (fieldRef.getValueType() != null) {
                eValueType.appendChild(xmlDoc.createTextNode(fieldRef.getValueType()));
            }
            Element eDisplayOrder = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_ORDER);
            eDisplayOrder.appendChild(xmlDoc.createTextNode(Integer.toString(fieldRef.getDisplayOrder())));
            Element eInputMask = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_INPUT_MASK);
            if (fieldRef.getInputMask() != null) {
                eInputMask.appendChild(xmlDoc.createTextNode(fieldRef.getInputMask()));
            }

            Element eValueMask = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_VALUE_MASK);

            if (fieldRef.getOutputMask() != null) {
                eValueMask.appendChild(xmlDoc.createTextNode(fieldRef.getOutputMask()));
            }

            Element eSensitive = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_IS_SENSITIVE);
            eSensitive.appendChild(xmlDoc.createTextNode(Boolean.toString(fieldRef.isSensitive())));
            
            Element eRangeSearch = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_IS_RANGE);
            eRangeSearch.appendChild(xmlDoc.createTextNode(Boolean.toString(fieldRef.isRangeSearch())));

            relTypeFieldElm.appendChild(eFieldName);
            relTypeFieldElm.appendChild(eDisplayName);
            relTypeFieldElm.appendChild(eDisplayOrder);
            relTypeFieldElm.appendChild(eMaxLen);
            relTypeFieldElm.appendChild(eGuiType);
            relTypeFieldElm.appendChild(eValueList);
            relTypeFieldElm.appendChild(eValueType);
            relTypeFieldElm.appendChild(eInputMask);            
            relTypeFieldElm.appendChild(eValueMask);            
            relTypeFieldElm.appendChild(eSensitive);   
            relTypeFieldElm.appendChild(eRangeSearch);
        }

        
    }
    
    private Element getPageDefinitionToStr(Document xmlDoc) {
        Element elmPageDefs = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_DEFINITION);
        if (mPageDefinition != null) {
            getScreenDefitionToStr(xmlDoc, elmPageDefs, mPageDefinition);
        }
        return elmPageDefs;
    }

    private void getScreenDefitionToStr(Document xmlDoc, Element parent, PageDefinition screenDefitions) {
        //Element elmPageDefs = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_DEFINITION);
        Element elmInitialScreenId = xmlDoc.createElement(WebManagerProperties.mTAG_INITIAL_SCREEN_ID);
        elmInitialScreenId.appendChild(xmlDoc.createTextNode(String.valueOf(screenDefitions.getInitialScreenId())));
        parent.appendChild(elmInitialScreenId);
        //ArrayList<ScrrpageDefinition.get
        for (ScreenDefinition screenDef : screenDefitions.getScreenDefs()) {
            Element elmScreen = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN);
            
            Element elmIdentifier = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_IDENTIFIER);
            elmIdentifier.appendChild(xmlDoc.createTextNode(screenDef.getIdentifier()));
            elmScreen.appendChild(elmIdentifier);
            
            Element elmScreenTitle = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN_TITLE);
            elmScreenTitle.appendChild(xmlDoc.createTextNode(screenDef.getScreenTitle()));
            elmScreen.appendChild(elmScreenTitle);
            
            Element elmViewPath = xmlDoc.createElement(WebManagerProperties.mTAG_VIEW_PATH);
            elmViewPath.appendChild(xmlDoc.createTextNode(screenDef.getViewPath()));
            elmScreen.appendChild(elmViewPath);
            
            Element elmScreenId = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN_ID);
            elmScreenId.appendChild(xmlDoc.createTextNode(String.valueOf(screenDef.getScreenId())));
            elmScreen.appendChild(elmScreenId);

            Element elmDisplayOrder = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN_DISPLAY_ORDER);
            elmDisplayOrder.appendChild(xmlDoc.createTextNode(String.valueOf(screenDef.getDisplayOrder())));
            elmScreen.appendChild(elmDisplayOrder);
            
            if (screenDef.getItemPerPage() > 0) {
                Element elmItemPerPage = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_ITEM_PER_PAGE);
                elmItemPerPage.appendChild(xmlDoc.createTextNode(String.valueOf(screenDef.getItemPerPage())));
                elmScreen.appendChild(elmItemPerPage);               
            }

            if (screenDef.getMaxItems() > 0) {
                Element elmMaxItems = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_MAX_RESULT_SIZE);
                elmMaxItems.appendChild(xmlDoc.createTextNode(String.valueOf(screenDef.getMaxItems())));
                elmScreen.appendChild(elmMaxItems);               
            }
            
            // for Subscreen
            if (screenDef.getChildPage() != null) {
                Element elmSubScreen = xmlDoc.createElement(WebManagerProperties.mTAG_SUB_SCREENS);
                getScreenDefitionToStr(xmlDoc, elmSubScreen, screenDef.getChildPage());
                elmScreen.appendChild(elmSubScreen);
            }
            
            if (screenDef.getPageRelationType() != null && screenDef.getPageRelationType().size() > 0) {
                Element elmSearchPages = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_PAGES);
                Element elmSimplePage = xmlDoc.createElement(WebManagerProperties.mTAG_SIMPLE_SEARCH_PAGE);
                elmSearchPages.appendChild(elmSimplePage);
                ArrayList<PageRelationType> relTypes = screenDef.getPageRelationType();
                for (PageRelationType tabRelType : relTypes) {
                    Element elmTabRelType = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_TYPE);
                    elmTabRelType.setAttribute(WebManagerProperties.mTAG_NAME, tabRelType.getRelType());

                    if (tabRelType.getFieldGroups().size() > 0) {
                        ArrayList<FieldGroup> relFieldGroups = tabRelType.getFieldGroups();
                        getFieldGroup(relFieldGroups, xmlDoc, elmTabRelType, WebManagerProperties.mTAG_REL_FIELD_GROUP);
                    }
                    
                    elmSimplePage.appendChild(elmTabRelType);

                }
                elmScreen.appendChild(elmSearchPages);
                
            }

            parent.appendChild(elmScreen);
             
        }
        
    }

    private Element getDomainsToStr(Document xmlDoc) throws Exception {
        Element elmDomains = xmlDoc.createElement(WebManagerProperties.mTAG_DOMAINS);
        ArrayList<DomainForWebManager> domains = mDomains.getDomains();
        for (DomainForWebManager domain : domains) {
            Element elmDomain = xmlDoc.createElement(WebManagerProperties.mTAG_DOMAIN);
            elmDomains.appendChild(elmDomain);
            String domainName = domain.getDomainName();
            Element elmDomainName = xmlDoc.createElement(WebManagerProperties.mTAG_NAME);
            elmDomainName.appendChild(xmlDoc.createTextNode(domain.getDomainName()));
            elmDomain.appendChild(elmDomainName);

            // domain xml location
            Element elmDomainLoc = xmlDoc.createElement(WebManagerProperties.mTAG_MIDM_XML_LOCATION);
            elmDomainLoc.appendChild(xmlDoc.createTextNode(domain.getMidmXMLLocation()));
            elmDomain.appendChild(elmDomainLoc);

            //search-pages
            ArrayList<SimpleSearchType> searchPages = domain.getSearchType();
            //Iterator searchPagesItr = searchPages.values().iterator();
            
            Element elmSearchPages = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_PAGES);

            
            for (SimpleSearchType simpleSearch : searchPages) {                
                Element elmSimpleSearch = xmlDoc.createElement(WebManagerProperties.mTAG_SIMPLE_SEARCH_PAGE);
                
                Element elmScreenTitle = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN_TITLE);
                elmScreenTitle.appendChild(xmlDoc.createTextNode(simpleSearch.getScreenTitle()));
                elmSimpleSearch.appendChild(elmScreenTitle);


                Element elmSearchId = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_ID);
                elmSearchId.appendChild(xmlDoc.createTextNode(Integer.toString(simpleSearch.getScreenResultID())));
                elmSimpleSearch.appendChild(elmSearchId);

                Element elmSearchScreenOrder = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_SCREEN_ORDER);
                elmSearchScreenOrder.appendChild(xmlDoc.createTextNode(Integer.toString(simpleSearch.getScreenOrder())));
                elmSimpleSearch.appendChild(elmSearchScreenOrder);

                Element elmInstruction = xmlDoc.createElement(WebManagerProperties.mTAG_INSTRUCTION);
                if (simpleSearch.getInstruction() != null) {
                    elmInstruction.appendChild(xmlDoc.createTextNode(simpleSearch.getInstruction()));
                }
                elmSimpleSearch.appendChild(elmInstruction);


                ArrayList<FieldGroup> fieldGroups = simpleSearch.getFieldGroups();
                getFieldGroup(fieldGroups, xmlDoc, elmSimpleSearch, WebManagerProperties.mTAG_FIELD_GROUP);

                //create SearchOption Element Node
                Element elmSearchOption = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_OPTION);
                SearchOptions searchOpt = simpleSearch.getSearchOption();
                Element elmQueryBuilder = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_QUERY_BUILDER);
                elmQueryBuilder.appendChild(xmlDoc.createTextNode(searchOpt.getQueryBulder()));
                elmSearchOption.appendChild(elmQueryBuilder);
                
                Element elmWeighted = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_WEIGHTED);
                elmWeighted.appendChild(xmlDoc.createTextNode(Boolean.toString(searchOpt.getWeighted())));
                elmSearchOption.appendChild(elmWeighted);
                
                ArrayList<Parameter> optParameters = searchOpt.getParameterList();
                for (Parameter param : optParameters) {
                    Element elmParam = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_OPTION_PARAMETER);
                    Element elmParamName = xmlDoc.createElement(WebManagerProperties.mTAG_OPTION_PARAMETER_NAME);
                    elmParamName.appendChild(xmlDoc.createTextNode(param.getName()));
                    Element elmParamValue = xmlDoc.createElement(WebManagerProperties.mTAG_OPTION_PARAMETER_VALUE);
                    elmParamValue.appendChild(xmlDoc.createTextNode(param.getValue()));
                    elmParam.appendChild(elmParamName);
                    elmParam.appendChild(elmParamValue);
                    elmSearchOption.appendChild(elmParam);
                    
                }
                
                elmSimpleSearch.appendChild(elmSearchOption);
                
                
                elmSearchPages.appendChild(elmSimpleSearch);
                
            }
            
            
            elmDomain.appendChild(elmSearchPages);
            //elmDomainName.appendChild(elmSearchPages);
            
            ArrayList<SearchDetail> searchDetails = domain.getSearchDetail();
            Element elmSearchDetail = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_PAGES);

            for (SearchDetail searchDetail : searchDetails) {
                Element elmSDetail = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_DETAIL);
                elmSearchDetail.appendChild(elmSDetail);

                Element elmResultName = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_NAME);
                elmResultName.appendChild(xmlDoc.createTextNode(searchDetail.getDisplayName()));
                elmSDetail.appendChild(elmResultName);
                
                Element elmResultId = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_ID);
                elmResultId.appendChild(xmlDoc.createTextNode(Integer.toString(searchDetail.getSearchResultID())));
                elmSDetail.appendChild(elmResultId);
                
                Element elmDetailId = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETAIL_ID);
                elmDetailId.appendChild(xmlDoc.createTextNode(Integer.toString(searchDetail.getRecordDetailID())));
                elmSDetail.appendChild(elmDetailId);

                Element elmItem = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_ITEM_PER_PAGE);
                elmItem.appendChild(xmlDoc.createTextNode(Integer.toString(searchDetail.getItemPerPage())));
                elmSDetail.appendChild(elmItem);

                Element elmMax = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_MAX_RESULT_SIZE);
                elmMax.appendChild(xmlDoc.createTextNode(Integer.toString(searchDetail.getMaxResultSize())));
                elmSDetail.appendChild(elmMax);

                ArrayList<FieldGroup> fieldGroups = searchDetail.getFieldGroups();
                getFieldGroup(fieldGroups, xmlDoc, elmSDetail, WebManagerProperties.mTAG_FIELD_GROUP);
                
            }

            
            elmDomain.appendChild(elmSearchDetail);
            
            
            ArrayList<RecordDetail> recordDetailList = domain.getRecordDetailList();
            Element elmRecordDetailPages = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETAIL_PAGES);


            for (RecordDetail recDetail : recordDetailList) {
                Element elmRecDetail = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETAIL);
                elmRecordDetailPages.appendChild(elmRecDetail);
                Element elmRecDetailId = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETAIL_ID);
                elmRecDetailId.appendChild(xmlDoc.createTextNode(Integer.toString(recDetail.getRecordDetailId())));
                elmRecDetail.appendChild(elmRecDetailId);
                Element elmRecDetailName = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETAIL_NAME);
                elmRecDetailName.appendChild(xmlDoc.createTextNode(recDetail.getDisplayName()));
                elmRecDetail.appendChild(elmRecDetailName);
                ArrayList<FieldGroup> fieldGroups = recDetail.getFieldGroups();
                getFieldGroup(fieldGroups, xmlDoc, elmRecDetail, WebManagerProperties.mTAG_FIELD_GROUP);
                elmRecordDetailPages.appendChild(elmRecDetail);
            }

            
            elmDomain.appendChild(elmRecordDetailPages);
            
            Element elmRecordID = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_ID);
            DomainRecordID recordID = domain.getRecordID();
            if (recordID != null) {
                Element elmShowEUID = xmlDoc.createElement(WebManagerProperties.mTAG_SHOW_EUID);
                elmShowEUID.appendChild(xmlDoc.createTextNode(Boolean.toString(recordID.isMShowEUID())));
                elmRecordID.appendChild(elmShowEUID);
   
                FieldGroup group = recordID.getFieldGroup();                
                if (group.getFieldRefs().size() > 0) {               
                    Element elmGroup = xmlDoc.createElement(WebManagerProperties.mTAG_FIELD_GROUP);
                    elmRecordID.appendChild(elmGroup);
                    for (FieldGroup.FieldRef field : group.getFieldRefs()) {
                        Element elmField = xmlDoc.createElement(WebManagerProperties.mTAG_FIELD_REF);
                        elmField.appendChild(xmlDoc.createTextNode(field.getFieldName()));
                        elmGroup.appendChild(elmField);
                }
                }
            }
            elmDomain.appendChild(elmRecordID);
            
            Element elmRecordSummary = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_SUMMARY);
            getFieldGroup(domain.getRecordSummaryFields(), xmlDoc, elmRecordSummary, WebManagerProperties.mTAG_FIELD_GROUP);
            elmDomain.appendChild(elmRecordSummary);
        }
        
        return elmDomains;
    }
    
    private void getFieldGroup(ArrayList<FieldGroup> fieldGroups, Document xmlDoc, Node parent, String elmName) {
        for (FieldGroup fieldGroup : fieldGroups) {
            Element elmGroup = xmlDoc.createElement(elmName);
            ArrayList<FieldGroup.FieldRef> fields = fieldGroup.getFieldRefs();
            if (fieldGroup.getDescription() != null && fieldGroup.getDescription().length() > 0) {
                Element elmGroupDescr = xmlDoc.createElement(WebManagerProperties.mTAG_FIELD_GROUP_DESCRIPTION);
                elmGroup.appendChild(elmGroupDescr);
                elmGroupDescr.appendChild(xmlDoc.createTextNode(fieldGroup.getDescription()));
            }
            for (FieldGroup.FieldRef field : fields) {
                Element elmField = xmlDoc.createElement(WebManagerProperties.mTAG_FIELD_REF);
                elmField.appendChild(xmlDoc.createTextNode(field.getFieldName()));
                elmGroup.appendChild(elmField);
            }
            parent.appendChild(elmGroup);
        }

    }

    private Element getJndiResToStr(Document xmlDoc) {
        Element elmJNDI = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_JNDI);
        if (mJndiResources != null) {
            ArrayList<RelationshipProperty> properties = mJndiResources.getProperties();
            if (properties.size() > 0) {
                Element elmProperties = xmlDoc.createElement(WebManagerProperties.mTAG_PROPERTIES);
                elmJNDI.appendChild(elmProperties);
                for (RelationshipProperty property : properties) {
                    Element elmProperty = xmlDoc.createElement(WebManagerProperties.mTAG_PROPERTY);
                    elmProperty.setAttribute(WebManagerProperties.mTAG_PROPERTY_NAME, property.getPropertyName());
                    elmProperty.setAttribute(WebManagerProperties.mTAG_PROPERTY_VALUE, property.getPropertyValue());
                    elmProperties.appendChild(elmProperty);
                }
            }
            ArrayList<RelationshipJDNIResources> jndiRes = mJndiResources.getJDNIResources();
            for (RelationshipJDNIResources res : jndiRes) {
                Element elmJDNIRes = xmlDoc.createElement(WebManagerProperties.mTAG_JNDIRESOURCES);
                elmJDNIRes.setAttribute(WebManagerProperties.mTAG_JNDI_NAME, res.getJNDIName());
                elmJDNIRes.setAttribute(WebManagerProperties.mTAG_JNDI_ID, res.getID());
                elmJDNIRes.setAttribute(WebManagerProperties.mTAG_JNDI_RESOURCE_TYPE, res.getResourceType());
                elmJDNIRes.setAttribute(WebManagerProperties.mTAG_JNDI_DESCRIPTION, res.getDescription());
                elmJNDI.appendChild(elmJDNIRes);
            }
        }
        return elmJNDI;
    }

    /**
     * The parseNode method parse RelationshipWebManager.xml file.
     * @param node - xml document.
     */
    public void parseNode(Node node) throws Exception {

        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl1 = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl1.getLength(); i++) {
                if (nl1.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl1.item(i);
                    break;
                }
            }

            if (null != element && ((Element) element).getTagName().equals(WebManagerProperties.mTAG_MULTIDOMAIN_WEB_MANAGER) && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (name.equals(WebManagerProperties.mTAG_DEFINITION_TYPES)) {
                            parseRelTypes(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_PAGE_DEFINITION)) {
                            mPageDefinition = new PageDefinition();
                            parsePageDef(nl1.item(i1), mPageDefinition);
                        } else if (name.equals(WebManagerProperties.mTAG_DOMAINS)) {
                            parseDomains(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_RELATIONSHIP_JNDI)) {
                            parseRelJNDI(nl1.item(i1));
                        }
                    }
                }
            }
        }
    }

    /**
     * The methods parse relationship-types section of doc and
     * constructs into LinkType class.
     * @param node
     */
    private void parseRelTypes(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            String nameAttr = null;
            String destAttr = null;
            String sourceAttr = null;
            String displayAttr = null;

            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elm.hasAttributes()) {
                    nameAttr = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    destAttr = elm.getAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_DESTINATION);
                    sourceAttr = elm.getAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_SOURCE);
                    displayAttr = elm.getAttribute(WebManagerProperties.mTAG_RELATIONSHIP_DISPLAY_NAME);
                }
                ArrayList<RelationFieldReference> fixedFieldRefs = new ArrayList<RelationFieldReference>();
                
                ArrayList<RelationFieldReference> extendedFieldRefs = new ArrayList<RelationFieldReference>();

                WebDefinition relType = new WebDefinition(nameAttr, sourceAttr, destAttr, displayAttr, fixedFieldRefs, extendedFieldRefs);
                
                relType.setType(elementName);
                
                parseType(elm, relType);

                mWebDefintions.add(relType);


            }
        }
    }

    /**
     * Parse relationship type for web
     * @param node - relationship-type object
     * @param fieldRefs
     */
    private void parseType(Node node, WebDefinition relType) {

        String elementName = null;

        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_RELATIONSHIP_FIXED_ATTRS)) {
                    //ArrayList<RelationFieldReference> fieldRefs = new ArrayList<RelationFieldReference>();
                    parseFieldAttrs(elm, relType.getPredefinedFieldRefs());
                } else if (elementName.equals(WebManagerProperties.mTAG_RELATIONSHIP_EXTENTED_ATTRS)) {
                    parseFieldAttrs(elm, relType.getExtendedRelFieldRefs());
                }
            }
        }
    }

    private void parseFieldAttrs(Node node, ArrayList<RelationFieldReference> fieldRefs) {
        String elementName = null;

        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                fieldRefs.add(parseRelFieldDef(elm));
            }
        }
        
    }

    /**
     * Parse rs-field-ref element content
     * @param node
     * @return fieldRef - RelationFieldReference
     */
    private RelationFieldReference parseRelFieldDef(Node node) {
        String elementName = null;
        String displayName = null;
        int displayOrder = -1;
        String guiType = null;
        boolean keyType = false;
        int maxLen = -1;
        String valueList = null;
        String valueType = null;
        String fieldName = null;
        String inputMask = null;
        String outputMask = null;
        boolean sensitive = false;
        boolean isRange = false;

        NodeList children = node.getChildNodes();

        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();

                if (elementName.equals(WebManagerProperties.mTAG_NAME)) {
                    fieldName = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_NAME)) {
                    displayName = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_ORDER)) {
                    displayOrder = RelationshipUtil.getIntElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_GUI_TYPE)) {
                    guiType = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_KEY_TYPE)) {                    
                    keyType = Boolean.valueOf(RelationshipUtil.getStrElementValue(elm)).booleanValue();
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_MAX_LENGTH)) {
                    maxLen = RelationshipUtil.getIntElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_VALUE_LIST)) {
                    valueList = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_VALUE_TYPE)) {
                    valueType = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_INPUT_MASK)) {
                    inputMask = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_VALUE_MASK)) {
                    outputMask = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_IS_SENSITIVE)) {
                    sensitive = Boolean.valueOf(RelationshipUtil.getStrElementValue(elm)).booleanValue();
                }  else if (elementName.equals(WebManagerProperties.mTAG_REL_FIELD_IS_RANGE)) {
                    isRange = Boolean.valueOf(RelationshipUtil.getStrElementValue(elm)).booleanValue();
                } 
            }

        }

        RelationFieldReference fieldRef = new RelationFieldReference(fieldName, displayName,
                displayOrder, maxLen, guiType, valueList, valueType, 
                inputMask, outputMask, sensitive, isRange);

        return fieldRef;


    }

    /**
     * Parse page-definition element
     * @param node - page-definition element node
     */
    private void parsePageDef(Node node, PageDefinition pageDef) {
        
        String elementName = null;

        NodeList children = node.getChildNodes();
        //PageDefinition pageDef = new PageDefinition();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_INITIAL_SCREEN_ID)) {
                    int initialScreenId = RelationshipUtil.getIntElementValue(elm);
                    pageDef.setInitialScreenId(initialScreenId);
                } else if (elementName.equals(WebManagerProperties.mTAG_SCREEN)) {
                    pageDef.addScreenDefition(parsePage(elm));
                } 
                
            }
        }

    }

    /**
     * Parse page element
     * @param node - page element node
     */
    private ScreenDefinition parsePage(Node node) {
        String elementName = null;

        PageRelationType pageRelType = null;
        NodeList children = node.getChildNodes();
        String pageIdentifier = null;
        String screenTitle = null;
        int screenId = -1;
        int screenDisplayOrder = -1;
        int itemPerPage = -1;
        int maxItems = -1;
        int intitalTabId = -1;
        String viewPath = null;
        RelationshipPageTabDefination pageTab = null;
        ScreenDefinition screenDef = new ScreenDefinition();
        int tabId = -1;
        String tabName = null;
        String tabDisplayName = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_PAGE_IDENTIFIER)) {
                    pageIdentifier = RelationshipUtil.getStrElementValue(elm);
                    screenDef.setIdentifier(pageIdentifier);
                } else if (elementName.equals(WebManagerProperties.mTAG_SCREEN_TITLE)) {
                    screenTitle = RelationshipUtil.getStrElementValue(elm);
                    screenDef.setScreenTitle(screenTitle);
                } else if (elementName.equals(WebManagerProperties.mTAG_VIEW_PATH)) {
                    viewPath = RelationshipUtil.getStrElementValue(elm);
                    screenDef.setViewPath(viewPath);
                } else if (elementName.equals(WebManagerProperties.mTAG_SCREEN_ID)) {
                    screenId = RelationshipUtil.getIntElementValue(elm);
                    screenDef.setScreenId(screenId);
                } else if (elementName.equals(WebManagerProperties.mTAG_SCREEN_DISPLAY_ORDER)) {
                    screenDisplayOrder = RelationshipUtil.getIntElementValue(elm);
                    screenDef.setDisplayOrder(screenDisplayOrder);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_ITEM_PER_PAGE)) {
                    itemPerPage = RelationshipUtil.getIntElementValue(elm);
                    screenDef.setItemPerPage(itemPerPage);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_MAX_RESULT_SIZE)) {
                    maxItems = RelationshipUtil.getIntElementValue(elm);
                    screenDef.setMaxItems(maxItems);
                } else if (elementName.equals(WebManagerProperties.mTAG_SUB_SCREENS)) {
                    PageDefinition subScreenDef = new PageDefinition();
                    parsePageDef(elm, subScreenDef);
                    screenDef.setChildPage(subScreenDef);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_PAGES)) {
                    parseRelSearchPages(elm, screenDef);
                }
                /**
                else if (elementName.equals(WebManagerProperties.mTAG_RELATIONSHIP_TYPE)) {
                    String relType = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    pageRelType = new PageRelationType(relType);
                    //parsePageRelationship(elm.getChildNodes(), pageRelType);
                    parseRelFieldGroup(elm, pageRelType);
                    screenDef.addPageRelationType(pageRelType);
                }
                 */ 

            }

        }
        
        return screenDef;
    }

    private void parseRelSearchPages(Node node, ScreenDefinition screenDef) {
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                parseRelSimpleSearchPage(elm, screenDef);
            }
        }
        
    }
    
    private void parseRelSimpleSearchPage(Node node, ScreenDefinition screenDef) {
        NodeList children = node.getChildNodes();
        PageRelationType pageRelType = null;
        String elementName = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_RELATIONSHIP_TYPE)) {
                    String relType = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    pageRelType = new PageRelationType(relType);
                    //parsePageRelationship(elm.getChildNodes(), pageRelType);
                    parseRelFieldGroup(elm, pageRelType);
                    screenDef.addPageRelationType(pageRelType);
                }

            }
        }
        
    }
    
    private void parsePageRelationship(NodeList children, PageRelationType pageRelType) {
        String elementName = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                parseRelFieldGroup(elm, pageRelType);

            }

        }

    }

    private void parseRelFieldGroup(Node node, PageRelationType pageRelation) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                //String relType = elm.getAttribute(NAME);
                FieldGroup fieldGroup = new FieldGroup();
                pageRelation.addFieldGroup(fieldGroup);
                parseFieldGroup(elm, fieldGroup);
            }

        }


    }

    private void parseDomains(Node node) throws Exception {
        String elementName = null;

        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                mDomains.addDomain(parseDomain(elm));

            }

        }


    }

    private DomainForWebManager parseDomain(Node node) throws Exception {

        String elementName = null;
        NodeList children = node.getChildNodes();
        DomainForWebManager domain = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_NAME)) {
                    String value = RelationshipUtil.getStrElementValue(elm);
                    domain = new DomainForWebManager(value);                    
                } else if (elementName.equals(WebManagerProperties.mTAG_MIDM_XML_LOCATION)) {
                    String value = RelationshipUtil.getStrElementValue(elm);
                    domain.setMidmXMLLocation(value);                 
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_PAGES)) {
                    parseSearchPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_PAGES)) {
                    parseSearchResultPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETAIL_PAGES)) {
                    parseRecordDetailPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_ID)) {
                    parseRecordId(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_SUMMARY)) {
                    parseRecordSummary(elm, domain);
                }

            }

        }

        return domain;

    }
    
    private void parseRecordId(Node node, DomainForWebManager domain) {
        
        String elementName = null;

        NodeList children = node.getChildNodes();
        
        DomainRecordID recordID = new DomainRecordID();

        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_SHOW_EUID)) {
                    boolean value = RelationshipUtil.getBooleanElementValue(elm); 
                    recordID.setMShowEUID(value);
                 } else  if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP)) {
                    parseFieldGroup(elm, recordID.getFieldGroup());
                }                

            }
        }
        
        domain.setRecordID(recordID);

        
    }
    
    private void parseRecordSummary(Node node, DomainForWebManager domain) {
        String elementName = null;

        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP)) {
                    FieldGroup fieldGroup = new FieldGroup();
                    parseFieldGroup(elm, fieldGroup);
                    domain.addRecordSummary(fieldGroup);
                }                

            }
        }
        
        
    }
    
    private void parseRecordDetailPages(Node node, DomainForWebManager domain) throws Exception {

        String elementName = null;
        NodeList children = node.getChildNodes();
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                //RecordDetail recordDetail = new RecordDetail(); 
                parseRecordDetail(elm, domain);
            }
        }


    }
    
    private void parseRecordDetail(Node node, DomainForWebManager domain) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        RecordDetail recordDetail = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETAIL_ID)) {
                    recordDetail = new RecordDetail(RelationshipUtil.getIntElementValue(elm));
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETAIL_NAME)) {
                    recordDetail.setDisplayName(RelationshipUtil.getStrElementValue(elm));
                } else if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP)) {
                    FieldGroup fieldGroup = new FieldGroup();
                    parseFieldGroup(elm, fieldGroup);
                    recordDetail.addFieldGroup(fieldGroup);                    
                }
            }
        }
        
        if (recordDetail != null) {
            domain.addRecordDetail(recordDetail);
        }


    }

    private void parseSearchResultPages(Node node, DomainForWebManager domain) {

        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            int resultID = -1;
            int itemPerPage = -1;
            int maxResult = -1;
            int detailID = -1;
            String resultName = null;
            ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                String value = RelationshipUtil.getStrElementValue(elm);
                Node childNode = (Node) elm;
                NodeList subChildren = childNode.getChildNodes();

                for (int i2 = 0; i2 < subChildren.getLength(); i2++) {
                    if (subChildren.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                        Element subElm = (Element) subChildren.item(i2);
                        elementName = subElm.getTagName();
                        value = RelationshipUtil.getStrElementValue(subElm);
                        if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP)) {
                            FieldGroup fieldGroup = new FieldGroup();
                            fieldGroups.add(fieldGroup);
                            parseFieldGroup(subElm, fieldGroup);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_ID)) {
                            resultID = RelationshipUtil.getIntElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETAIL_ID)) {
                            detailID = RelationshipUtil.getIntElementValue(subElm);                            
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_ITEM_PER_PAGE)) {
                            itemPerPage = RelationshipUtil.getIntElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_MAX_RESULT_SIZE)) {
                            maxResult = RelationshipUtil.getIntElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_NAME)) {
                            resultName = RelationshipUtil.getStrElementValue(subElm);
                        }

                    }

                }

                if (resultID != -1 && itemPerPage != -1 && maxResult != -1) {
                    SearchDetail searchResult = new SearchDetail(resultID, itemPerPage, maxResult, detailID, resultName, fieldGroups);
                    domain.addSearchDetail(domain.getDomainName(), resultID, searchResult);
                }
            }
        }


    }

    private void parseSearchPages(Node node, DomainForWebManager domain) {

        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            String screenTitle = null;
            int searchResultID = -1;
            String instruction = null;
            SearchOptions searchOpt = null;
            int screenOrder = 0;

            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                String value = RelationshipUtil.getStrElementValue(elm);
                Node childNode = (Node) elm;
                NodeList subChildren = childNode.getChildNodes();
                ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
                for (int i2 = 0; i2 < subChildren.getLength(); i2++) {
                    if (subChildren.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                        Element subElm = (Element) subChildren.item(i2);
                        elementName = subElm.getTagName();
                        value = RelationshipUtil.getStrElementValue(subElm);
                        if (elementName.equals(WebManagerProperties.mTAG_SCREEN_TITLE)) {
                            screenTitle = RelationshipUtil.getStrElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_ID)) {
                            searchResultID = RelationshipUtil.getIntElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_INSTRUCTION)) {
                            instruction = RelationshipUtil.getStrElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP)) {
                            FieldGroup fieldGroup = new FieldGroup();
                            fieldGroups.add(fieldGroup);
                            parseFieldGroup(subElm, fieldGroup);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_OPTION)) {
                            searchOpt = parseSearchOption(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_SCREEN_ORDER)) {
                            screenOrder = RelationshipUtil.getIntElementValue(subElm);
                        }
                        
                    }

                }

                if (screenTitle != null && searchResultID != -1) {
                    SimpleSearchType simpleSearchType = new SimpleSearchType(screenTitle,
                            searchResultID, instruction, screenOrder, fieldGroups);
                    simpleSearchType.setSearchOption(searchOpt);
                    domain.addSearchType(screenTitle, simpleSearchType);
                }

            }
        }


    }

    private SearchOptions parseSearchOption(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        
        SearchOptions searchOpt = new SearchOptions();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_SEARCH_QUERY_BUILDER)) {
                    searchOpt.setQueryBuilder(RelationshipUtil.getStrElementValue(elm));
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_WEIGHTED)) {
                    searchOpt.setWeighted(Boolean.parseBoolean(RelationshipUtil.getStrElementValue(elm)));
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_OPTION_PARAMETER)) {
                    NodeList subChildren = elm.getChildNodes();
                    String name = null;
                    String value = null;
                    for (int i2 = 0; i2 < subChildren.getLength(); i2++) {
                        if (subChildren.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                            Element subElm = (Element) subChildren.item(i2);
                            String subElementName = subElm.getTagName();
                            if (subElementName.equals(WebManagerProperties.mTAG_OPTION_PARAMETER_NAME)) {
                                name = RelationshipUtil.getStrElementValue(subElm);
                            } else if (subElementName.equals(WebManagerProperties.mTAG_OPTION_PARAMETER_VALUE)) {
                                value = RelationshipUtil.getStrElementValue(subElm);
                            }

                            if (name != null && value != null) {
                                searchOpt.addParameter(name, value);
                            }
                        }

                    }

                }
                
                
            }
        }
        return searchOpt;
    }
    
    private void parseFieldGroup(Node node, FieldGroup fieldGroup) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_FIELD_GROUP_DESCRIPTION)) {
                    String groupDescription = RelationshipUtil.getStrElementValue(elm);
                    fieldGroup.setDescription(groupDescription);
                } else if (elementName.equals(WebManagerProperties.mTAG_FIELD_REF)) {
                    String fieldName = RelationshipUtil.getStrElementValue(elm);
                    fieldGroup.addFieldRef(fieldGroup.createFieldRef(fieldName));
                }
            }
        }
    }

    private void parseRelJNDI(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        
        mJndiResources = new JNDIResources(new ArrayList<RelationshipProperty>(), new ArrayList<RelationshipJDNIResources>());
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elm.getTagName().equals(WebManagerProperties.mTAG_PROPERTIES)) {
                    parseProperties(elm);
                } else if (elm.getTagName().equals(WebManagerProperties.mTAG_JNDIRESOURCES)) {
                    parseResources(elm);
                }
            }
        }

    }

    private void parseProperties(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                String name = elm.getAttribute(WebManagerProperties.mTAG_PROPERTY_NAME);
                String value = elm.getAttribute(WebManagerProperties.mTAG_PROPERTY_VALUE);
                RelationshipProperty property = new RelationshipProperty(name, value);
                this.mJndiResources.addProperty(property);
            }
        }

    }

    private void parseResources(Node node) {
        if (node.hasAttributes()) {
            String jndiName = ((Element) node).getAttribute(WebManagerProperties.mTAG_JNDI_NAME);
            String id = ((Element) node).getAttribute(WebManagerProperties.mTAG_JNDI_ID);
            String resType = ((Element) node).getAttribute(WebManagerProperties.mTAG_JNDI_RESOURCE_TYPE);
            String description = ((Element) node).getAttribute(WebManagerProperties.mTAG_JNDI_DESCRIPTION);
            RelationshipJDNIResources res = new RelationshipJDNIResources(jndiName, id, resType, description);
            mJndiResources.addJNDIResource(res);

        }

    }

    public String getValidationStr() {
        return validationStr;
    }
}
