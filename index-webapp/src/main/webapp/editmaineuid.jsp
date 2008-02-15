<%-- 
    Document   : editmaineuid
    Created on : Jan 4, 2008, 3:42:00 PM
    Author     : Rajani Kanth M
                 www.ligaturesoftware.com
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.EditMainEuidHandler"  %>


<f:view>
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            
            <title>Master Indexed Web Application </title> 
            
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
            
            <!-- Additional source files go here -->
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <script type="text/javascript" src="scripts/newdateformat1.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
        
            
            <!--there is no custom header content for this example-->
    <style type="text/css">
        .squarecontainerOriginal { 
            width: 450px;
            overflow:auto;
        }

       .squarecontainer { 
            overflow:auto;
        }
        .squares {
            float: left;
            width: 5em;
            height: 5em;
            margin: .5em;
            border: 1px solid black;
        }
        
    </style>
        
        </head>
        <%@include file="./templates/header.jsp"%>
        <%
            EditMainEuidHandler editMainEuidHandler = (EditMainEuidHandler) session.getAttribute("EditMainEuidHandler");
            EnterpriseObject editEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            ValueExpression eoValueExpression = ExpressionFactory.newInstance().createValueExpression(editEnterpriseObject, editEnterpriseObject.getClass());

            int addressSize;
            int phoneSize;
            int aliasSize;
            int auxIdSize;
            int commentSize;
        %>
        
        <body class="yui-skin-sam">
            <div id="mainContent" style="overflow:hidden;"> 
                <div id="demo" class="yui-navset">
                    <div class="yui-content">
                        <h:form id="basicAddformData">
                            <table>
                                <tr>
                                    <td valign="top" align="left" width="50%">
                                        <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tablehead" align="left" colspan="2">
                                        <h:outputText value="Edit Main EUID" /> <%=editEnterpriseObject.getEUID()%> &nbsp;&nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" colspan="2">
                                        <h:commandLink  styleClass="button" 
                                                        actionListener="#{EditMainEuidHandler.toUpdatedEuidDetails}"
                                                        action="#{NavigationHandler.toEuidDetails}">
                                            <span><h:outputText value="Back" /></span>
                                        </h:commandLink>               
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <!-- Start Main Euid Details-->
                                        <div id="addTab">
                                            <!-- Start EDIT Fields-->
                                            <table border="0" width="100%">
                                                <tr>
                                                    <td class="tablehead" align="right"  colspan="2">
                                                        <h:outputText value="Main EUID"/>                                                
                                                    </td>
                                                </tr>
                                            </table>
                                            <!--Start Displaying the person fields -->                                        
                                             <h:dataTable  headerClass="tablehead"                                        
                                                           width="100%"
                                                           rowClasses="odd,even"                                     
                                                           id="hashIdEdit" 
                                                           var="fieldConfigPer" 
                                                           value="#{SourceHandler.personFieldConfigs}">                                                    
                                                 <h:column>
                                                     <h:outputText value="#{fieldConfigPer.displayName}"  />
                                                     <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                 </h:column>                                                        
                                                 <h:column>
                                                     <div id='linkSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                     <h:outputLink  rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"   
                                                                    value="javascript:void(0)" >
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] &&!EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id='linkSourceDivData:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style='visibility:hidden;display:none;'>
                                                     <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                 </h:column>                                                        
                                                 <h:column>
                                                     <div id="unlockSourceDiv">
                                                     <h:outputLink  rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="lock" styleClass="imgClass"
                                                                             url="./images/unlock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id="lockSourceDiv">
                                                     <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="lock" styleClass="imgClass"
                                                                             url="./images/lock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                 </h:column>                                                        
                                                 <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                         disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                         readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }">
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                     disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                     readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                     />
                                                    </h:column>
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'DOB'}">
                                                    <nobr>     
                                                        <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                     disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                     readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                     id="DOBEO"
                                                                     required="#{fieldConfigPer.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                        <script> var DOB = getDateFieldName('basicAddformData',':DOBEO');</script>                                                                             
                                                        <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,DOB)" > 
                                                            <h:graphicImage  id="calImgDobDate" 
                                                                             alt="calendar Image" styleClass="imgClass"
                                                                             url="./images/cal.gif"/>               
                                                        </a>
                                                    </nobr>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Dod'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="DODEO"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 required="#{fieldConfigPer.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var DOD = getDateFieldName('basicAddformData',':DODEO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,DOD)" > 
                                                        <h:graphicImage  id="calImgStartDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'PensionExpDate'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="PensionExpDateEO"
                                                                 required="#{fieldConfigPer.required}"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditPensionDate = getDateFieldName('basicAddformData',':PensionExpDateEO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditPensionDate)" > 
                                                        <h:graphicImage  id="calImgEditPensionExpDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'DummyDate'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="DummyDateEO"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 required="#{fieldConfigPer.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDummyDate = getDateFieldName('basicAddformData',':DummyDateEO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDummyDate)" > 
                                                        <h:graphicImage  id="calImgEditDummyDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date1'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="Date1EO"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 required="#{fieldConfigPer.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDate1 = getDateFieldName('basicAddformData',':Date1EO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDate1)" > 
                                                        <h:graphicImage  id="calImgEditDate1" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date2'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="Date2EO"
                                                                 required="#{fieldConfigPer.required}"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDate2 = getDateFieldName('basicAddformData',':Date2EO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDate2)" > 
                                                        <h:graphicImage  id="calImgEditDate2" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date3'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 id="Date3EO"
                                                                 required="#{fieldConfigPer.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDate3 = getDateFieldName('basicAddformData',':Date3EO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDate3)" > 
                                                        <h:graphicImage  id="calImgEditDate3" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date4'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="Date4EO"
                                                                 required="#{fieldConfigPer.required}"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDate4 = getDateFieldName('basicAddformData',':Date4EO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDate4)" > 
                                                        <h:graphicImage  id="calImgEditDate4" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date5'}">
                                                    <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                 value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                 id="Date5EO"
                                                                 required="#{fieldConfigPer.required}"
                                                                 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                 readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                    <script> var EditDate5 = getDateFieldName('basicAddformData',':Date5EO');</script>                                                                             
                                                    <a HREF="javascript:void(0);" 
                                                       onclick="g_Calendar.show(event,EditDate5)" > 
                                                        <h:graphicImage  id="calImgEditDate5" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                    <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextArea"   
                                                                     disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"
                                                                     readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"                                                                     
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"/>
                                                </h:column>
                                            </h:dataTable>    
                                            <!--End Displaying the person fields -->    
                                            <table border="0" width="100%">
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <h:outputText value="Address"/>                                                
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" align="right">
                                                        <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAddressEODiv',event)">
                                                            <span><h:outputText value="Add Addres"/></span>
                                                        </h:outputLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            <!-- Start Display address fields --> 
                                            <h:dataTable  headerClass="tablehead" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="adfieldConfigId" 
                                              var="adressMapArrayList" 
                                              value="#{EditMainEuidHandler.singleAddressHashMapArrayList}">
                                              <f:facet name="footer">
                                                    <h:column>
                                                        <%
            addressSize = editMainEuidHandler.getSingleAddressHashMapArrayList().size();
                                                        %>
                                                        <% if (addressSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                               <h:column>
                                                 <h:dataTable 
                                                  rowClasses="odd,even"                                     
                                                  id="addressHashId" 
                                                  var="addressFieldConfig" 
                                                  value="#{SourceHandler.addressFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                 required="#{addressFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{addressFieldConfig.displayName}"   
                                                                 value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{addressFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                            <a HREF="javascript:void(0);" 
                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </a>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                            <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                             required="#{addressFieldConfig.required}"/>
                                                        </h:column>
                                                        <f:facet name="footer">
                                                            <h:column>
                                                                <h:commandLink  styleClass="button" 
                                                                                actionListener="#{EditMainEuidHandler.removeEOAddress}" >  
                                                                    <f:attribute name="remAddressMap" value="#{adressMapArrayList}"/>
                                                                    <span><h:outputText value="Delete Address"/></span>
                                                                </h:commandLink>                                     
                                                            </h:column>
                                                        </f:facet>
                                                    </h:dataTable>               
                                                </h:column>
                                            </h:dataTable>                                                             
                                            <!-- End Display address fields --> 
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            
                                            <!-- Start Display Phone fields --> 
                                            <table border="0" width="100%">
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <h:outputText value="Phone"/>                                                
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" align="right">
                                                        <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraPhoneEODiv',event)">
                                                            <span><h:outputText value="Add Phone"/></span>
                                                        </h:outputLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            <h:dataTable width="100%"
                                                         rowClasses="odd,even"                                     
                                                         id="phfieldConfigId" 
                                                         var="phoneMapArrayList" 
                                                         value="#{EditMainEuidHandler.singlePhoneHashMapArrayList}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            phoneSize = editMainEuidHandler.getSinglePhoneHashMapArrayList().size();
                                                        %>
                                                        <% if (phoneSize == 0) {%>
                                                        <h:outputText  value="No details"/>
                                                        <%}%>
                                                    </h:column>
                                                </f:facet>
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  id="phoneHashId" 
                                                                  var="phoneFieldConfig" 
                                                                  value="#{SourceHandler.phoneFieldConfigs}">
                                                        <h:column>
                                                            <h:outputText value="#{phoneFieldConfig.displayName}"  />
                                                            <h:outputText value="*" rendered="#{phoneFieldConfig.required}" />
                                                        </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{phoneFieldConfig.guiType eq 'MenuList' &&  phoneFieldConfig.valueType ne 6}" >
                                                            <h:selectOneMenu value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{phoneFieldConfig.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType ne 6}" >
                                                            <h:inputText label="#{phoneFieldConfig.displayName}"  
                                                                         id="fieldConfigIdTextbox"   
                                                                         value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                         required="#{phoneFieldConfig.required}"/>
                                                        </h:column>
                                                        
                                                        
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType eq 6}">
                                                            <h:inputText label="#{phoneFieldConfig.displayName}"   
                                                                         value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}"  
                                                                         id="date"
                                                                         required="#{phoneFieldConfig.required}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                                            <a HREF="javascript:void(0);" 
                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </a>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{phoneFieldConfig.guiType eq 'TextArea' &&  phoneFieldConfig.valueType ne 6}" >
                                                            <h:inputTextarea label="#{phoneFieldConfig.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                             required="#{phoneFieldConfig.required}"/>
                                                        </h:column>
                                                        <f:facet name="footer">
                                                            <h:column>
                                                                <h:commandLink  styleClass="button" 
                                                                                actionListener="#{EditMainEuidHandler.removeEOPhone}" >  
                                                                    <f:attribute name="remPhoneMap" value="#{phoneMapArrayList}"/>
                                                                    <span><h:outputText value="Delete Phone"/></span>
                                                                </h:commandLink>                                     
                                                            </h:column>
                                                        </f:facet>
                                                        
                                                    </h:dataTable>               
                                                </h:column>
                                            </h:dataTable>                                                             
                                            <!-- End Display Phone fields --> 
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            
                                            <!-- Start Display Alias fields --> 
                                            <table border="0" width="100%">
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <h:outputText value="Alias"/>                                                
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" align="right">
                                                        <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAliasEODiv',event)">
                                                            <span><h:outputText value="Add Alias"/></span>
                                                        </h:outputLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                            <h:dataTable  headerClass="tablehead" 
                                                          width="100%"
                                                          rowClasses="odd,even"                                     
                                                          id="aliasfieldConfigId" 
                                                          var="aliasMapArrayList" 
                                                          value="#{EditMainEuidHandler.singleAliasHashMapArrayList}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            aliasSize = editMainEuidHandler.getSingleAliasHashMapArrayList().size();
                                                        %>
                                                        <% if (aliasSize == 0) {%>
                                                        <h:outputText  value="No details"/>
                                                        <%}%>
                                                    </h:column>
                                                </f:facet>
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="aliasHashId" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  var="aliasFieldConfig" 
                                                                  value="#{SourceHandler.aliasFieldConfigs}">
                                                        
                                                        <h:column>
                                                            <h:outputText value="#{aliasFieldConfig.displayName}"  />
                                                            <h:outputText value="*" rendered="#{aliasFieldConfig.required}" />
                                                        </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{aliasFieldConfig.guiType eq 'MenuList' &&  aliasFieldConfig.valueType ne 6}" >
                                                            <h:selectOneMenu value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{aliasFieldConfig.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType ne 6}" >
                                                            <h:inputText label="#{aliasFieldConfig.displayName}"  
                                                                         id="fieldConfigIdTextbox"   
                                                                         value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                         required="#{aliasFieldConfig.required}"/>
                                                        </h:column>
                                                        
                                                        
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType eq 6}">
                                                            <h:inputText label="#{aliasFieldConfig.displayName}"   
                                                                         value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}"  
                                                                         id="date"
                                                                         required="#{aliasFieldConfig.required}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                                            <a HREF="javascript:void(0);" 
                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </a>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{aliasFieldConfig.guiType eq 'TextArea' &&  aliasFieldConfig.valueType ne 6}" >
                                                            <h:inputTextarea label="#{aliasFieldConfig.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                             required="#{aliasFieldConfig.required}"/>
                                                        </h:column>
                                                        <f:facet name="footer">
                                                            <h:column>
                                                                <h:commandLink  styleClass="button" 
                                                                                actionListener="#{EditMainEuidHandler.removeEOAlias}" >  
                                                                    <f:attribute name="remAliasMap" value="#{aliasMapArrayList}"/>
                                                                    <span><h:outputText value="Delete Alias"/></span>
                                                                </h:commandLink>                                     
                                                            </h:column>
                                                        </f:facet>
                                                        
                                                    </h:dataTable>               
                                                </h:column>
                                            </h:dataTable>                                                             
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            <!-- End Display Alias fields --> 

                                                        <!-- Start Display AuxId fields --> 
                                            <table border="0" width="100%">
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <h:outputText value="AuxId"/>                                                
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" align="right">
                                                        <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAuxIdEODiv',event)">
                                                            <span><h:outputText value="Add AuxId"/></span>
                                                        </h:outputLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            <h:dataTable  headerClass="tablehead" 
                                                          width="100%"
                                                          rowClasses="odd,even"                                     
                                                          id="auxIdfieldConfigId" 
                                                          var="auxIdMapArrayList" 
                                                          value="#{EditMainEuidHandler.singleAuxIdHashMapArrayList}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            auxIdSize = editMainEuidHandler.getSingleAuxIdHashMapArrayList().size();
                                                        %>
                                                        <% if (auxIdSize == 0) {%>
                                                        <h:outputText  value="No details"/>
                                                        <%}%>
                                                    </h:column>
                                                </f:facet>
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  width="100%%"
                                                                  rowClasses="odd,even"                                     
                                                                  id="auxIdHashId" 
                                                                  var="auxIdFieldConfig" 
                                                                  value="#{SourceHandler.auxIdFieldConfigs}">
                                                        <h:column>
                                                            <h:outputText value="#{auxIdFieldConfig.displayName}"  />
                                                            <h:outputText value="*" rendered="#{auxIdFieldConfig.required}" />
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextBox' &&  auxIdFieldConfig.valueType ne 6}" >
                                                            <h:inputText label="#{auxIdFieldConfig.displayName}"  
                                                                         id="fieldConfigIdTextbox"   
                                                                         value="#{auxIdMapArrayList[auxIdFieldConfig.fullFieldName]}" 
                                                                         required="#{auxIdFieldConfig.required}"/>
                                                        </h:column>
                                                        
                                                        
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextBox' &&  auxIdFieldConfig.valueType eq 6}">
                                                            <h:inputText label="#{auxIdFieldConfig.displayName}"   
                                                                         value="#{auxIdMapArrayList[auxIdFieldConfig.fullFieldName]}"  
                                                                         id="date"
                                                                         required="#{auxIdFieldConfig.required}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                                            <a HREF="javascript:void(0);" 
                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </a>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextArea' &&  auxIdFieldConfig.valueType ne 6}" >
                                                            <h:inputTextarea label="#{auxIdFieldConfig.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             value="#{auxIdMapArrayList[auxIdFieldConfig.fullFieldName]}" 
                                                                             required="#{auxIdFieldConfig.required}"/>
                                                        </h:column>
                                                        <f:facet name="footer">
                                                            <h:column>
                                                                <h:commandLink  styleClass="button" 
                                                                                actionListener="#{EditMainEuidHandler.removeEOAuxId}" >  
                                                                    <f:attribute name="remAuxIdMap" value="#{auxIdMapArrayList}"/>
                                                                    <span><h:outputText value="Delete AuxId"/></span>
                                                                </h:commandLink>                                     
                                                            </h:column>
                                                        </f:facet>
                                                        
                                                    </h:dataTable>               
                                                </h:column>
                                            </h:dataTable>                                                             
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            
                                            <!-- End Display AuxId fields --> 
                                                     <!-- Start Display Comment fields --> 

                                            <table border="0" width="100%">
                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <h:outputText value="Comment"/>                                                
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" align="right">
                                                        <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraCommentEODiv',event)">
                                                            <span><h:outputText value="Add Comment"/></span>
                                                        </h:outputLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            <h:dataTable  headerClass="tablehead" 
                                                          width="100%"
                                                          rowClasses="odd,even"                                     
                                                          id="commentfieldConfigId" 
                                                          var="commentMapArrayList" 
                                                          value="#{EditMainEuidHandler.singleCommentHashMapArrayList}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            commentSize = editMainEuidHandler.getSingleCommentHashMapArrayList().size();
                                                        %>
                                                        <% if (commentSize == 0) {%>
                                                        <h:outputText  value="No details"/>
                                                        <%}%>
                                                    </h:column>
                                                </f:facet>
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="commentHashId" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  var="commentFieldConfig" 
                                                                  value="#{SourceHandler.commentFieldConfigs}">
                                                        <h:column>
                                                            <h:outputText value="#{commentFieldConfig.displayName}"  />
                                                            <h:outputText value="*" rendered="#{commentFieldConfig.required}" />
                                                        </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{commentFieldConfig.guiType eq 'MenuList' &&  commentFieldConfig.valueType ne 6}" >
                                                            <h:selectOneMenu value="#{commentMapArrayList[commentFieldConfig.fullFieldName]}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{commentFieldConfig.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{commentFieldConfig.guiType eq 'TextBox' &&  commentFieldConfig.valueType ne 6}" >
                                                            <h:inputText label="#{commentFieldConfig.displayName}"  
                                                                         id="fieldConfigIdTextbox"   
                                                                         value="#{commentMapArrayList[commentFieldConfig.fullFieldName]}" 
                                                                         required="#{commentFieldConfig.required}"/>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{commentFieldConfig.guiType eq 'TextBox' &&  commentFieldConfig.valueType eq 6}">
                                                            <h:inputText label="#{commentFieldConfig.displayName}"   
                                                                         value="#{commentMapArrayList[commentFieldConfig.fullFieldName]}"  
                                                                         id="date"
                                                                         required="#{commentFieldConfig.required}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                                            <a HREF="javascript:void(0);" 
                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </a>
                                                        </h:column>
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{commentFieldConfig.guiType eq 'TextArea' &&  commentFieldConfig.valueType ne 6}" >
                                                            <h:inputTextarea label="#{commentFieldConfig.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             value="#{commentMapArrayList[commentFieldConfig.fullFieldName]}" 
                                                                             required="#{commentFieldConfig.required}"/>
                                                        </h:column>
                                                        <f:facet name="footer">
                                                            <h:column>
                                                                <h:commandLink  styleClass="button" 
                                                                                actionListener="#{EditMainEuidHandler.removeEOComment}" >  
                                                                    <f:attribute name="remCommentMap" value="#{commentMapArrayList}"/>
                                                                    <span><h:outputText value="Delete Comment"/></span>
                                                                </h:commandLink>                                     
                                                            </h:column>
                                                        </f:facet>
                                                        
                                                    </h:dataTable>               
                                                </h:column>
                                            </h:dataTable>                                                             
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toEuidDetails}"
                                                                        actionListener="#{PatientDetailsHandler.deactivateEO}">
                                                            <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                            <span><h:outputText value="Deactivate" /></span>
                                                        </h:commandLink> 
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{EditMainEuidHandler.performSubmit}">
                                                            <span><h:outputText value="Save" /></span>
                                                        </h:commandLink>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toEuidDetails}">
                                                            <span><h:outputText value="Cancel" /></span>
                                                        </h:commandLink>
                                                    </td>
                                                </tr>
                                            </table>
                                            <!-- End Display Comment fields --> 
                                        </div>
                                    </td>
                                    <!-- End Main Euid Details-->
                                    <td> &nbsp;&nbsp;</td>        
                                    <!-- Start Main Euid SO Details-->
                                    <%
           /*                         
            ArrayList eoList = editMainEuidHandler.getEoSystemObjects();
            ValueExpression eoSystemObjectsValueExpression = ExpressionFactory.newInstance().createValueExpression(eoList, eoList.getClass());
            for(int i=0;i<eoList.size();i++) {
               HashMap valueMap = (HashMap) eoList.get(i);
               ArrayList newArray = new ArrayList();
               newArray.add(valueMap);
               ValueExpression eoMapValueExpression = ExpressionFactory.newInstance().createValueExpression(newArray, newArray.getClass());
         */ 
