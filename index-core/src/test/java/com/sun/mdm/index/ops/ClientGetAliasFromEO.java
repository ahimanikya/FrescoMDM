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
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.epath.EPathAPI;

/**
 * get alias from eo
 * @author gzheng
 */
public class ClientGetAliasFromEO extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientGetAliasFromEO(String name) {
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
    public void testClientGetAliasFromEO() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        String syscode = "SiteA";
        String lid = "3000";
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);

        AliasObject alias 
        = (AliasObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
        + syscode + ",@LocalID=" + lid + "].Person[0].Alias[@FirstName=Gary,@LastName=Zheng,@MiddleName=G].*", eo);
        assertTrue(alias != null);

        System.out.println("Alias: " + alias);
        System.out.println("Get Alias: OK");
    }
}
