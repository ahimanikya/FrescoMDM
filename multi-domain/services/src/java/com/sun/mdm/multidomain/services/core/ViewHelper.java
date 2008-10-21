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
package com.sun.mdm.multidomain.services.core;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle; 
import java.util.Locale;

import java.text.SimpleDateFormat;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.PageIterator;

import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.security.Operations;
        
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;

import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.util.ObjectSensitivePlugIn;

/**
 * ViewHelper class.
 * @author cye
 */
public class ViewHelper {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sun.mdm.multidomain.services.resources.mdwm", Locale.getDefault());
            
    public static List<RelationshipView> buildRelationshipView(PageIterator<MultiObject> pages) {      
        List<RelationshipView> relationships = new ArrayList<RelationshipView>();
        // ToDo
        return relationships;
    }
    
    public static RelationshipComposite buildRelationshipComposite(MultiObject relationshipObject) {
        RelationshipComposite relationshipComposite = new RelationshipComposite();
        // ToDo
        return relationshipComposite;
    }
    
    public static List<ObjectView> buildObjectView(PageIterator<ObjectNode> pages, boolean isWeighted){
        List<ObjectView> objects = new ArrayList<ObjectView>();
        
        List<ObjectRecord> records = new ArrayList<ObjectRecord>();
        //TBD: MDConfigManager.getDateFormat()
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        //TBD: get from configuration API.
        //RelationshipScreenConfig screenConfig = new RelationshipScreenConfig();
        //TBD:should get from screenConfig.getSearchResultsConfig();
        //EPathArrayList searchResultsEPathField = screenConfig.getSearchResultsEPathFields   
        EPathArrayList searchResultsFieldEPaths = new EPathArrayList();
        ArrayList searchResultsConfigFields = new ArrayList<FieldConfig>();
        
        Operations operations = new Operations();
        while(pages.hasNext()) {
            ObjectNode objectNode = pages.next();
            ObjectSensitivePlugIn plugin = null; //TBD: MDConfigManager.getInstance().getSecurityPlugIn();
            boolean hasSensitiveData = plugin != null ? plugin.equals(objectNode) : true;
            ObjectRecord record = new ObjectRecord();
            //Set the comparision score
            if (isWeighted) {
                //TBD: EOSearchResultRecord.getComparisonScore()
                //private float mComparisonScore
                //private String mEUID
                //private ObjectNode mResultRow
                //eo status?
                record.setFieldValue("Weight", "0");
            }         
            for (int i = 0; i < searchResultsFieldEPaths.size(); i++) {
                FieldConfig fieldConfig  = (FieldConfig) searchResultsConfigFields.get(i);
                EPath ePath = searchResultsFieldEPaths.get(i);
                try {
                    Object objectValue = EPathAPI.getFieldValue(ePath, objectNode);
                    String stringValue = null;
                    if(objectValue instanceof java.util.Date) {
                        String dateField = simpleDateFormat.format(objectValue);          
                        if (objectValue != null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { 
                            record.setFieldValue(fieldConfig.getFullFieldName(), resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                        } else {
                            record.setFieldValue(fieldConfig.getFullFieldName(), dateField);
                        }        
                    } else {
                        if (objectValue != null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { 
                            record.setFieldValue(fieldConfig.getFullFieldName(), resourceBundle.getString("SENSITIVE_FIELD_MASKING"));
                        } else {
                            if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && objectValue != null) {
                                //value for the fields with VALUE LIST
                                stringValue = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), objectValue.toString());
                                record.setFieldValue(fieldConfig.getFullFieldName(), stringValue);
                             } else if ((fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) && objectValue != null) {
                                //Mask the field values accordingly
                                stringValue = fieldConfig.mask(objectValue.toString());
                                record.setFieldValue(fieldConfig.getFullFieldName(), stringValue);
                             } else if ((fieldConfig.getUserCode() != null && fieldConfig.getUserCode().length() > 0) && objectValue != null) {
                                //Get the value if the user code is present for the fields
                                stringValue = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), objectValue.toString());
                                record.setFieldValue(fieldConfig.getFullFieldName(), stringValue);             
                             } else {
                                record.setFieldValue(fieldConfig.getFullFieldName(), objectValue.toString());
                             }
                         }                                         
                      }
                   } catch (Exception npe) {
		          npe.printStackTrace();
                   }                
            }
            //TBD: EUID?
            record.setEUID("EUID"); 
            records.add(record);
        }
        
        return objects;
    } 
    public static ObjectRecord buildObjectRecord(ObjectNode objectNode) {
        ObjectRecord objectRecord = new ObjectRecord();
        // ToDo
        return objectRecord;
    }
}
