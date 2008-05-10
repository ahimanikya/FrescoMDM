<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:view>
   
    <html>
        <head>
           <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
        <!-- Additional source files go here -->
        
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">    
        <script type="text/javascript" src="scripts/balloontip.js"></script>       
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
            
        </head>
        <title><h:outputText value="#{msgs.application_heading}" /></title>
        <body>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden;">
                <div id="basicSearch" class="basicSearch" style="visibility:visible;display:block;">
                    <h:form id="potentialDupBasicForm">
                        <table border="0" cellpadding="0" cellspacing="0" width="90%" align="left"> 
                            <tr>
                                <td align="left">
                                    <h:outputText value="#{msgs.datatable_euid_text}"/>
                                </td>
                                   <td align="left">
                                    <h:inputText   
                                        id="euidField"
                                        value="#{PatientDetailsHandler.singleEUID}" 
                                        maxlength="10"/>
                                </td>
                                <td>                                    
                                    <h:commandLink  styleClass="button" action="#{PatientDetailsHandler.singleEuidSearch}">  
                                        <span><h:outputText  value="#{msgs.search_button_label}"/> </span>
                                    </h:commandLink>                                     
                                    <h:commandLink  styleClass="button" action="#{NavigationHandler.toDuplicateRecords}">  
                                        <span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                       </span>
                                    </h:commandLink>                                     
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">                                    
                                    <h:messages styleClass="errorMessages"  layout="list" />
                                </td>
                            </tr>
                        </table>
                    </h:form>
                </div>
             <br>        
                <div id="mainDupSource" class="compareResults">
                    <table cellspacing="5" cellpadding="0" border="0">
                        <tr>
                            <td>
                                <div style="height:500px;overflow:auto;">
                                    <table cellspacing="5" cellpadding="0" border="0">
                                        <tr>
                                            
                                            <%
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();
            SourceHandler sourceHandler = new SourceHandler();

            
            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            
            int countEnt = 0;

            ArrayList eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;

            if (eoArrayList != null) {
                //request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));
                                            %>  
                                            <!-- Display the field Names first column-->
                                            <!--end displaying first column-->       
                                           <%
                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                            String dupHeading = "Main Euid";
                                            String cssMain = "maineuidpreview";
                                            String cssClass = "dynaw169";
                                            String cssHistory = "differentHistoryColour";
                                            String cssSources = "differentSourceColour";
                                            String cssDiffPerson = "differentPersonColour";
                                            String menuClass = "menutop";
                                            String dupfirstBlue = "dupfirst";
                                            String styleClass="yellow";
                                            String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                            String mainEUID = new String();
                                            if (eoArrayListObjects.length == 1) {
                                              styleClass = "blue";
                                            }
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                HashMap codesValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_CODES");

                                              //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);

                                                //int weight = (eoHashMapValues.get("Weight") != null)?((Float) eoHashMapValues.get("Weight")).intValue():0;
                                                String  weight =  (eoHashMapValues.get("Weight") !=null)?eoHashMapValues.get("Weight").toString():"0";

                                                String potDupStatus = (String) eoHashMapValues.get("Status");
                                                String potDupId = (String) eoHashMapValues.get("PotDupId");
                                                if (countEnt > 0) {
                                                    dupHeading = "<b> " + countEnt + "<sup>" + subscripts[countEnt] + "</sup> Duplicate</b>";
                                                } else if (countEnt == 0) {
                                                    dupHeading = "<b> Main EUID</b>";
                                                    mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                }

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
                   %>

                                          <%if (countEnt == 0) {%>
                                            <td  valign="top" align="left">
                                                <div id="outerMainContentDivid" style="visibility:visible;display:block;">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=cssMain%>">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>">
                                                            <div id="personassEuidDataContent" >
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                                    <tr>
                                                                    <%

                                                                String mainDOB;
                                                                ValueExpression fnameExpression;
                                                                ValueExpression fvalueVaueExpression;
                                                                String epathValue;

                                                                for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                                  FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%=fieldConfigMap.getDisplayName()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                     }
                                                                    %>
                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

                                                                   %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
                                                                    <%
												                    for (int max = 0; max< maxMinorObjectsMAX; max++) {
                                                                       for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                    %>  
                                                                       <tr>
                                                                          <td>
                                                                              <%=fieldConfigMap.getDisplayName()%>
                                                                          </td>
                                                                       </tr>
                                                                    <%
                                                                      }//FIELD CONFIG LOOP
																	%>
                                                                     <tr><td>&nbsp;</td></tr>

																	 <%
                                                                     } // MAX MINOR OBJECTS LOOP
																	 %>

																	<%
                                                                     } // CHILD OBJECTS LOOP
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>     
                                            <!-- START Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>">
                                                <div style="width:170px;overflow:hidden">
                                                <%
                                                if (countEnt > 0 && ("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
                                                 %>
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="deactivate" >
                                            <%} else {%>        
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>" >
                                            <%}%>        
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                     <td valign="top" align="left" class="dupfirst">
                                                                      <%
                                                                       if (countEnt > 0 && ("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
                                                                      %>
                                                                          <%=personfieldValuesMapEO.get("EUID")%>
                                                                     <%}else{%>                       
                                                                        <a class="dupbtn" 
                                                                           id="clickButton<%=personfieldValuesMapEO.get("EUID")%>" 
                                                                           href="javascript:void(0)" 
                                                                           onclick="javascript:accumilateMultiMergeEuids('<%=personfieldValuesMapEO.get("EUID")%>')">
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </a>
                                                                     <%}%>                       
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
                                                <% if (session.getAttribute("eocomparision") == null && countEnt > 0) {%>
                                         <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar" style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar" style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar" style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar" style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
                                                <%}%>    

                                                 <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                <%
                                                if (countEnt > 0 && ("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
                                                                                                                                                     
                                                 %>
                                                         <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="deactivate">
                                                  <%} else {%>            
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                  <%}%>          
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                    <%

                                    String mainDOB;
                                    ValueExpression fnameExpression;
                                    ValueExpression fvalueVaueExpression;
                                    String epathValue;
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
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
									     <div id="highlight<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>" style="background-color:none;">							
                                                                                <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>')" >
                                                                                    <font class="highlight">
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                    </font>
                                                                                </a>  
																				</div>
									     <div id="unHighlight<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>" style="visibility:hidden;display:none">							
                                                                                <%=personfieldValuesMapEO.get(epathValue)%>
																				</div>

                                                                                <%} else {if(fieldConfigMap.isSensitive()){%>                                                                               
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                 <%}else{%>
                                                                                   <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                 <%}%>
                                                                                <%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    <%
                                        }
                                        }
                                                                    %>

                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
int  maxMinorObjectsMinorDB =  ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


                                                                    ArrayList  minorObjectMapList =  (ArrayList) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    %>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp;
																			<%}%>
																	   </td>
																	</tr>
                                                                    <%
                                                                    for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
																	  minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);
                                                                      for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                       epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
										<%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b><%=minorObjectHashMap.get(epathValue)%></b>
										<%}else{if (fieldConfigMap.isSensitive()){%>																				  
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%}else {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
										<%}}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                  <%
                                                                      } //FIELD CONFIG LOOP
																 %>
                                                                  <tr><td>&nbsp;</td></tr>

                                                                  <%  } // TOTAL MINOR OBJECTS LOOP
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
																	 %>
                                                                      <tr><td>&nbsp;</td></tr>
																	 <%
                                                                        }//Extra minor objects loop
								                                    %>


                                                                    <%} //MINOR OBJECT TYPES LOOPS
                                                                    %>

                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <!-- END Display the field Values-->
                                            <!--Start displaying the sources-->
                                               <% 
                                               eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");

                                              if(eoSources.size() > 0 ) {
                                                //ArrayList soArrayList = (ArrayList) request.getAttribute("eoSources"+(String)personfieldValuesMapEO.get("EUID"));
                                                HashMap soHashMap = new HashMap();
                                                for(int i=0;i<eoSources.size();i++) {
                                                    soHashMap = (HashMap) eoSources.get(i);
                                                    HashMap soHashMapValues = (HashMap) soHashMap.get("SYSTEM_OBJECT");
													String soStatus = (String) soHashMap.get("Status");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupSources<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                    <div style="width:170px;overflow:hidden;">
											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                    <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="deactivate" >
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="transaction" >
												<%} else {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source" >
												<%}%>
												<table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=soHashMap.get("SYSTEM_CODE")%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <b><%=soHashMap.get("LID")%></b>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                   <div id="mainEuidContentButtonDiv<%=countEnt%>" class="deactivate">
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												   <div id="mainEuidContentButtonDiv<%=countEnt%>" class="transaction">
												<%} else {%>
												<div id="mainEuidContentButtonDiv<%=countEnt%>" class="source">
												<%}%>

                                                   <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent" class="source">

                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                    <%
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                        }
                                                                    %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (soHashMapValues.get(epathValue) != null) {%>
                                                                                
                                                                                <%=soHashMapValues.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    <%
                                        }
                                        }
                                                                    %>

                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                                    ArrayList  minorObjectMapList =  (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");

int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;

                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    HashMap minorObjectHashMap = new HashMap();
																	%>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp;
																			<%}%>
																	   </td>
																	</tr>

																	<%
                                                                    for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
                       						                           minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);
                                                                    

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
																				  <%}else{%>
																				   <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>

                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // FIELD CONFIGS LOOP
																    %>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%} // MINOR OBJECTS LOOP FOR THE SO
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
																	%>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%   }//Extra minor objects loop
								                                    %>



																   <%}
                                                                    %>

                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%                                                
                                                }
                                              }%>
                                                
                                            <!--END displaying the sources-->
                                            <!--START displaying the History-->
                                               <% 
                                               eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");

                                              if(eoHistory.size() > 0) {
                                               // ArrayList soArrayList = (ArrayList) request.getAttribute("eoHistory"+(String)personfieldValuesMapEO.get("EUID"));
                                                 
                                                for(int i=0;i<eoHistory.size();i++) {
                                                    HashMap objectHistMap = (HashMap) eoHistory.get(i);
                                                    String key = (String) objectHistMap.keySet().toArray()[0];
                                                    String keyTitle = key.substring(0, key.indexOf(":"));
                                                    HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                    HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                  <div style="width:170px;overflow:hidden;">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="history" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=keyTitle%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=objectHistMapValues.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>" class="history">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent" class="history">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                    <%
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                        }
                                                                    %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (eoValuesMap.get(epathValue) != null) {%>
                                                                                
                                                                                <%=eoValuesMap.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    <%
                                        }
                                        }
                                                                    %>

                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];

