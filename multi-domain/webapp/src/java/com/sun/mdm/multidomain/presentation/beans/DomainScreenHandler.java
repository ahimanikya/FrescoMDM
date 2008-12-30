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
package com.sun.mdm.multidomain.presentation.beans;

import com.sun.mdm.multidomain.services.configuration.DomainScreenConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfig;
import com.sun.mdm.multidomain.services.configuration.FieldConfigGroup;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager; 
import com.sun.mdm.multidomain.services.configuration.ObjectNodeConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultDetailsConfig;
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsSummaryConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import java.util.ArrayList;

import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefsObject;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * DomainScreenHandler class.
 * @author cye,Narahari
 */
public class DomainScreenHandler {
    
    /**
     * Create an instance of RelationshipDefHandler.
     */
    public DomainScreenHandler() 
        throws ServiceException { 
    }
    
    // Return list of fields for Summary, for the specified domain parameter
    public HashMap getSummaryFields(String domain) throws ServiceException ,Exception     {
        System.out.println("Getteing records summary fields for domain " + domain);
        HashMap recordSummaryGroup = new HashMap();
        HashMap summaryFieldGroups = new HashMap();
        DomainScreenConfig domainScreenConfig = (DomainScreenConfig)MDConfigManager.getDomainScreenConfig(domain);
        HashMap<String, ObjectNodeConfig> objNodeConfigMap = MDConfigManager.getObjectNodeConfig(domain);
        
        //Get the Search Records Summary for the domain
        ArrayList< SearchResultsSummaryConfig> summarySCFGS = domainScreenConfig.getSearchResultsSummaryConfigs();
        for (SearchResultsSummaryConfig summaryCFG : summarySCFGS) {
            ArrayList<FieldConfigGroup> summaryFieldCGS = summaryCFG.getFieldGroupConfigs();
            for (FieldConfigGroup fieldCG : summaryFieldCGS) {
                int k = 0;
                ArrayList<FieldConfig> fields = fieldCG.getFieldConfigs();
                HashMap summaryFieldMap = new HashMap();
                for (FieldConfig field : fields) {
                    String fieldName = field.getFieldName();
                    String fieldConfigName = field.getName();
                    ObjectNodeConfig objNodeConfig = objNodeConfigMap.get(field.getObjRef());
                    FieldConfig nField = objNodeConfig.getFieldConfig(fieldName);
                    summaryFieldMap.put(fieldConfigName, nField);
                }
                summaryFieldGroups.put(k, summaryFieldMap);
                k++;
            }
        }
        recordSummaryGroup.put("summaryFieldGroups", summaryFieldGroups);

        return recordSummaryGroup;
    }
    
    // Return list of fields for Detail, for the specified domain parameter
    public HashMap getDetailFields(String domain) throws ServiceException ,Exception     {
        System.out.println("Getteing records Detail fields for domain " + domain);
        HashMap recordDetailsGroup = new HashMap();
        HashMap detailsFieldGroups = new HashMap();
        DomainScreenConfig domainScreenConfig = (DomainScreenConfig)MDConfigManager.getDomainScreenConfig(domain);
        HashMap<String, ObjectNodeConfig> objNodeConfigMap = MDConfigManager.getObjectNodeConfig(domain);
        
        //Get the Search Records Detail for the domain
        ArrayList<SearchResultDetailsConfig> detailsSCFGS = domainScreenConfig.getSearchResultDetailsConfigs();
        for (SearchResultDetailsConfig detailsCFG : detailsSCFGS) {
            ArrayList<FieldConfigGroup> detailsFieldCGS = detailsCFG.getFieldConfigs();
            for (FieldConfigGroup fieldCG : detailsFieldCGS) {
                ArrayList<FieldConfig> fields = fieldCG.getFieldConfigs();
                String fieldGroupDescription =  fieldCG.getDescription();
                HashMap detailFieldMap = new HashMap();
                for (FieldConfig field : fields) {
                    String fieldName = field.getFieldName();
                    String fieldConfigName = field.getName();
                    ObjectNodeConfig objNodeConfig = objNodeConfigMap.get(field.getObjRef());
                    FieldConfig nField = objNodeConfig.getFieldConfig(fieldName);
                    detailFieldMap.put(fieldConfigName, nField);
                }
                detailsFieldGroups.put(fieldGroupDescription, detailFieldMap);
            }
        }
        recordDetailsGroup.put("detailFieldGroups", detailsFieldGroups);
        return recordDetailsGroup;
    }
    
    // Return list of fields for Search result, for the specified domain parameter
    public HashMap getSearchResultFields (String domain) throws ServiceException ,Exception{
        System.out.println("getting search result fields");
        
        HashMap searchResultFieldGroup = new HashMap();
        HashMap resultsFieldGroups = new HashMap();
        DomainScreenConfig domainScreenConfig = (DomainScreenConfig)MDConfigManager.getDomainScreenConfig(domain);
        HashMap<String, ObjectNodeConfig> objNodeConfigMap = MDConfigManager.getObjectNodeConfig(domain);
        
        //Get the Search Results for the domain
        ArrayList<SearchResultsConfig> resultsSCFGS = domainScreenConfig.getSearchResultsConfigs();
        for (SearchResultsConfig resultsCFG : resultsSCFGS) {
            ArrayList<FieldConfigGroup> resultsFieldCGS = resultsCFG.getFieldGroupConfigs();
            for (FieldConfigGroup fieldCG : resultsFieldCGS) {
                int k = 0;
                ArrayList<FieldConfig> fields = fieldCG.getFieldConfigs();
                HashMap resultsFieldMap = new HashMap();
                for (FieldConfig field : fields) {
                    String fieldName = field.getFieldName();
                    String fieldConfigName = field.getName();
                    ObjectNodeConfig objNodeConfig = objNodeConfigMap.get(field.getObjRef());
                    FieldConfig nField = objNodeConfig.getFieldConfig(fieldName);
                    resultsFieldMap.put(fieldConfigName, nField);
                }
                resultsFieldGroups.put(k, resultsFieldMap);
                k++;
            }
        }
        searchResultFieldGroup.put("resultsFieldGroups", resultsFieldGroups);

        return searchResultFieldGroup;
    }
}





