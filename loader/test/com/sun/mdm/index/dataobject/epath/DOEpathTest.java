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
package com.sun.mdm.index.dataobject.epath;

import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.sun.mdm.index.Resource;
import com.sun.mdm.index.dataobject.ChildType;
import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.epath.InvalidFieldException;
import com.sun.mdm.index.dataobject.objectdef.Field;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.dataobject.objectdef.ObjectDefinition;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;

/**
 * @author Sujit Biswas
 * 
 */
public class DOEpathTest extends TestCase {

	private static Logger logger = Logger.getLogger(
			DOEpathTest.class.getName(), Resource.BUNDLE_NAME);

	DataObject parent;

	ObjectDefinition objectDef;

	Lookup lookup;

	/**
	 * @param name
	 */
	public DOEpathTest(String name) {
		super(name);

	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		parent = createDataObject();
		objectDef = createObjectDefinition();
		lookup = Lookup.createLookup(objectDef);

		// logger.log(Level.INFO, "test");

		Resource.getProperty("test");
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
	 * {@link com.sun.mdm.index.dataobject.epath.DOEpath#getFieldValue(com.sun.eview.objects.epath.EPath, com.sun.mdm.index.dataobject.DataObject, com.sun.mdm.index.dataobject.objectdef.Lookup)}.
	 */
	public void testGetFieldValue() throws Exception {

		EPath e = EPathParser.parse("Person.Address.city");
		String s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals("Duarte", s);

		//field exist
		e = EPathParser.parse("Person.Address[0].city");
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals("Duarte", s);
		
		//field does not exist
		e = EPathParser.parse("Person.Address[0].state");
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals(null, s);

		
		//missing child instance
		e = EPathParser.parse("Person.Address[5].city");
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertNull(s);

		// should be used with field list
		e = EPathParser.parse("Person.Address[*].city");
		try {
			s = DOEpath.getFieldValue(e, parent, lookup);
			assertTrue(false);
		} catch (EPathException e1) {
			assertTrue(true);
		}

		// 3 level
		e = EPathParser.parse("Person.Phone.PhoneChild.companyInfo");
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals("Verizon", s);

		// child instance does not exist
		try {
			e = EPathParser.parse("Person.Phone[0].PhoneChild[2].companyInfo");
			s = DOEpath.getFieldValue(e, parent, lookup);
			assertNull(s);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		// field value not set
		e = EPathParser.parse("Person.Address[1].country");
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertNull(s);

		// invalid field
		try {
			e = EPathParser.parse("Person.Address[1].country_");
			s = DOEpath.getFieldValue(e, parent, lookup);
			assertTrue(false);
		} catch (InvalidFieldException e1) {
			assertTrue(true);
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		e = EPathParser.parse("Person.firstname");
		s = DOEpath.getFieldValue(e, parent, lookup);

		assertTrue(s.equals("John"));

		try {
			e = EPathParser.parse("Person.Alias.fname");
			s = DOEpath.getFieldValue(e, parent, lookup);
			assertNull(s);
		} catch (Exception ex) {
			logger.info(ex.getMessage() + " : " + e.toString());
		}

		try {
			e = EPathParser.parse("Person.Alias_.fname");
			s = DOEpath.getFieldValue(e, parent, lookup);
		} catch (EPathException ex) {
			logger.info(ex.getMessage() + " : " + e.toString());
		}

	}

	/**
	 * Test method for
	 * {@link com.sun.mdm.index.dataobject.epath.DOEpath#setFieldValue(com.sun.eview.objects.epath.EPath, com.sun.mdm.index.dataobject.DataObject, java.lang.String, com.sun.mdm.index.dataobject.objectdef.Lookup)}.
	 */
	public void testSetFieldValue() throws Exception {

		EPath e = EPathParser.parse("Person.Address[1].country");
		DOEpath.setFieldValue(e, parent, "USA", lookup);
		String s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals("USA", s);

		// child instance does not exist
		try {
			e = EPathParser.parse("Person.Address[2].country");
			DOEpath.setFieldValue(e, parent, "USA", lookup);
		} catch (Exception e1) {
			assertTrue(true);
		}

		// field name does not exist
		try {
			e = EPathParser.parse("Person.Address[1].country_");
			DOEpath.setFieldValue(e, parent, "USA", lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
			assertTrue(true);
		}

		// not valid while setting a field value
		e = EPathParser.parse("Person.Address[*].city");

		try {
			DOEpath.setFieldValue(e, parent, "Monrovia", lookup);
			assertTrue(false);
		} catch (Exception e1) {
			assertTrue(true);
		}

		e = EPathParser.parse("Person.firstname");
		DOEpath.setFieldValue(e, parent, "Henry", lookup);
		s = DOEpath.getFieldValue(e, parent, lookup);
		assertEquals("Henry", s);

		try {
			e = EPathParser.parse("Person.Alias.fname");
			DOEpath.setFieldValue(e, parent, "hello", lookup);
		} catch (Exception ex) {
			logger.info(ex.getMessage() + " : " + e.toString());
		}

		try {
			e = EPathParser.parse("Person.Alias_.fname");
			DOEpath.setFieldValue(e, parent, "hello", lookup);
		} catch (Exception ex) {
			logger.info(ex.getMessage() + " : " + e.toString());
		}

		try {
			e = EPathParser.parse("Address.city");
			DOEpath.setFieldValue(e, parent, "hello", lookup);
		} catch (Exception ex) {
			logger.info(ex.getMessage() + " : " + e.toString());
		}

	}

	public void testGetFieldValueList() throws Exception {

		EPath e = EPathParser.parse("Person.Address[*].city");
		List<String> list = DOEpath.getFieldValueList(e, parent, lookup);
		assertTrue(list.size() == 2);

		// try a single field value
		e = EPathParser.parse("Person.Address.city");
		list = DOEpath.getFieldValueList(e, parent, lookup);
		assertTrue(list.size() == 1);

		// invalid field name
		try {
			e = EPathParser.parse("Person.Address[*].city_");
			list = DOEpath.getFieldValueList(e, parent, lookup);
		} catch (Exception e1) {
			assertTrue(true);
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		// invalid child name
		try {
			e = EPathParser.parse("Person.Address_[*].city");
			list = DOEpath.getFieldValueList(e, parent, lookup);
		} catch (Exception e1) {
			assertTrue(true);
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		// valid epath for getting a single field value as list, this is done to
		// maintain the behaviour same as that of the Original Object Node API
		try {
			e = EPathParser.parse("Person.Address[0].city");
			list = DOEpath.getFieldValueList(e, parent, lookup);

			assertTrue(list.size() == 1);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
			assertTrue(false);
		}

		try {
			e = EPathParser.parse("Person.Alias.fname");
			list = DOEpath.getFieldValueList(e, parent, lookup);
			assertTrue(list.size() == 1);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
			assertTrue(true);
		}

		try {
			e = EPathParser.parse("Person.Phone.phoneno");
			list = DOEpath.getFieldValueList(e, parent, lookup);
			assertTrue(list.size() == 1);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
			assertTrue(true);
		}
		
		try {
			e = EPathParser.parse("Person.Alias[*].fname");
			list = DOEpath.getFieldValueList(e, parent, lookup);
			assertTrue(list.size() == 1);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
			assertTrue(true);
		}

	}

	public void testGetDataObject() throws Exception {

		EPath e = EPathParser.parse("Person.Address");
		DataObject d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Duarte"));

		e = EPathParser.parse("Person.Address[1]");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Monrovia"));

		e = EPathParser.parse("Person.Address[10]");
		try {
			DOEpath.getDataObject(e, parent, lookup);
		} catch (Exception e2) {
			logger.info(e2.getMessage() + " : " + e.toString());
		}

		// invalid path
		try {
			e = EPathParser.parse("Person.Address[1].x");
			d = DOEpath.getDataObject(e, parent, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		// invalid child instance
		try {
			e = EPathParser.parse("Person.Address[2]");
			d = DOEpath.getDataObject(e, parent, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		e = EPathParser.parse("Person.Phone[0].PhoneChild");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Verizon"));

		try {
			e = EPathParser.parse("Person.Alias");
			d = DOEpath.getDataObject(e, parent, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

		try {
			e = EPathParser.parse("Person.Alias_");
			d = DOEpath.getDataObject(e, parent, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

	}

	public void testSetDataObject() throws Exception {

		EPath e = EPathParser.parse("Person.Address");
		DataObject d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Duarte"));
		e = EPathParser.parse("Person.Address[1]");
		DOEpath.setDataObject(e, parent, d, lookup);

		e = EPathParser.parse("Person.Address[10]");
		try {
			DOEpath.setDataObject(e, parent, d, lookup);
		} catch (Exception e2) {
			logger.info(e2.getMessage() + " : " + e.toString());
		}

		e = EPathParser.parse("Person.Address[1]");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Duarte"));

		e = EPathParser.parse("Person.Phone[0].PhoneChild");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Verizon"));

		DOEpath.addDataObject(e, parent, d, lookup);
		DOEpath.addDataObject(e, parent, d, lookup);
		e = EPathParser.parse("Person.Phone[0].PhoneChild[2]");

		d = new DataObject();
		d.setFieldValue(0, "AT-and-T");
		d.setFieldValue(5, "chicago");
		DOEpath.setDataObject(e, parent, d, lookup);

		// invalid epath
		try {
			e = EPathParser.parse("Person.Phone[0].PhoneChild_");
			DOEpath.setDataObject(e, parent, d, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}
		// set only replaces and existing dataObject
		try {
			e = EPathParser.parse("Person.Alias");
			DOEpath.setDataObject(e, parent, d, lookup);
		} catch (Exception e1) {
			logger.info(e1.getMessage() + " : " + e.toString());
		}

	}

	public void testAddDataObject() throws Exception {

		EPath e = EPathParser.parse("Person.Address");

		// add the 3rd address instance
		DataObject d = new DataObject();
		d.setFieldValue(0, "Bangalore");
		DOEpath.addDataObject(e, parent, d, lookup);
		e = EPathParser.parse("Person.Address[2]");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Bangalore"));

		d = new DataObject();
		d.setFieldValue(0, "fname");
		e = EPathParser.parse("Person.Alias");
		DOEpath.addDataObject(e, parent, d, lookup);

		// add the 2nd phone child
		e = EPathParser.parse("Person.Phone[0].PhoneChild");
		d = new DataObject();
		d.setFieldValue(0, "hutch");
		DOEpath.addDataObject(e, parent, d, lookup);
		e = EPathParser.parse("Person.Phone[0].PhoneChild[1].companyInfo");
		String s = DOEpath.getFieldValue(e, parent, lookup);
		assertTrue(s.equals("hutch"));

		d = new DataObject();
		d.addFieldValue("parent-field");
		e = EPathParser.parse("Person.Phone");

		DataObject phone = new DataObject();
		phone.addFieldValue("phone-field");
		DOEpath.addDataObject(e, d, phone, lookup);

		d.toString();

	}

	public void testGetDataObjectList() throws Exception {

		EPath e = EPathParser.parse("Person.Address");
		DataObject d = DOEpath.getDataObjectList(e, parent, lookup).get(0);
		assertTrue(d.getFieldValue(0).equals("Duarte"));

		DOEpath.addDataObject(e, parent, d, lookup);

		e = EPathParser.parse("Person.Address[*]");
		d = DOEpath.getDataObjectList(e, parent, lookup).get(2);
		assertTrue(d.getFieldValue(0).equals("Duarte"));

		e = EPathParser.parse("Person.Phone[0].PhoneChild");
		d = new DataObject();
		d.setFieldValue(0, "Airtel");

		DOEpath.addDataObject(e, parent, d, lookup);

		e = EPathParser.parse("Person.Phone[0].PhoneChild[1]");
		d = DOEpath.getDataObject(e, parent, lookup);
		assertTrue(d.getFieldValue(0).equals("Airtel"));

		e = EPathParser.parse("Person.Alias[*]");
		List<DataObject> l = DOEpath.getDataObjectList(e, parent, lookup);

		assertNull(l);

		e = EPathParser.parse("Person.Alias");
		l = DOEpath.getDataObjectList(e, parent, lookup);

		assertNull(l);

	}

	private DataObject createDataObject() {

		// firstname|lastname#$city|zip#$phoneno|phoneCompany

		DataObject person = new DataObject();

		DataObject address1 = new DataObject();
		DataObject address2 = new DataObject();
		ChildType addressCT = new ChildType();
		addressCT.addChild(address1);
		addressCT.addChild(address2);

		DataObject phone = new DataObject();
		ChildType phoneCT = new ChildType();
		phoneCT.addChild(phone);

		person.addChildType(addressCT);
		person.addChildType(phoneCT);

		person.addFieldValue("John");
		person.addFieldValue("Doe");

		address1.addFieldValue("Duarte");
		address1.addFieldValue("91010");

		address2.addFieldValue("Monrovia");
		address2.addFieldValue("91016");

		phone.addFieldValue("626-999-9999");
		phone.addFieldValue("charter");

		DataObject phoneChild = new DataObject();
		ChildType phoneChildCT = new ChildType();
		phoneChildCT.addChild(phoneChild);
		phone.addChildType(phoneChildCT);

		phoneChild.addFieldValue("Verizon");
		phoneChild.addFieldValue("Texas");

		return person;

	}

	private ObjectDefinition createObjectDefinition() {

		// firstname|lastname#$city|zip#$phoneno|phoneCompany

		ObjectDefinition person = new ObjectDefinition("Person");

		ObjectDefinition address = new ObjectDefinition("Address");

		ObjectDefinition phone = new ObjectDefinition("Phone");

		ObjectDefinition phoneChild = new ObjectDefinition("PhoneChild");

		ObjectDefinition alias = new ObjectDefinition("Alias");

		person.addchild(address);
		person.addchild(phone);
		person.addchild(alias);
		phone.addchild(phoneChild);

		person.addField(newField("firstname"));
		person.addField(newField("lastname"));

		address.addField(newField("city"));
		address.addField(newField("zip"));
		address.addField(newField("state"));
		address.addField(newField("country"));

		phone.addField(newField("phoneno"));
		phone.addField(newField("phoneCompany"));

		phoneChild.addField(newField("companyInfo"));
		phoneChild.addField(newField("companyAddress"));

		alias.addField(newField("fname"));
		alias.addField(newField("lname"));

		return person;

	}

	private Field newField(String name) {

		Field f = new Field();

		f.setName(name);

		return f;
	}

}
