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

import com.sun.mdm.index.configurator.impl.standardization.SystemObjectStandardization;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;

/**
 * The interface a standardization engine adapter has to implement to enable 
 * communication between the framework and the specific standardization engine.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public interface StandardizerAPI {
    
    /**
     * Standardize the passed in SystemObject according to the passed in 
     * standardization configuration
     * Care should be taken as the standardization may be performed directly
     * on the passed in SystemObject, this may therefore change the actual 
     * object passed in.
     * @param objToStandardize the system object to standardize. Care should be
     * taken as the method may modify this object passed in directly.
     * @param metaData the metaData describing what parts of the systemobject
     * need to be standardized
     * @throws StandardizationException if the Standardization failed
     * @throws ObjectException if retrieving/setting the configured values failed
     * @return The standardized SystemObject. It is allowed to be a reference
     * to the same object (but with modified fields) as was passed into 
     * objToStandardize
     */        
    SystemObject standardize(SystemObject objToStandardize, SystemObjectStandardization metaData) 
            throws StandardizationException, ObjectException;
    
    /**
     * Initialize the standardization engine and the adapter
     * called once upon startup for each adapter instance
     * @param config the standardization engine configuration configured
     * @throws StandardizationException if the initialization failed
     */        
    void initialize(StandardizerEngineConfiguration config) 
            throws StandardizationException;
    
    /**
     * Shutdown and release any resources associated with the standardization
     * engine and the adapter
     * Called once per adapter instance before the adapter is discarded by the
     * framework
     * @throws StandardizationException if the shutdown failed
     */            
    void shutdown() 
            throws StandardizationException;
    
}
