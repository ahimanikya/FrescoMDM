/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)QwsController.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.multidomain.services.security;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

import com.sun.mdm.index.ejb.codelookup.CodeLookup;
import com.sun.mdm.index.ejb.codelookup.UserCodeLookup;
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;
import com.sun.mdm.index.util.ConnectorParamReader;

import com.sun.mdm.multidomain.services.security.util.DateUtil;
import com.sun.mdm.multidomain.services.security.util.QwsUtil;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**The QWS controller
 * @author xsong
 */
public class QwsController {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.security.QwsController");
    private transient static final Localizer mLocalizer = Localizer.get();

    private static CodeLookup val = null;
    private static UserCodeLookup usercode = null;
    
    /**
     * Private constructor to prevent direct instantiation of QwsController
     * class.
     */
    private QwsController() {
    }

    /**
     * to check the user login is valid or not based on the userid and the request handle.
     *
     * @param userId userid
     * @param request the request handle 
     * @return a user profile
     * @exception Exception if signOn fails.
     */
    public static UserProfile signOn(String userId, HttpServletRequest request) throws Exception {
        return new UserProfile(userId, request);  // may throw another exception here
    }

    //Used by init to get root cause of a RemoteException
    private static Throwable getRootCause(Exception e) {
        Throwable t = e;
        if (e instanceof RemoteException) {
            Throwable t2 = ((RemoteException)e).getCause();
            if (t2 != null) {
                if (t2 instanceof InvocationTargetException) {
                    Throwable t3 = ((InvocationTargetException)t2).getTargetException();
                    if (t3 != null) {
                        t = t3;
                    }
                } else {
                    t = t2;
                }
            }
        }
        return t;
    }

    public static void init() throws Exception {
        if (val == null) {
            try {
                pgetValidationService();
            } catch (Exception e) {
                val = null;
                throw new Exception (mLocalizer.t("COM501: Cannot connect to the ValidationService: {0}", getRootCause(e)));
            }
        }
        
        if (usercode == null) {
            try {
                pgetUserCodeLookup();
            } catch (Exception e) {
                usercode = null;
                throw new Exception (mLocalizer.t("COM502: Cannot connect to the UserCodeLookup Service: {0}", getRootCause(e)));
            }
        }
    }

    private static void pgetValidationService() throws Exception {
        if (val != null) {
            return;
        }
        MDConfigManager cm = MDConfigManager.getInstance();
        String appServerUrl = null;// ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
        // testing--raymond tam
        // RESUME HERE
        // Need to invoke a Validation service
//        val = (CodeLookup)getInitialContext(appServerFactory, appServerUrl, 
//            appServerObjectFactories).lookup(cm.getValidationServiceJndi());
        val = null;
    }

    private static void pgetUserCodeLookup() throws Exception {
        if (usercode != null) {
            return;
        }
        MDConfigManager cm = MDConfigManager.getInstance();
        String appServerUrl = null; //ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
          // testing--raymond tam
        // RESUME HERE
        // Need to invoke a user code look upservice
//      usercode = (UserCodeLookup)getInitialContext(appServerFactory, appServerUrl, 
//            appServerObjectFactories).lookup(cm.getUserCodeLookupJndi());       
        usercode = null;
    }
    
    /**
     * @todo Document: Getter for CodeLookup attribute of the CodeLookup EJB
     * @return the code lookup engine
     */
    public static CodeLookup getValidationService() {
        return val;
    }
    
    /**
     * @todo Document: Getter for UserCodeLookup attribute of the UserCodeLookup EJB
     * @return the user code lookup engine
     */
    public static UserCodeLookup getUserCodeLookup() {
        return usercode;
    }

    private static InitialContext getInitialContext(String factoryName, String serverUrl, String objectFactories) 
        throws Exception {
        InitialContext cxt = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Factory Name: [" + factoryName + "]");
                mLogger.fine("Server URL: [" + serverUrl + "]");
                mLogger.fine("Object Factories: [" + objectFactories + "]");
            }
            
            if (factoryName != null && factoryName.length() > 0
                && serverUrl != null && serverUrl.length() > 0) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("creating initial context by using url and factory");
                }
                
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, factoryName);
                env.put(Context.PROVIDER_URL, serverUrl);
                if (objectFactories != null && objectFactories.length() > 0) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Setting Object Factories");
                    }
                    env.put(Context.OBJECT_FACTORIES, objectFactories);
                }
                cxt = new InitialContext(env);
            } else {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("creating default initial context");
                }
                
                cxt = new InitialContext();
            }
        } catch (NamingException e) {
            throw new Exception(mLocalizer.t("COM504: EDM couldn't get initial context: " 
                + "factoryName={0}, serverURL={1}, objectFactories={2}, exception message={3}", 
                factoryName, serverUrl, objectFactories, getRootCause(e)));
        }

        return cxt;
    }

}
