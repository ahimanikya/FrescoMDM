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
package com.sun.mdm.index.master.search.transaction;

import java.util.Date;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.util.Localizer;


/**
 * The <b>TransactionSearchObject</b> class is an object representing the
 * search criteria passed to <b>lookupTransaction</b> when performing
 * a search for transaction histories. Use the methods in this class to
 * populate or retrieve the fields in the search object, and to define the fields
 * in the transaction search results list.
 */
public class TransactionSearchObject implements java.io.Serializable {
    private Date startDate = null;
    private Date endDate = null;
    private int maxElements = 0;
    private int pageSize = 0;
    private TransactionObject transObj = null;
    private transient final Localizer mLocalizer = Localizer.get();


    /**
     * Creates a new instance of the TransactionSearchObject class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @exception ProcessingException An error occurred while creating
     * the search object.
     * @include
     */
    public TransactionSearchObject()
        throws ProcessingException {
        try {
            transObj = new TransactionObject();
            setDefaults();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS511: Could not instantiate TransactionSearchObject: {0}", t));
        }
    }


    /**
     * Creates a new instance of the TransactionSearchObject class with
     * a starting date for the search.
     * <p>
     * @param sDate A starting date for the transaction history search.
     * @exception ProcessingException An error occurred while creating
     * the search object.
     * @include
     */
    public TransactionSearchObject(Date sDate)
        throws ProcessingException {
        try {
            transObj = new TransactionObject();
            setStartDate(sDate);
            setDefaults();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS512: Could not instantiate TransactionSearchObject: {0}", t));
        }
    }


    /**
     * Creates a new instance of the TransactionSearchObject class with
     * starting and ending dates for the search.
     * <p>
     * @param sDate A starting date for the transaction history search.
     * @param eDate An ending date for the transaction history search.
     * @exception ProcessingException An error occurred while creating
     * the search object.
     * @include
     */
    public TransactionSearchObject(Date sDate, Date eDate)
        throws ProcessingException {
        try {
            transObj = new TransactionObject();
            setStartDate(sDate);
            setEndDate(eDate);
            setDefaults();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS513: Could not instantiate TransactionSearchObject: {0}", t));
        }
    }


