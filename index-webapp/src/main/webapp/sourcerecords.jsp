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
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>


<f:view>
        <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>
        
        <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
        <!-- Additional source files go here -->
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
                
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <script type="text/javascript" src="scripts/balloontip.js"></script>
        <script type="text/javascript" src="scripts/validation.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo/yahoo.js"></script>
        <script type="text/javascript" src="./scripts/yui/event/event.js"></script>
        <script type="text/javascript" src="./scripts/yui/dom/dom.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
        <!--there is no custom header content for this example-->
        
    </head>
    <%@include file="./templates/header.jsp"%>
    <body class="yui-skin-sam">
        <div id="mainContent" style="overflow:hidden;"> 
        <div id="sourcerecords">
            <table border="0" cellspacing="0" cellpadding="0" width="90%">
                <% Operations operations=new Operations();%>
                <tr>
                    <td>
                        <div id="demo" class="yui-navset">
                            <ul class="yui-nav">
                                <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li class="selected"><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li class="selected"><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%} else {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a href="#addTab"><em>${msgs.source_submenu_add}</em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a href="#mergeTab"><em>${msgs.source_submenu_merge}</em></a></li>
                                <%}%>
                                <%}%>  
                            </ul>  
                            <%
                                        ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                                        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
                                        ArrayList searchResultsScreenConfigArray = (ArrayList) session.getAttribute("viewEditResultsConfigArray");
                                        ArrayList systemObjectsMapList = (ArrayList) session.getAttribute("systemObjectsMapList");
                                        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                                        ValueExpression LIDVaueExpression = null;
                                        ValueExpression sourceSystemVaueExpression = null;
                                        ConfigManager.init();
                                        ObjectNodeConfig objectNodeConfig = ConfigManager.getInstance().getObjectNodeConfig("Person");
                                        FieldConfig[] allFeildConfigs = objectNodeConfig.getFieldConfigs();
                                        EPathArrayList ePathArrayList = new EPathArrayList();

                                        //System.out.println("allFeildConfigs" + allFeildConfigs.length);
                                        String mainDOB;
                                        SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");

                                        HashMap systemObjectMap = (HashMap) session.getAttribute("systemObjectMap");
                                        String keyFunction = (String) session.getAttribute("keyFunction");
                                        SourceHandler sourceHandler = new SourceHandler();
                                        Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
                                        Object[] addressConfigFeilds = sourceHandler.getAddressFieldConfigs().toArray();
                                        Object[] aliasConfigFeilds = sourceHandler.getAliasFieldConfigs().toArray();
                                        Object[] phoneConfigFeilds = sourceHandler.getPhoneFieldConfigs().toArray();
                                        SourceEditHandler sourceEditHandler = (SourceEditHandler)session.getAttribute("SourceEditHandler");
                                        System.out.println("sourceEditHandler "  + sourceEditHandler );
                                        SourceAddHandler  sourceAddHandler   = new SourceAddHandler();
                                        int addressSize;
                                        int phoneSize;
                                        int aliasSize;
                                        int commentSize;
                                        int dateCount=99999;
                                                        HashMap resultArrayMapCompare = new HashMap();
                                                        HashMap resultArrayMapMain = new HashMap();

                                                        ValueExpression fnameExpression;
                                                        ValueExpression fvalueVaueExpression;
                                    %>
                            <div class="yui-content">
                              <% if(operations.isSO_SearchView()){%> 
                                <div id=viewEditTab">
                            
                                    <%if (singleSystemObjectLID != null) {%>
                                    <%if ("viewSO".equalsIgnoreCase(keyFunction)) {%>
                                    <h:form>
                                        <div id="sourceViewBasicSearch">                                            
                                            <table border="0" width="60%">
                                                    <tr>
                                                        <td>
                                                            <h:commandLink  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                            </h:commandLink>                                                                                                 
                                                        </td>
                                                    </tr>  
                                                    <tr class="odd">
                                                        <td><h:outputText value="#{msgs.source_rec_status_but}"/></td>
                                                        <td><%=singleSystemObjectLID.getStatus()%> </td>
                                                    </tr>                                                    
                                                    <tr class="even">
                                                        <td><h:outputText value="#{msgs.source_rec_sourcename_text}"/></td>
                                                        <td><%=singleSystemObjectLID.getSystemCode()%> </td>
                                                    </tr>
                                                    <tr class="odd">
                                                        <td><h:outputText value="#{msgs.datatable_localid_text}"/></td>
                                                        <td><%=singleSystemObjectLID.getLID()%> </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="tablehead" colspan="2">
                                                          <h:outputText value="#{msgs.personal_information_text}"/>                                              
                                                        </td>
                                                    </tr>
                                            </table>
                                            
                                            <!--Start Displaying the person fields -->                                        
                                            <div style="height:300px;width:60%;overflow:auto">                                                    
                                                    <h:dataTable  
                                                        headerClass="tablehead" 
                                                        id="hashId" 
                                                        width="100%"
                                                        var="fieldConfig" 
                                                        rowClasses="odd,even"
                                                        value="#{SourceHandler.personFieldConfigs}">
                                                            <h:column>
                                                                <h:outputText value="#{fieldConfig.displayName}"  />
                                                            </h:column>
                                                            <h:column>
                                                                <h:outputText value="#{SourceHandler.singleSOHashMap[fieldConfig.fullFieldName]}"  />
                                                            </h:column>
                                                    </h:dataTable>               
                                            </div>
                                     <!--End Displaying the person fields -->    
                                     <!--Start Displaying the address fields -->    
                                     <h:dataTable 
                                        headerClass="tablehead"                                        
                                        width="60%"
                                        rowClasses="odd,even"                                     
                                        id="adfieldConfigId" 
                                        var="adressMapArrayList" 
                                        value="#{SourceHandler.singleAddressHashMapArrayList}">
                                        <f:facet name="header">
                                             <h:column>
                                                <h:outputText value="#{msgs.source_rec_address_details_text}"/>
                                             </h:column>
                                        </f:facet>
                                        <f:facet name="footer">
                                             <h:column>
                                                 <% 
                                                     addressSize = sourceHandler.getSingleAddressHashMapArrayList().size();
                                                  %>
                                                  <% if (addressSize  == 0 ) { %>
                                                       <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                  <%}%>
                                            </h:column>
                                        </f:facet>
                                                     
                                        <h:column>
                                            <h:dataTable  
                                               headerClass="tablehead" 
                                               width="100%"
                                               rowClasses="odd,even"                                     
                                                id="addressHashId" 
                                                var="addressFieldConfig" 
                                                value="#{SourceHandler.addressFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                </h:column>
                                                <h:column rendered="#{addressFieldConfig.displayName eq 'Address Type'}">
                                                            <b>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'H'}" value="#{msgs.source_rec_viewedit_homeadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'H2'}" value="#{msgs.source_rec_viewedit_homeadd2}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'M'}" value="#{msgs.source_rec_viewedit_mailadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'O'}" value="#{msgs.source_rec_viewedit_offadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'T'}" value="#{msgs.source_rec_viewedit_temadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'V'}" value="#{msgs.source_rec_viewedit_vocationadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'W'}" value="#{msgs.source_rec_viewedit_workadd}"/>
                                                                <h:outputText rendered="#{adressMapArrayList[addressFieldConfig.fullFieldName] eq 'W2'}" value="#{msgs.source_rec_viewedit_workadd2}"/>
                                                            </b>
                                                </h:column>

                                                <h:column rendered="#{addressFieldConfig.displayName ne 'Address Type'}">
                                                    <h:outputText value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" />
                                                </h:column>
                                                     
                                            </h:dataTable>               
                                        </h:column>
                                     </h:dataTable> 
                                    <!--End Displaying the Address fields -->    

                                    <!--Start Displaying the phone fields -->    
                                     <h:dataTable  
                                        headerClass="tablehead"                                        
                                        width="60%"
                                        rowClasses="odd,even"                                     
                                          id="phfieldConfigId" 
                                          var="phoneMapArrayList" 
                                                 value="#{SourceHandler.singlePhoneHashMapArrayList}">
                                        <f:facet name="header">
                                             <h:column>
                                                <h:outputText value="#{msgs.source_rec_phone_text}"/>
                                             </h:column>
                                        </f:facet>
                                        <f:facet name="footer">
                                             <h:column>
                                                 <% 
                                                     phoneSize = sourceHandler.getSinglePhoneHashMapArrayList().size();
                                                  %>
                                                  <% if (phoneSize  == 0 ) { %>
                                                       <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                  <%}%>
                                            </h:column>
                                        </f:facet>
                                                     
                                        <h:column>
                                            <h:dataTable  
                                        headerClass="tablehead"      
                                        width="100%"
                                        rowClasses="odd,even"                                     
                                             id="phoneHashId" 
                                             var="phoneFieldConfig" 
                                                         value="#{SourceHandler.phoneFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{phoneFieldConfig.displayName}"  />
                                                </h:column>
                                                <h:column rendered="#{phoneFieldConfig.displayName ne 'Phone Type'}">
                                                    <h:outputText value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" />
                                                </h:column>
                                                <h:column rendered="#{phoneFieldConfig.displayName eq 'Phone Type'}">
                                                    <b>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CB'}" value="#{msgs.source_rec_viewedit_businessphone}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CBA'}" value="#{msgs.source_rec_viewedit_alternate_business_phone}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CBD'}" value="#{msgs.source_rec_viewedit_business_direct}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CC'}" value="#{msgs.source_rec_viewedit_cellular_phone}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CF'}" value="#{msgs.source_rec_viewedit_fax}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CH'}" value="#{msgs.source_rec_viewedit_home_phone}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CO'}" value="#{msgs.source_rec_viewedit_office_phone}"/>
                                                     <h:outputText rendered="#{phoneMapArrayList[phoneFieldConfig.fullFieldName] eq 'CP'}" value="#{msgs.source_rec_viewedit_pager}"/>
                                                     </b>
                                               </h:column>
                                          </h:dataTable>               
                                        </h:column>
                                     </h:dataTable> 
                                    <!--End Displaying the phone fields -->    

                                    <!--Start Displaying the alias fields -->    
                                     <h:dataTable  
                                        headerClass="tablehead"                                        
                                        width="60%"
                                        rowClasses="odd,even"                                     
                                     id="aliasfieldConfigId" 
                                     var="aliasMapArrayList" 
                                                 value="#{SourceHandler.singleAliasHashMapArrayList}">
                                        <f:facet name="header">
                                             <h:column>
                                                <h:outputText value="#{msgs.source_rec_alias_details_text}"/>
                                            </h:column>
                                        </f:facet>
                                                     
                                        <h:column>
                                            <h:dataTable  
                                              headerClass="tablehead"                                        
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              id="aliasHashId" 
                                              var="aliasFieldConfig" 
                                                         value="#{SourceHandler.aliasFieldConfigs}">
                                                <h:column>
                                                    <h:outputText value="#{aliasFieldConfig.displayName}"  />
                                                </h:column>
                                                <h:column>
                                                    <h:outputText value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" />
                                                </h:column>
                                            </h:dataTable>               
                                        </h:column>
                                        <f:facet name="footer">
                                             <h:column>
                                                 <% 
                                                     aliasSize = sourceHandler.getSingleAliasHashMapArrayList().size();
                                                  %>
                                                  <% if (aliasSize  == 0 ) { %>
                                                       <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                  <%}%>
                                            </h:column>
                                        </f:facet>
                                     </h:dataTable> 
                                    <!--End Displaying the alias fields -->    


                                    
                                            <table>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <%
                                                    ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());

                                                    %>
                                                    
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toSourceRecords}" 
                                                                        rendered="#{Operations.SO_Edit}"
                                                                        actionListener="#{SourceEditHandler.editLID}" >
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>                
                                                            <span><h:outputText value="#{msgs.source_rec_edit_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        rendered="#{Operations.SO_SearchView}"
                                                                        action="#{NavigationHandler.toEuidDetails}" 
                                                                        actionListener="#{SourceHandler.viewEUID}" >  
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
                                                </tr> 
                                                <tr><td>&nbsp;</td></tr>
                                                
                                            </table>
                                        </div>
                                    </h:form>
                                    <%} else if ("editSO".equalsIgnoreCase(keyFunction)) {%>
                                    <%
                                     ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());
                                    %>
                                    
                                    <div id="sourceViewBasicSearch">
                                        
                                        <h:form>
                                                    <table border="0" width="75%">
                                                        <tr>
                                                            <td>
                                                                <h:commandLink  styleClass="button" 
                                                                                action="#{NavigationHandler.toSourceRecords}" 
                                                                                actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                    <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                                </h:commandLink>                                                                                                 
                                                            </td>
                                                        </tr>  
                                                        <tr class="odd">
                                                            <td><h:outputText value="#{msgs.source_rec_status_but}"/> </td>
                                                            <td><%=singleSystemObjectLID.getStatus()%> </td>
                                                        </tr>                                                    
                                                        <tr class="even">
                                                            <td><h:outputText value="#{msgs.source_rec_status_but}"/></td>
                                                            <td><%=singleSystemObjectLID.getSystemCode()%> </td>
                                                        </tr>
                                                        <tr class="odd">
                                                            <td><h:outputText value="#{msgs.datatable_localid_text}"/> </td>
                                                            <td><%=singleSystemObjectLID.getLID()%> </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <h:outputText value="#{msgs.personal_information_text}"/>                                               
                                                            </td>
                                                        </tr>
                                                    </table>    
                                        </h:form>
                                        <h:form id="BasicSearchFieldsForm">
                                            <table width="100%">
                                                <tr>
                                                    <td align="left">
                                                        <% if ("View/Edit".equalsIgnoreCase((String)session.getAttribute("tabName")))      {%>
                                                          <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                        <!-- Start EDIT Fields-->
                                                            <!--Start Displaying the person fields -->                                        
                                                              <h:dataTable  
                                            headerClass="tablehead"                                        
                                            width="100%"
                                            rowClasses="odd,even"                                     
                                            id="hashIdEdit" 
                                            var="fieldConfigPer" 
                                            value="#{SourceHandler.personFieldConfigs}">                                                    
                                                    <h:column>
                                                        <h:outputText value="#{fieldConfigPer.displayName}"  />
                                                        <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                    </h:column>                                                        
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" >
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextbox"  
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                     />
                                                    </h:column>
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                          <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6  }">
                                                                  <h:inputText label="#{fieldConfigPer.displayName}"   
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}"  
                                                                 
                                                                     required="#{fieldConfigPer.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                    <script> var DOB = getDateFieldName('BasicSearchFieldsForm',':DOB');</script>                                                                             
                                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,DOB)" > 
                                                                        <h:graphicImage  id="calImgDobDate" 
                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                         url="./images/cal.gif"/>               
                                                                    </a>
                                                                </h:column>
                                                                
                                                                
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPer.fullFieldName]}" 
                                                                                     required="#{fieldConfigPer.required}"/>
                                                                </h:column>
                                                            </h:dataTable>               
                                                            <!--End Displaying the person fields -->    
                                                  
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.datatable_address_text}"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAddressEditDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_addaddress_but}"/></span>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                        <!-- Start Display address fields --> 
                                                          <h:dataTable  headerClass="tablehead" 
                                                  width="75%"
                                                  rowClasses="odd,even"                                     
                                                  id="adfieldConfigId" 
                                                  var="adressMapArrayList" 
                                                  value="#{SourceEditHandler.singleAddressHashMapArrayList}">
                                                    <f:facet name="footer">
                                                        <h:column>
                                                            <%
                                                             addressSize = sourceEditHandler.getSingleAddressHashMapArrayList().size();
                                                            %>
                                                            <% if (addressSize == 0) {%>
                                                              <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                            <%}%>              
                                                        </h:column>
                                                    </f:facet>
                                                <h:column>
                                                     <h:dataTable  width="100%"
                                                      rowClasses="odd,even"                                     
                                                      id="addressHashId" 
                                                      var="addressFieldConfig" 
                                                      value="#{SourceHandler.addressFieldConfigs}">
                                                    <h:column>
                                                        <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                        <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                    </h:column>
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" >
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                        <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                     required="#{addressFieldConfig.required}"/>
                                                    </h:column>
                                                    
                                                    
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                    <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                        <h:inputText label="#{addressFieldConfig.displayName}"   
                                                                     value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                     required="#{addressFieldConfig.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                         required="#{addressFieldConfig.required}"/>
                                                                     </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{SourceEditHandler.removeSOAddress}" >  
                                                                                <f:attribute name="remAddressMap" value="#{adressMapArrayList}"/>
                                                                                <span><h:outputText value="#{msgs.source_rec_delete_address_text}"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                </h:dataTable>               
                                                 </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display address fields --> 
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        
                                                        <!-- Start Display Phone fields --> 
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_phone_text}"/>                                                  
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraPhoneEditDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_add_phone_but}"/></span>
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <h:dataTable width="75%"
                                                                     rowClasses="odd,even"                                     
                                                                     id="phfieldConfigId" 
                                                                     var="phoneMapArrayList" 
                                                                     value="#{SourceEditHandler.singlePhoneHashMapArrayList}">
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <%
                                                                      phoneSize = sourceEditHandler.getSinglePhoneHashMapArrayList().size();
                                                                    %>
                                                                    <% if (phoneSize == 0) {%>
                                                                    <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                                    <%}%>
                                                                </h:column>
                                                            </f:facet>
                                                            <h:column>
                                                                <h:dataTable  headerClass="tablehead" 
                                                                              width="100%"
                                                                              rowClasses="odd,even"                                     
                                                                              id="phoneHashId" 
                                                                              var="phoneFieldConfig" 
                                                                              value="#{SourceHandler.phoneFieldConfigs}">
                                                                    <h:column>
                                                                        <h:outputText value="#{phoneFieldConfig.displayName}"  />
                                                                        <h:outputText value="*" rendered="#{phoneFieldConfig.required}" />
                                                                    </h:column>
                                                                    <!--Rendering HTML Select Menu List-->
                                                                    <h:column rendered="#{phoneFieldConfig.guiType eq 'MenuList' &&  phoneFieldConfig.valueType ne 6}" >
                                                                        <h:selectOneMenu value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" >
                                                                            <f:selectItem itemLabel="" itemValue="" />
                                                                            <f:selectItems  value="#{phoneFieldConfig.selectOptions}"  />
                                                                        </h:selectOneMenu>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType ne 6}" >
                                                                        <h:inputText label="#{phoneFieldConfig.displayName}"  
                                                                                     id="fieldConfigIdTextbox"   
                                                                                     value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                                     required="#{phoneFieldConfig.required}"/>
                                                                    </h:column>
                                                                    
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                                    <h:column rendered="#{phoneFieldConfig.guiType eq 'TextBox' &&  phoneFieldConfig.valueType eq 6}">
                                                                        <h:inputText label="#{phoneFieldConfig.displayName}"   
                                                                                     value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}"  
                                                                                  
                                                                                     required="#{phoneFieldConfig.required}"
                                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{phoneFieldConfig.guiType eq 'TextArea' &&  phoneFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{phoneFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{phoneMapArrayList[phoneFieldConfig.fullFieldName]}" 
                                                                                         required="#{phoneFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{SourceEditHandler.removeSOPhone}" >  
                                                                                <f:attribute name="remPhoneMap" value="#{phoneMapArrayList}"/>
                                                                                <span><h:outputText value="#{msgs.source_rec_delete_phone_text}"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                    
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display Phone fields --> 
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        
                                                        <!-- Start Display Alias fields --> 
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_alias_text}"/>                                               
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAliasEditDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_add_alias_text}"/></span></a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                        <h:dataTable  headerClass="tablehead" 
                                                                      width="75%"
                                                                      rowClasses="odd,even"                                     
                                                                      id="aliasfieldConfigId" 
                                                                      var="aliasMapArrayList" 
                                                                      value="#{SourceEditHandler.singleAliasHashMapArrayList}">
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <%
                                                                     aliasSize = sourceEditHandler.getSingleAliasHashMapArrayList().size();
                                                                    %>
                                                                    <% if (aliasSize == 0) {%>
                                                                    <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                                    <%}%>
                                                                </h:column>
                                                            </f:facet>
                                                            <h:column>
                                                                <h:dataTable  headerClass="tablehead" 
                                                                              id="aliasHashId" 
                                                                              width="100%"
                                                                              rowClasses="odd,even"                                     
                                                                              var="aliasFieldConfig" 
                                                                              value="#{SourceHandler.aliasFieldConfigs}">
                                                                    
                                                                    <h:column>
                                                                        <h:outputText value="#{aliasFieldConfig.displayName}"  />
                                                                        <h:outputText value="*" rendered="#{aliasFieldConfig.required}" />
                                                                    </h:column>
                                                                    <!--Rendering HTML Select Menu List-->
                                                                    <h:column rendered="#{aliasFieldConfig.guiType eq 'MenuList' &&  aliasFieldConfig.valueType ne 6}" >
                                                                        <h:selectOneMenu value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" >
                                                                            <f:selectItem itemLabel="" itemValue="" />
                                                                            <f:selectItems  value="#{aliasFieldConfig.selectOptions}"  />
                                                                        </h:selectOneMenu>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType ne 6}" >
                                                                        <h:inputText label="#{aliasFieldConfig.displayName}"  
                                                                                     id="fieldConfigIdTextbox"   
                                                                                     value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                                     required="#{aliasFieldConfig.required}"/>
                                                                    </h:column>
                                                                    
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                                    <h:column rendered="#{aliasFieldConfig.guiType eq 'TextBox' &&  aliasFieldConfig.valueType eq 6}">
                                                                        <h:inputText label="#{aliasFieldConfig.displayName}"   
                                                                                     value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}"  
                                                                                  
                                                                                     required="#{aliasFieldConfig.required}"
                                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{aliasFieldConfig.guiType eq 'TextArea' &&  aliasFieldConfig.valueType ne 6}" >
                                                                        <h:inputTextarea label="#{aliasFieldConfig.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{aliasMapArrayList[aliasFieldConfig.fullFieldName]}" 
                                                                                         required="#{aliasFieldConfig.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{SourceEditHandler.removeSOAlias}" >  
                                                                                <f:attribute name="remAliasMap" value="#{aliasMapArrayList}"/>
                                                                                <span><h:outputText value="#{msgs.source_rec_delete_alias_text}"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                    
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        <!-- End Display Alias fields --> 
                                  <!-- End EDIT Fields-->
                                 <%} else if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                  <!-- Start READ ONLY Fields-->
                                        <!-- Start EDIT Fields-->
                                                            <!--Start Displaying the person fields -->                                        
                                           <h:dataTable  
                                            headerClass="tablehead"                                        
                                            width="100%"
                                            rowClasses="odd,even"                                     
                                            id="hashIdEdit" 
                                            var="fieldConfigPerReadOnly" 
                                            value="#{SourceHandler.personFieldConfigs}">                                                    
                                                    <h:column>
                                                        <h:outputText value="#{fieldConfigPerReadOnly.displayName}"  />
                                                        <h:outputText value="*" rendered="#{fieldConfigPerReadOnly.required}" />
                                                    </h:column>                                                        
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPerReadOnly.guiType eq 'MenuList' &&  fieldConfigPerReadOnly.valueType ne 6}" >
                                                        <h:selectOneMenu disabled="true"   style="background-color:#efefef;font-color:#efefef"readonly="true" value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPerReadOnly.fullFieldName]}" >
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPerReadOnly.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPerReadOnly.updateable && fieldConfigPerReadOnly.guiType eq 'TextBox' &&  fieldConfigPerReadOnly.valueType ne 6}" >
                                                        <h:inputText disabled="true"   style="background-color:#efefef;font-color:#efefef"readonly="true" label="#{fieldConfigPerReadOnly.displayName}"  
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPerReadOnly.fullFieldName]}" 
                                                                     required="#{fieldConfigPerReadOnly.required}"
                                                                     />
                                                    </h:column>
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                   <h:column rendered="#{fieldConfigPerReadOnly.guiType eq 'TextBox' &&  fieldConfigPerReadOnly.valueType eq 6}">
                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{fieldConfigPerReadOnly.displayName}"   
                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPerReadOnly.fullFieldName]}"  
                                                                    
                                                                     required="#{fieldConfigPerReadOnly.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                            <script> var DOB = getDateFieldName('BasicSearchFieldsForm',':DOB');</script>                                                                                   
                                                                            <a HREF="javascript:void(0);" 
                                                                               onclick="g_Calendar.show(event,DOB)" > 
                                                                                <h:graphicImage  id="calImgdodDate" 
                                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                                 url="./images/cal.gif"/>               
                                                                            </a>
                                                     </h:column>
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPerReadOnly.updateable && fieldConfigPerReadOnly.guiType eq 'TextArea' &&  fieldConfigPerReadOnly.valueType ne 6}" >
                                                                    <h:inputTextarea readonly="true" 
                                                                                     disabled="true"
                                                                                     label="#{fieldConfigPerReadOnly.displayName}"  
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     value="#{SourceEditHandler.editSingleSOHashMap[fieldConfigPerReadOnly.fullFieldName]}" 
                                                                                     required="#{fieldConfigPerReadOnly.required}"/>
                                                                </h:column>
                                                            </h:dataTable>               
                                                            <!--End Displaying the person fields -->    
                                                
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.datatable_address_text}"/>                                                
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                        <!-- Start Display address fields --> 
                                                         <h:dataTable  headerClass="tablehead" 
                                                  width="75%"
                                                  rowClasses="odd,even"                                     
                                                  id="adfieldConfigId" 
                                                  var="adressMapArrayList" 
                                                  value="#{SourceEditHandler.singleAddressHashMapArrayList}">
                                                    <f:facet name="footer">
                                                        <h:column>
                                                            <%
                                                               addressSize = sourceEditHandler.getSingleAddressHashMapArrayList().size();
                                                            %>
                                                            <% if (addressSize == 0) {%>
                                                              <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                            <%}%>              
                                                        </h:column>
                                                    </f:facet>
                                     <h:column>
                                        <h:dataTable  width="100%"
                                                      rowClasses="odd,even"                                     
                                                      id="addressHashId" 
                                                      var="addressFieldConfigReadOnly" 
                                                      value="#{SourceHandler.addressFieldConfigs}">
                                                    <h:column>
                                                        <h:outputText value="#{addressFieldConfigReadOnly.displayName}"  />
                                                        <h:outputText value="*" rendered="#{addressFieldConfigReadOnly.required}" />
                                                    </h:column>
                                                    <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{addressFieldConfigReadOnly.guiType eq 'MenuList' &&  addressFieldConfigReadOnly.valueType ne 6}" >
                                                        <h:selectOneMenu readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"value="#{adressMapArrayList[addressFieldConfigReadOnly.fullFieldName]}" >
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{addressFieldConfigReadOnly.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{addressFieldConfigReadOnly.guiType eq 'TextBox' &&  addressFieldConfigReadOnly.valueType ne 6}" >
                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{addressFieldConfigReadOnly.displayName}"  
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{adressMapArrayList[addressFieldConfigReadOnly.fullFieldName]}" 
                                                                     required="#{addressFieldConfigReadOnly.required}"/>
                                                    </h:column>
                                                    
                                                    
                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                    <h:column rendered="#{addressFieldConfigReadOnly.guiType eq 'TextBox' &&  addressFieldConfigReadOnly.valueType eq 6}">
                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{addressFieldConfigReadOnly.displayName}"   
                                                                     value="#{adressMapArrayList[addressFieldConfigReadOnly.fullFieldName]}"  
                                                                  
                                                                     required="#{addressFieldConfigReadOnly.required}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{addressFieldConfigReadOnly.guiType eq 'TextArea' &&  addressFieldConfigReadOnly.valueType ne 6}" >
                                                                        <h:inputTextarea readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"
                                                                                         label="#{addressFieldConfigReadOnly.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{adressMapArrayList[addressFieldConfigReadOnly.fullFieldName]}" 
                                                                                         required="#{addressFieldConfigReadOnly.required}"/>
                                                                    </h:column>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display address fields --> 
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        
                                                        <!-- Start Display Phone fields --> 
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_phone_text}"/>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <h:dataTable width="75%"
                                                                     rowClasses="odd,even"                                     
                                                                     id="phfieldConfigId" 
                                                                     var="phoneMapArrayList" 
                                                                     value="#{SourceEditHandler.singlePhoneHashMapArrayList}">
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <%
                                                                       phoneSize = sourceEditHandler.getSinglePhoneHashMapArrayList().size();
                                                                    %>
                                                                    <% if (phoneSize == 0) {%>
                                                                    <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                                    <%}%>
                                                                </h:column>
                                                            </f:facet>
                                                            <h:column>
                                                                <h:dataTable  headerClass="tablehead" 
                                                                              width="100%"
                                                                              rowClasses="odd,even"                                     
                                                                              id="phoneHashId" 
                                                                              var="phoneFieldConfigReadOnly" 
                                                                              value="#{SourceHandler.phoneFieldConfigs}">
                                                                    <h:column>
                                                                        <h:outputText value="#{phoneFieldConfigReadOnly.displayName}"  />
                                                                        <h:outputText value="*" rendered="#{phoneFieldConfigReadOnly.required}" />
                                                                    </h:column>
                                                                    <!--Rendering HTML Select Menu List-->
                                                                    <h:column rendered="#{phoneFieldConfigReadOnly.guiType eq 'MenuList' &&  phoneFieldConfigReadOnly.valueType ne 6}" >
                                                                        <h:selectOneMenu readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"value="#{phoneMapArrayList[phoneFieldConfigReadOnly.fullFieldName]}" >
                                                                            <f:selectItem itemLabel="" itemValue="" />
                                                                            <f:selectItems  value="#{phoneFieldConfigReadOnly.selectOptions}"  />
                                                                        </h:selectOneMenu>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{phoneFieldConfigReadOnly.guiType eq 'TextBox' &&  phoneFieldConfigReadOnly.valueType ne 6}" >
                                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{phoneFieldConfigReadOnly.displayName}"  
                                                                                     id="fieldConfigIdTextbox"   
                                                                                     value="#{phoneMapArrayList[phoneFieldConfigReadOnly.fullFieldName]}" 
                                                                                     required="#{phoneFieldConfigReadOnly.required}"/>
                                                                    </h:column>
                                                                    
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                                    <h:column rendered="#{phoneFieldConfigReadOnly.guiType eq 'TextBox' &&  phoneFieldConfigReadOnly.valueType eq 6}">
                                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{phoneFieldConfigReadOnly.displayName}"   
                                                                                     value="#{phoneMapArrayList[phoneFieldConfigReadOnly.fullFieldName]}"  
                                                                                  
                                                                                     required="#{phoneFieldConfigReadOnly.required}"
                                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{phoneFieldConfigReadOnly.guiType eq 'TextArea' &&  phoneFieldConfigReadOnly.valueType ne 6}" >
                                                                        <h:inputTextarea readonly="true" disabled="true"
                                                                                         label="#{phoneFieldConfigReadOnly.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{phoneMapArrayList[phoneFieldConfigReadOnly.fullFieldName]}" 
                                                                                         required="#{phoneFieldConfigReadOnly.required}"/>
                                                                    </h:column>
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <!-- End Display Phone fields --> 
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        
                                                        <!-- Start Display Alias fields --> 
                                                        <table border="0" width="75%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_alias_text}"/>                                               
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                        <h:dataTable  headerClass="tablehead" 
                                                                      width="75%"
                                                                      rowClasses="odd,even"                                     
                                                                      id="aliasFieldConfigReadId" 
                                                                      var="aliasMapArrayList" 
                                                                      value="#{SourceEditHandler.singleAliasHashMapArrayList}">
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <%
                                                                      aliasSize = sourceEditHandler.getSingleAliasHashMapArrayList().size();
                                                                    %>
                                                                    <% if (aliasSize == 0) {%>
                                                                    <h:outputText value="#{msgs.source_rec_nodetails_text}"/>
                                                                    <%}%>
                                                                </h:column>
                                                            </f:facet>
                                                            <h:column>
                                                                <h:dataTable  headerClass="tablehead" 
                                                                              id="aliasHashId" 
                                                                              width="100%"
                                                                              rowClasses="odd,even"                                     
                                                                              var="aliasFieldConfigRead" 
                                                                              value="#{SourceHandler.aliasFieldConfigs}">
                                                                    
                                                                    <h:column>
                                                                        <h:outputText value="#{aliasFieldConfigRead.displayName}"  />
                                                                        <h:outputText value="*" rendered="#{aliasFieldConfigRead.required}" />
                                                                    </h:column>
                                                                    <!--Rendering HTML Select Menu List-->
                                                                    <h:column rendered="#{aliasFieldConfigRead.guiType eq 'MenuList' &&  aliasFieldConfigRead.valueType ne 6}" >
                                                                        <h:selectOneMenu readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"value="#{aliasMapArrayList[aliasFieldConfigRead.fullFieldName]}" >
                                                                            <f:selectItem itemLabel="" itemValue="" />
                                                                            <f:selectItems  value="#{aliasFieldConfigRead.selectOptions}"  />
                                                                        </h:selectOneMenu>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{aliasFieldConfigRead.guiType eq 'TextBox' &&  aliasFieldConfigRead.valueType ne 6}" >
                                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{aliasFieldConfigRead.displayName}"  
                                                                                     id="fieldConfigIdTextbox"   
                                                                                     value="#{aliasMapArrayList[aliasFieldConfigRead.fullFieldName]}" 
                                                                                     required="#{aliasFieldConfigRead.required}"/>
                                                                    </h:column>
                                                                    
                                                                    
                                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                                    <h:column rendered="#{aliasFieldConfigRead.guiType eq 'TextBox' &&  aliasFieldConfigRead.valueType eq 6}">
                                                                        <h:inputText readonly="true" disabled="true"   style="background-color:#efefef;font-color:#efefef"label="#{aliasFieldConfigRead.displayName}"   
                                                                                     value="#{aliasMapArrayList[aliasFieldConfigRead.fullFieldName]}"  
                                                                                    
                                                                                     required="#{aliasFieldConfigRead.required}"
                                                                                     onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <a HREF="javascript:void(0);" 
                                                                           onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                            <h:graphicImage  id="calImgStartDate" 
                                                                                             alt="calendar Image" styleClass="imgClass"
                                                                                             url="./images/cal.gif"/>               
                                                                        </a>
                                                                    </h:column>
                                                                    
                                                                    <!--Rendering Updateable HTML Text Area-->
                                                                    <h:column rendered="#{aliasFieldConfigRead.guiType eq 'TextArea' &&  aliasFieldConfigRead.valueType ne 6}" >
                                                                        <h:inputTextarea readonly="true" 
                                                                                         disabled="true"
                                                                                         label="#{aliasFieldConfigRead.displayName}"  
                                                                                         id="fieldConfigIdTextArea"   
                                                                                         value="#{aliasMapArrayList[aliasFieldConfigRead.fullFieldName]}" 
                                                                                         required="#{aliasFieldConfigReadOnly.required}"/>
                                                                    </h:column>
                                                                    <f:facet name="footer">
                                                                        <h:column>
                                                                            <h:commandLink  styleClass="button" 
                                                                                            actionListener="#{SourceEditHandler.removeSOAlias}" >  
                                                                                <f:attribute name="remAliasMap" value="#{aliasMapArrayList}"/>
                                                                                <span><h:outputText value="#{msgs.source_rec_delete_alias_text}"/></span>
                                                                            </h:commandLink>                                     
                                                                        </h:column>
                                                                    </f:facet>
                                                                    
                                                                </h:dataTable>               
                                                            </h:column>
                                                        </h:dataTable>                                                             
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        <!-- End Display Alias fields --> 
                                  <!-- End READ ONLY  Fields-->

                                 <%}%>
                                                        <table>  
                                                            <tr>        
                                                                <td>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Edit}"
                                                                                    action="#{SourceEditHandler.updateSO}" >
                                                                        <span><h:outputText value="#{msgs.source_rec_save_but}" /></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <h:commandLink  styleClass="button" 
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}" 
                                                                                    action="#{NavigationHandler.toEuidDetails}" 
                                                                                    actionListener="#{SourceHandler.viewEUID}" >  
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td>
                                                                    <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Deactivate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.deactivateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                    <%if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Activate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.activateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        
                                                    </td>
                                                </tr>
                                            </table>

                                        </h:form>
                                    </div>
                                    <%}%>
                                    <%} else {%>
                                    <div id="sourceViewBasicSearch">
                                        <h:form id="basicViewformData">
                                            <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                            <table border="0" cellpadding="0" cellspacing="0">
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <td>
                                                        <h:dataTable id="fieldConfigId" var="feildConfig" headerClass="tablehead"  value="#{SourceHandler.viewEditScreenConfigArray}">
                                                            <!--Rendering Non Updateable HTML Text Area-->
                                                            <h:column>
                                                                <h:outputText value="#{feildConfig.displayName}" />
                                                            </h:column>
                                                            <!--Rendering HTML Select Menu List-->
                                                            <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                                               <h:selectOneMenu value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" rendered="#{feildConfig.name eq 'SystemCode'}"
                                                                                 onchange="javascript:setLidMaskValue(this,'basicViewformData')">
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                               <h:selectOneMenu value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" rendered="#{feildConfig.name ne 'SystemCode'}">
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                            </h:column>
                                                            
                                                            <!--Rendering Updateable HTML Text boxes-->
                                                            <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="fieldConfigIdText" value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <!--Rendering Updateable HTML Text Area-->
                                                            <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                                                                <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{SourceHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            
                                                            <!--Rendering Non Updateable HTML Text boxes-->
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                                                <h:inputText id="SystemCode" value="#{SourceHandler.SystemCode}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="EUID" value="#{SourceHandler.EUID}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="LID" value="#{SourceHandler.LID}" required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, document.basicViewformData.lidmask.value)"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"
                                                                             />
                                                            </h:column>
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    value="#{SourceHandler.create_start_date}"  id="create_start_date"
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <a HREF="javascript:void(0);" 
                                                                   onclick="g_Calendar.show(event,'basicViewformData:fieldConfigId:1:create_start_date')" > 
                                                                    <h:graphicImage  id="calImgStartDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    value="#{SourceHandler.create_end_date}" id="create_end_date" 
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                             onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 'basicViewformData:fieldConfigId:2:create_end_date')" > 
                                                                    <h:graphicImage  id="calImgEndDate" 
                                                                                     alt="calendar Image" styleClass="imgClass"
                                                                                     url="./images/cal.gif"/>               
                                                                </a>
                                                            </h:column>
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                                                <h:inputText label="#{feildConfig.displayName}"    rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                                             value="#{SourceHandler.create_start_time}" required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="create_end_time" value="#{SourceHandler.create_end_time}" 
                                                                             required="#{feildConfig.required}"
                                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                             onkeyup="javascript:qws_field_on_key_up(this)"/>
                                                            </h:column>
                                                            
                                                            <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                                                <h:inputText label="#{feildConfig.displayName}"    id="Status"  value="#{SourceHandler.Status}" required="#{feildConfig.required}"/>
                                                            </h:column>
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                    <a class="button" href="javascript:ClearContents('basicViewformData')">
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                    <h:commandLink  styleClass="button" action="#{SourceHandler.performSubmit}" >  
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </h:column>
                                                                
                                                            </f:facet>
                                                            
                                                        </h:dataTable>
                                                    </td>
                                                    <td valign="top">
                                                        <% if ("View/Edit".equalsIgnoreCase((String)session.getAttribute("tabName")))      {%>
                                                          <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table>  
                                        </h:form>
                                    </div>                                                                 
                                    <div id="results" class=reportYUISearch"">
                                        <%
                                        //check if so's in session
                                        if (systemObjectsMapList != null && searchResultsScreenConfigArray != null) {
                                        %>
                                        <table>
                                            <tr>
                                                <td>
                                                    <h:outputText value="#{msgs.total_records_text}"/><%=systemObjectsMapList.size()%> 
                                                </td>
                                                <td>
                                                    <a class="button" href="#"><span><h:outputText value="#{msgs.print_text}"/></span></a>
                                                </td>
                                            </tr>
                                        </table>  
                                        <table> 
                                            <thead>
                                                <th>LID</th>
                                                <th>EUID</th>
                                                <%

                                            Object[] searchResultsObj = searchResultsScreenConfigArray.toArray();
                                            for (int i = 0; i < searchResultsObj.length; i++) {
                                                FieldConfig fieldConfig = (FieldConfig) searchResultsObj[i];
                                                //System.out.println("dispklay Name" + fieldConfig.getDisplayName());
                                                %>
                                                <th><%=fieldConfig.getDisplayName()%></th>
                                                <%
                                            }
                                                %>
                                                <th>Create Date Time</th>
                                                <th>Source</th>
                                            </thead>
                                            <h:form>
                                                <tbody>
                                                    <%for (int al = 0; al < systemObjectsMapList.size(); al++) {
                                                HashMap fieldvalues = (HashMap) systemObjectsMapList.get(al);
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <%
    LIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(fieldvalues.get("LID"), fieldvalues.get("LID").getClass());
    sourceSystemVaueExpression = ExpressionFactory.newInstance().createValueExpression(fieldvalues.get("Source"), fieldvalues.get("Source").getClass());
                                                            %>
                                                            <h:commandLink actionListener="#{SourceHandler.setLIDValue}">
                                                                <%=fieldvalues.get("LID")%>
                                                                <f:attribute name="sourceLID" value="<%=LIDVaueExpression%>"/>
                                                                <f:attribute name="sourceSystem" value="<%=sourceSystemVaueExpression%>"/>
                                                            </h:commandLink>
                                                        </td>
                                                        <td><%=fieldvalues.get("EUID")%></td>
                                                        <%
    for (int i = 0; i < searchResultsObj.length; i++) {
        FieldConfig fieldConfig = (FieldConfig) searchResultsObj[i];
                                                        %>
                                                        <td>
                                                            <%if (fieldvalues.get(fieldConfig.getFullFieldName()) != null) {%> 
                                                            <%=fieldvalues.get(fieldConfig.getFullFieldName())%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>
                                                        </td>
                                                        <%}%>
                                                        <td><%=fieldvalues.get("DateTime")%></td>
                                                        <td><%=fieldvalues.get("Source")%></td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </h:form>
                                        </table>
                                        <%}%>
                                    </div>
                                    <%}%>
                                    
                                </div>  
                              <%}%> 
                              <% if(operations.isSO_Add()){%> 
                                <div id="addTab">
                                    <h:form id="basicAddformData">
                                        <table width="85%">
                                            <tr>
                                                <td>
                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <!--Start Add source record form-->
                                                    <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                                    <input type="hidden" name="ssnmask" value="DDD-DD-DDDD" />
                                                            <table border="0" width="100%">
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{SourceAddHandler.addScreenConfigArray}">
                                <!--Rendering Non Updateable HTML Text Area-->
                                <h:column>
                                    <nobr>
                                        <h:outputText value="*" rendered="#{feildConfig.required}" />
                                        <h:outputText value="#{feildConfig.displayName}" />
                                    </nobr>
                                </h:column>
                                <!--Rendering HTML Select Menu List-->
                                <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                    <nobr>
                                        <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicAddformData')"
                                                          id="SystemCode" 
                                                          value="#{SourceAddHandler.systemCode}" 
                                                          rendered="#{feildConfig.name eq 'SystemCode'}"
                                                          required="true">
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                    </nobr>
                                </h:column>
                                <!--Rendering Updateable HTML Text boxes-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6}" >
                                    <nobr>
                                        <h:inputText   required="true" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, document.basicAddformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       value="#{SourceAddHandler.LID}"
                                                       maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                       rendered="#{feildConfig.name eq 'LID'}"/>
                                                       
                                    </nobr>
                                </h:column>
                           </h:dataTable>
                                                    <%if(session.getAttribute("validation") != null ) {%>
                                               
                                                    <!-- Start ADD  Fields-->
                                                    <table width="100%">
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <h:outputText value="#{msgs.personal_information_text}"/>                                            
                                                            </td>
                                                        </tr>
                                                     </table>
                                                    <!--Start Displaying the person fields -->  
                                                                                                     
                                                               <h:dataTable  headerClass="tablehead"  
                                                                      id="hashIdEdit" 
                                                                      width="100%"
                                                                      rowClasses="odd,even"                                     
                                                                      var="fieldConfigPerAdd" 
                                                                      value="#{SourceAddHandler.personFieldConfigs}">
                                                                <h:column>
                                                                   <h:outputText value="#{fieldConfigPerAdd.displayName}"  />
                                                                   
                                                                    <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
                                                                </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:selectOneMenu value="#{SourceAddHandler.newSOHashMap[fieldConfigPerAdd.fullFieldName]}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 id="fieldConfigIdTextbox"  
                                                                                 value="#{SourceAddHandler.newSOHashMap[fieldConfigPerAdd.fullFieldName]}" 
                                                                                 required="#{fieldConfigPerAdd.required}"/>
                                                                </h:column>                     
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 }">
                                                                    <h:inputText label="#{fieldConfigPerAdd.name}"  
                                                                                 value="#{SourceAddHandler.newSOHashMap[fieldConfigPerAdd.fullFieldName]}"  
                                                                                 required="#{fieldConfigPerAdd.required}"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                 />
                                                                   <script> var DOB1 = getDateFieldName('basicAddformData',':DOB');</script>                                                                            
                                                                    <h:outputLink value="javascript:void(0);"  id="calLink"
                                                                                  onclick="g_Calendar.show(event,DOB1)" > 
                                                                        <h:graphicImage  id="calImgStartDate" 
                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                         url="./images/cal.gif"/>               
                                                                    </h:outputLink>
                                                                </h:column>
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     value="#{SourceAddHandler.newSOHashMap[fieldConfigPerAdd.fullFieldName]}" 
                                                                                     required="#{fieldConfigPerAdd.required}"
                                                                                     />
                                                                </h:column>
                                                                
                                                            </h:dataTable>

                                                            
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.PatDetail_AddressPrompt}"/>                                                
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAddressAddDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_addaddress_but}"/></span>
                                                                        </a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <div id="addAddressDiv" style="width:100%;visibility:hidden;"></div>
                                                        <div id="addAddressDivClose" style="visibility:hidden;">
                                                            <table>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <a href="javascript:closeExtraDivs('addAddressDiv','addAddressDivClose')" class="button">
                                                                            <span><h:outputText value="#{msgs.source_rec_deleteaddress_but}"/></span></a>
                                                                    </td>
                                                                    
                                                                </tr>
                                                                </table>   
                                                        </div>
                                                        
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        
                                                        <!-- Start Display Phone fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_phone_text}"/>                                               
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraPhoneAddDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_add_phone_but}"/> </span></a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <div id="addPhoneDiv" style="width:100%;visibility:hidden;"></div>
                                                        <div id="addPhoneDivClose" style="visibility:hidden;">
                                                            <table>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <a href="javascript:closeExtraDivs('addPhoneDiv','addPhoneDivClose')" class="button">
                                                                            <span><h:outputText value="#{msgs.source_rec_delete_phone_text}"/> </a></span>
                                                                    </td>
                                                                    
                                                                </tr>
                                                                </table>   
                                                        </div>
                                                          
                                                        <!-- End Display Phone fields --> 
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        <!-- Start Display Alias fields --> 
                                                        <table border="0" width="100%">
                                                            <tr><td colspan="2">&nbsp;</td></tr>
                                                            <tr>
                                                                <td class="tablehead" colspan="2">
                                                                    <h:outputText value="#{msgs.source_rec_alias_text}"/>                                               
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right">
                                                                    <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAliasAddDiv',event)" class="button">
                                                                        <span><h:outputText value="#{msgs.source_rec_add_alias_text}"/></span></a>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <div id="addAliasDiv" style="width:100%;visibility:hidden;"></div>
                                                        <div id="addAliasDivClose" style="visibility:hidden;">
                                                            <table>
                                                                <tr>
                                                                    <td align="right" colspan="2">
                                                                        <a href="javascript:closeExtraDivs('addAliasDiv','addAliasDivClose')" class="button">
                                                                            <span><h:outputText value="#{msgs.source_rec_delete_alias_text}"/> </span></a>
                                                                    </td>
                                                                    
                                                                </tr>
                                                                </table>   
                                                        </div>
                                                          
                                                        <table><tr><td>&nbsp;</td></tr></table>
                                                        <!-- End Display Alias fields --> 
                                                        <!-- Start Display Comment fields --> 
                                                        <%}%>                                                                                                                                                           
                                                        
                                                        <!-- End Display Comment fields --> 
                                                    
                                                    <!--End Add source record form-->
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <nobr>
                                                    <a class="button" href="javascript:ClearContents('basicAddformData')">
                                                        <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                    </a>
                                                    </nobr>
                                                    <%if(session.getAttribute("validation") != null ) {%>
                                                    <nobr>
                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Add}"
                                                                    action="#{SourceAddHandler.addNewSO}">  
                                                        <span><h:outputText value="#{msgs.submit_button_text}"/></span>
                                                    </h:commandLink>                                     
                                                    </nobr>
                                                   <%}else{%>
                                                    <nobr>
                                                    <h:commandLink  styleClass="button" rendered="#{Operations.SO_Add}"
                                                                    action="#{SourceAddHandler.validateLID}">  
                                                        <span><h:outputText value="#{msgs.validate_button_text}"/></span>
                                                    </h:commandLink>                                     
                                                    </nobr>
                                                   <%}%>
                                                </td>
                                            </tr>
                                        </table>
                                    </h:form>
                                </div>
                              <%}%> 
                              <% if(operations.isSO_Merge()){%> 
                                <div id="mergeTab">
                                        <table border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td>
                                                   <h:form id="basicMergeformData">
                                                     <table border="0" cellpadding="0" cellspacing="0">
                                                           <tr>
                                                               <td>
                                                                   <h:outputLabel for="#{msgs.transaction_source}" value="#{msgs.transaction_source}"/>
                                                                </td>
                                                               <td>
                                                                   <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicMergeformData')"
                                                                                     id="sourceOption" 
                                                                                     value="#{SourceMergeHandler.source}" >
                                                                       <f:selectItems  value="#{SourceMergeHandler.selectOptions}" />
                                                                   </h:selectOneMenu>
                                                               </td>
                                                               <input id='lidmask' type='hidden' name='lidmask' value='<h:outputText value="#{SourceMergeHandler.lidMask}"/>' />
                                                               
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                               <h:outputText value="#{msgs.source_merge_head1}"/>
                                                               <h:inputText value="#{SourceMergeHandler.lid1}"
                                                                            onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                            onkeyup="javascript:qws_field_on_key_up(this)"
                                                                            maxlength="#{SourceMergeHandler.lidMaskLength}" />  
                                                               <td>&nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head2}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head3}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               <td>
                                                                   <h:outputText value="#{msgs.source_merge_head4}"/>
                                                                   <h:inputText value="#{SourceMergeHandler.lid4}"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                maxlength="#{SourceMergeHandler.lidMaskLength}"/>  
                                                               </td>
                                                               <td> &nbsp;&nbsp</td>
                                                               
                                                               <td><nobr>
                                                                       <h:commandLink  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                                       action="#{SourceMergeHandler.performLidMergeSearch}">
                                                                           <span><h:outputText value="#{msgs.source_merge_button}"/></span>
                                                                       </h:commandLink>                                     
                                                                   </nobr>  
                                                               </td>
                                                           </tr>
                                                
                                                    </table>
                                                   </h:form>
                                             <hr/>
                                             <%
                                               if(session.getAttribute("soHashMapArrayList") != null ){
                                                  //request.setAttribute("soHashMapArrayList",request.getAttribute("soHashMapArrayList")); 
                                                 ArrayList newSoArrayList= (ArrayList) session.getAttribute("soHashMapArrayList");
                                             %>
                                            <table cellpadding="0" cellspacing="0">  
                                            <tr>
                                                <td>
                                                    <div style="height:600px;overflow:auto;">
                                                        <table>
                                                          <tr>
                                                              
                                                               <%
                                                    Object[] soHashMapArrayListObjects = newSoArrayList.toArray();
                                                   // System.out.println("==>Hellllllllllllllllll : " + sourceMergeHandler.getSoArrayList());
                                                    String cssClass = "dynaw169";
                                                    String cssMain = "maineuidpreview";
                                                    String menuClass = "menutop";
                                                    String dupfirstBlue = "dupfirst";
                                                    for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) {
                                                        if (countEnt > 0) {
                                                            cssClass = "dynaw169";
                                                            menuClass = "menutop";
                                                            dupfirstBlue = "dupfirst";
                                                        }
                                                        HashMap soHashMap = (HashMap) soHashMapArrayListObjects[countEnt];
                                                        //System.out.println("Hellllllllllllllllllllllllllllll"+soHashMap);
    LIDVaueExpression = ExpressionFactory.newInstance().createValueExpression(soHashMap.get("LID"), soHashMap.get("LID").getClass());
                                                            %>
                                                               <!-- Display the field Values-->
                                                               <%if(countEnt ==0 ) {%>
                                                                  <td  valign="top">
                                                                          <div id="labelmainEuidContent" class="yellow">
                                                                               <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                                    <tr>
                                                                                       <td id="menu<%=soHashMap.get("LID")%>">&nbsp</td>
                                                                                    </tr> 
                                                                                    <tr>
                                                                                        <td valign="top"  id="Label<%=soHashMap.get("LID")%>"><b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b></td>
                                                                                    </tr>
                                                                               </table>
                                                                           </div>
                                                                          <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                                             <div id="labelpersonEuidDataContent" class="yellow">
                                                                                <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%
                                                        for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                                                                        %>  
                                                                                    <tr>
                                                                                      <td>
                                                                                         <%=fieldConfigMap.getDisplayName()%>                 
                                                                                      </td>
                                                                                    </tr>
                                                                        <%}%>
                                                             
                                                                                     <tr><td>&nbsp;</td></tr>
                                                                                     <tr><td>&nbsp;</td></tr>

                                                                                </table>
                                                                            </div>
                                                                          </div>
                                                                   </td>
                                                               <%}%>
                                                                  <td  valign="top">
                                                                     <div id="outerMainContentDivid<%=countEnt%>">
                                                            <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="yellow">
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td class="menutop"><b> LID&nbsp;<%=countEnt + 1%></b> </td>
                                                                    </tr> 
                                                                    <tr>
                                                                        <script> alllidsArray.push('<%=soHashMap.get("LID")%>')</script>
                                                                            <td valign="top" name="sri" id="curve<%=soHashMap.get("LID")%>">
                                                                            <a class="dupbtn" id="button<%=soHashMap.get("LID")%>" href="javascript:void(0)" onclick="javascript:collectLid('<%=soHashMap.get("LID")%>')">
                                                                                <%=soHashMap.get("LID")%>
                                                                            </a> 
                                                                            </td>
                                                                           <script> var thisText = document.getElementById('curve<%=soHashMap.get("LID")%>').innerHTML; alllidsactionText.push(thisText);</script> 
                                                                        </tr>
                                                                </table>
                                                            </div>
                                                                <div id="personEuidDataContent<%=soHashMap.get("LID")%>" class="yellow">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>"  >
                                                                        <%
                                                        HashMap personfieldValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT");
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
                                                                        <tr>
                                                                            <td>
                                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
                                                                                       <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
        fnameExpression = ExpressionFactory.newInstance().createValueExpression(epathValue, epathValue.getClass());
        fvalueVaueExpression = ExpressionFactory.newInstance().createValueExpression(personfieldValuesMapEO.get(epathValue), personfieldValuesMapEO.get(epathValue).getClass());
                                                                %>
                                                                                            <font class="highlight"><%=personfieldValuesMapEO.get(epathValue)%></font>
                                                                                       <%} else {%>
                                                                                           <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                <%} else {%>
                                                                                     &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr><td>&nbsp;</td></tr>

                                                                    </table>
                                                                </div>
                                                                     </div>
                                                                  </td>
                                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length)   {%>
                                                                  <td  valign="top">
                                                                       <div id="preview<%=countEnt%>">
                                                                <%
                                                                          HashMap mergedSOMap = new HashMap();
                                                                          String styleclass = "yellow";
                                                                          HashMap previewpersonfieldValuesMapEO = new HashMap();
                                                                         if(request.getAttribute("mergedSOMap") != null) {
                                                                          mergedSOMap = (HashMap) request.getAttribute("mergedSOMap");
                                                                          previewpersonfieldValuesMapEO = (HashMap) mergedSOMap.get("SYSTEM_OBJECT");
                                                                          styleclass ="blue";
                                                                         } 
                                                                
                                                                %>
                                                                       
                                                            <div id="previewmainEuidContent" class="<%=styleclass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td id="previewmenu" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                                    </tr> 
                                                                        <tr>
                                                                            <td valign="top"  id="previewcurve">&nbsp;</td>
                                                                        </tr>
                                                                </table>
                                                            </div>
                                                        <div id="previewmainEuidContentButtonDiv">
                                                            <div id="assEuidDataContent">
                                                                <div id="personassEuidDataContent" class="<%=styleclass%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="previewbuttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%

                                                        String previewepathValue;
                                                        for (int ifcp = 0; ifcp < personConfigFeilds.length; ifcp++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifcp];
                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                previewepathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                previewepathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
                                                        
                                                                        %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if(request.getAttribute("mergedSOMap") != null) {%>
                                                                                    <%if (previewpersonfieldValuesMapEO.get(previewepathValue) != null) {%> 
                                                                                          <%=previewpersonfieldValuesMapEO.get(previewepathValue)%>
                                                                                    <%} else {%>
                                                                                        &nbsp;
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr><td>&nbsp;</td></tr>

                                                                    </table>
                                                                </div>
                                                                
                                                                <!--Displaying view sources and view history-->
                                                                
                                                            </div>
                                                        </div>
                                                    </div>
                                                                  </td>
                                                               <%}%>
                                                                <td>&nbsp;</td>                                                
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
                                                          <td colspan="<%=soHashMapArrayListObjects.length*2 + 3%>">
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
                                           <% for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) { %>
                                               <% if (countEnt == 0)    { %>
                                                    <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                               <% }%>
                                                     <!--Displaying view sources and view history-->
                                                     <td valign="top">
                                                         <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" border="1">
                                                                        <h:form>
                                                                            <tr> 
                                                                                <td valign="top">
                                                                                    <h:commandLink  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}"
                                                                                                    action="#{NavigationHandler.toEuidDetails}" >  
                                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                                    </h:commandLink>                                                                                      

                                                                                </td>                                              
                                                                            </tr>
                                                                            
                                                                        </h:form>
                                                                    </table>
                                                            </div> 
                                                     </td>
                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length) { %>
                                                     <td>                                                                <!--Displaying view sources and view history-->
                                                         <div id="previewActionButton">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form  id="previewlid1Form">
                                                                                    <h:commandLink styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID1"><h:outputText value="Keep LID1"/></span>
                                                                                    </h:commandLink>
                                                                                    <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                                                                                    <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                                                                                </h:form>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <h:form id="previewlid2Form">
                                                                                    <h:commandLink styleClass="button" rendered="#{Operations.SO_Merge}" action="#{SourceMergeHandler.performPreviewLID}">
                                                                                        <span id="LID2"><h:outputText value="Keep LID2"/></span>
                                                                                        <h:inputHidden id="previewhiddenLid2" value="#{SourceMergeHandler.formlids}" />
                                                                                        <h:inputHidden id="previewhiddenLid2source" value="#{SourceMergeHandler.lidsource}" />
                                                                                    </h:commandLink>
                                                                                </h:form>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>  
                                                         <div id="confirmationButton" style="visibility:hidden">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <a class="button" href="javascript:void(0)" onclick="javascript:showLIDDiv('mergeDiv',event)" > 
                                                                                   <span id="confirmok"><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                                </a>
                                                                            </td>
                                                                            <td>
                                                                                <a class="button" >
                                                                                   <span id="confirmcancel"><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>  



                                                     </td>
                                               <%}%>
                                               
                                            <%}%>
                                                   </table>
                                               </div>
                                                </td>
                                           </tr>
                                            </table>
                                            <%}%>
                                </div>
                              <%}%>
                            </div> <!-- End YUI content -->
                        </div> <!-- demo end -->
                    </td>
                </tr>
            </table>
            
        </div> <!-end source records dic -->
         <!-- START Extra divs for add  SO-->
         <div id="mergeDiv" class="alert" style="top:500px;height:130px;left:560px;visibility:hidden">
             <h:form id="finalMergeForm">
                 <table cellspacing="0" cellpadding="0" border="0">
                     <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th>
                     <th align="right"><a href="javascript:void(0)" rel="mergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                      <tr>
                          <th align="center"><h:outputText value="#{msgs.lid_merge_popup_text}"/></th><th style="padding-right:160px;"><div id="confirmContent"></div></th>
                     </tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                     <tr><td colspan="2"> &nbsp;</td></tr>
                    <tr>
                         <td colspan=2>
                             <h:commandLink styleClass="button" 
                                            action="#{SourceMergeHandler.mergePreviewSystemObject}">
                                 <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                             </h:commandLink>   
                             <h:outputLink  onclick="javascript:showExtraDivs('mergeDiv',event)" 
                                            styleClass="button"          
                                            value="javascript:void(0)">
                                 <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                             </h:outputLink>   
                             <h:inputHidden id="previewhiddenLid1" value="#{SourceMergeHandler.formlids}" />
                             <h:inputHidden id="previewhiddenLid1source" value="#{SourceMergeHandler.lidsource}" />
                         </td>
                     </tr>
                     
                 </table>
             </h:form>
         </div>		
	
       <!-- END Extra divs for add SO-->
       <!-- Start Extra divs for editing SO-->
    <div id="extraAddressEditDiv" class="alert"  style="TOP:580px;LEFT:450px;HEIGHT:400px;WIDTH:400px;visibility:hidden;">
        <h:form>
             <table>
                 <tr>
                     <td align="right" colspan="2">
                         <div>
                             <a href="javascript:void(0)" rel="editballoonaddress"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                         </div> 
                     </td>
                  </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="hashAddressEditIdExtra" 
                                       var="fieldConfigEditAddress" 
                                       value="#{SourceHandler.addressFieldConfigs}">
                             <h:column>
                                 <h:outputText value="#{fieldConfigEditAddress.displayName}"  />
                                 <h:outputText value="*" rendered="#{fieldConfigEditAddress.required}" />
                             </h:column>
                             <!--Rendering HTML Select Menu List-->
                             <h:column rendered="#{fieldConfigEditAddress.guiType eq 'MenuList'}" >
                                 <h:selectOneMenu value="#{SourceEditHandler.editSoAddressHashMap[fieldConfigEditAddress.fullFieldName]}">
                                     <f:selectItem itemLabel="" itemValue="" />
                                     <f:selectItems  value="#{fieldConfigEditAddress.selectOptions}"  />
                                 </h:selectOneMenu>
                             </h:column>
                             <!--Rendering Updateable HTML Text boxes-->
                             <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextBox'}" >
                                 <h:inputText label="#{fieldConfigEditAddress.displayName}"  
                                              value="#{SourceEditHandler.editSoAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                              required="#{fieldConfigEditAddress.required}"/>
                             </h:column>                     
                             <!--Rendering Updateable HTML Text Area-->
                             <h:column rendered="#{fieldConfigEditAddress.guiType eq 'TextArea'}" >
                                 <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                  value="#{SourceEditHandler.editSoAddressHashMap[fieldConfigEditAddress.fullFieldName]}" 
                                                  required="#{fieldConfigAddAddress.required}"
                                                  />
                             </h:column>
                             
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <h:commandLink styleClass="button" actionListener="#{SourceEditHandler.addSOAddress}">
                             <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                         </h:commandLink>   
                         
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAddressEditDiv',event)" class="button"> 
                         <span><h:outputText value="#{msgs.cancel_but_text}"/> </span>
                          </a>    
                     </td>
                 </tr>
                 <tr>
                     <td valign="top" colspan="2">
                         <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                         <h:messages  styleClass="errorMessages"  layout="list" />
                         <%}%>
                     </td>
                 </tr>                              
             </table>
             
         </h:form>
     </div>
    <div id="extraPhoneEditDiv" class="alert" style="TOP:1300px;LEFT:500px;HEIGHT:180px;WIDTH:300px;visibility:hidden;">
         <h:form>
             <table>
                 <tr>
                     <td align="right" colspan="2">
                         <div>
                             <a href="javascript:void(0)" rel="editballoonphone"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                        </div>
                         &nbsp;
                      </td>
                 </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="hashPhoneEditIdExtra" 
                                       var="fieldConfigEditPhone" 
                                       value="#{SourceHandler.phoneFieldConfigs}">
                             <h:column>
                                 <h:outputText value="#{fieldConfigEditPhone.displayName}"  />
                                 <h:outputText value="*" rendered="#{fieldConfigEditPhone.required}" />
                             </h:column>
                             <!--Rendering HTML Select Menu List-->
                             <h:column rendered="#{fieldConfigEditPhone.guiType eq 'MenuList'}" >
                                 <h:selectOneMenu value="#{SourceEditHandler.editSoPhoneHashMap[fieldConfigEditPhone.fullFieldName]}">
                                     <f:selectItem itemLabel="" itemValue="" />
                                     <f:selectItems  value="#{fieldConfigEditPhone.selectOptions}"  />
                                 </h:selectOneMenu>
                             </h:column>
                             <!--Rendering Updateable HTML Text boxes-->
                             <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextBox'}" >
                                 <h:inputText label="#{fieldConfigEditPhone.displayName}"  
                                              value="#{SourceEditHandler.editSoPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                              required="#{fieldConfigEditPhone.required}"/>
                             </h:column>                     
                             <!--Rendering Updateable HTML Text Area-->
                             <h:column rendered="#{fieldConfigEditPhone.guiType eq 'TextArea'}" >
                                 <h:inputTextarea label="#{fieldConfigAddPhone.displayName}"  
                                                  value="#{SourceEditHandler.editSoPhoneHashMap[fieldConfigEditPhone.fullFieldName]}" 
                                                  required="#{fieldConfigAddPhone.required}"
                                                  />
                             </h:column>
                             
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <h:commandLink  styleClass="button" 
                                         actionListener="#{SourceEditHandler.addSOPhone}">
                             <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                         </h:commandLink>   
                        
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraPhoneEditDiv',event)" class="button">
                             <span><h:outputText value="#{msgs.cancel_but_text}"/></span></a>    
                     </td>
                 </tr>
                 <tr>
                     <td valign="top" colspan="2">
                         <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                         <h:messages  styleClass="errorMessages"  layout="list" />
                         <%}%>
                     </td>
                 </tr>                              
             </table>
             
         </h:form>
     </div>
    <div id="extraAliasEditDiv" class="alert" style="TOP:1450px;LEFT:500px;HEIGHT:170px;WIDTH:300px;visibility:hidden; ">
         <h:form>
             <table>
                 <tr>
                     <td align="right" colspan="2">
                         <div>
                             <a href="javascript:void(0)" rel="editballoonalias"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                         </div>
                     </td>
                 </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="hashAliasEditIdExtra" 
                                       var="fieldConfigEditAlias" 
                                       value="#{SourceHandler.aliasFieldConfigs}">
                             <h:column>
                                 <h:outputText value="#{fieldConfigEditAlias.displayName}"  />
                                 <h:outputText value="*" rendered="#{fieldConfigEditAlias.required}" />
                             </h:column>
                             <!--Rendering HTML Select Menu List-->
                             <h:column rendered="#{fieldConfigEditAlias.guiType eq 'MenuList'}" >
                                 <h:selectOneMenu value="#{SourceEditHandler.editSoAliasHashMap[fieldConfigEditAlias.fullFieldName]}">
                                     <f:selectItem itemLabel="" itemValue="" />
                                     <f:selectItems  value="#{fieldConfigEditAlias.selectOptions}"  />
                                 </h:selectOneMenu>
                             </h:column>
                             <!--Rendering Updateable HTML Text boxes-->
                             <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextBox'}" >
                                 <h:inputText label="#{fieldConfigEditAlias.displayName}"  
                                              value="#{SourceEditHandler.editSoAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                              required="#{fieldConfigEditAlias.required}"/>
                             </h:column>                     
                             <!--Rendering Updateable HTML Text Area-->
                             <h:column rendered="#{fieldConfigEditAlias.guiType eq 'TextArea'}" >
                                 <h:inputTextarea label="#{fieldConfigAddAlias.displayName}"  
                                                  value="#{SourceEditHandler.editSoAliasHashMap[fieldConfigEditAlias.fullFieldName]}" 
                                                  required="#{fieldConfigAddAlias.required}"
                                                  />
                             </h:column>
                             
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                         <h:commandLink  styleClass="button" 
                                         actionListener="#{SourceEditHandler.addSOAlias}">
                             <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                         </h:commandLink>   
                         
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAliasEditDiv',event)" class="button"> 
                         <span><h:outputText value="#{msgs.cancel_but_text}"/> </span></a>    
                     </td>
                 </tr>
                 <tr>
                     <td valign="top" colspan="2">
                         <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                         <h:messages  styleClass="errorMessages"  layout="list" />
                         <%}%>
                     </td>
                 </tr>                              
             </table>
             
         </h:form>
     </div>

    <!-- End Extra divs for editing SO-->
     <!-- Start Extra divs for add SO-->
