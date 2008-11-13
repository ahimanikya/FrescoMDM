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
 * Created on December 21, 2007, 
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
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
    MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

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
    private ArrayList eoBrandNewSystemObjects;
    
    /** Creates a new instance of EditMainEuidHandler */
    public EditMainEuidHandler() {
    }

    /** 
     * Modified  on 11/07/2008
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

            //Check if the EUID is merged
            String mergedEuid  = midmUtilityManager.getMergedEuid(updateEuid);// modified as fix of 202
            if(mergedEuid != null && mergedEuid.length() > 0) {
                return "MERGED_EUID:"+mergedEuid;
            }
            
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

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME001: ValidationException occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME002: UserException occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME003: Exception occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME004: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME005: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;
    }

    public void setEditEOFields(String euid) {
        try {
            //String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject editEnterpriseObject = masterControllerService.getEnterpriseObject(euid);

            setUpdatedEOFields(editEnterpriseObject); //set the updated values here

        } catch (ProcessingException ex) {
            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
            if (exceptionMessage.indexOf("stack trace") != -1) {
                String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                if (exceptionMessage.indexOf("message=") != -1) {
                    parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                }
                mLogger.error(mLocalizer.x("EME006: Service Layer Processing Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
            } else {
                mLogger.error(mLocalizer.x("EME007: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
            }
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("EME008: Service Layer User Exception occurred"), ex);
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
     * Modified  on 11/07/2008
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
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME011: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME012: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME013: Error  occurred"), ex);
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME014: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME015: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
             return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_SUCCESS;

    //Keep the updated SO in the session again
    }

    /** 
     * Modified  on 11/07/2008
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


          } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME016: Unable to add  System Object :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME017: Unable to add  System Object :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME018: Unable to add  System Object :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME019: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME020: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
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
     * Modified  on 11/07/2008
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

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME021: Unable to save selected links :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME022: Unable to save selected links :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME023: Unable to save selected links :{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME024: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME025: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }

    
    /** 
     * Modified  on 11/07/2008
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

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME026: Unable to save selected unlinks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME027: Unable to save selected unlinks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME028: Unable to save selected unlinks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME029: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME030: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return EditMainEuidHandler.SERVICE_LAYER_ERROR;
        }
        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }

    /** 
     * Modified  on 11/07/2008
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

        }catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME031: Unable to save selected unlocks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME032: Unable to save selected unlocks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME033: Unable to save selected unlocks:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME034: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME035: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
                    return EditMainEuidHandler.SERVICE_LAYER_ERROR;
          }
        return EditMainEuidHandler.EDIT_EO_SUCCESS;
    }
    
    
    /** 
     * Added on 26/06/2008
     * 
     * Modified  on 11/07/2008
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

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME036: UserException occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME037: Exception occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME038: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME039: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME040: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
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


            HashMap editEOMapMain = midmUtilityManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);

            editEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

            HashMap eoHashMap = midmUtilityManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);
            HashMap eoHashMapOld = midmUtilityManager.getEnterpriseObjectAsHashMap(editEnterpriseObject, screenObject);

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

                //HashMap soEuidHashMap = midmUtilityManager.getSystemObjectAsHashMap(systemObject, screenObject);

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
            //If Newly added 
            ArrayList eoBrandNewSystemObjectsTemp = (ArrayList)session.getAttribute("eoBrandNewSystemObjects");
            if (eoBrandNewSystemObjectsTemp != null && eoBrandNewSystemObjectsTemp.size() > 0) {
                for (int i = 0; i < eoBrandNewSystemObjectsTemp.size(); i++) {
                    HashMap soHashMapBrandNew = (HashMap) eoBrandNewSystemObjectsTemp.get(i);
                    eoSOobjects.add(soHashMapBrandNew);
                }
            }
            
            
            //Add new select item for systemcode/lid drop down
            setEoSystemObjectCodesWithLids(newSelectItemArrayList);

            //set all system objects here
            setEoSystemObjects(eoSOobjects);

            //set all system objects here
            setEoSystemObjectsOld(eoSOobjectsOld);
            
        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME041: Unable to set updated EO fields:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME042: Unable to set updated EO fields:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME043: Unable to set updated EO fields:{0}", ex.getMessage()));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME044: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME045: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
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
            HashMap enterprisObjectDBMap = midmUtilityManager.getEnterpriseObjectAsHashMapFromDB(eo, screenObject);

            //get the child object node configs
            for (int i = 0; i < childNodeConfigs.length; i++) {
                //get the child object node configs and build an array of minor objects here
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                ArrayList thisMinorObjectList = (ArrayList) this.editSingleEOHashMap.get("EOCODES" + objectNodeConfig.getName() + "ArrayList");
                ArrayList thisMinorObjectListDB = (ArrayList) enterprisObjectDBMap.get("EOCODES" + objectNodeConfig.getName() + "ArrayList");
 
                ArrayList updatedMinorObjectList = new ArrayList();
                for (int j = 0; j < thisMinorObjectList.size(); j++) {
                    HashMap minorObjectMap = (HashMap) thisMinorObjectList.get(j);
                     if (minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE).toString().equals(MasterControllerService.MINOR_OBJECT_UPDATE)) {
                        //add minor objects for SBR here 
                         minorObjectMap = getDifferenceMinorObjectMap(thisMinorObjectListDB, minorObjectMap);
                         if (minorObjectMap != null) {
                            updatedMinorObjectList.add(minorObjectMap);
                        }
                     } else {
                        updatedMinorObjectList.add(minorObjectMap);
                    }

 
                }
                 //update the minor object arraylist here
                this.editSingleEOHashMap.put("EOCODES" + objectNodeConfig.getName() + "ArrayList", updatedMinorObjectList);
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME046: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME047: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME048: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME049: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME050: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
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
                this.changedSBRArrayList.add(minorObjectMap);

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
                ArrayList thisMinorObjectList = (systemObjectsMap.get("SOEDIT" + objectNodeConfig.getName() + "ArrayList")!=null)?(ArrayList) systemObjectsMap.get("SOEDIT" + objectNodeConfig.getName() + "ArrayList"):new ArrayList();
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
            //check for the newly added so available in memory
           ArrayList eosystemobjects = (ArrayList)session.getAttribute("eoBrandNewSystemObjects"); //fix for 140
 	   if(eosystemobjects!=null && eosystemobjects.size()>0){
		   for(Object obj:eosystemobjects){
			HashMap tempMap = (HashMap)obj;
			if(tempMap.get("SYSTEM_CODE")!=null && tempMap.get("LID")!=null && 
				tempMap.get("SYSTEM_CODE").toString().equalsIgnoreCase(systemCode) && 
				tempMap.get("LID").toString().equalsIgnoreCase(LID)){
                            validated = false;
 			}
		   }
 	   }
 
        }catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("EME051: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("EME052: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("EME053: Exception has occurred :{0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("EME054: Service Layer Processing Exception occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("EME055: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
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
    
/** 
     * Addded  on 05/08/2008 <br>
     * 
     * This method is used to compare the minor objects hashmap with the arraylist of hashmap and will return the hash map with only modified values<br>
     * 
     * 
     * @param minorObjectList 
     * @param checkMinorObjectMap 
     * @return HashMap  - HashMap with (K,true/false) <br>
     * 
     **/

        public HashMap getDifferenceMinorObjectMap(ArrayList minorObjectList, HashMap checkMinorObjectMap) {
        HashMap returnHashMap = new HashMap();
        Object[] keyset = checkMinorObjectMap.keySet().toArray();
        
        HashMap matchHashMap  = new HashMap();
        for (Object object : minorObjectList) {
            HashMap map = (HashMap) object;
            //Check for the minor object id and the minor object type for comparing
             if (checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_ID).toString().equals(map.get(MasterControllerService.MINOR_OBJECT_ID).toString())) {
                    matchHashMap = map;
             }
         }
        int countDiff = 0;
        //If the matching map is not empty 
        if (!matchHashMap.isEmpty()) {
            if (checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_ID).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_ID).toString()) && checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString())) {
                for (int i = 0; i < matchHashMap.size(); i++) {
                    if (!keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_ID) &&
                            !keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_TYPE) &&
                            !keyset[i].toString().equals(MasterControllerService.HASH_MAP_TYPE)) {
                        if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) == null) {
                            returnHashMap.put(keyset[i], checkMinorObjectMap.get(keyset[i]));
                        } else if (matchHashMap.get(keyset[i]) == null && checkMinorObjectMap.get(keyset[i]) != null) {
                            returnHashMap.put(keyset[i], checkMinorObjectMap.get(keyset[i]));
                        } else if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) != null && !matchHashMap.get(keyset[i]).toString().equals(checkMinorObjectMap.get(keyset[i]).toString())) {
                            returnHashMap.put(keyset[i], checkMinorObjectMap.get(keyset[i]));
                        } else {
                            checkMinorObjectMap.remove(i);
                        }
                    }
                    
                }
                returnHashMap.put(MasterControllerService.MINOR_OBJECT_ID, checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_ID));
                returnHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE));
                returnHashMap.put(MasterControllerService.HASH_MAP_TYPE, checkMinorObjectMap.get(MasterControllerService.HASH_MAP_TYPE));
            }

        }
        if(checkMinorObjectMap.size()==3) {
            return null;
        } else {
            return returnHashMap;
        }
    }

    public ArrayList getEoBrandNewSystemObjects() {
         return eoBrandNewSystemObjects;
    }

    public void setEoBrandNewSystemObjects(ArrayList eoBrandNewSystemObjects) {
        this.eoBrandNewSystemObjects = eoBrandNewSystemObjects;
    }
    
}
 
