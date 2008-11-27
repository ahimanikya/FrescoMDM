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

<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>

<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
  URI = URI.substring(1, URI.lastIndexOf("/"));
  Operations operations = new Operations();
  String URL = new String();
 %>

<f:view>
   
    <html>
        <head>
           <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
        <!-- Additional source files go here -->
        
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">   
        <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
		<script type="text/javascript" src="scripts/balloontip.js"></script>       
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>  
            <!-- Required for View Merge Tree -->
        <script type="text/javascript" src="scripts/yui4jsf/yahoo/yahoo-min.js"></script>           
		<!-- Additional source files go here -->
		<script type="text/javascript" src="scripts/yui4jsf/core/yui4jsf-core.js"></script>
		<script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
		<script type="text/javascript" src="scripts/yui4jsf/treeview/treeview-min.js"></script>
		
		<script type="text/javascript" src="scripts/yui4jsf/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script type="text/javascript" src="scripts/yui4jsf/animation/animation-min.js"></script>                        
		<script type="text/javascript" src="scripts/yui/dragdrop/dragdrop-min.js"></script>

		<script type="text/javascript" >
		   var euidValueArray=[];
    	   var previewEuidDivs=[];
           var rand = "";
           function setRand(thisrand)  {
 	        rand = thisrand;
           }
           var editIndexid = "-1";
           function setEOEditIndex(editIndex)   {
				editIndexid = editIndex;
	   	   }
           function closeTree()    {                            
			document.getElementById('tree').style.visibility='hidden';
			document.getElementById('tree').style.display='none';
           }
		   // LID merge related global javascript variables
		   var euids="";
		   var popUrl="";
		   var reloadUrl="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];

         </script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}" /></title>
        <body>
            <%@include file="./templates/header.jsp"%>
             <div id="mainContent">
  			   <table width="100%"><tr><td>
                <div id="basicSearch" class="basicSearch" style="visibility:visible;display:block;">
                    <h:form id="advancedformData">
                        <table border="0" cellpadding="0" cellspacing="0" > 
                            <tr>
                                <td align="left">
                                    <h:outputText value="#{msgs.datatable_euid_text}"/>
                                </td>
                                <td align="left">
                                    <h:inputText   
                                        id="euidField"
                                        title="EUID"
                                        value="#{RecordDetailsHandler.singleEUID}" 
								        maxlength="#{SourceHandler.euidLength}" 
										/>
                                </td>
								 <!-- added  on 22/08/2008 for incorporate back button -->
								 
                                  <% if(request.getParameter("euids") != null) { %>
                                <td>                                    
                                     <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?pageName=compareduplicates.jsf&random='+rand+'&'+queryStr,'outputdiv','')"><span><h:outputText value="#{msgs.search_button_label}"/></span></a>
 
                                </td>                                    
                                <td>                                    
									 <h:commandLink  title="#{msgs.Advanced_search_text}"  styleClass="button" action="#{NavigationHandler.toPatientDetails}">  
                                        <span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                       </span>
                                    </h:commandLink>                          
							</td>
							<%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								                                                            
								<% 
								 String pageName = request.getParameter("fromUrl");
								 String previousQuery = request.getQueryString();
								 URL= pageName+"?"+previousQuery+"&back=true";
								 %>
 			               		<a class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>" href="<%=URL%>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>

 							 </td> 
							 <%} else {%>
							 <td>	
                                <FORM>
			               		<a href="#" 
								   onclick="history.back()" 
								   class="button" 
								   title="<h:outputText  value="#{msgs.back_button_text}"/>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>
  				              </FORM>
							 </td>
 						   <%}%>
                                   
                            <%}else {%>
                                <td>                                    
                                       <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"
										       href="javascript:void(0)"
                                               onclick="javascript:getFormValues('advancedformData');setRand(Math.random());
ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&random='+rand+'&'+queryStr,'outputdiv','')">  
                                                <span><h:outputText value="#{msgs.search_button_label}"/></span></a>
                                 </td>                                    
                                <td>                             
                                        <a  href="duplicaterecords.jsf" 
									       class="button" 
										   title="<h:outputText value="#{msgs.Advanced_search_text}"/>" >  
  										<span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                       </span>
									   </a>
                                </td> 
								<%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								
								<% 
								 String pageName = request.getParameter("fromUrl");
								 String previousQuery = request.getQueryString();
								 URL= pageName+"?"+previousQuery+"&back=true";
								 %>
 			               		<a class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>" href="<%=URL%>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>

 							 </td><%} else {%>
							 <td>	
                                <FORM>
			               		<a href="#" 
								   onclick="history.back()" 
								   class="button" 
								   title="<h:outputText  value="#{msgs.back_button_text}"/>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>
  				              </FORM>
							 </td>
 						   <%}%>
	<%}%>
                             </tr>
                            <tr>
                                <td colspan="2">                           
                                           <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" /> 
                                </td>
                            </tr>
                             <tr>
                              <td colspan="2"><div style="color:red;" id="messages"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="2"><div id="outputdiv"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="2"><div id="populatePreviewDiv"></div></td>
                             </tr> 
                        </table>
                    </h:form>
                 </div>
             </td></tr></table>
  			 <table width="100%">
			        <tr><td><div id="mainDupSource" class="compareResults"></div></td></tr>
 			 </table>
             </div> 
              <%if(request.getParameter("euids") != null) {%>
             	<script>
               	  ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&euids=<%=request.getParameter("euids")%>','mainDupSource','');
              	</script>
             <%} else {%>
             	<script>
               	  ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&fromPage=<%=request.getParameter("fromPage")%>&duplicateEuids=<%=request.getParameter("duplicateEuids")%>','mainDupSource','');
                  fromPage ="<%=request.getParameter("fromPage")%>";
                  duplicateEuids = "<%=request.getParameter("duplicateEuids")%>";

              	</script>
             <%}%>
					<!-- Modified  on 27-09-2008, added banner and close link to confirmation pop up window -->
                         <div id="mergeDiv" class="confirmPreview" style="TOP:250px;LEFT:300px;VISIBILITY:hidden;display:none;">
                             <table cellspacing="0" cellpadding="0" border="0">
                                <tr>
								<th align="center" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
								<th>
									<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
									<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
							    </th>	
							    </tr>
                                <tr><td colspan="2"> &nbsp;</td></tr>
                                <tr>
							    <td colspan="2" align="left"> 
							     <nobr>
							     <table border="0" align="center">
								  <tr>
								   <td align="left"><b>&nbsp;<h:outputText value="#{msgs.mergediv_popup_text}"/></b></td>
								   <td align="left"><b><div id="merge_destnEuid"></div></b></td>
 								  </tr>
								 </table> 
								 </nobr>
								</td> 
								</tr>
                                <tr><td colspan="2">&nbsp;</td></tr>
                                <tr>
                                  <td colspan="2" align="center">
                                            <h:form  id="mergeFinalForm">
 							                      <%if(operations.isEO_Merge()) {%>
												  <table align="center">
												  <tr>
												  <td>&nbsp;&nbsp;</td>
												  <td>
														<a href="javascript:void(0)"  
														   class="button" 
														    title="<h:outputText value="#{msgs.ok_text_button}" />"
                                                            onclick="javascript:getFormValues('mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&mergeFinal=true&rand=<%=rand%>','mainDupSource','');showExtraDivs('mergeDiv',event);"   >
                                                                     <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </a>
												  </td>
												  <td>
													  <a href="javascript:void(0)"  class="button" onclick="javascript:showExtraDivs('mergeDiv',event);"  title="<h:outputText value="#{msgs.cancel_but_text}" />" >
			                                          <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                      </a>
											      </td>
											      </tr>
											      </table>
												 <%}%>
                                                  <input type="hidden" id="mergeFinalForm:srcDestnEuids" title="MERGE_SRC_DESTN_EUIDS" />
                                                <h:inputHidden id="destinationEO" value="#{RecordDetailsHandler.destnEuid}" />
                                                <h:inputHidden id="selectedMergeFields" value="#{RecordDetailsHandler.selectedMergeFields}" />
                                            </h:form>
                                        </td>
                                    </tr>
                                    
                                </table>
                        </div>  
						<!-- Modified  on 27-09-2008, added banner and close link to confirmation pop up window -->
                                   <div id="resolvePopupDiv" class="confirmPreview" style="TOP:250px;LEFT:300px;visibility:hidden;display:none;">
                                     
                                       <h:form id="reportYUISearch">
 										   <input type="hidden" title="resolvePotentialDuplicateId" id="resolvePotentialDuplicateId" />
                                           <table cellspacing="0" cellpadding="0" border="0">
                                               <tr><th align="center" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
											   <th>
													<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('resolvePopupDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
													<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('resolvePopupDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
											   </th>
											   </tr>
                                               <tr><td colspan="2">&nbsp;</td></tr>
                                               <tr><td  colspan="2" align="center"><b><h:outputText															     value="#{msgs.different_person_dailog_text}"/>&nbsp;
                                                         <h:selectOneMenu id="diffperson" title="resolveType">
                                                           <f:selectItem  itemValue="AutoResolve" itemLabel="#{msgs.AutoResolve_Label}"/>
                                                           <f:selectItem  itemValue="Resolve"     itemLabel="#{msgs.Resolve_Perm_Label}"/>
                                                        </h:selectOneMenu>                                                        
                                                   &nbsp;</b></td>
                                               </tr>
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                               <tr id="actions">
                                                   <td colspan="2">
												   <table align="center">
												   <tr>
												   <td>
                  										<a class="button" title="<h:outputText value="#{msgs.ok_text_button}" />"
                                                            href="javascript:void(0)"
                                                            onclick="javascript:getFormValues('reportYUISearch');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&resolveDuplicate=true&rand=<%=rand%>','mainDupSource','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';"   >
                                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </a>
												   </td>
												   <td>
												      <a href="javascript:void(0)"  class="button" onclick="javascript:showResolveDivs('resolvePopupDiv',event,'123467');"  title="<h:outputText value="#{msgs.cancel_but_text}" />" >
													  <span>  <h:outputText value="#{msgs.cancel_but_text}" />  </span>
													   </a>
												   </td>
												   </tr>
												   </table>
                                                   </td>
                                               </tr>
                                           </table>
                                           
                                       </h:form>
                                   </div>                                                

             <div id="resolvepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.resolvepopup_help}"/></div>    
             <div id="mergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.mergepopup_help}"/></div>    
             
        </body>
        <%
        if( request.getAttribute("eoMultiMergePreview") != null) {
            String destnEuid  = (String) request.getAttribute("destnEuid");
            String[] srcs  = (String[]) request.getAttribute("sourceEUIDs");
		%>
		<script>
			document.getElementById('mainEuidContent<%=destnEuid%>').className = "blue";
            document.getElementById('personEuidDataContent<%=destnEuid%>').className = "blue";
		</script>
		<%
        for(int i=0;i<srcs.length;i++) {
        %>    
            
        <script>
			document.getElementById('mainEuidContent<%=srcs[i]%>').className = "blue";
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "blue";
         </script>
        <%}%>
        <%}%>

<script>
         if( document.advancedformData.elements[0]!=null) {
		var i;
		var max = document.advancedformData.length;
		for( i = 0; i < max; i++ ) {
			if( document.advancedformData.elements[ i ].type != "hidden" &&
				!document.advancedformData.elements[ i ].disabled &&
				!document.advancedformData.elements[ i ].readOnly ) {
				document.advancedformData.elements[ i ].focus();
				break;
			}
		}
      }         

    </script>
<script type="text/javascript">
    makeDraggable("resolvePopupDiv");
    makeDraggable("mergeDiv");
    makeDraggable("mergeConfirmationDiv");
    makeDraggable("activeDiv");
	
</script>
       <div id="activeDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="activemessageDiv"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td>
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:
 								window.location = popUrl;
								document.getElementById('activeDiv').style.visibility='hidden';
							    document.getElementById('activeDiv').style.display='none';" 
							    class="button" >
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </a>
							  </td>
							 </tr>
							 </table>
					     </td>
                     </tr> 
                 </table>
             </form>
         </div>

		 <div id="successDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
               <form id="successDiv">
                <table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<th align="center" title="<%=bundle.getString("move")%>"><%=bundle.getString("popup_information_text")%></th>
				<th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:window.location=popUrl;"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:window.location=popUrl;"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
				</th>
				</tr>
                    <tr><td colspan="2">&nbsp;</td></tr>    
					<tr>
						<td colspan="2">
							<b><div id="successMessageDiv"></div></b>
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>    
					<tr id="actions">
					  <td colspan="2" align="center">
					    <table align="center">
							<tr>
								<td>
									<a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.ok_text_button}" />" onclick="javascript:window.location=popUrl;">                          
										<span><h:outputText value="#{msgs.ok_text_button}"/></span>
									</a>
								</td>
							</tr>
						</table>
					  </td>
					</tr>
                </table> 
                </form>
            </div> 
 		 	 <!-- added as a fix of 6710694 -->
		       <div id="mergeConfirmationDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
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
					     <td colspan="2" ><b><div id="mergeConfirmationmessageDiv"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td> 
                               <a title="<h:outputText value="#{msgs.source_inpatient1_text}"/>"
                                href="javascript:void(0)"
                                onclick="if(reloadUrl.length<1) { 
								           window.location.reload(true);
										  }else {
                                            window.location = reloadUrl;
										    reloadUrl='';
 										  }";
 							    class="button" >
                                <span><h:outputText value="#{msgs.source_inpatient1_text}"/></span>
                                </a>
							  </td>
							  <td>
                              <a title="<h:outputText value="#{msgs.source_inpatient2_text}"/>"
                                href="javascript:void(0)"
                                onclick="window.location = '<%=URL%>' ;"
  							    class="button" >
                                <span><h:outputText value="#{msgs.source_inpatient2_text}"/></span>
                                </a>
							  </td>
							 </tr>
							 </table>
					     </td>
                     </tr> 
                 </table>
             </form>
         </div>

     </html>
    </f:view>
    