<div id="extraAddressAddDiv" class="alert"  style="TOP:1800px;LEFT:700px;HEIGHT:385px;WIDTH:400px;visibility:hidden;">
    <table>
        <tr>
            <td align="right" colspan="2">
                <div>
                    <a href="javascript:void(0)" rel="balloonaddress"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                </div>                               
             </td>
          
           </tr>
        <tr>
            <td colspan="2" align="left">
                <div id="addressInnerDiv">
                    <h:dataTable  headerClass="tablehead" 
                                  id="hashAddressIdExtra" 
                                  var="fieldConfigAddAddress" 
                                  value="#{SourceAddHandler.addressFieldConfigs}">
                        <h:column>
                            <h:outputText value="#{fieldConfigAddAddress.displayName}"  />
                            <h:outputText value="*" rendered="#{fieldConfigAddAddress.required}" />
                        </h:column>
                        <!--Rendering HTML Select Menu List-->
                        <h:column rendered="#{fieldConfigAddAddress.guiType eq 'MenuList' &&  fieldConfigAddAddress.valueType ne 6}" >
                            <h:selectOneMenu value="#{SourceAddHandler.addressFeildsMap[fieldConfigAddAddress.fullFieldName]}">
                                <f:selectItem itemLabel="" itemValue="" />
                                <f:selectItems  value="#{fieldConfigAddAddress.selectOptions}"  />
                            </h:selectOneMenu>
                        </h:column>
                        <!--Rendering Updateable HTML Text boxes-->
                        <h:column rendered="#{fieldConfigAddAddress.updateable && fieldConfigAddAddress.guiType eq 'TextBox' &&  fieldConfigAddAddress.valueType ne 6}" >
                            <h:inputText label="#{fieldConfigAddAddress.displayName}"  
                                         id="fieldConfigIdTextbox"   
                                         value="#{SourceAddHandler.addressFeildsMap[fieldConfigAddAddress.fullFieldName]}" 
                                         required="#{fieldConfigAddAddress.required}"/>
                        </h:column>                     
                        <!--Rendering Updateable HTML Text boxes date fields-->
                        <h:column rendered="#{fieldConfigAddAddress.guiType eq 'TextBox' &&  fieldConfigAddAddress.valueType eq 6}">
                            <h:inputText label="#{fieldConfigAddAddress.displayName}"   
                                         value="#{SourceAddHandler.addressFeildsMap[fieldConfigAddAddress.fullFieldName]}"  
                                      
                                         required="#{fieldConfigAddAddress.required}"
                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                         />
                            <a HREF="javascript:void(0);" 
                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                <h:graphicImage  id="calImgStartDate" 
                                                 alt="calendar Image" styleClass="imgClass"
                                                 url="./images/cal.gif"/>               
                            </a>
                        </h:column>
                        
           
                        <!--Rendering Updateable HTML Text Area-->
                        <h:column rendered="#{fieldConfigAddAddress.guiType eq 'TextArea' &&  fieldConfigAddAddress.valueType ne 6}" >
                            <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                             id="fieldConfigIdTextArea"   
                                             value="#{SourceAddHandler.addressFeildsMap[fieldConfigAddAddress.fullFieldName]}" 
                                             required="#{fieldConfigAddAddress.required}"
                                             />
                        </h:column>
                        
                    </h:dataTable>  
                </div>
            </td>
        </tr>
        <tr>
            <td align="right">
                <a href="javascript:populateExtraDivs('addressInnerDiv','addAddressDiv','extraAddressAddDiv','addAddressDivClose')" class="button">
                    <span><h:outputText value="#{msgs.ok_text_button}"/></span></a>    
            </td>
            <td>
                <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAddressAddDiv',event)" class="button">
                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span> </a>
            </td>
        </tr>
                            
    </table>
