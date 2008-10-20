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

import java.util.ArrayList;

public abstract class ObjectScreenConfig {
    private String mId;          // domain ID 
    private String mDisplayName;         // name to display
    private SummaryID mSummaryID;        // summary ID for a domain
	private ArrayList<FieldConfig> mFieldConfigs;		// ArrayList of FieldConfig objects for the relationshipAttributes in the RelationshipObject.
	private ArrayList<SearchScreenConfig> mSearchScreenConfigs;	// ArrayList of SearchScreenConfig objects
	private ArrayList<SearchResultsConfig> mSearchResultsConfigs;	// ArrayList of SearchResultsConfig objects
	private ArrayList<SearchResultDetailsConfig> mSearchResultDetailsConfigs;	// ArrayList of SearchResultDetails 
    
    
    public String getID() {         // retrieves the ID
        return mId;
    }

    public void setID(String id) {      // sets the ID
        mId = id;
    }

    public String getDisplayName() {        // retrieves the display name for this domain
        return mDisplayName;
    }
    
    public void setDisplayName(String name) {   // sets the display name for this domain
        mDisplayName = name;
    }

    public ArrayList<FieldConfig> getFieldConfigs() {        // retrieves the FieldConfig objects for the attributes in the corresponding object
        return mFieldConfigs;
    }

    public void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) {   // sets the FieldConfig objects for the attributes in the corresponding object
        mFieldConfigs = fieldConfigs;
    }

    public FieldConfig getFieldConfig(String fieldname) { // returns the field configuration information for a field.
        return null;
    }

    public ArrayList<SearchScreenConfig> getSearchScreenConfigs() { // returns the configuration for all search screens (ArrayList of SearchScreenConfig objects)
        return mSearchScreenConfigs;
    }

    public void setSearchScreenConfigs(ArrayList<SearchScreenConfig> searchScreenConfigs) { // returns the configuration for all search screens (ArrayList of SearchScreenConfig objects)
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
    public ArrayList<SearchResultsConfig> getSearchResultsConfigs() {  // returns the configuration for all first tier search results list screens (ArrayList of SearchResultsConfig objects)
        return mSearchResultsConfigs;
    }

    public void setSearchResultsConfigs(ArrayList<SearchResultsConfig> searchResultsConfigs) {  // returns the configuration for all first tier search results list screens (ArrayList of SearchResultsConfig objects)
        mSearchResultsConfigs = searchResultsConfigs;
    }

    // add a search result configuration
    public void addSearchResultsConfig(SearchResultsConfig searchResultsConfig) 
            throws Exception {
        mSearchResultsConfigs.add(searchResultsConfig);
    }
    
    // RESUME HERE
/*    
    public void removeSearchResultsConfig(SearchResultsConfig searchResultsConfig) 
            throws Exception {
        mSearchResultsConfigs.remove(searchResultsConfig);
    }
*/
    public ArrayList<SearchResultDetailsConfig> getSearchResultDetailsConfigs() { // returns the configuration for all second tier search results screens (for individual records) (ArrayList of SearchResultsDetailsConfig objects)
        return mSearchResultDetailsConfigs;
    }

    public void setSearchResultDetailsConfigs(ArrayList<SearchResultDetailsConfig> searchResultDetailsConfigs) { // returns the configuration for all second tier search results screens (for individual records) (ArrayList of SearchResultsDetailsConfig objects)
        mSearchResultDetailsConfigs = searchResultDetailsConfigs;
    }

    public SearchResultsConfig getSearchResultsConfig(Integer searchScreenConfigId) 
            throws Exception {  // returns the search results details configuration for a given SearchScreenConfig object.
        return null;
    }

    public SearchResultDetailsConfig getSearchResultsDetailsConfig(Integer searchResultsConfigId) throws Exception {  // returns the search results details configuration for a given SearchResultsConfig object.
        return null;
    }
    
    // add a search result details configuration
    public void addSearchResultDetailsConfig(SearchResultDetailsConfig searchResultDetailsConfig) 
            throws Exception {
        mSearchResultDetailsConfigs.add(searchResultDetailsConfig);
    }
    // RESUME HERE
/*    
    
    public void removeSearchResultDetailsConfig(SearchResultDetailsConfig searchResultDetailsConfig) 
            throws Exception {
        mSearchResultDetailsConfigs.remove(searchResultDetailsConfig);
    }
*/    

}
