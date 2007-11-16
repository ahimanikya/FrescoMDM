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
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.DeleteSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import java.util.Collection;
import java.util.List;
import com.sun.mdm.index.objects.EnterpriseObject;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class DeleteSystemObject extends TestCase {
    
    private MasterController mc;
        
    private static final String SYSTEM = "SiteA";
    private static final String LID1 = "0001";
    private static final String LID2 = "0002";
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeleteSystemObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Tests delete system object
     * @throws Exception an error occured
     */
    public void testDeleteSystemObject() throws Exception {
        //Insert Data
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        String[] command = {"fileName=DeleteSystemObject1.txt", "fileType=generic"};
        List eoList = createHelper.run(command);
        EnterpriseObject eo = (EnterpriseObject) eoList.get(0);
        Collection c = eo.getSystemObjects();
        assertTrue(c.size() == 2);
        
        //Delete SO
        DeleteSystemObjectHelper helper = new DeleteSystemObjectHelper();
        command = new String[] {"systemCode=" + SYSTEM, "lid=" + LID1}; 
        helper.run(command);
        log("Delete completed.");
                
        //Test assertions
        log("Testing assertions.");
        MasterController mc0 = MCFactory.getMasterController();
        EnterpriseObject eo2 = mc0.getEnterpriseObject(eo.getEUID());
        c = eo2.getSystemObjects();
        assertTrue(c.size() == 1);
                
    }
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DeleteSystemObject.class));
    }
    
}
