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
 * SearchDuplicatesHandler.java 
 * Created on September 12, 2007, 11:58 AM
 * Author : Pratibha, RajaniKanth
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.sql.Timestamp;
import java.util.Date;
import java.util.StringTokenizer;
import javax.faces.event.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;


/**
 * 
 * @author Rajani Kanth
 */
public class SearchDuplicatesHandler extends ScreenConfiguration {
    private HashMap updateableFeildsMap =  new HashMap();    
    private HashMap actionMap =  new HashMap();    
    private ArrayList nonUpdateableFieldsArray = new ArrayList();    
    
    String errorMessage = null;
    
    private  static final String SEARCH_DUPLICATES="Search Duplicates";
    
     /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;

   
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler");

    private String searchType = "Advanced Search";

    private static final String  BASIC_SEARCH_RES     = "basicSearchResults";
    private static final String  ADV_SEARCH_RES       = "advancedSearchResults";
    private static final String  POT_DUP_SEARCH_RES   = "potentialduplicates";
    private static final String  VALIDATION_ERROR     = "validationfailed";

    private PotentialDuplicateSummary comparePotentialDuplicateSummary;
    private ArrayList potentialDuplicateSummaryArray;

    
    /** Creates a new instance of SearchDuplicatesHandler */
    public SearchDuplicatesHandler() {
    }


