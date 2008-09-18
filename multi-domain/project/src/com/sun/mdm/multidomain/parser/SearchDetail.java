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
    
    private ArrayList<FieldGroup> mFieldGroups;
    
    public SearchDetail(int searchResultID, int itemPerPage, int maxResultSize, 
                        ArrayList<FieldGroup> fieldGroups) {
        
        this.mSearchResultID = searchResultID;
        this.mItemPerPage = itemPerPage;
        this.mMaxResultSize = maxResultSize;
        this.mFieldGroups = fieldGroups;
        
    }
    
    public int getSearchResultID() {
        return this.mSearchResultID;
    }
    
    public int getItemPerPage() {
        return this.mItemPerPage;
    }
    
    public int getMaxResultSize() {
        return this.mMaxResultSize;
    }
    
    public ArrayList<FieldGroup> getFieldGroups() {
        return this.mFieldGroups;
    }
    
    public void addFieldGroup(FieldGroup fieldGroup) {
        mFieldGroups.add(fieldGroup);
    }

}
