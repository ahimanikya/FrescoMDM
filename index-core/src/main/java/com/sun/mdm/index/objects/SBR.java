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

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
* The <b>SBR</b> class represents the information about an entity that is
* considered to be the most accurate information from all external systems.
* This is known as the single best record, or SBR. An SBR object is a
* component of an enterprise object, which is made up of one SBR and
* one or more system objects.
* @author gzheng
* @version
*/
public class SBR extends SystemObject {

   private static ArrayList mAuxNameList = null;
   private static ArrayList mAuxTypeList = null;
   // logger
   private final Logger mLogger = LogUtil.getLogger(this);

   static {
       mAuxNameList = new ArrayList();
       mAuxNameList.add("SystemCode");
       mAuxNameList.add("LocalID");
       mAuxNameList.add("ChildType");
       mAuxNameList.add("CreateSystem");
       mAuxNameList.add("CreateUser");
       mAuxNameList.add("CreateFunction");
       mAuxNameList.add("CreateDateTime");
       mAuxNameList.add("UpdateSystem");
       mAuxNameList.add("UpdateUser");
       mAuxNameList.add("UpdateFunction");
       mAuxNameList.add("UpdateDateTime");
       mAuxNameList.add("Status");
       mAuxNameList.add("RevisionNumber");

       mAuxTypeList = new ArrayList();
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_DATE_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
       mAuxTypeList.add(new Integer(ObjectField.OBJECTMETA_INT_TYPE));
   }


   /**
    * Creates a new instance of the SBR class.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @exception ObjectException Thrown if an error occurs while creating
    * the SBR object.
    * @include
    */
   public SBR()
       throws ObjectException {
       super("SystemSBR", mAuxNameList, mAuxTypeList);
   }


   /**
    * Creates a new instance of the SBR class.
    * <p>
    * @param childtype The type of object node contained in the
    * SBR object.
    * @param createuser The logon ID of the user who created the SBR
    * object.
    * @param createsystem The processing code of the system that
    * originated the record that created the SBR.
    * @param createfunction The type of transaction that created the
    * SBR object.
    * @param createdatetime The date and time the SBR object was
    * created.
    * @param updateuser The logon ID of the user who updated the SBR
    * object.
    * @param updatesystem The processing code of the system that
    * last updated a record that caused the SBR to be recalculated.
    * @param updatefunction The type of transaction that caused the
    * SBR object to be recalculated.
    * @param updatedatetime The date and time the SBR object was
    * last recalculated.
    * @param status The status of the SBR.
    * @param obj An ObjectNode object that contains the data for the
    * SBR object.
    * @exception ObjectException Thrown if an error occurs while creating
    * the SBR object.
    * @include
    */
   public SBR(String childtype, String createuser,
           String createsystem, String createfunction, Date createdatetime, String updateuser,
           String updatesystem, String updatefunction, Date updatedatetime, String status, ObjectNode obj)
       throws ObjectException {
       super("SystemSBR", "SBR", "DummyLID", childtype, status,
               createuser, createfunction, createdatetime,
               updateuser, updatefunction, updatedatetime, obj, mAuxNameList, mAuxTypeList);
       if (status.equals(SystemObject.STATUS_ACTIVE) || status.equals(SystemObject.STATUS_INACTIVE)
       || status.equals(SystemObject.STATUS_MERGED)) {
           setValue("Status", status);
       } else {
           throw new ObjectException("SystemSBR: Unsupported status code: " + status);
       }
       setValue("CreateSystem", createsystem);
       setValue("UpdateSystem", updatesystem);
       setValue("RevisionNumber", new Integer(1));
       setKeyType("CreateSystem", false);
       setKeyType("UpdateSystem", false);
       setKeyType("RevisionNumber", false);
       setNullable("CreateSystem", true);
       setNullable("UpdateSystem", true);
       setNullable("RevisionNumber", false);
       setNullable("Status", false);
   }


