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

import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

public class FoldersListSettings {

    private static FoldersListSettings INSTANCE = new FoldersListSettings();
    
    private static final String LAST_EXTERNAL_SOURCE_ROOT = "srcRoot";  //NOI18N

    private static final String NEW_PROJECT_COUNT = "newProjectCount"; //NOI18N

    private static final String SHOW_AGAIN_BROKEN_REF_ALERT = "showAgainBrokenRefAlert"; //NOI18N

    private FoldersListSettings() {
    }
    
    public String displayName() {
        return NbBundle.getMessage (FoldersListSettings.class, "TXT_WebProjectFolderList"); //NOI18N
    }

    public String getLastExternalSourceRoot () {
        return NbPreferences.forModule(FoldersListSettings.class)
                    .get(LAST_EXTERNAL_SOURCE_ROOT, null);
    }

    public void setLastExternalSourceRoot (String path) {
        NbPreferences.forModule(FoldersListSettings.class)
            .put(LAST_EXTERNAL_SOURCE_ROOT, path);
    }

    public int getNewProjectCount () {
        return NbPreferences.forModule(FoldersListSettings.class)
                    .getInt(NEW_PROJECT_COUNT, 0);
    }

    public void setNewProjectCount (int count) {
        NbPreferences.forModule(FoldersListSettings.class)
            .putInt(NEW_PROJECT_COUNT, count);
    }
    
    public boolean isShowAgainBrokenRefAlert() {
        return NbPreferences.forModule(FoldersListSettings.class)
                    .getBoolean(SHOW_AGAIN_BROKEN_REF_ALERT, true);
    }
    
    public void setShowAgainBrokenRefAlert(boolean again) {
        NbPreferences.forModule(FoldersListSettings.class)
            .putBoolean(SHOW_AGAIN_BROKEN_REF_ALERT, again);
    }

    public static FoldersListSettings getDefault () {
        return INSTANCE;
    }
}
