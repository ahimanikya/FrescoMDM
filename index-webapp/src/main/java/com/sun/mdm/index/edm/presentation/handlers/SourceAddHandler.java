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
 * SourceAddHandler.java 
 * Created on December 19, 2007, 21:45 PM
 * Author : Rajani Kanth, Samba
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.util.ResourceBundle;

public class SourceAddHandler {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
     
    private HashMap newSOHashMap = new HashMap();

    // Create fields for non updateable fields as per screen config array like LID, SYSTEM CODE and person fields
    private HashMap updateableFeildsMap =  new HashMap();    
    
     //Arraylist to display EDM driven search criteria Add screen.
    private ArrayList addScreenConfigArray;
    
    
    // Create fields for non updateable fields as per screen config array
    private String EUID;
    private String SystemCode;
    private String LID;
    private String create_start_date;
    private String create_end_date;
    private String create_start_time;
    private String create_end_time;
    private String Status;

    
    //Hash map arraylist for single SO  
    private ArrayList newSOHashMapArrayList = new ArrayList();
    //Hash map arraylist for new SO alias,Address,phone
    private ArrayList newSOMinorObjectsHashMapArrayList = new ArrayList();

   private static final String ADD_SOURCE_SUCCESS = "success";
   public static final String UPDATE_SUCCESS = "UPDATE_SUCCESS";
   private static final String VALIDATE_SOURCE_SUCCESS = "validationsuccess";
   private static final String SERVICE_LAYER_ERROR = "servicelayererror";

   private MasterControllerService masterControllerService = new MasterControllerService();

   //Get the session variable from faces context
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    //Get the session variable from faces context
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    //Get the screen object from session
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    String errorMessage = new String();

    //sub screen tab name for each tab on the source records page
    private String subScreenTab = "Add";

     /**
     * ResourceBundle
     */
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    String exceptionMessaage = bundle.getString("EXCEPTION_MSG");
    
    /**
     * all system codes
     */
    private String[][] allSystemCodes = masterControllerService.getSystemCodes();

   private String minorObjectsEnteredFieldValues  = new String();
   private String newSOEnteredFieldValues  = new String();
   private String minorObjectTotal = new String();
   private HashMap minorObjectsHashMap  = new HashMap();
    
   /** Creates a new instance of SourceHandler */
    public SourceAddHandler() {
    }


    public String addNewSO() {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");
         
        //convert the masked value here to 10 digit number
        String lid = getLID().replaceAll("-", ""); 
        setLID(lid);
        EnterpriseObject eoFinal = null;
        try {
            
            //add SystemCode and LID value to the new Hash Map
            newSOHashMap.put(MasterControllerService.SYSTEM_CODE, getSystemCode());
            newSOHashMap.put(MasterControllerService.LID, getLID());

            //add the key as brand new in the hashmap
            newSOHashMap.put(MasterControllerService.HASH_MAP_TYPE,MasterControllerService.SYSTEM_OBJECT_BRAND_NEW);

            //System.out.println("NEW SO HASH MAP" + newSOHashMap);
            //add new SO to the arraylist
            getNewSOHashMapArrayList().add(newSOHashMap);

   
            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());
            //set Update user name her
            masterControllerService.setUpdateUserName((String) session.getAttribute("user"));
            //create systemobject start
            SystemObject createSystemObject = masterControllerService.createSystemObject(getSystemCode(), getLID(), newSOHashMap);
            
            //add all minor objects here
            for(int i=0;i<getNewSOMinorObjectsHashMapArrayList().size();i++) {
                HashMap minorObjectMap = (HashMap) getNewSOMinorObjectsHashMapArrayList().get(i);
                masterControllerService.addMinorObject(createSystemObject, (String) minorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE), minorObjectMap);
            }
            
            masterControllerService.addSystemObject(createSystemObject);
            String summaryInfo = masterControllerService.getSummaryInfo();

                        
             //finished creating SO and EO
            
