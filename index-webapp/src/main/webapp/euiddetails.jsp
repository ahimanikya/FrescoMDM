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
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%
 double rand = java.lang.Math.random();
%>
<f:view>
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
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
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title> 
        <body>
        <script>
              function closeTree()    {                            
                    document.getElementById('tree').style.visibility='hidden';
                    document.getElementById('tree').style.display='none';
              }
        </script>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent">
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
                                    <h:commandLink  styleClass="button" action="#{NavigationHandler.toPatientDetails}">  
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
                    <table cellspacing="0" cellpadding="0" border="0">
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
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            if (request.getParameter("euid") != null) {
                reqEnterpriseObject = compareDuplicateManager.getEnterpriseObject(request.getParameter("euid"));
                eoArrayList.add(reqEnterpriseObject);
                session.setAttribute("enterpriseArrayList",eoArrayList);
            } else {
                eoArrayList = (ArrayList) session.getAttribute("enterpriseArrayList");
            }
            int countMain = 0;
            int totalMainDuplicates = 0;

            //System.out.println("==eoArrayList==="+eoArrayList);
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            String epathValue;
            if (eoArrayList != null && eoArrayList.size() > 0) {
                %>  
                          <%
                                Object[] eoArrayListObjects = eoArrayList.toArray();
                                String dupHeading = "EUID";
                                String cssClass = "dynaprevieww169";
                                String cssMain ="maineuidpreview";
                                String cssHistory = "differentHistoryColour";
                                String cssSources = "differentSourceColour";
                                String cssDiffPerson = "differentPersonColour";
                                String cssMerge = "differentMergeColour";
                                String menuClass = "menutop";
                                String dupfirstBlue = "dupfirstBlue";
                                for (int countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                    if (countEnt > 0) {
                                        cssMain ="maineuidpreview";
                                        cssClass = "dynaw169";
                                        menuClass = "menutop";
                                        dupfirstBlue = "dupfirst";
                                    }
                                    EnterpriseObject eo = (EnterpriseObject) eoArrayListObjects[countEnt];
                                    Collection items = eo.getSystemObjects();

                                    ArrayList resultArrayListMain = new ArrayList();
                                    ArrayList resultArrayListCompare = new ArrayList();

                                    so = (SystemObject) (eo.getSystemObjects().toArray()[0]);
                                    Collection fieldvalues;
           %>
            
                            <td  valign="top">
                                <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                                 <div id="assEuidDataContent<%=countEnt%>">
                                            <table>
                                               <tr>
                                                   <!-- Display the field Names first column-->
                                                   <% if(countEnt==0){%>
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
                                                    <%}%>
                                                    <!-- Display the field Values-->
                                                  <td valign="top">
                                           <%if ("active".equalsIgnoreCase(eo.getStatus())) {%>           
                                           <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>"> 
                                           <%}else{%>    
                                           <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssDiffPerson%>">
                                           <%}%>    
                                                <div id="mainEuidContent" class="w169">
                                                  <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                      <tr>
                                                          <td class="<%=menuClass%>">
                                                              <%if (eoArrayList.size() > 1) {%>
                                                              <%=dupHeading%> <%=countEnt + 1%>
                                                              <%} else {%>
                                                              <%=dupHeading%>
                                                              <%}%>
                                                          </td>
                                                      </tr> 
                                                     <h:form>
                                                          <tr>
                                                              <td valign="top" class="dupfirst">
                                                                  <%if (countEnt > 0) {
                                        EnterpriseObject sourceEO = (EnterpriseObject) eoArrayListObjects[0];
                                        EnterpriseObject destinationEO = (EnterpriseObject) eoArrayListObjects[countEnt];
                                        ValueExpression sourceEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(sourceEO.getEUID(), sourceEO.getEUID().getClass());
                                        ValueExpression destinationEUIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(destinationEO.getEUID(), destinationEO.getEUID().getClass());
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
                                                <div id="assEuidDataContent<%=countEnt%>" class="<%=cssMain%>">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                            
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
                                                                <h:form>
                                                                    <tr>
                                                                        <td valign="top">
                                                                            <%if (fieldValuesMapEO.get(epathValue) != null ) {%>
                                                                            
                                                                            <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue)  != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
        //System.out.println("Condition success!");
        fnameExpression = ExpressionFactory.newInstance().createValueExpression(epathValue, epathValue.getClass());
        fvalueVaueExpression = ExpressionFactory.newInstance().createValueExpression(fieldValuesMapEO.get(epathValue), fieldValuesMapEO.get(epathValue).getClass());

                                                                            %>
                                                                            <h:commandLink style="text-decoration:none;" actionListener="#{PatientDetailsHandler.setPreviewEnterpriseObjectValues}">
                                                                                <f:attribute name="fnameExpression" value="<%=fnameExpression%>"/>
                                                                                <f:attribute name="fvalueVaueExpression" value="<%=fvalueVaueExpression%>"/>
                                                                                
                                                                                <font class="highlight">
                                                                                    <%if (fieldConfigMap.getValueType() == 6) {
        mainDOB = simpleDateFormatFields.format(fieldValuesMapEO.get(epathValue));
                                                                                    %>
                                                                                    <b><%=mainDOB%></b>
                                                                                    <%} else {%>
                                                                                    <b><%=fieldValuesMapEO.get(epathValue)%></b>
                                                                                    <%}%>
                                                                                </font>
                                                                            </h:commandLink>
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

                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                        <tr>
                                                        <%
                                    HashMap phonefieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, phoneConfigFeilds);

                                    for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                        <tr><td>&nbsp;</td></tr>    
                                                        <tr>
                                                        <%
                                    HashMap aliasfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, aliasConfigFeilds);

                                    for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                        <tr>
                                                        <%
                                    HashMap auxidfieldValuesMapEO = compareDuplicateManager.getEOFieldValues(eo, objScreenObject, auxidConfigFeilds);

                                    for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                        FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];

                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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

                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                             
                                                 <%
                                   // EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(eo.getEUID());
                                   // Collection itemsSource = eoSource.getSystemObjects();
                                    ValueExpression eoValueExpression = ExpressionFactory.newInstance().createValueExpression(eo,eo.getClass());
                                    

                                        %>
                                        <!--Displaying view sources and view history-->
                                                <div id="dynamicMainEuidButtonContent<%=countEnt%>"  style="visibility:visible;display:block;" >
                                           <h:form>    
                                            <table border="0" cellspacing="0" cellpadding="0">
                                                        <%if ("active".equalsIgnoreCase(eo.getStatus())) {%>
                                                <tr> 
                                                    <td valign="top" width="125px">
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toEditMainEuid}"
                                                                        actionListener="#{EditMainEuidHandler.setEditEOFields}">
                                                            <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                            <span><h:outputText value="Edit EUID" /> </span>
                                                        </h:commandLink>  
                                                        <h:commandLink  styleClass="button" 
                                                                        actionListener="#{PatientDetailsHandler.deactivateEO}">
                                                            <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                        </h:commandLink> 
                                                    </td>
                                                </tr>
                                                        <%}%>            
                                                        <%if ("inactive".equalsIgnoreCase(eo.getStatus())) {%>
                                                        
                                                    <tr>
                                                         <td valign="top" width="125px">
                                                        <h:commandLink  styleClass="button" 
                                                                        actionListener="#{PatientDetailsHandler.activateEO}">
                                                            <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                        </h:commandLink>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <h:outputText style="color:red" value="This record has been deactivated "/>
                                                         </td>
                                                      </tr>
                                                        <%}%>            
                                                <tr> 
                                                    <td valign="top" colspan="2">
                                                        <%
                                                           if(session.getAttribute("eoHistory"+eo.getEUID()) !=null){
                                                        %>
                                                        
                                                        <h:commandLink  styleClass="diffviewbtn" 
                                                                        actionListener="#{PatientDetailsHandler.removeHistory}">
                                                              <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                             <h:outputText value="#{msgs.view_history_left_text}"/>
                                                        </h:commandLink>  
                                                      <%} else  {%> 
                                                      <h:commandLink  styleClass="viewbtn" 
                                                                      actionListener="#{PatientDetailsHandler.viewHistory}">
                                                              <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                          <h:outputText value="#{msgs.view_history_text}"/>
                                                      </h:commandLink>  
                                                      <%}%> 
                                                </tr>
                                                <tr>
                                                    <td valign="top" colspan="2">
                                                         <%
                                                           if(request.getAttribute("itemsSource") !=null){
                                                        %>
                                                        <h:commandLink  styleClass="diffviewbtn" 
                                                                      actionListener="#{PatientDetailsHandler.removeSource}">
                                                           <h:outputText value="#{msgs.view_sources_left_text}"/>
                                                        </h:commandLink>  
                                                        <%} else  {%> 
                                                        <h:commandLink  styleClass="viewbtn" 
                                                                        actionListener="#{PatientDetailsHandler.viewSource}">
                                                         <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>                   
                                                             <h:outputText value="#{msgs.view_sources_text}"/>
                                                        </h:commandLink> 
                                                      
                                                        <%}%>
                                                   </td> 
                                                </tr>
                                                
                                                <tr>
                                                    <td valign="top">
                                                        <a href="javascript:void(0);" onclick="javascript:ajaxURL('/SunEdm/viewmergetree.jsf?euid=<%=eo.getEUID()%>&rand=<%=rand%>','tree',event)" class="viewbtn">
                                                        <h:outputText value="#{msgs.View_MergeTree_but_text}"/></a>
                                                    </td>
                                                </tr>
                                                
                                            </table>
                                            </h:form> 
                                        </div>  
                                         </div>
                                    </div>
                                     <!--END Displaying view sources and view history Buttons-->
                                      </td>
                                     <!--Start displaying the sources-->
                                     <!-- Display the Sources Values-->
                      <%  if(request.getAttribute("itemsSource") !=null){
                             ArrayList viewSourceForEuid = (ArrayList)request.getAttribute("itemsSource");    
                             EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(eo.getEUID());
                             Collection itemsSource = eoSource.getSystemObjects();
                                    //System.out.println("itemsSource" + itemsSource);
                                    Iterator itSource = itemsSource.iterator();
                                    SystemObject soSource = null;
                                    ArrayList resultArrayListSource = new ArrayList();
                                    Object[] itemsSourceObj = itemsSource.toArray();
                                    //while (itSource.hasNext()) {
                                    //soSource = (SystemObject) itSource.next();
                                    for (int soc = 0; soc < itemsSourceObj.length; soc++) {
                                        soSource = (SystemObject) itemsSourceObj[soc];
                                        //System.out.println("System Object" + soSource.getSystemCode());
                                        Collection fieldvaluesSource;
                    %>
                             <td  valign="top">
                                <div id="mainDupSources<%=countEnt%><%=soc%>" >
                                    <div id="mainEuidContent" class="<%=cssSources%>">
                                        <table border="0" cellspacing="0" cellpadding="0">
                                            
                                            <tr>
                                                <td class="menutopSource"><%=soSource.getSystemCode()%></td>
                                            </tr> 
                                            
                                            <tr>
                                                <td valign="top" class="dupfirst_source">
                                                    <font style="text-decoration:none;color:#000000;"><b><%=soSource.getLID()%></b></font>
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
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                epathValue = fieldConfigMap.getFullFieldName();
                                            } else {
                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                    <!--End displaying the sources-->
                                    <!--Start displaying the history-->
                                    <!-- Display the history Values-->
                           <%if(session.getAttribute("eoHistory"+eo.getEUID()) !=null){
                           //compareDuplicateManager.viewHistoryForEuid(eo.getEUID());
                                    ArrayList viewHistoryForEuid = (ArrayList) session.getAttribute("eoHistory"+eo.getEUID());
                                    
                                    EnterpriseObject eoHistory;
                                    for (int eoh = 0; eoh < viewHistoryForEuid.size(); eoh++) {
                                        HashMap historyValuesMap = (HashMap) viewHistoryForEuid.get(eoh);
                                        Set keySetValue = historyValuesMap.keySet();
                                        Iterator keyIter = keySetValue.iterator();
                                        while (keyIter.hasNext()) {
                                            String keyValueOrg = (String) keyIter.next();
                                            //System.out.println("=====> : " + keyValueOrg);
                                            int ind = keyValueOrg.indexOf(":");
                                            //System.out.println("=====> : " + keyValueOrg.indexOf(":"));
                                            String keyValue = keyValueOrg.substring(0, ind);
                                            String tranNo = keyValueOrg.substring(ind+1, keyValueOrg.length()); 
                                            //System.out.println("tranNo =====> : " + tranNo);
                                            
                                            EnterpriseObject eoHist = (EnterpriseObject) historyValuesMap.get(keyValueOrg);
                             %>                    
                           <td  valign="top">
                               <%
                                    EnterpriseObject eoSourceunmerge = compareDuplicateManager.getEnterpriseObject(eo.getEUID());
                                    Collection itemsSourceunmerge = eoSourceunmerge.getSystemObjects();
                                    ValueExpression eoValueExpressionunmerge = ExpressionFactory.newInstance().createValueExpression(eo, eo.getClass());
                              %>
                               

                              <div id="unmergePopupDiv" class="alert" style="TOP: 2250px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                                  <table cellpadding="0" cellspacing="0">
                                      <h:form>
                                          <tr><th align="left">Confirmation</th><th align="right"><a href="javascript:void(0)" rel="unmergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                          <tr><td colspan="2"> &nbsp;</td></tr>
                                          <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_popup_content_text}" /></td></tr>
                                          <tr><td colspan="2"> &nbsp;</td></tr>
                                          <tr><td colspan="2"> &nbsp;</td></tr>
                                          <tr><td>
                                                  <h:commandLink styleClass="button" 
                                                                 actionListener="#{PatientDetailsHandler.unmergeEnterpriseObject}">
                                                      <f:attribute name="eoValueExpressionunmerge" value="<%=eoValueExpressionunmerge%>"/>                   
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
                              
                                   
                               <div id="mainDupHistory<%=countEnt%><%=eoh%>">
                                    <div id="mainEuidContent" class="<%=cssHistory%>">
                                        <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                            <tr>
                                                <td class="menutopHistory"><%=keyValue%></td>
                                            </tr> 
                                            <tr><td valign="top" class="dupfirst_history">
                                                    <font style="text-decoration:none;color:#000000;"><b><%=eoHist.getEUID()%></b></font>
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
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                     <table>
                                         <%
                                             if(keyValueOrg.startsWith("euidMerge")){
                                             ValueExpression tranNoValueExpressionviewmerge = ExpressionFactory.newInstance().createValueExpression(tranNo, tranNo.getClass());
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
                                                <tr>
                                                    <td valign="top" colspan="2">
                                                        <h:form>
                                                        <h:commandLink styleClass="activeviewbtn" 
                                                                       actionListener="#{PatientDetailsHandler.viewMergedRecords}">
                                                        <f:attribute name="tranNoValueExpressionviewmerge" value="<%=tranNoValueExpressionviewmerge%>"/>                   
                                                            <h:outputText  value="#{msgs.View_Merge_Records_but_text}"/>                                                            
                                                        </h:commandLink>
                                                       </h:form>
                                                    </td> 
                                                </tr>
                                            <% }else{%>
                                                <tr>
                                                    <td>&nbsp;</td> 
                                                </tr>
                                             <%}%>
                                               </table>
                                         
                                </div>
                                </div> <!--Main div for source-->                        
                            </td>     
                                     <%}%>
                                                    <%}%>
                                                    <%}%>
                                    <!--End displaying the History-->
                            <%if(request.getAttribute("mergeEOList") !=null){
                                ArrayList  mergeEOList =  (ArrayList) request.getAttribute("mergeEOList");
                                
                                for(int eomerge=0;eomerge< mergeEOList.size();eomerge++ ){
                                EnterpriseObject mergeEnterpriseObject = (EnterpriseObject) mergeEOList.get(eomerge);    
                              
                                %>
                              <td  valign="top">
                                <div id="viewMergeHistory<%=countEnt%><%=mergeEnterpriseObject%>">
                                    <div id="mainEuidContent" class="<%=cssMerge%>">
                                        <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                           <tr>
                                                <td class="menutopMerge">Merged Record</td>
                                            </tr> 
                                            
                                            <tr>
                                                  <td valign="top" class="dupfirst_history">
                                                    <font style="text-decoration:none;color:#000000;"><b><%=mergeEnterpriseObject.getEUID()%></b></font>
                                                </td>
                                            </tr>
                                        </table>
                                    </div> 
                                    <div id="mainEuidDataContent<%=countEnt%>"  class="<%=cssMerge%>">
                                                <div id="personEuidContent" class="w169">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                        <%
                                           HashMap personValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, personConfigFeilds);
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
                                           HashMap  addressValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, addressConfigFeilds);
                                            for (int ifc = 0; ifc < addressConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) addressConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                          HashMap   phoneValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, phoneConfigFeilds);
                                            for (int ifc = 0; ifc < phoneConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) phoneConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                           HashMap  aliasValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, aliasConfigFeilds);
                                            for (int ifc = 0; ifc < aliasConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) aliasConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                          HashMap    auxidValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, auxidConfigFeilds);
                                            for (int ifc = 0; ifc < auxidConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) auxidConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                            HashMap  commentValuesMapSource = compareDuplicateManager.getEOFieldValues(mergeEnterpriseObject, objScreenObject, commentConfigFeilds);
                                            for (int ifc = 0; ifc < commentConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) commentConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
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
                                                    <%}%>
                                                    <%}%>
                                                    
                                                </tr>
                                              
                                            </table>
                                   </div>
                                </div> 
                             </td>
                            <td valign="top"><div id="previewPane"></div></td>
                         </tr>
                    </table>
                </div>                                       
            </div>    
         <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>     
         <div id="tree" style="PADDING-LEFT: 5px; LEFT: 25px; VISIBILITY: hidden; WIDTH: 200px; PADDING-TOP: 5px;  POSITION: absolute;  OVERFLOW: auto; TOP: 260px; HEIGHT: 150px; BACKGROUND-COLOR: #c4c8e1; BORDER-RIGHT:  #000099 thin solid; BORDER-TOP: #000099 thin solid; BORDER-LEFT: #000099 thin solid; BORDER-BOTTOM:  #000099 thin solid"></div> 
        </body>
    </html>
    </f:view>
    