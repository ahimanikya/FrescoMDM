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

/** Helper class for getEnterpriseObject unit test
 * @author Dan Cidon
 */
public class DeactivateEnterpriseObjectHelper extends BasicHelper {
    
    /** Constructor
     */    
    public DeactivateEnterpriseObjectHelper() {       
    }

    /**
     * Get enterprise object
     * @param args command line argument
     * @throws Exception an error occured
     */
    
    public void run(String args[]) throws Exception {
        setArgs(args);
        String euid = getStringValue("euid");
        MasterController mc = MCFactory.getMasterController();
        mc.deactivateEnterpriseObject(euid);
    }
    
    /**
     * Main entry point
     * @param args command line argument
     */

    public static void main(String[] args) {
        try {
            DeactivateEnterpriseObjectHelper helper = new DeactivateEnterpriseObjectHelper();
            helper.run(args);
            System.out.println("Done.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
