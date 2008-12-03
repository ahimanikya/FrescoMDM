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

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ResourceBundle; 
import java.util.Locale;

import java.text.SimpleDateFormat;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.multidomain.services.configuration.MDConfigManager;

import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.MultiObject.RelationshipObject;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.MultiObject.RelationshipDomain;
        
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.model.AttributeDefExt;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyDefExt;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeRecord;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeView;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyTreeView;
import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipDefView;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipsObject;
import com.sun.mdm.multidomain.services.util.Helper;
        
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;

import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.multidomain.services.configuration.FieldConfig;
import com.sun.mdm.multidomain.services.configuration.SummaryLabel;
import com.sun.mdm.multidomain.services.configuration.RelationshipScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
import com.sun.mdm.multidomain.services.configuration.DomainScreenConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;

/**
 * ViewHelper class.
 * @author cye
 */
public class ViewBuilder {
    public static final String RECORD_ID_DELIMITER = " ";
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sun.mdm.multidomain.services.resources.mdwm", Locale.getDefault());
    
    public static HierarchyTreeView buildHierarchyTreeView(HierarchyNode hNode, List<HierarchyNode> ancestors, List<HierarchyNode> children) {
        HierarchyTreeView hTreeView = new HierarchyTreeView();
        // implement me, need to convert to objectrecord based on search result.         
        return hTreeView;
    }

    public static HierarchyNodeView buildHierarchyNodeView(HierarchyNode hNode) {
        HierarchyNodeView hNodeView = new HierarchyNodeView();
        // implement me, need to convert to objectrecord based on search result.                 
        return hNodeView;
    }
    
    public static HierarchyNodeRecord buildHierarchyNodeRecord(HierarchyNode hNode) 
        throws ConfigException {
        HierarchyNodeRecord hNodeRecord = new HierarchyNodeRecord();        
        hNodeRecord.setId(Long.toString(hNode.getNodeID()));
        hNodeRecord.setEUID(hNode.getEUID());
        HierarchyNode parent = hNode.getParent();
        hNodeRecord.setParentId(parent != null ? Long.toString(parent.getNodeID()) : "");
        hNodeRecord.setParentEUID(hNode.getParentEUID());
        hNodeRecord.setStartDate(hNode.getEffectiveFromDate());
        hNodeRecord.setEndDate(hNode.getEffectiveToDate());
        hNodeRecord.setPurgeDate(hNode.getPurgeDate());
        ObjectNode objectNode = hNode.getObjectNode();    
        ObjectRecord objectRecord = buildObjectRecord(hNode.getHierarchyDef().getDomain(), hNode.getEUID(), objectNode);
        hNodeRecord.setObjectRecord(objectRecord);        
        Map<Attribute, String> extAttrs = hNode.getAttributes();
        Iterator<Attribute> keys = extAttrs.keySet().iterator();
        while(keys.hasNext()) {
            Attribute key = keys.next();
            String value = extAttrs.get(key);
            hNodeRecord.setAttributeValue(key.getName(), value);
        }
        return hNodeRecord;
    }
     
