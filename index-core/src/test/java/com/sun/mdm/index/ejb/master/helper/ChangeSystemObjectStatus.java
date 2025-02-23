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
import com.sun.mdm.index.objects.SystemObjectPK;

/** Activate or Deactivate a SystemObject
 * @author Dan Cidon
 */
public class ChangeSystemObjectStatus extends BasicHelper {
    
    private static final String[][] DEFS = {
        {"server", "URL of server"},
        {"system", "system code"},
        {"lid", "local id"},
        {"status", "activate or deactivate"}
    };
    
    /** Constructor */
    public ChangeSystemObjectStatus() {       
    }

    /**
     * Changed the system object status
     * @param args command line argument
     * @throws Exception error occured
     */
    public void run(String args[]) throws Exception {
        setArgs(args);
        String system = getStringValue("system");
        String lid = getStringValue("lid");
        String status = getStringValue("status");
        MasterController mc = MCFactory.getMasterController();
        if (status.equals("activate")) {
            mc.activateSystemObject(new SystemObjectPK(system, lid));
        } else if (status.equals("deactivate")) {
            mc.deactivateSystemObject(new SystemObjectPK(system, lid));
        } else { 
            throw new Exception("Invalid status: " + status);
        }
       
    }
   
    /**
     * Main entry point
     * @param args command line argument
     */    
    public static void main(String[] args) {
        try {
            ChangeSystemObjectStatus helper = new ChangeSystemObjectStatus();
            helper.run(args);
            System.out.println("Done.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
