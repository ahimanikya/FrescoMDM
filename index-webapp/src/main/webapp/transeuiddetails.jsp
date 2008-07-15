<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.TransactionHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>


<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager" %>
<f:view> 
  
    <html>
        <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
             
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/balloontip.js"></script>
            <!-- Required for View Merge Tree -->
            <script type="text/javascript" src="scripts/yui4jsf/yahoo/yahoo-min.js"></script>           
            <!-- Additional source files go here -->
            <script type="text/javascript" src="scripts/yui4jsf/core/yui4jsf-core.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/treeview/treeview-min.js"></script>
            
            <script type="text/javascript" src="scripts/yui4jsf/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/animation/animation-min.js"></script>                        
			<script>
		     var euidValueArray=[];
			</script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title> 
        <body>
          <%@include file="./templates/header.jsp"%>
            <div id="mainContent">
                <div id="mainDupSource" class="compareResults">
                    <table cellspacing="0" cellpadding="0" border="0">
                        <tr>
                            <td>
							<FORM>
							 <a class="button" title="<h:outputText value="#{msgs.view_list_but_text}"/>" href="javascript:void()" onclick="history.go(-1)"><span><h:outputText value="#{msgs.view_list_but_text}"/></span></a>
							</FORM>
                           </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="height:500px;overflow:auto;">
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            
                                            <%
			//ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, //FacesContext.getCurrentInstance().getViewRoot().getLocale());

            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            TransactionHandler transactionHandler = new TransactionHandler();
            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();

            EPath ePath = null;
            PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();

            Operations operations = new Operations();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            String transactionId = (String) (request.getParameter("transactionId")==null?request.getAttribute("transactionId"):request.getParameter("transactionId"));
            String function  = (request.getParameter("function") != null)? request.getParameter("function") : "Update";
            
            if (request.getParameter("transactionId") != null) {

                eoArrayList = transactionHandler.getTranscationDetails(transactionId);

                request.setAttribute("comapreEuidsArrayList", eoArrayList);
            }
            
            

            eoArrayList = (ArrayList) request.getAttribute("comapreEuidsArrayList");
            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ValueExpression sourceEUIDVaueExpression = null;
            ValueExpression destinationEUIDVaueExpression = null;
            ValueExpression mergredHashMapVaueExpression = null;
            EnterpriseObject sourceEO = null;
            EnterpriseObject destinationEO = null;
            ArrayList eoSources = null;
            
			//variable for max minor objects count
			int maxMinorObjectsMAX = 0 ; 

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
                                            String styleTransaction = "transaction";
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
                                                String eoStatus = (String) eoHashMapValues.get("EO_STATUS");

                                                if ("Add".equalsIgnoreCase(function)) {
                                                        dupHeading = "<b>" + function + "</b>";
                                                } else if ("euidMerge".equalsIgnoreCase(function)){
                                                    if (countEnt == 1) {
														String mergedLabel = bundle.getString("merged_euid_label");
														dupHeading ="<b>"+mergedLabel+"</b>";

                                                        //dupHeading = "<b>Merged EUID</b>";
                                                    } else  if (countEnt >= 2) {
														String afterMergedLabel = bundle.getString("after_merged_euid_label");
                                                       // dupHeading = "<b>After Merged MAIN EUID</b>";
													   dupHeading = "<b>"+afterMergedLabel+"</b>";
                                                    } else 
                                                        if (countEnt == 0) {
														String beforeMergedLabel = bundle.getString("before_merged_euid_label");
                                                        //dupHeading = "<b>Before Merged MAIN  EUID</b>";
														dupHeading = "<b>"+beforeMergedLabel+"</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                } else if ("euidUnMerge".equalsIgnoreCase(function)) {
                                                    if (countEnt == 1) {
														String afterUnmergedLabel = bundle.getString("after_unmerged_euid_label");
														dupHeading = "<b>"+afterUnmergedLabel +"</b>";
                                                        //dupHeading = "<b>After UnMerge MAIN EO </b>";
                                                    } else if (countEnt >=2) {
														String unmergedLabel = bundle.getString("unmerged_euid_label");
														dupHeading ="<b>"+unmergedLabel+"</b>";
                                                        //dupHeading = "<b> UnMerged EO  </b>";
                                                    }
													else if (countEnt == 0) {
														String beforeUnmergedLabel = bundle.getString("before_unmerged_euid_label");                                                        
														dupHeading = "<b>"+beforeUnmergedLabel+"</b>";

                                                       // dupHeading = "<b>Before UnMerge MAIN EO</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                }
                                                else {
                                                    if (countEnt > 0) {
                                                        String afterLabel = bundle.getString("after_label");    
                                                        dupHeading = "<b> " + afterLabel + "  "+function+ "</b>";                                                                                                           
                                                        //dupHeading = "<b>After " + function + "</b>";
                                                    } else if (countEnt == 0) {
                                                        String beforeLabel = bundle.getString("before_label");  
                                                       // dupHeading = "<b>Before " + function + "</b>";
                                                        
                                                        dupHeading = "<b> " +beforeLabel+" "+ function + "</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                }
                                               

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
                   %>
                                          <%if (countEnt == 0) {%>
                                          <%

                                            %>
                                            <td  valign="top">
                                                <div id="outerMainContentDivid" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=cssMain%>">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" style="visibility:visible;display:block;">
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0" class="yellow">
																<tr><td>EUID</td></tr>
																<tr><td><h:outputText value="#{msgs.source_rec_status_but}"/></td></tr>
                                                                    <%

                                                                String mainDOB;
                                                                ValueExpression fnameExpression;
                                                                ValueExpression fvalueVaueExpression;
                                                                String epathValue;

                                                              for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                                 FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                                                   if(!"EUID".equalsIgnoreCase(fieldConfigMap.getDisplayName())) {

                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%=fieldConfigMap.getDisplayName()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                       }
                                                                      }
                                                                    %>
                                                                    <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());

                                                              maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                              int  maxMinorObjectsMinorDB =  ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
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
                                                                      } //FIELD CONFIG LOOP
																	  %>
                                                                     <tr><td>&nbsp;</td></tr>

																	  <%
                                                                     }
																	 %>

																	 <%
                                                                     }
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>     
                                            
                                            <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="menutop"><%=dupHeading%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td>
                                                                       <font style="text-decoration:none;color:#000000;"><b>
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </b></font>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
                                                
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="yellow">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoStatus)%>
																</font></td></tr>
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
                                                                                    <font class="highlight">
											<%if(fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
                                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%}else{%>
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                     <%}%>
                                                                                    </font>
                                                                                  <%} else {%>
 										<%if(resultArrayMapMain.get(epathValue) == null ) { %>
                                                                                    <font class="highlight">
											 <%if(fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
													       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                    </font>
																					 <%}else {%>
																				         <%if(fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     <%}%>
                                                                                   <%}%>

                                                                                <%} else {%>
 																					<%if(resultArrayMapMain.get(epathValue) != null ) { %>
                                                                                    <font class="highlight">
																				         <%if(fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																						 <img src="./images/calup.gif" border="0" alt="Blank Value"/>
                                                                                       <%}%>
                                                                                    </font>
																					 <%}else {%>
                                                                                       &nbsp; 
                                                                                     <%}%>

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
maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
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
										                                     <%if(fieldConfigMap.isKeyType()) {%> <!--if key type-->
                                                                                <b>
																		         <%if (fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else {%>
                                                                                    <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
																				</b>
										                                    <%}else{%> <!--if not key type-->
																		         <%if (fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else {%>
                                                                                    <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
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
                                            <!--Start displaying the sources-->
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(soStatus)%>
																</font></td></tr>

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

maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

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
                                                                                   <b>
                                                                                     <%if (fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
																				   </b>
																				  <%}else{%>
                                                                                     <%if (fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
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
                                            
                                           <%}%>
                                            <td  valign="top">
                                                 <div id="mainDupHistory" style="visibility:hidden;display:none"></div>
                                            </td>
                                            <td valign="top"><div id="previewPane"></div></td>                                        
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
                        <tr>
                            <td>
                                <div id="actionmainEuidContent" class="actionbuton">
                                    <table cellpadding="0" cellspacing="0">
                                        <% for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                          HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                          HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
										  String euid = (String) personfieldValuesMapEO.get("EUID");
										  eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");

											   %>
  									    <script>
												 euidValueArray.push('<%=euid%>');
										</script>
                                 		<% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <!--Displaying view sources and view history-->
                                        <% }%>

                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0" border="1">
                                                    <h:form>
                                                        <tr> 
                                                          <td valign="top">
                                                             <a title="<h:outputText value="#{msgs.view_sources_text}"/>"  href="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','0',euidValueArray)" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                          </td>                                              
                                                        </tr>
                                                        <%
                                                         boolean isMerge = transactionHandler.isEUIDMerge(transactionId);
                                                         if (isMerge && countEnt > 0) {
                                                        %>                 
                                                        <tr>
                                                            <td valign="top" colspan="2">
                                                                <h:outputLink styleClass="viewbtn" title="#{msgs.Unmerge_but_text}"
                                                                              onclick="Javascript:showExtraDivs('unmergePopupDiv',event)" 
                                                                              value="Javascript:void(0)">
                                                                    <h:outputText  value="#{msgs.Unmerge_but_text}"/>                                                          
                                                                </h:outputLink>
                                                            </td> 
                                                        </tr>
                                                        <% } else {%>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <%}%>
                                                    </h:form>
                                                </table>
                                            </div> 
                                        </td>
										<!--START  Extra tds for the sources-->
                                        <% for (int sCount = 0; sCount < eoSources.size(); sCount++) {%>
                                         <td>
										  <div id="spacer<%=sCount%><%=countEnt%>"  style="visiblity:hidden;display:none;">
										   <table>
										     <tr>
											   <td>
 										           <img src="images/spacer.gif" width="172px" height="1px" border="0">
 											   </td>
											   </tr>
											 </table>
                                            </div>
                                        </td>
										<%}%>
										<!--END Extra tds for the sources-->
                                         <%}%>
                                    </table>
                                </div>
                            </td>
                        </tr>
                   <%}%>
                        
                    </table>
                </div>                                       
            </div>    
                 <%ValueExpression tranNoValueExpressionviewunmerge = ExpressionFactory.newInstance().createValueExpression(transactionId, transactionId.getClass());%>
                       <div id="unmergePopupDiv" class="alert" style="TOP: 1050px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                              <table cellpadding="0" cellspacing="0">
                                <h:form>
                                    <tr><th align="left">Confirmation</th><th align="right"><a href="javascript:void(0)" rel="unmergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_tran_popup_content_text}" /></td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td align="center">
                                            <h:commandLink styleClass="button" title="#{msgs.ok_text_button}"
                                                           action="#{NavigationHandler.toTransactions}"
                                                           actionListener="#{TransactionHandler.unmergeEnterpriseObject}">
                                                <f:attribute name="tranNoValueExpressionviewunmerge" value="<%=tranNoValueExpressionviewunmerge%>"/>                   
                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                            </h:commandLink>   
                                            <h:outputLink  onclick="javascript:showExtraDivs('unmergePopupDiv',event)" 
                                                           styleClass="button"    title="#{msgs.cancel_but_text}"      
                                                           value="javascript:void(0)">
                                                <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                            </h:outputLink>   
                                    </td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr>
                                        <td colspan="2">
                                            <h:messages style="font-size:10px; font-color:red;" layout="table" />
                                        </td>
                                    </tr>        
                                </h:form>
                            </table>
                        </div> 
                       
         <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>      
        </body>
    </html>
    </f:view>
    
