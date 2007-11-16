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

/** Test class for range searches
 * @author Dan Cidon
 */
public class BlockerRangeSearchTest extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    
    
    /** Creates new BlockerRangeSearchTest
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public BlockerRangeSearchTest(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Test range search
     * @throws Exception an error occured
     */
    public void testRangeSearch() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject3.txt", "fileType=generic"});
       
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
        
        SystemObject sysobj = new SystemObject();
        SystemObject sysobj2 = new SystemObject();
        SystemObject sysobj3 = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj); 
        criteria.setSystemObject2(sysobj2); 
        criteria.setSystemObject3(sysobj3); 
        PersonObject person = new PersonObject();
        PersonObject person2 = new PersonObject();

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date dob2 = sdf.parse("01011974");
        Date dob3 = sdf.parse("01011976");
        person.setFirstName("YARITZA");
        person.setGender("M");
        person2.setDOB(dob2);
        sysobj.setChildType("Person");
        sysobj2.setChildType("Person");
        sysobj.setObject(person);
        sysobj2.setObject(person2);
        PersonObject person3 = new PersonObject();
        person3.setDOB(dob3);
        sysobj3.setChildType("Person");
        sysobj3.setObject(person3);
        
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 2);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 2);
        
        //Try address city zip code range
        sysobj2 = new SystemObject();
        sysobj3 = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject2(sysobj2); 
        criteria.setSystemObject3(sysobj3); 
        person2 = new PersonObject();
        AddressObject address2 = new AddressObject();
        address2.setPostalCode("95000");
        person2.addAddress(address2);
        
        sysobj2.setChildType("Person");
        sysobj2.setObject(person2);
        person3 = new PersonObject();
        AddressObject address3 = new AddressObject();
        address3.setPostalCode("95002");
        person3.addAddress(address3);
        sysobj3.setChildType("Person");
        sysobj3.setObject(person3);
        
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH", fields);
        searchOptions.setWeighted(false);
        i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 2);
        results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 2);
        
        //Now make sure blocker works OK when only one object is sent in
        sysobj = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj); 
        person = new PersonObject();

        Date dob = sdf.parse("01101974");
        person.setFirstName("YARITZA");
        person.setGender("M");
        person.setDOB(dob);
        sysobj.setChildType("Person");
        sysobj.setObject(person);
        
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH", fields);
        searchOptions.setWeighted(true);
        i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 1);
        results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
    }    
    
    /** Test range search - offset defaults
     * @throws Exception an error occured
     */
    public void testRangeSearch2() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject3.txt", "fileType=generic"});
       
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
        
        SystemObject sysobj = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj); 
        PersonObject person = new PersonObject();

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date dob = sdf.parse("12311974");
        person.setDOB(dob);
        sysobj.setChildType("Person");
        sysobj.setObject(person);
        
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH2", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 2);
        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 2);
    }

    /** Test range search - constant defaults
     * @throws Exception an error occured
     */
    public void testRangeSearch3() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=SearchEnterpriseObject3.txt", "fileType=generic"});
       
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
        
        SystemObject sysobj = new SystemObject();
        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj); 
        PersonObject person = new PersonObject();

        /*
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        Date dob = sdf.parse("12311974");
        person.setDOB(dob);
         */
        sysobj.setChildType("Person");
        sysobj.setObject(person);
        
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH3", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        assertTrue(i.count() == 1);

    }    
    
    /** Main entry point
     * @param args args
     */
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new BlockerRangeSearchTest(args[i]));
            }
        } else {
            ts = new TestSuite(BlockerRangeSearchTest.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
