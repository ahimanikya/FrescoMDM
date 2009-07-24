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
package com.sun.mdm.index.codelookup;


import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.SystemDefinition;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sdua
 */
public class SystemRegistryTest extends TestCase {
    private static SystemRegistry mSystemRegistry;
    
    
    /**
     * Creates a new instance of SystemRegistryTest
     *
     * @param name name of test
     */
    public SystemRegistryTest(String name) {
        super(name);
    }
    
    /**
     * setUp
     *@exception Exception error occurs
     **/
    protected void setUp() throws Exception {
    
          if (mSystemRegistry == null) {
			Connection con = MCFactory.getConnection();
			mSystemRegistry = SystemRegistry.getInstance(con);  
          }
    	
     
    }

    /**
     * Used by JUnit (called after each test method)
     */
    protected void tearDown() {
        //mQueryManager = null;
    }

    /** main method
     * @param args input arguements
     * @exception Exception error occurs
     *
     */
    public static void main(String[] args) throws Exception {
    	  
         junit.textui.TestRunner.run(new TestSuite(SystemRegistryTest.class));

    }
    
  

    /**
     * test getCodeMap
     *
     * @exception Exception error occurs
     */
    public void testGetCodeMap() throws Exception {
        

		List allSystems = mSystemRegistry.getSystemDefinitionList();
		System.out.println(allSystems);
		assertTrue(allSystems.size() > 0);
    }

    /**
     * test getCodesByModule
     *
     * @exception Exception error occurs
     */
    public void testGetSystemDefinition() throws Exception {
        

		SystemDefinition sd = mSystemRegistry.getSystemDefinition("RUBBISH");
		assertTrue(sd == null);

		sd = mSystemRegistry.getSystemDefinition("SBYN");
		assertTrue(sd != null && sd.getDescription() != null && sd.getDescription().startsWith("SeeBeyond"));
    }

    /**
     * test hasSystemCode
     *
     * @exception Exception error occurs
     */
    public void testHasCode() throws Exception {
        

		assertTrue(mSystemRegistry.hasSystemCode("SiteA"));
		assertTrue(!mSystemRegistry.hasSystemCode("RUBBISH"));
    }

    /**
     * test hasReset
     *
     * @exception Exception error occurs
     */
    public void testReset() throws Exception {
        
		Connection con = MCFactory.getConnection();
		SystemRegistry cr1 = null;
		SystemRegistry cr2 = null;
		// Confirm that the instance we get is still the original object
		cr1 = SystemRegistry.getInstance(con);
		assertTrue(cr1.equals(mSystemRegistry));
		assertTrue(cr1.isCurrent() && mSystemRegistry.isCurrent());
		// Reset and then confirm that we get a new object instance
		mSystemRegistry.reset();
		cr2 = SystemRegistry.getInstance(con);
		assertTrue(!cr2.equals(mSystemRegistry));
		assertTrue(!mSystemRegistry.isCurrent() && !cr1.isCurrent());
		mSystemRegistry = cr2;
    }

}
