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
 * Dynamic Proxy class to allow creating EJBs stand-alone by forwarding requests
 * from the localHome/home interface directly to a manually instantiated EJB instance.
 * @author  aegloff
 * @version $Revision: 1.6 $
 */
public class DynamicHomeProxy implements java.lang.reflect.InvocationHandler {
    Class mEjbImplClass;
    Class mComponentInterface;
    Object mManualEJBInstance;
    Object result = null;
    private final Logger mLogger = LogUtil.getLogger(this);
    
    /** 
     * Creates new DynamicHomeProxy 
     * @param ejbImplClass the EJB implementation class
     * @param componentInterface the component interface implementation class
     */
    public DynamicHomeProxy(Class ejbImplClass, Class componentInterface) {
        this.mEjbImplClass = ejbImplClass;
        this.mComponentInterface = componentInterface;
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
        String correspondingMethod = m.getName();
        Object componentInterfaceImpl = null;
        try {
            // Find and invoke the corresponding method that was invoked on 
            // the home interface on the actual EJB instance
            
            // Hard-coded mapping for now of the corresponding impl method
            if (m.getName().equals("create")) {
                correspondingMethod = "ejbCreate";
                // For create, instantiate the EJB implementation
                mManualEJBInstance = mEjbImplClass.newInstance();
                
                // Create the component interface proxy for the implementation
                componentInterfaceImpl = 
                        DynamicEJBProxyFactory.createInstance(
                        mManualEJBInstance, mComponentInterface);
                
                // Set the session context.               
                // Look for the setSessionContext(SessionContext) method. 
                try {
                    Method sessionContextMethod = 
                            mManualEJBInstance.getClass().getMethod(
                            "setSessionContext", 
                            new Class[]{javax.ejb.SessionContext.class});                
                    
                    // Work-around for not knowing all the remote and local 
                    // Implementations
                    javax.ejb.EJBHome homeImpl = null;
                    javax.ejb.EJBObject componentImpl = null;
                    javax.ejb.EJBLocalHome localHomeImpl = null;
                    javax.ejb.EJBLocalObject localComponentImpl = null;                    
                    
                    if (proxy instanceof javax.ejb.EJBHome) {
                        homeImpl = (javax.ejb.EJBHome) proxy;
                    } else {
                        localHomeImpl = (javax.ejb.EJBLocalHome) proxy;
                    }
                    
                    if (componentInterfaceImpl instanceof javax.ejb.EJBObject) {
                        componentImpl = 
                                (javax.ejb.EJBObject) componentInterfaceImpl;
                    } else {
                        localComponentImpl = 
                                (javax.ejb.EJBLocalObject) componentInterfaceImpl;
                    }
                    
                    // Call the setSessionContext method
                    javax.ejb.SessionContext sessionContext = 
                            new ProxySessionContext(homeImpl, componentImpl, 
                                    localHomeImpl, localComponentImpl);
                    Object[] sessArgs = new Object[]{sessionContext};
                    sessionContextMethod.invoke(mManualEJBInstance, sessArgs);
                } catch (NoSuchMethodException ex) {
                    // Ignore if it doesn't have a setSessionContext method
                    // Log for debugging purposes
                    mLogger.error("Info: class does not have a setSessionContext method: " 
                        + mManualEJBInstance.getClass().getName(), ex);
                }
            }

            // Hard-coded mapping for now of the corresponding impl method
            if (m.getName().equals("remove")) {
                correspondingMethod = "ejbRemove";
            }
            
            if (mLogger.isDebugEnabled()) {            
                int noOfArgs = 0;
                String instanceClassName = "--- No instance defined ---";
                if (m.getParameterTypes() != null) {
                    noOfArgs = m.getParameterTypes().length;
                }
                if (mManualEJBInstance != null) {
                    instanceClassName = mManualEJBInstance.getClass().getName();
                }

                mLogger.debug("Searching for the equivalent of method '" + m.getName() 
                        + " - " + correspondingMethod + "' on " 
                        + instanceClassName + " instance with the right "
                        + noOfArgs + " argTypes: " + m.toString());            
            }
            
            // Try to find the method. If it isn't found an exception is thrown.            
            Method manualEJBMethod = 
                    mManualEJBInstance.getClass().getMethod(correspondingMethod, 
                    m.getParameterTypes());

            mLogger.debug("Forwarding call by invoking the method.");                        
            // Invoke the method
            result = manualEJBMethod.invoke(mManualEJBInstance, args);
            
            if (m.getName().equals("create")) {
                // For the create call return the component interface proxy impl
                //result = DynamicEJBProxyFactory.createInstance(
                //        mManualEJBInstance, mComponentInterface);
                result = componentInterfaceImpl;
            }
        } catch (NoSuchMethodException ex) {
            mLogger.error("The EJB instance configured must implement the corresponding"
                    + " localHome and home interfaces being used.", ex);
            throw ex;
        } catch (java.lang.reflect.InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (Exception ex) {
            throw ex;
        }
        
        return result;
    }
}
