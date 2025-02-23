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
package com.sun.mdm.index.project.generator.webservice;

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
 * @author jlong
 * @version
 */
class ObjectBeanWriter {
    private TemplateWriter mTW = null;
    private final String mPackage = "/com/sun/mdm/index/webservice";
    private ArrayList mFieldNames;
    private ArrayList mFieldTypes;
    private ArrayList mSecList;
    private String mName;
    private String mPath;
    private ArrayList mPrimary = new ArrayList();


    /**
     * @param path template path
     * @param eot elephant object type
     * @param installPath install path
     */
    public ObjectBeanWriter(String path, 
                            String parentName, 
                            NodeDef eot, 
                            ArrayList seclist) {
        try {
            if (null != seclist) {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/webservice/ObjectBean.java.tmpl");
            } else {
                mTW = new TemplateWriter("com/sun/mdm/index/project/generator/webservice/ObjectBeanLeaf.java.tmpl");
            }
        } catch (TemplateFileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        mPath = path + mPackage;
        mName = eot.getTag();
        mFieldNames = eot.createFieldNames();
        mFieldTypes = eot.createFieldTypes();
        mSecList = seclist;
        mPrimary.add(";");
    }


    /**
     * @exception ParserException
     * @todo Document this method
     */
    public void write()
        throws ParserException {
        try {
            ArrayList cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(mName);
            values.add(mFieldNames);
            values.add(mFieldTypes);
            
            if (null != mSecList) {
                values.add(mSecList);
                values.add(mPrimary);   
            }
            

            String res = mTW.writeConstruct((String) cons.get(0), values);
            RandomAccessFile foutput 
            = new RandomAccessFile(mPath + "/" + CodeGeneratorUtil.makeClassName(mName) + "Bean.java", "rw");
            foutput.write(res.getBytes("UTF-8"));
            foutput.close();
        } catch (TemplateWriterException e) {
            throw new ParserException(e.getMessage());
        } catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
    }
}
