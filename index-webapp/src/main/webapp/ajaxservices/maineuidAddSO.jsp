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
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>


<%
            double rand = java.lang.Math.random();
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            String URI = request.getRequestURI();
           //remove the app name 
            URI = URI.replaceAll("/ajaxservices","");
            URI = URI.substring(1, URI.lastIndexOf("/"));
			ConfigManager.init();

             String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

%>
<f:view>
        <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            <title><h:outputText value="#{msgs.application_heading}"/></title>
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
            <!-- Additional source files go here -->
            <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="../css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="../css/DatePicker.css" rel="stylesheet" media="screen">
            
			<script type="text/javascript" src="/scripts/edm.js"></script>
            <script type="text/javascript" src="/scripts/Validation.js"></script>
            <script type="text/javascript" src="/scripts/calpopup.js"></script>
            <script type="text/javascript" src="/scripts/Control.js"></script>
            <script type="text/javascript" src="/scripts/dateparse.js"></script>
            <script type="text/javascript" src="/scripts/newdateformat1.js"></script>

            <link rel="stylesheet" type="text/css" href="/css/yui/fonts/fonts-min.css" />
            <link rel="stylesheet" type="text/css" href="/css/yui/tabview/assets/skins/sam/tabview.css" />
            <script type="text/javascript" src="/scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="/scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="/scripts/yui/tabview/tabview.js"></script>
            <script type="text/javascript" src="/scripts/yui4jsf/event/event.js"></script>
           <!--there is no custom header content for this example-->
        </head>
<body>
<div>
<table valign="top" border="0" cellpadding="0" cellspacing="0" >
<tr>
<td valign="top">
<form id="RootNodeInnerForm" name="RootNodeInnerForm" method="post" enctype="application/x-www-form-urlencoded">
<table valign="top" border="0" cellpadding="0" cellspacing="0" style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
    <input type="hidden" title="lidmask" name="lidmask" value="DDD-DDD-DDDD" />
     <tr height="22px" valign="top">
         <td align="left">
		     System:
         </td>
    	  <td align="left">
             <h:selectOneMenu id="SystemCode" title="SystemCode"
			 style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;width:220px"
			 onchange="javascript:setLidMaskValue(this,'RootNodeInnerForm')" >
              <f:selectItem itemLabel="Select Source" itemValue="" />
               <f:selectItems  value="#{EditMainEuidHandler.systemCodes}" />
             </h:selectOneMenu>
         </td>
		 <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="left" nowrap>
		        <%=localIdDesignation%>:
   	    </td>
   	    <td align="left">
                <h:inputText id="LID" 
				             title="LID" 
				             readonly="true"
                             onkeydown="javascript:qws_field_on_key_down(this, document.RootNodeInnerForm.lidmask.value)"
                             onkeyup="javascript:qws_field_on_key_up(this)"/>
         </td>
   	    <td align="left">
	       <a class="button"  href="javascript:void(0)" onclick="javascript:getFormValues('RootNodeInnerForm');javascript:ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&validateLID=true','validateMessages','');">
           <span><h:outputText value="#{msgs.validate_button_text}"/></span></a>
         </td>
	</tr>
    <tr>
         <td colspan="3"><div id="validateMessages"></div></td>
    </tr>                                            
<tr>
<td valign="top" colspan="3">
    <!-- Start ADD  Fields-->
        <h:dataTable  headerClass="tablehead"  
                      id="hashIdEdit" 
                      width="100%"
   				      style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                      var="fieldConfigPerAdd" 
                      value="#{SourceHandler.rootNodeFieldConfigs}">
        <h:column>
           <nobr>
			 <h:outputText value="#{fieldConfigPerAdd.displayName}" />
              <h:outputText value="*" rendered="#{fieldConfigPerAdd.required}" />
              <h:outputText value=":"/>
			</nobr>
         </h:column>                                                        
            <!--Rendering HTML Select Menu List-->
            <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" >
                    <f:selectItem itemLabel="" itemValue="" />
                    <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                </h:selectOneMenu>
            </h:column>
            <!--Rendering Updateable HTML Text boxes-->
            <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                             id="fieldConfigIdTextbox"  
                             title="#{fieldConfigPerAdd.fullFieldName}"
							 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                             onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                             maxlength="#{fieldConfigPerAdd.maxLength}"
                             onkeyup="javascript:qws_field_on_key_up(this)" 
                             required="#{fieldConfigPerAdd.required}"/>
            </h:column>                     
            <!--Rendering Updateable HTML Text boxes date fields-->
            <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">
            
                <nobr>
                    <input type="text" 
                           title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                           id = "<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"  
                           required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                           maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                           onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                           onkeyup="javascript:qws_field_on_key_up(this)" >
                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>')" > 
                        <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                    </a>
                </nobr>
                
                
            </h:column>
            <!--Rendering Updateable HTML Text Area-->
            <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                 title="#{fieldConfigPerAdd.fullFieldName}"
                                 id="fieldConfigIdTextArea"   
                                 required="#{fieldConfigPerAdd.required}"
                                 />
            </h:column>
            
        </h:dataTable>
