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
 * @(#)SearchScreenConfig.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.configuration;

//import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenOptions;
import java.util.Iterator;
import java.util.ArrayList;


/**
 * Search Result configurations
 *
 * @author rtam
 * @created July 27, 2007
 */
public class SearchScreenConfig implements java.io.Serializable {
    
    private ObjectNodeConfig mRootObj;  // object configuration
    private String mScreenTitle;        // screen title
    private String mInstruction;        // instruction to the user
    private int mSearchResultID;    // search result screen configuration ID
    private int mScreenOrder;       // display order for the search screen
    private boolean mShowEUID;      // indicates if the EUID should be displayed
    private boolean mShowLID;       // indicates if the LID should be displayed
    private boolean mShowCreateDate; // indicates if the Create Date should be displayed
    private boolean mShowCreateTime; // indicates if the Create Time should be displayed
    private boolean mShowStatus;    // indicates if the Status should be displayed
    private SearchScreenOptions mOptions;   // search screen options
    private ArrayList mFieldConfigs;   // array list of FieldConfigGroup 
                                       // objects containing display information
                                       // for the search fields.
    

    /**
     * Creates a new instance of the SearchScreenConfig class
     *
     * @param rootObj  The ObjectNodeConfig instance to which a 
     * SearchScreenConfig instance belongs.
     * @param screenTitle  The title of the search screen.
     * @param instruction  The instructions for the search screen.
     * @param searchResultID  The unique identifier for a SearchResultsConfig 
     * instance.  It identifies the Search Results screen configuration
     * specifying how to display the search results obtained from this 
     * particular search screen.
     * @param screenOrder  This is the order in which the search screen will be
     * displayed, starting from 0 and increasing.  A value of 0 means that it 
     * will be the first search screen displayed.
     * @param showEUID Indicates if the EUID should be displayed.
     * @param showLID Indicates if the LID should be displayed.
     * @param options  These are the search options
     * @param fieldConfigs  The configurations for the fields to be displayed.
     */
    public SearchScreenConfig(ObjectNodeConfig rootObj, String screenTitle, 
                              String instruction, int searchResultID, 
                              int screenOrder, boolean showEUID, 
                              boolean showLID, SearchScreenOptions options, 
                              ArrayList fieldConfigs) {
                    
        mRootObj = rootObj;
        mScreenTitle = screenTitle;
        mInstruction = instruction;
        mSearchResultID = searchResultID;
        mScreenOrder = screenOrder;
        mShowEUID = showEUID;
        mShowLID = showLID;
        mShowCreateDate = false;
        mShowCreateTime = false;
        mShowStatus = false;
        mOptions = options;
        mFieldConfigs = fieldConfigs;
    }
    
    /**
     * Creates a new instance of the SearchScreenConfig class
     *
     * @param rootObj  The ObjectNodeConfig instance to which a 
     * SearchScreenConfig instance belongs.
     * @param screenTitle  The title of the search screen.
     * @param instruction  The instructions for the search screen.
     * @param searchResultID  The unique identifier for a SearchResultsConfig 
     * instance.  It identifies the Search Results screen configuration
     * specifying how to display the search results obtained from this 
     * particular search screen.
     * @param screenOrder  This is the order in which the search screen will be
     * displayed, starting from 0 and increasing.  A value of 0 means that it 
     * will be the first search screen displayed.
     * @param showEUID Indicates if the EUID should be displayed.
     * @param showLID Indicates if the LID should be displayed.
     * @param showCreateDate Indicates if the Create Date should be displayed.
     * @param showCreateTime Indicates if the Create Time should be displayed.
     * @param showStatus Indicates if the Status should be displayed.
     * @param options  These are the search options
     * @param fieldConfigs  The configurations for the fields to be displayed.
     */
    public SearchScreenConfig(ObjectNodeConfig rootObj, String screenTitle, 
                              String instruction, int searchResultID, 
                              int screenOrder, boolean showEUID, 
                              boolean showLID, boolean showCreateDate, 
                              boolean showCreateTime, boolean showStatus, 
                              SearchScreenOptions options, 
                              ArrayList fieldConfigs) {
                    
        mRootObj = rootObj;
        mScreenTitle = screenTitle;
        mInstruction = instruction;
        mSearchResultID = searchResultID;
        mScreenOrder = screenOrder;
        mShowEUID = showEUID;
        mShowLID = showLID;
        mShowCreateDate = showCreateDate;
        mShowCreateTime = showCreateTime;
        mShowStatus = showStatus;
        mOptions = options;
        mFieldConfigs = fieldConfigs;
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
     * Getter for the mScreenTitle attribute
     *
     * @return Title of the search screen.
     */
    public String getScreenTitle() {
        return mScreenTitle;
    }

    /**
     * Getter for the mInstruction attribute
     *
     * @return Instruction for the search screen.
     */
    public String getInstruction() {
        return mInstruction;
    }

    /**
     * Getter for the mSearchResultID attribute
     *
     * @return The unique identifier for this instance of a SearchResultsConfig
     * object.  This is used to determine which SearchResultsConfig object
     * to use for displaying the results of a search.
     */
    public int getSearchResultID() {
        return mSearchResultID;
    }
    
    /**
     * Getter for the mScreenOrder attribute
     *
     * @return The order in which the search screen is to be displayed, starting 
     * from 0 and increasing.  A value of 0 means that it will be the first 
     * search screen displayed.
     */
    public int getScreenOrder() {
        return mScreenOrder;
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
     * Getter for the mShowCreateDate attribute
     *
     * @return Boolean value indicating whether or not to display the Create Date.
     */
    public boolean getShowCreateDate() {
        return mShowCreateDate;
    }

    /**
     * Getter for the mShowCreateTime attribute
     *
     * @return Boolean value indicating whether or not to display the Create Time.
     */
    public boolean getShowCreateTime() {
        return mShowCreateTime;
    }

    /**
     * Getter for the mShowStatus attribute
     *
     * @return Boolean value indicating whether or not to display the Status.
     */
    public boolean getShowStatus() {
        return mShowStatus;
    }

    /**
     * Getter for the mOptions attribute
     *
     * @return Options for the search screen.
     */
    public SearchScreenOptions getOptions() {
        return mOptions;
    }
    
    /**
     * Getter for the mFieldConfigs attribute
     *
     * @return ArrayList of FieldConfig objects.
     */
    public ArrayList getFieldConfigs() {
        return mFieldConfigs;
    }
    
}