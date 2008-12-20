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
package com.sun.mdm.multidomain.services.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.configurator.ConfigurationException;

import com.sun.mdm.multidomain.services.configuration.RelationshipScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
import com.sun.mdm.multidomain.services.configuration.DomainScreenConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfigGroup;
import com.sun.mdm.multidomain.services.configuration.FieldConfig;
import com.sun.mdm.multidomain.services.configuration.ScreenObject;
        
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions.DomainSearchOption;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.query.HierarchySearchCriteria;
        
import com.sun.mdm.multidomain.services.model.Attribute;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;
import com.sun.mdm.multidomain.services.control.HierarchyManager;
import com.sun.mdm.multidomain.services.util.Localizer;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeRecord;
import com.sun.mdm.multidomain.services.hierarchy.HierarchySearch;
import com.sun.mdm.multidomain.services.model.ObjectRecord;

/**
 * QueryBuilder class.
 * @author cye
 */
public class QueryBuilder {
    private static final String EUID_NAME = "EUID";
    private static final String SYSTEM_CODE_NAME = "SystemCode";
    private static final String LOCAL_ID_NAME = "LID";
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.core.QueryBuilder");
    private static Localizer localizer = Localizer.getInstance();
    private static HierarchyManager hierarchyManager;
    
    public QueryBuilder() throws ServiceException { 
        hierarchyManager = ServiceManagerFactory.Instance().createHierarchyManager();
    }
    /**
     * Build MultiDomainSearchOptions.
     * @param sourceDomainSearch SourceDomainSearch
     * @param targetDomainSearch TargetDomainSearch
     * @return MultiDomainSearchOptions.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static MultiDomainSearchOptions buildMultiDomainSearchOptions(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch)
        throws ConfigException {        
         MDConfigManager configManager =  MDConfigManager.getInstance();        
         MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();

         DomainSearchOption mdSearchOption1 = buildMultiDomainSearchOption(sourceDomainSearch);
         DomainSearchOption mdSearchOption2 = buildMultiDomainSearchOption(targetDomainSearch);
         
         ScreenObject manageRelationshipScreen = configManager.getScreenByName("manage/manage_relationship");
         mdSearchOptions.setPrimaryDomain(sourceDomainSearch.getName());
         mdSearchOptions.setPageSize(manageRelationshipScreen.getPageSize());
         mdSearchOptions.setMaxElements(manageRelationshipScreen.getMaxRecords());
         mdSearchOptions.setOptions(sourceDomainSearch.getName(), mdSearchOption1);
         mdSearchOptions.setOptions(targetDomainSearch.getName(), mdSearchOption2);
             
         return mdSearchOptions;
    }
    
    /**
     * Build DomainSearchOption.
     * @param domainSearch DomainSearch.
     * @return DomainSearchOption.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static DomainSearchOption buildMultiDomainSearchOption(DomainSearch domainSearch) 
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance(); 
        DomainScreenConfig domainConfig = configManager.getDomainScreenConfig(domainSearch.getName());      
        DomainSearchOption mdSearchOption = new DomainSearchOption();
        
        try {
            
            String EUID = domainSearch.getAttributeValue(EUID_NAME);
            String localId = domainSearch.getAttributeValue(LOCAL_ID_NAME);
            String systemCode = domainSearch.getAttributeValue(SYSTEM_CODE_NAME);    
            if (EUID != null) {
                // TBD: do EUID search. how to pass it back-end.
            } else if (systemCode != null && localId != null) {
                // TBD: systemcode and localId search. how to pass it back-end.
            } else if (systemCode == null && localId != null) {
                new ConfigException(localizer.t("QRY001: system code for simple search is not defined for localId {0}", localId));
            } else if (systemCode != null && localId == null) {
                new ConfigException(localizer.t("QRY002: local Id for simple search is not defined for system code {0}", systemCode));
            } 
                    
            // search result page based record-id
            List<FieldConfig> fieldConfigs = domainConfig.getSummaryLabel().getFieldConfigs();
            EPathArrayList resultFields = new EPathArrayList();
           
            for(FieldConfig fieldConfig : fieldConfigs) {
                String fieldEPath = fieldConfig.toEpathStyleString(fieldConfig. getFullFieldName());
                resultFields.add(fieldEPath);
            }
            
            // search page
            List<SearchScreenConfig> searchPages = domainConfig.getSearchScreenConfigs();
            Iterator searchPageIterator = searchPages.iterator();
            String queryBuilder = null;
            boolean isWeighted = false;
            for (SearchScreenConfig searchPage : searchPages) {
                if (searchPage.getScreenTitle().equalsIgnoreCase(domainSearch.getType())) {
                    queryBuilder = searchPage.getOptions().getQueryBuilder();
                    if (searchPage.getOptions().getIsWeighted()) {
                        isWeighted = true;
                    }
                    break;
                }             
            }
             
            mdSearchOption.setDomain(domainSearch.getName());
            mdSearchOption.setOptions(resultFields);
            mdSearchOption.setSearchId(queryBuilder);
            mdSearchOption.setIsWeighted(isWeighted);   
            
        } catch(EPathException eex) {
            throw new ConfigException(eex);            
        }        
        return mdSearchOption;
    } 
 
    /**
     * Build MultiDomainSearchCriteria.
     * @param sourceDomainSearch SourceDomainSearch.
     * @param targetDomainSearch TargetDomainSearch.
     * @param relationshipSearch RelationshipSearch.
     * @return MultiDomainSearchCriteria.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static MultiDomainSearchCriteria buildMultiDomainSearchCriteria(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch, RelationshipSearch relationshipSearch)
        throws ConfigException {        
        MDConfigManager configManager =  MDConfigManager.getInstance();                
        MultiDomainSearchCriteria mdSearchSearchCriteria = new MultiDomainSearchCriteria();
        Relationship relationship = buildRelationship(relationshipSearch);
        EOSearchCriteria sourceEOSearchCriteria = buildEOSearchCriteria(sourceDomainSearch);
        EOSearchCriteria targetEOSearchCriteria = buildEOSearchCriteria(targetDomainSearch);
        
        mdSearchSearchCriteria.setRelationship(relationship);
        mdSearchSearchCriteria.setDomainSearchCriteria(sourceDomainSearch.getName(), sourceEOSearchCriteria);
        mdSearchSearchCriteria.setDomainSearchCriteria(targetDomainSearch.getName(), targetEOSearchCriteria);
    
        return mdSearchSearchCriteria;
    } 

    /**
     * Build system objects for the search.
     * @param domainSearch DomainSearch.
     * @return SystemObject[] Range SystemObject.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static SystemObject[] buildSystemObjects(DomainSearch domainSearch)
        throws ConfigException {                
        MDConfigManager configManager =  MDConfigManager.getInstance(); 
        DomainScreenConfig domainConfig = configManager.getDomainScreenConfig(domainSearch.getName());
        
        Map searchCriteria = new HashMap<String, String>();
        Map searchCriteriaFrom = new HashMap<String, String>();
        Map searchCriteriaTo = new HashMap<String, String>();
            
        // search screen cofnig
        List<SearchScreenConfig> searchPages = domainConfig.getSearchScreenConfigs();
        Iterator searchPageIterator = searchPages.iterator();
        SearchScreenConfig searchPage = null;
        //TDB: a simple configuration API should be provied to get the specific searchPage for the given the search type.
        while (searchPageIterator.hasNext()) {
            searchPage = (SearchScreenConfig) searchPageIterator.next();                   
            if (searchPage.getScreenTitle().equalsIgnoreCase(domainSearch.getType())) {
                break;
            }
        }             
        
        //TDB: a simple configuration API should be provied to get a list of search field config.
        List<FieldConfigGroup> searchFieldConfigGroups = searchPage.getFieldConfigGroups();
        List<FieldConfig> searchFieldConfigs = new ArrayList<FieldConfig>();
        for (FieldConfigGroup fieldConfigGorup : searchFieldConfigGroups) {
            List<FieldConfig> fieldConfigs = fieldConfigGorup.getFieldConfigs();
            searchFieldConfigs.addAll(fieldConfigs);
        }        

        String objectRef = null;
        for (FieldConfig fieldConfig : searchFieldConfigs) {
            objectRef = fieldConfig.getRootObj();
            String fieldValue = null;                
            //Get the field value for each field config range type.
            if (fieldConfig.isRange()) {
                fieldValue = domainSearch.getAttributeValue(fieldConfig.getDisplayName());
            } else {
                fieldValue = domainSearch.getAttributeValue(fieldConfig.getFullFieldName());                    
            }                
            if (fieldValue != null && fieldValue.trim().length() > 0) {
                //Remove all masking fields from the field valued if any like SSN,LID...etc
                fieldValue = fieldConfig.demask(fieldValue);                        
                if (fieldConfig.isRange() && fieldConfig.getDisplayName().endsWith("From")) {
                    searchCriteriaFrom.put(fieldConfig.getFullFieldName(), fieldValue);
                } else if (fieldConfig.isRange() && fieldConfig.getDisplayName().endsWith("To")) {
                    searchCriteriaTo.put(fieldConfig.getFullFieldName(), fieldValue);
                } else {
                    searchCriteria.put(fieldConfig.getFullFieldName(), fieldValue);
                }
            }                         
        }
         
        SystemObject[] systemObjects = new SystemObject[3];             
        systemObjects[0] = buildSystemObject(objectRef, searchCriteria);   
        // systemObjectFrom
        if (!searchCriteriaFrom.isEmpty()) {
            systemObjects[1] = buildSystemObject(objectRef, searchCriteriaFrom);
        }
        // systemObjectTo
        if (!searchCriteriaTo.isEmpty()) {
            systemObjects[2] = buildSystemObject(objectRef, searchCriteriaTo);
        }     
        return systemObjects;
    }
    
    /**
     * Build Relationship for the given relationshipSearch.
     * @param relationshipSearch RelationshipSearch.
     * @return Relationship Relationship.
     */
    public static Relationship buildRelationship(RelationshipSearch relationshipSearch)
        throws ConfigException {        
        SimpleDateFormat dateFormat = new SimpleDateFormat(MDConfigManager.getInstance().getDateFormatForMultiDomain());
        Relationship relationship = new Relationship();        
        try {
            if (relationshipSearch.getStartDate() != null) {
                relationship.setEffectiveFromDate(dateFormat.parse(relationshipSearch.getStartDate()));
            }
            if (relationshipSearch.getEndDate() != null) {
                relationship.setEffectiveToDate(dateFormat.parse(relationshipSearch.getEndDate()));
            }
            if (relationshipSearch.getPurgeDate() != null) {
                relationship.setPurgeDate(dateFormat.parse(relationshipSearch.getPurgeDate()));   
            }        
            while(relationshipSearch.hasNext()) {
                Attribute field = relationshipSearch.next();
                com.sun.mdm.multidomain.attributes.Attribute attribute = new com.sun.mdm.multidomain.attributes.Attribute();
                attribute.setName(field.getName());
                relationship.setAttributeValue(attribute, field.getValue());
            }    
        } catch (ParseException pex) {            
            throw new ConfigException(pex);
        }
        return relationship;
    }
    
