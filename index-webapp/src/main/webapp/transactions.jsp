<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>

<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionIterator" %>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSearchObject"%>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSummary"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.TransactionHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>

<f:view>
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />    
    <html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">     
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/balloontip.js"></script>
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
    <%@include file="./templates/header.jsp"%>
    <body class="yui-skin-sam">
        <div id="mainContent">     
            <div id ="transactions " class="basicSearch">
                    <h:form id="advancedformData">
                        <table border="0" cellpadding="0" cellspacing="0" align="right">
                            <tr>
                                <td>
                                    <h:outputText rendered="#{TransactionHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu  rendered="#{TransactionHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{TransactionHandler.searchType}" valueChangeListener="#{TransactionHandler.changeSearchType}" >
                                        <f:selectItems  value="#{TransactionHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                        </table>
                        <table border="0" cellpadding="0" cellspacing="0" >
                            <tr>
                                <td>
                                    <input id='lidmask' type='hidden' name='lidmask' value='<h:outputText value="#{TransactionHandler.lidMask}"/>' />
                                    <h:dataTable headerClass="tablehead"  
                                                 id="fieldConfigId" 
                                                 var="feildConfig" 
                                                 value="#{TransactionHandler.screenConfigArray}">
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
                                            <h:selectOneMenu onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                             rendered="#{feildConfig.name ne 'SystemCode'}">
                                                <f:selectItem itemLabel="" itemValue="" />
                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                            </h:selectOneMenu>
                                            
                                            <h:selectOneMenu  id="sourceOption" 
                                                              onblur="javascript:accumilateSelectFieldsOnBlur(this,'#{feildConfig.name}')"
                                                              onchange="javascript:setLidMaskValue(this)"
                                                              rendered="#{feildConfig.name eq 'SystemCode'}">
                                                <f:selectItem itemLabel="" itemValue="" />
                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                            </h:selectOneMenu>
                                          </nobr>
                                        </h:column>
                                        <!--Rendering Updateable HTML Text boxes-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 }" >
                                          <nobr>
                                            <h:inputText   required="#{feildConfig.required}" 
                                                           label="#{feildConfig.displayName}" 
                                                           onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                           onkeyup="javascript:qws_field_on_key_up(this)"
                                                           onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                           maxlength="#{feildConfig.maxLength}" 
                                                           rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                            
                                            <h:inputText   required="#{feildConfig.required}" 
                                                           label="#{feildConfig.displayName}" 
                                                           onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                           onkeyup="javascript:qws_field_on_key_up(this)"
                                                           onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value);javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                           maxlength="#{SourceMergeHandler.lidMaskLength}" 
                                                           rendered="#{feildConfig.name eq 'LID'}"/>

                                        <h:inputText   required="#{feildConfig.required}" 
                                                       label="#{feildConfig.displayName}" 
                                                       onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                       maxlength="#{SourceHandler.euidLength}" 
                                                       rendered="#{feildConfig.name eq 'EUID'}"/>
                                                           
                                            
                                          </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                          <nobr>
                                            <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                          </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6 }" >
                                          <nobr>
                                            <h:inputText label="#{feildConfig.displayName}"  required="#{feildConfig.required}"  maxlength="#{feildConfig.maxLength}"
                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                         onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"/>

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
                                                        <h:commandLink  styleClass="button" rendered="#{Operations.transLog_SearchView}"  action="#{TransactionHandler.performSubmit}" >  
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
                        <h:inputHidden id="enteredFieldValues" value="#{TransactionHandler.enteredFieldValues}"/>
                    </h:form>
            </div>                           
        </div>
        
         <div class="printClass">
             <h:panelGrid rendered="#{TransactionHandler.searchSize gt 0}" columns="3">
                                <h:outputText  rendered="#{TransactionHandler.searchSize gt -1}" value="#{msgs.total_records_text}"/>
                                <h:outputText rendered="#{TransactionHandler.searchSize gt -1}" value="#{TransactionHandler.searchSize}"/>
                                <h:outputLink value="javascript:window.print()" styleClass="button" rendered="#{Operations.transLog_Print && TransactionHandler.searchSize gt 0}">
                                    <span><h:outputText value="#{msgs.print_text}"/></span>
                                </h:outputLink>
            </h:panelGrid>    
        </div>

            <% if(request.getAttribute("resultsArrayList")!=null &&  ((ArrayList)request.getAttribute("resultsArrayList")).size() > 0 ) {%>           
             <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
            <%}%>
        
        
        
        
        <div id="InvalidTransaction" class="balloonstyle"><h:outputText  value="#{msgs.invalid_transaction}"/></div>
   </body>   
        <%
           TransactionHandler transactionHandler = new TransactionHandler();
           String[][] lidMaskingArray = transactionHandler.getAllSystemCodes();
          
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
         //var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
         //document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
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
     ArrayList resultsArrayList = (ArrayList) request.getAttribute("resultsArrayList");
     //System.out.println("resultsArrayList" + resultsArrayList);
     ArrayList keyList  = transactionHandler.getKeysList();
     ArrayList labelsList  = transactionHandler.getLabelsList();
    %>

 
        <script>
            var fieldsArray = new Array();
        </script>
        <% 
        for(int i=0;i<keyList.size();i++) {
            String key = (String)keyList.get(i);
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=key%>';
        </script>
        <%}%>
        
        <%
        SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int maxRecords = searchResultsConfig.getMaxRecords();
        int pageSize = searchResultsConfig.getPageSize();
        
        String[] keys = new String[keyList.size()];
        for(int i=0;i<keyList.size();i++) {
            keys[i] = (String)keyList.get(i);
        }

        String[] labels = new String[labelsList.size()];
        for(int i=0;i<labelsList.size();i++) {
            labels[i] = (String)labelsList.get(i);
        }
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        //      <a href='transeuiddetails.jsf?transactionId=<h:outputText value="#{transactions.transactionId}"/>&function=<h:outputText value="#{transactions.function}"/>'>

        for(int i=0;i<keys.length;i++) {
            if(keys[i].equalsIgnoreCase("TransactionNumber")) {
              value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ","+
                       "formatter:function (elCell,oRecord,oColumn,oData) {elCell.innerHTML = '<a href=\"transeuiddetails.jsf?transactionId=' + oData +'&function='+ oRecord.getData(\'Function\')  + '\">' + oData + '</a>';}" +
                       ",sortable:true,resizeable:true}";
            } else {
              value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
            }  
            
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");
         %>       
        
        
       
 
<script>
     var dataArray = new Array();
     dataArray = <%=resultsArrayList%>;

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
