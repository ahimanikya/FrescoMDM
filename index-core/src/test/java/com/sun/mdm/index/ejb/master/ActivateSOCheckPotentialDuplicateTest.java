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
public class ActivateSOCheckPotentialDuplicateTest extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public ActivateSOCheckPotentialDuplicateTest(String name) {
        super(name);
    }
    
    /** Tests an EUI merge and EUID Unmerge then search transaction
     * @throws Exception an error occured
     */
    public void testOne() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=ActivateSOCheckPotentialDuplicateTest1.txt", "fileType=generic"});

        executeMatchHelper.run(new String[] {"fileName=ActivateSOCheckPotentialDuplicateTest2.txt", "fileType=generic"});

        MasterController mc = MCFactory.getMasterController();
        
        //Make sure there is one Potential Duplicate
        SystemObjectPK sysObjectPK = new SystemObjectPK("SiteA", "0002");
        
        EnterpriseObject eo = mc.getEnterpriseObject(sysObjectPK);

        PotentialDuplicateSearchObject so = new PotentialDuplicateSearchObject();
        so.setEUID(eo.getEUID());
        
        PotentialDuplicateIterator itr = mc.lookupPotentialDuplicates(so);
        int count  = itr.count();

        assertEquals( count, 1 );
        
        // Now deactivate the EO and see the potential duplicate count again. It should be zero.
        mc.deactivateSystemObject(sysObjectPK); 
        		
        itr = mc.lookupPotentialDuplicates(so);
        count  = itr.count();

        assertEquals( count, 0 );

        // Now activate the SO and see the potential duplicate count again. It should be zero as EO is still deactivated.
        mc.activateSystemObject(sysObjectPK);
        itr = mc.lookupPotentialDuplicates(so);
        count  = itr.count();

        assertEquals( count, 0 );        
        
        // Now activate the EO and see the potential duplicate count again. It should be one.
        mc.activateEnterpriseObject(eo.getEUID());
        itr = mc.lookupPotentialDuplicates(so);
        count  = itr.count();

        assertEquals( count, 1 );
        
        System.out.println("Done");
        
    }

    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ActivateSOCheckPotentialDuplicateTest.class));
    }
    
}
