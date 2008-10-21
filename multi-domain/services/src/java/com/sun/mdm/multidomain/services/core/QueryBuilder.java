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

import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.SystemObject;

import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.configurator.ConfigurationException;

import com.sun.mdm.multidomain.services.configuration.RelationshipScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;

import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.relationship.Relationship;

import com.sun.mdm.multidomain.services.model.Field;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.model.RelationshipSearch;
import com.sun.mdm.multidomain.services.model.MultiDomainSearchOption;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;

/**
 * QueryBuilder class.
 * @author cye
 */
public class QueryBuilder {
    
    public QueryBuilder() {
    }
    
    public static MultiDomainSearchCriteria buildMultiDomainSearchCriteria(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch, RelationshipSearch relationshipSearch){
         MultiDomainSearchCriteria mdSearchCriteria = new MultiDomainSearchCriteria();
         return mdSearchCriteria;
    }
    
    public static MultiDomainSearchOptions buildMultiDomainSearchOptions(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch)
        throws ConfigException {
         MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();

         MultiDomainSearchOption mdSearchOption1 = buildMultiDomainSearchOption(sourceDomainSearch);
         MultiDomainSearchOption mdSearchOption2 = buildMultiDomainSearchOption(targetDomainSearch);
         
         /*TBD:expect MultiDomainSearchOptions to provide a method to set search options for different domains.
         mdSearchOptions.setDomainSearchOption(sourceDomainSearch.getName(),mdSearchOption1);
         mdSearchOptions.setDomainSearchOption(targetDomainSearch.getName(),mdSearchOption2);
         */         
         return mdSearchOptions;
    }
    
