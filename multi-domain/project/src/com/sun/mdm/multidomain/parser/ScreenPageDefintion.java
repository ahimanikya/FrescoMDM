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
public class ScreenPageDefintion {
    private String mPageIdentifier = null;
    
    private String mScreenTitle = null;
    
    private int mScreenId = -1;
    
    private int mDisplayOrder = -1;
    
    private ArrayList<RelationshipPageTabDefination> mPageTabs = new ArrayList<RelationshipPageTabDefination>();
    
    public ScreenPageDefintion(String identifier) {
        this.mPageIdentifier = identifier;
    }

    public String getPageIdentifier() {
        return mPageIdentifier;
    }

    public ArrayList<RelationshipPageTabDefination> getPageTabs() {
        return mPageTabs;
    }
    
    public void addPageTab(RelationshipPageTabDefination pageTab) {
        mPageTabs.add(pageTab);
    }
    
    public void deletePageTab(RelationshipPageTabDefination pageTab) {
        for (RelationshipPageTabDefination tab : mPageTabs) {
            if (tab.getTabName().equals(pageTab.getTabName())) {
                mPageTabs.remove(tab);
                break;
            }
        }
    }

    public String getScreenTitle() {
        return mScreenTitle;
    }

    public void setScreenTitle(String screenTitle) {
        this.mScreenTitle = screenTitle;
    }

    public int getScreenId() {
        return mScreenId;
    }

    public void setScreenId(int screenId) {
        this.mScreenId = screenId;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.mDisplayOrder = displayOrder;
    }



}
