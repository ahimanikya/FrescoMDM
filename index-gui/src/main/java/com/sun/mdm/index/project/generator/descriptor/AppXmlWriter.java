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
package com.sun.mdm.index.project.generator.descriptor;

import java.io.File;
import java.util.ArrayList;
import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.ParserException;
import com.sun.mdm.index.parser.Utils;


/**
 * @author gzheng
 * @version
 */
public class AppXmlWriter {
    private TemplateWriter mTW = null;
    private final String mFileName = "application.xml";
    private final String mSunFileName = "sun-application.xml";
    private EIndexObject mEIndexObject;
    private String mPath;
    private String mEjbName;
    private String mWarName;  
   

    /**
     * @param path installation path
     * @param eo elephant object
     * @exception TemplateWriterException tempalte writer exception
     */
    public AppXmlWriter(String path, EIndexObject eo, String ejbName, String warName){        
        mEIndexObject = eo;
        mPath = path; 
        mEjbName = ejbName;
        mWarName = warName;        
    }

    /**
     * @exception ParserException parser exception
     */    
    public void write()
        throws ParserException {
        try {      
            //if there is a application.xml, then delete it. 
            File outFile = new File(mPath +File.separator + mFileName);
            if (outFile.exists()){
                outFile.delete();
            }
            //if there is a sun-application.xml, then delete it.
            outFile = new File(mPath +File.separator + mSunFileName);
            if (outFile.exists()){
                outFile.delete();
            }
            String templateFileName = "com/sun/mdm/index/project/generator/descriptor/application.xml.tmpl";            
            Utils.writeFile(mPath +File.separator + mFileName, writeProject(templateFileName));           
            templateFileName = "com/sun/mdm/index/project/generator/descriptor/sun-application.xml.tmpl";           
            Utils.writeFile(mPath +File.separator + mSunFileName, writeProject(templateFileName));
        } catch (TemplateWriterException e) {
            throw new ParserException(e.getMessage());
        } catch (ParserException e) {
            throw e;
        }
    }

    
    /**
     * @exception TemplateWriterException template writer exception
     * @return String ret string
     */
    public String writeProject(String templateFileName )
        throws TemplateWriterException {
        String strRet = null;
        try {
            TemplateWriter tw = new TemplateWriter(templateFileName);
            ArrayList cons = tw.construct();
            ArrayList values = new ArrayList();
            values.add(mEIndexObject.getName());
            values.add(mEjbName);
            values.add(mWarName);
            strRet = tw.writeConstruct((String) cons.get(0), values);
        } catch (TemplateWriterException ex) {
            throw ex;
        }
        return (strRet);
    } 
}
