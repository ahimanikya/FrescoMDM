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
import com.sun.mdm.multidomain.presentation.beans.ViewModelHandler;
import com.sun.mdm.multidomain.presentation.model.ViewModel;
import com.sun.mdm.multidomain.presentation.model.ViewModel.ModelObject;
        
/**
 * AdministerAddRelationshipDefController class.
 * @author Harish
 */
public class AdministerAddRelationshipDefController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        ViewModelHandler handler = (ViewModelHandler)request.getSession().getAttribute("viewModelHandler");
        ViewModel<String> model = handler.getAddRelationshipModel();
        String modelName = model.getModelName();
        ModelObject<String> modelObject = model.getModelObject();
        return new ModelAndView("administration/add_relationship", modelName, modelObject.getObjects());
    }    
}
