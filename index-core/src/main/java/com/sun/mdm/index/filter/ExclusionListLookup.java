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
package com.sun.mdm.index.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *This class will check for the fieldName and Fieldvalue present in the Exclusion list.
 * 
 */
public class ExclusionListLookup {

    /** Creates a new instance of ExclusionListLookup */
    public ExclusionListLookup() {
    }
    ExclusionFilterCofig exclusionFilterCofig = new ExclusionFilterCofig();

    /**
     * This method used to check field name is present in the corresponding 
     * exclusion list.
     * @param qualifiedFieldName the fieldname to search in the exclusion list.
     * @param exclusionType  the type of the filter.
     * @return boolean 
     */
    public boolean isFieldNameInExclusion(String qualifiedFieldName,
            String exclusionType) {
        boolean isFieldExists = false;
        ArrayList list = getExclusionList(exclusionType);
        System.out.println(list.size());
        if (!list.isEmpty() || list != null) {
            for (int i = 0; i < list.size(); i++) {
                HashMap map = (HashMap) list.get(i);
                Iterator it = map.keySet().iterator();

                while (it.hasNext()) {
                    String fieldName = (String) it.next();
                    if (qualifiedFieldName.trim().equals(fieldName.trim())) {

                        isFieldExists = true;
                    }
                }
            }
        }
        return isFieldExists;
    }

    /**
    * This method used to check field value is present in the corresponding 
    * exclusion list.
    * @param fieldValue the fieldvalue to search in the exclusion list.
    * @param qualifiedFieldName the fieldname to search in the exclusion list.
    * @param exclusionType the type of the filter.
    * @return boolean
    */
    public boolean isFieldValueInExclusion(Object fieldValue,
            String qualifiedFieldName, String exclusionType) {

        boolean isFieldValueExists = false;
        ArrayList list = getExclusionList(exclusionType);

        if (!list.isEmpty() || list != null) {

            for (int i = 0; i < list.size(); i++) {
                HashMap map = (HashMap) list.get(i);

                Iterator it = map.keySet().iterator();
                // Syste.out.println(map.keySet().size());
                while (it.hasNext()) {
                    String fieldName = (String) it.next();
                    if (qualifiedFieldName.equals(fieldName)) {
                        ArrayList fieldValueList = (ArrayList) map.get(fieldName);

                        isFieldValueExists = isValueExists(fieldValueList,
                                fieldValue);
                    }
                }
            }
        }
        return isFieldValueExists;
    }

    /**
     * This method will return the exclusionList.
     * @param exclusionType the type of the filter.
     * @return ArrayList  list of values for the given exclusionType.
     */
    public ArrayList getExclusionList(String exclusionType) {

        ArrayList list = new ArrayList();

        if (exclusionType.equals(FilterConstants.SBR_EXCLUSION_TYPE)) {
            list = exclusionFilterCofig.getSbrList();
        }

        if (exclusionType.equals(FilterConstants.BLOCK_EXCLUSION_TYPE)) {
            list = exclusionFilterCofig.getBlockingList();
        }

        if (exclusionType.equals(FilterConstants.MATCH_EXCLUSION_TYPE)) {
            list = exclusionFilterCofig.getmatchingList();
        }

        return list;
    }

    /**
     * This method will check whether field value list present in the corresponding 
     * list valus.
     * @param fieldValueList 
     * @param fieldValue 
     * @return boolean
     */
    public boolean isValueExists(ArrayList fieldValueList, Object fieldValue) {
        boolean valueExists = false;

        for (int i = 0; i < fieldValueList.size(); i++) {
            String value = (String) fieldValueList.get(i);
            String sfieldValue = fieldValue.toString();
            if (value.equals(sfieldValue)) {

                valueExists = true;
                break;
            }
            if ((sfieldValue.trim()).equalsIgnoreCase(value.trim())) {
                valueExists = true;
            }
        }
        return valueExists;
    }
}
