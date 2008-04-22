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
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.math.BigDecimal"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>


<f:view>
    
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
        </head>
        <body>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden">   
                    <div id="advancedSearch" class="basicSearchDup" style="visibility:visible;display:block">
                                <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{SearchDuplicatesHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{SearchDuplicatesHandler.possilbeSearchTypesCount gt 1}" 
                                                     onchange="submit();" style="width:220px;" 
                                                     value="#{SearchDuplicatesHandler.searchType}" 
                                                     valueChangeListener="#{SearchDuplicatesHandler.changeSearchType}" >
                                        <f:selectItems  value="#{SearchDuplicatesHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{SearchDuplicatesHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
		           <tr>
				     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{SearchDuplicatesHandler.instructionLine}" /></td>
			       </tr>

                    <tr>
                        <td>
                            <input id='lidmask' type='hidden' name='lidmask' value='' />
							
                            <h:dataTable headerClass="tablehead"  
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{SearchDuplicatesHandler.searchScreenFieldGroupArray}">
						    <h:column>
   				            <div style="font-size:12px;font-weight:bold;color:#0739B6;" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h:outputText value="#{searchScreenFieldGroup.description}" /></div>
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{searchScreenFieldGroup.fieldConfigs}">

                                <!--Rendering Non Updateable HTML Text Area-->
                                 <h:column>
                                            <nobr>
                                                <h:outputText value="*" rendered="#{feildConfig.required}" />
                                                <h:outputText value="#{feildConfig.displayName}" />
                                            </nobr>
                                        </h:column>
                                        <!--Rendering HTML Select Menu List-->
                                        <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                            <nobr>
                                                <h:selectOneMenu onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                                 rendered="#{feildConfig.name ne 'SystemCode'}"
	                                                             value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
                                                                  onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                                  id="SystemCode" 
    															  value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                                  rendered="#{feildConfig.name eq 'SystemCode'}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                            </nobr>
                                        </h:column>
                                        <!--Rendering Updateable HTML Text boxes-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 }" >
                                            <nobr>
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   readonly="true"
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value);javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
                                                               onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
                                                   value="<h:outputText value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                   required="<h:outputText value="#{feildConfig.required}"/>" 
                                                   maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateFieldsOnBlur(this,'<h:outputText value="#{feildConfig.name}"/>')">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{feildConfig.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                        </h:column>
                            </h:dataTable> <!--Field config loop-->
							</h:column>
                            </h:dataTable> <!--Field groups loop-->

                            <table  cellpadding="0" cellspacing="0" style="	border:0px red solid;padding-left:20px">
                                <tr>
                                    <td>
                                        <nobr>
                                            <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                            </h:outputLink>
                                        </nobr>
                                        <nobr>
                                            <h:commandLink  styleClass="button" 
											                rendered="#{Operations.potDup_SearchView}"  action="#{SearchDuplicatesHandler.performSubmit}" >  
                                                <span>
                                                    <h:outputText value="#{msgs.search_button_label}"/>
                                                </span>
                                            </h:commandLink>                                     
                                        </nobr>
                                        
                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td valign="top">
                            <h:messages styleClass="errorMessages"  layout="list" />
                        </td>
                    </tr>
                </table>
                <h:inputHidden id="enteredFieldValues" value="#{SearchDuplicatesHandler.enteredFieldValues}"/>
            </h:form>

                   </div>  
                  <%
                    ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");

					ArrayList finalArrayList = (ArrayList) request.getAttribute("finalArrayList");
                    
					PotentialDuplicateIterator asPdIter;
                    PotentialDuplicateSummary mainDuplicateSummary = null;
                    
                    PotentialDuplicateSummary associateDuplicateSummary = null;
                    int countMain = 0;
                    int totalMainDuplicates = 0;
                    int totalAssoDuplicates = 0;
                    String mainDob = null;
                    String assoDob = null;
                    
                    String mainEuidContentDiv = null ;
                    String assoEuidContentDiv = null ;
                    String previewEuidContentDiv = null ;
                    String dupHeading = "<b>Duplicate </b>";
                    SearchDuplicatesHandler searchDuplicatesHandler = new  SearchDuplicatesHandler(); 
                    Iterator searchResultFieldsIter = searchDuplicatesHandler.getResultsConfigArray().iterator();
                    Object[] resultsConfigFeilds  = searchDuplicatesHandler.getResultsConfigArray().toArray();
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer mainEUID = new StringBuffer();
                    StringBuffer dupEUID = new StringBuffer();
                    ValueExpression finalArrayListVE = null;
					ValueExpression potDupIdValueExpression = null;
					ValueExpression duplicateSearchObjectVE = null;
                    PotentialDuplicateSearchObject potentialDuplicateSearchObject = (PotentialDuplicateSearchObject) request.getAttribute("duplicateSearchObject");                                    
				     if(potentialDuplicateSearchObject != null) {
						   duplicateSearchObjectVE  = ExpressionFactory.newInstance().createValueExpression(potentialDuplicateSearchObject, potentialDuplicateSearchObject.getClass());
					}
                    %>                
                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {
						 //System.out.println("-------------IN JSPPPP---finalArrayList.size()--" + finalArrayList.size());
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                     <div class="printClass">
                       <table cellpadding="0" cellspacing="0" border="0" align="right">
                         <tr>
                             <td>
                                 <h:outputText value="#{msgs.total_records_text}"/><%=finalArrayList.size()%>&nbsp;&nbsp;
                             </td>
                             <td>
                                  <h:outputLink styleClass="button" 
                                                rendered="#{Operations.potDup_Print}"  
                                                value="JavaScript:window.print();">
                                      <span><h:outputText value="#{msgs.print_text}"/>  </span>
                                 </h:outputLink>              
                             </td>
                         </tr>
                       </table>
                    </div>
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                     <div id="dataDiv" class="compareResults" style="overflow:auto;width:1074px;height:1024px;">
                    <div>
                        <table>
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String potDupStatus = (String) eoHashMapValues.get("Status");
                                                String potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));
											   //System.out.println("fieldValuesMapSource EUID ====>" + fieldValuesMapSource.get("EUID"));
											   //System.out.println("eoHashMapValues EUID ====>" + eoHashMapValues.get("EUID"));
                                               

                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                            <a class="dupbtn" href="javascript:accumilateMultiMergeEuidsPreview('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>')">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                             epathValue = fieldConfigMap.getFullFieldName();
                                                         } else {
                                                             epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                         }

                                                     %>
                                                    <tr>
                                                        <td>

                                                            <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
                                            </div>   
                                        </td>
                                        <%} else {%> <!--For duplicates here-->  

                                            <%if (j ==1 && arlInner.size() > 3 ) { %>
                                            <!--Sri-->
                                            <td>
                                                 <div style="overflow:auto;width:507px;overflow-y:hidden;">
                                                     <table>
                                                         <tr>
                                            <%}%>
                                        
                                           <td valign="top">

                                                <%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                            <a class="dupbtn" href="javascript:accumilateMultiMergeEuidsPreview('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>')">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
                                                      <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar" style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar" style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar" style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar" style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                             epathValue = fieldConfigMap.getFullFieldName();
                                                         } else {
                                                             //epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
															 epathValue = fieldConfigMap.getFullFieldName();
                                                         }
                                                             
                                                    %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
												   <h:form>
                                                        <h:commandLink  styleClass="diffviewbtn" rendered="#{Operations.potDup_ResolveUntilRecalc}"
                                                                        actionListener="#{SearchDuplicatesHandler.unresolvePotentialDuplicateAction}">
                                                             <f:attribute name="potDupId" value="<%=potDupIdValueExpression%>"/>
                                                             <f:attribute name="finalArrayListVE" value="<%=finalArrayListVE%>"/>
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </h:commandLink>  
												   </h:form>
												<%}else{%>
                                                           <a onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" 
                                                              href="javascript:void(0)">
															  <img src="./images/diff.gif" alt="Different Person" border="0">
                                                            </a>   

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                            <%if (arlInner.size() > 3 && j == (arlInner.size()-1 )) { %>
                                            <!--Raj-->
                                                       </tr>
                                                     </table>
                                                 </div>
                                                </td>
                                            <%}%>
                                        
                                        <%}%>
                                        <td class="w7yelbg">&nbsp;</td><!--Separator b/n columns-->
                                        <%}%>
                                      <td  valign="top">
									  <%if(request.getAttribute("eoMultiMergePreview" + new Integer(fac).toString() ) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
                                                 <tr>
                                                        <td>&nbsp;</td>
                                                </tr>
                                                <%
											      HashMap previewHashMap = new HashMap();
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        if (fieldConfig.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                             epathValue = fieldConfig.getFullFieldName();
                                                         } else {
                                                             epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfig.getFullFieldName();
                                                         }

                                                    %>
                                                      <%if(request.getAttribute("eoMultiMergePreview" + new Integer(fac).toString() ) != null ) {
														previewHashMap  = (HashMap) request.getAttribute("eoMultiMergePreview" + new Integer(fac).toString());
														HashMap eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT");
                                                         %>

                                                    <tr>
                                                        <td>
														 <%if(eoMapPreview.get(epathValue) != null)  {%>
   														     <%=eoMapPreview.get(epathValue)%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
													<%} else {%>
													 <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>

													<%}%>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="right">
                                                        <div id="buttonsDiv<%=fac%>" style="visibility:hidden;">
														 <p>
                                                            <h:form id="mergeFinalForm">
                                                              <h:commandLink rendered="#{Operations.potDup_SearchView}"  styleClass="button"
                                                                          actionListener="#{SearchDuplicatesHandler.previewPostMultiMergedEnterpriseObject}">
                                                                   <f:attribute name="finalArrayListVE" value="<%=finalArrayListVE%>"/>
                                                                   <f:attribute name="duplicateSearchObjectVE"  value="<%=duplicateSearchObjectVE%>"  />
                                                                    <span>Preview</span>
                                                              </h:commandLink>
                                                                <h:commandLink rendered="#{Operations.potDup_SearchView}"  styleClass="button"
                                                                               actionListener="#{SearchDuplicatesHandler.cancelMultiMergeOperation}">
                                                                         <f:attribute name="duplicateSearchObjectVE"  value="<%=duplicateSearchObjectVE%>"  />
                                                                    <span>Cancel</span>
                                                                </h:commandLink>
   		                                                     <h:inputHidden id="rowCount" value="#{SearchDuplicatesHandler.rowCount}"/>
   		                                                     <h:inputHidden id="destinationEO" value="#{SearchDuplicatesHandler.destnEuid}"/>
                                                 			 <h:inputHidden id="previewhiddenMergeEuids" value="#{SearchDuplicatesHandler.mergeEuids}"/>
                                                            </h:form>   
															</p>
                                                        </div>
														
														<%if(request.getAttribute("eoMultiMergePreview" + new Integer(fac).toString() ) != null ) {
                                                         %>
														   <nobr>
                                                            <h:form id="mergeFinal">
                                                               <h:commandLink rendered="#{Operations.potDup_SearchView}"  styleClass="button"
                                                                          actionListener="#{SearchDuplicatesHandler.performMultiMergeEnterpriseObject}">
                                                                    <f:attribute name="duplicateSearchObjectVE"  value="<%=duplicateSearchObjectVE%>"  />
                                                                    <span>Merge</span>
                                                               </h:commandLink>
														   </nobr>
														   <nobr>
                                                                <h:commandLink rendered="#{Operations.potDup_SearchView}"  styleClass="button"
                                                                               actionListener="#{SearchDuplicatesHandler.cancelMultiMergeOperation}">
                                                                    <span>Cancel</span>
                                                                </h:commandLink>
   		                                                     <h:inputHidden id="rowCount" value="#{SearchDuplicatesHandler.rowCount}"/>
   		                                                     <h:inputHidden id="destinationEO" value="#{SearchDuplicatesHandler.destnEuid}"/>
                                                 			 <h:inputHidden id="previewhiddenMergeEuids" value="#{SearchDuplicatesHandler.mergeEuids}"/>
                                                            </h:form>   
														   </nobr>
														 <%}%>

                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                        <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            ValueExpression euidVaueExpressionList = ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
                                                         %>
                                                                             <h:form>
                                                                                <h:commandLink styleClass="downlink"  rendered="#{Operations.potDup_SearchView}" 
                                                                                               action="#{NavigationHandler.toCompareDuplicates}" 
                                                                                               actionListener="#{SearchDuplicatesHandler.buildCompareDuplicateEuids}">
                                                                                    <f:attribute name="euidsMap"  value="<%=euidVaueExpressionList%>"  />
                                                                                </h:commandLink>
                                                                            </h:form>   
                                                        </td>
                                                    </tr>
                                                        

                                            </table>
                                      </div>  
                                    </div>
                                    
                           </tr>
                        </table>
                    </div> 
                    <div id="separator"  class="sep"></div>
                         <%}%> <!--final Array list count loop -->
                         <%}%> <!-- final Array list  condition in session-->
               </div> 
               <%}%>  
               <%
                   if (finalArrayList != null && finalArrayList.size() == 0) {
               %>
               <div class="printClass">
                       <table cellpadding="0" cellspacing="0" border="0">
                         <tr>
                             <td>
                                 <h:outputText value="#{msgs.total_records_text}"/><%=finalArrayList.size()%>&nbsp;
                             </td>
                         </tr>
                       </table>
               </div>
               <%}%>
               
            </div>
            
            <%
                       if(finalArrayList != null && finalArrayList.size() >0 ) {
                       finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
                       

            %>
                                   <div id="resolvePopupDiv" class="alert" style="TOP:2250px; LEFT:300px; HEIGHT:195px;WIDTH: 300px;visibility:hidden; ">
                                     
                                       <h:form id="reportYUISearch">
                                           <h:inputHidden id="potentialDuplicateId" value="#{SearchDuplicatesHandler.potentialDuplicateId}"/>
                                           <table width="100%">
                                               <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="resolvepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                               <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr><td align="center"><b><h:outputText value="#{msgs.different_person_dailog_text}"/></b></td></tr>
                                               <tr>
                                                   <td  colspan="2">
                                                       <div class="selectContent">
                                                       <h:selectOneRadio id="diffperson" value="#{SearchDuplicatesHandler.resolveType}" 
                                                                         layout="pageDirection">
                                                           <f:selectItem  itemValue="AutoResolve" itemLabel="Resolve Until Recalculation"/>
                                                           <f:selectItem  itemValue="Resolve" itemLabel="Resolve Permanently"/>
                                                       </h:selectOneRadio> 
                                                       </div> 
                                                 </td>
                                               </tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr>
                                                   <td align="right"  colspan="2">
                                                       
                                                        <h:commandLink styleClass="button" 
                                                                       actionListener="#{SearchDuplicatesHandler.resolvePotentialDuplicate}">
                                                            <f:attribute name="finalArrayListVE" value="<%=finalArrayListVE%>" />           
                                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </h:commandLink>   
                                                       <h:outputLink  onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'123467')" 
                                                                      styleClass="button"  
                                                                      value="javascript:void(0)">
                                                         <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                       </h:outputLink>   
                                                   </td>
                                               </tr>
                                           </table>
                                           
                                       </h:form>
                                   </div>                                                
              <%}%>

            
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
         
     var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
     document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
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

    </html> 
</f:view>
