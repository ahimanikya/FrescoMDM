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
/**
 * 
 */
package com.sun.mdm.index.dataobject.objectdef;

import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 *
 */
public class ObjectDefinitionTest extends TestCase {

	/**
	 * @param name
	 */
	public ObjectDefinitionTest(String name) {
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
	 * Test method for {@link com.sun.mdm.index.dataobject.objectdef.ObjectDefinition#setField(int, com.sun.mdm.index.dataobject.objectdef.Field)}.
	 */
	public void testSetField() {
		//fail("Not yet implemented");
		
		ObjectDefinition od = new ObjectDefinition("person");
		od.setField(10, new Field());
		
		od.toString();
	}

	/**
	 * Test method for {@link com.sun.mdm.index.dataobject.objectdef.ObjectDefinition#setchild(int, com.sun.mdm.index.dataobject.objectdef.ObjectDefinition)}.
	 */
	public void testSetchild() {
		ObjectDefinition od = new ObjectDefinition("person");
		od.setchild(10, new ObjectDefinition("Address"));
		
		od.toString();
	}

	/**
	 * Test method for {@link com.sun.mdm.index.dataobject.objectdef.ObjectDefinition#setName(java.lang.String)}.
	 */
	public void testSetName() {
		//fail("Not yet implemented");
	}

}
