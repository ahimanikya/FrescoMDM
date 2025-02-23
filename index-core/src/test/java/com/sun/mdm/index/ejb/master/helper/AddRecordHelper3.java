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
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.AddressObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.MatchResult;
import java.util.Date;

/**  Adds records for testing cyclic merges.
 *
 * @author  rtam
 * @version 
 */

public class AddRecordHelper3 extends BasicHelper {
    
    // master controller instance
    private static MasterController controller;
    
    // system instance
    private static String system;
    
    // first local id instance
    private static String lid0;
    
    // second local id instance
    private static String lid1;
    
    // third local id instance
    private static String lid2;
     
    // fourth local id instance
    private static String lid3;
     
    // default system instance
    private static final String DEFAULT_SYSTEM = new String("SBYN");
    
    // default local ID 0
    private static final String DEFAULT_LID0 = new String("0000000000");

    // default local ID 1
    private static final String DEFAULT_LID1 = new String("1111111111");
    
    // default local ID 2
    private static final String DEFAULT_LID2 = new String("2222222222");
    
    // default local ID 3
    private static final String DEFAULT_LID3 = new String("3333333333");
    
    // EUID of the first object
    private static String euid0;
    
    // EUID of the second object
    private static String euid1;
    
    /** Creates a new AddRecordHelper2 instance
     *
     */
    public AddRecordHelper3() {
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
        try {
            // runs the second test by default
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

        //  Add the first test record in an EO
        System.out.println("\nInserting test records");
        SystemObject sysObj = null;
        sysObj = buildUnitTestSystemObject0();
        MatchResult mr = controller.executeMatch(sysObj);
        euid0 = mr.getEUID();
        EnterpriseObject eo1 = controller.getEnterpriseObject(euid0);
        
        // Invoke a one second delay to ensure that the second 
        // SO has a later timestamp than the first SO.
        
        delay(1);
        
        //  Add the second test record to the same EO
        sysObj = buildUnitTestSystemObject1();
        eo1.addSystemObject(sysObj);        
        controller.updateEnterpriseObject(eo1);

        // Invoke a one second delay to ensure that the third
        // SO has a later timestamp than the second SO.
        
        delay(1);
        
        //  Add the third test record in a seperate EO
        sysObj = buildUnitTestSystemObject2();
        mr = controller.executeMatch(sysObj);
        euid1 = mr.getEUID();
        EnterpriseObject eo2 = controller.getEnterpriseObject(euid1);
        
        delay(1);
        
        //  Add the fourth test record to the same EO
        sysObj = buildUnitTestSystemObject3();
        eo2.addSystemObject(sysObj);        
        controller.updateEnterpriseObject(eo2);

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
    
    /* Return euid0
     *
     * @return euid0
     */
    public static String getEuid0() {
        return euid0;
    }
    
    /* Return euid1
     *
     * @return euid1
     */
    public static String getEuid1() {
        return euid1;
    }
    
    /* Return system
     *
     * @return system
     */
    public static String getSystem() {
        return system;
    }
    
    /* Return lid0
     *
     * @return lid0
     */
    public static String getLid0() {
        return lid0;
    }
    
    /* Return lid1
     *
     * @return lid1
     */
    public static String getLid1() {
        return lid1;
    }
    
    /* Return lid2
     *
     * @return lid2
     */
    public static String getLid2() {
        return lid2;
    }
    
    /* Return lid3
     *
     * @return lid3
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
    
    /** Runs a delay.
     *
     */
    private static void delay(int seconds) 
    {
        if (seconds == 0) {
            return;
        }
        long startTime = System.currentTimeMillis();
        long curTime = startTime;
        long delayInterval = seconds * 1000;
        while (true) {
            for (int i = 0; i < 100000; i++ ) {
            }
            curTime = System.currentTimeMillis();
            if (curTime - startTime >= delayInterval) {
                return;
            }
        }
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
        
        SystemObject sysObj = new SystemObject(system, 
                                               lid0, 
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
    private static SystemObject buildUnitTestSystemObject1() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject1");

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
        
        SystemObject sysObj = new SystemObject(system, 
                                               lid1, 
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

    /** Build a third system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject2() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject2");

        PersonObject person = new PersonObject();
        person.setLastName("Alba");
        person.setFirstName("Anna");
        person.setMiddleName("Caren");
        person.setSSN("444229774");
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

        
        SystemObject sysObj = new SystemObject(system, 
                                               lid2, 
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
        person.setLastName("Hunter");
        person.setFirstName("Lisa");
        person.setMiddleName("Minmei");
        person.setSSN("113357799");
        person.setDOB(new java.util.Date());
        person.setGender("F");
        person.setMotherName("Trish");
        person.setMotherMN("Hernandez");
        person.setMaiden("Hayes");
        person.setSpouseName("Rick");
        person.setFatherName("Bob");
        
        SystemObject sysObj = new SystemObject(system, 
                                               lid3, 
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
}
