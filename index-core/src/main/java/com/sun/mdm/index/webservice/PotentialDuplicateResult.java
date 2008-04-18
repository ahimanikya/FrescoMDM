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

package com.sun.mdm.index.webservice;

import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.Date;

public class PotentialDuplicateResult {
    private PotentialDuplicateSummary mPotentialDuplicateSummary;
    
    //Creates a new instance of the PotentialDuplicateResult
    public PotentialDuplicateResult() throws ObjectException{
        mPotentialDuplicateSummary = new PotentialDuplicateSummary();
    }
    
    public PotentialDuplicateResult(PotentialDuplicateSummary pds) 
            throws ObjectException {
        mPotentialDuplicateSummary = pds;        
    }
    
    /**
     * Retrieves the value of the potential duplicate ID from the
     * PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the potential duplicate ID.
     * @exception ObjectException Thrown if the ID could not be
     * retrieved.
     * @include
     */
    public String getId() throws ObjectException {
        return mPotentialDuplicateSummary.getId();
    }
    
    /**
     * Set the value of the potential duplicate ID for the
     * PotentialDuplicateResult object.
     * <p>
     * @param id A string containing a potential duplicate ID
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the ID could not be
     * set.
     * @include
     */
    public void setId(String id) throws ObjectException {
        mPotentialDuplicateSummary.setValue("ID", id);
    }

    /**
     * Retrieves the value of the first EUID from the
     * PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if the EUID could not be
     * retrieved.
     * @include
     */
    public String getEUID1() throws ObjectException {
        return mPotentialDuplicateSummary.getEUID1();
    }
    
    /**
     * Set the value of the first EUID for the
     * PotentialDuplicateResult object.
     * <p>
     * @param euid1 A string containing the first EUID
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the first EUID could not be
     * set.
     * @include
     */
    public void setEUID1(String euid1) throws ObjectException {
        mPotentialDuplicateSummary.setValue("EUID1", euid1);
    }

    /**
     * Retrieves the value of the second EUID from the
     * PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - An EUID.
     * @exception ObjectException Thrown if the EUID could not be
     * retrieved.
     * @include
     */
    public String getEUID2() throws ObjectException {
        return mPotentialDuplicateSummary.getEUID2();
    }
    
    /**
     * Set the value of the second EUID for the
     * PotentialDuplicateResult object.
     * <p>
     * @param euid2 A string containing the second EUID
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the second EUID could not be
     * set.
     * @include
     */
    public void setEUID2(String euid2) throws ObjectException {
        mPotentialDuplicateSummary.setValue("EUID2", euid2);
    }

    /**
     * Retrieves the value of the potential duplicate status of the records
     * in the PotentialDuplicateResult object.
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
        return mPotentialDuplicateSummary.getStatus();
    }

    /**
     * Sets the status for a PotentialDuplicateResult object.
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
        mPotentialDuplicateSummary.setValue("Status", status);
    }

    /**
     * Retrieves the reason the records in the PotentialDuplicateResult
     * object were marked as potential duplicates.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The reason for the potential duplication.
     * @exception ObjectException Thrown if the reason could not
     * be retrieved.
     * @include
     */
    public String getReason() throws ObjectException {
        return mPotentialDuplicateSummary.getReason();
    }

    /**
     * Set the reason for the PotentialDuplicateResult object.
     * <p>
     * @param reason A string containing the reason
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the reason could not be
     * set.
     * @include
     */
    public void setReason(String reason) throws ObjectException {
        mPotentialDuplicateSummary.setValue("Reason", reason);
    }
    
    /**
     * Retrieves the matching probability weight of the records in the
     * PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Float</CODE> - The matching probability weight
     * between two records.
     * @exception ObjectException Thrown if the weight could not be
     * retrieved.
     * @include
     */
    public float getWeight() throws ObjectException {
        return mPotentialDuplicateSummary.getWeight();
    }
    
    /**
     * Set the weight for the PotentialDuplicateResult object.
     * <p>
     * @param weight A float containing the weight
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the weight could not be
     * set.
     * @include
     */
    public void setWeight(float weight) throws ObjectException {
        mPotentialDuplicateSummary.setValue("Weight", new Float(weight));
    }

