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

public class ScreenObject {
    private Integer mId;                    // screen ID
    private String mDisplayTitle;           // title to be displayed 
    private String mViewPath;
    private int mDisplayOrder;		    // order in which the screen is displayed
    private int mInitialSubScreenID;        
    private int mPageSize;
    private int mMaxRecords;
            
    private ArrayList<ScreenObject> mSubscreens;
    private ArrayList<SearchScreenConfig> mSearchScreenConfigs;
    
    public ScreenObject() {
        mSubscreens = new ArrayList<ScreenObject> ();
    }
    
    public ScreenObject(Integer id, String displayTitle, String viewPath, 
                        int displayOrder, int pageSize, int maxRecords,
                        ArrayList<SearchScreenConfig> searchScreenConfigs, 
                        ArrayList<ScreenObject> subscreens) {
        mId = id;
        mDisplayTitle = displayTitle;
        mViewPath = viewPath;
        mDisplayOrder = displayOrder;
        mPageSize = pageSize;
        mMaxRecords = maxRecords;
        mSearchScreenConfigs = searchScreenConfigs;
        mSubscreens = subscreens;
    }
    
    public ScreenObject(Integer id, String displayTitle, String viewPath, 
                        int displayOrder, int initialSubscreenID, 
                        int pageSize, int maxRecords,
                        ArrayList<SearchScreenConfig> searchScreenConfigs, 
                        ArrayList<ScreenObject> subscreens) {
        mId = id;
        mDisplayTitle = displayTitle;
        mViewPath = viewPath;
        mDisplayOrder = displayOrder;
        mPageSize = pageSize;
        mMaxRecords = maxRecords;
        mInitialSubScreenID = initialSubscreenID;
        mSearchScreenConfigs = searchScreenConfigs;
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
     * Retrieves the screen object's view path.
     *
     * @return the screen object's view path.
     */    
    public String getViewPath() {  
        return mViewPath;
    }

    /**
     * Retrieves the screen object's view path.
     *
     * @param viewPath  The screen object's view path.
     */    
    public void setViewPath(String viewPath) {  
        mViewPath = viewPath;
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
     * Retrieves the ID of the initial subscreen
     *
     * @return the ID of the initial subscreen
     */    
    public int getInitialSubscreenID() {
        return mInitialSubScreenID;
    }
	
    /**
     * Sets the ID of the initial subscreen
     *
     * @param screenID the ID of the initial subscreen
     */    
    public void getInitialSubscreenID(int screenID) {
        mInitialSubScreenID =  screenID;
    }
	
    /**
     * Retrieves the maximum items per page
     *
     * @return the maximum items per page
     */    
    public int getPageSize() {
        return mPageSize;
    }
	
    /**
     * Sets the maximum items per page
     *
     * @param pageSize Maximum number of items per page.
     */    
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }
	
    /**
     * Retrieves the maximum number of records to retrieve from the database.
     * 
     * @return the maximum number of records to retrieve from the database.
     */    
    public int getMaxRecords() {
        return mMaxRecords;
    }
	
    /**
     * Sets the maximum number of records to retrieve from the database.
     * 
     * @param maxRecords  The maximum number of records to retrieve from the database.
     */    
    public void getMaxRecords(int maxRecords) {
        mMaxRecords = maxRecords;
    }
	
    /**
     * Retrieves all the search configurations for this screen object.
     *
     * @return all the search configurations for this screen object.
     */	
    public ArrayList<SearchScreenConfig> getSearchScreenConfigs() {  
        return mSearchScreenConfigs;
    }

    /**
     * Sets the search configurations for this screen object.
     *
     * @param searchScreenConfigs  The search configurations for this screen object.
     */	
    public void setSearchScreenConfigs(ArrayList<SearchScreenConfig> searchScreenConfigs) {  
        mSearchScreenConfigs = searchScreenConfigs;
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
