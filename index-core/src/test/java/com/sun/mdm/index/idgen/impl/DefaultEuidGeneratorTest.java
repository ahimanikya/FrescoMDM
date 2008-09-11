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
package com.sun.mdm.index.idgen.impl;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.sql.Connection;

/**
 * Test class for DefaultEUIDGenerator
 * @author  gw194542
 */
public class DefaultEuidGeneratorTest extends TestCase {
    
    /** Creates a new instance of DecisionMakerResultTest 
     * @param name test name
     */
    public DefaultEuidGeneratorTest(String name) {
        super(name);
    }
    
    /** Test constructor and methods
     * @throws Exception exception
     */
    public void testClass() throws Exception {
	TestUtils tu = new TestUtils();
	tu.clearDB();
	DefaultEuidGenerator dckc = new DefaultEuidGenerator();
	dckc.setParameter("IdLength", new Integer(8));
	dckc.setParameter("ChecksumLength", new Integer(1));
	dckc.setParameter("ChunkSize", new Integer(100));
	Connection con = tu.getConnection();

	assertTrue(dckc.getNextEUID(con).equals("000000005"));
	assertTrue(dckc.getNextEUID(con).equals("000000018"));
        
        dckc.setParameter("ChecksumCalculatorClass",
                "com.sun.mdm.index.idgen.impl.SampleChecksumCalculator");
        assertTrue(dckc.getNextEUID(con).equals("000000025"));
        assertTrue(dckc.getNextEUID(con).equals("000000032"));
        assertTrue(dckc.getNextEUID(con).equals("000000057"));
        assertTrue(dckc.getNextEUID(con).equals("000000064"));
    }
    
    /** Main entry point
     * @param args args
     */
     
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DefaultEuidGeneratorTest.class));
    }    
    
    
}