int  maxMinorObjectsMinorDB =  ((Integer) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


																	ArrayList  minorObjectMapList =  (ArrayList) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                     //if(minorObjectMapList.size() >0) {
                                                                       //minorObjectHashMap = (HashMap) minorObjectMapList.get(0);
                                                                     //}  
                                                                     FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    

                                                                   %>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp;
																			<%}%>
																	   </td>
																	</tr>
																	<%for(int ar = 0 ; ar <minorObjectMapList.size() ; ar++ ) {
																	  minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                     for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                         FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                         epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                       <tr>
                                                                          <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                 <%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b><%=minorObjectHashMap.get(epathValue)%></b>
																				  <%}else{%>
																				   <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                          </td>
                                                                      </tr>
                                                                    <%
                                                                      } //FIELD CONFIG LOOP
																	%>
                                                                     <tr><td>&nbsp;</td></tr>

																	<%
																     }  //MINOR OBJECTS LOOP 
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
																	 %>
                                                                     <tr><td>&nbsp;</td></tr>

																	 <%
                                                                        }//Extra minor objects loop
								                                    %>

																	  
                                                                    <%} // TOTAL CHILD OBJECTS LOOP
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%                                                
                                                }
                                              }%>                                            
                                            <!--END displaying the History-->
                                            
                                            <%
                                               if (countEnt + 1 == eoArrayListObjects.length) {
                                                    if( request.getAttribute("eoMultiMergePreview") != null) {
                                                        styleClass = "blue";
                                                    }
                                                %>
                                            <td  valign="top">
                                                <div id="previewPane" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=styleClass%>">
                                                            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                    <td width="100%" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                                    <%
                                                                    HashMap eoMultiMergePreviewMap = new HashMap();
                                                                    HashMap mergePersonfieldValuesMapEO = new HashMap();
                                                                    if( request.getAttribute("eoMultiMergePreview") != null  ) {
                                                                        //request.setAttribute("eoMultiMergePreview",request.getAttribute("eoMultiMergePreview"));
                                                                        eoMultiMergePreviewMap =(HashMap) request.getAttribute("eoMultiMergePreview");
                                                                        mergePersonfieldValuesMapEO = (HashMap) eoMultiMergePreviewMap.get("ENTERPRISE_OBJECT");
                                                                    }    
                                                                    %>
                                                                    <tr>
                                                                        <td>
                                                                          <%
                                                                            if( request.getAttribute("eoMultiMergePreview") != null  ) {
                                                                            %>
                                                                                <b><%=mergePersonfieldValuesMapEO.get("EUID")%></b>
                                                                             <%} else {%>       
                                                                                 &nbsp;
                                                                             <%}%>       
                                                                        </td>
                                                                    </tr>
  
                                                                    <%
                                                                     for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                                      FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                                                                       if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                         epathValue = fieldConfigMap.getFullFieldName();
                                                                       } else {
                                                                         epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                                       }
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%
                                                                            if( request.getAttribute("eoMultiMergePreview") != null  ) {
                                                                            %>
                                                                              
                                                                              <%if(mergePersonfieldValuesMapEO.get(epathValue) != null ) {%>
                                                                               <span id="<%=epathValue%>"><%=mergePersonfieldValuesMapEO.get(epathValue)%></span>
                                                                             <%}else{%>
                                                                               <span id="<%=epathValue%>">&nbsp;</span>
                                                                             <%}%>
                                                                             
                                                                             <%}else{%>
                                                                              &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                     }
                                                                    %>
                                                                   <%
                                                                  
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
								    ArrayList  minorObjectMapList =  new ArrayList();
								  if( request.getAttribute("eoMultiMergePreview") != null  ) {
                                                                      minorObjectMapList =  (ArrayList) eoMultiMergePreviewMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
								 }
                                                                    HashMap minorObjectHashMap = new HashMap();
								
                                                                    for(int ar =0;ar<minorObjectMapList.size();ar++) {
                                                                    // if(minorObjectMapList.size() >0) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                     //}  
                                                                    //}   
                                                                      FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    

                                                                   %>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <tr>
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                     epathValue = fieldConfigMap.getFullFieldName();

                                                                    %>  
                                                                    <tr>
                                                                        <td>
										<%if( request.getAttribute("eoMultiMergePreview") != null  ) {%>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
										<%} else {%>
										&nbsp;
										<%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }
                                                                      }
																     
                                                  
                                                                    }
                                                                    %>

                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>     
                                            
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
                                        <td colspan="<%=eoArrayListObjects.length * 2 + 3%>">
                                            <div class="blueline">&nbsp;</div>
                                        </td>   
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!--BEFORE-->
                        <tr>
                            <td>
                                <div id="actionmainEuidContent" class="actionbuton">
                                    <table cellpadding="0" cellspacing="0">
                                        <% for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                String euid = (String) personfieldValuesMapEO.get("EUID");
                                                String potDupStatus = (String) eoHashMapValues.get("Status");
                                                String potDupId = (String) eoHashMapValues.get("PotDupId");
                                                                                     
                                                ValueExpression euidValueExpression = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());        
                                                
                                                ValueExpression potDupIdValueExpression = null;
                                                ValueExpression eoArrayListValueExpression = null;
                                                if(potDupId != null) {
                                                 potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());        
                                                }
                                                eoArrayListValueExpression = ExpressionFactory.newInstance().createValueExpression(eoArrayList, eoArrayList.getClass());        

                                                                                                                                                 
                                            %>
                                        <% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <% }%>
                                        <!--Displaying view sources and view history-->
                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                <table border="0" cellspacing="0" cellpadding="0" border="1">
                                                    <h:form>
                                               <% if (session.getAttribute("eocomparision") == null  && countEnt > 0 ) { %>
											   
                                               <tr> 
                                                    <td valign="top">
                                                      <% if (countEnt > 0 && "A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) { %>
                                                        <h:commandLink  styleClass="diffviewbtn" rendered="#{Operations.potDup_ResolveUntilRecalc}"
                                                                        actionListener="#{PatientDetailsHandler.unresolvePotentialDuplicateAction}">
                                                             <f:attribute name="potDupId" value="<%=potDupIdValueExpression%>"/>
                                                             <f:attribute name="eoArrayList" value="<%=eoArrayListValueExpression%>"/>
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </h:commandLink>  
                                                      <%} else  {%> 

                                                      <h:outputLink 
													  rendered="#{Operations.potDup_ResolveUntilRecalc}" 
													  title="#{msgs.diff_person_heading_text}"
													  onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" 
                                                          styleClass="diffviewbtn"  
                                                          value="javascript:void(0)">
                                                          <h:outputText value="#{msgs.diff_person_heading_text}"/>
                                                       </h:outputLink>   
                                                      <%}%> 
                                                </tr>
                                                <%} else {%> 
                                               <tr><td valign="top">&nbsp;</td></tr>
                                                <%}%> 
                                      
                                                  <tr> 
                                                      <td valign="top">
                                                          <a class="viewbtn"   href="javascript:showViewHistory('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoSources.size()%>')" >  
                                                              <h:outputText value="#{msgs.view_history_text}"/>
                                                          </a>
                                                      </td>    
                                                  </tr> 
                                                  <tr> 
                                                      <td valign="top">
                                                          <a href="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoHistory.size()%>')" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                      </td>                                              
                                                  </tr>
                                                     <tr><td>&nbsp;</td></tr>
                                                        
                                                    </h:form>
                                                </table>
                                            </div> 
                                        </td>
                                        <% if (countEnt + 1 == eoArrayListObjects.length) {%>
                                        <td> <!--Displaying view sources and view history-->
                                            <div id="mergeEuidsDiv"  style="visibility:hidden;display:none;">
                                                <table>
                                                    <tr>
                                                        <td>
                                                            <h:form  id="previewForm">
                                                                <h:commandLink styleClass="button" rendered="#{Operations.EO_Merge}"
                                                                               action="#{PatientDetailsHandler.previewPostMultiMergedEnterpriseObject}">
                                                                    <span><h:outputText value="Preview "/></span>
                                                                </h:commandLink>
                                                                <h:inputHidden id="previewhiddenMergeEuids" value="#{PatientDetailsHandler.mergeEuids}" />
                                                                <h:inputHidden id="destinationEO" value="#{PatientDetailsHandler.destnEuid}" />
                                                             </h:form>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>  
                                           <%
                                           if( request.getAttribute("eoMultiMergePreview") != null  ) {
                                           %>
                                            <div id="mergeFinalEuidsDiv"  >
                                                <table>
                                                    <tr>
                                                        <td>
                                                            <h:outputLink styleClass="button"  rendered="#{Operations.EO_Merge}"
                                                                          value="javascript:void(0)" 
                                                                          onclick="javascript:finalMultiMergeEuids('mergeDiv',event)" >
                                                                    <span><h:outputText value="#{msgs.source_submenu_merge}" /></span>
                                                            </h:outputLink>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <h:form  id="previewCancelForm">
                                                                <h:commandLink styleClass="button" 
                                                                               actionListener="#{PatientDetailsHandler.undoMultiMerge}">
                                                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                </h:commandLink>
                                                            </h:form>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>  
                                           <%}%>       
                                        </td>
                                        
                                        <%}%>

                                        <%}%>
                                        </table>
                                </div>
                            </td>
                        </tr>
                        <!--AFTER-->
                   <%}%>
                        
                    </table>
                </div>  
             </div> 

                                   <div id="mergeDiv" class="alert" style="TOP:2050px; LEFT:300px; HEIGHT:160px; WIDTH: 280px; VISIBILITY: hidden; ">
                            
                                <table cellspacing="0" cellpadding="0" border="0">
                                    <tr>
                                    <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="mergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td align="center"><nobr><h:outputText value="#{msgs.mergediv_popup_text}"/><b> <h:outputText value="#{PatientDetailsHandler.destnEuid}" /> </b>?</nobr></td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr>
                                        <td>
                                            <h:form  id="mergeFinalForm">
                                                <h:commandLink styleClass="button" 
                                                               action="#{PatientDetailsHandler.performMultiMergedEnterpriseObject}">
                                                    <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                </h:commandLink>
                                                <h:inputHidden id="previewhiddenMergeEuids" value="#{PatientDetailsHandler.mergeEuids}" />
                                                <h:inputHidden id="destinationEO" value="#{PatientDetailsHandler.destnEuid}" />
                                                <h:inputHidden id="selectedMergeFields" value="#{PatientDetailsHandler.selectedMergeFields}" />
                                            <h:outputLink styleClass="button"  
                                                          value="javascript:void(0)" 
                                                          onclick="javascript:showExtraDivs('mergeDiv',event)" >
                                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                            </h:outputLink>
                                            </h:form>
                                        </td>
                                    </tr>
                                    
                                </table>
                        </div>  
                                   <div id="resolvePopupDiv" class="alert" style="TOP:2250px; LEFT:300px; HEIGHT:195px;WIDTH: 300px;visibility:hidden; ">
                                     
                                       <h:form id="reportYUISearch">
                                           <h:inputHidden id="potentialDuplicateId" value="#{PatientDetailsHandler.potentialDuplicateId}"/>
                                           <table width="100%">
                                               <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="resolvepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                               <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr><td align="center"><b><h:outputText value="#{msgs.different_person_dailog_text}"/></b></td></tr>
                                               <tr>
                                                   <td  colspan="2">
                                                       <div class="selectContent">
                                                       <h:selectOneRadio id="diffperson" value="#{PatientDetailsHandler.resolveType}" layout="pageDirection">
                                                           <f:selectItem  itemValue="AutoResolve" itemLabel="Resolve Until Recalculation"/>
                                                           <f:selectItem  itemValue="Resolve" itemLabel="Resolve Permanently"/>
                                                       </h:selectOneRadio> 
                                                       </div> 
                                                   </td>
                                               </tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr>
                                                   <td align="right"  colspan="2">
                                                       
                                                        <h:commandLink styleClass="button" 
                                                                       action="#{PatientDetailsHandler.makeDifferentPersonAction}">
                                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </h:commandLink>   
                                                       <h:outputLink  onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'123467')" 
                                                                      styleClass="button"  
                                                                      value="javascript:void(0)">
                                                         <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                       </h:outputLink>   
                                                   </td>
                                               </tr>
                                           </table>
                                           
                                       </h:form>
                                   </div>                                                

             <div id="resolvepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.resolvepopup_help}"/></div>    
             <div id="mergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.mergepopup_help}"/></div>    
             
        </body>
        <%
        if( request.getAttribute("eoMultiMergePreview") != null) {
            String destnEuid  = (String) request.getAttribute("destnEuid");
            
            
        String[] srcs  = (String[]) request.getAttribute("sourceEUIDs");
        for(int i=0;i<srcs.length;i++) {
        %>    
            
        <script>
            document.getElementById('mainEuidContent<%=destnEuid%>').className = "blue";
            document.getElementById('personEuidDataContent<%=destnEuid%>').className = "blue";


            document.getElementById('mainEuidContent<%=srcs[i]%>').className = "yellow";
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "yellow";
            
            document.getElementById('clickButton<%=srcs[i]%>').style.cursor= 'not-allowed';
            document.getElementById('clickButton<%=destnEuid%>').style.cursor= 'not-allowed';
                
        </script>
        <%}%>
        <%}%>

<script>
         if( document.potentialDupBasicForm.elements[0]!=null) {
		var i;
		var max = document.potentialDupBasicForm.length;
		for( i = 0; i < max; i++ ) {
			if( document.potentialDupBasicForm.elements[ i ].type != "hidden" &&
				!document.potentialDupBasicForm.elements[ i ].disabled &&
				!document.potentialDupBasicForm.elements[ i ].readOnly ) {
				document.potentialDupBasicForm.elements[ i ].focus();
				break;
			}
		}
      }         

    </script>

    </html>
    </f:view>
    
