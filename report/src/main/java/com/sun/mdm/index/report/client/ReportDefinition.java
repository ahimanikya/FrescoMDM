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
import java.util.Vector;
import java.util.Hashtable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * @author jwu
 * @version
 */
public class ReportDefinition {
    
    private ArrayList mReportFields = null;
    private String mReportName = null;
    private String mReportOutput = null;
    private String mReportTemplate = null;
    private boolean mEnable = false;
    private Date mFromDate = null;
    private Date mToDate = null;
    private SimpleDateFormat mDateFormat = null;
    private SimpleDateFormat mDateLongFormat = null;
    private Integer mPageSize = null;//added for 88411
    private Integer mMaxResultSize = null;
    private String mStatus = null;
    private boolean mLongDateEnabled;
    
    private static String[] REPORTS 
        = {"Assumed Match", "Potential Duplicate", "Merged", "Unmerged", "Deactivated",
           "Update", "Weekly Activity", "Monthly Activity", "Yearly Activity"};
    /**
     * Creates new ReportDefinition
     */
    public ReportDefinition() {
        mReportFields = new ArrayList();
        mEnable = false;
        try {
            mDateFormat = new SimpleDateFormat("yyyyMMdd");
            mDateLongFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        
    }


    /**
     * Creates new ReportDefinition
     */
    public ReportDefinition(String reportName, String reportOutput) {
        this();
        if (reportName == null) {
            throw new RuntimeException("ReportDefinition() is passed with null report name");
        }
        mReportName = reportName.trim();
        if (reportOutput == null) {
            throw new RuntimeException("ReportDefinition() is passed with null report output file path");
        }
        mReportOutput = reportOutput.trim();
    }
    
    /**
     * Add Report Field to the ReporDefinition object
     * @param field Report Field
     * @throw ConfigurationException
     */
    public void addReportField(ReportField field) throws ConfigurationException {
        if (field == null) {
            throw new ConfigurationException("addReportField() invoked with null ReportField");
        }
        mReportFields.add(field);
    }
    
    /**
     * Get the Report Fields of the ReporDefinition object
     * @return ArrayList Report Fields
     */
    public ArrayList getReportFields() {
        return mReportFields;
    }
    
    /**
     * Setter for Report Name attribute of the ReporDefinition object
     * @param reportName Report Name
     */
    public void setReportName(String reportName) {
        mReportName = reportName.trim();
    }

    /**
     * Getter for ReportName attribute of the ReporDefinition object
     * @return Report Name of this report 
     */
    public String getReportName() {
        return mReportName;
    }

    /**
     * Setter for Report Template attribute of the ReporDefinition object
     * @param ReportTemplate Report Template 
     */
    public void setReportTemplate(String reportTemplate) {
        mReportTemplate = reportTemplate.trim();
    }

    /**
     * Getter for Report Template attribute of the ReporDefinition object
     * @return String Report Template 
     */
    public String getReportTemplate() {
        return mReportTemplate;
    }

    /**
     * Setter for Report Enable attribute of the ReporDefinition object
     * @param reportName Report name
     */
    public void setReportEnable(boolean bEnable) {
        mEnable = bEnable;
    }
    
    /**
     * Getter for Report Enable attribute of the ReporDefinition object
     * @return boolean
     */
    public boolean getReportEnable() {
        return mEnable;
    }
    
    /**
     * Setter for Report Output attribute of the ReporDefinition object
     * @param reportOutput Report output file 
     */
    public void setReportOutput(String reportOutput) {
        mReportOutput = reportOutput.trim();
    }
    
    /**
     * Getter for Report output attribute of the ReporDefinition object
     * @return String report output file
     */
    public String getReportOutput() {
        return mReportOutput;
    }

    /**
     * Setter for Report Max-result-size attribute of the ReporDefinition object
     * @param maxResultSize Max Result Size 
     */
    public void setMaxResultSize(Integer maxResultSize) {
        mMaxResultSize = maxResultSize;
    }
    
    /**
     * Getter for Report Max-result-size attribute of the ReporDefinition object
     * @return String report Max Result Size
     */
    public Integer getMaxResultSize() {
        return mMaxResultSize;
    }

    /**
     * Setter for Report Page-size attribute of the ReporDefinition object
     * @param pageSize pageSize
     */
    public void setPageSize(Integer pageSize) {
        mPageSize = pageSize;
    }
    /**
     * Getter for Report Page-size attribute of the ReporDefinition object
     * @return String report Page-size
     */
    public Integer getPageSize() {
        return mPageSize;
    }
    /**
     * Setter for Report Status attribute of the ReporDefinition object
     * @param status Status 
     */
    public void setStatus(String Status) {
        mStatus = Status;
    }
    
    /**
     * Getter for Report Status attribute of the ReporDefinition object
     * @return String report Status
     */
    public String getStatus() {
        return mStatus;
    }
    
    /**
     * Setter for Date Range attribute of the ReporDefinition object
     * @param dateRange Date Range 
     */
    public void setDateRange(String dateEnumeration) throws ConfigurationException {
        if (dateEnumeration == null) {
            throw new ConfigurationException("setDateRange(): Null date");
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date today = c.getTime();
        if ("today".equalsIgnoreCase(dateEnumeration)) {
            mFromDate = today;
            mToDate = new Date(mFromDate.getTime() + 86400000 - 1);
        } else if ("yesterday".equalsIgnoreCase(dateEnumeration)) {
            mToDate = new Date(today.getTime() - 1);
            mFromDate = new Date(today.getTime() - 86400000);
        } else {
            throw new ConfigurationException("\"" + dateEnumeration 
                + "\" is not recognized as Date Range parameter");
        }
    }
    
    /**
     * Setter for Date Range attribute of the ReporDefinition object
     * @param fromDate From Date 
     * @param toDate To Date 
     */
    public void setDateRange(String fromDate, String toDate) throws ConfigurationException {
        if (fromDate == null && toDate == null) {
            throw new ConfigurationException("setDateRange(): FromDate and ToDate are both null");
        }
        if (fromDate == null) {
            mFromDate = null;
        } else {
            try {
                if (fromDate.trim().length() == 8) {
                    mFromDate = mDateFormat.parse(fromDate);
                    mLongDateEnabled = false;                    
                } else {
                    mFromDate = mDateLongFormat.parse(fromDate);
                    mLongDateEnabled = true;
                }
            } catch (ParseException e) {
                throw new ConfigurationException("\"" + fromDate
                    + "\" is not valid date for FromDate");
            }
        }
        if (toDate == null) {
            mToDate = null;
        } else {
            try {
                if (fromDate.length() == 8) {
                    mToDate = new Date(mDateFormat.parse(toDate).getTime() + 86400000 - 1);
                } else {
                    mToDate = new Date(mDateLongFormat.parse(toDate).getTime());
                }
            } catch (ParseException e) {
                throw new ConfigurationException("\"" + toDate
                    + "\" is not valid date for ToDate");
            }
        }
    }
    
    /**
     * Getter for From Date attribute of the ReporDefinition object
     * @return String FromDate 
     */
    public Date getFromDate() {
        return mFromDate;
    }

    /**
     * Getter for To Date attribute of the ReporDefinition object
     * @return String ToDate 
     */
    public Date getToDate() {
        return mToDate;
    }

    private String fieldsToString() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < mReportFields.size(); i++) {
            ReportField fld = (ReportField) mReportFields.get(i);
            sb.append(fld.toString()).append(" ");
        }
        return sb.toString();
    }
    
