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



/**
 * Database MetaData Attributes
 *
 * @author gzheng
 */
public class DBMetaAttribute {
    private String[] mForeignKeys;
    private String[] mParentTableNames;
    private String[] mPrimaryKeys;
    private String mTableName;


    /**
     * Creates a new instance of DBMetaAttribute
     */
    public DBMetaAttribute() {
    }


    /**
     * creates a new instance of DBMetaAttribute by table name, array of pk,
     * array fk, array of parent tables
     *
     * @param tablename table name
     * @param pk primary keys
     * @param fk foreign keys
     * @param ptablenames parent table names
     */
    public DBMetaAttribute(String tablename, String[] pk, String[] fk, String[] ptablenames) {
        mTableName = tablename;
        mPrimaryKeys = pk;
        mForeignKeys = fk;
        mParentTableNames = ptablenames;
    }


    /**
     * The main program for the DBMetaAttribute class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        String tablename = "SBYN_PERSON";
        String[] ptablename = {"SBYN_SYSTEMOBJECT"};
        String[] pks = new String[2];
        pks[0] = "PERSONID";
        pks[1] = "PERSONTYPE";
        String[] fks = new String[2];
        fks[0] = "SYSTEMCODE";
        fks[1] = "LID";

        DBMetaAttribute attr = new DBMetaAttribute(tablename, pks, fks, ptablename);

    }


    /**
     * Getter for ForeignKeys attribute of the DBMetaAttribute object
     *
     * @return String[]
     */
    public String[] getForeignKeys() {
        return mForeignKeys;
    }


    /**
     * Getter for ParentTableNames attribute of the DBMetaAttribute object
     *
     * @return String[]
     */
    public String[] getParentTableNames() {
        return mParentTableNames;
    }


    /**
     * Getter for PrimaryKeys attribute of the DBMetaAttribute object
     *
     * @return String[]
     */
    public String[] getPrimaryKeys() {
        return mPrimaryKeys;
    }


    /**
     * Getter for TableName attribute of the DBMetaAttribute object
     *
     * @return String
     */
    public String getTableName() {
        return mTableName;
    }


    /**
     * Setter for ForeignKeys attribute of the DBMetaAttribute object
     *
     * @param fks foreign keys
     */
    public void setForeignKeys(String[] fks) {
        mForeignKeys = fks;
    }


    /**
     * Setter for ParentTableNames attribute of the DBMetaAttribute object
     *
     * @param ptablenames parent table names
     */
    public void setParentTableNames(String[] ptablenames) {
        mParentTableNames = ptablenames;
    }


    /**
     * Setter for PrimaryKeys attribute of the DBMetaAttribute object
     *
     * @param pks primary keys
     */
    public void setPrimaryKeys(String[] pks) {
        mPrimaryKeys = pks;
    }


    /**
     * Setter for TableName attribute of the DBMetaAttribute object
     *
     * @param tablename table name
     */
    public void setTableName(String tablename) {
        mTableName = tablename;
    }


    /**
     * toString
     *
     * @return String
     */
    public String toString() {
        return "TABLE[" 
            + getTableName() 
            + "]: Parent[" 
            + printArray(getParentTableNames()) 
            + "] PK[" + printArray(getPrimaryKeys()) + "] FK[" + printArray(getForeignKeys()) + "]";
    }


    private String printArray(String[] str) {
        String ret = "";
        if (null != str) {
            for (int i = 0; i < str.length; i++) {
                ret += " " + str[i];
            }
        }

        return ret;
    }
}
