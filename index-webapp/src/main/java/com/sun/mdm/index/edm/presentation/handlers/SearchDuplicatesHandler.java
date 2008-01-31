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
 * SearchDuplicatesHandler.java 
 * Created on September 12, 2007, 11:58 AM
 * Author : Pratibha, RajaniKanth
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import com.sun.mdm.index.page.PageException;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.faces.event.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.sun.mdm.index.edm.presentation.validations.HandlerException;
import com.sun.mdm.index.edm.presentation.validations.EDMValidation;
import javax.servlet.http.HttpServletRequest;


public class SearchDuplicatesHandler {
    private HashMap updateableFeildsMap =  new HashMap();    
    private HashMap actionMap =  new HashMap();    
    private ArrayList nonUpdateableFieldsArray = new ArrayList();    
    private ArrayList headerScreenObjectsArray = new ArrayList();
    private ArrayList screenConfigArray;
    private ArrayList resultsConfigArray;
    
    private  static final String SEARCH_DUPLICATES="Search Duplicates";
    
     /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;

   
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler");

    // Create fields for non updateable fields as per screen config array
    private String EUID;
    private String SystemCode;
    private String LID;
    private String create_start_date;
    private String create_end_date;
    private String create_start_time;
    private String create_end_time;
    private String Status;
    
    private String searchType = "Advanced Search";

    private static final String  BASIC_SEARCH_RES     = "basicSearchResults";
    private static final String  ADV_SEARCH_RES       = "advancedSearchResults";
    private static final String  POT_DUP_SEARCH_RES   = "potentialduplicates";
    private static final String  VALIDATION_ERROR     = "validationfailed";

    private PotentialDuplicateSummary comparePotentialDuplicateSummary;
    private ArrayList potentialDuplicateSummaryArray;
    //Resource bundle
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
    //Http session variable
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    //HTTP request variable
    HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    
    /** Creates a new instance of SearchDuplicatesHandler */
    public SearchDuplicatesHandler() {
    }


