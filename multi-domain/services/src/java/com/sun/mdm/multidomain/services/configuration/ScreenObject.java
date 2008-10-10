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
	Integer mId;			 // screen ID
  	String mDisplayTitle;		 // title to be displayed 
	int mDisplayOrder;		// order in which the screen is displayed
	ArrayList<DomainScreenConfig> mDomainScreenConfigs;	// ArrayList of DomainScreenConfig objects
	ArrayList<RelationshipScreenConfig> mRelationshipScreenConfigs;	// ArrayList of RelationshipScreenConfig objects
	ArrayList<ScreenObject> mSubscreens;		// ArrayList of ScreenObjects

	Integer getID() {  //  returns the screen objects ID
	    return mId;
	}
	
	void setID(Integer id) {  
	    mId = id;
	}
	

	String getDisplayTitle() {  //  returns the display title for the screen
	    return mDisplayTitle;
	}

	void setDisplayTitle(String title) {  
	    mDisplayTitle = title;
	}

	Integer getDisplayOrder() {  //  returns the display order for the screen.
	    return mDisplayOrder;
	}

	void setDisplayOrder(Integer displayOrder) {
	    mDisplayOrder = displayOrder;
	}

	ArrayList<DomainScreenConfig> getDomainScreenConfigs() {  //  returns all the DomainScreenConfig objects for this screen 
	    return mDomainScreenConfigs;
	}

	void setDomainScreenConfigs(ArrayList<DomainScreenConfig> domainScreenConfigs) {
	    mDomainScreenConfigs = domainScreenConfigs;
	}
	
	DomainScreenConfig getDomainScreenConfig(String domain) {  //  returns the configuration for a domain.  There can be only one such object per domain in a screen.
	    return null;
	}

	ArrayList<RelationshipScreenConfig> getRelationshipScreenConfig() {  //  returns all the RelationshipScreenConfig objects for this screen 
	    return mRelationshipScreenConfigs;
	}

	void setRelationshipScreenConfig(ArrayList<RelationshipScreenConfig> relationshipScreenConfig ) {
	    mRelationshipScreenConfigs = relationshipScreenConfig;
	}

	ArrayList<SearchScreenConfig> getSearchScreenConfig(String domain) {  // returns the configuration for all search screens for a domain (ArrayList of SearchScreenConfig objects)
	    return null;
	}

	ArrayList<SearchResultsConfig> getSearchResultsConfig(String domain) {  //  returns the configuration for all first tier search results list screens for a domain (ArrayList of SearchResultsConfig objects).
	    return null;
	}

	ArrayList<SearchResultDetailsConfig> getSearchResultDetailsConfig(String domain) {  // returns the configuration for all second tier search results screens (for individual records) for a domain (ArrayList of SearchResultsDetailsConfig objects).
	    return null;
	}

	SearchResultsConfig getSearchResultsConfig(String domain, Integer searchScreenConfigId) {  //  returns the search results details configuration for a given SearchScreenConfig object for a domain
	    return null;
	}

	SearchResultDetailsConfig getSearchResultsDetailsConfig(String domain, Integer searchResultsConfigId) {  //  returns the search results details configuration for a given SearchResultsConfig object for a domain
	    return null;
	}

	ArrayList<ScreenObject> getSubscreens() {  //  retrieves all the subscreens for this screen.
	    return mSubscreens;
	}

	void setSubscreens(ArrayList<ScreenObject> subscreens) {  
	    mSubscreens = subscreens;
	}

}
