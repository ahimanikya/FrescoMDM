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
package com.sun.mdm.multidomain.services.relationship;

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
    
    public AttributeList() {
    }
    public AttributeList(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    public List<Attribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    public void add(Attribute e) {
        if(attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(e);
    }
    public int getSize() {
        size = 0;
        if (attributes != null) {
            size  = attributes.size();
        }
        return size;
    }
    public Attribute first() {
        Attribute e = null;
        if(attributes != null && !attributes.isEmpty()) {
            position = 0;
            e = attributes.get(position);
            position++;
        } 
        return e;
    }
    public boolean hasNext() {
        boolean has = false;
        if(attributes != null && 
           !attributes.isEmpty() && 
           attributes.size() > position) { 
           has = true;
        }  
        return has;
    }
    public Attribute next() 
        throws NoSuchElementException {
        if(attributes != null && 
           !attributes.isEmpty() && 
           attributes.size() > position) { 
           Attribute e = attributes.get(position);
           position++;
           return e;
        } else {
            throw new NoSuchElementException();
        }
       
    }
    public void remove() {
        throw new UnsupportedOperationException("not supported optional operation.");    
    }    
}
