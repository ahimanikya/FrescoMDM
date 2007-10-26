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
package com.sun.mdm.index.objects.epath;

import java.util.ArrayList;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SuperKey;
import com.sun.mdm.index.objects.exception.ObjectException;

/** Builds an EPath given data in a object node format.
 * @author SeeBeyond
 * @version $Revision: 1.1 $
 */
public class EPathBuilder {
    
    /** Creates a new instance of EPathBuilder */
    public EPathBuilder() {
    }

    /** creates an EPath string using from the ObjectNode and specified field.
     * The created EPath will start from the root of the object graph the
     * specified node belongs to.
     * @return an EPath string
     * @param node node to create the EPath for
     * @param fn the field, if null or empty string, then '*' is used
     * @throws ObjectException error accessing the objects
     */    
    public static String createEPath(ObjectNode node, String fn) 
        throws ObjectException {
        return createEPath(node, fn, null); 
    }
    
    /** creates an EPath string using from the ObjectNode and specified field.
     * The created EPath will start from the root of the object graph the
     * specified node belongs to.
     * @return an EPath string
     * @param stopNode stop at this node, null to goto the root
     * @param node node to create the EPath for
     * @param fn the field, if null or empty string, then '*' is used
     * @throws ObjectException error accessing the objects
     */    
    public static String createEPath(ObjectNode node, String fn, ObjectNode stopNode) 
        throws ObjectException {
        ObjectNode current;
        current = node;
        StringBuffer buf = new StringBuffer();
        

        // if fn is null or empty, make it a *
        if (fn == null || fn.equals("")) {
            buf.insert(0, "*");
        } else {
            buf.insert(0, fn);
        }
        
        if (current != null) {
            buf.insert(0, '.');
        }
        
        // traverse from current node to root node
        while (current != null && current != stopNode) {
            
            // at each point, prepend the node information to epath
            StringBuffer nodeBuf = new StringBuffer();
            nodeBuf.append(current.pGetType());
            SuperKey key = current.pGetSuperKey();
            if (key != null) {
                ArrayList keyNames = key.getKeyNames();
                ArrayList keyValues = key.getKeyValues();
                nodeBuf.append('[');
                int size = keyNames.size();
                for (int i = 0; i < size; i++) {
                    String keyName = (String) keyNames.get(i);
                    Object keyValue = keyValues.get(i);
                    
                    nodeBuf.append('@');
                    nodeBuf.append(keyName);
                    nodeBuf.append('=');
                    if (keyValue != null) {
                        nodeBuf.append(EPathAPI.maskString(keyValue));
                    }
                    if ((i + 1) < size) {
                        nodeBuf.append(',');
                    }
                }
                nodeBuf.append(']');
            }
            buf.insert(0, nodeBuf.toString());
            current = current.getParent();
            if (current != null && current != stopNode) {
                buf.insert(0, ".");
            }
        }
        return buf.toString();
    }
    
}
