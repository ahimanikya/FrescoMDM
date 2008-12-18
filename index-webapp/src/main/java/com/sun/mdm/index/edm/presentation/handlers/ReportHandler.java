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
 * ReportHandler.java 
 * Created on November 23, 2007, 4:50 PM
 *  
 */ 
package com.sun.mdm.index.edm.presentation.handlers;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.event.*;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.edm.presentation.valueobjects.DeactivatedRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.MergedRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.UpdateRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.AssumeMatchesRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.DuplicateRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.ActivityRecords;
import com.sun.mdm.index.edm.presentation.valueobjects.UnmergedRecords;

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import net.java.hulp.i18n.LocalizationSupport;


public class ReportHandler {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ReportHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    String errorMessage = null;
    /**
     * Variable which identifies one of the the report.
     */
    private String reportType;
    /**
     * Used to get the Results fileds to be displayed in the JSP.
     */
    private ArrayList searchResultsScreenConfigArray;
    /**
     * Variable to hold the session
     */
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    /**
     * Variable to hold the screen object.
     */
    private ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    /**
     * Variable to hold the sub Screen object
     */
    private ScreenObject subScreenObject = null;
    /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;
    /**
     * Variable to hold Mas results defined in XML file defaulted to 100 records
     */
    private int maxResultsSize = 100;
    /**
     * Search Start Date
     */
    private String createStartDate = null;
    /**
     * Search End Date
     */
    private String createEndDate = null;
    /**
     * Search Start Time
     */
    private String createStartTime = null;
    /**
     * Search end Time
     */
    private String createEndTime = null;
    /**
     * Search DuplicateReports Function
     */
    private String reportFunction;
    /**
     * EUID
     */
    private String euid;
    /**
     * LID
     */
    private String lid;
    /**
     * System
     */
    private String system;
    /**
     * Search Maximum Reports in DuplicateReports & AssumeMatchReports
     */
    private String reportSize;
    /**
     * Hashmap to hold the search parameters
     */
    private HashMap reportParameters = new HashMap();
    /**
     * Search Maximum page size
     */
    private int pageSize;
    private ArrayList<SelectItem> selectOptions = new ArrayList();
    /**
     * Search ActivityReports ViewReports
     */
    private String frequency = "Weekly";
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    /**
     * Instance of Deactivated Report Handler
     */
    private DeactivatedReportHandler deactivatedReport = new DeactivatedReportHandler();
    /**
     * Instance of Assume Match Report Handler
     */
    private AssumeMatchReportHandler assumeMatchReport = new AssumeMatchReportHandler();
    /**
     * Instance of Duplicate Report Handler
     */
    private DuplicateReportHandler duplicateReport = new DuplicateReportHandler();
    /**
     * Instance of Update Report Handler
     */
    private UpdateReportHandler updateReport = new UpdateReportHandler();
    /**
     * Instance of UnMerge Report Handler
     */
    private UnmergedRecordsHandler unmergedRecordsHandler = new UnmergedRecordsHandler();
    /**
     * Instance of Merged Report Handler
     */
    private MergeRecordHandler mergedRecordsHandler = new MergeRecordHandler();
    /**
     * Instance of Activity Report Handler
     */
    private ActivityReportHandler activityReport = new ActivityReportHandler();
    /*
     * Value Object to hold the Deactivate Reports
     */
    private DeactivatedRecords[] deactivatedRecordsVO = null;
    /*
     * Value Object to hold the Merged Reports
     */
    private MergedRecords[] mergedRecordsVO = null;
    /*
     * Value Object to hold the UnMerged Reports
     */
    private UnmergedRecords[] unmergedRecordsVO = null;
    /*
     * Value Object to hold the Updated Report
     */
    private UpdateRecords[] updateRecordsVO = null;
    /*
     * Value Object to hold the Duplicate Records
     */
    private DuplicateRecords[] duplicateRecordsVO = null;
    /*
     * Value Object to hold the Assume Match Reports
     */
    private AssumeMatchesRecords[] assumematchesRecordsVO = null;
    /*
     * Value Object to hold the Activity Reports
     */
    private ActivityRecords[] activityRecordsVO = null;
    
    /**
     * Validation helper class
     */
    EDMValidation edmValidation = new EDMValidation();         
    
    /**
     * HashMap of Field Display Name
     */
    private HashMap keyDescriptionsMap = new HashMap();
    
    private ArrayList<SelectItem> activityReportTypes = new ArrayList();

    private ArrayList searchScreenConfigArray;

    //resource bundle definitin
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");

    //get locale specific texts for report tab names
    String Merged_Transaction_Report_Label = bundle.getString("Merged_Transaction_Report_Label");
    String Deactivated_Record_Report_Label = bundle.getString("Deactivated_Record_Report_Label");
    String Unmerged_Transaction_Report_Label = bundle.getString("Unmerged_Transaction_Report_Label");
    String Updated_Record_Report_Label = bundle.getString("Updated_Record_Report_Label");
    String Activity_Report_Label = bundle.getString("Activity_Report_Label");
    String Potential_Duplicate_Report_Label = bundle.getString("Potential_Duplicate_Report_Label");
    String Assumed_Matches_Report_Label = bundle.getString("Assumed_Matches_Report_Label");

