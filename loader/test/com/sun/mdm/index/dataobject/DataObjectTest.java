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
package com.sun.mdm.index.dataobject;

import com.sun.mdm.index.dataobject.DataObject;

import junit.framework.TestCase;

public class DataObjectTest extends TestCase {

	public DataObjectTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddChildType() {
		//fail("Not yet implemented");
	}

	public void testAddFieldValue() {
		//fail("Not yet implemented");
	}

	public void testSetFieldValue() {
		DataObject dob = new DataObject();
		
		dob.setFieldValue(3, "hello3");
		dob.setFieldValue(4, "hello4");
		dob.setFieldValue(10, "hello10");
		
		dob.setFieldValue(10, "hello10");
		
		dob.setFieldValue(0, "hello0");
		
		assertNull(dob.getFieldValue(5));
		
	}

	public void testAddChild() {
		//fail("Not yet implemented");
	}

	public void testSetChild() {
		DataObject dob = new DataObject();
		
		dob.addChild(10,  new DataObject());
		
		dob.addChild(3,  new DataObject());
		
		dob.setChild(10,  new DataObject(), 0);
		
		
		assertTrue(  dob.hasChild(10));
		assertTrue(  dob.hasChild(3));
		
		
		assertFalse(dob.hasChild(0));
		
		assertFalse(dob.hasChild(-1));
		
		assertFalse(dob.hasChild(11));
		
		assertFalse(dob.hasChild(12));
		assertFalse(dob.hasChild(120));
		
	}

}
