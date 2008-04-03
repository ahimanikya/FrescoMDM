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
import java.util.Iterator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public class ReportHandler {
    
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
     * Search Maximum Reports in DuplicateReports & AssumeMatchReports
     */
    private String reportSize;

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
    DeactivatedReportHandler deactivatedReport = new DeactivatedReportHandler();
    /**
     * Instance of Assume Match Report Handler
     */
    AssumeMatchReportHandler assumeMatchReport = new AssumeMatchReportHandler();
    /**
     * Instance of Duplicate Report Handler
     */
    DuplicateReportHandler duplicateReport = new DuplicateReportHandler();
    /**
     * Instance of Update Report Handler
     */
    UpdateReportHandler updateReport = new UpdateReportHandler();
    /**
     * Instance of UnMerge Report Handler
     */
    UnmergedRecordsHandler unmergedRecordsHandler = new UnmergedRecordsHandler();
    /**
     * Instance of Merged Report Handler
     */
    MergeRecordHandler mergedRecordsHandler = new MergeRecordHandler();
    /**
     * Instance of Activity Report Handler
     */
    ActivityReportHandler activityReport = new ActivityReportHandler();    
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
    

    public String deactivatedReport() {      
        try {
            //Set paramaters for the search
            deactivatedReport.setCreateStartTime(getCreateStartTime());
            deactivatedReport.setCreateEndTime(getCreateEndTime());
            deactivatedReport.setCreateStartDate(getCreateStartDate());
            deactivatedReport.setCreateEndDate(getCreateEndDate());
            //getSearchResultsScreenConfigArray();
            setDeactivatedRecordsVO(deactivatedReport.deactivateReport());
            setResultsSize(getDeactivatedRecordsVO().length);
        } catch (ValidationException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "DeactivateReportResults";
    }

    public String mergeReport() {
        try {            
            //Set paramaters for the search
            mergedRecordsHandler.setCreateStartTime(getCreateStartTime());
            mergedRecordsHandler.setCreateEndTime(getCreateEndTime());
            mergedRecordsHandler.setCreateStartDate(getCreateStartDate());
            mergedRecordsHandler.setCreateEndDate(getCreateEndDate());            
            setMergedRecordsVO(mergedRecordsHandler.mergeReport());
            setResultsSize(getMergedRecordsVO().length);
        } catch (ValidationException ex) {            
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "Merge Report";
    }
    
    public String activitiesReport()    {
     try {
            //Set paramaters for the search
            activityReport.setCreateStartTime(getCreateStartTime());
            activityReport.setCreateEndTime(getCreateEndTime());
            activityReport.setCreateStartDate(getCreateStartDate());
            activityReport.setCreateEndDate(getCreateEndDate());
            activityReport.setFrequency(getFrequency());            
            setActivityRecordsVO(activityReport.activityReport());
            setResultsSize(getActivityRecordsVO().length);
        } catch (ValidationException ex) {
            Logger.getLogger(ActivityReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "ActivityReportResults";
    }
     
    public String unMergeReport() {
        try {            
            //Set paramaters for the search
            unmergedRecordsHandler.setCreateStartTime(getCreateStartTime());
            unmergedRecordsHandler.setCreateEndTime(getCreateEndTime());
            unmergedRecordsHandler.setCreateStartDate(getCreateStartDate());
            unmergedRecordsHandler.setCreateEndDate(getCreateEndDate());            
            setUnmergedRecordsVO(unmergedRecordsHandler.unmergeReport());
            setResultsSize(getUnmergedRecordsVO().length);
        } catch (ValidationException ex) {            
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "UnMerge Report";
    }
    
    public String updateReport()    { 
        try {            
            //Set paramaters for the search
            updateReport.setCreateStartTime(getCreateStartTime());
            updateReport.setCreateEndTime(getCreateEndTime());
            updateReport.setCreateStartDate(getCreateStartDate());
            updateReport.setCreateEndDate(getCreateEndDate());            
            setUpdateRecordsVO(updateReport.updateReport());
            
            if(updateRecordsVO != null) { 
              setResultsSize(updateRecordsVO.length);            
            } else {
                setResultsSize(0);            
            }
            
        } catch (ValidationException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "Update Report";
    }
    
    public String duplicateReport()   {
        try {            
            //Set paramaters for the search
            duplicateReport.setCreateStartTime(getCreateStartTime());
            duplicateReport.setCreateEndTime(getCreateEndTime());
            duplicateReport.setCreateStartDate(getCreateStartDate());
            duplicateReport.setCreateEndDate(getCreateEndDate());
            duplicateReport.setReportSize(getReportSize());
            duplicateReport.setReportFunction(getReportFunction());           
            setDuplicateRecordsVO(duplicateReport.duplicateReport());
            setResultsSize(getDuplicateRecordsVO().length);
        } catch (ValidationException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "Duplicate Report";
    }
    
    public String assumeMatchReport()    {
        try {
            //Set paramaters for the search
            assumeMatchReport.setCreateStartTime(getCreateStartTime());
            assumeMatchReport.setCreateEndTime(getCreateEndTime());
            assumeMatchReport.setCreateStartDate(getCreateStartDate());
            assumeMatchReport.setCreateEndDate(getCreateEndDate());
            assumeMatchReport.setReportSize(getReportSize());            
            setAssumematchesRecordsVO(assumeMatchReport.assumeMatchReport());            
            setResultsSize(getAssumematchesRecordsVO().length);
        } catch (ValidationException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "ReportFormError";
        } catch (EPathException ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        } catch (Exception ex) {
            Logger.getLogger(DeactivatedReportHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FacesContext.getCurrentInstance().addMessage("Processing Exception", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured, please try again", "Error Occured, please try again"));
            return ("ProcessingException");
        }
        return "Assumed Matches";
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
            System.out.println("SUB SCREEN " + resultsScreenObject + "subScreenObject" + subScreenObject + "getReportType()" + getReportType());
             ArrayList resultsScreenConfigArraySub = resultsScreenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = resultsScreenConfigArraySub.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
                ArrayList fcgList = objSearchScreenConfig.getFieldConfigs();
                for (int i = 0; i < fcgList.size(); i++) {
                    FieldConfigGroup objectFieldConfigGroup = (FieldConfigGroup) fcgList.get(i);
                    ArrayList fcList = objectFieldConfigGroup.getFieldConfigs();
                    for (int j = 0; j < fcList.size(); j++) {
                        FieldConfig objectFieldConfig = (FieldConfig)fcList.get(j);
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
            e.printStackTrace();
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
     public void setReportsTabName(ActionEvent event){
       String reportTabName = (String) event.getComponent().getAttributes().get("tabName");
       request.setAttribute("tabName", reportTabName);
   
      }

     public ArrayList<SelectItem> getSelectOptions() {
        MasterControllerService masterControllerService  = new MasterControllerService(); 
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
                        FieldConfig objectFieldConfig = (FieldConfig)fcList.get(j);
                        newArrayList.add(objectFieldConfig);
                  }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        //System.out.println("SUB SCREEN " + resultsScreenObject + "subScreenObject" + subScreenObject);
        ArrayList screenConfigArraySub = resultsScreenObject.getSearchScreensConfig();
        return screenConfigArraySub;
    }

    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    
  public ArrayList<SelectItem> getActivityReportTypes() {
        setReportType("Activity Report");        
        ArrayList searchScreenConfigArrayList  = getSearchScreenConfigArray();
        ArrayList newArrayList = new ArrayList();

        for (int i = 0; i < searchScreenConfigArrayList.size(); i++) {
            SearchScreenConfig searchScreenConfig =  (SearchScreenConfig) searchScreenConfigArrayList.get(i);
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
        int displayOrder = 0;
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
            //System.out.println("tabName  +==> " + tabName + "   ========> " + subScreenObject.getDisplayTitle() + "  displayOrder===> " + displayOrder);
        }
        
        return displayOrder;
        
    }

    
    
}
