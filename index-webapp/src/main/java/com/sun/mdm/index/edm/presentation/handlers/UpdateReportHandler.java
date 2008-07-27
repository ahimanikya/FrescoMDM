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
 * UpdateReportHandler.java 
 * Created on November 19, 2007
 * Author : Pratibha, Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.UpdateRecords;
import com.sun.mdm.index.objects.exception.ObjectException;
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
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.UpdateReportRow;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;

import com.sun.mdm.index.report.UpdateReport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.presentation.handlers.NavigationHandler;
import com.sun.mdm.index.edm.presentation.security.Operations;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import net.java.hulp.i18n.LocalizationSupport;
/** Creates a new instance of DeactivatedReportsHandler*/ 
public class UpdateReportHandler    { 
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.UpdateReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    // dataRowList1
    ArrayList dataRowList1 = null;
    
    UpdateReportConfig urConfig = null;
  
    TransactionIterator updateIterator = null;
    
    Hashtable updateRecordsResultsHash = new Hashtable();
    /**
     * List of VO Objects  
     */ 
    ArrayList vOList = new ArrayList();

    /**
     * Data Object that holds the search results 
     */ 
    private UpdateRecords[] updateRecordsVO = null;
    
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
     *Resource bundle
     */
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());        
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    /**
     *Operations class used for implementing the security layer from midm-security.xml file
     */
    Operations operations = new Operations();

    private Integer maxResultsSize;  
    private Integer pageSize;  
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
    
    private ArrayList resultArrayList = new ArrayList();
    
    /**
     * @return the value object for display
     * @throws com.sun.mdm.index.objects.validation.exception.ValidationException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.report.ReportException 
     * @throws java.lang.Exception
     */
   public ArrayList updateReport() throws ValidationException, EPathException, ReportException, Exception    {
       urConfig = getUpdateSearchObject();

       if (urConfig != null) {
                   
           updateIterator = QwsController.getReportGenerator().execUpdateReportIterator(urConfig);
           ReportDataRow[] rdr = getURRows();
          
           return resultArrayList;
       } else {
           return null;
       }
    }
   
   //getter method to retrieve the data rows of report records.
   private ReportDataRow[] getURRows() throws Exception {
        
        ArrayList dataRowList = new ArrayList();
        
        String prevTimestamp = null;
        int index = 0;
        ArrayList summaryList = new ArrayList();
    //    updateIterator.sortBy("Timestamp",false);
        if(updateIterator != null ) {
        while (updateIterator.hasNext()) {
            UpdateReportRow  reportRow = new UpdateReportRow (updateIterator.next(), urConfig);
           
            
             SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
             
            String currentTime =simpleDateFormatFields.format(reportRow.getTimestamp()); 
            
           
            if (index == 0 ) { 
               // String prevTime = simpleDateFormatFields.format((Object)prevTimestamp);
                prevTimestamp = currentTime;
            }
            if (!prevTimestamp.equalsIgnoreCase(currentTime) || index +1 == updateIterator.count())   {
                vOList.add(summaryList);
                summaryList = new ArrayList();
            }
            summaryList.add(reportRow);
            /*ReportDataRow[] dataRows = writeRow(amrConfig, reportRow);
            for (int i = 0; i < dataRows.length; i++) {
                dataRowList.add(dataRows[i]);
            }*/
            prevTimestamp = currentTime;
            index++;
            ArrayList outputList = new ArrayList();
            outputList.add(getOutPutValuesMap(urConfig, reportRow));
            resultArrayList.add(outputList);
        
        }
        }
        
        //populateVO();
        //request.setAttribute("updateReportList", resultArrayList);
        return dataRowList2Array(dataRowList);
     }
   
   private void populateVO() throws Exception    {
       updateRecordsVO = new UpdateRecords[vOList.size()];
       
       for (int i=0; i < vOList.size();i++)   {
         ArrayList groupedList = (ArrayList)vOList.get(i);
         updateRecordsVO[i] = new UpdateRecords();
         for (int j=0;j< groupedList.size();j++)   {
             MultirowReportObject1 reportRow = (MultirowReportObject1)groupedList.get(j);
             populateRow(urConfig,reportRow,j,i);
         }
       }
   }
   
   private void populateRow(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow, int groupIndex, int voIndex) throws Exception {
        List transactionFields = reportConfig.getTransactionFields();
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            EnterpriseObject eo = null;
            Object obj = null;
            MasterControllerService masterControllerService = new MasterControllerService();
            while (i.hasNext()) {
                String field = (String) i.next();
                Object val1=reportRow.getValue(field);
                String val = null;
                if (val1 != null) {
                    val = val1.toString();
                } else {
                    val = null;
                }
                
                if (field.equalsIgnoreCase("EUID")) {
                    if (groupIndex == 0) {
                        updateRecordsVO[voIndex].getEuid().add(val);
                    }
                    eo = masterControllerService.getEnterpriseObject(val); 
                    
                    updateRecordsVO[voIndex].getAddressLine1().add(obj);
                    
                }                     
                if (field.equalsIgnoreCase(UpdateReport.TIMESTAMP)) {
                    //updateRecordsVO[voIndex].setUpdateDate(val);simpleDateFormatFields.format
                    updateRecordsVO[voIndex].setUpdateDate(val);
                }
                
            }
        }
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
        
       if (transactionFields != null) {
           Iterator iter = transactionFields.iterator();
           UpdateRecords updateRecords = new UpdateRecords();
           EnterpriseObject eo = null;
           Object obj = null;
           MasterControllerService masterControllerService = new MasterControllerService();
            String epathValue =  new String();
            HashMap newValuesMap = new HashMap();
           
           while (iter.hasNext()) {
               String field = (String) iter.next();
               String val = reportRow.getValue(field) == null ? " " : reportRow.getValue(field).toString();//null safe
               if (field.equalsIgnoreCase("EUID1")) {
                   newValuesMap.put("EUID",val);
                   updateRecords.getEuid().add(val);
                   eo = masterControllerService.getEnterpriseObject(val.toString());

               }
               else if (field.equalsIgnoreCase("EUID2")) {
                   newValuesMap.put("EUID",val);
                   updateRecords.getEuid().add(val);
               }
               //Populate Hash Table as backup
                //updateRecordsResultsHash.put(field, val);
               rptFields.add(new ReportField(val));
           }
           vOList.add(updateRecords);
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

   private HashMap getOutPutValuesMap(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow) throws Exception {
        HashMap newValuesMap = new HashMap();
        List transactionFields = reportConfig.getTransactionFields();

        ArrayList fcArrayList = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
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
                if (field.equalsIgnoreCase("EUID")) {
                    newValuesMap.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());

                    for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        //populate the field values (apply  field input maskings...etc)
                        newValuesMap = populateHashMapValues(fieldConfig,  newValuesMap,  eo);  
                        
                        
                    }
                } else if (field.equalsIgnoreCase("EUID2")) {
                    newValuesMap.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());
                    for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        //populate the field values (apply  field input maskings...etc)
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
            if (value!=null && !operations.isField_VIP() && fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly                                  

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

   
   
    public UpdateReportConfig getUpdateSearchObject() throws ValidationException, EPathException {
        String errorMessage = null;
        EDMValidation edmValidation = new EDMValidation();
        ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        UpdateReportConfig urConfig = new UpdateReportConfig();

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                String msg1 = bundle.getString("timeFrom");
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? errorMessage : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1 + errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("RPT047: {0}",errorMessage));
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
                mLogger.info(mLocalizer.x("RPT048: {0}",errorMessage));
                return null;
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)? " " + this.getCreateStartTime() : " 00:00:00");
                   
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        urConfig.setStartDate(new Timestamp(date.getTime()));
                    }
                   
                    //createStartTime = "";
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("RPT049: {0}:{1}",errorMessage,validationException.getMessage()));
                   return null;
                }
            }
        }

        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                 String msg2 = bundle.getString("timeFrom");
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg2 + errorMessage, errorMessage));
                //Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.info(mLocalizer.x("RPT050: {0}",errorMessage));
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                mLogger.info(mLocalizer.x("RPT051: {0}",errorMessage));
                return null;
            } else {
                try {
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    
                    
                    if (date != null) {
                        urConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                    //createEndTime = "";
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("RPT052: {0}:{1}",errorMessage,validationException.getMessage()),validationException);
                    return null;
                }
            }
        }

          if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0))&&
           ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))){                
               Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + (this.getCreateStartTime() != null? " " +this.getCreateStartTime():" 00:00:00"));
               Date todate = DateUtil.string2Date(this.getCreateEndDate()+((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " +this.getCreateEndTime():" 23:59:59"));
               long startDate = fromdate.getTime();
               long endDate = todate.getTime();
                 if(endDate < startDate){
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("RPT053: {0}",errorMessage));
                    return null;
                   }
        }
        urConfig.addTransactionField(UpdateReport.EUID, UpdateReport.EUID, 20);
        urConfig.addTransactionField(UpdateReport.TIMESTAMP, UpdateReport.TIMESTAMP, 20);

        
        urConfig.setMaxResultSize(getMaxResultsSize());
        urConfig.setPageSize(getPageSize());
        
        if (errorMessage != null && errorMessage.length() != 0) {
            throw new ValidationException(mLocalizer.t("RPT504: Encountered the ValidationException: {0}",errorMessage));
        } else {
            return urConfig;
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
     * Return the populated Value object to the presetation layer
     * @return
     */
    public UpdateRecords[] getUpdateRecordsVO() {
//     updateRecordsVO = new UpdateRecords[vOList.size()];
//        for (int i = 0; i < vOList.size(); i++) {
//            updateRecordsVO[i] = new UpdateRecords();
//            updateRecordsVO[i] = (UpdateRecords)vOList.get(i);
//        }
//        request.setAttribute("size", new Integer(updateRecordsVO.length)); 
        if(updateRecordsVO != null) {
           request.setAttribute("size", new Integer(updateRecordsVO.length));   
        } else {
            request.setAttribute("size", new Integer("0"));   
        }
         
        return updateRecordsVO;
    }
    /**
     * Sets the valueobject for the Update Reports search
     * @param updateRecordsVO 
     */
    public void setUpdateRecordsVO(UpdateRecords[] updateRecordsVO) {
        this.updateRecordsVO = updateRecordsVO;
    }

    public ArrayList getResultsConfigArrayList() {        
        String REPORT_LABEL = bundle.getString("Updated_Record_Report_Label");
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