    /**
     * Retrieves the system code from the PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The processing code of the system that
     * caused the potential duplication.
     * @exception ObjectException Thrown if the system code could
     * not be retrieved.
     * @include
     */
    public String getSystemCode() throws ObjectException {
        return mPotentialDuplicateSummary.getSystemCode();
    }
    
    /**
     * Set the System Code for the PotentialDuplicateResult object.
     * <p>
     * @param systemCode A String containing the System Code
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the System Code could not be
     * set.
     * @include
     */
    public void setSystemCode(String systemCode) throws ObjectException {
        mPotentialDuplicateSummary.setValue("SystemCode", systemCode);
    }

    /**
     * Retrieves the create user ID from the PotentialDuplicateResult
     * object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * @exception ObjectException Thrown if the user ID could not be
     * retrieved.
     * @include
     */
    public String getCreateUser() throws ObjectException {
        return mPotentialDuplicateSummary.getCreateUser();
    }
    
    /**
     * Set the Create User ID for the PotentialDuplicateResult object.
     * <p>
     * @param createUser A String containing the Create User ID
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the Create User ID could not be
     * set.
     * @include
     */
    public void setCreateUser(String createUser) throws ObjectException {
        mPotentialDuplicateSummary.setValue("CreateUser", createUser);
    }

    /**
     * Retrieves the date the potential duplicate listing was created
     * from the PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The create date of the
     * potential duplicate listing.
     * @exception ObjectException Thrown if the date could not be
     * retrieved.
     * @include
     */
    public java.util.Date getCreateDate() throws ObjectException {
        return mPotentialDuplicateSummary.getCreateDate();
    }
    
    /**
     * Set the date the potential duplicate listing was created
     * for the PotentialDuplicateResult object.
     * <p>
     * @param createDate A Date containing the creation date
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the creation date could not be
     * set.
     * @include
     */
    public void setCreateDate(Date createDate) throws ObjectException {
        mPotentialDuplicateSummary.setValue("CreateDate", createDate);
    }

    /**
     * Retrieves the resolved user ID from the PotentialDuplicateResult
     * object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A user logon ID.
     * @exception ObjectException Thrown if the user
     * ID could not be retrieved.
     * @include
     */
    public String getResolvedUser() throws ObjectException {
        return mPotentialDuplicateSummary.getResolvedUser();
    }
    
    /**
     * Set the resolved user ID for the PotentialDuplicateResult object.
     * <p>
     * @param resolvedUser A String containing the resolved user ID
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the resolved user ID could not be
     * set.
     * @include
     */
    public void setResolvedUser(String resolvedUser) throws ObjectException {
        mPotentialDuplicateSummary.setValue("ResolvedUser", resolvedUser);
    }

    /**
     * Retrieves the date the potential duplicate listing was resolved
     * from the PotentialDuplicateResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The resolved date of the
     * potential duplicate listing.
     * @exception ObjectException Thrown if the date could
     * not be retrieved.
     * @include
     */
    public java.util.Date getResolvedDate() throws ObjectException {
        return mPotentialDuplicateSummary.getResolvedDate();
    }
    
    /**
     * Set the date the potential duplicate listing was resolved
     * for the PotentialDuplicateResult object.
     * <p>
     * @param resolvedDate A Date containing the resolving date
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the resolving date could not be
     * set.
     * @include
     */
    public void setResolvedDate(Date resolvedDate) throws ObjectException {
        mPotentialDuplicateSummary.setValue("ResolvedDate", resolvedDate);
    }

    /**
     * Retrieves the comment generated when the potential duplicate
     * records in the PotentialDuplicateResult object were resolved.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A comment describing the
     * resolution.
     * @exception ObjectException Thrown if the comment could
     * not be retrieved.
     * @include
     */
    public String getResolvedComment() throws ObjectException {
        return mPotentialDuplicateSummary.getResolvedComment();
    }
    
    /**
     * Set the comment generated when the potential duplicate
     * records were resolved for the PotentialDuplicateResult object.
     * <p>
     * @param comment A String containing the resolved comment
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if the resolved comment could not be
     * set.
     * @include
     */
    public void setResolvedComment(String comment) throws ObjectException {
        mPotentialDuplicateSummary.setValue("ResolvedComment", comment);
    }

    /**
     * Retrieves a string representation of the potential duplicate ID, EUID1,
     * EUID2, and weight fields from the PotentialDuplicateResult object.
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

}
