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
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Iterator"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>


 
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler"%>

<%

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

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Merge Tree</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
     </head>
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
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
double rand = java.lang.Math.random();
Enumeration parameterNames = request.getParameterNames();
/*if(parameterNames != null)   {
    while(parameterNames.hasMoreElements() )   {
          String attributeName = (String) parameterNames.nextElement();
          String parameterValue = (String) request.getParameter(attributeName);
   }
}
*/

Operations operations = new Operations();


HttpSession session1 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
ArrayList minorObjectsAddList = (ArrayList)session1.getAttribute("minorObjectsAddList");

 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
  String editTitle = bundle.getString("source_rec_edit_but");
  String deleteTitle = bundle.getString("source_rec_delete_but");

HashMap thisMinorObject = new HashMap();
SourceAddHandler  sourceAddHandler   = (SourceAddHandler)session1.getAttribute("SourceAddHandler");
SourceHandler  sourceHandler   = (SourceHandler)session1.getAttribute("SourceHandler");

ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
String rootNodeName = objScreenObject.getRootObj().getName();
//get Field Config for the root node
FieldConfig[] fcRootArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
MasterControllerService masterControllerService = new MasterControllerService();
SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");

MidmUtilityManager midmUtilityManager = new MidmUtilityManager(); // Fix for #254

//String URI = request.getRequestURI();
//URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
//URI = URI.replaceAll("/ajaxservices","");

//Variables for load
String isLoadStr = request.getParameter("load");
boolean isLoad = (null == isLoadStr?false:true);
//Variables required for Delete
String deleteIndex = request.getParameter("deleteIndex");
boolean isDelete = (null == deleteIndex?false:true);

//Variables for Save
String minorObjSave = request.getParameter("minorObjSave");
boolean isminorObjSave = (null == minorObjSave?false:true);

//Variables required for Edit
String editIndex = request.getParameter("editIndex");
boolean isEdit = (null == editIndex?false:true);

//Variable for display long field value, fix for long string value display
String displayFieldValue = "";

//Variables for Validate LID
String validate = request.getParameter("validate");
boolean isValidate = (null == validate?false:true);
String validateLID = request.getParameter("LID");
String validateSystemCode  = request.getParameter("SystemCode");

//HashMap for the root node fields
HashMap rootNodesHashMap = (HashMap) sourceAddHandler.getNewSOHashMap().get("SYSTEM_OBJECT");
//Variables for adding new source fields
String saveString = request.getParameter("save");
boolean isSave= (null == saveString?false:true);

//Save Edited Values
//Variables for adding new source fields
String saveEditedValues= request.getParameter("editThisID");
boolean isSaveEditedValues = false;
if (saveEditedValues != null && !("-1".equalsIgnoreCase(saveEditedValues)))   {
	isSaveEditedValues = true;
}

if(isSave) {
	isSaveEditedValues = false;
%>
          <script> 
            setEditIndex('-1')
          </script>

<%
}
%>


<f:view>
<body>	
<%   boolean isValidationErrorOccured = false;
     ArrayList requiredValuesArray = new ArrayList();
	 HashMap valiadtions = new HashMap();


