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

import java.util.ArrayList;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.ObjectField;


/** tester
 * @author ckuo
 */
public class EPathBuilderTest extends TestCase {
    
    /** constructor
     * @param testName test name
     */    
    public EPathBuilderTest(java.lang.String testName) {
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
        TestSuite suite = new TestSuite(EPathBuilderTest.class);
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}

    /** test
     * @throws Exception error
     */    
    public void testBothNull() throws Exception {
        String s = EPathBuilder.createEPath(null, null);
        assertEquals(s, "*");
    }
    
    /** test
     * @throws Exception error
     */    
    public void testObjectNull() throws Exception {
        String a = "blahblah";
        String s = EPathBuilder.createEPath(null, a);
        assertEquals(s, a);
    }
    
    /** test
     * @throws Exception error
     */    
    public void testFullNode() throws Exception {
        ObjectNode o = createObject();
        
        ObjectNode n = o.getChild("Level1", 0).getChild("Level2", 0);
        
        String s;
        s = EPathBuilder.createEPath(n, "LastField");
        assertNotNull(s);
    }
    
    /** create object
     * @throws Exception error
     * @return an object node
     */    
    private ObjectNode createObject() throws Exception {
        ArrayList names = new ArrayList();
        ArrayList types = new ArrayList();
        ArrayList values = new ArrayList();
        
        names.add(0, "Key1");
        types.add(0, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        values.add(0, "Value1");

        names.add(1, "Key2");
        types.add(1, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        values.add(1, "Value2");

        names.add(2, "Key3");
        types.add(2, new Integer(ObjectField.OBJECTMETA_STRING_TYPE));
        values.add(2, "Value3");
        
        ObjectKey key = new ObjectKey(names, types, values);

        ObjectNode root = new ObjectNode("RootObject", names, types, values);
        ObjectNode lvl1 = new ObjectNode("Level1", names, types, values);
        lvl1.setKeyType("Key1", true);
        lvl1.setKeyType("Key2", true);
        lvl1.setKeyType("Key3", true);

        ObjectNode lvl2 = new ObjectNode("Level2", names, types, values);
        lvl2.setKeyType("Key1", true);
        lvl2.setKeyType("Key2", true);
        lvl2.setKeyType("Key3", true);
        lvl2.setParent(lvl1);
        lvl1.addChild(lvl2);
        lvl1.setParent(root);
        root.addChild(lvl1);
        return root;
    }
}
