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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.mdm.multidomain.parser.MiNodeDef;
import com.sun.mdm.multidomain.project.generator.exception.InvalidTemplateFileException;
import com.sun.mdm.multidomain.project.generator.exception.UnmatchedTagsException;
import com.sun.mdm.multidomain.project.generator.TemplateWriter;
import com.sun.mdm.multidomain.project.generator.exception.TemplateWriterException;
import com.sun.mdm.multidomain.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.multidomain.project.generator.CodeGeneratorUtil;


class ObjectNodeWriter {
    
    private static final String DOMAIN_OBJECTS_PACKAGE_NAME = "com/sun/mdm/index/objects/domains";
    
    private TemplateWriter mTW = null;
    private ArrayList mFieldKeys;
    private ArrayList mFieldNames;
    private ArrayList mFieldNullables;
    private ArrayList mFieldObjectFieldTypes;
    private ArrayList mFieldTypes;
    private String mName;
    private String mParentName;
    private String mPath;
    private ArrayList mSecondaryList;
    private String mDomainName;


    /**
     * @param path output path
     * @param parentName parent name
     * @param MiNodeDef MMaster Index object node definition
     * @param seclist secondary list
     * @param domainName domain name
     * @exception TemplateFileNotFoundException Template File Not Found
     */
    public ObjectNodeWriter(String path, 
                            String parentName, 
                            MiNodeDef eot, 
                            ArrayList seclist, String domainName) throws TemplateFileNotFoundException {
        
        if (null != seclist) {
            mTW = new TemplateWriter("com/sun/mdm/multidomain/project/generator/domainObjects/ObjectNode.java.tmpl");
        } else {
            mTW = new TemplateWriter("com/sun/mdm/multidomain/project/generator/domainObjects/ObjectNodeLeaf.java.tmpl");
        }
               
        String tp = CodeGeneratorUtil.makeJavaName(domainName);
        mDomainName = tp.toLowerCase();
        mPath = path + "/"+ DOMAIN_OBJECTS_PACKAGE_NAME + "/" + mDomainName;
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
     * @exception TemplateWriterException Template writing exception
     */
    public void write() throws TemplateWriterException {
        {
            BufferedWriter output = null;
            try {
                ArrayList cons = null;
                cons = mTW.construct();
                ArrayList values = new ArrayList();
                values.add(mDomainName);
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
                outDir.mkdirs();
                String javafile = mPath + "/" + CodeGeneratorUtil.makeClassName(mName) + "Object.java";
                output = new BufferedWriter(new FileWriter(javafile));
                byte[] utf8Bytes = res.getBytes("UTF-8");
                String utf8String = new String(utf8Bytes, "UTF-8");
                output.write(utf8String);
                output.close();

            } catch (IOException ex) {
                throw new TemplateWriterException(ex.getMessage());
            } catch (UnmatchedTagsException ex) {
                throw new TemplateWriterException(ex.getMessage());
            } catch (InvalidTemplateFileException ex) {
                throw new TemplateWriterException(ex.getMessage());
            }    
        }
    }
}
