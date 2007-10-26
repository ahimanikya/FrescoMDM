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


import java.util.ArrayList;


/**
 * @author gzheng
 * @version
 * metadata class extends to ArrayList
 */
public class AttributeMetaData extends ArrayList {
    /**
     * undefined type
     */
    public static final int OMEGAMETA_UNDEFINED_TYPE = -1;
    /**
     * int type
     */
    public static final int OMEGAMETA_INT_TYPE = 0;
    /**
     * boolean type
     */
    public static final int OMEGAMETA_BOOL_TYPE = 1;
    /**
     * string type
     */
    public static final int OMEGAMETA_STRING_TYPE = 2;
    /**
     * byte type
     */
    public static final int OMEGAMETA_BYTE_TYPE = 3;
    /**
     * long type
     */
    public static final int OMEGAMETA_LONG_TYPE = 4;
    /**
     * object type
     */
    public static final int OMEGAMETA_OMEGA_TYPE = 5;

    /**
     * default constructor
     */
    public AttributeMetaData() {
        super();
    }

    /**
     * gets an ArrayList of field names
     *
     * @return ArrayList
     */
    public ArrayList getFieldNames() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < this.size(); i++) {
            ArrayList element = (ArrayList) this.get(i);

            if (!(((Integer) element.get(1)).intValue() == OMEGAMETA_OMEGA_TYPE)) {
                list.add((String) element.get(0));
            }
        }

        return list;
    }

    /**
     * gets Match flag by field name
     *
     * @param elementName field name String
     * @exception SystemObjectException system object exception
     * @return boolean
     */
    public boolean getMatch(String elementName) throws SystemObjectException {
        boolean bRet = true;
        boolean bFound = false;

        for (int i = 0; i < this.size(); i++) {
            ArrayList element = (ArrayList) this.get(i);

            if (((String) element.get(0)).equals(elementName)) {
                bRet = ((Boolean) element.get(2)).booleanValue();
                bFound = true;

                break;
            }
        }

        if (!bFound) {
            throw new InvalidElementNameException();
        }

        return bRet;
    }

    /**
     * @return ArrayList list
     */
    public ArrayList getSecondaryObjectNames() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < this.size(); i++) {
            ArrayList element = (ArrayList) this.get(i);

            if (((Integer) element.get(1)).intValue() == OMEGAMETA_OMEGA_TYPE) {
                list.add((String) element.get(0));
            }
        }

        return list;
    }

    /**
     * @param elementName element name
     * @exception SystemObjectException system object exception
     * @return int type
     */
    public int getType(String elementName) throws SystemObjectException {
        int iRet = OMEGAMETA_UNDEFINED_TYPE;
        boolean bFound = false;

        for (int i = 0; i < this.size(); i++) {
            ArrayList element = (ArrayList) this.get(i);

            if (((String) element.get(0)).equals(elementName)) {
                iRet = ((Integer) element.get(1)).intValue();
                bFound = true;

                break;
            }
        }

        if (!bFound) {
            throw new InvalidElementNameException();
        }

        return iRet;
    }

    /**
     * @param elementName element name
     * @param elementType element type
     * @param match match
     * @TODO Document this method
     */
    public void add(String elementName, int elementType, boolean match) {
        ArrayList element = new ArrayList();
        element.add(elementName);
        element.add(new Integer(elementType));
        element.add(Boolean.valueOf(match));
        this.add(element);
    }

    class InvalidElementNameException extends SystemObjectException {
        /**
         * @todo Document this constructor
         */
        public InvalidElementNameException() {
            super("Attribute Meta Data: invalid element name");
        }
    }
}
