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
package com.sun.mdm.index.report;

import java.util.Date;
import java.util.ArrayList;
import java.io.PrintStream;

/**
 * Report class for key stats
 * @author  dcidon
 */
public class KeyStatisticsReport extends Report {
    
    //  Add array index 
    public static final int ADD_INDEX = 0;
    //  Deactivate array index 
    public static final int DEACTIVATE_INDEX = 1;
    //  Euid Merge array index 
    public static final int EUID_MERGE_INDEX = 2;
    //  Euid Unmerge array index 
    public static final int EUID_UNMERGE_INDEX = 3;
    //  Lid Merge array index 
    public static final int LID_MERGE_INDEX = 4;
    //  Lid Unmerge array index 
    public static final int LID_UNMERGE_INDEX = 5;
    ///  Unresolved potential duplicate array index 
    public static final int UNRESOLVED_POT_DUP_INDEX = 6;
    //  Resolved potential duplicate array index 
    public static final int RESOLVED_POT_DUP_INDEX = 7;
    //  Add function name 
    public static final String ADD_FUNCTION = "Add";
    //  Update function name 
    public static final String UPDATE_FUNCTION = "Update";
    //  Euid Deactivate function name 
    public static final String DEACTIVATE_FUNCTION = "euidDeactivate";
    //  Euid Merge function name 
    public static final String EUID_MERGE_FUNCTION = "euidMerge";
    //  Euid Unmerge function name 
    public static final String EUID_UNMERGE_FUNCTION = "euidUnMerge";
    //  Lid Merge function name 
    public static final String LID_MERGE_FUNCTION = "lidMerge";
    //  Lid Unmergefunction name 
    public static final String LID_UNMERGE_FUNCTION = "lidUnMerge";
    //  Lid Transfer function name 
    public static final String LID_TRANSFER_FUNCTION = "lidTransfer";
    
    private Date mStartDate;
    private ArrayList mAddCountForWeek;
    private ArrayList mUpdateCountForWeek;
    private ArrayList mDeactivateCountForWeek;
    private ArrayList mEuidMergeCountForWeek;
    private ArrayList mEuidUnmergeCountForWeek;
    private ArrayList mLidMergeCountForWeek;
    private ArrayList mLidUnmergeCountForWeek;
    private ArrayList mLidTransferCountForWeek;
    private ArrayList mDailyTotalsForWeek;
    private ArrayList mTransTotals;
    
    /** Creates a new instance of KeyStatisticsReport  
     * 
     * @param config Report configuration.
     */
      
    public KeyStatisticsReport(ReportConfig config) {
        super(config);
        mTransTotals = new ArrayList();
    }
       
    /** Getter for start date.
     *
     * @return start date.
     */   
    public Date getStartDate() {
    	return mStartDate;
    }
    
    /** Getter for Add function counts for each day of the week.
     *
     * @return Array list of Add function counts for each day of the week.
     */
    public ArrayList getAddCountsForWeek() {
    	return mAddCountForWeek;
    }

    /** Getter for Update function counts for each day of the week.
     *
     * @return Array list of Update function counts for each day of the week.
     */
    public ArrayList getUpdateCountsForWeek() {
    	return mUpdateCountForWeek;
    }
    
    /** Getter for Deactivate function counts for each day of the week.
     *
     * @return Array list of Deactivate function counts for each day of the week.
     */
    public ArrayList getDeactivateCountsForWeek() {
    	return mDeactivateCountForWeek;
    }
    
    /** Getter for Euid Merge function counts for each day of the week.
     *
     * @return Array list of Euid Merge function counts for each day of the week.
     */
    public ArrayList getEuidMergeCountsForWeek() {
    	return mEuidMergeCountForWeek;
    }
    
    /** Getter for Euid Unmerge function counts for each day of the week.
     *
     * @return Array list of Euid Unmerge function counts for each day of the week.
     */
    public ArrayList getEuidUnmergeCountsForWeek() {
    	return mEuidUnmergeCountForWeek;
    }
    
    /** Getter for Lid Merge function counts for each day of the week.
     *
     * @return Array list of Lid Merge function counts for each day of the week.
     */
    public ArrayList getLidMergeCountsForWeek() {
    	return mLidMergeCountForWeek;
    }
    
    /** Getter for Lid Unmerge function counts for each day of the week.
     *
     * @return Array list of Lid Unmerge function counts for each day of the week.
     */
    public ArrayList getLidUnmergeCountsForWeek() {
    	return mLidUnmergeCountForWeek;
    }
    
    /** Getter for Lid Transfer function counts for each day of the week.
     *
     * @return Array list of Lid Transfer function counts for each day of the week.
     */
    public ArrayList getLidTransferCountsForWeek() {
    	return mLidTransferCountForWeek;
    }
    
    /** Getter for Daily Totals for each day of the week.
     *
     * @return Array list of Daily Totals for each day of the week.
     */
    public ArrayList getDailyTotalsForWeek() {
    	return mDailyTotalsForWeek;
    }
    
