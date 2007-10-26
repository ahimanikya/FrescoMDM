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
import com.sun.mdm.index.objects.exception.InvalidKeyTypeException;

import java.io.Externalizable;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;


/**
 * @author gzheng
 * bit flag class for object fields
 */
public class FieldFlag implements Externalizable {
    static final long serialVersionUID = -2216279605200640800L;
    public static final int version = 1;
    /**
     * invalid key type exception
     */
    private static InvalidKeyTypeException invalidkeytype = new InvalidKeyTypeException();

    /**
     * bit mask for VISIBLE
     */
    public static final int VISIBLEMASK = 1;

    /**
     * bit mask for SEARCH
     */
    public static final int SEARCHEDMASK = 2;

    /**
     * bit mask for CHANGE
     */
    public static final int CHANGEDMASK = 4;

    /**
     * bit mask for KEYTYPE
     */
    public static final int KEYTYPEMASK = 8;

    /**
     * bit mask for NULL
     */
    public static final int NULLMASK = 16;

    /**
     * bit mask for NULLABLE
     */
    public static final int NULLABLEMASK = 32;

    /**
     * bit mask for READACCESS
     */
    public static final int READACCESSMASK = 64;

    /**
     * bit mask for UPDATEACCESS
     */
    public static final int UPDATEACCESSMASK = 128;

    /**
     * type for VISIBLE
     */
    public static final int VISIBLETYPE = 0;

    /**
     * type for SEARCHED
     */
    public static final int SEARCHEDTYPE = 1;

    /**
     * type for CHANGED
     */
    public static final int CHANGEDTYPE = 2;

    /**
     * type for KEYTYPE
     */
    public static final int KEYTYPETYPE = 3;

    /**
     * type for NULL
     */
    public static final int NULLTYPE = 4;

    /**
     * type for NULLABLE
     */
    public static final int NULLABLETYPE = 5;

    /**
     * type for READACCESS
     */
    public static final int READACCESSTYPE = 6;

    /**
     * type for UPDATEACCESS
     */
    public static final int UPDATEACCESSTYPE = 7;
    private int mFlag;

    public int getFlagValue() {
        return mFlag;
    }
    
    public FieldFlag(int flag) {
        mFlag = flag;
    }
    
    /**
     * Creates a new instance of FieldFlag sets flag to 0
     */
    public FieldFlag() {
        mFlag = READACCESSMASK + UPDATEACCESSMASK;
    }

    /**
     * Getter for Flag attribute of the FieldFlag object
     *
     * @param type int value
     * @exception ObjectException ObjectException
     * @return boolean true or false
     */
    public boolean getFlag(int type) throws ObjectException {
        boolean bRet = false;

        switch (type) {
        case VISIBLETYPE:
            bRet = isVisible();

            break;

        case SEARCHEDTYPE:
            bRet = isSearched();

            break;

        case CHANGEDTYPE:
            bRet = isChanged();

            break;

        case KEYTYPETYPE:
            bRet = isKeyType();

            break;

        case NULLTYPE:
            bRet = isNull();

            break;

        case NULLABLETYPE:
            bRet = isNullable();

            break;

        case READACCESSTYPE:
            bRet = hasReadAccess();

            break;

        case UPDATEACCESSTYPE:
            bRet = hasUpdateAccess();

            break;

        default:
            throw new InvalidKeyTypeException("Unrecognized flag type: '" + type + "'");
        }

        return bRet;
    }

    /**
     * Getter for Changed attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isChanged() {
        
        
        int i = mFlag & CHANGEDMASK;
        return (i != 0) ? true : false;
    }

    /**
     * Getter for KeyType attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isKeyType() {
        return (0 != (mFlag & KEYTYPEMASK)) ? true : false;
    }

    /**
     * Getter for Null attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isNull() {
        return (0 != (mFlag & NULLMASK)) ? true : false;
    }

    /**
     * Getter for Nullable attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isNullable() {
        return (0 != (mFlag & NULLABLEMASK)) ? true : false;
    }

    /**
     * Getter for Searched attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isSearched() {
        return (0 != (mFlag & SEARCHEDMASK)) ? true : false;
    }

    /**
     * Getter for Visible attribute of the FieldFlag object
     *
     * @return boolean
     */
    public boolean isVisible() {
        return (0 != (mFlag & VISIBLEMASK)) ? true : false;
    }

