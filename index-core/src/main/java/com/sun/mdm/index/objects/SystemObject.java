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
import com.sun.mdm.index.objects.exception.EmptyLocalIDException;
import com.sun.mdm.index.objects.exception.EmptyObjectException;
import com.sun.mdm.index.objects.exception.EmptySystemCodeException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Date;


/**
* The <b>SystemObject</b> class represents information sent to the master
* index from external systems. A system object is a component of an
* enterprise object, which is made up of one single best record (SBR) and
* one or more system objects.
* @author gzheng
* @version
*/
public class SystemObject extends ObjectNode {
   /**
    * Action Add
    */
   public static final String ACTION_ADD = "Add";
   /**
    * Action Update
    */
   public static final String ACTION_UPDATE = "Update";
   /**
    * Action Merge
    */
   public static final String ACTION_MERGE = "Merge";
   /**
    * Action UnMerge
    */
   public static final String ACTION_UNMERGE = "Unmerge";
   /**
    * Action Transfer
    */
   public static final String ACTION_TRANSFER = "Transfer";
   /**
    * Action Remove
    */
   public static final String ACTION_REMOVE = "Remove";
   /**
    * active status
    */
   public static final String STATUS_ACTIVE = "active";
   /**
    * inactive status
    */
   public static final String STATUS_INACTIVE = "inactive";
   /**
    * merged status
    */
   public static final String STATUS_MERGED = "merged";
   /**
    * field names
    */
   public static ArrayList mFieldNames;
   /**
    * field types
    */
   public static ArrayList mFieldTypes;

   // logger
   private final Logger mLogger = LogUtil.getLogger(this);

   static {
       mFieldNames = new ArrayList();
       mFieldNames.add("SystemCode");
       mFieldNames.add("LocalID");
       mFieldNames.add("ChildType");
       mFieldNames.add("CreateUser");
       mFieldNames.add("CreateFunction");
       mFieldNames.add("CreateDateTime");
       mFieldNames.add("UpdateUser");
       mFieldNames.add("UpdateFunction");
       mFieldNames.add("UpdateDateTime");
       mFieldNames.add("Status");
       mFieldTypes = new ArrayList();
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
       mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
   }

