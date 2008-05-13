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
 * @(#)AssumedMatchesUtil.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration.util;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.configuration.EosFieldGroupConfig;
import java.util.Iterator;
import java.util.ArrayList;


/**
 * AssumedMatchesUtil class 
 *
 * @author rtam
 * @created July 27, 2007
 */
public class AssumedMatchesUtil extends ScreenUtil  {
    
    private static final String FIELD_NAME_ID = "ID";
    private static final String FIELD_NAME_WEIGHT = "Weight";
    private static final String FIELD_NAME_CREATE_USER = "CreateUser";
    private static final String FIELD_NAME_CREATE_DATE = "CreateDate";
    
    public AssumedMatchesUtil() {
    }

    /**
     * Adds required fields for Assumed Match searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param localIdDesignation This is the local ID designation.
     */
    public static void addAssumedMatchesSearchFields(ObjectNodeConfig nodeConfigObj,
                                                     ArrayList searchFieldList,
                                                     String localIdDesignation) {
    
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_EUID, "EUID",
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, true, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM, "System",
            FieldConfig.GUI_TYPE_MENULIST, 
            ValidationService.CONFIG_MODULE_SYSTEM, DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_LID, localIdDesignation,
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_START_DATE,
            "Create Date From", FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
            ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_START_TIME,
            "Create Time From", FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, "DD:DD:DD",
            ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_END_DATE,
            "To Create Date", FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
            ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_CREATE_END_TIME,
            "To Create Time", FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, "DD:DD:DD",
            ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, searchFieldList);
    }
    
    /**
     * Adds required fields for Assumed Match search results.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param fieldConfigGroup This an ArrayList of FieldConfigGroup objects
     * representing the result fields.
     * @param localIdDesignation This is the local ID designation.
     * @throws Exception if an error is encountered.
     */
    public static void addAssumedMatchResultFields(ObjectNodeConfig nodeConfigObj, 
                                                  ArrayList fieldConfigGroup,
                                                  String localIdDesignation) 
            throws Exception{
    
        ArrayList resultFieldList = new ArrayList();
        
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_ID, "ID",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_EUID, "EUID",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, true, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_WEIGHT, "Weight",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM, "System",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_LID, 
            localIdDesignation, FieldConfig.GUI_TYPE_TEXTBOX, 
            DEFAULT_FIELD_LENGTH, ObjectField.OBJECTMETA_STRING_TYPE, false, 
            resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_CREATE_USER,
            "Create User", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_CREATE_DATE,
            "Create Date", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_DATE_TYPE, false, resultFieldList);
        
        EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(null, nodeConfigObj);
        FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
        FieldConfigGroup fgc = new FieldConfigGroup(null, resultFieldList);
        fieldConfigGroup.add(fgc);
    }
}