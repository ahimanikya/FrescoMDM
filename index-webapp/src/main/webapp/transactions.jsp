<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>

<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionIterator" %>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSearchObject"%>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSummary"  %>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>

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
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">     
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/balloontip.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
      </head>
    
    <title><h:outputText value="#{msgs.application_heading}"/></title> 
    <%@include file="./templates/header.jsp"%>
    <body>
        <div id="mainContent">     
            <div id ="transactions " class="basicSearch">
                <h:form   id="AuditSearchForm" >
                    <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />                        
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>                            
                                <table border="0" cellpadding="0" cellspacing="0">                                
                                    <tr> 
                                        <td>
                                            <nobr><h:outputLabel  for="#{msgs.source_edit_fromDatePrompt}" value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                        </td>
                                        <td align="left">  
                                           <nobr>                                            
                                            <h:inputText 
                                                id="createStDateField"
                                                label="#{TransactionHandler.createStartDate}"
                                                value="#{TransactionHandler.createStartDate}"
                                                onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                onkeyup="javascript:qws_field_on_key_up(this)" 
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
                                        
                                        <td>
                                            <nobr><h:outputLabel  for="#{msgs.source_edit_toDatePrompt}"  value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                        </td>
                                        <td align="left">
                                            <nobr>
                                                <h:inputText 
                                                    id="createEndDateField"
                                                    label="#{TransactionHandler.createEndDate}" 
                                                    value="#{TransactionHandler.createEndDate}" 
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
                                            <nobr><h:outputLabel  for="#{msgs.source_edit_fromTimePrompt}"  value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                        </td>
                                        <td align="left">    
                                            <h:inputText  
                                                id="createStartTimeField"
                                                label="#{TransactionHandler.createStartTime}" 
                                                value="#{TransactionHandler.createStartTime}" 
                                                onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                size="12"
                                                maxlength="8"/>
                                            
                                        </td>
                                        <td>
                                            <nobr><h:outputLabel  for="#{msgs.source_edit_toTimePrompt}"  value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                        </td>
                                        <td align="left">    
                                            <h:inputText 
                                                id="createEndTimeField"
                                                label="#{TransactionHandler.createEndTime}"
                                                value="#{TransactionHandler.createEndTime}" 
                                                onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                size="12"
                                                maxlength="8"/>
                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><h:outputLabel for="#{msgs.transaction_source}" value="#{msgs.transaction_source}"/></td>
                                        <td>

                                                              
                                            <h:selectOneMenu  id="sourceOption" label="#{TransactionHandler.source}" value="#{TransactionHandler.source}">
                                                 <f:selectItem itemLabel="" itemValue="" />
                                                 <f:selectItems  value="#{TransactionHandler.selectOptions}" />
                                            </h:selectOneMenu>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    
                                    <tr>
                                        <td><h:outputLabel for="#{msgs.transaction_localid}" value="#{msgs.transaction_localid}"/></td>
                                        <td>
                                            <h:inputText  id="AuditSearchFormLID" 
                                                          label="#{TransactionHandler.localid}" 
                                                          value="#{TransactionHandler.localid}" 
                                                          onkeydown="javascript:qws_field_on_key_down(this, document.AuditSearchForm.lidmask.value)"
                                                          onkeyup="javascript:qws_field_on_key_up(this)"
                                                          size="12"  maxlength="12"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><h:outputLabel for="#{msgs.transaction_euid}" value="#{msgs.transaction_euid}"/></td>     
                                        <td> <h:inputText id="euid"  label="#{TransactionHandler.euid}" value="#{TransactionHandler.euid}" size="12" maxlength="10"/></td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><h:outputLabel for="#{msgs.transaction_system_user}" value="#{msgs.transaction_system_user}"/></td>     
                                        <td> <h:inputText id="user" size="12" label="#{TransactionHandler.systemuser}" value="#{TransactionHandler.systemuser}"/></td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>                            
                                    <tr>
                                        <td><h:outputLabel for="#{msgs.transaction_function}"  value="#{msgs.transaction_function}"/></td>
                                        <td>
                                            <h:selectOneMenu  styleClass="selectContent" id="function" label="#{TransactionHandler.function}" value="#{TransactionHandler.function}" >
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
                                            <a class="button" href="javascript:ClearContents('AuditSearchForm')">
                                                <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                                            </a>
                                            <h:commandLink  id="submitSearch" 
                                                            styleClass="button"  
                                                            rendered="#{Operations.transLog_SearchView}" 
                                                            action="#{TransactionHandler.performTransactionSearch}">  
                                                <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                            </h:commandLink>
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
        
         <div class="printClass">
             <h:panelGrid rendered="#{TransactionHandler.searchSize gt 0}" columns="3">
                                <h:outputText  rendered="#{TransactionHandler.searchSize gt -1}" value="#{msgs.total_records_text}"/>
                                <h:outputText rendered="#{TransactionHandler.searchSize gt -1}" value="#{TransactionHandler.searchSize}"/>
                                <h:outputLink value="javascript:window.print()" styleClass="button" rendered="#{Operations.transLog_Print && TransactionHandler.searchSize gt 0}">
                                    <span><h:outputText value="#{msgs.print_text}"/></span>
                                </h:outputLink>
            </h:panelGrid>    
        </div>
         <div class="reportYUISearch" >
            <yui:datatable var="transactions" value="#{TransactionHandler.transactionsVO}" rendered="#{TransactionHandler.searchSize gt 0}"
                           paginator="true" 
                           rows="50"                
                           rowClasses="odd,even"
                           width="1024px">
                               
                <yui:column sortable="true" resizeable="true" rendered="#{transactions.eoArrayListSize gt 0}">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_transactionid_text}" />
                    </f:facet>
                    <a href='transeuiddetails.jsf?transactionId=<h:outputText value="#{transactions.transactionId}"/>'>
                     <h:outputText value="#{transactions.transactionId}" />
                   </a>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_euid_text}" />
                    </f:facet>
                        <h:outputText value="#{transactions.euid}" />
                    
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.firstName}"/>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.lastName}"/>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_source_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.source}"/>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText  value="#{msgs.datatable_localid_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.localid}"/>
                </yui:column>
                
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_function_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.function}"/>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_system_user_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.systemUser}"/>
                </yui:column>
                
                <yui:column sortable="true" resizeable="true">
                    <f:facet name="header">
                        <h:outputText value="#{msgs.datatable_createdate_text}" />
                    </f:facet>
                    <h:outputText value="#{transactions.transactionDate}"/>
                </yui:column>
            </yui:datatable>
        </div>
        <div id="InvalidTransaction" class="balloonstyle"><h:outputText  value="#{msgs.invalid_transaction}"/></div>
   </body>   
   </html>
</f:view>