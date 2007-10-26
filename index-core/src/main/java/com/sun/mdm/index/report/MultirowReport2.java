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
 * Base class for reports that contain two objects
 * @author  dcidon
 */
public abstract class MultirowReport2 extends MultirowReport1 {
    
    /** Creates a new instance of MultirowReport2.
     *
     * @param config Report configuration.
     */
    public MultirowReport2(MultirowReportConfig2 config) {
        super(config);
    }
    
    /** 
     * Appends text representation of object node to the stream.  Allows client
     * for example to save contents to disk.
     *
     * @param pstream Print stream for output.
     * @param reportRow Report row to output.
     * @throws Exception if an error occurs.
     */
    public void outputDelimitedTextRow(PrintStream pstream, 
                                       MultirowReportObject2 reportRow) 
            throws Exception {
                
        MultirowReportConfig2 config = (MultirowReportConfig2) mConfig;
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
            ArrayList childObjs = reportRow.getObjects();
            for (int i=0; i < fields.length; i++) {
                if (!first) {
                    pstream.print(',');
                }
                pstream.print(EPathAPI.getFieldValue(fields[i], (ObjectNode) childObjs.get(0)));
                first = false;
            }
            for (int i=0; i < fields.length; i++) {
                pstream.print(',');
                pstream.print(EPathAPI.getFieldValue(fields[i], (ObjectNode) childObjs.get(1)));
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
    public void outputDelimitedTextHeader(PrintStream pstream) 
            throws Exception {
                
        MultirowReportConfig2 config = (MultirowReportConfig2) mConfig;
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
            for (int i=0; i < fields.length; i++) {
                //Field label 2 can be null (e.g. Deactivate report)
                String label = config.getObjectFieldLabel2(fields[i].toString());
                if (label != null) {
                    pstream.print(',');
                    pstream.print(label);
                }
            }
        }
        pstream.println();
    }
    
}
