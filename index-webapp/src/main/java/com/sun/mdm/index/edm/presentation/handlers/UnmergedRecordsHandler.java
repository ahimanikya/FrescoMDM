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
 * UnmergedRecordsHandler.java 
 * Created on November 21, 2007
 * Author : Pratibha, Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.report.ReportException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportObject1;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportRow;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.edm.presentation.valueobjects.UnmergedRecords;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.util.LogUtil;
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
import com.sun.mdm.index.edm.presentation.security.Operations;
import com.sun.mdm.index.edm.presentation.handlers.NavigationHandler;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import net.java.hulp.i18n.LocalizationSupport;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.objects.epath.EPathException;
 
public class UnmergedRecordsHandler    {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.UnmergedRecordsHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private String reportType;
    private ArrayList dataRowList1 = null;
    private TransactionIterator updateIterator = null;
    private ArrayList vOList = new ArrayList();  
    private UnmergedRecords[] unmergedRecordsVO = null;    
    private String createStartDate = new String();
    private String createEndDate = new String();    
    private String createStartTime = new String();
    private String createEndTime = new String();    
    private Integer maxResultsSize;  
    private Integer pageSize;  
    
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
    
     /**
     *Operations class used for implementing the security layer from midm-security.xml file
     */
    Operations operations = new Operations();
    
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
   
