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
 * @(#)ScreenObject.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import java.util.Iterator;
import java.util.ArrayList;


/**
 * ScreenObject class representing eView screens.
 *
 * @author rtam
 * @created July 27, 2007
 */
public class ScreenObject implements java.io.Serializable {
    
    private static final String EUID = "EUID";
    
    private Integer mID;                    // screen ID
    private String mDisplayTitle;           // title to be displayed 
    private ObjectNodeConfig mRootObj;      // object configuration
    private int mDisplayOrder;     // order in which the screen is displayed
    private Object mEntrance;   // JSP/JSF page to be executed when this screen
                                // is chosen from the menu.  This is
                                // impelmentation-dependent on the Presentation
                                // Layer, so the details remain to be defined.
    private ArrayList mSearchResultsConfig;   // ArrayList of SearchResultsConfig
                                              // objects that define the search 
                                              // result configurations
    private ArrayList mSearchScreensConfig;   // ArrayList SearchScreenConfig 
                                              // objects that define the search
                                              // field configurations
    private ArrayList mSubscreensConfig;      // ArrayList of ScreenObjects
                                              // that represent subscreens.
    /**
     * Creates a new instance of the ScreenObject class
     *
     * @param id The screen ID.
     * @param displayTitle The screen title.
     * @param rootObj The object configuration.
     * @param displayOrder The order in which the screen dashboard 
     * title is to be displayed on the dashboard.
     * @param entrance The JSP/JSF page to be executed when this
     * screen is chosen from the dashboard.  This is implementation-dependent.
     * and remains to be defined in detail.
     * @param sScreensConfig The ArrayList containg SearchScreensConfig objects
     * that specify how to display the search criteria for a search screen.
     * @param sResultsConfig The ArrayList containg SearchResultsConfig objects
     * that specify how to display the search results in a search screen.
     * field configuration information.
     * @param subscreensConfig The ArrayList containg ScreenObject objects
     * that define the subscreens for an instance of this class.
     */
    public ScreenObject(Integer id, String displayTitle, ObjectNodeConfig rootObj, 
                        int displayOrder, Object entrance, 
                        ArrayList sScreensConfig, ArrayList sResultsConfig,
                        ArrayList subscreensConfig) {
                    
        mID = id;
        mDisplayTitle = displayTitle;
        mRootObj = rootObj;
        mDisplayOrder = displayOrder;
        mEntrance = entrance;
        mSearchScreensConfig = sScreensConfig;
        mSearchResultsConfig = sResultsConfig;
        mSubscreensConfig = subscreensConfig;
    }
    
    /**
     * Retrieves the SearchResultsConfig object whose search results ID matches 
     * the srID paramter.
     *
     * @param srID This is the ID of the SearchResultsConfig
     * object that is to be returned.
     * @return the SearchResultsConfig object whose ID matches the
     * srID paramter.
     * @throws Exception if any errors are encountered.
     */
    public SearchResultsConfig getSearchResultsConfig(int srID) 
            throws Exception {
        if (srID < 0) {
            throw new Exception("Invalid ID for SearchResultsConfig object: " 
                                + srID);
        }
        if (mSearchResultsConfig == null || mSearchResultsConfig.size() == 0) {
            throw new Exception("No SearchResultsConfig objects have been parsed");
        }
        Iterator iter = mSearchResultsConfig.iterator();
        while (iter.hasNext()) {
            SearchResultsConfig srcObj = (SearchResultsConfig) iter.next();
            if (srID == srcObj.getSearchResultID()) {
                return srcObj;
            }
        }
        return null;
    }
    
    /**
     * Getter for the mID attribute
     *
     * @return An integer uniquely identifying the screen represented by this
     * object.
     */
    public Integer getID() {
        return mID;
    }
    
    /**
     * Getter for the mDisplayTitle attribute
     *
     * @return The display title for the screen represented by this object.
     */
    public String getDisplayTitle() {
        return mDisplayTitle;
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
     * Getter for the mDisplayOrder attribute
     *
     * @return The order in which the title is to be displayed on the dashboard
     * for the screen represented by this object.  A value of 0 indicates it 
     * will be the leftmost title to be displayed.  A value of 1 indicates it 
     * will be the second title from the left, etc.
     */
    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    /**
     * Getter for the mEntrance attribute
     *
     * @return The JSP/JSF page to be executed when the screen represented by
     * this object is chosen from the dashboard.  This is implementation-
     * dependent on the Presentation Layer, so it remains to be defined.
     */
    public Object getEntrance() {
        return mEntrance;
    }

    /**
     * Getter for the mSearchResultsConfig attribute
     *
     * @return The ArrayList of SearchResultsConfig objects defining how the
     * fields of a search result will be displayed.
     */
    public ArrayList getSearchResultsConfig() {
        return mSearchResultsConfig;
    }

    /**
     * Getter for the mSearchScreensConfig attribute
     *
     * @return The ArrayList of SearchScreensConfig objects defining how the
     * fields of a search result will be displayed.
     */
    public ArrayList getSearchScreensConfig() {
        return mSearchScreensConfig;
    }

    /**
     * Getter for the mSubscreensConfig attribute
     *
     * @return The ArrayList of ScreenObjects objects defining the subscreens
     * of this object..
     */
    public ArrayList getSubscreensConfig() {
        return mSubscreensConfig;
    }
    
    /**
     * Setter for the mSearchScreensConfig attribute
     *
     * @param sscObj  This is an ArrayList of SearchScreenConfig objects 
     * defining how the fields of a search result will be displayed.
     */
    public void setSearchScreensConfig(ArrayList sscObjs) {
        mSearchScreensConfig = sscObjs;
    }
    
    /**
     * Setter for the mSubscreensConfig attribute
     *
     * @param subsObjs This is an ArrayList of ScreenObjects objects defining 
     * the subscreens of this object.
     */
    public void setSubscreensConfig(ArrayList subsObjs) {
        mSubscreensConfig = subsObjs;
    }
    
    /**
     * Sets the length of an EUID field.  This should be invoked after the
     * ConfigManager has successfully initialized and a connection to the 
     * MasterController has been obtained (the EUID length is obtained from
     * the MasterController).  An EUID field is any field that
     * starts with "EUID", such as "EUID", "EUID1", or "EUID2".
     *
     * @param length  This is the length of the EUID field.
     */
    public void setEuidLength(int length) {
        ArrayList sscList = getSearchScreensConfig();
        Iterator iter = sscList.iterator();
        
        while (iter.hasNext()) {
            SearchScreenConfig sscObj = (SearchScreenConfig) iter.next();
            ArrayList fConfigs = sscObj.getFieldConfigs();
            Iterator fConfigsIter = fConfigs.iterator();
            while (fConfigsIter.hasNext()) {
                FieldConfig fc = (FieldConfig) fConfigsIter.next();
                if (isEuidField(fc)) {
                    fc.setMaxLength(length);
                }
            }
        }
        ArrayList srList = getSearchResultsConfig();
        iter = srList.iterator();
        
        while (iter.hasNext()) {
            SearchResultsConfig srcObj = (SearchResultsConfig) iter.next();
            ArrayList fConfigs = srcObj.getFieldConfigs();
            Iterator fConfigsIter = fConfigs.iterator();
            while (fConfigsIter.hasNext()) {
                FieldConfig fc = (FieldConfig) fConfigsIter.next();
                if (isEuidField(fc)) {
                    fc.setMaxLength(length);
                }
            }
        }
    }
    
    /**
     * Checks if a field is an EUID field,  An EUID field is any field that
     * starts with "EUID", such as "EUID", "EUID1", or "EUID2".
     *
     * @param length  This is the length of the EUID field.
     * @returns true if the field is an EUID field, false otherwise.
     */
    private boolean isEuidField(FieldConfig fconfig) {
        String fieldName = fconfig.getDisplayName();
        if (fieldName == null || fieldName.length() == 0) {
            return false;
        }
        return (fieldName.toUpperCase().startsWith(EUID) == true);
    }
}