    //Constructor
    public ReportHandler() {

    }
        

    public ArrayList deactivatedReport() {        
        request.setAttribute("reportTabName", Deactivated_Record_Report_Label);
        ArrayList duplicateRecordsResults = new ArrayList();
        try {
             
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            
            //Check Valid report Size
            if(!validateReportSize()) return null;
            
            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Deactivated_Record_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Deactivated_Record_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }

            //Set paramaters for the search
            getDeactivatedReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            getDeactivatedReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            getDeactivatedReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getDeactivatedReport().setCreateEndDate((String) reportParameters.get("EndDate"));

            //if results size is supplied by the user
            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getDeactivatedReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getDeactivatedReport().setMaxResultsSize(getMaxReportSize(Deactivated_Record_Report_Label));
            }
            getDeactivatedReport().setPageSize(getRecordsPerPage(Deactivated_Record_Report_Label));
            
            
            duplicateRecordsResults = getDeactivatedReport().deactivateReport();
            //setDeactivatedRecordsVO(deactivatedReport.deactivateReport());
            if (duplicateRecordsResults != null && duplicateRecordsResults.size() > 0) {
              setResultsSize(duplicateRecordsResults.size());
            }
            return duplicateRecordsResults;

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT001: Unable to get deactivated report :{0} ", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT002: Unable to get deactivated report: {0} ", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT003: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT004: Unable to get deactivated report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RPT005: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
    }

    public ArrayList mergeReport() {
        request.setAttribute("reportTabName", Merged_Transaction_Report_Label);
        ArrayList mergeResults = new ArrayList();

        try {
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;

            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Merged_Transaction_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Merged_Transaction_Report_Label);
            if (requiredErrorsList.size() > 0 ) {
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }

            //Set paramaters for the search
            getMergedRecordsHandler().setCreateStartTime((String) reportParameters.get("StartTime"));
            getMergedRecordsHandler().setCreateEndTime((String) reportParameters.get("EndTime"));
            getMergedRecordsHandler().setCreateStartDate((String) reportParameters.get("StartDate"));
            getMergedRecordsHandler().setCreateEndDate((String) reportParameters.get("EndDate"));
             
            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getMergedRecordsHandler().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getMergedRecordsHandler().setMaxResultsSize(getMaxReportSize(Merged_Transaction_Report_Label));
            }
            getMergedRecordsHandler().setPageSize(getRecordsPerPage(Merged_Transaction_Report_Label));

            mergeResults = getMergedRecordsHandler().mergeReport();

            if (mergeResults != null && mergeResults.size() > 0) {
                setResultsSize(mergeResults.size() / 2);
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("RPT006: Failed to get Merge report {0} ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT007: Failed to get Merge report {0} ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT008: Failed to get Merge report {0} ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH099: Unable to get Merge report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH010: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        return mergeResults;
    }

    public ArrayList activitiesReport() {
        request.setAttribute("reportTabName", Activity_Report_Label);
        try {
            /*Set paramaters for the search
            //getActivityReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            //getActivityReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            //getActivityReport().setCreateEndDate((String) reportParameters.get("EndDate"));
            
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getActivityReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getActivityReport().setMaxResultsSize(getMaxReportSize(Activity_Report_Label));
            }
            getActivityReport().setPageSize(getRecordsPerPage(Activity_Report_Label));
             
            

            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Activity_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Activity_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }
            */
            

            if (reportParameters.get("StartDate") == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("enter_date_from"), bundle.getString("enter_date_from")));
                return null;
            }

            if (reportParameters.get("StartDate") != null && reportParameters.get("StartDate").toString().length() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("enter_date_from"), bundle.getString("enter_date_from")));
                return null;
            }
               
               
            getActivityReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getActivityReport().setFrequency((String) reportParameters.get("activityType"));
            
            ArrayList results = getActivityReport().activityReport();

            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT009: Unable to get activity report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT010: Unable to get activity report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT011: Unable to get activity report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT012: Unable to get activity report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RPT013: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }

    public ArrayList unMergeReport() {
        request.setAttribute("reportTabName", Unmerged_Transaction_Report_Label);
        ArrayList results = new ArrayList();
        try {
            //Set paramaters for the search
            getUnmergedRecordsHandler().setCreateStartTime((String) reportParameters.get("StartTime"));
            getUnmergedRecordsHandler().setCreateEndTime((String) reportParameters.get("EndTime"));
            getUnmergedRecordsHandler().setCreateStartDate((String) reportParameters.get("StartDate"));
            getUnmergedRecordsHandler().setCreateEndDate((String) reportParameters.get("EndDate"));
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getUnmergedRecordsHandler().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getUnmergedRecordsHandler().setMaxResultsSize(getMaxReportSize(Unmerged_Transaction_Report_Label));
            }
            getUnmergedRecordsHandler().setPageSize(getRecordsPerPage(Unmerged_Transaction_Report_Label));

            
            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Unmerged_Transaction_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Unmerged_Transaction_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }
            
            results = getUnmergedRecordsHandler().unmergeReport();
            
            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            
            return results;
        } 
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT014: Failed to get unMerge report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT015: Failed to get unMerge report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT016: Failed to get unMerge report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT017: Unable to get unMerge report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RPT018: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }

    public ArrayList updateReport() {
        ArrayList results = new ArrayList();
        try {
            //Set paramaters for the search            
            request.setAttribute("reportTabName", Updated_Record_Report_Label);
            //Set paramaters for the search
            getUpdateReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            getUpdateReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            getUpdateReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getUpdateReport().setCreateEndDate((String) reportParameters.get("EndDate"));
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getUpdateReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getUpdateReport().setMaxResultsSize(getMaxReportSize(Updated_Record_Report_Label));
            }
            getUpdateReport().setPageSize(getRecordsPerPage(Updated_Record_Report_Label));
            
            
            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Updated_Record_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Updated_Record_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }

            results = getUpdateReport().updateReport();

            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT019: Unable to get the updated report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT020: Unable to get the updated report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT021: Unable to get the updted report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT022: Unable to get the updated report "), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                 } else {
                    mLogger.error(mLocalizer.x("RPT023: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                 }
            }
            return null;
        }
    }

    public ArrayList duplicateReport() {
        try {
            request.setAttribute("reportTabName", Potential_Duplicate_Report_Label);
            //Set paramaters for the search
            getDuplicateReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            getDuplicateReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            getDuplicateReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getDuplicateReport().setCreateEndDate((String) reportParameters.get("EndDate"));
            getDuplicateReport().setDuplicateStatus((String) reportParameters.get("Status"));
            
            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;
            
            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getDuplicateReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getDuplicateReport().setMaxResultsSize(getMaxReportSize(Potential_Duplicate_Report_Label));
            }
            getDuplicateReport().setPageSize(getRecordsPerPage(Potential_Duplicate_Report_Label));
            

            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Potential_Duplicate_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Potential_Duplicate_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }
            
            ArrayList results = getDuplicateReport().duplicateReport();
            if(results != null && results.size() > 0) {
              setResultsSize(results.size()/2);
            }
            return results;
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT024: Unable to get duplicate report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT025: Unable to get duplicate report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT026: Unable to get duplicate report  {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT027: Unable to get duplicate report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RPT028: Unable to get duplicate report"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }

    public ArrayList assumeMatchReport() {
        setReportType(Assumed_Matches_Report_Label);
        request.setAttribute("reportTabName", Assumed_Matches_Report_Label);
        try {

            //check one of many condition here
            if(!checkOneOfManyCondition()) return null;
            //Check Valid report Size
            if(!validateReportSize()) return null;
            
            //Set paramaters for the search
            getAssumeMatchReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            getAssumeMatchReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            getAssumeMatchReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getAssumeMatchReport().setCreateEndDate((String) reportParameters.get("EndDate"));

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getAssumeMatchReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getAssumeMatchReport().setMaxResultsSize(getMaxReportSize(Assumed_Matches_Report_Label));
            }
            getAssumeMatchReport().setPageSize(getRecordsPerPage(Assumed_Matches_Report_Label));
            
            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = checkOneOfGroupCondition(Assumed_Matches_Report_Label);
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = isRequiredCondition(Assumed_Matches_Report_Label);
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }

            ArrayList results = getAssumeMatchReport().assumeMatchReport();
            
            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RPT029: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RPT030: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RPT031: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RPT032: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RPT033: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }

    /**
     *  Validate Report Size and Page Size
     *  Create Date: 06/13/2008
     *  Validates for a valid Report Size
     */
    private boolean validateReportSize()   {
        String thisReportSize = (String) reportParameters.get("MaxResultSize");
        //Report Size should be validated here,, not in the specific handler
        if (thisReportSize != null && thisReportSize.length() > 0) {
            String message = edmValidation.validateNumber(thisReportSize);
             
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0 ? message : message);
                String msg3 = bundle.getString("reports_reportsize_text") + " ";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg3 + errorMessage, errorMessage));
                 mLogger.info(mLocalizer.x("RPT034: {0}", msg3 + errorMessage));
                return false;
            } 
            
                
            if (Integer.parseInt(thisReportSize) < 1) {  //if its a valid number, check for negative value                
                errorMessage = bundle.getString("REPORT_SIZE_MINIMUM_VALUE");
                String msg3 = bundle.getString("reports_reportsize_text") + " ";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg3 +errorMessage, msg3 + errorMessage));
                //java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                mLogger.info(mLocalizer.x("RPT035: {0}", msg3  +errorMessage));
                return false;
            }
        }
        return true;
    }
                
    
      
   
     
    /**
     * @return createStartDate
     */
    public String getCreateStartDate() {
        return createStartDate;
    }

    /**
     * @param createStartDate
     * Sets the Start Date
     */
    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }

    /**
     * @return Create End Date
     */
    public String getCreateEndDate() {
        return createEndDate;
    }

    /**
     * Sets the End date parameter for the search
     * @param createEndDate
     */
    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    /**
     * @return create Start Date
     */
    public String getCreateStartTime() {
        return createStartTime;
    }

    /**
     * Sets the Start timeparameter for the search
     * @param createStartTime 
     */
    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    /**
     * @return Create End time
     */
    public String getCreateEndTime() {
        return createEndTime;
    }

    /**
     * Sets the End time parameter for the search
     * @param createEndTime 
     */
    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    /**
     * 
     * @return
     */
    public String getReportFunction() {
        return reportFunction;
    }

    /**
     * set Report Frequncy 
     * @param function
     */
    public void setReportFunction(String function) {
        this.reportFunction = function;
    }

    /**
     * @return Report Size
     */
    public String getReportSize() {
        return reportSize;
    }

    /**
     * Sets the Reports Size parameter for the search
     * @param reportSize 
     */
    public void setReportSize(String reportSize) {
        this.reportSize = reportSize;
    }

    /**
     * 
     * @return
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * set Report Frequncy 
     * @param freqency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public DeactivatedRecords[] getDeactivatedRecordsVO() {
        return deactivatedRecordsVO;
    }

    public void setDeactivatedRecordsVO(DeactivatedRecords[] deactivatedRecordsVO) {
        this.deactivatedRecordsVO = deactivatedRecordsVO;
    }

    public MergedRecords[] getMergedRecordsVO() {
        return mergedRecordsVO;
    }

    public void setMergedRecordsVO(MergedRecords[] mergedRecordsVO) {
        this.mergedRecordsVO = mergedRecordsVO;
    }

    public UnmergedRecords[] getUnmergedRecordsVO() {
        return unmergedRecordsVO;
    }

    public void setUnmergedRecordsVO(UnmergedRecords[] unmergedRecordsVO) {
        this.unmergedRecordsVO = unmergedRecordsVO;
    }

    public UpdateRecords[] getUpdateRecordsVO() {
        return updateRecordsVO;
    }

    public void setUpdateRecordsVO(UpdateRecords[] updateRecordsVO) {
        this.updateRecordsVO = updateRecordsVO;
    }

    public DuplicateRecords[] getDuplicateRecordsVO() {
        return duplicateRecordsVO;
    }

    public void setDuplicateRecordsVO(DuplicateRecords[] duplicateRecordsVO) {
        this.duplicateRecordsVO = duplicateRecordsVO;
    }

    public AssumeMatchesRecords[] getAssumematchesRecordsVO() {
        return assumematchesRecordsVO;
    }

    public void setAssumematchesRecordsVO(AssumeMatchesRecords[] assumematchesRecordsVO) {
        this.assumematchesRecordsVO = assumematchesRecordsVO;
    }

    public ActivityRecords[] getActivityRecordsVO() {
        return activityRecordsVO;
    }

    public void setActivityRecordsVO(ActivityRecords[] activityRecordsVO) {
        this.activityRecordsVO = activityRecordsVO;
    }

    public ArrayList getSearchResultsScreenConfigArray() {
        ArrayList newArrayList = new ArrayList();
        try {
            HttpSession sessionLocal = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            ScreenObject screenObjectLocal = (ScreenObject) sessionLocal.getAttribute("ScreenObject");

            //Array of Sub screen objects as Screen Objects
            ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();
            Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();
            ScreenObject resultsScreenObject = null;

            for (int i = 0; i < subScreenObjects.length; i++) {
                subScreenObject = (ScreenObject) subScreenObjects[i];
                if (subScreenObject.getDisplayTitle().equalsIgnoreCase(getReportType())) {
                    resultsScreenObject = subScreenObject;
                }
            }
            ArrayList resultsScreenConfigArraySub = resultsScreenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = resultsScreenConfigArraySub.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
                ArrayList fcgList = objSearchScreenConfig.getFieldConfigs();
                for (int i = 0; i < fcgList.size(); i++) {
                    FieldConfigGroup objectFieldConfigGroup = (FieldConfigGroup) fcgList.get(i);
                    ArrayList fcList = objectFieldConfigGroup.getFieldConfigs();
                    for (int j = 0; j < fcList.size(); j++) {
                        FieldConfig objectFieldConfig = (FieldConfig) fcList.get(j);
                        newArrayList.add(objectFieldConfig);
                    }
                }

            }
 
        } catch (Exception e) {
            mLogger.error(mLocalizer.x("RPT036: Unable to get search result config :{0} ", e.getMessage()), e);
        }

        this.searchResultsScreenConfigArray = newArrayList;

        return searchResultsScreenConfigArray;
    }

    public void setSearchResultsScreenConfigArray(ArrayList searchResultsScreenConfigArray) {
        this.searchResultsScreenConfigArray = searchResultsScreenConfigArray;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int count) {
        this.pageSize = count;
    }

    public int getMaxResultsSize() {
        return maxResultsSize;
    }

    public void setMaxResultsSize(int maxResultsSize) {
        this.maxResultsSize = maxResultsSize;
    }

    public void setReportsTabName(ActionEvent event) {
        String reportTabName = (String) event.getComponent().getAttributes().get("tabName");
        request.setAttribute("tabName", reportTabName);

    }
    public void setSelectOptions(ArrayList<SelectItem> selectOptions) {
        this.selectOptions = selectOptions;
    }

    public ArrayList getSearchResultsArrayByReportType(String reportTypeVar) {
        ArrayList newArrayList = new ArrayList();
        try {
            //Array of Sub screen objects as Screen Objects
            ArrayList resultsSubScreenConfigArray = screenObject.getSubscreensConfig();
            Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();
            ScreenObject resultsScreenObject = null;

            for (int i = 0; i < subScreenObjects.length; i++) {
                subScreenObject = (ScreenObject) subScreenObjects[i];
                if (subScreenObject.getDisplayTitle().equalsIgnoreCase(reportTypeVar)) {
                    resultsScreenObject = subScreenObject;
                }
            }

            ArrayList resultsScreenConfigArraySub = resultsScreenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = resultsScreenConfigArraySub.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
                ArrayList fcgList = objSearchScreenConfig.getFieldConfigs();
                for (int i = 0; i < fcgList.size(); i++) {
                    FieldConfigGroup objectFieldConfigGroup = (FieldConfigGroup) fcgList.get(i);
                    ArrayList fcList = objectFieldConfigGroup.getFieldConfigs();
                    for (int j = 0; j < fcList.size(); j++) {
                        FieldConfig objectFieldConfig = (FieldConfig) fcList.get(j);
                        newArrayList.add(objectFieldConfig);
                    }
                }

            }
        } catch (Exception e) {
            mLogger.error(mLocalizer.x("RPT037: Unable to get search results : {0} ", e.getMessage()), e);
        }
        return newArrayList;

    }

    public ArrayList getSearchScreenConfigArray() {
        ScreenObject screenObjectLocal = (ScreenObject) session.getAttribute("ScreenObject");

        //Array of Sub screen objects as Screen Objects
        ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();

        Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();

        ScreenObject resultsScreenObject = null;

        for (int i = 0; i < subScreenObjects.length; i++) {
            subScreenObject = (ScreenObject) subScreenObjects[i];
            if (subScreenObject.getDisplayTitle().equalsIgnoreCase(getReportType())) {
                resultsScreenObject = subScreenObject;
            }
        }

        ArrayList screenConfigArraySub = resultsScreenObject.getSearchScreensConfig();
        return screenConfigArraySub;
    }

     
    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    public ArrayList<SelectItem> getActivityReportTypes() {
        setReportType(Activity_Report_Label);
        ArrayList searchScreenConfigArrayList = getSearchScreenConfigArray();
        ArrayList newArrayList = new ArrayList();

        for (int i = 0; i < searchScreenConfigArrayList.size(); i++) {
            SearchScreenConfig searchScreenConfig = (SearchScreenConfig) searchScreenConfigArrayList.get(i);
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(searchScreenConfig.getScreenTitle());
            selectItem.setValue(searchScreenConfig.getScreenTitle());
            newArrayList.add(selectItem);
        }
        activityReportTypes = newArrayList;
        return activityReportTypes;
    }

    public void setActivityReportTypes(ArrayList<SelectItem> activityReportTypes) {
        this.activityReportTypes = activityReportTypes;
    }

    public int getDisplayOrder(String tabName) {
        int displayOrder = -1;
        ScreenObject screenObjectLocal = (ScreenObject) session.getAttribute("ScreenObject");

        //Array of Sub screen objects as Screen Objects
        ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();

        Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();

        ScreenObject resultsScreenObject = null;

        for (int i = 0; i < subScreenObjects.length; i++) {
            subScreenObject = (ScreenObject) subScreenObjects[i];
            if (subScreenObject.getDisplayTitle().equalsIgnoreCase(tabName)) {
                displayOrder = subScreenObject.getDisplayOrder();
            }
        }

        return displayOrder;

    }

    public boolean isTabExists(String tabName) {
        boolean tabNameExists = false;
        ScreenObject screenObjectLocal = (ScreenObject) session.getAttribute("ScreenObject");

        //Array of Sub screen objects as Screen Objects
        ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();

        Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();

        for (int i = 0; i < subScreenObjects.length; i++) {
            subScreenObject = (ScreenObject) subScreenObjects[i];
            if (subScreenObject.getDisplayTitle().equalsIgnoreCase(tabName)) {
                tabNameExists = true;
            }
        }

        return tabNameExists;

    }

    public HashMap getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(HashMap reportParameters) {
        this.reportParameters = reportParameters;
    }

    /**
     * Instance of Merged Report Handler
     */
    public MergeRecordHandler getMergedRecordsHandler() {
        return mergedRecordsHandler;
    }

    public void setMergedRecordsHandler(MergeRecordHandler mergedRecordsHandler) {
        this.mergedRecordsHandler = mergedRecordsHandler;
    }

    public String getEuid() {
        return euid;
    }

    public void setEuid(String euid) {
        this.euid = euid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public /**
             * Instance of Deactivated Report Handler
             */
            DeactivatedReportHandler getDeactivatedReport() {
        return deactivatedReport;
    }

    public void setDeactivatedReport(DeactivatedReportHandler deactivatedReport) {
        this.deactivatedReport = deactivatedReport;
    }

    public /**
             * Instance of Activity Report Handler
             */
            ActivityReportHandler getActivityReport() {
        return activityReport;
    }

    public void setActivityReport(ActivityReportHandler activityReport) {
        this.activityReport = activityReport;
    }

    public /**
             * Instance of UnMerge Report Handler
             */
            UnmergedRecordsHandler getUnmergedRecordsHandler() {
        return unmergedRecordsHandler;
    }

    public void setUnmergedRecordsHandler(UnmergedRecordsHandler unmergedRecordsHandler) {
        this.unmergedRecordsHandler = unmergedRecordsHandler;
    }

    public int getMaxReportSize(String tabName) {
        int maxSize = 0;
        ScreenObject screenObjectLocal = (ScreenObject) session.getAttribute("ScreenObject");

        //Array of Sub screen objects as Screen Objects
        ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();

        Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();

        ScreenObject resultsScreenObject = null;

        for (int i = 0; i < subScreenObjects.length; i++) {
            subScreenObject = (ScreenObject) subScreenObjects[i];
            if (subScreenObject.getDisplayTitle().equalsIgnoreCase(tabName)) {
                SearchResultsConfig searchResultsConfig = (SearchResultsConfig) subScreenObject.getSearchResultsConfig().toArray()[0];
                maxSize = searchResultsConfig.getMaxRecords();
            }
        }

        return maxSize;

    }

    public int getRecordsPerPage(String tabName) {
        int recordsPerPage = 0;
        ScreenObject screenObjectLocal = (ScreenObject) session.getAttribute("ScreenObject");

        //Array of Sub screen objects as Screen Objects
        ArrayList resultsSubScreenConfigArray = screenObjectLocal.getSubscreensConfig();

        Object[] subScreenObjects = resultsSubScreenConfigArray.toArray();

        ScreenObject resultsScreenObject = null;

        for (int i = 0; i < subScreenObjects.length; i++) {
            subScreenObject = (ScreenObject) subScreenObjects[i];
            if (subScreenObject.getDisplayTitle().equalsIgnoreCase(tabName)) {
                SearchResultsConfig searchResultsConfig = (SearchResultsConfig) subScreenObject.getSearchResultsConfig().toArray()[0];
                recordsPerPage = searchResultsConfig.getPageSize();
            }
        }

        return recordsPerPage;

    }

    public /**
             * Instance of Update Report Handler
             */
            UpdateReportHandler getUpdateReport() {
        return updateReport;
    }

    public void setUpdateReport(UpdateReportHandler updateReport) {
        this.updateReport = updateReport;
    }

     /**
     * Instance of Assume Match Report Handler
     */
    public AssumeMatchReportHandler getAssumeMatchReport() {
        return assumeMatchReport;
    }

    public void setAssumeMatchReport(AssumeMatchReportHandler assumeMatchReport) {
        this.assumeMatchReport = assumeMatchReport;
    }

    public /**
     * Instance of Duplicate Report Handler
     */
    DuplicateReportHandler getDuplicateReport() {
        return duplicateReport;
    }

    public void setDuplicateReport(DuplicateReportHandler duplicateReport) {
        this.duplicateReport = duplicateReport;
    }
    
     public ArrayList<SelectItem> getSelectOptions() {
        MasterControllerService masterControllerService  = new MasterControllerService();
        String[][] systemCodesArray = masterControllerService.getSystemCodes();
        
        HashMap  SystemCodeDesc = new HashMap ();
        
        String[] pullDownListItems = systemCodesArray[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            String sysDesc = masterControllerService.getSystemDescription(pullDownListItems[i]);
            String  sysCode  = pullDownListItems[i];
            SystemCodeDesc.put(sysCode, sysDesc);
            }
            HashMap  sortedSyscode = getSortedMap(SystemCodeDesc);
            Set  sysCodeSet =  sortedSyscode.keySet();
            Iterator it = sysCodeSet.iterator();
            while(it.hasNext()){
            SelectItem selectItem = new SelectItem();
            String  sysCode  = (String)it.next();
            String sysDesc = masterControllerService.getSystemDescription(sysCode);
            selectItem.setLabel(sysDesc);
            selectItem.setValue(sysCode);
            newArrayList.add(selectItem);
        }
       
           selectOptions = newArrayList;
           return selectOptions; 
  
    }

    
    public HashMap getSortedMap(HashMap hmap)
	 {
		HashMap map = new LinkedHashMap();
		List mapKeys = new ArrayList(hmap.keySet());
		List mapValues = new ArrayList(hmap.values());
		hmap.clear();
		TreeSet sortedSet = new TreeSet(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
 		for (int i=0; i<size; i++){
 		map.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);
     	}
		return map;
	}

    /**
 * Checks if the user has entered at least on of the the required fields in the group as
 * defined in the midm configuration file 
 * Modified Date:05/27/2008
 * @see isRequiredCondition
 * @return HashMap - List of all Groups along with the fields names in the group 
 */    
    public HashMap checkOneOfGroupCondition(String reportTypeArg) {
        setReportType(reportTypeArg);
        //get the field config groups here
        ArrayList fgGroups = getSearchScreenFieldGroupArray(getSearchScreenConfigArray());
        
        HashMap errorMap = new HashMap();
        boolean oneOfGroupValuesChecked = false;                
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            boolean oneOfGroupValuesEntered = false;    
            ArrayList errorsMapList = new ArrayList();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                //if one of these is required
                if (basicFieldConfig.isOneOfTheseRequired()) {

                            errorsMapList.add(basicFieldConfig.getDisplayName());
                      if (getReportParameters().get(basicFieldConfig.getName()) != null) {
                        String value = ((String) getReportParameters().get(basicFieldConfig.getName())).trim();                        
                        oneOfGroupValuesChecked = true;
                        if (value.length() > 0) { //Value found for this key
                          
                            oneOfGroupValuesEntered = true;
                        }
                    }
                } //one of required condition
            } // Field config loop
           
            if (!oneOfGroupValuesEntered && oneOfGroupValuesChecked)   {
                errorMap.put(basicSearchFieldGroup.getDescription(), errorsMapList);
                oneOfGroupValuesChecked = false;
            }
         } // Field group loop
       
        return errorMap;
    }

