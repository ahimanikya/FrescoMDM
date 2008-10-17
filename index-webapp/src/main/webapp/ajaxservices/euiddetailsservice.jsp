<%-- 
    Document   : euiddetailsservice
    Created on : Oct 7, 2008, 7:59:17 PM
--%>
<%--
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */

--%>


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"  %>

<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.faces.context.FacesContext" %>
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
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DashboardHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%
 double rand = java.lang.Math.random();
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
%>

<f:view>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));

}
%>

 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
         <title><h:outputText value="#{msgs.application_heading}"/></title>
 
     </head>
<%
//Date Created : 08-OCT-2008
 //This page is an Ajax Service, never to be used directly from the Faces-confg.
%>

	 <%
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
boolean isSessionActive = true;
%>

<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>

<%if (isSessionActive)  {%>
 		 <%
			ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
			Iterator messagesIter = null ;

            //EPathArrayList ePathArrayList = midmUtilityManager.retrieveEPathArrayList(objScreenObject);

			ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
			// Added by Anil, fix for  CR 6709864
			Operations operations=new Operations();

            EPath ePath = null;
            RecordDetailsHandler recordDetailsHandler = new RecordDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();
			DashboardHandler dashboardHandler=new DashboardHandler();
			String euidReq = request.getParameter("euid");
			//Variables for deactive 
			String isdeactiveEOString = request.getParameter("deactiveEO");
			boolean isDeactiveEO = (null == isdeactiveEOString?false:true);

			//Variables for active 
			String isactiveEOString = request.getParameter("activeEO");
			boolean isActiveEO = (null == isactiveEOString?false:true);

			//Variables for show Merged Record for EO
			String showMergedRecordStr = request.getParameter("showMergedRecord");
			boolean isShowMergedRecords = (null == showMergedRecordStr?false:true);

            ArrayList eoArrayList = new ArrayList();
			 ArrayList eoMergeRecords = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
    
			if(isActiveEO) { //If activate EO
 	         
			    if (euidReq != null) eoArrayList  = recordDetailsHandler.activateEO(euidReq);

 			} else if(isDeactiveEO) { //If activate EO
 				if (euidReq != null) eoArrayList  = recordDetailsHandler.deactivateEO(euidReq);

			}  else if(isShowMergedRecords) { //If Show merged records for EO
            
				eoArrayList  = recordDetailsHandler.buildEuids(euidReq);
                eoMergeRecords = recordDetailsHandler.viewMergedRecords(euidReq,request.getParameter("tranNo"));

			} else { //If simple EUID lookup

				if (euidReq != null) eoArrayList = recordDetailsHandler.buildEuids(euidReq);
             }
			//deactoive
            messagesIter = FacesContext.getCurrentInstance().getMessages(); 
             String tranNo = null;
            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ValueExpression mergredHashMapVaueExpression = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;
            ValueExpression unMergeEuidVE = null;
			int countInactive = 0;
			//variable for max minor objects count
			int maxMinorObjectsMAX = 0 ; 
			%>
             <%
            if (eoArrayList != null && eoArrayList.size() > 0) {%>
            <!-- if euid is merge-->
			<%HashMap euidMap  = (HashMap) eoArrayList.get(0);	%>
			<%if(euidMap.get("Merged_EUID") != null ) {%>
			  <table>
			     <tr><td>
				      <script>
						 document.getElementById("activemessageDiv").innerHTML='<%=euidMap.get("Merged_EUID_Message")%>';
						 document.getElementById('activeDiv').style.visibility='visible';
						 document.getElementById('activeDiv').style.display='block';
						 popUrl = '/<%=URI%>/euiddetails.jsf?euid=<%=euidMap.get("Merged_EUID")%>';
 				      </script>
				    </td>
				 </tr>
			  </table>
			<%} else {%>
                <div id="mainDupSource" class="compareResults">
                    <table cellspacing="0" cellpadding="0" border="0">
                        <tr>
                            <td>
                                <div style="height:700px;overflow:auto;">
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                           <%request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));%>  
                                            <!-- Display the field Names first column-->
                                            <!--end displaying first column-->       
                                           <%
                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                            String dupHeading = "Main Euid";
                                            String cssMain = "maineuidpreview";
                                            String cssClass = "dynaw169";
                                            String menuClass = "menutop";
                                            String dupfirstBlue = "dupfirst";
                                            String styleClass="yellow";
                                            String subscripts[] = midmUtilityManager.getSubscript(eoArrayListObjects.length);
                                            String mainEUID = new String();
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                 HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                 HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                 String eoStatus = (String) eoHashMapValues.get("EO_STATUS");
                                                if ("inactive".equalsIgnoreCase(eoStatus)) {
                                                    styleClass="deactivate";
                                                }
                                            
    
                                              //EnterpriseObject eoSource = midmUtilityManager.getEnterpriseObject(strDataArray);

                                                if (eoArrayListObjects.length > 1) {
                                                    dupHeading = "<b>EUID " + new Integer(countEnt + 1).toString() +  "</b>";
                                                } else if (eoArrayListObjects.length == 1) {
                                                    dupHeading = "<b>EUID</b>";
                                                }
                                                mainEUID = (String) personfieldValuesMapEO.get("EUID");

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
                   %>
                                          <%if (countEnt == 0) {%>
                                             <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=cssMain%>">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" style="visibility:visible;display:block;" class="yellow">
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0">
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
                                                                       } // If not EUID 
															         } 
                                                                    %>
                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());

                                                              maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
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
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                            </tr>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                        <% if ("inactive".equalsIgnoreCase(eoStatus)) {%>
                                                                           <%=personfieldValuesMapEO.get("EUID")%>
                                                                        <%} else {%>
                                                                           <span class="dupbtn" >
                                                                             <%=personfieldValuesMapEO.get("EUID")%>
                                                                           </span>
                                                                        <%} %>
                                                                    </td>
                                                                </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                                
                                                        
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(eoStatus)%>
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
                                                                     %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
   																				  <%if(eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>                                  <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else{%>
                                                                                   <%=personfieldValuesMapEO.get(epathValue)%>
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
maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
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
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else {%>
                                                                                    <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
																				</b>
										                                    <%}else{%> <!--if not key type-->
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
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
                                                    <div style="width:170px;overflow:hidden">
											   <%if("inactive".equalsIgnoreCase(soStatus)) {
													countInactive++;
													%>
                                                    <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="deactivate" >
												<%} else if("merged".equalsIgnoreCase(soStatus)) {
													countInactive++;
												%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="transaction" >
												<%} else {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source" >
												<%}%>
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=soHashMap.get("SYSTEM_CODE")%></td>
                                                            </tr>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <b><%=soHashMap.get("LID")%></b>
                                                                    </td>
                                                                </tr>
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="source">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td>
 																<font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(soStatus)%>
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
                                                                               
                                                                                 <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>  <!-- if sensitive fields-->
                                                                                    <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                 <%}else {%>
                                                                                    <%=soHashMapValues.get(epathValue)%>
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
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                                    ArrayList  minorObjectMapList =  (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");

int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

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
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive()&& !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                            <!--START displaying the History-->
                                               <% 
                                               eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");

                                              if(eoHistory.size() > 0) {
                                               // ArrayList soArrayList = (ArrayList) request.getAttribute("eoHistory"+(String)personfieldValuesMapEO.get("EUID"));
                                                 
                                                for(int i=0;i<eoHistory.size();i++) {
                                                    HashMap objectHistMap = (HashMap) eoHistory.get(i);
                                                    String key = (String) objectHistMap.keySet().toArray()[0];
                                                    String keyTitle = key.substring(0, key.indexOf(":"));
                                                    int ind = key.indexOf(":");
                                                    tranNo = key.substring(ind+1, key.length()); 
                                                    HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                    HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
													String eoHistStatus = (String) objectHistMapValues.get("EO_STATUS");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                  <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="history" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=keyTitle%></td>
                                                            </tr>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=objectHistMapValues.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                        </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="history">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(eoHistStatus)%></font></td></tr>
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
                                                                                
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=eoValuesMap.get(epathValue)%>
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
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];

int  maxMinorObjectsMinorDB =  ((Integer) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

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
                                                                                   <b>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> <!-- if sensitive fields-->
                                                                                      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                    <%}else {%>
                                                                                      <%=minorObjectHashMap.get(epathValue)%>
                                                                                    <%}%>
                                                                                   </b>
                                                                                  <%}else{%>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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

											 <!--Start displaying merged records -->
                                         <% 
                                               if( eoMergeRecords != null && eoMergeRecords.size() > 0) {
                                                 for(int i=0;i<eoMergeRecords.size();i++) {
                                                    HashMap mergedValuesMap = (HashMap) eoMergeRecords.get(i);
                                                    HashMap eoValuesMap = (HashMap) mergedValuesMap.get("ENTERPRISE_OBJECT");
					                                String eoMergedStatus = (String) mergedValuesMap.get("EO_STATUS");
                                            %>
                                               <td  valign="top">
                                                <div id="eoMergeRecords<%=countEnt%><%=i%>">
                                                  <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=eoValuesMap.get("EUID")%>" class="transaction" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>">
																  <%if(i==0) {%>
																    <h:outputText value="#{msgs.main_euid_label_text}"/>
																  <%}else {%>
																    <h:outputText value="#{msgs.merged_euid_label}"/>
																  <%}%>
																</td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=eoValuesMap.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                         </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=mergedValuesMap.get("EUID")%>" class="transaction">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(eoMergedStatus)%></font></td></tr>
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
                                                                                
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=eoValuesMap.get(epathValue)%>
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
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];

int  maxMinorObjectsMinorDB =  ((Integer) mergedValuesMap.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


																	ArrayList  minorObjectMapList =  (ArrayList) mergedValuesMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
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
                                                                                   <b>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> <!-- if sensitive fields-->
                                                                                      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                    <%}else {%>
                                                                                      <%=minorObjectHashMap.get(epathValue)%>
                                                                                    <%}%>
                                                                                   </b>
                                                                                  <%}else{%>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                              <!--End displaying merged records-->
                                           <%}%>
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
                                <table width="100%" cellpadding="2" cellspacing="2" border="0">
                                    <tr>
                                        <% 

                                        HashMap eoHashMapValues = new HashMap();
                                            
                                            HashMap personfieldValuesMapEO = new HashMap();
                                            String euid = new String();
                                            String eoStatus = new String();
                                        	ValueExpression  euidValueExpression = null;        
    						             for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                            eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                            personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                            euid = (String) personfieldValuesMapEO.get("EUID");
                                            eoStatus = (String) eoHashMapValues.get("EO_STATUS");
                                            euidValueExpression = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());        
    
    							
							          %>
									  	<script>
												 euidValueArray.push('<%=euid%>');
										</script>

                                        <% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <% }%>
									   
                                         <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                        <%if ("active".equalsIgnoreCase(eoStatus)) {%>
                                                <tr> 
                                                    <td valign="top" width="125px">
													<!-- Start Added by Anil, fix for  CR 6709864-->
													 <% if(operations.isEO_Edit()){%>
                                                        <a  title="<h:outputText value="#{msgs.edit_euid_button_text}" />" class="button" href="javascript:void(0)"
                                                                        onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=euid%>','ajaxContent','')">
                                                            <span><h:outputText value="#{msgs.edit_euid_button_text}" /></span>
                                                        </a>  
													 <%}%>
													 <!-- End  fix for  CR 6709864-->
                                                   <!-- Deactive/Activate button -->
												   <a  title="<h:outputText value="#{msgs.source_rec_deactivate_but}" />" class="button" href="javascript:void(0)"
                                                                        onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/euiddetailsservice.jsf?'+'&rand=<%=rand%>&euid=<%=euid%>&deactiveEO=true','targetDiv','')">
                                                            <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                        </a>
                                                    </td>
                                                   </tr>
                                                        <%}%>            
                                                    <%if (countInactive != eoSources.size() && "inactive".equalsIgnoreCase(eoStatus)) {%>
                                                    <tr>
                                                         <td valign="top" width="125px">
														 <a  title="<h:outputText value="#{msgs.source_rec_activate_but}" />" class="button" href="javascript:void(0)"
                                                                        onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/euiddetailsservice.jsf?'+'&rand=<%=rand%>&euid=<%=euid%>&activeEO=true','targetDiv','')">
                                                            <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                        </a>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <h:outputText style="color:red" value="#{msgs.euid_deactive_text}"/>
                                                         </td>
                                                      </tr>
                                                        <%}%>            

                                                  <tr> 
												  <%  
													  Operations ops = new Operations();
												     if(ops.isTransLog_SearchView()){
												  %>
                                                      <td valign="top">
                                                          <a class="viewbtn"   title="<h:outputText value="#{msgs.view_history_text}"/>" href="javascript:showViewHistory('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoSources.size()%>','true',euidValueArray)" >  
                                                              <h:outputText value="#{msgs.view_history_text}"/>
                                                          </a>
                                                      </td>    
												  <% } %>	  
                                                  </tr> 
                                                  <tr> 
                                                      <td valign="top">
                                                          <a  title="<h:outputText value="#{msgs.view_sources_text}"/>"  href="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoHistory.size()%>',euidValueArray)" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                      </td>                                              
                                                  </tr>

                                        <%if(eoHistory.size() > 0) {
												   %>
										
                                                     <%
											            String mergekey = new String();
											            boolean hasMergeHist= false;
                                                        for(int i=0;i<eoHistory.size();i++) {
                                                          HashMap objectHist = (HashMap) eoHistory.get(i);
                                                          mergekey = (String) objectHist.keySet().toArray()[0];
														  
										               }
                                                        if (mergekey.startsWith("euidMerge")) {                                                  
                                                      %>  
                                                     <tr>
                                                      <td valign="top">
                                                          <a href="javascript:void(0);" class="viewbtn" title="<h:outputText  value="#{msgs.View_MergeTree_but_text}"/>"									 onclick="javascript:showExtraDivs('tree',event);ajaxURL('/<%=URI%>/viewmergetree.jsf?euid=<%=personfieldValuesMapEO.get("EUID")%>&rand=<%=rand%>','tree',event)">
														  <h:outputText  value="#{msgs.View_MergeTree_but_text}"/>
                                                          </a>
                                                      </td>
                                                      </tr>
                                                      <tr>
                                                        <td valign="top">
 														   <a  title="<h:outputText value="#{msgs.View_Merge_Records_but_text}" />" class="viewbtn" href="javascript:void(0)"
														   onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/euiddetailsservice.jsf?'+'&rand=<%=rand%>&euid=<%=euid%>&showMergedRecord=true&tranNo=<%=tranNo%>','targetDiv','')">
                                                            <h:outputText  value="#{msgs.View_Merge_Records_but_text}"/>
                                                           </a>																
                                                         </td> 
                                                       </tr>
                                                   <%}%>
 
                                          <%}%> 


                                               </table>                                               
  											   </div>
                                        </td>   
										<%}%>
                                    </tr>
                                </table>
                                </div>
                            </td>
                        </tr>
                 
                    </table>
                </div>
                
            </div>
			<%}%>
 
			<%}else {%>
				 <div class="ajaxalert">
				  <table>
						<tr>
							<td>
								  <ul>
									<% while (messagesIter.hasNext())   { %>
										 <li>
											<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
											<%= facesMessage.getSummary() %>
										 </li>
									 <% } %>
								  </ul>
							<td>
						<tr>
					</table>
					</div>
			 <%}%>

			<div id="tree" style="VISIBILITY: hidden;WIDTH: 180px;POSITION: absolute;  OVERFLOW: auto;  BACKGROUND-COLOR: #c4c8e1;    border-right: 1px solid #000000;border-left: 1px solid #000000;border-top: 1px solid #000000;border-bottom: 1px solid #000000;"></div> 
			<div id="unmergePopupDiv" class="confirmPreview" style="TOP: 2250px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                                   <table cellpadding="0" cellspacing="0">
                                      <h:form>
                                       <tr>
								          <th align="center" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
		                                  <th>
				                            <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unmergePopupDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
                                            <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unmergePopupDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
		                                  </th>
		                               </tr>
                                       <tr><td colspan="2"> &nbsp;</td></tr>
                                       <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_popup_content_text}" /></td></tr>
                                       <tr><td colspan="2"> &nbsp;</td></tr>
                                       <tr><td colspan="2"> &nbsp;</td></tr>
                                       <tr><td>
                                          <h:commandLink styleClass="button" title="#{msgs.ok_text_button}" 
                                          actionListener="#{RecordDetailsHandler.unmergeEnterpriseObject}">
                                          <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                          </h:commandLink>   
                                          <h:outputLink  title="#{msgs.cancel_but_text}"  onclick="javascript:showConfirm('unmergePopupDiv',event)" 
                                                       styleClass="button" value="javascript:void(0)">
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
		 <form id="EditIndexForm" name="EditIndexForm">
		   <input type="hidden" id="EditIndexFormID" value="-1" />
       </form>
	   <!-- Added  on 08-10-2008, to change alert pop up window to information pop up window -->
       <div id="activeDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="activemessageDiv"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td>
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:showExtraDivs('activeDiv',event);window.location = popUrl;" class="button" >
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

 <%}%> <!-- if session is active -->


</html>

<script type="text/javascript">
    dd=new YAHOO.util.DD("activeDiv");
	dd_tree=new YAHOO.util.DD("tree");
</script>
     </html>
    </f:view>
    
