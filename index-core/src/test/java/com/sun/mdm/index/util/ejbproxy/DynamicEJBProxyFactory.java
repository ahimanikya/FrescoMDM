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

/**
 * A factory to create dynamic proxy classes to allow running EJBs outside of
 * a container, e.g. for unit tests.
 * @author  aegloff
 * @version $Revision: 1.2 $
 */
public class DynamicEJBProxyFactory {

    /** Creates new DynamicEJBProxyFactory */
    public DynamicEJBProxyFactory() {
    }
    
    /**
     * Factory method to create a Java dynamic proxy class to forward 
     * requests from remote/local interfaces to the EJB instance
     * @param manualEJBInstance The 'manual' (e.g. created by saying 'new'
     * instead of by the container) EJB implementation instance to 
     * forward the requests to.
     * @param remoteInterface The interface to implement, either the local or
     * remote EJB component interface.
     * @return the dynamic proxy class implementing the requested interface
     */
    public static Object createInstance(Object manualEJBInstance, Class remoteInterface) {
        Object proxy = null;

        // Create proxy to forward requests from remote interface to EJB instance
        proxy = java.lang.reflect.Proxy.newProxyInstance(
                manualEJBInstance.getClass().getClassLoader(),
                new Class[] {remoteInterface}, 
                new DynamicEJBProxy(manualEJBInstance));
                
        return proxy;
    }
}