</div>
<div id="extraPhoneAddDiv" class="alert"  style="TOP:1900px;LEFT:700px;HEIGHT:170px;WIDTH:300px;visibility:hidden; ">
    <table >
        <tr>
            <td align="right" colspan="2">
                 <div>
                    <a href="javascript:void(0)" rel="addballoonphone"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                </div>                               
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div id="phoneInnerDiv">
                    <h:dataTable  headerClass="tablehead"   
                                  id="hashPhoneIdExtra" 
                                  var="fieldConfigAddPhone" 
                                  value="#{SourceAddHandler.phoneFieldConfigs}">
                        <h:column>
                            <h:outputText value="#{fieldConfigAddPhone.displayName}"  />
                            <h:outputText value="*" rendered="#{fieldConfigAddPhone.required}" />
                        </h:column>
                        <!--Rendering HTML Select Menu List-->
                        <h:column rendered="#{fieldConfigAddPhone.guiType eq 'MenuList' &&  fieldConfigAddPhone.valueType ne 6}" >
                            <h:selectOneMenu value="#{SourceAddHandler.phoneFeildsMap[fieldConfigAddPhone.fullFieldName]}">
                                <f:selectItem itemLabel="" itemValue="" />
                                <f:selectItems  value="#{fieldConfigAddPhone.selectOptions}"  />
                            </h:selectOneMenu>
                        </h:column>
                        <!--Rendering Updateable HTML Text boxes-->
                        <h:column rendered="#{fieldConfigAddPhone.updateable && fieldConfigAddPhone.guiType eq 'TextBox' &&  fieldConfigAddPhone.valueType ne 6}" >
                            <h:inputText label="#{fieldConfigAddPhone.displayName}"  
                                         id="fieldConfigIdTextbox"   
                                         value="#{SourceAddHandler.phoneFeildsMap[fieldConfigAddPhone.fullFieldName]}" 
                                         required="#{fieldConfigAddPhone.required}"/>
                        </h:column>                     
                        <!--Rendering Updateable HTML Text boxes date fields-->
                        <h:column rendered="#{fieldConfigAddPhone.guiType eq 'TextBox' &&  fieldConfigAddPhone.valueType eq 6}">
                            <h:inputText label="#{fieldConfigAddPhone.displayName}"   
                                         value="#{SourceAddHandler.phoneFeildsMap[fieldConfigAddPhone.fullFieldName]}"  
                                       
                                         required="#{fieldConfigAddPhone.required}"
                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                         />
                            <a HREF="javascript:void(0);" 
                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                <h:graphicImage  id="calImgStartDate" 
                                                 alt="calendar Image" styleClass="imgClass"
                                                 url="./images/cal.gif"/>               
                            </a>
                        </h:column>
                        
           
                        <!--Rendering Updateable HTML Text Area-->
                        <h:column rendered="#{fieldConfigAddPhone.guiType eq 'TextArea' &&  fieldConfigAddPhone.valueType ne 6}" >
                            <h:inputTextarea label="#{fieldConfigAddPhone.displayName}"  
                                             id="fieldConfigIdTextArea"   
                                             value="#{SourceAddHandler.phoneFeildsMap[fieldConfigAddPhone.fullFieldName]}" 
                                             required="#{fieldConfigAddPhone.required}"
                                             />
                        </h:column>
                        
                    </h:dataTable>  
                </div>
            </td>
        </tr>
        <tr>
            <td align="right">
                <a href="javascript:populateExtraDivs('phoneInnerDiv','addPhoneDiv','extraPhoneAddDiv','addPhoneDivClose')" class="button">
                    <span><h:outputText value="#{msgs.ok_text_button}"/></span></a>    
            </td>
            <td>
                <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraPhoneAddDiv',event)" class="button"> 
                <span><h:outputText value="#{msgs.cancel_but_text}"/></span> </a>
            </td>
        </tr>
                           
    </table>
