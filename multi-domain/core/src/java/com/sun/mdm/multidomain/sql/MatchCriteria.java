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
public class MatchCriteria extends Criteria {

    public static enum OPERATOR {

        EQUALS("="),
        GREATER(">"),
        GREATEREQUAL(">="),
        LESS("<"),
        LESSEQUAL("<="),
        LIKE("LIKE"),
        NOTEQUAL("<>"),;
        private final String literal;

        OPERATOR(String operator) {
            this.literal = operator;
        }
    }
   
    private final String operator;
    private final String columnName;
    private final String value;

    public MatchCriteria(String columnName, OPERATOR operator, String value) {
        this.operator = operator.literal;
        this.columnName = columnName;
        this.value = value;

    }

    public String write() {
        StringBuffer sb = new StringBuffer();
        sb.append(columnName);
        sb.append(' ');
        sb.append(operator);
        sb.append(' ');
        sb.append(value);
        return sb.toString();
    }
}
