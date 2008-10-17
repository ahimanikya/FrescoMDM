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

import java.util.HashMap;
import java.util.ArrayList;

public abstract class ScreenObject {
	private Integer mId;                    // screen ID
  	private String mDisplayTitle;           // title to be displayed 
	private int mDisplayOrder;		        // order in which the screen is displayed
	private ArrayList<DomainScreenConfig> mDomainScreenConfigs; // ArrayList of DomainScreenConfig objects
	private ArrayList<RelationshipScreenConfig> mRelationshipScreenConfigs;	// ArrayList of RelationshipScreenConfig objects
	private ArrayList<ScreenObject> mSubscreens;                // ArrayList of ScreenObjects

    public ScreenObject() { 
    }
    
    public ScreenObject(Integer id, String displayTitle, int displayOrder,
                             ArrayList<DomainScreenConfig> domainScreenConfigs,
                             ArrayList<RelationshipScreenConfig> relationshipScreenConfigs,
                             ArrayList<ScreenObject> subscreens) {
        mId = id;
        mDisplayTitle = displayTitle;
        mDisplayOrder = displayOrder;
        mDomainScreenConfigs = domainScreenConfigs;
        mRelationshipScreenConfigs = relationshipScreenConfigs;
        mSubscreens = subscreens;
    }
    
    /**
     * Retrieves the screen object's ID.
     *
     * @return the screen object's ID
     */    
	public Integer getID() {
	    return mId;
	}
	
    /**
     * Sets the screen object's ID.
     *
     * @param id The screen object's ID.
     */    
	public void setID(Integer id) {  
	    mId = id;
	}
	
    /**
     * Retrieves the screen object's display title.
     *
     * @return the screen object's display title.
     */    
	public String getDisplayTitle() {  
	    return mDisplayTitle;
	}

    /**
     * Sets the screen object's display title.
     *
     * @param displayTitle The screen object's display title.
     */    
	public void setDisplayTitle(String displayTitle) {  
	    mDisplayTitle = displayTitle;
	}

    /**
     * Retrieves the screen object's display order.
     *
     * @return the screen object's display order.
     */	
     public Integer getDisplayOrder() {  
	    return mDisplayOrder;
	}

    /**
     * Sets the screen object's display order.
     *
     * @param displayOrder The screen object's display order.
     */    
	public void setDisplayOrder(Integer displayOrder) {
	    mDisplayOrder = displayOrder;
	}

    /**
     * Retrieves all the domain screen configurations for this screen object.
     *
     * @return all the domain screen configurations for this screen object.
     */	
	public ArrayList<DomainScreenConfig> getDomainScreenConfigs() {  
	    return mDomainScreenConfigs;
	}

    /**
     * Sets the domain screen configurations for this screen object.
     *
     * @param domainScreenConfigs  Domain screen configurations to add to this screen object.
     */	
	public void setDomainScreenConfigs(ArrayList<DomainScreenConfig> domainScreenConfigs) {
	    mDomainScreenConfigs = domainScreenConfigs;
	}
	
    /**
     * Add a domain screen configuration for this screen object.
     *
     * @param domainScreenConfig  Configuration information for a domain.
     * @throws Exception if an error is encountered.
     */	
	public void addDomainScreenConfig(DomainScreenConfig domainScreenConfig) throws Exception {
	    mDomainScreenConfigs.add(domainScreenConfig);
	}
	
    /**
     * Remove a domain screen configuration for this screen object.
     *
     * @param domainID  ID of the domain for which the configuration information is to be removed.
     * @throws Exception if an error is encountered.
     */	
	public void removeDomainScreenConfig(String domainID) throws Exception {  
	    for (int i = 0; i < mDomainScreenConfigs.size(); i++) {
	        DomainScreenConfig dsc = mDomainScreenConfigs.get(i);
	        if (domainID.equalsIgnoreCase(dsc.getID()) == true) {
	            mDomainScreenConfigs.remove(i);
	        }
	    }
	}
	
