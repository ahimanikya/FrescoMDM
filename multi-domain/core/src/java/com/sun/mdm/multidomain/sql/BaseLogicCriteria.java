/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author davidp
 */
public abstract class BaseLogicCriteria extends Criteria {

    private String operator;
    private Criteria left;
    private Criteria right;
    private Criteria[] expressions = null;

    public BaseLogicCriteria(String operator, Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public BaseLogicCriteria(String operator, Criteria... expressions) {
        this.expressions = expressions;
        this.operator = operator;
    }

    public String write() {
        StringBuffer sb = new StringBuffer();
        sb.append("("); 
        for (int i = 0; i < expressions.length; i++) {
            sb.append(expressions[i].write());
            if (i < expressions.length - 1) {
                sb.append(' ');
                sb.append(operator);
                sb.append(' ');
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String write(String x) {
        StringBuffer sb = new StringBuffer();
        sb.append("( ");
        sb.append(left.write());
        sb.append(' ');
        sb.append(operator);
        sb.append(' ');
        sb.append(right.write());
        sb.append(" )");
        return sb.toString();
    }
}