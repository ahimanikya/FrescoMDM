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
 * Created on September 19, 2007
 * Author : Rajani Kanth, Samba
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SourceEditHandler {
    private  static final String EDITRECORD="editRecord";

    //Get the session variable from faces context
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    //Get the session variable from faces context
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    //Get the screen object from session
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

    //sub screen tab name for each tab on the source records page
    private String subScreenTab = "View/Edit";
 
    // MasterControllerService from SL
    private MasterControllerService  masterControllerService = new MasterControllerService();

    //Hash map for single SO  for view
    private ArrayList singleSOHashMapArrayList = new ArrayList();

    //Hash map arraylist for single SO 
    private ArrayList singleAddressHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Address
    private ArrayList singleAliasHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Phone
    private ArrayList singlePhoneHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO AuxId
    private ArrayList singleAuxIdHashMapArrayList = new ArrayList();
    
    //Hash map arraylist for single SO Comment
    private ArrayList singleCommentHashMapArrayList = new ArrayList();

    
    //Hash map for singl SO  for EDITING
    private HashMap editSingleSOHashMap = new HashMap();

    //Hash map for single SO Address  for EDITING
    private HashMap editSoAddressHashMap = new HashMap();
    
    //Hash map for SO Phone  for EDITING
    private HashMap editSoPhoneHashMap = new HashMap();

    //Hash map for SO Alias  for EDITING
    private HashMap editSoAliasHashMap = new HashMap();
    
    //Hash map for SO AuxID for EDITING
    private HashMap editSoAuxIdHashMap = new HashMap();

    //Hash map for SO  comment for EDITING
    private HashMap editSoCommentHashMap = new HashMap();

    public static final String UPDATE_SUCCESS = "UPDATE SUCCESS";
    
    /** Creates a new instance of SourceEditHandler */
    public SourceEditHandler() {
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
            //take care of SSN masking here
            String ssn  = (String) editSingleSOHashMap.get("Person.SSN");
            if(editSingleSOHashMap.get("Person.SSN") != null) {
               if(ssn.length() > 1 ) {
                 ssn = ssn.replaceAll("-", "");
                 editSingleSOHashMap.put("Person.SSN",ssn);
               }  else {
                 editSingleSOHashMap.put("Person.SSN",null);
               }
            }

            SystemObject systemObject = (SystemObject) session.getAttribute("singleSystemObjectLID");
            this.getEditSingleSOHashMap().put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());
            this.getEditSingleSOHashMap().put(MasterControllerService.LID, systemObject.getLID());

            //get the enterprise object for the system object
            EnterpriseObject sysEnterpriseObject = masterControllerService.getEnterpriseObjectForSO(systemObject);
            // add so to the array list
            this.getSingleSOHashMapArrayList().add(this.getEditSingleSOHashMap());
            
            //call modifySystemObjects to update the
            masterControllerService.save(sysEnterpriseObject, null, this.getSingleSOHashMapArrayList(), null);

            //Keep the updated SO in the session again
            SystemObject updatedSystemObject = masterControllerService.getSystemObject(systemObject.getSystemCode(), systemObject.getLID());

            session.setAttribute("singleSystemObjectLID", updatedSystemObject);
            session.setAttribute("keyFunction", "editSO");
        } catch (Exception ex) {
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return UPDATE_SUCCESS;
   }
    /**
     * 
     * @param event
     */
    public void editLID(ActionEvent event){

        try {
            SystemObject singleSystemObjectEdit = (SystemObject) event.getComponent().getAttributes().get("soValueExpression");
            SourceHandler sourceHandler = new SourceHandler();
            EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();
            
            HashMap editSystemObjectMap = masterControllerService.getSystemObjectAsHashMap(singleSystemObjectEdit, personEPathArrayList);

            session.setAttribute("singleSystemObjectLID", singleSystemObjectEdit);
            session.setAttribute("systemObjectMap", editSystemObjectMap);
            
            //set the single SO hash map for single so EDITING
            this.setEditSingleSOHashMap(editSystemObjectMap);
                
            //set address array list of hasmap for editing
            ArrayList addressMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, sourceHandler.buildSystemObjectEpaths("Address"), "Address",masterControllerService.MINOR_OBJECT_UPDATE);
            this.setSingleAddressHashMapArrayList(addressMapArrayList);

            //set phone array list of hasmap for editing
            ArrayList phoneMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone",masterControllerService.MINOR_OBJECT_UPDATE);
            this.setSinglePhoneHashMapArrayList(phoneMapArrayList);
           
            //set alias array list of hasmap for editing
            ArrayList aliasMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias",masterControllerService.MINOR_OBJECT_UPDATE);
            this.setSingleAliasHashMapArrayList(aliasMapArrayList);
           
            //set auxid array list of hasmap for editing
            ArrayList auxIdMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId",masterControllerService.MINOR_OBJECT_UPDATE);
            this.setSingleAuxIdHashMapArrayList(auxIdMapArrayList);

            //set comment array list of hasmap for editing
            ArrayList commentMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(singleSystemObjectEdit, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment",masterControllerService.MINOR_OBJECT_UPDATE);
            this.setSingleCommentHashMapArrayList(commentMapArrayList);

            session.setAttribute("keyFunction", "editSO");
        } catch (ObjectException ex) {
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EPathException ex) {
            Logger.getLogger(SourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

    /**
     * 
     * @param event
     */
    public void addSOAddress(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        //set the search type as per the form
        this.singleAddressHashMapArrayList.add(this.getEditSoAddressHashMap());
    }
    
    /**
     * 
     * @param event
     */
    public void removeSOAddress(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
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
        session.setAttribute("tabName", "View/Edit");
        //set the search type as per the form
        this.singlePhoneHashMapArrayList.add(this.getEditSoPhoneHashMap());
    }
    
    
    /**
     * 
     * @param event
     */
    public void removeSOPhone(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
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
        session.setAttribute("tabName", "View/Edit");
        //set the search type as per the form
        this.singleAliasHashMapArrayList.add(this.getEditSoAliasHashMap());
    }

    /**
     * 
     * @param event
     */
    public void removeSOAlias(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        HashMap remAliasMap = (HashMap) event.getComponent().getAttributes().get("remAliasMap");

        //set the search type as per the form
        this.singleAliasHashMapArrayList.remove(remAliasMap);
    }
    
    /**
     * 
     * @param event
     */
    public void addSOAuxId(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        //set the search type as per the form
        this.singleAuxIdHashMapArrayList.add(this.getEditSoAuxIdHashMap());
    }
    
    /**
     * 
     * @param event
     */
    public void removeSOAuxId(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        HashMap remAuxIdMap = (HashMap) event.getComponent().getAttributes().get("remAuxIdMap");
        //set the search type as per the form
        this.singleAuxIdHashMapArrayList.remove(remAuxIdMap);
    }
    
    /**
     * 
     * @param event
     */
    public void addSOComment(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        //set the search type as per the form
        this.singleCommentHashMapArrayList.add(this.getEditSoCommentHashMap());
    }

    /**
     * 
     * @param event
     */
    public void removeSOComment(ActionEvent event) {
        //set the tab name to be "Add"
        session.setAttribute("tabName", "View/Edit");
        HashMap remCommentMap = (HashMap) event.getComponent().getAttributes().get("remCommentMap");
        //set the search type as per the form
        this.singleCommentHashMapArrayList.remove(remCommentMap);
    }
    
    public ArrayList getSingleSOHashMapArrayList() {
        return singleSOHashMapArrayList;
    }

    public void setSingleSOHashMapArrayList(ArrayList singleSOHashMapArrayList) {
        this.singleSOHashMapArrayList = singleSOHashMapArrayList;
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

    public HashMap getEditSingleSOHashMap() {
        return editSingleSOHashMap;
    }

    public void setEditSingleSOHashMap(HashMap editSingleSOHashMap) {
        this.editSingleSOHashMap = editSingleSOHashMap;
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

    public HashMap getEditSoAuxIdHashMap() {
        return editSoAuxIdHashMap;
    }

    public void setEditSoAuxIdHashMap(HashMap editSoAuxIdHashMap) {
        this.editSoAuxIdHashMap = editSoAuxIdHashMap;
    }

    public HashMap getEditSoCommentHashMap() {
        return editSoCommentHashMap;
    }

    public void setEditSoCommentHashMap(HashMap editSoCommentHashMap) {
        this.editSoCommentHashMap = editSoCommentHashMap;
    }
}
