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
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.Iterator;
import java.util.ArrayList;


/**
 * Search Result configurations
 *
 * @author rtam
 * @created July 27, 2007
 */
public class SearchResultsConfig implements java.io.Serializable {

    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.SearchResultsConfig");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    public static int DISABLED = -1;       // indicates if a search result summary or search 
                                            // result detail screen is disabled.
    private ObjectNodeConfig mRootObj;      // object configuration
    private String mDisplayName;            // display name
    private int mSearchResultsID;           // search result screen configuration ID
    private int mSearchResultsSummaryID;     // search result summary screen configuration ID
    private int mSearchResultsDetailID;      // search result details screen configuration ID
    private int mMaxRecords;                // maximum number of records to be returned
                                            // in a search
    private int mPageSize;                  // maximum number of records to be displayed
                                            // at one time
    private boolean mShowEUID;              // indicates if the EUID should be displayed
    private boolean mShowLID;               // indicates if the LID should be displayed
    private ArrayList<FieldConfigGroup> mFieldConfigGroups;        // ArrayList of FieldConfigGroup objects


    public SearchResultsConfig(ObjectNodeConfig rootObj, String displayName, int searchResultsID, 
                                int searchResultsSummaryID, int searchResultDetailsID, 
                                int pageSize, int maxRecords, boolean showEUID, 
                                boolean showLID, ArrayList<FieldConfigGroup> fieldConfigGroups) 
                throws Exception {
                    
        mRootObj = rootObj;
        mDisplayName = displayName;
        mSearchResultsID = searchResultsID;
        // RESUME HERE
        // is this required?
/*        
        if ((searchResultsSummaryID >= 0 && searchResultDetailsID >= 0) ||
            (searchResultsSummaryID < 0 && searchResultDetailsID < 0))  {
                
            throw new Exception(mLocalizer.t("CFG531: Either Search Results Summary or " +
                                             "Search Result Details must be defined. "));
        }
        if (searchResultsSummaryID >= 0)  {
            mSearchResultsSummaryID = searchResultsSummaryID;
            mSearchResultsDetailID = DISABLED;
        }
        if (searchResultDetailsID >= 0)  {
            mSearchResultsDetailID = searchResultDetailsID;
            mSearchResultsSummaryID = DISABLED;
        }
 */
        mSearchResultsSummaryID = searchResultsSummaryID;
        mSearchResultsDetailID = searchResultDetailsID;
        mPageSize = pageSize;
        mMaxRecords = maxRecords;
        mShowEUID = showEUID;
        mShowLID = showLID;
        mFieldConfigGroups = fieldConfigGroups;
    }

    /**
     * Getter for the mSearchResultsID attribute
     *
     * @return The unique identifier for this instance of a SearchResultsConfig
     * object. 
     */
    public String getDisplayName() {
        return mDisplayName;
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
     * Getter for the mSearchResultsID attribute
     *
     * @return The unique identifier for this instance of a SearchResultsConfig
     * object. 
     */
    public int getSearchResultsID() {
        return mSearchResultsID;
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
     * Getter for the mSearchResultsDetailID attribute
     *
     * @return The unique identifier for a SearchResultDetailsConfig
     * object.  This is used to determine which SearchResultDetailsConfig object
     * to use for displaying the results of a search.
     */
    public int getSearchResultsDetailID() {
        return mSearchResultsDetailID;
    }
    

    /**
     * Getter for the mPageSize attribute
     *
     * @return The maximum number of records to be displayed at one time
     */
    public int getPageSize() {
        return mPageSize;
    }
    
    /**
     * Getter for the mMaxRecords attribute
     *
     * @return The maximum number of records to be returned from a search.
     */
    public int getMaxRecords() {
        return mMaxRecords;
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