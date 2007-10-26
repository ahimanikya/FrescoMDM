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
package com.sun.mdm.index.master.search.assumedmatch;

import com.sun.mdm.index.querybuilder.SearchOptions;


/**
 * The <b>AssumedMatchSearchObject</b> class is an object representing the
 * search criteria passed to <b>lookupAssumedMatches</b> when performing
 * a search for assumed matches. Use the methods in this class to
 * populate or retrieve the fields in the search object, and to define the fields
 * in the assumed match search results list.
 */
public class AssumedMatchSearchObject extends SearchOptions
    implements java.io.Serializable {

    /** An assumed match ID
     */
    private String assumedMatchId;

    /** EUID array
     */
    private String[] euids;
    /** System code
     */
    private String systemCode;
    /** Local ID
     */
    private String lid;
    /** Create user
     */
    private String createUser;
    /** Create start date
     */
    private java.sql.Timestamp createStartDate;
    /** Create end date
     */
    private java.sql.Timestamp createEndDate;
    /** Maximum elements to retrieve
     */
    private int mMaxElements;
    /** Page size
     */
    private int mPageSize;

    /** Creates a new instance of the AssumedMatchSearchObject
     * class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public AssumedMatchSearchObject() {
    }

    /**
     * Retrieves the value of the <b>systemCode</b> field to be used in an
     * assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string containing a system code.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getSystemCode() {
        return systemCode;
    }

    /**
     * Sets the system code for an instance of AssumedMatchSearchObject.
     * <p>
     * @param systemCode A string containing a system code.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    /**
     * Retrieves the value of the <b>lid</b> field to be used in an
     * assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the local ID field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getLID() {
        return lid;
    }

    /**
     * Sets the local ID for an instance of AssumedMatchSearchObject.
     * <p>
     * @param lid A string containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setLID(String lid) {
        this.lid = lid;
    }

    /**
     * Retrieves the assumed match ID (assigned by the master index)
     * for an instance of AssumedMatchSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The unique identifier for the
     * assumed match.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getAssumedMatchId() {
        return assumedMatchId;
    }

    /**
     * Sets the assumed match ID for an instance of AssumedMatchSearchObject.
     * <p>
     * @param assumedMatchId A string containing the unique identifier
     * for the assumed match.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setAssumedMatchId(String assumedMatchId) {
        this.assumedMatchId = assumedMatchId;
    }

    /**
     * Sets the enterprise-wide identifier (EUID) for an instance of
     * AssumedMatchSearchObject.
     * <p>
     * @param euid A string containing the desired EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEUID(String euid) {
        this.euids = new String[1];
        this.euids[0] = euid;
    }

    /**
     * Sets an array of enterprise-wide identifiers (EUIDs) for an
     * instance of AssumedMatchSearchObject.
     * <p>
     * @param euids A string array containing the desired EUIDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEUIDs(String[] euids) {
        this.euids = euids;
    }

    /**
     * Retrieves a string array of EUIDs to use as criteria for an
     * assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String[]</CODE> - A string array containing
     * EUIDs.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String[] getEUIDs() {
        return euids;
    }

    /**
     * Retrieves the value of the <b>createUser</b> field (the user ID of
     * the user who created the assumed match record) to be used in
     * an assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getCreateUser() {
        return createUser;
    }
    /**
     * Sets the create user ID for an instance of
     * AssumedMatchSearchObject.
     * <p>
     * @param systemUser A string containing a user ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include

     */
    public void setCreateUser(String systemUser) {
        this.createUser = systemUser;
    }

    /**
     * Retrieves the value of the <b>startDate</b> field to be used
     * in an assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The starting date for the
     * assumed match search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateStartDate() {
        return createStartDate;
    }
    /**
     * Sets the starting date for an instance of
     * AssumedMatchSearchObject. Use this method with
     * <b>setCreateEndDate</b> to search for assumed matches
     * made between these two dates.
     * <p>
     * @param startDate The starting date for the assumed
     * match search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateStartDate(java.sql.Timestamp startDate) {
        this.createStartDate = startDate;
    }

    /**
     * Retrieves the value of the <b>endDate</b> field to be used
     * in an assumed match search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The end date for the
     * assumed match search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Sets the ending date for an instance of
     * AssumedMatchSearchObject. Use this method with
     * <b>setCreateStartDate</b> to search for assumed matches
     * made between these two dates.
     * <p>
     * @param endDate The end date for the assumed match
     * search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateEndDate(java.sql.Timestamp endDate) {
        this.createEndDate = endDate;
    }

    /**
     * Retrieves a string representation of the fields in the
     * AssumedMatchSearchObject object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The field values contained in the
     * AssumedMatchSearchObject object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("EUIDs: ");
        if (euids == null) {
            sb.append("null");
        } else {
            for (int i = 0; i < euids.length; i++) {
                sb.append(euids[i]).append(" | ");
            }
        }
        sb.append('\n');
        sb.append("SystemCode: ").append(systemCode).append('\n');
        sb.append("LID: ").append(lid).append('\n');
        sb.append("CreateUser: ").append(createUser).append('\n');
        sb.append("CreateStartDate: ").append(createStartDate).append('\n');
        sb.append("CreateEndDate: ").append(createEndDate).append('\n');
        sb.append("MaxElements: ").append(mMaxElements).append('\n');
        sb.append("PageSize: ").append(mPageSize);
        return sb.toString();
    }

}
