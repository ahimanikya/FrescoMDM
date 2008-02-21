<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchCriteria"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchOptions"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathAPI"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
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
        <div id="advancedSearch" class="basicSearch" style="visibility:visible;display:block">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{PatientDetailsHandler.searchType}" valueChangeListener="#{PatientDetailsHandler.changeSearchType}" >
                                        <f:selectItems  value="#{PatientDetailsHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{PatientDetailsHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td>
                            <input id='lidmask' type='hidden' name='lidmask' value='' />
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{PatientDetailsHandler.screenConfigArray}">
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
                                        <h:selectOneMenu value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                         onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                         rendered="#{feildConfig.name ne 'SystemCode'}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                        
                                        <h:selectOneMenu  onchange="javascript:setLidMaskValue(this)"
                                                          onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                          id="SystemCode" 
                                                          value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}" 
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
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                       value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{feildConfig.maxLength}" 
                                                       rendered="#{feildConfig.name ne 'LID'}"/>
                                        
                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                       rendered="#{feildConfig.name eq 'LID'}"/>
                                                       
                                    </nobr>
                                </h:column>
                                
                                <!--Rendering Updateable HTML Text Area-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                    <nobr>
                                        <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.range  && feildConfig.displayName eq 'DOB From'}" >
                                    <nobr>
                                        <h:inputText id="DOBFrom" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}"
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
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.range  && feildConfig.displayName eq 'DOB To'}" >
                                    <nobr>
                                        <h:inputText id="DOBTo" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var dateTo =  getDateFieldName('advancedformData','DOBTo');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,dateTo)" > 
                                            <h:graphicImage  id="calImgDateTo" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'StartDate'}" >
                                    <nobr>
                                    <h:inputText id="StartDate" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                 required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                 onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                    <script> var startdate = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                    <a HREF="javascript:void(0);" 
                                       onclick="g_Calendar.show(event,startdate)" > 
                                        <h:graphicImage  id="calImgStartDate" 
                                                         alt="calendar Image" styleClass="imgClass"
                                                         url="./images/cal.gif"/>               
                                    </a>
                                    <nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'EndDate'}" >
                                    <nobr>
                                        <h:inputText id="EndDate" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var EndDate = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,EndDate)" > 
                                            <h:graphicImage  id="calImgEndDate" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.name eq 'create_start_date'}" >
                                    <nobr>
                                    <h:inputText id="create_start_date" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                 required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                 onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                    <script> var create_start_date = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                   <a HREF="javascript:void(0);" 
                                       onclick="g_Calendar.show(event,create_start_date)" > 
                                        <h:graphicImage  id="calImgcreate_start_date" 
                                                         alt="calendar Image" styleClass="imgClass"
                                                         url="./images/cal.gif"/>               
                                    </a>
                                    <nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.name eq 'create_end_date'}" >
                                    <nobr>
                                        <h:inputText id="create_end_date" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var create_end_date = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,create_end_date)" > 
                                            <h:graphicImage  id="calImgcreate_end_date" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'DOB'}" >
                                    <nobr>
                                        <h:inputText id="DOB" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var DOB = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,DOB)" > 
                                            <h:graphicImage  id="calImgDOB" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Dod'}" >
                                    <nobr>
                                        <h:inputText id="Dod" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Dod = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Dod)" > 
                                            <h:graphicImage  id="calImgDod" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Date1'}" >
                                    <nobr>
                                        <h:inputText id="Date1" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Date1 = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Date1)" > 
                                            <h:graphicImage  id="calImgDate1" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Date2'}" >
                                    <nobr>
                                        <h:inputText id="Date2" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Date2 = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Date2)" > 
                                            <h:graphicImage  id="calImgDate2" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Date3'}" >
                                    <nobr>
                                        <h:inputText id="Date3" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Date3 = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Date3)" > 
                                            <h:graphicImage  id="calImgDate3" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Date4'}" >
                                    <nobr>
                                        <h:inputText id="Date4" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Date4 = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Date4)" > 
                                            <h:graphicImage  id="calImgDate4" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'Date5'}" >
                                    <nobr>
                                        <h:inputText id="Date5" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var Date5 = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,Date5)" > 
                                            <h:graphicImage  id="calImgDate5" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'PensionExpDate'}" >
                                    <nobr>
                                        <h:inputText id="PensionExpDate" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var PensionExpDate = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,PensionExpDate)" > 
                                            <h:graphicImage  id="calImgPensionExpDate" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'DummyDate'}" >
                                    <nobr>
                                        <h:inputText id="DummyDate" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var DummyDate = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,DummyDate)" > 
                                            <h:graphicImage  id="calImgDummyDate" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && !feildConfig.range  && feildConfig.name eq 'EnterDate'}" >
                                    <nobr>
                                        <h:inputText id="EnterDate" label="#{feildConfig.displayName}"    value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var EnterDate = getDateFieldName('advancedformData','<h:outputText value="#{feildConfig.name }" />');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,EnterDate)" > 
                                            <h:graphicImage  id="calImgEnterDate" 
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
                                            <h:commandLink  styleClass="button" rendered="#{Operations.assumedMatch_SearchView}"  action="#{PatientDetailsHandler.performSubmit}" >  
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
                <h:inputHidden id="enteredFieldValues" value="#{PatientDetailsHandler.enteredFieldValues}"/>
            </h:form>
        </div>  
        <br>    
                
        <%
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");


        %>    
          
    <%
          ArrayList resultArrayList  = new ArrayList();
          if(request.getAttribute("resultArrayListReq") != null) {
             request.setAttribute("resultArrayListReq", request.getAttribute("resultArrayListReq") );  
             resultArrayList = (ArrayList) request.getAttribute("resultArrayListReq"); 
             if (resultArrayList != null && resultArrayList.size() > 0) {
         %>                
         <div class="printClass">
             <table cellpadding="0" cellspacing="0" border="0" align="right">
                 <tr>
                     <td>
                         <h:outputText value="#{msgs.total_records_text}"/><%=resultArrayList.size()%>&nbsp;&nbsp;
                     </td>
                     <td>
                         <h:outputLink styleClass="button" 
                                       rendered="#{Operations.EO_PrintSBR }"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>              
                         
                     </td>
                 </tr>
             </table>
             <table align="left">
                 <tr>
                     <td align="right">
                         <h:form id="yuiform">
                             <h:commandLink  styleClass="button" 
                                             rendered="#{Operations.EO_Compare}"  
                                             action="#{PatientDetailsHandler.buildCompareEuids}" >  
                                 <span>
                                     <h:outputText value="#{msgs.dashboard_compare_tab_button}"/>
                                 </span>
                             </h:commandLink>                                     
                             <h:inputHidden id="compareEuids" value="#{PatientDetailsHandler.compareEuids}"/>
                         </h:form> 
                         
                     </td>
                 </tr>
             </table>                     
         </div>
         <%}%>
                
                <%
              if (resultArrayList != null && resultArrayList.size() == 0) {
           %>
                <div class="printClass">
                <table>
                  <tr><td>No Records Found in the System. Please Refine your Search Criteria..............</td></tr>
                </table>                     
         </div>
      <%}%>
<%}%>
    
