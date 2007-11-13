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
package com.sun.mdm.index.configurator.impl.idgen;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import com.sun.mdm.index.configurator.ConfigurationInfo;
import com.sun.mdm.index.configurator.Parameter;
import com.sun.mdm.index.configurator.ConfigurationException;
import com.sun.mdm.index.configurator.ParameterParser;
import com.sun.mdm.index.idgen.EuidGenerator;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;



/**
 * Decision maker configuration.
 *
 * @author dcidon
 */
public class EuidGeneratorConfiguration implements ConfigurationInfo {

    /** Module type name
     */    
    public static final String EUID_GENERATOR = "EuidGenerator";

    private Class mEuidGeneratorClass;
    private Parameter[] mParameters;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /** Creates new EuidGeneratorConfiguration instance. */
    public EuidGeneratorConfiguration() {
    }


    /** Get an instance of the EUID generator class.
     *
     * @throws ConfigurationException if an error occured.
     * @return EuidGenerator instance.
     */
    public EuidGenerator getEuidGenerator()
        throws ConfigurationException {
        try {
            EuidGenerator dm = (EuidGenerator) mEuidGeneratorClass.newInstance();
            for (int i = 0; i < mParameters.length; i++) {
                dm.setParameter(mParameters[i].getName(), mParameters[i].getValueObject());
            }
            return dm;
        } catch (Exception e) {
            throw new ConfigurationException(mLocalizer.t("CFG524: Encountered an " + 
                                    "error while retrieving the EUID Generator: {0}", e));
        }
    }


    /** Getter for ModuleType attribute of the EuidGeneratorConfiguration object.
     *
     * @return module type
     */
    public String getModuleType() {
        return EUID_GENERATOR;
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
        NodeList optionList = node.getChildNodes();
        int count = optionList.getLength();
        for (int i = 0; i < count; i++) {
            Node option = optionList.item(i);
            if (option.getNodeType() == Node.ELEMENT_NODE) {
                String optionName = option.getNodeName();
                if (optionName.equals("euid-generator-class")) {
                    Node optionValue = option.getFirstChild();
                    String className = optionValue.getNodeValue();
                    try {
                        mEuidGeneratorClass = Class.forName(className.trim());
                    } catch (Exception e) {
                        throw new ConfigurationException(mLocalizer.t("CFG525: EuidGeneratorConfiguration " + 
                                    "could not load class {0}: {1}", className, e));
                    }
                } else if (optionName.equals("parameters")) {
                    ParameterParser parameterParser = new ParameterParser();
                    ArrayList parameterList = parameterParser.getParameters((Element) node);
                    int size = parameterList.size();
                    mParameters = new Parameter[size];
                    for (int j = 0; j < size; j++) {
                        mParameters[j] = (Parameter) parameterList.get(j);
                    }
                } else {
                    throw new ConfigurationException(mLocalizer.t("CFG526: Unrecognized option for " + 
                                                        "EuidGeneratorConfiguration: {0}", optionName)); 
                }
            }
        }
    }

}
