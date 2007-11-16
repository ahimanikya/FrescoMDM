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

import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.epath.EPathAPI;

/**
 * add phone
 * @author gzheng
 */
public class ClientAddPhone extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAddPhone(String name) {
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
    public void testClientAddPhone() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        String syscode = "SiteA";
        String lid = "3000";
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);
        
        PhoneObject phone = new PhoneObject();
        phone.setPhoneType("remote");

        SystemObject so = (SystemObject) EPathAPI.getFieldValue("Enterprise.SystemObject[@SystemCode=" 
            + syscode + ",@LocalID=" + lid + "].*", eo);
        
        PersonObject person = (PersonObject) so.getObject();

        person.addPhone(phone);
        
        TMResult res = tm.updateEnterpriseObject(conn, eo, "Update");
        System.out.println("TM Result: " + res.getEUID() + ":" + res.getTMID());
        test.setTMID(res.getTMID());
        conn.commit();
        
        EnterpriseObject neweo = tm.getEnterpriseObject(conn, euid);
        
        assertTrue(neweo != null);
        
        boolean found = false;
        ArrayList sos = (ArrayList) neweo.getSystemObjects();
        if (sos != null) {
            for (int i = 0; i < sos.size(); i++) {
                so = (SystemObject) sos.get(i);
                PhoneObject ph = (PhoneObject) EPathAPI.getFieldValue("SystemObject.Person.Phone[@PhoneType=remote].*", 
                    so);
                if (ph != null) {
                    System.out.println("Added Phone: " + ph);
                    found = true;
                }
            }
        }
        
        assertTrue(found);

        System.out.println("Add Phone [tid=" + res.getTMID() + "]: OK");
    }
}   
