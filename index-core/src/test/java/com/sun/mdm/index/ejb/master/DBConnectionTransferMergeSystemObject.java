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
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.BasicHelper;
import com.sun.mdm.index.ejb.master.helper.AddRecordHelper2;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.objects.SystemObjectPK;

/** Test class
 *
 * The UpdateManagerTest class also has some Junit tests.  However, those
 * use the UpdateManager whereas these tests use the MasterController, which
 * may recalculate potential duplicates.
 *
 * @author rtam
 */
public class DBConnectionTransferMergeSystemObject extends TestCase {
    
    private static MasterController mMaster;
    
    /** Creates new tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DBConnectionTransferMergeSystemObject(String name) {
        super(name);
    }
        
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
        mMaster = MCFactory.getMasterController();
    }
    

    /**
     * Transfer a system object from one EO to another.  It should start with 
     * one potential duplicate.  After the transfer, the source EO should be
     * deactivated, and there should be no potential duplicates.
     *
     */
    public void testDbConnectionTransferSystem1() throws Exception {
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER0);
        
        String sysCode = addRecordHelper2.getSystem();
        String lid = addRecordHelper2.getLid0();
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be exactly 1 potential duplicate
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 1);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysCode, lid);
            mMaster.transferSystemObject(euid2, systemKey);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo2.getSystemObject(sysCode, lid) != null);
        // TODO: check if the following comment from UpdateManagerTest still applies.
        //Bug - trans mgr is removing SBR rather than changing status to inactive
        //assertTrue(eo1.getSBR().getStatus().equals(SystemObject.STATUS_INACTIVE));
        
        // There should be no potential duplicates
        potDupHelper = new LookupPotentialDuplicatesHelper();     
        i = potDupHelper.run(new String[0]);
        count = i.count();
        assertTrue(count == 0);
       
    }
     
    /**
     * Transfer a system object from one EO to another.  It should start with 
     * one potential duplicate.  After the transfer, the source EO should NOT 
     * be deactivated, and there should be no potential duplicates.
     */
    public void testDbConnectionTransferSystem2() throws Exception {
        // Add test records one at a time.  Otherwise, the two
        // system objects in the first EO might have the same
        // create/update times.  This would mess up the SBR calculation
        // because eIndex uses the MostRecentModified strategy.
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER1);
        
        String sysCode = addRecordHelper2.getSystem();
        String lid = addRecordHelper2.getLid0();
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be exactly 1 potential duplicate
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 1);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysCode, lid);
            mMaster.transferSystemObject(euid2, systemKey);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode, lid) == null);
        assertTrue(eo2.getSystemObject(sysCode, lid) != null);
        
        // There should be no potential duplicates
        i = potDupHelper.run(new String[0]);               
        count = i.count();
        assertTrue(count == 0);
        
    }
    
    /**
     * Transfer a system object from one EO to another.  It should start with 
     * NO potential duplicates.  After the transfer, the source EO should NOT 
     * be deactivated, and there should be one potential duplicate.
     */
    public void testDbConnectionTransferSystem3() throws Exception {
        // Add test records one at a time.  Otherwise, the two
        // system objects in the first EO might have the same
        // create/update times.  This would mess up the SBR calculation
        // because eIndex uses the MostRecentModified strategy.
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER2);
        
        String sysCode = addRecordHelper2.getSystem();
        String lid = addRecordHelper2.getLid1();
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be no potential duplicates
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 0);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysCode, lid);
            mMaster.transferSystemObject(euid2, systemKey);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode, lid) == null);
        assertTrue(eo2.getSystemObject(sysCode, lid) != null);
        
        // There should be exactly 1 potential duplicate
        i = potDupHelper.run(new String[0]);               
        count = i.count();
        assertTrue(count == 1);
        
    }
    
    /**
     * Merge a system object from one EO to another.  It should start with 
     * one potential duplicate.  After the transfer, the source EO should be
     * deactivated, and there should be no potential duplicates.
     *
     */
    public void testDbConnectionMergeSystem1() throws Exception {
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER0);
        
        String sysCode = addRecordHelper2.getSystem();
        String lidSource = addRecordHelper2.getLid0();
        String lidDest = addRecordHelper2.getLid2();
        
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be exactly 1 potential duplicate
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 1);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            mMaster.mergeSystemObject(sysCode, lidSource, lidDest, false);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1 == null);
        assertTrue(eo2.getSystemObject(sysCode, lidSource) != null);
        // TODO: check if the following comment from UpdateManagerTest still applies.
        //Bug - trans mgr is removing SBR rather than changing status to inactive
        //assertTrue(eo1.getSBR().getStatus().equals(SystemObject.STATUS_INACTIVE));
        
        // There should be no potential duplicates
        potDupHelper = new LookupPotentialDuplicatesHelper();     
        i = potDupHelper.run(new String[0]);
        count = i.count();
        assertTrue(count == 0);
       
    }
     
    /**
     * Merge a system object from one EO to another.  It should start with 
     * one potential duplicate.  After the transfer, the source EO should NOT 
     * be deactivated, and there should be no potential duplicates.
     */
    public void testDbConnectionMergeSystem2() throws Exception {
        // Add test records one at a time.  Otherwise, the two
        // system objects in the first EO might have the same
        // create/update times.  This would mess up the SBR calculation
        // because eIndex uses the MostRecentModified strategy.
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER1);
        
        String sysCode = addRecordHelper2.getSystem();
        String lidSource = addRecordHelper2.getLid0();
        String lidDest = addRecordHelper2.getLid2();
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be exactly 1 potential duplicate
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 1);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysCode, lidSource);
            mMaster.mergeSystemObject(sysCode, lidSource, lidDest, false);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode, lidSource) == null);
        assertTrue(eo2.getSystemObject(sysCode, lidSource) != null);
        
        // There should be no potential duplicates
        i = potDupHelper.run(new String[0]);               
        count = i.count();
        assertTrue(count == 0);
        
    }
    
    /**
     * Merge a system object from one EO to another.  It should start with 
     * NO potential duplicates.  After the transfer, the source EO should NOT 
     * be deactivated, and there should be one potential duplicate.
     */
    public void testDbConnectionMergeSystem3() throws Exception {
        // Add test records one at a time.  Otherwise, the two
        // system objects in the first EO might have the same
        // create/update times.  This would mess up the SBR calculation
        // because eIndex uses the MostRecentModified strategy.
        AddRecordHelper2 addRecordHelper2 = new AddRecordHelper2();
        addRecordHelper2.clearDb();
        boolean rc = addRecordHelper2.run(null, null, null, null, addRecordHelper2.TRANSFER_TEST_ORDER2);
        
        String sysCode = addRecordHelper2.getSystem();
        String lidSource = addRecordHelper2.getLid1();
        String lidDest = addRecordHelper2.getLid2();
        String euid1 = addRecordHelper2.getEuid1();
        String euid2 = addRecordHelper2.getEuid2();
        
        // There should be no potential duplicates
        LookupPotentialDuplicatesHelper potDupHelper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = potDupHelper.run(new String[0]);
        int count = i.count();
        assertTrue(count == 0);
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        
        try {
            SystemObjectPK systemKey = new SystemObjectPK(sysCode, lidSource);
            mMaster.mergeSystemObject(sysCode, lidSource, lidDest, false);
        } catch (Exception e) {
            throw e;
        }

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode, lidSource) == null);
        assertTrue(eo2.getSystemObject(sysCode, lidSource) != null);
        
        // There should be exactly 1 potential duplicate
        i = potDupHelper.run(new String[0]);               
        count = i.count();
        assertTrue(count == 1);
        
    }
    
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DBConnectionTransferMergeSystemObject.class));
    }
}
