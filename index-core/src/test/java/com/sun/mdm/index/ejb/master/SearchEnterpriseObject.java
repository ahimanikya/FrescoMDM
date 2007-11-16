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
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.ejb.master.helper.SearchEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.SystemObject;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class SearchEnterpriseObject extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    
    private static final String[] SEARCH_IDS = {
        "ALPHA-SEARCH",
        "PHONETIC-SEARCH",
        "BLOCKER-SEARCH"
    };
    
    private static final String[] TRUE_FALSE = {
        "true",
        "false"
    };
    
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public SearchEnterpriseObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Test the various person searches
     * @throws Exception an error occured
     */
    public void testSearch() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject1.txt", "fileType=eiEvent"});
        String args[] = new String[5];
        args[0] = "fileName=SearchEnterpriseObject1.txt";
        args[1] = "fileType=eiEvent";
        args[2] = "searchType=ALPHA-SEARCH";
        args[3] = "sortField=Enterprise.SystemSBR.Person.PersonCatCode";
        for (int j = 0; j < TRUE_FALSE.length; j++) {
            args[4] = "calcOnly=" + TRUE_FALSE[j];            
            SearchEnterpriseObjectHelper helper = new SearchEnterpriseObjectHelper();
            EOSearchResultIterator i = helper.run(args);
            assertTrue(i.count() == 2);
            EOSearchResultRecord[] results = i.absolute(0, i.count());
            assertTrue(results != null);
            assertTrue(results.length == 2);
        }
        
        /*
        for (int i = 0; i < SEARCH_IDS.length; i++) {
            args[1] = SEARCH_IDS[i];
            for (int j = 0; j < TRUE_FALSE.length; j++) {
                args[2] = TRUE_FALSE[j];
            }
        }
         */
    }

    /** Test phonetic search for QAI 101480
     * @throws Exception an error occured
     */
    public void testPhoneticSearch1() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        //Try first name range search (alpha, then phonetic)
        searchOptions = new EOSearchOptions("PHONETIC-SEARCH", fields);
        searchOptions.setWeighted(true);
        
        SystemObject sysobj1 = new SystemObject();
        //SystemObject sysobj2 = new SystemObject();
        //SystemObject sysobj3 = new SystemObject();
        
        criteria = new EOSearchCriteria();
        
        criteria.setSystemObject(sysobj1); 
        //criteria.setSystemObject2(sysobj2); 
        //criteria.setSystemObject3(sysobj3); 
        
        PersonObject person = new PersonObject();
        person.setFirstName("YARITZA");
        sysobj1.setChildType("Person");
        sysobj1.setObject(person);
                
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 1);
        EOSearchResultRecord result = i.absolute(0);
        assertTrue(result != null);
        assertTrue(result.getComparisonScore() == 10);
        
    }



    /** Test range search
     * @throws Exception an error occured
     */
    public void testRangeSearch1() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        //Try first name range search (alpha, then phonetic)
        searchOptions = new EOSearchOptions("ALPHA-SEARCH", fields);
        searchOptions.setWeighted(false);
        
        SystemObject sysobj1 = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        SystemObject sysobj3 = new SystemObject();
        
        criteria = new EOSearchCriteria();
        
        criteria.setSystemObject(sysobj1); 
        criteria.setSystemObject2(sysobj2); 
        criteria.setSystemObject3(sysobj3); 
        
        PersonObject person = new PersonObject();
        sysobj1.setChildType("Person");
        sysobj1.setObject(person);
        PersonObject person2 = new PersonObject();
        person2.setFirstName("T");
        person2.setLastName("RAMOS");
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        PersonObject person3 = new PersonObject();
        person3.setFirstName("ZB");
        sysobj3.setChildType("Person");
        sysobj3.setObject(person3);
        
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 3);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 3);
        
    }
    
    public void testRangeSearch2() throws Exception {
        
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        //Try first name range search (alpha, then phonetic)
        SystemObject sysobj1 = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        SystemObject sysobj3 = new SystemObject();
        
        criteria = new EOSearchCriteria();
        
        criteria.setSystemObject(sysobj1); 
        criteria.setSystemObject2(sysobj2); 
        criteria.setSystemObject3(sysobj3); 
        
        PersonObject person = new PersonObject();
        sysobj1.setChildType("Person");
        sysobj1.setObject(person);
        PersonObject person2 = new PersonObject();
        person2.setFirstName("T");
        person2.setLastName("RAMOS");
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        PersonObject person3 = new PersonObject();
        sysobj3.setChildType("Person");
        sysobj3.setObject(person3);
        
        searchOptions = new EOSearchOptions("PHONETIC-SEARCH", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 3);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 3);
        
    }
    
     public void testRangeSearch3() throws Exception {
        
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        //Tighten up the criteria by requesting specific DOB
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        searchOptions = new EOSearchOptions("ALPHA-SEARCH", fields);
        searchOptions.setWeighted(false);
        
        SystemObject sysobj1 = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj1); 
        criteria.setSystemObject2(sysobj2); 
        PersonObject person = new PersonObject();
        person.setLastName("RAMOS");
        person.setDOB(sdf.parse("01101974"));
        sysobj1.setChildType("Person");
        sysobj1.setObject(person);
        PersonObject person2 = new PersonObject();
        person2.setFirstName("R");
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 1);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
     }
     
     public void testRangeSearch4() throws Exception {
        
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        //Try DOB range search
        SystemObject sysobj1 = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj1); 
        criteria.setSystemObject2(sysobj2); 
        PersonObject person = new PersonObject();
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date dob1 = sdf.parse("01011974");
        Date dob2 = sdf.parse("01011975");
        person.setDOB(dob1);  //Just to confuse matters.... range search should override
        sysobj1.setChildType("Person");
        sysobj1.setObject(person);
        PersonObject person2 = new PersonObject();
        person2.setDOB(dob2);
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        
        searchOptions = new EOSearchOptions("ALPHA-SEARCH", fields);
        searchOptions.setWeighted(false);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);
        assertTrue(i.count() == 2);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 2);
        
     }
     
     public void testRangeSearch5() throws Exception {
        
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject2.txt", "fileType=generic"});
       
        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        MasterController mc = MCFactory.getMasterController();
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();           
        fields.add("Enterprise.SystemSBR.Person.PersonId");
        fields.add("Enterprise.SystemSBR.Person.PersonCatCode");
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");     

        //Try address city zip code range
        SystemObject sysobj = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        SystemObject sysobj3 = new SystemObject();
        criteria = new EOSearchCriteria();
        
        criteria.setSystemObject(sysobj); 
        criteria.setSystemObject2(sysobj2); 
        criteria.setSystemObject3(sysobj3); 
        
        PersonObject person = new PersonObject();
        sysobj.setChildType("Person");
        sysobj.setObject(person);
        
        PersonObject person2 = new PersonObject();
        AddressObject address2 = new AddressObject();
        address2.setPostalCode("95000");
        person2.addAddress(address2);
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        
        PersonObject person3 = new PersonObject();
        AddressObject address3 = new AddressObject();
        address3.setPostalCode("95002");
        person3.addAddress(address3);
        sysobj3.setChildType("Person");
        sysobj3.setObject(person3);
        
        searchOptions = new EOSearchOptions("ALPHA-SEARCH", fields);
        searchOptions.setWeighted(false);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 2);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 2);
    }    

    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new SearchEnterpriseObject(args[i]));
            }
        } else {
            ts = new TestSuite(SearchEnterpriseObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
