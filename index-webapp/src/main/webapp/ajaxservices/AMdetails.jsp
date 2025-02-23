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
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
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
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

<f:view>
     <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
<%
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
double rand = java.lang.Math.random();
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
	 <html>
	 <body>

				<div>
                    <table cellspacing="0" cellpadding="0" border="0">
                        <tr>
                            <td align="left">
                                <!--div style="text-align:left;width:200px"-->
                              <div style="height:700px;overflow:scroll;">
                                 <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            
                                            <%
            Operations operations = new Operations();
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

            //EPathArrayList ePathArrayList = midmUtilityManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();

            EPath ePath = null;
            SourceHandler sourceHandler = new SourceHandler();
            Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            //assumed match  values with systecode and lid
            HashMap amValues = new HashMap();
			String rootNodeValue = "";
            
            if (request.getParameter("AMID") != null) {
                amValues = assumeMatchHandler.getAssumedHashMap(request.getParameter("AMID"));
                request.setAttribute("amValues", amValues);
            }

            if (request.getAttribute("amValues") != null) {
                amValues = (HashMap) request.getAttribute("amValues");
            }

            if (request.getParameter("AMID") != null) {
                request.setAttribute("comapreEuidsArrayList", assumeMatchHandler.getEOList(request.getParameter("AMID")));
            }
            if (request.getAttribute("comapreEuidsArrayList") != null) {
                eoArrayList = (ArrayList) request.getAttribute("comapreEuidsArrayList");
            }
            String amPreviewId = (String) request.getAttribute("undoAssumedMatchId");


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
            ArrayList eoHistory = null;

            //variable for max minor objects count
            int maxMinorObjectsMAX = 0;

            if (eoArrayList != null && eoArrayList.size() > 0 ) {
                request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));
                                            %>  
                                            <!-- Display the field Names first column-->
                                            <!--end displaying first column-->       
                                           <%
                                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                                            String dupHeading = "Main Euid";
                                                            String cssMain = "maineuidpreview";
                                                            String cssHistory = "differentHistoryColour";
                                                            String cssSources = "differentSourceColour";
                                                            String cssDiffPerson = "differentPersonColour";
                                                            String menuClass = "menutop";
                                                            String dupfirstBlue = "dupfirst";
                                                            String styleClass = "yellow";
                                                            String subscripts[] = midmUtilityManager.getSubscript(eoArrayListObjects.length);
                                                            String mainEUID = new String();
                                                            //if (eoArrayListObjects.length == 1) {
                                                            //styleClass = "blue";
                                                            //}
                                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                                HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
																String eoStatus = (String) eoHashMapValues.get("EO_STATUS");

                                                                if (countEnt > 0) {
                                                                    dupHeading = "<b> " + countEnt + "<sup>" + subscripts[countEnt] + "</sup> Duplicate</b>";
                                                                } else if (countEnt == 0) {
                                                                    dupHeading = "<b> Main EUID</b>";
                                                                    mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                                }

                                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                                ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
                   %>
                                          <%if (countEnt == 0) {%>
<!--First Navigation --><!-- 176 fix 28-11-08 -->
<td valign="top" align="right" height="100%">
	<div id="first" onmouseout="changecolor(this)" style="cursor:hand;verticle-align:top;height:100%;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("first")%>" onclick="javascript:
		thisIdx = 0; 
		ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=prev&random=rand'+'&'+'AMID='+pages[thisIdx],'outputdiv','');" >
		<tr><td><img src='/<%=URI%>/images/chevrons-left.png'></td></tr> 
		<tr><td><img src='/<%=URI%>/images/chevrons-left.png'></td></tr> 
         </table>
	</div>
</td>

<!--Prev Navigation -->
<td valign="top" align="right" height="100%">
	<div id="prev" onmouseout="changecolor(this)" style="cursor:hand;verticle-align:top;height:100%;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("prev")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=prev&random=rand'+'&'+'AMID='+pages[--thisIdx],'outputdiv','');" >
		<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
		<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
         </table>
	</div>
</td>

                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>">
                                                    <div>
                                                        <div id="mainEuidContent" class="yellow">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Details</b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" >
                                                        <div id="assEuidDataContent<%=countEnt%>">
                                                            <div id="personassEuidDataContent" class="yellow">
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0" >
																<tr><td><span class="inlinefonts">EUID</span></td></tr>
																<tr><td><span class="inlinefonts"><h:outputText value="#{msgs.source_rec_status_but}"/></span></td></tr>
                                                                    <%

                                                                       String mainDOB;
                                                                       ValueExpression fnameExpression;
                                                                       ValueExpression fvalueVaueExpression;
                                                                       String epathValue;

                                                                       for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                                           FieldConfig fieldConfigMap = rootFieldConfigArray[ifc];
																		   if(!"EUID".equalsIgnoreCase(fieldConfigMap.getDisplayName())) {
                                                                    %>  
                                                                    <tr>
                                                                        <td><span class="inlinefonts">
                                                                               <%=fieldConfigMap.getDisplayName()%>
                                                                        </span></td>
                                                                    </tr>
                                                                    <%
                                                                       }
																	  }
                                                                    %>
                                                                    <%

    for (int i = 0; i < arrObjectNodeConfig.length; i++) {
        ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
        FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());

        maxMinorObjectsMAX = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList, objScreenObject, childObjectNodeConfig.getName());
        int maxMinorObjectsMinorDB = ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                                    %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
                                                                    <%

                                                                        for (int max = 0; max < maxMinorObjectsMAX; max++) {
                                                                            for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                                FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                    %>  
                                                                    <tr>
                                                                        <td><span class="inlinefonts">
                                                                        <%=fieldConfigMap.getDisplayName()%>
                                                                        </span></td>
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
                                            <!-- Modify on 29/07/2008
                                                 Description : Removed <h:form/> tag for EUID of mainEuidContent div
                                              -->

                                            <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                    <div style="width:170px;overflow:hidden">
                                                        <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>" >
                                                            <table border="0" cellspacing="0" cellpadding="0" >
                                                                <tr>
                                                                    <td class="<%=menuClass%>"><%=dupHeading%>
                                                                    </td>
                                                                </tr> 
                                                                    <tr>
                                                                        <td valign="top" class="dupfirst">
                                                                            <span class="dupbtn">
                                                                                <%=personfieldValuesMapEO.get("EUID")%>
                                                                            </span>
                                                                        </td>
                                                                    </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                  <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(eoStatus)%></font></td></tr>
                                                                    <%
                                                String mainDOB;
                                                ValueExpression fnameExpression;
                                                ValueExpression fvalueVaueExpression;
                                                String epathValue;
                                                for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                    FieldConfig fieldConfigMap = rootFieldConfigArray[ifc];
                                                    if (!(objScreenObject.getRootObj().getName() + ".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {

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
                                                                            <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=personfieldValuesMapEO.get(epathValue)%>')" >
                                                                                <font class="highlight">
																					<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																					if(rootNodeValue.length()>20){
																					rootNodeValue = rootNodeValue.substring(0,20);
																					%>
																					<%=rootNodeValue%>
																					<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																					<%}else{%>
																					<%=personfieldValuesMapEO.get(epathValue)%> 
																					<%}%>
                                                                                    
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                                    <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                    <%} else {%>
																						<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																						if(rootNodeValue.length()>20){
																						rootNodeValue = rootNodeValue.substring(0,20);
																						%>
																						<%=rootNodeValue%>
																						<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																						<%}else{%>
																						<%=personfieldValuesMapEO.get(epathValue)%> 
																						<%}%>
                                                                                    <%}%>
                                                                                    
                                                                                </font>
                                                                            </a>  
                                                                            <%} else {%>
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=personfieldValuesMapEO.get(epathValue)%> 
																				<%}%>
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
                                                    int maxMinorObjectsMinorDB = ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                    maxMinorObjectsMAX = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList, objScreenObject, childObjectNodeConfig.getName());
                                                    int maxMinorObjectsDiff = maxMinorObjectsMAX - maxMinorObjectsMinorDB;


                                                    ArrayList minorObjectMapList = (ArrayList) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                    HashMap minorObjectHashMap = new HashMap();
                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    %>
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() == 0) {%>
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
                                                                                FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                                epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() > 0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                            <%if (fieldConfigMap.isKeyType()) {%> <!--if key type-->
                                                                            <b>
                                                                                <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>								      
																				  <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%>
																					<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																					if(rootNodeValue.length()>20){
																					rootNodeValue = rootNodeValue.substring(0,20);
																					%>
																					<%=rootNodeValue%>
																					<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																					<%}else{%>
																					<%=minorObjectHashMap.get(epathValue)%> 
																					<%}%>
                                                                                <%}%>
                                                                            </b>
                                                                            <%} else {%> <!--if not key type-->
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>	    <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=minorObjectHashMap.get(epathValue)%> 
																				<%}%>
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
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
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

                                                if (eoSources.size() > 0) {
                                                    //ArrayList soArrayList = (ArrayList) request.getAttribute("eoSources"+(String)personfieldValuesMapEO.get("EUID"));
                                                    HashMap soHashMap = new HashMap();
                                                    for (int i = 0; i < eoSources.size(); i++) {
                                                        soHashMap = (HashMap) eoSources.get(i);
                                                        HashMap soHashMapValues = (HashMap) soHashMap.get("SYSTEM_OBJECT");
														String soStatus = (String) soHashMap.get("Status");

                                                        String soSource = (String) soHashMapValues.get("SYSTEM_CODE");
                                                        String soLID = (String) soHashMapValues.get("LID");

                                                        //get the assume match ID 
                                                        String amSourcesID = (String) amValues.get("amID" + soSource + ":" + soLID);

                                            %>
                                                                                        <%if (amSourcesID != null) {%>
                                            <td  valign="top">
                                                <div id="mainDupSources<%=countEnt%><%=i%>">
                                                    <div style="width:170px;overflow:hidden;">
 											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                   <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="deactivate">
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												   <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="transaction">
												<%} else {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source">
												<%}%>

                                                            <table border="0" cellspacing="0" cellpadding="0" >
                                                                <tr>
                                                                    <td class="<%=menuClass%>">
																		<%rootNodeValue =  soHashMap.get("SYSTEM_CODE").toString();
																		if(rootNodeValue.length()>12){
																		rootNodeValue = rootNodeValue.substring(0,12);
																		%>
																		<%=rootNodeValue%>
																		<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=soHashMap.get("SYSTEM_CODE")%>">... </a>
																		<%}else{%>
																		<%=soHashMap.get("SYSTEM_CODE")%> 
																		<%}%>
																	</td>
                                                                </tr> 
                                                                    <tr>
                                                                        <td class="dupfirst">
																		<font style="text-decoration:none;color:#000000;">
                                                                            <b><%=soHashMap.get("LID")%></b>
																		</font>
                                                                        </td>
                                                                    </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="source">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                  <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(soStatus)%></font></td></tr>

																	<%
    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
        FieldConfig fieldConfigMap = rootFieldConfigArray[ifc];
        if (!(objScreenObject.getRootObj().getName() + ".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {

            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                epathValue = fieldConfigMap.getFullFieldName();
            } else {
                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
            }
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (soHashMapValues.get(epathValue) != null) {%>
                                                                            
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>  <!-- if sensitive fields-->
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  soHashMapValues.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=soHashMapValues.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=soHashMapValues.get(epathValue)%> 
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

    for (int io = 0; io < arrObjectNodeConfig.length; io++) {
        ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
        ArrayList minorObjectMapList = (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");

        int maxMinorObjectsMinorDB = ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

        maxMinorObjectsMAX = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList, objScreenObject, childObjectNodeConfig.getName());

        int maxMinorObjectsDiff = maxMinorObjectsMAX - maxMinorObjectsMinorDB;

        FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
        HashMap minorObjectHashMap = new HashMap();
                                                                    %>
                                                                    <tr>
					                                                      <td>
                                                                            <%if (minorObjectMapList.size() == 0) {%>
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
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                            epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() > 0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                            <%if (fieldConfigMap.isKeyType()) {%>
                                                                            <b>
                                                                                <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%>
																					<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																					if(rootNodeValue.length()>20){
																					rootNodeValue = rootNodeValue.substring(0,20);
																					%>
																					<%=rootNodeValue%>
																					<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																					<%}else{%>
																					<%=minorObjectHashMap.get(epathValue)%> 
																					<%}%>
                                                                                <%}%>
                                                                            </b>
                                                                            <%} else {%>
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=minorObjectHashMap.get(epathValue)%> 
																				<%}%>
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
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
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

                                            <%}%> <!-- Check if it is assumed match-->
                                            <%
                                                    }
                                                }%>
                                            
                                            <!--END displaying the sources-->
                                            <!--START displaying the History-->
                                               <%
                                                eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");
                                                if (eoHistory.size() > 0) {
                                                    // ArrayList soArrayList = (ArrayList) request.getAttribute("eoHistory"+(String)personfieldValuesMapEO.get("EUID"));

                                                    for (int i = 0; i < eoHistory.size(); i++) {
                                                        HashMap objectHistMap = (HashMap) eoHistory.get(i);
                                                        String key = (String) objectHistMap.keySet().toArray()[0];
                                                        String keyTitle = key.substring(0, key.indexOf(":"));
                                                        HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                        HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
													    String eoHistStatus = (String) objectHistMapValues.get("EO_STATUS");
                                            %>
                                            <td  valign="top" >
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none;">
                                                    <div style="width:170px;overflow:hidden;">
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
                                                        FieldConfig fieldConfigMap = rootFieldConfigArray[ifc];
                                                        if (!(objScreenObject.getRootObj().getName() + ".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {

                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                epathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (eoValuesMap.get(epathValue) != null) {%>
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  eoValuesMap.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=eoValuesMap.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=eoValuesMap.get(epathValue)%> 
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

                                                    for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                        ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];

                                                        int maxMinorObjectsMinorDB = ((Integer) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

                                                        maxMinorObjectsMAX = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList, objScreenObject, childObjectNodeConfig.getName());

                                                        int maxMinorObjectsDiff = maxMinorObjectsMAX - maxMinorObjectsMinorDB;


                                                        ArrayList minorObjectMapList = (ArrayList) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                        HashMap minorObjectHashMap = new HashMap();

                                                        FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());


                                                                    %>
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() == 0) {%>
                                                                            No <%=childObjectNodeConfig.getName()%>.
                                                                            <%} else {%>
                                                                            &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%for (int ar = 0; ar < minorObjectMapList.size(); ar++) {
                                                                            minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                            for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                                FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
                                                                                epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%if (minorObjectMapList.size() > 0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                            <%if (fieldConfigMap.isKeyType()) {%>
                                                                            <b>
                                                                                <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%> <!-- if sensitive fields-->
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%>
																					<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																					if(rootNodeValue.length()>20){
																					rootNodeValue = rootNodeValue.substring(0,20);
																					%>
																					<%=rootNodeValue%>
																					<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																					<%}else{%>
																					<%=minorObjectHashMap.get(epathValue)%> 
																					<%}%>
                                                                                <%}%>
                                                                            </b>
                                                                            <%} else {%>
                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()) {%>
                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                            <%} else {%>
																				<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																				if(rootNodeValue.length()>20){
																				rootNodeValue = rootNodeValue.substring(0,20);
																				%>
																				<%=rootNodeValue%>
																				<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>: <%=minorObjectHashMap.get(epathValue)%>">... </a>
																				<%}else{%>
																				<%=minorObjectHashMap.get(epathValue)%> 
																				<%}%>
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
                                                                            FieldConfig fieldConfigMap = fieldConfigArrayMinor[ifc];
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
<!--Next Navigation -->
<td valign="top" align="left" height="100%">
	<div id="next" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("next")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=next&random=rand'+'&'+'AMID='+pages[++thisIdx],'outputdiv','');" >
           <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
           <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
         </table>
	</div>
</td>
<!--Last Navigation --><!-- 176 fix 28-11-08 -->
<td valign="top" align="left" height="100%">
	<div id="last" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("last")%>" onclick="javascript:
		var indx = pages.length-1;
		thisIdx = indx;
		ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=next&random=rand'+'&'+'AMID='+pages[thisIdx],'outputdiv','');" >
           <tr><td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td></tr> 
           <tr><td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td></tr> 
         </table>
	</div>
</td>


                                            <!--END displaying the History-->
                                            <% if (countEnt + 1 == eoArrayListObjects.length) {
                                                    if (request.getAttribute("previewAMEO") != null) {
                                                        styleClass = "blue";
                                                    }
                                            %>
											<td  valign="top">
											   <div  id="previewdiv"></div>
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
                        <tr class="detailedresults">
                            <td align="left">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                   <% for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                                 HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                           if (countEnt == 0) {%>
                                        <td><img src='/<%=URI%>/images/spacer.gif' border="0" width="20px"></td>
										<td width="169px" valign="top">
                                          <table cellpadding="0" cellspacing="0">
										    <tr>
											  <td valign="top">  
                                                   <a href="assumedmatches.jsf" class="button" title="<h:outputText value="#{msgs.search_again}"/>" >
															 <span><h:outputText value="#{msgs.search_again}"/></span>
												   </a>											  
											  </td>
											</tr>
										  </table>
										</td>
                      							 <%   Operations ops = new Operations();
												        if(ops.isTransLog_SearchView()){%>
                                                            <td width="167px">
                                                                <a class="viewbtn"   title="<h:outputText value="#{msgs.view_history_text}"/>" href="javascript:showViewHistory('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','0')" ><h:outputText value="#{msgs.view_history_text}"/></a>
                                                            </td>    
												         <% } %>	 
                                            <% }%>
											<%eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");
												 if (eoSources.size() > 0) {
													 //ArrayList soArrayList = (ArrayList) request.getAttribute("eoSources"+(String)personfieldValuesMapEO.get("EUID"));
													 HashMap soHashMap = new HashMap();
													 for (int i = 0; i < eoSources.size(); i++) {
														 soHashMap = (HashMap) eoSources.get(i);
														 HashMap soHashMapValues = (HashMap) soHashMap.get("SYSTEM_OBJECT");

														 String soSource = (String) soHashMapValues.get("SYSTEM_CODE");
														 String soLID = (String) soHashMapValues.get("LID");
														 //get the assume match ID 
														 String amSourcesID = (String) amValues.get("amID" + soSource + ":" + soLID);
														  if (amSourcesID != null) {
											%>
															  <td width="167px">
															     <div id="undoPreviewButton<%=i%>" >
															   <% if (operations.isAssumedMatch_Undo())   {%>
                                                                  <a href="javascript:void(0)" class='button' onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMpreview.jsf?&random=rand&amSourcesID=<%=amSourcesID%>&mainEUID=<%=mainEUID%>&divId=<%=i%>&AMID=<%=request.getParameter("AMID")%>','previewdiv','');document.getElementById('previewdiv').style.width='400px';"
																  title="<h:outputText value="#{msgs.undo_match_button_text}" />">
                                                                   <span><h:outputText value="#{msgs.undo_match_button_text}" /></span>
                                                                  </a>
																  </div>
																<%}%>
															   </td>
															 
										                 <%}%>
										          <%}%>
										      <%}%>
									<%}%>
                                    </tr>
                                </table>
                            </td>
						  </tr>
                      <%} else {%> 
					    <tr>
							<!--First Navigation --><!-- 176 fix 28-11-08 -->
							<td valign="top" align="right">
								<div id="first" onmouseout="changecolor(this)" style="cursor:hand;verticle-align:top;height:680px;overflow:hidden;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
									<table border="0" height="100%" title="<%=bundle.getString("first")%>" onclick="javascript:
									thisIdx = 0; 
									ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=prev&random=rand'+'&'+'AMID='+pages[thisIdx],'outputdiv','');" >
									<tr><td><img src='/<%=URI%>/images/chevrons-left.png'></td></tr> 
									<tr><td><img src='/<%=URI%>/images/chevrons-left.png'></td></tr> 
									 </table>
								</div>
							</td>

							<td valign="top" align="right">
 								<div id="prev" onmouseout="changecolor(this)" style="cursor:hand;verticle-align:top;height:680px;overflow:hidden;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
									<table border="0" height="100%" title="<%=bundle.getString("prev")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=prev&random=rand'+'&'+'AMID='+pages[--thisIdx],'outputdiv','');" >
									<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
									<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
									 </table>
								</div>
							</td>

						    <td valign="top">
							  <font style="color:red;overflow:hidden">
							 <%=bundle.getString("enterprise_object_not_found_error_message")%>&nbsp;<%=bundle.getString("undo_euid_message")%>
							   <font>
							 </td>

							<!--Next Navigation -->
							<td valign="top" align="left">
								<div id="next" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:680px;overflow:hidden;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
									<table border="0" height="100%" title="<%=bundle.getString("next")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=next&random=rand'+'&'+'AMID='+pages[++thisIdx],'outputdiv','');" >
									   <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
									   <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
									 </table>
								</div>
 							</td>
							<!--Last Navigation --><!-- 176 fix 28-11-08 -->
							<td valign="top" align="left" height="100%">
								<div id="last" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:680px;overflow:hidden;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
									<table border="0" height="100%" title="<%=bundle.getString("last")%>" onclick="javascript:
									var indx = pages.length-1;
									thisIdx = indx;
									ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=next&random=rand'+'&'+'AMID='+pages[thisIdx],'outputdiv','');" >
									   <tr><td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td></tr> 
									   <tr><td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td></tr> 
									 </table>
								</div>
							</td>

						</tr>
					  <%}%>
                        
                    </table>
                </div>				
<script>
var pageOperation = '<%=request.getParameter("operation")%>';

if (pages.length == 1)  {
       document.getElementById('next').style.visibility = 'hidden';	  
       document.getElementById('next').style.display = 'none';
	   
       document.getElementById('prev').style.visibility = 'hidden';
	   document.getElementById('prev').style.display = 'none';

	   document.getElementById('first').style.visibility = 'hidden';
       document.getElementById('first').style.display = 'none';

       document.getElementById('last').style.visibility = 'hidden';
       document.getElementById('last').style.display = 'hidden';

 } else if (thisIdx >= pages.length -1)  {
	   document.getElementById('next').style.visibility = 'hidden';
       document.getElementById('next').style.display = 'none';

       document.getElementById('prev').style.visibility = 'visible';
       document.getElementById('prev').style.display = 'block';

	   document.getElementById('last').style.visibility = 'hidden';
       document.getElementById('last').style.display = 'none';

       document.getElementById('first').style.visibility = 'visible';
       document.getElementById('first').style.display = 'block';
} else if (thisIdx <= 0)   {
	   document.getElementById('prev').style.visibility = 'hidden';
       document.getElementById('prev').style.display = 'none';

	   document.getElementById('next').style.visibility = 'visible';
       document.getElementById('next').style.display = 'block';

	   document.getElementById('first').style.visibility = 'hidden';
       document.getElementById('first').style.display = 'none';

       document.getElementById('last').style.visibility = 'visible';
       document.getElementById('last').style.display = 'block';
 }else if (thisIdx == 1){
       document.getElementById('first').style.visibility = 'hidden';
       document.getElementById('first').style.display = 'none';
 }else  if (thisIdx == pages.length-2){
       document.getElementById('last').style.visibility = 'hidden';
       document.getElementById('last').style.display = 'none';
 }
 </script>
 </body>
 </html>
<%}%> <!-- End session check if condition -->

</f:view>
