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
import com.sun.mdm.index.ejb.master.helper.AddRecordHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;   
import java.util.Date;

/** Test class for an alpha search
 * @author Raymond Tam
 */
public class AlphaSearchEnterpriseObject extends TestCase {
    
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
    
    /** minimum match threshold for retrieved records to be considered matches
      *
      */            
    private static float minComparisonThreshold = 0;   
    
        /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public AlphaSearchEnterpriseObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests alpha search
     * @throws Exception an error occured
     */
    public void testAlphaSearchEnterpriseObject() throws Exception {

        String defaultSearchType = "ALPHA-SEARCH";
        String defaultFirstName = "Deb";
        String defaultLastName = "Womack";
        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        float defaultMinComparisonThreshold = 5;
        
        boolean rc = run(defaultSearchType, 
                         defaultFirstName, 
                         defaultLastName, 
                         defaultMinComparisonThreshold, 
                         defaultSystem, 
                         defaultLid);
        assertTrue(rc);  
        
        
    }
    
    /** Tests alpha search
     *
     * @param tmpSearchType  search type
     * @param tmpFirstName  first name to search
     * @param tmpLastName  last name to search
     * @param tmpMinComparisonThreshold  minimum comparison threshold
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSearchType, 
                              String tmpFirstName, 
                              String tmpLastName, 
                              float tmpMinComparisonThreshold, 
                              String tmpSystem, 
                              String tmpLid)  
            throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        System.out.println("\n---AlphaSearchEnterpriseObject---\n");
        
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
        minComparisonThreshold = tmpMinComparisonThreshold;
        
        //  add a test record
        AddRecordHelper addRecordHelper = new AddRecordHelper();
        addRecordHelper.clearDb();
        boolean rc = addRecordHelper.run(system, lid, null);
        if (!rc)  {
            System.out.println("Test failed: addRecordHelper.run() returned false");
            return false;
        }
        controller = MCFactory.getMasterController();

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        // create search criteria 
        PersonObject person = new PersonObject();
        person.setLastName(lastName);
        person.setFirstName(firstName);

        // create system search object 
        SystemObject sysobj = new SystemObject("SBYN", 
                                               "4568915610", 
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
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");

        searchOptions = new EOSearchOptions(searchType, fields);
        searchOptions.setPageSize(20); 
        searchOptions.setWeighted(true);

        EOSearchResultIterator r = controller.searchEnterpriseObject(criteria, searchOptions);
        int count = 0;
        while (r.hasNext()) {
            EOSearchResultRecord rec = r.next();                
            if (rec.getComparisonScore() >= minComparisonThreshold)  {
                PersonObject p = (PersonObject) rec.getObject();
                System.out.println(rec + ":" + p.getFirstName() + ", " +  p.getLastName() + ", " + p.getSSN());
                count++;
            }
        }
        System.out.println("\nRecord Count (above min. threshold): " + count);
        if (count != expectedCount)  {
            System.out.println("Test failed: expected " + expectedCount + " record, but found " + count 
                    + " record(s)\n");
            return false;
        }  else  {
            System.out.println("Test succeeded!\n");
            return true;
        }
         
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
        junit.textui.TestRunner.run(new TestSuite(AlphaSearchEnterpriseObject.class));
    }
    
}
