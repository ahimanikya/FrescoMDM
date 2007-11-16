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
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.ejb.master.helper.AddRecordHelper4;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.objects.SystemObjectPK;
import java.util.List;

/** Test class for deferred pessimistic mode system merges
 */
public class DeferredPessimisticModeSystemMerge extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    private String mEuid1;
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeferredPessimisticModeSystemMerge(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
   

    /** Tests an SO merge with cross EUID.  Pessimistic mode processing is deferred.
     * @throws Exception an error occured
     */
     
    public void testMergeSystemObjectDeferred1() throws Exception {    
        
        AddRecordHelper4 addRecordHelper = new AddRecordHelper4();
        addRecordHelper.clearDb();
        boolean rc = addRecordHelper.run(null, null, null, null, null);
        if (!rc)  {
            fail("Test failed: addRecordHelper.run() returned false");
        }

        String lid0 = addRecordHelper.getLid0();
        String lid1 = addRecordHelper.getLid1();
        String lid2 = addRecordHelper.getLid2();
        String system = addRecordHelper.getSystem();
        
        MasterController mc = MCFactory.getMasterController();
        
        SystemObjectPK srcSysObjectPK = new SystemObjectPK(system, lid1);
        EnterpriseObject mEntObjSource = mc.getEnterpriseObject(srcSysObjectPK);
        SystemObjectPK destSysObjectPK = new SystemObjectPK(system, lid2);
        EnterpriseObject mEntObjDest = mc.getEnterpriseObject(destSysObjectPK);
        
        SystemObject so2 = mEntObjDest.getSystemObject(system, lid2);
        PersonObject personObj = (PersonObject) so2.getObject();
        
        Integer oldSrcRevisionNumber = mEntObjSource.getSBR().getRevisionNumber();
        Integer oldDestRevisionNumber = mEntObjDest.getSBR().getRevisionNumber();

        // Merge the records (cross EUID merge)
        // Pessimistic mode is disabled
        

        MergeResult mr = mc.mergeSystemObject(system, 
                                              lid1, 
                                              lid2, 
                                              personObj, 
                                              oldSrcRevisionNumber.toString(),
                                              oldDestRevisionNumber.toString(),
                                              false, 
                                              new Boolean(false));

        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = eoHelper.run(new String[] {"system=" + system, "lid=" + lid2});
        SystemObject soSource = eo.getSystemObject(system, lid1);
        SystemObject soDest = eo.getSystemObject(system, lid2);
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
        // Confirm there is a still a potential duplicate
        SystemObjectPK sysObjectPK = new SystemObjectPK(system, lid2);
        eo = mc.getEnterpriseObject(sysObjectPK);
        PotentialDuplicateSearchObject so = new PotentialDuplicateSearchObject();
        so.setEUID(eo.getEUID());
        
        PotentialDuplicateIterator itr = mc.lookupPotentialDuplicates(so);
        int count  = itr.count();

        if (count == 1) {
            log("MergeSystemObjectDeferred[pessimistic mode disabled]: found one potential duplicate as expected");
        } else {
            fail("MergeSystemObjectDeferred[pessimistic mode disabled]: " + 
                "one potential duplicate expected but found " + count + " records");
        }
        
        // Pessimistic mode is enabled
        addRecordHelper.clearDb();
        rc = addRecordHelper.run(null, null, null, null, null);
        if (!rc)  {
            fail("Test failed: addRecordHelper.run() returned false");
        }

        lid0 = addRecordHelper.getLid0();
        lid1 = addRecordHelper.getLid1();
        lid2 = addRecordHelper.getLid2();
        system = addRecordHelper.getSystem();
                        
        // Merge the records (cross EUID merge)
        // Pessimistic mode is enabled
        
        srcSysObjectPK = new SystemObjectPK(system, lid1);
        mEntObjSource = mc.getEnterpriseObject(srcSysObjectPK);
        destSysObjectPK = new SystemObjectPK(system, lid2);
        mEntObjDest = mc.getEnterpriseObject(destSysObjectPK);
        
        so2 = mEntObjDest.getSystemObject(system, lid2);
        personObj = (PersonObject) so2.getObject();
        
        oldSrcRevisionNumber = mEntObjSource.getSBR().getRevisionNumber();
        oldDestRevisionNumber = mEntObjDest.getSBR().getRevisionNumber();
        
        mr = mc.mergeSystemObject(system, 
                                  lid1, 
                                  lid2, 
                                  personObj, 
                                  oldSrcRevisionNumber .toString(),
                                  oldDestRevisionNumber.toString(),
                                  false, 
                                  new Boolean(true));

        
        //Test assertions
        eo = eoHelper.run(new String[] {"system=" + system, "lid=" + lid2});
        soSource = eo.getSystemObject(system, lid1);
        soDest = eo.getSystemObject(system, lid2);
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
        // Confirm there are no more potential duplicates
        sysObjectPK = new SystemObjectPK(system, lid2);
        eo = mc.getEnterpriseObject(sysObjectPK);
        so.setEUID(eo.getEUID());
        
        itr = mc.lookupPotentialDuplicates(so);
        count  = itr.count();

        if (count == 0) {
            log("MergeSystemObjectDeferred[pessimistic mode ensabled]: found no potential duplicate as expected");
        } else {
            fail("MergeSystemObjectDeferred[pessimistic mode ensabled]: " + count +
                " potential duplicate(s) found but not expected");
        }
        
    }
    
    public void testMergeSystemObjectDeferred2() throws Exception {    
        
        AddRecordHelper4 addRecordHelper = new AddRecordHelper4();
        addRecordHelper.clearDb();
        boolean rc = addRecordHelper.run(null, null, null, null, null);
        if (!rc)  {
            fail("Test failed: addRecordHelper.run() returned false");
        }

        String lid0 = addRecordHelper.getLid0();
        String lid1 = addRecordHelper.getLid1();
        String lid2 = addRecordHelper.getLid2();
        String system = addRecordHelper.getSystem();
        
        MasterController mc = MCFactory.getMasterController();
        
        SystemObjectPK srcSysObjectPK = new SystemObjectPK(system, lid1);
        EnterpriseObject mEntObjSource = mc.getEnterpriseObject(srcSysObjectPK);
        SystemObjectPK destSysObjectPK = new SystemObjectPK(system, lid2);
        EnterpriseObject mEntObjDest = mc.getEnterpriseObject(destSysObjectPK);
        
        SystemObject so2 = mEntObjDest.getSystemObject(system, lid2);
        PersonObject personObj = (PersonObject) so2.getObject();
        
        Integer oldSrcRevisionNumber = mEntObjSource.getSBR().getRevisionNumber();
        Integer oldDestRevisionNumber = mEntObjDest.getSBR().getRevisionNumber();

        // Merge the records (cross EUID merge)
        // Pessimistic mode is disabled
        
        MergeResult mr = mc.mergeSystemObject(system, 
                                              lid1, 
                                              lid2, 
                                              personObj, 
                                              oldSrcRevisionNumber.toString(),
                                              oldDestRevisionNumber.toString(),
                                              false, 
                                              new Boolean(false));

        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = eoHelper.run(new String[] {"system=" + system, "lid=" + lid2});
        SystemObject soSource = eo.getSystemObject(system, lid1);
        SystemObject soDest = eo.getSystemObject(system, lid2);
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
        // Confirm there is a still a potential duplicate
        SystemObjectPK sysObjectPK = new SystemObjectPK(system, lid2);
        eo = mc.getEnterpriseObject(sysObjectPK);
        PotentialDuplicateSearchObject so = new PotentialDuplicateSearchObject();
        so.setEUID(eo.getEUID());
        
        PotentialDuplicateIterator itr = mc.lookupPotentialDuplicates(so);
        int count  = itr.count();

        if (count == 1) {
            log("MergeSystemObjectDeferred[pessimistic mode disabled]: found one potential duplicate as expected");
        } else {
            fail("MergeSystemObjectDeferred[pessimistic mode disabled]: " + 
                "one potential duplicate expected but found " + count + " records");
        }
        
        // Pessimistic mode is enabled
        addRecordHelper.clearDb();
        rc = addRecordHelper.run(null, null, null, null, null);
        if (!rc)  {
            fail("Test failed: addRecordHelper.run() returned false");
        }

        lid0 = addRecordHelper.getLid0();
        lid1 = addRecordHelper.getLid1();
        lid2 = addRecordHelper.getLid2();
        system = addRecordHelper.getSystem();
                        
        // Merge the records (cross EUID merge)
        // Pessimistic mode is enabled
        
        srcSysObjectPK = new SystemObjectPK(system, lid1);
        mEntObjSource = mc.getEnterpriseObject(srcSysObjectPK);
        destSysObjectPK = new SystemObjectPK(system, lid2);
        mEntObjDest = mc.getEnterpriseObject(destSysObjectPK);
        
        so2 = mEntObjDest.getSystemObject(system, lid2);
        personObj = (PersonObject) so2.getObject();
        
        oldSrcRevisionNumber = mEntObjSource.getSBR().getRevisionNumber();
        oldDestRevisionNumber = mEntObjDest.getSBR().getRevisionNumber();
        
        mr = mc.mergeSystemObject(system, 
                                  lid1, 
                                  lid2, 
                                  personObj, 
                                  oldSrcRevisionNumber .toString(),
                                  oldDestRevisionNumber.toString(),
                                  false, 
                                  new Boolean(true));

        
        //Test assertions
        eo = eoHelper.run(new String[] {"system=" + system, "lid=" + lid2});
        soSource = eo.getSystemObject(system, lid1);
        soDest = eo.getSystemObject(system, lid2);
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
        // Confirm there are no more potential duplicates
        sysObjectPK = new SystemObjectPK(system, lid2);
        eo = mc.getEnterpriseObject(sysObjectPK);
        so.setEUID(eo.getEUID());
        
        itr = mc.lookupPotentialDuplicates(so);
        count  = itr.count();

        if (count == 0) {
            log("MergeSystemObjectDeferred[pessimistic mode ensabled]: found no potential duplicate as expected");
        } else {
            fail("MergeSystemObjectDeferred[pessimistic mode ensabled]: " + count +
                " potential duplicate(s) found but not expected");
        }
        
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
                ts.addTest(new DeferredPessimisticModeSystemMerge(args[i]));
            }
        } else {
            ts = new TestSuite(DeferredPessimisticModeSystemMerge.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
