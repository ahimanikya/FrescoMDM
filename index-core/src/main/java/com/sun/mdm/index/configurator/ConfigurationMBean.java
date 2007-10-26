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
package com.sun.mdm.index.configurator;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/** ConfigurationMBean interface
 *
 * @version $Revision: 1.1 $
 */
public interface ConfigurationMBean {

    /** MBean name */
    public static final String MBEAN_NAME = "BBEConfig";

    /** Returns the configuration instance given the name.
     *
     * @param name name of the configuration module.
     * @return returns the configuration instance given the name.
     */
    public ConfigurationInfo getConfiguration(String name);

    /** Load the configuration file.
     *
     * @param fileStream input stream to the file.
     * @throws IOException if there is an error accessing the file.
     * @throws SAXException if there is an error parsing the file.
     * @throws ParserConfigurationException if there is an invalid parser 
     * configuration.
     * @throws DOMException if there is an error accessing the DOM tree.
     * @throws ClassNotFoundException if the parser class not found.
     * @throws InstantiationException if the parser can not be instantiated.
     * @throws IllegalAccessException if it is unable to access parser default
     * constructor.
     * @throws ConfigurationException if it encounters unexpected values.
     */
    public void load(InputStream fileStream)
        throws IOException, SAXException, ParserConfigurationException, 
            DOMException, ClassNotFoundException, InstantiationException, 
            IllegalAccessException, ConfigurationException;

}