while(parameterNames.hasMoreElements() && !isLoad && !isEdit && !isValidate && !isSaveEditedValues)   { 
//  while(parameterNames.hasMoreElements() && ( (isminorObjSave || isSave) || (!isEdit && !isValidate && !isSaveEditedValues) ) )   { %>
               <% String attributeName = (String) parameterNames.nextElement(); %>
               <% String attributeValue = (String) request.getParameter(attributeName); %>
   		   <%
				if (isminorObjSave) { 
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
                      for(int k=0;k<fcArray.length;k++) {
						  //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
    						 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 

                         //--------------------------Is Numeric Validations -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) &&
							   (fcArray[k].getValueType() == 0 || 
							    fcArray[k].getValueType() == 4 || 
							    fcArray[k].getValueType() == 7))   {
							 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------
						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							 
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
								 if(fcArray[k].getValueType()==6){		
										valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +ConfigManager.getDateFormat());
								 }else{
									  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());
								 }
                                 								  
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------

                     }
				      if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                      if (!isValidationErrorOccured && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
                           thisMinorObject.put(attributeName, attributeValue);  //Add minorObject to the HashMap
					  }
                 }  else if (isSave) {   //Final Save hence add Root fields to the Hashmap
					  //validate all the mandatory fields before adding to the hashmap
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
                      for(int k=0;k<fcArray.length;k++) {
						  //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
    						 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 

                         //--------------------------Is Numeric Validations -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID"))   {continue;}  // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                         
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName)  &&
							      (fcArray[k].getValueType() == 0 || 
							       fcArray[k].getValueType() == 4 || 
							       fcArray[k].getValueType() == 7))   {
							 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------			
						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							 
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
								 if(fcArray[k].getValueType()==6){		
										valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +ConfigManager.getDateFormat());
								 }else{
									  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());
								 }
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------
                     }
					  

				      if (!isValidationErrorOccured && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
						  //if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                          //rootNodesHashMap.put(attributeName, attributeValue); 


						   if (rootNodesHashMap.get(attributeName) == null) {
  						      if (attributeValue.equalsIgnoreCase("")) { 
								  continue; 
							  }	   	  	   
                              rootNodesHashMap.put(attributeName, attributeValue); 
						  } else {
                               if (attributeValue.equalsIgnoreCase("")) { 
                                 rootNodesHashMap.put(attributeName, null); 
							  } else {
                                rootNodesHashMap.put(attributeName, attributeValue); 
							  }
						  }			
					  }
				 }
			   %>
 <%  } %>
 
<% if (isminorObjSave) {
     thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
     thisMinorObject.put(MasterControllerService.MINOR_OBJECT_TYPE, request.getParameter("MOT"));
     thisMinorObject.put(MasterControllerService.SYSTEM_CODE, singleSystemObjectLID.getSystemCode());
     thisMinorObject.put(MasterControllerService.LID, singleSystemObjectLID.getLID());
}%>
<!--Validate all the mandatory fields in root node fields-->
<% if (!valiadtions.isEmpty()) {
	Object[] keysValidations = valiadtions.keySet().toArray();
       if (isSave) {   //Final Save hence add Root fields to the Hashmap
	%>
    <script>
		window.location = "#top";
	</script>
	<%}%>
	<div class="ajaxalert" id="message1">
   	   	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
					       <%for(int i =0 ;i<keysValidations.length;i++) {%>
				             <li>
							   <%=keysValidations[i]%> <%=valiadtions.get((String) keysValidations[i])%>.
				             </li>
							 <%}%>
				      </ul>
				<td>
			<tr>
		</table>
    </div>
<%}%>


