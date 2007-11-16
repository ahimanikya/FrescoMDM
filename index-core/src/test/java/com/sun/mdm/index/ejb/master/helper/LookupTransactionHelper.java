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
import com.sun.mdm.index.master.search.transaction.TransactionSummary;

/** Helper class for lookup transaction unit test
 * @author Dan Cidon
 */
public class LookupTransactionHelper extends BasicHelper {
    
    /** Constructor
     */    
    public LookupTransactionHelper() {       
    }

    /**
     * Lookup transaction
     * @param args command line argument
     * @return EUID
     * @throws Exception error occured
     */
    
    public TransactionSummary run(String args[]) throws Exception {
        setArgs(args);
        String id = getStringValue("id");
        MasterController mc = MCFactory.getMasterController();
        TransactionSummary summary = mc.lookupTransaction(id);
        return summary;
    }
    
    /**
     * Main entry point
     * @param args command line argument
     */

    public static void main(String[] args) {
        try {
            LookupTransactionHelper helper = new LookupTransactionHelper();
            System.out.println(helper.run(args));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
