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
package com.sun.mdm.index.master;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import com.sun.mdm.index.objects.ObjectNode;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.matching.MatchingConfiguration;
import com.sun.mdm.index.configurator.impl.matching.SystemObjectMatching;
import com.sun.mdm.index.configurator.impl.matching.MatchColumn;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.objects.ObjectKey;

/**
 * Utility class to determine if a match field has changed
 */
public class MatchFieldChange {
    
    private EPath[] mMatchColumns;
    private ObjectFactory objFactory = null;
    private ObjectKey objectKeys[] = null;
    private boolean checkForObjectKeys = true;
    
    /** Creates a new instance of MatchFieldChange 
     */
    public MatchFieldChange() {
        if (objFactory == null) {
            objFactory = ObjectFactory.getInstance();
        }
    }
    
    private void initMatchColumns(String tag) throws InstantiationException {
        ConfigurationService bc = ConfigurationService.getInstance();
        MatchingConfiguration mcConfig = 
                (MatchingConfiguration)
                ConfigurationService.getInstance().getConfiguration(
                MatchingConfiguration.MATCHING); 
        SystemObjectMatching som = mcConfig.getSystemObjectMatching(tag);
        List matchColumns = som.getMatchColums();
        Iterator i = matchColumns.iterator();
        EPathArrayList ePathArrayList = new EPathArrayList();
        while (i.hasNext()) {
            MatchColumn matchColumn = (MatchColumn) i.next();
            ePathArrayList.add(matchColumn.getEPath());
        }
        mMatchColumns = ePathArrayList.toArray();
    }

    //  Handles match columns in child objects
    
    private void initMatchColumns(ObjectNode objNode) throws InstantiationException {
        ConfigurationService bc = ConfigurationService.getInstance();
        MatchingConfiguration mcConfig = 
                (MatchingConfiguration)
                ConfigurationService.getInstance().getConfiguration(
                MatchingConfiguration.MATCHING);
        ArrayList allTags = objNode.pGetChildTags();
            //  add parent tag
        if (allTags != null) {
            allTags.add(objNode.pGetTag());
        } else {
            initMatchColumns(objNode.pGetTag());
            return;
        }
        Iterator tagsIterator = allTags.iterator();
        while(tagsIterator.hasNext())
        {
            String tag = (String) tagsIterator.next();
            SystemObjectMatching som = mcConfig.getSystemObjectMatching(tag);
            if (som != null)  {
                List matchColumns = som.getMatchColums();
                Iterator i = matchColumns.iterator();
                EPathArrayList ePathArrayList = new EPathArrayList();
                while (i.hasNext()) {
                    MatchColumn matchColumn = (MatchColumn) i.next();
                    ePathArrayList.add(matchColumn.getEPath());
                }
                mMatchColumns = ePathArrayList.toArray();
            }
        }
    }
    
    /**
     * Determine if a match field has changed in two enterprise objects
     *
     * @param obj1 match fields from enterprise object 1
     * @param obj2 match fields from enterprise object 2
     * @return true if match field has changed
     * @throws ObjectException object access exception
     * @throws EPathException epath API exception
     * @throws InstantiationException matching config could not be loaded
     */
    public boolean isMatchFieldChanged(Object obj1[], Object obj2[]) 
            throws ObjectException, EPathException, InstantiationException {
        
        for (int i = 0; i < obj1.length; i++) {
            Object val1 = obj1[i];
            Object val2 = obj2[i];
            if (val1 == null) {
                if (val2 == null) {
                    continue;
                } else {
                    return true;
                }
            } else {
                if (val2 == null) {
                    return true;
                } else {
                    boolean val1Flag = val1 instanceof ArrayList;
                    boolean val2Flag = val2 instanceof ArrayList;
                    if (val1Flag && val2Flag) {
                        
                        //  if the sizes are different, then at least one addition
                        //  or deletion was made.
                        if (((ArrayList) val1).size() != ((ArrayList) val2).size()) {
                            return true;
                        }
                        
                        //  otherwise, check each element of the array list
                        int maxSize = ((ArrayList) val1).size();
                        for (int k = 0; k < maxSize; k++) {
                            Object tempObj = ((ArrayList) val1).get(k);
                            String value1 = null;
                            String value2 = null;
                            if (tempObj != null) {
                                value1 = tempObj.toString();
                            }
                            tempObj = ((ArrayList) val2).get(k);
                            if (tempObj != null) {
                                value2 = tempObj.toString();
                            }
                            if (value1 != null && value2 == null) {
                                return true;
                            } else if (value1 == null && value2 != null) {
                                return true;
                            }
                            if (value1 != null && value1.compareTo(value2) != 0) {
                                return true;
                            }
                        }
                    } else if (!val1.equals(val2)) {
                        return true;
                    }
                }
            }
        }
            
        return false;
    }
    
