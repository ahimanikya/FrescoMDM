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

/**
 *
 * @author davidp
 */
public class SelectBuilder extends AbstractBuilder {

    private ArrayList<String> criteriaList = new ArrayList<String>();
    private ArrayList<String> columnList = new ArrayList<String>();
    private ArrayList<String> orderByList = new ArrayList<String>();
    private ArrayList<String> groupByList = new ArrayList<String>();

    @Override
    public String getCommand() {
        return "SELECT ";
    }

    @Override
    public String getWhat() {
        StringBuffer what = new StringBuffer();
        String columnName = null;

        for (int i = 0; i < columnList.size(); i++) {
            columnName = columnList.get(i);
            what.append(columnName);
            if (i < columnList.size() - 1) {
                what.append(',');
            }
        }
        return what.toString() + " FROM ";
    }

    @Override
    public String getCriteria() {
        if (criteriaList.size() == 0) {
            return "";
        }
        StringBuffer criteria = new StringBuffer();
        String columnName = null;
        for (int i = 0; i < criteriaList.size(); i++) {
            columnName = criteriaList.get(i);
            criteria.append(columnName);
            if (i < criteriaList.size() - 1) {
                criteria.append(" AND ");
            }
        }
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        whereClause.append(criteria);
        whereClause.append(getGroupBy());
        whereClause.append(getOrderBy());
        return whereClause.toString();
    }

    public String getOrderBy() {
        if (orderByList.size() == 0) {
            return "";
        }
        StringBuffer orderBy = new StringBuffer();
        String columnName = null;

        for (int i = 0; i < orderByList.size(); i++) {
            columnName = orderByList.get(i);
            orderBy.append(columnName);
            if (i < orderByList.size() - 1) {
                orderBy.append(',');
            }
        }
        StringBuffer orderByClause = new StringBuffer(" ORDER BY ");
        orderByClause.append(orderBy);
        return orderByClause.toString();
    }

    public String getGroupBy() {
        if (groupByList.size() == 0) {
            return "";
        }
        StringBuffer groupBy = new StringBuffer();
        String columnName = null;

        for (int i = 0; i < groupByList.size(); i++) {
            columnName = groupByList.get(i);
            groupBy.append(columnName);
            if (i < groupByList.size() - 1) {
                groupBy.append(',');
            }
        }
        StringBuffer groupByClause = new StringBuffer(" GROUP BY ");
        groupByClause.append(groupBy);
        return groupByClause.toString();
    }

    public void addCriteria(String col1, String col2) {
        StringBuffer sb = new StringBuffer(col1);
        sb.append("=");
        if (col2 != null) {
            sb.append(col2);
        } else {
            sb.append("?");
        }
        criteriaList.add(sb.toString());
    }

    public void addColumns(String columnName) {
        if (columnName != null) {
            columnList.add(columnName);
        }
    }

    public void addOrderBy(String columnName) {
        if (columnName != null) {
            orderByList.add(columnName);
        }
    }

    public void addGroupBy(String columnName) {
        if (columnName != null) {
            groupByList.add(columnName);
        }
    }
}
