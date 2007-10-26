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
import java.util.*;

/**
 *
 * @author  sanjay.sharma
 */
 class ConditionLeafIterator {
    
    private List [] conditionList;
    private int j = 0;
    
    /** Creates a new instance of ConditionIterator */
     ConditionLeafIterator(Condition conditionNode) {
       //this.mConditions = conditions;
       conditionList = new ArrayList[1];
       List subList = new ArrayList();
       conditionNode.setConditionsLeafList(subList);
       conditionList[0] = subList;
       
    }

     boolean hasNext() {
       if ( j == conditionList[0].size()) return false;
       return true;
    }

     Condition next() {
       if ( !hasNext() ) return null;
       Condition c = (Condition)conditionList[0].get(j++);
       return c;
    }
}
