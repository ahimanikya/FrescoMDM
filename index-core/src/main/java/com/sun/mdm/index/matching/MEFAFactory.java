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
package com.sun.mdm.index.matching;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.MEFAConfiguration;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Helper class to load configured component implementation classes
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class MEFAFactory {

    private final Logger mLogger = LogUtil.getLogger(this);
    
    /** Creates new MEFAFactory */
    public MEFAFactory() {
    }
    
    /**
     * Load configured component implementation classes
     * @param componentName the component name for which to load the instance
     * @return an instance of the configured component implementation class, null
     * if no class is defined.
     * @throws InstantiationException Instantiating the configured class failed
     * @throws ClassNotFoundException Loading the configured class failed
     * @throws IllegalAccessException Loading/Instantiating the configured class 
     * is not allowed by the current security settings
     */
    java.lang.Object getComponentInstance(String componentName) 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        String implClassName = getMEFAConfiguration().getComponentImplClassName(componentName);
        java.lang.Object instance = null;
        if (implClassName != null) {
            java.lang.Class loadedClass = Class.forName(implClassName);
            instance = loadedClass.newInstance();
        } else {
            mLogger.debug("No implementation class configured for the MEFA component: " + componentName);
        }

        return instance;
    }

    /**
     * Get the MEFA configuration 
     * @return MEFAConfigration the configuration details for MEFA
     * @throws InstantiationException if the configuration could not be retrieved
     */
    MEFAConfiguration getMEFAConfiguration() 
            throws InstantiationException {
        MEFAConfiguration mefaConfig = null;
        ConfigurationService cfgFactory = ConfigurationService.getInstance();
        mefaConfig = (MEFAConfiguration) cfgFactory.getConfiguration(MEFAConfiguration.MEFA);
        
        return mefaConfig;
    }
 
}
