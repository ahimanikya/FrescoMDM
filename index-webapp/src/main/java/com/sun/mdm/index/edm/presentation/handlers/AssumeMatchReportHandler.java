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
 * Created on November 15, 2007, 
 * Author : Pratibha, Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.AssumeMatchesRecords;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.report.ReportException;
import java.rmi.RemoteException;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;

import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.AssumedMatchReportRow;
import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportObject1;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;

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
import net.java.hulp.i18n.LocalizationSupport;



public class AssumeMatchReportHandler  {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.AssumeMatchReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    /* @param dataRowList1 */
    ArrayList dataRowList1 = null;
    /* @param updateIterator */
    TransactionIterator updateIterator = null;
    /* @param amri */
    AssumedMatchIterator amri = null;
    /* @param amrConfig */
    AssumedMatchReportConfig amrConfig = null;
    /* @param amRpt */
    private AssumedMatchReport  amRpt = null;
    
    Hashtable assumematchRecordsResultsHash = new Hashtable();
    ArrayList vOList = new ArrayList();
     /**
     * Data Object that holds the search results 
     */
    private AssumeMatchesRecords[] assumematchesRecordsVO = null;
    
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
     * Search Maximum Reports in  AssumeMatchReports
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
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    
    private ArrayList resultsConfigArrayList  = new ArrayList();
    private ArrayList resultsArrayList  = new ArrayList();
   
    /* This method populates the Assume Match Reports using the Service Layer call and handles exceptions*/
   public AssumeMatchesRecords[] assumeMatchReport() throws ValidationException, EPathException, ReportException, PageException, RemoteException, Exception    {
       request.setAttribute("tabName", "ASSUME_MATCH");        
       amrConfig = getAssumedMatchReportSearchObject();
       amri = QwsController.getReportGenerator().execAssumedMatchReportIterator(amrConfig);
       // Code to retrieve the data rows of report records.
       ReportDataRow[] rdr = getAMRRows();
/*  
       for (int i = 0; i < rdr.length; i++) {
           ReportDataRow reportDataRow = rdr[i];
           getAssumematchesRecordsVO()[i] = new AssumeMatchesRecords(); //Be safe with malloc
           getAssumematchesRecordsVO()[i].setEuid(rdr[i].getFields()[i].getValue());
       }
 */
       return getAssumematchesRecordsVO();
    }
   
   //getter method to retrieve the data rows of report records.
   private ReportDataRow[] getAMRRows() throws Exception {
        ArrayList dataRowList = new ArrayList();
        ArrayList resultArrayList = new ArrayList();
        String prevEuid = "";
        int index = 0;
        ArrayList summaryList = new ArrayList();
        amri.sortBy("EUID",false);
        while (amri.hasNext()) {
            AssumedMatchReportRow reportRow = new AssumedMatchReportRow(amri.next(), amrConfig);
           
            if (index ==0 ) prevEuid = reportRow.getEUID();
            if (!prevEuid.equalsIgnoreCase(reportRow.getEUID()) || index +1 == amri.count())   {
                vOList.add(summaryList);
                summaryList = new ArrayList();
            }
            summaryList.add(reportRow);
            prevEuid = reportRow.getEUID();
            index++;
            //resultArrayList.add(getOutPutValuesMap(amrConfig, reportRow));
        }
        populateVO();
        request.setAttribute("assumeMatchReportList", resultsArrayList);
        return dataRowList2Array(dataRowList);
    }
   
