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

<f:view>
<f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">                
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/balloontip.js"></script>  
</head>
<title><h:outputText value="#{msgs.application_heading}"/></title> 
<body>
    <%@include file="./templates/header.jsp"%>
    <div id="mainContent">
        <div id="mainDupSource" class="compareResults" style="height:490px;width:1024px;overflow:auto;">
            <table cellspacing="5" cellpadding="0" border="0">
                <tr>
                    <td>
                        <a class="button" href='transactions.jsf'>
                            <span><h:outputText value="#{msgs.view_list_but_text}"/></span>
                        </a>
    
                    </td>
                </tr>
                <tr>
                  <%
                    ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                    //System.out.println("====ScreenObject pat details" + objScreenObject.getDisplayTitle());
                    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                    
                    //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
                    ArrayList objScreenObjectList =  objScreenObject.getSearchResultsConfig();
                    EPathArrayList ePathArrayList = compareDuplicateManager.retrievePatientResultsFields(objScreenObjectList);
                    
                    EPath ePath = null;
                    TransactionHandler transactionHandler = new TransactionHandler();
                    SourceHandler sourceHandler = new SourceHandler();
                    
                    //Object[] resultsConfigFeilds  = patientDetailsHandler.getSearchResultsScreenConfigArray().toArray();
                    Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
                    Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
                    
                    Object[] addressConfigFeilds = sourceHandler.getAddressFieldConfigs().toArray();
                    Object[] aliasConfigFeilds = sourceHandler.getAliasFieldConfigs().toArray();
                    Object[] phoneConfigFeilds = sourceHandler.getPhoneFieldConfigs().toArray();
                    Object[] auxidConfigFeilds = sourceHandler.getAuxIdFieldConfigs().toArray();
                    Object[] commentConfigFeilds = sourceHandler.getCommentFieldConfigs().toArray();

                    //System.out.println("objScreenObject.getSearchResultsConfig()" + patientDetailsHandler.getSearchResultsScreenConfigArray());
                    //System.out.println("resultsConfigFeilds legth RAJANI" + resultsConfigFeilds.length);
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                    ArrayList eoArrayList = new ArrayList();
                    EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
                    String transactionId = (String) (request.getParameter("transactionId")==null?request.getAttribute("transactionId"):request.getParameter("transactionId"));
                    
                    //System.out.println("PARAMETER ::" + (request.getParameter("transactionId")));
                    //System.out.println("ATTRIBUTE ::" + (request.getAttribute("transactionId")));
                    //System.out.println("TRANSACTION ID::" + transactionId);
                    
                    if (request.getParameter("transactionId") != null)   {  
                          
                         eoArrayList = transactionHandler.getTranscationDetails(transactionId);
                         
                         session.setAttribute("eoArrayList",eoArrayList);
                    } 
                    
                    eoArrayList = (ArrayList) session.getAttribute("eoArrayList");
                    
                    int countMain = 0;
                    int totalMainDuplicates = 0;
                    
                    //System.out.println("==eoArrayList==="+eoArrayList);
                    HashMap resultArrayMapMain = new HashMap();
                    HashMap resultArrayMapCompare = new HashMap();
                    SystemObject so = null;
                    String epathValue;
                    if(eoArrayList !=null) {%>  
                    <!-- Display the field Names first column-->
                    <td  valign="top">
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0">
                                 <tr><td>&nbsp;</td></tr>    
                                 <tr><td  class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.personal_info_heading}"/></font></td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <%
                        for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) personConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.address_details_heading}"/></font></td></tr>
                                 <%
                        for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) addressConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                                 <tr><td>&nbsp;</td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.phone_details_heading}"/></font></td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <%
                        for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) phoneConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                                 <tr><td>&nbsp;</td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.alias_details_heading}"/></font></td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <%
                        for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) aliasConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                                 <tr><td>&nbsp;</td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.auxid_details_heading}"/></font></td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <%
                        for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) auxidConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                                 <tr><td>&nbsp;</td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <tr><td>&nbsp;</td></tr>
                                 <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.comment_details_heading}"/></font></td></tr>
                             </table>
                         </div>
                         <div id="staticContent">
                             <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                 <%
                        for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                            FieldConfig fieldConfig = (FieldConfig) commentConfigFeilds[ifc];
                                 %>
                                 <tr>
                                     <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                 </tr>
                                 <%}%>
                                 
                             </table>
                         </div>
                    </td>
                    <!--end displaying first column-->       
                    <%
                      Object[] eoArrayListObjects = eoArrayList.toArray();
                      String dupHeading = "Main Euid";
                      String cssClass = "dynaprevieww169";
                      String cssMain ="maineuidpreview";
                      String cssSources = "differentSourceColour";
                      String cssTransaction = "differentTransactionColour";
                      String menuClass = "menutopBlue";
                      String dupfirstBlue = "dupfirstBlue";
                      for(int countEnt=0;countEnt<eoArrayListObjects.length;countEnt++) {
                      if(countEnt>0) {
                         cssClass = "dynaw169";
                         menuClass = "menutop";
                         dupfirstBlue = "dupfirst";
                         cssMain ="differentTransactionColour";
                      }
                      switch (countEnt) {
                              case 0:
                                  dupHeading = "<b>Before Update</b>";
                                  break;
                              case 1:
                                  dupHeading = "<b>After Update</b>";
                                  break;
                              
                              default:
                                  dupHeading = "<b>Preview</b>";
                                  break;
                          }

                        EnterpriseObject eo = (EnterpriseObject) eoArrayListObjects[countEnt];
                        Collection items = eo.getSystemObjects();
                        
                        ArrayList resultArrayListMain = new ArrayList();
                        ArrayList resultArrayListCompare = new ArrayList();
                        
                        Collection fieldvalues;
                   %>
                    <!-- Display the field Values-->
                    <td  valign="top">
                       <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                        <%if(countEnt == 0) {%>
                        <div id="mainEuidContent"  class="<%=cssMain%>">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                       <td valign="top" class="menutop"><%=dupHeading%></td>
                                </tr> 
                                <tr>
                                    <td valign="top" >
                                         <%=eo.getEUID()%>
                                    </td>
                                </tr>
                               </table>
                        </div> 
                        <%}else{%>
                        <div id="mainEuidContent">
                            <table border="0" cellspacing="0" cellpadding="0" class="<%=cssMain%>">
                                <tr>
                                    <td class="menutopTransaction"><%=dupHeading%></td>
                                </tr> 
                                <tr>
                                    <td class="fntdupgrouping" valign="top">
                                        <%=eo.getEUID()%>
                                    </td>
                                </tr>
                            </table>
                        </div> 
                        <%}%>
                        <div id="mainEuidContentButtonDiv" class="<%=cssMain%>">
                        <div id="assEuidDataContent" style="visibility:visible;display:block;">
                             <div id="personassEuidDataContent" style="visibility:visible;display:block;">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr>
                                    <%
                          HashMap fieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, personConfigFeilds);
                               String mainDOB;
                          ValueExpression fnameExpression;
                          ValueExpression fvalueVaueExpression;

                          for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                              FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                              if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                  epathValue = fieldConfigMap.getFullFieldName();
                              } else {
                                  epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                              }
                              if (countEnt > 0) {
                                  resultArrayMapCompare.put(epathValue, fieldValuesMapEO.get(epathValue));
                              } else {
                                  resultArrayMapMain.put(epathValue, fieldValuesMapEO.get(epathValue));

                              }
                                    %>  
                                        <tr>
                                            <td>
                                                <%if (fieldValuesMapEO.get(epathValue) != null ) {%>
                                                
                                                <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null
                                                        && resultArrayMapMain.get(epathValue)  != null) &&
                                                        !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
                                                %>
                                                    <font class="highlight">
                                                        <%if (fieldConfigMap.getValueType() == 6) {
                                                          mainDOB = simpleDateFormatFields.format(fieldValuesMapEO.get(epathValue));
                                                        %>
                                                        <%=mainDOB%>
                                                        <%} else {%>
                                                        <%=fieldValuesMapEO.get(epathValue)%>
                                                        <%}%>
                                                    </font>
                                                <%} else {
                                                  //System.out.println("Condition Failed!");
                                                %>
                                                <%if (fieldConfigMap.getValueType() == 6) {
                                                     mainDOB = simpleDateFormatFields.format(fieldValuesMapEO.get(epathValue));
                                                %>
                                                <%=mainDOB%>
                                                <%} else {%>
                                                <%=fieldValuesMapEO.get(epathValue)%>
                                                <%}%>
                                                <%}%>
                                                <%} else {%>
                                                &nbsp;
                                                <%}%>
                                                
                                            </td>
                                        </tr>
                                    <%
                          }
                                    %>
                                    <tr><td>&nbsp;</td></tr>
                                </table>
                            </div>
                            <div id="addressEuidDataContent" class="<%=cssMain%>">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td>&nbsp;</td></tr>

                                   <%
                HashMap addresfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, addressConfigFeilds);

                for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                    FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];

                    if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                        epathValue = fieldConfigMap.getFullFieldName();
                    } else {
                        epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                    }

                                    %>  
                                    <tr>
                                        <td>
                                            <%if (addresfieldValuesMapEO.get(epathValue) != null) {%>
                                            <%if (fieldConfigMap.getValueType() == 6) {
                                              mainDOB = simpleDateFormatFields.format(addresfieldValuesMapEO.get(epathValue));
                                            %>
                                            <b><%=mainDOB%></b>
                                            <%} else {%>
                                            <b><%=addresfieldValuesMapEO.get(epathValue)%></b>
                                            <%}%>
                                            <%} else {%>
                                            &nbsp; 
                                            <%}%>
                                        </td>
                                    </tr>
                                    <%
                                     }
                                    %>
                                    <%if (countEnt == 0) {%>
                                    <tr><td>&nbsp;</td></tr>
                                    <%}%>
                                </table>
                            </div>
                            <div id="phoneEuidDataContent" class="<%=cssMain%>">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                    <%
      HashMap phonefieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, phoneConfigFeilds);

      for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
          FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];

          if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
              epathValue = fieldConfigMap.getFullFieldName();
          } else {
              epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
          }

                                    %>  
                                    <tr>
                                        <td>
                                            <%if (addresfieldValuesMapEO.get(epathValue) != null) {%>
                                            <%if (fieldConfigMap.getValueType() == 6) {
mainDOB = simpleDateFormatFields.format(phonefieldValuesMapEO.get(epathValue));
                                            %>
                                            <b><%=mainDOB%></b>
                                            <%} else {%>
                                            <b><%=addresfieldValuesMapEO.get(epathValue)%></b>
                                            <%}%>
                                            <%} else {%>
                                            &nbsp;
                                            <%}%>
                                        </td>
                                    </tr>
                                    <%
      }
                                    %>
                                    
                                    <tr><td>&nbsp;</td></tr>
                                    
                                </table>
                            </div>
                            <div id="aliasEuidDataContent" class="<%=cssMain%>">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr><td>&nbsp;</td></tr>                                               
                                    <tr><td>&nbsp;</td></tr>      
                                    <tr>
                                    <%
      HashMap aliasfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, aliasConfigFeilds);

      for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
          FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];

          if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
              epathValue = fieldConfigMap.getFullFieldName();
          } else {
              epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
          }

                                    %>  
                                    <tr>
                                        <td>
                                            <%if (aliasfieldValuesMapEO.get(epathValue) != null) {%>
                                            <%if (fieldConfigMap.getValueType() == 6) {
    mainDOB = simpleDateFormatFields.format(aliasfieldValuesMapEO.get(epathValue));
                                            %>
                                            <b><%=mainDOB%></b>
                                            <%} else {%>
                                            <b><%=aliasfieldValuesMapEO.get(epathValue)%></b>
                                            <%}%>
                                            <%} else {%>
                                            &nbsp; 
                                            <%}%>
                                        </td>
                                    </tr>
                                    <%
      }
                                    %>
                                    
                                    <tr><td>&nbsp;</td></tr>
                                    
                                </table>
                            </div> 
                            <div id="auxidEuidDataContent" class="<%=cssMain%>">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                    <%
      HashMap auxidfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, auxidConfigFeilds);

      for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
          FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];

          if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
              epathValue = fieldConfigMap.getFullFieldName();
          } else {
              epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
          }

                                    %>  
                                    <tr>
                                        <td>
                                            <%if (auxidfieldValuesMapEO.get(epathValue) != null) {%>
                                            <%if (fieldConfigMap.getValueType() == 6) {
    mainDOB = simpleDateFormatFields.format(auxidfieldValuesMapEO.get(epathValue));
                                            %>
                                            <b><%=mainDOB%></b>
                                            <%} else {%>
                                            <b><%=auxidfieldValuesMapEO.get(epathValue)%></b>
                                            <%}%>
                                            <%} else {%>
                                            &nbsp; 
                                            <%}%>
                                        </td>
                                    </tr>
                                    <%
      }
                                    %> 
                                    
                                    <tr><td>&nbsp;</td></tr>
                                    
                                </table>
                            </div>
                            <div id="commentEuidDataContent" class="<%=cssMain%>">
                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                    <%
      HashMap commentfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, commentConfigFeilds);

      for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
          FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];

          if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
              epathValue = fieldConfigMap.getFullFieldName();
          } else {
              epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
          }

                                    %>  
                                    <tr>
                                        <td>
                                            <%if (commentfieldValuesMapEO.get(epathValue) != null) {%>
                                            <%if (fieldConfigMap.getValueType() == 6) {
mainDOB = simpleDateFormatFields.format(commentfieldValuesMapEO.get(epathValue));
                                            %>
                                            <b><%=mainDOB%></b>
                                            <%} else {%>
                                            <b><%=commentfieldValuesMapEO.get(epathValue)%></b>
                                            <%}%>
                                            <%} else {%>
                                            &nbsp; 
                                            <%}%>
                                        </td>
                                    </tr>
                                    <%
      }
                                    %>
                                    
                                    <tr><td>&nbsp;</td></tr>
                                    
                                </table>
                            </div>
                        
                        
                        <%
                        //System.out.println("eo" + eo);
                        Collection itemsSource = eo.getSystemObjects();
                        %>
                        <!--Displaying view sources and view history-->
                         <div id="dynamicMainEuidButtonContent<%=countEnt%>"  style="visibility:visible;display:block;" >
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td valign="top"><a href="javascript:showViewSources('mainDupSources','<%=itemsSource.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>')" class="viewbtn">View Sources</a></td> 
                                </tr>
                                         <% 
                                              boolean isMerge = transactionHandler.isEUIDMerge(transactionId);
                                              if(isMerge){
                                         %>                 
                                                <tr>
                                                    <td valign="top" colspan="2">
                                                        <h:outputLink styleClass="viewbtn"
                                                                      onclick="Javascript:showConfirm('unmergePopupDiv',event)" 
                                                                      value="Javascript:void(0)">
                                                            <h:outputText  value="#{msgs.Unmerge_but_text}"/>                                                          
                                                        </h:outputLink>
                                                    </td> 
                                                </tr>
                                              
                                            <% }else{%>
                                                <tr>
                                                    <td>&nbsp;</td> 
                                                </tr>
                                             <%}%>
                                         
                             </table>
                        </div>  
                        <!--END Displaying view sources-->
                        </div>
                       </div> 
                       </td>
                    <!--Start displaying the sources-->
                    <!-- Display the Sources Values-->
                    <%
                    Iterator itSource = itemsSource.iterator();
                    SystemObject soSource = null;
                    ArrayList resultArrayListSource = new ArrayList();
                    Object[] itemsSourceObj = itemsSource.toArray();
                    for(int soc=0;soc<itemsSourceObj.length;soc++) {              
                       soSource = (SystemObject) itemsSourceObj[soc];
                       Collection fieldvaluesSource;
                    %>                    
                    <td  valign="top">
                    
                     <div id="mainDupSources<%=countEnt%><%=soc%>" style="display:none;visibility:hidden;">
                                               <div id="mainEuidContent" class="<%=cssSources%>">
                                        <table border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td class="menutopSource"><b><%=soSource.getSystemCode()%></b></td>
                                            </tr> 
                                            <tr><td valign="top" class="dupfirst_source">
                                                    <%if (countEnt > 0) {%> 
                                                    <font style="text-decoration:none;color:#000000;"><b><%=soSource.getLID()%></b></font>
                                                    <%} else {%>
                                                    <font style="text-decoration:none;color:#000000;"><b><%=soSource.getLID()%></b></font>
                                                    <%}%>
                                                </td>
                                            </tr>
                                        </table>
                                    </div> 
                                    <div id="mainEuidDataContent<%=countEnt%>"  class="<%=cssSources%>">
                                                <div id="mainEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap personValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, personConfigFeilds);
                                        String mainDOBSource;
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
                                                                <%if (personValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(personValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=personValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                                        }
                                                        %>
                                                        
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                    </table>
                                                </div>
                                                <div id="addressEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap addressValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, addressConfigFeilds);

                                        for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                                            }

                                                        %>  
                                                        <tr>
                                                            <td>     
                                                                <%if (addressValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(addressValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=addressValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                        }  
                                                        %>
                                                        
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                    </table>
                                                </div>
                                                <div id="phoneEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap phoneValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, phoneConfigFeilds);

                                        for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                                            }

                                                        %>  
                                                        <tr>
                                                            <td>     
                                                                <%if (phoneValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(phoneValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=phoneValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                        }  
                                                        %>
                                                        
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                    </table>
                                                </div>
                                                <div id="aliasEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap aliasValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, aliasConfigFeilds);

                                        for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                                            }

                                                        %>  
                                                        <tr>
                                                            <td>     
                                                                <%if (aliasValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(aliasValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=aliasValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                        }  
                                                        %>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                    </table>
                                                </div>
                                                <div id="auxidEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap auxidValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, auxidConfigFeilds);

                                        for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                                            }

                                                        %>  
                                                        <tr>
                                                            <td>     
                                                                <%if (auxidValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(auxidValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=auxidValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                        }  
                                                        %>
                                                        
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                    </table>
                                                </div>
                                                <div id="commentEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                        HashMap commentValuesMapSource = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, commentConfigFeilds);

                                        for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName()+"." + fieldConfigMap.getFullFieldName();
                                            }

                                                        %>  
                                                        <tr>
                                                            <td>     
                                                                <%if (commentValuesMapSource.get(epathValue) != null) {%>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
        mainDOBSource = simpleDateFormatFields.format(commentValuesMapSource.get(epathValue));
                                                                %>
                                                                <%=mainDOBSource%>
                                                                <%} else {%>
                                                                <%=commentValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <%
                                        }  
                                                        %>
                                                        
                                                        
                                                        
                                                        
                                                    </table>
                                                </div>
                                                  
                                    </div>
                               </div>  <!--Main div for source--> 
                        <%ValueExpression tranNoValueExpressionviewunmerge = ExpressionFactory.newInstance().createValueExpression(transactionId, transactionId.getClass());%>
                          
                          <div id="unmergePopupDiv" class="alert" style="TOP: 2250px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                              <table cellpadding="0" cellspacing="0">
                                <h:form>
                                    <tr><th align="left">Confirmation</th><th align="right"><a href="javascript:void(0)" rel="unmergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_tran_popup_content_text}" /></td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                    <tr><td align="center">
                                            <h:commandLink styleClass="button" 
                                                           actionListener="#{TransactionHandler.unmergeEnterpriseObject}">
                                                <f:attribute name="tranNoValueExpressionviewunmerge" value="<%=tranNoValueExpressionviewunmerge%>"/>                   
                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                            </h:commandLink>   
                                            <h:outputLink  onclick="javascript:showConfirm('unmergePopupDiv',event)" 
                                                           styleClass="button"          
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
                 
                     </td>                        
                     <%}%>
                     <!--End displaying the sources-->
                     <%}%>
                     <td valign="top"><div id="previewPane"></div></td>
                    <%}%>
                   
                </tr>
             </table>
       </div>                                       
   </div>     
   <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>     
</body>
</html>
</f:view>
