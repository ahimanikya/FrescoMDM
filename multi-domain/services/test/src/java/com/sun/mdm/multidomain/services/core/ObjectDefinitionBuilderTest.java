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
package com.sun.mdm.multidomain.services.core;

import java.io.InputStream;

import junit.framework.TestCase;

import com.sun.mdm.multidomain.services.model.ObjectDefinition;
import com.sun.mdm.multidomain.services.core.ObjectFactory;

/**
 * ObjectDefinitionBuidlerTest class.
 * @author cye
 */
public class ObjectDefinitionBuilderTest extends TestCase {
    
    public ObjectDefinitionBuilderTest(String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        
        ObjectDefinitionBuilder builder = new ObjectDefinitionBuilder();
        InputStream  stream = ObjectFactory.class.getResourceAsStream("/Domains/Person/object.xml");      
        
        ObjectDefinition person = builder.parse(stream);
        assertTrue(person.getName().equals("Person"));
        
        ObjectDefinition address = person.getchild("Address");
        assertTrue(address.getName().equals("Address"));
        
        ObjectDefinition alias = person.getchild("Alias");
        assertTrue(alias.getName().equals("Alias"));
        
        ObjectDefinition phone = person.getchild("Phone");
        assertTrue(phone.getName().equals("Phone"));
    }
}
