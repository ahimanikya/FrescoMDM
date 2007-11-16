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
import java.util.Date;
import java.text.DateFormat;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.util.JNDINames;

/**  Looks up all transactions for a single EUID
 *
 * @author  rtam
 * @version 
 */

public class LookupEuidTransactions { 
    
    /** Creates a new LookupAllTransactions instance
     *
     */
    public LookupEuidTransactions() { 
    }

    /** main program for command line testing.  This currently searches only for euid 0.
     * @param  args args[0] = server, args[1] = starting date, args[2] = max records to display
     *
     */    
    public static void main(String[] args) { 
        
        String server = null;
        String startDate = null;
        String euid = "0000000000";
        int maxRecsToDisplay = 0;
        
        if (args.length == 3) {
            server = args[0];
            startDate = args[1];
            maxRecsToDisplay = Integer.parseInt(args[2]);
        }  else  {
            server = "t3://localhost:7001";
            startDate = "";
            maxRecsToDisplay = 3;
        }
        
        System.out.println("server--> " + server);
        System.out.println("euid--> " + euid);
        System.out.println("maxRecsToDisplay--> " + maxRecsToDisplay);
        
        boolean rc = lookupEuidTransactionsHelper(server, euid, startDate, maxRecsToDisplay);
        cleanup();
        if (rc)   {
            System.exit(0);
        }  else  {
            System.exit(-1);
        }
    }        

    /** Looks up a transaction for an Euid
     *
     * @param server  server to access
     * @param euid  euid to search 
     * @param startDate  starting date to search 
     * @param maxRecsToDisplay  maximum number of records to display
     * @return  true if successful, false otherwise
     */    
    public static boolean lookupEuidTransactionsHelper(String server, 
                                                       String euid, 
                                                       String startDate, 
                                                       int maxRecsToDisplay) {
        Context jndiContext = null;
        final int expectedCount = 1;    //  number of records expected to be found
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
            LookupEuidTransactions mc = new LookupEuidTransactions();
            TransactionSearchObject searchObj = mc.getSearchObject(euid, startDate);
            
            MasterController controller = (MasterController)jndiContext.lookup(JNDINames.EJB_REF_MASTER);

            // Lookup transactions
            TransactionIterator resultIterator = controller.lookupTransactions(searchObj);
            if  (resultIterator != null) {

                // Sort records by euid
                
                System.out.println("Sort records by EUID, descending");
                resultIterator.sortBy("EUID", true);
                
                // Retrieve records 
                int transactionCount = resultIterator.count();
                int maxRetrievedRecs = (transactionCount >= maxRecsToDisplay ? maxRecsToDisplay : transactionCount);
                System.out.println("Retrieve first " + maxRetrievedRecs + " records");
                TransactionSummary[] ts = resultIterator.first(maxRetrievedRecs);
                for (int i = 0; i < maxRetrievedRecs; i++) {
                    System.out.println(ts[i]);
                }
                
                if (transactionCount != expectedCount)  {
                    System.out.println("Test failed: expected " + expectedCount + " record(s), but found " 
                            + transactionCount + " record(s)");
                    return false;
                }  else  {
                    System.out.println("Test succeeded!");
                    return true;
                }
            } else {
                System.out.println("There are no transactions for EUID: " + euid);
                return true;
            }
        }  catch (Exception ex) { 
            ex.printStackTrace();
        }
        return true;
    }
    
    /** Creates a TransactionSearchObject.  If no date is provided, January 1, 1901 is assumed to be the
     * starting date for the search.
     *
     * @param euid  euid to search 
     * @param startDate  start date for the search
     * @return  transaction search object
     */    
    public TransactionSearchObject getSearchObject(String euid, String startDate) { 
        
        Date date = null;
        
        try { 
            // create a new search object
            DateFormat df = DateFormat.getDateInstance();
            if (startDate == null || startDate.length() == 0)  {
                date = df.parse("January 1, 1901");
            }  else  {
                date = df.parse(startDate);
            }
            System.out.println("startDate--> " + date);
            TransactionSearchObject obj = new TransactionSearchObject(date);
            obj.setEUID(euid);
            return obj;
        }  catch (Exception e) { 
            e.printStackTrace();
        }
        return null;
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
