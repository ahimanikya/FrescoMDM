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
package com.sun.mdm.index.matching.impl;
import com.sun.mdm.index.matching.DomainSelector;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import java.util.ArrayList;

/**
 *
 * @author  dcidon
 */
public class SingleDomainSelectorFR implements DomainSelector {
    
    
    public String[] getDomains(SystemObject sysObj, UnparsedFieldGroup fieldGroup, ArrayList allColumns) {
        return getDomainsFR(allColumns);
    }
    
    public String[] getDomains(SystemObject sysObj, PreparsedFieldGroup fieldGroup, ArrayList allColumns) {
        return getDomainsFR(allColumns);
    }
    
    private String[] getDomainsFR(ArrayList allColumns) {
        ArrayList column = (ArrayList) allColumns.get(0);
        if (column == null) {
            return null;
        }
        String[] retVals = new String[column.size()];
	for (int i=0; i < column.size(); i++) {
            retVals[i] = "FR";
        }
        return retVals;
    }
    
}
