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
/**
 * 
 */
package com.sun.mdm.index.monitor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.naming.InitialContext;

import com.sun.mdm.index.ejb.master.MasterControllerCore;
import com.sun.mdm.index.ejb.master.MasterControllerRemote;

/**
 * @author Sujit Biswas
 * 
 */
public class StatusMonitor implements StatusMonitorMBean {

	private static Logger logger = Logger.getLogger(StatusMonitor.class
			.getName());

	private String appStatus = "Up";

	private String applicationName = "";

	private String dbStatus = "Up";

	private String webStatus = "Up";

	private MasterControllerCore masterControllerCore;

	public StatusMonitor(MasterControllerCore masterControllerCore,
			String objectName) {
		this.masterControllerCore = masterControllerCore;
		this.applicationName = objectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#getAppStatus()
	 */
	public String getAppStatus() {

		ClassLoader currentThreadClassLoader = null;
		
		try {

			currentThreadClassLoader = Thread.currentThread()
					.getContextClassLoader();
			Thread.currentThread().setContextClassLoader(
					this.getClass().getClassLoader());

			InitialContext i = new InitialContext();

			String jndiName = "ejb/" + applicationName + "MasterController#"
					+ MasterControllerRemote.class.getName();

			MasterControllerRemote c = (MasterControllerRemote)i.lookup(jndiName);
			
			c.toString();

			appStatus = "Up";

		} catch (Exception e) {
			appStatus = "Down";
			logger.info(e.getMessage());
		} finally {
			if ( currentThreadClassLoader != null ) {
                Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
            }
		}

		return appStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#getApplicationName()
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#getDbStatus()
	 */
	public String getDbStatus() {
		try {
			dbStatus = masterControllerCore.getDatabaseStatus();
		} catch (Exception e) {
			dbStatus = "Down";
			logger.info(e.getMessage());
		}

		return dbStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#getWebStatus()
	 */
	public String getWebStatus() {

		try {
			String webURL = "http://localhost:8080/" + applicationName
					+ "edm/login.jsp";

			URL u = new URL(webURL);

			HttpURLConnection http = (HttpURLConnection) u.openConnection();

			http.toString();

			webStatus = "Up";

		} catch (Exception e) {
			webStatus = "Down";
			logger.info(e.getMessage());
		}

		return webStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#setAppStatus(java.lang.String)
	 */
	public void setAppStatus(String status) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#setDbStatus(java.lang.String)
	 */
	public void setDbStatus(String status) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.mdm.index.monitor.StatusMonitorMBean#setWebStatus(java.lang.String)
	 */
	public void setWebStatus(String status) {

	}

}
