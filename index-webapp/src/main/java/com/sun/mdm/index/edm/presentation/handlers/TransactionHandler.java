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
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
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
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import com.sun.mdm.index.util.LogUtil;
import java.util.HashMap;


public class TransactionHandler {
    /**
     * Search Start Date
     */
    private String createStartDate = "";
    /**
     * Search End Date
     */
    private String createEndDate = "";    
    /**
     * Search Start Time
     */ 
    private String createStartTime = "";
    /**
     * Search end Time
     */
    private String createEndTime = "";    
    /**
     * Search Local ID
     */
    private String localid = "";
    /**
     * Search EUID
     */
    private String euid = "";
    /**
     * Search System User
     */
    private String systemuser = "";    
    /**
     * Search Function
     */
    private String function = "";    

    /**
     * Search Function
     */    
    private String source = "";
    
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
    private  static final String TRANSACTIONS_PAGE_VALIDATIONS_ERROR ="Validation Error";
    
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	/** Creates a new instance of TransactionHandler */

    public TransactionHandler() {
    }
    
    public String performTransactionSearch() throws HandlerException  {
        String errorMessage = "success";
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
            try {
                EPathArrayList arlResultFields = null;
                ConfigManager configManager = ConfigManager.getInstance();
                Integer screenID = new Integer("2");
                ScreenObject objScreenObject = null;
                TransactionSearchObject tso = null;
                TransactionIterator iteratorTransaction = null;
                
                objScreenObject = configManager.getScreen(screenID);
                ArrayList arlScreenConfig = objScreenObject.getSearchScreensConfig();
                ArrayList arlResultsConfig = objScreenObject.getSearchResultsConfig();

                EPathArrayList resultFields = retrieveResultsFields(arlResultsConfig);

                tso = getTransactionSearchObject();
                MasterControllerService objMasterControllerService = new MasterControllerService();
                iteratorTransaction = objMasterControllerService.lookupTransactionHistory(tso);

                TransactionIterator iteratorTransaction1 = iteratorTransaction;
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
                        //System.out.println("HElllllllllllllllll" +eoArrayList.size());
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
                      
/**
 * Sridhar Comment : Required code for the Details screen
 * 
                TransactionSummary[] tranSummary = iteratorTransaction1.first(iteratorTransaction1.count());
                for (int i=0; i<tranSummary.length;i++)
                {TransactionSummary ts = tranSummary[i];
                if (ts.getTransactionObject().getFunction().equalsIgnoreCase("Update")) {
                EnterpriseObject eo  = ts.getEnterpriseObjectHistory().getBeforeEO1();
                eo.setEUID("0000000001");
                System.out.println("getBeforeEO1-start :::::::::::::::::::::::::::::::");
                test(eo,resultFields);
                System.out.println("getBeforeEO1-end:::::::::::::::::::::::::::::::");
                EnterpriseObject eo1 = ts.getEnterpriseObjectHistory().getAfterEO();
                eo1.setEUID("0000000001");
                System.out.println("getAfterEO-start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                test(eo1,resultFields);
                System.out.println(">getAfterEO-end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                } else if (ts.getTransactionObject().getFunction().equalsIgnoreCase("Add")) {
                EnterpriseObject eo  = ts.getEnterpriseObjectHistory().getAfterEO();
                EnterpriseObject eo1 = ts.getEnterpriseObjectHistory().getAfterEO();
                }else { // if EUIDMerge/LIDMerge/LIDTransfer, pass both EUIDs
                if (ts.getTransactionObject().getFunction().equalsIgnoreCase("euidMerge") ||
                ts.getTransactionObject().getFunction().equalsIgnoreCase("lidMerge") ||
                ts.getTransactionObject().getFunction().equalsIgnoreCase("lidTransfer")) {
                ts.getEnterpriseObjectHistory().getBeforeEO1().getEUID();
                ts.getEnterpriseObjectHistory().getAfterEO().getEUID();
                //.......... line#223
                }
                }
                }
                EnterpriseObject eo = QwsController.getMasterController().getEnterpriseObject("0000001002");
                Collection items = eo.getSystemObjects();
                System.out.println("items"+items);
                Iterator it = items.iterator();
                SystemObject so = null;
                ArrayList resultArrayList= new ArrayList();
                TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
             */                
            } catch (ValidationException ex) {
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.INFO, null, ex.getMessage());
                return TRANSACTIONS_PAGE_VALIDATIONS_ERROR;
            } catch (UserException ex) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, TransactionHandler.class.getName() + ":: "+ ex.getMessage(), ex);
                throw new HandlerException("UserException Occured");
            } catch (PageException ex) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, TransactionHandler.class.getName() + ":: " + ex.getMessage(), ex);
                throw new HandlerException("PageException Occured");
            } catch (RemoteException ex) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, TransactionHandler.class.getName() + ":: " + ex.getMessage(), ex);
                throw new HandlerException("RemoteException Occured");
            } catch (ProcessingException ex) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, TransactionHandler.class.getName() + ":: " + ex.getMessage(), ex);
                throw new HandlerException("ProcessingException Occured");
            } catch (Exception ex) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, TransactionHandler.class.getName() + ":: " + ex.getMessage(), ex);
            }            
            return TRANSACTIONS_PAGE;
