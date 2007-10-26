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

import com.sun.mdm.index.ejb.report.BatchReportGenerator;
import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.AssumedMatchReportRow;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.DeactivateReportRow;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReportRow;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.UpdateReportRow;
import com.sun.mdm.index.report.MergeReportRow;
import com.sun.mdm.index.report.UnmergeReportRow;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author  rtam
 */
public class ReportWriter {
    
    /**  assumed match report name */
    private final String ASSUMED_MATCH_REPORT_TEMPLATE = "Assumed Match";
    
    /**  potential duplicate report name */
    private final String POTENTIAL_DUPLICATE_REPORT_TEMPLATE = "Potential Duplicate";
    
    /**  deactivated report name */
    private final String DEACTIVATED_REPORT_TEMPLATE = "Deactivated";
    
    /**  update report name */
    private final String UPDATE_REPORT_TEMPLATE = "Update";
    
    /**  merged report name */
    private final String MERGED_REPORT_TEMPLATE = "Merged";
    
    /**  unmerged report name */
    private final String UNMERGEDREPORT_NAME = "Unmerged";
    
    /**  weekly key activity report name */
    private final String KEY_ACTIVITY_WEEKLY_REPORT_TEMPLATE = "Weekly Activity";
    
    /**  monthly key activity report name */
    private final String KEY_ACTIVITY_MONTHLY_REPORT_TEMPLATE = "Monthly Activity";
    
    /**  yearly key activity report name */
    private final String KEY_ACTIVITY_YEARLY_REPORT_TEMPLATE = "Yearly Activity";
    
    /** Default Page Size */

    private int Default_Page_Size = 500;
    /** Creates a new instance of ReportWriter */
    public ReportWriter() {
    }

    /** Create the reports
     *
     * @param repgen  report generator
     * @param username  user name
     * @param password  password
     * @param outputDirectory  output directory
     * @param cfgReader  configuration file reader
     * @return true or false
     * @throws Exception 
     */
    public boolean createReports(BatchReportGenerator repgen,
                                 String username, 
                                 String password, 
                                 File outputDirectory, 
                                 ReportConfigurationReader cfgReader)
            throws Exception {
		int i;
        //  retrieve the configuration data
        ReportConfiguration reportConfig = cfgReader.getReportConfig();
        String application = reportConfig.getApplication();
        ArrayList reportDefinitions = reportConfig.getReportDefinitions();
        
        //  Determine which reports are enabled.  Set the appropriate flags
        //  and configuration information.

        AssumedMatchReportConfig amrConfig = new AssumedMatchReportConfig();
        PotentialDuplicateReportConfig pdrConfig = new PotentialDuplicateReportConfig();
        DeactivateReportConfig drConfig = new DeactivateReportConfig();
        UpdateReportConfig urConfig = new UpdateReportConfig();
        MergeReportConfig mrConfig = new MergeReportConfig();
        UnmergeReportConfig umrConfig = new UnmergeReportConfig();
        KeyStatisticsReportConfig ksrConfig = new KeyStatisticsReportConfig();
        
        ConfigurationSettings configSettings = new ConfigurationSettings();
        String targetDir = outputDirectory.getPath() + outputDirectory.separator;
        FixedWidthReportWriter writer = new FixedWidthReportWriter();
        
        //  Iterate through all report definitions
        
        Iterator reportDefIter = reportDefinitions.iterator();
        while (reportDefIter.hasNext())  {
            ReportDefinition reportDef = (ReportDefinition) reportDefIter.next();
            String reportTemplate = reportDef.getReportTemplate();
            String outputFileName = reportDef.getReportOutput();
            String reportName = reportDef.getReportName();
            if (reportTemplate == null)  {
                throw new Exception("Error: report template cannot be null");
            }
            if (outputFileName == null)  {
                throw new Exception("Error: output file name cannot be null");
            }  
            
            outputFileName = targetDir + outputFileName;
            boolean validReport = false;
            if (reportTemplate.compareToIgnoreCase(ASSUMED_MATCH_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
					i = 1;
                    configSettings.setConfig(amrConfig, reportDef);

                    
                    // create output stream and print header information
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));
                    // print header information
                    writer.writeHeader(amrConfig, reportName, reportTemplate, ps);
                    AssumedMatchReport report = repgen.execAssumedMatchReport(amrConfig);
                    while (true) {
                        while (report.hasNext()) {
                            AssumedMatchReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(amrConfig, reportRow, ps, false);
			}
			if (!report.hasMore()) {
                            break ;
			}
			report = repgen.getNextAssumedMatchRecords();
                    }
                }
                validReport = true;
            }  
            
            if (reportTemplate.compareToIgnoreCase(POTENTIAL_DUPLICATE_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
					i = 1;
                    configSettings.setConfig(pdrConfig, reportDef);

                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));
                    