    public String performSubmit() throws ProcessingException, UserException, ValidationException,HandlerException{
        EDMValidation edmValidation = new EDMValidation();
        session.removeAttribute("finalArrayList");                
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
        String errorMessage = null;
        Date date = null;
        ArrayList fieldConfigArrayList  = getFieldConfigArrayListByTitle(this.searchType);
        Iterator fieldConfigArrayIter =  fieldConfigArrayList.iterator();
        int totalFields = fieldConfigArrayList.size();
        int countMenuFields = 0;
        int countEmptyFields = 0;
        while(fieldConfigArrayIter.hasNext())  {
             FieldConfig  fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
             String feildValue = (String) getUpdateableFeildsMap().get(fieldConfig.getName());                          
             if("MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue == null)  {
               countMenuFields++;     
             } else if(!"MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue != null && feildValue.trim().length() == 0)  { 
               countEmptyFields++;       
             } else if (!"MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue == null )  { 
                 countEmptyFields++;
             }
        }
        
        //Checking one of many condition here   
        if( (totalFields > 0 && countEmptyFields+countMenuFields == totalFields)   && // all updateable fields are left blank
           (getEUID() == null || (getEUID()  != null && getEUID().trim().length() == 0))  &&
           (getLID()  == null || (getLID()  != null && getLID().trim().length() == 0))  &&
           (getCreate_start_date()  == null || (getCreate_start_date()  != null && getCreate_start_date().trim().length() == 0))  &&
           (getCreate_start_time()  == null || (getCreate_start_time()  != null && getCreate_start_time().trim().length() == 0))  &&
           (getCreate_end_date()  == null  || (getCreate_end_date()  != null && getCreate_end_date().trim().length() == 0))  &&
           (getCreate_end_time()  == null || (getCreate_end_time()  != null && getCreate_end_time().trim().length() == 0))  &&
           (getSystemCode()  == null) &&  
           (getStatus()  == null ) 

           )  {
                errorMessage =  bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
          } 
        
          //Form Validation of  Start Date        
        if (getCreate_start_date() != null && getCreate_start_date().trim().length() > 0)    {
            String message = edmValidation.validateDate(getCreate_start_date());
            if (!"success".equalsIgnoreCase(message)) {
                 errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, message, message);
           }
        }  
       //Form Validation of  End Date        
        if (getCreate_end_date() != null && getCreate_end_date().trim().length() > 0)    {
            String message = edmValidation.validateDate(getCreate_end_date());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0? message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "End Date:: " + errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, message, message);
             }
        }
        
        //Check FromDate-ToDate Range
        if (((getCreate_start_date() != null) && (getCreate_start_date().trim().length() > 0)) &&
                ((getCreate_end_date() != null) && (getCreate_end_date().trim().length() > 0))) {
            Date fromdate = null;
            Date todate = null;
            long startDate = 0;
            long endDate = 0;
            try {
                fromdate = DateUtil.string2Date(getCreate_start_date() + (getCreate_start_time() != null ? " " + getCreate_start_time() : " 00:00:00"));
                todate = DateUtil.string2Date(getCreate_end_date() + (getCreate_end_time() != null ? " " + getCreate_end_time() : " 23:59:59"));
                startDate = fromdate.getTime();
                endDate = todate.getTime();
            } catch (Exception dateExe) {
                //We would have caught the message to display in the edmValidation above, so ignore it here
            }
            if (endDate < startDate) {
                errorMessage = bundle.getString("ERROR_INVALID_FROMDATE_RANGE");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "fromdate :: " + errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
            }
        }
         if (getEUID() != null && getEUID().length() > 0)    {
            String message = edmValidation.validateNumber(getEUID());
            if (!"success".equalsIgnoreCase(message)) {
                errorMessage = (errorMessage != null && errorMessage.length() > 0?message:message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID:: " + errorMessage, errorMessage));
                java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.WARNING, errorMessage, errorMessage);
            }
        }    
        if (errorMessage != null && errorMessage.length() != 0) {
            return this.VALIDATION_ERROR;
        } 
        
        PotentialDuplicateSearchObject potentialDuplicateSearchObject = getPDSearchObject();
        MasterControllerService  masterControllerService = new MasterControllerService();    
        try {
            PotentialDuplicateIterator pdPageIterArray = masterControllerService.lookupPotentialDuplicates(potentialDuplicateSearchObject);
            
            // Code Added by Pratibha 
            int count = pdPageIterArray.count();
            String[][] temp = new String[count][2];

            if (pdPageIterArray != null & pdPageIterArray.count() > 0) {
                // add all the potential duplicates to the summary array  
                while (pdPageIterArray.hasNext()) {
                    PotentialDuplicateSummary pds[] = pdPageIterArray.first(pdPageIterArray.count());
                    for(int i=0;i<pds.length;i++)
                    {   
                        String euid1 = pds[i].getEUID1();
                        String euid2 = pds[i].getEUID2();
                        temp[i][0] = euid1;
                        temp[i][1] = euid2;                       
                    }
               }
            }

            ArrayList arl = new ArrayList();
                        
            for(int i=0;i<count;i++)
            { for(int j=0;j<2;j++)
                { boolean addData = true;
                  String data ;                 

                  for (int ii=0;ii<arl.size();ii++)
                  {     data = (String) arl.get(ii);
                        if(data.equalsIgnoreCase(temp[i][j]))
                        { addData = false;
                          break;
                        }
                  } 
                  if (addData == true)
                  { arl.add(temp[i][j]);
                  }
                }
            }
            //Code to create ArrayList
            ArrayList arlOuter = new ArrayList();
            for (int x = 0; x < arl.size(); x++) {
                String id = (String) arl.get(x);
                ArrayList arlInner = new ArrayList();
                boolean avlInArlOuter = false;
                arlInner.add(id);
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < 2; j++) {
                        String strData = temp[i][j];
                        if (id.equalsIgnoreCase(strData)) {
                            if (j == 0) {
                                //if(!arlInner.contains(strData))
                                //{arlInner.add(strData);
                                //}
                                if(!arlInner.contains(temp[i][1]))
                                {arlInner.add(temp[i][1]);
                                }
                            } else if (j == 1) {
                                //if(!arlInner.contains(strData))
                                //{arlInner.add(strData);
                                //}
                            if(!arlInner.contains(temp[i][0]))
                                {arlInner.add(temp[i][0]);
                                }
                            }                          
                          }
                        }
                    }
                arlOuter.add(arlInner);
            }
            ArrayList finalArrayList = arlOuter;            
            ArrayList arlInner = null;          
            if (getEUID() == null) {
                finalArrayList = arlOuter;
            } else {
                ArrayList outer = new ArrayList();
                for (int i = 0; i < arlOuter.size(); i++) {
                    arlInner = (ArrayList) arlOuter.get(i);
                    String strData = (String) arlInner.get(0);
                    if (strData.equalsIgnoreCase(getEUID())) {                     
                        outer.add(arlInner);
                        finalArrayList = outer;
                    }
                }
            }

            session.setAttribute("finalArrayList", finalArrayList);                
            //session.setAttribute("pdPageIter", pdPageIter);                
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
        return this.SEARCH_DUPLICATES;
    }
    
    public ArrayList getHeaderScreenObjectsArray() {
        try  {
            ConfigManager.init();
            ScreenObject  screenObject;
            ArrayList arrayList = new ArrayList();
            for(int i=1 ; i<9 ; i++) {
                headerScreenObjectsArray.add(ConfigManager.getInstance().getScreen(new Integer(i)));
            }
        } catch(Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }        
        return headerScreenObjectsArray;
    }

    public void setHeaderScreenObjectsArray(ArrayList headerScreenObjectsArray) {
        this.headerScreenObjectsArray = headerScreenObjectsArray;
    }

    public HashMap getActionMap() {
        return actionMap;
    }

    public void setActionMap(HashMap actionMap) {
        this.actionMap = actionMap;
    }

    public ArrayList getScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        try {
            
            //ConfigManager.init();
            //screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();
            
            ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
            screenConfigArray = screenObject.getSearchScreensConfig();    
            
            Iterator iteratorScreenConfig = screenConfigArray.iterator();

            while (iteratorScreenConfig.hasNext()) {
                SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();

                if (getSearchType().equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                    // Get an array list of field config groups
                    basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                    Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                    //Iterate the the FieldConfigGroup array list
                    while (basicSearchFieldConfigsIterator.hasNext()) {
                        //Build array of field config groups 
                        FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                        //Build array of field configs from 
                        screenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                    }
                }
            }
            
        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Config Array Object: ", e);
        }
        return screenConfigArray;
    }

    public void setScreenConfigArray(ArrayList screenConfigArray) {
        this.screenConfigArray = screenConfigArray;
    }
    
    // Getter and setter methods for non updateable fields as per screen config array
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

    public ArrayList getNonUpdateableFieldsArray() {
        return nonUpdateableFieldsArray;
    }
    public void setNonUpdateableFieldsArray(ArrayList nonUpdateableFieldsArray) {
        this.nonUpdateableFieldsArray = nonUpdateableFieldsArray;
    }

    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @exception ValidationException when entry is not valid.
     * @todo Document: Getter for PDSearchObject attribute of the SearchForm
     *      object
     * @return  the PD search object
     */
    public PotentialDuplicateSearchObject getPDSearchObject() {
        String sCreateStartTime = null;
        String sCreateEndTime = null;
        String sResolvedStartTime = null;
        String sResolvedEndTime = null;
        String sysCode = null;
        
        boolean paramEntered = false;
        
        PotentialDuplicateSearchObject obj = new PotentialDuplicateSearchObject();
        
        // Set to static values need clarification from prathiba
        //This will be revoked when login module is implemented.

        //obj.setPageSize(ConfigManager.getInstance().getMatchingConfig().getItemPerSearchResultPage());
        //obj.setMaxElements(ConfigManager.getInstance().getMatchingConfig().getMaxResultSize());

        obj.setPageSize(10);
        obj.setMaxElements(100);
        
        Date date = null;
                
        try {
            if ((getCreate_start_date() != null) && (getCreate_start_date().trim().length() > 0)) {
                /*
                 *
                if (sCreateStartTime.trim().length() == 0) {
                    sCreateStartTime = "00:00:00";
                }
                */
                date = DateUtil.string2Date(getCreate_start_date()+" "+"00:00:00");
                
                //date = this.convertString2Date(getCreate_start_date()+" "+"00:00:00");
                if (date != null) {
                    obj.setCreateStartDate(new Timestamp(date.getTime()));
                }
            }
           
            if ((getCreate_end_date() != null) && (getCreate_end_date().trim().length() > 0)) {
                /*
                if (sCreateEndTime.trim().length() == 0) {
                    sCreateEndTime = "23:59:59";
                }
                 */
                date = DateUtil.string2Date(getCreate_end_date()+" "+"23:59:59");
               // date = this.convertString2Date(getCreate_end_date()+" "+"00:00:00");
                
                if (date != null) {
                    obj.setCreateEndDate(new Timestamp(date.getTime()));
                }
            }
            
            // Get array of strings
            if(getEUID() != null ) {
                String[] euidArray = getStringEUIDs(getEUID());
                
                if(euidArray!=null & euidArray.length >0) {
                    obj.setEUIDs(euidArray);
                } else {
                    obj.setEUIDs(null);
                }
            }
            
        } catch(ValidationException validationException) {
            String errorMessage = "Validation Exception";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
        }
        return obj;
    }
    // getFieldConfigArrayListByTitle to get the field configs
    /**
     * 
     * @param screenTitle
     * @return
     */
    public ArrayList getFieldConfigArrayListByTitle(String screenTitle) {
        ArrayList basicSearchFieldConfigs = null;
        ArrayList fieldConfigArrayList = null;
        try {
            //ConfigManager.init();
            //screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
            screenConfigArray = screenObject.getSearchScreensConfig();

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
                        fieldConfigArrayList = basicSearchFieldGroup.getFieldConfigs();
                    }
                }
            }

        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        return fieldConfigArrayList;
    }
    public String[] getStringEUIDs(String euids) {
        
        StringTokenizer stringTokenizer = new StringTokenizer(euids,",");
        String[] euidsArray = new String[stringTokenizer.countTokens()];
        int i =0;
        while(stringTokenizer.hasMoreTokens())  {
            euidsArray[i] = new String(stringTokenizer.nextElement().toString());
            i++;
        }
        return euidsArray;
    }

    public ArrayList getResultsConfigArray() {
        ArrayList basicSearchFieldConfigs = null;
        ArrayList fieldConfigArrayList = null;
        try {
            //ConfigManager.init();
            //screenConfigArray = ConfigManager.getInstance().getScreen(new Integer("3")).getSearchScreensConfig();
            
            ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
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
                    resultsConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
            }

        } catch (Exception e) {
            mLogger.error("Failed Get the Screen Object: ", e);
        }
        return resultsConfigArray;
    }

    public void setResultsConfigArray(ArrayList resultsConfigArray) {
        this.resultsConfigArray = resultsConfigArray;
    }

    /**
     * 
     * @param event
     */
    public void setSearchTypeAction(ActionEvent event){
            String searchTypeValue = (String) event.getComponent().getAttributes().get("searchType");
            //set the search type as per the form
            this.setSearchType(searchTypeValue);
    
   }
    /**
     * 
     * @param event
     */
    public void buildEuidsAction(ActionEvent event){
           try {
            String searchTitle = (String) event.getComponent().getAttributes().get("searchTitle");
            ArrayList euidAtrributeList = (ArrayList) event.getComponent().getAttributes().get("euids");
            
            // we will have to remove the following filtering mechanism when the potential duplicates are properly returned from the SL. 
            Object[] euidAtrributeListObject = euidAtrributeList.toArray();
            ArrayList euidsArray = new ArrayList();
            for(int i=0;i<euidAtrributeListObject.length;i++){
                   if(i==0)euidsArray.add((String)euidAtrributeListObject[0]);
                   if( !((String)euidAtrributeListObject[0]).equalsIgnoreCase(((String)euidAtrributeListObject[i])) ) {
                       euidsArray.add((String)euidAtrributeListObject[i]);
                   }
            }

            String[] euids = new String[euidsArray.size()];
            Object[] finalEuidsArray = euidsArray.toArray();           

            for(int i=0;i<finalEuidsArray.length;i++) {
              euids[i]  = (String) finalEuidsArray[i];
            }
            // End of filtering mechanism

            // use Master MasterControllerService to get an array of enterprise objects.
            MasterControllerService masterControllerService = new MasterControllerService(); 
            ArrayList eoArrayList = masterControllerService.getEnterpriseObjects(euids);
            //keep list array of enterprise objects in the session
            
            session.setAttribute("enterpriseArrayList", eoArrayList);
            session.setAttribute("searchTitle", searchTitle); //Display simple person look up in patient details
            
           } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String toCompareScreen(){
        return "Record Details";
    }

  

    PotentialDuplicateSummary getComparePotentialDuplicateSummary() {
        return comparePotentialDuplicateSummary;
    }

    /**
     * 
     * @param comparePotentialDuplicateSummary
     */
    public void setComparePotentialDuplicateSummary(PotentialDuplicateSummary comparePotentialDuplicateSummary) {
        this.comparePotentialDuplicateSummary = comparePotentialDuplicateSummary;
    }

    public ArrayList getPotentialDuplicateSummaryArray() {
        return potentialDuplicateSummaryArray;
    }

    public void setPotentialDuplicateSummaryArray(ArrayList potentialDuplicateSummaryArray) {
        this.potentialDuplicateSummaryArray = potentialDuplicateSummaryArray;
    }

    public ArrayList getEuidsArrayList(PotentialDuplicateSummary potentialDuplicateSummary) {
        ArrayList strEuid = new ArrayList();
        try {
            strEuid.add(potentialDuplicateSummary.getEUID1());
            PotentialDuplicateIterator potentialDuplicateIterator = potentialDuplicateSummary.getAssociatedPotentialDuplicates();
            PotentialDuplicateSummary[] associatedDuplicates = potentialDuplicateIterator.first(potentialDuplicateIterator.count());
            for (int i = 0; i < associatedDuplicates.length; i++) {
                strEuid.add(associatedDuplicates[i].getEUID1());
            }
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(SearchDuplicatesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strEuid;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }
     
}
