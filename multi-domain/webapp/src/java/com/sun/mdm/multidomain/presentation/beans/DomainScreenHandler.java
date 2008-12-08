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
import com.sun.mdm.multidomain.services.configuration.SearchScreenConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsSummaryConfig;
import com.sun.mdm.multidomain.services.configuration.SearchResultsConfig;
import java.util.List;
import java.util.ArrayList;

import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;

import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefsObject;
import java.util.HashMap;

/**
 * DomainScreenHandler class.
 * @author cye,Narahari
 */
public class DomainScreenHandler {

    private RelationshipManager relationshipManager;
    
    /**
     * Create an instance of RelationshipDefHandler.
     */
    public DomainScreenHandler() 
        throws ServiceException { 
        relationshipManager = ServiceManagerFactory.Instance().createRelationshipManager();
    }
    
    // Return list of fields for Summary, for the specified domain parameter
    public HashMap getSummaryFields(String domain) throws ServiceException ,Exception     {
        System.out.println("Getteing records summary fields for domain " + domain);
        HashMap recordSummaryGroup = new HashMap();
        DomainScreenConfig domainScreenConfig = (DomainScreenConfig)MDConfigManager.getDomainScreenConfig(domain);
        
        //Get the Search Records Summary for the domain
        ArrayList recordSummaryConfigs = domainScreenConfig.getSearchResultsSummaryConfigs();
        
        // For Stub demo
        //Field Config Group Array
         
        ArrayList recordSummaryFields = new ArrayList();        
        FieldConfig fieldConfig1 = new FieldConfig(null,null,"Person.FirstName","Person.FirstName","textbox",10);
        recordSummaryFields.add(fieldConfig1);
        
        FieldConfig fieldConfig2 = new FieldConfig(null,null,"Person.LastName","Person.LastName","textbox",10);
        recordSummaryFields.add(fieldConfig2);
        
        FieldConfig fieldConfig3 = new FieldConfig(null,null,"Person.Gender","Person.Gender","textbox",10);
        recordSummaryFields.add(fieldConfig3);
        
        recordSummaryGroup.put("1",recordSummaryFields);
        return recordSummaryGroup;
    }

    
    // Return list of fields for Search result, for the specified domain parameter
    public HashMap getSearchResultFields (String domain) throws ServiceException ,Exception{
        System.out.println("getting search result fields");
        
        HashMap searchResultFieldGroup = new HashMap();
        DomainScreenConfig domainScreenConfig = (DomainScreenConfig)MDConfigManager.getDomainScreenConfig(domain);
        //Get the Search Results for the domain
        ArrayList searchResultsConfigs = domainScreenConfig.getSearchResultsConfigs();
        
        for (int j = 0; j < searchResultsConfigs.size(); j++)   { 
            System.out.println("-------searchScreenConfigs.size()------" +searchResultsConfigs.size());
            SearchResultsConfig searchResultsConfig = (SearchResultsConfig)searchResultsConfigs.get(j);
                  ArrayList fieldGroupArray = searchResultsConfig.getFieldGroupConfigs();
                  for(int k=0;k < fieldGroupArray.size();k++)   {  //Field Config Group Array
                      System.out.println("-------fieldGroupArray.size()------" +fieldGroupArray.size());
                      FieldConfigGroup  fieldConfigGrp= (FieldConfigGroup)fieldGroupArray.get(k);
                      ArrayList fieldconfigsGroup =fieldConfigGrp.getFieldConfigs();                 
                      ArrayList searchResultsFields = new ArrayList();        
                      for(int l=0;l < fieldconfigsGroup.size();l++)    {  //Field Config Array
                          System.out.println("-------fieldconfigsGroup.size()------" +fieldconfigsGroup.size());
                            FieldConfig fieldConfig = (FieldConfig) fieldconfigsGroup.get(l);
                            searchResultsFields.add(fieldConfig);
                      }
                      searchResultFieldGroup.put(k,searchResultsFields);
              }             
           }
        return searchResultFieldGroup;
    }
}





