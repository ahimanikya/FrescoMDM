/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.query;

/**
 * FieldValuePair class.
 * @author cye
 */
public class FieldValuePair {
    private String name;
    private String value;
    
    public FieldValuePair() {        
    }
    public void setName(String name) {        
        this.name = name;
    }
    public String getName() {        
        return name;
    }
    public void setValue(String value) {        
        this.value = value;
    }
    public String getValue() {        
        return value;
    }
}
