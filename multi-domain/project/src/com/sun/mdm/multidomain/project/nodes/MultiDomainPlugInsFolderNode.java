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
package com.sun.mdm.multidomain.project.nodes;

import org.openide.loaders.DataFolder;
import org.openide.util.actions.SystemAction;
import org.openide.filesystems.FileObject;
import javax.swing.Action;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

public class MultiDomainPlugInsFolderNode extends MultiDomainFolderNode {
    private static final Logger mLog = Logger.getLogger(
            MultiDomainPlugInsFolderNode.class.getName()
        
        );
    private DataFolder folder;
    

    public MultiDomainPlugInsFolderNode(String displayName, DataFolder folder) {
        super(displayName, folder);
        this.folder = folder;
    }
    
    @Override
    public Action[] getActions( boolean context ) {
        return new Action[] {
            SystemAction.get(ImportRelationshipPluginAction.class),
            null,
            SystemAction.get(org.openide.actions.FindAction.class),
            null,
            SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
        };
    }
    
    @Override      
    public FileObject getFileObject() {
        FileObject fo = folder.getPrimaryFile();
        return fo;
    }
}
