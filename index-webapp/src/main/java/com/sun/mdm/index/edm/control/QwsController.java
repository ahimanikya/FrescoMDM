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
package com.sun.mdm.index.edm.control;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.ejb.report.ReportGenerator;
import com.sun.mdm.index.ejb.codelookup.CodeLookup;
import com.sun.mdm.index.ejb.codelookup.UserCodeLookup;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.util.ConnectorParamReader;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
/**The QWS controller
 * @author xsong
 */
public class QwsController {
    private static MasterController mc = null;
    private static CodeLookup val = null;
    private static UserCodeLookup usercode = null;
    private static ReportGenerator rptGen = null;
    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.qws.control.QwsController");

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
        if (mc == null) {
            try {
                pgetMasterController();
            } catch (Exception e) {
                mc = null;
                mLogger.error("Error occurs while connecting to MasterController: ", e);
                throw new Exception ("Cannot connect to MasterController: " + getRootCause(e));
            }
        }
        if (val == null) {
            try {
                pgetValidationService();
            } catch (Exception e) {
                val = null;
                mLogger.error("Error occurs while connecting to ValidationService: ", e);
                throw new Exception ("Cannot connect to ValidationService: " + getRootCause(e));
            }
        }
        
        if (usercode == null) {
            try {
                pgetUserCodeLookup();
            } catch (Exception e) {
                usercode = null;
                mLogger.error("Error occurs while connecting to UserCodeLookup Service: ", e);
                throw new Exception ("Cannot connect to UserCodeLookup Service: " + getRootCause(e));
            }
        }
        
        if (rptGen == null) {
            try {
                pgetReportGenerator();
            } catch (Exception e) {
                usercode = null;
                mLogger.error("Error occurs while connecting to ReportGenerator Service: ", e);
                throw new Exception ("Cannot connect to ReportGenerator Service: " + getRootCause(e));
            }
        }
    }

    private static void pgetMasterController() throws Exception {
        if (mc != null) {
            return;
        }
        ConfigManager cm = ConfigManager.getInstance();
        String appServerUrl = null; //ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
        mc = (MasterController)getInitialContext(appServerFactory, appServerUrl,
                appServerObjectFactories).lookup(cm.getMasterControllerJndi());
    }

    private static void pgetValidationService() throws Exception {
        if (val != null) {
            return;
        }
        ConfigManager cm = ConfigManager.getInstance();
        String appServerUrl = null;// ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
        val = (CodeLookup)getInitialContext(appServerFactory, appServerUrl, 
            appServerObjectFactories).lookup(cm.getValidationServiceJndi());
        // CodeLookupHome home = (CodeLookupHome) PortableRemoteObject.narrow(obj, CodeLookupHome.class);
        //Object obj2 =  PortableRemoteObject.narrow(obj, CodeLookupHome.class);
        //CodeLookupHome home = (CodeLookupHome)(obj2);
    }

    private static void pgetUserCodeLookup() throws Exception {
        if (usercode != null) {
            return;
        }
        ConfigManager cm = ConfigManager.getInstance();
        String appServerUrl = null; //ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
        usercode = (UserCodeLookup)getInitialContext(appServerFactory, appServerUrl, 
            appServerObjectFactories).lookup(cm.getUserCodeLookupJndi());       
    }
    
    private static void pgetReportGenerator() throws Exception {
        if (rptGen != null) {
            return;
        }
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.getReportGeneratorJndi() == null) {
            return;
        }
        String appServerUrl = null; //ConnectorParamReader.getProviderUrl();
        String appServerFactory = null; //ConnectorParamReader.getInitialContextFactory();
        String appServerObjectFactories = null;  //Not used?
        rptGen = (ReportGenerator)getInitialContext(appServerFactory, appServerUrl, 
            appServerObjectFactories).lookup(cm.getReportGeneratorJndi());
    }

    /**
     * @todo Document: Getter for MasterController attribute of the
     *      MasterControllerProxy object
     * @return the master controller
     */
    public static MasterController getMasterController() {
        return mc;
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
    
    /**
     * @todo Document: Getter for rptGen attribute of the Report Generator EJB
     * @return the report generator engine
     */
    public static ReportGenerator getReportGenerator() {
        return rptGen;
    }

    private static InitialContext getInitialContext(String factoryName, String serverUrl, String objectFactories) 
        throws Exception {
        InitialContext cxt = null;
        try {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("Factory Name: [" + factoryName + "]");
                mLogger.debug("Server URL: [" + serverUrl + "]");
                mLogger.debug("Object Factories: [" + objectFactories + "]");
            }
            
            if (factoryName != null && factoryName.length() > 0
                && serverUrl != null && serverUrl.length() > 0) {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("creating initial context by using url and factory");
                }
                
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, factoryName);
                env.put(Context.PROVIDER_URL, serverUrl);
                if (objectFactories != null && objectFactories.length() > 0) {
                    if (mLogger.isDebugEnabled()) {
                        mLogger.debug("Setting Object Factories");
                    }
                    env.put(Context.OBJECT_FACTORIES, objectFactories);
                }
                cxt = new InitialContext(env);
            } else {
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("creating default initial context");
                }
                
                cxt = new InitialContext();
            }
        } catch (NamingException e) {
            mLogger.error("Error occurs while EDM gets initial context: ", e);
            throw new Exception("EDM couldn't get initial context: " 
                + factoryName + "|" + serverUrl + "|" + objectFactories);
        }

        return cxt;
    }

}
