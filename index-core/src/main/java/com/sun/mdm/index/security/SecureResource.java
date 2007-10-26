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
package com.sun.mdm.index.security;

import java.util.ArrayList;
import com.sun.mdm.index.objects.epath.EPath;


/**
 * @author sdua
 */
 class SecureResource {
    private EPath[] mepathList;
    private EPath[] mobjectList;
    private String msMaskPlugInClassName;


    /**
     * Creates a new instance of SecureResource
     */
     SecureResource() {
    }


    /**
     * Creates a new instance of EPathResource
     *
     * @param sMaskPlugInClassName
     * @param epathList
     * @param objects
     */
     SecureResource(String sMaskPlugInClassName, ArrayList epathList,
            ArrayList objects) {
        msMaskPlugInClassName = sMaskPlugInClassName;
        mepathList = (EPath[]) epathList.toArray(new EPath[epathList.size()]);
        mobjectList = (EPath[]) objects.toArray(new EPath[objects.size()]);
    }


    final EPath[] getEPathList() {
        return mepathList;
    }


    final EPath[] getObjectList() {
        return mobjectList;
    }


    final String getSecurityMaskPlugIn() {
        return msMaskPlugInClassName;
    }
}
