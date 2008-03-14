<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchCriteria"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchOptions"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathAPI"%>
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
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            
<!--CSS file (default YUI Sam Skin) -->
            <link  type="text/css" rel="stylesheet" href="./css/yui/datatable/assets/skins/sam/datatable.css">
            <!-- Dependencies -->
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/datasource/datasource-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/json/json-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/calendar/calendar-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/connection/connection-min.js"></script>
            <!-- Source files -->
            <script type="text/javascript" src="./scripts/yui/datatable/datatable-beta-min.js"></script>
</head>
<title><h:outputText value="#{msgs.application_heading}"/></title>  
<body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
        
    <div id="mainContent" style="overflow:hidden;">
        <div id="advancedSearch" class="basicSearch" style="visibility:visible;display:block">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{PatientDetailsHandler.searchType}" valueChangeListener="#{PatientDetailsHandler.changeSearchType}" >
                                        <f:selectItems  value="#{PatientDetailsHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{PatientDetailsHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td>
                            <input id='lidmask' type='hidden' name='lidmask' value='' />
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{PatientDetailsHandler.screenConfigArray}">
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
                                        <h:selectOneMenu value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                         onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                         rendered="#{feildConfig.name ne 'SystemCode'}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                        
                                        <h:selectOneMenu  onchange="javascript:setLidMaskValue(this)"
                                                          onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                          id="SystemCode" 
                                                          value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}" 
                                                          rendered="#{feildConfig.name eq 'SystemCode'}">
                                            <f:selectItem itemLabel="" itemValue="" />
                                            <f:selectItems  value="#{feildConfig.selectOptions}" />
                                        </h:selectOneMenu>
                                    </nobr>
                                </h:column>
                                <!--Rendering Updateable HTML Text boxes-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6}" >
                                    <nobr>
                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.fullFieldName}')"
                                                       value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{feildConfig.maxLength}" 
                                                       rendered="#{feildConfig.name ne 'LID'}"/>
                                        
                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                       maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                       rendered="#{feildConfig.name eq 'LID'}"/>
                                                       
                                    </nobr>
                                </h:column>
                                
                                <!--Rendering Updateable HTML Text Area-->
                                <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                    <nobr>
                                        <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                    </nobr>
                                </h:column>
                                
                                <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                    <nobr>
                                        <h:inputText label="#{feildConfig.displayName}"    
                                                     value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.displayName]}"
                                                     required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                     onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateFieldsOnBlur(this,'#{feildConfig.displayName}')"
                                                     onkeyup="javascript:qws_field_on_key_up(this)" />
                                        <script> var dateFrom =  getDateFieldName('advancedformData','DOBFrom');</script>
                                        <a HREF="javascript:void(0);" 
                                           onclick="g_Calendar.show(event,dateFrom)" > 
                                            <h:graphicImage  id="calImgDateFrom" 
                                                             alt="calendar Image" styleClass="imgClass"
                                                             url="./images/cal.gif"/>               
                                        </a>
                                    </nobr>
                                </h:column>
                                
                             
                            </h:dataTable>
                            <table border="0" cellpadding="0" cellspacing="0" >
                                <tr>
                                    <td>
                                        <nobr>
                                            <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                            </h:outputLink>
                                        </nobr>
                                        <nobr>
                                            <h:commandLink  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}"  action="#{PatientDetailsHandler.performSubmit}" >  
                                                <span>
                                                    <h:outputText value="#{msgs.search_button_label}"/>
                                                </span>
                                            </h:commandLink>                                     
                                        </nobr>
                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td valign="top">
                            <h:messages styleClass="errorMessages"  layout="list" />
                        </td>
                    </tr>
                </table>
                <h:inputHidden id="enteredFieldValues" value="#{PatientDetailsHandler.enteredFieldValues}"/>
            </h:form>
        </div>  
        <br>    
                
        <%
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            ArrayList arlResultsConfig = objScreenObject.getSearchResultsConfig();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");


        %>    
          
    <%
          ArrayList resultArrayList  = new ArrayList();
          if(request.getAttribute("resultArrayListReq") != null) {
             request.setAttribute("resultArrayListReq", request.getAttribute("resultArrayListReq") );  
             resultArrayList = (ArrayList) request.getAttribute("resultArrayListReq"); 
             if (resultArrayList != null && resultArrayList.size() > 0) {
         %>                
         <div class="printClass">
             <table cellpadding="0" cellspacing="0" border="0" align="right">
                 <tr>
                     <td>
                         <h:outputText value="#{msgs.total_records_text}"/><%=resultArrayList.size()%>&nbsp;&nbsp;
                     </td>
                     <td>
                         <h:outputLink styleClass="button" 
                                       rendered="#{Operations.EO_PrintSBR }"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>              
                         
                     </td>
                 </tr>
             </table>
             <table align="left">
                 <tr>
                     <td align="right">
                         <h:form id="yuiform">
                             <h:commandLink  styleClass="button" 
                                             rendered="#{Operations.EO_Compare}"  
                                             action="#{PatientDetailsHandler.buildCompareEuids}" >  
                                 <span>
                                     <h:outputText value="#{msgs.dashboard_compare_tab_button}"/>
                                 </span>
                             </h:commandLink>                                     
                             <h:inputHidden id="compareEuids" value="#{PatientDetailsHandler.compareEuids}"/>
                         </h:form> 
                         
                     </td>
                 </tr>
             </table>                     
         </div>
         <%}%>
                
                <%
              if (resultArrayList != null && resultArrayList.size() == 0) {
           %>
                <div class="printClass">
                <table>
                  <tr><td>No Records Found in the System. Please Refine your Search Criteria..............</td></tr>
                </table>                     
         </div>
      <%}%>
<%}%>
    
  <%    if (resultArrayList != null && resultArrayList.size() > 0) {
  %>           
             <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
  <%}%>

</div>
</body>
        <%
          PatientDetailsHandler  patientDetailsHandler = new PatientDetailsHandler();
          String[][] lidMaskingArray = patientDetailsHandler.getAllSystemCodes();
          
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
        function setLidMaskValue(field) {
            var  selectedValue = field.options[field.selectedIndex].value;
            document.advancedformData.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
         var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
         document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
                  

	if( document.advancedformData.elements[0]!=null) {
		var i;
		var max = document.advancedformData.length;
		for( i = 0; i < max; i++ ) {
			if( document.advancedformData.elements[ i ].type != "hidden" &&
				!document.advancedformData.elements[ i ].disabled &&
				!document.advancedformData.elements[ i ].readOnly ) {
				document.advancedformData.elements[ i ].focus();
				break;
			}
		}
     }         
    </script>
     



    <%
    ArrayList fcArrayList  = patientDetailsHandler.getResultsConfigArray();
     
    %>

 
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int maxRecords = searchResultsConfig.getMaxRecords();
        int pageSize = searchResultsConfig.getPageSize();
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
            if(keys[i].equalsIgnoreCase("EUID")) {
              value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ","+
                       "formatter:function (elCell,oRecord,oColumn,oData) {elCell.innerHTML = '<input type=\"checkbox\" onclick=\"javascript:getEUIDS(' + oData + ')\"/> &nbsp; <a href=\"euiddetails.jsf?euid=' + oData + '\">' + oData + '</a>';}" +
                       ",sortable:true,resizeable:true}";
            } else {
              value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
            }  
            
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (resultArrayList != null && resultArrayList.size() > 0) {
                for (int i = 0; i < resultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) resultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != resultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
</html>
</f:view>
