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
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.AliasObject;   
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.SBR;
import java.util.Collection;
import java.util.Iterator;

/** Test class for testing first name standardization
 * @author Raymond Tam
 */
public class StandardizeNames extends TestCase {
    
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
    public StandardizeNames(String name) {
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
    public void testStandardizeNames() throws Exception {
        
        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSystem, defaultLid);
        assertTrue(rc);
    }
    /** Tests standardizing names  
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSystem, String tmpLid) throws Exception {
        
        System.out.println("\n---StandardizeNames---\n");
        
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
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        if (newSysObj == null)  {
            System.out.println("LID lookup failed: expected 1 record, but found 0");
            return false;
        }
        System.out.println("LID lookup succeeded!");
        PersonObject newPersonObj = (PersonObject) newSysObj.pGetChildren("Person").get(0);
        
        //
        //  test Person Object 
        //
        
        if (newPersonObj == null)  {
            System.out.println("Test failed: no person object retrieved");
            return false;
        }
        
        //  test standardized first name
        
        String stdFname = newPersonObj.getStdFirstName();
        if (stdFname != null && stdFname.compareToIgnoreCase("DEBORAH") == 0)  {
            
            //  test SBR
            String euid = controller.getEUID(new SystemObjectPK(system, lid));
            SBR newSBR = controller.getSBR(euid);
            PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
            
            System.out.println("System Object/Person Object standardized first name OK --> " + stdFname);
            stdFname = newPersonObjSBR.getStdFirstName();
            if (stdFname != null && stdFname.compareToIgnoreCase("DEBORAH") == 0)  {
                System.out.println("SBR/Person Object Standardized first name OK --> " + stdFname);
            } else  {
                System.out.println("SBR/Person Object expected DEBORAH but retrieved: " + stdFname);
                System.out.println("Test Failed!\n");
                return false;
            }
        }  else  {
            System.out.println("System Object/Person Object expected DEBORAH but retrieved: " + stdFname);
            System.out.println("Test Failed!\n");
            return false;
        }
        
        //  test standardized last name
        
        String stdLname = newPersonObj.getStdLastName();
        if (stdLname != null && stdLname.compareTo("WOMACK") == 0)  {
            
            //  test SBR
            String euid = controller.getEUID(new SystemObjectPK(system, lid));
            SBR newSBR = controller.getSBR(euid);
            PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
            
            System.out.println("System Object/Person Object standardized last name OK --> " + stdLname);
            stdLname = newPersonObjSBR.getStdLastName();
            if (stdLname != null && stdLname.compareTo("WOMACK") == 0)  {
                System.out.println("SBR/Person Object Standardized last name OK --> " + stdLname);
            } else  {
                System.out.println("SBR/Person Object expected WOMACK but retrieved: " + stdLname);
                System.out.println("Test Failed!\n");
                return false;
            }
        }  else  {
            System.out.println("System Object/Person Object expected WOMACK but retrieved: " + stdLname);
            System.out.println("Test Failed!\n");
            return false;
        }
        
        //  test standardized middle name
        
        String stdMname = newPersonObj.getStdMiddleName();
        if (stdMname != null && stdMname.compareTo("KAREN") == 0)  {
            
            //  test SBR
            String euid = controller.getEUID(new SystemObjectPK(system, lid));
            SBR newSBR = controller.getSBR(euid);
            PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
            
            System.out.println("System Object/Person Object standardized middle name OK --> " + stdMname);
            stdMname = newPersonObjSBR.getStdMiddleName();
            if (stdMname != null && stdMname.compareTo("KAREN") == 0)  {
                System.out.println("SBR/Person Object Standardized middle name OK --> " + stdMname);
            } else  {
                System.out.println("SBR/Person Object expected KAREN but retrieved: " + stdMname);
                System.out.println("Test Failed!\n");
                return false;
            }
        }  else  {
            System.out.println("System Object/Person Object expected KAREN but retrieved: " + stdMname);
            System.out.println("Test Failed!\n");
            return false;
        }

        
        //
        //  test alias object
        //
        
        
        //  Set up expected values
        
        //  Each row consists of standardized first name, standardized middle name, 
        //  and standardized last name
        String expectedValues[][] = {{"DEBORAH", null, "JACKSON"}, 
                                     {"DOROTHY", "JUDITH", "JONES"}};
        int aliasCount = 0;
        int STD_FNAME_INDEX = 0;
        int STD_MNAME_INDEX = 1;
        int STD_LNAME_INDEX = 2;

        
        //  Check all non-SBR aliases for person object (two are expected)
  
                                      
        Collection aliasCollection = newPersonObj.getAlias();
        Iterator aliasIter = aliasCollection.iterator();
        while (aliasIter.hasNext())  {
            AliasObject aliasObj = (AliasObject) aliasIter.next();

            //  Test standardized first name
            String expectedStdFname = expectedValues[aliasCount][STD_FNAME_INDEX];
            String stdAliasFname = aliasObj.getStdFirstName();
            if (stdAliasFname != null && stdAliasFname.compareToIgnoreCase(expectedStdFname) == 0)  {
                System.out.println("System Object/Alias Object standardized first name OK --> " + stdAliasFname);
            }  else  {
                System.out.println("System Object/Alias Object expected " + expectedStdFname 
                        + " but retrieved: " + stdAliasFname);
                System.out.println("Test Failed!\n");
                return false;
            }

            //  Test standardized last name
            String expectedStdLname = expectedValues[aliasCount][STD_LNAME_INDEX];
            String stdAliasLname = aliasObj.getStdLastName();
            if (stdAliasLname != null && stdAliasLname.compareTo(expectedStdLname) == 0)  {
                System.out.println("System Object/Alias Object standardized last name OK --> " + stdAliasLname);
            }  else {
                System.out.println("System Object/Alias Object expected " + expectedStdLname 
                        + " but retrieved: " + stdAliasLname);
                System.out.println("Test Failed!\n");
                return false;
            }

            //  Test standardized middle name.  Because this is optional, the expected value or 
            //  retrieved value may be null
            String expectedStdMname = expectedValues[aliasCount][STD_MNAME_INDEX];
            String stdAliasMname = aliasObj.getStdMiddleName();
            
            //  check null case
            if (expectedStdMname == null || expectedStdMname.compareToIgnoreCase("null") == 0)  {
                if (stdAliasMname == null || stdAliasMname.compareToIgnoreCase("null") == 0)  {
                    System.out.println("System Object/Alias Object standardized middle name OK --> " + stdAliasMname);
                }  else {
                    System.out.println("System Object/Alias Object expected null middle name but retrieved: " 
                            + stdAliasMname);
                    System.out.println("Test Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            
            if (stdAliasMname != null && expectedStdMname.compareToIgnoreCase("null") != 0)  {
                if (stdAliasMname.compareToIgnoreCase(expectedStdMname) == 0)  {
                    System.out.println("System Object/Alias Object standardized middle name OK --> " + stdAliasMname);
                }  else {
                    System.out.println("System Object/Alias Object1 expected " + expectedStdMname 
                            + " but retrieved: " + stdAliasMname);
                    System.out.println("Test Failed!\n");
                    return false;
                }
            }

            aliasCount++;
        }  //  end WHILE (aliasIter.hasNext()) for non-SBR alias records
        
        if (aliasCount != 2)  {     //  two aliases are expected
            System.out.println("Two non-SBR aliases expected but retrieved: " + aliasCount);
            System.out.println("Test Failed!\n");
            return false;
        }
        
        aliasCount = 0;     // reset for SBR testing
        
        //  Check all SBR aliases for person object (two are expected)
  
                                      
        String euid = controller.getEUID(new SystemObjectPK(system, lid));
        SBR newSBR = controller.getSBR(euid);
        PersonObject newPersonObjSBR = (PersonObject) newSBR.getObject();
        aliasCollection = newPersonObjSBR.getAlias();
        aliasIter = aliasCollection.iterator();
        while (aliasIter.hasNext())  {
            AliasObject aliasObj = (AliasObject) aliasIter.next();

            //  Test standardized first name
            String expectedStdFname = expectedValues[aliasCount][STD_FNAME_INDEX];
            String stdAliasFname = aliasObj.getStdFirstName();
            if (stdAliasFname != null && stdAliasFname.compareToIgnoreCase(expectedStdFname) == 0)  {
                System.out.println("SBR/Alias Object Standardized first name OK --> " + stdAliasFname);
            }  else  {
                    System.out.println("SBR/Alias Object expected " + expectedStdFname 
                            + " but retrieved: " + stdAliasFname);
                System.out.println("Test Failed!\n");
                return false;
            }

            //  Test standardized last name
            String expectedStdLname = expectedValues[aliasCount][STD_LNAME_INDEX];
            String stdAliasLname = aliasObj.getStdLastName();
            if (stdAliasLname != null && stdAliasLname.compareTo(expectedStdLname) == 0)  {
                System.out.println("SBR/Alias Object Object Standardized last name OK --> " + stdAliasLname);
            }  else {
                    System.out.println("SBR/Alias Object expected " + expectedStdLname 
                            + " but retrieved: " + stdAliasLname);
                System.out.println("Test Failed!\n");
                return false;
            }

            //  Test standardized middle name.  Because this is optional, the expected value or 
            //  retrieved value may be null
            String expectedStdMname = expectedValues[aliasCount][STD_MNAME_INDEX];
            String stdAliasMname = aliasObj.getStdMiddleName();
            
            //  check null case
            
            if (expectedStdMname == null || expectedStdMname.compareToIgnoreCase("null") == 0)  {
                if (stdAliasMname == null || stdAliasMname.compareToIgnoreCase("null") == 0)  {
                    System.out.println("SBR/Alias Object standardized middle name OK --> " + stdAliasMname);
                }  else {
                    System.out.println("SBR/Alias Object expected null middle name but retrieved: " + stdAliasMname);
                    System.out.println("Test Failed!\n");
                    return false;
                }
            }
            
            //  check non-null case
            
            if (stdAliasMname != null && expectedStdMname.compareToIgnoreCase("null") != 0)  {
                if (stdAliasMname.compareToIgnoreCase(expectedStdMname) == 0)  {
                    System.out.println("SBR/Alias Object standardized middle name OK --> " + stdAliasMname);
                }  else {
                    System.out.println("SBR/Alias Object1 expected " + expectedStdMname 
                            + " but retrieved: " + stdAliasMname);
                    System.out.println("Test Failed!\n");
                    return false;
                }
            }
            
            aliasCount++;
        }  //  end WHILE (aliasIter.hasNext()) for SBR alias records
        
        
        if (aliasCount != 2)  {     //  two aliases are expected
            System.out.println("Two SBR aliases for expected but retrieved: " + aliasCount);
            System.out.println("Test Failed!\n");
            return false;
        }
        
        System.out.println("Test succeeded!\n");
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
        junit.textui.TestRunner.run(new TestSuite(StandardizeNames.class));
    }
    
}
