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
import java.io.OutputStream;
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
public class RelationshipWebManager {

    private DomainsForWebManager mDomains = new DomainsForWebManager();
    private ArrayList<RelationshipType> mRelationshipTypes = new ArrayList<RelationshipType>();
    private JNDIResources mJndiResources = null;
    private ArrayList<PageDefinition> mPageDefinitions = new ArrayList<PageDefinition>();
    
    private static String RELATIONSHIP_WEB_MANAGER = "RelationshipWebManager";

    /**
     * Parse method will parse RelationshipWebManager.xml
     * @param node - input document
     */
    public DomainsForWebManager getDomains() {
        return this.mDomains;
    }

    public ArrayList<RelationshipType> getRelationshipTypes() {
        return this.mRelationshipTypes;
    }

    public JNDIResources getJndiResources() {
        return this.mJndiResources;
    }

    public String writeToString() throws IOException, Exception {



        //XMLWriterUtil xmlDoc = new XMLWriterUtil();
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        xmldoc = impl.createDocument(null, "RelationshipWebManager", null);

        Element root = xmldoc.getDocumentElement();
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "C:/Projects/Multi-Domains/xsd/RelationshipWebManager.xsd");
        root.appendChild(getRelationTypeToStr(xmldoc));
        root.appendChild(getPageDefinitionTypeToStr(xmldoc));
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

            //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "RelationshipWebManager.xsd");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
            //serializer.setOutputProperty(OutputKeys., "yes");
            //OutputStream os = new OutputStream()
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //StreamResult streamResult = new StreamResult(out);
            Result result = new StreamResult(new OutputStreamWriter(out,"UTF-8"));
            serializer.transform(domSource, result);
            //serializer.
            
