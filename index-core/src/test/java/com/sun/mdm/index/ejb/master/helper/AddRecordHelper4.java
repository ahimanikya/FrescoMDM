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
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.MatchResult;
import java.util.Date;

/**  Adds records for deferred pessimistic mode system object merge.
 *
 * @author  rtam
 * @version 
 */

public class AddRecordHelper4 extends BasicHelper {
    
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
    /** third local id instance
     *
     */            
    private static String lid2;
    
    /** fourth local id instance
     *
     */            
    private static String lid3;
    
    
    /** default system instance
     *
     */
    private static final String DEFAULT_SYSTEM = new String("SBYN");
    /** default local ID 0
     *
     */
    private static final String DEFAULT_LID0 = new String("0000000000");
    /** default local ID 1
     *
     */
    private static final String DEFAULT_LID1 = new String("1111111111");
    /** default local ID 2
     *
     */
    private static final String DEFAULT_LID2 = new String("2222222222");
    /** default local ID 3
     *
     */
    private static final String DEFAULT_LID3 = new String("3333333333");
    
    /** Creates a new LookupLidHelper instance
     *
     */
    public AddRecordHelper4() {
    }
    
    /** main program for command line testing
     * @param args args[0] = system, args[1] = local id
     *
     */    
    public static void main(String[] args) {
        String tmpSystem = null;
        String tmpLid0 = null;
        String tmpLid1 = null;
        String tmpLid2 = null;
        String tmpLid3 = null;
        
        if (args.length == 5) {
            tmpSystem = new String(args[0]);
            tmpLid0 = new String(args[1]);
            tmpLid1 = new String(args[2]);
            tmpLid2 = new String(args[3]);
            tmpLid3 = new String(args[4]);
        }  else  {
            tmpSystem = new String(DEFAULT_SYSTEM);
            tmpLid0 = new String(DEFAULT_LID0);
            tmpLid1 = new String(DEFAULT_LID1);
            tmpLid2 = new String(DEFAULT_LID2);
            tmpLid3 = new String(DEFAULT_LID3);
        }
        System.out.println("system--> " + tmpSystem);
        System.out.println("lid0--> " + tmpLid0);
        System.out.println("lid1--> " + tmpLid1);
        System.out.println("lid2--> " + tmpLid2);
        System.out.println("lid3--> " + tmpLid3);
        try {
            run(tmpSystem, tmpLid0, tmpLid1, tmpLid2, tmpLid3);
        }  catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }        

    /** Adds a record
     *
     * @param tmpSystem  system 
     * @param tmpLid0  first local id 
     * @param tmpLid1  second local id 
     * @param tmpLid2  third local id 
     * @param tmpLid3  fourth local id 
     * @return  true if successful, false otherwise
     * @throws  Exception if an error occurs
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid0,
                              String tmpLid1,
                              String tmpLid2,
                              String tmpLid3) throws Exception {
        
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
        if (lid2 == null)  {
            if (tmpLid2 != null)  {
                lid2 = tmpLid2;
            }  else  {
                lid2 = DEFAULT_LID2;
            }
        }
        if (lid3 == null)  {
            if (tmpLid3 != null)  {
                lid3 = tmpLid3;
            }  else  {
                lid3 = DEFAULT_LID3;
            }
        }
        controller = MCFactory.getMasterController();

        //  add the first test record 
        System.out.println("\nInserting test records");
        SystemObject sysObj = null;
        sysObj = buildUnitTestSystemObject0();
        MatchResult mr = controller.executeMatch(sysObj);
        
        // add a second system object to it
        sysObj = buildUnitTestSystemObject1();
        String euid = mr.getEUID();
        controller.addSystemObject(euid, sysObj);
                       
        //  add the third test record
        sysObj = buildUnitTestSystemObject2();
        mr = controller.executeMatch(sysObj);
        
        //  add the third test record
        sysObj = buildUnitTestSystemObject3();
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
        newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid2));
        if (newSysObj == null)  {
            System.out.println("Test failed for LID2 " + lid2 + " : expected 1 record, but found 0.\n");
            return false;
        }
        newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid3));
        if (newSysObj == null)  {
            System.out.println("Test failed for LID3 " + lid3 + " : expected 1 record, but found 0.\n");
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
    private static SystemObject buildUnitTestSystemObject0() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject0");

        PersonObject person = new PersonObject();
        person.setLastName("Yokota");
        person.setFirstName("Kariann");
        person.setMiddleName("Janet");
        person.setSSN("333772222");
        person.setDOB(new java.util.Date());
        person.setGender("F");
        person.setMotherName("Megumi");
        person.setMotherMN("Hosaka");
        person.setMaiden("Yokota");
        person.setSpouseName("George");
        person.setFatherName("Geoff");

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
    
    /** Build a system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject1() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject1");

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
    
    /** Build a third system object for testing.  This is identical
     * to the first system object.
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject2() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject2");

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
        
        
        SystemObject sysObj = new SystemObject(DEFAULT_SYSTEM, 
                                               DEFAULT_LID2, 
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
    
    /** Build a fourth system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject3() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject3");

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
                                               DEFAULT_LID3, 
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

    /** return the system
     *
     */    
    public static String getSystem() {
        return system;
    }
    /** return first local id instance
     *
     */    
    public static String getLid0() {
        return lid0;
    }
    /** return second local id instance
     *
     */    
    public static String getLid1() {
        return lid1;
    }
    /** return third local id instance
     *
     */    
    public static String getLid2() {
        return lid2;
    }
    /** return fourth local id instance
     *
     */    
    public static String getLid3() {
        return lid3;
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
