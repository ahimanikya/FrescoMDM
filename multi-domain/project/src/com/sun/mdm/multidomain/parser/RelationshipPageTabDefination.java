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
    
    private ArrayList<PageRelationType> mPageRelationType = new ArrayList<PageRelationType>();
    
    public RelationshipPageTabDefination(String tabName) {
        this.mTabName = tabName;
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

}
