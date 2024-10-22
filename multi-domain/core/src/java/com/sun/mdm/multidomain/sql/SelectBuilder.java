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
public class SelectBuilder extends AbstractBuilder {

    private final List<String> columns = new ArrayList<String>();
    private final List<OrderBy> orderBy = new ArrayList<OrderBy>();
    private final List<Criteria> criteria = new ArrayList<Criteria>();
    private String[] joinTables = new String[0];
    private Criteria joinConditions = null;
    private JoinCriteria.JOIN_TYPE joinType = null;
    private boolean distinct = false;

    @Override
    public String getCommand() {
        return distinct ? "SELECT DISTINCT " : "SELECT ";
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

        what.append(" FROM ");
        return what.toString();
    }

    public String getCriteria() {
        StringBuffer sb = new StringBuffer();
        sb.append(getJoin());
        if (criteria.size() > 0) {

            sb.append(" WHERE ");
            for (Criteria crit : criteria) {
                sb.append(crit.write());
            }
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

    public String getJoin() {
        if (joinTables.length == 0 || joinConditions == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(" ");
        sb.append(joinType.toString());
        sb.append(" (");
        for (int i = 0; i < joinTables.length; i++) {
            String tbl = joinTables[i];
            sb.append(tbl);
            if (i < joinTables.length - 1) {
                sb.append(',');
            }
        }
        sb.append(')');
        sb.append(" ON ");
        sb.append(joinConditions.write());
        return sb.toString();
    }

    public void addCriteria(Criteria crit) {
        if (crit != null) {
            criteria.add(crit);
        }
    }

    public void addColumns(String columnName) {
        if (columnName != null) {
            columns.add(columnName);
        }
    }

    public void addOrderBy(OrderBy order) {
        if (order != null) {
            orderBy.add(order);
        }
    }

    public void addJoin(String[] joinTables, JoinCriteria.JOIN_TYPE joinType, Criteria joinConditions) {
        this.joinTables = joinTables;
        this.joinConditions = joinConditions;
        this.joinType = joinType;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean getDistinct() {
        return this.distinct;
    }
}
