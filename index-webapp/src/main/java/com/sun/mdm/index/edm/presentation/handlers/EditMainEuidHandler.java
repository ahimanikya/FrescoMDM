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
 * Created on December 21, 2007, 
 * Author : Anil, RajaniKanth
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rajani Kanth
 */
public class EditMainEuidHandler {

    private static final String EDIT_SUCCESS = "EO_EDIT_SUCCESS";
    
    //Hash map for single EO  for view
    private ArrayList singleEOHashMapArrayList = new ArrayList();

    //Hash map arraylist for single EO 
    private ArrayList singleAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single EO Address
    private ArrayList singleAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single EO Phone
    private ArrayList singlePhoneHashMapArrayList = new ArrayList();
    

    
    //Hash map for singl EO  for EDITING
    private HashMap editSingleEOHashMap = new HashMap();

    //Hash map for single EO Address  for EDITING
    private HashMap editEOAddressHashMap = new HashMap();
    
    //Hash map for EO Phone  for EDITING
    private HashMap editEOPhoneHashMap = new HashMap();

    //Hash map for EO Alias  for EDITING
    private HashMap editEOAliasHashMap = new HashMap();
    

    //Hash map arraylist for  SO 
    private ArrayList eoSystemObjects;

    //Hash map arraylist for  SO sl
    private ArrayList eoSystemObjectSL;
    
    private int eoSystemObjectsSize = 0;
    

    //Hash map arraylist for edit SO alias,Address,phone
    private ArrayList editSOMinorObjectsHashMapArrayList = new ArrayList();
    
    private static final String EDIT_EO_SUCCESS = "success";

    //Hash map arraylist for edit EO alias,Address,phone (SBR)
    private ArrayList changedSBRArrayList = new ArrayList();
    
    //Hash map arraylist for edit SO 
    private ArrayList editSOHashMapArrayList = new ArrayList();
    
    MasterControllerService masterControllerService = new MasterControllerService();

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    
    private String errorMessage;

    private static final String SERVICE_LAYER_ERROR = "servicelayererror";
    
    SourceHandler sourceHandler = new SourceHandler();
    
    EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();

    //Adding the following variablefor getting the select options if the FieldConfig type is "Menu List"
    private ArrayList<SelectItem> systemCodes = new ArrayList();

    private String newSoSystemCode;

    private String newSoLID;
    
    //Hash map arraylist for newSO SO 
    private HashMap newSOHashMap  = new HashMap();

    //Hash map for single EO Address  for New SO
    private HashMap newSOAddressHashMap = new HashMap();
    
    //Hash map for EO Phone  for New SO
    private HashMap newSOPhoneHashMap = new HashMap();

    //Hash map for EO Alias  for New SO
    private HashMap newSOAliasHashMap = new HashMap();
    

    //Hash map arraylist for newSO SO 
    private ArrayList newSOHashMapArrayList = new ArrayList();

    //Hash map arraylist for newSO SO 
    private ArrayList newSOAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO Address
    private ArrayList newSOAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO Phone
    private ArrayList newSOPhoneHashMapArrayList = new ArrayList();
    
    
    //Hash map arraylist for new SO alias,Address,phone
    private ArrayList newSOMinorObjectsHashMapArrayList = new ArrayList();

    private String linkedSoWithLidByUser;

    private HashMap linkedFieldsHashMapByUser = new HashMap();

    private HashMap unLinkedFieldsHashMapByUser = new HashMap();
    
    private HashMap linkedFieldsHashMapFromDB = new HashMap();
    
    private HashMap lockedFieldsHashMapFromDB = new HashMap();
    
    private HashMap linkedSOFieldsHashMapFromDB = new HashMap();
    
    
    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

    //Adding the following variables for getting the select options if the FieldConfig type is "Menu List"
    
    private ArrayList<SelectItem> eoSystemObjectCodesWithLids = new ArrayList();

    private String hiddenLinkFields = new String();
    
    private String hiddenUnLinkFields = new String();
    
    private ArrayList allNodeFieldConfigs = new ArrayList();
    /** Creates a new instance of EditMainEuidHandler */
    public EditMainEuidHandler() {
    }

