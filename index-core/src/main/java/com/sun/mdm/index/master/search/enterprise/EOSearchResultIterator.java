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
package com.sun.mdm.index.master.search.enterprise;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.page.PageIteratorWrapper;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.util.Localizer;



/**
 * The <b>EOSearchResultIterator</b> class represents an iterator containing
 * the EOSearchResultRecord objects returned by a call to
 * <b>searchEnterpriseObject</b>. Uses indexes to page through the
 * EOSearchResultRecord objects and to retrieve specific objects from the
 * iterator.
 */
public class EOSearchResultIterator extends PageIteratorWrapper
{
    private transient final Localizer mLocalizer = Localizer.get();


    /** Creates a new instance of the EOSearchResultIterator class that
     * maintains the connection to the server.
     * <p>
     * @param pageData A session bean that stores the data retrieved from
     * the server.
     * @param pageSize The size of each unit of data retrieved from the
     * server.
     * @param maxElements The maximum number of elements retrieved for an
     * enterprise object search.
     * @exception PageException An error occurred while creating
     * the iterator.
     * @include
     */
    public EOSearchResultIterator(PageData pageData, int pageSize,
        int maxElements) throws PageException {
        super(pageData, pageSize, maxElements);
       
    }


    /** Creates a new instance of the EOSearchResultIterator class that does not
     * maintain the connection to the server.
     * <p>
     * @param fullData An array of all objects in the result set.
     * @exception PageException Thrown if an error occurs while creating
     * the iterator.
     * @include
     */
    public EOSearchResultIterator(EOSearchResultRecord[] fullData)
        throws PageException {
        super(fullData);
        
    }


    /** Retrieves an EOSearchResultRecord object at the specified
     * index from the EOSearchResultIterator object.
     * <p>
     * @param index The row to retrieve.
     * @return <CODE>EOSearchResultRecord</CODE> - An
     * EOSearchResultRecord object.
     * @exception PageException Thrown if an error occurs while
     * retrieving the object.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord absolute(int index)
        throws PageException, RemoteException {
        return (EOSearchResultRecord) getGenericIterator().absolute(index);
    }


    /** Retrieves a series of rows from an EOSearchResultIterator
     * object, beginning with the row at the specified index and continuing
     * through the specified number of rows.
     * <p>
     * @param index The first row to retrieve.
     * @param count The total number of rows to retrieve.
     * @return <CODE>EOSearchResultRecord[]</CODE> - An array of
     * EOSearchResultRecord objects.
     * @exception PageException Thrown if an error occurs while
     * retrieving the objects.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord[] absolute(int index, int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().absolute(index, count);
        return listToArray(list);
    }


    /** Retrieves the first row from an EOSearchResultIterator object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EOSearchResultRecord</CODE> - An
     * EOSearchResultRecord object.
     * @exception PageException Thrown if an error occurs while
     * retrieving the object.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord first()
        throws PageException, RemoteException {
        EOSearchResultRecord rec = (EOSearchResultRecord) getGenericIterator().first();
        return (rec);
    }


    /** Retrieves the first rows in the iterator. The number of rows retrieved
     * is specified as a parameter to this method. If the number of rows
     * specified by the parameter is larger than the number of records in the
     * iterator, all rows are retrieved.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>EOSearchResultREcord[]</CODE> - An array of
     * EOSearchResultREcord objects.
     * @exception PageException Thrown if an error occurs while
     * retrieving the objects.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord[] first(int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().first(count);
        return listToArray(list);
    }


   


    /** Retrieves the next row from an EOSearchResultIterator object. If
     * the current index is 4, this method returns the row at index=4.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EOSearchResultRecord</CODE> - An
     * EOSearchResultRecord object.
     * @exception PageException Thrown if the call to hasNext
     * returns false.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord next()
        throws RemoteException, PageException {
        EOSearchResultRecord rec = (EOSearchResultRecord) getGenericIterator().next();
        return (rec);
    }


    /** Retrieves the next series of rows in the iterator. The number of
     * rows retrieved is specified as a parameter to this method. If the
     * iterator does not have the specified number of rows remaining, it
     * only retrieves the remaining rows.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>EOSearchResultRecord[]</CODE> - An array of
     * EOSearchResultRecord objects.
     * @exception PageException Thrown if the index is already
     * at the end of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord[] next(int count)
        throws RemoteException, PageException {
        ArrayList list = getGenericIterator().next(count);
        return listToArray(list);
    }


    /** Retrieves the previous row from an EOSearchResultIterator object. If
     * the current index is 5, this method returns the row at index=4 and leaves
     * the position at index=4.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EOSearchResultRecord</CODE> - An EOSearchResultRecord
     * object.
     * @exception PageException Thrown if the index is already at the
     * beginning of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord prev()
        throws PageException, RemoteException {
        EOSearchResultRecord rec = (EOSearchResultRecord) getGenericIterator().prev();
        return (rec);
    }


    /** Retrieves the previous series of rows in the iterator. The number of
     * rows retrieved is specified as a parameter to this method.
     * <p>
     * @param count The total number of rows to retrieve.
     * @return <CODE>EOSearchResultRecord[]</CODE> - An array of
     * EOSearchResultRecord objects.
     * @exception PageException Thrown if the index is
     * already at the beginning of the iterator.
     * @exception RemoteException Thrown if the connection to
     * the server goes down during processing.
     * @include
     */
    public EOSearchResultRecord[] prev(int count)
        throws PageException, RemoteException {
        ArrayList list = getGenericIterator().prev(count);
        return listToArray(list);
    }


    /** Sorts the objects in the EOSearchResultIterator by the
     * specified comparator.
     * <p>
     * @param field The name of field to use as sorting criteria.
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
        throws RemoteException, PageException {
        Comparator c = new EOSearchResultComparator(field, reverse);
        getGenericIterator().sortBy(c);
    }


    /** Convert list to array
     * @return array of EOSearchResults
     * @param list input list
     * @throws PageException An error occurred.
     */
    private EOSearchResultRecord[] listToArray(ArrayList list)
        throws PageException {
        try {
            Object[] objArray = list.toArray();
            int size = list.size();
            EOSearchResultRecord[] result = new EOSearchResultRecord[size];
            SBR sbr = new SBR();
            for (int i = 0; i < size; i++) {
                result[i] = (EOSearchResultRecord) objArray[i];
                
            }
            return result;
        } catch (Exception e) {
            throw new PageException(mLocalizer.t("MAS509: Could not " + 
                                "convert an ArrayList to an " + 
                                "EOSearchResultRecord array: {0}", e));
        }
    }

    

}
