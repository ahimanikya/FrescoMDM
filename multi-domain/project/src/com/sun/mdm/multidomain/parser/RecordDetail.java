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
public class RecordDetail {
    
    private int mRecordDetailId;
    
    private String mDisplayName = null;
    
    private ArrayList<FieldGroup> mFieldGroups = new ArrayList<FieldGroup>();
    
    public RecordDetail(int recordDetailId, String displayName) {       
        this.mRecordDetailId = recordDetailId;
        this.mDisplayName = displayName;
    }
    
    public RecordDetail(int recordDetailId) {       
        this.mRecordDetailId = recordDetailId;
    }

    public RecordDetail (int detailID, String displayName, ArrayList<FieldGroup> fieldGroups) {
        this.mFieldGroups = fieldGroups;
        this.mDisplayName = displayName;
        this.mFieldGroups = fieldGroups;
    }
    
    public String getDisplayName() {
        return this.mDisplayName;
    }
    
    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }
    
    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void setFieldGroups(ArrayList<FieldGroup> fieldGroups) {
        this.mFieldGroups = null;
        this.mFieldGroups = fieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }
    
    public int getRecordDetailId() {
        return this.mRecordDetailId;
    }
    
    public void setRecordDetailId(int recordDetailId) {
        this.mRecordDetailId = recordDetailId;
    }

    public void deleteFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.remove(fieldGroup);
    }

}
