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
 * SourceHandler.java 
 * Created on September 12, 2007
 * Author : Rajanikanth
 *  
 */


package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.common.PullDownListItem;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import net.java.hulp.i18n.LocalizationSupport;

public class SourceHandler {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SourceHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private static final String SEARCH_SOURCE_SUCCESS = "success";

    // Create fields for non updateable fields as per screen config array
    private String EUID;
    private String SystemCode;
    private String LID;
    private String create_start_date;
    private String create_end_date;
    private String create_start_time;
    private String create_end_time;
    private String Status;

    // Create fields for updateable fields as per screen config array like firstname,lastname...etc (Person, Address, Auxiliary, Comment and Alias)
    private HashMap updateableFeildsMap =  new HashMap();    

    //Arraylist to display EDM driven search criteria.
    private ArrayList searchScreenConfigArray;
    
    //Arraylist to display EDM driven search results.
    private ArrayList searchResultsConfigArray;

    //Get the session variable from faces context
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    //Get the session variable from faces context
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    //Get the screen object from session
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    //sub screen tab name for each tab on the source records page
    private String subScreenTab = "View/Edit";

    //sub screen tab name for each tab on the source records page
    private String searchType = "Basic Search";

    //Arraylist to display EDM driven search criteria viewEdit.
    private ArrayList viewEditScreenConfigArray;

    //Arraylist to display EDM driven search criteria Add screen.
    private ArrayList addScreenConfigArray;
    
    //Arraylist to display EDM driven search criteria merge.
    private ArrayList mergeScreenConfigArray;

    //Arraylist to display EDM driven search criteria viewEdit.
    private ArrayList viewEditResultsConfigArray;
    
    private static final String  VALIDATION_ERROR     = "validationfailed";

    //Hash map for single SO  for view
    private HashMap singleSOHashMap = new HashMap();
    
    //Array list for all person fields
    private ArrayList allFieldConfigs;

    //Array list for all person fields
    private ArrayList personFieldConfigs;
    //Array list for all address fields
    private ArrayList addressFieldConfigs;
    //Array list for all person fields
    private ArrayList phoneFieldConfigs;
    //Array list for all person fields
    private ArrayList aliasFieldConfigs;

    MasterControllerService  masterControllerService = new MasterControllerService();
    private HashMap deactivatedSOHashMap = new HashMap();
    
    //Hash map for single SO  for view
    private ArrayList singleSOHashMapArrayList = new ArrayList();

    //Hash map arraylist for single SO 
    private ArrayList singleAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Address
    private ArrayList singleAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Phone
    private ArrayList singlePhoneHashMapArrayList = new ArrayList();
    

    //Hash map for single SO Adress
    private ArrayList singleSOKeyArrayList = new ArrayList();

    
    //Hash map for single SO  EDITING
    private HashMap editSingleSOHashMap = new HashMap();

    private HashMap editSoAddressHashMap = new HashMap();
    private HashMap editSoPhoneHashMap = new HashMap();
    private HashMap editSoAliasHashMap = new HashMap();
    
    public static final String UPDATE_SUCCESS = "UPDATE SUCCESS";
    
    //Hash map for single SO  EDITING
    private HashMap readOnlySingleSOHashMap = new HashMap();

    
    private ArrayList allChildNodesNames  = new ArrayList();
    
    private HashMap allNodeFieldConfigs = new HashMap();
    private HashMap allNodeFieldConfigsSizes = new HashMap();
    private ArrayList rootNodeFieldConfigs = new ArrayList();
    private ArrayList allEOChildNodesLists =  new ArrayList();
    private ArrayList allSOChildNodesLists =  new ArrayList();
    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
    private int euidLength;
    private String enteredFieldValues = new String();

    /** Creates a new instance of SourceHandler */
    public SourceHandler() {
    }

