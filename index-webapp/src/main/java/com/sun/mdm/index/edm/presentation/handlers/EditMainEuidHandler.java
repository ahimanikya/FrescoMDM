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
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.logging.s;
//import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Rajani Kanth
 * www.ligaturesoftware.com
 * rkanth@ligaturesoftware.com
 * 
 */
public class EditMainEuidHandler {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.EditMainEuidHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    String exceptionMessaage = bundle.getString("EXCEPTION_MSG");
    private static final String EDIT_SUCCESS = "EO_EDIT_SUCCESS";
    private static final String CONCURRENT_MOD_ERROR = "CONCURRENT_MOD_ERROR";
    //Hash map for single EO  for view
    private ArrayList singleEOHashMapArrayList = new ArrayList();
    //Hash map for singl EO  for EDITING
    private HashMap editSingleEOHashMap = new HashMap();
    //Hash map for singl EO  for EDITING
    private HashMap editSingleEOHashMapOld = new HashMap();

    //Hash map arraylist for  SO 
    private ArrayList eoSystemObjects;
    private ArrayList eoSystemObjectsOld;

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
    private HashMap newSOHashMap = new HashMap();
    //Hash map arraylist for newSO SO 
    private ArrayList newSOHashMapArrayList = new ArrayList();
    //Hash map arraylist for newSO SO Phone
    private ArrayList editEOMinorHashMapArrayList = new ArrayList();
    //Hash map arraylist for new SO alias,Address,phone
    private ArrayList newSOMinorObjectsHashMapArrayList = new ArrayList();
    private String linkedSoWithLidByUser;
    private HashMap linkedFieldsHashMapByUser = new HashMap();
    private HashMap unLinkedFieldsHashMapByUser = new HashMap();
    private HashMap linkedFieldsHashMapFromDB = new HashMap();
    private HashMap lockedFieldsHashMapFromDB = new HashMap();
    private HashMap unLockedFieldsHashMapByUser = new HashMap();
    private HashMap linkedSOFieldsHashMapFromDB = new HashMap();
    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

    //Adding the following variables for getting the select options if the FieldConfig type is "Menu List"
    private ArrayList<SelectItem> eoSystemObjectCodesWithLids = new ArrayList();
    private String hiddenLinkFields = new String();
    private String hiddenUnLinkFields = new String();
    private String hiddenUnLockFields = new String();
    private String hiddenLockFields = new String();
    private ArrayList allNodeFieldConfigs = new ArrayList();
    private String enteredFieldValues = new String();
    private String minorFieldsEO = new String();
    private String removeEOMinorObjectsValues = new String();

