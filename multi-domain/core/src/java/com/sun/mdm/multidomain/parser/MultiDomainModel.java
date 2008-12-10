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
package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;

/**
 * @author Kevin Kao
 * @version
 */
public class MultiDomainModel {
    private final String mTagConfiguration = "Configuration";
    private final String mTagName = "name";
    private final String mTagPlugin = "plugin";
    private final String mTagDatabase = "database";
    private final String mTagDataTypes = "datatypes";
    private final String mTagColumns = "columns";
    private final String mTagDateFormat = "dateformat";
    private final String mTagDomains = "domains";
    private final String mTagDomain = "domain";
    private final String mTagSourceDomain = "source-domain";
    private final String mTagTargetDomain = "target-domain";
    private final String mTagDescription = "description";
    private final String mTagRelationship  = "relationship";
    private final String mTagHierarchy = "hierarchy";
    private final String mTagGroup = "group";
    private final String mTagCategory = "category";
    private final String mTagDirection = "direction";
    private final String mTagEffectiveFrom = "effective-from";
    private final String mTagEffectiveTo = "effective-to";
    private final String mTagPredefinedAttributes = "predefined-attributes";
    private final String mTagExtendedAttributes = "extended-attributes";
    private final String mTagAttribute = "attribute";
    private final String mTagDataType = "data-type";
    private final String mTagSearchable = "searchable";
    private final String mTagRequired = "required";
    private final String mTagIncluded = "included";
    private final String mTagStartDate = "start-date";
    private final String mTagEndDate = "end-date";
    private final String mTagColumnName = "column-name";
    private final String mTagDefaultValue = "default-value";
    private Map mMapDataTypes = new HashMap();  // datatype, columns
    private Domains mDomains = new Domains();
    private ArrayList <Definition> mAlDefinitions = new ArrayList();
    private String strDatabase;
    private String strDateFormat = "MM/dd/yyyy";
    private boolean mModified = false;


    /**
     * The main program for the MultiDomainModel class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            Document doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param String dateformat
     */
    public void setDateFormat(String dateformat) {
        strDateFormat = dateformat;
    }

    /**
     * @return String DateFormat
     */
    public String getDateFormat() {
        return strDateFormat;
    }

    /**
     * @param String database
     */
    public void setDataBase(String database) {
        strDatabase = database;
    }

    /**
     * @return String Database
     */
    public String getDatabase() {
        return strDatabase;
    }

    public ArrayList <Definition> getAllDefinitions() {
        return this.mAlDefinitions;
    }
    
    public ArrayList <String> getAssociatedDomains(String domainName) {
        ArrayList <String> al = new ArrayList();
        for (int i=0; i< this.mAlDefinitions.size(); i++) {
            Definition definition = (Definition) mAlDefinitions.get(i);
            String sourceDomain = definition.getSourceDomain();
            String targetDomain = definition.getTargetDomain();
            String associatedDomain = null;
            if (domainName.equals(sourceDomain) || domainName.equals(targetDomain)) {
                associatedDomain = domainName.equals(sourceDomain) ? targetDomain : sourceDomain;
                al.add(associatedDomain);
            }
        }
        return al;
    }
    
    /**
     * @param String name of definition
     * @param String sourceDomain
     * @param String targetDomain
     * @return definition
     */
    public Definition getDefinition(String name, String sourceDomain, String targetDomain) {
        Definition definition = null;
        return definition;
    }

    /**
     * @return ArrayList of definition
     */
    public ArrayList <Definition> getDefinitionsByDomain(String domainName) {
        ArrayList <Definition> al = new ArrayList();
        for (int i=0; i< this.mAlDefinitions.size(); i++) {
            Definition definition = (Definition) mAlDefinitions.get(i);
            String sourceDomain = definition.getSourceDomain();
            String targetDomain = definition.getTargetDomain();
            if (domainName.equals(sourceDomain) || domainName.equals(targetDomain)) {
                al.add(definition);
            }
        }
        return al;
    }
    
