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
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionHelper;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;

/** Test class for transaction lookup
 * @author Dan Cidon
 */
public class LookupTransaction extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupTransaction(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests transaction lookup
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        ExecuteMatchHelper matchHelper = new ExecuteMatchHelper();
        String[] command = {"fileName=LookupTransaction1.txt", "fileType=generic"};
        List list = matchHelper.run(command);
        LookupTransactionHelper helper = new LookupTransactionHelper();
        //Get the update transaction
        MatchResult mr = (MatchResult) list.get(1);
        String transactionId = mr.getTransactionNumber(); 
        command = new String[] {"id=" + transactionId}; 
        TransactionSummary transSummary = helper.run(command);
        System.out.println("TransactionSummary:");
        System.out.println(transSummary.toString());
        
        //Test assertions
        log("Testing assertions.");  
        TransactionObject transObj = transSummary.getTransactionObject();
        assertTrue(transObj.getTransactionNumber().equals(transactionId));
        assertTrue(transObj.getFunction().equals("Update"));
        EnterpriseObjectHistory rr = transSummary.getEnterpriseObjectHistory();
        EnterpriseObject beforeEO = rr.getBeforeEO1();
        EnterpriseObject afterEO = rr.getAfterEO();
        //Check SBR
        PersonObject beforePO = (PersonObject) beforeEO.getSBR().getObject();
        PersonObject afterPO = (PersonObject) afterEO.getSBR().getObject();
        assertTrue(beforePO.getLastName().equals("CIDON"));
        assertTrue(afterPO.getLastName().equals("CIDONE"));
        assertTrue(beforePO.getMStatus() == null);
        assertTrue(afterPO.getMStatus().equals("S"));
        //Check SO
        beforePO = (PersonObject) beforeEO.getSystemObject("SiteA", "0001").getObject();
        afterPO = (PersonObject) afterEO.getSystemObject("SiteA", "0001").getObject();
        assertTrue(beforePO.getLastName().equals("CIDON"));
        assertTrue(afterPO.getLastName().equals("CIDONE"));
        assertTrue(beforePO.getMStatus() == null);
        assertTrue(afterPO.getMStatus().equals("S"));
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LookupTransaction.class));
    }
    
}
