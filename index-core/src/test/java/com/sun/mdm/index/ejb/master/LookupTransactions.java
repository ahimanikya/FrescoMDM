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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.math.BigInteger;

import java.util.List;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class LookupTransactions extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupTransactions(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
       ClearDb.run();
    }
    
    /** Tests lookup when PageData EJB not used
     * @throws Exception an error occured
     */
    public void testLookupTransactions1() throws Exception {
        executeTest(20);
    }

    /** Tests lookup when PageData EJB is used
     * @throws Exception an error occured
     */
    public void testLookupTransactions2() throws Exception {
        executeTest(2);
    }    
    
    private void executeTest(int pageSize) throws Exception {
        int recordCount = 10;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String tomorrow = getTomorrow();

        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        List eoList = createHelper.run(new String[] {"fileName=LookupTransactions1.txt", "fileType=eiEvent"});
        LookupTransactionsHelper lt = new LookupTransactionsHelper();
        TransactionIterator iterator = 
            lt.run(new String[] {"startDate=20000101000000", "endDate=" + tomorrow, "pageSize=" + pageSize});
		assertTrue(iterator != null);
        assertTrue(iterator.count() == recordCount);
        int count = 0;
        Date latestDate = null;
        TransactionSummary summary = null;
        Date nextDate = null;
        while (iterator.hasNext()) {
            summary = iterator.next();
            nextDate = summary.getTransactionObject().getTimeStamp();
            if (latestDate == null || nextDate.after(latestDate)) {
                latestDate = nextDate;
            }
            count++;
        }
        assertTrue(count == recordCount);
        TransactionSummary[] summaryArray = iterator.absolute(0, count);
        assertTrue(summaryArray.length == recordCount);
        
        //Now set the end date to be the last one in the set
        latestDate.setSeconds(latestDate.getSeconds()+1);
        String dateCriteria = sdf.format(latestDate);
        iterator = lt.run(new String[] {"startDate=20000101000000", "endDate=" + dateCriteria});
        assertTrue(iterator.count() == recordCount);      
        
        //  run another test
        iterator 
                = lt.run2(new String[] {"startDate=20000101000000", "endDate=" + tomorrow, "pageSize=" + pageSize});
        assertTrue(iterator.count() == recordCount);
        count = 0;
        latestDate = null;
        while (iterator.hasNext()) {
            summary = iterator.next();
            nextDate = summary.getTransactionObject().getTimeStamp();
            if (latestDate == null || nextDate.after(latestDate)) {
                latestDate = nextDate;
            }
            count++;
        }        
        
        
        //  run another test
        iterator 
                = lt.run3(new String[] {"startDate=20000101000000", "endDate=" + tomorrow, "pageSize=" + pageSize});
        assertTrue(iterator.count() == recordCount);
        count = 0;
        latestDate = null;
        while (iterator.hasNext()) {
            summary = iterator.next();
            nextDate = summary.getTransactionObject().getTimeStamp();
            if (latestDate == null || nextDate.after(latestDate)) {
                latestDate = nextDate;
            }
            count++;
        }        

        //  check support methods
        assertTrue(lt.runSupportMethods());
        
        
        
        //The following sorting tests are repeated for bug report scenario
        //regarding repeated sorting.
        for (int i = 0; i < 2; i++) {
            //Sort forward
            iterator.sortBy("EUID", false);
            BigInteger euid = new BigInteger(iterator.first().getTransactionObject().getEUID());
            BigInteger one = new BigInteger("1");
            while (iterator.hasNext()) {
                euid = euid.add(one);
                BigInteger nextEuid = new BigInteger(iterator.next().getTransactionObject().getEUID());
                assertTrue(nextEuid.equals(euid));
            }

            //Sort backward
            iterator.sortBy("EUID", true);
            euid = new BigInteger(iterator.first().getTransactionObject().getEUID());
            while (iterator.hasNext()) {
                euid = euid.subtract(one);
                BigInteger nextEuid = new BigInteger(iterator.next().getTransactionObject().getEUID());
                assertTrue(nextEuid.equals(euid));
            }        
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
        junit.textui.TestRunner.run(new TestSuite(LookupTransactions.class));
    }
    
}
