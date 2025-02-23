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
package com.sun.mdm.multidomain.presentation.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.validation.Validator;
import org.springframework.validation.BindException;

import com.sun.mdm.multidomain.presentation.beans.ApplicationHandler;
import com.sun.mdm.multidomain.services.core.ConfigException;

import net.java.hulp.i18n.Logger;
import com.sun.mdm.multidomain.presentation.util.Localizer;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;

/**
 * Loginontroller class.
 * @author cye
 */
public class LoginController extends BaseCommandController {
  private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.presentation.web.LoginController");
  private static Localizer localizer = Localizer.getInstance();
    
  private ApplicationHandler applicationHandler;
  
  public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    
    ModelAndView modelAndView = null;
    String pageId = null;
    String errorMessage = null;
    MDConfigManager.init();
    
    try {
        applicationHandler = (ApplicationHandler)getCommand(request);
        applicationHandler.initialize(request);        
        pageId = applicationHandler.getInitialPage();
    } catch (ConfigException cex){     
        errorMessage = cex.getLocalizedMessage();
    }
    
    request.getSession().setAttribute("mdwm_date_format", 
            MDConfigManager.getDateFormatForMultiDomain());
    request.getSession().setAttribute("mdwm_date_input_mask", 
            MDConfigManager.getDateInputMaskForMultiDomain());
    
    logger.info(localizer.x("WEB004: login controller delivered the request to the page {0}.", pageId));
    
    if (errorMessage != null) {
        modelAndView = new ModelAndView("loginerror", "errorMessage", errorMessage);
    } else if (pageId.equalsIgnoreCase("administration")) {
        //TBD: optional pages
        modelAndView = new ModelAndView("administration");
    } else if (pageId.equalsIgnoreCase("manage")) {
        //TBD: error page
        modelAndView = new ModelAndView("manage");        
    }
    return modelAndView;
  } 
  
}
