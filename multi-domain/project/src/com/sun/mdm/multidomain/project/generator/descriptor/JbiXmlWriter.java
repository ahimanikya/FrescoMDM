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


public class JbiXmlWriter {
    private File mPath;
    private String mObjectName;
    
    /**
     * @param path installation path
     * @param eo EIndexObject object
     * @exception TemplateWriterException tempalte writer exception
     */
    public JbiXmlWriter(File path, String objectName){
        mPath = path;
        mObjectName = objectName;
    }

    /**
     * @exception ParserException parser exception
     */    
    public void write()
        throws TemplateWriterException {
        try {
            String templateFileName = "com/sun/mdm/multidomain/project/generator/descriptor/jbi.xml.tmpl";
            
            File jbiFile = new File(mPath, "jbi.xml");
            mPath.mkdirs();
            writeStringToFile(jbiFile, writeProject(templateFileName));
        } catch (IOException ex) {
            new TemplateWriterException(ex.getMessage());
        }
    }
    
    /**
     * @param templatefilename template file name
     * @exception TemplateWriterException template writer exception
     * @return String ret string
     */
    public String writeProject(String templateFileName ) 
        throws TemplateWriterException {
        String strRet = null;
        try {
            TemplateWriter tw = new TemplateWriter(templateFileName);
            ArrayList cons = tw.construct();
            ArrayList<String> values = new ArrayList<String>();
            values.add(mObjectName);
            strRet = tw.writeConstruct((String) cons.get(0), values);
        } catch (TemplateWriterException ex) {
            throw ex;
        }
        return strRet;
    }
    
    private void writeStringToFile(File outFile, String content ) throws IOException{

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
