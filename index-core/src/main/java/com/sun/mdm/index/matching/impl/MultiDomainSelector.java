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
package com.sun.mdm.index.matching.impl;
import com.sun.mdm.index.matching.DomainSelector;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.SystemObjectField;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author  rtam
 */
public class MultiDomainSelector implements DomainSelector {
    
    private String LOCALE_US = "US";
    private String DEFAULT_LOCALE_TAG = "Default";
    private String defaultLocale = null;

    /*
     * Retrieve the domains for an unparsed field group.  
     *
     * @param sysObj  system object to standardize
     * @param fieldGroup  unparsed field group
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    public String[] getDomains(SystemObject sysObj, UnparsedFieldGroup fieldGroup, 
                               ArrayList allColumns) {
        return getDomains(sysObj, allColumns, fieldGroup);
    }
    
    /*
     * Retrieve the domains for a parsed field group.  
     *
     * @param sysObj  system object to standardize
     * @param fieldGroup  parsed field group
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    public String[] getDomains(SystemObject sysObj, PreparsedFieldGroup fieldGroup, 
                               ArrayList allColumns) {
        return getDomains(sysObj, allColumns, fieldGroup);
    }
    
    /*
     * Retrieve the domains for a parsed field group.  This one defaults to "US"
     * for backwards-compatibility reasons.
     *
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    private String[] getDomains(ArrayList allColumns) {
        ArrayList column = (ArrayList) allColumns.get(0);
        if (column == null) {
            return null;
        }
        String[] retVals = new String[column.size()];
	for (int i=0; i < column.size(); i++) {
            retVals[i] = "US";
        }
        return retVals;
    }
    
    /*
     * Retrieve the domains for an unparsed field group.  
     *
     * @param objToStandardize  system object to standardize
     * @param fieldGroup  parsed field group
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    private String[] getDomains(SystemObject objToStandardize, ArrayList allColumns, 
                                UnparsedFieldGroup fieldGroup) {
                                    
        ArrayList column = (ArrayList) allColumns.get(0);
        if (column == null) {
            return null;
        }
            
        // retrieve locale field and hash map
        SystemObjectField localeField = fieldGroup.getLocaleField();
        HashMap localeMappings = fieldGroup.getLocales();
        ArrayList localeFieldValue = new ArrayList();
        String domain = null;
        String[] retVals = new String[column.size()];
        
        if (localeField != null) {
            //  check if the locale field comes from the same object
            //  as the rest
            if (checkLocale(localeField, fieldGroup) == true) {
                try {
                    EPath aLocaleFieldEPath = localeField.getEPath();
                    EPathAPI.getFieldList(aLocaleFieldEPath, 0, 
                                          objToStandardize.getObject(), 
                                          localeFieldValue);
                } catch (Exception e) {
                    localeFieldValue = null;    //  check this later.  shouldn't occur
                }
            } else {
                domain = mapLocale(localeMappings, null);
                for (int i = 0; i < column.size(); i++) {
                    retVals[i] = domain;
                }
                return retVals;
            }
        }
        
        if (localeFieldValue != null && localeFieldValue.size() > 0) {
            int localeCount = localeFieldValue.size();
            for (int i = 0; i < localeCount; i++) {
                Object obj = localeFieldValue.get(i);
                String localeValue = null;
                if (obj != null) {
                    localeValue = obj.toString();
                }
                domain = mapLocale(localeMappings, localeValue);
                retVals[i] = domain;
            }
        } else {
            //  if the locale Field was not specified, then assign the 
            //  default domain for all objects
            domain = mapLocale(localeMappings, null);
            for (int i = 0; i < column.size(); i++) {
                retVals[i] = domain;
            }
        }
        return retVals;
    }
    
    /*
     * Check if the locale field and the field group are from the same object.
     * LocaleField needs to be checked for null before this is called.  This is
     * for unparsed groups.
     *
     * @param localeField  field where the locale information is stored, 
     * if applicable.
     * @param fieldGroup  unparsed field group
     * @return an array on strings containing valid domains
     */
    private boolean checkLocale(SystemObjectField localeField, 
                                UnparsedFieldGroup fieldGroup) {
          
        String path1 = localeField.getEPath().getLastChildPath();
        ArrayList sourceFields = fieldGroup.getSourceFields();
        if (sourceFields.size() == 0) {
            return false;
        }
        Iterator sourceFieldsIter = sourceFields.iterator();
        while (sourceFieldsIter.hasNext()) {
            SystemObjectField field = 
                (SystemObjectField) sourceFieldsIter.next();
            try {
                String aFieldName = field.getQualifiedName();
                String path2 = field.getEPath().getLastChildPath();
                if (path2.equalsIgnoreCase(path1) == false) { 
                    return false;
                }
            } catch (Exception e) {
                return false;       //  if there are any errors.
            }
        }        
        return true;
    }
    
