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
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import java.util.Collection;
import java.util.Iterator;

/** Test class for updateEnterpriseObject MC method
 * @author Dan Cidon
 */
public class UpdateSystemObject extends TestCase {
    
    private MasterController mc;
        
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdateSystemObject(String name) {
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
        String revisionNumber = eo.getSBR().getRevisionNumber().toString();
        assertTrue(eo != null);
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        PersonObject personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        
        //Update record without correct revision number
        personNode.setFirstName("CHANGED1");
        controller.updateSystemObject(sysObj, revisionNumber);
        //Check name change
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED1"));
        //Check SBR 
        SBR sbr = eo.getSBR(); 
        assertTrue(sbr != null);
        personNode = (PersonObject) sbr.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED1"));   
        
        //Update record without revision number
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        revisionNumber = eo.getSBR().getRevisionNumber().toString();
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        personNode.setFirstName("CHANGED2");
        controller.updateSystemObject(sysObj);       
        
        //Check name change
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED2"));
        //Check SBR 
        sbr = eo.getSBR(); 
        assertTrue(sbr != null);
        personNode = (PersonObject) sbr.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED2"));  
        
        //Update record with old revision number
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        //String revisionNumber = eo.getSBR().getRevisionNumber();
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        personNode.setFirstName("CHANGED3");
        boolean flag = false;
        try {
        	controller.updateSystemObject(sysObj, revisionNumber); 
        } catch (UserException ue) {
        	String msg = ue.getMessage();
        	flag = msg.endsWith("has been modified by another user.");
        }
        
        //Caught the exception
        assertTrue(flag);      
        
        //Check name change has been overwritten
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        assertTrue(eo != null);
        sysObj = eo.getSystemObject("SiteA", "0001"); 
        assertTrue(sysObj != null);
        personNode = (PersonObject) sysObj.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED2"));
        //Check SBR has not been overwritten
        sbr = eo.getSBR(); 
        assertTrue(sbr != null);
        personNode = (PersonObject) sbr.getObject();
        assertTrue(personNode != null);
        assertTrue(personNode.getFirstName().equals("CHANGED2"));          
          
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
                ts.addTest(new UpdateSystemObject(args[i]));
            }
        } else {
            ts = new TestSuite(UpdateSystemObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
