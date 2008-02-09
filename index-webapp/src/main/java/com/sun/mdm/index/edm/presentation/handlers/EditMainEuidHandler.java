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

import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    
    //Hash map arraylist for single EO AuxId
    private ArrayList singleAuxIdHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single EO Comment
    private ArrayList singleCommentHashMapArrayList = new ArrayList();

    
    //Hash map for singl EO  for EDITING
    private HashMap editSingleEOHashMap = new HashMap();

    //Hash map for single EO Address  for EDITING
    private HashMap editEOAddressHashMap = new HashMap();
    
    //Hash map for EO Phone  for EDITING
    private HashMap editEOPhoneHashMap = new HashMap();

    //Hash map for EO Alias  for EDITING
    private HashMap editEOAliasHashMap = new HashMap();
    
    //Hash map for EO AuxID for EDITING
    private HashMap editEOAuxIdHashMap = new HashMap();

    //Hash map for EO  comment for EDITING
    private HashMap editEOCommentHashMap = new HashMap();

    //Hash map arraylist for  SO 
    private ArrayList eoSystemObjects = new ArrayList();

    //Hash map arraylist for edit SO alias,Address,comment,auxid,phone
    private ArrayList editSOMinorObjectsHashMapArrayList = new ArrayList();
    
    private static final String EDIT_EO_SUCCESS = "success";

    //Hash map arraylist for edit EO alias,Address,comment,auxid,phone (SBR)
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
    
    //Hash map for EO AuxID for New SO
    private HashMap newSOAuxIdHashMap = new HashMap();

    //Hash map for EO  comment for New SO
    private HashMap newSOCommentHashMap = new HashMap();

    //Hash map arraylist for newSO SO 
    private ArrayList newSOHashMapArrayList = new ArrayList();

    //Hash map arraylist for newSO SO 
    private ArrayList newSOAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO Address
    private ArrayList newSOAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO Phone
    private ArrayList newSOPhoneHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO AuxId
    private ArrayList newSOAuxIdHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for newSO SO Comment
    private ArrayList newSOCommentHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for new SO alias,Address,comment,auxid,phone
    private ArrayList newSOMinorObjectsHashMapArrayList = new ArrayList();

    private String linkedSoWithLidByUser;

    private HashMap linkedFieldsHashMapByUser = new HashMap();

    private HashMap unLinkedFieldsHashMapByUser = new HashMap();
    
    private HashMap linkedFieldsHashMapFromDB = new HashMap();
    

    //Adding the following variables for getting the select options if the FieldConfig type is "Menu List"
    
    private ArrayList<SelectItem> eoSystemObjectCodesWithLids = new ArrayList();
    
    
    
    /** Creates a new instance of EditMainEuidHandler */
    public EditMainEuidHandler() {
    }

    public String performSubmit()  {
        try {
            
          // Keep the EO in session
           EnterpriseObject updateEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            //build an array of modified system object arrays  
            buildChangedSystemObjects();
            
            //System.out.println("In Perform submit NEW SO (this.newSOHashMap) ===> " + this.newSOHashMap);
            
            //System.out.println("In Perform submit BEFORE (this.changedSBRArrayList) ===> " + this.changedSBRArrayList);
            //System.out.println("In Perform submit (this.eoSystemObjects) ===> " + this.eoSystemObjects);
            //System.out.println("In Perform submit (this.editSOHashMapArrayList) ===> " + this.editSOHashMapArrayList);
            //System.out.println("In Perform submit (this.editSOMinorObjectsHashMapArrayList) ===> " + this.editSOMinorObjectsHashMapArrayList);
      
            //checkAndBuildModifiedSBRValues(updateEnterpriseObject);
            ////System.out.println("In Perform submit AFTER BUILDING (this.changedSBRArrayList) ===> " + this.changedSBRArrayList);

            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());

            //EDIT the EO and its system objects here
            EnterpriseObject eoFinal = masterControllerService.save(updateEnterpriseObject,   //Enterprise Object
                                                                    this.changedSBRArrayList,  //Changed SBR hashmap array
                                                                    this.editSOHashMapArrayList, //Changed/New System objects hashmap array
                                                                    null//this.editSOMinorObjectsHashMapArrayList // ChangedNew Minor Objects hashmap Array
                                                                    ); 
            //Get the Summary Info after the changes
            String summaryInfo = masterControllerService.getSummaryInfo();
                     
            //adding summary message after creating/editing systemobjects along with EO
            if(summaryInfo != null && summaryInfo.length() > 0) {
               FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,summaryInfo,summaryInfo));
            }

            String successMessage  = "EUID : \""+eoFinal.getEUID()+"\" details have been successfully updated";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,successMessage,successMessage));
    
            session.removeAttribute("enterpriseArrayList");

            //set the updated EO in the session for viewing
            EnterpriseObject eoUpdated = masterControllerService.getEnterpriseObject(eoFinal.getEUID());

            ArrayList newArrayList = new ArrayList();
            newArrayList.add(eoUpdated);
            
            session.setAttribute("enterpriseArrayList", newArrayList);

        } catch (ObjectException ex) {
            errorMessage = "ObjectException occurred";
            
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.toString()));
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (ValidationException ex) {
            errorMessage = "ValidationException occurred";
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.toString()));
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            return this.SERVICE_LAYER_ERROR;
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
            EnterpriseObject editEnterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");

            // Keep the EO in session
            session.setAttribute("editEnterpriseObject",editEnterpriseObject);

            //////System.out.println("Screen Object  ==> : " + screenObject.getRootObj().getName());
            //if(editEnterpriseObject != null )//System.out.println("In edit EO Action Event EUID   ===> : " + editEnterpriseObject.getEUID());
            
            String rootName = screenObject.getRootObj().getName();
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
            
            
            HashMap editEOMap = masterControllerService.getEnterpriseObjectAsHashMap(editEnterpriseObject, personEPathArrayList);

            editEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            //set EO as hash map for display
            setEditSingleEOHashMap(editEOMap);
            
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
            //set auxid array list of hasmap for editing
            ArrayList auxIdMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleAuxIdHashMapArrayList(auxIdMapArrayList);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (auxIdMapArrayList.size() > 0) {
                for (int k = 0; k < auxIdMapArrayList.size(); k++) {
                    HashMap sbrAuxIdHashMap = (HashMap) auxIdMapArrayList.get(k);
                    sbrAuxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrAuxIdHashMap);
                }
            }           

            //set comment array list of hasmap for editing
            ArrayList commentMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleCommentHashMapArrayList(commentMapArrayList);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (commentMapArrayList.size() > 0) {
                for (int k = 0; k < commentMapArrayList.size(); k++) {
                    HashMap sbrCommentHashMap = (HashMap) auxIdMapArrayList.get(k);
                    sbrCommentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrCommentHashMap);
                }
            }           
            

            //set EO system objects here.
            Object[] soArrayObj = editEnterpriseObject.getSystemObjects().toArray();
            for (int i = 0; i < soArrayObj.length; i++) {
                SystemObject systemObject = (SystemObject) soArrayObj[i];
                //////System.out.println(i + "==> :  LID " + systemObject.getLID() + "===> : Code " + systemObject.getSystemCode());
                //HashMap systemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                
                SelectItem selectItem = new SelectItem();
                //////System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
                selectItem.setLabel(systemObject.getSystemCode());
                selectItem.setValue(systemObject.getSystemCode() + ":" + systemObject.getLID());
                this.eoSystemObjectCodesWithLids.add(selectItem);

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
                //set auxid array list of hasmap for editing
                ArrayList auxIdMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOAuxIdList", auxIdMapSOArrayList);// set SO auxId as arraylist here

                if (auxIdMapSOArrayList.size() > 0) {
                    for (int k = 0; k < auxIdMapSOArrayList.size(); k++) {
                        HashMap auxIdHashMap = (HashMap) auxIdMapSOArrayList.get(k);
                        auxIdHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        auxIdHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        auxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(auxIdHashMap);
                    }
                }
                //set comment array list of hasmap for editing
                ArrayList commentMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOCommentList", commentMapSOArrayList);// set SO Comment as arraylist here
                
                if (commentMapSOArrayList.size() > 0) {
                    for (int k = 0; k < commentMapSOArrayList.size(); k++) {
                        HashMap commentHashMap = (HashMap) commentMapSOArrayList.get(k);
                        commentHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        commentHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        commentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(commentHashMap);
                    }
                }

                //build the system object hashmap for editing 
                this.eoSystemObjects.add(systemObjectHashMap);
                
                //////System.out.println("IN ACTION EVENT ===> : this.eoSystemObjects" + this.eoSystemObjects);
                //////System.out.println("IN ACTION EVENT ===> : this.editSOMinorObjectsHashMapArrayList" + this.editSOMinorObjectsHashMapArrayList);
            }

        } catch (ProcessingException ex) {
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

     public void addEOAuxId(ActionEvent event ) {
         this.editEOAuxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
         this.editEOAuxIdHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
         this.changedSBRArrayList.add(this.editEOAuxIdHashMap);

         ////System.out.println("in action even for add EO auxid hashmap" + this.editEOAuxIdHashMap);
        //set the search type as per the form
        this.singleAuxIdHashMapArrayList.add(this.editEOAuxIdHashMap);
     }

     
     public void removeEOAuxId(ActionEvent event ) {
        HashMap remAuxIdMap = (HashMap) event.getComponent().getAttributes().get("remAuxIdMap");

        this.editEOAuxIdHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE

        ////System.out.println("in action even for remove auxid hashmap" + remAuxIdMap);
        //set the search type as per the form
        this.singleAuxIdHashMapArrayList.remove(remAuxIdMap);

     }
     public void addEOComment(ActionEvent event ) {
         
         this.editEOCommentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
         this.editEOCommentHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
         this.changedSBRArrayList.add(this.editEOCommentHashMap);
         
        ////System.out.println("in action even for add EO Comment hashmap" + this.editEOCommentHashMap);
        //set the search type as per the form
        this.singleCommentHashMapArrayList.add(this.editEOCommentHashMap);
     }

     public void removeEOComment(ActionEvent event ) {
        HashMap remCommentMap = (HashMap) event.getComponent().getAttributes().get("remCommentMap");

        // set MINOR_OBJECT_TYPE
        this.editEOCommentHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);

        ////System.out.println("in action even for remove comment hashmap" + remCommentMap);
        //set the search type as per the form
        this.singleCommentHashMapArrayList.remove(remCommentMap);
         
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

                if (this.newSOAuxIdHashMapArrayList.size() > 0) {
                    for (int k = 0; k < this.newSOAuxIdHashMapArrayList.size(); k++) {
                        HashMap auxIdHashMap = (HashMap) this.newSOAuxIdHashMapArrayList.get(k);
                        auxIdHashMap.put(MasterControllerService.LID, this.newSoLID);// set LID here 
                        auxIdHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);// set System code here 
                        auxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
                        auxIdHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
                        this.newSOMinorObjectsHashMapArrayList.add(auxIdHashMap);
                    }
                }
                //set comment array list of hasmap for editing
                if (this.newSOCommentHashMapArrayList.size() > 0) {
                    for (int k = 0; k < newSOCommentHashMapArrayList.size(); k++) {
                        HashMap commentHashMap = (HashMap) newSOCommentHashMapArrayList.get(k);
                        commentHashMap.put(MasterControllerService.LID, this.newSoLID);// set LID here 
                        commentHashMap.put(MasterControllerService.SYSTEM_CODE, this.newSoSystemCode);// set System code here 
                        commentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
                        commentHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE
                        this.newSOMinorObjectsHashMapArrayList.add(commentHashMap);
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
            unSetMinorObjectPrimaryValues(this.newSOAuxIdHashMapArrayList);
            unSetMinorObjectPrimaryValues(this.newSOCommentHashMapArrayList);
            this.newSOHashMap.clear();
            setUpdatedEOFields(); //set the updated values here
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

    /**
     * 
     * @param event
     */
    public void addNewSOAuxId(ActionEvent event) {

        ////System.out.println("in action even for add EO auxId hashmap" + this.newSOAuxIdHashMap);
         this.newSOAuxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
         this.newSOAuxIdHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE


        //set the auxId arraylist for displaying in the jsp page
        this.newSOAuxIdHashMapArrayList.add(this.newSOAuxIdHashMap);

        //set the auxId arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAuxIdHashMap);
        
    }

    
    /**
     * 
     * @param event
     */
    public void removeNewSOAuxId(ActionEvent event) {

        HashMap remAuxIdMap = (HashMap) event.getComponent().getAttributes().get("remAuxIdMap");

        this.newSOAuxIdHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE


        //set the auxId arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOAuxIdHashMap);

        //set the search type as per the form
        this.newSOAuxIdHashMapArrayList.remove(remAuxIdMap);
    }
    /**
     * 
     * @param event
     */
    public void addNewSOComment(ActionEvent event) {

        ////System.out.println("in action even for add EO comment hashmap" + this.newSOCommentHashMap);
         this.newSOCommentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
         this.newSOCommentHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);// set MINOR_OBJECT_TYPE


        //set the comment arraylist for displaying in the jsp page
        this.newSOCommentHashMapArrayList.add(this.newSOCommentHashMap);

        //set the comment arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOCommentHashMap);
        
    }

    
    /**
     * 
     * @param event
     */
    public void removeNewSOComment(ActionEvent event) {

        HashMap remCommentMap = (HashMap) event.getComponent().getAttributes().get("remCommentMap");

        this.newSOCommentHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);// set MINOR_OBJECT_TYPE


        //set the comment arraylist for SL for adding
        this.newSOMinorObjectsHashMapArrayList.add(this.newSOCommentHashMap);

        //set the search type as per the form
        this.newSOCommentHashMapArrayList.remove(remCommentMap);
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

    public ArrayList getSingleAuxIdHashMapArrayList() {
        return singleAuxIdHashMapArrayList;
    }

    public void setSingleAuxIdHashMapArrayList(ArrayList singleAuxIdHashMapArrayList) {
        this.singleAuxIdHashMapArrayList = singleAuxIdHashMapArrayList;
    }

    public ArrayList getSingleCommentHashMapArrayList() {
        return singleCommentHashMapArrayList;
    }

    public void setSingleCommentHashMapArrayList(ArrayList singleCommentHashMapArrayList) {
        this.singleCommentHashMapArrayList = singleCommentHashMapArrayList;
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

    public HashMap getEditEOAuxIdHashMap() {
        return editEOAuxIdHashMap;
    }

    public void setEditEOAuxIdHashMap(HashMap editEOAuxIdHashMap) {
        this.editEOAuxIdHashMap = editEOAuxIdHashMap;
    }

    public HashMap getEditEOCommentHashMap() {
        return editEOCommentHashMap;
    }

    public void setEditEOCommentHashMap(HashMap editEOCommentHashMap) {
        this.editEOCommentHashMap = editEOCommentHashMap;
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

                for(int j=0;j<personFieldConfigsObj.length;j++) {
                    FieldConfig fieldConfig = (FieldConfig) personFieldConfigsObj[j];
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
            
            
            //loop through all field configs to get the updated values in the hashmap PERSON FIELDS only
            for(int i=0;i<this.changedSBRArrayList.size();i++) {    

                HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                ////System.out.println("==> updatedHashMap : " + updatedHashMap);
                if (MasterControllerService.SBR_UPDATE.equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.HASH_MAP_TYPE))) {
                    for (int j = 0; j < allFieldConfigs.size(); j++) {
                        FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                        ePathName = fieldConfig.getFullFieldName();

                        String oldValue = (String) eoOldMap.get(ePathName);
                        String newValue = (String) updatedHashMap.get(ePathName);
                        //check if old value is equal to new value
                        if ((oldValue == null && newValue == null)) {
                            updatedHashMap.remove(ePathName);
                        } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                            updatedHashMap.remove(ePathName);
                        } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                            updatedHashMap.remove(ePathName);
                        }
                    }

                } //if condition for hashmap type
                
            }
                //set address array list of hasmap for editing
            ArrayList addressMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(updateEnterpriseObjectArg, sourceHandler.buildSystemObjectEpaths("Address"), "Address",MasterControllerService.MINOR_OBJECT_UPDATE);

            //set the EO address in the SBR arraylist for sending to the SL for editing
            if (addressMapArrayList.size() > 0) {
                for (int k = 0; k < addressMapArrayList.size(); k++) {
                    HashMap sbrAddressHashMap = (HashMap) addressMapArrayList.get(k);
                    for (int i = 0; i < this.changedSBRArrayList.size(); i++) {

                        HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                        ////System.out.println("==> ADDRESS updatedHashMap : " + updatedHashMap);

                        if ("Address".equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE))) {
                            for (int j = 0; j < allFieldConfigs.size(); j++) {
                                FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                                ePathName = fieldConfig.getFullFieldName();

                                String oldValue = (String) eoOldMap.get(ePathName);
                                String newValue = (String) updatedHashMap.get(ePathName);
                                //check if old value is equal to new value
                                if ((oldValue == null && newValue == null)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                                    updatedHashMap.remove(ePathName);
                                }
                            }

                        } //if condition for hashmap type
                    }
                }
            }
            
            //set phone array list of hasmap for editing
            ArrayList phoneMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(updateEnterpriseObjectArg, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone",MasterControllerService.MINOR_OBJECT_UPDATE);

            //set the EO phone in the SBR arraylist for sending to the SL for editing
            if (phoneMapArrayList.size() > 0) {
                for (int k = 0; k < phoneMapArrayList.size(); k++) {
                    HashMap sbrPhoneHashMap = (HashMap) phoneMapArrayList.get(k);
                    for (int i = 0; i < this.changedSBRArrayList.size(); i++) {

                        HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                        ////System.out.println("==> Phone updatedHashMap : " + updatedHashMap);
                        if ("Phone".equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE))) {
                            for (int j = 0; j < allFieldConfigs.size(); j++) {
                                FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                                ePathName = fieldConfig.getFullFieldName();

                                String oldValue = (String) eoOldMap.get(ePathName);
                                String newValue = (String) updatedHashMap.get(ePathName);
                                //check if old value is equal to new value
                                if ((oldValue == null && newValue == null)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                                    updatedHashMap.remove(ePathName);
                                }
                            }

                        } //if condition for hashmap type
                    }
                }
            }
           
            //set alias array list of hasmap for editing
            ArrayList aliasMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(updateEnterpriseObjectArg, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias",MasterControllerService.MINOR_OBJECT_UPDATE);
            //set the EO alias in the SBR arraylist for sending to the SL for editing
            if (aliasMapArrayList.size() > 0) {
                for (int k = 0; k < aliasMapArrayList.size(); k++) {
                    HashMap sbrAliasHashMap = (HashMap) aliasMapArrayList.get(k);
                    for (int i = 0; i < this.changedSBRArrayList.size(); i++) {

                        HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                        ////System.out.println("==> ALIAS updatedHashMap : " + updatedHashMap);

                        if ("Alias".equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE))) {
                            for (int j = 0; j < allFieldConfigs.size(); j++) {
                                FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                                ePathName = fieldConfig.getFullFieldName();

                                String oldValue = (String) eoOldMap.get(ePathName);
                                String newValue = (String) updatedHashMap.get(ePathName);
                                //check if old value is equal to new value
                                if ((oldValue == null && newValue == null)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                                    updatedHashMap.remove(ePathName);
                                }
                            }

                        } //if condition for hashmap type

                    }
                }
            }           
            //set auxid array list of hasmap for editing
            ArrayList auxIdMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(updateEnterpriseObjectArg, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId",MasterControllerService.MINOR_OBJECT_UPDATE);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (auxIdMapArrayList.size() > 0) {
                for (int k = 0; k < auxIdMapArrayList.size(); k++) {
                    HashMap sbrAuxIdHashMap = (HashMap) auxIdMapArrayList.get(k);
                    for (int i = 0; i < this.changedSBRArrayList.size(); i++) {

                        HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                        ////System.out.println("==> sbrAuxIdHashMap updatedHashMap : " + updatedHashMap);

                        if ("AuxId".equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE))) {
                            for (int j = 0; j < allFieldConfigs.size(); j++) {
                                FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                                ePathName = fieldConfig.getFullFieldName();

                                String oldValue = (String) eoOldMap.get(ePathName);
                                String newValue = (String) updatedHashMap.get(ePathName);
                                //check if old value is equal to new value
                                if ((oldValue == null && newValue == null)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                                    updatedHashMap.remove(ePathName);
                                }
                            }

                        } //if condition for hashmap type

                    }
                }
            }           

            //set comment array list of hasmap for editing
            ArrayList commentMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(updateEnterpriseObjectArg, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment",MasterControllerService.MINOR_OBJECT_UPDATE);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (commentMapArrayList.size() > 0) {
                for (int k = 0; k < commentMapArrayList.size(); k++) {
                    HashMap sbrCommentHashMap = (HashMap) auxIdMapArrayList.get(k);
                    for (int i = 0; i < this.changedSBRArrayList.size(); i++) {

                        HashMap updatedHashMap = (HashMap) this.changedSBRArrayList.get(i);

                        ////System.out.println("==> COMMENT updatedHashMap : " + updatedHashMap);

                        if ("Comment".equalsIgnoreCase((String) updatedHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE))) {
                            for (int j = 0; j < allFieldConfigs.size(); j++) {
                                FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                                ePathName = fieldConfig.getFullFieldName();

                                String oldValue = (String) eoOldMap.get(ePathName);
                                String newValue = (String) updatedHashMap.get(ePathName);
                                //check if old value is equal to new value
                                if ((oldValue == null && newValue == null)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && (oldValue.length() == 0 && newValue.length() == 0)) {
                                    updatedHashMap.remove(ePathName);
                                } else if ((oldValue != null && newValue != null) && oldValue.equalsIgnoreCase(newValue)) {
                                    updatedHashMap.remove(ePathName);
                                }
                            }

                        } //if condition for hashmap type

                    }
                }
            }           
            

            
            
            

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

    public ArrayList getNewSOAuxIdHashMapArrayList() {
        return newSOAuxIdHashMapArrayList;
    }

    public void setNewSOAuxIdHashMapArrayList(ArrayList newSOAuxIdHashMapArrayList) {
        this.newSOAuxIdHashMapArrayList = newSOAuxIdHashMapArrayList;
    }

    public ArrayList getNewSOCommentHashMapArrayList() {
        return newSOCommentHashMapArrayList;
    }

    public void setNewSOCommentHashMapArrayList(ArrayList newSOCommentHashMapArrayList) {
        this.newSOCommentHashMapArrayList = newSOCommentHashMapArrayList;
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

    public HashMap getNewSOAuxIdHashMap() {
        return newSOAuxIdHashMap;
    }

    public void setNewSOAuxIdHashMap(HashMap newSOAuxIdHashMap) {
        this.newSOAuxIdHashMap = newSOAuxIdHashMap;
    }

    public HashMap getNewSOCommentHashMap() {
        return newSOCommentHashMap;
    }

    public void setNewSOCommentHashMap(HashMap newSOCommentHashMap) {
        this.newSOCommentHashMap = newSOCommentHashMap;
    }

    public ArrayList getNewSOHashMapArrayList() {
        return newSOHashMapArrayList;
    }

    public void setNewSOHashMapArrayList(ArrayList newSOHashMapArrayList) {
        this.newSOHashMapArrayList = newSOHashMapArrayList;
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
    
    public void setUpdatedEOFields() {
        try {
            EnterpriseObject editEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");

            editEnterpriseObject  = masterControllerService.getEnterpriseObject(editEnterpriseObject.getEUID());
            
            // Keep the EO in session
            session.setAttribute("editEnterpriseObject",editEnterpriseObject);

            //////System.out.println("Screen Object  ==> : " + screenObject.getRootObj().getName());
            //if(editEnterpriseObject != null )//System.out.println("In edit EO Action Event EUID   ===> : " + editEnterpriseObject.getEUID());
            
            String rootName = screenObject.getRootObj().getName();
            ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
            
            
            HashMap editEOMap = masterControllerService.getEnterpriseObjectAsHashMap(editEnterpriseObject, personEPathArrayList);

            editEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            //set EO as hash map for display
            setEditSingleEOHashMap(editEOMap);
            
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
            //set auxid array list of hasmap for editing
            ArrayList auxIdMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleAuxIdHashMapArrayList(auxIdMapArrayList);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (auxIdMapArrayList.size() > 0) {
                for (int k = 0; k < auxIdMapArrayList.size(); k++) {
                    HashMap sbrAuxIdHashMap = (HashMap) auxIdMapArrayList.get(k);
                    sbrAuxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrAuxIdHashMap);
                }
            }           

            //set comment array list of hasmap for editing
            ArrayList commentMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(editEnterpriseObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment",MasterControllerService.MINOR_OBJECT_UPDATE);
            setSingleCommentHashMapArrayList(commentMapArrayList);

            //set the EO auxId in the SBR arraylist for sending to the SL for editing
            if (commentMapArrayList.size() > 0) {
                for (int k = 0; k < commentMapArrayList.size(); k++) {
                    HashMap sbrCommentHashMap = (HashMap) auxIdMapArrayList.get(k);
                    sbrCommentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
                    //this.changedSBRArrayList.add(sbrCommentHashMap);
                }
            }           
            

            //set EO system objects here.
            Object[] soArrayObj = editEnterpriseObject.getSystemObjects().toArray();
            for (int i = 0; i < soArrayObj.length; i++) {
                SystemObject systemObject = (SystemObject) soArrayObj[i];
                //////System.out.println(i + "==> :  LID " + systemObject.getLID() + "===> : Code " + systemObject.getSystemCode());
                //HashMap systemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                
                SelectItem selectItem = new SelectItem();
                //////System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
                selectItem.setLabel(systemObject.getSystemCode());
                selectItem.setValue(systemObject.getSystemCode() + ":" + systemObject.getLID());
                this.eoSystemObjectCodesWithLids.add(selectItem);

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
                //set auxid array list of hasmap for editing
                ArrayList auxIdMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOAuxIdList", auxIdMapSOArrayList);// set SO auxId as arraylist here

                if (auxIdMapSOArrayList.size() > 0) {
                    for (int k = 0; k < auxIdMapSOArrayList.size(); k++) {
                        HashMap auxIdHashMap = (HashMap) auxIdMapSOArrayList.get(k);
                        auxIdHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        auxIdHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        auxIdHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "AuxId");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(auxIdHashMap);
                    }
                }
                //set comment array list of hasmap for editing
                ArrayList commentMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment", MasterControllerService.MINOR_OBJECT_UPDATE);
                systemObjectHashMap.put("SOCommentList", commentMapSOArrayList);// set SO Comment as arraylist here
                
                if (commentMapSOArrayList.size() > 0) {
                    for (int k = 0; k < commentMapSOArrayList.size(); k++) {
                        HashMap commentHashMap = (HashMap) commentMapSOArrayList.get(k);
                        commentHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                        commentHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                        commentHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, "Comment");// set MINOR_OBJECT_TYPE
                        //this.editSOMinorObjectsHashMapArrayList.add(commentHashMap);
                    }
                }

                //build the system object hashmap for editing 
                this.eoSystemObjects.add(systemObjectHashMap);
                
                //////System.out.println("IN ACTION EVENT ===> : this.eoSystemObjects" + this.eoSystemObjects);
                //////System.out.println("IN ACTION EVENT ===> : this.editSOMinorObjectsHashMapArrayList" + this.editSOMinorObjectsHashMapArrayList);
            }

        } catch (UserException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 }
 
    

      
      