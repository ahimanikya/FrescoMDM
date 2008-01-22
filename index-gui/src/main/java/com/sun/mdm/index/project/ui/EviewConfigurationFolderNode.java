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
import org.openide.cookies.SaveCookie;
import org.netbeans.api.project.Project;

import javax.swing.Action;

import com.sun.mdm.index.project.EviewApplication;

/**
 *
 */
public class EviewConfigurationFolderNode extends EviewFolderNode {
    private DataFolder folder;
    private Project project = null;
    private String displayName;
    
    /**
     * Constructor folder node for Configuration (xml files) 
     */
    public EviewConfigurationFolderNode(String displayName, DataFolder folder, Project project) {
        super(displayName, folder);
        this.project = project;
        ((EviewApplication) this.project).setAssociatedNode(this);
        this.getCookieSet().add(new EviewConfigurationFolderCookieImpl(this));                
    }
    
    @Override
    public Action[] getActions(boolean context) {
        if (this.project == null) {
            return new Action[] {
                SystemAction.get(org.openide.actions.FindAction.class),
                null,
                SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
            };

        } else {
            return new Action[] {
                SystemAction.get(ApplicationEditAction.class),
                null,           
                SystemAction.get(org.openide.actions.FindAction.class),
                null,
                SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
            };
        }
    }
    
    /**
     * @return project top Master Index project node
     */ 
    public Project getProject() {
        return this.project;
    }
    
    public void setModified(boolean flag) {
        if (flag) {
            if (getCookie(SaveCookie.class) == null) {
                SaveCookie saveCookie = ((EviewApplication) this.project).getObjectTopComponent().getSaveCookie();
                getCookieSet().add(saveCookie);
            }     
        } else {
            SaveCookie save = (SaveCookie) getCookie(SaveCookie.class);
            if (save != null) {
                getCookieSet().remove(save);
            }
        }
    }

}
