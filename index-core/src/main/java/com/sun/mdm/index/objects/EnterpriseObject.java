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

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.exception.PotentialDupMissingFieldException;
import com.sun.mdm.index.util.Localizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * The <b>EnterpriseObject</b> class represents a compilation of all objects
 * associated with a single entity in the master index database. An enterprise
 * object consists of one single best record (SBR) and one or more system
 * objects.
 * @author gzheng
 * @version
 */
public class EnterpriseObject extends ObjectNode {
    static final long serialVersionUID = -5968008396527735692L;
    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;

    private transient final Localizer mLocalizer = Localizer.get();
    
    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("EUID");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
    }

    /**
     * Creates a new instance of the EnterpriseObject class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @exception ObjectException Thrown if an error occurs while creating
     * the enterprise object.
     * @include
     */
    public EnterpriseObject() throws ObjectException {
        super("Enterprise", mFieldNames, mFieldTypes);
        mParentTag = "";

        if (null == mChildTags) {
            mChildTags = new ArrayList();
            mChildTags.add("SystemObject");
            mChildTags.add("SystemSBR");
        }
    }

    /**
     * Creates a new instance of the EnterpriseObject class based on the given
     * EUID, SBR, and system objects.
     * <p>
     * @param euid The EUID of the enterprise object.
     * @param sbr The SBR object associated with the enterprise
     * object.
     * @param sysobjs A collection of system objects that are
     * associated with the enterprise object.
     * @exception ObjectException Thrown if an error occurs while creating
     * the enterprise object.
     * @include
     */
    public EnterpriseObject(String euid, SBR sbr,
        Collection sysobjs) throws ObjectException {
        super("Enterprise", mFieldNames, mFieldTypes);
        setValue("EUID", euid);
        addChild(sbr);
        addChildren((ArrayList) sysobjs);

        if (null == mChildTags) {
            mChildTags = new ArrayList();
            mChildTags.add("SystemObject");
            mChildTags.add("SystemSBR");
        }

        setKeyType("EUID", true);
        setNullable("EUID", false);
        reset();
    }


    /**
     * Creates a new instance of the EnterpriseObject class. This
     * constructor is for internal use only.
     * <p>
     * @param euid The EUID of the enterprise object.
     * @param sbr The SBR object associated with the enterprise
     * object.
     * @param sysobjs A collection of system objects that are
     * associated with the enterprise object.
     * @param flag This parameter is only used internally.
     * @exception ObjectException Thrown if an error occurs while creating
     * the enterprise object.
     * @include
     */
    public EnterpriseObject(String euid, SBR sbr,
        Collection sysobjs, boolean flag) throws ObjectException {
        super("Enterprise", mFieldNames, mFieldTypes);
        setValue("EUID", euid);
        if (sbr != null) {
            addChildNoFlagSet(sbr);
        }

        ArrayList list = (ArrayList) sysobjs;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                SystemObject so = (SystemObject) list.get(i);
                addChildNoFlagSet(so);
            }
        }

        if (null == mChildTags) {
            mChildTags = new ArrayList();
            mChildTags.add("SystemObject");
            mChildTags.add("SystemSBR");
        }

        setKeyType("EUID", true);
        setNullable("EUID", false);
        reset();
    }

   

    /**
     * Retrieves the EUID from an instance of EnterpriseObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the <b>EUID</b>
     * field.
     * @exception ObjectException Thrown if an error occurs while retrieving
     * the field value.
     * @include
     */
    public String getEUID() throws ObjectException {
        try {
            return (String) getValue("EUID");
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the SBR object from an instance of EnterpriseObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>SBR</CODE> - The SBR associated with the enterprise
     * object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public SBR getSBR() {
        ArrayList list = pGetChildren("SystemSBR");

        if ((null != list) && (list.size() > 0)) {
            return (SBR) list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the status value from an instance of EnterpriseObject.
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
            SBR sbr = getSBR();
            if (sbr == null) {
                return null;
            } else {
                return sbr.getStatus();
            }
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Retrieves the system object associated with the specified system code
     * and local ID in an instance of EnterpriseObject.
     * <p>
     * @param system The processing code of the system to retrieve.
     * @param lid The local ID associated with the specified system.
     * @return <CODE>SystemObject</CODE> - The system object associated with
     * the specified system and local ID. Returns null if there is no
     * matching system and local ID in any system objects of the enterprise
     * object.
     * @exception ObjectException Thrown if an error occurs while retrieving
     * the object.
     * @include
     */
    public SystemObject getSystemObject(String system, String lid)
        throws ObjectException {
        SystemObject ret = null;

        try {
            ArrayList keynames = new ArrayList();
            ArrayList keytypes = new ArrayList();
            ArrayList keyvalues = new ArrayList();
            keynames.add("SystemCode");
            keynames.add("LocalID");
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keyvalues.add(system);
            keyvalues.add(lid);

            ObjectKey key = new ObjectKey(keynames, keytypes, keyvalues);
            ret = (SystemObject) getChild("SystemObject", key);
        } catch (ObjectException e) {
            throw e;
        }

        return ret;
    }

    /**
     * Retrieves all system objects from an instance of EnterpriseObject.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Collection</CODE> - A collection of system objects.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Collection getSystemObjects() {
        return pGetChildren("SystemObject");
    }

    /**
     * Sets the value of the <b>EUID</b> field in an instance of
     * EnterpriseObject.
     * <p>
     * @param euid The EUID of the enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the field value.
     * @include
     */
    public void setEUID(String euid) throws ObjectException {
        if ((null == euid) || euid.equals("")) {
            throw new PotentialDupMissingFieldException(mLocalizer.t("OBJ502: Could not " + 
                                            "set the EUID to a null or empty String"));
        }

        setValue("EUID", euid);
    }

    /**
     * Sets an SBR object in an instance of EnterpriseObject.
     * <p>
     * @param sbr The SBR object of the enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while setting
     * the SBR object.
     * @include
     */
    public void setSBR(SBR sbr) throws ObjectException {
        try {
            SBR sbrobj = getSBR();
            if (sbrobj != null) {
                throw new ObjectException(mLocalizer.t("OBJ503: Could not " + 
                                            "set the SBR record because an SBR " + 
                                            "record already exists."));
            }

            addChild(sbr);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Adds a system object to an instance of EnterpriseObject.
     * <p>
     * @param obj The system object to add the enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while adding
     * the system object.
     * @include
     */
    public void addSystemObject(SystemObject obj) throws ObjectException {
        try {
            addChild(obj);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Adds multiple system objects to an instance of EnterpriseObject.
     * <p>
     * @param systemobjs A collection of system objects to add to the
     * enterprise object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while adding
     * the system objects.
     * @include
     */
    public void addSystemObjects(Collection systemobjs)
        throws ObjectException {
        try {
            addChildren((ArrayList) systemobjs);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * deep copy for EnterpriseObject
     *
     * @exception ObjectException ObjectException
     * @return ObjectNode copy of itself
     */
    public ObjectNode copy() throws ObjectException {
        EnterpriseObject ret = null;

        try {
            SBR sbr = null;

            if (getSBR() != null) {
                sbr = (SBR) getSBR().copy();
            }

            ArrayList sysobjs = (ArrayList) getSystemObjects();
            ArrayList newsos = null;

            if (sysobjs != null) {
                newsos = new ArrayList();

                for (int i = 0; i < sysobjs.size(); i++) {
                    SystemObject so = (SystemObject) sysobjs.get(i);
                    newsos.add((SystemObject) so.copy());
                }
            }

            ret = new EnterpriseObject(getEUID(), sbr, newsos, false);
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
     * structural copy for EnterpriseObject
     *
     * @exception ObjectException ObjectException
     * @return ObjectNode copy of itself
     */
    public ObjectNode structCopy() throws ObjectException {
        EnterpriseObject ret = null;

        try {
            SBR sbr = (SBR) getSBR().structCopy();
            ArrayList sysobjs = (ArrayList) getSystemObjects();
            ArrayList newsos = null;

            if (sysobjs != null) {
                newsos = new ArrayList();

                for (int i = 0; i < sysobjs.size(); i++) {
                    SystemObject so = (SystemObject) sysobjs.get(i);
                    newsos.add((SystemObject) so.structCopy());
                }
            }

            ret = new EnterpriseObject(getEUID(), sbr, newsos);

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
     * Removes a system object from an instance of EnterpriseObject.
     * This is used to remove the SystemObject from the in memory
     * EnterpriseObject instance.  It will NOT result in the deletion
     * of the SystemObject from the database (see deleteSystemObject).
     * <p>
     * @param system The processing code of the system associated with
     * the system object to remove.
     * @param lid The local ID associated with the specified system.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while
     * removing the system object.
     * @include
     */
    public void removeSystemObject(String system, String lid)
        throws ObjectException {
        try {
            ArrayList keynames = new ArrayList();
            ArrayList keytypes = new ArrayList();
            ArrayList keyvalues = new ArrayList();
            keynames.add("SystemCode");
            keynames.add("LocalID");
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keyvalues.add(system);
            keyvalues.add(lid);

            ObjectKey key = new ObjectKey(keynames, keytypes, keyvalues);
            removeChild("SystemObject", key);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * Marks a system object for deletion from an instance of
     * EnterpriseObject. The system object is deleted by a call to
     * MasterController.updateEnterpriseObject.
     * <p>
     * @param system The processing code of the system associated with
     * the system object to mark for deletion.
     * @param lid The local ID associated with the specified system.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * @exception ObjectException Thrown if an error occurs while
     * marking the system object for deletion.
     * @include
     */
    public void deleteSystemObject(String system, String lid)
        throws ObjectException {
        try {
            ArrayList keynames = new ArrayList();
            ArrayList keytypes = new ArrayList();
            ArrayList keyvalues = new ArrayList();
            keynames.add("SystemCode");
            keynames.add("LocalID");
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keytypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
            keyvalues.add(system);
            keyvalues.add(lid);

            SuperKey key = new SuperKey(keynames, keytypes, keyvalues);
            deleteChild("SystemObject", key);
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * Retrieves a list of field names in an enterprise object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ArrayList</CODE> - A list of field names.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ArrayList getFieldNames() {
        return mFieldNames;
    }


    /**
     * Retrieves a list of field types in an enterprise object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>ArrayList</CODE> - A list of field types.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public ArrayList getFieldTypes() {
        return mFieldTypes;
    }

    /**
     * Indicates whether at least one system object in the enterprise object\
     * is still active.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - A Boolean indicator of whether the enterprise
     * object contains at least one active system object. <b>True</b> indicates
     * there is at least one active system object; <b>false</b> indicates there are
     * no active system objects.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */

    public boolean isAtleastOneSOActive() throws ObjectException {
        Collection sysObjs = getSystemObjects();

        boolean isAtleastOneSOActive = false;

        if (sysObjs != null) {
        	Iterator i = sysObjs.iterator();
        	while (i.hasNext()) {
        		SystemObject so = (SystemObject) i.next();
        		if ( ( so.getStatus().equals(SystemObject.STATUS_ACTIVE) ) && ( !so.isRemoved() ) ) {
        			isAtleastOneSOActive = true;
        			break;
        		}
        	}
        }
        return isAtleastOneSOActive;
    }


}
