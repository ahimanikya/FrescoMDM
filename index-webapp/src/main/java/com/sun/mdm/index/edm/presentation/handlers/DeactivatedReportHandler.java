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
 * DeactivatedReportHandler.java 
 * Created on November 23, 2007, 
 * Author : Pratibha, Sridhar
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.DeactivatedRecords;
import com.sun.mdm.index.report.ReportException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;

import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.DeactivateReportRow;
import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportObject1;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathException;

/**
 * @author Sridhar Narsingh
 * @exception ValidationException when entry is not valid.
 * This is a handler called by the JSF 
 */
/** Creates a new instance of DeactivatedReport*/ 
public class DeactivatedReportHandler    {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.DeactivatedReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    // dataRowList1 
    ArrayList dataRowList1 = null;
    DeactivateReportConfig drConfig = null;
    DeactivateReport dr = null;
    Hashtable duplicateRecordsResultsHash = new Hashtable();
    /**
     * List of VO Objects  
     */ 
    ArrayList vOList = new ArrayList();
    /**
     * Data Object that holds the search results 
     */ 
    private DeactivatedRecords[] deactivatedRecordsVO = null;
    
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

    //resource bundle definitin
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
  
    private ArrayList resultArrayList = new ArrayList();

    private Integer maxResultsSize;  
    private Integer pageSize;  
    
    /**
     * @return the value object for display
     * @throws com.sun.mdm.index.objects.validation.exception.ValidationException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.report.ReportException 
     * @throws java.lang.Exception
     */
    public ArrayList deactivateReport() throws ValidationException, EPathException, ReportException, Exception {
        drConfig = getDeactivateReportSearchObject();
        if (drConfig != null) {
            dr = QwsController.getReportGenerator().execDeactivateReport(drConfig);
            // Code to retrieve the data rows of report records.
            ReportDataRow[] rdr = getDRRows();
            //return getDeactivatedRecordsVO();
            return resultArrayList;
        } else {
            return null;
        }
    }

