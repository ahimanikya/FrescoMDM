/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.multidomain.services.util.DomainUtil;
import com.sun.mdm.multidomain.services.core.ConfigException;

import com.sun.mdm.index.util.Localizer;

import java.util.List;
import java.util.ArrayList;

public class SummaryLabel {
    
    private static transient final Localizer mLocalizer = Localizer.get();
    
    private ArrayList<FieldConfig> mFieldConfigs;   
    private ArrayList<String> mDelimiters;       
    private String DEFAULT_DELIMITER = " ";
    private boolean mShowEUID = false;

    public SummaryLabel(boolean showEUID, ArrayList<FieldConfig> fieldConfigs, 
                        ArrayList<String> delimiters) throws ConfigException {
        
        mShowEUID = showEUID;
        mFieldConfigs = fieldConfigs;
        setDelimiters(delimiters);
    }
    
    public SummaryLabel(boolean showEUID, ArrayList<FieldConfig> fieldConfigs) 
            throws ConfigException {
        
        mShowEUID = showEUID;
        mFieldConfigs = fieldConfigs;
        createDefaultDelimiters();
    }

    /** Creates an ArrayList of DEFAULT_DELIIMITERs
     * 
     */
    private void createDefaultDelimiters() throws ConfigException {
        int defaultDelimArrayListSize = 0;
        if (mShowEUID == true) {
            defaultDelimArrayListSize = mFieldConfigs.size() + 2;
        } else {
            defaultDelimArrayListSize = mFieldConfigs.size() + 1;
        }
        mDelimiters = new ArrayList<String> (defaultDelimArrayListSize);
        for (int i = 0; i < defaultDelimArrayListSize; i++) {
            mDelimiters.add(DEFAULT_DELIMITER);
        }
    }
    
    /** Retrieves a List of FieldConfig objects.
     * 
     * @return A List of FieldConfig objects.
     */
    public List<FieldConfig> getFieldConfigs() {      
        return mFieldConfigs;
    }

    /** Sets the fieldConfigs ArrayList that represents the fields comprising
     *  this SummaryLabel object.
     * 
     * @param fieldConfigs  The ArrayList of FieldConfig objects to which the 
     * fieldConfigs member is set.
     */
    public void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) { 
        mFieldConfigs = fieldConfigs;
    }

    /** Retrieves a List of String objects as delimiters.
     * 
     * @return A List of String objects as delimiters.
     */
    public List<String> getDelimiters() {     
        return mDelimiters;
    }

    /** Sets the delimiters.
     * 
     * @param delimiters  Delimiters to set.
     * @throws Exception if an error occurred.
     */
    public void setDelimiters(ArrayList<String> delimiters) throws ConfigException {
        if (mShowEUID == true) {
            if (delimiters.size() != mFieldConfigs.size() + 2) {
                throw new ConfigException(mLocalizer.t("CFG552: The number of delimiters ({0}) " +
                                                 "must be two more than the number of fields({1}).", 
                                                 delimiters.size(), mFieldConfigs.size()));
            }
        } else if (delimiters.size() != mFieldConfigs.size() + 1) {
                throw new ConfigException(mLocalizer.t("CFG553: The number of delimiters ({0}) " +
                                                 "must be one more than the number of fields({1}).", 
                                                 delimiters.size(), mFieldConfigs.size()));
        }
        mDelimiters = delimiters;
    }

    /** Retrieves the ID label.  ID label has the following format:
     *  mDelimiters[0] + mFieldConfigs[0] + mDelimiters[1] + ...  + mFieldConfigs[i] + mDelimiters[i+1] 
     * 
     * @param objectNode  ObjectNode instance that contains the values to retrieve.
     * @return A string representing the ID label for an ObjectNode instance.
     * @throws java.lang.Exception if an error occurred.
     */
    public String getIDLabel(ObjectNode objectNode) throws Exception {          
        
        List fieldValues = DomainUtil.getFieldValues(objectNode, mFieldConfigs);
        
        // If necessary, insert the EUID to the beginning of the list.
        
        if (mShowEUID == true) {
            Object euid = DomainUtil.getEUIDValue(objectNode);
            fieldValues.add(0, euid);
        }
        
        StringBuffer str = new StringBuffer();
        int delimiterIndex = 0;
        str.append(mDelimiters.get(delimiterIndex));
        delimiterIndex++;
        for (int i = 0; i < fieldValues.size(); i++, delimiterIndex++) {
            str.append(fieldValues.get(i));
            str.append(mDelimiters.get(delimiterIndex));
        }
        return str.toString();
    }

}
