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
package com.sun.mdm.index.objects.factory;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.metadata.ObjectFactory;


/**
 * Simple object factory implementation
 * @author gzheng
 */
public class SimpleFactory {
    /**
     * Creates a new instance of SimpleFactory
     */
    public SimpleFactory() {
    }


    /**
     * creats a new instance of ObjectNode by its object tag
     *
     * @param tag object tag
     * @exception ObjectException ObjectException
     * @return ObjectNode
     */
    public static ObjectNode create(String tag)
        throws ObjectException {
        ObjectNode ret = null;
        try {
            ret = ObjectFactory.create(tag);
        } catch (ObjectException e) {
            throw e;
        }
        return ret;
    }


    /**
     * The main program for the SimpleFactory class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            ObjectNode node = SimpleFactory.create("Person");
            ObjectNode node1 = SimpleFactory.create("Person");
            ObjectNode node2 = SimpleFactory.create("Person");
            ObjectNode node3 = SimpleFactory.create("Person");
        } catch (ObjectException e) {
            e.printStackTrace();
        }
    }
}