    /**
     * Build EOSearchOptions.
     * @param domainSearch DomainSearch.
     * @return EOSearchOptions.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static EOSearchOptions buildEOSearchOptions(DomainSearch domainSearch) 
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance();                
        EOSearchOptions eoSearchOptions = null;
        try {
            DomainScreenConfig domainConfig = configManager.getDomainScreenConfig(domainSearch.getName());  
            // build EOSearchOptions using SearchResultsConfig
            List<SearchResultsConfig> searchResultPages = domainConfig.getSearchResultsConfigs();
            Iterator searchResultPageIterator = searchResultPages.iterator();      
            EPathArrayList searchResultFields = new EPathArrayList();
            String objectRef = null;
            int pageSize = 0;
            int maxElements = 0;        
            while(searchResultPageIterator.hasNext()) {
                SearchResultsConfig searchResultPage = (SearchResultsConfig)searchResultPageIterator.next();
                List fieldEpaths = searchResultPage.getEPaths();
                Iterator fieldEpathsIterator = fieldEpaths.iterator();
                while(fieldEpathsIterator.hasNext()) {
                    String fieldEPath = (String) fieldEpathsIterator.next();
                    searchResultFields.add("Enterprise.SystemSBR." + fieldEPath);
                    if (objectRef == null) {
                        objectRef = fieldEPath.substring(0, fieldEPath.indexOf("."));
                    }      
                }
            }   
            searchResultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");
         
            // search screen config 
            List<SearchScreenConfig> searchPages = domainConfig.getSearchScreenConfigs();
            Iterator searchPageIterator = searchPages.iterator();
            String queryBuilder = null;
            boolean isWeighted = false;
            SearchScreenConfig searchPage = null;
            //TDB: a simple API should be provied to get the specific searchPage for the given the search type.
            while (searchPageIterator.hasNext()) {
                searchPage = (SearchScreenConfig) searchPageIterator.next();                   
                if (searchPage.getScreenTitle().equalsIgnoreCase(domainSearch.getType())) {
                    queryBuilder = searchPage.getOptions().getQueryBuilder();
                    if (searchPage.getOptions().getIsWeighted()) {
                        isWeighted = true;
                    }
                    break;
                }
            }                           
            eoSearchOptions = new EOSearchOptions(queryBuilder, searchResultFields);
            eoSearchOptions.setWeighted(isWeighted);
        } catch (EPathException eex) {
            throw new ConfigException(eex);
        } catch (ConfigurationException cex) {
            throw new ConfigException(cex);
        }
        return eoSearchOptions;
    }
    
    /**
     * Build EOSearchCriteria.
     * @param domainSearch
     * @return EOSearchCriteria.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static EOSearchCriteria buildEOSearchCriteria(DomainSearch domainSearch)
        throws ConfigException {
        EOSearchCriteria eoSearchCriteria = new EOSearchCriteria();
        SystemObject[] systemObjects = buildSystemObjects(domainSearch);
        
        /* range search supported */
        eoSearchCriteria.setSystemObject(systemObjects[0]);        
        eoSearchCriteria.setSystemObject2(systemObjects[1]);
        eoSearchCriteria.setSystemObject3(systemObjects[2]);
        
