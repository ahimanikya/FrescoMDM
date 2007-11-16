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
import com.sun.mdm.index.ejb.master.helper.LookupPotentialDuplicatesHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import java.util.Collection;
import java.util.Iterator;

/** Test class for updateEnterpriseObject MC method
 * @author Dan Cidon
 */
public class UpdateEnterpriseObject extends TestCase {
    
    private MasterController mc;
        
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdateEnterpriseObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
       ClearDb.run();
    }
    
    /** Tests an EO update
     * @throws Exception an error occured
     */
    public void testUpdateSystemObject() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject1.txt", "fileType=generic"});
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        PersonObject personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        
        //Update record
        personNode.setFirstName("CHANGED");
        controller.updateEnterpriseObject(eo);
        
        //Check name change
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED"));      
        
        //Check SBR 
        SBR sbr = eo.getSBR(); 
        assertTrue(sbr != null);
        personNode = (PersonObject) sbr.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED"));    
        
        //Check for alias
        Collection aliases = personNode.getAlias();
        assertTrue(aliases != null);
        assertTrue(aliases.size() == 1);
        
        //Modify address
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        Collection c = personNode.getAddress();
        assertTrue(c != null);
        assertTrue(c.size() > 0);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            AddressObject addrObj = (AddressObject) i.next();
            addrObj.setAddressLine1("500 NORTH NEW STREET");
        }
        controller.updateEnterpriseObject(eo);
       
        //Check address modification
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001");
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        c = personNode.getAddress();
        assertTrue(c != null);        
        while (i.hasNext()) {
            AddressObject addrObj = (AddressObject) i.next();
            assertTrue(addrObj.getAddressLine1().equals("500 NEW STREET"));
        }
        
        //Remove address
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        c = personNode.getAddress();
        assertTrue(c != null);
        assertTrue(c.size() > 0);
        i = c.iterator();
        while (i.hasNext()) {
            AddressObject addrObj = (AddressObject) i.next();
            personNode.deleteChild(addrObj);
        }
        controller.updateEnterpriseObject(eo);
       
        //Check address removal
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001");
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        c = personNode.getAddress();
        assertTrue(c == null);
        //Make sure aliases were not removed 
        aliases = personNode.getAlias();
        assertTrue(aliases != null);
        assertTrue(aliases.size() == 1);   
        
        //Now check SBR
        sbr = eo.getSBR();
        assertTrue(sbr != null);
        personNode = (PersonObject) sbr.getObject();
        assertTrue(personNode != null);
        c = personNode.getAddress();
        assertTrue(c == null);             
        //Make sure aliases were not removed 
        aliases = personNode.getAlias();
        assertTrue(aliases != null);
        assertTrue(aliases.size() == 1);        
    }
    
    /** Tests addition of SO to an EO
     * @throws Exception an error occured
     */
    public void testAddSystemObject() throws Exception {

        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject1.txt", "fileType=generic"});
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        
        //Add new SO
        SystemObject sysObj2 = new SystemObject();
        sysObj2.setLID("0001");
        sysObj2.setSystemCode("SiteB");
        sysObj2.setValue("Status", "active");
        sysObj2.setCreateDateTime(new java.util.Date(0));
        sysObj2.setUpdateDateTime(new java.util.Date(0));
        PersonObject po = new PersonObject();
        sysObj2.setChildType(po.pGetTag());
        po.setFirstName("HELLO");
        po.setLastName("WORLD");
        po.setDOB(new java.util.Date());
        po.setGender("M");
        AddressObject ao = new AddressObject();
        ao.setAddressType("W");
        ao.setAddressLine1("2505 NORTH WALTER ST");
        ao.setStateCode("CA");
        ao.setCity("DAVIS");
        ao.setPostalCode("95616");
        po.addSecondaryObject(ao);
        sysObj2.setObject(po);
        
        eo.addSystemObject(sysObj2);
        controller.updateEnterpriseObject(eo);
        
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj2 = eo.getSystemObject("SiteB", "0001"); 
        assertTrue(sysObj != null);
    }
    
    /** Test date fields of SO
     * @throws Exception an error occured
     */
     /*
    public void testSystemObjectDateFields() throws Exception {    
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject2.txt", "fileType=generic"});    
        //Update system object date field
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        sysObj.setCreateDateTime(new java.util.Date(0));
        sysObj.setUpdateDateTime(new java.util.Date(0));
        controller.updateEnterpriseObject(eo);
    }
    */
    
    /** Tests an EO update to see if causes pessimistic mode handling
     * @throws Exception an error occured
     */
    public void testPessimisticMode() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.run(new String[] {"fileName=UpdateEnterpriseObject3.txt", "fileType=generic"});
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        PersonObject personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        
        //Update record
        personNode.setFirstName("JOHN");
        personNode.setLastName("SMITH");
        controller.updateEnterpriseObject(eo);
        
        //Check that a duplicate was created
        LookupPotentialDuplicatesHelper helper = new LookupPotentialDuplicatesHelper();     
        PotentialDuplicateIterator i = helper.run(new String[0]);
        int count = i.count();
        assertEquals(count, 1);
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new UpdateEnterpriseObject(args[i]));
            }
        } else {
            ts = new TestSuite(UpdateEnterpriseObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
