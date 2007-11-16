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

package com.sun.mdm.index.objects;

import junit.framework.TestCase;

/**
 * test object field
 * @author jlong
 */
public class ObjectFieldTest extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ObjectFieldTest (String name) {
        super(name);
    }
    
    /**
     * setup
     */
    public void setUp() {
    }
    
    /**
     * tester
     * @exception Exception exception
     */
    public void testObjectField() throws Exception {

        System.out.println("ObjectField.getClass: " + ObjectField.getClass(ObjectField.OBJECTMETA_CHAR_TYPE));
        assertTrue(ObjectField.getClass(ObjectField.OBJECTMETA_CHAR_TYPE).toString().endsWith("java.lang.Character"));
 
        System.out.println("ObjectField.getTypeString: " + ObjectField.getTypeString(ObjectField.OBJECTMETA_CHAR_TYPE));
    	assertTrue(ObjectField.getTypeString(ObjectField.OBJECTMETA_CHAR_TYPE).compareTo(ObjectField.OBJECTMETA_CHAR_STRING) == 0);

        System.out.println("ObjectField.isValueValid: " + ObjectField.isValueValid(ObjectField.OBJECTMETA_CHAR_TYPE, new Character('Y')));
    	assertTrue(ObjectField.isValueValid(ObjectField.OBJECTMETA_CHAR_TYPE, new Character('Y')));

        ObjectField of1 = new ObjectField("Char1", ObjectField.OBJECTMETA_CHAR_TYPE, new Character('W'));
        ObjectField of2 = new ObjectField("Char2", ObjectField.OBJECTMETA_CHAR_TYPE, new Character('W'));
        System.out.println("ObjectField.equals: " + of1 + " : " + of2 + " : " + ObjectField.equals(of1, of2));        
        assertTrue(ObjectField.equals(of1, of2));
    }
}
