/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.query;

/**
 * FieldValuePair class.
 * @author SwaranjitDua
 * @author cye
 */
public class FieldValuePair {
    private String name;
    private String value;
    
    /**
     * Create an instance of FieldValuePair.
     */
    public FieldValuePair() {        
    }
    /**
     * Set name attribute.
     * @param name Name attribute.
     */
    public void setName(String name) {        
        this.name = name;
    }
    /**
     * Get name attribute.
     * @return String Name attribute.
     */
    public String getName() {        
        return name;
    }
    /**
     * Set value attribute.
     * @param value Value attribute.
     */
    public void setValue(String value) {        
        this.value = value;
    }
    /**
     * Get value attribute.
     * @return String Value attribute.
     */
    public String getValue() {        
        return value;
    }
}
