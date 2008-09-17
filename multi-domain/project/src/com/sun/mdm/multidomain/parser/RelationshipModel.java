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

//import com.sun.mdm.index.parser.EIndexObject;

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
    private final String mTagRelationshipType = "relationship-type";
    private final String mTagType = "type";
    private final String mTagRelationships  = "relationships";
    private final String mTagDeployment  = "deployment";
    public static final String TYPE_RELATIONSHIP = "relationship";
    public static final String TYPE_HIERARCHY = "hierarchy";
    public static final String TYPE_GROUP = "group";
    public static final String TYPE_CATEGORY = "category";
    public static final String FIXED_ATTRIBUTE_TYPE = "0";
    public static final String EXTENDED_ATTRIBUTE_TYPE = "0";
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

    /**
     * @param String name of Relationship Type
     * @param String sourceDomain
     * @param String targetDomain
     * @return RelationshipType
     */
    public RelationshipType getRelationshipType(String name, String sourceDomain, String targetDomain) {
        return mRelationships.getRelationshipType(name, sourceDomain, targetDomain);
    }

    /**
     * @return ArrayList of RelationshipType
     */
    public ArrayList <RelationshipType> getRelationshipTypes() {
        return mRelationships.getAllRelationshipTypes();
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
    
    /**
     * @param node node
     */
    public void parseRelationshipType(Node node) {
        String name = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attrName = nnm.getNamedItem(mTagName);
            if (attrName != null) {
                try {
                    name = attrName.getNodeValue();
                } catch (DOMException ex) {
                }
            }
        }

        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        name = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagType.equals(((Element) nl.item(i)).getTagName())) {
                        String type = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }
    
    /**
     * @param node node
     */
    public void parseRelationships(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagRelationshipType.equals(((Element) nl.item(i)).getTagName())) {
                        //parsemRelationshipType(nl.item(i));
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
     * RelationshipModel.xml
     * <relationships>
     *   <relationshp-type>
     *     <attributes>
     */
    class Attribute {
        String name;
        String type;        // Fixed 0, Extended 1
        String columnName;  // which column name in the table does it bind to
        String displayName;
        boolean searchable;
        boolean isRequired;
        String defaultValue;
        String dataType; 

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setIsRequired(boolean isRequired) {
            this.isRequired = isRequired;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isIsRequired() {
            return isRequired;
        }

        public String getName() {
            return name;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public String getType() {
            return type;
        }
    }
    
    /*
     * RelationshipModel.xml
     * <relationships>
     *   <relationshp-type>
     *     <attributes>
     */
    public class RelationshipType {
        String name;
        String type;        // Relationship/Hierachy/Group/Category
        String plugin;      // Relationship plugin
        String relTypeId;   // primary key which is generated, unique across domains, used during run time Relationships
        String displayName;
        String sourceRelationshipName; //
        String targetRelationshipName;
        String sourceDomain;
        String targetDomain;
        int direction; // 1 one direction, 2 bidirectional
        boolean includeEffectiveFrom;
        boolean includeEffectiveTo;
        boolean includePurgeDate;
        boolean effectiveFromRequired;
        boolean effectiveToRequired;
        boolean purgeDateRequired;
        ArrayList <Attribute> attributes;

        public void setAttributes(ArrayList<RelationshipModel.Attribute> attributes) {
            this.attributes = attributes;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setEffectiveFromRequired(boolean effectiveFromRequired) {
            this.effectiveFromRequired = effectiveFromRequired;
        }

        public void setEffectiveToRequired(boolean effectiveToRequired) {
            this.effectiveToRequired = effectiveToRequired;
        }

        public void setIncludeEffectiveFrom(boolean includeEffectiveFrom) {
            this.includeEffectiveFrom = includeEffectiveFrom;
        }

        public void setIncludeEffectiveTo(boolean includeEffectiveTo) {
            this.includeEffectiveTo = includeEffectiveTo;
        }

        public void setIncludePurgeDate(boolean includePurgeDate) {
            this.includePurgeDate = includePurgeDate;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlugin(String plugin) {
            this.plugin = plugin;
        }

        public void setPurgeDateRequired(boolean purgeDateRequired) {
            this.purgeDateRequired = purgeDateRequired;
        }

        public void setRelTypeId(String relTypeId) {
            this.relTypeId = relTypeId;
        }

        public void setSourceRelationshipName(String sourceRelationshipName) {
            this.sourceRelationshipName = sourceRelationshipName;
        }

        public void setSourceDomain(String sourceDomain) {
            this.sourceDomain = sourceDomain;
        }

        public void setTargetDomain(String targetDomain) {
            this.targetDomain = targetDomain;
        }

        public void setTargetRelationshipName(String targetRelationshipName) {
            this.targetRelationshipName = targetRelationshipName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<RelationshipModel.Attribute> getAttributesByType(String type) {
            ArrayList al = new ArrayList();
            for (int i=0; i<attributes.size(); i++) {
                Attribute attr = (Attribute) attributes.get(i);
                if (attr.type.equals(type)) {
                    al.add(attr); 
                }
            }
            return al;

        }

        public ArrayList<RelationshipModel.Attribute> getFixedAttributes() {
            return getAttributesByType(FIXED_ATTRIBUTE_TYPE);
        }

        public ArrayList<RelationshipModel.Attribute> getExtendedAttributes() {
            return getAttributesByType(EXTENDED_ATTRIBUTE_TYPE);
        }

        public ArrayList<RelationshipModel.Attribute> getAttributes() {
            return attributes;
        }

        public int getDirection() {
            return direction;
        }

        public String getDisplayName() {
            return displayName;
        }

        public boolean isEffectiveFromRequired() {
            return effectiveFromRequired;
        }

        public boolean isEffectiveToRequired() {
            return effectiveToRequired;
        }

        public boolean isIncludeEffectiveFrom() {
            return includeEffectiveFrom;
        }

        public boolean isIncludeEffectiveTo() {
            return includeEffectiveTo;
        }

        public boolean isIncludePurgeDate() {
            return includePurgeDate;
        }

        public String getName() {
            return name;
        }

        public String getPlugin() {
            return plugin;
        }

        public boolean isPurgeDateRequired() {
            return purgeDateRequired;
        }

        public String getRelTypeId() {
            return relTypeId;
        }

        public String getSourceRelationshipName() {
            return sourceRelationshipName;
        }

        public String getSourceDomain() {
            return sourceDomain;
        }

        public String getTargetDomain() {
            return targetDomain;
        }

        public String getTargetRelationshipName() {
            return targetRelationshipName;
        }

        public String getType() {
            return type;
        }
    }
    
    /*
     * RelationshipModel.xml
     * <relationships>
     *   <relationshp-type>
     */
    public class Relationships {
        ArrayList <RelationshipType> alRelationshipTypes = new ArrayList();
        ArrayList <String> alRelationshipTypeNames = new ArrayList();
        
        void addRelationshipType(RelationshipType relationshipType) {
            alRelationshipTypes.add(relationshipType);
        }
        
        RelationshipType getRelationshipType(String name, String sourceDomain, String targetDomain) {
            RelationshipType relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.name.equals(name) &&
                    relationshipType.sourceDomain.equals(sourceDomain) &&
                    relationshipType.targetDomain.equals(targetDomain)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        void deleteRelationshipType(String name, String sourceDomain, String targetDomain) {
            RelationshipType relationshipType = getRelationshipType(name, sourceDomain, targetDomain);
            if (relationshipType != null) {
                alRelationshipTypes.remove(relationshipType);
            }
        }
        
        ArrayList <RelationshipType> getAllRelationshipTypes() {
            return alRelationshipTypes;
        }
        
        ArrayList <RelationshipType> getRelationshipsByType(String type) { // type="relationship/hierarchy/group/category
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                RelationshipType relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.type.equals(type)) {
                    al.add(relationshipType); 
                }

            }
            return al;
        }
        
        ArrayList <RelationshipType> getRelationships() {         
            return getRelationshipsByType(TYPE_RELATIONSHIP);
        }
        
        ArrayList <RelationshipType> getHierarchies() {         
            return getRelationshipsByType(TYPE_HIERARCHY);
        }
        
        ArrayList <RelationshipType> getGroups() {
            return getRelationshipsByType(TYPE_GROUP);
        }
        
        ArrayList <RelationshipType> getCategories() {
            return getRelationshipsByType(TYPE_CATEGORY);
        }
    }
    
    /*
     * 
     *<domain name="Customer">
     *	<deployment>
     *    <jndi></jndi>
     *      </deployment>
     *</domain>
    */
    public class Domain {
        String name;
        Deployment deployment = new Deployment();
        //EIndexObject eIndexObject;
        
        class Deployment {
            String jndi;
            
            public String getJndi() {
                return jndi;
            }
        }
        
        String getName() {
            return name;
        }
        
        Deployment getDeployment() {
            return deployment;
        }

        //public void setEIndexObject(EIndexObject eIndexObject) {
        //    this.eIndexObject = eIndexObject;
        //}

        //public EIndexObject getEIndexObject() {
        //    return eIndexObject;
        //}
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
