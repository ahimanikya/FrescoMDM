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
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.TreeMap"  %>

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
<%@ page import="com.sun.mdm.index.master.MergeResult"  %>
 
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.TransactionHandler"  %>
  
 <%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
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
           <!--there is no custom header content for this example-->
     </head>
<%
 //Date Created : 03-July-2008
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
 <% String message = request.getParameter("msg");
	if (message != null && message.length() > 0)    {%>
     <script>
	    document.getElementById('messages').innerHTML = "<%=message%>";
     </script>
 <%}%>
<%          Enumeration parameterNames = request.getParameterNames();
            ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
			
			MasterControllerService masterControllerService = new MasterControllerService();

			Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	
			ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
            TransactionHandler transactionHandler = new TransactionHandler();
            //EPathArrayList ePathArrayList = midmUtilityManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();

            EPath ePath = null;
            SourceHandler sourceHandler = new SourceHandler();

            Operations operations = new Operations();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            String transactionId = (String) (request.getParameter("transactionId")==null?request.getAttribute("transactionId"):request.getParameter("transactionId"));
            String function  = (request.getParameter("function") != null)? request.getParameter("function") : "euidMerge";

			String unMergeFinalStr = request.getParameter("unMergeFinal");
            boolean isUnMergeFinal = (null == unMergeFinalStr?false:true);

			//unMergePreview
			String unMergePreviewStr = request.getParameter("unMergePreview");
            boolean isUnMergePreview = (null == unMergePreviewStr?false:true);
            //cancelUnmerge
            String cancelUnmergeStr = request.getParameter("cancelUnmerge");
            boolean isCancelUnmerge = (null == cancelUnmergeStr?false:true);
            
            String  unmergeTransactionId = request.getParameter("unmergeTransactionId");
			String mainEuid  = request.getParameter("mainEuid");
            String mainEUID = new String();
			String rootNodeValue = "";
            if (request.getParameter("transactionId") != null) {
                eoArrayList = transactionHandler.getTranscationDetails(transactionId);
                request.setAttribute("comapreEuidsArrayList", eoArrayList);
            }

			String localIdDesignation =	 ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");


            HashMap unmergeHashMap  = new HashMap();
            HashMap unmergePreviewHashMap  = new HashMap();

			TreeMap transDetailsMap = (TreeMap) session.getAttribute("transdetails");;

			if(isCancelUnmerge) {
    %> 
	  <table><tr><td>
        <script>
		   ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?random=rand'+'&'+'transactionId='+pages[thisIdx]+'&function='+functions[thisIdx],'mainDupSource','');
		</script>
		</td></tr>
		</table>
                
 <%   request.setAttribute("comapreEuidsArrayList", eoArrayList); 
}

			%>



	<%		if(isUnMergeFinal) {
                //eoArrayList = transactionHandler.getTranscationDetails(unmergeTransactionId);
				unmergeHashMap = transactionHandler.unmergeEnterpriseObject(unmergeTransactionId, mainEuid);
				messagesIter = FacesContext.getCurrentInstance().getMessages(); 
                if(unmergeHashMap != null ) {
				  if(unmergeHashMap.get("CONCURRENT_MOD_ERROR") != null ) {%> <!-- If concurrent modification-->
				  <table>
				    <tr>
				    <td>

             	   <script>
 					   alert("<%=unmergeHashMap.get("CONCURRENT_MOD_ERROR")%>");
                 	   ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?'+'&rand=<%=rand%>&transactionId=<%=unmergeTransactionId%>&function=euidMerge','mainDupSource','');
              	   </script>
				   </td>
				   </tr>
				 </td>

				<%} else {
					MergeResult unMergeResult  = (MergeResult) unmergeHashMap.get("unMergeResult");
					String unmergeTransNumber  = unMergeResult.getTransactionObject().getTransactionNumber();
                    String unmergeFunction    = unMergeResult.getTransactionObject().getFunction();
				    transDetailsMap.put(unmergeTransNumber,unmergeFunction);
 					session.setAttribute("transdetails",transDetailsMap);
					%>
				  <table>
				    <tr>
				    <td>
              	   <script>
 					  document.getElementById('messages').innerHTML="<%=unMergeResult.getSourceEO().getEUID()%>&nbsp;<%=bundle.getString("unmerged_from")%>&nbsp;<%=unMergeResult.getDestinationEO().getEUID()%>";
					  pages[thisIdx] = "<%=unmergeTransNumber%>";
					  functions[thisIdx] = "<%=unmergeFunction%>";
					  functionDesc[thisIdx] = "<%=ValidationService.getInstance().getDescription(ValidationService.CONFIG_MODULE_FUNCTION, unmergeFunction)%>";
					   
					  ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?random=rand'+'&'+'transactionId='+pages[thisIdx]+'&function='+functions[thisIdx],'mainDupSource','');
               	   </script>
				   </td>
				   </tr>
				 </td>
		 

				<%}%>

				<%} else {%> <!-- If unmerge fails modification-->
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

                
            <% request.setAttribute("comapreEuidsArrayList", eoArrayList);

			} else {%>
				<table>
				  <tr>
					<td> 
						<script>
								document.getElementById('messages').innerHTML  = ""; 
			 document.getElementById("transDetailsDiv").innerHTML  = "<%=bundle.getString("datatable_transactionid_text")%>: <b>" + pages[thisIdx] + "</b> <%=bundle.getString("transaction_function")%> : <b>"+ functionDesc[thisIdx] + "</b>" + "  <%=bundle.getString("Transaction_data_table_systemuser_column")%>  :  <b>" + systemUsers[thisIdx] + "</b>  <%=bundle.getString("Transaction_data_table_timestamp_column")%>  : <b>" + timeStamp[thisIdx] + "</b>"; 
						</script>
				   </td>
				</tr>
				</table>


			<%}%>



<%
				//Variables for euid merge  
  			 messagesIter = FacesContext.getCurrentInstance().getMessages(); 

            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
			int maxMinorObjectsMAX = 0;
            ValueExpression sourceEUIDVaueExpression = null;
            ValueExpression destinationEUIDVaueExpression = null;
            ValueExpression mergredHashMapVaueExpression = null;
            EnterpriseObject sourceEO = null;
            EnterpriseObject destinationEO = null;
      		
			HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();

			HashMap minorObjectMapMain = new HashMap();
            HashMap minorObjectMapCompare = new HashMap();

            ArrayList  minorObjectMapListMain = new ArrayList();
            HashMap  minorObjectCompareHashMap = new HashMap();
            ArrayList  minorObjectMapListCompare = new ArrayList();

            SystemObject so = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;

            if ( eoArrayList != null && eoArrayList.size() > 0 ) {
            %>  
               <table cellspacing="0" cellpadding="0" border="0">
			            <tr><td><div id="transDetailsDiv" style="font-color:#000000"></div></td></tr>
                        <tr>
                            <td>
                                <div style="height:500px;overflow:scroll;">
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
<!-- 176 fix 28-11-08 -->
<!--First Navigation -->
<td valign="top" align="left" height="100%">
	<div id="first" onmouseout="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("first")%>" onclick="javascript:
 		thisIdx = 0;
		ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?operation=prev&random=rand'+'&'+'transactionId='+pages[thisIdx]+'&function='+functions[thisIdx],'mainDupSource','');" >
		<tr>
			<td><img src='/<%=URI%>/images/chevrons-left.png'></td>
		</tr> 
		<tr>
			<td><img src='/<%=URI%>/images/chevrons-left.png'></td>
 		</tr> 
         </table>
	</div>
</td>
<!--Prev Navigation -->
<td valign="top" align="right" height="100%">
	<div id="prev" onmouseout="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("prev")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?operation=prev&random=rand'+'&'+'transactionId='+pages[thisIdx-1]+'&function='+functions[--thisIdx],'mainDupSource','');" >
		<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
		<tr><td><img src='/<%=URI%>/images/turner_arrow_left.gif'></td></tr> 
         </table>
	</div>
</td>
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
                                            String subscripts[] = midmUtilityManager.getSubscript(eoArrayListObjects.length);
                                             if (eoArrayListObjects.length == 1) {
                                              styleClass = "blue";
                                            }
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                String eoStatus = (String) eoHashMapValues.get("EO_STATUS");

                                                if ("Add".equalsIgnoreCase(function)) {
                                                        dupHeading = "<b>" + function + "</b>";
                                                } else if ("euidMerge".equalsIgnoreCase(function) || "lidMerge".equalsIgnoreCase(function) ){
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
                                                } else if ("euidUnMerge".equalsIgnoreCase(function) || "lidUnMerge".equalsIgnoreCase(function)) {
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
                                                ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
                   %>
                                          <%if (countEnt == 0) {%>
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
                                                            <div id="personassEuidDataContent" class="yellow">
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0" >
																<tr><td>EUID</td></tr>
																<tr><td><h:outputText value="#{msgs.source_rec_status_but}"/></td></tr>
                                                                    <%

                                                                String mainDOB;
                                                                String epathValue;

                                                              for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                                 FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
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
                                            
                                            <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="menutop"><%=dupHeading%></td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td>
                                                                       <font style="text-decoration:none;color:#000000;"><b>
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </b></font>
                                                                    </td>
                                                                </tr>
                                                         </table>
                                                    </div>
                                                </div>
                                                
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="yellow">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(eoStatus)%>
																</font></td></tr>
																
                                                                    <%

                                    String mainDOB;
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
  
                                                                                %>
                                                                                    <font class="highlight">
											<%if(eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
                                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%}else{%>
																							<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																							<%}else{%>
																							<%=personfieldValuesMapEO.get(epathValue)%> 
																							<%}%>
                                                                                     <%}%>
                                                                                    </font>
                                                                                  <%} else {%>
 										<%if(resultArrayMapMain.get(epathValue) == null ) { %>
                                                                                    <font class="highlight">
											 <%if(eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
													       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																							<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																							<%}else{%>
																							<%=personfieldValuesMapEO.get(epathValue)%> 
																							<%}%>
                                                                                       <%}%>
                                                                                    </font>
																					 <%}else {%>
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																							<%rootNodeValue =  personfieldValuesMapEO.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=personfieldValuesMapEO.get(epathValue)%>">... </a>
																							<%}else{%>
																							<%=personfieldValuesMapEO.get(epathValue)%> 
																							<%}%>
                                                                                       <%}%>
                                                                                     <%}%>
                                                                                   <%}%>

                                                                                <%} else {%>
 																					<%if(resultArrayMapMain.get(epathValue) != null ) { %>
                                                                                    <font class="highlight">
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
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
maxMinorObjectsMAX  = midmUtilityManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


                                                                    ArrayList  minorObjectMapList =  (ArrayList) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");

if (countEnt == 0) {
     minorObjectCompareHashMap.put("EO" + childObjectNodeConfig.getName() + "ArrayList",minorObjectMapList);
 }
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
																    boolean sameMinorObject  = false;
                                                                    for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
																	  minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);%>
  
<%if (countEnt > 0) {
   minorObjectMapCompare =  midmUtilityManager.getDifferenceMinorObjectMap((ArrayList)minorObjectCompareHashMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList"),minorObjectHashMap);
 }
%>



										 				  <%for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                       epathValue = fieldConfigMap.getFullFieldName();
																	   %>
  
                                                                    <tr>
                                                                        <td>

                                                                                <!-- if minor objects exists -->
                                                                                 <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
																				 															
                                                                                <%if (countEnt > 0 && minorObjectMapCompare != null 
																				   && minorObjectMapCompare.get(epathValue) != null  &&
                                                                                minorObjectMapCompare.get(epathValue).toString().equalsIgnoreCase("true") ){%>
                                                                                      <font class="highlight">
											                                         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
                                                                                       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else{%>
																						<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																						if(rootNodeValue.length()>20){
																						rootNodeValue = rootNodeValue.substring(0,20);
																						%>
																						<%=rootNodeValue%>
																						<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
																						<%}else{%>
																						<%=minorObjectHashMap.get(epathValue)%> 
																						<%}%>
                                                                                     <%}%>
                                                                                    </font>
																			   <%} else {%>
 																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																							<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
																							<%}else{%>
																							<%=minorObjectHashMap.get(epathValue)%> 
																							<%}%>
                                                                                       <%}%>
                                                                                <%}%>
  
                                                                                <%} else {%>
  
                                                                               <%if (countEnt > 0 && minorObjectMapCompare !=null 
																				   && minorObjectMapCompare.get(epathValue) != null  &&
                                                                                minorObjectMapCompare.get(epathValue).toString().equalsIgnoreCase("true") ){%>
                                                                                     <font class="highlight">
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																						 <img src="./images/calup.gif" border="0" alt="Blank Value"/>
                                                                                       <%}%>
                                                                                    </font>
                                                                                 <%} else {%>
																				  &nbsp;
																				 <%}%>
  																			<%}%>
                                                                         </td>
                                                                    </tr>
                                                                  <%
                                                                      } //FIELD CONFIG LOOP
																 %>
                                                                  <tr><td>&nbsp;</td></tr>

                                                                  <%  
																	  //minorObjectMapListCompare = new ArrayList(); 
 																	  minorObjectMapCompare = new HashMap();
																	  } // TOTAL MINOR OBJECTS LOOP
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=midmUtilityManager.getStatus(soStatus)%>
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
                                                                                
 
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
																						<%rootNodeValue =  soHashMapValues.get(epathValue).toString();
																						if(rootNodeValue.length()>20){
																						rootNodeValue = rootNodeValue.substring(0,20);
																						%>
																						<%=rootNodeValue%>
																						<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=soHashMapValues.get(epathValue)%>">... </a>
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
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
																							<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
																							<%}else{%>
																							<%=minorObjectHashMap.get(epathValue)%> 
																							<%}%>
                                                                                     <%}%>
																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
																							<%rootNodeValue =  minorObjectHashMap.get(epathValue).toString();
																							if(rootNodeValue.length()>20){
																							rootNodeValue = rootNodeValue.substring(0,20);
																							%>
																							<%=rootNodeValue%>
																							<a href="javascript:void(0)"  style="color:blue;font-weight:bold;text-decoration:none;" title="<%=fieldConfigMap.getDisplayName()%>:  <%=minorObjectHashMap.get(epathValue)%>">... </a>
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

<!--Next Navigation -->
<td valign="top" align="left" height="100%">
	<div id="next" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("next")%>" onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?operation=next&random=rand'+'&'+'transactionId='+pages[thisIdx+1]+'&function='+functions[++thisIdx],'mainDupSource','');" >
           <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
           <tr><td><img src='/<%=URI%>/images/turner_arrow_right.gif' border="0"></td></tr> 
         </table>
	</div>
 </td>
<!-- 176 fix 28-11_08 -->
<!--Last Navigation -->
<td valign="top" align="left" height="100%">
	<div id="last" onmouseout="changecolor(this)"  onmousemovein="changecolor(this)" style="height:100%;cursor:hand;verticle-align:top;position:relative;width:20px;border-bottom:1px outset;border-top:1px outset;border-right:1px outset;border-left:1px outset;border-left:1px inset;background-color:#e7e7d6">
		<table border="0" height="100%" title="<%=bundle.getString("last")%>" onclick="javascript:
		var indx = pages.length-2;
		thisIdx = indx;
		ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?operation=next&random=rand'+'&'+'transactionId='+pages[thisIdx+1]+'&function='+functions[++thisIdx],'mainDupSource','');" >
           <tr>
				<td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td>
 		   </tr> 
           <tr>
				<td><img src='/<%=URI%>/images/chevrons-right.png' border="0"></td>
 		   </tr> 
         </table>
	</div>
 </td>
          
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
                                        <% 
										 int unmergeSourcesSize = 0;	
										for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                          HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                          HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
										  String euid = (String) personfieldValuesMapEO.get("EUID");
										  eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");
 
										  if(countEnt == 0) {
											  mainEuid = euid;
										  }

											   %>
  									    <script>
												 euidValueArray.push('<%=euid%>');
										</script>
                                 		<% if (countEnt == 0) {%>
                                        <td><img src='/<%=URI%>/images/spacer.gif' border="0" width="40px"></td>
										<td width="169px" valign="top">
                                          <table cellpadding="0" cellspacing="0">
										    <tr>
											  <td valign="top">  
                                                   <a href="transactions.jsf" class="button" title="<h:outputText value="#{msgs.search_again}"/>" >
															 <span><h:outputText value="#{msgs.search_again}"/></span>
												   </a>											  
											  </td>
											</tr>
										  </table>
										</td>
                                        <!--Displaying view sources and view history-->
                                        <% }%>

                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table cellspacing="0" cellpadding="0" border="0">
                                                         <tr> 
                                                          <td valign="top">
                                                             <a title="<h:outputText value="#{msgs.view_sources_text}"/>"  href="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','0',euidValueArray)" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                          </td>                                              
                                                        </tr>
                                                        <%
											 
                                                         boolean isMerge = ("euidMerge".equalsIgnoreCase(function) || "lidMerge".equalsIgnoreCase(function)) ? transactionHandler.isEUIDMerge(transactionId): false;
                                                         if (isMerge && countEnt == 1 ) {
															 unmergeSourcesSize = eoSources.size();
                                                             EnterpriseObject mainMergeEO = masterControllerService.getEnterpriseObject(mainEuid);
															 if(mainMergeEO != null ) {
																 session.setAttribute("SBR_REVISION_NUMBER"+mainMergeEO.getEUID(), mainMergeEO.getSBR().getRevisionNumber()); 
															 }
                                                         %>                 
                                                        <tr>
                                                            <td valign="top" colspan="2">
                                                            <div id="unMergePreviewButtons" >
   
															<a href="javascript:void(0)" 
															   onclick="javascript:document.getElementById('unmergeTransactionId').value='<%=transactionId%>';document.getElementById('mainEuid').value='<%=mainEuid%>';getFormValues('unmergeForm');ajaxURL('/<%=URI%>/ajaxservices/unmergepreview.jsf?'+queryStr+'&unMergePreview=true&rand=<%=rand%>','unmergePreviewPane','');document.getElementById('unMergePreviewButtons').style.visibility='hidden';document.getElementById('unMergePreviewButtons').style.display='none';" 
															   class="button" title="<h:outputText  value="#{msgs.preview_column_text}"/>&nbsp;<%=("euidMerge".equalsIgnoreCase(function))?"EUID":localIdDesignation%>&nbsp;<h:outputText  value="#{msgs.Unmerge_but_text}"/>"><span><h:outputText  value="#{msgs.preview_column_text}"/>&nbsp;<h:outputText  value="#{msgs.Unmerge_but_text}"/></span>
															</a>   
														</div>
                                                            </td> 
                                                        </tr>
                                                        <% } else if (!isMerge && countEnt == 1 ) {%>
														  <%if("euidMerge".equalsIgnoreCase(function) || "lidMerge".equalsIgnoreCase(function)) {%>
                                                           <tr><td><h:outputText style="color:red" value="#{msgs.unmerge_button_not_avail_text} "/>&nbsp;</td></tr>
														  <%}%>
                                                        <%}%>
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
										  <td>
										   </td>
                                       </tr>
                    					      </table>
                                </div>
                            </td>
                        </tr>
                      <!-- eo ArrayList NOT NULL CONDITION  -->  
                    </table>
                    <%} else {%> <!-- If the transaction details are not found -->
					 <% if(!isUnMergeFinal) {%>
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
                   <%}%>
  <%}%> <!-- if session is active -->
  <!-- 176 fix on 28-11-08 -->
<script>
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

</html>


</f:view>
