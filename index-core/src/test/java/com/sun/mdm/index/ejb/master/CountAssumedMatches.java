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
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.util.Constants;
import java.sql.Timestamp;

/** Test counting assumed matches
 */
public class CountAssumedMatches extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public CountAssumedMatches(String name) {
        super(name);
    }
    
    /** Set up the unit test.  In order to create potential duplicates,
     * SameSystemMatch rule must be enabled (or OneExactMatch).
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Test the potential duplicate lookup
     * @throws Exception an error occured
     */
    public void testCountAssumedMatches() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.run(new String[] {"fileName=LookupAssumedMatches1.txt", "fileType=eiEvent"});

        MasterController mc = MCFactory.getMasterController();
        AssumedMatchSearchObject amso = new AssumedMatchSearchObject();
        amso.setPageSize(20);
        amso.setMaxElements(200);
        
        // set the search time frame for the last 24 hours
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - 86400000;    // for the previous 24 hours
        Timestamp startTimestamp = new Timestamp(startTime);
        // Increase the end timestamp by ten seconds in case the database is slow
        Timestamp endTimestamp = new Timestamp(currentTime + 10000);

        amso.setCreateStartDate(startTimestamp);
        amso.setCreateEndDate(endTimestamp);
        
        int count = mc.countAssumedMatches(amso);
        
        // There should be two assumed matches
        assertTrue(count == 2);

    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(CountAssumedMatches.class));
    }
    
}
