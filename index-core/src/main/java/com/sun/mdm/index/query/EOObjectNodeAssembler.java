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
package com.sun.mdm.index.query;

import java.util.ArrayList;
import java.util.Date;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import java.util.HashMap;



/**
 * The <b>EOObjectNodeAssembler</b> class creates an object node (class
 * ObjectNode) hierarchy for EO and their sub-objects. This class is different than 
 * a regular ObjectNodeAssembler because EO, SO and SBR classes require specical handling
 * different than regular ObjectNodes. 
 * Client: EORetriever
 * @include
 * @see EORetriever
 * @author Swaranjit Dua
 */
public class EOObjectNodeAssembler implements ResultObjectAssembler {
    
    // Attributes map containing SBR and SO field names that differ from
    // corresponding field names in the database tables, such as 
    // CreateDate, UpdateDate, and LID.
    private static HashMap mAttributesMap = new HashMap(3);
	
    /**
     * Creates a new instance of the ResultObjectAssembler class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EOObjectNodeAssembler() {
    }

    static {
    	loadMap();
    }

  /**
     * Creates the attributes of the object nodes in the hierarchy. The assembler
     * engine calls this method for each Object that will be created and that has
     * a new identity.
     * <p>
     * @param rootObject The root object created previously by the
     * <b>createRoot</b> method.
     * @param parent The parent object for the new object to be created.
     * @param objectName The name of the object to be created.
     * @param attrsData The attribute values.
     * @return <CODE>Object</CODE> - A new ObjectNode object.
     * @exception VOAException Thrown if an error occurs while creating
     * the attribute data.
     * @include
     */

    public Object createObjectAttributes(Object rootObject, Object parent,
            String objectName, AttributesData attrsData)
        throws VOAException {
        ObjectNode objectNode = null;
        objectName = stripPath(objectName);

        try {
            //  Class objectNodeClass = Class.forName("com.sun.mdm.index.objects." +
            //    objectName + "Object");
            // objectNode = (ObjectNode) objectNodeClass.newInstance();
            objectNode = SimpleFactory.create(objectName);

            String[] attributeNames = attrsData.getAttributeNames();

            for (int i = 0; i < attributeNames.length; i++) {
            	String attributeName = mapAttributeName(objectName, attributeNames[i]);
            	if (objectName.equals("SystemSBR") && attributeName.equals("EUID")) {
            		continue; /* EUID is not a field in SBR, and so don't set EUID to SBR */
            	}
                Object value = attrsData.get(i);
                int type = objectNode.getField(attributeName).getType();
                if (type == ObjectField.OBJECTMETA_INT_TYPE) {
                    // If the return type is BigDecimal, convert to Integer
                    if (value instanceof java.math.BigDecimal) {
                        value = new Integer(((java.math.BigDecimal)value).intValue());
                    }
                } else if (type == ObjectField.OBJECTMETA_FLOAT_TYPE) {
                    // If the return type is BigDecimal, convert to Float
                    if (value instanceof java.math.BigDecimal) {
                        value = new Float(((java.math.BigDecimal)value).floatValue());
                    }
                } else if (type == ObjectField.OBJECTMETA_LONG_TYPE) {
                    // If the return type is BigDecimal, convert to Long
                    if (value instanceof java.math.BigDecimal) {
                        value = new Long(((java.math.BigDecimal)value).longValue());
                    }
                } else if (type == ObjectField.OBJECTMETA_DATE_TYPE) {
                	if (value instanceof  java.sql.Timestamp) {
                		value = new Date(((java.sql.Timestamp)value).getTime());
                	}
                }
                objectNode.setValue(attributeName, value);
            }
            objectNode.resetAll();

            ObjectNode parentNode = (ObjectNode)parent;
            if (parentNode instanceof SBR) {
            	((SBR)parentNode).setObject(objectNode);
            } else if (parentNode instanceof SystemObject) {
            	((SystemObject)parentNode).setObject(objectNode);
              
            } else {
            	parentNode.addChild(objectNode);
            }
        } catch (Exception e) {
            throw new VOAException(e);
        }

        return objectNode;
    }


