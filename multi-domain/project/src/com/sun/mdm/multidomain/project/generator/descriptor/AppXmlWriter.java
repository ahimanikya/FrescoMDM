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
package com.sun.mdm.multidomain.project.generator.descriptor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.sun.mdm.multidomain.project.generator.TemplateWriter;
import com.sun.mdm.multidomain.project.generator.exception.TemplateWriterException;

/**
 * AppXmlWriter class.
 * @author mdm team.
 */
public class AppXmlWriter {
    private final String mFileName = "application.xml";
   
    private File mPath;
    private String mAppName;    
    private String mEjbName;
    private String mWarName;  
   

    public AppXmlWriter(File path, String appName, String ejbName, String warName){        
        mPath = path; 
        mAppName = appName;
        mEjbName = ejbName;
        mWarName = warName;        
    }

    public void write()
        throws TemplateWriterException {
        try {                  
            String templateFileName = "com/sun/mdm/multidomain/project/generator/descriptor/application.xml.tmpl";   
            
            File appFile = new File(mPath, mFileName);
            mPath.mkdirs();
            writeStringToFile(appFile, writeProject(templateFileName));            
        } catch (IOException ioe) {
            new TemplateWriterException(ioe.getMessage());
        } 
    }
    
    public String writeProject(String templateFileName )
        throws TemplateWriterException {
        String strRet = null;
        try {
            TemplateWriter tw = new TemplateWriter(templateFileName);
            ArrayList cons = tw.construct();
            ArrayList values = new ArrayList();
            values.add(mAppName);
            values.add(mEjbName);
            values.add(mWarName);
            strRet = tw.writeConstruct((String) cons.get(0), values);
        } catch (TemplateWriterException ex) {
            throw ex;
        }
        return strRet;
    } 
    
    private void writeStringToFile(File outFile, String content) 
        throws IOException{
        BufferedWriter output = new BufferedWriter(new FileWriter(outFile));
        byte[] utf8Bytes = content.getBytes("UTF-8");
        String utf8String = new String(utf8Bytes, "UTF-8");        
        try{
          output.write( utf8String );
        }finally {
          output.close();
        }
    }
    
}
