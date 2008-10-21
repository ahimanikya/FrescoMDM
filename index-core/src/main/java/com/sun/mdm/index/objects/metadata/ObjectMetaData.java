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

import java.util.ArrayList;


/**
 * Object MetaData structure
 *
 * @author gzheng
 */

public class ObjectMetaData {
    private String[] mForeignKeys = null;
    private int mNumFK = 0;
    private String[] mPrimaryKeys = null;
    private int mNumPK = 0;
    private String[] mParentObjects = null;
    private int mNumPO = 0;
    private String[] mChildObjects = null;
    private int mNumCO = 0;
    private String[] mFields = null;
    private int mNumFields = 0;
    private String[] mObjectKeys = null;
    private int mNumOKs = 0;
    private String mObjectName;


    /**
     * Creates a new instance of ObjectMetaData by foreign keys, primary keys,
     * parent tables, child tables, fields, and tag
     *
     * @param fks list of foreign keys
     * @param pks list of primary keys
     * @param pts list of parent objects
     * @param cts list of child objects
     * @param oks list of object keys
     * @param flds list of field names
     * @param name object tag
     */
    public ObjectMetaData(ArrayList fks, 
                          ArrayList pks, 
                          ArrayList pts, 
                          ArrayList cts, 
                          ArrayList flds, 
                          ArrayList oks, 
                          String name) {
        if (null != fks) {
            mNumFK = fks.size();
            mForeignKeys = new String[fks.size()];
            for (int i = 0; i < fks.size(); i++) {
                mForeignKeys[i] = (String) fks.get(i);
            }
        }
        if (null != pks) {
            mNumPK = pks.size();
            mPrimaryKeys = new String[pks.size()];
            for (int i = 0; i < pks.size(); i++) {
                mPrimaryKeys[i] = (String) pks.get(i);
            }
        }
        if (null != pts) {
            mNumPO = pts.size();
            mParentObjects = new String[pts.size()];
            for (int i = 0; i < pts.size(); i++) {
                mParentObjects[i] = (String) pts.get(i);
            }
        }
        if (null != cts) {
            mNumCO = cts.size();
            mChildObjects = new String[cts.size()];
            for (int i = 0; i < cts.size(); i++) {
                mChildObjects[i] = (String) cts.get(i);
            }
        }
        if (null != flds) {
            mNumFields = flds.size();
            mFields = new String[flds.size()];
            for (int i = 0; i < flds.size(); i++) {
                mFields[i] = (String) flds.get(i);
            }
        }
        if (null != oks) {
            mNumOKs = oks.size();
            mObjectKeys = new String[oks.size()];
            for (int i = 0; i < oks.size(); i++) {
                mObjectKeys[i] = (String) oks.get(i);
            }
        }

        mObjectName = name;
    }


    /**
     * toString
     *
     * @return String
     */
    public String toString() {
        String ret = null;
        ret = "Object \"" + getName() + "\"\n";
        ret += "    has " + getFKSize() + " Foreign Keys\n";
        if (getFKSize() > 0) {
            for (int i = 0; i < mForeignKeys.length; i++) {
                ret += "    FK(" + i + "): " + mForeignKeys[i] + "\n";
            }
        }
        ret += "    has " + getPKSize() + " Primary Keys\n";
        if (getPKSize() > 0) {
            for (int i = 0; i < mPrimaryKeys.length; i++) {
                ret += "    PK(" + i + "): " + mPrimaryKeys[i] + "\n";
            }
        }
        ret += "    has " + getPOSize() + " Parent Objects\n";
        if (getPOSize() > 0) {
            for (int i = 0; i < mParentObjects.length; i++) {
                ret += "    PO(" + i + "): " + mParentObjects[i] + "\n";
            }
        }
        ret += "    has " + getCOSize() + " Child Objects\n";
        if (getCOSize() > 0) {
            for (int i = 0; i < mChildObjects.length; i++) {
                ret += "    CO(" + i + "): " + mChildObjects[i] + "\n";
            }
        }
        ret += "    has " + getFieldSize() + " Fields\n";
        if (getFieldSize() > 0) {
            for (int i = 0; i < mFields.length; i++) {
                ret += "    Field(" + i + "): " + mFields[i] + "\n";
            }
        }
        ret += "    has " + getOKSize() + " Object Keys\n";
        if (getOKSize() > 0) {
            for (int i = 0; i < mObjectKeys.length; i++) {
                ret += "    ObjectKey(" + i + "): " + mObjectKeys[i] + "\n";
            }
        }

        return ret;
    }


    /**
     * gets child object counts
     *
     * @return int child object counts
     */
    int getCOSize() {
        return mNumCO;
    }


    /**
     * gets child objects
     *
     * @return String[] child objects
     */
    public String[] getCOs() {
        return mChildObjects;
    }


    /**
     * gets foreign key counts
     *
     * @return int foreign key number
     */
    int getFKSize() {
        return mNumFK;
    }


    /**
     * gets foreign keys
     *
     * @return String[] foreign keys
     */
    public String[] getFKs() {
        return mForeignKeys;
    }


    /**
     * gets object field counts
     *
     * @return int field count
     */
    int getFieldSize() {
        return mNumFields;
    }


    /**
     * gets object fields
     *
     * @return String[] field names
     */
    public String[] getFields() {
        return mFields;
    }


    /**
     * gets object name
     *
     * @return String object name
     */
    String getName() {
        return mObjectName;
    }


    /**
     * gets primary key counts
     *
     * @return int primary key number
     */
    int getPKSize() {
        return mNumPK;
    }


    /**
     * gets primary keys
     *
     * @reutrn String[] primary keys
     */
    public String[] getPKs() {
        return mPrimaryKeys;
    }


    /**
     * gets parent object counts
     *
     * @return int parent object counts
     */
    int getPOSize() {
        return mNumPO;
    }


    /**
     * gets parent objects
     *
     * @return String[] parent objects
     */
    public String[] getPOs() {
        return mParentObjects;
    }
    

    /**
     * gets object key counts
     *
     * @return int object key counts
     */
    int getOKSize() {
        return mNumOKs;
    }


    /**
     * gets object keys
     *
     * @return String[] object keys
     */
    public String[] getOKs() {
        return mObjectKeys;
    }
}
