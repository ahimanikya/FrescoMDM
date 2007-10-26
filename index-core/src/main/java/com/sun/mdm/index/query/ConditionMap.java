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
import java.util.HashMap;


/**
 * This maintains a HashMap of {object, list of conditions}. This is created
 * from QueryObject.conditions and is invoked by QueryParser. It also maintains
 * a HashMap of {QPath, lowest child node of this QPath that has a condition on
 * it}. This HashMap is used to create SubQPaths (subquery) for a QPath. This
 * class is internally used by QueryParser.
 *
 * @author sdua
 */
class ConditionMap {
    /*
     contains map of {object name,  list of conditions}
     */
    private HashMap mobjectConditionMap = new HashMap();

    /*
     contains map of {QPath, leaf node on the path that has a condition}
     */
    private HashMap mconditionPathLeafMap = new HashMap();


    /**
     * Creates a new instance of ConditionMap
     */
    ConditionMap() {
    }


    /*
     returns List of conditions for the input object (object).
     */
    ArrayList get(String object) {
        return (ArrayList) mobjectConditionMap.get(object);
    }


    /*
     returns the Leaf node on the input conditionQPath that has a condition.
     */
    String getLeafNode(QPath conditionPath) {
        return (String) mconditionPathLeafMap.get(conditionPath);
    }


    /*
     for each QPath, set the lowest child that has a condition.
     this leaf Condition node is used to create subquery.
     */
    void setLeafNode(QPath[] qpaths) {
        for (int i = 0; i < qpaths.length; i++) {
            QPath qpath = qpaths[i];
            int index = qpath.size();
            index--;

            for (int j = index; j > 0; j--) {
                String object = qpath.getObject(j);

                // so we have found leaf node in condtions path that exists in
                // this ConditionMap
                if (contains(object)) {
                    mconditionPathLeafMap.put(qpath, object);

                    break;
                }
            }
        }
    }


    /*
    add a new condition to this ConditionMap.
     */
    void add(Condition condition) {
        String object = condition.getObjectName();
        ArrayList conditions;
        conditions = (ArrayList) mobjectConditionMap.get(object);

        if (conditions == null) {
            conditions = new ArrayList();
            mobjectConditionMap.put(object, conditions);
        }

        conditions.add(condition);
    }


    boolean contains(String object) {
        return mobjectConditionMap.containsKey(object);
    }
}
