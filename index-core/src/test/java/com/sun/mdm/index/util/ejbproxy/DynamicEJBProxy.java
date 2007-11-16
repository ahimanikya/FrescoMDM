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

import java.lang.reflect.Method;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Dynamic Proxy class to allow running EJBs stand-alone by forwarding requests
 * from the remote/local interface directly to a manually instantiated EJB instance.
 * @author  aegloff
 * @version $Revision: 1.4 $
 */
public class DynamicEJBProxy implements java.lang.reflect.InvocationHandler {
    Object mManualEJBInstance;
    Object result = null;
    private final Logger mLogger = LogUtil.getLogger(this);
    
    /** 
     * Creates new DynamicEJBProxy 
     * @param manualEJBInstance a manual instance of an EJB implementation class
     */
    public DynamicEJBProxy(Object manualEJBInstance) {
        this.mManualEJBInstance = manualEJBInstance;
    }  

    /** 
     * Java Dynamic Proxy implementation to forward the request on a local
     * or remote EJB interface to a manual instance of the EJB implementation
     * class.
     *
     * This can be used to enable an EJB to run outside of a container as well,
     * e.g. for some unit testing.
     * @param proxy the interface implementation 
     * @param m the method invoked on the interface
     * @param args the arguments passed to the method invoked on the interface
     * @return the return value from the forwarded method call
     * @throws Throwable pass back any exceptions that were thrown
     * @see java.lang.reflect.InvocationHandler
     */    
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Class[] argTypes = null;
        try {
            // Find and invoke the same method that was invoked on the remote 
            // interface the actual EJB instance

            if (mLogger.isDebugEnabled()) {            
                int noOfArgs = 0;
                String instanceClassName = "--- No instance defined ---";
                if (m.getParameterTypes() != null) {
                    noOfArgs = m.getParameterTypes().length;
                }
                if (mManualEJBInstance != null) {
                    instanceClassName = mManualEJBInstance.getClass().getName();
                }

                mLogger.debug("Searching for method '" + m.getName() + "' on " 
                        + instanceClassName + " instance with the right " 
                        + noOfArgs + " argTypes: " + m.toString());            
            }
            
            // Try to find the method. If it isn't found an exception is thrown.            
            Method manualEJBMethod = 
                    mManualEJBInstance.getClass().getMethod(m.getName(), 
                    m.getParameterTypes());

            mLogger.debug("Forwarding call by invoking the method.");                        
            // Invoke the method
            result = manualEJBMethod.invoke(mManualEJBInstance, args);
        } catch (NoSuchMethodException ex) {
            mLogger.error("The EJB instance configured must implement the corresponding" 
                    + " local and remote interfaces being used.", ex);
            throw ex;
        } catch (java.lang.reflect.InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (Exception ex) {
            throw ex;
        }
        
        return result;
    }
}
