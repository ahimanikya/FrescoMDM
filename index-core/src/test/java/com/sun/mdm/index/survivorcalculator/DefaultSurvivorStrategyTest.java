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

import com.sun.mdm.index.survivor.impl.DefaultSurvivorStrategy;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.survivor.StrategyParameter;
import com.sun.mdm.index.objects.PhoneObject;   
import java.util.Collection;
import java.util.ArrayList;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/** Test class for testing the survivor calculator's Default Strategy
 * @author Raymond Tam
 */
public class DefaultSurvivorStrategyTest extends TestCase {

    /**  instance of DefaultSurvivorStrategy
     *
     */
    private static DefaultSurvivorStrategy defaultSurvivorInstance;
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DefaultSurvivorStrategyTest(String name) {
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
    public void testDefaultStrategy() throws Exception {
        
        ArrayList param = new ArrayList();
        boolean rc = run(param);
        assertTrue(rc);
    }

    /** Tests the DefaultSurvivorStrategy
     *
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(Collection param) throws Exception {
        
        System.out.println("\n---DefaultSurvivorStrategyTest---\n");

        String testPhone0 = "6268675309";
        String testPhone1 = "6265551212";
        String testSystem0 = "QWS";
        String testSystem1 = "SBYN";
        
        StrategyParameter strategyParam = new StrategyParameter("preferredSystem", "java.lang.String", testSystem1);
        param.add(strategyParam);
        defaultSurvivorInstance = new DefaultSurvivorStrategy();
        defaultSurvivorInstance.init(param);
        
        PhoneObject phone0 = new PhoneObject();
        phone0.setPhoneType("CH");
        phone0.setPhone(testPhone0);
        phone0.setPhoneExt("1234");
        
        SystemField testField0 = new SystemField("Phone", phone0);
        SystemFieldList sysFieldList0 = new SystemFieldList();
        sysFieldList0.add(testField0);
        
        String system0 = new String(testSystem0);
        String lid0  = new String("4568915615");

        PhoneObject phone1 = new PhoneObject();
        phone1.setPhoneType("CB");
        phone1.setPhone(testPhone1);
        String system1 = new String(testSystem1);
        String lid1  = new String("4568915614");
        
        SystemField testField1 = new SystemField("Phone", phone1);
        SystemFieldList sysFieldList1 = new SystemFieldList();
        sysFieldList1.add(testField1);
        
        SystemFieldListMap sysFieldListMap = new SystemFieldListMap();
        sysFieldListMap.put(system0, lid0, sysFieldList0);
        sysFieldListMap.put(system1, lid1, sysFieldList1);
        
        //  Check if fields have been processed.  Only the second number should be present.
        SystemField result = defaultSurvivorInstance.selectField("Phone", sysFieldListMap);
        String values = result.getValue().toString();
        if (values != null)  {
            int index = values.indexOf(testPhone0);
            if (index == -1)  {
                System.out.println("Phone " + testPhone0 + " not found as expected");
            }  else  {
                System.out.println("Error: " + testPhone0 + " found but not expected");
                return false;
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
        junit.textui.TestRunner.run(new TestSuite(DefaultSurvivorStrategyTest.class));
    }
    
}
