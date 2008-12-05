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
 * @author davidp
 */
public class SelectBuilder extends AbstractBuilder {

    private List<String> columns = new ArrayList<String>();
    private List<OrderBy> orderBy = new ArrayList<OrderBy>();
    private final List<Criteria> criteria = new ArrayList<Criteria>();

    @Override
    public String getCommand() {
        return "SELECT ";
    }

    @Override
    public String getColumns() {
        StringBuffer what = new StringBuffer();
        String columnName = null;

        for (int i = 0; i < columns.size(); i++) {
            columnName = columns.get(i);
            what.append(columnName);
            if (i < columns.size() - 1) {
                what.append(',');
            }
        }
        return what.toString() + " FROM ";
    }

    public String getCriteria() {
        StringBuffer sb = new StringBuffer(" WHERE ");
        for (Criteria crit : criteria) {
            sb.append(crit.write());
        }
        sb.append(getOrderBy());
        return sb.toString();
    }

    public String getOrderBy() {
        if (orderBy.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(" ORDER BY ");
        for (int i = 0; i < orderBy.size(); i++) {
            OrderBy order = orderBy.get(i);
            sb.append(order.write());
            if (i < orderBy.size() - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    public void addCriteria(Criteria crit) {
        criteria.add(crit);
    }

    public void addColumns(String columnName) {
        if (columnName != null) {
            columns.add(columnName);
        }
    }

    public void addOrderBy(OrderBy order) {
        orderBy.add(order);
    }
}