    /**
     * 
     * @return
     */
    public String performSubmit() {

        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
        
        ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
//        ArrayList fieldConfigArrayList  = this.getViewEditScreenConfigArray();
//        Iterator fieldConfigArrayIter =  fieldConfigArrayList.iterator();
//        int totalFields = fieldConfigArrayList.size();
//        int countMenuFields = 0;
//        int countEmptyFields = 0;
//        while(fieldConfigArrayIter.hasNext())  {           
//             FieldConfig  fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
//             String feildValue = (String) this.getUpdateableFeildsMap().get(fieldConfig.getName());                          
//            
//             if (fieldConfig.getName().equalsIgnoreCase("SystemCode")) {
//                this.setSystemCode(feildValue);
//             }
//
//             if("MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue == null)  {
//                 countMenuFields++;     
//             } else if(!"MenuList".equalsIgnoreCase(fieldConfig.getGuiType()) && feildValue != null && feildValue.trim().length() == 0)  { 
//               countEmptyFields++;       
//             }
//        }
//        String lid = this.getLID().replaceAll("-", ""); 
//        this.setLID(lid);
//        
//        //Checking one of many condition here   
//        if( (totalFields > 0 && countEmptyFields+countMenuFields == totalFields)   && // all updateable fields are left blank
//           (this.getEUID() == null || (this.getEUID()  != null && this.getEUID().trim().length() == 0))  &&
//           (this.getLID()  == null || (this.getLID()  != null && this.getLID().trim().length() == 0))  &&
//           (this.getCreate_start_date()  == null || (this.getCreate_start_date()  != null && this.getCreate_start_date().trim().length() == 0))  &&
//           (this.getCreate_start_time()  == null || (this.getCreate_start_time()  != null && this.getCreate_start_time().trim().length() == 0))  &&
//           (this.getCreate_end_date()  == null  || (this.getCreate_end_date()  != null && this.getCreate_end_date().trim().length() == 0))  &&
//           (this.getCreate_end_time()  == null || (this.getCreate_end_time()  != null && this.getCreate_end_time().trim().length() == 0))  &&
//           (this.getSystemCode()  == null) &&  
//           (this.getStatus()  == null ) 
//           )  {
//                String errorMessage = bundle.getString("potential_dup_search_error");
//                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
//                return VALIDATION_ERROR;  
//        }

        HashMap newFieldValuesMap = new HashMap();

        if (enteredFieldValues != null && enteredFieldValues.length() > 0) {
                String[] fieldNameValues = enteredFieldValues.split(">>");
                for (int i = 0; i < fieldNameValues.length; i++) {
                    String string = fieldNameValues[i];
                    String[] keyValues = string.split("##");
                    if(keyValues.length ==2) {
                      if(keyValues[1] != null && keyValues[1].trim().length() == 0 ) {
                        newFieldValuesMap.put(keyValues[0], null);
                      } else {
                        newFieldValuesMap.put(keyValues[0], keyValues[1]);
                      }
                    }
                }
            }
           
            //set LID and system codes here.
  
            setLID((String) newFieldValuesMap.get(MasterControllerService.LID));
            setSystemCode((String) newFieldValuesMap.get("SystemCode"));
                    String  validationMessage  = new String();  
					String localIdDesignation =	 ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
            if( this.getLID() == null) {
				 validationMessage =  bundle.getString("LID_SysCode") + " " + localIdDesignation  ;
                  //validationMessage = "Please Enter LID Value";
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, validationMessage, validationMessage));
                  return this.SEARCH_SOURCE_SUCCESS;
            } else if( this.getSystemCode()  == null ) {
                  validationMessage = "Please Enter SystemCode Value";
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, validationMessage, validationMessage));
                  return this.SEARCH_SOURCE_SUCCESS;
            } else if( (this.getLID() != null && this.getLID().trim().length() == 0 )
		 && (this.getSystemCode() != null && this.getSystemCode().trim().length() > 0 )) {
				  validationMessage =  bundle.getString("LID_SysCode") + " " + localIdDesignation ;
                  //validationMessage = "Please Enter LID Value";
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, validationMessage, validationMessage));
                  return this.SEARCH_SOURCE_SUCCESS;                
            }


        //get array of lids 
        String lids[] = this.getStringEUIDs(this.getLID());
        //instantiate master controller service
        SystemObject singleSystemObject = null;
        SystemObject[] systemObjectArrays = null;
        ArrayList systemObjectsMapList = new ArrayList();
        EPathArrayList ePathArrayList = new EPathArrayList();
