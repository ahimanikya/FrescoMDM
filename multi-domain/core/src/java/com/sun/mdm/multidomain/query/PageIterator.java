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
package com.sun.mdm.multidomain.query;

import java.util.Iterator;
import java.util.List;

/**
 * Generic PageIterator class.
 * @author cye
 */
public class PageIterator<E> implements Iterator<E> {

    private E[] entries;
    private int size;
    private int position;
    
    public PageIterator(E[] entries) {
        this.entries = entries;
        size = entries.length;
        if (size > 0) {
            position = 0;
        } else {
            position = -1;
        }
    }
    public PageIterator(List<E> entries) {
        this.entries = (E[])entries.toArray();
        size = entries.size();
        if (size > 0) {
            position = 0;
        } else {
            position = -1;
        }        
    }
    public E first() {
       if (entries != null && 
           size > 0 ) {
           position = 0;
           return entries[position];
       }
       return null;
    }
    public boolean hasNext() {
        if (entries != null && 
            size > 0 &&
            position > 0 &&
            position < size) {
            return true;
        } else {
            return false;
        }
    }
    public E next() {
        E element = null;
        if (entries != null && 
            size > 0 &&
            position > 0 &&
            position < size) {
            element = entries[position];
            position++;
        }      
        return element;
    }
    public void remove() {
        throw new UnsupportedOperationException("not supported optional operation.");
    }
    public int size(){
        return size;
    }
}
