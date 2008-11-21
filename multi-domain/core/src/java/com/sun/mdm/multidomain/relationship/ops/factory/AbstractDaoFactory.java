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
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDefDao;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEaDao;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEavDao;

/**
 * AbstractDaoFactory class.
 * @author Davidp
 */
public abstract class AbstractDaoFactory {

    public static enum DatabaseType {

        MySQL("MySQL"), Oracle("Oracle"),;
        public final String description;

        DatabaseType(String desc) {
            this.description = desc;
        }
    };

    public abstract RelationshipDefDao getRelationshipDefDao();

    public abstract RelationshipDao getRelationshipDao();

    public abstract RelationshipEavDao getRelationshipEavDao();

    public abstract RelationshipEaDao getRelationshipEaDao();

    public abstract RelationshipDefDao getRelationshipDefDao(Connection conn);

    public abstract RelationshipDao getRelationshipDao(Connection conn);

    public abstract RelationshipEavDao getRelationshipEavDao(Connection conn);

    public abstract RelationshipEaDao getRelationshipEaDao(Connection conn);

    public static AbstractDaoFactory getDoaFactory(DatabaseType dbType) {
        if (dbType == null) {
            return new DaoFactory();
        }
        switch (dbType) {
            case MySQL:
                throw new UnsupportedOperationException("Not supported yet.");
            case Oracle:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                return new DaoFactory();
        }
    }
}
