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
package com.sun.mdm.index.util.ejbproxy;

import javax.naming.Context;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;

/**
 * Well-known class to get the IntialContext to use the EJB proxy classes
 * to run EJBs 'stand-alone'
 * For testing for non-EJB operations it forwards requests to the configured
 * JNDI provider.
 * This implies that for testing those, the JNDI provider and all required
 * resources need to be running.
 * @author  aegloff
 * @version $Revision: 1.3 $
 */
public class EJBTestProxy {

    private static final String PROXY_JNDI = "com.sun.mdm.index.util.ejbproxy.ProxyJNDI";
    
    /** Creates new EJBTestProxy */
    public EJBTestProxy() {
    }
    
    /**
     * Gets the IntialContext to use the EJB proxy classes
     * to run EJBs 'stand-alone'
     * For testing for non-EJB operations it forwards requests to the configured
     * JNDI provider.
     * This implies that for testing those, the JNDI provider and all required
     * resources need to be running.
     * @return the ProxyInitialContext
     * @throws javax.naming.NamingException if there is a problem initializing 
     * the InitialContext to forward requests to or the ProxyInitialContext 
     * itself
     */ 
    public static javax.naming.Context getInitialContext() 
            throws javax.naming.NamingException {
        // Use the ProxyInitialContext to simulate a trivial EJB 'container'
        InitialContextFactoryBuilder builder = 
                new com.sun.mdm.index.util.ejbproxy.ProxyInitialContextFactoryBuilder();
        if (!NamingManager.hasInitialContextFactoryBuilder()){
            NamingManager.setInitialContextFactoryBuilder(builder);
        }
        
        // Settings for forwarding JNDI requests if there is no proxy mapping
        ResourceBundle jndiSettings = null;
        try {
            jndiSettings = ResourceBundle.getBundle(PROXY_JNDI);
        } catch (MissingResourceException ex) {
            System.out.println("Warning: Could not find " + PROXY_JNDI + " properties file."
                    + " Unknown JNDI names will not be forwarded to another JNDI provider.");
        }
        
        if (jndiSettings != null) {
            System.out.println("Loading JNDI provider settings in " + PROXY_JNDI + " properties file.");
            Enumeration keysEnum = jndiSettings.getKeys();
            while (keysEnum.hasMoreElements()) {
                String key = (String) keysEnum.nextElement();
                String value = jndiSettings.getString(key);
                System.out.println("Setting key: " + key + " to value: " + value);
                System.setProperty(key, value);
            }            
        } else {
            System.out.println("Warning: jndiSettings null when loading " + PROXY_JNDI + " properties file.");
        }

        Context ctx = new javax.naming.InitialContext();

        return ctx;
    }
}
