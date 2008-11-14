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
    
    private SearchOptions mSearchOption = null;
    
    private int mScreenOrder = -1;
    
    public SimpleSearchType(String screenTitle, int screenResultID, 
                            String instructionID, int searchScreenOrder, ArrayList<FieldGroup> fieldGroups) {
        
        this.mScreenTitle = screenTitle;
        this.mSearchResultID = screenResultID;
        this.mInstruction = instructionID;
        this.mScreenOrder = searchScreenOrder;
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
    
    public void setScreenOrder(int screenOrder) {
        this.mScreenOrder = screenOrder;
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
    
    public int getScreenOrder() {
        return this.mScreenOrder;
    }
    
    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }
    
    public SearchOptions getSearchOption() {
        return this.mSearchOption;
    }
    
    public void setSearchOption(SearchOptions searchOpt) {
        this.mSearchOption = searchOpt;
    }

    public FieldGroup getFieldGroup(String groupName) {
     
        for (FieldGroup group : mFieldGroups ) {
            if (group.getDescription().equalsIgnoreCase(groupName)) {
                return group;
            }
        }
        return null;
    }

    public FieldGroup getFieldGroup(int groupId) {
     
        for (FieldGroup group : mFieldGroups ) {
            if (group.hashCode() == groupId) {
                return group;
            }
        }
        return null;
    }    
}
