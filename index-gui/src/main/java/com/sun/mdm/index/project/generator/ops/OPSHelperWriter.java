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
import com.sun.mdm.index.parser.ParserException;


/**
 * @author gzheng
 * @version
 */
class OPSHelperWriter {
    private TemplateWriter mTW = null;
    private final String mPackage = "/com/sun/mdm/index/ops";
    private String mPath;
    private ArrayList mNodeList;


    /**
     * @param path template path
     * @param nodeList node list
     * @param installPath install path
     */
    public OPSHelperWriter(String path, 
                           ArrayList nodeList) {
        try {
            mTW = new TemplateWriter("com/sun/mdm/index/project/generator/ops/OPSInitHelper.java.tmpl");
        } catch (TemplateFileNotFoundException ex) {
            ex.printStackTrace();
        }
        mPath = path + mPackage;
        mNodeList = new ArrayList();
        for (int i = 0; i < nodeList.size(); i++) {
            mNodeList.add((String) nodeList.get(i) + "DB");
            mNodeList.add((String) nodeList.get(i) + "SBRDB");
        }
    }


    /**
     * @exception ParserException parser exception
     */
    public void write()
        throws ParserException {
        try {
            ArrayList cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(mNodeList);

            String res = mTW.writeConstruct((String) cons.get(0), values);
            RandomAccessFile foutput 
                = new RandomAccessFile(mPath 
                                       + "/OPSInitHelper.java", "rw");
            foutput.write(res.getBytes("UTF-8"));
            foutput.close();
        } catch (TemplateWriterException e) {
            throw new ParserException(e.getMessage());
        } catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
    }
}
