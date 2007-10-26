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
package com.sun.mdm.index.report;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for reports that contain one object image
 * @author  dcidon
 */
public abstract class MultirowReport1 extends Report {
    
    private final List mRows = new ArrayList();
    private int position = 0;
    
    private boolean moreRec = true;
    
    /** Creates a new instance of MultirowReport1.
     *
     * @param config Report configuration.
     */
    public MultirowReport1(MultirowReportConfig1 config) {
        super(config);
    }
    
    /** Checks if there is another row.
     *
     * @return True if there is another row, false otherwise.
     */
    public boolean hasNext() {
        return position < mRows.size();
    }
    
    /** Gets the next row.
     *
     * @return The next row.
     */
    public ReportRow getNextRow() {
        ReportRow reportRow = (ReportRow) mRows.get(position);
        position++;
        return reportRow;
    }
    
    /** Adds a row.
     *
     * @param reportRow The row that is to be added.
     */
    public void addRow(ReportRow reportRow) {
        mRows.add(reportRow);
    }
    
    /** Clears the old set of records from mRows ArrayList.
     *
     * @param reportRow The row that is to be added.
     */
    public void clear() {
        mRows.clear();
    }

    /** Checks if there are more records in the result set.
     *
     * @return True if there are more rows, false otherwise.
     */
    public boolean hasMore(){
        return moreRec;
    }

    /** Sets the moreRec flag to false when no records available in result set.
     *
     */
    public void setNoMore(){
        moreRec = false;
    }
    
    /** Appends text representation of object node to the stream.  Allows client
     * for example to save contents to disk.
     *
     * @param pstream Print stream for output.
     * @param reportRow Report row to output.
     * @throws Exception if an error occurs.
     */
    public void outputDelimitedTextRow(PrintStream pstream, MultirowReportObject1 reportRow) 
            throws Exception {
                
        MultirowReportConfig1 config = (MultirowReportConfig1) mConfig;
        List transactionFields = config.getTransactionFields();
        boolean first = true;
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                if (!first) {
                    pstream.print(',');
                }
                String field = (String) i.next();
                pstream.print(reportRow.getValue(field));
                first = false;
            }
        }
        EPathArrayList objectFields = config.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            ObjectNode childObj = reportRow.getObject1();
            for (int i=0; i < fields.length; i++) {
                if (!first) {
                    pstream.print(',');
                }
                pstream.print(EPathAPI.getFieldValue(fields[i], childObj));
                first = false;
            }
            
        }
        pstream.println();
    }
    
    /** 
     * Output the header to the output stream.  Header partly made up of hard 
     * coded name fields and those specified in config file.
     *
     * @param pstream Print stream for output.
     * @throws Exception if an error occurs.
     */
    public void outputDelimitedTextHeader(PrintStream pstream) throws Exception {
        MultirowReportConfig1 config = (MultirowReportConfig1) mConfig;
        List transactionFields = config.getTransactionFields();
        boolean first = true;
        if (transactionFields != null) {
            Iterator i = transactionFields.iterator();
            while (i.hasNext()) {
                if (!first) {
                    pstream.print(',');
                }
                String field = (String) i.next();
                pstream.print(config.getTransactionFieldLabel(field));
                first = false;
            }
        }
        EPathArrayList objectFields = config.getObjectFields();
        if (objectFields != null) {
            EPath[] fields = objectFields.toArray();
            for (int i=0; i < fields.length; i++) {
                if (!first) {
                    pstream.print(',');
                }
                pstream.print(config.getObjectFieldLabel1(fields[i].toString()));
                first = false;
            }
        }
        pstream.println();
    }
    
}
