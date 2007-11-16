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
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.epath.EPathAPI;


/**
 * add alias
 * @author gzheng
 */
public class ClientUpdateAlias extends TestCase {
    /**
     * constructor
     * @param name name
     */
    public ClientUpdateAlias(String name) {
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
    public void testClientUpdateAlias() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(1);
        String syscode = "SiteA";
        String lid = "3000";
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);

        PersonObject person = (PersonObject) EPathAPI.getFieldValue(
            "Enterprise.SystemObject[@SystemCode=" + syscode + ",@LocalID=" + lid + "].Person.*", eo);
        AliasObject alias = (AliasObject) EPathAPI.getFieldValue(
            "Person.Alias[@FirstName=Gary,@LastName=Zheng,@MiddleName=G].*", person);
        assertTrue(alias != null);
        alias.setFirstName("Smith");    

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
                SystemObject so = (SystemObject) sos.get(i);
                PersonObject per = (PersonObject) so.getObject();
                ArrayList aliases = (ArrayList) per.pGetChildren("Alias");
                if (aliases != null) {
                    for (int j = 0; j < aliases.size(); j++) {
                        AliasObject a = (AliasObject) aliases.get(j);
                        if (a.getFirstName().equals("Gary") && a.getLastName().equals("Zheng")) {
                            System.out.println("Alias: " + a);
                            found = true;
                        }
                    }
                }
            }
        }
        
        assertTrue(!found);
        
        found = false;
        sos = (ArrayList) neweo.getSystemObjects();
        if (sos != null) {
            for (int i = 0; i < sos.size(); i++) {
                SystemObject so = (SystemObject) sos.get(i);
                PersonObject per = (PersonObject) so.getObject();
                ArrayList aliases = (ArrayList) per.pGetChildren("Alias");
                if (aliases != null) {
                    for (int j = 0; j < aliases.size(); j++) {
                        AliasObject a = (AliasObject) aliases.get(j);
                        if (a.getFirstName().equals("Smith") && a.getLastName().equals("Zheng")) {
                            System.out.println("Alias: " + a);
                            found = true;
                        }
                    }
                }
            }
        }
        
        assertTrue(found);

        System.out.println("Add Alias [tid=" + res.getTMID() + "]: OK");
    }
}
