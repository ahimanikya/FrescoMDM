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
import com.sun.mdm.index.ejb.page.PageData;


/**
 * Base wrapper for PageIterator
 * @author Dan Cidon
 */
public abstract class PageIteratorWrapper implements java.io.Serializable {

    /** Page iterator that is wrapped
     */    
    private final PageIterator mPageIterator;


    /** Creates a new instance of PageIteratorWrapper that maintains connection
     * to server.
     *
     * @param pageData Handle to server side data
     * @param pageSize Size of page
     * @param maxElements Max elements to retrieve from server
     */
    public PageIteratorWrapper(PageData pageData, int pageSize, 
        int maxElements) {
        mPageIterator = new PageIterator(pageData, pageSize, maxElements);
    }


    /** Creates a new instance of PageIteratorWrapper that does not maintain
     * connection to server.
     *
     * @param fullData Full data source as array
     */
    public PageIteratorWrapper(ArrayList fullData) {
        mPageIterator = new PageIterator(fullData);
    }


    /** Creates a new instance of PageIteratorWrapper that does not maintain
     * connection to server.
     *
     * @param fullData Full data provided in array
     */
    public PageIteratorWrapper(Object[] fullData) {
        mPageIterator = new PageIterator(fullData);
    }


    /** See PageIterator
     * @exception RemoteException See PageIterator
     * @see com.sun.mdm.index.page.PageIterator#close()
     */
    public void close()
        throws RemoteException {
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
     * @return See PageIterator
     * @throws PageException See PageIterator
     * @exception RemoteException See PageIterator
     */
    public boolean hasNext()
        throws PageException, RemoteException {
        return mPageIterator.hasNext();
    }


    /** Get the underlying generic page iterator
     * @return Iterator
     */
    protected PageIterator getGenericIterator() {
        return mPageIterator;
    }

}