<%if (!isValidationErrorOccured && isSave) { %>  <!-- Save System Object -->

	 
	 <% 
		// removefield masking here
		if(rootNodesHashMap.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(rootNodesHashMap, rootNodeName);
		ArrayList newFinalMinorArrayList  = new ArrayList();
		String isSuccess = sourceAddHandler.updateSO();
        Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
     %> 
	 <% if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(isSuccess))  { 
                EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObjectForSO(singleSystemObjectLID);
                //keep the EO revision number in session
                session.setAttribute("SBR_REVISION_NUMBER" + enterpriseObject.getEUID(), enterpriseObject.getSBR().getRevisionNumber());
				sourceAddHandler.editLID(singleSystemObjectLID);
   		  %>
     <table>
      <tr>
      <td>
      <script>
  		window.location = "#top";
		document.getElementById("successMessageDiv").innerHTML = "'<%=enterpriseObject.getEUID()%>' <%=bundle.getString("concurrent_mod_text")%>";
		document.getElementById("successDiv").style.visibility="visible";
		document.getElementById("successDiv").style.display="block";
        
		//reload the content of the source record here  
		getFormValues('basicViewformData');
		ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?&rand='+<%=rand%>+'&editSO=true','sourceRecordSearchResult',event);
      </script>
     </td>
	 </tr>
	</table>
  <%} else if ("UPDATE_SUCCESS".equalsIgnoreCase(isSuccess))  { %>			   <!-- // close the Minor objects 
			   // Close the Root node fields
			   // Hide the Save button -->
		  <script>
			   document.getElementById('saveButtons').style.visibility = 'hidden';
			   document.getElementById('saveButtons').style.display = 'none';

			   document.getElementById('addFormFields').style.visibility = 'hidden';
			   document.getElementById('addFormFields').style.display = 'none';

			   document.getElementById('validateButtons').style.visibility = 'visible';
			   document.getElementById('validateButtons').style.display = 'block';
			   //CLEAR ALL FORM FIELDS
			   ClearContents('basicValidateAddformData');
		  </script>
		  <script> 
            setEditIndex('-1')
         </script>
		  <% Object[] keysSetMinorObjects  = allNodeFieldConfigsMap.keySet().toArray();
            for(int j = 0 ;j <keysSetMinorObjects.length;j++) {
			   String key = (String) keysSetMinorObjects[j]; 
		   %>
          <%if(!key.equalsIgnoreCase(rootNodeName)) {%>
		  <script>
			   document.getElementById('<%=key%>InnerForm').reset();
    	       document.getElementById('<%=key%>buttonspan').innerHTML = 'Save '+ '<%=key%>';
               document.getElementById('<%=key%>cancelEdit').style.visibility = 'hidden';
               document.getElementById('<%=key%>cancelEdit').style.display = 'none';

			   document.getElementById('extra<%=key%>AddDiv').style.visibility = 'hidden';
               document.getElementById('extra<%=key%>AddDiv').style.display = 'none';

			   document.getElementById('<%=key%>NewDiv').innerHTML = '';               
		    </script>
		  <%}%>


		  <%
			}
          %>
 	  	  <!-- Navigate the user to the top of the page to see the messages-->
          <script>
				window.location = "#top";
  				document.getElementById("successMessageDiv").innerHTML = "<%=bundle.getString("source_record_update_success_text")%>";
 				document.getElementById("successDiv").style.visibility="visible";
				document.getElementById("successDiv").style.display="block";
  	      </script>
      <%} else {%>
	  <!-- Navigate the user to the top of the page to see the messages-->
      <script>
	   	 window.location = "#top";
	  </script>
	  <div class="ajaxalert" id="message2">
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
<% } else if (isLoad) {%>
  <script>
    setEditIndex('-1');
  </script>
 <!-- Get the minor Objects to display -->
  <%  
	  ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");
  %>	
 <!-- Regenerate the table -->
		  <% 
		  int minorObjCount = 0;
		  for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
				%>
	    					
		    <% if ( i == 0)  { %>
                    <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">
                          <table border="0" width="100%" cellpadding="0">		  		  
                         <input type="hidden" name="minorindex" value="<%=i%>" />
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {%>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
			              </tr>
                    <% } %>
				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>
			              <tr class="<%=styleClass%>">
			                    <td width="14px" valign="center">
								  <% 
									  String minorObjType = request.getParameter("MOT");
									
								  %>						  
 									<!-- modified  on 22-09-08 for editMinorObjectType.length validation -->
									  <a href="javascript:void(0)" title="<%=editTitle%>"
											 onclick='javascript:
											 if(editMinorObjectType.length>1 && editMinorObjectType!="<%=minorObjType%>" ){
											 showUnSavedAlert(event,editMinorObjectType);
											 }else{ setMinorObjectAddressType("<%=minorObjType%>","<%=i%>");
											 unsavedEditMinorObjectType="<%=minorObjType%>";
 											 hideDivs("activeHeaders");showDivs("inactiveHeaders");
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")
											 }'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
								<!-- modified  on 22-09-08 for editMinorObjectType.length validation -->
							   <td width="14px" valign="center">							   
									  <a href="javascript:void(0)"  title="<%=deleteTitle%>"
											 onclick='if(editMinorObjectType.length<1){
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")
											 }else{showUnSavedAlert(event,editMinorObjectType)}'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								    <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( sourceAddHandler.getNewSOHashMap().get("hasSensitiveData")  != null && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
												  
													<!-- start fix for long string value display -->	
													<% String fieldValue = ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()));
														 if (fieldValue != null && fieldValue.length()>20) {
														 		 displayFieldValue = fieldValue.substring(0,20);
														 } else  {
														     displayFieldValue = fieldValue;
														 }
													%>
					   					   <%=displayFieldValue%><a href="javascript:void(0)" style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fcArray[k].getFullFieldName()%>:  <%=fieldValue%>">&nbsp&nbsp&nbsp&nbsp...</a>
													<!-- end fix for long string value display -->	
													
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>						   <% } %>
						   </tr>	
                    <% } %>
		    <% if ( i == thisMinorObjectList.size()-1)  { %>
     </table>  
</div>
<% } %>
						   
              <% }  %>

