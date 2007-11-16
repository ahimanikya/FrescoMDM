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
package com.sun.mdm.index.survivorcalculator;

import com.sun.mdm.index.survivor.impl.UnionSurvivorStrategy;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.objects.PhoneObject;   
import java.util.Collection;
import java.util.Date;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/** Test class for testing the survivor calculator's Union Strategy
 * @author Raymond Tam
 */
public class UnionSurvivorStrategyTest extends TestCase {

    /**  instance of UnionSurvivorStrategy
     *
     */
    private static UnionSurvivorStrategy unionSurvivorInstance;
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UnionSurvivorStrategyTest(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests updating standardized first names
     * @throws Exception an error occured
     */
    public void testUnionStrategy() throws Exception {
        
        // this strategy does not need initialization parameters
        Collection param = null;
        boolean rc = run(param);
        assertTrue(rc);
    }

    /** Tests the UnionSurvivorStrategy
     *
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(Collection param) throws Exception {
        
        System.out.println("\n---UnionSurvivorStrategyTest---\n");

        unionSurvivorInstance = new UnionSurvivorStrategy();
        unionSurvivorInstance.init(param);
        String testPhone0 = "6268675309";
        String testPhone1 = "6265551212";
        
        PhoneObject phone0 = new PhoneObject();
        phone0.setPhoneType("CH");
        phone0.setPhone(testPhone0);
        phone0.setPhoneExt("1234");
        
        SystemField testField00 = new SystemField("Phone", phone0);
        SystemField testField01 = new SystemField("LastModified", new Date());
        SystemFieldList sysFieldList0 = new SystemFieldList();
        sysFieldList0.add(testField00);
        sysFieldList0.add(testField01);
        
        String system0 = new String("QWS");
        String lid0  = new String("4568915615");

        PhoneObject phone1 = new PhoneObject();
        phone1.setPhoneType("CB");
        phone1.setPhone(testPhone1);
        String system1 = new String("SBYN");
        String lid1  = new String("4568915614");
        
        SystemField testField10 = new SystemField("Phone", phone1);
        SystemField testField11 = new SystemField("LastModified", new Date());
        SystemFieldList sysFieldList1 = new SystemFieldList();
        sysFieldList1.add(testField10);
        sysFieldList1.add(testField11);
        
        SystemFieldListMap sysFieldListMap = new SystemFieldListMap();
        sysFieldListMap.put(system0, lid0, sysFieldList0);
        sysFieldListMap.put(system1, lid1, sysFieldList1);
        
        //  Check if fields have been UNIONed.  There should be two phone numbers
        SystemField result = unionSurvivorInstance.selectField("Phone", sysFieldListMap);
        String values = result.getValue().toString();
        if (values != null)  {
            int index = values.indexOf(testPhone0);
            if (index == -1)  {
                System.out.println("Error: " + testPhone0 + " expected but not found");
                return false;
            }  else  {
                System.out.println("Phone " + testPhone0 + " found as expected");
            }
            index = values.indexOf(testPhone1);
            if (index == -1)  {
                System.out.println("Error: " + testPhone1 + " expected but not found");
                return false;
            }  else  {
                System.out.println("Phone " + testPhone1 + " found as expected");
            }
        }  else {
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
        junit.textui.TestRunner.run(new TestSuite(UnionSurvivorStrategyTest.class));
    }
    
}