</td>
</tr>
</table>
</form>
</td>
</tr>
<!-- Root node form End -->

<tr>
<td valign="top">    
    <h:dataTable  headerClass="tablehead" 
                  id="allChildNodesNamesAdd" 
                  width="100%"
        		  style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                  var="childNodesName" 
                  value="#{SourceHandler.allChildNodesNames}">
        <h:column>
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td class="tablehead" colspan="2">
                        <h:outputText value="#{childNodesName}"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2">
                        <a href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')" class="button">
                        <span>
                            <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;Add <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
                        </span>
                    </td>
                </tr>
                <!--Minor objects loop starts-->
                <tr>
                    <td>
                        <div id="extra<h:outputText value='#{childNodesName}'/>AddNewDiv"  style="visibility:hidden;display:none;">
                            <table>
                                <tr>
                                    <td colspan="2" align="left">
                                        <form id="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" name="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                            <h:dataTable  headerClass="tablehead" 
                                                          id="allNodeFieldConfigsMapAdd" 
                                                          var="allNodeFieldConfigsMapAdd"
                                                          value="#{SourceHandler.allNodeFieldConfigs}">
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="childFieldConfigsAdd" 
                                                                  var="childFieldConfigAdd" 
                               				      style="width:100%;background-color:#c4c8e1;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
       <h:column>
           <nobr>
			 <h:outputText value="#{childFieldConfigAdd.displayName}" />
              <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
              <h:outputText value=":"/>
			</nobr>
         </h:column>                                                        
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                            <h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                                            <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                   name = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
																    onblur="javascript:validate_Integer_fields(this,'<h:outputText value="#{childFieldConfigAdd.displayName}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')"
                                                                   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')" 
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" />
                                                        </h:column>                     
                                                        
                                                        <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                                            <nobr>
                                                                <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                       id = "New<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                       required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                       maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                       onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                                       onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                       onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilateMinorObjectFieldsOnBlurLocal('<h:outputText value="#{childFieldConfigAdd.objRef}"/>',this,'<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>','<h:outputText value="#{childFieldConfigAdd.inputMask}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')">
                                                                <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'New<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                                    <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                </a>
                                                            </nobr>
                                                        </h:column>                     
                                                        
                                                        
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        
                                                        <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                            <h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
                                                                             onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                                             required="#{fieldConfigAddAddress.required}" />
                                                        </h:column>
                                                    </h:dataTable>                                                                                
                                                </h:column>
                                            </h:dataTable>                                                                                
                                            
                                        </form>
                                    </td>
                                </tr>
								<tr>
					             <td>
                 					<a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
								   <span id="<h:outputText value='#{childNodesName}'/>buttonspan">Save&nbsp;<h:outputText value='#{childNodesName}'/> </span>
                                 </a>     
                                 <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}AddNewSOInnerForm')">
                                        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                 </h:outputLink>
								 </td>
								</tr>
                            </table>   
                        </div>   
                    </td>
                    <td><div id="<h:outputText value='#{childNodesName}'/>EditMessages" >   </div></td>
                </tr>
                <!--Minor objects loop ends-->

                <tr>
                    <td>
                        <div id="stealth" style="visibility:hidden;height:0px"> </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="<h:outputText value="#{childNodesName}"/>NewDiv" ></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="<h:outputText value="#{childNodesName}"/>AddNewSODiv"></div>
                    </td>
                </tr>
            </table>   
        </h:column>
    </h:dataTable>
</td>
</tr>
<tr><td><div id="AddNewSODivMessages"></div></td></tr>                

<tr>
   <td>
   <nobr>
    <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('RootNodeInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&save=save','AddNewSODivMessages',event)">
	  <span id="buttonspan">Save&nbsp;</span>
    </a>     
     <h:outputLink  styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('RootNodeInnerForm')">
        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
     </h:outputLink>    
	 <a class="button"  href="javascript:void(0)" onclick="javascript:ClearContents('RootNodeInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&cancel=cancel','AddNewSODivMessages',event);">
        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
     </a>    
	 </nobr>
     </td>
</tr>

</table>
</div>


</body>
</html>
</f:view>
<!--End Add source record form-->