<% if (thisMinorObjectList.size() == 0 ) { %>
         <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">
         <table border="0" width="100%" cellpadding="0" align="center">		  		  
						 <tr>
						   <td><%= request.getParameter("MOT") %> <%=bundle.getString("no_minor_objects_text")%>
						   </td>
						 </tr>
         </table>  
       </div>

<% } %>

 <!-- End Regenerate -->

<% } else if (isDelete) { %>   <!-- Delete Minor Object  -->
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none';
	setEditIndex('-1');
    </script>
		  <% 
		      ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");	
              thisMinorObject = (HashMap)thisMinorObjectList.get(new Integer(deleteIndex).intValue());
	          String thisminorObjectType = (String)thisMinorObject.get(MasterControllerService.HASH_MAP_TYPE);
	       %>
		   <%  if (thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) { 
		          thisMinorObjectList.remove(new Integer(deleteIndex).intValue());
			   }  else {
                  thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);
			   }
           %>
 <!-- Regenerate the table -->
   <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">
		  <% 
		  int minorObjCount = 0;
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
		  %>
     <table border="0" width="100%" cellpadding="0">		  
		  <% for (int i =0 ; i <  thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
		  %>
                         <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {%>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
			              </tr>
                    <% } %>
					<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");
									 

								  %>						  
 								<!-- modified  on 22-09-08 for editMinorObjectType.length validation -->
									  <a href="javascript:void(0)" title="<%=editTitle%>"
											 onclick='javascript: 
											 if(editMinorObjectType.length>1 && editMinorObjectType!="<%=minorObjType%>" ){
											 showUnSavedAlert(event,editMinorObjectType);
											 }else{ setMinorObjectAddressType("<%=minorObjType%>","<%=i%>");
											 unsavedEditMinorObjectType="<%=minorObjType%>";											 hideDivs("activeHeaders");showDivs("inactiveHeaders");									
											 tabName="viewEditTab";
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")
											 }'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)" title="<%=deleteTitle%>"
											 onclick='if(editMinorObjectType.length<1){
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")
											 }else{showUnSavedAlert(event,editMinorObjectType);}'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								    <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( sourceAddHandler.getNewSOHashMap().get("hasSensitiveData") != null && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>			   
                      <%} %> <!-- End if thisMinorObjectList.get(MasterControllerService.MINOR_OBJECT_REMOVE) -->
              <% }  %>
     </table>  
</div>

 <!-- End Regenerate -->
<% } else if (!isValidationErrorOccured && isSaveEditedValues) { %>   <!-- this condition has to be before isminorObjectSave  -->

	  <% ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");%>
      <%  thisMinorObject = (HashMap)thisMinorObjectList.get(new Integer(saveEditedValues).intValue());
		  HashMap tempMinorObjectMap = new HashMap();

	  %>
<%    while(parameterNames.hasMoreElements())   { %>
               <% String attributeName = (String) parameterNames.nextElement(); %>
               <% String attributeValue = (String) request.getParameter(attributeName); %>
   		   <%
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
                      for(int k=0;k<fcArray.length;k++) {
                        //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
							 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 
						 if (fcArray[k].getName().equalsIgnoreCase("EUID"))   {continue;}  // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                         
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName)  &&
							      (fcArray[k].getValueType() == 0 || 
							       fcArray[k].getValueType() == 4 || 
							       fcArray[k].getValueType() == 7))   {
							 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------Validations End -------------------------------------

						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							 
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
								 if(fcArray[k].getValueType()==6){		
										valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +ConfigManager.getDateFormat());
								 }else{
									  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());
								 }
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------

			         }
			     if (attributeValue.equalsIgnoreCase("rand")) continue;
			     if (attributeValue.equalsIgnoreCase("MOT")) continue;
			     if (attributeValue.equalsIgnoreCase("listIndex")) continue;
			     if (attributeValue.equalsIgnoreCase("editThisID")) continue;
			     if (attributeValue.equalsIgnoreCase("minorObjSave")) continue;

                  //listIndex=1, minorObjSave=save, editThisID=-1,

			     if (! ("rand".equalsIgnoreCase(attributeName) 					                
					   && !"MOT".equalsIgnoreCase(attributeName)  
 					   && !"listIndex".equalsIgnoreCase(attributeName)  
 					   && !"editThisID".equalsIgnoreCase(attributeName)  
 					   && !"minorObjSave".equalsIgnoreCase(attributeName)  
					  ))  {
					 	if (attributeValue.equalsIgnoreCase(""))   {
							attributeValue = null;
						}
					 //Update the values
                     //thisMinorObject.put(attributeName, attributeValue);  //Add minorObject to the HashMap
					 tempMinorObjectMap.put(attributeName, attributeValue);
                 }  

			%>
 <%  } %>

