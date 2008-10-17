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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LoginHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="javax.faces.context.FacesContext" %>

<% ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());  %>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
        </head>
        <title><%=bundle.getString("login_error_heading_text")%></title>  
        <body>
                        
            <div id="content">  <!-- Main content -->
            <table border="0" cellpadding="3" cellspacing="1">
                <tr>
                    <td> 

                       <%if(request.getParameter("na") != null) {%>
                        <div class="errorHeadMessage"> <%=bundle.getString("not_authorized_head")%></div> 
                        <div id="errorSummary"  class="errorSummary"> 
                            <p>
                               <%=request.getParameter("na")%>
                            </p>
							<p>
							   <%=bundle.getString("check_permissions")%>&nbsp;<a title="<%=bundle.getString("login_submit_button_prompt")%>" href="login.jsp"> <%=bundle.getString("login_try_again_text")%>  </a>&nbsp;<%=bundle.getString("login_after_issue_resolved")%>
                               
                            </p>
                        </div>

                       <%} else if(request.getParameter("error") != null) {%>
                        <div class="errorHeadMessage"> <%=bundle.getString("login_user_login_init_load_message")%></div> 
                        <div id="errorSummary"  class="errorSummary"> 
                            <p>
                               <%=request.getParameter("error")%>
                            </p>
							<p>
							   <%=bundle.getString("contact_system_admin_text")%>
                               <a title="<%=bundle.getString("login_try_again_text")%>" href="login.jsf"> <%=bundle.getString("login_try_again_text")%></a> <%=bundle.getString("login_after_issue_resolved")%>
                            </p>
                        </div>
                      <%} else {%>
					   <div class="errorHeadMessage"><%=bundle.getString("login_error_heading_text")%></div> 
                        <div id="errorSummary" class="errorSummary" > 
                            <p>
                               <%=bundle.getString("login_user_login_failure_message")%> <a title="<%=bundle.getString("login_try_again_text")%>" href="login.jsf"> <%=bundle.getString("login_try_again_text")%></a>
                            </p>
                        </div>

					  <%}%>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
	<%  //invalidate the session and logut the user to start over once again
	    request.setAttribute("Logout", "LoggedOut");
        request.getSession().invalidate(); 
             
	%>
            
        </body>
    </html>
    
