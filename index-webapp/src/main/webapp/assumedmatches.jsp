<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>
<f:view>    
<f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
<html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
       </head>
<title><h:outputText value="#{msgs.application_heading}"/></title>  
<body>
    <%@include file="./templates/header.jsp"%>
     <div id="mainContent" style="overflow:hidden;">   
    
    <div id ="assumedmatches " class="basicSearch">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{AssumeMatchHandler.searchType}" valueChangeListener="#{AssumeMatchHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AssumeMatchHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{AssumeMatchHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td>
                            <input id='lidmask' type='hidden' name='lidmask' value='' />
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{AssumeMatchHandler.screenConfigArray}">
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
                                        <h:selectOneMenu value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                         rendered="#{feildConfig.name ne 'SystemCode'}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                        
                                        <h:selectOneMenu  onchange="javascript:setLidMaskValue(this)"
                                                          onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                          id="SystemCode" 
                                                          value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}" 
                                                          rendered="#{feildConfig.name eq 'SystemCode'}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                    </nobr>
                                </h:column>
                                <!--Rendering Updateable HTML Text boxes-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6}" >
                                    <nobr>
                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:validate_number(this,'#{feildConfig.displayName}');javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{feildConfig.maxLength}" 
                                                       rendered="#{feildConfig.name ne 'LID'}"/>
                                        
                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                       rendered="#{feildConfig.name eq 'LID'}"/>
                                                       
                                    </nobr>
                                </h:column>
                                
                                <!--Rendering Updateable HTML Text Area-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                    <nobr>
                                        <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                    <nobr>
                                        <h:inputText  label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.displayName]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var dateFrom =  getDateFieldName('advancedformData','DOBFrom');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,dateFrom)" > 
                                            <h:graphicImage  id="calImgDateFrom" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                            </h:dataTable>
                            <table border="0" cellpadding="0" cellspacing="0" >
                                <tr>
                                    <td>
                                        <nobr>
                                            <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                            </h:outputLink>
                                        </nobr>
                                        <nobr>
                                            <h:commandLink  styleClass="button" rendered="#{Operations.assumedMatch_SearchView}"  action="#{AssumeMatchHandler.performSubmit}" >  
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
                <h:inputHidden id="enteredFieldValues" value="#{AssumeMatchHandler.enteredFieldValues}"/>
            </h:form>
    </div>
    
    <div class="printClass">
    <h:panelGrid rendered="#{AssumeMatchHandler.searchSize gt 0}" columns="3">
                        <h:outputText rendered="#{AssumeMatchHandler.searchSize gt -1}" value="#{msgs.total_records_text}"/>
                        <h:outputText rendered="#{AssumeMatchHandler.searchSize gt -1}" value="#{AssumeMatchHandler.searchSize}" />
                        <h:outputLink   styleClass="button" rendered="#{Operations.assumedMatch_Print && AssumeMatchHandler.searchSize gt 0}" value="javaScript:window.print();">
                            <span><h:outputText value="#{msgs.print_text}"/></span>
                        </h:outputLink>                                    
    </h:panelGrid>
    </div>
    
    <div class="reportYUISearch">
       <h:form>
           <yui:datatable var="assumeMatch" value="#{AssumeMatchHandler.assumeMatchesRecordsVO}"
                                   paginator="true"
                                   rowClasses="even,odd"
                                   tableClass="reportYUISearch"
                                   rows="50"
                                   rendered="#{AssumeMatchHandler.searchSize gt 0}"
                                   width="1024px">
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText  value="#{msgs.datatable_euid_text}"/>
                            </f:facet>
                            <a href='ameuiddetails.jsf?AMID=<h:outputText value="#{assumeMatch.assumedMatchId}" />'>
                                 <h:outputText value="#{assumeMatch.euid}" /> 
                            </a>                            
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText  value="#{msgs.datatable_localid_text}" />
                            </f:facet>
                            <h:outputText value="#{assumeMatch.localId}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText  value="#{msgs.potential_dup_table_system_column}" />
                            </f:facet>
                            <h:outputText value="#{assumeMatch.systemCode}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText  value="#{msgs.datatable_firstname_text}"/>
                            </f:facet>
                           <div id="multicolumn">
                            <h:dataTable var="fname" value="#{assumeMatch.firstName}">
                                <h:column>
                                    <h:outputText value="#{fname}" />
                                </h:column>
                            </h:dataTable>
                        </div>
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_lastname_text}"/>
                            </f:facet>
                           <div id="multicolumn">
                            <h:dataTable var="lname" value="#{assumeMatch.lastName}">
                                <h:column>
                                    <h:outputText value="#{lname}" />
                                </h:column>
                            </h:dataTable>
                        </div>
                            
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_ssn_text}"/>
                            </f:facet>
                           <div id="multicolumn">
                            <h:dataTable var="social" value="#{assumeMatch.ssn}">
                                <h:column>
                                    <h:outputText value="#{social}" />
                                </h:column>
                            </h:dataTable>
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_DOB_text}"/>
                            </f:facet>
                            <div id="multicolumn">
                            <h:dataTable var="birth" value="#{assumeMatch.dob}">
                                <h:column>
                                    <h:outputText value="#{birth}" />
                                </h:column>
                            </h:dataTable>
                        </div>
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_address_text}" />
                            </f:facet>
                            <div id="multicolumn">
                                <h:dataTable var="line1" value="#{assumeMatch.addressLine1}">
                                    <h:column>
                                        <h:outputText value="#{line1}" />
                                    </h:column>
                                </h:dataTable>
                            </div>
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_weight_text}"/>
                            </f:facet>
                            <h:outputText value="#{assumeMatch.weight}" />
                        </yui:column>
                        
       </yui:datatable>
       </h:form>
    </div>
</div>
   </div>
</body>
        <%
          AssumeMatchHandler  assumeMatchHandler = new AssumeMatchHandler();
          String[][] lidMaskingArray = assumeMatchHandler.getAllSystemCodes();
          
          
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
        function setLidMaskValue(field) {
            var  selectedValue = field.options[field.selectedIndex].value;
            document.advancedformData.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
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