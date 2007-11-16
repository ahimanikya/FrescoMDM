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
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.util.JNDINames;

/**
 *
 * @author  rtam
 * @version 
 */

public class LookupPotDup {

    /** Creates a new LookupPotDup instance
     *
     */
    public LookupPotDup() {
    }
    
    /** main program for command line testing
     * @param args  args[0] = server,
     *
     */    
    public static void main(String[] args) {
        String server = null;
        
        if (args.length == 1) {
            server = new String(args[0]);
        }  else  {
            server = "t3://localhost:7001";
        }
        
        System.out.println("Server--> " + server);
        boolean rc = lookupPotDupHelper(server);
        cleanup();
        if (rc)   {
            System.exit(0);
        }  else  {
            System.exit(-1);
        }
    }        
        
    /** Looks up a local id on a given system 
     *
     * @param server  server to access
     * @return  true if successful, false otherwise
     */    
    public static boolean lookupPotDupHelper(String server) {
        
        Context jndiContext = null;
        final int expectedCount = 9;    //  number of records expected to be found
        
         /*
         * Create a JNDI API InitialContext object if none exists
         * yet.
         */
        try {
            jndiContext = new InitialContext();
            jndiContext = com.sun.mdm.index.util.ejbproxy.EJBTestProxy.getInitialContext();
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }
        
        try {
            MasterController controller = (MasterController)jndiContext.lookup(JNDINames.EJB_REF_MASTER);
            PotentialDuplicateSearchObject searchObj = new PotentialDuplicateSearchObject();
            //String tdate = "20021014195359";
            //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
            //java.util.Date date = sdf.parse(tdate);
            //searchObj.setCreateStartDate(new java.sql.Timestamp(date.getTime()));
/*            
            String[] euids = new String[2];
            euids[0]="1537";
            euids[1]="1547";
//            searchObj.setEUIDs(euids);
*/
            searchObj.setPageSize(20);
            searchObj.setMaxElements(200);
            
            /* create search options */
            EPathArrayList fields = new EPathArrayList();
            fields.add("Enterprise.SystemSBR.Person.PersonId");
            fields.add("Enterprise.SystemSBR.Person.EUID");
            fields.add("Enterprise.SystemSBR.Person.SSN");
            fields.add("Enterprise.SystemSBR.Person.FirstName");
            fields.add("Enterprise.SystemSBR.Person.LastName");            
            searchObj.setFieldsToRetrieve(fields);
            
            PotentialDuplicateIterator iterator = controller.lookupPotentialDuplicates(searchObj);
            iterator.sortBy("Weight", false);
            PotentialDuplicateSummary summary = null;
            System.out.println("Count: " + iterator.count());
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
        } catch (Exception ex) {
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
