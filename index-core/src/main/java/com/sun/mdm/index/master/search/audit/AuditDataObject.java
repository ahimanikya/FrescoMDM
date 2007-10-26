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

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.ArrayList;



/**
 * The <b>AuditDataObject</b> class represents an object containing an audit log
 * summary returned by a call to <b>lookupAuditLog</b>. An audit log search
 * returns an iterator of AuditDataObject objects.
 */
public class AuditDataObject extends ObjectNode {
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("AUDITID");
        mFieldNames.add("PrimaryObjectType");
        mFieldNames.add("EUID1");
        mFieldNames.add("EUID2");
        mFieldNames.add("Function");
        mFieldNames.add("Detail");
        mFieldNames.add("CreateDate");
        mFieldNames.add("CreateUser");

        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));

        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_TIMESTAMP_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }
    /**
     * Creates a new instance of the AuditDataObject class.
     *<p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public AuditDataObject() {
    }

    /**
     * Creates a new instance of the AuditDataObject class.
     *<p>
     * @param id An audit log ID.
     * @param primaryObjectType The primary object type of the master
     * index application.
     * @param euid1 The primary EUID in an audit log entry.
     * @param euid2 The second EUID in an audit log
     * entry (for example, the unkept EUID from a merge transaction).
     * @param function The type of function that caused the audit log entry.
     * @param detail The detail information for the audit log entry.
     * @param createDate The date the audit log entry was created.
     * @param createUser The logon ID of the user who performed the
     * transaction that caused the audit log entry.
     * @exception ObjectException Thrown if an error occurs
     * while creating the AuditDataObject object.
     * @include
     */
    public AuditDataObject(String id, String primaryObjectType, String euid1, String euid2,
        String function, String detail, java.util.Date createDate,
        String createUser) throws ObjectException {
        super("Audit", mFieldNames, mFieldTypes);


        setValue("AUDITID", id);
        setValue("EUID1", euid1);
        setValue("EUID2", euid2);
        setValue("PrimaryObjectType", primaryObjectType);
        setValue("Function", function);
        setValue("Detail", detail);

        java.sql.Date date = null;

        if (createDate instanceof java.util.Date
                || createDate instanceof java.sql.Timestamp) {
            long time = ((java.util.Date) createDate).getTime();
            date = new java.sql.Date(time);
        } else if (date instanceof java.sql.Date) {
            date = (java.sql.Date) createDate;
        }

        setValue("CreateUser", createUser);
        setValue("CreateDate", date);
    }

    /**
     * Retrieves the date the audit log entry was created
     * from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Date</CODE> - The create date of the
     * audit log entry.
     * @exception ObjectException Thrown if the date could not be
     * retrieved.
     * @include
     */
    public java.sql.Date getCreateDate() throws ObjectException {
        return (java.sql.Date) getValue("CreateDate");
    }

    /**
     * Retrieves the create user ID from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * @exception ObjectException Thrown if the user ID could not be
     * retrieved.
     * @include
     */
    public String getCreateUser() throws ObjectException {
        return (String) getValue("CreateUser");
    }

    /**
     * Retrieves the detail information from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - Audit log entry detail information.
     * @exception ObjectException Thrown if the detail information could
     * not be retrieved.
     * @include
     */
    public String getDetail() throws ObjectException {
        return (String) getValue("Detail");
    }

    /**
     * Retrieves the value of the primary EUID from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if the EUID could not be
     * retrieved.
     * @include
     */
    public String getEUID1() throws ObjectException {
        return (String) getValue("EUID1");
    }

    /**
     * Retrieves the value of the second EUID from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if the EUID could not be
     * retrieved.
     * @include
     */
    public String getEUID2() throws ObjectException {
        return (String) getValue("EUID2");
    }

    /**
     * Retrieves the function type from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A function type.
     * @exception ObjectException Thrown if the function could not be
     * retrieved.
     * @include
     */
    public String getFunction() throws ObjectException {
        return (String) getValue("Function");
    }

    /**
     * Retrieves the value of the audit log ID from the audit data
     * object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the audit log ID.
     * @exception ObjectException Thrown if the ID could not be
     * retrieved.
     * @include
     */
    public String getId() throws ObjectException {
        return (String) getValue("AUDITID");
    }

    /**
     * Retrieves the primary object type from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The primary object type of the
     * master index application.
     * @exception ObjectException Thrown if the primary object type
     * could not be retrieved.
     * @include
     */
    public String getPrimaryObjectType() throws ObjectException {
        return (String) getValue("PrimaryObjectType");
    }

    /**
     * Retrieves a string representation of the audit log ID, EUID1,
     * EUID2, function, and detail fields from the audit data object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string containing field values
     * from the audit data object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append('[').append(getId()).append(',').append(getEUID1());
            sb.append(',').append(getEUID2()).append(',').append(getFunction());
            sb.append(',').append(getDetail()).append(']');
        } catch (Exception e) {
            sb.append("toString() error: " + e);
        }
        return sb.toString();
    }

}
