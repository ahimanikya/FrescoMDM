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
 * DuplicateReportHandler.java 
 * Created on November 19, 2007, 
 * Author : Pratibha, Sridhar
 *  
 */ 
 
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.DuplicateRecords;
import com.sun.mdm.index.report.ReportException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;

import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportObject1;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReportRow;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

public class DuplicateReportHandler    {
    /* @param reportType the report type*/
    /* @param dataRowList1 */
     private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.DuplicateReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    ArrayList dataRowList1 = null;
    TransactionIterator updateIterator = null;
    PotentialDuplicateIterator pdIter = null;
    PotentialDuplicateReportConfig pdrConfig = null;
    private PotentialDuplicateReport    pdRpt = null;
    Hashtable duplicateRecordsResultsHash = new Hashtable();
    ArrayList vOList = new ArrayList();
    private Integer maxResultsSize;  
    private Integer pageSize;  
    
     /**
     * Data Object that holds the search results 
     */
    private DuplicateRecords[] duplicateRecordsVO = null;
    
    
    /**
     * Search Start Date
     */
    private String createStartDate = new String();
    /**
     * Search End Date
     */
    private String createEndDate = new String();    
    /**
     * Search Start Time
     */ 
    private String createStartTime = new String();
    /**
     * Search end Time
     */
    private String createEndTime = new String();    
     /**
     * Search DuplicateReports Function
     */ 
    private String duplicateStatus;
    /**
     * Search Maximum Reports in DuplicateReports 
     */ 
    private String reportSize;
    /*
     *  Request Object Handle
     */  
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
   
