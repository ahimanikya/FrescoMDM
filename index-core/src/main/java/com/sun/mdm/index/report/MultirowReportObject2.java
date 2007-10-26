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
 * Report row for reports that have two object images
 * @author  dcidon
 */
public class MultirowReportObject2 extends MultirowReportObject1 {
        
    /** Creates a new instance of MultirowReportObject2.
     *
     * @param reportName Name of the report.
     * @param fieldNames Fields to display.
     * @param fieldTypes Types of fields to display.
     * @param config Report configuration.
     * @throws ObjectException if an error occurs.
     */
    public MultirowReportObject2(String reportName, ArrayList fieldNames, 
                                 ArrayList fieldTypes, MultirowReportConfig2 config) 
            throws ObjectException {
                
        super(reportName, fieldNames, fieldTypes, config);
    }
    
    /** Gets the second object.
     *
     * @return The second object.
     */
    public ObjectNode getObject2() {
        return (ObjectNode) mObjectNode.pGetChildren().get(1);
    }

    /** Gets the objects.
     *
     * @return An ArrayList of objects.
     */
    public ArrayList getObjects() {
        return mObjectNode.pGetChildren();
    }
    
    /** Sets the two objects.
     *
     * @param node1  First object to set.
     * @param node2  Second object to set.
     * @throws ObjectException if an error occurs.
     */
    public void setObjects(ObjectNode node1, ObjectNode node2) 
            throws ObjectException {
                
        if (mObjectNode.pGetChildren() != null) {
            mObjectNode.removeChildren();
        }
        ArrayList list = new ArrayList();
        list.add(node1);
        list.add(node2);
        mObjectNode.addChildren(list);
    }
    
}
