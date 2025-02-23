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
<%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="javax.faces.application.FacesMessage" %>

<f:view>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

     <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
<%
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
String URI = request.getRequestURI();
String divId= "";
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
double rand = java.lang.Math.random();
int confirmPosition =0;
boolean isSessionActive = true;
%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>

<%if (isSessionActive)  {%>
<%
Operations operations = new Operations();
ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

 SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
 
  EnterpriseObject reqEnterpriseObject = new EnterpriseObject();

 //assumed match  values with systecode and lid
 HashMap amValues = new HashMap();
 
 //variable for max minor objects count
 int maxMinorObjectsMAX = 0;

 String amSourcesID = request.getParameter("amSourcesID");
 String mainEUID= request.getParameter("mainEUID");
 divId= request.getParameter("divId");
AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();

ArrayList eoArrayList = assumeMatchHandler.getEOList(request.getParameter("AMID"));

HashMap previewAMEO = assumeMatchHandler.previewUndoAssumedMatch(amSourcesID);
SourceHandler sourceHandler = new SourceHandler();

 HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
 Object[] rootNodeConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
 confirmPosition = rootNodeConfigFeilds.length*25;
 String rootNodeName = objScreenObject.getRootObj().getName();
 FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
 ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
 String epathValue = new String();
 String rootNodeValue = "";
 
 Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
%>
<%if(previewAMEO  != null) {%>
                                                <div id="previewPane" class="blue">
                                                        <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                                            <tr>
                                                                <td width="100%" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                            </tr>
                                                        </table>
                                                                     <%
     HashMap eoAssumeMatchPreviewMap = new HashMap();
     HashMap mergePersonfieldValuesMapEO = new HashMap();
     if (previewAMEO != null) {
         eoAssumeMatchPreviewMap = previewAMEO;
         mergePersonfieldValuesMapEO = (HashMap) eoAssumeMatchPreviewMap.get("ENTERPRISE_OBJECT");
     }
                                                                    %>

                                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                                    
                                                                    <%
     for (int ifc = 0; ifc < rootNodeConfigFeilds.length; ifc++) {
         FieldConfig fieldConfigMap = (FieldConfig) rootNodeConfigFeilds[ifc];
         if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
             epathValue = fieldConfigMap.getFullFieldName();
         } else {
             epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
         }
                                                                    %>  
																	<% if(ifc == 0) {%>
                                                                      <tr><td>&nbsp;</td></tr>
                                                                      <tr><td>&nbsp;</td></tr>
																	<%}%>
                                                                    <tr>
                                                                        <td>
                                                                            <%
                                                                        if (previewAMEO != null) {
                                                                            %>
                                                                            
                                                                            <%if (mergePersonfieldValuesMapEO.get(epathValue) != null) {%>
                                                                            <%if (eoAssumeMatchPreviewMap.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  mergePersonfieldValuesMapEO.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=mergePersonfieldValuesMapEO.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=mergePersonfieldValuesMapEO.get(epathValue)%> 
																				<%}%>
                                                                            <%}%>
                                                                            
                                                                            <%} else {%>
                                                                            &nbsp;
                                                                            <%}%>
                                                                            
                                                                            <%} else {%>
                                                                            &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
     }
                                                                    %>
                                                                    <!--start displaying minor objects-->
                                                                    
                                                                    <%
     for (int io = 0; io < arrObjectNodeConfig.length; io++) {
         ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
         ArrayList minorObjectMapList = (previewAMEO != null) ? (ArrayList) eoAssumeMatchPreviewMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList") : new ArrayList();


         int maxMinorObjectsMinorDB = (previewAMEO != null) ? ((Integer) eoAssumeMatchPreviewMap.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue() : 0;

         maxMinorObjectsMAX = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList, objScreenObject, childObjectNodeConfig.getName());

         int maxMinorObjectsDiff = maxMinorObjectsMAX - maxMinorObjectsMinorDB;

         FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
         HashMap minorObjectHashMap = new HashMap();
                                                                    %>
                                                                    <tr>

                                                                        <td>
                                                                            <%if (previewAMEO != null && minorObjectMapList.size() == 0) {%>
                                                                            No <%=childObjectNodeConfig.getName()%>.
                                                                            <%} else {%>
                                                                            &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    
                                                                    <%
                                                                        for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
                                                                            minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);


                                                                    %>
                                                                    <%
                                                                        for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                            epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() > 0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                            <%if (fieldConfigMap.isKeyType()) {%>
                                                                            <b>
                                                                            <%if (eoAssumeMatchPreviewMap.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%>
																					<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																					if(rootNodeValue.length()>20){
																					rootNodeValue = rootNodeValue.substring(0,20);
																					%>
																					<%=rootNodeValue%>
																					<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
																					<%}else{%>
																					<%=minorObjectHashMap.get(epathValue)%> 
																					<%}%>
                                                                                <%}%>
                                                                            </b>
                                                                            <%} else {%>
                                                                            <%if (eoAssumeMatchPreviewMap.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=minorObjectHashMap.get(epathValue)%> 
																				<%}%>
                                                                            <%}%>
                                                                            <%}%>
                                                                            
                                                                            <%} else {%>
                                                                            &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                        } // FIELD CONFIGS LOOP
