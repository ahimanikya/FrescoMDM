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
package com.sun.mdm.multidomain.project.wizard;

public class WizardProperties {
    public static final String PROJECT_DIR = "projdir"; //NOI18N
    public static final String NAME = "name"; //NOI18N
    public static final String SOURCE_ROOT = "sourceRoot"; //NOI18N

    public static final String SET_AS_MAIN = "setAsMain"; //NOI18N

    public static final String CONFIG_FILES_FOLDER = "configFilesFolder"; //NOI18N
    public static final String JAVA_ROOT = "javaRoot"; //NOI18N
    public static final String LIB_FOLDER = "libFolder"; //NOI18N
    public static final String J2EE_LEVEL = "1.5";
    /** Target DB */
    public static final String PROP_DATABASE = "MyDataBase";
    /** Target XA Transaction */
    public static final String PROP_TRANSACTION = "CONTAINER";
    /** Target Date Format */
    public static final String PROP_DATE_FORMAT = "MyDateFormat";

    // xml content of configuration files
    /** Multi-Domain Model definition file  */
    public static final String PROP_XML_MULTI_DOMAIN_MODEL_FILE = "Multi-Domain Model";
    /** Multi-Domain Web Manager  */
    public static final String PROP_XML_MULTI_DOMAIN_WEB_MANAGER_FILE = "Multi-Domain Web Manager";
    /** Database Systems */
    public static final String PROP_DBSCRIPT_CREATE_MAPPINGS = "Create Mappings";
    /** Database Code List */
    public static final String PROP_DBSCRIPT_CREATE_MULTI_DOMAIN = "Create Multi-Domain";
    /** Replace existing instance */
    public static final String PROP_REPLACE_INSTANCE = "No";
    
    public static final String SERVER_INSTANCE_ID = "serverInstanceID"; //NOI18N
}
