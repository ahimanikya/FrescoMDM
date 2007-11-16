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

package com.sun.mdm.index.matching;

import com.sun.mdm.index.ejb.master.helper.MCFactory;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import java.util.ArrayList;

/**
 * MatchEngineController unit test
 */
public class MatchEngineControllerTest extends TestCase {

    private MatchEngineController mec;
    private SystemObject testSysObj;
    
    /** 
     * Creates new MatchEngineControllerTester      
     * @see junit.framework.TestCase
     */
    public MatchEngineControllerTest(String name) {
        super(name);
    }
    
    /** 
     * Set up the unit test
     * @see junit.framework.TestCase
     */        
    protected void setUp() throws Exception {
        MCFactory.getContext();
        mec = MatchEngineControllerFactory.getInstance();
        
        // Create SystemObject test fixture
        testSysObj = createTestSysObj();
    }

    /** 
     * Tear down the unit test
     * @see junit.framework.TestCase
     */    
    protected void tearDown() {
        // cleanup code
    }

    /**
     * Test the MatchEngineController
     * requires that the Dbbe.home (e.g. -Dbbe.home=c:\elephant\dist)
     * property is set for loading the configuration
     */
    public void testMatchEngineController() { 
        try {
            // Standardize the SystemObject first
            SystemObject result = null;
            try {
                result = mec.standardize(testSysObj);
            } catch (Exception ex) {
                throw new RuntimeException("Failed standardization unit test", ex);
            }

            // Test that we got a result
            assertNotNull(result);
            
            System.out.println(result);            
            
            // Match SystemObject against DB
            try {
                EOSearchCriteria crit = new EOSearchCriteria(result);
                String searchId = "BLOCKER-SEARCH";
                com.sun.mdm.index.objects.epath.EPathArrayList fields = null;
                EOSearchOptions opts = new EOSearchOptions(searchId, fields);
                MatchOptions matchOptions = new MatchOptions();
                ArrayList matchResult = mec.findMatch(crit, opts, matchOptions);
                
                //? temporary test
                System.out.println("Number of match results: " + matchResult.size());
                java.util.Iterator resultIter = matchResult.iterator();
                while (resultIter.hasNext()) {
                    ScoreElement elem = (ScoreElement) resultIter.next();
                    System.out.println("EUID: " + elem.getEUID() + " score: " + elem.getWeight());
                }

            } catch (Exception ex) {
                throw new RuntimeException("Failed matching unit test", ex);
            }            
                        
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
    /**
     * Main method needed to make a self runnable class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MatchEngineControllerTest.class));
    }    

    /**
     * Create a test system object
     * @return a SystemObject to test with
     * @throws ObjectException if constructing or accessing parts of the Object failed
     * @throws SystemObjectException if constructing or accessing the SystemObject failed
     */    
    public SystemObject createTestSysObj() throws ObjectException, SystemObjectException {
            // create a new phone object
            com.sun.mdm.index.objects.PhoneObject phone = new com.sun.mdm.index.objects.PhoneObject();
            
            phone.setPhoneId("0002");
            phone.setPhoneType("home");
            phone.setPhone("6268105256");
            
            // create a new person object
            com.sun.mdm.index.objects.PersonObject person = new com.sun.mdm.index.objects.PersonObject();
            person.setPersonId("0001");
            person.setLastName("McMaster");
            //person.setLastName("Zheng");
            person.setMiddleName("middlename");
            person.setFirstName("Cathy");
            //person.setFirstName("Gary");
            person.setGender("Male");
            person.setSSN("932-43-3442");
            person.setTitle("Mr.");
            person.setRace("partyanimal");
            person.setDOB(new java.util.Date());
            
            // add 'phone' to person
            person.addSecondaryObject(phone);
            
            // create yet another phone
            phone = new com.sun.mdm.index.objects.PhoneObject();
            phone.setPhoneId("0003");
            phone.setPhoneType("Business");
            phone.setPhone("626-471-6000");
            
            // add 'phone to person
            person.addSecondaryObject(phone);
            
            // create a new address
            com.sun.mdm.index.objects.AddressObject address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0001");
            address.setAddressType("home");
            address.setAddressLine1("18803 Sherbourne Place");
            address.setAddressLine2("#b");
            address.setCity("Rowland Heights");
            address.setStateCode("California");
            address.setPostalCode("91748");
            
            // add address to person
            person.addSecondaryObject(address);
            
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0002");
            address.setAddressType("biz");
            address.setAddressLine1("404 e. huntington drive");
            address.setAddressLine2("  ");
            address.setCity("Monrovia");
            address.setStateCode("California");
            address.setPostalCode("91016");

            // add address to person
            person.addSecondaryObject(address);







            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0003");
            address.setAddressType("home2");
            address.setAddressLine1("10 S 8TH AVE NO 508");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0004");
            address.setAddressType("home3");
            address.setAddressLine1("PO BOX 708");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0005");
            address.setAddressType("home4");
            address.setAddressLine1("8 N 32ND AVE");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0006");
            address.setAddressType("home5");
            address.setAddressLine1("2613 JACKSON HWY");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0007");
            address.setAddressType("home6");
            address.setAddressLine1("20525 28TH AVE W #A");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);
            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0008");
            address.setAddressType("home7");
            address.setAddressLine1("1502 1ST TRAILER 3");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);

            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0009");
            address.setAddressType("home8");
            address.setAddressLine1("511 N HERALD RD");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);

            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("0010");
            address.setAddressType("home9");
            address.setAddressLine1("710 ST RT 821 #107");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);


            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("00011");
            address.setAddressType("home10");
            address.setAddressLine1("22550 ST RT 24");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);

            // create yet another address
            address = new com.sun.mdm.index.objects.AddressObject();
            address.setAddressId("00012");
            address.setAddressType("home11");
            address.setAddressLine1("787 ST HWY 508");
            address.setAddressLine2("  ");
            address.setCity("Warrensburg");
            address.setStateCode("Missouri");
            address.setPostalCode("93939");

            // add address to person
            person.addSecondaryObject(address);

            com.sun.mdm.index.objects.SystemObject sys1 = 
                    new com.sun.mdm.index.objects.SystemObject("QWS", "1111", "Person", 
                    "active", "Admin", "add", new java.util.Date(), 
                    "Admin", "add", new java.util.Date(),  person);            
            
            return sys1;
    }
}