   /**
     * Creates each root object. The assembler engine calls this method for each
     * root object to be created.The root object is the ObjectNode object.
     * <p>
     * @param objectName The name of the root object to be created.
     * @param attrsData The attribute values.
     * @return <CODE>Object</CODE> - A new root object (class ObjectNode).
     * @exception VOAException Thrown if an error occurs while creating
     * the root object.
     * @include
     */
    public Object createRoot(String objectName, AttributesData attrsData)
        throws VOAException {
        ObjectNode objectNode = null;
        objectName = stripPath(objectName);

        try {
            
            objectNode = SimpleFactory.create(objectName);

            String[] attributeNames = attrsData.getAttributeNames();

            for (int i = 0; i < attributeNames.length; i++) {
            	String attributeName = mapAttributeName(objectName, attributeNames[i]);
                /*
                      create subnode to store EUID
                  */
            	    if ( (objectName.equals("Enterprise")) && !attributeName.equals("EUID")) {
            	    	/*
            	    	 * Enterpise class only has one field EUID even though in enterprise table 
            	    	 * has other fields Localid and SystemCode that we don't set in Enterprise class.
            	    	 */
            	    	continue;
            	    }
            	    if ( (objectName.equals("SystemSBR")) && attributeName.equals("EUID")) {
            	    	/*
            	    	 * SystemSBR class does not has field EUID even though table systemsbr has field EUID.
            	    	 * So we don't assign field EUID to SystemSBR.
            	    	 */
            	    	continue;
            	    }
              
                    Object value = attrsData.get(i);
                    int type = objectNode.getField(attributeName).getType();
                    if (type == ObjectField.OBJECTMETA_INT_TYPE) {
                        // If the return type is BigDecimal, convert to Integer
                        if (value instanceof java.math.BigDecimal) {
                            value = new Integer(((java.math.BigDecimal)value).intValue());
                        }
                    } else if (type == ObjectField.OBJECTMETA_FLOAT_TYPE) {
                        // If the return type is BigDecimal, convert to Float
                        if (value instanceof java.math.BigDecimal) {
                            value = new Float(((java.math.BigDecimal)value).floatValue());
                        }
                    } else if (type == ObjectField.OBJECTMETA_LONG_TYPE) {
                        // If the return type is BigDecimal, convert to Long
                        if (value instanceof java.math.BigDecimal) {
                            value = new Long(((java.math.BigDecimal)value).longValue());
                        }
                    } else if (type == ObjectField.OBJECTMETA_DATE_TYPE) {
                    	if (value instanceof  java.sql.Timestamp) {
                    		value = new Date(((java.sql.Timestamp)value).getTime());
                    	}
                    }
                    objectNode.setValue(attributeName, value);
            }
        } catch (Exception e) {
            throw new VOAException(e);
        }
        objectNode.resetAll();

        return objectNode;
    }


    /**
     * @todo Document this method
     */
    public void init() {
    }


    /*
     * Retrieves an object name from a path.
     * @param objectName object path
     * @return object name
     */
    private String stripPath(String objectName) {
        int lastIndex = objectName.lastIndexOf('.');
        String objName = objectName.substring(lastIndex + 1);

        return objName;
    }
    
    /*
     * Some field names in SBR and SystemObject class are different than correpsonding 
     * field names in database tables.  Like CreateDate, UpdateDate, LID.
     * So we return the corresponding class field names for a database field name.
     * @param object object name like SystemObject, SystemSBR
     * @param attr field name corresponding to database 
     * @return field name corresponding to class.
     */
    private String mapAttributeName(String object, String attr) {
    	String ret = null;
    	
    	if (object.equals("SystemObject") || object.equals("SystemSBR")) {
    		ret = (String)mAttributesMap.get(attr);
    	}  else if (attr.equals("LID")) {
    		ret = "LocalID";
    	}
    	
    	if (ret == null) {
    		ret = attr;
    	}
    	return ret;
    }
    
    /*
     * Load initial values into mAttributesMap
     * 
     */
    private static void loadMap() {
    	mAttributesMap.put("CreateDate", "CreateDateTime");
    	mAttributesMap.put("UpdateDate", "UpdateDateTime");
    	mAttributesMap.put("LID", "LocalID");
    }
}
