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
package com.sun.mdm.index.querybuilder;

import java.util.HashMap;
import java.util.Iterator;

import com.sun.mdm.index.util.Constants;


/**
 * Generic class representing a set of options for executing a query
 * @author Dan Cidon
 */
public class SearchOptions
         implements java.io.Serializable {

    private final HashMap mHashMap = new HashMap();
    /** Max elements
     */    
    protected int mMaxElements;
    /** Page size
     */    
    protected int mPageSize;
    
    /*
     *  used to specify QueryObject maxobjects limit
     */
    private int mMaxQueryElements;


    /** Constructor
     */
    public SearchOptions() {
        //mPageSize = 20;
        mPageSize = Constants.DEFAULT_PAGE_SIZE;
        mMaxElements = 0;
    }


    /** Retrieves boolean option
     * @return boolean value
     * @param name option name
     */
    public boolean getBooleanOption(String name) {
        Boolean b = (Boolean) mHashMap.get(name);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }


    /** Max number of records to retrieve.
     * @return max elements to retrieve
     */
    public int getMaxElements() {
        return mMaxElements;
    }
    
    /** Absolute Max number of records to retrieve.
     * limit set by system
     * @return max elements to retrieve
     */
    public int getMaxQueryElements() {
        return mMaxQueryElements;
    }



    /** Getter for search specific options
     *
     * @return options value
     * @param name option name
     */
    public String getOption(String name) {
        return (String) mHashMap.get(name);
    }


    /** Get all available option names
     * @return option name array
     */
    public String[] getOptionNames() {
        String[] retArray = new String[mHashMap.size()];
        Object[] keys = mHashMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            retArray[i] = (String) keys[i];
        }
        return retArray;
    }


    /** Number of records transferred to caller when data is required.
     * @return page size
     */
    public int getPageSize() {
        return mPageSize;
    }


    /** Set max number of records to retrieve.
     *
     * @param maxElements max elements
     * @return max elements
     */
    public int setMaxElements(int maxElements) {
        return mMaxElements = maxElements;
    }

    /** Sets  max number of records to retrieve from QueryManager
     * So this sets the limit of QueryObject
    *
    * @param maxElements max elements
    * @return max elements
    */
   public int setMaxQueryElements(int maxElements) {
       return mMaxQueryElements = maxElements;
   }

    /**
     * Setter for search specific options.
     *
     * @param name option
     * @param value option
     * @todo Document: Setter for Option attribute of the SearchOptions object
     */
    public void setOption(String name, String value) {
        mHashMap.put(name, value);
    }


    /**
     * See getPageSize
     *
     * @param pageSize page size
     */
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }


    /** String representation
     * @return string representation
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator i = mHashMap.keySet().iterator();
        sb.append("Page size: ").append(mPageSize).append('\n');
        sb.append("Max elements: ").append(mMaxElements).append('\n');
        while (i.hasNext()) {
            Object key = i.next();
            sb.append('\n').append('[').append(key).append(',');
            sb.append(mHashMap.get(key)).append(']');
        }
        return sb.toString();
    }

}
