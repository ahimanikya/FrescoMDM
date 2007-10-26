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

import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.ReportException;

/**
 * Interface for ReportGeneratorImpl
 */

public interface BatchReportGenerator{

    /** Execute the assumed match report
     * @param config Assumed Match Report Configuration
     * @return AssumedMatchReport
     * @throws ReportException an error occured
     */
    AssumedMatchReport execAssumedMatchReport(AssumedMatchReportConfig config)
    throws ReportException;

    /** To get the range of assumed match records
     * @return AssumedMatchReport
     * @throws ReportException an error occured
     */
    AssumedMatchReport getNextAssumedMatchRecords()
    throws ReportException;

    /** Execute the potential duplicate report
     * @param config potential duplicate report config
     * @return Potential Duplicate Report
     * @throws ReportException an error occured
     */
    PotentialDuplicateReport execPotentialDuplicateReport(PotentialDuplicateReportConfig config)
    throws ReportException;

    /** To get range of potential duplicate records
     * @return Next Potential Duplicate records
     * @throws ReportException an error occured
     */
    PotentialDuplicateReport getNextPotDupRecords()
    throws ReportException;

    /** Execute the merge report
     * @param config merge report config
     * @return merge report
     * @throws ReportException an error occured
     */
    MergeReport execMergeReport(MergeReportConfig config)
    throws ReportException;

    /** To get the range of Merged records
     * @return Next Merge Records
     * @throws ReportException an error occured
     */
    MergeReport getNextMergeRecords()
	throws ReportException;

    /** Execute the unmerge report
     * @param config unmerge report config
     * @return unmerge report
     * @throws ReportException an error occured
     */
    UnmergeReport execUnmergeReport(UnmergeReportConfig config)
    throws ReportException;

    /** To get the range of Unmerged records
     * @return Next Unmerge Records
     * @throws ReportException an error occured
     */
    UnmergeReport getNextUnmergeRecords()
	throws ReportException;

    /** Execute the deactivate report
     * @param config deactivate report config
     * @return deactivate report
     * @throws ReportException an error occured
     */
    DeactivateReport execDeactivateReport(DeactivateReportConfig config)
    throws ReportException;

    /** To get the range of Deactivated records
     * @return Next Deactivate Records
     * @throws ReportException an error occured
     */
    DeactivateReport getNextDeactivateRecords()
    throws ReportException;

    /** Execute the update report
     * @param config update report config
     * @return update report
     * @throws ReportException an error occured
     */
    UpdateReport execUpdateReport(UpdateReportConfig config)
    throws ReportException;

    /** To get the range of Updated records
     * @return Next Update Records
     * @throws ReportException an error occured
     */
    UpdateReport getNextUpdateRecords()
	throws ReportException;

    /** Execute the weekly key statistics report
     * @param config key statistics report configuration
     * @return Weekly key statistics report
     * @throws ReportException an error occured
     */
    KeyStatisticsReport execWeeklyKeyStatisticsReport(KeyStatisticsReportConfig config)
    throws ReportException;

    /** Execute the monthly key statistics report
     * @param config key statistics report configuration
     * @return Monthly key statistics report
     * @throws ReportException an error occured
     */
    KeyStatisticsReport execMonthlyKeyStatisticsReport(KeyStatisticsReportConfig config)
    throws ReportException;

    /** Execute the yearly key statistics report
     * @param config key statistics report configuration
     * @return Yearly key statistics report
     * @throws ReportException an error occured
     */
    KeyStatisticsReport execYearlyKeyStatisticsReport(KeyStatisticsReportConfig config)
    throws ReportException;

}
