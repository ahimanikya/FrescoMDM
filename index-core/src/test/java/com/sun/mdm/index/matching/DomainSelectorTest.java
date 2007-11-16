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
package com.sun.mdm.index.matching;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class DomainSelectorTest extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DomainSelectorTest(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests valid creation
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        String[] args = { 
            "fileName=DomainSelectorTest.txt",
            "fileType=generic"
        };
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper(); 
        createHelper.clearDb();
        createHelper.run(args);
        MasterController mc = MCFactory.getMasterController();
        EnterpriseObject eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        PersonObject person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a US name
        assertTrue(person.getStdLastName().equals("FINK"));
        //Check address 1 standardized as US address
        AddressObject address = (AddressObject) person.getChild("Address", 0);
        assertTrue(address.getStreetNamePhoneticCode().equals("CALANVAL"));
        //Check address 2 standardized as UK address        
        address = (AddressObject) person.getChild("Address", 1);
        assertTrue(address.getStreetNamePhoneticCode().equals("CALAN"));
        
        eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0002"));
        assertTrue(eo != null);
        person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a US name
        assertTrue(person.getStdLastName().equals("FINK"));
        //Check address 1 standardized as US address
        address = (AddressObject) person.getChild("Address", 0);
        assertTrue(address.getStreetNamePhoneticCode().equals("CALANVAL"));
        
        eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0003"));
        assertTrue(eo != null);
        person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a UK name
        assertTrue(person.getStdLastName().equals("PHINQUE"));
        //Check address 1 standardized as UK address        
        address = (AddressObject) person.getChild("Address", 0);
        assertTrue(address.getStreetNamePhoneticCode().equals("CALAN"));
        
        eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0004"));
        assertTrue(eo != null);
        person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a US name
        assertTrue(person.getStdLastName().equals("FINK"));
        
        eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0005"));
        assertTrue(eo != null);
        person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a US name
        assertTrue(person.getStdLastName().equals("FINK"));
        //Check alias 1 standardized as US name
        AliasObject alias = (AliasObject) person.getChild("Alias", 0);
        assertTrue(alias.getStdLastName().equals("FINK"));
        //Check alias 2 standardized as US name
        alias = (AliasObject) person.getChild("Alias", 1);
        assertTrue(alias.getStdLastName().equals("FINK"));
        //Check alias 3 standardized as US name
        alias = (AliasObject) person.getChild("Alias", 2);
        assertTrue(alias.getStdLastName().equals("FINK"));
        
        eo = mc.getEnterpriseObject(new SystemObjectPK("SiteA", "0006"));
        assertTrue(eo != null);
        person = (PersonObject) eo.getSBR().getObject();
        //Check that the last name was standardized as a UK name
        assertTrue(person.getStdLastName().equals("PHINQUE"));
        //Check address 1 standardized as UK address        
        address = (AddressObject) person.getChild("Address", 0);
        assertTrue(address.getStreetNamePhoneticCode().equals("CALAN"));
        //Check alias 1 standardized as US name
        alias = (AliasObject) person.getChild("Alias", 0);
        assertTrue(alias.getStdLastName().equals("PHINQUE"));
        //Check alias 2 standardized as US name
        alias = (AliasObject) person.getChild("Alias", 1);
        assertTrue(alias.getStdLastName().equals("PHINQUE"));
        //Check alias 3 standardized as US name
        alias = (AliasObject) person.getChild("Alias", 2);
        assertTrue(alias.getStdLastName().equals("PHINQUE"));
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DomainSelectorTest.class));
    }
    
}
