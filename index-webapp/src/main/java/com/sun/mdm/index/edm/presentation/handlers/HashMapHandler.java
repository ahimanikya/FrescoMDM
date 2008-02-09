/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
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

     
/*
 * HashMapHandler.java 
 * Created on September 21, 2007, 11:58 AM
 * Author : RajaniKanth
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.NonUpdateableFields;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.faces.event.*;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;


import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;

public class HashMapHandler {
    private HashMap updateableFeildsMap =  new HashMap();    
    private ArrayList nonUpdateableFieldsArray = new ArrayList();    
    private HashMap actionMap =  new HashMap();    
    private ArrayList fieldConfigArray;
    private ArrayList headerScreenObjectsArray = new ArrayList();
    private ArrayList screenConfigArray;
    private  static final String ADDEUID="success";
    
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.HashMapHandler");

    // Create fields for non updateable fields as per screen config array
    private String EUID;
    private String SystemCode;
    private String LID;
    private String create_start_date;
    private String create_end_date;
    private String create_start_time;
    private String create_end_time;
    private String Status;
    
    private boolean mShowEUID;      // indicates if the EUID should be displayed
    private boolean mShowLID;       // indicates if the LID should be displayed
    private boolean mShowCreateDate; // indicates if the Create Date should be displayed
    private boolean mShowCreateTime; // indicates if the Create Time should be displayed
    private boolean mShowStatus;    // indicates if the Status should be displayed

    
    
    /** Creates a new instance of SourceHandler */
    public HashMapHandler() {
        try {
            ConfigManager.init();
            screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();
            
            Iterator iteratorScreenConfig = screenConfigArray.iterator();
            
            while (iteratorScreenConfig.hasNext()) {
                  SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                  this.setMShowEUID(objSearchScreenConfig.getShowEUID());
                  this.setMShowLID(objSearchScreenConfig.getShowLID());
                  this.setMShowCreateDate(objSearchScreenConfig.getShowCreateDate());
                  this.setMShowCreateTime(objSearchScreenConfig.getShowCreateTime());
                  this.setMShowStatus(objSearchScreenConfig.getShowStatus());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }   
        
    }

    
    public String performSubmit() throws ProcessingException, UserException {
        Iterator fieldConfigArrayIter =  this.getFieldConfigArray().iterator();
        while(fieldConfigArrayIter.hasNext())  {
             FieldConfig  fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
        }       
        return this.ADDEUID;
    }

    public ArrayList getFieldConfigArray() {
        ArrayList basicSearchFieldConfigs = null;
        ArrayList advancedSearchFieldConfigs = null;
        ArrayList fieldConfigs = null;
        try {
            ConfigManager.init();
            screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();           
            Iterator iteratorScreenConfig = screenConfigArray.iterator();            
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                
                if("Advanced Search".equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        
                        //Build array of field config groups from in the basic search screen
                        FieldConfigGroup basicSearchFieldGroup  =  (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        
                        //Build array of field configs from in the basic search screen
                        fieldConfigArray = basicSearchFieldGroup.getFieldConfigs();
                    }
                    
                }  else if("Advanced Search".equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())){
                    // Get an array list of field config groups
                    advancedSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                } else {
                    // Get an array list of field config groups
                    fieldConfigs = objSearchScreenConfig.getFieldConfigs();
                }
            }            
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        
        return fieldConfigArray;
    }

    public void setFieldConfigArray(ArrayList fieldConfigArray) {
        this.fieldConfigArray = fieldConfigArray;
    }

    public ArrayList getHeaderScreenObjectsArray() {
        try  {
            ConfigManager.init();
            ScreenObject  screenObject;
            ArrayList arrayList = new ArrayList();
            for(int i=1 ; i<9 ; i++) {
                headerScreenObjectsArray.add(ConfigManager.getInstance().getScreen(new Integer(i)));
            }
            //headerScreenObjectsArray = arrayList;
        } catch(Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        
        return headerScreenObjectsArray;
    }

    public void setHeaderScreenObjectsArray(ArrayList headerScreenObjectsArray) {
        this.headerScreenObjectsArray = headerScreenObjectsArray;
    }

    public HashMap getActionMap() {
        return actionMap;
    }

    public void setActionMap(HashMap actionMap) {
        this.actionMap = actionMap;
    }

    public ArrayList getScreenConfigArray() {
        try {
            
            ConfigManager.init();
            screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();
                        
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Config Array Object: ", e);
        }

        return screenConfigArray;
    }

    public void setScreenConfigArray(ArrayList screenConfigArray) {
        this.screenConfigArray = screenConfigArray;
    }

    
    
    // Getter and setter methods for non updateable fields as per screen config array

    public String getEUID() {
        return EUID;
    }

    public void setEUID(String EUID) {
        this.EUID = EUID;
    }

    public String getSystemCode() {
        return SystemCode;
    }

    public void setSystemCode(String SystemCode) {
        this.SystemCode = SystemCode;
    }

    public String getLID() {
        return LID;
    }

    public void setLID(String LID) {
        this.LID = LID;
    }

    public String getCreate_start_date() {
        return create_start_date;
    }

    public void setCreate_start_date(String create_start_date) {
        this.create_start_date = create_start_date;
    }

    public String getCreate_end_date() {
        return create_end_date;
    }

    public void setCreate_end_date(String create_end_date) {
        this.create_end_date = create_end_date;
    }

    public String getCreate_start_time() {
        return create_start_time;
    }

    public void setCreate_start_time(String create_start_time) {
        this.create_start_time = create_start_time;
    }

    public String getCreate_end_time() {
        return create_end_time;
    }

    public void setCreate_end_time(String create_end_time) {
        this.create_end_time = create_end_time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public ArrayList getNonUpdateableFieldsArray() {
        try {
            ArrayList basicSearchFieldConfigs;
            ConfigManager.init();
            screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();

            Iterator iteratorScreenConfig = screenConfigArray.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();

                if ("Advanced Search".equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups from in the basic search screen
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        //Build array of field configs from in the basic search screen
                        fieldConfigArray = basicSearchFieldGroup.getFieldConfigs();
                        Iterator fieldConfigArrayIter = fieldConfigArray.iterator();
                        while (fieldConfigArrayIter.hasNext()) {
                            //Build array of field config groups from in the basic search screen
                            FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
                            if(!fieldConfig.isUpdateable())nonUpdateableFieldsArray.add(fieldConfig);
                        }
                    }
                }

            }
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return nonUpdateableFieldsArray;
    }
    public void setNonUpdateableFieldsArray(ArrayList nonUpdateableFieldsArray) {
        this.nonUpdateableFieldsArray = nonUpdateableFieldsArray;
    }

    public boolean isMShowEUID() {
        return mShowEUID;
    }

    public void setMShowEUID(boolean mShowEUID) {
        this.mShowEUID = mShowEUID;
    }

    public boolean isMShowLID() {
        return mShowLID;
    }

    public void setMShowLID(boolean mShowLID) {
        this.mShowLID = mShowLID;
    }

    public boolean isMShowCreateDate() {
        return mShowCreateDate;
    }

    public void setMShowCreateDate(boolean mShowCreateDate) {
        this.mShowCreateDate = mShowCreateDate;
    }

    public boolean isMShowCreateTime() {
        return mShowCreateTime;
    }

    public void setMShowCreateTime(boolean mShowCreateTime) {
        this.mShowCreateTime = mShowCreateTime;
    }

    public boolean isMShowStatus() {
        return mShowStatus;
    }

    public void setMShowStatus(boolean mShowStatus) {
        this.mShowStatus = mShowStatus;
    }

    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    
}
