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
 * CompareDuplicateManager.java 
 * Created on November 29, 2007
 * Author : Anil, Pratibha
 *  
 */

package com.sun.mdm.index.edm.presentation.managers;
import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.presentation.handlers.NavigationHandler;
import com.sun.mdm.index.edm.presentation.handlers.SourceHandler;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.master.ConnectionInvalidException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.page.PageException;
import java.rmi.RemoteException;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.event.*;

import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import net.java.hulp.i18n.LocalizationSupport;

public class CompareDuplicateManager {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager");
    private static transient final Localizer mLocalizer = Localizer.get();
    public static final String MINOR_OBJECT_TYPE = "MINOR_OBJECT_TYPE";
    private MasterControllerService masterControllerService = new MasterControllerService();
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
     String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    /**
     *Http session variable
     */
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    /** Creates a new instance of CompareDuplicateManager*/
    public CompareDuplicateManager() {
    }

     
    public EPathArrayList retrieveResultsFields(ArrayList arlResultsConfig) throws Exception {
        EPathArrayList arlResultFields = new EPathArrayList();
        SearchResultsConfig searchResultConfig = null;
        ArrayList arlEPaths = null;
        Iterator ePathsIterator = null;
        Iterator resultConfigIterator = arlResultsConfig.iterator();
        while (resultConfigIterator.hasNext()) {
            searchResultConfig = (SearchResultsConfig) resultConfigIterator.next();
            arlEPaths = searchResultConfig.getEPaths();
            ePathsIterator = arlEPaths.iterator();
            while (ePathsIterator.hasNext()) {
                String strEPath = (String) ePathsIterator.next();
                arlResultFields.add(strEPath);
            }
        }
        return arlResultFields;
    }

        public EPathArrayList retrieveEpathResultsFields(ArrayList arlResultsConfig) throws Exception {
        EPathArrayList arlResultFields = new EPathArrayList();
        SearchResultsConfig searchResultConfig = null;
        ArrayList arlEPaths = null;
        Iterator ePathsIterator = null;
        Iterator resultConfigIterator = arlResultsConfig.iterator();
        while (resultConfigIterator.hasNext()) {
            searchResultConfig = (SearchResultsConfig) resultConfigIterator.next();
            arlEPaths = searchResultConfig.getEPaths();
            ePathsIterator = arlEPaths.iterator();
            while (ePathsIterator.hasNext()) {
                String strEPath = (String) ePathsIterator.next();
                  // copy EPath strings to the EPathArrayList
                arlResultFields.add(strEPath);
            }
        }
       return arlResultFields;
    }

        public static Collection getFieldValue(ObjectNode objNode, EPath epath) {

        int ePathIndicesCount = epath.getIndices().length;
        // the last parent object in the hierarchy will be located here
        String ePathObjectTag = epath.getTag(ePathIndicesCount - 2);
        Collection c = null;
        // check if the ePathObjectTag is one of the children of the objNode.
        if (isChild(objNode, ePathObjectTag)) {
            try {
                c = QwsUtil.getValueForField(objNode, epath.getName(), null);
                return c;
            } catch (ObjectException ex) {
                //Throwing object exception
                mLogger.error(mLocalizer.x("CPD001: Failed to get field values :{0}", ex.getMessage()),ex);
            }
        } else {    // Check the children using a depth-first search.
            ArrayList childNodes = objNode.pGetChildren();
            
            if (childNodes != null && childNodes.size() > 0) {
                Iterator childIter = childNodes.iterator();
                while (childIter.hasNext() && c == null) {
                    ObjectNode childNode = (ObjectNode) childIter.next();
                    c = getFieldValue(childNode, epath);
                }
            } else {
                return null;    // terminate search if no children are found
            }
        }
        return c;
    }

