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
 *  
 */ 

package com.sun.mdm.index.edm.presentation.handlers; 

import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
import com.sun.mdm.index.edm.presentation.security.Operations;
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
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import net.java.hulp.i18n.LocalizationSupport;
public class SearchDuplicatesHandler extends ScreenConfiguration {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private HashMap parametersMap =  new HashMap();    
    private HashMap actionMap =  new HashMap();    
    private ArrayList nonUpdateableFieldsArray = new ArrayList();      
    Operations operations = new Operations();
    String errorMessage = null;   
    private  static final String SEARCH_DUPLICATES="Search Duplicates";
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    public static final String CONCURRENT_MOD_ERROR = "CONCURRENT_MOD_ERROR";    
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
    private PotentialDuplicateSearchObject pdSearchObject;
    /** Creates a new instance of SearchDuplicatesHandler */
    public SearchDuplicatesHandler() {
    }


    public ArrayList performSubmit() throws ProcessingException, UserException, ValidationException,HandlerException{
            super.setUpdateableFeildsMap(parametersMap);

            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
                 mLogger.info(mLocalizer.x("SDP001: Validation failed : {0} ", errorMessage));
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

            //Added to check the format of the user enter LID value adheres to the 
            //System defined LID format
            if((getUpdateableFeildsMap().get("LID") != null && getUpdateableFeildsMap().get("LID").toString().trim().length() > 0)) {
                if (!super.checkMasking((String)getUpdateableFeildsMap().get("LID"), (String)getUpdateableFeildsMap().get("lidmask"))) {
                    String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
                    String messages = localIdDesignation + " " + bundle.getString("lid_format_error_text") + " " +(String)getUpdateableFeildsMap().get("lidmask");
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, messages,messages));
                   ArrayList lidErrorList = new ArrayList();            
                   HashMap lidError = new HashMap();  //fix of 6703149
                   lidError.put("LID_SYSTEM_CODE_ERROR",messages);
                   lidErrorList.add(lidError);
                   return lidErrorList;
                }                  
            }
            
            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage =bundle.getString("LID_only"); // "Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                 mLogger.info(mLocalizer.x("SDP002: {0} ",errorMessage));
               ArrayList lidErrorList = new ArrayList();            
               HashMap lidError = new HashMap();                   //fix of 6703149
               lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
               lidErrorList.add(lidError);
               return lidErrorList;
            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
               errorMessage =bundle.getString("enter_LID");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                //mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                mLogger.info(mLocalizer.x("SDP003:{0}",errorMessage));
               ArrayList lidErrorList = new ArrayList();            
               HashMap lidError = new HashMap();                  //fix of 6703149
               lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
               lidErrorList.add(lidError);
               return lidErrorList;
            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                   errorMessage =bundle.getString("enter_LID");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  errorMessage, errorMessage));
                   // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                    mLogger.info(mLocalizer.x("SDP004: {0}",errorMessage));
                   ArrayList lidErrorList = new ArrayList();            //fix of 6703149
                   HashMap lidError = new HashMap();                      
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
                           // mLogger.error("Validation failed. Message displayed to the user: " + "LID/SYSTEM CODE :: " + errorMessage);
                            mLogger.info(mLocalizer.x("SDP005: {0} ", errorMessage));
                           ArrayList lidErrorList = new ArrayList();            //fix of 6703149
                           HashMap lidError = new HashMap();  
                           lidError.put("LID_SYSTEM_CODE_ERROR",errorMessage);
                           lidErrorList.add(lidError);
                           return lidErrorList;
                        }
                    } catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("SDP006: Service Layer Validation Exception has occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("SDP007: Service Layer User Exception occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("SDP008: Error  occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof ProcessingException) {
                            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                            if (exceptionMessage.indexOf("stack trace") != -1) {
                                String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                                if (exceptionMessage.indexOf("message=") != -1) {
                                    parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                                }
                                mLogger.error(mLocalizer.x("SDP009: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                            } else {
                                mLogger.error(mLocalizer.x("SDP010: Error  occurred"), ex);
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
                   // mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    mLogger.error(mLocalizer.x("SDP011: {0}:{1} ", fieldErrors[0] ,fieldErrors[1] ));
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
                   // mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                     mLogger.error(mLocalizer.x("SDP012: {0}:{1} ", fieldErrors[0] ,fieldErrors[1] ));
                    return null;
                }

            }
        
        //End Validation
        
        
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = getPDSearchObject();
        if(potentialDuplicateSearchObject == null) {
            return null;
        }
        
        MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
       
        //Build and arraylist of hashmaps for the duplicates before putting in the request
        ArrayList newFinalArray  = new ArrayList();        
        
        try {
              //EPathArrayList epathList  = midmUtilityManager.retrieveEpathResultsFields(screenObject.getSearchResultsConfig());
              EPathArrayList epathList = retrieveResultsFields(screenObject.getSearchResultsConfig());

            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            
            // Code Added by Pratibha 
            int count = pdPageIterArray.count();
            if(count > 0) {
                httpRequest.setAttribute("duplicateSearchObject", potentialDuplicateSearchObject);                
                setPdSearchObject(potentialDuplicateSearchObject);
            }    
            String[][] temp = new String[count][2];

            HashMap duplicatesHashMap = new HashMap();
            if (pdPageIterArray != null & pdPageIterArray.count() > 0) {
                // add all the potential duplicates to the summary array  
                while (pdPageIterArray.hasNext()) {
                    PotentialDuplicateSummary pds[] = pdPageIterArray.first(pdPageIterArray.count());
                    for(int i=0;i<pds.length;i++)
                    {   
                        String euid1 = pds[i].getEUID1();
                        String euid2 = pds[i].getEUID2();
                        //keep the object nodes as well here in the hashmap
                        duplicatesHashMap.put(euid1, pds[i].getObject1());
                        duplicatesHashMap.put(euid2, pds[i].getObject2());

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

            float wt = 0.0f;
            for (int i = 0; i < finalArrayList.size(); i++) {
                ArrayList newInnerArray = new ArrayList();
                ArrayList innerArrayList = (ArrayList) finalArrayList.get(i);
                for (int j = 0; j < innerArrayList.size(); j++) {
                    String euids = (String) innerArrayList.get(j);
                    EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids);
                    HashMap eoMap = new HashMap();
                    eoMap.put("ENTERPRISE_OBJECT_PREVIEW", getValuesForResultFields(eo, retrieveEPathsResultsFields(screenObject.getSearchResultsConfig())));
                    eoMap.put("EUID", eo.getEUID());
                    //Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());

                    session.setAttribute("SBR_REVISION_NUMBER"+eo.getEUID(),eo.getSBR().getRevisionNumber());
                    //eoMap.put("ENTERPRISE_OBJECT_PREVIEW", getValuesForResultFields((ObjectNode) duplicatesHashMap.get(euids), retrieveEPathsResultsFields(screenObject.getSearchResultsConfig())));
                    //eoMap.put("EUID", euids);
                    
                    if (j > 0) {
                        //Add weight/PotDupId/Status to the hashmap 
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
            //httpRequest.setAttribute("finalArrayList", newFinalArray);                
            session.setAttribute("finalArrayList", newFinalArray);                
        
          } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP013: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP014: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP015: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP016: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP017: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
             }

            return null;

        }
        return newFinalArray;
    }

    //This method is used to set the array list of duplicates in the session for the compare duplicates page.
    public void buildCompareDuplicateEuids(String compareEuids){
            String[] compareEuidsSplitted = getStringEUIDs(compareEuids);
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
            float wt = 0.0f;
            ArrayList compareEOArrayList = new ArrayList();
            try {
            for (int j = 0; j < compareEuidsSplitted.length; j++) {
                String duplicateEuid = compareEuidsSplitted[j];
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(duplicateEuid);
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                //HashMap eoMap = new HashMap();
                eoMap.put("EUID", eo.getEUID());
                if (j > 0) {
                    eoMap.put("Weight", masterControllerService.getPotentialDuplicateFromKey(compareEuidsSplitted[0], duplicateEuid, "WEIGHT"));
                    eoMap.put("PotDupId", masterControllerService.getPotentialDuplicateFromKey(compareEuidsSplitted[0], duplicateEuid, "duplicateid"));
                    eoMap.put("Status", masterControllerService.getPotentialDuplicateFromKey(compareEuidsSplitted[0], duplicateEuid, "status"));
                } else {
                    eoMap.put("Weight", wt);
                    eoMap.put("PotDupId", "000");
                    eoMap.put("Status", "U");
                }
                compareEOArrayList.add(eoMap);
                }
          
           } catch (Exception ex) {
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("SDP018: Service Layer Validation Exception has occurred"), ex);
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("SDP019: Service Layer User Exception occurred"), ex);
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("SDP020: Error  occurred"), ex);
                } else if (ex instanceof ProcessingException) {
                    String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                    if (exceptionMessage.indexOf("stack trace") != -1) {
                        String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                        if (exceptionMessage.indexOf("message=") != -1) {
                            parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                        }
                        mLogger.error(mLocalizer.x("SDP021: Error  occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                    } else {
                        mLogger.error(mLocalizer.x("SDP022: Error  occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                    }
                }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
        session.setAttribute("comapreEuidsArrayList", compareEOArrayList);
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
                           mLogger.info(mLocalizer.x("SDP023: LID/SYSTEM CODE: {0} ", errorMessage));
                           return null;
                                                          
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

                    } catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("SDP024: Service Layer Validation Exception has occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("SDP025: Service Layer User Exception occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("SDP026: Error  occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof ProcessingException) {
                            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                            if (exceptionMessage.indexOf("stack trace") != -1) {
                                String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                                if (exceptionMessage.indexOf("message=") != -1) {
                                    parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                                }
                                mLogger.error(mLocalizer.x("SDP027: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                            } else {
                                mLogger.error(mLocalizer.x("SDP028: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                            }

                        }
                        return null;
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


            //Set StartDate to the potentialDuplicateSearchObject
            if (super.getUpdateableFeildsMap().get("create_start_date") != null && super.getUpdateableFeildsMap().get("create_start_date").toString().trim().length() > 0) {
                try {
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
                     mLogger.error(mLocalizer.x("SDP029: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()),ex);
                     return null;
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


            //Set StartDate to the potentialDuplicateSearchObject
            if (super.getUpdateableFeildsMap().get("create_end_date") != null && super.getUpdateableFeildsMap().get("create_end_date").toString().trim().length() > 0) {
                try {
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
                    mLogger.error(mLocalizer.x("SDP030: Failed to set PotentialDuplicate search objects: {0} ", ex.getMessage()),ex);
                    return null;
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
            //EPathArrayList epathList  = midmUtilityManager.retrieveEpathResultsFields(screenObject.getSearchResultsConfig());
            EPathArrayList epathList = retrieveResultsFields(screenObject.getSearchResultsConfig());

            //set fields to retrieve
            potentialDuplicateSearchObject.setFieldsToRetrieve(epathList);
            //Set max page results and page size here
            potentialDuplicateSearchObject.setMaxElements(super.getMaxRecords());
            potentialDuplicateSearchObject.setPageSize(super.getPageSize());
           

        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SDP031: Failed to get PotentialDuplicate search objects: {0} ", ex.getMessage()),ex); 
            return null;
           
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
            mLogger.error(mLocalizer.x("SDP032: Encountered an Exception : {0} ", ex.getMessage()),ex);
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

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP033: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP034: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP035: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP036: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP037: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
        }
        return strEuid;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
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
            mLogger.error(mLocalizer.x("SDP038: Failed to get Field values  : {0} ", e.getMessage()),e);
        }
        return c;
    }
// Method added to handle Service Layer dynamic result fields

public EPathArrayList retrieveResultsFields(ArrayList arlResultsConfig) throws Exception {
        EPathArrayList arlResultFields = new EPathArrayList();
        SearchResultsConfig searchResultConfig = null;
        ArrayList arlEPaths = null;
        Iterator ePathsIterator = null;
        
 if(arlResultsConfig!= null){
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

    private HashMap getValuesForResultFields(EnterpriseObject eo, EPathArrayList retrieveResultsFields) throws ObjectException, EPathException, Exception {
        ConfigManager.init();
        //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
        //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
        boolean hasSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(eo.getSBR()) : true;
        HashMap resultHashMap=new HashMap();
        ArrayList fieldConfigArray = super.getResultsConfigArray();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
        String strVal = new String();
        String dateField = new String();
        if(retrieveResultsFields!=null){
            for (int i = 0; i < retrieveResultsFields.size(); i++) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigArray.get(i);
                String epath = retrieveResultsFields.get(i).toString();
                Object value = EPathAPI.getFieldValue(epath, eo.getSBR().getObject());
                if (value instanceof java.util.Date) {
                    dateField = simpleDateFormatFields.format(value);
               
                    if (value!=null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
                         resultHashMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                    } else {
                        resultHashMap.put(fieldConfig.getFullFieldName(), dateField);
                    }                
                    //resultHashMap.put(fieldConfig.getFullFieldName(), dateField);
                } else {
                    if (value!=null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
                        resultHashMap.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                    } else {
                        if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && value != null) {

                            //SET THE VALUES WITH USER CODES AND VALUE LIST 
                            if (fieldConfig.getUserCode() != null) {
                                strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                            } else {
                                strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                            }

                            // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                            //value
                            resultHashMap.put(fieldConfig.getFullFieldName(), strVal);
                        } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                            if (value != null) {
                                //Mask the value as per the masking 
                                value = fieldConfig.mask(value.toString());
                                resultHashMap.put(fieldConfig.getFullFieldName(), value);
                            }
                        } else {
                            resultHashMap.put(fieldConfig.getFullFieldName(), value);
                        }
                    }

                }

                //resultHashMap.put(epath, value);
            }
        }
        
        return resultHashMap;
    }
    private HashMap getValuesForResultFields(ObjectNode objectNode, EPathArrayList retrieveResultsFields) throws ObjectException, EPathException {
        HashMap resultHashMap=new HashMap();
        ArrayList fieldConfigArray = super.getResultsConfigArray();
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
        String dateField = new String();
        if(retrieveResultsFields!=null){
            for (int i = 0; i < retrieveResultsFields.size(); i++) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigArray.get(i);
                String epath = retrieveResultsFields.get(i).toString();
                Object value = EPathAPI.getFieldValue(epath, objectNode);
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

                //resultHashMap.put(epath, value);
            }
        }
        return resultHashMap;
    }

    public HashMap getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
    }

    public PotentialDuplicateSearchObject getPdSearchObject() {
        return pdSearchObject;
    }

    public void setPdSearchObject(PotentialDuplicateSearchObject pdSearchObject) {
        this.pdSearchObject = pdSearchObject;
    }


    
    public void resolvePotentialDuplicate(HashMap resolveDuplicatesMap) {
        try {
            String potDupId = (String) resolveDuplicatesMap.get("potentialDuplicateId");
            String resolveTypeSelected = (String) resolveDuplicatesMap.get("resolveType");
            //resolve the potential duplicate as per resolve type
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(resolveTypeSelected)) ? false : true;
            
            //flag=false incase of autoresolve
            //flag = true incase of permanant resolve
 
            masterControllerService.setAsDifferentPerson(potDupId, resolveBoolean);
          

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP039: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP040: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP041: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP042: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP043: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
        }

    }

    public void unresolvePotentialDuplicateAction(HashMap resolveDuplicatesMap) {
        try {
            //get potential duplicate ID
            String potDupId = (String) resolveDuplicatesMap.get("potentialDuplicateId");
            
            //un resolve the potential duplicate 
            masterControllerService.unresolvePotentialDuplicate(potDupId);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP044: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP045: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP046: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP047: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP048: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
        }

    }

    
  public HashMap previewPostMultiMergedEnterpriseObject(String[]  sourceDestnEuids,String rowCount) {
        HashMap finalPreviewMap = new HashMap();
        try {
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
 
            //Check if the EUID is merged
            String mergedEuid  = midmUtilityManager.getMergedEuid(sourceDestnEuids[0]);// modified as fix of 202
            if(mergedEuid != null && mergedEuid.length() > 0) {
                finalPreviewMap.put("IS_EUID_MERGED",sourceDestnEuids[0]);
                finalPreviewMap.put("MERGED_EUID",mergedEuid);
                return finalPreviewMap;
             }
            //String destnEuidValue = (String) previewDuplicatesMap.get("destnEuid");
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(sourceDestnEuids[0]);
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();

            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            // modifying as part of fixing 202 on 12-11-08             
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                finalPreviewMap.put(SearchDuplicatesHandler.CONCURRENT_MOD_ERROR,bundle.getString("concurrent_mod_text"));
                finalPreviewMap.put("DESTN_EUID",destinationEO.getEUID());
                return finalPreviewMap;
            }
             
            
            ArrayList srcsList  = new ArrayList();
            for (int i = 0; i < sourceDestnEuids.length; i++) {
                if(i !=0 ) {
                    srcsList.add(sourceDestnEuids[i]);
                }
            }    
            
            Object[] sourceEUIDObjs =  srcsList.toArray();            
            String[] sourceEUIDs  = new String[srcsList.size()];            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];


             //Check if the EUID is merged - Fix for #202
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                String mergedEuidDuplicate = midmUtilityManager.getMergedEuid(sourceEuid);// modified as fix of 202
                if (mergedEuidDuplicate != null && mergedEuidDuplicate.length() > 0) {
                    finalPreviewMap.put("IS_EUID_MERGED", sourceEuid);
                    finalPreviewMap.put("MERGED_EUID", mergedEuidDuplicate);
                    return finalPreviewMap;
                }

            }

            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
            // modifying as part of fixing 202 on 12-11-08                 
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
                sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+sourceEuid);
                if(new Integer(srcRevisionNumbers[i]).intValue() != sessionRevisionNumber.intValue() ) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                    finalPreviewMap.put(SearchDuplicatesHandler.CONCURRENT_MOD_ERROR,bundle.getString("concurrent_mod_text"));
                    finalPreviewMap.put("DESTN_EUID",sourceEuid);
                    return finalPreviewMap;
                }
                
            }
            EnterpriseObject resulteo = masterControllerService.getPostMergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);
            HashMap eoMultiMergePreview = new HashMap();//midmUtilityManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);
            eoMultiMergePreview.put("ENTERPRISE_OBJECT_PREVIEW", getValuesForResultFields(resulteo, retrieveEPathsResultsFields(screenObject.getSearchResultsConfig())));           
            eoMultiMergePreview.put("EUID", resulteo.getEUID());
            finalPreviewMap.put("eoMultiMergePreview" + rowCount, eoMultiMergePreview);
            //PUT THE DESTINATION AND SOURCE EUIDS for selecting it by default in the preview screen
             for (int i = 0; i < sourceDestnEuids.length; i++) {
                finalPreviewMap.put(sourceDestnEuids[i] + ":" + rowCount, sourceDestnEuids[i] + rowCount);
             }
             
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP049: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP050: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP051: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP052: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP053: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
       }
           
         return finalPreviewMap;
}
  
  public HashMap performMultiMergeEnterpriseObject(String[]  sourceDestnEuids,String rowCount) {
        HashMap finalMergeHashMap = new HashMap();
        EnterpriseObject resultingEO = null;
        try {
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
 
            //Check if the EUID is merged
            String mergedEuid  = midmUtilityManager.getMergedEuid(sourceDestnEuids[0]);// modified as fix of 202
            if(mergedEuid != null && mergedEuid.length() > 0) {
                finalMergeHashMap.put("IS_EUID_MERGED",sourceDestnEuids[0]);
                finalMergeHashMap.put("MERGED_EUID",mergedEuid);
                return finalMergeHashMap;
             }
            //String destnEuidValue = (String) previewDuplicatesMap.get("destnEuid");
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(sourceDestnEuids[0]);
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();
     
            //modifying as part of fixing 202
                    
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                finalMergeHashMap.put(SearchDuplicatesHandler.CONCURRENT_MOD_ERROR,bundle.getString("concurrent_mod_text"));
                finalMergeHashMap.put("DESTN_EUID",destinationEO.getEUID());
                return finalMergeHashMap;
            }
            
            ArrayList srcsList  = new ArrayList();
            
            for (int i = 0; i < sourceDestnEuids.length; i++) {
                if(i !=0 ) {
                    srcsList.add(sourceDestnEuids[i]);
                }
            }    
            Object[] sourceEUIDObjs =  srcsList.toArray();            
            String[] sourceEUIDs  = new String[srcsList.size()];            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];            
            
            //Check if the EUID is merged - Fix for #202
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                String mergedEuidDuplicate = midmUtilityManager.getMergedEuid(sourceEuid);// modified as fix of 202
                if (mergedEuidDuplicate != null && mergedEuidDuplicate.length() > 0) {
                    finalMergeHashMap.put("IS_EUID_MERGED", sourceEuid);
                    finalMergeHashMap.put("MERGED_EUID", mergedEuidDuplicate);
                    return finalMergeHashMap;
                }

            }

            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
                // modifying as part of fixing 202 on 12-11-08                 
                sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+sourceEuid);
                if(new Integer(srcRevisionNumbers[i]).intValue() != sessionRevisionNumber.intValue() ) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+destinationEO.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                    finalMergeHashMap.put(SearchDuplicatesHandler.CONCURRENT_MOD_ERROR,bundle.getString("concurrent_mod_text"));
                    finalMergeHashMap.put("DESTN_EUID",sourceEuid);
                    return finalMergeHashMap;
                }                
            }
            
            //perform multi merge here
            resultingEO =  masterControllerService.mergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);              
           
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SDP054: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SDP055: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SDP056: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SDP057: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SDP058: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
       }

      //Insert Audit logs 
       try {
       //String userName, String euid1, String euid2, String function, int screeneID, String detail
        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               sourceDestnEuids[0], 
                                               "",
                                               "EUID Multi Merge Confirm",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View two selected EUIDs of the merge confirm page");
        } catch (UserException ex) {   
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("SDP059: Failed to insert AuditLogs : {0} ", ex.getMessage()),ex);
           return null;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
           mLogger.error(mLocalizer.x("SDP060: Failed to insert AuditLogs : {0} ", ex.getMessage()),ex);
           return null;
        }
        if(resultingEO==null)
            return null;
        else 
            return finalMergeHashMap;
    }        

   /** 
     * Addded on 22/08/2008 <br>
     * 
     * This method is used to build String of duplicate euids delimited by commas (,) <br>
     * 
     * 
     * @param finalArrayList 
     * @return String  - String of duplicate euids <br>
     * 
     **/

    public String buildDuplicateEuids(ArrayList finalArrayList) {
        String finalEuidsString = new String();
        StringBuffer arlInnerEuids = new StringBuffer();

        try {
            for (int fac = 0; fac < finalArrayList.size(); fac++) {
                ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
                //accumilate the duplicate euids here
                for (int j = 0; j < arlInner.size(); j++) {
                    HashMap eoHashMapValues = (HashMap) arlInner.get(j);
                    arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
                }
            }

        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SDP061: Encountered an Exception : {0} ", ex.getMessage()), ex);
        }
        finalEuidsString = arlInnerEuids.toString();
        finalEuidsString = finalEuidsString.substring(0, finalEuidsString.length() - 1);
        return finalEuidsString;
    }

}


