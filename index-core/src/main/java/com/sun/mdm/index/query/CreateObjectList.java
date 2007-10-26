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
package com.sun.mdm.index.query;

import java.util.ArrayList;


/**
 * This is a collection of CreatObjectMeta objects. It is during initialization
 * of Assembling. After initialization, these objects are copied to
 * QueryResults.CurrentObject.
 *
 * @author sdua
 */
class CreateObjectList {
    
    private ArrayList mcreateObjectList = new ArrayList();


    /**
     * Creates a new instance of CreateObjectList
     */
    CreateObjectList() {
    }


    CreateObjectMeta get(int i) {
        return (CreateObjectMeta) mcreateObjectList.get(i);
    }


    int getIndex(String child) {
        for (int i = 0; i < mcreateObjectList.size(); i++) {
            CreateObjectMeta cobjMeta = (CreateObjectMeta) mcreateObjectList.get(i);
            String name = cobjMeta.getObjName();

            if (name.equals(child)) {
                return i;
            }
        }

        return -1;
    }


    void add(CreateObjectMeta createObjectMeta) {
        mcreateObjectList.add(createObjectMeta);
    }


    int size() {
        return mcreateObjectList.size();
    }
}
