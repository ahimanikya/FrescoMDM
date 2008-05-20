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


 
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler"%>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
//This page is an Ajax Service, never to be used directly from the Faces-confg.
//This will render a datatable of minor object to be rendered on the calling JSP.
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



HttpSession session1 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
ArrayList minorObjectsAddList = (ArrayList)session1.getAttribute("minorObjectsAddList");

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
    						 valiadtions.put(fcArray[k].getDisplayName(),": is Required");
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
                                  valiadtions.put(fcArray[k].getDisplayName(),": is not a Number");
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------

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
    						 valiadtions.put(fcArray[k].getDisplayName(),": is Required");
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
                                  valiadtions.put(fcArray[k].getDisplayName(),": is not a Number");
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------			
                     }
					  

				      if (!isValidationErrorOccured && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
						  if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                          rootNodesHashMap.put(attributeName, attributeValue); 
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
%>
	<div class="ajaxalert">
   	   	  <table>
			<tr>
				<td>  
				   Validation  Error:
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
	 <%	if ("UPDATE_SUCCESS".equalsIgnoreCase(isSuccess))  { %>
			   <!-- // close the Minor objects 
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
		  <script>
			   document.getElementById('<%=key%>InnerForm').reset();
		  </script>
          <%if(!key.equalsIgnoreCase(rootNodeName)) {%>
		    <script>
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

          <%
			//reset all the fields here for root node and minor objects 
		    //sourceAddHandler.getNewSOHashMap().clear();
		   } %>
		 <div class="ajaxsuccess">
	      <table>
	     	 <tr>
				<td>
				      <ul>
					    <li> Source record details have been successfully updated.</li>
					  </ul>
			    </td>
		     </tr>
	      </table>
         </div>

	  <table>
			<tr>
				<td>
				      <ul>
			            <% while (messagesIter.hasNext())   { %>
				             <li>
								<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
								<%= facesMessage.getDetail() %>
								<%= facesMessage.getSummary() %>
				             </li>
						 <% } %>
				      </ul>
				<td>
			<tr>
		</table>
<% } else if (isLoad) {%>
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

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td width="14px" valign="center">							   
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								    <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                           <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
 										      <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
										         <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										      <%}else{%>
                                                <%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										     <%}%>
										   <%} else {%> <!-- In other cases-->
                                             <%=minorObjectMap.get(fcArray[k].getFullFieldName())%>
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
						   <td>  No <%= request.getParameter("MOT") %> exists
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

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								    <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                           <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
 										      <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
										         <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										      <%}else{%>
                                                <%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										     <%}%>
										   <%} else {%> <!-- In other cases-->
                                             <%=minorObjectMap.get(fcArray[k].getFullFieldName())%>
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
							 valiadtions.put(fcArray[k].getDisplayName(),": is Required");
						  }							 
						 if (fcArray[k].getName().equalsIgnoreCase("EUID"))   {continue;}  // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                         
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName)  &&
							      (fcArray[k].getValueType() == 0 || 
							       fcArray[k].getValueType() == 4 || 
							       fcArray[k].getValueType() == 7))   {
							 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": is not a Number");
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------Validations End -------------------------------------
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
	<div class="ajaxalert">
   	   	  <table>
			<tr>
				<td>  
				   Validation  Error:
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
		  boolean checkKeyTypes = false;
		  String keyTypeValues = new String();
		  String keyType = new String();
	     //Validate to check the uniqueness of the Address 
		 FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
         for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			 HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);
 
			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) tempMinorObjectMap.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
                     //CHECK FOR THE KEY TYPE VALUES WITH USER CODES AND VALUE LIST
				     if (fcArrayLocal[k].getValueList() != null){  
				       if (fcArrayLocal[k].getUserCode() != null) {  
						 keyTypeValues = ValidationService.getInstance().getUserCodeDescription(fcArrayLocal[k].getUserCode(),originalValue);
					   } else{
                          keyTypeValues  = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
					  }
					}
					keyType = fcArrayLocal[k].getDisplayName();
				  }
			    }
	           } 
			 }
		 }

         if(checkKeyTypes) {
	%>
	 <div class="ajaxalert">
	  <table>
			<tr>
				<td>  
				   Validation  Error:
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  Cannot add <%=keyType%> <b>'<%=keyTypeValues%>'</b> again.
				      </ul>
				<td>
			<tr>
		</table>
	   </div>
	<%} else {
		if(!isValidationErrorOccured) {
		   //copy the content into the minor objects
	       for(int k=0;k<fcArrayLocal.length;k++) {
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

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								  <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                           <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
 										      <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
										         <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										      <%}else{%>
                                                <%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										     <%}%>
										   <%} else {%> <!-- In other cases-->
                                             <%=minorObjectMap.get(fcArray[k].getFullFieldName())%>
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
      document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	  document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
      document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none'; 
   </script>
   <script> 
    setEditIndex('-1')
   </script>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>InnerForm').reset();		  
   </script>
<%}%>

<% } else if (!isSaveEditedValues && isminorObjSave)  { %>
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none';

    </script>
		  <% 
		  int minorObjCount = 0;
		  ArrayList thisMinorObjectList = (ArrayList) sourceAddHandler.getNewSOHashMap().get("SOEDIT"+request.getParameter("MOT")+"ArrayList");        
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
%>

 <% 
     boolean checkKeyTypes = false;
	 String keyTypeValues = new String();
	 String keyType = new String();
	  //Validate to check the uniqueness of the Address 
	 FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
     if (!isValidationErrorOccured) {
         for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			 HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);
			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) thisMinorObject.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
                     //CHECK FOR THE KEY TYPE VALUES WITH USER CODES AND VALUE LIST
				     if (fcArrayLocal[k].getValueList() != null){  
				       if (fcArrayLocal[k].getUserCode() != null) {  
						 keyTypeValues = ValidationService.getInstance().getUserCodeDescription(fcArrayLocal[k].getUserCode(),originalValue);
					   } else{
                          keyTypeValues  = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
					  }
					}
					
					keyType = fcArrayLocal[k].getDisplayName();
				  }
			    }
	           } 
			 }
		 }
       
         if(checkKeyTypes) {
	%>
	<div class="ajaxalert">
   	   <table>
			<tr>
				<td>  
				   Validation  Error:
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  <li>Cannot add <%=keyType%> <b>'<%=keyTypeValues%>'</b> again.</li>
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

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td width="14px" valign="center">
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/editminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>NewDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								   <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                           <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
 										      <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
										         <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										      <%}else{%>
                                                <%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										     <%}%>
										   <%} else {%> <!-- In other cases-->
                                             <%=minorObjectMap.get(fcArray[k].getFullFieldName())%>
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
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Update '+ '<%=request.getParameter("MOT")%>';
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
			       <% for(int k=0;k<fcArray.length;k++) {					     
				   %>
  					    var thisFrm = document.getElementById('<%=formName%>');
                        elemType = thisFrm.elements[<%=k%>].type.toUpperCase()
					   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {
							if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
				       %>
                           if(elemType != 'HIDDEN') {
						  
							for (var i=0; i< thisFrm.elements[<%=k%>].options.length; i++)  {
								if ( (thisFrm.elements[<%=k%>].options[i].value) ==  '<%=minorObjectMap.get(fcArray[k].getFullFieldName())%>')   {
									thisFrm.elements[<%=k%>].options.selectedIndex = i
								}
						     }
					       }

						<%} else {%>
							if(elemType != 'HIDDEN') {
                              thisFrm.elements[<%=k%>].value = '<%=minorObjectMap.get(fcArray[k].getFullFieldName())%>'
						    }
						<%}%>
					<%}%>
						
		           <%}%>
			       
			   </script>
	<!-- Validate System Object and LID -->
