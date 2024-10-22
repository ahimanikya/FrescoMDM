/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.common.PullDownListItem;
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
import javax.faces.event.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
/**
 * Generic class to build the screen object
 */
public class ScreenConfiguration {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ScreenConfiguration");
    private static transient final Localizer mLocalizer = Localizer.get();

    private ArrayList searchScreenFieldGroupArray = new ArrayList();
    private ArrayList searchScreenFieldGroupArrayGrouped = new ArrayList();
    private HashMap searchScreenHashMap = new HashMap();

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
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
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
    //private static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ScreenConfiguration");

    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    /**
     * SearchScreenConfig
     */
     SearchScreenConfig searchScreenConfig = null;

    /**
     * Search type
     */
    private String searchType = new String("");
    
    SearchResultsConfig searchResultsConfig = null;

    /**
     * Search type
     */
    private String resultsType = new String("");
    
    
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
      private int lidMaskLength = 0;

      private String selectedSearchType = new String("");
    
      private String instructionLine  = new String("");

    /**
     * One of Group condition exists in the screen
     */
    private boolean oneOfGroupExists = false;
      
    /**
     * is Required condition exists in the screen
     */
    private boolean requiredExists = false;

    /**
     * Hashmap for descriptions and keys
     */
    private HashMap keyDescriptionsMap = new HashMap();
    
    /** Creates a new instance of ScreenConfiguration */
    public ScreenConfiguration() {
        if (screenObject!=null){ //fix for 6679172,6684209
        searchScreenConfig = (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() > 0) ? (SearchScreenConfig) screenObject.getSearchScreensConfig().toArray()[0] : null;
        
        searchType = (searchScreenConfig != null) ?  searchScreenConfig.getScreenTitle() : new String("");
        
        searchResultsConfig = (screenObject.getSearchResultsConfig() != null && screenObject.getSearchResultsConfig().size() > 0) ? (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0] : null;
        
        resultsType = (searchResultsConfig != null) ? searchResultsConfig.getMaxRecords() + " Records"+ " (" + searchResultsConfig.getSearchResultID() + ")" : new String("");
        
        selectedSearchType = (searchScreenConfig != null) ? searchScreenConfig.getScreenTitle() : new String("");
        
        instructionLine  = (searchScreenConfig != null) ? searchScreenConfig.getInstruction() : new String("");
        
        lidMaskLength = (getAllSystemCodes()[1][0] != null)? getAllSystemCodes()[1][0].length():0;
        
    }
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
            
            if (screenObject!=null && screenObject.getSearchScreensConfig()!= null){//fix for 6679172,6684209
            ArrayList screenConfigList = screenObject.getSearchScreensConfig();
            Iterator iteratorScreenConfig = screenConfigList.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                //07/24/08 Added the != null condition as a precaution to fix the bug#66
                if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() > 1 && this.searchType.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
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
                            screenConfigArray.add(object);
                        }
                    }//07/24/08 Added the != null condition as a precaution to fix the bug#66                
                } else if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() == 1){
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
                            screenConfigArray.add(object);
                        }
                    }
                }
             }}

        } catch (Exception e) {
            //Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
            mLogger.error(mLocalizer.x("SNC001: Failed to get screen Config array ", e.getMessage()),e);
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
            if(screenObject!=null && screenObject.getSearchResultsConfig()!= null ){//fix for 6679172,6684209
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
           
            }
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
        
        if(screenObject!=null && screenObject.getSearchResultsConfig()!= null){ //fix for 6679172,6684209
        ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
        SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) resultsScreenConfigArray.get(0);
        mRecords = objSearchScreenConfig.getMaxRecords();
        return mRecords;
        }
        else  {
           return mRecords;
        }
    }

    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    public int getPageSize() {
        int psize = 0;
        
        if(screenObject!=null && screenObject.getSearchResultsConfig()!= null){ //fix for 6679172,6684209
        ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
        SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) resultsScreenConfigArray.get(0);
        psize = objSearchScreenConfig.getPageSize();       
        return psize;
        }
        else {
            return psize;
        } 
          
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean checkOneOfManyCondition() {
        //Modified  on 7/24/2008        
        HashMap oneofCheckMap = new HashMap();
        oneofCheckMap.putAll(getUpdateableFeildsMap());
        oneofCheckMap.remove("lidmask"); //Do not consider lidmask for one-to-many check
        Object[] keySet = oneofCheckMap.keySet().toArray();
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
        if (count == keySet.length) {
            return true;
        } else {
            return false;
        }
    }
    
