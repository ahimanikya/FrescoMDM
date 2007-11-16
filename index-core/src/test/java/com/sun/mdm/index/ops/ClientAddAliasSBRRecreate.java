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
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SBR;

/**
 * add alias sbr recreate
 * @author gzheng
 */
public class ClientAddAliasSBRRecreate extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAddAliasSBRRecreate(String name) {
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
    public void testClientAddAliasSBRRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.ADDALIASSBR);
        System.out.println("AddAliasSBRRecreate: TID: " + tid);
        String syscode = test.SYSTEMCODE;
        String lid = test.LOCALID;
        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        AliasObject alias 
        = (AliasObject) EPathAPI.getFieldValue(
        "Enterprise.SystemSBR.Person.Alias[@FirstName=GarySBR,@LastName=ZhengSBR,@MiddleName=G].*", after);
        System.out.println("After Alias: " + alias);
        assertTrue(alias != null);
        
        assertTrue(before1 != null);
        SBR sbr = before1.getSBR();
        PersonObject per = (PersonObject) sbr.getObject();
        ArrayList aliases = (ArrayList) per.pGetChildren("Alias");
        System.out.println("aliases: " + aliases);
        boolean found = false;
        if (aliases != null) {
            for (int i = 0; i < aliases.size(); i++) {
                AliasObject a = (AliasObject) aliases.get(i);
                if (a.getFirstName().equals("GarySBR") 
                && a.getLastName().equals("ZhengSBR") && a.getMiddleName().equals("G")) {
                    found = true;
                    System.out.println("After Alias: " + a);
                    break;
                }
            }
        }

        assertTrue(!found);
        
        assertTrue(before2 == null);
        
        System.out.println("Add AliasSBR Recreate[tid=" + tid + "]: OK");
    }
}
