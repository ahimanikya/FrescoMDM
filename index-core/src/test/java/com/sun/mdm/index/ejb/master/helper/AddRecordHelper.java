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
package com.sun.mdm.index.ejb.master.helper;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.AddressObject;   
import com.sun.mdm.index.objects.AliasObject;   
import com.sun.mdm.index.objects.PhoneObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.MatchResult;
import java.util.Date;

/**  Adds a record
 *
 * @author  rtam
 * @version 
 */

public class AddRecordHelper extends BasicHelper {
    
    /** master controller instance
      *
      */            
    private static MasterController controller;
    
    /** system instance
      *
      */            
    private static String system;
    
    /** first local id instance
      *
      */            
    private static String lid0;
    
    /** second local id instance
      *
      */            
    private static String lid1;
    /** default system instance
     *
     */
    private static final String DEFAULT_SYSTEM = new String("SBYN");
    /** default local ID 0
     *
     */
    private static final String DEFAULT_LID0 = new String("4568915615");
    /** default local ID 1
     *
     */
    private static final String DEFAULT_LID1 = new String("6541985165");
    
    /** Creates a new LookupLidHelper instance
     *
     */
    public AddRecordHelper() {
    }
    
    /** main program for command line testing
     * @param args args[0] = system, args[1] = local id
     *
     */    
    public static void main(String[] args) {
        String tmpSystem = null;
        String tmpLid0 = null;
        String tmpLid1 = null;
        
        if (args.length == 3) {
            tmpSystem = new String(args[0]);
            tmpLid0 = new String(args[1]);
            tmpLid1 = new String(args[2]);
        }  else  {
            tmpSystem = new String(DEFAULT_SYSTEM);
            tmpLid0 = new String(DEFAULT_LID0);
            tmpLid1 = new String(DEFAULT_LID1);
        }
        System.out.println("system--> " + tmpSystem);
        System.out.println("lid0--> " + tmpLid0);
        System.out.println("lid1--> " + tmpLid1);
        try {
            run(tmpSystem, tmpLid0, tmpLid1);
        }  catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }        

    /** Adds a record
     *
     * @param tmpSystem  system to search 
     * @param tmpLid0  first local id to search
     * @param tmpLid1  second local id to search
     * @return  true if successful, false otherwise
     * @throws  Exception if an error occurs
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid0,
                              String tmpLid1) throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        //  set the class members
        if (system == null)  {
            if (tmpSystem != null)  {
                system = tmpSystem;
            }  else  {
                system = DEFAULT_SYSTEM;
            }
        }
        if (lid0 == null)  {
            if (tmpLid0 != null)  {
                lid0 = tmpLid0;
            }  else  {
                lid0 = DEFAULT_LID0;
            }
        }
        if (lid1 == null)  {
            if (tmpLid1 != null)  {
                lid1 = tmpLid1;
            }  else  {
                lid1 = DEFAULT_LID1;
            }
        }
        controller = MCFactory.getMasterController();

        //  add the first test record 
        System.out.println("\nInserting test records");
        SystemObject sysObj = null;
        sysObj = buildUnitTestSystemObject();
        MatchResult mr = controller.executeMatch(sysObj);
        
        //  add the second test record
        sysObj = buildUnitTestSystemObject2();
        mr = controller.executeMatch(sysObj);
        
        //  verify the record insertions 
        System.out.println("Verifying record insertions");        
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid0));
        if (newSysObj == null)  {
            System.out.println("Test failed for LID0 " + lid0 + " : expected 1 record, but found 0.\n");
            return false;
        }
        newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid1));
        if (newSysObj == null)  {
            System.out.println("Test failed for LID1 " + lid1 + " : expected 1 record, but found 0.\n");
            return false;
        }
        System.out.println("Records added successfully!\n");
        return true;
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
        person.setLastName("Womack");
        person.setFirstName("Deb");
        person.setMiddleName("Caren");
        person.setSSN("554129874");
        person.setDOB(new java.util.Date());
        person.setGender("F");
        person.setMotherName("Lottie");
        person.setMotherMN("Loveless");
        person.setMaiden("Jones");
        person.setSpouseName("Bob");
        person.setFatherName("Bill");
                        
        //  add address information
            
        AddressObject address0 = new AddressObject();
        address0.setAddressType("H");
        address0.setAddressLine1("800 Royal Oaks Dr.");
        address0.setAddressLine2("");
        address0.setAddressLine3("");
        address0.setAddressLine4("");
        address0.setCity("MONROVIA");
        address0.setStateCode("CA");
        address0.setCity("MONROVIA");
        address0.setPostalCode("91016");
        address0.setPostalCodeExt("1234");
        address0.setCounty("Los Angeles");
        address0.setCountryCode(null);
        person.addAddress(address0);        

        AddressObject address1 = new AddressObject();
        address1.setAddressType("W");
        address1.setAddressLine1("404 W. Huntington Ave.");
        address1.setAddressLine2("");
        address1.setAddressLine3("");
        address1.setAddressLine4("");
        address1.setCity("MONROVIA");
        address1.setStateCode("CA");
        address1.setCity("MONROVIA");
        address1.setPostalCode("91016");
        address1.setPostalCodeExt("1234");
        address1.setCounty("Los Angeles");
        address1.setCountryCode(null);
        person.addAddress(address1);        
        
        //  add alias information
        
        AliasObject alias0 = new AliasObject(); 
        alias0.setFirstName("Deb");
        //  middle name must be set to empty string for eViewME to work properly
        alias0.setMiddleName("");
        alias0.setLastName("Jackson");
        person.addAlias(alias0);        
        
        AliasObject alias1 = new AliasObject(); 
        alias1.setFirstName("Dotty");
        alias1.setLastName("Jones");
        alias1.setMiddleName("Judy");
        person.addAlias(alias1);        
        
        //  add phone information
            
        PhoneObject phone0 = new PhoneObject();
        phone0.setPhoneType("CH");
        phone0.setPhone("6268675309");
        phone0.setPhoneExt("1234");
        person.addPhone(phone0);        
        
        SystemObject sysObj = new SystemObject(DEFAULT_SYSTEM, 
                                               DEFAULT_LID0, 
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
    /** Build a second system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject2() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject");

        PersonObject person = new PersonObject();
        person.setLastName("Brown");
        person.setFirstName("Caren");
        person.setMiddleName("Lottie");
        person.setSSN("123456789");
        person.setDOB(new java.util.Date());
        person.setGender("F");
        person.setMotherName("Deb");
        person.setMotherMN("Johnson");
        person.setMaiden("Smith");
        person.setSpouseName("Bill");
        person.setFatherName("Bob");
        
        SystemObject sysObj = new SystemObject(DEFAULT_SYSTEM, 
                                               DEFAULT_LID1, 
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
    /** Cleans up system resources
     *
     */    
    public static void cleanup()  {
        System.gc();
        System.runFinalization();
        System.out.println("\nAll done. Bye Bye.\n\n");
    }
    
}
