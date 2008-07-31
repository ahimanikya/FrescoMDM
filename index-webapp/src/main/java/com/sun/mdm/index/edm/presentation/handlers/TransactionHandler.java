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
 * TransactionHandler.java 
 * Created on October 18, 2007, 6:15PM
 * Author : Pratibha, Sridhar
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.page.PageException;
import java.rmi.RemoteException;
import javax.faces.event.*;
import javax.faces.event.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import com.sun.mdm.index.edm.presentation.valueobjects.Transaction;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;

import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import net.java.hulp.i18n.LocalizationSupport;

public class TransactionHandler extends ScreenConfiguration {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.TransactionHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
   
    /**
     * Data Object that holds the search results 
     */    
   private Transaction[] transactionsVO = null;    
   
   private int searchSize  = -1; 
   
    
   //Adding the following variable for getting the select options if the FieldConfig type is "Menu List"
    private ArrayList<SelectItem> selectOptions = new ArrayList();
    
    /**
     * JSF Naviagation String
     */       
    private  static final String TRANSACTIONS_PAGE ="transactions";
    
    private  static final String VALIDATION_ERROR ="Validation Error";
    
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    
    //private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.TransactionHandler");

    String errorMessage = new String();
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    private ArrayList keysList  = new ArrayList();
    
    private ArrayList labelsList  = new ArrayList();
/*
 * Map to hold the parameters
 **/
    private HashMap parametersMap  = new HashMap();

    /*
     * Map to hold the CONCURRENT_MOD_ERROR TEXT
     **/
    public static final String CONCURRENT_MOD_ERROR = "CONCURRENT_MOD_ERROR";
    
    /** Creates a new instance of TransactionHandler */

    public TransactionHandler() {
    }

