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
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
 URI = URI.substring(1, URI.lastIndexOf("/"));%>

<f:view>
 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       

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
            var userDefinedInputMask="";

            function setEditIndex(editIndex)   {
				editIndexid = editIndex;
			}
            
           function cancelEdit(formName, thisDiv,minorObject)   {
                ClearContents(formName); 
                setEditIndex("-1");
				document.getElementById(thisDiv).style.visibility = 'hidden';
				document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(minorObject+'buttonspan').innerHTML = '<h:outputText value="{msgs.source_rec_save_but}"/> '+ minorObject;
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
            
            var URI_VAL = '<%=URI%>';
			var RAND_VAL = '<%=rand%>';
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
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title ="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li class="selected"><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>"  href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li class="selected"><a  title ="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
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
                                         ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
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

									 CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                                        int addressSize;
                                        int phoneSize;
                                        int aliasSize;
                                        int commentSize;
                                        int dateCount=99999;
                                                        HashMap resultArrayMapCompare = new HashMap();
                                                        HashMap resultArrayMapMain = new HashMap();

                                                        ValueExpression fnameExpression;
                                                        ValueExpression fvalueVaueExpression;
                                                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
										String eoStatus= new String();
 									ValueExpression localIdDesignationVE = ExpressionFactory.newInstance().createValueExpression( localIdDesignation  ,  localIdDesignation.getClass()); 	
									%>
                            <div class="yui-content">
                              <% if(operations.isSO_SearchView()){%> 
                                <div id=viewEditTab">
                            
                                    <%if (singleSystemObjectLID != null) {
										eoStatus= compareDuplicateManager.getEnterpriseObjectStatusForSO(singleSystemObjectLID);%>
                                    <%if ("viewSO".equalsIgnoreCase(keyFunction)) {%>
                                    <h:form>
                                        <div  id="sourceViewBasicSearch">                                            
                                            <table border="0" width="100%">
                                                    <tr>
                                                        <td>
                                                            <h:commandLink title="#{msgs.source_rec_viewrecordslist_but}"  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                            </h:commandLink>                                                                                                 
                                                        </td>
                                                        <td style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><b><h:outputText value="#{msgs.source_rec_status_but}"/></b>:</td>
                                                        <td>
														   <%=singleSystemObjectLID.getStatus()%> 
														</td>
                                                        <td style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><b><h:outputText value="#{msgs.source_rec_sourcename_text}"/></b>:</td>
                                                        <td><%=sourceHandler.getSystemCodeDescription(singleSystemObjectLID.getSystemCode())%></td>
                                                        <td style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><b><%=localIdDesignation%></b>:</td>
                                                        <td><%=singleSystemObjectLID.getLID()%> </td>
                                                    </tr>
                                            </table>
                                            
                                            <!--Start Displaying the root node fields -->                                        
                                            <div class="minorobjects">                                                    
											   <table>
											     <tr>
												   <td>
												   <p><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</p>
												   <table>
												 <tr>
												   <td>
                                                    <h:dataTable  
                                                        id="hashId" 
                                                        var="fieldConfig" 
                                                        value="#{SourceHandler.rootNodeFieldConfigs}">
                                                            <h:column>
                                                                <h:outputText value="#{fieldConfig.displayName}"  />
                                                            </h:column>
                                                            <h:column>
                                                                <h:outputText value="#{SourceHandler.singleSOHashMap['SYSTEM_OBJECT'][fieldConfig.fullFieldName]}"  />
                                                            </h:column>
                                                    </h:dataTable>               
												</td>
												</tr>
                                                </table>
												   </td>
												 </tr>
												</table>
                                            </div>

                                     <!--End Displaying the root node fields -->    
                                     
                                    <!--End Displaying the minor object fields -->    
									<div class="minorobjects">
                                     <h:dataTable  headerClass="tablehead" 
                                                          id="allChildNodesNamesSoEdit" 
                                                          var="childNodesName" 
                                                          value="#{SourceHandler.allSOChildNodesLists}">
                                       <h:column>
                                              <p><h:outputText value="#{childNodesName['NAME']}"  /></p>
                                              <h:dataTable  headerClass="tablehead" 
                                                                  width="100%"
                                                                  id="sofieldConfigDPId" 
                                                                  var="childMapArrayList" 
                                                                  value="#{SourceHandler.singleSOHashMap[childNodesName['KEYLIST']]}">
                                                        <h:column>
                                                            <h:dataTable 
                                                                id="minorHashId" 
                                                                var="childFieldConfig" 
                                                                value="#{childNodesName['FIELDCONFIGS']}">
                                                                <h:column>
                                                                    <h:outputText value="#{childFieldConfig.displayName}"  />
                                                                </h:column>
                                                                <h:column>
                                                                    <h:outputText value="#{childMapArrayList[childFieldConfig.fullFieldName]}" rendered="#{!childFieldConfig.sensitive}" />
                                                                    <h:outputText value="#{msgs.SENSITIVE_FIELD_MASKING}" rendered ="#{childFieldConfig.sensitive}"   />
                                                                </h:column>
                                                            </h:dataTable>               
                                                        </h:column>
                                                    </h:dataTable>                                                             
                                       </h:column>
                                   </h:dataTable>
								   </div>

                                    <!--End Displaying the minor object fields -->    

                                    
                                            <table>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <%
                                                    ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());

                                                    %>
                                                    
                                                    <td>
                                                    <%if(eoStatus.equalsIgnoreCase("active")){ %>
													    <!--Display edit link only when the system object-->
                                                        <h:commandLink  title= "#{msgs.source_rec_edit_but}" styleClass="button" 
                                                                        action="#{NavigationHandler.toSourceRecords}" 
                                                                        rendered="#{Operations.SO_Edit}"
                                                                        actionListener="#{SourceAddHandler.editLID}" >
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>                
                                                            <span><h:outputText value="#{msgs.source_rec_edit_but}"/></span>
                                                        </h:commandLink>   
													 <% } else {%>
													        <input type="button" title="<h:outputText value="#{msgs.source_rec_edit_but}"/>"  disabled="true" readonly="true"  value="<h:outputText value="#{msgs.source_rec_edit_but}"/>"
                                                                             />
                                             
                                                
													 <%}%>
                                                    </td>
													
                                                    <td>
                                                        <h:commandLink title="#{msgs.source_rec_vieweuid_but}"  styleClass="button" 
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
                                                                            <h:commandLink title="#{msgs.source_rec_viewrecordslist_but}" styleClass="button" 
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
                                                                                 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                onfocus="javascript:clear_masking_on_focus()" required="#{fieldConfigPerAdd.required}"/>
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
                                                                        <a title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"  HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPerAdd.name}"/>')" > 
                                                                            <h:graphicImage  id="calImgDateFrom"  alt="#{fieldConfigPerAdd.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
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
                                                                            <a title="<h:outputText value="#{msgs.source_rec_view}"/>&nbsp;<h:outputText value="#{childNodesName}"/> " href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddDiv');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?&MOT=<h:outputText value="#{childNodesName}"/>&load=load&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv','')" class="button">
                                                                            <span>
                                                                                <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;View <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="<h:outputText value="#{msgs.source_submenu_add}"/>  <h:outputText value="#{childNodesName}"/>"/>
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
                                            <!--Rendering HTML Select Menu List-->
										  <!--user code related changes starts here-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <!-- User code fields here -->
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" onchange="getFormValues('#{childNodesName}InnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand=+RAND_VAL+&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
												rendered="#{childFieldConfigAdd.userCode ne null}">
												    <f:selectItem itemLabel="" itemValue="" />
                                                   <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>    
												
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" 
												                 rendered="#{childFieldConfigAdd.userCode eq null}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>

                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                           
                                                            <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, userDefinedInputMask)"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy ne null}"
																		 />     
																		 
																		 <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy eq null}"
																		 />

                                          </h:column>                     
										  <!--user code related changes ends here-->
                                          
										  <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                                                                                                <nobr>
                                                                                                                    <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                                                                           id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                                                                           required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                                                                           maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                                                                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                                                                                           onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                                                           onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateMinorObjectFieldsOnBlurLocal('<h:outputText value="#{childFieldConfigAdd.objRef}"/>',this,'<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>','<h:outputText value="#{childFieldConfigAdd.inputMask}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')">
                                                                                                                    <a  title ="<h:outputText value="#{childFieldConfigAdd.displayName}"/> HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                                                                                        <h:graphicImage  id="calImgDateFrom"  alt="#{childFieldConfigAdd.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                                                                    </a>
                                                                                                                </nobr>
                                                                                                            </h:column>                     
                                                                                                                
                                                                                                                
                                                                                                            <!--Rendering Updateable HTML Text Area-->
                                                                                                                
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                                                                                <h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
                                                                                                                                 required="#{childFieldConfigAdd.required}" />
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
                                                                                                <a title=" <h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/>"  href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>InnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<%=singleSystemObjectLID.getLID()%>&SYS=<%=singleSystemObjectLID.getSystemCode()%>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv',event)">
                                                                                                     <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/> </span>
                                                                                                 </a>     
                                                                                                  <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}InnerForm');setEditIndex('-1')">
                                                                                                       <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                                                                   </h:outputLink> 
                                                                                                   <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelEdit">
                                                                                                      <a title="<h:outputText value="#{msgs.source_rec_cancel_but}"/>  <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>InnerForm', '<h:outputText value='#{childNodesName}'/>cancelEdit', '<h:outputText value='#{childNodesName}'/>')">
                                                                                                          <span><h:outputText value="{msgs.source_rec_cancel_but}"/> <h:outputText value='#{childNodesName}'/></span>
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
                                                         <%} else if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus()) || "merged".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
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
																				 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
																				 onfocus="javascript:clear_masking_on_focus()"
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
                                                                                         alt="#{childFieldConfigAdd.displayName}" styleClass="imgClass"
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
                                                                    <a title="<h:outputText value="#{msgs.source_rec_save_but}"/>"  class="button" 
                                                                       href="javascript:void(0);"
                                                                       onclick="javascript:getFormValues('BasicSearchFieldsForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','editFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.source_rec_save_but}"/></span>
                                                                    </a>                                     
                                                                        
                                                                        
                                                                </td>                                                                
                                                                <td>
                                                                    <!-- Edit CANCEL button-->
                                                                    <h:commandLink title="#{msgs.cancel_but_text}"  styleClass="button" 
                                                                                    action="#{SourceHandler.cancelEditLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                                                      
                                                                </td>
                                                                <%}%>                                                                         
                                                                <td>                                                                   
                                                                    <h:commandLink title="#{msgs.source_rec_vieweuid_but}"  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}" 
                                                                                    action="#{NavigationHandler.toEuidDetails}" 
                                                                                    actionListener="#{SourceHandler.viewEUID}" >  
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink title="#{msgs.source_rec_deactivate_but}"  styleClass="button" rendered="#{Operations.SO_Deactivate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.deactivateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                    <%if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink title="#{msgs.source_rec_activate_but}"  styleClass="button" rendered="#{Operations.SO_Activate}"
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
                                                                <h:selectOneMenu title="SystemCode" rendered="#{feildConfig.name eq 'SystemCode'}"
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
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                   onblur="javascript:accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                   maxlength="#{feildConfig.maxLength}" 
                                                                                   rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   id="LID" 
                                                                                   label="#{feildConfig.displayName}" 
																				   title="<%=localIdDesignationVE%>"
                                                                                   readonly="true"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, document.basicViewformData.lidmask.value)"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                   onblur="javascript:accumilateFormFieldsOnBlur(this,'#{feildConfig.name}',document.basicViewformData.lidmask.value,'#{feildConfig.valueType}','basicViewformData')"
                                                                                   rendered="#{feildConfig.name eq 'LID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																				   onfocus="javascript:clear_masking_on_focus()"
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
                                                                    <a title="<h:outputText value="#{feildConfig.displayName}"/> HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{feildConfig.name}"/>')" > 
                                                                        <h:graphicImage  id="calImgDateFrom"  alt="#{feildConfig.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                    </a>
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                   
                                                                    <h:commandLink title="#{msgs.patdetails_search_button2}" styleClass="button" action="#{SourceHandler.performSubmit}" >  
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
																	<a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>"  class="button" href="javascript:ClearContents('basicViewformData')">
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
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
                                       <%if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                            <tr>
                                                <td>

                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
									   <%}%>
                                            <tr>
                                                <td>
				                <h:form id="basicValidateAddformData">
                                                    <table width="100%">
                                                        <tr><td>
                                                    <!--Start Add source record form-->
                                                    <input type="hidden" title="lidmask" name="lidmask" value="DDD-DDD-DDDD" />
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
                                                                                           onblur="javascript:qws_field_on_key_down(this, document.basicValidateAddformData.lidmask.value);"
                                                                                           onkeyup="javascript:qws_field_on_key_up(this)"
																						   onfocus="javascript:clear_masking_on_focus()"
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
																		 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
																		 onfocus="javascript:clear_masking_on_focus()"
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
                                                  <a title ="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"  HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPerAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="#{fieldConfigPerAdd.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
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
																	<a title="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp; <h:outputText value="#{childNodesName}"/>" href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')" class="button">
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
                                                <!-- User code fields here -->
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" 
												onchange="getFormValues('#{childNodesName}AddNewSOInnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand='+RAND_VAL+'&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
												rendered="#{childFieldConfigAdd.userCode ne null}">
												    <f:selectItem itemLabel="" itemValue="" />
                                                   <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>    
												
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" rendered="#{childFieldConfigAdd.userCode eq null}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                           
                                                            <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, userDefinedInputMask)"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		 onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
																		 onfocus="javascript:clear_masking_on_focus()"
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy ne null}"
																		 />     
																		 
																		 <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		  onfocus="javascript:clear_masking_on_focus()"
																		  onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
																		 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy eq null}"
																		 />


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
                                                  <a title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>" HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="#{childFieldConfigAdd.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
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
                                <a title="<h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
                                        <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/> </span>
                                 </a>     
                                  <h:outputLink  title ="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}AddNewSOInnerForm');setEditIndex('-1')">
                                          <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                   </h:outputLink>

                                    <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelSOEdit">
                                         <a title="<h:outputText value="#{msgs.source_rec_cancel_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm', '<h:outputText value='#{childNodesName}'/>cancelSOEdit', '<h:outputText value='#{childNodesName}'/>')">
                                          <span><h:outputText value="{msgs.source_rec_cancel_but}"/> <h:outputText value='#{childNodesName}'/></span>
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
                                                                </nobr>
                                                                <nobr>
                                                                    <a title ="<h:outputText value="#{msgs.validate_button_text}"/>" class="button" 
																	   href="javascript:void(0);"													
																	   onclick="javascript:getFormValues('basicValidateAddformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&validate=true&rand=<%=rand%>','addFormValidate','');" >  
