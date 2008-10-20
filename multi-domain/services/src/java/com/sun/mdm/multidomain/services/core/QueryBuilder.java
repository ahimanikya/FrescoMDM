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
import java.util.Iterator;

import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;

import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.configurator.ConfigurationException;

import com.sun.mdm.multidomain.services.configuration.RelationshipScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
  
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.relationship.Relationship;

import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.model.RelationshipSearch;
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
      
    public static MultiDomainSearchOptions buildMultiDomainSearchOptions(DomainSearch sourceDomainSearch, DomainSearch targetDomainSearch){
         MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();
         return mdSearchOptions;
    }
    
    public static MultiDomainSearchOptions buildMultiDomainSearchOptions(DomainSearch domainSearch){
        MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();
         try {
        RelationshipScreenConfig screenConfig = new RelationshipScreenConfig();
        
        // search result page
        List<SearchResultsConfig> searchResultPages = null; //screenConfig.getSearchResultsConfig();
        Iterator searchResultPageIterator = searchResultPages.iterator();      
        EPathArrayList searchResultFields = new EPathArrayList();
        String objectReference = null;
        while(searchResultPageIterator.hasNext()) {
            SearchResultsConfig searchResultPage = (SearchResultsConfig)searchResultPageIterator.next();
            List fieldEpaths = searchResultPage.getEPaths();
            Iterator fieldEpathsIterator = fieldEpaths.iterator();
            while(fieldEpathsIterator.hasNext()) {
                String fieldEPath = (String) fieldEpathsIterator.next();
                searchResultFields.add("Enterprise.SystemSBR." + fieldEPath);
                if (objectReference == null) {
                    objectReference = fieldEPath.substring(0, fieldEPath.indexOf("."));
                }      
            }
        }   
        searchResultFields.add("Enterprise.SystemSBR." + objectReference + ".EUID");
         
        // search page
        List<SearchScreenConfig> searchPages = null; //screenConfig.getSearchScreensConfig();
        Iterator searchPageIterator = searchPages.iterator();
        String queryBuilder = null;
        boolean weighted = false;
        SearchScreenConfig searchPage = null;
        while (searchPageIterator.hasNext()) {
            searchPage = (SearchScreenConfig) searchPageIterator.next();                   
            if (searchPage.getScreenTitle().equalsIgnoreCase(domainSearch.getType())) {
                queryBuilder = searchPage.getOptions().getQueryBuilder();
                if (searchPage.getOptions().getIsWeighted()) {
                    weighted = true;
                }
                break;
            }
        }
        /*
        MultiDomainSearchOptions searchOptions = buildSearchOptions(queryBuilder,searchResultFields);
                     
        MultiDomainSearchCriteria
        */        
        } catch(EPathException eex) {
        }        
        //dem       
        return mdSearchOptions;
    } 
 
    public static MultiDomainSearchCriteria buildMultiMultiDomainSearchCriteria(DomainSearch domainSearch, RelationshipSearch relationshipSearch){
        MultiDomainSearchCriteria mdSearchSearchCriteria = new MultiDomainSearchCriteria();
        return mdSearchSearchCriteria;
    } 
    
    public static EOSearchOptions buildEOSearchOptions(DomainSearch domainSearch) {
        // ToDO
        EOSearchOptions eoSearchOptions = null;
        try {
            eoSearchOptions = new EOSearchOptions();
        } catch (ConfigurationException cex) {
        }
        return eoSearchOptions;
    }
    
    public static EOSearchCriteria buildEOSearchCriteria(DomainSearch domainSearch) {
        // ToDo
        EOSearchCriteria eoSearchCriteria = new EOSearchCriteria();
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
 
        /* The back-end does not the following attributes. why? 
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
