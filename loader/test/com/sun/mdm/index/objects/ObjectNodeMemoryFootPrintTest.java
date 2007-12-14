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

import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * @author Sujit Biswas
 * 
 */
public class ObjectNodeMemoryFootPrintTest extends TestCase {

	Logger logger = Logger.getLogger(ObjectNodeMemoryFootPrintTest.class
			.getName());

	/**
	 * @param name
	 */
	public ObjectNodeMemoryFootPrintTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testEpathAPI() throws Exception {
		ObjectNode o = createObject();

		ObjectNode n = o.getChild("Level1", 0).getChild("Level2", 0);

		String s = (String) EPathAPI.getFieldValue("RootObject.Key1", o);
		
		//this is broken ...
		Object s1 = (Object) EPathAPI.getFieldValue(
				"RootObject.Level1[*].Key24", o);

		EPath e = EPathParser.parse("RootObject.Level1[*].Key24");

		ArrayList list = new ArrayList();
		EPathAPI.getFieldList(e, 0, o, list);

		String s2 = (String) EPathAPI.getFieldValue(
				"RootObject.Level1.Level2.Key29", o);

		ArrayList l = new ArrayList();

		EPath e1 = EPathParser.parse("RootObject.Level1[*].Key24");
		EPathAPI.getFieldList(e1, 0, o, l);
		assertNotNull(s);
		assertNotNull(s1);
		assertNotNull(s2);

		l = new ArrayList();

		e1 = EPathParser.parse("RootObject.Level1.Key24");
		EPathAPI.getFieldList(e1, 0, o, l);
		
		l.toString();
	}

	/**
	 * test time taken to create 1 million ObjectNode
	 * 
	 * @throws Exception
	 */
	public void _testCreateObject() throws Exception {

		logger.info("testCreateObject");

		long t1 = System.currentTimeMillis();

		for (int i = 0; i < 1000000; i++) {
			createObject();
		}

		long t2 = System.currentTimeMillis();

		long timeTaken = (t2 - t1) / 1000;

		logger.info("time taken to create 1 million ObjectNode in seconds: "
				+ timeTaken);

	}

	/**
	 * test time taken to create 1 million ObjectNode
	 * 
	 * @throws Exception
	 */
	public void _testCreateObjectAndEPath() throws Exception {

		logger.info("testCreateObjectAndEPath");

		long t1 = System.currentTimeMillis();

		for (int i = 0; i < 1000000; i++) {
			ObjectNode o = createObject();

			String s = (String) EPathAPI.getFieldValue("RootObject.Key1", o);
			String s1 = (String) EPathAPI.getFieldValue(
					"RootObject.Level1.Key24", o);
			String s2 = (String) EPathAPI.getFieldValue(
					"RootObject.Level1.Level2.Key29", o);
		}

		long t2 = System.currentTimeMillis();

		long timeTaken = (t2 - t1) / 1000;

		logger
				.info("time taken to create and access 3 times using Epath 1 million ObjectNode in seconds: "
						+ timeTaken);

	}

	/**
	 * test time taken to create 1 million ObjectNode
	 * 
	 * @throws Exception
	 */
	public void _testEPathAccess() throws Exception {

		logger.info("testCreateObjectAndEPath");

		ObjectNode o = createObject();

		long t1 = System.currentTimeMillis();

		for (int i = 0; i < 1000000; i++) {

			String s = (String) EPathAPI.getFieldValue("RootObject.Key1", o);
			String s1 = (String) EPathAPI.getFieldValue(
					"RootObject.Level1.Key24", o);
			String s2 = (String) EPathAPI.getFieldValue(
					"RootObject.Level1.Level2.Key29", o);
		}

		long t2 = System.currentTimeMillis();

		long timeTaken = (t2 - t1) / 1000;

		logger
				.info("time taken to access 3 times using Epath 1 million ObjectNode in seconds: "
						+ timeTaken);

	}

	@SuppressWarnings("unchecked")
	private ObjectNode createObject() throws Exception {
		ArrayList names = new ArrayList();
		ArrayList types = new ArrayList();
		ArrayList values = new ArrayList();

		for (int i = 0; i < 30; i++) {
			names.add(i, "Key" + i);
			types.add(i, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
			values.add(i, "Value" + i);
		}

		ObjectKey key = new ObjectKey(names, types, values);

		ObjectNode root = new ObjectNode("RootObject", names, types, values);
		ObjectNode lvl1 = new ObjectNode("Level1", names, types, values);
		lvl1.setKeyType("Key1", true);
		lvl1.setKeyType("Key2", true);
		lvl1.setKeyType("Key3", true);
		
		ObjectNode lvl11 = new ObjectNode("Level1", names, types, values);
		//lvl11.setKeyType("Key11", true);
		//lvl11.setKeyType("Key21", true);
		//lvl11.setKeyType("Key31", true);

		ObjectNode lvl2 = new ObjectNode("Level2", names, types, values);
		lvl2.setKeyType("Key1", true);
		lvl2.setKeyType("Key2", true);
		lvl2.setKeyType("Key3", true);
		lvl2.setParent(lvl1);
		lvl1.addChild(lvl2);
		lvl1.setParent(root);
		root.addChild(lvl1);
		
		lvl11.setParent(root);
		root.addChild(lvl11);
		
		return root;
	}

	/**
	 * calculate the footprint for a ObjectNode
	 * 
	 * @throws Exception
	 */
	public void _testMemoryFootPrint() throws Exception {
		// Warm up all classes/methods we will use
		runGC();
		usedMemory();
		// Array to keep strong references to allocated objects
		final int count = 50000;
		Object[] objects = new Object[count];

		long heap1 = 0;
		// Allocate count+1 objects, discard the first one
		for (int i = -1; i < count; ++i) {
			Object object = null;

			// Instantiate your data here and assign it to object

			object = createObject();

			if (i >= 0)
				objects[i] = object;
			else {
				object = null; // Discard the warm up object
				runGC();
				heap1 = usedMemory(); // Take a before heap snapshot
			}
		}
		runGC();
		long heap2 = usedMemory(); // Take an after heap snapshot:

		final int size = Math.round(((float) (heap2 - heap1)) / count);
		logger.info("'before' heap: " + heap1 + ", 'after' heap: " + heap2);
		logger.info("heap delta: " + (heap2 - heap1) + ", {"
				+ objects[0].getClass() + "} size = " + size + " bytes");
		for (int i = 0; i < count; ++i)
			objects[i] = null;
		objects = null;
	}

	private static void runGC() throws Exception {
		// It helps to call Runtime.gc()
		// using several method calls:
		for (int r = 0; r < 4; ++r)
			_runGC();
	}

	private static void _runGC() throws Exception {
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.yield();

			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

	private static long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static final Runtime s_runtime = Runtime.getRuntime();

}
