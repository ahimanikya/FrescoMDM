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
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.PersonObject;


/**
 * delete phone sbr
 * @author gzheng
 */
public class ClientDeletePhoneSBR extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientDeletePhoneSBR(String name) {
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
    public void testClientDeletePhoneSBR() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);
        
        PhoneObject phone 
        = (PhoneObject) EPathAPI.getFieldValue("Enterprise.SystemSBR.Person.Phone[@PhoneType=home].*", eo);
        PersonObject person = (PersonObject) EPathAPI.getFieldValue("Enterprise.SystemSBR.Person.*", eo);
        person.deleteChild(phone);
        
        TMResult res = tm.updateEnterpriseObject(conn, eo, "Update");
        System.out.println("TM Result: " + res.getEUID() + ":" + res.getTMID());
        test.setTMID(res.getTMID());
        conn.commit();
        
        EnterpriseObject neweo = tm.getEnterpriseObject(conn, euid);
        
        assertTrue(neweo != null);
        
        boolean found = false;
        SBR sbr = neweo.getSBR();
        assertTrue(sbr != null);
        PersonObject per = (PersonObject) sbr.getObject();
        ArrayList phones = (ArrayList) per.pGetChildren("Phone");
        if (phones != null) {
            for (int j = 0; j < phones.size(); j++) {
                PhoneObject a = (PhoneObject) phones.get(j);
                if (a.getPhoneType().equals("home")) {
                    System.out.println("Phone SBR: " + a);
                    found = true;
                }
            }
        }
        
        assertTrue(!found);

        System.out.println("Delete Phone SBR [tid=" + res.getTMID() + "]: OK");
    }
}