/**
 * Checks if the user has entered the values for all the required fields as
 * defined in the midm configuration file 
 * Modified Date:05/28/2008
 * @see checkOneOfGroupCondition
 * @see isRequiredCondition
 * @return ArrayList - List of all fields for which the user did not enter the values
 */    
    public ArrayList isRequiredCondition(String reportTypeArg) {
        ArrayList errorsMapList = new ArrayList();
        setReportType(reportTypeArg);
        //get the field config groups here
        ArrayList fgGroups = getSearchScreenFieldGroupArray(getSearchScreenConfigArray());
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                String s = ((String) getReportParameters().get(basicFieldConfig.getName())).trim();
                //if one of these is required
                if (basicFieldConfig.isRequired()) {
                      if (getReportParameters().get(basicFieldConfig.getName()) != null) {
                        String value = ((String) getReportParameters().get(basicFieldConfig.getName())).trim();
                        if (value.length() == 0) { //Value found for this key
                            
                            errorsMapList.add(basicFieldConfig.getDisplayName());                            
                        }
                        
                    }
                } //isrequired condition
            } // Field config loop
         } // Field group loop
        return errorsMapList;
    }

    public ArrayList getSearchScreenFieldGroupArray(ArrayList screenConfigList) {
        ArrayList searchScreenFieldGroupArray = new ArrayList();

        ArrayList basicSearchFieldConfigs;
        try {

            //ArrayList screenConfigList = screenObject.getSearchScreensConfig();

            Iterator iteratorScreenConfig = screenConfigList.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                    searchScreenFieldGroupArray.add(basicSearchFieldGroup);
                }
            }

        } catch (Exception e) {
            mLogger.error(mLocalizer.x("RPT038: Failed to get SearchScreenField GroupArray:{0}", e.getMessage()));
        }

        return searchScreenFieldGroupArray;
    }

 /**
 * modified date:05/28/2008
 * This method checks if the screen has any One of Group condition
 * @return true if the oneOfGroup exists in the screen configuration
 */
    public boolean isOneOfGroupExists(String reportTypeArg) {
        boolean oneOfGroupExists = false;
        setReportType(reportTypeArg);
        //get the field config groups here
        ArrayList fgGroups = getSearchScreenFieldGroupArray(getSearchScreenConfigArray());
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
             ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
             for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                 //if one of these is required
                if (basicFieldConfig.isOneOfTheseRequired()) {
                    return true;
                } //one of required condition
                 
            } // Field config loop
          } // Field group loop

        return oneOfGroupExists;
    }
    
    
