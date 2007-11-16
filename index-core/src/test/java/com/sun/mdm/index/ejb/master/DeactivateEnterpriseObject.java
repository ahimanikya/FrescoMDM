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
import com.sun.mdm.index.ejb.master.helper.ActivateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.DeactivateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;

/** Test class for deactivate system object method
 * @author Dan Cidon
 */
public class DeactivateEnterpriseObject extends TestCase {
    
    private MasterController mc;
            
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeactivateEnterpriseObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests deactivate enterprise object
     * @throws Exception an error occured
     * @return euid that was deactivated
     */
    public String testDeactivateEnterpriseObject1() throws Exception {
        //Insert Data
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        String[] command = {"fileName=DeactivateEnterpriseObject1.txt", "fileType=generic"};
        List eoList = createHelper.run(command);
        EnterpriseObject eo = (EnterpriseObject) eoList.get(0);
        
        //Deactivate EO
        DeactivateEnterpriseObjectHelper helper = new DeactivateEnterpriseObjectHelper();
        command = new String[] {"euid=" + eo.getEUID()}; 
        helper.run(command);
        log("Deactivate completed.");
                
        //Test assertions
        log("Testing assertions.");
        MasterController mc0 = MCFactory.getMasterController();
        EnterpriseObject eo2 = mc0.getEnterpriseObject(eo.getEUID());
        //assertTrue(eo2.getStatus().equals(SystemObject.STATUS_INACTIVE));
        Collection c = eo2.getSystemObjects();
        assertTrue(c.size() == 2);
        Iterator i = c.iterator();
        String system = null;
        String lid = null;
        while (i.hasNext()) {
            SystemObject so = (SystemObject) i.next();
            system = so.getSystemCode();
            lid = so.getLID();
            assertTrue(so.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        }
        
        //Check that EO can still be looked up
        SystemObjectPK key = new SystemObjectPK(system, lid);
        String euid = mc0.getEUID(key);
        assertTrue(euid.equals(eo.getEUID()));
        
        return euid;
    }

    /** Tests deactivate system object
     * @throws Exception an error occured
     */
    public void testActivateEnterpriseObject1() throws Exception {
        String euid = testDeactivateEnterpriseObject1();
        //Activate EO
        ActivateEnterpriseObjectHelper helper = new ActivateEnterpriseObjectHelper();
        String[] command = new String[] {"euid=" + euid}; 
        helper.run(command);
        log("Activate completed.");
                
        //Test assertions
        log("Testing assertions.");
        MasterController mc0 = MCFactory.getMasterController();
        EnterpriseObject eo2 = mc0.getEnterpriseObject(euid);
        assertTrue(eo2.getStatus().equals(SystemObject.STATUS_ACTIVE));
        Collection c = eo2.getSystemObjects();
        assertTrue(c.size() == 2);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            SystemObject so = (SystemObject) i.next();
            assertTrue(so.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        }
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
                ts.addTest(new DeactivateEnterpriseObject(args[i]));
            }
        } else {
            ts = new TestSuite(DeactivateEnterpriseObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
