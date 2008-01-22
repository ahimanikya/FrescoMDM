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
import com.sun.mdm.index.querybuilder.SearchOptions;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * The <b>PotentialDuplicateSearchObject</b> class is an object representing the
 * search criteria passed to <b>lookupPotentialDuplicate</b> when performing
 * a search for potential duplicates. Use the methods in this class to
 * populate or retrieve the fields in the search object, and to define the fields
 * in the potential duplicate search results list.
 */

public class PotentialDuplicateSearchObject extends SearchOptions implements java.io.Serializable {
    /** EUID array
     */
    private String[] euids;
    /** System code
     */
    private String systemCode;
    /** Local id
     */
    private String lid;
    /** Status
     */
    private String status;    // (R)esolved, (A)uto Resolve, (Unresolved)
    /** Create user
     */
    private String createUser;
    /** Type
     */
    private String type;
    /** Create start date
     */
    private java.sql.Timestamp createStartDate;
    /** Create end date
     */
    private java.sql.Timestamp createEndDate;
    /** Resolved user
     */
    private String resolvedUser;
    /** Resolved start date
     */
    private java.sql.Timestamp resolvedStartDate;
    /** Resolved end date
     */
    private java.sql.Timestamp resolvedEndDate;
    /** Fields to retrieve
     */
    private EPathArrayList fieldsToRetrieve;
    /** Fields to search on
     */
    private EPathArrayList mFieldsToSearch;
    /** Value of the fields to search on
     */
    private ArrayList mSearchCriteriaValues;
    
    /** Creates a new instance of the PotentialDuplicateSearchObject
     * class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public PotentialDuplicateSearchObject() {
    }

    /**
     * Retrieves the value of the <b>systemCode</b> field to be used in a
     * potential duplicate search.
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
     * Sets the system code for an instance of PotentialDuplicateSearchObject.
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
     * Retrieves the value of the <b>lid</b> field to be used in a
     * potential duplicate search.
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
     * Sets the local ID for an instance of PotentialDuplicateSearchObject.
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
     * Sets the enterprise-wide identifier (EUID) for an instance of
     * PotentialDuplicateSearchObject.
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
     * instance of PotentialDuplicateSearchObject.
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
     * Retrieves a string array of EUIDs to use as criteria for a
     * potential duplicate search.
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
     * Retrieves the value of the <b>status</b> field to be used in a
     * potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the
     * status field. One of the following values is returned:
     * <UL>
     * <LI>A - Indicates the pair has been auto resolved.
     * <LI>R - Indicates the pair has been resolved.
     * <LI>Unresolved.
     * </UL>
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status for an instance of PotentialDuplicateSearchObject.
     * <p>
     * @param status A string containing a status. Possible
     * values are:
     * <UL>
     * <LI>A - Indicates the pair has been auto resolved.
     * <LI>R - Indicates the pair has been resolved.
     * <LI>Unresolved.
     * </UL>
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the value of the <b>type</b> field to be used in a
     * potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> -  The value of the type field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type for an instance of PotentialDuplicateSearchObject.
     * <p>
     * @param type A string containing a type.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the value of the <b>createUser</b> field (the user ID of
     * the user who created the potential duplicate records) to be used in
     * a potential duplicate search.
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
     * PotentialDuplicateSearchObject.
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
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The starting date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateStartDate() {
        return createStartDate;
    }

    /**
     * Sets the starting date for an instance of
     * PotentialDuplicateSearchObject. Use this method with
     * <b>setCreateEndDate</b> to search for potential duplicates
     * created between these two dates.
     * <p>
     * @param startDate The starting date for the
     * potential duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateStartDate(java.sql.Timestamp startDate) {
        this.createStartDate = startDate;
    }

    /**
     * Retrieves the value of the <b>endDate</b> field to be used
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The end date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Sets the ending date for an instance of
     * PotentialDuplicateSearchObject. Use this method with
     * <b>setCreateStartDate</b> to search for potential duplicates
     * created between these two dates.
     * <p>
     * @param endDate The end date for the potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateEndDate(java.sql.Timestamp endDate) {
        this.createEndDate = endDate;
    }

    /**
     * Retrieves the value of the <b>resolvedUser</b> field (the
     * user ID of the user who resolved the potential duplicates) to
     * be used in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The logon ID of the user who
     * resolved the records to find.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getResolvedUser() {
        return resolvedUser;
    }

    /**
     * Sets the resolved user ID for an instance of
     * PotentialDuplicateSearchObject.
     * <p>
     * @param systemUser A string containing the logon ID of the user
     * who resolved the records to find.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedUser(String systemUser) {
        this.resolvedUser = systemUser;
    }

    /**
     * Retrieves the value of the <b>resolvedStartDate</b> field to be
     * used in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The starting date for a
     * search for resolved potential duplicate records.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getResolvedStartDate() {
        return resolvedStartDate;
    }

    /**
     * Sets the resolved start date for an instance of
     * PotentialDuplicateSearchObject. Use this method with
     * <b>setResolvedEndDate</b> to search for potential duplicates
     * resolved between these two dates.
     * <p>
     * @param startDate The resolved start date for the potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedStartDate(java.sql.Timestamp startDate) {
        this.resolvedStartDate = startDate;
    }

    /**
     * Retrieves the value of the <b>resolvedEndDate</b> field to be used
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.sql.Timestamp</CODE> - The resolved end date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.sql.Timestamp getResolvedEndDate() {
        return resolvedEndDate;
    }

    /**
     * Sets the resolved end date for an instance of
     * PotentialDuplicateSearchObject. Use this method with
     * <b>setResolvedStartDate</b> to search for potential duplicates
     * resolved between these two dates.
     * <p>
     * @param endDate The resolved end date for a potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedEndDate(java.sql.Timestamp endDate) {
        this.resolvedEndDate = endDate;
    }

    /**
     * Sets the fields to be retrieved from the resulting records
     * of a potential duplicate search.
     * <p>
     * @param fieldsToRetrieve An array containing the ePaths to the fields
     * to retrieve (see <i>Creating a Master Index with Master Index Studio</i>
     * for more information about ePaths).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setFieldsToRetrieve(EPathArrayList fieldsToRetrieve) {
        this.fieldsToRetrieve = fieldsToRetrieve;
    }
    
    /**
     * Sets the fields to be retrieved from the resulting records
     * of a potential duplicate search.
     * <p>
     * @param fieldsToSearch An array containing the ePaths to the fields
     * that are used as the search criteria (see <i>Creating a Master Index with Master Index Studio</i>
     * for more information about ePaths).
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setFieldsToSearch(EPathArrayList fieldsToSearch) {
        mFieldsToSearch = fieldsToSearch;
    }
    /**
     * Sets the value of the search criteria fields.
     * <
     * @param fieldsToSearch An ArrayList containing the user-supplied values 
     * to the fields that are used as the search criteria 
     */
    public void setSearchCriteriaValues(ArrayList searchCriteriaValues) {
        mSearchCriteriaValues = searchCriteriaValues;
    }

