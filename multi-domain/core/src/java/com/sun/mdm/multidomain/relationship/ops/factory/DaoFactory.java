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
package com.sun.mdm.multidomain.relationship.ops.factory;

import java.sql.Connection;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDefDao;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEaDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDefDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEavDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEaDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEavDaoImpl;

/**
 * DaoFactory class.
 * @author Davidp
 */
public class DaoFactory extends AbstractDaoFactory {

    public RelationshipDefDao getRelationshipDefDao() {
        return new RelationshipDefDaoImpl();
    }

    public RelationshipDefDao getRelationshipDefDao(Connection conn) {
        return new RelationshipDefDaoImpl(conn);
    }

    public RelationshipEavDao getRelationshipEavDao() {
        return new RelationshipEavDaoImpl();
    }

    public RelationshipEavDao getRelationshipEavDao(Connection conn) {
        return new RelationshipEavDaoImpl(conn);
    }

    public RelationshipDao getRelationshipDao() {
        return new RelationshipDaoImpl();
    }

    public RelationshipDao getRelationshipDao(Connection conn) {
        return new RelationshipDaoImpl(conn);
    }

    public RelationshipEaDao getRelationshipEaDao() {
        return new RelationshipEaDaoImpl();
    }

    public RelationshipEaDao getRelationshipEaDao(Connection conn) {
        return new RelationshipEaDaoImpl(conn);
    } 
}
