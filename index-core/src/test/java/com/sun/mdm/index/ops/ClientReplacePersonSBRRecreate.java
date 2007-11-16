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

import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.PersonObject;

/**
 * replace person sbr recreate
 * @author gzheng
 */
public class ClientReplacePersonSBRRecreate extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientReplacePersonSBRRecreate(String name) {
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
    public void testClientReplacePersonSBRRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.REPLACEPERSONSBR);

        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        PersonObject p = (PersonObject) EPathAPI.getFieldValue("Enterprise.SystemSBR.Person.*", after);
        assertTrue(p != null);
        assertTrue(p.getLastName().equals("Kwan"));
        assertTrue(p.getFirstName().equals("SOME First Name"));

        assertTrue(before1 != null);
        p = (PersonObject) EPathAPI.getFieldValue("Enterprise.SystemSBR.Person.*", before1);
        assertTrue(p != null);
        assertTrue(p.getLastName().equals("ZhengSBR"));
        assertTrue(p.getFirstName().equals("Gary"));
        
        assertTrue(before2 == null);

        System.out.println("Replace PersonSBR Recreate [tid=" + tid + "]: OK");
    }
}
