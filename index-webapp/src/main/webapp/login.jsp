
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>

<f:view>    
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <LINK REL="STYLESHEET" HREF="./css/styles.css"  TYPE="text/css">
        </head>
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
                    <h:form>
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tbody>
                                <tr>
                                    <!--alt=Enterprise Data Manager-->                                    
                                    <td colspan='2'><img src='images/spacer.gif' alt="Enterprise Data Manager" height='120px'></td>
                                </tr>
                                <tr>
                                    <td colspan='2'><h:inputText label="User Name" id="userName" value="#{LoginHandler.userName}" required="true"/></td>
                                </tr>
                                <tr>
                                    <td colspan='2'> <h:inputSecret label="Password" id="password" value="#{LoginHandler.password}" required="true"/></td>
                                </tr>
                                <tr>
                                    <td colspan="2">                                          
                                            <h:commandLink  id="submit" styleClass="button" action="#{LoginHandler.authorizeAndLoginUser}">
                                                <span> <h:outputText value="#{msgs.login_submit_button_prompt}"/> </span>
                                            </h:commandLink>    
                                    </td>
                                </tr>
                                <tr>
                                    <!--alt=Sun Microsystems Logo-->
                                    <td colspan='2'><img src='images/spacer.gif' alt="Sun Microsystems Logo" height='75px'></td>
                                </tr>
                            </tbody>
                        </table>
                    </h:form>
                    </div>
                    <div>
                        <table border="0">
                            <tr>
                                <td>
                                           <h:messages  styleClass="errorMessages"  layout="list" />                                           
                                </td>
                                <td><img src='images/spacer.gif' alt="" width='35px'></td>
                            </tr>
                        </table>
                    </div>
            </div>
            </div>
            </center>
        </body>
    </html> 
</f:view>
