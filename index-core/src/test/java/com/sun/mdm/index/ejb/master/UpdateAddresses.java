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
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.AddressObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.SBR;
import java.util.Collection;
import java.util.Iterator;

/** Test class for testing address updates
 * @author Raymond Tam
 */
public class UpdateAddresses extends TestCase {
    
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
    public UpdateAddresses(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests first name standardization
     * @throws Exception an error occured
     */
    public void testUpdateAddresses() throws Exception {
        
        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSystem, defaultLid);
        assertTrue(rc);
    }
    /** Tests address updates
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSystem, String tmpLid) throws Exception {
        
        System.out.println("\n---UpdateAddresses---\n");
        
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
        controller = MCFactory.getMasterController();
        System.out.println("Attempting an LID lookup");
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK(system, lid));
        if (eo == null)  {
            System.out.println("Initial EnterpriseObject retrieval failed");
            return false;
        }
        SystemObject newSysObj = eo.getSystemObject(system, lid); 
        if (newSysObj == null)  {
            System.out.println("LID lookup failed: expected 1 record, but found 0");
            return false;
        }
        System.out.println("LID lookup succeeded!");
        PersonObject newPersonObj = (PersonObject) newSysObj.pGetChildren("Person").get(0);
        
        if (newPersonObj == null)  {
            System.out.println("Test failed: no person object retrieved");
            return false;
        }
        
        //
        //  test Address Object 
        //
        
        
        //  Set up expected initial values
        
        //  Each row consists of standardized first name, standardized middle name, 
        //  and standardized last name
        String expectedInitialValues[][] = {{"800", null, "ROYAL OAKS", "DR", "RALAC"}, 
                                            {"404", "W", "HUNTINGTON", "AVE", "HANTANGT"}};
        int addressCount = 0;
        int HOUSE_NUMBER_INDEX = 0;
        int STREET_DIR_INDEX = 1;
        int STREET_NAME_INDEX = 2;
        int STREET_TYPE_INDEX = 3;
        int STREET_NAME_PHONETIC_INDEX = 4;

        
        //  Check all non-SBR aliases for person object (two are expected)
  
                                      
        Collection addressCollection = newPersonObj.getAddress();
        Iterator addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            AddressObject addressObj = (AddressObject) addressIter.next();
            System.out.println("Testing System Object Address Object" + addressCount);

            //  Test house number

            String expectedHouseNumber = expectedInitialValues[addressCount][HOUSE_NUMBER_INDEX];
            String houseNumber = addressObj.getHouseNumber();
            if (houseNumber != null && houseNumber.compareToIgnoreCase(expectedHouseNumber) == 0)  {
                System.out.println("System Object/Address Object house number OK --> " + houseNumber);
            }  else  {
                System.out.println("System Object/Address Object expected house number " 
                        + expectedHouseNumber + " but retrieved: " 
                        + houseNumber + "\nTest Failed!\n");
                return false;
            }

            //  Test street direction number.  The expected value or retrieved value may be null
            
            String expectedStreetDir = expectedInitialValues[addressCount][STREET_DIR_INDEX];
            String streetDir = addressObj.getStreetDir();
            
            //  check null case
            if (expectedStreetDir == null || expectedStreetDir.compareToIgnoreCase("null") == 0)  {
                if (streetDir == null || streetDir.compareToIgnoreCase("null") == 0)  {
                    System.out.println("System Object/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("System Object/Address Object expected null street direction but retrieved: " 
                            + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            if (streetDir != null && expectedStreetDir.compareToIgnoreCase("null") != 0)  {
                if (streetDir.compareToIgnoreCase(expectedStreetDir) == 0)  {
                    System.out.println("System Object/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("System Object/Address Object expected street direction " 
                            + expectedStreetDir + " but retrieved: " + streetDir 
                            + "\nTest Failed!\n");
                    return false;
                }
            }

            //  Test street name

            String expectedStreetName = expectedInitialValues[addressCount][STREET_NAME_INDEX];
            String streetName = addressObj.getStreetName();
            if (streetName != null && streetName.compareToIgnoreCase(expectedStreetName) == 0)  {
                System.out.println("System Object/Address Object street name OK --> " + streetName);
            }  else  {
                System.out.println("System Object/Address Object expected street name " 
                        + expectedStreetName + " but retrieved: " 
                        + streetName + "\nTest Failed!\n");
                return false;
            }

            //  Test street type

            String expectedStreetType = expectedInitialValues[addressCount][STREET_TYPE_INDEX];
            String streetType = addressObj.getStreetType();
            if (streetType != null && streetType.compareToIgnoreCase(expectedStreetType) == 0)  {
                System.out.println("System Object/Address Object street type OK --> " + streetType);
            }  else  {
                System.out.println("System Object/Address Object expected street type " 
                        + expectedStreetType + " but retrieved: " 
                        + streetType + "\nTest Failed!\n");
                return false;
            }


            //  Test street name phonetic code

            String expectedStreetNamePhonetic = expectedInitialValues[addressCount][STREET_NAME_PHONETIC_INDEX];
            String streetNamePhonetic = addressObj.getStreetNamePhoneticCode();
            if (streetNamePhonetic != null && streetNamePhonetic.compareToIgnoreCase(expectedStreetNamePhonetic) == 0) {
                System.out.println("System Object/Address Object street name phonetic OK --> " + streetNamePhonetic);
            }  else  {
                System.out.println("System Object/Address Object expected street name phonetic code " 
                        + expectedStreetNamePhonetic + " but retrieved: " 
                        + streetNamePhonetic + "\nTest Failed!\n");
                return false;
            }

            addressCount++;
        }  //  end WHILE (addressIter.hasNext()) for non-SBR address records
        
        if (addressCount != 2)  {     //  two addresses are expected
            System.out.println("Two non-SBR addresses expected but retrieved: " + addressCount + "\nTest Failed!\n");
            return false;
        }
        
        addressCount = 0;     // reset for SBR testing
        
        
        //  Check all SBR addresses for person object (two are expected)
  
                                      
        String euid = controller.getEUID(new SystemObjectPK(system, lid));
        SBR newSBR = controller.getSBR(euid);
        PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
        addressCollection = newPersonObjSBR.getAddress();
        addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            AddressObject addressObj = (AddressObject) addressIter.next();
            System.out.println("Testing SBR Address Object " + addressCount);
            //  Test house number

            String expectedHouseNumber = expectedInitialValues[addressCount][HOUSE_NUMBER_INDEX];
            String houseNumber = addressObj.getHouseNumber();
            if (houseNumber != null && houseNumber.compareToIgnoreCase(expectedHouseNumber) == 0)  {
                System.out.println("SBR/Address Object house number OK --> " + houseNumber);
            }  else  {
                System.out.println("SBR/Address Object expected " + expectedHouseNumber 
                        + " but retrieved: " + houseNumber + "\nTest Failed!\n");
                return false;
            }

            //  Test street direction number.  The expected value or retrieved value may be null
            
            String expectedStreetDir = expectedInitialValues[addressCount][STREET_DIR_INDEX];
            String streetDir = addressObj.getStreetDir();
            
            //  check null case
            if (expectedStreetDir == null || expectedStreetDir.compareToIgnoreCase("null") == 0)  {
                if (streetDir == null || streetDir.compareToIgnoreCase("null") == 0)  {
                    System.out.println("SBR/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("SBR/Address Object expected null street direction but retrieved: " 
                            + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            if (streetDir != null && expectedStreetDir.compareToIgnoreCase("null") != 0)  {
                if (streetDir.compareToIgnoreCase(expectedStreetDir) == 0)  {
                    System.out.println("SBR/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("SBR/Address Object expected " + expectedStreetDir 
                            + " but retrieved: " + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }

            //  Test street name

            String expectedStreetName = expectedInitialValues[addressCount][STREET_NAME_INDEX];
            String streetName = addressObj.getStreetName();
            if (streetName != null && streetName.compareToIgnoreCase(expectedStreetName) == 0)  {
                System.out.println("System Object/Address Object street name OK --> " + streetName);
            }  else  {
                System.out.println("System Object/Address Object expected " + expectedStreetName 
                        + " but retrieved: " + streetName + "\nTest Failed!\n");
                return false;
            }

            //  Test street type

            String expectedStreetType = expectedInitialValues[addressCount][STREET_TYPE_INDEX];
            String streetType = addressObj.getStreetType();
            if (streetType != null && streetType.compareToIgnoreCase(expectedStreetType) == 0)  {
                System.out.println("SBR/Address Object street type OK --> " + streetType);
            }  else  {
                System.out.println("SBR/Address Object expected " + expectedStreetType 
                        + " but retrieved: " + streetType + "\nTest Failed!\n");
                return false;
            }


            //  Test street name phonetic code

            String expectedStreetNamePhonetic = expectedInitialValues[addressCount][STREET_NAME_PHONETIC_INDEX];
            String streetNamePhonetic = addressObj.getStreetNamePhoneticCode();
            if (streetNamePhonetic != null && streetNamePhonetic.compareToIgnoreCase(expectedStreetNamePhonetic) == 0) {
                System.out.println("SBR/Address Object street name phonetic OK --> " + streetNamePhonetic);
            }  else  {
                System.out.println("SBR/Address Object expected " + expectedStreetNamePhonetic 
                        + " but retrieved: " + streetNamePhonetic + "\nTest Failed!\n");
                return false;
            }

            addressCount++;
        }  //  end WHILE (addressIter.hasNext()) for SBR address records
        
        
        if (addressCount != 2)  {     //  two addresses are expected
            System.out.println("Two SBR address for expected but retrieved: " + addressCount + "\nTest Failed!\n");
            return false;
        }
        
        //
        //  Update addresses and check the resulting values
        //
        
        System.out.println("Updating addresses");
        
        //  Set up expected modified values
        
        //  Each row consists of standardized first name, standardized middle name, 
        //  and standardized last name
        String expectedFinalValues[][] = {{"123", "N", "MAIN", "ST", "MAN"}, 
                                          {"456", "S", "Val", "AVE", "VAL"}};
        addressCount = 0;
        addressCollection = newPersonObj.getAddress();
        addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            AddressObject addressObj = (AddressObject) addressIter.next();
            if (addressCount == 0)  {
                addressObj.setAddressLine1("123 N. Main St."); 
            }  else  {
                addressObj.setAddressLine1("456 S. Valley Ave."); 
            }
            addressCount++;
        }
        controller.updateEnterpriseObject(eo);
        

        //  check updated values
        
        //  Check all non-SBR aliases for person object (two are expected)
  
                                      
        addressCount = 0;
        addressCollection = newPersonObj.getAddress();
        addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            AddressObject addressObj = (AddressObject) addressIter.next();
            System.out.println("Testing Updated System Object Address Object " + addressCount);

            //  Test house number

            String expectedHouseNumber = expectedFinalValues[addressCount][HOUSE_NUMBER_INDEX];
            String houseNumber = addressObj.getHouseNumber();
            if (houseNumber != null && houseNumber.compareToIgnoreCase(expectedHouseNumber) == 0)  {
                System.out.println("Updated System Object/Address Object house number OK --> " + houseNumber);
            }  else  {
                System.out.println("Updated System Object/Address Object expected " + expectedHouseNumber 
                        + " but retrieved: " + houseNumber + "\nTest Failed!\n");
                return false;
            }

            //  Test street direction number.  The expected value or retrieved value may be null
            
            String expectedStreetDir = expectedFinalValues[addressCount][STREET_DIR_INDEX];
            String streetDir = addressObj.getStreetDir();
            
            //  check null case
            if (expectedStreetDir == null || expectedStreetDir.compareToIgnoreCase("null") == 0)  {
                if (streetDir == null || streetDir.compareToIgnoreCase("null") == 0)  {
                    System.out.println("Updated System Object/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("Updated System Object/Address Object expected null street direction " 
                            + "but retrieved: " + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            if (streetDir != null && expectedStreetDir.compareToIgnoreCase("null") != 0)  {
                if (streetDir.compareToIgnoreCase(expectedStreetDir) == 0)  {
                    System.out.println("Updated System Object/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("Updated System Object/Address Object expected " + expectedStreetDir 
                            + " but retrieved: " + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }

            //  Test street name

            String expectedStreetName = expectedFinalValues[addressCount][STREET_NAME_INDEX];
            String streetName = addressObj.getStreetName();
            if (streetName != null && streetName.compareToIgnoreCase(expectedStreetName) == 0)  {
                System.out.println("Updated System Object/Address Object street name OK --> " + streetName);
            }  else  {
                System.out.println("Updated System Object/Address Object expected " + expectedStreetName 
                        + " but retrieved: " + streetName + "\nTest Failed!\n");
                return false;
            }

            //  Test street type

            String expectedStreetType = expectedFinalValues[addressCount][STREET_TYPE_INDEX];
            String streetType = addressObj.getStreetType();
            if (streetType != null && streetType.compareToIgnoreCase(expectedStreetType) == 0)  {
                System.out.println("Updated System Object/Address Object street type OK --> " + streetType);
            }  else  {
                System.out.println("Updated System Object/Address Object expected " + expectedStreetType 
                        + " but retrieved: " + streetType + "\nTest Failed!\n");
                return false;
            }


            //  Test street name phonetic code

            String expectedStreetNamePhonetic = expectedFinalValues[addressCount][STREET_NAME_PHONETIC_INDEX];
            String streetNamePhonetic = addressObj.getStreetNamePhoneticCode();
            if (streetNamePhonetic != null && streetNamePhonetic.compareToIgnoreCase(expectedStreetNamePhonetic) == 0) {
                System.out.println("Updated System Object/Address Object street name phonetic OK --> " 
                        + streetNamePhonetic);
            }  else  {
                System.out.println("Updated System Object/Address Object expected " + expectedStreetNamePhonetic 
                        + " but retrieved: " + streetNamePhonetic + "\nTest Failed!\n");
                return false;
            }

            addressCount++;
        }  //  end WHILE (addressIter.hasNext()) for non-SBR address records
        
        if (addressCount != 2)  {     //  two addresses are expected
            System.out.println("Two updated non-SBR addresses expected but retrieved: " 
                    + addressCount + "\nTest Failed!\n");
            return false;
        }
        
        addressCount = 0;     // reset for SBR testing
        
        
        //  Check all SBR addresses for person object (two are expected)
  
                                      
        euid = controller.getEUID(new SystemObjectPK(system, lid));
        newSBR = controller.getSBR(euid);
        newPersonObjSBR = (PersonObject) newSBR.getObject();
        addressCollection = newPersonObjSBR.getAddress();
        addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            AddressObject addressObj = (AddressObject) addressIter.next();
            System.out.println("Testing Updated SBR Address Object " + addressCount);

            //  Test house number

            String expectedHouseNumber = expectedFinalValues[addressCount][HOUSE_NUMBER_INDEX];
            String houseNumber = addressObj.getHouseNumber();
            if (houseNumber != null && houseNumber.compareToIgnoreCase(expectedHouseNumber) == 0)  {
                System.out.println("Updated SBR/Address Object house number OK --> " + houseNumber);
            }  else  {
                System.out.println("Updated SBR/Address Object expected " + expectedHouseNumber 
                        + " but retrieved: " + houseNumber + "\nTest Failed!\n");
                return false;
            }

            //  Test street direction number.  The expected value or retrieved value may be null
            
            String expectedStreetDir = expectedFinalValues[addressCount][STREET_DIR_INDEX];
            String streetDir = addressObj.getStreetDir();
            
            //  check null case
            if (expectedStreetDir == null || expectedStreetDir.compareToIgnoreCase("null") == 0)  {
                if (streetDir == null || streetDir.compareToIgnoreCase("null") == 0)  {
                    System.out.println("SBR/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("SBR/Address Object expected null street direction but retrieved: " 
                            + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            if (streetDir != null && expectedStreetDir.compareToIgnoreCase("null") != 0)  {
                if (streetDir.compareToIgnoreCase(expectedStreetDir) == 0)  {
                    System.out.println("Updated SBR/Address Object street direction OK --> " + streetDir);
                }  else {
                    System.out.println("Updated SBR/Address Object expected " + expectedStreetDir 
                            + " but retrieved: " + streetDir + "\nTest Failed!\n");
                    return false;
                }
            }

            //  Test street name

            String expectedStreetName = expectedFinalValues[addressCount][STREET_NAME_INDEX];
            String streetName = addressObj.getStreetName();
            if (streetName != null && streetName.compareToIgnoreCase(expectedStreetName) == 0)  {
                System.out.println("Updated System Object/Address Object street name OK --> " + streetName);
            }  else  {
                System.out.println("Updated System Object/Address Object expected " + expectedStreetName 
                        + " but retrieved: " + streetName + "\nTest Failed!\n");
                return false;
            }

            //  Test street type

            String expectedStreetType = expectedFinalValues[addressCount][STREET_TYPE_INDEX];
            String streetType = addressObj.getStreetType();
            if (streetType != null && streetType.compareToIgnoreCase(expectedStreetType) == 0)  {
                System.out.println("Updated SBR/Address Object street type OK --> " + streetType);
            }  else  {
                System.out.println("Updated SBR/Address Object expected " + expectedStreetType 
                        + " but retrieved: " + streetType + "\nTest Failed!\n");
                return false;
            }


            //  Test street name phonetic code

            String expectedStreetNamePhonetic = expectedFinalValues[addressCount][STREET_NAME_PHONETIC_INDEX];
            String streetNamePhonetic = addressObj.getStreetNamePhoneticCode();
            if (streetNamePhonetic != null && streetNamePhonetic.compareToIgnoreCase(expectedStreetNamePhonetic) == 0) {
                System.out.println("Updated SBR/Address Object street name phonetic OK --> " + streetNamePhonetic);
            }  else  {
            System.out.println("Updated SBR/Address Object expected " + expectedStreetNamePhonetic 
                        + " but retrieved: " + streetNamePhonetic + "\nTest Failed!\n");
                return false;
            }

            addressCount++;
        }  //  end WHILE (addressIter.hasNext()) for SBR address records
        
        
        if (addressCount != 2)  {     //  two addresses are expected
            System.out.println("Two updated SBR address for expected but retrieved: " 
                    + addressCount + "\nTest Failed!\n");
            return false;
        }
        
        System.out.println("Test succeeded!\n");
        return true;       
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(UpdateAddresses.class));
    }
    
}
