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
package com.sun.mdm.index.ejb.page;

import com.sun.mdm.index.page.PageAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import com.sun.mdm.index.page.PageException;


/**
 * Interface for PageDataEJB
 */

public interface PageData{

    /** Get the next "count" number of records. For example, if current row
     * position is 3, and count is 5, return rows 4,5,6,7,8 and place row
     * position at 8. If less than "count" number of records left, return all
     * records to end of set, and place row position atend of set. If no records
     * are left, return null and leave row position as is.
     *
     * @param count Number of records to get
     * @exception PageException An error has occured.
     * @return List of records
     */
    ArrayList next(int count) throws PageException;


    /** Get the previous "count" number of records. For example, if current row
     * position is 8, and count is 5, return rows 7,6,5,4,3 and place row
     * position at 3. If less than "count" number of records left, return all
     * records to beginning of set, and set row position to 0. If no records are
     * left, return null and leave row position at 0.
     *
     * @param count Number of records to get
     * @exception PageException An error has occured.
     * @return List of records
     */
    ArrayList prev(int count) throws PageException;


    /** Same as next(count) however position is changed to index first.
     *
     * @param index Starting point of retrieval.
     * @param count Number of records to get
     * @exception PageException An error has occured.
     * @return List of records
     */
    ArrayList next(int index, int count) throws PageException;

    /** Set the forward only mode that will clear all the DataPage objects of 
     * of a given loaded page i when we start reading/loading the next page i+1.
     * 
     * @param forwardOnly the forward only flag
     * @exception PageException An error has occured.
     */    
    void setReadForwardOnly(boolean forwardOnly) throws PageException;

    /** Same as prev(count) however position is changed to index first.
     *
     * @param index Starting point of retrieval.
     * @param count Number of records to get
     * @exception PageException An error has occured.
     * @return List of records
     */
    ArrayList prev(int index, int count) throws PageException;

    /** Return total number of records in set.
     *
     * @exception PageException An error has occured.
     * @return Number of records in data source.
     */
    int count() throws PageException;


    /** Sort the set based on the comparator.
     *
     * @param c Comparator by which to sort.
     * @exception PageException An error has occured.
     */
    void sort(Comparator c) throws PageException;
    
    /** Sort the set based on the comparator.
     *
     * @param c Comparator by which to sort.
     * @exception PageException An error has occured.
     */
    void sortSummary(Comparator c) throws PageException;   
    
    /** Set the PageAdaptor for data source
     * 
     * @param pageAdapter PageAdaptor for data source
     * @exception PageException An error has occured.
     */    
    void setPageAdapter(PageAdapter pageAdapter) throws PageException;
    
    /** remove the PageAdaptor 
     *  
     */   
    void remove();

}
