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

import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathAPI;

/**
 * euid merge
 * @author gzheng
 */
public class ClientEUIDMerge2 extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientEUIDMerge2(String name) {
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
    public void testClientEUIDMerge2() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid1 = test.getEUID(0);
        String euid2 = test.getEUID(1);
        String syscode = "SiteC";
        String lid = "4000";

        EnterpriseObject eo1 = tm.getEnterpriseObject(conn, euid1);
        EnterpriseObject eo2 = tm.getEnterpriseObject(conn, euid2);
        
        SystemObject so 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
        + syscode + ",@LocalID=" + lid + "].*", eo1);
        
        SystemObject delso = (SystemObject) so.copy();
        SystemObject addso = (SystemObject) so.copy();
        eo1.deleteChild(delso);
        eo2.addChild(addso);

        TMResult res = tm.euidMerge(conn, eo2, eo1);
        test.setTMID(res.getTMID());
        conn.commit();

        EnterpriseObject neweo1 = tm.getEnterpriseObject(conn, euid1);
        EnterpriseObject neweo2 = tm.getEnterpriseObject(conn, euid2);

        assertTrue(neweo1 == null);
                
        assertTrue(neweo2 != null);
        so = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
        + syscode + ",@LocalID=" + lid + "].*", neweo2);
        assertTrue(so != null);
        
        System.out.println("EUID Merge [tid=" + res.getTMID() + "]: OK");
    }
}
