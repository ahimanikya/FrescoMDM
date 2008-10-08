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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>


<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
 URI = URI.substring(1, URI.lastIndexOf("/"));%>

 <%
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

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
		<script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>  
        <script type="text/javascript" >
           var fieldNameValuesLocal="";
           var fieldNamesLocal="";
           var minorObjTypeLocal = "";
           var minorObjTypeLocalCount = 0;
           var minorArrayLocal = new Array();
  	       var editIndexid = "-1";
           var userDefinedInputMask="";
 		   // LID merge related global javascript variables
		   var lids="";
           var lidArray = [];
           var alllidsArray = [];
           var alllidsactionText = [];
		   var editMinorObjectType ="";
  		   var tabName = "";

           function setEditIndex(editIndex)   {
		      editIndexid = editIndex;
	       }
		   function hideDivs(thisDiv){
 			    document.getElementById(thisDiv).style.visibility = 'hidden';
		        document.getElementById(thisDiv).style.display  = 'none';
		   }
		   function showDivs(thisDiv){
			    document.getElementById(thisDiv).style.visibility = 'visible';
		        document.getElementById(thisDiv).style.display  = 'block';
		   }   
 
           function cancelEdit(formName, thisDiv,minorObject)   {
                ClearContents(formName); 
                enableallfields(formName);
                setEditIndex("-1");
				editMinorObjectType = '';
		        document.getElementById(thisDiv).style.visibility = 'hidden';
		        document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(minorObject+'buttonspan').innerHTML = '<h:outputText value="#{msgs.source_rec_save_but}"/> '+ minorObject;
	     }
		// added by Bhat on 22-09-08  
		function setMinorObjectAddressType(MinorObjectType,editIndex,objectType){
			editMinorObjectType = MinorObjectType;
			editIndexid = editIndex;
		}
		
		// added by Bhat on 22-09-08		 
		function showUnSavedAlert(thisEvent,minorObjectType){
 			document.getElementById("unsavedMessageDiv").innerHTML = "<h:outputText value="#{msgs.unsaved_message_part_I}"/> '"+editMinorObjectType+"' <h:outputText value="#{msgs.unsaved_message_part_III}"/>";
 			showExtraDivs("unsavedDiv",thisEvent);
  		}
		// added by Bhat on 22-09-08		 
		function showMessage(messageText){
 			document.getElementById("unsavedMessageDiv").innerHTML = messageText;
			document.getElementById("unsavedDiv").style.visibility="visible";
			document.getElementById("unsavedDiv").style.display="block";
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
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab" onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title ="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"
									onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li class="selected"><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>"  href="#mergeTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"
									onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li class="selected"><a  title ="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"
									onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"
								onclick="javascript:if(editMinorObjectType.length>1){showUnSavedAlert(event,editMinorObjectType);}"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%}%>  
                            </ul>  
                            <%
                                        ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                                        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
                                        ArrayList searchResultsScreenConfigArray = (ArrayList) session.getAttribute("viewEditResultsConfigArray");
                                        ArrayList systemObjectsMapList = (ArrayList) session.getAttribute("systemObjectsMapList");
                                        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
                                        ValueExpression LIDVaueExpression = null;
                                        ValueExpression sourceSystemVaueExpression = null;
                                        //ConfigManager.init();
                                         //ResourceBundle bundle = //ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, //FacesContext.getCurrentInstance().getViewRoot().getLocale());
                                        String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

                                        EPathArrayList ePathArrayList = new EPathArrayList();

                                        String mainDOB;
                                        SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");
										HashMap systyemObjectAsHashMap  = new HashMap();
										
                                        HashMap systemObjectMap = (HashMap) session.getAttribute("systemObjectMap");
                                        String keyFunction = (String) session.getAttribute("keyFunction");
                                        SourceHandler sourceHandler = new SourceHandler();
                                        Object[] roorNodeFieldConfigs = sourceHandler.getRootNodeFieldConfigs().toArray();
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
                                <div id="viewEditTab">
                                  
                                     <!--START SEARCH CRITERIA-->

                                    <div id="sourceViewBasicSearch">
                                        <form id="basicViewformData" name="basicViewformData">
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
 onchange="javascript:setLidMaskValue(this,'basicViewformData')">
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                                    
                                                                <h:selectOneMenu  
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
                                                                                    rendered="#{feildConfig.name eq 'LID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                    maxlength="#{SourceHandler.euidLength}" 
                                                                                   rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                                       
                                                                                       
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                                                <nobr>
                                                                    <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   
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
                                                                           onblur="javascript:validate_date(this,'<%=dateFormat%>');">
                                                                    <a href="javascript:void(0);" 
												     title="<h:outputText value="#{feildConfig.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{feildConfig.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{feildConfig.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <f:facet name="footer">
                                                                <h:column>
																<!--  modified by Bhat on 24-09-08 to incorparate with ajax call-->
														<a title="<h:outputText value="#{msgs.patdetails_search_button2}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" 
														onclick="javascript:if(editMinorObjectType.length<1){
														getFormValues('<h:outputText value="#{childNodesName}"/>basicViewformData');
														ajaxMinorObjects('/<%=URI%>/ajaxservices/sourcerecordservice.jsf?'+queryStr+'&rand='+<%=rand%>+'&viewSO=true',
														'sourceRecordSearchResult',
														event);
														}else{showUnSavedAlert(event,editMinorObjectType);}">
														<span id="<h:outputText value='#{childNodesName}'/>buttonspan">
														<h:outputText value="#{msgs.patdetails_search_button2}" /></span>
														</a> 
                                                                                                       
																	<a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>"  class="button" href="javascript:ClearContents('basicViewformData');">
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
                                        </form>
                                    </div>                                                                 
                                    <!--END SEARCH CRITERIA-->
                                    <div id="sourceRecordSearchResult"></div>
									<div id="viewEuidDiv"></div>
                                     
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
                                                   <table border="0" cellpadding="1" cellspacing="1" width="100%">
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
														<td style="font-size:10px;" colspan="2">
															 <nobr>
																 <span style="font-size:12px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
															</nobr>
														</td>
													  </tr> 
                                                       <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
                                           <table width="100%" cellspacing="1"  border="0" cellpadding="1">
                                            <tr>
                                             <td colspan="2">
				                            <form id="<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                    <h:dataTable  headerClass="tablehead"  
                                                                  id="hashIdEdit" 
                                                                   var="fieldConfigPerAdd" 
                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">
                                                        <h:column>
															 <h:outputText rendered="#{fieldConfigPerAdd.required}">
																<span style="font-size:12px;color:red;verticle-align:top">*</span>
															</h:outputText>													  
															<h:outputText rendered="#{!fieldConfigPerAdd.required}">
																<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
															</h:outputText>													  
															<h:outputText value="#{fieldConfigPerAdd.displayName}" />
														    <h:outputText value=":"/>
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
												   onblur="javascript:validate_date(this,'<%=dateFormat%>');"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                  onkeyup="javascript:qws_field_on_key_up(this)" >
                                                  <a href="javascript:void(0);" 
												     title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
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
                                          </td>
										  </tr>
										  </table>
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
																	<!-- modified by Bhat on 22-09-08 to verify editMinorObjectType.length-->
																	<a title="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp; <h:outputText value="#{childNodesName}"/>" href="javascript:void(0)" onclick="javascript:
																	if(editMinorObjectType.length<1){showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')}
																	else{showUnSavedAlert(event,editMinorObjectType);}" class="button" >
																	
																	<span>
							                                            <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;Add <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
																	</span>
																	</a>

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
												 <h:outputText rendered="#{childFieldConfigAdd.required}">
													<span style="font-size:12px;color:red;verticle-align:top">*</span>
												</h:outputText>													  
												<h:outputText rendered="#{!childFieldConfigAdd.required}">
													<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
												</h:outputText>													  
												<h:outputText value="#{childFieldConfigAdd.displayName}" />
												<h:outputText value=":"/>
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
                                                  onblur="javascript:validate_date(this,'<%=dateFormat%>');">
                                                 <a href="javascript:void(0);" 
												     title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{childFieldConfigAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{childFieldConfigAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
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
                       <table cellpadding="0" cellspacing="0" border="0">
						<tr>
						<td>
 						   <a title="<h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp;<h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:editMinorObjectType='';getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
                           <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/> </span>
                           </a>
						</td>
						<td>
 						   <a title="<h:outputText value="#{msgs.clear_button_label}"/>" class="button"  href="javascript:void(0)" onclick="javascript:ClearContents('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');setEditIndex('-1');">
						   <span><h:outputText value="#{msgs.clear_button_label}"/></span>
						   </a>
						</td>
						<td>
						   <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelSOEdit">
                              <a title="<h:outputText value="#{msgs.source_rec_cancel_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm', '<h:outputText value='#{childNodesName}'/>cancelSOEdit', '<h:outputText value='#{childNodesName}'/>')">
                               <span><h:outputText value="#{msgs.source_rec_cancel_but}"/>&nbsp;<h:outputText value='#{childNodesName}'/></span>
                              </a>     
                            </div>
						</td>
						</tr>
					   </table>
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
  																<!-- modified by Bhat on 22-09-08 added for editMinorObjectType.length validation -->
                                                                    <a title ="<h:outputText value="#{msgs.validate_button_text}"/>" class="button" 
																	   href="javascript:void(0);"													
																	   onclick="javascript:if(editMinorObjectType.length<1){getFormValues('basicValidateAddformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&validate=true&rand=<%=rand%>','addFormValidate','');
																	   }else{showUnSavedAlert(event,editMinorObjectType)}" >  
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
                                                             <td><!-- modified by Bhat on 22-09-08 to verify editMinorObjectType.length-->
                                                                <a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>" class="button" 
																   href="javascript:void(0);"
																   onclick="javascript:if(editMinorObjectType.length<1){
																   ClearContents('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');
																   setEditIndex('-1');
																   }else{showUnSavedAlert(event,editMinorObjectType);}">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td><!-- modified by Bhat on 22-09-08 to verify editMinorObjectType.length-->
                                                                <nobr>
                                                                    <a title = "<h:outputText value="#{msgs.submit_button_text}"/>" class="button" 
																	   href="javascript:void(0);"
																	   onclick="javascript:if(editMinorObjectType.length<1){
																	   getFormValues('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','addFormValidate','');
																	   }else{showUnSavedAlert(event,editMinorObjectType);}" >  
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
                                                               <h:inputText value="#{SourceMergeHandler.lid1}" 
															        id="LID1"
															        title="<%=mergeLIDVaueExpression%>" 
 																    style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                    onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                    onkeyup="javascript:qws_field_on_key_up(this)"
															        onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"
																	/>  

																	<%
																	mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 2" ,  localIdDesignation.getClass());   	
																	%>
                                                               <td>
                                                                
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 2  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}" id="LID2" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																   onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
                                                               </td>																   <%
																	 mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 3" ,  localIdDesignation.getClass());   
																   %>
                                                               <td>
 
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%>3  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}" id="LID3" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
																				onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
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
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
																				onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
                                                               </td>
                                                            </tr>
                                                            <tr>
                                                               <td colspan="2">
                                                                   <nobr><!-- modified by Bhat on 22-09-08 added for editMinorObjectType.length validation -->
                                                                       <a title="<h:outputText value="#{msgs.source_merge_button}"/>"
                                                                          href="javascript:void(0)"
                                                                          onclick="javascript:if(editMinorObjectType.length<1){
																		  getFormValues('basicMergeformData');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&save=true&rand=<%=rand%>','sourceRecordMergeDiv','');}else{showUnSavedAlert(event,editMinorObjectType)}"  
                                                                          class="button" >
                                                                           <span><h:outputText value="#{msgs.source_merge_button}"/></span>
                                                                       </a>                                     
                                                                </nobr> 
                                                             <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('basicMergeformData')" >
                                                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                              </h:outputLink>
                                                               </td>
                                                               <td colspan="4">&nbsp;</td>
                                                           </tr>
                                                
                                                    </table>
							                       <input type="hidden" id="duplicateLid" title="duplicateLid" />
                                                    </h:form>
                                             <hr/>
											 <table>
											   <tr><td><div id="duplicateIdsDiv" class="ajaxalert"></div></td></tr>
											   <tr><td><div id="sourceRecordMergeDiv"></div></td></tr>
											   <tr><td><div id="sourceRecordEuidDiv"></div></td></tr>
											 </table>
 
 
                                </div>
                              <%}%>
                            </div> <!-- End YUI content -->
                        </div> <!-- demo end -->
                    </td>
                </tr>
            </table>
            
        </div> <!--end source records div -->
         <!-- START Extra divs for add  SO-->
		 <!-- Modified By Narahari.M on 27-09-2008, added banner and close link to confirmation pop up window -->
         <div id="mergeDiv" class="confirmPreview" style="top:500px;left:560px;visibility:hidden">
             <h:form id="mergeFinalForm">
                 <table cellspacing="0" cellpadding="0" border="0">
                     <tr><th align="center" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th> 
					     <th>
				          <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
                          <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('mergeDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
				        </th>
					</tr>
                      <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" style="color:#ffffff;">&nbsp;<h:outputText value="#{msgs.source_keep_btn}"/>&nbsp;<%=localIdDesignation%>&nbsp;&nbsp;<span style="color:#ffffff;font-weight:bold;" id="soMergeConfirmContent"></span>&nbsp;&nbsp;?</td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2">
						 <table align="center">
						<tr>
						<td>
                            <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&mergeFinal=true&rand=<%=rand%>','sourceRecordMergeDiv','');"  
                                class="button" >
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                             </a>
						</td>
						<td>
						     <a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}" />"  onclick="javascript:showExtraDivs('mergeDiv',event)">
										<span><h:outputText value="#{msgs.cancel_but_text}"/></span>
						     </a>
						</td>
						</tr>
						</table>
  							 <input type="hidden" id="mergeFinalForm:previewhiddenLid1" title="mergeFinalForm_LIDS" />
							 <input type="hidden" id="mergeFinalForm:previewhiddenLid1source" title="mergeFinalForm_SOURCE" />
							 <input type="hidden" id="mergeFinalForm:selectedMergeFields" title="mergeFinalForm_MODIFIED_VALUES" />
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


         <!-- Added By Narayan Bhat on 07-10-2008 for all information popups -->
  		 <div id="unsavedDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
               <form id="unsavedDivForm">
                <table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<th align="center" title="<%=bundle.getString("move")%>"><%=bundle.getString("popup_information_text")%></th>
				<th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedDiv',event);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedDiv',event);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
				</th>
				</tr>
                    <tr><td colspan="2">&nbsp;</td></tr>    
					<tr>
						<td colspan="2">
							<b><div id="unsavedMessageDiv"></div></b>
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>    
					<tr id="actions">
					  <td colspan="2" align="center">
					    <table align="center">
							<tr>
								<td>
									<a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.ok_text_button}" />" onclick="javascript:showExtraDivs('unsavedDiv',event);">                          
										<span><h:outputText value="#{msgs.ok_text_button}"/></span>
									</a>
								</td>
							</tr>
						</table>
					  </td>
					</tr>
                </table> 
                </form>
            </div> 

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
  <!-- Clear the merge form fields upoon load of the page -->  
  <script>
   ClearContents("basicMergeformData");
  </script>
</html>

 <script type="text/javascript">
  makeDraggable("mergeDiv");
  makeDraggable("unsavedDiv");
  makeDraggable("successDiv");
 </script>
</f:view>