   /**
    * Creates a new instance of the SystemObject class.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject() throws ObjectException {
       super("SystemObject", mFieldNames, mFieldTypes);
       setKeyType("SystemCode", true);
       setKeyType("LocalID", true);
   }

   /**
    * Creates a new instance of the SystemObject by its object tag, field
    * names, and field types.
    * <p>
    * @param tag The object tag for the system object.
    * @param aux_name_list A list of field names in the system object.
    * @param aux_type_list A list of field types in the system object.
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject(String tag, ArrayList aux_name_list, ArrayList aux_type_list) throws ObjectException {
       super(tag, aux_name_list, aux_type_list);
   }

   /**
    * Creates a new instance of the SystemObject class.
    * <p>
    * @param system The processing code of the external system from
    * which the SystemObject information originated.
    * @param lid The local ID generated by the external system for the
    * system object.
    * @param childtype The type of object node contained in the
    * system object.
    * @param status The status of the system object. Possible values
    * are active, inactive, or merged.
    * @param createuser The login ID of the user who created the system
    * object.
    * @param createfunction The type of transaction that created the
    * system object.
    * @param createdatetime The date and time the system object was
    * created.
    * @param updateuser The login ID of the user who updated the system
    * object.
    * @param updatefunction The type of transaction that caused the
    * system object to be updated.
    * @param updatedatetime The date and time the system object was
    * last updated.
    * @param obj An ObjectNode object that contains the data for the
    * system object.
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject(String system, String lid, String childtype,
       String status,
       String createuser, String createfunction, Date createdatetime,
       String updateuser, String updatefunction, Date updatedatetime,
       ObjectNode obj) throws ObjectException {
       super("SystemObject", mFieldNames, mFieldTypes);
       setKeyType("SystemCode", true);
       setKeyType("LocalID", true);

       if ((null == system) || system.equals("")) {
           throw new EmptySystemCodeException();
       }

       if ((null == lid) || lid.equals("")) {
           throw new EmptyLocalIDException();
       }

       if (null == obj) {
           throw new EmptyObjectException();
       }

       setValue("SystemCode", system);
       setValue("LocalID", lid);
       setValue("ChildType", childtype);
       setValue("CreateUser", createuser);
       setValue("CreateFunction", createfunction);
       setValue("CreateDateTime", createdatetime);
       setValue("UpdateUser", updateuser);
       setValue("UpdateFunction", updatefunction);
       setValue("UpdateDateTime", updatedatetime);
       setValue("Status", status);
       addChild(obj);
       setKeyType("SystemCode", true);
       setKeyType("LocalID", true);
       setKeyType("ChildType", false);
       setKeyType("CreateUser", false);
       setKeyType("CreateFunction", false);
       setKeyType("CreateDateTime", false);
       setKeyType("UpdateUser", false);
       setKeyType("UpdateFunction", false);
       setKeyType("UpdateDateTime", false);
       setKeyType("Status", false);
       setNullable("SystemCode", false);
       setNullable("LocalID", false);
       setNullable("ChildType", false);
       setNullable("CreateUser", true);
       setNullable("CreateFunction", true);
       setNullable("CreateDateTime", true);
       setNullable("UpdateUser", true);
       setNullable("UpdateFunction", true);
       setNullable("UpdateDateTime", true);
       setNullable("Status", true);
       reset();
   }


   /**
    * Creates a new instance of the SystemObject class.
    * <p>
    * @param tag The object tag of the system object.
    * @param system The processing code of the external system from
    * which the SystemObject information originated.
    * @param lid The local ID generated by the external system for the
    * system object.
    * @param childtype The type of object node contained in the
    * system object.
    * @param status The status of the system object. Possible values
    * are active, inactive, or merged.
    * @param createuser The logon ID of the user who created the system
    * object.
    * @param createfunction The type of transaction that created the
    * system object.
    * @param createdatetime The date and time the system object was
    * created.
    * @param updateuser The logon ID of the user who updated the system
    * object.
    * @param updatefunction The type of transaction that caused the
    * system object to be updated.
    * @param updatedatetime The date and time the system object was
    * last updated.
    * @param obj An ObjectNode object that contains the data for the
    * system object.
    * @param aux_name_list A list of field names in the system object.
    * @param aux_type_list A list of field types in the system object.
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject(String tag, String system, String lid, String childtype,
       String status,
       String createuser, String createfunction, Date createdatetime,
       String updateuser, String updatefunction, Date updatedatetime,
       ObjectNode obj, ArrayList aux_name_list, ArrayList aux_type_list) throws ObjectException {
       super(tag, aux_name_list, aux_type_list);

       if ((null == system) || system.equals("")) {
           throw new EmptySystemCodeException();
       }

       if ((null == lid) || lid.equals("")) {
           throw new EmptyLocalIDException();
       }

       if (null == obj) {
           throw new EmptyObjectException();
       }

       setValue("SystemCode", system);
       setValue("LocalID", lid);
       setValue("ChildType", childtype);
       setValue("CreateUser", createuser);
       setValue("CreateFunction", createfunction);
       setValue("CreateDateTime", createdatetime);
       setValue("UpdateUser", updateuser);
       setValue("UpdateFunction", updatefunction);
       setValue("UpdateDateTime", updatedatetime);
       setValue("Status", status);
       addChild(obj);
       setKeyType("SystemCode", false);
       setKeyType("LocalID", false);
       setKeyType("ChildType", false);
       setKeyType("CreateUser", false);
       setKeyType("CreateFunction", false);
       setKeyType("CreateDateTime", false);
       setKeyType("UpdateUser", false);
       setKeyType("UpdateFunction", false);
       setKeyType("UpdateDateTime", false);
       setKeyType("Status", false);
       setNullable("SystemCode", false);
       setNullable("LocalID", false);
       setNullable("ChildType", false);
       setNullable("CreateUser", true);
       setNullable("CreateFunction", true);
       setNullable("CreateDateTime", true);
       setNullable("UpdateUser", true);
       setNullable("UpdateFunction", true);
       setNullable("UpdateDateTime", true);
       setNullable("Status", true);
       reset();
   }


   /**
    * Creates a new instance of the SystemObject class. This
    * constructor is for internal use only.
    * <p>
    * @param system The processing code of the external system from
    * which the SystemObject information originated.
    * @param lid The local ID generated by the external system for the
    * system object.
    * @param childtype The type of object node contained in the
    * system object.
    * @param status The status of the system object. Possible values
    * are active, inactive, or merged.
    * @param createuser The logon ID of the user who created the system
    * object.
    * @param createfunction The type of transaction that created the
    * system object.
    * @param createdatetime The date and time the system object was
    * created.
    * @param updateuser The logon ID of the user who updated the system
    * object.
    * @param updatefunction The type of transaction that caused the
    * system object to be updated.
    * @param updatedatetime The date and time the system object was
    * last updated.
    * @param obj An ObjectNode object that contains the data for the
    * system object.
    * @param flag This parameter is used internally only.
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject(String system, String lid, String childtype,
       String status,
       String createuser, String createfunction, Date createdatetime,
       String updateuser, String updatefunction, Date updatedatetime,
       ObjectNode obj, boolean flag) throws ObjectException {
       super("SystemObject", mFieldNames, mFieldTypes);
       setKeyType("SystemCode", true);
       setKeyType("LocalID", true);

       if ((null == system) || system.equals("")) {
           throw new EmptySystemCodeException();
       }

       if ((null == lid) || lid.equals("")) {
           throw new EmptyLocalIDException();
       }

       if (null == obj) {
           throw new EmptyObjectException();
       }

       setValue("SystemCode", system);
       setValue("LocalID", lid);
       setValue("ChildType", childtype);
       setValue("CreateUser", createuser);
       setValue("CreateFunction", createfunction);
       setValue("CreateDateTime", createdatetime);
       setValue("UpdateUser", updateuser);
       setValue("UpdateFunction", updatefunction);
       setValue("UpdateDateTime", updatedatetime);
       setValue("Status", status);
       addChildNoFlagSet(obj);
       setKeyType("SystemCode", true);
       setKeyType("LocalID", true);
       setKeyType("ChildType", false);
       setKeyType("CreateUser", false);
       setKeyType("CreateFunction", false);
       setKeyType("CreateDateTime", false);
       setKeyType("UpdateUser", false);
       setKeyType("UpdateFunction", false);
       setKeyType("UpdateDateTime", false);
       setKeyType("Status", false);
       setNullable("SystemCode", false);
       setNullable("LocalID", false);
       setNullable("ChildType", false);
       setNullable("CreateUser", true);
       setNullable("CreateFunction", true);
       setNullable("CreateDateTime", true);
       setNullable("UpdateUser", true);
       setNullable("UpdateFunction", true);
       setNullable("UpdateDateTime", true);
       setNullable("Status", true);
       reset();
   }


   /**
    * Creates a new instance of the SystemObject class. This
    * constructor is for internal use only.
    * <p>
    * @param tag The object tag for the system object.
    * @param system The processing code of the external system from
    * which the SystemObject information originated.
    * @param lid The local ID generated by the external system for the
    * system object.
    * @param childtype The type of object node contained in the
    * system object.
    * @param status The status of the system object. Possible values
    * are active, inactive, or merged.
    * @param createuser The logon ID of the user who created the system
    * object.
    * @param createfunction The type of transaction that created the
    * system object.
    * @param createdatetime The date and time the system object was
    * created.
    * @param updateuser The logon ID of the user who updated the system
    * object.
    * @param updatefunction The type of transaction that caused the
    * system object to be updated.
    * @param updatedatetime The date and time the system object was
    * last updated.
    * @param obj An ObjectNode object that contains the data for the
    * system object.
    * @param flag This parameter is used internally only.
    * @param aux_name_list A list of field names in the system object.
    * @param aux_type_list A list of field types in the system object.
    * @exception ObjectException Thrown if an error occurs while creating
    * the system object.
    * @include
    */
   public SystemObject(String tag, String system, String lid, String childtype,
       String status,
       String createuser, String createfunction, Date createdatetime,
       String updateuser, String updatefunction, Date updatedatetime,
       ObjectNode obj, boolean flag, ArrayList aux_name_list, ArrayList aux_type_list) throws ObjectException {
       super(tag, aux_name_list, aux_type_list);

       if ((null == system) || system.equals("")) {
           throw new EmptySystemCodeException();
       }

       if ((null == lid) || lid.equals("")) {
           throw new EmptyLocalIDException();
       }

       if (null == obj) {
           throw new EmptyObjectException();
       }

       setValue("SystemCode", system);
       setValue("LocalID", lid);
       setValue("ChildType", childtype);
       setValue("CreateUser", createuser);
       setValue("CreateFunction", createfunction);
       setValue("CreateDateTime", createdatetime);
       setValue("UpdateUser", updateuser);
       setValue("UpdateFunction", updatefunction);
       setValue("UpdateDateTime", updatedatetime);
       setValue("Status", status);
       addChildNoFlagSet(obj);
       setKeyType("SystemCode", false);
       setKeyType("LocalID", false);
       setKeyType("ChildType", false);
       setKeyType("CreateUser", false);
       setKeyType("CreateFunction", false);
       setKeyType("CreateDateTime", false);
       setKeyType("UpdateUser", false);
       setKeyType("UpdateFunction", false);
       setKeyType("UpdateDateTime", false);
       setKeyType("Status", false);
       setNullable("SystemCode", false);
       setNullable("LocalID", false);
       setNullable("ChildType", false);
       setNullable("CreateUser", true);
       setNullable("CreateFunction", true);
       setNullable("CreateDateTime", true);
       setNullable("UpdateUser", true);
       setNullable("UpdateFunction", true);
       setNullable("UpdateDateTime", true);
       setNullable("Status", true);
       reset();
   }

