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
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.ArrayList;
import java.util.Date;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

/**
 * The <b>AssumedMatchSummary</b> class represents an object containing transaction
 * information about assumed match transactions that were returned by a call to
 * <b>lookupAssumedMatches</b>. An assumed match search returns an iterator of
 * AssumedMatchSummary objects.
 */
public class AssumedMatchSummary extends ObjectNode {
    static final long serialVersionUID = -7748836226130658344L;
    public static final int version = 1;
    /** Field names
     */
    private static ArrayList mFieldNames;
    /** Field types
     */
    private static ArrayList mFieldTypes;
    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("ID");
        mFieldNames.add("EUID");
        mFieldNames.add("Weight");
        mFieldNames.add("CreateDate");
        mFieldNames.add("CreateUser");
        mFieldNames.add("SystemCode");
        mFieldNames.add("LID");
        mFieldNames.add("TransactionNumber");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_FLOAT_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }

    /** MasterController handle
     */
    private MasterController mc;
    /** Search object
     */
    private AssumedMatchSearchObject searchObj;
    /** Before image of EO
     */
    private EnterpriseObject mBeforeEO;
    /** After image of EO
     */
    private EnterpriseObject mAfterEO;
    /** New system object as result of assumed match
     */
    private SystemObject mNewSO;


    /**
     * Creates a new instance of the AssumedMatchSummary class.
     *<p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public AssumedMatchSummary() {
    }


    /**
     * Creates a new instance of the AssumedMatchSummary class.
     *<p>
     * @param id A string containing an assumed match ID.
     * @param euid The EUID of the record that was updated as a result
     * of the assumed match.
     * @param systemCode A string containing the processing code of the
     * system that caused the assumed match.
     * @param lid The local ID of the record that cause the assumed match.
     * @param createUser A string containing the logon ID of the user
     * who performed the transaction causing the assumed match.
     * @param createDate The date the assumed match was created.
     * @param transactionNumber The transaction number of the transaction
     * that caused the assumed match.
     * @exception ObjectException Thrown if an error occurs
     * while creating the AssumedMatchSummary object.
     * @include
     */
    public AssumedMatchSummary(String id, String euid, String systemCode,
    String lid, String createUser,
    Date createDate, String transactionNumber) throws ObjectException {
        super("AssumedMatch", mFieldNames, mFieldTypes);
        setValue("ID", id);
        setValue("EUID", euid);
        setValue("SystemCode", systemCode);
        setValue("LID", lid);
        setValue("CreateUser", createUser);
        setValue("CreateDate", createDate);
        setValue("TransactionNumber", transactionNumber);
    }

    /**
     * Retrieves the value of the assumed match ID from the
     * AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the assumed match ID.
     * @exception ObjectException Thrown if the ID could not be
     * retrieved.
     * @include
     */
    public String getId() throws ObjectException {
        return (String) getValue("ID");
    }
    /**
     * Retrieves the value of the EUID from the AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if the EUID could not be
     * retrieved.
     * @include
     */
    public String getEUID() throws ObjectException {
        return (String) getValue("EUID");
    }
    /**
     * Retrieves the value of the local ID from the AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ObjectException Thrown if the local ID could not be
     * retrieved.
     * @include
     */
    public String getLID() throws ObjectException {
        return (String) getValue("LID");
    }
    /**
     * Retrieves the value of the transaction number from the
     * AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The transaction number for the assumed
     * match.
     * @exception ObjectException Thrown if the transaction number could not be
     * retrieved.
     * @include
     */
    public String getTransactionNumber() throws ObjectException {
        return (String) getValue("TransactionNumber");
    }
    /**
     * Retrieves the matching probability weight of the records represented by
     * the AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Float</CODE> - The matching probability weight
     * between two records.
     * @exception ObjectException Thrown if the weight could not be
     * retrieved.
     * @include
     */
    public float getWeight() throws ObjectException {
        return ((Float) getValue("Weight")).floatValue();
    }
    /**
     * Retrieves the system code from the AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The processing code of the system that
     * caused the assumed match transaction.
     * @exception ObjectException Thrown if the system code could
     * not be retrieved.
     * @include
     */
    public String getSystemCode() throws ObjectException {
        return (String) getValue("SystemCode");
    }
    /**
     * Retrieves the create user ID from the AssumedMatchSummary
     * object.
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
     * Retrieves the date that the assumed match occurred from the
     * AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The create date of the
     * assumed match.
     * @exception ObjectException Thrown if the date could not be
     * retrieved.
     * @include
     */
    public java.util.Date getCreateDate() throws ObjectException {
        return (java.util.Date) getValue("CreateDate");
    }
    /**
     * Retrieves the system object that was added to the existing record
     * in the assumed match transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>SystemObject</CODE> - A system object representing
     * the system record that updated the existing record.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SystemObject getNewSO() {
        return mNewSO;
    }
    /**
     * Retrieves the SBR object in the existing record as it appeared
     * prior to the assumed match update.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>SBR</CODE> - The SBR object of the existing record that was
     * updated in the assumed match transaction.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SBR getBeforeSBR() {
        if (mBeforeEO == null) {
            return null;
        } else {
            return mBeforeEO.getSBR();
        }
    }
    /**
     * Retrieves the enterprise object that was updated in the assumed
     * match transaction. This method retrieves the enterprise
     * object as it appeared prior to the assumed match transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object before
     * it was updated by the assumed match.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getBeforeEO() {
        return mBeforeEO;
    }
    /**
     * Retrieves the enterprise object that was updated in the assumed
     * match transaction. This method retrieves the enterprise
     * object as it appears after the assumed match transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The enterprise object after
     * it was updated by the assumed match.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getAfterEO() {
        return mAfterEO;
    }
    /**
     * Sets the "after" enterprise object in the AssumedMatchSummary object.
     * This represents the enterprise object as it appears after the
     * assumed match updates.
     * <p>
     * @param afterEO The enterprise object to set.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * include
     */
    public void setAfterEO(EnterpriseObject afterEO) {
        mAfterEO = afterEO;
    }
    /**
     * Sets the new system object in the AssumedMatchSummary object.
     * <p>
     * @param newSO The system object to set.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * include
     */
    public void setNewSO(SystemObject newSO) {
        mNewSO = newSO;
    }
    /**
     * Sets the "before" enterprise object in the AssumedMatchSummary object.
     * This represents the enterprise object as it appeared before the
     * assumed match updates.
     * <p>
     * @param afterEO The enterprise object to set.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * include
     */
    public void setBeforeEO(EnterpriseObject beforeEO) {
        mBeforeEO = beforeEO;
    }
    /**
     * Sets the matching probability weight in an AssumedMatchSummary
     * object.
     * <p>
     * @param weight The matching probability weight between two records in
     * an AssumedMatchSummary object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the weight could not be set.
     * include
     */
    public void setWeight(float weight) throws ObjectException {
        setValue("Weight", new Float(weight));
    }
    /**
     * Retrieves a string representation of the assumed match ID, EUID,
     * and weight fields from the AssumedMatchSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The assumed match ID, EUID1, and weight
     * field values.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        String retString = null;
        try {
            retString = "[" + getId() + ":" + getEUID() + ","
            + getWeight() + "]";
        } catch (Exception e) {
            return "Error converting to string: " + e.getMessage();
        }
        return retString;
    }


    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};

        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeObject(mc);
            out.writeObject(searchObj);
            out.writeObject(mBeforeEO);
            out.writeObject(mAfterEO);
            out.writeObject(mNewSO);
        }

        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mc = (MasterController) in.readObject();
            searchObj = (AssumedMatchSearchObject) in.readObject();
            mBeforeEO = (EnterpriseObject) in.readObject();
            mAfterEO = (EnterpriseObject) in.readObject();
            mNewSO = (SystemObject) in.readObject();
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
        super.writeExternal(out);
        ev.writeExternal(out);
    }

    public void readExternal(ObjectInput in)
	throws IOException, java.lang.ClassNotFoundException
    {
        int version = in.readInt();
        super.readExternal(in);
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        }
    }
}
