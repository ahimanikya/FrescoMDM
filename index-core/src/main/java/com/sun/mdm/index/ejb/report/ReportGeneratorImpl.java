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
package com.sun.mdm.index.ejb.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.sql.Timestamp;
import java.sql.Connection;

import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.DateUtil;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportRow;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportRow;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportDB;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportRow;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportRow;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportRow;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportRow;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.ReportException;

import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;

import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;

import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;

import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;


class ReportGeneratorImpl implements ReportGenerator {

    private static final int NUM_DAYS_IN_WEEK = 7;
    
    private MasterController mMaster;
    
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of ReportGeneratorImpl
     */
    public ReportGeneratorImpl( ) { 
    }
    
    /**
     * Creates a new instance of ReportGeneratorImpl
     * @param master MasterController
     */
    public ReportGeneratorImpl(MasterController master ) { 
        this.mMaster  = master;
    }
    
    /** Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return assumed match report
     * @throws ReportException an error occured
     */    
    public AssumedMatchReport execAssumedMatchReport(AssumedMatchReportConfig config) 
    throws ReportException{
        AssumedMatchReport report = new AssumedMatchReport(config);
        try {
            AssumedMatchSearchObject searchObj = new AssumedMatchSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            if (config.getStartDate() != null) {
                searchObj.setCreateStartDate(new Timestamp(config.getStartDate().getTime()));
            }
            if (config.getEndDate() != null) {
                searchObj.setCreateEndDate(new Timestamp(config.getEndDate().getTime()));
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            AssumedMatchIterator iterator = mMaster.lookupAssumedMatches(searchObj);
            if (iterator != null) {
                iterator.sortBy("Weight", true);
                while (iterator.hasNext()) {
                    AssumedMatchSummary summary = iterator.next();
                    AssumedMatchReportRow reportRow = new AssumedMatchReportRow(summary, config);
                    report.addRow(reportRow);
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE541: Could not generate the " + 
                                                   "Assumed Match report."));
        }
        return report;
    }
    
    /** Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return assumed match iterator
     * @throws ReportException an error occured
     */    
    public AssumedMatchIterator execAssumedMatchReportIterator(AssumedMatchReportConfig config) 
    throws ReportException {
       
        try {
            AssumedMatchSearchObject searchObj = new AssumedMatchSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            if (config.getStartDate() != null) {
                searchObj.setCreateStartDate(new Timestamp(config.getStartDate().getTime()));
            }
            if (config.getEndDate() != null) {
                searchObj.setCreateEndDate(new Timestamp(config.getEndDate().getTime()));
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            return mMaster.lookupAssumedMatches(searchObj);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE542: Could not retrieve the " + 
                                                   "Assumed Match records: {0}", e));
        }
    }    
    
