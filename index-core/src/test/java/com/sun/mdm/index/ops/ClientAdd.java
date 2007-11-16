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

import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import junit.framework.TestCase;
import java.sql.Connection;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.persistence.TMResult;

/**
 * client add
 * @author gzheng
 */
public class ClientAdd extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAdd(String name) {
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
    public void testClientAdd() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        test.clearDB();
        
        EnterpriseObject eo = test.createEO("1", "SiteA", "3000");
        
        TMResult res = tm.addEnterpriseObject(conn, eo);
        System.out.println("Add Enterprise Object: " + res.getEUID() + "|" + res.getTMID());
        conn.commit();
        assertTrue(res != null);
        test.setEUID(res.getEUID());
        test.setTMID(res.getTMID());

        System.out.println("Add EnterpriseOjbect [tid=" + res.getTMID() + "][euid=" + res.getEUID() + "]: OK");
    }
}
