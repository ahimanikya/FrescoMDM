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
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.metadata.MetaDataService;

/**
 * @author gzheng
 * @version
 */
public class SBROverWrite extends ObjectNode {

    private static ArrayList mFieldNames;
    private static ArrayList mFieldTypes;
    static {
        mFieldNames = new ArrayList();
        mFieldNames.add("Path");
        mFieldNames.add("Data");
        mFieldTypes = new ArrayList();
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        mFieldTypes.add(new Integer(ObjectField.OBJECTMETA_BLOB_TYPE));
    }


    /**
     * Creates new potentialDuplicate
     *
     * @exception ObjectException object exception
     */
    public SBROverWrite() throws ObjectException {
        super("SBROverWrite", mFieldNames, mFieldTypes);
        setKeyType("Path", true);
        setKeyType("Data", false);
        setNullable("Path", false);
        setNullable("Data", true);
    }


    /**
     * @param path EPath
     * @param data value
     * @exception ObjectException object exception 
     * @todo Document this constructor
     */
    public SBROverWrite(String path, Object data) throws ObjectException {
        super("SBROverWrite", mFieldNames, mFieldTypes);
        setValue("Path", path);
        setValue("Data", data);

        setKeyType("Path", true);
        setKeyType("Data", false);
        setNullable("Path", false);
        setNullable("Data", true);
        reset();
    }


    /**
     * @exception ObjectException object exception
     * @return String path
     */
    public String getPath()
            throws ObjectException {
        try {
            return ((String) getValue("Path"));
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * @exception ObjectException object exception
     * @return String value
     */
    public Object getData()
            throws ObjectException {
        try {
            return (getValue("Data"));
        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * @exception ObjectException object exception
     * @return String field type
     */
    public String getFieldType()
            throws ObjectException {
        String ret = null;
        try {
            Object value = getData();
            if (value instanceof String) {
                String tempValue = (String) value;
                if (tempValue != null && tempValue.length() > 0) {
                    if (tempValue.charAt(0) == '[' && tempValue.charAt(tempValue.length() - 1) == ']') {
                        ret = ObjectField.OBJECTMETA_LINK_STRING;
                    } // for SBROverride
                } 
                else {
                    ret = ObjectField.OBJECTMETA_STRING_STRING;
                }

            } else if (value instanceof Boolean) {
                ret = ObjectField.OBJECTMETA_BOOL_STRING;
            } else if (value instanceof Integer) {
                ret = ObjectField.OBJECTMETA_INT_STRING;
            } else if (value instanceof Long) {
                ret = ObjectField.OBJECTMETA_LONG_STRING;
            } else if (value instanceof Float) {
                ret = ObjectField.OBJECTMETA_FLOAT_STRING;
            } else if (value instanceof Byte) {
                ret = ObjectField.OBJECTMETA_BYTE_STRING;
            } else if (value instanceof Character) {
                ret = ObjectField.OBJECTMETA_CHAR_STRING;
            } else if (value instanceof java.util.Date) {
                ret = ObjectField.OBJECTMETA_DATE_STRING;
            }
        } catch (ObjectException e) {
            throw e;
        }

        // If null is returned, retrieve the field type from the
        // MetaDataService.

        if (ret == null) {
            String path = getEPath();
            StringBuffer destBuf = new StringBuffer("Enterprise.SystemSBR.");
            StringBuffer sourceBuf = new StringBuffer(path);
            int startIndex = 0;
            int endIndex = sourceBuf.indexOf(".", startIndex);
            int lastIndex = sourceBuf.lastIndexOf(".");
            while (endIndex != -1) {
                String str = sourceBuf.substring(startIndex, endIndex);
                int leftParenIndex = str.indexOf("[");
                if (leftParenIndex != -1) {
                    destBuf.append(str.substring(0, leftParenIndex));
                } else {
                    destBuf.append(str);
                }
                destBuf.append(".");

                startIndex = endIndex + 1;

                // check for boundary condition
                if (endIndex == lastIndex) {
                    str = sourceBuf.substring(startIndex);
                    leftParenIndex = str.indexOf("[");
                    if (leftParenIndex != -1) {
                        destBuf.append(str.substring(0, leftParenIndex));
                    } else {
                        destBuf.append(str);
                    }
                }
                endIndex = sourceBuf.indexOf(".", startIndex);
            }
            ret = MetaDataService.getFieldType(destBuf.toString());

            // MetaDataService does not throw an exception on error.  
            // MetaDataService returns some type values with first letter not capitalized.
            if (ret != null && Character.isLowerCase(ret.charAt(0))) {
                ret = Character.toUpperCase(ret.charAt(0)) + ret.substring(1);
            }
        }
        return (ret);
    }


    /**
     * @param path Path
     * @exception ObjectException object exception
     * @todo Document: Setter for ChildFieldType attribute of the SBR object
     */
    public void setPath(String path)
            throws ObjectException {
        try {
            path = path.replaceAll("\\.", "#dot");
            path = path.replaceAll("\\[", "#left");
            path = path.replaceAll("\\]", "#right");
            path = path.replaceAll("\\*", "#star");
            path = path.replaceAll("=", "#equal");
            path = path.replaceAll("@", "#key");
            path = path.replaceAll(",", "#comma");

            setValue("Path", path);

        } catch (ObjectException e) {
            throw e;
        }
    }


    /**
     * get epath string
     * @return String epath string
     * @exception ObjectException object exception
     */
    public String getEPath()
            throws ObjectException {
        try {
            String path = (String) getValue("Path");

            path = path.replaceAll("#dot", "\\.");
            path = path.replaceAll("#left", "\\[");
            path = path.replaceAll("#right", "\\]");
            path = path.replaceAll("#star", "\\*");
            path = path.replaceAll("#equal", "=");
            path = path.replaceAll("#key", "@");
            path = path.replaceAll("#comma", ",");

            return path;
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * @param data Data
     * @exception ObjectException object exception
     * @todo Document: Setter for ChildFieldType attribute of the SBR object
     */
    public void setData(Object data)
            throws ObjectException {
        try {
            setValue("Data", data);
        } catch (ObjectException e) {
            throw e;
        }
    }

    /**
     * @exception ObjectException object exception
     * @return ObjectNode copy
     */
    public ObjectNode copy()
            throws ObjectException {
        SBROverWrite ret = null;
        try {
            ret = new SBROverWrite(getPath(), getData());
            ArrayList names = pGetFieldNames();
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
            ret.setFieldUpdateLogs(pGetFieldUpdateLogs());
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
        SBROverWrite ret = null;
        try {
            ret = new SBROverWrite(getPath(), getData());
            ArrayList names = pGetFieldNames();
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
