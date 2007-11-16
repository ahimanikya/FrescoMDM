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
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObject;
import java.util.List;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.master.MatchResult;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class ExecuteMatch extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public ExecuteMatch(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
       ClearDb.run();
    }
    
    /** Tests valid creation and update based on system object key
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        createHelper.run(new String[] {"fileName=ExecuteMatch1.txt", "fileType=eiEvent"});
    }
    
    /** Tests assumed match update
     * @throws Exception an error occured
     */
    public void test2() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List result = createHelper.run(new String[] {"fileName=ExecuteMatch2.txt", "fileType=eiEvent"});
        
        //Verify that only one EO was created
        MatchResult mr1 = (MatchResult) result.get(0);
        MatchResult mr2 = (MatchResult) result.get(1);
        String euid1 = mr1.getEUID();
        String euid2 = mr2.getEUID();
        assertTrue(euid1.equals(euid2));
        
        //Verify that new String1 value was persisted
        MasterController mc = MCFactory.getMasterController();
        EnterpriseObject eo = mc.getEnterpriseObject(euid1);
        PersonObject person = (PersonObject) eo.getSBR().getObject();
        String string1 = person.getString1();
        assertTrue(string1.equals("STRING1MOD"));
        
        //TODO: check assumed match count
        
    }

    /** Test pessimistic mode
     * @throws Exception an error occured
     */
    public void test4() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List result = createHelper.run(new String[] {"fileName=ExecuteMatch4.txt", "fileType=eiEvent"});
        
        //Verify that match result 3 indicates 1 potential duplicate found
        MatchResult mr = (MatchResult) result.get(2);
        assertEquals(1, mr.getPotentialDuplicates().length);
    }    

    /** Tests valid creation and update based on system object key for update mode
     * @throws Exception an error occured
     */
    public void testExecuteMatchUpdate() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List result = createHelper.update(new String[] {"fileName=ExecuteMatch5.txt", "fileType=generic"});
        MatchResult mr1 = (MatchResult) result.get(0);
        String euid1 = mr1.getEUID();
        
        //Verify that last name is updated, SSN value and phone are not null after update
        MasterController mc = MCFactory.getMasterController();
        EnterpriseObject eo = mc.getEnterpriseObject(euid1);
        PersonObject person = (PersonObject) eo.getSBR().getObject();
        String lname = person.getLastName();
        assertTrue(lname.equals("JONES-LEE"));
        String ssn = person.getSSN();
        assertTrue(ssn.equals("111223333"));
        assertTrue(person.getPhone().size()==2);
    }
        
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new ExecuteMatch(args[i]));
            }
        } else {
            ts = new TestSuite(ExecuteMatch.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