    /**
     * Retrieves an array of ePaths to the fields to be retrieved from the
     * results of a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EPathArrayList</CODE> - An array containing the ePaths to
     * the fields to retrieve.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EPathArrayList getFieldsToRetrieve() {
            return fieldsToRetrieve;
    }
    
    /**
     * Retrieves an array of ePaths to the fields that are used as the search
     * criteria for a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EPathArrayList</CODE> - An array containing the ePaths to
     * the fields used for the search criteria.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EPathArrayList getFieldsToSearch() {
            return mFieldsToSearch;
    }
    /**
     * Retrieves an ArrayList of the values used as the search
     * criteria for a potential duplicate search.
     * 
     * @return An ArrayList containing the valiues of the 
     * the fields used for the search criteria.
     */
    public ArrayList getSearchCriteriaValues() {
            return mSearchCriteriaValues;
    }

    /**
     * Retrieves a string representation of the fields in the
     * PotentialDuplicateSearchObject object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The field values contained in the
     * PotentialDuplicateSearchObject object.
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
        sb.append("Status: ").append(status).append('\n');
        sb.append("CreateUser: ").append(createUser).append('\n');
        sb.append("CreateStartDate: ").append(createStartDate).append('\n');
        sb.append("CreateEndDate: ").append(createEndDate).append('\n');
        sb.append("ResolvedUser: ").append(resolvedUser).append('\n');
        sb.append("ResolvedStartDate: ").append(resolvedStartDate).append('\n');
        sb.append("ResolvedEndDate: ").append(resolvedEndDate).append('\n');
        sb.append("MaxElements: ").append(mMaxElements).append('\n');
        sb.append("PageSize: ").append(mPageSize);
        return sb.toString();
    }

}
