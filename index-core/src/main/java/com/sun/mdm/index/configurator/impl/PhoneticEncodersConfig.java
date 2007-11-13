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
package com.sun.mdm.index.configurator.impl; 

import java.util.HashMap;
import java.util.logging.Level;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Phonetic encoder configuration information.
 * Handles the parsing of the Phonetic Encoder configuration and exposes the settings.
 *
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class PhoneticEncodersConfig 
        implements ConfigurationInfo {

    /** Module Name to use with the Configuration Service to load the Phonetic Encoder configuration */
    public static final String PHONETIC_ENCODERS = "PhoneticEncoders";

    // HashMap from the encoding type to the configured PhoneticEncoder implementation class
    private HashMap encodersClasses;
    private Document doc;

    private String moduleName;
    private String parserClass;
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
    

    /**
     * Creates new StandardizationConfiguration
     */
    public PhoneticEncodersConfig() {
    }

    /**
     * Returns the hashmap for all the configured available encoders. The
     * hashmap maps the encoding type to the fully qualified class name of the
     * PhoneticEncoder implementation to use.
     *
     * @return configured encoder implementation classes
     */
    public HashMap getEncodersClasses() {
        return encodersClasses;
    }

    /**
     * Getter for ModuleType attribute of the PhoneticEncodersConfig object.
     *
     * @return the type of this configuration module
     */
    public String getModuleType() {
        return PHONETIC_ENCODERS;
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

            encodersClasses = new java.util.HashMap();

            moduleName = elem.getAttribute(Attributes.MODULE_NAME);
            parserClass = elem.getAttribute(Attributes.PARSER_CLASS);

            // Parse the matching definition for each system object
            NodeList encoderElements = elem.getElementsByTagName(Tags.ENCODER);
            int noOfElements = encoderElements.getLength();
            for (int elementCount = 0; elementCount < noOfElements; elementCount++) {
                Element elementToParse = (Element) encoderElements.item(elementCount);

                NodeList encodingTypeElements = elementToParse.getElementsByTagName(Tags.ENCODING_TYPE);
                String encodingTypeName = getFirstStrElementValue(encodingTypeElements);

                NodeList encoderImplClassElements = elementToParse.getElementsByTagName(Tags.ENCODER_IMPL_CLASS);
                String encoderImplClassName = getFirstStrElementValue(encoderImplClassElements);

                encodersClasses.put(encodingTypeName, encoderImplClassName);
            }
            mLogger.info(mLocalizer.x("CFG022: The Phonetic Encoders Configuration encoder mappings are: {0}", encodersClasses));
        } catch (Exception ex) {
            throw new ConfigurationException(mLocalizer.t("CFG510: Failed to parse phonetic encoders configuration: {0}", ex));
        }
    }

    /**
     * Returns the #text value of the first XML node in the passed in NodeList..
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
     * Attributes that correspond to the XML attribute names.
     */
    public static final class Attributes {
        /** name attribute */
        public static final String NAME = "name";
        /** module attribute */
        public static final String MODULE_NAME = "module-name";
        /** parser class attribute */
        public static final String PARSER_CLASS = "parser-class";
    }

    /**
     * Tags that correspond to the XML node names.
     */
    public static final class Tags {
        /** encoder tag */
        public static final String ENCODER = "encoder";
        /** encoding type tag */
        public static final String ENCODING_TYPE = "encoding-type";
        /** encoder implementation class tag */
        public static final String ENCODER_IMPL_CLASS = "encoder-implementation-class";
    }
}