    /**
     * Retrieves the match fields from an Enterprise Object.
     *
     * @param obj  Enterprise Object
     * @return array of values for the match fields
     * @throws ObjectException object access exception
     * @throws EPathException epath API exception
     * @throws InstantiationException instantiation exception
     */
    public Object[] getMatchFields(EnterpriseObject obj) 
            throws ObjectException, EPathException, InstantiationException {
        if (mMatchColumns == null) {
            initMatchColumns(obj.getSBR().getObject());            
        }
        // retrieve object keys just once
        if (checkForObjectKeys) {        
            getObjectKeys(obj);
            checkForObjectKeys = false;
        }
        
        // gather key field data and enter into objArray
        
        Object objArray[] = new Object[2 * mMatchColumns.length];
        int k = 0;
        for (; k < mMatchColumns.length; k++) {
            ObjectKey objKey =  objectKeys[k];
            if (objKey != null) {
                ArrayList keyNames = objKey.getKeyNames();
                Iterator keyNamesIter = keyNames.iterator();
                
                //  Create a new EPath object for each Object Key
                while (keyNamesIter.hasNext()) {
                    String fieldName = keyNamesIter.next().toString();
                    String newEPath = createNewEPath(mMatchColumns[k], fieldName);
                    Object tempObj = getKeyFieldValues(obj, newEPath);
                    objArray[k] = tempObj;
                }
            }            
            
        }        
        for (int i = 0; i < mMatchColumns.length; i++) {
            Object tempObj = getKeyFieldValues(obj, mMatchColumns[i].getName());
            objArray[i + k] = tempObj;
        }
        return objArray;
    }

    /**
     * Retrieves an value from an Enterprise Object given an ePath.
     *
     * @param obj  Enterprise Object
     * @param ePath  String representation of an EPath
     * @return array of values for the match fields
     * @throws ObjectException object access exception
     * @throws EPathException epath API exception
     * @throws InstantiationException instantiation exception
     */
    private Object getKeyFieldValues(EnterpriseObject obj, String ePath) 
            throws ObjectException, EPathException, InstantiationException {
    
        if (ePath == null || obj == null) {
            return null;
        }
        
        Object tempObj = EPathAPI.getFieldValue(ePath, obj);
        //  If tempObj is an ArrayList, then retrieve the values from 
        //  each element of the ArrayList.  Each element may contain
        //  more than one EPath and non-target EPaths.
        if (tempObj instanceof ArrayList)  {
            //  retrieve name of the target match field
            String targetMatchColumnName = null;
            String tempMatchColumnName = ePath;
            int index = tempMatchColumnName.lastIndexOf('.');
            if (index != -1) {
                targetMatchColumnName = tempMatchColumnName.substring(index + 1);
            }

            ArrayList valueArray = new ArrayList();
            Iterator iter = ((ArrayList) tempObj).iterator();
            while (iter.hasNext() && targetMatchColumnName != null){
                //  check for the "]" and ":" to make sure it is an exact match
                String tempStr = iter.next().toString();
                int startIndex = tempStr.indexOf("]" + targetMatchColumnName + ":");
                //  obtain value of the match field
                if (startIndex != -1) {
                    startIndex += targetMatchColumnName.length() + 2;
                    int endIndex = tempStr.indexOf("\n", startIndex);
                    String value = tempStr.substring(startIndex + 1, endIndex);
                    if (value != null) {
                        value = value.trim();
                    }
                    valueArray.add(value);
                }
            }
            return valueArray;
        } else {
            return tempObj;
        }
    }

    /**
     * Creates a fully-qualified name given an EPath and field name.  This 
     * replaces the field name in an existing EPath string with the contents of
     * the fieldName argument.  For example, if ePath is "Person.Address[*].PostalCode"
     * and fieldName is "Country", this will return "Person.Address[*].Country"
     *
     * @param obj  Enterprise Object
     * @param ePath  String representation of an EPath
     * @return string representing the new EPath.
     *
     */
    private String createNewEPath(EPath ePath, String fieldName) {
        String basepath = ePath.getName();
        int endIndex = basepath.lastIndexOf(".") + 1;
        String newPath = basepath.substring(0, endIndex) + fieldName;
        return newPath;
    }
    
    /**
     * Retrieves all object keys for an Enterprise Object.  This is done only
     * once.
     *
     * @param obj  Enterprise Object
     * @return array of values for the match fields
     * @throws ObjectException object access exception
     * @throws EPathException epath API exception
     * @throws InstantiationException instantiation exception
     */
    public void getObjectKeys(EnterpriseObject obj) 
            throws ObjectException, EPathException, InstantiationException {

        if (mMatchColumns == null) {
            initMatchColumns(obj.getSBR().getObject());            
        }

        if (objectKeys == null) {
            objectKeys = new ObjectKey[mMatchColumns.length];
        } 
        for (int i = 0; i < mMatchColumns.length; i++) {
            //  Retrieve object keys if they have not yet been retrieved.
            if (objectKeys[i] == null) {
                String objName = mMatchColumns[i].getLastChildName();
                ObjectNode objNode = objFactory.create(objName);
                ObjectKey objKey = objNode.pGetKey();
                objectKeys[i] = objKey;
            }
        }
    }
}