    /**
     * 
     * @param defName
     * @param sourceDomain
     * @param targetDomain
     */
    public void deleteDefinition(String defName, String sourceDomain, String targetDomain) {
        for (int i=0; i< this.mAlDefinitions.size(); i++) {
            Definition definition = (Definition) mAlDefinitions.get(i);
            if (definition.getName().equals(defName) &&
                definition.getSourceDomain().equals(sourceDomain) &&
                definition.getTargetDomain().equals(targetDomain)) {
                mAlDefinitions.remove(i);
                break;
            }
        }
    }
    
    /** Delete a definition when domain is removed
     * 
     * @param sourceDomain
     */
    public int deleteDefinition(String domain) {
        int cnt = 0;
        int length = this.mAlDefinitions.size();
        for (int i=length - 1; i>=0 && i < length; i--) {
            Definition definition = (Definition) mAlDefinitions.get(i);
            if (definition.getSourceDomain().equals(domain) ||
                definition.getTargetDomain().equals(domain)) {
                mAlDefinitions.remove(i);
                cnt++;
            }
        }
        return cnt;
    }
    
    /** Add a definition to the array mAlDefinitions
     * 
     * @param definition
     */
    public void addDefinition(Definition definition) {
        mAlDefinitions.add(definition);
    }
    
    /** Get map of key:datatype entry:# of columns
     * 
     * @return Map mMapDatatypes
     */
    public Map getDatatypeMap() {
        return this.mMapDataTypes;
    }
    
    /**
     * @param String domain name
     * @return Domain
     */
    public Domain getDomain(String name) {
        return mDomains.getDomain(name);
    }
    
    /**
     * @return ArrayList Domain Names
     */
    public void setDomainNames(ArrayList <String> alDomainNames) {
        mDomains.alDomainNames = alDomainNames;
    }
    
    /**
     * @return ArrayList Domain Names
     */
    public ArrayList <String> getDomainNames() {
        return mDomains.getDomainNames();
    }
    
