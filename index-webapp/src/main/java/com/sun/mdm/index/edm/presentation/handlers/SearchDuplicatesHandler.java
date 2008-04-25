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
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
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
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;
public class SearchDuplicatesHandler extends ScreenConfiguration {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private HashMap updateableFeildsMap =  new HashMap();    
    private HashMap actionMap =  new HashMap();    
    private ArrayList nonUpdateableFieldsArray = new ArrayList();      
    String errorMessage = null;   
    private  static final String SEARCH_DUPLICATES="Search Duplicates";
    
     /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;   
   // private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler");
    private String searchType = "Advanced Search";
    private static final String  BASIC_SEARCH_RES     = "basicSearchResults";
    private static final String  ADV_SEARCH_RES       = "advancedSearchResults";
    private static final String  POT_DUP_SEARCH_RES   = "potentialduplicates";
    private static final String  VALIDATION_ERROR     = "validationfailed";

    private PotentialDuplicateSummary comparePotentialDuplicateSummary;
    private ArrayList potentialDuplicateSummaryArray;

    private String resolveType = "AutoResolve";
    private String potentialDuplicateId;

    private String mergeEuids = new String();
    private String destnEuid  = new String();
    private String rowCount  = new String();
    private String selectedMergeFields = new String();
    
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

            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
                 mLogger.error(mLocalizer.x("SDP001: Validation failed : {0} ", errorMessage));
                return VALIDATION_ERROR;
            }

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage =bundle.getString("LID_only"); // "Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                 mLogger.error(mLocalizer.x("SDP002: {0} ",errorMessage));
                return VALIDATION_ERROR;

            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
               errorMessage =bundle.getString("enter_LID");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                mLogger.error(mLocalizer.x("SDP003:{0}",errorMessage));
                return VALIDATION_ERROR;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                   errorMessage =bundle.getString("enter_LID");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                   // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                    mLogger.error(mLocalizer.x("SDP004: {0}",errorMessage));
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
                           // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SYSTEM CODE :: " + errorMessage);
                            mLogger.error(mLocalizer.x("SDP005: {0} ", errorMessage));
                            return VALIDATION_ERROR;
                        }
                    } catch (ProcessingException ex) {
                        mLogger.error(mLocalizer.x("SDP006: Failed  during submit {0} ", ex.getMessage()));
                        return VALIDATION_ERROR;
                    } catch (UserException ex) {
                        mLogger.error(mLocalizer.x("SDP007: Failed  during submit {0} ", ex.getMessage()));                       
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
                   // mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    mLogger.error(mLocalizer.x("SDP008: {0}:{1} ", fieldErrors[0] ,fieldErrors[1] ));
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
                   // mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                     mLogger.error(mLocalizer.x("SDP009: {0}:{1} ", fieldErrors[0] ,fieldErrors[1] ));
                    return VALIDATION_ERROR;
                }

            }
        
        //End Validation
        
        
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = getPDSearchObject();
        PotentialDuplicateSummary mainPotentialDuplicateSummary = null;
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        
        try {
              //EPathArrayList epathList  = compareDuplicateManager.retrieveEpathResultsFields(screenObject.getSearchResultsConfig());
              EPathArrayList epathList = retrieveResultsFields(screenObject.getSearchResultsConfig());

            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            
            // Code Added by Pratibha 
            int count = pdPageIterArray.count();
            if(count > 0) {
                httpRequest.setAttribute("duplicateSearchObject", potentialDuplicateSearchObject);                
            }    
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
            ArrayList newFinalArray  = new ArrayList();        
            float wt = 0.0f;
            for (int i = 0; i < finalArrayList.size(); i++) {
                ArrayList newInnerArray = new ArrayList();
                ArrayList innerArrayList = (ArrayList) finalArrayList.get(i);
                for (int j = 0; j < innerArrayList.size(); j++) {
                    String euids = (String) innerArrayList.get(j);
                    EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids);
                    HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                    //System.out.println(eoMap1);
                    //HashMap eoMap = new HashMap();
                    eoMap.put("ENTERPRISE_OBJECT_PREVIEW", getValuesForResultFields(eo, retrieveEPathsResultsFields(screenObject.getSearchResultsConfig())));
                    eoMap.put("EUID", eo.getEUID());
                    if (j > 0) {
                        //Add weight to the hashmap 
                        //eoMap.put("Weight", masterControllerService.getPotentialDuplicateWeight((String) innerArrayList.get(0), euids));
//                   eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateID((String) innerArrayList.get(0), euids));
//                   eoMap.put("Status", masterControllerService.getPotentialDuplicateStatus((String) innerArrayList.get(0), euids));

                        eoMap.put("Weight", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "WEIGHT"));
                        eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "duplicateid"));
                        eoMap.put("Status", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "status"));
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
                    mLogger.error(mLocalizer.x("SDP010: Unable to perform submit :{0} ", ex.getMessage()));
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("SDP011: Unable to perform submit :{0} ", ex.getMessage()));
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("SDP012: Unable to perform submit :{0} ", ex.getMessage()));
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof PageException)) {
                    mLogger.error(mLocalizer.x("SDP013: Unable to perform submit :{0} ", ex.getMessage()));
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof RemoteException)) {
                    mLogger.error(mLocalizer.x("SDP014: Unable to perform submit :{0} ", ex.getMessage()));
                }else
                { //mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                     mLogger.error(mLocalizer.x("SDP015: Unable to perform submit :{0} ", ex.getMessage()));
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
        try {

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
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                           // mLogger.error("LID/SYSTEM CODE:: " + errorMessage);
                             mLogger.error(mLocalizer.x("SDP036: LID/SYSTEM CODE: {0} ", errorMessage));
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
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        mLogger.error(mLocalizer.x("SDP016: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()));
                    } catch (UserException ex) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), ex.toString()));
                        mLogger.error(mLocalizer.x("SDP017: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()));
                    }
                }
            }

            //set EUID VALUE IF lid/system code not supplied
            if (super.getUpdateableFeildsMap().get("EUID") != null && super.getUpdateableFeildsMap().get("EUID").toString().trim().length() > 0) {
//            // Get array of strings
                String[] euidArray = getStringEUIDs((String) super.getUpdateableFeildsMap().get("EUID"));

                if (euidArray != null & euidArray.length > 0) {
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
                    //java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
                    mLogger.error(mLocalizer.x("SDP018: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()));
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
            //EPathArrayList epathList  = compareDuplicateManager.retrieveEpathResultsFields(screenObject.getSearchResultsConfig());
            EPathArrayList epathList = retrieveResultsFields(screenObject.getSearchResultsConfig());

            //set fields to retrieve
            potentialDuplicateSearchObject.setFieldsToRetrieve(epathList);
            //Set max page results and page size here
            potentialDuplicateSearchObject.setMaxElements(super.getMaxRecords());
            potentialDuplicateSearchObject.setPageSize(super.getPageSize());
           

        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SDP019: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()));
        }
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

    public void setSearchTypeAction(ActionEvent event){
            String searchTypeValue = (String) event.getComponent().getAttributes().get("searchType");
            //set the search type as per the form
            this.setSearchType(searchTypeValue);  
   }

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
            mLogger.error(mLocalizer.x("SDP020: Encountered an Exception : {0} ", ex.getMessage()));
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
           mLogger.error(mLocalizer.x("SDP040: Failed to get the EUIDs  : {0} ", ex.getMessage()));
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SDP041: Failed to get the EUIDs  : {0} ", ex.getMessage()));
        }
        return strEuid;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }
    
    public void buildCompareDuplicateEuids(ActionEvent event){
            ArrayList euidsMapList = (ArrayList) event.getComponent().getAttributes().get("euidsMap");
            session.setAttribute("comapreEuidsArrayList", euidsMapList);
    }

	public void resolvePotentialDuplicate(ActionEvent event) {
        try {
            ArrayList duplicatesArray = (ArrayList) event.getComponent().getAttributes().get("finalArrayListVE");
        
            //resolve the potential duplicate as per resolve type
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(this.getResolveType())) ? false : true;
            String resolveString = ("AutoResolve".equalsIgnoreCase(this.getResolveType())) ? "R": "A";

            //flag=false incase of autoresolve
            //flag = true incase of permanant resolve
 
            masterControllerService.setAsDifferentPerson(this.getPotentialDuplicateId(), resolveBoolean);
            httpRequest.removeAttribute("finalArrayList");
          
            ArrayList finalDuplicatesList = new ArrayList();
            //reset the status and set it back in session
            for (int i = 0; i < duplicatesArray.size(); i++) {
                ArrayList arlInner = (ArrayList) duplicatesArray.get(i);

                ArrayList arlInnerTemp = new ArrayList();
                for (int j = 0; j < arlInner.size(); j++) {
                    HashMap objectHashMap = (HashMap) arlInner.get(j);
                    //set the resolve type for the selected potential duplicate
                    if (this.getPotentialDuplicateId().equals((String) objectHashMap.get("PotDupId"))) {
                        objectHashMap.put("Status", resolveString);
                    }
                    arlInnerTemp.add(objectHashMap);
                }
                finalDuplicatesList.add(arlInnerTemp);
            }

			httpRequest.setAttribute("finalArrayList", finalDuplicatesList);                
        } catch (ProcessingException ex) {
             mLogger.error(mLocalizer.x("SDP021: Unable to resolve  PotentialDuplicates : {0} ", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
        } catch (UserException ex) {
             mLogger.error(mLocalizer.x("SDP022: Unable to resolve PotentialDuplicates : {0} ", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
        }
   
    }

    public void unresolvePotentialDuplicateAction(ActionEvent event) {
        try {
            //get potential duplicate ID
            String potDupId = (String) event.getComponent().getAttributes().get("potDupId");
            ArrayList duplicatesArray = (ArrayList) event.getComponent().getAttributes().get("finalArrayListVE");

            //un resolve the potential duplicate 
            masterControllerService.unresolvePotentialDuplicate(potDupId);

            httpRequest.removeAttribute("finalArrayList");
          
            ArrayList finalDuplicatesList = new ArrayList();
            //reset the status and set it back in session
            for (int i = 0; i < duplicatesArray.size(); i++) {
                ArrayList arlInner = (ArrayList) duplicatesArray.get(i);

                ArrayList arlInnerTemp = new ArrayList();
                for (int j = 0; j < arlInner.size(); j++) {
                    HashMap objectHashMap = (HashMap) arlInner.get(j);
                    //set the resolve type for the selected potential duplicate
                    if (potDupId.equals((String) objectHashMap.get("PotDupId"))) {
                        objectHashMap.put("Status", "U");
                    }
                    arlInnerTemp.add(objectHashMap);
                }
                finalDuplicatesList.add(arlInnerTemp);
            }
            httpRequest.setAttribute("finalArrayList", finalDuplicatesList);                

  
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
           mLogger.error(mLocalizer.x("SDP023: Unable  to unResolve PotentialDuplicates : {0} ", ex.getMessage()));

        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error(mLocalizer.x("SDP024: Unable to unResolve PotentialDuplicates : {0} ", ex.getMessage()));
        }

    }

    
        public void previewPostMultiMergedEnterpriseObject(ActionEvent event) {
            ArrayList duplicatesArray = (ArrayList) event.getComponent().getAttributes().get("finalArrayListVE");
            PotentialDuplicateSearchObject duplicateSearchObject = (PotentialDuplicateSearchObject) event.getComponent().getAttributes().get("duplicateSearchObjectVE");
            httpRequest.setAttribute("duplicateSearchObject", duplicateSearchObject);                
        try {
            //httpRequest.setAttribute("comapreEuidsArrayList", httpRequest.getAttribute("comapreEuidsArrayList"));

            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnEuid);
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();


            String[] allEUIDs = mergeEuids.split("##");
            
            
            ArrayList srcsList  = new ArrayList();
            for (int i = 0; i < allEUIDs.length; i++) {
                if(i !=0 ) {
                    srcsList.add(allEUIDs[i]);
                }
            }    
            
            Object[] sourceEUIDObjs =  srcsList.toArray();
            
            String[] sourceEUIDs  = new String[srcsList.size()];
            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];

            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            httpRequest.setAttribute("sourceEUIDs" + getRowCount(), sourceEUIDs);

            httpRequest.setAttribute("destnEuid"+ getRowCount(), destnEuid);
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            EnterpriseObject resulteo = masterControllerService.getPostMergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);
            HashMap eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);
            httpRequest.setAttribute("eoMultiMergePreview" + getRowCount(), eoMultiMergePreview);
            
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
             mLogger.error(mLocalizer.x("SDP037: Unable to unResolve PotentialDuplicates : {0} ", ex.getMessage()));
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error(mLocalizer.x("SDP025: Unable to unResolve PotentialDuplicates : {0} ", ex.getMessage()));
        }
         httpRequest.setAttribute("finalArrayList", duplicatesArray);                
        
}
    
