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
package com.sun.mdm.index.report.client;

import java.util.Iterator;
import java.util.List;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

import com.sun.mdm.index.report.MultirowReportConfig1;
import com.sun.mdm.index.report.MultirowReportConfig2;
import com.sun.mdm.index.report.MultirowReportObject1;
import com.sun.mdm.index.report.MultirowReportObject2;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPath;

/**
 *
 * @author  rtam
 */
public class FixedWidthReportWriter implements ReportFormatter {
    
    /**  instances of StringFormatter */
    StringFormatter strFormatter = new StringFormatter();
    
    /** Creates a new instance of FixedWidthReportWriter */
    public FixedWidthReportWriter() {
    }

    
    /**  Formats a fixed-length header for the output file.
     *
     * @param reportConfig  report configuration
     * @param reportName  report name
     * @param reportTemplate  report template name
     * @param ps  print stream for output 
     */
    public void writeHeader(MultirowReportConfig1 reportConfig, 
                            String reportName, 
                            String reportTemplate,
                            PrintStream ps) {
        List transactionFields = reportConfig.getTransactionFields();
        boolean first = true;
        StringBuffer sbuf = new StringBuffer();
        if (transactionFields != null) {
            sbuf.append(reportTemplate + "\n" + reportName + "\n");
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                String field = (String) i.next();
                String transactionFieldLabel = reportConfig.getTransactionFieldLabel(field);
                int length = reportConfig.getTransactionFieldLength(field);
                boolean visibleLine2 = reportConfig.getTransactionFieldVisibleLine2(field);
                //  print non-repeatable fields in the header
                if (visibleLine2 == false)  {
                    if (!first) {
                        sbuf.append(' ');
                    }
                    sbuf.append(strFormatter.rightPadString(transactionFieldLabel, length, ' '));
                    first = false;
                }
            }
        }
        
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            for (int i=0; i < fields.length; i++) {
                String epath = fields[i].toString();
                String ojbectLabel = reportConfig.getObjectFieldLabel1(epath);
                int length = reportConfig.getObjectFieldLength(epath);
                if (!first) {
                    sbuf.append(' ');
                }
                sbuf.append(strFormatter.rightPadString(ojbectLabel, length, ' '));
                first = false;
            }
        }
        sbuf.append('\n');
        ps.print(sbuf.toString());        
    }
    
    /**  Writes a header to the output file
     *
     * @param reportConfig  report configuration
     * @param reportName  report name
     * @param reportTemplate  report template name
     * @param ps  print stream for output 
     * @throws Exception if necessary
     */
    public void writeHeader(MultirowReportConfig2 reportConfig, 
                            String reportName, 
                            String reportTemplate,
                            PrintStream ps) 
            throws Exception {
                
        List transactionFields = reportConfig.getTransactionFields();
        boolean first = true;
        StringBuffer sbuf = new StringBuffer();
        if (transactionFields != null) {
            sbuf.append(reportTemplate + "\n" + reportName + "\n");
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                String field = (String) i.next();
                String transactionFieldLabel = reportConfig.getTransactionFieldLabel(field);
                int length = reportConfig.getTransactionFieldLength(field);
                boolean visibleLine2 = reportConfig.getTransactionFieldVisibleLine2(field);
                //  print non-repeatable fields in the header
                if (visibleLine2 == false)  {
                    if (!first) {
                        sbuf.append(' ');
                    }
                    sbuf.append(strFormatter.rightPadString(transactionFieldLabel, length, ' '));
                    first = false;
                }
            }
        }
        
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            
            //  Object1 and Object2 will access the same columns, so only
            //  one label is needed
            for (int i=0; i < fields.length; i++) {
                String epath = fields[i].toString();
                String ojbectLabel = reportConfig.getObjectFieldLabel1(epath);
                int length = reportConfig.getObjectFieldLength(epath);
                if (!first) {
                    sbuf.append(' ');
                }
                sbuf.append(strFormatter.rightPadString(ojbectLabel, length, ' '));
                first = false;
            }
        }
        sbuf.append('\n');
        ps.print(sbuf.toString());        
    }
    
    /**  Writes a row to the output file
     *
     * @param reportConfig  report configuration
     * @param reportRow  reportRow to write
     * @param ps  print stream for output 
     * @param addNewline  flag to add a newline after every row
     * @throws Exception if necessary
     */
    public void writeRow(MultirowReportConfig1 reportConfig, 
                         MultirowReportObject1 reportRow,
                         PrintStream ps,
                         boolean addNewline) 
            throws Exception {
                
        StringBuffer sbuf = new StringBuffer();
        List transactionFields = reportConfig.getTransactionFields();
        boolean first = true;
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                String field = (String) i.next();
                String val = reportRow.getValue(field).toString();
                int length = reportConfig.getTransactionFieldLength(field);
                if (!first) {
                    sbuf.append(' ');
                }
                sbuf.append(strFormatter.rightPadString(val, length, ' '));
                first = false;
            }
        }
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            ObjectNode childObj = reportRow.getObject1();
            for (int i=0; i < fields.length; i++) {
                if (!first) {
                    sbuf.append(' ');
                }
                String field = fields[i].toString();
                String val = new String();
                Object obj = EPathAPI.getFieldValue(field, childObj);
                if (obj != null)  {
                    if (obj instanceof java.util.Date)  {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        val = sdf.format((java.util.Date) obj);
                    }  else  {
                        val = obj.toString();
                    }
                }
                int length = reportConfig.getObjectFieldLength(field.toString());
                sbuf.append(strFormatter.rightPadString(val, length, ' '));
                first = false;
            }
        }
        sbuf.append('\n');
        if (addNewline == true)  {
            sbuf.append('\n');
        }
        ps.print(sbuf.toString());
    }
    
    /**  Writes a row to the output file
     *
     * @param reportConfig  report configuration
     * @param reportRow  reportRow to write
     * @param ps  print stream for output 
     * @param addNewline  flag to add a newline after every row
     * @throws Exception if necessary
     */
    
    public void writeRow(MultirowReportConfig2 reportConfig, 
                         MultirowReportObject2 reportRow,
                         PrintStream ps,
                         boolean addNewline) 
            throws Exception {
                
        StringBuffer sbuf = new StringBuffer();
        StringBuffer sbuf2 = new StringBuffer();
        List transactionFields = reportConfig.getTransactionFields();
        boolean first = true;
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                String field = (String) i.next();
                int length = reportConfig.getTransactionFieldLength(field);
                boolean visibleLine1 = reportConfig.getTransactionFieldVisibleLine1(field);
                boolean visibleLine2 = reportConfig.getTransactionFieldVisibleLine2(field);
                String val = new String();
                Object obj = reportRow.getValue(field);
                if (obj != null)  {
                        val = reportRow.getValue(field).toString();
                }  
                //  print non-repeatable fields in the row
                if (visibleLine2 == false)  {
                    //  insert spaces for placeholder
                    if (visibleLine1 == false)  {  // for formatting two EUID's
                        StringBuffer sbuf3 = new StringBuffer(length);
                        for (int k = 0; k < length; k++)  {
                            sbuf2.append(' ');
                        }
                    }
                    if (!first) {
                        sbuf.append(' ');
                        sbuf2.append(' ');
                    }
                    sbuf.append(strFormatter.rightPadString(val, length, ' '));
                    first = false;
                } else { // for formatting two EUID's
                    sbuf2.append(strFormatter.rightPadString(val, length, ' '));
                }
            }
        }
        EPathArrayList objectFields = reportConfig.getObjectFields();
        if (objectFields != null) {
            sbuf2.insert(0, '\n');
            EPath[] fields = objectFields.toArray();
            ObjectNode childObj = reportRow.getObject1();
            for (int i=0; i < fields.length; i++) {
                if (!first) {
                    sbuf.append(' ');
                }
                String field = fields[i].toString();
                int length = reportConfig.getObjectFieldLength(field);
                Object obj = EPathAPI.getFieldValue(field, childObj);
                String val = new String();
                if (obj != null)  {
                    if (obj instanceof String)  {
                        int g = 0;
                    }
                    if (obj instanceof java.util.Date)  {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        val = sdf.format((java.util.Date) obj);
                    }  else  {
                        val = EPathAPI.getFieldValue(field, childObj).toString();
                    }
                }
                sbuf.append(strFormatter.rightPadString(val, length, ' '));
                first = false;
            }
            //  print out information for child object2
            ObjectNode childObj2 = reportRow.getObject2();
            if (childObj2 != null)  {
                for (int i=0; i < fields.length; i++) {
                    if (!first) {
                        sbuf2.append(' ');
                    }
                    String field = fields[i].toString();
                    int length = reportConfig.getObjectFieldLength(field);
                    Object obj = EPathAPI.getFieldValue(field, childObj2);
                    String val = new String();
                    if (obj != null)  {
                        if (obj instanceof java.util.Date)  {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            val = sdf.format((java.util.Date) obj);
                        }  else  {
                            val = EPathAPI.getFieldValue(field, childObj2).toString();
                        }
                    }
                    sbuf2.append(strFormatter.rightPadString(val, length, ' '));
                    first = false;
                }
            }
        }
        sbuf.append(sbuf2);
        sbuf.append('\n');
        if (addNewline == true)  {
            sbuf.append('\n');
        }
        ps.print(sbuf.toString());
    }
 
}
    
