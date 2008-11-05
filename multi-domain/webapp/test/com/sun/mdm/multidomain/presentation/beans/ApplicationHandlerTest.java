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
package com.sun.mdm.multidomain.presentation.beans;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;

import com.sun.mdm.multidomain.services.core.ConfigException;

/**
 * ApplicationHandlerTest class.
 * @author cye
 */
public class ApplicationHandlerTest extends TestCase {

    public ApplicationHandlerTest(String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
       try {
        ApplicationHandler handler = new ApplicationHandler(); 
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addUserRole("MultiDomain.Admin");
        handler.initialize(request);
        handler.logout();
        assertTrue(true);
       } catch(ConfigException cex) {
           fail(cex.getMessage());
       }
    }

    public void test002() {
       try {
        ApplicationHandler handler = new ApplicationHandler(); 
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addUserRole("MultiDomain.Unknown");
        handler.initialize(request);
        handler.logout();
        fail("unexpected!");
       } catch(ConfigException cex) {
        assertTrue(true);  
       }
    }    
}
