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

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.RelationDef;
import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.util.CodeGeneratorUtil;

import com.sun.mdm.index.objects.metadata.ObjectMetaData;
import com.sun.mdm.index.objects.metadata.FieldMetaData;
import com.sun.mdm.index.objects.metadata.DBMetaAttribute;
        
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import org.xml.sax.InputSource;

import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * ObjectFactory class. This class is copied from com.sun.mdm.index.objects.metadata.ObjectFactory
 * by changing static variables to private variables. The best should optimize the original one.
 * @author cye
 */
public class ObjectFactoryImpl implements ObjectFactory {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.core.ObjectFactoryImpl");	
    private static final Localizer localizer = Localizer.getInstance();
    
    private static final String OBJECT_FILE_NAME = "object.xml";
    private static final String DOMAIN_CONFIG_PATH = "Domains";
    public static final String VARCHAR2 = "VARCHAR2";
    public static final String CHAR = "CHAR";
    public static final String FLOAT = "FLOAT";
    public static final String LONG= "LONG";
    public static final String NUMBER = "NUMBER";
    public static final String DATE = "DATE";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String LONGRAW = "LONG RAW";
    public static final String BOOLEAN = "BOOLEAN";
	
    private static final String FIELD_EUID_STRING = "EUID";
    private static final String FIELD_EUID_TYPE = "String";
    private static final int FIELD_EUID_SIZE = 20;
    private static final String FIELD_SYSTEMCODE_STRING = "SystemCode";
    private static final String FIELD_SYSTEMCODE_TYPE = "String";
    private static final int FIELD_SYSTEMCODE_SIZE = 20;
    private static final String FIELD_LID_STRING = "LID";
    private static final String FIELD_LID_TYPE = "String";
    private static final int FIELD_LID_SIZE = 25;
    private static final String FIELD_ID_TYPE = "String";
    private static final int FIELD_ID_SIZE = 20;
    private static final int FIELD_SO_SIZE[] = {20, 25, 20, -1, -1, -1, 1000, 30, 20, -1, 30, 20, -1, 15};
    private static final int FIELD_SBR_SIZE[] = {20, 20, -1, -1, -1, 1000, 20, 30, 20, -1, 20, 30, 20, -1, 15};
    
    private static final String OBJECT_EO_STRING = "Enterprise";
    private static final String OBJECT_SO_STRING = "SystemObject";
    private static final String OBJECT_SBR_STRING = "SystemSBR";
    
    /**
     * enterprise tag
     */
    public static final String ENTERPRISETAG = OBJECT_EO_STRING;
    /**
     * systemobject tag
     */
    public static final String SYSTEMOBJECTTAG = OBJECT_SO_STRING;
    /**
     * sbr tag
     */
    public static final String SYSTEMSBRTAG = OBJECT_SBR_STRING;
    /**
     * node separator
     */
    public static final String NODESEPARATOR = ".";
        
    private static final String mSystemObjectPathHead = ENTERPRISETAG + NODESEPARATOR + SYSTEMOBJECTTAG + NODESEPARATOR;
    private static final String mSystemSBRPathHead = ENTERPRISETAG + NODESEPARATOR + SYSTEMSBRTAG + NODESEPARATOR;
    
    private String objectName = null;
    private EIndexObject mEIndexObject;
    private HashMap mFields;
    private HashMap mHandles;
    private HashMap mObjects;
    private HashMap mEPaths;
    private ArrayList mRelationships;
    private InputStream objectDefStream;
    private boolean isInit = false;
    private boolean isInitCreate = false;

    /**
     * Create instance of ObjectFactoryImpl.
     * @param name ObjectFactory name.
     */
    public ObjectFactoryImpl(String name){
        objectName = name;
    }    
    