    public static HierarchyDef buildHierarchyDef(HierarchyDefExt hDefExt) {
        HierarchyDef hDef = new HierarchyDef();
        hDef.setName(hDefExt.getName());
        hDef.setId(0);
        hDef.setDomain(hDefExt.getDomain());
        //TBD need to fix core HierarchyDef
        //hDef.setPluginInfo(); 
        hDef.setDescription(hDefExt.getDescription());
        hDef.setPlugin(hDefExt.getPlugin());       
        hDef.setEffectiveFrom("true".equalsIgnoreCase(hDefExt.getStartDate()));
        hDef.setEffectiveTo("true".equalsIgnoreCase(hDefExt.getEndDate()));
        hDef.setPurgeDate("true".equalsIgnoreCase(hDefExt.getPurgeDate()));        
        hDef.setEffectiveFromRequired("true".equalsIgnoreCase(hDefExt.getStartDateRequired()) ? true : false);
        hDef.setEffectiveToRequired("true".equalsIgnoreCase(hDefExt.getEndDateRequired()) ? true : false);        
        hDef.setPurgeDateRequired("true".equalsIgnoreCase(hDefExt.getPurgeDateRequired()) ? true : false);
        
        List<AttributeDefExt> attributes = hDefExt.getExtendedAttributes();
        for (AttributeDefExt aDefExt : attributes) {
            Attribute a = new Attribute();            
            a.setName(aDefExt.getName());
            if (aDefExt.getId() != null) { 
                a.setId(Long.parseLong(aDefExt.getId()));
            }
            a.setColumnName(aDefExt.getColumnName());
            a.setType(AttributeType.valueOf(aDefExt.getDataType().toUpperCase()));
            a.setDefaultValue(aDefExt.getDefaultValue());
            a.setIsRequired("true".equalsIgnoreCase(aDefExt.getIsRequired()));
            a.setIsSearchable("true".equalsIgnoreCase(aDefExt.getSearchable()));
            hDef.setAttribute(a);
        }                
        return hDef;
    }
    
    public static HierarchyDefExt buildHierarchyDefExt(HierarchyDef hDef) {
        HierarchyDefExt hDefExt = new HierarchyDefExt();
        hDefExt.setName(hDef.getName());
        hDefExt.setId(Long.toString(hDef.getId()));
        hDefExt.setDomain(hDef.getDomain());
        //TBD need to fix core HierarchyDef
        //hDefExt.setPluginInfo(); 
        hDefExt.setDescription(hDef.getDescription());
        hDefExt.setPlugin(hDef.getPlugin());       
        hDefExt.setStartDate(hDef.getEffectiveFrom() ? "true" : "false");
        hDefExt.setEndDate(hDef.getEffectiveTo() ? "true" : "false");
        hDefExt.setPurgeDate(hDef.getPurgeDate() ? "true" : "false");        
        hDefExt.setStartDateRequired(hDef.getEffectiveFromRequired() ? "true" : "false");
        hDefExt.setEndDateRequired(hDef.getEffectiveToRequired() ? "true" : "false");
        hDefExt.setPurgeDateRequired(hDef.getPurgeDateRequired() ? "true" : "false");
        
        List<Attribute> attributes = hDef.getAttributes();
        for (Attribute a : attributes) {
            AttributeDefExt aExt = new AttributeDefExt();
            aExt.setName(a.getName());
            aExt.setColumnName(a.getColumnName());
            aExt.setSearchable(a.getIsSearchable() ? "true" : "false");
            aExt.setIsRequired(a.getIsRequired() ? "true" : "false");
            aExt.setDataType(a.getType().toString().toLowerCase());
            aExt.setDefaultValue(a.getDefaultValue());
            hDefExt.setExtendedAttribute(aExt);
        }                
        return hDefExt;
    }
    
    public static RelationshipDefExt buildRelationshipDefExt(RelationshipDef rDef) {
        RelationshipDefExt rDefExt = new RelationshipDefExt();
        rDefExt.setName(rDef.getName());
        rDefExt.setId(Long.toString(rDef.getId()));
        rDefExt.setSourceDomain(rDef.getSourceDomain());
        rDefExt.setTargetDomain(rDef.getTargetDomain());        
        rDefExt.setBiDirection(rDef.getDirection() == RelationshipDef.DirectionMode.BIDIRECTIONAL ? "true" : "false");
        //TBD need to fix core RelationshipDef
        //rDefExt.setPluginInfo();      
        rDefExt.setDescription(rDef.getDescription());
        rDefExt.setPlugin(rDef.getPlugin());       
        rDefExt.setStartDate(rDef.getEffectiveFrom() ? "true" : "false");
        rDefExt.setEndDate(rDef.getEffectiveTo() ? "true" : "false");
        rDefExt.setPurgeDate(rDef.getPurgeDate() ? "true" : "false"); 
        rDefExt.setStartDateRequired(rDef.getEffectiveFromRequired() ? "true" : "false");
        rDefExt.setEndDateRequired(rDef.getEffectiveToRequired() ? "true" : "false");
        rDefExt.setPurgeDateRequired(rDef.getPurgeDateRequired() ? "true" : "false");
        
        List<Attribute> attributes = rDef.getAttributes();
        for (Attribute a : attributes) {
            AttributeDefExt aExt = new AttributeDefExt();
            aExt.setName(a.getName());
            aExt.setColumnName(a.getColumnName());
            aExt.setSearchable(a.getIsSearchable() ? "true" : "false");
            aExt.setIsRequired(a.getIsRequired() ? "true" : "false");
            aExt.setDataType(a.getType().toString().toLowerCase());
            aExt.setDefaultValue(a.getDefaultValue());
            rDefExt.setExtendedAttribute(aExt);
        }        
        return rDefExt;
    }
    
