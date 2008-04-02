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
                <div id="mainDupSource" class="compareResults">
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

            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            if (request.getParameter("euid") != null) {
                eoArrayList = patientDetailsHandler.buildEuids(request.getParameter("euid"));
                request.setAttribute("comapreEuidsArrayList",eoArrayList);
            } 
            
            if(request.getAttribute("comapreEuidsArrayList") != null) {
                session.removeAttribute("comapreEuidsArrayList");        
                eoArrayList = (ArrayList) request.getAttribute("comapreEuidsArrayList");
            }
            if (session.getAttribute("comapreEuidsArrayList") != null) {
                eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
                session.removeAttribute("comapreEuidsArrayList");        
                request.setAttribute("comapreEuidsArrayList",eoArrayList);
            } 
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
            if (eoArrayList != null && eoArrayList.size() > 0) {
                request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));
                                            %>  
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
                                            String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                            String mainEUID = new String();
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                 HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                 HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                 String eoStatus = (String) eoHashMapValues.get("EO_STATUS");
                                                if ("inactive".equalsIgnoreCase(eoStatus)) {
                                                    styleClass="deactivate";
                                                }
                                            
    
                                              //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);

                                                if (eoArrayListObjects.length > 1) {
                                                    dupHeading = "<b>EUID " + new Integer(countEnt + 1).toString() +  "</b>";
                                                } else if (eoArrayListObjects.length == 1) {
                                                    dupHeading = "<b>EUID</b>";
                                                }
                                                mainEUID = (String) personfieldValuesMapEO.get("EUID");

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
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
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
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
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
                                                                        <% if ("inactive".equalsIgnoreCase(eoStatus)) {%>
                                                                           <%=personfieldValuesMapEO.get("EUID")%>
                                                                        <%} else {%>
                                                                           <span class="dupbtn" >
                                                                             <%=personfieldValuesMapEO.get("EUID")%>
                                                                           </span>
                                                                        <%} %>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
                                                
                                                        
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
                                                    <div style="width:170px;overflow:hidden">
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="source">
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
                                                    int ind = key.indexOf(":");
                                                    tranNo = key.substring(ind+1, key.length()); 
                                                    HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                    HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                  <div style="width:170px;overflow:hidden">
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
                                                <%
                                                  if (request.getAttribute("mergeEOList") != null) {
                                                    ArrayList mergeEOList = (ArrayList) request.getAttribute("mergeEOList");
                                                    for (int eomerge = 0; eomerge < mergeEOList.size(); eomerge++) {
                                                        HashMap objectMergeMap =(HashMap) mergeEOList.get(eomerge);
                                                        HashMap mergeeoValuesMap = (HashMap) objectMergeMap.get("ENTERPRISE_OBJECT");
                                                %>
                                                <td  valign="top">
                                  <div id="viewMergeHistory<%=countEnt%>">
                                  <div style="width:170px;overflow:hidden">
                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="merge">
                                        <table border="0" cellspacing="0" cellpadding="0">
                                           <tr>
                                                <td class="menutopMerge">Merged Record</td>
                                            </tr> 
                                            
                                            <tr>
                                            <td valign="top">
                                                    <font style="text-decoration:none;color:#000000;"><b><%=mergeeoValuesMap.get("EUID")%></b></font>
                                                </td>
                                            </tr>
                                        </table>
                                    </div> 
                                    <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="merge">
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
                                                                                <%if (mergeeoValuesMap.get(epathValue) != null) {%>
                                                                                
                                                                                <%=mergeeoValuesMap.get(epathValue)%>
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
                                                                    ArrayList  minorObjectMapList =  (ArrayList) objectMergeMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
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
                                </div> <!--Main div for source-->  
                                </div>
                            </td> 
                                            <%}%>
                                            <%}%> 
                                      <!--END displaying the History-->
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
                                <table width="100%" cellpadding="0" cellspacing="0">
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
                                        <% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <% }%>
									   
                                        <td>
                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0" border="1">

                                                    <h:form>
                                                        
                                                        <%if ("active".equalsIgnoreCase(eoStatus)) {%>
                                                <tr> 
                                                    <td valign="top" width="125px">
                                                        <h:commandLink  styleClass="button" rendered="#{Operations.EO_Edit}"
                                                                        action="#{NavigationHandler.toEditMainEuid}"
                                                                        actionListener="#{EditMainEuidHandler.setEditEOFields}">
                                                             <f:attribute name="euidValueExpression" value="<%=euidValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.edit_euid_button_text}" /> </span>
                                                        </h:commandLink>  
                                                        <h:commandLink  styleClass="button" rendered="#{Operations.EO_Deactivate}"
                                                                        actionListener="#{PatientDetailsHandler.deactivateEO}">
                                                             <f:attribute name="euidValueExpression" value="<%=euidValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                        </h:commandLink> 
                                                    </td>
                                                </tr>
                                                        <%}%>            
                                                        <%if ("inactive".equalsIgnoreCase(eoStatus)) {%>
                                                        
                                                    <tr>
                                                         <td valign="top" width="125px">
                                                        <h:commandLink  styleClass="button" rendered="#{Operations.EO_Activate}"
                                                                        actionListener="#{PatientDetailsHandler.activateEO}">
                                                             <f:attribute name="euidValueExpression" value="<%=euidValueExpression%>"/>
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
                                                    </h:form>
                                                <tr> 
                                                      <td valign="top">
                                                          <a class="viewbtn" href="javascript:showViewSources('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','0')" >  
                                                              <h:outputText value="#{msgs.view_history_text}"/>
                                                          </a>
                                                      </td>    
                                                  </tr>
                                                  <tr> 
                                                      <td valign="top">
                                                          <a href="javascript:showViewHistory('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','0')" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                      </td>                                              
                                                  </tr>
									<tr>
                                        <%if(eoHistory.size() > 0) {
												   %>
										
                                        <td valign="top">
                                                     <%
											            String mergekey = new String();
                                                        for(int i=0;i<eoHistory.size();i++) {
                                                          HashMap objectHist = (HashMap) eoHistory.get(i);
                                                          mergekey = (String) objectHist.keySet().toArray()[0];
										               }
                                                        if (mergekey.startsWith("euidMerge")) {                                                  
                                                        ValueExpression tranNoValueExpressionviewmerge = ExpressionFactory.newInstance().createValueExpression(tranNo, tranNo.getClass());
                                                        ValueExpression eoArrayListVE = ExpressionFactory.newInstance().createValueExpression(eoArrayList, eoArrayList.getClass());
                                                      %>  
                                                      <div id="unmerge">    
                                                      <table cellpadding="0" cellspacing="0" border="0">
                                                          <tr>
                                                            <td valign="top" colspan="2">
                                                                <h:outputLink styleClass="viewbtn" rendered="#{Operations.EO_Unmerge}"
                                                                              onclick="Javascript:showConfirm('unmergePopupDiv',event)" 
                                                                              value="Javascript:void(0)">
                                                                    <h:outputText  value="#{msgs.Unmerge_but_text}"/>                                                          
                                                                </h:outputLink>
                                                            </td> 
                                                        </tr>
                                                        <tr>
                                                            <td valign="top" colspan="2">
                                                                <h:form>
                                                                    <h:commandLink styleClass="activeviewbtn" rendered="#{Operations.EO_Merge}"
                                                                                   actionListener="#{PatientDetailsHandler.viewMergedRecords}">
                                                                        <f:attribute name="eoArrayList" value="<%=eoArrayListVE%>"/>                   
                                                                        <f:attribute name="tranNoValueExpressionviewmerge" value="<%=tranNoValueExpressionviewmerge%>"/>                   
                                                                        <h:outputText  value="#{msgs.View_Merge_Records_but_text}"/>                                                            
                                                                    </h:commandLink>
                                                                </h:form>
                                                            </td> 
                                                        </tr>
                                                    <tr>
                                                      <td valign="top">
                                                         <%
                                                          String URI = request.getRequestURI();
                                                          URI = URI.substring(1, URI.lastIndexOf("/"));
                                                          %>
                                                         <a href="javascript:void(0);" class="viewbtn" onclick="javascript:ajaxURL('/<%=URI%>/viewmergetree.jsf?euid=<%=personfieldValuesMapEO.get("EUID")%>&rand=<%=rand%>','tree',event)">
                                                          <h:outputText value="#{msgs.View_MergeTree_but_text}"/>
                                                         </a>
                                                      </td>
                                                  </tr>
                                                        
                                                        </table>
                                                      </div>
                                                   <%}%>
                                          </td>  
										<%
                                        unMergeEuidVE = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());
                                         %> 

                                          <%}%> 

									</tr>

  											   </div>
                                               </table>                                               
                                        </td>   
										<%}%>
                                    </tr>
                                </table>
                                </div>
                            </td>
                        </tr>
                 <%}%>
                    </table>
                </div>
                
            </div>    
                <div id="unmergePopupDiv" class="alert" style="TOP: 2250px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                                                        <table cellpadding="0" cellspacing="0">
                                                            <h:form>
                                          <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="unmergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_popup_content_text}" /></td></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td>
                                                                        <h:commandLink styleClass="button" 
                                                                                       actionListener="#{PatientDetailsHandler.unmergeEnterpriseObject}">
                                                                            <f:attribute name="unMergeEuidVE" value="<%=unMergeEuidVE%>"/>                   
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
                       
         <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>     
         <div id="tree" style="PADDING-LEFT: 5px; LEFT: 25px; VISIBILITY: hidden; WIDTH: 200px; PADDING-TOP: 5px;  POSITION: absolute;  OVERFLOW: auto; TOP: 260px; HEIGHT: 150px; BACKGROUND-COLOR: #c4c8e1; BORDER-RIGHT:  #000099 thin solid; BORDER-TOP: #000099 thin solid; BORDER-LEFT: #000099 thin solid; BORDER-BOTTOM:  #000099 thin solid"></div> 
        </body>
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
    