public void cancelMultiMergeOperation(ActionEvent event) {
        PotentialDuplicateSearchObject duplicateSearchObject = (PotentialDuplicateSearchObject) event.getComponent().getAttributes().get("duplicateSearchObjectVE");
        httpRequest.setAttribute("duplicateSearchObject", duplicateSearchObject);                

        //Reset the search criteria here
        ArrayList finalArrayList  = resetOutputList(duplicateSearchObject);
        httpRequest.setAttribute("finalArrayList", finalArrayList);                
        
}  

public void performMultiMergeEnterpriseObject(ActionEvent event) {
        PotentialDuplicateSearchObject duplicateSearchObject = (PotentialDuplicateSearchObject) event.getComponent().getAttributes().get("duplicateSearchObjectVE");
        httpRequest.setAttribute("duplicateSearchObject", duplicateSearchObject);                
        try {
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnEuid);
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();
            String[] allEUIDs = mergeEuids.split("##");
             ArrayList srcsList  = new ArrayList();
            for (int i = 0; i < allEUIDs.length; i++) {
                if(i !=0 ) {
                    srcsList.add(allEUIDs[i]);
                }
            }    
            
            Object[] sourceEUIDObjs =  srcsList.toArray();
            
            String[] sourceEUIDs  = new String[srcsList.size()];
            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];
            
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            masterControllerService.mergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);              
            
            
        } catch (ValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
             mLogger.error(mLocalizer.x("SDP026: Unable to perform MultiMergeEnterpriseObject : {0} ", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
             mLogger.error(mLocalizer.x("SDP027: Unable to perform MultiMergeEnterpriseObject : {0} ", ex.getMessage()));
        }
      //Insert Audit logs 
       try {
       //String userName, String euid1, String euid2, String function, int screeneID, String detail
        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               destnEuid, 
                                               "",
                                               "EUID Multi Merge Confirm",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View two selected EUIDs of the merge confirm page");
        } catch (UserException ex) {   
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
           mLogger.error(mLocalizer.x("SDP028: Failed to insert AuditLogs : {0} ", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
           mLogger.error(mLocalizer.x("SDP029: Failed to insert AuditLogs : {0} ", ex.getMessage()));
        }
        
        httpRequest.removeAttribute("finalArrayList");
        
        //Reset the search criteria here
        ArrayList finalArrayList  = resetOutputList(duplicateSearchObject);
        httpRequest.setAttribute("finalArrayList", finalArrayList);                

}        

public ArrayList resetOutputList(PotentialDuplicateSearchObject potentialDuplicateSearchObject ) {
       ArrayList newFinalArray  = new ArrayList();        
        try {
            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            
            // Code Added by Pratibha 
            int count = pdPageIterArray.count();
            if(count > 0) {
                httpRequest.setAttribute("duplicateSearchObject", potentialDuplicateSearchObject);                
            }    
            String[][] temp = new String[count][2];

            if (pdPageIterArray != null & pdPageIterArray.count() > 0) {
                // add all the potential duplicates to the summary array  
                while (pdPageIterArray.hasNext()) {
                    PotentialDuplicateSummary pds[] = pdPageIterArray.first(pdPageIterArray.count());
                    for(int i=0;i<pds.length;i++)
                    {   
                        String euid1 = pds[i].getEUID1();
                        String euid2 = pds[i].getEUID2();

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
            float wt = 0.0f;
            for (int i = 0; i < finalArrayList.size(); i++) {
                ArrayList newInnerArray  = new ArrayList();        
                ArrayList innerArrayList = (ArrayList) finalArrayList.get(i);
                for (int j = 0; j < innerArrayList.size(); j++) {
                    String euids = (String) innerArrayList.get(j);
                    EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids);
                    HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                    //set the values for the preview
                    eoMap.put("ENTERPRISE_OBJECT_PREVIEW", getValuesForResultFields(eo, retrieveEPathsResultsFields(screenObject.getSearchResultsConfig())));
                    eoMap.put("EUID", eo.getEUID());

                    if (j > 0) {
                        //Add weight to the hashmap 
                        //eoMap.put("Weight", masterControllerService.getPotentialDuplicateWeight((String) innerArrayList.get(0), euids));
//                   eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateID((String) innerArrayList.get(0), euids));
//                   eoMap.put("Status", masterControllerService.getPotentialDuplicateStatus((String) innerArrayList.get(0), euids));

                        eoMap.put("Weight", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "WEIGHT"));
                        eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "duplicateid"));
                        eoMap.put("Status", masterControllerService.getPotentialDuplicateFromKey((String) innerArrayList.get(0), euids, "status"));
                    } else {
                        eoMap.put("Weight", wt);
                        eoMap.put("PotDupId", "000");
                        eoMap.put("Status", "U");
                    }

                    newInnerArray.add(eoMap);
                }

                newFinalArray.add(newInnerArray);
            }
        } catch (Exception ex) {
               // UserException and ValidationException don't need a stack trace.
                // ProcessingException stack trace logged by MC
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("SDP030: Failed to reset OutputList : {0} ", ex.getMessage()));
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("SDP031: Failed to reset OutputList : {0} ", ex.getMessage()));
                } else if (!(ex instanceof ProcessingException)) {
                   mLogger.error(mLocalizer.x("SDP032: Failed to reset OutputList : {0} ", ex.getMessage()));
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof PageException)) {
                   mLogger.error(mLocalizer.x("SDP033: Failed to reset OutputList : {0} ", ex.getMessage()));
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof RemoteException)) {
                   mLogger.error(mLocalizer.x("SDP034: Failed to reset OutputList : {0} ", ex.getMessage()));
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                }else
                { mLogger.error(mLocalizer.x("SDP035: Failed to reset OutputList : {0} ", ex.getMessage()));
                }
        }
    
       return newFinalArray;
}
    
    
    
    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public String getPotentialDuplicateId() {
        return potentialDuplicateId;
    }

    public void setPotentialDuplicateId(String potentialDuplicateId) {
        this.potentialDuplicateId = potentialDuplicateId;
    }

    public String getMergeEuids() {
        return mergeEuids;
    }

    public void setMergeEuids(String mergeEuids) {
        this.mergeEuids = mergeEuids;
    }

    public String getDestnEuid() {
        return destnEuid;
    }

    public void setDestnEuid(String destnEuid) {
        this.destnEuid = destnEuid;
    }

    public String getSelectedMergeFields() {
        return selectedMergeFields;
    }

    public void setSelectedMergeFields(String selectedMergeFields) {
        this.selectedMergeFields = selectedMergeFields;
    }

    public String getRowCount() {
        return rowCount;
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    
    public static Collection getFieldValue(ObjectNode objNode, EPath epath) throws Exception {
        Collection c = null;
        try {
            c = QwsUtil.getValueForField(objNode, epath.getName(), null);
            if (c == null) {
                return null;
            } else {
                return c;
            }
        } catch (Exception e) {
            mLogger.error(mLocalizer.x("SDP038: Failed to get Field values  : {0} ", e.getMessage()));
        }
        return c;
    }
// Method added to handle Service Layer dynamic result fields

public EPathArrayList retrieveResultsFields(ArrayList arlResultsConfig) throws Exception {
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
                // copy EPath strings to the EPathArrayList
                arlResultFields.add("Enterprise.SystemSBR." + strEPath);
                // POTENTIAL DUPLICATE-RELATED FIX from Raymond
                // retrieve the object reference eg, if the epath is is "Person.Address.City" this retrieves "Person".
                if (objectRef == null) {
                    int index = strEPath.indexOf(".");
                    objectRef = strEPath.substring(0, index);
                 
                }
            //
            }
            // POTENTIAL DUPLICATE-RELATED FIX from Raymond
            // Add an EUID field for the PotentialDuplicateAManager.  This is required.
            arlResultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");
        }

       
        return arlResultFields;
    }

