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
package com.sun.mdm.index.objects.metadata;

import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.ParserException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import com.sun.mdm.index.parser.Utils;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;
/**
 * @author gzheng
 * meta data service class
 */
public class MetaDataService {

    /**
     * Creates a new instance of MetaDataService
     */
    public MetaDataService() {
    }

    private transient static final Logger mLogger 
        = Logger.getLogger("com.sun.mdm.index.objects.metadata.MetaDataService");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    //hash map of EIndexObject
    private static final HashMap EINDEX_OBJECTS = new HashMap();
    
    public static void registerObjectDefinition(InputStream objectDef) throws ParserException {
        EIndexObject eIndexObject = Utils.parseEIndexObject(new InputSource(objectDef));
        EINDEX_OBJECTS.put(eIndexObject.getObjectName(), eIndexObject);
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Registered object: " + eIndexObject.getObjectName());
        }
        ObjectFactory.setObject(eIndexObject);

    }
    
    public static void selectObjectDefinition(String name) {
        Iterator i = EINDEX_OBJECTS.values().iterator();
        EIndexObject obj = null;
        while (i.hasNext()) {
            obj = (EIndexObject) i.next();
            if (obj.getNode(name) != null) {
                ObjectFactory.setObject(obj);
                return;
            }
        }
        if (obj == null) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Object is not registered: " + name);
            }
        }        
    }
    
    /**
     * returns valid children types
     *
     * @param path object path
     * @return array of chldren tags
     */
    public static String[] getChildTypePaths(String path) {
        String[] ret = null;
        try {
            ObjectFactory f = ObjectFactory.getInstance();
            ret = f.getChildTypePaths(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ017: The child types could not be " + 
                                      "retrieved from the path {0}: {1}", path, 
                                      e.getMessage()));
        }
        return ret;
    }


    /**
     * gets array of child type(s)
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return String[]
     */
    public static String[] getChildTypes(String path)
        throws ObjectException {
        String[] ret = null;
        try {
            ret = ObjectFactory.getChildTypes(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * gets column name by a field path
     *
     * @param path field path
     * @return String column name
     */
    public static String getColumnName(String path) {
        if (!ObjectFactory.isFieldNodePath(path)) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Path \"" + path + "\" is not of field type");
            }
        }

        int pos = path.lastIndexOf(ObjectFactory.NODESEPARATOR);
        return (path.substring(pos + 1, path.length())).toUpperCase();
    }


    /**
     * gets column names and types by object path
     *
     * @param path field path
     * @exception ObjectException ObjectException
     * @return String[][] array of column names and types
     */
    public static String[][] getColumnNameTypes(String path)
        throws ObjectException {
        String[][] ret = null;
        try {
            ret = ObjectFactory.getColumnNameTypes(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * gets field type by field path
     *
     * @param path field path
     * @return String field type
     */
    public static String getFieldType(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getFieldType(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ018: The field type could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets field name by field path
     *
     * @param path field path
     * @return String field name
     */
    public static String getFieldName(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getFieldName(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ019: The field name could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets field size by field path
     *
     * @param path field path
     * @throws ObjectException if necessary
     * @return int field size
     */
    public static int getFieldSize(String path) throws ObjectException{
        int ret = -1;
        try {
            ret = ObjectFactory.getFieldSize(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ020: The field size could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
            throw e;
        }
        return ret;
    }
    
    /**
     * gets field required by field path
     *
     * @param path field path
     * @return boolean field required or not
     */
    public static boolean isFieldRequired(String path) {
        boolean ret = false;
        try {
            ret = ObjectFactory.isFieldRequired(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ021: The required field attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }
    
    /**
     * gets field updateable by field path
     *
     * @param path field path
     * @return boolean field updateable or not
     */
    public static boolean isFieldUpdateable(String path) {
        boolean ret = false;
        try {
            ret = ObjectFactory.isFieldUpdateable(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ022: The updateable field attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }
    
    /**
     * gets user code by field path for AUXID pull down
     *
     * @param path field path
     * @return String user code value
     */
    public static String getUserCode(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getUserCode(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ023: The user code attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }
    
    /**
     * gets constraint by by field path for AUXID pull down
     *
     * @param path field path
     * @return String constraint by value
     */
    public static String getConstraintBy(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getConstraintBy(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ024: The constraint attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }

    /**
     * gets column type by field path
     *
     * @param path field path
     * @return String
     */
    public static String getColumnType(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getColumnType(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ025: The column type attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * returns database meta information on the object indicated by 'tag'
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return DBMetaAttribute structure
     */
    public static DBMetaAttribute getDBAttribute(String path)
        throws ObjectException {
        DBMetaAttribute attr = new DBMetaAttribute();
        try {
            attr = ObjectFactory.getDBAttribute(path);
        } catch (ObjectException e) {
            throw e;
        }
        return attr;
    }


    /**
     * returns object's parent table name
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return parent table name
     */
    public static String[] getDBParentTableNames(String path)
        throws ObjectException {
        String[] ret = null;
        try {
            ret = ObjectFactory.getDBParentTableNames(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * returns primary keys on the parent table
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return array of primary keys
     */
    public static String[] getDBParentTablePK(String path)
        throws ObjectException {
        String[] ret = null;
        try {
            ret = ObjectFactory.getDBParentTablePK(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * returns foreign keys on the object table
     *
     * @param path object path
     * @return array of foreign keys
     */
    public static String[] getDBTableFK(String path) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getDBTableFK(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ026: The database foreign key attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * returns foreign keys on the object node
     *
     * @param path object path
     * @return array of foreign keys
     */    
    public static String[] getObjectFK(String path) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getParentPK(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ027: The foreign key attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }
    

    /**
     * returns foreign keys on the object table
     *
     * @param path object path
     * @param parent parent object tag
     * @return array of foreign keys
     */
    public static String[] getDBTableFK(String path, String parent) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getDBTableFK(path, parent);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ028: The database foreign key attribute could not be " + 
                                      "retrieved for the parent {0} from the path {1}\n: {2}", 
                                      parent, path, e.getMessage()));
        }
        return ret;
    }


    /**
     * returns name of the object table
     *
     * @param path object path
     * @return table name
     */
    public static String getDBTableName(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getDBTableName(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ029: The object table name attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * return object primary key field names, that have corresponding
     * PK in the table. Note: This is different than Object keys.
     *
     * @param path object path
     * @return an array of primary key names
     */
    public static String[] getPrimaryKey(String path) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getPrimaryKey(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ030: The primary key attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }
    
    /**
     * return primary keys on object table
     *
     * @param path object path
     * @return an array of primary keys
     */
    public static String[] getDBTablePK(String path) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getDBTablePK(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ031: The object table primary key attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets field paths
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return parent tag
     */
    public static String[] getFieldPaths(String path)
        throws ObjectException {
        String[] ret = null;
        try {
            ret = ObjectFactory.getFieldPaths(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * gets all fields
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return String[] fields
     */
    public static String[] getFields(String path)
        throws ObjectException {
        String[] ret = null;
        try {
            ret = ObjectFactory.getFields(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * gets parent object tag
     *
     * @param path object path
     * @exception ObjectException ObjectException
     * @return String parent object tag
     */
    public static String getParentType(String path)
        throws ObjectException {
        String ret = null;
        try {
            ret = ObjectFactory.getParentType(path);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * gets full parent path
     *
     * @param path object path
     * @return parent tag
     */
    public static String getParentTypePath(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getParentTypePath(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ032: The full parent path attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets SystemSBR path by a object name
     *
     * @param path object path
     * @return String SystemSBR full path
     */
    public static String getSBRPath(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getSBRPath(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ033: The SBR path attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets Object Keys by a object name
     *
     * @param path object path
     * @return String[] object keys
     */
    public static String[] getObjectKeys(String path) {
        String[] ret = null;
        try {
            ret = ObjectFactory.getObjectKeys(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ034: The object keys attribute could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }


    /**
     * gets SystemObject path by a object name
     *
     * @param path object path
     * @return String SystemObject full path
     */
    public static String getSOPath(String path) {
        String ret = null;
        try {
            ret = ObjectFactory.getSOPath(path);
        } catch (ObjectException e) {
            mLogger.warn(mLocalizer.x("OBJ035: The SystemObject path could not be " + 
                                      "retrieved from the path {0}: {1}", path, e.getMessage()));
        }
        return ret;
    }

    /**
     * gets Date Format
     * @return String Date Format
     */
    public static String getDateFormat() {
        return ObjectFactory.getDateFormat();
    }
    
    /**
     * tester
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        try {
            System.out.println("Is Field Node: " + ObjectFactory.isFieldNodePath("Enterprise.SystemSBR"));
            System.out.println("Is Field Node: " + ObjectFactory.isFieldNodePath("Enterprise.SystemSBR.Person"));
            System.out.println("Is Field Node: " 
                + ObjectFactory.isFieldNodePath("Enterprise.SystemObject.Person.Address.AddressLine2"));
            System.out.println("Is Object Node: " 
                + ObjectFactory.isObjectNodePath("Enterprise.SystemObject.Person.Address"));
            System.out.println("Is Object Node: " 
                + ObjectFactory.isObjectNodePath("Enterprise.SystemObject.Person.Address.AddressLine2"));
            System.out.println("Col Type: " + getColumnType("Enterprise.SystemObject.CreateUser"));
            System.out.println("Col Type: " + getColumnName("Enterprise.SystemObject.CreateUser"));

            String[] res = getChildTypePaths("Enterprise.SystemSBR.Person");
            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Paths: " + res[i]);
            }
            res = getChildTypes("Enterprise.SystemSBR.Person");
            for (int i = 0; i < res.length; i++) {
                System.out.println("Child Paths: " + res[i]);
            }

            res = getFieldPaths("Enterprise.SystemSBR.Person");
            for (int i = 0; i < res.length; i++) {
                System.out.println("Field Paths: " + res[i]);
            }
            res = getFields("Enterprise");
            for (int i = 0; i < res.length; i++) {
                System.out.println("Fields: " + res[i]);
            }

            String[][] colnametype = ObjectFactory.getColumnNameTypes("Enterprise.SystemSBR.Person");
            System.out.println("    Column                Type");
            for (int i = 0; i < colnametype.length; i++) {
                System.out.println("    " + colnametype[i][0] + "            " + colnametype[i][1]);
            }

            String[] ptables = getDBParentTableNames("Enterprise.SystemObject.Person");
            System.out.println(" PARENT TABLES");
            for (int i = 0; i < ptables.length; i++) {
                System.out.println("    " + ptables[i]);
            }

            String[] ppk = getDBParentTablePK("Enterprise");
            System.out.println(" PARENT TABLE PKs");
            for (int i = 0; i < ppk.length; i++) {
                System.out.println("    " + ppk[i]);
            }

            System.out.println("Table Name: " + getDBTableName("Enterprise"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemSBR"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemObject"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemSBR.Person"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemObject.Person"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemSBR.Person.Address"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemSBR.Person.Alias"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemSBR.Person.Phone"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemObject.Person.Address"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemObject.Person.Alias"));
            System.out.println("Table Name: " + getDBTableName("Enterprise.SystemObject.Person.Phone"));

            System.out.println("Table PK: ");
            String[] tpk = getDBTablePK("Enterprise.SystemSBR.Person.Address");
            for (int i = 0; i < tpk.length; i++) {
                System.out.println("    " + tpk[i]);
            }

            System.out.println("Parent type path: ");
            String tp = getParentTypePath("Enterprise.SystemSBR");
            System.out.println("    " + tp);

            System.out.println("Parent type: ");
            tp = getParentType("Enterprise.SystemSBR.Person.Address");
            System.out.println("    " + tp);

            DBMetaAttribute attr = getDBAttribute("Enterprise.SystemSBR.Person.Address");
            System.out.println("DB Meta: " + attr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
