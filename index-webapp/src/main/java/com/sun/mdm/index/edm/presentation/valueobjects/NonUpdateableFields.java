/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

import java.util.ArrayList;

public class NonUpdateableFields {
    private String fieldId;    // indicates if the Status should be displayed
    private String fieldName;    // indicates if the Status should be displayed
    private Object fieldObject;    // indicates if the Status should be displayed
    private int dataType;    // indicates if the Status should be displayed
    private ArrayList rangeFields;    // indicates if the Status should be displayed
    
  
    /** Creates a new instance of AuditLogHandler */
    public NonUpdateableFields() {
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldObject() {
        return fieldObject;
    }

    public void setFieldObject(Object fieldObject) {
        this.fieldObject = fieldObject;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public ArrayList getRangeFields() {
        return rangeFields;
    }

    public void setRangeFields(ArrayList rangeFields) {
        this.rangeFields = rangeFields;
    }
    
    
}
