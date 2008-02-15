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
import com.sun.mdm.index.util.Logger;
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

public class TransactionHandler extends ScreenConfiguration {
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
    
    
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.TransactionHandler");

    String errorMessage = new String();
    /** Creates a new instance of TransactionHandler */

    public TransactionHandler() {
    }
    
    public String performSubmit() throws HandlerException  {
            try {
            //System.out.println("---------------1-------------------" + super.getUpdateableFeildsMap() + super.checkOneOfManyCondition());
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                //System.out.println("---------------1-------------------" + super.getUpdateableFeildsMap());
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
                return VALIDATION_ERROR;
            }

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = "Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID Validation :: " + errorMessage, errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                return VALIDATION_ERROR;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = "Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SystemCode Validation :: " + errorMessage, errorMessage));
                    mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
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
                        ////System.out.println("SystemCode" + SystemCode + "LID" + LID);
                        SystemObject so = masterControllerService.getSystemObject(SystemCode, LID);
                        if (so == null) {
                            errorMessage = bundle.getString("system_object_not_found_error_message");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SYSTEM CODE :: " + errorMessage, errorMessage));
                            mLogger.error("Validation failed. Message displayed to the user: " + "LID/SYSTEM CODE :: " + errorMessage);
                            return VALIDATION_ERROR;
                        }
                    } catch (ProcessingException ex) {
                        mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("ProcessingException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ProcessingException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        return VALIDATION_ERROR;
                    } catch (UserException ex) {
                        mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("UserException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        return VALIDATION_ERROR;
                    }

                }

            }

            //Validate all date fields entered by the user
            if (super.validateDateFields().size() > 0) {
                Object[] messObjs = super.validateDateFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(":");
                    ////System.out.println("===> Field" + fieldErrors[0] + "===> Message" + fieldErrors[1]);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    return VALIDATION_ERROR;
                }

            }

            //Validate all time fields entered by the user
            if (super.validateTimeFields().size() > 0) {
                Object[] messObjs = super.validateTimeFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(":");
                    ////System.out.println("===> Field" + fieldErrors[0] + "===> Message" + fieldErrors[1]);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    return VALIDATION_ERROR;
                }

            }
            
            

                
                TransactionSearchObject tso = getTransactionSearchObject();
                
                //System.out.println("---------------5-------------------" + tso);
                
                MasterControllerService objMasterControllerService = new MasterControllerService();

                TransactionIterator iteratorTransaction = iteratorTransaction = objMasterControllerService.lookupTransactionHistory(tso);
                setSearchSize(0);
                if (iteratorTransaction != null) {
                    TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
                    setTransactionsVO(new Transaction[tsArray.length]);
                    for (int i = 0; i < tsArray.length; i++) {
                        TransactionSummary ts = tsArray[i];

                        getTransactionsVO()[i] = new Transaction(); //to be safe with malloc
                        getTransactionsVO()[i].setTransactionId(ts.getTransactionObject().getTransactionNumber());
                        //String transactionNumber = ts.getTransactionObject().getTransactionNumber();
                        getTransactionsVO()[i].setEuid(ts.getTransactionObject().getEUID());
                        //String euid = ts.getTransactionObject().getEUID();
                        getTransactionsVO()[i].setFunction(ts.getTransactionObject().getFunction());
                        //String function = ts.getTransactionObject().getFunction();
                         getTransactionsVO()[i].setLocalid(ts.getTransactionObject().getLID());
                        
                        //String lid = ts.getTransactionObject().getLID();
                        getTransactionsVO()[i].setSource(ts.getTransactionObject().getSystemCode());
                        //String systemCode = ts.getTransactionObject().getSystemCode();
                        getTransactionsVO()[i].setSystemUser(ts.getTransactionObject().getSystemUser());
                        //String systemUser = ts.getTransactionObject().getSystemUser();
                        getTransactionsVO()[i].setTransactionDate(ts.getTransactionObject().getTimeStamp());
                        //String createDate = ts.getTransactionObject().getTimeStamp().toString();
                        getTransactionsVO()[i].setFirstName("");
                        getTransactionsVO()[i].setLastName("");
                        
                        ArrayList eoArrayList = new ArrayList();
                        
                        if(ts.getEnterpriseObjectHistory().getAfterEO() != null ) eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO());
                        if(ts.getEnterpriseObjectHistory().getAfterEO2() != null ) eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO2());
                        if(ts.getEnterpriseObjectHistory().getBeforeEO1() != null ) eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO1());
                        if(ts.getEnterpriseObjectHistory().getBeforeEO2() != null ) eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO2());
                        ////System.out.println("HElllllllllllllllll" +eoArrayList.size());
                        if(eoArrayList.size() > 0) {
                           getTransactionsVO()[i].setEoArrayList(eoArrayList);
                           getTransactionsVO()[i].setEoArrayListSize(eoArrayList.size());
                        }
                        

                    }
                    setTransactionsVO(transactionsVO);
                    setSearchSize(transactionsVO.length);
                    request.setAttribute("searchSize",new Integer(transactionsVO.length) );
                  //  request.setAttribute("searchSize",new Integer(transactionsVO.length) );
                }                
            } catch (ValidationException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ValidationException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("ValidationException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ValidationException ex : " + ex.toString());
                    return VALIDATION_ERROR;
            } catch (UserException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("UserException ex : " + ex.toString());
                    return VALIDATION_ERROR;
            } catch (PageException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PageException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("PageException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("PageException ex : " + ex.toString());
                    return VALIDATION_ERROR;
            } catch (RemoteException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "RemoteException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("RemoteException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("RemoteException ex : " + ex.toString());
                    return VALIDATION_ERROR;
            } catch (ProcessingException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ProcessingException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ProcessingException ex : " + ex.toString());
                    return VALIDATION_ERROR;
            } catch (Exception ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("Exception ex : " + ex.toString());
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
                                
                                if(obj!=null)
                                {  ////System.out.println(ePath.getFieldTag()+" ---- "+obj[0]);
                                  ////System.out.println(" field "+ePath.getFieldTag());
                                  /*
                                  if(ePath.getFieldTag().equals("FirstName"))
                                  {//System.out.println("FirstName : "+obj[0]);
                                  }else if(ePath.getFieldTag().equals("LastName"))
                                  {//System.out.println("LastName : "+obj[0]);
                                  } 
                                   */ 
                                }
                            }    
                        }
                        ////System.out.println("--------------------------------------------------");
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
            mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
            mLogger.error("UserException ex : " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
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
                    ////System.out.println("::::  EPath string: " + strEPath);
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
                    if ("euidMerge".equalsIgnoreCase(functionName)) {
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
                        }

                    } else {
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

                    }
                    ////System.out.println("HElllllllllllllllll" + eoArrayList);
                }
            }

        } catch (PageException ex) {
                        mLogger.error("PageException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("PageException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PageException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        return null;
        } catch (RemoteException ex) {
                        mLogger.error("RemoteException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("RemoteException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "RemoteException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        return null;
        } catch (UserException ex) {
                        mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("UserException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        return null;
        } catch (ProcessingException ex) {
                        mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("ProcessingException ex : " + ex.toString());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ProcessingException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
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
                    ////System.out.println("SystemCode" + SystemCode + "LID" + LID);
                    SystemObject so = masterControllerService.getSystemObject(SystemCode, LID);
                    if (so == null) {
                        errorMessage = bundle.getString("system_object_not_found_error_message");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SYSTEM CODE:: " + errorMessage, errorMessage));
                        mLogger.error("Validation failed. Message displayed to the user: " + "LID/SYSTEM CODE:: " + errorMessage);
                    } else {
                        EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                        transactionSearchObject.setEUID(eo.getEUID());
                    }
                } catch (ProcessingException ex) {
                    mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ProcessingException ex : " + ex.toString());
                } catch (UserException ex) {
                    mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("UserException ex : " + ex.toString());
                }

            }

        }

        //set EUID VALUE IF lid/system code not supplied
        ////System.out.println("======1======EUID==");
        if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() == 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
            if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
                transactionSearchObject.setEUID((String) super.getUpdateableFeildsMap().get("EUID"));
            } else {
                transactionSearchObject.setEUID(null);
            }
        }


        ////System.out.println("======1=====st=date==");
        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("StartDate") != null && super.getUpdateableFeildsMap().get("StartDate").toString().trim().length() > 0) {
            ////System.out.println("======11=====st=date==");
            String startTime = (String) super.getUpdateableFeildsMap().get("StartTime");
            ////System.out.println("======12=====startTime==" + startTime);
            String searchStartDate = (String) super.getUpdateableFeildsMap().get("StartDate");
            ////System.out.println("======13=====searchStartDate==" + searchStartDate);
            //append the time aling with date
            if (startTime != null && startTime.trim().length() > 0) {
                searchStartDate = searchStartDate + " " + startTime;
            } else {
                searchStartDate = searchStartDate + " 00:00:00";
            }

            ////System.out.println("======14=====st=date==" + searchStartDate);
            Date date = DateUtil.string2Date(searchStartDate);
            //System.out.println("======15=====st=date==" + date);
            if (date != null) {
                transactionSearchObject.setStartDate(new Timestamp(date.getTime()));

            }
        }

        ////System.out.println("======1=====end=date==");

        //EndDate=02/27/2008, StartDate=02/01/2008, Function=null, SystemUser=, EndTime=, StartTime=
        //Set StartDate to the amso  
        if (super.getUpdateableFeildsMap().get("EndDate") != null && super.getUpdateableFeildsMap().get("EndDate").toString().trim().length() > 0) {
            String endTime = (String) super.getUpdateableFeildsMap().get("EndTime");
            String searchEndDate = (String) super.getUpdateableFeildsMap().get("EndDate");
            //append the time aling with date
            if (endTime != null && endTime.trim().length() > 0) {
                searchEndDate = searchEndDate + " " + endTime;
            } else {
                searchEndDate = searchEndDate + " 23:59:59";
            }
            Date date = DateUtil.string2Date(searchEndDate);
            //System.out.println("======15=====END=date==" + date);
            if (date != null) {
                transactionSearchObject.setEndDate(new Timestamp(date.getTime()));
            }
        }
        ////System.out.println("======1=====System User==");
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
     
         transactionSearchObject.setPageSize(10);
         transactionSearchObject.setMaxElements(100);

