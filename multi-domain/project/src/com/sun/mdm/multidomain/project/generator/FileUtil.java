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

package com.sun.mdm.multidomain.project.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author jlu
 */
public class FileUtil {
    
    public static String readFileToString(File inputFile) throws java.io.IOException{
        StringBuffer strBuffer = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            strBuffer.append(buf, 0, numRead);
        }
        reader.close();
        return strBuffer.toString();
    }
    
    public static void writeStringToFile(File outFile, String content ) throws IOException{

        BufferedWriter output = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
        output.write( content );
        output.flush();
        output.close();
    }
    
    public static void updateFile(File file, String content) throws IOException {
        String fileName = file.getAbsolutePath();
        File tempFile = new File(fileName + ".tmp");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        file.renameTo(tempFile);
        file = new File(fileName);
        try {
            writeStringToFile(file, content);
        } catch (IOException ex) {
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
            throw ex;
        } finally {
            tempFile = new File(fileName + ".tmp");
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    
    public static void replaceTokenInFile(File file, String token, String value) 
            throws IOException {
        String content = readFileToString(file);
        updateFile(file, content.replaceAll(token, value));        
    }

}
