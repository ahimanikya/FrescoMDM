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
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;   
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import java.util.Date;


/** Test class for testing Vality and eViewME match weights
 * @author Raymond Tam
 */
public class GenerateMatchWeight extends TestCase {
    
    /** master controller instance
      *
      */            
    private static MasterController controller;
    
    /** system instance
      *
      */            
    private static String system;
    
    /** local id instance
      *
      */            
    private static String lid;
    
    /** search type instance
      *
      */            
    private static String searchType;
    
    /** first name instance
      *
      */            
    private static String firstName;
    
    /** last name instance
      *
      */            
    private static String lastName;
    
    /**  match score tolerance for retrieved records 
      *
      */            
    private static float matchScoreTolerance = 1;   
    
    /**  expected Vality match score 
      *
      */            
    private static float valityMatchScore1 = 13;   
    
    /**  expected Vality match score 
      *
      */            
    private static float valityMatchScore2 = 19;   
    
    /**  expected eview Match Engine match score 
      *
      */            
    private static float eviewMEMatchScore1 = 20;   
    
    /**  expected eview Match Engine match score 
      *
      */            
    private static float eviewMEMatchScore2 = 17;   
    
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public GenerateMatchWeight(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests generating match weights
     * @throws Exception an error occured
     */
    public void testGenerateMatchWeight() throws Exception {

        String defaultSearchType = "BLOCKER-SEARCH2";
        String defaultFirstName = "DICK";
        String defaultLastName = "BROWN";
        float defaultMatchScoreTolerance = 1;
        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSearchType, 
                         defaultFirstName, 
                         defaultLastName, 
                         defaultMatchScoreTolerance, 
                         defaultSystem, 
                         defaultLid);
        assertTrue(rc);
    }
    /** Test match score generation.  
     *
     * @param tmpSearchType  search type
     * @param tmpFirstName  first name to search
     * @param tmpLastName  last name to search
     * @param tmpMatchScoreTolerance  match score tolerance
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSearchType, 
                              String tmpFirstName, 
                              String tmpLastName, 
                              float tmpMatchScoreTolerance, 
                              String tmpSystem, 
                              String tmpLid)  
            throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        System.out.println("\n---GenerateMatchWeight---\n");
        
        //  set the class members
        if (searchType == null && tmpSearchType != null)  {
            searchType = tmpSearchType;
        }
        if (firstName == null && tmpFirstName != null)  {
            firstName = tmpFirstName;
        }
        if (lastName == null && tmpLastName != null)  {
            lastName = tmpLastName;
        }
        if (system == null && tmpSystem != null)  {
            system = tmpSystem;
        }
        if (lid == null && tmpLid != null)  {
            lid = tmpLid;
        }
        matchScoreTolerance = tmpMatchScoreTolerance;
        
        controller = MCFactory.getMasterController();

        //  add two test records
        System.out.println("\nAttempting to add a record");
        SystemObject sysObj1 = buildUnitTestSystemObject1();
        SystemObject sysObj2 = buildUnitTestSystemObject2();
        System.out.println("Attempting executeMatch");
        MatchResult mr = controller.executeMatch(sysObj1);
        mr = controller.executeMatch(sysObj2);
        
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        // create search criteria 
        PersonObject person = new PersonObject();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        
        // create system search object 
        SystemObject sysobj = new SystemObject("SBYN", 
                                               "4568915615", 
                                               "Person",
                                               "active", 
                                               "Admin", 
                                               "add", 
                                               new Date(),
                                               "Admin", 
                                               "add", 
                                               new Date(), 
                                               person);

        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj);

        // create search options 
        EPathArrayList fields = new EPathArrayList();

        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");

        searchOptions = new EOSearchOptions(searchType, fields);
        searchOptions.setPageSize(20); 
        searchOptions.setWeighted(true);

        EOSearchResultIterator r = controller.searchEnterpriseObject(criteria, searchOptions);
        boolean rc = true;
        while (r.hasNext()) {
            EOSearchResultRecord rec = r.next();                
            if (!validScore(rec.getComparisonScore()))  {
                System.out.println("Test failed:  matchScore (" + rec.getComparisonScore()
                        + ") exceeded match score tolerance (" + matchScoreTolerance);
                rc = false;
            }  else  {
                PersonObject p = (PersonObject) rec.getObject();
                System.out.println(rec + ":" + p.getFirstName() + ", " +  p.getLastName() + ", " + p.getSSN());
            }
        }
        if (!rc)  {
            System.out.println("Test failed!\n");
            return false;
        }  else  {
            System.out.println("Test succeeded!\n");
            return true;
        }
    }
            
    /** Test match score to determine if it does not exceed the defined tolerance
     *
     * @param matchScore  match score to test
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    private static boolean validScore(float matchScore)  {
        
        float matchScoreDiff = 0;
        boolean valityTest1 = true;
        boolean valityTest2 = true;
        boolean eviewMETest1 = true;
        boolean eviewMETest2 = true;
        
        //  test against valitMatchScore1
        matchScoreDiff = Math.abs(matchScore - valityMatchScore1);
        if (matchScoreDiff > matchScoreTolerance)  {
            valityTest1 = false;
        }
        //  test against valitMatchScore2
        matchScoreDiff = Math.abs(matchScore - valityMatchScore2);
        if (matchScoreDiff > matchScoreTolerance)  {
            valityTest2 = false;
        }
        //  test against eviewMEMatchScore1
        matchScoreDiff = Math.abs(matchScore - eviewMEMatchScore1);
        if (matchScoreDiff > matchScoreTolerance)  {
            eviewMETest1 = false;
        }
        //  test against eviewMEMatchScore2
        matchScoreDiff = Math.abs(matchScore - eviewMEMatchScore2);
        if (matchScoreDiff > matchScoreTolerance)  {
            eviewMETest2 = false;
        }
        
        //  if all tests are false, then an error has occurred
        return (!(!valityTest1 && !valityTest2 && !eviewMETest1 && !eviewMETest2));
    }

    /** Build a system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject1() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject1");

        PersonObject person = new PersonObject();
        person.setLastName("BROWNE");
        person.setFirstName("DICK");
        person.setDOB(new java.util.Date());
        person.setGender("M");

        SystemObject sysObj = new SystemObject("SBYN", 
                                               "4568915645", 
                                               "Person",
                                               "active", 
                                               "Admin", 
                                               "add", 
                                               new Date(),
                                               "Admin", 
                                               "add", 
                                               new Date(), 
                                               person);
                                               
        return sysObj;
    }    
    
    /** Build a system object for testing
     *
     * @throws  ObjectException if an object error occurs
     * @return  system object
     */    
    private static SystemObject buildUnitTestSystemObject2() 
            throws ObjectException {
                
        System.out.println("Creating test SystemObject2");

        PersonObject person = new PersonObject();
        person.setLastName("BROWN");
        person.setFirstName("RICHARD");
        person.setDOB(new java.util.Date());
        person.setGender("M");

        SystemObject sysObj = new SystemObject("SBYN", 
                                               "4568915646", 
                                               "Person", 
                                               "active", 
                                               "Admin", 
                                               "add", 
                                               new Date(),
                                               "Admin", 
                                               "add", 
                                               new Date(), 
                                               person);
                                               
        return sysObj;
    }    
    
    /** Prints out a message
     *
     */    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(GenerateMatchWeight.class));
    }
    
}
