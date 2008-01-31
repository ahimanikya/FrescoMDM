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
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;


/**
 *
 * @author  rtam
 */
public class Logon {
    
    // logger
    private static final Logger mLogger 
        = LogUtil.getLogger("com.sun.mdm.index.edm.service.security.Logon");
    
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
            mLogger.error("ConfigManager error: " 
                           + QwsUtil.getRootCause(e).getMessage());
            throw e;
        }

        // Initialize the SecurityManager.
        try {
            SecurityManager.init();
        } catch (Exception e) {
            mLogger.error("SecurityManager error: " 
                           + QwsUtil.getRootCause(e).getMessage());
            throw e;
        }

        // Check if MasterController is available
        // To get to MasterController, ConfigManager is required. 
        if (QwsController.getMasterController() == null 
            || QwsController.getValidationService() == null) {
            try {
                QwsController.init();
            } catch (Exception e) {
                mLogger.error("Failed to instantiate Master controller: " 
                              + QwsUtil.getRootCause(e).getMessage());
                throw e;
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
                    if (mLogger.isInfoEnabled()) {
                        mLogger.info("Failed to get length of EUID: " 
                                     + QwsUtil.getRootCause(e).getMessage());
                        throw e;
                    }
                } else if (!(e instanceof ProcessingException)) {
                    mLogger.error("Failed to get length of EUID: ", e);
                    throw e;
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
                mLogger.error("Failed to instantiate CodeLookup manager: " 
                              + QwsUtil.getRootCause(e).getMessage());
                throw e;
            }
            
            // Initialize the date format in DateUtil for EDM to convert 
            // date/string back and forth
            try {
                DateUtil.init();
            } catch (Exception e) {
                mLogger.error("Failed to instantiate Date Utility: " 
                               + QwsUtil.getRootCause(e).getMessage());
                throw e;
            }
        } else {
            // Restart the ValidationService and DateUtil in case there were 
            // problems with logging in the EDM or if the database connection 
            // was lost.  Otherwise, the EDM will not display values for 
            // the pull-down menus.
            try {
               ValidationService.init();
            } catch (Exception e) {
                mLogger.error("Failed to instantiate CodeLookup manager: " 
                              + QwsUtil.getRootCause(e).getMessage());
                throw e;
            }
            
            // initialize the date format in DateUtil for EDM to convert date/string back and forth
            try {
               DateUtil.init();
            } catch (Exception e) {
                mLogger.error("Failed to instantiate Date Utility: " 
                               + QwsUtil.getRootCause(e).getMessage());
                throw e;
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
