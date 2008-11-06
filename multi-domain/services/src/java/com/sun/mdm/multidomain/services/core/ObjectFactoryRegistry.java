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
package com.sun.mdm.multidomain.services.core;

import java.util.Map;
import java.util.HashMap;
        
import net.java.hulp.i18n.Logger;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * ObjectFactoryRegistry class.
 * @author cye
 */
public class ObjectFactoryRegistry {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.core.ServiceLocator");
    private static Localizer localizer = Localizer.getInstance();

    private static Map<String, ObjectFactory> entries = new HashMap<String, ObjectFactory>();    
    
    /**
     * Register ObjectFactory for the given object name and ObjectFactory.
     * @param objectName Object name.
     * @param objectFactory ObjectFactory.
     */
    public static void register(String objectName, ObjectFactory objectFactory) {  
        entries.put(objectName, objectFactory);
        logger.info(localizer.x("SVC002: ObjectFactoryRegistry registered {0} for object {1}", 
                                 objectFactory.getClass().getName(), objectName));        
    }

    /**
     * Register ObjectFactory for the given object name.
     * @param objectName Object name.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static void register(String objectName) 
        throws ConfigException {  
        ObjectFactory entry = entries.get(objectName);
        if (entry == null) {
            entry = new ObjectNodeFactoryImpl(objectName);
            register(objectName, entry);       
        }        
    }
    
    /**
     * Lookup ObjectFactory for the given object name.
     * @param objectName Object name.
     * @return ObjectFactory.
     * @throws ConfigException Thrown if an error occurs during processing.
     */
    public static ObjectFactory lookup(String objectName) 
        throws ConfigException {
        ObjectFactory entry = entries.get(objectName);
        if (entry == null) {
            entry = new ObjectNodeFactoryImpl(objectName);
            register(objectName, entry);
        }
        return entry;
    }
}
