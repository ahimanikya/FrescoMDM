<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditDataObject"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditSearchObject"  %>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
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
        <%@include file="./templates/header.jsp"%>
        <body>
             <div id="mainContent">     
              <div id ="auditlog " class="basicSearch">
                   <h:form   id="AuditSearchForm" >
                        <table>
                            <tr>
                                <td>                            
                                    <table border="0" cellpadding="0" cellspacing="0">                                
                                        <tr> 
                                            <nobr>
                                            <td>
                                                <h:outputLabel  for="#{msgs.source_edit_fromDatePrompt}" value="#{msgs.source_edit_fromDatePrompt}"/>:
                                            </td>
                                            </nobr>
                                            <td align="left">    
                                                <nobr>
                                                <h:inputText 
                                                    label="#{AuditLogHandler.createStartDate}"
                                                    id="createStDateField"
                                                    value="#{AuditLogHandler.createStartDate}"
                                                    onchange="setchanged('');"
                                                    onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                    onkeyup="javascript:qws_field_on_key_up(this)" 
                                                    onblur="javascript:compareDate(this)"
                                                    size="12"
                                                    maxlength="10"/>
                                                <A HREF="javascript:void(0);"
                                                   onclick="g_Calendar.show(event, 'AuditSearchForm:createStDateField')">
                                                    <h:graphicImage  id="calImg" 
                                                                     alt="calendar Image"  
                                                                     url="./images/cal.gif" styleClass="imgClass"/>                                                    
                                                </A>
                                                </nobr>
                                            </td>
                                            <nobr>
                                            <td>
                                                <h:outputLabel  for="#{msgs.source_edit_toDatePrompt}"  value="#{msgs.source_edit_toDatePrompt}"/>:
                                            </td>
                                            </nobr>
                                            
                                            <td align="left">
                                                <nobr>    
                                                <h:inputText 
                                                    id="createEndDateField"
                                                    value="#{AuditLogHandler.createEndDate}" 
                                                    label="#{AuditLogHandler.createEndDate}" 
                                                    onchange="setchanged('');"
                                                    onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')" 
                                                    onkeyup="javascript:qws_field_on_key_up(this)" 
                                                    size="12" 
                                                    maxlength="10"/>
                                        
                                                <A HREF="javascript:void(0);"
                                                   onclick="g_Calendar.show(event, 'AuditSearchForm:createEndDateField')">
                                                    <h:graphicImage styleClass="imgClass" id="calImg1" 
                                                                    alt="calendar Image"  
                                                                    url="./images/cal.gif"/>               
                                                </A>
                                                 </nobr>
                                            </td>
                                       
                                        </tr>
                                        <tr>                                 
                                            <td>
                                                <h:outputLabel  for="#{msgs.source_edit_fromTimePrompt}"  value="#{msgs.source_edit_fromTimePrompt}"/>:
                                            </td>
                                            <td align="left">    
                                                <h:inputText  
                                                    id="createStartTimeField"
                                                    value="#{AuditLogHandler.createStartTime}" 
                                                    label="#{AuditLogHandler.createStartTime}" 
                                                    onchange="setchanged('');" 
                                                    onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                    onkeyup="javascript:qws_field_on_key_up(this)"
                                                    size="12"
                                                    maxlength="8"/>
                                                
                                            </td>
                                            <td>
                                                <h:outputLabel  for="#{msgs.source_edit_toTimePrompt}"  value="#{msgs.source_edit_toTimePrompt}"/>:
                                            </td>
                                            <td align="left">    
                                                <h:inputText 
                                                    id="createEndTimeField"
                                                    value="#{AuditLogHandler.createEndTime}" 
                                                    label="#{AuditLogHandler.createEndTime}" 
                                                    onchange="setchanged('');"  
                                                    onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                    onkeyup="javascript:qws_field_on_key_up(this)"
                                                    size="12"
                                                    maxlength="8"/>
                                                
                                            </td>
                                        </tr>
                                        
                                        <tr>
                                            <td><h:outputLabel  for="#{msgs.transaction_euid}"  value="#{msgs.transaction_euid}"/></td>     
                                            <td> <h:inputText id="euid"  label="#{AuditLogHandler.euid}"  value="#{AuditLogHandler.euid}" size="10" maxlength="10"/></td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td><h:outputLabel  for="#{msgs.transaction_system_user}" value="#{msgs.transaction_system_user}"/></td>     
                                            <td> <h:inputText id="user" size="12" label="#{AuditLogHandler.systemuser}" value="#{AuditLogHandler.systemuser}"/></td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>                            
                                        <tr>
                                            <td><h:outputLabel  for="#{msgs.transaction_function}" value="#{msgs.transaction_function}"/></td>
                                            <td>
                                                <h:selectOneMenu  styleClass="selectContent" id="function" value="#{AuditLogHandler.resultOption}" >
                                                    <f:selectItem  itemValue="" itemLabel=""/>
                                                    <f:selectItem  itemValue="Add" itemLabel="Add"/>
                                                    <f:selectItem  itemValue="Associated Potential Duplicates" itemLabel="Associated Potential Duplicates"/>
                                                    <f:selectItem  itemValue="Assumed Match Comparison" itemLabel="Assumed Match Comparison"/>
                                                    <f:selectItem  itemValue="Assumed Match Search Result" itemLabel="Assumed Match Search Result"/> 
                                                    <f:selectItem  itemValue="Auto Resolve" itemLabel="Auto Resolve"/> 
                                                    <f:selectItem  itemValue="EO Comparison" itemLabel="EO Comparison"/>
                                                    <f:selectItem  itemValue="EO Search Result" itemLabel="EO Search Result"/>
                                                    <f:selectItem  itemValue="EO View/Edit" itemLabel="EO View/Edit"/>
                                                    <f:selectItem  itemValue="EUID Merge Confirm" itemLabel="EUID Merge Confirm"/>
                                                    <f:selectItem  itemValue="EUID Unmerge" itemLabel="EUID Unmerge"/>
                                                    <f:selectItem  itemValue="EUID Unmerge Confirm" itemLabel="EUID Unmerge Confirm"/>
                                                    <f:selectItem  itemValue="History Comparison" itemLabel="History Comparison"/>
                                                    <f:selectItem  itemValue="Hisory Search Result" itemLabel="Hisory Search Result"/>
                                                    <f:selectItem  itemValue="LID Merge - Selection" itemLabel="LID Merge - Selection"/>
                                                    <f:selectItem  itemValue="LID Merge Confirm" itemLabel="LID Merge Confirm"/>
                                                    <f:selectItem  itemValue="LID Unmerge" itemLabel="LID Unmerge"/>
                                                    <f:selectItem  itemValue="LID Unmerge Confirm" itemLabel="LID Unmerge Confirm"/>
                                                    <f:selectItem  itemValue="Matching Review Search Result" itemLabel="Matching Review Search Result"/>
                                                    <f:selectItem  itemValue="Merge" itemLabel="Merge"/>
                                                    <f:selectItem  itemValue="Merge Tree Comparison" itemLabel="Merge Tree Comparison"/>
                                                    <f:selectItem  itemValue="Potential Duplicate Comparison" itemLabel="Potential Duplicate Comparison"/>
                                                    <f:selectItem  itemValue="Resolve" itemLabel="Resolve"/>
                                                    <f:selectItem  itemValue="Undo Assumed Match" itemLabel="Undo Assumed Match"/>
                                                    <f:selectItem  itemValue="Unmerge Comparison" itemLabel="Unmerge Comparison"/>
                                                    <f:selectItem  itemValue="Unresolve" itemLabel="Unresolve"/>
                                                    <f:selectItem  itemValue="Update" itemLabel="Update"/>
                                                    <f:selectItem  itemValue="View Merge Tree" itemLabel="View Merge Tree"/>                                        
                                                </h:selectOneMenu>  
                                            </td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan="4">                                    
                                                <nobr>
                                                 <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:ClearContents('AuditSearchForm')">
                                                    <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                </h:outputLink>
                                                <h:commandLink   id="submitSearch" styleClass="button" rendered="#{Operations.auditLog_SearchView}" action="#{AuditLogHandler.auditLogSearch}">  
                                                    <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                </h:commandLink>                                                    
                                            </nobr>
                                            </td>
                                        </tr>                    
                                    </table>
                                </td>
                                
                                <td valign="top">
                                    <h:messages  styleClass="errorMessages"  layout="list" />
                                </td>
                            </tr>
                        </table>                              
                        
                    </h:form>
                </div> 
            </div>  
            <br>       
            
            <div class="printClass"> 
            <h:panelGrid rendered="#{AuditLogHandler.resultsSize gt 0}" columns="3">
                                    <h:outputText rendered="#{AuditLogHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>
                                    <h:outputText rendered="#{AuditLogHandler.resultsSize gt -1}" value="#{AuditLogHandler.resultsSize}" />
                                    <h:outputLink styleClass="button" rendered="#{Operations.auditLog_Print && AuditLogHandler.resultsSize gt 0}" value="javaScript:window.print();">                                            
                                        <span> <h:outputText value="#{msgs.print_text}"/></span>
                                    </h:outputLink>                                                           
              </h:panelGrid>          
             </div>
            
            <% if(request.getAttribute("resultsSize")!=null &&  ((Integer)request.getAttribute("resultsSize")).intValue() == 0 ) {%>           
            <div class="printClass" >
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <h:outputText rendered="#{AuditLogHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>
                            <h:outputText rendered="#{AuditLogHandler.resultsSize gt -1}" value="#{AuditLogHandler.resultsSize}" />&nbsp;&nbsp;
                        </td>
                    </tr>
                </table>
            </div>
            <%}%>
            <div class="reportYUISearch">       
                    <yui:datatable var="auditLog" value="#{AuditLogHandler.auditLogVO}"
                                   paginator="true"
                                   rendered="#{AuditLogHandler.resultsSize gt 0}"
                                   rows="50"     
                                   rowClasses="even,odd"                                   
                                   width="1024px">                                                                            
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_auditid_text}"/>
                            </f:facet>                            
                            <h:outputText value="#{auditLog.id}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_euid1_text}" />
                            </f:facet>
                              <h:outputText value="#{auditLog.EUID1}"/>
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_function_text}" />
                            </f:facet>
                            <h:outputText value="#{auditLog.function}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_detail_text}" />
                            </f:facet>
                            <h:outputText value="#{auditLog.detail}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_createuser_text}" />
                            </f:facet>
                            <h:outputText value="#{auditLog.createUser}" />
                        </yui:column>
                        <yui:column sortable="true" resizeable="true">
                            <f:facet name="header">
                                <h:outputText value="#{msgs.datatable_createdate_text}"/>
                            </f:facet>
                            <h:outputText value="#{auditLog.createDate}" />
                       </yui:column>
                    </yui:datatable>
         </div>
            
       </body>     
    </html>
</f:view>