    /** Execute the potential duplicate report
     * @param config Potential Duplicate Report Configuration
     * @return potential duplicate report
     * @throws ReportException an error occured
     */    
    public PotentialDuplicateReport execPotentialDuplicateReport(PotentialDuplicateReportConfig config) 
    throws ReportException{
        
        PotentialDuplicateReport report = new PotentialDuplicateReport(config);
        try {
            PotentialDuplicateSearchObject searchObj = new PotentialDuplicateSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            if (config.getStartDate() != null) {
                searchObj.setCreateStartDate(new Timestamp(config.getStartDate().getTime()));
            }
            if (config.getEndDate() != null) {
                searchObj.setCreateEndDate(new Timestamp(config.getEndDate().getTime()));
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            
            if (config.getStatus() != null) {
                searchObj.setStatus(config.getStatus());
            }
            
            if (config.getObjectFields() != null) {
                EPathArrayList fields = config.getObjectFields();
                EPathArrayList modFields = new EPathArrayList();
                String objName = ObjectFactory.getObjectDef().getObjectName();
                modFields.add("Enterprise.SystemSBR." + objName + "." + objName + "Id");
                modFields.add("Enterprise.SystemSBR." + objName + ".EUID");
                EPath[] epaths = fields.toArray();
                for (int i = 0; i < epaths.length; i++) {
                    modFields.add("Enterprise.SystemSBR." + epaths[i].toString());
                }
                searchObj.setFieldsToRetrieve(modFields);
            }
            PotentialDuplicateIterator iterator = mMaster.lookupPotentialDuplicates(searchObj);
            if (iterator != null) {
                iterator.sortBy("Weight", true);
                while (iterator.hasNext()) {
                    PotentialDuplicateSummary summary = iterator.next();
                    if (summary.getObject1()!= null && summary.getObject2() != null){
                        PotentialDuplicateReportRow reportRow = new PotentialDuplicateReportRow(summary, config);
                        report.addRow(reportRow);
                    }
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE543: Could not generate the " + 
                                                   "Potential Duplicate report."));
        }
        return report;
    }
    
    /** Execute the potential duplicate report
     * @param config Potential Duplicate Report Configuration
     * @return potential duplicate iterator
     * @throws ReportException an error occured
     */    
    public PotentialDuplicateIterator execPotentialDuplicateReportIterator(PotentialDuplicateReportConfig config) 
    throws ReportException{
        
        PotentialDuplicateReport report = new PotentialDuplicateReport(config);
        try {
            PotentialDuplicateSearchObject searchObj = new PotentialDuplicateSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            if (config.getStartDate() != null) {
                searchObj.setCreateStartDate(new Timestamp(config.getStartDate().getTime()));
            }
            if (config.getEndDate() != null) {
                searchObj.setCreateEndDate(new Timestamp(config.getEndDate().getTime()));
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            
            if (config.getStatus() != null) {
                searchObj.setStatus(config.getStatus());
            }
            
            if (config.getObjectFields() != null) {
                EPathArrayList fields = config.getObjectFields();
                EPathArrayList modFields = new EPathArrayList();
                String objName = ObjectFactory.getObjectDef().getObjectName();
                modFields.add("Enterprise.SystemSBR." + objName + "." + objName + "Id");
                modFields.add("Enterprise.SystemSBR." + objName + ".EUID");
                EPath[] epaths = fields.toArray();
                for (int i = 0; i < epaths.length; i++) {
                    modFields.add("Enterprise.SystemSBR." + epaths[i].toString());
                }
                searchObj.setFieldsToRetrieve(modFields);
            }
            return mMaster.lookupPotentialDuplicates(searchObj);
            
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE544: Could not retrieve the " + 
                                                   "Potential Duplicate records: {0}", e));
        }
    }
    
