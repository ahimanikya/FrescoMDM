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

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchIterator;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.util.JNDINames;

/**
 *
 * @author  rtam
 * @version 
 */

public class LookupAssumedMatch {
    
    /** Creates a new LookupAssumedMatch instance
     *
     */
    public LookupAssumedMatch() {
    }
    
    /** main program for command line testing
     * @param args args[0] = server, args[1] = system
     *
     */    
    public static void main(String[] args) {
        String server = null;
        String system = null;
        
        if (args.length == 2) {
            server = new String(args[0]);
            system = new String(args[1]);
        }  else  {
            server = "t3://localhost:7001";
            system = "SiteA";
        }
        
        System.out.println("server--> " + server);
        System.out.println("system--> " + system);

        boolean rc = lookupAssumedMatchHelper(server, system);
        cleanup();
        if (rc)   {
            System.exit(0);
        }  else  {
            System.exit(-1);
        }
    }        

    /** Looks up all assumed matches for a given system
     *
     * @param server  server to access
     * @param system  system to search 
     * @return  true if successful, false otherwise
     */    
    public static boolean lookupAssumedMatchHelper(String server, String system)  {
        
        final int expectedCount = 4;    //  number of records expected to be found
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
            AssumedMatchSearchObject searchObj = new AssumedMatchSearchObject();
            searchObj.setSystemCode(system);
            searchObj.setPageSize(20);
            searchObj.setMaxElements(200);
            AssumedMatchIterator iterator = controller.lookupAssumedMatches(searchObj);
            AssumedMatchSummary summary = null;
            int count = 0;
            while (iterator.hasNext()) {
                summary = iterator.next();
                System.out.println(summary);
                count++;
            }
            
            if (count != expectedCount)  {
                System.out.println("Test failed: expected " + expectedCount + " record(s), but found " + count 
                        + " record(s)");
                return false;
            }  else  {
                System.out.println("Test succeeded!");
                return true;
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
