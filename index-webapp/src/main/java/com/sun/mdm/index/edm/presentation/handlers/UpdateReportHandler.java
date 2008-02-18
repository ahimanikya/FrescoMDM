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
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;

import com.sun.mdm.index.report.UpdateReport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

/** Creates a new instance of DeactivatedReportsHandler*/ 
public class UpdateReportHandler    { 
    
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
   
    /*
     *  Request Object Handle
     */  
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    /**
     * @return the value object for display
     * @throws com.sun.mdm.index.objects.validation.exception.ValidationException
     * @throws com.sun.mdm.index.objects.epath.EPathException
     * @throws com.sun.mdm.index.report.ReportException 
     * @throws java.lang.Exception
     */
   public UpdateRecords[] updateReport() throws ValidationException, EPathException, ReportException, Exception    {
       request.setAttribute("tabName", "UPDATE_REPORT");        
       urConfig = getUpdateSearchObject();
       updateIterator = QwsController.getReportGenerator().execUpdateReportIterator(urConfig);
       
       // Code to retrieve the data rows of report records.
       ReportDataRow[] rdr = getURRows();
      // setUpdateRecordsVO(new UpdateRecords[rdr.length]);
       return getUpdateRecordsVO();
    }
   
   //getter method to retrieve the data rows of report records.
   private ReportDataRow[] getURRows() throws Exception {
//        ArrayList dataRowList = new ArrayList();
//        while (updateIterator.hasNext()) {
//            UpdateReportRow reportRow = new UpdateReportRow(updateIterator.next(), urConfig);
//            ReportDataRow[] dataRows = writeRow(urConfig, reportRow);
//            for (int i = 0; i < dataRows.length; i++) {
//                dataRowList.add(dataRows[i]);
//            }
//        }
       
        
        ArrayList dataRowList = new ArrayList();
        String prevTimestamp = null;
        int index = 0;
        ArrayList summaryList = new ArrayList();
    //    updateIterator.sortBy("Timestamp",false);
        while (updateIterator.hasNext()) {
            UpdateReportRow  reportRow = new UpdateReportRow (updateIterator.next(), urConfig);
           
            
             SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy HH:MM:ss a");
             
            String currentTime =simpleDateFormatFields.format(reportRow.getTimestamp()); 
            
           
            if (index == 0 ) { 
               // String prevTime = simpleDateFormatFields.format((Object)prevTimestamp);
                prevTimestamp = currentTime;
            }
            //System.out.println("PREV:: " + prevTimestamp + " Current :: " + currentTime + "index:: " + index);
            if (!prevTimestamp.equalsIgnoreCase(currentTime) || index +1 == updateIterator.count())   {
                //System.out.println("Accumulated!");
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
        }
        populateVO();
        return dataRowList2Array(dataRowList);
     }
   
   private void populateVO() throws Exception    {
       //System.out.println("--------------------------VOLIST Size ----------------------" + vOList.size());             
       updateRecordsVO = new UpdateRecords[vOList.size()];
       
       for (int i=0; i < vOList.size();i++)   {
         ArrayList groupedList = (ArrayList)vOList.get(i);
         updateRecordsVO[i] = new UpdateRecords();
         //System.out.println("--------------------------Group Size ----------------------" + groupedList.size());             
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
                    //System.out.println("Field " + field + " Value " + val + " Group Index " + groupIndex + " VoIndex " + voIndex);                    
                    if (groupIndex == 0) {
                        updateRecordsVO[voIndex].getEuid().add(val);
                    }
                    eo = masterControllerService.getEnterpriseObject(val); 
                    
               
                    obj = EPathAPI.getFieldValue("Person.FirstName", eo.getSBR().getObject());
                    //System.out.println("First Name:: " + obj.toString());
                    //Set the First Name Values in VO
                    updateRecordsVO[voIndex].getFirstName().add(obj);

                    obj = EPathAPI.getFieldValue("Person.LastName", eo.getSBR().getObject());
                    //System.out.println("Last Name:: " + obj.toString());
                    //Set the Last Name Values in VO
                    updateRecordsVO[voIndex].getLastName().add(obj);

                    obj = EPathAPI.getFieldValue("Person.SSN", eo.getSBR().getObject());
                    
                    
                    ////System.out.println("SSN :: " + obj.toString());
                    //Set the Last Name Values in VO       
                    updateRecordsVO[voIndex].getSsn().add(obj);

                    obj = EPathAPI.getFieldValue("Person.DOB", eo.getSBR().getObject());
                    //System.out.println("DOB :: " + obj.toString());
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                    String dob = simpleDateFormatFields.format(obj);
                    updateRecordsVO[voIndex].getDob().add(dob);
                    
                    obj = EPathAPI.getFieldValue("Person.Address.AddressLine1", eo.getSBR().getObject());
                    updateRecordsVO[voIndex].getAddressLine1().add(obj);
                    
                }                     
                if (field.equalsIgnoreCase(UpdateReport.TIMESTAMP)) {
                    //updateRecordsVO[voIndex].setUpdateDate(val);simpleDateFormatFields.format
                    updateRecordsVO[voIndex].setUpdateDate(val);
                }
                
                //System.out.println("Field " + field + " Value " + val + " Group Index " + groupIndex + " VoIndex " + voIndex);                    
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
       if (transactionFields != null) {
           Iterator iter = transactionFields.iterator();
           UpdateRecords updateRecords = new UpdateRecords();
           EnterpriseObject eo = null;
           Object obj = null;
           MasterControllerService masterControllerService = new MasterControllerService();

           while (iter.hasNext()) {
               String field = (String) iter.next();
               //System.out.println("Field :: " + field);
               String val = reportRow.getValue(field) == null ? " " : reportRow.getValue(field).toString();//null safe
               if (field.equalsIgnoreCase("EUID")) {
                   updateRecords.getEuid().add(val);
                   eo = masterControllerService.getEnterpriseObject(val.toString());
                   obj = EPathAPI.getFieldValue("Person.FirstName", eo.getSBR().getObject());
                   //Set the First Name Values in VO
                   updateRecords.getFirstName().add((String) obj);

                   obj = EPathAPI.getFieldValue("Person.LastName", eo.getSBR().getObject());
                   //Set the Last Name Values in VO
                   updateRecords.getLastName().add((String) obj);

                   obj = EPathAPI.getFieldValue("Person.SSN", eo.getSBR().getObject());
                   //Set the Last Name Values in VO       
                   updateRecords.getSsn().add((String) obj);

                   obj = EPathAPI.getFieldValue("Person.DOB", eo.getSBR().getObject());
                   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                   String dob = simpleDateFormatFields.format(obj);
                   updateRecords.getDob().add(dob);

               } else if (field.equalsIgnoreCase(UpdateReport.TIMESTAMP)) {
                   updateRecords.setUpdateTime(val);
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

    public UpdateReportConfig getUpdateSearchObject() throws ValidationException, EPathException {
        String errorMessage = null;
        EDMValidation edmValidation = new EDMValidation();
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        UpdateReportConfig urConfig = new UpdateReportConfig();
        // One of Many validation 
        if ((this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)) {
            errorMessage = bundle.getString("ERROR_one_of_many");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
            Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
        }

        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? errorMessage : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
                Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }

        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, message, message);
            } else {
                //If Time is supplied append it to the date and check if it parses as a valid date
                try {
                    String searchStartDate = this.getCreateStartDate() + (this.getCreateStartTime() != null ? " " + this.getCreateStartTime() : "00:00:00");
                    Date date = DateUtil.string2Date(searchStartDate);
                    if (date != null) {
                        urConfig.setStartDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_start_date") : bundle.getString("ERROR_start_date"));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
                }
            }
        }

        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0) {
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
                Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }

        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0) {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, message, message);
            } else {
                try {
                    //If Time is supplied append it to the date to check if it parses into a valid Date
                    String searchEndDate = this.getCreateEndDate() + (this.getCreateEndTime() != null ? " " + this.getCreateEndTime() : " 23:59:59");
                    Date date = DateUtil.string2Date(searchEndDate);
                    if (date != null) {
                        urConfig.setEndDate(new Timestamp(date.getTime()));
                    }
                } catch (ValidationException validationException) {
                    Logger.getLogger(UpdateReportHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
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
        urConfig.addTransactionField(UpdateReport.EUID, UpdateReport.EUID, 20);
 //       urConfig.addTransactionField(UpdateReport.LID, UpdateReport.LID, 20);
        urConfig.addTransactionField(UpdateReport.TIMESTAMP, UpdateReport.TIMESTAMP, 20);

        urConfig.setMaxResultSize(new Integer("20"));
        if (errorMessage != null && errorMessage.length() != 0) {
            throw new ValidationException(errorMessage);
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
        
         request.setAttribute("size", new Integer(updateRecordsVO.length));   
         
         
         //System.out.println("*******VO Lenght*****"+updateRecordsVO.length);
          for (int i =0 ; i< updateRecordsVO.length;i++)   {
            //System.out.println( "EUID "  + updateRecordsVO[i].getEuid() + "First "+   updateRecordsVO[i].getFirstName() + "LAST " +  updateRecordsVO[i].getLastName() );
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

}
