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
package com.sun.mdm.multidomain.presentation.beans;

import java.util.List;
import java.util.ResourceBundle; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;
import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.core.ObjectFactoryRegistry;
import com.sun.mdm.multidomain.services.security.util.DateUtil;
import com.sun.mdm.multidomain.services.security.SecurityManager;
import com.sun.mdm.multidomain.services.security.UserProfile;
import com.sun.mdm.multidomain.services.model.Domain;
        
import com.sun.mdm.multidomain.presentation.util.Localizer;

/**
 * ApplicationHandler class.
 * @author cye
 */
public class ApplicationHandler {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.presentation.beans.ApplicationHandler");
    private static Localizer localizer = Localizer.getInstance();

    private HttpServletRequest request;
    private HttpSession session;

    /**
     * Create an instance of ApplicationHandler.
     */
    public ApplicationHandler(){        
    }
    
    /**
     * Initialize application after login succeeds.
     * @param request HttpServletRequest.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public void initialize(HttpServletRequest request) throws ConfigException {
        
        this.request = request;
        this.session = request.getSession();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sun.mdm.multidomain.presentation.util.messages", request.getLocale());
        
        try {
            if (!request.isUserInRole("MultiDomain.Admin") &&
                !request.isUserInRole("MultiDomain.User") &&
                !request.isUserInRole("MultiDomain.View") ) {
                throw new ConfigException("logged-on user is not in the specified roles.");
            }
            /* initialize MultiDomain configuration API */
            MDConfigManager.getInstance();
        
            /* initialize Service managerFactory */
            ServiceManagerFactory.Instance();

            /* initialize the date format */
            DateUtil.init();
        
            /* initialize ServiceManagerFactory */            
            ServiceManagerFactory.Instance().initialize();
            
            /* register objectFactory */
            //TBD List<Domain> domains = MDConfigManager.getInstance().getDomains();
            //for(Domain domain: domains) {
            //    ObjectFactoryRegistry.register(domain.getName());
            //}
            
            /* initialize validation service */
            //TBD ValidationService.init();
            
            /* initialize security manager */
            SecurityManager.getInstance();
            
            /* initialize user profile */
            //TBD UserProfile userProfile = new UserProfile(request.getRemoteUser(), request);
            
            //TBD session.setAttribute("userProfile", userProfile);
            session.setAttribute("user", request.getRemoteUser());
             
            logger.info(localizer.x("WEB001: application handler initialization completed."));
        } catch(ServiceException sex) {
            throw new ConfigException(sex);
        } catch(Exception ex) {
            throw new ConfigException(ex);            
        }               
    }
    
    /**
     * Logout. 
     */
    public void logout() {
        if(request != null && session != null) {
            request.setAttribute("logout", "LoggedOut");
            session.invalidate();
            logger.info(localizer.x("WEB002: application logged out."));            
        }
    }

    /**
     * Get the cofigured initial web page. 
     * @return String InitialPage.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public String getInitialPage() throws ConfigException {        
        //TBD MDConfigManager.getInstance().getInitialPage();
        //request.setAttribute("screenObject", screenObject);
        return "landingpage";
    }
}