/**
 * Checks if the user has entered at least on of the the required fields in the group as
 * defined in the midm configuration file 
 * Modified Date:05/27/2008
 * @see isRequiredCondition
 * @return HashMap - List of all Groups along with the fields names in the group 
 */    
    public HashMap checkOneOfGroupCondition() {
        ArrayList fgGroups = getSearchScreenFieldGroupArray();
        
        HashMap errorMap = new HashMap();
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            boolean oneOfGroupValuesEntered = false;    
            String fieldGroup = "";
            boolean oneOfGroupValuesChecked = false;                
            ArrayList errorsMapList = new ArrayList();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                //if one of these is required
                if (basicFieldConfig.isOneOfTheseRequired()) {
                      fieldGroup = basicSearchFieldGroup.getDescription();
                     
                            errorsMapList.add(basicFieldConfig.getDisplayName());
                      if (getUpdateableFeildsMap().get(basicFieldConfig.getName()) != null) {
                        String value = ((String) getUpdateableFeildsMap().get(basicFieldConfig.getName())).trim();                        
                        oneOfGroupValuesChecked = true;
                        if (value.length() > 0) { //Value found for this key
                            
                            oneOfGroupValuesEntered = true;
                        }
                    }
                } //one of required condition
            } // Field config loop
            
            if (!oneOfGroupValuesEntered && oneOfGroupValuesChecked)   {
                errorMap.put(fieldGroup, errorsMapList);
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
    public ArrayList isRequiredCondition() {
        ArrayList errorsMapList = new ArrayList();
        ArrayList fgGroups = getSearchScreenFieldGroupArray();
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                //if one of these is required
                if (basicFieldConfig.isRequired()) {
                      if (getUpdateableFeildsMap().get(basicFieldConfig.getName()) != null) {
                        String value = ((String) getUpdateableFeildsMap().get(basicFieldConfig.getName())).trim();
                        if (value.length() == 0) { //Value found for this key
                            
                            errorsMapList.add(basicFieldConfig.getDisplayName());                            
                        }
                        
                    }
                } //isrequired condition
            } // Field config loop
         } // Field group loop
        return errorsMapList;
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
            mLogger.error(mLocalizer.x("SNC002: {0}:{1}",message, ex.getMessage())); 
           // Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, null, ex);
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
               
                if (startDateValue != null) {
                    if (startDateValue.trim().length() > 0) {
                        if (!"success".equalsIgnoreCase(edmValidation.validateDate(startDateValue))) {
                            messages.add(objectFieldConfig.getDisplayName() + ">>" + edmValidation.validateDate(startDateValue));
                           
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
               
                if (timeValue != null) {
                    if (timeValue.trim().length() > 0) {
                        if (!"success".equalsIgnoreCase(edmValidation.validateTime(timeValue))) {
                            messages.add(objectFieldConfig.getDisplayName() + ">>" + edmValidation.validateTime(timeValue));
                            
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
        if(screenObject!=null){//fix for 6679172,6684209
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

        }
        // returning the arraylist of searchTitles
        return possilbeSearchTypes;
    }

    /**
     * 
     * @return int
     */
    public int getPossilbeSearchTypesCount() {
        int searchCount= 0;
        //07/24/08 Added the != null condition as a precaution to fix the bug#66                
        if (screenObject!=null && screenObject.getSearchScreensConfig() != null ){
                 return screenObject.getSearchScreensConfig().size();
        }else
        {
            return searchCount;
        }
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
        String selectedSearchTypeVar = (String) event.getNewValue();
        setSearchType(selectedSearchTypeVar);
        setSelectedSearchType(selectedSearchTypeVar);
        
        searchScreenFieldGroupArray = new ArrayList();
        searchScreenHashMap  = new HashMap();
        searchScreenFieldGroupArrayGrouped = new ArrayList();
                        
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
        //set mask and its length
        setLidMask(lidMaskValue);
        setLidMaskLength(lidMaskValue.length());

    }

    private String getMaskedValue(String systemCodeSelected) {
        String lidMaskValue = new String();
        String[][] lidMaskingArray = masterControllerService.getSystemCodes();

        for (int i = 0; i < lidMaskingArray.length; i++) {
            String[] strings = lidMaskingArray[i];
            //Get the lid masking values here
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if (systemCodeSelected.equalsIgnoreCase(string)) {
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
        if (screenObject!=null && screenObject.getSearchResultsConfig()!=null){//fix for 6679172,6684209
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
        }
        return possilbeResultsTypes;
    }

    public void setPossilbeResultsTypes(ArrayList<SelectItem> possilbeResultsTypes) {
        this.possilbeResultsTypes = possilbeResultsTypes;
    }

    public int getPossilbeResultsTypesCount() {
        int resultsCount =0;
        if (screenObject!=null){
            return screenObject.getSearchResultsConfig().size();
        }
        else 
        {
            return resultsCount;
        }
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
        ArrayList configArray = null;
        if (screenObject.getSearchScreensConfig() != null && screenObject!=null){
             return screenObject.getSearchScreensConfig();
        }
        else
        {
            return configArray;
        }
       
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

    public ArrayList getSearchScreenFieldGroupArray() {
        searchScreenFieldGroupArray = new ArrayList();
        
        ArrayList basicSearchFieldConfigs;
        try {
            
            if(screenObject!=null && screenObject.getSearchScreensConfig()!= null ){ //fix for 6679172,6684209
            ArrayList screenConfigList = screenObject.getSearchScreensConfig();
            Iterator iteratorScreenConfig = screenConfigList.iterator();
            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() > 1 && this.searchType.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    //set the instruction line here
                    setInstructionLine(objSearchScreenConfig.getInstruction());
                    
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                        //Fix for 6682971 - Start
                        //Build array of field configs from 
                        ArrayList fieldConfigsArray = basicSearchFieldGroup.getFieldConfigs();
                        ArrayList fcArrayList = new ArrayList();
                        for (int i = 0; i < fieldConfigsArray.size(); i++) {
                            FieldConfig objFieldConfig = (FieldConfig) fieldConfigsArray.get(i);
                            if((objFieldConfig.getValueType() == 6 || objFieldConfig.getValueType() == 8 )&& objFieldConfig.getDisplayName().indexOf("To") != -1 || objFieldConfig.getDisplayName().indexOf("From") != -1) {
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
                        searchScreenHashMap.put(basicSearchFieldGroup.getDescription(), searchScreenFieldGroupArrayGrouped);
                        searchScreenFieldGroupArrayGrouped = new ArrayList();
                        //Fix for 6682971 - Ends

                        searchScreenFieldGroupArray.add(basicSearchFieldGroup);
                    }
                } else if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() == 1){
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();

                    //set the instruction line here
                    setInstructionLine(objSearchScreenConfig.getInstruction());

                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                        //Fix for 6682971 - Start
                        //Build array of field configs from 
                        ArrayList fieldConfigsArray = basicSearchFieldGroup.getFieldConfigs();
                        ArrayList fcArrayList = new ArrayList();
                        for (int i = 0; i < fieldConfigsArray.size(); i++) {
                            FieldConfig objFieldConfig = (FieldConfig) fieldConfigsArray.get(i);
                            if((objFieldConfig.getValueType() == 6 || objFieldConfig.getValueType() == 8 )&& objFieldConfig.getDisplayName().indexOf("To") != -1 || objFieldConfig.getDisplayName().indexOf("From") != -1) {
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
                        searchScreenHashMap.put(basicSearchFieldGroup.getDescription(), searchScreenFieldGroupArrayGrouped);
                        searchScreenFieldGroupArrayGrouped = new ArrayList();
                        //Fix for 6682971 - Ends

                        searchScreenFieldGroupArray.add(basicSearchFieldGroup);
                    }
                }
            }
            }

        } catch (Exception e) {
            //Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
            mLogger.error(mLocalizer.x("SNC003: Failed to get SearchScreenField GroupArray:{0}", e.getMessage())); 
        }
         
        
        return searchScreenFieldGroupArray;
    }

    public void setSearchScreenFieldGroupArray(ArrayList searchScreenFieldGroupArray) {
        this.searchScreenFieldGroupArray = searchScreenFieldGroupArray;
    }
    
/**

 * modified date:05/28/2008
 * This method checks if the screen has any One of Group condition
 * @return true if the oneOfGroup exists in the screen configuration
 */
    public boolean isOneOfGroupExists() {
         
        ArrayList fgGroups = getSearchScreenFieldGroupArray();
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

    public void setOneOfGroupExists(boolean oneOfGroupExists) {
        this.oneOfGroupExists = oneOfGroupExists;
    }

/**
 * modified date:05/28/2008
 * This method checks if the screen has required fields
 * @return true if the oneOfGroup exists in the screen configuration
 */
    public boolean isRequiredExists() {
        ArrayList fgGroups = getSearchScreenFieldGroupArray();
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

    public void setRequiredExists(boolean requiredExists) {
        this.requiredExists = requiredExists;
    }

    public HashMap getKeyDescriptionsMap() {
        ArrayList screenConfigArrayLocal = getScreenConfigArray();
        HashMap newHashMap = new HashMap();
        for (Iterator it = screenConfigArrayLocal.iterator(); it.hasNext();) {
            FieldConfig fieldConfig = (FieldConfig) it.next();
            if(!fieldConfig.isRange()) {
                newHashMap.put(fieldConfig.getName(), fieldConfig.getDisplayName());
            }
           }
        keyDescriptionsMap = newHashMap;
        return keyDescriptionsMap;
    }

    public void setKeyDescriptionsMap(HashMap keyDescriptionsMap) {
        this.keyDescriptionsMap = keyDescriptionsMap;
    }

    
    /**
     * Checks if the user has entered the values with proper input mask as 
     * defined in the midm configuration file 
     * Modified Date:07/18/2008
     * @see checkMasking
     * @return HashMap - Hashmap with values and input maskings
     */   
    public HashMap checkInputMasking() {
        HashMap valiadtions = new HashMap();
        
        ArrayList fgGroups = getSearchScreenFieldGroupArray();
        boolean maskValidation  = true;
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList fieldConfigs = basicSearchFieldGroup.getFieldConfigs();
            for (int fc = 0; fc < fieldConfigs.size(); fc++) {
                FieldConfig basicFieldConfig = (FieldConfig) fieldConfigs.get(fc);
                //if one of these is required
                       if (getUpdateableFeildsMap().get(basicFieldConfig.getName()) != null) {
                        String value = ((String) getUpdateableFeildsMap().get(basicFieldConfig.getName())).trim();
                        if (value.length() > 0 && basicFieldConfig.getInputMask() != null && basicFieldConfig.getInputMask().length() > 0)  { 
                              if((getUpdateableFeildsMap().get("LID") != null && getUpdateableFeildsMap().get("LID").toString().trim().length() > 0)) {
                                  maskValidation = checkMasking(value,(String)getUpdateableFeildsMap().get("lidmask"));
                              } else {
                                  maskValidation = checkMasking(value,basicFieldConfig.getInputMask());
                              }                              
                              if(!maskValidation) {
                                 valiadtions.put(basicFieldConfig.getDisplayName(),bundle.getString("lid_format_error_text") + " " +basicFieldConfig.getInputMask());								  
                              }
                              
                              
                        }
                        
                    }
             } // Field config loop
         } // Field group loop
        return valiadtions;
    }
    
    /**
     * Checks if the user has entered the values with proper input mask as 
     * defined in the midm configuration file 
     * Modified Date:07/18/2008
     * @see checkInputMasking
     * @return HashMap - Hashmap with values and input maskings
     */   
    public boolean checkMasking(String thisValue, String masking)  {
        Character c;
		if (masking == null || masking.length() == 0) {
			// no masking
			return true;
		}
        if ((thisValue != null && thisValue.trim().length() > 0 ) &&  (masking.length() != thisValue.length()) ) { 
			return false; //check length
		}
        try {
            if (thisValue != null && thisValue.trim().length() > 0) {
                for (int i = 0; i < masking.length(); i++) {  //Digit 

                    if (masking.charAt(i) == 'D') {
                        if (!Character.isDigit(thisValue.charAt(i))) {
                            return false;
                        }
                    } else if (masking.charAt(i) == 'L') {  //Char 
                        if (!Character.isLetter(thisValue.charAt(i))) {
                            return false;
                        }
                    } else if (masking.charAt(i) == 'A') {  //Char 
                        if (!Character.isLetter(thisValue.charAt(i)) && !Character.isDigit(thisValue.charAt(i))) {
                            return false;
                        }
                    } else {
                        if (masking.charAt(i) != thisValue.charAt(i)) {
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e)  {
                             mLogger.error(mLocalizer.x("SNC004: Failed to check masking :{0}", e.getMessage()),e);
            return false;
        }                
        return true;
    }

    public ArrayList getSearchScreenFieldGroupArrayGrouped() {
        return searchScreenFieldGroupArrayGrouped;
    }

    public void setSearchScreenFieldGroupArrayGrouped(ArrayList searchScreenFieldGroupArrayGrouped) {
        this.searchScreenFieldGroupArrayGrouped = searchScreenFieldGroupArrayGrouped;
    }

    public HashMap getSearchScreenHashMap() {
        return searchScreenHashMap;
    }

    public void setSearchScreenHashMap(HashMap searchScreenHashMap) {
        this.searchScreenHashMap = searchScreenHashMap;
    }


    public ArrayList getFieldGroupList(String selectedSearchType) {
        ArrayList searchScreenFieldGroup = new ArrayList();

        ArrayList basicSearchFieldConfigs;
        try {

            if (screenObject != null && screenObject.getSearchScreensConfig() != null) { //fix for 6679172,6684209
                ArrayList screenConfigList = screenObject.getSearchScreensConfig();
                Iterator iteratorScreenConfig = screenConfigList.iterator();
                while (iteratorScreenConfig.hasNext()) {
                    SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                    if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() > 1 && selectedSearchType.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                        // Get an array list of field config groups
                        basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                        //set the instruction line here
                        setInstructionLine(objSearchScreenConfig.getInstruction());

                        Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();

                        //Iterate the the FieldConfigGroup array list
                        while (basicSearchFieldConfigsIterator.hasNext()) {
                            //Build array of field config groups 
                            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                            //Fix for 6682971 - Start
                            //Build array of field configs from 
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
                            searchScreenHashMap.put(basicSearchFieldGroup.getDescription(), searchScreenFieldGroupArrayGrouped);
                            searchScreenFieldGroupArrayGrouped = new ArrayList();
                            //Fix for 6682971 - Ends

                            searchScreenFieldGroup.add(basicSearchFieldGroup);
                        }
                    } else if (screenObject.getSearchScreensConfig() != null && screenObject.getSearchScreensConfig().size() == 1) {
                        // Get an array list of field config groups
                        basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();

                        //set the instruction line here
                        setInstructionLine(objSearchScreenConfig.getInstruction());

                        Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                        //Iterate the the FieldConfigGroup array list
                        while (basicSearchFieldConfigsIterator.hasNext()) {
                            //Build array of field config groups 
                            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                            //Fix for 6682971 - Start
                            //Build array of field configs from 
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
                            searchScreenHashMap.put(basicSearchFieldGroup.getDescription(), searchScreenFieldGroupArrayGrouped);
                            searchScreenFieldGroupArrayGrouped = new ArrayList();
                            //Fix for 6682971 - Ends

                            searchScreenFieldGroup.add(basicSearchFieldGroup);
                        }
                    }
                }
            }

        } catch (Exception e) {
            //Logger.getLogger(ScreenConfiguration.class.getName()).log(Level.SEVERE, "Failed Get the Screen Config Array Object: ", e);
            mLogger.error(mLocalizer.x("SNC003: Failed to get SearchScreenField GroupArray:{0}", e.getMessage()));
        }


        return searchScreenFieldGroup;
    }

    
}
