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
 * DashboardHandler.java 
 * Created on January 19, 2008
 * Author : Pratibha
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.objects.EnterpriseObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;

public class DashboardHandler  {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.DashboardHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG"); 
    private String singleEuid ;
    private String euid1 ;
    private String euid2 ;
    private String euid3 ;
    private String euid4 ;
    
    //instantiate the MasterControllerService
    private MasterControllerService masterControllerService = new MasterControllerService();
    
    //instantiate the compare duplicate manager
    private MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
    
    //get the screen object from session
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    NavigationHandler navigationHandler = new NavigationHandler();
    
    /** Creates a new instance of SearchDuplicatesHandler */
    public DashboardHandler() {
         session.setAttribute("ScreenObject", navigationHandler.getScreenObject("dashboard"));
    }

    private int countPotentialDuplicates = 0;
    private int countAssumedMatches = 0;
    
    public int getCountPotentialDuplicates(){
        try {
            PotentialDuplicateSearchObject potentialDuplicateSearchObject = new PotentialDuplicateSearchObject();

            Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
            Long currentTime = new java.util.Date().getTime();
            potentialDuplicateSearchObject.setCreateEndDate(tsCurrentTime);

            long milliSecsInADay = 86400000L;
        
 
            Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
            potentialDuplicateSearchObject.setCreateStartDate(ts24HrsBack);

            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            //Fix for bug 6709103 starts 
            int count = pdPageIterArray.count();

            String[][] temp = new String[count][2];

            if (pdPageIterArray != null & pdPageIterArray.count() > 0) {
                // add all the potential duplicates to the summary array  
                while (pdPageIterArray.hasNext()) {
                    PotentialDuplicateSummary pds[] = pdPageIterArray.first(pdPageIterArray.count());
                    for (int i = 0; i < pds.length; i++) {
                        temp[i][0] = pds[i].getEUID1();
                        temp[i][1] = pds[i].getEUID2();

                    }
                }
            }

            ArrayList arl = new ArrayList();

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 2; j++) {
                    boolean addData = true;
                    String data;

                    for (int ii = 0; ii < arl.size(); ii++) {
                        data = (String) arl.get(ii);
                        if (data.equalsIgnoreCase(temp[i][j])) {
                            addData = false;
                            break;
                        }
                    }
                    if (addData == true) {
                        arl.add(temp[i][j]);
                    }
                }
            }
            //Code to create ArrayList
            ArrayList arlOuter = new ArrayList();
            for (int x = 0; x < arl.size(); x++) {
                String id = (String) arl.get(x);
                ArrayList arlInner = new ArrayList();
                arlInner.add(id);
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < 2; j++) {
                        String strData = temp[i][j];
                        if (id.equalsIgnoreCase(strData)) {
                            if (j == 0) {
                                if (!arlInner.contains(temp[i][1])) {
                                    arlInner.add(temp[i][1]);
                                }
                            } else if (j == 1) {
                                //if(!arlInner.contains(strData))
                                //{arlInner.add(strData);
                                //}
                                if (!arlInner.contains(temp[i][0])) {
                                    arlInner.add(temp[i][0]);
                                }
                            }
                        }
                    }
                }
                arlOuter.add(arlInner);
            }
            ArrayList finalArrayList = arlOuter;

            finalArrayList = arlOuter;
            //Fix for bug 6709103 ends 
            countPotentialDuplicates = finalArrayList.size();

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("DBH001: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DBH002: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DBH003: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("DBH004: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                    return 0;
                } else {
                    mLogger.error(mLocalizer.x("DBH005: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                    return 0;
                }
            }
            
      } 
       return countPotentialDuplicates;
    }
    
     public int getCountAssumedMatches(){
        try {

            AssumedMatchSearchObject assumedMatchSearchObject = new AssumedMatchSearchObject();
            
            Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
            
            assumedMatchSearchObject.setCreateEndDate(tsCurrentTime);
            
            Long currentTime = new java.util.Date().getTime();
            long milliSecsInADay = 86400000L;

            Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
            
            assumedMatchSearchObject.setCreateStartDate(ts24HrsBack);
            AssumedMatchIterator amIter = masterControllerService.lookupAssumedMatches(assumedMatchSearchObject);            

            countAssumedMatches = amIter.count();
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("DBH006: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DBH007: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DBH008: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("DBH009: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("DBH010: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return 0;
         }
            return countAssumedMatches;
    }



    public void setCountPotentialDuplicates(int countPotentialDuplicates) {
        this.countPotentialDuplicates = countPotentialDuplicates;
    }


    public void setCountAssumedMatches(int countAssumedMatches) {
        this.countAssumedMatches = countAssumedMatches;
    }

    
     /** 
     * 
     * Modified on 11/06/2008
     * 
     * This method is used to search the single EUID. This method will also informs the user about the merged EUIDS.
     *
     * @return String  
     *    "EUID Details"  if EUID is found. 
     *    "dashboard"  if EUID is not found or any exception occurs in retrieving the EUID.
     *    
     * 
     * The return string used for navigation string which will be mapped in the faces-config.xml file.
     * 
     */
    public String singleEuidSearch() {
      String  returnString  = new String();
            
        try {
            
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getSingleEuid() != null && this.getSingleEuid().trim().length() > 0 ) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getSingleEuid());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    
                    String mergeEuid  = midmUtilityManager.getMergedEuid(this.getSingleEuid());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) {
                        String errorMessage = "'" + this.getSingleEuid() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                        returnString =  "dashboard";
                     } else if(mergeEuid != null) {
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getSingleEuid() +"'"  ;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                        //Set the merged euid for the user to search in the duplicates screen
                        this.setSingleEuid(mergeEuid);

                        //Insert audit log here for merged EUID search here
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                mergeEuid,
                                this.getSingleEuid(),
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                         returnString =  "dashboard";
                        }
                    
                } else {
                    //set the screen object of the record details screen once EUID is found 
                    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getSingleEuid(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                    returnString = "EUID Details";
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("DBH011: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DBH012: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DBH013: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("DBH014: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("DBH015: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
             returnString =  "dashboard";
        }
      
        return returnString;
    }

    /** 
     * 
     * Modified on 11/06/2008
     * 
     * This method is used to search the more than one euids for comparing.
     *
     * @return String  
     *    "Compare Duplicates"  if EUIDa is found. 
     *    "dashboard"  if EUID is not found or any exception occurs in retrieving the EUID.
     *    
     * 
     * The return string used for navigation string which will be mapped in the faces-config.xml file.
     * 
     */

    public String compareEuidSearch() {
        try {
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();
            
            ArrayList errorsList  = new ArrayList();
            ArrayList requiredFieldsList  = new ArrayList();
            ArrayList warningsList  = new ArrayList();
            
             if (this.getEuid1() != null && this.getEuid1().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid1() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            if (this.getEuid1() != null && this.getEuid1().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid1());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid1() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid1() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);

                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                        newArrayList.add(eoMap);

                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid1(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                 } else {
                        String errorMessage = this.getEuid1() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }

            if (this.getEuid2() != null && this.getEuid2().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid2() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }

            if (this.getEuid2() != null && this.getEuid2().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid2());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid2() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                         String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid2() +"'"  ;
                         errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                        newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid2(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                     } else {
                        String errorMessage = this.getEuid2() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }

            if (this.getEuid3() != null && this.getEuid3().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid3() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            
            
            if (this.getEuid3() != null && this.getEuid3().trim().length() > 0 ) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid3());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid3() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid3() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                        newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid3(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                      } else {
                        String errorMessage = this.getEuid3() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }
            if (this.getEuid4() != null && this.getEuid4().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid4() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            if (this.getEuid4() != null && this.getEuid4().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid4());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid4() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                      } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid4() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                      }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                        newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid4(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                     } else {
                        String errorMessage = this.getEuid4() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }
             
            //If the error ocurrs then display all the messages together
            if(errorsList.size() > 0 || warningsList.size() > 0 ) {
                for(int i = 0 ; i< errorsList.size() ; i ++ ) {
                    String errorMessage = (String) errorsList.get(i);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                }

                for(int i = 0 ; i< warningsList.size() ; i ++ ) {
                    String warningMessage = (String) warningsList.get(i);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningMessage, warningMessage));
                }
                return "dashboard";
            }
     
            if(requiredFieldsList.size() ==  4 || newArrayList.size() ==  1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("ENTER_MORE_COMPARE_EUIDS"), bundle.getString("ENTER_MORE_COMPARE_EUIDS")));
                return "dashboard";
            }

             
            session.setAttribute("eocomparision", "yes");
            //keep array of eos hashmap in the session
            session.setAttribute("comapreEuidsArrayList", newArrayList);
            
             
            //If the no error ocurrs then navigate to the compare duplicates page
            if (errorsList.size() == 0 && warningsList.size() == 0 && newArrayList.size() >  1) {
                return "Compare Duplicates";
            }
            
        } 

        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("DBH016: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DBH017: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DBH018: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("DBH019: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("DBH020: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
        }

        return "Compare Duplicates";
    }

     /** 
     * 
     * Added By  on 11/06/2008
     * 
     * This method is used to search the more than one euids for comparing. This method is called from the ajax services.
     *
     * @return ArrayList
     *    ArrayList of compare euids  if EUIDs are found. 
     *    null if EUID is not found or any exception occurs in retrieving the EUIDs.
     *    
     * 
     * 
     * 
     */

    public ArrayList performCompareEuidSearch() {
        ArrayList newArrayList = new ArrayList();
        try {
            EnterpriseObject enterpriseObject = null;

            HashMap eoMap = new HashMap();
            
            ArrayList errorsList  = new ArrayList();
            ArrayList requiredFieldsList  = new ArrayList();
            ArrayList warningsList  = new ArrayList();
            
             if (this.getEuid1() != null && this.getEuid1().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid1() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            if (this.getEuid1() != null && this.getEuid1().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid1());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid1() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid1() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);

                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                         newArrayList.add(eoMap);

                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid1(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                 } else {
                        String errorMessage = this.getEuid1() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }

            if (this.getEuid2() != null && this.getEuid2().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid2() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }

            if (this.getEuid2() != null && this.getEuid2().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid2());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid2() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                         String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid2() +"'"  ;
                         errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                         newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid2(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                     } else {
                        String errorMessage = this.getEuid2() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }

            if (this.getEuid3() != null && this.getEuid3().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid3() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            
            
            if (this.getEuid3() != null && this.getEuid3().trim().length() > 0 ) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid3());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid3() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid3() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                        newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid3(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                      } else {
                        String errorMessage = this.getEuid3() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }
            if (this.getEuid4() != null && this.getEuid4().trim().length() == 0) {
              String errorMessage =  "'"+ this.getEuid4() + "' " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2")  ;
              requiredFieldsList.add(errorMessage);
            }
            if (this.getEuid4() != null && this.getEuid4().trim().length() > 0) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String mergeEuid = midmUtilityManager.getMergedEuid(this.getEuid4());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid4() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                      } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid4() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                      }
                } else {
                    eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    //add the EO if it is active only
                    if ("active".equalsIgnoreCase(enterpriseObject.getStatus())) {
                         newArrayList.add(eoMap);
                        //Insert audit log here for EUID search
                        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                this.getEuid4(),
                                "",
                                "EO View/Edit",
                                new Integer(screenObject.getID()).intValue(),
                                "View/Edit detail of enterprise object");
                     } else {
                        String errorMessage = this.getEuid4() + " Enterprise Object is " + enterpriseObject.getStatus();
                        warningsList.add(errorMessage); //add the errors to the array list
                     }
                }
            }
             
            //If the error ocurrs then display all the messages together
            if(errorsList.size() > 0 || warningsList.size() > 0 ) {
                for(int i = 0 ; i< errorsList.size() ; i ++ ) {
                    String errorMessage = (String) errorsList.get(i);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                }

                for(int i = 0 ; i< warningsList.size() ; i ++ ) {
                    String warningMessage = (String) warningsList.get(i);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningMessage, warningMessage));
                }
                return null;
            }
     
            if(requiredFieldsList.size() ==  4 || newArrayList.size() ==  1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("ENTER_MORE_COMPARE_EUIDS"), bundle.getString("ENTER_MORE_COMPARE_EUIDS")));
                return null;
            }

             
            session.setAttribute("eocomparision", "yes");
            //keep array of eos hashmap in the session
            //session.setAttribute("comapreEuidsArrayList", newArrayList);
            
             
            //If the no error ocurrs then navigate to the compare duplicates page
            if (errorsList.size() == 0 && warningsList.size() == 0 && newArrayList.size() >  1) {
                session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                return newArrayList;
            }
            
        } 

         catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("DBH021: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DBH022: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DBH023: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("DBH024: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("DBH025: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
        return newArrayList;
    }

    
    
    public String getEuid1() {
        return euid1;
    }

    public void setEuid1(String euid1) {
        this.euid1 = euid1;
    }

    public String getEuid2() {
        return euid2;
    }

    public void setEuid2(String euid2) {
        this.euid2 = euid2;
    }

    public String getEuid3() {
        return euid3;
    }

    public void setEuid3(String euid3) {
        this.euid3 = euid3;
    }

    public String getEuid4() {
        return euid4;
    }

    public void setEuid4(String euid4) {
        this.euid4 = euid4;
    }
    
    public boolean showSubscreenTab(String subscreenTabName) {
        boolean showTab = false;
        ScreenObject localScreenObject = navigationHandler.getScreenObject("dashboard");
        ArrayList subTabsLabelsList = localScreenObject.getSubscreensConfig();
        session.setAttribute("ScreenObject", localScreenObject);
        
        Object[] subTabsLabelsListObj = subTabsLabelsList.toArray();
        for (int i = 0; i < subTabsLabelsListObj.length; i++) {
            ScreenObject screenObj = (ScreenObject) subTabsLabelsListObj[i];
            if(screenObj.getDisplayTitle().equalsIgnoreCase(subscreenTabName)) {
                showTab = true;
            }
        }
        return showTab;
    }

    public String getSingleEuid() {
        return singleEuid;
    }

    public void setSingleEuid(String singleEuid) {
        this.singleEuid = singleEuid;
    }
}
