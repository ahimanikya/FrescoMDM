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

import junit.framework.TestCase;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;

/**
 * ObjectFactoryRegistryTest class.
 * @author cye
 */
public class ObjectFactoryRegistryTest extends TestCase {

    public ObjectFactoryRegistryTest(String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        try {
            ObjectFactory objectFactory1 = ObjectFactoryRegistry.lookup("FOO");        
            ObjectFactory objectFactory2 = ObjectFactoryRegistry.lookup("FOO");                    
            assertTrue(objectFactory1 == objectFactory2);
        } catch (ConfigException cex){            
            fail(cex.getMessage());
        }
    }    

    public void test002() {
        try {
            ObjectFactory objectFactory = ObjectFactoryRegistry.lookup("Person");        
            
            ObjectNode personObject = objectFactory.create("Person");
            assertTrue("Person".equals(personObject.pGetTag()));  
                        
            ObjectNode addressObject = (ObjectNode)(personObject.pGetChildren("Address").get(0));
            assertTrue("Address".equals(addressObject.pGetTag())); 
            assertTrue("Person".equals(addressObject.getParent().pGetTag()));

            ObjectNode phoneObject = (ObjectNode)(personObject.pGetChildren("Phone").get(0));
            assertTrue("Phone".equals(phoneObject.pGetTag())); 
            assertTrue("Person".equals(phoneObject.getParent().pGetTag()));

            ObjectNode aliasObject = (ObjectNode)(personObject.pGetChildren("Alias").get(0));
            assertTrue("Alias".equals(aliasObject.pGetTag())); 
            assertTrue("Person".equals(aliasObject.getParent().pGetTag()));

        } catch(ObjectException oex) {
            fail(oex.getMessage());            
        } catch (ConfigException cex){            
            fail(cex.getMessage());
        }
    }    

    public void test003() {
        try {
            ObjectFactory objectFactory = ObjectFactoryRegistry.lookup("Company");        
            
            ObjectNode personObject = objectFactory.create("Company");
            assertTrue("Company".equals(personObject.pGetTag()));  
                        
            ObjectNode addressObject = (ObjectNode)(personObject.pGetChildren("Address").get(0));
            assertTrue("Address".equals(addressObject.pGetTag())); 
            assertTrue("Company".equals(addressObject.getParent().pGetTag()));

            ObjectNode phoneObject = (ObjectNode)(personObject.pGetChildren("Phone").get(0));
            assertTrue("Phone".equals(phoneObject.pGetTag())); 
            assertTrue("Company".equals(phoneObject.getParent().pGetTag())); 
                        
        } catch(ObjectException oex) {
            fail(oex.getMessage());            
        } catch (ConfigException cex){            
            fail(cex.getMessage());
        }
    }    
    
}
