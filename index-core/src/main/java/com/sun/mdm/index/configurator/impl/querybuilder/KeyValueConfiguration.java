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
package com.sun.mdm.index.configurator.impl.querybuilder;

import java.util.HashMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Key Value Configuration.
 * @author dcidon
 */
public class KeyValueConfiguration implements ConfigurationInfo {

    private final HashMap keyValueMap = new HashMap();

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /** Creates new KeyValueConfiguration */
    public KeyValueConfiguration() {
    }


    /** Get boolean option value.
     *
     * @param key option name
     * @return true or false
     * @throws ConfigurationException if there is an error in parsing the 
     * configuration XML file.
     */
    public boolean getBooleanValue(String key)
        throws ConfigurationException {
        String val = (String) keyValueMap.get(key);
        if (val == null) {
            return false;
        } else {
            if (val.equalsIgnoreCase("true")) {
                return true;
            } else if (val.equalsIgnoreCase("false")) {
                return false;
            } else {
                throw new ConfigurationException(mLocalizer.t("CFG535: Invalid boolean value: {0}", val));
            }
        }
    }


    /** Get module type name.
     *
     * @return module type
     */
    public String getModuleType() {
        return "KeyValueConfiguration";
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
    public void parse(Node topNode)
            throws ConfigurationException {
                
        //Sub-elements are option elements
        try {
            NodeList list = topNode.getChildNodes();
            int count = list.getLength();
            for (int i = 0; i < count; i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NamedNodeMap attributes = node.getAttributes();
                    String key = attributes.getNamedItem("key").getNodeValue();
                    String value = attributes.getNamedItem("value").getNodeValue();

                    keyValueMap.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG536: KeyConfiguration could " + 
                                        "not parse the XML configuration node: {0}", e));
        }
    }


    /** String representation of the KeyValueCOnfiguration class.
     *
     * @return String representation of the KeyValueCOnfiguration class.
     */
    public String toString() {
        return LogUtil.mapToString(keyValueMap);
    }
}
