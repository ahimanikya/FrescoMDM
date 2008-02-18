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
import java.util.logging.Logger;
import javax.faces.event.*;

import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class CompareDuplicateManager {
    private MasterControllerService masterControllerService = new MasterControllerService();
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

        public EPathArrayList retrievePatientResultsFields(ArrayList arlResultsConfig) throws Exception {
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
                Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
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
            ex.printStackTrace();
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
                Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, "Unable to get value from Child Object"+field);
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
                Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, "Unable to get value from Child Object"+ePath);
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
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
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
        HashMap systemObjectHashMap = new HashMap();
        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();

        try {
            //add SystemCode and LID value to the new Hash Map
            systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
            systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());
            systemObjectHashMap.put("Status", systemObject.getStatus()); // set Status here

            HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));

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

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                    minorObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                    minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                }
                systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here


            }

        } catch (EPathException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ObjectException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        return systemObjectHashMap;

    }
    
    public HashMap getEnterpriseObjectAsHashMap(EnterpriseObject enterpriseObject, ScreenObject screenObject) {
            HashMap enterpriseObjectHashMap = new HashMap();
            SourceHandler sourceHandler = new SourceHandler();
            String rootNodeName = screenObject.getRootObj().getName();

        try {

            //add SystemCode and LID value to the new Hash Map
            HashMap editEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(enterpriseObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));

            HashMap newLinkedHashMap = new HashMap();

            HashMap eoWithLinkedHashMap = masterControllerService.getLinkedFields(enterpriseObject);
            System.out.println("==> : enterpriseObject.getEUID()" + enterpriseObject.getEUID());

            Object[] keySet = editEnterpriseObjectHashMap.keySet().toArray();
            //BUILD the hash map with links
            for (int i = 0; i < keySet.length; i++) {
                String key = (String) keySet[i];
                if (eoWithLinkedHashMap.get(key) != null) {
                    newLinkedHashMap.put(key, true);
                } else {
                    newLinkedHashMap.put(key, false);
                }
            }

            //add SystemCode and LID value to the new Hash Map  
            editEnterpriseObjectHashMap.put("EUID", enterpriseObject.getEUID()); // set LID here
            editEnterpriseObjectHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT", editEnterpriseObjectHashMap); // Set the edit EnterpriseObject here

            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_HISTORY", getEoHistory(enterpriseObject.getEUID(), screenObject));
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_SOURCES", getEoSources(enterpriseObject, screenObject));

            enterpriseObjectHashMap.put("EO_STATUS", enterpriseObject.getStatus()); // Set the edit EnterpriseObject here
            enterpriseObjectHashMap.put("ENTERPRISE_OBJECT_LINKED", newLinkedHashMap); // Set the edit EnterpriseObject here
            ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

            //Build and array of minor object values from the screen object child object nodes
            for (int i = 0; i < childNodeConfigs.length; i++) {

                //get the child object node configs
                ObjectNodeConfig objectNodeConfig = childNodeConfigs[i];

                //set address array list of hasmap for editing
                ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(enterpriseObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                    HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                    minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                }

                enterpriseObjectHashMap.put("EO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
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
        } catch (ObjectException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectionInvalidException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OPSException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProcessingException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        }

            return enterpriseObjectHashMap;
    }
    
    
    
    public ArrayList getEoHistory(String euid, ScreenObject screenObject) {
        EnterpriseObject eoHist = null;
        ArrayList newArrayListHistory = new ArrayList();
        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();

        try {

            ArrayList viewHistoryEOList = masterControllerService.viewHistory(euid);
            HashMap histEOMap = new HashMap();
            for (int i = 0; i < viewHistoryEOList.size(); i++) {
                HashMap objectHistMap = (HashMap) viewHistoryEOList.get(i);
                String key = (String) objectHistMap.keySet().toArray()[0];

                //System.out.println(i + "  <==>keysSet " + key + "==> : objectHistMap");
                HashMap objectHistMapUpdated = new HashMap();
                if (objectHistMap.get(key) != null) {
                    eoHist = (EnterpriseObject) objectHistMap.get(key);
                    
                    HashMap editEnterpriseObjectHashMap = masterControllerService.getEnterpriseObjectAsHashMap(eoHist, sourceHandler.buildSystemObjectEpaths(rootNodeName));

                    //add SystemCode and LID value to the new Hash Map  
                    histEOMap.put("EUID", eoHist.getEUID()); // set LID here
                    histEOMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE); //SBR_UPDATE HASH MAP type here

                    histEOMap.put("ENTERPRISE_OBJECT", editEnterpriseObjectHashMap); // Set the edit EnterpriseObject here

                    ObjectNodeConfig[] childNodeConfigs = screenObject.getRootObj().getChildConfigs();

                    //Build and array of minor object values from the screen object child object nodes
                    for (int j = 0; j < childNodeConfigs.length; j++) {

                        //get the child object node configs
                        ObjectNodeConfig objectNodeConfig = childNodeConfigs[j];

                        //set address array list of hasmap for editing
                        ArrayList soMinorObjectsMapArrayList = masterControllerService.getEnterpriseObjectChildrenArrayList(eoHist, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                        for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                            HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                            minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                        }

                        histEOMap.put("EO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
                    }

                    objectHistMapUpdated.put(key, histEOMap);

                    newArrayListHistory.add(objectHistMapUpdated);

                }
            }
        } catch (ProcessingException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newArrayListHistory;
    }

    
    public ArrayList getEoSources(EnterpriseObject enterpriseObject, ScreenObject screenObject) {
        ArrayList newArrayList = new ArrayList();
        Collection itemsSource = enterpriseObject.getSystemObjects();

        Iterator iterSources = itemsSource.iterator();

        HashMap systemObjectHashMap = new HashMap();
        SourceHandler sourceHandler = new SourceHandler();
        String rootNodeName = screenObject.getRootObj().getName();

        try {
            while (iterSources.hasNext()) {

                SystemObject systemObject = (SystemObject) iterSources.next();

                systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());
                systemObjectHashMap.put("Status", systemObject.getStatus()); // set Status here
                HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, sourceHandler.buildSystemObjectEpaths(rootNodeName));

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
                    ArrayList soMinorObjectsMapArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths(objectNodeConfig.getName()), objectNodeConfig.getName(), MasterControllerService.MINOR_OBJECT_UPDATE);
                    for (int k = 0; k < soMinorObjectsMapArrayList.size(); k++) {
                        HashMap minorObjectHashMap = (HashMap) soMinorObjectsMapArrayList.get(k);
                        minorObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); // set LID here
                        minorObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); // set System code here
                        minorObjectHashMap.put(MasterControllerService.MINOR_OBJECT_TYPE, objectNodeConfig.getName()); // set MINOR_OBJECT_TYPE
                    }
                    systemObjectHashMap.put("SO" + objectNodeConfig.getName() + "ArrayList", soMinorObjectsMapArrayList); // set SO addresses as arraylist here
                }
                newArrayList.add(systemObjectHashMap);
            }
        } catch (EPathException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ObjectException ex) {
            Logger.getLogger(CompareDuplicateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newArrayList;
    }

}
