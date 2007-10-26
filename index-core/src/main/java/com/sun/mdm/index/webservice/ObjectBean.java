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
package com.sun.mdm.index.webservice;

import java.util.Collection;
import java.util.ArrayList;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.exception.ObjectException;

/**
 * Interface for object bean 
 */
public interface ObjectBean {
       
    /** 
     * Getter for all children nodes
     * @return collection of children nodes
     * @exception ObjectException if error occures
     */            
    public Collection pGetChildren() throws ObjectException;

    /** 
     * Getter for children of a specified type
     * @param type Type of children to retrieve
     * @return Collection of children of specified type
     */
    public Collection pGetChildren(String type);

    /** 
     * Getter for child types
     * @return Arraylist of child types
     */
    public ArrayList pGetChildTypes();

    /**
     * Count of all children
     * @return number of children
     */
    public int countChildren();

    /**
     * Count of children of specified type
     * @param type of children to count
     * @return number of children of specified type
     */
    public int countChildren(String type);

    /**
     * Getter for Object node
     * @return object node
     */
    public ObjectNode pGetObject();
}
