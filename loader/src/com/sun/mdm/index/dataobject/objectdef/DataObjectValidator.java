/**
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
package com.sun.mdm.index.dataobject.objectdef;

import com.sun.mdm.index.dataobject.*;
import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathParser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author abhijeet.gupta@sun.com
 */
public class DataObjectValidator {

    private HashMap<String, String> ePathList;
    private Lookup lookup;
    private DataObject parent;
    

    public DataObjectValidator() {
        
    }
    public DataObjectValidator(String objDefFilePath) {
        FileInputStream fileInputStream = null;

        ePathList = new HashMap<String, String>();
        try {

            fileInputStream = new FileInputStream(objDefFilePath);
            ObjectDefinitionBuilder objDefBuilder = new ObjectDefinitionBuilder();
            ObjectDefinition objDef = objDefBuilder.parse(fileInputStream);
            fileInputStream.close();
            lookup = Lookup.createLookup(objDef);

            buildFieldList(objDef, objDef.getName());
            parent = null;

        } catch (FileNotFoundException ex) {
        //ex.printStackTrace();

        } catch (IOException ex) {
        //ex.printStackTrace();
        }

    }

    public DataObjectValidator(ObjectDefinition objDef) {

        ePathList = new HashMap<String, String>();
        lookup = Lookup.createLookup(objDef);
        buildFieldList(objDef, objDef.getName());
        parent = null;
    }

    private void buildFieldList(ObjectDefinition objDef, String strParent) {
        ArrayList<Field> fldArray = objDef.getFields();

        Field tmpField;
        if (fldArray != null) {
            for (int i = 0; i < fldArray.size(); i++) {
                tmpField = (Field) (fldArray.get(i));
                String strKey = "";
                if (tmpField != null) {
                    strKey = strParent + "." + tmpField.getName();
                    ePathList.put(strKey, tmpField.getType());

                }
            }
        }
        ArrayList<ObjectDefinition> objDefArray = objDef.getChildren();
        ObjectDefinition tmpObjDef;
        if (objDefArray != null) {
            for (int i = 0; i < objDefArray.size(); i++) {
                tmpObjDef = (ObjectDefinition) (objDefArray.get(i));
                if (tmpObjDef != null) {
                    String strParentKey = "";
                    strParentKey = strParent + "." + tmpObjDef.getName();
                    buildFieldList(tmpObjDef, strParentKey);

                }
            }
        }

    }

    private List<String> getFieldValueList(String ePath) {//throws DataObjectHandlerException {

        try {
            List<String> list = null;
            EPath ep = EPathParser.parse(ePath);
            list = DOEpath.getFieldValueList(ep, parent, lookup);
            return list;
        } catch (Exception ex) {

        }
        return null;
    }

    /*public  DataObjectValidator getDataObjectValidator(String objDefFilePath) {
        doValidateType = new DataObjectValidator(objDefFilePath);
        return doValidateType;
    }*/

    /*public DataObjectValidator getDataObjectValidator(ObjectDefinition objDef) {
        doValidateType = new DataObjectValidator(objDef);
        return doValidateType;
    }*/

    public void clean() {
        ePathList.clear();
        lookup = null;
        parent = null;

    }

    public ArrayList<String> validateDataType(DataObject dataObj) {
        parent = dataObj;
        Set keys = ePathList.keySet();
        Iterator rlListItr = keys.iterator();
        String typeStr = "", value;
        List valueList;
        ArrayList<String> typeMismatchFlds = new ArrayList<String>();
        /**
         * string; boolean; int; byte; float; date
         * */
        while (rlListItr.hasNext()) {
            String ePath = "";
            try {
                ePath = (String) rlListItr.next();
                value = null;
                if (ePath != null) {
                    typeStr = ePathList.get(ePath);
                    valueList = getFieldValueList(ePath);
                    for (int i = 0; i < valueList.size(); i++) {
                        if (valueList.get(i) != null) {

                            value = (String) valueList.get(i);

                            if (typeStr.trim().compareToIgnoreCase("int") == 0) {
                                try {
                                    Integer.parseInt(value);
                                } catch (NumberFormatException ex) {
                                    typeMismatchFlds.add(ePath);
                                }
                            } else if (typeStr.trim().compareToIgnoreCase("date") == 0) {
                                SimpleDateFormat dateVal = null;
                                try {
                                    dateVal = new SimpleDateFormat(value);
                                } catch (Exception ex) {
                                    dateVal = null;
                                }
                                if (dateVal == null) {
                                    typeMismatchFlds.add(ePath);
                                }
                            } else if (typeStr.trim().compareToIgnoreCase("float") == 0) {

                                try {
                                    Float.parseFloat(value);
                                } catch (NumberFormatException ex) {
                                    typeMismatchFlds.add(ePath);
                                }
                            } else if (typeStr.trim().compareToIgnoreCase("boolean") == 0) {

                                try {
                                    Boolean.parseBoolean(value);
                                } catch (NumberFormatException ex) {
                                    typeMismatchFlds.add(ePath);
                                }

                            } else if (typeStr.trim().compareToIgnoreCase("byte") == 0) { //Char validation

                                if (value == null || value.length() > 1) {
                                    typeMismatchFlds.add(ePath);
                                }

                            }
                        }
                    }

                }
            } catch (Exception ex) {
                typeMismatchFlds.add(ePath);
            }
        }
        return typeMismatchFlds;
    }

    public static void main(String[] args) throws Exception {
        DataObject dataObj = null;
        DataObjectValidator tmpDOValidator = new DataObjectValidator("C:/temp/object.xml");

        for (int i = 0; i < 100; i++) {
            ArrayList<String> arrLst = tmpDOValidator.validateDataType(dataObj);
            if (arrLst == null || arrLst.size() < 1) {
                System.out.println("No Mismatch");
            } else {
                System.out.println("Mismatch !!!");
            }
        }
        tmpDOValidator.clean();
    }
}
