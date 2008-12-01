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
package com.sun.mdm.multidomain.project.generator.domainObjects;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import com.sun.mdm.multidomain.project.generator.TemplateWriter;
import com.sun.mdm.multidomain.project.generator.exception.TemplateWriterException;
import com.sun.mdm.multidomain.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.util.CodeGeneratorUtil;
import java.io.File;


class ObjectNodeWriter {
    private TemplateWriter mTW = null;
    private final String mPackage = "/com/sun/mdm/index/objects";
    private ArrayList mFieldKeys;
    private ArrayList mFieldNames;
    private ArrayList mFieldNullables;
    private ArrayList mFieldObjectFieldTypes;
    private ArrayList mFieldTypes;
    private String mName;
    private String mParentName;
    private String mPath;
    private ArrayList mSecondaryList;


    /**
     * @param path template path
     * @param parentName parent name
     * @param eot elephant object type
     * @param seclist secondary list
     * @param installPath install path
     */
    public ObjectNodeWriter(String path, 
                            String parentName, 
                            NodeDef eot, 
                            ArrayList seclist) {
        try {
            if (null != seclist) {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/objects/ObjectNode.java.tmpl");
            } else {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/objects/ObjectNodeLeaf.java.tmpl");
            }
        } catch (TemplateFileNotFoundException ex) {
            ex.printStackTrace();
        }
        mPath = path + mPackage;
        mName = eot.getTag();
        mParentName = parentName;
        mSecondaryList = seclist;
        mFieldNames = eot.createFieldNames();
        mFieldTypes = eot.createFieldTypes();
        mFieldKeys = eot.createFieldKeys();
        mFieldNullables = eot.createFieldNullables();
        mFieldObjectFieldTypes = new ArrayList();
        for (int i = 0; i < mFieldTypes.size(); i++) {
            if (((String) mFieldTypes.get(i)).startsWith("String")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_STRING_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Boolean")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_BOOL_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Integer")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_INT_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Long")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_LONG_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Float")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_FLOAT_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Date")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_DATE_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Timestamp")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_TIMESTAMP_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Byte")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_BYTE_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Character")) {
                mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_CHAR_TYPE");
            } else if (((String) mFieldTypes.get(i)).equals("Blob")) {
            mFieldObjectFieldTypes.add("ObjectField.OBJECTMETA_BLOB_TYPE");
            }
        }
    }


    /**
     * @exception ParserException
     * @todo Document this method
     */
    public void write()
        throws ParserException {
        ArrayList cons = null;
        try {
            cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(mName);
            values.add(mParentName);
            values.add(mFieldNames);
            values.add(mFieldObjectFieldTypes);
            values.add(mFieldTypes);
            values.add(mFieldKeys);
            values.add(mFieldNullables);
            if (null != mSecondaryList) {
                values.add(mSecondaryList);
            }

            String res = mTW.writeConstruct((String) cons.get(0), values);
            File outDir = new File(mPath);
            if (!outDir.exists()){
                outDir.mkdirs();
            }
            RandomAccessFile foutput 
            = new RandomAccessFile(mPath + "/" + CodeGeneratorUtil.makeClassName(mName) + "Object.java", "rw");
            foutput.write(res.getBytes("UTF-8"));
            foutput.close();
        } catch (TemplateWriterException e) {
            throw new ParserException(e);
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
