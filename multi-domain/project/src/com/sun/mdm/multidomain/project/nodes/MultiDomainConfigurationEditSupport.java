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

import org.openide.cookies.*;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.text.DataEditorSupport;
import org.openide.windows.CloneableOpenSupport;

import java.io.IOException;

/**
 *
 */
public class MultiDomainConfigurationEditSupport extends DataEditorSupport implements OpenCookie {
    /**
     * Creates a new MultiDomainConfigurationEditSupport object.
     *
     * @param obj DOCUMENT ME!
     */
    public MultiDomainConfigurationEditSupport(MultiDomainConfigurationDataObject obj) {
        super(obj, new MultiDomainEnv(obj));
    }

    private static class MultiDomainEnv extends DataEditorSupport.Env {
        //private static final long serialVersionUID = 85639662675875L;

        /**
         * Creates a new MultiDomainEnv object.
         *
         * @param obj DOCUMENT ME!
         */
        public MultiDomainEnv(MultiDomainConfigurationDataObject obj) {
            super(obj);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected FileObject getFile() {
            return getDataObject().getPrimaryFile();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getMimeType() {
            return "text/xml";
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IOException DOCUMENT ME!
         */
        protected FileLock takeLock() throws IOException {
            return ((MultiDomainConfigurationDataObject) getDataObject()).getPrimaryEntry().takeLock();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public CloneableOpenSupport findCloneableOpenSupport() {
            return getDataObject().getCookie(MultiDomainConfigurationEditSupport.class);
        }
    }
}
