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

import org.openide.filesystems.FileObject;

import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader; //.MultiFileLoader;
import org.netbeans.spi.xml.cookies.CheckXMLSupport;
import org.netbeans.spi.xml.cookies.DataObjectAdapters;
import org.xml.sax.InputSource;

import org.openide.nodes.Node;

import org.openide.util.HelpCtx;

/**
 *
 */
public class MultiDomainConfigurationDataObject extends MultiDataObject {
    //private static final long serialVersionUID = 6338889116068357652L;

    /**
     * Creates a new MultiDomainConfigurationDataObject object.
     *
     * @param fObj DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws DataObjectExistsException DOCUMENT ME!
     */
    public MultiDomainConfigurationDataObject(FileObject fObj, UniFileLoader loader)
        throws DataObjectExistsException {
        super(fObj, loader);
        getCookieSet().add(new MultiDomainConfigurationEditSupport(this));
        InputSource is = DataObjectAdapters.inputSource(this);
        getCookieSet().add(new CheckXMLSupport(is));
        
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;

        // If you add context help, change to:
        // return new HelpCtx (MyDataObject.class);
    }

    @Override    protected Node createNodeDelegate() {
        return new MultiDomainFileNode(new MultiDomainConfigurationNode(this));
    }
 
    @Override
    public boolean isRenameAllowed() {
        return false;
    }
}
