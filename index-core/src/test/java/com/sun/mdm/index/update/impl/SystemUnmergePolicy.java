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
 
package com.sun.mdm.index.update.impl;
import com.sun.mdm.index.update.UpdatePolicy;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
/**
 * Update policy for system unmerges.
 * @author  dcidon
 */
public class SystemUnmergePolicy implements UpdatePolicy {
    
    private final EnterpriseUnmergePolicy mPolicy;
    
    /** Creates a new instance of SystemUnmergePolicy */
    public SystemUnmergePolicy() throws Exception {
        mPolicy = new EnterpriseUnmergePolicy();
    }
    
    /** This update policy replicates the alias creation approach in prior
     * versions of eIndex.
     * @param enterpriseObject image of the EnterpriseObject before core update logic
     * is applied
     * @param enterpriseObject1 image of the EnterpriseObject after core update logic is
     * applied
     * @throws SystemObjectException Error accessing SystemObjects
     * @throws ObjectException error in the ObjectNodes
     * @return EnterpriseObject image to be persisted
     */    
    public EnterpriseObject applyUpdatePolicy(EnterpriseObject enterpriseObject, 
    EnterpriseObject enterpriseObject1) throws SystemObjectException, ObjectException {
        return mPolicy.applyUpdatePolicy(enterpriseObject, enterpriseObject1);
    }
    
}
