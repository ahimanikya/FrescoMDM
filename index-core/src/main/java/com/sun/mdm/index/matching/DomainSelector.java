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
package com.sun.mdm.index.matching;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import java.util.ArrayList;

/**
 * Select the domain for each of the standardization groups in a system object
 * 
 */
public interface DomainSelector {
    
    String[][] DOMAIN_MAP = {
        { "AU", "Australia" },
        { "US", "United States" },
        { "UK", "United Kingdom" },
        { "AU", "Australia" },
        { "FR", "France" }
    };
    
    
    /**
     * Get an array of domain designations for the given preparsed field group values.
     * @param sysObj System object being standardized
     * @param fieldGroup Field group being standardized
     * @param allColumns Field group values to be standardized
     * @throws ObjectException An exception has occured
     * @return array of domains ("US", "UK", ...) for 
     */    
    String[] getDomains(SystemObject sysObj, PreparsedFieldGroup fieldGroup, ArrayList allColumns) throws ObjectException;
    
    /**
     * Get an array of domain designations for the given unparsed field group values.
     * @param sysObj System object being standardized
     * @param fieldGroup Field group being standardized
     * @param allColumns Field group values to be standardized
     * @throws ObjectException An exception has occured
     * @return array of domains ("US", "UK", ...) for 
     */    
    String[] getDomains(SystemObject sysObj, UnparsedFieldGroup fieldGroup, ArrayList allColumns) throws ObjectException;
    
}
