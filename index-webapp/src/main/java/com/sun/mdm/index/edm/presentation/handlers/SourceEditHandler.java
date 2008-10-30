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
 *  
 */

package com.sun.mdm.index.edm.presentation.handlers;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.xml.bind.ValidationException;


public class SourceEditHandler {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    
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

     //Hash map for singl SO  for EDITING
    private HashMap editSingleSOHashMap = new HashMap();
    
    public static final String UPDATE_SUCCESS = "UPDATE SUCCESS";
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());    
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    
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
            mLogger.error(mLocalizer.x("SRCED001: Failed to update the SO:{0}" , ex.getMessage()),ex);
        }
        return UPDATE_SUCCESS;
   }
     
    public ArrayList getSingleSOHashMapArrayList() {
        return singleSOHashMapArrayList;
    }

    public void setSingleSOHashMapArrayList(ArrayList singleSOHashMapArrayList) {
        this.singleSOHashMapArrayList = singleSOHashMapArrayList;
    }
 

    public HashMap getEditSingleSOHashMap() {
        return editSingleSOHashMap;
    }

    public void setEditSingleSOHashMap(HashMap editSingleSOHashMap) {
        this.editSingleSOHashMap = editSingleSOHashMap;
    }
 
}
