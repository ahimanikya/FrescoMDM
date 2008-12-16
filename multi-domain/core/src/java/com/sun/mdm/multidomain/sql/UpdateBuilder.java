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
package com.sun.mdm.multidomain.sql;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Peh
 */
public class UpdateBuilder extends AbstractBuilder {

    private final List<Parameter> columns = new ArrayList<Parameter>();
    private final List<Criteria> criteria = new ArrayList<Criteria>();

    @Override
    public String getCommand() {
        return "UPDATE ";
    }

    @Override
    public String getCriteria() {
        if (criteria.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer("WHERE ");
        for (Criteria crit : criteria) {
            sb.append(crit.write());
        }
        return sb.toString();
    }

    @Override
    public String getColumns() {
        if (columns.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(" SET ");
        for (int i = 0; i < columns.size(); i++) {
            Parameter col = columns.get(i);
            sb.append(col.write());
            if (i < columns.size() - 1) {
                sb.append(", ");
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public void addColumns(String colName) {
        columns.add(new Parameter(colName));
    }

    public void addCriteria(Criteria crit) {
        if (crit != null) {
            criteria.add(crit);
        }
    }
}