%>
                                        <td valign="top">
                                         
                                        <h:dataTable  headerClass="tablehead"                                        
                                                      width="100%"
                                                      rowClasses="odd,even"   
                                                      id="hashIdEditEo" 
                                                      style="background-color:#efefef;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;"    
                                                      var="eoSystemObjectMap"  
                                                      value="#{EditMainEuidHandler.eoSystemObjects}">
                                               <h:column>
                                                        <table border="0" width="100%">
                                              <tr>
                                                <td class="tablehead" colspan="2">
                                                     <h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/> - <h:outputText value="#{eoSystemObjectMap['LID']}" /> 
                                                </td>
                                              </tr>
                                         </table>
	                                                <h:dataTable  headerClass="tablehead"                                        
                                                                      width="100%"
                                                                      rowClasses="odd,even"                                     
                                                                      id="hashIdEdit" 
                                                                      var="fieldConfigPer" 
                                                                      value="#{SourceHandler.personFieldConfigs}">                                                    
                                                 <h:column>
                                                      <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/>:<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'>
                                                         <h:outputLink  rendered="#{EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                        value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                             <h:graphicImage  alt="link" styleClass="imgClass"
                                                                              url="./images/link.PNG"/>          
                                                         </h:outputLink>
                                                     </div> 
                                                     <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/>:<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'
                                                          style="visibility:hidden;display:none;">
                                                         <h:outputLink  rendered="#{EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                        value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                             <h:graphicImage  alt="link" styleClass="imgClass"
                                                                              url="./images/link.PNG"/>               
                                                         </h:outputLink>
                                                     </div> 
                                                         <h:outputLink  rendered="#{!EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                        value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                             <h:graphicImage  alt="link" styleClass="imgClass"
                                                                              url="./images/link.PNG"/>               
                                                         </h:outputLink>
                                                 </h:column>                                                        
                                                                      
                                                            <!--Rendering HTML Select Menu List-->
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                                <h:selectOneMenu value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" >
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                                </h:selectOneMenu>
                                                            </h:column>
                                                            <!--Rendering Updateable HTML Text boxes-->
                                                            <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                                <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                             id="fieldConfigIdTextbox"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                             required="#{fieldConfigPer.required}"
                                                                             />
                                                            </h:column>
                                                            <!--Rendering Updateable HTML Text boxes date fields-->
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'DOB'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="DOBSO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var DOBSO = getDateFieldName('basicAddformData',':DOBSO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,DOBSO)" > 
                                                                    <h:graphicImage  id="calImgDobDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Dod'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="DODSO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var DODSO = getDateFieldName('basicAddformData',':DODSO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,DODSO)" > 
                                                                    <h:graphicImage  id="calImgStartDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'PensionExpDate'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="PensionExpDateSO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var EditPensionDateSO = getDateFieldName('basicAddformData',':PensionExpDateSO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,EditPensionDateSO)" > 
                                                                    <h:graphicImage  id="calImgEditPensionExpDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'DummyDate'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="DummyDateSO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var EditDummyDateSO = getDateFieldName('basicAddformData',':DummyDateSO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,EditDummyDateSO)" > 
                                                                    <h:graphicImage  id="calImgEditDummyDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date1'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="Date1SO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var SOEditDate1 = getDateFieldName('basicAddformData',':Date1SO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,SOEditDate1)" > 
                                                                    <h:graphicImage  id="calImgEditDate1" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date2'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="Date2SO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var SOEditDate2 = getDateFieldName('basicAddformData',':Date2SO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,SOEditDate2)" > 
                                                                    <h:graphicImage  id="calImgEditDate2" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date3'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="Date3SO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var SOEditDate3 = getDateFieldName('basicAddformData',':Date3SO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,SOEditDate3)" > 
                                                                    <h:graphicImage  id="calImgEditDate3" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date4'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="Date4SO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var SOEditDate4 = getDateFieldName('basicAddformData',':Date4SO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,SOEditDate4)" > 
                                                                    <h:graphicImage  id="calImgEditDate4" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 &&  fieldConfigPer.name eq  'Date5'}">
                                                                <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"  
                                                                             id="Date5SO"
                                                                             required="#{fieldConfigPer.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <script> var SOEditDate5 = getDateFieldName('basicAddformData',':Date5SO');</script>                                                                             
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,SOEditDate5)" > 
                                                                    <h:graphicImage  id="calImgEditDate5" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <!--Rendering Updateable HTML Text Area-->
                                                            <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                                 id="fieldConfigIdTextArea"   
                                                                                 value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                 required="#{fieldConfigPer.required}"/>
                                                            </h:column>
                                                        </h:dataTable>    
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="Address"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAddressEditDiv',event)">
                                                                        <span><h:outputText value="Add Address"/></span>
                                                                    </h:outputLink>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <!-- Start Display address fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                               width="100%"
                                               rowClasses="odd,even"                                     
                                               id="adfieldConfigId" 
                                               var="soAddressMapArrayList" 
                                               value="#{eoSystemObjectMap['SOAddressList']}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            addressSize = editMainEuidHandler.getSingleAddressHashMapArrayList().size();
            ////System.out.println("addressSize =>: " + addressSize );
