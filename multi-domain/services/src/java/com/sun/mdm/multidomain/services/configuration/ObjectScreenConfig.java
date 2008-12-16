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
    

    /** Constructor for ObjectScreenConfig.
     * 
     */
    public ObjectScreenConfig() {
    	mFieldConfigs = new ArrayList<FieldConfig>();		
    	mSearchScreenConfigs = new ArrayList<SearchScreenConfig>();	
    	mSearchResultsConfigs = new ArrayList<SearchResultsConfig>();	
    	mSearchResultsSummaryConfigs = new ArrayList<SearchResultsSummaryConfig>();
    	mSearchResultDetailsConfigs = new ArrayList<SearchResultDetailsConfig>();	
    }    

    /**  Retrieve the Domain ID
     * 
     * @return the Domain ID
     */
    public String getID() {         
        return mId;
    }
    
    /** Sets the Domain ID
     * 
     * @param id The value of the Domain ID
     */
    public void setID(String id) {
        mId = id;
    }

    /**  Retrieve the display name for this object screen.
     * 
     * @return the display name for this object screen.
     */
    public String getDisplayName() {
        return mDisplayName;
    }
    
    /** Sets the display name for this object screen.
     * 
     * @param  name  The display name for this object screen.
     */
    public void setDisplayName(String name) {   
        mDisplayName = name;
    }

    /**  Retrieve the FieldConfig objects for the attributes in the corresponding object.
     * 
     * @return the the FieldConfig objects for the attributes in the corresponding object.
     */
    public ArrayList<FieldConfig> getFieldConfigs() {        
        return mFieldConfigs;
    }

    /**  Sets the FieldConfig objects for the attributes in the corresponding object
     * 
     * @return the the FieldConfig objects for the attributes in the corresponding object.
     */
    public void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) {   
        mFieldConfigs = fieldConfigs;
    }

    /**  Retrives the field configuration information for a field.
     * 
     * @param fieldname  Name of the field to retrieve.
     * @return the FieldConfig objects for a field.
     */
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

    /**  Add a field configuration instance.
     * 
     * @param fieldConfig  FieldConfig instance to add.
     */
    public void addFieldConfig(FieldConfig fieldConfig) { 
        mFieldConfigs.add(fieldConfig);
    }

    /**  Retrives the configuration for all search screens
     * 
     * @param fieldname  Name of the field to retrieve.
     * @return An ArrayList containing the configuration for all search screens.
     */
    public ArrayList<SearchScreenConfig> getSearchScreenConfigs() { 
        return mSearchScreenConfigs;
    }

    /**  Retrives the search screen configuration that matches the screenTitle parameter.
     * 
     * @param screenTitle  Title of the screen
     * @return The configuration for corresponding search screen.
     */
    public SearchScreenConfig getSearchScreenConfig(String screenTitle) { 
        if (screenTitle != null) {
            for (SearchScreenConfig ssc : mSearchScreenConfigs) {
                if (screenTitle.equalsIgnoreCase(ssc.getScreenTitle()) == true)
                    return ssc;
            }
        }
        return null;
    }

    /**  Sets the search screen configurations
     * 
     * @param searchScreenConfigs  Search screen configurations.
     */
    public void setSearchScreenConfigs(ArrayList<SearchScreenConfig> searchScreenConfigs) { 
        mSearchScreenConfigs = searchScreenConfigs;
    }
    
    /**  Add a search screen configurations
     * 
     * @param searchScreenConfig  Search screen configuration to add.
     */
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

    /**  Retrieves all search results configurations
     * 
     * @return Search results screen configurations.
     */
    public ArrayList<SearchResultsConfig> getSearchResultsConfigs() {  
        return mSearchResultsConfigs;
    }

    /**  Sets all search results configurations
     * 
     * @param searchResultsConfig  Search results screen configuration to add.
     */
    public void setSearchResultsConfigs(ArrayList<SearchResultsConfig> searchResultsConfigs) {  
        mSearchResultsConfigs = searchResultsConfigs;
    }

    /**  Add a search result configuration.
     * 
     * @param searchResultsConfig  Search results screen configuration to add.
     */
    public void addSearchResultsConfig(SearchResultsConfig searchResultsConfig) 
            throws Exception {
        mSearchResultsConfigs.add(searchResultsConfig);
    }
    

    /**  Retrieves all search results summary configurations.
     * 
     * @return All search result summary configurations.
     */
    public ArrayList<SearchResultsSummaryConfig> getSearchResultsSummaryConfigs() {  
        return mSearchResultsSummaryConfigs;
    }

    /**  Sets all search results summary configurations.
     * 
     * @param sRSC  ArrayList of search results summary configurations.
     */
    public void setSearchResultsSummaryConfigs(ArrayList<SearchResultsSummaryConfig>sRSC) {  
        mSearchResultsSummaryConfigs = sRSC;
    }

    /**  Add a search results summary configuration.
     * 
     * @param sRSC  Search results summary configuration to add.
     */
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

    /**  Retrieves all search result details configurations.
     * 
     * @returns All search result details configurations.
     */
    public ArrayList<SearchResultDetailsConfig> getSearchResultDetailsConfigs() { 
        return mSearchResultDetailsConfigs;
    }

    /**  Sets all search result details configurations.
     * 
     * @param searchResultDetailsConfigs All search result details configurations.
     */
    public void setSearchResultDetailsConfigs(ArrayList<SearchResultDetailsConfig> searchResultDetailsConfigs) { 
        mSearchResultDetailsConfigs = searchResultDetailsConfigs;
    }

    /**  Add a search result details configuration.
     * 
     * @param searchResultDetailsConfig One instance of search result details configuration.
     */
    public void addSearchResultDetailsConfig(SearchResultDetailsConfig searchResultDetailsConfig) 
            throws Exception {
        mSearchResultDetailsConfigs.add(searchResultDetailsConfig);
    }
    
    /**  Retrieves the search results configuration for a given search result config ID.
     * 
     * @param searchResultsConfigId  Search result config ID.
     * @return search results configuration for the search result config ID.
     */
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
    
    /**  Retrieves the search results summary configuration for a given search results config ID.
     * 
     * @param searchResultsConfigId  Search result config ID.
     * @return search results summary configuration for the seardch result config ID.
     */
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
    
    /**  Retrieves the search results detail configuration for a given search results config ID.
     * 
     * @param searchResultsConfigId  Search result config ID.
     * @return search results details configuration for the seardch result config ID.
     */
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
