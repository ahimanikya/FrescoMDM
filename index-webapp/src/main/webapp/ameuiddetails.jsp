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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.TreeMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<f:view>
   <%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));

}
%>
     <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
            
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/balloontip.js"></script>
            <!-- Required for View Merge Tree -->
            <script type="text/javascript" src="scripts/yui4jsf/yahoo/yahoo-min.js"></script>           
            <!-- Additional source files go here -->
            <script type="text/javascript" src="scripts/yui4jsf/core/yui4jsf-core.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/treeview/treeview-min.js"></script>
            
            <script type="text/javascript" src="scripts/yui4jsf/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/animation/animation-min.js"></script>                        
            <script type="text/javascript" src="scripts/yui/dragdrop/dragdrop-min.js"></script>
			<script>
			 function displayEO(idx)  {
				 alert(idx);
			 }

			 function changecolor(v)   {
				 if (v.style.backgroundColor == "#696969")   {
					 v.style.backgroundColor="#e7e7d6";
				 } else {
					 v.style.backgroundColor="#696969";
				 }
				 //alert("Next");
				  //v.innerText="<tr><td>Next</td></tr>";
			}
            function cancelPreview(divId)  {
             	//confirmPreview
             	var confirmPreview = document.getElementById("confirmPreview");
             	confirmPreview.style.visibility="hidden";

                var undoPreviewButton = document.getElementById("undoPreviewButton"+divId);
             	undoPreviewButton.style.visibility="visible";

             	var previewdiv = document.getElementById("previewdiv");
             	previewdiv.innerHTML = "";
             	previewdiv.style.visibility="hidden";
  }
  // variable used to fix the view source/history issues
  var euidArray = [];
 </script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title>
        <body class="yui-skin-sam">

<!-- Modify on 29/07/2008
     Description : sorted euids and modified thisIdx to set the initial navigation
 -->

<%
   String amid = request.getParameter("AMID");
   String paginationArray = request.getParameter("euids");
   String [] pagination = paginationArray.split(",");
   TreeMap paginationArrayMap= new TreeMap();
   for (int i=0; i<pagination.length;i++){ 
   	   paginationArrayMap.put(pagination[i],"");
   }
   pagination = (String[])(paginationArrayMap.keySet().toString()).split(",");
   for (int i=0; i<pagination.length;i++)    { 
		if (i == 0 ) {
			pagination[i] = pagination[i].substring(1,pagination[i].length());
         } else if (i == pagination.length -1 )  {
			pagination[i] = pagination[i].substring(0,pagination[i].length()-1);
		 }
		 pagination[i] = pagination[i].trim();
   }
   
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));
%>

<script>
var pages =[];
var thisIdx=0;
  <%for (int i=0; i<pagination.length;i++)    { %>
         pages.push("<%=pagination[i]%>");
       <% if(pagination[i].equalsIgnoreCase(amid)){%>
         thisIdx = <%=i%>;
		<%}%>
   <% }%>
</script>
<!-- Added on 22-aug-2008 to incorparte with the functionality of back button in ameuiddetails.jsp  -->                                                                 
 <% 
 	String pageName = request.getParameter("fromUrl");
	String previousQuery = request.getQueryString();
	String URL= pageName+"?"+previousQuery+"&back=true";
 %>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent">
                    <div id="header" class="detailedresults">
                        <table border="0">
                            <tr>
                                <th align="left" class="euidHeadMessage">
                                    <b> <h:outputText value="Assumed Matches Details"/></b>
                                </th>
                                
                                <td>
                                    <a class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>" href="<%=URL%>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>
                                </td>
                            </tr>                
							<tr>
							   <td align="left" colspan="2"><div class="detailedresults" id="outputdiv"></div></td>
							</tr>
                        </table>
                    </div>
                    </div>  <!-- Main content -->

<script>
  ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?random=rand'+'&'+'AMID=<%=amid%>','outputdiv','')
</script>
<!-- Fix for Issue #111 Starts -->
		       <div id="undoInformationDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="undoForm" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeConfirmationDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeConfirmationDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="undoInformationDivMessage"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							  <td>&nbsp;</td>
							  <td>
                              <a title="<h:outputText value="#{msgs.source_inpatient2_text}"/>"
                                href="javascript:void(0)"
                                onclick="window.location = '<%=URL%>' ;"
  							    class="button" >
                                <span><h:outputText value="#{msgs.source_inpatient1_text}"/></span>
                                </a>
							  </td>
							 </tr>
							 </table>
					     </td>
                     </tr> 
                 </table>
             </form>
         </div>
<script type="text/javascript">
     makeDraggable("undoInformationDiv");
  	
</script>
<!-- Fix for Issue #111 Ends-->
        </body>
    </html>
</f:view>







