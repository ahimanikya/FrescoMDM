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
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.SBR;

/**
 * replace person sbr tester
 * @author gzheng
 */
public class ClientReplacePersonSBR extends TestCase {
    /**
     * default constructor required by junit
     * @param name name
     */
    public ClientReplacePersonSBR(String name) {
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
    public void testClientReplacePersonSBR() throws Exception {
        TestUtils test = new TestUtils();
        TransactionMgr tm = test.getTransactionMgr();
        Connection conn = test.getConnection();
        
        String euid = test.getEUID(0);
        
        EnterpriseObject eo = tm.getEnterpriseObject(conn, euid);
        SBR sbr = eo.getSBR();
        PersonObject person = (PersonObject) sbr.getObject();
        PersonObject newperson = new PersonObject();
        newperson.setLastName("Kwan");
        newperson.setFirstName("SOME First Name");
        
        sbr.deleteChild(person);
        sbr.addChild(newperson);
        
        TMResult res = tm.updateEnterpriseObject(conn, eo, "Update");
        test.setTMID(res.getTMID());
        conn.commit();
        
        EnterpriseObject neweo = tm.getEnterpriseObject(conn, euid);
        
        assertTrue(neweo != null);
        
        PersonObject p = (PersonObject) EPathAPI.getFieldValue("Enterprise.SystemSBR.Person.*", neweo);
        assertTrue(p != null);
        
        assertTrue(p.getLastName().equals("Kwan"));
        assertTrue(p.getFirstName().equals("SOME First Name"));
        
        System.out.println("Replace PersonSBR [tid=" + res.getTMID() + "]: OK");
    }
}
