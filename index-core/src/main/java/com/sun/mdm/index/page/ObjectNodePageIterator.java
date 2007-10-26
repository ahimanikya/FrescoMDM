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
import java.util.Comparator;
import java.util.ArrayList;
import javax.ejb.RemoveException;
import com.sun.mdm.index.ejb.page.PageData;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNodeComparator;

/**
 * Page Iterator for Object Nodes
 * @author Dan Cidon
 */
public class ObjectNodePageIterator {

    /** Underlying object node page iterator
     */    
    private final PageIterator mPageIterator;


    /** Creates a new instance of ObjectNodePageIterator that maintains
     * connection to server.
     *
     * @param pageData Iterator based on server side bean
     * @param pageSize Size of page
     * @param maxElements Maximum elements that will be loaded
     */
    public ObjectNodePageIterator(PageData pageData, int pageSize, 
        int maxElements) {
        mPageIterator = new PageIterator(pageData, pageSize, maxElements);
    }


    /** Creates a new instance of ObjectNodePageIterator that does not maintain
     * connection to server.
     *
     * @param fullData Full data array
     */
    public ObjectNodePageIterator(ArrayList fullData) {
        mPageIterator = new PageIterator(fullData);
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#absolute(int)
     * @param index See PageIterator
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode absolute(int index)
        throws PageException, RemoteException {
        return (ObjectNode) mPageIterator.absolute(index);
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#absolute(int,int)
     * @param index See PageIterator
     * @param count See PageIterator
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode[] absolute(int index, int count)
        throws PageException, RemoteException {
        ArrayList list = mPageIterator.absolute(index, count);
        return listToArray(list);
    }


    /** See PageIterator
     * @exception RemoteException See PageIterator
     * @exception RemoveException See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#close()
     */
    public void close()
        throws RemoteException, RemoveException {
        mPageIterator.close();
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#count()
     * @exception RemoteException See PageIterator
     * @exception PageException See PageIterator
     * @return See PageIterator
     */
    public int count()
        throws PageException, RemoteException {
        return mPageIterator.count();
    }


    /** See PageIterator
     * @return See PageIterator
     */
    public int currentPosition() {
        return mPageIterator.currentPosition();
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#first()
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode first()
        throws PageException, RemoteException {
        return (ObjectNode) mPageIterator.first();
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#first(int)
     * @param count See PageIterator
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode[] first(int count)
        throws PageException, RemoteException {
        ArrayList list = mPageIterator.first(count);
        return listToArray(list);
    }


    /** See PageIterator
     * @return See PageIterator
     * @throws PageException See PageIterator
     * @exception RemoteException See PageIterator
     */
    public boolean hasNext()
        throws PageException, RemoteException {
        return mPageIterator.hasNext();
    }


     /** See PageIterator
      * @see com.sun.mdm.index.page.PageIterator#next()
      * @exception RemoteException See PageIterator
      * @exception PageException See PageIterator
      * @return See PageIterator
      */
    public ObjectNode next()
        throws RemoteException, PageException {
        return (ObjectNode) mPageIterator.next();
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#next(int)
     * @param count See PageIterator
     * @exception RemoteException See PageIterator
     * @exception PageException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode[] next(int count)
        throws RemoteException, PageException {
        ArrayList list = mPageIterator.next(count);
        return listToArray(list);
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#prev()
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode prev()
        throws PageException, RemoteException {
        return (ObjectNode) mPageIterator.prev();
    }


    /** See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#prev(int)
     * @param count See PageIterator
     * @exception PageException See PageIterator
     * @exception RemoteException See PageIterator
     * @return See PageIterator
     */
    public ObjectNode[] prev(int count)
        throws PageException, RemoteException {
        ArrayList list = mPageIterator.prev(count);
        return listToArray(list);
    }


    /** Sort by field name
     * @param reverse Set true to reverse sort order
     * @param field Field name to sort on
     * @exception PageException An error occured.
     * @exception RemoteException An error occured.
     */
    public void sortBy(String field, boolean reverse)
        throws PageException, RemoteException {
        Comparator c = new ObjectNodeComparator(field, reverse);
        mPageIterator.sortBy(c);
    }


    /** Convert list to array
     * @param list List to convert
     * @return Return ObjectNode array
     */    
    private ObjectNode[] listToArray(ArrayList list) {
        Object[] objArray = list.toArray();
        int size = list.size();
        ObjectNode[] result = new ObjectNode[size];
        for (int i = 0; i < size; i++) {
            result[i] = (ObjectNode) objArray[i];
        }
        return result;
    }

}