%>
                                                        <% if (addressSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                                <h:column>
                                                   <h:dataTable  width="100%"
                                                  rowClasses="odd,even"                                     
                                                  id="addressHashId" 
                                                  var="addressFieldConfig" 
                                                  value="#{SourceHandler.addressFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{soAddressMapArrayList[addressFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{soAddressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                 required="#{addressFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{addressFieldConfig.displayName}"   
                                                                 value="#{soAddressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{addressFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{soAddressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                         required="#{addressFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{EditMainEuidHandler.removeEOAddress}" >  
                                                                                <f:attribute name="remAddressMap" value="#{soAddressMapArrayList}"/>
                                                                                <span><h:outputText value="Delete Address"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display address fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="Phone"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraPhoneEditDiv',event)">
                                                                        <span><h:outputText value="Add Phone"/></span>
                                                                    </h:outputLink>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <!-- Start Display phone fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="phfieldConfigId" 
                                              var="soPhoneMapArrayList" 
                                              value="#{eoSystemObjectMap['SOPhoneList']}">
                                                <f:facet name="footer">
                                                    <h:column>
                                                        <%
            phoneSize = editMainEuidHandler.getSinglePhoneHashMapArrayList().size();
            ////System.out.println("phoneSize =>: " + phoneSize);
%>
                                                        <% if (phoneSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                            <h:column>
                                                 <h:dataTable  width="100%"
                                                  rowClasses="odd,even"                                     
                                                  id="phoneHashId" 
                                                  var="phoneFieldConfig" 
                                                  value="#{SourceHandler.phoneFieldConfigs}">
                                                      
                                                <h:column>
                                                    <h:outputText value="#{phoneFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{phoneFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{phoneFieldConfig.guiType eq 'MenuList' &&  phoneFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{soPhoneMapArrayList[phoneFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{phoneFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{phoneFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{soPhoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                 required="#{phoneFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{phoneFieldConfig.displayName}"   
                                                                 value="#{soPhoneMapArrayList[phoneFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{phoneFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{phoneFieldConfig.guiType eq 'TextArea' &&  phoneFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{phoneFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{soPhoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                                         required="#{phoneFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{EditMainEuidHandler.removeEOPhone}" >  
                                                                                <f:attribute name="remPhoneMap" value="#{soPhoneMapArrayList}"/>
                                                                                <span><h:outputText value="Delete Phone"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display phone fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="Alias"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAliasEditDiv',event)">
                                                                        <span><h:outputText value="Add Alias"/></span>
                                                                    </h:outputLink>
                                                                    
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <!-- Start Display alias fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="aliasfieldConfigId" 
                                              var="soAliasMapArrayList" 
                                              value="#{eoSystemObjectMap['SOAliasList']}">
                                                 <f:facet name="footer">
                                                    <h:column>
                                                        <%
            aliasSize = editMainEuidHandler.getSingleAliasHashMapArrayList().size();
            ////System.out.println("aliasSize =>: " + aliasSize);
%>
                                                        <% if (aliasSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                            <h:column>
                                                 <h:dataTable  width="100%"
                                                  rowClasses="odd,even"                                     
                                                  id="aliasHashId" 
                                                  var="aliasFieldConfig" 
                                                  value="#{SourceHandler.aliasFieldConfigs}">
                                               <h:column>
                                                    <h:outputText value="#{aliasFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{aliasFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{aliasFieldConfig.guiType eq 'MenuList' &&  aliasFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{soAliasMapArrayList[aliasFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{aliasFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{aliasFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{soAliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                 required="#{aliasFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{aliasFieldConfig.displayName}"   
                                                                 value="#{soAliasMapArrayList[aliasFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{aliasFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{aliasFieldConfig.guiType eq 'TextArea' &&  aliasFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{aliasFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{soAliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                                         required="#{aliasFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{EditMainEuidHandler.removeEOAlias}" >  
                                                                                <f:attribute name="remAliasMap" value="#{soAliasMapArrayList}"/>
                                                                                <span><h:outputText value="Delete Alias"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display alias fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="AuxId"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAuxIdEditDiv',event)">
                                                                        <span><h:outputText value="Add AuxId"/></span>
                                                                    </h:outputLink>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <!-- Start Display auxId fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="auxIdfieldConfigId" 
                                              var="soAuxIdMapArrayList" 
                                              value="#{eoSystemObjectMap['SOAuxIdList']}">
                                                 <f:facet name="footer">
                                                    <h:column>
                                                        <%
            auxIdSize = editMainEuidHandler.getSingleAuxIdHashMapArrayList().size();
            ////System.out.println("auxIdSize =>: " + auxIdSize);
%>
                                                        <% if (auxIdSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                               <h:column>
                                                 <h:dataTable  width="100%"
                                                  rowClasses="odd,even"                                     
                                                  id="auxIdHashId" 
                                                  var="auxIdFieldConfig" 
                                                  value="#{SourceHandler.auxIdFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{auxIdFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{auxIdFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{auxIdFieldConfig.guiType eq 'MenuList' &&  auxIdFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{soAuxIdMapArrayList[auxIdFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{auxIdFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextBox' &&  auxIdFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{auxIdFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{soAuxIdMapArrayList[auxIdFieldConfig.fullFieldName]}" 
                                                                 required="#{auxIdFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextBox' &&  auxIdFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{auxIdFieldConfig.displayName}"   
                                                                 value="#{soAuxIdMapArrayList[auxIdFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{auxIdFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{auxIdFieldConfig.guiType eq 'TextArea' &&  auxIdFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{auxIdFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{soAuxIdMapArrayList[auxIdFieldConfig.fullFieldName]}" 
                                                                                         required="#{auxIdFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{EditMainEuidHandler.removeEOAuxId}" >  
                                                                                <f:attribute name="remAuxIdMap" value="#{soAuxIdMapArrayList}"/>
                                                                                <span><h:outputText value="Delete AuxId"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display auxId fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="Comment"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraCommentEditDiv',event)">
                                                                        <span><h:outputText value="Add Comment"/></span>
                                                                    </h:outputLink>
                                                                    
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <!-- Start Display comment fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="commentfieldConfigId" 
                                              var="soCommentMapArrayList" 
                                              value="#{eoSystemObjectMap['SOCommentList']}">
                                                 <f:facet name="footer">
                                                    <h:column>
                                                        <%
            commentSize = editMainEuidHandler.getSingleCommentHashMapArrayList().size();
            ////System.out.println("commentSize =>: " + commentSize);
%>
                                                        <% if (commentSize == 0) {%>
                                                          <h:outputText  value="No details"/>
                                                        <%}%>              
                                                    </h:column>
                                                </f:facet>
                                            <h:column>
                                                 <h:dataTable  width="100%"
                                                  rowClasses="odd,even"                                     
                                                  id="commentHashId" 
                                                  var="commentFieldConfig" 
                                                  value="#{SourceHandler.commentFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{commentFieldConfig.displayName}"  />
                                                    <h:outputText value="*" rendered="#{commentFieldConfig.required}" />
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                <h:column rendered="#{commentFieldConfig.guiType eq 'MenuList' &&  commentFieldConfig.valueType ne 6}" >
                                                    <h:selectOneMenu value="#{soCommentMapArrayList[commentFieldConfig.fullFieldName]}" >
                                                        <f:selectItem itemLabel="" itemValue="" />
                                                        <f:selectItems  value="#{commentFieldConfig.selectOptions}"  />
                                                    </h:selectOneMenu>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{commentFieldConfig.guiType eq 'TextBox' &&  commentFieldConfig.valueType ne 6}" >
                                                    <h:inputText label="#{commentFieldConfig.displayName}"  
                                                                 id="fieldConfigIdTextbox"   
                                                                 value="#{soCommentMapArrayList[commentFieldConfig.fullFieldName]}" 
                                                                 required="#{commentFieldConfig.required}"/>
                                                </h:column>
                                                
                                                
                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                <h:column rendered="#{commentFieldConfig.guiType eq 'TextBox' &&  commentFieldConfig.valueType eq 6}">
                                                    <h:inputText label="#{commentFieldConfig.displayName}"   
                                                                 value="#{soCommentMapArrayList[commentFieldConfig.fullFieldName]}"  
                                                                 id="date"
                                                                 required="#{commentFieldConfig.required}"
                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{commentFieldConfig.guiType eq 'TextArea' &&  commentFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{commentFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{soCommentMapArrayList[commentFieldConfig.fullFieldName]}" 
                                                                                         required="#{commentFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{EditMainEuidHandler.removeEOComment}" >  
                                                                                <f:attribute name="remCommentMap" value="#{soCommentMapArrayList}"/>
                                                                                <span><h:outputText value="Delete Comment"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display comment fields --> 
                                                        <h:commandLink  styleClass="button" 
                                                                        rendered="#{eoSystemObjectMap['Status'] eq 'active'}"
                                                                        actionListener="#{EditMainEuidHandler.deactivateEOSO}">
                                                            <f:attribute name="eoSystemObjectMapVE" value="#{eoSystemObjectMap}"/>
                                                            <span><h:outputText value="Deactivate" /></span>
                                                        </h:commandLink>                         
                                                        
                                                        <h:commandLink  styleClass="button" 
                                                                        rendered="#{eoSystemObjectMap['Status'] eq 'inactive'}"
                                                                        actionListener="#{EditMainEuidHandler.activateEOSO}">
                                                            <f:attribute name="eoSystemObjectMapVE" value="#{eoSystemObjectMap}"/>
                                                            <span><h:outputText value="Activate" /></span>
                                                        </h:commandLink>                 
                                               </h:column>
                                         </h:dataTable> 
                                         
                                    </td>
                                    <%//}%>
                                    <h:inputHidden  id="hiddenLinkFields"  value="#{EditMainEuidHandler.hiddenLinkFields}" />
                                    <h:inputHidden  id="hiddenUnLinkFields"  value="#{EditMainEuidHandler.hiddenUnLinkFields}" />
                                </h:form>
                                <!-- New SO fields start here -->
                                <h:form id="basicNewSOAddformData">
                                    <td valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td colspan="1">
                                                    <h:selectOneMenu id="systemCode" value="#{EditMainEuidHandler.newSoSystemCode}">
                                                        <f:selectItems  value="#{EditMainEuidHandler.systemCodes}" />
                                                    </h:selectOneMenu>
                                                </td>
                                                <td>
                                                    LID : 
                                                </td>
                                                <td>
                                                    <h:inputText id="LID" value="#{EditMainEuidHandler.newSoLID}" 
                                                                 onkeydown="javascript:qws_field_on_key_down(this, document.basicNewSOAddformData.lidmask.value)"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" maxlength="10"
                                                                 />
                                                </td>
                                            </tr>                                            
                                        </table>
                                        <h:dataTable  headerClass="tablehead"  
                                                      id="hashIdEditNewSO" 
                                                      width="100%"
                                                      var="newSOfieldConfigPerAdd" 
                                                      value="#{SourceHandler.personFieldConfigs}">
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'MenuList' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:selectOneMenu value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{newSOfieldConfigPerAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:inputText label="#{newSOfieldConfigPerAdd.displayName}"  
                                                             id="fieldConfigIdTextbox"  
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}" 
                                                             required="#{newSOfieldConfigPerAdd.required}"/>
                                            </h:column>                     
                                            <!--Rendering Updateable HTML Text boxes date fields-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'DOB'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="DOB"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var DOB1 = getDateFieldName('basicNewSOAddformData',':DOB');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink"
                                                              onclick="g_Calendar.show(event,DOB1)" > 
                                                    <h:graphicImage  id="calImgStartDate" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Dod'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="DOD"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var DOD1 = getDateFieldName('basicNewSOAddformData',':DOD');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink1"
                                                              onclick="g_Calendar.show(event,DOD1)"> 
                                                    <h:graphicImage  id="calImgDODDate" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'PensionExpDate'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="PensionExpDate"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var PensionDate = getDateFieldName('basicNewSOAddformData',':PensionExpDate');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink2"
                                                              onclick="g_Calendar.show(event,PensionDate)"> 
                                                    <h:graphicImage  id="PensionDateImg" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'DummyDate'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="DummyDate"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var DummyDate = getDateFieldName('basicNewSOAddformData',':DummyDate');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink3"
                                                              onclick="g_Calendar.show(event,DummyDate)"> 
                                                    <h:graphicImage  id="DummyDateImg" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Date1'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="Date1"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var Date1 = getDateFieldName('basicNewSOAddformData',':Date1');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink4"
                                                              onclick="g_Calendar.show(event,Date1)"> 
                                                    <h:graphicImage  id="calImgDate1" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Date2'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="Date2"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var Date2 = getDateFieldName('basicNewSOAddformData',':Date2');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink5"
                                                              onclick="g_Calendar.show(event,Date2)"> 
                                                    <h:graphicImage  id="calImgDate2" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Date3'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="Date3"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var Date3 = getDateFieldName('basicNewSOAddformData',':Date3');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink6"
                                                              onclick="g_Calendar.show(event,Date3)"> 
                                                    <h:graphicImage  id="calImgDate3" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Date4'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="Date4"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var Date4 = getDateFieldName('basicNewSOAddformData',':Date4');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink7"
                                                              onclick="g_Calendar.show(event,Date4)"> 
                                                    <h:graphicImage  id="calImgDate4" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 && newSOfieldConfigPerAdd.name eq 'Date5'}">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="Date5"
                                                             value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var Date5 = getDateFieldName('basicNewSOAddformData',':Date5');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink8"
                                                              onclick="g_Calendar.show(event,Date5)"> 
                                                    <h:graphicImage  id="calImgDate5" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text Area-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextArea' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:inputTextarea label="#{newSOfieldConfigPerAdd.displayName}"  
                                                                 id="fieldConfigIdTextArea"   
                                                                 value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}" 
                                                                 required="#{newSOfieldConfigPerAdd.required}"
                                                                 />
                                            </h:column>
                                            
                                        </h:dataTable>
                                        
                                        <table border="0" width="100%">
                                            <tr><td colspan="2">&nbsp;</td></tr>
                                            <tr>
                                                <td class="tablehead" colspan="2">
                                                    <h:outputText value="#{msgs.PatDetail_AddressPrompt}"/>                                                
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAddressNewSODiv',event)" class="button"><span><h:outputText value="#{msgs.source_rec_addaddress_but}"/></span></a>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Start Display Phone fields --> 
                                        <table border="0" width="100%">
                                            <tr><td colspan="2">&nbsp;</td></tr>
                                            <tr>
                                                <td class="tablehead" colspan="2">
                                                    <h:outputText value="#{msgs.source_rec_phone_text}"/>                                               
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraPhoneNewSODiv',event)" class="button"><span><h:outputText value="#{msgs.source_rec_add_phone_but}"/></span> </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- End Display Phone fields --> 

                                        <!-- Start Display Alias fields --> 
                                        <table border="0" width="100%">
                                            <tr><td colspan="2">&nbsp;</td></tr>
                                            <tr>
                                                <td class="tablehead" colspan="2">
                                                    <h:outputText value="#{msgs.source_rec_alias_text}"/>                                               
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAliasNewSODiv',event)" class="button"><span><h:outputText value="#{msgs.source_rec_add_alias_text}"/></span></a>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- End Display Alias fields --> 
                                       <!-- Start Display AuxId fields --> 
                                        <table border="0" width="100%">
                                            <tr><td colspan="2">&nbsp;</td></tr>
                                            <tr>
                                                <td class="tablehead" colspan="2">
                                                    <h:outputText value="#{msgs.source_rec_auxid_text}"/>                                                
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAuxIdNewSODiv',event)" class="button"><span><h:outputText value="#{msgs.source_rec_add_auxid_text}"/></span></a>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- End Display AuxId fields --> 
                                      <!-- Start Display Comment fields --> 

                                        <table border="0" width="100%">
                                            <tr><td colspan="2">&nbsp;</td></tr>
                                            <tr>
                                                <td class="tablehead" colspan="2">
                                                    <h:outputText value="#{msgs.source_rec_comment_text}"/>                                                
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraCommentNewSODiv',event)" class="button"><span> <h:outputText value="#{msgs.source_rec_add_comment_text}"/></span> </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <table>
                                            <tr>
                                                <td>
                                                    <h:commandLink  styleClass="button" 
                                                                    action="#{EditMainEuidHandler.addNewSO}">
                                                        <span><h:outputText value="Add New SO" /></span>
                                                    </h:commandLink>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>                                        
                                    <!--new SO Fields end here-->
                               </h:form>
                            </tr>
                        </table>
                    </div>         
                </div>         
            </div>         
            <!-- START Extra divs for edit EO-->
            <div id="extraAddressEODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 400px;  WIDTH: 400px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:400px; height:400px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonaddress"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAddressEditIdExtra" 
                                              var="fieldConfigEditAddress" 
                                              value="#{SourceHandler.addressFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAddress.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAddress.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.editEOAddressHashMap[fieldConfigEditAddress.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditAddress.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAddress.displayName}"  
                                                     value="#{EditMainEuidHandler.editEOAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                                     required="#{fieldConfigEditAddress.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                         value="#{EditMainEuidHandler.editEOAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                                         required="#{fieldConfigAddAddress.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addEOAddress}"  styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAddressEODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraPhoneEODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonphone"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashPhoneEditIdExtra" 
                                              var="fieldConfigEditPhone" 
                                              value="#{SourceHandler.phoneFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditPhone.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditPhone.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.editEOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditPhone.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditPhone.displayName}"  
                                                     value="#{EditMainEuidHandler.editEOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                                     required="#{fieldConfigEditPhone.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddPhone.displayName}"  
                                                         value="#{EditMainEuidHandler.editEOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                                         required="#{fieldConfigAddPhone.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addEOPhone}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraPhoneEODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraAliasEODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonalias"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAliasEditIdExtra" 
                                              var="fieldConfigEditAlias" 
                                              value="#{SourceHandler.aliasFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAlias.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAlias.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.editEOAliasHashMap[fieldConfigEditAlias.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditAlias.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAlias.displayName}"  
                                                     value="#{EditMainEuidHandler.editEOAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                                     required="#{fieldConfigEditAlias.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAlias.displayName}"  
                                                         value="#{EditMainEuidHandler.editEOAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                                         required="#{fieldConfigAddAlias.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addEOAlias}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAliasEODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraAuxIdEODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonauxId"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAuxIdEditIdExtra" 
                                              var="fieldConfigEditAuxId" 
                                              value="#{SourceHandler.auxIdFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAuxId.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAuxId.required}" />
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAuxId.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAuxId.displayName}"  
                                                     value="#{EditMainEuidHandler.editEOAuxIdHashMap[fieldConfigEditAuxId.fullFieldName]}" 
                                                     required="#{fieldConfigEditAuxId.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAuxId.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAuxId.displayName}"  
                                                         value="#{EditMainEuidHandler.editEOAuxIdHashMap[fieldConfigEditAuxId.fullFieldName]}" 
                                                         required="#{fieldConfigAddAuxId.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addEOAuxId}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAuxIdEODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraCommentEODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballooncomment"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashCommentEditIdExtra" 
                                              var="fieldConfigEditComment" 
                                              value="#{SourceHandler.commentFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditComment.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditComment.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.editEOCommentHashMap[fieldConfigEditComment.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditComment.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditComment.displayName}"  
                                                     value="#{EditMainEuidHandler.editEOCommentHashMap[fieldConfigEditComment.fullFieldName]}" 
                                                     required="#{fieldConfigEditComment.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddComment.displayName}"  
                                                         value="#{EditMainEuidHandler.editEOCommentHashMap[fieldConfigEditComment.fullFieldName]}" 
                                                         required="#{fieldConfigAddComment.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addEOComment}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraCommentEODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <!-- END Extra divs for EDIT EO-->
<!-- START Extra divs for NEW SO -->
            <div id="extraAddressNewSODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 400px;  WIDTH: 400px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:400px; height:400px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonaddress"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAddressEditIdExtra" 
                                              var="fieldConfigEditAddress" 
                                              value="#{SourceHandler.addressFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAddress.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAddress.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.newSOAddressHashMap[fieldConfigEditAddress.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditAddress.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAddress.displayName}"  
                                                     value="#{EditMainEuidHandler.newSOAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                                     required="#{fieldConfigEditAddress.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                         value="#{EditMainEuidHandler.newSOAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                                         required="#{fieldConfigAddAddress.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addNewSOAddress}"  styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAddressNewSODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraPhoneNewSODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonphone"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashPhoneEditIdExtra" 
                                              var="fieldConfigEditPhone" 
                                              value="#{SourceHandler.phoneFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditPhone.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditPhone.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.newSOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditPhone.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditPhone.displayName}"  
                                                     value="#{EditMainEuidHandler.newSOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                                     required="#{fieldConfigEditPhone.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddPhone.displayName}"  
                                                         value="#{EditMainEuidHandler.newSOPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                                         required="#{fieldConfigAddPhone.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addNewSOPhone}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraPhoneNewSODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraAliasNewSODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonalias"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAliasEditIdExtra" 
                                              var="fieldConfigEditAlias" 
                                              value="#{SourceHandler.aliasFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAlias.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAlias.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.newSOAliasHashMap[fieldConfigEditAlias.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditAlias.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAlias.displayName}"  
                                                     value="#{EditMainEuidHandler.newSOAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                                     required="#{fieldConfigEditAlias.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAlias.displayName}"  
                                                         value="#{EditMainEuidHandler.newSOAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                                         required="#{fieldConfigAddAlias.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addNewSOAlias}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAliasNewSODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraAuxIdNewSODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballoonauxId"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashAuxIdEditIdExtra" 
                                              var="fieldConfigEditAuxId" 
                                              value="#{SourceHandler.auxIdFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditAuxId.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditAuxId.required}" />
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditAuxId.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditAuxId.displayName}"  
                                                     value="#{EditMainEuidHandler.newSOAuxIdHashMap[fieldConfigEditAuxId.fullFieldName]}" 
                                                     required="#{fieldConfigEditAuxId.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditAuxId.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddAuxId.displayName}"  
                                                         value="#{EditMainEuidHandler.newSOAuxIdHashMap[fieldConfigEditAuxId.fullFieldName]}" 
                                                         required="#{fieldConfigAddAuxId.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addNewSOAuxId}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraAuxIdNewSODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>            
            <div id="extraCommentNewSODiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 200px;  WIDTH: 200px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:200px; height:200px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <h:form>
                    <table>
                        <tr>
                            <td align="right" colspan="2">
                                <div>
                                    <a href="javascript:void(0)" rel="editballooncomment"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h:dataTable  headerClass="tablehead" 
                                              id="hashCommentEditIdExtra" 
                                              var="fieldConfigEditComment" 
                                              value="#{SourceHandler.commentFieldConfigs}">
                                    <h:column>
                                        <h:outputText value="#{fieldConfigEditComment.displayName}"  />
                                        <h:outputText value="*" rendered="#{fieldConfigEditComment.required}" />
                                    </h:column>
                                    <!--Rendering HTML Select Menu List-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'MenuList'}" >
                                        <h:selectOneMenu value="#{EditMainEuidHandler.newSOCommentHashMap[fieldConfigEditComment.fullFieldName]}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{fieldConfigEditComment.selectOptions}"  />
                                        </h:selectOneMenu>
                                    </h:column>
                                    <!--Rendering Updateable HTML Text boxes-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'TextBox'}" >
                                        <h:inputText label="#{fieldConfigEditComment.displayName}"  
                                                     value="#{EditMainEuidHandler.newSOCommentHashMap[fieldConfigEditComment.fullFieldName]}" 
                                                     required="#{fieldConfigEditComment.required}"/>
                                    </h:column>                     
                                    <!--Rendering Updateable HTML Text Area-->
                                    <h:column rendered="#{fieldConfigEditComment.guiType eq 'TextArea'}" >
                                        <h:inputTextarea label="#{fieldConfigAddComment.displayName}"  
                                                         value="#{EditMainEuidHandler.newSOCommentHashMap[fieldConfigEditComment.fullFieldName]}" 
                                                         required="#{fieldConfigAddComment.required}"
                                                         />
                                    </h:column>
                                    
                                </h:dataTable>                                                                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h:commandLink  actionListener="#{EditMainEuidHandler.addNewSOComment}" styleClass="button">
                                    <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </h:commandLink>   
                                
                            </td>
                            <td>
                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('extraCommentNewSODiv',event)">
                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                </h:outputLink>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <h:messages  styleClass="errorMessages"  layout="list" />
                            </td>
                        </tr>                              
                    </table>
                    
                </h:form>
            </div>      	            
            
            <!-- END Extra divs for NEW  SO-->
            <div id="linkSoDiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 100px;  WIDTH: 300px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:300px; height:100px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <form name="linkForm">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="linkedValueDiv" style="visibility:hidden"></div>
                            <nobr>Link to the '<span id="linkedDisplayValueDiv"></span>' field of :</nobr> 
                        </td>
                        <td>
                            <h:selectOneMenu id="systemCodeWithLid" value="#{EditMainEuidHandler.linkedSoWithLidByUser}">
                                <f:selectItems  value="#{EditMainEuidHandler.eoSystemObjectCodesWithLids}" />
                            </h:selectOneMenu>
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:populateLinkFields()">
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                            </h:outputLink>
                        </td>
                        <td align="left">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('linkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 

            <div id="unLinkSoDiv" style="TOP: 620px; LEFT: 450px; HEIGHT: 100px;  WIDTH: 300px; BORDER-RIGHT: #000099 thin solid; font-color:red;BORDER-TOP: #000099 thin solid; PADDING-LEFT: 5px; VISIBILITY: hidden; BORDER-LEFT: #000099 thin solid; PADDING-TOP: 5px; BORDER-BOTTOM: #000099 thin solid; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; BACKGROUND-COLOR:#f9f7de ;">
                <iframe src="Blank.html" scrolling="no" frameborder="0" style="width:300px; height:100px; z-index: -1; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);">
                    
                </iframe>
                <form name="unlinkForm">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="unLinkedValueDiv" style="visibility:hidden"></div>
                            <div id="unLinkedFullFieldDiv" style="visibility:hidden"></div>
                            <nobr>Unlink from  '<span id="unLinkedDisplayValueDiv"></span>' field of Main EUID?</nobr> 
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:populateUnLinkFields()">
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                            </h:outputLink>
                        </td>
                        <td align="left">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('unLinkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 
            
            <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
            <script>
                (function() {
                    var tabView = new YAHOO.widget.TabView('demo');
                    
                    YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
                })();
            </script>
            <!--END SOURCE CODE FOR EXAMPLE =============================== -->
        </body>
    </html>
    </f:view>
    
    
    