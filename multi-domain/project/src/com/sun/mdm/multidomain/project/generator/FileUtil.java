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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.util.Exceptions;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;

/**
 *
 * @author jlu
 */
public class FileUtil {
    
    /**
     * read a file into a String.
     *
     * @param inputFile The input file.
     * @return The content of the file in String
     * @throws IOException if an error occurs during reading file.
     */
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
    
    /**
     * Write a string to a file.
     *
     * @param outFile The output file.
     * @param content The content to be written to file.
     * @throws IOException if an error occurs during writing file.
     */
    public static void writeStringToFile(File outFile, String content ) throws IOException{

        BufferedWriter output = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
        output.write( content );
        output.flush();
        output.close();
    }
    
    /**
     * update a file.
     *
     * @param file The file to be updated.
     * @param content The content to be written to file.
     * @throws IOException if an error occurs during writing file.
     */
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
    
    /**
     * replace a token in the content of a file.
     *
     * @param file The file to update.
     * @param token The token to be replaced.
     * @param value The replacement value.
     * @throws IOException if an error occurs during writing file.
     */
    public static void replaceTokenInFile(File file, String token, String value) 
            throws IOException {
        String content = readFileToString(file);
        updateFile(file, content.replaceAll(token, value));        
    }
    
    /**
     * transform a XML file to String
     *
     * @param Document The XML document
     * @return The content of the XML in String
     * @throws TransformerException if an error occurs.
     */
    public static String transformXMLtoString(Document xmldoc) throws TransformerException{
        DOMConfiguration domConfig = xmldoc.getDomConfig();
        //domConfig.setParameter("format-pretty-print", "true");
        DOMSource domSource = new DOMSource(xmldoc);
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute("indent-number", 4);
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(new OutputStreamWriter(out));
        serializer.transform(domSource, result);
        return out.toString(); 
    }

}
