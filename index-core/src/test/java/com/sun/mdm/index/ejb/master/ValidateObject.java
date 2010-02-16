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
package com.sun.mdm.index.ejb.master;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.validation.exception.ValidationException;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class ValidateObject extends TestCase {
        
    /** Creates new ValidateObject
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public ValidateObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests valid creation
     * @throws Exception an error occured
     */
    public void testValidateObject() throws Exception {
        MasterController mc = MCFactory.getMasterController();
		AddressObject ao = new AddressObject();
		boolean error = false;
		try {
			mc.validateObject(ao);
		} catch (ValidationException ve) {
			System.out.println("ValidateObject(1): Exception thrown: " + ve.getMessage());
		    error = true;
		}
		assertTrue(error);

		// Note: incorrect address type code
		ao.setAddressType("home");
		ao.setAddressLine1("addr1");
		ao.setCity("city");
		ao.setStateCode("CA");
		ao.setPostalCode("90210");
		error = false;
		try {
			mc.validateObject(ao);
		} catch (ValidationException ve) {
			System.out.println("ValidateObject(2): Exception thrown: " + ve.getMessage());
		    error = true;
		}
		assertTrue(error);

		// Fix address type code and recheck
		ao.setAddressType("H");
		error = false;
		try {
			mc.validateObject(ao);
		} catch (ValidationException ve) {
			System.out.println("ValidateObject(3): Exception thrown: " + ve.getMessage());
		    error = true;
		}
		assertTrue(!error);
		}    
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ValidateObject.class));
    }
    
}
