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
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.UndoAssumedMatchHelper;
import com.sun.mdm.index.ejb.master.helper.GetEUID;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.LookupAssumedMatchesHelper; 
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class UndoAssumedMatch extends TestCase {
    
    private static final String SYSTEM1 = "SiteA";
    private static final String SYSTEM2 = "SiteB";
    private static final String LID1 = "0001";
    private static final String LID2 = "0002";
        
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UndoAssumedMatch(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests undo assumed match.  Basic objects (no secondaries).
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        String euid = insertRecords("UndoAssumedMatch1.txt");
        String assumedMatchId = findAssumedMatch(euid);
        String newEuid = undoAssumedMatch(assumedMatchId);
        verifyUndo(newEuid, SYSTEM1, LID1, SYSTEM2, LID1);
    }

    /** Tests undo assumed match.  Full objects, multiple SO's.
     * @throws Exception an error occured
     */
    public void test2() throws Exception {
        String euid = insertRecords("UndoAssumedMatch2.txt");
        String assumedMatchId = findAssumedMatch(euid);
        String newEuid = undoAssumedMatch(assumedMatchId);
        verifyUndo(newEuid, SYSTEM1, LID1, SYSTEM2, LID1);
    }    
    
    private String insertRecords(String dataFile) throws Exception {
        ExecuteMatchHelper matchHelper = new ExecuteMatchHelper();
        List resultArray = matchHelper.run(new String[] {"fileName=" + dataFile, "fileType=generic"});
        int count = resultArray.size();
        assertTrue(count > 1);
        String euid = ((MatchResult) resultArray.get(0)).getEUID();
        for (int i = 1; i < count; i++) {
            String euid2 = ((MatchResult) resultArray.get(i)).getEUID();
            //Check to make sure an assumed match was done
            assertTrue(euid.equals(euid2));
        }
        return euid;
    }
    
    private void verifyUndo(String newEuid, String system1, String lid1, 
    String system2, String lid2) throws Exception {
        //Test assertions
        log("Testing assertions.");    
        GetEUID euidHelper = new GetEUID();
        String[] command1 = {"system=" + system1, "lid=" + lid1};
        String[] command2 = {"system=" + system2, "lid=" + lid2};
        String euid1 = euidHelper.run(command1);
        String euid2 = euidHelper.run(command2);
        assertTrue(!euid1.equals(euid2));
        assertTrue(euid2.equals(newEuid));        
    }
    
    private String findAssumedMatch(String euid) throws Exception {
        LookupAssumedMatchesHelper lookupHelper = new LookupAssumedMatchesHelper();
        String[] command = {"euid=" + euid};
        AssumedMatchIterator i = lookupHelper.run(command);
        assertTrue(i.count() == 1);
        if (i.hasNext()) {
            AssumedMatchSummary summary = (AssumedMatchSummary) i.next();
            return summary.getId();
        } else {
            throw new Exception("Assumed match record not found.");
        }        
    }
    
    private String undoAssumedMatch(String assumedMatchId) throws Exception {
        UndoAssumedMatchHelper helper = new UndoAssumedMatchHelper();
        log("Undo assumed match.");
        String[] undoCommand = {"id=" + assumedMatchId}; 
        String newEuid = helper.run(undoCommand);
        log("Undo completed.");
        return newEuid;
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(UndoAssumedMatch.class));
    }
    
}