    //method to retrieve the data rows of report records.
    private ReportDataRow[] getDRRows() throws Exception {
        ArrayList dataRowList = new ArrayList();
        while (dr.hasNext()) {
            DeactivateReportRow reportRow = dr.getNextReportRow();
            ReportDataRow[] dataRows = writeRow(drConfig, reportRow);
            for (int i = 0; i < dataRows.length; i++) {
                dataRowList.add(dataRows[i]);
            }
            resultArrayList.add(getOutPutValuesMap(drConfig, reportRow));
        }
        request.setAttribute("deactivatedReportList", resultArrayList);
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
        String strVal = new String();
       
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
                     DeactivatedRecords deactivatedRecords = new DeactivatedRecords();
                     deactivatedRecords.setEuid(val);
                     eo = masterControllerService.getEnterpriseObject(val.toString());
                
                     for (int i = 0; i < fcArrayList.size(); i++) {
                         FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                         if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                             epathValue = fieldConfig.getFullFieldName();
                         } else {
                             epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                         }

       
                             if (fieldConfig.getValueType() == 6 ) {
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

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                                        newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                } else {
                                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                                }
                                 
                             }
                         
                     }
                }  else if (field.equalsIgnoreCase("EUID2"))  {
                    newValuesMap.put("EUID",val);
                    DeactivatedRecords deactivatedRecords = new DeactivatedRecords();
                    deactivatedRecords.setEuid(val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());

                    for (int i = 0; i < fcArrayList.size(); i++) {
                         FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                         if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                             epathValue = fieldConfig.getFullFieldName();
                         } else {
                             epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                         }

                             if (fieldConfig.getValueType() == 6 ) {
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

                                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                                        newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                } else {
                                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                                }
                                 
                             }
                     }
                }  
                resultArrayList.add(newValuesMap);
            }
        }
        
        request.setAttribute("deactivatedReportList", resultArrayList);
        
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            ObjectNode childObj = reportRow.getObject1();
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].toString();
                String val = new String(); //16 = 39.04 
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


    private HashMap getOutPutValuesMap(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow) throws Exception {
        HashMap newValuesMap = new HashMap();
        List transactionFields = reportConfig.getTransactionFields();

        ArrayList fcArrayList = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");

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
                if (field.equalsIgnoreCase("EUID")) {
                    newValuesMap.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());

                    for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        newValuesMap = populateHashMapValues(fieldConfig,  newValuesMap,  eo);  
                    }
                } 
            }
        }
        return newValuesMap;
    }
    
      private HashMap populateHashMapValues(FieldConfig fieldConfig, HashMap newValuesMap, EnterpriseObject eo) throws ObjectException, EPathException {
        String epathValue = new String();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
        if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
            epathValue = fieldConfig.getFullFieldName();
        } else {
            epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
        }
        String strVal;
        if (fieldConfig.getValueType() == 6) {

            if (eo != null) {
                newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                //euid1Map.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                if (fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly

                    newValuesMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                } else {
                    newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                }
            } else {
                newValuesMap.put(fieldConfig.getFullFieldName(), null);
            }
        } else {
            Object value = null;
            if (eo != null) {
                value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
            }
            if (fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly                                  

                newValuesMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));

            } else {
                if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                    if (value != null) {
                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                        if (fieldConfig.getUserCode() != null) {
                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                        } else {
                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                        }

                        // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString());                                      
                        newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                    }
                } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                    if (value != null) {
                        //Mask the value as per the masking 
                        value = fieldConfig.mask(value.toString());
                        newValuesMap.put(fieldConfig.getFullFieldName(), value);
                    }
                } else {
                    newValuesMap.put(fieldConfig.getFullFieldName(), value);
                }
            }

        }

        return newValuesMap;
    }
    public DeactivateReportConfig getDeactivateReportSearchObject() throws ValidationException, EPathException {
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         DeactivateReportConfig drc = new DeactivateReportConfig();
                  
        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                String msg2 = bundle.getString("timeFrom"); 
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("RPT010: {0}",errorMessage));
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
                 mLogger.info(mLocalizer.x("RPT011: {0}",errorMessage));
                return null;
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) ? " " + this.getCreateStartTime() : " 00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        drc.setStartDate(new Timestamp(date.getTime()));
                        
                    }   
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("RPT012: {0}",errorMessage),validationException);
                    return null;
                }
            }
        }
         
       //Form Validation of End Time
       if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("RPT013: {0}",errorMessage));
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                //Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("RPT014: {0}",errorMessage));
                return null;
            } else {
                try {
                    if (getCreateEndTime().trim().length() == 0) {
                        createEndTime = "23:59:59";
                    }
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 11:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        drc.setEndDate(new Timestamp(date.getTime()));
                    }
                    createEndTime = "";
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("RPT015: {0}",errorMessage),validationException);
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
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                    mLogger.info(mLocalizer.x("RPT016: {0}",errorMessage));
                    return null;
                   }
        }
          
        //Mandatory fields
        //Variable Name, Name, Width(Size) 
        drc.addTransactionField(DeactivateReport.EUID, "EUID", 20); 
        drc.addTransactionField(DeactivateReport.SYSTEM_USER, "SystemUser", 20);
        drc.addTransactionField(DeactivateReport.TRANSACTION_NUMBER, "TransactionNumber", 20);
        drc.addTransactionField(DeactivateReport.TIMESTAMP, "Timestamp", 20);        

        //set the max results size and page size here as per midm.xml
        drc.setMaxResultSize(getMaxResultsSize());
        drc.setPageSize(getPageSize());
        
        if (errorMessage != null && errorMessage.length() != 0)  {            
            throw new ValidationException(mLocalizer.t("RPT502: {0}",errorMessage));
        } else {
           return drc;    
        }                                 
        
    }
   ///////////////////////////////////////End of Deactivated  Reports//////////////////////////////////////// 
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
    public DeactivatedRecords[] getDeactivatedRecordsVO() {
        deactivatedRecordsVO = new DeactivatedRecords[vOList.size()];
        for (int i = 0; i < vOList.size(); i++) {
            deactivatedRecordsVO[i] = new DeactivatedRecords();
            deactivatedRecordsVO[i] = (DeactivatedRecords)vOList.get(i);
        }
        if(deactivatedRecordsVO != null) {
          request.setAttribute("size", new Integer(deactivatedRecordsVO.length));        
        } else {
          request.setAttribute("size", new Integer("0"));          
        }
        return deactivatedRecordsVO;
    }
    
     /**
     * Sets the valueobject for the Deactivated Reports search
     * @param deactivatedRecordsVO 
     */
    public void setDeactivatedRecordsVO(DeactivatedRecords[] deactivatedRecordsVO) {
        this.deactivatedRecordsVO = deactivatedRecordsVO;
    }
     public ArrayList getResultsConfigArrayList() {         
        ReportHandler reportHandler = new ReportHandler();
        String REPORT_LABEL = bundle.getString("Deactivated_Record_Report_Label");
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
