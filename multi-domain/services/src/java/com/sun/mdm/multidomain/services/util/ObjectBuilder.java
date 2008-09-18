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
package com.sun.mdm.multidomain.services.util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.multidomain.services.core.ServiceException;

/**
 * Object builder class
 * @author cye
 *
 */
public class ObjectBuilder {

	public static SystemObject createSystemObject(String objectName, Map<String, String> searchCriteria) 
		throws ServiceException {		
		SystemObject so = null;
		try {
			ObjectNode topNode = ObjectFactory.create(objectName);
			Iterator<String> keys = searchCriteria.keySet().iterator();        
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) searchCriteria.get(key);
            
				if ((value != null) && (value.trim().length() > 0)) {
					int index = key.indexOf(".");
					if (index > -1) {
						String tmpRef = key.substring(0, index);
						String fieldName = key.substring(index + 1);

						if (tmpRef.equalsIgnoreCase(objectName)) {
							setObjectNodeFieldValue(topNode, fieldName, value);
						} else {
							ArrayList<ObjectNode> childNodes = topNode.pGetChildren(tmpRef);
							ObjectNode node = null;
							if (childNodes == null) {
								node = ObjectFactory.create(tmpRef);
								topNode.addChild(node);
							} else {
								node = (ObjectNode) childNodes.get(0);
							}                        
							setObjectNodeFieldValue(node, fieldName, value);
						}
					}
				}            
			}        
			so = (SystemObject) ObjectFactory.create("SystemObject");
			so.setValue("ChildType", objectName);
			so.setObject(topNode);        
		} catch(ObjectException oe) {
			throw new ServiceException(oe);
		}
        return so;		
	}

	public static ObjectNode createObjectNode(String objectName, Map<String, String> searchCriteria) 
		throws ServiceException {		
		ObjectNode objectNode = null;
		try {
			objectNode = ObjectFactory.create(objectName);
			Iterator<String> keys = searchCriteria.keySet().iterator();        
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) searchCriteria.get(key);
        
				if ((value != null) && (value.trim().length() > 0)) {
					int index = key.indexOf(".");
					if (index > -1) {
						String tmpRef = key.substring(0, index);
						String fieldName = key.substring(index + 1);
						if (tmpRef.equalsIgnoreCase(objectName)) {
							setObjectNodeFieldValue(objectNode, fieldName, value);
						} else {
							ArrayList<ObjectNode> childNodes = objectNode.pGetChildren(tmpRef);
							ObjectNode node = null;
							if (childNodes == null) {
								node = ObjectFactory.create(tmpRef);
								objectNode.addChild(node);
							} else {
								node = (ObjectNode) childNodes.get(0);
							}                        
							setObjectNodeFieldValue(node, fieldName, value);
						}
					}
				}            
			}        
		} catch(ObjectException oe) {
			throw new ServiceException(oe);
		}
    return objectNode;		
}
	
    public static void setObjectNodeFieldValue(ObjectNode node, String field, String value)
    	throws ObjectException {
            if (value == null) {
              if (node.isNullable(field)) {
            	  node.setValue(field, null);
            	  return;
              } else {
            	  //ObjectNodeConfig config = ConfigManager.getInstance().getObjectNodeConfig(node.pGetType());
                  //String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
                  throw new ObjectException("Field [" + field + "] is required");
              }
            }
            int type = node.pGetType(field);
            try {
            	switch (type) {
            	case ObjectField.OBJECTMETA_DATE_TYPE:
            	case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
            		node.setValue(field, (Object) new SimpleDateFormat("mm/dd/yyyy").parse(value));
            		break;
            	case ObjectField.OBJECTMETA_INT_TYPE:
            		node.setValue(field, (Object) Integer.valueOf(value));
            		break;
            	case ObjectField.OBJECTMETA_BOOL_TYPE:
            		node.setValue(field, (Object) Boolean.valueOf(value));
            		break;
            	case ObjectField.OBJECTMETA_BYTE_TYPE:
            		node.setValue(field, (Object) Byte.valueOf(value));
            		break;
            	case ObjectField.OBJECTMETA_CHAR_TYPE:
            		node.setValue(field, (Object) new Character(value.charAt(0)));
            		break;
            	case ObjectField.OBJECTMETA_LONG_TYPE:
            		node.setValue(field, (Object) Long.valueOf(value));
            		break;
            	case ObjectField.OBJECTMETA_FLOAT_TYPE:
            		node.setValue(field, (Object) Float.valueOf(value));
            		break;
            	case ObjectField.OBJECTMETA_STRING_TYPE:
            	default:
            		node.setValue(field, (Object) value);
					break;
            	}
            } catch (ParseException pex) {
            } catch (NumberFormatException nex) {
                //ObjectNodeConfig config = ConfigManager.getInstance().getObjectNodeConfig(node.pGetType());
                //String fieldDisplayName = config.getFieldConfig(field).getDisplayName(); 
                throw new ObjectException("Invalid value [" + value + "] for field [" + field + "]"); 
            }
        }	    
}
