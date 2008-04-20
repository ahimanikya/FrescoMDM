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
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
 URI = URI.substring(1, URI.lastIndexOf("/"));%>

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
        <script type="text/javascript" src="./scripts/yui/animation/animation.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
        <script type="text/javascript" >
            var fieldNameValuesLocal="";
            var fieldNamesLocal="";
            var minorObjTypeLocal = "";
            var minorObjTypeLocalCount = 0;
            var minorArrayLocal = new Array();
			var editIndexid = "-1";


            function setEditIndex(editIndex)   {
				editIndexid = editIndex;
			}
            
           function cancelEdit(formName, thisDiv,minorObject)   {
                ClearContents(formName); 
                setEditIndex("-1");
				document.getElementById(thisDiv).style.visibility = 'hidden';
				document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(minorObject+'buttonspan').innerHTML = 'Save '+ minorObject;
		   }
	
	       function accumilateMinorObjectFieldsOnBlurLocal(objectType,field,fullFieldName,mask,valueType) {
                var maskChars = new Array();
                var str = mask;
                
                str  = str.replace(/D/g,"");
                
                maskChars = str.split('');
                
                var valueEntered =  "";
                valueEntered =  field.value;
                //valueEntered  = valueEntered.replace(/\)/g,"");
                //valueEntered  = valueEntered.replace(/\(/g,"");
                
                if(valueType != '6') {
                    for(var i=0;i<maskChars.length;i++) {
                        valueEntered  = valueEntered.replace(maskChars[i],'');
                    }    
                }    

                minorObjTypeLocal =   objectType;
                if(fieldNamesLocal != fullFieldName+':') {
                    fieldNamesLocal+=fullFieldName+':';
                }
                fieldNameValuesLocal += fullFieldName + "##"+valueEntered+ ">>" + minorObjTypeLocalCount + ">>";
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValuesLocal;
                
                fieldNameValuesLocal = fieldNameValuesLocal  + minorObjTypeLocal ;
                minorArrayLocal.push(fieldNameValuesLocal);
                //RESET THE FIELD VALUES HERE
                fieldNameValuesLocal = "";              
               //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArrayLocal;
                
            }
            var selectedValue;
            function accumilateMinorObjectSelectFieldsOnBlurLocal(objectType,field,fullFieldName,keyType) {
				//check if key type is already selected
				if(keyType == 'true' && selectedValue == field.options[field.selectedIndex].value) {
					alert("Cannot add " + objectType + "type '"+ field.options[field.selectedIndex].text +"' again. Please choose different value");
					field.selectedIndex =0;
					return false;
				}

                selectedValue = field.options[field.selectedIndex].value;
                minorObjTypeLocal =   objectType;
                if(fieldNamesLocal != fullFieldName+':') {
                    fieldNamesLocal+=fullFieldName+':';
                }
                fieldNameValuesLocal += fullFieldName + "##"+selectedValue + ">>" + minorObjTypeLocalCount +">>";
                //document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = fieldNameValuesLocal;
                
                fieldNameValuesLocal = fieldNameValuesLocal  + minorObjTypeLocal;
                minorArrayLocal.push(fieldNameValuesLocal);
                //RESET THE FIELD VALUES HERE
                fieldNameValuesLocal = "";
                
                ////document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = minorArrayLocal;
                 
            }
            
   </script>
        <!--there is no custom header content for this example-->
        
    </head>
    <%@include file="./templates/header.jsp"%>
   
    <% String validLid = "Not Validated";
       if(session.getAttribute("validation") != null ) {
         validLid = "Validated";
        } 
    %>
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
                                        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
                                        ArrayList searchResultsScreenConfigArray = (ArrayList) session.getAttribute("viewEditResultsConfigArray");
                                        ArrayList systemObjectsMapList = (ArrayList) session.getAttribute("systemObjectsMapList");
                                        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                                        ValueExpression LIDVaueExpression = null;
                                        ValueExpression sourceSystemVaueExpression = null;
                                        //ConfigManager.init();

                                        String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

                                        EPathArrayList ePathArrayList = new EPathArrayList();

                                        String mainDOB;
                                        SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");
                                        HashMap systemObjectMap = (HashMap) session.getAttribute("systemObjectMap");
                                        String keyFunction = (String) session.getAttribute("keyFunction");
                                        SourceHandler sourceHandler = new SourceHandler();
                                        Object[] personConfigFeilds = sourceHandler.getRootNodeFieldConfigs().toArray();
                                        SourceEditHandler sourceEditHandler = (SourceEditHandler)session.getAttribute("SourceEditHandler");
                                        SourceAddHandler  sourceAddHandler   = new SourceAddHandler();

									HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                             		SourceAddHandler  sourceAddHandlerFaces   = (SourceAddHandler)sessionFaces.getAttribute("SourceAddHandler");
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
                                                        <td>
														   <%=singleSystemObjectLID.getStatus()%> 
														</td>
                                                    </tr>                                                    
                                                    <tr class="even">
                                                        <td><h:outputText value="#{msgs.source_rec_sourcename_text}"/></td>
                                                        <td><%=sourceHandler.getSystemCodeDescription(singleSystemObjectLID.getSystemCode())%> </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td><%=localIdDesignation%></td>
                                                        <td><%=singleSystemObjectLID.getLID()%> </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="tablehead" colspan="2">
                                                          <b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b>
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
                                                          var="childNodesName" 
                                                          value="#{SourceHandler.allSOChildNodesLists}">
                                       <h:column>
                                              <p><h:outputText styleClass="tablehead" style="width:100%;" value="#{childNodesName['NAME']}"  /></p>
                                              <h:dataTable  headerClass="tablehead" 
                                                                  width="100%"
                                                                  id="sofieldConfigDPId" 
                                                                  var="childMapArrayList" 
                                                                  value="#{SourceHandler.singleSOHashMap[childNodesName['KEYLIST']]}">
                                                        <h:column>
                                                            <h:dataTable 
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
                                                                        actionListener="#{SourceAddHandler.editLID}" >
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
                                        <!-- Start Status div-->
                                        <div id='edistatusdisplay'>
                                            <table border=0 width="100%">
                                                <tr>
                                                    <td>
                                                        <h:form>
                                                            <table border="0" cellpadding="1" cellspacing="1" width="100%">
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <nobr>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                                            </h:commandLink>                                                                
                                                                        </nobr>
                                                                    </td>
                                                                </tr>  
                                                                <tr>
                                                                    <td><nobr><h:outputText value="Status"/> </nobr></td>
                                                                    <td><nobr>
																	   <%=singleSystemObjectLID.getStatus()%> 
																	   </nobr>
																	</td>
                                                                </tr>                                                    
                                                                <tr>
                                                                    <td><nobr><h:outputText value="System Code"/></nobr></td>
                                                                    <td><nobr><%=sourceHandler.getSystemCodeDescription(singleSystemObjectLID.getSystemCode())%> </nobr></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><nobr><%=localIdDesignation%></nobr></td>
                                                                    <td><nobr><%=singleSystemObjectLID.getLID()%> </nobr></td>
                                                                </tr>
                                                            </table>    
                                                        </h:form>
                                                    </td>
                                                    <td><div id="editFormValidate"></div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <!-- Status div-->
                                            <table width="100%">
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <%=objScreenObject.getRootObj().getName()%>&nbsp;Info                      
                                                    </td>
                                                </tr>
                                                    
                                                <tr>
                                                    <td align="left">
                                                        <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                        <h:form id="BasicSearchFieldsForm">
                                                          <!-- Start EDIT Fields-->
                                                          <!--Start Displaying the person fields -->                                        
                                                        <form id="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                            <h:dataTable  headerClass="tablehead"  
                                                                          id="hashIdEdit" 
                                                                          width="100%"                                                                  
                                                                          var="fieldConfigPerAdd" 
                                                                          value="#{SourceHandler.rootNodeFieldConfigs}">
                                                                <h:column>
                                                                    <h:outputText value="#{fieldConfigPerAdd.displayName}"  />
                                                                    <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
                                                                </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" 
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 id="fieldConfigIdTextbox"  
                                                                                 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                 required="#{fieldConfigPerAdd.required}"/>
                                                                </h:column>                     
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">
                                                                    
                                                                    <nobr><!--Sridhar -->
                                                                        <input type="text" 
                                                                               title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                                               value="<h:outputText value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
                                                                               id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                                               required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                                               maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                                                                               onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                               onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                                               onkeyup="javascript:qws_field_on_key_up(this)" >
                                                                        <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPerAdd.name}"/>')" > 
                                                                            <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </nobr>
                                                                        
                                                                        
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                                                                     title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     required="#{fieldConfigPerAdd.required}"
                                                                                     />
                                                                </h:column>
                                                                    
                                                            </h:dataTable>
                                                        </form>
                                                            
                                                        <!--End Displaying the person fields -->    
                                                        <!--Minor Object fields here -->     
                                                        <h:dataTable  headerClass="tablehead" 
                                                                                          id="allChildNodesNamesAdd" 
                                                                                          width="100%"
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
                                                                        <td colspan="2">
                                                                            <input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
                                                                        </td>
                                                                    </tr>
                                                                        
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <a href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddDiv');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?&MOT=<h:outputText value="#{childNodesName}"/>&load=load&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv','')" class="button">
                                                                            <span>
                                                                                <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;View <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
                                                                            </span>
                                                                                
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td>
                                                                            <div id="extra<h:outputText value='#{childNodesName}'/>AddDiv"  style="visibility:hidden;display:none;">
                                                                                <table>
                                                                                    <tr>
                                                                                        <td colspan="2" align="left">
                                                                                            <form id="<h:outputText value="#{childNodesName}"/>InnerForm" name="<h:outputText value="#{childNodesName}"/>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                                                                <h:dataTable  headerClass="tablehead" 
                                                                                                              id="allNodeFieldConfigsMapAdd" 
                                                                                                              var="allNodeFieldConfigsMapAdd" 
                                                                                                              width="100%"
                                                                                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                                                                                    <h:column>
                                                                                                        <h:dataTable  headerClass="tablehead" 
                                                                                                                      id="childFieldConfigsAdd" 
                                                                                                                      var="childFieldConfigAdd" 
                                                                                                                      width="100%"
                                                                                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                                                                                                          
                                                                                                            <h:column>
                                                                                                                <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                                                                                <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                                                                                            </h:column>
                                                                                                            <!--Rendering HTML Select Menu List-->
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                                                                             <h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" >
                                                                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                                                                                </h:selectOneMenu>
                                                                                                            </h:column>
                                                                                                            <!--Rendering Updateable HTML Text boxes-->
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                                                                                                <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                                                                       name = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                                                                       id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                                                                       required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                                                                       maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                                                                       onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')" 
                                                                                                                       onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                                                            </h:column>                     
                                                                                                                
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                                                                                                <nobr>
                                                                                                                    <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                                                                           id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                                                                           required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                                                                           maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                                                                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                                                                                           onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                                                           onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateMinorObjectFieldsOnBlurLocal('<h:outputText value="#{childFieldConfigAdd.objRef}"/>',this,'<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>','<h:outputText value="#{childFieldConfigAdd.inputMask}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')">
                                                                                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                                                                                        <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                                                                    </a>
                                                                                                                </nobr>
                                                                                                            </h:column>                     
                                                                                                                
                                                                                                                
                                                                                                            <!--Rendering Updateable HTML Text Area-->
                                                                                                                
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                                                                                <h:inputTextarea title="#{fieldConfigAddAddress.fullFieldName}"  
                                                                                                                                 onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                                                                                                 required="#{fieldConfigAddAddress.required}" />
                                                                                                            </h:column>
                                                                                                        </h:dataTable>                                                                                
                                                                                                    </h:column>
                                                                                                </h:dataTable>                                                                                
                                                                                                    
                                                                                            </form>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <!--EDIT SO buttons START-->
                                                                                    <tr>                                                                                                                
																					  <td colspan="2">
                                                                                           <nobr>
                                                                                                <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>InnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<%=singleSystemObjectLID.getLID()%>&SYS=<%=singleSystemObjectLID.getSystemCode()%>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv',event)">
                                                                                                     <span id="<h:outputText value='#{childNodesName}'/>buttonspan">Save <h:outputText value='#{childNodesName}'/> </span>
                                                                                                 </a>     
                                                                                                  <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}InnerForm');setEditIndex('-1')">
                                                                                                       <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                                                                   </h:outputLink> 
                                                                                                   <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelEdit">
                                                                                                      <a href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>InnerForm', '<h:outputText value='#{childNodesName}'/>cancelEdit', '<h:outputText value='#{childNodesName}'/>')">
                                                                                                          <span>Cancel <h:outputText value='#{childNodesName}'/></span>
                                                                                                       </a>     
                                                                                                    </div>
											                                                 </nobr>																		    
																					  </td>
																					</tr>
                                                                                    <!--EDIT SO buttons ENDS-->
                                                                                </table>   
                                                                            </div>  
                                                                    </td></tr>
                                                                        
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="stealth" style="visibility:hidden;display:none;"></div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="<h:outputText value="#{childNodesName}"/>NewDiv" >
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="<h:outputText value="#{childNodesName}"/>AddDiv"></div>
                                                                        </td>
                                                                    </tr>
                                                                        
                                                                </table>   
                                                            </h:column>
                                                        </h:dataTable>
                                                        <!-- End Display minor objects fields --> 
                                                        <!-- End Edit Acive SO -->
                                                         </h:form>
                                             
                                                          <!-- End EDIT Fields-->														   
                                                         <%} else if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                                   <!-- Start READ ONY Fields-->
                                                                   <!--Start Displaying the root node fields -->                                        
							
							   <h:dataTable  headerClass="tablehead"  
                                             id="hashIdEditDeactive" 
                                             width="100%"                                                                  
                                             var="fieldConfigPerAdd" 
                                             value="#{SourceHandler.rootNodeFieldConfigs}">
                                                                <h:column>
                                                                    <h:outputText value="#{fieldConfigPerAdd.displayName}"  />
                                                                    <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
                                                                </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" readonly="true" disabled="true"
                                                                    value="#{SourceHandler.deactivatedSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 id="fieldConfigIdTextbox"  
																				 readonly="true" disabled="true"
                                                                                 value="#{SourceHandler.deactivatedSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                 required="#{fieldConfigPerAdd.required}"/>
                                                                </h:column>                     
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">
                                                                    
                                                                    <nobr><!--Sridhar -->
                                                                        <input type="text" 
																		       readonly="true" disabled="true"
                                                                               title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                                               value="<h:outputText value="#{SourceHandler.deactivatedSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
                                                                               id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                                               required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                                               maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                                                                               onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                               onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                                               onkeyup="javascript:qws_field_on_key_up(this)" >
                                                                            <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                    </nobr>
                                                                        
                                                                        
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
																	                 readonly="true" disabled="true"
                                                                                     title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                     value="#{SourceHandler.deactivatedSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     required="#{fieldConfigPerAdd.required}"
                                                                                     />
                                                                </h:column>
                                                        </h:dataTable>
                                                         <!-- Start Display minor objects fields --> 
                                                        <h:dataTable  headerClass="tablehead" 
                                                                      id="allChildNodesNameDeactive" 
                                                                      width="100%"
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
                                                                 <h:dataTable id="childFieldDeactive" 
                                                                              var="minorojectsMapArrayList" 
                                                                              width="100%"
                                                                              value="#{SourceHandler.deactivatedSOHashMap[childNodesName['EDITKEYLIST']]}">
                                                                  <h:column>
                                                                     <h:dataTable id="childFieldDeactive" 
                                                                              var="childFieldConfigAdd" 
                                                                              width="100%"
                                                                              value="#{childNodesName['FIELDCONFIGS']}">
                                                                                <h:column>
                                                                                   <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                                                    <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                                                                 </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList' &&  childFieldConfigAdd.valueType ne 6}" >
                                                                    <h:selectOneMenu readonly="true" disabled="true" value="#{minorojectsMapArrayList[childFieldConfigAdd.fullFieldName]}" >
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                                                    <h:inputText readonly="true" disabled="true" 
								                 label="#{childFieldConfigAdd.displayName}"  
                                                                                 id="fieldConfigIdTextbox"   
                                                                                 maxlength="#{childFieldConfigAdd.maxLength}"
										 value="#{minorojectsMapArrayList[childFieldConfigAdd.fullFieldName]}" 
                                                                                 required="#{childFieldConfigAdd.required}"/>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType eq 6}">
                                                                    <h:inputText label="#{childFieldConfigAdd.displayName}"   
                                                                                 maxlength="#{childFieldConfigAdd.maxLength}"
						                                 readonly="true" disabled="true"
										 value="#{minorojectsMapArrayList[childFieldConfigAdd.fullFieldName]}"  
                                                                                 id="date" />
                                                                                 <h:graphicImage  id="calImgStartDate" 
                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                         url="./images/cal.gif"/>               
                                                                    </a>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea' &&  childFieldConfigAdd.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{childFieldConfigAdd.displayName}"  
                                                                                     id="fieldConfigIdTextArea"  
										     readonly="true" disabled="true"
                                                                                     value="#{minorojectsMapArrayList[childFieldConfigAdd.fullFieldName]}" 
                                                                                     required="#{childFieldConfigAdd.required}"/>
                                                                </h:column>
                                                                      </h:dataTable> <!--all child node values datatable-->
                                                                   </h:column>
                                                                 </h:dataTable><!--all child node Names values datatable-->
                                                                 </h:column>
                                                          </h:dataTable><!--all child node Names values datatable-->
                                                           <!-- End Display minor objects fields --> 
                                                           <!-- End READ ONLY Fields-->
                                                          <%}%>
                                                          <h:form>
                                                        <table>  
                                                            <tr>       
                                                                <% if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                                    
                                                                <td>
                                                                    <!-- Edit Submit button-->
                                                                    <a class="button" 
                                                                       href="javascript:void(0);"
                                                                       onclick="javascript:getFormValues('BasicSearchFieldsForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','editFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.source_rec_save_but}"/></span>
                                                                    </a>                                     
                                                                        
                                                                        
                                                                </td>                                                                
                                                                <td>
                                                                    <!-- Edit CANCEL button-->
                                                                    <h:commandLink  styleClass="button" 
                                                                                    action="#{SourceHandler.cancelEditLID}" >  
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
                                                        </h:form>    <!-- close Action button on Edit tab -->
                                                    </td>
                                                </tr>
                                            </table>
                                                
                                    </div>
                                    <%}%>
                                    <%} else {%>
                                    <!--START SEARCH CRITERIA-->
                                    <div id="sourceViewBasicSearch">
                                        <h:form id="basicViewformData">
                                            <h:inputHidden id="enteredFieldValues" value="#{SourceHandler.enteredFieldValues}"/>
                                                
                                            <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                            <table border="0" cellpadding="0" cellspacing="0">
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <td>
                                                        <h:dataTable id="fieldConfigId" var="feildConfig" headerClass="tablehead"  value="#{SourceHandler.viewEditScreenConfigArray}">
                                                            <!--Rendering Non Updateable HTML Text Area-->
                                                            <h:column>
                                                                <h:outputText value="#{feildConfig.displayName}" />
                                                                <h:outputText  value="*"  rendered="#{feildConfig.required}" /> 
                                                            </h:column> 
                                                                
                                                            <!--Rendering HTML Select Menu List-->
                                                            <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                                                <h:selectOneMenu rendered="#{feildConfig.name eq 'SystemCode'}"
onblur="javascript:accumilateFormSelectFieldsOnBlur('basicViewformData',this,'#{feildConfig.name}')"
onchange="javascript:setLidMaskValue(this,'basicViewformData')">
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                                    
                                                                <h:selectOneMenu onblur="javascript:accumilateFormSelectFieldsOnBlur('basicViewformData',this,'#{feildConfig.name}')"
                                                     rendered="#{feildConfig.name ne 'SystemCode'}">
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                                    
                                                            </h:column>
                                                                
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 }" >
                                                                <nobr>
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                   onblur="javascript:accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                   maxlength="#{feildConfig.maxLength}" 
                                                                                   rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   id="LID"
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   readonly="true"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, document.basicViewformData.lidmask.value)"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                   onblur="javascript:validateLidValue(this, 'basicViewformData');accumilateFormFieldsOnBlur(this,'#{feildConfig.name}',document.basicViewformData.lidmask.value,'#{feildConfig.valueType}','basicViewformData')"
                                                                                   rendered="#{feildConfig.name eq 'LID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                   onblur="accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                   maxlength="#{SourceHandler.euidLength}" 
                                                                                   rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                                       
                                                                                       
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                                                <nobr>
                                                                    <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   
                                                                                     onblur="accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                         
                                                                                     required="#{feildConfig.required}"/>
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                                                <nobr>
                                                                    <input type="text" 
                                                                           id = "<h:outputText value="#{feildConfig.name}"/>"  
                                                                           value="<h:outputText value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                                           required="<h:outputText value="#{feildConfig.required}"/>" 
                                                                           maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                                           onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                           onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateFormFieldsOnBlur('basicViewformData',this,'<h:outputText value="#{feildConfig.name}"/>')">
                                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{feildConfig.name}"/>')" > 
                                                                        <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                    </a>
                                                                </nobr>
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
                                                        <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>	
                                                </tr>
                                            </table>  
                                        </h:form>
                                    </div>                                                                 
                                    <!--END SEARCH CRITERIA-->
                                    <%}%>
                                    
                                </div>  
                              <%}%> 
                              <% if(operations.isSO_Add()){%> 
                                <div id="addTab">
                                    

                                        <table width="100%">
                                            <tr>
                                                <td>
                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
				                <h:form id="basicValidateAddformData">
                                                    <table width="100%">
                                                        <tr><td>
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
                                                                            <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicValidateAddformData')"
                                                                                              id="SystemCode" 
																							  title="SystemCode"
                                                                                              rendered="#{feildConfig.name eq 'SystemCode'}"
                                                                                              required="true">
                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                            </h:selectOneMenu>
                                                                        </nobr>
                                                                    </h:column>
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 && feildConfig.name ne 'LID' }" >
                                                                        <nobr>
                                                                            <h:inputText   required="true" 
                                                                                           label="#{feildConfig.displayName}" 
                                                                                           value=""/>
                                                                                           
                                                                        </nobr>
                                                                    </h:column>
                                                                    <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 && feildConfig.name eq 'LID'}" >
                                                                        <nobr>
                                                                            <h:inputText   id="LID"
																			               title="LID"
                                                                                           required="true" 
																						   readonly="true"
																						   label="#{feildConfig.displayName}" 
																						   maxlength="#{feildConfig.maxLength}"    onkeydown="javascript:qws_field_on_key_down(this, document.basicValidateAddformData.lidmask.value)"
                                                                                           onblur="javascript:validateLidValue(this, 'basicValidateAddformData');javascript:qws_field_on_key_down(this, document.basicValidateAddformData.lidmask.value);"
                                                                                           onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                           />
                                                                                           
                                                                        </nobr>
                                                                    </h:column>
                                                          </h:dataTable>
                                                     </td>
                                                     <td>
                                                        <div id="addFormValidate"> </div>    
                                                     </td>
                                                     </tr>
												</table>
                                               </h:form>
						
                                                  <div id="addFormFields" style="visibility:hidden;display:none;">
                                                    <!-- Start ADD  Fields-->
                                                    <table width="100%">
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
				                            <form id="<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                    <h:dataTable  headerClass="tablehead"  
                                                                  id="hashIdEdit" 
                                                                  width="100%"
                                                                  var="fieldConfigPerAdd" 
                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">
                                                        <h:column>
                                                            <h:outputText value="#{fieldConfigPerAdd.displayName}"  />
                                                            <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
                                                        </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                         id="fieldConfigIdTextbox"  
																		 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
																		  maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{fieldConfigPerAdd.required}"/>
                                                        </h:column>                     
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">

                                          <nobr>
                                            <input type="text" 
											       title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                   id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                   required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
												   onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                  onkeyup="javascript:qws_field_on_key_up(this)" >
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPerAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>


                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
															                  title="#{fieldConfigPerAdd.fullFieldName}"
                                                                             id="fieldConfigIdTextArea"   
                                                                             required="#{fieldConfigPerAdd.required}"
                                                                             />
                                                        </h:column>
                                                        
                                                    </h:dataTable>
                                                    </form>

                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="allChildNodesNamesAdd" 
                                                                  width="100%"
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
                                                                    <td colspan="2">
																    <input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
                                                                    </td>
                                                                </tr>
																
                                                                <tr>
                                                                    <td colspan="2">
																	<a href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')" class="button">
																	<span>
							                                            <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;Add <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
																	</span>

                                                                    </td>
                                                                </tr>
			<!--Minor objects loop starts-->
																<tr>
																<td>
              <div id="extra<h:outputText value='#{childNodesName}'/>AddNewDiv"  style="visibility:hidden;display:none;">
                <table>
                    <tr>
                        <td colspan="2" align="left">
                            <form id="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" name="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                <h:dataTable  headerClass="tablehead" 
                                              id="allNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              width="100%"
                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
                                        <h:dataTable  headerClass="tablehead" 
                                                      id="childFieldConfigsAdd" 
                                                      var="childFieldConfigAdd" 
                                                      width="100%"
                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                            
                                            <h:column>
                                                <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                            </h:column>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" >
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                            <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
											       name = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')" 
												   onkeyup="javascript:qws_field_on_key_up(this)" />
                                            </h:column>                     
 
                                     <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                  onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateMinorObjectFieldsOnBlurLocal('<h:outputText value="#{childFieldConfigAdd.objRef}"/>',this,'<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>','<h:outputText value="#{childFieldConfigAdd.inputMask}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{fieldConfigAddAddress.fullFieldName}"  
                                                                 onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                                 required="#{fieldConfigAddAddress.required}" />
                                            </h:column>
                                        </h:dataTable>                                                                                
                                    </h:column>
                                </h:dataTable>                                                                                
                                
                            </form>
                        </td>
                    </tr>
                    <!--Add New SO buttons START-->
					<tr>
					  <td>
                            <nobr>
                                <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
                                        <span id="<h:outputText value='#{childNodesName}'/>buttonspan">Save <h:outputText value='#{childNodesName}'/> </span>
                                 </a>     
                                  <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}AddNewSOInnerForm');setEditIndex('-1')">
                                          <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                   </h:outputLink>

                                    <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelSOEdit">
                                         <a href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm', '<h:outputText value='#{childNodesName}'/>cancelSOEdit', '<h:outputText value='#{childNodesName}'/>')">
                                          <span>Cancel <h:outputText value='#{childNodesName}'/></span>
                                         </a>     
                                   </div>
                             </nobr>
                      
					  </td>
					</tr>
                    <!--Add New SO buttons ENDS -->

                </table>   
            </div>   
			</td>
			<td><div id="<h:outputText value='#{childNodesName}'/>EditMessages" >   </div></td>
			</tr>
			<!--Minor objects loop ends-->

			 <tr>
				 <td colspan="2">
 					 <div id="stealth" style="visibility:hidden;heigh:0px"> </div>
				 </td>
		     </tr>
			 <tr>
 				 <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/>NewDiv" ></div>
				 </td>
			 </tr>
			 <tr>
			    <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/>AddNewSODiv"></div>
				 </td>
			 </tr>

              </table>   
           </h:column>
          </h:dataTable>
         </div>
		<!--End Add source record form-->
													<div id="validateButtons" style="visibility:visible;display:block">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a class="button" href="javascript:ClearContents('basicValidateAddformData');">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td>
                                                                </nobr>
                                                                <nobr>
                                                                    <a class="button" 
																	   href="javascript:void(0);"
																	   onclick="javascript:getFormValues('basicValidateAddformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&validate=true&rand=<%=rand%>','addFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.validate_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
                                                        </tr>
                                                    </table>
													</div>
													<div id="saveButtons" style="visibility:hidden;display:none">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a class="button" 
																   href="javascript:void(0);"
																   onclick="javascript:ClearContents('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');setEditIndex('-1')">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <nobr>
                                                                    <a class="button" 
																	   href="javascript:void(0);"
																	   onclick="javascript:getFormValues('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','addFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.submit_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
															<td>
                                                                <h:form>
																 <!-- Edit CANCEL button-->
                                                                    <h:commandLink  styleClass="button" 
                                                                                    action="#{SourceHandler.cancelSaveLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                                                      
                                                                </h:form>
                       

															</td>
                                                        </tr>
                                                    </table>
													</div>
                                             </td>   
                                          </tr>   
                                       </table>   
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
                                                                   <h:selectOneMenu  onchange="javascript:setLidMaskMergeValue(this,'basicMergeformData')"
                                                                                     id="sourceOption" 
                                                                                     value="#{SourceMergeHandler.source}" >
																	   <f:selectItem itemLabel="" itemValue="" />
                                                                       <f:selectItems  value="#{SourceMergeHandler.selectOptions}" />
                                                                   </h:selectOneMenu>
                                                               </td>
                                                               <input id='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />
                                                               
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                               <h:outputText value="#{msgs.source_merge_head1}"/>
                                                               <h:inputText value="#{SourceMergeHandler.lid1}" id="LID1"
                                                                            onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                            onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               <td>&nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head2}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}" id="LID2"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head3}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}" id="LID3"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head4}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid4}" id="LID4"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"/>  
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
                                             %>
                                            <table cellpadding="0" cellspacing="0">  
                                            <tr>
                                                <td>
                                                    <div style="height:600px;overflow:auto;">
                                                        <table>
                                                          <tr>
                                                              
                                                               <%
                                                    Object[] soHashMapArrayListObjects = newSoArrayList.toArray();
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
                                           <% ValueExpression soValueExpressionMerge = null;
                                           for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) { 
                                               HashMap soHashMapMerge = (HashMap) soHashMapArrayListObjects[countEnt];
                                               soValueExpressionMerge = ExpressionFactory.newInstance().createValueExpression(soHashMapMerge , soHashMapMerge.getClass());

                                               %>
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
                                                                                                    actionListener="#{SourceMergeHandler.viewEUID}"  
                                                                                                    action="#{NavigationHandler.toEuidDetails}" >  
                                                                                        <f:attribute name="soValueExpressionMerge" value="<%=soValueExpressionMerge%>"/>
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
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextBox' &&  childFieldConfig.valueType ne 6}" >
                                         <h:inputText label="#{childFieldConfig.displayName}"
													  maxlength="#{childFieldConfig.maxLength}"
                                                      value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                      required="#{childFieldConfig.required}"/>
                                     </h:column>                                     
									 <h:column rendered="#{childFieldConfig.guiType eq 'TextBox' &&  childFieldConfig.valueType eq 6}" >
                                         <h:inputText id="cal"
										              label="#{childFieldConfig.displayName}"  
										              maxlength="#{childFieldConfig.maxLength}"
                                                      value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                      required="#{childFieldConfig.required}"
													  onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfig.inputMask}')"
                                                       onkeyup="javascript:qws_field_on_key_up(this)" 
													  
													  
													  />
									         <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'cal')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
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

