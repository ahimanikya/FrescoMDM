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
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PotentialDuplicate;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;

import java.util.List;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Sanjay.Sharma
 */
public class BlockerQueryTest1 extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public BlockerQueryTest1(String name) {
        super(name);
    }
    
    public void testOne() throws Exception {
    
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
        fields.add("Enterprise.SystemSBR.Person.EUID");
        fields.add("Enterprise.SystemSBR.Person.SSN");
        fields.add("Enterprise.SystemSBR.Person.FirstName");
        fields.add("Enterprise.SystemSBR.Person.LastName");
        
        SystemObject sysobj = new SystemObject();
        

        criteria = new EOSearchCriteria();
        criteria.setSystemObject(sysobj); 
        
        PersonObject person = new PersonObject();
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        person.setDOB(sdf.parse("02051966"));
        sysobj.setChildType("Person");
        sysobj.addChild(person);

        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");
        
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");
        
        person.addAddress(address2);
         
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-MULTI-VAL", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testTwo() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
 
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person2 = new PersonObject();
        person2.setDOB(sdf.parse("02041966"));
        
        
        PersonObject person3 = new PersonObject();
        person3.setDOB(sdf.parse("02061966"));
        
        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);

                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-MULTI-VAL-RANGE", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testThree() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
 
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person2 = new PersonObject();
        
        PersonObject person3 = new PersonObject();

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-CONSTANTS", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testFour() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person = new PersonObject();
        
        person.setDOB(sdf.parse("02051966"));
 
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
 
        PersonObject person2 = new PersonObject();
        
        PersonObject person3 = new PersonObject();

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-OFFSET", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        if ( i.hasNext() ) {
        	EOSearchResultRecord eosr = i.next();
        }
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testFive() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
 
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person2 = new PersonObject();
        person2.setDOB(sdf.parse("02041966"));
        PersonObject person3 = new PersonObject();

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-OFFSET", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testSix() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person = new PersonObject();
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
        person.setDOB(sdf.parse("02051966"));
        
        PersonObject person2 = new PersonObject();
        PersonObject person3 = new PersonObject();
        person3.setDOB(sdf.parse("02061966"));

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-OFFSET", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }
    
    public void testSeven() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person = new PersonObject();
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
        
        PersonObject person2 = new PersonObject();
        person2.setDOB(sdf.parse("02031966"));
        PersonObject person3 = new PersonObject();
        person3.setDOB(sdf.parse("02071966"));

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-OFFSET", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    public void testEight() throws Exception {
    	
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb();
        executeMatchHelper.run(new String[] {"fileName=BlockerSearchSingleEOMultipleValues.txt", "fileType=generic"});

        //This record is now created. Now get the enterprise Object and use it to do a blocker query
        
        MasterController mc = MCFactory.getMasterController();        
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));

        EOSearchCriteria criteria = null;
        EOSearchOptions searchOptions = null;
        
        /* create search options */
        EPathArrayList fields = new EPathArrayList();     
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");

        PersonObject person = new PersonObject();
        person.setDOB(sdf.parse("02051966"));
        
        AddressObject address = new AddressObject();
        address.setAddressType("H");
        address.setCity("Irvine");
        address.setStateCode("CA");     
        person.addAddress(address);
 
        AddressObject address2 = new AddressObject();
        address2.setAddressType("W");
        address2.setCity("MONROVIA");
        address2.setStateCode("CA");        
        person.addAddress(address2);
        
        PersonObject person2 = new PersonObject();
        person2.setDOB(sdf.parse("02031966"));
        PersonObject person3 = new PersonObject();
        person3.setDOB(sdf.parse("02071966"));

        sysobj.setChildType("Person");
        sysobj.addChild(person);
        sysobj2.addChild(person2);
        sysobj3.addChild(person3);
                
        searchOptions = new EOSearchOptions("BLOCKER-SEARCH-RANGE-WITH-OFFSET", fields);
        searchOptions.setWeighted(true);
        EOSearchResultIterator i = mc.searchEnterpriseObject(criteria, searchOptions);

        boolean b = i.hasNext();
        EOSearchResultRecord eosr = i.next();
        
        assertTrue(i.count() == 1);

        EOSearchResultRecord[] results = i.absolute(0, i.count());
        assertTrue(results != null);
        assertTrue(results.length == 1);
        
        System.out.println("Done");
        
    }

    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(BlockerQueryTest1.class));
    }
    
}
