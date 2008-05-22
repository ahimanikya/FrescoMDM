<%-- 
    Document   : Duplicate Record Ajax services
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Rajani Kanth M
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ArrayList"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>


<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.math.BigDecimal"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="java.util.ResourceBundle"  %>

<f:view>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />


<%
String URI_Session = request.getRequestURI();URI_Session = URI_Session.substring(1, URI_Session.lastIndexOf("/"));
//remove the app name 
URI_Session = URI_Session.replaceAll("/ajaxservices","");
boolean isSessionActive = true;
%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI_Session%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>
<%if (isSessionActive)  {%>

<%
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
MasterControllerService masterControllerService = new MasterControllerService();

String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
SearchDuplicatesHandler searchDuplicatesHandler = new SearchDuplicatesHandler();

Enumeration parameterNames = request.getParameterNames();
Enumeration parameterNamesResolve = request.getParameterNames();

Operations operations = new Operations();
//Map to hold the validation Errors
HashMap valiadtions = new HashMap();

EDMValidation edmValidation = new EDMValidation();         


//List to hold the results
ArrayList finalArrayList = new ArrayList();

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

ArrayList fullFieldNamesList  = new ArrayList();


//Variables required compare euids
String compareEuids = request.getParameter("compareEuids");
boolean iscompareEuids = (null == compareEuids?false:true);

//Variables required for compare euids
String collectedEuids = request.getParameter("collecteuid");

//resolveDuplicate

//Variables required for resolve Duplicate
String resolveDuplicate = request.getParameter("resolveDuplicate");
boolean isResolveDuplicate = (null == resolveDuplicate?false:true);

//unresolve Duplicate fields
String unresolveDuplicate = request.getParameter("unresolveDuplicate");
boolean isUnresolveDuplicate = (null == unresolveDuplicate?false:true);


//unresolve Duplicate fields
String previewMerge = request.getParameter("previewMerge");
boolean isPreviewMerge = (null == previewMerge?false:true);

//multi Merge EO fields
String multiMergeEOs = request.getParameter("multiMergeEOs");
boolean isMultiMergeEOs = (null == multiMergeEOs?false:true);

//cancel Multi Merge EOs operation fields  (cancelMultiMergeEOs)
String cancelMultiMergeEOs = request.getParameter("cancelMultiMergeEOs");
boolean isCancelMultiMergeEOs= (null == cancelMultiMergeEOs?false:true);

String keyParam = new String();
ArrayList collectedEuidsList = new ArrayList();
%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>
 <%
                    ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                    SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
  
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
                    Iterator searchResultFieldsIter = searchDuplicatesHandler.getResultsConfigArray().iterator();
                    Object[] resultsConfigFeilds  = searchDuplicatesHandler.getResultsConfigArray().toArray();
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer mainEUID = new StringBuffer();
                    StringBuffer dupEUID = new StringBuffer();
                    ValueExpression finalArrayListVE = null;
					ValueExpression potDupIdValueExpression = null;
					ValueExpression duplicateSearchObjectVE = null;
                    PotentialDuplicateSearchObject potentialDuplicateSearchObject = (PotentialDuplicateSearchObject) request.getAttribute("duplicateSearchObject");                                    
				     if(potentialDuplicateSearchObject != null) {
						   duplicateSearchObjectVE  = ExpressionFactory.newInstance().createValueExpression(potentialDuplicateSearchObject, potentialDuplicateSearchObject.getClass());
					}
    HashMap eoMultiMergePreviewHashMap = new HashMap();

%>                



<%if(isPreviewMerge) {%>  <!--if is Preview Merge-->
 <%
	 HashMap previewDuplicatesMap = new HashMap();
	 //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
		
	   if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) &&
			!("potentialDuplicateId".equalsIgnoreCase(attributeName)) &&
			!("resolveType".equalsIgnoreCase(attributeName)) 
		   ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
	   }
   } 

//parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("destnEuidValue".equalsIgnoreCase(attributeName)) ||
           ("rowcount".equalsIgnoreCase(attributeName)) ||
           ("sourceEuids".equalsIgnoreCase(attributeName))
		  
		  ) { 
		   previewDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   if(previewDuplicatesMap.keySet().toArray().length  > 0) {
     eoMultiMergePreviewHashMap = searchDuplicatesHandler.previewPostMultiMergedEnterpriseObject((String) previewDuplicatesMap.get("destnEuidValue"), (String) previewDuplicatesMap.get("sourceEuids"),(String) previewDuplicatesMap.get("rowcount"));
   }
    
%>


<%
 //Final duplicates array list here
 finalArrayList = searchDuplicatesHandler.performSubmit();

%>
<% if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   
                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%><%=fac%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                             <%

													    keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														if(eoMultiMergePreviewHashMap.get(keyParam) == null ){
							
							                %>
                                                          <a title="<%=fieldValuesMapSource.get("EUID")%>" class="dupbtn" href="javascript:void(0)" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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
                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fieldValuesMapSource.get("EUID")%><%=fac%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%
													      keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														  if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                           <a title="<%=fieldValuesMapSource.get("EUID")%>" class="dupbtn" href="javascript:void(0)" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 

                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
												
                                                 	<%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

												        <a title="<h:outputText value="#{msgs.potential_dup_button}"/>" class="diffviewbtn" href="javascript:void(0)" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                         <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
														<%}%>

												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%
										keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
											if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									 	   <script>
											   document.getElementById('mainEuidContentDiv'+'<%=eoMultiMergePreviewHashMap.get("destinationEuid"+new Integer(fac).toString())%>').className='blue';
										    </script>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();
												  keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%
													 keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
													 if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">


													  <nobr>
                                                        <!--div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
                                                               <a  class="dupbtn" href="javascript:void(0)" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
                                                           </div-->

                                                          <form id="multiMergeFinal<%=fac%>" name="multiMergeFinal<%=fac%>">
														    <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
															  if(eoMultiMergePreviewHashMap.get(keyParam)  != null ) {
                                                            %>
                                                       	     <%if(operations.isEO_Merge()) {%>
                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.source_submenu_merge}"/>"
															   onclick="javascript:getFormValuesMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?multiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                       	         <span><h:outputText value="#{msgs.source_submenu_merge}"/></span>
                                                       	       </a>
                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                        	     <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
                                                       	     <%}%>

															<%}%>
                                                            </form>   

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>" onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

//
<%} else if(isCancelMultiMergeEOs) {%>  <!--if  Cancel Multi Merge EOs -->


 <%
	 HashMap previewDuplicatesMap = new HashMap();
	 //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
		
	   if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) &&
			!("potentialDuplicateId".equalsIgnoreCase(attributeName)) &&
			!("resolveType".equalsIgnoreCase(attributeName)) 
		   ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
	   }
   } 

%>

<!-- redisplay the output here START-->
<%  
	//finalArrayList = searchDuplicatesHandler.resetOutputList(searchDuplicatesHandler.getPdSearchObject());
   //Final duplicates array list here
    finalArrayList = searchDuplicatesHandler.performSubmit();
	if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   

                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                        <%
														keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)"  title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														  if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
												  <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

                                                 <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
												  <%}%>
												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
										if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();

											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													 <nobr>
													    <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
																  
                                                       	       </a>
														 <%}%>
                                                        </div>

													  </nobr>
                                                  </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>"  onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

<%} else if(isMultiMergeEOs) {%>  <!--if MERGE EO's -->
 <%
	 HashMap previewDuplicatesMap = new HashMap();
	 //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
		
	   if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) &&
			!("potentialDuplicateId".equalsIgnoreCase(attributeName)) &&
			!("resolveType".equalsIgnoreCase(attributeName)) 
		   ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
	   }
   } 

//parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("destnEuidValue".equalsIgnoreCase(attributeName)) ||
           ("rowcount".equalsIgnoreCase(attributeName)) ||
           ("sourceEuids".equalsIgnoreCase(attributeName))
		  
		  ) { 
		   previewDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   if(previewDuplicatesMap.keySet().toArray().length  > 0) {
     searchDuplicatesHandler.performMultiMergeEnterpriseObject((String) previewDuplicatesMap.get("destnEuidValue"), (String) previewDuplicatesMap.get("sourceEuids"),(String) previewDuplicatesMap.get("rowcount"));
   }
    
%>

<!-- redisplay the output here START-->
<%  
	//finalArrayList = searchDuplicatesHandler.resetOutputList(searchDuplicatesHandler.getPdSearchObject());
   //Final duplicates array list here
    finalArrayList = searchDuplicatesHandler.performSubmit();
	if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   

                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                        <%
														keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														  if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
												  <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

                                                 <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
												  <%}%>
												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
										if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();

											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													  <nobr>
													    <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
														 <%}%>                       
														 </div>

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>"  onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

