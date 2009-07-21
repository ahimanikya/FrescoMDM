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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author sdua
 */
public class CodeRegistryTest extends TestCase {
    private static CodeRegistry mCodeRegistry;
    
    
    /**
     * Creates a new instance of CodeRegistryTest
     *
     * @param name name of test
     */
    public CodeRegistryTest(String name) {
        super(name);
    }
    
    /**
     * setUp
     *@exception Exception error occurs
     **/
    protected void setUp() throws Exception {
    
          if (mCodeRegistry == null) {
			Connection con = MCFactory.getConnection();
			mCodeRegistry = CodeRegistry.getInstance(con);  
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
    	  
         junit.textui.TestRunner.run(new TestSuite(CodeRegistryTest.class));

    }
    
  

    /**
     * test getCodeMap
     *
     * @exception Exception error occurs
     */
    public void testGetCodeMap() throws Exception {
        

		Map allCodes = mCodeRegistry.getCodeMap();
		System.out.println(allCodes);
		assertTrue(allCodes.size() > 0);
		Object map = allCodes.get("STATE");
		assertTrue(map instanceof Map);
		Map stateMap = (Map) map;
		assertTrue(stateMap.size() > 0);
    }

    /**
     * test getCodesByModule
     *
     * @exception Exception error occurs
     */
    public void testGetCodesByModule() throws Exception {
        

		ArrayList religionCodes = mCodeRegistry.getCodesByModule("RELIGION");
		assertTrue(religionCodes.size() > 0);
		ArrayList rubbishCodes = mCodeRegistry.getCodesByModule("RUBBISH");
		assertTrue(rubbishCodes.size() == 0);
    }

    /**
     * test getCodeMapByModule
     *
     * @exception Exception error occurs
     */
    public void testGetCodeMapByModule() throws Exception {
        

		Map phTypeCodes = mCodeRegistry.getCodeMapByModule("PHONTYPE");
		assertTrue(phTypeCodes != null && phTypeCodes.size() > 0);
		Map rubbishCodes = mCodeRegistry.getCodeMapByModule("RUBBISH");
		assertTrue(rubbishCodes == null);
    }

    /**
     * test hasCode
     *
     * @exception Exception error occurs
     */
    public void testHasCode() throws Exception {
        

		assertTrue(mCodeRegistry.hasCode("STATE", "AZ"));
		assertTrue(!mCodeRegistry.hasCode("STATE", "AUS"));
    }

    /**
     * test hasModule
     *
     * @exception Exception error occurs
     */
    public void testHasModule() throws Exception {
        

		assertTrue(mCodeRegistry.hasModule("COUNTRY"));
		assertTrue(!mCodeRegistry.hasModule("RUBBISH"));
    }

    /**
     * test hasReset
     *
     * @exception Exception error occurs
     */
    public void testReset() throws Exception {
        
		Connection con = MCFactory.getConnection();
		CodeRegistry cr1 = null;
		CodeRegistry cr2 = null;
		// Confirm that the instance we get is still the original object
		cr1 = CodeRegistry.getInstance(con);
		assertTrue(cr1.equals(mCodeRegistry));
		// Reset and then confirm that we get a new object instance
		mCodeRegistry.reset();
		cr2 = CodeRegistry.getInstance(con);
		assertTrue(!cr2.equals(mCodeRegistry));
		mCodeRegistry = cr2;
    }

}
