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

import java.util.List;
import java.util.ArrayList;        
import junit.framework.TestCase;

import com.sun.mdm.multidomain.relationship.RelationshipDef;

/**
 * DomainRelationshipDefObjectTest class.
 * @author cye
 */
public class DomainRelationshipDefObjectTest extends TestCase {

    public DomainRelationshipDefObjectTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        
        RelationshipDef rDef1 = new RelationshipDef();
        rDef1.setName("FOO1");
        rDef1.setSourceDomain("sourceDomain");
        rDef1.setTargetDomain("targetDomain");
        
        RelationshipDef rDef2 = new RelationshipDef();
        rDef2.setName("FOO2");
        rDef2.setSourceDomain("targetDomain");
        rDef2.setTargetDomain("sourceDomain");
        
        List<DomainRelationshipDefObject> types = new ArrayList<DomainRelationshipDefObject>();
        int index = types.indexOf(new DomainRelationshipDefObject("sourceDomain"));
        if(index == - 1) {
            DomainRelationshipDefObject value = new DomainRelationshipDefObject("sourceDomain");
            types.add(value);  
            index = types.indexOf(value);
            assertTrue(true);
        } else {
            assertTrue(false);
        }
        DomainRelationshipDefObject value = types.get(index);
        value.add(rDef1);
        index = types.indexOf(new DomainRelationshipDefObject("sourceDomain"));
        if(index == - 1) {
             assertTrue(false);
        } else {
             assertTrue(true);
        }
        value = types.get(index);
        value.add(rDef2);
        assertTrue(types.size() == 1);
    }

}