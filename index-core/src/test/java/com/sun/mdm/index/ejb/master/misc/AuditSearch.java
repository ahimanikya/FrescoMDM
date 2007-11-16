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

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.master.search.audit.AuditIterator;
import com.sun.mdm.index.master.search.audit.AuditSearchObject;

/**  Audit search
 *
 * @author  rtam
 * @version 
 */

public class AuditSearch {
    
    /** Creates a new LookupLid instance
     *
     */
    public AuditSearch() {
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
            euid = new String("0");
        }
        
        System.out.println("server--> " + server);
        System.out.println("euid--> " + euid);
        boolean rc = auditSearchHelper(server, euid);
        cleanup();
        if (rc)   {
            System.exit(0);
        }  else  {
            System.exit(-1);
        }
    }        

    /** Looks up the audit trail of a given euid.
     *
     * @param server  server to access
     * @param euid  euid to search 
     * @return  true if successful, false otherwise
     */    
    public static boolean auditSearchHelper(String server, String euid)  {
        
        try {
            MasterController controller = MCFactory.getMasterController();
            AuditSearchObject aso = new AuditSearchObject();
            aso.setPageSize(20);
            aso.setMaxElements(200);
            aso.setPrimaryObjectType("Person");
            AuditIterator auditIter = controller.lookupAuditLog(aso);
            if (auditIter == null)  {
                System.out.println("Test failed.  No audit records were found");
                return false;
            }  else  {
                int recordCount = auditIter.count();
                if (recordCount > 0)  {
                    System.out.println("Retrieved " + recordCount + " records");
                    System.out.println("\nTest succeeded!");
                    return true;
                }  else  {
                    System.out.println("Test failed: no audit records retrieved.  Check if sybn_audit has any rows.");
                    return false;
                }
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
