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
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;   

import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.MatchResult;
import java.util.Date;

/** Test class for generating non-null phonetic codes
 * @author Raymond Tam
 */
public class PhoneticCode extends TestCase {
    
    /** master controller instance
      *
      */            
    private static MasterController controller;
    
    /** system instance
      *
      */            
    private static String system;
    
    /** local id instance
      *
      */            
    private static String lid;
    
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public PhoneticCode(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests phonetic code generation
     * @throws Exception an error occured
     */
    public void testPhoneticCode() throws Exception {

        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSystem, defaultLid);
        assertTrue(rc);
    }

    /** Tests phonetic code generation for a record.  
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid) throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        System.out.println("\n---PhoneticCode---\n");
        
        //  set the class members
        if (system == null && tmpSystem != null)  {
            system = tmpSystem;
        }
        if (lid == null && tmpLid != null)  {
            lid = tmpLid;
        }
        
        //  Add the test record.  Normally, the AddRecordHelper class
        //  is used for this, but this test involves using unusual name
        //  in order to test phoneticization

        controller = MCFactory.getMasterController();
        
        System.out.println("Attempting to add a record");
        SystemObject sysObj = null;
        sysObj = buildUnitTestSystemObject();
        System.out.println("Attempting executeMatch");
        MatchResult mr = controller.executeMatch(sysObj);
        
        System.out.println("Attempting an LID lookup");
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        if (newSysObj == null)  {
            System.out.println("LID lookup failed: expected 1 record, but found 0");
            return false;
        }
        System.out.println("LID lookup succeeded!");
        PersonObject newPersonObj = (PersonObject) newSysObj.pGetChildren("Person").get(0);        
        if (newPersonObj == null)  {
            System.out.println("Test failed: no person object retrieved\n");
            return false;
        }

        String fnamePhonCode = newPersonObj.getFnamePhoneticCode();
        String lnamePhonCode = newPersonObj.getLnamePhoneticCode();
        
        if (fnamePhonCode != null && fnamePhonCode.length() > 0
            && lnamePhonCode != null && lnamePhonCode.length() > 0)  {
                    
            System.out.println("phonetic codes : " 
                               + fnamePhonCode + ", " 
                               + lnamePhonCode);
            System.out.println("Test succeeded!\n");
            return true;
        }
        
        if (fnamePhonCode == null || fnamePhonCode.length() == 0)  {
            System.out.println("Test failed: first name phonetic code not generated\n");
        }
        if (lnamePhonCode == null || lnamePhonCode.length() == 0)  {
            System.out.println("Test failed: first name phonetic code not generated\n");
        }
        return false;
    }
            
    /** Build a system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    
    private static SystemObject buildUnitTestSystemObject() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject");

        PersonObject person = new PersonObject();
        person.setLastName("Womack#$[2]&");
        person.setFirstName("Leann***1(1)");
        person.setSSN("554129874");
        person.setDOB(new java.util.Date());
        person.setGender("F");
        person.setMotherName("Patty");
        person.setMotherPhoneticCode("");
        person.setMotherMN("Loveless");
        person.setMaiden("Jones");
        person.setSpouseName("John");
        person.setFatherName("George");
        person.setMiddleName("Lynn");

        SystemObject sysObj = new SystemObject("SBYN", 
                                               "4568915615", 
                                               "Person", 
                                               "active", 
                                               "Admin", 
                                               "add", 
                                               new Date(),
                                               "Admin", 
                                               "add", 
                                               new Date(), 
                                               person);
                                               
        return sysObj;
    }    
    
    /** Prints out a message
     *
     */    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(PhoneticCode.class));
    }
    
}
