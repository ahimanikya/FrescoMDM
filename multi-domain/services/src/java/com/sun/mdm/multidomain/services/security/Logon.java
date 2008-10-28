/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)Logon.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */

/*
 * Logon.java
 *
 * Created on October 27, 2008, 3:06 PM
 * @author  rtam
 */

package com.sun.mdm.multidomain.services.security;

import com.sun.mdm.multidomain.services.configuration.MDConfigManager;
import com.sun.mdm.multidomain.services.core.ConfigException;

// testing--raymond tam
//import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.multidomain.services.security.util.DateUtil;
import com.sun.mdm.multidomain.services.security.util.QwsUtil;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 *
 * @author  rtam
 */
public class Logon {
    
    // logger
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.security.Logon");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of Logon */
    public Logon() {
    }

    /*
     *  Initialize the ConfigurationManager and SecurityManager.
     *
     *  @throws Exception if an error is encountered.
     */
    public static void  initializeConfigurationSecurity()
        throws Exception {

        // Initialize the MDConfigManager.
        try {
            MDConfigManager.getInstance().init();
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("SRS505: An error occurred in the Multi-Domain ConfigManager: {0}", 
                                         e.getMessage()));
        }

        // Initialize the SecurityManager.
        try {
            SecurityManager.init();
        } catch (Exception e) {
            throw new ConfigException(mLocalizer.t("SRS506: An error occurred in the SecurityManager: {0}", 
                                                   e.getMessage()));
        }
    }
    
    /*
     *  Initialize the QWS Controller, Validation Service, and Date Service
     *
     *  @throws Exception if an error is encountered.
     */

    public static void  initializeQWSControllerValidationDate ()
        throws Exception {

        try {
            QwsController.init();
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS507: Failed to instantiate the QWS Controller: {0}", 
                                     QwsUtil.getRootCause(e).getMessage()));
        }
        // testing--raymond tam
        //RESUME HERE
        // Re-initialize the ValidationService as it depends on CodeLookupService
/*        
        try {
            ValidationService.init();
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS505: Failed to instantiate CodeLookup manager: {0}", 
                                             QwsUtil.getRootCause(e).getMessage()));
        }
*/        
        // Initialize the date format in DateUtil for EDM to convert 
        // date/string back and forth
        try {
            DateUtil.init();
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS508: Failed to instantiate Date Utility: {0}", 
                                             QwsUtil.getRootCause(e).getMessage()));
        }
    }
}
