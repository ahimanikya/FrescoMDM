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
import com.sun.mdm.index.util.Localizer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJBContext;

/**
 * Adapts any array for use in PageIterator
 * @author Dan Cidon
 */
public class ArrayPageAdapter implements PageAdapter, java.io.Serializable {

    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Current row position
     */    
    private int mPosition = 0;
    /** Forward only mode 
     */ 
    private boolean mForwardOnly = false;     
    /** Data source stored as array
     */    
    private final Object[] mObjArray;


    /** Creates a new instance of ArrayPageAdapter
     *
     * @param objArray Data source as an array
     */
    public ArrayPageAdapter(Object[] objArray) {
        mObjArray = objArray;
    }


    /** Creates a new instance of ArrayPageAdapter
     * @param objList Data source as a list
     */
    public ArrayPageAdapter(List objList) {
        this(objList.toArray());
    }


    /** See PageAdapter
     * @param index See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void setCurrentPosition(int index)
        throws PageException {
        mPosition = index;
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
     */
    public void activate() {
    }


    /** See PageAdapter
     */
    public void close() {
    }


    /** See PageAdapter
     * @exception PageException See PageAdapter
     * @return See PageAdapter
     */
    public int count()
        throws PageException {
        return mObjArray.length;
    }


    /** See PageAdapter
     * @return See PageAdapter
     */
    public boolean hasNext() {
        return (mPosition < mObjArray.length);
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
        if (mPosition == mObjArray.length) {
            throw new PageException(mLocalizer.t("PAG500: Page out of bounds."));
        }
        return mObjArray[mPosition++];
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
        if (mPosition == 0) {
            throw new PageException(mLocalizer.t("PAG501: Page out of bounds."));
        }
        return mObjArray[--mPosition];
    }


    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sort(Comparator c)
        throws PageException {
        Arrays.sort(mObjArray, c);
    }
    /** See PageAdapter
     * @param c See PageAdapter
     * @exception PageException See PageAdapter
     */
    public void sortSummary(Comparator c)
        throws PageException {
        sort(c);
    }
}
