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
package com.sun.mdm.index.objects;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.DBAdapter;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

import java.util.ArrayList;
import java.util.Date;


/**
 * The <b>TransactionObject</b> class represents a transaction object
 * as stored in the master index database table sbyn_transaction.  This
 * database table contains three EUID fields and three local ID fields
 * (along with additional transaction information). Of these fields, only
 * the EUID, EUID2, LID, and LID2 fields are used. EUID1 and LID1 are not
 * currently used and are reserved for future functionality.
 * @author gzheng
 * @version
 */
public class TransactionObject extends ObjectNode {
    /**
     * status
     */

    public static final String STATUS_ACTIVE = "active";
    /**
     * merge to recover
     */
    public static final String RECOVER_MERGED = "Merged";

    /**
     * survivor to recover
     */
    public static final String RECOVER_SURVIVOR = "Survivor";

    private static String mOperationColumnName = null;  // Name of the operation column.
    // Logger instance.  This next line is hard-coded because it may be invoked from
    // within a static context, and there is no other way to obtain the name
    // of the object if it is not instantiated.
    private static final Logger mLogger = LogUtil.getLogger("TransactionObject"); 
    private static ArrayList mFieldNames;       // field names
    private static ArrayList mFieldTypes;       // field types
 
    static {
        mFieldNames = constructFieldNames();
        mFieldTypes = constructFieldTypes();
    }

    /**
     * Creates a new instance of the TransactionObject class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @exception ObjectException Thrown if an error occurs while creating
     * the transaction object.
     * @include
     */
    public TransactionObject() throws ObjectException {
        super("TransactionObject", mFieldNames, mFieldTypes);

        setNullable("TransactionNumber", true);
        setNullable("LID1", true);
        setNullable("LID2", true);
        setNullable("EUID1", true);
        setNullable("EUID2", true);
        mOperationColumnName = getOperationColumnName();
        setNullable(mOperationColumnName, true);
        setNullable("SystemUser", true);
        setNullable("TimeStamp", true);
        setNullable("SystemCode", true);
        setNullable("LID", true);
        setNullable("EUID", true);
        setNullable("Delta", true);
        setNullable("RecoverObject", true);

        setNull("TransactionNumber", true);
        setNull("LID1", true);
        setNull("LID2", true);
        setNull("EUID1", true);
        setNull("EUID2", true);
        mOperationColumnName = getOperationColumnName();
        setNull(mOperationColumnName, true);
        setNull("SystemUser", true);
        setNull("TimeStamp", true);
        setNull("SystemCode", true);
        setNull("LID", true);
        setNull("EUID", true);
        setNull("Delta", true);
        setNull("RecoverObject", true);
    }

    /**
     * Creates a new instance of the TransactionObject class.
     * <p>
     * @param transactionnumber The unique transaction number assigned
     * by the master index.
     * @param lid1 The first local ID for the transaction. This field is
     * not currently used and is reserved for future functionality (the
     * first local ID is populated into the LID field).
     * @param lid2 The second local ID for the transaction.
     * @param euid1 The first EUID for the transaction. This field is
     * not currently used and is reserved for future functionality (the
     * first EUID is populated into the EUID field).
     * @param euid2 The second EUID for the transaction.
     * @param function The type of transaction that created the
     * transaction record.
     * @param systemuser The login ID of the user who performed
     * the transaction.
     * @param timestamp The date and time the transaction occurred.
     * @param systemcode The processing code of the system from which
     * the transaction originated.
     * @param lid The primary local ID for the transaction.
     * @param euid The primary EUID for the transaction.
     * @param delta Information about what was changed or discarded during the
     * transaction.
     * @exception ObjectException Thrown if an error occurs while creating
     * the transaction object.
     * @include
     */
    public TransactionObject(String transactionnumber, String lid1,
        String lid2, String euid1, String euid2, String function,
        String systemuser, Date timestamp, String systemcode, String lid,
        String euid, Object delta) throws ObjectException {
        super("TransactionObject", mFieldNames, mFieldTypes);

        setValue("TransactionNumber", transactionnumber);
        setValue("LID1", lid1);
        setValue("LID2", lid2);
        setValue("EUID1", euid1);
        setValue("EUID2", euid2);
        mOperationColumnName = getOperationColumnName();
        setValue(mOperationColumnName, function);
        setValue("SystemUser", systemuser);
        setValue("TimeStamp", timestamp);
        setValue("SystemCode", systemcode);
        setValue("LID", lid);
        setValue("EUID", euid);
        setValue("Delta", delta);
    }

