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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * FieldList class.
 * @author cye
 */
public abstract class FieldList implements Iterator<Field> {
    
    protected List<Field> fields;
    protected int position = 0;
    protected int size;
    
    public FieldList() {
    }
    public FieldList(List<Field> fields) {
        this.fields = fields;
    }
    public List<Field> getFields() {
        return fields;
    }
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    public void add(Field e) {
        if(fields == null) {
            fields = new ArrayList<Field>();
        }
        fields.add(e);
    }
    public int getSize() {
        size = 0;
        if (fields != null) {
            size  = fields.size();
        }
        return size;
    }
    public Field first() {
        Field e = null;
        if(fields != null && !fields.isEmpty()) {
            position = 0;
            e = fields.get(position);
            position++;
        } 
        return e;
    }
    public boolean hasNext() {
        boolean has = false;
        if(fields != null && 
           !fields.isEmpty() && 
           fields.size() > position) { 
           has = true;
        }  
        return has;
    }
    public Field next() 
        throws NoSuchElementException {
        if(fields != null && 
           !fields.isEmpty() && 
           fields.size() > position) { 
           Field e = fields.get(position);
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
