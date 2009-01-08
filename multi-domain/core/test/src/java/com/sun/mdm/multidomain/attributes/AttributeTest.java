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
package com.sun.mdm.multidomain.attributes;

import java.util.Map;
import java.util.HashMap;
import junit.framework.TestCase;

/**
 * AttributeTest
 * @author cye
 */
public class AttributeTest extends TestCase {

    public AttributeTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        Attribute attr1 = new Attribute();
        attr1.setName("foo");
        Attribute attr2 = new Attribute();
        attr2.setName("foo");
        assertTrue(attr1.equals(attr2));            
        Map<Attribute, String> attributes = new HashMap<Attribute, String>();
        attributes.put(attr1, "foo");
        boolean contained = attributes.containsKey(attr2);
        assertTrue(contained);                            
        String value1 = attributes.get(attr2);
        assertTrue(value1 != null);                            
    }
}