    public String performSubmit()  {
        try {
            if (this.getHiddenLinkFields() != null && this.getHiddenLinkFields().trim().length() > 0) {
//                String[] allLinks = this.getHiddenLinkFields().split(":");
                //save all the links
                saveLinksSelected();
            }

            System.out.println("==> UN LINKED FIELDS ===> :" + this.getHiddenUnLinkFields());
            if (this.getHiddenUnLinkFields() != null && this.getHiddenUnLinkFields().trim().length() > 0) {
                //String[] allUnLinks = this.getHiddenUnLinkFields().split("##");
                //save all the un links
                saveUnLinksSelected();
            }

            EnterpriseObject updateEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            
            
            //setUpdatedEOFields(masterControllerService.getEnterpriseObject(updateEnterpriseObject.getEUID())); //set the updated values here
            
            // Keep the EO in session
            //build an array of modified system object arrays  
            buildChangedSystemObjects();
      
            checkAndBuildModifiedSBRValues(updateEnterpriseObject);
  
            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());

//            System.out.println("===> : this.editSingleEOHashMap" + this.editSingleEOHashMap.get("ENTERPRISE_OBJECT"));
            ArrayList newArrayListSbr = new ArrayList();
            newArrayListSbr.add((HashMap)this.editSingleEOHashMap.get("ENTERPRISE_OBJECT"));
            setChangedSBRArrayList(newArrayListSbr);
            //this.changedSBRArrayList.add((HashMap) this.editSingleEOHashMap.get("ENTERPRISE_OBJECT"));
            //System.out.println("===> : this.changedSBRArrayList" + this.changedSBRArrayList);
            
            
            //EDIT the EO and its system objects here
            EnterpriseObject eoFinal = masterControllerService.save(updateEnterpriseObject,   //Enterprise Object
                                                                    newArrayListSbr,  //Changed SBR hashmap array
                                                                    null, //this.editSOHashMapArrayList, //Changed/New System objects hashmap array
                                                                    null  //this.editSOMinorObjectsHashMapArrayList // ChangedNew Minor Objects hashmap Array
                                                                    ); 
            //Get the Summary Info after the changes
            String summaryInfo = masterControllerService.getSummaryInfo();
                     
            //adding summary message after creating/editing systemobjects along with EO
            if(summaryInfo != null && summaryInfo.length() > 0) {
               FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,summaryInfo,summaryInfo));
            }

            String successMessage  = "EUID : \""+eoFinal.getEUID()+"\" details have been successfully updated";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,successMessage,successMessage));
            
            setUpdatedEOFields(masterControllerService.getEnterpriseObject(updateEnterpriseObject.getEUID())); //set the updated values here

