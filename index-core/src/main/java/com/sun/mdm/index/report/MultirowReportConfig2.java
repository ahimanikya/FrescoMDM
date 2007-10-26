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
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Base configuration class for reports that contain two object images
 * @author  dcidon
 */
public abstract class MultirowReportConfig2 extends MultirowReportConfig1 {
    
    private final HashMap mObjectLabelMap2 = new HashMap();
    
    // Creates a new instance of MultirowReportConfig2 
    public MultirowReportConfig2() {
    }
    
    /** 
     * Add an Object Field.
     *
     * @param epath EPath of the object field.
     * @param label1 First label for the field.
     * @param label2 Second labelfor the field.
     * @param length Length of the field.
     */
    public void addObjectField(String epath, String label1, 
                               String label2, int length) 
            throws EPathException {
                
        addObjectField(epath, label1, length);
        mObjectLabelMap2.put(epath, label2);
    }
    
    /** 
     * Get the second field label for an EPath.
     *
     * @param epath EPath of the object field.
     * @return The second field label for an EPath.
     */
    public String getObjectFieldLabel2(String epath) {
        return (String) mObjectLabelMap2.get(epath);
    }
}
