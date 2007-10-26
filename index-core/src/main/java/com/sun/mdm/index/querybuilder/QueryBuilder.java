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

import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.query.QueryObject;


/**
 * QueryBuilder takes criteria and options and transforms them into a set of
 * zero or more query objects.
 * @author Dan Cidon
 */
public abstract class QueryBuilder {

    private boolean mPhoneticizeRequired = false;
    private boolean mStandardizeRequired = false;


    /** A query builder can build more than one query object for a given system
     * object input. This method returns an array of applicable ids for each
     * query object.
     *
     * @param crit Search criteria
     * @param opts Search options
     * @exception QueryBuilderException an error occured
     * @return applicable id array
     */
    public abstract String[] getApplicableQueryIds(SearchCriteria crit, SearchOptions opts)
        throws QueryBuilderException;


    /** Return true if phoneticization required
     * @return true or false
     */
    public boolean isPhoneticizeRequired() {
        return mPhoneticizeRequired;
    }


    /** Return true if Standardization required
     * @return true or false
     */
    public boolean isStandardizeRequired() {
        return mStandardizeRequired;
    }


    /** Set phoneticize required
     * @param val phoneticize required
     */
    public void setPhoneticizeRequired(boolean val) {
        mPhoneticizeRequired = val;
    }


    /** Set standardize required
     * @param val standardize required
     */
    public void setStandardizeRequired(boolean val) {
        mStandardizeRequired = val;
    }


    /*
          Given an id (one of the set of ids from getApplicableIds), return a
          query object.
      */
    /** Build the query objects
     * @return query objects
     * @param ids applicable ids
     * @param crit search criteria
     * @param opts search options
     * @exception QueryBuilderException an error occured
     */
    public abstract QueryObject buildQueryObject(String ids[], SearchCriteria crit, SearchOptions opts)
        throws QueryBuilderException;


    /** Initialize the builder
     * @param info configuration info
     * @exception QueryBuilderException an error occured
     */
    public abstract void init(ConfigurationInfo info)
        throws QueryBuilderException;

}
