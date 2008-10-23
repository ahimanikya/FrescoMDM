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
package com.sun.mdm.multidomain.services.control;

import java.util.List;
import java.util.ArrayList;
import junit.framework.TestCase;

import com.sun.mdm.multidomain.services.relationship.Attribute;
import com.sun.mdm.multidomain.services.relationship.AttributeList;

/**
 * AttributeListTest class.
 * @author cye
 */
public class AttributeListTest extends TestCase {
    
    public AttributeListTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("FOO1", "FOO1"));
        attributes.add(new Attribute("FOO2", "FOO2"));
        attributes.add(new Attribute("FOO3", "FOO3"));
        AttributeList aList = new AttributeList();
        aList.setAttributes(attributes);
        assertTrue(aList.getSize() == 3);
        
        assertTrue(aList.hasNext());
        Attribute a = aList.next();
        assertTrue(a.getName().equals("FOO1"));
        assertTrue(a.getValue().equals("FOO1"));
        
        assertTrue(aList.hasNext());
        a = aList.next();
        assertTrue(a.getName().equals("FOO2"));
        assertTrue(a.getValue().equals("FOO2"));
        
        assertTrue(aList.hasNext());
        a = aList.next();
        assertTrue(a.getName().equals("FOO3"));
        assertTrue(a.getValue().equals("FOO3"));
        
    }
}