<%} else if(isUnresolveDuplicate) {%>  <!--if Resolve Duplicate-->
 <%
	 HashMap resolveDuplicatesMap = new HashMap();
	 //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
		
	   if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) &&
			!("potentialDuplicateId".equalsIgnoreCase(attributeName)) &&
			!("resolveType".equalsIgnoreCase(attributeName)) 
		   ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
	   }
   } 

//parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("potentialDuplicateId".equalsIgnoreCase(attributeName)) ||
		  ("resolveType".equalsIgnoreCase(attributeName)) ) { 
		   resolveDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   searchDuplicatesHandler.unresolvePotentialDuplicateAction(resolveDuplicatesMap);
    
%>

<!-- redisplay the output here START-->
<%  
	//finalArrayList = searchDuplicatesHandler.resetOutputList(searchDuplicatesHandler.getPdSearchObject());
   //Final duplicates array list here
    finalArrayList = searchDuplicatesHandler.performSubmit();
	if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   

                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                        <%
														keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                        <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                            <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														  if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
												  <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

                                                 <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
												  <%}%>
												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
										if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();

											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													  <nobr>
                                                        <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
														 <%}%>                       
														 </div>

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>"  onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

<%} else if(isResolveDuplicate) {%>  <!--if Resolve Duplicate-->
 <% HashMap resolveDuplicatesMap = new HashMap();
	 //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
		
	   if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) &&
			!("potentialDuplicateId".equalsIgnoreCase(attributeName)) &&
			!("resolveType".equalsIgnoreCase(attributeName)) 
		   ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
	   }
   } 


//parameterNamesResolve
   while(parameterNamesResolve.hasMoreElements())   { 
    String attributeName = (String) parameterNamesResolve.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	   //potentialDuplicateId=00000000000000065000, create_end_date=, resolveDuplicate=true, create_start_date=05/12/2008, resolveType=AutoResolve
      if ( ("potentialDuplicateId".equalsIgnoreCase(attributeName)) ||
		  ("resolveType".equalsIgnoreCase(attributeName)) ) { 
		   resolveDuplicatesMap.put(attributeName,attributeValue);			
	   }
   } 

   searchDuplicatesHandler.resolvePotentialDuplicate(resolveDuplicatesMap);
    
%>

<script>
	 document.getElementById('resolvePopupDiv').style.visibility = 'hidden';
	 document.getElementById('resolvePopupDiv').style.display = 'none';
</script>

<!-- redisplay the output here START-->
<%  
	//finalArrayList = searchDuplicatesHandler.resetOutputList(searchDuplicatesHandler.getPdSearchObject());
   //Final duplicates array list here
    finalArrayList = searchDuplicatesHandler.performSubmit();
	if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   

                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                        <%
                                                        keyParam = "eoMultiMergePreview" + new Integer(fac).toString();

														if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
														  if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
                                                   <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

												        <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                             <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
														<%}%>
												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%keyParam = "eoMultiMergePreview" + new Integer(fac).toString();
											if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();
											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													  <nobr>
													      <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
														 <%}%>                       
														 </div>

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>"  onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>


<!-- redisplay the output here END-->


<%}else if(iscompareEuids) {%>  <!--if is compare euids case -->
  <%
    //build the arraylist of compare euids and navigate to the compare duplicates page.
    searchDuplicatesHandler.buildCompareDuplicateEuids(collectedEuids);
  %>
<table>
  <tr>
  <td>
  <script>
    window.location = '/<%=URI%>/compareduplicates.jsf';
 </script>
  </td>
 </tr>
</table>
<%} else {%> <!--other wise-->


<% //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
		     searchDuplicatesHandler.getParametersMap().put(attributeName,attributeValue);			
      }
   } 

%>
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>

<%
 //Final duplicates array list here
 finalArrayList = searchDuplicatesHandler.performSubmit();

