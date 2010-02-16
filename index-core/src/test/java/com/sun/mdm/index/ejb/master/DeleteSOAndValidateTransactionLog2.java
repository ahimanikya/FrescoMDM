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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.DeleteSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class DeleteSOAndValidateTransactionLog2 extends TestCase {
    
    private MasterController mc;
        
    private static final String SYSTEM = "SiteA";
    private static final String LID = "0001";
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeleteSOAndValidateTransactionLog2(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests delete system object
     * @throws Exception an error occured
     */
    public void testDeleteSystemObject() throws Exception {
        //Insert Data
    	// EO will contain one SystemObject. SystemObject will be deleted and TransactionLog
    	// Will be validated
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        String[] command = {"fileName=DeleteSystemObject2.txt", "fileType=generic"};
        List eoList = createHelper.run(command);
        EnterpriseObject eo = (EnterpriseObject) eoList.get(0);
        Collection c = eo.getSystemObjects();
        assertTrue(c.size() == 1);
        
        //Delete SO
        DeleteSystemObjectHelper helper = new DeleteSystemObjectHelper();
        command = new String[] {"systemCode=" + SYSTEM, "lid=" + LID}; 
        helper.run(command);
        log("Delete completed.");
                
        //Test assertions
        log("Testing assertions.");
        MasterController mc0 = MCFactory.getMasterController();
        EnterpriseObject eo2 = mc0.getEnterpriseObject(eo.getEUID());
        // eo should be null
        assertTrue(eo2 == null);
        
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
        
        // Simulate GUI to reference the major object of the afterEO for every transaction summary.
        for (int i = 0; i < summary.length; i++) {
    		EnterpriseObject afterEO = summary[i].getEnterpriseObjectHistory().getAfterEO();
    		EnterpriseObject afterEO2 = summary[i].getEnterpriseObjectHistory().getAfterEO2();
    		EnterpriseObject beforeEO1 = summary[i].getEnterpriseObjectHistory().getBeforeEO1();
    		EnterpriseObject beforeEO2 = summary[i].getEnterpriseObjectHistory().getBeforeEO2();
    		
        	if ( i == 0 ) {         		
                assertTrue(afterEO != null );
                assertTrue(afterEO2 == null );
                assertTrue(beforeEO1 == null );
                assertTrue(beforeEO2 == null );                
        	}
        	if ( i == 1 ) {         		
                assertTrue(afterEO == null );
                assertTrue(afterEO2 == null );
                assertTrue(beforeEO1 != null );
                assertTrue(beforeEO2 == null );                
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
        junit.textui.TestRunner.run(new TestSuite(DeleteSOAndValidateTransactionLog.class));
    }
    
}
