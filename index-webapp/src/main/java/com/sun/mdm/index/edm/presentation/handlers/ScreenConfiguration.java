/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 * Generic class to build the screen object
 * @author Sridhar Narsingh, Rajani Kanth
 * www.ligaturesoftware.com
 */
public class ScreenConfiguration {

    private ArrayList searchScreenConfigArray = new ArrayList();
    /**
     * Screen Config Array 
     */
    private ArrayList screenConfigArray = new ArrayList();
    /**
     * Results Config Array
     */
    private ArrayList resultsConfigArray = new ArrayList();
    /**
     * maxRecords in the resutls
     */
    private int maxRecords;
    /**
     * Page size in the resutls
     */
    private int pageSize;
    /**
     * Possible Search types
     */
    private ArrayList<SelectItem> possilbeSearchTypes = new ArrayList();
    /**
     * possilbe search types count
     */
    private int possilbeSearchTypesCount;
    /**
     * Page size in the resutls
     */
    private ArrayList<SelectItem> possilbeResultsTypes = new ArrayList();
    /**
     * possilbe results types count
     */
    private int possilbeResultsTypesCount;
    /**
     * Error message string
     */
    String errormessage = "success";
    /**
     * ResourceBundle
     */
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    /**
     * Hashmap for recieving the input values from 
     */
    private HashMap updateableFeildsMap = new HashMap();
    
    private Set keySet;
    
    /**
     *Http session variable
     */
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    /**
     *Http request variable
     */
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    /**
     *EDMValidation Class instantiation
     */
    EDMValidation edmValidation = new EDMValidation();

    /**
     *EDMValidation Class instantiation
     */
    private String  enteredFieldValues  = new String();
    
    /**
     *MasterControllerService instantiation
     */
    MasterControllerService masterControllerService = new MasterControllerService();
    
    /**
     *Logger for logging 
     */
    private static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ScreenConfiguration");

    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    /**
     * SearchScreenConfig
     */
     SearchScreenConfig searchScreenConfig = (SearchScreenConfig) screenObject.getSearchScreensConfig().toArray()[0];

    /**
     * Search type
     */
    private String searchType = searchScreenConfig.getScreenTitle();
    
    SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

    /**
     * Search type
     */
    private String resultsType = searchResultsConfig.getMaxRecords() + " Records"+ " (" + searchResultsConfig.getSearchResultID() + ")";
    
    /**
     * all system codes
     */
    private String[][] allSystemCodes = masterControllerService.getSystemCodes();

    /**
     *LID mask value
     */
    private String lidMask = getAllSystemCodes()[1][0];

    /**
     *LID mask length
     */
    private int lidMaskLength = getAllSystemCodes()[1][0].length();

    private String selectedSearchType = searchScreenConfig.getScreenTitle();
    
    private String instructionLine  = searchScreenConfig.getInstruction();
    
    /** Creates a new instance of ScreenConfiguration */
    public ScreenConfiguration() {
    }

