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
package com.sun.mdm.multidomain.relationship;

import com.sun.mdm.index.objects.ObjectNode;

/**
 * HierarchyObject class.
 * @author cye
 */
public class HierarchyObject {
    /**
     * Current object.
     */
    private ObjectNode object;
    /**
     * All ancestors of the current object up to root. The ancestors are in order of 
     * position in the array. The 1st element is the parent and the last element is root.
     */
    private ObjectNode[] ancestors;
    /**
     * All the children of the current object.
     */
    private ObjectNode[] children;
}
