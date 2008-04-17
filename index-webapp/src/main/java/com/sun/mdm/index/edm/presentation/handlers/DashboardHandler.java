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
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
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
import javax.faces.event.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


public class DashboardHandler  {
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.DashboardHandler");
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

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
    
    public int getCountPotentialDuplicates() throws ProcessingException, UserException, ValidationException,HandlerException{
        
        try {
                Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime()); 
                Long currentTime = new java.util.Date().getTime();
                long milliSecsInADay = 86324442L;

                Timestamp ts24HrsBack = new Timestamp(currentTime-milliSecsInADay); 
                countPotentialDuplicates = masterControllerService.countPotentialDuplicates(ts24HrsBack,tsCurrentTime);
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
        return countPotentialDuplicates;
    }
    
     public int getCountAssumedMatches() throws ProcessingException, UserException, ValidationException,HandlerException{
        
        try {
                Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime()); 
                Long currentTime = new java.util.Date().getTime();
                long milliSecsInADay = 86324442L;

                Timestamp ts24HrsBack = new Timestamp(currentTime-milliSecsInADay); 
                countAssumedMatches = masterControllerService.countAssumedMatches(ts24HrsBack,tsCurrentTime);
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
        return countAssumedMatches;
    }



    public void setCountPotentialDuplicates(int countPotentialDuplicates) {
        this.countPotentialDuplicates = countPotentialDuplicates;
    }


    public void setCountAssumedMtaches(int countAssumedMatches) {
        this.countAssumedMatches = countAssumedMatches;
    }

    public String lookupEuid1() {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
        try {
            
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid1(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("ProcessingException ex : " + ex.toString());
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("UserException ex : " + ex.toString());
        }
        return "EUID Details";
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
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("ProcessingException ex : " + ex.toString());
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("UserException ex : " + ex.toString());
        }
        return "EUID Details";
    }

    public String lookupEuid3() {
            NavigationHandler navigationHandler = new NavigationHandler();
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
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("ProcessingException ex : " + ex.toString());
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("UserException ex : " + ex.toString());
        }
        return "EUID Details";
    }

    public String lookupEuid4() {
            NavigationHandler navigationHandler = new NavigationHandler();
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
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("ProcessingException ex : " + ex.toString());
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("UserException ex : " + ex.toString());
        }
        return "EUID Details";
    }


       public String compareEuidSearch() {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
           
        try {
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 1 :" + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);

                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid1(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                }
            }
            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
//                    session.removeAttribute("comapreEuidsArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 2 :" + errorMessage, errorMessage));
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
            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 3 :" + errorMessage, errorMessage));
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
            if (this.getEuid4() != null && this.getEuid4().length() > 0 ) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 4 :" + errorMessage, errorMessage));
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
           
//            //System.out.println("===> : " + newArrayList);
            session.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("Processing  ex : " + ex.toString());
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            mLogger.error("UserException ex : " + ex.toString());
        }

        return "EUID Details";
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
}
