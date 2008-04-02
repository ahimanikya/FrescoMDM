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

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SourceAddHandler {

    //Array list for all person fields
    private ArrayList personFieldConfigs;
    //Array list for all address fields
    private ArrayList addressFieldConfigs;
    //Array list for all person fields
    private ArrayList phoneFieldConfigs;
    //Array list for all person fields
    private ArrayList aliasFieldConfigs;
    //Array list for all person fields
    
    
    private HashMap newSOHashMap = new HashMap();

    // Create fields for non updateable fields as per screen config array like LID, SYSTEM CODE and person fields
    private HashMap updateableFeildsMap =  new HashMap();    
    
    private HashMap addressFeildsMap =  new HashMap();    
    private HashMap phoneFeildsMap =  new HashMap();    
    private HashMap aliasFeildsMap =  new HashMap();    
    
    private HashMap addressFeildsMapExtra =  new HashMap();    
    private HashMap phoneFeildsMapExtra =  new HashMap();    
    private HashMap aliasFeildsMapExtra =  new HashMap();    

    //Hash map arraylist for single SO 
    private ArrayList singleAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Address
    private ArrayList singleAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Phone
    private ArrayList singlePhoneHashMapArrayList = new ArrayList();
    

    
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

        ArrayList fieldConfigArrayList  = getAddScreenConfigArray();
        Iterator fieldConfigArrayIter =  fieldConfigArrayList.iterator();
        int totalFields = fieldConfigArrayList.size();
        
        HashMap newFieldValuesMap = new HashMap();

        if (newSOEnteredFieldValues != null && newSOEnteredFieldValues.length() > 0) {
                String[] fieldNameValues = newSOEnteredFieldValues.split(">>");
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
           
            setNewSOHashMap(newFieldValuesMap); //set the root node values here
        
        //set the source code 
//        while(fieldConfigArrayIter.hasNext())  {
//             FieldConfig  fieldConfig = (FieldConfig) fieldConfigArrayIter.next();
//             String feildValue = (String) getUpdateableFeildsMap().get(fieldConfig.getName());                          
//            if (fieldConfig.getName().equalsIgnoreCase("SystemCode")) {
//                setSystemCode(feildValue);
//            }
//        }
        ArrayList newMinorObjectsList = new ArrayList();
        SourceHandler sourceHandler = new SourceHandler();
        ArrayList allChildNodesList  = sourceHandler.getAllChildNodesNames();
        if (minorObjectTotal != null && minorObjectTotal.length() > 0) {
            //System.out.println("==========minorObjectTotal=====" + minorObjectTotal);
            int totalMinorObjects = new Integer(minorObjectTotal).intValue();
            if (minorObjectsEnteredFieldValues != null && minorObjectsEnteredFieldValues.length() > 0) {
                minorObjectsEnteredFieldValues = minorObjectsEnteredFieldValues + ",";
                //System.out.println("minorObjectsEnteredFieldValues ==> : " + minorObjectsEnteredFieldValues + allChildNodesList.size());
                for (int c = 0; c < allChildNodesList.size(); c++) {
                    for (int mc = 0; mc < totalMinorObjects; mc++) {
                        String childObjectType = (String) allChildNodesList.get(c);
                        //System.out.println("childObjectType ==> : " + childObjectType );
                        String[] fieldNameValues = minorObjectsEnteredFieldValues.split(">>" + new Integer(mc).toString() + ">>" + childObjectType + ",");
                        ArrayList newMinorsList = new ArrayList();
                        HashMap newMap = new HashMap();
                        for (int i = 0; i < fieldNameValues.length; i++) {
                            String string = fieldNameValues[i];
                            String[] keyValues = string.split("##");
                            if (keyValues.length == 2) {
                                //System.out.println("Key " + keyValues[0] + "Value ==> : " + keyValues[1]);
                                minorObjectsHashMap.put(keyValues[0], keyValues[1]);
                                newMap.put(keyValues[0], keyValues[1]);
                                newMinorsList.add(keyValues[0] + "::" + keyValues[1]);
                            }
                        }

                        HashMap newMinorValues = new HashMap();
                        newMinorValues.put(childObjectType + new Integer(mc).toString(), newMinorsList);
                        newMinorObjectsList.add(newMinorValues);
                    }
                }
            }
            //System.out.println("minorObjectsHashMap ==>" + minorObjectsHashMap);
            //System.out.println("newMinorObjectsList ==>" + newMinorObjectsList);

            ArrayList finalminorObjectMaplist = new ArrayList();
            for (int mc = 0; mc < totalMinorObjects; mc++) {

                for (int i = 0; i < allChildNodesList.size(); i++) {
                    String childObjectType = (String) allChildNodesList.get(i);
                    for (int j = 0; j < newMinorObjectsList.size(); j++) {
                        HashMap minorMap = (HashMap) newMinorObjectsList.get(j);
                        ArrayList innerList = (ArrayList) minorMap.get(childObjectType + new Integer(mc).toString());
                        ///System.out.println("=======>" + innerList);
                        HashMap minorObjectMap = new HashMap();
                        if (innerList != null && innerList.size() > 0) {
                            for (int k = 0; k < innerList.size(); k++) {
                                String keyAndValue = (String) innerList.get(k);
                                String[] keyAndValueArray = keyAndValue.split("::");
                                for (int l = 0; l < keyAndValueArray.length; l++) {
                                    //System.out.println("key =====> " + keyAndValueArray[0] + "Value ==> " + keyAndValueArray[1]);
                                    if (keyAndValueArray[1] != null && keyAndValueArray[1].trim().length() == 0) {
                                        minorObjectMap.put(keyAndValueArray[0], null);
                                    } else {
                                        minorObjectMap.put(keyAndValueArray[0], keyAndValueArray[1]);
                                    }
                                }
                            }
                            //System.out.println("minorObjectMap =======>" + minorObjectMap);
                            setMinorObjectPrimaryValues(minorObjectMap, childObjectType);
                            finalminorObjectMaplist.add(minorObjectMap);
                        }
                    }
                }
            }
            //System.out.println("finalminorObjectMaplist =======>" + finalminorObjectMaplist);

            setNewSOMinorObjectsHashMapArrayList(finalminorObjectMaplist);
        }
        //convert the masked value here to 10 digit number
        String lid = getLID().replaceAll("-", ""); 
        setLID(lid);
       EnterpriseObject eoFinal = null;
        try {
            //validateLID(); // validate the combination of SOURCE/LID value
            
            //add SystemCode and LID value to the new Hash Map
            newSOHashMap.put(MasterControllerService.SYSTEM_CODE, getSystemCode());
            newSOHashMap.put(MasterControllerService.LID, getLID());

            //add the key as brand new in the hashmap
            newSOHashMap.put(MasterControllerService.HASH_MAP_TYPE,MasterControllerService.SYSTEM_OBJECT_BRAND_NEW);

            //System.out.println("NEW SO HASH MAP" + newSOHashMap);
            //add new SO to the arraylist
            getNewSOHashMapArrayList().add(newSOHashMap);

            //check the field values
            //checkMapValues(addressFeildsMap);
            //checkMapValues(aliasFeildsMap);


            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());
            //set Update user name her
            masterControllerService.setUpdateUserName((String) session.getAttribute("user"));
            //create systemobject start
            SystemObject createSystemObject = masterControllerService.createSystemObject(getSystemCode(), getLID(), newSOHashMap);
            
            //createSystemObject.setUpdateUser("eview");
   
            for(int i=0;i<getNewSOMinorObjectsHashMapArrayList().size();i++) {
                HashMap minorObjectMap = (HashMap) getNewSOMinorObjectsHashMapArrayList().get(i);
                masterControllerService.addMinorObject(createSystemObject, (String) minorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE), minorObjectMap);
            }
            
           
            
            masterControllerService.addSystemObject(createSystemObject);
            String summaryInfo = masterControllerService.getSummaryInfo();

                        
            //masterControllerService.createEnterpriseObject(createSystemObject);
            //finished creating SO and EO
            
            SystemObject newSystemObject = masterControllerService.getSystemObject(getSystemCode(), getLID());
            eoFinal  = masterControllerService.getEnterpriseObjectForSO(newSystemObject);

            
             
             // call mastercontrollerservice api to add minor objects all together
             //add minor objects to the newly created EO
             //eoFinal  = masterControllerService.save(eoNew, null, null, getNewSOMinorObjectsHashMapArrayList());
            
            //adding summary message after creating systemobjec
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,summaryInfo,summaryInfo));
        
        } catch (UserException ex) {   
            errorMessage = "Service Layer User Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            session.removeAttribute("validation");
            return this.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            errorMessage = "Service Layer Object Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            session.removeAttribute("validation");
            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            errorMessage = "Service Layer Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            session.removeAttribute("validation");
            return this.SERVICE_LAYER_ERROR;
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
            errorMessage = "Service Layer User Exception occurred while inserting audit log";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            errorMessage = "Service Layer Object Exception occurred while inserting audit log";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            errorMessage = "Service Layer Exception occurred while inserting audit log";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        }
        session.removeAttribute("validation");
      
        return this.ADD_SOURCE_SUCCESS;
    }
    
    
    /**
     * 
     * @param event
     */
    public void addSOAddress(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");
        
        //build array of address field hashmap and the keys for adding the new address objects
        addressFeildsMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
        addressFeildsMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");

        //set the address arraylist for displaying in the jsp page
        getSingleAddressHashMapArrayList().add(addressFeildsMap);
        
        //set the new hash map for so again for persiting
        setNewSOHashMap(newSOHashMap);
    }

    
    /**
     * 
     * @param event
     */
    public void removeSOAddress(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");

        HashMap remAddressMap = (HashMap) event.getComponent().getAttributes().get("remAddressMap");
        //set the search type as per the form
        this.singleAddressHashMapArrayList.remove(remAddressMap);
    }

    
    /**
     * 
     * @param event
     */
    public void addSOPhone(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");

        //build array of phone field hashmap
        phoneFeildsMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
        phoneFeildsMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");

        //set the hashmap for SL
        getNewSOMinorObjectsHashMapArrayList().add(phoneFeildsMap);
        
        //set the hashmap for displaying
        this.singlePhoneHashMapArrayList.add(getPhoneFieldConfigs());
    }
    
    public void removeSOPhone(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");
        HashMap remPhoneMap = (HashMap) event.getComponent().getAttributes().get("remPhoneMap");

        //set the search type as per the form
        this.singlePhoneHashMapArrayList.remove(remPhoneMap);
    }
    
    /**
     * 
     * @param event
     */
    public void addSOAlias(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");

        //build array of alias field hashmap and the keys for adding the new alias objects
        aliasFeildsMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
        aliasFeildsMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");

        //set the alias hash map for SL
        getNewSOMinorObjectsHashMapArrayList().add(aliasFeildsMap);
        
        //set the alias hash map for displaying
        this.singleAliasHashMapArrayList.add(getAliasFeildsMap());
    }

    public void removeSOAlias(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "Add");
        HashMap remAliasMap = (HashMap) event.getComponent().getAttributes().get("remAliasMap");

        //set the search type as per the form
        this.singleAliasHashMapArrayList.remove(remAliasMap);
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
        //System.out.println("====> " + lid + "SystemCodce" + getSystemCode());
        try {
            SystemObject systemObject = masterControllerService.getSystemObject(getSystemCode(), getLID());
            if(systemObject != null) {
                errorMessage = "Validation Failed. SystemCode/LID already found.";
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
                session.setAttribute("validation", "Validation Failed");
            } else if(systemObject == null) {
               errorMessage = "Validation Success.";
               //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,errorMessage,errorMessage));
               session.setAttribute("validation", "Validation Success");
            }
           
        } catch (ProcessingException ex) {
            errorMessage = "Processing Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            errorMessage = "UserException Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
            Logger.getLogger(SourceAddHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        }
        return this.VALIDATE_SOURCE_SUCCESS;
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
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
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
            ObjectNodeConfig personObjectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig("Person");
            FieldConfig[] allFeildConfigs = personObjectNodeConfig.getFieldConfigs();

            //Build Person Epath Arraylist
            for (int i = 0; i < allFeildConfigs.length; i++) {
                FieldConfig fieldConfig = allFeildConfigs[i];
                if (!"Person.EUID".equalsIgnoreCase(fieldConfig.getFullFieldName())) {
                    newArrayList.add(fieldConfig);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        personFieldConfigs = newArrayList;//store all the fields in the arraylist
        return personFieldConfigs;
    }

    public void setPersonFieldConfigs(ArrayList personFieldConfigs) {
        this.personFieldConfigs = personFieldConfigs;
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

    public HashMap getAddressFeildsMap() {
        return addressFeildsMap;
    }

    public void setAddressFeildsMap(HashMap addressFeildsMap) {
        this.addressFeildsMap = addressFeildsMap;
    }

    public HashMap getPhoneFeildsMap() {
        return phoneFeildsMap;
    }

    public void setPhoneFeildsMap(HashMap phoneFeildsMap) {
        this.phoneFeildsMap = phoneFeildsMap;
    }

    public HashMap getAliasFeildsMap() {
        return aliasFeildsMap;
    }

    public void setAliasFeildsMap(HashMap aliasFeildsMap) {
        this.aliasFeildsMap = aliasFeildsMap;
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

    public HashMap getAddressFeildsMapExtra() {
        return addressFeildsMapExtra;
    }

    public void setAddressFeildsMapExtra(HashMap addressFeildsMapExtra) {
        this.addressFeildsMapExtra = addressFeildsMapExtra;
    }

    public HashMap getPhoneFeildsMapExtra() {
        return phoneFeildsMapExtra;
    }

    public void setPhoneFeildsMapExtra(HashMap phoneFeildsMapExtra) {
        this.phoneFeildsMapExtra = phoneFeildsMapExtra;
    }

    public HashMap getAliasFeildsMapExtra() {
        return aliasFeildsMapExtra;
    }

    public void setAliasFeildsMapExtra(HashMap aliasFeildsMapExtra) {
        this.aliasFeildsMapExtra = aliasFeildsMapExtra;
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

}
