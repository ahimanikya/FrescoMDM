<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
 <%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>

<%
//Author Bhat
//narayan.bhat@ligaturesoftware.com
//http://www.ligaturesoftware.com
//This page is an Ajax Service, never to be used directly from the Faces-confg.
//This will render a datatable of minor object to be rendered on the calling JSP.
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>View Sorce Record</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
     </head>
	 
<f:view>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />

<!-- Global variables for the calendar-->
<%
 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
 String global_daysOfWeek  = bundle.getString("global_daysOfWeek");
 String global_months = bundle.getString("global_months");
 String cal_prev_text = bundle.getString("cal_prev_text");
 String cal_next_text = bundle.getString("cal_next_text");
 String cal_today_text = bundle.getString("cal_today_text");
 String cal_month_text = bundle.getString("cal_month_text");
 String cal_year_text = bundle.getString("cal_year_text");
 String  dateFormat = ConfigManager.getDateFormat();
%>

 <%
 double rand = java.lang.Math.random();
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
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
 		CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
		SourceHandler sourceHandler = new SourceHandler();
        
		String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

		ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
		ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
		Object[] roorNodeFieldConfigs = sourceHandler.getRootNodeFieldConfigs().toArray();
		SourceEditHandler sourceEditHandler = (SourceEditHandler)session.getAttribute("SourceEditHandler");
		SourceAddHandler  sourceAddHandler   = (SourceAddHandler)session.getAttribute("SourceAddHandler"); ;
		Operations operations=new Operations();
		HashMap systemObjectAsHashMap  = new HashMap();
		ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
		HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
		
	%>

	<%
		 //get system code and lid here		 
 		if(request.getParameter("viewSO")!=null && request.getParameter("viewSO").equals("true")){ 
			    String systemCodeSelected = request.getParameter("SystemCode");
			    String lid = request.getParameter(localIdDesignation);
			    String lidSelected = lid.replaceAll("-","");
 				
				systemObjectAsHashMap  =  sourceHandler.sourceRecordSearch(systemCodeSelected,lidSelected);
			    Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
				session.setAttribute("lidSelected",lidSelected);
				session.setAttribute("systemCodeSelected",systemCodeSelected);
			    session.setAttribute("systemObjectAsHashMap",systemObjectAsHashMap);
 
	%>

<%if(systemObjectAsHashMap != null) {%>
 <form Id="basicSOViewformData1">	
	<div  id="sourceViewBasicSearch1">                                            
			<table border="0" width="100%">
					<tr>
						<td>
						 <%if(operations.isSO_SearchView()){%>
 							<a href="javascript:void(0)" 
							   class="button" 
							   title="<%=bundle.getString("back_button_text")%>"
							   onclick="javascript:
							   if(editMinorObjectType.length<1){
							   showDivs('sourceViewBasicSearch');
							   hideDivs('sourceRecordSearchResult');							    
							   }else{showUnSavedAlert(event,editMinorObjectType);}"
							     >  							  
								<span><%=bundle.getString("back_button_text")%></span>								
							</a>  
 							
							<%}%>
 						</td>
						<th><b><%=bundle.getString("source_rec_status_but")%></b>&nbsp;:&nbsp;<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;">
						<%=compareDuplicateManager.getStatus((String)systemObjectAsHashMap.get("Status"))%> </font>
					 	</th>
						<th><b><%=bundle.getString("transaction_source")%></b>:	<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=sourceHandler.getSystemCodeDescription(systemCodeSelected)%></font></th>
						<th> <b><%=localIdDesignation%></b>:<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=lidSelected%></font></th>
					</tr>
			 </table>
			<%if(systemObjectAsHashMap!=null && systemObjectAsHashMap.size()>0){%>
			<!--Start Displaying the root node fields -->                                        
			<div class="minorobjects">                                                    
			   <table border="0" cellpadding="1" cellspacing="1" width="100%">
				 <tr><td class="tablehead" width="100%"><b><%=objScreenObject.getRootObj().getName()%></b> &nbsp; </td></tr>
				 <tr>
				   <td>
					<table border="0" cellpadding="1" cellspacing="1" >
				   <%  HashMap rootFieldValuesMap  = (HashMap) systemObjectAsHashMap.get("SYSTEM_OBJECT");
						for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
							FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
										%>  
					  <tr>
						<th align="left">
						  <%=fieldConfigMap.getDisplayName()%>
						</th>
						<td>
							<%if(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) != null && systemObjectAsHashMap.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>   
							   <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
							<%}else{%>
							   <%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : "&nbsp"%>
							 <%}%>
						</td>
					 </tr>
				   <%}%>
				   </table>
				</td>
				</tr>
				<!-- STARTDisplaying the minor object fields -->    
				<% String epathValue = new String();
				  for (int io = 0; io < arrObjectNodeConfig.length; io++) {
					 ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
					 ArrayList  minorObjectMapList =  (ArrayList) systemObjectAsHashMap.get("SOEDIT" + childObjectNodeConfig.getName() + "ArrayList");
				%>
				<tr><td>&nbsp;</td></tr>
				<tr><td class="tablehead" width="100%"><b><%=childObjectNodeConfig.getName()%></b> &nbsp; </td></tr>
				<tr>
				  <td> 
					 <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
					 <table border="0" width="100%" cellpadding="0">
					 <%
					  FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
					   HashMap minorObjectMap = new HashMap();
					   %>
						<%if(minorObjectMapList.size() == 0) {%>
						<tr class="odd">
						  <td><%=bundle.getString("source_rec_nodetails_text")%></td>
						</tr>
					   <%}%>
					   <%
					   for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
						 minorObjectMap = (HashMap) minorObjectMapList.get(ar);
						 String styleClass = ((ar%2==0)?"even":"odd");
					 %>

					   <%if(ar == 0) {%>
						<tr>			   
						   <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
							  <td class="tablehead">
								 <%=fieldConfigArrayMinor[k].getDisplayName()%>
							   </td>
						  <%}%>
						</tr> 
					  <%}%>

					 <tr class="<%=styleClass%>">
						<% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
						  <td>
						  <%if(minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()) != null ) {%>  <!--if has value-->
							<%if( systemObjectAsHashMap.get("hasSensitiveData") != null &&  fieldConfigArrayMinor[k].isSensitive() && !operations.isField_VIP()){%>   
							   <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
							<%}else{%>
								<%if(fieldConfigArrayMinor[k].getValueList() != null) {%> <!-- if the field config has value list-->
								  <%if (fieldConfigArrayMinor[k].getUserCode() != null){%> <!-- if it has user defined value list-->
									 <%=ValidationService.getInstance().getUserCodeDescription(fieldConfigArrayMinor[k].getUserCode(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
								  <%}else{%>
									<%=ValidationService.getInstance().getDescription(fieldConfigArrayMinor[k].getValueList(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
								 <%}%>
							   <%} else {%> <!--minorObjectMap- In other cases-->
							   <%
								String value = minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()).toString();   
								if (fieldConfigArrayMinor[k].getInputMask() != null && fieldConfigArrayMinor[k].getInputMask().length() > 0) {
								  if (value != null) {
									 //Mask the value as per the masking 
									 value = fieldConfigArrayMinor[k].mask(value.toString());
								   }
								} 
								%> 
								 <%=value%>
							   <%}%>
 							 <%}%>
 						  <%} else {%> <!-- else print &nbsp-->
							&nbsp;
						  <%}%>
						  </td>
					  <% } %>
					</tr>
 					 <%}%>

					 </table>
					 </div><!-- inner div -->
				  </td>
				</tr>
 				<%}%>
			<!-- End Displaying the minor object fields -->    
		</table>			
	  </div><!-- minorobjects div -->
 	  <%}else{%>
 		 			<div class="ajaxalert">				 
					  <table>
							<tr>
								<td>
									  <ul>
									 
										<% while (messagesIter.hasNext())   { %>
											 <li>
												<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
												<%= facesMessage.getSummary() %>												 
											 </li>
										 <% } %>
									  </ul>
								<td>
							<tr>
						</table>
					</div>

		<%}%>

	 <!--End Displaying the root node fields -->    
	 	<table>
				<tr><td>&nbsp;</td></tr>
				<tr>
				 
					<td>
						<!--Display edit link only when the system object-->
 						 
							<%if ("inactive".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>
								<%if(operations.isSO_Activate()){%>
									   <a href="javascript:void(0)" 
										   class="button"  
										   onclick="javascript:
										   ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&activateSO=true',
										   '<h:outputText value="#{childNodesName}"/>sourceRecordSearchResult',event);" 
										   title="<%=bundle.getString("source_rec_activate_but")%>"/>
										   <span><%=bundle.getString("source_rec_activate_but")%></span>
									   </a>  
								<%}%>
 							<%}%>
							<%if ("active".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>
						 		<%if(operations.isSO_Deactivate()){%>
								   <a href="javascript:void(0)" 
									   class="button"
									   onclick="javascript:
										ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&deactivateSO=true',
										'<h:outputText value="#{childNodesName}"/>sourceRecordSearchResult',event);" 
									   title="<%=bundle.getString("source_rec_deactivate_but")%>" >
									   <span><%=bundle.getString("source_rec_deactivate_but")%></span>
								   </a>  
								<%}%>
								
								<%if( operations.isSO_Edit()){%>							
									<a href="javascript:void(0)" 
									   class="button"  
									   title="<%=bundle.getString("source_rec_edit_but")%>"
									   onclick="javascript:getFormValues('basicViewformData');
									   ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&editSO=true',
									   'sourceRecordSearchResult',
									   event);" >   
									   <span><%=bundle.getString("source_rec_edit_but")%></span>							
									</a>  
 								<%}%> 
 							 <%}%>
							 

 					</td>					
					<td>
						 <%if( operations.isSO_SearchView()){%>
						 <%String euid = sourceHandler.viewEUID(systemCodeSelected,lidSelected);%>
 							<a 
							href="javascript:void(0);"
							onclick='javascript:if(editMinorObjectType.length<1){
									 href="/<%=URI%>/euiddetails.jsf?euid=<%=euid%>" 
									 document.getElementById("viewEuidDiv").style.visibility = "visible";
							         document.getElementById("viewEuidDiv").style.display  = "block";
									 }else{
									 showUnSavedAlert(event,editMinorObjectType);
									 
									 }'  
							   class="button"
							   title="<%=bundle.getString("source_rec_vieweuid_but")%>"
							   >  
							<span><%=bundle.getString("source_rec_vieweuid_but")%></span>
							</a>  
							<%}%>
 					</td>
				</tr> 
				<tr><td>&nbsp;</td></tr>
				
			</table>
		</div>
		<script>hideDivs('sourceViewBasicSearch');</script>
		<%} else {%> <!-- if system object not found--->
					<script>showDivs('sourceViewBasicSearch');</script>
		 			<div class="ajaxalert">				 
					  <table>
							<tr>
								<td>
									  <ul>
									 
										<% while (messagesIter.hasNext())   { %>
											 <li>
												<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
												<%= facesMessage.getSummary() %>												 
											 </li>
										 <% } %>
									  </ul>
								<td>
							<tr>
						</table>
					</div>
 
		<%}%>

	<%} else if(request.getParameter("editSO")!=null && request.getParameter("editSO").equals("true")){
 		String lidSelected = (String)session.getAttribute("lidSelected") ;
		String systemCodeSelected = (String)session.getAttribute("systemCodeSelected") ;
		sourceAddHandler.editLID(systemCodeSelected,lidSelected);
		
		systemObjectAsHashMap = sourceAddHandler.getNewSOHashMap();

 	%>
 
	 
			 <div id="sourceViewBasicSearch2">
			<!-- Start Status div-->
			<div id='edistatusdisplay'>
				<table border=0 width="100%">
					<tr>
						<td>
							<h:form>
								<table border="0" cellpadding="1" cellspacing="1" width="100%">

						<tr>
							<td>  
							<%//if(operations.SO_SearchView){%>
							   <a href="javascript:void(0)" 
							   class="button"
							   onclick="javascript:
							   if(editMinorObjectType.length<1){
							   showDivs('sourceViewBasicSearch');
							   hideDivs('sourceRecordSearchResult');
 							   }else{showUnSavedAlert(event,editMinorObjectType)}"
							   title="<%=bundle.getString("back_button_text")%>">							   
										<span><%=bundle.getString("back_button_text")%></span>								
					        </a>  
							<%//}%>	
							</td>
							<th><b><%=bundle.getString("source_rec_status_but")%></b>&nbsp;:&nbsp;<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;">
							   <%=compareDuplicateManager.getStatus((String)systemObjectAsHashMap.get("Status"))%> </font>
							</th>
							<th><b><%=bundle.getString("transaction_source")%></b>:	<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=sourceHandler.getSystemCodeDescription(systemCodeSelected)%> </font></th>
							<th><b><%=localIdDesignation%></b>:<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=systemObjectAsHashMap.get("LID")%></font></th>
						</tr>
									
								</table>    
							</h:form>
						</td>
						<td><div id="editFormValidate"></div>
						</td>
					</tr>
				</table>
 			</div>
			<!-- Status div-->
																	 
				   <table border="0" " width="100%" >
					<%if ("active".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>
					<tr>
						<td class="tablehead" colspan="2">
							<%=objScreenObject.getRootObj().getName()%>                  
						</td>
					</tr>
					<%}%>
									 
					<tr>
						<td align="left">
							<% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
							<h:messages  styleClass="errorMessages"  layout="list" />
							<%}%>
						</td>
					</tr>
					<tr>
						<td>
							<%if ("active".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>
						   <table>
							<tr>
							<td style="font-size:10px;">
								 <nobr>
									 <span style="font-size:12px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
								</nobr>
						    </td>
						  </tr> 
						  </table> 

							<h:form id="BasicSearchFieldsForm">
							  <!-- Start EDIT Fields-->
							  <!--Start Displaying the person fields -->                                        
							<form id="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">

								<h:dataTable  id="hashIdEdit" 
											  var="fieldConfigPerAdd" 
											  value="#{SourceHandler.rootNodeFieldConfigs}">
									<h:column>
										 <h:outputText rendered="#{fieldConfigPerAdd.required}">
											<span style="font-size:12px;color:red;verticle-align:top">*</span>
										</h:outputText>													  
										<h:outputText rendered="#{!fieldConfigPerAdd.required}">
											<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
										</h:outputText>													  
										<h:outputText value="#{fieldConfigPerAdd.displayName}" />
										<h:outputText value=":"/>
									  </h:column>
									<!--Rendering HTML Select Menu List-->
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive}" >
										<h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" 
														 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
											<f:selectItem itemLabel="" itemValue="" />
											<f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
										</h:selectOneMenu>
									</h:column>
									
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive}" >
										<h:selectOneMenu 
												readonly="true" 
												disabled="true" 
												rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP }">
											<f:selectItem itemLabel="" itemValue="" />
										</h:selectOneMenu>
										<h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" 
														 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP}"
														 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
											<f:selectItem itemLabel="" itemValue="" />
											<f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
										</h:selectOneMenu>      
									</h:column>
									
									<!--Rendering Updateable HTML Text boxes-->
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive}" >
									   
										<h:inputText label="#{fieldConfigPerAdd.displayName}"  
													  value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
													 title="#{fieldConfigPerAdd.fullFieldName}"
													 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
													 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
													 maxlength="#{fieldConfigPerAdd.maxLength}"
													 onkeyup="javascript:qws_field_on_key_up(this)" 
													onfocus="javascript:clear_masking_on_focus()" required="#{fieldConfigPerAdd.required}"
													rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' ||  Operations.field_VIP}"/>
									
										<h:inputText label="#{fieldConfigPerAdd.displayName}"  
													 value="#{msgs.SENSITIVE_FIELD_MASKING}"
													 readonly="true"
													 disabled="true"
													rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP && SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName] ne null}"/>	

										<h:inputText label="#{fieldConfigPerAdd.displayName}"  
													 readonly="true"
													 disabled="true"
													rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' &&  !Operations.field_VIP && SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName] eq null}"/>	

									</h:column>                     

									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive}" >
										<h:inputText label="#{fieldConfigPerAdd.displayName}"  
													 id="fieldConfigIdTextbox"  
													 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
													 title="#{fieldConfigPerAdd.fullFieldName}"
													 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
													 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
													 maxlength="#{fieldConfigPerAdd.maxLength}"
													 onkeyup="javascript:qws_field_on_key_up(this)" 
													onfocus="javascript:clear_masking_on_focus()" required="#{fieldConfigPerAdd.required}"/>
									</h:column>                     
									<!--Rendering Updateable HTML Text boxes date fields-->
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && !fieldConfigPerAdd.sensitive}">
										
										<nobr><!--Sridhar -->
											<input type="text" 
												   title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
												   value="<h:outputText value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
												   id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
												   required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
												   maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
												   onblur="javascript:validate_date(this,'<%=dateFormat%>')"
												   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
												   onkeyup="javascript:qws_field_on_key_up(this)" >
										   <a href="javascript:void(0);" 
						 title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
						 onclick="g_Calendar.show(event,
							  '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
							  '<%=dateFormat%>',
							  '<%=global_daysOfWeek%>',
							  '<%=global_months%>',
							  '<%=cal_prev_text%>',
							  '<%=cal_next_text%>',
							  '<%=cal_today_text%>',
							  '<%=cal_month_text%>',
							  '<%=cal_year_text%>')" 
							  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
					  <font class="dateFormat">(<%=dateFormat%>)</font>
										</nobr>
											
											
									</h:column>

									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && fieldConfigPerAdd.sensitive && (SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP) }">
										<nobr><!--Sridhar -->
											<input type="text" 
												   title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
												   value="<h:outputText value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
												   id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
												   required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
												   maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
												   onblur="javascript:validate_date(this,'<%=dateFormat%>');"
												   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
												   onkeyup="javascript:qws_field_on_key_up(this)" >
										   <a href="javascript:void(0);" 
						 title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
						 onclick="g_Calendar.show(event,
							  '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
							  '<%=dateFormat%>',
							  '<%=global_daysOfWeek%>',
							  '<%=global_months%>',
							  '<%=cal_prev_text%>',
							  '<%=cal_next_text%>',
							  '<%=cal_today_text%>',
							  '<%=cal_month_text%>',
							  '<%=cal_year_text%>')" 
							  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
					  <font class="dateFormat">(<%=dateFormat%>)</font>
										</nobr>
									</h:column>

									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true'  && fieldConfigPerAdd.sensitive && !Operations.field_VIP }">
										<nobr><!--Sridhar -->
											<input type="text" 
													value="<h:outputText value="#{msgs.SENSITIVE_FIELD_MASKING}"/>"
												   id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
												   readonly="true" 
												   disabled="true" 
												   maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
												   ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/><font class="dateFormat">(<%=dateFormat%>)</font>
										</nobr>
									</h:column>

									<!--Rendering Updateable HTML Text Area-->
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive }" >
										<h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
														 title="#{fieldConfigPerAdd.fullFieldName}"
														 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
														 id="fieldConfigIdTextArea"   
														 required="#{fieldConfigPerAdd.required}"
														  />
									</h:column>
									<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive }" >
										<h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
														 readonly="true"
														 disabled="true"
														 value="#{msgs.SENSITIVE_FIELD_MASKING}" 
														 required="#{fieldConfigPerAdd.required}"
														 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP}"
														 />
										<h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
														 title="#{fieldConfigPerAdd.fullFieldName}"
														 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}" 
														 required="#{fieldConfigPerAdd.required}"
														 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP}"
														 />
									</h:column>
										
								</h:dataTable>
							</form>
								
							<!--End Displaying the person fields -->    
							<!--Minor Object fields here -->     
							<h:dataTable  id="allChildNodesNamesAdd" 
										  width="100%"
										  var="childNodesName" 
										  value="#{SourceHandler.allChildNodesNames}">
								 <h:column>
									<table width="100%">
										<tr>
											<td class="tablehead" colspan="2">
												<h:outputText value="#{childNodesName}"/>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
											</td>
										</tr>
											
										<tr>
										<!-- modified by Bhat on 22-09-08 for editMinorObjectType.length validation -->
											<td colspan="2">
												<a title="<h:outputText value="#{msgs.source_rec_view}"/>&nbsp;<h:outputText value="#{childNodesName}"/> " href="javascript:void(0)" onclick="javascript:if(editMinorObjectType.length<1){
												showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddDiv');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?&MOT=<h:outputText value="#{childNodesName}"/>&load=load&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv','')
												}else{showUnSavedAlert(event,editMinorObjectType)}" class="button">
												<span>
													<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;View <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="<h:outputText value="#{msgs.source_submenu_add}"/>  <h:outputText value="#{childNodesName}"/>"/>
												</span>
													
											</td>
										</tr>
										<tr><td>
												<div id="extra<h:outputText value='#{childNodesName}'/>AddDiv"  style="visibility:hidden;display:none;">
													<table>
														<tr>
															<td colspan="2" align="left">
																<form id="<h:outputText value="#{childNodesName}"/>InnerForm" name="<h:outputText value="#{childNodesName}"/>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
																	<h:dataTable  headerClass="tablehead" 
																				  id="allNodeFieldConfigsMapAdd" 
																				  var="allNodeFieldConfigsMapAdd" 
																				  width="100%"
																				  value="#{SourceHandler.allNodeFieldConfigs}">
																		<h:column>
																			<h:dataTable  headerClass="tablehead" 
																						  id="childFieldConfigsAdd" 
																						  var="childFieldConfigAdd" 
																						  width="100%"
																						  value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
																							  
																				<h:column>

																 <h:outputText rendered="#{childFieldConfigAdd.required}">
																	<span style="font-size:12px;color:red;verticle-align:top">*</span>
																</h:outputText>													  
																<h:outputText rendered="#{!childFieldConfigAdd.required}">
																	<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
																</h:outputText>													  
																<h:outputText value="#{childFieldConfigAdd.displayName}" />
																<h:outputText value=":"/>

																				</h:column>
																				<!--Rendering HTML Select Menu List-->
				<!--Rendering HTML Select Menu List-->
			  <!--user code related changes starts here-->
				<h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
					<!-- User code fields here -->
					<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" onchange="getFormValues('#{childNodesName}InnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand=+RAND_VAL+&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
					rendered="#{childFieldConfigAdd.userCode ne null}">
						<f:selectItem itemLabel="" itemValue="" />
					   <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
					</h:selectOneMenu>    
					
					<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" 
									 rendered="#{childFieldConfigAdd.userCode eq null}">
						<f:selectItem itemLabel="" itemValue="" />
						<f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
					</h:selectOneMenu>
				</h:column>

				<!--Rendering Updateable HTML Text boxes-->
				<h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
			   
								<h:inputText label="#{childFieldConfigAdd.displayName}"  
											 title="#{childFieldConfigAdd.fullFieldName}"
											 onkeydown="javascript:qws_field_on_key_down(this, userDefinedInputMask)"
											  maxlength="#{childFieldConfigAdd.maxLength}"
											onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
											 onkeyup="javascript:qws_field_on_key_up(this)" 
											 required="#{childFieldConfigAdd.required}"
											 rendered="#{childFieldConfigAdd.constraintBy ne null}"
											 />     
											 
											 <h:inputText label="#{childFieldConfigAdd.displayName}"  
											 title="#{childFieldConfigAdd.fullFieldName}"
											 onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
											  maxlength="#{childFieldConfigAdd.maxLength}"
											onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
											 onkeyup="javascript:qws_field_on_key_up(this)" 
											 required="#{childFieldConfigAdd.required}"
											 rendered="#{childFieldConfigAdd.constraintBy eq null}"
											 />

			  </h:column>                     
			  <!--user code related changes ends here-->
			  
			  <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
																					<nobr>
																						<input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
																							   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
																							   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
																							   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
																							   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
																							   onkeyup="javascript:qws_field_on_key_up(this)" 
																							   onblur="javascript:validate_date(this,'<%=dateFormat%>');">
																						<a href="javascript:void(0);" 
						 title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>"
						 onclick="g_Calendar.show(event,
							  '<h:outputText value="#{childFieldConfigAdd.name}"/>',
							  '<%=dateFormat%>',
							  '<%=global_daysOfWeek%>',
							  '<%=global_months%>',
							  '<%=cal_prev_text%>',
							  '<%=cal_next_text%>',
							  '<%=cal_today_text%>',
							  '<%=cal_month_text%>',
							  '<%=cal_year_text%>')" 
							  ><img  border="0"  title="<h:outputText value="#{childFieldConfigAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
					  <font class="dateFormat">(<%=dateFormat%>)</font>
																					</nobr>
																				</h:column>                     
																					
																					
																				<!--Rendering Updateable HTML Text Area-->
																					
																				<h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
																					<h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
																									 required="#{childFieldConfigAdd.required}" />
																				</h:column>
																			</h:dataTable>                                                                                
																		</h:column>
																	</h:dataTable>                                                                                
																		
																</form>
															</td>
														</tr>
														<!--EDIT SO buttons START-->
														<tr>                                                                
														<!-- modified by Bhat on 22-09-08 to  set editMinorObjectType = '' -->                                                
														  <td colspan="2">
															   <nobr> 
															   <table>
															     <tr>
																   <td>
															   <a title=" <h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/>"  href="javascript:void(0);" class="button" onclick="javascript:editMinorObjectType='';getFormValues('<h:outputText value="#{childNodesName}"/>InnerForm');
															   ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<%=systemObjectAsHashMap.get("LID")%>&SYS=<%=systemObjectAsHashMap.get("SYSTEM_CODE")%>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv',event);
															   ">
																		 <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/> </span>
																	 </a>     
																	 </td>
																	 <td>
																	  <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:editMinorObjectType='';ClearContents('#{childNodesName}InnerForm');setEditIndex('-1')">
																		   <span><h:outputText value="#{msgs.clear_button_label}"/></span>
																	   </h:outputLink> 
																	 </td>
																	 <td>
																	   <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelEdit">
																		  <a href="javascript:void(0);" 
																			 onclick="javascript:editMinorObjectType='';setEditIndex('-1');cancelEdit('<h:outputText value="#{childNodesName}"/>InnerForm', '<h:outputText value='#{childNodesName}'/>cancelEdit', '<h:outputText value='#{childNodesName}'/>');"	
																			 class="button"
																		     title="<h:outputText value="#{msgs.source_rec_cancel_but}"/> <h:outputText value='#{childNodesName}'/>">
