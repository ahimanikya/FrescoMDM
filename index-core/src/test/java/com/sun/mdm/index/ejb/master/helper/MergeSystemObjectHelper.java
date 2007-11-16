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
package com.sun.mdm.index.ejb.master.helper;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.MergeResult;

/** Test MC mergeSystemObject
 * @author Dan Cidon
 */
public class MergeSystemObjectHelper extends BasicHelper {
    
    /** Constructor
     */
    public MergeSystemObjectHelper() {
    }
    
    /**
     * Merge system objects
     * @param args command line argument
     * @return merge result
     * @throws Exception error occured
     */    
    public MergeResult run(String args[]) throws Exception {
        setArgs(args);
        String systemCode = getStringValue("systemCode");
        String lidSource = getStringValue("lidSource");
        String lidDest = getStringValue("lidDest");
        boolean calcOnly = getBooleanValue("calcOnly");
        MasterController mc = MCFactory.getMasterController();
        
        MergeResult mr = mc.mergeSystemObject(systemCode, lidSource, lidDest, calcOnly);
        return mr;
    }
    
    /**
     * Main entry point
     * @param args command line argument
     */    
    public static void main(String[] args) {
        try {
            MergeSystemObjectHelper helper = new MergeSystemObjectHelper();
            System.out.println(helper.run(args));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
       
}
