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
 * AuditLogHandler.java 
 * Created on September 30, 2007, 
 * Author : Anil, Rajanikanth
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.objects.validation.exception.ValidationException;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import java.rmi.RemoteException;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


public class AuditLogHandler {    
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
     * Search Local ID
     */
    private String localid = null;
    /**
     * Search EUID
     */
    private String euid = null;
    /**
     * Search System User
     */
    private String systemuser = null;    
    /**
     * Search Function
     */
    private String resultOption = null;    
    /**
     * Data Object that holds the search results 
     */
    private AuditDataObject[] auditLogVO = null;    
    /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;

    
    ArrayList vOList = new ArrayList();
    /**
     * JSF Naviagation String
     */
    private static final String AUDIT_LOG_SEARCH_RES = "Audit Log";
    private static final String AUDIT_LOG_VALIDATION_ERROR = "Validation Error";

    /**
     * Data Object that holds the search results 
     */
    private int searchSize  = 0;    
    
    MasterControllerService  masterControllerService=null;
    
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    /** Creates a new instance of AuditLogHandler */
    public AuditLogHandler() {
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
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createStartDate::" + createStartDate);
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
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createEndDate:: " + createEndDate);
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
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createStartTime::" + createStartTime);
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
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createEndTime::" + createEndTime);
    }

    /**
     * @return Local Id
     */
    public String getLocalid() {
        return localid;
    }

    /**
     * Sets the Local ID parameter for the search
     * @param localid
     */
    public void setLocalid(String localid) {
        this.localid = localid;
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:localid::" + localid);
    }

    /**
     * @return EUID
     */
    public String getEuid() {
        return euid;
    }

    /**
     * Sets the EUID parameter for the search
     * @param euid
     */
    public void setEuid(String euid) {
        this.euid = euid;
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:euid::" + euid);
      }

    /**
     * @return System User
     */
    public String getSystemuser() {
        return systemuser;
    }

    /**
     * Sets the System User parameter for the search
     * @param systemuser
     */
    public void setSystemuser(String systemuser) {
        this.systemuser = systemuser;
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:systemuser::" + systemuser);
    }

    /**
     * @return function
     */
    public String getResultOption() {
        return resultOption;
    }

    /**
     * Sets the function parameter for the search
     * @param function
     */
    public void setResultOption(String function) {
        this.resultOption = function;
        Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:function::" + resultOption);
    }
   
