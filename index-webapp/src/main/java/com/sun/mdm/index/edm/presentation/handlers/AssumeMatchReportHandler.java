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
import com.sun.mdm.index.objects.exception.ObjectException;
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
import com.sun.mdm.index.objects.SystemObject;
import net.java.hulp.i18n.LocalizationSupport;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathException;




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
    
    //resource bundle definitin
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
        
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
    private Integer maxResultsSize;  
    private Integer pageSize;  
   
    /**
     * This method populates the Assume Match Reports using the Service Layer call and handles exceptions
     * 
     * 
     * @return  ArrayList of assumed match report hashmap values.
     * 
     * @throws EPathException 
     * @throws PageException
     * @throws RemoteException
     * @throws ReportException
     * @throws Exception
     * 
     * @see populateValuesMap, buildHashMapValues
     * 
     **/
    public ArrayList assumeMatchReport() throws ValidationException, EPathException, ReportException, PageException, RemoteException, Exception {
        request.setAttribute("tabName", "ASSUME_MATCH");
        amrConfig = getAssumedMatchReportSearchObject();
        if (amrConfig != null) {
            amri = QwsController.getReportGenerator().execAssumedMatchReportIterator(amrConfig);
            // Code to retrieve the data rows of report records.
            //ReportDataRow[] rdr = getAMRRows();
            String prevEuid = "";
            int index = 0;
            ArrayList summaryList = new ArrayList();
            amri.sortBy("EUID", false);
            while (amri.hasNext()) {
                AssumedMatchReportRow assumedMatchReportRow = new AssumedMatchReportRow(amri.next(), amrConfig);

                /*if (index == 0) {
                    prevEuid = assumedMatchReportRow.getEUID();
                }*/
                if (index != 0 && !prevEuid.equalsIgnoreCase(assumedMatchReportRow.getEUID())) {
                    populateValuesMap(summaryList);
                    summaryList = new ArrayList();
                 }
                summaryList.add(assumedMatchReportRow);
                prevEuid = assumedMatchReportRow.getEUID();
                index++;
            }
            populateValuesMap(summaryList);
            return resultsArrayList;
        } else {
            return null;
        }
    }
   
   //getter method to retrieve the data rows of report records.
    private ReportDataRow[] getAMRRows() throws Exception {
        ArrayList dataRowList = new ArrayList();
        String prevEuid = "";
        int index = 0;
        ArrayList summaryList = new ArrayList();
        amri.sortBy("EUID", false);
        
		while (amri.hasNext()) {
            AssumedMatchReportRow assumedMatchReportRow = new AssumedMatchReportRow(amri.next(), amrConfig);

            if (index == 0) {
                prevEuid = assumedMatchReportRow.getEUID();
            }
            if (!prevEuid.equalsIgnoreCase(assumedMatchReportRow.getEUID()) || index + 1 == amri.count()) {
                populateValuesMap(summaryList);
                summaryList = new ArrayList();
            }
            summaryList.add(assumedMatchReportRow);
            prevEuid = assumedMatchReportRow.getEUID();
            index++;
        }
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
                } else  {
                    newValuesMap.put(field, val);
                    /*if (groupIndex == 0) {
                        newValuesMap.put(field, val);
                    } else {
                        newValuesMap.put(field, "");
                    }*/
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
                         newValuesMap = populateHashMapValues(fieldConfig,  newValuesMap,  eo);  
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
   private void populateValuesMap(ArrayList assumedMatchReportRowList) throws Exception {
        MasterControllerService masterControllerService = new MasterControllerService();
         
        AssumedMatchReportRow assumedMatchReportCompare = (AssumedMatchReportRow) assumedMatchReportRowList.get(0);
        Object[] systemObjectsArrayObjCompare = masterControllerService.getEnterpriseObject(assumedMatchReportCompare.getEUID()).getSystemObjects().toArray();
       HashMap compareMap = new HashMap();
       for (int i = 0; i < systemObjectsArrayObjCompare.length; i++) {
           SystemObject objectCompare = (SystemObject) systemObjectsArrayObjCompare[i];
           compareMap.put(objectCompare.getLID() + "/" + objectCompare.getSystemCode(), objectCompare);
       }

       HashMap amSystemCodesMap = new HashMap();
       for (int r = 0; r < assumedMatchReportRowList.size(); r++) {
           AssumedMatchReportRow assumedMatchReportRow = (AssumedMatchReportRow) assumedMatchReportRowList.get(r);
           amSystemCodesMap.put(assumedMatchReportRow.getLID() + "/" + assumedMatchReportRow.getSystemCode(), "");

       }
       Object array[] = amSystemCodesMap.keySet().toArray();
       for (int i = 0; i < array.length; i++) {
           String thisLidShystem = (String)array[i];           
           compareMap.remove(thisLidShystem);
       }
              
        
       array =  compareMap.keySet().toArray();
       for (int i = 0; i < array.length; i++) {
           HashMap newValuesMap = new HashMap();           
           String thisLidShystem = (String)array[i];                      
           SystemObject  systemObject = (SystemObject)compareMap.get(thisLidShystem);
           newValuesMap = buildHashMapValues(systemObject);
           newValuesMap.put("EUID", assumedMatchReportCompare.getEUID());
           newValuesMap.put("LID", systemObject.getLID());
           newValuesMap.put("SystemCode", ValidationService.getInstance().getSystemDescription(systemObject.getSystemCode()));
           newValuesMap.put("Weight", assumedMatchReportCompare.getWeight());
           resultsArrayList.add(newValuesMap);           
       }
       
        for (int r = 0; r < assumedMatchReportRowList.size(); r++) {
           AssumedMatchReportRow assumedMatchReportRow = (AssumedMatchReportRow) assumedMatchReportRowList.get(r);
           HashMap newValuesMap = new HashMap();
           SystemObject systemObject = masterControllerService.getSystemObject(assumedMatchReportRow.getSystemCode(), assumedMatchReportRow.getLID());
           newValuesMap = buildHashMapValues(systemObject);
           newValuesMap.put("LID", assumedMatchReportRow.getLID());
           newValuesMap.put("EUID", assumedMatchReportRow.getEUID());
           newValuesMap.put("SystemCode", ValidationService.getInstance().getSystemDescription(assumedMatchReportRow.getSystemCode()));
           newValuesMap.put("Weight", assumedMatchReportRow.getWeight());
           resultsArrayList.add(newValuesMap);
           newValuesMap = new HashMap();
            
       }

    }
   
   private HashMap buildHashMapValues(SystemObject systemObject) throws ObjectException, EPathException {
       ArrayList fcArrayList = getResultsConfigArrayList();
       SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
       String strVal;
       String epathValue;
       HashMap newValuesMap = new HashMap();
       for (int i = 0; i < fcArrayList.size(); i++) {
           FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
           if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
               epathValue = fieldConfig.getFullFieldName();
           } else {
               epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
           }

           if (fieldConfig.getValueType() == 6) { // For date related fields

               if (fieldConfig.isSensitive()) { // Mask the sensitive fields accordingly

                   newValuesMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
               } else {
                   newValuesMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, systemObject.getObject())));
               }
           } else {
               Object value = EPathAPI.getFieldValue(epathValue, systemObject.getObject());
               if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                   if (value != null) {
                       //SET THE VALUES WITH USER CODES AND VALUE LIST 
                       if (fieldConfig.getUserCode() != null) { //If user code exists then get the user defined descriptions

                           strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                       } else { //if  value list then get the descrption for the codes

                           strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                       }

                       if (fieldConfig.isSensitive()) { // Mask the sensitive fields accordingly

                           newValuesMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                       } else {
                           newValuesMap.put(fieldConfig.getFullFieldName(), strVal);
                       }
                   }
               } else {
                   if (fieldConfig.isSensitive()) { // Mask the sensitive fields accordingly

                       newValuesMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                   } else {
                       newValuesMap.put(fieldConfig.getFullFieldName(), value);
                   }
               }
           }
       }
       
       return newValuesMap;
       
   }
   public AssumedMatchReportConfig getAssumedMatchReportSearchObject()  throws ValidationException, EPathException {
         String errorMessage = null;
         EDMValidation edmValidation = new EDMValidation();         
         ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());        
         AssumedMatchReportConfig amrc = new AssumedMatchReportConfig(); 

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                String msg1 = bundle.getString("timeFrom");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1+ errorMessage, errorMessage));
                //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                mLogger.info(mLocalizer.x("ASM028: Start time validation failed: {0}",message));
                return null;
            }            
                //if only time fields are entered validate for the date fields 
            if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_from");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("ASM202: {0} ",errorMessage));
                return null;
            }
    }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, message, message);
                 mLogger.info(mLocalizer.x("ASM029: Start Date validation failed :{0} :{1}",message,errorMessage));
                return null;
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
                    mLogger.error(mLocalizer.x("ASM030: Validation failed :{0}",errorMessage),validationException);
                    return null;
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
                 mLogger.info(mLocalizer.x("ASM031: Validation failed :{0}",msg2+errorMessage));
                return null;
            } 
            //if only time fields are entered validate for the date fields 
            if ((this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_to");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("ASM201: {0} ",errorMessage));
                return null;
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
                 mLogger.info(mLocalizer.x("ASM032: Validation failed :{0}",errorMessage));
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
                        amrc.setEndDate(new Timestamp(date.getTime()));
                    }
                    createEndTime = "";
                } catch (ValidationException validationException) {
                    //Logger.getLogger(AssumeMatchReportHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.error(mLocalizer.x("ASM033: Validation failed :{0}",errorMessage),validationException);
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
                   // Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                   mLogger.info(mLocalizer.x("ASM034: Validation failed :{0}",errorMessage));
                   return null;
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
                mLogger.info(mLocalizer.x("ASM035: Validation failed :{0}",errorMessage));
                return null;
            }
        }    
               
        amrc.addTransactionField("SystemCode", "SystemCode", 10);
        amrc.addTransactionField("LID", "Local Id", 20);
        amrc.addTransactionField("Weight", "Weight", 10);
        amrc.addTransactionField("EUID", "EUID", 20);

     
        amrc.setMaxResultSize(getMaxResultsSize());
        amrc.setPageSize(getPageSize());
        
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
        String REPORT_LABEL = bundle.getString("Assumed_Matches_Report_Label");
        ReportHandler reportHandler = new ReportHandler();
        reportHandler.setReportType(REPORT_LABEL);                
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
