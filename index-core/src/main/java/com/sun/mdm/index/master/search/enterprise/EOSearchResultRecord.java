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

import com.sun.mdm.index.objects.ObjectNode;



/**
 * The <b>EOSearchResultRecord</b> class represents an object containing a
 * record returned by a call to <b>MasterController.searchEnterpriseObject</b>.
 * A search for an enterprise object returns an iterator of EOSearchResultRecord
 * objects.
 */
public class EOSearchResultRecord implements java.io.Serializable, Comparable {

    /** Comparison score
     */
    private float mComparisonScore;
    /** EUID
     */
    private String mEUID;
    /** Object node retrieved
     */
    private ObjectNode mResultRow;


    /**
     * Creates a new instance of the EOSearchResultRecord class.
     * <p>
     * @param comparisonScore The matching probability weight between a
     * search result record and the search criteria.
     * @param euid The EUID of a search result record.
     * @param resultRow The object node of a search result record.
     * @include
     */
    public EOSearchResultRecord(float comparisonScore, String euid,
        ObjectNode resultRow) {
        mComparisonScore = comparisonScore;
        mEUID = euid;
        mResultRow = resultRow;
    }


    /**
     * Creates a new instance of the EOSearchResultRecord class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public EOSearchResultRecord() {
    }


    /**
     * Retrieves the comparison score (the <i>matching probability
     * weight</i>) for a search result record. If the query is non-weighted,
     * this method returns 0.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return float - The matching probability weight for the record.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public float getComparisonScore() {
        return mComparisonScore;
    }


    /**
     * Retrieves the EUID for a row of data (an EOSearchResultRecord object).
     * This method only returns an EUID for weighted queries, and returns an
     * empty string for non-weighted queries. For non-weighted queries, use
     * the getValues method in the ResultObject class to retrieve an EUID for
     * a search result record.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return String - The EUID of an EOSearchResultRecord object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getEUID() {
        return mEUID;
    }


    /**
     * Retrieves a result row from a system object in the EOSearchResultRecord
     * object. The fields retrieved are specified by
     * EOSearchOptions.getFieldsToRetrieve.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ObjectNode</CODE> - The specified fields in the
     * given object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ObjectNode getObject() {
        return (ObjectNode) mResultRow;
    }


    /**
     * Sets the comparison score (the <i>matching probability
     * weight</i>) for a search result record.
     * <p>
     * @param comparisonScore The matching probability weight for the
     * record.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setComparisonScore(float comparisonScore) {
        mComparisonScore = comparisonScore;
    }


    /**
     * Sets the EUID for a search result record.
     * <p>
     * @param val The EUID of the record.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEUID(String val) {
        mEUID = val;
    }


    /**
     * Sets the result row (ObjectNode) for a search result record.
     * <p>
     * @param objectNode The result row of the record.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setObject(ObjectNode objectNode) {
        mResultRow = objectNode;
    }


    /** Standard compare function
     * @param obj retrieved object
     * @return standard compare value
     */
    public int compareTo(Object obj) {
        EOSearchResultRecord other = (EOSearchResultRecord) obj;
        return Float.compare(this.mComparisonScore, other.mComparisonScore);
    }


    /**
     * Retrieves the EUID and comparison score (the <i>matching probability
     * weight</i>) for a search result record.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>string</CODE> - A string representation of the EUID and
     * the comparison weight.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return mEUID + ": " + mComparisonScore;
    }

}