<!--Validate all the mandatory fields in root node fields-->
<%if (!valiadtions.isEmpty()) {
	Object[] keysValidations = valiadtions.keySet().toArray();
	%>
	<div class="ajaxalert" id="message3">
   	   	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
					       <%for(int i =0 ;i<keysValidations.length;i++) {%>
				             <li>
							   <%=keysValidations[i]%> <%=valiadtions.get((String) keysValidations[i])%>.
				             </li>
							 <%}%>
				      </ul>
				<td>
			<tr>
		</table>
   </div>
<%}%>

	  <% 
	      //This is valid for keyed and unkeyed minor objects - Fix for 254 
		  String keyTypeValueRet = midmUtilityManager.getKeyTypeForMinorObjects(request.getParameter("MOT"),tempMinorObjectMap);
          tempMinorObjectMap.put("keyTypeValue", keyTypeValueRet); 

		  boolean checkKeyTypes = false;
	     //Validate to check the uniqueness of the Address 
		 FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));

 		  //Validate to check the uniqueness of the Child objects while adding the child objects -- Fix for #254
          if (!isValidationErrorOccured) {
           for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			  HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);
			  moHashMap.put("keyTypeValue",midmUtilityManager.getKeyTypeForMinorObjects(request.getParameter("MOT"),moHashMap));
 			  if(new Integer(saveEditedValues).intValue() != mo) {
 			    //Check the key types here
                if(tempMinorObjectMap.get("keyTypeValue") != null && moHashMap.get("keyTypeValue") != null && tempMinorObjectMap.get("keyTypeValue").toString().equalsIgnoreCase(moHashMap.get("keyTypeValue").toString())) { 
				    checkKeyTypes = true;
			    }
 			  }
 		   }
 		  }
          // Fix for 254 ends



         if(checkKeyTypes) {
           String[] keysValueAlready  = thisMinorObject.get("keyTypeValue").toString().split(":");
           String[] keysValueTemp = tempMinorObjectMap.get("keyTypeValue").toString().split(":");
	%>
	 <div class="ajaxalert" id="message4">
	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  <li><%=bundle.getString("cannot_update_text")%>  <b>'<%=keysValueAlready[1]%>' <%=keysValueAlready[0]%></b> <%=bundle.getString("to_text")%> <b>'<%=keysValueTemp[1]%>' <%=keysValueTemp[0]%></b>
							  </li>
							  <li>
                              <b>'<%=keysValueTemp[1]%>' <%=keysValueTemp[0]%></b> <%=bundle.getString("already_found_error_text")%> 
							  </li>
				      </ul>
				<td>
			<tr>
		</table>
	   </div>
	<%} else {
		if(!isValidationErrorOccured) {
		   //copy the content into the minor objects
		  for(int k=0;k<fcArrayLocal.length;k++) {
		   
			  if( fcArrayLocal[k].isSensitive() && sourceAddHandler.getNewSOHashMap().get("hasSensitiveData") != null && !operations.isField_VIP() ) { 
				 
				  continue;
			   }
              thisMinorObject.put(fcArrayLocal[k].getFullFieldName(),tempMinorObjectMap.get(fcArrayLocal[k].getFullFieldName()) );
			  
		   }

		   
		 }
	}%>
      

 <!-- Regenerate the table -->
   <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">
		  <% 
		  int minorObjCount = 0;
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
		  %>
     <table border="0" width="100%" cellpadding="0">		  
		  <% for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
		  %>
                         <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {%>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
			              </tr>
                    <% } %>
				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>

			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");									 

								  %>						  
									<!-- modified  on 22-09-08 for editMinorObjectType.length validation -->
									  <a href="javascript:void(0)" title="<%=editTitle%>"
											 onclick='javascript:
											 if(editMinorObjectType.length>1 && editMinorObjectType!="<%=minorObjType%>" ){
											 showUnSavedAlert(event,editMinorObjectType);
											 }else{ setMinorObjectAddressType("<%=minorObjType%>","<%=i%>");
												unsavedEditMinorObjectType="<%=minorObjType%>";
												hideDivs("activeHeaders");showDivs("inactiveHeaders");
											 tabName="viewEditTab";
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")
											 }'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)"  title="<%=deleteTitle%>"
											 onclick='if(editMinorObjectType.length<1){ 
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")
											 }else{showUnSavedAlert(event,editMinorObjectType);}'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								  <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( sourceAddHandler.getNewSOHashMap().get("hasSensitiveData")  != null && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>			   
                    <% }  %>   <!-- end if mark for delete condition -->
              <% }  %>
     </table>  
