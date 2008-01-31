/*
 * PatientDetailsHandler.java
 *
 * Created on November 4, 2007, 6:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.common.PullDownListItem;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.ops.TransactionLog;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.faces.model.SelectItem;
import com.sun.mdm.index.edm.presentation.valueobjects.PatientDetails;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;


/**
 * @author Rajani
 */
public class PatientDetailsHandler {

    private HashMap updateableFeildsMap = new HashMap();    //for EDM driven feilds from the search page
    private ArrayList searchScreenConfigArray;
    private ArrayList searchResultsScreenConfigArray;
    private static final String SEARCH_PATIENT_DETAILS = "Record Details";
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler");

    // Create fields for non updateable fields as per screen config array
    private String EUID;
    private String SystemCode;
    private String LID;
    private String create_start_date;
    private String create_end_date;
    private String create_start_time;
    private String create_end_time;
    private String Status;
    private String searchType = "Advanced Person Lookup (Alpha)";
    //private String searchType = "Simple Person Lookup";
    //result array list for the out put
    private ArrayList resultArrayList = new ArrayList();
    private String[] euidCheckValues;
    private boolean euidCheckboolean;
    private static final String BASIC_SEARCH_RES = "basicSearchResults";
    private static final String ADV_SEARCH_RES = "advancedSearchResults";
    private static final String POT_DUP_SEARCH_RES = "potentialduplicates";
    private static final String VALIDATION_ERROR = "validationfailed";
    
    private PatientDetails[] patientDetailsVO = null;

    private String resolveType;
    /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;
  
