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
public class RelationshipModel {
    private final String mTagConfiguration = "Configuration";
    private final String mTagName = "name";
    private final String mTagDatabase = "database";
    private final String mTagAppServer = "appserver";
    private final String mTagDateFormat = "dateformat";
    private final String mTagDomains = "domains";
    private final String mTagDomain = "domain";
    private final String mTagDomain1 = "domain1";
    private final String mTagDomain2 = "domain2";
    private final String mTagRelationshipType = "relationship-type";
    private final String mTagType = "type";
    private final String mTagSourceDomain = "source-domain";
    private final String mTagTargetDomain = "target-domain";
    private final String mTagRelationships  = "relationships";
    private final String mTagRelationship  = "relationship";
    private final String mTagFixedAttributes = "fixed-attributes";
    private final String mTagExtendedAttributes = "extended-attributes";
    private final String mTagAttribute = "attribute";
    private final String mTagDataType = "data-type";
    private final String mTagValue = "value";
    private final String mTagDeployment  = "deployment";
    private Domains mDomains = new Domains();
    private Relationships mRelationships = new Relationships();
    private String strDatabase;
    private String strDateFormat = "MM/dd/yyyy";
    private boolean mModified = false;


    /**
     * The main program for the RelationshipModel class
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

    public ArrayList <Relationship> getAllRelationships() {
        return mRelationships.getAllRelationships();
    }
    
    public ArrayList <Relationship> getRelationshipsByDomain(String domainName) {
        return mRelationships.getRelationshipsByDomain(domainName);
    }
    
    /**
     * @param String name of Relationship Type
     * @param String sourceDomain
     * @param String targetDomain
     * @return RelationshipType
     */
    public RelationshipType getRelationshipType(String name, String sourceDomain, String targetDomain) {
        Relationship relationshp = mRelationships.getRelationship(sourceDomain, targetDomain);
        RelationshipType relationshipType = null;
        if (relationshp != null) {
            relationshipType = relationshp.getRelationshipType(name, sourceDomain, targetDomain);
        }
        return relationshipType;
    }

    /**
     * @return ArrayList of RelationshipType
     */
    public ArrayList <RelationshipType> getRelationshipTypesByDomain(String domainName) {
        ArrayList <RelationshipType> alRelationshipTypes = new ArrayList();
        ArrayList alRelationships = mRelationships.getRelationshipsByDomain(domainName);
        for (int i=0; alRelationships != null && i < alRelationships.size(); i++) {
            Relationship relationship = (Relationship) alRelationships.get(i);
            if (relationship != null) {
                ArrayList al = relationship.getRelationshipTypesByDomain(domainName);
                if (al != null) {
                    alRelationshipTypes.addAll(al);
                }
            }
        }
        return alRelationshipTypes;
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
                        domain.name = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagDeployment.equals(((Element) nl.item(i)).getTagName())) {
                        //parseDeployment(nl.item(i));
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
                    } else if (mTagValue.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setValue(Utils.getStrElementValue(nl.item(i)));
                    } else if (mTagDataType.equals(((Element) nl.item(i)).getTagName())) {
                        attr.setDataType(Utils.getStrElementValue(nl.item(i)));
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
    public void parseRelationshipType(Node node, Relationship relationship) {
        RelationshipType relationshipType = relationship.addRelationshipType();
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

        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.name = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagType.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.type = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagSourceDomain.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.sourceDomain = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagTargetDomain.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.targetDomain = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagFixedAttributes.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.setFixedAttributes(parseAttributes(nl.item(i)));
                    } else if (mTagExtendedAttributes.equals(((Element) nl.item(i)).getTagName())) {
                        relationshipType.setExtendedAttributes(parseAttributes(nl.item(i)));
                    }
                }
            }
        }
    }
    
    /**
     * @param node node
     */
    public void parseRelationship(Node node) {
        Relationship relationship = new Relationship();
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

        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagDomain1.equals(((Element) nl.item(i)).getTagName())) {
                        relationship.setDomain1(getAttributeName(nl.item(i)));
                    } else if (mTagDomain2.equals(((Element) nl.item(i)).getTagName())) {
                        relationship.setDomain2(getAttributeName(nl.item(i)));
                    } else if (mTagRelationshipType.equals(((Element) nl.item(i)).getTagName())) {
                        parseRelationshipType(nl.item(i), relationship);
                    }
                }
            }
        }
        mRelationships.addRelationship(relationship);
    }

    /**
     * @param node node
     */
    public void parseRelationships(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagRelationship.equals(((Element) nl.item(i)).getTagName())) {
                        parseRelationship(nl.item(i));
                    }
                }
            }
        }
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
                        }
                        if (mTagDateFormat.equals(((Element) nl.item(i)).getTagName())) {
                            strDateFormat = Utils.getStrElementValue(nl.item(i));
                        }                       
                        if (mTagDomains.equals(((Element) nl.item(i)).getTagName())) {
                            parseDomains(nl.item(i));
                        }
                        if (mTagRelationships.equals(((Element) nl.item(i)).getTagName())) {
                            parseRelationships(nl.item(i));
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