</div>

 <!-- End Regenerate -->
<!-- reset the Edit index -->
<%if (!isValidationErrorOccured) {%>
    <script>
      document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+' ' +'<%=request.getParameter("MOT")%>';
	  document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
      document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none'; 
   </script>
   <script> 
    setEditIndex('-1')
   </script>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>InnerForm').reset();	
       enableallfields('<%=request.getParameter("MOT")%>InnerForm');
    </script>
<%}%>

<% } else if (!isSaveEditedValues && isminorObjSave)  { %>
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none';

    </script>
		  <% 
		  int minorObjCount = 0;
		  ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");        
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
		  
		  //Fix for #254 starts
		  //Every minor object is identified by the MINOR_OBJECT_TYPE and KEY TYPE Ex: Address:Home
          //This is valid for keyed and unkeyed minor objects 
		  String keyTypeValueRet = midmUtilityManager.getKeyTypeForMinorObjects(request.getParameter("MOT"),thisMinorObject);
          thisMinorObject.put("keyTypeValue", keyTypeValueRet); 
 		  //Fix for #254 ends
          
		  boolean checkKeyTypes = false;
	      //Validate to check the uniqueness of the Address 
	      FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
		  //Validate to check the uniqueness of the Child objects while adding the child objects -- Fix for #254
          if (!isValidationErrorOccured) {
            for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			  HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);
			  moHashMap.put("keyTypeValue",midmUtilityManager.getKeyTypeForMinorObjects(request.getParameter("MOT"),moHashMap));
			  if(new Integer(saveEditedValues).intValue() != mo) {
 			    //Check the key types here
                if(thisMinorObject.get("keyTypeValue") != null && moHashMap.get("keyTypeValue") != null && thisMinorObject.get("keyTypeValue").toString().equalsIgnoreCase(moHashMap.get("keyTypeValue").toString())) { 
				    checkKeyTypes = true;
			    }
 			  }
		  }
		  //Fix for #254 ends
 
         if(checkKeyTypes) {
			 String[] keysValue  = thisMinorObject.get("keyTypeValue").toString().split(":");
	%>
	<div class="ajaxalert" id="message5">
   	   <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  <li><b>'<%=keysValue[1]%>'</b> <%=keysValue[0]%> <%=bundle.getString("duplicate_minor_object_message_text")%></li>
				      </ul>
				<td>
			<tr>
		</table>
	</div>   
	<%} else {
		 //copy the content into the minor objects
          //add to the array list of ONLY when mandatory fields are addded
		  //sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().add(thisMinorObject);
          thisMinorObjectList.add(thisMinorObject); 
		  
	}
	
   }%>
      


  <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">
		  
     <table border="0" width="100%" cellpadding="0">		  
		  <% for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
		  %>
                         <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {%>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
			              </tr>
                    <% } %>

				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>
			              <tr class="<%=styleClass%>">
			                    <td width="14px" valign="center">
								  <% 
									  String minorObjType = request.getParameter("MOT");
									
								  %>						  

									<!-- modified  on 22-09-08 for editMinorObjectType.length validation -->
									  <a href="javascript:void(0)"  title="<%=editTitle%>"
											 onclick='javascript:
											 if(editMinorObjectType.length>1 && editMinorObjectType!="<%=minorObjType%>" ){
											 showUnSavedAlert(event,editMinorObjectType);
											 }else{ setMinorObjectAddressType("<%=minorObjType%>","<%=i%>");
											 unsavedEditMinorObjectType="<%=minorObjType%>";
											 tabName="viewEditTab";
											 hideDivs("activeHeaders");showDivs("inactiveHeaders");
											 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")
											 }'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td width="14px" valign="center">
										  <a href="javascript:void(0)" title="<%=deleteTitle%>"
												 onclick='if(editMinorObjectType.length<1){
												 ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")
												 }else{showUnSavedAlert(event,editMinorObjectType);}'> 
													 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
										  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								   <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( sourceAddHandler.getNewSOHashMap().get("hasSensitiveData")  != null &&  !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>			   
                    <% } %>
              <% }  %>
     </table>  
