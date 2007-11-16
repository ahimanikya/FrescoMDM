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

import com.sun.mdm.index.objects.SBROverWrite;

/**
 * client add
 * @author gzheng
 */
public class ClientSBROverWrite extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientSBROverWrite(String name) {
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
    public void testClientSBROverWrite() throws Exception {
        TestUtils test = new TestUtils();
        Connection conn = test.getConnection();

        String euid = test.getEUID(0);
        String stringpath = "Enterprise.SystemObject[0].Person[0].FirstName";
        String stringdata = "Gary";
        String intpath = "Enterprise.SystemObject[0].Person[0].Age";
        Integer intdata = new Integer(40);
        String boolpath = "Enterprise.SystemObject[0].Person[0].Dead";
        Boolean booldata = Boolean.valueOf(false);
        String bytepath = "Enterprise.SystemObject[0].Person[0].Gender";
        Byte bytedata = new Byte((byte) 'M');
        String datepath = "Enterprise.SystemObject[0].Person[0].DOB";
        java.util.Date datedata = new java.util.Date();
        String floatpath = "Enterprise.SystemObject[0].Person[0].Weight";
        Float floatdata = new Float(130.1);

        SBROverWriteDB owdb = new SBROverWriteDB();

        SBROverWrite ow = new SBROverWrite();
        ow.setPath(stringpath);
        ow.setData(stringdata);
        owdb.create(conn, null, euid, ow);
        
        ow = new SBROverWrite();
        ow.setPath(boolpath);
        ow.setData(booldata);
        owdb.create(conn, null, euid, ow);
        
        ow = new SBROverWrite();
        ow.setPath(datepath);
        ow.setData(datedata);
        owdb.create(conn, null, euid, ow);
        
        ow = new SBROverWrite();
        ow.setPath(floatpath);
        ow.setData(floatdata);
        owdb.create(conn, null, euid, ow);
        
        ow = new SBROverWrite();
        ow.setPath(intpath);
        ow.setData(intdata);
        owdb.create(conn, null, euid, ow);
        
        conn.commit();


        ArrayList list = owdb.get(conn, null, euid);
        
        assertTrue(list != null);
        assertTrue(list.size() == 5);

        // string value
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(stringpath) && ow.getData().equals(stringdata)) {
                found = true;
            }
        }
        
        assertTrue(found);

        // boolean value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(boolpath) && ow.getData().equals(booldata)) {
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

        
        // float value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("OW Path: " + ow.getPath());
            System.out.println("OW Data: " + ow.getData());
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(floatpath) && ow.getData().equals(floatdata)) {
                found = true;
            }
        }
        
        assertTrue(found);

        
        // boolean value
        found = false;
        for (int i = 0; i < list.size(); i++) {
            ow = (SBROverWrite) list.get(i);
            if (ow.getEPath().equals(datepath)) {
//                && ((java.util.Date) ow.getData()).getTime() == datedata.getTime()) {
                found = true;
            }
        }
        
        assertTrue(found);

        
        System.out.println("SBR OverWrite test: OK");
    }
}
