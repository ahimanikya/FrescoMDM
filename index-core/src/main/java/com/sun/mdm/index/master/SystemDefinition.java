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
package com.sun.mdm.index.master;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * The <b>SystemDefinition</b> class represents the information about the
 * external systems sharing data with the master index application. This
 * information is stored in the sbyn_systems database table.
 */
public class SystemDefinition implements java.io.Serializable, Cloneable, Comparable<SystemDefinition> {

    private String mSystemCode;
    private String mDescription;
    private String mStatus;
    private int mIdLength;
    private String mFormat;
	private Pattern mPattern;
    private String mInputMask;
    private String mValueMask;
    private Date mCreateDate;
    private String mCreateUserId;
    private Date mUpdateDate;
    private String mUpdateUserId;

    /**
     * Creates a new instance of the SystemDefinition class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public SystemDefinition() {
    }

    /**
     * Retrieves the system code from a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The processing code of the system
     * represented by the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getSystemCode() {
        return mSystemCode;
    }
    /**
     * Retrieves the system description from a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The description of the system
     * represented by the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getDescription() {
        return mDescription;
    }
    /**
     * Retrieves the system status from a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The status of the system
     * represented by the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getStatus() {
        return mStatus;
    }
    /**
     * Retrieves the defined length of the local IDs assigned by the system
     * represented in a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>int</CODE> - The local ID length defined for the system
     * in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int getIdLength() {
        return mIdLength;
    }
    /**
     * Retrieves the format of the local IDs assigned by the system
     * represented in a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The local ID format of the system
     * in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getFormat() {
        return mFormat;
    }
    /**
     * Retrieves the compiled format of the local IDs assigned by the system
     * represented in a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Pattern</CODE> - The compiled local ID format of the system
     * in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Pattern getPattern() {
        return mPattern;
    }
    /**
     * Retrieves the input mask defined for the local IDs assigned by the
     * system represented in a SystemDefinition object. The input mask is
     * used by the Enterprise Data Manager to add punctuation to a
     * displayed field.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The input mask of the local IDs assigned by
     * the system in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getInputMask() {
        return mInputMask;
    }
    /**
     * Retrieves the value mask for the local IDs assigned by the system
     * represented in a SystemDefinition object. The value mask is used to
     * strip any punctuation added by the input mask before storing a value
     * in the database.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value mask of the local IDs assigned by
     * the system in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getValueMask() {
        return mValueMask;
    }
    /**
     * Retrieves the create date of the system record represented in a
     * SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The create date of the system
     * in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Date getCreateDate() {
        return mCreateDate;
    }
    /**
     * Retrieves the login ID of the user who created the system record
     * represented in a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user login ID.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getCreateUser() {
        return mCreateUserId;
    }
    /**
     * Retrieves the date the system record represented in a
     * SystemDefinition object was last updated.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The update date of the system
     * in the SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Date getUpdateDate() {
        return mUpdateDate;
    }
    /**
     * Retrieves the login ID of the user who last updated the system record
     * represented in a SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user login ID.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getUpdateUser() {
        return mUpdateUserId;
    }
    /**
     * Sets the processing code for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param systemCode An string containing a system code.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSystemCode(String systemCode) {
        mSystemCode = systemCode;
    }
    /**
     * Sets the description for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param description An string containing a description.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setDescription(String description) {
        mDescription = description;
    }
    /**
     * Sets the status of the system represented in a
     * SystemDefinition object.
     * <p>
     * @param status A string containing a status.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setStatus(String status) {
        mStatus = status;
    }
    /**
     * Sets the local ID length for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param length An integer indicating the length of the local IDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setIdLength(int length) {
        mIdLength = length;
    }
    /**
     * Sets the local ID format for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param format A string indicating the format of the local IDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setFormat(String format) {
        mFormat = format;
		if (mFormat != null) {
			mPattern = Pattern.compile(format);
		} else {
			mPattern = null;
		}
    }
    /**
     * Sets the local ID input mask for the system represented in a
     * SystemDefinition object. The input mask is used by the Enterprise
     * Data Manager to add punctuation to a field. For example, if users
     * enter dates in the format MMDDYYYY, you can add an input mask to
     * display the dates as MM/DD/YYYY. To define an input mask, enter a
     * character type for each character in the field, and place any necessary
     * punctuation between the character types. For example, the input mask
     * for the above date format is DD/DD/DDDD.
     * The following character types are allowed:
     * <UL>
     * <LI><b>D</b> - indicates a numeric character.
     * <LI><b>L</b> - indicates an alphabetic character.
     * <LI><b>A</b> - indicates an alphanumeric character.
     * </UL>
     * <p>
     * @param inputMask The input mask for the local IDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setInputMask(String inputMask) {
        mInputMask = inputMask;
    }
    /**
     * Sets the local ID value mask for the system represented in a
     * SystemDefinition object. The value mask is used by the master
     * index to strip any extra characters that were added by the input
     * mask, ensuring that data is stored in the database in the correct
     * format. To specify a value mask, enter the same value as is
     * entered for the input mask, but type an "x" in place of each
     * punctuation mark. For example, if an SSN field has an input mask
     * of DDD-DD-DDDD, specify a value mask of DDDxDDxDDDD in order to
     * strip the dashes before storing the SSN. A value mask is not
     * required for date fields.
     * <p>
     * @param valueMask The value mask for the local IDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setValueMask(String valueMask) {
        mValueMask = valueMask;
    }
    /**
     * Sets the create date for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param createDate A date indicating when the system record was
     * created.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }
    /**
     * Sets the create user ID for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param createUserId A user login ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateUserId(String createUserId) {
        mCreateUserId = createUserId;
    }
    /**
     * Sets the update date for the system represented in a
     * SystemDefinition object.
     * <p>
     * @param updateDate A date indicating when the system record was
     * last updated.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setUpdateDate(Date updateDate) {
        mUpdateDate = updateDate;
    }
    /**
     * Sets the login ID of the user who last updated the system record
     * represented in a SystemDefinition object.
     * <p>
     * @param updateUserId A user login ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setUpdateUserId(String updateUserId) {
        mUpdateUserId = updateUserId;
    }
    /**
     * Retrieves a string representation of the fields in the
     * SystemDefinition object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The field values contained in the
     * SystemDefinition object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return "[" + mSystemCode + "," + mDescription + "," + mStatus
         + "," + mIdLength +  "," + mFormat + "," + mInputMask
         + "," + mValueMask + "," + mCreateDate + "," + mCreateUserId
         + "," + mUpdateDate + "," + mUpdateUserId + "]";
    }

	/**
	 * Clone the current SystemDefinition object
	 * @return a copy of the current SystemDefinition object
	 */
	public Object clone() {
		SystemDefinition sd = new SystemDefinition();
		sd.setSystemCode(mSystemCode);
		sd.setDescription(mDescription);
		sd.setStatus(mStatus);
		sd.setIdLength(mIdLength);
		sd.setFormat(mFormat);
		sd.setInputMask(mInputMask);
		sd.setValueMask(mValueMask);
		sd.setCreateDate(mCreateDate);
		sd.setCreateUserId(mCreateUserId);
		sd.setUpdateDate(mUpdateDate);
		sd.setUpdateUserId(mUpdateUserId);
		return sd;
	}
	
	/**
	 * Compare with the current SystemDefinition
	 * @param obj SystemDefinition to compare to
	 * @return negative, zero, or positive when less than, equal, or greater than the compared object
	 */
	public int compareTo(SystemDefinition obj) {
		return mSystemCode.compareTo(obj.getSystemCode());
	}
}