/**
* Sridhar Comment : Required code for the Details screen

                TransactionSummary[] tranSummary = iteratorTransaction1.first(iteratorTransaction1.count());                
                for (int i=0; i<tranSummary.length;i++)
                {TransactionSummary ts = tranSummary[i];
                   if (ts.getTransactionObject().getFunction().equalsIgnoreCase("Update")) {
                       EnterpriseObject eo  = ts.getEnterpriseObjectHistory().getBeforeEO1();
                       eo.setEUID("0000000001");
                       System.out.println("getBeforeEO1-start :::::::::::::::::::::::::::::::");
                       test(eo,resultFields);                    
                       System.out.println("getBeforeEO1-end:::::::::::::::::::::::::::::::");
                       EnterpriseObject eo1 = ts.getEnterpriseObjectHistory().getAfterEO();
                       eo1.setEUID("0000000001");                       
                       System.out.println("getAfterEO-start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                       test(eo1,resultFields);                    
                       System.out.println(">getAfterEO-end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                   } else if (ts.getTransactionObject().getFunction().equalsIgnoreCase("Add")) {
                         EnterpriseObject eo  = ts.getEnterpriseObjectHistory().getAfterEO();
                         EnterpriseObject eo1 = ts.getEnterpriseObjectHistory().getAfterEO();
                     }else { // if EUIDMerge/LIDMerge/LIDTransfer, pass both EUIDs                                               
                            if (ts.getTransactionObject().getFunction().equalsIgnoreCase("euidMerge") ||
                                ts.getTransactionObject().getFunction().equalsIgnoreCase("lidMerge") ||
                                ts.getTransactionObject().getFunction().equalsIgnoreCase("lidTransfer")) {
                                    ts.getEnterpriseObjectHistory().getBeforeEO1().getEUID();
                                    ts.getEnterpriseObjectHistory().getAfterEO().getEUID();
                                    //.......... line#223
                                 }
                     }       
                }
                
                
                EnterpriseObject eo = QwsController.getMasterController().getEnterpriseObject("0000001002");
                Collection items = eo.getSystemObjects();              
                System.out.println("items"+items);
                Iterator it = items.iterator();
                SystemObject so = null;
                ArrayList resultArrayList= new ArrayList();                                                   
                TransactionSummary[] tsArray = iteratorTransaction.first(iteratorTransaction.count());
*/
                
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
                                {  //System.out.println(ePath.getFieldTag()+" ---- "+obj[0]);
                                  //System.out.println(" field "+ePath.getFieldTag());
                                  /*
                                  if(ePath.getFieldTag().equals("FirstName"))
                                  {System.out.println("FirstName : "+obj[0]);
                                  }else if(ePath.getFieldTag().equals("LastName"))
                                  {System.out.println("LastName : "+obj[0]);
                                  } 
                                   */ 
                                }
                            }    
                        }
                        //System.out.println("--------------------------------------------------");
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
        }catch(Exception e){
            
            //System.out.println("*****************************************************"+e.toString());
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
                    //System.out.println("::::  EPath string: " + strEPath);
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
                                eoArrayList.add(viewMergehist.getBeforeEO1());
                            }
                            if (viewMergehist.getBeforeEO2() != null) {
                                eoArrayList.add(viewMergehist.getBeforeEO2());
                            }
                            if (viewMergehist.getAfterEO2() != null) {
                                eoArrayList.add(viewMergehist.getAfterEO2());
                            }
                        }

                    } else {
                        if (ts.getEnterpriseObjectHistory().getAfterEO() != null) {
                            eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO());
                        }
                        if (ts.getEnterpriseObjectHistory().getAfterEO2() != null) {
                            eoArrayList.add(ts.getEnterpriseObjectHistory().getAfterEO2());
                        }
                        if (ts.getEnterpriseObjectHistory().getBeforeEO1() != null) {
                            eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO1());
                        }
                        if (ts.getEnterpriseObjectHistory().getBeforeEO2() != null) {
                            eoArrayList.add(ts.getEnterpriseObjectHistory().getBeforeEO2());
                        }

                    }
                    //System.out.println("HElllllllllllllllll" + eoArrayList);
                }
            }

        } catch (PageException ex) {
            Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eoArrayList;
         
        
    }
       
     public TransactionSearchObject getTransactionSearchObject() throws ProcessingException, ValidationException {
         // From and to date hardcoded here
         TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
         EDMValidation edmValidation = new EDMValidation();
         String errorMessage = null;
         ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
         transactionSearchObject.setPageSize(10);
         transactionSearchObject.setMaxElements(100);

        // One of Many validation 
            if ((this.getLocalid() != null && this.getLocalid().trim().length() == 0) &&
                (this.getEuid() != null && this.getEuid().trim().length() == 0) &&
                (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() == 0) &&
                (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() == 0) &&
                (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() == 0) &&
                (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() == 0)&&
                (this.getFunction() == null) && //Function
                (this.getSource() == null ) && //Function
                (this.getSystemuser() != null && this.getSystemuser().trim().length() == 0)){
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
        }
         
        
        //Form Validation of  Start Time
        if (this.getCreateStartTime() != null && this.getCreateStartTime().trim().length() > 0)    {
            String message = edmValidation.validateTime(this.getCreateStartTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?errorMessage:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
            }            
        }
         
        //Form Validation of End Time
        if (this.getCreateEndTime() != null && this.getCreateEndTime().trim().length() > 0)    {            
            String message = edmValidation.validateTime(this.getCreateEndTime());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
            }           
        }    
        //Form Validation of  Start Date        
        if (this.getCreateStartDate() != null && this.getCreateStartDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateStartDate());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
            }
        }        
        //Check CreateStartDateField
        try {         
            if ((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0)) {                
                //If Time is supplied append it to the date
                String searchStartDate = this.getCreateStartDate() + (this.getCreateStartTime() != null? " " + this.getCreateStartTime():" 00:00:00");                                
                Date date = DateUtil.string2Date(searchStartDate);
                if (date != null) {
                    transactionSearchObject.setStartDate(new Timestamp(date.getTime()));
                }
            }
        } catch (ValidationException validationException) {
            errorMessage = (errorMessage != null && errorMessage.length() > 0? bundle.getString("ERROR_start_date"):bundle.getString("ERROR_start_date"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, errorMessage, validationException);
        }

        //Form Validation of  End Date        
        if (this.getCreateEndDate() != null && this.getCreateEndDate().trim().length() > 0)    {
            String message = edmValidation.validateDate(this.getCreateEndDate());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
            }            
        }

        //Check CreateEndDateField
        if ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0)) {
            try {
                //If Time is supplied append it to the date
                String searchEndDate = this.getCreateEndDate() +  (this.getCreateEndTime() != null? " " +this.getCreateEndTime():" 23:59:59");
                Date date = DateUtil.string2Date(searchEndDate);
                if (date != null) {
                    transactionSearchObject.setEndDate(new Timestamp(date.getTime()));
                }
            } catch (ValidationException validationException) {
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, validationException.toString(), validationException);
                errorMessage = (errorMessage != null && errorMessage.length() > 0?bundle.getString("ERROR_end_date"):bundle.getString("ERROR_end_date"));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));                
            }
        }
        
          if (((this.getCreateStartDate() != null) && (this.getCreateStartDate().trim().length() > 0))&&
           ((this.getCreateEndDate() != null) && (this.getCreateEndDate().trim().length() > 0))){                
               Date fromdate = DateUtil.string2Date(this.getCreateStartDate() + (this.getCreateStartTime() != null? " " +this.getCreateStartTime(): " 00:00:00"));
               Date todate = DateUtil.string2Date(this.getCreateEndDate()+(this.getCreateEndTime() != null? " " +this.getCreateEndTime(): " 23:59:59"));
               long startDate = fromdate.getTime();
               long endDate = todate.getTime();
                 if(endDate < startDate){
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "fromdate :: " + errorMessage));
                    Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                   }
        }

        //Form Validation of Local ID
        if (this.getLocalid() != null && this.getLocalid().length() > 0)    {
            transactionSearchObject.setLID(this.getLocalid().replaceAll("-",""));
            String message = edmValidation.validateLocalId(this.getLocalid());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Local ID:: " + errorMessage, errorMessage));
                Logger.getLogger(TransactionHandler.class.getName()).log(Level.WARNING, message, message);
            }
         } else {
            transactionSearchObject.setLID(null);
         }
        
         
        if (this.getEuid() != null && this.getEuid().length() > 0) {
            transactionSearchObject.setEUID(this.getEuid());
            String message = edmValidation.validateNumber(this.getEuid());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
                Logger.getLogger(AuditLogHandler.class.getName()).log(Level.WARNING, message, message);
         }
        } else {
            transactionSearchObject.setEUID(null);
        }
        
        if (this.getSystemuser() != null && this.getSystemuser().length() > 0) {
            transactionSearchObject.setSystemUser(this.getSystemuser());
          } else {
            transactionSearchObject.setSystemUser(null);
        }
         
        if (this.getFunction() != null && this.getFunction().length() > 0) {
            transactionSearchObject.setFunction(this.getFunction());
          } else {
            transactionSearchObject.setFunction(null);
        }
                 
        transactionSearchObject.setSystemUser((this.getSystemuser() != null && this.getSystemuser().length() > 0)?this.getSystemuser():null);
        transactionSearchObject.setFunction((this.getFunction() != null && this.getFunction().length() > 0)?this.getFunction():null);
        transactionSearchObject.setEUID((this.getEuid() != null && this.getEuid().length() > 0)?this.getEuid():null);
        transactionSearchObject.setSystemCode((this.getSource() != null && this.getSource().length() > 0)?this.getSource():null);

         
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createStartDate::" + createStartDate);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createEndDate:: " + createEndDate);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createStartTime::" + createStartTime);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:createEndTime::" + createEndTime);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:localid::" + localid);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:euid::" + euid);
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
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:systemuser::" + systemuser);
    }

    /**
     * @return function
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the function parameter for the search
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:Function::" + source);
    }
	
	
    /**
     * @return source Option
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the function parameter for the search
     * @param sourceOption 
     */
    public void setSource(String sourceOption) {
        this.source = sourceOption;
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.INFO, null, this.getClass().getName() + " FORM:sourceOption::" + sourceOption);
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
            //System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
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
        
        MasterControllerService masterControllerService=new MasterControllerService();
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewunmerge");
        
        try {
             masterControllerService.isEUIDMerge(transactionNumber);
             MergeResult unmerge = masterControllerService.unmerge(transactionNumber);
             //System.out.println("helllllllllllllllo"+transactionNumber);
             HttpServletRequest facesRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
             facesRequest.setAttribute("transactionId", transactionNumber);             
             //System.out.println("RETURNING THE CONTROL TO JSP");
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    
    public boolean isEUIDMerge(String trasnNumber) throws ProcessingException, UserException{
        MasterControllerService masterControllerService=new MasterControllerService();
         TransactionSearchObject transactionSearchObject = new TransactionSearchObject();
         //set the transaction number to the transaction summary object
          transactionSearchObject.getTransactionObject().setTransactionNumber(trasnNumber);
          return masterControllerService.isEUIDMerge(trasnNumber);
    }

}
