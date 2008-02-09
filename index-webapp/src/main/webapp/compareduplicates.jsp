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
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
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
                                    <h:outputLabel for="#{msgs.patdetails_search_euid}" value="#{msgs.patdetails_search_euid}"/>
                                </td>
                                <td align="left">
                                    <h:inputText   
                                        id="createStDateField"
                                        value="#{PatientDetailsHandler.EUID}" 
                                        maxlength="10"/>
                                </td> 
                                <td>                                    
                                    <h:commandLink  styleClass="button" action="#{PatientDetailsHandler.performSubmit}">  
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
                        </table>
                    </h:form>
                </div>
                <div id="errorDiv" style="padding-left:350px;">
                    <h:messages style="color: red;font-size:12px" id="errorMessages" layout="table" />
                </div>
                <br>        
                <div id="mainDupSource" class="compareResults" style="height:490px;width:1024px;overflow:auto;">
                    <table cellspacing="5" cellpadding="0" border="0">
                        <tr>
                                <%
                ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();
            EPathArrayList ePathArrayList = compareDuplicateManager.retrievePatientResultsFields(objScreenObjectList);

            EPath ePath = null;
            PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();

            //Object[] resultsConfigFeilds  = patientDetailsHandler.getSearchResultsScreenConfigArray().toArray();
            Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            Object[] addressConfigFeilds = sourceHandler.getAddressFieldConfigs().toArray();
            Object[] aliasConfigFeilds = sourceHandler.getAliasFieldConfigs().toArray();
            Object[] phoneConfigFeilds = sourceHandler.getPhoneFieldConfigs().toArray();
            Object[] auxidConfigFeilds = sourceHandler.getAuxIdFieldConfigs().toArray();
            Object[] commentConfigFeilds = sourceHandler.getCommentFieldConfigs().toArray();

            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
            ArrayList eoArrayList = (ArrayList) session.getAttribute("enterpriseArrayList");
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ValueExpression sourceEUIDVaueExpression = null;
            ValueExpression destinationEUIDVaueExpression = null;
            ValueExpression mergredHashMapVaueExpression = null;
            EnterpriseObject sourceEO = null;
            EnterpriseObject destinationEO = null;

            if (eoArrayList != null) {%>  
                            <!-- Display the field Names first column-->
                               <td valign="top">
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
                                                                <tr><td>&nbsp;</td></tr>
                                                            </table>
                                                        </div>
                                        <div id="staticContent">
                                                            <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td class="fntdupgrouping"><font style="color:blue;"><h:outputText value="#{msgs.address_details_heading}"/></font></td></tr>
                                            </table>
                                        </div>
                                       <div id="staticContent">
                                            <table border="0" cellspacing="0" cellpadding="0" class="w114">
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
                                String cssMain ="maineuidpreview";
                                String cssClass = "dynaw169";
                                String cssHistory = "differentHistoryColour";
                                String cssSources = "differentSourceColour";
                                String cssDiffPerson = "differentPersonColour";
                                String menuClass = "menutop";
                                String dupfirstBlue = "dupfirst";
                                String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                
                                for ( countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                    if (countEnt > 0) {
                                        cssMain ="maineuidpreview";
                                        cssClass = "dynaw169";
                                        menuClass = "menutop";
                                        dupfirstBlue = "dupfirst";
                                    }                                  
                                    
                                   if (countEnt>0)
                                   {    dupHeading = "<b> "+countEnt+"<sup>"+subscripts[countEnt] +"</sup> Duplicate</b>";
                                   } else if (countEnt==0)
                                   {    dupHeading = "<b> Main EUID</b>";
                                   }

                                    EnterpriseObject eo = (EnterpriseObject) eoArrayListObjects[countEnt];
                                    Collection items = eo.getSystemObjects();

                                    ArrayList resultArrayListMain = new ArrayList();
                                    ArrayList resultArrayListCompare = new ArrayList();

                                    so = (SystemObject) (eo.getSystemObjects().toArray()[0]);
                                    Collection fieldvalues;
                                    sourceEO = (EnterpriseObject) eoArrayListObjects[0];
                                    destinationEO = (EnterpriseObject) eoArrayListObjects[countEnt];
                                    String  potDupStatus = null;
                   %>
                           <!-- Display the field Values-->
                               <td  valign="top">
                                   <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                                    <div style="width:170px;overflow:auto">
                                        <div id="mainEuidContent" class="w169" style="visibility:visible;display:block;">
                                            <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                <tr>
                                                    <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                </tr> 
                                                <h:form>
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                            <%
                                                               if (countEnt > 0) {
                                                                sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                                destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                                            %> 
                                                            <!--a href="javascript:populatePreviewDiv('<%=countEnt%>')" class="dupbtn"> <%=eo.getEUID()%></a-->
                                                            <h:commandLink styleClass="dupbtn" actionListener="#{PatientDetailsHandler.keepEuidsAction}">
                                                                <f:attribute name="sourceEUID" value="<%=sourceEUIDVaueExpression%>"/>
                                                                <f:attribute name="destinationEUID" value="<%=destinationEUIDVaueExpression%>"/>
                                                                <h:outputText value="<%=destinationEUIDVaueExpression%>"/>
                                                            </h:commandLink>
                                                            <%} else {%>
                                                            <a href="#" class="dupbtn"> <%=eo.getEUID()%></a>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                </h:form>
                                            </table>
                                        </div>
                                    </div>
                                       <%if (countEnt > 0) {
                                                  sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                  destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                                  potDupStatus = compareDuplicateManager.getPotentialDupStatus(sourceEO.getEUID(),destinationEO.getEUID());
                                         }         
                                       %>

                                    <div id="Anim<%=countEnt%>" class="curtainClose">  </div>
                                   <% if(countEnt > 0 && "A".equalsIgnoreCase(potDupStatus)|| "R".equalsIgnoreCase(potDupStatus)) {%>
                                     <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssDiffPerson%>">
                                    <%}else{%>    
                                     <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                    <%}%>    
                                    <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                <div id="personassEuidDataContent" style="visibility:visible;display:block;">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <tr>
                                                    <%
                                    HashMap personfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, personConfigFeilds);

                                    String mainDOB;
                                    ValueExpression fnameExpression;
                                    ValueExpression fvalueVaueExpression;
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
                                                    <h:form>
                                                        <tr>
                                                            <td>
                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
                                                                
                                                                <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
        fnameExpression = ExpressionFactory.newInstance().createValueExpression(epathValue, epathValue.getClass());
        fvalueVaueExpression = ExpressionFactory.newInstance().createValueExpression(personfieldValuesMapEO.get(epathValue), personfieldValuesMapEO.get(epathValue).getClass());

                                                                %>
                                                                <h:commandLink actionListener="#{PatientDetailsHandler.setPreviewEnterpriseObjectValues}">
                                                                    <f:attribute name="fnameExpression" value="<%=fnameExpression%>"/>
                                                                    <f:attribute name="fvalueVaueExpression" value="<%=fvalueVaueExpression%>"/>
                                                                    
                                                                    <font class="highlight">
                                                                        <%if (fieldConfigMap.getValueType() == 6) {
                                                                         mainDOB = simpleDateFormatFields.format(personfieldValuesMapEO.get(epathValue));
                                                                        %>
                                                                        <%=mainDOB%>
                                                                        <%} else {%>
                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                        <%}%>
                                                                    </font>
                                                                </h:commandLink>  
                                                                <%} else {
                                                                %>
                                                                <%if (fieldConfigMap.getValueType() == 6) {
                                                                 mainDOB = simpleDateFormatFields.format(personfieldValuesMapEO.get(epathValue));
                                                                %>
                                                                <%=mainDOB%>
                                                                <%} else {%>
                                                                <%=personfieldValuesMapEO.get(epathValue)%>
                                                                <%}%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                
                                                            </td>
                                                        </tr>
                                                    </h:form>
                                                    <%
                                                      }
                                                    %>
                                                </table>
                                            </div>
                                                <div id="addressEuidDataContent" class="<%=cssMain%>">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                       <%
                                    HashMap addresfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, addressConfigFeilds);

                                    for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                                    </table>
                                                </div>
                                                <div id="phoneEuidDataContent" class="<%=cssMain%>">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                        <tr>
                                                        <%
                                    HashMap phonefieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, phoneConfigFeilds);

                                    for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = "Person." + fieldConfigMap.getFullFieldName();
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

                                        if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                                    </table>
                                                </div> 
                                                <div id="auxidEuidDataContent" class="<%=cssMain%>">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                        <tr>
                                                        <%
                                    HashMap auxidfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, auxidConfigFeilds);

                                    for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                                    </table>
                                                </div>
                                                <div id="commentEuidDataContent" class="<%=cssMain%>">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <tr><td>&nbsp;</td></tr>
                                                        <tr><td>&nbsp;</td></tr>
                                                        
                                                        <tr>
                                                        <%
                                    HashMap commentfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, commentConfigFeilds);

                                    for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                                    </table>
                                                </div>
                                            <%if (countEnt > 0) {
                                                  sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                  destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                                  potDupStatus = compareDuplicateManager.getPotentialDupStatus(sourceEO.getEUID(),destinationEO.getEUID());
                                       %>

                                            <!--Displaying different Person -->
                                             <% if("A".equalsIgnoreCase(potDupStatus)|| "R".equalsIgnoreCase(potDupStatus)) {%>
                                             <div id="potentialDuplicate<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr><td>&nbsp;</td></tr>
                                                    <tr><td>&nbsp;</td></tr>
                                                    <tr>
                                                        <td valign="top">
                                                            <h:form>
                                                            <h:commandLink  styleClass="diffviewbtn" 
                                                                            actionListener="#{PatientDetailsHandler.unresolvePotentialDuplicateAction}">
                                                                <f:attribute name="sourceEUID" value="<%=sourceEUIDVaueExpression%>"/>
                                                                <f:attribute name="destinationEUID" value="<%=destinationEUIDVaueExpression%>"/>
                                                                <h:outputText value="#{msgs.potential_dup_button}"/>
                                                            </h:commandLink>   
                                                            </h:form>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>    
                                               
                                              <%} else {%>
                                               <div id="differentPerson<%=countEnt%>"  >
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top">
                                                            <a href="javascript:void(0)" class="diffviewbtn"
                                                            onclick="Javascript:togglePDDivs('resolvePopupDiv','<%=countEnt*169%>','<%=resultsConfigFeilds.length*28%>')">
                                                            <h:outputText value="#{msgs.diff_person_heading_text}"/>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div> 
                                            <%}%>   
                                            
                                            <%} else {%>
                                            <!--Displaying different Person -->
                                            <div style="visibility:visible;display:block;" >
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">&nbsp;</td>
                                                    </tr>
                                                </table>
                                            </div>        
                                            <%}%>
                                            <%
                                    EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(eo.getEUID());
                                    Collection itemsSource = eoSource.getSystemObjects();

                                            %>
                                            <!--Displaying view sources and view history-->
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>"  style="visibility:visible;display:block;" >
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <h:form>
                                                        
                                                        <tr> 
                                                            <td valign="top">
                                                                <a class="viewbtn" href="javascript:showViewSources('mainDupHistory','<%=itemsSource.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>')" >  
                                                                    <h:outputText value="#{msgs.view_history_text}"/>
                                                                </a>      
                                                            </td>                                              
                                                        </tr>
                                                        <tr>
                                                            <td valign="top">
                                                                <a class="viewbtn" href="javascript:showViewSources('mainDupSources','<%=itemsSource.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>')" >  
                                                                    <h:outputText value="#{msgs.view_sources_text}"/>
                                                                </a>      
                                                            </td> 
                                                        </tr>
                                                    </h:form>
                                                </table>
                                            </div>  
                                            <!--END Displaying view sources and view history-->
                                        </div>
                                    </div>
                                </div>
                                       <%if (countEnt > 0) {
                                                  sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                  destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                       %>                                                  
                                           <div id="resolvePopupDiv" class="alert" style="visibility:hidden;display:none;">
                                           
                                           <table cellpadding="0" cellspacing="0">
                                               <h:form>
                                                   <tr><th align="left"><h:outputText value="#{msgs.diff_person_heading_text}"/></th><th align="right"><a href="javascript:void(0)" rel="resolvepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                                   <tr><td colspan="2"> &nbsp;</td></tr>
                                                   <tr>
                                                       <td colspan="2"><h:outputText value="#{msgs.different_person_dailog_text}"/></td>
                                                   </tr>
                                                   <tr><td colspan="2"> &nbsp;</td></tr>
                                                   <tr>
                                                       <td  colspan="2">
                                                           <div class="selectContent">
                                                               <h:selectOneRadio id="diffperson" value="#{PatientDetailsHandler.resolveType}">
                                                                   <f:selectItem  itemValue="ResolvePermanently" itemLabel="Resolve Permanently "/>
                                                                   <f:selectItem  itemValue="AutoResolve" itemLabel="AutoResolve"/>
                                                               </h:selectOneRadio> 
                                                           </div> 
                                                       </td>
                                                   </tr>
                                                   <tr><td colspan="2"> &nbsp;</td></tr>
                                                   <tr>
                                                       <td colspan="2" align="center">
                                                           <%
    sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
    destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());

                                                           %>
                                                           <h:commandLink styleClass="button" 
                                                                          actionListener="#{PatientDetailsHandler.makeDifferentPersonAction}">
                                                               <f:attribute name="sourceEUID" value="<%=sourceEUIDVaueExpression%>"/>
                                                               <f:attribute name="destinationEUID" value="<%=destinationEUIDVaueExpression%>"/>
                                                               <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                           </h:commandLink>   
                                                           <h:outputLink  onclick="Javascript:togglePDDivs('resolvePopupDiv','<%=countEnt*169%>','<%=resultsConfigFeilds.length*28%>')"
                                                                          styleClass="button"  
                                                                          value="javascript:void(0)">
                                                               <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                           </h:outputLink>   
                                                   </td></tr>
                                                   <tr>
                                                       <td colspan="2">
                                                           <h:messages style="font-size:10px; font-color:red;" layout="table" />
                                                       </td>
                                                   </tr>        
                                               </h:form>
                                           </table>
                                       </div>    
                                       <%}%>
                            </td>
                            <!--Start displaying the sources-->
                            <!-- Display the Sources Values-->
                            <%

                                    Iterator itSource = itemsSource.iterator();
                                    SystemObject soSource = null;
                                    ArrayList resultArrayListSource = new ArrayList();
                                    Object[] itemsSourceObj = itemsSource.toArray();
                                    //while (itSource.hasNext()) {
                                    //soSource = (SystemObject) itSource.next();
                                    for (int soc = 0; soc < itemsSourceObj.length; soc++) {
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
                                        HashMap personValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, personConfigFeilds);
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
                                        HashMap addressValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, addressConfigFeilds);

                                        for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                        HashMap phoneValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, phoneConfigFeilds);

                                        for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                        HashMap aliasValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, aliasConfigFeilds);

                                        for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                        HashMap auxidValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, auxidConfigFeilds);

                                        for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                        HashMap commentValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject, commentConfigFeilds);

                                        for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                            FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];
                                            if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                    
                                </div> <!--Main div for source-->                        
                            </td>                        
                            <%}%>
                            <!--Start displaying the history-->
                            <!-- Display the history Values-->
                            <%
                                    ArrayList viewHistoryForEuid = compareDuplicateManager.viewHistoryForEuid(eo.getEUID());
                                    EnterpriseObject eoHistory;
                                    for (int eoh = 0; eoh < viewHistoryForEuid.size(); eoh++) {
                                    HashMap historyValuesMap = (HashMap) viewHistoryForEuid.get(eoh);
                                    Set keySetValue = historyValuesMap.keySet();
                                    Iterator keyIter = keySetValue.iterator();
                                    while (keyIter.hasNext()) {
                                      String keyValueOrg = (String) keyIter.next();
                                      int ind = keyValueOrg.indexOf(":");
                                      String keyValue = keyValueOrg.substring(0, ind);
                                      String tranNo = keyValueOrg.substring(ind + 1, keyValueOrg.length());
                                      EnterpriseObject eoHist = (EnterpriseObject) historyValuesMap.get(keyValueOrg);
                    
                           %>                    
                            <td  valign="top">
                                <div id="mainDupHistory<%=countEnt%><%=eoh%>" style="display:none;visibility:hidden;">
                                    <div id="mainEuidContent" class="<%=cssHistory%>">
                                        <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                            <tr>
                                                <td class="menutopHistory"><%=keyValue%></td>
                                            </tr> 
                                            <tr><td valign="top" class="dupfirst_history">
                                                    <%if (countEnt > 0) {%> 
                                                    <font style="text-decoration:none;color:#000000;"><b><%=eoHist.getEUID()%></b></font>
                                                    <%} else {%>
                                                    <font style="text-decoration:none;color:#000000;"><b><%=eoHist.getEUID()%></b></font>
                                                    <%}%>
                                                </td>
                                            </tr>
                                        </table>
                                    </div> 
                                    <div id="mainEuidDataContent<%=countEnt%>"  class="<%=cssHistory%>">
                                                <div id="personEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                            HashMap personValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, personConfigFeilds);
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
                                            HashMap addressValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, addressConfigFeilds);
                                            for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                            HashMap phoneValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, phoneConfigFeilds);
                                            for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                            HashMap aliasValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, aliasConfigFeilds);
                                            for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                                <div id="auxEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                            HashMap auxidValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, auxidConfigFeilds);
                                            for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                            HashMap commentValuesMapSource = compareDuplicateManager.getEOFieldValues(eoHist, objScreenObject, commentConfigFeilds);
                                            for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = "Person." + fieldConfigMap.getFullFieldName();
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
                                    
                                </div> <!--Main div for source-->                        
                            </td>                        
                            <%}%>
                            <%}%>
                            <!--End displaying the History-->
                            
                            <%}%>
                            <%}%>
                            <td valign="top">
                                <div id="previewPane">
                                    <div id="previewEuidDiv" style="visibility:visible;display:block;">
                                        <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td class="menutop1">Preview</td>
                                            </tr>
                                                    
                                        </table>
                                    </div>
                                    <div id="mergePopupDiv" class="alert" style="TOP: 2250px; LEFT: 700px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                                        <table cellpadding="0" cellspacing="0">
                                            <h:form>
                                                <tr><th align="left">Confirmation</th><th align="right"><a href="javascript:void(0)" rel="mergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr><tr>
                                                    <%
                                                        if (session.getAttribute("mergedEOMap") != null) {
                                                            HashMap fieldValuesMergeValue = (HashMap)session.getAttribute("mergedEOMap");
                                                            HashMap mergredHashMap = (HashMap) session.getAttribute("mergedEOMap");
                                                            mergredHashMapVaueExpression = ExpressionFactory.newInstance().createValueExpression(mergredHashMap, mergredHashMap.getClass());
                                                            String mergedEUID = (String) fieldValuesMergeValue.get("EUID");
                                                            String retText = "Keep Main EUID ";
                                                            if(mergedEUID.equalsIgnoreCase(sourceEO.getEUID())) {
                                                               sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                               destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                                               retText = "Keep Main EUID ";
                                                            } else {
                                                               sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
                                                               destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                                               retText = "Keep Duplicate ";
                                                            }
                                                             
                                                    %>
                                                    <td align="center"><b><%=retText%> "<%=fieldValuesMergeValue.get("EUID")%>" ?</b></td>
                                                    <%}%>
                                                </tr>
                                                
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td>
                                                         <h:commandLink styleClass="button" 
                                                                       actionListener="#{PatientDetailsHandler.mergePreviewEnterpriseObject}">
                                                            <f:attribute name="mergedEOValueExpression" value="<%=mergredHashMapVaueExpression%>"/>                  
                                                            <f:attribute name="mainEOVaueExpression" value="<%=sourceEUIDVaueExpression%>"/>
                                                            <f:attribute name="duplicateEOVaueExpression" value="<%=destinationEUIDVaueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                        </h:commandLink>   
                                                       <h:outputLink  
                                                                       onclick="Javascript:togglePDDivs('mergePopupDiv','<%=countEnt*169%>','<%=resultsConfigFeilds.length*28%>')"
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
                                   <div id="previewEuidContentDiv"   class="dynaw169">
                                        <div id="showReadonlyButtonDiv" style="visibility:visible;display:block;">
                                            <table border="0" cellspacing="0" cellpadding="0" class="w169"> 
                                                <% if (session.getAttribute("mergedEOMap") != null) {
                                                        HashMap fieldValuesMerge = (HashMap) session.getAttribute("mergedEOMap");
                                               %>
                                                <tr><td><b><%=fieldValuesMerge.get("EUID")%></b></td></tr>
                                                <%
                                                   String mainDOBMerge;
                                                   String mergeEpathValue;
                                                   for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                   FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                   if (fieldConfigMap.getFullFieldName().startsWith("Person")) {
                                                   mergeEpathValue = fieldConfigMap.getFullFieldName();
                                                   } else {
                                                   mergeEpathValue = "Person." + fieldConfigMap.getFullFieldName();
                                                   }
                                                  
                                                %>  
                                                <tr>
                                                    <td>
                                                        <%if (fieldValuesMerge.get(mergeEpathValue) != null) {%>
                                                        <%if (fieldConfigMap.getValueType() == 6) {
                                                        mainDOBMerge = simpleDateFormatFields.format(fieldValuesMerge.get(mergeEpathValue));
                                                        %>
                                                        <%=mainDOBMerge%>
                                                        <%} else {%>
                                                        <%=fieldValuesMerge.get(mergeEpathValue)%>
                                                        <%}%>
                                                        <%} else {%>
                                                        &nbsp;
                                                        <%}%>
                                                        
                                                    </td>
                                                </tr>
                                                <%}%>
                                               <tr><td>&nbsp;</td></tr>
                                               <%}else {%>
                                               <%
                                                  for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) personConfigFeilds[ifc];
                                               %>   
                                                  <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <%
                                                  for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) addressConfigFeilds[ifc];
                                                %>  
                                               
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                               <%
                                                  for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) phoneConfigFeilds[ifc];
                                                %>  
                                               
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <%
                                                  for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) aliasConfigFeilds[ifc];
                                                %>  
                                               
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <%
                                                  for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) auxidConfigFeilds[ifc];
                                                %>  
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td>&nbsp;</td></tr>
                                                <%
                                                  for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                                  FieldConfig fieldConfig = (FieldConfig) commentConfigFeilds[ifc];
                                               
                                                %>  
                                               
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%>
                                                <tr><td>&nbsp;</td></tr>
                                                
                                                <tr><td>&nbsp;</td></tr>
                                                <%}%> 
                                                <tr><td>&nbsp;</td></tr>
                                                <%if (session.getAttribute("sourceEUIDSessionObj") != null && session.getAttribute("destinationEUIDSessionObj") != null) {
                                                 String sourceEUID = (String) session.getAttribute("sourceEUIDSessionObj");
                                                 String destnEUID = (String) session.getAttribute("destinationEUIDSessionObj");
                                                 ValueExpression mainEOVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEUID, sourceEUID.getClass());
                                                 ValueExpression duplicateEOVaueExpression = ExpressionFactory.newInstance().createValueExpression(destnEUID, destnEUID.getClass());
                                                %>
                                                <h:form>
                                                    <% if (session.getAttribute("mergedEOMap") != null) {
                                                    //  HashMap mergredHashMap = (HashMap) session.getAttribute("mergedEOMap");
                                                      //mergredHashMapVaueExpression = ExpressionFactory.newInstance().createValueExpression(mergredHashMap, mergredHashMap.getClass());
                                                    %>
                                                    <tr><td>&nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                     <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                        <tr>
                                                        <td  valign="top" align="center" >
                                                            <h:outputLink styleClass="activeviewbtn" 
                                                                          onclick="Javascript:togglePDDivs('mergePopupDiv','<%=countEnt*169%>','96*28')"
                                                                          rendered="#{Operations.EO_Merge}" 
                                                                          value="Javascript:void(0)">
                                                                <h:outputText value="#{msgs.source_submenu_merge}"/>                                                            
                                                            </h:outputLink>
                                                        </td> 
                                                    </tr>
                                                    <%} else {%>
                                                    <tr> 
                                                        <td valign="top" align="center" >
                                                            <h:commandLink styleClass="activeviewbtn" rendered="#{Operations.EO_Merge}" actionListener="#{PatientDetailsHandler.getMergedEnterpriseObject}" >
                                                                <f:attribute name="finalSourceEuid" value="<%=duplicateEOVaueExpression %>"/>
                                                                <f:attribute name="finalDestinationEuid" value="<%=mainEOVaueExpression%>"/>
                                                                <h:outputText value="#{msgs.keep_main_euid_but_text}"/>                                                            
                                                            </h:commandLink>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td  valign="top" align="center" >
                                                            <h:commandLink styleClass="activeviewbtn"  rendered="#{Operations.EO_Merge}" actionListener="#{PatientDetailsHandler.getMergedEnterpriseObject}">
                                                                <f:attribute name="finalSourceEuid" value="<%=mainEOVaueExpression%>"/>
                                                                <f:attribute name="finalDestinationEuid" value="<%=duplicateEOVaueExpression%>"/>
                                                                <h:outputText  value="#{msgs.keep_dup_euid_text}"/>                                                            
                                                            </h:commandLink>
                                                        </td> 
                                                    </tr>
                                                    <%}%>
                                                </h:form>
                                                <%} else {%>
                                                <tr> 
                                                    <td valign="top" align="center"><a href="#" class="readonlyviewbtn"><h:outputText value="#{msgs.keep_main_euid_but_text}"/></a></td>
                                                </tr>
                                                <tr>
                                                    <td  valign="top" align="center"> <a href="#" class="readonlyviewbtn"><h:outputText  value="#{msgs.keep_dup_euid_text}"/></a></td> 
                                                </tr>
                                                <%}%>
                                                <tr>
                                                        <td  valign="top" align="center"><a href="#" class="cancelbtn"><h:outputText  value="#{msgs.cancel_but_text}"/></a></td> 
                                                </tr>                   
                                            </table>
                                        </div>
                                    </div>  
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>  
             </div> 
             <div id="resolvepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.resolvepopup_help}"/></div>    
             <div id="mergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.mergepopup_help}"/></div>    
        </body>
    </html>
    </f:view>
    