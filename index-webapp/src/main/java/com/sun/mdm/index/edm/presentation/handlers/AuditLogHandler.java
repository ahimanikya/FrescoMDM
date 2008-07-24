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

import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.master.search.audit.AuditDataObject;
import com.sun.mdm.index.objects.validation.exception.ValidationException;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import java.rmi.RemoteException;

import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;


import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import java.util.Iterator;
import net.java.hulp.i18n.LocalizationSupport;

//import net.java.hulp.i18n.Logger;


public class AuditLogHandler extends ScreenConfiguration {

     private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.AuditLogHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
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
    /**
     * Value Object
     */
    ArrayList vOList = new ArrayList();
    
    /*
    * Map to hold the parameters
    **/
    private HashMap parametersMap  = new HashMap();
    /**
     * JSF Naviagation String
     */
    private static final String AUDIT_LOG_SEARCH_RES = "Audit Log";
    private static final String VALIDATION_ERROR = "Validation Error";
    /**
     * Data Object that holds the search results 
     */
    private int searchSize = 0;
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String errorMessage = null;
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    /**
     * Results to be displayed on the JSP
     */
    ArrayList resultsArrayList = new ArrayList();

    private ArrayList keysList  = new ArrayList();
    
    private ArrayList labelsList  = new ArrayList();
            
    /** Creates a new instance of AuditLogHandler */
    public AuditLogHandler() {
    }

    /**
     * @return System User
     */
    public String getSystemuser() {
        return systemuser;
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
    }

    /**
     * This method calls the service layer method MasterControllerService.lookupAuditLog to fetch the Audit Log Search results
     * The method builds the array of AuditDataObject to be displayed on the resulting JSF
     * @return AUDIT_LOG_SEARCH_RES the Navigation rule for the JSF framework
     * @throws com.sun.mdm.index.presentation.exception.HandlerException 
     */
    public ArrayList auditLogSearch() throws HandlerException {
        try {
            super.setUpdateableFeildsMap(parametersMap);

            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
               mLogger.info(mLocalizer.x("AUD009: Validation failed : {0}" , errorMessage));
                return null;
            }

            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = super.checkOneOfGroupCondition();
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = super.isRequiredCondition();
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }
            
