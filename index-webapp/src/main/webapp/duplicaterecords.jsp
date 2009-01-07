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
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
 
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>


<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.math.BigDecimal"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.sql.Timestamp"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager" %>

<%@ page import="javax.faces.model.SelectItem" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>

<f:view>
    
   
    <html>
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="./scripts/yui/yahoo/yahoo-min.js" ></script>

        
        <!-- Additional source files go here -->
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
                
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <script type="text/javascript" src="scripts/balloontip.js"></script>
        <script type="text/javascript" src="scripts/validation.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo/yahoo.js"></script>
        <script type="text/javascript" src="./scripts/yui/event/event.js"></script>
        <script type="text/javascript" src="./scripts/yui/dom/dom.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/animation/animation.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
		<script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script> 
<script>
//not used in transactions, but since its require in the script we fake it
var editIndexid = ""; 
var thisForm;
var rand = "";
var reloadUrl ="";
var popUrl = "";
// Fields used for merge 
var rowCountMerge  ="";
var destinationEOFinalMerge ="";
var previewhiddenMergeEuidsFinalMerge ="";

function setRand(thisrand)  {
	rand = thisrand;
}

var checkedItems = new Array();
function getCheckedValues(a,v)   {
   if (a.checked == true)  {
      checkedItems.push(v);
   } else {
     for(i=0;i<checkedItems.length;i++){
       if(v == checkedItems[i]) checkedItems.splice(i, 1);
      } 
  }
}

function align(thisevent,divID) {
	divID.style.visibility= 'visible';
	divID.style.top = thisevent.clientY-180;
	divID.style.left= thisevent.clientX;
}

		   //  merge related global javascript variables
		   var euids="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];
		   var previewEuidDivs=[];
</script>

		</head>
        <body>
            <%@include file="./templates/header.jsp"%>

<% 
   FacesContext facesContext = FacesContext.getCurrentInstance(); 
   
   HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   SearchDuplicatesHandler  searchDuplicatesHandler = (facesSession.getAttribute("SearchDuplicatesHandler") != null ) ? (SearchDuplicatesHandler) facesSession.getAttribute("SearchDuplicatesHandler") : new SearchDuplicatesHandler();
   SourceHandler sourceHandler= new SourceHandler();

   String from = (String)facesContext.getExternalContext().getRequestParameterMap().get("where");
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Enumeration parameterNames = request.getParameterNames();
   Operations operations=new Operations();

%>

