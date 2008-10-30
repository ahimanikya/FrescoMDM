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
 * LoginHandler.java 
 * Created on September 17, 2007, 10:15 AM
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.control.UserProfile;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.edm.services.security.Logon;
import com.sun.mdm.index.edm.control.UserProfile;
import com.sun.mdm.index.edm.services.security.SecurityManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

public class LoginHandler {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.LoginHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    private String userNameValue ="eview";
    private String userName ="eview";
    private String password = "eview";
    private static final String SUCCESS ="success";
    private static final String FAILURE ="failure";
    public static final String FAIL_INITIALIZATION = "initializationfailed";
    private static final String SUCCESS_INITIALIZATION = "initializationsuccess";
    private static final String LOGOUT ="logout";
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    /** Creates a new instance of LoginHandler */
    public LoginHandler() {
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    /** 
     * 
     * Modified on 03/06/2008
     * This method is used to sign out of the application. This method will invalidate the session as well.
     * 
     * 
     * @return String        
     */
    
    public String signOutUser() {
        request.setAttribute("Logout", "LoggedOut");
        session.invalidate();
        return LOGOUT;
    }
    /** 
     * 
     * Modified on 03/06/2008
     * 
     * This method is used to initialize the application parameteres
     *
     * @return String initializationfailed if Exception occurs      
     */

     public String  initializeApplication() {
        int euidLength=10;
        // This function is the entry point to EDM
        // check if ConfigManager is available
        try {
            ConfigManager.init();
        } catch (Exception t) {
            request.setAttribute("Error while initializing ConfigManager", QwsUtil.getRootCause(t).getMessage());
            mLogger.error(mLocalizer.x("LGH001: Error while initializing ConfigManager {0}",t.getMessage()));
            request.setAttribute(FAIL_INITIALIZATION, "Error while initializing ConfigManager : " + t.getMessage());
            return FAIL_INITIALIZATION;
        }

        // check if MasterController is available
        // To get to MasterController, ConfigManager is required.
        if (QwsController.getMasterController() == null || QwsController.getValidationService() == null) {
             try {
                // this piece of info in ConfigManager comes from MC
                ConfigManager.getInstance().setEuidLength(euidLength);
            } catch (Exception e) {
                 mLogger.error(mLocalizer.x("LGH002: Error while initializing QwsController : {0}",e.getMessage()),e);
                 request.setAttribute(FAIL_INITIALIZATION, "Error while initializing QwsController : " + QwsUtil.getRootCause(e).getMessage());
                return FAIL_INITIALIZATION;
            }
 
             // initialize the date format in DateUtil for EDM to convert date/string back and forth
            try {
                DateUtil.init();
            } catch (Exception t) {
                mLogger.error(mLocalizer.x("LGH003: Error while initializing DateUtil : {0}",t.getMessage()),t);
               request.setAttribute(FAIL_INITIALIZATION, "Error while initializing DateUtil : " + QwsUtil.getRootCause(t).getMessage());
               return FAIL_INITIALIZATION;
            }
        } else {
            // Restart the ValidationService and DateUtil in case there were problems
            // with logging in the EDM or if the database connection was lost.  Otherwise,
            // the EDM will not display values for pull-down menus.
            try {
                ValidationService.init();
            } catch (Exception t) {
                mLogger.error(mLocalizer.x("LGH004: Error while initializing ValidationService : {0}",t.getMessage()),t);
                request.setAttribute(FAIL_INITIALIZATION, "Error while initializing ValidationService : " + QwsUtil.getRootCause(t).getMessage());
               return FAIL_INITIALIZATION;
            }
            
            // initialize the date format in DateUtil for EDM to convert date/string back and forth
            try {
                DateUtil.init();
            } catch (Exception t) {
                mLogger.error(mLocalizer.x("LGH005: Error while initializing ValidationService : {0}",t.getMessage()),t);
                request.setAttribute(FAIL_INITIALIZATION, "Error while initializing ValidationService : " + QwsUtil.getRootCause(t).getMessage());
                return FAIL_INITIALIZATION;
            }
            
        }
        //authorizeAndLoginUser();
        
        ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String errorMessage =bundle.getString("Login_Success");
        //Logon logon  = new Logon();
        //Use LogOn class here
        try {
            Logon.initializeConfigurationSecurity();            
        } catch(Exception ex) {
            
           // ex.printStackTrace();
            errorMessage = bundle.getString("login_user_login_init_load_message");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
            mLogger.error(mLocalizer.x("LGH006: {0}:{1}",errorMessage,ex.getMessage()),ex);
            request.setAttribute(FAIL_INITIALIZATION, errorMessage + " : " + QwsUtil.getRootCause(ex).getMessage());
            return FAIL_INITIALIZATION;
        }
        UserProfile userProfile = null;
        String  initialScreenName = "to_screen_8";

        try {
            // HttpServletRequest facesRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            userProfile = QwsController.signOn(request.getRemoteUser(), request);
            if(userProfile != null) {
              session.setAttribute("userProfile",userProfile);
            }
            // Initialize the QWS Controller, Validation, and Date services
            Logon.initializeQWSControllerValidationDate();
        } catch (Exception e) {
           // e.printStackTrace();
            errorMessage = bundle.getString("login_user_login_init_load_message");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
            mLogger.error(mLocalizer.x("LGH007: {0} :{1}",errorMessage,e.getMessage()),e);
            request.setAttribute(FAIL_INITIALIZATION, errorMessage + " : " + QwsUtil.getRootCause(e).getMessage());
            return FAIL_INITIALIZATION;
    
        }
        
        if( userProfile != null ) {
            session.setAttribute("user",request.getRemoteUser());
            try {
               initialScreenName = "to_screen_"+ConfigManager.getInstance().getInitialScreen().getID().toString();
              
                //Build the screen feilds array from EDM.xml file here and put it in session
                session.setAttribute("ScreenObject",ConfigManager.getInstance().getInitialScreen());
            } catch(Exception ex) {
                 //ex.printStackTrace();
                mLogger.error(mLocalizer.x("LGH008: {0} ",ex.getMessage()));
                request.setAttribute(FAIL_INITIALIZATION, "Failed to get the initial screen id : " + QwsUtil.getRootCause(ex).getMessage());
                return FAIL_INITIALIZATION;
            }
            return initialScreenName;            
        } else {
            errorMessage = bundle.getString("login_user_login_failure_message");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
            mLogger.error(mLocalizer.x("LGH009: {0} :{1}",errorMessage));
            request.setAttribute(FAIL_INITIALIZATION, "Failed to create the user profile.");

            return FAIL_INITIALIZATION;
        } 

      }
    
    
    
    public String[] getAllRoles() {
        // get the roles for this Userprofile
        return SecurityManager.getInstance().getAllRoles();
        

    }  
    
    

    
}
