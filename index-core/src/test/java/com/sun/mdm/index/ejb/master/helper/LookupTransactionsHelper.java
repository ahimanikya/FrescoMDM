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
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.master.search.transaction.TransactionIterator;
import com.sun.mdm.index.objects.TransactionObject;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Helper class for lookup transactions unit test 
 * @author Dan Cidon
 */
public class LookupTransactionsHelper extends BasicHelper {
    
    /** Constructor
     */    
    public LookupTransactionsHelper() {       
    }

    /**
     * Lookup transaction
     * @param args command line argument
     * @return TransactionIterator object
     * @throws Exception error occured
     */
    
    public TransactionIterator run(String args[]) throws Exception {
        setArgs(args);
        Date startDate = getDateTimeValue("startDate");
        Date endDate = getDateTimeValue("endDate");
        Integer pageSize = getIntegerValue("pageSize");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        System.out.println("Start date: " + sdf.format(startDate));
        System.out.println("End date: " + sdf.format(endDate));
        MasterController mc = MCFactory.getMasterController();
        TransactionSearchObject searchObj = new TransactionSearchObject();
        searchObj.setStartDate(startDate);
        searchObj.setEndDate(endDate);
        if (pageSize != null) {
            searchObj.setPageSize(pageSize.intValue());
        }
        TransactionIterator iterator = mc.lookupTransactions(searchObj);
        return iterator;
    }
    
    /**
     * Lookup transaction 2
     * @param args command line argument
     * @return TransactionIterator object
     * @throws Exception error occured
     */
    
    public TransactionIterator run2(String args[]) throws Exception {
        setArgs(args);
        Date startDate = getDateTimeValue("startDate");
        Date endDate = getDateTimeValue("endDate");
        Integer pageSize = getIntegerValue("pageSize");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        System.out.println("Start date: " + sdf.format(startDate));
        System.out.println("End date: " + sdf.format(endDate));
        MasterController mc = MCFactory.getMasterController();
        TransactionSearchObject searchObj = new TransactionSearchObject(startDate, endDate);
        if (pageSize != null) {
            searchObj.setPageSize(pageSize.intValue());
        }
        TransactionIterator iterator = mc.lookupTransactions(searchObj);
        return iterator;
    }
    
    /**
     * Lookup transaction 3
     * @param args command line argument
     * @return TransactionIterator object
     * @throws Exception error occured
     */
    
    public TransactionIterator run3(String args[]) throws Exception {
        setArgs(args);
        Date startDate = getDateTimeValue("startDate");
        Date endDate = getDateTimeValue("endDate");
        Integer pageSize = getIntegerValue("pageSize");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        System.out.println("Start date: " + sdf.format(startDate));
        System.out.println("End date: " + sdf.format(endDate));
        MasterController mc = MCFactory.getMasterController();
        TransactionSearchObject searchObj = new TransactionSearchObject(startDate);
        searchObj.setEndDate(endDate);
        if (pageSize != null) {
            searchObj.setPageSize(pageSize.intValue());
        }
        TransactionIterator iterator = mc.lookupTransactions(searchObj);
        return iterator;
    }
    
    
    /**
     * Checks support methods
     * @return true if no problems encountered false otherwise
     * @throws Exception error occured
     */
    
    public boolean runSupportMethods() throws Exception {
        
        Date startDate = getDateTimeValue("startDate");
        Date endDate = getDateTimeValue("endDate");
        Integer pageSize = getIntegerValue("pageSize");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        System.out.println("Start date: " + sdf.format(startDate));
        System.out.println("End date: " + sdf.format(endDate));
        MasterController mc = MCFactory.getMasterController();
        TransactionSearchObject searchObj = new TransactionSearchObject(startDate, endDate);
        System.out.println("TransactionSearchObject: ");
        System.out.println(searchObj.toString());
        if (pageSize != null) {
            searchObj.setPageSize(pageSize.intValue());
        }

        searchObj.setEUID("12341");
        searchObj.setEUID1("12342");
        searchObj.setEUID2("12343");
        searchObj.setFunction("Test");
        searchObj.setLID("11111");
        searchObj.setLID1("11112");
        searchObj.setLID2("11113");
        searchObj.setSystemCode("SBYN");
        searchObj.setSystemUser("bgates");
        searchObj.getEUID();
        String euid0 = searchObj.getEUID();
        String euid1 = searchObj.getEUID1();
        String euid2 = searchObj.getEUID2();
        String function = searchObj.getFunction();
        String lid0 = searchObj.getLID();
        String lid1 = searchObj.getLID1();
        String lid2 = searchObj.getLID2();
        int maxElements = searchObj.getMaxElements();
        String systemCode = searchObj.getSystemCode();
        String systemUser = searchObj.getSystemUser();
        if (euid0.compareToIgnoreCase("12341") != 0)  {
            return false;
        }
        if (euid1.compareToIgnoreCase("12342") != 0)  {
            return false;
        }
        if (euid2.compareToIgnoreCase("12343") != 0)  {
            return false;
        }
        if (function.compareToIgnoreCase("Test") != 0)  {
            return false;
        }
        if (lid0.compareToIgnoreCase("11111") != 0)  {
            return false;
        }
        if (lid1.compareToIgnoreCase("11112") != 0)  {
            return false;
        }
        if (lid2.compareToIgnoreCase("11113") != 0)  {
            return false;
        }
        if (maxElements != 200)  {
            return false; 
        }
        if (lid2.compareToIgnoreCase("11113") != 0)  {
            return false;
        }
        if (maxElements != 200)  {
            return false; 
        }
        if (systemCode.compareToIgnoreCase("SBYN") != 0)  {
            return false;
        }
        if (systemUser.compareToIgnoreCase("bgates") != 0)  {
            return false;
        }
        
        return true;
    }
    
    /**
     * Main entry point
     * @param args command line argument
     */

    public static void main(String[] args) {
        try {
            LookupTransactionsHelper helper = new LookupTransactionsHelper();
            TransactionIterator iterator = helper.run(args);
            if (iterator != null) {
                while (iterator.hasNext()) {
                    TransactionSummary summary = iterator.next();
                    TransactionObject transObj = summary.getTransactionObject();
                    System.out.println(transObj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
