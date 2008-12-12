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
package com.sun.mdm.multidomain.services.core;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.multidomain.services.model.ObjectDefinition;
import com.sun.mdm.multidomain.services.model.Field;

/**
 * ObjectNodeFactoryImpl class.
 * @author cye
 */
public class ObjectNodeFactoryImpl implements ObjectFactory {

    private static final String[] FIELD_TYPE_STRINGS = new String[] { 
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
                                    ObjectField.OBJECTMETA_CHAR_STRING};
    
    private SimpleDateFormat dateFormat;
    private ObjectDefinition objectDef;
    private String objectName; 
    private String objectTag;
    private List<Field> fields; 
            
    private boolean initialized = false;

    /**
     * Create instance of ObjectNodeFactoryImpl.
     * @param name Object name.
     */
    public ObjectNodeFactoryImpl(String name){
        objectName = name;
    }   

    /**
     * Retrieve the objectDef value.
     * 
     * @returns  Value of the objectDef attribute.
     */
    public ObjectDefinition getObjectDefinition() {
        return objectDef;
    }

    /**
     * Initailzation.
     */
    private void initialize() {
        if(!initialized) {
            synchronized(ObjectNodeFactoryImpl.class) {
                InputStream stream = ObjectNodeFactoryImpl.class.getResourceAsStream("/domains/" + objectName + "/object.xml");
                ObjectDefinitionBuilder builder = new ObjectDefinitionBuilder();
                objectDef = builder.parse(stream);                
                addObjectId(objectDef);                
                dateFormat = new SimpleDateFormat(objectDef.getDateFormat());
                fields = objectDef.getFields();
                objectTag = objectDef.getName();    
            }
        }
    }
        
    /**
     * Create Object node.
     * @param nodeTag Object node tag.
     * @param fields List of Field.
     * @return ObjectNode.
     * @throws ObjectException Thrown if an error occurs during processing.
     */
    private ObjectNode createObjectNode(String nodeTag, List<Field> fields) 
        throws ObjectException {        
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> types = new ArrayList<Integer>();                
        for(Field field : fields) {
            names.add(field.getName());
            Integer fieldType = new Integer(toFieldTypeInt(field.getType()));
            types.add(fieldType);                       
        }
        ObjectNode node = new ObjectNode(nodeTag, names, types);         
        /* 
        for(Field field : fields) {
            String name = field.getName();
            node.setKeyType(name, field.isKeyType());
            node.setNullable(name, !field.isRequired());             
        }
        */
        return node;
    }
    
    /**
     * Add Object id into object definition.
     * @param objdef Object Definition.
     */
    private void addObjectId(ObjectDefinition objdef) {
        String name = objdef.getName();
        String idName = name + "Id";
        Field field = new Field();
	field.setName(idName);
	field.setType("string");
	objdef.addField(0, field);
        
	List<ObjectDefinition> children = objdef.getChildren();
	for (ObjectDefinition child: children) {
            addObjectId(child);
	}        
    }	
    
    /**
     * Convert field type into int value.
     * @param fieldType Field type.
     * @return int Field type.
     */
    private static int toFieldTypeInt(String fieldType) {    	 
        int type = -1;
        if( fieldType == null || "".equalsIgnoreCase(fieldType) ) {
            type = ObjectField.OBJECTMETA_UNDEFINED_TYPE;
        } else {
            if (fieldType.equalsIgnoreCase("int")) {
                fieldType = ObjectField.OBJECTMETA_INT_STRING;
            }
            for(int i=0; i< FIELD_TYPE_STRINGS.length;i++) {            	 
                if(FIELD_TYPE_STRINGS[i].equalsIgnoreCase(fieldType)) {
                    type = i-1;
                    break;
                } 
             }
        }
        return type;
     }    
     
    /**
     * Create ObjectNode for the given tag.
     * @param objectTag Object tag.
     * @return ObjectNode.
     * @throws ObjectException Thrown if an error occurs during processing.
     */
    public ObjectNode create(String objectTag) 
        throws ObjectException {        
        initialize();                
        ObjectNode rootNode = createObjectNode(objectTag, fields);        
        List<ObjectDefinition> childObjectDefs = objectDef.getChildren();
        for(ObjectDefinition childObjectDef : childObjectDefs) {
            ObjectNode childNode = createObjectNode(childObjectDef.getName(), childObjectDef.getFields());
            rootNode.addChildNoFlagSet(childNode);
        }                
        return rootNode;
    }
     
}
