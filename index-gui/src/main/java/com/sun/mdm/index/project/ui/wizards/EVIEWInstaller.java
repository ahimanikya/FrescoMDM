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

import org.openide.modules.ModuleInstall;


/** Manages a module's lifecycle.
 * Remember that an installer is optional and often not needed at all.
 *
 */
public class EVIEWInstaller extends ModuleInstall {
    /** By default, do nothing but call restored().
     */
    public void installed() {
        restored();
    }

    /** By default, do nothing.
     */
    public void restored() {
        super.restored();
    }

    /** By default, do nothing.
     */
    public void uninstalled() {
    }

    /** By default, call restored().
     *@param release release number
     *@param specVersion Version
     */
    @Override
    public void updated(int release, String specVersion) {
    }

    // It is no longer recommended to override Externalizable methods
    // (readExternal and writeExternal). See the Modules API section on
    // "installation-clean" modules for an explanation.
}
