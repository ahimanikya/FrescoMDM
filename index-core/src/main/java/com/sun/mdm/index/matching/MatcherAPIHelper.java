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

import com.sun.mdm.index.configurator.impl.MEFAConfiguration.Components;

/**
 * A Helper class to load the configured implementation class as configured
 * for the matcher api component
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class MatcherAPIHelper {

    private MEFAFactory mefaFactory;
    
    /** Creates new MatcherAPIHelper */
    public MatcherAPIHelper() {
    }

    /**
     * Load the configured implementation class and instantiate it.
     * @throws InstantiationException Instantiating the configured class failed
     * @throws ClassNotFoundException Loading the configured class failed
     * @throws IllegalAccessException Loading/Instantiating the configured class 
     * is not allowed by the current security settings
     * @return the MatcherAPI implementation instance configured
     * @throws ClassNotFoundException if the configured class could not be found
     * @throws InstantiationException if the configured class could not be instantiated 
     * @throws IllegalAccessException Loading/Instantiating the configured class 
     * is not allowed by the current security settings
     */     
    public MatcherAPI getMatcherAPIImpl() 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        java.lang.Object instance = getMEFAFactory().getComponentInstance(Components.MATCHER_API);
        return (MatcherAPI) instance;
    }
    
    /**
     * Load the configured implementation class and instantiate it.
     * @throws InstantiationException Instantiating the configured class failed
     * @throws ClassNotFoundException Loading the configured class failed
     * @throws IllegalAccessException Loading/Instantiating the configured class 
     * is not allowed by the current security settings
     * @return the configured MatchEngineConfiguration instance
     */     
    public MatchEngineConfiguration getMatchEngineConfigImpl() 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        java.lang.Object instance = getMEFAFactory().getComponentInstance(Components.MATCH_ENGINE_CONFIG);
        return (MatchEngineConfiguration) instance;        
    }
    
    /**
     * Helper to load and instantiate MEFA classes 
     * @return MEFAFactory to help loading and instantiating MEFA classes 
     */     
    MEFAFactory getMEFAFactory() {
        if (mefaFactory == null) {
            mefaFactory = new MEFAFactory();
        }
        return mefaFactory;
    }
}
