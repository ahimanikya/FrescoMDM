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
    
    private int mInitialScreenId = -1;
    
    private ArrayList<ScreenPageDefintion> mScreenDefintions = new ArrayList<ScreenPageDefintion>();
    
    public PageDefinition() {
        
    }

    public int getInitialScreenId() {
        return mInitialScreenId;
    }

    public void setInitialScreenId(int initialScreenId) {
        this.mInitialScreenId = initialScreenId;
    }

    public ArrayList<ScreenPageDefintion> getScreenDefs() {
        return mScreenDefintions;
    }

    public void setScreenDefs(ArrayList<ScreenPageDefintion> pageDefs) {
        this.mScreenDefintions = pageDefs;
    }
    
    public void addScreenDefition(ScreenPageDefintion screenDef) {
        mScreenDefintions.add(screenDef);       
    }
    
    public void deleteScreenDefition(ScreenPageDefintion screenDef) {
        mScreenDefintions.remove(screenDef);
    }
}


