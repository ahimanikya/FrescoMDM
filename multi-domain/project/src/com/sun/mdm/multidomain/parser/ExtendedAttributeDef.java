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
package com.sun.mdm.multidomain.parser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * @author gzheng
 * @version
 */
public class ExtendedAttributeDef {
    /**
     * string
     */
    public static final String STRINGFIELD = "string";
    /**
     * blob
     */
    public static final String BLOBFIELD = "blob";
    /**
     * byte
     */
    public static final String BYTEFIELD = "byte";
    /**
     * byte
     */
    public static final String CHARFIELD = "char";
    /**
     * boolean
     */
    public static final String BOOLEANFIELD = "boolean";
    /**
     * int
     */
    public static final String INTFIELD = "int";
    /**
     * float
     */
    public static final String FLOATFIELD = "float";
    /**
     * long
     */
    public static final String LONGFIELD = "long";
    /**
     * date
     */
    public static final String DATEFIELD = "date";

    private final String mTagFields = "fields";
    private final String mTagFieldName = "field-name";
    private final String mTagFieldType = "field-type";
    private final String mTagSize = "size";
    private final String mTagKeyType = "key-type";
    private final String mTagVisible = "visible";
    private final String mTagRequired = "required";
    private final String mTagUpdateable = "updateable";
    private final String mTagCodeModule = "code-module";
    private final String mTagUserCode = "user-code";
    private final String mTagMatchType = "match-type";
    private final String mTagConstraintBy = "constraint-by";
    private final String mTagMinimumValue = "minimum-value";
    private final String mTagMaximumValue = "maximum-value";
    private final String mTagPattern = "pattern";
    
    private String strFieldName;
    private String strFieldType;
    private boolean blocking;
    private boolean keytype;
    private boolean updateable;
    private boolean required;
    private int size = -1;
    private String sPattern = null;
    private String sCodeModule = null;
    private String sUserCode = null;
    private String sMatchType = null;
    private String sConstraintBy = null;    
    
    private boolean visible;
    private String sMinimumValue = null;
    private String sMaximumValue = null;

    /**
     * default constructor
     */
    public ExtendedAttributeDef() {
    }


    /**
     * create a new instance of FieldDef object
     * @param fieldName field name
     * @param fieldType field type
     * @param k key type
     * @param u visible
     * @param r required
     * @param fieldSize field size
     * @param pattern
     * @pattern codeModule
     * @param userCode
     * @param constrainedBy
     */
    public ExtendedAttributeDef(String fieldName, String fieldType, 
            String strMatchType,
            boolean b,
            boolean k, boolean u,
            boolean r, int fieldSize, 
            String pattern, String codeModule, 
            String userCode, String constrainedBy) {
        strFieldName = fieldName;
        strFieldType = fieldType;
        sMatchType = strMatchType;
        blocking = b;
        keytype = k;
        updateable = u;
        required = r;
        size = fieldSize;
        sPattern = pattern;
        sCodeModule = codeModule;
        sUserCode = userCode;
        sConstraintBy = constrainedBy;
        visible = true;
    }

    /**
     * create a new instance of FieldDef object
     * @param fieldName field name
     * @param fieldType field type
     * @param fieldSize field size
     * @param r required
     * @param k key type
     * @param v visible
     */
    public ExtendedAttributeDef(String fieldName, String fieldType, int fieldSize, boolean r, boolean k, boolean v) {
        strFieldName = fieldName;
        strFieldType = fieldType;
        size = fieldSize;
        required = r;
        keytype = k;
        visible = v;
    }
    
    /**
     * @return String ret string
     */
    public String getFieldName() {
        return strFieldName;
    }


    /**
     * @return String ret string
     */
    public String getFieldType() {
        return strFieldType;
    }


    /**
     * @return int ret int
     */
    public int getFieldSize() {
        return size;
    }
    
    
    /**
     * @return boolean ret boolean
     */
    public boolean isBlockOn() {
        return blocking;
    }


    /**
     * @return boolean ret boolean
     */
    public boolean isKeyType() {
        return keytype;
    }


    /**
     * @return boolean ret boolean
     */
    public boolean isRequired() {
        return required;
    }


    /**
     * @return boolean ret boolean
     */
    public boolean isNullable() {
        return !required;
    }


    /**
     * @return boolean ret boolean
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @return boolean ret boolean
     */
    public boolean isUpdateable() {
        return updateable;
    }

    /**
     * @return String ret String
     */
    public String getCodeModule() {
        return sCodeModule;
    }
    
    /**
     * @return String ret String
     */
    public String getUserCode() {
        return sUserCode;
    }
    
    /**
     * @return String ret String
     */
    public String getMatchType() {
        return sMatchType;
    }
    
    /**
     * @return String ret String
     */
    public String getConstraintBy() {
        return sConstraintBy;
    }
    
    /**
     * @return String ret String
     */
    public String getMaximumValue() {
        return sMaximumValue;
    }
    
    /**
     * @return String ret String
     */
    public String getMinimumValue() {
        return sMinimumValue;
    }
    
    /**
     * @return String ret String
     */
    public String getPattern() {
        return sPattern;
    }

    /**
     * @param node Node
     */
    public void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagFieldName.equals(((Element) nl.item(i)).getTagName())) {
                        strFieldName = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagFieldType.equals(((Element) nl.item(i)).getTagName())) {
                        strFieldType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagSize.equals(((Element) nl.item(i)).getTagName())) {
                        if (!(Utils.getStrElementValue(nl.item(i)).equals(""))) {
                            size = (new Integer(Utils.getStrElementValue(nl.item(i)))).intValue();
                        }
                    }
                    if (mTagKeyType.equals(((Element) nl.item(i)).getTagName())) {
                        keytype = (Boolean.valueOf(Utils.getStrElementValue(nl.item(i)))).booleanValue();
                    }
                    if (mTagVisible.equals(((Element) nl.item(i)).getTagName())) {
                        visible = (Boolean.valueOf(Utils.getStrElementValue(nl.item(i)))).booleanValue();
                    }
                    if (mTagRequired.equals(((Element) nl.item(i)).getTagName())) {
                        required = (Boolean.valueOf(Utils.getStrElementValue(nl.item(i)))).booleanValue();
                    }
                    if (mTagUpdateable.equals(((Element) nl.item(i)).getTagName())) {
                        updateable = (Boolean.valueOf(Utils.getStrElementValue(nl.item(i)))).booleanValue();
                    }

                    if (mTagMinimumValue.equals(((Element) nl.item(i)).getTagName())) {
                        sMinimumValue = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagMaximumValue.equals(((Element) nl.item(i)).getTagName())) {
                        sMaximumValue = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagCodeModule.equals(((Element) nl.item(i)).getTagName())) {
                        sCodeModule = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagUserCode.equals(((Element) nl.item(i)).getTagName())) {
                        sUserCode = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagMatchType.equals(((Element) nl.item(i)).getTagName())) {
                        sMatchType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagConstraintBy.equals(((Element) nl.item(i)).getTagName())) {
                        sConstraintBy = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagPattern.equals(((Element) nl.item(i)).getTagName())) {
                        sPattern = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }


    /**
     * @return String ret string
     */
    @Override
    public String toString() {
        String ret = "";
        ret += "        " + strFieldName + ": " + strFieldType;
        if (strFieldType.equals(STRINGFIELD)) {
            ret += "(" + size + ")";
        }
        ret += "  K: " + keytype + "  V: " + visible + "   R: " + required + "\n";

        return ret;
    }
}