</div>

<%if (!isValidationErrorOccured) {%>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>InnerForm').reset();		  
   </script>
<%}%>


<% } else if (isEdit) { %>  <!-- Edit Minor Object -->
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("edit_euid")%> '+ ' '+'<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'visible';
    document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'block';

    </script>
		     <%
		      // HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
               ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");
		       FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
			   String formName = request.getParameter("MOT")+"InnerForm";
			   int intEditIndex = new Integer(editIndex).intValue();
			 %>
			 <%HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(intEditIndex);%>
               <!-- Generate the script to populate the user code maskings -->
			       <% for(int k=0;k<fcArray.length;k++) {	
				        String constarintBy = fcArray[k].getConstraintBy();
						if(constarintBy != null && constarintBy.length() > 0) {
				        int refIndex = sourceHandler.getReferenceFields(fcArray,constarintBy);
                       
                        String userInputMask = ValidationService.getInstance().getUserCodeInputMask(fcArray[refIndex].getUserCode(), (String)   minorObjectMap.get(fcArray[refIndex].getFullFieldName()));
                       
			        
				     %>
						<script>
                         userDefinedInputMask = '<%=userInputMask%>';
				         
						</script>
				      <%}%> 
				   <%}%> 
               <!-- Generate the script to populate the form -->
			   <script>
			       <%
				     String thisminorObjectType = (String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE);
 				     for(int k=0;k<fcArray.length;k++) {					     
				   %>
                  
				   <%
						String value = (minorObjectMap.get(fcArray[k].getFullFieldName())) != null ?minorObjectMap.get(fcArray[k].getFullFieldName()).toString():null;   
                        if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
                          if (value != null) {
                              //Mask the value as per the masking 
                              value = fcArray[k].mask(value.toString());
                          }
                        } 
					%> 

  					    var thisFrm = document.getElementById('<%=formName%>');
                        elemType = thisFrm.elements[<%=k%>].type.toUpperCase()
					<%if( fcArray[k].isSensitive() && sourceAddHandler.getNewSOHashMap().get("hasSensitiveData")  != null && !operations.isField_VIP()){%>
					
					   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>
						<%	if(fcArray[k].isRequired()) {
				       %>

						<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
				       %>
 						  
                            thisFrm.elements[<%=k%>].readOnly = true;
                            thisFrm.elements[<%=k%>].disabled = true;
							thisFrm.elements[<%=k%>].options.selectedIndex = 0;
   
						<%} else {%>
							if(elemType != 'HIDDEN') {
                               thisFrm.elements[<%=k%>].readOnly = true;
                               thisFrm.elements[<%=k%>].disabled = true;
 							   thisFrm.elements[<%=k%>].value = "<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>";
						    }
						<%}%>

					    <%} else {%>
						<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
				       %>
 						  
                            thisFrm.elements[<%=k%>].readOnly = true;
                            thisFrm.elements[<%=k%>].disabled = true;
							thisFrm.elements[<%=k%>].options.selectedIndex = 0;
                            thisFrm.elements[<%=k%>].title = '';
  
						<%} else {%>
							if(elemType != 'HIDDEN') {
                               thisFrm.elements[<%=k%>].readOnly = true;
                               thisFrm.elements[<%=k%>].disabled = true;
 							   thisFrm.elements[<%=k%>].value = "<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>";
                               thisFrm.elements[<%=k%>].title = '';
						    }
						<%}%>
						<%}%>

					   <%}%>
					<%} else {%>
						<%	if(!thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW) && fcArray[k].isKeyType()) {
				       %>
						   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>
							<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
						   %>
							  
								thisFrm.elements[<%=k%>].readOnly = true;
								thisFrm.elements[<%=k%>].disabled = true;

								for (var i=0; i< thisFrm.elements[<%=k%>].options.length; i++)  {
									if ( (thisFrm.elements[<%=k%>].options[i].value) ==  "<%=value%>")   {
										thisFrm.elements[<%=k%>].options.selectedIndex = i
									}
								 }
 		
							<%} else {%>
								if(elemType != 'HIDDEN') {
								   thisFrm.elements[<%=k%>].readOnly = true;
								   thisFrm.elements[<%=k%>].disabled = true;
								   thisFrm.elements[<%=k%>].value = "<%=value%>"
								}
							<%}%>
							<%}%>
					   <%} else {%>
						   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>
							<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
						   %>
							   if(elemType != 'HIDDEN') {
							  
								for (var i=0; i< thisFrm.elements[<%=k%>].options.length; i++)  {
									if ( (thisFrm.elements[<%=k%>].options[i].value) ==  "<%=value%>")   {
										thisFrm.elements[<%=k%>].options.selectedIndex = i
									}
								 }
							   }

							<%} else {%>
								if(elemType != 'HIDDEN') {
								  thisFrm.elements[<%=k%>].value = "<%=value%>"
								}
							<%}%>
						<%}%>
				   <%}%>

					<%}%>
						
		           <%}%>
			        <%for(int k=0;k<fcArray.length;k++) {			//Fix for #257 starts%>
					   <%if(!fcArray[k].isUpdateable()) {%>
					  <%if(!thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) {%>
                         thisFrm.elements[<%=k%>].readOnly = true;
				       <%}%>
				     <%}%>
				   <%} //Fix for #257 ends here
					%>
                    
                   <%
				int countReadonlyFields = 0;
				for(int k=0;k<fcArray.length;k++) {			//Fix for #231 starts
				    System.out.println("fcArray[k].isUpdateable()---> " + fcArray[k].isUpdateable());
                    if(!thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW) && !fcArray[k].isUpdateable()) {
						countReadonlyFields++;
					}
                }%>

					<%if(countReadonlyFields == fcArray.length ){%>
						editIndex = "-1";
						editIndexid = "-1";
						editMinorObjectType = "";
				   <%}%>

			   </script>
	<!-- Validate System Object and LID -->