<form id="EditIndexForm" name="EditIndexForm">
		<input type="hidden" id="EditIndexFormID" value="-1" />
</form>


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
        function setLidMaskMergeValue(field,formName) {
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            
			var lidField1 =  getDateFieldName(formNameValue.name,'LID1');
			var lidField2 =  getDateFieldName(formNameValue.name,'LID2');
			var lidField3 =  getDateFieldName(formNameValue.name,'LID3');
			var lidField4 =  getDateFieldName(formNameValue.name,'LID4');

            document.getElementById(lidField1).value = "";
            document.getElementById(lidField2).value = "";
            document.getElementById(lidField3).value = "";
            document.getElementById(lidField4).value = "";



            /*
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField1).value = "";
			 document.getElementById(lidField1).readOnly = true;

			 document.getElementById(lidField2).value = "";
			 document.getElementById(lidField3).readOnly = true;
             
			 document.getElementById(lidField3).value = "";
			 document.getElementById(lidField3).readOnly = true;
             
			 document.getElementById(lidField4).value = "";
			 document.getElementById(lidField4).readOnly = true;
		    }
			*/

            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   

		function setLidMaskValue(field,formName) {
            var  selectedValue = field.options[field.selectedIndex].value;
			
            var formNameValue = document.forms[formName];
			
            var lidField =  getDateFieldName(formName,'LID');
            //document.getElementById(lidField).value = "";

			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
             document.getElementById(lidField).disabled = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).disabled = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
		
         }   

           function validateLidValue(field,formName) {
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');

            if(document.getElementById(lidField).value.length > 0 && document.getElementById(lidField).value.length != formNameValue.lidmask.value.length) {
			   alert("'" + document.getElementById(lidField).value + "' is invalid LID Format please change the value! Should be in '" + formNameValue.lidmask.value +"' Format");
			   //document.getElementById(lidField).value = "";
			}
			
         }   

         function resetLidFields(formName,validString) {
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');
            if(validString == 'Not Validated') {
              document.getElementById(lidField).value = "";
            }
         }


    </script>
    <script>
          var formName ="basicAddformData";
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
function addEvent(object,minorObjectdivId) {
  var ni = document.getElementById(object+'NewDiv');
  var numi = document.getElementById(object+"CountValue");
  var num = (document.getElementById(object+"CountValue").value -1)+ 2;
  numi.value = num;
  var divIdName = object+":"+num;
  var newdiv = document.createElement('div');
  newdiv.setAttribute("id",divIdName);
//  newdiv.innerHTML = "<table><tr><td>"+object +" Attr1 <input type='text' id='raj'> </td><tr></tr><td>"+object +" Attr 2<input type='text' id='raj'> //</td><tr></tr><td>"+object +" Attr3 <input type='text' id='raj'> </td><td><a href=\"javascript:;\" onclick=\"removeElement(\'"+divIdName+ //"\')\">Remove &quot;"+ object+"&quot;</a></td></tr><tr><td>&nbsp</td></tr></table>";

    //newdiv.innerHTML +=  "<table border=\"1\"><tr><td>";
    newdiv.innerHTML +=  document.getElementById(minorObjectdivId).innerHTML;
    //newdiv.innerHTML +=  "</td><td>";
    //newdiv.innerHTML +="<table  width=\"50%\" align=\"center\"><tr><td><a href=\"javascript:;\" class=\"button\" onclick=\"removeElement(\'"+divIdName+ "\')\"><span>Remove "+ object+"</span></a></td></tr></table>";
    //newdiv.innerHTML +=  "</td></tr></table>";
    
  ni.appendChild(newdiv);
  minorObjTypeLocalCount++;
    //<form name="extra<h:outputText value='#{childNodesName}'/>AddForm" ">
	//clear all the fields of the form after adding here
	//ClearMinorObjectContents(newdiv);
}

function removeElement(divNum) {
  var array = divNum.split(":");
  var d = document.getElementById(array[0]+'NewDiv');
  var olddiv = document.getElementById(divNum);
  d.removeChild(olddiv);
   minorObjTypeLocalCount--;
}

var minorObjectsArray = new Array();

function onclickCaptureAllMinorObjects() {
        //populate the minor object values here.
        minorObjectsArray.push(minorArrayLocal);
        var localArray = minorObjectsArray[minorObjectsArray.length-1];

        document.getElementById("basicAddformData:minorObjectsEnteredFieldValues").value = localArray;
        document.getElementById("basicAddformData:minorObjectTotal").value = minorObjTypeLocalCount;


}


</script>




</html>
</f:view>
