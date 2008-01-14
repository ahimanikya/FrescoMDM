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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.dataobject.*;
import com.sun.mdm.index.objects.exception.InvalidFieldValueException;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * This class is a two way pin for DataObject
 * It provides conversion to and from ObjectNode
 * @author Srinivasan Rengarajan
 */
public class DataObjectAdapter {
    
    public static String configFilePath;
    private static ObjectDefinition objDef;
    private static HashMap<String,ObjectDefinition> objDefMap;
    private static SimpleDateFormat dateFormat_ = null;

   
    
  
    public static void init(String eViewConfigFilePath) {
        DataObjectAdapter.configFilePath = eViewConfigFilePath;
        objDef = createObjectDefinition(DataObjectAdapter.configFilePath);
        List<ObjectDefinition> childObjDefs = objDef.getChildren();
        Iterator<ObjectDefinition> iter = childObjDefs.iterator();
        if( objDefMap == null ) objDefMap = new HashMap<String,ObjectDefinition>();
        while(iter.hasNext()) {
            ObjectDefinition childObjDef = iter.next();
            objDefMap.put(childObjDef.getName(),childObjDef);
        }
               
    }
    
    public static void init(ObjectDefinition odef) {
        objDef = odef;
        List<ObjectDefinition> childObjDefs = objDef.getChildren();
        Iterator<ObjectDefinition> iter = childObjDefs.iterator();
        if( objDefMap == null ) objDefMap = new HashMap<String,ObjectDefinition>();
        while(iter.hasNext()) {
            ObjectDefinition childObjDef = iter.next();
            objDefMap.put(childObjDef.getName(),childObjDef);
        }
               
    }

    public static void setDateFormat(SimpleDateFormat dateFormat) {
    	dateFormat_ = dateFormat;
    }
    
