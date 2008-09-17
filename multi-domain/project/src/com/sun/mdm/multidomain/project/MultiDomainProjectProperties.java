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
package com.sun.mdm.multidomain.project;

import java.io.File;

/** Defines constants for properties used in Multi-Domain Master Index. 
 */
public class MultiDomainProjectProperties {
    public static final String MODULES_EXT_MDM_LOCATION = "modules" + File.separator + "ext" + File.separator + "mdm";
    public static final String MULTIDOMAIN_TEMPLATE_LOCATION = MODULES_EXT_MDM_LOCATION + File.separator + "repository" + File.separator + "templates";
    public static final String PLUGINS = "plug-ins";
    public static final String RELATIONSHIP_DEPLOYMENT_LOCATION = MODULES_EXT_MDM_LOCATION + File.separator + "relationship" + File.separator + PLUGINS;
    public static final String SECURITY_TEMPLATE_LOCATION = MODULES_EXT_MDM_LOCATION + File.separator + "security" + File.separator + PLUGINS;

    public static final String RELATIONSHIP_PLUGIN_LOCATION = RELATIONSHIP_DEPLOYMENT_LOCATION + File.separator + PLUGINS;
    public static final String SCHEMA_TEMPLATE_LOCATION = MODULES_EXT_MDM_LOCATION + File.separator + "repository" + File.separator + "schema";
    /** Auto Generate */
    public static final String AUTO_GENERATE = "AutoGenerate";

    public static final String CONF_FOLDER = "conf"; //NOI18N    
    // Master Index Configuration Folder
    public static final String CONFIGURATION_FOLDER = "Configuration"; //NOI18N
    // Master Index Match Engine Folder
    public static final String SCHEMA_FOLDER = "schema"; //NOI18N
    // Master Index Database Script Folder
    public static final String DATABASE_SCRIPT_FOLDER = "DatabaseScript"; //NOI18N
    // Master Index Custom Plug-ins Folder
    public static final String RELATIONSHIP_PLUGINS_FOLDER = "Plug-ins"; //NOI18N
    public static final String J2EE_MODULES_FOLDER = "J2EE Modules"; //NOI18N
    
    public static final String RELATIONSHIP_MODEL_XML = "RelationshipModel.xml";
    public static final String RELATIONSHIP_MODEL_XSD = "RelationshipModel.xsd";
    public static final String RELATIONSHIP_WEB_MANAGER_XML = "RelationshipWebManager.xml";
    public static final String RELATIONSHIP_WEB_MANAGER_XSD = "RelationshipWebManager.xsd";
    public static final String SECURITY_XML = SECURITY_TEMPLATE_LOCATION + File.separator + "security.xml";
    public static final String SECURITY_XSD = "security.xsd";
    
    public static final String CREATE_RELATIONSHIPS_SQL = "CreateRelationships.sql";
    public static final String CREATE_MAPPINGS_SQL = "CreateMappings.sql";
    
    public static final String TEMPLATE_PROJECT = "EviewApplicationTemplateProject.zip";
    public static final String TEMPLATE_ZIP = MULTIDOMAIN_TEMPLATE_LOCATION + File.separator + TEMPLATE_PROJECT;
    
    public static final String MULTIDOMAIN_GENERATED_FOLDER = "files-generated";
    public static final String REPORT_CLIENT_FOLDER = "report-client";
    public static final String MULTIDOMAIN_JBI_JAR = "jbi.jar";
    public static final String SE_DEPLOYMENT_JAR ="sedeployment.jar"; 
    public static final String JBI_SE_TYPE = "com.sun.jbi.ui.devtool.jbi.setype.prefix";
    public static final String ARTIFACT_TYPE_JBI_ASA = "CAPS.asa";
    public static final String JAVA_EE_SE_COMPONENT_NAME = "sun-javaee-engine";

