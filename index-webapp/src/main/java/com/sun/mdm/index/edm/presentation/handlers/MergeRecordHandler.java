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
 * MergeRecordHandler.java 
 * Created on November 23, 2007, 4:50 PM
 * Author : Pratibha, Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.presentation.valueobjects.MergedRecords;
import com.sun.mdm.index.report.ReportException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.MergeReportRow;
import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportObject1;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

/** Creates a new instance of DeactivatedReportsHandler*/ 
public class MergeRecordHandler    {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.MergeRecordHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    private String reportType;
    ArrayList dataRowList1 = null;
    private ArrayList vOList = new ArrayList();
    private MergedRecords[] mergedRecordsVO = null;
    private String createStartDate = new String();
    private String createEndDate = new String();    
    private String createStartTime = new String();
    private String createEndTime = new String();  
    //private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.MergeRecordHandler");    
    /*
     *  Request Object Handle
     */  
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
      /**
     *Http session variable
     */
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
  
    /**
     * This method populates the DeactivatedReports using the Service Layer call
          * @TODO
     * @return
     * @throws com.sun.mdm.index.objects.validation.exception.ValidationException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws java.lang.Exception
     */    
     public MergedRecords[] mergeReport() throws ValidationException, EPathException, ReportException, Exception    {
         reportType = "Merge Reports";
         MergeReportConfig  mrConfig = getMergeReportSearchObject();
         MergeReport mRpt = QwsController.getReportGenerator().execMergeReport(mrConfig);
         ReportDataRow[] rdr = getMRRows(mrConfig,mRpt);
         return getMergedRecordsVO();
    }
     