public EPathArrayList retrieveEPathsResultsFields(ArrayList arlResultsConfig) throws Exception {
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
                // copy EPath strings to the EPathArrayList
                arlResultFields.add(strEPath);
                // POTENTIAL DUPLICATE-RELATED FIX from Raymond
                // retrieve the object reference eg, if the epath is is "Person.Address.City" this retrieves "Person".
//                if (objectRef == null) {
//                    int index = strEPath.indexOf(".");
//                    objectRef = strEPath.substring(0, index);
//                 
//                }
            //
            }
            // POTENTIAL DUPLICATE-RELATED FIX from Raymond
            // Add an EUID field for the PotentialDuplicateAManager.  This is required.
            // arlResultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");
        }

       
        return arlResultFields;
    }

    private HashMap getValuesForResultFields(EnterpriseObject eo, EPathArrayList retrieveResultsFields) throws ObjectException, EPathException {
        // System.out.println("<<=== eo " + eo);
        // System.out.println("<<=== retrieveResultsFields " + retrieveResultsFields);  
        HashMap resultHashMap=new HashMap();
        ArrayList fieldConfigArray = super.getResultsConfigArray();
        //System.out.println("===================================================");
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        String dateField = new String();
        if(retrieveResultsFields!=null){
            for (int i = 0; i < retrieveResultsFields.size(); i++) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigArray.get(i);
                String epath = retrieveResultsFields.get(i).toString();
                Object value = EPathAPI.getFieldValue(epath, eo.getSBR().getObject());
                if (value instanceof java.util.Date) {
                    dateField = simpleDateFormatFields.format(value);
                    resultHashMap.put(fieldConfig.getFullFieldName(), dateField);
                } else {
                    if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && value != null) {
                        //value
                        resultHashMap.put(fieldConfig.getFullFieldName(), ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString()));
                    } else {
                        resultHashMap.put(fieldConfig.getFullFieldName(), value);
                    }
                }

                //System.out.println("epath : " + retrieveResultsFields.get(i));
                //System.out.println("value : " + value);
                //resultHashMap.put(epath, value);
            }
        }
        
        //System.out.println("===================================================" + resultHashMap);
        return resultHashMap;
    }

    
}