            SystemObject newSystemObject = masterControllerService.getSystemObject(getSystemCode(), getLID());
            eoFinal  = masterControllerService.getEnterpriseObjectForSO(newSystemObject);

            
            //adding summary message after creating systemobjec
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,summaryInfo,summaryInfo));
        
        } catch (UserException ex) {   
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC001: Service Layer User Exception has occurred"),ex);
             return SourceAddHandler.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC002: Service Layer Object Exception occurred"),ex);
             return SourceAddHandler.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC003: Service Layer Exception occurred"),ex);
             return SourceAddHandler.SERVICE_LAYER_ERROR;
        }
      
       //Insert Audit logs after adding the new System Object
       try {
       //String userName, String euid1, String euid2, String function, int screeneID, String detail
        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               eoFinal.getEUID(), 
                                               "",
                                               "EO View/Edit",
                                               new Integer(screenObject.getID()).intValue(),
                                               masterControllerService.getAuditMsg());
        } catch (UserException ex) {   
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC004: Service Layer User Exception occurred while inserting audit log"),ex);
            return SourceAddHandler.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC005: Service Layer Object Exception occurred while inserting audit log"),ex);
            return SourceAddHandler.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
             FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
             mLogger.error(mLocalizer.x("SRC006: Service Layer Exception occurred while inserting audit log"),ex);
             return SourceAddHandler.SERVICE_LAYER_ERROR;
        }
        session.removeAttribute("validation");
      
        return SourceAddHandler.ADD_SOURCE_SUCCESS;
    }
    
    
     /**
     * 
     * @return 
     */
    public String validateLID(){
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");
            //convert the masked value here to 10 digit number
        String lid = getLID().replaceAll("-", ""); 
        setLID(lid);
        try {
            SystemObject systemObject = masterControllerService.getSystemObject(getSystemCode(), getLID());
            if(systemObject != null) {
                errorMessage = bundle.getString("SystemCode_LID_Validation"); //"Validation Failed. SystemCode/LID already found.";
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
                mLogger.info(mLocalizer.x("SRC077: {0}",errorMessage));
                session.setAttribute("validation", "Validation Failed");
            } else if(systemObject == null) {
               errorMessage = bundle.getString("Validation_Success"); //"Validation Success.";
               //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,errorMessage,errorMessage));
               mLogger.info(mLocalizer.x("SRC078: {0}",errorMessage));
               session.setAttribute("validation", "Validation Success");
            }
           
        } catch (ProcessingException ex) {
           // errorMessage = "Processing Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
           mLogger.error(mLocalizer.x("SRC007: Processing Exception occurred :{0}",ex.getMessage()),ex);
            return SourceAddHandler.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            //errorMessage = "UserException Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
            mLogger.error(mLocalizer.x("SRC008: UserException Exception occurred:{0}",ex.getMessage()),ex);
            return SourceAddHandler.SERVICE_LAYER_ERROR;
        }
        return SourceAddHandler.VALIDATE_SOURCE_SUCCESS;
   }
