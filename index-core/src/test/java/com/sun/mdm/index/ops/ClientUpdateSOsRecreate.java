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
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.SystemObject;


/**
 * update SOs recreate test
 * @author gzheng
 */
public class ClientUpdateSOsRecreate extends TestCase {
    /**
     * default constructor
     * @param name name
     */
    public ClientUpdateSOsRecreate(String name) {
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
    public void testClientUpdateSOsRecreate() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String tid = test.getTMID(test.UPDATESOS);

        RecreateResult res = tm.recreateObject(conn, tid);
        
        EnterpriseObject after = res.getAfterEO();
        EnterpriseObject before1 = res.getBeforeEO1();
        EnterpriseObject before2 = res.getBeforeEO2();
        
        assertTrue(after != null);
        
        SystemObject addso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteC,@LocalID=4000].*", after);
        assertTrue(addso != null);
        
        SystemObject delso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000a].*", after);
        assertTrue(delso == null);
        
        SystemObject updso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].*", after);
        assertTrue(updso != null);

        PersonObject p = (PersonObject) updso.getObject();
        assertTrue(p.getLastName().equals("Fancy dude"));
        assertTrue(p.getFirstName().equals("dummy ass"));

        PhoneObject phone 
        = (PhoneObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].Person[0].Phone[@PhoneType=home].*", after);
        assertTrue(phone.getPhone().equals("800-555-1212"));

        assertTrue(before1 != null);        
        addso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteC,@LocalID=4000].*", before1);
        assertTrue(addso == null);
        
        delso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000a].*", before1);
        assertTrue(delso != null);
        
        updso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].*", before1);
        assertTrue(updso != null);

        p = (PersonObject) updso.getObject();
        assertTrue(p.getLastName().equals("Zheng1"));
        assertTrue(p.getFirstName().equals("Gary1"));
        
        assertTrue(before2 == null);

        System.out.println("Update SOs Recreate [tid=" + tid + "]: OK");
    }
}
