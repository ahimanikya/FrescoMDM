<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <META http-equiv="Content-Type" content="text/html; charset=UTF-8">  
     
    <link rel="stylesheet" type="text/css" href=".../../css/styles.css" media="screen"/>
    			                     
    </head>    
    <body>
    <div id="head">&nbsp;</div>
    <br clear="all">
    <br>
    <br> 
    <p>This is <f:message key="webpage.title" /> welcome page based on Spring Web MVC.</p><br> 
    <i>This is just a template page which displays concepts of  <f:message key="webpage.title" /> without any expected UI and look-and feel design. The expected page flow should be a login page and then follows a landing page through which a user accesses to different functional pages based on the security roles. To display a finalized page of the project, modify</i> <tt>index.jsp</tt> <i>, or create your own welcome page then change the redirection in </i><tt>redirect.jsp</tt> <i>to point to the finalized RMWC page and also update the welcome-file setting in </i><tt>web.xml</tt>. 
    <i>The project development is in progress.</i>
    <i>The supported browsers are Internet Explorer, Mozilla Firefox, Safari, Chrome, ...</i>                
    <p>   
    <i>
    &emsp;Tools and specifications used: 
    <ul>    
        <li>DoJo 1.2.0</li>
        <li>DWR 2.0</li>
        <li>Spring MVC 2.5.5</li>        
        <li>Java EE 5 (JSP 2.1, Java Servlet 2.5, EJB 3.0, JAX-WS 2.0, etc.)</li>                                        
    </ul>    
    </i>
    </p>
    <p>Click following links to access different functional pages:</p>    
    <ul>    
        <li><a href="<c:url value="managerelationshiptype.htm" />">RelationshipType Management Page</a></li>
        <li><a href="<c:url value="managerelationship.htm" />">Relationship Management Page</a></li>
        <li><a href="<c:url value="managehierarchytype.htm" />">HierarchyType Management Page</a></li>
        <li><a href="<c:url value="managehierarchy.htm" />">Hierarchy Management Page</a></li>
    </ul>    
    </body>
</html>
