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
import com.sun.mdm.index.objects.epath.EPathException;

/**
 * Configuration class for Potential Duplicate Report
 * @author  dcidon
 */
public class PotentialDuplicateReportConfig extends MultirowReportConfig2 {
    
    private String mSystemCode;
    private String mStatus;
    
    // Creates a new instance of PotentialDuplicateReportConfig 
    public PotentialDuplicateReportConfig() {
    }
        
    /**
     * Sets the system code.
     * 
     * @param systemCode  New value for the system code.
     */
    public void setSystemCode(String systemCode) {
        mSystemCode = systemCode;
    }
    
    /**
     * Gets the system code.
     * 
     * @return systemCode System code.
     */
    public String getSystemCode() {
        return mSystemCode;
    }
    
    /**
     * Sets the system code.
     * 
     * @param status  New value for the status.
     */
    public void setStatus(String status) {
        mStatus = status;
    }
    
    /**
     * Gets the system code.
     * 
     * @return status  Status.
     */
    public String getStatus() {
        return mStatus;
    }
    
       
}
