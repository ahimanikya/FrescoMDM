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
    
    private ArrayList<FieldGroup> mFieldGroups;
    
    public RecordDetail (ArrayList<FieldGroup> fieldGroups) {
        this.mFieldGroups = fieldGroups;
    }
    
    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }

}
