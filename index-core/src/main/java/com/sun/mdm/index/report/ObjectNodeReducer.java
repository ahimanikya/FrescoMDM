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
package com.sun.mdm.index.report;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import java.util.ArrayList;

/**
 * This class reduces an object node such that it only contains the field 
 * values that are desired.  This conserves bandwidth when returning an 
 * ObjectNode over the network.
 * @author  dcidon
 */
public class ObjectNodeReducer {
    
    /** Creates a new instance of ObjectNodeReducer.  This Reduces object node 
     * such that it only contains the field values that are desired.  This 
     * conserves bandwidth when returning an ObjectNode over the network.
     *
     * @param node Original ObjectNode instance.
     * @param epaths EPaths of the original ObjectNode instance.
     */
    public static ObjectNode reduceObjectNode(ObjectNode node, 
                                              EPathArrayList epaths) 
            throws ObjectException, EPathException {
        ObjectNode nodeNew = node.structCopy();
        EPath[] epathArray = epaths.toArray();
        for (int i=0; i < epathArray.length; i++) {
            Object value = EPathAPI.getFieldValue(epathArray[i], node);
            if (value != null) {
                EPathAPI.setFieldValue(epathArray[i], nodeNew, value);
            }
        }
        return nodeNew;
    }
    
}
