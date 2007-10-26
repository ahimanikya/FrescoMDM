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
 * This is used by SingleQueryParser. Say if object graph is: 
     System 
     Person
 * Address Phone Alias. 
   The joinList would contain objects: {System, Person,
 * Address}, {Person, Phone}, {Person, Alias} So this joinList uses parent child
 * relationships to do the joins
 *
 * @author sdua
 */
class JoinList {

    private ArrayList mjoinList = new ArrayList();
    private Integer[] mouterJoinIndices;


    /**
     * Creates a new instance of JoinList
     */
    JoinList() {
    }


    Object get(int index) {
        return mjoinList.get(index);
    }


    /**
     * The index of object above which the outer joins should be done
     *
     * @param unionIndex
     */
    int getOuterJoinIndex(int unionIndex) {
        
       
        if ( mouterJoinIndices == null ) {
            return 0;            
        }
        
        Integer iIndex = mouterJoinIndices[unionIndex];

        if (iIndex == null) {
            return 0;
        }

        int index = iIndex.intValue();

        return index;
    }


    /*
          set outer join Indices for every ConditionMap (will be distinct for every
          union operator.
      */
    void setOuterJoinIndices(ConditionMap[] conditionMaps) {
        
      
        if ( conditionMaps == null ) {
            return;
        }
        
        mouterJoinIndices = new Integer[conditionMaps.length];

        for (int i = 0; i < conditionMaps.length; i++) {
            ConditionMap conditionMap = conditionMaps[i];

            /*
                  iterate from the bottom to top, to find the lowest
                  object that has condition. So the outerjoin will be performed
                  to the objects lower than this object.
              */
            for (int j = mjoinList.size() - 1; j > 0; j--) {
                String object = (String) mjoinList.get(j);

                if (conditionMap.contains(object)) {
                    mouterJoinIndices[i] = new Integer(j);

                    break;
                }
            }
        }
    }


    void add(String object) {
        mjoinList.add(object);
    }


    int size() {
        return mjoinList.size();
    }
}
