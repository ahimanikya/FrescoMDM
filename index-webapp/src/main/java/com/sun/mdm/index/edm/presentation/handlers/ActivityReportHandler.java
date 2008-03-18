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
    
/*
 * AssumeMatchReportHandler.java 
 * Created on November 27, 2007, 
 * Author : Pratibha, Sridhar
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.ActivityRecords;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;

import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.report.ReportException;
import java.rmi.RemoteException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;


/** Creates a new instance of ActivityReportHandler*/ 
public class ActivityReportHandler {
    private String reportType;
    /* @param dataRowList1 */
    ArrayList dataRowList1 = null;
    /* @param updateIterator */
    TransactionIterator updateIterator = null;
    /* @param KeyStatisticsReport */
    private KeyStatisticsReport ksRpt = null;
    /* @param KeyStatisticsReportConfig*/
    private KeyStatisticsReportConfig arConfig = null;
    Hashtable activityResultsHash = new Hashtable();
    
    ArrayList vOList = new ArrayList();
    /**
     * Data Object that holds the search results 
     */ 
    private ActivityRecords[] activityRecordsVO = null;
    /**
     * Search Start Date
     */
    private String createStartDate = null;
    /**
     * Search End Date
     */
    private String createEndDate = null;
    /**
     * Search Start Time
     */
    private String createStartTime = null;
    /**
     * Search end Time
     */
    private String createEndTime = null;
    /**
     * Search ActivityReportHandlers ViewReports
     */
    private String frequency ;
           
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    /**
     * Search ActivityReportHandlers-Weekly
     */
    private static final String REPORT_TYPE_DAILY_ACTIVITY = "Daily Activity";
    /**
     * Search ActivityReportHandlers-Weekly
     */
    private static final String REPORT_TYPE_WEEKLY_ACTIVITY = "Weekly Activity";
    /**
     * Search ActivityReportHandlers-Monthly
     */
    private static final String REPORT_TYPE_MONTHLY_ACTIVITY = "Monthly Activity";
    /**
     * Search ActivityReportHandlers-Yearly
     */
    private static final String REPORT_TYPE_YEARLY_ACTIVITY = "Yearly Activity";

    
    public ActivityRecords[] activityReport() throws ValidationException, EPathException, ReportException, PageException, RemoteException, Exception{
             reportType = getFrequency();
             request.setAttribute("tabName", "ACTIVITY_REPORT");
             if (reportType.equalsIgnoreCase(REPORT_TYPE_WEEKLY_ACTIVITY) || reportType.equalsIgnoreCase(REPORT_TYPE_MONTHLY_ACTIVITY) || reportType.equalsIgnoreCase(REPORT_TYPE_YEARLY_ACTIVITY)) {
                arConfig = getActivitySearchObject();
                if (reportType.equalsIgnoreCase(REPORT_TYPE_WEEKLY_ACTIVITY)) {
                    ksRpt = QwsController.getReportGenerator().execWeeklyKeyStatisticsReport(arConfig);
                } else if (reportType.equalsIgnoreCase(REPORT_TYPE_MONTHLY_ACTIVITY)) {
                    ksRpt = QwsController.getReportGenerator().execMonthlyKeyStatisticsReport(arConfig);
                } else if (reportType.equalsIgnoreCase(REPORT_TYPE_YEARLY_ACTIVITY)) {
                    ksRpt = QwsController.getReportGenerator().execYearlyKeyStatisticsReport(arConfig);
                }

                // Methods to fetch data
                if (reportType.equals(getREPORT_TYPE_WEEKLY_ACTIVITY())) {
                    ReportDataRow[] rdr = getWKRRows();
                    return getActivityRecordsVO();
                } else if (reportType.equals(getREPORT_TYPE_MONTHLY_ACTIVITY()) || reportType.equals(getREPORT_TYPE_YEARLY_ACTIVITY())) {
                    ReportDataRow[] rdr = getMONYRRRows();
                    return getActivityRecordsVO();
                }
            }
        return activityRecordsVO;
    }
    /*
     *  getter method to retrieve the data rows of report records.
     */
    private ReportDataRow[] getWKRRows() throws Exception {        
        ArrayList count = null;
        ReportDataRow[] dataRows = new ReportDataRow[10];
        ArrayList fields = new ArrayList();
        
        fields.add(new ReportField("ADD")); //Add Transactions
        ArrayList countArr = ksRpt.getAddCountsForWeek();
        
        if (countArr == null) return null;
        
        ActivityRecords activityRecords[] = new ActivityRecords[countArr.size()-1];
        
        //Populate The VO for the Add Records
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i] = new ActivityRecords(); //malloc objects while collecting the first info
            activityRecords[i].setAddTransactions(countArr.get(i).toString());
        }

        fields = new ArrayList();
        fields.add(new ReportField("UPDATE"));   //XXXXXXXXXXXXXXX
        countArr = ksRpt.getUpdateCountsForWeek();
        writeCountForWeek(fields, countArr);  // add value to dataRowList
        //Populate The VO for the Update Records
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setUpdateCount(countArr.get(i).toString());
        }

        countArr = ksRpt.getDeactivateCountsForWeek();
        
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setEuidDeactivateTrans(countArr.get(i).toString());
        }
        
        fields = new ArrayList();
        fields.add(new ReportField("EUID MERGE"));  //EUID Merge
        countArr = ksRpt.getEuidMergeCountsForWeek();
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setEuidMergedTrans(countArr.get(i).toString());
        }
        
        fields = new ArrayList();
        fields.add(new ReportField("EUID UNMERGE"));
        countArr = ksRpt.getEuidUnmergeCountsForWeek();
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setEuidUnmergedTrans(countArr.get(i).toString()); //Not on JSF
        }
        writeCountForWeek(fields, countArr);  // add value to dataRowList
        dataRows[4] = new ReportDataRow(fields);
        
        for (int i = 0; i < countArr.size()-1; i++) {        
            activityRecords[i].setEuidUnmergedTrans(countArr.get(i).toString()); //VO FIX
        }
        
        fields = new ArrayList();
        fields.add(new ReportField("LID MERGE"));//LID Merge
        countArr = ksRpt.getLidMergeCountsForWeek();
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setLidMergedTrans(countArr.get(i).toString());
        }
        
        fields = new ArrayList();
        fields.add(new ReportField("LID UNMERGE"));//LID UNMERGE
        countArr = ksRpt.getLidUnmergeCountsForWeek();
 
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setLidUnMergedTrans(countArr.get(i).toString());
        }

        fields = new ArrayList();
        fields.add(new ReportField("LID TRANSFER"));//????????????????????????????????
        countArr = ksRpt.getLidTransferCountsForWeek();
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setLidTransfer(countArr.get(i).toString());  //Not on JSF
        }

        fields = new ArrayList();
        fields.add(new ReportField("Daily Totals"));        
        countArr = ksRpt.getDailyTotalsForWeek();
        writeCountForWeek(fields, countArr);  // add value to dataRowList
        for (int i = 0; i < countArr.size()-1; i++) {
            activityRecords[i].setTotals(countArr.get(i).toString());
        }
        
        //Populate the Date and Day for the week of the start date
        Date date = DateUtil.string2Date(getCreateStartDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a, EEEEEEEEEE, MMMMMMMMMM dd, yyyy");                
        String strDate = simpleDateFormat.format(date);        
        int distanceFromSunday = 0;
        if (strDate.indexOf("Sun") != -1) {
            distanceFromSunday = 0;
        } else if (strDate.indexOf("Mon") != -1) {
            distanceFromSunday = 1;
        } else if (strDate.indexOf("Tue") != -1) {
            distanceFromSunday = 2;
        } else if (strDate.indexOf("Wed") != -1) {
            distanceFromSunday = 3;
        } else if (strDate.indexOf("Thu") != -1) {
            distanceFromSunday = 4;
        } else if (strDate.indexOf("Fri") != -1) {
            distanceFromSunday = 5;
        } else if (strDate.indexOf("Sat") != -1) {
            distanceFromSunday = 6;
        }
        long datelong = date.getTime();
        long displacementFromSunday = (distanceFromSunday * (24 * 60 * 60 *1000)); 
        long Sundaylong = datelong - displacementFromSunday;
        
        String sundayDMY =" ";
        Date sundayDate = new Date(Sundaylong);
        
        for (int i = 0; i < countArr.size()-1; i++) {
                Date thisDate = new Date(sundayDate.getTime() + i * ((24 * 60 * 60 * 1000)));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                sundayDMY = sdf.format(thisDate);
                activityRecords[i].setActivityDate(sundayDMY);
                simpleDateFormat = new SimpleDateFormat("HH:mm a, EEEEEEEEEE, MMMMMMMMMM dd, yyyy");
                strDate = simpleDateFormat.format(thisDate);
                if (strDate.indexOf("Sun") != -1) {
                    activityRecords[i].setDay("Sunday");
                } else if (strDate.indexOf("Mon") != -1) {
                    activityRecords[i].setDay("Monday");
                } else if (strDate.indexOf("Tue") != -1) {
                    activityRecords[i].setDay("Tuesday");
                } else if (strDate.indexOf("Wed") != -1) {
                    activityRecords[i].setDay("Wednesday");
                } else if (strDate.indexOf("Thu") != -1) {
                    activityRecords[i].setDay("Thursday");
                } else if (strDate.indexOf("Fri") != -1) {
                    activityRecords[i].setDay("Friday");
                } else if (strDate.indexOf("Sat") != -1) {
                    activityRecords[i].setDay("Saturday");
                }
        }        
        activityRecordsVO = activityRecords;
        vOList.add(activityRecords);                   
         return dataRows;       
    }
     /*
     *  getter method to retrieve the data rows of report records.
     */
    private ReportDataRow[] getMONYRRRows() throws Exception {
        ReportDataRow[] dataRows = new ReportDataRow[1];
        ArrayList fields = writeTransactionTotals();
        dataRows[0] = new ReportDataRow(fields);
        return dataRows;
    }
     /** write data row for writeCountForWeek */
     
    private void writeCountForWeek(ArrayList fields, ArrayList countArr) {        
        if (countArr != null){ 
            ActivityRecords activityRecords = new ActivityRecords();
            // Write transaction count for each day of the week
    	    for (int i = 0; i < countArr.size()-1; i++) {
                fields.add(new ReportField(((Integer) countArr.get(i)).toString()));
                activityResultsHash.put(fields, countArr);
    	    }
    	}
    }
    /** write data row for writeTransactionTotals */
     
    private ArrayList writeTransactionTotals() {
        ArrayList fields = new ArrayList();
        ArrayList countArr = ksRpt.getTransactionTotals();
        ActivityRecords activityRecords[] = new ActivityRecords[1];
        activityRecords[0] = new ActivityRecords(); // Safe with malloc
            
        for (int i = 0; i < countArr.size(); i++) {
            if (i == 0 )activityRecords[0].setAddTransactions(((Integer) countArr.get(i)).toString());            
            if (i == 1 )activityRecords[0].setEuidDeactivateTrans(((Integer) countArr.get(i)).toString());
            if (i == 2 )activityRecords[0].setEuidMergedTrans(((Integer) countArr.get(i)).toString());
            if (i == 3 )activityRecords[0].setEuidUnmergedTrans(((Integer) countArr.get(i)).toString());
            if (i == 4 )activityRecords[0].setLidMergedTrans(((Integer) countArr.get(i)).toString());
            if (i == 5 )activityRecords[0].setLidUnMergedTrans(((Integer) countArr.get(i)).toString());
            if (i == 6 )activityRecords[0].setUnresolvedPotentialDup(((Integer) countArr.get(i)).toString());
            if (i == 7 )activityRecords[0].setResolvedPotentialDup(((Integer) countArr.get(i)).toString());
        }
        activityRecordsVO = activityRecords;
        vOList.add(activityRecords);                   
        return fields;
    }

    public KeyStatisticsReportConfig getActivitySearchObject() throws ValidationException, EPathException {
        String errorMessage = null;
        EDMValidation edmValidation = new EDMValidation();
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String sStartDate = null, sEndDate = null;
        Date dStartDate = null, dEndDate = null;
        KeyStatisticsReportConfig arConfig = new KeyStatisticsReportConfig();
        // One of Many validation 
        if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)) {
            errorMessage = bundle.getString("ERROR_one_of_many");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
            Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
        }

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? errorMessage : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
                Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + (this.getCreateStartTime() != null ? " " + this.getCreateStartTime() : " 00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        arConfig.setStartDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
                }
            }
        }

        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
                Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }

        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
            } else {
                try {
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + (this.getCreateEndTime() != null ? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        arConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                }
            }
        }

          if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0))&&
           ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))){                
               Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + (this.getCreateStartTime() != null? " " +this.getCreateStartTime():"00:00:00"));
               Date todate = DateUtil.string2Date(this.getCreateEndDate()+(this.getCreateEndTime() != null? " " +this.getCreateEndTime():"23:59:59"));
               long startDate = fromdate.getTime();
               long endDate = todate.getTime();
                 if(endDate < startDate){
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "fromdate :: " + errorMessage));
                    Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                   }
        }
        
        //HardCoded for now, has to come from ConfigManager
        int maxResultSize = 20;
        // if result size is defined, set result size, otherwise use default in backend
        if (maxResultSize > 0) {
            arConfig.setMaxResultSize(new Integer(maxResultSize));
        } // Set labels, path and other UI attributes here        
        if (errorMessage != null && errorMessage.length() != 0)  {            
            throw new ValidationException(errorMessage);
        } else {
            return arConfig;
        }                                 
    }
    ////////////////////////////////////////End of Activity  Reports////////////////////////////////////////  
    /**
     * @return createStartDate
     */
    public String getCreateStartDate() {
        return createStartDate;
    }

    /**
     * @param createStartDate
     * Sets the Start Date
     */
    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }

    /**
     * @return Create End Date
     */
    public String getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Sets the End date parameter for the search
     * @param createEndDate
     */
    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    /**
     * @return create Start Date
     */
    public String getCreateStartTime() {
        return createStartTime;
    }

    /**
     * Sets the Start timeparameter for the search
     * @param createStartTime 
     */
    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    /**
     * @return Create End time
     */
    public String getCreateEndTime() {
        return createEndTime;
    }

    /**
     * Sets the End time parameter for the search
     * @param createEndTime 
     */
    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    /**
     * 
     * @return
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * set Report Frequncy 
     * @param freqency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
     /**
     * Getter the REPORT_TYPE_WEEKLY_ACTIVITY for the search
     * @param REPORT_TYPE_WEEKLY_ACTIVITY 
     */
    public static String getREPORT_TYPE_WEEKLY_ACTIVITY() {
        return REPORT_TYPE_WEEKLY_ACTIVITY;
    }
     /**
     * Getter the REPORT_TYPE_MONTHLY_ACTIVITY for the search
     * @param REPORT_TYPE_MONTHLY_ACTIVITY 
     */
    public static String getREPORT_TYPE_MONTHLY_ACTIVITY() {
        return REPORT_TYPE_MONTHLY_ACTIVITY;
    }
     /**
     * Getter the REPORT_TYPE_YEARLY_ACTIVITY for the search
     * @param REPORT_TYPE_YEARLY_ACTIVITY 
     */
    public static String getREPORT_TYPE_YEARLY_ACTIVITY() {
        return REPORT_TYPE_YEARLY_ACTIVITY;
    }
     /**
     * Return the populated Value object to the presetation layer
     * @return
     */
    public ActivityRecords[] getActivityRecordsVO() {
        /* activityRecordsVO = (ActivityRecords[])vOList.get(1);         
        for (int i = 0; i < vOList.size(); i++) {
            activityRecordsVO[i] = new ActivityRecords();
            activityRecordsVO[i] = (ActivityRecords)vOList.get(i);
        }*/
        request.setAttribute("size", new Integer(activityRecordsVO.length));        
        request.setAttribute("frequency", getFrequency());
        return activityRecordsVO;
    }
     /**
     * Sets the valueobject for the Activity Reports search
     * @param activityRecordsVO 
     */
    public void setActivityRecordsVO(ActivityRecords[] activityRecordsVO) {
        this.activityRecordsVO = activityRecordsVO;
    }
    
}