<div id="mainContent" style="overflow:hidden">   
<table width="100%"><tr><td>
        <div id="advancedSearch" class="duplicaterecords" style="visibility:visible;display:block">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{SearchDuplicatesHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                               <select id="searchTypeList" title="<%=bundle.getString("search_Type")%>" onchange="javascript:
										       document.getElementById('messages').innerHTML='';
											   document.getElementById('outputdiv').innerHTML ='';getRecordDetailsFormValues('searchTypeForm');checkedItems = new Array();setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchscreenservices.jsf?random='+rand+'&'+queryStr,'SearchCriteria','')" style="width:220px;">	
                                              <%ArrayList   searchListItemArray = searchDuplicatesHandler.getPossilbeSearchTypes();%>
											    <%for(int p = 0; p <searchListItemArray.size();p++) {
                                                        SelectItem selectItem = (SelectItem) searchListItemArray.get(p);
													%>
                                                    <%if(request.getParameter("selectedSearchType") != null && request.getParameter("selectedSearchType").equalsIgnoreCase(selectItem.getLabel())) {%>
											     	  <option value="<%=selectItem.getValue()%>" selected="true"><%=selectItem.getLabel()%></option>
													<%} else {%>
											     	<option value="<%=selectItem.getValue()%>"><%=selectItem.getLabel()%></option>
													<%}%>
												<%}%>
											</select>
                                </td>
                            </tr>
                </form>
					 <tr><td colspan="2">&nbsp;</td></tr>
            </table>

   <h:form id="advancedformData" >
                            <input id='lidmask' type='hidden' title='lidmask' name='lidmask' value='' />
                <input type="hidden" id="selectedSearchType" title='selectedSearchType' 
				value='<%=searchDuplicatesHandler.getSelectedSearchType()%>' />

             <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2">
                           <div id="SearchCriteria">
							<table width="100%" cellpadding="0" cellspacing="0">
					 <tr><td colspan="2">&nbsp;</td></tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
							<%if(searchDuplicatesHandler.getInstructionLine() != null ) {%>
						   <tr>
							 <td colspan="2" align="left"><p><nobr><%=searchDuplicatesHandler.getInstructionLine()%></nobr></p>
							 </td>
						   </tr>
						   <%}%>
							<%
							  for(int i = 0 ; i < searchDuplicatesHandler.getSearchScreenFieldGroupArray().size(); i++) {
                                 FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) searchDuplicatesHandler.getSearchScreenFieldGroupArray().get(i);

							%>
							   <tr><td>&nbsp;</td></tr>
							   <%if( basicSearchFieldGroup.getDescription() != null ) { %>
							   <tr>
							     <td colspan="2">
							      <font style="color:blue"><%=basicSearchFieldGroup.getDescription()%></font>
							     </td>
							   </tr>
							   <%}%>
                               <%
								 ArrayList fieldGroupArrayList  = (ArrayList)searchDuplicatesHandler.getSearchScreenHashMap().get(basicSearchFieldGroup.getDescription());
								%>
							   <%for(int j = 0 ; j < fieldGroupArrayList.size() ; j++) {
								  ArrayList fieldConfigArrayList = (ArrayList) fieldGroupArrayList.get(j);
								  ValueExpression fieldConfigArrayListVar = ExpressionFactory.newInstance().createValueExpression( fieldConfigArrayList,  fieldConfigArrayList.getClass()); 	

								%>
								<tr>
								<%for(int k = 0 ; k < fieldConfigArrayList.size() ; k++) {
								  FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayList.get(k);
									   String title =  fieldConfig.getName();
									   int maxlength = (fieldConfig.getName().equalsIgnoreCase("EUID")) ? sourceHandler.getEuidLength(): fieldConfig.getMaxSize();
								%>
							     <td>
									<nobr>											
										<%if(fieldConfig.isOneOfTheseRequired()) {%>
											 <span style="font-size:9px;color:blue;verticle-align:top">&dagger;&nbsp;</span>
										<%}%>
										<%if(fieldConfig.isRequired()) {%>
											 <span style="font-size:9px;color:red;verticle-align:top">*&nbsp;</span>
										<%}%>
										<%=fieldConfig.getDisplayName()%>
									</nobr>
								 
								 </td>
							      <td>
								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("MenuList")) {%>
								             <% if( "SystemCode".equalsIgnoreCase(fieldConfig.getName()))  {%>
                                                <select title="<%=fieldConfig.getName()%>"
												        name="<%=fieldConfig.getName()%>" 
                                                        onchange="javascript:setLidMaskValue(this,'advancedformData')"
												        id="SystemCode">	
											 <%} else {%>
                                               <select title="<%=fieldConfig.getName()%>"
												     name="<%=fieldConfig.getName()%>" >	
											<%}%>
                                              <%PullDownListItem[]   pullDownListItemArray = fieldConfig.getPossibleValues();%>
											    <option value=""></option>
											    <%for(int p = 0; p <pullDownListItemArray.length;p++) {%>
											     	<option value="<%=pullDownListItemArray[p].getName()%>"><%=pullDownListItemArray[p].getDescription()%></option>
												<%}%>
											</select>

								  <%}%>
								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("TextArea")) {%>
                                      <textarea 
									            id="<%=fieldConfig.getName()%>" 
												title="<%=title%>"
												name="<%=fieldConfig.getName()%>" ></textarea>
                                <%}%>

								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("TextBox")) {
									  %>
									  <%if(fieldConfig.getName().equalsIgnoreCase("LID")) {%>
                                                 <input type="text" 
												       id="LID"
												       title="<%=title%>"
												       name="<%=fieldConfig.getName()%>" 
													   maxlength="<%=maxlength%>" 
                                                       readonly="true"
													   onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"/>
 
									  <%} else {%>
									   <%if(fieldConfig.getValueType() == 6 ) {%>
                                          <nobr>
                                            <input type="text" 
												   id="<%=title%>"
												   title="<%=title%>"
												   name="<%=fieldConfig.getName()%>" 
												   maxlength="<%=maxlength%>" 
 												   size="<%=fieldConfig.getMaxLength()%>"
													onkeydown="javascript:qws_field_on_key_down(this, '<%=(fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0)?fieldConfig.getInputMask():""%>')"
													onkeyup="javascript:qws_field_on_key_up(this)"                                          onblur="javascript:validate_date(this,'<%=dateFormat%>')">
                                                  <a href="javascript:void(0);" 
												     title="<%=title%>"
                                                     onclick="g_Calendar.show(event,
												          '<%=title%>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<%=title%> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                          </nobr>
                                       <%} else {%>

                                                <input type="text" 
												       title="<%=title%>"
												       name="<%=fieldConfig.getName()%>" 
													   maxlength="<%=maxlength%>" 
 													   size="<%=fieldConfig.getMaxLength()%>"
													   onkeydown="javascript:qws_field_on_key_down(this, '<%=(fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0)?fieldConfig.getInputMask():""%>')"
													   onkeyup="javascript:qws_field_on_key_up(this)" />

                                       <%}%>

									  <%}%>
 								  <%}%>
								  </td>
  							   <%}%>
							   </tr>
							   <%}%>

							<%}%>
							 <tr><td colspan="2">&nbsp;</td></tr>
							</table>
                           </div> <!-- Search div ends here -->
                            <table  cellpadding="0" cellspacing="0" style="	border:0px red solid;padding-left:20px">
                                <tr>
                                    <td align="left">
                                        <nobr>
										<% if(operations.isPotDup_SearchView()){%>	
                                           <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"
										       href="javascript:void(0)"
                                               onclick="javascript:
										       document.getElementById('messages').innerHTML='';
											   getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
                                               <span>
                                                 <h:outputText value="#{msgs.search_button_label}"/>
                                               </span>
                                           </a>
										 <%}%>
                                        </nobr>
									    <nobr>
										    <h:outputLink  title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:
										    document.getElementById('messages').innerHTML='';
											ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                            </h:outputLink>
                                        </nobr>                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr><tr><td>&nbsp</td></tr>
				   <tr>
					 <td><div id="messages" class="ajaxalert" valign="bottom"></div></td>
				   </tr>
                </table>
            </h:form>
                        </td>				
                    </tr>
					<tr><td>&nbsp</td></tr>
					<tr><td align="left"><div id="messages" class="ajaxalert"></div></td></tr>					
                </table>

			<h:panelGrid>
               <h:panelGroup rendered="#{SearchDuplicatesHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			   <h:panelGroup rendered="#{SearchDuplicatesHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			</h:panelGrid>