<% } else if (isValidate) {  %> 
   <%
  	 String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
	 if( (validateLID != null && validateLID.trim().length() == 0 )
		 && (validateSystemCode != null && validateSystemCode.trim().length() == 0 )) {
	%>
		<div class="ajaxalert" id="message6">
	   	  <table>
			<tr>
				<td>
				      <ul>
				             <li>
							  <%=bundle.getString("enter_lid_and_systemcode_text")%> <%=localIdDesignation%>
				             </li>
				      </ul>
				<td>
			<tr>
		</table>
		</div>
	<%} else {%> <!--If systemcode/lid is not entered condition ends here-->
    <%
	String tempValidateLid = validateLID;
    String sysDesc  = masterControllerService.getSystemDescription(validateSystemCode);
	validateLID = validateLID.replaceAll("-","");
    sourceAddHandler.setLID(validateLID);
    sourceAddHandler.setSystemCode(validateSystemCode);
    String  validated =  sourceAddHandler.validateSystemCodeLID(validateLID,validateSystemCode); 
	Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
  
    
   if(validated != null)  {	%>

     <%if("true".equalsIgnoreCase(validated))  {	%>
      <script>
	       document.getElementById('saveButtons').style.visibility = 'visible';
     	   document.getElementById('saveButtons').style.display = 'block';

	       document.getElementById('addFormFields').style.visibility = 'visible';
    	   document.getElementById('addFormFields').style.display = 'block';

	       document.getElementById('validateButtons').style.visibility = 'hidden';
	       document.getElementById('validateButtons').style.display = 'none';
      </script>
    <%} else {%>
        
				<div class="ajaxalert" id="message7">
						  <table>
							<tr>
								<td>
									  <ul>
											 <li>
											  <%=sysDesc%>/<%=tempValidateLid%> <%=bundle.getString("already_found_error_text")%>
											 </li>
									  </ul>
								<td>
							<tr>
						</table>
					</div>
				<%}%>


			   <%} else {%>			   
 					   <div class="ajaxalert" id="message8">
				 
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
         <%}%>
  <%}%>


 </body>
</f:view>
<%} %>  <!-- Session check -->
</html>