    /**
     * Retrieves the old information that was changed or discarded during a
     * transaction from an instance of TransactionObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - An object containing a record's
     * changed information.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the delta value.
     * @include
     */
    public Object getDelta() throws ObjectException {
        try {
            return (getValue("Delta"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the EUID of the record against which the transaction
     * was performed.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the EUID value.
     * @include
     */
    public String getEUID() throws ObjectException {
        try {
            return ((String) getValue("EUID"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves value of the <b>EUID1</b> field from an instance of
     * TransactionObject. Currently, this field is not used in the
     * master index.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the EUID value.
     * @include
     */
    public String getEUID1() throws ObjectException {
        try {
            return ((String) getValue("EUID1"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the EUID of the second record involved in the transaction
     * (this EUID is only populated for merge and unmerge transactions).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the EUID value.
     * @include
     */
    public String getEUID2() throws ObjectException {
        try {
            return ((String) getValue("EUID2"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the type of transaction performed from the instance of
     * TransactionObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A transaction type.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the transaction type.
     * @include
     */
    public String getFunction() throws ObjectException {
        return ((String) getValue(getOperationColumnName()));
    }
    
    /**
     * Retrieves the local ID of the system record against which the
     * transaction was performed (this is only populated for local ID
     * merges, unmerges, and transfers).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the local ID value.
     * @include
     */
    public String getLID() throws ObjectException {
        try {
            return ((String) getValue("LID"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the value of the <b>LID1</b> field from an instance of
     * TransactionObject. Currently, this field is not used in the
     * master index.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the local ID value.
     * @include
     */
    public String getLID1() throws ObjectException {
        try {
            return ((String) getValue("LID1"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the local ID of the second system record involved in the
     * transaction (this local ID is only populated for local ID merge,
     * unmerge, and transfer transactions).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the local ID value.
     * @include
     */
    public String getLID2() throws ObjectException {
        try {
            return ((String) getValue("LID2"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the processing code of the system from which the
     * transaction originated.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A system code.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the system code.
     * @include
     */
    public String getSystemCode() throws ObjectException {
        try {
            return ((String) getValue("SystemCode"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the logon ID of the user who performed the
     * transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the logon ID value.
     * @include
     */
    public String getSystemUser() throws ObjectException {
        try {
            return ((String) getValue("SystemUser"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the date and time the transaction occurred.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Date</CODE> - A date and time.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the date and time.
     * @include
     */
    public Date getTimeStamp() throws ObjectException {
        try {
            return ((Date) getValue("TimeStamp"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the unique identification number for the transaction
     * (this number is assigned by the master index).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A local ID.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the transaction ID.
     * @include
     */
    public String getTransactionNumber() throws ObjectException {
        try {
            return ((String) getValue("TransactionNumber"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves before and after images for a transaction history to
     * display in side-by-side comparisons.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The before or after image of a
     * specific transaction.
     * @exception ObjectException Thrown if an error occurs while
     * retrieving the image.
     * @include
     */
    public String getRecoverObject() throws ObjectException {
        try {
            return ((String) getValue("RecoverObject"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the delta value in an instance of the TransactionObject class.
     * This is the old information that was changed or discarded during a
     * transaction.
     * <p>
     * @param delta An object containing the delta information.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the delta value.
     * @include
     */
    public void setDelta(Object delta) throws ObjectException {
        try {
            setValue("Delta", delta);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>EUID</b> field in an instance of the
     * TransactionObject class. This is the EUID of the primary record
     * involved in the transaction.
     * <p>
     * @param euid An object containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the EUID value.
     * @include
     */
    public void setEUID(Object euid) throws ObjectException {
        try {
            setValue("EUID", euid);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>EUID1</b> field in an instance of the
     * TransactionObject class. This field is not currently used.
     * <p>
     * @param euid1 An object containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the EUID value.
     * @include
     */
    public void setEUID1(Object euid1) throws ObjectException {
        try {
            setValue("EUID1", euid1);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>EUID2</b> field in an instance of the
     * TransactionObject class. This is the EUID of the secondary record
     * involved in a transaction, such as the non-surviving record in a merge.
     * <p>
     * @param euid2 An object containing an EUID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the EUID value.
     * @include
     */
    public void setEUID2(Object euid2) throws ObjectException {
        try {
            setValue("EUID2", euid2);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>Function</b> field in an instance of the
     * TransactionObject class. This is the type of transaction performed.
     * <p>
     * @param function An object containing a transaction type.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the function value.
     * @include
     */
    public void setFunction(Object function) throws ObjectException {
        try {
            mOperationColumnName = getOperationColumnName();
            setValue(mOperationColumnName, function);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>LID</b> field in an instance of the
     * TransactionObject class. This is the local ID of the primary system
     * record involved in a transaction.
     * <p>
     * @param lid An object containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the local ID value.
     * @include
     */
    public void setLID(Object lid) throws ObjectException {
        try {
            setValue("LID", lid);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>LID1</b> field in an instance of the
     * TransactionObject class. This field is not currently used.
     * <p>
     * @param lid1 An object containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the local ID value.
     * @include
     */
    public void setLID1(Object lid1) throws ObjectException {
        try {
            setValue("LID1", lid1);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>LID2</b> field in an instance of the
     * TransactionObject class. This is the local ID of the secondary
     * system record involved in the transaction, such as the non-surviving
     * system record in a local ID merge.
     * <p>
     * @param lid2 An object containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the local ID value.
     * @include
     */
    public void setLID2(Object lid2) throws ObjectException {
        try {
            setValue("LID2", lid2);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>SystemCode</b> field in an instance of the
     * TransactionObject class. This is the processing code of the system from
     * which the transaction originated.
     * <p>
     * @param systemcode An object containing a system code.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the system code value.
     * @include
     */
    public void setSystemCode(Object systemcode) throws ObjectException {
        try {
            setValue("SystemCode", systemcode);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>SystemUser</b> field in an instance of the
     * TransactionObject class. This is the logon ID of the user who performed
     * the transaction.
     * <p>
     * @param systemuser An object containing a user logon ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the system user value.
     * @include
     */
    public void setSystemUser(Object systemuser) throws ObjectException {
        try {
            setValue("SystemUser", systemuser);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>TimeStamp</b> field in an instance of the
     * TransactionObject class. This is the date and time the transaction
     * was performed.
     * <p>
     * @param timestamp An object containing a date and time.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the timestamp value.
     * @include
     */
    public void setTimeStamp(Object timestamp) throws ObjectException {
        try {
            setValue("TimeStamp", timestamp);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the value of the <b>TransactionNumber</b> field in an instance of the
     * TransactionObject class. This is the unique transaction ID assigned by the
     * master index. These numbers are sequential and are controlled by the
     * sbyn_seq_table database table.
     * <p>
     * @param transactionnumber An object containing the transaction ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the transaction ID.
     * @include
     */
    public void setTransactionNumber(Object transactionnumber)
        throws ObjectException {
        try {
            setValue("TransactionNumber", transactionnumber);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Sets the before or after image in an instance of the
     * TransactionObject class. This image is reconstructed from the information
     * contained in the delta column of the sbyn_transaction table.
     * <p>
     * @param recoverobject An object containing an image of an enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the object.
     * @include
     */
    public void setRecoverObject(Object recoverobject)
        throws ObjectException {
        try {
            setValue("RecoverObject", recoverobject);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * @exception ObjectException object exception
     * @return ObjectNode copy
     */
    public ObjectNode copy() throws ObjectException {
        TransactionObject ret = null;

        try {
            ret = new TransactionObject(getTransactionNumber(), getLID1(),
                    getLID2(), getEUID1(), getEUID2(), getSystemUser(),
                    getFunction(), getTimeStamp(), getLID(), getSystemCode(),
                    getEUID(), getDelta());
            ret.setRecoverObject(getRecoverObject());

            ArrayList names = pGetFieldNames();

            for (int i = 0; i < names.size(); i++) {
                String name = (String) names.get(i);
                ret.setVisible(name, isVisible(name));
                ret.setSearched(name, isSearched(name));
                ret.setChanged(name, isChanged(name));
                ret.setKeyType(name, isKeyType(name));
            }

            ret.setUpdateFlag(isUpdated());
            ret.setRemoveFlag(isRemoved());
            ret.setAddFlag(isAdded());

            ArrayList fieldUpdateLogs = null;
            if ( pGetFieldUpdateLogs()!= null ){
                fieldUpdateLogs = (ArrayList)pGetFieldUpdateLogs().clone();
            }
            ret.setFieldUpdateLogs(fieldUpdateLogs);
        } catch (ObjectException e) {
            throw e;
        }

        return (ObjectNode) ret;
    }

    /** Retrieves the name of the operation column
     * 
     * @throws  ObjectException if any errors are encountered.
     * @return  ArrayList object containing the field names.
     */
    private static String getOperationColumnName() throws ObjectException {
        try {
            if (mOperationColumnName == null) {
                mOperationColumnName = 
                    DBAdapter.getDBAdapterInstance().getOperationColumnName();
            }
        } catch (OPSException e) {
            throw new ObjectException(e.getMessage());
        }
        return mOperationColumnName;
    }

    /**
     * Constructs the field names.  
     * 
     * @return  ArrayList object containing the field names.
     */
    private static ArrayList constructFieldNames() {
        
        mFieldNames = new ArrayList();
        mFieldNames.add("TransactionNumber");
        mFieldNames.add("LID1");
        mFieldNames.add("LID2");
        mFieldNames.add("EUID1");
        mFieldNames.add("EUID2");
        try {
            mOperationColumnName = getOperationColumnName();
        } catch (ObjectException e) {
            mLogger.error("Unable to retrieve operation column name");
            return null;
        }
        mFieldNames.add(mOperationColumnName);
        mFieldNames.add("SystemUser");
        mFieldNames.add("TimeStamp");
        mFieldNames.add("SystemCode");
        mFieldNames.add("LID");
        mFieldNames.add("EUID");
        mFieldNames.add("Delta");
        mFieldNames.add("RecoverObject");
        
        return mFieldNames;
    }    
    
    /**
     * Constructs the field types.
     * 
     * @return  ArrayList object containing the field types.
     */
    private static ArrayList constructFieldTypes() {
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_TIMESTAMP_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_BLOB_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        
        return mFieldTypes;
    }    
    
}
