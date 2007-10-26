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
package com.sun.mdm.index.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import com.sun.mdm.index.query.QMException;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.objects.ObjectNode;
import java.io.Serializable;

/** Page adapter for Iterators
 * @author dcidon
 */
public class IteratorPageAdapter implements PageAdapter,Serializable {
    /** List of objects that have been loaded
     */    
    private final ArrayList mData;

    /** Iterator data source
     */    
    private final transient Iterator mIterator;
    
    /** Max number of elements that will be loaded
     */    
    private final int mMaxElements;
    
    /** Current row position 
     */    
    private int mCurrentPosition;
    
    /** Forward only mode 
     */ 
    private boolean mForwardOnly = false;  


    /** Creates a new instance of IteratorPageAdapter
     *
     * @param iterator Iterator data source
     * @param maxElements Max elements that will be loaded
     */
    public IteratorPageAdapter(Iterator iterator, int maxElements) {
        mIterator = iterator;
        mData = new ArrayList();
        mMaxElements = maxElements;
        mCurrentPosition = 0;
    }

    /** Creates a new instance of IteratorPageAdapter
     * 
     * @param iterator Iterator data source
     * @param maxElements Max elements that will be loaded
     */
    public IteratorPageAdapter(QMIterator iterator, int maxElements) {
        this(new QMIteratorAdapter(iterator), maxElements);
    }

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * 
     * @param forwardOnly the 
     */
    public void setReadForwardOnly(boolean forwardOnly) 
        throws PageException {
        mForwardOnly = forwardOnly;
    }    
    
    /** See PageAdapter
     * 
     * @param index See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void setCurrentPosition(int index)
        throws PageException {
        //If index is more than the max number of possible elements, then
        //clearly it is out of bounds
        if (index >= mMaxElements) {
            throw new PageException("Index greater than max elements.  Index: " 
            + index + " Max elements: " + mMaxElements);
        }

        //If index is less than or equal to current position, then we are
        //going backwards and no additional loading is required
        if (index <= mCurrentPosition) {
            mCurrentPosition = index;
        } else {
            //Advance iterator to read all elements up to the desired element
            while (hasNext() && mCurrentPosition < index) {
                next();
            }
            //If we did not reach desired elements, then the index is invalid
            if (mCurrentPosition < index) {
                throw new PageException("Index out of bounds: " + index);
            }
        }
    }


    /** See PageAdapter
     */
    public void activate() {
    }


    /** See PageAdapter
     */
    public void close() {
      
    }


    /** See PageAdapter
     * @return See PageAdapter
     * @throws PageException See PageAdapter
     */
    public int count()
        throws PageException {
        int savePosition = mCurrentPosition;
        //Skip to end of loaded data
        mCurrentPosition = mData.size() - 1;
        while (hasNext()) {
            next();
        }
        int count = mCurrentPosition;
        mCurrentPosition = savePosition;
        
        return count;
    }


    /** See PageAdapter
     * @return See PageAdapter
     * @throws PageException See PageAdapter
     */
    public boolean hasNext()
        throws PageException {
        boolean res = false;
        if (mCurrentPosition < mData.size()) {
            res = true;
             // else if all elements have not been retrieved so far
        } else  {
            try {
                 // maxElements has been loaded
                if (mData.size() >= mMaxElements) {
                     // If loaded as serialzable in SessionBean, this would be null
                    if (mIterator != null) {
                      ((QMIteratorAdapter)mIterator).close();
                    }
                    res = false;
                   
                    
                } else if (mIterator != null) {
                   res = mIterator.hasNext();
                   
                }
            } catch (Exception e) {
                throw new PageException(e);
            }
        }
        
        return res;
    }


    /** See PageAdapter
     */
    public void idleTimeOut() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object next()
        throws PageException {
        if (!hasNext()) {
            throw new PageException("End of iterator reached");
        }
        Object obj = null;
        if (mCurrentPosition < mData.size() && mCurrentPosition >= 0) {
            obj = mData.get(mCurrentPosition);
        } else {
            try {
                //Load records as required
                int startIndex = mData.size() - 1;
                for (int i = startIndex; mIterator != null && i < mCurrentPosition; i++) {
                    obj = mIterator.next();
                    // Currently only MC is using this pageAdapter and it is populating only
                    // EOSearchResultRecord. 
                    if (obj instanceof com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord ) {
                      ObjectNode objNode =  ((com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord)obj).getObject();
                    }
                    mData.add(obj);
                }
            } catch (Exception e) {
                throw new PageException(e);
            }
        }
        mCurrentPosition++;
        return obj;
    }


    /** See PageAdapter
     */
    public void passivate() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public Object prev()
        throws PageException {
        if (mCurrentPosition == 0) {
            throw new PageException("Already at beginning of iterator");
        } else {
            mCurrentPosition--;
            return mData.get(mCurrentPosition);
        }
    }


    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sort(Comparator c)
        throws PageException {
        //Do a count operation to load all remaining data
        count();
        setCurrentPosition(0);
        Collections.sort(mData, c);
    }

    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sortSummary(Comparator c)
        throws PageException {
        sort(c);
    }    
    /** See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sort()
        throws PageException {
        //Do a count operation to load all remaining data
        count();
        setCurrentPosition(0);
        Collections.sort(mData);
    }

    /** Adapter because QMIterator does not implement Iterator
     * interface
     */    
    private static class QMIteratorAdapter implements Iterator {

        /** QMIterator data source
         */        
        private final transient QMIterator mQMIterator;

        /** QMIteratorAdapter constructor
         * @param lQMIterator QMIterator data source
         */     
        QMIteratorAdapter(QMIterator lQMIterator) {
            mQMIterator = lQMIterator;
        }


        /** See Iterator
         * @return See Iterator
         */
        public boolean hasNext() {
            boolean retVal;
            try {
                retVal = mQMIterator.hasNext();
            } catch (QMException e) {
                throw new NoSuchElementException(e.getMessage());
            }
            return retVal;
        }


        /** See Iterator
         * @return See Iterator
         */
        public Object next() {
            Object retVal;
            try {
                retVal = mQMIterator.next();
            } catch (QMException e) {
                throw new NoSuchElementException(e.getMessage());
            }
            return retVal;
        }


        /** See Iterator
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        void close() throws Exception {
            mQMIterator.close();
            
        }

    }

}