%>
<% if (finalArrayList != null)   {
%>

 <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px;align:right;width:100%;border: 1px solid #6B757B;"> 
         <tr>
           <td  align="left" width="50%">
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=finalArrayList.size()%>
            </td>
            <td colspan="2" style="align:left;width:50%">
				<% if (finalArrayList != null && finalArrayList.size() > 0)   {%>
                         <h:outputLink styleClass="button" title="#{msgs.print_text}" 
                                       rendered="#{Operations.potDup_Print}"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>             
				<% } %>
            </td>
			</tr>
       </table>    
<% } %>


<% if (finalArrayList != null && finalArrayList.size() > 0 )   {%>
 <!-- Output here -->
<table>
<tr><td>
<script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";		 
</script>
</td>
</tr>
</table>



                <br>   
                  <%
                     if(finalArrayList != null && finalArrayList.size() >0 ) {						
						finalArrayListVE = ExpressionFactory.newInstance().createValueExpression(finalArrayList, finalArrayList.getClass());
						

                         request.setAttribute("finalArrayList", request.getAttribute("finalArrayList"));
                 %>                
                 <%}%>
                <%
                    if(finalArrayList != null && finalArrayList.size() >0 ) {

                %>
                <div id="dataDiv" style="overflow:auto;height:1024px;border: 1px solid #6B757B;">
                    <div>
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                            <%
                            if(finalArrayList != null && finalArrayList.size() >0 ) {
                                
                                for(int fac=0;fac<finalArrayList.size();fac++) {
                                   
                                %>
                            <div id="mainEuidDiv<%=countMain%>">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="images/spacer.gif" width="15"></td>
                                        <%
                                        HashMap resultArrayMapMain = new HashMap();
                                        HashMap resultArrayMapCompare = new HashMap();
                                        String epathValue;

                                        ArrayList arlInner = (ArrayList) finalArrayList.get(fac);
									    //accumilate the duplicate euids here
                                        StringBuffer arlInnerEuids = new StringBuffer();
                                        String subscripts[] = compareDuplicateManager.getSubscript(arlInner.size());
                                        for (int j = 0; j < arlInner.size(); j++) {
                                               HashMap eoHashMapValues = (HashMap) arlInner.get(j);

                                               //int weight = ((Float) eoHashMapValues.get("Weight")).intValue();
                                               String  weight =  eoHashMapValues.get("Weight").toString();
                                               String  potDupStatus = (String) eoHashMapValues.get("Status");
                                               String  potDupId = (String) eoHashMapValues.get("PotDupId");

   						                        potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());
                                               //weight = (new BigDecimal(weight)).ROUND_CEILING;
                                               //float weight = ((Float) eoHashMapValues.get("Weight")).floatValue();
                                               
                                               HashMap fieldValuesMapSource = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_PREVIEW");
											   fieldValuesMapSource.put("EUID",eoHashMapValues.get("EUID"));

                                               arlInnerEuids.append((String) eoHashMapValues.get("EUID") + ",");
											   //arlInnerEuids.add((String) eoHashMapValues.get("EUID") );
											   
                                               // Code to render headers
                                               if (j>0)
                                                {    dupHeading = "<b> "+j+"<sup>"+subscripts[j] +"</sup> Duplicate </b>";
                                                } else if (j==0)
                                                {    dupHeading = "<b> Main EUID</b>";
                                                }
                                               //String strDataArray = (String) arlInner.get(j);
                                               //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);
                                               //HashMap fieldValuesMapSource = compareDuplicateManager.getEOFieldValues(eoSource, objScreenObject) ;
                                               for (int ifc = 0; ifc < resultsConfigFeilds.length; ifc++) {
                                                FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc];
                                                if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                    epathValue = fieldConfigMap.getFullFieldName();
                                                } else {
                                                    epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                }
                                                if (j > 0) {
                                                    resultArrayMapCompare.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                } else {
                                                    resultArrayMapMain.put(epathValue, fieldValuesMapSource.get(epathValue));
                                                }
                                              }
                                        
					                
                                               
                                        %>
                                       <%if(j == 0 ) {%>
                                        <td valign="top">
                                            <div id="mainEuidContent">
                                                <table border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td valign="top" style="width:100%;height:45px;border-bottom: 1px solid #EFEFEF; ">&nbsp;</td>
                                                    </tr> 
                                                </table>
                                            </div> 
                                             <div id="mainEuidContentDiv<%=countMain%>" class="dynaw169">
                                                <table border="0" cellspacing="0" cellpadding="0" class="w169">
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                             
                                                    %>
                                                    <tr><td><%=fieldConfigMap.getDisplayName()%></td></tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
													<tr><td>&nbsp</td></tr>
                                                </table>
                                            </div>   
                                        </td>
                                        <td valign="top">
                                            <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop">Main EUID</td>
                                                    </tr> 
                                                    <tr>
                                                        <td valign="top" class="dupfirst">
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>" onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>
														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

                                                        </td>
                                                    </tr>
                                                        
                                                </table>
												<div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
														epathValue = fieldConfigMap.getFullFieldName();
                                                        
                                                     %>
                                                    <tr>
                                                        <td>
														    <%if (fieldValuesMapSource.get(epathValue) != null) {%>

                                                              <%=fieldValuesMapSource.get(epathValue)%>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>

                                                        </td>                                                        
                                                    </tr>
                                                    <%}%>
													<tr><td>&nbsp</td></tr>
    												<tr><td>&nbsp;</td></tr>
                                              </table>
											  </div>
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

                                            <% 
                                                if ("R".equalsIgnoreCase(potDupStatus)) {       
                                                 %>
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="deactivate" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else if ("A".equalsIgnoreCase(potDupStatus) ){%>        
                                                 <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="source" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%} else {%>        
                                                  <div id="mainEuidContentDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>" class="yellow" style="width:169px;overflow:auto;overflow-y:hidden;overflow-x:visible;width:169px;">
                                            <%}%>        

                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <tr>
                                                        <td valign="top" class="menutop"><%=dupHeading%> </td>
                                                    </tr> 
                                                    <tr>
                                                      <td valign="top" class="dupfirst">
   												      <%
                                                       if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
		        			                            %>
                                                                <%=fieldValuesMapSource.get("EUID")%>
    
                                                      <%} else {%>        
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) == null ){%>
                                                          <a  class="dupbtn" href="javascript:void(0)" title="<%=fieldValuesMapSource.get("EUID")%>"
														  onclick="javascript:accumilateMultiMergeEuidsPreviewDuplicates('<%=fac%>','<%=j%>','<%=fieldValuesMapSource.get("EUID")%>');"> 
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </a>

														<%} else {%>
                                                            <span class="dupbtn" href="javascript:void(0)">
                                                                <%=fieldValuesMapSource.get("EUID")%>
                                                            </span>
														<%} %>

													  <%} %>        

                                                      </td>
                                                    </tr>
                                                </table>
                                            <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar"  title="<%=weight%>"   style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar"  title="<%=weight%>"   style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar"  title="<%=weight%>"   style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar"  title="<%=weight%>"   style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
										 <div id="mainEuidDataDiv<%=fac%><%=j%><%=fieldValuesMapSource.get("EUID")%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                    <%
                                                     for(int ifc=0;ifc<resultsConfigFeilds.length;ifc++) {
                                                        FieldConfig fieldConfigMap = (FieldConfig) resultsConfigFeilds[ifc]; 
                                                        epathValue = fieldConfigMap.getFullFieldName();                                            %>
                                                    <tr>                                                        
                                                       <td> 
                                                                <%if (fieldValuesMapSource.get(epathValue) != null) {%>
                                                                
                                                                <%if ((j > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {

                                                                %>
                                                                    
                                                                    <font class="highlight">
                                                                        <%=fieldValuesMapSource.get(epathValue)%>
                                                                    </font>
                                                                <%} else {
                                                                %>
                                                                    <%=fieldValuesMapSource.get(epathValue)%>
                                                                <%}%>
                                                                <%} else {%>
                                                                &nbsp;
                                                                <%}%>
                                                                

                                                        </td>        
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td class="align:right;padding-left:150px;" >
												<%
                                                if (("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
					                            %>
                                                  <%if(operations.isPotDup_ResolveUntilRecalc() || operations.isPotDup_ResolvePermanently()) {%>

												        <a  class="diffviewbtn" href="javascript:void(0)" title="<h:outputText value="#{msgs.potential_dup_button}"/>" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?potentialDuplicateId=<%=potDupId%>&unresolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                                                         <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
												   <%}%>

												<%}else{%>
												<%
                                                String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												%>
												    	<%if(operations.isPotDup_Unresolve()) {%>

                                                           <a href="javascript:void(0)" title="<%=diff_person_heading_text%>" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															 <img src="./images/diff.gif" alt="<%=diff_person_heading_text%>" border="0">
                                                            </a>   
															<%}%>

												<%}%>

                                                         </td>
                                                   </tr>
                                                    <tr><td>&nbsp</td></tr>
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
                                      <td  valign="top">
									  <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {%>
									    <div id="previewEuidDiv<%=fac%>" class="blue" style="visibility:visible;display:block;">
									  <%} else {%>
   									    <div id="previewEuidDiv<%=fac%>" class="yellow" style="visibility:visible;display:block;">
									  <%}%>
                                          <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                                              <tr>
                                                  <td width="100%" class="menutop1">Preview</td>
                                              </tr>
												<%
 											      HashMap previewHashMap = new HashMap();
                                                  HashMap eoMapPreview = new HashMap();
											       if(eoMultiMergePreviewHashMap.get(keyParam) != null ) {
														previewHashMap  = (HashMap) eoMultiMergePreviewHashMap.get(keyParam);
														eoMapPreview = (HashMap) previewHashMap.get("ENTERPRISE_OBJECT_PREVIEW");
                                                   }%>

                                                 <tr>
                                                        <td>
                                                        <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
                                                          <b><%=previewHashMap.get("EUID")%></b>
														<%} else {%>
														  &nbsp;
														<%}%>														
														</td>
                                                </tr>
                                                <%
                                                   for (int i = 0; i < resultsConfigFeilds.length; i++) {
                                                     FieldConfig fieldConfig = (FieldConfig) resultsConfigFeilds[i];
                                                        epathValue = fieldConfig.getFullFieldName();                                            
                                                    %>

                                                    <tr>
                                                        <td>
														 <%if(eoMultiMergePreviewHashMap.get(keyParam) != null ){%>
														     <%if(eoMapPreview.get(epathValue) != null)  {%>
   														        <%=eoMapPreview.get(epathValue)%>
														      <%} else {%>
														        &nbsp;
														      <%}%>
														   <%} else {%>
														     &nbsp;
														   <%}%>
														</td>

                                                    </tr>
                                                <%}%>

                                                <tr>
                                                    <td valign="top" align="left">
													  <nobr>
													     <div id="buttonsDiv<%=fac%>" style="visibility:hidden;display:none;">
														    <%if(operations.isEO_Merge()) {%>

                                                               <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.preview_column_text}"/>" onclick="javascript:multiMergeEuidsPreview('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?previewMerge=true&random='+rand+'&'+queryStr,'outputdiv','');"> 
                                                                 <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                               </a>
                                                              <a  class="button" href="javascript:void(0)" title="<h:outputText value="#{msgs.cancel_but_text}"/>" onclick="javascript:getFormValuesCancelMerge('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?cancelMultiMergeEOs=true&random='+rand+'&'+queryStr,'outputdiv','');">
                                                                  <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                       	       </a>
														 <%}%>
														 </div>

													  </nobr>
                                                    </td>
                                                </tr>
                                                    
                                                    <tr>
                                                       <td valign="top" align="right">
                                                            <!--Show compare duplicates button-->
                                                         <%
                                                            //ValueExpression euidVaueExpressionList = //ExpressionFactory.newInstance().createValueExpression(arlInner, arlInner.getClass());
														    String finalEuidsString = arlInnerEuids.toString();
                                                            finalEuidsString = finalEuidsString.substring(0,finalEuidsString.length()-1);
                                                         %>
                                                              <a href="javascript:void(0)" class="downlink" title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>"  onclick="setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&collecteuid=<%=finalEuidsString%>&random='+rand+'&'+queryStr,'messages','')">  
															  </a>

														</td>
                                                    </tr>
                                                        

                                            </table>
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
            


<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>


<%}%>

  <%} %>  <!-- Session check -->
</f:view>