   private void populateVO() throws Exception    {
       assumematchesRecordsVO = new AssumeMatchesRecords[vOList.size()];
       
       for (int i=0; i < vOList.size();i++)   {
         ArrayList groupedList = (ArrayList)vOList.get(i);
         assumematchesRecordsVO[i] = new AssumeMatchesRecords();
         for (int j=0;j< groupedList.size();j++)   {
             MultirowReportObject1 reportRow = (MultirowReportObject1)groupedList.get(j);
             populateRow(amrConfig,reportRow,j,i);
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
   
    private void populateRow(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow, int groupIndex, int voIndex) throws Exception {
        ArrayList fcArrayList = getResultsConfigArrayList();
        HashMap newValuesMap = new HashMap();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        String epathValue =  new String();
        List transactionFields = reportConfig.getTransactionFields();
        String strVal;
        if (transactionFields != null) {
            Iterator iter = transactionFields.iterator();
            EnterpriseObject eo = null;
            MasterControllerService masterControllerService = new MasterControllerService();

            while (iter.hasNext()) {
                String field = (String) iter.next();
                String val = reportRow.getValue(field).toString();
                if (field.equalsIgnoreCase("EUID")) {
                    newValuesMap.put("EUID", val);
                    eo = masterControllerService.getEnterpriseObject(val.toString());

					for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                            epathValue = fieldConfig.getFullFieldName();
                        } else {
                            epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                        }

                        if (eo!=null) {
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
                } else if (field.equalsIgnoreCase("SystemCode")) {
                    if (groupIndex == 0) {
                        newValuesMap.put(field, val);
                    } else {
                        newValuesMap.put(field, "");
                    }
                } else  {
                    if (groupIndex == 0) {
                        newValuesMap.put(field, val);
                    } else {
                        newValuesMap.put(field, "");
                    }
                }
            }
        }
        //add the output here 
        resultsArrayList.add(newValuesMap);   
    }

   private HashMap getOutPutValuesMap(MultirowReportConfig1 reportConfig, MultirowReportObject1 reportRow) throws Exception {
        HashMap newValuesMap = new HashMap();
        List transactionFields = reportConfig.getTransactionFields();

        ArrayList fcArrayList = getResultsConfigArrayList();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
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
                                    strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                                }
                            } else {
                                newValuesMap.put(fieldConfig.getFullFieldName(), value);
                            }
                        }
                    }
                
               }  else if (field.equalsIgnoreCase("SystemCode")){
                    newValuesMap.put("SystemCode", val);
                }  else if (field.equalsIgnoreCase("LID")){
                    newValuesMap.put("LID", val);
                }  else if (field.equalsIgnoreCase("Weight")){
                    newValuesMap.put("Weight", val);
                }   
               
                
            }
        }
        return newValuesMap;
    }
   
   public AssumedMatchReportConfig getAssumedMatchReportSearchObject()  throws ValidationException, EPathException {
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         AssumedMatchReportConfig amrc = new AssumedMatchReportConfig(); 
        // One of Many validation 
        if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)&&
                (this.getReportSize() != null && this.getReportSize().trim().length() == 0)){
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                mLogger.error(mLocalizer.x("ASM027: {0} ",errorMessage));
           }

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                String msg1 = bundle.getString("timeFrom");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1+ errorMessage, errorMessage));
                //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.error(mLocalizer.x("ASM028: Start time validation failed: {0}",message));
            }            
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.error(mLocalizer.x("ASM029: Start Date validation failed :{0} :{1}",message,errorMessage));
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    if (getCreateStartTime().trim().length() == 0) {
                        createStartTime = "00:00:00";
                    }
                    String searchStartDate = this.getCreateStartDate() + ((this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)? " " + this.getCreateStartTime() : " 00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        amrc.setStartDate(new Timestamp(date.getTime()));
                    }   
                    createStartTime = "";
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
                    mLogger.error(mLocalizer.x("ASM030: Validation failed :{0}",errorMessage));
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
                //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.error(mLocalizer.x("ASM031: Validation failed :{0}",msg2+errorMessage));
            } 
       }    
         
        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                 //String msg3 = bundle.getString("End_Date");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.error(mLocalizer.x("ASM032: Validation failed :{0}",errorMessage));
            } else {
                try {
                    if (getCreateEndTime().trim().length() == 0) {
                        createEndTime = "23:59:59";
                    }
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + ((this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        amrc.setEndDate(new Timestamp(date.getTime()));
                    }
                    createEndTime = "";
                } catch (ValidationException validationException) {
                    //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("ASM033: Validation failed :{0}",errorMessage));
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
                   // Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                   mLogger.error(mLocalizer.x("ASM034: Validation failed :{0}",errorMessage));
                   }
        }
          if (this.getReportSize() != null && this.getReportSize().length() > 0)    {
            String message = edmValidation.validateNumber(this.getReportSize());
            amrc.setMaxResultSize(new Integer(this.getReportSize()));
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                String msg4 = bundle.getString("ReportSize");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg4+errorMessage, errorMessage));
                //java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                mLogger.error(mLocalizer.x("ASM035: Validation failed :{0}",errorMessage));
            }
        }    
               
        amrc.addTransactionField("SystemCode", "SystemCode", 10);
        amrc.addTransactionField("LID", "Local Id", 20);
        amrc.addTransactionField("Weight", "Weight", 10);
        amrc.addTransactionField("EUID", "EUID", 20);
        // Set labels, path and other UI attributes here        

        if (errorMessage != null && errorMessage.length() != 0)  {            
            throw new ValidationException(mLocalizer.t("ASM501: {0}",errorMessage));
        } else {
            return amrc;
        }                                 
    }
   
   
   
   ////////////////////////////////////////End of Assume Match Reports////////////////////////////////////////  
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
     * sets Report Size
     */
    public void setReportSize(String size) {
        this.reportSize = size;
    }
    
    
     /**
     * @return Report Size
     */
    public String getReportSize() {
        return reportSize;
    }
    /**
     * Return the populated Value object to the presetation layer
     * @return
     */
     public AssumeMatchesRecords[] getAssumematchesRecordsVO() {
        request.setAttribute("size", new Integer(assumematchesRecordsVO.length));        
        return assumematchesRecordsVO;
    }
     /**
     * Set the AssumeMatchReports objects
     * @param assumematchesRecordsVO
     */
    public void setAssumematchesRecordsVO(AssumeMatchesRecords[] assumematchesRecordsVO) {
        this.assumematchesRecordsVO = assumematchesRecordsVO;
    }

    public ArrayList getResultsConfigArrayList() {
        ReportHandler reportHandler = new ReportHandler();
        reportHandler.setReportType("Assume Match Report");        
        ArrayList fcArrayList  = reportHandler.getSearchResultsScreenConfigArray();
        return fcArrayList;
    }


    public void setResultsConfigArrayList(ArrayList resultsConfigArrayList) {
        this.resultsConfigArrayList = resultsConfigArrayList;
    }

    public ArrayList getResultsArrayList() {
        return resultsArrayList;
    }

    public void setResultsArrayList(ArrayList resultsArrayList) {
        this.resultsArrayList = resultsArrayList;
    }
    
}
