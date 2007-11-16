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

import com.sun.mdm.index.survivor.impl.WeightedSurvivorStrategy;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.survivor.StrategyParameter;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.objects.PhoneObject;   
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;

/** Test class for testing the survivor calculator's Weighted Strategy
 * @author Raymond Tam
 */
public class WeightedSurvivorStrategy1Test extends TestCase {

    /**  instance of WeightedSurvivorStrategy
     *
     */
    private static WeightedSurvivorStrategy weightedSurvivorInstance;

    /**  instance of TEST SRC */
    private static String TestSrc;
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public WeightedSurvivorStrategy1Test(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests the WeightSurvivorStrategy (most recent modified)
     * @throws Exception an error occured
     */
    public void testWeightedSurvivorStrategy1() throws Exception {
        
        ArrayList param = new ArrayList();
        boolean rc = run(param);
        assertTrue(rc);
    }

    /** Tests the WeightedSurvivorStrategy using the MostRecentModified criterion
     *
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(Collection param) throws Exception {
        
        System.out.println("\n---WeightedSurvivorStrategy1Test---\n");
        
        String testPhone0 = "6268675309";
        String testPhone1 = "6265551212";
        String testPhoneType0 = "CH";
        String testPhoneType1 = "CB";
        
        TestSrc = System.getProperty("stc.root");
        InputStream fs = null;
        if (TestSrc != null)  {
            fs = new FileInputStream(TestSrc + "/com/sun/mdm/index/survivorcalculator/config/update1.xml");
        }  else  {
            fs = WeightedSurvivorStrategy1Test.class.getClassLoader().getResourceAsStream(
                    ("com/sun/mdm/index/survivorcalculator/config/update1.xml"));
        }
        ConfigurationService configService = new ConfigurationService();
        configService.load(fs);
        
        StrategyParameter strategyParam = new StrategyParameter("ConfigurationModuleName", "java.lang.String", "WeightedSurvivorCalculator");
        param.add(strategyParam);
        weightedSurvivorInstance = new WeightedSurvivorStrategy();
        weightedSurvivorInstance.init(param);
        
        PhoneObject phone0 = new PhoneObject();
        phone0.setPhoneType(testPhoneType0);
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
        phone1.setPhoneType(testPhoneType0);
        phone1.setPhone(testPhone1);
        
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        Date oldDate = df.parse("Jan 1, 1901");  
        SystemField testField10 = new SystemField("Phone", phone1);
        SystemField testField11 = new SystemField("LastModified", oldDate);
        SystemFieldList sysFieldList1 = new SystemFieldList();
        sysFieldList1.add(testField10);
        sysFieldList1.add(testField11);
        String system1 = new String("SBYN");
        String lid1  = new String("4568915614");
        
        SystemFieldListMap sysFieldListMap = new SystemFieldListMap();
        sysFieldListMap.put(system0, lid0, sysFieldList0);
        sysFieldListMap.put(system1, lid1, sysFieldList1);
        
        
        //  Check if fields have been processed.  
        SystemField result = weightedSurvivorInstance.selectField("Phone", sysFieldListMap);
        String values = result.getValue().toString();
        if (values != null)  {
            //  Only testphone0 should be present.
            int index = values.indexOf(testPhone0);
            if (index == -1)  {
                System.out.println("Error: phone " + testPhone0 + " expected but not found");
                return false;
            }  else  {
                System.out.println("Phone " + testPhone0 + " found (expected result)");
            }
            index = values.indexOf(testPhone1);
            if (index == -1)  {
                System.out.println("Phone " + testPhone1 + " not found (expected result)");
            }  else  {
                System.out.println("Error: phone " + testPhone1 + " found but not expected");
                return false;
            }
            //  testPhoneType0 should be the phone type
            index = values.indexOf(testPhoneType1);
            if (index == -1)  {
                System.out.println("Phone type " + testPhoneType1 + " not found (expected result)");
            }  else  {
                System.out.println("Error: phone type " + testPhoneType1 + " found but not expected");
                return false;
            }
            index = values.indexOf(testPhoneType0);
            if (index == -1)  {
                System.out.println("Error: phone type " + testPhoneType0 + " not found but expected");
                return false;
            }  else  {
                System.out.println("Phone type " + testPhoneType0 + " found (expected result)");
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
        junit.textui.TestRunner.run(new TestSuite(WeightedSurvivorStrategy1Test.class));
    }
    
}