    /**
     * Creates a new instance of the TransactionSearchObject class with
     * search criteria.
     * <p>
     * @param euid An EUID for the transaction history search.
     * @param sysCode A system code for the transaction history search.
     * @param lid A local ID for the transaction history search.
     * @param sDate A starting date for the transaction history search.
     * @param eDate An ending date for the transaction history search.
     * @param function A transaction type for the transaction history search.
     * @param user A user login ID for the transaction history search.
     * @exception ProcessingException An error occurred while creating
     * the search object.
     * @include
     */
    public TransactionSearchObject(String euid, String sysCode, String lid,
            Date sDate, Date eDate, String function, String user)
        throws ProcessingException {
        try {
            transObj = new TransactionObject(null, null, null, null, null, function,
            user, new Date(), sysCode, lid, euid, null);
            setStartDate(sDate);
            setEndDate(eDate);
            setDefaults();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS514: Could not instantiate TransactionSearchObject: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>EUID</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string containing an EUID.
     * @exception ProcessingException An error occurred while retrieving
     * the EUID.
     * @include
     */
    public String getEUID()
        throws ProcessingException {
        try {
            return transObj.getEUID();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS515: Could not retrieve EUID: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>EUID1</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string containing an EUID.
     * @exception ProcessingException An error occurred while retrieving
     * the EUID.
     * @include
     */
    public String getEUID1()
        throws ProcessingException {
        try {
            return transObj.getEUID1();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS516: Could not retrieve EUID1: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>EUID2</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string containing an EUID.
     * @exception ProcessingException An error occurred while retrieving
     * the EUID.
     * @include
     */
    public String getEUID2()
        throws ProcessingException {
        try {
            return transObj.getEUID2();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS517: Could not retrieve EUID2: {0}", t));
        }
    }


    /** Get EUID type
     * @return EUID type
     * @exclude
     */
    public String getEUIDType() {
        return null;
    }


    /** Get EUID1 Type
     * @return EUID1 type
     * @exclude
     */
    public String getEUIDType1() {
        return null;
    }


    /** Get EUID2 type
     * @todo Document: Getter for EUIDType2 attribute of the
     *      TransactionSearchObject object
     * @return EUID2 type
     * @exclude
     */
    public String getEUIDType2() {
        return null;
    }


    /**
     * Retrieves the value of the <b>endDate</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The ending date for a transaction
     * history search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getEndDate() {
        return endDate;
    }


    /**
     * Retrieves the value of the <b>function</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The function field in a transaction
     * history search.
     * @exception ProcessingException An error occurred while retrieving
     * the function.
     * @include
     */
    public String getFunction()
        throws ProcessingException {
        try {
            return transObj.getFunction();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS518: Could not retrieve function: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>lid</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ProcessingException An error occurred while retrieving
     * the local ID.
     * @include
     */
    public String getLID()
        throws ProcessingException {
        try {
            return transObj.getLID();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS519: Could not retrieve LID: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>lid1</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ProcessingException An error occurred while retrieving
     * the local ID.
     * @include
     */
    public String getLID1()
        throws ProcessingException {
        try {
            return transObj.getLID1();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS520: Could not retrieve LID1: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>lid2</b> field in an instance of
     * TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ProcessingException An error occurred while retrieving
     * the local ID.
     * @include
     */
    public String getLID2()
        throws ProcessingException {
        try {
            return transObj.getLID2();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS521: Could not retrieve LID2: {0}", t));
        }
    }


    /** Get local id type
     * @return local id type
     * @exclude
     */
    public String getLIDType() {
        return null;
    }


    /** Get local id type 1
     * @return local id type 1
     * @exclude
     */
    public String getLIDType1() {
        return null;
    }


    /** Get local id type 2
     * @return local id type 2
     * @exclude
     */
    public String getLIDType2() {
        return null;
    }


    /**
     * Retrieves an integer indicating the maximum number of results that
     * can be returned in a transaction history search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The maximum number of records to return.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int getMaxElements() {
        return maxElements;
    }


    /**
     * Retrieves an integer indicating the maximum number of results that
     * can be transferred to the caller at one time.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The maximum number of records to transfer.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int getPageSize() {
        return pageSize;
    }


    /**
     * Retrieves the value of the <b>startDate</b> field in an instance
     * of TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The starting date for a
     * transaction history search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getStartDate() {
        return startDate;
    }


    /**
     * Retrieves the value of the <b>systemCode</b> field in an instance
     * of TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The system code for a transaction
     * history search.
     * @exception ProcessingException An error occurred while retrieving
     * the system code.
     * @include
     */
    public String getSystemCode()
        throws ProcessingException {
        try {
            return transObj.getSystemCode();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS522: Could not retrieve SystemCode: {0}", t));
        }
    }


    /**
     * Retrieves the value of the <b>systemUser</b> field in an instance
     * of TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The user login ID for a transaction
     * history search.
     * @exception ProcessingException An error occurred while retrieving
     * the user login ID.
     * @include
     */
    public String getSystemUser()
        throws ProcessingException {
        try {
            return transObj.getSystemUser();
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS523: Could not retrieve SystemUser: {0}", t));
        }
    }


    /**
     * Retrieves the transaction object (class TransactionObject) in an instance
     * of TransactionSearchObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>TransactionObject</CODE> - A transaction object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TransactionObject getTransactionObject() {
        return transObj;
    }


    /**
     * Sets the EUID for an instance of TransactionSearchObject.
     * <p>
     * @param id A string containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setEUID(String id)
        throws ProcessingException {
        try {
            transObj.setEUID(id);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS524: Could not set EUID: {0}", t));
        }
    }


    /**
     * Sets the primary EUID (the EUID1 field) for an instance of
     * TransactionSearchObject.
     * <p>
     * @param id A string containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setEUID1(String id)
        throws ProcessingException {
        try {
            transObj.setEUID1(id);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS525: Could not set EUID1: {0}", t));
        }
    }


    /**
     * Sets the secondary EUID (the EUID2 field) for an instance of
     * TransactionSearchObject.
     * <p>
     * @param id A string containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setEUID2(String id)
        throws ProcessingException {
        try {
            transObj.setEUID2(id);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS526: Could not set EUID2: {0}", t));
        }
    }


    /** Set EUID type
     * @param type EUID type
     * @exclude
     */
    public void setEUIDType(String type) {
    }


    /** Set EUID type 1
     * @param type EUID type1
     * @exclude
     */
    public void setEUIDType1(String type) {
    }


    /** Set EUID type 2
     * @param type EUID type2
     * @exclude
     */
    public void setEUIDType2(String type) {
    }


    /**
     * Sets the ending date for an instance of TransactionSearchObject.
     * <p>
     * @param eDate The ending date for the transaction history search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEndDate(java.util.Date eDate) {
        endDate = eDate;
    }


    /**
     * Sets the function for an instance of TransactionSearchObject.
     * <p>
     * @param function A function type.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setFunction(String function)
        throws ProcessingException {
        try {
            transObj.setFunction(function);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS527: Could not set Function: {0}", t));
        }
    }


    /**
     * Sets the local ID for an instance of TransactionSearchObject.
     * <p>
     * @param lid A string containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setLID(String lid)
        throws ProcessingException {
        try {
            transObj.setLID(lid);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS528: Could not set LID: {0}", t));
        }
    }


    /**
     * Sets the primary local ID (the LID1 field) for an instance of
     * TransactionSearchObject.
     * <p>
     * @param lid A string containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setLID1(String lid)
        throws ProcessingException {
        try {
            transObj.setLID1(lid);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS529: Could not set LID1: {0}", t));
        }
    }


    /**
     * Sets the secondary local ID (the LID2 field) for an instance of
     * TransactionSearchObject.
     * <p>
     * @param lid A string containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setLID2(String lid)
        throws ProcessingException {
        try {
            transObj.setLID2(lid);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS530: Could not set LID2: {0}", t));
        }
    }


    /** Set local id type
     * @param type lid type
     * @exclude
     */
    public void setLIDType(String type) {
    }


    /** Set Local type 1
     * @exclude
     * @param type lid type1
     */
    public void setLIDType1(String type) {
    }


    /** Set local id type 2
     * @exclude
     * @param type lid type2
     */
    public void setLIDType2(String type) {
    }


    /**
     * Sets the maximum number of results that can be returned from
     * a transaction history search.
     * <p>
     * @param value The maximum number of records to return.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setMaxElements(int value) {
        maxElements = value;
    }


    /**
     * Sets the maximum number of results that can be transferred to
     * the caller at a given time.
     * <p>
     * @param value The maximum number of records to transfer.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setPageSize(int value) {
        pageSize = value;
    }


    /**
     * Sets the starting date for an instance of TransactionSearchObject.
     * <p>
     * @param sDate The starting date for the transaction history search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setStartDate(java.util.Date sDate) {
        startDate = sDate;
    }


    /**
     * Sets the system code for an instance of TransactionSearchObject.
     * <p>
     * @param code The system code for the transaction history search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setSystemCode(String code)
        throws ProcessingException {
        try {
            transObj.setSystemCode(code);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS531: Could not set SystemCode: {0}", t));
        }
    }


    /**
     * Sets the user login ID for an instance of TransactionSearchObject.
     * <p>
     * @param user The user login ID for the transaction history search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ProcessingException An error occurred while setting
     * the field value.
     * @include
     */
    public void setSystemUser(String user)
        throws ProcessingException {
        try {
            transObj.setSystemUser(user);
        } catch (Throwable t) {
            throw new ProcessingException(mLocalizer.t("MAS532: Could not set SystemUser: {0}", t));
        }
    }


    /**
     * Retrieves a string representation of the fields in the
     * TransactionSearchObject object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The field values contained in the
     * TransactionSearchObject object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public String toString() {
        String str = "";

        str += "Object: TransactionSearchObject\n";
        str += "startDate: " + startDate + "\n";
        str += "endDate: " + endDate + "\n";
        str += "maxElements: " + maxElements + "\n";
        str += "pageSize: " + pageSize + "\n";
        str += transObj.toString();

        return str;
    }


    void setDefaults() {
        //Set defaults
        maxElements = 200;
        pageSize = 20;
    }
}
