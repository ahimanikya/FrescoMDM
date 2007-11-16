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

import java.util.List;
import java.util.LinkedList;

/**
 * Test class for MC.createEnterpriseObject
 * @author Dan Cidon
 */
public class CreateEnterpriseObjectHelper extends BasicHelper {
               
    /** Constructor
     */
    public CreateEnterpriseObjectHelper() {
    }
    
    /**
     * Create enterprise objects
     * @param args command line argument
     * @throws Exception error occured
     * @return list of enterprise objects
     */
    public List run(String args[]) throws Exception {
        setArgs(args);
        List eoList = new LinkedList();
        IteratorRecord record = getNextRecord();
        MasterController mc = MCFactory.getMasterController();
        while (record != null) {
            SystemObject sysobj[] = record.getSystemObjects();
            EnterpriseObject eo = null;
            if (sysobj.length == 1) {
                eo = mc.createEnterpriseObject(sysobj[0]);
            } else {
                eo = mc.createEnterpriseObject(sysobj);
            }
            eoList.add(eo);
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
            CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
            int recordsProcessed = helper.run(args).size();
            System.out.println("Records processed: " + recordsProcessed);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
