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
package com.sun.mdm.index.project.ui.wizards;

import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import java.awt.Toolkit;

/**
 * Wizard properties.
 *
 */
public abstract class Properties {
    /** Target View Name */
    public static final String PROP_TARGET_VIEW_NAME = "MyView";
    /** Object Name */
    public static final String PROP_OBJECT_NAME = "MyObject";
    /** Target DB */
    public static final String PROP_DATABASE = "MyDataBase";
    /** Target match engine */
    public static final String PROP_MATCH_ENGINE = "MyMatchEngine";
    /** Target Date Format */
    public static final String PROP_DATE_FORMAT = "MyDateFormat";
    /** Participating source systems */
    public static final String PROP_SOURCE_SYSTEMS = "MySourceSystems";
    /** Target XA Transaction */
    public static final String PROP_TRANSACTION = "CONTAINER";
    /** Target XA Transaction */
    public static final String PROP_MASTER_INDEX_EDM = "MasterIndexEDM";

    // xml content of configuration files
    /** Object definition file  */
    public static final String PROP_XML_OBJECT_DEF_FILE = "Object";
    /** Enterprise Data Manager  */
    public static final String PROP_XML_EDM_CONFIG_FILE = "EDM";
    /** Enterprise Data Manager  */
    public static final String PROP_XML_MIDM_CONFIG_FILE = "MIDM";
    /** MEFA file */
    public static final String PROP_XML_MEFA_CONFIG_FILE = "Mefa";
    /** Master config file  */
    public static final String PROP_XML_MASTER_CONFIG_FILE = "Master";
    /** Update config */
    public static final String PROP_XML_UPDATE_CONFIG_FILE = "Update";
    /** Query config */
    public static final String PROP_XML_QUERY_CONFIG_FILE = "Query";
    /** Validate config */
    public static final String PROP_XML_VALID_CONFIG_FILE = "Validate";
    /** Security config */
    public static final String PROP_XML_SECURITY_CONFIG_FILE = "Security";
    /** Database Systems */
    public static final String PROP_DBSCRIPT_SYSTEMS = "Systems";
    /** Database Code List */
    public static final String PROP_DBSCRIPT_CODELIST = "Code List";
    /** Replace existing instance */
    public static final String PROP_REPLACE_INSTANCE = "No";
    /** Auto Generate */
    public static final String PROP_AUTO_GENERATE = "No";
    
    public static final String SERVER_INSTANCE_ID = "serverInstanceID"; //NOI18N

    private static String mDatabase;

    public static void setDatabase(String database) {
        mDatabase = database;
    }
    
    public static String getDatabase() {
        return mDatabase;
    }

    public static boolean checkNodeNameValue(String value) {
        boolean bRet = true;
        NotifyDescriptor desc;
        if (value.length() == 0) {
            Toolkit.getDefaultToolkit().beep();

            desc = new NotifyDescriptor.Message(NbBundle.getMessage(Properties.class, "MSG_Name_Cannot_Be_Empty"));
            desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
            desc.setTitle("Error");
            desc.setTitle(NbBundle.getMessage(Properties.class, "MSG_Error"));
            DialogDisplayer.getDefault().notify(desc);
            return false;
        }
        if (value.length() > 20 && !mDatabase.equalsIgnoreCase("MySQL")) {
            Toolkit.getDefaultToolkit().beep();
            String prompt = NbBundle.getMessage(Properties.class, "MSG_Long_Field_Name", value);
            desc = new NotifyDescriptor.Message(prompt);
            desc.setMessageType(NotifyDescriptor.WARNING_MESSAGE);
            desc.setTitle(NbBundle.getMessage(Properties.class, "MSG_Warning"));
            DialogDisplayer.getDefault().notify(desc);
        }

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' || c == ' ') {
                Toolkit.getDefaultToolkit().beep();
                String msgC = "" + c;
                String msg = NbBundle.getMessage(Properties.class, "MSG_Invalid_Character", msgC);
                desc = new NotifyDescriptor.Message(msg);
                desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
                desc.setTitle(NbBundle.getMessage(Properties.class, "MSG_Error"));
                DialogDisplayer.getDefault().notify(desc);
                bRet = false;
                break;
            }
        }
        return bRet;
    }
}
