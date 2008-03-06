<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
        </head>
        <title>Login Error Page</title>  
        <body>
                        
            <div id="content">  <!-- Main content -->
            <table border="0" cellpadding="3" cellspacing="1">
                <tr>
                    <td> 
                        <div class="errorHeadMessage"> Login  Error</div> 
                        <div id="errorSummary" class="errorSummary"> 
                            <p>
                              Invalid username and/or password, please 
                              <a title="Logout and return to Login screen" href="login.jsf"> try again. </a>
                               
                               

                            </p>
                        </div>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
            
        </body>
    </html>
    
