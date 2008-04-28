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
 * AssumeMatchHandler.java 
 * Created on October 17, 2007, 04:45 PM
 * Authors : Pratibha, Sridhar Narsingh, Rajani Kanth
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.presentation.valueobjects.AssumeMatchesRecords;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;

//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;

//import com.sun.mdm.index.util.Localizer;
//import com.sun.mdm.index.edm.presentation.util.Localizer;
//import java.util.logging.Level;
//import net.java.hulp.i18n.LocalizationSupport;
//import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
//import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;

public class AssumeMatchHandler extends ScreenConfiguration {

    private static final String ASSUMEMATCHRECORD = "assumeRecord";
    private static final String VALIDATION_ERROR = "validationError";
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler");
     private static transient final Localizer mLocalizer = Localizer.get();
    //private static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler");
    private AssumeMatchesRecords[] assumeMatchesRecordsVO = null;
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    /*
     *  Request Object Handle
     */  
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    int totalFields = 0;
    int countMenuFields = 0;
    int countEmptyFields = 0;
    private int searchSize = -1;
    private int counter = 0;

    //Arraylist to display EDM driven search results.
    private FieldConfig[] searchResultsConfigArray;
    
    Object objFirstName = null;
    Object objLastName = null;
    private String errorMessage;
    ArrayList resultsArray  = new ArrayList();

    CompareDuplicateManager  compareDuplicateManager  = new CompareDuplicateManager();
    /** Creates a new instance of AssumeMatchHandler */
    public AssumeMatchHandler() {
    }

