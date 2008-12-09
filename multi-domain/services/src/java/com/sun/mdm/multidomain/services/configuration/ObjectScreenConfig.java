/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ObjectScreenConfig {
    private String mId;          // domain ID 
    private String mDisplayName;         // name to display
	private ArrayList<FieldConfig> mFieldConfigs;		// ArrayList of FieldConfig objects for the relationshipAttributes in the RelationshipObject.
	private ArrayList<SearchScreenConfig> mSearchScreenConfigs;	// ArrayList of SearchScreenConfig objects
	private ArrayList<SearchResultsConfig> mSearchResultsConfigs;	// ArrayList of SearchResultsConfig objects
	private ArrayList<SearchResultsSummaryConfig> mSearchResultsSummaryConfigs;	// ArrayList of SearchResultsSummaryConfig objects
	private ArrayList<SearchResultDetailsConfig> mSearchResultDetailsConfigs;	// ArrayList of SearchResultDetails 
    

    public ObjectScreenConfig() {
    	mFieldConfigs = new ArrayList<FieldConfig>();		
    	mSearchScreenConfigs = new ArrayList<SearchScreenConfig>();	
    	mSearchResultsConfigs = new ArrayList<SearchResultsConfig>();	
    	mSearchResultsSummaryConfigs = new ArrayList<SearchResultsSummaryConfig>();
    	mSearchResultDetailsConfigs = new ArrayList<SearchResultDetailsConfig>();	
    }    

    // retrieves the Domain ID
    
    public String getID() {         
        return mId;
    }
    
    // sets the Domain ID

    public void setID(String id) {
        mId = id;
    }

    // retrieves the display name for this domain
    
    public String getDisplayName() {
        return mDisplayName;
    }
    
    // sets the display name for this domain
    
    public void setDisplayName(String name) {   
        mDisplayName = name;
    }

    // retrieves the FieldConfig objects for the attributes in the corresponding object
    
    public ArrayList<FieldConfig> getFieldConfigs() {        
        return mFieldConfigs;
    }

    // sets the FieldConfig objects for the attributes in the corresponding object
    
    public void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) {   
        mFieldConfigs = fieldConfigs;
    }

    // returns the field configuration information for a field.
    
    public FieldConfig getFieldConfig(String fieldname) { 
        Iterator<FieldConfig> iter = mFieldConfigs.iterator();
        while (iter.hasNext()) {
            FieldConfig fc = iter.next();
            if (fieldname.equalsIgnoreCase(fc.getName())) {
                return fc;
            }
        }
        return null;
    }

    // add a field configuration class
    
    public void addFieldConfig(FieldConfig fieldConfig) { 
        mFieldConfigs.add(fieldConfig);
    }

    // returns the configuration for all search screens (ArrayList of SearchScreenConfig objects)
    
    public ArrayList<SearchScreenConfig> getSearchScreenConfigs() { 
        return mSearchScreenConfigs;
    }

    // returns the search screen configuration that matches the screenTitle parameter.
    
    public SearchScreenConfig getSearchScreenConfig(String screenTitle) { 
        if (screenTitle != null) {
            for (SearchScreenConfig ssc : mSearchScreenConfigs) {
                if (screenTitle.equalsIgnoreCase(ssc.getScreenTitle()) == true)
                    return ssc;
            }
        }
        return null;
    }

    // returns the configuration for all search screens (ArrayList of SearchScreenConfig objects)
    
    public void setSearchScreenConfigs(ArrayList<SearchScreenConfig> searchScreenConfigs) { 
        mSearchScreenConfigs = searchScreenConfigs;
    }
    
    public void addSearchScreenConfig(SearchScreenConfig searchScreenConfig) 
            throws Exception {
        mSearchScreenConfigs.add(searchScreenConfig);
    }
    // RESUME HERE
