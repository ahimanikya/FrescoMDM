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

import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.querybuilder.SearchOptions;


/**
 * The <b>EOSearchOptions</b> class represents a specific sub-class of the
 * <b>SearchOptions</b> class, and is used by <b>searchEnterpriseObject</b>
 * as criteria for an enterprise object search. Methods in this class use
 * fully qualified field names (see <i>Creating a Master Index with eView Studio</i>
 * for more information about field names).
 */
public class EOSearchOptions extends SearchOptions {
    /** Fields to retrieve
     */
    private EPathArrayList mFieldsToRetrieve;


    /** Search id to execute
     */
    private String mSearchId;
    /** Set true to send to MEC
     */
    private boolean mWeighted;
    /** Maximum rows to return
     */
    private int mCandidateThreshold = 0;


    /**
     * Creates a new instance of the EOSearchOptions class. The query specified
     * in this method transforms the system object in an EOSearchCriteria object
     * to a set of query objects.
     * <p>
     * @param searchId The name of the query to be used. The possible values for
     * this parameter are defined in the Candidate Select configuration file.
     * @param fields An array of fully qualified field names pointing to the fields
     * to retrieve for the query. Fully qualified field names allow you to define
     * fields within the context of the enterprise object; that is, the field name
     * uses "Enterprise" as the root. For example,
     * <b>Enterprise.SystemSBR.Person.FirstName</b>.
     * @exception ConfigurationException Thrown when a parameter is invalid.
     * @include
     */
    public EOSearchOptions(String searchId, EPathArrayList fields)
        throws ConfigurationException {
        mSearchId = searchId;
        setFieldsToRetrieve(fields);
    }


    /**
     * Creates a new instance of the EOSearchOptions class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @exception ConfigurationException Thrown when a parameter is invalid.
     * @include
     */
    public EOSearchOptions()
        throws ConfigurationException {
    }


    /**
     * Retrieves an array of fully qualified field names pointing to the location
     * of the fields to retrieve from a search for an enterprise object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EPathArrayList</CODE> - An array containing the fully qualified
     * field names for the fields to retrieve.
     * @exception ConfigurationException Thrown when a parameter is invalid.
     * @include
     */
    public EPathArrayList getFieldsToRetrieve()
        throws ConfigurationException {
        return mFieldsToRetrieve;
    }


    /**
     * Retrieves the value of the search ID (query name) defined in
     * an EOSearchOptions object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return search id - The name of the query used in the specified
     * instance of EOSearchOptions.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getSearchId() {
        return mSearchId;
    }


    /**
     * Retrieves the value set by the setWeighted class, which determines
     * whether query results are weighted.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return boolean A <b>boolean</b> value specifying whether the search
     * is weighted.
     * <DT><B>Throws:</B><DD>None.
     */
    public boolean isWeighted() {
        return mWeighted;
    }

    /**
     * Retrieves the value set by the setMaximumRows, which determines
     * the maximuim rows that can be returned.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return int A <b>int</b> value specifying the maximum rows 
     * that can be returned.
     * <DT><B>Throws:</B><DD>None.
     */
     public int getCandidateThreshold() {
         return mCandidateThreshold;
     }

    /**
     * Sets the fields to retrieve for an enterprise object search, using the
     * ePaths of the fields.
     * <p>
     * @param fields An array containing the fully qualified field names for
     * the fields to retrieve.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ConfigurationException Thrown when a parameter is invalid.
     * @include
     */
    public void setFieldsToRetrieve(EPathArrayList fields)
        throws ConfigurationException {
        mFieldsToRetrieve = fields;
    }


    /**
     * Sets the search ID (the name of the query) for an enterprise object search.
     * <p>
     * @param searchId A query name (available names are defined in the
     * Candidate Select configuration file).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSearchId(String searchId) {
        mSearchId = searchId;
    }


    /**
     * Specifies whether query results are weighted. .
     * <p>
     * @param weighted if enabled, query results are forwarded to MEFA
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ConfigurationException Thrown when a parameter is invalid.
     */
    public void setWeighted(boolean weighted)
        throws ConfigurationException {
        mWeighted = weighted;
    }

    /**
     * Set the maximum row to return.
     * <p>
     * @param count Maximum rows
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ConfigurationException Thrown when a parameter is invalid.
     */
    public void setCandidateThreshold(int count) {
        if (count > 0) {
            mCandidateThreshold = count;
        }
    }

     /** String representation
      * @todo Document this method
      * @return String representation
      */
    public String toString() {
        return "Search Id: " + mSearchId + "\nWeighted: " + mWeighted + '\n'
        + "Field to retrieve: " + mFieldsToRetrieve + '\n' + super.toString();
    }

}
