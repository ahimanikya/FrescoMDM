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
 * Update policy for enterprise updates.
 * @author  dcidon
 */
public class EnterpriseUpdatePolicy implements UpdatePolicy {
    
    /** Creates a new instance of EnterpriseUpdatePolicy */
    public EnterpriseUpdatePolicy() { 
    }
    
    /** This update policy replicates the alias creation approach in prior
     * versions of eIndex.
     * @param beforeImage image of the EnterpriseObject before core update logic
     * is applied
     * @param afterImage image of the EnterpriseObject after core update logic is
     * applied
     * @throws SystemObjectException Error accessing SystemObjects
     * @throws ObjectException error in the ObjectNodes
     * @return EnterpriseObject image to be persisted
     */
    public EnterpriseObject applyUpdatePolicy(EnterpriseObject beforeImage,
    EnterpriseObject afterImage) throws SystemObjectException, ObjectException {
        
        //Check SO name fields.  If changed, add alias to that SO
        AliasHelper.addNameChangeAliasSO(beforeImage, afterImage);
        
        //See if SBR name fields changed
        AliasHelper.addNameChangeAliasSBR(beforeImage, afterImage);

        //Go through system objects and construct aliases based on maiden name      
        AliasHelper.addAliasMaidenNameSO(afterImage);
        
        //Go through SBR and construct alias based on maiden name      
        AliasHelper.addAliasMaidenNameSBR(afterImage);        
        
        //Go through active system objects and add their aliases and names
        //to the SBR alias set
        AliasHelper.addAliasSBRFromSO(afterImage);
                
        //Add alias overwrite values for all SBR's on the list
        AliasHelper.addAliasOverwrite(afterImage);
        
        return afterImage;
    }
    
}