    public void init() {
        if (!isInit) {
            synchronized(ObjectFactoryImpl.class) {
            try {
                if (mEIndexObject == null) {
                    InputStream stream = ObjectFactoryImpl.class.getResourceAsStream("/" + DOMAIN_CONFIG_PATH + "/" + objectName + "/" + OBJECT_FILE_NAME);
                    mEIndexObject = Utils.parseEIndexObject(new InputSource(stream));
                    stream.close();
                }                
                mRelationships = mEIndexObject.getRelationships();
                mObjects = new HashMap();
                mEPaths = new HashMap();
                    
                ObjectMetaData objMeta = null;
                FieldMetaData fieldMeta = null;
                ArrayList fks = null;
                ArrayList pks = null;
                ArrayList pts = null;
                ArrayList cts = null;
                ArrayList flds = null;

                fks = new ArrayList();
                fks.add(FIELD_EUID_STRING);
                fks.add(FIELD_SYSTEMCODE_STRING);
                fks.add(FIELD_LID_STRING);
                pks = new ArrayList();
                pks.add(FIELD_EUID_STRING);
                //pks.add(FIELD_SYSTEMCODE_STRING);
                //pks.add(FIELD_LID_STRING);
                cts = new ArrayList();
                cts.add(OBJECT_SBR_STRING);
                cts.add(OBJECT_SO_STRING);
                flds = new ArrayList();
                flds.add(FIELD_EUID_STRING);
                flds.add(FIELD_SYSTEMCODE_STRING);
                flds.add(FIELD_LID_STRING);
                objMeta = new ObjectMetaData(fks, pks, null, cts, flds, null, OBJECT_EO_STRING);
                mObjects.put(OBJECT_EO_STRING, objMeta);

                RelationDef rel = (RelationDef) mRelationships.get(0);
                SystemObject so = new SystemObject();
                SBR sbr = new SBR();

                pks = new ArrayList();
                pks.add(FIELD_EUID_STRING);
                pts = new ArrayList();
                pts.add(OBJECT_EO_STRING);
                cts = new ArrayList();
                cts.add(rel.getName());
                flds = sbr.pGetFieldNames();
                //flds.add(FIELD_EUID_STRING);
                flds.remove("SystemCode");
                flds.remove("LocalID");
                
                replaceFieldNames(flds);
                
                objMeta = new ObjectMetaData(null, pks, pts, cts, flds, null, OBJECT_SBR_STRING);
                mObjects.put("Enterprise.SystemSBR", objMeta);

                flds = so.pGetFieldNames();
                replaceFieldNames(flds);
               
                pts = new ArrayList();
                pts.add(OBJECT_EO_STRING);
                cts = new ArrayList();
                cts.add(rel.getName());
                pks = new ArrayList();
                pks.add(FIELD_SYSTEMCODE_STRING);
                pks.add(FIELD_LID_STRING);
                objMeta = new ObjectMetaData(null, pks, pts, cts, flds, null, OBJECT_SO_STRING);
                mObjects.put("Enterprise.SystemObject", objMeta);

                mFields = new HashMap();
                mFields.put("Enterprise.EUID", 
                            new FieldMetaData(FIELD_EUID_STRING, FIELD_EUID_TYPE, 
                                              FIELD_EUID_SIZE, true, false, null, null));
                mFields.put("Enterprise.SystemCode",
                             new FieldMetaData(FIELD_SYSTEMCODE_STRING, FIELD_SYSTEMCODE_TYPE, 
                                               FIELD_SYSTEMCODE_SIZE, true, false, null, null));
                mFields.put("Enterprise.LID", 
                             new FieldMetaData(FIELD_LID_STRING, FIELD_LID_TYPE, FIELD_LID_SIZE, 
                                               true, false, null, null));
                so = new SystemObject();
                ArrayList fields = so.pGetFieldNames();
                ArrayList types = so.getFieldTypes();
                replaceFieldNames(fields);
                
                for (int i = 0; i < fields.size(); i++) {
                     FieldMetaData fmeta = new FieldMetaData((String) fields.get(i), (Integer) types.get(i), 
                                                              FIELD_SO_SIZE[i], true, true, null, null);
                     mFields.put("Enterprise.SystemObject." + (String) fields.get(i), fmeta);
                }

                sbr = new SBR();
                fields = sbr.pGetFieldNames();
                replaceFieldNames(fields);
                types = sbr.getFieldTypes();
                
                for (int i = 0; i < fields.size(); i++) {
                    FieldMetaData fmeta = new FieldMetaData((String) fields.get(i), (Integer) types.get(i),
                                                             FIELD_SBR_SIZE[i], true, true, null, null);
                    mFields.put("Enterprise.SystemSBR." + (String) fields.get(i), fmeta);
                }
                mFields.put("Enterprise.SystemSBR.EUID", new FieldMetaData(FIELD_EUID_STRING, 
                             FIELD_EUID_TYPE, FIELD_EUID_SIZE, true, false, null, null));
                
                setPaths("", (RelationDef) mRelationships.get(0), true);
                isInit = true;
            } catch (Exception e) {
                throw new RuntimeException(localizer.t("OBJ577: Could not " + 
                                                        "initialize ObjectFactory: {0}", e));
            }
        } // end of synchronization.
      }        
    }
    
    private void initCreate() {
        if (!isInitCreate) {
            synchronized(ObjectFactoryImpl.class) {
            try {               
                mHandles = new HashMap();              
                mHandles.put(OBJECT_EO_STRING, Class.forName("com.sun.mdm.index.objects.EnterpriseObject"));
                mHandles.put(OBJECT_SO_STRING, Class.forName("com.sun.mdm.index.objects.SystemObject"));
                mHandles.put(OBJECT_SBR_STRING, Class.forName("com.sun.mdm.index.objects.SBR"));
                
                ArrayList nodes = mEIndexObject.getNodes();
                if (null != nodes) {
                    for (int i = 0; i < nodes.size(); i++) {
                        String tag = ((NodeDef) nodes.get(i)).getTag();
                        mHandles.put(tag, Class.forName("com.sun.mdm.index.objects." + CodeGeneratorUtil.makeClassName(tag) + "Object"));
                        //mHandles.put(tag, Class.forName("com.stc.eindex.objects." + CodeGeneratorUtil.makeClassName(tag) + "Object"));
                    }
                 }
                 isInitCreate = true;
            } catch (Exception e) {
                throw new RuntimeException(localizer.t("OBJ611: Could not " + 
                                                       "create an ObjectFactory instance: {0}", e));
            }
          } // end of synchronization.
        }
    }

    /** 
     * Set object def stream.
     * @param stream stream.
     */
    public void setObject(EIndexObject object) {
        mEIndexObject = object;
        isInit = false;
        isInitCreate = false;
    }
    
