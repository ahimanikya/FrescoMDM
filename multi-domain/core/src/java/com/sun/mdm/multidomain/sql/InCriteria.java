/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davidp
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