    /**
     * @param node node
     */
    public void parseDataTypes(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagDataType.equals(((Element) nl.item(i)).getTagName())) {
                        String name = getAttributeValue(nl.item(i), mTagName);
                        String columns = getAttributeValue(nl.item(i), mTagColumns);
                        this.mMapDataTypes.put(name, columns);
                    }
                }
            }
        }
    }
    
    /**
     * @param node node
     */
    public void parseDomains(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagDomain.equals(((Element) nl.item(i)).getTagName())) {
                        Domain domain = new Domain();
                        domain.name = getAttributeValue(nl.item(i), mTagName);
                        mDomains.addDomain(domain);
                    }
                }
            }
        }
    }

    public String getAttributeValue(Node node, String attrName) {
        String val = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attrNode = nnm.getNamedItem(attrName);
            if (attrNode != null) {
                try {
                    val = attrNode.getNodeValue();
                } catch (DOMException ex) {
                }
            }
        }
        return val;
    }
    
    private Attribute parseAttribute(Node node) {
        Attribute attr = new Attribute();
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setName(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagColumnName.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setColumnName(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagDefaultValue.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setDefaultValue(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagDataType.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setDataType(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagSearchable.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setSearchable(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagRequired.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setRequired(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagIncluded.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setIncluded(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagStartDate.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setStartDate(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagEndDate.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setEndDate(Utils.getStrElementValue(nl.item(i)));
                    }                
                }
            }
        }
        return attr;

    }
    
    private ArrayList parseAttributes(Node node) {
        ArrayList al = new ArrayList();
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagAttribute.equals(((Element) nl.item(i)).getTagName())) {
                        al.add(parseAttribute(nl.item(i)));
                    }
                }
            }
        }
        return al;
    }
    
    /**
     * @param node node
     */
    public void parseDefinition(Node node, String type) {
        Definition definition = new Definition(type);
        String val = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attrName = nnm.getNamedItem(mTagName);
            if (attrName != null) {
                try {
                    definition.name = attrName.getNodeValue();
                } catch (DOMException ex) {
                }
            }
        }

        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagSourceDomain.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setSourceDomain(getAttributeValue(nl.item(i), mTagName));
                    } else if (mTagTargetDomain.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setTargetDomain(getAttributeValue(nl.item(i), mTagName));
                    } else if (mTagDomain.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setDomain(getAttributeValue(nl.item(i), mTagName));
                    } else if (mTagPlugin.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setPlugin(getAttributeValue(nl.item(i), mTagName));
                    } else if (mTagDirection.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setDirection(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagDescription.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setDescription(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagEffectiveFrom.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setEffectiveFrom(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagEffectiveTo.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setEffectiveTo(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagPredefinedAttributes.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setPredefinedAttributes(parseAttributes(nl.item(i)));
                    } else if (mTagExtendedAttributes.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setExtendedAttributes(parseAttributes(nl.item(i)));
                    }
                }
            }
        }
        mAlDefinitions.add(definition);
    }

    /**
     * @param node node
     */
    public void parse(Node node) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl.item(i);
                    break;
                }
            }

            if (null != element
                     && ((Element) element).getTagName().equals(mTagConfiguration)
                     && element.hasChildNodes()) {
                nl = element.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if (mTagDatabase.equals(((Element) nl.item(i)).getTagName())) {
                            strDatabase = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagDateFormat.equals(((Element) nl.item(i)).getTagName())) {
                            strDateFormat = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagDataTypes.equals(((Element) nl.item(i)).getTagName())) {
                            parseDataTypes(nl.item(i));
                        } else if (mTagDomains.equals(((Element) nl.item(i)).getTagName())) {
                            parseDomains(nl.item(i));
                        } else if (mTagRelationship.equals(((Element) nl.item(i)).getTagName())) {
                            parseDefinition(nl.item(i), Definition.TYPE_RELATIONSHIP);
                        } else if (mTagHierarchy.equals(((Element) nl.item(i)).getTagName())) {
                            parseDefinition(nl.item(i), Definition.TYPE_HIERARCHY);
                        } else if (mTagGroup.equals(((Element) nl.item(i)).getTagName())) {
                            parseDefinition(nl.item(i), Definition.TYPE_GROUP);
                        } else if (mTagCategory.equals(((Element) nl.item(i)).getTagName())) {
                            parseDefinition(nl.item(i), Definition.TYPE_CATEGORY);
                        }
                    }
                }
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
    
    
    
    /*
     * <domains>
     *   <domain>
     *     <delplyment>
     */
    public class Domains {
        ArrayList <Domain> alDomains = new ArrayList();
        ArrayList <String> alDomainNames = new ArrayList();
        
        void addDomain(Domain domain) {
            alDomains.add(domain);
            alDomainNames.add(domain.name);
        }
        
        void deleteDomain(Domain domain) {
            alDomainNames.remove(domain.name);
            alDomains.remove(domain);           
        }
        
        void deleteDomain(String name) {
            for (int i=0; i < alDomains.size(); i++) {
                Domain domain = (Domain) alDomains.get(i);
                if (domain.name.equals(name)) {
                    alDomains.remove(domain);
                    alDomainNames.remove(name);
                    break;
                }
            }
        }
        
        Domain getDomain(String name) {
            Domain domain = null;
            for (int i=0; alDomains.size() > i; i++) {
                domain = (Domain) alDomains.get(i);
                if (domain.name.equals(name)) {
                    break; 
                }
            }

            return domain;
        }
        
        ArrayList <Domain> getDomains() {
            return alDomains;
        }
        
        ArrayList <String> getDomainNames() {
            return alDomainNames;
        }

    }
    
    private Element getDatabaseToStr(Document xmlDoc) throws Exception {
        Element database = xmlDoc.createElement(this.mTagDatabase);
        database.appendChild(xmlDoc.createTextNode(getDatabase()));
        return database;
    }
    
    private Element getDateFormatToStr(Document xmlDoc) throws Exception {
        Element dateFormat = xmlDoc.createElement(this.mTagDateFormat);
        dateFormat.appendChild(xmlDoc.createTextNode(getDateFormat()));
        return dateFormat;
    }
    
    /**
     * 
     * @param datatype = one of string, date, char, boolean, int, float
     * @param columns max number of columns
     */
    private void setDataTypeMaxColumns(Map datatypeColumns) {
        for (Object datatype : datatypeColumns.keySet()) {
            String columns = (String) datatypeColumns.get(datatype);
            if (mMapDataTypes.containsKey(datatype)) {
                String oldColumns = (String) mMapDataTypes.get(datatype);
                if (Integer.valueOf(columns) > Integer.valueOf(oldColumns)) {
                    mMapDataTypes.remove(datatype);
                    mMapDataTypes.put(datatype, columns);
                }
            } else {
                mMapDataTypes.put(datatype, columns);
            }
        }
    }
    
    private String addDatatypeColumn(Map datatypeColumns, String datatype) {
        String columns = "1";
        if (datatypeColumns.containsKey(datatype)) {
            columns = (String) datatypeColumns.get(datatype);
            Integer i = Integer.valueOf(columns) + 1;
            columns = Integer.toString(i);
        }
        datatypeColumns.put(datatype, columns);
        return columns;
    }
    
    private Element getDataTypesToStr(Document xmlDoc) throws Exception {
        for (Definition definition : getAllDefinitions()) {
            Map datatypeColumns = new HashMap();
            ArrayList <Attribute> attrs = definition.getExtendedAttributes();
            for (Attribute attr : attrs) {
                String datatype = attr.getDataType();
                String columns = addDatatypeColumn(datatypeColumns, datatype);
                attr.setColumnName(datatype.toUpperCase() + "_" + columns);
            }
            setDataTypeMaxColumns(datatypeColumns);
        }
        Element domains = xmlDoc.createElement(this.mTagDataTypes);
        for (Object key : mMapDataTypes.keySet()) {
            Element datatype = xmlDoc.createElement(this.mTagDataType);
            datatype.setAttribute(mTagColumns, (String) mMapDataTypes.get(key));
            datatype.setAttribute(mTagName, (String) key);
            domains.appendChild(datatype);
        }
        
        return domains;
    }
    
    private Element getDomainsToStr(Document xmlDoc) throws Exception {
        Element domains = xmlDoc.createElement(this.mTagDomains);
        for (String domainName : getDomainNames()) {
            Element domain = xmlDoc.createElement(this.mTagDomain);
            domain.setAttribute(mTagName, domainName);
            domains.appendChild(domain);
        }
        
        return domains;
    }
    
    private void getDefinitionsToStr(Document xmlDoc) throws Exception {
        Element root = xmlDoc.getDocumentElement();
        for (Definition definition : getAllDefinitions()) {
            String type = definition.getType();
            Element def = xmlDoc.createElement(type);
            def.setAttribute(mTagName, definition.getName());
            if (type.equals(Definition.TYPE_HIERARCHY)) {
                Element domain = xmlDoc.createElement(this.mTagDomain);
                domain.setAttribute(mTagName, definition.getDomain());
                def.appendChild(domain);
                Element effectiveFrom = xmlDoc.createElement(this.mTagEffectiveFrom);
                effectiveFrom.appendChild(xmlDoc.createTextNode(definition.getEffectiveFrom()));
                def.appendChild(effectiveFrom);
                Element effectiveTo = xmlDoc.createElement(this.mTagEffectiveTo);
                effectiveTo.appendChild(xmlDoc.createTextNode(definition.getEffectiveTo()));
                def.appendChild(effectiveTo);
            } else {
                //Sourec Domain
                Element sDomain = xmlDoc.createElement(this.mTagSourceDomain);
                sDomain.setAttribute(mTagName, definition.getSourceDomain());
                def.appendChild(sDomain);
                //Target Domain
                Element tDomain = xmlDoc.createElement(this.mTagTargetDomain);
                tDomain.setAttribute(mTagName, definition.getTargetDomain());
                def.appendChild(tDomain);
            }
            //Direction
            Element direction = xmlDoc.createElement(this.mTagDirection);
            direction.appendChild(xmlDoc.createTextNode(definition.getDirection()));
            def.appendChild(direction);
            //Plugin
            Element plugin = xmlDoc.createElement(this.mTagPlugin);
            plugin.setAttribute(mTagName, definition.getPlugin());
            def.appendChild(plugin);
            //Description
            Element description = xmlDoc.createElement(this.mTagDescription);
            description.appendChild(xmlDoc.createTextNode(definition.getDescription()));
            def.appendChild(description);
            
            Element predefinedAttrs = xmlDoc.createElement(this.mTagPredefinedAttributes);
            def.appendChild(predefinedAttrs);
            getPredefinedFieldAttrs(xmlDoc, predefinedAttrs, definition.getPredefinedAttributes());

            if (definition.getExtendedAttributes() != null && definition.getExtendedAttributes().size() > 0) {
                Element extendedAttrs = xmlDoc.createElement(this.mTagExtendedAttributes);
                def.appendChild(extendedAttrs);
                getExtendedFieldAttrs(xmlDoc, extendedAttrs, definition.getExtendedAttributes());
            }
            
            root.appendChild(def);
        }
        
        return;
    }
    
    private void getPredefinedFieldAttrs(Document xmlDoc, Node elmNode, ArrayList<Attribute> attrs) {
        for (Attribute attr : attrs) {
            Element attrElm = xmlDoc.createElement(this.mTagAttribute);
            elmNode.appendChild(attrElm);
            
            Element eName = xmlDoc.createElement(mTagName);
            eName.appendChild(xmlDoc.createTextNode(attr.getName()));
            attrElm.appendChild(eName);
            
            Element eDataType = xmlDoc.createElement(mTagDataType);
            eDataType.appendChild(xmlDoc.createTextNode(attr.getDataType()));
            attrElm.appendChild(eDataType);
            
            Element eIncluded = xmlDoc.createElement(mTagIncluded);
            eIncluded.appendChild(xmlDoc.createTextNode(attr.getIncluded()));
            attrElm.appendChild(eIncluded);
            
            Element eRequired = xmlDoc.createElement(mTagRequired);
            eRequired.appendChild(xmlDoc.createTextNode(attr.getRequired()));
            attrElm.appendChild(eRequired);
        }
    }
    
    private void getExtendedFieldAttrs(Document xmlDoc, Node elmNode, ArrayList<Attribute> attrs) {
        for (Attribute attr : attrs) {
            Element attrElm = xmlDoc.createElement(this.mTagAttribute);
            elmNode.appendChild(attrElm);
            
            Element eName = xmlDoc.createElement(mTagName);
            eName.appendChild(xmlDoc.createTextNode(attr.getName()));
            attrElm.appendChild(eName);
            
            String columnName = attr.getColumnName();
            Element eColumnName = xmlDoc.createElement(mTagColumnName);
            eColumnName.appendChild(xmlDoc.createTextNode(columnName));
            attrElm.appendChild(eColumnName);
            
            Element eDataType = xmlDoc.createElement(mTagDataType);
            eDataType.appendChild(xmlDoc.createTextNode(attr.getDataType()));
            attrElm.appendChild(eDataType);
            
            Element eDefaultValue = xmlDoc.createElement(mTagDefaultValue);
            eDefaultValue.appendChild(xmlDoc.createTextNode(attr.getDefaultValue()));
            attrElm.appendChild(eDefaultValue);
            
            Element eSearchable = xmlDoc.createElement(mTagSearchable);
            eSearchable.appendChild(xmlDoc.createTextNode(attr.getSearchable()));
            attrElm.appendChild(eSearchable);
            
            Element eRequired = xmlDoc.createElement(mTagRequired);
            eRequired.appendChild(xmlDoc.createTextNode(attr.getRequired()));
            attrElm.appendChild(eRequired);
        }
    }
    
    public String writeToString() throws IOException, Exception {
        //XMLWriterUtil xmlDoc = new XMLWriterUtil();
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        xmldoc = impl.createDocument(null, mTagConfiguration, null);

        Element root = xmldoc.getDocumentElement();
        root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", "schema/MultiDomainModel.xsd");
        root.appendChild(getDatabaseToStr(xmldoc));
        root.appendChild(getDateFormatToStr(xmldoc));
        root.appendChild(getDataTypesToStr(xmldoc));
        root.appendChild(getDomainsToStr(xmldoc));
        getDefinitionsToStr(xmldoc);
        byte[] xml = transformToXML(xmldoc);
        return new String(xml);

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

    /**
     * @return String ret String
     */
    public String toString() {
        String ret = "";
        ret += "(" + mTagConfiguration + ")" + ": \n";
        ret += "(" 
               + mTagConfiguration 
               + ")" 
               + mTagDatabase 
               + ": " 
               + strDatabase 
               + ": " 
               + mDomains 
               + "\n";

        return ret;
    }
}