    private static boolean isChild(ObjectNode objNode, String ePathObjectTag) {
        ArrayList allChildren = objNode.pGetChildren();
        if (allChildren != null) {
            for (int i = 0; i < allChildren.size(); i++) {
                ObjectNode obj = (ObjectNode) allChildren.get(i);
                String childNodeTag = obj.pGetTag();
                if (ePathObjectTag.equalsIgnoreCase(childNodeTag)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 
     * @param EUID
     * @return
     */
    public EnterpriseObject getEnterpriseObject(String EUID)  {
        EnterpriseObject enterpriseObject = null;
        try {
            enterpriseObject = masterControllerService.getEnterpriseObject(EUID);
        } catch (Exception ex) {
           mLogger.error(mLocalizer.x("CPD002: Failed to get EnterpriseObject :{0}", ex.getMessage()),ex);
        }
        return enterpriseObject;
    }

    /**
     * 
     * @param screenObject
     * @return
     * @throws java.lang.Exception
     */
    public EPathArrayList retrieveEPathArrayList(ScreenObject screenObject) throws Exception {
        // arlResultsConfig ArrayList to be used by Service Layer to fetch serach results from EDM.xml
        ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();
        return  retrieveResultsFields(arlResultsConfig);
  }

    
    /**
     * 
     * @param enterpriseObject
     * @param screenObjectVar 
     * @return java.util.HashMap
     * @throws java.lang.Exception 
     */
    public HashMap getEOFieldValues(EnterpriseObject enterpriseObject, ScreenObject screenObjectVar,Object[] resultsConfigFeilds) throws Exception {
        //get EPathArrayList using the screen object      
        EPathArrayList ePathArrayList = retrieveEPathArrayList(screenObjectVar);

        HashMap eoFieldValuesMap = new HashMap();
       
        Collection fieldvalues;
        ArrayList fieldValuesArrayList = new ArrayList();

        //ObjectNode objectNode = systemObject.getObject();
        ObjectNode objectNode = enterpriseObject.getSBR().getObject();
        //for (int m = 0; m < ePathArrayList.size(); m++) {
          //  EPath ePath = ePathArrayList.get(m);
        for(int i=0 ;i < resultsConfigFeilds.length;i++) {
            FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
            String field = fieldConfig.getFullFieldName();
            if (!field.startsWith(screenObjectVar.getRootObj().getName())) {
                field = screenObjectVar.getRootObj().getName() + "." + field;
            }
            
            // ObjectField field  = objectNode.getField(ePath.getName());  
            try {
                Object value = EPathAPI.getFieldValue(field, objectNode);
                eoFieldValuesMap.put(field, value);
            } catch (Exception npe) {
              mLogger.error(mLocalizer.x("CPD003: Unable to get value from child objects  :{0}", npe.getMessage()),npe);
            // THIS SHOULD BE FIXED
            // npe.printStackTrace();
            }
        }
        //eoFieldValuesMap.put("EUID",enterpriseObject.getEUID());// Set the EUID

        return eoFieldValuesMap;
    }
    
    public HashMap getEOFieldValues(EnterpriseObject enterpriseObject, ScreenObject screenObjectVar) throws Exception {

        //get EPathArrayList using the screen object      
        EPathArrayList ePathArrayList = retrieveEPathArrayList(screenObjectVar);
      
        HashMap eoFieldValuesMap = new HashMap();
       
        Collection fieldvalues;
        ArrayList fieldValuesArrayList = new ArrayList();

        //ObjectNode objectNode = systemObject.getObject();
        ObjectNode objectNode = enterpriseObject.getSBR().getObject();
        for (int m = 0; m < ePathArrayList.size(); m++) {
            EPath ePath = ePathArrayList.get(m);
            
            // ObjectField field  = objectNode.getField(ePath.getName());  
            try {
                Object value = EPathAPI.getFieldValue(ePath, objectNode);
                eoFieldValuesMap.put(ePath.toString(), value);
            } catch (Exception npe) {
               mLogger.error(mLocalizer.x("CPD004: Unable to get value from child objects  :{0}", npe.getMessage()),npe);
            // THIS SHOULD BE FIXED
            // npe.printStackTrace();
            }
        }
        //eoFieldValuesMap.put("EUID",enterpriseObject.getEUID());// Set the EUID

        return eoFieldValuesMap;
    }
    /**
     * 
     * @param EUID
     * @return ArrayList
     */
    public ArrayList viewHistoryForEuid(String EUID)  {
        ArrayList euidHistoryArrayList = null;
        try {
            euidHistoryArrayList = masterControllerService.viewHistory(EUID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return euidHistoryArrayList;

    }

    public String getPotentialDupStatus(String mainEuid, String dupId) {
        String dupStatus = null;
        try {
            dupStatus = masterControllerService.getPotentialDuplicateStatus(mainEuid, dupId);
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("CPD005: Failed to get Potential duplicate status  :{0}", ex.getMessage()),ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("CPD006: Failed to get Potential duplicate status  :{0}", ex.getMessage()),ex);
        } catch (RemoteException ex) {
           mLogger.error(mLocalizer.x("CPD007: Failed to get Potential duplicate status   :{0}", ex.getMessage()),ex);
        }
        return dupStatus;
    
    }
    
    // Added by Pratibha for fetching Header subscripts
    public String[] getSubscript(int size)
	{  String subscript[] = new String[size];
           String  strCounter = new String("");
           
           for(int counter=0;counter<size;counter++)
           {    strCounter = Integer.toString(counter);
                if (strCounter.endsWith("11")||strCounter.endsWith("12")||strCounter.endsWith("13"))
                    {subscript[counter] = "th";
                    }
		else if (strCounter.endsWith("1"))
                    {subscript[counter] = "st";
                    }
		else if (strCounter.endsWith("2"))
                    {subscript[counter] = "nd";
                    }
		else 	if (strCounter.endsWith("3"))
                    {subscript[counter] = "rd";
                    }
                //FIXED the multiple of 10s bug 
                else if (counter == 0) {
                    subscript[counter] = "Main EUID";
                }
                else
                    {subscript[counter] = "th";
                    }
           }        
		return subscript;
	}

    public HashMap getSystemObjectAsHashMap(SystemObject systemObject, ScreenObject screenObject) {
        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();
        HashMap systemObjectHashMap = new HashMap();

        try {
            
            ConfigManager.init();
            //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
            //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
            boolean hasEOSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(masterControllerService.getEnterpriseObjectForSO(systemObject).getSBR()) : true;
 
            //add SystemCode and LID value to the new Hash Map
            systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
            systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, masterControllerService.getSystemDescription(systemObject.getSystemCode()));
            systemObjectHashMap.put("Status", systemObject.getStatus()); // set Status here

            HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));
            
            HashMap editSystemObjectHashMapUpdate = masterControllerService.getSystemObjectAsHashMap(systemObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));

            //add SystemCode and LID value to the new Hash Map
            editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
            editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
            editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE); // set UPDATE TYPE HERE

            //add SystemCode and LID value to the new Hash Map
            editSystemObjectHashMapUpdate.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
            editSystemObjectHashMapUpdate.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
            editSystemObjectHashMapUpdate.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE); // set UPDATE TYPE HERE
            systemObjectHashMap.put("SYSTEM_OBJECT_EDIT", editSystemObjectHashMapUpdate); // Set the edit SystemObject here
            
            if(hasEOSensitiveData) {
               systemObjectHashMap.put("hasSensitiveData", "true"); // Set the boolean value if the object node contains VIP Data
            }
            
            FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

            String strVal = new String();

            for (int i = 0; i < rootFieldConfigs.length; i++) {
                FieldConfig fieldConfig = rootFieldConfigs[i];
                Object value = editSystemObjectHashMap.get(fieldConfig.getFullFieldName());
                //set the menu list values here
                if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                    if (value != null) {
                        //SET THE VALUES WITH USER CODES AND VALUE LIST
                        if (fieldConfig.getUserCode() != null) {
                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                        } else {
                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                        }


                        //strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                        editSystemObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                    }

                } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                    if (value != null) {
                        //Mask the value as per the masking 
                         value = fieldConfig.mask(value.toString());
                         editSystemObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                    }
                }

            }

