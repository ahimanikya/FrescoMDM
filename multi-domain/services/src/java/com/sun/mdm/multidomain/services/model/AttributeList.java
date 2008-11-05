/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.multidomain.services.model;

import com.sun.mdm.multidomain.services.model.Attribute;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * AttributeList class.
 * @author cye
 */
public class AttributeList implements Iterator<Attribute> {

    protected List<Attribute> attributes;
    protected int position = 0;
    protected int size;
    
    /**
     * Create an instance of AttributeList.
     */
    public AttributeList() {
    }
    
    /**
     * Create an instance of AttributeList.
     * @param attributes List of Attribute.
     */
    public AttributeList(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Get a list of Attribute.
     * @return List<Attribute> List of Attribute.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    /**
     * Set a list of Attribute.
     * @param attributes List of Attribute.
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Add a new Attribute.
     * @param a Attribute.
     */
    public void add(Attribute a) {
        if(attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(a);
    }
    
    /**
     * Get number of Attribute in the list.
     * @return int Number of Attribute.
     */
    public int getSize() {
        size = 0;
        if (attributes != null) {
            size  = attributes.size();
        }
        return size;
    }
    
    /**
     * Get the first attribute in the list.
     * @return Attribute The first attribute in the list; null if the list is empty.
     */
    public Attribute first() {
        Attribute a = null;
        if(attributes != null && !attributes.isEmpty()) {
            position = 0;
            a = attributes.get(position);
            position++;
        } 
        return a;
    }

    /**
     * @see java.util.Iterator#hasNext()  
     */
    public boolean hasNext() {
        boolean has = false;
        if(attributes != null && 
           !attributes.isEmpty() && 
           attributes.size() > position) { 
           has = true;
        }  
        return has;
    }

    /**
     * @see java.util.Iterator#next()  
     */    
    public Attribute next() 
        throws NoSuchElementException {
        if(attributes != null && 
           !attributes.isEmpty() && 
           attributes.size() > position) { 
           Attribute a = attributes.get(position);
           position++;
           return a;
        } else {
            throw new NoSuchElementException();
        }       
    }    

    /**
     * @see java.util.Iterator#remove()  
     */    
    public void remove() {
        throw new UnsupportedOperationException("not supported optional operation.");    
    } 
    
    /**
     * Set attribute value.
     * @param name Attribute name.
     * @param value Attribute value.
     */
    public void setAttributeValue(String name, String value){        
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        boolean found = false;
        for (Attribute attribute : attributes){
            if (name.equals(attribute.getName())) {
                attribute.setValue(value);
                found = true;
            }
        }
        if(!found) {
            attributes.add(new Attribute(name, value));
        }
    }
    
    /**
     * Get attribute value.
     * @param name Attribute name.
     * @return String Attribute value.
     */
    public String getAttributeValue(String name) {        
        String value = null;
        if(attributes != null) {
            for (Attribute attribute : attributes){
                if (name.equals(attribute.getName())) {
                    value = attribute.getValue();
                    break;
                }
            }
        }
        return value;
    }    
}
