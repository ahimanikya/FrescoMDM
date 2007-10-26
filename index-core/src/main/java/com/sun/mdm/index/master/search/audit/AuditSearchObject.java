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
package com.sun.mdm.index.master.search.audit;

import com.sun.mdm.index.querybuilder.SearchOptions;


/**
 * The <b>AuditSearchObject</b> class is an object representing the
 * search criteria passed to <b>lookupAuditLog</b> when performing
 * a search for audit log entries. Use the methods in this class to
 * populate or retrieve the fields in the search object, and to define
 * the fields in the audit log search results list.
 */
public class AuditSearchObject extends SearchOptions
    implements java.io.Serializable {
     /** Create end date
     */
    private java.sql.Timestamp createEndDate;
     /** Create start date
     */
    private java.sql.Timestamp createStartDate;
     /** Create user
     */
    private String createUser;
    /** Audit log detail
     */
    private String detail;
    /** EUID
     */
    private String euid;
    /** Audit log function
     */
    private String function;
    /** Type
     */
    private String type;


    /**
     * Creates a new instance of the AuditSearchObject class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public AuditSearchObject() {
    }

    /**
     * Retrieves the value of the <b>endDate</b> field to be used
     * in an audit log search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The end date for the
     * audit log search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Retrieves the value of the <b>startDate</b> field to be used
     * in an audit log search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The start date for the
     * audit log search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateStartDate() {
        return createStartDate;
    }

    /**
     * Retrieves the value of the <b>createUser</b> field to be used
     * in an audit log search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The user logon ID to use
     * for the audit log search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * Retrieves the value of the <b>detail</b> field to be used
     * in an audit log search. Note that this field is not commonly
     * used in audit log searches, since each character in the search
     * field must match each character in the detail field of any
     * resulting records.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The detail information to use for
     * the audit log search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Retrieves the value of the <b>EUID</b> field to be used
     * in an audit log search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The EUID to use for the audit
     * log search.
     * <DT><B>Throws:</B><DD>None.
     */
    public String getEUID() {
        return euid;
    }

    /**
     * Retrieves the value of the <b>function</b> field to be used
     * in an audit log search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The type of function to use
     * for the audit log search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getFunction() {
        return function;
    }

    /**
     * Retrieves the primary object type of the master index application.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The primary object type.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getPrimaryObjectType() {
        return type;
    }

    /**
     * Sets the ending date for an instance of AuditSearchObject.
     * Use this method with <b>setCreateStartDate</b> to search for
     * audit log entries created between these two dates.
     * <p>
     * @param endDate The end date for the audit log search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateEndDate(java.sql.Timestamp endDate) {
        this.createEndDate = endDate;
    }

    /**
     * Sets the starting date for an instance of AuditSearchObject.
     * Use this method with <b>setCreateEndDate</b> to search for
     * audit log entries created between these two dates.
     * <p>
     * @param startDate The start date for the audit log search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateStartDate(java.sql.Timestamp startDate) {
        this.createStartDate = startDate;
    }

    /**
     * Sets the create user ID for an instance of AuditSearchObject.
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
     * Sets the detail field for an instance of AuditSearchObject.
     * Note that this field is not commonly used in audit log searches,
     * since each character in the search field must match each character
     * in the detail field of any resulting records.
     * <p>
     * @param detail A string containing the detail information.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Sets the EUID for an instance of AuditSearchObject.
     * <p>
     * @param euid A string containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEUID(String euid) {
        this.euid = euid;
    }

    /**
     * Sets the function (type of transaction) for an instance
     * of AuditSearchObject.
     * <p>
     * @param function A string containing a type of function.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * Sets the primary object type for an instance of AuditSearchObject.
     * <p>
     * @param type The primary object type of the master index
     * application.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setPrimaryObjectType(String type) {
        this.type = type;
    }

    /**
     * Retrieves a string representation of the fields in the
     * AuditSearchObject object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The field values contained in the
     * AuditSearchObject object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append('\n');
        sb.append("EUID: ").append(euid).append('\n');
        sb.append("type: ").append(type).append('\n');
        sb.append("CreateUser: ").append(createUser).append('\n');
        sb.append("CreateStartDate: ").append(createStartDate).append('\n');
        sb.append("CreateEndDate: ").append(createEndDate).append('\n');

        sb.append("MaxElements: ").append(mMaxElements).append('\n');
        sb.append("PageSize: ").append(mPageSize);

        return sb.toString();
    }
}
