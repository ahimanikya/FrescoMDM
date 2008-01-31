/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;

/**
 *
 * @author PMalhotra
 */
public class ReportField implements java.io.Serializable, Comparable {
    
    String val;
    

    /**
     * @param config the fieldconfig object
     * @param val the value of the field
     * @todo Document this constructor
     */
    public ReportField(String val) {
        this.val = val;
    }

    /**
     * @todo Document: Getter for Value attribute of the Field object
     * @return the value
     */
    public String getValue() {
        return this.val;
    }

    /** compare this field to another instanct
     * @param o the othre field
     * @todo Document this method
     * @return greater than, equal to, or less than 0 depending on the result of the comaprison
     */
    public int compareTo(Object o) {
        return getValue().compareTo(((ReportField) o).getValue());
    }

    /**
     * @todo Document this method
     * @return a string rep
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<value>" + getValue() + "</value>\n");

        return buf.toString();
    }
}