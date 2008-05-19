<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.HashMap"  %>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>
<f:view>    
<f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.midm" var="msgs" />
<html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
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
<script>
//not used in transactions, but since its require in the script we fake it
var editIndexid = ""; 
var thisForm;
var rand = "";
function setRand(thisrand)  {
	rand = thisrand;
 }
</script>

       </head>

    <body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
     <div id="mainContent">   
<%
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

  AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();

%>
    <div id ="assumedmatches " class="basicSearch">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{AssumeMatchHandler.searchType}" valueChangeListener="#{AssumeMatchHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AssumeMatchHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{AssumeMatchHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
		           <tr>
			     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{AssumeMatchHandler.instructionLine}" /></td>
			       </tr>

                    <tr>
                        <td>
						   <%
						   int countFc = 0;
						   ArrayList sList  = assumeMatchHandler.getSearchScreenFieldGroupArray();
						   for(int i=0;i<sList.size();i++) {
							    FieldConfigGroup fcg = (FieldConfigGroup)  sList.get(i);
								countFc = fcg.getFieldConfigs().size();
						   }
                           %>
                            
                            <input id='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />
                            <h:dataTable headerClass="tablehead"  
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{AssumeMatchHandler.searchScreenFieldGroupArray}">
						    <h:column>
   				            <div style="font-size:12px;font-weight:bold;color:#0739B6;"><h:outputText value="#{searchScreenFieldGroup.description}" /></div>
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                         value="#{searchScreenFieldGroup.fieldConfigs}">

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
                                                <h:selectOneMenu title='#{feildConfig.name}'
                                                                 rendered="#{feildConfig.name ne 'SystemCode'}"
	                                                             value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
                                                                  title='#{feildConfig.name}'                                           id="SystemCode" 
    															  value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
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
                                                               title='#{feildConfig.name}'
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   readonly="true"
															   title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
															   title='#{feildConfig.name}'
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
												   title="<h:outputText value="#{feildConfig.name}"/>"
                                                   value="<h:outputText value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                   required="<h:outputText value="#{feildConfig.required}"/>" 
                                                   maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateFieldsOnBlur(this,'<h:outputText value="#{feildConfig.name}"/>')">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{feildConfig.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                        </h:column>
							  <f:facet name="footer">
                                     <h:column>
                                       <div>
                                        <table  cellpadding="0" cellspacing="0">
                                         <tr>
                                          <td>
                                           <nobr>
                                              <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')" >
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                              </h:outputLink>
                                           </nobr>
                                          <nobr>
										   <a class="button" id = "deactivateReport" href="javascript:void(0)"
										   onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=rand'+'&'+queryStr,'outputdiv','')">
												 <span><h:outputText value="#{msgs.search_button_label}"/></span>
											</a>
                                        </nobr>
                                      </td>
                                        </tr>
                                      </table>
									   </div>
                                     </h:column>
                               </f:facet>

                            </h:dataTable>
								</h:column>
                            </h:dataTable>
                        </td>
						<td><div id="messages"> </div></td>
                    </tr>
					<%if(countFc == 0) {%>
					<tr>
					  <td> 
					     <h:outputText value="#{msgs.search_config_error}" />
					  </td>
					</tr>
					<%}%>
                </table>
                <h:inputHidden id="enteredFieldValues" value="#{AssumeMatchHandler.enteredFieldValues}"/>
            </h:form>
<div class="searchresults" id="outputdiv"></div>
    </div>     
  </div>
</body>
        <%
          String[][] lidMaskingArray = assumeMatchHandler.getAllSystemCodes();          
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
            var lidField =  getDateFieldName(formNameValue.name,'LID');
			if(lidField != null) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).readOnly = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).readOnly = true;
		    }

            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
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
     



</html>
</f:view>              