/**
 * modified date:05/28/2008
 * This method checks if the screen has required fields
 * @return true if required field exists in the screen configuration
 */
    public boolean isRequiredExists(String reportTypeArg) {
         boolean requiredExists = false;
        //get the field config groups here
        ArrayList fgGroups = getSearchScreenFieldGroupArray(getSearchScreenConfigArray());
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
             ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
             for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                 //if required fields are present
                if (basicFieldConfig.isRequired()) {
                    return true;
                } //one of required condition
                 
            } // Field config loop
          } // Field group loop
        return requiredExists;
    }
/**
 * modified date:05/28/2008
 * This method checks if the screen has required fields
 * @return false if the user has not entered any values 
 */
  public boolean checkOneOfManyCondition() {       // modified on 17-11-08 as fix of #55
        HashMap oneOfMap = getReportParameters();
        Object[] keySet = oneOfMap.keySet().toArray();
        for (int i = 0; i < keySet.length; i++) {
            String key = (String) keySet[i];
            /*if (key.equalsIgnoreCase("MaxResultSize")) { 
                continue;
            }*/
            if ((oneOfMap.get(key) != null && ((String) oneOfMap.get(key)).trim().length() > 0)) {
               return true;
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("potential_dup_search_error"), bundle.getString("potential_dup_search_error")));
        mLogger.info(mLocalizer.x("RPT039: Validation failed : {0}", bundle.getString("potential_dup_search_error")));                
        return false;
    }
