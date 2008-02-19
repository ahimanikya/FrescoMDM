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
import com.sun.mdm.index.potdup.PotentialDuplicateManager;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.util.Constants;

/** Test resolving potential duplicates
 */
public class ResolvePotentialDuplicates extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public ResolvePotentialDuplicates(String name) {
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
        
        // All should be potential duplicates
        assertTrue(count == 36);
        PotentialDuplicateSummary[] results = i.absolute(0, count);
        assertTrue(results.length == 36);
        
        //  check each record
        PotentialDuplicateSummary pds = null;
        PotentialDuplicateSummary pdsSearchResults[] = null;
        
        //  check only the first record
        pds = i.absolute(0);

        //Check the records have data components
        System.out.println("PotentialDuplicateSummary");
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
        assertTrue(pds.getStatus().compareToIgnoreCase(PotentialDuplicateManager.UNRESOLVED) == 0);        
        assertTrue(pds.getSystemCode() != null); 
        assertTrue(pds.getCreateUser() != null); 
        assertTrue(pds.getCreateDate() != null); 
        assertTrue(pds.getResolvedUser() == null); 
        assertTrue(pds.getResolvedDate() == null); 
        assertTrue(pds.getResolvedComment() == null); 
        assertTrue(pds.getWeight() > 0);        
        
        // peform a resolve operation
        MasterController mc = MCFactory.getMasterController();
        mc.resolvePotentialDuplicate(pds.getId(), false);

        PotentialDuplicateSearchObject pdso = new PotentialDuplicateSearchObject();
        pdso.setPageSize(20);
        pdso.setMaxElements(200);
        pdso.setStatus(PotentialDuplicateManager.RESOLVED);
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");            
        pdso.setFieldsToRetrieve(fields);
        count = mc.countPotentialDuplicates(pdso);
        
        // There should be one resolved record
        assertTrue(count == 1);

        //  check only the second record
        pds = i.absolute(1);
        
        // peform an auto-resolve operation
        mc.resolvePotentialDuplicate(pds.getId(), true);
        pdso.setStatus(PotentialDuplicateManager.AUTO_RESOLVED);
        count = mc.countPotentialDuplicates(pdso);
        
        //  There should be one auto-resolved record
        assertTrue(count == 1);

        pdso.setStatus(PotentialDuplicateManager.UNRESOLVED);
        count = mc.countPotentialDuplicates(pdso);
        
        //  There should be thirty-four unresolved record
        assertTrue(count == 34);

    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ResolvePotentialDuplicates.class));
    }
    
}
