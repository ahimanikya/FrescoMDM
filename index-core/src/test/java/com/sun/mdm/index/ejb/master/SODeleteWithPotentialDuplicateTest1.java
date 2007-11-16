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

import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Sanjay.Sharma
 */
public class SODeleteWithPotentialDuplicateTest1 extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public SODeleteWithPotentialDuplicateTest1(String name) {
        super(name);
    }
    
    /** Tests an EUI merge and EUID Unmerge then search transaction
     * @throws Exception an error occured
     */
    public void testOne() throws Exception {
    	
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.clearDb();
        createHelper.run(new String[] {"fileName=LookupPotentialDuplicates1.txt", "fileType=eiEvent"});
        LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper.run(new String[0]);    	
    	
        MasterController mc = MCFactory.getMasterController();
        
   		SystemObjectPK soPk = new SystemObjectPK("SiteA","0001");
   		mc.deleteSystemObject(soPk);

        System.out.println("Test");
        
        /*
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.clearDb();
        createHelper.run(new String[] {"fileName=SODeleteWithPotentialDuplicateTest1.txt", "fileType=generic"});
        createHelper.run(new String[] {"fileName=SODeleteWithPotentialDuplicateTest2.txt", "fileType=generic"});
        
        MasterController controller = MCFactory.getMasterController();
        
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        
        //Create Another Enterprise Object that will have result in potential Deuplicate
        EnterpriseObject eo2 = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0002"));

        String [] euIds = new String[] { eo.getEUID() , eo2.getEUID() };
        
        // Check if there is any potential Duplicate 
        PotentialDuplicateSearchObject potDupSearchObject = new PotentialDuplicateSearchObject();
        potDupSearchObject.setEUIDs(euIds);

        PotentialDuplicateIterator itr = controller.lookupPotentialDuplicates(potDupSearchObject);
        
        PotentialDuplicateSummary pdSumm = null;
        
        while ( itr.hasNext() ) {
        	pdSumm = (PotentialDuplicateSummary)itr.next();
        	System.out.println(pdSumm);
        }
        */
    	System.out.println("Done");
    }

    private void lookupXA(int pageSize) throws Exception {
        LookupTransactionsHelper lt = new LookupTransactionsHelper();
        TransactionIterator iterator = 
            lt.run(new String[] {"startDate=20000101000000", "endDate=20100101000000", "pageSize=" + pageSize});
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
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
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(SODeleteWithPotentialDuplicateTest1.class));
    }
    
}
