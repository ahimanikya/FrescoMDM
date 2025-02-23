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

import java.io.IOException;
        
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;        
import javax.servlet.ServletException;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 * ManageRelAddRelationshipAttributesController class
 * @author Narahari
 */
public class ManageRelAddRelationshipAttributesController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException { 
        
        System.out.println("Getting Rel Add Relationship Attributes...");
        String value = "add_relationship_attributes";
        return new ModelAndView("/manage/relationship/add_relationship_attributes", "add_relationship_attributes", value);
    }

}
