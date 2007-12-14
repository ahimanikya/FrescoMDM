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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 *
 */
public class SerializedObjectTest extends TestCase {

	/**
	 * @param name
	 */
	public SerializedObjectTest(String name) {
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
	
	
	public void testSerialization() throws Exception{
		SerializedObject s = new SerializedObject();
		
		s.setField1("hello");
		s.setField2(100);
		
		FileOutputStream fos = new FileOutputStream("test/data/serialized.txt");
		
		ObjectOutputStream obs = new ObjectOutputStream(fos);
		
		obs.writeObject(s);
		
		obs.close();
		
		FileInputStream fis = new FileInputStream("test/data/serialized.txt");
		
		ObjectInputstreamForBC ois = new ObjectInputstreamForBC(fis);
		
		DeSerializedObject ds =   (DeSerializedObject) ois.readObject();
		
		ds.toString();
		
		
	}
	
	public void testSerializationWithActualDescriptor() throws Exception{
		S_ObjectNode s = new S_ObjectNode();
		
		
		
		FileOutputStream fos = new FileOutputStream("test/data/serialized1.txt");
		
		ObjectOutputStream obs = new ObjectOutputStream(fos);
		
		obs.writeObject(s);
		
		obs.close();
		
		FileInputStream fis = new FileInputStream("test/data/serialized1.txt");
		
		ObjectInputstreamForBC ois = new ObjectInputstreamForBC(fis);
		
		D_ObjectNode ds =   (D_ObjectNode) ois.readObject();
		
		ds.toString();
		
		Logger.global.info(ds.toString());
		
		
	}
	

}
