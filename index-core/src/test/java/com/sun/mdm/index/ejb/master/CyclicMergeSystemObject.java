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
import com.sun.mdm.index.ejb.master.helper.BasicHelper;
import com.sun.mdm.index.ejb.master.helper.AddRecordHelper3;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.search.merge.MergeHistoryHelper;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;

/** Test for cyclic LID merges
 * @author rtam
 */
public class CyclicMergeSystemObject extends TestCase {
    
    private MasterController mMaster;
    private String mEuid1;
    
    /** Creates new CyclicMergeSystemObject
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public CyclicMergeSystemObject(String name) {
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
    
   
    /** Tests an SO merge within same EUID
     * @throws Exception an error occured
     */
    public void testCyclicMergeUnmerge() throws Exception {
        AddRecordHelper3 addRecordHelper3 = new AddRecordHelper3();
        boolean rc = addRecordHelper3.run(null, null, null, null, null);
        
        String sysCode = addRecordHelper3.getSystem();
        String lid0 = addRecordHelper3.getLid0();
        String lid1 = addRecordHelper3.getLid1();
        String lid2 = addRecordHelper3.getLid2();
        String lid3 = addRecordHelper3.getLid3();
        String euid0 = addRecordHelper3.getEuid0();
        String euid1 = addRecordHelper3.getEuid1();

        // create the cyclic merge
        
        MergeResult mr = mMaster.mergeSystemObject(sysCode, lid0, lid2, false);
        mr = mMaster.mergeSystemObject(sysCode, lid3, lid1, false);
        
        EnterpriseObject eo0 = mMaster.getEnterpriseObject(euid0);
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        
        //Test assertions
        
        SystemObject so0 = eo1.getSystemObject(sysCode, lid0);
        SystemObject so1 = eo0.getSystemObject(sysCode, lid1);
        SystemObject so2 = eo1.getSystemObject(sysCode, lid2);
        SystemObject so3 = eo0.getSystemObject(sysCode, lid3);
        assertTrue(so0.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(so1.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        assertTrue(so2.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        assertTrue(so3.getStatus().equals(SystemObject.STATUS_MERGED));
        
        // check merge history node
        MergeHistoryNode mergeHistoryNode = mMaster.getMergeHistory(euid0);
        assertTrue(mergeHistoryNode != null);
        mergeHistoryNode = mMaster.getMergeHistory(euid1);
        assertTrue(mergeHistoryNode != null);
        
        log("Cyclic merge completed.");

        // unmerge

        mr = mMaster.unmergeSystemObject(sysCode, lid0, lid2, false);
        mr = mMaster.unmergeSystemObject(sysCode, lid3, lid1, false);
        
        // retrieve the latest versions from the database
        
        eo0 = mMaster.getEnterpriseObject(euid0);
        eo1 = mMaster.getEnterpriseObject(euid1);
        
        //Test assertions
        so0 = eo0.getSystemObject(sysCode, lid0);
        so1 = eo0.getSystemObject(sysCode, lid1);
        so2 = eo1.getSystemObject(sysCode, lid2);
        so3 = eo1.getSystemObject(sysCode, lid3);
        assertTrue(so0.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(so1.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        assertTrue(so2.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        assertTrue(so3.getStatus().equals(SystemObject.STATUS_ACTIVE));
        
        log("Cyclic unmerge completed.");
        
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
                ts.addTest(new CyclicMergeSystemObject(args[i]));
            }
        } else {
            ts = new TestSuite(CyclicMergeSystemObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