/**
 * 
 * @return HashMap of Display Names
 */
    public HashMap getKeyDescriptionsMap() {        
        HashMap newHashMap = new HashMap();
        //get the field config groups here
        ArrayList fgGroups = getSearchScreenFieldGroupArray(getSearchScreenConfigArray());
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                if (!basicFieldConfig.isRange()) {
                    newHashMap.put(basicFieldConfig.getName(), basicFieldConfig.getDisplayName());
                }

            } // Field config loop

        } // Field group loop

        keyDescriptionsMap = newHashMap;
        return keyDescriptionsMap;
    }

    public void setKeyDescriptionsMap(HashMap keyDescriptionsMap) {
        this.keyDescriptionsMap = keyDescriptionsMap;
    }

/**
 * This method checks for the valid ordering of the Report Sub tabs, 
 * @create date: July 7th 2008
 * @return valid screen object array 
 * @exception Exception if unhandled exception occurs such as 
 *  1. Array manuplation fails
 *  2. NumberFormatException if the XML subscreen display number is not a number
 *  
 */    
  public ScreenObject[] getOrderedScreenObjects() {
    ScreenObject screenObjectObj = (ScreenObject) session.getAttribute("ScreenObject");
    ArrayList subTabsLabelsList = screenObjectObj.getSubscreensConfig();
    Object[] subTabsLabelsListObj = subTabsLabelsList.toArray();
    ScreenObject[] orderdedScreens = new ScreenObject[subTabsLabelsList.size()];
    HashMap screenObjectsMap = new HashMap();
    ArrayList messageList = new ArrayList();
    HashMap messageMap = new HashMap();
    try {        
          for (int i = 0; i < subTabsLabelsListObj.length; i++) {
            //If the display order is more than the total no of subscreens.
            ScreenObject screenObj = (ScreenObject) subTabsLabelsListObj[i];                
            String displayOrderStr = new Integer(screenObj.getDisplayOrder()).toString();
            Integer.parseInt(displayOrderStr);
            //Integer.parseInt(new Integer(screenObj.getDisplayOrder()).toString());
            if(screenObjectsMap.get(displayOrderStr) != null ) { //if screen object with same display order found already, throw exception 
              ScreenObject foundscreenObject  = (ScreenObject)screenObjectsMap.get(displayOrderStr);
              if (messageList.size() == 0)   {
                 messageList.add(foundscreenObject.getDisplayTitle());                  
              }
              messageList.add(screenObj.getDisplayTitle());
              messageMap.put(new Integer(screenObj.getDisplayOrder()).toString(),messageList);
            } else {
              screenObjectsMap.put(displayOrderStr, screenObj);  
            }
         }
        if (!messageMap.isEmpty()) {
            Object[] keys = messageMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        bundle.getString("DISPLAY_ORDER") + " " +  keys[i] + " " + bundle.getString("ALREADY_DEFINED"), bundle.getString("DISPLAY_ORDER") + " " + (String) keys[i] + " " + bundle.getString("ALREADY_DEFINED")));
                ArrayList tabNamesArray = (ArrayList) messageMap.get(keys[i]);
                for (int j = 0; j < tabNamesArray.size(); j++) {
                    String tabNames = (String) tabNamesArray.get(j);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            tabNames, tabNames));
                }
            }
            return null;
        }
          
        Object[] keys = screenObjectsMap.keySet().toArray();
        int[] intKeys = new int[keys.length];
        for (int k = 0; k < keys.length; k++) {
            String displayOrderStr = (String) keys[k];
            intKeys[k] = new Integer(displayOrderStr).intValue();
        }
        //sort the keys as integers by display order
        Arrays.sort(intKeys);
        //orderdedScreens
        for (int k = 0; k < intKeys.length; k++) {
            orderdedScreens[k] = (ScreenObject) screenObjectsMap.get(new Integer(intKeys[k]).toString());
        }
    } catch (Exception ex) {
        mLogger.error(mLocalizer.x("RPT040: Unable to get ordered screen objects : {0} ", ex.getMessage()), ex);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,   bundle.getString("CONFIG_ERROR") + " " , ex.getMessage()));
        return null;
    }
    return orderdedScreens;
} 
  
  
    public ArrayList getFieldGroupList(SearchScreenConfig objSearchScreenConfig) {
        ArrayList searchScreenFieldGroupArrayGrouped = new ArrayList();
        try {
            Iterator basicSearchFieldConfigsIterator = objSearchScreenConfig.getFieldConfigs().iterator();
            //Iterate the the FieldConfigGroup array list
            while (basicSearchFieldConfigsIterator.hasNext()) {
                //Build array of field config groups 
                FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                ArrayList fieldConfigsArray = basicSearchFieldGroup.getFieldConfigs();
                ArrayList fcArrayList = new ArrayList();
                for (int i = 0; i < fieldConfigsArray.size(); i++) {
                    FieldConfig objFieldConfig = (FieldConfig) fieldConfigsArray.get(i);
                    if ((objFieldConfig.getValueType() == 6 || objFieldConfig.getValueType() == 8) && objFieldConfig.getDisplayName().indexOf("To") != -1 || objFieldConfig.getDisplayName().indexOf("From") != -1) {
                        objFieldConfig.setRange(true);
                    }
                    if (objFieldConfig.isRange()) {
                        fcArrayList.add(objFieldConfig);
                        if (fcArrayList.size() == 2) {
                            searchScreenFieldGroupArrayGrouped.add(fcArrayList);
                            fcArrayList = new ArrayList();
                        }
                    } else {
                        fcArrayList.add(objFieldConfig);
                        searchScreenFieldGroupArrayGrouped.add(fcArrayList);
                        fcArrayList = new ArrayList();
                    }
                }
                //Fix for 6682971 - Ends

            }
        } catch (Exception e) {
            //Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
            mLogger.error(mLocalizer.x("SNC003: Failed to get SearchScreenField GroupArray:{0}", e.getMessage()));
        }
        return searchScreenFieldGroupArrayGrouped;
    }
  
}
