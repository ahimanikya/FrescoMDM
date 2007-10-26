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
package com.sun.mdm.index.ops;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.ops.exception.OPSException;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author gzheng
 * object persistence service interface
 */
public interface EntityOPS {
    /** Retrive object by its parent key(s)
     * @param conn JDBC connection.
     * @param opsmap HashMap of entity object ops handles.
     * @param parentkeys Array of parent keys.
     * @return list of objects
     * @throws OPSException if an error occurs.
     */
    ArrayList get(Connection conn, HashMap opsmap, String[] parentkeys)
        throws OPSException;

    /** Persist a new object by its parent keys.
     * @param conn JDBC connection.
     * @param opsmap HashMap of entity object ops handles.
     * @param parentkeys Array of parent keys.
     * @param node ObjectNode to be persisted.
     * @throws OPSException if an error occurs.
     */
    void create(Connection conn, HashMap opsmap, String[] parentkeys,
                ObjectNode node) throws OPSException;

    /** Update an existing object by its parent keys.
     * @param conn JDBC connection.
     * @param opsmap HashMap of entity object ops handles.
     * @param parentkeys Array of parent keys.
     * @param node ObjectNode to update.
     * @throws OPSException if an error occurs.
     */
    void update(Connection conn, HashMap opsmap, String[] parentkeys,
                ObjectNode node) throws OPSException;

    /** Remove an existing object.
     * @param conn JDBC connection.
     * @param opsmap HashMap of entity object ops handles.
     * @param node ObjectNode to be removed.
     * @throws OPSException if an error occurs.
     */
    void remove(Connection conn, HashMap opsmap, ObjectNode node)
        throws OPSException;
}
