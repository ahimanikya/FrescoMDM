/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)SearchResultsConfig.java
 * Copyright 2004-2008 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.configuration;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * Search Result configurations
 *
 * @author rtam
 * @created October 20, 2008
 */
public class SearchResultsSummaryConfig implements java.io.Serializable {
    
    private ObjectNodeConfig mRootObj;      // object configuration
    private int mSearchResultsSummaryID;    // search result summary screen configuration ID
    private int mSearchResultsDetailsID;    // search result details screen configuration ID
    private boolean mShowEUID;              // indicates if the EUID should be displayed
    private boolean mShowLID;               // indicates if the LID should be displayed
    private ArrayList<FieldConfigGroup> mFieldConfigGroups;        // ArrayList of FieldConfigGroup objects

    public SearchResultsSummaryConfig(ObjectNodeConfig rootObj, 
                                      int searchResultSummaryID, 
                                      int searchResultDetailsID, 
                                      boolean showEUID, 
                                      boolean showLID, 
                                      ArrayList<FieldConfigGroup> fieldConfigGroups) {
        mRootObj = rootObj;
        mSearchResultsSummaryID = searchResultSummaryID;
        mSearchResultsDetailsID = searchResultDetailsID;
        mShowEUID = showEUID;
        mShowLID = showLID;
        mFieldConfigGroups = fieldConfigGroups;
    }
    
    /**
     * Constructs an ArrayList of ePaths for the FieldConfigGroup objects
     * stored in mFieldConfigs.
     *
     * @return an ArrayList of ePaths for the FieldConfigGroup objects
     * stored in mFieldConfigs.
     */
    public ArrayList getEPaths() {
        ArrayList<String> ePaths = new ArrayList<String>();
        Iterator aListIter = mFieldConfigGroups.iterator();
        
        while (aListIter.hasNext()) {
            FieldConfigGroup fcg = (FieldConfigGroup) aListIter.next();
            ArrayList<FieldConfig> fieldConfigs = fcg.getFieldConfigs();
            Iterator fieldConfigsIter = fieldConfigs.iterator();
            while (fieldConfigsIter.hasNext()) {
                FieldConfig fc = (FieldConfig) fieldConfigsIter.next();
                String ePathStr = fc.toEpathStyleString(fc.getFullName());
                ePaths.add(ePathStr);
            }
        }
        return ePaths;
    }
    
    /**
     * Getter for the mSearchResultsSummaryID attribute
     *
     * @return The unique identifier for a SearchResultSummaryConfig
     * object.  This is used to determine which SearchResultSummaryConfig object
     * to use for displaying the summary of a record.
     */
    public int getSearchResultsSummaryID() {
        return mSearchResultsSummaryID;
    }
    
    /**
     * Getter for the mSearchResultsDetailsID attribute
     *
     * @return The unique identifier for a SearchResultDetailsConfig
     * object.  This is used to determine which SearchResultDetailsConfig object
     * to use for displaying the results of a search.
     */
    public int getSearchResultsDetailsID() {
        return mSearchResultsDetailsID;
    }
    
    /**
     * Getter for the mRootObj attribute
     *
     * @return Configuration image for the root object.
     */
    public ObjectNodeConfig getRootObj() {
        return mRootObj;
    }
    
    /**
     * Getter for the mShowEUID attribute
     *
     * @return Boolean value indicating whether or not to display the EUID.
     */
    public boolean getShowEUID() {
        return mShowEUID;
    }
  
    /**
     * Getter for the mShowLID attribute
     *
     * @return Boolean value indicating whether or not to display the LID.
     */
    public boolean getShowLID() {
        return mShowLID;
    }


    /**
     * Getter for the mFieldConfigGroups attribute
     *
     * @return ArrayList of FieldConfigGroup objects.
     */
    public ArrayList<FieldConfigGroup> getFieldGroupConfigs() {
        return mFieldConfigGroups;
    }
    
}