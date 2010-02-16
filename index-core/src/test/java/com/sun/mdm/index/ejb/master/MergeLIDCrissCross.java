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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.MergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;

import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/* 
 * CreateEO 1 with 2 SO, CreateEO 2 with SO
 * LID Merge 1.LID1->2.LID2
 * LID Merge 2.LID1->1.LID2
 * Unmerge All
 * Lookup history
 */

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Sanjay.Sharma
 */
public class MergeLIDCrissCross extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeLIDCrissCross(String name) {
        super(name);
    }
    
    /** Tests an LID merge and LID Unmerge
     * @throws Exception an error occured
     */
    public void testTMTest() throws Exception {
    	//EnterpriseObjectWithTwoSO1.txt
    	
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        
        createHelper.clearDb();
        
        createHelper.run(new String[] {"fileName=EnterpriseObjectWithTwoSO1.txt", "fileType=generic"});

        createHelper.run(new String[] {"fileName=EnterpriseObjectWithTwoSO2.txt", "fileType=generic"});

        MasterController controller = MCFactory.getMasterController();

        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        EnterpriseObject eo2 = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0003"));
    	           
        // LID merge 0001-> 0003
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("Merging records.");
        String[] mergeCommand = {"systemCode=SiteA", "lidSource=0001", "lidDest=0003", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand);
        
        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        
        eo = eoHelper.run(new String[] {"system=SiteA", "lid=0003"});
        
        SystemObject soSource = eo.getSystemObject("SiteA", "0001");
        SystemObject soDest = eo.getSystemObject("SiteA", "0003");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));     
        log("LID Merge completed.");

        // LID merge 0004-> 0002
        mergeHelper = new MergeSystemObjectHelper();
        log("Merging records.");
        String[] mergeCommand2 = {"systemCode=SiteA", "lidSource=0004", "lidDest=0002", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand2);
        
        eo = eoHelper.run(new String[] {"system=SiteA", "lid=0002"});
        
        soSource = eo.getSystemObject("SiteA", "0004");
        soDest = eo.getSystemObject("SiteA", "0002");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));     
        log("LID Merge completed.");

        eo = eoHelper.run(new String[] {"system=SiteA", "lid=0002"});
        eo2 = eoHelper.run(new String[] {"system=SiteA", "lid=0003"});

        
        
        // LID Unmerge 2 (call unmergeSystemObject twice with flag true and false like GUI)
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        
        String[] unmergeCommand = {"systemCode=SiteA", "lidSource=0004", "lidDest=0002", "calcOnly=true"}; 
        unmergeHelper.run(unmergeCommand);
        
        log("Unmerging records.");
        String[] unmergeCommand2 = {"systemCode=SiteA", "lidSource=0004", "lidDest=0002", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand2);
        
        //Test assertions
        GetEnterpriseObjectHelper helper2 = new GetEnterpriseObjectHelper();
        eo = helper2.run(new String[] {"system=SiteA", "lid=0002"});
        eo2 = helper2.run(new String[] {"system=SiteA", "lid=0004"});
         
        soSource = eo.getSystemObject("SiteA", "0002");
        soDest = eo2.getSystemObject("SiteA", "0004");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));

        // LID Unmerge 2 (call unmergeSystemObject twice with flag true and false like GUI)
        UnmergeSystemObjectHelper unmergeHelper2 = new UnmergeSystemObjectHelper();
        
        String[] unmergeCommand3 = {"systemCode=SiteA", "lidSource=0001", "lidDest=0003", "calcOnly=true"}; 
        unmergeHelper.run(unmergeCommand3);
        
        log("Unmerging records.");
        String[] unmergeCommand4 = {"systemCode=SiteA", "lidSource=0001", "lidDest=0003", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand4);
        
        //Test assertions
        GetEnterpriseObjectHelper helper3 = new GetEnterpriseObjectHelper();
        eo = helper3.run(new String[] {"system=SiteA", "lid=0001"});
        eo2 = helper3.run(new String[] {"system=SiteA", "lid=0003"});
         
        soSource = eo.getSystemObject("SiteA", "0001");
        soDest = eo2.getSystemObject("SiteA", "0003");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));
        
        
        log("LID Unmerge completed.");
        
        lookupXA(10);
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
        junit.textui.TestRunner.run(new TestSuite(MergeLIDCrissCross.class));
    }
    
}
