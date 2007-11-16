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

import java.util.List;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchDeferredHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.update.UpdateResult;

/** Test class for delete system object method
 * @author Raymond Tam
 */
public class DeferredPessimisticMode extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeferredPessimisticMode(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
       ClearDb.run();
    }
    
    /** Tests deferred pessimistic mode processing: no pessimistic mode processing
     * @throws Exception an error occured
     */
    public void testExecuteMatchDupRecalc1() throws Exception {
        ExecuteMatchDeferredHelper createHelper = new ExecuteMatchDeferredHelper(false); 
        List result = createHelper.runDupRecalc(new String[] {"fileName=DeferredPessimisticMode1.txt", "fileType=eiEvent"});
        
        // Verify that no potential duplicates were generated
        MatchResult mr = (MatchResult) result.get(1);
        assertEquals(mr.getPotentialDuplicates(), null);
        
        // Verify that match fields have not changed
        boolean obj = mr.getMatchFieldChanged();
        assertEquals(obj, false);
        
        // Verify that match fields have changed
        mr = (MatchResult) result.get(2);
        obj = mr.getMatchFieldChanged();
        assertEquals(obj, true);
        
        // Verify that there is 2 potential duplicates in the database (from the new EO
        // and not from the updates)
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 2);
    }    

    /** Tests deferred pessimistic mode processing: pessimistic mode processing enabled
     * @throws Exception an error occured
     */
    public void testExecuteMatchDupRecalc2() throws Exception {
        ExecuteMatchDeferredHelper createHelper = new ExecuteMatchDeferredHelper(true); 
        List result = createHelper.runDupRecalc(new String[] {"fileName=DeferredPessimisticMode1.txt", "fileType=eiEvent"});
        
        // Verify that match result 3 indicates 1 potential duplicate was generated
        MatchResult mr = (MatchResult) result.get(2);
        assertEquals(1, mr.getPotentialDuplicates().length);
        
        //  Verify that match result 4 has 2 potential duplicates
        mr = (MatchResult) result.get(3);
        assertEquals(2, mr.getPotentialDuplicates().length);
        
        // Verify that match fields have not changed
        mr = (MatchResult) result.get(1);
        boolean obj = mr.getMatchFieldChanged();
        assertEquals(obj, false);
        
        // Verify that match fields have changed
        mr = (MatchResult) result.get(2);
        obj = mr.getMatchFieldChanged();
        assertEquals(obj, true);

        // Verify that there are 3 total potential duplicates in the database
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 3);
        
    }    

    /** Tests deferred pessimistic mode processing: no pessimistic mode processing
     * @throws Exception an error occured
     */
    public void testExecuteMatchUpdateDupRecalc1() throws Exception {
        ExecuteMatchDeferredHelper createHelper = new ExecuteMatchDeferredHelper(false); 
        List result = createHelper.runUpdateDupRecalc(new String[] {"fileName=DeferredPessimisticMode1.txt", "fileType=eiEvent"});
        
        // Verify that no potential duplicates were generated
        MatchResult mr = (MatchResult) result.get(1);
        assertEquals(mr.getPotentialDuplicates(), null);
        
        // Verify that match fields have not changed
        boolean obj = mr.getMatchFieldChanged();
        assertEquals(obj, false);

        // Verify that match fields have changed
        mr = (MatchResult) result.get(2);
        obj = mr.getMatchFieldChanged();
        assertEquals(obj, true);
        assertEquals(mr.getPotentialDuplicates(), null);
        
        // Verify that there is 2 potential duplicates in the database (from the new EO
        // and not from the updates)
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 2);
        
    }    

    /** Tests deferred pessimistic mode processing: pessimistic mode processing enabled
     * @throws Exception an error occured
     */
    public void testExecuteMatchUpdateDupRecalc2() throws Exception {
        ExecuteMatchDeferredHelper createHelper = new ExecuteMatchDeferredHelper(true); 
        List result = createHelper.runUpdateDupRecalc(new String[] {"fileName=DeferredPessimisticMode1.txt", "fileType=eiEvent"});
        
        // Verify that match result 3 indicates 1 potential duplicate was generated
        MatchResult mr = (MatchResult) result.get(2);
        assertEquals(1, mr.getPotentialDuplicates().length);
        
        //  Verify that match result 4 has 2 potential duplicates
        mr = (MatchResult) result.get(3);
        assertEquals(2, mr.getPotentialDuplicates().length);
        
        // Verify that match fields have not changed
        mr = (MatchResult) result.get(0);
        boolean obj = mr.getMatchFieldChanged();
        assertEquals(obj, false);

        // Verify that match fields have changed
        mr = (MatchResult) result.get(2);
        obj = mr.getMatchFieldChanged();
        assertEquals(obj, true);
        
        // Verify that there are 3 total potential duplicates in the database
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 3);
        
    }    

    /** Tests deferred pessimistic mode processing: no pessimistic mode processing
     * @throws Exception an error occured
     */
    public void testUpdateEnterpriseDupRecalc1() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject3.txt", "fileType=generic"});
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        PersonObject personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        
        // Update record
        personNode.setFirstName("JOHN");
        personNode.setLastName("SMITH");
        Boolean val = new Boolean(false);
        UpdateResult result = controller.updateEnterpriseDupRecalc(eo, val);
        
        // Verify that the at least one match field was changed
        boolean val2 = result.getMatchFieldChanged();
        assertEquals(val2, true);
        
        // Verify that no duplicate was created
        LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 0);
    }
    
    /** Tests deferred pessimistic mode processing: pessimistic mode processing enabled
     * @throws Exception an error occured
     */
    public void testUpdateEnterpriseDupRecalc2() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject3.txt", "fileType=generic"});
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        PersonObject personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        
        // Update record
        personNode.setFirstName("JOHN");
        personNode.setLastName("SMITH");
        Boolean val = new Boolean(true);
        UpdateResult result = controller.updateEnterpriseDupRecalc(eo, val);
        
        // Verify that the at least one match field was changed
        boolean val2 = result.getMatchFieldChanged();
        assertEquals(val2, true);
        
        // Verify that a duplicate was created
        LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 1);
    }

    /** Tests forced calculation of potential duplicates for executeMatch
     * @throws Exception an error occured
     */
    public void testCalculatePotentialDuplicates() throws Exception {
        ExecuteMatchDeferredHelper createHelper = new ExecuteMatchDeferredHelper(false); 
        List result = createHelper.runDupRecalc(new String[] {"fileName=DeferredPessimisticMode1.txt", "fileType=eiEvent"});
        MatchResult mr = (MatchResult) result.get(2);
        String euid = mr.getEUID();
        String transid = mr.getTransactionNumber();

        // Verify that 3 potential duplicate were generated
        MasterController mc = MCFactory.getMasterController();
        mc.calculatePotentialDuplicates(euid, transid);
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 3);

        // Update the last record so that there it no longer has any potential 
        // duplicates.  The existing potential duplicate records will be deleted
        // in calculatePotentialDuplicates() before the potential duplicates
        // are actually recalculated.
        
        result = createHelper.runDupRecalc(new String[] {"fileName=DeferredPessimisticMode2.txt", "fileType=eiEvent", "clearDb=false"});
        mr = (MatchResult) result.get(0);
        euid = mr.getEUID();
        transid = mr.getTransactionNumber();
        
        // Verify that 3 potential duplicates still remain
        i = potDupHelper.run(new String[0]);
        count = i.count();
        assertEquals(count, 3);
        
        // Verify that 1 potential duplicates still remains
        mc.calculatePotentialDuplicates(euid, transid);
        i = potDupHelper.run(new String[0]);
        count = i.count();
        assertEquals(count, 1);
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
                ts.addTest(new DeferredPessimisticMode(args[i]));
            }
        } else {
            ts = new TestSuite(DeferredPessimisticMode.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
