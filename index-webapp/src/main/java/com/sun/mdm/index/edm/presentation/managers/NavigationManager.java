/*
 * NavigationManager.java
 *
 * Created on August 31, 2007, 5:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 * 
 */

package com.sun.mdm.index.edm.presentation.managers;

import javax.faces.event.*;

/**
 * This managed bean class will be used for the navigation purpose. 
 * This is configured in faces-config.xml file
 * @author Administrator
 * @see <CODE>com.sun.mdm.index.edm.presentation.managers.NavigationManager</CODE>
 * @since <B>September,7, 2007</B>
 */
public class NavigationManager {
    /** Static field for ADD_EUID*/
    private static final String ADD_EUID = "addeuid";
    private static final String DASH_BOARD = "dashboard";
    private static final String DUP_REC = "duplicaterecords";
    private static final String PAT_DETAILS = "patientdetails";
    private static final String REPORTS = "reports";

    /** Creates a new instance of NavigationManager */
    public NavigationManager() {
    }
    
    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toAddEuid() {
        return ADD_EUID;
    }
    /**
     * This method is used to navigate dashboard page.
     * @return <CODE>String</CODE>
     */
    public String toDashboard() {
        return DASH_BOARD;
    }
    /**
     * This method is used to navigate to the duplicate records page.
     * @return <CODE>String</CODE>
     */
    public String toDupRecords() {
        return DUP_REC;
    }
    /**
     * Navigation  to the patient details page.
     * @return <CODE>String</CODE>
     */
    public String toPatientDetails() {
        return PAT_DETAILS;
    }
    /**
     * This method is used to navigate reports page.
     * @return <CODE>String</CODE>
     */
    public String toReports() {
        return REPORTS;
    }
}