    public ArrayList transactionSearch() throws HandlerException  {
        ArrayList resultsArrayList = new ArrayList();
        try {
            //parametersMap is set using the Ajax call
            super.setUpdateableFeildsMap(getParametersMap());
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                mLogger.info(mLocalizer.x("TRS001: {0}",errorMessage));
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
                 //errorMessage = "Please Enter System Code";
                errorMessage = bundle.getString("LID_only");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("TRS002: {0}",errorMessage));
                return null;
            }
            
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString("enter_LID");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("TRS003: {0}",errorMessage));
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
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                            mLogger.info(mLocalizer.x("TRS025: {0}",errorMessage));
                            return null;
                        }
                    } 

                    catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("TRS005: Service Layer Validation Exception has occurred"), ex);
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("TRS006: Service Layer User Exception occurred"), ex);
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("TRS080: Error  occurred"), ex);
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
                    mLogger.info(mLocalizer.x("TRS007: Validation failed :{0}:{1}",fieldErrors[0],fieldErrors[1]));
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
                    mLogger.info(mLocalizer.x("TRS008: Validation failed:{0}:{1}",fieldErrors[0],fieldErrors[1]));
                    return null;
                }
            }
            TransactionSearchObject tso = getTransactionSearchObject();
            if (tso == null) return null;
            
            MasterControllerService objMasterControllerService = new MasterControllerService();
            TransactionIterator iteratorTransaction = objMasterControllerService.lookupTransactionHistory(tso);
            setSearchSize(0);
            if (iteratorTransaction != null) {
                TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
                setTransactionsVO(new Transaction[tsArray.length]);
                for (int i = 0; i < tsArray.length; i++) {
                    TransactionSummary ts = tsArray[i];
                    getTransactionsVO()[i] = new Transaction(); //to be safe with malloc
                    /*String outputValues = "{TransactionNumber:" + "\"" + ts.getTransactionObject().getTransactionNumber() + "\"" +
                            ", EUID1: " + "\"" + ((ts.getTransactionObject().getEUID() != null) ? ts.getTransactionObject().getEUID() : "") + "\"" +
                            ", EUID2: " + "\"" + ((ts.getTransactionObject().getEUID2() != null) ? ts.getTransactionObject().getEUID2() : "") + "\"" +
                            ", LID: " + "\"" + ((ts.getTransactionObject().getLID() != null) ? ts.getTransactionObject().getLID() : "") + "\"" +
                            ", Function: " + "\"" + ((ts.getTransactionObject().getFunction() != null) ? ts.getTransactionObject().getFunction() : "") + "\"" +
                            ", SystemCode: " + "\"" + ((ts.getTransactionObject().getSystemCode() != null) ? masterControllerService.getSystemDescription(ts.getTransactionObject().getSystemCode()): "") + "\"" +
                            ", SystemUser: " + "\"" + ((ts.getTransactionObject().getSystemUser() != null) ? ts.getTransactionObject().getSystemUser() : "") + "\"" +
                            ", TimeStamp: " + "\"" + ((ts.getTransactionObject().getTimeStamp() != null) ? ts.getTransactionObject().getTimeStamp() : "") + "\"" + "}";
                     */
                    //Insert audit log here for Transaction search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"), 
                                                    ts.getTransactionObject().getEUID(), 
                                                    ts.getTransactionObject().getEUID1(), 
                                                    "History Search Result", 
                                                    new Integer(screenObject.getID()).intValue(), 
                                                    "View History Search Result");
                    HashMap resultsMap = new HashMap();
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"TransactionNumber", ts.getTransactionObject().getTransactionNumber());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"EUID", ts.getTransactionObject().getEUID());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"EUID2", ts.getTransactionObject().getEUID2());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"LID", ts.getTransactionObject().getLID());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"LID2", ts.getTransactionObject().getLID2());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"Function", ValidationService.getInstance().getDescription(ValidationService.CONFIG_MODULE_FUNCTION, ts.getTransactionObject().getFunction()) );
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"FunctionCode",ts.getTransactionObject().getFunction());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"SystemCode", ValidationService.getInstance().getSystemDescription(ts.getTransactionObject().getSystemCode()));
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"SystemUser", ts.getTransactionObject().getSystemUser());
                    resultsMap.put(screenObject.getRootObj().getName()+"."+"TimeStamp", ts.getTransactionObject().getTimeStamp());

                    resultsArrayList.add(resultsMap);
                }
                setTransactionsVO(transactionsVO);
                setSearchSize(transactionsVO.length);
                //request.setAttribute("searchSize", new Integer(transactionsVO.length));
                //request.setAttribute("resultsArrayList", resultsArrayList);
                return resultsArrayList;
            //  request.setAttribute("searchSize",new Integer(transactionsVO.length) );
            }
        }
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("TRS009: Service Layer Validation Exception has occurred"), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("TRS010: Service Layer User Exception occurred"), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("TRS011: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            return null;
        }
        return resultsArrayList;
    }
    
    public String performSubmit() throws HandlerException  {
        try {
           
            HashMap newFieldValuesMap = new HashMap();
            if (super.getEnteredFieldValues() != null && super.getEnteredFieldValues().length() > 0) {
                String[] fieldNameValues = super.getEnteredFieldValues().split(">>");
                for (int i = 0; i < fieldNameValues.length; i++) {
                    String string = fieldNameValues[i];
                    String[] keyValues = string.split("##");
                    if (keyValues.length == 2) {
                        newFieldValuesMap.put(keyValues[0], keyValues[1]);
                    }
                }
            }

            super.setUpdateableFeildsMap(newFieldValuesMap);


            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,  errorMessage));
                mLogger.info(mLocalizer.x("TRS001: {0}",errorMessage));
                return VALIDATION_ERROR;
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
            
            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                 //errorMessage = "Please Enter System Code";
                errorMessage = bundle.getString("LID_only");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("TRS002: {0}",errorMessage));
                return VALIDATION_ERROR;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString("LID_only");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("TRS003: {0}",errorMessage));
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
                            mLogger.info(mLocalizer.x("TRS025: {0}",errorMessage));
                            return VALIDATION_ERROR;
                        }

                    }catch  (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("TRS005: Service Layer Validation Exception has occurred"), ex);
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("TRS006: Service Layer User Exception occurred"), ex);
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("TRS081: Error  occurred"), ex);
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
                    mLogger.info(mLocalizer.x("TRS007: Validation failed :{0}:{1}",fieldErrors[0],fieldErrors[1]));
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
                    mLogger.info(mLocalizer.x("TRS008: Validation failed:{0}:{1}",fieldErrors[0],fieldErrors[1]));
                    return VALIDATION_ERROR;
                }

            }




            TransactionSearchObject tso = getTransactionSearchObject();

        

            MasterControllerService objMasterControllerService = new MasterControllerService();

            TransactionIterator iteratorTransaction = objMasterControllerService.lookupTransactionHistory(tso);
            ArrayList resultsArrayList = new ArrayList();
            setSearchSize(0);
            if (iteratorTransaction != null) {
                TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
                setTransactionsVO(new Transaction[tsArray.length]);
                for (int i = 0; i < tsArray.length; i++) {
                    TransactionSummary ts = tsArray[i];

                    getTransactionsVO()[i] = new Transaction(); //to be safe with malloc

                    String outputValues = "{TransactionNumber:" + "\"" + ts.getTransactionObject().getTransactionNumber() + "\"" +
                            ", EUID1: " + "\"" + ((ts.getTransactionObject().getEUID() != null) ? ts.getTransactionObject().getEUID() : "") + "\"" +
                            ", EUID2: " + "\"" + ((ts.getTransactionObject().getEUID2() != null) ? ts.getTransactionObject().getEUID2() : "") + "\"" +
                            ", LID: " + "\"" + ((ts.getTransactionObject().getLID() != null) ? ts.getTransactionObject().getLID() : "") + "\"" +
                            ", Function: " + "\"" + ((ts.getTransactionObject().getFunction() != null) ? ValidationService.getInstance().getDescription(ValidationService.CONFIG_MODULE_FUNCTION, ts.getTransactionObject().getFunction()) : "") + "\"" +
                            ", SystemCode: " + "\"" + ((ts.getTransactionObject().getSystemCode() != null) ? masterControllerService.getSystemDescription(ts.getTransactionObject().getSystemCode()): "") + "\"" +
                            ", SystemUser: " + "\"" + ((ts.getTransactionObject().getSystemUser() != null) ? ts.getTransactionObject().getSystemUser() : "") + "\"" +
                            ", TimeStamp: " + "\"" + ((ts.getTransactionObject().getTimeStamp() != null) ? ts.getTransactionObject().getTimeStamp() : "") + "\"" + "}";

                    //Insert audit log here for Transaction search
            
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"), 
                                                    ts.getTransactionObject().getEUID(), 
                                                    ts.getTransactionObject().getEUID1(), 
                                                    "History Search Result", 
                                                    new Integer(screenObject.getID()).intValue(), 
                                                    "View History Search Result");

                    resultsArrayList.add(outputValues);


                }
                setTransactionsVO(transactionsVO);
                setSearchSize(transactionsVO.length);
                request.setAttribute("searchSize", new Integer(transactionsVO.length));
                request.setAttribute("resultsArrayList", resultsArrayList);
            //  request.setAttribute("searchSize",new Integer(transactionsVO.length) );
            }

        }catch  (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("TRS012: Service Layer Validation Exception has occurred"), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("TRS013: Service Layer User Exception occurred"), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("TRS014: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            return VALIDATION_ERROR;
        }
        return TRANSACTIONS_PAGE;
                
    }

        
        public void test(EnterpriseObject eo,EPathArrayList resultFields) throws Exception
        {
                    Collection collectionSO = eo.getSystemObjects();
                   
                    Iterator it = collectionSO.iterator();
                    SystemObject so = null;
                    ArrayList resultArrayList= new ArrayList();                                  

                    while (it.hasNext()) {

                        so = (SystemObject) it.next();  
                        Collection fieldvalues;
                        for (int m = 0; m < resultFields.size(); m++) {
                            EPath ePath = resultFields.get(m);
                            fieldvalues = getFieldValue(so, ePath);
                            resultArrayList.add(fieldvalues);
                            if(fieldvalues!=null)
                            {   Object[] obj = fieldvalues.toArray();
                                
                            }    
                        }
                    } 

        
        }        
        
        
        
        
        public static Collection getFieldValue(ObjectNode objNode, EPath epath) throws Exception {
        try{
        int ePathIndicesCount = epath.getIndices().length;
        // the last parent object in the hierarchy will be located here
        String ePathObjectTag = epath.getTag(ePathIndicesCount - 2);
        Collection c = null;
        // check if the ePathObjectTag is one of the children of the objNode.
        if (isChild(objNode, ePathObjectTag)) {
            //Added by Pratibha
            c = QwsUtil.getValueForField(objNode, epath.getName(), null);
            if(c==null)
            {return null;}
            else
            {return c;}
            //Ends Here
        } else {    // Check the children using a depth-first search.
            ArrayList childNodes = objNode.pGetChildren();
            if (childNodes != null && childNodes.size() > 0) {
                Iterator childIter = childNodes.iterator();
                while (childIter.hasNext() && c == null) {
                    ObjectNode childNode = (ObjectNode) childIter.next();
                    c = getFieldValue(childNode, epath);
                }
            } else {
                return null;    // terminate search if no children are found
            }            
        }
        return c;
        }catch(Exception ex){
            mLogger.error(mLocalizer.x("TRS015: Failed to get field value: {0}",ex.getMessage()),ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error has occured" , ex.toString()));
            return null;
        }
    }
    
    /**
     * Checks if the ePathObjectTag belongs to an object which is a child of the
     * objNode parameter.
     * @param ePathObjectTag The tag of the epath object.
     * @param objNode The ObjectNode instance to check.
     * @return true if the ePathObjectTag belongs to a child of objNode, false
     * otherwise.
     */
    private static boolean isChild(ObjectNode objNode, String ePathObjectTag) {
        ArrayList allChildren = objNode.pGetChildren();
        //Added by Pratibha
        if (allChildren==null)
            return false;
        //Ends Here
        for (int i = 0; i < allChildren.size(); i++) {
            ObjectNode obj = (ObjectNode) allChildren.get(i);
            String childNodeTag = obj.pGetTag();
            if (ePathObjectTag.equalsIgnoreCase(childNodeTag)) {
                return true;
            }
        }
        return false;
    }
        public ArrayList UIResultsFields(ArrayList arlScreenConfig) throws Exception
        {            
            EPathArrayList arlResultFields = new EPathArrayList(); 
            SearchScreenConfig searchScreenConfig = null;
            ArrayList arlUIFields = null;
            ArrayList arlField = null;
            Iterator eFieldIterator = null;
            Iterator screenConfigIterator = arlScreenConfig.iterator();
            while (screenConfigIterator.hasNext()) {
                searchScreenConfig = (SearchScreenConfig) screenConfigIterator.next();
                                
                //SearchScreenOptions objSearchScreenOptions = searchScreenConfig.getOptions();
                arlField = searchScreenConfig.getFieldConfigs();
                eFieldIterator = arlField.iterator();
                    while (eFieldIterator.hasNext()) {
                        FieldConfig fc = (FieldConfig) eFieldIterator.next();
                        FieldConfigGroup objFieldConfigGroup  =  (FieldConfigGroup) eFieldIterator.next();
                        arlUIFields = objFieldConfigGroup.getFieldConfigs();
                    }
            }
            return arlUIFields;             
    }
        
// Method added to handle Service Layer dynamic result fields
       public EPathArrayList retrieveResultsFields(ArrayList arlResultsConfig) throws Exception         {            
            EPathArrayList arlResultFields = new EPathArrayList(); 
            SearchResultsConfig searchResultConfig = null;
            ArrayList arlEPaths = null;
            Iterator ePathsIterator = null;
            Iterator resultConfigIterator = arlResultsConfig.iterator();
            String objectRef = null;
            while (resultConfigIterator.hasNext()) {
                searchResultConfig = (SearchResultsConfig) resultConfigIterator.next();
                arlEPaths = searchResultConfig.getEPaths();
                ePathsIterator = arlEPaths.iterator();
                    while (ePathsIterator.hasNext()) {
                    String strEPath = (String) ePathsIterator.next();
                    //if(strEPath.equals("Person.SystemCode"))
                    //       strEPath="Person.LastName";
                    // copy EPath strings to the EPathArrayList
                    arlResultFields.add("Enterprise.SystemObject." + strEPath);
                    //
                    // POTENTIAL DUPLICATE-RELATED FIX
                    // retrieve the object reference eg, if the epath is is "Person.Address.City" this retrieves "Person".
                    if (objectRef == null) {
                        int index = strEPath.indexOf(".");
                        objectRef = strEPath.substring(0, index);
                    }
                    //
                    }
            // POTENTIAL DUPLICATE-RELATED FIX
            // Add an EUID field for the PotentialDuplicateAManager.  This is required.
            arlResultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");
            }
            return arlResultFields;
    }
       
    public ArrayList getTranscationDetails(String transactionNumber) {
        ArrayList eoArrayList = new ArrayList();
        try {
            
            MasterControllerService objMasterControllerService = new MasterControllerService();
            EnterpriseObjectHistory viewMergehist = objMasterControllerService.viewMergeRecords(transactionNumber);
            CompareDuplicateManager compareDuplicateManager=new CompareDuplicateManager();
            TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
            //set the transaction number to the transaction summary object
            transactionSearchObject.getTransactionObject().setTransactionNumber(transactionNumber);
            TransactionIterator iteratorTransaction = objMasterControllerService.lookupTransactionHistory(transactionSearchObject);
            TransactionIterator iteratorTransaction1 = iteratorTransaction;
            setSearchSize(0);
            if (iteratorTransaction != null) {
                TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
                for (int i = 0; i < tsArray.length; i++) {
                    TransactionSummary ts = tsArray[i];
                    String functionName = ts.getTransactionObject().getFunction();
                    if ("euidMerge".equalsIgnoreCase(functionName) ||  "euidMerge".equalsIgnoreCase(functionName)) {
                        if (viewMergehist != null) {
                            if (viewMergehist.getBeforeEO1() != null) {
                                //eoArrayList.add(viewMergehist.getBeforeEO1());
                                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO1(), screenObject);
                                eoArrayList.add(eoMap);
                            }
                            if (viewMergehist.getBeforeEO2() != null) {
                                //eoArrayList.add(viewMergehist.getBeforeEO2());
                                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO2(), screenObject);
                                eoArrayList.add(eoMap);
                            }
                            if (viewMergehist.getAfterEO2() != null) {
                                //eoArrayList.add(viewMergehist.getAfterEO2());
                                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getAfterEO2(), screenObject);
                                eoArrayList.add(eoMap);
                            }
                            if (ts.getEnterpriseObjectHistory().getAfterEO() != null) {
                            //eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO());
                            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(ts.getEnterpriseObjectHistory().getAfterEO(), screenObject);
                            eoArrayList.add(eoMap);
                            }
                        }

                    } else {
                        if (ts.getEnterpriseObjectHistory().getBeforeEO1() != null) {
                            //eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO1());
                            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(ts.getEnterpriseObjectHistory().getBeforeEO1(), screenObject);
                            eoArrayList.add(eoMap);
                        }
                        if (ts.getEnterpriseObjectHistory().getBeforeEO2() != null) {
                            //eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO2());
                            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(ts.getEnterpriseObjectHistory().getBeforeEO2(), screenObject);
                            eoArrayList.add(eoMap);
                        }

                        if (ts.getEnterpriseObjectHistory().getAfterEO() != null) {
                            //eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO());
                            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(ts.getEnterpriseObjectHistory().getAfterEO(), screenObject);
                            eoArrayList.add(eoMap);
                        }
                        if (ts.getEnterpriseObjectHistory().getAfterEO2() != null) {
                            //eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO2());
                            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(ts.getEnterpriseObjectHistory().getAfterEO2(), screenObject);
                            eoArrayList.add(eoMap);
                        }

                    }
                }
            }

        } 

        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("TRS016: Service Layer Validation Exception has occurred"), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("TRS017: Service Layer User Exception occurred"), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("TRS018: Error  occurred"), ex);
            } else if (ex instanceof ProcessingException) {

                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=")+8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("TRS090: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                    return null;
                } else {
                    mLogger.error(mLocalizer.x("TRS091: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                    return null;
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
             return null;
        }
        return eoArrayList;
         
        
    }
       
     public TransactionSearchObject getTransactionSearchObject() throws ProcessingException, ValidationException {
         // From and to date hardcoded here
         TransactionSearchObject transactionSearchObject = new TransactionSearchObject();

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
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                        mLogger.info(mLocalizer.x("TRS024: LID/SYSTEM CODE: {0} " , errorMessage));
                    } else {
                        EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                        transactionSearchObject.setEUID(eo.getEUID());
                    }
                } 

                catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("TRS019: Service Layer Validation Exception has occurred"), ex);
                    } else if (ex instanceof UserException) {
                        mLogger.error(mLocalizer.x("TRS020: Service Layer User Exception occurred"), ex);
                    } else if (!(ex instanceof ProcessingException)) {
                        mLogger.error(mLocalizer.x("TRS021: Error  occurred"), ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                }

            }

        }

        //set EUID VALUE IF lid/system code not supplied
            if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
                transactionSearchObject.setEUID((String) super.getUpdateableFeildsMap().get("EUID"));