    public String performSubmit() throws ProcessingException, UserException, ValidationException,HandlerException{
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
        //Start Validation
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

            //System.out.println("-------------1-------------------" + super.getUpdateableFeildsMap());

            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            
            
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
                return VALIDATION_ERROR;
            }

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = "Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System Code/LID Validation  :: " + errorMessage, errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                return VALIDATION_ERROR;

            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
                errorMessage = "Please Enter LID Value";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System Code/LID Validation :: " + errorMessage, errorMessage));
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
                        return VALIDATION_ERROR;
                    } catch (UserException ex) {
                        mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("UserException ex : " + ex.toString());
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
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    return VALIDATION_ERROR;
                }

            }
        
        
        //End Validation
        
        
        
        
        
        
        
        
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = getPDSearchObject();
        
        try {
            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            
            // Code Added by Pratibha 
            int count = pdPageIterArray.count();
            String[][] temp = new String[count][2];

            if (pdPageIterArray != null & pdPageIterArray.count() > 0) {
                // add all the potential duplicates to the summary array  
                while (pdPageIterArray.hasNext()) {
                    PotentialDuplicateSummary pds[] = pdPageIterArray.first(pdPageIterArray.count());
                    for(int i=0;i<pds.length;i++)
                    {   
                        String euid1 = pds[i].getEUID1();
                        String euid2 = pds[i].getEUID2();

                        //Insert audit log for the "Matching Review Search Result"
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               euid1, 
                                               euid2,
                                               "Matching Review Search Result",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View Potential Duplicate Search Result");
                        
                        temp[i][0] = euid1;
                        temp[i][1] = euid2;                       
                    }
               }
            }

            ArrayList arl = new ArrayList();
                        
            for(int i=0;i<count;i++)
            { for(int j=0;j<2;j++)
                { boolean addData = true;
                  String data ;                 

                  for (int ii=0;ii<arl.size();ii++)
                  {     data = (String) arl.get(ii);
                        if(data.equalsIgnoreCase(temp[i][j]))
                        { addData = false;
                          break;
                        }
                  } 
                  if (addData == true)
                  { arl.add(temp[i][j]);
                  }
                }
            }
            //Code to create ArrayList
            ArrayList arlOuter = new ArrayList();
            for (int x = 0; x < arl.size(); x++) {
                String id = (String) arl.get(x);
                ArrayList arlInner = new ArrayList();
                boolean avlInArlOuter = false;
                arlInner.add(id);
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < 2; j++) {
                        String strData = temp[i][j];
                        if (id.equalsIgnoreCase(strData)) {
                            if (j == 0) {
                                //if(!arlInner.contains(strData))
                                //{arlInner.add(strData);
                                //}
                                if(!arlInner.contains(temp[i][1]))
                                {arlInner.add(temp[i][1]);
                                }
                            } else if (j == 1) {
                                //if(!arlInner.contains(strData))
                                //{arlInner.add(strData);
                                //}
                            if(!arlInner.contains(temp[i][0]))
                                {arlInner.add(temp[i][0]);
                                }
                            }                          
                          }
                        }
                    }
                arlOuter.add(arlInner);
            }
            ArrayList finalArrayList = arlOuter;            
            ArrayList arlInner = null;          
            if (super.getUpdateableFeildsMap().get("EUID")== null) {
                finalArrayList = arlOuter;
            } else {
                ArrayList outer = new ArrayList();
                for (int i = 0; i < arlOuter.size(); i++) {
                    arlInner = (ArrayList) arlOuter.get(i);
                    String strData = (String) arlInner.get(0);
                    if (strData.equalsIgnoreCase((String)super.getUpdateableFeildsMap().get("EUID"))) {                     
                        outer.add(arlInner);
                        finalArrayList = outer;
                    }
                }
            }

            //Build and arraylist of hashmaps for the duplicates before putting in the request
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            ArrayList newFinalArray  = new ArrayList();        
            float wt = 0.0f;
            for (int i = 0; i < finalArrayList.size(); i++) {
                ArrayList newInnerArray  = new ArrayList();        
                ArrayList innerArrayList = (ArrayList) finalArrayList.get(i);
                for (int j = 0; j < innerArrayList.size(); j++) {
                    String euids = (String) innerArrayList.get(j);
                    EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids);
                    HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                  if(j > 0) {
                  //Add weight to the hashmap 
                   //eoMap.put("Weight", masterControllerService.getPotentialDuplicateWeight((String) innerArrayList.get(0), euids));
//                   eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateID((String) innerArrayList.get(0), euids));
//                   eoMap.put("Status", masterControllerService.getPotentialDuplicateStatus((String) innerArrayList.get(0), euids));
                      
                  eoMap.put("Weight", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids,"WEIGHT"));
                  eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0),euids, "duplicateid"));
                  eoMap.put("Status", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids,"status"));
                  } else {
                   eoMap.put("Weight", wt);
                   eoMap.put("PotDupId", "000");
                   eoMap.put("Status", "U");
                  }    

                   newInnerArray.add(eoMap);
                }
                
                newFinalArray.add(newInnerArray);
        }
            httpRequest.setAttribute("finalArrayList", newFinalArray);                
        } catch (Exception ex) {
               // UserException and ValidationException don't need a stack trace.
                // ProcessingException stack trace logged by MC
                if (ex instanceof ValidationException) {
                    mLogger.info("Validation failed. Message displayed to the user: " 
                                  + QwsUtil.getRootCause(ex).getMessage());
                } else if (ex instanceof UserException) {
                    mLogger.info("UserException. Message displayed to the user: "
                                  + QwsUtil.getRootCause(ex).getMessage());
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ProcessingException ex : " + ex.toString());
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof PageException)) {
                    mLogger.error("PageException : " + QwsUtil.getRootCause(ex).getMessage());
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof RemoteException)) {
                    mLogger.error("RemoteException : " + QwsUtil.getRootCause(ex).getMessage());
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                }else
                { mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                }
        }
        return this.SEARCH_DUPLICATES;
    }
    
    public HashMap getActionMap() {
        return actionMap;
    }

    public void setActionMap(HashMap actionMap) {
        this.actionMap = actionMap;
    }

    /**
     * @exception ValidationException when entry is not valid.
     * @todo Document: Getter for PDSearchObject attribute of the SearchForm
     *      object
     * @return  the PD search object
     */
    public PotentialDuplicateSearchObject getPDSearchObject() {
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = new PotentialDuplicateSearchObject();
        
        //if user enters LID and SystemCode get the EUID and set it to the potentialDuplicateSearchObject
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
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SYSTEM CODE:: " + errorMessage, errorMessage));
                        mLogger.error("LID/SYSTEM CODE:: " + errorMessage);

                    } else {
                        EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                        //potentialDuplicateSearchObject.setEUID(eo.getEUID());
                        String[] euidArray = getStringEUIDs(eo.getEUID());

                        if (euidArray != null & euidArray.length > 0) {
                            potentialDuplicateSearchObject.setEUIDs(euidArray);
                        } else {
                            potentialDuplicateSearchObject.setEUIDs(null);
                        }
                    }
                } catch (ProcessingException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ProcessingException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                    mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("ProcessingException ex : " + ex.toString());
                } catch (UserException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "UserException : " + QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                    mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                    mLogger.error("UserException ex : " + ex.toString());
                }

            }

        }

        //set EUID VALUE IF lid/system code not supplied
          if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
