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
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.util.Constants;
import java.sql.Timestamp;


/** Test counting potential duplicates.
 */
public class CountPotentialDuplicates extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public CountPotentialDuplicates(String name) {
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
    
    /** Test the potential duplicate count
     * @throws Exception an error occured
     */
    public void testCountPotentialDuplicates() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.run(new String[] {"fileName=LookupPotentialDuplicates1.txt", "fileType=eiEvent"});
        
        MasterController mc = MCFactory.getMasterController();
        PotentialDuplicateSearchObject pdso = new PotentialDuplicateSearchObject();
        pdso.setPageSize(20);
        pdso.setMaxElements(200);

        // set the search time frame for the last 24 hours
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - 86400000;    // for the previous 24 hours
        Timestamp startTimestamp = new Timestamp(startTime);
        // Increase the end timestamp by ten seconds in case the database is slow
        Timestamp endTimestamp = new Timestamp(currentTime + 10000);

        pdso.setCreateStartDate(startTimestamp);
        pdso.setCreateEndDate(endTimestamp);
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");            
        pdso.setFieldsToRetrieve(fields);
        
        int count = mc.countPotentialDuplicates(pdso);
        
        //All should be potential duplicates
        assertTrue(count == 36);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(CountPotentialDuplicates.class));
    }
    
}
