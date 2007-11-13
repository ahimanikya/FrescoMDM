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
package com.sun.mdm.index.configurator.impl.validation;

import java.util.Hashtable;
import java.util.logging.Level;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * @author jwu
 */
public class ValidationConfiguration implements ConfigurationInfo {

    /** module name */
    public static final String VALIDATION_CONFIGURATION = "ValidationConfig";
    private final Hashtable hCustomValidationByRule = new Hashtable();
    private final Hashtable hCustomValidationByObject = new Hashtable();
    static final String TAG_RULES = "rules";
    static final String TAG_RULE = "rule";

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of ValidationConfiguration */
    public ValidationConfiguration() {
    }


    /**
     * Get module type.
     *
     * @return module type.
     */
    public String getModuleType() {
        return VALIDATION_CONFIGURATION;
    }


    /**
     * Get list of validators by object.
     *
     * @return Hashtable, the registry for custom validation.
     */
    public Hashtable getValidatorsByObject() {
        return hCustomValidationByObject;
    }


    /**
     * Get list of validators by rule.
     *
     * @return Hashtable, the list of custom validator.
     */
    public Hashtable getValidatorsByRule() {
        return hCustomValidationByRule;
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

        NodeList ruleTypes = node.getChildNodes();
        int typeCount = ruleTypes.getLength();
        for (int i = 0; i < typeCount; i++) {
            Node typeNode = ruleTypes.item(i);
            if (typeNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = typeNode.getNodeName();
                if (TAG_RULES.equals(nodeName)) {
                    NodeList ruleList = typeNode.getChildNodes();
                    int ruleCount = ruleList.getLength();
                    for (int j = 0; j < ruleCount; j++) {
                        Node ruleNode = ruleList.item(j);
                        if (ruleNode.getNodeType() == Node.ELEMENT_NODE) {
                            NamedNodeMap attributes = ruleNode.getAttributes();
                            String ruleName = attributes.getNamedItem("name").getNodeValue();
                            String objectName = attributes.getNamedItem("object-name").getNodeValue();
                            String className = attributes.getNamedItem("class").getNodeValue();
                            Object validator;
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine(mLocalizer.x("CFG041: Validation class for {0} = {1}", objectName.toUpperCase(), className)); 
                            }
                            try {
                                Class validationClass = Class.forName(className);
                                validator = validationClass.newInstance();
                            } catch (Exception e) {
                                throw new ConfigurationException(mLocalizer.t("CFG547: Failed to " + 
                                            "retrieve an instance of ValidationConfiguration: {0}", e));
                            }
                            hCustomValidationByRule.put(ruleName.toUpperCase(), validator);
                            hCustomValidationByObject.put(objectName.toUpperCase(), validator);
                        }
                    }
                }
            }
        }
        mLogger.info(mLocalizer.x("CFG016: The CustomValidationByRule class is: {0}", hCustomValidationByRule));
        mLogger.info(mLocalizer.x("CFG017: The CustomValidationByObject class is: {0}", hCustomValidationByObject));
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Validation configuration parsed"); 
        }
    }

}
