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
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG"); 
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
                mLogger.error(mLocalizer.x("DHB001: Encountered the ValidationException : {0}", ex.getMessage()),ex);
                } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("DHB002: Encountered the UserException: {0}", ex.getMessage()),ex);
                } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("DHB003: Encountered the ProcessingException {0}", ex.getMessage()),ex);
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof PageException)) {
                mLogger.error(mLocalizer.x("DHB004: Encountered the PageException: {0}", ex.getMessage()),ex);
                } else if (!(ex instanceof RemoteException)) {
                mLogger.error(mLocalizer.x("DHB005: Encountered the RemoteException: {0}", ex.getMessage()),ex);
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
            } else {
                mLogger.error(mLocalizer.x("DHB006: {0}", ex.getMessage()),ex);

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
                      mLogger.error(mLocalizer.x("DHB007: Encountered ValidationException: {0}", ex.getMessage()),ex);
                } else if (ex instanceof UserException) {
                      mLogger.error(mLocalizer.x("DHB008: Encountered UserException: {0}", ex.getMessage()),ex);
                } else if (!(ex instanceof ProcessingException)) {
                     mLogger.error(mLocalizer.x("DHB009: Encountered the ProcessingException: {0}", ex.getMessage()),ex);
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof PageException)) {
                    mLogger.error(mLocalizer.x("DHB010: Encountered the PageException: {0}", ex.getMessage()),ex);
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                } else if (!(ex instanceof RemoteException)) {
                     mLogger.error(mLocalizer.x("DHB011: Encountered the RemoteException: {0}", ex.getMessage()),ex);
                    //log(QwsUtil.getRootCause(ex).getMessage(), QwsUtil.getRootCause(ex));
                }else
                { //mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
                  mLogger.error(mLocalizer.x("DHB012: Encountered the exception:{0}", ex.getMessage()),ex);  
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB013: Encountered the ProcessingException:{0}", ex.getMessage()));  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB014: Encountered the UserException:{0}", ex.getMessage()));  
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("DHB015: Encountered the ProcessingException:{0}", ex.getMessage()));  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("DHB016: Encountered the UserException:{0}", ex.getMessage()));  
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB017: Encountered the ProcessingException:{0}", ex.getMessage()));  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB018: Encountered the UserException:{0}", ex.getMessage()));  
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("DHB019: Encountered the UserException:{0}", ex.getMessage()),ex);  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB020: Encountered the UserException:{0}", ex.getMessage()),ex);  
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
                    String msg1 = bundle.getString("EUID1");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1 + errorMessage, errorMessage));
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
                    String msg2 = bundle.getString("EUID2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg2 + errorMessage, errorMessage));
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
                     String msg3 = bundle.getString("EUID3");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg3 + errorMessage, errorMessage));
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
                    String msg4 = bundle.getString("EUID4");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg4+ errorMessage, errorMessage));
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("DHB021: Encountered the ProcessingException:{0}", ex.getMessage()),ex);  
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("DHB022: Encountered the UserException:{0}", ex.getMessage()),ex);  
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