            //Added by Sridhar to check the format of the user enter LID value adheres to the 
            //System defined LID format
            if((getUpdateableFeildsMap().get("LID") != null && getUpdateableFeildsMap().get("LID").toString().trim().length() > 0)) {
                if (!super.checkMasking((String)getUpdateableFeildsMap().get("LID"), (String)getUpdateableFeildsMap().get("lidmask"))) {
                    String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
                    String messages = localIdDesignation + " " + bundle.getString("lid_format_error_text") + " " +(String)getUpdateableFeildsMap().get("lidmask");
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages,messages));
                   return null;
                }                  
            }
            
            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = bundle.getString("LID_only");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
               // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                mLogger.info(mLocalizer.x("AUD010: {0} ",errorMessage ));
                return null;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                     errorMessage = bundle.getString("enter_LID");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                   // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                    mLogger.info(mLocalizer.x("AUD011: LID/SystemCode Validation failed : {0}" , errorMessage));
                    return null;

                }
            }


            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (LID.trim().length() > 0 && SystemCode.trim().length() > 0) {
                    try {
                        //remove masking for LID field
                        LID = LID.replaceAll("-", "");
                        SystemObject so = masterControllerService.getSystemObject(SystemCode, LID);
                        if (so == null) {
                            errorMessage = bundle.getString("system_object_not_found_error_message");
                            String msg = bundle.getString("LID_SYSTEM_CODE");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg + errorMessage, errorMessage));
                            mLogger.info(mLocalizer.x("AUD012: LID Validation failed : {0}", LID, errorMessage));
                            return null;
                        }
                    } 

                    catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("AUD015: Service Layer Validation Exception has occurred"), ex);
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("AUD016: Service Layer User Exception occurred"), ex);
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("AUD017: Error  occurred"), ex);
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        return null;
                    }

                }

            }

            //Validate all date fields entered by the user
            if (super.validateDateFields().size() > 0) {
                Object[] messObjs = super.validateDateFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(">>");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                  
                    mLogger.info(mLocalizer.x("AUD013: Invalid date format : {0} : {1}" ,fieldErrors[0],fieldErrors[1]));
                    return null;
                }

            }

            //Validate all time fields entered by the user
            if (super.validateTimeFields().size() > 0) {
                Object[] messObjs = super.validateTimeFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(">>");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));                   
                    mLogger.info(mLocalizer.x("AUD014: Invalid time format : {0} :{1}" ,fieldErrors[0],fieldErrors[1]));
                    return null;
                }

            }
            
            AuditSearchObject aso = this.getAuditSearchObject();
            if (aso == null) return null;
            
            // Lookup Audit log Controller
            AuditIterator alPageIter = masterControllerService.lookupAuditLog(aso);
            AuditIterator alPageIterOutput = masterControllerService.lookupAuditLog(aso);
        
            
            int i = 0;
            //Set the size of the VO Array
            setAuditLogVO(new AuditDataObject[alPageIter.count()]);
            
           SimpleDateFormat sdf = new SimpleDateFormat(ConfigManager.getDateFormat());
           SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
            //Populate the Value Object to be displayed on the JSF page.
            while (alPageIter.hasNext()) {
                auditLogVO[i] = new AuditDataObject(); //to be safe with malloc
                auditLogVO[i] = alPageIter.next();
                AuditDataObject auditDataObject = auditLogVO[i]; //to be safe with malloc

                /*String outputValues = "{AuditId:" + "\"" + auditDataObject.getId() +  "\"" + 
                                      ", EUID1: " + "\"" + ((auditDataObject.getEUID1() != null) ? auditDataObject.getEUID1() : "")  +"\"" +
                                      ", EUID2: " + "\"" + ((auditDataObject.getEUID2() != null) ? auditDataObject.getEUID2() : "") +"\""  +
                                      ", Function: " + "\"" + ((auditDataObject.getFunction()  != null) ? auditDataObject.getFunction()  : "") +"\"" +
                                      ", Detail: " + "\"" + ((auditDataObject.getDetail()  != null) ? auditDataObject.getDetail()  : "") +"\""  +
                                      ", CreateDate: " + "\"" + ((auditDataObject.getCreateDate()  != null) ? sdf.format(auditDataObject.getCreateDate())  : "") +"\""  +
                                      ", CreateUser: " + "\"" + ((auditDataObject.getCreateUser()  != null) ? auditDataObject.getCreateUser()  : "") +"\""  
                                     +  "}";
                */
                HashMap resultsMap = new HashMap();
                resultsMap.put("AUDITID",auditDataObject.getId());
                resultsMap.put("EUID1",auditDataObject.getEUID1());
                resultsMap.put("EUID2",auditDataObject.getEUID2());
                resultsMap.put("Function",auditDataObject.getFunction());
                resultsMap.put("Detail",auditDataObject.getDetail());
                resultsMap.put("PrimaryObjectType",auditDataObject.getPrimaryObjectType());
                resultsMap.put("CreateDate",simpleDateFormatFields.format(auditDataObject.getCreateDate()));
                resultsMap.put("CreateUser",auditDataObject.getCreateUser());
                resultsArrayList.add(resultsMap);

                //Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + "Audit Log Handler EUID is " + this.getEuid());
                i++;
            }
            
        } catch (ValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  exceptionMessaage,exceptionMessaage));
            mLogger.error(mLocalizer.x("AUD001: ValidationException has encountered : {0}" , ex.getMessage()),QwsUtil.getRootCause(ex));
            //mLogger.error("ValidationException ex : " + ex.toString());
            return null;
        } 

        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("AUD002: Service Layer Validation Exception has occurred"), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("AUD003: Service Layer User Exception occurred"), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("AUD004: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
             return null;
        }
        return resultsArrayList;
    }

    /**
     * @exception ValidationException when entry is not valid.
     * This function validates the user input and builds the search object
     * @return  the Audit search object
     */
    public AuditSearchObject getAuditSearchObject() throws ValidationException {
        AuditSearchObject auditSearchObject = new AuditSearchObject();

        //if user enters LID and SystemCode get the EUID and set it to the AuditSearchObject
        if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
            String LID = (String) super.getUpdateableFeildsMap().get("LID");
            String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
            if (LID.trim().length() > 0 && SystemCode.trim().length() > 0) {
                try {
                    //remove masking for LID field
                    LID = LID.replaceAll("-", "");

                    SystemObject so = masterControllerService.getSystemObject(SystemCode, LID);
                    if (so == null) {
                        errorMessage = bundle.getString("system_object_not_found_error_message");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                       // mLogger.error("LID/SYSTEM CODE:: " + errorMessage);
                        mLogger.info(mLocalizer.x("AUD006: LID/SYSTEM CODE : {0}" ,errorMessage));
                        
                    } else {
                        EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                        auditSearchObject.setEUID(eo.getEUID());
                    }
                } 
//                catch (ProcessingException ex) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  exceptionMessaage, exceptionMessaage));
//                    //mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
//                    //mLogger.error("ProcessingException ex : " + ex.toString());
//                   mLogger.error(mLocalizer.x("AUD007: ProcessingException has encountered : {0}" , ex.getMessage()),QwsUtil.getRootCause(ex));
//                   
//                } catch (UserException ex) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, exceptionMessaage));
//                   // mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
//                    //mLogger.error("UserException ex : " + ex.toString());
//                     mLogger.error(mLocalizer.x("AUD008: UserException has encountered : {0}" , ex.getMessage()),QwsUtil.getRootCause(ex));
//                    
//                }
                catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("AUD007: Service Layer Validation Exception has occurred"), ex);
                    } else if (ex instanceof UserException) {
                        mLogger.error(mLocalizer.x("AUD008: Service Layer User Exception occurred"), ex);
                    } else if (!(ex instanceof ProcessingException)) {
                        mLogger.error(mLocalizer.x("AUD020: Error  occurred"), ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                }

            }

        }

        //set EUID VALUE IF lid/system code not supplied
          if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
              auditSearchObject.setEUID((String) super.getUpdateableFeildsMap().get("EUID"));