    /** Execute the merge report
     * @param config Merge Report Configuration
     * @return merge report
     * @throws ReportException an error occured
     */    
    public MergeReport execMergeReport(MergeReportConfig config) 
    throws ReportException{
        
        MergeReport report = new MergeReport(config);
        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();
            
            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            searchObj.setFunction("euidMerge");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }          
            TransactionIterator iterator = mMaster.lookupTransactions(searchObj);
            if (iterator != null) { 
                iterator.sortBy("EUID", false);
                while (iterator.hasNext()) {
                    TransactionSummary summary = iterator.next();
                    MergeReportRow reportRow = new MergeReportRow(summary, config);
                    report.addRow(reportRow);
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE545: Could not generate the " + 
                                                   "Merge report."));
        }
        return report;
    }
    
    /** Execute the unmerge report
     * @param config Unmerge Report Configuration
     * @return unmerge report
     * @throws ReportException an error occured
     */    
    public UnmergeReport execUnmergeReport(UnmergeReportConfig config) 
    throws ReportException{
        
        UnmergeReport report = new UnmergeReport(config);
        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();
            
            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            searchObj.setFunction("euidUnMerge");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }          
            TransactionIterator iterator = mMaster.lookupTransactions(searchObj);
            if (iterator != null) {
                iterator.sortBy("EUID", false);
                while (iterator.hasNext()) {
                    TransactionSummary summary = iterator.next();
                    UnmergeReportRow reportRow = new UnmergeReportRow(summary, config);
                    report.addRow(reportRow);
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE546: Could not generate the " + 
                                                   "Unmerge report."));
        }
        return report;
    }
    
    /** Execute the deactivate report
     * @param config Deactivate Report Configuration
     * @return deactivate report
     * @throws ReportException an error occured
     */    
    public DeactivateReport execDeactivateReport(DeactivateReportConfig config) 
    throws ReportException{

        DeactivateReport report = new DeactivateReport(config);
        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            searchObj.setFunction("euidDeactivate");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }            
            TransactionIterator iterator = mMaster.lookupTransactions(searchObj);
            if (iterator != null) {
                iterator.sortBy("EUID", false);
                while (iterator.hasNext()) {
                    TransactionSummary summary = iterator.next();
                    DeactivateReportRow reportRow = new DeactivateReportRow(summary, config);
                    report.addRow(reportRow);
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE547: Could not generate the " + 
                                                   "Deactivate report."));
        }
        return report;
    }
    
    /** Execute the update report
     * @param config Update Report Configuration
     * @return update report
     * @throws ReportException an error occured
     */    
    public UpdateReport execUpdateReport(UpdateReportConfig config) 
    throws ReportException{
        
        UpdateReport report = new UpdateReport(config);
        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            searchObj.setFunction("Update");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }            
            TransactionIterator iterator = mMaster.lookupTransactions(searchObj);
            if (iterator != null) {
                iterator.sortBy("EUID", false);
                while (iterator.hasNext()) {
                    TransactionSummary summary = iterator.next();
                    UpdateReportRow reportRow = new UpdateReportRow(summary, config);
                    report.addRow(reportRow);
                }
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE548: Could not generate the " + 
                                                   "Update report."));
        }
        return report;
    }
    
    /** Execute the update report
     * @param config Update Report Configuration
     * @return Update Report Iterator
     * @throws ReportException an error occured
     */    
    public TransactionIterator execUpdateReportIterator(UpdateReportConfig config) 
    throws ReportException {
        
        UpdateReport report = new UpdateReport(config);
        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            
            searchObj.setFunction("Update");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            return mMaster.lookupTransactions(searchObj);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE549: Could not retrieve the " + 
                                                   "Update records: {0}", e));
        }
    }    
    /** Execute the weekly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Weekly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execWeeklyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        
        KeyStatisticsReport report = new KeyStatisticsReport(config);
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;
            
            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
            	throw new ReportException(mLocalizer.t("RPE550: Start or end date must be specified " + 
            	                                    "for the Weekly Statistics report."));
            }
            
            // Determine first date of week, Sunday's date, based on specified date
            Date startDate = DateUtil.getFirstDateOfWeek(specDate);
            report.setStartDate(startDate);
            
            String function = KeyStatisticsReport.ADD_FUNCTION;
            report.setAddCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.UPDATE_FUNCTION;
            report.setUpdateCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.DEACTIVATE_FUNCTION;
            report.setDeactivateCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.EUID_MERGE_FUNCTION;
            report.setEuidMergeCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.EUID_UNMERGE_FUNCTION;
            report.setEuidUnmergeCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.LID_MERGE_FUNCTION;
            report.setLidMergeCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.LID_UNMERGE_FUNCTION;
            report.setLidUnmergeCountsForWeek(getWeeklyCount(startDate, function));
            
            function = KeyStatisticsReport.LID_TRANSFER_FUNCTION;
            report.setLidTransferCountsForWeek(getWeeklyCount(startDate, function));
            
            report.setDailyTotalsForWeek(getWeeklyCount(startDate));
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE551: Could not generate the " + 
                                                   "Weekly Statistics report."));
        }
        return report;
    }
    
    /** Execute the monthly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Monthly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execMonthlyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        
        KeyStatisticsReport report = new KeyStatisticsReport(config);
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;
            
            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
            	throw new ReportException(mLocalizer.t("RPE552: Start or end date must be specified " + 
            	                                    "for the Monthly Statistics report."));
            }
            
            // Determine first date of month based on specified date
            Date startDate = DateUtil.getFirstDateOfMonth(specDate);
            report.setStartDate(startDate);
            // Determine last date of month based on specified date
            Date endDate = DateUtil.getLastDateOfMonth(specDate);
            // Get key stats totals
            report = getKeyStatsReportTotals(report, startDate, endDate);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE553: Could not generate the " + 
                                                   "Monthly Statistics report: {0}", e));
        }
        return report;
    }
    
    /** Execute the yearly key statistics report
     * @param config Key Statistics Report Configuration
     * @return Yearly key statistics report
     * @throws ReportException an error occured
     */    
    public KeyStatisticsReport execYearlyKeyStatisticsReport(KeyStatisticsReportConfig config) 
    throws ReportException{
        
        KeyStatisticsReport report = new KeyStatisticsReport(config);
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;
            
            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
            	throw new ReportException(mLocalizer.t("RPE554: Start or end date must be specified " + 
            	                                    "for the Yearly Statistics report."));
            }
            
            // Determine first date of year based on specified date
            Date startDate = DateUtil.getFirstDateOfYear(specDate);
            report.setStartDate(startDate);
            // Determine last date of year based on specified date
            Date endDate = DateUtil.getLastDateOfYear(specDate);
            // Get key stats totals
            report = getKeyStatsReportTotals(report, startDate, endDate);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE555: Could not generate the " + 
                                                   "Yearly Statistics report: {0}", e));
        }
        return report;
    }       

    /** Get JDBC connection
     * @throws ReportException An error occured.
     * @return JDBC connection from pool.
     */    
    private Connection getConnection()
        throws ReportException {
            try {
                Connection con = ConnectionUtil.getConnection();
                return con;
            } catch (Exception e) {
                throw new ReportException(mLocalizer.t("RPE556: failed to get JDBC connection: {0}", e));
            }
    }
    
    /**
     * Get key statistics report information for specified dates 
     * @param report Key statistics report to set values for
     * @param startDate start date of report
     * @param endDate end date of report
     * @return key statistics report for specified dates
     * @exception ReportException
     */
    private KeyStatisticsReport getKeyStatsReportTotals(KeyStatisticsReport report, Date startDate, Date endDate) 
        throws ReportException {
    	
	String function = KeyStatisticsReport.ADD_FUNCTION;
        report.setAddCount(getCount(startDate, endDate, function));
        
        function = KeyStatisticsReport.DEACTIVATE_FUNCTION;
        report.setDeactivateCount(getCount(startDate, endDate, function));
        
        function = KeyStatisticsReport.EUID_MERGE_FUNCTION;
        report.setEuidMergeCount(getCount(startDate, endDate, function));
        
        function = KeyStatisticsReport.EUID_UNMERGE_FUNCTION;
        report.setEuidUnmergeCount(getCount(startDate, endDate, function));
        
        function = KeyStatisticsReport.LID_MERGE_FUNCTION;
        report.setLidMergeCount(getCount(startDate, endDate, function));
        
        function = KeyStatisticsReport.LID_UNMERGE_FUNCTION;
        report.setLidUnmergeCount(getCount(startDate, endDate, function));
        
        boolean resolved = false;
        report.setUnresolvedPotDupCount(getCount(startDate, endDate, resolved));
        
        resolved = true;
        report.setResolvedPotDupCount(getCount(startDate, endDate, resolved));
        
        return report;
    }
    
    // Return an ArrayList of counts for each day of the week of function
    private ArrayList getWeeklyCount(Date startDate, String function) 
        throws ReportException {
        try {
            Connection conn = getConnection();
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            int weeklyTotal = 0;
            Date dateN = startDate;
            
            for (int i = 0; i < NUM_DAYS_IN_WEEK; i++ ) {
                count = statsDb.getDailyCountByFunction(conn, dateN, function);
                weeklyCount.add(new Integer(count));
                // Add up weekly total
                weeklyTotal+=count;
                // Set next date
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dateN);
                cal.add(Calendar.DATE, 1); 
                dateN = cal.getTime();  
            }
            // Add weekly total to ArrayList
            weeklyCount.add(new Integer(weeklyTotal));
            conn.close();
            return weeklyCount;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE557: Error getting weekly count for function : {0}", function));
        }
    }
    
    // Return an ArrayList of counts for each day of the week
    private ArrayList getWeeklyCount(Date startDate) 
        throws ReportException {
        try {
            Connection conn = getConnection();
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            int weeklyTotal = 0;
            Date dateN = startDate;
            
            for (int i = 0; i < NUM_DAYS_IN_WEEK; i++ ) {
                count = statsDb.getDailyCount(conn, dateN);
                weeklyCount.add(new Integer(count));
                // Add up weekly total
                weeklyTotal+=count;
                // Set next date
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dateN);
                cal.add(Calendar.DATE, 1); 
                dateN = cal.getTime();  
            }
            // Add weekly total to ArrayList
            weeklyCount.add(new Integer(weeklyTotal));
            conn.close();
            return weeklyCount;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE558: Error getting weekly count: {0}", e));
        }
    }
    
    // Return count for specified start and end dates and function
    private int getCount(Date startDate, Date endDate, String function) 
        throws ReportException {
        try {
            Connection conn = getConnection();
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            
            count = statsDb.getCountByDatesNFunction(conn, startDate, endDate, function);
            conn.close();
            return count;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE559: Error getting monthly " + 
                                                   "count for function : {0}", function));
        }
    }
    
    /*
     * Return count for potential duplicates, resolved or unresolved 
     * specified by boolean and start and end dates
     */
    private int getCount(Date startDate, Date endDate, boolean resolved) 
        throws ReportException {
        try {
            Connection conn = getConnection();
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            
            if (resolved) {
            	count = statsDb.getResolvedPotDupByDates(conn, startDate, endDate);
            } else {
                count = statsDb.getUnresolvedPotDupByDates(conn, startDate, endDate);
            }
            conn.close();
            return count;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE560: Error getting potential duplicate monthly " + 
                                                   "count for: {0}", (resolved ? "resolved" : "unresolved")));
        }
    }
}
