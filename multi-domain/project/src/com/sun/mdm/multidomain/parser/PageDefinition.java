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
    
    private ArrayList<ScreenDefinition> mScreenDefintions = new ArrayList<ScreenDefinition>();
    
    public PageDefinition() {
        
    }

    public int getInitialScreenId() {
        return mInitialScreenId;
    }

    public void setInitialScreenId(int initialScreenId) {
        this.mInitialScreenId = initialScreenId;
    }

    public ArrayList<ScreenDefinition> getScreenDefs() {
        return mScreenDefintions;
    }

    public void setScreenDefs(ArrayList<ScreenDefinition> pageDefs) {
        this.mScreenDefintions = pageDefs;
    }
    
    public void addScreenDefition(ScreenDefinition screenDef) {
        mScreenDefintions.add(screenDef);       
    }
    
    public void deleteScreenDefinition(ScreenDefinition screenDef) {
        mScreenDefintions.remove(screenDef);
    }
}


