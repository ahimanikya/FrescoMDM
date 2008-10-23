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
package com.sun.mdm.multidomain.parser;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;

import com.sun.mdm.multidomain.util.Logger;

/**
 * @author gzheng
 * @version
 */
public class Utils {
    
    //private transient static final Localizer mLocalizer = Localizer.get();
    private static final Logger mLog = Logger.getLogger(
            Utils.class.getName()
        
        );

    private static Document mDoc;
    public static final String LINE = "\r";
    public static final String TAB = "    ";    // each tab = 4 spaces
    public static final String TAB2 = "        ";
    public static final String TAB3 = "            ";
    public static final String TAB4 = "                ";
    public static final String TAB5 = "                    ";
    public static final String TAB6 = "                        ";
    public static final String TAB7 = "                            ";
    public static final String TAB8 = "                                ";
    public static final String LEFT = "<";
    public static final String RIGHT = ">";
    public static final String mTagParameters = "parameters";
    public static final String mTagParameter = "parameter";
    public static final String mTagParameterName = "parameter-name";
    public static final String mTagParameterType = "parameter-type";
    public static final String mTagParameterValue = "parameter-value";

    /**
     * @param objectName object name
     * @return String ret string
     */
    public static String getSBRTableName(String objectName) {
        return "EJB" + objectName + "SBR";
    }

    /**
     * @param node node
     * @return String ret string
     */
    public static String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();
        
        if (null != tnode) {
            return tnode.getNodeValue();
        } else {
            return "";
        }
    }
    
    
    /**
     * Recursively deletes a directory.
     *
     * @param d a file or directory to delete
     * @return true if succeeded, or falseotherwise;
     */
    public static boolean recursiveDelete(File d) {
        // If d is not a directory, just delete it.
        if (!d.isDirectory())
            if (d.delete() == false) {
                return false;
            } else {
                return true;
            }
        else {
            // Construct a file list.
            File[] ar = d.listFiles();
            
            // Iterate through the file list.
            for (int i = 0; i < ar.length; i++) {
                // Recurse into directories. Deep directories will need a lot of stack space for this.
                if (ar[i].isDirectory()) {
                    if (!recursiveDelete(ar[i])) {
                        return false; // Failed!
                    }
                }
                else {
                    // Delete a file.
                    if (!ar[i].delete()) {
                        return false; // Failed!
                    }
                }
            }
            
            //Delete the empty directory
            if (!d.delete()) {
                return false;
            } else {
                return true;
            }            
        }
    }
    
    
    /**
     * @param xmlPath xml file path
     * @return ret RelationshipType
     * @throws ParserException exception
     */
    public static MultiDomainModel parseMultiDomainModel(String xmlPath) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(new File(xmlPath));
            MultiDomainModel ret = new MultiDomainModel();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException("PAR502: Failed to parse: " + 
                                                    xmlPath + ":" + e.getMessage());
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret RelationshipType
     * @throws ParserException exception
     */
    public static MultiDomainModel parseMultiDomainModel(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MultiDomainModel ret = new MultiDomainModel();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException("PAR503: Failed to parse MultiDomainModel: " + 
                                                    xmlSource.getPublicId() + ":" + e.getMessage());
        }
    }


        /**
     * @param xmlSource object definition
     * @return ret RelationshipType
     * @throws ParserException exception
     */
    public static MultiDomainWebManager parseMultiDomainWebManager(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MultiDomainWebManager ret = new MultiDomainWebManager();
            ret.parseNode(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException("PAR503: Failed to parse MultiDomainWebManager: " + 
                                                    xmlSource.getPublicId() + ":" + e.getMessage());
        }
    }
/**
     * @param xmlSource object definition
     * @return ret EIndexObject
     * @throws ParserException exception
     */
    public static MiObject parseMiObject(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MiObject ret = new MiObject();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException("PAR503: Failed to parse: {0}:{1}" + 
                                                    xmlSource.getPublicId() + ":" + e.getMessage());
        }
    }
    
/**
     * @param xmlSource object definition
     * @return ret EIndexObject
     * @throws ParserException exception
     */
    public static MIQueryBuilder parseMiQueryBuilder(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MIQueryBuilder ret = new MIQueryBuilder();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException("PAR503: Failed to parse: {0}:{1}" + 
                                                    xmlSource.getPublicId() + ":" + e.getMessage());
        }
    }    

    /**
     * @param path file path
     * @param data binary data
     * @exception ParserException parser exception
     */
    public static void writeFile(String path, String data)
    throws ParserException {
        if (null == path) {
            new ParserException("Parser Util: invalid path: " + path);
        }
        
        try {
            RandomAccessFile foutput = new RandomAccessFile(path, "rw");
            foutput.setLength(0);
            foutput.write(data.getBytes("UTF-8"));
            foutput.close();
        } catch (IOException e) {
            throw new ParserException("PAR509: Failed to write " +
                                                "file: " + path + ":" + e.getMessage());
        }
    }
    
    public static String quoteAttribute(String name, String value) {
        return " " + name + "=" + "\"" + value + "\"";
    }
    
    public static String quoteElementAttribute(String elementName, String name, String value) {
        return "<" + elementName + "  " + name + "=" + "\"" + value + "\">";
    }
    
    public static String endTag(String name) {
        return "</" + name + ">" + LINE;
    }
    
    public static String startTagNoLine(String name) {
        return "<" + name + ">";
    }
    
    public static String startTag(String name) {
        return "<" + name + ">" + LINE;
    }

    public static void main(String args[]) {
        File file = new File("/temp/deploy");
        recursiveDelete(file);
    }
    
    public static Parameter createParameter(String name, String type, String value) {
        Parameter parameter = new Parameter(name, type, value);
        return parameter;
    }
    
    public static Utils.Parameter parseParameter(Node node) {
        String pn = null;
        String pt = null;
        String pv = null;
        
        NodeList nl4 = node.getChildNodes();
        for (int i4 = 0; i4 < nl4.getLength(); i4++) {
            if (nl4.item(i4).getNodeType() == Node.ELEMENT_NODE) {
                String name4 = ((Element) nl4.item(i4)).getTagName();
                if (mTagParameterName.equals(name4)) {
                    pn = Utils.getStrElementValue(nl4.item(i4));
                } else if (mTagParameterType.equals(name4)) {
                    pt = Utils.getStrElementValue(nl4.item(i4));
                } else if (mTagParameterValue.equals(name4)) {
                    pv = Utils.getStrElementValue(nl4.item(i4));
                }
            }
        }
        if (pn != null && pt != null && pv != null) {
            Utils.Parameter parameter = Utils.createParameter(pn, pt, pv);
            return parameter;
        } else {
            return null;
        }
    }
    
    public static class Parameter {
        String name;
        String type;
        String value;
               
        public Parameter(String name, String type, String value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
        
        public String getName() {
            return name;
        }
        
        public String getType() {
            return type;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String val) {
            value = val;
        }
    }

}
