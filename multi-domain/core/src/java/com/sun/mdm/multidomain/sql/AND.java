/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
 */
public class AND extends BaseLogicCriteria {

//    public AND(Criteria left, Criteria right) {
//        super("AND", left, right);
//    }

    public AND(Criteria... expressions) {
        super("AND", expressions);
    }
}
