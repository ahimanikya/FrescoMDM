/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
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
    public static final String IS_NOT_NULL = "IS NOT NULL";
    public static final String IS_NULL = "IS NULL";
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