    /**
     * This method calls the service layer method MasterControllerService.lookupAuditLog to fetch the Audit Log Search results
     * The method builds the array of AuditDataObject to be displayed on the resulting JSF
     * @return AUDIT_LOG_SEARCH_RES the Navigation rule for the JSF framework
     * @throws com.sun.mdm.index.presentation.exception.HandlerException 
     */
    public String performSubmit() {
        session.removeAttribute("enterpriseArrayList");
        session.removeAttribute("previewAMEO");
        try {
            HashMap newFieldValuesMap = new HashMap();

            if (super.getEnteredFieldValues() != null && super.getEnteredFieldValues().length() > 0) {
                String[] fieldNameValues = super.getEnteredFieldValues().split(">>");
                for (int i = 0; i < fieldNameValues.length; i++) {
                    String string = fieldNameValues[i];
                    String[] keyValues = string.split("##");
                    if(keyValues.length ==2) {
                      newFieldValuesMap.put(keyValues[0], keyValues[1]);
                    }
                }
            }
           
            super.setUpdateableFeildsMap(newFieldValuesMap);


            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            
           // mLogger.error("Submitted HashMap by the UI: " + super.getUpdateableFeildsMap());
            mLogger.debug(mLocalizer.x("ASM001: Submitted HashMap by  UI: {0}" , super.getUpdateableFeildsMap()));
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("ASM002: {0}" ,errorMessage));
                return VALIDATION_ERROR;
            }

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = bundle.getString ("enter_LID");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("ASM003: LID/SystemCode Validation failed: Please Enter LID value" ));
                return VALIDATION_ERROR;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString ("enter_LID");//"Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("ASM004: LID/SystemCode Validation failed: Please Enter LID Value" ));
                    return VALIDATION_ERROR;

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
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                            mLogger.info(mLocalizer.x("ASM005: LID/SYSTEM CODE Validation failed : {0}" ,errorMessage));
                            return VALIDATION_ERROR;
                        }
                    } catch (ProcessingException ex) {
                       // mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                       // mLogger.error("ProcessingException ex : " + ex.toString());
                        mLogger.error(mLocalizer.x("ASM006: Encountered the  ProcessingException: {0} :{1}:" ,ex.getMessage() ,QwsUtil.getRootCause(ex).getMessage()),ex);
                        //throw new ProcessingException(mLocalizer.t("AMH102:ProcessingException ex : {0}" , ex.toString()));
                        return VALIDATION_ERROR;
                    } catch (UserException ex) {
                        mLogger.error(mLocalizer.x("ASM007: Encountered the  UserException : {0}:{1}" ,ex.getMessage(),QwsUtil.getRootCause(ex).getMessage()),ex);
                       // throw new UserException(mLocalizer.t("AMH104:UserException ex : {0}" , ex.toString()));
                        //mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                        //mLogger.error("UserException ex : " + ex.toString());
                        return VALIDATION_ERROR;
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
                    //mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                   mLogger.error(mLocalizer.x("ASM008: Date Validation failed :{0}:{1}:" ,fieldErrors[0],fieldErrors[1]));
                    return VALIDATION_ERROR;
                }

            }

            //Validate all time fields entered by the user
            if (super.validateTimeFields().size() > 0) {
                Object[] messObjs = super.validateTimeFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(">>");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    //mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                     mLogger.error(mLocalizer.x("ASM009: Time Validation failed:{0}:{1}:" ,fieldErrors[0],fieldErrors[1]));
                    return VALIDATION_ERROR;
                }

            }
            
            


            //get the AssumedMatchSearchObject             
            AssumedMatchSearchObject amso = getAMSearchObject();
           // mLogger.error("amso ==>: " + amso);

            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(amso);
            //mLogger.error(">>> amIter.count " + amIter.count());

            ArrayList amArrayList = new ArrayList();
            if (amIter != null & amIter.count() > 0) {
                amIter.sortBy("EUID", false);
                assumeMatchesRecordsVO = new AssumeMatchesRecords[amIter.count()];
                int index = 0;
                ArrayList summaryList = new ArrayList();
                String prevEuid = "";
                ArrayList eoArrayList = new ArrayList();
                HashMap amHashMap = new HashMap();
                HashMap summaryHash = new HashMap();
                int startPosition = 0;
                while (amIter.hasNext()) {
                    AssumedMatchSummary amSummary = (AssumedMatchSummary) amIter.next();
                    startPosition++;
                    EnterpriseObject beforeEO = amSummary.getBeforeEO();
                    eoArrayList.add(beforeEO);
                    amHashMap.put(beforeEO.getEUID(), amSummary.getId()); // set the assumed match id in the hashmap                        
                    //Insert audit log for AM search results 
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            beforeEO.getEUID(),
                            "",
                            "Assumed Match Search Result",
                            new Integer(screenObject.getID()).intValue(),
                            "View Assumed Match Search Result");
                    
                    amHashMap.put("SystemCode", amSummary.getSystemCode()); // set the System code in the hashmap
                    EnterpriseObject afterEO = amSummary.getAfterEO();
                    eoArrayList.add(afterEO);
                    summaryHash.put("summary", amSummary);
                    summaryHash.put("before", beforeEO);
                    summaryHash.put("after", afterEO);
                    summaryList.add(summaryHash);
                    if ((index != 0 && !prevEuid.equalsIgnoreCase(amSummary.getEUID())) || index + 1 == amIter.count()) {  //Boundary value condition 
                        //populate VO                            
                        amArrayList  = populateVO(summaryList, index);
                        resultsArray.add(amArrayList);   
                        summaryHash.clear();
                        summaryList.clear();
                        counter++;
                    }
                    prevEuid = amSummary.getEUID();
                    index++;

                    //session.setAttribute("enterpriseArrayList", eoArrayList); //set the array of EO's (Before and After ) in the session
                    session.setAttribute("amId", amHashMap);//set am id in the session.
                }// end of while
                
                //Accumilate the arrayList here  
                

                setSearchSize(counter);
            }
            request.setAttribute("assumeMatchList", resultsArray);
        } catch (Exception ex) {
            // UserException and ValidationException don't need a stack trace.
            // ProcessingException stack trace logged by MC
            if (ex instanceof ValidationException) {
                //mLogger.error("Validation failed. Message displayed to the user: " + QwsUtil.getRootCause(ex).getMessage());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                mLogger.error(mLocalizer.x("ASM010: Encountered the ValidationException: {0}" ,ex.getMessage()),ex);
                return VALIDATION_ERROR;
            } else if (ex instanceof UserException) {
                //mLogger.error("UserException. Message displayed to the user: " + QwsUtil.getRootCause(ex).getMessage());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                mLogger.error(mLocalizer.x("ASM011: Encountered the  UserException: {0}",ex.getMessage() ),ex);
                return ("ProcessingException");
            } else if (!(ex instanceof ProcessingException)) {
                //mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                //mLogger.error("ProcessingException ex : " + ex.toString());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                //ex.printStackTrace();
                mLogger.error(mLocalizer.x("ASM012: Encountered the  ProcessingException: {0}" ,ex.getMessage() ),ex);
                return ("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else if (!(ex instanceof PageException)) {
                //mLogger.error("PageException : " + QwsUtil.getRootCause(ex).getMessage());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                 mLogger.error(mLocalizer.x("ASM013: Encountered the  PageException:{0}" ,ex.getMessage() ),ex);
                return ("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else if (!(ex instanceof RemoteException)) {
                //mLogger.error("RemoteException : " + QwsUtil.getRootCause(ex).getMessage());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
               mLogger.error(mLocalizer.x("ASM014: Encountered the  RemoteException:{0}" ,ex.getMessage() ),ex);
                return ("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else {
                mLogger.error(mLocalizer.x("ASM015: Encountered the  Exception:{0}" ,ex.getMessage() ),ex);
                //mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                return ("ProcessingException");
            }
        }
        return ASSUMEMATCHRECORD;
    }

    /**
     * @exception ValidationException when entry is not valid.
     * @throws java.lang.Exception 
     * @todo Document: Getter for PDSearchObject attribute of the SearchForm
     *      object
     * @return  the AssumedMatchSearchObject
     */
    public AssumedMatchSearchObject getAMSearchObject() throws ValidationException, Exception {

        AssumedMatchSearchObject amso = new AssumedMatchSearchObject();

        //if user enters LID and SystemCode get the EUID and set it to the amso
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
                        mLogger.error(mLocalizer.x("ASM016: LID/SYSTEM CODE validation failed:{0}" , errorMessage));

                    } else {
                        EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                        //amso.setEUID(eo.getEUID());
                        String[] euidArray = getStringEUIDs(eo.getEUID());

                        if (euidArray != null & euidArray.length > 0) {
                            amso.setEUIDs(euidArray);
                        } else {
                            amso.setEUIDs(null);
                        }
                    }
                } catch (ProcessingException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  exceptionMessaage, exceptionMessaage));
                    //mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                    //mLogger.error("ProcessingException ex : " + ex.toString());
                     mLogger.error(mLocalizer.x("ASM017: Encountered the  ProcessingException: {0}" ,ex.getMessage() ),ex);
                } catch (UserException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, exceptionMessaage));
                    //mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                   // mLogger.error("UserException ex : " + ex.toString());
                     mLogger.error(mLocalizer.x("ASM018: Encountered the  UserException : {0}",ex.getMessage() ),ex);
                }

            }

        }

        //set EUID VALUE IF lid/system code not supplied
          if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
            // Get array of strings
            if(super.getUpdateableFeildsMap().get("EUID") != null ) {
                String[] euidArray = getStringEUIDs((String) super.getUpdateableFeildsMap().get("EUID"));
                if(euidArray!=null & euidArray.length >0) {
                    amso.setEUIDs(euidArray);
                } else {
                    amso.setEUIDs(null);
                }
            }
          }



        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("create_start_date") != null && super.getUpdateableFeildsMap().get("create_start_date").toString().trim().length() > 0) {
            String startTime = (String) super.getUpdateableFeildsMap().get("create_start_time");
            String searchStartDate = (String) super.getUpdateableFeildsMap().get("create_start_date");
            //append the time aling with date
            if (startTime != null && startTime.trim().length() > 0) {
                searchStartDate = searchStartDate + " " + startTime;
            } else {
                searchStartDate = searchStartDate + " 00:00:00";
            }

            Date date = DateUtil.string2Date(searchStartDate);
            if (date != null) {
                amso.setCreateStartDate(new Timestamp(date.getTime()));
            }
        }


        //create_start_time=, create_start_date=02/01/2008, EUID=, Status=null, create_end_time=, create_end_date=02/29/2008, SystemCode=null, LID=
        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("create_end_date") != null && super.getUpdateableFeildsMap().get("create_end_date").toString().trim().length() > 0) {
            String endTime = (String) super.getUpdateableFeildsMap().get("create_end_time");
            String searchEndDate = (String) super.getUpdateableFeildsMap().get("create_end_date");
            //append the time aling with date
            if (endTime != null && endTime.trim().length() > 0) {
                searchEndDate = searchEndDate + " " + endTime;
            } else {
                searchEndDate = searchEndDate + " 23:59:59";
            }
            Date date = DateUtil.string2Date(searchEndDate);
            if (date != null) {
                amso.setCreateEndDate(new Timestamp(date.getTime()));
            }
        }
        //EndTime=, StartTime=, EndDate=, StartDate=, Function=null, SystemUser=, SystemCode=null, LID=, EUID=
        if (super.getUpdateableFeildsMap().get("SystemUser") != null && super.getUpdateableFeildsMap().get("SystemUser").toString().trim().length() > 0) {
            amso.setCreateUser((String) super.getUpdateableFeildsMap().get("SystemUser"));
        } else {
            amso.setCreateUser(null);
        }
        
        //set max records and page size
        amso.setPageSize(super.getPageSize());
        amso.setMaxElements(super.getMaxRecords());

        if (errorMessage != null && errorMessage.length() != 0) {
            //throw new ValidationException(errorMessage);
            throw new ValidationException(mLocalizer.t("ASM502: {0} ",errorMessage));
        } else {
            return amso;
        }
    }

    private ArrayList  populateVO(ArrayList amList, int offset) throws ObjectException, EPathException {
        //for (int i=0; i < assumeMatchesRecordsVO.length;i++) {
        AssumedMatchSummary ams = new AssumedMatchSummary();
        HashMap newValuesMap  = new HashMap();
        EnterpriseObject before = null;
        EnterpriseObject eo = before;
        SimpleDateFormat sdf = new SimpleDateFormat(ConfigManager.getDateFormat());

        //Array list of field configs
        ArrayList fcArrayList = super.getResultsConfigArray();
        String epathValue = new String();
        
        ArrayList amArrayList = new ArrayList();
        HashMap beforeMap = new HashMap();
        HashMap afterMap = new HashMap();
        HashMap summaryMap = new HashMap();
        String strVal = new String();
        for (int j = 0; j < amList.size(); j++) { //Each Summary has Before and After
            HashMap hashMap = (HashMap) amList.get(j); //Values always are in 0th index
            if (j == 0) {
                ams = (AssumedMatchSummary) hashMap.get("summary");
                eo = (EnterpriseObject) hashMap.get("before");
                beforeMap.put("ID", ams.getId());
                beforeMap.put("EUID", ams.getEUID());
                beforeMap.put("LID", ams.getLID());
                beforeMap.put("SystemCode", masterControllerService.getSystemDescription(ams.getSystemCode()));
                beforeMap.put("Weight", ams.getWeight());
                beforeMap.put("CreateUser", ams.getCreateUser());
                beforeMap.put("CreateDate", sdf.format(ams.getCreateDate()));

                for (int i = 0; i < fcArrayList.size(); i++) {
                    FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                    if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                        epathValue = fieldConfig.getFullFieldName();
                    } else {
                        epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                    }
                    if (fieldConfig.isUpdateable()) {
                        if (fieldConfig.getValueType() == 6) {
                            beforeMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                        } else {
                            Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                if (value != null) {
                                    strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    beforeMap.put(fieldConfig.getFullFieldName(), strVal);
                                }
                            } else {
                                beforeMap.put(fieldConfig.getFullFieldName(), value);
                            }
                        }
                    }
                }

                afterMap.put("ID", "");
                afterMap.put("EUID", "");
                afterMap.put("LID", "");
                afterMap.put("SystemCode", "");
                afterMap.put("Weight", "");
                afterMap.put("CreateUser", "");
                afterMap.put("CreateDate", "");

                eo = (EnterpriseObject) hashMap.get("after");
                for (int i = 0; i < fcArrayList.size(); i++) {
                    FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                    if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                        epathValue = fieldConfig.getFullFieldName();
                    } else {
                        epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                    }

                    if (fieldConfig.isUpdateable()) {
                        if (fieldConfig.getValueType() == 6) {
                            afterMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
//                                 String dupList = (String) beforeMap.get(fieldConfig.getFullFieldName());
//                                 dupList += "," + sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
//                                 beforeMap.put(fieldConfig.getFullFieldName(),dupList + "}]");
                        } else {
                            //afterMap.put(fieldConfig.getFullFieldName(), EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
                            Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                if (value != null) {
                                    strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    afterMap.put(fieldConfig.getFullFieldName(), strVal);
                                }
                            } else {
                                afterMap.put(fieldConfig.getFullFieldName(), value);
                            }
//                                 String dupList = (String) beforeMap.get(fieldConfig.getFullFieldName());
//                                 dupList += "," + (String) (EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
//                                 beforeMap.put(fieldConfig.getFullFieldName(),dupList + "}]");
                        }
                    }
                }
            } else {
                ams = (AssumedMatchSummary) hashMap.get("summary");
                eo = (EnterpriseObject) hashMap.get("after");
                summaryMap.put("ID", "");
                summaryMap.put("EUID", "");
                summaryMap.put("LID", "");
                summaryMap.put("SystemCode", "");
                summaryMap.put("Weight", "");
                summaryMap.put("CreateUser", "");
                summaryMap.put("CreateDate", "");


                for (int i = 0; i < fcArrayList.size(); i++) {
                    FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                    if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                        epathValue = fieldConfig.getFullFieldName();
                    } else {
                        epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                    }

                    if (fieldConfig.isUpdateable()) {
                        if (fieldConfig.getValueType() == 6) {
                            summaryMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
//                                 String dupList = (String) beforeMap.get(fieldConfig.getFullFieldName());
//                                 dupList += "," + sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
//                                 beforeMap.put(fieldConfig.getFullFieldName(),dupList + "}]");
                        } else {
                            //summaryMap.put(fieldConfig.getFullFieldName(), EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
                            Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                if (value != null) {
                                    strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    summaryMap.put(fieldConfig.getFullFieldName(), strVal);
                                }
                            } else {
                                summaryMap.put(fieldConfig.getFullFieldName(), value);
                            }
//                                 String dupList = (String) beforeMap.get(fieldConfig.getFullFieldName());
//                                 dupList += "," + (String) (EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject()));
//                                 beforeMap.put(fieldConfig.getFullFieldName(),dupList +"}]");
                        }
                    }
                }
            }
        }
        amArrayList.add(beforeMap);
        amArrayList.add(afterMap);
        //amArrayList.add(summaryMap);
        return amArrayList;
    }

    public AssumeMatchesRecords[] getAssumeMatchesRecordsVO() {
        return assumeMatchesRecordsVO;
    }

    public void setAssumeMatchesRecordsVO(AssumeMatchesRecords[] assumeMatchesRecordsVO) {
        this.assumeMatchesRecordsVO = assumeMatchesRecordsVO;
    }

    public FieldConfig[] getSearchResultsConfigArray() {

        ArrayList basicSearchFieldConfigs;
        FieldConfig[] newFcArray = null;
        try {
            ArrayList screenConfigArray = screenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = screenConfigArray.iterator();

            while (iteratorScreenConfig.hasNext()) {
                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                    //Build array of field configs from 
                    //searchResultsConfigArray = (FieldConfig[]) basicSearchFieldGroup.getFieldConfigs().toArray();
                    newFcArray = new FieldConfig[basicSearchFieldGroup.getFieldConfigs().size()];
                    Object[] fcObj = basicSearchFieldGroup.getFieldConfigs().toArray();
                    for (int i = 0; i < fcObj.length; i++) {
                        FieldConfig object = (FieldConfig) fcObj[i];
                        newFcArray[i] = object;
                    }
                }
            }
        } catch (Exception e) {
            //mLogger.error("Failed Get the Screen Config Array Object: ", e);
        }

        searchResultsConfigArray = newFcArray;
        return searchResultsConfigArray;
    }

    public void setSearchResultsConfigArray(FieldConfig[] searchResultsConfigArray) {
        this.searchResultsConfigArray = searchResultsConfigArray;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }

    /** 
     * This method provides functionality to preview/simulate the UNDo on an existing Assumed Match
     * @param event 
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid id
     */
    public void previewUndoAssumedMatch(ActionEvent event) {
        try {
            String assumedMatchId = (String) event.getComponent().getAttributes().get("previewamIdValueExpression");
            ArrayList eoArrayList = (ArrayList) event.getComponent().getAttributes().get("eoArrayList");
            httpRequest.setAttribute("comapreEuidsArrayList", eoArrayList);

            EnterpriseObject newEO = masterControllerService.previewUndoAssumedMatch(assumedMatchId);

            //Insert audit log for preview assumed match
            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               newEO.getEUID(), 
                                               "",
                                               "Assumed Match Comparison",
                                               new Integer(screenObject.getID()).intValue(),
                                               "Compare the selected EUID of the Assumed Match Search Result (Preview)");
            
            HashMap previewAMEO = compareDuplicateManager.getEnterpriseObjectAsHashMap(newEO, screenObject);

            httpRequest.setAttribute("AMID", assumedMatchId);
            httpRequest.setAttribute("previewAMEO", previewAMEO);
        } catch (ProcessingException ex) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
               // mLogger.error("ProcessingException ex : " + ex.getMessage());
                mLogger.error(mLocalizer.x("ASM019: Encountered the  ProcessingException: {0}" ,ex.getMessage() ),ex); 

        } catch (UserException ex) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
                //mLogger.error("ProcessingException ex : " + ex.getMessage());
                mLogger.error(mLocalizer.x("ASM020: Encountered the  UserException : {0}",ex.getMessage() ),ex);   
        }
    }

    /**
     * 
     * @param event
     */
    public void undoMatch(ActionEvent event) {
        try {
            String assumedMatchId = (String) event.getComponent().getAttributes().get("previewamIdValueExpression");
            String amEuid = masterControllerService.undoAssumedMatch(assumedMatchId);
            
            ArrayList euidsMapList = new ArrayList();
            EnterpriseObject amPreviewEnterpriseObject = masterControllerService.getEnterpriseObject(amEuid);
            httpRequest.removeAttribute("previewAMEO");
            httpRequest.removeAttribute("amEoList");
            //Insert audit log for preview assumed match
            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               amEuid, 
                                               "",
                                               "Undo Assumed Match",
                                               new Integer(screenObject.getID()).intValue(),
                                               "Undo Assumed Match");

            if (amPreviewEnterpriseObject != null) {
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(amPreviewEnterpriseObject, screenObject);
                euidsMapList.add(eoMap);
            }
             
            //ArrayList newArrayList = new ArrayList();
            //newArrayList.add(amPreviewEnterpriseObject);

            httpRequest.setAttribute("comapreEuidsArrayList", euidsMapList);
        } catch (ProcessingException ex) {
           //java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
          mLogger.error(mLocalizer.x("ASM021: Encountered the  ProcessingException: {0}" ,ex.getMessage() ),ex);   
        } catch (UserException ex) {
            //java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
             mLogger.error(mLocalizer.x("ASM022: Encountered the  UserException : {0}",ex.getMessage() ),ex);
        }

    }

    public ArrayList getAssumedEOList(String assumedMatchId) {
        ArrayList eoArrayList = new ArrayList();
        CompareDuplicateManager compareDuplicateManager=new CompareDuplicateManager();
        try {
            AssumedMatchSearchObject aso = new AssumedMatchSearchObject();
            aso.setPageSize(super.getPageSize());
            aso.setMaxElements(super.getMaxRecords());
            aso.setAssumedMatchId(assumedMatchId);

            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(aso);
            if (amIter != null & amIter.count() > 0) {
                amIter.sortBy("EUID", false);
                while (amIter.hasNext()) {
                    AssumedMatchSummary amSummary = (AssumedMatchSummary) amIter.next();
                    if (amSummary.getBeforeEO() != null) {
                        //eoArrayList.add(amSummary.getBeforeEO());
                        HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(amSummary.getBeforeEO(), screenObject);
                        eoArrayList.add(eoMap);
                    }
                    if (amSummary.getAfterEO() != null) {
                        //eoArrayList.add(amSummary.getAfterEO());
                        HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(amSummary.getAfterEO(), screenObject);
                        eoArrayList.add(eoMap);
                    }
                }
            }
        } catch (PageException ex) {
                    mLogger.error(mLocalizer.x("ASM023: Encountered the  PageException:  {0}" ,ex.getMessage() ),ex);
        } catch (RemoteException ex) {
        mLogger.error(mLocalizer.x("ASM024: Encountered the  RemoteException:  {0}" ,ex.getMessage() ),ex);
        } catch (ProcessingException ex) {
                    mLogger.error(mLocalizer.x("ASM025: Encountered the  ProcessingException: {0}" ,ex.getMessage() ),ex); 
        } catch (UserException ex) {
                        mLogger.error(mLocalizer.x("ASM026: Encountered the  UserException : {0}",ex.getMessage() ),ex);
        }
        session.setAttribute("amEoList", eoArrayList);
        return eoArrayList;
    }
    
     public String[] getStringEUIDs(String euids) {

        StringTokenizer stringTokenizer = new StringTokenizer(euids, ",");
        String[] euidsArray = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            euidsArray[i] = new String(stringTokenizer.nextElement().toString());
            i++;
        }
        return euidsArray;
    }

    

}
