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

/**
 *
 * @author David Peh
 */
public class NullCriteria extends Criteria {

    public static enum OPERATOR {

        ISNOTNULL("IS NOT NULL"),
        ISNULL("IS NULL");
        private String literal;

        OPERATOR(String op) {
            this.literal = op;
        }
    }
    private final String operator;
    private final String columnName;

    public NullCriteria(String columnName, OPERATOR op) {
        this.operator = op.literal;
        this.columnName = columnName;

    }

    public String write() {
        StringBuffer sb = new StringBuffer();
        // sb.append("(");
        sb.append(columnName);
        sb.append(' ');
        sb.append(operator);
        //sb.append(")");
        return sb.toString();
    }
}