    public static MultiDomainSearchOption buildMultiDomainSearchOption(DomainSearch domainSearch) 
        throws ConfigException {
        MultiDomainSearchOption mdSearchOption = new MultiDomainSearchOption();
        try {
            //TBD: get from configuration API.
            RelationshipScreenConfig screenConfig = new RelationshipScreenConfig();        
            // search result page
            List<SearchResultsConfig> searchResultPages = null; //TBD:should get from screenConfig.getSearchResultsConfig();
            Iterator searchResultPageIterator = searchResultPages.iterator();      
            EPathArrayList searchResultFields = new EPathArrayList();
            String objectRef = null;
            int pageSize = 0;
            int maxElements = 0;        
            while(searchResultPageIterator.hasNext()) {
                SearchResultsConfig searchResultPage = (SearchResultsConfig)searchResultPageIterator.next();
                pageSize = searchResultPage.getPageSize();
                maxElements = searchResultPage.getMaxRecords();
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
         
            // search page
            List<SearchScreenConfig> searchPages = null; //TBD:should get from screenConfig.getSearchScreensConfig();
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
            mdSearchOption.setEPathArrayList(searchResultFields);
            mdSearchOption.setSearchId(queryBuilder);
            mdSearchOption.setIsWeighted(isWeighted);
            mdSearchOption.setPageSize(pageSize);
            mdSearchOption.setMaxElements(maxElements);        
            
        } catch(EPathException eex) {
            throw new ConfigException(eex);            
        }        
        return mdSearchOption;
    } 
 
    public static MultiDomainSearchCriteria buildMultiMultiDomainSearchCriteria(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch, RelationshipSearch relationshipSearch)
        throws ConfigException {
        
        MultiDomainSearchCriteria mdSearchSearchCriteria = new MultiDomainSearchCriteria();
        Relationship relationship = buildRelationship(relationshipSearch);
        SystemObject object1 = buildSystemObject(sourceDomainSearch)[0];
        SystemObject object2 = buildSystemObject(targetDomainSearch)[0];
        
        mdSearchSearchCriteria.setRelationship(relationship);
        //TBD: the back-end multidomain service does not support the range search yet.
        mdSearchSearchCriteria.setSystemObject(sourceDomainSearch.getName(), object1);
        mdSearchSearchCriteria.setSystemObject(targetDomainSearch.getName(), object2);
        
        return mdSearchSearchCriteria;
    } 

    public static SystemObject[] buildSystemObject(DomainSearch domainSearch)
        throws ConfigException {        
        
        Map searchCriteria = new HashMap<String, String>();
        Map searchCriteriaFrom = new HashMap<String, String>();
        Map searchCriteriaTo = new HashMap<String, String>();
            
        //TBD: get from configuration API.
        RelationshipScreenConfig screenConfig = new RelationshipScreenConfig();        
        // search page
        List<SearchScreenConfig> searchPages = null; //TBD:should get from screenConfig.getSearchScreensConfig();
        Iterator searchPageIterator = searchPages.iterator();
        SearchScreenConfig searchPage = null;
        //TDB: a simple configuration API should be provied to get the specific searchPage for the given the search type.
        while (searchPageIterator.hasNext()) {
            searchPage = (SearchScreenConfig) searchPageIterator.next();                   
            if (searchPage.getScreenTitle().equalsIgnoreCase(domainSearch.getType())) {
                break;
            }
        }               
        String objectRef = null;
        //TDB: a simple configuration API should be provied to get a list of search field config.
        //List<FieldConfig> searchFieldConfigs = searchPage.getSearchFieldConfigs();
        List<FieldConfig> searchFieldConfigs = new ArrayList<FieldConfig>();
        Iterator<FieldConfig> fieldConfigIterator = searchFieldConfigs.iterator();
        while(fieldConfigIterator.hasNext()) {
            FieldConfig fieldConfig = fieldConfigIterator.next();
            objectRef = fieldConfig.getRootObj();
            String fieldValue = null;                
            //Get the field value for each field config range type.
            if (fieldConfig.isRange()) {
                fieldValue = domainSearch.getFieldValue(fieldConfig.getDisplayName());
            } else {
                fieldValue = domainSearch.getFieldValue(fieldConfig.getFullFieldName());                    
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
                
        systemObjects[0] = ObjectBuilder.buildSystemObject(objectRef, searchCriteria);
        // The back-end multidomain service not support range yet.        
        // systemObjectFrom
        systemObjects[1] = ObjectBuilder.buildSystemObject(objectRef, searchCriteriaFrom);
        // systemObjectTo
        systemObjects[2] = ObjectBuilder.buildSystemObject(objectRef, searchCriteriaTo);
        
        return systemObjects;
    }
    
    public static Relationship buildRelationship(RelationshipSearch relationshipSearch){
        
        Relationship relationship = new Relationship();
        relationship.setEffectiveFromDate(relationshipSearch.getStartDate());
        relationship.setEffectiveToDate(relationshipSearch.getEndDate());
        relationship.setPurgeDate(relationshipSearch.getPurgeDate());        
        while(relationshipSearch.hasNext()) {
            Field field = relationshipSearch.next();
            com.sun.mdm.multidomain.association.Attribute attribute = new com.sun.mdm.multidomain.association.Attribute();
            attribute.setName(field.getName());
            relationship.setAttributeValue(attribute, field.getValue());
        }
        
        return relationship;
    }
    
    public static EOSearchOptions buildEOSearchOptions(DomainSearch domainSearch) 
        throws ConfigException {
        EOSearchOptions eoSearchOptions = null;
        try {
            //TBD: get from configuration API.
            RelationshipScreenConfig screenConfig = new RelationshipScreenConfig();        
            // search result page
            List<SearchResultsConfig> searchResultPages = null; //TBD:should get from screenConfig.getSearchResultsConfig();
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
         
            // search page
            List<SearchScreenConfig> searchPages = null; //TBD:should get from screenConfig.getSearchScreensConfig();
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
    
    public static EOSearchCriteria buildEOSearchCriteria(DomainSearch domainSearch)
        throws ConfigException {
        
        EOSearchCriteria eoSearchCriteria = new EOSearchCriteria();
        SystemObject[] systemObjects = buildSystemObject(domainSearch);
        
        eoSearchCriteria.setSystemObject(systemObjects[0]);        
        eoSearchCriteria.setSystemObject2(systemObjects[1]);
        eoSearchCriteria.setSystemObject3(systemObjects[2]);
        
        return eoSearchCriteria;
    }
    
    public static Relationship buildRelationship(RelationshipRecord relastionshipRecord){
        Relationship relationship = new Relationship(); 
        relationship.setRelationshipID(relastionshipRecord.getId());
        relationship.setSourceEUID(relastionshipRecord.getSourceEUID());
        relationship.setTargetEUID(relastionshipRecord.getTargetEUID());
        relationship.setEffectiveFromDate(relastionshipRecord.getStartDate());
        relationship.setEffectiveToDate(relastionshipRecord.getEndDate());
        relationship.setPurgeDate(relastionshipRecord.getPurgeDate());
 
        /* The back-end multidomain service does not the following attributes. why?
        relastionshipRecord.getName()
        relastionshipRecord.getSourceDomain()
        relastionshipRecord.getTargetDomain()
        */
        
        com.sun.mdm.multidomain.services.relationship.Attribute attribute1 = null;
        while(relastionshipRecord.hasNext()) {
            attribute1 = relastionshipRecord.next();
            com.sun.mdm.multidomain.association.Attribute attribute2 = new com.sun.mdm.multidomain.association.Attribute();
            attribute2.setName(attribute1.getName());
            relationship.setAttributeValue(attribute2, attribute1.getValue());
        }
        return relationship;
    } 
}
