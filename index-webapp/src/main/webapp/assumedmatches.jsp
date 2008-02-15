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
                    <h:form id="advancedformData">
                        <table border="0" cellpadding="0" cellspacing="0" align="right">
                            <tr>
                                <td>
                                    <h:outputText rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu  rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{AssumeMatchHandler.searchType}" valueChangeListener="#{AssumeMatchHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AssumeMatchHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                        </table>
                        <table border="0" cellpadding="0" cellspacing="0" >
                            <tr>
                                <td>
                                    <input id='lidmask' type='hidden' name='lidmask' value='<h:outputText value="#{AssumeMatchHandler.lidMask}"/>' />
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
                                                             rendered="#{feildConfig.name ne 'SystemCode'}">
                                                <f:selectItem itemLabel="" itemValue="" />
                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                            </h:selectOneMenu>
                                            
                                            <h:selectOneMenu  onchange="submit();" 
                                                              id="sourceOption" 
                                                              value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}" 
                                                              valueChangeListener="#{AssumeMatchHandler.setLidMaskValue}"
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
                                                           value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                           maxlength="#{feildConfig.maxLength}" 
                                                           rendered="#{feildConfig.name ne 'LID'}"/>
                                            
                                            <h:inputText   required="#{feildConfig.required}" 
                                                           label="#{feildConfig.displayName}" 
                                                           onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                           onkeyup="javascript:qws_field_on_key_up(this)"
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
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.range  && feildConfig.displayName eq 'DOB From'}" >
                                          <nobr>
                                            <h:inputText id="DOBFrom" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.displayName]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
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
                                            <h:inputText id="DOBTo" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.displayName]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="StartDate" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
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
                                            <h:inputText id="EndDate" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="create_start_date" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="create_end_date" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="DOB" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="Dod" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
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
                                            <h:inputText id="Date1" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="Date2" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="Date3" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="Date4" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="Date5" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="PensionExpDate" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="DummyDate" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
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
                                            <h:inputText id="EnterDate" label="#{feildConfig.displayName}"    value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                         required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
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