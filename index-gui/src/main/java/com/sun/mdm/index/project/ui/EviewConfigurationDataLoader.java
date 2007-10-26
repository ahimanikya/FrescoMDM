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

import org.openide.actions.OpenAction;
import org.openide.actions.PropertiesAction;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.ExtensionList;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

import java.io.IOException;


/**
 * Recognizes .xml files as a single DataObject.
 *
 */
public class EviewConfigurationDataLoader extends UniFileLoader {
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            EviewConfigurationDataLoader.class.getName()
        );

    /** Extension for the file to be loaded. */
    public static final String EVIEW_EXTENSION = "eview";

    /**
     */
    public static final String XML_MIME = "text/xml";
    //private static final long serialVersionUID = -4579746482156152498L;

    /**
     * Creates a new EviewConfigurationDataLoader object.
     */
    public EviewConfigurationDataLoader() {
        super("com.sun.mdm.index.project.ui.EviewConfigurationDataObject");
    }

    /**
     *
     * @return String display name
     */
    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(EviewConfigurationDataLoader.class, "LBL_loaderName");
    }

    /**
     *
     * @return SystemAction[]
     */
    @Override
    protected SystemAction[] defaultActions() {
        return new SystemAction[] {
            SystemAction.get(ApplicationEditAction.class),            
            SystemAction.get(OpenAction.class),
            null,
            SystemAction.get(EviewGenerateAction.class), 
            null,
            SystemAction.get(PropertiesAction.class),
        };
    }
     
    /**
     *
     * @param primaryFile 
     *
     * @return MultiDataObject
     *
     * @throws DataObjectExistsException 
     * @throws IOException 
     */
    protected MultiDataObject createMultiObject(FileObject primaryFile)
        throws DataObjectExistsException, IOException {
        return new EviewConfigurationDataObject(primaryFile, this);
    }

    /**
     *
     * @return The list of extensions this loader recognizes.
     */
    @Override
    public ExtensionList getExtensions() {
        ExtensionList extensions = (ExtensionList) getProperty(PROP_EXTENSIONS);

        if (extensions == null) {
            extensions = new ExtensionList();
            extensions.addExtension(EVIEW_EXTENSION);
            //extensions.addMimeType(XML_MIME);
            putProperty(PROP_EXTENSIONS, extensions, false);
        }

        return extensions;
    }
}
