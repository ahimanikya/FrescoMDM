/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.report.client;

import com.sun.mdm.index.ejb.report.BatchReportGenerator;
import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 *
 * @author  rtam
 */
public class ReportClient {

    /**  user name */
    private static String username = null;
    
    /**  password  */
    private static String password = null;
    
    /**  output directory  */
    private static String outputDirectory = null;
    
    /**  config file  */
    private static String configFile = null;
    
    /**  application name  */
    private static String applicationName = null;
    
    /**  application name  */
    private static String appServer = null;
    
    /**  help flag */
    private static final String HELP_FLAG = "-h";

    /**  username flag */
    private static final String USERNAME_FLAG = "-u";

    /**  password flag */
    private static final String PASSWORD_FLAG = "-p";

    /**  output directory flag */
    private static final String OUTPUT_DIRECTORY_FLAG = "-d";

    /**  config file flag */
    private static final String CONFIG_FILE_FLAG = "-f";

    /**  maximum number of command line arguments */
    // TO DO:  modify to "9" when IS has been fixed
    private static final int MAX_ARGS_LEN = 5;

    private static ReportWriter reportWriter = new ReportWriter();
    
    /** Creates a new instance of Report */
    public ReportClient() {
    }
    
    /** Returns the value of a command line argument as indicated by the flag
     *  parameter.  For example, suppose args is "-p Password1".  If flag 
     *  is set to "-p", then "Password1" will be returned.
     *
     * @param args  command line to process
     * @param flag  flag to process
     * @returns value of the flag if found, null otherwise.
     */
    public static String getParamValue(String args[], String flag)  {
        if (args.length == 0 || flag == null)  {
            return null;
        }
        int len = args.length;
        for (int i = 0; i < len; i++)  {
            String arg = args[i];
            if (arg.compareToIgnoreCase(flag) == 0)  {
                if (i < (args.length - 1))  {
                    return args[i + 1];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**  Determine if a flag has been set in the command line.  
     *
     * @param args  command line to process
     * @param flag  flag to process
     * @returns true if found, false otherwise.
     */
    public static boolean checkFlag(String args[], String flag)  { 
        if (args.length == 0 || flag == null)  {
            return false;
        }
        int len = args.length;
        for (int i = 0; i < len; i++)  {
            String arg = args[i];
            if (arg.compareToIgnoreCase(flag) == 0)  {
                return true;
            }
        }
        return false;
    }

    /**  Display the help screen
     *
     * @param errorMessage   error message to print out
     */
    public static void displayHelp(String errorMessage)  {
        if (errorMessage != null)  {
            System.out.println(errorMessage + "\n");
        }
        System.out.println("ReportClient");
        System.out.println("\nUsage:");
        System.out.println("ReportClient -f ConfigurationFile -d OutputDirectory -h");
        //  TO DO:  reinstate when IS has been fixed
//        System.out.println("ReportClient -uUsername -pPassword -fConfigurationFile -dOutputDirectory -h");
//        System.out.println("\n-u:  Username for server connection");
//        System.out.println("-p:  Password for server connection");
        System.out.println("-f:  XML configuration file");
        System.out.println("-d:  Output directory for reports (overrides XML configuration file)");
        System.out.println("-h:  This help screen");
        System.out.println("All command line arguments are case-sensitive");
    }

    /**  Retrieve the JNDI context
     *
     * @param server  server
     * @returns  an initial context
     * @throws RuntimeException
     */
    public static InitialContext getJndiContext(String server) {
        
        InitialContext jndiContext = null;
        
        try {
        	Hashtable env = new Hashtable();
            //using com.sun.jndi.cosnaming.CNCtxFactory does not need vendor specific library.
            //it needs ejb stub on the class path.
            env.put(Context.INITIAL_CONTEXT_FACTORY, 
            		"com.sun.jndi.cosnaming.CNCtxFactory"); 
            //For IBM Websphere the PROVIDER_URL is corbaname:iiop:host:port/NameServiceServerRoot
            env.put(Context.PROVIDER_URL, server);
            jndiContext = new InitialContext( env );
        }catch (NamingException e) {
        	System.out.println("Could not create JNDI API context: " + e.toString());
	        throw new RuntimeException(e.getMessage());
        }
        return jndiContext; 
    }
   
    /**  Create a ReportGenerator instance
     *
     * @param jndiContext  JNDI context
     * @param applicationName  application Name
     * @returns a ReportGeneratorInstance
     * @throws Exception  if necessary
     */
    public static BatchReportGenerator getReportGenerator(Context jndiContext, String applicationName) 
            throws Exception {
        BatchReportGenerator reportGen = null;
        if (applicationName == null)  {
            throw new Exception("Error: applicationName cannot be null");
        }
        try {
            String lookupString = "ejb/" + applicationName + "BatchReportGenerator";
            reportGen = (BatchReportGenerator)jndiContext.lookup(lookupString);
            //BatchReportGeneratorHome home = (BatchReportGeneratorHome)PortableRemoteObject.narrow( obj, BatchReportGeneratorHome.class );
            //reportGen = home.create();
            
        } catch (Exception ne) {
            ne.printStackTrace();
        } 
        return reportGen;
    }
    
    /** Prompts the users if the want to continue and risk overwriting existing 
     *  report files.
     *
     * @param directoryPath  directory path
     * @throws IOException
     * @returns true if user wants to continue, false otherwise.
     */
    public static boolean promptToContinue(String directoryPath) throws IOException {
        System.out.println("Directory already exists: " + directoryPath);
        System.out.println("Generating reports will overwrite existing files");
        System.out.println("Do you wish to continue? [Y|N] ");
        BufferedReader stdin = new BufferedReader (new InputStreamReader(System.in));
        String reply = stdin.readLine();
        
        if (reply.compareToIgnoreCase("Y") == 0)  {
            return true;
        }  else  {
            return false;
        }
    }
    
    /**
     * Main entry point
     *
     * @param args -d OutputDirectory -f ConfigFile -h
     */
    public static void main(String args[]) {
    
        int argc = args.length;
        
        //  parse command line arguments
        if (args.length != 0)  {
            if (checkFlag(args, HELP_FLAG) == true)  {
                displayHelp("Help Screen");
                System.exit(0);
            }
            if (args.length > MAX_ARGS_LEN)  {
                displayHelp("Invalid number of arguments");
                System.exit(0);
            }
        }
        
        //  TO DO:  reinstate when IS has been fixed
//        username = getParamValue(args, USERNAME_FLAG);
//        password = getParamValue(args, PASSWORD_FLAG);
        configFile = getParamValue(args, CONFIG_FILE_FLAG);
        outputDirectory  = getParamValue(args, OUTPUT_DIRECTORY_FLAG);
        
        //  TO DO:  reinstate when IS has been fixed
/*        if (username == null)  {
            displayHelp("Error: Username is required");
            System.exit(0);
        }
        if (password == null)  {
            displayHelp("Error: Password is required");
            System.exit(0);
        }
*/        
        if (configFile == null)  {
            displayHelp("Error: Configuration File is required");
            System.exit(0);
        }
        
        //  Check if the configuration file exists.  
        
        File configurationFile = new File(configFile);
        if (configurationFile.exists() == false)  {
            displayHelp("Error: configuration file does not exist--> " + configFile);
            System.exit(0);
        }

        //  parse the configuration file
        ReportConfigurationReader cfgReader = null;
        ReportConfiguration config = null;
        try  {
            cfgReader = new ReportConfigurationReader();
            cfgReader.setInputXml(configFile);
            cfgReader.setupHandlers();
            config = cfgReader.parse();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
            
        //  check if output directory exists.  If not, create it.
        //  If command line argument overrides XML value, then use the command line argument

        if (outputDirectory == null)  {
            outputDirectory = config.getReportOutputFolder();
            if (outputDirectory == null)  {
                displayHelp("Error: output directory is required in either the "
                            + "command line or the configuration file");
                System.exit(0);
            }
        }
        File directory = new File(outputDirectory);
        
        //  Directory should be empty.  Otherwise, the reports will overwrite existing files.
        try  {
            if (directory.exists() == false)  {
                directory.mkdirs();
            }  else if (promptToContinue(outputDirectory) != true)  {
                System.out.println("Report generation aborted.");
                System.exit(0);
            }  
        }  catch (IOException e)  {
            e.printStackTrace();
            System.exit(0);
        }

        //  Generate the report(s)
        boolean retVal = false;
        BatchReportGenerator repgen = null;
        try  {
            System.out.println("Generating reports.  Please wait....");
            applicationName = config.getApplication();
            appServer = config.getApplicationServer();
            repgen = getReportGenerator(getJndiContext(appServer), applicationName);
            retVal = reportWriter.createReports(repgen, username, password, directory, cfgReader);
        } catch (Exception e)  {
            e.printStackTrace();
            System.exit(0);
        } catch (NoClassDefFoundError e2) {
            System.out.println("Failed to connect to Report Generator");
            System.exit(0);
        } finally {
            try {
//                repgen.remove();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        if (retVal == false)  {
            System.out.println("Error: report generator failed");
            System.out.println("Reports completed with error(s)");
            System.exit(0);
        }
        
        System.out.println("Report files saved in " + outputDirectory);
        System.out.println("Reports completed");
        System.exit(0);
    }

}
