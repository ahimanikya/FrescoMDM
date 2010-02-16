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
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/* TMTest3: Test program for bug 806
 * CreateEO 1, CreateEO 2
 * EUID Merge 1->2
 * EUID Merge 2->3
 * EUID Unmerge all 
 * EUID Merge 3->2
 * EUID Merge 2->1
 * EUID Unmerge all
 * Lookup history
 */

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Sanjay.Sharma
 */
public class MergeEnterpriseObjectAndReverseMerge extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeEnterpriseObjectAndReverseMerge(String name) {
        super(name);
    }
    
    /** Tests an EUI merge and EUID Unmerge then search transaction
     * @throws Exception an error occured
     */
    public void testTMTest() throws Exception {
        // Create EO 1 and 2
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.clearDb();
        List eoList = helper.run(new String[] {"fileName=MergeEnterpriseObjectAndReverseMerge1.txt", "fileType=eiEvent"});
        EnterpriseObject eo1 = (EnterpriseObject) eoList.get(0);
        EnterpriseObject eo2 = (EnterpriseObject) eoList.get(1);
        EnterpriseObject eo3 = (EnterpriseObject) eoList.get(2);
           
        //Merge the records 1->2
        MergeEnterpriseObjectHelper mergeHelper = new MergeEnterpriseObjectHelper();
        String euid1 = eo1.getEUID();
        String euid2 = eo2.getEUID();
        String euid3 = eo3.getEUID();
        String calcOnly = "false";
        log("Merging records.  Source: " + euid1 + " Dest: " + euid2);
        String[] mergeCommand = {"euidSource=" + euid1, "euidDest=" + euid2, "calcOnly=" + calcOnly}; 
        MergeResult mr = mergeHelper.run(mergeCommand);
        log("Merge completed.");
        
        //Test assertions by checking history 
        log("Testing assertions for first EUID Merge.");   
        assertTrue(mr != null);
        TransactionObject tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        String transId = tobj.getTransactionNumber();
        String func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidMerge"));
        MasterController mc = MCFactory.getMasterController();
        TransactionSummary summary = mc.lookupTransaction(transId);
        EnterpriseObjectHistory rr = summary.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO1 = rr.getBeforeEO1();
        EnterpriseObject beforeEO2 = rr.getBeforeEO2();
        EnterpriseObject afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 != null);
        
        //Merge the records 2->3
        log("Merging records.  Source: " + euid2 + " Dest: " + euid3);
        String[] mergeCommand2 = {"euidSource=" + euid2, "euidDest=" + euid3, "calcOnly=" + calcOnly}; 
        mr = mergeHelper.run(mergeCommand2);
        log("Merge completed.");
        
        //Test assertions by checking history 
        log("Testing assertions for second EUID Merge.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidMerge"));
        mc = MCFactory.getMasterController();
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 != null);
        
        //Unmerge the record 2 from 3
        UnmergeEnterpriseObjectHelper unmergeHelper = new UnmergeEnterpriseObjectHelper();
        log("Unmerging records 2 from 3.");
        String[] unmergeCommand = {"euid=" + euid3, "calcOnly=" + calcOnly}; 
        mr = unmergeHelper.run(unmergeCommand);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions for EUID Unmerge 2 from 3.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidUnMerge"));
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);    // after EUID unmerge, afterEO should not be null
        assertTrue(beforeEO1 != null);  // after EUID unmerge, beforeEO1 should not be null
        assertTrue(beforeEO2 == null);  // after EUID unmerge, beforeEO2 should not be null
        
        log("Unmerging records 1 from 2.");
        String[] unmergeCommand2 = {"euid=" + euid2, "calcOnly=" + calcOnly}; 
        mr = unmergeHelper.run(unmergeCommand2);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions for EUID Unmerge 1 from 2.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidUnMerge"));
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);    // after EUID unmerge, afterEO should not be null
        assertTrue(beforeEO1 != null);  // after EUID unmerge, beforeEO1 should not be null
        assertTrue(beforeEO2 == null);  // after EUID unmerge, beforeEO2 should not be null
        
        //Merge the records 3->2
        log("Merging records.  Source: " + euid3 + " Dest: " + euid2);
        String[] mergeCommand3 = {"euidSource=" + euid3, "euidDest=" + euid2, "calcOnly=" + calcOnly}; 
        mr = mergeHelper.run(mergeCommand3);
        log("Merge completed.");
        
        //Test assertions by checking history 
        log("Testing assertions for first EUID Merge.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidMerge"));
       
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 != null);
        
        //Merge the records 2->1
        log("Merging records.  Source: " + euid2 + " Dest: " + euid1);
        String[] mergeCommand4 = {"euidSource=" + euid2, "euidDest=" + euid1, "calcOnly=" + calcOnly}; 
        mr = mergeHelper.run(mergeCommand4);
        log("Merge completed.");
        
        //Test assertions by checking history 
        log("Testing assertions for second EUID Merge.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidMerge"));
        mc = MCFactory.getMasterController();
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);
        assertTrue(beforeEO1 != null);
        assertTrue(beforeEO2 != null);
        
        //Unmerge the record 2 from 1
        log("Unmerging records 2 from 3.");
        String[] unmergeCommand3 = {"euid=" + euid1, "calcOnly=" + calcOnly}; 
        mr = unmergeHelper.run(unmergeCommand3);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions for EUID Unmerge 2 from 1.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidUnMerge"));
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);    // after EUID unmerge, afterEO should not be null
        assertTrue(beforeEO1 != null);  // after EUID unmerge, beforeEO1 should not be null
        assertTrue(beforeEO2 == null);  // after EUID unmerge, beforeEO2 should not be null
        
        log("Unmerging records 3 from 2.");
        String[] unmergeCommand4 = {"euid=" + euid2, "calcOnly=" + calcOnly}; 
        mr = unmergeHelper.run(unmergeCommand4);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions for EUID Unmerge 3 from 2.");   
        assertTrue(mr != null);
        tobj = mr.getTransactionObject();
        assertTrue(tobj != null);
        transId = tobj.getTransactionNumber();
        func = tobj.getFunction();
        assertTrue(func != null);
        assertTrue(func.equals("euidUnMerge"));
        summary = mc.lookupTransaction(transId);
        rr = summary.getEnterpriseObjectHistory();
        beforeEO1 = rr.getBeforeEO1();
        beforeEO2 = rr.getBeforeEO2();
        afterEO = rr.getAfterEO();
        assertTrue(afterEO != null);    // after EUID unmerge, afterEO should not be null
        assertTrue(beforeEO1 != null);  // after EUID unmerge, beforeEO1 should not be null
        assertTrue(beforeEO2 == null);  // after EUID unmerge, beforeEO2 should not be null
                
        lookupXA(20);
    }

    private void lookupXA(int pageSize) throws Exception {
		String tomorrow = getTomorrow();
        LookupTransactionsHelper lt = new LookupTransactionsHelper();
        TransactionIterator iterator = 
            lt.run(new String[] {"startDate=20000101000000", "endDate=" + tomorrow, "pageSize=" + pageSize});
        System.out.println("# of XA's found: " + iterator.count());
        int count = 0;
        Date latestDate = null;
        while (iterator.hasNext()) {
            TransactionSummary summary = iterator.next();
            Date nextDate = summary.getTransactionObject().getTimeStamp();
            if (latestDate == null || nextDate.after(latestDate)) {
                latestDate = nextDate;
            }
            count++;
        }
        TransactionSummary[] summary = iterator.absolute(0, count);
        int recordCount = iterator.count();
        
        //Now set the end date to be the last one in the set
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        latestDate.setSeconds(latestDate.getSeconds()+1);
        String dateCriteria = sdf.format(latestDate);
        iterator = lt.run(new String[] {"startDate=20000101000000", "endDate=" + dateCriteria});
        assertTrue(iterator.count() == recordCount);      
        
        // Simulate GUI to reference the major object of the afterEO for every transaction summary.
        for (int i = 0; i < summary.length; i++) {
            EnterpriseObject eo = summary[i].getEnterpriseObjectHistory().getAfterEO();
            ObjectNode obj = eo.getSBR().getObject();
            assertTrue(obj != null);
        }
    }
    

    private void log(String msg) {
        System.out.println(msg);
    }

	private String getTomorrow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,1);
		return sdf.format(cal.getTime());
	}
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MergeEnterpriseObjectAndReverseMerge.class));
    }
    
}
