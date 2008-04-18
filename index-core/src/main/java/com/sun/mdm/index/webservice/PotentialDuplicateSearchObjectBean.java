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

import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import java.util.Calendar;

/**
 *
 * @author jlu
 */
public class PotentialDuplicateSearchObjectBean {
    private PotentialDuplicateSearchObject mPDSearchObject;
    int mPageNumber;

    /** Creates a new instance of the PotentialDuplicateSearchObjectBean
     * class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public PotentialDuplicateSearchObjectBean() {
        mPDSearchObject = new PotentialDuplicateSearchObject();
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
        return mPDSearchObject.getSystemCode();
    }

    /**
     * Sets the system code for an instance of PotentialDuplicateSearchObjectBean.
     * <p>
     * @param systemCode A string containing a system code.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSystemCode(String systemCode) {
        mPDSearchObject.setSystemCode(systemCode);
    }

    /**
     * Retrieves the value of the <b>local id</b> field to be used in a
     * potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the local ID field.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getLocalId() {
        return mPDSearchObject.getLID();
    }

    /**
     * Sets the local ID for an instance of PotentialDuplicateSearchObjectBean.
     * <p>
     * @param lid A string containing a local ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setLocalId(String lid) {
        mPDSearchObject.setLID(lid);
    }

    /**
     * Sets an array of enterprise-wide identifiers (EUIDs) for an
     * instance of PotentialDuplicateSearchObjectBean.
     * <p>
     * @param euids A string array containing the desired EUIDs.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEUIDs(String[] euids) {
        mPDSearchObject.setEUIDs(euids);
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
        return mPDSearchObject.getEUIDs();
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
        return mPDSearchObject.getStatus();
    }

    /**
     * Sets the status for an instance of PotentialDuplicateSearchObjectBean.
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
        mPDSearchObject.setStatus(status);
    }

    /**
     * Sets the type for an instance of PotentialDuplicateSearchObjectBean.
     * <p>
     * @param type A string containing a type.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setType(String type) {
        mPDSearchObject.setType(type);
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
        return mPDSearchObject.getCreateUser();
    }

    /**
     * Sets the create user ID for an instance of
     * PotentialDuplicateSearchObjectBean.
     * <p>
     * @param systemUser A string containing a user ID.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateUser(String systemUser) {
        mPDSearchObject.setCreateUser(systemUser);
    }

    /**
     * Retrieves the value of the <b>startDate</b> field to be used
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The starting date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getCreateStartDate() {
        return mPDSearchObject.getCreateStartDate();
    }

    /**
     * Sets the starting date for an instance of
     * PotentialDuplicateSearchObjectBean. Use this method with
     * <b>setCreateEndDate</b> to search for potential duplicates
     * created between these two dates.
     * <p>
     * @param startDate The starting date for the
     * potential duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateStartDate(java.util.Date startDate) {
        if (null!=startDate){
            java.sql.Timestamp createStartTime = new java.sql.Timestamp(startDate.getTime());
            mPDSearchObject.setCreateStartDate(createStartTime);
        }       
    }

    /**
     * Retrieves the value of the <b>endDate</b> field to be used
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The end date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getCreateEndDate() {
        return mPDSearchObject.getCreateEndDate();
    }

    /**
     * Sets the ending date for an instance of
     * PotentialDuplicateSearchObjectBean. Use this method with
     * <b>setCreateStartDate</b> to search for potential duplicates
     * created between these two dates.
     * <p>
     * @param endDate The end date for the potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setCreateEndDate(java.util.Date endDate) {
        if (null!=endDate){
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            // if no time set for ending date, then set the time to 23:59:59 
            int hour   = endCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = endCalendar.get(Calendar.MINUTE);
            int second = endCalendar.get(Calendar.SECOND);
            if (hour==0 && minute==0 && second==0){
                endCalendar.set(Calendar.HOUR_OF_DAY,23);
                endCalendar.set(Calendar.MINUTE,59);
                endCalendar.set(Calendar.SECOND,59);
            }
            java.sql.Timestamp createEndTime = 
                    new java.sql.Timestamp(endCalendar.getTime().getTime());
            mPDSearchObject.setCreateEndDate(createEndTime);
        }   
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
        return mPDSearchObject.getResolvedUser();
    }

    /**
     * Sets the resolved user ID for an instance of
     * PotentialDuplicateSearchObjectBean.
     * <p>
     * @param systemUser A string containing the logon ID of the user
     * who resolved the records to find.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedUser(String systemUser) {
        mPDSearchObject.setResolvedUser(systemUser);
    }

    /**
     * Retrieves the value of the <b>resolvedStartDate</b> field to be
     * used in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The starting date for a
     * search for resolved potential duplicate records.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getResolvedStartDate() {
        return mPDSearchObject.getResolvedStartDate();
    }

    /**
     * Sets the resolved start date for an instance of
     * PotentialDuplicateSearchObjectBean. Use this method with
     * <b>setResolvedEndDate</b> to search for potential duplicates
     * resolved between these two dates.
     * <p>
     * @param startDate The resolved start date for the potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedStartDate(java.util.Date startDate) {        
        if (null!=startDate){
            java.sql.Timestamp startTime = new java.sql.Timestamp(startDate.getTime());
            mPDSearchObject.setResolvedStartDate(startTime);
        }   
    }

    /**
     * Retrieves the value of the <b>resolvedEndDate</b> field to be used
     * in a potential duplicate search.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>java.util.Date</CODE> - The resolved end date for the
     * potential duplicate search.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public java.util.Date getResolvedEndDate() {
        return mPDSearchObject.getResolvedEndDate();
    }

    /**
     * Sets the resolved end date for an instance of
     * PotentialDuplicateSearchObjectBean. Use this method with
     * <b>setResolvedStartDate</b> to search for potential duplicates
     * resolved between these two dates.
     * <p>
     * @param endDate The resolved end date for a potential
     * duplicate search.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setResolvedEndDate(java.util.Date endDate) {        
        if (null!=endDate){
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            // if the time of ending date is not set, then set it to 23:59:59 
            int hour   = endCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = endCalendar.get(Calendar.MINUTE);
            int second = endCalendar.get(Calendar.SECOND);
            if (hour==0 && minute==0 && second==0){
                endCalendar.set(Calendar.HOUR_OF_DAY,23);
                endCalendar.set(Calendar.MINUTE,59);
                endCalendar.set(Calendar.SECOND,59);
            }
            java.sql.Timestamp endTime = 
                    new java.sql.Timestamp(endCalendar.getTime().getTime());
            mPDSearchObject.setResolvedEndDate(endTime);
        }
    }
    
    /** 
     * Get max number of records to retrieve.
     * @return max elements
     */
    public int getMaxElements() { 
        return mPDSearchObject.getMaxElements();
    }
    /** 
     * Set max number of records to retrieve.
     * @param maxElements maximum elements
     */
    public void setMaxElements(int maxElements) { 
        mPDSearchObject.setMaxElements(maxElements);
    }
        
    /** 
     * Get number of records transferred to caller when data is required.
     * @return page size
     */
    public int  getPageSize() {    
        return mPDSearchObject.getPageSize();
    }
    
    /** 
     * Set number of records transferred to caller when data is required.
     * @param pageSize page size
     */
    public void setPageSize(int pageSize) {    
        mPDSearchObject.setPageSize(pageSize);
    }
    
    /** 
     * Get page number.
     * @return page number
     */
    public int getPageNumber() {    
        return mPageNumber;
    }
    
    /** 
     * Set page number.
     * @param pageNumber page number
     */
    public void setPageNumber(int pageNumber) {    
        this.mPageNumber = pageNumber;
    }
    
    /** 
     * Get Potential Duplicate Search Object
     * @return Potential Duplicate Search Object
     */
    public PotentialDuplicateSearchObject getPotentialDuplicateSearchObjec() {        
        return mPDSearchObject;
    }
    
}
