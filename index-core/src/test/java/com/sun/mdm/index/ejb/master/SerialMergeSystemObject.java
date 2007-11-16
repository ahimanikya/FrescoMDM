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
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.UnmergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import java.util.List;

/** Test class for serially merging and unmerging two system objects as defined in the
 * eIndex50.xml file.  This simulates a portion of an EDM-initiated operation because
 * it checks the results of the merge (q.v. EOViewEditForm:performUnmerge()).
 * @author rtam
 */
public class SerialMergeSystemObject extends TestCase {
    
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    private String mEuid1;
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public SerialMergeSystemObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests serial merges of EO1/SO1--> EO2/SO2-->EO3/SO3
     * @throws Exception an error occured
     */
    public void testSerialMergeSystemObject() throws Exception {    
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Merge the records (cross EUID merge)
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("*** Serial Merge and Unmerge Test ***");
        String[] mergeCommand1 = {"systemCode=SiteA", "lidSource=0001", "lidDest=0003", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand1);
        log("Merge 1 completed.");
        String[] mergeCommand2 = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand2);
        log("Merge 2 completed.");
        
        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = eoHelper.run(new String[] {"system=SiteA", "lid=0005"});
        
        // soSource1 should no longer be accessible because it has been
        // merged into soSource2.
        SystemObject soSource1 = eo.getSystemObject("SiteA", "0001");
        SystemObject soSource2 = eo.getSystemObject("SiteA", "0003");
        SystemObject soDest = eo.getSystemObject("SiteA", "0005");
        assertTrue(soSource1 == null);
        assertTrue(soSource2.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
        //Unmerge the records
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand1 = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        MergeResult mr1 = unmergeHelper.run(unmergeCommand1);
        log("Unmerge1 completed.");
        String[] unmergeCommand2 = {"systemCode=SiteA", "lidSource=0001", "lidDest=0003", "calcOnly=false"}; 
        MergeResult mr2 = unmergeHelper.run(unmergeCommand2);
        log("Unmerge2 completed.");

        //Test assertions
        eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo1 = eoHelper.run(new String[] {"system=SiteA", "lid=0005"});
        soDest = eo1.getSystemObject("SiteA", "0005");
        EnterpriseObject eo2 = eoHelper.run(new String[] {"system=SiteA", "lid=0003"});
        soSource2 = eo2.getSystemObject("SiteA", "0003");
        EnterpriseObject eo3 = eoHelper.run(new String[] {"system=SiteA", "lid=0001"});
        soSource1 = eo3.getSystemObject("SiteA", "0001");
        assertTrue(!eo1.getEUID().equals(eo2.getEUID()));
        assertTrue(soSource1.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soSource2.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));  

        // check transaction summary
        String transNo1 = mr1.getTransactionObject().getTransactionNumber();
        TransactionSummary xaSummary1 = unmergeHelper.getTransactionHistory(transNo1);        
        String transNo2 = mr2.getTransactionObject().getTransactionNumber();
        TransactionSummary xaSummary2 = unmergeHelper.getTransactionHistory(transNo2); 
        
        EnterpriseObjectHistory eoHistory = xaSummary1.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO1 = eoHistory.getBeforeEO1();
        EnterpriseObject beforeEO2 = eoHistory.getBeforeEO2();
        EnterpriseObject afterEO = eoHistory.getAfterEO();

        // test assertions for first unmerge
        assertTrue(!eo1.getEUID().equals(eo2.getEUID()));
        assertTrue(!eo2.getEUID().equals(eo3.getEUID()));
        
        eoHistory = xaSummary2.getEnterpriseObjectHistory();
        beforeEO1 = eoHistory.getBeforeEO1();
        beforeEO2 = eoHistory.getBeforeEO2();
        afterEO = eoHistory.getAfterEO();
        
        // test assertions for second unmerge
        assertTrue(!eo1.getEUID().equals(eo2.getEUID()));
        assertTrue(!eo2.getEUID().equals(eo3.getEUID()));
        
        log("*** Serial Merge and Unmerge Test Completed ***");
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
                ts.addTest(new MergeSystemObject(args[i]));
            }
        } else {
            ts = new TestSuite(SerialMergeSystemObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
