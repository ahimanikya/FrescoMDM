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
 * @(#)ReportUtil.java
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
 * ReportUtil class 
 *
 * @author rtam
 * @created July 27, 2007
 */
public class ReportUtil extends ScreenUtil {

    private static final String FIELD_NAME_START_DATE = "StartDate";
    private static final String FIELD_NAME_END_DATE = "EndDate";
    private static final String FIELD_NAME_START_TIME = "StartTime";
    private static final String FIELD_NAME_END_TIME = "EndTime";    
    private static final String START_DATE = "From Date";
    private static final String START_TIME = "From Time";
    private static final String END_DATE = "To Date";
    private static final String END_TIME = "To Time";
    private static final String FIELD_NAME_MAX_RESULT_SIZE = "MaxResultSize";    
    private static final String MAX_REPORT_SIZE = "Report Maximum Size";
    
    public ReportUtil() {
    }
    
    /**
     * Adds required fields for Activity report searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addActivityReportSearchFields(ObjectNodeConfig nodeConfigObj, 
						     ArrayList searchFieldList) {
                                                         
        addDateFields(nodeConfigObj, searchFieldList);
    }

    /**
     * Adds required fields for Deactivated, Merged, and Unmerged report searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param localIdDesignation  Local ID designation.
     */
    public static void addMergeReportSearchFields(ObjectNodeConfig nodeConfigObj, 
						  ArrayList searchFieldList, 
                                                  String localIdDesignation) {
        addEUIDField(nodeConfigObj, searchFieldList);
        addLIDFields(nodeConfigObj, localIdDesignation, searchFieldList);
        addDateFields(nodeConfigObj, searchFieldList);
        addTimeFields(nodeConfigObj, searchFieldList);
    }
    
    /**
     * Adds required fields for Update and Assumed Matches report searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param localIdDesignation  Local ID designation.
     */
    public static void addUpdateReportSearchFields(ObjectNodeConfig nodeConfigObj, 
						  ArrayList searchFieldList,
                                                  String localIdDesignation) {
                                                      
        addEUIDField(nodeConfigObj, searchFieldList);
        addLIDFields(nodeConfigObj, localIdDesignation, searchFieldList);
        addDateFields(nodeConfigObj, searchFieldList);
        addTimeFields(nodeConfigObj, searchFieldList);
        addMaxReportSizeField(nodeConfigObj, searchFieldList);
    }
    
    /**
     * Adds required fields for Potential Duplicate report searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param localIdDesignation  Local ID designation.
     */
    public static void addPotDupReportSearchFields(ObjectNodeConfig nodeConfigObj, 
						  ArrayList searchFieldList,
                                                  String localIdDesignation) {
                                                      
        addEUIDField(nodeConfigObj, searchFieldList);
        addLIDFields(nodeConfigObj, localIdDesignation, searchFieldList);
        addDateFields(nodeConfigObj, searchFieldList);
        addTimeFields(nodeConfigObj, searchFieldList);
        addStatusField(nodeConfigObj, searchFieldList);
        addMaxReportSizeField(nodeConfigObj, searchFieldList);
    }    

    /**
     * Add a FieldConfig object for the EUID field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addEUIDField(ObjectNodeConfig nodeConfigObj, 
                               ArrayList searchFieldList) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_EUID, EUID,
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, true, searchFieldList);
    }
    
    /**
     * Add FieldConfig objects for the SystemObject and LID fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param localIdDesignation  Local ID designation.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    public static void addLIDFields(ObjectNodeConfig nodeConfigObj, 
                              String localIdDesignation, 
                              ArrayList searchFieldList) {
                                   
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM, SYSTEM,
            FieldConfig.GUI_TYPE_MENULIST, 
            ValidationService.CONFIG_MODULE_SYSTEM, DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_LID, localIdDesignation,
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
    }
    
    /**
     * Add FieldConfig objects for the Date start and end fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    private static void addDateFields(ObjectNodeConfig nodeConfigObj, 
                                     ArrayList searchFieldList) {
                                         
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_START_DATE, 
            START_DATE, FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
            ConfigManager.getDateInputMask(), ObjectField.OBJECTMETA_DATE_TYPE, 
            false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_END_DATE, END_DATE,
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
            ConfigManager.getDateInputMask(), ObjectField.OBJECTMETA_DATE_TYPE, 
            false, searchFieldList);
    }    
    
    /**
     * Add FieldConfig objects for the Time start and end fields.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    private static void addTimeFields(ObjectNodeConfig nodeConfigObj, 
                                     ArrayList searchFieldList) {
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_START_TIME, 
            START_TIME, FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
            DATE_STRING_FORMAT, ObjectField.OBJECTMETA_TIMESTAMP_TYPE, 
            false, searchFieldList);        
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_END_TIME, END_TIME,
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
            DATE_STRING_FORMAT, ObjectField.OBJECTMETA_TIMESTAMP_TYPE, 
            false, searchFieldList);        
    }    
    
    /**
     * Add FieldConfig objects for the status field.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    private static void addStatusField(ObjectNodeConfig nodeConfigObj, 
                                       ArrayList searchFieldList) {
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_MATCHING_STATUS, 
            STATUS, FieldConfig.GUI_TYPE_MENULIST, 
            ValidationService.CONFIG_MODULE_RESOLVETYPE, DEFAULT_FIELD_LENGTH, 
            null, ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
    }    
    
    /**
     * Add FieldConfig objects for the maximum report size.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     */
    private static void addMaxReportSizeField(ObjectNodeConfig nodeConfigObj, 
                                              ArrayList searchFieldList) {
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_MAX_RESULT_SIZE, 
            MAX_REPORT_SIZE, FieldConfig.GUI_TYPE_TEXTBOX, "", 
            DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_INT_TYPE, false, searchFieldList); 
    }    
}