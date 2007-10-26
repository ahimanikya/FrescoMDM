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
package com.sun.mdm.index.master.search.assumedmatch;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNodeComparator;
import com.sun.mdm.index.page.PageIteratorWrapper;
import com.sun.mdm.index.page.PageException;


/**
 * The <b>AssumedMatchIterator</b> class represents an iterator containing
 * the AssumedMatchSummary objects returned by a call to
 * <b>lookupAssumedMatches</b>. Use indexes to page through the
 * AssumedMatchSummary objects and to retrieve specific objects from the
 * iterator.
 */
public class AssumedMatchIterator extends PageIteratorWrapper
    implements java.io.Serializable {

    
    /**
     * Creates a new instance of the AssumedMatchIterator class that maintains
     * the connection to the server.
     * <p>
     * @param pageData A session bean that stores the data retrieved from
     * the server.
     * @param pageSize The size of each unit of data retrieved from the
     * server.
     * @param maxElements The maximum number of elements retrieved for
     * an assumed match search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public AssumedMatchIterator(PageData pageData, int pageSize,
    int maxElements) {
        super(pageData, pageSize, maxElements);
    }


    /**
     * Creates a new instance of the AssumedMatchIterator class that does not
     * maintain the connection to the server.
     * <p>
     * @param fullData An array of all objects in the result set.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public AssumedMatchIterator(ArrayList fullData) {
        super(fullData);
    }

    /** 
     * Specifies whether the assumed match iterator will clear all the DataPage 
     * objects of a loaded page (i) once it starts to read and load the next page
     * (i+1). Setting the iterator to clear old DataPage objects helps to optimize
     * the performance of assumed match searches and assumed match reports.
     * <p>
     * @param forwardOnly A Boolean indicator of whether the iterator will clear 
     * old DataPage objects. Specify <b>true</b> to clear old DataPage objects; 
     * specify <b>false</b> to leave the objects in memory.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setReadForwardOnly(boolean forwardOnly) {
        getGenericIterator().setReadForwardOnly(forwardOnly);
    } 
   


    /**
     * Retrieves an AssumedMatchSummary object at the specified
     * index from the AssumedMatchIterator object.
     * <p>
     * @param index The row to retrieve.
     * @return <CODE>AssumedMatchSummary</CODE> - An
     * AssumedMatchSummary object.
     * @exception PageException Thrown if an error occurs while
     * retrieving the object.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary absolute(int index)
        throws PageException, RemoteException {
        return (AssumedMatchSummary) getGenericIterator().absolute(index);
    }


    /**
     * Retrieves a series of rows from an AssumedMatchIterator
     * object, beginning with the row at the specified index and continuing
     * through the specified number of rows.
     * <p>
     * @param index The first row to retrieve.
     * @param count The total number of rows to retrieve.
     * @return <CODE>AssumedMatchSummary[]</CODE> - An array of
     * AssumedMatchSummary objects.
     * @exception PageException Thrown if an error occurs while
     * retrieving the objects.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary[] absolute(int index, int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().absolute(index, count);
        return listToArray(list);
    }


    /**
     * Retrieves the first row from an AssumedMatchIterator object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>AssumedMatchSummary</CODE> - An
     * AssumedMatchSummary object.
     * @exception PageException Thrown if an error occurs while
     * retrieving the object.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary first()
        throws PageException, RemoteException {
        return (AssumedMatchSummary) getGenericIterator().first();
    }


    /**
     * Retrieves the first rows in the iterator. The number of rows retrieved
     * is specified as a parameter to this method. If the number of rows
     * specified by the parameter is larger than the number of records in the
     * iterator, all rows are retrieved.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>AssumedMatchSummary[]</CODE> - An array of
     * AssumedMatchSummary objects.
     * @exception PageException Thrown if an error occurs while
     * retrieving the objects.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary[] first(int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().first(count);
        return listToArray(list);
    }


    /**
     * Retrieves the next row from an AssumedMatchIterator object. If
     * the current index is 4, this method returns the row at index=4.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>AssumedMatchSummary</CODE> - An
     * AssumedMatchSummary object.
     * @exception PageException Thrown if the call to hasNext
     * returns false.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary next()
        throws RemoteException, PageException {
        return (AssumedMatchSummary) getGenericIterator().next();
    }


    /**
     * Retrieves the next series of rows in the iterator. The number of
     * rows retrieved is specified as a parameter to this method. If the
     * iterator does not have the specified number of rows remaining, it
     * only retrieves the remaining rows.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>AssumedMatchSummary[]</CODE> - An array of
     * AssumedMatchSummary objects.
     * @exception PageException Thrown if the index is
     * already at the end of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary[] next(int count)
        throws RemoteException, PageException {
        ArrayList list = getGenericIterator().next(count);
        return listToArray(list);
    }


    /**
     * Retrieves the previous row from an AssumedMatchIterator object. If
     * the current index is 5, this method returns the row at index=4 and leaves
     * the position at index=4.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>AssumedMatchSummary</CODE> - An
     * AssumedMatchSummary object.
     * @exception PageException Thrown if the index is already at the
     * beginning of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary prev()
        throws PageException, RemoteException {
        return (AssumedMatchSummary) getGenericIterator().prev();
    }


    /**
     * Retrieves the previous series of rows in the iterator. The number of
     * rows retrieved is specified as a parameter to this method.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>AssumedMatchSummary[]</CODE> - An array of
     * AssumedMatchSummary objects.
     * @exception PageException Thrown if the index is
     * already at the beginning of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public AssumedMatchSummary[] prev(int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().prev(count);
        return listToArray(list);
    }


    /**
     * Sorts the objects in the AssumedMatchIterator by the specified 
     * comparator. This method loads the objects to be sorted.
     * <p>
     * @param field The name of the field to use as sorting criteria.
     * @param reverse An indicator of whether to sort in ascending or
     * descending order. Specify <b>true</b> to sort in descending
     * order, or specify <b>false</b> to sort in ascending order.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception PageException Thrown if there is an error
     * during sorting.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public void sortBy(String field, boolean reverse)
        throws PageException, RemoteException {
        Comparator c = new ObjectNodeComparator(field, reverse);
        getGenericIterator().sortBy(c);
    }
    /**
     * Sorts the objects in the AssumedMatchIterator by the specified comparator. 
     * This method does not load the associated objects, improving the performance
     * of the action.
     * <p>
     * @param field The name of the field to use as sorting criteria.
     * @param reverse An indicator of whether to sort in ascending or
     * descending order. Specify <b>true</b> to sort in descending
     * order, or specify <b>false</b> to sort in ascending order.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception PageException Thrown if there is an error
     * during sorting.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public void sortSummaryBy(String field, boolean reverse)
        throws PageException, RemoteException {
        Comparator c = new ObjectNodeComparator(field, reverse);
        getGenericIterator().sortSummaryBy(c);
    }

    /** Convert list to array
     * @param list list to convert
     * @return array
     */
    private AssumedMatchSummary[] listToArray(ArrayList list) throws PageException {
        Object[] objArray = list.toArray();
        int size = list.size();
        AssumedMatchSummary[] result = new AssumedMatchSummary[size];
        for (int i = 0; i < size; i++) {
            result[i] = (AssumedMatchSummary) objArray[i];
            
        }
        return result;
    }

    


}
