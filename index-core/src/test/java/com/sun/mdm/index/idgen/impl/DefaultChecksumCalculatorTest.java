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
import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for DefaultChecksumCalculator
 * @author  gw194542
 */
public class DefaultChecksumCalculatorTest extends TestCase {
    
    /** Creates a new instance of DecisionMakerResultTest 
     * @param name test name
     */
    public DefaultChecksumCalculatorTest(String name) {
        super(name);
    }
    
    /** Test constructor and methods
     * @throws Exception exception
     */
    public void testClass() throws Exception {
	DefaultChecksumCalculator dckc = new DefaultChecksumCalculator();
	dckc.setParameter("ChecksumLength", new Integer(1));
	assertTrue(dckc.calcChecksum("0000").equals("5"));
	assertTrue(dckc.calcChecksum("10000000").equals("1"));
    }
    
    /** Main entry point
     * @param args args
     */
     
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DefaultChecksumCalculatorTest.class));
    }    
    
    
}