   /**
    * Creates a new instance of the SBR class. This
    * constructor is for internal use only.
    * <p>
    * @param childtype The type of object node contained in the
    * SBR object.
    * @param createuser The logon ID of the user who created the SBR
    * object.
    * @param createsystem The processing code of the system that
    * originated the record that created the SBR.
    * @param createfunction The type of transaction that created the
    * SBR object.
    * @param createdatetime The date and time the SBR object was
    * created.
    * @param updateuser The login ID of the user who updated the SBR
    * object.
    * @param updatesystem The processing code of the system that
    * last updated a record that caused the SBR to be recalculated.
    * @param updatefunction The type of transaction that caused the
    * SBR object to be recalculated.
    * @param updatedatetime The date and time the SBR object was
    * last recalculated.
    * @param status The status of the SBR.
    * @param obj An ObjectNode object that contains the data for the
    * SBR object.
    * @param flag This parameter is only used internally.
    * @exception ObjectException Thrown if an error occurs while creating
    * the SBR object.
    * @include
    */
   public SBR(String childtype, String createuser,
           String createsystem, String createfunction, Date createdatetime, String updateuser,
           String updatesystem, String updatefunction, Date updatedatetime, String status, ObjectNode obj,
           boolean flag)
       throws ObjectException {
       super("SystemSBR", "SBR", "DummyLID", childtype, status,
               createuser, createfunction, createdatetime,
               updateuser, updatefunction, updatedatetime,
               obj, flag, mAuxNameList, mAuxTypeList);

       if (status.equals(SystemObject.STATUS_ACTIVE) || status.equals(SystemObject.STATUS_INACTIVE)
       || status.equals(SystemObject.STATUS_MERGED)) {
           setValue("Status", status);
       } else {
           throw new ObjectException("SystemSBR: Unsupported status code: " + status);
       }
       setValue("CreateSystem", createsystem);
       setValue("UpdateSystem", updatesystem);
       setValue("RevisionNumber", new Integer(1));
       setKeyType("CreateSystem", false);
       setKeyType("UpdateSystem", false);
       setKeyType("RevisionNumber", false);
       setNullable("CreateSystem", true);
       setNullable("UpdateSystem", true);
       setNullable("RevisionNumber", false);
       setNullable("Status", false);
   }