%>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%} // MINOR OBJECTS LOOP FOR THE SO
%>
                                                                    
                                                                    <%
                                                                        for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {
                                                                    %>
                                                                    
                                                                    <%
                                                                        for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                    %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
%>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%   }//Extra minor objects loop
%>
                                                                    <%}
                                                                    %>
                                                                    
                                                                    
                                                                    <!-- end displaying minor objects -->
                                                                </table>                                      
                                                </div>                                                
											   <div id="confirmPreview" class="confirmPreview">
											      <table border="0" cellpadding="0" cellspacing="0" >
											       <tr>
												   <th title="<%=bundle.getString("move")%>"><%=bundle.getString("confirmation_window_heading")%>
												   </th>
													<th>
														<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:cancelPreview(<%=divId%>);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

														<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:cancelPreview(<%=divId%>);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
													</th>

													 </tr>
													 <tr><td colspan="2">&nbsp;</td></tr>
											         <tr>
													       <td colspan="2">&nbsp;&nbsp;<%=bundle.getString("undo_assume_match")%></td>
													 </tr>
													 <tr><td colspan="2">&nbsp;</td></tr>
											         <tr id="actions">
											            <td colspan="2">
														  <table align="center">
														    <tr>
															 <td>&nbsp;</td>
															 <td>
                                                          <a href="javascript:void(0)" class="button"
														      onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMMerge.jsf?random=rand'+'&'+'undoAssumedMatchId=<%=amSourcesID%>'+'&'+'&mainEUID=<%=mainEUID%>','confirmPreview','')"
														       title="<h:outputText value="#{msgs.ok_text_button}" />">
														       <span>
                                                                 <h:outputText value="#{msgs.ok_text_button}" /> 
															   </span>
                                                          </a>
														 </td>
														 <td>
                                                          <a href="javascript:void(0)"  class="button" onclick="javascript:cancelPreview(<%=divId%>);"  title="<h:outputText value="#{msgs.cancel_but_text}" />" >
														     <span>
                                                               <h:outputText value="#{msgs.cancel_but_text}" /> 
														     </span>
                                                         </a>
														 </td>
														   </tr>
														 </table>
											           </td>
											         </tr>
											        </table>
												</div>

<%} else {%>
	  <% boolean alreadyUndoMatch = false;%>
      <div id="ajaxalert"> 
	  <table>
			<tr>
				<td>
				      <ul>
			            <% while (messagesIter.hasNext())   { %>
								<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); 
	                             %>
								<%if(facesMessage.getSummary().indexOf("MDM-MI-MSC582") != -1 ) {
									alreadyUndoMatch = true;
								}			                    
								%>
                                								
							    <%if(!alreadyUndoMatch){%>
 				                  <li style="color:red"><%= facesMessage.getSummary() %></li>
 						      <% } %>
 						 <% } %>

						<%if(alreadyUndoMatch){%>
							  <table>
									<tr>
										<td><!-- Modified  on 12-08-2008 as fix of Issue# 111 -->
												  <script>
														document.getElementById("undoInformationDivMessage").innerHTML = " Assumed Match record could not be undone. Assumed Match ID '<%=amSourcesID%>' has already been undone.";
														document.getElementById("undoInformationDiv").style.visibility="visible";
														document.getElementById("undoInformationDiv").style.display="block";
												  </script>
									   <td>
									<tr>
								</table>
						<%}%>

				      </ul>
				<td>
			<tr>
		</table>
		</div>

<%}%> <!-- Assume match preview condition Fix for Issue#111-->
<%}%> <!-- End session check if condition -->
<script>
  var undoPreviewButton = document.getElementById("undoPreviewButton<%=divId%>");
  undoPreviewButton.style.visibility="hidden";
  
   //confirmPreview
  var confirmPreview = document.getElementById("confirmPreview");
  confirmPreview.style.visibility="visible";
  //confirmPreview.style.top="<%=confirmPosition%>px";
  confirmPreview.style.top="350px";
    
            dd = new YAHOO.util.DD("confirmPreview");
 
</script>

</f:view>
