<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>


<f:view>
        <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>
        
        <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
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
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
        <script type="text/javascript" >
            var fieldNameValuesLocal="";
            var fieldNamesLocal="";
            var minorObjTypeLocal = "";
            var minorObjTypeLocalCount = 0;
            var minorArrayLocal = new Array();
            
            function accumilateMinorObjectFieldsOnBlurLocal(objectType,field,fullFieldName,mask,valueType) {
                var maskChars = new Array();
                var str = mask;
                
                str  = str.replace(/D/g,"");
                
                maskChars = str.split('');
                
                var valueEntered =  "";
                valueEntered =  field.value;
                //alert(valueEntered);

                //valueEntered  = valueEntered.replace(/\)/g,"");
                //valueEntered  = valueEntered.replace(/\(/g,"");
                
                //alert(valueType);
                if(valueType != '6' || valueType != '8' ) {
                    for(var i=0;i<maskChars.length;i++) {
                        valueEntered  = valueEntered.replace(maskChars[i],'');
                    }    
                }    
                //alert(valueEntered);

                minorObjTypeLocal =   objectType;
                //alert("fieldNamesLocal ==>" + valueEntered+":" + fullFieldName);
                if(fieldNamesLocal != fullFieldName+':') {
                    fieldNamesLocal+=fullFieldName+':';
                }
                //alert(fullFieldName  + "field.value====> " + field.value);
                fieldNameValuesLocal += fullFieldName + "##"+valueEntered+ ">>" + minorObjTypeLocalCount + ">>";
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValuesLocal;
                
                fieldNameValuesLocal = fieldNameValuesLocal  + minorObjTypeLocal ;
                //alert(" <====== > fieldNameValuesLocal "+fieldNameValuesLocal);
                
                minorArrayLocal.push(fieldNameValuesLocal);
                //RESET THE FIELD VALUES HERE
                fieldNameValuesLocal = "";
                
                //alert(" <====== >minorObjTypeLocalCount "+minorObjTypeLocalCount);
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArrayLocal;
                
            }
            
            function accumilateMinorObjectSelectFieldsOnBlurLocal(objectType,field,fullFieldName) {
                var selectedValue = field.options[field.selectedIndex].value;
                minorObjTypeLocal =   objectType;
                //alert("SELECT FIELDS fieldNamesLocal ==>" + selectedValue+":" + fullFieldName);
                if(fieldNamesLocal != fullFieldName+':') {
                    fieldNamesLocal+=fullFieldName+':';
                }
                //alert(fullFieldName  + "field.value====> " + field.value);
                fieldNameValuesLocal += fullFieldName + "##"+selectedValue + ">>" + minorObjTypeLocalCount +">>";
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValuesLocal;
                
                fieldNameValuesLocal = fieldNameValuesLocal  + minorObjTypeLocal;
                //alert(" <====== > fieldNameValuesLocal "+fieldNameValuesLocal);
                
                minorArrayLocal.push(fieldNameValuesLocal);
                //RESET THE FIELD VALUES HERE
                fieldNameValuesLocal = "";
                
                //alert(" <====== > minorArrayLocal "+minorArrayLocal);
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArrayLocal;
                //alert(" <====== >minorObjTypeLocalCount "+minorObjTypeLocalCount);
                
            }
            
            
   </script>
        <!--there is no custom header content for this example-->
        
    </head>
    <%@include file="./templates/header.jsp"%>
    <body class="yui-skin-sam">
        <div id="mainContent" style="overflow:hidden;"> 
        <div id="sourcerecords">
            <table border="0" cellspacing="0" cellpadding="0" width="90%">
                <% Operations operations=new Operations();%>
                <tr>
                    <td>
                        <div id="demo" class="yui-navset">
                            <ul class="yui-nav">
                                <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li class="selected"><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li class="selected"><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%}%>  
                            </ul>  
                            <%
                                        ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                                        //System.out.println("screenObject.getSubscreensConfig();" + screenObject.getSubscreensConfig());
                                        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
                                        ArrayList searchResultsScreenConfigArray = (ArrayList) session.getAttribute("viewEditResultsConfigArray");
                                        ArrayList systemObjectsMapList = (ArrayList) session.getAttribute("systemObjectsMapList");
                                        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                                        ValueExpression LIDVaueExpression = null;
                                        ValueExpression sourceSystemVaueExpression = null;
                                        ConfigManager.init();
                                        EPathArrayList ePathArrayList = new EPathArrayList();

                                        //System.out.println("allFeildConfigs" + allFeildConfigs.length);
                                        String mainDOB;
                                        SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");

                                        HashMap systemObjectMap = (HashMap) session.getAttribute("systemObjectMap");
                                        String keyFunction = (String) session.getAttribute("keyFunction");
                                        SourceHandler sourceHandler = new SourceHandler();
                                        Object[] personConfigFeilds = sourceHandler.getRootNodeFieldConfigs().toArray();
                                        SourceEditHandler sourceEditHandler = (SourceEditHandler)session.getAttribute("SourceEditHandler");
                                        //System.out.println("sourceEditHandler "  + sourceEditHandler );
                                        SourceAddHandler  sourceAddHandler   = new SourceAddHandler();
                                        int addressSize;
                                        int phoneSize;
                                        int aliasSize;
                                        int commentSize;
                                        int dateCount=99999;
                                                        HashMap resultArrayMapCompare = new HashMap();
                                                        HashMap resultArrayMapMain = new HashMap();

                                                        ValueExpression fnameExpression;
                                                        ValueExpression fvalueVaueExpression;
                                    %>
                            <div class="yui-content">
                              <% if(operations.isSO_SearchView()){%> 
                                <div id=viewEditTab">
                            
                                    <%if (singleSystemObjectLID != null) {%>
                                    <%if ("viewSO".equalsIgnoreCase(keyFunction)) {%>
                                    <h:form>
                                        <div id="sourceViewBasicSearch">                                            
                                            <table border="0" width="60%">
                                                    <tr>
                                                        <td>
                                                            <h:commandLink  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                            </h:commandLink>                                                                                                 
                                                        </td>
                                                    </tr>  
                                                    <tr class="odd">
                                                        <td><h:outputText value="#{msgs.source_rec_status_but}"/></td>
                                                        <td><%=singleSystemObjectLID.getStatus()%> </td>
                                                    </tr>                                                    
                                                    <tr class="even">
                                                        <td><h:outputText value="#{msgs.source_rec_sourcename_text}"/></td>
                                                        <td><%=singleSystemObjectLID.getSystemCode()%> </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td><h:outputText value="#{msgs.datatable_localid_text}"/></td>
                                                        <td><%=singleSystemObjectLID.getLID()%> </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="tablehead" colspan="2">
                                                          <h:outputText value="#{msgs.personal_information_text}"/>                                              
                                                        </td>
                                                    </tr>
                                            </table>
                                            
                                            <!--Start Displaying the root node fields -->                                        
                                            <div style="height:300px;width:60%;overflow:auto">                                                    
                                                    <h:dataTable  
                                                        headerClass="tablehead" 
                                                        id="hashId" 
                                                        width="100%"
                                                        var="fieldConfig" 
                                                        rowClasses="odd,even"
                                                        value="#{SourceHandler.rootNodeFieldConfigs}">
                                                            <h:column>
                                                                <h:outputText value="#{fieldConfig.displayName}"  />
                                                            </h:column>
                                                            <h:column>
                                                                <h:outputText value="#{SourceHandler.singleSOHashMap['SYSTEM_OBJECT'][fieldConfig.fullFieldName]}"  />
                                                            </h:column>
                                                    </h:dataTable>               
                                            </div>

                                     <!--End Displaying the root node fields -->    
                                     
                                    <!--End Displaying the minor object fields -->    
                                     <h:dataTable  headerClass="tablehead" 
                                                          id="allChildNodesNamesSoEdit" 
                                                          width="60%"
                                                          rowClasses="odd,even"                                     
                                                          var="childNodesName" 
                                                          value="#{SourceHandler.allSOChildNodesLists}">
                                       <h:column>
                                              <p><h:outputText styleClass="tablehead" style="width:100%;" value="#{childNodesName['NAME']}"  /></p>
                                              <h:dataTable  headerClass="tablehead" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  id="sofieldConfigDPId" 
                                                                  var="childMapArrayList" 
                                                                  value="#{SourceHandler.singleSOHashMap[childNodesName['KEYLIST']]}">
                                                        <h:column>
                                                            <h:dataTable 
                                                                rowClasses="odd,even"                                     
                                                                id="minorHashId" 
                                                                width="100%"
                                                                var="childFieldConfig" 
                                                                value="#{childNodesName['FIELDCONFIGS']}">
                                                                <h:column>
                                                                    <h:outputText value="#{childFieldConfig.displayName}"  />
                                                                </h:column>
                                                                <h:column>
                                                                    <h:outputText value="#{childMapArrayList[childFieldConfig.fullFieldName]}"  />
                                                                </h:column>
                                                            </h:dataTable>               
                                                        </h:column>
                                                    </h:dataTable>                                                             
                                       </h:column>
                                   </h:dataTable>

                                    <!--End Displaying the minor object fields -->    

                                    
                                            <table>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <%
                                                    ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());

                                                    %>
                                                    
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toSourceRecords}" 
                                                                        rendered="#{Operations.SO_Edit}"
                                                                        actionListener="#{SourceEditHandler.editLID}" >
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>                
                                                            <span><h:outputText value="#{msgs.source_rec_edit_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        rendered="#{Operations.SO_SearchView}"
                                                                        action="#{NavigationHandler.toEuidDetails}" 
                                                                        actionListener="#{SourceHandler.viewEUID}" >  
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
                                                </tr> 
                                                <tr><td>&nbsp;</td></tr>
                                                
                                            </table>
                                        </div>
                                    </h:form>
                                    <%} else if ("editSO".equalsIgnoreCase(keyFunction)) {%>
                                    <%
                                     ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());
                                    %>
                                    
                                    <div id="sourceViewBasicSearch">
                                        
                                        <h:form>
                                            <table border="0" width="75%">
                                                        <tr>
                                                            <td>
                                                                <h:commandLink  styleClass="button" 
                                                                                action="#{NavigationHandler.toSourceRecords}" 
                                                                                actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                    <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                                </h:commandLink>                                                                                                 
                                                            </td>
                                                        </tr>  
                                                        <tr class="odd">
                                                            <td><h:outputText value="#{msgs.source_rec_status_but}"/> </td>
                                                            <td><%=singleSystemObjectLID.getStatus()%> </td>
                                                        </tr>                                                    
                                                        <tr class="even">
                                                            <td><h:outputText value="#{msgs.source_rec_status_but}"/></td>
                                                            <td><%=singleSystemObjectLID.getSystemCode()%> </td>
                                                        </tr>
                                                        <tr class="odd">
                                                            <td><h:outputText value="#{msgs.datatable_localid_text}"/> </td>
                                                            <td><%=singleSystemObjectLID.getLID()%> </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <h:outputText value="#{msgs.personal_information_text}"/>                                               
                                                            </td>
                                                        </tr>
                                                    </table>    
                                        </h:form>
                                        <h:form id="BasicSearchFieldsForm">
                                            <table width="100%">
                                                <tr>
                                                    <td align="left">
                                                        <% if ("View/Edit".equalsIgnoreCase((String)session.getAttribute("tabName")))      {%>
                                                          <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                        <!-- Start EDIT Fields-->
                                                            <!--Start Displaying the person fields -->                                        
                                                              <h:dataTable  
                                            headerClass="tablehead"                                        
                                            width="100%"
                                            rowClasses="odd,even"                                     
                                            id="hashIdEdit" 
                                            var="fieldConfigPer" 
                                            value="#{SourceHandler.rootNodeFieldConfigs}">                                                    
                                                    <h:column>
                                                        <h:outputText value="#{fieldConfigPer.displayName}"  />
                                                        <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                    </h:column>                                                        
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" >
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextbox"  
                                                                     maxlength="#{fieldConfigPer.maxLength}"
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                     />
                                                    </h:column>
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                          <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6}">
                                                                  <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}"  
                                                                     required="#{fieldConfigPer.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPer.inputMask}')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                     onblur="javascript:validate_date(this,'MM/dd/yyyy');"/>
                                                                    <script> var DOB = getDateFieldName('BasicSearchFieldsForm',':DOB');</script>                                                                             
                                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,DOB)" > 
                                                                        <h:graphicImage  id="calImgDobDate" 
                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                         url="./images/cal.gif"/>               
                                                                    </a>
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                                     required="#{fieldConfigPer.required}"/>
                                                                </h:column>
                                                            </h:dataTable>               
                                                            <!--End Displaying the person fields -->    
                                                            <!--Minor Object fields here -->       
                                                               <h:dataTable  headerClass="tablehead" 
                                                                                  id="allChildNodesNamesSoEdit" 
                                                                                  width="100%"
                                                                                  rowClasses="odd,even"                                     
                                                                                  var="childNodesName" 
                                                                                  value="#{SourceHandler.allSOChildNodesLists}">
                                                                        <h:column>
                                                                            <table width="100%">
                                                                                <tr>
                                                                                    <td class="tablehead" colspan="2">
                                                                                        <h:outputText value="#{childNodesName['NAME']}"/>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right" colspan="2">
                                                                                        <a href="javascript:void(0);" 
                                                                                           onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName['NAME']}"/>EODiv',event)" 
                                                                                           class="button">
                                                                                            <span>Add <h:outputText value="#{childNodesName['NAME']}"/></span>
                                                                                        </a>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="left" colspan="2">
                                                                                        <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODiv" style="width:100%;visibility:hidden;"></div>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right" colspan="2">
                                                                                        <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose" style="visibility:hidden;">
                                                                                            <table>
                                                                                                <tr>
                                                                                                    <td align="right" colspan="2">
                                                                                                        <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>EODiv','add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose')" class="button">
                                                                                                        <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </div>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>   
                                                                            <h:dataTable  headerClass="tablehead" 
                                                                                          width="100%"
                                                                                          rowClasses="odd,even"                                     
                                                                                          id="sofieldConfigId" 
                                                                                          var="adressMapArrayList" 
                                                                                          value="#{SourceHandler.singleSOHashMap[childNodesName['KEYLIST']]}">
                                                                                <h:column>
                                                                                    <h:dataTable 
                                                                                        rowClasses="odd,even"                                     
                                                                                        id="minorHashId" 
                                                                                        var="addressFieldConfig" 
                                                                                        value="#{childNodesName['FIELDCONFIGS']}">
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
                                                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                                         onblur="javascript:validate_date(this,'MM/dd/yyyy');"/>
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
                                                                                                <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>EODiv','add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose')" class="button">
                                                                                                <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                                            </h:column>
                                                                                        </f:facet>
                                                                                    </h:dataTable>               
                                                                                </h:column>
                                                                            </h:dataTable>                                                             
                                                                        </h:column>
                                                                    </h:dataTable>
                                                            <!-- End Display minor objects fields --> 
                                                           <!-- End EDIT Fields-->
                                                     <%} else if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                          <!-- Start READ ONY Fields-->
                                                            <!--Start Displaying the root node fields -->                                        
                                           <h:dataTable  
                                            headerClass="tablehead"                                        
                                            width="100%"
                                            rowClasses="odd,even"                                     
                                            id="hashIdEdit" 
                                            var="fieldConfigPer" 
                                            value="#{SourceHandler.rootNodeFieldConfigs}">                                                    
                                                    <h:column>
                                                        <h:outputText value="#{fieldConfigPer.displayName}"  />
                                                        <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                    </h:column>                                                        
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                         readonly="true" 
                                                                         disabled="true">
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextbox"  readonly="true" disabled="true"
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                     />
                                                    </h:column>
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6}">
                                                                        <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}"  
                                                                                     readonly="true" disabled="true"
                                                                                     required="#{fieldConfigPer.required}"
                                                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPer.inputMask}')"
                                                                                     onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                     onblur="javascript:validate_date(this,'MM/dd/yyyy');"/>
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   readonly="true" disabled="true"
                                                                                         value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                                         required="#{fieldConfigPer.required}"/>
                                                                    </h:column>
                                                                </h:dataTable>               
                                                                <!--End Displaying the root node fields -->    
                                                              <!--Minor Object fields here -->       
                                                              <h:dataTable  headerClass="tablehead" 
                                                                              id="allChildNodesNamesSoEdit" 
                                                                              width="100%"
                                                                              rowClasses="odd,even"                                     
                                                                              var="childNodesName" 
                                                                              value="#{SourceHandler.allSOChildNodesLists}">
                                                                    <h:column>
                                                                    <table width="100%">
                                                                        <tr>
                                                                            <td class="tablehead" colspan="2">
                                                                                <h:outputText value="#{childNodesName['NAME']}"/>
                                                                            </td>
                                                                        </tr>
                                                                    </table>   
                                                                    <h:dataTable  headerClass="tablehead" 
                                                                                  width="100%"
                                                                                  rowClasses="odd,even"                                     
                                                                                  id="sofieldConfigId" 
                                                                                  var="adressMapArrayList" 
                                                                                  value="#{SourceHandler.singleSOHashMap[childNodesName['KEYLIST']]}">
                                                                        <h:column>
                                                                            <h:dataTable 
                                                                                rowClasses="odd,even"                                     
                                                                                id="minorHashrReadOnlyId" 
                                                                                var="addressFieldConfig" 
                                                                                value="#{childNodesName['FIELDCONFIGS']}">
                                                                                <h:column>
                                                                                    <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                                                    <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                                                </h:column>
                                                                                <!--Rendering HTML Select Menu List-->
                                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                                                    <h:selectOneMenu value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" readonly="true" disabled="true">
                                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                                        <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                                                    </h:selectOneMenu>
                                                                                </h:column>
                                                                                
                                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                                                    <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                                                 id="fieldConfigIdTextbox"   readonly="true" disabled="true"
                                                                                                 value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                                 required="#{addressFieldConfig.required}"/>
                                                                                </h:column>
                                                                                
                                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                                                    <h:inputText label="#{addressFieldConfig.displayName}"   
                                                                                                 value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                                                 id="dateField" readonly="true" disabled="true"
                                                                                                 required="#{addressFieldConfig.required}"
                                                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                                 onblur="javascript:validate_date(this,'MM/dd/yyyy');"/>
                                                                                        <h:graphicImage  id="calImgStartDate" 
                                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                                         url="./images/cal.gif"/>               
                                                                                </h:column>
                                                                                
                                                                                <!--Rendering Updateable HTML Text Area-->
                                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                                                    <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                                                     id="fieldConfigIdTextArea"   readonly="true" disabled="true"
                                                                                                     value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                                     required="#{addressFieldConfig.required}"/>
                                                                                </h:column>
                                                                            </h:dataTable>               
                                                                        </h:column>
                                                                    </h:dataTable>                                                             
                                                                </h:column>
                                                            </h:dataTable>
                                                              <!-- End Display minor objects fields --> 
                                                         <!-- End READ ONLY Fields-->

                                                     <%}%>
                                                        <table>  
                                                            <tr>       
                                                     <% if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                            
                                                                <td>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Edit}"
                                                                                    action="#{SourceEditHandler.updateSO}" >
                                                                        <span><h:outputText value="#{msgs.source_rec_save_but}" /></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <h:commandLink  styleClass="button" 
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <%}%>         
                                                                <td>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}" 
                                                                                    action="#{NavigationHandler.toEuidDetails}" 
                                                                                    actionListener="#{SourceHandler.viewEUID}" >  
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Deactivate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.deactivateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                    <%if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Activate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.activateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                    </td>
                                                </tr>
                                            </table>

                                        </h:form>
                                    </div>
                                    <%}%>
                                    <%} else {%>
                                    <div id="sourceViewBasicSearch">
                                        <h:form id="basicViewformData">
                                            <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                            <table border="0" cellpadding="0" cellspacing="0">
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <td>
                                                        <h:dataTable id="fieldConfigId" var="feildConfig" headerClass="tablehead"  value="#{SourceHandler.viewEditScreenConfigArray}">
                                                            <!--Rendering Non Updateable HTML Text Area-->
                                                            <h:column>
                                                                <h:outputText value="#{feildConfig.displayName}" />
                                                            </h:column>
                                                            <!--Rendering HTML Select Menu List-->
                                                            <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                                               <h:selectOneMenu value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" rendered="#{feildConfig.name eq 'SystemCode'}"
                                                                                 onchange="javascript:setLidMaskValue(this,'basicViewformData')">
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                               <h:selectOneMenu value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" rendered="#{feildConfig.name ne 'SystemCode'}">
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                            </h:column>
                                                            
                                                            <!--Rendering Updateable HTML Text boxes-->
                                                            <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="fieldConfigIdText" value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <!--Rendering Updateable HTML Text Area-->
                                                            <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                                                                <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            
                                                            <!--Rendering Non Updateable HTML Text boxes-->
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                                                <h:inputText id="SystemCode" value="#{SourceHandler.SystemCode}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="EUID" value="#{SourceHandler.EUID}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="LID" value="#{SourceHandler.LID}" required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, document.basicViewformData.lidmask.value)"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"
                                                                             />
                                                            </h:column>
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    value="#{SourceHandler.create_start_date}"  id="create_start_date"
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,'basicViewformData:fieldConfigId:1:create_start_date')" > 
                                                                    <h:graphicImage  id="calImgStartDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    value="#{SourceHandler.create_end_date}" id="create_end_date" 
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 'basicViewformData:fieldConfigId:2:create_end_date')" > 
                                                                    <h:graphicImage  id="calImgEndDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                                             value="#{SourceHandler.create_start_time}" required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="create_end_time" value="#{SourceHandler.create_end_time}" 
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="Status"  value="#{SourceHandler.Status}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <a class="button" href="javascript:ClearContents('basicViewformData')">
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                    <h:commandLink  styleClass="button" action="#{SourceHandler.performSubmit}" >  
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </h:column>
                                                                
                                                            </f:facet>
                                                            
                                                        </h:dataTable>
                                                    </td>
                                                    <td valign="top">
                                                        <% if ("View/Edit".equalsIgnoreCase((String)session.getAttribute("tabName")))      {%>
                                                          <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table>  
                                        </h:form>
                                    </div>                                                                 
                                    <div id="results" class=reportYUISearch"">
                                        <%
                                        //check if so's in session
                                        if (systemObjectsMapList != null && searchResultsScreenConfigArray != null) {
                                        %>
                                        <table>
                                            <tr>
                                                <td>
                                                    <h:outputText value="#{msgs.total_records_text}"/><%=systemObjectsMapList.size()%> 
                                                </td>
                                                <td>
                                                    <a class="button" href="#"><span><h:outputText value="#{msgs.print_text}"/></span></a>
                                                </td>
                                            </tr>
                                        </table>  
                                        <table> 
                                            <thead>
                                                <th>LID</th>
                                                <th>EUID</th>
                                                <%

                                            Object[] searchResultsObj = searchResultsScreenConfigArray.toArray();
                                            for (int i = 0; i < searchResultsObj.length; i++) {
                                                FieldConfig fieldConfig = (FieldConfig) searchResultsObj[i];
                                                //System.out.println("dispklay Name" + fieldConfig.getDisplayName());
                                                %>
                                                <th><%=fieldConfig.getDisplayName()%></th>
                                                <%
                                            }
                                                %>
                                                <th>Create Date Time</th>
                                                <th>Source</th>
                                            </thead>
                                            <h:form>
                                                <tbody>
                                                    <%for (int al = 0; al < systemObjectsMapList.size(); al++) {
                                                HashMap fieldvalues = (HashMap) systemObjectsMapList.get(al);
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <%
    LIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(fieldvalues.get("LID"), fieldvalues.get("LID").getClass());
    sourceSystemVaueExpression = ExpressionFactory.newInstance().createValueExpression(fieldvalues.get("Source"), fieldvalues.get("Source").getClass());
                                                            %>
                                                            <h:commandLink actionListener="#{SourceHandler.setLIDValue}">
                                                                <%=fieldvalues.get("LID")%>
                                                                <f:attribute name="sourceLID" value="<%=LIDVaueExpression%>"/>
                                                                <f:attribute name="sourceSystem" value="<%=sourceSystemVaueExpression%>"/>
                                                            </h:commandLink>
                                                        </td>
                                                        <td><%=fieldvalues.get("EUID")%></td>
                                                        <%
    for (int i = 0; i < searchResultsObj.length; i++) {
        FieldConfig fieldConfig = (FieldConfig) searchResultsObj[i];
                                                        %>
                                                        <td>
                                                            <%if (fieldvalues.get(fieldConfig.getFullFieldName()) != null) {%> 
                                                            <%=fieldvalues.get(fieldConfig.getFullFieldName())%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>
                                                        </td>
                                                        <%}%>
                                                        <td><%=fieldvalues.get("DateTime")%></td>
                                                        <td><%=fieldvalues.get("Source")%></td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </h:form>
                                        </table>
                                        <%}%>
                                    </div>
                                    <%}%>
                                    
                                </div>  
                              <%}%> 
                              <% if(operations.isSO_Add()){%> 
                                <div id="addTab">
                                    <h:form id="basicAddformData">
                                        <table width="85%">
                                            <tr>
                                                <td>
                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <!--Start Add source record form-->
                                                    <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                                    <input type="hidden" name="ssnmask" value="DDD-DD-DDDD" />
                                                    <h:dataTable headerClass="tablehead"  
                                                                             id="fieldConfigId" 
                                                                             var="feildConfig" 
                                                                             value="#{SourceAddHandler.addScreenConfigArray}">
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
                                                                            <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicAddformData')"
                                                                                              id="SystemCode" 
                                                                                              value="#{SourceAddHandler.systemCode}" 
                                                                                              rendered="#{feildConfig.name eq 'SystemCode'}"
                                                                                              required="true">
                                                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                            </h:selectOneMenu>
                                                                        </nobr>
                                                                    </h:column>
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6}" >
                                                                        <nobr>
                                                                            <h:inputText   required="true" 
                                                                                           label="#{feildConfig.displayName}" 
                                                                                           onkeydown="javascript:qws_field_on_key_down(this, document.basicAddformData.lidmask.value)"
                                                                                           onblur="javascript:qws_field_on_key_down(this, document.basicAddformData.lidmask.value);"
                                                                                           onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                           value="#{SourceAddHandler.LID}"
                                                                                           maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                                                           rendered="#{feildConfig.name eq 'LID'}"/>
                                                                                           
                                                                        </nobr>
                                                                    </h:column>
                                                                </h:dataTable>
                                                   <%if(session.getAttribute("validation") != null ) {%>
                                               
                                                    <!-- Start ADD  Fields-->
                                                    <table width="100%">
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
                                                    <h:dataTable  headerClass="tablehead"  
                                                                  id="hashIdEdit" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  var="fieldConfigPerAdd" 
                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">
                                                        <h:column>
                                                            <h:outputText value="#{fieldConfigPerAdd.displayName}"  />
                                                            
                                                            <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
                                                        </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:selectOneMenu onblur="javascript:accumilateNewPersonSelectFieldsOnBlur(this,'#{fieldConfigPerAdd.fullFieldName}')">
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                         id="fieldConfigIdTextbox"  
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         onblur="javascript:accumilateNewPersonFieldsOnBlur(this,'#{fieldConfigPerAdd.fullFieldName}','#{fieldConfigPerAdd.inputMask}','#{fieldConfigPerAdd.valueType}')"                                                                         
                                                                         required="#{fieldConfigPerAdd.required}"/>
                                                        </h:column>                     
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">
                                                            <h:inputText label="#{fieldConfigPerAdd.name}"
                                                                         required="#{fieldConfigPerAdd.required}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateNewPersonFieldsOnBlur(this,'#{fieldConfigPerAdd.fullFieldName}','#{fieldConfigPerAdd.inputMask}','#{fieldConfigPerAdd.valueType}')"                                                                         
                                                                         />
                                                            <script> var DOB1 = getDateFieldName('basicAddformData',':DOB');</script>                                                                            
                                                            <h:outputLink value="javascript:void(0);"  id="calLink"
                                                                          onclick="g_Calendar.show(event,DOB1)" > 
                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                 url="./images/cal.gif"/>               
                                                            </h:outputLink>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                                                             id="fieldConfigIdTextArea"   
                                                                             required="#{fieldConfigPerAdd.required}"
                                                                             onblur="javascript:accumilateNewPersonFieldsOnBlur(this,'#{fieldConfigPerAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"                                                                         
                                                                             />
                                                        </h:column>
                                                        
                                                    </h:dataTable>
                                                    
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="allChildNodesNamesAdd" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  var="childNodesName" 
                                                                  value="#{SourceHandler.allChildNodesNames}">
                                                        <h:column>
                                                            <table width="100%">
                                                                <tr>
                                                                    <td class="tablehead" colspan="2">
                                                                        <h:outputText value="#{childNodesName}"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <a href="javascript:void(0);" 
                                                                           onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>AddDiv',event)" 
                                                                           class="button">
                                                                            <span>Add <h:outputText value="#{childNodesName}"/></span>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <div id="add<h:outputText value="#{childNodesName}"/>Div" style="width:100%;visibility:hidden;"></div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <div id="add<h:outputText value="#{childNodesName}"/>DivClose" style="visibility:hidden;">
                                                                            <table>
                                                                                <tr>
                                                                                    <td align="right" colspan="2">
                                                                                        <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName}"/>Div','add<h:outputText value="#{childNodesName}"/>DivClose')" class="button">
                                                                                        <span>Delete <h:outputText value="#{childNodesName}"/></span></a>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>   
                                                        </h:column>
                                                    </h:dataTable>
                                                    <%}%>                                                                                                                                                           
                                                    
                                                    <!--End Add source record form-->
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a class="button" href="javascript:ClearContents('basicAddformData')">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <%if (session.getAttribute("validation") != null) {%>
                                                                <nobr>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Add}"
                                                                                    action="#{SourceAddHandler.addNewSO}">  
                                                                        <span><h:outputText value="#{msgs.submit_button_text}"/></span>
                                                                    </h:commandLink>                                     
                                                                </nobr>
                                                                <%} else {%>
                                                                <nobr>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Add}"
                                                                                    action="#{SourceAddHandler.validateLID}">  
                                                                        <span><h:outputText value="#{msgs.validate_button_text}"/></span>
                                                                    </h:commandLink>                                     
                                                                </nobr>
                                                                <%}%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                             </td>   
                                          </tr>   
                                       </table>   
                                        <h:inputHidden id="minorObjectsEnteredFieldValues" value="#{SourceAddHandler.minorObjectsEnteredFieldValues}"/>
                                        <h:inputHidden id="newSOEnteredFieldValues" value="#{SourceAddHandler.newSOEnteredFieldValues}"/>
                                        <h:inputHidden id="minorObjectTotal" value="#{SourceAddHandler.minorObjectTotal}"/>
                                  </h:form>
                                </div>
                              <%}%> 
                              <% if(operations.isSO_Merge()){%> 
                                <div id="mergeTab">
                                        <table border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td>
                                                   <h:form id="basicMergeformData">
                                                     <table border="0" cellpadding="0" cellspacing="0">
                                                           <tr>
                                                               <td>
                                                                   <h:outputLabel for="#{msgs.transaction_source}" value="#{msgs.transaction_source}"/>
                                                                </td>
                                                               <td>
                                                                   <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicMergeformData')"
                                                                                     id="sourceOption" 
                                                                                     value="#{SourceMergeHandler.source}" >
                                                                       <f:selectItems  value="#{SourceMergeHandler.selectOptions}" />
                                                                   </h:selectOneMenu>
                                                               </td>
                                                               <input id='lidmask' type='hidden' name='lidmask' value='<h:outputText value="#{SourceMergeHandler.lidMask}"/>' />
                                                               
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                               <h:outputText value="#{msgs.source_merge_head1}"/>
                                                               <h:inputText value="#{SourceMergeHandler.lid1}"
                                                                            onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                            onkeyup="javascript:qws_field_on_key_up(this)"
                                                                            maxlength="#{SourceMergeHandler.lidMaskLength}" />  
                                                               <td>&nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head2}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head3}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head4}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid4}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               
                                                               <td><nobr>
                                                                       <h:commandLink  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                                       action="#{SourceMergeHandler.performLidMergeSearch}">
                                                                           <span><h:outputText value="#{msgs.source_merge_button}"/></span>
                                                                       </h:commandLink>                                     
                                                                   </nobr>  
                                                               </td>
                                                           </tr>
                                                
                                                    </table>
                                                   </h:form>
                                             <hr/>
                                             <%
                                               if(session.getAttribute("soHashMapArrayList") != null ){
                                                   
                                                  //request.setAttribute("soHashMapArrayList",request.getAttribute("soHashMapArrayList")); 
                                                 ArrayList newSoArrayList= (ArrayList) session.getAttribute("soHashMapArrayList");
                                                 //System.out.println("==>Hellllllllllllllllll : " + newSoArrayList);
                                             %>
                                            <table cellpadding="0" cellspacing="0">  
                                            <tr>
                                                <td>
                                                    <div style="height:600px;overflow:auto;">
                                                        <table>
                                                          <tr>
                                                              
                                                               <%
                                                    Object[] soHashMapArrayListObjects = newSoArrayList.toArray();
                                                   // System.out.println("==>Hellllllllllllllllll : " + sourceMergeHandler.getSoArrayList());
                                                    String cssClass = "dynaw169";
                                                    String cssMain = "maineuidpreview";
                                                    String menuClass = "menutop";
                                                    String dupfirstBlue = "dupfirst";
                                                    for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) {
                                                        if (countEnt > 0) {
                                                            cssClass = "dynaw169";
                                                            menuClass = "menutop";
                                                            dupfirstBlue = "dupfirst";
                                                        }
                                                        HashMap soHashMap = (HashMap) soHashMapArrayListObjects[countEnt];
                                                        //System.out.println("Hellllllllllllllllllllllllllllll"+soHashMap);
    LIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(soHashMap.get("LID"), soHashMap.get("LID").getClass());
                                                            %>
                                                               <!-- Display the field Values-->
                                                               <%if(countEnt ==0 ) {%>
                                                                  <td  valign="top">
                                                                          <div id="labelmainEuidContent" class="yellow">
                                                                               <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                                    <tr>
                                                                                       <td id="menu<%=soHashMap.get("LID")%>">&nbsp</td>
                                                                                    </tr> 
                                                                                    <tr>
                                                                                        <td valign="top"  id="Label<%=soHashMap.get("LID")%>"><b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b></td>
                                                                                    </tr>
                                                                               </table>
                                                                           </div>
                                                                          <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                                             <div id="labelpersonEuidDataContent" class="yellow">
                                                                                <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%
                                                        for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                                                                        %>  
                                                                                    <tr>
                                                                                      <td>
                                                                                         <%=fieldConfigMap.getDisplayName()%>                 
                                                                                      </td>
                                                                                    </tr>
                                                                        <%}%>
                                                             
                                                                                     <tr><td>&nbsp;</td></tr>
                                                                                     <tr><td>&nbsp;</td></tr>

                                                                                </table>
                                                                            </div>
                                                                          </div>
                                                                   </td>
                                                               <%}%>
                                                                  <td  valign="top">
                                                                     <div id="outerMainContentDivid<%=countEnt%>">
                                                            <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="yellow">
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td class="menutop"><b> LID&nbsp;<%=countEnt + 1%></b> </td>
                                                                    </tr> 
                                                                    <tr>
                                                                        <script> alllidsArray.push('<%=soHashMap.get("LID")%>')</script>
                                                                            <td valign="top" name="sri" id="curve<%=soHashMap.get("LID")%>">
                                                                            <a class="dupbtn" id="button<%=soHashMap.get("LID")%>" href="javascript:void(0)" onclick="javascript:collectLid('<%=soHashMap.get("LID")%>')">
                                                                                <%=soHashMap.get("LID")%>
                                                                            </a> 
                                                                            </td>
                                                                           <script> var thisText = document.getElementById('curve<%=soHashMap.get("LID")%>').innerHTML; alllidsactionText.push(thisText);</script> 
                                                                        </tr>
                                                                </table>
                                                            </div>
                                                                <div id="personEuidDataContent<%=soHashMap.get("LID")%>" class="yellow">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>"  >
                                                                        <%
                                                        HashMap personfieldValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT");
                                                        String epathValue;
                                                        for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                epathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
                                                            if (countEnt > 0) {
                                                                resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                                            } else {
                                                                resultArrayMapMain.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                                            }
                                                        
                                                                        %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
                                                                                       <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
        fnameExpression = ExpressionFactory.newInstance().createValueExpression(epathValue, epathValue.getClass());
        fvalueVaueExpression = ExpressionFactory.newInstance().createValueExpression(personfieldValuesMapEO.get(epathValue), personfieldValuesMapEO.get(epathValue).getClass());
                                                                %>
                                                                                            <font class="highlight"><%=personfieldValuesMapEO.get(epathValue)%></font>
                                                                                       <%} else {%>
                                                                                           <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                <%} else {%>
                                                                                     &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr><td>&nbsp;</td></tr>

                                                                    </table>
                                                                </div>
                                                                     </div>
                                                                  </td>
                                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length)   {%>
                                                                  <td  valign="top">
                                                                       <div id="preview<%=countEnt%>">
                                                                <%
                                                                          HashMap mergedSOMap = new HashMap();
                                                                          String styleclass = "yellow";
                                                                          HashMap previewpersonfieldValuesMapEO = new HashMap();
                                                                         if(request.getAttribute("mergedSOMap") != null) {
                                                                          mergedSOMap = (HashMap) request.getAttribute("mergedSOMap");
                                                                          previewpersonfieldValuesMapEO = (HashMap) mergedSOMap.get("SYSTEM_OBJECT");
                                                                          styleclass ="blue";
                                                                         } 
                                                                
                                                                %>
                                                                       
                                                            <div id="previewmainEuidContent" class="<%=styleclass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td id="previewmenu" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                                    </tr> 
                                                                        <tr>
                                                                            <td valign="top"  id="previewcurve">&nbsp;</td>
                                                                        </tr>
                                                                </table>
                                                            </div>
                                                        <div id="previewmainEuidContentButtonDiv">
                                                            <div id="assEuidDataContent">
                                                                <div id="personassEuidDataContent" class="<%=styleclass%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="previewbuttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%

                                                        String previewepathValue;
                                                        for (int ifcp = 0; ifcp < personConfigFeilds.length; ifcp++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifcp];
                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                previewepathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                previewepathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
                                                        
                                                                        %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if(request.getAttribute("mergedSOMap") != null) {%>
                                                                                    <%if (previewpersonfieldValuesMapEO.get(previewepathValue) != null) {%> 
                                                                                          <%=previewpersonfieldValuesMapEO.get(previewepathValue)%>
                                                                                    <%} else {%>
                                                                                        &nbsp;
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr><td>&nbsp;</td></tr>

                                                                    </table>
                                                                </div>
                                                                
                                                                <!--Displaying view sources and view history-->
                                                                
                                                            </div>
                                                        </div>
                                                    </div>
                                                                  </td>
                                                               <%}%>
                                                                <td>&nbsp;</td>                                                
                                                              <%}%>
                                                             
                                                          </tr>
                                                      </table>
                                                    </div>
                                                 </td>
                                             </tr>   

                                             <tr>
                                                 <td>
                                                     <table width="100%" cellpadding="0" cellspacing="0">
                                                       <tr>
                                                          <td colspan="<%=soHashMapArrayListObjects.length*2 + 3%>">
                                                             <div class="blueline">&nbsp;</div>
                                                          </td>   
                                                       </tr>
                                                      </table>
                                                 </td>
                                             </tr>
                                           <tr>
                                               <td>
                                                   <div id="actionmainEuidContent" class="actionbuton">
                                                   <table cellpadding="0" cellspacing="0">
                                           <% for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) { %>
                                               <% if (countEnt == 0)    { %>
                                                    <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                               <% }%>
                                                     <!--Displaying view sources and view history-->
                                                     <td valign="top">
                                                         <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" border="1">
                                                                        <h:form>
                                                                            <tr> 
                                                                                <td valign="top">
                                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}"
                                                                                                    action="#{NavigationHandler.toEuidDetails}" >  
                                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                                    </h:commandLink>                                                                                      

                                                                                </td>                                              
                                                                            </tr>
                                                                            
                                                                        </h:form>
                                                                    </table>
                                                            </div> 
                                                     </td>
                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length) { %>
                                                     <td>                                                                <!--Displaying view sources and view history-->
                                                         <div id="previewActionButton">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form  id="previewlid1Form">
                                                                                    <h:commandLink styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID1"><h:outputText value="Keep LID1"/></span>
                                                                                    </h:commandLink>
                                                                                    <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                                                                                    <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                                                                                </h:form>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form id="previewlid2Form">
                                                                                    <h:commandLink styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID2"><h:outputText value="Keep LID2"/></span>
                                                                                        <h:inputHidden id="previewhiddenLid2" value="#{SourceMergeHandler.formlids}" />
                                                                                        <h:inputHidden id="previewhiddenLid2source" value="#{SourceMergeHandler.lidsource}" />
                                                                                    </h:commandLink>
                                                                                </h:form>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>  
                                                         <div id="confirmationButton" style="visibility:hidden">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <a class="button" href="javascript:void(0)" onclick="javascript:showLIDDiv('mergeDiv',event)" > 
                                                                                   <span id="confirmok"><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                                </a>
                                                                            </td>
                                                                            <td>
                                                                                <a class="button" >
                                                                                   <span id="confirmcancel"><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>  



                                                     </td>
                                               <%}%>
                                               
                                            <%}%>
                                                   </table>
                                               </div>
                                                </td>
                                           </tr>
                                            </table>
                                            <%}%>
                                </div>
                              <%}%>
                            </div> <!-- End YUI content -->
                        </div> <!-- demo end -->
                    </td>
                </tr>
            </table>
            
        </div> <!--end source records div -->
         <!-- START Extra divs for add  SO-->
         <div id="mergeDiv" class="alert" style="top:500px;height:130px;left:560px;visibility:hidden">
             <h:form id="finalMergeForm">
                 <table cellspacing="0" cellpadding="0" border="0">
                     <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
                     <th align="right"><a href="javascript:void(0)" rel="mergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                      <tr>
                          <th align="center"><h:outputText value="#{msgs.lid_merge_popup_text}"/></th><th style="padding-right:160px;"><div id="confirmContent"></div></th>
                     </tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                    <tr>
                         <td colspan=2>
                             <h:commandLink styleClass="button" 
                                            action="#{SourceMergeHandler.mergePreviewSystemObject}">
                                 <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                             </h:commandLink>   
                             <h:outputLink  onclick="javascript:showExtraDivs('mergeDiv',event)" 
                                            styleClass="button"          
                                            value="javascript:void(0)">
                                 <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                             </h:outputLink>   
                             <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                             <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                         </td>
                     </tr>
                     
                 </table>
             </h:form>
         </div>		
	
       <!-- END Extra divs for add SO-->
       <!-- Start Extra divs for editing SO-->
   <h:dataTable  headerClass="tablehead" 
                 id="allChildNodesNames" 
                 var="childNodesName" 
                 value="#{SourceHandler.allChildNodesNames}">
        <h:column>
            <div id="extra<h:outputText value="#{childNodesName}"/>EditDiv" 
                 class="alertSource"  
                 style="TOP:580px;LEFT:450px;HEIGHT:<h:outputText value="#{SourceHandler.allNodeFieldConfigsSizes[childNodesName]}" />px;WIDTH:400px;visibility:hidden;">
             <h:form>
                <table>
                    <tr>
                        <td align="right" colspan="2">
                            <div>
                                <a href="javascript:void(0)" rel="editballoon<h:outputText value="#{childNodesName}"/>"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                            </div> 
                        </td>
                    </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="allNodeFieldConfigsMap" 
                                       var="allNodeFieldConfigsMap" 
                                       value="#{SourceHandler.allNodeFieldConfigs}">
                             <h:column>
                                 <h:dataTable  headerClass="tablehead" 
                                               id="childFieldConfigs" 
                                               var="childFieldConfig" 
                                               width="100%"
                                               rowClasses="odd,even"                                     
                                               value="#{allNodeFieldConfigsMap[childNodesName]}">
                                     
                                     <h:column>
                                         <h:outputText value="#{childFieldConfig.displayName}"  />
                                         <h:outputText value="*" rendered="#{childFieldConfig.required}" />
                                     </h:column>
                                     <!--Rendering HTML Select Menu List-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'MenuList'}" >
                                         <h:selectOneMenu value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}">
                                             <f:selectItem itemLabel="" itemValue="" />
                                             <f:selectItems  value="#{childFieldConfig.selectOptions}"  />
                                         </h:selectOneMenu>
                                     </h:column>
                                     <!--Rendering Updateable HTML Text boxes-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextBox'}" >
                                         <h:inputText label="#{childFieldConfig.displayName}"  
                                                      value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                      required="#{childFieldConfig.required}"/>
                                     </h:column>                     
                                     <!--Rendering Updateable HTML Text Area-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextArea'}" >
                                         <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                          value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                          required="#{fieldConfigAddAddress.required}"
                                                          />
                                     </h:column>
                                 </h:dataTable>                                                                                
                             </h:column>
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                          <a href="javascript:populateExtraDivs('<h:outputText value="#{childNodesName}"/>InnerDiv','add<h:outputText value="#{childNodesName}"/>Div','extra<h:outputText value="#{childNodesName}"/>AddDiv','add<h:outputText value="#{childNodesName}"/>DivClose')" class="button">
                            <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                          </a>    
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>EditDiv',event)" class="button"> 
                         <span><h:outputText value="#{msgs.cancel_but_text}"/> </span>
                          </a>    
                     </td>
                 </tr>
                 
                </table>
             </h:form>
            </div>
            
            <div id="extra<h:outputText value='#{childNodesName}'/>AddDiv" 
                 class="alertSource"  
                 style="TOP:1800px;LEFT:700px;HEIGHT:<h:outputText value="#{SourceHandler.allNodeFieldConfigsSizes[childNodesName]}" />px;WIDTH:400px;visibility:hidden;">
                <h:form>
                <table>
                    <tr>
                        <td align="right" colspan="2">
                            <div>
                                <a href="javascript:void(0)" rel="balloon<h:outputText value="#{childNodesName}"/>">
                                <h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                            </div>                               
                        </td>
                        
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <div id="<h:outputText value="#{childNodesName}"/>InnerDiv">
                                <h:dataTable  headerClass="tablehead" 
                                              id="allNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
                                        <h:dataTable  headerClass="tablehead" 
                                                      id="childFieldConfigsAdd" 
                                                      var="childFieldConfigAdd" 
                                                      width="100%"
                                                      rowClasses="odd,even"                                     
                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                            
                                            <h:column>
                                                <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                            </h:column>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu onblur="javascript:accumilateMinorObjectSelectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}')"
                                                                 value="">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'}" >
                                                <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                             onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                             onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             value=""
                                                             required="#{childFieldConfigAdd.required}"/>
                                            </h:column>                     
                                            <!--Rendering Updateable HTML Text Area-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                                 onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                                 required="#{fieldConfigAddAddress.required}"
                                                                 value=""/>
                                            </h:column>
                                        </h:dataTable>                                                                                
                                    </h:column>
                                </h:dataTable>                                                                                
                                
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <a href="javascript:minorObjTypeLocalCount++;javascript:populateMinorObjectsExtraDivs('<h:outputText value="#{childNodesName}"/>InnerDiv','add<h:outputText value="#{childNodesName}"/>Div','extra<h:outputText value="#{childNodesName}"/>AddDiv','add<h:outputText value="#{childNodesName}"/>DivClose',minorArrayLocal);" class="button">
                            <span><h:outputText value="#{msgs.ok_text_button}"/></span></a>    
                        </td>
                        <td>
                            <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>AddDiv',event)" class="button">
                            <span><h:outputText value="#{msgs.cancel_but_text}"/></span> </a>
                        </td>
                    </tr>
                </table>   
                </h:form>                 
            </div>   
        </h:column>                 
    </h:dataTable>
    <!-- End Extra divs for editing SO-->
     <!-- Start Extra divs for add SO-->
 <!-- End Extra divs for add SO-->                                                                                                                                       
     <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
                                             
