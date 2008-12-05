/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
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
