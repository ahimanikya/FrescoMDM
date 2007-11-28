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
package com.sun.mdm.index.project.ui;

import org.openide.loaders.DataFolder;
import org.openide.util.actions.SystemAction;

import javax.swing.Action;

public class EviewStandardizationFolderNode extends EviewFolderNode {
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            EviewStandardizationFolderNode.class.getName()
        
        
        
        );
    private DataFolder folder;
    
    public EviewStandardizationFolderNode(String displayName, DataFolder folder) {
        super(displayName, folder);
        this.folder = folder;
        this.getCookieSet().add(new EviewStandardizationFolderCookie(this));                
    }
    
    @Override
    public Action[] getActions( boolean context ) {
        return new Action[] {
            SystemAction.get(ImportStandardizationDataTypeAction.class),
            null,
            SystemAction.get(org.openide.actions.FindAction.class),
            null,
            SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
        };
    }
}
