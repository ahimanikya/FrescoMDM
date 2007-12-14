/**
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

package com.sun.mdm.index.loader.log;

import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 *
 */
public class LoaderLogManagerTest extends TestCase {

	
	Logger logger = Logger.getLogger(LoaderLogManagerTest.class.getName());
	
	/**
	 * @param name
	 */
	public LoaderLogManagerTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.sun.mdm.index.loader.log.LoaderLogManager#init()}.
	 */
	public final void testInit() {
		new LoaderLogManager().init();

		for (int i = 0; i < 1000; i++) {

			logger.info("info init loader log");

			logger.severe("severe init loader log");

			logger.fine("fine init loader log");
		}

	}

}
