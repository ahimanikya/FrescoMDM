<%-- 
    Document   : login
    Created on : Oct 29, 2008, 2:47:11 PM
    Author     : cye
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page session="false" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/spring" %>
<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.sun.mdm.multidomain.presentation.beans.UserLocale" %>
<%@ page import="com.sun.mdm.multidomain.presentation.beans.LocaleHandler" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/styles.css" media="screen"/>
        <title><f:message key="webpage.title" /></title>
        
        <% 
            request.setAttribute("localeHandler", new LocaleHandler());
        %>
               
        <script language="JavaScript">
        function submitLogin() {
            document.getElementById("loading").style.visibility = 'visible';
            document.getElementById("loading").style.display = 'block';
            document.loginform.submit();
        }
         function languageChange() {
            document.localeform.submit();
        }       
        </script> 
    </head>
    <body>  
          
    <c:set var="selectedLang" value="${param.selectedLang}" />
    <c:set var="isSelectedLang" value="${!empty selectedLang}" />     
    <jsp:useBean id="localeHandler" scope="application" class="com.sun.mdm.multidomain.presentation.beans.LocaleHandler"/>
    <c:if test="${isSelectedLang}" >
        <jsp:setProperty name="localeHandler" property="selectedLang" value="${selectedLang}" />
    </c:if>

    <c:set var="selectedLocale" value="${localeHandler.selectedLocale}" />
    <f:setLocale value="${selectedLocale}" scope="application" /> 
    <f:setBundle basename="com.sun.mdm.multidomain.presentation.mdwm" scope="page"/> 
    
    <% 
        request.setAttribute("userLocale", new UserLocale("${selectedLang}"));
    %>
        
    <center>
    <div id="mainContent">
        <div id="head"></div>
        <p></p>
        
        <div id="localeDiv" align="right" style="position:relative;top:-11px;">
        <table align="right" border="0" style="font-size:12px;"> 
        <tr>
            <td>
            <div class="chooseLangPrompt"><f:message key="locale_choose_lang"/></div>
            </td>
            <td>
            <form name="localeform" action="login.jsp" method="post">
            <select name="selectedLang" onchange="languageChange()">
                <c:forEach var="localeString" items="${localeHandler.languages}" >
                    <c:choose>
                    <c:when test="${isSelectedLang}">
                        <c:choose>
                        <c:when test="${fn:startsWith(selectedLang,localeString)}">
                            <option value="${localeString}" selected>${localeString}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${localeString}">${localeString}</option>
                        </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <option value="${localeString}">${localeString}</option>
                    </c:otherwise>
                </c:choose>
                </c:forEach>
            </select>
            </form>
            <td>
        </tr>    
        </table>
        </div>
              
        <div id="log" class="loginForm" align="center">
        <form name="loginform" id ="formid" action="j_security_check" method=post focus="j_username">
        <table border="0" cellpadding="0" cellspacing="0">
            <tbody>
            <tr>
                <td colspan="2">
                <img src="images/spacer.gif" alt="MultiDomain Web Manager" title="MultiDomain Web Manager" height='120px'>
                </td>
            </tr>
            <tr>
                <td>
                <div id="loginPrompt"><f:message key="login_username_prompt"/></div>
                </td>                        
                <td colspan="1">                                     
                <input type="text" name="j_username"  onkeyup=""  size="10"/>
                </td>                        
            </tr>
            <tr>
                <td>
                <div id="loginPrompt"><f:message key="login_password_prompt"/></div>
                </td>
                <td colspan="1">                                        
                <input type="password" name="j_password" onkeyup="" size="10" redisplay="false"/>
                </td>               
            </tr>
            <tr>
                <td colspan="2">                                          
                <a title="<f:message key="login_submit_button_prompt"/>" href="javascript:submitLogin();" class="button">
                <span><f:message key="login_submit_button_prompt"/></span></a>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                <img src='./images/spacer.gif' alt="Sun Microsystems Logo" title="Sun Microsystems" height='75px'>
                </td>
            </tr>            
            </tbody>        
        </table>       
        </form>    
        </div>
 
        <div id="loading" style="visibility:hidden;display:none;">
            <table border="0" style="position:relative;top:120px;">
            <tr>
                <td><img src='images/loading.gif' alt="Loading..."/> Loading.... Please Wait.</td>
            </tr>
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
