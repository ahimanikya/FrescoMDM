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
 * This managed bean class will be used for the navigation purpose. 
 * This is configured in faces-config.xml file
 * NavigationHandler.java 
 * Created on September 7, 2007, 12:15 AM
 * Author : RajaniKanth
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

public class NavigationHandler {
    
    /** Static field for SOURCE_RECORDS*/
    private static final String DASH_BOARD = "dashboard";
    private static final String DUPLICATE_RECORDS = "duplicate-records";
    private static final String RECORD_DETAILS = "record-details";
    private static final String ASUMMED_MATCHES = "assumed-matches";
    private static final String SOURCE_RECORDS = "source-record";
    private static final String TRANSACTIONS = "transactions";
    private static final String REPORTS = "reports";
    private static final String AUDIT_LOG = "audit-log";
    private static final String COMP_DUP = "Compare Duplicates";
    
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.NavigationHandler");
    
    /** Creates a new instance of NavigationHandler */
    public NavigationHandler() {
    }

    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toPatientDetails() {
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "to_screen_1";
    }
    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toCompareDuplicates() {
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "Compare Duplicates";
    }

    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toEuidDetails() {
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "EUID Details";
    }
     /**
     * This method is used to navigate to the Transaction details page.
     * @return <CODE>String</CODE>
     */
    public String toTransEuidDetails() {
        System.out.println("===> : to trans details");
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "Transaction Details";
    }

    
    /**
     * Navigation  to the transaction page.
     * @return <CODE>String</CODE>
     */
    public String toTransactions() {
        session.setAttribute("ScreenObject",getScreenObject(TRANSACTIONS));
        return "to_screen_2";
    }

    /**
     * This method is used to navigate to the duplicate records page.
     * @return <CODE>String</CODE>
     */
    public String toDuplicateRecords() {
        session.setAttribute("ScreenObject",getScreenObject(DUPLICATE_RECORDS));
        return "to_screen_3";
    }
    /**
     * Navigation  to the assumed matches page.
     * @return <CODE>String</CODE>
     */
    public String toAssumedMatches() {
        session.setAttribute("ScreenObject",getScreenObject(ASUMMED_MATCHES));
        return "to_screen_4";
    }

    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toSourceRecords() {
        session.setAttribute("ScreenObject",getScreenObject(SOURCE_RECORDS));
        return "to_screen_5";
    }

    /**
     * Navigation  to the Reports page.
     * @return <CODE>String</CODE>
     */
    
    public String toReports() {
        session.setAttribute("ScreenObject",getScreenObject(REPORTS));
        return "to_screen_6";
    }

    /**
     * Navigation  to the audit log page.
     * @return <CODE>String</CODE>
     */
    
    public String toAuditLog() {
        session.setAttribute("ScreenObject",getScreenObject(AUDIT_LOG));
        return "to_screen_7";
    }    

    
    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toDashboard() {
        // There is no screen object defined in the EDM.xml file for the "Dashboard"
        session.setAttribute("ScreenObject",getScreenObject(DASH_BOARD));
        return "to_screen_8";
    }
    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toEditMainEuid() {
        //System.out.println("Navigating to edit euid details page from Navi Handler");
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "Edit Main EUID";
    }
    
    public ScreenObject getScreenObject(String tagName) {
        ScreenObject screenObject = null;
        try {
            ConfigManager.init();
            //screenObject =  ConfigManager.getInstance().getScreen(screenId);
              screenObject = ConfigManager.getInstance().getScreenObjectFromScreenName(tagName);
            
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        return screenObject;
    }
    
}
