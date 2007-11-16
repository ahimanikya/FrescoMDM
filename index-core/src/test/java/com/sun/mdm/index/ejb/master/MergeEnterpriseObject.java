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
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class MergeEnterpriseObject extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    private EnterpriseObject mEntObjSource;
    private EnterpriseObject mEntObjDest;
    private CreateEnterpriseObjectHelper mHelper;

    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeEnterpriseObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
        
    }
    
    /** Tests an EO merge 
     * @throws Exception an error occured
     */
    public void testMerge() throws Exception {
        CreateEnterpriseObjectHelper cHelper = new CreateEnterpriseObjectHelper();
        List eoList = cHelper.run(new String[] {"fileName=MergeEnterpriseObject1.txt", "fileType=eiEvent"});
        mEntObjSource = (EnterpriseObject) eoList.get(1);
        mEntObjDest = (EnterpriseObject) eoList.get(0);
        //Merge the records
        MergeEnterpriseObjectHelper helper = new MergeEnterpriseObjectHelper();
        String sourceEUID = mEntObjSource.getEUID();
        String destEUID = mEntObjDest.getEUID();
        String calcOnly = "false";
        log("Merging records.  Source: " + sourceEUID + " Dest: " + destEUID);
        String[] mergeCommand = {"euidSource=" + sourceEUID, "euidDest=" + destEUID, "calcOnly=" + calcOnly}; 
        MergeResult mr = helper.run(mergeCommand);
        log("Merge completed.");
        
        //Test assertions
        log("Testing assertions.");
        
        //Check history 
        assertTrue(mr != null);
        TransactionObject tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        String transId = tobj.getTransactionNumber();
        String func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidMerge"));
        MasterController mc0 = MCFactory.getMasterController();
        TransactionSummary summary = mc0.lookupTransaction(transId);
        EnterpriseObjectHistory rr = summary.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO1 = rr.getBeforeEO1();
        EnterpriseObject beforeEO2 = rr.getBeforeEO2();
        EnterpriseObject afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 != null);
        
        //Make sure there are no potential duplicates (QAI 57971)
        LookupPotentialDuplicatesHelper helper2 = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper2.run(new String[0]);
        int count = i.count();
        assertEquals(0, count);
    }

    /** Tests an EO unmerge 
     * @throws Exception an error occured
     */
    public void testUnmerge() throws Exception {
        CreateEnterpriseObjectHelper cHelper = new CreateEnterpriseObjectHelper();
        List eoList = cHelper.run(new String[] {"fileName=MergeEnterpriseObject1.txt", "fileType=eiEvent"});
        mEntObjSource = (EnterpriseObject) eoList.get(1);
        mEntObjDest = (EnterpriseObject) eoList.get(0);
        //Merge the records
        MergeEnterpriseObjectHelper helper = new MergeEnterpriseObjectHelper();
        String sourceEUID = mEntObjSource.getEUID();
        String destEUID = mEntObjDest.getEUID();
        String calcOnly = "false";
        log("Merging records.  Source: " + sourceEUID + " Dest: " + destEUID);
        String[] mergeCommand = {"euidSource=" + sourceEUID, "euidDest=" + destEUID, "calcOnly=" + calcOnly}; 
        MergeResult mr = helper.run(mergeCommand);
        log("Merge completed.");
        
        //Unmerge the records
        UnmergeEnterpriseObjectHelper helper2 = new UnmergeEnterpriseObjectHelper();
        String euid = mEntObjDest.getEUID();
        log("Unmerging records.");
        String[] unmergeCommand = {"euid=" + euid, "calcOnly=" + calcOnly}; 
        mr = helper2.run(unmergeCommand);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions.");   
        assertTrue(mr != null);
        TransactionObject tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        String transId = tobj.getTransactionNumber();
        String func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidUnMerge"));
        MasterController mc0 = MCFactory.getMasterController();
        TransactionSummary summary = mc0.lookupTransaction(transId);
        EnterpriseObjectHistory rr = summary.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO1 = rr.getBeforeEO1();
        EnterpriseObject beforeEO2 = rr.getBeforeEO2();
        EnterpriseObject afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 == null);
    }
    
    /** Tests an EO unmerge - specifically to check that aliases are removed
     *
     * @throws Exception an error occured
     */
    public void testUnmerge2() throws Exception {
        ExecuteMatchHelper mHelper = new ExecuteMatchHelper();
        List eoList = mHelper.run(new String[] {"fileName=MergeEnterpriseObject2.txt", "fileType=eiEvent"});
        MatchResult mrSource = (MatchResult) eoList.get(2);
        MatchResult mrDest = (MatchResult) eoList.get(0);
        //Merge the records
        MergeEnterpriseObjectHelper helper = new MergeEnterpriseObjectHelper();
        String sourceEUID = mrSource.getEUID();
        String destEUID = mrDest.getEUID();
        String calcOnly = "false";
        log("Merging records.  Source: " + sourceEUID + " Dest: " + destEUID);
        String[] mergeCommand = {"euidSource=" + sourceEUID, "euidDest=" + destEUID, "calcOnly=" + calcOnly}; 
        MergeResult mr = helper.run(mergeCommand);
        log("Merge completed.");
        
        //Unmerge the records
        UnmergeEnterpriseObjectHelper helper2 = new UnmergeEnterpriseObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand = {"euid=" + destEUID, "calcOnly=" + calcOnly}; 
        mr = helper2.run(unmergeCommand);
        log("Unmerge completed.");        
        
        //Test assertions
        
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        try {
            TestSuite ts = null;
            if (args.length > 0) {
                ts = new TestSuite();
                for (int i = 0; i < args.length; i++) {
                    ts.addTest(new MergeEnterpriseObject(args[i]));
                }
            } else {
                ts = new TestSuite(MergeEnterpriseObject.class);
            }
            junit.textui.TestRunner.run(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