    /**
     *Http session variable
     */
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    /**
     *Resource bundle
     */
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getViewRoot().getLocale());        
        
    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
    
    private ArrayList resultArrayList = new ArrayList();
            
    public ArrayList duplicateReport() throws ValidationException, EPathException, ReportException, Exception {
        pdrConfig = getPotentialDuplicateSearchObject();
        if (pdrConfig != null) {
            pdIter = QwsController.getReportGenerator().execPotentialDuplicateReportIterator(pdrConfig);
            // Code to retrieve the data rows of report records.
            ReportDataRow[] rdr = getPDRRows();
            setDuplicateRecordsVO(new DuplicateRecords[rdr.length]);
            return resultArrayList;
        } else {
            return null;
        }
    }
   
   //getter method to retrieve the data rows of report records.
   private ReportDataRow[] getPDRRows() throws Exception {
        ArrayList dataRowList = new ArrayList();
        while (pdIter.hasNext()) {
            PotentialDuplicateReportRow reportRow = new PotentialDuplicateReportRow(pdIter.next(), pdrConfig);
            ReportDataRow[] dataRows = writeRow(pdrConfig, reportRow);
            for (int i = 0; i < dataRows.length; i++) {
                dataRowList.add(dataRows[i]);
            }
            ArrayList newArrayList = getOutPutValuesMap(pdrConfig, reportRow);
            for (int i = 0; i < newArrayList.size(); i++) {
                HashMap object = (HashMap) newArrayList.get(i);
                resultArrayList.add(object);
            }
        }            
        request.setAttribute("duplicateReportList", resultArrayList);
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
        String strVal;
        //getSearchResultsArrayByReportType();
        if (transactionFields != null) {
            Iterator iter = transactionFields.iterator();
            EnterpriseObject eo = null;
            Object obj = null;
            MasterControllerService masterControllerService = new MasterControllerService();
            String epathValue =  new String();
            HashMap newValuesMap = new HashMap();
            while (iter.hasNext()) {
                String field = (String) iter.next();
                String val = reportRow.getValue(field).toString();
                 if (field.equalsIgnoreCase("EUID1"))  {
                     newValuesMap.put("EUID",val);
                     DuplicateRecords duplicateRecords = new DuplicateRecords();
                     duplicateRecords.setEuid(val);
                     eo = masterControllerService.getEnterpriseObject(val.toString());
                
                     for (int i = 0; i < fcArrayList.size(); i++) {
                         FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                         if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                             epathValue = fieldConfig.getFullFieldName();
                         } else {
                             epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                         }

                         if (fieldConfig.getValueType() == 6) {
                             newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                         } else {
                             Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                             if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                 if (value != null) {
                                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                        if (fieldConfig.getUserCode() != null) {
                                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                        } else {
                                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        }

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString());                                      newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                 }
                             } else {
                                 newValuesMap.put(fieldConfig.getFullFieldName(), value);
                             }
                         }
                         
                     }
                }  else if (field.equalsIgnoreCase("EUID2"))  {
                    newValuesMap.put("EUID",val);
                    DuplicateRecords duplicateRecords = new DuplicateRecords();
                    duplicateRecords.setEuid(val); 
                    eo = masterControllerService.getEnterpriseObject(val.toString());

                    for (int i = 0; i < fcArrayList.size(); i++) {
                         FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                         if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                             epathValue = fieldConfig.getFullFieldName();
                         } else {
                             epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                         }

                        if (fieldConfig.getValueType() == 6) {
                            newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                        } else {
                            Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                if (value != null) {
                                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                        if (fieldConfig.getUserCode() != null) {
                                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                        } else {
                                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        }

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString());                                     newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                }
                            } else {
                                newValuesMap.put(fieldConfig.getFullFieldName(), value);
                            }
                        }
                     }
                }  
                resultArrayList.add(newValuesMap);
                
                //Populate Hash Table as backup
                //duplicateRecordsResultsHash.put(field, val);
                rptFields.add(new ReportField(val));
            }
        }
        
        request.setAttribute("duplicateReportList", resultArrayList);
        
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

    private ArrayList getOutPutValuesMap(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow) throws Exception {
        HashMap newValuesMap = new HashMap();
        List transactionFields = reportConfig.getTransactionFields();

        ArrayList fcArrayList = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");

        HashMap euid1Map = new HashMap();
        HashMap euid2Map = new HashMap();
        ArrayList dupArrayList = new ArrayList();
        String strVal;
        
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
                if (field.equalsIgnoreCase("EUID1")) {
                    newValuesMap.put("EUID", val);
                    euid1Map.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());

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
                                euid1Map.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                            } else {
                                Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                                if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                    if (value != null) {
                                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                        if (fieldConfig.getUserCode() != null) {
                                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                        } else {
                                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        }

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString());                                         newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                        euid1Map.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                } else {
                                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                                    euid1Map.put(fieldConfig.getFullFieldName(), value);
                                }
                            }
                        }
                    }
                } else if (field.equalsIgnoreCase("EUID2")) {
                    newValuesMap.put("EUID", val);
                    euid2Map.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());
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
                                euid2Map.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                            } else {
                                Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                                if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                    if (value != null) {
                                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                        if (fieldConfig.getUserCode() != null) {
                                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                        } else {
                                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        }

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString());                                         newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                        euid2Map.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                } else {
                                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                                    euid2Map.put(fieldConfig.getFullFieldName(), value);
                                }
                            }
                        }
                    }
                }
            }
        }
        dupArrayList.add(euid1Map);
        dupArrayList.add(euid2Map);
        return dupArrayList;
    }

    public PotentialDuplicateReportConfig getPotentialDuplicateSearchObject()throws ValidationException, EPathException {
         request.setAttribute("tabName", "DUPLICATE_REPORT");        
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         PotentialDuplicateReportConfig pdrConfig = new PotentialDuplicateReportConfig();

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                String msg1 = bundle.getString("timeFrom");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1 + errorMessage, errorMessage));
                //Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.info(mLocalizer.x("RPT018: {0}",errorMessage));
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
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                // Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, message, message);
                  mLogger.info(mLocalizer.x("RPT025: {0}",message));
                return null;
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) ? " " + this.getCreateStartTime() : " 00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        pdrConfig.setStartDate(new Timestamp(date.getTime()));
                    }                                    
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    //Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
                     mLogger.error(mLocalizer.x("RPT019: {0}",errorMessage),validationException);
                     return null;
                }
            }
        }
         
       //Form Validation of End Time
       if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                String msg1 = bundle.getString("timeTo");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                //Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.info(mLocalizer.x("RPT020: {0}",message));
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
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
               // Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.error(mLocalizer.x("RPT021: {0}",message));
                return null;
            } else {
                try {
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        pdrConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    //Logger.getLogger(DuplicateReportHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("RPT022: {0}",errorMessage), validationException);
                    return null;
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
                    //Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                    mLogger.info(mLocalizer.x("RPT023: {0}",errorMessage));
                    return null;
                   }
        }
         if (getDuplicateStatus() != null && getDuplicateStatus().length() > 0)    {
             pdrConfig.setStatus(getDuplicateStatus());
         }
         
         if (this.getReportSize() != null && this.getReportSize().length() > 0)    {
            String message = edmValidation.validateNumber(this.getReportSize());
            pdrConfig.setMaxResultSize(new Integer(this.getReportSize()));        
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 String msg3 = bundle.getString("ReportSize");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg3 + errorMessage, errorMessage));
                //java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                mLogger.info(mLocalizer.x("RPT024: {0}",message));
                return null;
            }
        } 
               
       // From and to date hardcoded here
        pdrConfig.addTransactionField(PotentialDuplicateReport.EUID1, "EUID1", 20);
        pdrConfig.addTransactionField(PotentialDuplicateReport.EUID2, "EUID2", 20);        
        pdrConfig.addTransactionField(PotentialDuplicateReport.ID, "ID", 20);

    
        pdrConfig.setMaxResultSize(getMaxResultsSize());
        pdrConfig.setPageSize(getPageSize());
        
        //pdrConfig.addTransactionField(PotentialDuplicateReport.SYSTEM_CODE, "SystemCode", 10);        
        //pdrConfig.addTransactionField(PotentialDuplicateReport.WEIGHT, "Weight", 20);        
        //pdrConfig.addTransactionField(PotentialDuplicateReport.REASON, "Reason", 20);        
        //pdrConfig.addTransactionField(PotentialDuplicateReport.STATUS, "Status", 20);  
       
        
            
        /* Commented for now, will be used later
        EPath[] a =  fields.toArray();        
        for (int i=0;i<a.length;i++)
            { EPath epath = fields.get(i);
            pdrConfig.addObjectField(epath.getName(), epath.getFieldTag(), 20);  
        }    
        */
        if (errorMessage != null && errorMessage.length() != 0)  {            
            throw new ValidationException(mLocalizer.t("RPT503: {0}",errorMessage));
        } else {
            return pdrConfig;
        }                                 
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
     * 
     * @return
     */
    public String getDuplicateStatus() {
        return duplicateStatus;
    }

    /**
     * set Report Frequncy 
     * @param function
     */
    public void setDuplicateStatus(String function) {
        this.duplicateStatus = function;
    }
    

     /**
     * @return Report Size
     */
    public String getReportSize() {
        return reportSize;
    }
    /**
     * Sets the Reports Size parameter for the search
     * @param reportSize 
     */
    public void setReportSize(String reportSize) {
        this.reportSize = reportSize;
    }
    /**
     * Return the populated Value object to the presetation layer
     * @return
     */
     public DuplicateRecords[] getDuplicateRecordsVO() {
        duplicateRecordsVO = new DuplicateRecords[vOList.size()];
        for (int i = 0; i < vOList.size(); i++) {
            duplicateRecordsVO[i] = new DuplicateRecords();
            duplicateRecordsVO[i] = (DuplicateRecords)vOList.get(i);
        }
        if(duplicateRecordsVO != null) {
          request.setAttribute("size", new Integer(duplicateRecordsVO.length));        
        } else {
          request.setAttribute("size", new Integer("0"));        
        }
        return duplicateRecordsVO;
    }
    /**
     * Set the DuplicateReports objects
     * @param duplicateRecordsVO
     */
    public void setDuplicateRecordsVO(DuplicateRecords[] duplicateRecordsVO) {
        this.duplicateRecordsVO = duplicateRecordsVO;
    }

    public ArrayList getResultsConfigArrayList() {            
        String REPORT_LABEL = bundle.getString("Potential_Duplicate_Report_Label");
        ReportHandler reportHandler = new ReportHandler();
        reportHandler.setReportType(REPORT_LABEL);                
        ArrayList fcArrayList  = reportHandler.getSearchResultsScreenConfigArray();
        return fcArrayList;
    }

    public void setResultsConfigArrayList(ArrayList resultsConfigArrayList) {
        this.resultsConfigArrayList = resultsConfigArrayList;
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

    
}