    /**
     * Construct a string that consists of the attributes of the Report Definition
     * @return a string that consists of the attributes
     */
    public String toString() {
        String dateFrom;
        String dateTo;
        if (mLongDateEnabled) {
            dateFrom = "\" Dates: From=\"" + mDateLongFormat.format(mFromDate);
            dateTo = "\" To=\"" + mDateLongFormat.format(mToDate);
        } else {
            dateFrom = "\" Dates: From=\"" + mDateFormat.format(mFromDate);
            dateTo = "\" To=\"" + mDateFormat.format(mToDate);            
        }
        return "Report=\"" + mReportName + "\" Output=\"" + mReportOutput 
            + "\" Template=\"" + mReportTemplate 
            + dateFrom
            + dateTo 
            + "\" PotentialDuplicateStatus=\"" + mStatus 
            + "\" MaxResultSize=\"" + mMaxResultSize 
            + "\" PageSize=\"" + mPageSize
            + "\" Enabled=\"" + Boolean.valueOf(mEnable).toString() 
            + "\" Fields=[" + fieldsToString() + "]"; 
    }

    /**
     * Validate the Report Definition
     * @throw ConfigurationException
     */
    public void validate() throws ConfigurationException {
        if (mReportName.length() == 0) {
            throw new ConfigurationException("Report name is not specified");
        }
        if (mReportOutput.length() == 0) {
            throw new ConfigurationException("Report output is not specified");
        }
        if (mReportTemplate.length() == 0) {
            throw new ConfigurationException("Report type is not specified");
        } 
        
        if (!isSupportedReportType(mReportTemplate.trim())) {
            throw new ConfigurationException("\"" + mReportTemplate 
                + "\", is not a supported report type");
        }
        if (mReportOutput.length() == 0) {
            throw new ConfigurationException("Report Output is not specified");
        }
        if (mFromDate == null && mToDate == null) {
            throw new ConfigurationException("FromDate and ToDate are both null");
        } 
        if (mFromDate != null && mToDate != null) {
            if (mToDate.before(mFromDate)) {
                throw new ConfigurationException("Report Output is not specified");
            }
        }
    }

    private boolean isSupportedReportType(String template) {
        for (int i=0; i < REPORTS.length; i++) {
            if (template.equals(REPORTS[i]) ) {
                return true;
            }
        }
        return false;
    }
}
