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
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.persistence.TMResult;

/**
 * client add
 * @author gzheng
 */
public class ClientAddOverWrite extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientAddOverWrite(String name) {
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
    public void testAddOverWrite() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        String syscode = "SiteA";
        String lid = "3000";
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);

        String stringpath = "Enterprise.SystemObject[0].Person[0].MiddleName";
        String stringdata = "Gang";
        String intpath = "Enterprise.SystemObject[0].Person[0].Height";
        Integer intdata = new Integer(168);
        SBROverWrite ow = new SBROverWrite();
        ow.setPath(stringpath);
        ow.setData(stringdata);

        SBR sbr = eo.getSBR();
        sbr.addOverWrite(ow);

        ow = new SBROverWrite();
        ow.setPath(intpath);
        ow.setData(intdata);
        sbr.addOverWrite(ow);
                
        TMResult res = tm.updateEnterpriseObject(conn, eo, "Update");
        System.out.println("TM Result: " + res.getEUID() + ":" + res.getTMID());
        test.setTMID(res.getTMID());
        conn.commit();
        
        EnterpriseObject neweo = tm.getEnterpriseObject(conn, euid);
        
        assertTrue(neweo != null);
        sbr = neweo.getSBR();

        ArrayList list = sbr.getOverWrites();
        assertTrue(list != null);

        // string value
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(stringpath) && ow.getData().equals(stringdata)) {
                found = true;
            }
        }
        
        assertTrue(found);
        
        // int value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(intpath) && ow.getData().equals(intdata)) {
                found = true;
            }
        }
        
        assertTrue(found);
        
        System.out.println("Add SBR OverWrite test: OK");
    }
}
