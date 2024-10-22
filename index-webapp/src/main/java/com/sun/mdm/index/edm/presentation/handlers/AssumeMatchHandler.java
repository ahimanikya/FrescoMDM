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
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
import com.sun.mdm.index.edm.presentation.valueobjects.AssumeMatchesRecords;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
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
//import com.sun.mdm.index.util.Logger;

//import com.sun.mdm.index.util.Localizer;
//import com.sun.mdm.index.edm.presentation.util.Localizer;
//import java.util.logging.Level;
//import net.java.hulp.i18n.LocalizationSupport;
//import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.presentation.security.Operations;

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
    /*
    * Map to hold the parameters
    **/
    private HashMap parametersMap  = new HashMap();

    MidmUtilityManager  midmUtilityManager  = new MidmUtilityManager();

    /**
     *Operations class used for implementing the security layer from midm-security.xml file
     */
    Operations operations = new Operations();
    
    /** Creates a new instance of AssumeMatchHandler */
    public AssumeMatchHandler() {
    }

    /**
     * This method calls the service layer method MasterControllerService.lookupAuditLog to fetch the Audit Log Search results
     * The method builds the array of AuditDataObject to be displayed on the resulting JSF
     * @return AUDIT_LOG_SEARCH_RES the Navigation rule for the JSF framework
     * @throws com.sun.mdm.index.presentation.exception.HandlerException 
     */
    public ArrayList  performSubmit() {
        session.removeAttribute("enterpriseArrayList");
        session.removeAttribute("previewAMEO");
        try {
            super.setUpdateableFeildsMap(getParametersMap());
            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());            
           // mLogger.error("Submitted HashMap by the UI: " + super.getUpdateableFeildsMap());
            mLogger.debug(mLocalizer.x("ASM001: Submitted HashMap by  UI: {0}" , super.getUpdateableFeildsMap()));
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("ASM002: {0}" ,errorMessage));
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
            
            //Added   to check the format of the user enter LID value adheres to the 
            //System defined LID format
            if((getUpdateableFeildsMap().get("LID") != null && getUpdateableFeildsMap().get("LID").toString().trim().length() > 0)) {
                if (!super.checkMasking((String)getUpdateableFeildsMap().get("LID"), (String)getUpdateableFeildsMap().get("lidmask"))) {
                    String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
                    String messages = localIdDesignation + " " + bundle.getString("lid_format_error_text") + " " +(String)getUpdateableFeildsMap().get("lidmask");
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages,messages));
                   ArrayList lidErrorList = new ArrayList();            
                   HashMap lidError = new HashMap();                   //fix of 6703149
                   lidError.put("LID_SYSTEM_CODE_ERROR",messages);
                   lidErrorList.add(lidError);
                   return lidErrorList;
                }                  
            }
            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = bundle.getString ("enter_LID");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("ASM003: LID/SystemCode Validation failed: Please Enter LID value" ));
               ArrayList lidErrorList = new ArrayList();            
               HashMap lidError = new HashMap();                   //fix of 6703149
               lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
               lidErrorList.add(lidError);
               return lidErrorList;
            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString ("enter_LID");//"Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("ASM004: LID/SystemCode Validation failed: Please Enter LID Value" ));
               ArrayList lidErrorList = new ArrayList();            
               HashMap lidError = new HashMap();                   //fix of 6703149
               lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
               lidErrorList.add(lidError);
               return lidErrorList;
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
                               ArrayList lidErrorList = new ArrayList();            
                               HashMap lidError = new HashMap();                   //fix of 6703149
                               lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
                               lidErrorList.add(lidError);
                               return lidErrorList;
                        }

                    } catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("ASM006: Service Layer Validation Exception has occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("ASM007: Service Layer User Exception occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("ASM009: Error  occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof ProcessingException) {
                            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                            if (exceptionMessage.indexOf("stack trace") != -1) {
                                String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                                if (exceptionMessage.indexOf("message=") != -1) {
                                    parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                                }
                                mLogger.error(mLocalizer.x("ASM010: Service Layer Processing Exception occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                            } else {
                                mLogger.error(mLocalizer.x("ASM011: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                            }
                        }
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
                    //mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                   mLogger.error(mLocalizer.x("ASM012: Date Validation failed :{0}:{1}:" ,fieldErrors[0],fieldErrors[1]));
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
                    //mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                     mLogger.error(mLocalizer.x("ASM013: Time Validation failed:{0}:{1}:" ,fieldErrors[0],fieldErrors[1]));
                    return null;
                }
            }
            
            //get the AssumedMatchSearchObject             
            AssumedMatchSearchObject amso = getAMSearchObject();
           
            if (amso == null ) return null;
            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(amso);
            ArrayList amArrayList = new ArrayList();
            if (amIter != null & amIter.count() > 0) {
                amIter.sortBy("EUID", false);
                assumeMatchesRecordsVO = new AssumeMatchesRecords[amIter.count()];
                int index = 0;
                ArrayList summaryList = new ArrayList();
                String prevEuid = "";
                int startPosition = 0;
                while (amIter.hasNext()) {
                    AssumedMatchSummary amSummary = (AssumedMatchSummary) amIter.next();
                    HashMap summaryHash = new HashMap();
                    startPosition++;
                   // EnterpriseObject beforeEO = amSummary.getBeforeEO
                     EnterpriseObject beforeEO = masterControllerService.getEnterpriseObject(amSummary.getEUID());
                    //Insert audit log for AM search results 
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            //beforeEO.getEUID(),
                            amSummary.getEUID(),
                            "",
                            "Assumed Match Search Result",
                            new Integer(screenObject.getID()).intValue(),
                            "View Assumed Match Search Result");
                    
                    EnterpriseObject afterEO = amSummary.getAfterEO();
                   
                    if ((index != 0 && !prevEuid.equalsIgnoreCase(amSummary.getEUID()))) {  //Boundary value condition 
                        //populate VO                            
                        amArrayList  = populateVO(summaryList, index);
                        resultsArray.add(amArrayList);   
                        summaryHash.clear();
                        summaryList.clear();
                        counter++;
                    }
                    summaryHash.put("summary", amSummary);
                    //summaryHash.put("before", beforeEO);
                    summaryHash.put("before", beforeEO);
                    summaryHash.put("after", afterEO);
                    summaryList.add(summaryHash);
                    prevEuid = amSummary.getEUID();
                    index++;                    
                }// end of while
                //Catch the last one here!
                amArrayList  = populateVO(summaryList, index);
                resultsArray.add(amArrayList);
                //Accumilate the arrayList here  
                setSearchSize(counter);
            }
            request.setAttribute("assumeMatchList", resultsArray);
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM014: Encountered the ValidationException: {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM015: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM016: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM017: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM018: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        
        }
        return resultsArray;
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
                        mLogger.error(mLocalizer.x("ASM019: LID/SYSTEM CODE validation failed:{0}" , errorMessage));

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

                } catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("ASM020: Service Layer Validation Exception has occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                    } else if (ex instanceof UserException) {
                        mLogger.error(mLocalizer.x("ASM021: Service Layer User Exception occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                    } else if (!(ex instanceof ProcessingException)) {
                        mLogger.error(mLocalizer.x("ASM022: Error  occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                    } else if (ex instanceof ProcessingException) {
                        String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                        if (exceptionMessage.indexOf("stack trace") != -1) {
                            String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                            if (exceptionMessage.indexOf("message=") != -1) {
                                parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                            }
                            mLogger.error(mLocalizer.x("ASM023: Service Layer Processing Exception occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                        } else {
                            mLogger.error(mLocalizer.x("ASM024: Error  occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                        }
                    }
                    return null;
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

        // added as fix of bug with Id 113 on 21-10-08
         if (((super.getUpdateableFeildsMap().get("create_start_date") != null) 
                    && (super.getUpdateableFeildsMap().get("create_start_date").toString().trim().length() > 0))&&
           ((super.getUpdateableFeildsMap().get("create_end_date") != null) 
                    && (super.getUpdateableFeildsMap().get("create_end_date").toString().trim().length() > 0))){                
               
               Date fromdate = DateUtil.string2Date(super.getUpdateableFeildsMap().get("create_start_date").toString() + ((super.getUpdateableFeildsMap().get("create_start_time") != null && super.getUpdateableFeildsMap().get("create_start_time").toString().trim().length() > 0)? " " +super.getUpdateableFeildsMap().get("create_start_time").toString():" 00:00:00"));
               Date todate = DateUtil.string2Date(super.getUpdateableFeildsMap().get("create_end_date").toString()+((super.getUpdateableFeildsMap().get("create_end_time") != null && super.getUpdateableFeildsMap().get("create_end_time").toString().trim().length() > 0)? " " +super.getUpdateableFeildsMap().get("create_end_time").toString():" 23:59:59"));
               long startDate = fromdate.getTime();
               long endDate = todate.getTime();
                 if(endDate < startDate){
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                    return null;
                   }
         }    

        
        String startTime = (String) super.getUpdateableFeildsMap().get("create_start_time");
        String searchStartDate = (String) super.getUpdateableFeildsMap().get("create_start_date");
        if (startTime != null && startTime.trim().length() > 0) {
            //if only time fields are entered validate for the date fields 
            if ((searchStartDate != null && searchStartDate.trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_from");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                return null;
            }
        }


        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("create_start_date") != null && super.getUpdateableFeildsMap().get("create_start_date").toString().trim().length() > 0) {
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

        String endTime = (String) super.getUpdateableFeildsMap().get("create_end_time");
        String searchEndDate = (String) super.getUpdateableFeildsMap().get("create_end_date");
        if (endTime != null && endTime.trim().length() > 0) {
            //if only time fields are entered validate for the date fields 
            if ((searchEndDate != null && searchEndDate.trim().length() == 0)) {
                errorMessage = bundle.getString("enter_date_to");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                return null;
            }
        }


        //create_start_time=, create_start_date=02/01/2008, EUID=, Status=null, create_end_time=, create_end_date=02/29/2008, SystemCode=null, LID=
        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("create_end_date") != null && super.getUpdateableFeildsMap().get("create_end_date").toString().trim().length() > 0) {
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
        EnterpriseObject eo  = null;
        EnterpriseObject before = null;
        EnterpriseObject after = null;
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
         
        //Array list of field configs
        ArrayList fcArrayList = super.getResultsConfigArray();
        String epathValue = new String();
       
        
        ArrayList amArrayList = new ArrayList();
        HashMap summaryMap = new HashMap();
        String strVal = new String();
        HashMap beforeMap = new HashMap();
        HashMap afterMap = new HashMap();
        try {
             ConfigManager.init();
            
            for (int j = 0; j < amList.size(); j++) { //Each Summary has Before and After

                HashMap hashMap = (HashMap) amList.get(j); //Values always are in 0th index
                
                if (j == 0) {
                    ams = (AssumedMatchSummary) hashMap.get("summary");
                    before = (EnterpriseObject) hashMap.get("before");
                    after = (EnterpriseObject) hashMap.get("after");
                    SystemObject assumedSystemObject = masterControllerService.getSystemObject(ams.getSystemCode(), ams.getLID());

                    //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
                    //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
                    boolean hasSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null ) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(assumedSystemObject.getObject()):true;
 
                    //get the system object of tht original assumed match from the before image of the EO
                    //Collect before and after at the 0th index
                        for (int i = 0; i < fcArrayList.size(); i++) {
                            FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                            //Continue the loop for the mandatory fields from midm.xml
                            if ("ID".equalsIgnoreCase(fieldConfig.getName())) {
                                afterMap.put(fieldConfig.getFullFieldName(), ams.getId());
                            } else if ("EUID".equalsIgnoreCase(fieldConfig.getName())) {
                                afterMap.put(fieldConfig.getFullFieldName(), ams.getEUID());
                            } else if ("LID".equalsIgnoreCase(fieldConfig.getName())) {
                                       afterMap.put(fieldConfig.getFullFieldName(), ams.getLID());
                            } else if ("SystemCode".equalsIgnoreCase(fieldConfig.getName())) {                                
                                 afterMap.put(fieldConfig.getFullFieldName(), masterControllerService.getSystemDescription(ams.getSystemCode()));
                            } else if ("Weight".equalsIgnoreCase(fieldConfig.getName())) {
                                afterMap.put(fieldConfig.getFullFieldName(), ams.getWeight());
                            } else if ("CreateUser".equalsIgnoreCase(fieldConfig.getName())) {
                                afterMap.put(fieldConfig.getFullFieldName(), ams.getCreateUser());
                            } else if ("CreateDate".equalsIgnoreCase(fieldConfig.getName())) {
                                afterMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(ams.getCreateDate()));
                            } else {
                                if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                                    epathValue = fieldConfig.getFullFieldName();
                                } else {
                                    epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                                }
                             // Add the date fields here
                            if (fieldConfig.getValueType() == 6) {
                                //afterMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                                //afterMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject())));
                                if (EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject()) != null && hasSensitiveData && !operations.isField_VIP() && fieldConfig.isSensitive()) { // Mask the sensitive fields accordingly

                                    afterMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                                } else {
                                    afterMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject())));
                                }

                            } else {
                                //Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                                Object value = EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject());
                                if (value != null && hasSensitiveData  && !operations.isField_VIP() && fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly                                  

                                    afterMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));

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
                                            afterMap.put(fieldConfig.getFullFieldName(), strVal);
                                        }
                                    } else if (fieldConfig.getInputMask() != null  && fieldConfig.getInputMask().length() > 0) {
                                        if (value != null) {
                                            //Mask the value as per the masking 
                                            value = fieldConfig.mask(value.toString());
                                            afterMap.put(fieldConfig.getFullFieldName(), value);
                                        }
                                    } else {
                                        afterMap.put(fieldConfig.getFullFieldName(), value);
                                    }
                                }


                            }
                            } // end SBR fields

                        }
                        amArrayList.add(afterMap);
                        afterMap = new HashMap();                        
                    //}

                } else {
                    ams = (AssumedMatchSummary) hashMap.get("summary");
                    after = (EnterpriseObject) hashMap.get("after");
                    
                    SystemObject assumedSystemObject = masterControllerService.getSystemObject(ams.getSystemCode(), ams.getLID());

                    //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
                    //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
                    boolean hasSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null ) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(assumedSystemObject.getObject()):true;

                    eo = after;
                    for (int i = 0; i < fcArrayList.size(); i++) {
                        FieldConfig fieldConfig = (FieldConfig) fcArrayList.get(i);
                        //Continue the loop for the mandatory fields from midm.xml
                        if ("ID".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), ams.getId());
                        } else if ("EUID".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), ams.getEUID());
                        } else if ("LID".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), ams.getLID());
                        } else if ("SystemCode".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), masterControllerService.getSystemDescription(ams.getSystemCode()));
                        } else if ("Weight".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), ams.getWeight());
                        } else if ("CreateUser".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), ams.getCreateUser());
                        } else if ("CreateDate".equalsIgnoreCase(fieldConfig.getName())) {
                            afterMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(ams.getCreateDate()));
                        } else {
                            if (fieldConfig.getFullFieldName().startsWith(screenObject.getRootObj().getName())) {
                                epathValue = fieldConfig.getFullFieldName();
                            } else {
                                epathValue = screenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                            }
                            // Add the date fields here
                            if (fieldConfig.getValueType() == 6) {
                                //afterMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject())));
                                //afterMap.put(fieldConfig.getFullFieldName(), sdf.format(EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject())));
                                if (EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject()) != null && hasSensitiveData  && !operations.isField_VIP() && fieldConfig.isSensitive()) { // Mask the sensitive fields accordingly

                                    afterMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                                } else {
                                    afterMap.put(fieldConfig.getFullFieldName(), simpleDateFormatFields.format(EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject())));
                                }

                            } else {
                                //Object value = EPathAPI.getFieldValue(epathValue, eo.getSBR().getObject());
                                Object value = EPathAPI.getFieldValue(epathValue, assumedSystemObject.getObject());
                                if (value != null && hasSensitiveData  && !operations.isField_VIP() && fieldConfig.isSensitive()) { //if the field is senstive then mask the value accordingly                                  

                                    afterMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));

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
                                            afterMap.put(fieldConfig.getFullFieldName(), strVal);
                                        }
                                    } else if (fieldConfig.getInputMask() != null  && fieldConfig.getInputMask().length() > 0) {
                                        if (value != null) {
                                            //Mask the value as per the masking 
                                            value = fieldConfig.mask(value.toString());
                                            afterMap.put(fieldConfig.getFullFieldName(), value);
                                        }
                                    } else {
                                        afterMap.put(fieldConfig.getFullFieldName(), value);
                                    }
                                }


                            }
                        }  // end SBR fields

                    }
                    amArrayList.add(afterMap);
                    afterMap = new HashMap();
                }
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM025: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM026: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM027: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM028: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM029: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
        //amArrayList.add(beforeMap);
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
     * This method provides functionality to preview/simulate the UNDO on an existing Assumed Match
     * @param event 
     * @exception ProcessingException An error has occured.
     * @exception UserException Invalid id
     */
    public HashMap previewUndoAssumedMatch(String assumedMatchId) {
        HashMap previewAMEO = null;
        try {
            EnterpriseObject newEO = masterControllerService.previewUndoAssumedMatch(assumedMatchId);
            
            //Insert audit log for preview assumed match
            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               newEO.getEUID(), 
                                               "",
                                               "Assumed Match Comparison",
                                               new Integer(screenObject.getID()).intValue(),
                                               "Compare the selected EUID of the Assumed Match Search Result (Preview)");
            
            previewAMEO = midmUtilityManager.getEnterpriseObjectAsHashMap(newEO, screenObject);
            httpRequest.setAttribute("undoAssumedMatchId", assumedMatchId);
            httpRequest.setAttribute("previewAMEO", previewAMEO);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM030: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM031: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM032: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM033: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM034: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
        return previewAMEO;
    }

    /**
     * 
     * @param event
     */
    public String undoMatch(String assumedMatchId) {
        try {
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
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(amPreviewEnterpriseObject, screenObject);
                euidsMapList.add(eoMap);
            }             
            httpRequest.setAttribute("comapreEuidsArrayList", euidsMapList);
           return amEuid;

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM035: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM036: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM037: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM038: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM039: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }

    public ArrayList getEOList(String assumedMatchEUID) {
        ArrayList eoArrayList = new ArrayList();
        try {
            EnterpriseObject eo  = masterControllerService.getEnterpriseObject(assumedMatchEUID);
            if(eo == null ) return null;
            //get the enterprise object for the assumed match EUID        
            HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            //put Assumed match summary System/LID in the hashmap
            eoArrayList.add(eoMap);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM040: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM041: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM042: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM043: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM044: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
        return eoArrayList;

    }
    
    public HashMap getAssumedHashMap(String assumedMatchEUID) {
        HashMap amEOHashMap = new HashMap();
        try {
            AssumedMatchSearchObject aso = new AssumedMatchSearchObject();
            aso.setPageSize(super.getPageSize());
            aso.setMaxElements(super.getMaxRecords());
            //aso.setAssumedMatchId(assumedMatchId);
            aso.setEUID(assumedMatchEUID);

            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(aso);
            if (amIter != null & amIter.count() > 0) {
                amIter.sortBy("EUID", false);

                while (amIter.hasNext()) {
                    AssumedMatchSummary amSummary = (AssumedMatchSummary) amIter.next();
                    amEOHashMap.put("amID"+amSummary.getSystemCode()+":"+amSummary.getLID(), amSummary.getId());                   
                }
            }
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("ASM045: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("ASM046: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("ASM047: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("ASM048: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("ASM049: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
        }
        return amEOHashMap;
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
     
/*     
    public ArrayList getAssumeMatchesByTransaction(String transID) throws ValidationException, Exception   {
            //get the AssumedMatchSearchObject             
            AssumedMatchSearchObject amso = getAMSearchObject();
            
            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(amso);
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
                    session.setAttribute("amId", amHashMap);//set am id in the session.
                }// end of while
                //Accumilate the arrayList here  
                setSearchSize(counter);
            }        
            return null;
    }
*/
    public HashMap getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
    }

    

}
