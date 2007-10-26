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
package com.sun.mdm.index.update;

import com.sun.mdm.index.objects.EnterpriseObject;

import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.master.UserException;

/**Update policy, called before persisting the data.  This policy allow users to
 * plug in customized logic that changes the data content of an EnterpriseObject
 * before it is persisted in the database
 */
public interface UpdatePolicy {

    /**This policy allow users to
     * plug in customized logic that changes the data content of an EnterpriseObject
     * before it is persisted in the database
     * @param beforeImage image of the EnterpriseObject before core update logic 
     * is applied
     * @param afterImage image of the EnterpriseObject after core update logic is 
     * applied
     * @return EnterpriseObject image to be persisted
     * @throws SystemObjectException Error accessing SystemObjects
     * @throws ObjectException error in the ObjectNodes    
     * @throws UserException general user exception 
     */    
    EnterpriseObject applyUpdatePolicy(EnterpriseObject beforeImage, EnterpriseObject afterImage)
        throws SystemObjectException, ObjectException, UserException;
}
