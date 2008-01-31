<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
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
        <h:form  id="AssumeMatchForm">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                  <td>                            
                      <h:dataTable id="fieldConfigId" var="feildConfig" value="#{AssumeMatchHandler.searchScreenConfigArray}">
                
                <!--Rendering Non Updateable HTML Text Area-->
                <h:column>
                    <h:outputLabel for="#{feildConfig.name}" value="#{feildConfig.displayName}" />                        
                </h:column>
                
                <!--Rendering Updateable HTML Text boxes-->
                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                    <h:inputText label="#{feildConfig.name}"    id="fieldConfigIdText" value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                </h:column>
                
                <!--Rendering Updateable HTML Text Area-->
                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                    <h:inputTextarea label="#{feildConfig.name}"  id="fieldConfigIdTextArea"   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                </h:column>
                
                
                <!--Rendering Non Updateable HTML Text boxes-->
                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                    <h:inputText label="#{feildConfig.name}"    id="EUID" size="14" maxlength="10" value="#{AssumeMatchHandler.EUID}" required="#{feildConfig.required}"/>
                </h:column>
                
                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                    <h:inputText label="#{feildConfig.displayName}" size="14" maxlength="12"   id="LID" value="#{AssumeMatchHandler.LID}" required="#{feildConfig.required}"
                                 onkeydown="javascript:qws_field_on_key_down(this, 'DDD-DDD-DDDD')"
                                 onkeyup="javascript:qws_field_on_key_up(this)"/>
                </h:column>
                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                    <h:inputText label="#{feildConfig.name}"    value="#{AssumeMatchHandler.create_start_date}"  id="create_start_date"
                                 required="#{feildConfig.required}" size="12" maxlength="10"
                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                    <a HREF="javascript:void(0);" 
                       onclick="g_Calendar.show(event,'AssumeMatchForm:fieldConfigId:4:create_start_date')" > 
                        <h:graphicImage  id="calImgStartDate" 
                                         alt="calendar Image" styleClass="imgClass"
                                         url="./images/cal.gif"/>               
                    </a>
                </h:column>
                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                    <h:inputText label="#{feildConfig.name}"    value="#{AssumeMatchHandler.create_end_date}" id="create_end_date" 
                                 required="#{feildConfig.required}" size="12" maxlength="10"
                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 'AssumeMatchForm:fieldConfigId:6:create_end_date')" > 
                        <h:graphicImage  id="calImgEndDate" 
                                         alt="calendar Image" styleClass="imgClass"
                                         url="./images/cal.gif"/>               
                    </a>
                </h:column>
                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                    <h:inputText label="#{feildConfig.name}"    rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                 value="#{AssumeMatchHandler.create_start_time}" required="#{feildConfig.required}" size="12" maxlength="8"
                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                 onkeyup="javascript:qws_field_on_key_up(this)"/>
                </h:column>
                
                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                    <h:inputText label="#{feildConfig.name}"    id="create_end_time" value="#{AssumeMatchHandler.create_end_time}" 
                                 required="#{feildConfig.required}" size="12" maxlength="8"
                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                 onkeyup="javascript:qws_field_on_key_up(this)"/>
                </h:column>
                <!--Rendering HTML Select Menu List-->
                <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                    <h:selectOneMenu value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}">
                        <f:selectItem itemLabel="" itemValue="" />
                        <f:selectItems  value="#{feildConfig.selectOptions}" />
                    </h:selectOneMenu>
                </h:column>
                
                
                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                    status <h:inputText label="#{feildConfig.name}"    id="Status"  value="#{AssumeMatchHandler.Status}" required="#{feildConfig.required}"/>
                </h:column>            
                
                <f:facet name="footer">                    
                    <h:column>
                        <a class="button" href="javascript:ClearContents('AssumeMatchForm')">
                            <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                        </a>
                        <h:commandLink  id="submitSearch" styleClass="button" rendered="#{Operations.assumedMatch_SearchView}" action="#{AssumeMatchHandler.performAssumeSearch}">  
                            <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                        </h:commandLink>                                     
                    </h:column>
                </f:facet>                
            </h:dataTable>  
                 </td>      
                 <td valign="top">
                     <h:messages  styleClass="errorMessages"  layout="list" />
                 </td>     
             </tr>
         </table>                                               
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
</html>
</f:view>              