//          } else {
//              auditSearchObject.setEUID(null);
          }
        

        String startTime = (String) super.getUpdateableFeildsMap().get("StartTime");
        String searchStartDate = (String) super.getUpdateableFeildsMap().get("StartDate");
        if (startTime != null && startTime.trim().length() > 0) {
            //if only time fields are entered validate for the date fields 
            if ((searchStartDate != null && searchStartDate.trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_from");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                return null;
            }
        }

        //Set StartDate to the AuditSearchObject  
        if (super.getUpdateableFeildsMap().get("StartDate") != null && super.getUpdateableFeildsMap().get("StartDate").toString().trim().length() > 0) {            
            //append the time aling with date
            if (startTime != null && startTime.trim().length() > 0) {
                searchStartDate = searchStartDate + " " + startTime;
            } else {
                searchStartDate = searchStartDate + " 00:00:00";
            }

            Date date = DateUtil.string2Date(searchStartDate);
            if (date != null) {
                auditSearchObject.setCreateStartDate(new Timestamp(date.getTime()));
            }            
        }

        String endTime = (String) super.getUpdateableFeildsMap().get("EndTime");
        String searchEndDate = (String) super.getUpdateableFeildsMap().get("EndDate");
        if (endTime != null && endTime.trim().length() > 0) {
            //if only time fields are entered validate for the date fields 
            if ((searchEndDate != null && searchEndDate.trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_to");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                return null;
            }
        }

        //Set StartDate to the AuditSearchObject  
        if (super.getUpdateableFeildsMap().get("EndDate") != null && super.getUpdateableFeildsMap().get("EndDate").toString().trim().length() > 0) {
            //append the time aling with date
            if (endTime != null && endTime.trim().length() > 0) {
                searchEndDate = searchEndDate + " " + endTime;
            } else {
                searchEndDate = searchEndDate + " 23:59:59";
            }
            Date date = DateUtil.string2Date(searchEndDate);
            if (date != null) {
                auditSearchObject.setCreateEndDate(new Timestamp(date.getTime()));
            }
        }
        //EndTime=, StartTime=, EndDate=, StartDate=, Function=null, SystemUser=, SystemCode=null, LID=, EUID=
        if (super.getUpdateableFeildsMap().get("SystemUser") != null && super.getUpdateableFeildsMap().get("SystemUser").toString().trim().length() > 0) {
            auditSearchObject.setCreateUser((String) super.getUpdateableFeildsMap().get("SystemUser"));
        } else {
            auditSearchObject.setCreateUser(null);
        }

        if (super.getUpdateableFeildsMap().get("Function") != null && super.getUpdateableFeildsMap().get("Function").toString().trim().length() > 0) {
            auditSearchObject.setFunction((String) super.getUpdateableFeildsMap().get("Function"));
        } else {
            auditSearchObject.setFunction(null);
        }


        // Set to static values need clarification from prathiba
        //This will be revoked when login module is implemented.
        //auditSearchObject.setPageSize(ConfigManager.getInstance().getMatchingConfig().getItemPerSearchResultPage());
        //auditSearchObject.setMaxElements(ConfigManager.getInstance().getMatchingConfig().getMaxResultSize());


        //set max results and page size here
        auditSearchObject.setPageSize(super.getPageSize());
        auditSearchObject.setMaxElements(super.getMaxRecords());
        Date date = null;

        if (errorMessage != null && errorMessage.length() != 0) {
            throw new ValidationException(mLocalizer.t("AUD501: {0}",errorMessage));
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

    public String getCreateStartDate() {
        return createStartDate;
    }

    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }

    public String getCreateEndDate() {
        return createEndDate;
    }

    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    public String getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getLocalid() {
        return localid;
    }

    public void setLocalid(String localid) {
        this.localid = localid;
    }

    public String getEuid() {
        return euid;
    }

    public void setEuid(String euid) {
        this.euid = euid;
    }

    public ArrayList getKeysList() {
        ArrayList newArrayList = new ArrayList();
        newArrayList.add("AuditId");
        newArrayList.add("EUID1");
        newArrayList.add("EUID2");
        newArrayList.add("Function");
        newArrayList.add("Detail");
        newArrayList.add("CreateDate");
        newArrayList.add("CreateUser");
        return newArrayList;
    }

    public void setKeysList(ArrayList keysList) {
        this.keysList = keysList;
    }


    public ArrayList getLabelsList() {
        ArrayList newArrayList = new ArrayList();
        newArrayList.add("Audit Id");
        newArrayList.add("EUID1");
        newArrayList.add("EUID2");
        newArrayList.add("Function");
        newArrayList.add("Detail");
        newArrayList.add("CreateDate");
        newArrayList.add("CreateUser");
        return newArrayList;
    }

    public void setLabelsList(ArrayList labelsList) {
        this.labelsList = labelsList;
    }

    public HashMap getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
    }


}
