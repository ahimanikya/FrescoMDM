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
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;

/** Test class for looking up a record by LID
 * @author Raymond Tam
 */
public class LookupLid extends TestCase {
    
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
    
    
    /** Creates new instance
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupLid(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Tests valid lookup by Lid
     * @throws Exception an error occured
     */
    public void testLookupLid() throws Exception {

        String defaultSystem = "SBYN";
        String defaultLid = "4568915615";
        
        boolean rc = run(defaultSystem, defaultLid);
        assertTrue(rc);
    }
    
    /** Tests lookup by LID.  
     *
     * @param tmpSystem  system to search 
     * @param tmpLid  local id to search
     * @throws  Exception if an object error occurs
     * @return  true if successful, false otherwise
     */    
    public static boolean run(String tmpSystem, 
                              String tmpLid) throws Exception {
        
        final int expectedCount = 1;        //  number of expected records 
    
        System.out.println("\n---LookupLid---\n");
        
        //  set the class members
        if (system == null && tmpSystem != null)  {
            system = tmpSystem;
        }
        if (lid == null && tmpLid != null)  {
            lid = tmpLid;
        }
        
        //  add a test record
        AddRecordHelper addRecordHelper = new AddRecordHelper();
        addRecordHelper.clearDb();
        boolean rc = addRecordHelper.run(system, lid, null);
        if (!rc)  {
            System.out.println("Test failed: addRecordHelper.run() returned false");
            return false;
        }
        
        controller = MCFactory.getMasterController();
        System.out.println("Attempting an LID lookup");
        SystemObject newSysObj = controller.getSystemObject(new SystemObjectPK(system, lid));
        if (newSysObj != null)  {
            System.out.println("Test succeeded\n");
            return true;
        }
        System.out.println("Test failed\n");
        return false;
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
        junit.textui.TestRunner.run(new TestSuite(LookupLid.class));
    }
    
}
