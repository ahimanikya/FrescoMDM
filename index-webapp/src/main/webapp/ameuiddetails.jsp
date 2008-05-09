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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
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
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title>
        <body>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent">
                <div id="mainDupSource" class="compareResults">
                    <table border="0" width="100%">
                        <tr>
                            <td width="100%" align="center" class="euidHeadMessage">
                                <b> <h:outputText value="Assumed Matches Details"/></b>                                                      
                            </td>
                        </tr>                
                   </table>
                    <table cellspacing="0" cellpadding="0" border="0">
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

            EPath ePath = null;
            PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();
            Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            if (request.getParameter("AMID") != null) {
                request.setAttribute("comapreEuidsArrayList",assumeMatchHandler.getAssumedEOList(request.getParameter("AMID")));
            }
            if (request.getAttribute("comapreEuidsArrayList") != null) {
                eoArrayList = (ArrayList) request.getAttribute("comapreEuidsArrayList");
            }
            String amPreviewId = (String)(request.getParameter("AMID")==null?request.getAttribute("AMID"):request.getParameter("AMID"));   
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
            
            if (eoArrayList != null) {
                request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));
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
                                          <%

                                            %>
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=cssMain%>">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Details</b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" style="visibility:visible;display:block;">
                                                                
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

                                                                   %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Details</b></td></tr>
                                                                    <tr>
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
                                            
                                            <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                        <a class="dupbtn" href="javascript:void(0)">
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
                                                
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
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
                                                                                <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=personfieldValuesMapEO.get(epathValue)%>')" >
                                                                                    <font class="highlight">
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                    </font>
                                                                                </a>  
                                                                                <%} else {
                                                                                %>
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
                                                                    ArrayList  minorObjectMapList =  (ArrayList) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                    //for(int ar =0;ar<minorObjectMapList.size();ar++) {
                                                                     if(minorObjectMapList.size() >0) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(0);
                                                                     }  
                                                                    //}   
                                                                      FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    

                                                                   %>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <tr>
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                     epathValue = fieldConfigMap.getFullFieldName();
                                                                          if(minorObjectMapList.size() >0) {
                                        if (countEnt > 0) {
                                            resultArrayMapCompare.put(epathValue, minorObjectHashMap.get(epathValue));
                                        } else {
                                            resultArrayMapMain.put(epathValue, minorObjectHashMap.get(epathValue));
                                        }
                                        }
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }
                                                                    }
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
                                            %>
                                            <td  valign="top">
                                                <div id="mainDupSources<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                    <div style="width:170px;overflow:hidden;">
                                                    <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source" >
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

                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="source">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
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
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                     if(minorObjectMapList.size() >0) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(0);
                                                                     }  
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
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }
                                                                    }
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

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="history">
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
                                                                    ArrayList  minorObjectMapList =  (ArrayList) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                     if(minorObjectMapList.size() >0) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(0);
                                                                     }  
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
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }
                                                                    }
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
                                             <% if (countEnt + 1 == eoArrayListObjects.length) {
                                                    if( request.getAttribute("previewAMEO") != null) {
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
                                                                    HashMap eoAssumeMatchPreviewMap = new HashMap();
                                                                    HashMap mergePersonfieldValuesMapEO = new HashMap();
                                                                    if( request.getAttribute("previewAMEO") != null  ) {
                                                                        request.setAttribute("previewAMEO",request.getAttribute("previewAMEO"));
                                                                        eoAssumeMatchPreviewMap =(HashMap) request.getAttribute("previewAMEO");
                                                                        mergePersonfieldValuesMapEO = (HashMap) eoAssumeMatchPreviewMap.get("ENTERPRISE_OBJECT");
                                                                    }    
%>
                                                                    <tr>
                                                                        <td>
                                                                          <%
                                                                            if( request.getAttribute("previewAMEO") != null  ) {
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
                                                                            if( request.getAttribute("previewAMEO") != null  ) {
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
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());

                                                                   %>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <tr>
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                       if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                         epathValue = fieldConfigMap.getFullFieldName();
                                                                       } else {
                                                                         epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                                       }
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%
                                                                            if( request.getAttribute("previewAMEO") != null  ) {
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
                        <tr>
                            <td>
                                <div id="actionmainEuidContent" class="actionbuton">
                                    <table cellpadding="0" cellspacing="0">
                                        <% for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                        %>
                                        <% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <% }%>
                                        <!--Displaying view sources and view history-->
                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0" border="1">
                                                        <%
                                                           if(countEnt == 0){
                                                        %>
                                     
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
                                                        <%} else {%> 
                                                        <tr> 
                                                            <td valign="top">
                                                        <%
                                                          
                                                          ValueExpression amPreviewIdValueExpression = ExpressionFactory.newInstance().createValueExpression(amPreviewId, amPreviewId.getClass());
                                                          ValueExpression eoArrayListValueExpression = ExpressionFactory.newInstance().createValueExpression(eoArrayList, eoArrayList.getClass());
                                                        %>
                                                            <h:form  id="previewForm">
                                                                <h:commandLink styleClass="button" rendered="#{Operations.assumedMatch_Undo}" actionListener="#{AssumeMatchHandler.previewUndoAssumedMatch}">
                                                                        <f:attribute name="previewamIdValueExpression" value="<%=amPreviewIdValueExpression%>"/>
                                                                        <f:attribute name="eoArrayList" value="<%=eoArrayListValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.undo_match_button_text}" /></span>
                                                                </h:commandLink>
                                                             </h:form>
                                                                
                                                            </td>                                              
                                                        </tr>
                                                       <%}%>  
                                                </table>
                                            </div> 
                                        </td>
                                        <% if (countEnt + 1 == eoArrayListObjects.length) {%>
                                         <td valign="top">
                                                        <%
                                                         if( request.getAttribute("previewAMEO") != null  ) {
                                                         ValueExpression amPreviewIdVaueExpression = ExpressionFactory.newInstance().createValueExpression(amPreviewId, amPreviewId.getClass());
                                                        %>
                                                        <table border="0" cellspacing="0" cellpadding="0">
                                                        <tr> 
                                                            <td valign="top">
                                                                <h:form>
                                                                    <h:commandLink styleClass="button" rendered="#{Operations.assumedMatch_Undo}"
                                                                                   actionListener="#{AssumeMatchHandler.undoMatch}">
                                                                        <f:attribute name="previewamIdValueExpression" value="<%=amPreviewIdVaueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                    </h:commandLink>
                                                                </h:form>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td  valign="top">
                                                                <h:form  id="previewCancelForm">
                                                                <h:commandLink styleClass="button" 
                                                                               action="#{NavigationHandler.toAssumedMatches}">
                                                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                </h:commandLink>
                                                              </h:form>
                                                            </td> 
                                                        </tr>  
                                                        </table>
                                                        <%}%>   
                                                        </td>
                                        <%}%> 
                                        
                                        <%}%>
                                       
                                    </table>
                                </div>
                            </td>
                        </tr>
                   <%}%>
                        
                    </table>
                </div>                                       
            </div>    
                
       </body>
    </html>
</f:view>
    
