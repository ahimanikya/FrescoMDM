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
package com.sun.mdm.index.outbound;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;

/**
 * Test class for ObjectNodeXML
 * @author  gw194542
 */
public class ObjectNodeXMLTest extends TestCase {
    
    /** Creates a new instance of ObjectNodeXMLTest 
     * @param name test name
     */
    public ObjectNodeXMLTest(String name) {
        super(name);
    }
    
    /** Test constructor and methods
     * @throws Exception exception
     */
    public void testClass() throws Exception {
	  int iPos;
	  Calendar createDate = Calendar.getInstance();
	  Calendar updateDate = Calendar.getInstance();
	  createDate.set(1999, 2, 10, 23, 12, 20);
	  updateDate.set(2003, 10, 1, 8, 15, 30);
	  ObjectNodeXML onx = new ObjectNodeXML();

        //Create EnterpriseObject
        EnterpriseObject eo = new EnterpriseObject();
        SBR sbr = new SBR();
        sbr.setChildType("Person");
        sbr.setStatus(SystemObject.STATUS_ACTIVE);
        sbr.setRevisionNumber(0);
	  sbr.setCreateUser("User1");
	  sbr.setCreateFunction(SystemObject.ACTION_ADD);
	  sbr.setCreateDateTime(createDate.getTime());
	  sbr.setUpdateUser("User2");
	  sbr.setUpdateFunction(SystemObject.ACTION_UPDATE);
	  sbr.setUpdateDateTime(updateDate.getTime());
        PersonObject personObject = new PersonObject();
        personObject.setFirstName("JOHN");
        personObject.setLastName("SMITH");
        personObject.setMiddleName("A");
        PhoneObject phoneObject1 = new PhoneObject();
        phoneObject1.setPhoneType("H");
        phoneObject1.setPhone("6264714000");
        personObject.addPhone(phoneObject1);
        PhoneObject phoneObject2 = new PhoneObject();
        phoneObject2.setPhoneType("W");
        phoneObject2.setPhone("6264714000");
        personObject.addPhone(phoneObject2);
        sbr.setObject(personObject);
	  eo.setEUID("10001");
        eo.setSBR(sbr);
        SystemObject so = new SystemObject();
        so.setStatus(SystemObject.STATUS_ACTIVE);
        so.setChildType("Person");
        so.setSystemCode("SBYN");
        so.setLID("1111");
        so.setObject(personObject);
        eo.addSystemObject(so);


	  String xml = onx.constructXml(SystemObject.ACTION_UPDATE, "TRANSID001", eo);
          System.out.println(xml);
	  iPos = xml.indexOf("CreateDateTime=");
	  assertTrue(iPos > 0 && xml.substring(iPos).startsWith("CreateDateTime=\"03/10/1999"));
	  iPos = xml.indexOf("UpdateDateTime=");
	  assertTrue(iPos > 0 && xml.substring(iPos).startsWith("UpdateDateTime=\"11/01/2003"));
          iPos = xml.indexOf("FirstName=\"JOHN");
	  assertTrue(iPos > 0);
          iPos = xml.indexOf("LastName=\"SMITH");
	  assertTrue(iPos > 0);
          iPos = xml.indexOf("PhoneType=\"H");
	  assertTrue(iPos > 0);
          iPos = xml.indexOf("PhoneType=\"W");
	  assertTrue(iPos > 0);
    }
    
    /** Main entry point
     * @param args args
     */
     
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ObjectNodeXMLTest.class));
    }    
    
    
}
