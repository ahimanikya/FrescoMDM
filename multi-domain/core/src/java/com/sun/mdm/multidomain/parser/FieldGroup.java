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
    
    public boolean isFieldAdded(String fieldName) {
        for (FieldRef field : mFieldRefs) {
            if (field.getFieldName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
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
        
        /* Retrieves the object references for a field.  This is the object to
         * which a field belongs.  For example, in "Person.Address.City", "Address" 
         * is the object reference.
         * 
         * @return Object reference for a field.
         */
        public String getObjectRef() {
            
            String objectRef = null;
            int lastDelim = mFieldName.lastIndexOf('.');
            if (lastDelim != -1) {
                int objectDelim = mFieldName.lastIndexOf('.', lastDelim - 1);
                if (objectDelim != -1) {
                    objectRef = mFieldName.substring(objectDelim + 1, lastDelim);
                } else { // top level object
                    objectRef = mFieldName.substring(0, lastDelim);
                }
                // remove any extraneous characters
                objectDelim = objectRef.indexOf('[');
                if (objectDelim != -1) {
                    objectRef = objectRef.substring(0, objectDelim);
                }
            }
            return objectRef;
        }
        
    }


}