/*    
    public void removeSearchScreenConfig(SearchScreenConfig searchScreenConfig) 
            throws Exception {
        mSearchScreenConfigs.remove(searchScreenConfig);
    }
*/    
    // retrieves all search results configurations
    
    public ArrayList<SearchResultsConfig> getSearchResultsConfigs() {  
        return mSearchResultsConfigs;
    }

    // sets all search results configurations

    public void setSearchResultsConfigs(ArrayList<SearchResultsConfig> searchResultsConfigs) {  
        mSearchResultsConfigs = searchResultsConfigs;
    }

    // add a search result configuration
    
    public void addSearchResultsConfig(SearchResultsConfig searchResultsConfig) 
            throws Exception {
        mSearchResultsConfigs.add(searchResultsConfig);
    }
    

    // retrieves all SearchResultsSummaryConfig objects
        
    public ArrayList<SearchResultsSummaryConfig> getSearchResultsSummaryConfigs() {  
        return mSearchResultsSummaryConfigs;
    }

    // sets all SearchResultsSummaryConfig objects
    
    public void  setSearchResultsSummaryConfigs(ArrayList<SearchResultsSummaryConfig>sRSC) {  
        mSearchResultsSummaryConfigs = sRSC;
    }

    // add a search result summary configuration
    public void addSearchResultsSummaryConfig(SearchResultsSummaryConfig sRSC) 
            throws Exception {
        mSearchResultsSummaryConfigs.add(sRSC);
    }
    
    
    
    // RESUME HERE
/*    
    public void removeSearchResultsConfig(SearchResultsConfig searchResultsConfig) 
            throws Exception {
        mSearchResultsConfigs.remove(searchResultsConfig);
    }
*/
    // retrieves all search results detail configurations
    
    public ArrayList<SearchResultDetailsConfig> getSearchResultDetailsConfigs() { 
        return mSearchResultDetailsConfigs;
    }

    // sets all search results detail configurations

    public void setSearchResultDetailsConfigs(ArrayList<SearchResultDetailsConfig> searchResultDetailsConfigs) { // returns the configuration for all second tier search results screens (for individual records) (ArrayList of SearchResultsDetailsConfig objects)
        mSearchResultDetailsConfigs = searchResultDetailsConfigs;
    }

    // add a search result details configuration
    public void addSearchResultDetailsConfig(SearchResultDetailsConfig searchResultDetailsConfig) 
            throws Exception {
        mSearchResultDetailsConfigs.add(searchResultDetailsConfig);
    }
    
    // retrieves the search results configuration for a given search  result config ID

    public SearchResultsConfig getSearchResultsConfig(Integer searchResultsConfigId) 
            throws Exception {  
        Iterator<SearchResultsConfig> iter = mSearchResultsConfigs.iterator();	
        while(iter.hasNext()) {
            SearchResultsConfig sRC = iter.next();
            if (sRC.getSearchResultsID() == searchResultsConfigId.intValue()) {
                return sRC;
            }
        }
        return null;
    }
    
    // retrieves the search results summary configuration for a given search results config ID

    public SearchResultsSummaryConfig getSearchResultsSummaryConfig(Integer searchResultsConfigId) 
            throws Exception {  
                
        SearchResultsConfig sRC = getSearchResultsConfig(searchResultsConfigId);
        int searchResultSummaryConfigID = sRC.getSearchResultsDetailsID();

        Iterator<SearchResultsSummaryConfig> iter = mSearchResultsSummaryConfigs.iterator();	
        while(iter.hasNext()) {
            SearchResultsSummaryConfig sRSC = iter.next();
            if (sRSC.getSearchResultsSummaryID() ==searchResultSummaryConfigID) {
                return sRSC;
            }
        }
        return null;
    }
    
     // retrieves the search results detail configuration for a given search results config ID

    public SearchResultDetailsConfig getSearchResultsDetailsConfig(Integer searchResultsConfigId) 
            throws Exception {  

    
        SearchResultsConfig sRC = getSearchResultsConfig(searchResultsConfigId);
        int searchResultDetailsConfigID = sRC.getSearchResultsDetailsID();
        Iterator<SearchResultDetailsConfig> iter = mSearchResultDetailsConfigs.iterator();	
        while(iter.hasNext()) {
            SearchResultDetailsConfig sRDC = iter.next();
            if (sRDC.getSearchResultDetailID() == searchResultDetailsConfigID) {
                return sRDC;
            }
        }
        return null;
    }
    
   
    // RESUME HERE
/*    
    
    public void removeSearchResultDetailsConfig(SearchResultDetailsConfig searchResultDetailsConfig) 
            throws Exception {
        mSearchResultDetailsConfigs.remove(searchResultDetailsConfig);
    }
*/    

}
