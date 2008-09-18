/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.IOException;
import org.jdom.DocType;
import org.jdom.JDOMException;

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

    public String writeToString() throws IOException, JDOMException, Exception {



        XMLWriterUtil xmlDoc = new XMLWriterUtil();
        DocType type = new DocType(RELATIONSHIP_WEB_MANAGER, "schema/RelationshipWebManager.xsd");
        xmlDoc.setRoot("modulelist", "schema/RelationshipWebManager.xsd", "schema/RelationshipWebManager.xsd");
        getRelationTypeToStr(xmlDoc);
        getPageDefinitionTypeToStr(xmlDoc);
        getDomainsToStr(xmlDoc);
        getJndiResToStr(xmlDoc);
        String webXmlStr = new String(xmlDoc.getXMLStream());
        return webXmlStr;

    }

    private void getRelationTypeToStr(XMLWriterUtil xmlDoc) throws Exception{
        
        org.jdom.Element relTypes = xmlDoc.addElement("relationship-types");
        for (RelationshipType relType : mRelationshipTypes) {
            String relTypeName = relType.getRelTypeName();
            String destination = relType.getDestionation();
            String source = relType.getSource();
            org.jdom.Element elm = xmlDoc.addElement(relTypes, "relationship-types");
            org.jdom.Element relTypeElm = xmlDoc.addElement("relationship-type");
            org.jdom.Attribute relAttrName = new org.jdom.Attribute(WebManagerProperties.mTAG_NAME, relTypeName);
            relTypeElm.setAttribute(relAttrName);
            org.jdom.Attribute relAttrSource = new org.jdom.Attribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_SOURCE, source);
            relTypeElm.setAttribute(relAttrSource);
            org.jdom.Attribute relAttrDest = new org.jdom.Attribute(WebManagerProperties.mTAG_RELATIONSHIP_TYPE_DESTINATION, destination);
            relTypeElm.setAttribute(relAttrDest);
            ArrayList<RelationFieldReference> fieldRefs = relType.getRelFieldRefs();
            for (RelationFieldReference fieldRef : fieldRefs) {
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_NAME, fieldRef.getFieldName());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_DISPLAY_NAME, fieldRef.getDisplayOrder());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_MAX_LENGTH, fieldRef.getMaxLength());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_GUI_TYPE, fieldRef.getGuiType());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_VALUE_LIST, fieldRef.getValueList());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_VALUE_TYPE, fieldRef.getValueType());
                xmlDoc.setElement(relTypeElm, WebManagerProperties.mTAG_REL_FIELD_KEY_TYPE, Boolean.toString(fieldRef.getKeyType()));                
            }
            
        }
    }

    private void getPageDefinitionTypeToStr(XMLWriterUtil xmlDoc) {
    }

    private void getDomainsToStr(XMLWriterUtil xmlDoc) throws Exception {
        org.jdom.Element relTypes = xmlDoc.addElement(WebManagerProperties.mTAG_DOMAINS);
    }

    private void getJndiResToStr(XMLWriterUtil xmlDoc) {
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
                        if (name.equals(WebManagerProperties.mTAG_RELATIONSHIP_TYPE)) {
                            parseRelTypes(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_PAGE_DEFINITION)) {
                            parsePageDef(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_DOMAINS)) {
                            parseDomains(nl1.item(i1));
                        } else if (name.equals(WebManagerProperties.mTAG_JNDIRESOURCES)) {
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
                    keyType = Boolean.getBoolean(RelationshipUtil.getStrElementValue(elm));
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
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                if (elementName.equals(WebManagerProperties.mTAG_PAGE_IDENTIFIER)) {
                    pageIdentifier = RelationshipUtil.getStrElementValue(elm);
                } else if (elementName.equals(WebManagerProperties.mTAG_PAGE_TAB)) {

                    if (elm.hasAttribute(WebManagerProperties.mTAG_NAME)) {
                        tabName = elm.getAttribute(WebManagerProperties.mTAG_NAME);
                    }
                    pageTab = new RelationshipPageTabDefination(tabName);
                    pageTab.addPageRelationType(parsePageRelationship(elm.getChildNodes()));

                }

                if (tabName != null && pageIdentifier != null) {
                    PageDefinition pageDef = new PageDefinition();
                    pageDef.addPageTab(pageTab);
                    mPageDefinitions.add(pageDef);
                }
            }

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
                parseRelFieldGroup(elm);

            }

        }
        return pageRelType;

    }

    private void parseRelFieldGroup(Node node) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                //String relType = elm.getAttribute(NAME);
                FieldGroup fieldGroup = new FieldGroup();
                fieldGroups.add(fieldGroup);
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
        DomainForWebManager domain = new DomainForWebManager();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                elementName = elm.getTagName();
                String value = RelationshipUtil.getStrElementValue(elm);
                if (elementName.equals(WebManagerProperties.mTAG_SEARCH_PAGES)) {
                    parseSearchPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_RESULT_PAGES)) {
                    parseSearchResultPages(elm, domain);
                } else if (elementName.equals(WebManagerProperties.mTAG_RECORD_DETIAL)) {
                    parseRecordDetail(elm, domain);
                }

            }

        }

        return domain;

    }

    private void parseRecordDetail(Node node, DomainForWebManager domain) {
        String elementName = null;
        NodeList children = node.getChildNodes();
        ArrayList<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            if (children.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                Element elm = (Element) children.item(i1);
                FieldGroup fieldGroup = new FieldGroup();
                fieldGroups.add(fieldGroup);
                parseFieldGroup(elm, fieldGroup);
            }
        }


    }

    private void parseSearchResultPages(Node node, DomainForWebManager domain) {

        String elementName = null;
        NodeList children = node.getChildNodes();
        for (int i1 = 0; i1 < children.getLength(); i1++) {
            int resultID = -1;
            int itemPerPage = -1;
            int maxResult = -1;
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
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_ITEM_PER_PAGE)) {
                            itemPerPage = RelationshipUtil.getIntElementValue(subElm);
                        } else if (elementName.equals(WebManagerProperties.mTAG_SEARCH_MAX_RESULT_SIZE)) {
                            maxResult = RelationshipUtil.getIntElementValue(subElm);
                        }

                    }

                }

                if (resultID != -1 && itemPerPage != -1 && maxResult != -1) {
                    SearchDetail searchResult = new SearchDetail(resultID, itemPerPage, maxResult, fieldGroups);
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
                        }
                    }

                }

                if (screenTitle != null && searchResultID != -1) {

                    SimpleSearchType simpleSearchType = new SimpleSearchType(screenTitle,
                            searchResultID, instruction, fieldGroups);
                    domain.addSearchType(screenTitle, simpleSearchType);
                }

            }
        }


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
