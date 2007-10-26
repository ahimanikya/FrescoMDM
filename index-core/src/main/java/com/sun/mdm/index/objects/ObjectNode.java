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
package com.sun.mdm.index.objects;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.exception.InvalidObjectException;
import com.sun.mdm.index.objects.exception.NotNullableFieldException;
import com.sun.mdm.index.objects.exception.InvalidKeyObjectException;
import com.sun.mdm.index.objects.exception.InvalidFieldNameException;
import com.sun.mdm.index.objects.exception.DuplicateChildKeyException;
//import com.sun.mdm.index.objects.validation.exception.MaximumConstraintException;
import com.sun.mdm.index.ops.TransactionLog;


import java.io.Externalizable;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;



/**
* The <b>ObjectNode</b> class performs the following functions:
* <UL>
* <LI>Maintains object control flags (UPDATE, REMOVE, and ADD).
* <LI>Maintains object change logs.
* <LI>Contains a list of child tags (a tag is the name of a type of
* child object, such as "ADDRESS" or "PHONE").
* <LI>Contains a list of child objects.
* <LI>Contains a list of object fields (ObjectField class).
* <LI>Contains one parent object.
* <LI>Maintains the parent tag (that is, the name of the parent object,
* such as "PERSON" or "COMPANY").
* <LI>Maintains its own tag.
* </UL>
* @author gzheng
*/
public class ObjectNode implements Externalizable {
   static final long serialVersionUID = 907956887390148938L;
   public static int mVersion = 2; // with mChildrenHashMap

   /**
    * bit mask for object KeyChange
    */
   private final int mKeyChangeMask = 8;

   /**
    * bit mask for object UPDATE
    */
   private final int mUpdateMask = 1;

   /**
    * bit mask for object REMOVE
    */
   private final int mRemoveMask = 2;

   /**
    * bit mask for object ADD
    */
   private final int mAddMask = 4;

   /**
    * bit mask for object read access flag
    */
   private final int mObjectReadAccessMask = 1;

   /**
    * bit mask for object update access flag
    */
   private final int mObjectUpdateAccessMask = 2;

   /**
    * bit mask for object delete access flag
    */
   private final int mObjectDeleteAccessMask = 4;

   /**s
    * bit mask for object add access flag
    */
   private final int mObjectAddAccessMask = 8;
   private ArrayList mFieldUpdateLogs = null;

   private Logger mLogger = LogUtil.getLogger(this);
   private boolean mDebug = mLogger.isDebugEnabled();

   /**
    * list of child object tags
    */
   protected ArrayList mChildTags;

   /**
    * list of child objects
    */
   //protected ArrayList mChildren;

   /**
    * map of child objects
    * |key |value    |
    * |type|ArrayList|
    */
   protected HashMap mChildrenHashMap;

   /**
    * list of fields
    */
   protected HashMap mFields;

   /**
    * the parent object
    */
   protected ObjectNode mParent;

   /**
    * parent object tag
    */
   protected String mParentTag;

   /**
    * object tag
    */
   protected String mTag;
   private int mObjectFlag;
   private int mObjectAccessFlag = mObjectReadAccessMask
   + mObjectUpdateAccessMask + mObjectDeleteAccessMask + mObjectAddAccessMask;


   public Map indexMap = new HashMap();
   protected int mPosition = -1;
   protected boolean mPartOfIndex;

   private transient ObjectNode selfRef = this;

    /**
    * Creates a new instance of the ObjectNode class.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public ObjectNode() {
       mObjectAccessFlag = mObjectReadAccessMask + mObjectUpdateAccessMask
       + mObjectDeleteAccessMask + mObjectAddAccessMask;
   }

   /**
    * Creates a new instance of the ObjectNode class by its object tag,
    * field names, and field types.
    * <p>
    * @param tag A string containing the object tag.
    * @param names A list of names of the fields contained in the object node.
    * @param types A list of the types of fields contained in the object node.
    * @exception ObjectException Thrown if an error occurs while creating
    * the instance.
    * @include
    */
   public ObjectNode(String tag, ArrayList names, ArrayList types) throws ObjectException {
       if ((null == names) || (null == types) || (names.size() != types.size())) {
           throw new InvalidFieldNameException("Invalid field definitions: "
           + tag + "\n" + names + "\n" + types);
       }

       mTag = tag;
       mFields = new HashMap();
       mObjectFlag = 0;
       mObjectAccessFlag = mObjectReadAccessMask + mObjectUpdateAccessMask
       + mObjectDeleteAccessMask + mObjectAddAccessMask;

       for (int i = 0; i < names.size(); i++) {
           ObjectField field = new ObjectField((String) names.get(i),
           ((Integer) types.get(i)).intValue());
           mFields.put((String) names.get(i), field);
           setKeyType((String) names.get(i), false);
           setNullable((String) names.get(i), true);
       }
   }

   public String getObjectId() throws ObjectException
   {
       return null;
   }

   public boolean isNew()
   {
       boolean ret = false;
       try {
           ret = (getObjectId() == null) ? true : false;
       } catch (ObjectException ex) {
           ret = true;
       }

       return ret;
   }

   /**
    * Creates a new instance of the ObjectNode class by its object tag,
    * field names, field types, and field values.
    * <p>
    * @param tag A string containing the object tag.
    * @param names A list of names of the fields contained in the object node.
    * @param types A list of the types of fields contained in the object node.
    * @param values A list of the values of the fields contained in the object
    * node.
    * @exception ObjectException Thrown if an error occurs while creating
    * the instance.
    * @include
    */
   public ObjectNode(String tag, ArrayList names, ArrayList types, ArrayList values) throws ObjectException {
       if (null == names) {
           throw new InvalidFieldNameException(
           "ObjectNode cannot be constructed from empty field names");
       }

       if ((null == values) || (null == types) || (values.size() != names.size())
       || (types.size() != values.size())) {
           throw new InvalidFieldNameException("Invalid field definitions: "
           + tag + "\n" + names + "\n" + types + "\n" + values);
       }

       mTag = tag;
       mObjectFlag = 0;
       mObjectAccessFlag = mObjectReadAccessMask + mObjectUpdateAccessMask
       + mObjectDeleteAccessMask + mObjectAddAccessMask;
       mFields = new HashMap();

       for (int i = 0; i < names.size(); i++) {
           ObjectField field = new ObjectField((String) names.get(i),
           ((Integer) types.get(i)).intValue(), values.get(i));
           mFields.put((String) names.get(i), field);
       }
   }

   /**
    * Retrieves a child ObjectNode by its object tag and its unique objecy key
    * value.
    * <p>
    * @param type A string containing the object tag.
    * @param key The value of the unique key field.
    * @return ObjectNode A child object of the specified type with the specified
    * unique key field.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * child object.
    * @include
    */

   public ObjectNode getChild(String type, ObjectKey key)
   throws ObjectException {
   	ObjectNode ret = null;

   	try {
   		ArrayList targetChildren = getChildrenForType(type, false);

   		if (targetChildren != null) {
   			if ( key == null ) {
                if ( targetChildren.size() > 0 ) {
                    ret = (ObjectNode) targetChildren.get(targetChildren.size()-1);
                }
            } else {
   				// Key is supplied. Assuming at least one key field is not null
   				// Construct ObjectKeyType
   				ObjectKeyType objectKeyType = new ObjectKeyType(type, key,true);
   				Integer retIndexTrue = (Integer)indexMap.get(objectKeyType.toString());

   				objectKeyType = new ObjectKeyType(type, key,false);
   				Integer retIndexFalse = (Integer)indexMap.get(objectKeyType.toString());

   				int index = max(retIndexTrue,retIndexFalse);
   				if ( index != -1 ) {
   					ret = (ObjectNode)targetChildren.get(index);
   				}
   			}
   		}

   	} catch (Exception e) {
   		throw new com.sun.mdm.index.objects.exception.InvalidObjectException();
   	}

   	return ret;
   }



   /**
    * Retrieves a child ObjectNode by its object tag, its unique object key
    * value, and whether the ObjectNode is flagged for removal.
    * <p>
    * @param type A string containing the object tag.
    * @param key The value of the unique key field.
    * @param removed A Boolean value indicating whether the object is flagged
    * for removal.
    * @return ObjectNode A child object of the specified type with the specified
    * unique key field and removal flag.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * child object.
    */

   public ObjectNode getChild(String type, ObjectKey key, boolean removed)
   throws ObjectException {
   	ObjectNode ret = null;

   	try {
   		ArrayList targetChildren = getChildrenForType(type, false);
   		if ( targetChildren != null ) {
   			if ( key == null ) {
   				for (int i = 0; i < targetChildren.size(); i++) {
   					ObjectNode node = (ObjectNode) targetChildren.get(i);
   					if (node.isRemoved() == removed) {
   						ret = node;
   						break;
   					}
   				}
   			}
   			else {
   				// Key is supplied. Assuming at least one key field is not null
   				// Construct ObjectKeyType
   				ObjectKeyType objectKeyType = new ObjectKeyType(type, key,removed);

   				Integer index  = (Integer)indexMap.get(objectKeyType.toString());
   				if ( index != null ) {
   					ret = (ObjectNode)targetChildren.get(index.intValue());
   				}
   			}

   		}

   	} catch (Exception e) {
   		mLogger.error("ObjectException", e);
   		throw new ObjectException(e.getMessage());
   	}
   	return ret;
   }


   /**
    * Retrieves a child ObjectNode by its object tag and its unique
    * identification code assigned by the master index.
    * <p>
    * @param type A string containing the object tag.
    * @param id The child object's unique identification code as assigned by
    * the master index.
    * @return ObjectNode A child object of the specified type with the specified
    * ID.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * child object.
    * @include
    */
   public ObjectNode getChild(String type, String id)
   throws ObjectException {
       ObjectNode ret = null;

       try {
           // Check keyed children first
           ArrayList targetChildren = getChildrenForType(type, false);

           if (targetChildren != null) {
               for (int i=0; i<targetChildren.size(); i++) {
                   ObjectNode node = (ObjectNode) targetChildren.get(i);

                   if (node.pGetTag().equals(type) && node.getObjectId() != null && node.getObjectId().equals(id)) {
                       ret = node;
                       break;
                   }
               }
           }

       } catch (ObjectException e) {
           throw e;
       }

       return ret;
   }

