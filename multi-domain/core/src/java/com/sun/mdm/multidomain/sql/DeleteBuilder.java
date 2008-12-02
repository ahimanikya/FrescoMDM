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
public class DeleteBuilder extends AbstractBuilder {

    private ArrayList<String> criteriaList = new ArrayList<String>();

    @Override
    public String getCommand() {
        return "DELETE FROM ";
    }

    @Override
    public String getColumns() {
        return "";
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
            criteria.append("=?");
            if (i < criteriaList.size() - 1) {
                criteria.append(" AND ");
            }
        }
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        whereClause.append(criteria);
        return whereClause.toString();
    }

    public void addCriteria(String criterion) {
        if (criterion != null) {
            criteriaList.add(criterion);
        }
    }
}