    //following properties are from ear project
    public static final String WEB_PROJECT_NAME = "web.project.name"; //NOI18N
    public static final String JAVA_PLATFORM = "platform.active"; //NOI18N
    public static final String J2EE_PLATFORM = "j2ee.platform"; //NOI18N
    public static final String SOURCE_ROOT = "source.root"; //NOI18N
    public static final String BUILD_FILE = "buildfile"; //NOI18N
    public static final String LIBRARIES_DIR = "lib.dir"; //NOI18N
    public static final String DIST_DIR = "dist.dir"; //NOI18N
    public static final String DIST_JAR = "dist.jar"; //NOI18N
    public static final String JAVAC_CLASSPATH = "javac.classpath"; //NOI18N
    public static final String DEBUG_CLASSPATH = "debug.classpath";     //NOI18N
    public static final String RUN_CLASSPATH = "run.classpath";
    public static final String JAR_NAME = "jar.name"; //NOI18N
    public static final String JAR_COMPRESS = "jar.compress"; //NOI18N
    public static final String JAR_CONTENT_ADDITIONAL = "jar.content.additional"; //NOI18N
    public static final String LAUNCH_URL_RELATIVE = "client.urlPart"; //NOI18N
    public static final String DISPLAY_BROWSER = "display.browser"; //NOI18N
    public static final String CLIENT_MODULE_URI = "client.module.uri"; //NOI18N
    public static final String J2EE_SERVER_INSTANCE = "j2ee.server.instance"; //NOI18N
    public static final String J2EE_SERVER_TYPE = "j2ee.server.type"; //NOI18N
    public static final String J2EE_PLATFORM_CLASSPATH = "j2ee.platform.classpath";//NOI18N 
    public static final String J2EE_PLATFORM_WSCOMPILE_CLASSPATH="j2ee.platform.wscompile.classpath"; //NOI18N
    public static final String JAVAC_SOURCE = "javac.source"; //NOI18N
    public static final String JAVAC_DEBUG = "javac.debug"; //NOI18N
    public static final String JAVAC_DEPRECATION = "javac.deprecation"; //NOI18N
    public static final String JAVAC_TARGET = "javac.target"; //NOI18N
    public static final String SRC_DIR = "src.dir"; //NOI18N
    public static final String META_INF = "meta.inf"; //NOI18N
    public static final String RESOURCE_DIR = "resource.dir"; //NOI18N
    public static final String WEB_DOCBASE_DIR = "web.docbase.dir"; //NOI18N
    public static final String BUILD_DIR = "build.dir"; //NOI18N
    public static final String BUILD_ARCHIVE_DIR = "build.archive.dir"; //NOI18N
    public static final String BUILD_GENERATED_DIR = "build.generated.dir"; //NOI18N
    public static final String BUILD_CLASSES_DIR = "build.classes.dir"; //NOI18N
    public static final String BUILD_CLASSES_EXCLUDES = "build.classes.excludes"; //NOI18N
    public static final String DIST_JAVADOC_DIR = "dist.javadoc.dir"; //NOI18N
    public static final String NO_DEPENDENCIES="no.dependencies"; //NOI18N    
    public static final String JAVADOC_PRIVATE="javadoc.private"; //NOI18N
    public static final String JAVADOC_NO_TREE="javadoc.notree"; //NOI18N
    public static final String JAVADOC_USE="javadoc.use"; //NOI18N
    public static final String JAVADOC_NO_NAVBAR="javadoc.nonavbar"; //NOI18N
    public static final String JAVADOC_NO_INDEX="javadoc.noindex"; //NOI18N
    public static final String JAVADOC_SPLIT_INDEX="javadoc.splitindex"; //NOI18N
    public static final String JAVADOC_AUTHOR="javadoc.author"; //NOI18N
    public static final String JAVADOC_VERSION="javadoc.version"; //NOI18N
    public static final String JAVADOC_WINDOW_TITLE="javadoc.windowtitle"; //NOI18N
    public static final String JAVADOC_ENCODING="javadoc.encoding"; //NOI18N
    public static final String JAVADOC_PREVIEW="javadoc.preview"; //NOI18N
    public static final String COMPILE_JSPS = "compile.jsps"; //NOI18N    
    public static final String EJB_DIR = "ejb.dir";
    public static final String WAR_DIR = "war.dir";
    public static final String TAG_WEB_MODULE_LIBRARIES = "web-module-libraries"; // NOI18N
    public static final String TAG_WEB_MODULE__ADDITIONAL_LIBRARIES = "web-module-additional-libraries"; //NOI18N
    public static final String DEPLOY_ANT_PROPS_FILE = "deploy.ant.properties.file"; // NOI18N    
    public static final String ANT_DEPLOY_BUILD_SCRIPT = "nbproject" + File.separator + "ant-deploy.xml"; // NOI18N
    public static final String J2EE_PLATFORM_WSGEN_CLASSPATH="j2ee.platform.wsgen.classpath"; //NOI18N
    public static final String J2EE_PLATFORM_WSIMPORT_CLASSPATH="j2ee.platform.wsimport.classpath"; //NOI18N
    public static final String J2EE_PLATFORM_JSR109_SUPPORT = "j2ee.platform.is.jsr109"; //NOI18N
    
}
