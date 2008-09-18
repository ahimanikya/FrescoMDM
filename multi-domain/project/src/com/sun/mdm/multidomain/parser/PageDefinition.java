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
public class PageDefinition {
    
    private String mPageIdentifier = null;
    
    private ArrayList<RelationshipPageTabDefination> mPageTabs = new ArrayList<RelationshipPageTabDefination>();

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
     
    

}
