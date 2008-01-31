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
            <h:form id="advancedformData">
             <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
             <input type="hidden" name="ssnmask" value="DDD-DDD-DDD" />
             <table align="right">
                 <tr>
                 <td><h:outputText value="#{msgs.patdet_search_text}"/></td>
                 <td>
                     <h:selectOneMenu  onchange="submit();" style="width:220px;" value="#{PatientDetailsHandler.searchType}" valueChangeListener="#{PatientDetailsHandler.changeSearchType}" >
                         <f:selectItems  value="#{PatientDetailsHandler.possilbeSearchTypes}" />
                     </h:selectOneMenu>
                 </td>
                 <tr>
             </table>
             <br>             
               <h:dataTable id="advScreenConfigId" value="#{PatientDetailsHandler.searchScreenConfigArray}" var="screenConfig" >
                    <h:column rendered="#{screenConfig.screenTitle eq PatientDetailsHandler.searchType}">
                        <h:column>
                                <h:outputText style="color:blue;padding-left:40px;" value="#{screenConfig.instruction}" />
                        </h:column>
                        <h:dataTable id="fieldConfigGrpId" var="feildConfigGrp" value="#{screenConfig.fieldConfigs}" >
                            <h:column>
                            <h:column>
                                <h:outputText style="color:blue;padding-left:20px;" value="#{feildConfigGrp.description}" />
                            </h:column>
                                <h:dataTable id="basicFieldConfigId" var="feildConfig" headerClass="tablehead" columnClasses="columnclass" value="#{feildConfigGrp.fieldConfigs}">
                                                <!--Rendering Non Updateable HTML Text Area-->
                                                <h:column>
                                                    <h:outputText value="#{feildConfig.displayName}"/>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6}" >
                                                    <h:inputText id="fieldConfigIdText" 
                                                                 value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                                 required="#{feildConfig.required}" 
                                                                 rendered="#{feildConfig.name ne 'SSN'}"/>

                                                    <h:inputText   id="SSN"   required="#{feildConfig.required}"
                                                                          onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.ssnmask.value)"
                                                                          onkeyup="javascript:qws_field_on_key_up(this)"
                                                                          value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                                          rendered="#{feildConfig.name eq 'SSN'}" size="12" maxlength="12"/>
                                                                          
                                                </h:column>
                                                  
                                                <!--Rendering Updateable Date -->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.displayName eq 'DOB From'  }" >
                                                 <nobr> <h:inputText id="DOBFROM" value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}" required="#{feildConfig.required}"
                                                                              onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')" 
                                                                              onkeyup="javascript:qws_field_on_key_up(this)"
                                                                              onblur="javascript:validate_date(this,MM/dd/yyyy')"size="12" maxlength="10"/>
                                                             
                                                      <script> var DOB = getDateFieldName('advancedformData',':DOBFROM');</script>
                                                     <a HREF="javascript:void(0);"
                                                     onclick="g_Calendar.show(event,DOB)">
                                                        <h:graphicImage  id="calImgDOBFROM" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </nobr>
                                                </h:column>
                                                
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 && feildConfig.displayName eq 'DOB To'  }" >
                                                 <nobr>   <h:inputText id="DOBTO" value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}" required="#{feildConfig.required}"
                                                                              onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')" 
                                                                              onkeyup="javascript:qws_field_on_key_up(this)"
                                                                              onblur="javascript:validate_date(this,MM/dd/yyyy')"size="12" maxlength="10" />
                                               
                                                    <script> var DOBto = getDateFieldName('advancedformData',':DOBTO'); </script>
                                                    <a HREF="javascript:void(0);" 
                                                    onclick="g_Calendar.show(event, DOBto)" > 
                                                        <h:graphicImage  id="calImgDOBTO" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                    </nobr>
                                                 </h:column>
                                                
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea' && feildConfig.valueType ne 6}" >
                                                    <h:inputTextarea id="fieldConfigIdTextArea"   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'MenuList'}" >
                                                    <h:selectOneMenu value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}">
                                                        <f:selectItem itemLabel="" itemValue="Default" />
                                                        <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'MenuList'}" >
                                                    <h:selectOneMenu value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}">
                                                        <f:selectItem itemLabel="" itemValue="Default" />
                                                        <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Non Updateable HTML Text boxes-->
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                                        <h:inputText id="SystemCode" value="#{PatientDetailsHandler.SystemCode}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <h:column  rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                                    <h:inputText id="EUID" value="#{PatientDetailsHandler.EUID}" required="#{feildConfig.required}" size="14" maxlength="10"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                                    <h:inputText id="LID" value="#{PatientDetailsHandler.LID}" required="#{feildConfig.required}"
                                                                          onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                                          onkeyup="javascript:qws_field_on_key_up(this)" size="12" maxlength="10"/>
                                                </h:column>
                                            
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                                    <h:inputText value="#{PatientDetailsHandler.create_start_date}" id="create_start_date"
                                                                 required="#{feildConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')" 
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" size="12" maxlength="10"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'basicformData:basicformData:basicScreenConfigId:0:fieldConfigGrpId:0:basicFieldConfigId:0:create_start_date')" > 
                                                        <h:graphicImage  id="calImgStartDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                                    <h:inputText value="#{PatientDetailsHandler.create_end_date}" id="create_end_date"
                                                                 required="#{feildConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')" 
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" size="12" maxlength="10"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'basicformData:basicScreenConfigId:0:fieldConfigGrpId:0:basicFieldConfigId:1:create_end_date')" > 
                                                        <h:graphicImage  id="calImgEndDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                                    <h:inputText rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                                 value="#{PatientDetailsHandler.create_start_time}" required="#{feildConfig.required}"
                                                                                                                    onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                                                                    onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                                                    size="12" maxlength="8"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                                    <h:inputText id="create_end_time" value="#{PatientDetailsHandler.create_end_time}" 
                                                                 required="#{feildConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" size="12" maxlength="8"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                                    <h:inputText id="Status"  value="#{PatientDetailsHandler.Status}" required="#{feildConfig.required}"/>
                                                </h:column>
                                </h:dataTable>
                            </h:column>
                        </h:dataTable>
                        <h:column footerClass="footerclass">
                            <br>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        
                                            <a class="button" href="javascript:ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                            </a>  
                                    </td>
                                    <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
                                    <td>
                                        
                                            <h:commandLink  styleClass="button" 
                                                            action="#{PatientDetailsHandler.performSubmit}" rendered="#{Operations.EO_SearchViewSBR}"
                                                            actionListener="#{PatientDetailsHandler.setSearchTypeField}">  
                                                <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                <f:attribute name="searchType" value="#{screenConfig.screenTitle}"/>   
                                            </h:commandLink>                                     
                                        
                                    </td>
                                </tr>
                            </table>
                        </h:column>
                    </h:column>
               </h:dataTable>
            </h:form>
                 <h:messages  styleClass="errorMessages"  layout="table" />
        </div>  
        <br>    
                
        <%
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");


        %>    
          
    <%
         ArrayList resultArrayList = (ArrayList) session.getAttribute("resultArrayList"); 
         ArrayList resultArrayListReq = (ArrayList) request.getAttribute("resultArrayListReq"); 
         if (resultArrayList != null) {
         //if (resultArrayListReq != null) {
      
       %>
                                       
  
<%  }  //check if iterator is in session%>

          <%

           if (resultArrayList != null && resultArrayList.size() > 0) {
         %>                
                <div class="printClass">
                          <table cellpadding="0" cellspacing="0" border="0">
                              <tr>
                                  <td>
                                      <nobr>
                                          <h:outputText value="#{msgs.total_records_text}"/><%=resultArrayList.size()%>
                                          <h:outputLink styleClass="button" 
                                                        rendered="#{Operations.EO_PrintSBR }"  
                                                        value="JavaScript:window.print();">
                                              <span><h:outputText value="#{msgs.print_text}"/>  </span>
                                          </h:outputLink>              
                                      </nobr>
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
    
<div class="reportYUISearch">
    
<h:form id="yuiform">
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
   </h:form>
</div>                   
</div>
</body>
</html>
</f:view>