    /** Getter for transaction totals.
     *
     * @return Array list of transaction totals .
     */
    public ArrayList getTransactionTotals() {
    	return mTransTotals;
    }
    
    /** Setter for start date.
     *
     * @param sDate Start date of the week.
     */
    public void setStartDate(Date sDate) {
    	mStartDate = sDate;
    }
        
    /** Setter for Add function counts for each day of the week.
     * 
     * @param countArr Array list of Add function counts for each day of the week.
     */    
    public void setAddCountsForWeek(ArrayList countArr) {
    	mAddCountForWeek = countArr;
    }
    	
    /** Setter for Update function counts for each day of the week.
     * 
     * @param countArr Array list of Update function counts for each day of the week.
     */ 	
    public void setUpdateCountsForWeek(ArrayList countArr) {
    	mUpdateCountForWeek = countArr;
    }
    
    /** Setter for Deactivate function counts for each day of the week.
     *
     * @param countArr Array list of Deactivate function counts for each day of the week.
     */
    public void setDeactivateCountsForWeek(ArrayList countArr) {
    	mDeactivateCountForWeek = countArr;
    }
    	
    /** Setter for Euid Merge function counts for each day of the week.
     *
     * @param countArr Array list of Euid Merge function counts for each day of the week.
     */	
    public void setEuidMergeCountsForWeek(ArrayList countArr) {
    	mEuidMergeCountForWeek = countArr;
    }
    
    /** Setter for Euid Unmerge function counts for each day of the week.
     *
     * @param countArr Array list of Euid Unmerge function counts for each day of the week.
     */
    public void setEuidUnmergeCountsForWeek(ArrayList countArr) {
    	mEuidUnmergeCountForWeek = countArr;
    }
    	
    /** Setter for Lid Merge function counts for each day of the week.
     *
     * @param countArr Array list of Lid Merge function counts for each day of the week.
     */
    public void setLidMergeCountsForWeek(ArrayList countArr) {
    	mLidMergeCountForWeek = countArr;
    }
    
    /** Setter for Lid Unmerge function counts for each day of the week.
     *
     * @param countArr Array list of Lid Unmerge function counts for each day of the week.
     */
    public void setLidUnmergeCountsForWeek(ArrayList countArr) {
    	mLidUnmergeCountForWeek = countArr;
    }
    
    /** Setter for Lid Transfer function counts for each day of the week.
     *
     * @param countArr Array list of Lid Transfer function counts for each day of the week.
     */
    public void setLidTransferCountsForWeek(ArrayList countArr) {
    	mLidTransferCountForWeek = countArr;
    }
  
    /** Setter for Daily Totals for each day of the week.
     *
     * @param countArr Array list of Daily Totals for each day of the week.
     */
    public void setDailyTotalsForWeek(ArrayList countArr) {
    	mDailyTotalsForWeek = countArr;
    }
    
    /** Setter for Add count.
     *
     * @param count Number of Add transactions.
     */
    public void setAddCount(int count) {
    	mTransTotals.add(ADD_INDEX, new Integer(count));
    }
    
    /** Setter for Deactivate count.
     *
     * @param count Number of Deactivate transactions.
     */
    public void setDeactivateCount(int count) {
    	mTransTotals.add(DEACTIVATE_INDEX, new Integer(count));
    }
    
    /** Setter for Euid Merge count.
     *
     * @param count Number of Euid Merge transactions.
     */
    public void setEuidMergeCount(int count) {
    	mTransTotals.add(EUID_MERGE_INDEX, new Integer(count));
    }
    
    /** Setter for Euid Unmerge count.
     *
     * @param count Number of Euid Unmerge transactions.
     */
    public void setEuidUnmergeCount(int count) {
    	mTransTotals.add(EUID_UNMERGE_INDEX, new Integer(count));
    }
    
    /** Setter for Lid Merge count.
     *
     * @param count Number of Lid Merge transactions.
     */
    public void setLidMergeCount(int count) {
    	mTransTotals.add(LID_MERGE_INDEX, new Integer(count));
    }
    
    /** Setter for Lid Unmerge count.
     *
     * @param count Number of Lid Unmerge transactions.
     */
    public void setLidUnmergeCount(int count) {
    	mTransTotals.add(LID_UNMERGE_INDEX, new Integer(count));
    }
    
    /** Setter for Unresolved Potential Duplicate count.
     *
     * @param count Number of Unresolved Potential Duplicates.
     */
    public void setUnresolvedPotDupCount(int count) {
    	mTransTotals.add(UNRESOLVED_POT_DUP_INDEX, new Integer(count));
    }
    
    /** Setter for Resolved Potential Duplicate count.
     *
     * @param count Number of Resolved Potential Duplicates.
     */
    public void setResolvedPotDupCount(int count) {
    	mTransTotals.add(RESOLVED_POT_DUP_INDEX, new Integer(count));
    }  
}
