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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;

/**
 * @author Kevin Kao
 * @version
 */
public class MultiDomainModel {
    private final String mTagConfiguration = "Configuration";
    private final String mTagName = "name";
    private final String mTagPlugin = "plugin";
    private final String mTagDatabase = "database";
    private final String mTagAppServer = "appserver";
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
    private final String mTagUsed = "used";
    private final String mTagStartDate = "start-date";
    private final String mTagEndDate = "end-date";
    private final String mTagAttributeID = "attributeID";
    private final String mTagColumnName = "column-name";
    private final String mTagDefaultValue = "default-value";
    private final String mTagValue = "value";
    private final String mTagDeployment  = "deployment";
    private Domains mDomains = new Domains();
    private Relationships mRelationships = new Relationships();
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
    public String getDataBase() {
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
    
    /** Add a definition to the array mAlDefinitions
     * 
     * @param definition
     */
    public void addDefinition(Definition definition) {
        mAlDefinitions.add(definition);
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
    public ArrayList <String> getDomainNames() {
        return mDomains.getDomainNames();
    }
    
    /**
     * @param node node
     */
    public Domain parseDomain(Node node) {
        Domain domain = new Domain();
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        domain.name = getAttributeName(nl.item(i));
                    }
                }
            }
        }
        return domain;
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
                        mDomains.addDomain(parseDomain(nl.item(i)));
                    }
                }
            }
        }
    }

    public String getAttributeName(Node node) {
        String val = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attrName = nnm.getNamedItem(mTagName);
            if (attrName != null) {
                try {
                    val = attrName.getNodeValue();
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
                    } else if (mTagAttributeID.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setAttributeID(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagUsed.equals(((Element) nl.item(i)).getTagName())) {
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
                        definition.setSourceDomain(getAttributeName(nl.item(i)));
                    } else if (mTagTargetDomain.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setTargetDomain(getAttributeName(nl.item(i)));
                    } else if (mTagPlugin.equals(((Element) nl.item(i)).getTagName())) {
                        definition.setPlugin(getAttributeName(nl.item(i)));
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
        }
        
        void deleteDomain(Domain domain) {
            alDomains.remove(domain);
        }
        
        void deleteDomain(String name) {
            for (int i=0; i < alDomains.size(); i++) {
                Domain domain = (Domain) alDomains.get(i);
                if (domain.name.equals(name)) {
                    alDomains.remove(domain);
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
            alDomainNames.clear();
            for (int i=0; alDomains.size() > i; i++) {
                Domain domain = (Domain) alDomains.get(i);
                alDomainNames.add(domain.name); 
            }

            return alDomainNames;
        }

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
        
        ret += "mRelationships: \n" 
               + mRelationships 
               + "\n";

        return ret;
    }
}
