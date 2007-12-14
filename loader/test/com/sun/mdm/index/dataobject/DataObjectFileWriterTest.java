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

import java.util.ArrayList;
import java.util.logging.Logger;

import com.sun.mdm.index.dataobject.ChildType;
import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.DataObjectFileReader;
import com.sun.mdm.index.dataobject.DataObjectFileWriter;
import com.sun.mdm.index.dataobject.DataObjectReader;
import com.sun.mdm.index.dataobject.DataObjectWriter;

import junit.framework.TestCase;

/**
 * @author Sujit Biswas
 * 
 */
public class DataObjectFileWriterTest extends TestCase {

	/**
	 * logger
	 */
	public static Logger logger = Logger
			.getLogger(DataObjectFileReaderTest.class.getName());

	public DataObjectFileWriterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testWriteDataObject() throws Exception {
		DataObjectReader fr = new DataObjectFileReader(
				"test/data/blockBucket1", true);
		DataObject r = null;
		DataObjectWriter fw = new DataObjectFileWriter(
				"test/data/blockBucket2", true);

		while (true) {

			r = fr.readDataObject();

			if (r == null) {
				break;
			}
			logger.info(r.toString());
			fw.writeDataObject(r);

		}

		fw.close();
		
		
		//read again and compare record
		
		fr = new DataObjectFileReader(
				"test/data/blockBucket1", true);
		
		
		DataObjectReader fr1 = new DataObjectFileReader(
				"test/data/blockBucket2", true);
		
		DataObject r1 = null;
		while (true) {

			r = fr.readDataObject();
			r1 = fr1.readDataObject();

			if (r == null || r1 == null) {
				break;
			}else{
				assertTrue(compare(r, r1));
			}
			logger.info(r.toString());
			

		}
		

	}

	private boolean compare(DataObject r, DataObject r1) {
		
		boolean result= false;
		result =  r1.toString().equals(r.toString());
		
		return result;
		
	}

	public void testWriteCreatedDataObject() throws Exception {
		
		
		
		ArrayList<DataObject> list = new ArrayList<DataObject>();
		
		for (int i = 0; i < 100; i++) {
			list.add(createDataObject());
		}
		
//		DataObjectWriter fw = new DataObjectFileWriter(
//				"test/data/blockBucket2", true);
//		
//		
//		for (int i = 0; i < 100; i++) {
//			fw.writeDataObject(list.get(i));
//		}
//
//		
//
//		fw.close();

	}

	public void testFlush() {
		// fail("Not yet implemented");
	}

	public void testClose() {
		// fail("Not yet implemented");
	}

	private DataObject createDataObject() {

		DataObject person = new DataObject();

		for (int i = 0; i < 10; i++) {
			person.addFieldValue("person-field-" + i);
		}

		DataObject address = new DataObject();

		for (int i = 0; i < 5; i++) {
			address.addFieldValue("address-field-" + i);
		}

		DataObject phone = new DataObject();

		for (int i = 0; i < 5; i++) {
			phone.addFieldValue(null);
		}
		ChildType ct = new ChildType();
		ct.addChild(address);
		person.addChildType(ct);
		
		ct = new ChildType();
		ct.addChild(phone);
		person.addChildType(ct);
		
		
		return person;

	}
}
