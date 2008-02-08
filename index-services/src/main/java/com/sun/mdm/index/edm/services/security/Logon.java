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
 * Created on August 3, 2007, 3:06 PM
 * @author  rtam
 */

package com.sun.mdm.index.edm.services.security;

import com.sun.mdm.index.edm.control.QwsController;
import com.sun.mdm.index.edm.control.UserProfile;
import java.io.IOException;
import java.util.Locale;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.edm.util.DateUtil;
import com.sun.mdm.index.edm.util.QwsUtil;
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
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.service.security.Logon");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    /** Creates a new instance of Logon */
    public Logon() {
    }

    // logon 
    public static void  execute (String userid, String password)
        throws Exception {

        // Initialize the ConfigManager.
        try {
            ConfigManager.init();
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS500: An error occurred in the ConfigManager: {0}", 
                                         QwsUtil.getRootCause(e).getMessage()));
        }

        // Initialize the SecurityManager.
        try {
            SecurityManager.init();
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS501: An error occurred in the SecurityManager: {0}", 
                                         QwsUtil.getRootCause(e).getMessage()));
        }

        // Check if MasterController is available
        // To get to MasterController, ConfigManager is required. 
        if (QwsController.getMasterController() == null 
            || QwsController.getValidationService() == null) {
            try {
                QwsController.init();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS502: Failed to instantiate the Master Controller: {0}", 
                                         QwsUtil.getRootCause(e).getMessage()));
            }
            // Obtain the length of the EUID fields from the MasterController
            int euidLength = 32;
            try {
                
                euidLength 
                    = ((Integer) QwsController.getMasterController()
                                              .getConfigurationValue("EUID_LENGTH"))
                                              .intValue();
            } catch (Exception e) {
                // UserException doesn't need a stack trace, and ProcessingException
                // stack trace is already logged in the MC.
                if (e instanceof UserException) {          
                    throw new Exception(mLocalizer.t("SRS503: Failed to get length of EUID: {0}", 
                                             QwsUtil.getRootCause(e).getMessage()));
                } else if (!(e instanceof ProcessingException)) {
                    throw new Exception(mLocalizer.t("SRS504: Failed to get length of EUID: {0}", 
                                             e.getMessage()));
                }
            }
            // Reset any EUID fields in the ScreenObjects to the 
            // correct length obtained from the MasterController
            ConfigManager.getInstance().setEuidLength(euidLength);
            // Re-initialize the ValidationService as it depends on 
            // CodeLookupService and MasterController
            try {
                ValidationService.init();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS505: Failed to instantiate CodeLookup manager: {0}", 
                                                 QwsUtil.getRootCause(e).getMessage()));
            }
            
            // Initialize the date format in DateUtil for EDM to convert 
            // date/string back and forth
            try {
                DateUtil.init();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS506: Failed to instantiate Date Utility: {0}", 
                                                 QwsUtil.getRootCause(e).getMessage()));
            }
        } else {
            // Restart the ValidationService and DateUtil in case there were 
            // problems with logging in the EDM or if the database connection 
            // was lost.  Otherwise, the EDM will not display values for 
            // the pull-down menus.
            try {
               ValidationService.init();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS507: Failed to instantiate CodeLookup manager: {0}", 
                                                 QwsUtil.getRootCause(e).getMessage()));
            }
            
            // initialize the date format in DateUtil for EDM to convert date/string back and forth
            try {
               DateUtil.init();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS508: Failed to instantiate Date Utility: {0}", 
                                                 QwsUtil.getRootCause(e).getMessage()));
            }
        }

        UserProfile userProfile = null;
        // TODO: The signOn still needs to be implemented       
/*        
        try {
            userProfile = QwsController.signOn(userid, request);
        } catch (Exception e) {
            mLogger.error("Logon failed: ", e);
            throw e;
        }
        if (userProfile == null) {
            mLogger.error("Invalid user/password. Please try again.");
        }  else {
            // TODO: setup screens for a user.
            // Check permissions before a user can access an screen or some
            // type of control (e.g. button, pull-down menu, etc.)
        }
*/
    }
}
