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
package com.sun.mdm.multidomain.services.relationship;

import com.sun.mdm.multidomain.services.model.AttributeDefExt;

import junit.framework.TestCase;

/**
 * RelationshipDefinitionTest class.
 * @author cye
 */
public class RelationshipDefExtTest extends TestCase {

    public RelationshipDefExtTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        RelationshipDefExt rDef = new RelationshipDefExt();
        rDef.setName("FOO");
        assertTrue("FOO".equals(rDef.getName()));    
        rDef.setBiDirection("true");
        assertTrue("true".equals(rDef.getBiDirection()));
        rDef.setBiDirection("true");
        assertTrue("true".equals(rDef.getBiDirection()));        
    }

    public void test002() {
        RelationshipDefExt rDef = new RelationshipDefExt();
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("FOO");
        rDef.setExtendedAttribute(aDef);
        assertTrue("FOO".equals(rDef.getExtendeddAttribute("FOO").getName()));
    }
    
}
