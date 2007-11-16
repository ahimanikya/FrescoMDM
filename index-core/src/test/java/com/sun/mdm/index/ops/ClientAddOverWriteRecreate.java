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

package com.sun.mdm.index.ops;

import junit.framework.TestCase;
import java.sql.Connection;
import java.util.ArrayList;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;

/**
 * add phone recreate
 * @author gzheng
 */
public class ClientAddOverWriteRecreate extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAddOverWriteRecreate(String name) {
        super(name);
    }
    
    /**
     * setup
     */
    public void setUp() {
    }
    
    /**
     * tester
     * @exception Exception exception
     */
    public void testClientAddOverWriteRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.ADDOVERWRITE);
        String syscode = test.SYSTEMCODE;
        String lid = test.LOCALID;
        String stringpath = "Enterprise.SystemObject[0].Person[0].MiddleName";
        String stringdata = "Gang";
        String intpath = "Enterprise.SystemObject[0].Person[0].Height";
        Integer intdata = new Integer(168);

        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        SBR sbr = after.getSBR();

        ArrayList list = sbr.getOverWrites();
        assertTrue(list != null);

        SBROverWrite ow = null;
        // string value
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(stringpath) && ow.getData().equals(stringdata)) {
                found = true;
            }
        }
        
        assertTrue(found);
        
        // int value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(intpath) && ow.getData().equals(intdata)) {
                found = true;
            }
        }
        
        assertTrue(found);
        
        assertTrue(before1 != null);
        sbr = before1.getSBR();
        list = sbr.getOverWrites();

        // string value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(stringpath) && ow.getData().equals(stringdata)) {
                found = true;
            }
        }
        
        assertTrue(!found);
        
        // int value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(intpath) && ow.getData().equals(intdata)) {
                found = true;
            }
        }
        
        assertTrue(!found);
        
                
        assertTrue(before2 == null);

        System.out.println("Add OverWrite Recreate [tid=" + tid + "]: OK");
    }
}
