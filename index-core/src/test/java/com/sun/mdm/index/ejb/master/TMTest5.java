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
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Date;
import java.text.SimpleDateFormat;

/* TMTest5: Test program for bug 3645
 * Problem 1: After EUID merge, the SBR person value(s) of the transaction's before EO have been changed.
 * Problem 2: After EUID unmerge, the SBR person of the survivor was not restore to its original state.
 * Process:
 * CreateEO 1, 2, 3, 4
 * EUID Merge 1->2
 * Lookup history
 * Compare the images before merge and the SBR.person of the beforeEO of the transaction
 */

/** 
 * @author Jeff Lin
 */
public class TMTest5 extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public TMTest5(String name) {
        super(name);
    }
    
    /** Tests an EUID merge then search transaction to check the beforeEO is changed.
     * @throws Exception an error occured
     */
    public void testTMTest5() throws Exception {
        log("******** Beginning ********");
        // Create EO 1, 2, 3, 4
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.clearDb();
        List eoList = helper.run(new String[] {"fileName=LookupTransactions2.txt", "fileType=eiEvent"});
        EnterpriseObject eo1 = (EnterpriseObject) eoList.get(0);
        EnterpriseObject eo2 = (EnterpriseObject) eoList.get(1);
           
        //Merge the records 1->2
        MergeEnterpriseObjectHelper mergeHelper = new MergeEnterpriseObjectHelper();
        String euid1 = eo1.getEUID();
        String euid2 = eo2.getEUID();
        log("Merging records.  Source: " + euid1 + " Dest: " + euid2);
        
        String calcOnly = "true";
        String[] mergeCommand = {"euidSource=" + euid1, "euidDest=" + euid2, "calcOnly=" + calcOnly}; 
        MergeResult mr = mergeHelper.run(mergeCommand);
        
        calcOnly = "false";
        String[] mergeCommand2 = {"euidSource=" + euid1, "euidDest=" + euid2, "calcOnly=" + calcOnly}; 
        MergeResult mr2 = mergeHelper.run(mergeCommand2);
        log("Merge completed.");
        
        //Test assertions by checking history 
        log("Testing assertions for first EUID Merge.");   
        assertTrue(mr2 != null);
        log("Check mr2 != null.");   
        TransactionObject tobj = mr2.getTransactionObject();
        assertTrue(tobj != null);
        log("Check tobj != null?");   
        String transId = tobj.getTransactionNumber();
        String func = tobj.getFunction();
        assertTrue(func != null);
        log("Check func != null?");   
        assertTrue(func.equals("euidMerge"));
        log("Check func = euidMerge?");   
        MasterController mc = MCFactory.getMasterController();
        TransactionSummary summary = mc.lookupTransaction(transId);
        EnterpriseObjectHistory rr = summary.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO1 = rr.getBeforeEO1();
        EnterpriseObject beforeEO2 = rr.getBeforeEO2();
        EnterpriseObject afterEO = rr.getAfterEO();
        log("Check after EO is null?");   
        assertTrue(afterEO != null);
        log("Check before EO1 is null?");   
        assertTrue(beforeEO1 != null);
        log("Check before EO2 is null?");   
        assertTrue(beforeEO2 != null);
        log("Testing assertions for first EUID Merge ..... completed.");   
        
        // compare the person object value of the SBR of the original EO and transaction's beforeEO
        ObjectNode originalObj = eo2.getSBR().getObject();
        ObjectNode beforeObj = beforeEO1.getSBR().getObject();
        ObjectField[] fields = originalObj.pGetFields();
        
        System.out.println("\n\n**** The error is original SBR person" +
                            " and transaction's before EO not same ****\n");
        
        System.out.println("**** Both original SBR person and " + 
                            "Merge transaction's before EO sould be same ****");
        
        for (int i = 0; i < fields.length; i++) {
            Object orgValue = originalObj.getValue(fields[i].getName());
            Object beforeValue = beforeObj.getValue(fields[i].getName());
            System.out.println("Field=" + fields[i].getName() +
                            " original and before =" + orgValue + " " + beforeValue);
        }
        

        System.out.println("**** Both Original and Before merge value" + 
                            "might be different and stop ****");
        for (int i = 0; i < fields.length; i++) {
            Object orgValue = originalObj.getValue(fields[i].getName());
            Object beforeValue = beforeObj.getValue(fields[i].getName());
            System.out.println("Field=" + fields[i].getName() +
                            " original and before =" + orgValue + " " + beforeValue);
            assertTrue((orgValue == null && beforeValue == null) || orgValue.equals(beforeValue));
        }

        
        /*
         * Starting Unmerge
         */
        //Unmerge the record 1 from 2
        UnmergeEnterpriseObjectHelper unmergeHelper = new UnmergeEnterpriseObjectHelper();
        log("\n\nUnmerging records 1 from 2.");
        calcOnly = "true";
        String[] unmergeCommand1 = {"euid=" + euid2, "calcOnly=" + calcOnly}; 
        mr = unmergeHelper.run(unmergeCommand1);
        calcOnly = "false";
        String[] unmergeCommand2 = {"euid=" + euid2, "calcOnly=" + calcOnly}; 
        mr2 = unmergeHelper.run(unmergeCommand2);
        log("Unmerge completed.");        
        
        //Test assertions
        log("Testing assertions for EUID Unmerge 1 from 2.");   
        assertTrue(mr2 != null);
        tobj = mr2.getTransactionObject();
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
        
        ObjectNode afterObj = afterEO.getSBR().getObject();
        
        System.out.println("\n\n**** The error is original SBR person" +
                            " and Unmerge transaction's after EO not same ****\n");
        
        System.out.println("**** Both original SBR person and " + 
                            "transaction's before EO should be same ****");
        
        for (int i = 0; i < fields.length; i++) {
            Object orgValue = originalObj.getValue(fields[i].getName());
            Object afterValue = afterObj.getValue(fields[i].getName());
            System.out.println("Field=" + fields[i].getName() +
                            " original and after =" + orgValue + " " + afterValue);
        }
        
       System.out.println("\n**** Both Original and after merge value" + 
                            "might be different and stop ****");
        for (int i = 0; i < fields.length; i++) {
            Object orgValue = originalObj.getValue(fields[i].getName());
            Object afterValue = afterObj.getValue(fields[i].getName());
            System.out.println("Field=" + fields[i].getName() +
                            " original and after =" + orgValue + " " + afterValue);
            assertTrue((orgValue == null && afterValue == null) || orgValue.equals(afterValue));
        }
        
        
            
    }

    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TMTest5.class));
    }
    
}
