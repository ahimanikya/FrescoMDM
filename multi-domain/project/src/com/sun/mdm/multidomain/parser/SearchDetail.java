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
public class SearchDetail {
    
    private int mSearchResultID = 0;
    
    private int mItemPerPage = 0;
    
    private int mMaxResultSize = 0;
    
    private int mRecordDetailID = 0;
    
    private String mDisplayName = null;
    
    private ArrayList<FieldGroup> mFieldGroups;
    
    public SearchDetail(int searchResultID, int itemPerPage, int maxResultSize, int recordDetailID, String displayName,
                        ArrayList<FieldGroup> fieldGroups) {
        
        this.mSearchResultID = searchResultID;
        this.mItemPerPage = itemPerPage;
        this.mMaxResultSize = maxResultSize;
        this.mFieldGroups = fieldGroups;
        this.mRecordDetailID = recordDetailID;
        this.mDisplayName = displayName;
        
    }
    
    public String getDisplayName() {
        return this.mDisplayName;
    }
    
    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }
    
    public int getSearchResultID() {
        return this.mSearchResultID;
    }
 
    public void setSearchResultID(int resultID) {
        this.mSearchResultID = resultID;
    }
    
    public int getItemPerPage() {
        return this.mItemPerPage;
    }

    
    public int getMaxResultSize() {
        return this.mMaxResultSize;
    }
 
    public void setItemPerPage(int itemPerPage) {
        this.mItemPerPage = itemPerPage;
    }

    
    public void setMaxResultSize(int maxResultSize) {
        this.mMaxResultSize = maxResultSize;
    }    
    
    public int getRecordDetailID() {
        return this.mRecordDetailID;
    }
  
    public void setRecordDetailID(int recordID) {
        this.mRecordDetailID = recordID;
    }    

    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void setFieldGroups(ArrayList<FieldGroup> fieldGroups) {
        this.mFieldGroups = fieldGroups;
    }    
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }
    

}
