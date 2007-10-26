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
package com.sun.mdm.index.objects;
import com.sun.mdm.index.ops.RecreateResult;

/**
 * Class providing access to before / after images of an EnterpriseObject
 * for a given transaction
 * @author Dan Cidon
 */
public class EnterpriseObjectHistory implements java.io.Serializable {
    
    private final RecreateResult mRecreateResult;
    
    /** Creates a new instance of EnterpriseHistory 
     * @param recreateResult recreateResult object from transaction manager
     */
    public EnterpriseObjectHistory(RecreateResult recreateResult) {
        mRecreateResult = recreateResult;
    }

    /** 
     * Get the after image of the EO
     * @return after image of EO
     */
    public EnterpriseObject getAfterEO() {
        return mRecreateResult.getAfterEO();
    }
    
    /** 
     * Get the after image of the merged EO after unmerge
     * @return after image of merged EO after unmerge
     */
    public EnterpriseObject getAfterEO2() {
        return mRecreateResult.getAfterEO2();
    }

    /** 
     * Get the before image of the EO.  Note that some transaction involve
     * more than one EO in the before image (such as merge transactions).
     * Therefore this method returns the EO referenced by the EUID or
     * EUID1 field.
     * @return before image of EO
     */
    public EnterpriseObject getBeforeEO1() {
        return mRecreateResult.getBeforeEO1();
    }    

    /** 
     * Get the before image of the EO.  Note that some transaction involve
     * more than one EO in the before image (such as merge transactions).
     * Therefore this method returns the EO referenced by the EUID or
     * EUID2 field.
     * @return before image of EO
     */
    public EnterpriseObject getBeforeEO2() {
        return mRecreateResult.getBeforeEO2();
    }      
}