    /**
     *Resource bundle
     */
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());        
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    
    ArrayList resultArrayList = new ArrayList();
            
    /**
     * This method populates the UnmergedRecords 
     * @return UnmergedRecords[]
     * @throws com.sun.mdm.index.objects.validation.exception.ValidationException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws ValidationException, EPathException, ReportException, Exception
     */
    public ArrayList unmergeReport() throws ValidationException, EPathException, ReportException, Exception {
        reportType = "Unmerge Reports";
        UnmergeReportConfig umrConfig = getUnmergeReportSearchObject();
        
        if (umrConfig != null) {
            try{
            UnmergeReport umRpt = QwsController.getReportGenerator().execUnmergeReport(umrConfig);
            ReportDataRow[] rdr = getUMRRows(umrConfig, umRpt);
            setUnmergedRecordsVO(new UnmergedRecords[rdr.length]);
            } catch (Exception ex) {
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("UMG050: Service Layer Validation Exception has occurred"), ex);
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("UMG051: Service Layer User Exception occurred"), ex);
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("UMG052: Error  occurred"), ex);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            }

            return resultArrayList;
        } else {
            return null;
        }

    }

   //getter method to retrieve the data rows of report records.
  private ReportDataRow[] getUMRRows(UnmergeReportConfig umrConfig,UnmergeReport  umRpt) throws Exception {
        ArrayList dataRowList = new ArrayList();
        while (umRpt.hasNext()) {
            UnmergeReportRow reportRow = umRpt.getNextReportRow();
            ReportDataRow[] dataRows = writeRow(umrConfig, reportRow);
            for (int i = 0; i < dataRows.length; i++) {
                dataRowList.add(dataRows[i]);
            }
            ArrayList outputList = new ArrayList();
            outputList.add(getOutPutValuesMap(umrConfig, reportRow,"EUID1"));
            outputList.add(getOutPutValuesMap(umrConfig, reportRow,"EUID2"));
            //add list of values here
            resultArrayList.add(outputList);
            
            
            
        }
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
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
        ArrayList resultArrayList  = new ArrayList();
        boolean first = true;
        if (transactionFields != null) {
            int j = 0;
            EnterpriseObject eo = null;
            Object obj = null;
            MasterControllerService masterControllerService = new MasterControllerService();
            
            UnmergedRecords unMergedRecords = new UnmergedRecords();
            Iterator i = transactionFields.iterator();
            String epathValue =  new String();
            HashMap newValuesMap = new HashMap();
            while (i.hasNext()) {
                String field = (String) i.next();
                String val = reportRow.getValue(field).toString();
                 
                 if (field.equalsIgnoreCase("EUID1"))  {
                    newValuesMap.put("EUID",val);
                    unMergedRecords.getEuid().add(val);
                }else if (field.equalsIgnoreCase("EUID2")){
                    newValuesMap.put("EUID",val);
                    unMergedRecords.getEuid().add(val);
                } 
                 else if (field.equalsIgnoreCase("Timestamp")){
                     newValuesMap.put("EUID",val);
                    unMergedRecords.setUnmergedDate(val);                  
                }                
                j++;
                rptFields.add(new ReportField(val));
                resultArrayList.add(newValuesMap);
            }
            vOList.add(unMergedRecords);

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
        HashMap newValuesMap = new HashMap();
        List transactionFields = reportConfig.getTransactionFields();

        ArrayList fcArrayList = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
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
                         newValuesMap = populateHashMapValues(fieldConfig,  newValuesMap,  eo);  
                    }
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
                if (EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())!=null && !operations.isField_VIP() && fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly

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
            if (value != null && !operations.isField_VIP()  && fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly                                  

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
  public UnmergeReportConfig getUnmergeReportSearchObject() throws ValidationException, EPathException {
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         UnmergeReportConfig umrConfig = new UnmergeReportConfig();

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                String msg = bundle.getString("timeFrom");
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("UMG002: {0}",errorMessage));
                return null;
            }            
            //if only time fields are entered validate for the date fields 
            if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_from");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("UMG201: {0} ",errorMessage));
                return null;
            }
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("UMG003: {0}",errorMessage));
                return null;
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)? " " + this.getCreateStartTime() : " 00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        umrConfig.setStartDate(new Timestamp(date.getTime()));
                    }        
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("UMG004: Failed to unmerge the search object reports  :{0}",validationException.getMessage()));
                    return null;
                }
            }
        }
         
       //Form Validation of End Time
       if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                String msg1 = bundle.getString("timeTo");
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1 + errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("UMG005: {0}",errorMessage));
                return null;
            } 
            //if only time fields are entered validate for the date fields 
            if ((this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_to");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("UMG202: {0} ",errorMessage));
                return null;
            }
       }    
         
        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                mLogger.info(mLocalizer.x("UMG006: {0}",errorMessage));
                return null;
            } else {
                try {
                     if (getCreateEndTime().trim().length() == 0) {
                        createEndTime = "23:59:59";
                    }
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        umrConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                    createEndTime="";
                } catch (ValidationException validationException) {
                     mLogger.error(mLocalizer.x("UMG007: Failed to unmerge the search object reports  :{0}",validationException.getMessage()));
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
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
                     mLogger.info(mLocalizer.x("UMG008: {0}",errorMessage));
                    return null;
                   }
        }  
   
       umrConfig.addTransactionField(UnmergeReport.EUID1,UnmergeReport.EUID1, 20);
       umrConfig.addTransactionField(UnmergeReport.EUID2,UnmergeReport.EUID2, 20);       
       umrConfig.addTransactionField(UnmergeReport.TIMESTAMP, UnmergeReport.TIMESTAMP, 20);
       umrConfig.addTransactionField(UnmergeReport.SYSTEM_USER, UnmergeReport.SYSTEM_USER, 20);
       umrConfig.addTransactionField(UnmergeReport.TRANSACTION_NUMBER, UnmergeReport.TRANSACTION_NUMBER, 20);


       //set the max results size and page size here as per midm.xml
       umrConfig.setMaxResultSize(getMaxResultsSize());
       umrConfig.setPageSize(getPageSize());
       return umrConfig;
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
      public UnmergedRecords[] getUnmergedRecordsVO() {
        unmergedRecordsVO = new UnmergedRecords[vOList.size()];

        for (int i = 0; i < vOList.size(); i++) {
            unmergedRecordsVO[i] = new UnmergedRecords();
            unmergedRecordsVO[i] = (UnmergedRecords)vOList.get(i);
        }
        if(unmergedRecordsVO != null) {
          request.setAttribute("size", new Integer(unmergedRecordsVO.length));        
        } else {
            request.setAttribute("size", new Integer("0"));        
        }
        request.setAttribute("tabName", "UNMERGED_RECORDS");            
        return unmergedRecordsVO;          
      }

    /**
     * Sets the valueobject for the Unmerged Reports search
     * @param unmergedRecordsVO 
     */
    public void setUnmergedRecordsVO(UnmergedRecords[] unmergedRecordsVO) {
        this.unmergedRecordsVO = unmergedRecordsVO;
    }

    public ArrayList getResultsConfigArrayList() {
        String REPORT_LABEL = bundle.getString("Unmerged_Transaction_Report_Label");
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