<% } else if (isValidate) {  %> 
   <%
	 if( (validateLID != null && validateLID.trim().length() == 0 )
		 && (validateSystemCode != null && validateSystemCode.trim().length() == 0 )) {
	%>
		<div class="ajaxalert">
	   	  <table>
			<tr>
				<td>
				      <ul>
				             <li>
							  Please enter System and LID values.
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
    boolean validated =  sourceAddHandler.validateSystemCodeLID(validateLID,validateSystemCode); 
    %>
    <% if(validated)  {	%>
      <script>
	       document.getElementById('saveButtons').style.visibility = 'visible';
     	   document.getElementById('saveButtons').style.display = 'block';

	       document.getElementById('addFormFields').style.visibility = 'visible';
    	   document.getElementById('addFormFields').style.display = 'block';

	       document.getElementById('validateButtons').style.visibility = 'hidden';
	       document.getElementById('validateButtons').style.display = 'none';
      </script>
    <%} else {%>
       <!--script>
       </script-->
	   	<div class="ajaxalert">
	   	  <table>
			<tr>
				<td>
				      <ul>
				             <li>
							   <%=sysDesc%>/<%=tempValidateLid%> is already found.
				             </li>
				      </ul>
				<td>
			<tr>
		</table>
		</div>

     <%}%>
  <%}%>
<% } %>

 </body>
</f:view>
<%} %>  <!-- Session check -->
</html>
