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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.LookupTransactionsHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/** Test class for LID merge with deleting a destination child as defined in the
 * eIndex50.xml file
 * @author rtam
 */
public class MergeLIDWithNewImage extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeLIDWithNewImage(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /* Test an LID merge for proper handling of "removed" child objects.  "removed"
     * does NOT mean that the object was marked for removal in the desired image.
     * Instead, it means that the cihld is is eradicated from the desired image 
     * and not found in the image sent to the MasterController for processing.  
     * This simulates a LID merge from the EDM where the destination address is 
     * removed.  The source address should replace the destination address.
     * @throws Exception object access exception
     */
    public void testMergeLIDUpdate() throws Exception {
        
    	PersonObject person = new PersonObject();
    	person.setFirstName("Sanju");
    	person.setLastName("Bohra");
    	person.setSSN("000000001");
    	person.setDOB(new Date("10/30/2005"));
    	person.setGender("M");
    	
    	AddressObject address = new AddressObject();
    	address.setAddressType("H");
    	address.setAddressLine1("123 Main");
    	address.setCity("Irvine");
    	address.setPostalCode("11111");
    	
    	person.addAddress(address);
    	
    	PersonObject person2 = new PersonObject();
    	person2.setFirstName("Sanjay");
    	person2.setLastName("Sharma");
    	person2.setSSN("000000002");
    	person2.setDOB(new Date("02/05/2005"));
    	person2.setGender("M");

    	AddressObject address2 = new AddressObject();
    	address2.setAddressType("H");
    	address2.setAddressLine1("456 Main");
    	address2.setCity("Monrovia");
    	address2.setPostalCode("22222");

    	AddressObject address22 = new AddressObject();
    	address22.setAddressType("M");
    	address22.setAddressLine1("123 PO Box");
    	address22.setCity("Monrovia");
    	address22.setPostalCode("33333");
    	    	
    	person2.addAddress(address2);
    	person2.addAddress(address22);

    	String systemCode = "SiteA";
    	String lidSource = "0001";
    	String lidDest = "0002";
		
    	SystemObject sysObj = new SystemObject(systemCode, lidSource, "Person", "active", "eview", "Add", new Date(), "eview", "Add", new Date(), person);
    	
    	SystemObject sysObj2 = new SystemObject(systemCode, lidDest, "Person", "active", "eview", "Add", new Date(), "eview", "Add", new Date(), person2);
    	
        MasterController mc = MCFactory.getMasterController();
    	
    	// Create Two Enterprise Object 
    	EnterpriseObject eo = mc.createEnterpriseObject(sysObj);
    	EnterpriseObject eo2 = mc.createEnterpriseObject(sysObj2);

    	EnterpriseObject eoBeforeMerge = mc.getEnterpriseObject(eo.getEUID());
    	EnterpriseObject eo2BeforeMerge = mc.getEnterpriseObject(eo2.getEUID());
 
    	PersonObject person3 = new PersonObject();
    	person3.setFirstName("Sanju");
    	person3.setLastName("Kaushik");
    	person3.setSSN("000000002");
    	person3.setDOB(new Date("02/05/2005"));
    	person3.setGender("M");
        
    	AddressObject address3 = new AddressObject();
    	address3.setAddressType("H");
    	address3.setAddressLine1("789 Main");
    	address3.setCity("Monrovia");
    	address3.setPostalCode("33333");

    	AddressObject address33 = new AddressObject();
    	address33.setAddressType("W");
    	address33.setAddressLine1("901 Business St.");
    	address33.setCity("Monrovia");
    	address33.setPostalCode("44444");
         
    	person3.addAddress(address3);
    	person3.addAddress(address33);
    	
    	mc.mergeSystemObject(systemCode, lidSource, lidDest, person3, false);

    	EnterpriseObject eoAfterMerge = mc.getEnterpriseObject(eo.getEUID());
    	EnterpriseObject eo2AfterMerge = mc.getEnterpriseObject(eo2.getEUID());
    	
    	mc.unmergeSystemObject(systemCode, lidSource, lidDest, false);

    	EnterpriseObject eoAfterUnMerge = mc.getEnterpriseObject(eo.getEUID());
    	EnterpriseObject eo2AfterUnMerge = mc.getEnterpriseObject(eo2.getEUID());
    	
        LookupTransactionsHelper lt = new LookupTransactionsHelper();
        TransactionIterator iterator = 
            lt.run(new String[] {"startDate=20000101000000", "endDate=20100101000000", "pageSize=" + 10});
        System.out.println("# of XA's found: " + iterator.count());
        int count = 0;
        Date latestDate = null;
        while (iterator.hasNext()) {
            TransactionSummary summary = iterator.next();
            Date nextDate = summary.getTransactionObject().getTimeStamp();
            if (latestDate == null || nextDate.after(latestDate)) {
                latestDate = nextDate;
            }
            count++;
        }

        TransactionSummary[] summary = iterator.absolute(0, iterator.count());

        EnterpriseObjectHistory eh1 = summary[summary.length-1].getEnterpriseObjectHistory();
        
        EnterpriseObject histBeforeEO1 = eh1.getBeforeEO1();
        EnterpriseObject histBeforeEO2 = eh1.getBeforeEO2();
        EnterpriseObject histAfterEO = eh1.getAfterEO();
        EnterpriseObject histAfterEO2 = eh1.getAfterEO2();

        //===========================================================================================================================
        assertTrue  ( histBeforeEO1 != null );
        assertTrue  ( histBeforeEO2 == null );        
        assertTrue  ( histAfterEO != null );
        assertTrue  ( histAfterEO2 != null );

        PersonObject person2AfterMergeSurvivor = (PersonObject)eo2AfterMerge.getSystemObject(systemCode, lidDest).getChild("Person",0);
        PersonObject person2AfterMergeSurvivorHist = (PersonObject)histBeforeEO1.getSystemObject(systemCode, lidDest).getChild("Person",0);

        assertTrue( person2AfterMergeSurvivor.getFirstName().equals(person2AfterMergeSurvivorHist.getFirstName())); 
        assertTrue( person2AfterMergeSurvivor.getLastName().equals(person2AfterMergeSurvivorHist.getLastName())); 
        assertTrue( person2AfterMergeSurvivor.getDOB().equals(person2AfterMergeSurvivorHist.getDOB())); 
        assertTrue( person2AfterMergeSurvivor.getSSN().equals(person2AfterMergeSurvivorHist.getSSN())); 
        assertTrue( person2AfterMergeSurvivor.getGender().equals(person2AfterMergeSurvivorHist.getGender())); 

        Collection coll = person2AfterMergeSurvivorHist.getAddress();
        Iterator itr = coll.iterator();        
        while ( itr.hasNext() ) {
        	AddressObject addressHist = (AddressObject)itr.next();
        	ObjectKey key = addressHist.pGetKey();
        	AddressObject addressTmp = (AddressObject)person2AfterMergeSurvivor.getChild("Address",key);
        	assertTrue( addressHist.equals(addressTmp));
        }
        
        Collection coll2 = person2AfterMergeSurvivor.getAddress();
        Iterator itr2 = coll2.iterator();        
        while ( itr2.hasNext() ) {
        	AddressObject addressTmp = (AddressObject)itr2.next();
        	ObjectKey key = addressTmp.pGetKey();
        	AddressObject addressHist = (AddressObject)person2AfterMergeSurvivorHist.getChild("Address",key);
        	assertTrue( addressTmp.equals(addressHist));
        }
        
        //===========================================================================================================================
        PersonObject person2AfterMergeMerged = (PersonObject)eo2AfterMerge.getSystemObject(systemCode, lidSource).getChild("Person",0);
        PersonObject person2AfterMergeMergedHist = (PersonObject)histBeforeEO1.getSystemObject(systemCode, lidSource).getChild("Person",0);
                
        assertTrue( person2AfterMergeMerged.getFirstName().equals(person2AfterMergeMergedHist.getFirstName())); 
        assertTrue( person2AfterMergeMerged.getLastName().equals(person2AfterMergeMergedHist.getLastName())); 
        assertTrue( person2AfterMergeMerged.getDOB().equals(person2AfterMergeMergedHist.getDOB())); 
        assertTrue( person2AfterMergeMerged.getSSN().equals(person2AfterMergeMergedHist.getSSN())); 
        assertTrue( person2AfterMergeMerged.getGender().equals(person2AfterMergeMergedHist.getGender())); 

        Collection coll3 = person2AfterMergeMergedHist.getAddress();
        Iterator itr3 = coll3.iterator();        
        while ( itr3.hasNext() ) {
        	AddressObject addressHist = (AddressObject)itr3.next();
        	ObjectKey key = addressHist.pGetKey();
        	AddressObject addressTmp = (AddressObject)person2AfterMergeMerged.getChild("Address",key);
        	assertTrue( addressHist.equals(addressTmp));
        }
        
        Collection coll4 = person2AfterMergeMerged.getAddress();
        Iterator itr4 = coll4.iterator();        
        while ( itr4.hasNext() ) {
        	AddressObject addressTmp = (AddressObject)itr4.next();
        	ObjectKey key = addressTmp.pGetKey();
        	AddressObject addressHist = (AddressObject)person2AfterMergeMergedHist.getChild("Address",key);
        	assertTrue( addressTmp.equals(addressHist));
        }
        //===========================================================================================================================
        
        EnterpriseObjectHistory eh2 = summary[summary.length-2].getEnterpriseObjectHistory();
        
        histBeforeEO1 = eh2.getBeforeEO1();
        histBeforeEO2 = eh2.getBeforeEO2();
        histAfterEO = eh2.getAfterEO();
        histAfterEO2 = eh2.getAfterEO2();

        assertTrue  ( histBeforeEO1 != null );
        assertTrue  ( histBeforeEO2 != null );        
        assertTrue  ( histAfterEO != null );
        assertTrue  ( histAfterEO2 == null );

        //===========================================================================================================================
        PersonObject person2BeforeMergeSurvivor = (PersonObject)eo2BeforeMerge.getSystemObject(systemCode, lidDest).getChild("Person",0);
        PersonObject person2BeforeMergeSurvivorHist = (PersonObject)histBeforeEO1.getSystemObject(systemCode, lidDest).getChild("Person",0);

        assertTrue( person2BeforeMergeSurvivor.getFirstName().equals(person2BeforeMergeSurvivorHist.getFirstName())); 
        assertTrue( person2BeforeMergeSurvivor.getLastName().equals(person2BeforeMergeSurvivorHist.getLastName())); 
        assertTrue( person2BeforeMergeSurvivor.getDOB().equals(person2BeforeMergeSurvivorHist.getDOB())); 
        assertTrue( person2BeforeMergeSurvivor.getSSN().equals(person2BeforeMergeSurvivorHist.getSSN())); 
        assertTrue( person2BeforeMergeSurvivor.getGender().equals(person2BeforeMergeSurvivorHist.getGender())); 

        Collection coll5 = person2BeforeMergeSurvivorHist.getAddress();
        Iterator itr5 = coll5.iterator();        
        while ( itr5.hasNext() ) {
        	AddressObject addressHist = (AddressObject)itr5.next();
        	ObjectKey key = addressHist.pGetKey();
        	AddressObject addressTmp = (AddressObject)person2BeforeMergeSurvivor.getChild("Address",key);
        	assertTrue( addressHist.equals(addressTmp));
        }
        
        Collection coll6 = person2BeforeMergeSurvivor.getAddress();
        Iterator itr6 = coll6.iterator();        
        while ( itr6.hasNext() ) {
        	AddressObject addressTmp = (AddressObject)itr6.next();
        	ObjectKey key = addressTmp.pGetKey();
        	AddressObject addressHist = (AddressObject)person2BeforeMergeSurvivorHist.getChild("Address",key);
        	assertTrue( addressTmp.equals(addressHist));
        }
        //===========================================================================================================================
        PersonObject person2BeforeMergeMerged = (PersonObject)eoBeforeMerge.getSystemObject(systemCode, lidSource).getChild("Person",0);
        PersonObject person2BeforeMergeMergedHist = (PersonObject)histBeforeEO2.getSystemObject(systemCode, lidSource).getChild("Person",0);

        assertTrue( person2BeforeMergeMerged.getFirstName().equals(person2BeforeMergeMergedHist.getFirstName())); 
        assertTrue( person2BeforeMergeMerged.getLastName().equals(person2BeforeMergeMergedHist.getLastName())); 
        assertTrue( person2BeforeMergeMerged.getDOB().equals(person2BeforeMergeMergedHist.getDOB())); 
        assertTrue( person2BeforeMergeMerged.getSSN().equals(person2BeforeMergeMergedHist.getSSN())); 
        assertTrue( person2BeforeMergeMerged.getGender().equals(person2BeforeMergeMergedHist.getGender())); 

        Collection coll7 = person2BeforeMergeMergedHist.getAddress();
        Iterator itr7 = coll7.iterator();        
        while ( itr7.hasNext() ) {
        	AddressObject addressHist = (AddressObject)itr7.next();
        	ObjectKey key = addressHist.pGetKey();
        	AddressObject addressTmp = (AddressObject)person2BeforeMergeMerged.getChild("Address",key);
        	assertTrue( addressHist.equals(addressTmp));
        }
        
        Collection coll8 = person2BeforeMergeMerged.getAddress();
        Iterator itr8 = coll8.iterator();        
        while ( itr8.hasNext() ) {
        	AddressObject addressTmp = (AddressObject)itr8.next();
        	ObjectKey key = addressTmp.pGetKey();
        	AddressObject addressHist = (AddressObject)person2BeforeMergeMergedHist.getChild("Address",key);
        	assertTrue( addressTmp.equals(addressHist));
        }
        //===========================================================================================================================
        
    }
   
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MergeLIDUpdate.class));
    }
}
