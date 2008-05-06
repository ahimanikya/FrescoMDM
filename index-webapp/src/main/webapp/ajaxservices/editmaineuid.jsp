<%-- 
    Document   : editmaineuid
    Created on : Jan 4, 2008, 3:42:00 PM
    Author     : Rajani Kanth M
                 www.ligaturesoftware.com
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>

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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.EditMainEuidHandler"  %>

<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
 URI = URI.substring(1, URI.lastIndexOf("/"));

 String URICSS = URI.replaceAll("/ajaxservices","");

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
            <link type="text/css" href="/css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="/css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="/css/DatePicker.css" rel="stylesheet" media="screen">
            
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
        <%
		   //Variables for load
            String isLoadStr = request.getParameter("load");
            boolean isLoad = (null == isLoadStr?false:true);

            String euidValue = request.getParameter("euid");
        
		    HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   	        ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            String rootNodeName = objScreenObject.getRootObj().getName();
            EditMainEuidHandler editMainEuidHandler =  new EditMainEuidHandler();

			//(EditMainEuidHandler) facesSession.getAttribute("EditMainEuidHandler");
            editMainEuidHandler.setEditEOFields(euidValue);

            facesSession.setAttribute("EditMainEuidHandler",editMainEuidHandler);

            String editEuid = (String) session.getAttribute("editEuid");
            ValueExpression eoValueExpression = ExpressionFactory.newInstance().createValueExpression(editEuid, editEuid.getClass());
        %>
        <body>
            <div> 
                <div>
                    <div>
						  <table width="100%">
                                <tr>
                                    <td valign="top" align="left" >
                                        <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
										<div id="euidFinalErrorMessages"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" colspan="2">
					                 <a class="button" href="javascript:void()" onclick="history.go(-1)">
						               <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					                 </a>
                                    </td>
                                </tr>
						       <tr>
                                    <td class="tablehead">
                                        <h:outputText value="#{msgs.edit_main_euid_label_text}" /> <%=euidValue%> &nbsp;&nbsp;
                                    </td>
                                </tr>
                           </table>						   
                            <table valign="top" border="0" cellpadding='0' cellspacing="0">
                                <tr>
                                    <td valign="top" style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef">
                                        <!-- Start Main Euid Details-->
                                        <div id="addTab">
                                            <!-- Start EDIT Fields-->
                                            <table border="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef" >
                                                <tr>
                                                    <td class="tablehead" align="right"  colspan="2">
                                                        <h:outputText value="#{msgs.main_euid_label_text}"/>      
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            </table>
                                            <!--Start Displaying the root node fields -->                                        
			                                <table valign="top" border="0" cellpadding='0' cellspacing="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef" >
											   <tr>
											     <td valign="top">
											       <form id="EORootNodeForm" >

                                             <h:dataTable                                         
                                                           id="hashIdEdit" 
														   style="verticle-align:top;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef"
                                                           var="fieldConfigPer" 
                                                           value="#{SourceHandler.rootNodeFieldConfigs}">                                                    
                                                 <h:column>
                                                     <nobr>
													  <h:outputText value="#{fieldConfigPer.displayName}" />
                                                      <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                      <h:outputText value=":"/>
													 </nobr>
                                                 </h:column>                                                        
                                                 <h:column>
												    <table border="0" cellpadding="0" cellspacing="0">
													  <tr>
													   <td valign="center">
                                                     <div id='linkSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'  style="valign:top;">
                                                     <h:outputLink        rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"   
                                                                    value="javascript:void(0)" >
                                                            <img alt="link" border="0"src="images/link.PNG"/>               
                                                      </h:outputLink>
													 <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] &&!EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                                <img alt="link" styleClass="imgClass" border="0" src="images/link.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id='linkSourceDivData:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'             style='visibility:hidden;display:none;'>
                                                           <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                               <img alt="link" border="0" src="images/link.PNG"/>               
                                                            </h:outputLink>
                                                     </div> 
                                                   </td>                              
												   <td valign="center">												 
                                                     <div id='unlockSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style="valign:top;">
													 <h:panelGrid rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }">
													 <h:panelGroup><a href ="javascript:void(0)" 
                                                     onclick="document.getElementById('hiddenUnLockFields').value = '<h:outputText value="#{fieldConfigPer.fullFieldName}"/>';getFormValues('unLockFieldsForm');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&unlocking=true','euidFinalErrorMessages','')"><img alt="unlock" border="0" height="13px" width="13px" src="images/unlock.PNG"/></a></h:panelGroup></h:panelGrid>
                                                     </div> 
                                                     <div id="lockSourceDiv" style="valign:top;">
                                                     <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" >
                                                            <img   alt="lock" border="0" height="13px" width="13px" src="images/lock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id='lockSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style='visibility:hidden;display:none'>
                                                     <h:outputLink  value="javascript:void(0)" >
                                                            <img alt="lock" border="0" height="13px" width="13px" src="images/lock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
													  </td>
													  </tr>
													</table>
                                                 </h:column>                                                        
                                                 <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu  title="#{fieldConfigPer.fullFieldName}"
														style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
														value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT_CODES'][fieldConfigPer.fullFieldName]}" 
                                                                         disabled="#{!fieldConfigPer.updateable || (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}"                                                                         
                                                                         readonly="#{!fieldConfigPer.updateable || (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
														             title="#{fieldConfigPer.fullFieldName}"
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                      maxlength="#{fieldConfigPer.maxLength}"
																	  disabled="#{!fieldConfigPer.updateable || EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"
                                                                     readonly="#{!fieldConfigPer.updateable || EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}" 
                                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPer.inputMask}')"
                                                                     onkeyup="javascript:qws_field_on_key_up(this)"                    
                                                                 />
                                                         </div>            
                                                    </h:column>
                                                    
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 && !(EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <nobr>
                                                            <input type="text" 
															       title="<h:outputText value="#{fieldConfigPer.fullFieldName}"/>"
                                                                   id = "<h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
																   readonly="<h:outputText value="#{!fieldConfigPer.updateable}"/>"
                                                                   value = "<h:outputText value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                   />
                                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPer.name}"/>')" > 
                                                            <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                            </a>
                                                        </nobr>
                                                        </div>            
                                                    </h:column>

                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 && (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <nobr>
                                                            <input type="text" 
                                                                   id = "<h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                   value = "<h:outputText value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
																   readonly="<h:outputText value="#{!fieldConfigPer.updateable}"/>"
                                                                   />
                                                                       
                                                            <h:graphicImage  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                
                                                        </nobr>
                                                        </div>            
                                                    </h:column>

                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                    <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextArea"   
																	 disabled="#{!fieldConfigPer.updateable || EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"
                                                                     readonly="#{!fieldConfigPer.updateable || EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}" 
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"/>
                                                </h:column>
                                                
                                            </h:dataTable>    


											</form>
											</td>
											</tr>
                    <tr>
					  <td><nobr>
                          <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('EORootNodeForm');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&saveSbr=save','EUIDRootDiv','Updating SBR...please wait')">
                               <span id="EObuttonspan">Update <%=rootNodeName%></span>
                           </a>    
                           <a class="button"  href="javascript:void(0)" onclick="javascript:ClearContents('EORootNodeForm');setEOEditIndex('-1')">
                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
						   </a>
						  </nobr>
					  </td>
					</tr>

			 <tr>
				 <td colspan="2">
 					 <div id="EUIDRootEditMessages" ></div>
				 </td>
		     </tr>
			 <tr>
			    <td colspan="2">
					 <div id="EUIDRootDiv"></div>
				 </td>
			 </tr>
											</table>
                                            <!--End Displaying the root  fields -->    



                                            <!-- aersrlknflksdsdsf -->
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="allChildNodesNamesAdd" 
                                                                  style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef"                     var="childNodesName" 
                                                                  value="#{SourceHandler.allChildNodesNames}">
                                                        <h:column>
                                                            <table width="100%" valign="top" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td class="tablehead" colspan="2">
                                                                        <h:outputText value="#{childNodesName}"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2">
																	<a href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>EOMinorDiv');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&load=load&rand=<%=rand%>','<h:outputText value="#{childNodesName}"/>EOMinorDiv',event)" class="button">
																	<span>
							                                            <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>View &nbsp;<h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
																	</span>

                                                                    </td>
                                                                </tr>
			<!--Minor objects loop starts-->
																<tr>
																<td>

			<div id="extra<h:outputText value='#{childNodesName}'/>EOMinorDiv"  style="visibility:hidden;display:none;">
			    <form id="<h:outputText value="#{childNodesName}"/>EOInnerForm" >
                <table style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef">
                    <tr>
                        <td valign="top"> 
                                <h:dataTable  headerClass="tablehead" 
                                              id="allNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef"                                               value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
			                            <h:dataTable  headerClass="tablehead" 
                                                      id="childFieldConfigsAdd" 
                                                      var="childFieldConfigAdd" 
                                                      style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;background-color:#efefef"                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                            
                                            <h:column>
                                                <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                                <h:outputText value=":"/>
                                            </h:column>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}"
                                                                 style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
