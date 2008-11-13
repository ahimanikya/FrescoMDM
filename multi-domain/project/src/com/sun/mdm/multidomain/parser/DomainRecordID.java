/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author wee
 */
public class DomainRecordID {
    
    private boolean mShowEUID = false;
    
    private FieldGroup mFieldGroup = new FieldGroup();
    
    private String validationStr = null;

    public boolean isMShowEUID() {
        return mShowEUID;
    }

    public void setMShowEUID(boolean mShowEUID) {
        this.mShowEUID = mShowEUID;
    }

    public FieldGroup getFieldGroup() {
        return mFieldGroup;
    }

    public void setFieldGroup(FieldGroup fieldGroup) {
        this.mFieldGroup = fieldGroup;
    }

    public boolean isValidDomainRecordID() {
        if (mShowEUID == false && mFieldGroup.getFieldRefs().size() == 0) {
            validationStr = "Either EUID or other field is required.";
            return false;
        }
        
        return true;
    }

    public String getValidationStr() {
        return validationStr;
    }
}
