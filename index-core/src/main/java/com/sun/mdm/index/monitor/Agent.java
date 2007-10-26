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

import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.sun.mdm.index.ejb.master.MasterControllerCore;

/**
 * JMX agent
 * 
 * @author Sujit Biswas
 * 
 */
public class Agent {

	private static Logger logger = Logger.getLogger(Agent.class.getName());

	private MBeanServer mbs;

	private MasterControllerCore masterControllerCore;

	/**
	 * default constructor, to use when the monitor is run within the sun java
	 * application server
	 * 
	 * @param masterControllerCore
	 * @param beanServer
	 */
	public Agent(MBeanServer beanServer,
			MasterControllerCore masterControllerCore) {

		this.mbs = beanServer;
		this.masterControllerCore = masterControllerCore;

	}

	public MBeanServer getMBeanServer() {
		return mbs;
	}

	public void registerMBean(String objectName) {

		try {

			// TODO read the object from properties file

			ObjectName name = getObjectName(objectName);
			if (!mbs.isRegistered(name)) {
				StatusMonitorMBean object = new StatusMonitor(
						masterControllerCore,objectName);
				mbs.registerMBean(object, name);

				logger.info("Registering  Monitoring Mbean: " + name);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public void unregisterMBean(String objectName) {

		try {

			ObjectName name = getObjectName(objectName);
			if (mbs.isRegistered(name)) {
				mbs.unregisterMBean(name);
				logger.info("UnRegistering  Monitoring Mbean: " + name);
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * @param objectName
	 * @return
	 * @throws MalformedObjectNameException
	 */
	private ObjectName getObjectName(String objectName)
			throws MalformedObjectNameException {
		ObjectName name = new ObjectName(
				"com.sun.mdm.index:applicationType=mural,applicationName="
						+ objectName);
		return name;
	}

}
