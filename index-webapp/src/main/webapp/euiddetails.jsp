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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"  %>

<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
  URI = URI.substring(1, URI.lastIndexOf("/"));
  String URL = new String();
 %>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
 session.removeAttribute("eoBrandNewSystemObjects"); //fixof 140
}
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
<f:view>
    
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
            <script language="JavaScript" src="scripts/Validation.js"></script>
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

		   <script type="text/javascript" >
           var rand = "";
		   var editMinorObjectType="";
		   var editObjectType = "";
		   var sbrInEdit = "";
		   var systemcodeInEdit = "";
 		   var lidInEdit = "";
		   var newSoInEdit = "";
		   var editSOflag = "false";
		   var addSOflag = "false";
		   var editSOMinorflag = "false";

           function setRand(thisrand)  {
 	        rand = thisrand;
           }
  			 var editIndexid = "-1";
             function closeTree()    {                            
                    document.getElementById('tree').style.visibility='hidden';
                    document.getElementById('tree').style.display='none';
              }
              function setEOEditIndex(editIndex)   {
				editIndexid = editIndex;
	   		  }
              function cancelEdit(formName, thisDiv,minorObject,buttonID)   {
                ClearContents(formName); 
				enableallfields(formName);
				editMinorObjectType = '';
                setEOEditIndex("-1");
				document.getElementById(thisDiv).style.visibility = 'hidden';
				document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(buttonID).innerHTML = '<h:outputText value="#{msgs.source_rec_save_but}"/>  '+ minorObject;
		      }
			   function hideDivs(thisDiv){
					document.getElementById(thisDiv).style.visibility = 'hidden';
					document.getElementById(thisDiv).style.display  = 'none';
			   }
			   function showDivs(thisDiv){
					document.getElementById(thisDiv).style.visibility = 'visible';
					document.getElementById(thisDiv).style.display  = 'block';
			   }   

			// added  on 22-09-08  
			function setMinorObjectAddressType(MinorObjectType,editIndex,objectType){
				editMinorObjectType = MinorObjectType;
				editIndexid = editIndex;
				editObjectType = objectType;
	 		}
			//added on 20-10-08 as a fix of 15
			function setSBRInEditMode(sbr){
				sbrInEdit = sbr;
			}
			
			//added on 20-10-08 as a fix of 15
			function setNewSOInEditMode(newso){
				newSoInEdit = newso;
			}

			//added on 20-10-08 as a fix of 15
			function setSOInEditMode(syscode,lid){
 					systemcodeInEdit = syscode;
					lidInEdit = lid;
  			}
			
			// added  on 22-09-08  			
			function showUnSavedAlert(thisEvent,minorObjectType,editObjectType){
				if(editObjectType.length == 0) {
					document.getElementById("unsavedMessageDiv").innerHTML = '<h:outputText value="#{msgs.unsaved_message_part_I}"/> \''+editMinorObjectType+'\'<h:outputText value="#{msgs.unsaved_message_part_III}"/>';
					//document.getElementById("unsavedDiv").style.visibility="visible";
					//document.getElementById("unsavedDiv").style.display="block";
			         showExtraDivs("unsavedDiv",thisEvent);
  
				} else {
					document.getElementById("unsavedMessageDiv").innerHTML = '<h:outputText value="#{msgs.unsaved_message_part_I}"/> \' '+editMinorObjectType+' \' <h:outputText value="#{msgs.unsaved_message_part_II}"/> ('+editObjectType+') <h:outputText value="#{msgs.unsaved_message_part_III}"/>';
					//document.getElementById("unsavedDiv").style.visibility="visible";
					//document.getElementById("unsavedDiv").style.display="block";
			        showExtraDivs("unsavedDiv",thisEvent);
				}
			   }
			 var userDefinedInputMask="";
             var URI_VAL = '<%=URI%>';
			 var RAND_VAL = '<%=rand%>';
		   var euidValueArray=[];
		   var euids="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];
		   var popUrl="";
		</script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title> 
        <body>

        

            <%@include file="./templates/header.jsp"%>
            <div id="mainContent1">
            <div id="ajaxContent">
                <table width="100%">
                    <tr><td>
                     <div id="basicSearch" class="basicSearch" >
                        <table border="0" cellpadding="0" cellspacing="0" width="100%"> 
                            <tr>
                              <h:form id="potentialDupBasicForm">
                                <td>
                                    <h:outputText value="#{msgs.datatable_euid_text}"/>
                                </td>
                                <td>
								<%
								        ValueExpression requestEuidVE = null ;
										if( request.getParameter("euid") != null )  {
											String euidString  = request.getParameter("euid");
 											requestEuidVE = ExpressionFactory.newInstance().createValueExpression(euidString, euidString.getClass());
										}

								%>
                                         <h:inputText    id="euidField"
                                                         label="EUID"  
														 title="EUID"
												         maxlength="#{SourceHandler.euidLength}" 
                                                         value="<%=requestEuidVE%>" /> 
                                </td>
                                <td>                                    
                                    <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('potentialDupBasicForm');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?pageName=euiddetails.jsf&random='+rand+'&'+queryStr,'outputdiv','')"><span><h:outputText value="#{msgs.search_button_label}"/></span></a>
							    </td>
                                <td>                                    
                                     <h:commandLink  title="#{msgs.Advanced_search_text}" styleClass="button" action="#{NavigationHandler.toPatientDetails}">  
                                        <span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                       </span>
                                    </h:commandLink>                                     
                                </td>
                             </h:form>
							    <%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								<!-- Added on 22-aug-2008 to incorparte with the functionality of back button in euiddetails.jsp  -->                                                                 
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
			               		<a href="javascript:void(0)" 
								   onclick="history.back()" 
								   class="button" 
								   title="<h:outputText  value="#{msgs.back_button_text}"/>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>
  				              </FORM>
							 </td>
 						   <%}%>
                            </tr>
 				             <tr>
                                <td colspan="5">
                                    <h:messages styleClass="errorMessages"  layout="list" />
                                </td>
                            </tr>
                            <tr>
                              <td colspan="5"><div style="color:red;" id="messages"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="5"><div id="outputdiv"></div></td>
                             </tr> 
				</table>
                     </div>
               </td>
               </tr>
                <tr>
                <td><div id="targetDiv"></div></td></tr>
                </table>
			   <%if(request.getParameter("euid") != null) {%>
				   <%if(request.getParameter("showMergeTree") != null) {%>
					   <script>
						  ajaxURL('/<%=URI%>/ajaxservices/euiddetailsservice.jsf?'+'&rand=<%=rand%>&euid=<%=request.getParameter("euid")%>&showMergeTree=true','targetDiv','');
					   </script>
				   <%} else {%>
					   <script>
						  ajaxURL('/<%=URI%>/ajaxservices/euiddetailsservice.jsf?'+'&rand=<%=rand%>&euid=<%=request.getParameter("euid")%>','targetDiv','');
					   </script>
				   <%}%>
              <%}%>

			   </div> <!-- Ajax content div-->
   </div> <!-- end mainEuidContent -->

 
 <!-- Added  on 27-09-2008, to change alert pop up window to information pop up window -->
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
                                onclick="javascript:window.location = popUrl;" class="button" >
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

         <!-- Added  on 07-10-2008 for all information popups -->
  		 <div id="unsavedDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
               <form id="successDiv">
                <table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<th align="center" title="<%=bundle.getString("move")%>"><%=bundle.getString("popup_information_text")%></th>
				<th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedDiv',event);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedDiv',event);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
				</th>
				</tr>
                    <tr><td colspan="2">&nbsp;</td></tr>    
					<tr>
						<td colspan="2">
							<b><div id="unsavedMessageDiv"></div></b>
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>    
					<tr id="actions">
					  <td colspan="2" align="center">
					    <table align="center">
							<tr>
								<td>
									<a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.ok_text_button}" />" onclick="javascript:showExtraDivs('unsavedDiv',event);">                          
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
         <div id="checkLatest"></div><!-- Fix for 671089-->

        </body>
        <%
		 MasterControllerService masterControllerService = new MasterControllerService();
         String[][] lidMaskingArray = masterControllerService.getSystemCodes();
         SourceHandler sourceHandler = new SourceHandler(); 
          
        %>
        <script>
            var systemCodes = new Array();
            var systemCodeDesc = new Array();
            var lidMasks = new Array();
        </script>
        
        <%
        for(int i=0;i<lidMaskingArray.length;i++) {
            String[] innerArray = lidMaskingArray[i];
            for(int j=0;j<innerArray.length;j++) {
            if(i==0) {
         %>       
         <script>
           systemCodes['<%=j%>']  = "<%=lidMaskingArray[i][j]%>";
           systemCodeDesc['<%=lidMaskingArray[i][j]%>']  = "<%=sourceHandler.getSystemCodeDescription(lidMaskingArray[i][j])%>";
         </script>      
         <%       
            } else {
         %>
         <script>
           lidMasks ['<%=j%>']  = "<%=lidMaskingArray[i][j]%>";
         </script>
         <%       
            }
           }
           }
        %>

<script>
		function setLidMaskValue(field,formName) {
			var  selectedValue = field.options[field.selectedIndex].value;
			
            var formNameValue = document.forms[formName];
			
            var lidField =  getDateFieldName(formName,'LID');
            //document.getElementById(lidField).value = "";
			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
             document.getElementById(lidField).disabled = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).disabled = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
</script>



<script>
         if( document.potentialDupBasicForm.elements[0]!=null) {
		var i;
		var max = document.potentialDupBasicForm.length;
		for( i = 0; i < max; i++ ) {
			if( document.potentialDupBasicForm.elements[ i ].type != "hidden" &&
				!document.potentialDupBasicForm.elements[ i ].disabled &&
				!document.potentialDupBasicForm.elements[ i ].readOnly ) {
				document.potentialDupBasicForm.elements[ i ].focus();
				break;
			}
		}
      }         

    </script>
<script type="text/javascript">
    makeDraggable("lockSBRDiv");
    makeDraggable("unLinkSoDiv");
    makeDraggable("linkSoDiv");
	makeDraggable("activeDiv");
    makeDraggable("tree");
</script>
     </html>
    </f:view>
    
