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
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.SystemDefinition;

/** Test class for delete system object method
 * @author Dan Cidon
 */
public class LookupSystemDefinitions extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public LookupSystemDefinitions(String name) {
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
        MasterController mc = MCFactory.getMasterController();
        SystemDefinition[] defs = mc.lookupSystemDefinitions();
        assertTrue(defs.length > 0);    
        for (int i = 0; i < defs.length; i++) {
            System.out.println(defs[i]);
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(LookupSystemDefinitions.class));
    }
    
}
