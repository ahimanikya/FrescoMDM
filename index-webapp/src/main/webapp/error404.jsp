<%--
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

--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="java.util.Locale"  %>
<%
    String selectedLocale  = (String) session.getAttribute("selectedLocale");
 
	Locale locale = Locale.US;

	if("English".equalsIgnoreCase(selectedLocale)) {
        locale = Locale.US;
 	} else if("German".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.GERMANY;
 	} else if("France".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.FRANCE;
 	} else if("Japanese".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.JAPANESE;
 	} else if("Chinese".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.SIMPLIFIED_CHINESE;
 	} else {
       locale = Locale.US;
  	}
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,locale);
    %>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
        </head>
        <title><%=bundle.getString("error_404_title")%></title>  
        <body>
                        
            <div id="content">  <!-- Main content -->
            <table border="0" cellpadding="3" cellspacing="1">
                <tr>
                    <td> 
                        <div class="errorHeadMessage"> <%=bundle.getString("error_404_page_not_found")%> </div> 
                        <div id="errorSummary" class="errorSummary">  
                            <p><%=bundle.getString("error_404_text")%></p>
                        </div>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
        
        </body>
    </html>
    
