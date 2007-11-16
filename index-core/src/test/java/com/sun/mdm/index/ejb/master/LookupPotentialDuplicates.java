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
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.util.Constants;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class LookupPotentialDuplicates extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupPotentialDuplicates(String name) {
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
    public void testPotentialDuplicates() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.run(new String[] {"fileName=LookupPotentialDuplicates1.txt", "fileType=eiEvent"});
        LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper.run(new String[0]);
        int count = i.count();
        
        //All should be potential duplicates
        assertTrue(count == 36);
        PotentialDuplicateSummary[] results = i.absolute(0, count);
        assertTrue(results.length == 36);
        
        //Check that masking works
        PersonObject person1 = (PersonObject) results[0].getObject1();  
        PersonObject person2 = (PersonObject) results[0].getObject2();  
        assertTrue(person1 != null);
        assertTrue(person2 != null);

        //  check each record
        PotentialDuplicateSummary pds = null;
        PotentialDuplicateSummary pdsSearchResults[] = null;
        
        //  check only the first record
        pds = i.absolute(0);

        //Check the records have data components
        System.out.println("PotentialDuplicateSummary");
        pds.setStatus("Test");
        System.out.println(pds.toString()); 
        System.out.println("pds.getStatus(): " + pds.getStatus());
        System.out.println("pds.getReason(): " + pds.getReason());
        System.out.println("pds.getSystemCode(): " + pds.getSystemCode());
        System.out.println("pds.getCreateUser(): " + pds.getCreateUser());
        System.out.println("pds.getCreateDate(): " + pds.getCreateDate());
        System.out.println("pds.getResolvedUser(): " + pds.getResolvedUser());
        System.out.println("pds.getResolvedDate(): " + pds.getResolvedDate());
        System.out.println("pds.getResolvedComment(): " + pds.getResolvedComment());
        System.out.println("pds.getWeight(): " + pds.getWeight());
        assertTrue(pds.getStatus().compareToIgnoreCase("Test") == 0);        
        assertTrue(pds.getSystemCode() != null); 
        assertTrue(pds.getCreateUser() != null); 
        assertTrue(pds.getCreateDate() != null); 
        assertTrue(pds.getResolvedUser() == null); 
        assertTrue(pds.getResolvedDate() == null); 
        assertTrue(pds.getResolvedComment() == null); 
        assertTrue(pds.getWeight() > 0);        
        
        
        //  Check the record count 
        pdsSearchResults = i.first(40); 
        System.out.println("pdsSearchResults: length --> " + pdsSearchResults.length);
        assertTrue(pdsSearchResults.length == 36); 
        
        //  Check the record count
        pds = i.first();
        System.out.println("currentPosition--> " + i.currentPosition());
        assertTrue(i.currentPosition() == 1);
        pdsSearchResults = i.next(5);
        System.out.println("pdsSearchResults2: length --> " + pdsSearchResults.length);
        assertTrue(pdsSearchResults.length == 5);
        
        //  Check the record count
        pds = i.first();
        pds = i.next();
        System.out.println("currentPosition--> " + i.currentPosition());
        assertTrue(i.currentPosition() == 2);
        
        //  Check the record count
        pds = i.absolute(1);
        pds = i.prev();
        System.out.println("currentPosition--> " + i.currentPosition());
        assertTrue(i.currentPosition() == 1);
        
        //  Check the record count
        pds = i.absolute(35);
        pdsSearchResults = i.prev(5);
        System.out.println("pdsSearchResults3: length --> " + pdsSearchResults.length);
        assertTrue(pdsSearchResults.length == 5); 
        
        //  Check associated potential duplicates
        pds = i.absolute(0);
        i = pds.getAssociatedPotentialDuplicates();
        System.out.println("pds.getAssociatedPotentialDuplicates().count()--> " + i.count());
        assertTrue(i.count() == 15);
        
        System.out.println("\n\n"); 

        
        
        //Try sorting multiple times (per Bug 455)
        for (int j = 0; j < 5; j++) {
            i.sortBy("EUID1", false);
            results = i.absolute(0, count);
        }
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LookupPotentialDuplicates.class));
    }
    
}
