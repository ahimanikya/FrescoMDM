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
package com.sun.mdm.index.project.generator.ops;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.util.CodeGeneratorUtil;


/**
 * @author gzheng
 * @version
 */
class ObjectNodeDBWriter {
    private TemplateWriter mTW = null;
    private TemplateWriter mSBRTW = null;
    private final String mPackage = "\\com\\sun\\mdm\\index\\ops";
    private ArrayList mFieldNames;
    private ArrayList mFieldTypes;
    private String mName;
    private String mParentName;
    private String mPath;
    private ArrayList mSecondaryList;


    /**
     * @param path template path
     * @param pname parent name
     * @param node node definition
     * @param seclist secondary list
     * @param installPath install path
     */
    public ObjectNodeDBWriter(String path, 
                              String pname, 
                              NodeDef node, 
                              ArrayList seclist) {
        try {
            if (pname.equals("")) {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/ObjectNodeRootDB.java.tmpl");
                mSBRTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/ObjectNodeRootSBRDB.java.tmpl");
            } else if (null != seclist) {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/ObjectNodeDB.java.tmpl");
                mSBRTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/ObjectNodeDB.java.tmpl");
            } else {
                mTW = new TemplateWriter("com/sun/mdm/index/templates/ops/ObjectNodeLeafDB.java.tmpl");
                mSBRTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/ObjectNodeLeafDB.java.tmpl");
            }
        } catch (TemplateFileNotFoundException ex) {
            ex.printStackTrace();
        }
        mPath = path + mPackage;
        mParentName = pname;
        mName = node.getTag();
        mSecondaryList = seclist;
        mFieldNames = node.createFieldNames();
        mFieldTypes = node.createFieldTypes();
    }


    /**
     * @exception ParserException parser exception
     */
    public void write()
        throws ParserException {
        try {
            ArrayList cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(mName);
            values.add(mParentName);
            values.add(mFieldNames);
            values.add(mFieldTypes);
            if (null != mSecondaryList) {
                values.add(mSecondaryList);
            }
            values.add("");

            String res = mTW.writeConstruct((String) cons.get(0), values);
            RandomAccessFile foutput 
                = new RandomAccessFile(mPath 
                                       + "\\" 
                                       + CodeGeneratorUtil.makeClassName(mName) 
                                       + "DB.java", "rw");
            foutput.write(res.getBytes());
            foutput.close();
            

            values.clear();
            values.add(mName);
            values.add(mParentName);
            values.add(mFieldNames);
            values.add(mFieldTypes);
            if (null != mSecondaryList) {
                values.add(mSecondaryList);
            }
            values.add("SBR");

            cons = mSBRTW.construct();
            res = mSBRTW.writeConstruct((String) cons.get(0), values);
            RandomAccessFile sbrfoutput 
                = new RandomAccessFile(mPath 
                                       + "\\" 
                                       + CodeGeneratorUtil.makeClassName(mName) 
                                       + "SBRDB.java", "rw");
            sbrfoutput.write(res.getBytes());
            sbrfoutput.close();
        } catch (TemplateWriterException e) {
            throw new ParserException(e.getMessage());
        } catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
    }
}