//        } catch (ObjectException ex) {
//            errorMessage = "ObjectException occurred";
//            
//            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.toString()));
//            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
//            return this.SERVICE_LAYER_ERROR;
//        } catch (ValidationException ex) {
//            errorMessage = "ValidationException occurred";
//            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.toString()));
//            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
//            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            errorMessage = "Exception occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.toString()));
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        }
        
        return this.EDIT_SUCCESS;
    }

    public void setEditEOFields(ActionEvent event ) {
        try {
            String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject editEnterpriseObject = masterControllerService.getEnterpriseObject(euid);

            setUpdatedEOFields(editEnterpriseObject); //set the updated values here

        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void deactivateEOSO(ActionEvent event ) {
        try {
            HashMap soHashMap = (HashMap) event.getComponent().getAttributes().get("eoSystemObjectMapVE");

            ////System.out.println("in action even for Deactivate SO" + soHashMap);
            SystemObject systemObject = masterControllerService.getSystemObject((String) soHashMap.get("SystemCode"), (String) soHashMap.get("LID"));
            //call mastercontroller service to deactivate the system object
            masterControllerService.deactivateSystemObject(systemObject);
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     public void activateEOSO(ActionEvent event ) {
        try {
            HashMap soHashMap = (HashMap) event.getComponent().getAttributes().get("eoSystemObjectMapVE");

            ////System.out.println("in action even for activate SO" + soHashMap);
            SystemObject systemObject = masterControllerService.getSystemObject((String) soHashMap.get("SystemCode"), (String) soHashMap.get("LID"));
            //call mastercontroller service to deactivate the system object
            masterControllerService.activateSystemObject(systemObject);
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     public void addEOAddress(ActionEvent event ) {
        ////System.out.println("in action even for add EO address hashmap" + this.editEOAddressHashMap);
         this.editEOAddressHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");// set MINOR_OBJECT_TYPE
         this.editEOAddressHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
         
         this.changedSBRArrayList.add(this.editEOAddressHashMap);

         //set the search type as per the form
        this.singleAddressHashMapArrayList.add(this.editEOAddressHashMap);
     }

     public void removeEOAddress(ActionEvent event ) {
         HashMap remAddressMap = (HashMap) event.getComponent().getAttributes().get("remAddressMap");

         this.editEOAddressHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE

         this.changedSBRArrayList.add(this.editEOAddressHashMap);

        ////System.out.println("in action even for remove ADDRESS hashmap" + remAddressMap);
        //set the search type as per the form
        this.singleAddressHashMapArrayList.remove(remAddressMap);
     }
     
     
     public void addEOPhone(ActionEvent event ) {
         this.editEOPhoneHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");// set MINOR_OBJECT_TYPE
         this.editEOPhoneHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
         this.changedSBRArrayList.add(this.editEOPhoneHashMap);

         ////System.out.println("in action even for add EO phone  hashmap" + this.editEOPhoneHashMap);
         //set the search type as per the form
         this.singlePhoneHashMapArrayList.add(this.editEOPhoneHashMap);
     }

     public void removeEOPhone(ActionEvent event ) {
         HashMap remPhoneMap = (HashMap) event.getComponent().getAttributes().get("remPhoneMap");

         this.editEOPhoneHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE

         this.changedSBRArrayList.add(this.editEOPhoneHashMap);

         ////System.out.println("in action even for remove phone hashmap" + remPhoneMap);
        //set the search type as per the form
        this.singlePhoneHashMapArrayList.remove(remPhoneMap);
     }

     public void addEOAlias(ActionEvent event ) {
         this.editEOAliasHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");// set MINOR_OBJECT_TYPE
         this.editEOAliasHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
         this.changedSBRArrayList.add(this.editEOAliasHashMap);
         ////System.out.println("in action even for add EO alias hashmap" + this.editEOAliasHashMap);
         //set the search type as per the form
         this.singleAliasHashMapArrayList.add(this.editEOAliasHashMap);
     }

     public void removeEOAlias(ActionEvent event ) {
        HashMap remAliasMap = (HashMap) event.getComponent().getAttributes().get("remAliasMap");

        this.editEOAliasHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE

        ////System.out.println("in action even for remove alias hashmap" + remAliasMap);
        //set the search type as per the form
        this.singleAliasHashMapArrayList.remove(remAliasMap);
         
     }

//add new SO fields here
     /**
     * 
      * @param event
      * @return 
     */
    public String addNewSO() {
        try {
            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());
            EnterpriseObject updateEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            
            //System.out.println("=> IN ADD NEW SO METHOD ===> : " + this.newSOHashMap + this.newSoSystemCode + "==> : LID" + this.newSoLID);
            this.newSoLID = this.newSoLID.replaceAll("-", "");
            
            //add SystemCode and LID value to the new Hash Map
            newSOHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);
            newSOHashMap.put(MasterControllerService.LID, this.newSoLID);

            //take care of SSN masking here
            String ssn  = (String) newSOHashMap.get("Person.SSN");
            if(newSOHashMap.get("Person.SSN") != null) {
               if(ssn.length() > 1 ) {
                 ssn = ssn.replaceAll("-", "");
                 newSOHashMap.put("Person.SSN",ssn);
               }  else {
                 newSOHashMap.put("Person.SSN",null);
               }
            }
            
            //add the key as brand new in the hashmap
            newSOHashMap.put(MasterControllerService.HASH_MAP_TYPE,MasterControllerService.SYSTEM_OBJECT_BRAND_NEW);
           
            //add new SO to the arraylist
            this.newSOHashMapArrayList.add(newSOHashMap);

            if (this.newSOAddressHashMapArrayList.size() > 0) {
                    for (int k = 0; k < newSOAddressHashMapArrayList.size(); k++) {
                        HashMap addressHashMap = (HashMap) newSOAddressHashMapArrayList.get(k);
                        addressHashMap.put(MasterControllerService.LID, this.newSoLID);// set LID here 
                        addressHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);// set System code here 
                        addressHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");// set MINOR_OBJECT_TYPE
                        addressHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
                        this.newSOMinorObjectsHashMapArrayList.add(addressHashMap);
                    }
                }


                //set phone array list of hasmap for editing
                if (this.newSOPhoneHashMapArrayList.size()> 0) {
                    for (int k = 0; k < this.newSOPhoneHashMapArrayList.size() ; k++) {
                        HashMap phoneHashMap = (HashMap) this.newSOPhoneHashMapArrayList.get(k);
                        phoneHashMap.put(MasterControllerService.LID, this.newSoLID);// set LID here 
                        phoneHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);// set System code here 
                        phoneHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");// set MINOR_OBJECT_TYPE
                        phoneHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
                        this.newSOMinorObjectsHashMapArrayList.add(phoneHashMap);
                    }
                }

                //set alias array list of hasmap for editing
                if (this.newSOAliasHashMapArrayList.size() > 0) {
                    for (int k = 0; k < this.newSOAliasHashMapArrayList.size(); k++) {
                        HashMap aliasHashMap = (HashMap) this.newSOAliasHashMapArrayList.get(k);
                        aliasHashMap.put(MasterControllerService.LID, this.newSoLID);// set LID here 
                        aliasHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);// set System code here 
                        aliasHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");// set MINOR_OBJECT_TYPE
                        aliasHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
                        this.newSOMinorObjectsHashMapArrayList.add(aliasHashMap);
                    }
                }

            //SystemObject createSystemObject = masterControllerService.createSystemObject(this.newSoSystemCode, this.newSoLID, newSOHashMap);
            ////System.out.println("===> createSystemObject" + createSystemObject);
            //createSystemObject.setUpdateUser("eview");
            

            // Keep the EO in session
            //System.out.println("===> enterpriseObject" + updateEnterpriseObject);
            //Add new SO here
            EnterpriseObject eoFinal = masterControllerService.save(updateEnterpriseObject, 
                                                                    null, 
                                                                    this.newSOHashMapArrayList, 
                                                                    this.newSOMinorObjectsHashMapArrayList);


            String summaryInfo = masterControllerService.getSummaryInfo();
            //System.out.println("===> summaryInfo" + summaryInfo);
            

            //adding summary message after creating/editing systemobjects along with EO
            if (summaryInfo != null && summaryInfo.length() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summaryInfo, summaryInfo));
            }
            //
            unSetMinorObjectPrimaryValues(this.newSOAddressHashMapArrayList);
            unSetMinorObjectPrimaryValues(this.newSOPhoneHashMapArrayList);
            unSetMinorObjectPrimaryValues(this.newSOAliasHashMapArrayList);
            this.newSOHashMap.clear();
            
            setUpdatedEOFields(eoFinal); //set the updated values here
            
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ObjectException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.EDIT_SUCCESS;
    }
     
     /**
     * 
     * @param event
     */
    public void addNewSOAddress(ActionEvent event) {

        ////System.out.println("in action even for add EO address hashmap" + this.newSOAddressHashMap);
         this.newSOAddressHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");// set MINOR_OBJECT_TYPE
         this.newSOAddressHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE


        //set the address arraylist for displaying in the jsp page
        this.newSOAddressHashMapArrayList.add(this.newSOAddressHashMap);

        //set the address arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAddressHashMap);
        
    }

    
    /**
     * 
     * @param event
     */
    public void removeNewSOAddress(ActionEvent event) {

        HashMap remAddressMap = (HashMap) event.getComponent().getAttributes().get("remAddressMap");

        this.newSOAddressHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE


        //set the address arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAddressHashMap);

        //set the search type as per the form
        this.newSOAddressHashMapArrayList.remove(remAddressMap);
    }
    
         /**
     * 
     * @param event
     */
    public void addNewSOPhone(ActionEvent event) {

        ////System.out.println("in action even for add EO phone hashmap" + this.newSOPhoneHashMap);
         this.newSOPhoneHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");// set MINOR_OBJECT_TYPE
         this.newSOPhoneHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE


        //set the phone arraylist for displaying in the jsp page
        this.newSOPhoneHashMapArrayList.add(this.newSOPhoneHashMap);

        //set the phone arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOPhoneHashMap);
        
    }

    
    /**
     * 
     * @param event
     */
    public void removeNewSOPhone(ActionEvent event) {

        HashMap remPhoneMap = (HashMap) event.getComponent().getAttributes().get("remPhoneMap");

        this.newSOPhoneHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE


        //set the phone arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOPhoneHashMap);

        //set the search type as per the form
        this.newSOPhoneHashMapArrayList.remove(remPhoneMap);
    }
    /**
     * 
     * @param event
     */
    public void addNewSOAlias(ActionEvent event) {

        ////System.out.println("in action even for add EO alias hashmap" + this.newSOAliasHashMap);
         this.newSOAliasHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");// set MINOR_OBJECT_TYPE
         this.newSOAliasHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE


        //set the alias arraylist for displaying in the jsp page
        this.newSOAliasHashMapArrayList.add(this.newSOAliasHashMap);

        //set the alias arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAliasHashMap);
        
    }

    
    /**
     * 
     * @param event
     */
    public void removeNewSOAlias(ActionEvent event) {

        HashMap remAliasMap = (HashMap) event.getComponent().getAttributes().get("remAliasMap");

        this.newSOAliasHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE


        //set the alias arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAliasHashMap);

        //set the search type as per the form
        this.newSOAliasHashMapArrayList.remove(remAliasMap);
    }

     
     
    public ArrayList getSingleEOHashMapArrayList() {
        return singleEOHashMapArrayList;
    }

    public void setSingleEOHashMapArrayList(ArrayList singleEOHashMapArrayList) {
        this.singleEOHashMapArrayList = singleEOHashMapArrayList;
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



    public HashMap getEditSingleEOHashMap() {
        return editSingleEOHashMap;
    }

    public void setEditSingleEOHashMap(HashMap editSingleEOHashMap) {
        this.editSingleEOHashMap = editSingleEOHashMap;
    }

    public HashMap getEditEOAddressHashMap() {
        return editEOAddressHashMap;
    }

    public void setEditEOAddressHashMap(HashMap editEOAddressHashMap) {
        this.editEOAddressHashMap = editEOAddressHashMap;
    }

    public HashMap getEditEOPhoneHashMap() {
        return editEOPhoneHashMap;
    }

    public void setEditEOPhoneHashMap(HashMap editEOPhoneHashMap) {
        this.editEOPhoneHashMap = editEOPhoneHashMap;
    }

    public HashMap getEditEOAliasHashMap() {
        return editEOAliasHashMap;
    }

    public void setEditEOAliasHashMap(HashMap editEOAliasHashMap) {
        this.editEOAliasHashMap = editEOAliasHashMap;
    }

    public ArrayList getEoSystemObjects() {
        return eoSystemObjects;
    }

    public void setEoSystemObjects(ArrayList eoSystemObjects) {
        this.eoSystemObjects = eoSystemObjects;
    }

    public ArrayList getEditSOMinorObjectsHashMapArrayList() {
        return editSOMinorObjectsHashMapArrayList;
    }

    public void setEditSOMinorObjectsHashMapArrayList(ArrayList editSOMinorObjectsHashMapArrayList) {
        this.editSOMinorObjectsHashMapArrayList = editSOMinorObjectsHashMapArrayList;
    }

    public ArrayList getChangedSBRArrayList() {
        return changedSBRArrayList;
    }

    public void setChangedSBRArrayList(ArrayList changedSBRArrayList) {
        this.changedSBRArrayList = changedSBRArrayList;
    }

    public ArrayList getEditSOHashMapArrayList() {
        return editSOHashMapArrayList;
    }

    public void setEditSOHashMapArrayList(ArrayList editSOHashMapArrayList) {
        this.editSOHashMapArrayList = editSOHashMapArrayList;
    }

    private void buildChangedSystemObjects() {
            //set EO system objects here.
            Object[] soArrayObj = this.eoSystemObjects.toArray();
            SourceHandler sourceHandler  = new SourceHandler();
            Object[] personFieldConfigsObj = sourceHandler.getPersonFieldConfigs().toArray();
            for (int i = 0; i < soArrayObj.length; i++) {
                HashMap objectMap = (HashMap) soArrayObj[i];
                HashMap systemObjectMap = (HashMap) objectMap.get("SYSTEM_OBJECT");

                
                //take care of SSN masking here
                String ssnField = screenObject.getRootObj().getName() + ".SSN";
                ////System.out.println("SSN FIELD==>: " + ssnField);
                String ssn = (String) systemObjectMap.get(ssnField);
                if (systemObjectMap.get(ssnField) != null) {
                    if (ssn.length() > 1) {
                        ssn = ssn.replaceAll("-", "");
                        systemObjectMap.put(ssnField, ssn);
                    } else {
                        systemObjectMap.put(ssnField, null);
                    }
                }

                editSOHashMapArrayList.add(systemObjectMap); // adding SO hashmaps for editing here.
            }    
        
    }

    private void checkAndBuildModifiedSBRValues(EnterpriseObject updateEnterpriseObjectArg) {
                       
        try {

            HashMap eoOldMap = masterControllerService.getEnterpriseObjectAsHashMap(updateEnterpriseObjectArg, personEPathArrayList);

            eoOldMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

            //all field configs as per the root objects
            ArrayList allFieldConfigs = sourceHandler.getAllFieldConfigs();

            String ePathName = new String();
            String rootName  = screenObject.getRootObj().getName();
            
            
//            System.out.println("Before Updating (size)" + this.editSingleEOHashMap.size());
            HashMap eoHashMap = (HashMap) this.editSingleEOHashMap.get("ENTERPRISE_OBJECT");
            HashMap newUpdatedMap = new HashMap();
            System.out.println( "befoew eoHashMap ===> : " +  eoHashMap);
                if (MasterControllerService.SBR_UPDATE.equalsIgnoreCase((String) eoHashMap.get(MasterControllerService.HASH_MAP_TYPE))) {
                    for (int j = 0; j < allFieldConfigs.size(); j++) {
                        FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                        ePathName = fieldConfig.getFullFieldName();

                        String oldValue = (String) eoOldMap.get(ePathName);
                        String newValue = (String) eoHashMap.get(ePathName);
                        //check if no value entered put null into the value
                        if ((oldValue == null && (newValue != null && newValue.trim().length() == 0) )) {
                            eoHashMap.put(ePathName,null);
                        }
                        //check if old value is equal to new value
                        if ((oldValue == null && newValue == null)) {
                            //System.out.println(ePathName + "ALL NULL ePathName===> : "   +eoHashMap.get(ePathName));
                            eoHashMap.remove(ePathName);
//                        } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
//                            System.out.println("oldValue != null && newValue != null===> : " + ePathName  +eoHashMap);
//                            eoHashMap.remove(ePathName);
                        } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
//                            System.out.println(ePathName + "oldValue != null && newValue != null ePathName===> : " + eoHashMap.get(ePathName));
                            eoHashMap.remove(ePathName);
                        }
                    }
                }
            
            Object[] keys = eoHashMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String)keys[i];
                if(eoHashMap.get(key) != null) {
                    newUpdatedMap.put(key,eoHashMap.get(key));
                }
            }
            this.editSingleEOHashMap.put("ENTERPRISE_OBJECT",newUpdatedMap);
            
            System.out.println("after===> : "  + "AFTER  TOTAL KEYS" + this.editSingleEOHashMap);
            

        } catch (ObjectException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EPathException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        
        
    }

    public ArrayList<SelectItem> getSystemCodes() {
        String[][] systemCodesWithLIDMasking = masterControllerService.getSystemCodes();
        String[] pullDownListItems = systemCodesWithLIDMasking[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            SelectItem selectItem = new SelectItem();
            //////System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
            selectItem.setLabel(pullDownListItems[i]);
            selectItem.setValue(pullDownListItems[i]);
            newArrayList.add(selectItem);
        }
        systemCodes = newArrayList;
        return systemCodes;
    }

    public void setSystemCodes(ArrayList<SelectItem> systemCodes) {
        this.systemCodes = systemCodes;
    }

    public String getNewSoSystemCode() {
        return newSoSystemCode;
    }

    public void setNewSoSystemCode(String newSoSystemCode) {
        this.newSoSystemCode = newSoSystemCode;
    }

    public String getNewSoLID() {
        return newSoLID;
    }

    public void setNewSoLID(String newSoLID) {
        this.newSoLID = newSoLID;
    }

    public void toUpdatedEuidDetails(ActionEvent event) {
        try {

            session.removeAttribute("enterpriseArrayList");

            EnterpriseObject eo = (EnterpriseObject) session.getAttribute("editEnterpriseObject");

            EnterpriseObject eoUpdated = masterControllerService.getEnterpriseObject(eo.getEUID());

            ArrayList newArrayList = new ArrayList();
            newArrayList.add(eoUpdated);
            
            session.setAttribute("enterpriseArrayList", newArrayList);
            
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public HashMap getNewSOHashMap() {
        return newSOHashMap;
    }

    public void setNewSOHashMap(HashMap newSOHashMap) {
        this.newSOHashMap = newSOHashMap;
    }

    public HashMap getLinkedFieldsHashMapByUser() {
        return linkedFieldsHashMapByUser;
    }

    public void setLinkedFieldsHashMapByUser(HashMap linkedFieldsHashMapByUser) {
        this.linkedFieldsHashMapByUser = linkedFieldsHashMapByUser;
    }

    public HashMap getLinkedFieldsHashMapFromDB() {
        return linkedFieldsHashMapFromDB;
    }

    public void setLinkedFieldsHashMapFromDB(HashMap linkedFieldsHashMapFromDB) {
        this.linkedFieldsHashMapFromDB = linkedFieldsHashMapFromDB;
    }

    public HashMap getUnLinkedFieldsHashMapByUser() {
        return unLinkedFieldsHashMapByUser;
    }

    public void setUnLinkedFieldsHashMapByUser(HashMap unLinkedFieldsHashMapByUser) {
        this.unLinkedFieldsHashMapByUser = unLinkedFieldsHashMapByUser;
    }

    public ArrayList<SelectItem> getEoSystemObjectCodesWithLids() {
        return eoSystemObjectCodesWithLids;
    }

    public void setEoSystemObjectCodesWithLids(ArrayList<SelectItem> eoSystemObjectCodesWithLids) {
        this.eoSystemObjectCodesWithLids = eoSystemObjectCodesWithLids;
    }

    public String getLinkedSoWithLidByUser() {
        return linkedSoWithLidByUser;
    }

    public void setLinkedSoWithLidByUser(String linkedSoWithLidByUser) {
        this.linkedSoWithLidByUser = linkedSoWithLidByUser;
    }

    public ArrayList getNewSOAddressHashMapArrayList() {
        return newSOAddressHashMapArrayList;
    }

    public void setNewSOAddressHashMapArrayList(ArrayList newSOAddressHashMapArrayList) {
        this.newSOAddressHashMapArrayList = newSOAddressHashMapArrayList;
    }

    public ArrayList getNewSOAliasHashMapArrayList() {
        return newSOAliasHashMapArrayList;
    }

    public void setNewSOAliasHashMapArrayList(ArrayList newSOAliasHashMapArrayList) {
        this.newSOAliasHashMapArrayList = newSOAliasHashMapArrayList;
    }

    public ArrayList getNewSOPhoneHashMapArrayList() {
        return newSOPhoneHashMapArrayList;
    }

    public void setNewSOPhoneHashMapArrayList(ArrayList newSOPhoneHashMapArrayList) {
        this.newSOPhoneHashMapArrayList = newSOPhoneHashMapArrayList;
    }

    public ArrayList getNewSOMinorObjectsHashMapArrayList() {
        return newSOMinorObjectsHashMapArrayList;
    }

    public void setNewSOMinorObjectsHashMapArrayList(ArrayList newSOMinorObjectsHashMapArrayList) {
        this.newSOMinorObjectsHashMapArrayList = newSOMinorObjectsHashMapArrayList;
    }

    public HashMap getNewSOAddressHashMap() {
        return newSOAddressHashMap;
    }

    public void setNewSOAddressHashMap(HashMap newSOAddressHashMap) {
        this.newSOAddressHashMap = newSOAddressHashMap;
    }

    public HashMap getNewSOPhoneHashMap() {
        return newSOPhoneHashMap;
    }

    public void setNewSOPhoneHashMap(HashMap newSOPhoneHashMap) {
        this.newSOPhoneHashMap = newSOPhoneHashMap;
    }

    public HashMap getNewSOAliasHashMap() {
        return newSOAliasHashMap;
    }

    public void setNewSOAliasHashMap(HashMap newSOAliasHashMap) {
        this.newSOAliasHashMap = newSOAliasHashMap;
    }


    public ArrayList getNewSOHashMapArrayList() {
        return newSOHashMapArrayList;
    }

    public void setNewSOHashMapArrayList(ArrayList newSOHashMapArrayList) {
        this.newSOHashMapArrayList = newSOHashMapArrayList;
    }

    private void saveLinksSelected() {
        try {
            EnterpriseObject updateEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            HashMap newHashMap = new HashMap();
     
            System.out.println("==>:" + this.getHiddenLinkFields());
            String[] allLinks  = this.getHiddenLinkFields().split("##");

            for (int i = 0; i < allLinks.length; i++) {
                String string = allLinks[i];
                //System.out.println(" string ==>: " + string);
                String[] values = string.split(">>");
                for (int j = 0; j < values.length; j++) {
                    String string1 = values[j];
                    newHashMap.put(values[0], values[1]);
//                 String key = string.split(">>")[0];
//                  String value = string.split(">>")[1];
                   // System.out.println("key ==>:" + j + " value ==>: " + string1);
              }
                
               //System.out.println("FINAL newHashMap==>:" + newHashMap);
                //newHashMap.put(key, value);
            }
            EnterpriseObject updateEO = masterControllerService.saveLinks(newHashMap, updateEnterpriseObject);
            session.setAttribute("editEnterpriseObject",updateEO);
            // masterControllerService.updateEnterpriseObject(updateEO);
            
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveUnLinksSelected() {
        try {
            EnterpriseObject updateEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            HashMap newHashMap = new HashMap();
            //System.out.println("==>:" + this.getHiddenUnLinkFields());

            String[] allLinks = this.getHiddenUnLinkFields().split("##");

            for (int i = 0; i < allLinks.length; i++) {
                String string = allLinks[i];
                String[] values = string.split(">>");
                for (int j = 0; j < values.length; j++) {
                    String string1 = values[j];
                    newHashMap.put(values[0], values[1]);
                }

                //System.out.println("FINAL newHashMap==>:" + newHashMap);
               //newHashMap.put(key, value);
            }
            EnterpriseObject updateEO = masterControllerService.removeLinks(newHashMap, updateEnterpriseObject);
            //masterControllerService.updateEnterpriseObject(updateEO);
            session.setAttribute("editEnterpriseObject",updateEO);

        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void unSetMinorObjectPrimaryValues(ArrayList minorObjects) {
        if (minorObjects.size() > 0) {
            for (int k = 0; k < minorObjects.size(); k++) {
                HashMap minorObjectHashMap = (HashMap) minorObjects.get(k);
                minorObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, null);
                minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, null);
                minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, null);
                minorObjectHashMap.put(MasterControllerService.LID, null);
                minorObjects.remove(minorObjectHashMap);
            }
        }
    }
    
    public void setUpdatedEOFields(EnterpriseObject editEnterpriseObject) {
        try {
            // Keep the EO in session
            session.setAttribute("editEnterpriseObject",editEnterpriseObject);
            
            String rootName = screenObject.getRootObj().getName();
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
            
            
            HashMap editEOMap = masterControllerService.getEnterpriseObjectAsHashMap(editEnterpriseObject, personEPathArrayList);

            HashMap eoWithLinkedHashMap = masterControllerService.getLinkedFields(editEnterpriseObject);
            System.out.println("==> ; eoWithLinkedHashMap"  + eoWithLinkedHashMap);
            HashMap newHashMapWithLinks  = new HashMap();
            Object[] keySet  = editEOMap.keySet().toArray();
            //BUILD the hash map with links
            for (int i = 0; i < keySet.length; i++) {
                 String key = (String) keySet[i];
                if(eoWithLinkedHashMap.get(key) != null ) {
                   newHashMapWithLinks.put(key, true);
                } else {
                   newHashMapWithLinks.put(key, false);
                }
            }
            //System.out.println("newHashMapWithLinks +++ from DB >>>>" + newHashMapWithLinks);
            //set the EO linked information here
            setLinkedFieldsHashMapFromDB(newHashMapWithLinks);

            HashMap eoLockedFeildsHashMap = masterControllerService.getLockedFields(editEnterpriseObject);
            //System.out.println("==> ; eoLockedFeildsHashMap"  + eoLockedFeildsHashMap);
            
            setLockedFieldsHashMapFromDB(eoLockedFeildsHashMap);
             //set the linkedfields for so here
            setLinkedSOFieldsHashMapFromDB(eoWithLinkedHashMap);
            
            
            HashMap editEOMapMain = compareDuplicateManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);

            editEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            
            HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);
            
            //set EO as hash map for display
            setEditSingleEOHashMap(eoHashMap);
            
            //set the EO person details in the SBR arraylist for sending to the SL for editing
            this.changedSBRArrayList.add(editEOMap);

            //set address array list of hasmap for editing
            ArrayList addressMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("Address"), "Address",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleAddressHashMapArrayList(addressMapArrayList);

            //set the EO address in the SBR arraylist for sending to the SL for editing
            if (addressMapArrayList.size() > 0) {
                for (int k = 0; k < addressMapArrayList.size(); k++) {
                    HashMap sbrAddressHashMap = (HashMap) addressMapArrayList.get(k);
                    sbrAddressHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrAddressHashMap);
                }
            }
            
            //set phone array list of hasmap for editing
            ArrayList phoneMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSinglePhoneHashMapArrayList(phoneMapArrayList);

            //set the EO phone in the SBR arraylist for sending to the SL for editing
            if (phoneMapArrayList.size() > 0) {
                for (int k = 0; k < phoneMapArrayList.size(); k++) {
                    HashMap sbrPhoneHashMap = (HashMap) phoneMapArrayList.get(k);
                    sbrPhoneHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrPhoneHashMap);
                }
            }
           
            //set alias array list of hasmap for editing
            ArrayList aliasMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleAliasHashMapArrayList(aliasMapArrayList);

            //set the EO alias in the SBR arraylist for sending to the SL for editing
            if (aliasMapArrayList.size() > 0) {
                for (int k = 0; k < aliasMapArrayList.size(); k++) {
                    HashMap sbrAliasHashMap = (HashMap) aliasMapArrayList.get(k);
                    ////System.out.println("Alias middle Name" + sbrAliasHashMap.get("Alias.MiddleName"));
                    sbrAliasHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrAliasHashMap);
                }
            }           
            //set EO system objects here.
            Object[] soArrayObj = editEnterpriseObject.getSystemObjects().toArray();
            ArrayList eoSOobjects = new ArrayList();
            ArrayList newSelectItemArrayList  = new ArrayList();
            for (int i = 0; i < soArrayObj.length; i++) {
                SystemObject systemObject = (SystemObject) soArrayObj[i];
                System.out.println(i + "==> :  LID " + systemObject.getLID() + "===> : Code " + systemObject.getSystemCode());

                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(systemObject.getSystemCode() + ":" + systemObject.getLID());
                selectItem.setValue(systemObject.getSystemCode() + ":" + systemObject.getLID());
                newSelectItemArrayList.add(selectItem);

                HashMap systemObjectHashMap = new HashMap();
                //add SystemCode and LID value to the new Hash Map
                systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                systemObjectHashMap.put("Status", systemObject.getStatus());// set Status here 
                
                HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                
                //add SystemCode and LID value to the new Hash Map
                editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE);// set UPDATE TYPE HERE 
                editSystemObjectHashMap.put("LINK_KEY", systemObject.getSystemCode() + ":" + systemObject.getLID());// set UPDATE TYPE HERE 

                systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap);// Set the edit SystemObject here

                //set address array list of hasmap for editing
                ArrayList addressMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Address"), "Address", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOAddressList", addressMapSOArrayList);// set SO addresses as arraylist here

                if (addressMapSOArrayList.size() > 0) {
                    for (int k = 0; k < addressMapSOArrayList.size(); k++) {
                        HashMap addressHashMap = (HashMap) addressMapSOArrayList.get(k);
                        addressHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        addressHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        addressHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Address");// set MINOR_OBJECT_TYPE

                        //this.editSOMinorObjectsHashMapArrayList.add(addressHashMap);
                    }
                }


                //set phone array list of hasmap for editing
                ArrayList phoneMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOPhoneList", phoneMapSOArrayList);// set SO phones as arraylist here
                if (phoneMapSOArrayList.size() > 0) {
                    for (int k = 0; k < phoneMapSOArrayList.size(); k++) {
                        HashMap phoneHashMap = (HashMap) phoneMapSOArrayList.get(k);
                        phoneHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        phoneHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        phoneHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Phone");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(phoneHashMap);
                    }
                }

                //set alias array list of hasmap for editing
                ArrayList aliasMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOAliasList", aliasMapSOArrayList);// set SO alias as arraylist here
                if (aliasMapSOArrayList.size() > 0) {
                    for (int k = 0; k < aliasMapSOArrayList.size(); k++) {
                        HashMap aliasHashMap = (HashMap) aliasMapSOArrayList.get(k);
                        aliasHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        aliasHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        aliasHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Alias");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(aliasHashMap);
                    }
                }
                //build the system object hashmap for editing 
                eoSOobjects.add(systemObjectHashMap);
                
            }
            //Add new select item for systemcode/lid drop down
            setEoSystemObjectCodesWithLids(newSelectItemArrayList);

            //set all system objects here
            setEoSystemObjects(eoSOobjects);

        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHiddenLinkFields() {
        return hiddenLinkFields;
    }

    public void setHiddenLinkFields(String hiddenLinkFields) {
        this.hiddenLinkFields = hiddenLinkFields;
    }

    public ArrayList getAllNodeFieldConfigs() {
            ArrayList newArrayList = new ArrayList();
            HashMap newHashMap = new HashMap();
            
            String rootNodeName = screenObject.getRootObj().getName();

            //Build and array of field configs for the root node for ex: PERSON
            newHashMap.put(rootNodeName, sourceHandler.buildAllFieldConfigArrayList(rootNodeName));
            
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
 
            for (int i = 0; i < arrObjectNodeConfig.length; i++) {
              ObjectNodeConfig objectNodeConfig = arrObjectNodeConfig[i];
               //Build and array of field configs for the child node for ex: ADDRESS, ALIAS and PHONE
               newHashMap.put(objectNodeConfig.getName(), sourceHandler.buildAllFieldConfigArrayList(rootNodeName));
            }

            newArrayList.add(newHashMap);            
            
            return newArrayList;
    }

    public void setAllNodeFieldConfigs(ArrayList allNodeFieldConfigs) {
        this.allNodeFieldConfigs = allNodeFieldConfigs;
    }

    public int getEoSystemObjectsSize() {
        return this.eoSystemObjects.size();
    }

    public void setEoSystemObjectsSize(int eoSystemObjectsSize) {
        this.eoSystemObjectsSize = eoSystemObjectsSize;
    }

    public ArrayList getEoSystemObjectSL() {
        return eoSystemObjectSL;
    }

    public void setEoSystemObjectSL(ArrayList eoSystemObjectSL) {
        this.eoSystemObjectSL = eoSystemObjectSL;
    }

    public String getHiddenUnLinkFields() {
        return hiddenUnLinkFields;
    }

    public void setHiddenUnLinkFields(String hiddenUnLinkFields) {
        this.hiddenUnLinkFields = hiddenUnLinkFields;
    }

    public HashMap getLockedFieldsHashMapFromDB() {
        return lockedFieldsHashMapFromDB;
    }

    public void setLockedFieldsHashMapFromDB(HashMap lockedFieldsHashMapFromDB) {
        this.lockedFieldsHashMapFromDB = lockedFieldsHashMapFromDB;
    }

    public HashMap getLinkedSOFieldsHashMapFromDB() {
        return linkedSOFieldsHashMapFromDB;
    }

    public void setLinkedSOFieldsHashMapFromDB(HashMap linkedSOFieldsHashMapFromDB) {
        this.linkedSOFieldsHashMapFromDB = linkedSOFieldsHashMapFromDB;
    }

 }
 
    

      
      