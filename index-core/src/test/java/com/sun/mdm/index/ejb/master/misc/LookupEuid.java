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
package com.sun.mdm.index.ejb.master.misc;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.util.JNDINames;

/**  Looks up an EUID
 *
 * @author  rtam
 * @version 
 */

public class LookupEuid {
    
    /** Creates a new LookupEuid instance
     *
     */
    public LookupEuid() {
    }
    
    
    /** main program for command line testing
     * @param args  args[0] = server, args[1] = euid
     *
     */    
    public static void main(String[] args) {
        String server = null;
        String euid = null;
        
        if (args.length == 2) {
            server = new String(args[0]);
            euid = new String(args[1]);
        }  else  {
            server = new String("t3://localhost:7001");
            euid = new String("0000000000");
        }
        boolean rc = lookupEuidHelper(server, euid);
        cleanup();
        if (rc)   {
            System.exit(0);
        }  else  {
            System.exit(-1);
        }
    }        
        

    /** Looks up an euid 
     *
     * @param server  server to access
     * @param euid   euid to search
     * @return  true if successful, false otherwise
     */    
    public static boolean lookupEuidHelper(String server, String euid)  {
        
        Context jndiContext = null;
         /*
         * Create a JNDI API InitialContext object if none exists
         * yet.
         */
        try {
            jndiContext = new InitialContext();
            jndiContext = com.sun.mdm.index.util.ejbproxy.EJBTestProxy.getInitialContext();
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " + e.toString());
            return false;
        }
        
        try {
            MasterController controller = (MasterController)jndiContext.lookup(JNDINames.EJB_REF_MASTER);
            EnterpriseObject eo = controller.getEnterpriseObject(euid);
            if (eo != null)  {
                System.out.println("STATUS: " + eo.getStatus());
                System.out.println(eo);
                System.out.println("\nTest succeeded!");
                return true;
            }  else  {
                System.out.println("Test failed: expected 1 record, but found 0");
                return false;
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    /** Cleans up system resources
     *
     */    
    private static void cleanup()  {
        System.gc();
        System.runFinalization();
        System.out.println("\nAll done. Bye Bye.");
    }
    
}
