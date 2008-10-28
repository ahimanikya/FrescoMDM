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
public class FieldGroup {
    
    private String mDescription = null;
    
    private ArrayList<FieldRef> mFieldRefs = new ArrayList<FieldRef>();
    
    public ArrayList<FieldRef> getFieldRefs() {
        return this.mFieldRefs;
    }
    
    public void addFieldRef(FieldRef fieldRef) {
        mFieldRefs.add(fieldRef);
    }
    
    public void deleteFieldRef(String fieldRefName) {
        for (FieldRef fieldRef : mFieldRefs) {
            if (fieldRef.getFieldName().equals(fieldRefName)) {
                mFieldRefs.remove(fieldRef);
                break;
            }
        }
    }
    
    public FieldRef createFieldRef(String fieldName) {
        return new FieldRef(fieldName);
        
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
    
    public class FieldRef {

        private String mFieldName;
        
        public FieldRef(String fieldName) {
            mFieldName = fieldName;
        }
        
        public String getFieldName() {
            return this.mFieldName;
        }
        
        
    }


}

