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

package com.sun.mdm.index.objects.validation;

import com.sun.mdm.index.objects.ObjectField;

import junit.framework.TestCase;

/**
 * test object field
 * @author jlong
 */
public class FieldDescriptorTest extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public FieldDescriptorTest (String name) {
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
    public void testFieldDescriptor() throws Exception {

        FieldDescriptor fd = new FieldDescriptor("Test", ObjectField.OBJECTMETA_STRING_TYPE);
        ObjectField of = new ObjectField("Test", ObjectField.OBJECTMETA_STRING_TYPE, "Data1");
        try {
            fd.validate(of, true);
            // This will change the field type to ObjectField.OBJECTMETA_LINK_TYPE
            of.setValue("[Data2]");
            fd.validate(of, true);
        } catch (Exception e) {
            System.out.println("Error during field descriptor validation test: " + e.getMessage());
            assertTrue(false);
        }
    }
}
