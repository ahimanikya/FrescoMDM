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
import com.sun.mdm.index.ejb.master.helper.AddRecordHelper;
import com.sun.mdm.index.ejb.master.helper.UpdateRecordHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.AliasObject;   
import com.sun.mdm.index.objects.PhoneObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.SBR;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import com.sun.mdm.index.objects.metadata.ObjectFactory;

/** Test class for testing updates
 * @author Raymond Tam
 */
public class UpdateRecord extends TestCase {
    
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
    
    // database type
    private static String mDbType = null;
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdateRecord(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        mDbType = new String(ObjectFactory.getDatabase());
        if (mDbType.equalsIgnoreCase("Oracle") &&
            mDbType.equalsIgnoreCase("SQL Server")) {
            throw new Exception("Error. Unsupported database: " + mDbType);
        }
    }
    
    /** Tests updating 
     * @throws Exception an error occured
     */
    public void testUpdateRecord() throws Exception {
        
        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSystem, defaultLid);
        assertTrue(rc);
    }

    /** Tests updating first names and their standardizations
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid) throws Exception {
        
        System.out.println("\n---UpdateRecord---\n");
        
        //  set the class members
        if (system == null && tmpSystem != null)  {
            system = tmpSystem;
        }
        if (lid == null && tmpLid != null)  {
            lid = tmpLid;
        }

        //  add a test record
        AddRecordHelper addRecordHelper = new AddRecordHelper();
        addRecordHelper.clearDb();
        boolean rc = addRecordHelper.run(system, lid, null);
        if (!rc)  {
            System.out.println("Test failed: addRecordHelper.run() returned false");
            return false;
        }

        //  update the test record
        UpdateRecordHelper updateRecordHelper = new UpdateRecordHelper();
        rc = updateRecordHelper.run(system, lid);
        if (!rc)  {
            System.out.println("Test failed: updateRecordHelper.run() returned false");
            return false;
        }
        
        controller = MCFactory.getMasterController();
        System.out.println("Attempting an LID lookup");
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        if (newSysObj == null)  {
            System.out.println("LID lookup failed: expected 1 record, but found 0");
            return false;
        }
        System.out.println("LID lookup succeeded!");
        
        //
        //  test System Object
        //
        
        PersonObject newPersonObj = (PersonObject) newSysObj.pGetChildren("Person").get(0);
        if (newPersonObj == null)  {
            System.out.println("Test failed: no person object retrieved");
            return false;
        }
        
        // test system object--person
        
        String val = newPersonObj.getLastName();
        if (checkValue(val, "LastName", "Jones") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getFirstName();
        if (checkValue(val, "FirstName", "Deb") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        // should be null
        val = newPersonObj.getMiddleName();
        if (checkNullValue(val, "MiddleName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getSSN();
        if (checkValue(val, "SSN", "554129873") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        Date dateVal = newPersonObj.getDOB();
        if (checkNonNullValue(val, "DOB") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getGender();
        if (checkValue(val, "Gender", "M") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMotherName();
        if (checkNullValue(val, "MotherName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObj.getMotherMN();
        if (checkNullValue(val, "MotherMN") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMaiden();
        if (checkNullValue(val, "Maiden") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObj.getSpouseName();
        if (checkNullValue(val, "SpouseName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObj.getFatherName();
        if (checkNullValue(val, "FatherName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getPersonCatCode();
        if (checkValue(val, "PersonCatCode", "P") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getSuffix();
        if (checkValue(val, "Suffix", "JR") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getTitle();
        if (checkValue(val, "Title", "MD") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getDeath();
        if (checkValue(val, "Death", "N") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMStatus();
        if (checkValue(val, "MStatus", "M") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getRace();
        if (checkValue(val, "Race", "w") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getEthnic();
        if (checkValue(val, "Ethnic", "41") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getLanguage();
        if (checkValue(val, "Language", "ENGL") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getPobCity();
        if (checkValue(val, "PobCity", "San Francisco") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getPobState();
        if (checkValue(val, "PobState", "CA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getPobCountry();
        if (checkValue(val, "PobCountry", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getVIPFlag();
        if (checkValue(val, "VIPFlag", "N") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getVetStatus();
        if (checkValue(val, "VetStatus", "Y") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getDriversLicense();
        if (checkValue(val, "DriversLicense", "A1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getDriversLicenseSt();
        if (checkValue(val, "DriversLicenseSt", "CA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDod();
        if (checkNonNullValue(val, "Dod") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getDeathCertificate();
        if (checkValue(val, "DeathCertificate", "D1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getNationality();
        if (checkValue(val, "Nationality", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getCitizenship();
        if (checkValue(val, "Citizenship", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getPensionNo();
        if (checkValue(val, "PensionNo", "P1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getPensionExpDate();
        if (checkNonNullValue(val, "PensionExpDate") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getDistrictOfResidence();
        if (checkValue(val, "DistrictOfResidence", "Sunset") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getLgaCode();
        if (checkValue(val, "LgaCode", "ALGA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMilitaryRank();
        if (checkValue(val, "MilitaryRank", "MAJ") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMilitaryBranch();
        if (checkValue(val, "MilitaryBranch", "USMC") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getMilitaryStatus();
        if (checkValue(val, "MilitaryStatus", "I") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDummyDate();
        if (checkNonNullValue(val, "DummyDate") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getClass1();
        if (checkValue(val, "Class1", "class1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getClass2();
        if (checkValue(val, "Class2", "class2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getClass3();
        if (checkValue(val, "Class3", "class3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getClass4();
        if (checkValue(val, "Class4", "class4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getClass5();
        if (checkValue(val, "Class5", "class5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString1();
        if (checkValue(val, "String1", "string1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString2();
        if (checkValue(val, "String2", "string2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString3();
        if (checkValue(val, "String3", "string3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString4();
        if (checkValue(val, "String4", "string4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString5();
        if (checkValue(val, "String5", "string5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString6();
        if (checkValue(val, "String6", "string6") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString7();
        if (checkValue(val, "String7", "string7") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString8();
        if (checkValue(val, "String8", "string8") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString9();
        if (checkValue(val, "String9", "string9") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObj.getString10();
        if (checkValue(val, "String10", "string10") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDate1();
        if (checkNonNullValue(val, "Date1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDate2();
        if (checkNonNullValue(val, "Date2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDate3();
        if (checkNonNullValue(val, "Date3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDate4();
        if (checkNonNullValue(val, "Date4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObj.getDate5();
        if (checkNonNullValue(val, "Date5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        
        //  test system object--alias
        
        Collection aliases = newPersonObj.getAlias();
        Iterator aliasesIter = aliases.iterator();
        AliasObject aliasObj = null;
        
        //  there should be three aliases 
        if (aliases.size() != 3)  {
            System.out.println("Error: there should be 3 aliases but " + aliases.size()
                    + " were found");
            System.out.println("Test Failed!\n");
            while (aliasesIter.hasNext()) {
                System.out.println(aliasesIter.next());
            }
            return false;
        }
        
        //  Check first alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DEBORAH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "JACKSON") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkNullValue(val, "StdMiddleName") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Deb") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Jackson") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkNullorEmptyValue(val, "MiddleName") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }

        //  Check second alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DOROTHY") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "JONES") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkValue(val, "StdMiddleName", "JUDITH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Dotty") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Jones") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkValue(val, "MiddleName", "Judy") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }
        
        //  Check third alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DEBORAH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "WOMACK") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkValue(val, "StdMiddleName", "KAREN") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Deb") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Womack") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkValue(val, "MiddleName", "Caren") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }
        
        //  test system object--phone
        
        Collection phones = newPersonObj.getPhone();
        Iterator phonesIter = phones.iterator();
        PhoneObject phoneObj = null;
        
        //  there should be one phone 
        if (phones.size() != 1)  {
            System.out.println("Error: there should be 1 phone but " + phones.size()
                    + " were found");
            System.out.println("Test Failed!\n");
            return false;
        }
        
        //  Check phone values
        phoneObj = (PhoneObject) phonesIter.next();
        if (phoneObj != null)  {            
            val = phoneObj.getPhoneType();
            if (checkValue(val, "PhoneType", "CB") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = phoneObj.getPhone();
            if (checkValue(val, "Phone", "6265551212") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = phoneObj.getPhoneExt();
            if (checkValue(val, "PhoneExt", "4444") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }

        //
        //  test SBR
        //
        
        String euid = controller.getEUID(new SystemObjectPK(system, lid));
        SBR newSBR = controller.getSBR(euid);
        PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
        
        val = newPersonObjSBR.getLastName();
        if (checkValue(val, "LastName", "Jones") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getFirstName();
        if (checkValue(val, "FirstName", "Deb") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMiddleName();
        if (checkNullValue(val, "MiddleName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getSSN();
        if (checkValue(val, "SSN", "554129873") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDOB();
        if (checkNonNullValue(val, "DOB") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getGender();
        if (checkValue(val, "Gender", "M") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMotherName();
        if (checkNullValue(val, "MotherName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObjSBR.getMotherMN();
        if (checkNullValue(val, "MotherMN") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMaiden();
        if (checkNullValue(val, "Maiden") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObjSBR.getSpouseName();
        if (checkNullValue(val, "SpouseName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }

        val = newPersonObjSBR.getFatherName();
        if (checkNullValue(val, "FatherName") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getPersonCatCode();
        if (checkValue(val, "PersonCatCode", "P") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getSuffix();
        if (checkValue(val, "Suffix", "JR") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getTitle();
        if (checkValue(val, "Title", "MD") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getDeath();
        if (checkValue(val, "Death", "N") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMStatus();
        if (checkValue(val, "MStatus", "M") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getRace();
        if (checkValue(val, "Race", "w") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getEthnic();
        if (checkValue(val, "Ethnic", "41") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getLanguage();
        if (checkValue(val, "Language", "ENGL") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getPobCity();
        if (checkValue(val, "PobCity", "San Francisco") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getPobState();
        if (checkValue(val, "PobState", "CA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getPobCountry();
        if (checkValue(val, "PobCountry", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getVIPFlag();
        if (checkValue(val, "VIPFlag", "N") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getVetStatus();
        if (checkValue(val, "VetStatus", "Y") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getDriversLicense();
        if (checkValue(val, "DriversLicense", "A1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getDriversLicenseSt();
        if (checkValue(val, "DriversLicenseSt", "CA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDod();
        if (checkNonNullValue(val, "Dod") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getDeathCertificate();
        if (checkValue(val, "DeathCertificate", "D1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getNationality();
        if (checkValue(val, "Nationality", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getCitizenship();
        if (checkValue(val, "Citizenship", "USA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getPensionNo();
        if (checkValue(val, "PensionNo", "P1234567") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getPensionExpDate();
        if (checkNonNullValue(val, "PensionExpDate") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getDistrictOfResidence();
        if (checkValue(val, "DistrictOfResidence", "Sunset") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getLgaCode();
        if (checkValue(val, "LgaCode", "ALGA") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMilitaryRank();
        if (checkValue(val, "MilitaryRank", "MAJ") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMilitaryBranch();
        if (checkValue(val, "MilitaryBranch", "USMC") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getMilitaryStatus();
        if (checkValue(val, "MilitaryStatus", "I") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDummyDate();
        if (checkNonNullValue(val, "DummyDate") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getClass1();
        if (checkValue(val, "Class1", "class1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getClass2();
        if (checkValue(val, "Class2", "class2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getClass3();
        if (checkValue(val, "Class3", "class3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getClass4();
        if (checkValue(val, "Class4", "class4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getClass5();
        if (checkValue(val, "Class5", "class5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString1();
        if (checkValue(val, "String1", "string1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString2();
        if (checkValue(val, "String2", "string2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString3();
        if (checkValue(val, "String3", "string3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString4();
        if (checkValue(val, "String4", "string4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString5();
        if (checkValue(val, "String5", "string5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString6();
        if (checkValue(val, "String6", "string6") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString7();
        if (checkValue(val, "String7", "string7") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString8();
        if (checkValue(val, "String8", "string8") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString9();
        if (checkValue(val, "String9", "string9") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        val = newPersonObjSBR.getString10();
        if (checkValue(val, "String10", "string10") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDate1();
        if (checkNonNullValue(val, "Date1") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDate2();
        if (checkNonNullValue(val, "Date2") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDate3();
        if (checkNonNullValue(val, "Date3") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDate4();
        if (checkNonNullValue(val, "Date4") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        dateVal = newPersonObjSBR.getDate5();
        if (checkNonNullValue(val, "Date5") == false)  {
            System.out.println("Test Failed!\n");
            return false;
        }
        
        
        //  test system object--alias
        
        aliases = newPersonObjSBR.getAlias();
        aliasesIter = aliases.iterator();
        
        //  there should be three aliases 
        if (aliases.size() != 3)  {
            System.out.println("Error: there should be 3 aliases but " + aliases.size()
                    + " were found");
            System.out.println("Test Failed!\n");
            while (aliasesIter.hasNext()) {
                System.out.println(aliasesIter.next());
            }
            return false;
        }
        
        //  Check first alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DEBORAH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "JACKSON") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkNullValue(val, "StdMiddleName") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Deb") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Jackson") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkNullorEmptyValue(val, "MiddleName") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }

        //  Check second alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DOROTHY") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "JONES") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkValue(val, "StdMiddleName", "JUDITH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Dotty") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Jones") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkValue(val, "MiddleName", "Judy") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }
        
        //  Check third alias values
        aliasObj = (AliasObject) aliasesIter.next();
        if (aliasObj != null)  {            
            val = aliasObj.getStdFirstName();
            if (checkValue(val, "StdFirstName", "DEBORAH") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdLastName();
            if (checkValue(val, "StdLastName", "WOMACK") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getStdMiddleName();
            if (checkValue(val, "StdMiddleName", "KAREN") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
            
            val = aliasObj.getFirstName();
            if (checkValue(val, "FirstName", "Deb") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getLastName();
            if (checkValue(val, "LastName", "Womack") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = aliasObj.getMiddleName();
            if (checkValue(val, "MiddleName", "Caren") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }
        
        //  test system object--phone
        
        phones = newPersonObjSBR.getPhone();
        phonesIter = phones.iterator();
        
        //  there should be one aliases 
        if (phones.size() != 1)  {
            System.out.println("Error: there should be 1 phone but " + phones.size()
                    + " were found");
            System.out.println("Test Failed!\n");
            return false;
        }
        
        //  Check phone values
        phoneObj = (PhoneObject) phonesIter.next();
        if (phoneObj != null)  {            
            val = phoneObj.getPhoneType();
            if (checkValue(val, "PhoneType", "CB") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = phoneObj.getPhone();
            if (checkValue(val, "Phone", "6265551212") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }

            val = phoneObj.getPhoneExt();
            if (checkValue(val, "PhoneExt", "4444") == false)  {
                System.out.println("Test Failed!\n");
                return false;
            }
        }
        System.out.println("Test succeeded!\n");
        return true;
    }
    
    /**  Checks the if the value of a non-null field matches the expected value.
     *
     * @param value  value to check
     * @param fieldName  field name to check
     * @param expectedValue  expected value 
     * @returns true if match, false otherwise
     */
    public static boolean checkValue(String value, String fieldName, String expectedValue)  {
        if (value == null)  {
            System.out.println("System Object expected " + fieldName 
                                + " to be non-null but retrieved a null value");
            return false;
        }            
        if (value != null && value.compareToIgnoreCase(expectedValue) != 0)
        {
            System.out.println("System Object expected " + fieldName + " to be " 
                                + expectedValue + " but retrieved: " + value);
            return false;
        }        
        return true;
    }
        
    /**  Checks the if the value of a null field is null.
     *
     * @param value  value to check
     * @param fieldName  field name to check
     * @returns true if value is null, false otherwise
     */
    public static boolean checkNullValue(String value, String fieldName)  {
        if (value != null) {
            System.out.println("System Object expected " + fieldName + " to be " 
                                + " null but retrieved: '" + value + "'");
            return false;
        }        
        return true;
    }
        
    /**  Checks the if the value of a null field is null for Oracle
     * or an empty string for SQL Server.
     *
     * @param value  value to check
     * @param fieldName  field name to check
     * @returns true if value is null, false otherwise
     */
    public static boolean checkNullorEmptyValue(String value, String fieldName)  {
        if (mDbType.compareToIgnoreCase("Oracle") == 0) {
            if (value != null) {
                System.out.println("System Object expected " + fieldName + " to be " 
                                    + "null but retrieved: '" + value + "'");
                return false;
            }        
        } else if (mDbType.compareToIgnoreCase("SQL Server") == 0) {
            if (!value.equals("")) {
                System.out.println("System Object expected " + fieldName + " to be " 
                                    + "an empty string but retrieved: '" 
                                    + value + "'");
                return false;
            }        
        } 
        return true;
    }
    
    /**  Checks the if the value of a non-null field is not null.
     *
     * @param value  value to check
     * @param fieldName  field name to check
     * @returns true if value is not null, false otherwise
     */
    public static boolean checkNonNullValue(String value, String fieldName)  {
        if (value == null) {
            System.out.println("System Object expected " + fieldName + " to be " 
                                + "non-null but retrieved a null value");
            return false;
        }        
        return true;
    }
        
    /**  Checks the if the value of a non-null date field is not null.
     *
     * @param value  value to check
     * @param fieldName  field name to check
     * @returns true if value is not null, false otherwise
     */
    public static boolean checkNonNullValue(Date value, String fieldName)  {
        if (value == null) {
            System.out.println("System Object expected " + fieldName + " to be " 
                                + " non-null but retrieved a null value");
            return false;
        }        
        return true;
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
        junit.textui.TestRunner.run(new TestSuite(UpdateRecord.class));
    }
    
}
