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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.sun.mdm.index.ejb.page.PageData;


/**
 * Used by paging mechanism to efficiently transport data to caller in pages.
 * @author Dan Cidon
 */
public class PageIterator implements java.io.Serializable {

    /** Data loaded so far
     */    
    private final Object[] mData;
    /** Handle to data source on server
     */    
    private PageData mPageData;
    /** Page size
     */    
    private final int mPageSize;
    /** Maximum position achieved thus far
     */    
    private int mMaxPosition;
    /** Current row position
     */    
    private int mPosition;
    /** Forward only mode 
     */    
    private boolean mForwardOnly = false;


    /** Constructor where data is stored on the server
     *
     * @param pageData session bean storing data
     * @param pageSize unit size of data retrieval from server
     * @param maxElements maximum elements that will be retrieved
     */
    public PageIterator(PageData pageData, int pageSize, int maxElements) {
        mPageData = pageData;
        mPageSize = pageSize;
        mPosition = 0;
        mMaxPosition = 0;
        mData = new Object[maxElements];
    }


    /**
     * Constructor where data is passed to client directly
     *
     * @param fullData array of all objects of result set
     */
    public PageIterator(Object[] fullData) {
        mData = fullData;
        mPosition = 0;
        mMaxPosition = fullData.length - 1;
        mPageData = null;
        mPageSize = -1;
    }


    /** Constructor where data is passed to client directly
     * @param fullData list of all objects
     */
    public PageIterator(List fullData) {
        this(fullData.toArray());
    }

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * 
     * @param forwardOnly the 
     */
    public void setReadForwardOnly(boolean forwardOnly) {
        mForwardOnly = forwardOnly;
    }
    
    /** Returns the element at the specified index.
     *
     * @param index row to retrieve
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return Object at index
     */
    public Object absolute(int index)
        throws PageException, RemoteException {
        mPosition = index;
        return next();
    }

    /** Returns the next 'count' elements beginning with the element at the
     * specified index.
     *
     * @param index Row to retrieve.
     * @param count Number of elements to retrieve.
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return List of objects beginning at index
     */
    public ArrayList absolute(int index, int count)
        throws PageException, RemoteException {
        mPosition = index;
        return next(count);
    }


    /** Free the server side resources.
     *
     * @exception RemoteException An error occured.
     */
    public void close()
        throws RemoteException {
        if (mPageData != null) {
            mPageData.remove();
            mPageData = null;
        }     
    }


    /** Returns the number of records in the iterator.
     *
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return number of records in the iterator
     */
    public int count()
        throws PageException, RemoteException {
        if (mPageData != null) {
            return mPageData.count();
        } else {
            return mData.length;
        }
    }


    /** Returns the current position of the iterator. The position of the
     * iterator can be from (-1) to (recordCount - 1).
     * @return Current position
     */
    public int currentPosition() {
        return mPosition;
    }


    /** Get the first record in the iterator.
     *
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return First Object
     */
    public Object first()
        throws PageException, RemoteException {
        mPosition = 0;
        return next();
    }


    /** Get the first 'count' records in the iterator or however many records 
     * are left if 'count' is greater than the size of the iterator.
     *
     * @param count Number of records to retrieve
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return List of first 'count' records
     */
    public ArrayList first(int count)
        throws PageException, RemoteException {
        mPosition = 0;
        return next(count);
    }


    /** Returns true if there are more records left in the iterator.
     *
     * @return true if there are more records
     * @exception RemoteException An error occured.
     * @exception PageException An error occured. */
    public boolean hasNext()
        throws RemoteException, PageException {
        if (mPosition <= mMaxPosition) {
            return true;
        } else {
            // if not in full data mode (can't load anymore in full data mode)
            if (mPageData != null) {
                if (mForwardOnly && mPosition > 0) {   
                    clearPageSizeCache();
                }                   
                //Load the data for the next element
                loadData(mPosition);
                if (mPosition <= mMaxPosition) {
                    return true;
                }
            }
        }
        return false;
    }