<!--- Validate Button -->
                                                                         <span><h:outputText value="#{msgs.validate_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
                                                            <td>
                                                                <a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>" class="button" href="javascript:ClearContents('basicValidateAddformData');">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </table>
													</div>
													<div id="saveButtons" style="visibility:hidden;display:none">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>" class="button" 
																   href="javascript:void(0);"
																   onclick="javascript:ClearContents('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');setEditIndex('-1')">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <nobr>
                                                                    <a title = "<h:outputText value="#{msgs.submit_button_text}"/>" class="button" 
																	   href="javascript:void(0);"
																	   onclick="javascript:getFormValues('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','addFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.submit_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
															<td>
                                                                <h:form>
																 <!-- Edit CANCEL button-->
                                                                    <h:commandLink title="#{msgs.cancel_but_text}" styleClass="button" 
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
                                       <%if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                            <tr>
                                                <td>

                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
									   <%}%>

                                            <tr>
                                                <td>
                                                   <h:form id="basicMergeformData">
                                                     <table border="0" cellpadding="4" cellspacing="4">
                                                           <tr>
                                                               <td>
                                                                   <h:outputLabel style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   for="#{msgs.transaction_source}" value="#{msgs.transaction_source}"/>
                                                                </td>
                                                               <td>
                                                                   <h:selectOneMenu title="#{msgs.transaction_source}" onchange="javascript:setLidMaskMergeValue(this,'basicMergeformData')"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"id="sourceOption" 
                                                                   value="#{SourceMergeHandler.source}" >
																	   <f:selectItem itemLabel="" itemValue="" />
                                                                       <f:selectItems  value="#{SourceMergeHandler.selectOptions}" />
                                                                   </h:selectOneMenu>
                                                               </td>
                                                               <input id='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />           
                                                               <td>
                                                               <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 1  </font>
																   <%
																	ValueExpression mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 1" ,  localIdDesignation.getClass());   
																   %>
                                                               <h:inputText value="#{SourceMergeHandler.lid1}" id="LID1" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                    onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                    onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               <td>
                                                                
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 2  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}" id="LID2" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               </td>																   <%
																	 mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 3" ,  localIdDesignation.getClass());   
																   %>
                                                               <td>
 
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%>3  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}" id="LID3" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               </td>
                                                                   <%
																	 mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 4" ,  localIdDesignation.getClass());   
																   %>
                                                               <td>
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 4  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid4}" id="LID4" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"/>  
                                                               </td>
 </tr>
  <tr>
                                                               <td colspan="6">
															   <nobr>
                                                                       <h:commandLink title="#{msgs.source_merge_button}" styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                                       action="#{SourceMergeHandler.performLidMergeSearch}">
                                                                           <span><h:outputText value="#{msgs.source_merge_button}"/></span>
                                                                       </h:commandLink>                                     
                                                                   </nobr>  
                                                               </td>
															   <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('basicMergeformData')" >
                                                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                               </h:outputLink>
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
                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                   int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                                    int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                                   %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
                                                                    <%
                                                                      for (int max = 0; max< maxMinorObjectsMAX; max++) {
																    %>

																  <%
                               		 		                       for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%=fieldConfigMap.getDisplayName()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // field config loop
																	  %>

																	<%
                                                                      } // max minor objects loop
																 	 %>
																	<%
                                                                      } // max minor objects loop
																 	 %>

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
                                                                        <td class="menutop"><b> <%=localIdDesignation%>&nbsp;<%=countEnt + 1%></b> </td>
                                                                    </tr> 
                                                                     <tr>
                                                                    <script> alllidsArray.push('<%=soHashMap.get("LID")%>')</script>
                                                                    <td valign="top" name="sri" id="curve<%=soHashMap.get("LID")%>">
                                                                   <% if(request.getAttribute("mergedSOMap") != null ) { %>
                                                                     <span title ="<%=soHashMap.get("LID")%>" class="dupbtn" >
                                                                     <%=soHashMap.get("LID")%>
                                                                     </span>
                                                                     <%} else {%>
                                                                     <a title ="<%=soHashMap.get("LID")%>" class="dupbtn" id="button<%=soHashMap.get("LID")%>" href="javascript:void(0)" onclick="javascript:collectLid('<%=soHashMap.get("LID")%>','<%=bundle.getString("source_keep_btn") + " " + localIdDesignation%>')">
                                                                     <%=soHashMap.get("LID")%>
                                                                     <script> var thisText = document.getElementById('curve<%=soHashMap.get("LID")%>').innerHTML; alllidsactionText.push(thisText);</script>
                                                                      </a>
                                                                  <%}%>
                                                                 </td>
                                                                 </tr>
                                                                </table>
                                                            </div>
                                                                <div id="personEuidDataContent<%=soHashMap.get("LID")%>" class="yellow">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>"  >
                                                                        <%
                                                        HashMap personfieldValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT");
                                                        HashMap codesValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT_EDIT");
                                                        String epathValue = new String();
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
																     <div id="highlight<%=soHashMap.get("LID")%><%=soHashMap.get("SYSTEM_CODE")%>:<%=epathValue%>">
																            <a title="<%=personfieldValuesMapEO.get(epathValue)%>" href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=soHashMap.get("LID")%><%=soHashMap.get("SYSTEM_CODE")%>:<%=epathValue%>')" >
                                                                                            <font class="highlight"><%=personfieldValuesMapEO.get(epathValue)%></font>
																							</a>
                                                                                       </div>
																     <div id="unHighlight<%=soHashMap.get("LID")%><%=soHashMap.get("SYSTEM_CODE")%>:<%=epathValue%>"
																	 style="visibility:hidden;display:none">
																	                  <%=personfieldValuesMapEO.get(epathValue)%>
																	 </div>
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
                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
																	   %>
                                                                    <tr><td>&nbsp;</td></tr>

																	   <%
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                                    ArrayList  minorObjectMapList =  (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                    int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                 int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
												 int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;

                                                 FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
			 //if (maxMinorObjectsDiff > 0 ) {
			 %>

		     <%
								for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                   %>
																    
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                     epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
																				<%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b><%=minorObjectHashMap.get(epathValue)%></b>
																				<%} else {%>
																				   <%=minorObjectHashMap.get(epathValue)%>
																				<%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }//field config loop
								       } //minor objects values list
                                                                      %>

                                                                    <%
								    for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								   %>

								 <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
                                                                        }//Extra minor objects loop
								 %>


								 <%  
                                                                    } //total minor objects loop
                                                                    %>

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
													                      ArrayList mergePreviewMinorObjectList = new ArrayList();
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
                                                                            <td valign="top"  id="previewcurve">
																			<%if(request.getAttribute("mergedSOMap") != null) {%>
																			  <b><%=previewpersonfieldValuesMapEO.get("LID")%></b>
																			<%} else {%>
																			   &nbsp;
																			<%}%>
																			</td>
                                                                        </tr>
                                                                </table>
                                                            </div>
                                                        <div id="previewmainEuidContentButtonDiv">
                                                            <div id="assEuidDataContent">
                                                                <div id="personassEuidDataContent" class="<%=styleclass%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="previewbuttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%

                                                        String previewepathValue = new String();
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
                                                                                          <span id="<%=previewepathValue%>"><%=previewpersonfieldValuesMapEO.get(previewepathValue)%></span>
                                                                                    <%} else {%>
                                                                                        <span id="<%=previewepathValue%>">&nbsp;</span>
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr><td>&nbsp;</td></tr>
                                                                   <%
                                                                   HashMap minorObjectHashMapPreview = new HashMap();
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                                    int  maxMinorObjectsMinorDB =  (request.getAttribute("mergedSOMap")  != null) ?((Integer) mergedSOMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue():0;
								    int  maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;
								    mergePreviewMinorObjectList = (request.getAttribute("mergedSOMap")  != null)?(ArrayList) mergedSOMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList"):new ArrayList();
                                                                   %>
                                                                    <%
                                                                     for(int ar = 0; ar < mergePreviewMinorObjectList.size() ;ar ++) {
                                                                       minorObjectHashMapPreview = (HashMap) mergePreviewMinorObjectList.get(ar);                                                                    %>
                                                                  
								    <%
                               		 		            for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
								       previewepathValue = fieldConfigMap.getFullFieldName();
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if(request.getAttribute("mergedSOMap") != null) {%>
                                                                                    <%if (minorObjectHashMapPreview.get(previewepathValue) != null) {%> 
                                                                                          <span id="<%=previewepathValue%>"><%=minorObjectHashMapPreview.get(previewepathValue)%></span>
                                                                                    <%} else {%>
                                                                                        <span id="<%=previewepathValue%>">&nbsp;</span>
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // field config loop
								    %>

								    <%
                                                                      } // max minor objects loop
								     %>

                                    <%
								    for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								   %>

								   <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                    %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
                                                                        }//Extra minor objects loop
								  %>

                                                                        <tr><td>&nbsp;</td></tr>
                                                                     
							       <%
                                                                     } // all child nodes loop
								   %>

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
											   HashMap soHashMap = (HashMap) soHashMapMerge.get("SYSTEM_OBJECT");
                                               soValueExpressionMerge = ExpressionFactory.newInstance().createValueExpression(soHashMap, soHashMap.getClass());

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
                                                                                    <h:commandLink title="#{msgs.source_rec_vieweuid_but}"  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}"
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
                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length) { 
																			String keepLid1 = bundle.getString("source_keep_btn") + " " + localIdDesignation + " 1" ;
																			 ValueExpression keepLid1ValueExpression = ExpressionFactory.newInstance().createValueExpression(keepLid1, keepLid1.getClass());
																			String keepLid2 = bundle.getString("source_keep_btn") + " " + localIdDesignation + " 2" ;
ValueExpression keepLid2ValueExpression = ExpressionFactory.newInstance().createValueExpression(keepLid2, keepLid2.getClass());
																			%>
                                                     <td>                                                                <!--Displaying view sources and view history-->
                                                         <div id="previewActionButton">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form  id="previewlid1Form">
                                                                                    <h:commandLink id="lid1Link" title="<%=keepLid1ValueExpression%>" styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID1"> <%=keepLid1%>  </span>
                                                                                    </h:commandLink>
                                                                                    <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                                                                                    <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                                                                                </h:form>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form id="previewlid2Form">
                                                                                    <h:commandLink id="lid2Link" title="<%=keepLid2ValueExpression%>" styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID2"><%=keepLid2%></span>
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
                                                                                <a title='<h:outputText value="#{msgs.ok_text_button}" />' class="button" href="javascript:void(0)" onclick="javascript:showLIDDiv('mergeDiv',event)" > 
                                                                                   <span id="confirmok"><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                                </a>
                                                                            </td>
                                                                            <td>
                                                                               <h:form>
                                                                                    <h:commandLink  title ="#{msgs.cancel_but_text}" styleClass="button"   rendered="#{Operations.SO_Merge}"
                                                                                    action="#{SourceMergeHandler.cancelMergeOperation}" >  
                                                                                   <span id="confirmcancel"><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                                                    </h:commandLink>                                                 
                                                                               </h:form>
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
             <h:form id="mergeFinalForm">
                 <table cellspacing="0" cellpadding="0" border="0">
                     <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
                    </tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                      <tr>
                          <th  align="center" valign="top"><h:outputText value="#{msgs.lid_merge_popup_text}"/></th><th style="padding-right:160px;"><div id="confirmContent"></div></th>
                     </tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                    <tr>
                         <td colspan=2  align="center" valign="top">
                             <h:commandLink title ="#{msgs.ok_text_button}"  styleClass="button" 
                                            action="#{SourceMergeHandler.mergePreviewSystemObject}">
                                 <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                             </h:commandLink>   
                             <h:outputLink title="#{msgs.cancel_but_text}"  onclick="javascript:showExtraDivs('mergeDiv',event)" 
                                            styleClass="button"          
                                            value="javascript:void(0)">
                                 <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                             </h:outputLink>   
                             <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                             <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                             <h:inputHidden id="selectedMergeFields" value="#{SourceMergeHandler.selectedMergeFields}" />
                         </td>
                     </tr>
                     
                 </table>
             </h:form>
         </div>		
	
       <!-- END Extra divs for add SO-->
       <!-- Start Extra divs for editing SO-->
  
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
    <!--Fix for Bug : 6692060 (By Sridhar) START-->
        <%if( request.getAttribute("mergeComplete") != null) {%>
		     <script>
		      document.getElementById('confirmationButton').style.visibility = 'hidden';
		      document.getElementById('confirmationButton').style.display = 'none';
       		 </script>
        <%} else if( request.getAttribute("lids") != null) {           
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
            document.getElementById("confirmContent").innerHTML  = '<%=srcs[1]%>';
            document.getElementById("mergeFinalForm:previewhiddenLid1").value  = '<%=srcs[0]+":" + srcs[1]%>';
            document.getElementById("mergeFinalForm:previewhiddenLid1source").value  = '<%=lidsSource%>';
        </script>
        <%}%> 
	  <!--Fix for Bug : 6692060 (By Sridhar) ENDS -->

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

           function validateLidValue(formName) {
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');

            if(document.getElementById(lidField).value.length > 0 && document.getElementById(lidField).value.length != formNameValue.lidmask.value.length) {
			   alert("'" + document.getElementById(lidField).value + "' is invalid LID Format please change the value! Should be in '" + formNameValue.lidmask.value +"' Format");
			   //document.getElementById(lidField).value = "";
			   return false;
			}
			return true;
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
