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

import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javax.faces.event.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


public class DashboardHandler {
    
    
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.DashboardHandler");
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    
    /** Creates a new instance of SearchDuplicatesHandler */
    public DashboardHandler() {
    }

    private int countPotentialDuplicates = 0;
    private int countAssumedMatches = 0;
    
    public int getCountPotentialDuplicates() throws ProcessingException, UserException, ValidationException,HandlerException{
        
        MasterControllerService  masterControllerService = new MasterControllerService();    
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
        
        MasterControllerService  masterControllerService = new MasterControllerService();    
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
}
