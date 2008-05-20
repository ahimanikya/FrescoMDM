UKPatient Solution
==================

Purpose of the UKPatient Solution
---------------------------------
The purpose of this solution is to demonstate how a United Kingdom Patient model might be 
implemented in the Master Data Management (MDM) product suite.  Additionally the solution 
demonstrates the capabilities of the MDM suite.
 
This solution uses a patient definition geared towards the United Kingdom.  Since this is a
UK Patient, fields such as the HealthID's IdNum and HealthID's IdNumType are used 
for both block-search definitions and matches.

Loading and examining the various files provided with this solution will give a good
basis for understanding the MDM product suite.
 

How to use the solution.
-----------------------
	1. Unzip UKPatient.zip.

	2. Open the following projects in Netbeans 6.1.
	
           TIP: You can open all of these by opening the main UKPatient project
           and selecting the "Open Required Projects" option. 
		UKPatient
		UKPatient-ejb
		UKPatient-war
		
	   NOTE: When you open the solution you may see the following errors.  These can both be ignored.
           
             o Deployment descriptor directory can not be found. It may have been removed or renamed.
           
             o Reference Problems 
             
               One or more project resources could not be found.
               Right-click the project in the Project window and choose
               Resolve Reference Problems to find the missing resources.
           
           Additionally the -ejb sub-project may be colored red. This is also to be expected and can 
           be ignored. 	

        3. Create the database.
	 	The solution creates a user called 'uk'. To create a different user,
		edit the createuser-uk.sql file.


		As a SYSDBA user run the createuser-uk.sql script.

		As the 'uk' user (password uk) run the following scripts. 
		TIP: Be sure to run the create.sql script first!

	 		create.sql
			codelist.sql
			systems.sql
			createIndexes.sql
			createUserCodeData.sql

        4. Make sure that the GlassFish server is started.

	5. Right click on the UK Patient project and select 
           "Generate Master Index Files".

	6. Build and deploy the UKPatient project.
		a) Right click on the UKPatient project and select 
		   "Build".
		b) Right click on the UKPatient project and select
		   "Undeploy and Deploy".

	7. Using the GlassFish Admin Console define the following:
	    Resources
		JDBC
		  Connection pool
			UKPatient
		  JDBC Resources
			jdbc/UKPatientDataSource

	    Configuration
		Security
		   	Realms
			   file
				a) Select "Manage Users"
				b) Select "New"
				    Enter the following values:
					User ID: 	uk
                                        Group List:     MasterIndex.Admin,Administrator
                                        Password:       uk
                          

       8. Bring up the MIDM GUI using the following URL:
               http://localhost:<http_port_default_8080>/UKPatientMIDM
               
          NOTE: If you changed the default port but can not remember the new port number do 
          one of the following:
            o From NetBeans 6.1 bring up the HTTP monitor. 
              The port is in the Client and Server tab shown as "Port number of HTTP server"
              
            o Edit the domain.xml file found under your JavaCaps6 install directory.  This file can be found
              in the following sub-directory: appserver\domains\domain1\config
              
              Find a line similar to the following example.  The line you are looking for will have the following values:
                  - http-listener acceptor-threads="1"
                  - default-virtual-server="server"
                  - security-enabled="false"
              Usually this is the first http-listener entry in the domain.xml file.
             
              Example (Notice in this example the port used is 9090 instead of the default HTTP port 8080).
                 <http-listener acceptor-threads="1" address="0.0.0.0" blocking-enabled="false" 
                   default-virtual-server="server" enabled="true" family="inet" id="http-listener-1" 
                   port="9090" security-enabled="false" server-name="" xpowered-by="true">
                   
       9. Log into the MIDM GUI using the username of 'uk' and the password of 'uk'.            

      10. Add new records:
          a) Select the "Source Record" tab.  
          b) Select the "Add" sub-tab.  
          c) Select "Sun" for the System and enter a unique Local ID, such as 444-444-4444.
          d) At a minimum specify the required values in the UKPatient Info section.
          e) Select "Submit" to add the new record.

Examples of what you might try
------------------------------
   o Add the same person but give them different dates of birth.
   o Add different people.
   o Add the same person but give them different Health ID data.
   o Check for duplicates.
   o Look at the various tabs and try different queries or actions once you have some 
     duplicates.		
			
Updating the UKPatient model
-----------------------------
   o Make the desired changes to the Master Index configuration files.
      
   o Right click on the UKPatient project and select "Generate Master Index Files".
      
   o Update the database definitions by running the following SQL commands as the 'uk' user.
     TIP: Be sure to run the drop.sql script first! Then run the create.sql script.
      
        drop.sql
        create.sql
	codelist.sql
	systems.sql
	createIndexes.sql      
        createUserCodeData.sql     
             
   o  Build and deploy the UKPatient project.      
        a) Right click on the UKPatient project and select
           "Clean and Build".
        b) Right click on the UKPatient project and select
           "Undeploy and Deploy".

Current restrictions on the UK Patient solution
-----------------------------------------------
As shipped the UK Patient solution uses the following date time format rather than the standard
European date/time format - MM/dd/yyyy.

This was done because of a known issue with the MIDM GUI's handling of the European date/format 
yyyy/MM/dd.  