/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)SearchScreenOptions.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import java.util.Iterator;
import java.util.ArrayList;


/**
 * Search screen options configurations
 *
 * @author rtam
 * @created July 27, 2007
 */
public class SearchScreenOptions implements java.io.Serializable {
    
    private String mDisplayName;    // Display name for these options
    private String mQueryBuilder;   // name of the QueryBuilder to use
    private boolean mIsWeighted;    // indicates this search is to be weighted
    private int mCandidateThreshold;    // candidate threshold value
    private ArrayList mNameValuePairs;  // array list of parameters and their
                                        // associated values
    

    /**
     * Creates a new instance of the SearchScreenOptions class
     *
     * @param displayName  This is the name of the search screen options.
     * @param queryBuilder  This is the name of the querybuilder object to be used.
     * @param isWeighted Indicates if search is a weighted search.
     * @param candidateThreshold This is the candidate threshold value.
     * @param nameValuePairs  This is an ArrayList of NameValuePair objects
     * that define the parameter name and corresponding value.
     */
    public SearchScreenOptions(String displayName, String queryBuilder, 
                               boolean isWeighted, int candidateThreshold,
                               ArrayList nameValuePairs) {
                    
        mDisplayName = displayName;
        mQueryBuilder = queryBuilder;
        mIsWeighted = isWeighted;
        mCandidateThreshold = candidateThreshold;
        mNameValuePairs = nameValuePairs;
    }
    
    /**
     * Getter for the mDisplayName attribute
     *
     * @return The display name for this screen option
     */
    public String getDisplayName() {
        return mDisplayName;
    }
    
     /**
     * Getter for the mQueryBuilder attribute
     *
     * @return The name of the QueryBuilder to use.
     */
    public String getQueryBuilder() {
        return mQueryBuilder;
    }
    
    /**
     * Getter for the mIsWeighted attribute
     *
     * @return Boolean value indicating whether or not to display the
     * search is weighted.
     */
    public boolean getIsWeighted() {
        return mIsWeighted;
    }
  
    /**
     * Getter for the mCandidateThreshold attribute
     *
     * @return Candidate threshold value.
     */
    public int getCandidateThreshold() {
        return mCandidateThreshold;
    }
  
    /**
     * Getter for the mNameValuePairs attribute
     *
     * @return ArrayList of FieldConfig objects.
     */
    public ArrayList getNameValuePairs() {
        return mNameValuePairs;
    }
    
}