public boolean validateSystemCodeLID(String LID,String systemCode){
        boolean validated = false;
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");

        //convert the masked value here to 10 digit number
        String lid = getLID().replaceAll("-", ""); 
        setLID(lid);
        try {
            SystemObject systemObject = masterControllerService.getSystemObject(systemCode, LID);
            if(systemObject != null) {
                validated = false;
            } else if(systemObject == null) {
                setLID(LID);
                setSystemCode(systemCode);
                validated = true;
            }
           
        } catch (ProcessingException ex) {
            
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
            mLogger.error(mLocalizer.x("SRC071: ProcessingException has occurred:{0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),exceptionMessaage));
           mLogger.error(mLocalizer.x("SRC072: UserException  has occurred:{0}",ex.getMessage()),ex);
        }
        return validated;
   }    
   public HashMap getNewSOHashMap() {
        return newSOHashMap;
    }

    public void setNewSOHashMap(HashMap newSOHashMap) {
        this.newSOHashMap = newSOHashMap;
    }

    public HashMap getUpdateableFeildsMap() {
        return updateableFeildsMap;
    }

    public void setUpdateableFeildsMap(HashMap updateableFeildsMap) {
        this.updateableFeildsMap = updateableFeildsMap;
    }

    public String getLID() {
        return LID;
    }

    public void setLID(String LID) {
        this.LID = LID;
    }

    public ArrayList getAddScreenConfigArray() {
        ArrayList basicSearchFieldConfigs;
        
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
            if ("Basic Search".equalsIgnoreCase(objSearchScreenConfig.getScreenTitle())) {
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
        return addScreenConfigArray;
    }

    public void setAddScreenConfigArray(ArrayList addScreenConfigArray) {
        this.addScreenConfigArray = addScreenConfigArray;
    }

     public ArrayList getNewSOHashMapArrayList() {
        return newSOHashMapArrayList;
    }

    public void setNewSOHashMapArrayList(ArrayList newSOHashMapArrayList) {
        this.newSOHashMapArrayList = newSOHashMapArrayList;
    }

    public ArrayList getNewSOMinorObjectsHashMapArrayList() {
        return newSOMinorObjectsHashMapArrayList;
    }

    public void setNewSOMinorObjectsHashMapArrayList(ArrayList newSOMinorObjectsHashMapArrayList) {
        this.newSOMinorObjectsHashMapArrayList = newSOMinorObjectsHashMapArrayList;
    }

    public String getEUID() {
        return EUID;
    }

    public void setEUID(String EUID) {
        this.EUID = EUID;
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

    public String getSystemCode() {
        return SystemCode;
    }

    public void setSystemCode(String SystemCode) {
        this.SystemCode = SystemCode;
    }

     
    private void setLidAndSystemCodes(ArrayList minorArrayList) {

        for (int i = 0; i < minorArrayList.size(); i++) {
            HashMap minorObjMap = (HashMap) minorArrayList.get(i);
            //set lid annd system code before submitting
            //add SystemCode and LID value to the new Hash Map FOR Address
            minorObjMap.put(MasterControllerService.SYSTEM_CODE, getSystemCode());
            minorObjMap.put(MasterControllerService.LID, getLID());
            getNewSOMinorObjectsHashMapArrayList().add(minorObjMap);
        }
    }

    private void setMinorObjectPrimaryValues(HashMap minorObjectHashMap,String minorObjectType) {
           
            minorObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
            minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, minorObjectType);
            minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, getSystemCode());
            minorObjectHashMap.put(MasterControllerService.LID, getLID());
    }

    private void unSetMinorObjectPrimaryValues(HashMap minorObjectHashMap) {
            minorObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, null);
            minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, null);
            minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, null);
            minorObjectHashMap.put(MasterControllerService.LID, null);
            getNewSOMinorObjectsHashMapArrayList().remove(minorObjectHashMap);
    }

    private boolean checkMapValues(HashMap minorMap) {
        boolean noValuesExists = true;
        Set minorMapKey = minorMap.keySet();
        Object[] objArray = minorMapKey.toArray();
        int count = 0;
        for (int i = 0; i < objArray.length; i++) {
            Object objectValue = minorMap.get(objArray[i]);
            if (objectValue == null) {
                count++;
            }
        }

        if (count == objArray.length) {
            noValuesExists = true;
        } else {
            noValuesExists = false;
        }
        return noValuesExists;
    }
    
    public String getSubScreenTab() {
        return subScreenTab;
    }

    public void setSubScreenTab(String subScreenTab) {
        this.subScreenTab = subScreenTab;
    }

    public String[][] getAllSystemCodes() {
        
        return allSystemCodes;
    }

    public void setAllSystemCodes(String[][] allSystemCodes) {
        this.allSystemCodes = allSystemCodes;
    }

    public String getMinorObjectsEnteredFieldValues() {
        return minorObjectsEnteredFieldValues;
    }

    public void setMinorObjectsEnteredFieldValues(String minorObjectsEnteredFieldValues) {
        this.minorObjectsEnteredFieldValues = minorObjectsEnteredFieldValues;
    }

    public HashMap getMinorObjectsHashMap() {
        return minorObjectsHashMap;
    }

    public void setMinorObjectsHashMap(HashMap minorObjectsHashMap) {
        this.minorObjectsHashMap = minorObjectsHashMap;
    }

    public String getMinorObjectTotal() {
        return minorObjectTotal;
    }

    public void setMinorObjectTotal(String minorObjectTotal) {
        this.minorObjectTotal = minorObjectTotal;
    }

    public String getNewSOEnteredFieldValues() {
        return newSOEnteredFieldValues;
    }

    public void setNewSOEnteredFieldValues(String newSOEnteredFieldValues) {
        this.newSOEnteredFieldValues = newSOEnteredFieldValues;
    }