   /**
    * Retrieves a child ObjectNode by its object tag and its index
    * position.
    * <p>
    * @param type A string containing the object tag.
    * @param pos The index position of the child object.
    * @return ObjectNode A child object of the specified type in the specified
    * index position.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * child object.
    * @include
    */
   public ObjectNode getChild(String type, int pos) {
       ObjectNode ret = null;

       // To get it from the natural order
       ArrayList targetChildren = getChildrenForType(type, false);
       if (targetChildren != null && pos < targetChildren.size()) {
           ret = (ObjectNode) targetChildren.get(pos);
       }

       return ret;
   }

   /**
    * Retrieves the unique key field setting for the child object identified by
    * the specified object tag.
    * <p>
    * @param type A string containing the object tag.
    * @return ObjectKey The setting of the unique key field for the specified
    * child object.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * key setting.
    *
    */
   public ObjectKey getChildKey(String type) throws ObjectException {
       ObjectKey ret = null;

       try {
           ArrayList targetChildren = getChildrenForType(type, false);
           if (targetChildren != null && targetChildren.size() > 0) {
               ObjectNode node = (ObjectNode) targetChildren.get(0);
               ret = node.pGetKey();
           }
       } catch (ObjectException e) {
           throw e;
       }

       return ret;
   }


   /**
    * Retrieves the unique object key setting for the child object identified by the
    * specified object tag. If there is no unique key field for the object, this
    * method retrieves the unique ID for the object as assigned by the master
    * index.
    * <p>
    * @param type A string containing the object tag.
    * @return SuperKey The setting of the unique key field or, if no unique key
    * field exists, the unique master index ID of the specified child object.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * key setting.
    *
    */
   public SuperKey getChildSuperKey(String type) throws ObjectException {
       SuperKey ret = null;

       try {
           ArrayList targetChildren = getChildrenForType(type, false);
           if (targetChildren != null && targetChildren.size() > 0) {
               ObjectNode node = (ObjectNode) targetChildren.get(0);
               ret = node.pGetSuperKey();
           }
       } catch (ObjectException e) {
           throw e;
       }

       return ret;
   }

