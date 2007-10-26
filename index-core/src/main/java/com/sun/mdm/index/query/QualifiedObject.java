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
import java.util.StringTokenizer;
import java.util.List;


/**
 * This class is used to determine the root object in a QueryObject by comparing
 * list of QualifiedObjects and determining the common parent. This is a helper
 * class useful for Fully Qualified Object names.
 *
 * @author sdua
 */
class QualifiedObject {
    
    // Say if your fully qualified Object Name is Enterprise.SystemSBR.Person
    // Then mobjectList would be list of
    // { Enterprise, Enterprise.SystemSBR, Enterprise.SystemSBR.Person }
    private ArrayList mobjectList = new ArrayList();


    /**
     * Creates a new instance of QualifiedObject
     *
     * @param fQObjectName
     */
    public QualifiedObject(String fQObjectName) {
        parseObjectNames(fQObjectName);
    }


    static String getCommonParent(List qualObjects) {
        String root = null;
        String rootcmp = null;
        int index = 0;

        while (true) {
            for (int i = 0; i < qualObjects.size(); i++) {
                QualifiedObject quo = (QualifiedObject) qualObjects.get(i);

                if (index == quo.size()) {
                    return root;
                }

                String objName = (String) quo.mobjectList.get(index);

                if (i == 0) {
                    rootcmp = objName;
                } else if (!rootcmp.equals(objName)) {
                    return root;
                }
            }

            root = rootcmp;
            index++;
        }
    }


    ArrayList getDescendants(String root) {
        ArrayList descendants = new ArrayList();

        for (int i = 0; i < mobjectList.size(); i++) {
            String objName = (String) mobjectList.get(i);

            if (objName.length() > root.length()) {
                descendants.add(objName);
            }
        }

        return descendants;
    }


    void parseObjectNames(String qualifiedObject) {
        
        StringTokenizer st = new StringTokenizer(qualifiedObject, ".");
        String objectName = "";

        while (st.hasMoreElements()) {
            objectName = objectName + st.nextElement();
            mobjectList.add(objectName);
            objectName = objectName + ".";
        }
    }


    int size() {
        return mobjectList.size();
    }
}