//            // Get array of strings
                String[] euidArray = getStringEUIDs((String) super.getUpdateableFeildsMap().get("EUID"));
                
                if(euidArray!=null & euidArray.length >0) {
                    potentialDuplicateSearchObject.setEUIDs(euidArray);
                } else {
                    potentialDuplicateSearchObject.setEUIDs(null);
                }
          }
        



        //Set StartDate to the potentialDuplicateSearchObject  
        if (super.getUpdateableFeildsMap().get("create_start_date") != null && super.getUpdateableFeildsMap().get("create_start_date").toString().trim().length() > 0) {
            try {
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
                    potentialDuplicateSearchObject.setCreateStartDate(new Timestamp(date.getTime()));
                }
            } catch (ValidationException ex) {
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        //Set StartDate to the potentialDuplicateSearchObject  
        if (super.getUpdateableFeildsMap().get("create_end_date") != null && super.getUpdateableFeildsMap().get("create_end_date").toString().trim().length() > 0) {
            try {
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
                    potentialDuplicateSearchObject.setCreateEndDate(new Timestamp(date.getTime()));
                }
            } catch (ValidationException ex) {
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //EndTime=, StartTime=, EndDate=, StartDate=, Function=null, SystemUser=, SystemCode=null, LID=, EUID=
        if (super.getUpdateableFeildsMap().get("SystemUser") != null && super.getUpdateableFeildsMap().get("SystemUser").toString().trim().length() > 0) {
            potentialDuplicateSearchObject.setCreateUser((String) super.getUpdateableFeildsMap().get("SystemUser"));
        } else {
            potentialDuplicateSearchObject.setCreateUser(null);
        }

        //EndTime=, StartTime=, EndDate=, StartDate=, Function=null, SystemUser=, SystemCode=null, LID=, EUID=
        if (super.getUpdateableFeildsMap().get("Status") != null && super.getUpdateableFeildsMap().get("Status").toString().trim().length() > 0) {
            potentialDuplicateSearchObject.setStatus((String) super.getUpdateableFeildsMap().get("Status"));
        } else {
            potentialDuplicateSearchObject.setStatus(null);
        }

        
        
        
        
        
        
        
        
//        
//        
//        
//        
//        
//        
//        // Set to static values need clarification from prathiba
//        //This will be revoked when login module is implemented.
//
//        //obj.setPageSize(ConfigManager.getInstance().getMatchingConfig().getItemPerSearchResultPage());
//        //obj.setMaxElements(ConfigManager.getInstance().getMatchingConfig().getMaxResultSize());
//
//        obj.setPageSize(10);
//        obj.setMaxElements(100);
//        
//        Date date = null;
//                
//        try {
//            if ((getCreate_start_date() != null) && (getCreate_start_date().trim().length() > 0)) {
//                /*
//                 *
//                if (sCreateStartTime.trim().length() == 0) {
//                    sCreateStartTime = "00:00:00";
//                }
//                */
//                date = DateUtil.string2Date(getCreate_start_date()+" "+"00:00:00");
//                
//                //date = this.convertString2Date(getCreate_start_date()+" "+"00:00:00");
//                if (date != null) {
//                    obj.setCreateStartDate(new Timestamp(date.getTime()));
//                }
//            }
//           
//            if ((getCreate_end_date() != null) && (getCreate_end_date().trim().length() > 0)) {
//                /*
//                if (sCreateEndTime.trim().length() == 0) {
//                    sCreateEndTime = "23:59:59";
//                }
//                 */
//                date = DateUtil.string2Date(getCreate_end_date()+" "+"23:59:59");
//               // date = this.convertString2Date(getCreate_end_date()+" "+"00:00:00");
//                
//                if (date != null) {
//                    obj.setCreateEndDate(new Timestamp(date.getTime()));
//                }
//            }
//            
//            // Get array of strings
//            if(getEUID() != null ) {
//                String[] euidArray = getStringEUIDs(getEUID());
//                
//                if(euidArray!=null & euidArray.length >0) {
//                    obj.setEUIDs(euidArray);
//                } else {
//                    obj.setEUIDs(null);
//                }
//            }
//            
//        } catch(ValidationException validationException) {
//            String errorMessage = "Validation Exception";
//            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
//        }
        return potentialDuplicateSearchObject;
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

    /**
     * 
     * @param event
     */
    public void setSearchTypeAction(ActionEvent event){
            String searchTypeValue = (String) event.getComponent().getAttributes().get("searchType");
            //set the search type as per the form
            this.setSearchType(searchTypeValue);
    
   }
    /**
     * 
     * @param event
     */
    public void buildEuidsAction(ActionEvent event){
           try {
            String searchTitle = (String) event.getComponent().getAttributes().get("searchTitle");
            ArrayList euidAtrributeList = (ArrayList) event.getComponent().getAttributes().get("euids");
            
            // we will have to remove the following filtering mechanism when the potential duplicates are properly returned from the SL. 
            Object[] euidAtrributeListObject = euidAtrributeList.toArray();
            ArrayList euidsArray = new ArrayList();
            for(int i=0;i<euidAtrributeListObject.length;i++){
                   if(i==0)euidsArray.add((String)euidAtrributeListObject[0]);
                   if( !((String)euidAtrributeListObject[0]).equalsIgnoreCase(((String)euidAtrributeListObject[i])) ) {
                       euidsArray.add((String)euidAtrributeListObject[i]);
                   }
            }

            String[] euids = new String[euidsArray.size()];
            Object[] finalEuidsArray = euidsArray.toArray();           

            for(int i=0;i<finalEuidsArray.length;i++) {
              euids[i]  = (String) finalEuidsArray[i];
            }
            // End of filtering mechanism

            // use Master MasterControllerService to get an array of enterprise objects.
            MasterControllerService masterControllerService = new MasterControllerService(); 
            ArrayList eoArrayList = masterControllerService.getEnterpriseObjects(euids);
            //keep list array of enterprise objects in the session
            
            session.setAttribute("enterpriseArrayList", eoArrayList);
            session.setAttribute("searchTitle", searchTitle); //Display simple person look up in patient details
            
           } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String toCompareScreen(){
        return "Record Details";
    }

  

    PotentialDuplicateSummary getComparePotentialDuplicateSummary() {
        return comparePotentialDuplicateSummary;
    }

    /**
     * 
     * @param comparePotentialDuplicateSummary
     */
    public void setComparePotentialDuplicateSummary(PotentialDuplicateSummary comparePotentialDuplicateSummary) {
        this.comparePotentialDuplicateSummary = comparePotentialDuplicateSummary;
    }

    public ArrayList getPotentialDuplicateSummaryArray() {
        return potentialDuplicateSummaryArray;
    }

    public void setPotentialDuplicateSummaryArray(ArrayList potentialDuplicateSummaryArray) {
        this.potentialDuplicateSummaryArray = potentialDuplicateSummaryArray;
    }

    public ArrayList getEuidsArrayList(PotentialDuplicateSummary potentialDuplicateSummary) {
        ArrayList strEuid = new ArrayList();
        try {
            strEuid.add(potentialDuplicateSummary.getEUID1());
            PotentialDuplicateIterator potentialDuplicateIterator = potentialDuplicateSummary.getAssociatedPotentialDuplicates();
            PotentialDuplicateSummary[] associatedDuplicates = potentialDuplicateIterator.first(potentialDuplicateIterator.count());
            for (int i = 0; i < associatedDuplicates.length; i++) {
                strEuid.add(associatedDuplicates[i].getEUID1());
            }
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strEuid;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }
    
     /**
     * 
     * @param event
     */
    public void buildCompareDuplicateEuids(ActionEvent event){
            ArrayList euidsMapList = (ArrayList) event.getComponent().getAttributes().get("euidsMap");
            session.setAttribute("comapreEuidsArrayList", euidsMapList);
    }

     
}
