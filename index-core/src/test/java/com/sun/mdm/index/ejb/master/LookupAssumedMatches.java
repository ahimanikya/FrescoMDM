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
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.ejb.master.helper.LookupAssumedMatchesHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.util.Constants;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class LookupAssumedMatches extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupAssumedMatches(String name) {
        super(name);
    }
    
    /** Set up the unit test.  In order to create potential duplicates,
     * SameSystemMatch rule must be enabled (or OneExactMatch).
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Test the potential duplicate lookup
     * @throws Exception an error occured
     */
    public void testLookupAssumedMatches() throws Exception {
        String args[] = {"fileName=LookupAssumedMatches1.txt", "fileType=eiEvent"};
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        LookupAssumedMatchesHelper helper = new LookupAssumedMatchesHelper();
        createHelper.clearDb();
        createHelper.run(args);
        AssumedMatchIterator i = helper.run(args);
        AssumedMatchSummary[] results = i.absolute(0, i.count());
        
        //  check each record
        AssumedMatchSummary ams = null;
        AssumedMatchSummary amsSearchResults[] = null;
        
        System.out.println("Checking createDate, Weights, and CreateUser");
        for (int counter = 0; counter < i.count(); counter++)   {
            ams = i.absolute(counter);
            
            //Check the records have data components
            System.out.println("AssumedMatchSummary: ");
            System.out.println(ams.toString());
            assertTrue(ams.getCreateDate() != null);        
            assertTrue(ams.getWeight() > 0);        
            assertTrue(ams.getCreateUser() != null); 
        }
        
        //  Check the record count 
        amsSearchResults = i.first(3); 
        System.out.println("amsSearchResults1: length --> " + amsSearchResults.length);
        assertTrue(amsSearchResults.length == 2); 
        
        //  Check the record count
        ams = i.first();
        System.out.println("currentPosition--> " + i.currentPosition());
        assertTrue(i.currentPosition() == 1);
        amsSearchResults = i.next(1);
        System.out.println("amsSearchResults2: length --> " + amsSearchResults.length);
        assertTrue(amsSearchResults.length == 1);
        
        //  Check the record count
        ams = i.absolute(1);
        ams = i.prev();
        System.out.println("currentPosition--> " + i.currentPosition());
        assertTrue(i.currentPosition() == 1);
        
        //  Check the record count
        ams = i.absolute(1);
        amsSearchResults = i.prev(1);
        System.out.println("amsSearchResults3: length --> " + amsSearchResults.length);
        assertTrue(amsSearchResults.length == 1);
        
        System.out.println("\n\n");
        
        //Check that before/newSO images not null
        SBR beforeSBR = results[0].getBeforeSBR();
        SystemObject newSO = results[0].getNewSO();
        PersonObject person1 = (PersonObject) beforeSBR.getObject();
        PersonObject person2 = (PersonObject) newSO.getObject();
        assertTrue(person1 != null);
        assertTrue(person2 != null);
        
        //TODO: check beforeSBR and NewSO to see if content valid
   
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LookupAssumedMatches.class));
    }
    
}
