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

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.PhoneObject;

/**
 * add phone recreate
 * @author gzheng
 */
public class ClientAddPhoneRecreate extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAddPhoneRecreate(String name) {
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
    public void testClientAddPhoneRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.ADDPHONE);
        String syscode = test.SYSTEMCODE;
        String lid = test.LOCALID;
        System.out.println("TID: " + tid);
        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        PhoneObject ph = (PhoneObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
            + syscode + ",@LocalID=" + lid + "].Person.Phone[@PhoneType=remote].*", after);
        System.out.println("After Phone: " + ph);
        assertTrue(ph != null);
        
        assertTrue(before1 != null);
        ph = (PhoneObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
            + syscode + ",@LocalID=" + lid + "].Person.Phone[@PhoneType=remote].*", before1);
        System.out.println("Before Phone: " + ph);
        assertTrue(ph == null);
        
        assertTrue(before2 == null);

        System.out.println("Add Phone Recreate [tid=" + tid + "]: OK");
    }
}
