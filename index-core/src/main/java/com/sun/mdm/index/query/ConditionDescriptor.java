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
import java.util.List;

/**
 * @author sanjay.sharma
 * 
 * A Condition Objects can be viewed as representing a Condition Expression. 
 * While constructing a SQL to be executed by the JDBC driver, the root 
 * Condition Tree can simply be asked to return a String representation 
 * of the Condition Expression. 
 * 
 * In case of a prepared statement being constructed, The Top level Condition 
 * Node ( root Condition ) can be asked to return a String representation of 
 * Parsed Condition Expression as well as a Map for the binding position and 
 * its value. ConditionDescriptor accomplishes precisely the same
 *   
 */

class ConditionDescriptor implements java.io.Serializable {

    private StringBuffer buffer;
    private List bindParams;

    /**
     * Creates a new instance of SQLDescriptor
     */
     ConditionDescriptor() {
        buffer = new StringBuffer();
        bindParams = new ArrayList();
     }

      StringBuffer getConditionString() {
        return buffer;
    }

     List getBindParams() {
        return bindParams;
    }
     
     void appendConditionString(StringBuffer aBuffer) {
        this.buffer.append(aBuffer);
    }
    
     void appendConditionString(String aBuffer) {
        this.buffer.append(aBuffer);
    }
    
     void appendBindParam(Condition condition) {
        bindParams.add(condition);
    }
    
}
