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
public class ScreenDefinition {
    
    private String mIdentifier = null;
    
    private String mScreenTitle = null;
    
    private int mScreenId = -1;
    
    private int mDisplayOrder = -1;
    
    private ArrayList<PageRelationType> mPageRelationType = null;
    
    private PageDefinition mChildPage = null;
    
    public ScreenDefinition() {
        
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        this.mIdentifier = identifier;
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

    public ArrayList<PageRelationType> getPageRelationType() {
        return mPageRelationType;
    }

    public void addPageRelationType(PageRelationType relationType) {
        if (mPageRelationType == null) {
            mPageRelationType = new ArrayList<PageRelationType>();
            
        }
        mPageRelationType.add(relationType);
        
    }
    
    public void setPageRelationType(ArrayList<PageRelationType> pageRelationType) {
        this.mPageRelationType = pageRelationType;
    }

    public PageDefinition getChildPage() {
        return mChildPage;
    }

    public void setChildPage(PageDefinition childPage) {
        this.mChildPage = childPage;
    }

}
