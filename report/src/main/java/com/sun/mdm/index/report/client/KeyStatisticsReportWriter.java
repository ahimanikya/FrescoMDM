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
package com.sun.mdm.index.report.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import com.sun.mdm.index.util.DateUtil;
import com.sun.mdm.index.report.KeyStatisticsReport;

/**
 * Report writer class for key stats
 * @author  cychow
 */
public class KeyStatisticsReportWriter {
    /** Width of function column in weekly report */
    private static final int FUNCTION_COL_WIDTH = 17;
    /** Width of day column in weekly report */
    private static final int DAY_COL_WIDTH = 11;
    /** Width of column in monthly report */
    private static final int MONTHLY_REP_COL_WIDTH = 14;
    /** Number of days in a week */
    private static final int NUM_DAYS_IN_WEEK = 7;
    
    private KeyStatisticsReport mKeyStatsReport;
    StringFormatter strFormatter = new StringFormatter();
    
    /** 
      * Creates a new instance of KeyStatisticsReportWriter
      * @param report KeyStatisticsReport object
      */
    public KeyStatisticsReportWriter(KeyStatisticsReport report) {
        mKeyStatsReport = report;
    }
      	
    /** 
     * Write weekly key staticstics report
     * @param reportName Name of the report     
     * @param reportTemplate Name of the report template
     * @param ps Output stream to write to
     * @throws java.lang.Exception 
     */
    public void writeWeekly(String reportName, String reportTemplate, PrintStream ps) 
        throws Exception {
    	
    	StringBuffer sbuf = new StringBuffer();
    	
    	// Write report template and name
    	sbuf.append(reportTemplate + "\n" + reportName + "\n\n");
    	
    	// Construct report header info
    	sbuf.append(strFormatter.rightPadString("Functions", FUNCTION_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("SUNDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("MONDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("TUESDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("WEDNESDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("THURSDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("FRIDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("SATURDAY", DAY_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString("Weekly", DAY_COL_WIDTH, ' '));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString(" ", FUNCTION_COL_WIDTH, ' '));
    	writeDates(sbuf);
    	sbuf.append(strFormatter.rightPadString("Total", DAY_COL_WIDTH, ' '));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("-", FUNCTION_COL_WIDTH + (8 * DAY_COL_WIDTH), '-'));
    	sbuf.append("\n");
    	// Write transaction count info
    	sbuf.append(strFormatter.rightPadString("ADD", FUNCTION_COL_WIDTH, ' '));
    	ArrayList countArr = mKeyStatsReport.getAddCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("UPDATE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getUpdateCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("EUID DEACTIVATE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getDeactivateCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("EUID MERGE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getEuidMergeCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("EUID UNMERGE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getEuidUnmergeCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("LID MERGE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getLidMergeCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("LID UNMERGE", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getLidUnmergeCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("LID TRANSFER", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getLidTransferCountsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	sbuf.append("\n");
    	// Write daily totals
    	sbuf.append(strFormatter.rightPadString("=", FUNCTION_COL_WIDTH + (8 * DAY_COL_WIDTH), '='));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("Daily Totals", FUNCTION_COL_WIDTH, ' '));
    	countArr = mKeyStatsReport.getDailyTotalsForWeek();
    	writeCountForWeek(sbuf, countArr);
    	// Write report to output stream
    	ps.print(sbuf.toString());
    }
    
    /** 
     * Write monthly key staticstics report
     * @param reportName Name of the report
     * @param reportTemplate Name of the report template
     * @param ps Output stream to write to
     * @throws Exception
     */
    public void writeMonthly(String reportName, String reportTemplate, PrintStream ps) 
        throws Exception {
    	
    	StringBuffer sbuf = new StringBuffer();
    	
    	// Write report template and name
    	sbuf.append(reportTemplate + "\n" + reportName + "\n");
    	sbuf.append("for " + DateUtil.getMonth(mKeyStatsReport.getStartDate()) + " ");
    	sbuf.append(DateUtil.getYear(mKeyStatsReport.getStartDate()) + "\n\n");
    	
        // Write report contents
        writeReportContents(sbuf);
    	
    	// Write report to output stream
    	ps.print(sbuf.toString());
    }
    
    /** 
     * Write yearly key staticstics report
     * @param reportName Name of the report
     * @param reportTemplate Name of the report template
     * @param ps Output stream to write to
     * @throws Exception 
     */
    public void writeYearly(String reportName, String reportTemplate, PrintStream ps) 
        throws Exception {
    	
    	StringBuffer sbuf = new StringBuffer();
    	
    	// Write report template and name
    	sbuf.append(reportTemplate + "\n" + reportName + "\n");
    	sbuf.append("for " + DateUtil.getYear(mKeyStatsReport.getStartDate()) + "\n\n");
    	
        // Write report contents
        writeReportContents(sbuf);
    	
    	// Write report to output stream
    	ps.print(sbuf.toString());
    }
    
    // Write report contents for monthly and yearly reports
    private void writeReportContents(StringBuffer sbuf) {
    	
    	// Construct report header info
    	sbuf.append(strFormatter.rightPadString(" ", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("EUID", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.rightPadString(" ", MONTHLY_REP_COL_WIDTH * 4, ' '));
    	sbuf.append(strFormatter.centerNPadString("Unresolved", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Resolved", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.centerNPadString("Add", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Deactivate", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("EUID Merge", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("EUID Unmerge", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("LID Merge", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("LID Unmerge", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Potential", MONTHLY_REP_COL_WIDTH, ' '));    	
    	sbuf.append(strFormatter.centerNPadString("Potential", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Transactions", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append(strFormatter.centerNPadString("Duplicates", MONTHLY_REP_COL_WIDTH, ' '));    	
    	sbuf.append(strFormatter.centerNPadString("Duplicates", MONTHLY_REP_COL_WIDTH, ' '));
    	sbuf.append("\n");
    	sbuf.append(strFormatter.rightPadString("-", 8 * MONTHLY_REP_COL_WIDTH, '-'));
    	sbuf.append("\n");
    	// Write transaction totals
    	writeTransactionTotals(sbuf, mKeyStatsReport.getTransactionTotals());
    	sbuf.append("\n");
    }
    
    // Write dates for weekly report
    private void writeDates(StringBuffer sb) {
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    	String dateStr = null;
    	Date dateN = mKeyStatsReport.getStartDate();
    	
    	// Write dates MM/dd/yy, starting with start date, for each day of week
    	for (int i = 0; i < NUM_DAYS_IN_WEEK; i++ ) {
    	    dateStr = sdf.format((java.util.Date) dateN);
    	    sb.append(strFormatter.rightPadString(dateStr, DAY_COL_WIDTH, ' '));
    	    // Set next date
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dateN);
            cal.add(Calendar.DATE, 1); 
            dateN = cal.getTime();
        }
    }
    
    // Write transaction counts for weekly report
    private void writeCountForWeek(StringBuffer sb, ArrayList countArr) {
    	
    	if (countArr != null) {
    	    // Write transaction count for each day of the week
    	    for (int i = 0; i < countArr.size(); i++) {
    	        sb.append(strFormatter.rightPadString(((Integer) countArr.get(i)).toString(), DAY_COL_WIDTH, ' '));
    	    }
    	}
    }
    
    // Write transaction totals for monthly and yearly reports
    private void writeTransactionTotals(StringBuffer sb, ArrayList countArr) {
    	
    	if (countArr != null) {
    	    // Write transaction totals for each function
    	    for (int i = 0; i < countArr.size(); i++) {
    	        sb.append(strFormatter.centerNPadString(((Integer) countArr.get(i)).toString(), MONTHLY_REP_COL_WIDTH, ' '));
    	    }
    	}
    }
}