    /**
     * if field is accessible for read
     *
     * @return boolean
     */
    public boolean hasReadAccess() {
        return (0 != (mFlag & READACCESSMASK)) ? true : false;
    }

    /**
     * if field is accessible for update
     *
     * @return boolean
     */
    public boolean hasUpdateAccess() {
        return (0 != (mFlag & UPDATEACCESSMASK)) ? true : false;
    }

    /**
     * Setter for Changed attribute of the FieldFlag object
     *
     * @param flag changed
     */
    public void setChanged(boolean flag) {
        if (flag) {
            mFlag = mFlag | CHANGEDMASK;
        } else {
            mFlag = mFlag & ~CHANGEDMASK;
        }
    }

    /**
     * Setter for Flag attribute of the FieldFlag object
     *
     * @param flag flag
     */
    public void setFlag(int flag) {
        mFlag = flag;
    }

    /**
     * Setter for Flag attribute of the FieldFlag object
     *
     * @param type int value
     * @param flag boolean value
     * @exception ObjectException ObjectException
     */
    public void setFlag(int type, boolean flag) throws ObjectException {
        switch (type) {
        case VISIBLETYPE:
            setVisible(flag);

            break;

        case SEARCHEDTYPE:
            setSearched(flag);

            break;

        case CHANGEDTYPE:
            setChanged(flag);

            break;

        case KEYTYPETYPE:
            setKeyType(flag);

            break;

        case NULLTYPE:
            setNull(flag);

            break;

        case NULLABLETYPE:
            setNullable(flag);

            break;

        case READACCESSTYPE:
            setReadAccess(flag);

            break;

        case UPDATEACCESSTYPE:
            setUpdateAccess(flag);

            break;

        default:
            throw new InvalidKeyTypeException("Unrecognized flag type: '" + type + "'");
        }
    }

    /**
     * Setter for KeyType attribute of the FieldFlag object
     *
     * @param flag key type
     */
    public void setKeyType(boolean flag) {
        if (flag) {
            mFlag = mFlag | KEYTYPEMASK;
        } else {
            mFlag = mFlag & ~KEYTYPEMASK;
        }
    }

    /**
     * Setter for Null attribute of the FieldFlag object
     *
     * @param flag null
     */
    public void setNull(boolean flag) {
        if (flag) {
            mFlag = mFlag | NULLMASK;
        } else {
            mFlag = mFlag & ~NULLMASK;
        }
    }

    /**
     * Setter for Nullable attribute of the FieldFlag object
     *
     * @param flag nullable
     */
    public void setNullable(boolean flag) {
        if (flag) {
            mFlag = mFlag | NULLABLEMASK;
        } else {
            mFlag = mFlag & ~NULLABLEMASK;
        }
    }

    /**
     * Setter for Searched attribute of the FieldFlag object
     *
     * @param flag searched
     */
    public void setSearched(boolean flag) {
        if (flag) {
            mFlag = mFlag | SEARCHEDMASK;
        } else {
            mFlag = mFlag & ~SEARCHEDMASK;
        }
    }

    /**
     * Setter for Visible attribute of the FieldFlag object
     *
     * @param flag visible
     */
    public void setVisible(boolean flag) {
        if (flag) {
            mFlag = mFlag | VISIBLEMASK;
        } else {
            mFlag = mFlag & ~VISIBLEMASK;
        }
    }

    /**
     * set field's read access flag
     *
     * @param flag read access
     */
    public void setReadAccess(boolean flag) {
        if (flag) {
            mFlag = mFlag | READACCESSMASK;
        } else {
            mFlag = mFlag & ~READACCESSMASK;
        }
    }

    /**
     * set field's update access flag
     *
     * @param flag update access
     */
    public void setUpdateAccess(boolean flag) {
        if (flag) {
            mFlag = mFlag | UPDATEACCESSMASK;
        } else {
            mFlag = mFlag & ~UPDATEACCESSMASK;
        }
    }

    /**
     * self copy
     *
     * @return FieldFlag
     */
    public FieldFlag copy() {
        FieldFlag flag = new FieldFlag();
        flag.setFlag(mFlag);

        return flag;
    }

    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeInt(mFlag);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mFlag = in.readInt();
        }
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
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
    }
}