    /**
     * This method calls the service layer method MasterControllerService.lookupAuditLog to fetch the Audit Log Search results
     * The method builds the array of AuditDataObject to be displayed on the resulting JSF
     * @return AUDIT_LOG_SEARCH_RES the Navigation rule for the JSF framework
     * @throws com.sun.mdm.index.presentation.exception.HandlerException 
     */
    public String auditLogSearch() throws HandlerException  {
       try {
            ConfigManager.init();
            AuditSearchObject aso = this.getAuditSearchObject();
            // Lookup Audit log Controller
            masterControllerService = new MasterControllerService();
            AuditIterator alPageIter = masterControllerService.lookupAuditLog(aso);
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " :: In Audit Log Handler iter size===>" + alPageIter.count());
            int i = 0;
           //Set the size of the VO Array
            setAuditLogVO(new AuditDataObject[alPageIter.count()]);
            //Populate the Value Object to be displayed on the JSF page.
            while (alPageIter.hasNext()) {
                auditLogVO[i] = new AuditDataObject(); //to be safe with malloc
                auditLogVO[i] = alPageIter.next();
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " :: EUID 1 : " + auditLogVO[i].getEUID1());
                //Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + "Audit Log Handler EUID is " + this.getEuid());
                            i++;
            }
            setResultsSize(auditLogVO.length);
            request.setAttribute("resultsSize",new Integer(auditLogVO.length) );
        } catch (ValidationException ex) {
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, ex.getMessage());
            return this.AUDIT_LOG_VALIDATION_ERROR;
        } catch ( ProcessingException ex) {
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new HandlerException("Processing Exception Occured");
        } catch (RemoteException ex) {
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new HandlerException("Remote Exception Occured");
        } catch (UserException ex) {
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new HandlerException("User Exception Occured");
        } catch (Exception ex) {
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new HandlerException("Exception Occured");
        }
        return AUDIT_LOG_SEARCH_RES;
    }
    
    /**
     * @exception ValidationException when entry is not valid.
     * This function validates the user input and builds the search object
     * @return  the Audit search object
     */

    public AuditSearchObject getAuditSearchObject() throws ValidationException {
          
        AuditSearchObject auditSearchObject = new AuditSearchObject();
        EDMValidation edmValidation = new EDMValidation();

        // Set to static values need clarification from prathiba
        //This will be revoked when login module is implemented.
        //auditSearchObject.setPageSize(ConfigManager.getInstance().getMatchingConfig().getItemPerSearchResultPage());
        //auditSearchObject.setMaxElements(ConfigManager.getInstance().getMatchingConfig().getMaxResultSize());

        auditSearchObject.setPageSize(10);
        auditSearchObject.setMaxElements(100);
        Date date = null;
        
        String errorMessage = null;
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        // One of Many validation 
            if ((this.getEuid() != null && this.getEuid().trim().length() == 0) &&
                (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)&&
                (this.getResultOption() == null) && //Function
                (this.getSystemuser() != null && this.getSystemuser().trim().length() == 0)){
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
        }
        
        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
            }            
        }
        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
            }           
        }    
         //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }        
        
        //Check CreateStartDateField and add timestamp it its null.
        try {
            if ((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0)) {                
                //If Time is supplied append it to the date
                 if (getCreateStartTime().trim().length() == 0) {
                    createStartTime = "00:00:00";
                 }
                
                String searchStartDate = this.getCreateStartDate() + (this.getCreateStartTime() != null? " " + this.getCreateStartTime():" 00:00:00");                                
                date = DateUtil.string2Date(searchStartDate);
                if (date != null) {
                    auditSearchObject.setCreateStartDate(new Timestamp(date.getTime()));
                }
            }
             
           } catch (ValidationException validationException) {
            errorMessage = (errorMessage != null && errorMessage.length() > 0? bundle.getString("ERROR_start_date"):bundle.getString("ERROR_start_date"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
        }
                 
        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
            }            
        }

        //Check CreateEndDateField
        if ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0)) {
            try {
                //If Time is supplied append it to the date
                 if (getCreateEndTime().trim().length() == 0) {
                    createEndTime="23:59:59";
                  }

                String searchEndDate = this.getCreateEndDate() +  (this.getCreateEndTime() != null? " " +this.getCreateEndTime():" 23:59:59");
                date = DateUtil.string2Date(searchEndDate);
                if (date != null) {
                    auditSearchObject.setCreateEndDate(new Timestamp(date.getTime()));
                }
                //createEndTime="";
            } catch (ValidationException validationException) {
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                errorMessage = (errorMessage != null && errorMessage.length() > 0?bundle.getString("ERROR_end_date"):bundle.getString("ERROR_end_date"));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));                
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
          
        if (this.getSystemuser() != null && this.getSystemuser().length() > 0) {
            auditSearchObject.setCreateUser(this.getSystemuser());
        } else {
            auditSearchObject.setCreateUser(null);
        }

        if (this.getResultOption() != null && this.getResultOption().length() > 0) {
            auditSearchObject.setFunction(this.getResultOption());
        } else {
            auditSearchObject.setFunction(null);
         }

        if (this.getEuid() != null && this.getEuid().length() > 0) {
            auditSearchObject.setEUID(this.getEuid());
            String message = edmValidation.validateNumber(this.getEuid());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
         }
        } else {
            auditSearchObject.setEUID(null);
        }
 
       if (errorMessage != null && errorMessage.length() != 0)  {
                throw new ValidationException(errorMessage);
            }
            return auditSearchObject;
        }

    public AuditDataObject[] getAuditLogVO() {
        return this.auditLogVO;
    }

    public void setAuditLogVO(AuditDataObject[] auditLogVO) {
        this.auditLogVO = auditLogVO;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
        
    }
    
}