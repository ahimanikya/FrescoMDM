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

                       <%if(request.getParameter("error") != null) {%>
                        <div class="errorHeadMessage"> <%=bundle.getString("login_user_login_init_load_message")%></div> 
                        <div id="errorSummary"  class="errorSummary"> 
                            <p>
                               <%=request.getParameter("error")%>
                            </p>
							<p>
							   <%=bundle.getString("contact_system_admin_text")%>
                               <a title="<%=bundle.getString("login_try_again_text")%>" href="login.jsf"> <%=bundle.getString("login_try_again_text")%>  </a>
                            </p>
                        </div>
                      <%} else {%>
					   <div class="errorHeadMessage"><%=bundle.getString("login_error_heading_text")%></div> 
                        <div id="errorSummary" class="errorSummary" > 
                            <p>
                               <%=bundle.getString("login_user_login_failure_message")%>
                              <a title="<%=bundle.getString("login_try_again_text")%>" href="login.jsf"> <%=bundle.getString("login_try_again_text")%>  </a>
                            </p>
                        </div>

					  <%}%>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
            
        </body>
    </html>
    