            return out.toByteArray();
        
    }

    private Element getRelationTypeToStr(Document xmlDoc) throws Exception{
        
        Element relTypes = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_TYPES);
        for (RelationshipType relType : mRelationshipTypes) {
            String relTypeName = relType.getRelTypeName();
            String destination = relType.getDestionation();
            String source = relType.getSource();
            //Element elm = xmlDoc.createElement("relationship-types");
            Element relTypeElm = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_TYPE);
            //org.jdom.Attribute relAttrName = new org.jdom.Attribute(WebManagerProperties.mTAG_NAME, relTypeName);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_SOURCE, source);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_DESTINATION, destination);
            relTypeElm.setAttribute(WebManagerProperties.mTAG_NAME, relTypeName);
            ArrayList<RelationFieldReference> fieldRefs = relType.getRelFieldRefs();
            for (RelationFieldReference fieldRef : fieldRefs) {
                Element relTypeFieldElm = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_REF);
                relTypeElm.appendChild(relTypeFieldElm);
                Element eFieldName = xmlDoc.createElement(WebManagerProperties.mTAG_NAME);
                eFieldName.appendChild(xmlDoc.createTextNode( fieldRef.getFieldName()));
                Element eDisplayName = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_NAME);
                eDisplayName.appendChild(xmlDoc.createTextNode(fieldRef.getFieldDisplayName()));
                Element eMaxLen = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_MAX_LENGTH);
                eMaxLen.appendChild(xmlDoc.createTextNode(Integer.toString(fieldRef.getMaxLength())));
                Element eGuiType = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_GUI_TYPE);
                eGuiType.appendChild(xmlDoc.createTextNode(fieldRef.getGuiType()));
                Element eValueList = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_VALUE_LIST);
                eGuiType.appendChild(xmlDoc.createTextNode(fieldRef.getValueList()));
                Element eValueType = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_VALUE_TYPE);
                eValueType.appendChild(xmlDoc.createTextNode(fieldRef.getValueType()));
                Element eKeyType = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_KEY_TYPE);
                eKeyType.appendChild(xmlDoc.createTextNode(Boolean.toString(fieldRef.getKeyType())));
                Element eDisplayOrder = xmlDoc.createElement(WebManagerProperties.mTAG_REL_FIELD_DISPLAY_ORDER);
                eDisplayOrder.appendChild(xmlDoc.createTextNode(Integer.toString(fieldRef.getDisplayOrder())));
                relTypeFieldElm.appendChild(eFieldName);
                relTypeFieldElm.appendChild(eDisplayName);
                relTypeFieldElm.appendChild(eDisplayOrder);
                relTypeFieldElm.appendChild(eMaxLen);
                relTypeFieldElm.appendChild(eGuiType);
                relTypeFieldElm.appendChild(eValueList);
                relTypeFieldElm.appendChild(eValueType);
                relTypeFieldElm.appendChild(eKeyType); 
            }
            
            relTypes.appendChild(relTypeElm);
            
        }
        
        return relTypes;
    }

    private Element getPageDefinitionTypeToStr(Document xmlDoc) {
        Element elmPageDefs = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_DEFINITION);
        
        for (PageDefinition page : mPageDefinitions) {
            Element elmPage = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE);
            
            Element elmIdentifier = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_IDENTIFIER);
            elmIdentifier.appendChild(xmlDoc.createTextNode(page.getPageIdentifier()));
            elmPage.appendChild(elmIdentifier);
            
            
            ArrayList<RelationshipPageTabDefination> pageTabs = page.getPageTabs();
            
            for (RelationshipPageTabDefination tab : pageTabs ) {
                Element elmTab = xmlDoc.createElement(WebManagerProperties.mTAG_PAGE_TAB);
                elmTab.setAttribute(WebManagerProperties.mTAG_NAME, tab.getTabName());
                
                ArrayList<PageRelationType> relTypes = tab.getPageRelationType();
                for (PageRelationType tabRelType : relTypes) {
                    Element elmTabRelType = xmlDoc.createElement(WebManagerProperties.mTAG_RELATIONSHIP_TYPE);
                    elmTabRelType.setAttribute(WebManagerProperties.mTAG_NAME, tabRelType.getRelType());
                    
                    if (tabRelType.getFieldGroups().size() > 0) {
                        ArrayList<FieldGroup> relFieldGroups = tabRelType.getFieldGroups();
                        getFieldGroup(relFieldGroups, xmlDoc, elmTabRelType, WebManagerProperties.mTAG_REL_FIELD_GROUP);
                    }
                    
                    elmTab.appendChild(elmTabRelType);
                    
                }
                elmPage.appendChild(elmTab);
            }
            elmPageDefs.appendChild(elmPage);
            
        }
        
        return elmPageDefs;
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
            
            //search-pages
            ArrayList<SimpleSearchType> searchPages = domain.getSearchType();
            //Iterator searchPagesItr = searchPages.values().iterator();
            
            Element elmSearchPages = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_PAGES);

            
            for ( SimpleSearchType simpleSearch : searchPages) { 
                Element elmSimpleSearch = xmlDoc.createElement(WebManagerProperties.mTAG_SIMPLE_SEARCH_PAGE);
                
                Element elmScreenTitle = xmlDoc.createElement(WebManagerProperties.mTAG_SCREEN_TITLE);
                elmScreenTitle.appendChild(xmlDoc.createTextNode(simpleSearch.getScreenTitle()));
                elmSimpleSearch.appendChild(elmScreenTitle);


                Element elmSearchId = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_ID);
                elmSearchId.appendChild(xmlDoc.createTextNode(Integer.toString(simpleSearch.getScreenResultID())));
                elmSimpleSearch.appendChild(elmSearchId);

                Element elmInstruction = xmlDoc.createElement(WebManagerProperties.mTAG_INSTRUCTION);
                if (simpleSearch.getInstruction() != null) {
                    elmInstruction.appendChild(xmlDoc.createTextNode(simpleSearch.getInstruction()));
                }
                elmSimpleSearch.appendChild(elmInstruction);


                ArrayList<FieldGroup> fieldGroups = simpleSearch.getFieldGroups();
                getFieldGroup(fieldGroups, xmlDoc, elmSimpleSearch, WebManagerProperties.mTAG_FIELD_GROUP);
                
                //create SearchOption Element Node
                Element elmSearchOption = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_OPTION);
                SearchOptions searchOpt =simpleSearch.getSearchOption();
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

            for ( SearchDetail searchDetail : searchDetails) {
                Element elmSDetail = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_DETAILL);
                elmSearchDetail.appendChild(elmSDetail);

                Element elmResultName = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_NAME);
                elmResultName.appendChild(xmlDoc.createTextNode(searchDetail.getDisplayName()));
                elmSDetail.appendChild(elmResultName);
                
                Element elmResultId = xmlDoc.createElement(WebManagerProperties.mTAG_SEARCH_RESULT_ID);
                elmResultId.appendChild(xmlDoc.createTextNode(Integer.toString(searchDetail.getSearchResultID())));
                elmSDetail.appendChild(elmResultId);
                
                Element elmDetailId = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETIAL_ID);
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
            Element elmRecordDetailPages = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETIAL_PAGES);


            for (RecordDetail recDetail : recordDetailList) {
                Element elmRecDetail = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETIAL);
                elmRecordDetailPages.appendChild(elmRecDetail);
                Element elmRecDetailId = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETIAL_ID);
                elmRecDetailId.appendChild(xmlDoc.createTextNode(Integer.toString(recDetail.getRecordDetailId())));
                elmRecDetail.appendChild(elmRecDetailId);
                Element elmRecDetailName = xmlDoc.createElement(WebManagerProperties.mTAG_RECORD_DETIAL_NAME);
                elmRecDetailName.appendChild(xmlDoc.createTextNode(recDetail.getDisplayName()));
                elmRecDetail.appendChild(elmRecDetailName);
                ArrayList<FieldGroup> fieldGroups = recDetail.getFieldGroups();
                getFieldGroup(fieldGroups, xmlDoc, elmRecDetail, WebManagerProperties.mTAG_FIELD_GROUP);
                elmRecordDetailPages.appendChild(elmRecDetail);
            }

            
            elmDomain.appendChild(elmRecordDetailPages);
            
        }
        
        return elmDomains;
    }
    
    
    private void getFieldGroup (ArrayList<FieldGroup> fieldGroups, Document xmlDoc, Node parent, String elmName){
        for (FieldGroup fieldGroup : fieldGroups) {
            Element elmGroup = xmlDoc.createElement(elmName);
            ArrayList<FieldGroup.FieldRef> fields = fieldGroup.getFeildRefs();
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
        Element elmProperties = xmlDoc.createElement(WebManagerProperties.mTAG_PROPERTIES);
        elmJNDI.appendChild(elmProperties);
        ArrayList<RelationshipProperty> properties = mJndiResources.getProperties();
        for (RelationshipProperty property : properties) {
            Element elmProperty = xmlDoc.createElement(WebManagerProperties.mTAG_PROPERTY);
            elmProperty.setAttribute(WebManagerProperties.mTAG_PROPERTY_NAME, property.getPropertyName());
            elmProperty.setAttribute(WebManagerProperties.mTAG_PROPERTY_VALUE, property.getPropertyValue());
            elmProperties.appendChild(elmProperty);
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
        return elmJNDI;
    }

    /**
     * The parseNode method parse RelationshipWebManager.xml file.
     * @param node - xml document.
     */
    public void parseNode(Node node) {

        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl1 = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl1.getLength(); i++) {
                if (nl1.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl1.item(i);
                    break;
                }
            }

            if (null != element && ((Element) element).getTagName().equals(WebManagerProperties.mTAG_RELATIONSHIP_WEB_MANAGER) && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (name.equals(WebManagerProperties.mTAG_RELATIONSHIP_TYPES)) {
                            parseRelTypes(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_PAGE_DEFINITION)) {
                            parsePageDef(nl1.item(i1));
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
     * constructs into RelationshipType class.
     * @param node
     */
    private void parseRelTypes(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            String nameAttr = null;
            String destAttr = null;
            String sourceAttr = null;

            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elm.hasAttributes()) {
                    nameAttr = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    destAttr = elm.getAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_DESTINATION);
                    sourceAttr = elm.getAttribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_SOURCE);

                }

                ArrayList<RelationFieldReference> fieldRefs = new ArrayList<RelationFieldReference>();

                RelationshipType relType = new RelationshipType(nameAttr, destAttr, sourceAttr, fieldRefs);

                parseType(elm, fieldRefs);

                mRelationshipTypes.add(relType);


            }
        }
    }

    /**
     * Parse relationship type for web
     * @param node - relationship-type object
     * @param fieldRefs
     */
    private void parseType(Node node, ArrayList<RelationFieldReference> fieldRefs) {

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
                }

            }

        }

        RelationFieldReference fieldRef = new RelationFieldReference(fieldName, displayName,
                displayOrder, maxLen, guiType, valueList, valueType, keyType);

        return fieldRef;


    }

    /**
     * Parse page-definition element
     * @param node - page-definition element node
     */
    private void parsePageDef(Node node) {
        String elementName = null;

        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                parsePage(elm);
            }
        }

    }

    /**
     * Parse page element
     * @param node - page element node
     */
    private void parsePage(Node node) {
        String elementName = null;

        NodeList children = node.getChildNodes();
        String pageIdentifier = null;
        String tabName = null;
        RelationshipPageTabDefination pageTab = null;
        PageDefinition pageDef = null;
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_PAGE_IDENTIFIER)) {
                    pageIdentifier = RelationshipUtil.getStrElementValue(elm);
                    pageDef = new PageDefinition(pageIdentifier);
                } else if (elementName.equals(WebManagerProperties.mTAG_PAGE_TAB)) {

                    if (elm.hasAttribute(WebManagerProperties.mTAG_NAME)) {
                        tabName = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    }
                    pageTab = new RelationshipPageTabDefination(tabName);
                    pageTab.addPageRelationType(parsePageRelationship(elm.getChildNodes()));
                    pageDef.addPageTab(pageTab);

                }

            }

        }
        
        if (pageDef != null) {
            mPageDefinitions.add(pageDef);
        }
    }

    private PageRelationType parsePageRelationship(NodeList children) {
        String elementName = null;
        PageRelationType pageRelType = null;
        //NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                String relType = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                pageRelType = new PageRelationType(relType);
                parseRelFieldGroup(elm, pageRelType);

            }

        }
        return pageRelType;

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

    private void parseDomains(Node node) {
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

    private DomainForWebManager parseDomain(Node node) {

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
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_PAGES)) {
                    parseSearchPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_PAGES)) {
                    parseSearchResultPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETIAL_PAGES)) {
                    parseRecordDetailPages(elm, domain);
                }

            }

        }

        return domain;

    }
    
    private void parseRecordDetailPages(Node node, DomainForWebManager domain) {
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
                if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETIAL_ID)) {
                    recordDetail = new RecordDetail(RelationshipUtil.getIntElementValue(elm));
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETIAL_NAME)) {
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
                        } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETIAL_ID)) {
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
                        }
                        
                    }

                }

                if (screenTitle != null && searchResultID != -1) {
                    SimpleSearchType simpleSearchType = new SimpleSearchType(screenTitle,
                            searchResultID, instruction, fieldGroups);
                    simpleSearchType.setSearchOption(searchOpt);
                    domain.addSearchType(screenTitle, simpleSearchType);

                }

            }
        }


    }

    private SearchOptions parseSearchOption(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        
        SearchOptions  searchOpt = new SearchOptions();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_SEARCH_QUERY_BUILDER)) {
                    searchOpt.setQueryBuilder(RelationshipUtil.getStrElementValue(elm));
                } else  if (elementName.equals(WebManagerProperties.mTAG_SEARCH_WEIGHTED)) {
                    searchOpt.setWeighted(Boolean.parseBoolean(RelationshipUtil.getStrElementValue(elm)));
                } else  if (elementName.equals(WebManagerProperties.mTAG_SEARCH_OPTION_PARAMETER)) {
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
                String fieldName = RelationshipUtil.getStrElementValue(elm);
                fieldGroup.addFieldRef(fieldGroup.createFieldRef(fieldName));
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
}
