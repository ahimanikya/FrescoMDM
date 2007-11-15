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
package com.sun.mdm.index.parser;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import com.sun.mdm.index.util.Localizer;


/**
 * @author gzheng
 * @version
 */
public class Utils {
    
    private transient static final Localizer mLocalizer = Localizer.get();
    
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
     * @param path target file path
     * @exception ParserException parser exception
     */
    public static void createFolder(String path) throws ParserException {
        try {
            java.io.File newPath = new java.io.File(path);
            
            if (newPath.exists()) {
                if (recursiveDelete(newPath) == false) {
                    throw new ParserException(mLocalizer.t("PAR500: Failed to " + 
                                                     "delete the directory: {0}", path));
                }
            }
            
            newPath = new java.io.File(path);
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\com\\stc\\eindex\\objects");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\com\\stc\\eindex\\collab");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\com\\stc\\eindex\\objects\\validation");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\com\\stc\\eindex\\ops");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\ddl");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\schema");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\config");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\application");
            newPath.mkdirs();
            newPath = new java.io.File(path + "\\usersrc\\java");
            newPath.mkdirs();
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR501: Failed to set " + 
                                        "up the temporary build directory: {0}:{1}", 
                                        path, e));
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
     * @return ret EIndexObject
     * @throws ParserException exception
     */
    public static EIndexObject parseEIndexObject(String xmlPath) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(new File(xmlPath));
            EIndexObject ret = new EIndexObject();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR502: Failed to parse: {0}:{1}", 
                                                    xmlPath, e));
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret EIndexObject
     * @throws ParserException exception
     */
    public static EIndexObject parseEIndexObject(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            EIndexObject ret = new EIndexObject();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR503: Failed to parse: {0}:{1}", 
                                                    xmlSource, e));
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret EDMType
     * @throws ParserException exception
     */
    public static EDMType parseEDMType(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            EDMType ret = new EDMType();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR510: Failed to parse " +
                                                "EDM type: {0}:{1}", 
                                                xmlSource, e));
        }
    }
    
    /**
     * @param xmlPath xml file path
     * @return ret UpdateType
     * @throws ParserException exception
     */
    public static UpdateType parseUpdateType(String xmlPath) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(new File(xmlPath));
            UpdateType ret = new UpdateType();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR504: Failed to parse " +
                                                "Update type: {0}:{1}", 
                                                xmlPath, e));
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret UpdateType
     * @throws ParserException exception
     */
    public static UpdateType parseUpdateType(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            UpdateType ret = new UpdateType();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR505: Failed to parse " +
                                                "Update type: {0}:{1}", 
                                                xmlSource, e));
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret QueryType
     * @throws ParserException exception
     */
    public static QueryType parseQueryType(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            QueryType ret = new QueryType();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR506: Failed to parse " +
                                                "Query type: {0}:{1}", 
                                                xmlSource, e));
        }
    }
    
    /**
     * @param xmlSource object definition
     * @return ret MatchFieldDef
     * @throws ParserException exception
     */
    public static MatchFieldDef parseMatchFieldDef(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MatchFieldDef ret = new MatchFieldDef();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR507: Failed to parse " +
                                                "Match Field Definition: {0}:{1}", 
                                                xmlSource, e));
        }
    }
    
    
    /**
     * @param xmlSource object definition
     * @return ret MasterType
     * @throws ParserException exception
     */
    public static MasterType parseMasterType(InputSource xmlSource) throws ParserException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            mDoc = docBuilder.parse(xmlSource);
            MasterType ret = new MasterType();
            ret.parse(mDoc);
            return ret;
        } catch (Exception e) {
            throw new ParserException(mLocalizer.t("PAR508: Failed to parse " +
                                                "Master type: {0}:{1}", 
                                                xmlSource, e));
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
            foutput.write(data.getBytes());
            foutput.close();
        } catch (IOException e) {
            throw new ParserException(mLocalizer.t("PAR509: Failed to write " +
                                                "file: {0}:{1}", 
                                                path, e));
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
        File file = new File("C:\\rep0429\\edesigner\\bin\\deploy");
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