   /**
    * Retrieves a list of the names of the different types of child objects
    * defined in the object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of child object tags.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetChildTags() {
       return mChildTags;
   }

   /**
    * Retrieves a list of all child nodes in the object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of child object nodes.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetChildren() {
       ArrayList list = getAllChildrenFromHashMap();
       return list;
   }

   /**
    * Retrieves a list of all child nodes of the specified child type.
    * <p>
    * @param type A string containing an object tag.
    * @return ArrayList A list of child object nodes.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetChildren(String type) {
       // 01/15/05
       ArrayList aTargetChildren = getChildrenForType(type, false);
       ArrayList list = null;

       if (aTargetChildren != null) {
           list = new ArrayList();
           list.addAll(aTargetChildren);
       }

       return list;
   }

   /**
    * Retrieves a list of all field names in the object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of field names.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetFieldNames() {
       ArrayList ret = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();
               ret.add(field.getName());
           }
       }

       return ret;
   }

   /**
    * Retrieves a list of the types of fields in the object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of field types.
    * <DT><B>Throws:</B><DD>None.
    * @exclude
    */
   protected ArrayList getFieldTypes() {
       ArrayList ret = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();
               ret.add(new Integer(field.getType()));
           }
       }

       return ret;
   }

   /**
    * Retrieves a list of transaction log entries for the object
    * node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of transaction log entries.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetFieldUpdateLogs() {
       return mFieldUpdateLogs;
   }

   /**
    * Retrieves a list of field values for the fields defined in the object
    * node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ArrayList A list of transaction log entries.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ArrayList pGetFieldValues() {
       ArrayList ret = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();
               ret.add(field.getValue());
           }
       }

       return ret;
   }

   /**
    * Retrieves an ObjectField object by its field name.
    * <p>
    * @param fieldname The name of the field whose ObjectField object you want
    * to retrieve.
    * @return ObjectField The field object corresponding to the given field
    * name.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * ObjectField object.
    * @include
    */
   public ObjectField getField(String fieldname) throws ObjectException {
       ObjectField field = null;

       if (mFields == null) {
           throw new ObjectException("the object has empty field list");
       }

       field = (ObjectField) mFields.get(fieldname);

       if (field == null) {
           throw new ObjectException("object '" + pGetTag() + "' does not have field '" + fieldname + "'");
       }

       return field;
   }

   /**
    * Retrieves an array list of all ObjectField objects in the object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ObjectField[] A list of field objects.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public ObjectField[] pGetFields() {
       ObjectField[] ret = null;

       if (null != mFields) {
           ret = new ObjectField[mFields.size()];

           Iterator it = mFields.values().iterator();
           int count = 0;

           while (it.hasNext()) {
               ret[count++] = (ObjectField) it.next();
           }
       }

       return ret;
   }

   /**
    * Checks whether a field's bit mask is enabled or disabled.
    * <p>
    * @param name The name of the field to check.
    * @param mask The bit mask of the field.
    * @return boolean An indicator of whether the bit mask is enabled or
    * disabled. <B>True</B> indicates the bit mask is enabled; <B>false</B>
    * indicates it is disabled.
    * @exception ObjectException Thrown if an error occurs while checking the
    * bit mask status.
    *
    */
   public boolean getFlag(String name, int mask) throws ObjectException {
       boolean bRet = false;

       try {
           ObjectField field = getField(name);
           bRet = field.getFlag(mask);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * gets Object Key setting
    * @return ObjectKey object key
    * @exception ObjectException object exception
    */
   protected ObjectKey obtainKey() throws ObjectException {
       ArrayList names = new ArrayList();
       ArrayList types = new ArrayList();
       ArrayList values = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();

               if (field.isKeyType()) {
                   names.add(field.getName());
                   types.add(new Integer(field.getType()));
                   values.add(field.getValue());
               }
           }
       }

       ObjectKey key = null;

       if (names.size() > 0) {
           try {
               key = new ObjectKey(names, types, values);
           } catch (ObjectException e) {
               throw e;
           }
       }

       return key;
   }


   /**
    * gets Object Key setting
    * @return ObjectKey object key
    * @exception ObjectException object exception
    */
   protected SuperKey obtainSuperKey() throws ObjectException {
       ArrayList names = new ArrayList();
       ArrayList types = new ArrayList();
       ArrayList values = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();

               if (field.isKeyType()) {
                   names.add(field.getName());
                   types.add(new Integer(field.getType()));
                   values.add(field.getValue());
               }
           }
       }

       SuperKey key = null;

       if (names.size() > 0) {
           try {
               key = new SuperKey(names, types, values);
           } catch (ObjectException e) {
               throw e;
           }
       } else if (getObjectId() != null) {
           try {
               names.add("ObjectId");
               types.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
               values.add(getObjectId());
               key = new SuperKey(names, types, values);
           } catch (ObjectException e) {
               throw e;
           }
       }

       return key;
   }

   /**
    * Retrieves the unique object key setting for the object node. This includes
    * a list of field names, field types, and field values.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ObjectKey The names, types, and values of the object's unique key
    * fields.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * key setting.
    * @include
    */
   public ObjectKey pGetKey() throws ObjectException {
       return obtainKey();
   }

   /**
    * Retrieves the unique key field setting for the object node. This includes
    * a list field names, field types, and field values. If there are no unique
    * key fields defined for the given object, the object ID is returned.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ObjectKey The names, types, and values of the object's unique key
    * fields.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * key setting.
    *
    */
   public SuperKey pGetSuperKey() throws ObjectException {
       return obtainSuperKey();
   }

   /**
    * Retrieves the parent object node.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return ObjectNode The parent object node of the given object.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public ObjectNode getParent() {
       return mParent;
   }

   /**
    * gets parent object tag
    *
    * @return String parent's object tag
    */
   protected String getParentTag() {
       return mParentTag;
   }

   /**
    * Retrieves the tag of the ObjectNode object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return String The object tag for the given object.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public String pGetTag() {
       return mTag;
   }

   /**
    * Retrieves the tag of the ObjectNode object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return String The object tag for the given object.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public String pGetType() {
       return pGetTag();
   }

   /**
    * Retrieves a field's data type given the field's name.
    * <p>
    * @param name The name of the field to check.
    * @return int An integer representing the field type.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * field type.
    *
    */
   public int pGetType(String name) throws ObjectException {
       int type = -1;

       try {
           ObjectField field = getField(name);
           type = field.getType();
       } catch (ObjectException e) {
           throw e;
       }

       return type;
   }

   /**
    * Retrieves the value of a field given the field's name.
    * <p>
    * @param name The name of the field to retrieve.
    * @return Object The value of the given field.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * field value.
    * @include
    */
   public Object getValue(String name) throws ObjectException {
       Object value = null;

       try {
           ObjectField field = getField(name);
           value = field.getValue();
       } catch (ObjectException e) {
           throw e;
       }

       return value;
   }

   /**
    * Retrieves a field object (ObjectField) given the field's name.
    * <p>
    * @param name The name of the field.
    * @return ObjectField A field object with the given field name.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * field object.
    *
    */
   public ObjectField getValueObject(String name) throws ObjectException {
       ObjectField value = null;

       try {
           value = getField(name);
       } catch (ObjectException e) {
           throw e;
       }

       return value;
   }

   /**
    * Checks whether the object node has been added to the object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object node is new.
    * <B>True</B> indicates the node is new; <B>false</B> indicates it is not
    * new.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public boolean isAdded() {
       return (0 != (mObjectFlag & mAddMask) && !isRemoved()) ? true : false;
   }

   /**
    * Checks whether key fields in the object node have changed.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether key fields in the object node have
    * changed. <B>True</B> indicates key fields have changed; <B>false</B>
    * indicates they have not changed.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public boolean isKeyChanged() {
       return (0 != (mObjectFlag & mKeyChangeMask) && !isRemoved()) ? true : false;
   }

   /**
    * Checks whether the object has read access.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object has read access.
    * <B>True</B> indicates the object has read access; <B>false</B>
    * indicates it does not.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public boolean hasObjectReadAccess() {
       return (0 != (mObjectAccessFlag & mObjectReadAccessMask)) ? true : false;
   }

   /**
    * Checks whether the object has update access.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object has update access.
    * <B>True</B> indicates the object has update access; <B>false</B>
    * indicates it does not.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public boolean hasObjectUpdateAccess() {
       return (0 != (mObjectAccessFlag & mObjectUpdateAccessMask)) ? true : false;
   }

   /**
    * Checks whether the object has delete access.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object has delete access.
    * <B>True</B> indicates the object has delete access; <B>false</B>
    * indicates it does not.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public boolean hasObjectDeleteAccess() {
       return (0 != (mObjectAccessFlag & mObjectDeleteAccessMask)) ? true : false;
   }

   /**
    * Checks whether the object has add access.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object has add access.
    * <B>True</B> indicates the object has add access; <B>false</B>
    * indicates it does not.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public boolean hasObjectAddAccess() {
       return (0 != (mObjectAccessFlag & mObjectAddAccessMask)) ? true : false;
   }

   /**
    * Checks whether the given field has read access.
    * <p>
    * @param name The name of the field.
    * @return boolean An indicator of whether the field has read access.
    * <B>True</B> indicates the field has read access; <B>false</B>
    * indicates it does not.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * read access status of the field.
    *
    */
   public boolean hasFieldReadAccess(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.READACCESSTYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether the given field has update access.
    * <p>
    * @param name The name of the field.
    * @return boolean An indicator of whether the field has update access.
    * <B>True</B> indicates the field has update access; <B>false</B>
    * indicates it does not.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * update access status of the field.
    *
    */
   public boolean hasFieldUpdateAccess(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.UPDATEACCESSTYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether the value of a field changed in the current transaction.
    * <p>
    * @param name The name of the field.
    * @return boolean An indicator of whether the field value changed.
    * <B>True</B> indicates the field value changed; <B>false</B>
    * indicates it did not change.
    * @exception ObjectException Thrown if an error occurs while checking whether
    * the value of the field changed.
    * @include
    */
   public boolean isChanged(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.CHANGEDTYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether the given field is a unique key field.
    * <p>
    * @param name The name of the field.
    * @return boolean An indicator of whether the field is a unique key field.
    * <B>True</B> indicates the field is a unique key field; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * unique key access status of the field.
    *
    */
   public boolean isKeyType(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.KEYTYPETYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether the given key object is valid.
    * <p>
    * @param key The object key (ObjectKey) to check.
    * @return boolean An indicator of whether the key is valid.
    * <B>True</B> indicates the key is valid; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while checking
    * the key.
    *
    */
   public boolean isKeyValid(ObjectKey key) {
       boolean bRet = false;

       ArrayList knames = new ArrayList();
       ArrayList ktypes = new ArrayList();

       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();

               if (field.isKeyType()) {
                   knames.add(field.getName());
                   ktypes.add(new Integer(field.getType()));
               }
           }
       }

       ArrayList keynames = key.getKeyNames();
       ArrayList keytypes = key.getKeyTypes();

       if ((knames.size() == keynames.size()) && knames.containsAll(keynames)
       && (knames.size() == keytypes.size()) && ktypes.containsAll(keytypes)) {
           bRet = true;
       }

       return bRet;
   }

   /**
    * Checks whether the given field contains a null value.
    * <p>
    * @param name The name of the field to check.
    * @return boolean An indicator of whether the field value is null.
    * <B>True</B> indicates the value of the field is null; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while checking the
    * value of the field.
    *
    */
   public boolean isNull(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.NULLTYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether the value of the given field can be null.
    * <p>
    * @param name The name of the field.
    * @return boolean An indicator of whether the value of field can be null.
    * <B>True</B> indicates the field value can be null; <B>false</B>
    * indicates it cannot.
    * @exception ObjectException Thrown if an error occurs while checking
    * the field.
    *
    */
   public boolean isNullable(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.NULLABLETYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether an object node is marked for deletion.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the field is marked for deletion.
    * <B>True</B> indicates the field is marked for deletion; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while retrieving the
    * status of the field.
    * @include
    */
   public boolean isRemoved() {
       return (0 != (mObjectFlag & mRemoveMask)) ? true : false;
   }

   /**
    * Checks whether a field is used for searches.
    * <p>
    * @param name The name of the field to check.
    * @return boolean An indicator of whether the field is used for searches.
    * <B>True</B> indicates the field is used; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while checking
    * the field.
    *
    */
   public boolean isSearched(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.SEARCHEDTYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Checks whether an object node has been updated during a transaction.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return boolean An indicator of whether the object node was updated.
    * <B>True</B> indicates the object was updated; <B>false</B>
    * indicates it was not.
    * @exception ObjectException Thrown if an error occurs while checking
    * the object.
    * @include
    */
   public boolean isUpdated() {
       return (0 != (mObjectFlag & mUpdateMask) && !isRemoved() && !isAdded()) ? true : false;
   }

   /**
    * Checks whether a field is visible on the Enterprise Data Manager (EDM).
    * <p>
    * @param name The name of the field to check.
    * @return boolean An indicator of whether the field is visible on the EDM.
    * <B>True</B> indicates the field is visible; <B>false</B>
    * indicates it is not.
    * @exception ObjectException Thrown if an error occurs while checking
    * the field.
    *
    */
   public boolean isVisible(String name) throws ObjectException {
       boolean bRet = false;

       try {
           bRet = getFlag(name, FieldFlag.VISIBLETYPE);
       } catch (ObjectException e) {
           throw e;
       }

       return bRet;
   }

   /**
    * Sets the value of the add flag, indicating whether the ObjectNode object
    * is newly added.
    * <p>
    * @param flag A Boolean flag indicating whether the object node is newly
    * added. <b>True</b> indicates the object node is newly added; <b>false</b>
    * indicates it is not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setAddFlag(boolean flag) {
       if (flag) {
           mObjectFlag |= mAddMask;
       } else {
           mObjectFlag &= ~mAddMask;
       }
   }

   /**
    * Sets the value of the key change flag, indicating whether key fields were
    * changed in the ObjectNode object.
    * <p>
    * @param flag A Boolean flag indicating whether key fields were updated in
    * the object node. <b>True</b> indicates key fields were updated; <b>false</b>
    * indicates they were not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setKeyChangeFlag(boolean flag) {
       if (flag) {
           mObjectFlag |= mKeyChangeMask;
       } else {
           mObjectFlag &= ~mKeyChangeMask;
       }
   }

   /**
    * Sets the value of the field change flag, indicating whether the specified
    * fields was changed in the ObjectNode object.
    * <p>
    * @param name The name of the field whose change flag is being set.
    * @param flag A Boolean flag indicating whether the field was updated in
    * the object node. <b>True</b> indicates the field was updated; <b>false</b>
    * indicates it was not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the flag.
    *
    */
   public void setChanged(String name, boolean flag) throws ObjectException {
       try {
           setFlag(name, FieldFlag.CHANGEDTYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the read access flag for the object node, indicating
    * whether the object has read access.
    * <p>
    * @param flag A Boolean flag indicating whether the object node has read
    * access. <b>True</b> indicates the object node has read access; <b>false</b>
    * indicates it does not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setObjectReadAccess(boolean flag) {
       if (flag) {
           mObjectAccessFlag = mObjectAccessFlag | mObjectReadAccessMask;
       } else {
           mObjectAccessFlag = mObjectAccessFlag & ~mObjectReadAccessMask;
       }
   }

   /**
    * Sets the value of the update access flag for the object node, indicating
    * whether the object has update access.
    * <p>
    * @param flag A Boolean flag indicating whether the object node has update
    * access. <b>True</b> indicates the object node has update access;
    * <b>false</b> indicates it does not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setObjectUpdateAccess(boolean flag) {
       if (flag) {
           mObjectAccessFlag = mObjectAccessFlag | mObjectUpdateAccessMask;
       } else {
           mObjectAccessFlag = mObjectAccessFlag & ~mObjectUpdateAccessMask;
       }
   }

   /**
    * Sets the value of the delete access flag for the object node, indicating
    * whether the object has delete access.
    * <p>
    * @param flag A Boolean flag indicating whether the object node has delete
    * access. <b>True</b> indicates the object node has delete access; <b>false</b>
    * indicates it does not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setObjectDeleteAccess(boolean flag) {
       if (flag) {
           mObjectAccessFlag = mObjectAccessFlag | mObjectDeleteAccessMask;
       } else {
           mObjectAccessFlag = mObjectAccessFlag & ~mObjectDeleteAccessMask;
       }
   }

   /**
    * Sets the value of the add access flag for the object node, indicating
    * whether the object has add access.
    * <p>
    * @param flag A Boolean flag indicating whether the object node has add
    * access. <b>True</b> indicates the object node has add access; <b>false</b>
    * indicates it does not.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * <DT><B>Throws:</B><DD>None.
    *
    */
   public void setObjectAddAccess(boolean flag) {
       if (flag) {
           mObjectAccessFlag = mObjectAccessFlag | mObjectAddAccessMask;
       } else {
           mObjectAccessFlag = mObjectAccessFlag & ~mObjectAddAccessMask;
       }
   }

    /**
     * Sets a list of transaction log entries.
     * <p>
     * @param logs An array list of transaction log entries.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
   public void setFieldUpdateLogs(ArrayList logs) {
       mFieldUpdateLogs = logs;
   }

    /**
     * Sets a field's bit bit mask, which indicates whether to update, add,
     * or remove a field. No bit mask indicates that no action is taken against
     * a field.
     * <p>
     * @param name The name of the field whose bit mask is being set.
     * @param mask The bit mask indicator for the field.
     * @param flag A boolean flag that indicates...
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the flag.
     *
     * sets a field's bit mask
     *
     * @param name field name
     * @param mask bit mask
     * @param flag boolean flag
     * @exception ObjectException ObjectException
     * mKeyChangeMask=8
     * mUpdateMask=1
     * mRemoveMask=2
     * mAddMask=4
     * mObjectReadAccessMask=1
     * mObjectUpdateAccessMask=2
     * mObjectDeleteAccessMask=4
     * mObjectAddAccessMask=8
     */

   public void setFlag(String name, int mask, boolean flag)
   throws ObjectException {
       try {
           ObjectField field = getField(name);
           field.setFlag(mask, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Sets the object node's key field value.
     * <p>
     * @param key The value to set in the key field.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the flag.
     *
     */
   public void setKey(ObjectKey key) throws ObjectException {
       if (!isKeyValid(key)) {
           throw new InvalidKeyObjectException("Invalid key setting: " + key.toString());
       }

       ArrayList knames = key.getKeyNames();

       for (int i = 0; i < knames.size(); i++) {
           String name = (String) knames.get(i);
           Object value = key.getKeyValue(name);
           setValue(name, value);
       }
   }

    /**
     * Specifies whether a field is a key type field or not.
     * <p>
     * @param name The name of the field whose key type flag is being set
     * @param flag A boolean flag indicating whether the field is a key type
     * field. <b>True</b> indicates the field is a key type field; <b>false</b>
     * indicates it is not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the key type.
     *
     */
   public void setKeyType(String name, boolean flag) throws ObjectException {
       try {
           setFlag(name, FieldFlag.KEYTYPETYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Specifies whether a field is holding a null value.
     * <p>
     * @param name The name of the field whose value is being set.
     * @param flag A boolean flag indicating whether the field is holding a null
     * value. <b>True</b> indicates the field is holding a null value; <b>false</b>
     * indicates it is not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the null value indicator.
     *
     */
   public void setNull(String name, boolean flag) throws ObjectException {
       try {
           if (flag && !isNullable(name)) {
               throw new NotNullableFieldException(pGetTag() + "." + name);
           }

           setFlag(name, FieldFlag.NULLTYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Specifies whether a field can contain a null value.
     * <p>
     * @param name The name of the field whose null flag is being set.
     * @param flag A boolean flag indicating whether the field can contain a null
     * value. <b>True</b> indicates the field can be null; <b>false</b>
     * indicates it cannot.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the null flag.
     *
     */
   public void setNullable(String name, boolean flag)
   throws ObjectException {
       try {
           setFlag(name, FieldFlag.NULLABLETYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Sets a field to null and the modifies the "changed" flag to true.
     * <p>
     * @param name The name of the field being modified.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the field value or modifying the flag.
     *
     */
   public void clearField(String name) throws ObjectException {
       setNull(name, true);
       setChanged(name, true);
   }

    /**
     * Sets the value of a field's read access flag.
     * <p>
     * @param name The name of the field whose flag is being set.
     * @param flag A boolean flag indicating whether the field has read
     * access. <b>True</b> indicates the field has read access; <b>false</b>
     * indicates it does not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the read access flag.
     *
     */
   public void setFieldReadAccess(String name, boolean flag)
   throws ObjectException {
       try {
           setFlag(name, FieldFlag.READACCESSTYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Sets the value of a field's update access flag.
     * <p>
     * @param name The name of the field whose flag is being set.
     * @param flag A boolean flag indicating whether the field has update
     * access. <b>True</b> indicates the field has update access; <b>false</b>
     * indicates it does not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the update access flag.
     *
     */
   public void setFieldUpdateAccess(String name, boolean flag)
   throws ObjectException {
       try {
           setFlag(name, FieldFlag.UPDATEACCESSTYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Sets the parent object in an object node.
     * <p>
     * @param parent The name of the parent object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
   public void setParent(ObjectNode parent) {
       mParent = parent;
   }

    /**
     * Specifies whether the object node is marked for deletion.
     * <p>
     * @param flag A boolean indicator of whether to mark the object node
     * for deletion. <b>True</b> indicates the object node is marked for
     * deletion; <b>false</b> indicates it is not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
   public void setRemoveFlag(boolean flag) {
		ObjectKey objectKey = null;
   		boolean reCreateIndex = false;


   		try {
    	   		objectKey = pGetSuperKey();

   	   	   	if ( mParent != null && objectKey != null && isRemoved() != flag  ) {
   	   	   		mParent.removeChildIndex(this);
   	            reCreateIndex = true;
   	   	   	}
   	   	}
   	   	catch ( ObjectException e ) {
   	   		mLogger.error(e.getMessage(), e);
   	   		return;
   		}

       if (flag) {
           mObjectFlag |= mRemoveMask;
       } else {
           mObjectFlag &= ~mRemoveMask;
       }

       if ( reCreateIndex ) {
       	try {
       		mParent.createChildIndex(this,false);
       	}
       	catch ( ObjectException e ) {
   	   		mLogger.error(e.getMessage(), e);
       		return;
       	}
       }

   }

    /**
     * Specifies whether the given field is used for searches.
     * <p>
     * @param name The name of the field whose search flag is being set.
     * @param flag A boolean indicator of whether the field is used for
     * searches. <b>True</b> indicates the field is used for searches;
     * <b>false</b> indicates it is not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the search flag.
     *
     */
   public void setSearched(String name, boolean flag)
   throws ObjectException {
       try {
           setFlag(name, FieldFlag.SEARCHEDTYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Specifies whether the given object node was updated.
     * <p>
     * @param flag A boolean indicator of whether the object node was
     * updated. <b>True</b> indicates the object node was updated;
     * <b>false</b> indicates it was not.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     *
     */
   public void setUpdateFlag(boolean flag) {
       if (flag) {
           mObjectFlag |= mUpdateMask;
       } else {
           mObjectFlag &= ~mUpdateMask;
       }
   }

    /**
     * Resets the changes made to an object, returning the object to its
     * previous status.
     * <p>
	 * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while reversing
     * the changes to the object.
     *
     */
   public void unChange() throws ObjectException {
       if (mFieldUpdateLogs != null) {
           for (int i = 0; i < mFieldUpdateLogs.size(); i++) {
               TransactionLog log = (TransactionLog) mFieldUpdateLogs.get(i);
               if (log.getFunction() == TransactionLog.FUNCTION_UPDATE) {
                   String p = log.getPath();
                   int pos = p.lastIndexOf(".");
                   if (pos >= 0) {
                       p = p.substring(pos + 1, p.length());
                   }
                   setValue(p, log.getValue());
               }
           }
       }

 
       if (mChildrenHashMap != null) {
           Iterator it = mChildrenHashMap.values().iterator();
           while (it.hasNext()) {
               ArrayList aTargetChildren = (ArrayList) it.next();
               if (aTargetChildren != null) {
                   for (int i = 0; i < aTargetChildren.size(); i++) {
                       ObjectNode child = (ObjectNode) aTargetChildren.get(i);
                       child.unChange();
                   }
               }
           }
       }
  
       reset();
   }

    /**
     * Sets the value of a field, using the field name to identify the field.
     * <p>
     * @param name The name of the field whose value is being set.
     * @param value The new value for the given field.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while changing the
     * value of the field.
     * @include
     */
   public void setValue(String name, Object value) throws ObjectException {
       try {



       	if (!(value == null && getValue(name) == null)
       			&& !(value != null && getValue(name) != null && getValue(name).equals(value))) {

       		
       		if ( isKeyType(name) && mParent != null ) {
       			mParent.removeChildIndex(this);
       		}
       	

       		ObjectField field = getField(name);

       		TransactionLog log = new TransactionLog();
       		log.setPath(name);
       		log.setFunction(TransactionLog.FUNCTION_UPDATE);
       		log.setValue(field.getValue());

       		if (mFieldUpdateLogs == null || !mFieldUpdateLogs.contains(log)) {
       			if (null == mFieldUpdateLogs) {
       				mFieldUpdateLogs = new ArrayList();
       			}
       			mFieldUpdateLogs.add(log);
       		}

       		field.setValue(value);
       		setUpdateFlag(true);
       		setNull(name, (value != null) ? false : true);

       		if (isKeyType(name)) {
       			setKeyChangeFlag(true);
       		}

           	if ( isKeyType(name) && mParent != null ) {
       			mParent.createChildIndex(this,false);
       		}
  
       	}
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * sets if a field is visible
    *
    * @param name field name
    * @param flag boolean flag
    * @exception ObjectException ObjectException
    */
   public void setVisible(String name, boolean flag) throws ObjectException {
       try {
           setFlag(name, FieldFlag.VISIBLETYPE, flag);
       } catch (ObjectException e) {
           throw e;
       }
   }


    /**
     * Adds a new child object to the object node. This method checks for duplicates
     * before adding the new object and throws an exception if a duplicate object is
     * found. <b>addChild</b> sets the ADD flag for the child object to <b>true</b> and
     * sets the REMOVE flag to <b>false</b> only if no matching child objects are found.
     * <p>
     * @param child The child object to add.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while adding the
     * child object.
     * @include
     */
   public void addChild(ObjectNode child) throws ObjectException {
       try {
           if (child != null) {
               ObjectKey objKey = child.pGetKey();
               if (objKey == null) { // non-keyed children
                   addChildToTypeArrayList(child);
                   child.setRemoveFlag(false);
                   child.setAddFlag(true);
                   child.setParent(this);
               } else { // keyed children
                   ObjectNode node = getChild(child.pGetTag(), objKey, child.isRemoved());

                   if ((null == node) || node.isRemoved()) {
                       addChildToTypeArrayList(child);
                       child.setAddFlag(true);
                       child.setParent(this);

                   } else {
                       throw new DuplicateChildKeyException("A child object with the same key already exists: "
                       + objKey);
                   }
               }
           }
       } catch (Exception e) {
           throw new InvalidObjectException(e.getMessage());
       }
   }


   /**
    * adds a new child to the ObjectNode without checking on duplicates
    *
    * @param child The feature to be added to the Child attribute
    * @exception ObjectException ObjectException
    */
   public void addChildHard(ObjectNode child) throws ObjectException {
       if (child != null) {
           addChildToTypeArrayList(child);

           child.setRemoveFlag(false);
           child.setAddFlag(true);
           child.setParent(this);
       }
   }



   /**
    * adds a new child to the ObjectNode, and not to set flags
    *
    * @param child The feature to be added to the Child attribute
    * @exception ObjectException ObjectException
    */
   public void addChildNoFlagSet(ObjectNode child) throws ObjectException {
       try {
           if (child != null) {
               ObjectKey objKey = child.pGetKey();

               ObjectNode node = getChild(child.pGetTag(), objKey, child.isRemoved());

               if (objKey == null) {
                   addChildToTypeArrayList(child);
                   child.setParent(this);
               } else {
                   if ((null == node) || node.isRemoved()) {
                       addChildToTypeArrayList(child);
                       child.setParent(this);
                   } else {
                       throw new DuplicateChildKeyException("a child object of the same key already exists: "
                       + objKey);
                   }
               }
           }
       } catch (ObjectException e) {
           throw new InvalidObjectException(e.getMessage());
       }
   }


   /**
    * update a child to the ObjectNode. if the child exists in its parent
    * an update is executed, or an insert is executed
    *
    * @param child The feature to be added to the Child attribute
    * @exception ObjectException ObjectException
    */
   public void updateChild(ObjectNode child) throws ObjectException {
       try {
           ObjectKey objKey = child.pGetKey();
           ObjectNode node = getChild(child.pGetTag(), objKey);

           if (objKey == null) {
               addChildToTypeArrayList(child);
               child.setAddFlag(true);
           } else {
               if (null == node) {
                   addChildToTypeArrayList(child);
                   child.setAddFlag(true);
               } else {
                   node.updateIfChanged(child, true, true);
               }
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * adds a list of children objects
    *
    * @param children ArrayList of ObjectNode(s)
    * @exception ObjectException ObjectException
    */
   public void addChildren(ArrayList children) throws ObjectException {
       try {
           if (null != children) {
               for (int i = 0; i < children.size(); i++) {
                   ObjectNode node = (ObjectNode) children.get(i);
                   addChild(node.copy());
                   setUpdateFlag(true);
               }
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * ObjectNode deep copy
    *
    * @exception ObjectException ObjectException
    * @return ObjectNode a copy of itself
    */
   public ObjectNode copy() throws ObjectException {
       ObjectNode node = null;

       try {
           String tag = pGetTag();
           ArrayList names = pGetFieldNames();
           ArrayList types = getFieldTypes();
           ArrayList values = pGetFieldValues();
           node = new ObjectNode(tag, names, types, values);

           for (int i = 0; i < names.size(); i++) {
               String name = (String) names.get(i);
               node.setVisible(name, isVisible(name));
               node.setSearched(name, isSearched(name));
               node.setChanged(name, isChanged(name));
               node.setKeyType(name, isKeyType(name));
               node.setFieldReadAccess(name, hasFieldReadAccess(name));
               node.setFieldUpdateAccess(name, hasFieldUpdateAccess(name));
           }

           node.setRemoveFlag(isRemoved());
           node.setAddFlag(isAdded());
           node.setUpdateFlag(isUpdated());
           node.setKeyChangeFlag ( isKeyChanged() );

           if (null != mParent) {
               node.setParent(mParent);
           }

           ArrayList aChildren = getAllChildrenFromHashMap();
           if (null != aChildren) {
               for (int i = 0; i < aChildren.size(); i++) {
                   ObjectNode child = (ObjectNode) aChildren.get(i);
                   node.addChildNoFlagSet(child.copy());
               }
           }
       } catch (ObjectException e) {
           throw e;
       }

       return node;
   }

   /**
    * ObjectNode structural copy
    *
    * @exception ObjectException ObjectException
    * @return ObjectNode a copy of itself
    */
   public ObjectNode structCopy() throws ObjectException {
       ObjectNode node = null;

       try {
           String tag = pGetTag();
           ArrayList names = pGetFieldNames();
           ArrayList types = getFieldTypes();
           node = new ObjectNode(tag, names, types);

           if (null != mParent) {
               node.setParent(mParent);
           }

           ArrayList aChildren = getAllChildrenFromHashMap();
           if (null != aChildren) {
               for (int i = 0; i < aChildren.size(); i++) {
                   ObjectNode child = (ObjectNode) aChildren.get(i);
                   node.addChild(child.structCopy());
               }
           }
       } catch (ObjectException e) {
           throw e;
       }

       return node;
   }

    /**
     * Marks a child object for deletion, identifying the child object by its
     * object tag and key field settings.
     * <p>
     * @param type The type of child object to mark for deletion.
     * @param key The key field of the child object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while marking the
     * object for deletion.
     * @include
     */
   public void deleteChild(String type, SuperKey key)
   throws ObjectException {
       if (key == null) {
           return;
       }
       try {
           ArrayList aTargetChildren = getChildrenForType(type, false);
           if (null != aTargetChildren) {
               for (int i = 0; i < aTargetChildren.size(); i++) {
                   ObjectNode node = (ObjectNode) aTargetChildren.get(i);

                   if (node.pGetSuperKey() != null &&
                       node.pGetSuperKey().equals(key)) {
                       node.setRemoveFlag(true);
                       setUpdateFlag(true);
                       
                   }
               }
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Marks a child object for deletion, identifying the child object by its
     * object tag and object ID.
     * <p>
     * @param type The type of child object to mark for deletion.
     * @param objectId The object ID of the child object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while marking the
     * object for deletion.
     * @include
     */
   public void deleteChild(String type, String objectId)
   throws ObjectException {
       try {
           if (objectId == null) {
               return;
           }

           ArrayList aTargetChildren = getChildrenForType(type, false);
           if (aTargetChildren != null) {
               for (int i=0; i<aTargetChildren.size(); i++) {
                   ObjectNode node = (ObjectNode) aTargetChildren.get(i);

                   if (node.getObjectId() != null && node.getObjectId().equals(objectId)) {
                       node.setRemoveFlag(true);
                       setUpdateFlag(true);
                       
                   }
               }
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

    /**
     * Marks a child object for deletion.
     * <p>
     * @param node An ObjectNode object containing the child object to delete.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while marking the
     * object for deletion.
     * @include
     */
   public void deleteChild(ObjectNode node) throws ObjectException {
       try {
           SuperKey key = node.pGetSuperKey();
           String type = node.pGetTag();

           if (key != null) {
               deleteChild(type, key);
           } else {
               ArrayList aTargetChildren = getChildrenForType(type, false);
               if (null != aTargetChildren) {
                   for (int i = 0; i < aTargetChildren.size(); i++) {
                       ObjectNode n = (ObjectNode) aTargetChildren.get(i);

                       if (n.equals(node)) {
                           n.setRemoveFlag(true);
                           setUpdateFlag(true);
                       }
                   }
               }
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * checks if itself equals to input ObjectNode
    *
    * @param node ObjectNode
    * @exception ObjectException ObjectException
    * @return boolean
    */
   public boolean equals(ObjectNode node) throws ObjectException {
       boolean bret = true;

       try {
           if (node.pGetTag().equals(pGetTag())) {
               ArrayList names = pGetFieldNames();

               for (int i = 0; i < names.size(); i++) {
                   String name = (String) names.get(i);
                   ObjectField field1 = getValueObject(name);
                   ObjectField field2 = node.getValueObject(name);

                   if (!ObjectField.equals(field1, field2)) {
                       bret = false;
                       break;
                   }

               }

               ArrayList children1 = pGetChildren();
               ArrayList children2 = node.pGetChildren();

               if (!((children1 == null) && (children2 == null))) {
                   if ((children1 != null) && (children2 != null)) {
                       if (children1.size() != children2.size()) {
                           bret = false;
                       } else if (!children1.containsAll(children2)) {
                           bret = false;
                       }
                   }
               }
           } else {
               bret = false;
           }
       } catch (ObjectException e) {
           throw e;
       }

       return bret;
   }
   /**
    * checks if itself equals to input Object. 
    *
    * @param node Object
    * @return boolean
    */
   public boolean equals(Object node) {
       try{
           return equals((ObjectNode)node);
       }catch (ObjectException e){ 
           mLogger.error(e.getMessage(), e);
           return false;
       }
   }
   
   /**
    * hash code
    * @return int hash code
    */
   public int hashCode() {
       return super.hashCode();
   }


   /**
    * Removes a child object from the ObjectNode
    *
    * @param node ObjectNode child object
    */
   public void removeChild(ObjectNode node) {
       ArrayList aTargetChildren = getChildrenForType(node.pGetTag(), false);
       if (null != aTargetChildren) {
       	 	try {
       	 		//removeAndReCalculateIndex(aTargetChildren,node);
       	 		removeChildForType(aTargetChildren,node);
       	 		recalculateIndex();
       	 	}
       	 	catch ( ObjectException e ) {
       	   		mLogger.error(e.getMessage(), e);
             }
       }
   }

   /**
    * Removes all child objects from the ObjectNode
    *
    */
   public void removeChildren() {
       if (null != mChildrenHashMap) {
           Iterator it = mChildrenHashMap.values().iterator();
           while (it.hasNext()) {
               ArrayList aTargetChildren = (ArrayList) it.next();
               
               try {
               
               clearChildrenForType(aTargetChildren);
               recalculateIndex();               }
               catch ( ObjectException e ) {
           	   		mLogger.error(e.getMessage(), e);
               }
           }
       }
   }

   /**
    * Removes all child objects of one type from the ObjectNode
    *
    * @param type object type
    */
   public void removeChildren(String type) {
       ArrayList aTargetChildren = getChildrenForType(type, false);
       if (aTargetChildren != null) {
           
       	   try {
       	   		
       	   		clearChildrenForType(aTargetChildren);
       	   		recalculateIndex();
       		}
           	catch ( ObjectException e ) {
       	   		mLogger.error(e.getMessage(), e);
           	}
       }
   }

   /**
    * removes a child object from the ObjectNode by child's object tag and key
    * setting
    *
    * @param type object tag
    * @param key ObjectKey
    * @exception ObjectException ObjectException
    */
   public void removeChild(String type, ObjectKey objKey)
   throws ObjectException {
       try {
           ArrayList targetChildren = getChildrenForType(type, false);

           if (targetChildren != null) {
               for (int i=0; i<targetChildren.size(); i++) {
                   ObjectNode node = (ObjectNode) targetChildren.get(i);

                   if (objKey != null) {
                       if (node != null && node.pGetKey().equals(objKey)) {
                           
                           removeChildForType(targetChildren,node);
                           i--;
                       }
                   } else {
                       if (node.pGetTag().equals(type)) {
                       	
                       	removeChildForType(targetChildren,node);
                       	i--;
                       }
                   }
               }
               recalculateIndex();
           }
       } catch (Exception e) {
           throw new com.sun.mdm.index.objects.exception.InvalidObjectException();
       }
   }

   /**
    * resets all control flags on the ObjectNode
    */
   public void reset() {
       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();
               field.setChanged(false);
           }
       }

       mObjectFlag = 0;
       mObjectAccessFlag = mObjectReadAccessMask + mObjectUpdateAccessMask
       + mObjectDeleteAccessMask + mObjectAddAccessMask;
       mFieldUpdateLogs = null;

   }


   /**
    * resets all control flags on the ObjectNode, including child(ren)
    */
   public void resetAll() {
       if (mFields != null) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               ObjectField field = (ObjectField) it.next();
               field.setChanged(false);
           }
       }

       mObjectFlag = 0;
       mObjectAccessFlag = mObjectReadAccessMask + mObjectUpdateAccessMask
       + mObjectDeleteAccessMask + mObjectAddAccessMask;
       mFieldUpdateLogs = null;

       if (mChildrenHashMap != null) {
           Iterator it = mChildrenHashMap.values().iterator();
           while (it.hasNext()) {
               ArrayList aList = (ArrayList) it.next();
               for (int i = 0; i < aList.size(); i++) {
                   ObjectNode child = (ObjectNode) aList.get(i);
                   child.resetAll();
               }
           }
       }
   }

   /**
    * toString
    *
    * @return String
    */
   public String toString() {
       String strRet = "[ Pos : " + mPosition + " ]" ;

   	   
       String r = (isRemoved()) ? "R" : "r";
       String u = (isUpdated()) ? "U" : "u";
       String a = (isAdded()) ? "A" : "a";
       String read = (hasObjectReadAccess()) ? "R" : "r";
       String update = (hasObjectUpdateAccess()) ? "U" : "u";
       String delete = (hasObjectDeleteAccess()) ? "D" : "d";
       String add = (hasObjectAddAccess()) ? "A" : "a";

       strRet += ("Object[" + r + u + a + "][" + read + update + delete + add + "]: " + mTag + "\n");

       if (null != mFieldUpdateLogs) {
           strRet += "    UPDATE LOGS\n";

           for (int i = 0; i < mFieldUpdateLogs.size(); i++) {
               TransactionLog log = (TransactionLog) mFieldUpdateLogs.get(i);
               strRet += (log + "\n");
           }
       }

       if (null != mFields) {
           Iterator it = mFields.values().iterator();

           while (it.hasNext()) {
               strRet += ((ObjectField) it.next()).toString();
           }
       }

       strRet += "\n\n";

       if (null != mParent) {
           strRet += "Parent:\n";
           strRet += (getParentTag() + "\n");
       }

       strRet += "\n";

       ArrayList aChildren = getAllChildrenFromHashMap();
       if (null != aChildren) {
           strRet += "Children:\n";

           for (int i = 0; i < aChildren.size(); i++) {
               ObjectNode node = (ObjectNode) aChildren.get(i);
               strRet += node.toString();
           }
       }

       strRet += "\n";
       return strRet;
   }

   /**
    * updates the ObjectNode by input object
    *
    * @param node ObjectNode
    * @param addNewChild add-new-child flag
    * @param delMissingChild delete-missing-child flag
    * @exception ObjectException ObjectException
    */
   public void updateIfChanged(ObjectNode node, boolean addNewChild, boolean delMissingChild)
   throws ObjectException {
       try {
           if (node.pGetTag().equals(pGetTag())) {
               ArrayList names = node.pGetFieldNames();

               for (int i = 0; i < names.size(); i++) {
                   String name = (String) names.get(i);

                   if (node.isChanged(name)) {
                       setValue(name, node.getValue(name));
                   }
               }

               /* if incoming there is self child node missing from
                * incoming children list, remove the child if
                * delMissingChild is true
                */
               ArrayList selfchildlist = pGetChildren();

               if (selfchildlist != null) {
                   for (int i = 0; i < selfchildlist.size(); i++) {
                       ObjectNode n = (ObjectNode) selfchildlist.get(i);
                       String t = n.pGetTag();
                       SuperKey k = n.pGetSuperKey();
                       ObjectNode n2 = node.getChild(t, k);

                       if (n2 == null) {
                           if (delMissingChild) {
                               deleteChild(t, k);
                           }
                       } else {
                           /* copy the remove flag only
                            * add and update are implicitly handled
                            */
                           n.setRemoveFlag(n2.isRemoved());
                       }
                   }
               }

               /* if incoming children list has new child, then add it if
                * addNewChild is true
                */
               ArrayList childList = node.pGetChildren();

               if (childList != null) {
                   for (int i = 0; i < childList.size(); i++) {
                       ObjectNode n = (ObjectNode) childList.get(i);
                       String t = n.pGetTag();
                       ObjectKey k = n.pGetKey();
                       ObjectNode n2 = getChild(t, k);

                       // If remove flag is set, do not add or update child.
                       if (!n.isRemoved()) {
                           if (n2 != null) {
                               n2.updateIfChanged(n, addNewChild,
                               delMissingChild);
                           } else {
                               if (addNewChild) {
                                   addChild(n.copy());
                               }
                           }
                       }
                   }
               }
           } else {
               throw new InvalidObjectException();
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   public void updateIfNotEqual(ObjectNode node, boolean addNewChild, boolean delMissingChild)
   throws ObjectException {
       try {
           if (node.pGetTag().equals(pGetTag())) {
               ArrayList names = node.pGetFieldNames();

               for (int i = 0; i < names.size(); i++) {
                   String name = (String) names.get(i);

                   
                       setValue(name, node.getValue(name));
                  
               }

               /* if incoming there is self child node missing from
                * incoming children list, remove the child if
                * delMissingChild is true
                */
               ArrayList selfchildlist = pGetChildren();

               if (selfchildlist != null) {
                   for (int i = 0; i < selfchildlist.size(); i++) {
                       ObjectNode n = (ObjectNode) selfchildlist.get(i);
                       String t = n.pGetTag();
                       SuperKey k = n.pGetSuperKey();
                       ObjectNode n2 = node.getChild(t, k);

                       if (n2 == null) {
                           if (delMissingChild) {
                               deleteChild(t, k);
                           }
                       } else {
                           /* copy the remove flag only
                            * add and update are implicitly handled
                            */
                           n.setRemoveFlag(n2.isRemoved());
                       }
                   }
               }

               /* if incoming children list has new child, then add it if
                * addNewChild is true
                */
               ArrayList childList = node.pGetChildren();

               if (childList != null) {
                   for (int i = 0; i < childList.size(); i++) {
                       ObjectNode n = (ObjectNode) childList.get(i);
                       String t = n.pGetTag();
                       ObjectKey k = n.pGetKey();
                       // If the child is unkeyed, the objectKey will be null.
                       // The superkey should be used in this situation.  Otherwise,
                       // getChild() will return the last child object of this type.
                       if (k == null) {
                           k = n.pGetSuperKey();
                       }
                       ObjectNode n2 = getChild(t, k);

                       // If remove flag is set, do not add or update child.
                       if (!n.isRemoved()) {
                           if (n2 != null) {
                               n2.updateIfNotEqual(n, addNewChild,
                               delMissingChild);
                           } else {
                               if (addNewChild) {
                                   addChild(n.copy());
                               }
                           }
                       }
                   }
               }
           } else {
               throw new InvalidObjectException();
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * updates the ObjectNode by input object
    *
    * @param node ObjectNode
    * @param addNewChild add-new-child flag
    * @param delMissingChild delete-missing-child flag
    * @exception ObjectException ObjectException
    */
   public void updateIfNotNull(ObjectNode node, boolean addNewChild,
   boolean delMissingChild) throws ObjectException {
       updateIfNotNull(node, addNewChild, delMissingChild, false);
   }

   /**
    * updates the ObjectNode by input object
    *
    * @param node ObjectNode
    * @param addNewChild add-new-child flag
    * @param delMissingChild delete-missing-child flag
    * @param copyFlags if set to true, will copy the flags: isRemoved, isAdded, isUpdated
    * @exception ObjectException ObjectException
    */
   public void updateIfNotNull(ObjectNode node, boolean addNewChild,
   boolean delMissingChild, boolean copyFlags) throws ObjectException {
       try {
           if (node.pGetTag().equals(pGetTag())) {
               ArrayList names = node.pGetFieldNames();

               for (int i = 0; i < names.size(); i++) {
                   String name = (String) names.get(i);

                   if (!node.isNull(name)) {
                       setValue(name, node.getValue(name));
                       // Set object flag to update mask
                       mObjectFlag = mUpdateMask;
                   }
               }

               /* if incoming there is self child node missing from
                * incoming children list, remove the child if
                * delMissingChild is true
                */
               ArrayList selfchildlist = pGetChildren();

               if (selfchildlist != null) {
                   for (int i = 0; i < selfchildlist.size(); i++) {
                       ObjectNode n = (ObjectNode) selfchildlist.get(i);
                       String t = n.pGetTag();
                       SuperKey k = n.pGetSuperKey();
                       boolean removed = n.isRemoved();
                       ObjectNode n2 = node.getChild(t, k, removed);

                       if (n2 == null) {
                           if (delMissingChild) {
                               deleteChild(t, k);
                           }
                       } else { // copy the flags
                           if (copyFlags) {
                               n.setRemoveFlag(n2.isRemoved());
                               n.setAddFlag(n2.isAdded());
                               n.setUpdateFlag(n2.isUpdated());
                           }
                       }
                   }
               }

               /* if incoming children list has new child, then add it if
                * addNewChild is true
                */
               ArrayList childList = node.pGetChildren();

               if (childList != null) {
                   for (int i = 0; i < childList.size(); i++) {
                       ObjectNode n = (ObjectNode) childList.get(i);
                       String t = n.pGetTag();
                       ObjectKey k = n.pGetKey();
                       boolean removed = n.isRemoved();
                       ObjectNode n2 = getChild(t, k, removed);

                       if (n2 != null) {
                           n2.updateIfNotNull(n, addNewChild,
                           delMissingChild);
                       } else {
                           if (addNewChild) {
                               addChild(n.copy());
                           }
                       }
                   }
               }
           } else {
               throw new InvalidObjectException();
           }
       } catch (ObjectException e) {
           throw e;
       }
   }


  /**
    * updates the ObjectNode by input object
    * copies all the fields of input objectNode to this ObjectNode,
    *    including nulls except the Id fields.
    * Currently used by SurvivorCalculator
    *
    * @param node ObjectNode
    * @param addNewChild add-new-child flag
    * @param delMissingChild delete-missing-child flag
    * @exception ObjectException ObjectException
    */
   public void update(ObjectNode node, boolean addNewChild,
   boolean delMissingChild) throws ObjectException {
       try {
           String idfield = node.pGetTag() + "Id";

           if (node.pGetTag().equals(pGetTag())) {
               ArrayList names = node.pGetFieldNames();

               for (int i = 0; i < names.size(); i++) {
                   String name = (String) names.get(i);
                    if (!name.equals(idfield)) {

                       setValue(name, node.getValue(name));
                   }
               }

               /* if incoming there is self child node missing from
                * incoming children list, remove the child if
                * delMissingChild is true
                */
               ArrayList selfchildlist = pGetChildren();

               if (selfchildlist != null) {
                   for (int i = 0; i < selfchildlist.size(); i++) {
                       ObjectNode n = (ObjectNode) selfchildlist.get(i);
                       String t = n.pGetTag();
                       SuperKey k = n.pGetSuperKey();
                       boolean removed = n.isRemoved();
                       ObjectNode n2 = node.getChild(t, k, removed);

                       if (n2 == null) {
                           if (delMissingChild) {
                               deleteChild(t, k);
                           }
                       } else { // copy the flags
                       }
                   }
               }

               /* if incoming children list has new child, then add it if
                * addNewChild is true
                */
               ArrayList childList = node.pGetChildren();

               if (childList != null) {
                   boolean addUnkeyedObject = false;
                   for (int i = 0; i < childList.size(); i++) {
                       ObjectNode n = (ObjectNode) childList.get(i);
                       String t = n.pGetTag();
                       ObjectKey k = n.pGetKey();
                       // If the child is unkeyed, the objectKey will be null.
                       // The superkey should be used in this situation.  Otherwise,
                       // getChild() will return the first child object of this type
                       // that has the same "removed" status
                       if (k == null) {
                           k = n.pGetSuperKey();
                           if (k == null) {
                               // If the SuperKey is null, the unkeyed child object
                               // has not been retrieved from the database.  Instead,
                               // it is in the memory, such as in the case of adding
                               // a SO to an existing EO.  In this situation, the
                               // unkeyed child object should be added.
                               addUnkeyedObject = true;
                           }
                       }
                       boolean removed = n.isRemoved();
                       ObjectNode n2 = getChild(t, k, removed);

                       if (n2 != null && !addUnkeyedObject) {
                           n2.update(n, addNewChild,
                           delMissingChild);
                       } else {
                           addChild(n.copy());
                       }
                       addUnkeyedObject = false;
                   }
               }
           } else {
               throw new InvalidObjectException();
           }
       } catch (ObjectException e) {
           throw e;
       }
   }







   /**
    * returns String for objectnode without printing out field details
    * @return String non-flag string
    */
   public String pGetFlagString() {
       String strRet = "";
       String r = (isRemoved()) ? "R" : "r";
       String u = (isUpdated()) ? "U" : "u";
       String a = (isAdded()) ? "A" : "a";
       String read = (hasObjectReadAccess()) ? "R" : "r";
       String update = (hasObjectUpdateAccess()) ? "U" : "u";
       String delete = (hasObjectDeleteAccess()) ? "D" : "d";
       String add = (hasObjectAddAccess()) ? "A" : "a";

       strRet += ("Object[" + r + u + a + "][" + read + update + delete + add + "]: " + mTag + "\n");

       try {
           if (null != mFields) {
               Iterator it = mFields.values().iterator();

               while (it.hasNext()) {
                   ObjectField f = (ObjectField) it.next();
                   if (f.getFlag(FieldFlag.KEYTYPETYPE)) {
                       strRet += "        " + f.getName() + ": " + f.getValue() + "\n";
                   }
               }
           }
       } catch (ObjectException ex) {
           mLogger.error("ObjectException", ex);
       }

       ArrayList aChildren = getAllChildrenFromHashMap();
       if (null != aChildren) {
           for (int i = 0; i < aChildren.size(); i++) {
               ObjectNode node = (ObjectNode) aChildren.get(i);
               strRet += node.pGetFlagString();
           }
       }
       strRet += "\n";
       return strRet;
   }

   private void stringWriter(ObjectOutput out, String str) throws java.io.IOException {
       out.writeObject(str);
   }

   private String stringReader(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
       return (String) in.readObject();
   }

   private void objectWriter(ObjectOutput out, Object obj) throws java.io.IOException {
       if (obj != null) {
           out.writeInt(1);
           out.writeObject(obj);
       } else {
           out.writeInt(0);
       }
   }

   private Object objectReader(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
       int f = in.readInt();
       return (f == 1) ? in.readObject() : null;
   }

   private final class ExternalizableVersion1 {
       public ExternalizableVersion1() {};

       void writeExternal(ObjectOutput out) throws java.io.IOException {
           objectWriter(out, mFieldUpdateLogs);
           objectWriter(out, mChildTags);
           ArrayList aChildren = getAllChildrenFromHashMap();
           objectWriter(out, aChildren);
           //  Write out the parent tag to reduce the size of the delta blobs.
           //  Previously, the entire parent was written out, which resulted in huge
           //  delta blobs.
           objectWriter(out, mParentTag);
           stringWriter(out, mParentTag);
           stringWriter(out, mTag);
           out.writeInt(mObjectFlag);
           out.writeInt(mObjectAccessFlag);
           out.writeInt(mFields.size());
           java.util.Iterator it = mFields.values().iterator();
           while(it.hasNext()) {
               ObjectField f = (ObjectField) it.next();
               out.writeInt(f.getFieldFlag().getFlagValue());
               stringWriter(out, f.getName());
               out.writeInt(f.getType());
               objectWriter(out, f.getValue());
           }
       }

       void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
           mFieldUpdateLogs = (ArrayList) objectReader(in);
           mChildTags = (ArrayList) objectReader(in);
           ArrayList aChildren = (ArrayList) objectReader(in);
           //  Read in the parent tag but ignore it to reduce the size of the delta blobs.
           //  This maintains backwards-compatibility.
           objectReader(in);
           mParent = null;
           mParentTag = stringReader(in);
           mTag = stringReader(in);
           mObjectFlag = in.readInt();
           mObjectAccessFlag = in.readInt();
           
           int size = in.readInt();
           
            if ( mFields == null )  {
                mFields = new HashMap(size);
            }
           
            for (int i =0; i < size; i++) {
                int f = in.readInt();
                String n = stringReader(in);
                int t = in.readInt();
                Object v = objectReader(in);
                try {
                    ObjectField obj = new ObjectField(n, t, v, f);
                    mFields.put(n, obj);
                } catch (ObjectException ex) {
                    String msg= ex.getMessage();
                    if (msg != null)  {
                        if (msg.indexOf("local class incompatible:") == -1) {
                            mLogger.error("IOException", ex);
                        }
                    }
                    throw new java.io.IOException(ex.getMessage());
                }
           }
           // Split up children into HashMap
           if (aChildren != null) {
               for (int i = 0; i < aChildren.size(); i++) {
                   ObjectNode child = (ObjectNode) aChildren.get(i);
                   addChildToTypeArrayList(child);
                   child.setParent(selfRef);
               }
           }
       }
   }


    private final class ExternalizableVersion2 {
        public ExternalizableVersion2() {};

        void writeExternal(ObjectOutput out) throws java.io.IOException {
            objectWriter(out, mFieldUpdateLogs);
            objectWriter(out, mChildTags);
            objectWriter(out, null); // null, use writeChildren
            //  Write out the parent tag to reduce the size of the delta blobs.
            //  Previously, the entire parent was written out, which resulted in huge
            //  delta blobs.
            objectWriter(out, mParentTag);
            stringWriter(out, mParentTag);
            stringWriter(out, mTag);
            out.writeInt(mObjectFlag);
            out.writeInt(mObjectAccessFlag);
            out.writeInt(mFields.size());
            java.util.Iterator it = mFields.values().iterator();
            while(it.hasNext()) {
                ObjectField f = (ObjectField) it.next();
                out.writeInt(f.getFieldFlag().getFlagValue());
                stringWriter(out, f.getName());
                out.writeInt(f.getType());
                objectWriter(out, f.getValue());
            }
            writeChildren(out);
        }

        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mFieldUpdateLogs = (ArrayList) objectReader(in);
            mChildTags = (ArrayList) objectReader(in);
            ArrayList aChildren = (ArrayList) objectReader(in); // null, use readChildren()
            //  Read in the parent tag but ignore it to reduce the size of the delta blobs.
            //  This maintains backwards-compatibility.
            objectReader(in);
            mParent = null;
            mParentTag = stringReader(in);
            mTag = stringReader(in);
            mObjectFlag = in.readInt();
            mObjectAccessFlag = in.readInt();
            int size = in.readInt();

            if ( mFields == null )  {
                mFields = new HashMap(size);
            }

            for (int i = 0; i < size; i++) {
                int f = in.readInt();
                String n = stringReader(in);
                int t = in.readInt();
                Object v = objectReader(in);
                try {
                    ObjectField obj = new ObjectField(n, t, v, f);
                    mFields.put(n, obj);
                } catch (ObjectException ex) {
                    String msg= ex.getMessage();
                    if (msg != null)  {
                        if (msg.indexOf("local class incompatible:") == -1)
                            mLogger.error("IOException", ex);
                    }
                    throw new java.io.IOException(ex.getMessage());
                }
            }
            readChildren(in);
        }

        private void writeChildren(ObjectOutput out) throws java.io.IOException {
            int size = 0;
            // All Children
            if (mChildrenHashMap != null && mChildrenHashMap.size() > 0) {
                size = mChildrenHashMap.size();
                out.writeInt(size);
                Iterator it = mChildrenHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    stringWriter(out, key);
                    ArrayList aTargetChildren = (ArrayList) mChildrenHashMap.get(key);
                    objectWriter(out, aTargetChildren);
                }
            } else {
                out.writeInt(size);
            }
            // indexMap
            if (indexMap != null) {
                out.writeInt(indexMap.size());
                java.util.Iterator it = indexMap.keySet().iterator();
                while(it.hasNext()) {
                    String key = (String) it.next();
                    stringWriter(out, key);
                    Object index = indexMap.get(key);
                    objectWriter(out, index);
                }
            } else {
                out.writeInt(0);
            }
        }

        private void readChildren(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            try {
                int size = in.readInt();
                // All Children
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        String type = stringReader(in);
                        ArrayList aTargetChildren = (ArrayList) objectReader(in);
                        if (aTargetChildren != null) {
                            ArrayList aNewTargetChildren = getChildrenForType(type, true);

                            if ( aNewTargetChildren != null ) {
                            	for (int j = 0; j < aTargetChildren.size(); j++ ) {
                            		ObjectNode child = (ObjectNode)aTargetChildren.get(j);
                            		child.setParent(selfRef);
                            	}
                            	aNewTargetChildren.addAll(aTargetChildren);
                            }

                        }
                    }
                }
                // indexMap
                indexMap.clear();
                size = in.readInt();
                for (int i = 0; i < size; i++) {
                    String key = stringReader(in);
                    Integer index = (Integer) objectReader(in);
                    indexMap.put(key, index);
                }
            } catch (java.io.IOException ex) {
                String msg= ex.getMessage();
                if (msg != null)  {
                    if (msg.indexOf("local class incompatible:") == -1)
                           mLogger.error("IOException", ex);
                }
                throw new java.io.IOException(ex.getMessage());
            }
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion2 ev = new ExternalizableVersion2();
        out.writeInt(mVersion);
        ev.writeExternal(out);
    }

    public void readExternal(ObjectInput in)
	throws IOException, java.lang.ClassNotFoundException
    {
        int version = in.readInt();
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        }
        if (version == 2) {
            ExternalizableVersion2 ev = new ExternalizableVersion2();
            ev.readExternal(in);
        }
    }

   public ArrayList getAllChildrenFromHashMap() {
       ArrayList ret = null;
       if (mChildrenHashMap != null) {
           Iterator it = mChildrenHashMap.values().iterator();
           while (it.hasNext()) {
               ArrayList aList = (ArrayList) it.next();
               if (aList != null && aList.size() > 0) {
                   if (ret == null) {
                       ret = new ArrayList();
                   }
                   ret.addAll(aList);
               }
           }
       }
       return ret;
   }

   public ArrayList getChildrenForType(String type, boolean create) {
       ArrayList aList = null;
       if (mChildrenHashMap == null && create) {
           mChildrenHashMap = new HashMap();
       }
       if (mChildrenHashMap != null) {
           aList = (ArrayList) mChildrenHashMap.get(type);
       }
       if (aList == null && create) {
           aList = new ArrayList();
           mChildrenHashMap.put(type, aList);
       }
       return aList;
   }

   public void addChildToTypeArrayList(ObjectNode child) {
       ArrayList aTargetChildren = getChildrenForType(child.pGetType(), true);
       
       try {
       add(aTargetChildren, child);
       }
       catch ( Exception e) {
   	   		mLogger.error(e.getMessage(), e);
       }
   }

   protected void add(ArrayList targetArrayList, ObjectNode child) throws ObjectException {
   		child.mPosition = targetArrayList.size();
   		targetArrayList.add(child);
	    createChildIndex(child,true);
   }

   protected void createChildIndex(ObjectNode child,boolean overrideFlag ) throws ObjectException {

		ObjectKey key = child.pGetSuperKey();

		if ( key == null ) return;


   		if ( child.isKeyIndexable() ) {

  			ObjectKeyType objectKeyType = new ObjectKeyType(child.pGetTag(), key,child.isRemoved());
           if ( !overrideFlag ) {
           	Object object = indexMap.get(objectKeyType.toString());
           	if ( object != null ) {
           	   return;
           	}
           }
  			indexMap.put(objectKeyType.toString(), new Integer(child.mPosition));
  			child.mPartOfIndex = true;
  		}
  }

  	protected void removeChildIndex(ObjectNode child ) throws ObjectException {

  		ObjectKey key = child.pGetSuperKey();

  		if ( key == null ) return;

  		
			ObjectKeyType objectKeyType = new ObjectKeyType(child.mTag, key, child.isRemoved());
			indexMap.remove(objectKeyType.toString());
			child.mPartOfIndex = false;
  		
  	}

   

  	protected boolean isKeyIndexable() throws ObjectException {

    	ObjectKey key = pGetKey();
    	boolean isSuperKey = false;

    	if ( key == null ) {
    		key = pGetSuperKey();
    		if ( key == null ) return false;

    		if (getObjectId() != null) return true;
            return false;
    	}

    	return isRegularKeyIndexable();

  	}

  	protected boolean isRegularKeyIndexable() throws ObjectException {
		boolean indexable = false;

  		if (mFields != null) {
        	Iterator it = mFields.values().iterator();

        	while (it.hasNext()) {
            	ObjectField field = (ObjectField) it.next();

            	if (field.isKeyType()) {
            		indexable = true;
            		return indexable;
            	}
        	}
    	}
    	return indexable;
   	}

  	


   	private int min(Integer integer1, Integer integer2 ) {

   		if ( integer1 != null && integer2 != null ) {
   	   	    if ( integer1.intValue() > integer2.intValue() )  {
   	   	    	return integer2.intValue();
   	   	    }
   	   	    else  {
   	   	    	return integer1.intValue();
    		}
   		}
   	   	else if ( integer1 != null ) {
        	return integer1.intValue();
        }
        else if ( integer2 != null ) {
        	return integer2.intValue();
        }
        return -1;
   	}

   	private int max(Integer integer1, Integer integer2 ) {

   		if ( integer1 != null && integer2 != null ) {
   	   	    if ( integer1.intValue() > integer2.intValue() )  {
   	   	    	return integer1.intValue();
   	   	    }
   	   	    else  {
   	   	    	return integer2.intValue();
    		}
   		}
   	   	else if ( integer1 != null ) {
        	return integer1.intValue();
        }
        else if ( integer2 != null ) {
        	return integer2.intValue();
        }
        return -1;
   	}


 

   protected void removeChildForType ( ArrayList targetChildren, ObjectNode child )  throws ObjectException {
		targetChildren.remove(child);
   }

   protected void clearChildrenForType ( ArrayList targetChildren) throws ObjectException {
		targetChildren.clear();
   }

   protected void recalculateIndex() throws ObjectException {
    ArrayList ret = null;
    ObjectNode objectNode = null;
    ObjectKey objectKey = null;
    ObjectKeyType objectKeyType = null;
    Map idxMap = new HashMap();

    if (mChildrenHashMap != null) {
        Iterator it = mChildrenHashMap.values().iterator();
        while (it.hasNext()) {
            ArrayList aList = (ArrayList) it.next();
            if (aList != null ) {
            	for ( int i = 0; i < aList.size(); i ++ ) {
            		objectNode = (ObjectNode)aList.get(i);
            	    objectNode.mPosition = i;

            		ObjectKey key = objectNode.pGetSuperKey();
          			if ( key != null ) {
          				if ( objectNode.isKeyIndexable() ) {
              				objectKeyType = new ObjectKeyType(objectNode.mTag, key, objectNode.isRemoved());
             	    		if ( idxMap.get(objectKeyType.toString()) == null ) {
            	    		   idxMap.put(objectKeyType.toString(),new Integer(i));
             	    		}
            	    	}
            	    }
            	}
            }
        }
        this.indexMap = idxMap;
    }
   }

}