    /*
     * Check if the locale field and the field group are from the same object.
     * LocaleField needs to be checked for null before this is called.  This is 
     * for preparsed groups.
     *
     * @param objToStandardize  system object to standardize
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    private String[] getDomains(SystemObject objToStandardize, ArrayList allColumns, 
                                PreparsedFieldGroup fieldGroup) {
        
        ArrayList column = (ArrayList) allColumns.get(0);
        if (column == null) {
            return null;
        }
            
        // retrieve locale field and hash map
        SystemObjectField localeField = fieldGroup.getLocaleField();
        HashMap localeMappings = fieldGroup.getLocales();
        ArrayList localeFieldValue = new ArrayList();
        String domain = null;
        String[] retVals = new String[column.size()];
        
        if (localeField != null) {
            try {
                EPath aLocaleFieldEPath = localeField.getEPath();
                EPathAPI.getFieldList(aLocaleFieldEPath, 0, 
                                      objToStandardize.getObject(), 
                                      localeFieldValue);
            } catch (Exception e) {
                localeFieldValue = null;    //  check this later.  shouldn't occur
            }
        }
        
        if (localeFieldValue != null && localeFieldValue.size() > 0) {
            //  check if the locale field comes from the same object
            //  as the rest
            if (checkLocale(localeField, fieldGroup) == true) {
                int localeCount = localeFieldValue.size();
                for (int i = 0; i < localeCount; i++) {
                    Object obj = localeFieldValue.get(i);
                    String localeValue = null;
                    if (obj != null) {
                        localeValue = obj.toString();
                    }
                    domain = mapLocale(localeMappings, localeValue);
                    retVals[i] = domain;
                }
            } else {
                domain = mapLocale(localeMappings, null);
                for (int i = 0; i < column.size(); i++) {
                    retVals[i] = domain;
                }
                return retVals;
            }
        } else {
            //  if the locale Field was not specified, then assign the 
            //  default domain for all objects
            domain = mapLocale(localeMappings, null);
            for (int i = 0; i < column.size(); i++) {
                retVals[i] = domain;
            }
        }
        return retVals;
    }

    /*
     * Check if the locale field and the field group are from the same object.
     * LocaleField needs to be checked for null before this is called. This is 
     * for preparsed groups.
     *
     * @param objToStandardize  system object to standardize
     * @param allColumns  contains all values that may need domain(s)
     * @return an array on strings containing valid domains
     */
    private boolean checkLocale(SystemObjectField localeField, 
                                PreparsedFieldGroup fieldGroup) {
          
        String path1 = localeField.getEPath().getLastChildPath();
        LinkedHashMap sourceFields = fieldGroup.getSourceFieldsDirectMap();
        if (sourceFields.size() == 0) {
            return false;
        }
        
        LinkedHashMap sourceFieldsDirectMap = 
            fieldGroup.getSourceFieldsDirectMap();
        Iterator sourceFieldsIter = 
            sourceFieldsDirectMap.entrySet().iterator();
        while (sourceFieldsIter.hasNext()) {
            Entry entry = (Entry) sourceFieldsIter.next();
            SystemObjectField sourceField = 
                (SystemObjectField) entry.getKey();

            // Get the epath information
            try {
                String aFieldName = sourceField.getQualifiedName();
                EPath aFieldEPath = sourceField.getEPath();
                String path2 = aFieldEPath.getLastChildPath();
                if (path2.equalsIgnoreCase(path1) == false) { 
                    return false;
                }
            } catch (Exception e) {
                return false;       //  if there are any errors.
            }
        }        
        return true;
    }

    /*
     * Map a locale to a country name.
     *
     * @param localeMaps  hash table containing athe localemaps.
     * @param localeValue  EPath of ID that you wish to check
     * @return an array on strings containing valid domains
     */
    
    private String mapLocale(HashMap localeMaps, String localeValue) {
        //  Check the locale map for a mapped locale.
        //  If it does not exist, then use the user-specified Default mapping.
        //  If that one does not exist, then use LOCALE_US
        String locale = null;
        if (localeMaps == null || localeMaps.isEmpty()) {
            locale = LOCALE_US;
        } else {
            Object obj = localeMaps.get(localeValue);
            if (obj != null) {
                locale = (String) obj;
            } else {
                //  retrieve the default locale just once
                if (defaultLocale == null) {
                    obj = localeMaps.get(DEFAULT_LOCALE_TAG);
                    if (obj != null) {
                        locale = (String) obj;
                    } else {
                        locale = LOCALE_US;
                    }
                    defaultLocale = locale;  // set the default locale
                } else {
                    locale = defaultLocale;
                }
            }
        }
        return locale;
    }
}
