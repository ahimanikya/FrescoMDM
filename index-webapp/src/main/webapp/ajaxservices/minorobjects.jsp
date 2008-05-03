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

thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
thisMinorObject.put(MasterControllerService.MINOR_OBJECT_TYPE, request.getParameter("MOT"));
thisMinorObject.put(MasterControllerService.SYSTEM_CODE, sourceAddHandler.getSystemCode());
thisMinorObject.put(MasterControllerService.LID, sourceAddHandler.getLID());

String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//replace ajaxservices folder name 
URI = URI.replaceAll("/ajaxservices","");

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
HashMap rootNodesHashMap = new HashMap();

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
     HashMap valiadtions = new HashMap();
     ArrayList requiredValuesArray = new ArrayList();
     ArrayList numericValuesArray = new ArrayList();
	 boolean isNumberValidation = false;

    while(parameterNames.hasMoreElements() && !isEdit && !isValidate && !isSaveEditedValues)   { 
    
	//while(parameterNames.hasMoreElements() && ( (isminorObjSave || isSave) || (!isEdit && !isValidate && !isSaveEditedValues) ) )   { 
		%>
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
						  //check for numberic fields here
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
                      if (valiadtions.isEmpty() && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("SYS".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
                           thisMinorObject.put(attributeName, attributeValue);  //Add minorObject to the HashMap
					  }
                 }  else if (isSave) {   //Final Save hence add Root fields to the Hashmap
				      /*if (!("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) )  {
					  */
					  //validate all the mandatory fields before adding to the hashmap
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
                      for(int k=0;k<fcArray.length;k++) {
 			                if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) &&           attributeValue.equalsIgnoreCase("")) {  
                                 isValidationErrorOccured = true;
			                     //build array of required values here
                                 requiredValuesArray.add(fcArray[k].getDisplayName());
			                     valiadtions.put(fcArray[k].getDisplayName()," is Required");
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

				      if (!isValidationErrorOccured  && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("SYS".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
					      if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                                   rootNodesHashMap.put(attributeName, attributeValue); 
					  }
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

<%  if (!isValidationErrorOccured && isSave) { %>  <!-- Save System Object and ignore all other conditions  -->

	 
	 <% 
		// removefield masking here
		if(rootNodesHashMap.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(rootNodesHashMap, rootNodeName);
		ArrayList newFinalMinorArrayList  = new ArrayList();

		for(int i = 0; i< sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size() ;i ++) {
			HashMap oldHashMap  = (HashMap) sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(i);
            HashMap newHashMap  = new HashMap();
			Object[] keysSet  = oldHashMap.keySet().toArray();
			for(int j = 0 ;j <keysSet.length;j++) {
			   String key = (String) keysSet[j];
			   if( !"listIndex".equalsIgnoreCase(key) &&
				   !"MOT".equalsIgnoreCase(key) &&
				   !"editThisID".equalsIgnoreCase(key) &&
				   !"minorObjSave".equalsIgnoreCase(key)
				   ) {
                   newHashMap.put(key,oldHashMap.get(key));
			   }
			}
			newFinalMinorArrayList.add(newHashMap);

	    }
		if(newFinalMinorArrayList.size() > 0 ) sourceAddHandler.setNewSOMinorObjectsHashMapArrayList(newFinalMinorArrayList);

		sourceAddHandler.setNewSOHashMap(rootNodesHashMap);
		String isSuccess = sourceAddHandler.addNewSO();
        Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
     %> 
	 <%	if ("success".equalsIgnoreCase(isSuccess))  { %>
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

               var formNameValue = document.forms['basicValidateAddformData'];
               var lidField =  getDateFieldName(formNameValue.name,'LID');
               var systemField =  getDateFieldName(formNameValue.name,'SystemCode');

			   document.getElementById(lidField).readOnly = false;
               document.getElementById(lidField).disabled = false;
               document.getElementById(lidField).style.backgroundColor = '';

				document.getElementById(systemField).readOnly = false;
                document.getElementById(systemField).disabled = false;
                document.getElementById(systemField).style.backgroundColor  = '';
		  </script>
         
		  <script> 
            setEditIndex('-1')
          </script>
         
		  <% Object[] keysSetMinorObjects  = allNodeFieldConfigsMap.keySet().toArray();
            for(int j = 0 ;j <keysSetMinorObjects.length;j++) {
			   String key = (String) keysSetMinorObjects[j]; 
		   %>
		  <script>
			   document.getElementById('<%=key%>AddNewSOInnerForm').reset();
		  </script>
          <%if(!rootNodeName.equalsIgnoreCase(key)) {%>
              <script>
               document.getElementById('<%=key%>AddNewSODiv').innerHTML = "";
			   document.getElementById('extra<%=key%>AddNewDiv').style.display = 'none';
			   document.getElementById('extra<%=key%>AddNewDiv').style.visibility = 'hidden';

               document.getElementById('<%=key%>buttonspan').innerHTML = 'Save '+ '<%=key%>';
               document.getElementById('<%=key%>cancelSOEdit').style.visibility = 'hidden';
               document.getElementById('<%=key%>cancelSOEdit').style.display = 'none';
              </script>
		 <%}%>

		  <%
			}
          %>

     <%	   //reset all the fields here for root node and minor objects
		    sourceAddHandler.getNewSOHashMap().clear();
            sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().clear();

			  } %>
	<div class="ajaxsuccess">
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
<% } else if (isDelete) { %>   <!-- Delete Minor Object  -->
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.display = 'none';

    </script>
		   <%  thisMinorObject = (HashMap)sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().remove(new Integer(deleteIndex).intValue());%>

 <!-- Regenerate the table -->
		  <% 
		  int minorObjCount = 0;
		  ArrayList thisMinorObjectList = new ArrayList();
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
  	      //sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().add(thisMinorObject); 

		  for (int i =0 ; i <sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size();i++)  { 
			    HashMap minorObjectMap  = (HashMap) sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(i); 
				String str = (String) minorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE);
				if (str.equalsIgnoreCase(request.getParameter("MOT"))) {
                    minorObjectMap.put("listIndex",i);
					thisMinorObjectList.add(minorObjectMap);

				}
				%>
          <%}%>
		  <% for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
		  %>
                         <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                  <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">	<table border="0" width="100%" cellpadding="0">		  
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
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String thisIndex = ((Integer)minorObjectMap.get("listIndex")).toString();
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=thisIndex%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&editIndex=<%=thisIndex%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&deleteIndex=<%=thisIndex%>&MOT=<%=minorObjType%>","<%=minorObjType%>AddNewSODiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								   <td>
										 <%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":(fcArray[k].getValueList() != null)?ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName())):minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>	
<% if ( i == thisMinorObjectList.size()-1)  { %>
     </table>  
</div>
<% } %>
						   
              <% }  %>

 <!-- End Regenerate -->
<% } else if (!isValidationErrorOccured && isSaveEditedValues) { %>   <!-- this condition has to be before isminorObjectSave  -->
	   <%  thisMinorObject = (HashMap)sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(new Integer(saveEditedValues).intValue());
		   HashMap tempMinorObjectMap = new HashMap();
    while(parameterNames.hasMoreElements())   { 
		String attributeName = (String) parameterNames.nextElement(); 
		String attributeValue = (String) request.getParameter(attributeName); 
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
			     /*if (attributeValue.equalsIgnoreCase("")) continue;
			     if (attributeValue.equalsIgnoreCase("rand")) continue;
			     if (attributeValue.equalsIgnoreCase("MOT")) continue;
			     if (attributeValue.equalsIgnoreCase("listIndex")) continue;
			     if (attributeValue.equalsIgnoreCase("editThisID")) continue;
			     if (attributeValue.equalsIgnoreCase("minorObjSave")) continue;
                  */
                  //listIndex=1, minorObjSave=save, editThisID=-1,
				      if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                      if (!isValidationErrorOccured&& 
						  !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("SYS".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {

			     /*if (! ("rand".equalsIgnoreCase(attributeName) 					                
					   && !"MOT".equalsIgnoreCase(attributeName)  
 					   && !"listIndex".equalsIgnoreCase(attributeName)  
 					   && !"editThisID".equalsIgnoreCase(attributeName)  
 					   && !"minorObjSave".equalsIgnoreCase(attributeName)  
					  ))  {
					 */
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
         for(int mo = 0; mo < sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size();mo++) {
			 HashMap moHashMap = (HashMap)sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(mo);
			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) tempMinorObjectMap.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
				   keyTypeValues = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
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
   <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
		  <% 
		  int minorObjCount = 0;
		  ArrayList thisMinorObjectList = new ArrayList();
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
  	      //sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().add(thisMinorObject); 

		  for (int i =0 ; i <sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size();i++)  { 
			    HashMap minorObjectMap  = (HashMap) sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(i); 
				String str = (String) minorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE);
				if (str.equalsIgnoreCase(request.getParameter("MOT"))) {
                    minorObjectMap.put("listIndex",i);
					thisMinorObjectList.add(minorObjectMap);

				}
          }
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
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String thisIndex = ((Integer)minorObjectMap.get("listIndex")).toString();
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=thisIndex%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&editIndex=<%=thisIndex%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&deleteIndex=<%=thisIndex%>&MOT=<%=minorObjType%>","<%=minorObjType%>AddNewSODiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								   <td>
										 <%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":(fcArray[k].getValueList() != null)?ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName())):minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>			   
              <% }  %>
     </table>  
</div>

 <!-- End Regenerate -->
<!-- reset the Edit index -->
<!-- reset the Edit index -->
<script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.display = 'none';
</script>

<script> 
  setEditIndex('-1')
</script>
<%if (!isValidationErrorOccured) {%>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>AddNewSOInnerForm').reset();		  
   </script>
<%}%>

<% } else if (!isValidationErrorOccured && !isSaveEditedValues && isminorObjSave)  { %>
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Save '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.visibility = 'hidden';
    document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.display = 'none';

    </script>
		  <% 
		  int minorObjCount = 0;
		  ArrayList thisMinorObjectList = new ArrayList();
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
         for(int mo = 0; mo < sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size();mo++) {
			 HashMap moHashMap = (HashMap)sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(mo);
			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) thisMinorObject.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
				   keyTypeValues = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
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
		  //add to the array list  ONLY when mandatory fields are addded
          sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().add(thisMinorObject); 
	  
	}
	
   }%>
      
		  <%


	
		  for (int i =0 ; i <sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().size();i++)  { 
			    HashMap minorObjectMap  = (HashMap) sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(i); 
				String str = (String) minorObjectMap.get(MasterControllerService.MINOR_OBJECT_TYPE);
				if (str.equalsIgnoreCase(request.getParameter("MOT"))) {
                    minorObjectMap.put("listIndex",i);
					thisMinorObjectList.add(minorObjectMap);

				}
          }
		  %>
  <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
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
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String thisIndex = ((Integer)minorObjectMap.get("listIndex")).toString();
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" 
											 onclick='javascript:setEditIndex(<%=thisIndex%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&editIndex=<%=thisIndex%>&&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">
									  <a href="javascript:void(0)" 
											 onclick='ajaxMinorObjects("/<%=URI%>/ajaxservices/minorobjects.jsf?&deleteIndex=<%=thisIndex%>&MOT=<%=minorObjType%>","<%=minorObjType%>AddNewSODiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
							   </td>

							  <% for(int k=0;k<fcArray.length;k++) {%>
								   <td>
										 <%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":(fcArray[k].getValueList() != null)?ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName())):minorObjectMap.get(fcArray[k].getFullFieldName()))%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
						   </tr>			   
              <% }  %>

     </table>  
</div>

<%if (!isValidationErrorOccured && !checkKeyTypes) {%>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>AddNewSOInnerForm').reset();		  
   </script>
<%}%>


<% } else if (isEdit) { %>  <!-- Edit Minor Object -->
    <script>
    document.getElementById('<%=request.getParameter("MOT")%>buttonspan').innerHTML = 'Update '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.visibility = 'visible';
    document.getElementById('<%=request.getParameter("MOT")%>cancelSOEdit').style.display = 'block';
    </script>
		     <%
		      // HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
		       FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
			   String formName = request.getParameter("MOT")+"AddNewSOInnerForm";
			   int intEditIndex = new Integer(editIndex).intValue();
			 %>
			 <%HashMap minorObjectMap  = (HashMap) sourceAddHandler.getNewSOMinorObjectsHashMapArrayList().get(intEditIndex);%>
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
		 && (validateSystemCode != null && validateSystemCode.trim().length() > 0 )) {
	%>
	<div class="ajaxalert">
	   	  <table>
			<tr>
				<td>
				      <ul>
				             <li>
							  Please enter LID value.
				             </li>
				      </ul>
				<td>
			<tr>
		</table>
		</div>
   <%
	 } else if( (validateLID != null && validateLID.trim().length() == 0 )
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
        	boolean isMaskingCorrect = sourceHandler.checkMasking(validateLID,request.getParameter("lidmask"));
         	if (!isMaskingCorrect)   { %>
            <div class="ajaxalert">
        	  <table>
	    		<tr>
		    		<td>
			    	      <ul>
				                <li>
					    		  LID is not in the format <%=request.getParameter("lidmask")%>.
				               </li>
				         </ul>
				    <td>
			    <tr>
	     	</table>
			</div>
           <% } else { 
				String tempValidateLid = validateLID;
				String sysDesc  = masterControllerService.getSystemDescription(validateSystemCode);
				validateLID = validateLID.replaceAll("-","");
				sourceAddHandler.setLID(validateLID);
				sourceAddHandler.setSystemCode(validateSystemCode);
				boolean validated =  sourceAddHandler.validateSystemCodeLID(validateLID,validateSystemCode); 
				 if(validated)  {	%>
				  <script>
                       var formNameValue = document.forms['basicValidateAddformData'];
                       var lidField =  getDateFieldName(formNameValue.name,'LID');
                       var systemField =  getDateFieldName(formNameValue.name,'SystemCode');

					   document.getElementById(lidField).readOnly = true;
                       document.getElementById(lidField).disabled = true;
                       document.getElementById(lidField).style.backgroundColor = '#efefef';

					   document.getElementById(systemField).readOnly = true;
                       document.getElementById(systemField).disabled = true;
                       document.getElementById(systemField).style.backgroundColor  = '#efefef';


					   document.getElementById('saveButtons').style.visibility = 'visible';
					   document.getElementById('saveButtons').style.display = 'block';

					   document.getElementById('addFormFields').style.visibility = 'visible';
					   document.getElementById('addFormFields').style.display = 'block';

					   document.getElementById('validateButtons').style.visibility = 'hidden';
					   document.getElementById('validateButtons').style.display = 'none';
				  </script>
			   <%} else {%>
			   `    <!--script>
				    //alert("System Code and LID " + "'<%=validateSystemCode%>'" + "/" + "'<%=validateLID%>' "+ " is already found !");
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
  <%}%>
<% } %>


 </body>
</f:view>
</html>
