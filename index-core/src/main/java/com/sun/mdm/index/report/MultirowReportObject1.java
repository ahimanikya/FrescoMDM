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
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPath;
import java.util.ArrayList;
import java.util.Date;

/**
 * Report row for reports that have one object image
 * @author  dcidon
 */
public class MultirowReportObject1 extends ReportRow {
    
    protected final MultirowReportConfig1 mConfig;
    protected final ObjectNode mObjectNode;
    
    /** Creates a new instance of MultirowReportObject1.
     *
     * @param reportName Name of the report.
     * @param fieldNames Fields to display.
     * @param fieldTypes Types of fields to display.
     * @param config Report configuration.
     * @throws ObjectException if an error occurs.
     */
    public MultirowReportObject1(String reportName, ArrayList fieldNames, 
                                 ArrayList fieldTypes, MultirowReportConfig1 config) 
            throws ObjectException {
                
        mObjectNode = new ObjectNode(reportName, fieldNames, fieldTypes);   
        mConfig = config;
    }
    
    /** Gets the object.
     *
     * @return The object.
     */
    public ObjectNode getObject1() {
        return (ObjectNode) mObjectNode.pGetChildren().get(0);
    }
    
    /** Sets the object.
     *
     * @param node1 The ObjectNode to set as the object.
     */
    public void setObject1(ObjectNode node1) throws ObjectException {
        if (mObjectNode.pGetChildren() != null) {
            mObjectNode.removeChildren();
        }
        mObjectNode.addChild(node1);
    }

    /** Sets the value of a field..
     *
     * @param fieldName The name of the field.
     * @param value The new value of the field.
     */
    public void setValue(String fieldName, Object value) 
            throws ObjectException {
                
        mObjectNode.setValue(fieldName, value);
    }
    
    /** Gets the value of a field..
     *
     * @param fieldName The name of the field for which the value will 
     * be retrieved.
     * @return The value of the field.
     */
    public Object getValue(String fieldName) throws ObjectException {
        return mObjectNode.getValue(fieldName);
    }
       
    /** Constructs an ObjectNode.
     *
     * @param node ObjectNode to contruct.
     * @return The new ObjectNode instance.
     */
    protected ObjectNode constructObjectNode(ObjectNode node) {
        //ToDo: Create a reduced ObjectNode based on fieldsToRetrieve.  For
        //now just return everything.  This consumes more bandwidth.
        return node;
    }
    
}
