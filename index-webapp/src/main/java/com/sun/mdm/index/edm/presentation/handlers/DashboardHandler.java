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

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
//import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.objects.EnterpriseObject;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.faces.event.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

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
    private CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
    
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

            Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
            Long currentTime = new java.util.Date().getTime();
            long milliSecsInADay = 86400000L;

            Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
            countPotentialDuplicates = masterControllerService.countPotentialDuplicates(ts24HrsBack, tsCurrentTime);
             
         } catch (ProcessingException ex) {
                mLogger.error(mLocalizer.x("DHB003: Encountered the ProcessingException {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the ProcessingException", "Encountered the ProcessingException"));
                
        } catch (UserException ex) {
                mLogger.error(mLocalizer.x("DHB002: Encountered the UserException: {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the UserException", "Encountered the UserException"));
                
        } catch(Exception ex) {
                mLogger.error(mLocalizer.x("DHB012: Encountered the exception:{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the Exception", "Encountered the Exception"));
                
        }
        return countPotentialDuplicates;
    }
    
     public int getCountAssumedMatches(){
        try {

            Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
            Long currentTime = new java.util.Date().getTime();
            long milliSecsInADay = 86400000L;

            Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
            countAssumedMatches = masterControllerService.countAssumedMatches(ts24HrsBack, tsCurrentTime);
        } catch (ProcessingException ex) {
                mLogger.error(mLocalizer.x("DHB009: Encountered the ProcessingException: {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the ProcessingException", "Encountered the ProcessingException"));
        } catch (UserException ex) {
                mLogger.error(mLocalizer.x("DHB008: Encountered UserException: {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the UserException", "Encountered the UserException"));
        } catch(Exception ex) {
                mLogger.error(mLocalizer.x("DHB012: Encountered the exception:{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Encountered the Exception", "Encountered the Exception"));
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
     * Modified By Rajani Kanth on 11/06/2008
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
                    
                    String mergeEuid  = compareDuplicateManager.getMergedEuid(this.getSingleEuid());
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
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB013: Encountered the ProcessingException:{0}", ex.getMessage()));  
            returnString =  "dashboard";
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB014: Encountered the UserException:{0}", ex.getMessage()));  
            returnString =  "dashboard";
        }
        return returnString;
    }

    public String lookupEuid2() {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
        try {
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
  					String errorMessage = this.getEuid2()+" : ";
					errorMessage +=	bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid2(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("DHB015: Encountered the ProcessingException:{0}", ex.getMessage()));  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("DHB016: Encountered the UserException:{0}", ex.getMessage()));  
        }
        return "EUID Details";
    }

    public String lookupEuid3() {
             session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
        try {
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
					String errorMessage = this.getEuid3()+" : ";
					errorMessage +=	bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid3(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                    
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB017: Encountered the ProcessingException:{0}", ex.getMessage()));  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB018: Encountered the UserException:{0}", ex.getMessage()));  
        }
        return "EUID Details";
    }

    public String lookupEuid4() {
        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
        try {
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid4() != null && !"EUID 4".equalsIgnoreCase(this.getEuid4())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
					String errorMessage = this.getEuid4()+" : ";
					errorMessage +=	bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid4(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                    
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("DHB019: Encountered the UserException:{0}", ex.getMessage()),ex);  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB020: Encountered the UserException:{0}", ex.getMessage()),ex);  
        }
        return "EUID Details";
    }

     /** 
     * 
     * Modified By Rajani Kanth on 11/06/2008
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid1());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid1() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid1() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);

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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid2());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid2() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                         String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid2() +"'"  ;
                         errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid3());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid3() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid3() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid4());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid4() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                      } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid4() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                      }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
            
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB021: Encountered the ProcessingException:{0}", ex.getMessage()), ex);
            return "dashboard";
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB022: Encountered the UserException:{0}", ex.getMessage()), ex);
            return "dashboard";
        }

        return "Compare Duplicates";
    }

     /** 
     * 
     * Added By Rajani Kanth on 11/06/2008
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid1());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid1() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid1() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);

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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid2());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid2() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                         String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid2() +"'"  ;
                         errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid3());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid3() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                     } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid3() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                     }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
                    String mergeEuid = compareDuplicateManager.getMergedEuid(this.getEuid4());
                    if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { // If EO NOT FOUND
                        String errorMessage = "'" + this.getEuid4() + "' : " + bundle.getString("enterprise_object_not_found_error_message");
                        errorsList.add(errorMessage); //add the errors to the array list
                      } else if (mergeEuid != null) { // IF EO IS MERGED INTO ANTOHER EO
                        String errorMessage =  "'"+ mergeEuid + "' " + bundle.getString("active_euid_text") + " '"+this.getEuid4() +"'"  ;
                        errorsList.add(errorMessage); //add the errors to the array list
                      }
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
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
            session.setAttribute("comapreEuidsArrayList", newArrayList);
            
             
            //If the no error ocurrs then navigate to the compare duplicates page
            if (errorsList.size() == 0 && warningsList.size() == 0 && newArrayList.size() >  1) {
                session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
                return newArrayList;
            }
            
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB021: Encountered the ProcessingException:{0}", ex.getMessage()), ex);
            return null;
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB022: Encountered the UserException:{0}", ex.getMessage()), ex);
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
