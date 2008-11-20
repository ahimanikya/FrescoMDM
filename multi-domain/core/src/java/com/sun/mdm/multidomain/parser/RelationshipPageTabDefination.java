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
public class RelationshipPageTabDefination {
    
    private String mTabName = null;
    
    private int mTabId = -1;
    
    private String mTabDisplayName = null;
    
    private ArrayList<PageRelationType> mPageRelationType = new ArrayList<PageRelationType>();
    
    public RelationshipPageTabDefination(String tabName, int tabId, String tabDisplayName) {
        this.mTabName = tabName;
        this.mTabId = tabId;
        this.mTabDisplayName = tabDisplayName;
    }

    public String getTabName() {
        return mTabName;
    }

    public ArrayList<PageRelationType> getPageRelationType() {
        return mPageRelationType;
    }
    
    public void setPageRelationType(ArrayList<PageRelationType> pageRelTypes) {
        mPageRelationType = pageRelTypes;
    }
    
    public void addPageRelationType(PageRelationType pageType) {
        mPageRelationType.add(pageType);        
    }
    
    public void deletePageRelationType(PageRelationType pageType) {
        for (PageRelationType pageTab : mPageRelationType) {
            if (pageTab.getRelType().equals(pageType.getRelType())) {
                mPageRelationType.remove(pageTab);
                break;
            }
        }
    }

    public int getTabId() {
        return mTabId;
    }

    public void setTabId(int tabId) {
        this.mTabId = tabId;
    }

    public String getTabDisplayName() {
        return mTabDisplayName;
    }

    public void setTabDisplayName(String tabDisplayName) {
        this.mTabDisplayName = tabDisplayName;
    }

}
