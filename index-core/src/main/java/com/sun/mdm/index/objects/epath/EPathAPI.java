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
package com.sun.mdm.index.objects.epath;

import com.sun.mdm.index.objects.SuperKey;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.factory.SimpleFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/** The methods in this class are convenience methods for using the
 * EPath object.
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class EPathAPI {

    private static final String[][] STRING_FILTER = { {"\\.", "#kdot"}, {",", "#kcomma"} };
    private static final int UNMASKED = 0;
    private static final int MASKED = 1;
    
    public static String maskString(Object str) {
        String ret = null;
        
        if (str != null) {
        	ret = str.toString();
        	for (int i = 0; i < STRING_FILTER.length; i++ ) {
        	    ret = ret.replaceAll(STRING_FILTER[i][UNMASKED], STRING_FILTER[i][MASKED]);
        	}
        }
        return ret;
    }
    
    public static String unmaskString(Object str) {
        String ret = null;
        
        if (str != null) {
        	ret = str.toString();
            for (int i = 0; i < STRING_FILTER.length; i++ ) {
                ret = ret.replaceAll(STRING_FILTER[i][MASKED], STRING_FILTER[i][UNMASKED]);
            }
        }
        
        return ret;
    }

    /** prevent creation    
     */
    protected EPathAPI() {
    }

    /** Use an EPath string to select a single field object.
     * This method is significantly more expensive then passing in a
     * pre-parsed EPath object
     * @return return an object representing the value of the specified field,
     * or a collection if the qualified field name points to a list of child objects
     * @param fqfn EPath string
     * @param context ObjectNode where the traversal begins
     * @throws ObjectException object access exception
     * @throws EPathException EPath exception
     */
    public static Object getFieldValue(String fqfn, ObjectNode context)
        throws ObjectException, EPathException {
        return getFieldValue(EPathParser.parse(fqfn), context);
    }

    /** uses the EPath String to traverse the ObjectNode and set the value
     * specified by the Epath Object.
     *
     * This method is significantly more expensive then passing in a pre-parsed EPath object
     * @param fqfn EPath string
     * @param context ObjectNode where the EPath begins
     * @param value value of the object to set.  If the EPath is points to a
     * collection, designated by [*], then the value must be of the Collection
     * type
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */
    public static void setFieldValue(String fqfn, ObjectNode context,
        Object value) throws ObjectException, EPathException {
        setFieldValue(EPathParser.parse(fqfn), context, value);
    }

    /** uses the EPath String to traverse the ObjectNode and set the value
     * specified by the Epath Object.
     *
     * This method is significantly more expensive then passing in a pre-parsed EPath object
     * @param fqfn EPath string
     * @param context ObjectNode where the EPath begins
     * @param value value of the object to set.  If the EPath is points to a
     * collection, designated by [*], then the value must be of the Collection
     * type
     * @param byReference adds the value as a reference 
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */    
    public static void setFieldValue(String fqfn, ObjectNode context,
        Object value, boolean byReference)
        throws ObjectException, EPathException {
        setFieldValue(EPathParser.parse(fqfn), context, value, byReference);
    }

    /** uses the parsed op commands to traverse the ObjectNode and return the value
     * specified by the EPath object.
     * @return an object representing the value of the specified field, or a
     * collection if the qualified field name points to a list of child objects
     * @param e EPath object, contains parsed op commands and fqfn
     * @param context ObjectNode, the node where the path starts
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */
    public static Object getFieldValue(EPath e, ObjectNode context)
        throws ObjectException, EPathException {
        ObjectNode current = context;

        for (int i = 0; i < e.ops.length; i++) {
            // added here tentatively, TODO need to reconsider
            // based on shortcircut or operator
            if ((current == null) || current.isRemoved()) {
                return null;
            }

            // end add
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);
                if (key == null) {
                    return null;
                }
                for (int j = 0; j < e.filters[i].length; j++) {
                    String field = (e.filters[i][j]).getField();
                    int type = key.getKeyType(field);
                    String stringValue = unmaskString((e.filters[i][j]).getValue());
                    Object value = null;
                    if (stringValue != null) {
                        switch (type) {
                            case ObjectField.OBJECTMETA_STRING_TYPE:
                                value = stringValue;
                                break;
                            case ObjectField.OBJECTMETA_FLOAT_TYPE:
                                value = new Float(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_INT_TYPE:
                                value = new Integer(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_LONG_TYPE:
                                value = new Long(stringValue);
                                break;
                            default:
                                throw new ObjectException("Invalid key type: " + type);
                        }
                    }
                    
                    key.setKeyValue(field, value);
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;

            case EPath.OP_ALL_FIELD:
                return current;

            case EPath.OP_FIELD:
                return current.getValue(e.tokenQueue[i]);

            case EPath.OP_ALL_SECONDARY: {
                // @todo consider filtering out all children that has the remove
                // flag set
                List children = current.pGetChildren(e.tokenQueue[i]);
                if (children != null) {
                    Iterator iter = children.iterator();
                    while (iter.hasNext()) {
                        ObjectNode node = (ObjectNode) iter.next();
                        if (node.isRemoved()) {
                            iter.remove();
                        }
                    }
                }
                return children;
            }
            case EPath.OP_SECONDARY_BY_INDEX:
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }

        return current;
    }
    
    /** uses the parsed op commands to traverse the ObjectNode and return the value
     * specified by the EPath object.
     * @return an object representing the value of the specified field, or a
     * collection if the qualified field name points to a list of child objects
     * @param e EPath object, contains parsed op commands and fqfn
     * @param context ObjectNode, the node where the path starts
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */
    public static Object getFieldValueQB(EPath e, ObjectNode context)
        throws ObjectException, EPathException {
        ObjectNode current = context;

        for (int i = 0; i < e.ops.length; i++) {
            // added here tentatively, TODO need to reconsider
            // based on shortcircut or operator
            if ((current == null) || current.isRemoved()) {
                return null;
            }

            // end add
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);
                if (key == null) {
                    return null;
                }
                for (int j = 0; j < e.filters[i].length; j++) {
                    String field = (e.filters[i][j]).getField();
                    int type = key.getKeyType(field);
                    String stringValue = unmaskString((e.filters[i][j]).getValue());
                    Object value = null;
                    if (stringValue != null) {
                        switch (type) {
                            case ObjectField.OBJECTMETA_STRING_TYPE:
                                value = stringValue;
                                break;
                            case ObjectField.OBJECTMETA_FLOAT_TYPE:
                                value = new Float(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_INT_TYPE:
                                value = new Integer(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_LONG_TYPE:
                                value = new Long(stringValue);
                                break;
                            default:
                                throw new ObjectException("Invalid key type: " + type);
                        }
                    }
                    
                    key.setKeyValue(field, value);
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;

            case EPath.OP_ALL_FIELD:
                return current;

            case EPath.OP_FIELD:
                return current.getValue(e.tokenQueue[i]);

            case EPath.OP_ALL_SECONDARY: {
                // @todo consider filtering out all children that has the remove
                // flag set
                List children = current.pGetChildren(e.tokenQueue[i]);
                if (children != null) {
                    Iterator iter = children.iterator();
                    while (iter.hasNext()) {
                        ObjectNode node = (ObjectNode) iter.next();
                        if (node.isRemoved()) {
                            iter.remove();
                        }
                    }                         
                    ArrayList a = new ArrayList();
                    for (int j=0; j < children.size(); j++)
                        a.add((Object)getFieldValueRec(e, (ObjectNode)children.get(j), i+1));
                    return (Object) a;
                } else {
                    return null; 
                }
            }
            case EPath.OP_SECONDARY_BY_INDEX:
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }

        return current;
    }
    
    public static Object getFieldValueRec(EPath e, ObjectNode context, int pos)
        throws ObjectException, EPathException {
        ObjectNode current = context;

        for (int i = pos; i < e.ops.length; i++) {
            // added here tentatively,TODO need to reconsider
            // based on shortcircut or operator
            if ((current == null) || current.isRemoved()) {
                return null;
            }

            // end add
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);
                if (key == null) {
                    return null;
                }
                for (int j = 0; j < e.filters[i].length; j++) {
                    String field = (e.filters[i][j]).getField();
                    int type = key.getKeyType(field);
                    String stringValue = unmaskString((e.filters[i][j]).getValue());
                    Object value = null;
                    if (stringValue != null) {
                        switch (type) {
                            case ObjectField.OBJECTMETA_STRING_TYPE:
                                value = stringValue;
                                break;
                            case ObjectField.OBJECTMETA_FLOAT_TYPE:
                                value = new Float(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_INT_TYPE:
                                value = new Integer(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_LONG_TYPE:
                                value = new Long(stringValue);
                                break;
                            default:
                                throw new ObjectException("Invalid key type: " + type);
                        }
                    }
                    
                    key.setKeyValue(field, value);
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;

            case EPath.OP_ALL_FIELD:
                return current;

            case EPath.OP_FIELD:
                return current.getValue(e.tokenQueue[i]);

            case EPath.OP_ALL_SECONDARY: {
                // @todo consider filtering out all children that has the remove
                // flag set
                List children = current.pGetChildren(e.tokenQueue[i]);
                if (children != null) {
                    Iterator iter = children.iterator();
                    while (iter.hasNext()) {
                        ObjectNode node = (ObjectNode) iter.next();
                        if (node.isRemoved()) {
                            iter.remove();
                        }
                    }
                
                    ArrayList a = new ArrayList();
                    for (int j=0; j < children.size(); j++)
                        a.add((Object)getFieldValueRec(e, (ObjectNode)children.get(j), i+1));
                    return (Object) a;
                } else {
                    return null;
                }
                
            }
            case EPath.OP_SECONDARY_BY_INDEX:
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }

        return current;
    }

    /** uses the parsed op commands to traverse the ObjectNode and set the value
     * specified by the Epath Object.
     * @param e EPath Object
     * @param context ObjectNode where the EPath begins
     * @param value value of the object to set.  If the EPath is points to a
     * collection, designated by [*], then the value must be of the Collection
     * type.  If the EPath points to a complete object, denoted by child[key].*,
     * then the value must be of ObjectNode type
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */
    public static void setFieldValue(EPath e, ObjectNode context, Object value)
        throws ObjectException, EPathException {
        setFieldValue(e, context, value, true);
    }

    /** uses the parsed op commands to traverse the ObjectNode and set the value
     * specified by the Epath Object.
     * @param e EPath Object
     * @param context ObjectNode where the EPath begins
     * @param value value of the object to set.  If the EPath is points to a
     * collection, designated by [*], then the value must be of the Collection
     * type.  If the EPath points to a complete object, denoted by child[key].*,
     * then the value must be of ObjectNode type
     * @param byReference sets the field value by reference
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */    
    public static void setFieldValue(EPath e, ObjectNode context, Object value,
        boolean byReference) throws ObjectException, EPathException {
        ObjectNode current = context;

        for (int i = 0; i < e.ops.length; i++) {
            if (current == null) {
                // @todo caller method should log epath object when catching an exception
               throw new EPathException("Current context was null while processing element [" 
                + i + "] of epath: " + e);
            }
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);

                for (int j = 0; j < e.filters[i].length; j++) {
                    String field = (e.filters[i][j]).getField();
                    int type = key.getKeyType(field);
                    String stringValue = unmaskString((e.filters[i][j]).getValue());
                    Object keyValue = null;
                    if (stringValue != null) {
                        switch (type) {
                            case ObjectField.OBJECTMETA_STRING_TYPE:
                                keyValue = stringValue;
                                break;
                            case ObjectField.OBJECTMETA_FLOAT_TYPE:
                                keyValue = new Float(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_INT_TYPE:
                                keyValue = new Integer(stringValue);
                                break;
                            case ObjectField.OBJECTMETA_LONG_TYPE:
                                keyValue = new Long(stringValue);
                                break;
                            default:
                                throw new ObjectException("Invalid key type: " + type);
                        }
                    }
                    
                    key.setKeyValue(field, keyValue);
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;

            case EPath.OP_FIELD:

                if (current != null) {
                    current.setValue(e.tokenQueue[i], value);
                }

                break;

            case EPath.OP_ALL_FIELD:

                // @todo: check value is ObjectNode type
                // do not do adds, only updates
                current.updateIfNotNull((ObjectNode) value, true, true);

                break;

            case EPath.OP_ALL_SECONDARY:

                // check value is Collection type
                if (value instanceof Collection) {
                    Collection c = (Collection) value;
                    Iterator iter = c.iterator();

                    // loop through all of the child ObjectNodes, see if it
                    // already exists in the current node.
                    // if already exist, update, else add
                    while (iter.hasNext()) {
                        ObjectNode n = (ObjectNode) iter.next();

                        if (n != null) {
                            SuperKey k = n.pGetSuperKey();
                            if (k == null) {
                                if (!byReference) {
                                    n = n.copy();
                                }

                                current.addChild(n);
                            } else {
                                ObjectNode child = current.getChild(n.pGetType(), k);
                                if (null == child) {
                                    if (!byReference) {
                                        n = n.copy();
                                    }
                                
                                    current.addChild(n);
                                } else {
                                    child.updateIfNotNull(n, true, true);
                                }
                            }
                        }
                    }
                } else {
                    if (value == null) {
                        throw new EPathException(
                            "Value is null.  Trying to set value for: " + e);
                    } else {
                        throw new EPathException(
                            "Expecting a Collection, instead of: " + value.getClass().getName() 
                                + " Trying to set value for: " + e);
                    }
                }

                return;

            case EPath.OP_SECONDARY_BY_INDEX:
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }
    }

    /** uses the parsed op commands to traverse the ObjectNode and set the field
     * specified by the Epath Object to null.
     * @param fqfn Fully qualified field name
     * @param context ObjectNode where the EPath begins
     * @param forceNullable Null field even if not set as nullable
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException EPath exception
     */
    public static void setFieldNull(String fqfn, ObjectNode context,
        boolean forceNullable) throws ObjectException, EPathException {
        EPath e = EPathParser.parse(fqfn);
        ObjectNode current = context;

        for (int i = 0; i < e.ops.length; i++) {
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);

                for (int j = 0; j < e.filters[i].length; j++) {
                    key.setKeyValue((e.filters[i][j]).getField(),
                        unmaskString((e.filters[i][j]).getValue()));
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;

            case EPath.OP_FIELD:

                if (current != null) {
                    if (forceNullable) {
                        current.setNullable(e.tokenQueue[i], true);
                    }

                    current.setNull(e.tokenQueue[i], true);
                }

                break;

            case EPath.OP_ALL_FIELD:
                break;

            case EPath.OP_ALL_SECONDARY:
                return;

            case EPath.OP_SECONDARY_BY_INDEX:
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }
    }

    /** sets a field value, creates new objects along the path
     * if it does not exist already.
     * @param e EPath Object
     * @param context context of the operation
     * @param value value
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException unsupported operation
     */
    public static void addObjectValue(EPath e, ObjectNode context, Object value)
        throws ObjectException, EPathException {
        ObjectNode current = context;
        ObjectNode tmpNode;

        for (int i = 0; i < e.ops.length; i++) {
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);

                if (key == null) {
                  tmpNode = null;
                  ArrayList names = new ArrayList();
                  ArrayList types = new ArrayList();
                  ArrayList values = new ArrayList();
                  for (int j = 0; j < e.filters[i].length; j++) {
                    names.add(j, e.filters[i][j].getField());
                    types.add(j, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
                    values.add(j, e.filters[i][j].getValue());
                  }
                  key = new SuperKey(names, types, values);
                } else {
                  for (int j = 0; j < e.filters[i].length; j++) {
                    key.setKeyValue((e.filters[i][j]).getField(),
                        unmaskString((e.filters[i][j]).getValue()));
                  }
                  tmpNode = current.getChild(e.tokenQueue[i], key);
                }

                if (tmpNode == null) {
                    tmpNode = SimpleFactory.create(e.tokenQueue[i]);
                    tmpNode.setKey(key);
                    current.addChild(tmpNode);
                }

                current = tmpNode;

                break;

            case EPath.OP_FIELD:
                current.setValue(e.tokenQueue[i], value);

                break;

            case EPath.OP_SECONDARY_BY_INDEX:
                tmpNode = current.getChild(e.tokenQueue[i], e.indices[i]);

                if (tmpNode == null) {
                    tmpNode = SimpleFactory.create(e.tokenQueue[i]);
                    current.addChild(tmpNode);
                }

                current = tmpNode;

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }
    }

    /** set a field on one or multiple object nodes with the provided values
     *
     * e.g. setting Address[*].state with [CA, AZ] will set the state field
     * in the first and second Address object node to CA and AZ respectively.
     * If the Address object nodes don't exist already, they are created.
     *
     * the EPath should contain one * if using this to set multiple values,
     * it will start setting on the object nodes,
     * from index 0 to number of values.
     *
     * creates new objects along the path if it does not exist already.
     * @param e EPath object
     * @param pos The current position, only used internally for recursion. Should be set to 0 in the initial call.
     * @param context context of the operation
     * @param values pre allocated list to hold values
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException unsupported operation
     */
    public static void addObjectValues(EPath e, int pos, ObjectNode context,
        ArrayList values) throws ObjectException, EPathException {
        ObjectNode current = context;
        ObjectNode tmpNode;

        for (int i = pos; i < e.ops.length; i++) {
            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY:

                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);

                for (int j = 0; j < e.filters[i].length; j++) {
                    key.setKeyValue((e.filters[i][j]).getField(),
                        unmaskString((e.filters[i][j]).getValue()));
                }

                tmpNode = current.getChild(e.tokenQueue[i], key);

                if (tmpNode == null) {
                    tmpNode = SimpleFactory.create(e.tokenQueue[i]);
                    tmpNode.setKey(key);
                    current.addChild(tmpNode);
                }

                current = tmpNode;

                break;

            case EPath.OP_FIELD:
                current.setValue(e.tokenQueue[i], values.get(0));

                break;

            case EPath.OP_ALL_SECONDARY: {
                ArrayList c = current.pGetChildren(e.tokenQueue[i]);
                int j = i + 1;

                // Go through all the values
                Iterator valuesIter = values.iterator();
                int noOfChildren = 0;

                if (c != null) {
                    noOfChildren = c.size();
                }

                
                // Handles deleting a child object followed by add.  Removed nodes
                // should be ignored.  Otherwise, the standardized values in the 
                // values array list will be added to the wrong node.
                int valueCount = 0;
                int ValuesSize=values.size();
                int valuePointer=0;

                while (valuePointer < ValuesSize) {
                    Object value = values.get(valuePointer);

                    if (noOfChildren <= valueCount) {
                        // Add child
                        tmpNode = SimpleFactory.create(e.tokenQueue[i]);
                        current.addChild(tmpNode);
                    } else {
                        tmpNode = (ObjectNode) c.get(valueCount);
                    }

                    // ignore any removed nodes
                    if (tmpNode.isRemoved() == false) {
                        // Set child value
                        ArrayList singleValue = new ArrayList();
                        singleValue.add(value);
                        addObjectValues(e, j, tmpNode, singleValue);
                        valueCount++;
                        valuePointer++;
                    } else {
                        valueCount++;
                    }
                    
                }
            }

            // because the recursive call would have traversed the rest of the path,
            // return instead of continue processing the path
            return;

            case EPath.OP_SECONDARY_BY_INDEX:
                tmpNode = current.getChild(e.tokenQueue[i], e.indices[i]);

                if (tmpNode == null) {
                    tmpNode = SimpleFactory.create(e.tokenQueue[i]);
                    current.addChild(tmpNode);
                }

                current = tmpNode;

                break;

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }
    }

    /** Convenience method to return a list of field values denoted by the EPath object
     * for Example, Person.Address[*].Line1 returns a collection of Line1 from
     * each of the Addresss objects
     * @param e the epath object
     * @param pos The current position, only used internally for recursion. Should be set to 0 in the initial call.
     * @param context context of the operation
     * @param values pre allocated list to hold values
     * @throws ObjectException ObjectNode access exception
     * @throws EPathException unsupported operation
     */
    public static void getFieldList(EPath e, int pos, ObjectNode context,
        List values) throws ObjectException, EPathException {
        ObjectNode current = context;
        ObjectNode tmpNode;

        for (int i = pos; i < e.ops.length; i++) {
            // Check if current == null and current.isRemoved()
            // in which case there is no further values to add
            if ((current == null) || current.isRemoved()) {
                return;
            }

            switch (e.ops[i]) {
            case EPath.OP_NOOP:
                break;

            case EPath.OP_SECONDARY_BY_KEY: {
                SuperKey key = current.getChildSuperKey(e.tokenQueue[i]);

                for (int j = 0; j < e.filters[i].length; j++) {
                    key.setKeyValue((e.filters[i][j]).getField(),
                        unmaskString((e.filters[i][j]).getValue()));
                }

                current = current.getChild(e.tokenQueue[i], key);

                break;
            }

            case EPath.OP_ALL_FIELD: {
                values.add(current);

                break;
            }

            case EPath.OP_FIELD: {
                Object value = current.getValue(e.tokenQueue[i]);
                values.add(value);

                break;
            }

            case EPath.OP_ALL_SECONDARY: {
                Collection c = current.pGetChildren(e.tokenQueue[i]);

                if (null != c) {
                    Iterator iter = c.iterator();
                    int j = i + 1;

                    while (iter.hasNext()) {
                        ObjectNode n = (ObjectNode) iter.next();
                        getFieldList(e, j, n, values);
                    }
                }

                // because the recursive call would have traversed the rest of the path,
                // return instead of continued processing of the path
                return;
            }

            case EPath.OP_SECONDARY_BY_INDEX: {
                current = current.getChild(e.tokenQueue[i], e.indices[i]);

                break;
            }

            case EPath.OP_SECONDARY_BY_FILTER: {
                // get all the children by type
                List childList = current.pGetChildren(e.tokenQueue[i]);

                // tell the recursive call to start one position further down the stack
                int z = i + 1;

                for (int j = 0; childList != null && j < childList.size(); j++) {
                    tmpNode = (ObjectNode) childList.get(j);

                    boolean include = true;

                    for (int k = 0; k < e.filters[i].length; k++) {
                        String field = e.filters[i][k].getField();
                        String value = e.filters[i][k].getValue();

                        Object curValue = tmpNode.getValue(field);

                        if (!value.equals(curValue)) {
                            include = false;
                        }
                    }

                    if (include) {
                        getFieldList(e, z, tmpNode, values);
                    }
                }

                // because the recursive call would have traversed the rest of the path,
                // return instead of continue processing the path
                return;
            }

            default:
                throw new EPathException("unrecognized op:" + e.ops[i]);
            }
        }
    }
}
