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
package com.sun.mdm.index.objects;

import java.io.RandomAccessFile;
import java.util.logging.Logger;

import junit.framework.TestCase;

public class RandomAccessFileSeekTest extends TestCase {

	private static Logger logger = Logger
			.getLogger(RandomAccessFileSeekTest.class.getName());

	public RandomAccessFileSeekTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSeek() throws Exception {
		RandomAccessFile raf = new RandomAccessFile("test/data/raf", "rw");

		int size = 10000000;
		raf.setLength(size);

		raf.seek(0);
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			raf.read();

		}
		long t2 = System.currentTimeMillis();

		logger.info("time taken to read in seconds: " + (t2 - t1) / 1000);

		raf.seek(0);
		t1 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			raf.seek(i);
			raf.read();

		}
		t2 = System.currentTimeMillis();

		logger.info("time taken to read in seconds: " + (t2 - t1) / 1000);

	}

}
