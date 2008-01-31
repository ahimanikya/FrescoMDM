<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
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
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>


<f:view>
    
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
        </head>
        <body>
            <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden">   
                    <div id="advancedSearch" class="basicSearch" style="visibility:visible;display:block">
                    <h:form id="advancedformData">
                            <input type="hidden" name="ssnmask" value="DDD-DDD-DDD" />
                            <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <h:outputText value="#{SearchDuplicatesHandler.searchType}" />
                                    <h:dataTable headerClass="tablehead"  id="fieldConfigId" var="feildConfig" value="#{SearchDuplicatesHandler.screenConfigArray}">
                                        <!--Rendering Non Updateable HTML Text Area-->
                                        <h:column>
                                            <h:outputText value="#{feildConfig.displayName}" />
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text boxes-->
                                        <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                                            <h:inputText label="#{feildConfig.displayName}"   id="fieldConfigIdText" 
                                                         value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}" 
                                                         required="#{feildConfig.required}" rendered="#{feildConfig.name ne 'SSN'}"/>
                                            
                                            <h:inputText id="SSN"  required="#{feildConfig.required}" label="#{feildConfig.displayName}" 
                                                                          onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.ssnmask.value)"
                                                                          onkeyup="javascript:qws_field_on_key_up(this)"
                                                                          value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                                          rendered="#{feildConfig.name eq 'SSN'}"  maxlength="12" />
             
                                        </h:column>
                                        

                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                                            <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                        </h:column>
                                        
                                        
                                        <!--Rendering Non Updateable HTML Text boxes-->
                                        <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                            <h:inputText id="SystemCode" value="#{SearchDuplicatesHandler.SystemCode}" required="#{feildConfig.required}"/>
                                        </h:column>
                                        
                                        <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                            <h:inputText label="#{feildConfig.displayName}"    id="EUID"
                                                         value="#{SearchDuplicatesHandler.EUID}"  maxlength="10" required="#{feildConfig.required}"/>
                                        </h:column>
                                        
                                        <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                            <h:inputText label="#{feildConfig.displayName}"    id="LID" value="#{SearchDuplicatesHandler.LID}" required="#{feildConfig.required}"
                                                         onkeydown="javascript:qws_field_on_key_down(this,'DDD-DDD-DDDD')"
                                                         onkeyup="javascript:qws_field_on_key_up(this)"  maxlength="12"/>
                                        </h:column>
                                        <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                            <h:inputText label="#{feildConfig.displayName}"    value="#{SearchDuplicatesHandler.create_start_date}"  id="create_start_date"
                                                         required="#{feildConfig.required}"  maxlength="10"
                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                               <script> var startdate = getDateFieldName('advancedformData',':create_start_date');</script>
                                            <a HREF="javascript:void(0);" 
                                               onclick="g_Calendar.show(event,startdate)" > 
                                                <h:graphicImage  id="calImgStartDate" 
                                                                 alt="calendar Image" styleClass="imgClass"
                                                                 url="./images/cal.gif"/>               
                                            </a>
                                        </h:column>
                                        <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                            <h:inputText label="#{feildConfig.displayName}"    value="#{SearchDuplicatesHandler.create_end_date}" id="create_end_date" 
                                                         required="#{feildConfig.required}"  maxlength="10"
                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                              <script> var enddate = getDateFieldName('advancedformData','create_end_date');</script>
                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, enddate)" > 
                                                <h:graphicImage  id="calImgEndDate" 
                                                                 alt="calendar Image" styleClass="imgClass"
                                                                 url="./images/cal.gif"/>               
                                            </a>
                                        </h:column>
                                        <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                            <h:inputText label="#{feildConfig.displayName}"    rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                         value="#{SearchDuplicatesHandler.create_start_time}" required="#{feildConfig.required}"
                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                         onkeyup="javascript:qws_field_on_key_up(this)"  maxlength="8"/>
                                        </h:column>
                                        
                                        <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                            <h:inputText label="#{feildConfig.displayName}"    id="create_end_time" value="#{SearchDuplicatesHandler.create_end_time}" 
                                                         required="#{feildConfig.required}"  maxlength="8"
                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                         onkeyup="javascript:qws_field_on_key_up(this)"/>
                                        </h:column>
                                        
                                        <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                            <h:inputText label="#{feildConfig.displayName}"    id="Status"  value="#{SearchDuplicatesHandler.Status}" required="#{feildConfig.required}"/>
                                        </h:column>
                                          <f:facet name="footer">
                                              
                                            <h:column>
                                                <nobr>
                                                <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                    <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                </h:outputLink>

                                                <h:commandLink  styleClass="button" rendered="#{Operations.potDup_SearchView}"  action="#{SearchDuplicatesHandler.performSubmit}" >  
                                                    <span>
                                                        <h:outputText value="#{msgs.search_button_label}"/>
                                                    </span>
                                                    <f:attribute name="searchType" value="Advanced Search"/>   
                                                </h:commandLink>                                     

                                                <h:commandLink  styleClass="button" rendered="#{Operations.potDup_SearchView && SearchDuplicatesHandler.searchType eq 'Advanced Search'}" actionListener="#{SearchDuplicatesHandler.setSearchTypeAction}" >  
                                                    <span>
                                                        <img src="./images/up-chevron-button.png" border="0" alt="Basic search"/>
                                                           <h:outputLabel for="#{msgs.basic_search_text}" value="#{msgs.basic_search_text}"/> 
                                                        <img src="./images/up-chevron-button.png" border="0" alt=""/>
                                                    </span>
                                                    <f:attribute name="searchType" value="Basic Search"/>   
                                                </h:commandLink>

                                                <h:commandLink  styleClass="button" rendered="#{Operations.potDup_SearchView && SearchDuplicatesHandler.searchType eq 'Basic Search'}" actionListener="#{SearchDuplicatesHandler.setSearchTypeAction}" >  
                                                    <span>
                                                        <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>                                                        
                                                        <h:outputLabel for="#{msgs.Advanced_search_text}" value="#{msgs.Advanced_search_text}"/>
                                                        <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>                                                        
                                                    </span>
                                                    <f:attribute name="searchType" value="Advanced Search"/>   
                                                </h:commandLink>
                                                </nobr>
                                            </h:column>
                                        
                                        </f:facet>
                                        
                                    </h:dataTable>
                                </td>
                                <td valign="top">
                                    <h:messages styleClass="errorMessages"  layout="list" />
                                </td>
                            </tr>
                        </table>  
                    </h:form>
                   </div>  
                  <%
                    ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                    
                    EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
                    EPath ePath = null;
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
                    
                    
                    PotentialDuplicateIterator pdPageIter = (PotentialDuplicateIterator) session.getAttribute("pdPageIter");
                    ArrayList finalArrayList = (ArrayList) session.getAttribute("finalArrayList");
                    PotentialDuplicateIterator asPdIter;
                    PotentialDuplicateSummary mainDuplicateSummary = null;
                    
                    PotentialDuplicateSummary associateDuplicateSummary = null;
                    int countMain = 0;
                    int totalMainDuplicates = 0;
                    int totalAssoDuplicates = 0;
                    String mainDob = null;
                    String assoDob = null;
                    
                    String mainEuidContentDiv = null ;
                    String assoEuidContentDiv = null ;
                    String previewEuidContentDiv = null ;
                    String dupHeading = "<b>Duplicate </b>";
                    SearchDuplicatesHandler searchDuplicatesHandler = new  SearchDuplicatesHandler(); 
                    Iterator searchResultFieldsIter = searchDuplicatesHandler.getResultsConfigArray().iterator();
                    Object[] resultsConfigFeilds  = searchDuplicatesHandler.getResultsConfigArray().toArray();
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer mainEUID = new StringBuffer();
                    StringBuffer dupEUID = new StringBuffer();
                    
                    %>                
                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {
                 %>                
                     <div class="printClass">
                       <table cellpadding="0" cellspacing="0" border="0">
                         <tr>
                             <td>
                                 <h:outputText value="#{msgs.total_records_text}"/><%=finalArrayList.size()%>&nbsp;&nbsp;
                             </td>
                             <td>
                                  <h:outputLink styleClass="button" 
                                                rendered="#{Operations.potDup_Print}"  
                                                value="JavaScript:window.print();">
                                      <span><h:outputText value="#{msgs.print_text}"/>  </span>
                                 </h:outputLink>              
                             </td>
                         </tr>
                       </table>
                    </div>
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {
                %>
                     <div id="dataDiv" class="compareResults" style="overflow:auto;width:1024px;height:1024px;">
                    <div>
                        <table>
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <td  valign="top">
                                            <div id="staticContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                </table>
                                            </div>     
                                            <div id="staticContent">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w114">
                                                    <%
                                                    String mainDOBSource;
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                    %>
                                                    <tr>
                                                        <td class="fntdup4"><%=fieldConfig.getDisplayName()%></td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </div>
                                        </td>
                                        <%
                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate</b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               String strDataArray = (String) arlInner.get(j);
                                               EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;

                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent" class="w169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                            <a href="#" class="dupbtn">
                                                                <%=strDataArray%>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                        
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr>
                                                        <td>
                                                            
                                                            <%if (fieldValuesMapSource.get(fieldConfigMap.getFullFieldName()) != null) {%>
                                                            <%if (fieldConfigMap.getValueType() == 6) {
                                                             mainDOBSource = simpleDateFormatFields.format(fieldValuesMapSource.get(fieldConfigMap.getFullFieldName()));
                                                            %>
                                                            <%=mainDOBSource%>
                                                            <%} else {%>
                                                            <%=fieldValuesMapSource.get(fieldConfigMap.getFullFieldName())%>
                                                            <%}%>
                                                            
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>
                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </div>   
                                        </td>
                                        <%} else {%> <!--For duplicates here-->  

                                            <%if (j ==1 && arlInner.size() > 3 ) { %>
                                            <!--Sri-->
                                            <td>
                                                 <div style="overflow:auto;width:507px;overflow-y:hidden;">
                                                     <table>
                                                         <tr>
                                            <%}%>
                                        
                                           <td valign="top">
                                            <div id="mainEuidContent" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%></td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                            <a href="#" class="dupbtn">
                                                                <%=strDataArray%>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr>
                                                        <td>
                                                            
                                                            <%if (fieldValuesMapSource.get(fieldConfigMap.getFullFieldName()) != null) {%>
                                                            <%if (fieldConfigMap.getValueType() == 6) {
                                                             mainDOBSource = simpleDateFormatFields.format(fieldValuesMapSource.get(fieldConfigMap.getFullFieldName()));
                                                            %>
                                                            <%=mainDOBSource%>
                                                            <%} else {%>
                                                            <%=fieldValuesMapSource.get(fieldConfigMap.getFullFieldName())%>
                                                            <%}%>
                                                            
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>
                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </div>   
                                        </td>
                                            <%if (arlInner.size() > 3 && j == (arlInner.size()-1 )) { %>
                                            <!--Raj-->
                                                       </tr>
                                                     </table>
                                                 </div>
                                                </td>
                                            <%}%>
                                        
                                        <%}%>
                                        <td class="w7yelbg">&nbsp;</td><!--Separator b/n columns-->
                                        <%}%>
                                      <td class="w169" valign="top">
                                      <div id="previewEuidDiv<%=countMain%>" style="visibility:visible;display:block;">
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" class="w169">
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
                                          </table>
                                      </div>
                                     <div id="previewEuidContentDiv<%=countMain%>"   class="dynaw169">
                                        <div id="showCompareButtonDiv<%=countMain%>" style="visibility:visible;">
                                            <table border="0" cellspacing="0" cellpadding="0" class="w169"> 
                                                <tr>
                                                        <td>&nbsp;</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length - 1; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                    %>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="right">
                                                        <!--Show compare duplicates button-->
                                                        <%
                                                        String searchTitle = "Simple Person Lookup";
                                                        ValueExpression euidVaueExpressionList = ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
                                                        ValueExpression searchTitleVaueExpression = ExpressionFactory.newInstance().createValueExpression(searchTitle, searchTitle.getClass());
                                                        %>
                                                         <h:form>
                                                            <h:commandLink styleClass="downlink"  
                                                                           action="#{NavigationHandler.toCompareDuplicates}" 
                                                                           actionListener="#{SearchDuplicatesHandler.buildEuidsAction}">
                                                                <f:attribute name="euids"  value="<%=euidVaueExpressionList%>"  />
                                                                <f:attribute name="searchTitle"  value="<%=searchTitleVaueExpression%>"  />
                                                            </h:commandLink>
                                                        </h:form>   

                                                    </td> 
                                                </tr>
                                            </table>
                                        </div>
                                      </div>  
                                    </div>
                                    
                           </tr>
                        </table>
                    </div> 
                    <div id="separator"  class="sep"></div>
                         <%}%> <!--final Array list count loop -->
                         <%}%> <!-- final Array list  condition in session-->
               </div> 
               <%}%>  
               <%
                   if (finalArrayList != null && finalArrayList.size() == 0) {
               %>
               <div class="printClass">
                       <table cellpadding="0" cellspacing="0" border="0">
                         <tr>
                             <td>
                                 <h:outputText value="#{msgs.total_records_text}"/><%=finalArrayList.size()%>&nbsp;
                             </td>
                         </tr>
                       </table>
               </div>
               <%}%>
               
            </div>
        </body>
    </html> 
</f:view>