        return eoSearchCriteria;
    }
    
    /**
     * Build relationship for the given relastionshipRecord.
     * @param relastionshipRecord.
     * @return relastionship.
     */
    public static Relationship buildRelationship(RelationshipRecord relastionshipRecord)
        throws ConfigException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(MDConfigManager.getInstance().getDateFormatForMultiDomain());
        Relationship relationship = new Relationship();     
        
        try {
            relationship.setRelationshipId(Integer.parseInt(relastionshipRecord.getId()));
            relationship.setSourceEUID(relastionshipRecord.getSourceEUID());
            relationship.setTargetEUID(relastionshipRecord.getTargetEUID());
            if (relastionshipRecord.getStartDate() != null) {
                relationship.setEffectiveFromDate(dateFormat.parse(relastionshipRecord.getStartDate()));
            }
            if (relastionshipRecord.getEndDate() != null) {
                relationship.setEffectiveToDate(dateFormat.parse(relastionshipRecord.getEndDate()));
            }
            if (relastionshipRecord.getPurgeDate() != null) {
                relationship.setPurgeDate(dateFormat.parse(relastionshipRecord.getPurgeDate()));
            }
            relationship.getRelationshipDef().setName(relastionshipRecord.getName());
            relationship.getRelationshipDef().setSourceDomain(relastionshipRecord.getSourceDomain());
            relationship.getRelationshipDef().setTargetDomain(relastionshipRecord.getTargetDomain());
        
            com.sun.mdm.multidomain.services.model.Attribute attribute1 = null;
            while(relastionshipRecord.hasNext()) {
                attribute1 = relastionshipRecord.next();
                com.sun.mdm.multidomain.attributes.Attribute attribute2 = new com.sun.mdm.multidomain.attributes.Attribute();
                attribute2.setName(attribute1.getName());
                relationship.setAttributeValue(attribute2, attribute1.getValue());
            }
        } catch(ParseException pex) {            
            throw new ConfigException(pex);
        }
        return relationship;
    } 
    
    public static SystemObject buildSystemObject(String objectName, Map<String, String> searchCriteria) 
        throws ConfigException {	
	SystemObject so = null;
	try {
            ObjectFactory objectFactory = ObjectFactoryRegistry.lookup(objectName);
            ObjectNode topNode = objectFactory.create(objectName);
            
            Iterator<String> keys = searchCriteria.keySet().iterator();        
            while (keys.hasNext()) {
                String key = (String) keys.next();
		String value = (String) searchCriteria.get(key);
		if (value != null && value.trim().length() > 0) {
                    
                    int index = key.indexOf(".");
                    if (index > -1) {                      
                        String nodeName = key.substring(0, index);
			String fieldName = key.substring(index + 1);
                        
                        index = fieldName.indexOf(".");
                        if (index > -1) {
                            nodeName = fieldName.substring(0, index);
                            fieldName = fieldName.substring(index + 1);         
                        }
                        
			if (nodeName.equalsIgnoreCase(objectName)) {
                            setObjectNodeFieldValue(topNode, fieldName, value);
			} else {
                            ArrayList<ObjectNode> children = topNode.pGetChildren(nodeName);
                            ObjectNode childNode = null;
                            if (children == null) {
                                childNode = objectFactory.create(nodeName);
				topNode.addChild(childNode);
                            } else {
                                childNode = (ObjectNode) children.get(0);
                            }                        
                            setObjectNodeFieldValue(childNode, fieldName, value);
                        }
                    }
                 }            
            }        
            so = new SystemObject();
            so.setValue("ChildType", objectName);
            so.setObject(topNode);        
	} catch(ObjectException oe) {
            throw new ConfigException(oe);
	}
        return so;		
    }   

    public static ObjectNode buildObjectNode(String objectName, Map<String, String> searchCriteria) 
        throws ConfigException {		
	ObjectNode objectNode = null;
	try {
            ObjectFactory objectFactory = ObjectFactoryRegistry.lookup(objectName);
            objectNode = objectFactory.create(objectName);
            Iterator<String> keys = searchCriteria.keySet().iterator();        
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String) searchCriteria.get(key);
        
                if ((value != null) && (value.trim().length() > 0)) {
                    int index = key.indexOf(".");
                    if (index > -1) {
                        String tmpRef = key.substring(0, index);
			String fieldName = key.substring(index + 1);
			if (tmpRef.equalsIgnoreCase(objectName)) {
                            setObjectNodeFieldValue(objectNode, fieldName, value);
			} else {
                            ArrayList<ObjectNode> childNodes = objectNode.pGetChildren(tmpRef);
                            ObjectNode node = null;
                            if (childNodes == null) {
                                node = objectFactory.create(tmpRef);
				objectNode.addChild(node);
                            } else {
                                node = (ObjectNode) childNodes.get(0);
                            }                        
                            setObjectNodeFieldValue(node, fieldName, value);
                        }
                    }
                 }            
            }        
	} catch(ObjectException oe) {
            throw new ConfigException(oe);
	}
        return objectNode;
    }
	
    public static void setObjectNodeFieldValue(ObjectNode node, String field, String value)
        throws ObjectException, ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance();                
        if (value == null) {
            if (node.isNullable(field)) {
                node.setValue(field, null);
            	return;
            } else {
                //ObjectNodeConfig config = configManager.getInstance().getObjectNodeConfig(node.pGetType());
                //String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
                throw new ObjectException("Field [" + field + "] is required");
            }
        }
        int type = node.pGetType(field);
        try {
            switch (type) {
            case ObjectField.OBJECTMETA_DATE_TYPE:
            case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
                node.setValue(field, (Object) new SimpleDateFormat("mm/dd/yyyy").parse(value));
            	break;
            case ObjectField.OBJECTMETA_INT_TYPE:
            	node.setValue(field, (Object) Integer.valueOf(value));
            	break;
            case ObjectField.OBJECTMETA_BOOL_TYPE:
            	node.setValue(field, (Object) Boolean.valueOf(value));
            	break;
            case ObjectField.OBJECTMETA_BYTE_TYPE:
            	node.setValue(field, (Object) Byte.valueOf(value));
            	break;
            case ObjectField.OBJECTMETA_CHAR_TYPE:
            	node.setValue(field, (Object) new Character(value.charAt(0)));
            	break;
            case ObjectField.OBJECTMETA_LONG_TYPE:
            	node.setValue(field, (Object) Long.valueOf(value));
            	break;
            case ObjectField.OBJECTMETA_FLOAT_TYPE:
            	node.setValue(field, (Object) Float.valueOf(value));
            	break;
            case ObjectField.OBJECTMETA_STRING_TYPE:
            default:
            	node.setValue(field, (Object) value);
		break;
            }
        } catch (ParseException pex) {
            throw new ConfigException(pex);
        } catch (NumberFormatException nex) {
            //ObjectNodeConfig config = configManager.getInstance().getObjectNodeConfig(node.pGetType());
            //String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
            throw new ObjectException("Invalid value [" + value + "] for field [" + field + "]"); 
        }
   }
    
   /**
    * Build HierarchyNode for the given HierarchyNodeRecord.  Only the immediate children
    * will be instantiated.
    * @param hNodeRecord HierarchyNodeRecord.
    * @return HierarchyNode HierarchyNode.
    * @throws ServiceException if an error occurs during processing.
    */ 
    public static HierarchyNode buildHierarchyNode(HierarchyNodeRecord hNodeRecord) 
            throws ServiceException{
        return (buildHierarchyNode(hNodeRecord, true));
        
    }
   /**
    * Build HierarchyNode for the given HierarchyNodeRecord.
    * @param hNodeRecord HierarchyNodeRecord.
    * @param instantiateChildren  Set to true if immediate children are to be instantiated,
    * false otherwise.
    * @return HierarchyNode HierarchyNode.
    * @throws ServiceException if an error occurs during processing.
    */ 
    public static HierarchyNode buildHierarchyNode(HierarchyNodeRecord hNodeRecord, 
                                                   boolean instantiateChildren) 
            throws ServiceException{
        
        if (hNodeRecord == null) {
            throw new ServiceException(localizer.t("QRY503: The hNodeRecord parameter cannot be null"));
        }
  
        HierarchyNode hNode = new HierarchyNode();
        int nodeID = -1;
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MDConfigManager.getInstance().getDateFormatForMultiDomain());        
            String id = hNodeRecord.getId();
            if (id != null) {
                nodeID = Integer.parseInt(id);
            }
            hNode.setEUID(hNodeRecord.getEUID());
            hNode.setParentEUID(hNodeRecord.getParentEUID());
            hNode.setEffectiveFromDate(dateFormat.parse(hNodeRecord.getStartDate()));
            hNode.setEffectiveToDate(dateFormat.parse(hNodeRecord.getEndDate()));
            hNode.setPurgeDate(dateFormat.parse(hNodeRecord.getPurgeDate()));
            } catch (ConfigException cex) {
                throw new ServiceException(cex);            
            } catch(ParseException pex) {
                throw new ServiceException(pex);
            }
        
        List<Attribute> attributeList = hNodeRecord.getAttributes();
        for (Attribute attr : attributeList) {
            String name = attr.getName();
            String value = attr.getValue();
            hNode.setAttributeValue(name, value);
        }

        HierarchyDef hierarchyDef = new HierarchyDef();
        hierarchyDef.setId(nodeID);
        hNode.setHierarchyDef(hierarchyDef);
        
        ObjectRecord objectRecord = hNodeRecord.getObjectRecord();
        String objectName = objectRecord.getName();

        List<Attribute> objectRecordAttributeList = objectRecord.getAttributes();
        HashMap<String, String> searchCriteria = new HashMap<String, String>();
        for (Attribute attr : objectRecordAttributeList) {
            String attrName = attr.getName();
            String value = attr.getValue();
            searchCriteria.put(attrName, value);
        }
        
        try {
            ObjectNode objectNode = buildObjectNode(objectName, searchCriteria);
            hNode.setObjectNode(objectNode);
        } catch (ConfigException e) {
            throw new ServiceException(e);
        }
        
        // The HierarchyNode for the parent is not needed.  It should be handled
        // by the backend APIs.

        // We don't necessarily need to instantiate all children--just
        // the immediate children.
        if (instantiateChildren == true) {
            ArrayList<HierarchyNode> children = new ArrayList();
            List<HierarchyNodeRecord> hNodeRecordChildren = hierarchyManager.getHierarchyNodeChildren(nodeID);
            for (HierarchyNodeRecord child : hNodeRecordChildren) {
                HierarchyNode hierarchyNode = buildHierarchyNode(child, false);
                children.add(hierarchyNode);
            }
            hNode.setChildren(children);
        }

        return hNode;
   } 
   
   /**  Build HierarchySearchCriteria instance for the given HierarchySearch instance.
    * 
    * @param hNodeSearch HierarchySearch instance.
    * @return HierarchyNode HierarchySearchCriteria.
    * @throws ServiceException if an error occurs during processing.
    */ 
   public static HierarchySearchCriteria buildHierarchySearchCriteria(HierarchySearch hNodeSearch) 
          throws ServiceException{
        
        if (hNodeSearch == null) {
            throw new ServiceException(localizer.t("QRY504: The hNodeSearch parameter cannot be null"));
        }            
        HierarchySearchCriteria hSearchCriteria = new HierarchySearchCriteria();
        HierarchyNode hNode = new HierarchyNode();
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MDConfigManager.getInstance().getDateFormatForMultiDomain());

            hNode.setEffectiveFromDate(dateFormat.parse(hNodeSearch.getStartDate()));
            hNode.setEffectiveToDate(dateFormat.parse(hNodeSearch.getEndDate()));
            hNode.setPurgeDate(dateFormat.parse(hNodeSearch.getPurgeDate()));    
        } catch (ConfigException cex) {
            throw new ServiceException(cex);            
        } catch(ParseException pex) {
            throw new ServiceException(pex);
        }
        
        hSearchCriteria.setHierarchyNode(hNode);
        
        List<Attribute> attributeList = hNodeSearch.getAttributes();
        HashMap<String, String> searchCriteria = new HashMap<String, String>();
        for (Attribute attr : attributeList) {
            String attrName = attr.getName();
            String value = attr.getValue();
            searchCriteria.put(attrName, value);
        }
        String name = hNodeSearch.getName();    
        
        try {
            SystemObject systemObject = buildSystemObject(name, searchCriteria);
            hSearchCriteria.setSystemObject(systemObject);
        } catch (ConfigException e) {
            throw new ServiceException(e);
        }
        
        return hSearchCriteria;
    }
}
