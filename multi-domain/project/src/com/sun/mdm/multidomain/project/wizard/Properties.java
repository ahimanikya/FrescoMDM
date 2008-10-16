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


/**
 * Wizard properties.
 *
 */
public abstract class Properties {
    /** Target DB */
    public static final String PROP_DATABASE = "MyDataBase";
    /** Target XA Transaction */
    public static final String PROP_TRANSACTION = "CONTAINER";
    /** Target Date Format */
    public static final String PROP_DATE_FORMAT = "MyDateFormat";

    // xml content of configuration files
    /** Relationship Model definition file  */
    public static final String PROP_XML_MULTI_DOMAIN_MODEL_FILE = "Relationship Model";
    /** Relationship Manager  */
    public static final String PROP_XML_RELATIONSHIP_MANAGER_FILE = "Relationship Manager";
    /** Database Systems */
    public static final String PROP_DBSCRIPT_CREATE_MAPPINGS = "Create Mappings";
    /** Database Code List */
    public static final String PROP_DBSCRIPT_CREATE_RELATIONSHIPS = "Create Relationships";
    /** Replace existing instance */
    public static final String PROP_REPLACE_INSTANCE = "No";
    
    public static final String SERVER_INSTANCE_ID = "serverInstanceID"; //NOI18N
    
}
