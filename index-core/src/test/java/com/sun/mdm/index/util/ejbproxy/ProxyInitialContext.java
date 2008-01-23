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

//import javax.naming.*;
import javax.naming.Name;
import javax.naming.Context;
import javax.naming.NamingException;
import oracle.jdbc.pool.OracleDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.SQLException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

/**
 * InitialContext implementation to use the DynamicEJBProxy
 * @author  aegloff
 * @version $Revision: 1.11 $
 */
public class ProxyInitialContext 
        implements javax.naming.Context { 

    /** Defines the prefix java:comp/env/ this InitialContext tries to handle */
    public static final String PREFIX_COMP_ENV = "java:comp/env/";
    
    private java.util.ResourceBundle namingMappings;
    javax.naming.Context delegate = null;
    
    private final Logger mLogger = LogUtil.getLogger(this);
    
    
    /** 
     * Creates new ProxyInitialContext 
     * @throws NamingException if the proxy InitialContext could
     * not be initialized
     */
    public ProxyInitialContext() 
            throws NamingException {
        java.util.Hashtable conv = new java.util.Hashtable();
        conv.putAll(System.getProperties());
        initialize(conv);
    }

    /** 
     * Creates new ProxyInitialContext 
     * @param environment the environment properties to use
     * @throws NamingException if the proxy InitialContext could
     * not be initialized
     */
    public ProxyInitialContext(java.util.Hashtable environment) 
            throws NamingException {
        mLogger.debug("construct ProxyInitialContext"); 
        initialize(environment);
    }
    
    /** 
     * Creates new ProxyInitialContext 
     * Not yet supported.
     * @param lazy initialization for this InitialContext
     * @throws NamingException if the proxy InitialContext could
     * not be initialized
     */
    public ProxyInitialContext(boolean lazy) 
            throws NamingException {
        this();
        mLogger.debug("Lazy switch not yet supported in ProxyIntialContext");
    }
    
    /**
     * Initialize this Context
     * @param environment the environment properties to use
     * @throws NamingException if the proxy InitialContext could
     * not be initialized
     */
    public void initialize(java.util.Hashtable environment) 
            throws NamingException {
        try {
            mLogger.debug("Loading proxy naming mapping");
            namingMappings = java.util.ResourceBundle.getBundle(
                    com.sun.mdm.index.util.ejbproxy.NamingMappings.class.getName());

            mLogger.debug("Initializing delegate context");
            String delegateFactoryName = 
                    (String) environment.get(Context.INITIAL_CONTEXT_FACTORY);
            if (delegateFactoryName != null) {
                mLogger.debug("Delegate context factory name: " + delegateFactoryName);
                Class delegateFactoryClass = 
                        this.getClass().forName(delegateFactoryName);
                mLogger.debug("Delegate context loaded class: " 
                        + delegateFactoryClass.getName());
                java.lang.reflect.Constructor construct = null;
                try {
                    construct = delegateFactoryClass.getConstructor(
                            new Class[]{java.util.Hashtable.class});
                } catch (NoSuchMethodException ex) {
                    // Ignore as we will use default constructor instead
                    mLogger.debug("Info: delegate factory class does not have a "
                    + "constructor with java.util.Hashtable argument");
                }
                Object instance = null;
                if (construct != null) {
                    mLogger.debug("Delegate context constructor with arg found: " 
                            + (construct != null));
                    instance = construct.newInstance(new Object[]{environment});
                } else {
                    mLogger.debug("Using delegate context default constructor.");
                    instance = delegateFactoryClass.newInstance();
                }
                javax.naming.spi.InitialContextFactory fact = 
                        (javax.naming.spi.InitialContextFactory) instance;
                delegate = fact.getInitialContext(environment);

                mLogger.debug("Delegate context " + delegateFactoryName + " not null: " 
                        + (delegate != null));
            } else {
                mLogger.debug("No delegate context configured.");
            }
        } catch (Exception ex) {
            mLogger.error(ex.getMessage());
            throw new NamingException("Initializing ProxyInitialContext "
                    + "failed." + ex.getMessage());
        }
    }

    /**
     * @see javax.naming.Context
     */
    public Object lookup(Name name) throws NamingException {
        mLogger.debug("looking up Name:" + name);
        return lookup(name.toString());
    }

    /**
     * @see javax.naming.Context
     */    
    public Object lookup(String name) throws NamingException {
        Object result = null;
        if (name.equals(com.sun.mdm.index.util.JNDINames.BBE_DATASOURCE)) {
            try {
                String vendor = System.getProperty("proxy.datasource.vendor");
                
                if (vendor == null) {
                    throw new NamingException("Invalid database vendor specified: " + vendor);
                } else if (vendor.equalsIgnoreCase("ORACLE")) {
                    OracleDataSource ocpds = new OracleDataSource();
                    String serverName = System.getProperty("proxy.datasource.server.name");
                    int portNumber = Integer.parseInt(System.getProperty("proxy.datasource.port.number"));
                    String networkProtocol = System.getProperty("proxy.datasource.network.protocol");
                    String driverType = System.getProperty("proxy.datasource.driver.type");
                    String userName = System.getProperty("proxy.datasource.user.id");
                    String password = System.getProperty("proxy.datasource.password");
                    String databaseName = System.getProperty("proxy.datasource.name");
                    ocpds.setServerName(serverName);
                    ocpds.setPortNumber(portNumber);
                    ocpds.setNetworkProtocol(networkProtocol);
                    ocpds.setDriverType(driverType);
                    ocpds.setDatabaseName(databaseName); 
                    ocpds.setUser(userName);
                    ocpds.setPassword(password);
                    return ocpds;
                } else if (vendor.equalsIgnoreCase("SQL Server")) {
                    SQLServerDataSource ds = new SQLServerDataSource();
                    String serverName = System.getProperty("proxy.datasource.server.name");
                    int portNumber = Integer.parseInt(System.getProperty("proxy.datasource.port.number"));
                    String networkProtocol = System.getProperty("proxy.datasource.network.protocol");
                    String driverType = System.getProperty("proxy.datasource.driver.type");
                    String userName = System.getProperty("proxy.datasource.user.id");
                    String password = System.getProperty("proxy.datasource.password");
                    String databaseName = System.getProperty("proxy.datasource.name");
                    ds.setServerName(serverName);
                    ds.setPortNumber(portNumber);
//                    ds.setNetworkProtocol(networkProtocol);
//                    ds.setDriverType(driverType);
                    ds.setDatabaseName(databaseName); 
                    ds.setUser(userName);
                    ds.setPassword(password);
                    return ds;
                } else {    // TODO: add support for other database vendors as necessary
                    throw new NamingException("Invalid database vendor specified: " + vendor);
                }
            } catch (SQLException e) {
                throw new NamingException(e.getMessage());
            }
        }
        mLogger.debug("looking up mapping: " + name);
        Object values = null;
        try {    
            values = namingMappings.getObject(name);
        } catch (java.util.MissingResourceException ex) {
            mLogger.debug("No mapping found for: " + name);
            // Ignore, as it just means that we don't emulate it. We will 
            // forward the request to the delegate Context
        }
        // If it wasn't found, try whether it exists with prefix java:comp/env
        if (values == null && !name.startsWith(PREFIX_COMP_ENV)) {
            String withPrefix = PREFIX_COMP_ENV + name;
            try {    
                mLogger.debug("Try to find mapping with prefix: " + withPrefix);
                values = namingMappings.getObject(withPrefix);
            } catch (java.util.MissingResourceException ex) {
                mLogger.debug("No mapping found for: " + withPrefix);
                // Ignore, as it just means that we don't emulate it. We will 
                // forward the request to the delegate Context
            }            
        }
        
        try {            
            if (values != null) {
                mLogger.debug("Mapping found.");
                String[] mapping = (String[]) values;
                if (mapping.length == 2) {
                    mLogger.debug("Mapping for EJB3.");
                    Class remoteInterface = this.getClass().forName(mapping[0]);
                    Class ejbImplClass = this.getClass().forName(mapping[1]);
                    result = lookupBusinessInterface(remoteInterface, ejbImplClass);                     
                }else if (mapping.length == 3) {
                    Class homeInterface = this.getClass().forName(mapping[0]);
                    Class remoteInterface = this.getClass().forName(mapping[1]);
                    Class ejbImplClass = this.getClass().forName(mapping[2]);
                    result = DynamicHomeProxyFactory.createInstance(
                            ejbImplClass, homeInterface, remoteInterface);                     
                }else{           
                    throw new NamingException(
                        "Mapping defintion is invalid for name: " + name);
                }
            } else {
                if (delegate != null) {
                    String forwardedName = name;
                    if (name.startsWith(PREFIX_COMP_ENV)) {
                        mLogger.debug("Name to look up starts with prefix: " + PREFIX_COMP_ENV 
                            + ". Removing before forwarding.");
                        forwardedName = name.substring(PREFIX_COMP_ENV.length());
                    }
                    
                    mLogger.debug("Forwarding lookup to delegate Context for name: " + forwardedName);
                    result = delegate.lookup(forwardedName);                
                    mLogger.debug("Forwarded lookup complete.");
                } else {
                    throw new NamingException("Name was not found in "
                            + "ProxyInitialContext: " + name);
                }
            }
        } catch (Exception ex) {
            throw new NamingException("Root Exception: " + ex.getClass() + ": " 
            + ex.getMessage());
        }
        
        return result;
    }
    
    private Object lookupBusinessInterface(Class BusinessInterface, Class ejbImplClass) throws Exception{
        Object mManualEJBInstance;
        try{
            mManualEJBInstance = ejbImplClass.newInstance();
            HashMap <Class,Field> fieldNameMap = new HashMap <Class,Field>();
            for (Field field : ejbImplClass.getDeclaredFields()) {
                if(null!=field.getAnnotation(Resource.class)){
                    fieldNameMap.put(Resource.class, field);
                }else if (null!=field.getAnnotation(EJB.class)){
                    fieldNameMap.put(EJB.class, field);
                }
            }
            if (null!=fieldNameMap.get(Resource.class)){                
                Field resourceField = fieldNameMap.get(Resource.class);
                String fieldTypeName = resourceField.getType().getName();
                if (fieldTypeName.equals(SessionContext.class.getName())){
                    SessionContext sessionContext = new ProxySessionContext(BusinessInterface, mManualEJBInstance);
                    resourceField.setAccessible(true);
                    resourceField.set(mManualEJBInstance,sessionContext);
                }
            }
            if (null!=fieldNameMap.get(EJB.class)){               
                Field ejbField = fieldNameMap.get(EJB.class);
                String fieldTypeName = ejbField.getType().getName();
                Object ejb = lookup(fieldTypeName);
                ejbField.setAccessible(true);
                ejbField.set(mManualEJBInstance,ejb);                
            }
            
            HashMap <Class,String> methodNameMap = new HashMap <Class,String>();
            for (Method m : ejbImplClass.getMethods()) {
                if(null!=m.getAnnotation(Resource.class)){
                    methodNameMap.put(Resource.class, m.getName());
                }else if (null!=m.getAnnotation(PostConstruct.class)){
                    methodNameMap.put(PostConstruct.class, m.getName());
                }
            }
            
            if (null!=methodNameMap.get(Resource.class)){
                Method sessionContextMethod =
                    mManualEJBInstance.getClass().getMethod(
                        methodNameMap.get(Resource.class), 
                        new Class[]{javax.ejb.SessionContext.class});                

                // Call the setSessionContext method
                javax.ejb.SessionContext sessionContext = 
                        new ProxySessionContext(BusinessInterface, mManualEJBInstance);
                Object[] sessArgs = new Object[]{sessionContext};
                sessionContextMethod.invoke(mManualEJBInstance, sessArgs);
            }
            
            
            if (null!=methodNameMap.get(PostConstruct.class)){
                Method postConstructMethod = 
                        mManualEJBInstance.getClass().getMethod(
                            methodNameMap.get(PostConstruct.class), null);
                postConstructMethod.invoke(mManualEJBInstance,null);
            }            
        }catch(Exception ex){
            System.err.println("BusinessInterface"+ BusinessInterface.getName());
            System.err.println("ejbImplClass"+ ejbImplClass.getName());
            ex.printStackTrace();
            throw ex;
        }
        return mManualEJBInstance;
    }
    
    /**
     * @see javax.naming.Context
     */    
    public javax.naming.NamingEnumeration list(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */    
    public void close() throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");        
    }
    
    /**
     * @see javax.naming.Context
     */    
    public java.util.Hashtable getEnvironment() 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.String getNameInNamespace() 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.Object addToEnvironment(java.lang.String str, java.lang.Object obj) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void bind(javax.naming.Name name, java.lang.Object obj) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.Object lookupLink(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.Context createSubcontext(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void rebind(javax.naming.Name name, java.lang.Object obj) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void destroySubcontext(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.NamingEnumeration list(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void rename(javax.naming.Name name, javax.naming.Name name1) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void rename(java.lang.String str, java.lang.String str1) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.String composeName(java.lang.String str, java.lang.String str1) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.Name composeName(javax.naming.Name name, javax.naming.Name name1) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.NameParser getNameParser(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void bind(java.lang.String str, java.lang.Object obj) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void destroySubcontext(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void unbind(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.Context createSubcontext(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.Object lookupLink(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void rebind(java.lang.String str, java.lang.Object obj) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public java.lang.Object removeFromEnvironment(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.NamingEnumeration listBindings(java.lang.String str) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.NamingEnumeration listBindings(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public void unbind(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }
    
    /**
     * @see javax.naming.Context
     */
    public javax.naming.NameParser getNameParser(javax.naming.Name name) 
            throws javax.naming.NamingException {
        throw new java.lang.UnsupportedOperationException("Operation not supported by this simulator.");
    }

}