//            } else {
//                transactionSearchObject.setEUID(null);
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

        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("StartDate") != null && super.getUpdateableFeildsMap().get("StartDate").toString().trim().length() > 0) {
            //append the time aling with date
            if (startTime != null && startTime.trim().length() > 0) {
                searchStartDate = searchStartDate + " " + startTime;
            } else {
                searchStartDate = searchStartDate + " 00:00:00";
            }

            Date date = DateUtil.string2Date(searchStartDate);
            if (date != null) {
                transactionSearchObject.setStartDate(new Timestamp(date.getTime()));

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
        
        //EndDate=02/27/2008, StartDate=02/01/2008, Function=null, SystemUser=, EndTime=, StartTime=
        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("EndDate") != null && super.getUpdateableFeildsMap().get("EndDate").toString().trim().length() > 0) {
            //append the time aling with date
            if (endTime != null && endTime.trim().length() > 0) {
                searchEndDate = searchEndDate + " " + endTime;
            } else {
                searchEndDate = searchEndDate + " 23:59:59";
            }
            Date date = DateUtil.string2Date(searchEndDate);
            if (date != null) {
                transactionSearchObject.setEndDate(new Timestamp(date.getTime()));
            }
        }
        //EndTime=, StartTime=, EndDate=, StartDate=, Function=null, SystemUser=, SystemCode=null, LID=, EUID=
        if (super.getUpdateableFeildsMap().get("SystemUser") != null && super.getUpdateableFeildsMap().get("SystemUser").toString().trim().length() > 0) {
            transactionSearchObject.setSystemUser((String) super.getUpdateableFeildsMap().get("SystemUser"));
        } else {
            transactionSearchObject.setSystemUser(null);
        }

         if (super.getUpdateableFeildsMap().get("Function") != null && super.getUpdateableFeildsMap().get("Function").toString().trim().length() > 0) {
            transactionSearchObject.setFunction((String) super.getUpdateableFeildsMap().get("Function"));
        } else {
            transactionSearchObject.setFunction(null);
        }
     
         //set Max records and page size here
         transactionSearchObject.setPageSize(super.getPageSize());
         transactionSearchObject.setMaxElements(super.getMaxRecords());

         
        if (errorMessage != null && errorMessage.length() != 0)  {
            throw new ValidationException(errorMessage);
        }                  
        return transactionSearchObject;
    }
     
    public String[] getStringEUIDs(String euids) {
        StringTokenizer stringTokenizer = new StringTokenizer(euids,",");
        String[] euidsArray = new String[stringTokenizer.countTokens()];
        int i =0;
        while(stringTokenizer.hasMoreTokens())  {
            euidsArray[i] = new String(stringTokenizer.nextElement().toString());
            i++;
        }
        return euidsArray;
    }

     public void viewTransactionHistory(ActionEvent event) throws ObjectException{
        
        ArrayList eoArrayList = (ArrayList) event.getComponent().getAttributes().get("eoArrayList");
        session.setAttribute("eoArrayList",eoArrayList);  
    }

    /**
     * Return the populated Value object to the presetation layer
     * @return
     */
    public Transaction[] getTransactionsVO() {
        return transactionsVO;
    }

    /**
     * Set the Transaction Object
     * @param transactionsVO
     */
    public void setTransactionsVO(Transaction[] transactionsVO) {
        this.transactionsVO = transactionsVO;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }

    public ArrayList<SelectItem> getSelectOptions() {
        MasterControllerService masterControllerService  = new MasterControllerService(); 
        String[][] systemCodes = masterControllerService.getSystemCodes();
        String[] pullDownListItems = systemCodes[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(masterControllerService.getSystemDescription(pullDownListItems[i]));
            selectItem.setValue(pullDownListItems[i]);
            newArrayList.add(selectItem);
        }
        selectOptions = newArrayList;
        return selectOptions;
    }
    public void setSelectOptions(ArrayList<SelectItem> selectOptions) {
        this.selectOptions = selectOptions;
    }

    public void unmergeEnterpriseObject(ActionEvent event) throws ObjectException, ProcessingException, UserException, PageException, RemoteException {
        
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewunmerge");
        ArrayList eoArrayList = new ArrayList();
        
        try {
             masterControllerService.isEUIDMerge(transactionNumber);
             MergeResult unmerge = masterControllerService.unmerge(transactionNumber);
             HttpServletRequest facesRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
             facesRequest.setAttribute("transactionId", transactionNumber);     
             eoArrayList = getTranscationDetails(transactionNumber);
             request.setAttribute("comapreEuidsArrayList", eoArrayList);

            if (unmerge.getDestinationEO() != null && unmerge.getSourceEO() != null) {
                //Insert audit log here for EUID search
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                                       unmerge.getDestinationEO().getEUID(),
                                                       unmerge.getSourceEO().getEUID(),
                                                       "EUID Unmerge",
                                                       new Integer(screenObject.getID()).intValue(),
                                                        "Unmerge two enterprise objects");
            }
                
        } 

        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("TRS022: Service Layer Validation Exception has occurred"), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("TRS023: Service Layer User Exception occurred"), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("TRS026: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }

    }

    
    public boolean isEUIDMerge(String trasnNumber) throws ProcessingException, UserException, PageException, RemoteException{
         TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
         //set the transaction number to the transaction summary object
          transactionSearchObject.getTransactionObject().setTransactionNumber(trasnNumber);
          return masterControllerService.isEUIDMerge(trasnNumber);
    }

    /**
     * 
     * @return
     */
    public ArrayList getKeysList() {
        ArrayList newArrayList = new ArrayList();
        newArrayList.add("TransactionNumber");
        newArrayList.add("EUID1");
        newArrayList.add("EUID2");
        newArrayList.add("LID");
        newArrayList.add("Function");
        newArrayList.add("SystemCode");
        newArrayList.add("SystemUser");
        newArrayList.add("TimeStamp");
        return newArrayList;
    }

    /**
     * 
     * @param keysList
     */
    public void setKeysList(ArrayList keysList) {
        this.keysList = keysList;
    }


    /**
     * 
     * @return
     */
    public ArrayList getLabelsList() {
        ArrayList newArrayList = new ArrayList();
        newArrayList.add("Transaction Number");
        newArrayList.add("EUID1");
        newArrayList.add("EUID2");
        newArrayList.add("LID");
        newArrayList.add("Function");
        newArrayList.add("System Code");
        newArrayList.add("System User");
        newArrayList.add("Time Stamp");
        return newArrayList;
    }

    /**
     * 
     * @param labelsList
     */
    public void setLabelsList(ArrayList labelsList) {
        this.labelsList = labelsList;
    }

    public HashMap getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
    }

   
     /** 
     * Added By Rajani Kanth  on 07/17/2008 <br>
     * 
     * This method is called from the ajax services for unmerging the enterprise object.<br>
     *
     * @param transactionNumber  Transaction Number of the EUID.<br>
     *                 srcDestnEuids[0] will be the surviving euid and rest of them are source euids in the multi merge operation.<br>
     *                 
     * @return MergeResult return the new un merge result object if unmerge is successfull.<br>
     *         null if merge fails or any exception occurs.
     * 
     */
    
    public HashMap unmergeEnterpriseObject(String transactionNumber, String euid) {
        HashMap retHashMap = new HashMap();
        try {

            Integer sessionRevisionNumber = (Integer) session.getAttribute("SBR_REVISION_NUMBER" + euid);
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(euid);

            Integer dbRevisionNumber = destinationEO.getSBR().getRevisionNumber();
            
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, TransactionHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
 
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());

                retHashMap.put(TransactionHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                return retHashMap;
            }

            MergeResult unMergeResult = masterControllerService.unmerge(transactionNumber);

            
            if (unMergeResult.getDestinationEO() != null && unMergeResult.getSourceEO() != null) {
            
                //keep the unmerge result in the hashmap
                retHashMap.put("unMergeResult", unMergeResult);

                //Insert audit log here for EUID search
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                        unMergeResult.getDestinationEO().getEUID(),
                        unMergeResult.getSourceEO().getEUID(),
                        "EUID Unmerge",
                        new Integer(screenObject.getID()).intValue(),
                        "Unmerge two enterprise objects");
            }
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            mLogger.error(mLocalizer.x("TRS092: Failed to unmerge EnterpriseObject due to ProcessingException : {0}",ex.getMessage()),ex);
            return null;
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            mLogger.error(mLocalizer.x("TRS093: Failed to unmerge EnterpriseObject due to UserException : {0}", ex.getMessage()), ex);
            return null;
         } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            return null;
        }
        return retHashMap;
    }
     /** 
     * Added By Rajani Kanth  on 07/17/2008 <br>
     * 
     * This method is called from the ajax services for unmerging the enterprise object.<br>
     *
     * @param transactionNumber  Transaction Number of the EUID.<br>
     *                 srcDestnEuids[0] will be the surviving euid and rest of them are source euids in the multi merge operation.<br>
     *                 
     * @return MergeResult return the new un merge result object if unmerge is successfull.<br>
     *         null if merge fails or any exception occurs.
     * 
     */
    
    public HashMap previewUnmergeEnterpriseObject(String transactionNumber, String euid) {
        HashMap retHashMap = new HashMap();
        try {

            Integer sessionRevisionNumber = (Integer) session.getAttribute("SBR_REVISION_NUMBER" + euid);
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(euid);

            Integer dbRevisionNumber = destinationEO.getSBR().getRevisionNumber();
            
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, TransactionHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
 
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());

                retHashMap.put(TransactionHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                return retHashMap;
            }

            MergeResult unMergeResult = masterControllerService.previewUnmerge(transactionNumber);
            CompareDuplicateManager compareDuplicateManager  = new CompareDuplicateManager();
            
            if (unMergeResult.getDestinationEO() != null && unMergeResult.getSourceEO() != null) {
                 //retHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(unMergeResult.getDestinationEO(), screenObject);
                 retHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(unMergeResult.getSourceEO(), screenObject);
             }
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            mLogger.error(mLocalizer.x("TRS092: Failed to unmerge EnterpriseObject due to ProcessingException : {0}",ex.getMessage()),ex);
            return null;
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            mLogger.error(mLocalizer.x("TRS093: Failed to unmerge EnterpriseObject due to UserException : {0}", ex.getMessage()), ex);
            return null;
         } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.toString()));
            return null;
        }
        return retHashMap;
    }

    
}
