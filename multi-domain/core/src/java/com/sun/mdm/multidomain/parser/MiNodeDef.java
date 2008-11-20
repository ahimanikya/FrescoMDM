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
import java.util.ArrayList;

/**
 * @author gzheng
 * @version
 */
public class MiNodeDef {
    private final String mIDTail = "Id";
    private final String mTagNodes = "nodes";
    private final String mTagTag = "tag";
    private final String mTagmFields = "fields";
    private final String mTagmParent = "parent";
    private final String mTagChildren = "children";
    private ArrayList mFields = null;
    private MiNodeDef mParent = null;
    private ArrayList mChildren = null; // Support multi-tier object

    private String strTag;


    /**
     * @return ArrayList list
     */
    public ArrayList getChildren() {
        return mChildren;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldNames() {
        ArrayList mFieldNams = new ArrayList();
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            mFieldNams.add(field.getFieldName());
        }

        return mFieldNams;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFields() {
        return mFields;
    }


    /**
     * @return NodeDef node definition
     */
    public MiNodeDef getParent() {
        return mParent;
    }


    /**
     * @return String string
     */
    public String getTag() {
        return strTag;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldKeys() {
        ArrayList keys = new ArrayList();
        keys.add("false");
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            keys.add((field.isKeyType()) ? "true" : "false");
        }
        return keys;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldNames() {
        ArrayList mFieldNams = new ArrayList();
        mFieldNams.add(strTag + mIDTail);
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            mFieldNams.add(field.getFieldName());
        }
        return mFieldNams;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldNullables() {
        ArrayList nullables = new ArrayList();
        nullables.add("false");
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            nullables.add((field.isNullable()) ? "true" : "false");
        }
        return nullables;
    }

    
    /**
     * @return ArrayList list
     */
    public ArrayList createFieldDefs() {
        ArrayList ret = new ArrayList();
        MiFieldDef id = new MiFieldDef(strTag + mIDTail, "string", 30, false, false, true);
        ret.add(id);
        ret.addAll(mFields);
        return ret;
    }

    /**
     * @return ArrayList list
     */
    public ArrayList createFieldTypeSizes() {
        ArrayList mFieldTypes = new ArrayList();
        mFieldTypes.add("String30");
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            if (field.getFieldType().equals(MiFieldDef.STRINGFIELD)) {
                mFieldTypes.add("String" + field.getFieldSize());
            } else if (field.getFieldType().equals(MiFieldDef.BOOLEANFIELD)) {
                mFieldTypes.add("Boolean");
            } else if (field.getFieldType().equals(MiFieldDef.BYTEFIELD)) {
                mFieldTypes.add("Byte");
            } else if (field.getFieldType().equals(MiFieldDef.CHARFIELD)) {
                mFieldTypes.add("Character");
            } else if (field.getFieldType().equals(MiFieldDef.DATEFIELD)) {
                mFieldTypes.add("Date");
            } else if (field.getFieldType().equals(MiFieldDef.INTFIELD)) {
                mFieldTypes.add("Integer");
            } else if (field.getFieldType().equals(MiFieldDef.LONGFIELD)) {
                mFieldTypes.add("Long");
            } else if (field.getFieldType().equals(MiFieldDef.FLOATFIELD)) {
                mFieldTypes.add("Float");
            }
        }
        return mFieldTypes;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldTypes() {
        ArrayList mFieldTypes = new ArrayList();
        mFieldTypes.add("String");
        for (int i = 0; i < mFields.size(); i++) {
            MiFieldDef field = (MiFieldDef) mFields.get(i);
            if (field.getFieldType().equals(MiFieldDef.STRINGFIELD)) {
                mFieldTypes.add("String");
            } else if (field.getFieldType().equals(MiFieldDef.BOOLEANFIELD)) {
                mFieldTypes.add("Boolean");
            } else if (field.getFieldType().equals(MiFieldDef.BYTEFIELD)) {
                mFieldTypes.add("Byte");
            } else if (field.getFieldType().equals(MiFieldDef.CHARFIELD)) {
                mFieldTypes.add("Character");
            } else if (field.getFieldType().equals(MiFieldDef.DATEFIELD)) {
                mFieldTypes.add("Date");
            } else if (field.getFieldType().equals(MiFieldDef.INTFIELD)) {
                mFieldTypes.add("Integer");
            } else if (field.getFieldType().equals(MiFieldDef.LONGFIELD)) {
                mFieldTypes.add("Long");
            } else if (field.getFieldType().equals(MiFieldDef.FLOATFIELD)) {
                mFieldTypes.add("Float");
            }
        }
        return mFieldTypes;
    }


    /**
     * @param node node
     */
    public void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagTag.equals(((Element) nl.item(i)).getTagName())) {
                        strTag = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagmFields.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == mFields) {
                            mFields = new ArrayList();
                        }
                        MiFieldDef ft = new MiFieldDef();
                        ft.parse(nl.item(i));
                        mFields.add(ft);
                    }
                    if (mTagmParent.equals(((Element) nl.item(i)).getTagName())) {
                        MiNodeDef nt = new MiNodeDef();
                        nt.parse(nl.item(i));
                        mParent = nt;
                    }
                    if (mTagChildren.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == mChildren) {
                            mChildren = new ArrayList();
                        }

                        MiNodeDef nt = new MiNodeDef();
                        nt.parse(nl.item(i));
                        mChildren.add(nt);
                    }
                }
            }
        }
    }


    /**
     * @return String string
     */
    public String toString() {
        String ret = "";
        ret += "\n  (" + mTagNodes + ")" + mTagTag + ": " + strTag + "\n";
        ret += "    mFields: \n  " + mFields + "\n";
        ret += "    mParent: \n  " + mParent + "\n";
        ret += "    mChildren: \n    " + mChildren + "\n";

        return ret;
    }
}