<script>
(function() {
    var tabView = new YAHOO.widget.TabView('demo');

    YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
})();
</script>

<!--END SOURCE CODE FOR EXAMPLE =============================== -->
</div>
   <h:dataTable  headerClass="tablehead" 
                 id="allChildNodeschildBallon" 
                 var="childNodesName" 
                 value="#{SourceHandler.allChildNodesNames}">
           <h:column>
              <div id="balloon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
              <div id="addballoon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
              <div id="editballoon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
          </h:column>                 
    </h:dataTable>
    
        <%if( request.getAttribute("lids") != null) {
           
        String[] srcs  = (String[]) request.getAttribute("lids");
        String  lidsSource  = (String) request.getAttribute("lidsource");
        for(int i=0;i<srcs.length;i++) {
        %>    
        
        <script>
            collectLid('<%=srcs[i]%>'); 
            document.getElementById('confirmationButton').style.visibility = 'visible';
            document.getElementById("previewActionButton").style.visibility = "hidden";
            document.getElementById("previewActionButton").style.display = "none";                        
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "blue";
        </script>
        <%}%>
        <script>
            document.getElementById("confirmContent").innerHTML  = '<%=srcs[0]%>';
            document.getElementById("finalMergeForm:previewhiddenLid1").value  = '<%=srcs[0]+":" + srcs[1]%>';
            document.getElementById("finalMergeForm:previewhiddenLid1source").value  = '<%=lidsSource%>';
        </script>
        <%}%> 


</body>

        <%
          String[][] lidMaskingArray = sourceAddHandler.getAllSystemCodes();
          
          
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
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
    </script>
    <script>
          var formName ="";
    </script>
    <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicAddformData";
    </script>
    
    <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicMergeformData";
     </script>
      
    <%} else {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%}%>  
    
   <script>
         if( formName.elements[0]!=null) {
		var i;
		var max = formName.length;
		for( i = 0; i < max; i++ ) {
			if( formName.elements[ i ].type != "hidden" &&
				!formName.elements[ i ].disabled &&
				!formName.elements[ i ].readOnly ) {
				formName.elements[ i ].focus();
				break;
			}
		}
      }         
       
       
   </script>
   

</html>
</f:view>