   /**
    * Retrieves the child type value from an instance of SystemObject.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>childType</b> field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getChildType() throws ObjectException {
       try {
           return ((String) getValue("ChildType"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the date and time a system object was created.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>Date</CODE> - The value of the <b>createDateTime</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public Date getCreateDateTime() throws ObjectException {
       try {
           return ((Date) getValue("CreateDateTime"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the type of transaction that created a system object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>createFunction</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getCreateFunction() throws ObjectException {
       try {
           return ((String) getValue("CreateFunction"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves logon ID of the user who created a system object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>createUser</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getCreateUser() throws ObjectException {
       try {
           return ((String) getValue("CreateUser"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the local ID value from an instance of SystemObject.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>lid</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getLID() throws ObjectException {
       try {
           return ((String) getValue("LocalID"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the object node from an instance of SystemObject.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>ObjectNode</CODE> - An object containing the data in
    * a system object.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public ObjectNode getObject() {
       ArrayList list = pGetChildren();

       if (null != list && list.size() > 0) {
           ObjectNode node = (ObjectNode) list.get(0);

           return node;
       } else {
           return null;
       }
   }

   /**
    * Retrieves the status value from an instance of SystemObject.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>status</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getStatus() throws ObjectException {
       try {
           return ((String) getValue("Status"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the system code value from an instance of SystemObject.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>systemCode</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getSystemCode() throws ObjectException {
       try {
           return ((String) getValue("SystemCode"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the date and time a system object was last updated.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>Date</CODE> - The value of the <b>updateDateTime</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public Date getUpdateDateTime() throws ObjectException {
       try {
           return ((Date) getValue("UpdateDateTime"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves the type of transaction that updated a system object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>updateFunction</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getUpdateFunction() throws ObjectException {
       try {
           return ((String) getValue("UpdateFunction"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Retrieves logon ID of the user who updated a system object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>updateUser</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getUpdateUser() throws ObjectException {
       try {
           return ((String) getValue("UpdateUser"));
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>childType</b> field in an instance of
    * SystemObject.
    * <p>
    * @param childtype The child type (or object type) of the system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setChildType(String childtype) throws ObjectException {
       try {
           setValue("ChildType", childtype);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>createDateTime</b> field in an instance of
    * SystemObject.
    * <p>
    * @param createdatetime The date and time the system object was
    * created.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setCreateDateTime(Date createdatetime)
       throws ObjectException {
       try {
           setValue("CreateDateTime", createdatetime);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>createFunction</b> field in an instance of
    * SystemObject.
    * <p>
    * @param createfunction The type of transaction that created the
    * system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setCreateFunction(String createfunction)
       throws ObjectException {
       try {
           setValue("CreateFunction", createfunction);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>createUser</b> field in an instance of
    * SystemObject.
    * <p>
    * @param createuser The logon ID of the user who created the
    * system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setCreateUser(String createuser) throws ObjectException {
       try {
           setValue("CreateUser", createuser);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>lid</b> field in an instance of
    * SystemObject.
    * <p>
    * @param lid The local ID of the system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setLID(String lid) throws ObjectException {
       try {
           setValue("LocalID", lid);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the object node in an instance of SystemObject. You must call
    * setChildType prior to calling setObject.
    * <p>
    * @param obj The object node of the system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setObject(ObjectNode obj) throws ObjectException {
       if (getValue("ChildType").equals(obj.pGetTag())) {
           ObjectKey objKey = obj.pGetKey();
           String type = obj.pGetType();
           ArrayList aTargetChildren = getChildrenForType(type, true);
           if (aTargetChildren != null) {
               //##aTargetChildren.clear();
    		   try {
		   			//clearAndReCalculateIndex(aTargetChildren);
		   			clearChildrenForType(aTargetChildren);
		   			recalculateIndex();
    		   }
    		   catch ( ObjectException e ) {
           	       mLogger.error(e.getMessage(), e);
    		   }
           }

           //##aTargetChildren.add(obj);
           add(aTargetChildren,obj);
           obj.setParent(this);
       } else {
           throw new ObjectException("Child type '"
               + getValue("ChildType") + "' does not match with incoming object tag (" + obj.pGetTag() + ")");
       }
   }


   /**
    * Sets the value of the <b>status</b> field in an instance of
    * SystemObject.
    * <p>
    * @param status The status of the system object. Possible values are
    * active, inactive, or merged.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setStatus(String status) throws ObjectException {
       try {
           if (getStatus() != null && getStatus().equals(SystemObject.STATUS_MERGED)) {
               throw new ObjectException("Status of merged system object can "
               + "not be modified.");
           }
           if (status.equals(SystemObject.STATUS_ACTIVE)
           || status.equals(SystemObject.STATUS_INACTIVE)) {
               setValue("Status", status);
           } else if (status.equals(SystemObject.STATUS_MERGED)) {
               throw new ObjectException("Use MasterController merge functions "
               + "to merge a SystemObject.");
           } else {
               throw new ObjectException("Invalid status: " + status);
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>systemCode</b> field in an instance of
    * SystemObject.
    * <p>
    * @param system The processing code of the system that created
    * the system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setSystemCode(String system) throws ObjectException {
       try {
           setValue("SystemCode", system);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>updateDateTime</b> field in an instance of
    * SystemObject.
    * <p>
    * @param updatedatetime The date and time the system object was
    * last updated.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setUpdateDateTime(Date updatedatetime)
       throws ObjectException {
       try {
           setValue("UpdateDateTime", updatedatetime);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>updateFunction</b> field in an instance of
    * SystemObject.
    * <p>
    * @param updatefunction The type of transaction that updated the
    * system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setUpdateFunction(String updatefunction)
       throws ObjectException {
       try {
           setValue("UpdateFunction", updatefunction);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * Sets the value of the <b>updateUser</b> field in an instance of
    * SystemObject.
    * <p>
    * @param updateuser The logon ID of the user who last updated the
    * system object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setUpdateUser(String updateuser) throws ObjectException {
       try {
           setValue("UpdateUser", updateuser);
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * @exception ObjectException object exception
    * @return ObjectNode copy
    */
   public ObjectNode copy() throws ObjectException {
       SystemObject ret = null;

       try {
           if (getObject() == null) {
               ret = new SystemObject(getSystemCode(), getLID(),
                       getChildType(),
                       getStatus(), getCreateUser(),
                       getCreateFunction(), getCreateDateTime(),
                       getUpdateUser(), getUpdateFunction(),
                       getUpdateDateTime(), null);
           } else {
               ret = new SystemObject(getSystemCode(), getLID(),
                       getChildType(),
                       getStatus(), getCreateUser(),
                       getCreateFunction(), getCreateDateTime(),
                       getUpdateUser(), getUpdateFunction(),
                       getUpdateDateTime(), getObject().copy(), false);
           }

           ArrayList names = getFieldNames();

           for (int i = 0; i < names.size(); i++) {
               String name = (String) names.get(i);
               ret.setVisible(name, isVisible(name));
               ret.setSearched(name, isSearched(name));
               ret.setChanged(name, isChanged(name));
               ret.setKeyType(name, isKeyType(name));
           }

           ret.setUpdateFlag(isUpdated());
           ret.setRemoveFlag(isRemoved());
           ret.setAddFlag(isAdded());

           ArrayList fieldUpdateLogs = null;
           if ( pGetFieldUpdateLogs()!= null ){
               fieldUpdateLogs = (ArrayList)pGetFieldUpdateLogs().clone();
           }
           ret.setFieldUpdateLogs(fieldUpdateLogs);
       } catch (ObjectException e) {
           throw e;
       }
       return (ObjectNode) ret;
   }


   /**
    * get field names
    * @return ArrayList list
    */
   public ArrayList getFieldNames() {
       return mFieldNames;
   }


   /**
    * get field types
    * @return ArrayList list
    */
   public ArrayList getFieldTypes() {
       return mFieldTypes;
   }


   /**
    * @exception ObjectException object exception
    * @return ObjectNode structure copy
    */
   public ObjectNode structCopy() throws ObjectException {
       SystemObject ret = null;

       try {
           ret = new SystemObject(getSystemCode(), getLID(), getChildType(),
                   getStatus(),
                   getCreateUser(), getCreateFunction(), getCreateDateTime(),
                   getUpdateUser(), getUpdateFunction(), getUpdateDateTime(),
                   getObject().structCopy());

           ArrayList names = getFieldNames();

           for (int i = 0; i < names.size(); i++) {
               String name = (String) names.get(i);
               ret.setVisible(name, isVisible(name));
               ret.setSearched(name, isSearched(name));
               ret.setKeyType(name, isKeyType(name));
           }

           ObjectKey key = pGetKey();

           if (key != null) {
               ret.setKey(key);
           }
       } catch (ObjectException e) {
           throw e;
       }

       return (ObjectNode) ret;
   }

  

}