    /**
     * This method will return an array of search field configs as configured in edm.xml.
     * 
     * @return ArrayList
     */
    public ArrayList getScreenConfigArray() {
        screenConfigArray = new ArrayList();
        
        ArrayList basicSearchFieldConfigs;
        try {
            ////System.out.println("possilbeSearchTypes --> " + this.possilbeSearchTypes.size());
//          if(this.possilbeSearchTypes.size() > 1) {
//            setSearchType(searchScreenConfig.getScreenTitle());
//          }

            ArrayList screenConfigList = screenObject.getSearchScreensConfig();

            Iterator iteratorScreenConfig = screenConfigList.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();

                //System.out.println("screenObject.getSearchScreensConfig().size()"+screenObject.getSearchScreensConfig().size()+"this.searchType ==> : " + this.searchType+ "objSearchScreenConfig.getScreenTitle() --> " + objSearchScreenConfig.getScreenTitle());
                if (screenObject.getSearchScreensConfig().size() > 1 && this.searchType.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    //set the instruction line here
                    setInstructionLine(objSearchScreenConfig.getInstruction());
                    ////System.out.println("size() > 1  Basic --> " + basicSearchFieldConfigs);
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        //Build array of field configs from 
                        //screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                        ArrayList fieldConfigsList = basicSearchFieldGroup.getFieldConfigs();
                        for (int i = 0; i < fieldConfigsList.size(); i++) {
                            FieldConfig object = (FieldConfig) fieldConfigsList.get(i);
                            //System.out.println("Screen Config Object --> " + fieldConfigsList.get(i));
                            screenConfigArray.add(object);
                        }
                    }
                } else if (screenObject.getSearchScreensConfig().size() == 1){
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    //set the instruction line here
                    setInstructionLine(objSearchScreenConfig.getInstruction());

                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        //Build array of field configs from 
                        //screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                        ArrayList fieldConfigsList = basicSearchFieldGroup.getFieldConfigs();
                        for (int i = 0; i < fieldConfigsList.size(); i++) {
                            FieldConfig object = (FieldConfig) fieldConfigsList.get(i);
                            //////System.out.println("Screen Config Object --> " + fieldConfigsList.get(i));
                            screenConfigArray.add(object);
                        }
                    }
                }
            }

        } catch (Exception e) {
            Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
        }
        return screenConfigArray;
    }

    /**
     * 
     * @param screenConfigArray
     */
    public void setScreenConfigArray(ArrayList screenConfigArray) {
        this.screenConfigArray = screenConfigArray;
    }

    /**
     * This method will return an array of resuls field configs as configured in edm.xml.
     * 
     * @return ArrayList
     */
    public ArrayList getResultsConfigArray() {
//        ArrayList basicSearchFieldConfigs = null;
//        try {
//            ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
//            Iterator iteratorScreenConfig = resultsScreenConfigArray.iterator();
//
//            while (iteratorScreenConfig.hasNext()) {
//                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();
//                            System.out.println("possilbeResultsTypesCount ===> " + objSearchScreenConfig);
//                if (possilbeResultsTypesCount > 1 && this.resultsType.equalsIgnoreCase(objSearchScreenConfig.getMaxRecords()+" Records"+ " (" + objSearchScreenConfig.getSearchResultID() + ")" ) ) {
//                    setMaxRecords(objSearchScreenConfig.getMaxRecords());
//                    setPageSize(objSearchScreenConfig.getPageSize());
//
//                    // Get an array list of field config groups
//                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
//                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
//                    //Iterate the the FieldConfigGroup array list
//                    while (basicSearchFieldConfigsIterator.hasNext()) {
//                        //Build array of field config groups 
//                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
//                        //Build array of field configs from 
//                        //screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
//                        ArrayList fieldConfigsList = basicSearchFieldGroup.getFieldConfigs();
//                        for (int i = 0; i < fieldConfigsList.size(); i++) {
//                            FieldConfig object = (FieldConfig) fieldConfigsList.get(i);
//                            resultsConfigArray.add(object);
//                        }
//                    }
//                } else if (screenObject.getSearchResultsConfig().size() == 1){
//
//                    setMaxRecords(objSearchScreenConfig.getMaxRecords());
//                    setPageSize(objSearchScreenConfig.getPageSize());
//
//                    // Get an array list of field config groups
//                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
//                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
//                    //Iterate the the FieldConfigGroup array list
//                    while (basicSearchFieldConfigsIterator.hasNext()) {
//                        //Build array of field config groups 
//                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
//                        //Build array of field configs from 
//                        //screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
//                        ArrayList fieldConfigsList = basicSearchFieldGroup.getFieldConfigs();
//                        for (int i = 0; i < fieldConfigsList.size(); i++) {
//                            FieldConfig object = (FieldConfig) fieldConfigsList.get(i);
//                            resultsConfigArray.add(object);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
//        }
            ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = resultsScreenConfigArray.iterator();
            ArrayList newArrayList = new ArrayList();
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
            this.resultsConfigArray = newArrayList;
        return resultsConfigArray;
    }

    /**
     * 
     * @param resultsConfigArray
     */
    public void setResultsConfigArray(ArrayList resultsConfigArray) {
        this.resultsConfigArray = resultsConfigArray;
    }

     /**
     * This method will return a hashmap which holds the values entered by the user on the form.
     * 
     * @return HashMap
     */

    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    public int getMaxRecords() {
        int mRecords = 0;
        ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
        SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) resultsScreenConfigArray.get(0);
        mRecords = objSearchScreenConfig.getMaxRecords();
      
        return mRecords;
    }

    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    public int getPageSize() {
        int psize = 0;
        ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
        SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) resultsScreenConfigArray.get(0);
        psize = objSearchScreenConfig.getPageSize();
       
        return psize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean checkOneOfManyCondition() {
        Object[] keySet = getUpdateableFeildsMap().keySet().toArray();
        //System.out.println("==> " + getUpdateableFeildsMap() +"==> " + getUpdateableFeildsMap().keySet());
        int count = 0;
        for (int i = 0; i < keySet.length; i++) {
            String key = (String) keySet[i];
            if (getUpdateableFeildsMap().get(key) == null) {
                count++;
            } else if (getUpdateableFeildsMap().get(key) != null) {
                String value = ((String) getUpdateableFeildsMap().get(key)).trim();
                if (value.length() == 0) {
                    count++;
                }
            }
        }
        //System.out.println("==> " + count +"==> " + keySet.length);
        if (count == keySet.length) {
            return true;
        } else {
            return false;
        }

    }

    public String checkFromToDateRange() {
        String message = "success";
        Date todate = null;
        Date fromdate = null;
        try {
            if (getUpdateableFeildsMap().get("StartDate") != null && getUpdateableFeildsMap().get("StartDate").toString().trim().length() > 0) {
                String startTime = (String) getUpdateableFeildsMap().get("StartTime");
                String searchFromDate = (String) getUpdateableFeildsMap().get("StartDate");
                //append the time aling with date
                if (startTime != null && startTime.trim().length() > 0) {
                    searchFromDate = searchFromDate + " " + startTime;
                } else {
                    searchFromDate = searchFromDate + " 00:00:00";
                }
                fromdate = DateUtil.string2Date(searchFromDate);
            }

            if (getUpdateableFeildsMap().get("EndDate") != null && getUpdateableFeildsMap().get("EndDate").toString().trim().length() > 0) {
                String endTime = (String) getUpdateableFeildsMap().get("EndTime");
                String searchEndDate = (String) getUpdateableFeildsMap().get("EndDate");
                //append the time aling with date
                if (endTime != null && endTime.trim().length() > 0) {
                    searchEndDate = searchEndDate + " " + endTime;
                } else {
                    searchEndDate = searchEndDate + " 23:59:59";
                }
                todate = DateUtil.string2Date(searchEndDate);
            }

            //Check FromDate-ToDate Range


            long startDate = fromdate.getTime();
            long endDate = todate.getTime();
            if (endDate < startDate) {
                message = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
            }
        } catch (ValidationException ex) {
            Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public ArrayList validateDateFields() {
        ArrayList messages = new ArrayList();
        String startDateValue = new String();
        Object[] fcObjects = getScreenConfigArray().toArray();
        for (int i = 0; i < fcObjects.length; i++) {
            FieldConfig objectFieldConfig = (FieldConfig) fcObjects[i];

            //validate all date fields here
            if (objectFieldConfig.getValueType() == 6) {
                //Form Validation of  Start Date
                if (objectFieldConfig.isRange()) {
                    startDateValue = (String) getUpdateableFeildsMap().get(objectFieldConfig.getDisplayName());
                } else {
                    startDateValue = (String) getUpdateableFeildsMap().get(objectFieldConfig.getName());
                }
                //System.out.println("startDateValue ===> : " + startDateValue + objectFieldConfig.getDisplayName() + objectFieldConfig.isRange());
                if (startDateValue != null) {
                    if (startDateValue.trim().length() > 0) {
                        if (!"success".equalsIgnoreCase(edmValidation.validateDate(startDateValue))) {
                            messages.add(objectFieldConfig.getDisplayName() + ":" + edmValidation.validateDate(startDateValue));
                            //System.out.println("Adding fields and message" + messages);
                        }
                    }
                }

            }
        }
        return messages;
    }

    /**
     * 
     * @return
     */
    public ArrayList validateTimeFields() {
        ArrayList messages = new ArrayList();
        String timeValue = new String();
        Object[] fcObjects = getScreenConfigArray().toArray();
        for (int i = 0; i < fcObjects.length; i++) {
            FieldConfig objectFieldConfig = (FieldConfig) fcObjects[i];

            //validate all date fields here
            if (objectFieldConfig.getValueType() == 8) {
                //Form Validation of  Start Date
                if (objectFieldConfig.isRange()) {
                    timeValue = (String) getUpdateableFeildsMap().get(objectFieldConfig.getDisplayName());
                } else {
                    timeValue = (String) getUpdateableFeildsMap().get(objectFieldConfig.getName());
                }
                //System.out.println("startDateValue ===> : " + startDateValue + objectFieldConfig.getDisplayName() + objectFieldConfig.isRange());
                if (timeValue != null) {
                    if (timeValue.trim().length() > 0) {
                        if (!"success".equalsIgnoreCase(edmValidation.validateTime(timeValue))) {
                            messages.add(objectFieldConfig.getDisplayName() + ":" + edmValidation.validateTime(timeValue));
                            //System.out.println("Adding fields and message" + messages);
                        }
                    }
                }

            }
        }
        return messages;
    }
    
    public String validateStartDate() {
        String message = "success";
        //Form Validation of  Start Date
        if (getUpdateableFeildsMap().get("StartDate") != null) {
            String startDateValue = (String) getUpdateableFeildsMap().get("StartDate");
            if (startDateValue.trim().length() > 0) {
                message = edmValidation.validateDate(startDateValue);
            }
        }
        return message;
    }

   
    public String validateStartTime() {
        String message = "success";
        //Form Validation of  Start Date
        if (getUpdateableFeildsMap().get("StartTime") != null) {
            String startTimeValue = (String) getUpdateableFeildsMap().get("StartTime");
            if (startTimeValue.trim().length() > 0) {
                message = edmValidation.validateTime(startTimeValue);
            }
        }
        return message;

    }

    public String validateEndDate() {
        String message = "success";
        //Form Validation of  Start Date
        if (getUpdateableFeildsMap().get("EndDate") != null) {
            String endDateValue = (String) getUpdateableFeildsMap().get("EndDate");
            if (endDateValue.trim().length() > 0) {
                message = edmValidation.validateDate(endDateValue);
            }
        }
        return message;

    }

    public String validateEndTime() {
        String message = "success";
        //Form Validation of  Start Date
        if (getUpdateableFeildsMap().get("EndTime") != null) {
            String endTimeValue = (String) getUpdateableFeildsMap().get("EndTime");
            if (endTimeValue.trim().length() > 0) {
                message = edmValidation.validateTime(endTimeValue);
            }
        }
        return message;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<SelectItem> getPossilbeSearchTypes() {
        ArrayList screenConfigPossibleTypes = screenObject.getSearchScreensConfig();
        Iterator iteratorScreenConfigIter = screenConfigPossibleTypes.iterator();
        SearchScreenConfig objSearchScreenConfig;
        ArrayList newArrayList = new ArrayList();
        int count = 1;
        while (iteratorScreenConfigIter.hasNext()) {
            objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfigIter.next();
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(objSearchScreenConfig.getScreenTitle());
            selectItem.setValue(objSearchScreenConfig.getScreenTitle());
            newArrayList.add(selectItem);
            count++;
        }
        possilbeSearchTypes = newArrayList;

        
        // returning the arraylist of searchTitles
        return possilbeSearchTypes;
    }

    /**
     * 
     * @return int
     */
    public int getPossilbeSearchTypesCount() {
        return screenObject.getSearchScreensConfig().size();
    }

    /**
     * 
     * @param possilbeSearchTypesCount
     */
    public void setPossilbeSearchTypesCount(int possilbeSearchTypesCount) {
        this.possilbeSearchTypesCount = possilbeSearchTypesCount;
    }

    public void clearAllFeilds() {
        Object[] keySet = getUpdateableFeildsMap().keySet().toArray();
        for (int i = 0; i < keySet.length; i++) {
            String key = (String) keySet[i];
            getUpdateableFeildsMap().remove(key);
        }

    }
    /*
     * Method used to set the search type when from the select options
     *
     * Triggered when value is changed using ValueChangeListener.
     * @param event
     */
    public void changeSearchType(ValueChangeEvent event) {
        // get the event with the changed values
        String selectedSearchType = (String) event.getNewValue();
        //this.searchType = selectedSearchType;
        setSearchType(selectedSearchType);
        ArrayList screenConfigList = screenObject.getSearchScreensConfig();
        HashMap newHashMap = new HashMap();
        
        Iterator iteratorScreenConfig = screenConfigList.iterator();
        while (iteratorScreenConfig.hasNext()) {
            SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
            //System.out.println("screenObject.getSearchScreensConfig().size()"+screenObject.getSearchScreensConfig().size()+"this.searchType ==> : " + this.searchType+ "objSearchScreenConfig.getScreenTitle() --> " + objSearchScreenConfig.getScreenTitle());
            if (this.searchType.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                
                //set the instruction line here
                setInstructionLine(objSearchScreenConfig.getInstruction());

                // Get an array list of field config groups
                ArrayList basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                ////System.out.println("size() > 1  Basic --> " + basicSearchFieldConfigs);
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                    //Build array of field configs from 
                    //screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                    ArrayList fieldConfigsList = basicSearchFieldGroup.getFieldConfigs();
                    for (int i = 0; i < fieldConfigsList.size(); i++) {
                        FieldConfig object = (FieldConfig) fieldConfigsList.get(i);
                        if (object.isRange()) {
                            newHashMap.put(object.getDisplayName(), null);
                        } else {
                            newHashMap.put(object.getName(), null);
                        }
                    }
                }
            }
        }
        this.setUpdateableFeildsMap(newHashMap);        
        
    }

    public String getSearchType() {
        return this.searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /*
     * Method used to set the lid masking when user picks the system code from the select options.
     *
     * Triggered when value is changed using ValueChangeListener.
     * @param event
     */
    public void setLidMaskValue(ValueChangeEvent event) {
        session.setAttribute("tabName", "Merge");
        // get the event with the changed values
        String systemCodeSelected = (String) event.getNewValue();
        String lidMaskValue = getMaskedValue(systemCodeSelected);
        ////System.out.println("==>: returned value " + lidMaskValue);
        //set mask and its length
        setLidMask(lidMaskValue);
        setLidMaskLength(lidMaskValue.length());

    ////System.out.println("this.getLidMask ==>: " + this.getLidMask() + "Length ==> : " + this.getLidMaskLength() );

    }

    private String getMaskedValue(String systemCodeSelected) {
        String lidMaskValue = new String();
        //////System.out.println("systemCodeSelected ==> : " +  systemCodeSelected);
        String[][] lidMaskingArray = masterControllerService.getSystemCodes();

        for (int i = 0; i < lidMaskingArray.length; i++) {
            String[] strings = lidMaskingArray[i];
            //////System.out.println("Outer Loop ==> : " +  strings);
            //Get the lid masking values here
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if (systemCodeSelected.equalsIgnoreCase(string)) {
                    ////System.out.println( systemCodeSelected + "<=== [" +i + "]"  + "[" +j + "]" + "Inner Loop ==> : ");
                    lidMaskValue = lidMaskingArray[i + 1][j];
                }

            }
        }

        return lidMaskValue;
    }

    public int getLidMaskLength() {
        return lidMaskLength;
    }

    public void setLidMaskLength(int lidMaskLength) {
        this.lidMaskLength = lidMaskLength;
    }

    public String getLidMask() {
        return lidMask;
    }

    public void setLidMask(String lidMask) {
        this.lidMask = lidMask;
    }

    public ArrayList<SelectItem> getPossilbeResultsTypes() {
        ArrayList searchResultsConfigPossibleTypes = screenObject.getSearchResultsConfig();
        Iterator iteratorSearchResultsConfigIter = searchResultsConfigPossibleTypes.iterator();
        SearchResultsConfig objSearchResultsConfig;
        ArrayList newArrayList = new ArrayList();
        int count = 1;
        while (iteratorSearchResultsConfigIter.hasNext()) {
            objSearchResultsConfig = (SearchResultsConfig) iteratorSearchResultsConfigIter.next();
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(objSearchResultsConfig.getMaxRecords() + " Records" + " (" + objSearchResultsConfig.getSearchResultID() + ")");
            selectItem.setValue(objSearchResultsConfig.getMaxRecords() + " Records" + " (" + objSearchResultsConfig.getSearchResultID() + ")");
            //set the default search type as per the screen order
            newArrayList.add(selectItem);
            count++;
        }
        possilbeResultsTypes = newArrayList;

        //set possilble results types count
        setPossilbeResultsTypesCount(possilbeResultsTypes.size());
  
        return possilbeResultsTypes;
    }

    public void setPossilbeResultsTypes(ArrayList<SelectItem> possilbeResultsTypes) {
        this.possilbeResultsTypes = possilbeResultsTypes;
    }

    public int getPossilbeResultsTypesCount() {
        return screenObject.getSearchResultsConfig().size();
    }

    public void setPossilbeResultsTypesCount(int possilbeResultsTypesCount) {
        this.possilbeResultsTypesCount = possilbeResultsTypesCount;
    }

    public String getResultsType() {
        return resultsType;
    }

    public void setResultsType(String resultsType) {
        this.resultsType = resultsType;
    }

    /*
     * Method used to set the results type when from the select options
     *
     * Triggered when value is changed using ValueChangeListener.
     * @param event
     */
    public void changeResultsType(ValueChangeEvent event) {
        // get the event with the changed values
        String selectedResultsType = (String) event.getNewValue();

        this.resultsType = selectedResultsType;
    }

    public Set getKeySet() {
        return keySet;
    }

    public void setKeySet(Set keySet) {
        this.keySet = keySet;
    }
    
        /**
     * 
     * @return
     */
    public ArrayList getSearchScreenConfigArray() {
        return screenObject.getSearchScreensConfig();
    }

    /**
     * 
     * @param searchScreenConfigArray
     */
    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    public String getEnteredFieldValues() {
        return enteredFieldValues;
    }

    public void setEnteredFieldValues(String enteredFieldValues) {
        this.enteredFieldValues = enteredFieldValues;
    }

    public String getSelectedSearchType() {
        return selectedSearchType;
    }

    public void setSelectedSearchType(String selectedSearchType) {
        this.selectedSearchType = selectedSearchType;
    }

    public String[][] getAllSystemCodes() {
        return allSystemCodes;
    }

    public void setAllSystemCodes(String[][] allSystemCodes) {
        this.allSystemCodes = allSystemCodes;
    }

    public String getInstructionLine() {
        return instructionLine;
    }

    public void setInstructionLine(String instructionLine) {
        this.instructionLine = instructionLine;
    }

}
