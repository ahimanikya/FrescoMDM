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

import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;


/**
 *
 * @author  rtam
 */
public class ConfigurationSettings {
    
    /** Creates a new instance of ConfigurationSettings */
    public ConfigurationSettings() {
    }
    
    /**  Sets the configuration values for the assumed match report
     *
     * @param config  assumed match config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(AssumedMatchReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionField(AssumedMatchReport.SYSTEM_CODE, "SystemCode", 10);
        config.addTransactionField(AssumedMatchReport.LID, "Local Id", 40);
        config.addTransactionField(AssumedMatchReport.WEIGHT, "Weight", 10);
        config.addTransactionField(AssumedMatchReport.EUID, "EUID", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);

		//  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, label, width);
            }  else  {
                throw new Exception("Assumed Match Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
    
    /**  Sets the configuration values for the potential duplicate report
     *
     * @param config  potential duplicate config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(PotentialDuplicateReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        String status = reportDef.getStatus(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionField(PotentialDuplicateReport.SYSTEM_CODE, "SystemCode", 10);
        config.addTransactionField(PotentialDuplicateReport.WEIGHT, "Weight", 10);
        config.addTransactionFieldVisibleLine1(PotentialDuplicateReport.EUID1, "EUID", 20);
        config.addTransactionFieldVisibleLine2(PotentialDuplicateReport.EUID2, "EUID2", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setStatus(status);
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);

		//  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, label, width);
            }  else  {
                throw new Exception("Potential Duplicate Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
    
    /**  Sets the configuration values for the update report
     *
     * @param config  update config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    
    public void setConfig(UpdateReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionField(UpdateReport.SYSTEM_CODE, "SystemCode", 10);
        config.addTransactionField(UpdateReport.LID, "Local ID", 40);
        config.addTransactionField(UpdateReport.EUID, "EUID", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);
        
        //  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, label, width);
            }  else  {
                throw new Exception("Update Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
 
    /**  Sets the configuration values for the deactivate report
     *
     * @param config  deactivate config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(DeactivateReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionField(DeactivateReport.EUID, "EUID", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);
        
        //  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, width);
            }  else  {
                throw new Exception("Deactivate Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
    /**  Sets the configuration values for the merge report
     *
     * @param config  merge config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(MergeReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionFieldVisibleLine1(MergeReport.EUID1, "EUID", 20);
        config.addTransactionFieldVisibleLine2(MergeReport.EUID2, "EUID2", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);
        
        //  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, label, width);
            }  else  {
                throw new Exception("Merge Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
    /**  Sets the configuration values for the unmerge report
     *
     * @param config  unmerge config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(UnmergeReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        ArrayList reportFields = reportDef.getReportFields();
        String outputFileName = reportDef.getReportOutput();
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        Integer pageSize = reportDef.getPageSize();
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        
        config.addTransactionFieldVisibleLine1(UnmergeReport.EUID1, "EUID", 20);
        config.addTransactionFieldVisibleLine2(UnmergeReport.EUID2, "EUID2", 20);
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setMaxResultSize(maxElement);
        config.setPageSize(pageSize);
        
        //  add report fields to the report config object
        Iterator reportFieldIter = reportFields.iterator();
        while (reportFieldIter.hasNext())  {
            ReportField repField = (ReportField) reportFieldIter.next();
            String path = repField.getFieldPath();
            if (checkPathForWildcard(path) == false)  {
                String label = repField.getFieldLabel();
                int width = repField.getFieldWidth();
                config.addObjectField(path, label, width);
            }  else  {
                throw new Exception("Unmerge Report configuration: "
                        + "path cannot contain a wildcard character.\n" + path);
            }
        }
    }
    
    /**  Sets the configuration values for the key statistics report
     *
     * @param config  key stats config object
     * @param reportDef  report definition
     * @throws Exception 
     */
    public void setConfig(KeyStatisticsReportConfig config, ReportDefinition reportDef) 
            throws Exception {
                
        Date startDate = reportDef.getFromDate(); 
        Date endDate = reportDef.getToDate(); 
        Integer maxElement = reportDef.getMaxResultSize(); 
        
        config.setStartDate(startDate);
        config.setEndDate(endDate);
        config.setMaxResultSize(maxElement);
    }    
    
    /**  Checks if a path contains a wildcard character
     *
     * @param path  path to check
     * @return false if wildcard is not found, true otherwise
     * @throws Exception 
     */
    public boolean checkPathForWildcard(String path)  
        throws Exception {
        if (path == null)  {
            return false;
        }
        if (path.indexOf('*') == -1)  {
            return false;
        } 
        return true;
    }
}
