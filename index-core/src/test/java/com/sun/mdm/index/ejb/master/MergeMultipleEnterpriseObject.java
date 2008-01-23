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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
// import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/* Merge multiple EnterpriseObjects in one method.
 * CreateEO 1, CreateEO 2, Create EO 3
 * EUID Merge 1->3
 * EUID Merge 2->3
 */

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 */
public class MergeMultipleEnterpriseObject extends TestCase {
    
    /** master controller instance
      *
      */            
    private static MasterController controller;
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeMultipleEnterpriseObject(String name) {
        super(name);
    }
    
    /** Tests an EUI merge and EUID Unmerge then search transaction
     * @throws Exception an error occured
     */
    public void testMultipleMerge() throws Exception {
        // Create EO1, EO2, and EO3
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.clearDb();
        MasterController mc = MCFactory.getMasterController();
                        
        List eoList = helper.run(new String[] {"fileName=MergeEnterpriseObject1.txt", "fileType=eiEvent"});
        EnterpriseObject eo1 = (EnterpriseObject) eoList.get(0);
        EnterpriseObject eo2 = (EnterpriseObject) eoList.get(1);
        EnterpriseObject eo3 = (EnterpriseObject) eoList.get(2);
        
        String euid1 = eo1.getEUID();
        String euid2 = eo2.getEUID();
        String euid3 = eo3.getEUID();
        // Retrieve the destination image from the database.  
        // This image is different from the one in eoList, which has
        // Update Logs that cannot be present for this junit test.
        eo3 = mc.getEnterpriseObject(euid3);
        
        String[] sourceEUIDs = {euid1, euid2};
        
        // Retrieve the revision numbers from the database

        String srcRevisionNumbers[] = new String[2];
        srcRevisionNumbers[0] = eo1.getSBR().getRevisionNumber().toString();
        srcRevisionNumbers[1] = eo2.getSBR().getRevisionNumber().toString();
        String destRevisionNumber = eo3.getSBR().getRevisionNumber().toString();

        // Basic test of multiple merge functionality
        try{
            MergeResult[] mergeResults 
                = mc.mergeMultipleEnterpriseObjects(sourceEUIDs,
                                                    eo3,
                                                    srcRevisionNumbers,
                                                    destRevisionNumber,
                                                    false);
            EnterpriseObject mergeEO = mc.getEnterpriseObject(euid3);
            
            assertTrue(mergeResults.length == 2);
            
            // Verify that EO1 and EO2 have been merged
            EnterpriseObject eo = mc.getEnterpriseObject(euid1);
            assertTrue(eo == null);
            eo = mc.getEnterpriseObject(euid2);
            assertTrue(eo == null);
            eo = mc.getEnterpriseObject(euid3);
            assertTrue(eo != null);
            assertTrue(eo.getStatus().equals(SystemObject.STATUS_ACTIVE));
            
            log("Merged multiple EO Merge successfully.");
            
        } catch (Exception e) {
            log("Error: could not merge multiple EnterpriseObjects: " + e.getMessage());
            assertTrue(false);
        }
        

        // This merge should fail on the first source (eo1) because the revision number does not match.
        // Clear the database and obtain the new EUIDs for this test.  EO3 is not needed for this test.
        
        helper.clearDb();
                        
        eoList = helper.run(new String[] {"fileName=MergeEnterpriseObject_StarMerge.txt", "fileType=eiEvent"});
        eo1 = (EnterpriseObject) eoList.get(0);
        eo2 = (EnterpriseObject) eoList.get(1);
           
        euid1 = eo1.getEUID();
        euid2 = eo2.getEUID();
        
        sourceEUIDs[0] = euid1;
        sourceEUIDs[1] = null;
        
        // Retrieve only the revision number for EO1 from the database.  Then modify it to simulate
        // a concurrent update from another user.
        Integer oldSrcRevNum = eo1.getSBR().getRevisionNumber();
        int newSrcRevNum = oldSrcRevNum.intValue() + 1;
        srcRevisionNumbers[0] = String.valueOf(newSrcRevNum);
        srcRevisionNumbers[1] = null;
        destRevisionNumber = eo2.getSBR().getRevisionNumber().toString();
        

        try{
            MergeResult[] mergeResults 
                = mc.mergeMultipleEnterpriseObjects(sourceEUIDs,
                                                    eo2,
                                                    srcRevisionNumbers,
                                                    destRevisionNumber,
                                                    false);
            log("Error: revision numbers mismatch exception expected for eo1 but not encountered.");
            assertTrue(false);
        } catch (Exception e) {
            log("Exception caught for eo1 as expected: revision number mismatch: " + e.getMessage());

            // Verify that EO1 is still active
            EnterpriseObject eo = mc.getEnterpriseObject(euid1);
            assertTrue(eo.getStatus().equals(SystemObject.STATUS_ACTIVE));
        }
        
        // This merge should fail on the second source (eo3) because the revision number does not match.
        // Clear the database and obtain the new EUIDs for this test.  EO1 is not actually needed for this 
        // test, but it is included to simulate an actual invocation of the MC method.
        
        helper.clearDb();
                        
        eoList = helper.run(new String[] {"fileName=MergeEnterpriseObject_StarMerge.txt", "fileType=eiEvent"});
        eo1 = (EnterpriseObject) eoList.get(0);
        eo2 = (EnterpriseObject) eoList.get(1);
        eo3 = (EnterpriseObject) eoList.get(2);
           
        euid1 = eo1.getEUID();
        euid2 = eo2.getEUID();
        euid3 = eo3.getEUID();
        // Retrieve the destination image from the database.  
        // This image is different from the one in eoList, which has
        // Update Logs that cannot be present for this junit test.
        eo3 = mc.getEnterpriseObject(euid3);
        
        sourceEUIDs[0] = euid1;
        sourceEUIDs[1] = euid3;
        
        // Retrieve only the revision numbers from the database.  Then modify the revision number for EO3
        //  to simulate a concurrent update from another user.
        srcRevisionNumbers[0] = eo1.getSBR().getRevisionNumber().toString();
        oldSrcRevNum = eo3.getSBR().getRevisionNumber();
        newSrcRevNum = oldSrcRevNum.intValue() + 1;
        srcRevisionNumbers[1] = String.valueOf(newSrcRevNum);
        destRevisionNumber = eo2.getSBR().getRevisionNumber().toString();
        

        try{
            MergeResult[] mergeResults 
                = mc.mergeMultipleEnterpriseObjects(sourceEUIDs,
                                                    eo2,
                                                    srcRevisionNumbers,
                                                    destRevisionNumber,
                                                    false);
            log("Error: revision numbers mismatch exception expected for eo3 but not encountered.");
            assertTrue(false);
        } catch (Exception e) {
            log("Exception caught for eo3 as expected: revision number mismatch: " + e.getMessage());

            // Verify that EO1 and EO3 are still active
            EnterpriseObject eo = mc.getEnterpriseObject(euid1);
            assertTrue(eo.getStatus().equals(SystemObject.STATUS_ACTIVE));
            eo = mc.getEnterpriseObject(euid3);
            assertTrue(eo.getStatus().equals(SystemObject.STATUS_ACTIVE));
        }
        
        
        log("Merging multiple EnterpriseObjects completed successfully.");
        assert(true);
    }
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MergeMultipleEnterpriseObject.class));
    }
    
}