</div>
<div id="extraAliasAddDiv" class="alert" style="TOP:2000px;LEFT:700px;HEIGHT:170px;WIDTH:300px;visibility:hidden;">
    <table>
        <tr>
            <td align="right" colspan="2">
                <div>
                    <a href="javascript:void(0)" rel="addballoonalias"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                </div>                               
             </td>
        </tr>
        <tr>
            <td colspan="2">
                <div id="aliasInnerDiv">
                    <h:dataTable  headerClass="tablehead" 
                                  id="hashAliasIdExtra" 
                                  var="fieldConfigAddAlias" 
                                  value="#{SourceAddHandler.aliasFieldConfigs}">
                        <h:column>
                            <h:outputText value="#{fieldConfigAddAlias.displayName}"  />
                            <h:outputText value="*" rendered="#{fieldConfigAddAlias.required}" />
                        </h:column>
                        <!--Rendering HTML Select Menu List-->
                        <h:column rendered="#{fieldConfigAddAlias.guiType eq 'MenuList' &&  fieldConfigAddAlias.valueType ne 6}" >
                            <h:selectOneMenu value="#{SourceAddHandler.aliasFeildsMap[fieldConfigAddAlias.fullFieldName]}">
                                <f:selectItem itemLabel="" itemValue="" />
                                <f:selectItems  value="#{fieldConfigAddAlias.selectOptions}"  />
                            </h:selectOneMenu>
                        </h:column>
                        <!--Rendering Updateable HTML Text boxes-->
                        <h:column rendered="#{ fieldConfigAddAlias.guiType eq 'TextBox' &&  fieldConfigAddAlias.valueType ne 6}" >
                            <h:inputText label="#{fieldConfigAddAlias.displayName}"  
                                         id="fieldConfigIdTextbox"   
                                         value="#{SourceAddHandler.aliasFeildsMap[fieldConfigAddAlias.fullFieldName]}" 
                                         required="#{fieldConfigAddAlias.required}"/>
                        </h:column>                     
                        <!--Rendering Updateable HTML Text boxes date fields-->
                        <h:column rendered="#{fieldConfigAddAlias.guiType eq 'TextBox' &&  fieldConfigAddAlias.valueType eq 6}">
                            <h:inputText label="#{fieldConfigAddAlias.displayName}"   
                                         value="#{SourceAddHandler.aliasFeildsMap[fieldConfigAddAlias.fullFieldName]}"  
                                         
                                         required="#{fieldConfigAddAlias.required}"
                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                         />
                            <a HREF="javascript:void(0);" 
                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                <h:graphicImage  id="calImgStartDate" 
                                                 alt="calendar Image" styleClass="imgClass"
                                                 url="./images/cal.gif"/>               
                            </a>
                        </h:column>
                        
           
                        <!--Rendering Updateable HTML Text Area-->
                        <h:column rendered="#{fieldConfigAddAlias.guiType eq 'TextArea' &&  fieldConfigAddAlias.valueType ne 6}" >
                            <h:inputTextarea label="#{fieldConfigAddAlias.displayName}"  
                                             id="fieldConfigIdTextArea"   
                                             value="#{SourceAddHandler.aliasFeildsMap[fieldConfigAddAlias.fullFieldName]}" 
                                             required="#{fieldConfigAddAlias.required}"
                                             />
                        </h:column>
                        
                    </h:dataTable>  
                </div>
            </td>
        </tr>
        <tr>
            <td align="right">
                <a href="javascript:populateExtraDivs('aliasInnerDiv','addAliasDiv','extraAliasAddDiv','addAliasDivClose')" class="button">
                    <span><h:outputText value="#{msgs.ok_text_button}"/></span></a>    
            </td>
            <td>
                <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extraAliasAddDiv',event)" class="button"> <span><h:outputText value="#{msgs.cancel_but_text}"/> </span></a>
            </td>
        </tr>
                            
    </table>
