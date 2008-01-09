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

import java.security.Identity;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.xml.rpc.handler.MessageContext;

/**
 * Very trivial implementation of a SessionContext to use the DynamicEJBProxy
 * Not all methods are supported in a first wave of supporting only minimal
 * functionality.
 * Additionally, the EJBProxy currently does not know the corresponding 
 * local/remote interfaces.
 * @author  aegloff
 * @version $Revision: 1.4 $
 */
public class ProxySessionContext implements javax.ejb.SessionContext {

    javax.ejb.EJBHome mHomeImpl;
    javax.ejb.EJBObject mComponentImpl;
    javax.ejb.EJBLocalHome mLocalHomeImpl;
    javax.ejb.EJBLocalObject mLocalComponentImpl;
    Class mBusinessInterface;
    Object mEjb;
    
    /** Creates new ProxySessionContext */
    public ProxySessionContext() {
    }
    
    /** 
     * Creates new ProxySessionContext 
     * @param homeImpl Home interface implementation instance
     * @param componentImpl Remote interface implementation instance
     * @param localHomeImpl local home interface implementation instance
     * @param localComponentImpl local interface implementation instance
     */    
    public ProxySessionContext(javax.ejb.EJBHome homeImpl, 
            javax.ejb.EJBObject componentImpl, 
            javax.ejb.EJBLocalHome localHomeImpl, 
            javax.ejb.EJBLocalObject localComponentImpl) {
        mHomeImpl = homeImpl;
        mComponentImpl = componentImpl;
        mLocalHomeImpl = localHomeImpl;
        mLocalComponentImpl = localComponentImpl;
    }

    /** 
     * Creates new ProxySessionContext only for remote interfaces
     * @param homeImpl Home interface implementation instance
     * @param componentImpl Remote interface implementation instance
     */    
    public ProxySessionContext(javax.ejb.EJBHome homeImpl, 
            javax.ejb.EJBObject componentImpl) {
        mHomeImpl = homeImpl;
        mComponentImpl = componentImpl;
    }    
    
    /** 
     * Creates new ProxySessionContext only for local interfaces
     * @param localHomeImpl local home interface implementation instance
     * @param localComponentImpl local interface implementation instance
     */    
    public ProxySessionContext(javax.ejb.EJBLocalHome localHomeImpl, 
            javax.ejb.EJBLocalObject localComponentImpl) {
        mLocalHomeImpl = localHomeImpl;
        mLocalComponentImpl = localComponentImpl;
    }
    
    /** 
     * Creates new ProxySessionContext only for local interfaces
     * @param localHomeImpl local home interface implementation instance
     * @param localComponentImpl local interface implementation instance
     */    
    public ProxySessionContext(Class businessInterface, 
            Object ejb) {
        mBusinessInterface = businessInterface;
        mEjb = ejb;
    }    

    /**
     * @see javax.ejb.SessionContext
     */
    public javax.ejb.EJBHome getEJBHome() {
        return mHomeImpl;
    }
    
    /**
     * @see javax.ejb.SessionContext
     */
    public javax.ejb.EJBObject getEJBObject() 
            throws java.lang.IllegalStateException {
        return mComponentImpl;
    }
    
    /**
     * @see javax.ejb.SessionContext
     */
    public javax.ejb.EJBLocalHome getEJBLocalHome() {
        return mLocalHomeImpl;
    }    

    /**
     * @see javax.ejb.SessionContext
     */
    public javax.ejb.EJBLocalObject getEJBLocalObject() 
            throws java.lang.IllegalStateException {
        return mLocalComponentImpl;
    }
    
    /**
     * Get the Caller Principle
     * @return a test principle
     */
    public java.security.Principal getCallerPrincipal() {
        java.security.Principal principal = new DummyPrincipal();
        return principal;
    }    
    
    /**
     * @see javax.ejb.SessionContext
     */
    public boolean isCallerInRole(java.lang.String str) {
        return true;       
    }
    
    /**
     * @see javax.ejb.SessionContext
     */
    public javax.transaction.UserTransaction getUserTransaction() 
            throws java.lang.IllegalStateException {
        //throw new java.lang.IllegalStateException("Not supported by proxy");
        javax.transaction.UserTransaction utx = new DummyUserTransaction();
        return utx;
    }
    
    /**
     * @see javax.ejb.SessionContext
     */
    public boolean getRollbackOnly() throws java.lang.IllegalStateException {
        throw new java.lang.IllegalStateException("Not supported by proxy");
    }
    
    /**
     * @see javax.ejb.SessionContext
     */
    public void setRollbackOnly() throws java.lang.IllegalStateException {
        //throw new java.lang.IllegalStateException("Not supported by proxy");
        Logger.getLogger(ProxySessionContext.class.getName()).log(Level.INFO, "Transaction is not supported at proxy");
        
    }

    /**
     * @see javax.ejb.SessionContext
     * WARNING! This is an EJB 2.1 method
     */
    public javax.ejb.TimerService getTimerService() throws java.lang.IllegalStateException {
        throw new java.lang.IllegalStateException("Not supported by proxy");
    }

    public MessageContext getMessageContext() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T getBusinessObject(Class<T> businessInterface) throws IllegalStateException {
        //throw new UnsupportedOperationException("Not supported yet.");
        return (T)mEjb;
    }

    public Class getInvokedBusinessInterface() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookup(String name) {
        Object obj = null;
        if (name == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        try {
            Context context = EJBTestProxy.getInitialContext();
            obj= context.lookup(name);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return obj;       
    }

    public Properties getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Identity getCallerIdentity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCallerInRole(Identity arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
}
