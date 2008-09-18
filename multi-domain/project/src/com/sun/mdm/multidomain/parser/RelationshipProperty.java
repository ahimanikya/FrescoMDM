/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

/**
 *
 * @author wee
 */
public class RelationshipProperty {
    
    private String mPropertyName = null;
    
    private String mPropertyValue = null;
    
    public RelationshipProperty(String name, String value) {
        this.mPropertyName = name;
        this.mPropertyValue = value;
    }
    
    public String getPropertyName() {
        return this.mPropertyName;
    }
    
    public String getPropertyValue() {
        return this.mPropertyValue;
    }
    
    public void setPropertyValue(String propertyValue) {
        this.mPropertyValue = propertyValue;  
    }

}
