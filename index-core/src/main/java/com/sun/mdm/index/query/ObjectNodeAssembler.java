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
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.util.Localizer;



/**
 * The <b>ObjectNodeAssembler</b> class creates an object node (class
 * ObjectNode) hierarchy. This is a generic object node assembler and can
 * be used to create any kind of ObjectNode structure.
 * @include
 * @author Daniel Cidon
 */
public class ObjectNodeAssembler implements ResultObjectAssembler {
    
    private transient final Localizer mLocalizer = Localizer.get();
    
    /**
     * Creates a new instance of the ObjectNodeAssembler class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public ObjectNodeAssembler() {
    }


  /**
     * Creates an object containing the attributes of the object nodes in the
     * hierarchy (other than the root object). The assembler engine calls this
     * method for each object that will be created and that has a new identity.
     * This method may create a new object, and sets the attribute values for
     * the new object to those in the input AttributesData object.
     * createObjectAttributes also binds the new attributes object to the input
     * parent object. For example, if Parent.FirstName and Parent.Address.City
     * are retrieved in a query, createObjectAttributes is called for each Address
     * object retrieved. The parent object is the Person object, to which the new
     * attributes object is bound.
     * <p>
     * @param rootObject The root object created previously by the
     * <b>createRoot</b> method.
     * @param parent The parent object for the new object to be created (this
     * may be the same as the root object).
     * @param objectName The name of the object to be created, as defined in the
     * Object Definition configuration file of the eView Project.
     * @param attrsData The attribute data associated with the object.
     * @return <CODE>Object</CODE> - An object containing the attributes.
     * @exception VOAException Thrown if an error occurs while creating
     * the object attributes.
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
                Object value = attrsData.get(i);
                int type = objectNode.getField(attributeNames[i]).getType();
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
                }
                objectNode.setValue(attributeNames[i], value);
            }

            ObjectNode parentNode = (ObjectNode)parent;
            parentNode.addChild(objectNode);
        } catch (Exception e) {
            throw new VOAException(mLocalizer.t("QUE515: Could not create Object attributes:{0}", e));
        }

        return objectNode;
    }


   /**
     * Creates the root object for each object. The assembler engine calls this
     * method for each root object to be created. The root object is an
     * ObjectNode object. Inside createRoot(), you can create initialization
	 * data structures that can later be used by the createObjectAttributes method.
     * <p>
     * @param objectName The name of the root object to be created.
     * @param attrsData The attribute data associated with the root object.
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
            // Class objectNodeClass = Class.forName("com.sun.mdm.index.objects." +
            //        objectName + "Object");
            //objectNode = (ObjectNode) objectNodeClass.newInstance();
            objectNode = SimpleFactory.create(objectName);

            String[] attributeNames = attrsData.getAttributeNames();

            for (int i = 0; i < attributeNames.length; i++) {
                /*
                      create subnode to store EUID
                  */
                if (attributeNames[i].equals("EUID")) {
                    ArrayList names = new ArrayList();
                    names.add("EUID");

                    ArrayList values = new ArrayList();
                    values.add("0");

                    ArrayList types = new ArrayList();
                    types.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));

                    ObjectNode euidNode = new ObjectNode("EUID", names, types,
                            values);
                    euidNode.setValue(attributeNames[i], attrsData.get(i));

                    objectNode.addChild(euidNode);
                } else {
                    Object value = attrsData.get(i);
                    int type = objectNode.getField(attributeNames[i]).getType();
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
                    } else if (type == ObjectField.OBJECTMETA_CHAR_TYPE) {
                        // If the return type is Character, convert String value to Character
                        if (value instanceof String) {
                            value = new Character(((String)value).charAt(0));
                        }
                    } else if (type == ObjectField.OBJECTMETA_BOOL_TYPE) {
                        // If the return type is Character, convert String value to Character
                        if (value instanceof java.math.BigDecimal) {
                            value = new Boolean(((java.math.BigDecimal)value).intValue()==1?true:false);
                        }
                    }
                    objectNode.setValue(attributeNames[i], value);
                }
            }
        } catch (Exception e) {
            throw new VOAException(mLocalizer.t("QUE516: Could not create root:{0}", e));
        }

        return objectNode;
    }


    /**
     * Initializes the resources to be used by ObjectNodeAssembler. Call this
     * method only once before starting the assembly process.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Returns:</B><DD> <CODE>void</CODE> - None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public void init() {
    }


    private String stripPath(String objectName) {
        int lastIndex = objectName.lastIndexOf('.');
        String objName = objectName.substring(lastIndex + 1);

        return objName;
    }
}