//        ArrayList newArrayList  = this.getViewEditResultsConfigArray();
//        try {
//            for (int i = 0; i < newArrayList.size(); i++) {
//                FieldConfig fieldConfig = (FieldConfig) newArrayList.get(i);
//                ePathArrayList.add(fieldConfig.getFullFieldName());
//            }
//        } catch (EPathException ex) {
//            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//          

          SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
          String createDate = null;
        try {
            if (lids.length == 1) { // only sigle LID entered by the user
                // only sigle LID entered by the user
                singleSystemObject = masterControllerService.getSystemObject(this.SystemCode, lids[0]);
                if(singleSystemObject == null) {
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("system_object_not_found_error_message"), bundle.getString("system_object_not_found_error_message")));
                  return this.SEARCH_SOURCE_SUCCESS;
                }
                //EPathArrayList personEPathArrayList = buildSystemObjectEpaths(screenObject.getRootObj().getName());
               
                HashMap systemObjectMap = compareDuplicateManager.getSystemObjectAsHashMap(singleSystemObject, screenObject);
                        
                session.setAttribute("singleSystemObjectLID", singleSystemObject);
                session.setAttribute("systemObjectMap", systemObjectMap);
                //set the single SO hash map for single so
                this.setSingleSOHashMap(systemObjectMap);
                
                 
                //session.setAttribute("singleSystemObject", singleSystemObject);
                session.setAttribute("keyFunction","viewSO");
                
            } else if (lids.length > 1) {
                // only sigle LID entered by the user
                systemObjectArrays = masterControllerService.getSystemObjects(this.SystemCode, lids);
                HashMap valueHasphMap = new HashMap();
                for (int i = 0; i < systemObjectArrays.length; i++) {
                    SystemObject systemObject = systemObjectArrays[i];
                    valueHasphMap = masterControllerService.getSystemObjectAsHashMap(systemObject, ePathArrayList);
                    valueHasphMap.put("LID", systemObject.getLID());// add lid here
                    EnterpriseObject enterpriseObject =  masterControllerService.getEnterpriseObjectForSO(systemObject);
                    valueHasphMap.put("EUID", enterpriseObject.getEUID());// add euid here
                    valueHasphMap.put("Source", systemObject.getSystemCode());// add euid here
                    createDate = simpleDateFormatFields.format(systemObject.getCreateDateTime());
                    valueHasphMap.put("DateTime", createDate);// add CreateDateTime
                    
                    systemObjectsMapList.add(valueHasphMap);
                }
                // add systemObjectsMapList in the session for retrieving first name...etc in the output
                session.setAttribute("systemObjectsMapList", systemObjectsMapList);
                session.setAttribute("viewEditResultsConfigArray", this.getViewEditResultsConfigArray());
                session.removeAttribute("keyFunction");
            }

        } catch (ProcessingException ex) {
             mLogger.error(mLocalizer.x("SRC018: Unable to perform submit :{0}", ex.getMessage()),ex);
            //Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
             mLogger.error(mLocalizer.x("SRC019: Unable to perform submit :{0}", ex.getMessage()),ex);
            //Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.SEARCH_SOURCE_SUCCESS;
    }

    
    // added by samba for testing source record search.
    public static void main(String args[]) throws ProcessingException, UserException {
        SourceHandler sourceHandler = new SourceHandler();

    }



    //getter and setter methods for updateable fields.
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

    /**
     * 
     * @param LID
     */
    public void setLID(String LID) {
        this.LID = LID;
    }

    /**
     * 
     * @return
     */
    public String getCreate_start_date() {
        return create_start_date;
    }

    /**
     * 
     * @param create_start_date
     */
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
    public ArrayList getSearchResultsConfigArray() {
        return searchResultsConfigArray;
    }

    /**
     * 
     * @param searchResultsConfigArray
     */
    public void setSearchResultsConfigArray(ArrayList searchResultsConfigArray) {
        this.searchResultsConfigArray = searchResultsConfigArray;
    }

    public String getSubScreenTab() {
        return subScreenTab;
    }

    public void setSubScreenTab(String subScreenTab) {
        this.subScreenTab = subScreenTab;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * 
     * @param event
     */
    public void setSearchTypeAction(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");


        String searchTypeValue = (String) event.getComponent().getAttributes().get("searchType");
        //set the search type as per the form
        this.setSearchType(searchTypeValue);    
   }
    
    /**
     * 
     * @param event
     */
    public void deleteSOAddress(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");       
   }

    /**
     * 
     * @param event
     */
    public void addSOAddress(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
   }

    /**
     * 
     * @param event
     */
    public void removeSingleLID(ActionEvent event){
        // set the tab name to be view/edit
            session.setAttribute("tabName", "View/Edit");
            session.removeAttribute("singleSystemObjectLID");   
   }
    
   public String cancelEditLID(){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
        session.removeAttribute("singleSystemObjectLID");   
        SourceAddHandler sourceAddHandlerFaces = (SourceAddHandler)session.getAttribute("SourceAddHandler");
        sourceAddHandlerFaces.getNewSOHashMap().clear();

        return NavigationHandler.SOURCE_RECORDS;
   }

   public String cancelSaveLID(){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "Add");
        session.removeAttribute("singleSystemObjectLID");   
        SourceAddHandler sourceAddHandlerFaces = (SourceAddHandler)session.getAttribute("SourceAddHandler"); 
        
        //reset all the fields here for root node and minor objects
        sourceAddHandlerFaces.getNewSOHashMap().clear();
        sourceAddHandlerFaces.getNewSOMinorObjectsHashMapArrayList().clear();

        return NavigationHandler.SOURCE_RECORDS;
   }
    
    /**
     * 
     * @param event
     */
    public void editLID(ActionEvent event){ 
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
        try {
            SystemObject singleSystemObjectEdit = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");
            EPathArrayList personEPathArrayList = buildPersonEpaths();
            HashMap editSystemObjectMap = masterControllerService.getSystemObjectAsHashMap(singleSystemObjectEdit, personEPathArrayList);

            session.setAttribute("singleSystemObjectLID", singleSystemObjectEdit);
            session.setAttribute("systemObjectMap", editSystemObjectMap);
            
            //set the single SO hash map for single so EDITING
            this.setEditSingleSOHashMap(editSystemObjectMap);
            this.setReadOnlySingleSOHashMap(editSystemObjectMap);
            
            //set address array list of hasmap for editing
            ArrayList addressMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, buildSystemObjectEpaths("Address"), "Address",null);
            this.setSingleAddressHashMapArrayList(addressMapArrayList);

            //set phone array list of hasmap for editing
            ArrayList phoneMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, buildSystemObjectEpaths("Phone"), "Phone",null);
            this.setSinglePhoneHashMapArrayList(phoneMapArrayList);
           
            //set alias array list of hasmap for editing
            ArrayList aliasMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, buildSystemObjectEpaths("Alias"), "Alias",null);
            this.setSingleAliasHashMapArrayList(aliasMapArrayList);
           

            session.setAttribute("keyFunction", "editSO");
        } catch (ObjectException ex) {
             mLogger.error(mLocalizer.x("SRC020: Unable to edit LID :{0}", ex.getMessage()),ex);
        } catch (EPathException ex) {
            mLogger.error(mLocalizer.x("SRC021: Unable to edit LID :{0}", ex.getMessage()),ex);
        }
   }
    
    /**
     * 
     * @param event
     */
    public void activateSO(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
    
        try {
            SystemObject systemObject = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");
            masterControllerService.activateSystemObject(systemObject);             
            SystemObject updatedSystemObject = masterControllerService.getSystemObject(systemObject.getSystemCode(), systemObject.getLID());
            //get the System Object as hashmap
            HashMap updatedSoMap = compareDuplicateManager.getSystemObjectAsHashMap(updatedSystemObject, screenObject);
             
            SourceAddHandler sourceAddHandler = (SourceAddHandler) session.getAttribute("SourceAddHandler");

            //update the handler variable for editing
            sourceAddHandler.setNewSOHashMap(updatedSoMap);
            
           //Keep the updated SO in the session again
            session.setAttribute("singleSystemObjectLID", updatedSystemObject);
            session.setAttribute("keyFunction", "editSO");
                        
        } catch (ProcessingException ex) {
         mLogger.error(mLocalizer.x("SRC023: Unable to activate SO :{0}", ex.getMessage()),ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("SRC024: Unable to activate SO :{0}", ex.getMessage()),ex);
        }
   }
    /**
     * 
     * @param event
     */
    public void deactivateSO(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");

        try {
            SystemObject systemObject = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");          
            masterControllerService.deactivateSystemObject(systemObject);
            SystemObject updatedSystemObject = masterControllerService.getSystemObject(systemObject.getSystemCode(), systemObject.getLID());
            
            setDeactivatedSOHashMap(compareDuplicateManager.getSystemObjectAsHashMap(updatedSystemObject, screenObject));
            
            //Keep the updated SO in the session again
            session.setAttribute("singleSystemObjectLID", updatedSystemObject);
            session.setAttribute("keyFunction","editSO");
        } catch (ObjectException ex) {
             mLogger.error(mLocalizer.x("SRC025: Unable to deactivate SO :{0}", ex.getMessage()),ex);
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SRC026: Unable to deactivate SO :{0}", ex.getMessage()),ex);
        } catch (UserException ex) {
             mLogger.error(mLocalizer.x("SRC027: Unable to deactivate SO :{0}", ex.getMessage()),ex);
        }
          //session.setAttribute("keyFunction","editSO");
   }
    
    /**
     * 
     * @return 
     */
    public String updateSO(){
            // set the tab name to be view/edit
            session.setAttribute("tabName", "View/Edit");       
        try {
             // To recognize which operation/object type             
             this.getEditSingleSOHashMap().put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE);
             SystemObject systemObject = (SystemObject) session.getAttribute("singleSystemObjectLID");
             //get the enterprise object for the system object
             EnterpriseObject  sysEnterpriseObject = masterControllerService.getEnterpriseObjectForSO(systemObject);
             
            // add so to the array list
            //call modifySystemObjects to update the
            masterControllerService.modifySystemObject(systemObject, this.getEditSingleSOHashMap());
            
            //Keep the updated SO in the session again
            //SystemObject updatedSystemObject = masterControllerService.getSystemObject(systemObject.getSystemCode(), systemObject.getLID());

            //session.setAttribute("singleSystemObjectLID", updatedSystemObject);
            session.setAttribute("keyFunction","editSO");
        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC028: Unable to update SO :{0}", ex.getMessage()),ex);
        }
        return UPDATE_SUCCESS;
          //session.setAttribute("keyFunction","viewSO");
   }
    /**
     * 
     * @param event
     */
    public void viewEUID(ActionEvent event){
            
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");            
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            SystemObject systemObject = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");
            EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(systemObject);
            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            ArrayList newEOArrayList = new ArrayList();
            newEOArrayList.add(eoMap);
            
            session.setAttribute("comapreEuidsArrayList",newEOArrayList);          
   }

    /**
     * 
     * @param event
     */
    public void setLIDValue(ActionEvent event){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");
        
        try {
            String sourceLID = (String) event.getComponent().getAttributes().get("sourceLID");
            String sourceSystem = (String) event.getComponent().getAttributes().get("sourceSystem");
            SystemObject systemObject = masterControllerService.getSystemObject(sourceSystem, sourceLID);
            EPathArrayList  personEPathArrayList  = buildPersonEpaths();
            HashMap systemObjectMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
            
                //set the single SO hash map for single so
                this.setSingleSOHashMap(systemObjectMap);
                
                ArrayList addressMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, buildSystemObjectEpaths("Address"),"Address",null);
                this.setSingleAddressHashMapArrayList(addressMapArrayList);

                ArrayList phoneMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, buildSystemObjectEpaths("Phone"),"Phone",null);
                this.setSinglePhoneHashMapArrayList(phoneMapArrayList);                
                
                ArrayList aliasMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, buildSystemObjectEpaths("Alias"),"Alias",null);
                this.setSingleAliasHashMapArrayList(aliasMapArrayList);                
                
            
            session.setAttribute("singleSystemObjectLID", systemObject);
            session.setAttribute("systemObjectMap", systemObjectMap);
            session.setAttribute("keyFunction","viewSO");
           
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC029: Unable to set LID value :{0}", ex.getMessage()),ex);
        }
    
   }

    public ArrayList getViewEditScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        HttpSession sessionLocal = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
        ScreenObject screenObjectLocal = (ScreenObject) sessionLocal.getAttribute("ScreenObject");
        
        if(screenObjectLocal!=null){
    
        ArrayList subScreenObjectList = screenObjectLocal.getSubscreensConfig();
        ScreenObject subScreenObject = null;

        // build the screen  as per the subscreen title (this.getSubScreenTab())
        for (int i = 0; i < subScreenObjectList.size(); i++) {
            ScreenObject object = (ScreenObject) subScreenObjectList.get(i);
            if(object.getDisplayTitle().equalsIgnoreCase("View/Edit")) {
                subScreenObject = object;
            }
        }

        //Get the screen config array
        ArrayList screenConfigArray = subScreenObject.getSearchScreensConfig();
        Iterator iteratorScreenConfig = screenConfigArray.iterator();

        while (iteratorScreenConfig.hasNext()) {
            SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();

            //get the field config as per the search type
            if (this.getSearchType().equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                    //Build array of field configs from 
                    viewEditScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
            }
        }
        }
        return viewEditScreenConfigArray;
    }

    public void setViewEditScreenConfigArray(ArrayList viewEditScreenConfigArray) {
        this.viewEditScreenConfigArray = viewEditScreenConfigArray;
    }

    public ArrayList getAddScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        
        if(screenObject!=null){
        
        ArrayList subScreenObjectList = screenObject.getSubscreensConfig();
        ScreenObject subScreenObject = null;

        // build the screen  as per the subscreen title "Add"
        for (int i = 0; i < subScreenObjectList.size(); i++) {
            ScreenObject object = (ScreenObject) subScreenObjectList.get(i);
            if(object.getDisplayTitle().equalsIgnoreCase("Add")) {
                subScreenObject = object;
            }
        }
        
        //Get the screen config array
        ArrayList screenConfigArray = subScreenObject.getSearchScreensConfig();
        Iterator iteratorScreenConfig = screenConfigArray.iterator();

        while (iteratorScreenConfig.hasNext()) {
            SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
            //get the field config as per the search type
            if (this.getSearchType().equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                    //Build array of field configs from 
                    addScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
            }
        }
        }
        return addScreenConfigArray;
    }

    public void setAddScreenConfigArray(ArrayList addScreenConfigArray) {
        this.addScreenConfigArray = addScreenConfigArray;
    }

    public ArrayList getMergeScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        
        if(screenObject!=null){
        
        ArrayList subScreenObjectList = screenObject.getSubscreensConfig();
        ScreenObject subScreenObject = null;

        // build the screen  as per the subscreen title "Merge"
        for (int i = 0; i < subScreenObjectList.size(); i++) {
            ScreenObject object = (ScreenObject) subScreenObjectList.get(i);
            if(object.getDisplayTitle().equalsIgnoreCase("Merge")) {
                subScreenObject = object;
            }
        }
        
        //Get the screen config array
        ArrayList screenConfigArray = subScreenObject.getSearchScreensConfig();
        Iterator iteratorScreenConfig = screenConfigArray.iterator();

        while (iteratorScreenConfig.hasNext()) {
            SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
            //get the field config as per the search type
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                    //Build array of field configs from 
                    mergeScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
        }
        }
        
        return mergeScreenConfigArray;
    }

    public void setMergeScreenConfigArray(ArrayList mergeScreenConfigArray) {
        this.mergeScreenConfigArray = mergeScreenConfigArray;
    }

    /**
     * 
     * @return
     */
    public ArrayList getSearchScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        
        if (screenObject!=null){
            
        ArrayList subScreenObjectList = screenObject.getSubscreensConfig();
        ScreenObject subScreenObject = null;

        // build the screen  as per the subscreen title (this.getSubScreenTab())
        for (int i = 0; i < subScreenObjectList.size(); i++) {
            ScreenObject object = (ScreenObject) subScreenObjectList.get(i);
            if(object.getDisplayTitle().equalsIgnoreCase(this.getSubScreenTab())) {
                subScreenObject = object;
            }
        }
        
        //Get the screen config array
        ArrayList screenConfigArray = subScreenObject.getSearchScreensConfig();
        Iterator iteratorScreenConfig = screenConfigArray.iterator();

        while (iteratorScreenConfig.hasNext()) {
            SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) iteratorScreenConfig.next();
            //get the field config as per the search type
            if (this.getSearchType().equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
                // Get an array list of field config groups
                basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
                //Iterate the the FieldConfigGroup array list
                while (basicSearchFieldConfigsIterator.hasNext()) {
                    //Build array of field config groups 
                    FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                    //Build array of field configs from 
                    searchScreenConfigArray = basicSearchFieldGroup.getFieldConfigs();
                }
            }
        }
        }
        return searchScreenConfigArray;
    }

    /**
     * 
     * @param searchScreenConfigArray
     */
    public void setSearchScreenConfigArray(ArrayList searchScreenConfigArray) {
        this.searchScreenConfigArray = searchScreenConfigArray;
    }

    public ArrayList getViewEditResultsConfigArray() {

        HttpSession sessionLocal = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
      
        ScreenObject screenObjectLocal = (ScreenObject) sessionLocal.getAttribute("ScreenObject");
       if (screenObjectLocal!=null){

        ArrayList subScreenObjectList = screenObject.getSubscreensConfig();
        ScreenObject subScreenObject = null;

        // build the screen  as per the subscreen title (this.getSubScreenTab())
        for (int i = 0; i < subScreenObjectList.size(); i++) {
            ScreenObject object = (ScreenObject) subScreenObjectList.get(i);
            if (object.getDisplayTitle().equalsIgnoreCase("View/Edit")) {
                subScreenObject = object;
            }
        }
        ArrayList resultsScreenConfigArray = subScreenObject.getSearchResultsConfig();
        Iterator iteratorScreenConfig = resultsScreenConfigArray.iterator();

        while (iteratorScreenConfig.hasNext()) {
            SearchResultsConfig objSearchScreenConfig = (SearchResultsConfig) iteratorScreenConfig.next();

            // Get an array list of field config groups
            ArrayList basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
            Iterator basicSearchFieldConfigsIterator = basicSearchFieldConfigs.iterator();
            //Iterate the the FieldConfigGroup array list
            while (basicSearchFieldConfigsIterator.hasNext()) {
                //Build array of field config groups 
                FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) basicSearchFieldConfigsIterator.next();

                //Build array of field configs from 
                viewEditResultsConfigArray = basicSearchFieldGroup.getFieldConfigs();
            }
        }
        }
        return viewEditResultsConfigArray;
    }

    public void setViewEditResultsConfigArray(ArrayList viewEditResultsConfigArray) {
        this.viewEditResultsConfigArray = viewEditResultsConfigArray;
    }

    /**
     * 
     * @param euids
     * @return
     */
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

    public EPathArrayList buildPersonEpaths() {
        EPathArrayList ePathArrayList = new EPathArrayList();
        try {
            ConfigManager.init();
            
            if(screenObject!=null){
            String rootName = screenObject.getRootObj().getName();
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(rootName);
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                if(!(rootName+ ".EUID").equalsIgnoreCase(fieldConfig.getFullFieldName())) {
                  ePathArrayList.add(fieldConfig.getFullFieldName());
                }
            }
            }
            
        } catch (Exception ex) {
             mLogger.error(mLocalizer.x("SRC030: Failed to build Epaths :{0}", ex.getMessage()),ex);
        }
        return ePathArrayList;

    }

    public EPathArrayList buildSystemObjectEpaths(String objectType) {
        EPathArrayList ePathArrayList = new EPathArrayList();
        try {
            ConfigManager.init();
            ObjectNodeConfig objectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(objectType);
            if (objectNodeConfig != null)
			{
				FieldConfig[] allFeildConfigs = objectNodeConfig.getFieldConfigs();
                                
                     if (screenObject!=null){
	            String rootName = screenObject.getRootObj().getName();
	            //Build Person Epath Arraylist
		        for (int i = 0; i < allFeildConfigs.length; i++) {
			        FieldConfig fieldConfig = allFeildConfigs[i];
				    if(     !(rootName+ ".EUID").equalsIgnoreCase(fieldConfig.getFullFieldName())) 
						{    ePathArrayList.add(fieldConfig.getFullFieldName());
						}
				}
			}
            }
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC031: Failed to build Epaths :{0}", ex.getMessage()),ex);
        }
        return ePathArrayList;

    }

    public ArrayList buildAllFieldConfigArrayList(String objectType) {
        ArrayList fcArrayList = new ArrayList();
        try {
            ConfigManager.init();
            ObjectNodeConfig objectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(objectType);
            FieldConfig[] allFeildConfigs = objectNodeConfig.getFieldConfigs();
            
            if(screenObject!=null){
            String rootName = screenObject.getRootObj().getName();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                fcArrayList.add(fieldConfig);
            }
            }
        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC070: Failed to build Field config list :{0}", ex.getMessage()),ex);
        }
        return fcArrayList;

    }
    public HashMap getSingleSOHashMap() {
        return singleSOHashMap;
    }

    public void setSingleSOHashMap(HashMap singleSOHashMap) {
        this.singleSOHashMap = singleSOHashMap;
    }

    public ArrayList getAllFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            
            if(screenObject!=null){
            String rootName  = screenObject.getRootObj().getName();
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(rootName);
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                if(!"Person.EUID".equalsIgnoreCase(fieldConfig.getFullFieldName())) {
                  newArrayList.add(fieldConfig);
                }
            }

            ObjectNodeConfig[] childObjectNodeConfig = personObjectNodeConfig.getChildConfigs();
            for (int i = 0; i < childObjectNodeConfig.length; i++) {
                ObjectNodeConfig objectNodeConfig = childObjectNodeConfig[i];
                FieldConfig[] allChildFeildConfigs = objectNodeConfig.getFieldConfigs();
                for (int j = 0; j < allChildFeildConfigs.length; j++) {
                    FieldConfig fieldConfig = allChildFeildConfigs[j];
                    newArrayList.add(fieldConfig);
                }
            }}
        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC032: Failed to get all field configs :{0}", ex.getMessage()),ex);
        }

        allFieldConfigs = newArrayList;//store all the fields in the arraylist
        return allFieldConfigs;
    }

    public void setAllFieldConfigs(ArrayList allFieldConfigs) {
        this.allFieldConfigs = allFieldConfigs;
    }

    public ArrayList getSingleSOHashMapArrayList() {
        return singleSOHashMapArrayList;
    }

    public void setSingleSOHashMapArrayList(ArrayList singleSOHashMapArrayList) {
        this.singleSOHashMapArrayList = singleSOHashMapArrayList;
    }

    public ArrayList getSingleSOKeyArrayList() {
        return singleSOKeyArrayList;
    }

    public void setSingleSOKeyArrayList(ArrayList singleSOKeyArrayList) {
        this.singleSOKeyArrayList = singleSOKeyArrayList;
    }

    private ArrayList buildSOKeyList(ArrayList systemObjectMapArrayList) {
        ArrayList soKeySetList = new ArrayList();
        //loop through the  systemObjectMapArrayList
        for (int i = 0; i < systemObjectMapArrayList.size(); i++) {
            HashMap objectHashMap = (HashMap) systemObjectMapArrayList.get(i);
            Set soKeySet = objectHashMap.keySet();
            Object[] soKeySetObj = soKeySet.toArray();
            //build the key set and add to the arraylist
            for (int j = 0; j < soKeySetObj.length; j++) {
                String objectKeySetString = (String) soKeySetObj[j];
                soKeySetList.add(objectKeySetString);
            }

        }
        return soKeySetList;
    }

    public ArrayList getSingleAddressHashMapArrayList() {
        return singleAddressHashMapArrayList;
    }

    public void setSingleAddressHashMapArrayList(ArrayList singleAddressHashMapArrayList) {
        this.singleAddressHashMapArrayList = singleAddressHashMapArrayList;
    }

    public ArrayList getSingleAliasHashMapArrayList() {
        return singleAliasHashMapArrayList;
    }

    public void setSingleAliasHashMapArrayList(ArrayList singleAliasHashMapArrayList) {
        this.singleAliasHashMapArrayList = singleAliasHashMapArrayList;
    }

    public ArrayList getSinglePhoneHashMapArrayList() {
        return singlePhoneHashMapArrayList;
    }

    public void setSinglePhoneHashMapArrayList(ArrayList singlePhoneHashMapArrayList) {
        this.singlePhoneHashMapArrayList = singlePhoneHashMapArrayList;
    }

    public ArrayList getAddressFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig("Address");
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                  newArrayList.add(fieldConfig);
            }

        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC033: Failed to get field configs :{0}", ex.getMessage()),ex);
        }

        addressFieldConfigs = newArrayList;//store all the fields in the arraylist

        return addressFieldConfigs;
    }

    public void setAddressFieldConfigs(ArrayList addressFieldConfigs) {
        this.addressFieldConfigs = addressFieldConfigs;
    }

    public ArrayList getPhoneFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig("Phone");
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                  newArrayList.add(fieldConfig);
            }

        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC034: Failed to get field configs :{0}", ex.getMessage()),ex);
        }

        phoneFieldConfigs = newArrayList;//store all the fields in the arraylist
        return phoneFieldConfigs;
    }

    public void setPhoneFieldConfigs(ArrayList phoneFieldConfigs) {
        this.phoneFieldConfigs = phoneFieldConfigs;
    }

    public ArrayList getAliasFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig("Alias");
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                newArrayList.add(fieldConfig);
            }

        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC035: Failed to get field configs :{0}", ex.getMessage()),ex);
        }

        aliasFieldConfigs = newArrayList;//store all the fields in the arraylist
        return aliasFieldConfigs;
    }

    public void setAliasFieldConfigs(ArrayList aliasFieldConfigs) {
        this.aliasFieldConfigs = aliasFieldConfigs;
    }

    public ArrayList getPersonFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            
            if(screenObject!=null){
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(screenObject.getRootObj().getName());
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
               
                String strCheckEuid = screenObject.getRootObj().getName()+".EUID";
               
                if(!strCheckEuid.equalsIgnoreCase(fieldConfig.getFullFieldName())) {
                  newArrayList.add(fieldConfig);
                }
            }
            }

        } catch (Exception ex) {
          mLogger.error(mLocalizer.x("SRC036: Failed to get field configs :{0}", ex.getMessage()),ex);
        }

        personFieldConfigs = newArrayList;//store all the fields in the arraylist
        return personFieldConfigs;
    }

    public void setPersonFieldConfigs(ArrayList personFieldConfigs) {
        this.personFieldConfigs = personFieldConfigs;
    }

    public HashMap getEditSingleSOHashMap() {
        return editSingleSOHashMap;
    }

    public void setEditSingleSOHashMap(HashMap editSingleSOHashMap) {
        this.editSingleSOHashMap = editSingleSOHashMap;
    }

    public HashMap getReadOnlySingleSOHashMap() {
        return readOnlySingleSOHashMap;
    }

    public void setReadOnlySingleSOHashMap(HashMap readOnlySingleSOHashMap) {
        this.readOnlySingleSOHashMap = readOnlySingleSOHashMap;
    }

    public HashMap getEditSoAddressHashMap() {
        return editSoAddressHashMap;
    }

    public void setEditSoAddressHashMap(HashMap editSoAddressHashMap) {
        this.editSoAddressHashMap = editSoAddressHashMap;
    }

    public HashMap getEditSoPhoneHashMap() {
        return editSoPhoneHashMap;
    }

    public void setEditSoPhoneHashMap(HashMap editSoPhoneHashMap) {
        this.editSoPhoneHashMap = editSoPhoneHashMap;
    }

    public HashMap getEditSoAliasHashMap() {
        return editSoAliasHashMap;
    }

    public void setEditSoAliasHashMap(HashMap editSoAliasHashMap) {
        this.editSoAliasHashMap = editSoAliasHashMap;
    }

    
    /**
     * Will return the HashMap with Array of field configs
     * HashMap key - Node Name ex: Person, Address , Company...etc
     * HashMap value - FieldConfig[]
     * 
     * @return java.util.HashMap
     */
    public HashMap getAllNodeFieldConfigs() {
         HashMap newHashMap = new HashMap();

        try {

            if(screenObject!=null){
            String rootNodeName = screenObject.getRootObj().getName();
            ConfigManager.init();
            ObjectNodeConfig rootNodeObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(rootNodeName);

            //Build and array of field configs for the root node for ex: PERSON
            newHashMap.put(rootNodeName, rootNodeObjectNodeConfig.getFieldConfigs());
            
            
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();

            for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                newHashMap.put(childObjectNodeConfig.getName(), childObjectNodeConfig.getFieldConfigs());
            }
            }

        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC037: Failed to get all node field configs :{0}", ex.getMessage()),ex);
        }
         
        return newHashMap;
    }

    /**
     * 
     * @param allNodeFieldConfigs
     */
    public void setAllNodeFieldConfigs(HashMap allNodeFieldConfigs) {
        this.allNodeFieldConfigs = allNodeFieldConfigs;    
}


    /**
     * 
     * @return
     */
    public ArrayList getAllChildNodesNames() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            if (screenObject != null) {
                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();

                for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                    newArrayList.add(childObjectNodeConfig.getName());
                }
            }


        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("SRC038: Failed to get all child Node Names :{0}", ex.getMessage()),ex);
        }
        return newArrayList;
    }

    /**
     * 
     * @param allChildNodesNames
     */
    public void setAllChildNodesNames(ArrayList allChildNodesNames) {
        this.allChildNodesNames = allChildNodesNames;
    }

    public HashMap getAllNodeFieldConfigsSizes() {
         HashMap newHashMap = new HashMap();
        try {
            
            
            ConfigManager.init();
        
            if(screenObject!=null){
            
            String rootNodeName = screenObject.getRootObj().getName();
            ObjectNodeConfig rootNodeObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(rootNodeName);
            //Build and array of field configs for the root node for ex: PERSON
            newHashMap.put(rootNodeName, rootNodeObjectNodeConfig.getFieldConfigs());
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
            for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                newHashMap.put(childObjectNodeConfig.getName(), childObjectNodeConfig.getFieldConfigs().length*60);
            }
            }

        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC039: Failed to get all field configs sizes:{0}", ex.getMessage()),ex);
        }
        return newHashMap;
    }

    /**
     * 
     * @param allNodeFieldConfigs sizes
     */
    
    public void setAllNodeFieldConfigsSizes(HashMap allNodeFieldConfigs) {
        this.allNodeFieldConfigsSizes = allNodeFieldConfigs;
    }

    public ArrayList getRootNodeFieldConfigs() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            
            if (screenObject!=null){    
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig(screenObject.getRootObj().getName());
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                String strCheckEuid = screenObject.getRootObj().getName()+".EUID";               
                if(!strCheckEuid.equalsIgnoreCase(fieldConfig.getFullFieldName())) {
                  newArrayList.add(fieldConfig);
                }
            }
            }
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC040: Failed to get field configs :{0}", ex.getMessage()));
        }

        rootNodeFieldConfigs = newArrayList;//store all the fields in the arraylist
        return rootNodeFieldConfigs;
    }

    public void setRootNodeFieldConfigs(ArrayList rootNodeFieldConfigs) {
        this.rootNodeFieldConfigs = rootNodeFieldConfigs;
    }
    
    /**
     * 
     * @return
     */

    public ArrayList getAllEOChildNodesLists() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            
            if(screenObject!=null){
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
            
            for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                HashMap newHashMap = new HashMap();
                newHashMap.put("KEYLIST","EO" + childObjectNodeConfig.getName() + "ArrayList");
                newHashMap.put("NAME", childObjectNodeConfig.getName());
                newHashMap.put("FIELDCONFIGS", childObjectNodeConfig.getFieldConfigs());
                newArrayList.add(newHashMap);
            }
           }

        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC041: Failed to get EO child list:{0}", ex.getMessage()),ex);
        }
        return newArrayList;
    }

    /**
     * 
     * @param allChildNodesNameList
     */
    public void setAllEOChildNodesLists(ArrayList allChildNodesNameList) {
        this.allEOChildNodesLists = allChildNodesNameList;
    }

    public ArrayList getAllSOChildNodesLists() {
        ArrayList newArrayList = new ArrayList();
        try {
            ConfigManager.init();
            
            if(screenObject!=null){
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();

            for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                HashMap newHashMap = new HashMap();
                newHashMap.put("KEYLIST","SO" + childObjectNodeConfig.getName() + "ArrayList");
                    newHashMap.put("EDITKEYLIST","SOEDIT" + childObjectNodeConfig.getName() + "ArrayList");
                newHashMap.put("NAME", childObjectNodeConfig.getName());
                newHashMap.put("FIELDCONFIGS", childObjectNodeConfig.getFieldConfigs());
                newArrayList.add(newHashMap);
            }
            }
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC042: Failed to get child node list :{0}", ex.getMessage()),ex);
        }
        return newArrayList;
    }

    /**
     * 
     * @param allChildNodesNameList
     */
    public void setAllSOChildNodesLists(ArrayList allChildNodesNameList) {
        this.allSOChildNodesLists = allChildNodesNameList;
    }

    public int getEuidLength() {
        int euidLen = 0;
        try {
            euidLen = masterControllerService.getEuidLength();
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SRC043: Failed to get EUID length :{0}", ex.getMessage()),ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("SRC044: Failed to get EUID length :{0}", ex.getMessage()),ex);
        }
        return euidLen;
    }

    public void setEuidLength(int euidLength) {
        this.euidLength = euidLength;
    }

    public String getSystemCodeDescription(String systemCode) {
        return masterControllerService.getSystemDescription(systemCode);
    }

    public String getEnteredFieldValues() {
        return enteredFieldValues;
    }

    public void setEnteredFieldValues(String enteredFieldValues) {
        this.enteredFieldValues = enteredFieldValues;
    }


    public HashMap removeFieldInputMasking(HashMap valueEnteredMap, String objectType) { 
        //get Field Config for the root 
        FieldConfig[] fcRootArray = (FieldConfig[]) getAllNodeFieldConfigs().get(objectType);
        //loop through all the FieldConfig values 
        for (int k = 0; k < fcRootArray.length; k++) {
            String inputMask = fcRootArray[k].getInputMask();
            String userCode = fcRootArray[k].getUserCode();
            String constarintBy = fcRootArray[k].getConstraintBy();          
            //replace all the masked fields here
            if (inputMask != null && inputMask.length() > 0 && fcRootArray[k].getValueType() == ObjectField.OBJECTMETA_STRING_TYPE) {
                inputMask = inputMask.replace("D", ":");
                inputMask = inputMask.replace("L", ":");
                inputMask = inputMask.replace("A", ":");
                String unMaskedValueEntered = (String) valueEnteredMap.get(fcRootArray[k].getFullFieldName());
                String[] maskChars = inputMask.split(":");
                for (int i = 0; unMaskedValueEntered != null  && i <  maskChars.length; i++) {
                    unMaskedValueEntered = unMaskedValueEntered.replace(maskChars[i], "");
                }
                if (unMaskedValueEntered != null)  valueEnteredMap.put(fcRootArray[k].getFullFieldName(), unMaskedValueEntered);
                
            } else if (constarintBy != null && constarintBy.length() > 0 ) {
                 int refIndex = getReferenceFields(fcRootArray,constarintBy);
                 
                 String userInputMask = ValidationService.getInstance().getUserCodeInputMask(fcRootArray[refIndex].getUserCode(), (String) valueEnteredMap.get(fcRootArray[refIndex].getFullFieldName()));
              
                 
                if(userInputMask!=null){
                userInputMask = userInputMask.replace("D", ":");
                userInputMask = userInputMask.replace("L", ":");    
                userInputMask = userInputMask.replace("A", ":");   
                
              
                 String unMaskedValueEntered = (String) valueEnteredMap.get(fcRootArray[k].getFullFieldName());
                 
                String[] maskChars = userInputMask.split(":");
                for (int i = 0; unMaskedValueEntered != null  && i <  maskChars.length; i++) {                   
                    unMaskedValueEntered = unMaskedValueEntered.replace(maskChars[i], "");                    
                }
                if (unMaskedValueEntered != null)  valueEnteredMap.put(fcRootArray[k].getFullFieldName(), unMaskedValueEntered);
                }
            }
        }
        return valueEnteredMap;
    }

    public HashMap getDeactivatedSOHashMap() {
        return deactivatedSOHashMap;
    }

    public void setDeactivatedSOHashMap(HashMap deactivatedSOHashMap) {
        this.deactivatedSOHashMap = deactivatedSOHashMap;
    }
    
    public boolean isNumber(String thisValue,int type)  {
	   
        if (thisValue != null && thisValue.length() > 0) {
            try {
                if (type == 0) {  //Int value
                    Integer.parseInt(thisValue);
                } else if (type == 4) { // Long value
                    Long.parseLong(thisValue);
                } else if (type == 7) { // Float value
                    Float.parseFloat(thisValue);
                }
            } catch (Exception e) {
                 mLogger.error(mLocalizer.x("SRC075: Failed to check isNumber() :{0}", e.getMessage()),e);
                return false;
            }
        }
        return true;
    }
//(DDD)DDD-DDDD
    public boolean checkMasking(String thisValue, String masking)  {
        Character c;
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
                             mLogger.error(mLocalizer.x("SRC076: Failed to check masking :{0}", e.getMessage()),e);
            return false;
        }                
        return true;
    }

    public int getReferenceFields(FieldConfig[] childFieldConfigs,String refName) {
        //get the reference value index 
        for(int i = 0;i<childFieldConfigs.length;i++) {
            if(refName.equalsIgnoreCase(childFieldConfigs[i].getName()))  {
                return i;
            }
        }    
        return -1;
    }
    
}
