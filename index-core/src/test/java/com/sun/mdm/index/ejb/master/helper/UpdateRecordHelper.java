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
import com.sun.mdm.index.objects.PhoneObject;   
import com.sun.mdm.index.objects.AliasObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.MatchResult;
import java.util.Date;

/**  Updates a record added by the AddRecordHelperClass
 *
 * @author  rtam
 * @version 
 */

public class UpdateRecordHelper extends BasicHelper {
    
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
    
    /** Creates a new LookupLidHelper instance
     *
     */
    public UpdateRecordHelper() {
    }
    
    /** main program for command line testing
     * @param args args[0] = system, args[1] = local id
     *
     */    
    public static void main(String[] args) {
        String defaultSystem = null;
        String defaultLid = null;
        
        if (args.length == 2) {
            defaultSystem = new String(args[0]);
            defaultLid = new String(args[1]);
        }  else  {
            defaultSystem = new String(DEFAULT_SYSTEM);
            defaultLid = new String(DEFAULT_LID0);
        }
        System.out.println("system--> " + defaultSystem);
        System.out.println("lid--> " + defaultLid);
        try {
            run(defaultSystem, defaultLid);
        }  catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }        

    /** Updates a record
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @return  true if successful, false otherwise
     * @throws  Exception if an error occurs
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid) throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        //  set the class members
        if (system == null && tmpSystem != null)  {
            system = tmpSystem;
        }
        if (lid == null && tmpLid != null)  {
            lid = tmpLid;
        }
        controller = MCFactory.getMasterController();

        //  add the test record 
        System.out.println("\nAttempting to update a record");
        SystemObject sysObj = null;
        sysObj = buildUnitTestSystemObject();
        System.out.println("Attempting executeMatch");
        MatchResult mr = controller.executeMatch(sysObj);
        
        //  verify the record addition
        System.out.println("Verifying record update");
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        if (newSysObj == null)  {
            System.out.println("Test failed: expected 1 record, but found 0.\n");
            return false;
        }
        System.out.println("Record updated successfully!\n");
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
        person.setLastName("Jones");
        person.setFirstName("Deb");
        person.setSSN("554129873");
        person.setDOB(new java.util.Date());
        person.setGender("M");
        person.setPersonCatCode("P");
        person.setSuffix("JR");
        person.setTitle("MD");
        person.setDeath("N");
        person.setMStatus("M");
        person.setRace("W");
        person.setEthnic("41");
        person.setReligion("CH");
        person.setLanguage("ENGL");
        person.setPobCity("San Francisco");
        person.setPobState("CA");
        person.setPobCountry("USA");
        person.setVIPFlag("N");
        person.setVetStatus("Y");
        person.setDriversLicense("A1234567");
        person.setDriversLicenseSt("CA");
        person.setDod(new java.util.Date());   
        person.setDeathCertificate("D1234567");
        person.setNationality("USA");
        person.setCitizenship("USA");
        person.setPensionNo("P1234567");
        person.setPensionExpDate(new java.util.Date());
        person.setDistrictOfResidence("Sunset");
        person.setLgaCode("ALGA");
        person.setMilitaryBranch("USMC");
        person.setMilitaryRank("MAJ");
        person.setMilitaryStatus("I");
        person.setDummyDate(new java.util.Date());
        person.setClass1("class1");
        person.setClass2("class2");
        person.setClass3("class3");
        person.setClass4("class4");
        person.setClass5("class5");
        person.setString1("string1");
        person.setString2("string2");
        person.setString3("string3");
        person.setString4("string4");
        person.setString5("string5");
        person.setString6("string6");
        person.setString7("string7");
        person.setString8("string8");
        person.setString9("string9");
        person.setString10("string10");
        person.setDate1(new java.util.Date());
        person.setDate2(new java.util.Date());
        person.setDate3(new java.util.Date());
        person.setDate4(new java.util.Date());
        person.setDate5(new java.util.Date());

        //  add phone information
            
        PhoneObject phone0 = new PhoneObject();
        phone0.setPhoneType("CB");
        phone0.setPhone("6265551212");
        phone0.setPhoneExt("4444");
        person.addPhone(phone0);        
        
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
    /** Cleans up system resources
     *
     */    
    public static void cleanup()  {
        System.gc();
        System.runFinalization();
        System.out.println("\nAll done. Bye Bye.\n\n");
    }
    
}
