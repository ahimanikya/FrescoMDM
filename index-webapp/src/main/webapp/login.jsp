
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LoginHandler"  %>

<%@ page import="javax.faces.context.FacesContext"  %>

<f:view>    
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
<% 
LoginHandler loginHandler = new LoginHandler();
System.out.println("User Profile" + session.getAttribute("userProfile") + "USER IN SESSION" + session.getAttribute("user") );
if (request.getAttribute("Logout") == null && request.getRemoteUser() != null && request.isUserInRole("MasterIndex.Admin")) {
    FacesContext.getCurrentInstance().getExternalContext().redirect("results.jsf");
}
%>        
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <LINK REL="STYLESHEET" HREF="./css/styles.css"  TYPE="text/css">
        </head>
<script language="JavaScript">
    function submitAction() {
        document.loginform.submit();
    }

</script>        
        <body>         
            <center> 
                <div id="mainContent">
                <h:form id="localeForm">
                   <div id="localeDiv" style="padding-left:900px;">
                        <table width="300px" border="0">
                            <tr>
                                <td>
                                    <h:outputText style="font-size:10px;" value="#{msgs.locale_choose_lang}" />&nbsp;
                                    <h:selectOneMenu  id="langOption"  
                                                      value="#{LocaleHandler.langOption}"  
                                                      onchange="submit();"
                                                      valueChangeListener="#{LocaleHandler.localeChanged}"
                                                      style="font-size:10px;">
                                        <f:selectItem  itemValue="English" itemLabel="#{msgs.locale_english_text}" />
                                        <f:selectItem  itemValue="German" itemLabel="#{msgs.locale_german_text}"/>
                                        <f:selectItem  itemValue="France" itemLabel="#{msgs.locale_french_text}"/>
                                    </h:selectOneMenu>                                                       
                                    
                                </td>
                            </tr>
                        </table>
                    </div>
                </h:form>
            <div class="blueline">&nbsp;</div>
            
            <div>                         
                    <div id="log" class="loginForm">
                            <f:verbatim>
                            <form name="loginform" method="POST" action="j_security_check" focus="j_username">
                            </f:verbatim>
                            <table border="0" cellpadding="0" cellspacing="0">
                                <tbody>
                                    <tr>
                                        <!--alt=Enterprise Data Manager-->                                    
                                        <td colspan='2'><img src='images/spacer.gif' alt="Enterprise Data Manager" height='120px'></td>
                                    </tr>
                                    <tr>
                                        <td colspan='2'>
                                            <f:verbatim>
                                                <input type="text" name="j_username" size="25"/>
                                            </f:verbatim>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan='2'> 
                                            <f:verbatim>
                                                <input type="password" name="j_password" size="25" redisplay="false"/>
                                            </f:verbatim>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">                                          
                                            <f:verbatim>
                                                <a href="javascript:loginform.submit();" class="button"><span> Login </span></a>
                                            </f:verbatim>
                                        </td>
                                    </tr>
                                    <tr>
                                    <!--alt=Sun Microsystems Logo-->
                                    <td colspan='2'><img src='images/spacer.gif' alt="Sun Microsystems Logo" height='75px'></td>
                                </tr>
                            </tbody>
                        </table>
                        <f:verbatim>
                              </form>
                        </f:verbatim>
                    </div>
                    <div>
                        <table border="0">
                            <tr>
                                <td>
                               <%if (request.getAttribute("Logout") == null && request.getRemoteUser() != null && !request.isUserInRole("MasterIndex.Admin")) { %>
                                 Please check the user credentials.
							   <%}%>
                                </td>
                                <td><img src='images/spacer.gif' alt="" width='35px'></td>
                            </tr>
                        </table>
                    </div>
            </div>
            </div>
            </center>
    <script>
         if( document.loginform.elements[0]!=null) {
		var i;
		var max = document.loginform.length;
		for( i = 0; i < max; i++ ) {
			if( document.loginform.elements[ i ].type != "hidden" &&
				!document.loginform.elements[ i ].disabled &&
				!document.loginform.elements[ i ].readOnly ) {
				document.loginform.elements[ i ].focus();
				break;
			}
		}
      }         
         
    </script>
            
        </body>
    </html> 
</f:view>
