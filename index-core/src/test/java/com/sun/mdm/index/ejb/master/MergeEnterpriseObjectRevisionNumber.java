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
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 */
public class MergeEnterpriseObjectRevisionNumber extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    private EnterpriseObject mEntObjSource;
    private EnterpriseObject mEntObjDest;
    private CreateEnterpriseObjectHelper mHelper;

    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeEnterpriseObjectRevisionNumber(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
        
    }
    
    /** Tests an EO merge.  It should fail because the revision numbers
     * do not match.
     *
     * @throws Exception an error occured
     */
    public void testMergeRevisionNumber() throws Exception {
        CreateEnterpriseObjectHelper cHelper = new CreateEnterpriseObjectHelper();
        List eoList = cHelper.run(new String[] {"fileName=MergeEnterpriseObject1.txt", "fileType=eiEvent"});
        mEntObjSource = (EnterpriseObject) eoList.get(1);
        mEntObjDest = (EnterpriseObject) eoList.get(0);
        //Merge the records
        MergeEnterpriseObjectHelper helper = new MergeEnterpriseObjectHelper();
        String sourceEUID = mEntObjSource.getEUID();
        String destEUID = mEntObjDest.getEUID();
        Integer oldSrcRevisionNumber = mEntObjSource.getSBR().getRevisionNumber();
        Integer oldDestRevisionNumber = mEntObjDest.getSBR().getRevisionNumber();
        int tempNewSrcRevisionNumber = oldSrcRevisionNumber.intValue() + 1;
        int tempNewDestRevisionNumber = oldDestRevisionNumber.intValue() + 1;
        Integer newSrcRevisionNumber = new Integer(tempNewSrcRevisionNumber);
        Integer newDestRevisionNumber = new Integer(tempNewDestRevisionNumber);
        String calcOnly = "false";
        log("Merging records.  Source: " + sourceEUID + " Dest: " + destEUID);
        String[] mergeCommand = {"euidSource=" + sourceEUID, 
                                 "euidDest=" + destEUID, 
                                 "srcRevisionNumber=" + newSrcRevisionNumber, 
                                 "destRevisionNumber=" + newDestRevisionNumber, 
                                 "calcOnly=" + calcOnly}; 
       
        //  This should fail.  An exception is expected.
        try {
            MergeResult mr = helper.run(mergeCommand);
            log("Error: revision numbers mismatch exception expected but not encountered.");
            assertTrue(false);
            
        } catch (Exception e) {
            log("Exception caught as expected: revision number mismatch.");
        }
    }

    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MergeEnterpriseObjectRevisionNumber.class));
    }
    
}