            //add SystemCode and LID value to the new Hash Map
            editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
            editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
            editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE); // set UPDATE TYPE HERE

            systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap); // Set the edit SystemObject here

            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //Build and array of minotr object values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs(); 

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                
                ArrayList soMinorObjectsMapArrayListEdit = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);;


                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMapEdit = (HashMap) soMinorObjectsMapArrayListEdit.get(k);

                    minorObjectHashMapEdit.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                    minorObjectHashMapEdit.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                    minorObjectHashMapEdit.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                }

                //set the values for the minor objects with keys only
                systemObjectHashMap.put("SOEDIT" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListEdit); // set SO addresses as arraylist here

                
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


                                //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
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
                systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayListSize", new Integer(soMinorObjectsMapArrayList.size())); // set SO addresses as arraylist here
                
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("CPD008: Failed to get SystemObject   :{0}", ex.getMessage()),ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("CPD009: Failed to get SystemObject  :{0}", ex.getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("CPD090: Failed to get SystemObject  :{0}", ex.getMessage()),ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
           

        return systemObjectHashMap;

    }
    
     
     /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used to get the enterprise object as hash map.  The returned hashmap will contain the in information about <br>
       * SBR - root node information as hashmap  <br>
       * SBR - minorobjects as arraylist of hashmaps <br>
       * History as ArrayList of hashmap with function+date as key <br>
       * Sources as Arraylist of hashmap with SystemCode/LID <br>
       * Link/Lock related informatin <br>
       * 
     * 
     * @param enterpriseObject 
     * @param screenObject 
     * @return HashMap 
     * 
     */

    public HashMap getEnterpriseObjectAsHashMap(EnterpriseObject enterpriseObject, ScreenObject screenObject) {
            HashMap enterpriseObjectHashMap = new HashMap();
            SourceHandler sourceHandler = new SourceHandler();
            String rootNodeName = screenObject.getRootObj().getName();
         try {
            //add SystemCode and LID value to the new Hash Map
            HashMap editEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(enterpriseObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));
            HashMap codesEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(enterpriseObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));
            
            FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

            String strVal = new String();

            for (int i = 0; i < rootFieldConfigs.length; i++) {
                 FieldConfig fieldConfig = rootFieldConfigs[i];
                Object value  = editEnterpriseObjectHashMap.get(fieldConfig.getFullFieldName());
                //set the menu list values here
                if(fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                    if(value != null) {
                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                        if (fieldConfig.getUserCode() != null) {
                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                        } else {
                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                        }
                                  
                       // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                        editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                    }
                    
                } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                     if (value != null) {
                        //Mask the value as per the masking 
                         value = fieldConfig.mask(value.toString());
                         editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                     }

                }
            
            }
 
            HashMap newLinkedHashMap = new HashMap();
            HashMap eoWithLinkedHashMap = masterControllerService.getLinkedFields(enterpriseObject);
            Object[] keySet = editEnterpriseObjectHashMap.keySet().toArray();
            //BUILD the hash map with links
            for (int i = 0; i < keySet.length; i++) {
                String key = (String) keySet[i];
                if (eoWithLinkedHashMap.get(key) != null) {
                    String[] sysLid = ((String)eoWithLinkedHashMap.get(key)).split(":");
                    if (sysLid.length == 2) {
                        HashMap soHashMapCodes = (HashMap) getSystemObjectAsHashMap(masterControllerService.getSystemObject(sysLid[0], sysLid[1]), screenObject).get("SYSTEM_OBJECT_EDIT");
                        HashMap soHashMap = (HashMap) getSystemObjectAsHashMap(masterControllerService.getSystemObject(sysLid[0], sysLid[1]), screenObject).get("SYSTEM_OBJECT");
                        //put the linked values here for the SBR from the linked system object
                        editEnterpriseObjectHashMap.put(key, soHashMap.get(key));
                        codesEnterpriseObjectHashMap.put(key, soHashMapCodes.get(key));
                    }
                    
                    newLinkedHashMap.put(key, true);
                } else {
                    newLinkedHashMap.put(key, false);
                }
            }

            //add SystemCode and LID value to the new Hash Map  
            editEnterpriseObjectHashMap.put("EUID", enterpriseObject.getEUID()); // set EUID here
             
            session.setAttribute("SBR_REVISION_NUMBER"+enterpriseObject.getEUID(), enterpriseObject.getSBR().getRevisionNumber());
            
            
            editEnterpriseObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT", editEnterpriseObjectHashMap); // Set the edit EnterpriseObject here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_CODES", codesEnterpriseObjectHashMap); // Set the edit EnterpriseObject here

            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_HISTORY", getEoHistory(enterpriseObject.getEUID(), screenObject));
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_SOURCES", getEoSources(enterpriseObject, screenObject));

            enterpriseObjectHashMap.put("EO_STATUS", enterpriseObject.getStatus()); // Set the edit EnterpriseObject here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_LINKED", newLinkedHashMap); // Set the edit EnterpriseObject here

            ConfigManager.init();
            //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
            //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
            boolean hasSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null ) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(enterpriseObject.getSBR()):true;

            if(hasSensitiveData) {
               enterpriseObjectHashMap.put("hasSensitiveData", "true"); // Set the boolean value if the object node contains VIP Data
            }
             
            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //Build and array of minor object values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs(); 

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                ArrayList soMinorObjectsMapArrayListCodes = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMapCodes = (HashMap) soMinorObjectsMapArrayListCodes.get(k);
                    minorObjectHashMapCodes.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                }
                enterpriseObjectHashMap.put("EOCODES" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListCodes); // set SO addresses as arraylist here
                
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE

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

                                //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
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
                }

                enterpriseObjectHashMap.put("EOCODES" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListCodes); // set SO addresses as arraylist here
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayListSize", new Integer(soMinorObjectsMapArrayList.size())); // set SO addresses as arraylist here
            }

            ArrayList newMinorObjectsLinkedList = new ArrayList();

            //Build and array of minor objects with links  values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);

                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    HashMap minorObjectsLinkedHashMap = masterControllerService.getLinkValuesForChildren(enterpriseObject, objectNodeConfig.getName(), (String) minorObjectHashMap.get(MasterControllerService.MINOR_OBJECT_ID));

                    Object[] keySetMinor = minorObjectHashMap.keySet().toArray();
                    HashMap newMinorLinkedHashMap = new HashMap();
                    newMinorLinkedHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    for (int l = 0; l < keySetMinor.length; l++) {
                        String key = (String) keySetMinor[l];
                        if (minorObjectsLinkedHashMap.get(key) != null) {
                            newMinorLinkedHashMap.put(key, true);
                        } else {
                            newMinorLinkedHashMap.put(key, false);
                        }
                    }
                    newMinorObjectsLinkedList.add(newMinorLinkedHashMap);
                }
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayListLink", newMinorObjectsLinkedList); // set SO addresses as arraylist here
            }


            return enterpriseObjectHashMap;