    /**
     * Get child type paths.
     * @param path object path path.
     * @exception ObjectException object exception object exception.
     * @return String[] string array of child types.
     * @todo Document: Getter for ChildTypePaths attribute of the ObjectFactory
     *      class
     */
    public String[] getChildTypePaths(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }

        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] ret = null;
        String[] res = ometa.getCOs();

        if (null != res) {
            ret = new String[res.length];
            for (int i = 0; i < res.length; i++) {
                ret[i] = path + "." + res[i];
            }
        }
        return ret;
    }

    /**
     * Gte object keys.
     * @param path object path object path.
     * @exception ObjectException object exception object exception.
     * @return String[] string array of object keys.
     */
    public String[] getObjectKeys(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] res = ometa.getOKs();
        return res;
    }

    /**
     * Get child types.
     * @param path object path. 
     * @exception ObjectException object exception.
     * @return String[] string array of child types.
     */
    public String[] getChildTypes(String path)             
        throws ObjectException {
        init();
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        return ometa.getCOs();
    }

    /**
     * Get column name.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String column name.
     */
    public String getColumnName(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        int pos = path.lastIndexOf(ObjectFactoryImpl.NODESEPARATOR);
        return (path.substring(pos + 1, path.length())).toUpperCase();
    }

    /**
     * Get column name and types.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[][] 2D array of column names and types.
     */
    public String[][] getColumnNameTypes(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[][] ret = null;
        String[] res = ometa.getFields();
        if (null != res) {
            String[] fpath = new String[res.length];
            ret = new String[res.length][2];

            for (int i = 0; i < res.length; i++) {
                fpath[i] = path + "." + res[i];
                ret[i][0] = getColumnName(fpath[i]);
                ret[i][1] = getColumnType(fpath[i]);
            }
        }
        return ret;
    }

    /**
     * Get field type.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String field type.
     */
    public String getFieldType(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String type = ((FieldMetaData) mFields.get(path)).getType();
        return (type);
    }

    /**
     * Get field name.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String field name.
     */
    public String getFieldName(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String name = ((FieldMetaData) mFields.get(path)).getName();
        return (name);
    }


    /**
     * Get field size.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return int field size.
     */
    public int getFieldSize(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        int size = ((FieldMetaData) mFields.get(path)).getSize();
        return (size);
    }
    
    /**
     * Check if field required.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return boolean field required.
     */
    public boolean isFieldRequired(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        boolean ret = ((FieldMetaData) mFields.get(path)).isRequired();
        return (ret);
    }
    
    /**
     * Check if field updateable.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return boolean field updateable.
     */
    public boolean isFieldUpdateable(String path) throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        boolean ret = ((FieldMetaData) mFields.get(path)).isUpdateable();
        return (ret);
    }
    
    /**
     * Get user code.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String user code for AUXID pull down.
     */
    public String getUserCode(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String ret = ((FieldMetaData) mFields.get(path)).getUserCode();
        return (ret);
    }
    
    /**
     * Get constrainBy.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String constraint by for AUXID pull down.
     */
    public String getConstraintBy(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String ret = ((FieldMetaData) mFields.get(path)).getConstraintBy();
        return (ret);
    }


    /**
     * Get colummn type.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String column type.
     */
    public String getColumnType(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isFieldNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String type = ((FieldMetaData) mFields.get(path)).getType();
        return (dbType(type));
    }

    /**
     * Get DBAttribute.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return DBMetaAttribute database metadata attributes.
     */
    public DBMetaAttribute getDBAttribute(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);

        DBMetaAttribute attr = new DBMetaAttribute();
        attr.setTableName(getDBTableName(path));
        attr.setParentTableNames(getDBParentTableNames(path));
        attr.setForeignKeys(getDBTableFK(path));
        attr.setPrimaryKeys(getDBTablePK(path));
        return attr;
    }

    /**
     * Get DBParentTableNames.
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] parent table names.
     */
    public String[] getDBParentTableNames(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String[] ret = null;
        if (path.equals(OBJECT_EO_STRING)) {
            ret = new String[2];
            ret[0] = "SBYN_SYSTEMSBR";
            ret[1] = "SBYN_SYSTEMOBJECT";
        } else if (path.equals("Enterprise.SystemSBR") || 
                    path.equals("Enterprise.SystemObject")) {
            ret = null;
        } else if (isPrimaryPath(path)) {
            ret = new String[1];
            if (isSBRPath(path)) {
                ret[0] = "SBYN_SYSTEMSBR";
            } else {
                ret[0] = "SBYN_SYSTEMOBJECT";
            }
        } else {
            ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
            String[] parents = ometa.getPOs();
            if (null != parents) {
                String tag = (isSBRPath(path)) ? "SBR" : "";
                ret = new String[parents.length];
                for (int i = 0; i < parents.length; i++) {
                    ret[i] = "SBYN_" + parents[i].toUpperCase() + tag;
                }
            }
        }
        return ret;
    }

    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] parent table primary keys.
     */
    public String[] getDBParentTablePK(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String[] ret = null;
        if (path.equals(OBJECT_EO_STRING)) {
            ret = new String[3];
            ret[0] = "SBYN_SYSTEMSBR.EUID";
            ret[1] = "SBYN_SYSTEMOBJECT.SYSTEMCODE";
            ret[2] = "SBYN_SYSTEMOBJECT.LID";
        } else if (path.equals("Enterprise.SystemSBR") || 
                   path.equals("Enterprise.SystemObject")) {
            ret = null;
        } else if (isPrimaryPath(path)) {
            if (isSBRPath(path)) {
                ret = new String[1];
                ret[0] = "SBYN_SYSTEMSBR.EUID";
            } else {
                ret = new String[2];
                ret[0] = "SBYN_SYSTEMOBJECT.SYSTEMCODE";
                ret[1] = "SBYN_SYSTEMOBJECT.LID";
            }
        } else {
            ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
            String[] parents = ometa.getPOs();
            if (null != parents) {
                String tag = (isSBRPath(path)) ? "SBR" : "";
                ret = new String[parents.length];
                for (int i = 0; i < parents.length; i++) {
                    ret[i] = "SBYN_" + parents[i].toUpperCase() + tag + "." + parents[i].toUpperCase() + "ID";
                }
            }
        }
        return ret;
    }


    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] parent table primary keys.
     */    
    public String[] getParentPK(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String[] ret = null;
        if (path.equals(OBJECT_EO_STRING)) {
            ret = new String[3];
            ret[0] = "SystemSBR.EUID";
            ret[1] = "SystemObject.SystemCode";
            ret[2] = "SystemObject.LID";
        } else if (path.equals("Enterprise.SystemSBR") || 
                   path.equals("Enterprise.SystemObject")) {
            ret = null;
        } else if (isPrimaryPath(path)) {
            if (isSBRPath(path)) {
                ret = new String[1];
                ret[0] = "SystemSBR.EUID";
            } else {
                ret = new String[2];
                ret[0] = "SystemObject.SystemCode";
                ret[1] = "SystemObject.LID";
            }
        } else {
            ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
            String[] parents = ometa.getPOs();
            if (null != parents) {
                String tag = (isSBRPath(path)) ? "SBR" : "";
                ret = new String[parents.length];
                for (int i = 0; i < parents.length; i++) {
                    ret[i] = parents[i] + "." + parents[i] + "Id";
                }
            }
        }
        return ret;
    }
    
    
    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] table foreign keys.
     */     
    public String[] getDBTableFK(String path) 
        throws ObjectException {
        String[] ret = null;
        init();
        try {
            ret = getDBParentTablePK(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }

    /**
     * @param path object path.
     * @param parent parent.
     * @exception ObjectException object exception.
     * @return String[] table foreign keys.
     */
    public String[] getDBTableFK(String path, String parent)
        throws ObjectException {
        init();
        String[] ret = null;
        try {
            ret = getDBParentTablePK(path);
            if (null != ret) {
                for (int i = 0; i < ret.length; i++) {
                    String tag = "";
                    if (isSBRPath(path)) {
                        tag = "SBR";
                    }
                    int pos = ret[i].lastIndexOf(".");
                    ret[i] = ret[i].substring(pos + 1, ret[i].length());
                    //String pt = "SBYN_" + parent.toUpperCase() + tag + ".";
                    //ret[i] = ret[i].substring(pt.length(), ret[i].length());
                }
            }
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }

    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String table name.
     */
    public String getDBTableName(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String ret = null;
        int pos = path.lastIndexOf(".");
        if (path.equals("Enterprise.SystemSBR")) {
            ret = "SBYN_SYSTEMSBR";
        } else if (isSBRPath(path)) {
            ret = "SBYN_" + path.substring(pos + 1, path.length()).toUpperCase() + "SBR";
        } else {
            ret = "SBYN_" + path.substring(pos + 1, path.length()).toUpperCase();
        }
        return ret;
    }

    /**
     * @param path object path
     * @exception ObjectException object exception
     * @return String[] table primary keys
     */
    public String[] getDBTablePK(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String[] ret = null;
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] res = ometa.getPKs();
        if (null != res) {
            ret = new String[res.length];
            for (int i = 0; i < res.length; i++) {
                ret[i] = res[i].toUpperCase();
            }
        }
        return ret;
    }

    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] primarykey field names.
     */
    public String[] getPrimaryKey(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String[] ret = null;
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] res = ometa.getPKs();
        return res;
    }
    
    /**
     * @param path object path
     * @exception ObjectException object exception
     * @return String[] field paths
     */
    public String[] getFieldPaths(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] ret = null;
        String[] res = ometa.getFields();
        if (null != res) {
            ret = new String[res.length];

            for (int i = 0; i < res.length; i++) {
                ret[i] = path + "." + res[i];
            }
        }
        return ret;
    }

    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String[] field names.
     */
    public String[] getFields(String path) 
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] ret = ometa.getFields();
        return ret;
    }

    /**
     * @return EIndexObject object definition
     */
    public EIndexObject getObjectDef() {
        init();
        return mEIndexObject;
    }

    /**
     * @param path object path.
     * @exception ObjectException object exception.
     * @return String parent type/tag.
     */
    public String getParentType(String path) throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        ObjectMetaData ometa = (ObjectMetaData) mObjects.get(path);
        String[] res = ometa.getPOs();
        return res[0];
    }

    /**
     * @param path object path
     * @exception ObjectException object exception
     * @return String parent type path
     */
    public String getParentTypePath(String path)
        throws ObjectException {
        init();
        if (!isPathValid(path)) {
            throw new ObjectException(localizer.t("OBJ578: Invalid path: {0}", path));
        }
        if (!isObjectNodePath(path)) {
            throw new ObjectException(localizer.t("OBJ579: Invalid path.  This " + 
                                                  "is not an ObjectNodePath: {0}", path));
        }
        String ret = null;
        if (path.equals(OBJECT_EO_STRING)) {
            
        } else if (path.equals("Enterprise.SystemSBR") || 
                   path.equals("Enterprise.SystemObject")) {
            ret = OBJECT_EO_STRING;
        } else if (isPrimaryPath(path)) {
            if (isSBRPath(path)) {
                ret = "Enterprise.SystemSBR";
            } else {
                ret = "Enterprise.SystemObject";
            }
        } else {
            int pos = path.lastIndexOf(".");
            ret = path.substring(0, pos);
        }
        return ret;
    }

    /**
     * @param tag tag.
     * @exception ObjectException object exception.
     * @return String SBR path.
     */
    public String getSBRPath(String tag) 
        throws ObjectException {
        init();
        String ret = null;
        Object[] keys = mObjects.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            if (key.startsWith("Enterprise.SystemSBR") && key.endsWith("." + tag)) {
                ret = key;
                break;
            }
        }
        // if not recognized as objet, try field
        if (ret == null) {        
            Object[] fieldkeys = mFields.keySet().toArray();            
            for (int i = 0; i < fieldkeys.length; i++) {
                String key = (String) fieldkeys[i];            
                if (key.startsWith("Enterprise.SystemSBR") && key.endsWith("." + tag)) {
                    ret = key;           
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param tag tag.
     * @exception ObjectException object exception.
     * @return String system object path.
     */
    public String getSOPath(String tag) 
        throws ObjectException {
        init();
        String ret = null;
        Object[] keys = mObjects.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            if (key.startsWith("Enterprise.SystemObject") && key.endsWith("." + tag)) {
                ret = key;
                break;
            }
        }
        if (ret == null) {
            // if not recognized as objet, try field        
            Object[] fieldkeys = mFields.keySet().toArray();            
            for (int i = 0; i < fieldkeys.length; i++) {
                String key = (String) fieldkeys[i];            
                if (key.startsWith("Enterprise.SystemObject") && key.endsWith("." + tag)) {
                    ret = key;            
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param path object path.
     * @return boolean if path is of field node.
     */
    public boolean isFieldNodePath(String path) {
        init();
        return (mFields.containsKey(path)) ? true : false;
    }

    /**
     * @param tag tag.
     * @return boolean if path is of object node.
     */
    public boolean isObjectNode(String tag) {
        init();
        boolean ret = false;
        if (tag.equals(OBJECT_EO_STRING) || 
            tag.equals(OBJECT_SO_STRING) || 
            tag.equals(OBJECT_SBR_STRING)) {
            ret = true;
        } else {
            Object[] keys = mObjects.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String) keys[i];
                if (key.endsWith("." + tag)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param path object path.
     * @return boolean if path is of object node path.
     */
    public boolean isObjectNodePath(String path) {
        init();
        return (mObjects.containsKey(path)) ? true : false;
    }

    /**
     * @param path object path
     * @return boolean if path is valid
     */
    public boolean isPathValid(String path) {
        init();
        return (mObjects.containsKey(path) || mFields.containsKey(path)) ? true : false;
    }

    public String getDateFormat() {
        init();
        return mEIndexObject.getDateFormat();
    }

    public String getDatabase() {
        init();
        return mEIndexObject.getDataBase();
    }

    /**
     * Does Database requires ANSI queries
     * @return true is DB Type is ANSI
     */
    public boolean isDatabaseANSI() {
        boolean ansi = false;
    	init();
        String dbServer = mEIndexObject.getDataBase();
        if ( dbServer.equals("SQL Server") || dbServer.equals("MySQL")) {
            ansi = true;
        }
        return ansi;
    }
    
    /**
     * @param epath object path.
     * @return boolean if epath is valid.
     */
    public boolean isEPathValid(String epath) {
        init();
        return (mEPaths.containsKey(epath)) ? true : false;
    }

    public HashMap getFields() {
       return mFields; 
    }
    
    /**
     * @param tag tag.
     * @exception ObjectException object exception.
     * @return ObjectNode create an empty instance of ObjectNode.
     */
    public ObjectNode create(String tag) 
        throws ObjectException {
        init();
        initCreate();
        ObjectNode ret = null;
        if (!isObjectNode(tag)) {
            throw new ObjectException(localizer.t("OBJ600: Could not create an " + 
                                                  "ObjectNode.  The tag not an ObjectNode tag: {0}", tag));
        }
        Class res = (Class) mHandles.get(tag);
        try {
            ret = (ObjectNode) res.newInstance();
        } catch (java.lang.InstantiationException e) {
            throw new ObjectException(localizer.t("OBJ601: Could not create an " + 
                                                   "ObjectNode: {0}", e));
        } catch (java.lang.IllegalAccessException e) {
            throw new ObjectException(localizer.t("OBJ601: Could not create an " + 
                                                  "ObjectNode: {0}", e));
        }
        return ret;
    }

    /**
     * The main program for the ObjectFactory class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            ObjectFactoryImpl instance = new ObjectFactoryImpl("Person");
            instance.init();
            System.out.println("Is Field Node: " + instance.isFieldNodePath("Enterprise.SystemSBR"));
            System.out.println("Is Field Node: " + instance.isFieldNodePath("Enterprise.SystemSBR.Person"));
            System.out.println("Is Field Node: " 
                + instance.isFieldNodePath("Enterprise.SystemObject.Person.Address.AddressLine2"));
            System.out.println("Is Object Node: " + instance.isObjectNodePath("Enterprise.SystemObject.Person.Address"));
            System.out.println("Is Object Node: " 
                + instance.isObjectNodePath("Enterprise.SystemObject.Person.Address.AddressLine2"));
            System.out.println("Get the field: " + instance.getFields().get("Enterprise.SystemObject.CreateUser"));
            System.out.println("Col Type: " + instance.getColumnType("Enterprise.SystemObject.CreateUser"));
            System.out.println("Col Type: " + instance.getColumnName("Enterprise.SystemObject.CreateUser"));

            String[] res = instance.getChildTypePaths("Enterprise.SystemSBR.Person");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Paths: " + res[i]);
            }

            res = instance.getChildTypes("Enterprise.SystemSBR.Person");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Paths: " + res[i]);
            }

            res = instance.getChildTypes("Enterprise.SystemSBR.Person.Address");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Paths: " + res[i]);
            }

            res = instance.getObjectKeys("Enterprise.SystemSBR.Person.Address");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Object Keys: " + res[i]);
            }

            res = instance.getChildTypePaths("Enterprise.SystemSBR.Person.Address");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Type Paths: " + res[i]);
            }

            res = instance.getFieldPaths("Enterprise.SystemSBR.Person");

            for (int i = 0; i < res.length; i++) {
                System.out.println("Field Paths: " + res[i]);
            }

            res = instance.getFields(OBJECT_EO_STRING);

            for (int i = 0; i < res.length; i++) {
                System.out.println("Fields: " + res[i]);
            }

            String[][] colNameType = instance.getColumnNameTypes(
                    "Enterprise.SystemSBR.Person");
            System.out.println("    Column                Type");

            for (int i = 0; i < colNameType.length; i++) {
                System.out.println("    " + colNameType[i][0] + "            " + colNameType[i][1]);
            }

            String[] ptables = instance.getDBParentTableNames(
                    "Enterprise.SystemObject.Person");
            System.out.println(" PARENT TABLES");

            for (int i = 0; i < ptables.length; i++) {
                System.out.println("    " + ptables[i]);
            }

            String[] ppk = instance.getDBParentTablePK(OBJECT_EO_STRING);
            System.out.println(" PARENT TABLE PKs");

            for (int i = 0; i < ppk.length; i++) {
                System.out.println("    " + ppk[i]);
            }

            System.out.println("Table Name: " + instance.getDBTableName(OBJECT_EO_STRING));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemSBR"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemObject"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemSBR.Person"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemObject.Person"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemSBR.Person.Address"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemSBR.Person.Alias"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemSBR.Person.Phone"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemObject.Person.Address"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemObject.Person.Alias"));
            System.out.println("Table Name: " + instance.getDBTableName("Enterprise.SystemObject.Person.Phone"));

            System.out.println("Table PK: ");

            String[] tpk = instance.getDBTablePK("Enterprise.SystemSBR.Person.Address");

            for (int i = 0; i < tpk.length; i++) {
                System.out.println("    " + tpk[i]);
            }

            System.out.println("Table FK: ");
            tpk = instance.getDBTableFK("Enterprise.SystemSBR.Person.Address", "Person");

            for (int i = 0; i < tpk.length; i++) {
                System.out.println("    " + tpk[i]);
            }

            System.out.println("Table FK: ");
            tpk = instance.getDBTableFK("Enterprise.SystemSBR.Person", OBJECT_SBR_STRING);

            for (int i = 0; i < tpk.length; i++) {
                System.out.println("    " + tpk[i]);
            }

            System.out.println("Table FK: ");
            tpk = instance.getDBTableFK("Enterprise.SystemSBR.Person.Address");

            for (int i = 0; i < tpk.length; i++) {
                System.out.println("    " + tpk[i]);
            }

            System.out.println("Parent type path: ");

            String tp = instance.getParentTypePath("Enterprise.SystemSBR");
            System.out.println("    " + tp);

            System.out.println("Parent type: ");
            tp = instance.getParentType("Enterprise.SystemSBR.Person.Std_BizName");
            System.out.println("    " + tp);

            DBMetaAttribute attr = instance.getDBAttribute(
                    "Enterprise.SystemSBR.Person.Address.Std_Address");
            System.out.println("DB Meta: " + attr);

            System.out.println("Get SBR Path: ");
            System.out.println("    " + instance.getSBRPath("Std_BizName"));

            System.out.println("Get SO Path: ");
            System.out.println("    " + instance.getSOPath("Std_CustName"));

            //System.out.println("handles: " + mHandles);
            ObjectNode n = instance.create("Person");
            System.out.println("Node: " + n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param path object path.
     * @return boolean if the path is of primary object path.
     */
    private boolean isPrimaryPath(String path) {
        if (path.startsWith("Enterprise.SystemSBR") || 
            path.startsWith("Enterprise.SystemObject")) {
            String h = null;

            if (isSBRPath(path)) {
                h = "Enterprise.SystemSBR";
            } else {
                h = "Enterprise.SystemObject";
            }
            h = path.substring(h.length() + 1, path.length());
            int pos = h.indexOf(".");
            if (pos < 0) {
                return true;
            }
        }
        return false;
    }
    
    private void replaceFieldNames(ArrayList flds) {
        int index = flds.indexOf("CreateDateTime");
        if (index > -1) {
           flds.set(index, "CreateDate");
        }
        index = flds.indexOf("UpdateDateTime");
        if (index > -1){
           flds.set(index, "UpdateDate");
        }
        index = flds.indexOf("LocalID");
        if (index > -1) {
          flds.set(index, "LID");
        }
    }

    /**
     * @param path object path.
     * @return boolean if path is of SBR path.
     */
    private boolean isSBRPath(String path) {
        return (path.startsWith("Enterprise.SystemSBR")) ? true : false;
    }

    /**
     * @param context path context.
     * @param rel relation definition.
     * @param primary boolean if it's primary.
     */
    private void setPaths(String context, RelationDef rel, boolean primary) {
        String name = rel.getName();
        NodeDef node = mEIndexObject.getNode(name);
        String head = "";

        if (!context.equals("")) {
            head = context + ".";
        }
        ObjectMetaData objMeta = null;
        FieldMetaData fieldMeta = null;

        ArrayList fks = null;
        ArrayList pks = null;
        ArrayList pts = null;
        ArrayList cts = null;
        ArrayList flds = null;
        if (primary) {
            ArrayList fields = node.createFieldDefs();
            ArrayList keyvalues = node.createFieldKeys();
            ArrayList keys = null;
            for (int i = 0; i < keyvalues.size(); i++) {
                if (((String) keyvalues.get(i)).equals("true")) {
                    if (keys == null) {
                        keys = new ArrayList();
                    }
                    keys.add(((FieldDef) fields.get(i)).getFieldName());
                }
            }

            cts = rel.getChildren();
            ArrayList childRelList = rel.getRelations();
            //ArrayList childRelList = mRelationships;
            if (childRelList != null) {
                if (cts == null) {
                    cts = new ArrayList();
                }

                for (int i = 0; i < childRelList.size(); i++) {
                    RelationDef childRel = (RelationDef) childRelList.get(i);
                    cts.add(childRel.getName());
                }
            }

            fks = new ArrayList();
            fks.add(FIELD_EUID_STRING);
            pks = new ArrayList();
            pks.add(name + "Id");
            pts = new ArrayList();
            pts.add(OBJECT_SBR_STRING);
            flds = new ArrayList();
           // flds.add(FIELD_EUID_STRING);
            if (fields != null) {
                for (int i = 0; i < fields.size(); i++) {
                    FieldDef f = (FieldDef) fields.get(i);
                    flds.add(f.getFieldName());
                }
            }
            objMeta = new ObjectMetaData(fks, pks, pts, cts, flds, keys, name);
            mObjects.put(mSystemSBRPathHead + head + name, objMeta);

            fks = new ArrayList();
            fks.add(FIELD_SYSTEMCODE_STRING);
            fks.add(FIELD_LID_STRING);
            pks = new ArrayList();
            pks.add(name + "Id");
            pts = new ArrayList();
            pts.add(OBJECT_SO_STRING);
            flds = new ArrayList();
            //flds.add(FIELD_SYSTEMCODE_STRING);
            //flds.add(FIELD_LID_STRING);
            if (fields != null) {
                for (int i = 0; i < fields.size(); i++) {
                    FieldDef f = (FieldDef) fields.get(i);
                    flds.add(f.getFieldName());
                }
            }
            objMeta = new ObjectMetaData(fks, pks, pts, cts, flds, keys, name);

            mObjects.put(mSystemObjectPathHead + head + name, objMeta);

            mFields.put(mSystemSBRPathHead + head + name + ".EUID", 
                new FieldMetaData(FIELD_EUID_STRING, FIELD_EUID_TYPE, FIELD_EUID_SIZE, 
                        true, false, null, null));
            mFields.put(mSystemObjectPathHead + head + name + ".SystemCode", 
                new FieldMetaData(FIELD_SYSTEMCODE_STRING, FIELD_SYSTEMCODE_TYPE, FIELD_SYSTEMCODE_SIZE, 
                        true, false, null, null));
            mFields.put(mSystemObjectPathHead + head + name + ".LID", 
                new FieldMetaData(FIELD_LID_STRING, FIELD_LID_TYPE, FIELD_LID_SIZE, 
                        true, false, null, null));

            for (int i = 0; i < fields.size(); i++) {
                FieldDef f = (FieldDef) fields.get(i);
                fieldMeta = new FieldMetaData(f.getFieldName(), f.getFieldType(), f.getFieldSize(),
                            !f.isNullable(), f.isUpdateable(), f.getUserCode(), f.getConstraintBy());
                mFields.put(mSystemSBRPathHead + head + name + "." + f.getFieldName(), fieldMeta);
                mEPaths.put(mSystemSBRPathHead + head + name + "." + f.getFieldName(), null);
                fieldMeta = new FieldMetaData(f.getFieldName(), f.getFieldType(), f.getFieldSize(), 
                            !f.isNullable(), f.isUpdateable(), f.getUserCode(), f.getConstraintBy());
                mFields.put(mSystemObjectPathHead + head + name + "." + f.getFieldName(), fieldMeta);
                mEPaths.put(mSystemObjectPathHead + head + name + "." + f.getFieldName(), null);
                mEPaths.put(name + "." + f.getFieldName(), null);
            }
        }

        ArrayList childlist = rel.getChildren();

        if (null != childlist) {
            for (int i = 0; i < childlist.size(); i++) {
                String cName = (String) childlist.get(i);

                NodeDef n = mEIndexObject.getNode((String) childlist.get(i));
                ArrayList f = n.createFieldDefs();
                ArrayList kv = n.createFieldKeys();
                ArrayList k = null;

                for (int j = 0; j < kv.size(); j++) {
                    if (((String) kv.get(j)).equals("true")) {
                        if (k == null) {
                            k = new ArrayList();
                        }

                        k.add(((FieldDef) f.get(j)).getFieldName());
                    }
                }

                fks = new ArrayList();
                fks.add(name + "Id");
                pks = new ArrayList();
                pks.add(cName + "Id");
                pts = new ArrayList();
                pts.add(name);
                flds = new ArrayList();
                if (f != null) {
                    for (int j = 0; j < f.size(); j++) {
                        FieldDef fdef = (FieldDef) f.get(j);
                        flds.add(fdef.getFieldName());
                    }
                }
                objMeta = new ObjectMetaData(fks, pks, pts, null, flds, k, cName);

                mObjects.put(mSystemObjectPathHead + head + name + "." + cName, objMeta);
                mObjects.put(mSystemSBRPathHead + head + name + "." + cName, objMeta);
                mEPaths.put(mSystemObjectPathHead + head + name + "." + cName, null);
                mEPaths.put(mSystemSBRPathHead + head + name + "." + cName, null);
                mEPaths.put(name + "." + cName, null);
                mEPaths.put(name + "." + cName + "[*].*", null);

           
                for (int j = 0; j < f.size(); j++) {
                    FieldDef fdef = (FieldDef) f.get(j);
                    fieldMeta = new FieldMetaData(fdef.getFieldName(), fdef.getFieldType(), fdef.getFieldSize(),
                        !fdef.isNullable(), fdef.isUpdateable(), fdef.getUserCode(), fdef.getConstraintBy());
                    mFields.put(mSystemObjectPathHead 
                        + head + name + "." + cName + "." + fdef.getFieldName(), fieldMeta);
                    fieldMeta = new FieldMetaData(fdef.getFieldName(), fdef.getFieldType(), fdef.getFieldSize(),
                        !fdef.isNullable(), fdef.isUpdateable(), fdef.getUserCode(), fdef.getConstraintBy());
                    mFields.put(mSystemSBRPathHead + head + name + "." + cName + "." + fdef.getFieldName(), fieldMeta);
                    mEPaths.put(mSystemObjectPathHead  
                        + head + name + "." + cName + "." + fdef.getFieldName(), null);
                    mEPaths.put(mSystemSBRPathHead  
                        + head + name + "." + cName + "." + fdef.getFieldName(), null);
                    mEPaths.put(mSystemSBRPathHead  
                        + head + name + "." + cName + "[*]."+ fdef.getFieldName(), null);
                    mEPaths.put(name + "." + cName + "."+ fdef.getFieldName(), null);
                    mEPaths.put(name + "." + cName + "[*]."+ fdef.getFieldName(), null);
                    mEPaths.put(cName + "."+ fdef.getFieldName(), null);
                }
            }
        }

        ArrayList relList = rel.getRelations();

        if (null != relList) {
            for (int i = 0; i < relList.size(); i++) {
                RelationDef r = (RelationDef) relList.get(i);
                String cName = r.getName();

                NodeDef n = mEIndexObject.getNode(cName);
                ArrayList f = n.createFieldDefs();
                ArrayList kv = n.createFieldKeys();
                ArrayList k = null;

                for (int j = 0; j < kv.size(); j++) {
                    if (((String) kv.get(j)).equals("true")) {
                        if (k == null) {
                            k = new ArrayList();
                        }

                        k.add((String) f.get(j));
                    }
                }

                cts = r.getChildren();

                ArrayList childRList = r.getRelations();

                if (childRList != null) {
                    if (cts == null) {
                        cts = new ArrayList();
                    }

                    for (int j = 0; j < childRList.size(); j++) {
                        RelationDef childR = (RelationDef) childRList.get(j);
                        cts.add(childR.getName());
                    }
                }

                fks = new ArrayList();
                fks.add(name + "Id");
                pks = new ArrayList();
                pks.add(cName + "Id");
                pts = new ArrayList();
                pts.add(name);
                flds = new ArrayList();
                flds.add(name + "Id");
                flds.addAll(f);
                objMeta = new ObjectMetaData(fks, pks, pts, cts, flds, k, cName);

                mObjects.put(mSystemObjectPathHead + head + name + "." + cName, objMeta);
                mObjects.put(mSystemSBRPathHead + head + name + "." + cName, objMeta);
                mEPaths.put(mSystemObjectPathHead + head + name + "." + cName, null);
                mEPaths.put(mSystemSBRPathHead + head + name + "." + cName, null);
                mEPaths.put(name + "." + cName, objMeta);
                mEPaths.put(name + "." + cName + "[*].*", null);

                mFields.put(mSystemObjectPathHead 
                    + head + name + "." + cName + "." + name + "Id", 
                    new FieldMetaData(name + "Id", FIELD_ID_TYPE, FIELD_ID_SIZE, true, false, null, null));
                mFields.put(mSystemSBRPathHead 
                    + head + name + "." + cName + "." + name + "Id", 
                    new FieldMetaData(name + "Id", FIELD_ID_TYPE, FIELD_ID_SIZE, true, false, null, null));
                mEPaths.put(mSystemObjectPathHead + head + name + "." + cName + "." + name + "Id", null);
                mEPaths.put(mSystemSBRPathHead + head + name + "." + cName + "." + name + "Id", null);
                mEPaths.put(name + "." + cName + "." + name + "Id", null);

                for (int j = 0; j < f.size(); j++) {
                    FieldDef fdef = (FieldDef) f.get(j);
                    fieldMeta = new FieldMetaData(fdef.getFieldName(), fdef.getFieldType(), fdef.getFieldSize(), 
                        !fdef.isNullable(), fdef.isUpdateable(), fdef.getUserCode(), fdef.getConstraintBy());
                    mFields.put(mSystemObjectPathHead 
                            + head + name + "." + cName + "." + fdef.getFieldName(), fieldMeta);
                    fieldMeta = new FieldMetaData(fdef.getFieldName(), fdef.getFieldType(), fdef.getFieldSize(),
                        !fdef.isNullable(), fdef.isUpdateable(), fdef.getUserCode(), fdef.getConstraintBy());
                    mFields.put(mSystemSBRPathHead + head + name + "." + cName + "." + fdef.getFieldName(), fieldMeta);
                    mEPaths.put(mSystemObjectPathHead + head + name + "." + cName + "." + fdef.getFieldName(), null);
                    mEPaths.put(mSystemSBRPathHead + head + name + "." + cName + "." + fdef.getFieldName(), null);
                    mEPaths.put(name + "." + cName + "." + fdef.getFieldName(), null);
                    mEPaths.put(name + "." + cName + "[*]." + fdef.getFieldName(), null);
                    mEPaths.put(cName + "." + fdef.getFieldName(), null);
                }
                setPaths(head + name, r, false);
            }
        }
    }


    /**
     * @param type type.
     * @return String ret string.
     */
    private String dbType(String type) {
        String ret = null;
        // to be replaced by db mapper
        if (type.equals(ObjectField.OBJECTMETA_STRING_STRING) || type.equals(FieldDef.STRINGFIELD)) {
            ret = VARCHAR2;
        } else if (type.equals(ObjectField.OBJECTMETA_BYTE_STRING) || type.equals(FieldDef.BYTEFIELD)) {
            ret = CHAR;
        } else if (type.equals(ObjectField.OBJECTMETA_CHAR_STRING) || type.equals(FieldDef.CHARFIELD)) {
            ret = CHAR;
        } else if (type.equals(ObjectField.OBJECTMETA_FLOAT_STRING) || type.equals(FieldDef.FLOATFIELD)) {
            ret = FLOAT;
        } else if (type.equals(ObjectField.OBJECTMETA_LONG_STRING) || type.equals(FieldDef.LONGFIELD)) {
            ret = LONG;
        } else if (type.equals(ObjectField.OBJECTMETA_INT_STRING) || type.equals(FieldDef.INTFIELD)) {
            ret = NUMBER;
        } else if (type.equals(ObjectField.OBJECTMETA_DATE_STRING) || type.equals(FieldDef.DATEFIELD)) {
            ret = DATE;
        } else if (type.equals(ObjectField.OBJECTMETA_TIMESTAMP_STRING) || type.equals(FieldDef.DATEFIELD)) {
            ret = TIMESTAMP;
        } else if (type.equals(ObjectField.OBJECTMETA_BLOB_STRING) || type.equals(FieldDef.BLOBFIELD)) {
            ret = LONGRAW;
        } else if (type.equals(ObjectField.OBJECTMETA_BOOL_STRING) || type.equals(FieldDef.BOOLEANFIELD)) {
            ret = BOOLEAN;
        }
        return ret;
    }    
}
