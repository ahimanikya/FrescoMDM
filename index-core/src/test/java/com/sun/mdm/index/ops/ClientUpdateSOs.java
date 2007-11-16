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
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.epath.EPathAPI;

/**
 * update sos
 * @author gzheng
 */
public class ClientUpdateSOs extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientUpdateSOs(String name) {
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
    public void testClientUpdateSOs() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);

        SystemObject addso = test.createSO("SiteC", "4000", "03");
        SystemObject delso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000a].*", eo);
        SystemObject updso 
        = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].*", eo);
        
        PersonObject person = (PersonObject) updso.getObject();
        person.setLastName("Fancy dude");
        person.setFirstName("dummy ass");
        
        PhoneObject phone 
        = (PhoneObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].Person[0].Phone[@PhoneType=home].*", eo);
        phone.setPhone("800-555-1212");
        
        eo.deleteChild(delso);
        eo.addChild(addso);
        
        TMResult res = tm.updateEnterpriseObject(conn, eo, "Update");
        test.setTMID(res.getTMID());
        conn.commit();
        
        EnterpriseObject neweo = tm.getEnterpriseObject(conn, euid);
        
        assertTrue(neweo != null);
        
        addso = (SystemObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteC,@LocalID=4000].*", neweo);
        assertTrue(addso != null);
        
        delso = (SystemObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000a].*", neweo);
        assertTrue(delso == null);
        
        updso = (SystemObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].*", neweo);
        assertTrue(updso != null);

        PersonObject p = (PersonObject) updso.getObject();
        assertTrue(p.getLastName().equals("Fancy dude"));
        assertTrue(p.getFirstName().equals("dummy ass"));

        phone = (PhoneObject) EPathAPI.getFieldValue(
        "Enterprise.SystemObject[@SystemCode=SiteA,@LocalID=3000].Person[0].Phone[@PhoneType=home].*", neweo);
        assertTrue(phone.getPhone().equals("800-555-1212"));

        System.out.println("Update SOs [tid=" + res.getTMID() + "]: OK");
    }
}