    /**
     * Retrieves the screen configuration for a domain.  There can be only one such 
     * object per domain in a screen.
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @return screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */    
	public DomainScreenConfig getDomainScreenConfig(String domainID) throws Exception {  
	    for (int i = 0; i < mDomainScreenConfigs.size(); i++) {
	        DomainScreenConfig dsc = mDomainScreenConfigs.get(i);
	        if (domainID.equalsIgnoreCase(dsc.getID()) == true) {
	            return dsc;
	        }
	    }
	    return null;
	}

    /**
     * Retrieves all the relationship screen configurations for this screen object.
     *
     * @return all the relationship screen configurations for this screen object.
     */	
	public ArrayList<RelationshipScreenConfig> getRelationshipScreenConfig() {  
	    return mRelationshipScreenConfigs;
	}

    /**
     * Sets the relationship screen configurations for this screen object.
     *
     * @param relationshipScreenConfigs  Relationship screen configurations to add to 
     * this screen object.
     */	
	public void setRelationshipScreenConfig(ArrayList<RelationshipScreenConfig> relationshipScreenConfigs) {
	    mRelationshipScreenConfigs = relationshipScreenConfigs;
	}

    /**
     * Retrieves all the search screen configurations for a domain.
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @return search screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */	
	public ArrayList<SearchScreenConfig> getSearchScreenConfig(String domainID) throws Exception {  
	    DomainScreenConfig dsc = getDomainScreenConfig(domainID);
	    if (dsc != null) {
	        return dsc.getSearchScreenConfigs();
	    }
	    return null;
	}

    /**
     * Retrieves all the search results screen configurations for a domain.
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @return search result screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */	
	public ArrayList<SearchResultDetailsConfig> getSearchResultsConfig(String domainID) 
	        throws Exception {  
	            
	    DomainScreenConfig dsc = getDomainScreenConfig(domainID);
	    if (dsc != null) {
	        return dsc.getSearchResultsDetailsConfigs();
	    }
	    return null;
	}

    /**
     * Retrieves all the search result details screen configurations for a domain.
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @return search result details screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */	
	public ArrayList<SearchResultDetailsConfig> getSearchResultDetailsConfig(String domainID) 
	        throws Exception {  
	            
	    DomainScreenConfig dsc = getDomainScreenConfig(domainID);
	    if (dsc != null) {
	        return dsc.getSearchResultsDetailsConfigs();
	    }
	    return null;
	}

    /**
     * Retrieves the search result screen configuration for a domain and a search screen .
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @param searchScreenConfigId  search screen configuration ID
     * @return search results screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */	
	public SearchResultsConfig getSearchResultsConfig(String domainID, 
	                                                  Integer searchScreenConfigId) 
            throws Exception {  
                
	    DomainScreenConfig dsc = getDomainScreenConfig(domainID);
	    if (dsc != null) {
	        return dsc.getSearchResultsConfig(searchScreenConfigId);
	    }
	    return null;
	}

    /**
     * Retrieves the search result details screen configuration for a domain and a search result screen.
     *
     * @param domainID  ID of the domain for which the configuration information is requested.
     * @param searchResultsConfigId  search screen results configuration ID
     * @return search result details screen configuration information for the domain.
     * @throws Exception if an error is encountered.
     */	
	public SearchResultDetailsConfig getSearchResultsDetailsConfig(String domainID, 
	                                                               Integer searchResultsConfigId) 
            throws Exception {  
                
	    DomainScreenConfig dsc = getDomainScreenConfig(domainID);
	    if (dsc != null) {
	        return dsc.getSearchResultsDetailsConfig(searchResultsConfigId);
	    }
	    return null;
	}

    /**
     * Retrieves all the subscreens for this screen object.
     *
     * @return all the subscreens for this screen object.
     */	
	public ArrayList<ScreenObject> getSubscreens() {  
	    return mSubscreens;
	}

    /**
     * Sets the subscreens for this screen object.
     *
     * @param subscreens Subscreens to add to this screen object.
     */	
	public void setSubscreens(ArrayList<ScreenObject> subscreens) {  
	    mSubscreens = subscreens;
	}

}
