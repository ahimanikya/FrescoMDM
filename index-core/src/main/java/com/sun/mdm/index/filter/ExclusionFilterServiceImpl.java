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

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.survivor.SystemFieldListMap.SystemKey;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * The <b>ExclusionFilterServiceImpl</b>  is the implementation of the 
 * ExclusionFilterService service which will contain the primary 
 * functions of the  exclusion filters. 
 * 
 */
public class ExclusionFilterServiceImpl implements ExclusionFilterService {
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    
    ExclusionListLookup lookup = new ExclusionListLookup();
    
    /** Creates a new instance of ExclusionFilterService */
    public ExclusionFilterServiceImpl() {
    }
    
    
     /**This method checks the data available  for the SBR calculation  is 
     * present in the exclusion list. If it is present in the exclusion list then
     * ignore that SystemField for the survivor calculation. 
     * @param SystemFieldListMap  collection of all the field names assocated 
      * with the system
     * @param  candidateId  The field for the SBR calculation.
     * @return SystemFieldListMap.
    */
    public SystemFieldListMap exclusionSystemFieldList(SystemFieldListMap sysFields,
            String candidateId) {

        SystemFieldListMap filterdSysFields = new SystemFieldListMap();
        ArrayList sbrList = lookup.getExclusionList(FilterConstants.SBR_EXCLUSION_TYPE);
        if (sbrList.size() != 0 && !sbrList.isEmpty() && sbrList != null) {
            Set sysfieldList = sysFields.keySet();
            SystemFieldList fieldValues = new SystemFieldList();
            Iterator it = sysfieldList.iterator();

            while (it.hasNext()) {

                SystemKey key = (SystemFieldListMap.SystemKey) it.next();
                if (key != null) {
                    fieldValues = sysFields.get(key);
                    SystemField sysField = fieldValues.get(candidateId);
                    String fieldName = sysField.getName();
                    Object oSysField = sysField.getValue();

                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("The field name is  = " + fieldName);
                    }

                    if (oSysField != null) {
                        String fieldValue = oSysField.toString();

                        if (mLogger.isDebugEnabled()) {
                            mLogger.debug("the field value is :" + fieldValue);
                        }

                        if (fieldValue != null && !fieldValue.equals(" ") && !fieldValue.equals("null")) {

                            //chk feldname is present in the exclusion list.
                            boolean isFieldNameExists = lookup.isFieldNameInExclusion(fieldName,
                                    FilterConstants.SBR_EXCLUSION_TYPE);
                            if (isFieldNameExists) {
                                boolean isFieldvalueExists = lookup.isFieldValueInExclusion(fieldValue,
                                        fieldName,
                                        FilterConstants.SBR_EXCLUSION_TYPE);
                                if (isFieldvalueExists) {
                                    if (mLogger.isDebugEnabled()) {
                                        mLogger.debug("the field name to be removed from the SystemFieldListMap =: " + fieldName);
                                    }
                                } else {
                                    filterdSysFields.put(key, fieldValues);
                                    if (mLogger.isDebugEnabled()) {
                                        mLogger.debug("the field value not in exclusion list=:   " + fieldValues);
                                    }
                                }
                            } else {

                                filterdSysFields.put(key, fieldValues);
                            }
                        }
                    }
                }
            }
            //If all the field values are present in the exclusion list then return the original list without filtering.        
            if (filterdSysFields.size() == 0 || filterdSysFields == null) {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("All the fields are in exclusion list, disable the filter");
                }

                filterdSysFields = sysFields;
            }
        } else {
            if (mLogger.isDebugEnabled()) {
                    mLogger.debug("The SBR exclusion list is empty");
                }
            filterdSysFields = sysFields;
        }
        return filterdSysFields;
    }


    /**This method checks  the data available  for the blocking process 
     * is present in the exclusion list. If it is present in the exclusion list
     * then ignore that field  for the Blocking queries.
     * @param  ObjectNode 
     * @return  ObjectNode.
     * @throws  ObjectException and EPathException.
    */
    
    public ObjectNode blockingExclusion(ObjectNode objectToBlock)  {

        ArrayList blockList = lookup.getExclusionList(FilterConstants.BLOCK_EXCLUSION_TYPE);
        excludeField(objectToBlock, blockList, FilterConstants.BLOCK_EXCLUSION_TYPE);

        return objectToBlock;
    }

    
    /**This method checks  the data available  for the matching process 
     * is present in the exclusion list. If it is present in the exclusion list
     * then ignore that field  for the Match process.
     * @param  ObjectNode
     * @return  ObjectNode.
     * @throws  ObjectException and EPathException.
    */
    
    public ObjectNode exclusionMatchField(ObjectNode objToMatch)  {

        ArrayList blockList = lookup.getExclusionList(FilterConstants.MATCH_EXCLUSION_TYPE);
        excludeField(objToMatch, blockList, FilterConstants.MATCH_EXCLUSION_TYPE);

        return objToMatch;
    }

    
   /**
    * This Method will check whether the field values are pesent in the exclusion list.
    * If is presents then sets the field value to null. 
    * @param objectNode
    * @param blockList
    * @param listType
     */     
      
      private void excludeField(ObjectNode objectNode, ArrayList blockList,
            String listType) {

        if (blockList.size()!= 0 && !blockList.isEmpty() && blockList != null) {
            for (int i = 0; i < blockList.size(); i++) {
                HashMap map = (HashMap) blockList.get(i);
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    String fieldName = (String) it.next();
                    String fieldValue = "";
                    try {
                        fieldValue = (String) EPathAPI.getFieldValue(fieldName, objectNode);
                    } catch (ObjectException ex) {
                        mLogger.error(mLocalizer.x("EFS001: Unrecognised  fieldname :{0} ", fieldName), ex);
                    } catch (EPathException e) {
                        mLogger.error(mLocalizer.x("EFS002: EPathException has encountered "), e);
                    }

                    if (fieldValue != null && !fieldValue.equals("") && !fieldValue.equals("null")) {
                        boolean isFieldvalueExists = lookup.isFieldValueInExclusion(fieldValue, fieldName, listType);
                        if (isFieldvalueExists) {
                            try {
                                String onlyName = "";
                                StringTokenizer tokenizer = new StringTokenizer(fieldName, ".");
                                while (tokenizer.hasMoreTokens()) {
                                    String ss = tokenizer.nextToken();
                                    if (!tokenizer.hasMoreTokens()) {
                                        onlyName = ss;
                                    }
                                }
                                objectNode.setNullable(onlyName, true);
                                objectNode.setNull(onlyName, true);
                            } catch (ObjectException ex) {
                                mLogger.error(mLocalizer.x("EFS003: the objectNode Field is not nullable :{0}", fieldName), ex);
                            }
                        }
                    }
                }
            }
        } else {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("The exclusion list is empty");
            }
        }
    }




    
    
    
}