<span><h:outputText value="#{msgs.source_rec_cancel_but}"/>&nbsp;<h:outputText value='#{childNodesName}'/></span>
																		   </a>   
																		</div>
																	 </td>
																	 </tr>
																	 </table>
																 </nobr>																		    
														  </td>
														</tr>
														<!--EDIT SO buttons ENDS-->
													</table>   
												</div>  
										</td></tr>
											
										<tr>
											<td colspan="2">
												<div id="stealth" style="visibility:hidden;display:none;"></div>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="<h:outputText value="#{childNodesName}"/>NewDiv" >
												</div>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="<h:outputText value="#{childNodesName}"/>AddDiv"></div>
											</td>
										</tr>
											
									</table>   
								</h:column>
							</h:dataTable>
							<!-- End Display minor objects fields --> 
							<!-- End Edit Acive SO -->
							 </h:form>
				 
							  <!-- End EDIT Fields-->														   
							 <%	} else if ("inactive".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status")) || "merged".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>          
									   <!-- Start READ ONY Fields-->
									   <!--Start Displaying the root node fields -->                                        
				<!--Start Displaying the root node fields -->                                        
				<div class="minorobjects">                                                    
				   <table border="0" cellpadding="1" cellspacing="1" width="100%">
					 <tr><td class="tablehead" width="100%"><b><%=objScreenObject.getRootObj().getName()%></b> &nbsp; </td></tr>
					 <tr>
					   <td>
						<table border="0" cellpadding="1" cellspacing="1" >
					   <%  HashMap rootFieldValuesMap  = (HashMap) systemObjectAsHashMap.get("SYSTEM_OBJECT");
							for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
								FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
											%>  
						  <tr>
							<th align="left">
							  <%=fieldConfigMap.getDisplayName()%>
							</th>
							<td>
							<%if(fieldConfigMap.getGuiType().equalsIgnoreCase("TextBox")) {%>
							  <input type="text" title="<%=fieldConfigMap.getDisplayName()%>" style="background:#efefef;border: 1px inset;" value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>"  readonly="true" disabled="true"/>
							<%}else if(fieldConfigMap.getGuiType().equalsIgnoreCase("TextArea")) {%>
							   <textarea title="<%=fieldConfigMap.getDisplayName()%>" disabled="true" readonly="true" style="background:#efefef;border: 1px inset;" >
								  <%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>
							   </textarea>
							<%}else if(fieldConfigMap.getGuiType().equalsIgnoreCase("MenuList")) {%>
							  <select readonly="true" disabled="true" style="background:#efefef;border: 1px inset;"  title="<%=fieldConfigMap.getDisplayName()%>"> 
								<option value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : "&nbsp"%>"><%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%></option>
							   </select>                                              
							 <%} else {%>
							  <input type="text" title="<%=fieldConfigMap.getDisplayName()%>" value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>"  readonly="true" disabled="true" style="background:#efefef;border: 1px inset;" />
							<%}%>
							</td>
						 </tr>
					   <%}%>
					   </table>
					</td>
					</tr>
					<!-- STARTDisplaying the minor object fields -->    
					<% String epathValue = new String();
					  for (int io = 0; io < arrObjectNodeConfig.length; io++) {
						 ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
						 ArrayList  minorObjectMapList =  (ArrayList) systemObjectAsHashMap.get("SOEDIT" + childObjectNodeConfig.getName() + "ArrayList");
					%>
					<tr><td>&nbsp;</td></tr>
					<tr><td class="tablehead" width="100%"><b><%=childObjectNodeConfig.getName()%></b> &nbsp; </td></tr>
					<tr>
					  <td> 
						 <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
						 <table border="0" width="100%" cellpadding="0">
						 <%
						  FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
						   HashMap minorObjectMap = new HashMap();
						   %>
							<%if(minorObjectMapList.size() == 0) {%>
							<tr class="odd">
							  <td><%=bundle.getString("source_rec_nodetails_text")%></td>
							</tr>
						   <%}%>
						   <%
						   for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
							 minorObjectMap = (HashMap) minorObjectMapList.get(ar);
							 String styleClass = ((ar%2==0)?"even":"odd");
						 %>

						   <%if(ar == 0) {%>
							<tr>			   
							   <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
								  <td class="tablehead">
									 <%=fieldConfigArrayMinor[k].getDisplayName()%>
								   </td>
							  <%}%>
							</tr> 
						  <%}%>

						 <tr style="background:#efefef;" >
							<% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
							  <td>
							  <%if(minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()) != null ) {%>  <!--if has value-->
								   <%if(fieldConfigArrayMinor[k].getValueList() != null) {%> <!-- if the field config has value list-->
									  <%if (fieldConfigArrayMinor[k].getUserCode() != null){%> <!-- if it has user defined value list-->
										 <%=ValidationService.getInstance().getUserCodeDescription(fieldConfigArrayMinor[k].getUserCode(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
									  <%}else{%>
										<%=ValidationService.getInstance().getDescription(fieldConfigArrayMinor[k].getValueList(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
									 <%}%>
								   <%} else {%> <!--minorObjectMap- In other cases-->
								   <%
									String value = minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()).toString();   
									if (fieldConfigArrayMinor[k].getInputMask() != null && fieldConfigArrayMinor[k].getInputMask().length() > 0) {
									  if (value != null) {
										 //Mask the value as per the masking 
										 value = fieldConfigArrayMinor[k].mask(value.toString());
									   }
									} 
									%> 
									 <%=value%>
								   <%}%>
							  <%} else {%> <!-- else print &nbsp-->
								&nbsp;
							  <%}%>
							  </td>
						  <% } %>
						</tr>
							  
						 <%}%>

						 </table>
						 </div>
					  </td>
					</tr>
					<%}%>
				<!--End Displaying the minor object fields -->    
			</table>

		   </div>

		 <!--End Displaying the root node fields -->    
							   <!-- End Display minor objects fields --> 
							   <!-- End READ ONLY Fields-->
							   
			<%}%>

			<h:form>
							<table>  
								<tr>       
									<% if ("active".equalsIgnoreCase((String)systemObjectAsHashMap.get("Status"))) {%>          
										
									<td>
									<!-- modified by Bhat on 22-09-08 for editMinorObjectType.length validation -->
										<!-- Edit Submit button-->
										
									   <a href="javascript:void(0)" 
									   class="button"  
									   title="<%=bundle.getString("source_rec_save_but")%>"
										onclick="javascript:if(editMinorObjectType.length<1){
										getFormValues('BasicSearchFieldsForm');
										ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','editFormValidate','');
										}else{showUnSavedAlert(event,editMinorObjectType)}">
									   <span><%=bundle.getString("source_rec_save_but")%></span>
									   </a>   
									</td>                                                                
									<td>
										<!-- Edit CANCEL button-->
										<!--   action="SourceHandler.cancelEditLID" 
												actionListener="SourceHandler.removeSingleLID"   -->
							 
									   <a href="javascript:void(0)" 
									   class="button"  
									   title="<%=bundle.getString("cancel_but_text")%>"	
									   onClick="javaScript:editMinorObjectType='';setEditIndex('-1');
									   showDivs('sourceViewBasicSearch');hideDivs('sourceRecordSearchResult');">
									   <span><%=bundle.getString("cancel_but_text")%></span>
									   </a>  
							  
									</td>
									<%}%>                                                                         
									<td>
									 
									<%//if(operations.EO_SearchViewSBR){%>
									<%String euid = sourceHandler.viewEUID(systemCodeSelected,lidSelected);%>
									 <a 
									 href="javascript:void(0);"
									 onclick='javascript:if(editMinorObjectType.length<1){
									 document.getElementById("viewEuidDiv").style.visibility = "visible";
							         document.getElementById("viewEuidDiv").style.display  = "block";
									 href="/<%=URI%>/euiddetails.jsf?euid=<%=euid%>" 
									 }else{
									 showUnSavedAlert(event,editMinorObjectType);									 
									 }' 
									   class="button"  
									   title="<%=bundle.getString("source_rec_vieweuid_but")%>"	
									   >
									   <span><%=bundle.getString("source_rec_vieweuid_but")%></span>
									   </a>   
									<%//}%>	                                     
									</td>
									<td> 
									      
									</td>
								   
								</tr>
							</table>
							</h:form>    <!-- close Action button on Edit tab -->
						</td>
					</tr>
				</table>                                                
		</div> 
		<%}else if(request.getParameter("deactivateSO")!=null && request.getParameter("deactivateSO").equals("true")){%>
		
				<%
 					Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
					String lidSelected = (String)session.getAttribute("lidSelected");
					String systemCodeSelected = (String)session.getAttribute("systemCodeSelected");
 					String deactivateMsg = sourceHandler.deactivateSO(systemCodeSelected,lidSelected);
					if(deactivateMsg!=null){ 
 				%> 		
				<table>
				  <tr>
				    </td>
					<script>						 
						 ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand=<%=rand%>&SystemCode=<%=systemCodeSelected%>&<%=localIdDesignation%>=<%=lidSelected%>&viewSO=true','sourceRecordSearchResult','');
						 showMessage("<%=sourceHandler.getSystemCodeDescription(systemCodeSelected)%>/<%=lidSelected%> <%=bundle.getString("deactivate_success_message_text")%>");
					</script>  
				    </td>
				  </tr>
				</table>
				<%} else{ %>
 					<div class="ajaxalert">				 
					  <table>
							<tr>
								<td>
									  <ul>									 
										<% while (messagesIter.hasNext())   { %>
											 <li>
												<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
												<%= facesMessage.getSummary() %>												 
											 </li>
										 <% } %>
									  </ul>
								<td>
							<tr>
						</table>
					</div>
			<%}//end of deactivateMsg!=null%>
				
		<%}else if(request.getParameter("activateSO")!=null && request.getParameter("activateSO").equals("true")){%>
		
				<%
 					Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
					String lidSelected = (String)session.getAttribute("lidSelected");
					String systemCodeSelected = (String)session.getAttribute("systemCodeSelected");
  					String activateMsg = sourceHandler.activateSO(systemCodeSelected,lidSelected);
 					if(activateMsg!=null){ 
 				%>
				<table>
				  <tr>
				    </td>
					<script>
						 ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand=<%=rand%>&SystemCode=<%=systemCodeSelected%>&<%=localIdDesignation%>=<%=lidSelected%>&viewSO=true','sourceRecordSearchResult','');
						 showMessage("<%=sourceHandler.getSystemCodeDescription(systemCodeSelected)%>/<%=lidSelected%> <%=bundle.getString("activate_success_message_text")%>");
					</script> 
					</td>
				</tr>
			  </table>

				<%} else{ %>
 					<div class="ajaxalert">				 
					  <table>
							<tr>
								<td>
									  <ul>									 
										<% while (messagesIter.hasNext())   { %>
											 <li>
												<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
												<%= facesMessage.getSummary() %>												 
											 </li>
										 <% } %>
									  </ul>
								<td>
							<tr>
						</table>
					</div>
			<%}//end of activateMsg!=null%>
					
		<%} // end of if(request.getParameter("activateSO")!=null%>
 	</form> <!-- basicSOViewformData1 -->
 <%} //end of is sessionActive%>
         <!-- Added By Narahari.M on 23-09-2008 for all information popups -->
  		 <div id="successDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
               <form id="successDiv">
                <table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<th align="center" title="<h:outputText value="#{msgs.move}"/>"><h:outputText value="#{msgs.popup_information_text}"/></th>
				<th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:getFormValues('basicViewformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&editSO=true','sourceRecordSearchResult',event);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:getFormValues('basicViewformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&editSO=true','sourceRecordSearchResult',event);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
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
									<a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.ok_text_button}" />" onclick="javascript:getFormValues('basicViewformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&editSO=true','sourceRecordSearchResult',event);"><span><h:outputText value="#{msgs.ok_text_button}"/></span></a>
								</td>
							</tr>
						</table>
					  </td>
					</tr>
                </table> 
                </form>
            </div> 
	<script>
	dd_successDiv=new YAHOO.util.DD("successDiv");
	</script>

</f:view>

</html>