//        } catch (ObjectException ex) {
//           mLogger.error(mLocalizer.x("CPD010: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
//        } catch (ConnectionInvalidException ex) {
//           mLogger.error(mLocalizer.x("CPD011: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
//        } catch (OPSException ex) {
//            mLogger.error(mLocalizer.x("CPD012: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
//        } catch (ProcessingException ex) {
//            mLogger.error(mLocalizer.x("CPD013: Failed to get EnterpriseObject  :{0}", ex.getMessage()));
//        } catch (UserException ex) {
//            mLogger.error(mLocalizer.x("CPD014: Failed to get EnterpriseObject  :{0}", ex.getMessage()));
//        }
         } catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("CPD010: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    } else if (ex instanceof UserException) {
                         mLogger.error(mLocalizer.x("CPD011: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    } else if (!(ex instanceof ProcessingException)) {
                       mLogger.error(mLocalizer.x("CPD012: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
               }
                   

            return enterpriseObjectHashMap;
    }
    
    
    
    public ArrayList getEoHistory(String euid, ScreenObject screenObject) {
        EnterpriseObject eoHist = null;
        ArrayList newArrayListHistory = new ArrayList();
        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();

        try {
            FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

            String strVal = new String();

            ArrayList viewHistoryEOList = masterControllerService.viewHistory(euid);
           // Commented By Anil (fix for 6677999,6681656,6685729), this Hashmap has to be local 
		   //HashMap histEOMap = new HashMap();
            for (int i = 0; i < viewHistoryEOList.size(); i++) {
			
				// Start fix for 6677999,6681656,6685729
				HashMap histEOMap = new HashMap();

				// End fix  

                HashMap objectHistMap = (HashMap) viewHistoryEOList.get(i);
                String key = (String) objectHistMap.keySet().toArray()[0];
                HashMap objectHistMapUpdated = new HashMap();
                if (objectHistMap.get(key) != null) {
                    eoHist = (EnterpriseObject) objectHistMap.get(key);
                    
                    HashMap editEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(eoHist, sourceHandler.buildSystemObjectEpaths(rootNodeName));
                   
                    for (int r = 0; r < rootFieldConfigs.length; r++) {
                        FieldConfig fieldConfig = rootFieldConfigs[r];
                        Object value = editEnterpriseObjectHashMap.get(fieldConfig.getFullFieldName());
                        //set the menu list values here
                        if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                            if (value != null) {
                                strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                            }
                        } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                            if (value != null) {
                                //Mask the value as per the masking 
                                 value = fieldConfig.mask(value.toString());
                                 editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                            }
                        }

                    }

                    //add SystemCode and LID value to the new Hash Map  
                    histEOMap.put("EUID", eoHist.getEUID()); // set EUID 
                    histEOMap.put("EO_STATUS", eoHist.getStatus()); // set Status of EO here
                     histEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

                    histEOMap.put("ENTERPRISE_OBJECT", editEnterpriseObjectHashMap); // Set the edit EnterpriseObject here

                    ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();


                    //Build and array of minor object values from the screen object child object nodes
                    for (int j = 0; j < childNodeConfigs.length; j++) {

                        //get the child object node configs
                        ObjectNodeConfig objectNodeConfig = childNodeConfigs[j];

                        FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs(); 

                        //set address array list of hasmap for editing
                        ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(eoHist, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                        for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                            HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                            minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE

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
                                        
                                        //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                        minorObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                                    }
                                    
                                }  else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                                    if (value != null) {
                                        //Mask the value as per the masking 
                                         value = fieldConfig.mask(value.toString());
                                         minorObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                                    }
                                 }

                            }
                        }

                        histEOMap.put("EO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set EO minor objects here as arraylist here
                        histEOMap.put("EO" + objectNodeConfig.getName() + "ArrayListSize", new Integer(soMinorObjectsMapArrayList.size()).intValue()); // set SO addresses as arraylist here
                    }

                    objectHistMapUpdated.put(key, histEOMap);

                    newArrayListHistory.add(objectHistMapUpdated);

                }
            }
//        } catch (ProcessingException ex) {
//             mLogger.error(mLocalizer.x("CPD015: Failed to get EnterpriseObject history :{0}", ex.getMessage()),ex);
//        } catch (UserException ex) {
//             mLogger.error(mLocalizer.x("CPD016: Failed to get EnterpriseObject history :{0}", ex.getMessage()),ex);
//        } catch (RemoteException ex) {
//             mLogger.error(mLocalizer.x("CPD017: Failed to get EnterpriseObject history :{0}", ex.getMessage()),ex);
//        }
        }catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("CPD015: Failed to get EnterpriseObject history :{0}", ex.getMessage()),ex);
                    } else if (ex instanceof UserException) {
                        mLogger.error(mLocalizer.x("PDH086: Service Layer User Exception occurred"), ex);
                    } else if (!(ex instanceof ProcessingException)) {
                        mLogger.error(mLocalizer.x("CPD017: Failed to get EnterpriseObject history :{0}", ex.getMessage()),ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                }
                   
        return newArrayListHistory;
    }

    
    public ArrayList getEoSources(EnterpriseObject enterpriseObject, ScreenObject screenObject) {
        ArrayList newArrayList = new ArrayList();
        Collection itemsSource = enterpriseObject.getSystemObjects();

        Iterator iterSources = itemsSource.iterator();

        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();
        
        FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

        String strVal = new String();

        try {
            while (iterSources.hasNext()) {
                HashMap systemObjectHashMap = new HashMap();

                SystemObject systemObject = (SystemObject) iterSources.next();
                systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, masterControllerService.getSystemDescription(systemObject.getSystemCode()));
                systemObjectHashMap.put("Status", systemObject.getStatus()); // set Status here
                HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));

                //add SystemCode and LID value to the new Hash Map
                editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());
                editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE); // set UPDATE TYPE HERE

                systemObjectHashMap.put("SYSTEM_OBJECT_EDIT", editSystemObjectHashMap); // Set the edit EnterpriseObject here
                
                for (int r = 0; r < rootFieldConfigs.length; r++) {
                    FieldConfig fieldConfig = rootFieldConfigs[r];
                    Object value = editSystemObjectHashMap.get(fieldConfig.getFullFieldName());
                    //set the menu list values here
                    if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                        if (value != null) {
                            //SET THE VALUES WITH USER CODES AND VALUE LIST 
                            if (fieldConfig.getUserCode() != null) {
                                strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                            } else {
                                strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                            }

                            //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                            editSystemObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                        }
                    } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                        if (value != null) {
                            //Mask the value as per the masking 
                             value = fieldConfig.mask(value.toString());
                             editSystemObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                        }
                    }

                }
                
                
                
                //add SystemCode and LID value to the new Hash Map
                editSystemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());
                editSystemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                editSystemObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SYSTEM_OBJECT_UPDATE); // set UPDATE TYPE HERE
                systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap); // Set the edit SystemObject here

                
                ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

                //get the child object node configs
                for (int i = 0; i < childNodeConfigs.length; i++) {

                    //get the child object node configs
                    ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];

                    FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs(); 
                    
                    ArrayList soMinorObjectsMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                    ArrayList soMinorObjectsMapArrayListEdit = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                    for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                        HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);

                        //Build an array of edit so minor objects here 
                        HashMap minorObjectHashMapEdit = (HashMap) soMinorObjectsMapArrayListEdit.get(k);
                        
                        for (int m = 0; m < minorFiledConfigs.length; m++) {
                            FieldConfig fieldConfig = minorFiledConfigs[m];
                            Object value = minorObjectHashMap.get(fieldConfig.getFullFieldName());
                            //set the menu list values here
                            if (fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                                minorObjectHashMapEdit.put(fieldConfig.getFullFieldName(), value);
                                if (value != null) {
                                    //SET THE VALUES WITH USER CODES AND VALUE LIST 
                                    if (fieldConfig.getUserCode() != null) {
                                        strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                    } else {
                                        strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                    }
                                    //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
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

                        //Edit fields here  
                        minorObjectHashMapEdit.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                        minorObjectHashMapEdit.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                        minorObjectHashMapEdit.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    }
                    systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO minor objects list here
                    systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayListSize", new Integer(soMinorObjectsMapArrayList.size()).intValue()); // set SO minor objects size here
                    systemObjectHashMap.put("SOEDIT" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
                }
                newArrayList.add(systemObjectHashMap);
            }

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("CPD018: Failed to get EOSources :{0}", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("CPD019: Failed to get EOSources :{0}", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("CPD091: Failed to get EOSources :{0}", ex.getMessage()), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
                   
        return newArrayList;
    }

    public int getMinorObjectsMaxSize(ArrayList enterpriseObjectHashMapList, ScreenObject screenObject, String childObjectName) {

        ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();
        
        ArrayList maxMinorObjectsCount  = new ArrayList();
        
        for (int j = 0; j < enterpriseObjectHashMapList.size(); j++) {
            
           //Build and array of minor object values from the screen object child object nodes for the EO
            HashMap valuesObjectHashMap = (HashMap) enterpriseObjectHashMapList.get(j);
            for (int i = 0; i < childNodeConfigs.length; i++) {
                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                if (childObjectName.equalsIgnoreCase(objectNodeConfig.getName())) {
                    //add minor objects count for the Enterprise objects here
                    maxMinorObjectsCount.add((Integer) valuesObjectHashMap.get("EO" + childObjectName + "ArrayListSize")) ;
                }
            }
            
//            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_SOURCES", getEoSources(enterpriseObject, screenObject));
            //Build and array of minor object values from the screen object child object nodes for the EO
            ArrayList soMinorObjectsList  = (ArrayList) valuesObjectHashMap.get("ENTERPRISE_OBJECT_SOURCES");
            for (int so = 0; so < soMinorObjectsList.size(); so++) {
                HashMap soHashMap = (HashMap) soMinorObjectsList.get(so);

                for (int i = 0; i < childNodeConfigs.length; i++) {
                    //get the child object node configs
                    ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                    if (childObjectName.equalsIgnoreCase(objectNodeConfig.getName())) {
                        //add minor objects count for the Enterprise objects here
                        maxMinorObjectsCount.add((Integer) soHashMap.get("SO" + childObjectName + "ArrayListSize"));
                    }
                }
            }
           
            //Build and array of minor object values from the screen object child object nodes for the EO
//            enterpriseObjectHashMap.put("", getEoHistory(enterpriseObject.getEUID(), screenObject));

            
            ArrayList historyMinorObjectsList  = (ArrayList) valuesObjectHashMap.get("ENTERPRISE_OBJECT_HISTORY");
            for (int hi = 0; hi < historyMinorObjectsList.size(); hi++) {
                HashMap historyHashMap = (HashMap) historyMinorObjectsList.get(hi);

                for (int i = 0; i < childNodeConfigs.length; i++) {
                    //get the child object node configs
                    ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                    if (childObjectName.equalsIgnoreCase(objectNodeConfig.getName())) {
                        //add minor objects count for the Enterprise objects here
                        maxMinorObjectsCount.add((Integer) historyHashMap.get("EO" + childObjectName + "ArrayListSize"));
                    }
                }
            }
           
        }
   
        //build and array of integers for sorting
        int[] countsFinalArray = new int[maxMinorObjectsCount.size()];

        //filter the array list for the array list of max minor object sizes
        for (int j = 0; j < maxMinorObjectsCount.size(); j++) {
            countsFinalArray[j] = ((Integer)  maxMinorObjectsCount.get(j) != null)?((Integer)  maxMinorObjectsCount.get(j)).intValue():0;
        }
   
        //Sort the final array to get the max value out of all values
        Arrays.sort(countsFinalArray);
       
        return countsFinalArray[countsFinalArray.length-1];
    }

    public int getSOMinorObjectsMaxSize(ArrayList valuesObjectHashMapList, ScreenObject screenObject, String childObjectName) {
        ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();
        int[] countsArray = new int[valuesObjectHashMapList.size()];
   
        //Build and array of minor object values from the screen object child object nodes
        for (int j = 0; j < valuesObjectHashMapList.size(); j++) {
            HashMap valuesObjectHashMap = (HashMap) valuesObjectHashMapList.get(j);
            for (int i = 0; i < childNodeConfigs.length; i++) {
                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                if (childObjectName.equalsIgnoreCase(objectNodeConfig.getName())) {
                    countsArray[j] = ((Integer) valuesObjectHashMap.get("SO" + childObjectName + "ArrayListSize")).intValue();
                }
            }
        }
        Arrays.sort(countsArray);
        return countsArray[countsArray.length-1];
    }
    
	// Method Added for initcap function
    public String getStatus(String strStatus) {
        String name = "";
        if (strStatus != null) {
            strStatus = strStatus.trim();
            name = String.valueOf(strStatus.charAt(0)).toUpperCase() + strStatus.substring(1);
        }
        return name;
    }

       /** 
     * Addded  By Rajani Kanth  on 07/08/2008 <br>
     * 
     * This method is used to check the overwrites for the entered minor object.
     * 
     * 
     * @param editEuid  - EUID
     * @param minorObjectHashMap - Minor object hashmap
     * @return boolean  
        * <b>true</b> - if the overwrites are found for all the minor object keys <br>
        * <b>false</b> - if total number of overwrites doesnot match the minor object keys<br>
     * 
     **/

    
    public boolean checkOverWrites(String editEuid, HashMap minorObjectHashMap) throws ObjectException, ProcessingException, UserException {
        boolean ovwerWriteFound = false;
        EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(editEuid);
        SBR sbr = enterpriseObject.getSBR();

        //calculate the total number of keys in the hashmap
        int totalKeysSize = minorObjectHashMap.keySet().size() - 3;

        int countOverwrites = 0;

        if (sbr != null) {
            ObjectNode minorObjectNode = sbr.getObject().getChild((String) minorObjectHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE), (String) minorObjectHashMap.get(MasterControllerService.MINOR_OBJECT_ID));
            for (Object obj : minorObjectHashMap.keySet()) {
                // Object value = hm.get(obj);
                String key = obj.toString();
                // setObjectNodeFieldValue(minorObject, (String) obj, (String) value);
                if (!key.toString().equals(MasterControllerService.MINOR_OBJECT_ID) &&
                        !key.toString().equals(MasterControllerService.MINOR_OBJECT_TYPE) &&
                        !key.toString().equals(MasterControllerService.HASH_MAP_TYPE)) {
                     key = key.substring(key.indexOf(".")+1, key.length());
                     SBROverWrite overWriteObj = QwsUtil.getOverWrite(sbr, minorObjectNode, key);
                    if (overWriteObj != null) {
                         //increment the number of overwrites here
                        countOverwrites++;
                    }
                }
            }

             if (countOverwrites == totalKeysSize) {
                ovwerWriteFound = true;
            }
        }
         return ovwerWriteFound;
    }
    
    // Method added by Anil to get Merged euid.
    public String getMergedEuid(String euid)  {
        String mergedEuid = null;
        try {

            EnterpriseObject enterpriseObject = null;

            enterpriseObject = masterControllerService.getEnterpriseObject(euid);

            if (enterpriseObject != null) {
                mergedEuid = null;
            } else {
                MergeHistoryNode mhn = masterControllerService.getMergeHistory(euid);
                //if the merge history is found return the merged EUID else return "invalid euid" String
                if(mhn != null ) { 
                    mergedEuid = mhn.getEUID();
                } else { 
                    return "Invalid EUID";
                }
            }

            
        }catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("CPD020: Service Layer Validation Exception has occurred"), ex);
                    } else if (ex instanceof UserException) {
                        mLogger.error(mLocalizer.x("CPD021: Service Layer User Exception occurred"), ex);
                    } else if (!(ex instanceof ProcessingException)) {
                        mLogger.error(mLocalizer.x("CPD080: Error  occurred"), ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                     return "Exception_Occured";
           }
        return mergedEuid;
    }

    public String  getEnterpriseObjectStatusForSO(SystemObject so) {
        String eoStatus = null;
        EnterpriseObject enterpriseObject = null;
        try {
            SystemObjectPK systemObjectPK = new SystemObjectPK();
            systemObjectPK.lID = so.getLID();
            systemObjectPK.systemCode = so.getSystemCode();
            enterpriseObject = QwsController.getMasterController().getEnterpriseObject(systemObjectPK);
            eoStatus = enterpriseObject.getStatus();
        } catch (ProcessingException ex) {
            mLogger.severe(mLocalizer.x("CPD022: Could not retrieve an EnterpriseObject Status for SystemObject : {0}", ex.getMessage()));
        } catch (UserException ex) {
            mLogger.severe(mLocalizer.x("CPD023: Could not retrieve an EnterpriseObject: Status for SystemObject : {0}", ex.getMessage()));
        } 
        return eoStatus;

    } 
   /** 
     * Addded  By Rajani Kanth  on 05/08/2008 <br>
     * 
     * This method is used to compare the minor objects hashmap with the arraylist of hashmap <br>
     * 
     * 
     * @param minorObjectList 
     * @param checkMinorObjectMap 
     * @return HashMap  - null if minor object doesnot exists in the arraylist of hashmap <br>
     *                  - HashMap with (K,true/false) <br>
     *                         <b>true</b> - if the values are different or if hashmap is new <br>
     *                         <b>false</b> - if the values are same <br>
     * 
     **/

    public HashMap getDifferenceMinorObjectMap(ArrayList minorObjectList, HashMap checkMinorObjectMap) {
        HashMap returnHashMap = new HashMap();
        Object[] keyset = checkMinorObjectMap.keySet().toArray();
        SourceHandler sourceHandler = new SourceHandler();
        HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
        FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE));
        ArrayList keyTypeList = new ArrayList();
        for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
            if (fieldConfigMap.isKeyType()) {
                keyTypeList.add(fieldConfigMap.getFullFieldName());
            }
        }

        // if no minor object found in the previous transaction to compare
        if (minorObjectList.size() == 0) {
            for (int j = 0; j < checkMinorObjectMap.size(); j++) {
                if (!keyset[j].toString().equals(MasterControllerService.MINOR_OBJECT_ID) &&
                        !keyset[j].toString().equals(MasterControllerService.MINOR_OBJECT_TYPE) &&
                        !keyset[j].toString().equals(MasterControllerService.HASH_MAP_TYPE)) {
                    returnHashMap.put(keyset[j], "true");
                }
            }
            return returnHashMap;
        }

        
        HashMap matchHashMap = new HashMap();
        if (keyTypeList.size() == 1) { //for keyed minor objects
             matchHashMap = getMatchHashMap(minorObjectList, checkMinorObjectMap);
        }

 
        //If the matching map is not empty 
        if (!matchHashMap.isEmpty()) {
            if (checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_ID).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_ID).toString()) && checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString())) {
                for (int i = 0; i < matchHashMap.size(); i++) {
                    if (!keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_ID) &&
                            !keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_TYPE) &&
                            !keyset[i].toString().equals(MasterControllerService.HASH_MAP_TYPE)) {
                        if (matchHashMap.get(keyset[i]) == null && checkMinorObjectMap.get(keyset[i]) == null) {
                            returnHashMap.put(keyset[i], new Boolean(false));
                        } else if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) == null) {
                            returnHashMap.put(keyset[i], new Boolean(true));
                        } else if (matchHashMap.get(keyset[i]) == null && checkMinorObjectMap.get(keyset[i]) != null) {
                            returnHashMap.put(keyset[i], new Boolean(true));
                        } else if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) != null && !matchHashMap.get(keyset[i]).toString().equalsIgnoreCase(checkMinorObjectMap.get(keyset[i]).toString())) {
                            returnHashMap.put(keyset[i], new Boolean(true));
                        } else {
                            returnHashMap.put(keyset[i], new Boolean(false));
                        }
                    }
                }
            }
         }
        return returnHashMap;
    }

    /** 
     * Added  By Narayan Bhat  on 28/08/2008 <br>
     * Modified By Narayan Bhat  on 18/09/2008 <br>
     * 
     * This method is used to compare the minor objects hashmap with the arraylist of hashmap <br>
     * 
     * 
     * @param minorObjectList 
     * @param checkMinorObjectMap 
     * @return HashMap  - null if minor object doesnot exists in the arraylist of hashmap <br>
     *                  - HashMap with (K,true/false) <br>
     *                         <b>true</b> - if the values are different or if hashmap is new <br>
     *                         <b>false</b> - if the values are same <br>
     * 
     **/
 public HashMap getDifferenceMinorObjectMapWithKeyType(ArrayList minorObjectList, HashMap checkMinorObjectMap) {
        HashMap returnHashMap = new HashMap();
        Object[] keyset = checkMinorObjectMap.keySet().toArray();
        SourceHandler sourceHandler = new SourceHandler();
        HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
        FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE));
        ArrayList keyTypeList = new ArrayList();
        for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
            if (fieldConfigMap.isKeyType()) {
                keyTypeList.add(fieldConfigMap.getFullFieldName());
            }
        }
        HashMap matchHashMap = new HashMap();
        if (keyTypeList.size() == 1) {
            matchHashMap = getMatchHashMap(minorObjectList, checkMinorObjectMap);
        }
        //If the matching map is not empty 
        if (!matchHashMap.isEmpty()) {
            //if (checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_ID).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_ID).toString()) && checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString().equals(matchHashMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString())) {
            for (int i = 0; i < matchHashMap.size(); i++) {
                if (!keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_ID) &&
                        !keyset[i].toString().equals(MasterControllerService.MINOR_OBJECT_TYPE) &&
                        !keyset[i].toString().equals(MasterControllerService.HASH_MAP_TYPE)) {
                    if (matchHashMap.get(keyset[i]) == null && checkMinorObjectMap.get(keyset[i]) == null) {
                        returnHashMap.put(keyset[i], new Boolean(false));
                    } else if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) == null) {
                        returnHashMap.put(keyset[i], new Boolean(true));
                    } else if (matchHashMap.get(keyset[i]) == null && checkMinorObjectMap.get(keyset[i]) != null) {
                        returnHashMap.put(keyset[i], new Boolean(true));
                    } else if (matchHashMap.get(keyset[i]) != null && checkMinorObjectMap.get(keyset[i]) != null && !matchHashMap.get(keyset[i]).toString().equalsIgnoreCase(checkMinorObjectMap.get(keyset[i]).toString())) {
                        returnHashMap.put(keyset[i], new Boolean(true));
                    } else {
                        returnHashMap.put(keyset[i], new Boolean(false));
                    }
                }
            }
        // }
        }
        return returnHashMap;
    }
    
    /** 
     * Modified  By Rajani Kanth  on 11/07/2008
     * 
     * This method is used to get the enterprise object as hash map.  The returned hashmap will contain the in information about <br>
       * SBR - root node information as hashmap  <br>
       * SBR - minorobjects as arraylist of hashmaps <br>
       * 
     * 
     * @param enterpriseObject 
     * @param screenObject 
     * @return HashMap 
     * 
     */
        
     public HashMap getEnterpriseObjectAsHashMapFromDB(EnterpriseObject enterpriseObject, ScreenObject screenObject) {
            HashMap enterpriseObjectHashMap = new HashMap();
            SourceHandler sourceHandler = new SourceHandler();
            String rootNodeName = screenObject.getRootObj().getName();
         try {

            //add SystemCode and LID value to the new Hash Map
            HashMap editEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(enterpriseObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));
            HashMap codesEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(enterpriseObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));
            
            FieldConfig[] rootFieldConfigs = screenObject.getRootObj().getFieldConfigs();

            String strVal = new String();

            for (int i = 0; i < rootFieldConfigs.length; i++) {
                 FieldConfig fieldConfig = rootFieldConfigs[i];
                Object value  = editEnterpriseObjectHashMap.get(fieldConfig.getFullFieldName());
                //set the menu list values here
                if(fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) {
                    if(value != null) {
                        //SET THE VALUES WITH USER CODES AND VALUE LIST 
                        if (fieldConfig.getUserCode() != null) {
                            strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                        } else {
                            strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                        }
                                  
                       // strVal= ValidationService.getInstance().getDescription(fieldConfig.getValueList(),value.toString()); 
                        editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), strVal);
                    }
                    
                } else if (fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) {
                     if (value != null) {
                        //Mask the value as per the masking 
                         value = fieldConfig.mask(value.toString());
                         editEnterpriseObjectHashMap.put(fieldConfig.getFullFieldName(), value);
                     }

                }
            
            }
 
            HashMap newLinkedHashMap = new HashMap();
            HashMap eoWithLinkedHashMap = masterControllerService.getLinkedFields(enterpriseObject);
            Object[] keySet = editEnterpriseObjectHashMap.keySet().toArray();
            //BUILD the hash map with links
            for (int i = 0; i < keySet.length; i++) {
                String key = (String) keySet[i];
                if (eoWithLinkedHashMap.get(key) != null) {
                    String[] sysLid = ((String)eoWithLinkedHashMap.get(key)).split(":");
                    if (sysLid.length == 2) {
                        HashMap soHashMapCodes = (HashMap) getSystemObjectAsHashMap(masterControllerService.getSystemObject(sysLid[0], sysLid[1]), screenObject).get("SYSTEM_OBJECT_EDIT");
                        HashMap soHashMap = (HashMap) getSystemObjectAsHashMap(masterControllerService.getSystemObject(sysLid[0], sysLid[1]), screenObject).get("SYSTEM_OBJECT");
                        //put the linked values here for the SBR from the linked system object
                        editEnterpriseObjectHashMap.put(key, soHashMap.get(key));
                        codesEnterpriseObjectHashMap.put(key, soHashMapCodes.get(key));
                    }
                    
                    newLinkedHashMap.put(key, true);
                } else {
                    newLinkedHashMap.put(key, false);
                }
            }

            //add SystemCode and LID value to the new Hash Map  
            editEnterpriseObjectHashMap.put("EUID", enterpriseObject.getEUID()); // set EUID here
             
             
            
            editEnterpriseObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT", editEnterpriseObjectHashMap); // Set the edit EnterpriseObject here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_CODES", codesEnterpriseObjectHashMap); // Set the edit EnterpriseObject here

 
            enterpriseObjectHashMap.put("EO_STATUS", enterpriseObject.getStatus()); // Set the edit EnterpriseObject here
  
            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //Build and array of minor object values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];
                FieldConfig[] minorFiledConfigs = objectNodeConfig.getFieldConfigs(); 

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                ArrayList soMinorObjectsMapArrayListCodes = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMapCodes = (HashMap) soMinorObjectsMapArrayListCodes.get(k);
                    minorObjectHashMapCodes.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                }
                enterpriseObjectHashMap.put("EOCODES" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListCodes); // set SO addresses as arraylist here
                
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE

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

                                //strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
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
                }

                enterpriseObjectHashMap.put("EOCODES" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayListCodes); // set SO addresses as arraylist here
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayListSize", new Integer(soMinorObjectsMapArrayList.size())); // set SO addresses as arraylist here
            }

            ArrayList newMinorObjectsLinkedList = new ArrayList();

            //Build and array of minor objects with links  values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);

                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    HashMap minorObjectsLinkedHashMap = masterControllerService.getLinkValuesForChildren(enterpriseObject, objectNodeConfig.getName(), (String) minorObjectHashMap.get(MasterControllerService.MINOR_OBJECT_ID));

                    Object[] keySetMinor = minorObjectHashMap.keySet().toArray();
                    HashMap newMinorLinkedHashMap = new HashMap();
                    newMinorLinkedHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    for (int l = 0; l < keySetMinor.length; l++) {
                        String key = (String) keySetMinor[l];
                        if (minorObjectsLinkedHashMap.get(key) != null) {
                            newMinorLinkedHashMap.put(key, true);
                        } else {
                            newMinorLinkedHashMap.put(key, false);
                        }
                    }
                    newMinorObjectsLinkedList.add(newMinorLinkedHashMap);
                }
                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayListLink", newMinorObjectsLinkedList); // set SO addresses as arraylist here
            }


            return enterpriseObjectHashMap;
          } catch (Exception ex) {
                    if (ex instanceof ValidationException) {
                        mLogger.error(mLocalizer.x("CPD010: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    } else if (ex instanceof UserException) {
                         mLogger.error(mLocalizer.x("CPD011: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    } else if (!(ex instanceof ProcessingException)) {
                       mLogger.error(mLocalizer.x("CPD012: Failed to get EnterpriseObject  :{0}", ex.getMessage()),ex);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
               }
                   


        return enterpriseObjectHashMap;
    }
 
    /** 
     * Added  By Rajani Kanth  on 18/09/2008
     * 
     * This method is used to get the map (minorObjectMap from minorObjectList) to be compared with checkMinorObjectMap.
     * 
     * 
     * @param minorObjectList 
     * @param checkMinorObjectMap 
     * @return HashMap 
     * 
     */
    HashMap getMatchHashMap(ArrayList minorObjectList, HashMap checkMinorObjectMap) {
        ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
        ObjectNodeConfig rootNodeConfig = screenObject.getRootObj();
        ObjectNodeConfig childNodeConfig = rootNodeConfig.getConfigForNode(checkMinorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE).toString());
        FieldConfig[] keyFieldConfigs = childNodeConfig.getKeyFieldConfigs();
        for (Object minorObjectListObject : minorObjectList) {
            HashMap minorObjectMap = (HashMap) minorObjectListObject;
            int countKeys = 0;
            for (int i = 0; i < keyFieldConfigs.length; i++) {
                String keyTypeValue = keyFieldConfigs[i].getFullFieldName();
                if (checkMinorObjectMap.get(keyTypeValue) != null &&
                        minorObjectMap.get(keyTypeValue) != null &&
                        checkMinorObjectMap.get(keyTypeValue).toString().equalsIgnoreCase(minorObjectMap.get(keyTypeValue).toString())) {
                    countKeys++;
                }
            }
            //check if the key types are more then 1
            if (countKeys == keyFieldConfigs.length) {
                return minorObjectMap;
            }
        }
        return new HashMap();
    }
    
}