    /** Returns next element. Throws PageException if hasNext() is false. Note
     * that if current index=4, next() will return element at index=4.
     *
     * @exception RemoteException An error occured.
     * @exception PageException An error occured.
     * @return Next element
     */
    public Object next()
        throws RemoteException, PageException {
        if (!hasNext()) {
            throw new PageException("Exceeded iterator index.");
        }
        Object obj = mData[mPosition];
        if (obj == null) {
            if (mForwardOnly && mPosition > 0) {   
                clearPageSizeCache();
            }        
            loadData(mPosition);
            obj = mData[mPosition];
        }
        mPosition++;
        return obj;
    }


    /** Get next 'count' number of elements. If less than count number of
     * elements left only return as many as are left. If already at end of
     * iterator throw PageException.
     *
     * @param count Number of objects to retrieve
     * @exception RemoteException An error occured.
     * @exception PageException An error occured.
     * @return List of next 'count' elements.
     */
    public ArrayList next(int count)
        throws RemoteException, PageException {
        if (!hasNext()) {
            throw new PageException("Exceeded iterator index.");
        }
        ArrayList list = new ArrayList(count);
        for (int i = 0; i < count && hasNext(); i++) {
            list.add(next());
        }
        list.trimToSize();
        return list;
    }


    /** Get previous element. If already at beginning of iterator, throws
     * PageException. Note that if current position is index=5, prev() will
     * return element at index=4 and leave the current position at index=4.
     *
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return Previous element
     */
    public Object prev()
        throws PageException, RemoteException {
        if (mPosition < 1) {
            throw new PageException("Already at beginning of iterator.");
        }
        mPosition--;
        Object obj = mData[mPosition];
        if (obj == null) {
            loadData(mPosition - mPageSize);
            obj = mData[mPosition];
        }
        return obj;
    }


    /** Get 'count' previous elements.
     *
     * @param count Number of objects to retrieve
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     * @return List of objects
     */
    public ArrayList prev(int count)
        throws PageException, RemoteException {
        ArrayList list = new ArrayList(count);
        for (int i = 0; i < count && mPosition > 0; i++) {
            list.add(prev());
        }
        list.trimToSize();
        return list;
    }


    /** Sort the objects based on the comparator
     *
     * @param c Comparator to sort by
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     */
    public void sortBy(Comparator c)
        throws RemoteException, PageException {
        mPosition = 0;
        if (mPageData != null) {
            mPageData.sort(c);
            clearCache();
        } else {
            Arrays.sort(mData, c);
        }
    }
    
    /** Sort the objects based on the comparator
     *
     * @param c Comparator to sort by
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     */
    public void sortSummaryBy(Comparator c)
        throws RemoteException, PageException {
        mPosition = 0;
        if (mPageData != null) {
            mPageData.sortSummary(c);            
            clearCache();
        } else {
            Arrays.sort(mData, c);
        }
    }    

    //Clear local cache
    /** Clear the values in the data array
     */    
    private void clearCache() {
        for (int i = 0; i < mData.length; i++) {
            mData[i] = null;
        }
    }
    //Clear local cache of the previous page
    /** Clear the values in the data array
     */  
    private void clearPageSizeCache() throws PageException {

        int startPosition = mPosition - mPageSize; 
        int endPosition = mPosition -1;
        if (startPosition < 0) {
            startPosition = 0;
        }
        for (int i = startPosition; i <= endPosition; i++) {
            mData[i] = null;
        }       
    }            

    /** Load values into the data array from the data source
     * @param index start index (inclusive)
     * @throws PageException An error occured.
     * @throws RemoteException An error occured.
     */    
    private void loadData(int index)
        throws PageException, RemoteException {
        ArrayList objArrayList = null;
        mPageData.setReadForwardOnly(mForwardOnly);
        objArrayList = mPageData.next(index, mPageSize);
        if (objArrayList != null) {
            Object[] objArray = objArrayList.toArray();
            int count = 0;
            while (count < objArray.length && (index + count) < mData.length) {
                mData[index + count] = objArray[count];
                count++;
            }
            if (index + count - 1 > mMaxPosition) {
                mMaxPosition = index + count - 1;
            }
        }
    }

}
