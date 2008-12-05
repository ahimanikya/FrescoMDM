/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.mdm.multidomain.sql;

/**
 *
 * @author davidp
 */
public class OR extends BaseLogicCriteria {
/*
    public OR(Criteria left, Criteria right) {
        super("OR", left, right);
    }
*/
    public OR(Criteria... expressions) {
        super("OR", expressions);
    }
}