   /**
    * Retrieves the child type value from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>childType</b> field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getChildType()
       throws ObjectException {
       try {
           return ((String) getValue("ChildType"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the create date and time from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>Date</CODE> - The value of the <b>createDateTime</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public Date getCreateDateTime()
       throws ObjectException {
       try {
           return ((Date) getValue("CreateDateTime"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the transaction type from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>createFunction</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getCreateFunction()
       throws ObjectException {
       try {
           return ((String) getValue("CreateFunction"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the processing code of the system that originated the
    * record that created the SBR.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>createSystem</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getCreateSystem()
       throws ObjectException {
       try {
           return ((String) getValue("CreateSystem"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves logon ID of the user who created the SBR.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>createUser</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getCreateUser()
       throws ObjectException {
       try {
           return ((String) getValue("CreateUser"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the revision number from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>Integer</CODE> - The revision number of the SBR.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the revision number.
    * @include
    */
   public Integer getRevisionNumber()
       throws ObjectException {
       try {
           return ((Integer) getValue("RevisionNumber"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the object node from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>ObjectNode</CODE> - An object containing the data in
    * the SBR object.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public ObjectNode getObject() {
       try {
           ArrayList list = pGetChildren(getChildType());
           if (null != list && list.size() > 0) {
               ObjectNode node = (ObjectNode) list.get(0);
               return node;
           } else {
               return null;
           }
       } catch (ObjectException ex) {
           return null;
       }
   }


   /**
    * Retrieves an array of overwrite values from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>ArrayList</CODE> - An array of overwrite
    * values in the SBR.
    * <DT><B>Throws:</B><DD>None.
    * @include
    */
   public ArrayList getOverWrites() {
       return pGetChildren("SBROverWrite");
   }


   /**
    * Retrieves the date and time the SBR object was last updated.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>Date</CODE> - The value of the <b>updateDateTime</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public Date getUpdateDateTime()
       throws ObjectException {
       try {
           return ((Date) getValue("UpdateDateTime"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the type of transaction that updated the SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>updateFunction</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getUpdateFunction()
       throws ObjectException {
       try {
           return ((String) getValue("UpdateFunction"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves processing code of the system that last updated the SBR
    * object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>updateSystem</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getUpdateSystem()
       throws ObjectException {
       try {
           return ((String) getValue("UpdateSystem"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves logon ID of the user who updated the system object
    * that last caused the SBR to be recalculated.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>updateUser</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getUpdateUser()
       throws ObjectException {
       try {
           return ((String) getValue("UpdateUser"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Retrieves the status value from an SBR object.
    * <p>
    * <DL><DT><B>Parameters:</B><DD>None.</DL>
    * @return <CODE>String</CODE> - The value of the <b>status</b>
    * field.
    * @exception ObjectException Thrown if an error occurs while retrieving
    * the field value.
    * @include
    */
   public String getStatus()
       throws ObjectException {
       try {
           return ((String) getValue("Status"));
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>childType</b> field in an SBR object.
    * <p>
    * @param childtype The child type (or object type) of the SBR.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setChildType(String childtype)
       throws ObjectException {
       try {
           setValue("ChildType", childtype);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>createDateTime</b> field in an SBR object.
    * <p>
    * @param createdatetime The date and time the SBR was created.
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
    * Sets the value of the <b>createFunction</b> field in an SBR object.
    * <p>
    * @param createfunction The type of transaction that created the SBR.
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
    * Sets the value of the <b>createSystem</b> field in an SBR object.
    * <p>
    * @param createsystem The processing code of the system whose record
    * created the SBR.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setCreateSystem(String createsystem)
       throws ObjectException {
       try {
           setValue("CreateSystem", createsystem);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>createUser</b> field in an SBR object.
    * <p>
    * @param createuser The logon ID of the user who created the SBR.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setCreateUser(String createuser)
       throws ObjectException {
       try {
           setValue("CreateUser", createuser);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>revisionNumber</b> field in an SBR object.
    * <p>
    * @param revisionNumber The revision number for the SBR object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setRevisionNumber(int revisionNumber)
       throws ObjectException {
       try {
           setValue("RevisionNumber", new Integer(revisionNumber));
       } catch (ObjectException e) {
           throw e;
       }
   }

  /**
    * Sets the object node in an instance of SBR. You must call
    * setChildType prior to calling setObject.
    * <p>
    * @param obj The object node of the SBR object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setObject(ObjectNode obj)
       throws ObjectException {
       if (getValue("ChildType").equals(obj.pGetTag())) {
           ObjectKey objKey = obj.pGetKey();
           String type = obj.pGetType();
           ArrayList aTargetChildren = getChildrenForType(type, true);
           if (aTargetChildren != null) {
		   try {
		   		//clearAndReCalculateIndex(aTargetChildren);
		   		clearChildrenForType(aTargetChildren);
		   		recalculateIndex();
              }
              catch ( ObjectException e ) {
           	      mLogger.error(e.getMessage(), e);
              }

              
           }

           
           add(aTargetChildren,obj);
           obj.setParent(this);
       } else {
           throw new ObjectException("Child type '"
                   + getValue("ChildType") + "' does not match with incoming object tag (" + obj.pGetTag() + ")");
       }
   }

   /**
    * Sets the value of the <b>updateDateTime</b> field in an SBR object.
    * <p>
    * @param updatedatetime The date and time the SBR was last updated.
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
    * Sets the value of the <b>updateFunction</b> field in an SBR object.
    * <p>
    * @param updatefunction The type of transaction that updated the
    * SBR object.
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
    * Sets the value of the <b>updateSystem</b> field in an SBR object.
    * <p>
    * @param updatesystem The processing code of the system whose record
    * updated the SBR object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setUpdateSystem(String updatesystem)
       throws ObjectException {
       try {
           setValue("UpdateSystem", updatesystem);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>updateUser</b> field in an SBR object.
    * <p>
    * @param updateuser The login ID of the user who last updated the
    * SBR object.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setUpdateUser(String updateuser)
       throws ObjectException {
       try {
           setValue("UpdateUser", updateuser);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Sets the value of the <b>status</b> field in an SBR object.
    * <p>
    * @param status The status of the SBR object. Possible values are
    * active, inactive, or merged.
    * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
    * @exception ObjectException Thrown if an error occurs while setting
    * the field value.
    * @include
    */
   public void setStatus(String status)
       throws ObjectException {
       try {
           if (status.equals(SystemObject.STATUS_ACTIVE) || status.equals(SystemObject.STATUS_INACTIVE)
           || status.equals(SystemObject.STATUS_MERGED)) {
               setValue("Status", status);
           } else {
               throw new ObjectException("SystemSBR: Unsupported status code: " + status);
           }
       } catch (ObjectException e) {
           throw e;
       }
   }

   /**
    * get field names
    * @return ArrayList list
    */
   public ArrayList getFieldNames() {
       return mAuxNameList;
   }


   /**
    * get field types
    * @return ArrayList list
    */
   public ArrayList getFieldTypes() {
       return mAuxTypeList;
   }


   /**
    * @exception ObjectException object exception
    * @return ObjectNode copy
    */
   public ObjectNode copy()
       throws ObjectException {
       SBR ret = null;
       try {
           ret = new SBR(getChildType(), getCreateUser(), getCreateSystem(),
                   getCreateFunction(),
                   getCreateDateTime(), getUpdateUser(), getUpdateSystem(), getUpdateFunction(),
                   getUpdateDateTime(), getStatus(), getObject().copy(), false);
           ArrayList overwrite = this.getOverWrites();
           if (overwrite != null) {
               for (int i = 0; i < overwrite.size(); i++) {
                   SBROverWrite s = (SBROverWrite) ((SBROverWrite) overwrite.get(i)).copy();
                   boolean u = s.isUpdated();
                   boolean a = s.isAdded();
                   boolean r = s.isRemoved();
                   ret.addOverWrite(s);
                   s.setUpdateFlag(u);
                   s.setAddFlag(a);
                   s.setRemoveFlag(r);
               }
               
           }

           ret.setRevisionNumber(this.getRevisionNumber().intValue());
           ArrayList names = getFieldNames();
           for (int i = 0; i < names.size(); i++) {
               String name = (String) names.get(i);
               ret.setVisible(name, isVisible(name));
               ret.setSearched(name, isSearched(name));
               ret.setChanged(name, isChanged(name));
               ret.setKeyType(name, isKeyType(name));
           }

           ret.setUpdateFlag (isUpdated());
           ret.setRemoveFlag (isRemoved());
           ret.setAddFlag (isAdded());

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
    * @exception ObjectException object exception
    * @return ObjectNode structure copy
    */
   public ObjectNode structCopy()
       throws ObjectException {
       SBR ret = null;
       try {
           ret = new SBR(getChildType(), getCreateUser(), getCreateSystem(),
                   getCreateFunction(),
                   getCreateDateTime(), getUpdateUser(), getUpdateSystem(), getUpdateFunction(),
                   getUpdateDateTime(), getStatus(), getObject().structCopy());

           ret.setRevisionNumber(this.getRevisionNumber().intValue());
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


   /**
    * Adds a feature to the OverWrite attribute of the SBR object
    *
    * @param ow overwrite object
    * @exception ObjectException object exception
    */
   public void addOverWrite(SBROverWrite ow)
       throws ObjectException {
       try {
           addChild(ow);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * Adds a feature to the OverWrite attribute of the SBR object
    *
    * @param ows collection of overwrite objects
    * @exception ObjectException object exception
    */
   public void addOverWrites(Collection ows)
       throws ObjectException {
       try {
           addChildren((ArrayList) ows);
       } catch (ObjectException e) {
           throw e;
       }
   }


   /**
    * @param ow overwrite
    * @todo Document this method
    */
   public void removeOverWrite(SBROverWrite ow) {
       removeChild(ow);
   }
}
