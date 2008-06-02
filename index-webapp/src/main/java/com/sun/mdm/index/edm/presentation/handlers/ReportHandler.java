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
 * Author : Sridhar
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
    private ArrayList<SelectItem> activityReportTypes = new ArrayList();

    //resource bundle definitin
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm", FacesContext.getCurrentInstance().getViewRoot().getLocale());

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
            //getSearchResultsScreenConfigArray();
            duplicateRecordsResults = getDeactivatedReport().deactivateReport();
            //setDeactivatedRecordsVO(deactivatedReport.deactivateReport());
            if (duplicateRecordsResults != null && duplicateRecordsResults.size() > 0) {
              setResultsSize(duplicateRecordsResults.size());
            }
            return duplicateRecordsResults;
        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT043: Unable to get deactivated report :{0} ", ex.getMessage()));
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT044: Unable to get deactivated report: {0} ", ex.getMessage()));
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("EPathException", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT027: Unable to get deactivated report:{0} ", ex.getMessage()));
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        }
    }

    public ArrayList mergeReport() {
        request.setAttribute("reportTabName", Merged_Transaction_Report_Label);
        ArrayList mergeResults = new ArrayList();

        try {
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
        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT028: Failed to get Merge report {0} ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured " + ex.getMessage(), ex.getMessage()));
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT029: Failed to get Merge report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT030: Failed to get Merge report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        }
        return mergeResults;
    }

    public ArrayList activitiesReport() {
        request.setAttribute("reportTabName", Activity_Report_Label);
        try {
            //Set paramaters for the search
            getActivityReport().setCreateStartTime((String) reportParameters.get("StartTime"));
            getActivityReport().setCreateEndTime((String) reportParameters.get("EndTime"));
            getActivityReport().setCreateStartDate((String) reportParameters.get("StartDate"));
            getActivityReport().setCreateEndDate((String) reportParameters.get("EndDate"));

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getActivityReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getActivityReport().setMaxResultsSize(getMaxReportSize(Activity_Report_Label));
            }
            getActivityReport().setPageSize(getRecordsPerPage(Activity_Report_Label));

            getActivityReport().setFrequency((String) reportParameters.get("activityType"));
            ArrayList results = getActivityReport().activityReport();

            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;
        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT031: Unable to get activity report {0} ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured " + ex.getMessage(), ex.getMessage()));
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT032: Unable to get activity report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT033: Unable to get activity report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
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

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getUnmergedRecordsHandler().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getUnmergedRecordsHandler().setMaxResultsSize(getMaxReportSize(Unmerged_Transaction_Report_Label));
            }
            getUnmergedRecordsHandler().setPageSize(getRecordsPerPage(Unmerged_Transaction_Report_Label));

            results = getUnmergedRecordsHandler().unmergeReport();
            
            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            
            return results;
        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT034: Failed to get unMerge report {0} ", ex.getMessage()), ex);
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT035: Failed to get unMerge report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT036: Failed to get unMerge report  {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
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

            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getUpdateReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getUpdateReport().setMaxResultsSize(getMaxReportSize(Updated_Record_Report_Label));
            }
            getUpdateReport().setPageSize(getRecordsPerPage(Updated_Record_Report_Label));

            results = getUpdateReport().updateReport();

            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;

        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT037: Unable to update the report  {0} ", ex.getMessage()), ex);
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT038: Unable to update the report  {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT039: Unable to update the report  {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
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
            
            if (reportParameters.get("MaxResultSize") != null && ((String) reportParameters.get("MaxResultSize")).trim().length() > 0) {
                getDuplicateReport().setMaxResultsSize(new Integer((String) reportParameters.get("MaxResultSize")));
            } else {
                getDuplicateReport().setMaxResultsSize(getMaxReportSize(Potential_Duplicate_Report_Label));
            }
            getDuplicateReport().setPageSize(getRecordsPerPage(Potential_Duplicate_Report_Label));

            ArrayList results = getDuplicateReport().duplicateReport();
            if(results != null && results.size() > 0) {
              setResultsSize(results.size()/2);
            }
            return results;
        } catch (ValidationException ex) {
            //Logger.getLogger(ReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // Sailaja to add code for this
            mLogger.error(mLocalizer.x("Error Occured ", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured " + ex.getMessage(), ex.getMessage()));
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT054: Unable to duplicate the report  {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT055: Unable to duplicate the report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        }
    }

    public ArrayList assumeMatchReport() {
        setReportType(Assumed_Matches_Report_Label);
        request.setAttribute("reportTabName", Assumed_Matches_Report_Label);
        try {

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

            ArrayList results = getAssumeMatchReport().assumeMatchReport();
            
            if(results != null && results.size() > 0) {
              setResultsSize(results.size());
            }
            return results;

        } catch (ValidationException ex) {
            mLogger.error(mLocalizer.x("RPT040: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
            return null;
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("RPT041: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("RPT042: Unable to get assumeMatch report {0} ", ex.getMessage()), ex);
            errorMessage = bundle.getString("Error_Occured");
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            return null;
        }
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





//            Iterator iteratorScreenConfig = resultsScreenObject.getSearchResultsConfig().iterator();
//
//            while (iteratorScreenConfig.hasNext()) {
//                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
//                //Set Max Results
//                setMaxResultsSize(objSearchScreenConfig.getMaxRecords());
//                //Set the page size
//                setPageSize(objSearchScreenConfig.getPageSize());
//                // Get an array list of field config groups
//                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
//                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
//                //Iterate the the FieldConfigGroup array list
//                while (basicSearchFieldConfigsIterator.hasNext()) {
//                    //Build array of field config groups 
//                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
//                    //Build array of field configs from 
//                    Object[] fieldConfigArrayList = basicSearchFieldGroup.getFieldConfigs().toArray();
//                    searchResultsScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
//                }
//            }
        } catch (Exception e) {
            mLogger.error(mLocalizer.x("RPT101: Unable to get search result config :{0} ", e.getMessage()), e);
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

/*    public ArrayList<SelectItem> getSelectOptions() {
        MasterControllerService masterControllerService = new MasterControllerService();
        String[][] systemCodes = masterControllerService.getSystemCodes();
        String[] pullDownListItems = systemCodes[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(masterControllerService.getSystemDescription(pullDownListItems[i]));
            selectItem.setValue(pullDownListItems[i]);
            newArrayList.add(selectItem);
        }
        selectOptions = newArrayList;
        return selectOptions;
    }
*/
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
            mLogger.error(mLocalizer.x("RPT102: Unable to get search results : {0} ", e.getMessage()), e);
        }
        return newArrayList;

    }
    private ArrayList searchScreenConfigArray;

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




}
