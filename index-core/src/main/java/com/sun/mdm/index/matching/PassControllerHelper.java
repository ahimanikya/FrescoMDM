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
 * for the PassController component
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class PassControllerHelper {

    /** Creates new PassControllerHelper */
    public PassControllerHelper() {
    }

    /**
     * Load the configured implementation class and instantiate it.
     * @throws InstantiationException Instantiating the configured class failed
     * @throws ClassNotFoundException Loading the configured class failed
     * @throws IllegalAccessException Loading/Instantiating the configured class 
     * is not allowed by the current security settings
     * @return an instance of the PassController implementation configured
     */         
    public PassController getPassControllerImpl() 
            throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        MEFAFactory mefaFactory = new MEFAFactory();
        java.lang.Object instance = mefaFactory.getComponentInstance(Components.PASS_CONTROLLER);
        return (PassController) instance;
    }
}
