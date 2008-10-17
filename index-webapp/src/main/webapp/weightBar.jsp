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
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
    <title>4 x 4 grid using div tags and css</title>
    <style type="text/css">
        .squarecontainerOriginal { 
            width: 450px;
            overflow:auto;
        }

       .squarecontainer { 
            overflow:auto;
        }
        .squares {
            float: left;
            width: 5em;
            height: 5em;
            margin: .5em;
            border: 1px solid black;
        }
        
    </style>
    <body>
        
        <h1>JSP Page</h1>
        
        <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
        --%>
        <!-- This is the first row-->
        <div class="squarecontainerOriginal">
        <div class="squarecontainer">
        <% 
            for (int i = 0; i < 10; i++) {%>
            <div class="squares"><%=i%></div>
        <%}%>
        </div>
        </div>
        
        <% int sel = 1;
            for (int i = 0; i < 10; i++) {%>
        <%if (i > 2) {%>
        <div style="width:10px;height:1px;border-top:1px solid #EFEFEF;border-left:1px solid #EFEFEF;background-color:green;"></div>
        <%} else {%>
        <div style="width:10px;height:1px;border-top:1px solid #EFEFEF;border-left:1px solid #EFEFEF;background-color:white;"></div>
        <%}%>
        <%}%>   
    </body>
</html>