</div>
 <!-- End Extra divs for add SO-->                                                                                                                                       
     <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
                                             
<script>
(function() {
    var tabView = new YAHOO.widget.TabView('demo');

    YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
})();
</script>

<!--END SOURCE CODE FOR EXAMPLE =============================== -->
</div>
    
 <div id="balloonaddress" class="balloonstyle"><h:outputText  value="#{msgs.add_address_help_text}"/></div>
 <div id="addballoonphone" class="balloonstyle"><h:outputText  value="#{msgs.add_phone_help_text}"/></div>
 <div id="addballoonalias" class="balloonstyle"><h:outputText  value="#{msgs.add_alias_help_text}"/></div>
 <div id="addballoonaux" class="balloonstyle"><h:outputText  value="#{msgs.add_aux_help_text}"/></div>
 <div id="addballooncomment" class="balloonstyle"><h:outputText  value="#{msgs.add_comment_help_text}"/></div>
 <div id="editballoonaddress" class="balloonstyle"><h:outputText  value="#{msgs.edit_address_help_text}"/></div>
 <div id="editballoonphone" class="balloonstyle"><h:outputText  value="#{msgs.edit_phone_help_text}"/></div>
 <div id="editballoonalias" class="balloonstyle"><h:outputText  value="#{msgs.edit_alias_help_text}"/></div>
 <div id="editballoonaux" class="balloonstyle"><h:outputText  value="#{msgs.edit_aux_help_text}"/></div>
 <div id="editballooncomment" class="balloonstyle"><h:outputText  value="#{msgs.edit_comment_help_text}"/></div>

        <%if( request.getAttribute("lids") != null) {
           
        String[] srcs  = (String[]) request.getAttribute("lids");
        String  lidsSource  = (String) request.getAttribute("lidsource");
        System.out.println(">>>>>>>>>>>>>>>>>>>" + lidsSource);
        for(int i=0;i<srcs.length;i++) {
        %>    
        
        <script>
            collectLid('<%=srcs[i]%>'); 
            document.getElementById('confirmationButton').style.visibility = 'visible';
            document.getElementById("previewActionButton").style.visibility = "hidden";
            document.getElementById("previewActionButton").style.display = "none";                        
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "blue";
        </script>
        <%}%>
        <script>
            document.getElementById("confirmContent").innerHTML  = '<%=srcs[0]%>';
            document.getElementById("finalMergeForm:previewhiddenLid1").value  = '<%=srcs[0]+":" + srcs[1]%>';
            document.getElementById("finalMergeForm:previewhiddenLid1source").value  = '<%=lidsSource%>';
        </script>
        <%}%> 


</body>

        <%
          String[][] lidMaskingArray = sourceAddHandler.getAllSystemCodes();
          
          
        %>
        <script>
            var systemCodes = new Array();
            var lidMasks = new Array();
        </script>
        
        <%
        for(int i=0;i<lidMaskingArray.length;i++) {
            String[] innerArray = lidMaskingArray[i];
            for(int j=0;j<innerArray.length;j++) {
            
            if(i==0) {
         %>       
         <script>
           systemCodes['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>      
         <%       
            } else {
         %>
         <script>
           lidMasks ['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>
         <%       
            }
           }
           }
        %>
    <script>
        function setLidMaskValue(field,formName) {
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
    </script>
    <script>
          var formName ="";
    </script>
    <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicAddformData";
    </script>
    
    <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicMergeformData";
     </script>
      
    <%} else {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%}%>  
    
   <script>
         if( formName.elements[0]!=null) {
		var i;
		var max = formName.length;
		for( i = 0; i < max; i++ ) {
			if( formName.elements[ i ].type != "hidden" &&
				!formName.elements[ i ].disabled &&
				!formName.elements[ i ].readOnly ) {
				formName.elements[ i ].focus();
				break;
			}
		}
      }         
       
       
   </script>



</html>
</f:view>
