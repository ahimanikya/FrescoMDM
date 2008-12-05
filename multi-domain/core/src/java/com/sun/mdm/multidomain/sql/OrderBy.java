/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
 */
public class OrderBy {

    public static enum ORDER {

        ASC,
        DESC;
    }
    private String keyPart;
    private ORDER order;

    /**
     * @param keyPart    keyPart to order by.
     * @param order Order.ASCENDING or Order.DESCENDING
     */
    public OrderBy(String keyPart, ORDER order) {
        this.keyPart = keyPart;
        this.order = order;
    }

    public String write() {
        StringBuffer sb = new StringBuffer();
        sb.append(keyPart);
        sb.append(' ');
        sb.append(order.name());
        return sb.toString();
    }
}
