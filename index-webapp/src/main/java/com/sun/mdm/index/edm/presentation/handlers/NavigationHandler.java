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

import java.io.IOException;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import net.java.hulp.i18n.LocalizationSupport;

public  class NavigationHandler {
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.NavigationHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    /** Static field for SOURCE_RECORDS*/
    private static final String DASH_BOARD = "dashboard";
    private static final String DUPLICATE_RECORDS = "duplicate-records";
    private static final String RECORD_DETAILS = "record-details";
    private static final String ASUMMED_MATCHES = "assumed-matches";
    public static final String SOURCE_RECORDS = "source-record";
    private static final String TRANSACTIONS = "transactions";
    private static final String REPORTS = "reports";
    private static final String AUDIT_LOG = "audit-log";
    public static final  String  MIDM_PROP = "com.sun.mdm.index.edm.presentation.messages.midm";
    public String  MIDM_PROP_JSP = "com.sun.mdm.index.edm.presentation.messages.midm";
    
    
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    /** Creates a new instance of NavigationHandler */
    public NavigationHandler() {
    }

    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toPatientDetails() {
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return RECORD_DETAILS;
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
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "Transaction Details";
    }

    
    /**
     * Navigation  to the transaction page.
     * @return <CODE>String</CODE>
     */
    public String toTransactions() {
        session.setAttribute("ScreenObject",getScreenObject(TRANSACTIONS));
        return TRANSACTIONS;
    }

    /**
     * This method is used to navigate to the duplicate records page.
     * @return <CODE>String</CODE>
     */
    public String toDuplicateRecords() {
        session.setAttribute("ScreenObject",getScreenObject(DUPLICATE_RECORDS));
        return DUPLICATE_RECORDS;
    }
    /**
     * Navigation  to the assumed matches page.
     * @return <CODE>String</CODE>
     */
    public String toAssumedMatches() {
        session.setAttribute("ScreenObject",getScreenObject(ASUMMED_MATCHES));
        return ASUMMED_MATCHES;
    }

    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toSourceRecords() {
        session.setAttribute("ScreenObject",getScreenObject(SOURCE_RECORDS));
        return SOURCE_RECORDS;
    }

    /**
     * Navigation  to the Reports page.
     * @return <CODE>String</CODE>
     */
    
    public String toReports() {
        session.setAttribute("ScreenObject",getScreenObject(REPORTS));
        return REPORTS;
    }

    /**
     * Navigation  to the audit log page.
     * @return <CODE>String</CODE>
     */
    
    public String toAuditLog() {
        session.setAttribute("ScreenObject",getScreenObject(AUDIT_LOG));
        return AUDIT_LOG;
    }    

    
    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toDashboard() {
        session.setAttribute("ScreenObject",getScreenObject(DASH_BOARD));
        return DASH_BOARD;
    }
    /**
     * This method is used to navigate to the patient deatils page.
     * @return <CODE>String</CODE>
     */
    public String toEditMainEuid() {
        session.setAttribute("ScreenObject",getScreenObject(RECORD_DETAILS));
        return "Edit Main EUID";
    }
    
    public ScreenObject getScreenObject(String tagName) {
        ScreenObject screenObject = null;
        try {
            ConfigManager.init();
            screenObject = ConfigManager.getInstance().getScreenObjectFromScreenName(tagName);
            session.removeAttribute("ScreenObject");
        } catch (Exception e) {
           // mLogger.error("Failed Get the Screen Object: ", e);
             mLogger.error(mLocalizer.x("NAV001: Failed to get the Screen Object :{0} ",e.getMessage()),e);
        }
        return screenObject;
    }

    public String getTagNameByScreenId(Integer screenId) {
        String tagName = null;
        try {
            ConfigManager.init();
            tagName = ConfigManager.getInstance().getScreenObjectTagName(screenId.toString());
        } catch (Exception e) {
            //mLogger.error("Failed Get the Screen Object: ", e);
             mLogger.error(mLocalizer.x("NAV002: Failed to get the Screen Object:{0} ",e.getMessage()),e);
        }
        return tagName;
    }
    public void setHeaderByTabName(ActionEvent event) {
        Integer screenId = (Integer) event.getComponent().getAttributes().get("screenId");
        String midmTagName = getTagNameByScreenId(screenId);
        //String tabName = (String) event.getComponent().getAttributes().get("headertabName");
        try {
            if (midmTagName.equalsIgnoreCase("dashboard")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.jsf");
            } else if (midmTagName.equalsIgnoreCase("duplicate-records")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("duplicaterecords.jsf");
            } else if (midmTagName.equalsIgnoreCase("record-details")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("recorddetails.jsf");
            } else if (midmTagName.equalsIgnoreCase("assumed-matches")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("assumedmatches.jsf");
            } else if (midmTagName.equalsIgnoreCase("source-record")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("sourcerecords.jsf");
            } else if (midmTagName.equalsIgnoreCase("reports")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("reports.jsf");
            } else if (midmTagName.equalsIgnoreCase("transactions")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("transactions.jsf");
            } else if (midmTagName.equalsIgnoreCase("audit-log")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("auditlog.jsf");
            }
        } catch (IOException ex) {
             mLogger.error(mLocalizer.x("NAV003: Exception has occured:{0} ",ex.getMessage()),ex);
        }
        session.setAttribute("ScreenObject", getScreenObject(midmTagName));
        //Remove the memory used for edit and merge source records.
        session.removeAttribute("singleSystemObjectLID");   
        session.removeAttribute("soHashMapArrayList");
        session.removeAttribute("eocomparision");
        //return headertabName;
    }
    public String  getMIDM_PROP( ){
    
      return MIDM_PROP;
    }

    public String getMIDM_PROP_JSP() {
        return MIDM_PROP_JSP;
    }

    public void setMIDM_PROP_JSP(String MIDM_PROP_JSP) {
        this.MIDM_PROP_JSP = MIDM_PROP_JSP;
    }
    
    
}