//------------------------------Methods used on the View/Edit tab
    
/**
     * 
     * @param event
     */
    public void editLID(ActionEvent event){
        SystemObject singleSystemObjectEdit = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");
        SourceHandler sourceHandler = new SourceHandler();
        EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        //HashMap editSystemObjectMap = masterControllerService.getSystemObjectAsHashMap(singleSystemObjectEdit, personEPathArrayList);
        HashMap editSystemObjectMap = compareDuplicateManager.getSystemObjectAsHashMap(singleSystemObjectEdit, screenObject);
        SourceHandler sourceHandlerFaces = (SourceHandler)session.getAttribute("SourceHandler");


        session.setAttribute("singleSystemObjectLID", singleSystemObjectEdit);
        session.setAttribute("systemObjectMap", editSystemObjectMap);

        if("active".equalsIgnoreCase((String) editSystemObjectMap.get("Status")) ) {
           //set the single SO hash map for single so EDITING
           this.setNewSOHashMap(editSystemObjectMap);
        } else {
            sourceHandlerFaces.setDeactivatedSOHashMap(editSystemObjectMap);
        }

        //set address array list of hasmap for editing
        session.setAttribute("keyFunction", "editSO");
   }
    
    public String updateSO(){
        // set the tab name to be view/edit
        session.setAttribute("tabName", "View/Edit");

         try {

            SystemObject systemObject = (SystemObject) session.getAttribute("singleSystemObjectLID");
    
            //get the enterprise object for the system object
            EnterpriseObject sysEnterpriseObject = masterControllerService.getEnterpriseObjectForSO(systemObject);
            ArrayList editSOHashRootNodeMapList = new ArrayList();
            
            editSOHashRootNodeMapList.add((HashMap) this.getNewSOHashMap().get("SYSTEM_OBJECT"));

            SourceHandler sourceHandler = new SourceHandler();            
            ArrayList allChilds = sourceHandler.getAllChildNodesNames();            
            ArrayList editSOHashMinorObjectsList = new ArrayList();            
            //loop through all the minor objects
             for (int i = 0; i < allChilds.size(); i++) {
                 String minorObjType = (String) allChilds.get(i);
                 ArrayList thisMinorObjectList = (ArrayList) this.getNewSOHashMap().get("SOEDIT" + minorObjType + "ArrayList");
                 for (int j = 0; j < thisMinorObjectList.size(); j++) {
                     HashMap oldHashMap = (HashMap) thisMinorObjectList.get(j);
                     sourceHandler.removeFieldInputMasking(oldHashMap, minorObjType);

                     HashMap newHashMap = new HashMap();
                     Object[] keysSet = oldHashMap.keySet().toArray();
                     for (int k = 0; k < keysSet.length; k++) {
                         String key = (String) keysSet[k];
                         if (!"listIndex".equalsIgnoreCase(key) &&
                             !"SYS".equalsIgnoreCase(key) &&
                             !"MOT".equalsIgnoreCase(key) &&
                             !"editThisID".equalsIgnoreCase(key) &&
                             !"minorObjSave".equalsIgnoreCase(key)) {
                             newHashMap.put(key, oldHashMap.get(key));
                         }
                     }
                     editSOHashMinorObjectsList.add(newHashMap);
                 }

             }
            
            //call modifySystemObjects to update the
            masterControllerService.save(sysEnterpriseObject, null, editSOHashRootNodeMapList, editSOHashMinorObjectsList);
            
            //add so fields here

            //Keep the updated SO in the session again
            SystemObject updatedSystemObject = masterControllerService.getSystemObject(systemObject.getSystemCode(), systemObject.getLID());

            session.setAttribute("singleSystemObjectLID", updatedSystemObject);
            session.setAttribute("keyFunction", "editSO");
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("SRC073: Exception has occurred:{0}",ex.getMessage()),ex);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),QwsUtil.getRootCause(ex).getMessage()));
            return SERVICE_LAYER_ERROR;
        }
        
        return UPDATE_SUCCESS;
   }    
 
}
