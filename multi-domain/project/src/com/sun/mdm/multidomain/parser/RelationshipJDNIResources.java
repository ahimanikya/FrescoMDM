/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

/**
 *
 * @author wee
 */
public class RelationshipJDNIResources {
    
    private String mJNDIName = null;
    
    private String mID = null;
    
    private String mResType = null;
    
    private String mDescription = null;
    
    public RelationshipJDNIResources(String jndiName, String id, String resType, String description) {
        this.mJNDIName = jndiName;
        this.mID = id;
        this.mResType = resType;
        this.mDescription = description;
    }
    
    public String getJNDIName() {
        return this.mJNDIName;
    }

    public String getID() {
        return this.mID;
    }
    
    public String getResourceType() {
        return this.mResType;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
}
