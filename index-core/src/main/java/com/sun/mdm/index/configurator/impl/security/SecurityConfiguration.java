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
package com.sun.mdm.index.configurator.impl.security;

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
public class SecurityConfiguration implements ConfigurationInfo {

    /**
     * module name
     */
    public static final String SECURITY_CONFIGURATION = "SecurityConfig";
    
    static final String TAG_SECURITYPLUGIN = "SecurityPlugInClass";
  
    private String msecurityPlugInClassName;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    



    /** Creates a new instance of SecurityConfiguration */
    public SecurityConfiguration() {
    }


    /**
     * Get module type.
     *
     * @return module type.
     */
    public String getModuleType() {
        return SECURITY_CONFIGURATION;
    }


    /**
     * Get list of validators.
     *
     * @return Hashtable, the registry for custom validation.
     */
    public String getSecurityPlugInClass() {
        return msecurityPlugInClassName;
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
            NodeList securityTypes = node.getChildNodes();
            int typeCount = securityTypes.getLength();
            for (int i = 0; i < typeCount; i++) {
                Node typeNode = securityTypes.item(i);
                if (typeNode.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = typeNode.getNodeName();
                    if (TAG_SECURITYPLUGIN.equals(nodeName)) {
                        NodeList pluginList = typeNode.getChildNodes();
                        int pluginCount = pluginList.getLength();
                        for (int j = 0; j < pluginCount; j++) {
                            Node securityPlugNode = pluginList.item(j);
                            if (securityPlugNode.getNodeType() == Node.TEXT_NODE) {
                                 msecurityPlugInClassName = securityPlugNode.getNodeValue();
                                
                            }   
                        }
                    }
                }
            }
            mLogger.info(mLocalizer.x("CFG012: SecurityConfiguration: securityPlugInClassName is {0}", msecurityPlugInClassName));
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG541: Could not parse " + 
                                    "the SecurityConfiguration XML node: {0}", e));
        }
    }

}
