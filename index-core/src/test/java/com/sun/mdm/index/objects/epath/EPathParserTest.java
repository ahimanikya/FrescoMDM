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
package com.sun.mdm.index.objects.epath;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;


/** test
 * @author ckuo
 */
public class EPathParserTest extends TestCase {
    
    /** constructor
     * @param testName test name
     */
    public EPathParserTest(java.lang.String testName) {
        super(testName);
    }
    
    /** main
     * @param args args
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    /** suite
     * @return suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(EPathParserTest.class);
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    /** test
     * @throws Exception error
     */
    public void testBasic() throws Exception {
        EPath e = EPathParser.parse("Enterprise.SystemSBR.Person.FirstName");
        assertNotNull(e);
    }
    
    /** test
     * @throws Exception error
     */
    public void testIndex() throws Exception {
        EPath e = EPathParser.parse("Person.Address[1].Someobject[2].*");
        assertNotNull(e);
        
        EPath e1 = EPathParser.parse("Person.Address[ 1].Someobject[ 2 ].*");
        assertNotNull(e1);
    }
    
    /** test
     * @throws Exception error
     */
    public void testFilter() throws Exception {
        EPath e = EPathParser.parse("Person.Address[City = Monrovia].Zip");
        assertNotNull(e);
        
        EPath e1 = EPathParser.parse("Person.Address[City=Monrovia].Zip");
        assertNotNull(e1);
        
        EPath e2 = EPathParser.parse("Person.Address[City= Monrovia].Zip");
            assertNotNull(e2);
    }
    
    /** test
     * @throws Exception error
     */
    public void testKey() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where].*");
        assertNotNull(e4);
        
        EPath e5 = EPathParser.parse("Person.Address[@somekey=who].*");
        assertNotNull(e5);
        
        EPath e52 = EPathParser.parse("Person.Address[@somekey = who].*");
        assertNotNull(e52);
    }
    
    /** test
     * @throws Exception error
     */
    public void testWild() throws Exception {
        EPath e3 = EPathParser.parse("Person.Address[*].AddressLine1");
        assertNotNull(e3);
        EPath e4 = EPathParser.parse("Root.Object1[*].Object2[*].Field1");
        assertNotNull(e4);
    }
    
                /*
    public void testBadPath() throws Exception {
        try {
            EPath e1 = EPathParser.parse("Person#Address[*]#AddressLine1");
        } catch (Exception ex) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }
                 */

    public void testKeyForSpecialCharactersOne() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where-Dash].*");
        assertTrue(e4.ops[1] == EPath.OP_SECONDARY_BY_KEY);        
    }

    public void testKeyForSpecialCharactersTwo() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where/Slash].*");
        assertTrue(e4.ops[1] == EPath.OP_SECONDARY_BY_KEY);        
    }

    public void testKeyForSpecialCharactersThree() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where'SingleQuote].*");
        assertTrue(e4.ops[1] == EPath.OP_SECONDARY_BY_KEY);        
    }

    public void testKeyForSpecialCharactersFour() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where spaces].*");
        assertTrue(e4.ops[1] == EPath.OP_SECONDARY_BY_KEY);        
    }
    
	public void testKeyForSpecialCharactersFive() throws Exception {
        EPath e4 = EPathParser.parse("Person.Address[@somekey=where\"doublequote].*");
        assertTrue(e4.ops[1] == EPath.OP_SECONDARY_BY_KEY);        
    }
    
    
    /** test
     * @throws Exception error
     */
    public void testToString() throws Exception {
        EPath e3 = EPathParser.parse("Person.Address[*].AddressLine1");
        e3.toString();
    }
    
}
