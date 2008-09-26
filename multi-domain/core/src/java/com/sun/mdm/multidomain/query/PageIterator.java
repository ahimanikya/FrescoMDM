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
import java.util.NoSuchElementException;

/**
 * Generic PageIterator class.
 * @author cye
 */
public class PageIterator<E> implements Iterator<E> {

    private E[] entries;
    private int size;
    private int position;
    
    /**
     * Create an instance of PageIterator.
     * @param entries Data.
     */
    public PageIterator(E[] entries) {
        this.entries = entries;
        size = entries.length;
        if (size > 0) {
            position = 0;
        } else {
            position = -1;
        }
    }
    
    /**
     * Create an instance of PageIterator.
     * @param entries Data.
     */
    public PageIterator(List<E> entries) {
        this.entries = (E[])entries.toArray();
        size = entries.size();
        if (size > 0) {
            position = 0;
        } else {
            position = -1;
        }        
    }
    
    /**
     * Get the first element.
     * @return E Element.
     * @exception NoSuchElementException Thrown if no element.
     */
    public E first() 
       throws NoSuchElementException {
       if (entries != null && 
           size > 0 ) {
           position = 0;
           E element = entries[position]; 
           position++;
           return element;
       } else {
            throw new NoSuchElementException();
       }
    }
    
    /**
     * Returns true if the iteration has more elements.
     * @return boolean If has next element, return true otherise return false.
     */
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
    /**
     * Returns the next element in the iteration. Calling this method repeatedly until 
     * the hasNext() method returns false will return each element in the underlying 
     * collection exactly once. 
     * @return the next element in the iteration.
     * @throws NoSuchElementException Iteration has no more elements.
     */
    public E next() 
        throws NoSuchElementException {
        if (entries != null && 
            size > 0 &&
            position > 0 &&
            position < size) {
            E element = entries[position];
            position++;
            return element;
        } else {
           throw new NoSuchElementException(); 
        }     
       
    }
    
    /**
     * Removes from the underlying collection the last element returned by the iterator 
     * (optional operation). 
     */
    public void remove() {
        throw new UnsupportedOperationException("not supported optional operation.");
    }
    
    /**
     * Get number of elements in iteration.
     * @return Size Number of elements.
     */
    public int size(){
        return size;
    }
}
