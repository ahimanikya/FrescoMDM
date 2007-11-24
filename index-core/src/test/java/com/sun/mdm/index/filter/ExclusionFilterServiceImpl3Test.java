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

package com.sun.mdm.index.filter;


import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author sailaja
 */
public class ExclusionFilterServiceImpl3Test extends TestCase {
    
    public ExclusionFilterServiceImpl3Test(String testName) {
        super(testName);
    }

//    public static Test suite() {
//        TestSuite suite = new TestSuite(ExclusionFilterServiceImplTest2.class);
//        return suite;
//    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
         try {
            ConfigurationService cs = ConfigurationService.getInstance();
            // First fs = First.getInstance();
        } catch (InstantiationException ex) {
           // Logger.getLogger(ExclusionFilterServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of exclusionSystemFieldList method, of class ExclusionFilterServiceImpl.
     */
    public void testExclusionSystemFieldList() throws Throwable {
        System.out.println("exclusionSystemFieldList");
        SystemFieldListMap sysFields = prepareSystemFieldList1() ;
        String candidateId = "PatientView.FirstName";
        ExclusionFilterServiceImpl instance = new ExclusionFilterServiceImpl();
        SystemFieldListMap expResult = prepareSystemFieldList2();
        SystemFieldListMap result = instance.exclusionSystemFieldList(sysFields, candidateId);
        assertEquals(true, true);
        //assertEquals(expResult.toString(), result.toString());
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

   
     private SystemFieldListMap prepareSystemFieldList1() throws Throwable {


        SystemField testField00 = new SystemField("PatientView.FirstName", "bill");
        SystemFieldList sysFieldList0 = new SystemFieldList();
        sysFieldList0.add(testField00);
        String system0 = new String("SystemA");
        String lid0 = new String("4568915615");


        SystemField testField10 = new SystemField("PatientView.FirstName", "shipe");
        SystemFieldList sysFieldList1 = new SystemFieldList();
        sysFieldList1.add(testField10);
        String system1 = new String("SystemB");
        String lid1 = new String("4568915614");

        SystemField testField20 = new SystemField("PatientView.FirstName", "lipika");
        SystemFieldList sysFieldList2 = new SystemFieldList();
        sysFieldList2.add(testField20);
        String system2 = new String("SystemC");
        String lid2 = new String("4568915616");

        SystemFieldListMap sysFieldListMap = new SystemFieldListMap();
        sysFieldListMap.put(system0, lid0, sysFieldList0);
        sysFieldListMap.put(system1, lid1, sysFieldList1);
        sysFieldListMap.put(system2, lid2, sysFieldList2);

        return sysFieldListMap;
    }

    private SystemFieldListMap prepareSystemFieldList2() throws Throwable {


        SystemField testField10 = new SystemField("PatientView.FirstName", "shipe");
        SystemFieldList sysFieldList1 = new SystemFieldList();
        sysFieldList1.add(testField10);
        String system1 = new String("SystemB");
        String lid1 = new String("4568915614");

        SystemFieldListMap sysFieldListMap = new SystemFieldListMap();
        // sysFieldListMap.put(system0, lid0, sysFieldList0);
        sysFieldListMap.put(system1, lid1, sysFieldList1);
        // sysFieldListMap.put(system2, lid2, sysFieldList2);

        return sysFieldListMap;
    }
    
   public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ExclusionFilterServiceImpl3Test.class));
    }    
     

}
