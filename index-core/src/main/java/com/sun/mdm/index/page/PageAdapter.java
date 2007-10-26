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

import java.util.Comparator;

/**
 * Page adapter interface
 * @author dcidon
 */
public interface PageAdapter {

    /** Get a count of all the objects in the data source
     *
     * @exception PageException An error occured.
     * @return Number of records in adapter
     */
    int count()
        throws PageException;

    /** Sort the records based on comparator
     * @param c Comparator to sort by
     * @exception PageException An error occured.
     */
    void sort(Comparator c)
        throws PageException;

    /** Sort the records based on comparator
     * @param c Comparator to sort by
     * @exception PageException An error occured.
     */
    void sortSummary(Comparator c)
        throws PageException;
    
    /** Get the next element
     * @exception PageException An error occured.
     * @return Next element
     */
    Object next()
        throws PageException;

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * @param forwardOnly forward only mode
     * @exception PageException An error occured. 
     */    
    void setReadForwardOnly(boolean forwardOnly) 
        throws PageException;
    
    /** Get the previous element
     * @exception PageException An error occured.
     * @return Previous element
     */
    Object prev()
        throws PageException;

    /** Return true if there are more records
     * @return True if there are more records
     * @throws PageException An error occured
     */
    boolean hasNext()
        throws PageException;

    /** Set the current position of the adapter
     * @param index current position
     * @exception PageException An error occured.
     */
    void setCurrentPosition(int index)
        throws PageException;

    /**
     * When the stateful session bean passivates, it will first call this method
     * on the data source. If there are any resources that must be freed prior
     * to passivation, this is the time to do it.
     */
    void passivate();

    /**
     * When the stateful session bean activates, it will first call this method.
     */
    void activate();

    /**
     * After the timeout period has expired, this method is called to allow the
     * data source to perform resource conservation activities.
     */
    void idleTimeOut();

    /**
     * When the stateful session bean is removed, it will first call this
     * method.
     */
    void close();
   
}