<div  id="outputdiv"></div>

    </div>  
    </td>
    </tr>
</table>

</div>  
 <!-- Modified  on 27-09-2008, added banner and close link to confirmation pop up window -->
  <!-- Resolve popup div starts here  -->
    <div id="resolvePopupDiv" class="confirmPreview" style="TOP:250px;LEFT:300px;visibility:hidden;display:none">
       <h:form id="reportYUISearch">
         <input type="hidden" id="potentialDuplicateId" name="potentialDuplicateId" title="potentialDuplicateId" />
         <table cellspacing="0" cellpadding="0" border="0">
           <tr><th align="center" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
		   <th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('resolvePopupDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
                <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('resolvePopupDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
		   </th>
		   </tr>
           <tr><td colspan="2">&nbsp;</td></tr>
           <tr><td colspan="2"><b><h:outputText value="#{msgs.different_person_dailog_text}"/>&nbsp;
                  <h:selectOneMenu id="diffperson" title="resolveType">
                      <f:selectItem  itemValue="AutoResolve" itemLabel="Resolve Until Recalculation"/>
                      <f:selectItem  itemValue="Resolve" itemLabel="Resolve Permanently"/>
                 </h:selectOneMenu> 
                &nbsp;</b></td>
          </tr>
          <tr><td colspan="2"> &nbsp;</td></tr>
          <tr id="actions">
             <td colspan="2">
			 <table align="center">
			 <tr>
			 <td>
               <a  title="<h:outputText value="#{msgs.ok_text_button}" />" class="button" href="javascript:void(0)" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?resolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
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
  <!-- Resolve popup div ends here  -->
   
</body>        

        <%
         String[][] lidMaskingArray = searchDuplicatesHandler.getAllSystemCodes();
         
        %>
        <script>
            var systemCodes = new Array();
            var lidMasks = new Array();
        </script>
        
        <%
        for(int i=0;i<lidMaskingArray.length;i++) {
            String[] innerArray = lidMaskingArray[i];
            for(int j=0;j<innerArray.length;j++) {
            
            if(i==0) {
         %>       
         <script>
           systemCodes['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>      
         <%       
            } else {
         %>
         <script>
           lidMasks ['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
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
            var lidField =  getDateFieldName(formNameValue.name,'LID');
			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).readOnly = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }  
         
     var selectedSearchValue = document.getElementById("searchTypeList").options[document.getElementById("searchTypeList").selectedIndex].value;
     document.getElementById("selectedSearchType").value = selectedSearchValue;
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

<%
   if ("dashboard".equalsIgnoreCase(from))   {
   Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
   //currentTime = new java.util.Date();
   String queryStr ="";
   Long currentTime = new java.util.Date().getTime();
   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String startDateField = simpleDateFormatFields.format(currentTime);
   queryStr = "create_end_date="+startDateField;

   SimpleDateFormat simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String startTimeField = simpletimeFormatFields.format(currentTime);
   queryStr += "&create_end_time="+startTimeField;
   long milliSecsInADay = 86400000L; //24 hours back
   Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
   Date dt24HrsBack = new Date(currentTime - milliSecsInADay);

   simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String endDateField = simpleDateFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_date="+endDateField;

   simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String  endTimeField = simpletimeFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_time="+endTimeField;
%>

    <script>
	   var last24hours = "";
	     populateContents('advancedformData','create_start_date','<%=endDateField%>');
	     populateContents('advancedformData','create_start_time','<%=startTimeField%>');
	     populateContents('advancedformData','create_end_date','<%=startDateField%>');
	     populateContents('advancedformData','create_end_time','<%=endTimeField%>');
         var selectedSearchTypeVal = document.getElementById("searchTypeList").options[document.getElementById("searchTypeList").selectedIndex].value;
  	    ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random=<%=rand%>&<%=queryStr%>&selectedSearchType='+selectedSearchTypeVal,'outputdiv','')
     </script>
<% }  %>
<!-- added  on 22/08/2008 for incorporate back button -->
<% if(request.getParameter("back") != null )  {%>
<script>
    var queryStr = '<%=request.getQueryString()%>'
	setRand(Math.random());
  <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);%>
        populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
  <%} %>
	   
</script>
 <%
	searchDuplicatesHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));   
	searchDuplicatesHandler.setSearchType(request.getParameter("selectedSearchType")); 
	
	facesSession.setAttribute("SearchDuplicatesHandler",searchDuplicatesHandler);

   %>
<% } %>   
 
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
									showExtraDivs('activeDiv',event);
									getFormValues('advancedformData');
									setRand(Math.random());	
								ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');" class="button" >
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
<!-- modified on 11-11-08 as fix of 202 -->
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
                                onclick="javascript:showExtraDivs('mergeConfirmationDiv',event);
								if(reloadUrl.length<1) {
									getFormValues('advancedformData');
									setRand(Math.random());
									ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')	
								 }else {
										window.location = reloadUrl;
										reloadUrl='';
 								 }"
 							    class="button" >
                                <span><h:outputText value="#{msgs.source_inpatient1_text}"/></span>
                                </a>
							  </td>
							  <td>
                              <a title="<h:outputText value="#{msgs.source_inpatient2_text}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:showExtraDivs('mergeConfirmationDiv',event);
								getFormValues('advancedformData');
								setRand(Math.random());
								ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');"
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
				<!-- Modified  on 27-11-2008, fix of bug 275 added banner and close link to confirmation pop up window -->
                         <div id="mergeDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
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
								   <td align="left"><b>?</b></td>
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
                                                            onclick="javascript:
																			var rowcnt = document.getElementById('mergeFinalForm:rowCnt').value;
																			getFormValues('mergeFinalForm');			
																			getDuplicateFormValues('multiMergeFinal'+rowcnt,'advancedformData');setRand(Math.random());
																			showExtraDivs('mergeDiv',event);
																			ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?multiMergeEOs=true&random='+rand+'&'+queryStr+'&rwcnt='+rowcnt,'outputdiv','');">
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
												  <input type="hidden" id="mergeFinalForm:rowCnt" title="rowCount">
                                                  <input type="hidden" id="mergeFinalForm:srcDestnEuids" title="MERGE_SRC_DESTN_EUIDS" />
                                                <h:inputHidden id="destinationEO" value="#{SearchDuplicatesHandler.destnEuid}" />
                                                <h:inputHidden id="selectedMergeFields" value="#{SearchDuplicatesHandler.selectedMergeFields}" />
                                            </h:form>
                                        </td>
                                    </tr>
                                    
                                </table>
                        </div>  
  	 </html> 
<script type="text/javascript">
  makeDraggable("resolvePopupDiv");
  makeDraggable("mergeConfirmationDiv");
  makeDraggable("activeDiv");
  makeDraggable("mergeDiv");
  </script>
         <%if(request.getParameter("back")!=null){%>
	   	<% 
		  String qryString  = request.getQueryString();
	      qryString  = qryString.replaceAll("collectEuids=true","");
		%>

    <script>
            var queryStrVar = '<%=qryString%>';
		    getRecordDetailsFormValues('searchTypeForm');
			ajaxURL('/<%=URI%>/ajaxservices/searchscreenservices.jsf?random='+rand+'&'+queryStr+'&myStrVar='+queryStrVar,'SearchCriteria','');
   </script>
   <%}%>

</f:view>
