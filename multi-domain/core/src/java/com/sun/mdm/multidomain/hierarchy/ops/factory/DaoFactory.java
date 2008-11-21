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
package com.sun.mdm.multidomain.hierarchy.ops.factory;

import java.sql.Connection;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeDao;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyDefDao;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeEaDao;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyDefDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeEavDao;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeEaDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeEavDaoImpl;

/**
 * DaoFactory class.
 * @author Davidp
 */
public class DaoFactory extends AbstractDaoFactory {

    public HierarchyDefDao getHierarchyDefDao() {
        return new HierarchyDefDaoImpl();
    }

    public HierarchyDefDao getHierarchyDefDao(Connection conn) {
        return new HierarchyDefDaoImpl(conn);
    }

    public HierarchyNodeEavDao getHierarchyNodeEavDao() {
        return new HierarchyNodeEavDaoImpl();
    }

    public HierarchyNodeEavDao getHierarchyNodeEavDao(Connection conn) {
        return new HierarchyNodeEavDaoImpl(conn);
    }

    public HierarchyNodeDao getHierarchyNodeDao() {
        return new HierarchyNodeDaoImpl();
    }

    public HierarchyNodeDao getHierarchyNodeDao(Connection conn) {
        return new HierarchyNodeDaoImpl(conn);
    }

    public HierarchyNodeEaDao getHierarchyNodeEaDao() {
        return new HierarchyNodeEaDaoImpl();
    }

    public HierarchyNodeEaDao getHierarchyNodeEaDao(Connection conn) {
        return new HierarchyNodeEaDaoImpl(conn);
    } 
}