    public static RelationshipDef buildRelationshipDef(RelationshipDefExt rDefExt) {
        RelationshipDef rDef = new RelationshipDef();        
        rDef.setName(rDefExt.getName());
        rDef.setId(0);
        rDef.setSourceDomain(rDefExt.getSourceDomain());
        rDef.setTargetDomain(rDefExt.getTargetDomain());        
        rDef.setDirection("true".equalsIgnoreCase(rDefExt.getBiDirection()) ? 
                          RelationshipDef.DirectionMode.BIDIRECTIONAL : RelationshipDef.DirectionMode.UNIDIRECTIONAL);
        //TBD need to fix core RelationshipDef
        //rDef.setPluginInfo();   
        rDef.setDescription(rDefExt.getDescription());
        rDef.setPlugin(rDefExt.getPlugin());
        rDef.setEffectiveFrom("true".equalsIgnoreCase(rDefExt.getStartDate()) ? true : false);
        rDef.setEffectiveTo("true".equalsIgnoreCase(rDefExt.getEndDate()) ? true : false);
        rDef.setPurgeDate("true".equalsIgnoreCase(rDefExt.getPurgeDate()) ? true : false);
        rDef.setEffectiveFromRequired("true".equalsIgnoreCase(rDefExt.getStartDateRequired()) ? true : false);
        rDef.setEffectiveToRequired("true".equalsIgnoreCase(rDefExt.getEndDateRequired()) ? true : false);        
        rDef.setPurgeDateRequired("true".equalsIgnoreCase(rDefExt.getPurgeDateRequired()) ? true : false);
        
        List<AttributeDefExt> attributes = rDefExt.getExtendedAttributes();
        for (AttributeDefExt aDefExt : attributes) {
            Attribute a = new Attribute();            
            a.setName(aDefExt.getName());
            if (aDefExt.getId() != null) {
                a.setId(Long.parseLong(aDefExt.getId()));
            }
            a.setColumnName(aDefExt.getColumnName());
            a.setType(AttributeType.valueOf(aDefExt.getDataType().toUpperCase()));
            a.setDefaultValue(aDefExt.getDefaultValue());
            a.setIsRequired("true".equalsIgnoreCase(aDefExt.getIsRequired()));
            a.setIsSearchable("true".equalsIgnoreCase(aDefExt.getSearchable()));
            rDef.setAttribute(a);
        }        
        return rDef;        
    }
            
    public static DomainRelationshipsObject buildRelationshipView(PageIterator<MultiObject> pages, String primaryDomain) throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance();        
        DomainRelationshipsObject domainRelationshipsObject  = new DomainRelationshipsObject();
        domainRelationshipsObject.setDomain(primaryDomain);                
    
        MultiObject multiObject =  pages.first();
        ObjectNode sourceObject = multiObject.getSourceDomainObject();
        //TBD: configManager.getRecordIdEPathFields(sourceDomain);
        EPathArrayList sourceRecordIdEPathFields = new EPathArrayList();
        //TBD: configManager.getRecordIdConfigFields(sourceDomain);
        List<FieldConfig> sourceRecordIdConfigFields = new ArrayList<FieldConfig>();            
        String sourceHighLight = buildHighLight(primaryDomain, sourceRecordIdConfigFields, sourceRecordIdEPathFields, sourceObject);
        ObjectView primaryObject = new ObjectView();
        primaryObject.setName(sourceObject.pGetTag());
        //TBD: how to get EUID of sourceObject?
        //primaryObject.setEUID((String)sourceObject.getValue("EUID"));
        primaryObject.setHighLight(sourceHighLight);
        domainRelationshipsObject.setPrimaryObject(primaryObject);
                
