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
package com.sun.mdm.multidomain.presentation.web;

import junit.framework.TestCase;

import java.util.Map;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * AdministrationControllerTest class.
 * @author cye
 */
public class AdministrationControllerTest extends TestCase {

    public AdministrationControllerTest(String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
       try {
        AdministrationController controller = new AdministrationController();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
         
        ModelAndView mv = controller.handleRequest(request, response);
     
        assertTrue("administration".equals(mv.getViewName()));
        
        Map mMap = mv.getModelMap();
        
        String value = (String)mMap.get("administration");
        assertTrue("administration".equals(value));
       
       } catch(Exception ex) {
           fail(ex.getMessage());
       }        
    }
}