                    // print header information
                    writer.writeHeader(pdrConfig, reportName, reportTemplate, ps);

                    PotentialDuplicateReport report = repgen.execPotentialDuplicateReport(pdrConfig);
                    while (true) {
                        while (report.hasNext()) {
                            PotentialDuplicateReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(pdrConfig, reportRow, ps, false);
                        }
                        if (!report.hasMore()) {
                            break ;
                        }
                        report = repgen.getNextPotDupRecords();
                    }
                }
                validReport = true;
            } 
            if (reportTemplate.compareToIgnoreCase(DEACTIVATED_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(drConfig, reportDef);

                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));
                    writer.writeHeader(drConfig, reportName, reportTemplate, ps);
                    DeactivateReport report = repgen.execDeactivateReport(drConfig);
                    while (true) {
                        while (report.hasNext()) {
                            DeactivateReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(drConfig, reportRow, ps, false);
                        }
                        if (!report.hasMore()) {
                            break ;
                        }
                        report = repgen.getNextDeactivateRecords();
                    }
                }
                validReport = true;
            }  
            if (reportTemplate.compareToIgnoreCase(UPDATE_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(urConfig, reportDef);

                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));
                    
                    // print header information
                    writer.writeHeader(urConfig, reportName, reportTemplate, ps);
                    UpdateReport report = repgen.execUpdateReport(urConfig);
                    while (true) {
                        while (report.hasNext()) {
                            UpdateReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(urConfig, reportRow, ps, false);
                        }
                        if (!report.hasMore()) {
                            break ;
                        }
                        report = repgen.getNextUpdateRecords();
                    }
                }
                validReport = true;
            }  
            if (reportTemplate.compareToIgnoreCase(MERGED_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(mrConfig, reportDef);

                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));
                    
                    // print header information
                    writer.writeHeader(mrConfig, reportName, reportTemplate, ps);
                    MergeReport report = repgen.execMergeReport(mrConfig);
                    while (true) {
                        while (report.hasNext()) {
                            MergeReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(mrConfig, reportRow, ps, true);
                        }
                        if (!report.hasMore()) {
                            break ;
			}
			report = repgen.getNextMergeRecords();
                    }
                }
                validReport = true;
            }  
            if (reportTemplate.compareToIgnoreCase(UNMERGEDREPORT_NAME) == 0)  {
                if (reportDef.getReportEnable() == true)  {
					i = 1;
                    configSettings.setConfig(umrConfig, reportDef);
                // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));

                    // print header information
                    writer.writeHeader(umrConfig, reportName, reportTemplate, ps);

                    UnmergeReport report = repgen.execUnmergeReport(umrConfig);
                    while (true) {
                        while (report.hasNext()) {
                            UnmergeReportRow reportRow = report.getNextReportRow();
                            writer.writeRow(umrConfig, reportRow, ps, true);
                        }
                        if (!report.hasMore()) {
                            break ;
			}
			report = repgen.getNextUnmergeRecords();
                    }
                }
                validReport = true;
            }  
            if (reportTemplate.compareToIgnoreCase(KEY_ACTIVITY_WEEKLY_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(ksrConfig, reportDef);
                    KeyStatisticsReport report = repgen.execWeeklyKeyStatisticsReport(ksrConfig);
                    
                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));

                    // Write report
                    KeyStatisticsReportWriter ksrWriter = new KeyStatisticsReportWriter(report);
                    ksrWriter.writeWeekly(reportName, reportTemplate, ps);
                }
                validReport = true;
            }
            if (reportTemplate.compareToIgnoreCase(KEY_ACTIVITY_MONTHLY_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(ksrConfig, reportDef);
                    KeyStatisticsReport report = repgen.execMonthlyKeyStatisticsReport(ksrConfig);
                    
                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));

                    // Write report
                    KeyStatisticsReportWriter ksrWriter = new KeyStatisticsReportWriter(report);
                    ksrWriter.writeMonthly(reportName, reportTemplate, ps);
                }
                validReport = true;
            }
            if (reportTemplate.compareToIgnoreCase(KEY_ACTIVITY_YEARLY_REPORT_TEMPLATE) == 0)  {
                if (reportDef.getReportEnable() == true)  {
                    configSettings.setConfig(ksrConfig, reportDef);
                    KeyStatisticsReport report = repgen.execYearlyKeyStatisticsReport(ksrConfig);
                    
                    // create output stream
                    PrintStream ps = new PrintStream(new FileOutputStream(outputFileName));

                    // Write report
                    KeyStatisticsReportWriter ksrWriter = new KeyStatisticsReportWriter(report);
                    ksrWriter.writeYearly(reportName, reportTemplate, ps);
                }
                validReport = true;
            }
            if (validReport == false)  {
                throw new Exception("Error: unsupported report template " + reportTemplate);
            }
        }
        return true;
    }

}
