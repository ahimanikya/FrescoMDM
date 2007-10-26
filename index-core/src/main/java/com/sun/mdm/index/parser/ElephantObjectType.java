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
package com.sun.mdm.index.parser;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class ElephantObjectType {
    /**
     * stc_sigma tag
     */
    public static final String STCSIGMATAG = "Stc_sigma";
    private final String mFmt = "    [mElephantObjectType]";
    private final String mTagName = "name";
    private final String mTagFields = "fields";
    private final String mTagStandardizationType = "standardization-type";
    private final String mTagRelatedObject = "related-object";

    private final int mPrimaryObject = 1;
    private final int mSecondaryObject = 2;
    private ArrayList eftFields = null;
    private ArrayList erotRelatedObjects = null;

    private String strName;
    private String strStandardizationType;


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldDefs() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                String[] strNameType = new String[3];
                strNameType[0] = field.getFieldName();
                strNameType[1] = field.getFieldType();
                strNameType[2] = field.getFieldSigma();
                al.add(strNameType);
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldNameTypes() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                String[] strNameType = new String[2];
                strNameType[0] = field.getFieldName();
                strNameType[1] = field.getFieldType();
                al.add(strNameType);
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldNames() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                al.add(field.getFieldName());
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldTypeObjs() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                al.add(field);
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getFieldTypes() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                al.add(field.getFieldType());
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getMatchFlags() {
        ArrayList al = null;

        if (null != eftFields) {
            al = new ArrayList();
            for (int i = 0; i < eftFields.size(); i++) {
                FieldType field = (FieldType) eftFields.get(i);
                boolean mflag = field.getMatchFlag();
                ArrayList mfld = new ArrayList();
                mfld.add(field.getFieldName());
                mfld.add(Boolean.valueOf(mflag));
                al.add(mfld);
            }
        }

        return al;
    }


    /**
     * @return String ret string
     */
    public String getName() {
        return strName;
    }


    /**
     * @return String ret string
     */
    public String getSigma() {
        return strStandardizationType;
    }


    /**
     * @return String ret string
     */
    public String getType() {
        return getName();
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldNameTypes() {
        String objname = getName();
        String objsigma = getSigma();
        ArrayList fieldnametypes = new ArrayList();
        String[] nt = new String[2];

        ArrayList fields = getFieldDefs();

        nt[0] = objname + "Id";
        nt[1] = "java.lang.String";
        fieldnametypes.add(nt);
        for (int i = 0; i < fields.size(); i++) {
            String[] fld = (String[]) fields.get(i);
            nt = new String[2];
            nt[0] = fld[0];
            nt[1] = fld[1];
            fieldnametypes.add(nt);
            if (null != fld[2]) {
                nt = new String[2];
                nt[0] = STCSIGMATAG + fld[0];
                nt[1] = fld[1];
                fieldnametypes.add(nt);
            }
        }
        if (null != objsigma) {
            nt = new String[2];
            nt[0] = STCSIGMATAG + objname;
            nt[1] = "java.lang.String";
            fieldnametypes.add(nt);
        }
        nt = new String[2];
        
        return fieldnametypes;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldNames() {
        String objname = getName();
        String objsigma = getSigma();
        ArrayList fieldnames = new ArrayList();

        ArrayList fields = getFieldDefs();

        fieldnames.add(objname + "Id");
        for (int i = 0; i < fields.size(); i++) {
            String[] fld = (String[]) fields.get(i);
            fieldnames.add(fld[0]);
            if (null != fld[2]) {
                fieldnames.add(STCSIGMATAG + fld[0]);
            }
        }
        if (null != objsigma) {
            fieldnames.add(STCSIGMATAG + objname);
        }
        return fieldnames;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList createFieldTypes() {
        String objsigma = getSigma();
        ArrayList fieldtypes = new ArrayList();

        ArrayList fields = getFieldDefs();

        fieldtypes.add("java.lang.String");
        for (int i = 0; i < fields.size(); i++) {
            String[] fld = (String[]) fields.get(i);
            fieldtypes.add(fld[1]);
            if (null != fld[2]) {
                fieldtypes.add(fld[1]);
            }
        }
        if (null != objsigma) {
            fieldtypes.add("java.lang.String");
        }
        return fieldtypes;
    }


    /**
     * default constructor
     */
    public ElephantObjectType() {
    }


    /**
     * @param node Node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        strName = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagFields.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == eftFields) {
                            eftFields = new ArrayList();
                        }

                        FieldType eft = new FieldType();
                        eft.parse(nl.item(i));
                        eftFields.add(eft);
                    } else if (mTagStandardizationType.equals(((Element) nl.item(i)).getTagName())) {
                        strStandardizationType = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagRelatedObject.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == erotRelatedObjects) {
                            erotRelatedObjects = new ArrayList();
                        }

                        RelatedObjectType erot = new RelatedObjectType();
                        erot.parse(nl.item(i));
                        erotRelatedObjects.add(erot);
                    }
                }
            }
        }
    }
}
