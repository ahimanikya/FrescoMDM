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
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.master.MatchResult;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.SystemObject;
import java.util.List;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class LookupSystemObjectPKs extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupSystemObjectPKs(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
       ClearDb.run();
    }
    
    /** Tests valid creation
     * @throws Exception an error occured
     */
    public void test1() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List results = createHelper.run(new String[] {"fileName=LookupSystemObjectPks1.txt", "fileType=eiEvent"});
        MatchResult mr = (MatchResult) results.get(0);
        String euid = mr.getEUID();
        MasterController mc = MCFactory.getMasterController();
        SystemObjectPK[] keys = mc.lookupSystemObjectPKs(euid);
        assertTrue(keys.length == 3);
        keys = mc.lookupSystemObjectPKs("SiteA", "0001", "SiteB", SystemObject.STATUS_ACTIVE);
        assertTrue(keys.length == 2);        
    }

    
    /** Tests an invalid retrieval
     * @throws Exception an error occured
     */
    public void test2() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List results = createHelper.run(new String[] {"fileName=LookupSystemObjectPks1.txt", "fileType=eiEvent"});
        MatchResult mr = (MatchResult) results.get(0);
        String euid = mr.getEUID();
        MasterController mc = MCFactory.getMasterController();
        SystemObjectPK[] keys = mc.lookupSystemObjectPKs(euid);
        assertTrue(keys.length == 3);
        try {
            keys = mc.lookupSystemObjectPKs("SiteA", "0001", "SiteB", SystemObject.STATUS_INACTIVE);
        } catch (Exception e) {
            String errMesg = e.getMessage();
            String targetMesg = "No EUID found for System = SiteA, LID = 0001, Status = " 
                                    + SystemObject.STATUS_INACTIVE;
            assertTrue(errMesg != targetMesg);
        }
    }

    /** Tests an invalid retrieval
     * @throws Exception an error occured
     */
    public void test3() throws Exception {
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper(); 
        List results = createHelper.run(new String[] {"fileName=LookupSystemObjectPks1.txt", "fileType=eiEvent"});
        MatchResult mr = (MatchResult) results.get(0);
        String euid = mr.getEUID();
        MasterController mc = MCFactory.getMasterController();
        SystemObjectPK[] keys = mc.lookupSystemObjectPKs(euid);
        assertTrue(keys.length == 3);
        // deactivate one of the system objects
        SystemObjectPK systemKey = new SystemObjectPK("SiteB", "0001");
        mc.deactivateSystemObject(systemKey);
        keys = mc.lookupSystemObjectPKs("SiteA", "0001", "SiteB", SystemObject.STATUS_ACTIVE);
        assertTrue(keys.length == 1);
        assertTrue(keys[0].systemCode.equals("SiteB"));
        assertTrue(keys[0].lID.equals("0002"));
        keys = mc.lookupSystemObjectPKs("SiteA", "0001", "SiteB", SystemObject.STATUS_INACTIVE);
        assertTrue(keys.length == 1);
        assertTrue(keys[0].systemCode.equals("SiteB"));
        assertTrue(keys[0].lID.equals("0001"));
        keys = mc.lookupSystemObjectPKs("SiteB", "0001", "SiteA", SystemObject.STATUS_ACTIVE);
        assertTrue(keys.length == 1);
        assertTrue(keys[0].systemCode.equals("SiteA"));
        assertTrue(keys[0].lID.equals("0001"));
        keys = mc.lookupSystemObjectPKs("SiteB", "0001", "SiteA", SystemObject.STATUS_INACTIVE);
        assertTrue(keys == null);
    }

    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LookupSystemObjectPKs.class));
    }
    
}
