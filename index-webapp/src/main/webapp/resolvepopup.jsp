<%-- 
    Document   : resolvepopup
    Created on : Dec 4, 2007, 12:25:42 AM
--%>
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="javax.el.*"  %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
             <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/newdateformat1.js"></script>
    
   
        <title>Different Records</title>
    </head>
    <%
            String sourceEUID = (String) session.getAttribute("sourceEUIDSessionObj");
            String destnEUID = (String) session.getAttribute("destinationEUIDSessionObj");
            ValueExpression mainEOVaueExpression = null;
            ValueExpression duplicateEOVaueExpression = null;
            
            if(session.getAttribute("sourceEUIDSessionObj") != null && session.getAttribute("destinationEUIDSessionObj") != null) {
               mainEOVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEUID, sourceEUID.getClass());
               duplicateEOVaueExpression = ExpressionFactory.newInstance().createValueExpression(destnEUID, destnEUID.getClass());
            }
    %>
    
    <body>
        <h:form>
        <table class="resolvepopup">
            <tr><td>Select the type of resolve:</td></tr>
            <tr><td><input type="radio" name="jsftags:_resolve" value="Resolve"> Resolve</td></tr>
            <tr><td><input type="radio" name="jsftags:_auto_resolve" value="AutoResolve"> Auto Resolve</td></tr>
            <tr>
                <td align="right">
                    <input type="button" class="button" name="jsftags:_ok" onclick="javascript:markDuplicate()" value="OK"> &nbsp;
                    <input type="button" class="button" name="jsftags:_Cancel" onclick="javascript:return;" value="Cancel"> 
                 </td>
            </tr>
            
        </table>
        </h:form>
    </body>
</html>
