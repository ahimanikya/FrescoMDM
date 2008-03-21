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
 * @(#)UserProfile.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.control;

import java.util.Set;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.security.SecurityManager;
import java.util.ArrayList;

/**
 * @author rtam
 */
public class UserProfile {

    private String mUserName;
    private String[] mRoles;
    private String[] mOperations;

    /** Constructor
     *
     * @param userName User name.
     * @param request HTTP request handle.
     * @throws Exception if an error is encountered
     */
    
    public UserProfile(String userName, HttpServletRequest request) throws Exception {
        mUserName = userName;
        
        // initialize the security manager if necessary.
        if (SecurityManager.getInstance() == null) {
            SecurityManager.init();
        }
        // set the roles for this Userprofile
        String allRoles[] = SecurityManager.getInstance().getAllRoles();
        //mRoles = new String[1];
        ArrayList mRolesList = new ArrayList();
        for (int i = 0, j = 0; i < allRoles.length; i++) {
            if(request.isUserInRole(allRoles[i])) {               
                //mRoles[j] = allRoles[i];   
                mRolesList.add(allRoles[i]);
            }
        }
        
        mRoles = new String[mRolesList.size()];
        
        for (int j = 0; j < mRolesList.size(); j++) {
             mRoles[j] = (String) mRolesList.get(j);
        }
        mOperations = SecurityManager.getInstance().getOperations(this);
    }

    /** 
     * Retrieves all the roles assigned to a UserProfile instance
     *
     * @return String array of all the roles
     */
    public String[] getRoles() {
        return mRoles;
    }

    /** 
     * Retrieves all operations for the roles assigned to a UserProfile instance.
     *
     * @return String array of all operations
     */
    public String[] getOperations() {
        return mOperations;
    }

    /**
     * Returns the mUserName attribute for a UserProfile instance.
     *
     * @return the user name
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Checks if an operation is allowed for a UserProfile instance.
     *
     * @param operation This is the name of the operation.
     * @return true if is allowed, false otherwise,
     */
    
    public boolean isAllowed(String operation) {
        for (int i = 0; i < mOperations.length; i++) {
            if (operation.equalsIgnoreCase(mOperations[i])) {
                return true;
            }
        }
        return false;
    }
    
}