>
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                                        <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                     id="childFieldConfigAdd"   
																	 title="#{childFieldConfigAdd.fullFieldName}"
                                                                     maxlength="#{childFieldConfigAdd.maxLength}"
																	 readonly="#{!childFieldConfigAdd.updateable}"
                                                                     onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')" 
																	 onkeyup="javascript:qws_field_on_key_up(this)"
																	 required="#{childFieldConfigAdd.required}"  
																	 />

                                            </h:column>                     
 
                                     <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                  onblur="javascript:validate_date(this,'MM/dd/yyyy');">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{childFieldConfigAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{fieldConfigAddAddress.fullFieldName}"  
                                                                 required="#{fieldConfigAddAddress.required}" />
                                            </h:column>
                                        </h:dataTable>                                                                                
			                        </h:column>
                                </h:dataTable>                                                                                
						</td>
					</tr>
                    <tr>
					  <td><nobr>
                          <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>EOInnerForm');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>EOMinorDiv',event)">
                             <span id="EO<h:outputText value='#{childNodesName}'/>buttonspan">Save <h:outputText value='#{childNodesName}'/> </span>
                          </a>
                           <a class="button"  href="javascript:void(0)" onclick="javascript:ClearContents('<h:outputText value="#{childNodesName}"/>EOInnerForm');setEOEditIndex('-1')">
                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                          </a>
                         <div style="visibility:hidden;display:none;" id="EO<h:outputText value='#{childNodesName}'/>cancelEdit">
                                         <a href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>EOInnerForm', '<h:outputText value='#{childNodesName}'/>cancelEdit', '<h:outputText value='#{childNodesName}'/>')">
                                          <span>Cancel <h:outputText value='#{childNodesName}'/></span>
                                         </a>     
                         </div>

						  </nobr>
					  </td>
					</tr>
					</table>
					</form>
					</div>
			         </td>
			         <td>
						       </div>
					  </td>
			       </tr>
			<!--Minor objects loop ends-->

			 <tr>
				 <td colspan="2">
 					 <div id="<h:outputText value='#{childNodesName}'/>EditMessages" ></div>
				 </td>
		     </tr>
			 <tr>
			    <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/>EOMinorDiv"></div>
				 </td>
			 </tr>

              </table>   
           </h:column>
          </h:dataTable>
		  <!---->

                                         </div>                           
										 <div>
                                            <table style="background-color:#efefef" width="100%">
                                                <tr>
                                                    <td>
                                                  <a href="euiddetails.jsf?euid=<%=editEuid%>" class="button" onclick="ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&deactiveEO=true','euidFinalErrorMessages',event)">

                                                            <span><h:outputText value="Deactivate" /></span>
                                                        </a> 
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>

                                    <!-- End Main Euid Details-->
                                    <td valign="top">
									<%
									   ArrayList eoList = editMainEuidHandler.getEoSystemObjects();
                                    %>
									   <%if(eoList.size() == 1) {%>
                                        <div id="sourceRecordsDiv">
									   <%}else if(eoList.size() >= 2) {%>
                                        <div id="sourceRecordsDiv" style="width:660px;overflow:auto;overflow-y:hidden;">
									   <%}%>

                                        <table border="0" cellpadding="0px" cellspacing="0px" style="width:100%;background-color:#cddcb1;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;">
                                        <tr>
                                                        
                                                        <!-- Start Main Euid SO Details-->
                                    <%
            ValueExpression eoSystemObjectsValueExpression = ExpressionFactory.newInstance().createValueExpression(eoList, eoList.getClass());
            String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

            for (int i = 0; i < eoList.size(); i++) {
                HashMap valueMap = (HashMap) eoList.get(i);
                ValueExpression eoMapValueExpression = ExpressionFactory.newInstance().createValueExpression(valueMap, valueMap.getClass());
%>

                                                 <%String soStatus = (String) valueMap.get("Status");
												   String colorCode = "#cddcb1";%>
                                                 <%if("active".equalsIgnoreCase(soStatus)) {
													   colorCode = "#cddcb1";
												 %>
											     <%} else if("inactive".equalsIgnoreCase(soStatus)){
													   colorCode = "#e7e7d6";
												 %>
											     
											     <%} else if("merged".equalsIgnoreCase(soStatus)){
													   colorCode = "#fdeddf";
													 %>
											     <%} else{
													   colorCode = "#cddcb1";
												 %>
											     <%} %>
                                                        <td valign="top" style="background-color:<%=colorCode%>;border-top:1px solid #efefef;border-left:1px 
														solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;">
												

                                                        <table border="0" cellspacing="0" cellpadding="0" style="width:100%;background-color:<%=colorCode%>;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
                                                                 <tr>
                                                                        <td class="tablehead" colspan="2">
                                                                                <%=valueMap.get("SYSTEM_CODE_DESC")%> (<%=valueMap.get("Status")%>)
                                                                        </td>
                                                                 </tr>
                                                                  <tr>
                                                                        <td style="width:50%;padding-left:5px; font-size: 12px; text-align: left;">  <%=localIdDesignation%></td>
                                                                        <td style="width:50%;padding-left:5px; font-size: 12px; text-align: left;">  
																		    <b><%=valueMap.get("LID")%></b>
                                                                                <!--input type="text" style="background-color:#cddcb1" readonly="true" value='<%=valueMap.get("LID")%>'/--> 	
                                                                        </td>
                                                                 </tr>
                                                        </table>


                                                            <h:dataTable id="hashIdEditEo" 
                                                                          style="background-color:<%=colorCode%>;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                                                                          var="eoSystemObjectMap"  
                                                                          value="<%=eoMapValueExpression%>">
																<h:column>
																	<!-- SYSTEM OBJECT ROOT NODE FIELDS FORM START HERE-->
                                                   			    <form id="<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SORootNodeInnerForm" >
                                                 <%if("active".equalsIgnoreCase(soStatus)) {%>

                                                                    <h:dataTable  headerClass="tablehead"                                        
                                                                                  width="100%"
                                                                                  id="hashIdEdit" 
                                                                                  var="fieldConfigPer" 
																				  style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">     
                                                <h:column>
                                                     <nobr>
													  <h:outputText value="#{fieldConfigPer.displayName}" />
                                                      <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                      <h:outputText value=":"/>
													 </nobr>
                                                 </h:column>                                                        
                             
                                                                        <h:column>
                                                                            <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/>:<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'
                                                                                 style="visibility:hidden;display:none;">
                                                                                <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                    <h:graphicImage  alt="link" styleClass="imgClass" url="./images/link.PNG"/>               
                                                                                </h:outputLink>
                                                                            </div> 
                                                                            <h:outputLink  rendered="#{EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                                           value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                <h:graphicImage  alt="link" styleClass="imgClass"
                                                                                                 url="./images/link.PNG"/>               
                                                                            </h:outputLink>
                                                                        </h:column>                                                        
                                                                        
                                                                        <!--Rendering HTML Select Menu List-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:selectOneMenu 													
																			title="#{fieldConfigPer.fullFieldName}"
																			style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"			
																			disabled="#{!fieldConfigPer.updateable}"
																			readonly="#{ !fieldConfigPer.updateable}"
																			value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}">
                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                                            </h:selectOneMenu>
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPer.inputMask}')" 
																	                     onkeyup="javascript:qws_field_on_key_up(this)"
																			             title="#{fieldConfigPer.fullFieldName}"
                                                                                         id="fieldConfigIdTextbox"   
                                                                                         maxlength="#{fieldConfigPer.maxLength}"
																			             disabled="#{!fieldConfigPer.updateable}"
																			             readonly="#{!fieldConfigPer.updateable}"
                                                                               value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                         required="#{fieldConfigPer.required}"
                                                                                         />
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                                        <h:column rendered="#{!fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' && fieldConfigPer.valueType eq 6}">
																		    <nobr>
                                                                            <input type="text" 
																			       title="<h:outputText value="#{fieldConfigPer.fullFieldName}"/>"
																				   readonly="true"
																				   disabled="true"
                                                                                   id = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                   value = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                                   />
                                                                                      
                                                                                <h:graphicImage alt="#{fieldConfigPer.displayName}"  styleClass="imgClass" url="./images/cal.gif"/>               
																		   </nobr>
                                                                       </h:column>

																		<h:column rendered="#{fieldConfigPer.updateable && fieldConfigPer.guiType eq 'TextBox' && fieldConfigPer.valueType eq 6}">
																		    <nobr>

                                                                            <input type="text" 
																			       title="<h:outputText value="#{fieldConfigPer.fullFieldName}"/>"
                                                                                   id = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                   value = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                                   />
                                                                                       
                                                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>')" > 
                                                                                <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                            </a>
																		   </nobr>
                                                                       </h:column>
                                                                        <!--Rendering Updateable HTML Text Area-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputTextarea label="#{fieldConfigPer.displayName}"  
																			                 title="#{fieldConfigPer.fullFieldName}"
                                                                                             id="fieldConfigIdTextArea"   
                                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                             required="#{fieldConfigPer.required}"/>
                                                                        </h:column>
                                                            </h:dataTable>
															<%} else {%>
                                                               <h:dataTable  headerClass="tablehead"                                        
                                                                                  width="100%"
                                                                                  id="hashIdReadOnly" 
                                                                                  var="fieldConfigPer" 
																				  style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">     
                                                <h:column>
                                                     <nobr>
													  <h:outputText value="#{fieldConfigPer.displayName}" />
                                                      <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                      <h:outputText value=":"/>
													 </nobr>
                                                 </h:column>                                                        
                             
                                                                        <h:column>
                                                                            <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/>:<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'
                                                                                 style="visibility:hidden;display:none;">
                                                                                <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                    <h:graphicImage  alt="link" styleClass="imgClass" url="./images/link.PNG"/>               
                                                                                </h:outputLink>
                                                                            </div> 
                                                                            <h:outputLink  rendered="#{EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                                           value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                <h:graphicImage  alt="link" styleClass="imgClass"
                                                                                                 url="./images/link.PNG"/>               
                                                                            </h:outputLink>
                                                                        </h:column>                                                        
                                                                        
                                                                        <!--Rendering HTML Select Menu List-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:selectOneMenu 													
																			title="#{fieldConfigPer.fullFieldName}"
																			style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"			
																			disabled="true"
																			readonly="true"
																			value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}">
                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                                            </h:selectOneMenu>
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPer.inputMask}')" 
																	                     onkeyup="javascript:qws_field_on_key_up(this)"
																			             title="#{fieldConfigPer.fullFieldName}"
                                                                                         id="fieldConfigIdTextbox"   
                                                                                         maxlength="#{fieldConfigPer.maxLength}"
																			             disabled="true"
																			             readonly="true"
                                                                               value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                         required="#{fieldConfigPer.required}"
                                                                                         />
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' && fieldConfigPer.valueType eq 6 }">
																		    <nobr>
                                                                            <input type="text" 
																			       title="<h:outputText value="#{fieldConfigPer.fullFieldName}"/>"
																				   readonly="true"
																				   disabled="true"
                                                                                   id = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                   value = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                                   />
                                                                                       
                                                                                <h:graphicImage   alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
																		   </nobr>
                                                                       </h:column>
                                                                        <!--Rendering Updateable HTML Text Area-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputTextarea label="#{fieldConfigPer.displayName}"  
																			                 title="#{fieldConfigPer.fullFieldName}"
                                                                                             id="fieldConfigIdTextArea"   
																							 readonly="true"
                                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                             required="#{fieldConfigPer.required}"/>
                                                                        </h:column>
                                                            </h:dataTable>

															<%}%>
                                					     </form>
														 <table>
                    <tr>
					  <td><nobr>
                       <%if("active".equalsIgnoreCase(soStatus)) {%>
                          <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SORootNodeInnerForm');ajaxMinorObjects('/<%=URI%>/editsominorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&SOLID=<h:outputText value="#{eoSystemObjectMap['LID']}"/>&SOSYS=<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>&rand=<%=rand%>&save=save','<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>RootEditMessages',event)">

                               <span id="EObuttonspan">Update <%=rootNodeName%></span>
                           </a>
                           <a class="button"  href="javascript:void(0)" onclick="javascript:ClearContents(<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SORootNodeInnerForm);setEOEditIndex('-1')">
                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
						   </a>
						 <%}else{%>
						    <input type="button" value="Update <%=rootNodeName%>" readonly="true" disabled="true"/>
						    <input type="button" value="Clear" readonly="true" disabled="true"/>
						 <%}%>
						</nobr>
					  </td>
					</tr>

			 <tr>
				 <td colspan="2">
 					 <div id="<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>RootEditMessages" ></div>
				 </td>
		     </tr>
														 </table>
														 <!-- EUID SYSTEM OBJECT ROOT NODE FIELDS FORM ENDS HERE-->

                                                        <!-- START EUID SO  MINOR OBJECTS START HERE -->
                                                         <h:dataTable  headerClass="tablehead" 
                                                                  id="soChildNodesNamesAdd" 
                                                                  style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"                     
																  var="childNodesName" 
                                                                  value="#{SourceHandler.allChildNodesNames}">
                                                        <h:column>
                                                            <table width="100%" valign="top" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td class="tablehead" colspan="2">
                                                                        <h:outputText value="#{childNodesName}"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2">
                                                      
																	<a value="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMinorDiv');ajaxMinorObjects('/<%=URI%>/editsominorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&SOLID=<h:outputText value="#{eoSystemObjectMap['LID']}"/>&SOSYS=<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>&load=load&rand=<%=rand%>','<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMinorDiv',event)" class="button">
																	<span>
																	<img src="./images/down-chevron-button.png" border="0" alt="View <h:outputText value="#{childNodesName}"/>" />
																	View &nbsp;<h:outputText value="#{childNodesName}"/>&nbsp;
																	<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>" />
																	</span>
                                                                    </a>

                                                                   </td>
                                                                </tr>
			<!--Minor objects loop starts-->
																<tr>
																<td>

			<div id="extra<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMinorDiv"  style="visibility:hidden;display:none;">
			    <form id="<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOInnerForm" >
                <table style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
                    <tr>
                        <td valign="top"> 
                                <h:dataTable  headerClass="tablehead" 
                                              id="soNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"  
											  value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
			                            <h:dataTable  headerClass="tablehead" 
                                                      id="soChildFieldConfigsAdd" 
                                                      var="soChildFieldConfigAdd" 
                                                      style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                                 <h:column>
                                                     <nobr>
													  <h:outputText value="#{soChildFieldConfigAdd.displayName}" />
                                                      <h:outputText value="*" rendered="#{soChildFieldConfigAdd.required}" />
                                                      <h:outputText value=":"/>
                                					 </nobr>
                                                 </h:column>                                                        
                                          <%if("active".equalsIgnoreCase(soStatus)) {%>

                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu title="#{soChildFieldConfigAdd.fullFieldName}"
                                                                 style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{soChildFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextBox' &&  soChildFieldConfigAdd.valueType ne 6}" >
                                                     <h:inputText label="#{soChildFieldConfigAdd.displayName}" 
 													              title="#{soChildFieldConfigAdd.fullFieldName}"
                                                                  id="soChildFieldConfigAdd"   
                                                                  maxlength="#{soChildFieldConfigAdd.maxLength}"
                                                                 disabled="#{ !soChildFieldConfigAdd.updateable}"
                                                                 readonly="#{ !soChildFieldConfigAdd.updateable}"
                                                                  onkeydown="javascript:qws_field_on_key_down(this, '#{soChildFieldConfigAdd.inputMask}')" 
																	 onkeyup="javascript:qws_field_on_key_up(this)"
																	 required="#{soChildFieldConfigAdd.required}"  
																	 />

                                            </h:column>                     
 
                                     <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextBox'  &&  soChildFieldConfigAdd.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" title = "<h:outputText value="#{soChildFieldConfigAdd.fullFieldName}"/>"  
                                                   readonly="<h:outputText value="#{!fieldConfigPer.updateable}" />"
                                                   disabled="<h:outputText value="#{!fieldConfigPer.updateable}" />"
                                                   id = "<h:outputText value="#{soChildFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{soChildFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{soChildFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{soChildFieldConfigAdd.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                  onblur="javascript:validate_date(this,'MM/dd/yyyy');">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{soChildFieldConfigAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{fieldConfigAddAddress.fullFieldName}"  
                                                                 disabled="#{!fieldConfigAddAddress.updateable}"
                                                                 readonly="#{!fieldConfigAddAddress.updateable}"
							                                     required="#{fieldConfigAddAddress.required}" />
                                            </h:column>
											<%}else{%>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu title="#{soChildFieldConfigAdd.fullFieldName}"
                                                                 disabled="true"
                                                                 readonly="true"
                                                                 style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{soChildFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextBox' &&  soChildFieldConfigAdd.valueType ne 6}" >
                                                     <h:inputText label="#{soChildFieldConfigAdd.displayName}" 
 													              title="#{soChildFieldConfigAdd.fullFieldName}"
                                                                  id="soChildFieldConfigAdd"   
                                                                  maxlength="#{soChildFieldConfigAdd.maxLength}"
                                                                 disabled="true"
                                                                 readonly="true"
                                                                  onkeydown="javascript:qws_field_on_key_down(this, '#{soChildFieldConfigAdd.inputMask}')" 
																	 onkeyup="javascript:qws_field_on_key_up(this)"
																	 required="#{soChildFieldConfigAdd.required}"  
																	 />

                                            </h:column>                     
 
                                     <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextBox'  &&  soChildFieldConfigAdd.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" title = "<h:outputText value="#{soChildFieldConfigAdd.fullFieldName}"/>"  
                                                   readonly="true"
                                                   disabled="true"
                                                   id = "<h:outputText value="#{soChildFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{soChildFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{soChildFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{soChildFieldConfigAdd.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                  onblur="javascript:validate_date(this,'MM/dd/yyyy');">
                                                  <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{soChildFieldConfigAdd.name}"/>')" > 
                                                     <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                 </a>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{soChildFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{fieldConfigAddAddress.fullFieldName}"  
                                                                 disabled="true"
                                                                 readonly="true"
							                                     required="#{fieldConfigAddAddress.required}" />
                                            </h:column>
											<%}%>
                                        </h:dataTable>                                                                                
			                        </h:column>
                                </h:dataTable>                                                                                
						</td>
					</tr>
                    <tr>
					  <td>
					  <nobr>
					  <%if("active".equalsIgnoreCase(soStatus)) {%>
					      <a href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText  value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOInnerForm');ajaxMinorObjects('/<%=URI%>/editsominorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&SOLID=<h:outputText value="#{eoSystemObjectMap['LID']}"/>&SOSYS=<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMinorDiv',event)">
                               <span id="<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>buttonspan">Save <h:outputText value='#{childNodesName}'/> </span>
                            </a>
						    <a class="button"  href="javascript:void(0)" onclick="javascript:ClearContents('<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOInnerForm');setEOEditIndex('-1')">
                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                          </a>
                        <%}else{%>
                            <input type="button" value="Save <h:outputText value="#{childNodesName}"/>" readonly="true" disabled="true"/>
                            <input type="button" value="Clear" readonly="true" disabled="true"/>
                      	<%}%>
						</nobr>
                         <div style="visibility:hidden;display:none;" id="<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>cancelEdit">
                                         <a href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOInnerForm', 'EO<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>cancelEdit', '<h:outputText value='#{childNodesName}'/>')">
                                          <span>Cancel <h:outputText value='#{childNodesName}'/></span>
                                         </a>     
                         </div>
					  </td>
					</tr>
					</table>
					</form>
					</div>
			         </td>
			         <td>
						       </div>
					  </td>
			       </tr>
			<!--Minor objects loop ends-->

			 <tr>
				 <td colspan="2">
 					 <div id="<h:outputText value='#{childNodesName}'/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOEditMessages" ></div>
				 </td>
		     </tr>
			 <tr>
			    <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMinorDiv"></div>
				 </td>
			 </tr>

              </table>   
           </h:column>
          </h:dataTable>
		  <table>
		  	  <tr>
				 <td colspan="2">
				        <%if("active".equalsIgnoreCase(soStatus)) {%>
					      <a href="javascript:void(0);" class="button" onclick="ajaxMinorObjects('/<%=URI%>/editsominorobjects.jsf?'+queryStr+'&SOLID=<h:outputText value="#{eoSystemObjectMap['LID']}"/>&SOSYS=<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>&rand=<%=rand%>&deactivateSO=true','<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMessageDiv',event)">
                               <span>Deactivate</span>
                            </a>
						<%} else if("inactive".equalsIgnoreCase(soStatus)){%>
							<a href="javascript:void(0);" class="button" onclick="ajaxMinorObjects('/<%=URI%>/editsominorobjects.jsf?'+queryStr+'&SOLID=<h:outputText value="#{eoSystemObjectMap['LID']}"/>&SOSYS=<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>&rand=<%=rand%>&activateSO=true','<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMessageDiv',event)">
                               <span>Activate</span>
                            </a>

						<%}%>
				 </td>
		     </tr>
		     <tr><td>&nbsp;<div id="<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/>:<h:outputText value="#{eoSystemObjectMap['LID']}"/>SOMessageDiv"></div></td></tr>
		  </table>
			<!-- START EUID SO  MINOR OBJECTS END HERE -->
                                                                </h:column>
                                                            </h:dataTable> 
                                                        </td>
                                                        <%}%>
                                                </tr>


                                            </table>
                                                    </div>
                                            </td>
                                 <form id="unLockFieldsForm">
								 	<input type="hidden" name="hiddenUnLockFields" title="hiddenUnLockFields"/>
                                </form>
								<td valign="top">&nbsp;</td>
								<td valign="top">
                                     <!-- New SO fields start here -->
                                     <div id="AddSO"  style="width:220px;visibility:hidden;display:none;background-color:#c4c8e1;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"></div>
								</td>
							</tr>
							<tr style="background-color:#efefef"><td colspan="2"><hr/></td></tr>

                            <!-- SAVE, CANCEL AND ADD NEW SO BUTTONS START HERE-->
							<tr style="background-color:#efefef">
                                <td>
								 <nobr>
                                 <a href="javascript:void(0);" class="button" onclick="ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&save=save','euidFinalErrorMessages',event)">
                                       <span><h:outputText value="Save" /></span>
                                    </a>
                                    <a href="euiddetails.jsf?euid=<%=editEuid%>" class="button" onclick="ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&canceleoedit=true','euidFinalErrorMessages',event)">
                                       <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                   </a> 
									 <a href="javascript:void(0);" class="button" onclick="ajaxMinorObjects('/<%=URI%>/maineuidAddSO.jsf?&rand=<%=rand%>&addSO=addSO','AddSO','')">	
											<span id="<h:outputText value='#{childNodesName}'/>buttonspan">Add SO</span>

									 </a>     
									 </nobr>
                                 </td>
                             </tr>
                            <!-- SAVE, CANCEL AND ADD NEW SO BUTTONS ENDS HERE-->
							
                        </table>

                    </div>         
                </div>         
    <!-- END Extra divs for NEW  SO-->
            <div id="linkSoDiv"  class="alert" style="TOP:620px;LEFT:450px;HEIGHT:100px;WIDTH:350px;overflow:auto;VISIBILITY:hidden;">
                <form name="linkForm">
				<input type="hidden" name="sbrfullfieldname" title="sbrfullfieldname">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="linkedValueDiv" style="visibility:hidden"></div>
                            <nobr>Link to the '<span id="linkedDisplayValueDiv"></span>' field of :</nobr> 
                        </td>
                        <td>
                            <h:selectOneMenu id="systemCodeWithLid" title="systemCodeWithLid" value="#{EditMainEuidHandler.linkedSoWithLidByUser}">
                                <f:selectItems  value="#{EditMainEuidHandler.eoSystemObjectCodesWithLids}" />
                            </h:selectOneMenu>
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <a  class="button"  href="javascript:void(0)" onclick="javascript:getFormValues('linkForm');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&linking=true','euidFinalErrorMessages','')">                          
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
							</a>
                        </td>
                        <td align="left">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('linkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 
           <div id="unLinkSoDiv" class="alert" style="TOP:620px;LEFT:450px;HEIGHT:120px;WIDTH:350px;VISIBILITY:hidden;">
               <form name="unlinkForm">
				<input type="hidden" name="sbrfullfieldname" title="sbrfullfieldname">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="unLinkedValueDiv" style="visibility:hidden"></div>
                            <div id="unLinkedFullFieldDiv" style="visibility:hidden"></div>
                            <nobr>Unlink from  '<span id="unLinkedDisplayValueDiv"></span>' field of Main EUID?</nobr> 
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <a  class="button"  href="javascript:void(0)" onclick="javascript:getFormValues('unlinkForm');ajaxMinorObjects('/<%=URI%>/editeuidminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&unlinking=true','euidFinalErrorMessages','')">                          
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                            </a>
                           <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('unLinkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 
         </div>         

        </body>
    </html>
    </f:view>
    
    
    