        List<RelationshipDomain>  relationshipDomains = multiObject.getRelationshipDomains();
        //TBD need to revist because of back-end changes
        List<RelationshipObject> relationshipObjects = relationshipDomains.get(0).getRelationshipObjects();
        for(RelationshipObject relationshipObject : relationshipObjects) {
            ObjectNode targetObject = relationshipObject.getTargetObject();
            Relationship relationship = relationshipObject.getRelationship();
            
            //TBD: configManager.getRecordIdEPathFields(targetDomain);
            EPathArrayList targetRecordIdEPathFields = new EPathArrayList();
            //TBD: configManager.getRecordIdConfigFields(targetDomain);
            List<FieldConfig> targetRecordIdConfigFields = new ArrayList<FieldConfig>();            
            String targetHighLight = buildHighLight(targetObject.pGetTag(), targetRecordIdConfigFields, targetRecordIdEPathFields, targetObject); 
            
            RelationshipDefView relationshipDef = buildRelationshipDefView(relationship);
            RelationshipView relationshipView = buildRelationshipView(relationship, sourceHighLight, targetHighLight);            
            domainRelationshipsObject.addRelationshipView(relationshipDef, relationshipView);
        }    
                    
        return domainRelationshipsObject;
    }

    public static List<RelationshipView> buildRelationshipView(PageIterator<MultiObject> pages, String sourceDomain, String targetDomain, String relationshipName) 
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance(); 
        
        DomainScreenConfig sourceDomainConfig = configManager.getDomainScreenConfig(sourceDomain);
        DomainScreenConfig targetDomainConfig = configManager.getDomainScreenConfig(targetDomain); 
        SummaryLabel sourceDomainSummaryLabel = sourceDomainConfig.getSummaryLabel();
        SummaryLabel targetDomainSummaryLabel = targetDomainConfig.getSummaryLabel();
         
        List<FieldConfig> sourceRecordIdConfigFields = sourceDomainSummaryLabel.getFieldConfigs();
        EPathArrayList sourceRecordIdEPathFields = Helper.toEPathArrayList(sourceRecordIdConfigFields);
   
        List<FieldConfig> targetRecordIdConfigFields = targetDomainSummaryLabel.getFieldConfigs();
        EPathArrayList targetRecordIdEPathFields = Helper.toEPathArrayList(targetRecordIdConfigFields);
            
        List<RelationshipView> relationships = new ArrayList<RelationshipView>();
        // base on Record-Id configuration
        while(pages.hasNext()) {
            MultiObject multiObject =  pages.next();
            ObjectNode sourceObject = multiObject.getSourceDomainObject();                       
            String sourceHighLight = buildHighLight(sourceDomain, sourceRecordIdConfigFields, sourceRecordIdEPathFields, sourceObject);
           
            //TBD: should go through each one
            List<RelationshipDomain>  relationshipDomains = multiObject.getRelationshipDomains();
            //TBD need to revist because of back-end changes
            List<RelationshipObject> relationshipObjects = relationshipDomains.get(0).getRelationshipObjects();
        
            ObjectNode targetObject =  relationshipObjects.get(0).getTargetObject();
            String targetHighLight = buildHighLight(targetDomain, targetRecordIdConfigFields, targetRecordIdEPathFields, targetObject); 
               
            Relationship relationship = relationshipObjects.get(0).getRelationship();     
            RelationshipView relationshipView =  buildRelationshipView(relationship, sourceHighLight, targetHighLight);
            relationships.add(relationshipView);
        }        
        return relationships;
    }
    
    public static String buildHighLight(String domain, List<FieldConfig> recordIdConfigFields, EPathArrayList recordIdEPathFields, ObjectNode objectNode) 
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance();                
        String highLight = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(configManager.getDateFormat());
        boolean hasSensitiveData = false;
        boolean getSensitiveField = true;
        try {
            hasSensitiveData = configManager.getObjectSensitivePlugIn() != null ? 
                               configManager.getObjectSensitivePlugIn().isDataSensitive(objectNode) : true;
        } catch (Exception ignore) {
        }
        try {
            for(int i = 0; i < recordIdEPathFields.size(); i++) {
                EPath ePath = recordIdEPathFields.get(i);
                FieldConfig fieldConfig  = recordIdConfigFields.get(i);          
                Object value = EPathAPI.getFieldValue(ePath, objectNode);                        
                if(value instanceof java.util.Date) {
                    String dateField = simpleDateFormat.format(value);               
                    if (value != null && hasSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                        highLight = highLight + RECORD_ID_DELIMITER + resourceBundle.getString("SENSITIVE_FIELD_MASKING");
                    } else {
                        highLight = highLight + RECORD_ID_DELIMITER + dateField;
                    }                               
                } else {
                    if (value != null && hasSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                        highLight = highLight + RECORD_ID_DELIMITER + resourceBundle.getString("SENSITIVE_FIELD_MASKING");
                    } else {
                        if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && value != null) {
                            value = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                            highLight = highLight + RECORD_ID_DELIMITER + value;             
                        } else if ((fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) && value != null) {                                    
                            value = fieldConfig.mask(value.toString());
                            highLight = highLight + RECORD_ID_DELIMITER + value;
                        } else if ((fieldConfig.getUserCode() != null && fieldConfig.getUserCode().length() > 0) && value != null) {                               
                            value = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                            highLight = highLight + RECORD_ID_DELIMITER + value;                                
                        } else {
                            highLight = highLight + RECORD_ID_DELIMITER + value;
                        }
                    }
                }     
            }
        } catch(EPathException eex) { 
            throw new ConfigException(eex);
        } catch(ObjectException oex) {
            throw new ConfigException(oex);
        }
        return highLight;
    }

    public static RelationshipDefView buildRelationshipDefView(Relationship relationship) {   
        RelationshipDefView relationshipDefView = new RelationshipDefView();        
        RelationshipDef type = relationship.getRelationshipDef();
        relationshipDefView.setName(type.getName());
        relationshipDefView.setId(Long.toString(type.getId()));
        relationshipDefView.setSourceDomain(type.getSourceDomain());
        relationshipDefView.setTargetDomain(type.getTargetDomain());
        relationshipDefView.setBiDirection(type.getDirection()== RelationshipDef.DirectionMode.BIDIRECTIONAL? true : false);        
        return relationshipDefView;
    }
    
    public static RelationshipView buildRelationshipView(Relationship relationship, String sourceHighLight, String targetHighLight) {   
        RelationshipView relationshipView = new RelationshipView();        
        relationshipView.setId(Long.toString(relationship.getRelationshipId()));
        relationshipView.setSourceEUID(relationship.getSourceEUID());
        relationshipView.setTargetEUID(relationship.getTargetEUID());
        relationshipView.setSourceHighLight(sourceHighLight);
        relationshipView.setTargetHighLight(targetHighLight);           
        return relationshipView;
    }
            
    public static RelationshipComposite buildRelationshipComposite(MultiObject multiObject) {
        RelationshipComposite relationshipComposite = new RelationshipComposite();
        
        ObjectNode sourceObject = multiObject.getSourceDomainObject();
         
        //TBD: should go through each one
        List<RelationshipDomain>  relationshipDomains = multiObject.getRelationshipDomains();
        //TBD need to revist because of back-end changes
        List<RelationshipObject> relationshipObjects = relationshipDomains.get(0).getRelationshipObjects();
           
        ObjectNode targetObject = relationshipObjects.get(0).getTargetObject();
        Relationship relationship = relationshipObjects.get(0).getRelationship();
        
        ObjectRecord sourceRecord = new ObjectRecord();
        relationshipComposite.setSourceRecord(sourceRecord);
        
        ObjectRecord targetRecord = new ObjectRecord();
        relationshipComposite.setTargetRecord(targetRecord);
                
        RelationshipRecord relationshipRecord = buildRelationshipRecord(relationship);
        relationshipComposite.setRelationshipRecord(relationshipRecord);
                
        return relationshipComposite;
    }
    
    public static List<ObjectRecord> buildObjectRecords(String domain, PageIterator<ObjectNode> pages, boolean isWeighted)
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance(); 
        boolean getSensitiveField = true;
        List<ObjectRecord> records = new ArrayList<ObjectRecord>();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(configManager.getDateFormat());
        DomainScreenConfig domainConfig = configManager.getDomainScreenConfig(domain);
        List<SearchResultsConfig> searchResultsConfigs = domainConfig.getSearchResultsConfigs();
          
        EPathArrayList searchResultsFieldEPaths = Helper.toSearchResultsFieldEPaths(searchResultsConfigs);
        List<FieldConfig> searchResultsConfigFields = Helper.toSearchResultsFieldConfigs(searchResultsConfigs);
        
        while(pages.hasNext()) {
            ObjectNode objectNode = pages.next();
            ObjectSensitivePlugIn plugin = configManager.getObjectSensitivePlugIn();
            boolean isSensitiveData = plugin != null ? plugin.equals(objectNode) : true;
            ObjectRecord record = new ObjectRecord();
            //Set the comparision score
            if (isWeighted) {
                //TBD: EOSearchResultRecord.getComparisonScore()
                //     EOSearchResultRecord.getEUID();
                //     EOSearchResultRecord.getObjectNode(); 
                record.setAttributeValue("Weight", "0");
            }         
            for (int i = 0; i < searchResultsFieldEPaths.size(); i++) {
                FieldConfig fieldConfig  = searchResultsConfigFields.get(i);
                EPath ePath = searchResultsFieldEPaths.get(i);
                try {
                    Object objectValue = EPathAPI.getFieldValue(ePath, objectNode);
                    String stringValue = null;
                    if(objectValue instanceof java.util.Date) {
                        String dateField = simpleDateFormat.format(objectValue);          
                        if (objectValue != null && isSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                            record.setAttributeValue(fieldConfig.getFullFieldName(), resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                        } else {
                            record.setAttributeValue(fieldConfig.getFullFieldName(), dateField);
                        }        
                    } else {
                        if (objectValue != null && isSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                            record.setAttributeValue(fieldConfig.getFullFieldName(), resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                        } else {
                            if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && objectValue != null) {
                                //value for the fields with VALUE LIST
                                stringValue = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), objectValue.toString());
                                record.setAttributeValue(fieldConfig.getFullFieldName(), stringValue);
                             } else if ((fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) && objectValue != null) {
                                //Mask the field values accordingly
                                stringValue = fieldConfig.mask(objectValue.toString());
                                record.setAttributeValue(fieldConfig.getFullFieldName(), stringValue);
                             } else if ((fieldConfig.getUserCode() != null && fieldConfig.getUserCode().length() > 0) && objectValue != null) {
                                //Get the value if the user code is present for the fields
                                stringValue = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), objectValue.toString());
                                record.setAttributeValue(fieldConfig.getFullFieldName(), stringValue);             
                             } else {
                                record.setAttributeValue(fieldConfig.getFullFieldName(), objectValue.toString());
                             }
                         }                                         
                      }
                   } catch (Exception npe) {
		          npe.printStackTrace();
                   }                
            }
            //TBD: EUID
            record.setEUID("EUID"); 
            records.add(record);
        }
        return records;
    } 
    
    public static RelationshipRecord buildRelationshipRecord(Relationship relationship) {
        RelationshipRecord relationshipRecord = new RelationshipRecord();
        
        RelationshipDef type = (RelationshipDef)relationship.getRelationshipDef();
        relationshipRecord.setId(Long.toString(relationship.getRelationshipId()));
        relationshipRecord.setSourceEUID(relationship.getSourceEUID());
        relationshipRecord.setTargetEUID(relationship.getTargetEUID());
        relationshipRecord.setName(type.getName());
        if(relationship.getEffectiveFromDate()!= null) {
            relationshipRecord.setStartDate(relationship.getEffectiveFromDate());
        }
        if(relationship.getEffectiveToDate()!= null) {
            relationshipRecord.setEndDate(relationship.getEffectiveToDate());
        }
        if(relationship.getPurgeDate()!= null) {
            relationshipRecord.setPurgeDate(relationship.getPurgeDate());
        }         
        return relationshipRecord;
    }
    
    public static ObjectRecord buildObjectRecord(String domain, String EUID, ObjectNode objectNode) 
        throws ConfigException {
        MDConfigManager configManager =  MDConfigManager.getInstance();        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(configManager.getDateFormat());
        boolean hasSensitiveData = false;
        boolean getSensitiveField = true;
        try {
            hasSensitiveData = configManager.getObjectSensitivePlugIn() != null ? 
                               configManager.getObjectSensitivePlugIn().isDataSensitive(objectNode) : true;
        } catch (Exception ignore) {
        }
        DomainScreenConfig domainConfig = configManager.getDomainScreenConfig(domain);
        List<SearchResultsConfig> searchResultsConfigs = domainConfig.getSearchResultsConfigs();
          
        EPathArrayList searchResultsFieldEPaths = Helper.toSearchResultsFieldEPaths(searchResultsConfigs);
        List<FieldConfig> searchResultsConfigFields = Helper.toSearchResultsFieldConfigs(searchResultsConfigs);
              
        ObjectRecord objectRecord = new ObjectRecord();
        objectRecord.setEUID(EUID);
        objectRecord.setName(objectNode.pGetTag());
        
        for (int i = 0; i < searchResultsFieldEPaths.size(); i++) {
            FieldConfig fieldConfig  = searchResultsConfigFields.get(i);
            EPath ePath = searchResultsFieldEPaths.get(i);
            try {
                Object objectValue = EPathAPI.getFieldValue(ePath, objectNode);
                String stringValue = null;
                com.sun.mdm.multidomain.services.model.Attribute attribute = 
                            new com.sun.mdm.multidomain.services.model.Attribute(); 
                if(objectValue instanceof java.util.Date) {
                    String dateField = simpleDateFormat.format(objectValue);          
                    if (objectValue != null && hasSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                        attribute.setName(fieldConfig.getFullFieldName());
                        attribute.setValue(resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                    } else {
                        attribute.setName(fieldConfig.getFullFieldName());
                        attribute.setValue(dateField);
                    }        
                } else {
                    if (objectValue != null && hasSensitiveData && fieldConfig.isSensitive() && !getSensitiveField) { 
                        attribute.setName(fieldConfig.getFullFieldName());
                        attribute.setValue(resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                    } else {
                        if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && objectValue != null) {
                            //value for the fields with VALUE LIST
                            stringValue = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), objectValue.toString());
                            attribute.setName(fieldConfig.getFullFieldName());
                            attribute.setValue(stringValue);
                        } else if ((fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) && objectValue != null) {
                            //Mask the field values accordingly
                            stringValue = fieldConfig.mask(objectValue.toString());
                            attribute.setName(fieldConfig.getFullFieldName());
                            attribute.setValue(stringValue);
                        } else if ((fieldConfig.getUserCode() != null && fieldConfig.getUserCode().length() > 0) && objectValue != null) {
                            //Get the value if the user code is present for the fields
                            stringValue = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), objectValue.toString());
                            attribute.setName(fieldConfig.getFullFieldName());
                            attribute.setValue(stringValue);             
                        } else {
                            attribute.setName(fieldConfig.getFullFieldName());
                            attribute.setValue(objectValue.toString());
                        }
                     }                                         
                 }
                 objectRecord.add(attribute);
            } catch (EPathException eex) {
                throw new ConfigException(eex);
            } catch(ObjectException oex) {
               throw new ConfigException(oex); 
            }
       }
       return objectRecord;
    }
}