    private static ObjectDefinition createObjectDefinition(String eviewConfigFilePath) {
        ObjectDefinition objDef = null;
        try {
         FileInputStream fileInputStream = new FileInputStream(eviewConfigFilePath);
         ObjectDefinitionBuilder objDefBuilder = new ObjectDefinitionBuilder();
         objDef = objDefBuilder.parse(fileInputStream);
         fileInputStream.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return objDef;
    }
    
    public static ObjectDefinition getObjectDefinition() {
        return objDef;
    }
    

    
    
    public static ObjectNode toObjectNode(DataObject dataObject) throws ObjectException, InvalidFieldValueException, ParseException  {
        if(dataObject == null || objDef == null ) return null; //TBD notify user of initialization if objDef is null
        
        List fields = objDef.getFields();
      //  String idName = objDef.getName() + "Id";
        //fields.add(0, idName);
        
        ObjectNode objectNode = constructNode(fields, objDef.getName(), dataObject);
        List<ChildType> childTypes = dataObject.getChildTypes();
        List<ObjectDefinition> childObjDefs = objDef.getChildren();
        int i=0;
        for (Iterator iter = childTypes.iterator(); iter.hasNext();) {
            ChildType childType = (ChildType) iter.next();
            ObjectDefinition childObjDef = childObjDefs.get(i++);
            ArrayList<DataObject> children = childType.getChildren();
            for (Iterator childIter = children.iterator(); childIter.hasNext();) {
                DataObject child = (DataObject) childIter.next();
                objectNode.addChildNoFlagSet(constructNode(childObjDef.getFields(), childObjDef.getName(),child));
            }
        }
        return objectNode;
    }

    public static DataObject fromObjectNode(ObjectNode objectNode) throws ObjectException {
        //logger.info("DataBaseDatareader : Using ObjectNode to DataObject mapping method");
        DataObject newDataObject = new DataObject();
        HashMap childTypeMap = new HashMap();
        // Add Fields to the parent
        copyFields(objDef.getFields(), objectNode, newDataObject);

        //Analyze children
        ArrayList allChildren = objectNode.getAllChildrenFromHashMap();
        List<ObjectDefinition> childObjDefs = objDef.getChildren();
        
        
        if (allChildren != null) {
            for (int i = 0; i < allChildren.size(); i++) {
                ObjectNode childnode = (ObjectNode) allChildren.get(i);
                ObjectDefinition childObjDef = objDefMap.get(childnode.pGetTag());
                if (childTypeMap.containsKey(childnode.pGetTag())) {
                    ChildType childtype = (ChildType) childTypeMap.get(childnode.pGetTag());
                   
                    // Add New Data Object for the child instance
                    childtype.addChild(copyFields(childObjDef.getFields(), childnode, new DataObject()));
                } else {
                    // ChildType is not available in the parent, create it
                    ChildType childtype = new ChildType();
                    childtype.addChild(copyFields(childObjDef.getFields(), childnode, new DataObject()));
                    childTypeMap.put(childnode.pGetTag(), childtype); // Add it to the map
                }
            }
            
           ArrayList<ObjectDefinition> children = objDef.getChildren();
           
           for (ObjectDefinition child: children) {
        	   String name = child.getName();
        	   ChildType childType = (ChildType)childTypeMap.get(name);
        	   if (childType == null) {
        		   childType = new ChildType();
        	   }
        	   newDataObject.addChildType(childType);        	   
           }
           /*

            // Add All Children to the parant data object
            Iterator i = childTypeMap.keySet().iterator();
            while (i.hasNext()) {
                newDataObject.addChildType((ChildType) childTypeMap.get(i.next()));
            }
            */

            // Append to the ret list
            //Clean up map for reuse
            childTypeMap.clear();
        }

        return newDataObject;
    }

    /**
     * Copy Field Values from the Object Node to the Data Node
     * @param objectnode
     * @param dataobject
     */
    private static DataObject copyFields(Collection<Field> fields, ObjectNode objectnode, DataObject dataobject) throws ObjectException {
        if( fields == null || objectnode == null || dataobject == null ) {
            return null;
        }
        Iterator iter = fields.iterator();
        while(iter.hasNext()) {
            Field f = (Field) iter.next();
            String name = f.getName();
            Object value = objectnode.getValue(name);
            dataobject.addFieldValue(valueAsString(value, getTypeAsInt(f.getType())));
        }
        return dataobject;
    }
    
     
     private static final String[] typeStrings = new String[] { 
        ObjectField.OBJECTMETA_UNDEFINED_STRING,
        ObjectField.OBJECTMETA_INT_STRING, 
        ObjectField.OBJECTMETA_BOOL_STRING,
        ObjectField.OBJECTMETA_STRING_STRING, 
        ObjectField.OBJECTMETA_BYTE_STRING,
        ObjectField.OBJECTMETA_LONG_STRING, 
        ObjectField.OBJECTMETA_BLOB_STRING,
        ObjectField.OBJECTMETA_DATE_STRING, 
        ObjectField.OBJECTMETA_FLOAT_STRING,        
        ObjectField.OBJECTMETA_TIMESTAMP_STRING,
        ObjectField.OBJECTMETA_CHAR_STRING 
     };
    
    
     private static int getTypeAsInt(String fieldType) {
         int type = -1;
         ArrayList types = new ArrayList();
         if( fieldType == null || "".equalsIgnoreCase(fieldType) ) {
             type = ObjectField.OBJECTMETA_UNDEFINED_TYPE;
         } else {
             for(int i=0; i< typeStrings.length;i++) {
                if( typeStrings[i].equalsIgnoreCase(fieldType)) {
                    type = i-1;
                    break;
                }
             }
         }
         return type;
     }
    
    
    public static void main(String[] args) {
    }
    
    
    public static String valueAsString(Object value, int type) {
        String str = null;
        if( value == null  ) {
            return null;
        }
        switch(type) {
            case ObjectField.OBJECTMETA_STRING_TYPE:
            case ObjectField.OBJECTMETA_BLOB_TYPE:
            case ObjectField.OBJECTMETA_BOOL_TYPE:
            case ObjectField.OBJECTMETA_BYTE_TYPE:
            case ObjectField.OBJECTMETA_CHAR_TYPE:
            case ObjectField.OBJECTMETA_LONG_TYPE:
            case ObjectField.OBJECTMETA_LINK_TYPE:
            case ObjectField.OBJECTMETA_FLOAT_TYPE:
            case ObjectField.OBJECTMETA_INT_TYPE:
                    str = value.toString();
                    break;
            case ObjectField.OBJECTMETA_DATE_TYPE:
            	   if (dateFormat_ != null) {
            	     str = dateFormat_.format((java.util.Date)value);
            	   } else {
                      str = DateFormat.getDateInstance().format((java.util.Date)value);
            	   }
                    break;
            case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
                    str = DateFormat.getDateTimeInstance().format((java.util.Date)value);
                    break;
            default:
                    break;
        }
        return str;
    }
    
    public static Object parseFieldValue(String value, int type) throws InvalidFieldValueException, ParseException {
        Object valueObj = null;
        try {
            if (value == null || type == ObjectField.OBJECTMETA_UNDEFINED_TYPE ||
                    type == ObjectField.OBJECTMETA_STRING_TYPE || type == ObjectField.OBJECTMETA_BLOB_TYPE) {
                return value;
            } else if (type == ObjectField.OBJECTMETA_INT_TYPE) {
                valueObj = Integer.parseInt(value);
            } else if (type == ObjectField.OBJECTMETA_BOOL_TYPE) {
                valueObj = Boolean.parseBoolean(value);
            } else if (type == ObjectField.OBJECTMETA_FLOAT_TYPE) {
                valueObj = Float.parseFloat(value);
            } else if (type == ObjectField.OBJECTMETA_LONG_TYPE) {
                valueObj = Long.parseLong(value);
            } else if (type == ObjectField.OBJECTMETA_BYTE_TYPE) {
                valueObj = Byte.parseByte(value);
            } else if (type == ObjectField.OBJECTMETA_CHAR_TYPE) {
                if (value.length() > 1) {
                    throw new Exception();
                }
                valueObj = new Character(value.charAt(0));
            } else if (type == ObjectField.OBJECTMETA_DATE_TYPE) {
            	if (dateFormat_ == null) {
                 valueObj = DateFormat.getDateInstance().parse(value);
            	} else {
            	 valueObj = dateFormat_.parse(value);
            	}
            } else if (type == ObjectField.OBJECTMETA_TIMESTAMP_TYPE) {
                valueObj = DateFormat.getDateTimeInstance().parse(value);
            }
        } catch (ParseException e) {
            throw e;
        } catch (Throwable e) {
            String classname = (value.getClass()).getName();
            throw new InvalidFieldValueException();
        }
        return valueObj;
    }

    private static ObjectNode constructNode(Collection<Field> fields, String objectTag, DataObject dataObject) throws ObjectException, InvalidFieldValueException, ParseException {
        ArrayList names = new ArrayList();
        ArrayList types = new ArrayList();
        ArrayList values = new ArrayList();

        Iterator iter = fields.iterator();
        int i = 0;
        while(iter.hasNext()) {
            Field f = (Field) iter.next();
            String name = f.getName();
            String type = f.getType();
            names.add(name);
            Integer fieldType = new Integer(getTypeAsInt(type));
            types.add(fieldType);
            String value = dataObject.getFieldValue(i++);
            if (value!= null && value.equals("")) {
            	value = null;
            }
            values.add(parseFieldValue(value,fieldType.intValue()));
        }
        ObjectNode node = new ObjectNode(objectTag, names, types, values);
        iter = fields.iterator();
        
        while(iter.hasNext()) {
            Field f = (Field) iter.next();
            String name = f.getName();
            node.setKeyType(name, f.isKeyType());
            node.setNullable(name, !f.isRequired());
       
        }
        return node;
    }
}