    private ArrayList<SelectItem> possilbeSearchTypes = new ArrayList();
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    MasterControllerService masterControllerService = new MasterControllerService();
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());

    private String singleEUID;
    private String compareEUID;
     private String euid1 = "EUID 1";
     private String euid2 = "EUID 2";
     private String euid3 = "EUID 3"; 
     private String euid4 = "EUID 4";
       
    /** Creates a new instance of PatientDetailsHandler */
    public PatientDetailsHandler() {

    }

    /**
     * 
     * @return
     */
    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    /**
     * 
     * @param updateableFeildsMap
     */
    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    /**
     * 
     * @return
     */
    public ArrayList getSearchScreenConfigArray() {
            searchScreenConfigArray = screenObject.getSearchScreensConfig();
           return searchScreenConfigArray;
    }

    /**
     * 
     * @param searchScreenConfigArray
     */
    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    /**
     * 
     * @return
     */
    public ArrayList getSearchResultsScreenConfigArray() {
        ArrayList basicSearchFieldConfigs = null;
        ArrayList fieldConfigArrayList = null;
        try {
            ArrayList resultsScreenConfigArray = screenObject.getSearchResultsConfig();
            Iterator iteratorScreenConfig = resultsScreenConfigArray.iterator();

            while (iteratorScreenConfig.hasNext()) {
                SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();

                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                    //Build array of field configs from 
                    searchResultsScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
            }

        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        return searchResultsScreenConfigArray;
    }

    /**
     * 
     * @param searchResultsScreenConfigArray
     */
    public void setSearchResultsScreenConfigArray(ArrayList searchResultsScreenConfigArray) {
        this.searchResultsScreenConfigArray = searchResultsScreenConfigArray;
    }

    /**
     * 
     * @param event
     */
    public void setSearchTypeField(ActionEvent event) {
        String searchTypeField = (String) event.getComponent().getAttributes().get("searchType");
        this.setSearchType(searchTypeField);
    }

    /**
     * 
     * @param event
     */
    public void setPreviewEnterpriseObjectValues(ActionEvent event) {
        
        String fnameExpression = (String) event.getComponent().getAttributes().get("fnameExpression");
        Object fvalueVaueExpression = (Object) event.getComponent().getAttributes().get("fvalueVaueExpression");

        HashMap fieldValuesMerge = (HashMap)session.getAttribute("mergedEOMap");

        if (fieldValuesMerge != null) {
            fieldValuesMerge.put(fnameExpression, fvalueVaueExpression); //set the value for the preview section
            session.setAttribute("mergedEOMap", fieldValuesMerge);  //restore the session object again.
        }

    }

    /**
     * 
     * @param event
     */
    public void makeDifferentPersonAction(ActionEvent event) {
        try {
            //System.out.println("===== IN RRRRRRRRRRRRRR === > ");


            String sourceEUID = (String) event.getComponent().getAttributes().get("sourceEUID");
            String destinationEUID = (String) event.getComponent().getAttributes().get("destinationEUID");

            //System.out.println("===== resolveType" + this.getResolveType());
            //System.out.println("===== sourceEUID" + sourceEUID);
            //System.out.println("===== destinationEUID" + destinationEUID);



            //get potential duplicate ID
            String potDupId = masterControllerService.getPotentialDuplicateID(sourceEUID, destinationEUID);
            //resolve the potential duplicate as per resolve type
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(this.getResolveType()))?true:false;

            //System.out.println("===== resolveBoolean" + resolveBoolean);


            masterControllerService.setAsDifferentPerson(potDupId, resolveBoolean);
            
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     /**
     * 
     * @param event
     */
    public void unresolvePotentialDuplicateAction(ActionEvent event) {
        try {
            String sourceEUID = (String) event.getComponent().getAttributes().get("sourceEUID");
            String destinationEUID = (String) event.getComponent().getAttributes().get("destinationEUID");

            //get potential duplicate ID
            String potDupId = masterControllerService.getPotentialDuplicateID(sourceEUID, destinationEUID);
            
            //un resolve the potential duplicate as per resolve type
            masterControllerService.unresolvePotentialDuplicate(potDupId);
            
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * 
     * @param event
     */
    public void getEUIDDetails(ActionEvent event) {
        try {
            String euid = (String) event.getComponent().getAttributes().get("euid");
            
            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
            ArrayList newEOArrayList = new ArrayList();
            newEOArrayList.add(eo);
        
            session.setAttribute("enterpriseArrayList", newEOArrayList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }

    /**
     * 
     * @param event
     */
    public void keepEuidsAction(ActionEvent event) {
        String sourceEUID = (String) event.getComponent().getAttributes().get("sourceEUID");
        String destinationEUID = (String) event.getComponent().getAttributes().get("destinationEUID");

        //set both source and destination euids in the session
        session.setAttribute("sourceEUIDSessionObj", sourceEUID);
        session.setAttribute("destinationEUIDSessionObj", destinationEUID);
    }

    /**
     * 
     * @param event 
     * 
     */
    public void getMergedEnterpriseObject(ActionEvent event) {
        try {
            String sourceEUID = (String) event.getComponent().getAttributes().get("finalSourceEuid");
            String destinationEUID = (String) event.getComponent().getAttributes().get("finalDestinationEuid");

            EnterpriseObject sourceEnterpriseObject = masterControllerService.getEnterpriseObject(sourceEUID);//get source EO
            EnterpriseObject destinationEnterpriseObject = masterControllerService.getEnterpriseObject(destinationEUID);//get destination EO


            //get the merged result for the source and destination using master master controller.
            EnterpriseObject mergeResultEO = masterControllerService.getPostMergeEO(sourceEnterpriseObject, destinationEnterpriseObject);

            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            HashMap fieldValuesMergeMap = compareDuplicateManager.getEOFieldValues(mergeResultEO, screenObject, getSearchResultsScreenConfigArray().toArray());
            //Add the EUID to the hashmap
            fieldValuesMergeMap.put("EUID", mergeResultEO.getEUID());

            //set the merged Enterprise object in the session for displaying the preview in compare duplicates screen
            session.setAttribute("mergedEO", mergeResultEO);
            session.setAttribute("mergedEOMap", fieldValuesMergeMap);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public String singleEuidSearch () {
          NavigationHandler navigationHandler = new NavigationHandler();
          session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
          
        try {
            ArrayList newArrayList = new ArrayList();

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(this.getSingleEUID());
            //Throw exception if EO is found null.
            if (enterpriseObject == null) {
               session.removeAttribute("enterpriseArrayList");
               String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            } else {
                newArrayList.add(enterpriseObject);
                session.setAttribute("enterpriseArrayList", newArrayList);
            }
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
    
        
    public String compareEuidSearch () {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            if (this.getEuid4() != null && !"EUID 4".equalsIgnoreCase(this.getEuid4())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
    
    public String lookupEuid1 () {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
    public String lookupEuid2 () {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();

            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
    public String lookupEuid3 () {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();

            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
   public String lookupEuid4 () {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject(new Integer("1")));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();

            if (this.getEuid4() != null && !"EUID 4".equalsIgnoreCase(this.getEuid4())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(enterpriseObject);
                }
            }
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }
/**

@return String
@throws com.sun.mdm.index.master.ProcessingException 
@throws com.sun.mdm.index.master.UserException 
*/
public String performSubmit() throws ProcessingException, UserException {
        EDMValidation edmValidation = new EDMValidation();
        ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
        String errorMessage = null;
        Date date = null;
        
    try {
        int countMenuFields = 0;
        int countEmptyFields = 0;
        // get the array list as per the search
        ArrayList fieldConfigArrayList = this.getFieldConfigArrayListByTitle(this.searchType);
        Iterator fieldConfigArrayIter = fieldConfigArrayList.iterator();
        int totalFields = 0;
        Object[] fieldConfigArrayListObj = fieldConfigArrayList.toArray(); //build array of objects from arraylist
        int countFields = 0;
        //loop through array list of field config array lists
        for (int c = 0; c < fieldConfigArrayListObj.length; c++) {
            ArrayList fieldConfigList = (ArrayList) fieldConfigArrayListObj[c];
            Iterator fieldConfigListIter = fieldConfigList.iterator();  
            while (fieldConfigListIter.hasNext()) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigListIter.next();
                String feildValue = (String) this.getUpdateableFeildsMap().get(fieldConfig.getName());
                if ("MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue == null) {
                    countMenuFields++;
                } else if (!"MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue != null && feildValue.trim().length() == 0) {
                    countEmptyFields++;
                }
                          
                totalFields++;
            }
        }

        //Checking one of many condition here   
        if ((totalFields > 0 && countEmptyFields + countMenuFields == totalFields) && // all updateable fields are left blank
                (this.getEUID() == null || (this.getEUID() != null && this.getEUID().trim().length() == 0)) &&
                (this.getLID() == null || (this.getLID() != null && this.getLID().trim().length() == 0)) &&
                (this.getCreate_start_date() == null || (this.getCreate_start_date() != null && this.getCreate_start_date().trim().length() == 0)) &&
                (this.getCreate_start_time() == null || (this.getCreate_start_time() != null && this.getCreate_start_time().trim().length() == 0)) &&
                (this.getCreate_end_date() == null || (this.getCreate_end_date() != null && this.getCreate_end_date().trim().length() == 0)) &&
                (this.getCreate_end_time() == null || (this.getCreate_end_time() != null && this.getCreate_end_time().trim().length() == 0)) &&
                (this.getSystemCode() == null) &&
                (this.getStatus() == null)) {
            errorMessage =  bundle.getString("ERROR_one_of_many");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
            return VALIDATION_ERROR;
        }
         if (this.getEUID() != null && this.getEUID().length() > 0)    {
            String message = edmValidation.validateNumber(this.getEUID());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                
            }
        }   
      
         if (this.getLID() != null && this.getLID().length() > 0)    {
            String message = edmValidation.validateNumber(this.getLID());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID:: " + errorMessage, errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
                
            }
        }    
        
        // SambaG
        // Start here
        ArrayList sResultsConfigArrayList = screenObject.getSearchResultsConfig();

        EPathArrayList resultFields = new EPathArrayList();

        Iterator srcalIterator = sResultsConfigArrayList.iterator();
        String objectRef = null;
        while (srcalIterator.hasNext()) {
            SearchResultsConfig srConfig = (SearchResultsConfig) srcalIterator.next();
            ArrayList epaths = srConfig.getEPaths();
            Iterator ePathsIterator = epaths.iterator();
            while (ePathsIterator.hasNext()) {
                String ePathStr = (String) ePathsIterator.next();
                resultFields.add("Enterprise.SystemSBR." + ePathStr);
                if (objectRef == null) {
                    int index = ePathStr.indexOf(".");
                    objectRef = ePathStr.substring(0, index);
                }
            }
        }

        ArrayList searchScreenArray = this.getSearchScreenConfigArray();
        Iterator searchScreenArrayIter = searchScreenArray.iterator();
        String eoSearchOptionQueryBuilder = new String();
        while (searchScreenArrayIter.hasNext()) {
            SearchScreenConfig searchScreenConfig = (SearchScreenConfig) searchScreenArrayIter.next();
            if (searchScreenConfig.getScreenTitle().equalsIgnoreCase(this.searchType)) {
               //get the EO search option from the EDM.xml file here as per the search type
               eoSearchOptionQueryBuilder = searchScreenConfig.getOptions().getQueryBuilder();
            }
        }
        
        resultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");

        EOSearchOptions eoSearchOptions = new EOSearchOptions(eoSearchOptionQueryBuilder, resultFields);
        
        EOSearchCriteria eoSearchCriteria = new EOSearchCriteria();

        HashMap gSearchCriteria = new HashMap();
        HashMap gSearchCriteriaFromDOB = new HashMap();
        HashMap gSearchCriteriaToDOB = new HashMap();
        String feildValue; 
        //build the search criteria as per the user inputs from the form   
        for (int c = 0; c < fieldConfigArrayListObj.length; c++) {
            ArrayList fieldConfigList = (ArrayList) fieldConfigArrayListObj[c];
            Iterator fieldConfigListIter = fieldConfigList.iterator();
            while (fieldConfigListIter.hasNext()) {
                FieldConfig fieldConfig = (FieldConfig) fieldConfigListIter.next();
                 if (fieldConfig.getDisplayName().equalsIgnoreCase("DOB From") || fieldConfig.getDisplayName().equalsIgnoreCase("DOB To")) {
                    feildValue = (String) this.getUpdateableFeildsMap().get(fieldConfig.getDisplayName());
                 } else {
                   feildValue = (String) this.getUpdateableFeildsMap().get(fieldConfig.getName());
                 }                   
                
                //check if the field value is not null and the size is more than one charector.
                if (feildValue != null && feildValue.trim().length() > 0) {
                    if (fieldConfig.getDisplayName().equalsIgnoreCase("DOB From")) {
                        gSearchCriteriaFromDOB.put(fieldConfig.getFullFieldName(), feildValue);
                    } else if (fieldConfig.getDisplayName().equalsIgnoreCase("DOB To")) {
                        gSearchCriteriaToDOB.put(fieldConfig.getFullFieldName(), feildValue);
                    } else {
                        if(feildValue !=null && "SSN".equalsIgnoreCase(fieldConfig.getName()) ){
                            feildValue.replaceAll("-", "");
                        }
                        gSearchCriteria.put(fieldConfig.getFullFieldName(), feildValue);
                    }

                }
            }
        }
        //set the search criteria here
        //gSearchCriteria.put("Person.FirstName", "Mitch");
        //gSearchCriteria.put("Person.LastName", "keith");
        String objRef = objectRef;
        // following code is from buildObjectNodeFromSearchCriteria()
        //ObjectNode topNode = SimpleFactory.create(objRef);
        
        SystemObject sysobj = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteria);
        SystemObject sysobj2= null;
        SystemObject sysobj3= null;
        if(!gSearchCriteriaFromDOB.isEmpty()){        
            sysobj2= buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaFromDOB);
            eoSearchCriteria.setSystemObject2(sysobj2); // for dob from
        }
        if(!gSearchCriteriaToDOB.isEmpty()){        
            sysobj3= buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaToDOB);
            eoSearchCriteria.setSystemObject3(sysobj3); // for dob to
        }
        eoSearchCriteria.setSystemObject(sysobj);  // for all search attributes other than dob range
        
        
        EOSearchResultIterator eoSearchResultIteratorSession = masterControllerService.searchEnterpriseObject(eoSearchCriteria, eoSearchOptions);

        EOSearchResultIterator eoSearchResultIterator = masterControllerService.searchEnterpriseObject(eoSearchCriteria, eoSearchOptions);
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();
        EPathArrayList ePathArrayList = compareDuplicateManager.retrievePatientResultsFields(arlResultsConfig);

        while (eoSearchResultIterator.hasNext()) {
            EOSearchResultRecord eoSearchResultRecord = eoSearchResultIterator.next();
            ObjectNode objectNode = eoSearchResultRecord.getObject();
            HashMap fieldvalues = new HashMap();

            for (int m = 0; m < ePathArrayList.size(); m++) {
                EPath ePath = ePathArrayList.get(m);
                try {
                    Object value = EPathAPI.getFieldValue(ePath, objectNode);
                    fieldvalues.put(ePath.getName(), value);
                } catch (Exception npe) {
                // THIS SHOULD BE FIXED
                // npe.printStackTrace();
                }
            }
            fieldvalues.put("EUID", eoSearchResultRecord.getEUID());
            resultArrayList.add(fieldvalues);
        }
        setResultsSize(getPatientDetailsVO().length);
    // End here            
    // SambaG

    } catch (Exception exception) {
        exception.printStackTrace();
    }
         return this.SEARCH_PATIENT_DETAILS;
    }
    // getFieldConfigArrayListByTitle to get the field configs
    /**
     * 
     * @param screenTitle
     * @return
     */
    public ArrayList getFieldConfigArrayListByTitle(String screenTitle) {
        ArrayList basicSearchFieldConfigs = null;
        ArrayList fieldConfigArrayList = new ArrayList();
        try {
            ArrayList screenConfigArray = screenObject.getSearchScreensConfig();
            Iterator iteratorScreenConfig = screenConfigArray.iterator();

            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
                if (screenTitle.equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();
                        //Build array of field configs from 
                        ArrayList fieldConfigArrayListValue = basicSearchFieldGroup.getFieldConfigs();
                        fieldConfigArrayList.add(fieldConfigArrayListValue);//Build an Array list of field congfig group arraylist.
                    }
                }
            }

        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        return fieldConfigArrayList;
    }
    public String[] getStringEUIDs(String euids) {

        StringTokenizer stringTokenizer = new StringTokenizer(euids, ",");
        String[] euidsArray = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            euidsArray[i] = new String(stringTokenizer.nextElement().toString());
            i++;
        }
        return euidsArray;
    }

    /**
     * 
     * @param event
     */
    public void activateEO(ActionEvent event){
        try {
            EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
            masterControllerService.activateEnterpriseObject(enterpriseObject.getEUID());
            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());
            
            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(updatedEnterpriseObject);
            
            //Keep the updated SO in the session again
            session.setAttribute("enterpriseArrayList", updatedEOList);
            
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    
                       
   }
    /**
     * 
     * @param event
     */
    public void deactivateEO(ActionEvent event){
        try {

            EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");

            masterControllerService.deactivateEnterpriseObject(enterpriseObject.getEUID());

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());

            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(updatedEnterpriseObject);
            
            //Keep the updated SO in the session again
            session.setAttribute("enterpriseArrayList", updatedEOList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

            //Keep the updated SO in the session again
   }
    //get and setters for resultsArray
    public ArrayList getResultArrayList() {
        return resultArrayList;
    }

    public void setResultArrayList(ArrayList resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

    //getter and setter methods for the non updateable fields
    public String getEUID() {
        return EUID;
    }

    public void setEUID(String EUID) {
        this.EUID = EUID;
    }

    public String getSystemCode() {
        return SystemCode;
    }

    public void setSystemCode(String SystemCode) {
        this.SystemCode = SystemCode;
    }

    public String getLID() {
        return LID;
    }

    public void setLID(String LID) {
        this.LID = LID;
    }

    public String getCreate_start_date() {
        return create_start_date;
    }

    public void setCreate_start_date(String create_start_date) {
        this.create_start_date = create_start_date;
    }

    public String getCreate_end_date() {
        return create_end_date;
    }

    public void setCreate_end_date(String create_end_date) {
        this.create_end_date = create_end_date;
    }

    public String getCreate_start_time() {
        return create_start_time;
    }

    public void setCreate_start_time(String create_start_time) {
        this.create_start_time = create_start_time;
    }

    public String getCreate_end_time() {
        return create_end_time;
    }

    public void setCreate_end_time(String create_end_time) {
        this.create_end_time = create_end_time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String[] getEuidCheckValues() {
        return euidCheckValues;
    }

    public void setEuidCheckValues(String[] euidCheckValues) {
        this.euidCheckValues = euidCheckValues;
    }

    public boolean getEuidCheckboolean() {
        return euidCheckboolean;
    }

    public void setEuidCheckboolean(boolean euidCheckboolean) {
        this.euidCheckboolean = euidCheckboolean;
    }

    /**
     * 
     * @return
     */
    public ArrayList<SelectItem> getPossilbeSearchTypes() {
            ArrayList screenConfigArray = screenObject.getSearchScreensConfig();
            Iterator iteratorScreenConfigIter = screenConfigArray.iterator();
            SearchScreenConfig objSearchScreenConfig;
            ArrayList newArrayList = new ArrayList();
            int count = 1;
            while(iteratorScreenConfigIter.hasNext()) {
               objSearchScreenConfig = (SearchScreenConfig)iteratorScreenConfigIter.next() ;
               SelectItem  selectItem = new SelectItem();
               selectItem.setLabel(objSearchScreenConfig.getScreenTitle());
               selectItem.setValue(objSearchScreenConfig.getScreenTitle());
               newArrayList.add(selectItem);
               //possilbeSearchTypes.add(selectItem);
               count++;
            }
            possilbeSearchTypes = newArrayList;
         // returning the arraylist of searchTitles
        return possilbeSearchTypes;
    }

    /*
     * Method used to set the search type when from the select options
     *
     * Triggered when value is changed using ValueChangeListener.
     * @param event
     */
    public void changeSearchType(ValueChangeEvent event) {
        // get the event with the changed values
        String selectedSearcType = (String) event.getNewValue();
        
        this.setSearchType(selectedSearcType);
    }

    /**
     * 
     * @param possilbeSearchTypes
     */
    public void setPossilbeSearchTypes(ArrayList<SelectItem> possilbeSearchTypes) {
        this.possilbeSearchTypes = possilbeSearchTypes;
    }

    private SystemObject buildObjectNodeFromSearchCriteria(String objectRef, HashMap gSearchCriteria) throws ObjectException, ValidationException {
        ObjectNode topNode = SimpleFactory.create(objectRef);

        for (Iterator i = gSearchCriteria.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) gSearchCriteria.get(key);
            if ((value != null) && (value.trim().length() > 0)) {
                int index = key.indexOf(".");

                if (index > -1) {
                    String tmpRef = key.substring(0, index);
                    String fieldName = key.substring(index + 1);

                    if (tmpRef.equalsIgnoreCase(objectRef)) {
                        QwsUtil.setObjectNodeFieldValue(topNode, fieldName, value);
                    } else {
                        ArrayList childNodes = topNode.pGetChildren(tmpRef);
                        ObjectNode node = null;

                        if (childNodes == null) {
                            node = SimpleFactory.create(tmpRef);
                            topNode.addChild(node);
                        } else {
                            node = (ObjectNode) childNodes.get(0);
                        }
                        QwsUtil.setObjectNodeFieldValue(node, fieldName, value);
                    }
                }
            }
        }
        SystemObject sysobj = (SystemObject) SimpleFactory.create("SystemObject");
        sysobj.setValue("ChildType", objectRef);
        sysobj.setObject(topNode);
        return sysobj;
    }
    /**
     * 
     * @param event
     */
    public void mergePreviewEnterpriseObject(ActionEvent event) {
       
        String srcEUIDVaueExpression = (String) event.getComponent().getAttributes().get("mainEOVaueExpression");
        String destnEUIDVaueExpression = (String) event.getComponent().getAttributes().get("duplicateEOVaueExpression");
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        HashMap mergredHashMapVaueExpression = (HashMap) event.getComponent().getAttributes().get("mergedEOValueExpression");
        String sbrEUID =  (String) mergredHashMapVaueExpression.get("EUID");
        String destnId = (sbrEUID.equalsIgnoreCase(srcEUIDVaueExpression))?destnEUIDVaueExpression:srcEUIDVaueExpression;
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        SBR finalSBR = compareDuplicateManager.getEnterpriseObject(sbrEUID).getSBR();
        //Object[] resultsConfigFeilds  = getSearchResultsScreenConfigArray().toArray();
         SourceHandler sourceHandler = new SourceHandler();
         Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
           
        try {
            for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                FieldConfig fieldConfig = (FieldConfig) personConfigFeilds[ifc];

                Object strValue =  mergredHashMapVaueExpression.get(fieldConfig.getFullFieldName());
                String dateField;
                String fieldName = fieldConfig.getFullFieldName().substring(fieldConfig.getFullFieldName().indexOf(".")+1, fieldConfig.getFullFieldName().length());
                if (strValue != null) {
                    if (fieldConfig.getValueType() == 6) {
                        dateField = simpleDateFormatFields.format(mergredHashMapVaueExpression.get(fieldConfig.getFullFieldName()));
                        QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, dateField, finalSBR);
                    } else {
                        QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, strValue.toString(), finalSBR);
                    }
                } else {
                    QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, null, finalSBR);
                }
            }
            EnterpriseObject sourceEO = masterControllerService.getEnterpriseObject(sbrEUID);
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnId);
            
            MergeResult mergeResult  = masterControllerService.mergeEnterpriseObject(sourceEO, destinationEO);
            EnterpriseObject  finalMergredDestnEO = mergeResult.getDestinationEO();
            ArrayList finalMergredDestnEOArrayList = new ArrayList();
            finalMergredDestnEOArrayList.add(finalMergredDestnEO);
            session.removeAttribute("mergedEOMap");
            session.removeAttribute("enterpriseArrayList");
            session.removeAttribute("finalArrayList");
            
            session.setAttribute("enterpriseArrayList", finalMergredDestnEOArrayList);
            
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      /**
     * 
     * @param event
     */
    public void unmergeEnterpriseObject(ActionEvent event) throws ObjectException {
        
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpressionunmerge");
        try {
           MergeResult unMerge = masterControllerService.unMerge(enterpriseObject.getEUID());
          //httpRequest.setAttribute("unMerged", "unmerge");
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        
        }                     
          
    }
    
    public void viewMergedRecords(ActionEvent event) throws ObjectException{
        
        //HashMap unmergedHashMapValueExpression = (HashMap) event.getComponent().getAttributes().get("unmergedEOValueExpression");
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewmerge");
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpressionViewMerge");
        HashMap viewMergeRecordsHashMapValueExpression = (HashMap) event.getComponent().getAttributes().get("viewMergeEOValueExpression");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            SourceHandler sourceHandler = new SourceHandler();
            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            Object[] addressConfigFeilds = sourceHandler.getAddressFieldConfigs().toArray();
            Object[] aliasConfigFeilds = sourceHandler.getAliasFieldConfigs().toArray();
            Object[] phoneConfigFeilds = sourceHandler.getPhoneFieldConfigs().toArray();
            Object[] auxidConfigFeilds = sourceHandler.getAuxIdFieldConfigs().toArray();
            Object[] commentConfigFeilds = sourceHandler.getCommentFieldConfigs().toArray();

        try {
          EnterpriseObjectHistory viewMergehist = masterControllerService.viewMergeRecords(transactionNumber);
          ArrayList mergeEOList = new ArrayList();
          
          if(viewMergehist.getBeforeEO1() !=null){
              mergeEOList.add(viewMergehist.getBeforeEO1());
          }
          if(viewMergehist.getBeforeEO2() !=null){
              mergeEOList.add(viewMergehist.getBeforeEO2());
          }
//          if(viewMergehist.getAfterEO() !=null){
//              mergeEOList.add(viewMergehist.getAfterEO());
//          }
          if(viewMergehist.getAfterEO2() !=null){
              mergeEOList.add(viewMergehist.getAfterEO2());
          }

          
          httpRequest.setAttribute("mergeEOList",mergeEOList);  
          
         } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
       
        }                     
    }
    
     public void viewHistory(ActionEvent event) throws ObjectException{
        // session.removeAttribute("eoHistory");  
          
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
          
        try {
           ArrayList viewHistoryEOList = masterControllerService.viewHistory(enterpriseObject.getEUID());
          
           session.setAttribute("eoHistory"+enterpriseObject.getEUID(),viewHistoryEOList);  
          
         } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
       
        }                     
    }
   
      public void removeHistory(ActionEvent event) throws ObjectException{
           EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
           //System.out.println("Removing from session==:>"  + enterpriseObject.getEUID());
           session.removeAttribute("eoHistory"+enterpriseObject.getEUID());  
    }
    
     public void viewSource(ActionEvent event) throws ObjectException{
        
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
          
        try {
            EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(enterpriseObject.getEUID());
            Collection itemsSource = eoSource.getSystemObjects();
          
           httpRequest.setAttribute("itemsSource",itemsSource);  
          
         } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
       
        }                     
    }
   
      public void removeSource(ActionEvent event) throws ObjectException{
           httpRequest.removeAttribute("itemsSource");  
    }

   public PatientDetails[] getPatientDetailsVO() {
        patientDetailsVO = new PatientDetails[this.resultArrayList.size()];
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        int size = this.resultArrayList.size();

        HashMap values = new HashMap();
        for (int i = 0; i < size; i++) {
            patientDetailsVO[i] = new PatientDetails();
            values = (HashMap)resultArrayList.get(i);
            String dateField = simpleDateFormatFields.format(values.get("Person.DOB"));
            patientDetailsVO[i].setEuid((String) values.get("EUID"));
            patientDetailsVO[i].setFirstName((String) values.get("Person.FirstName"));
            patientDetailsVO[i].setLastName((String) values.get("Person.LastName"));
            patientDetailsVO[i].setDob(dateField);
            patientDetailsVO[i].setSsn((String) values.get("Person.SSN"));
            patientDetailsVO[i].setAddressLine1((String) values.get("Person.Address.AddressLine1"));
        }
        
        return patientDetailsVO;
    }

    public void setPatientDetailsVO(PatientDetails[] patientDetailsVO) {
        this.patientDetailsVO = patientDetailsVO;
    }

    public String getSingleEUID() {
        return singleEUID;
    }

    public void setSingleEUID(String singleEUID) {
        this.singleEUID = singleEUID;
    }


    public String getCompareEUID() {
        return compareEUID;
    }

    public void setCompareEUID(String compareEUID) {
        this.compareEUID = compareEUID;
    }

    public String getEuid1() {
        return euid1;
    }

    public void setEuid1(String euid1) {
        this.euid1 = euid1;
    }

    public String getEuid2() {
        return euid2;
    }

    public void setEuid2(String euid2) {
        this.euid2 = euid2;
    }

    public String getEuid3() {
        return euid3;
    }

    public void setEuid3(String euid3) {
        this.euid3 = euid3;
    }

    public String getEuid4() {
        return euid4;
    }

    public void setEuid4(String euid4) {
        this.euid4 = euid4;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }
}


   

