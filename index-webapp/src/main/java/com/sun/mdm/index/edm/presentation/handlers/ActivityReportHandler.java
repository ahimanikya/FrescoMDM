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
//import java.util.logging.Logger;
import java.util.ResourceBundle;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import java.util.HashMap;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import net.java.hulp.i18n.LocalizationSupport;

/** Creates a new instance of ActivityReportHandler*/ 
public class ActivityReportHandler {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ActivityReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
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
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    private  String REPORT_TYPE_DAILY_ACTIVITY = bundle.getString("DAILY_ACTIVITY") ; // Daily Activity";
    /**
     * Search ActivityReportHandlers-Weekly
     */
    private  String REPORT_TYPE_WEEKLY_ACTIVITY =bundle.getString("WEEKLY_ACTIVITY") ; // "Weekly Activity";
    /**
     * Search ActivityReportHandlers-Monthly
     */
    private String REPORT_TYPE_MONTHLY_ACTIVITY = bundle.getString("MONTHLY_ACTIVITY") ; // "Monthly Activity";
    /**
     * Search ActivityReportHandlers-Yearly
     */
    private String REPORT_TYPE_YEARLY_ACTIVITY = bundle.getString("YEARLY_ACTIVITY") ; // "Yearly Activity";
    /**
     * Maximum report items to be rendered 
     */
    private Integer maxResultsSize;  
    /**
     * Page Size for the paging. 
     */
    private Integer pageSize;  

    
    public ArrayList activityReport() throws ValidationException, EPathException, ReportException, PageException, RemoteException, Exception {
        ArrayList finalOutputList = new ArrayList();
        reportType = getFrequency();
        //request.setAttribute("tabName", "ACTIVITY_REPORT");
        if (reportType.equalsIgnoreCase(getREPORT_TYPE_WEEKLY_ACTIVITY()) ||
            reportType.equalsIgnoreCase(getREPORT_TYPE_MONTHLY_ACTIVITY()) ||
            reportType.equalsIgnoreCase(getREPORT_TYPE_YEARLY_ACTIVITY())) {
            arConfig = getActivitySearchObject();
			if (arConfig == null)  {
				return null;  // Form Validation occured and the messages are accumulated in Faces context
			}
            if (reportType.equalsIgnoreCase(getREPORT_TYPE_WEEKLY_ACTIVITY())) {
                ksRpt = QwsController.getReportGenerator().execWeeklyKeyStatisticsReport(arConfig);
            } else if (reportType.equalsIgnoreCase(getREPORT_TYPE_MONTHLY_ACTIVITY())) {
                ksRpt = QwsController.getReportGenerator().execMonthlyKeyStatisticsReport(arConfig);
            } else if (reportType.equalsIgnoreCase(getREPORT_TYPE_YEARLY_ACTIVITY())) {
                ksRpt = QwsController.getReportGenerator().execYearlyKeyStatisticsReport(arConfig);
            }
            // Methods to fetch data
            if (reportType.equals(getREPORT_TYPE_WEEKLY_ACTIVITY())) {
                ReportDataRow[] rdr = getWKRRows();
                finalOutputList = getActivityRecordsVO();
            } else if (reportType.equals(getREPORT_TYPE_MONTHLY_ACTIVITY()) || reportType.equals(getREPORT_TYPE_YEARLY_ACTIVITY())) {
                ReportDataRow[] rdr = getMONYRRRows();
                finalOutputList = getActivityRecordsVO();
            }
        }
        return finalOutputList;
    }
    /*
     *  getter method to retrieve the data rows of report records.
     */
    private ReportDataRow[] getWKRRows() throws Exception {        
        ArrayList outPutValues =  new ArrayList();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a, EEEEEEEEEE, MMMMMMMMMM dd, yyyy", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String strDate = simpleDateFormat.format(date);
        int distanceFromSunday = 0;
        if (strDate.indexOf(bundle.getString("DAY00")) != -1) {
            distanceFromSunday = 0;
        } else if (strDate.indexOf(bundle.getString("DAY01")) != -1) {
            distanceFromSunday = 1;
        } else if (strDate.indexOf(bundle.getString("DAY02")) != -1) {
            distanceFromSunday = 2;
        } else if (strDate.indexOf(bundle.getString("DAY03")) != -1) {
            distanceFromSunday = 3;
        } else if (strDate.indexOf(bundle.getString("DAY04")) != -1) {
            distanceFromSunday = 4;
        } else if (strDate.indexOf(bundle.getString("DAY05")) != -1) {
            distanceFromSunday = 5;
        } else if (strDate.indexOf(bundle.getString("DAY06")) != -1) {
            distanceFromSunday = 6;
        }
        long datelong = date.getTime();
        long displacementFromSunday = (distanceFromSunday * (24 * 60 * 60 * 1000));
        long Sundaylong = datelong - displacementFromSunday;

        String sundayDMY = " ";
        Date sundayDate = new Date(Sundaylong);

        for (int i = 0; i < countArr.size() - 1; i++) {
            Date thisDate = new Date(sundayDate.getTime() + i * ((24 * 60 * 60 * 1000)));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sundayDMY = sdf.format(thisDate);
            activityRecords[i].setActivityDate(sundayDMY);
            simpleDateFormat = new SimpleDateFormat("HH:mm a, EEEEEEEEEE, MMMMMMMMMM dd, yyyy");
            strDate = simpleDateFormat.format(thisDate);
            if (strDate.indexOf(bundle.getString("DAY00")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY00"));
            } else if (strDate.indexOf(bundle.getString("DAY01")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY01"));
            } else if (strDate.indexOf(bundle.getString("DAY02")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY02"));
            } else if (strDate.indexOf(bundle.getString("DAY03")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY03"));
            } else if (strDate.indexOf(bundle.getString("DAY04")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY04"));
            } else if (strDate.indexOf(bundle.getString("DAY05")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY05"));
            } else if (strDate.indexOf(bundle.getString("DAY06")) != -1) {
                activityRecords[i].setDay(bundle.getString("DAY06"));
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
        // bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        KeyStatisticsReportConfig arConfig = new KeyStatisticsReportConfig();
    
        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? errorMessage : message);
                String em = bundle.getString("timeFrom");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, em + errorMessage, errorMessage));
                //Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("RPT002: {0}", errorMessage));
                return null;            
            }
            //if only time fields are entered validate for the date fields 
            if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_from");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("RPT201: {0} ",errorMessage));
                return null;
            }
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("RPT003: {0}", message));
                return null;
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
                    //Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
                    mLogger.error(mLocalizer.x("RPT004: {0}", errorMessage), validationException);
                    return null;
                }
            }
        }
        //From Date is required for the Activity report.
        if (this.getCreateStartDate() == null || (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() ==0 )) {
           String message = bundle.getString("patdetails_search_date1") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
           return null;
        }
        
        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                String msg = bundle.getString("timeTo");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + errorMessage, errorMessage));
                //Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("RPT005: {0}", message));
                return null;
            }

            //if only time fields are entered validate for the date fields 
            if ((this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_to");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("RPT202: {0} ",errorMessage));
                return null;
            }
        }

        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("RPT006: {0}", message));
                return null;
            } else {
                try {
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + (this.getCreateEndTime() != null ? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        arConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("RPT007: {0}", validationException.toString()), validationException);
                    return null;
                }
            }
        }

        if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0)) &&
                ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))) {
            Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + (this.getCreateStartTime() != null ? " " + this.getCreateStartTime() : " 00:00:00"));
            Date todate = DateUtil.string2Date(this.getCreateEndDate() + (this.getCreateEndTime() != null ? " " + this.getCreateEndTime() : " 23:59:59"));
            long startDate = fromdate.getTime();
            long endDate = todate.getTime();
            if (endDate < startDate) {
                errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("RPT008: {0}", errorMessage));
                return null;
            }
        }
       //set the max results size and page size here as per midm.xml
       arConfig.setMaxResultSize(getMaxResultsSize());
       arConfig.setPageSize(getPageSize());

        if (errorMessage != null && errorMessage.length() != 0) {
            throw new ValidationException(mLocalizer.t("RPT501: Encountered the validationException:{0}", errorMessage));
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
    public  String getREPORT_TYPE_WEEKLY_ACTIVITY() {
        return REPORT_TYPE_WEEKLY_ACTIVITY;
    }
     /**
     * Getter the REPORT_TYPE_MONTHLY_ACTIVITY for the search
     * @param REPORT_TYPE_MONTHLY_ACTIVITY 
     */
    public  String getREPORT_TYPE_MONTHLY_ACTIVITY() {
        return REPORT_TYPE_MONTHLY_ACTIVITY;
    }
     /**
     * Getter the REPORT_TYPE_YEARLY_ACTIVITY for the search
     * @param REPORT_TYPE_YEARLY_ACTIVITY 
     */
    public  String getREPORT_TYPE_YEARLY_ACTIVITY() {
        return REPORT_TYPE_YEARLY_ACTIVITY;
    }
     /**
     * Return the populated Value object to the presetation layer
     * @return
     */
    public ArrayList getActivityRecordsVO() {
        ArrayList outputList = new ArrayList();
        ArrayList keyList = new ArrayList();
        if (getFrequency() != null && "Weekly Activity".equalsIgnoreCase(getFrequency())) {
            keyList.add("ActivityDay");
            keyList.add("ActivityDate");
        }
        keyList.add("Add");
        keyList.add("EUIDDeactivate");
        keyList.add("EUIDMerge");
        keyList.add("EUIDUnmerge");
        keyList.add("LIDMerge");
        keyList.add("LIDUnMerge");
        keyList.add("UnresolvedDuplicate");
        keyList.add("ResolvedDuplicate");

        ArrayList labelList = new ArrayList();
        if (getFrequency() != null && "Weekly Activity".equalsIgnoreCase(getFrequency())) {
            labelList.add("Activity Day");
            labelList.add("Activity Date");
        }
        labelList.add("Add");
        labelList.add("EUID Deactivate");
        labelList.add("EUID Merge");
        labelList.add("EUID Unmerge");
        labelList.add("LID Merge");
        labelList.add("LID UnMerge");
        labelList.add("Unresolved Duplicate");
        labelList.add("Resolved Duplicate");

        for (int i = 0; i < vOList.size(); i++) {
            ActivityRecords activityRecords[] = (ActivityRecords[]) vOList.get(i);
            for (int j = 0; j < activityRecords.length; j++) {
                HashMap values = new HashMap();
                values.put("ActivityDay", activityRecords[j].getDay());
                values.put("ActivityDate", activityRecords[j].getActivityDate());
                values.put("Add", activityRecords[j].getAddTransactions());
                values.put("EUIDDeactivate", activityRecords[j].getEuidDeactivateTrans());
                values.put("EUIDMerge", activityRecords[j].getEuidMergedTrans());
                values.put("EUIDUnmerge", activityRecords[j].getEuidUnmergedTrans());
                values.put("LIDMerge", activityRecords[j].getLidMergedTrans());
                values.put("LIDUnMerge", activityRecords[j].getLidUnMergedTrans());
                values.put("UnresolvedDuplicate", activityRecords[j].getUnresolvedPotentialDup());
                values.put("ResolvedDuplicate", activityRecords[j].getResolvedPotentialDup());
                outputList.add(values);

            }
        }
        request.setAttribute("keys", keyList);        
        request.setAttribute("labels",labelList);        
        request.setAttribute("activityOutputList",outputList);        
        if(activityRecordsVO  != null) {
           request.setAttribute("size", new Integer(activityRecordsVO.length));        
        } else {
           request.setAttribute("size", new Integer(activityRecordsVO.length));                    
        }
        request.setAttribute("frequency", getFrequency());
        return outputList;
    }
     /**
     * Sets the valueobject for the Activity Reports search
     * @param activityRecordsVO 
     */
    public void setActivityRecordsVO(ActivityRecords[] activityRecordsVO) {
        this.activityRecordsVO = activityRecordsVO;
    }

    public Integer getMaxResultsSize() {
        return maxResultsSize;
    }

    public void setMaxResultsSize(Integer maxResultsSize) {
        this.maxResultsSize = maxResultsSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public String getLocaleMonthName() {
        String monthNam = "";
        try {
            if (getCreateStartDate() != null) {
                Date date = DateUtil.string2Date(getCreateStartDate());
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int monthNum = c.get(Calendar.MONTH)+1;
                switch (monthNum) {
                    case 1:
                       monthNam = bundle.getString("MONTH01");
                        break;
                    case 2:
                        monthNam = bundle.getString("MONTH02");
                        break;
                    case 3:
                        monthNam = bundle.getString("MONTH03");
                        break;
                    case 4:
                        monthNam = bundle.getString("MONTH04");
                        break;
                    case 5:
                       monthNam =  bundle.getString("MONTH05");
                        break;
                    case 6:
                        monthNam = bundle.getString("MONTH06");
                        break;
                    case 7:
                        monthNam = bundle.getString("MONTH07");
                        break;
                    case 8:
                       monthNam = bundle.getString("MONTH08");
                        break;
                    case 9:
                        monthNam = bundle.getString("MONTH09");
                        break;
                    case 10:
                        monthNam = bundle.getString("MONTH10");
                        break;
                    case 11:
                        monthNam = bundle.getString("MONTH11");
                        break;
                    case 12:
                        monthNam = bundle.getString("MONTH12");
                        break;
                }
            }
        } catch (ValidationException ex) {
            String errorMessage = bundle.getString("ERROR_start_date");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            mLogger.error(mLocalizer.x("RPT060: {0}", errorMessage), ex);
            return null;
        }
        return monthNam;
    }

    public int getYearValue() {
        int yearNum = 0;
        if (getCreateStartDate() != null) {
            try {

                Date date = DateUtil.string2Date(getCreateStartDate());
                java.util.Calendar c = Calendar.getInstance();
                c.setTime(date);
                yearNum = c.get(c.YEAR);

            } catch (ValidationException ex) {
                String errorMessage = bundle.getString("ERROR_start_date");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.error(mLocalizer.x("RPT061: {0}", errorMessage), ex);
                return 0;
            }
        }
        return yearNum;
    }


    
}