//        // One of Many validation 
//            if ((this.getLocalid() != null && this.getLocalid().trim().length() == 0) &&
//                (this.getEuid() != null && this.getEuid().trim().length() == 0) &&
//                (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
//                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
//                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
//                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)&&
//                (this.getFunction() == null) && //Function
//                (this.getSource() == null ) && //Function
//                (this.getSystemuser() != null && this.getSystemuser().trim().length() == 0)){
//                errorMessage = bundle.getString("ERROR_one_of_many");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
//        }
//         
//        
//        //Form Validation of  Start Time
//        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
//            String message = edmValidation.validateTime(this.getCreateStartTime());
//            if (!"success".equalsIgnoreCase(message)) {
//                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
//            }            
//        }
//         
//        //Form Validation of End Time
//        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
//            String message = edmValidation.validateTime(this.getCreateEndTime());
//            if (!"success".equalsIgnoreCase(message)) {
//                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
//            }           
//        }    
//        //Form Validation of  Start Date        
//        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
//            String message = edmValidation.validateDate(this.getCreateStartDate());
//            if (!"success".equalsIgnoreCase(message)) {
//                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
//                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
//                 Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
//            }
//        }        
//        //Check CreateStartDateField
//        try {         
//            if ((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0)) {                
//                //If Time is supplied append it to the date
//                String searchStartDate = this.getCreateStartDate() + (this.getCreateStartTime() != null? " " + this.getCreateStartTime():" 00:00:00");                                
//                Date date = DateUtil.string2Date(searchStartDate);
//                if (date != null) {
//                    transactionSearchObject.setStartDate(new Timestamp(date.getTime()));
//                }
//            }
//        } catch (ValidationException validationException) {
//            errorMessage = (errorMessage != null && errorMessage.length() > 0? bundle.getString("ERROR_start_date"):bundle.getString("ERROR_start_date"));
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
//            Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
//        }
//
//        //Form Validation of  End Date        
//        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
//            String message = edmValidation.validateDate(this.getCreateEndDate());
//            if (!"success".equalsIgnoreCase(message)) {
//                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
//            }            
//        }
//
//        //Check CreateEndDateField
//        if ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0)) {
//            try {
//                //If Time is supplied append it to the date
//                String searchEndDate = this.getCreateEndDate() +  (this.getCreateEndTime() != null? " " +this.getCreateEndTime():" 23:59:59");
//                Date date = DateUtil.string2Date(searchEndDate);
//                if (date != null) {
//                    transactionSearchObject.setEndDate(new Timestamp(date.getTime()));
//                }
//            } catch (ValidationException validationException) {
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
//                errorMessage = (errorMessage != null && errorMessage.length() > 0?bundle.getString("ERROR_end_date"):bundle.getString("ERROR_end_date"));
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));                
//            }
//        }
//        
//          if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0))&&
//           ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))){                
//               Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + (this.getCreateStartTime() != null? " " +this.getCreateStartTime(): " 00:00:00"));
//               Date todate = DateUtil.string2Date(this.getCreateEndDate()+(this.getCreateEndTime() != null? " " +this.getCreateEndTime(): " 23:59:59"));
//               long startDate = fromdate.getTime();
//               long endDate = todate.getTime();
//                 if(endDate < startDate){
//                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "fromdate :: " + errorMessage));
//                    Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
//                   }
//        }
//
//        //Form Validation of Local ID
//        if (this.getLocalid() != null && this.getLocalid().length() > 0)    {
//            transactionSearchObject.setLID(this.getLocalid().replaceAll("-",""));
//            String message = edmValidation.validateLocalId(this.getLocalid());
//            if (!"success".equalsIgnoreCase(message)) {
//                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Local ID:: " + errorMessage, errorMessage));
//                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
//            }
//         } else {
//            transactionSearchObject.setLID(null);
//         }
//        
//         
//        if (this.getEuid() != null && this.getEuid().length() > 0) {
//            transactionSearchObject.setEUID(this.getEuid());
//            String message = edmValidation.validateNumber(this.getEuid());
//            if (!"success".equalsIgnoreCase(message)) {
//                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
//                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
//         }
//        } else {
//            transactionSearchObject.setEUID(null);
//        }
//        
//        if (this.getSystemuser() != null && this.getSystemuser().length() > 0) {
//            transactionSearchObject.setSystemUser(this.getSystemuser());
//          } else {
//            transactionSearchObject.setSystemUser(null);
//        }
//         
//        if (this.getFunction() != null && this.getFunction().length() > 0) {
//            transactionSearchObject.setFunction(this.getFunction());
//          } else {
//            transactionSearchObject.setFunction(null);
//        }
//                 
//        transactionSearchObject.setSystemUser((this.getSystemuser() != null && this.getSystemuser().length() > 0)?this.getSystemuser():null);
//        transactionSearchObject.setFunction((this.getFunction() != null && this.getFunction().length() > 0)?this.getFunction():null);
//        transactionSearchObject.setEUID((this.getEuid() != null && this.getEuid().length() > 0)?this.getEuid():null);
//        transactionSearchObject.setSystemCode((this.getSource() != null && this.getSource().length() > 0)?this.getSource():null);

         
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
            selectItem.setLabel(pullDownListItems[i]);
            selectItem.setValue(pullDownListItems[i]);
            newArrayList.add(selectItem);
        }
        selectOptions = newArrayList;
        return selectOptions;
    }
    public void setSelectOptions(ArrayList<SelectItem> selectOptions) {
        this.selectOptions = selectOptions;
    }

    public void unmergeEnterpriseObject(ActionEvent event) throws ObjectException {
        
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewunmerge");
        
        try {
             masterControllerService.isEUIDMerge(transactionNumber);
             MergeResult unmerge = masterControllerService.unmerge(transactionNumber);
             ////System.out.println("helllllllllllllllo"+transactionNumber);
             HttpServletRequest facesRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
             facesRequest.setAttribute("transactionId", transactionNumber);             
             ////System.out.println("RETURNING THE CONTROL TO JSP");
        } catch (ProcessingException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ProcessingException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("ProcessingException  : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ProcessingException ex : " + ex.toString());
        } catch (UserException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(),ex.toString()));
                    mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("UserException ex : " + ex.toString());
        }  
    }

    
    public boolean isEUIDMerge(String trasnNumber) throws ProcessingException, UserException{
         TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
         //set the transaction number to the transaction summary object
          transactionSearchObject.getTransactionObject().setTransactionNumber(trasnNumber);
          return masterControllerService.isEUIDMerge(trasnNumber);
    }

}
