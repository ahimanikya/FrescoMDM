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

import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObject;

/**
 * add alias recrate
 * @author gzheng
 */
public class ClientUpdateAliasRecreate extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientUpdateAliasRecreate(String name) {
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
    public void testClientUpdateAliasRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.UPDATEALIAS);
        String syscode = test.SYSTEMCODE;
        String lid = test.LOCALID;
        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        boolean found = false;
        ArrayList sos = (ArrayList) after.getSystemObjects();
        if (sos != null) {
            for (int i = 0; i < sos.size(); i++) {
                SystemObject so = (SystemObject) sos.get(i);
                PersonObject per = (PersonObject) so.getObject();
                ArrayList aliases = (ArrayList) per.pGetChildren("Alias");
                if (aliases != null) {
                    for (int j = 0; j < aliases.size(); j++) {
                        AliasObject a = (AliasObject) aliases.get(j);
                        if (a.getFirstName().equals("Smith") && a.getLastName().equals("Zheng")) {
                            System.out.println("Alias: " + a);
                            found = true;
                        }
                    }
                }
            }
        }
        
        assertTrue(found);
        
        assertTrue(before1 != null);
        found = false;
        sos = (ArrayList) before1.getSystemObjects();
        if (sos != null) {
            for (int i = 0; i < sos.size(); i++) {
                SystemObject so = (SystemObject) sos.get(i);
                PersonObject per = (PersonObject) so.getObject();
                ArrayList aliases = (ArrayList) per.pGetChildren("Alias");
                if (aliases != null) {
                    for (int j = 0; j < aliases.size(); j++) {
                        AliasObject a = (AliasObject) aliases.get(j);
                        if (a.getFirstName().equals("Gary") && a.getLastName().equals("Zheng")) {
                            System.out.println("Alias: " + a);
                            found = true;
                        }
                    }
                }
            }
        }
        
        assertTrue(found);
        
        assertTrue(before2 == null);

        System.out.println("Add Alias Recreate [tid=" + tid + "]: OK");
    }
}
