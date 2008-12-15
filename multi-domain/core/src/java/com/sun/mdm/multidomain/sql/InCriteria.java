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
public class InCriteria extends Criteria {

    private final String columnName;
    private List<String> valueSet = null;

    public InCriteria(String columnName, String... values) {
        this.columnName = columnName;
        valueSet = new ArrayList<String>(values.length);
        for (String val : values) {
            valueSet.add(val);
        }
    }

    public String write() {
        StringBuffer sb = new StringBuffer();
        sb.append(columnName);
        sb.append(" IN ");
        sb.append("(");
        for (int i = 0; i < valueSet.size(); i++) {
            sb.append(valueSet.get(i));
            if (i < valueSet.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
