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
 * Author : Pratibha, Sridhar Narsingh RajaniKanth
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.presentation.valueobjects.AssumeMatchesRecords;
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
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;



import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public class AssumeMatchHandler {
    
    
    private static final String ASSUMEMATCHRECORD="assumeRecord";
    private static final String VALIDATIION_ERROR ="validationError";
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler");
    private AssumeMatchesRecords[] assumeMatchesRecordsVO = null;
    private ArrayList voList = new ArrayList();
    private MasterControllerService masterControllerService = new MasterControllerService();
    
/** Dynamic  Values***/    
    // Create fields for non updateable fields as per screen config array
    private String EUID=null;
    private String SystemCode=null;
    private String LID=null;
    private String create_start_date=null;
    private String create_end_date=null;
    private String create_start_time=null;
    private String create_end_time=null;
    private String Status=null;
    int totalFields = 0;
    int countMenuFields = 0;
    int countEmptyFields = 0;
    private int searchSize = -1;
    private int counter = 0;
    
    //Arraylist to display EDM driven search criteria.
    private ArrayList searchScreenConfigArray;
    
    //Arraylist to display EDM driven search results.
    private FieldConfig[] searchResultsConfigArray;
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    // Create fields for updateable fields as per screen config array like firstname,lastname...etc (Person, Address, Auxiliary, Comment and Alias)
     private HashMap updateableFeildsMap =  new HashMap();    
    
    
    Object objFirstName=null;
    Object objLastName=null;
    
    /** Creates a new instance of AssumeMatchHandler */
    public AssumeMatchHandler() {
    }
    

    public String getCreate_start_date() {
        return create_start_date;
    }


    public String getCreate_end_date() {
        return create_end_date;
    }
    
    public String performAssumeSearch() {
            session.removeAttribute("enterpriseArrayList");
            session.removeAttribute("previewAMEO"); 
        try {
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            // arlScreenConfig ArrayList to be used for UI to fetch UI components from EDM.xml
            ArrayList arlScreenConfig = objScreenObject.getSearchScreensConfig();
            
            // arlResultsConfig ArrayList to be used by Service Layer to fetch serach results from EDM.xml
            ArrayList arlResultsConfig = objScreenObject.getSearchResultsConfig();

            AssumedMatchSearchObject tso = getAMSearchObject();

            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(tso);
           /* Debug loop
            AssumedMatchIterator amIter1 = masterControllerService.lookupAssumedMatches(tso);          
                while (amIter1.hasNext()) {
                    AssumedMatchSummary assumedMatchSummary = (AssumedMatchSummary) amIter1.next();
                    //System.out.println("ID....." +  assumedMatchSummary.getId() + "Before EUID ..." + assumedMatchSummary.getBeforeEO().getEUID()+ " After ..." + assumedMatchSummary.getAfterEO().getEUID());
                }            
            */
            mLogger.error(">>> amIter.count " + amIter.count());
            ////System.out.println(">>> amIter.count " + amIter.count());
            ArrayList arlOuter = new ArrayList();
            ArrayList arlInner = null;
            ArrayList eoHashMapList = new ArrayList();
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
                    ////System.out.println("PREV EUID:: " + prevEuid + " THIS EUID ::" + amSummary.getEUID());
                    startPosition++;
                    EnterpriseObject beforeEO = amSummary.getBeforeEO();
                    eoArrayList.add(beforeEO);
                    amHashMap.put(beforeEO.getEUID(), amSummary.getId()); // set the assumed match id in the hashmap                        
                    amHashMap.put("SystemCode", amSummary.getSystemCode()); // set the System code in the hashmap
                    ////System.out.println(">>>>>>>>>>>>> "+amSummary.getId());      
                    EnterpriseObject afterEO = amSummary.getAfterEO();
                    eoArrayList.add(afterEO);
                    summaryHash.put("summary", amSummary);
                    summaryHash.put("before", beforeEO);
                    summaryHash.put("after", afterEO);
                    summaryList.add(summaryHash);
                    ////System.out.println(summaryList.size());
                    if ((index != 0 && !prevEuid.equalsIgnoreCase(amSummary.getEUID())) || index +1 == amIter.count() ) {  //Boundary value condition 
                        ////System.out.println("populating VO.////////////////" + summaryList.size());
                        //populate VO                            
                        populateVO(summaryList, index);
                        summaryHash.clear();
                        summaryList.clear();
                        counter++;
                    }
                    prevEuid = amSummary.getEUID();
                    index++;
                    
                    //session.setAttribute("enterpriseArrayList", eoArrayList); //set the array of EO's (Before and After ) in the session
                    session.setAttribute("amId", amHashMap);//set am id in the session.
                }// end of while
                setSearchSize(counter);
            }          
        } catch (Exception ex) {
            // UserException and ValidationException don't need a stack trace.
                // ProcessingException stack trace logged by MC
            if (ex instanceof ValidationException) {
                mLogger.error("Validation failed. Message displayed to the user: " + QwsUtil.getRootCause(ex).getMessage());
                return VALIDATIION_ERROR;
            } else if (ex instanceof UserException) {
                mLogger.error("UserException. Message displayed to the user: " + QwsUtil.getRootCause(ex).getMessage());
                return("ProcessingException");
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                mLogger.error("ProcessingException ex : " + ex.toString());
                ex.printStackTrace();
                return("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else if (!(ex instanceof PageException)) {
                mLogger.error("PageException : " + QwsUtil.getRootCause(ex).getMessage());
                return("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else if (!(ex instanceof RemoteException)) {
                mLogger.error("RemoteException : " + QwsUtil.getRootCause(ex).getMessage());
                return("ProcessingException");
            //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else {
                mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                return("ProcessingException");
            }
        }        
        return ASSUMEMATCHRECORD;
    }

    /**
     * @exception ValidationException when entry is not valid.
     * @todo Document: Getter for PDSearchObject attribute of the SearchForm
     *      object
     * @return  the PD search object
     */
    public AssumedMatchSearchObject getAMSearchObject() throws ValidationException, Exception {
/*            //System.out.println("EUID" + getEUID());
            //System.out.println("LID" + getLID());
            //System.out.println("start_date" + getCreate_start_date());
            //System.out.println("start_time" + getCreate_start_time());
            //System.out.println("end_date" + getCreate_end_date());
            //System.out.println("Create_end_time" + getCreate_end_time());
            //System.out.println("SystemCode" + getSystemCode());
            //System.out.println("Status" + getStatus());
*/

            String errorMessage = null;
            EDMValidation edmValidation = new EDMValidation();
            ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            AssumedMatchSearchObject amso = new AssumedMatchSearchObject();
            // get the array list as per the search
            ArrayList fieldConfigArrayList = getSearchScreenConfigArray();
            Iterator fieldConfigArrayIter = fieldConfigArrayList.iterator();
            totalFields = fieldConfigArrayList.size();
            while (fieldConfigArrayIter.hasNext()) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
                String feildValue = (String) getUpdateableFeildsMap().get(fieldConfig.getName());

                //set the system code and status values here
                if ("SystemCode".equalsIgnoreCase(fieldConfig.getName()) && feildValue != null) {
                    setSystemCode(feildValue);
                }
                if ("Status".equalsIgnoreCase(fieldConfig.getName()) && feildValue != null) {
                    setStatus(feildValue);
                }
                if ("MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue != null) {
                    countMenuFields++;
                } else {
                    countEmptyFields++;
                }
            }
            // One of Many validation
            if ((totalFields > 0 && countEmptyFields + countMenuFields == totalFields) && 
                (getCreate_start_date() != null && getCreate_start_date().trim().length() == 0) && 
                (getCreate_end_date() != null && getCreate_end_date().trim().length() == 0) && 
                (getCreate_start_time() != null && getCreate_start_time().trim().length() == 0) && 
                (getEUID() != null && getEUID().trim().length() == 0) && 
                (getLID() != null && getLID().trim().length() == 0) && 
                (getSystemCode() == null) &&
                (getStatus() == null) &&
                (getCreate_end_time() != null && getCreate_end_time().trim().length() == 0)) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                // Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                mLogger.info("Validation failed. Message displayed to the user: " + null);
            }

            //Form Validation of  Start Time
            if (getCreate_start_time() != null && getCreate_start_time().trim().length() > 0) {
                String message = edmValidation.validateTime(getCreate_start_time());
                if (!"success".equalsIgnoreCase(message)) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? errorMessage : message);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time From:: " + errorMessage, errorMessage));
                }
            }

            //Form Validation of  Start Date
            if (getCreate_start_date() != null && getCreate_start_date().trim().length() > 0) {
                String message = edmValidation.validateDate(getCreate_start_date());
                if (!"success".equalsIgnoreCase(message)) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    //If Time is supplied append it to the date and check if it parses as a valid date
                    try {
                        String searchStartDate = getCreate_start_date() + (getCreate_start_time() != null ? " " + getCreate_start_time() : " 00:00:00");
                        Date date = DateUtil.string2Date(searchStartDate);
                        if (date != null) {
                            amso.setCreateStartDate(new Timestamp(date.getTime()));
                        }
                    } catch (ValidationException validationException) {
                        errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    }
                }
            }


            //Form Validation of End Time
            if (getCreate_end_time() != null && getCreate_end_time().trim().length() > 0) {
                String message = edmValidation.validateTime(getCreate_end_time());
                if (!"success".equalsIgnoreCase(message)) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create Time To:: " + errorMessage, errorMessage));
                }
            }

            //Form Validation of  End Date
            if (getCreate_end_date() != null && getCreate_end_date().trim().length() > 0) {
                String message = edmValidation.validateDate(getCreate_end_date());
                if (!"success".equalsIgnoreCase(message)) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                } else {
                    try {
                        String searchEndDate = getCreate_end_date() + (getCreate_end_time() != null ? " " + getCreate_end_time() : " 23:59:59");
                        Date date = DateUtil.string2Date(searchEndDate);
                        if (date != null) {
                            amso.setCreateEndDate(new Timestamp(date.getTime()));
                        }
                    } catch (ValidationException validationException) {
                        errorMessage = (errorMessage != null && errorMessage.length() > 0 ? bundle.getString("ERROR_end_date") : bundle.getString("ERROR_end_date"));
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    }
                }
            }

            if (((getCreate_start_date() != null) && (getCreate_start_date().trim().length() > 0)) && ((getCreate_end_date() != null) && (getCreate_end_date().trim().length() > 0))) {
                Date fromdate = DateUtil.string2Date(getCreate_start_date() + (getCreate_start_time() != null ? " " + getCreate_start_time() : " 00:00:00"));
                Date todate = DateUtil.string2Date(getCreate_end_date() + (getCreate_end_time() != null ? " " + getCreate_end_time() : " 23:59:59"));
                long startDate = fromdate.getTime();
                long endDate = todate.getTime();
                if (endDate < startDate) {
                    errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "fromdate :: " + errorMessage));
                }
            }
            
            if (getEUID() != null && getEUID().length() > 0) {
                amso.setEUID(getEUID());
                String message = edmValidation.validateNumber(getEUID());
                ////System.out.println("Assume Match Handler EUID is " + getEUID());
                if (!"success".equalsIgnoreCase(message)) {
                    errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
                }
            } 
            //set the system code in amso
            if (getSystemCode() != null) {
                amso.setSystemCode(getSystemCode());
            }
            //set the page size and total number of records from edm.xml
            ////System.out.println("Max records ====>  :" + screenObject.getSearchResultsConfig(screenObject.getID().intValue()).getMaxRecords());
            ////System.out.println("Page size ====>  :" + screenObject.getSearchResultsConfig(screenObject.getID().intValue()).getPageSize());

            amso.setPageSize(10);
            amso.setMaxElements(100);
            if (errorMessage != null && errorMessage.length() != 0) {
                throw new ValidationException(errorMessage);
            } else {
                return amso;
            }
    }
    
   private void populateVO (ArrayList amList,int offset) throws ObjectException, EPathException  {
       //for (int i=0; i < assumeMatchesRecordsVO.length;i++) {
       ////System.out.println("In the populate VO " + "Displacement-> "+  displacement + "offset ->" + offset + "Index ->" + i + "length -> " + assumeMatchesRecordsVO.length);
           HashMap hashMap = (HashMap) amList.get(0); //Values always are in 0th index
           AssumedMatchSummary ams = (AssumedMatchSummary)hashMap.get("summary");
           EnterpriseObject before = (EnterpriseObject) hashMap.get("before");
           EnterpriseObject after = (EnterpriseObject) hashMap.get("after");
           EnterpriseObject eo = before;
           
           assumeMatchesRecordsVO[offset] = new AssumeMatchesRecords(); //malloc
           assumeMatchesRecordsVO[offset].setAssumedMatchId(ams.getId());

           assumeMatchesRecordsVO[offset].setEuid(eo.getEUID());
           assumeMatchesRecordsVO[offset].setLocalId(ams.getLID());
           assumeMatchesRecordsVO[offset].setSystemCode(ams.getSystemCode());
           assumeMatchesRecordsVO[offset].setWeight(new Float(ams.getWeight()).toString());
           
           for (int j = 0; j < amList.size(); j++) { //Each Summary has Before and After
               ////System.out.println("Xxssssssssssssssssssssss" + j);
               if ( j == 0)  {
                   ams = (AssumedMatchSummary)hashMap.get("summary");
                   eo = (EnterpriseObject) hashMap.get("before");
                   objFirstName = EPathAPI.getFieldValue("Person.FirstName", eo.getSBR().getObject());
                   ////System.out.println("First Name::" + (String) objFirstName);
                   ////System.out.println("In the populate VO " + "offset ->" + offset + "length -> " + assumeMatchesRecordsVO.length);
                   //Set the First Name Values in VO
                   assumeMatchesRecordsVO[offset].getFirstName().add(objFirstName);
                   objLastName = EPathAPI.getFieldValue("Person.LastName", eo.getSBR().getObject());
                   ////System.out.println("Last Name::" + (String) objLastName);
                   //Set the Last Name Values in VO
                   assumeMatchesRecordsVO[offset].getLastName().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.SSN", eo.getSBR().getObject());
                   //Set the Last Name Values in VO       
                   assumeMatchesRecordsVO[offset].getSsn().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.DOB", eo.getSBR().getObject());
                   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                   String dob = simpleDateFormatFields.format(objLastName);
                   //Set the DOB Values in VO
                   assumeMatchesRecordsVO[offset].getDob().add(dob);
                   objLastName = EPathAPI.getFieldValue("Person.Address.AddressLine1", eo.getSBR().getObject());
                   //Set the Address Line1 Values in VO
                   assumeMatchesRecordsVO[offset].getAddressLine1().add(objLastName);
                   assumeMatchesRecordsVO[offset].setWeight(Float.toString(ams.getWeight()));
                    
                   eo = (EnterpriseObject) hashMap.get("after");
                   objFirstName = EPathAPI.getFieldValue("Person.FirstName", eo.getSBR().getObject());
                   ////System.out.println("First Name::" + (String) objFirstName);
                   ////System.out.println("In the populate VO " + "offset ->" + offset + "length -> " + assumeMatchesRecordsVO.length);
                   //Set the First Name Values in VO
                   assumeMatchesRecordsVO[offset].getFirstName().add(objFirstName);
                   objLastName = EPathAPI.getFieldValue("Person.LastName", eo.getSBR().getObject());
                   ////System.out.println("Last Name::" + (String) objLastName);
                   //Set the Last Name Values in VO
                   assumeMatchesRecordsVO[offset].getLastName().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.SSN", eo.getSBR().getObject());
                   //Set the Last Name Values in VO       
                   assumeMatchesRecordsVO[offset].getSsn().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.DOB", eo.getSBR().getObject());
                   simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                   dob = simpleDateFormatFields.format(objLastName);
                   //Set the DOB Values in VO
                   assumeMatchesRecordsVO[offset].getDob().add(dob);
                   objLastName = EPathAPI.getFieldValue("Person.Address.AddressLine1", eo.getSBR().getObject());
                   //Set the Address Line1 Values in VO
                   assumeMatchesRecordsVO[offset].getAddressLine1().add(objLastName);
                   assumeMatchesRecordsVO[offset].setWeight(Float.toString(ams.getWeight()));
                    
               } else {
                   ams = (AssumedMatchSummary)hashMap.get("summary");
                   eo = (EnterpriseObject) hashMap.get("after");
                   objFirstName = EPathAPI.getFieldValue("Person.FirstName", eo.getSBR().getObject());
                   ////System.out.println("First Name::" + (String) objFirstName);
                   ////System.out.println("In the populate VO " + "offset ->" + offset + "length -> " + assumeMatchesRecordsVO.length);
                   //Set the First Name Values in VO
                   assumeMatchesRecordsVO[offset].getFirstName().add(objFirstName);
                   objLastName = EPathAPI.getFieldValue("Person.LastName", eo.getSBR().getObject());
                   ////System.out.println("Last Name::" + (String) objLastName);
                   //Set the Last Name Values in VO
                   assumeMatchesRecordsVO[offset].getLastName().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.SSN", eo.getSBR().getObject());
                   //Set the Last Name Values in VO       
                   assumeMatchesRecordsVO[offset].getSsn().add(objLastName);
                   objLastName = EPathAPI.getFieldValue("Person.DOB", eo.getSBR().getObject());
                   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                   String dob = simpleDateFormatFields.format(objLastName);
                   //Set the DOB Values in VO
                   assumeMatchesRecordsVO[offset].getDob().add(dob);
                   objLastName = EPathAPI.getFieldValue("Person.Address.AddressLine1", eo.getSBR().getObject());
                   //Set the Address Line1 Values in VO
                   assumeMatchesRecordsVO[offset].getAddressLine1().add(objLastName);
                   assumeMatchesRecordsVO[offset].setWeight(Float.toString(ams.getWeight()));
               }
           }
       //}       
   }
   
   

    public AssumeMatchesRecords[] getAssumeMatchesRecordsVO() {
       return assumeMatchesRecordsVO;
    }

    public void setAssumeMatchesRecordsVO(AssumeMatchesRecords[] assumeMatchesRecordsVO) {
        this.assumeMatchesRecordsVO = assumeMatchesRecordsVO;
    }

    public String getEUID() {
        return EUID;
    }

    public void setEUID(String EUID) {
        this.EUID = EUID;
    }

    public String getSystemCode() {
        return SystemCode;
    }

    public void setSystemCode(String SystemCode) {
        this.SystemCode = SystemCode;
    }

    public String getLID() {
        return LID;
    }

    public void setLID(String LID) {
        this.LID = LID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    public ArrayList getSearchScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        try {
            ArrayList screenConfigArray = screenObject.getSearchScreensConfig();               
            Iterator iteratorScreenConfig = screenConfigArray.iterator();

            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                if ("Basic Search".equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        //Build array of field configs from 
                        searchScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                    }
                }
            }            
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Config Array Object: ", e);
        }                
        return searchScreenConfigArray;
    }

    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    public FieldConfig[] getSearchResultsConfigArray() {
        
        ArrayList basicSearchFieldConfigs;
        ArrayList newArrayList = new ArrayList();
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
                            FieldConfig object = (FieldConfig)fcObj[i];
                            newFcArray[i] = object;
                        }
                    }
            }            
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Config Array Object: ", e);
        }                

        searchResultsConfigArray = newFcArray;
        return searchResultsConfigArray;
    }

    public void setSearchResultsConfigArray(FieldConfig[] searchResultsConfigArray) {
        this.searchResultsConfigArray = searchResultsConfigArray;
    }


    public void setCreate_start_date(String create_start_date) {
        this.create_start_date = create_start_date;
    }

    public void setCreate_end_date(String create_end_date) {
        this.create_end_date = create_end_date;
    }

    public void setCreate_start_time(String create_start_time) {
        this.create_start_time = create_start_time;
    }

    public void setCreate_end_time(String create_end_time) {
        this.create_end_time = create_end_time;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }    
             
    /** 
    * This method provides functionality to preview/simulate the UNDo on an existing Assumed Match
    * @return EnterpriseObject
    * @param assumedMatchId Id of assumed match to be resolved
    * @exception ProcessingException An error has occured.
    * @exception UserException Invalid id
    */
    public void previewUndoAssumedMatch(ActionEvent event) throws ProcessingException, UserException {
        String assumedMatchId = (String) event.getComponent().getAttributes().get("amIdValueExpression");
        EnterpriseObject newEO = masterControllerService.previewUndoAssumedMatch(assumedMatchId); 
        //mLogger.info(">>>>> new EO  "+newEO);
        session.setAttribute("previewAMEO", newEO); 
        session.setAttribute("previewAMID", assumedMatchId); 
    }
    
    /**
     * 
     * @param event
     */
    public void undoMatch(ActionEvent event) {
        try {
            String assumedMatchId = (String) event.getComponent().getAttributes().get("previewamIdValueExpression");
            ////System.out.println("==: " + assumedMatchId);
            String amEuid = masterControllerService.undoAssumedMatch(assumedMatchId);

            EnterpriseObject amPreviewEnterpriseObject = masterControllerService.getEnterpriseObject(amEuid);
            
            session.removeAttribute("previewAMEO"); 
            session.removeAttribute("amEoList"); 
     
            if(amPreviewEnterpriseObject != null)System.out.println("==: " + amPreviewEnterpriseObject.getEUID());

            ArrayList newArrayList =  new ArrayList();
            newArrayList.add(amPreviewEnterpriseObject);
            
            httpRequest.setAttribute("undoEnterpriseArrayList", newArrayList); 
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
            

    public ArrayList getAssumedEOList(String assumedMatchId) {
        ArrayList eoArrayList = new ArrayList();
        try {
            //System.out.println("AM SUMMARY ++: : assumedMatchId" + assumedMatchId);
            AssumedMatchSearchObject aso = new AssumedMatchSearchObject();
            aso.setPageSize(10);
            aso.setMaxElements(100);
            aso.setAssumedMatchId(assumedMatchId);
            //System.out.println("AM SUMMARY ++: : aso" + aso);
            
            // Lookup Assumed Matches
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(aso);
            if (amIter != null & amIter.count() > 0) {
                amIter.sortBy("EUID", false);
                while (amIter.hasNext()) {
                    AssumedMatchSummary amSummary = (AssumedMatchSummary) amIter.next();
                    //System.out.println("AM SUMMARY ++: : " + amSummary.getAfterEO());
                    //System.out.println("AM SUMMARY ++: : " + amSummary.getBeforeEO());
                    if(amSummary.getBeforeEO() != null )eoArrayList.add(amSummary.getBeforeEO());
                    if(amSummary.getAfterEO() != null )eoArrayList.add(amSummary.getAfterEO());
                }
            }
        } catch (PageException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(AssumeMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("amEoList" , eoArrayList); 
        return eoArrayList;
    }
  
    public String getCreate_start_time() {
        return create_start_time;
    }

    public String getCreate_end_time() {
        return create_end_time;
    }

}
