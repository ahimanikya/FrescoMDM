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
package com.sun.mdm.multidomain.services.security;

import java.util.List;
import junit.framework.TestCase;

/**
 * SecurityManagerTest class.
 * @author cye
 */
public class SecurityManagerTest extends TestCase {
    
    public static int ROLE_COUNT = 12;
    
    public SecurityManagerTest (String name) {
        super(name);
    }
    
    public void setUp() {
    }
    
    public void test001() {
        try {
            SecurityManager securityManager = SecurityManager.getInstance().init();    
            String roles[] = securityManager.getAllRoles();
            if (roles.length == ROLE_COUNT) {
                System.out.println("Roles parsed successfully.");
                assertTrue(true);
            } else {
                System.out.println("Error: expected " + ROLE_COUNT + " roles but " +
                                   "actually processed " + roles.length + " roles.");
                assertTrue(false);
            }
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }
    
   public void test002() {
        try {
            SecurityManager securityManager = SecurityManager.getInstance().init();    
            String roles[] = securityManager.getAllRoles();
            if (roles.length == ROLE_COUNT) {
                System.out.println("Roles parsed successfully.");
                assertTrue(true);
            } else {
                System.out.println("Error: expected " + ROLE_COUNT + " roles but " +
                                   "actually processed " + roles.length + " roles.");
                assertTrue(false);
            }
            UserProfile userprofile = new UserProfile("test", "MultiDomain.Admin");
            String[] operationsByName = securityManager.getOperations(userprofile);
            if (operationsByName != null && operationsByName.length == 9) {
               assertTrue(true); 
            } else {
                fail("operations is null.");
            }    
     
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }  
   
    public void test003() {
        try {
            SecurityManager securityManager = SecurityManager.getInstance().init();    
            String roles[] = securityManager.getAllRoles();
            if (roles.length == ROLE_COUNT) {
                System.out.println("Roles parsed successfully.");
                assertTrue(true);
            } else {
                System.out.println("Error: expected " + ROLE_COUNT + " roles but " +
                                   "actually processed " + roles.length + "roles.");
                 assertTrue(false);
            }
         
            List<String> operations = securityManager.getOperationsforRole("MultiDomain.Admin");
            if (operations != null && operations.size() == 9) {
               assertTrue(true);  
            } else {
                fail("operations is null.");
            }    
        } catch(Exception ex) {
            fail(ex.getMessage());
        }        
    }     
}
