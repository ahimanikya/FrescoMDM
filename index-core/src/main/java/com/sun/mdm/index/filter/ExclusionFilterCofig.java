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
package com.sun.mdm.index.filter;

import com.sun.mdm.index.configurator.ConfigurationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.Logger;


/**
 *  The ExclusionFilterCofig class will parse the filter Config file.
 * 
 */
public class ExclusionFilterCofig implements ConfigurationInfo {

    private transient final Logger mLogger = Logger.getLogger(this.getClass().
            getName());
    private transient Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of ExclusionFilterCofig */
    public ExclusionFilterCofig() {
    }
    
    private static ArrayList sbrList = new ArrayList();
    private static ArrayList matchingList = new ArrayList();
    private static ArrayList blockingList = new ArrayList();

    public ArrayList getSbrList() {
        return sbrList;
    }

    public ArrayList getmatchingList() {
        return matchingList;
    }

    public ArrayList getBlockingList() {
        return blockingList;
    }

    public int init() {
        return 0;
    }

    public int finish() {
        return 0;
    }

    public String getModuleType() {
        return "ExclusionFilter";
    }

    /** Method used to parse the Node.
     * @param node the root node of the filter Confg file.
     * @exception ConfigurationException See ConfigurationInfo
     */
    public void parse(Node node) throws ConfigurationException {

        NodeList optionList = node.getChildNodes();

        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {
            Node option = optionList.item(i);
            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();
                if (optionName.equals(FilterConstants.TAG_FIELD)) {

                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("parseField(option) : " + optionName);
                    }
                    parseField(option);

                } else {
                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("Unknown tag: " + optionName);
                    }

                }
            }
        }
    }

    /**
     * The method used to parse the Field Node of the config file.
     * @param node the Field Node of the config file.
     */
    
    private void parseField(Node node) {

        NamedNodeMap attributes = node.getAttributes();
        String sbrValue = attributes.getNamedItem(FilterConstants.ATR_SBR).
                getNodeValue();
        String matchValue = attributes.getNamedItem(FilterConstants.ATR_MATCHING).
                getNodeValue();
        String blockvalue = attributes.getNamedItem(FilterConstants.ATR_BLOCKING).
                getNodeValue();
        HashMap map = new HashMap();
        String fieldName = null;
        ArrayList fieldValues = new ArrayList();
        ArrayList values = new ArrayList();


        NodeList optionList = node.getChildNodes();
        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {
            Node option = optionList.item(i);
            Object ofield = null;

            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();

                if (optionName.equals(FilterConstants.TAG_NAME)) {
                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("the name is: " + optionName);
                    }
                    fieldName = parseName(option);
                    ofield = fieldName;
                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug(" fieldName :  " + fieldName);
                    }

                } else if (optionName.equals(FilterConstants.TAG_VALUE)) {
                    values = parseValue(option);
                }
            }
        }

        if (fieldName != null && values.size() != 0) {
            map.put(fieldName, values);
        }
        if (sbrValue.equalsIgnoreCase("true")) {
            sbrList.add(map);
        }

        if (matchValue.equalsIgnoreCase("true")) {
            matchingList.add(map);
        }

        if (blockvalue.equalsIgnoreCase("true")) {
            blockingList.add(map);
        }

    }

    /**
     * The method used to parse the Name node of the filterConfig file.
     * @param node the Name node
     * @return String the name value 
     */
    
    private String parseName(Node node) {

        String name = null;
        Node nameNode = node.getFirstChild();
        if (nameNode != null) {
            name = nameNode.getNodeValue();

        } else {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("the name is null ");
            }
        }
        return name;
    }

    /**
     * The method used to parse the value node of the filterConfig file.
     * @param node the value node
     * @return String the name value 
     */
    
    private ArrayList parseValue(Node node) {
        ArrayList values = new ArrayList();
        NodeList optionList = node.getChildNodes();
        int builderCount = optionList.getLength();

        for (int i = 0; i < builderCount; i++) {

            Node option = optionList.item(i);
            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();

                if (optionName.equals(FilterConstants.TAG_FILE)) {
                    values = parseFileTest(option);

                } else if (optionName.equals(FilterConstants.TAG_FIELD_VALUES)) {
                    String value = parseFieldValues(option);
                    values.add(value);
                }
            }
        }
        return values;
    }

     /**
     * The method used to parse the FlatFile node of the filterConfig file.
     * @param node the FlatFile node
     * @return ArrayList the value from the file.
     */
    
    private ArrayList parseFileTest(Node node) {
        ArrayList fileValueList = new ArrayList();
        NamedNodeMap attributes = node.getAttributes();
        String delimiter = attributes.getNamedItem(FilterConstants.ATR_DELIMITER).
                getNodeValue();
        NodeList optionList = node.getChildNodes();
        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {

            Node option = optionList.item(i);
            String optionName = option.getNodeName();
            if (optionName.equals(FilterConstants.TAG_FILE_NAME)) {
                String filename = parseFileName(option);
                fileValueList = parseFlatFile(filename, delimiter);
            }
        }
        return fileValueList;
    }

     /**
     * The method used to parse the FieldValues node of the filterConfig file.
     * @param node the FieldValues node
     * @return string the value from the FieldValue.
     */
    
    private String parseFieldValues(Node node) {

        NodeList optionList = node.getChildNodes();
        String values = null;
        int builderCount = optionList.getLength();
        for (int i = 0; i < builderCount; i++) {
            Node option = optionList.item(i);
            String optionName = option.getNodeName();
            values = option.getNodeValue();
        }
        return values;
    }
    
   /**
     * The method used to parse the FileName node of the filterConfig file.
     * @param node the FileName node
     * @return string the FileName.
     */
    
    private String parseFileName(Node node) {

        String name = null;
        Node nameNode = node.getFirstChild();
        if (nameNode != null) {
            name = nameNode.getNodeValue();
        } else {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("the name is null ");
            }
        }
        return name;
    }
    /**
     * 
     * @param fileName
     * @param delimiter
     * @return
     */

    public ArrayList parseFlatFile(String fileName, String delimiter) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        ArrayList fileValues = new ArrayList();
        try {

            ConfigurationService confService = ConfigurationService.getInstance();
            is = confService.getConfigFileStream(fileName);
            isr = new InputStreamReader(is);

            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, delimiter);
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("line  :" + line);
                }

                while (tokenizer.hasMoreTokens()) {
                    String ss = tokenizer.nextToken();
                    fileValues.add(ss);
                }
            }

        } catch (InstantiationException ex) {
            mLogger.error(mLocalizer.x("EFC002: Failed to intantiate the ConfigurationService "),
                    ex);
        } catch (FileNotFoundException ex) {
            mLogger.error(mLocalizer.x("EFC001: The file {0}  is not found ",
                    fileName), ex);
        } catch (IOException ex) {
            mLogger.error(mLocalizer.x("EFC003: IOException has encounterd"), ex);
        } finally {
            try {
                br.close();
                isr.close();
                is.close();
            } catch (IOException ex) {
                mLogger.error(mLocalizer.x("EFC003: IOException has encounterd"),
                        ex);
            }
        }
        return fileValues;
    }
}
