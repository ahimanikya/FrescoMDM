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
 * @(#)ScreenUtil.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration.util;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import java.util.Iterator;
import java.util.ArrayList;


/**
 * ScreenUtil class 
 *
 * @author rtam
 * @created July 27, 2007
 */
public class ScreenUtil  {

    protected static final String FIELD_NAME_EUID = "EUID";
    protected static final String FIELD_NAME_SYSTEM = "SystemCode";
    protected static final String FIELD_NAME_LID = "LID";
    protected static final String FIELD_NAME_MATCHING_STATUS = "Status";
    protected static final String FIELD_NAME_CREATE_DATE = "CreateDate";
    protected static final String FIELD_NAME_CREATE_START_DATE = "create_start_date";
    protected static final String FIELD_NAME_CREATE_START_TIME = "create_start_time";
    protected static final String FIELD_NAME_CREATE_END_DATE = "create_end_date";
    protected static final String FIELD_NAME_CREATE_END_TIME = "create_end_time";
    protected static final String FIELD_NAME_SYSTEM_USER = "SystemUser";
    protected static final String FIELD_NAME_TIME_STAMP = "TimeStamp";
    protected static final String EUID = "EUID";
    protected static final String SYSTEM = "System";
    protected static final String DATE_STRING_FORMAT = "DD:DD:DD";
    protected static final String STATUS = "Status";
    protected static final String TIMESTAMP = "Timestamp";
    protected static final String CREATE_START_DATE = "Create Date From";
    protected static final String CREATE_START_TIME = "Create Time From";
    protected static final String CREATE_END_DATE = "To Create Date";
    protected static final String CREATE_END_TIME = "To Create Time";
    protected static final int DEFAULT_FIELD_LENGTH = 32;
    
    public static final int SEARCH_SCREEN = 0;
    public static final int RESULT_SCREEN = 1;

    public ScreenUtil() {
    }

    /**
     * Add a FieldConfig object for the EUID field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addEUID(ObjectNodeConfig nodeConfigObj, 
                               ArrayList searchFieldList) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_EUID, EUID,
                       FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
                       null, ObjectField.OBJECTMETA_STRING_TYPE, true, 
                       searchFieldList);
    }
    
    /**
     * Add FieldConfig objects for the SystemObject and LID fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param localIdDesignation  Local ID designation.
     * @param fieldList This an ArrayList of FieldConfig objects
     * representing the search or search result fields.
     * @param screenType This identifies if the screen is a search or 
     * result screen.  The screen type affects the display type for
     * result screen.  The System field would normally be a menulist
     * in a search screen, but it would be a text box in a result screen.
     */
    public static void addLID(ObjectNodeConfig nodeConfigObj, 
                              String localIdDesignation, 
                              ArrayList fieldList,
                              int screenType) {
                                   
        if (screenType == SEARCH_SCREEN) {
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM, SYSTEM,
                           FieldConfig.GUI_TYPE_MENULIST, 
                           ValidationService.CONFIG_MODULE_SYSTEM, 
                           DEFAULT_FIELD_LENGTH, null, 
                           ObjectField.OBJECTMETA_STRING_TYPE, false, fieldList);
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_LID, localIdDesignation,
                           FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
                           null, ObjectField.OBJECTMETA_STRING_TYPE, false, fieldList);
        } else {
            addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM,
                                 SYSTEM, FieldConfig.GUI_TYPE_TEXTBOX, 
                                 DEFAULT_FIELD_LENGTH, 
                                 ObjectField.OBJECTMETA_STRING_TYPE, 
                                 false, fieldList);
            addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_LID, 
                                 localIdDesignation, FieldConfig.GUI_TYPE_TEXTBOX, 
                                 DEFAULT_FIELD_LENGTH, 
                                 ObjectField.OBJECTMETA_STRING_TYPE, 
                                 false, fieldList);
        }
    }
    
    /**
     * Add FieldConfig objects for the Create Date start and end fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addCreateDate(ObjectNodeConfig nodeConfigObj, 
                                     ArrayList searchFieldList,
                                     int screenType) {
                                   
        if (screenType == SEARCH_SCREEN) {
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_START_DATE,
                           CREATE_START_DATE, FieldConfig.GUI_TYPE_TEXTBOX, "", 
                           DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
                           ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_END_DATE,
                           CREATE_END_DATE, FieldConfig.GUI_TYPE_TEXTBOX, "", 
                           DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
                           ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
        } else {
            addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_CREATE_DATE,
                                 CREATE_START_DATE, FieldConfig.GUI_TYPE_TEXTBOX,
                                 DEFAULT_FIELD_LENGTH, 
                                 ObjectField.OBJECTMETA_DATE_TYPE, false, 
                                 searchFieldList);
        }
    }

    /**
     * Add FieldConfig objects for the Create Time start and end fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param screenType This identifies if the screen is a search or 
     * result screen.  For a search screen, two fields would be added: 
     * "Create Time From" and "To Create Time".  For a result screen,
     *  one field would be added: "Create Time".
     */
    public static void addCreateTime(ObjectNodeConfig nodeConfigObj, 
                                     ArrayList searchFieldList,
                                     int screenType) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_START_TIME,
                       CREATE_START_TIME, FieldConfig.GUI_TYPE_TEXTBOX, "", 
                       DEFAULT_FIELD_LENGTH, DATE_STRING_FORMAT,
                       ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, 
                       searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_END_TIME,
                       CREATE_END_TIME, FieldConfig.GUI_TYPE_TEXTBOX, "", 
                       DEFAULT_FIELD_LENGTH, DATE_STRING_FORMAT,
                       ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, 
                       searchFieldList);
    }

    /**
     * Add a FieldConfig object for the Status field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param fieldList This an ArrayList of FieldConfig objects
     * representing the search or search result fields.
     * @param screenType This identifies if the screen is a search or 
     * result screen.  The Status field would normally be a menulist
     * in a search screen, but it would be a text box in a result screen.
     */
    public static void addStatus(ObjectNodeConfig nodeConfigObj, 
                               ArrayList fieldList,
                               int screenType) {
                                   
        if (screenType == SEARCH_SCREEN) {
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_MATCHING_STATUS,
                           STATUS, FieldConfig.GUI_TYPE_MENULIST, 
                           ValidationService.CONFIG_MODULE_RESOLVETYPE, 
                           DEFAULT_FIELD_LENGTH, null, 
                           ObjectField.OBJECTMETA_STRING_TYPE, false, fieldList);
        } else {
            addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_MATCHING_STATUS,
                                 STATUS, FieldConfig.GUI_TYPE_TEXTBOX, 
                                 DEFAULT_FIELD_LENGTH, 
                                 ObjectField.OBJECTMETA_STRING_TYPE, false, 
                                 fieldList);
        }
    }

    /**
     * Add a FieldConfig object for the System User field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addSystemUser(ObjectNodeConfig nodeConfigObj, 
                                     ArrayList searchFieldList) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM_USER,
                       STATUS, FieldConfig.GUI_TYPE_TEXTBOX, "", 
                       DEFAULT_FIELD_LENGTH, null, 
                       ObjectField.OBJECTMETA_STRING_TYPE, false, 
                       searchFieldList);
    }
    
    /**
     * Add a FieldConfig object for the Operation field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addOperation(ObjectNodeConfig nodeConfigObj, 
                                    ArrayList searchFieldList) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_MATCHING_STATUS,
                       STATUS, FieldConfig.GUI_TYPE_MENULIST, 
                       ValidationService.CONFIG_MODULE_FUNCTION, 
                       DEFAULT_FIELD_LENGTH, null, 
                       ObjectField.OBJECTMETA_STRING_TYPE, false, 
                       searchFieldList);
    }
    
    
    /**
     * Add a FieldConfig object for the Timetamp field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addTimestamp(ObjectNodeConfig nodeConfigObj, 
                                    ArrayList searchFieldList) {

        addSearchField(nodeConfigObj.getName(), FIELD_NAME_TIME_STAMP,
            TIMESTAMP, FieldConfig.GUI_TYPE_TEXTBOX, 
            null, DEFAULT_FIELD_LENGTH,
            null, ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
    }
    
    /**
     * Adds a FieldConfig object for the search fields.
     *
     * @param rootObjectName  This is the ObjectNodeConfig instance to which 
     * the search field belongs.
     * @param fieldName  This is the name for the field
     * @param displayName  This is the display name for the field.
     * @param guiType  This is the GUI type for the field.
     * @param valueListName  This is the value list name for the field.
     * @param length    This is the length or the field.  For EUID fields, this 
     * should be changed once the ConfigManager has been initialized and a
     * connection to the MasterController has been obtained.
     * @param inputMask  This is the input mask for the field.
     * @param objectFieldType  This is the ObjectField type for the field.
     * @param isEuidField This indicates if this is an EUID field.
     * @param searchFieldList This indicates where the new FieldConfig objects will
     * be stored
     */
    
    protected static void addSearchField(String rootObjectName, String fieldName,
                                        String displayName, String guiType, 
                                        String valueListName, int length,
                                        String inputMask, int objectFieldType, 
                                        boolean isEuidField, 
                                        ArrayList searchFieldList) { 
                                     
        FieldConfig fieldConfig = new FieldConfig(rootObjectName, fieldName,
                displayName, guiType, length, objectFieldType);
        fieldConfig.setDisplayOrder(0);
        fieldConfig.setKeyType(false);

        if (guiType.equals(FieldConfig.GUI_TYPE_MENULIST)) {
            fieldConfig.setValueList(valueListName);
        }
        fieldConfig.setInputMask(inputMask);

        searchFieldList.add(fieldConfig);
        // testing--raymond tam
        // resume here.  What is this used for?  Recalculating EUID length?
//        
//        if (isEuidField) {
//            euidFields.add(fieldConfig);
//        }

    }
    
    /**
     * Adds a FieldConfig object for the search results fields.
     *
     * @param rootObjectName  This is the ObjectNodeConfig instance to which 
     * the search field belongs.
     * @param fieldName  This is the name for the field
     * @param displayName  This is the display name for the field.
     * @param guiType  This is the GUI type for the field.
     * @param length    This is the length or the field.  For EUID fields, this 
     * should be changed once the ConfigManager has been initialized and a
     * connection to the MasterController has been obtained.
     * @param objectFieldType  This is the ObjectField type for the field.
     * @param isEuidField This indicates if this is an EUID field.
     * @param resultFieldList This indicates where the new FieldConfig objects will
     * be stored.
     */
    
    protected static void addSearchResultField(String rootObjectName, String fieldName,
                                                String displayName, String guiType, 
                                                int length, int objectFieldType, 
                                                boolean isEuidField, 
                                                ArrayList resultFieldList) {
                                           
        FieldConfig fieldConfig = new FieldConfig(rootObjectName, fieldName,
                displayName, guiType, length, objectFieldType);
        fieldConfig.setDisplayOrder(0);
        fieldConfig.setKeyType(false);
        resultFieldList.add(fieldConfig);
        
        // testing--raymond tam
        // resume here.  What is this used for?  Recalculating EUID length?
//        
//        if (isEuidField) {
//            euidFields.add(fieldConfig);
//        }
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
/*
    public void setEuidLength(int length) {
        ArrayList sscList = super.getSearchScreensConfig();
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
    }
*/    
    /**
     * Checks if a field is an EUID field,  An EUID field is any field that
     * starts with "EUID", such as "EUID", "EUID1", or "EUID2".
     *
     * @param length  This is the length of the EUID field.
     * @returns true if the field is an EUID field, false otherwise.
     */
/*
    private boolean isEuidField(FieldConfig fconfig) {
        String fieldName = fconfig.getDisplayName();
        if (fieldName == null || fieldName.length() == 0) {
            return false;
        }
        return (fieldName.toUpperCase().startsWith(EUID) == true);
    }
*/    
}