    //getter method to retrieve the data rows of report records.
    private ReportDataRow[] getMRRows(MergeReportConfig  mrConfig,MergeReport  mRpt) throws Exception {
        ArrayList dataRowList = new ArrayList();
        ArrayList resultArrayList = new ArrayList();
        while (mRpt.hasNext()) {
            MergeReportRow reportRow = mRpt.getNextReportRow();
            ReportDataRow[] dataRows = writeRow(mrConfig, reportRow);
            //for (int i = 0; i < dataRows.length; i++) {
            //    dataRowList.add(dataRows[i]);
            //}
            resultArrayList.add(getOutPutValuesMap(mrConfig, reportRow,"EUID1"));
            resultArrayList.add(getOutPutValuesMap(mrConfig, reportRow,"EUID2"));
        }
        request.setAttribute("mergeReportList", resultArrayList);
   
        return dataRowList2Array(dataRowList);
       
     }
    /** write data row for dataRowList2Array */
    private ReportDataRow[] dataRowList2Array(ArrayList dataRowList) {
        int count = dataRowList.size();
        ReportDataRow[] dataRows = new ReportDataRow[count];
        int index = 0;

        for (Iterator iter = dataRowList.iterator(); iter.hasNext();) {
            dataRows[index++] = (ReportDataRow) iter.next();
        }
        return dataRows;
    }
    /** write data row for writeRow */
    private ReportDataRow[] writeRow(MultirowReportConfig1 reportConfig,MultirowReportObject1 reportRow) throws Exception {
        ReportDataRow[] dataRows = new ReportDataRow[1];
        ArrayList rptFields = new ArrayList();
        List transactionFields = reportConfig.getTransactionFields();
        ArrayList fcArrayList  = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList resultArrayList  = new ArrayList();
        
        boolean first = true;
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            int j = 0;
            MergedRecords mergedRecords = new MergedRecords();
            EnterpriseObject eo = null;
            Object obj = null;
            MasterControllerService masterControllerService = new MasterControllerService();
            
            while (i.hasNext()) {               
                String field = (String) i.next();
                String val = reportRow.getValue(field).toString();
                
                
                mLogger.info("field  "+field+"  val  "+val);
                
                if (field.equalsIgnoreCase("EUID1") ) {
                     mergedRecords.getEuid().add(val);
                     eo = masterControllerService.getEnterpriseObject(val);
                } else if (field.equalsIgnoreCase("EUID2")){
                    mergedRecords.getEuid().add(val);
                } else if (field.equalsIgnoreCase("Timestamp")){
                    mergedRecords.setMergedtime(val);                  
                }   
                rptFields.add(new ReportField(val));
                j++;
            }
            vOList.add(mergedRecords);

        }
        
      
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            ObjectNode childObj = reportRow.getObject1();
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].toString();
                String val = new String();
                Object obj = EPathAPI.getFieldValue(field, childObj);
                if (obj != null) {
                    if (obj instanceof java.util.Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat(ConfigManager.getDateFormat());
                        val = sdf.format((java.util.Date) obj);
                    } else {
                        val = obj.toString();
                    }
                }
                rptFields.add(new ReportField(val));
            }
        }
        dataRows[0] = new ReportDataRow(rptFields);
        return dataRows;
    }
    
    private HashMap getOutPutValuesMap(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow,String euidVal) throws Exception {
        List transactionFields = reportConfig.getTransactionFields();
        ArrayList fcArrayList = getResultsConfigArrayList();
        HashMap newValuesMap = new HashMap();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");

        String strVal = new String();
        //getSearchResultsArrayByReportType();
        if (transactionFields != null) {
            Iterator iter = transactionFields.iterator();
            EnterpriseObject eo = null;
            Object obj = null;
            MasterControllerService masterControllerService = new MasterControllerService();
            String epathValue = new String();
            while (iter.hasNext()) {
                String field = (String) iter.next();
                String val = reportRow.getValue(field).toString();
                if (field.equalsIgnoreCase(euidVal)) {
                    newValuesMap.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());
                    if(eo != null ) {
                    for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                            epathValue = fieldConfig.getFullFieldName();
                        } else {
                            epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                        }

                        if (fieldConfig.isUpdateable()) {
                            if (fieldConfig.getValueType() == 6) {
                                newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                            } else {
                                Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                                if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                    if (value != null) {
                                        strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                } else {
                                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                                }
                            }
                        }
                    }
                  }
                } 
            }
        }
        return newValuesMap;
    }
    public MergeReportConfig getMergeReportSearchObject() throws ValidationException, EPathException {
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         MergeReportConfig mrConfig = new MergeReportConfig();
        // One of Many validation 
        if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)){
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                //mLogger.error(errorMessage);
                mLogger.info(mLocalizer.x("MRG001: {0} ",errorMessage));
                
           }

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                String msg1 = bundle.getString("timeFrom");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,msg1 + errorMessage, errorMessage));
                //mLogger.error(errorMessage);
                 mLogger.info(mLocalizer.x("MRG002: {0} ",errorMessage));
                
            }            
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                // mLogger.error(errorMessage);
                 mLogger.info(mLocalizer.x("MRG003: {0} ",errorMessage));
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) ? " " + this.getCreateStartTime() : " 00:00:00" );
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        mrConfig.setStartDate(new Timestamp(date.getTime()));
                    }  
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    //mLogger.error(errorMessage);
                    mLogger.error(mLocalizer.x("MRG004: {0}:{1} ",errorMessage,validationException.getMessage()),validationException);
                }
            }
        }
         
       //Form Validation of End Time
       if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 String msg2 = bundle.getString("timeTo");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg2 + errorMessage, errorMessage));
                //mLogger.error(errorMessage);
                mLogger.info(mLocalizer.x("MRG005: {0} ",errorMessage));
            } 
       }    
         
        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error(errorMessage);
                mLogger.info(mLocalizer.x("MRG006: {0} ",errorMessage));
            } else {
                try {
//                    if (getCreateEndTime().trim().length() == 0) {
//                        createEndTime = "23:59:59";
//                    }
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        mrConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                     //createEndTime = "";
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    //mLogger.error(errorMessage);
                    mLogger.error(mLocalizer.x("MRG007: {0}:{1} ",errorMessage,validationException.getMessage()),validationException);
                }
            }           
        }
              
        if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0))&&
           ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))){                
               Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)? " " +this.getCreateStartTime():" 00:00:00"));
               Date todate = DateUtil.string2Date(this.getCreateEndDate()+((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " +this.getCreateEndTime():" 23:59:59"));
               long startDate = fromdate.getTime();
               long endDate = todate.getTime();
                 if(endDate < startDate){
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    //mLogger.error(errorMessage);
                    mLogger.info(mLocalizer.x("MRG008: {0} ",errorMessage));
                   }
        }   
        
      //  mrConfig.addTransactionField(MergeReport.EUID,MergeReport.EUID, 20);
        mrConfig.addTransactionField(MergeReport.EUID1,"EUID", 20);
        mrConfig.addTransactionField(MergeReport.EUID2,MergeReport.EUID2, 20);       
        mrConfig.addTransactionField(MergeReport.TIMESTAMP, MergeReport.TIMESTAMP, 20);
        mrConfig.addTransactionField(MergeReport.SYSTEM_USER, MergeReport.SYSTEM_USER, 20);
        mrConfig.addTransactionField(MergeReport.TRANSACTION_NUMBER, MergeReport.TRANSACTION_NUMBER, 20);
        mrConfig.setMaxResultSize(new Integer("20"));
//        mrConfig.addTransactionFieldVisibleLine1(MergeReport.EUID1,"EUID", 20);
//        mrConfig.addTransactionFieldVisibleLine1(MergeReport.EUID2,MergeReport.EUID2, 20);
     
        return mrConfig;
    }  

    
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
     * Return the populated Value object to the presetation layer
     * @return
     */
   public MergedRecords[] getMergedRecordsVO() {
        mergedRecordsVO = new MergedRecords[vOList.size()];
        for (int i = 0; i < vOList.size(); i++) {
            mergedRecordsVO[i] = new MergedRecords();
            mergedRecordsVO[i] = (MergedRecords)vOList.get(i);
        }
        request.setAttribute("size", new Integer(mergedRecordsVO.length));        
        request.setAttribute("tabName", "MERGED_RECORDS");               
        return mergedRecordsVO;
    }
   
    /**
     * Sets the valueobject for the Merged Reports search
     * @param mergedRecordsVO 
     */
    public void setMergedRecordsVO(MergedRecords[] mergedRecordsVO) {
        this.mergedRecordsVO = mergedRecordsVO;
    }

    public ArrayList getResultsConfigArrayList() {
        ReportHandler reportHandler = new ReportHandler();
        reportHandler.setReportType("Merged Transaction Report");        
        ArrayList fcArrayList  = reportHandler.getSearchResultsScreenConfigArray();
        return fcArrayList;
    }

    public void setResultsConfigArrayList(ArrayList resultsConfigArrayList) {
        this.resultsConfigArrayList = resultsConfigArrayList;
    }
    
}
