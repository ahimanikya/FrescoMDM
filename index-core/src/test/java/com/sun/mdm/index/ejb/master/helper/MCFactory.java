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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.ejbproxy.EJBTestProxy;

import java.rmi.RemoteException;  

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.ejb.CreateException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;

import javax.sql.DataSource;


/** Factor for MasterController objects and connections
 * @author dcidon
 */
public class MCFactory {
    private static MasterController mMasterController;
    private static Context mContext;
    private static final String PROXY_JNDI = "com.sun.mdm.index.util.ejbproxy.ProxyJNDI";

    /** Creates a new instance of MCFactory */
    public MCFactory() {
    }

    /** Get master controller based on server parameter
     * @return Master controller handle
     * @throws RemoteException An error occured
     * @throws NamingException An error occured
     * @throws CreateException An error occured
     */
    public static MasterController getMasterController()
                                                throws RemoteException, 
                                                       NamingException, 
                                                       CreateException {
        if (mMasterController == null) {
            Context jndiContext = getContext();
            String jndiName = null;

            if (TestConstants.USE_EJB_PROXY) {
                jndiName = JNDINames.EJB_REF_MASTER;
            } else {
                jndiName = "ejb/MasterController";
            }
            mMasterController = (MasterController)jndiContext.lookup(jndiName);
        }

        return mMasterController;
    }

    /** Get connection from app server
     * @return connection
     * @throws NamingException An error occured
     * @throws SQLException An error occured
     */
    public static Connection getConnection() throws NamingException, 
                                                    SQLException {
        Context ctx = getContext();
        String jndiName = null;
        if (TestConstants.USE_EJB_PROXY) {
            jndiName = JNDINames.BBE_DATASOURCE;
        } else {
            jndiName = "jdbc/BBEDataSource";
        }
        
        //Create a new DataSource by Locating It in the Naming Service
        DataSource ds = (DataSource) ctx.lookup(jndiName);

        //Get a new JDBC connection from the DataSource:
        Connection con=null;
        try{
            con = ds.getConnection();
        }catch(Exception ex){
            System.out.println(ex);
        }

        return con;
    }

    /** Get intial context
     * @return context
     * @throws NamingException error  
     */
    public static Context getContext() throws NamingException { 
        if (mContext == null) {
            if (TestConstants.USE_EJB_PROXY) {
                mContext = EJBTestProxy.getInitialContext();
            } else {
                ResourceBundle jndiSettings = null;
                Hashtable env = new Hashtable();

                try {
                    jndiSettings = ResourceBundle.getBundle(PROXY_JNDI);
                } catch (MissingResourceException ex) {
                    System.out.println("Warning: Could not find "  
                                       + PROXY_JNDI + " properties file."  
                                       + " Unknown JNDI names will not be forwarded to another JNDI provider.");
                }

                if (jndiSettings != null) {
                    System.out.println("Loading JNDI provider settings in "  
                                       + PROXY_JNDI + " properties file.");

                    Enumeration keysEnum = jndiSettings.getKeys();

                    while (keysEnum.hasMoreElements()) {
                        String key = (String) keysEnum.nextElement();
                        if (!key.startsWith("proxy")) {
                            String value = jndiSettings.getString(key);
                            System.out.println("Setting key: " + key  
                                               + " to value: " + value);
                            env.put(key, value);
                        }
                    }
                } else {
                    System.out.println(
                            "Warning: jndiSettings null when loading "  
                            + PROXY_JNDI + " properties file.");
                }

                mContext = new InitialContext(env);
            }
        }

        return mContext;
    }

    /** Test method
     * @param args none
     */    
    public static void main(String[] args) {
        try {
            //test getting connection
            System.out.println("Getting connection");
            getMasterController();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
