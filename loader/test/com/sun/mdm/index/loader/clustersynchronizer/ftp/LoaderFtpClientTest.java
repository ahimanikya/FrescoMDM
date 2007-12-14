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

package com.sun.mdm.index.loader.clustersynchronizer.ftp;

import com.sun.mdm.index.loader.clustersynchronizer.ftp.LoaderFtpClient;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 * 
 */
public class LoaderFtpClientTest extends TestCase {

	LoaderFtpClient lftp;

	/**
	 * @param name
	 */
	public LoaderFtpClientTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		lftp = new LoaderFtpClient("sbiswas-acer.stc.com", "sujit", "sujit");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.ftp.LoaderFtpClient#storeFile(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testStoreFile() {
		lftp = new LoaderFtpClient("sbiswas-acer.stc.com", "sujit", "sujit");
		boolean b = lftp.storeFile("/eview/loader", "/eview/loader/",
				"blockBucket1");

		assertTrue(b);
	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.loader.clustersynchronizer.ftp.LoaderFtpClient#retrieveFile(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testRetrieveFile() {
		lftp = new LoaderFtpClient("sbiswas-acer.stc.com", "sujit", "sujit");
		boolean b = lftp.retrieveFile("/eview/loader", "/eview/loader/match/",
				"blockBucket1");

		assertTrue(b);

		// TODO note the change dir happens always with respect to the root dir
		// of the ftp server

		lftp = new LoaderFtpClient("sbiswas-acer.stc.com", "sujit", "sujit");
		
		b = lftp.retrieveFile("/eview/loader/sbr-match",
				"/eview/loader/match/", "blockBucket1");

		assertTrue(b);

	}

}
