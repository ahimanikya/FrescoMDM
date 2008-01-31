/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author PMalhotra
 */
class ReportDataRow implements java.io.Serializable {
    private ArrayList fieldList;
    
    /**
     * @param fields the fields in this row
     * @todo Document this constructor
     */
    public ReportDataRow(ReportField[] fields) {
        fieldList = new ArrayList();

        for (int i = 0; i < fields.length; i++) {
            fieldList.add(fields[i]);
        }
    }

    /**
     * @param fields the fields in this row
     * @param validFlag indicates if this is a valid, error-free data row to display.
     */
    public ReportDataRow(ArrayList fields) {
        fieldList = fields;
    }

    /**
     * @todo Document: Getter for Fields attribute of the DataRow object
     * @return the fields in the row
     */
    public ReportField[] getFields() {
        ReportField[] fields = new ReportField[fieldList.size()];
        int index = 0;

        for (Iterator iter = fieldList.iterator(); iter.hasNext();) {
            fields[index++] = (ReportField) iter.next();
        }

        return fields;
    }

    /**
     * Adds a feature to the Field attribute of the DataRow object
     *
     * @param field The feature to be added to the Field attribute
     */
    public void addField(ReportField field) {
        fieldList.add(field);
    }

    /**
     * @todo Document this method
     * @return a string rep
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<data-row>\n");
        //Commented for not finidnig Field java class while compiling.
        //for (Iterator iter = fieldList.iterator(); iter.hasNext();) {
        //    buf.append(((Field) iter.next()).toString());
        //}
        
        buf.append("</data-row>\n");

        return buf.toString();
    }
    
}