<div id="reportYUISearch" class="reportYUISearch">
    <yui:datatable var="patientDetails" value="#{PatientDetailsHandler.patientDetailsVO}"
                   paginator="true"  
                   rows="50"     
                   rowClasses="even,odd"
                   rendered="#{PatientDetailsHandler.resultsSize gt 0}"
                   width="1024px">  
                

                   
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_euid_text}" />
            </f:facet>
            <input type="checkbox" onclick="javascript:getEUIDS('<h:outputText value="#{patientDetails.euid}" />')"/>
            <a id="euid" href='euiddetails.jsf?euid=<h:outputText value="#{patientDetails.euid}" />'>
                <h:outputText value="#{patientDetails.euid}" />
            </a>
        </yui:column>
        
        
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_firstname_text}"  />
            </f:facet>
            <h:outputText value="#{patientDetails.firstName}" />
        </yui:column>
        
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_lastname_text}"  />
            </f:facet>
            <h:outputText value="#{patientDetails.lastName}" />
        </yui:column>
        
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_ssn_text}"  />
            </f:facet>
            <h:outputText value="#{patientDetails.ssn}" />
        </yui:column>
        
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_DOB_text}"  />
            </f:facet>
            <h:outputText value="#{patientDetails.dob}" />
        </yui:column>
        
        <yui:column sortable="true" resizeable="true">
            <f:facet name="header">
                <h:outputText value="#{msgs.datatable_addressline1_text}"  />
            </f:facet>
            <h:outputText value="#{patientDetails.addressLine1}" />
        </yui:column>
        
   </yui:datatable>
</div>                   
</div>
</body>
        <%
          PatientDetailsHandler  patientDetailsHandler = new PatientDetailsHandler();
          String[][] lidMaskingArray = patientDetailsHandler.getAllSystemCodes();
          
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
    </script>
     


</html>
</f:view>
