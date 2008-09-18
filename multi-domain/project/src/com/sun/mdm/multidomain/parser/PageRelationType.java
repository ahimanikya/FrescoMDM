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
public class PageRelationType {
    
    private String mRelType = null;
   
    private ArrayList<FieldGroup> mFieldGroups;
    
    public PageRelationType(String relType) {
        this.mRelType = relType;
    }

    public String getRelType() {
        return mRelType;
    }

    public void setRelType(String relType) {
        this.mRelType = relType;
    }

    public ArrayList<FieldGroup> getFieldGroups() {
        return mFieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }
    
    public void deleteFieldGroup(FieldGroup fieldGroup) {
        for (FieldGroup fGroup : mFieldGroups) {
            if (fGroup.equals(fieldGroup)) {
                mFieldGroups.remove(fieldGroup);
                break;
            }
        }
    }

}
