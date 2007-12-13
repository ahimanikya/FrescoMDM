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

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *This class will check for the fieldName and Fieldvalue present in the Exclusion list.
 * 
 */
public class ExclusionListLookup {

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
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
        if (!list.isEmpty() && list != null  ) {
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

        if (!list.isEmpty() && list != null ) {

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
   public boolean isSbrFilterEnabled() {
        boolean isEnabled = false;
        ArrayList list = exclusionFilterCofig.getSbrList();
        if (list != null && !list.isEmpty() ) {
            isEnabled = true;
        }
        return isEnabled;
    }

    public boolean isBlockingFilterEnabled() {
        boolean isEnabled = false;
        ArrayList list = exclusionFilterCofig.getBlockingList();
        if (list != null && !list.isEmpty() ) {
            isEnabled = true;
        }
        return isEnabled;
    }

    public boolean isMatchingFilterEnabled() {
        boolean isEnabled = false;
        ArrayList list = exclusionFilterCofig.getmatchingList();
        if (list != null && !list.isEmpty() ) {
            isEnabled = true;
        }
        return isEnabled;
    }
     public void restoreOriginalValue(ObjectNode oNode,
            HashMap originalVaue) throws ObjectException  {
        Iterator itr = originalVaue.keySet().iterator();
        while (itr.hasNext()) {
            try {
                String fieldName = (String) itr.next();
                Object filedValue = originalVaue.get(fieldName);
                ObjectField oField = oNode.getField(fieldName);
                oNode.setValue(fieldName, filedValue);
      
            } catch (ObjectException ex) {
                throw new ObjectException(mLocalizer.t("EFL001:failed to set the fieldValue"));

            }
        }
    }
      public HashMap excludeFieldMap(ObjectNode objectNode, String listType)
           {

        HashMap originlMap = new HashMap();
        ArrayList blockList = getExclusionList(listType); 
      
        if ( !blockList.isEmpty() && blockList != null) {
            for (int i = 0; i < blockList.size(); i++) {
                HashMap map = (HashMap) blockList.get(i);
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    String fieldName = (String) it.next();
                    String fieldValue = "";
                    try {
                        fieldValue = (String) EPathAPI.getFieldValue(fieldName,
                                objectNode);
                    } catch (ObjectException ex) {
                        mLogger.error(mLocalizer.x("EFS001: Unrecognised  fieldname :{0} ",
                                fieldName), ex);

                    } catch (EPathException ep) {

                        mLogger.error(mLocalizer.x("EFS002: EPathException has encountered "),
                                ep);

                    }

                    if (fieldValue != null && !fieldValue.equals("") && fieldValue.trim().length()!= 0) {
                        boolean isFieldvalueExists = isFieldValueInExclusion(fieldValue,
                                fieldName, listType);
                        if (isFieldvalueExists) {
                            String name = fieldName.substring(fieldName.lastIndexOf(".") + 1,
                                    fieldName.length());
                            mLogger.info("  fieldname in Hashmap :" + name + "fieldValue " + fieldValue);
                            originlMap.put(name, fieldValue);

                        }
                    }
                }
            }
        }
        return originlMap;
    }

}
