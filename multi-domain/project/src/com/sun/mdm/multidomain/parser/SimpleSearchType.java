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
public class SimpleSearchType {

    private String mScreenTitle = null;
    
    private int mSearchResultID = 0;
    
    private String mInstruction = null;
    
    private ArrayList<FieldGroup> mFieldGroups; 
    
    public SimpleSearchType(String screenTitle, int screenResultID, 
                            String instructionID, ArrayList<FieldGroup> fieldGroups) {
        
        this.mScreenTitle = screenTitle;
        this.mSearchResultID = screenResultID;
        this.mInstruction = instructionID;
        this.mFieldGroups = fieldGroups;
    }
    
      
    public void setScreenTitle(String screenTitle) {
        this.mScreenTitle = screenTitle;
    }
    
    public void setScreenResultID(int resultID) {
        this.mSearchResultID = resultID;
    }
    
    public void setInstruction(String instruction) {
        this.mInstruction = instruction;
    }
    
    public String getScreenTitle() {
        return this.mScreenTitle;
    }
    
    public int getScreenResultID() {
        return this.mSearchResultID;
    }
    
    public String getInstruction() {
        return this.mInstruction;
    }
    
    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }
}
