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
package com.sun.mdm.index.master.search.potdup;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.util.Localizer;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.ArrayList;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

/**
 * The <b>PotentialDuplicateSummary</b> class represents an object containing a potential
 * duplicate pair returned by a call to <b>lookupPotentialDuplicates</b>. A
 * potential duplicate search returns an iterator of PotentialDuplicateSummary
 * objects.
 */
public class PotentialDuplicateSummary extends ObjectNode {
    static final long serialVersionUID = -7397109923175099807L;
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
        mFieldNames.add("EUID1");
        mFieldNames.add("EUID2");
        mFieldNames.add("Weight");
        mFieldNames.add("CreateDate");
        mFieldNames.add("CreateUser");
        mFieldNames.add("Reason");
        mFieldNames.add("Status");
        mFieldNames.add("SystemCode");
        mFieldNames.add("ResolvedDate");
        mFieldNames.add("ResolvedUser");
        mFieldNames.add("ResolvedComment");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_FLOAT_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }

    /** MasterController handle
     */
    private MasterController mc;
    /** Originating search object
     */
    private PotentialDuplicateSearchObject searchObj;
    /** Object 1
     */
    private ObjectNode mObject1;
    /** Object 2
     */
    private ObjectNode mObject2;

    private transient final Localizer mLocalizer = Localizer.get();

    public PotentialDuplicateSummary() {
    }
    

    /** Creates a new instance of the PotentialDuplicateSummary
     * class.
     *<p>
     * @param mc A handle to the MasterController instance.
     * @param searchObj A PotentialDuplicateSearchObject search object.
     * @param id A string containing a potential duplicate ID.
     * @param euid1 A string containing the first EUID in a potential
     * duplicate pair.
     * @param euid2 A string containing the second EUID in a potential
     * duplicate pair.
     * @param status A string containing the status of the potential
     * duplicate pair.
     * @param reason A string containing the reason the records were
     * listed as potential duplicates.
     * @param weight A floating integer indicating the matching
     * probability between the two records.
     * @param systemCode A string containing the processing code of the
     * system that caused the potential duplication.
     * @param createUser A string containing the logon ID of the user
     * who performed the transaction causing the potential duplication.
     * @param createDate The date the potential duplicate listing was
     * created.
     * @param resolvedUser A string containing the logon ID of the user
     * who resolved the potential duplicate records.
     * @param resolvedDate The date the potential duplicate listing was
     * resolved.
     * @param comment A string containing a comment about the potential
     * duplicate records.
     * @exception ObjectException Thrown if an error occurs
     * while creating the PotentialDuplicateSummary object.
     * @include
     */
    public PotentialDuplicateSummary(MasterController mc,
    PotentialDuplicateSearchObject searchObj, String id,
    String euid1, String euid2, String status, String reason, float weight,
    String systemCode, String createUser, Date createDate, String resolvedUser,
    Date resolvedDate, String comment) throws ObjectException {
        super("PotentialDuplicate", mFieldNames, mFieldTypes);
        this.searchObj = searchObj;
        this.mc = mc;
        setValue("ID", id);
        setValue("EUID1", euid1);
        setValue("EUID2", euid2);
        setValue("Status", status);
        setValue("Reason", reason);
        setValue("Weight", new Float(weight));
        setValue("SystemCode", systemCode);
        setValue("CreateUser", createUser);
        setValue("CreateDate", createDate);
        setValue("ResolvedUser", resolvedUser);
        setValue("ResolvedDate", resolvedDate);
        setValue("ResolvedComment", comment);
    }

    /** Creates a new instance of the PotentialDuplicateSummary
     * class.
     *<p>
     * @param searchObj A PotentialDuplicateSearchObject search object.
     * @param id A string containing a potential duplicate ID.
     * @param euid1 A string containing the first EUID in a potential
     * duplicate pair.
     * @param euid2 A string containing the second EUID in a potential
     * duplicate pair.
     * @param status A string containing the status of the potential
     * duplicate pair.
     * @param reason A string containing the reason the records were
     * listed as potential duplicates.
     * @param weight A floating integer indicating the matching
     * probability between the two records.
     * @param systemCode A string containing the processing code of the
     * system that caused the potential duplication.
     * @param createUser A string containing the logon ID of the user
     * who performed the transaction causing the potential duplication.
     * @param createDate The date the potential duplicate listing was
     * created.
     * @param resolvedUser A string containing the logon ID of the user
     * who resolved the potential duplicate records.
     * @param resolvedDate The date the potential duplicate listing was
     * resolved.
     * @param comment A string containing a comment about the potential
     * duplicate records.
     * @exception ObjectException Thrown if an error occurs
     * while creating the PotentialDuplicateSummary object.
     * @include
     */    
    public PotentialDuplicateSummary(PotentialDuplicateSearchObject searchObj, String id,
	    String euid1, String euid2, String status, String reason, float weight,
	    String systemCode, String createUser, Date createDate, String resolvedUser,
	    Date resolvedDate, String comment) throws ObjectException {
	        super("PotentialDuplicate", mFieldNames, mFieldTypes);
	        this.searchObj = searchObj;
	        setValue("ID", id);
	        setValue("EUID1", euid1);
	        setValue("EUID2", euid2);
	        setValue("Status", status);
	        setValue("Reason", reason);
	        setValue("Weight", new Float(weight));
	        setValue("SystemCode", systemCode);
	        setValue("CreateUser", createUser);
	        setValue("CreateDate", createDate);
	        setValue("ResolvedUser", resolvedUser);
	        setValue("ResolvedDate", resolvedDate);
	        setValue("ResolvedComment", comment);
    }
    
    /**
     * Retrieves the value of the potential duplicate ID from the
     * PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the potential duplicate ID.
     * @exception ObjectException Thrown if the ID could not be
     * retrieved.
     * @include
     */
    public String getId() throws ObjectException {
        return (String) getValue("ID");
    }

    /**
     * Retrieves the value of the first EUID from the
     * PotentialDuplicateSummary object.
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
     * Retrieves the value of the second EUID from the
     * PotentialDuplicateSummary object.
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
     * Retrieves the value of the potential duplicate status of the records
     * in the PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The status of the potential duplicate
     * records. One of these values is returned:
     * <UL>
     * <LI>A - Indicates the pair has been auto resolved.
     * <LI>R - Indicates the pair has been resolved.
     * <LI>Unresolved.
     * </UL>
     * @exception ObjectException Thrown if the status could not
     * be retrieved.
     * @include
     */
    public String getStatus() throws ObjectException {
        return (String) getValue("Status");
    }

    /**
     * Sets the status for a PotentialDuplicateSummary object.
     * <p>
     * @param status A string containing a status. Possible
     * values are:
     * <UL>
     * <LI>A - Indicates the pair has been auto resolved.
     * <LI>R - Indicates the pair has been resolved.
     * <LI>Unresolved.
     * </UL>
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the status could not
     * be set.
     */
    public void setStatus(String status) throws ObjectException {
        setValue("Status", status);
    }

    /**
     * Retrieves the reason the records in the PotentialDuplicateSummary
     * object were marked as potential duplicates.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The reason for the potential duplication.
     * @exception ObjectException Thrown if the reason could not
     * be retrieved.
     * @include
     */
    public String getReason() throws ObjectException {
        return (String) getValue("Reason");
    }

    /**
     * Retrieves the matching probability weight of the records in the
     * PotentialDuplicateSummary object.
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
     * Retrieves the system code from the PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The processing code of the system that
     * caused the potential duplication.
     * @exception ObjectException Thrown if the system code could
     * not be retrieved.
     * @include
     */
    public String getSystemCode() throws ObjectException {
        return (String) getValue("SystemCode");
    }

    /**
     * Retrieves the create user ID from the PotentialDuplicateSummary
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
     * Retrieves the date the potential duplicate listing was created
     * from the PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The create date of the
     * potential duplicate listing.
     * @exception ObjectException Thrown if the date could not be
     * retrieved.
     * @include
     */
    public java.util.Date getCreateDate() throws ObjectException {
        return (java.util.Date) getValue("CreateDate");
    }

    /**
     * Retrieves the resolved user ID from the PotentialDuplicateSummary
     * object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * @exception ObjectException Thrown if the user
     * ID could not be retrieved.
     * @include
     */
    public String getResolvedUser() throws ObjectException {
        return (String) getValue("ResolvedUser");
    }

    /**
     * Retrieves the date the potential duplicate listing was resolved
     * from the PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The resolved date of the
     * potential duplicate listing.
     * @exception ObjectException Thrown if the date could
     * not be retrieved.
     * @include
     */
    public java.util.Date getResolvedDate() throws ObjectException {
        return (java.util.Date) getValue("ResolvedDate");
    }

    /**
     * Retrieves the comment generated when the potential duplicate
     * records in the PotentialDuplicateSummary object were resolved.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A comment describing the
     * resolution.
     * @exception ObjectException Thrown if the comment could
     * not be retrieved.
     * @include
     */
    public String getResolvedComment() throws ObjectException {
        return (String) getValue("ResolvedComment");
    }

    /**
     * Retrieves fields from one EUID object in the PotentialDuplicateSummary
     * object. The fields retrieved are specified by
     * PotentialDuplicateSearchObject.getFieldsToRetrieve.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ObjectNode</CODE> - The specified fields in the
     * given object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ObjectNode getObject1() {
        return mObject1;
    }

    /**
     * Retrieves fields from the second EUID object in the PotentialDuplicateSummary
     * object. The fields retrieved are specified by
     * PotentialDuplicateSearchObject.getFieldsToRetrieve.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ObjectNode</CODE> - The specified fields in the
     * given object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ObjectNode getObject2() {
        return mObject2;
    }

    /** Set object 1
     * @param obj object 1
     */
    public void setObject1(ObjectNode obj) {
        mObject1 = obj;
    }

    /** Set object 2
     * @param obj object 2
     */
    public void setObject2(ObjectNode obj) {
        mObject2 = obj;
    }

    /**
     * Retrieves additional potential duplicate records associated with the
     * potential duplicate records contained in the PotentialDuplicateSummary
     * object. If the resulting iterator only contains one record, there are
     * no associated potential duplicates.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>PotentialDuplicateIterator</CODE> - An iterator of
     * PotentialDuplicateSummary objects.
     * @exception ProcessingException Thrown if an error occurs
     * while processing the associated potential duplicate records.
     * @exception RemoteException Thrown if the connection to the
     * remote server goes down during processing.
     * @include
     */
    public PotentialDuplicateIterator getAssociatedPotentialDuplicates()
    throws RemoteException, ProcessingException {
        /*
         * This function will use euid1 and euid2 of this class
         * to search for the associated potential duplicates
         *
         * If the iterator only contains 1 record, that means no associated records
         * If the iterator contains more than 1 records,
         * this means there are associated potential duplicate
         */
        PotentialDuplicateSearchObject newSearchObj = new PotentialDuplicateSearchObject();
        String euids[] = new String[2];
        try {
            euids[0] = getEUID1();
            euids[1] = getEUID2();
            newSearchObj.setEUIDs(euids);
            newSearchObj.setPageSize(searchObj.getPageSize());
            newSearchObj.setMaxElements(searchObj.getMaxElements());
            newSearchObj.setFieldsToRetrieve(searchObj.getFieldsToRetrieve());
            return mc.lookupPotentialDuplicates(newSearchObj);
        } catch (Exception e) {
            throw new ProcessingException(mLocalizer.t("MAS510: Could not " + 
                                "retrieve associated potential duplicates: {0}", e));
        }
    }

    /**
     * Retrieves a string representation of the potential duplicate ID, EUID1,
     * EUID2, and weight fields from the PotentialDuplicateSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The potential duplicate ID, EUID1, EUID2,
     * and weight field values.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        String retString = null;
        try {
            retString = "[" + getId() + ":" + getEUID1() + "," + getEUID2() + "," + getWeight() + "]";
        } catch (Exception e) {
            retString = "Error: " + e.getMessage();
        }
        return retString;
    }
    
    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeObject(mc);
            out.writeObject(searchObj);
            out.writeObject(mObject1);
            out.writeObject(mObject2);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mc = (MasterController) in.readObject();
            searchObj = (PotentialDuplicateSearchObject) in.readObject();
            mObject1 = (ObjectNode) in.readObject();
            mObject2 = (ObjectNode) in.readObject();
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
