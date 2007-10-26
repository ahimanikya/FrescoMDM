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
package com.sun.mdm.index.util;
import java.util.ResourceBundle;
import java.util.Properties;

/**
 * Reader of connector.properties
 * @author  dcidon
 */
public class ConnectorParamReader {
    
    private static ResourceBundle properties;
    /** File name of connector properties file used by EDM and Collab war/jar
     * to get MC connectivity information
     */
    public static final String PROPERTY_FILE_NAME = "com.sun.mdm.index.connector";  
    
    /**
     * Application instance name
     **/
    public static final String APPLICATION_INSTANCE_NAME = "eview-application-instance-name";  
    
    /**
     * primary object name
     **/
    public static final String PRIMARY_OBJECT_INSTANCE_NAME = "eview-object-instance-name";    
    
    /**
     * jndi initial context factory
     **/
    public static final String JNDI_INIT_CONTEXT_FACTORY = "jndi-initial-context-factory-name";    
    
    /**
     * jndi provider url
     **/
    public static final String JNDI_PROVIDER_URL = "provider-url";    
    
    /**
     * project name
     **/
    public static final String PROJECT_NAME = "project-name";   
    
    /**
     * integration server name
     **/
    public static final String INTEGRATION_SERVER_NAME = "integration-server-name";  
    
    /**
     * environment name
     **/
    public static final String ENVIRONMENT_NAME = "environment-name";  
    
    /**
     * logical host name
     **/
    public static final String LOGICAL_HOST_NAME = "logical-host-name";  
    
    /** Creates a new instance of ConnectorParamReader */
    private ConnectorParamReader() {
    }
    
    /** Get the resource bundle identified by PROPERTY_FILE_NAME.
     *
     * @return The resource bundle identified by PROPERTY_FILE_NAME.
     */
    private static ResourceBundle getResourceBundle() {
        if (properties == null) {
            properties = ResourceBundle.getBundle(PROPERTY_FILE_NAME);
        }
        return properties;
    }
    
    /** Get JNDI URL using property file in classpath.
     *
     * @return JNDI URL.
     */
    public static String getProviderUrl() {
        return getResourceBundle().getString(JNDI_PROVIDER_URL);
    }
    
    /** Get JNDI URL using given property file.
     *
     * @param props Property file.
     * @return JNDI URL.
     */
    public static String getProviderUrl(Properties props) {
        return props.getProperty(JNDI_PROVIDER_URL);
    }
    
    /** Get JNDI Initial Context Factory using property file in classpath.
     *
     * @return JNDI Initial Context Factory.
     */
    public static String getInitialContextFactory() {
        return getResourceBundle().getString(JNDI_INIT_CONTEXT_FACTORY);
    }
    
    /** Get JNDI Initial Context Factory using given property file.
     *
     * @param props Property file.
     * @return JNDI Initial Context Factory.
     */
    public static String getInitialContextFactory(Properties props) {
        return props.getProperty(JNDI_INIT_CONTEXT_FACTORY);
    }
       
    /** Get Object Type using property file in classpath.
     *
     * @return Object Type
     */
    public static String getApplicationInstance() {
    	String ret = getResourceBundle().getString(APPLICATION_INSTANCE_NAME);
    	if (ret.endsWith(".Application.Connectable")) {
    		int idx = ret.indexOf(".Application.Connectable");
    		ret = ret.substring(0, idx);
    	}
    	
    	return ret;
    }
    
    /** Get Application name.
     *
     * @return application name.
     */
    public static String getApplicationName() {
    	String str = getApplicationInstance();
        int offset = "eView.Application-".length();
        String substr = str.substring(offset);
    	   	
    	return substr;
    }
    
    /** Get Object Type using given property file .
     *
     * @param props property file
     * @return Object Type
     */
    public static String getApplicationInstance(Properties props) {
    	String ret = props.getProperty(APPLICATION_INSTANCE_NAME);
    	if (ret.endsWith(".Application.Connectable")) {
    		int idx = ret.indexOf(".Application.Connectable");
    		ret = ret.substring(0, idx);
    	}
    	
    	return ret;
    }
    
    /** Get instance of the primary object using property file in classpath.
     *
     * @return Instance of the primary object.
     */
    public static String getPrimaryObjectInstance() {
    	String ret = getResourceBundle().getString(PRIMARY_OBJECT_INSTANCE_NAME);
    	
    	return ret;
    }
    
    /** Get object name, such as Person, Company etc.
     *
     * @return Primary object name.
     */
    public static String getPrimaryObjectName() {
    	String str = getPrimaryObjectInstance();
       
    	return str;
    }
    
    /** Get Object Type using given property file.
     *
     * @param props Property file.
     * @return Name of the primary object instance.
     */
    public static String getPrimaryObjectInstance(Properties props) {
    	String ret = props.getProperty(PRIMARY_OBJECT_INSTANCE_NAME);
   	
    	return ret;
    }

    /** Get project name using given property file.
     *
     * @return Project name.
     */
    public static String getProjectName() {
        return getResourceBundle().getString(PROJECT_NAME);
    }    
    
    /** Get project name using given property file.
     *
     * @param props Property file.
     * @return Project name.
     */
    public static String getProjectName(Properties props) {
        return props.getProperty(PROJECT_NAME);
    }
    
    /** Get environment name using given property file.
     *
     * @return Environment name.
     */
    public static String getEnvironmentName() {
        return getResourceBundle().getString(ENVIRONMENT_NAME);
    }    
    
    /** Get environment name using given property file.
     *
     * @param props Property file
     * @return Environment name.
     */
    public static String getEnvironmentName(Properties props) {
        return props.getProperty(ENVIRONMENT_NAME);
    }    
    
    /** Get logical host name using given property file.
     *
     * @return Logical host name.
     */
    public static String getLogicalHostName() {
        return getResourceBundle().getString(LOGICAL_HOST_NAME);
    }    
    
    /** Get logical host name using given property file.
     *
     * @param props Property file.
     * @return Logical host name.
     */
    public static String getLogicalHostName(Properties props) {
        return props.getProperty(LOGICAL_HOST_NAME);
    }  
    
    /** Get integration server name using given property file.
     *
     * @return Integration server name.
     */
    public static String getIntegrationServerName() {
        return getResourceBundle().getString(INTEGRATION_SERVER_NAME);
    }    
    
    /** Get integration server name using given property file.
     *
     * @param props Property file.
     * @return Integration server name.
     */
    public static String getIntegrationServerName(Properties props) {
        return props.getProperty(INTEGRATION_SERVER_NAME);
    }  
}