    /** Creates a new instance of EditMainEuidHandler */
    public EditMainEuidHandler() {
    }

    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used to create the  enterprise object.
     *
     *  @return EO_EDIT_SUCCESS if save is successful <br>
     *         null if save EO fails or any exception occurs. <br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user <br>
     * 
     */
    public String performSubmit() {
        try {
            String updateEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);

            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());

            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }
            
            
            
            //EDIT the EO and its system objects here
            EnterpriseObject eoFinal = masterControllerService.save(updateEnterpriseObject, this.changedSBRArrayList, this.editSOHashMapArrayList, this.editSOMinorObjectsHashMapArrayList);

            //Get the Summary Info after the changes
            String summaryInfo = masterControllerService.getSummaryInfo();

            //adding summary message after creating/editing systemobjects along with EO
            if (summaryInfo != null && summaryInfo.length() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summaryInfo, summaryInfo));
            }

            String successMessage = "EUID : \"" + eoFinal.getEUID() + "\" details have been successfully updated";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successMessage, successMessage));

        //setUpdatedEOFields(masterControllerService.getEnterpriseObject(updateEnterpriseObject.getEUID())); //set the updated values here
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME030: UserException occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME031: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME032: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME001: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;
    }

    public void setEditEOFields(ActionEvent event) {
        try {
            String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject editEnterpriseObject = masterControllerService.getEnterpriseObject(euid);

            setUpdatedEOFields(editEnterpriseObject); //set the updated values here

        } catch (ProcessingException ex) {
//            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME005: Unable to update EO fields :{0}", ex.getMessage()));
//            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME034: Exception has occurred :{0}", ex.getMessage()), ex);
        }
    }

    public void setEditEOFields(String euid) {
        try {
            //String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject editEnterpriseObject = masterControllerService.getEnterpriseObject(euid);

            setUpdatedEOFields(editEnterpriseObject); //set the updated values here

            session.setAttribute("", "");
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("EME035: Exception has occurred :{0}", ex.getMessage()), ex);
        } catch (UserException ex) {
//            Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME006: Unable to update EO fields :{0}", ex.getMessage()));
        }
    }

    public void deactivateEOSO(ActionEvent event) {
        try {
            HashMap soHashMap = (HashMap) event.getComponent().getAttributes().get("eoSystemObjectMapVE");
            SystemObject systemObject = masterControllerService.getSystemObject((String) soHashMap.get("SystemCode"), (String) soHashMap.get("LID"));
            //call mastercontroller service to deactivate the system object
            masterControllerService.deactivateSystemObject(systemObject);
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("EME007: Unable to deactivate  System Object :{0}", ex.getMessage()));
        //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME008: Unable to deactivate  System Object :{0}", ex.getMessage()));
        //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void activateEOSO(ActionEvent event) {
        try {
            HashMap soHashMap = (HashMap) event.getComponent().getAttributes().get("eoSystemObjectMapVE");
            SystemObject systemObject = masterControllerService.getSystemObject((String) soHashMap.get("SystemCode"), (String) soHashMap.get("LID"));
            //call mastercontroller service to deactivate the system object
            masterControllerService.activateSystemObject(systemObject);
        } catch (ProcessingException ex) {
            //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME009: Unable to activate  System Object :{0}", ex.getMessage()));
        } catch (UserException ex) {
            //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME010: Unable to activate  System Object :{0}", ex.getMessage()));
        }
    }
   
    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used to deactive the EO from edit main euid screen.
     *
     *  @return EO_EDIT_SUCCESS if save is successful<br>
     *         null if add SO , save EO fails or any exception occurs.<br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user<br>
     * 
     */

    public String deactivateEO(String euid) {

        try {
            String updateEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);
            
           
            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();

            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }

            //Deactivate the enterprise object
            masterControllerService.deactivateEnterpriseObject(euid);

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(euid);

            // Keep the EO in session
            session.setAttribute("editEuid", updatedEnterpriseObject.getEUID());


        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("EME035: Exception has occurred :{0}", ex.getMessage()), ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;

    //Keep the updated SO in the session again
    }

    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used to add the SO to enterprise object.
     *
     *  @return EO_EDIT_SUCCESS if save is successful <br>
     *         null if add SO , save EO fails or any exception occurs.<br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user<br>
     * 
     */
      public String addNewSO() {
        try {
            masterControllerService.setRootNodeName(screenObject.getRootObj().getName());
            String updateEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);
            
            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }
            
            

            //Add new SO here
            EnterpriseObject eoFinal = masterControllerService.save(updateEnterpriseObject,
                    null,
                    this.newSOHashMapArrayList,
                    this.newSOMinorObjectsHashMapArrayList);


            String summaryInfo = masterControllerService.getSummaryInfo();
            //adding summary message after creating/editing systemobjects along with EO
            if (summaryInfo != null && summaryInfo.length() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summaryInfo, summaryInfo));
            }

            setUpdatedEOFields(eoFinal); //set the updated values here

        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME011: Unable to add  System Object :{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            mLogger.error(mLocalizer.x("EME012: Unable to add  System Object :{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            mLogger.error(mLocalizer.x("EME013: Unable to add  System Object :{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;
    }

    public ArrayList getSingleEOHashMapArrayList() {
        return singleEOHashMapArrayList;
    }

    public void setSingleEOHashMapArrayList(ArrayList singleEOHashMapArrayList) {
        this.singleEOHashMapArrayList = singleEOHashMapArrayList;
    }

    public HashMap getEditSingleEOHashMap() {
        return editSingleEOHashMap;
    }

    public void setEditSingleEOHashMap(HashMap editSingleEOHashMap) {
        this.editSingleEOHashMap = editSingleEOHashMap;
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
        Object[] personFieldConfigsObj = sourceHandler.getPersonFieldConfigs().toArray();
        for (int i = 0; i < soArrayObj.length; i++) {
            HashMap objectMap = (HashMap) soArrayObj[i];
            HashMap systemObjectMap = (HashMap) objectMap.get("SYSTEM_OBJECT");


            //take care of SSN masking here
            String ssnField = screenObject.getRootObj().getName() + ".SSN";
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

    /*
    public ArrayList<SelectItem> getSystemCodes() {
    String[][] systemCodesWithLIDMasking = masterControllerService.getSystemCodes();
    String[] pullDownListItems = systemCodesWithLIDMasking[0];
    ArrayList newArrayList = new ArrayList();
    for (int i = 0; i < pullDownListItems.length; i++) {
    SelectItem selectItem = new SelectItem();
    selectItem.setLabel(masterControllerService.getSystemDescription(pullDownListItems[i]));
    selectItem.setValue(pullDownListItems[i]);
    newArrayList.add(selectItem);
    }
    systemCodes = newArrayList;
    return systemCodes;
    }
     */
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
            String eoEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject eoUpdated = masterControllerService.getEnterpriseObject(eoEuid);

            ArrayList newArrayList = new ArrayList();
            newArrayList.add(eoUpdated);
            session.setAttribute("enterpriseArrayList", newArrayList);
        } catch (ProcessingException ex) {
            //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME016: Unable to update EUID details :{0}", ex.getMessage()));
        } catch (UserException ex) {
            //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME017: Unable to update EUID details:{0}", ex.getMessage()));
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

    public ArrayList getNewSOMinorObjectsHashMapArrayList() {
        return newSOMinorObjectsHashMapArrayList;
    }

    public void setNewSOMinorObjectsHashMapArrayList(ArrayList newSOMinorObjectsHashMapArrayList) {
        this.newSOMinorObjectsHashMapArrayList = newSOMinorObjectsHashMapArrayList;
    }

    public ArrayList getNewSOHashMapArrayList() {
        return newSOHashMapArrayList;
    }

    public void setNewSOHashMapArrayList(ArrayList newSOHashMapArrayList) {
        this.newSOHashMapArrayList = newSOHashMapArrayList;
    }

    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used SAVE the links selected by the user from edit main euid ajax services
     *
     *  @return EO_EDIT_SUCCESS if save is successful <br>
     *         null if add SO , save EO fails or any exception occurs.<br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user<br>
     * 
     */

    public String saveLinksSelected() {
        try {
            String updateEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);
            
            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }

            
            EnterpriseObject updateEO = masterControllerService.saveLinks(linkedFieldsHashMapByUser, updateEnterpriseObject);
            session.setAttribute("editEuid", updateEO.getEUID());

            masterControllerService.updateEnterpriseObject(updateEO);

        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("EME018: Unable to save selected links :{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            //Logger.getLogger(EditMainEuidHandler.class.getName()).log(Level.SEVERE, null, ex);
            mLogger.error(mLocalizer.x("EME019: Unable to save selected links:{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }

        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }

    
    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used SAVE the unlinks selected by the user from edit main euid ajax services.
     *
     *  @return EO_EDIT_SUCCESS if save is successful<br>
     *         null if add SO , save EO fails or any exception occurs.<br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user<br>
     * 
     */

    public String saveUnLinksSelected() {
        try {
            String updateEuid = (String) session.getAttribute("editEuid");
            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);

            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber = (Integer) session.getAttribute("SBR_REVISION_NUMBER" + updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber = updateEnterpriseObject.getSBR().getRevisionNumber();
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'" + updateEnterpriseObject.getEUID() + "' "+bundle.getString("concurrent_mod_text"), "'" + updateEnterpriseObject.getEUID() + "' "+bundle.getString("concurrent_mod_text")));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }
            
            EnterpriseObject updateEO = masterControllerService.removeLinks(unLinkedFieldsHashMapByUser, updateEnterpriseObject);
            //masterControllerService.updateEnterpriseObject(updateEO);
            session.setAttribute("editEuid", updateEO.getEUID());

            masterControllerService.updateEnterpriseObject(updateEO);

        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("EME020: Unable to save selected unlinks:{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME021: Unable to save selected unlinks:{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }

    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used SAVE the unlocks selected by the user from edit main euid ajax services.
     *
     *  @return EO_EDIT_SUCCESS if save is successful <br>
     *         null if add SO , save EO fails or any exception occurs. <br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user <br>
     * 
     */
    public String saveUnLocksSelected() {
        try {
            String updateEuid = (String) session.getAttribute("editEuid");

            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);
            
            
            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }

            EnterpriseObject updateEO = masterControllerService.removeLocks(unLockedFieldsHashMapByUser, updateEnterpriseObject);
            
            //masterControllerService.updateEnterpriseObject(updateEO);
            session.setAttribute("editEuid", updateEO.getEUID());

            masterControllerService.updateEnterpriseObject(updateEO);

        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("EME022: Unable to save selected unlocks:{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME023: Unable to save selected unlocks:{0}", ex.getMessage()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }
    
    
    /** 
     * Added  By Rajani Kanth on 26/06/2008
     * 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used SAVE the locks selected by the user from edit main euid ajax services
     *
     *  @return EO_EDIT_SUCCESS if save is successful <br>
     *         null if add SO , save EO fails or any exception occurs. <br>
     *         CONCURRENT_MOD_ERROR if the EO is already modified by another user <br>
     * 
     */


    public String saveLocksSelected(ArrayList sbrHashMapList, ArrayList sbrMinorObjectsList) {
        try {
            String updateEuid = (String) session.getAttribute("editEuid");

            EnterpriseObject updateEnterpriseObject = masterControllerService.getEnterpriseObject(updateEuid);

            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+updateEnterpriseObject.getEUID());
            Integer dbRevisionNumber  = updateEnterpriseObject.getSBR().getRevisionNumber();
            if(dbRevisionNumber.intValue() != sessionRevisionNumber.intValue() ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text"),"'"+updateEnterpriseObject.getEUID()+ "' "+bundle.getString("concurrent_mod_text") ));
                return EditMainEuidHandler.CONCURRENT_MOD_ERROR;
            }
            masterControllerService.save(updateEnterpriseObject, sbrHashMapList, sbrMinorObjectsList, null);
             
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME030: UserException occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME031: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME032: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.toString()));
            mLogger.error(mLocalizer.x("EME001: Exception occurred :{0}", ex.getMessage()), ex);
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;
    }
    
    
    public void setUpdatedEOFields(EnterpriseObject editEnterpriseObject) {
        try {
            // Keep the EO in session
            session.setAttribute("editEuid", editEnterpriseObject.getEUID());
            HashMap editEOMap = masterControllerService.getEnterpriseObjectAsHashMap(editEnterpriseObject, personEPathArrayList);
            HashMap eoWithLinkedHashMap = masterControllerService.getLinkedFields(editEnterpriseObject);
            HashMap newHashMapWithLinks = new HashMap();
            Object[] keySet = editEOMap.keySet().toArray();
            //BUILD the hash map with links
            for (int i = 0; i < keySet.length; i++) {
                String key = (String) keySet[i];
                if (eoWithLinkedHashMap.get(key) != null) {
                    newHashMapWithLinks.put(key, true);
                } else {
                    newHashMapWithLinks.put(key, false);
                }
            }
            //set the EO linked information here
            setLinkedFieldsHashMapFromDB(newHashMapWithLinks);

            HashMap eoLockedFeildsHashMap = masterControllerService.getLockedFields(editEnterpriseObject);
            setLockedFieldsHashMapFromDB(eoLockedFeildsHashMap);
            //set the linkedfields for so here
            setLinkedSOFieldsHashMapFromDB(eoWithLinkedHashMap);


            HashMap editEOMapMain = compareDuplicateManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);

            editEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

            HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);
            HashMap eoHashMapOld = compareDuplicateManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);

            //set EO as hash map for display
            setEditSingleEOHashMap(eoHashMap);

            //Old values are stored 
            setEditSingleEOHashMapOld(eoHashMapOld);

            //set the EO person details in the SBR arraylist for sending to the SL for editing
            //this.changedSBRArrayList.add(editEOMap);

            //set EO system objects here.
            Object[] soArrayObj = editEnterpriseObject.getSystemObjects().toArray();
            ArrayList eoSOobjects = new ArrayList();
            ArrayList eoSOobjectsOld = new ArrayList();
            ArrayList newSelectItemArrayList = new ArrayList();
            for (int i = 0; i < soArrayObj.length; i++) {
                SystemObject systemObject = (SystemObject) soArrayObj[i];
                SelectItem selectItem = new SelectItem();

                //Add the drop down if the system object is active only
                if ("active".equalsIgnoreCase(systemObject.getStatus())) {
                    selectItem.setLabel(ValidationService.getInstance().getSystemDescription(systemObject.getSystemCode()) + "/" + systemObject.getLID());
                    selectItem.setValue(systemObject.getSystemCode() + ":" + systemObject.getLID());
                    newSelectItemArrayList.add(selectItem);
                }

                HashMap systemObjectHashMap = new HashMap();
                HashMap systemObjectHashMapOld = new HashMap();
                //add SystemCode and LID value to the new Hash Map
                systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 

                systemObjectHashMapOld.put(MasterControllerService.LID, systemObject.getLID());// set LID here 

                systemObjectHashMap.put("SYSTEM_CODE_DESC", masterControllerService.getSystemDescription(systemObject.getSystemCode()));// set System code here 

                systemObjectHashMapOld.put("SYSTEM_CODE_DESC", masterControllerService.getSystemDescription(systemObject.getSystemCode()));// set System code here 

                systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 

                systemObjectHashMapOld.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 

                systemObjectHashMap.put("Status", systemObject.getStatus());// set Status here 

                systemObjectHashMapOld.put("Status", systemObject.getStatus());// set Status here 

                HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

                for (int r = 0; r < rootFieldConfigs.length; r++) {
                    FieldConfig fieldConfig = rootFieldConfigs[r];
                    Object value = editSystemObjectHashMap.get(fieldConfig.getFullFieldName());
                    //set the menu list values here
                    if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                        if (value != null) {
                            //Mask the value as per the masking 
                            
                            value = fieldConfig.mask(value.toString());                            
                            editSystemObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                        }
                    }

                }

                
                //add SystemCode and LID value to the new Hash Map
                editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 

                editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 

                editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE);// set UPDATE TYPE HERE 

                editSystemObjectHashMap.put("LINK_KEY", systemObject.getSystemCode() + ":" + systemObject.getLID());// set UPDATE TYPE HERE 

                systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap);// Set the edit SystemObject here

                systemObjectHashMapOld.put("SYSTEM_OBJECT", editSystemObjectHashMap);// Set the edit SystemObject here

                //HashMap soEuidHashMap = compareDuplicateManager.getSystemObjectAsHashMap(systemObject, screenObject);

                ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

                //Build and array of minotr object values from the screen object child object nodes
                for (int j = 0; j < childNodeConfigs.length; j++) {

                    //get the child object node configs
                    ObjectNodeConfig objectNodeConfig = childNodeConfigs[j];
                    FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs();

                    //set address array list of hasmap for editing
                    ArrayList soMinorObjectsMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);

                    ArrayList soMinorObjectsMapArrayListEdit = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);

                    for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                        HashMap minorObjectHashMapEdit = (HashMap) soMinorObjectsMapArrayListEdit.get(k);

                        minorObjectHashMapEdit.put(MasterControllerService.LID, systemObject.getLID()); // set LID here

                        minorObjectHashMapEdit.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here

                        minorObjectHashMapEdit.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE

                    }

                    //set the values for the minor objects with keys only
                    systemObjectHashMap.put("SOEDIT" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListEdit); // set SO addresses as arraylist here

                    systemObjectHashMapOld.put("SOEDIT" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListEdit); // set SO addresses as arraylist here

                    String strVal = new String();
                    for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                        HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                        for (int m = 0; m < minorFiledConfigs.length; m++) {
                            FieldConfig fieldConfig = minorFiledConfigs[m];
                            Object value = minorObjectHashMap.get(fieldConfig.getFullFieldName());
                            //set the menu list values here
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                if (value != null) {
                                    //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                    if (fieldConfig.getUserCode() != null) {
                                        strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                    } else {
                                        strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    }
                                    minorObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                                }

                            } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                                if (value != null) {
                                    //Mask the value as per the masking 
                                    
                                    value = fieldConfig.mask(value.toString());                                    
                                    minorObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                                }
                            }


                        }

                        minorObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here

                        minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here

                        minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE

                    }
                    systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here

                    systemObjectHashMapOld.put("SO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here

                }


                //build the system object hashmap for editing 
                eoSOobjects.add(systemObjectHashMap);
                eoSOobjectsOld.add(systemObjectHashMapOld);

            }
            //Add new select item for systemcode/lid drop down
            setEoSystemObjectCodesWithLids(newSelectItemArrayList);

            //set all system objects here
            setEoSystemObjects(eoSOobjects);

            //set all system objects here
            setEoSystemObjectsOld(eoSOobjectsOld);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME024: Unable to set updated EO fields:{0}", ex.getMessage()));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME025: Unable to set updated EO fields:{0}", ex.getMessage()));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME125: Unable to set updated EO fields:{0}", ex.getMessage()));
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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

    public String getEnteredFieldValues() {
        return enteredFieldValues;
    }

    public void setEnteredFieldValues(String enteredFieldValues) {
        this.enteredFieldValues = enteredFieldValues;
    }

    public String getHiddenUnLockFields() {
        return hiddenUnLockFields;
    }

    public void setHiddenUnLockFields(String hiddenUnLockFields) {
        this.hiddenUnLockFields = hiddenUnLockFields;
    }

    public ArrayList getEditEOMinorHashMapArrayList() {
        return editEOMinorHashMapArrayList;
    }

    public void setEditEOMinorHashMapArrayList(ArrayList editEOMinorHashMapArrayList) {
        this.editEOMinorHashMapArrayList = editEOMinorHashMapArrayList;
    }

    public String getMinorFieldsEO() {
        return minorFieldsEO;
    }

    public void setMinorFieldsEO(String minorFieldsEO) {
        this.minorFieldsEO = minorFieldsEO;
    }

    public String getRemoveEOMinorObjectsValues() {
        return removeEOMinorObjectsValues;
    }

    public void setRemoveEOMinorObjectsValues(String removeEOMinorObjectsValues) {
        this.removeEOMinorObjectsValues = removeEOMinorObjectsValues;
    }

    public String getHiddenLockFields() {
        return hiddenLockFields;
    }

    public void setHiddenLockFields(String hiddenLockFields) {
        this.hiddenLockFields = hiddenLockFields;
    }

    public void checkAndBuildModifiedSBRValues(String updateEnterpriseObjectArg) {

        try {

            EnterpriseObject eo = masterControllerService.getEnterpriseObject(updateEnterpriseObjectArg);
            HashMap eoOldMap = masterControllerService.getEnterpriseObjectAsHashMap(eo, personEPathArrayList);

            eoOldMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

            //all field configs as per the root objects
            ArrayList allFieldConfigs = sourceHandler.getAllFieldConfigs();

            String ePathName = new String();
            String rootName = screenObject.getRootObj().getName();

            HashMap eoHashMap = (HashMap) this.editSingleEOHashMap.get("ENTERPRISE_OBJECT_CODES");
            HashMap newUpdatedMap = new HashMap();
            if (MasterControllerService.SBR_UPDATE.equalsIgnoreCase((String) eoHashMap.get(MasterControllerService.HASH_MAP_TYPE))) {
                for (int j = 0; j < allFieldConfigs.size(); j++) {
                    FieldConfig fieldConfig = (FieldConfig) allFieldConfigs.get(j);
                    ePathName = fieldConfig.getFullFieldName();

                    Object oldValue = eoOldMap.get(ePathName);
                    Object newValue = eoHashMap.get(ePathName);
                    //check if no value entered put null into the value
                    if ((oldValue == null && (newValue != null && newValue.toString().trim().length() == 0))) {
                        eoHashMap.put(ePathName, null);
                    }
                    if ((oldValue != null && (newValue != null && newValue.toString().trim().length() == 0))) {
                        eoHashMap.put(ePathName, "NULLVALUE");
                    }

                    if ((oldValue != null && newValue == null)) {
                        eoHashMap.put(ePathName, "NULLVALUE");
                    }
                    
                    //check if old value is equal to new value
                    if ((oldValue == null && newValue == null)) {
                        eoHashMap.remove(ePathName);
                     } else if ((oldValue != null && newValue != null) && oldValue.toString().equalsIgnoreCase(newValue.toString())) {
                        eoHashMap.remove(ePathName);
                    }
                }
            }

            Object[] keys = eoHashMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String) keys[i];
                if (eoHashMap.get(key) != null) {
                    if("NULLVALUE".equalsIgnoreCase( (String)eoHashMap.get(key) ) ) {
                        newUpdatedMap.put(key, null);
                    } else {
                      newUpdatedMap.put(key, eoHashMap.get(key));
                    }
                }
            }
            this.editSingleEOHashMap.put("ENTERPRISE_OBJECT_CODES", newUpdatedMap);


            //BUILD AND THE VALUES FOR THE MINOR OBJECTS HERE
            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //get the child object node configs
            for (int i = 0; i < childNodeConfigs.length; i++) {
                //get the child object node configs and build an array of minor objects here
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                ArrayList thisMinorObjectList = (ArrayList) this.editSingleEOHashMap.get("EOCODES" + objectNodeConfig.getName() + "ArrayList");
                for (int j = 0; j < thisMinorObjectList.size(); j++) {
                    HashMap minorObjectMap = (HashMap) thisMinorObjectList.get(j);
                    minorObjectMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName());
                //add minor objects for SBR here 

                }
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("EME036: Exception has occurred :{0}", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
               mLogger.error(mLocalizer.x("EME037: Exception has occurred :{0}", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                 mLogger.error(mLocalizer.x("EME137: Exception has occurred :{0}", ex.getMessage()), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
                   


    }

    //build an array of changed SBR values here
    public void buildModifiedSBRValues() {
        //this.changedSBRArrayList
        HashMap rootNodesHashMap = (HashMap) this.editSingleEOHashMap.get("ENTERPRISE_OBJECT_CODES");

        //build the hashmap with only modified values only


        //add root node for SBR here if at least one value is entered
        if (rootNodesHashMap.keySet().size() != 2) {
            this.changedSBRArrayList.add(rootNodesHashMap);
        }

        ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

        //get the child object node configs
        for (int i = 0; i < childNodeConfigs.length; i++) {
            //get the child object node configs and build an array of minor objects here
            ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
            ArrayList thisMinorObjectList = (ArrayList) this.editSingleEOHashMap.get("EOCODES" + objectNodeConfig.getName() + "ArrayList");
            for (int j = 0; j < thisMinorObjectList.size(); j++) {
                HashMap minorObjectMap = (HashMap) thisMinorObjectList.get(j);
                minorObjectMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName());

                //add minor objects for SBR here 
                //this.changedSBRArrayList.add(minorObjectMap);

            }
        }

    }

    //build an array of changed SBR values here for the system objects here
    public void buildModifiedSystemObjects() {

        for (int so = 0; so < this.getEoSystemObjects().size(); so++) {
            HashMap systemObjectsMap = (HashMap) this.eoSystemObjects.get(so);

            HashMap soRootNodesMap = (HashMap) systemObjectsMap.get("SYSTEM_OBJECT");
            HashMap soRootNodesMapNew = new HashMap();
            Object[] keys = soRootNodesMap.keySet().toArray();
            //build an array of hashmap with out link key
            for (int i = 0; i < keys.length; i++) {
                String objectKey = (String) keys[i];

                //ignore keys like SOSYS, SOLID and LINK_KEY
                if (!"LINK_KEY".equalsIgnoreCase(objectKey) &&
                        !"SOSYS".equalsIgnoreCase(objectKey) &&
                        !"SOLID".equalsIgnoreCase(objectKey)) {
                    soRootNodesMapNew.put(objectKey, soRootNodesMap.get(objectKey));
                }
            }
            //Remove input field maskings before saving the values
            sourceHandler.removeFieldInputMasking(soRootNodesMapNew, screenObject.getRootObj().getName());
           
            //build an array of system object hashmaps for sending to the SL
            this.editSOHashMapArrayList.add(soRootNodesMapNew);

            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //get the child object node configs
            for (int i = 0; i < childNodeConfigs.length; i++) {
                //get the child object node configs and build an array of minor objects here
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                ArrayList thisMinorObjectList = (ArrayList) systemObjectsMap.get("SOEDIT" + objectNodeConfig.getName() + "ArrayList");
                for (int j = 0; j < thisMinorObjectList.size(); j++) {
                    HashMap minorObjectMap = (HashMap) thisMinorObjectList.get(j);
                    HashMap minorObjectMapNew = new HashMap();
                    Object[] minorkeys = minorObjectMap.keySet().toArray();
                    //build an array of hashmap with out link key
                    for (int k = 0; k < minorkeys.length; k++) {
                        String objectKey = (String) minorkeys[k];
                        //ignore keys like SOSYS, SOLID and LINK_KEY
                        if (!"LINK_KEY".equalsIgnoreCase(objectKey) &&
                                !"SOSYS".equalsIgnoreCase(objectKey) &&
                                !"SOLID".equalsIgnoreCase(objectKey)) {
                            minorObjectMapNew.put(objectKey, minorObjectMap.get(objectKey));
                        }
                    }
                    this.editSOMinorObjectsHashMapArrayList.add(minorObjectMapNew);

                }
            }

        }
    }

    public boolean validateSystemCodeLID(String LID, String systemCode) {
        boolean validated = false;

        //convert the masked value here to 10 digit number
        try {
            SystemObject systemObject = masterControllerService.getSystemObject(systemCode, LID);
            if (systemObject != null) {
                validated = false;
            } else if (systemObject == null) {
                validated = true;
            }

        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
            mLogger.error(mLocalizer.x("EME037: Exception has occurred :{0}", ex.getMessage()), ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
            mLogger.error(mLocalizer.x("EME038: Exception has occurred :{0}", ex.getMessage()), ex);
        }
        return validated;
    }

    public ArrayList getEoSystemObjectsOld() {
        return eoSystemObjectsOld;
    }

    public void setEoSystemObjectsOld(ArrayList eoSystemObjectsOld) {
        this.eoSystemObjectsOld = eoSystemObjectsOld;
    }

    public HashMap getEditSingleEOHashMapOld() {
        return editSingleEOHashMapOld;
    }

    public void setEditSingleEOHashMapOld(HashMap editSingleEOHashMapOld) {
        this.editSingleEOHashMapOld = editSingleEOHashMapOld;
    }

    public HashMap getUnLockedFieldsHashMapByUser() {
        return unLockedFieldsHashMapByUser;
    }

    public void setUnLockedFieldsHashMapByUser(HashMap unLockedFieldsHashMapByUser) {
        this.unLockedFieldsHashMapByUser = unLockedFieldsHashMapByUser;
    }

    public ArrayList<SelectItem> getSystemCodes() {
        String[][] systemCodesArray = masterControllerService.getSystemCodes();
        HashMap SystemCodeDesc = new HashMap();

        String[] pullDownListItems = systemCodesArray[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            String sysDesc = masterControllerService.getSystemDescription(pullDownListItems[i]);
            String sysCode = pullDownListItems[i];
            SystemCodeDesc.put(sysCode, sysDesc);
        }
        HashMap sortedSyscode = getSortedMap(SystemCodeDesc);
        Set sysCodeSet = sortedSyscode.keySet();
        Iterator it = sysCodeSet.iterator();
        while (it.hasNext()) {
            SelectItem selectItem = new SelectItem();
            String sysCode = (String) it.next();
            String sysDesc = masterControllerService.getSystemDescription(sysCode);
            selectItem.setLabel(sysDesc);
            selectItem.setValue(sysCode);
            newArrayList.add(selectItem);
        }

        systemCodes = newArrayList;
        return systemCodes;

    }

    public HashMap getSortedMap(HashMap hmap) {
        HashMap map = new LinkedHashMap();
        List mapKeys = new ArrayList(hmap.keySet());
        List mapValues = new ArrayList(hmap.values());
        hmap.clear();
        TreeSet sortedSet = new TreeSet(mapValues);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            map.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);
        }
        return map;
    }
}
 
    

      
      
