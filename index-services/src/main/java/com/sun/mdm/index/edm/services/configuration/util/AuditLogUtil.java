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
 * @(#)AuditLogUtil.java
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
 * AuditLogUtil class 
 *
 * @author rtam
 * @created July 27, 2007
 */
public class AuditLogUtil extends ScreenUtil {
    
    private static final String FIELD_NAME_AL_EUID1 = "EUID1";
    private static final String FIELD_NAME_AL_EUID2 = "EUID2";
    private static final String FIELD_NAME_AUDIT_ID = "AUDITID";
    private static final String FIELD_NAME_DETAIL = "Detail";
    private static final String FIELD_NAME_TYPE = "PrimaryObjectType";
    private static final String FIELD_NAME_FUNCTION = "Function";
    private static final String FIELD_NAME_START_DATE = "StartDate";
    private static final String FIELD_NAME_START_TIME = "StartTime";
    private static final String FIELD_NAME_END_DATE = "EndDate";
    private static final String FIELD_NAME_END_TIME = "EndTime";
    private static final String FIELD_NAME_CREATE_USER = "CreateUser";
    private static final String FIELD_NAME_CREATE_DATE = "CreateDate";
    
    public AuditLogUtil() {
    }

    /**
     * Adds required fields for Audit Log searches.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param searchFieldList This an ArrayList of FieldConfig objects
     * representing the search fields.
     * @param localIdDesignation  Local ID designation.
     * @param addEUIDSearchField  This indicates if an EUID search field is to
     * be added.
     * @param addLIDSearchField  This indicates if LID and System Code search 
     * fields are to be added.
     */
    public static void addAuditLogSearchFields(ObjectNodeConfig nodeConfigObj, 
                                               ArrayList searchFieldList,
                                               String localIdDesignation,
                                               boolean addEUIDSearchField,
                                               boolean addLIDSearchField) {
    
        if (addEUIDSearchField == true) {
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_EUID, "EUID",
                FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
                ObjectField.OBJECTMETA_STRING_TYPE, true, searchFieldList);
        }
        
        if (addLIDSearchField == true) {
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM, SYSTEM,
                           FieldConfig.GUI_TYPE_MENULIST, 
                           ValidationService.CONFIG_MODULE_SYSTEM, 
                           DEFAULT_FIELD_LENGTH, null, 
                           ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
            addSearchField(nodeConfigObj.getName(), FIELD_NAME_LID, localIdDesignation,
                           FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, 
                           null, ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
        }
        
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_START_DATE, "From Date",
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
            ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_END_DATE, "To Date",
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, ConfigManager.getDateInputMask(),
            ObjectField.OBJECTMETA_DATE_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_START_TIME, "From Time",
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, "DD:DD:DD",
            ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_END_TIME, "To Time",
            FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, "DD:DD:DD",
            ObjectField.OBJECTMETA_TIMESTAMP_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_SYSTEM_USER,
            "System User", FieldConfig.GUI_TYPE_TEXTBOX, "", DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
        addSearchField(nodeConfigObj.getName(), FIELD_NAME_FUNCTION, "Function",
            FieldConfig.GUI_TYPE_MENULIST, 
        ValidationService.CONFIG_MODULE_AUDIT_FUNCTION, DEFAULT_FIELD_LENGTH, null,
            ObjectField.OBJECTMETA_STRING_TYPE, false, searchFieldList);
		//added CONFIG_MODULE_AUDIT_FUNCTION to get the audit log fields.
    }
    
    /**
     * Adds required fields for Audit Log search results.
     *
     * @param nodeConfigObj  The ObjectNodeConfig instance.
     * @param fieldConfigGroup This an ArrayList of FieldConfigGroup objects
     * representing the result fields.
     * @throws Exception if an error is encountered.
     */
    public static void addAuditLogResultFields(ObjectNodeConfig nodeConfigObj, 
                                               ArrayList fieldConfigGroup) 
            throws Exception{
                                                   
        ArrayList resultFieldList = new ArrayList();
        
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_AUDIT_ID,
            "Audit ID", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_AL_EUID1,
            "EUID1", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, true, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_AL_EUID2,
            "EUID2", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, true, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_TYPE, "Type",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_FUNCTION,
            "Function", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_DETAIL, "Detail",
            FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH, 
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_CREATE_USER,
            "Create User", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_STRING_TYPE, false, resultFieldList);
        addSearchResultField(nodeConfigObj.getName(), FIELD_NAME_CREATE_DATE,
            "Create Date & Time", FieldConfig.GUI_TYPE_TEXTBOX, DEFAULT_FIELD_LENGTH,
            ObjectField.OBJECTMETA_DATE_TYPE, false, resultFieldList);
        
        EosFieldGroupConfig fgconfig = new EosFieldGroupConfig(null, nodeConfigObj);
        FieldConfig[] fconfigs = fgconfig.getFieldConfigs();
        FieldConfigGroup fgc = new FieldConfigGroup(null, resultFieldList);
        fieldConfigGroup.add(fgc);
    }
}