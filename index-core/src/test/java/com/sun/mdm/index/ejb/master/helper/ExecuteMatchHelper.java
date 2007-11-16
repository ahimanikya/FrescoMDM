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

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.master.MatchResult;
import java.util.List;
import java.util.LinkedList;

/** Test MC.executeMatch()
 * @author Dan Cidon
 */
public class ExecuteMatchHelper extends BasicHelper {
    
    /** Constructor
     */
    public ExecuteMatchHelper() { 
    }
    
    /**
     * Invoke execute match, replace SO mode
     * @param args command line argument
     * @return list of match objects
     * @throws Exception error occured
     */
    public List run(String args[]) throws Exception {
        setArgs(args);
        if (getBooleanValue("clearDb")) {
            ClearDb.run();
        }
        List eoList = new LinkedList();
        IteratorRecord record = getNextRecord();
        MasterController mc = MCFactory.getMasterController();
        while (record != null) {
            SystemObject[] sysobj = record.getSystemObjects();
            MatchResult mr = mc.executeMatch(sysobj[0]);
            eoList.add(mr);
            if (sysobj.length > 1) {
                EnterpriseObject eo = mc.getEnterpriseObject(mr.getEUID());
                for (int i = 1; i < sysobj.length; i++) {                    
                    eo.addSystemObject(sysobj[i]);
                }
                mc.updateEnterpriseObject(eo);
            }
            record = getNextRecord();
        }
        return eoList;
    }  
    
    
    /**
     * Invoke execute match, update SO mode
     * @param args command line argument
     * @return list of match objects
     * @throws Exception error occured
     */
    public List update(String args[]) throws Exception {
        setArgs(args);
        if (getBooleanValue("clearDb")) {
            ClearDb.run();
        }
        List eoList = new LinkedList();
        IteratorRecord record = getNextRecord();
        MasterController mc = MCFactory.getMasterController();
        while (record != null) {
            SystemObject[] sysobj = record.getSystemObjects();
            MatchResult mr = mc.executeMatchUpdate(sysobj[0]);
            eoList.add(mr);
            if (sysobj.length > 1) {
                EnterpriseObject eo = mc.getEnterpriseObject(mr.getEUID());
                for (int i = 1; i < sysobj.length; i++) {                    
                    eo.addSystemObject(sysobj[i]);
                }
                mc.updateEnterpriseObject(eo);
            }
            record = getNextRecord();
        }
        return eoList;
    }    
    
    
    /**
     * Main entry point
     * @param args command line argument
     */
    public static void main(String[] args) {
 
        try {
            ExecuteMatchHelper helper = new ExecuteMatchHelper();
            int recordsProcessed = helper.run(args).size();
            System.out.println("Records processed: " + recordsProcessed);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
