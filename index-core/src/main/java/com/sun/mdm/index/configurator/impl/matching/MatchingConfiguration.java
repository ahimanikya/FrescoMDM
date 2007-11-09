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
package com.sun.mdm.index.configurator.impl.matching;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.objects.metadata.MetaDataService;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Handles the parsing of the Matching configuration and defining how to match
 * objects.
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class MatchingConfiguration implements ConfigurationInfo {

    /** Module Name to use with the Configuration Service to load the Matching configuration */
    public static final String MATCHING = "Matching";

    private HashMap sysObjs;
    private Document doc;

    private String moduleName;
    private String parserClass;
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    


    /** Creates new MatchingConfiguration */
    public MatchingConfiguration() {
    }

    /**
     * Getter for ModuleType attribute of the
     * MatchingConfiguration object.
     * 
     * @return the module type of this configuration info.
     */
    public String getModuleType() {
        return MATCHING;
    }

    /**
     * Gets the system object matching configuration for the specified 
     * system object.
     *
     * @param sysObjName The system object name for which to retrieve the 
     * matching configuration.
     * @return the matching configuration for a system object.
     */
    public SystemObjectMatching getSystemObjectMatching(String sysObjName) {
        SystemObjectMatching matching = (SystemObjectMatching) sysObjs.get(sysObjName);
        return matching;
    }

    /** Finish.
     *
     * @return result code.
     */
    public int finish() {
        return 0;
    }

    /** Initialize.
     *
     * @return result code.
     */
    public int init() {
        return 0;
    }

    /** Parse an XML node.
     *
     * @param node XML node starting the configuration node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public void parse(Node node)
        throws ConfigurationException {
        try {
            doc = node.getOwnerDocument();
            Element elem = (Element) node;

            sysObjs = new java.util.HashMap();

            moduleName = elem.getAttribute(Attributes.MODULE_NAME);
            parserClass = elem.getAttribute(Attributes.PARSER_CLASS);

            // Parse the matching definition for each system object
            NodeList matchSystemObjects = elem.getElementsByTagName(Tags.MATCH_SYSTEM_OBJECT);
            int noOfElements = matchSystemObjects.getLength();
            for (int elementCount = 0; elementCount < noOfElements; elementCount++) {
                Element elementToParse = (Element) matchSystemObjects.item(elementCount);
                SystemObjectMatching sysObjMatch = parseMatchSystemObject(elementToParse);
                sysObjs.put(sysObjMatch.getQualifiedName(), sysObjMatch);
            }
            mLogger.info(mLocalizer.x("CFG024: MatchingConfiguration: SystemObjects mappings are: {0}", sysObjs));
        } catch (Exception ex) {
            throw new ConfigurationException("Failed to parse Matching configuration:" + ex.getMessage(), ex);
        }
    }

    /**
     * Returns the #text value of the first XML node in the passed in NodeList.
     *
     * @param nodeList XML nodeList.
     * @return #text value as a String object, null if didn't exist.
     */
    private String getFirstStrElementValue(NodeList nodeList) {
        String result = null;

        if (nodeList != null && nodeList.getLength() > 0) {
            Node aNode = nodeList.item(0);
            result = getStrElementValue(aNode);
        }
        return result;
    }

    /**
     * Returns the #text value of an XML node.
     *
     * @param node XML node.
     * @return #text value as a String object, null if it doesn't exist.
     */
    private String getStrElementValue(Node node) {
        String result = null;
        Node tnode = node.getFirstChild();
        if (tnode != null) {
            result = tnode.getNodeValue();
        }
        return result;
    }

    /**
     * Parses a XML element node representing a match column for matching.
     *
     * @param matchColumnElement XML element representing a "match-column" node.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     * @return a MatchColumn object
     */
    private MatchColumn parseMatchColumn(Element matchColumnElement)
            throws ConfigurationException {
                
        MatchColumn matchColumn = new MatchColumn();

        if (matchColumnElement != null) {
            int matchOrder = 0;

            NodeList columnNameNodes = matchColumnElement.getElementsByTagName(Tags.COLUMN_NAME);
            String columnName = getFirstStrElementValue(columnNameNodes);

            // Ensure the match column is defined relative to the top level,
            // pointing to the relevant SBR. The query subsystem expects this.
            String sbrColumnName = MetaDataService.getSBRPath(columnName);
            if (sbrColumnName != null) {
                columnName = sbrColumnName;
            }

            NodeList matchTypeNodes = matchColumnElement.getElementsByTagName(Tags.MATCH_TYPE);
            String matchType = getFirstStrElementValue(matchTypeNodes);

            NodeList matchOrderNodes = matchColumnElement.getElementsByTagName(Tags.MATCH_ORDER);
            String matchOrderStr = getFirstStrElementValue(matchOrderNodes);
            if (matchOrderStr != null) {
                matchOrder = Integer.parseInt(matchOrderStr);
            }

            matchColumn.setQualifiedName(columnName);
            matchColumn.setMatchType(matchType);
            matchColumn.setMatchOrder(matchOrder);
        }

        return matchColumn;
    }

    /**
     * Parses a XML element node representing a system object match definition.
     *
     * @param sysObjMatchElement an XML element defining system object matching.
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     * @return a SystemObjectMatching object.
     */
    private SystemObjectMatching parseMatchSystemObject(Element sysObjMatchElement)
            throws ConfigurationException {
                
        String sysObjName = null;
        ArrayList matchColumns = new ArrayList();

        NodeList sysObjNameNodes = sysObjMatchElement.getElementsByTagName(Tags.OBJECT_NAME);
        sysObjName = getFirstStrElementValue(sysObjNameNodes);

        NodeList matchColumnsElements = sysObjMatchElement.getElementsByTagName(Tags.MATCH_COLUMNS);
        if (matchColumnsElements != null && matchColumnsElements.getLength() > 0) {
            Element matchColumnsElement = (Element) matchColumnsElements.item(0);

            NodeList matchColumnElements = matchColumnsElement.getElementsByTagName(Tags.MATCH_COLUMN);
            int noOfElements = matchColumnElements.getLength();
            for (int elementCount = 0; elementCount < noOfElements; elementCount++) {
                MatchColumn matchColumn = parseMatchColumn((Element) matchColumnElements.item(elementCount));
                matchColumns.add(matchColumn);
            }
        }
        
        SystemObjectMatching sysObjMatch = new SystemObjectMatching(matchColumns, sysObjName);

        return sysObjMatch;
    }

    /**
     * Attributes that correspond to the XML attribute names.
     */
    public static final class Attributes {
        /** name attribute */
        public static final String NAME = "name";
        /** module name attribute */
        public static final String MODULE_NAME = "module-name";
        /** parser class attribute */
        public static final String PARSER_CLASS = "parser-class";
    }

    /**
     * Tags that correspond to the XML node names.
     */
    public static final class Tags {
        /** match system object tag */
        public static final String MATCH_SYSTEM_OBJECT = "match-system-object";
        /** object name tag */
        public static final String OBJECT_NAME = "object-name";
        /** match columns tag */
        public static final String MATCH_COLUMNS = "match-columns";
        /** match column tag */
        public static final String MATCH_COLUMN = "match-column";
        /** column name tag */
        public static final String COLUMN_NAME = "column-name";
        /** match type tag */
        public static final String MATCH_TYPE = "match-type";
        /** match order tag */
        public static final String MATCH_ORDER = "match-order";
    }

}
