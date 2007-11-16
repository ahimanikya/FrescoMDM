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
package com.sun.mdm.index.ejb.master.helper;

import junit.framework.TestCase;
import junit.framework.TestResult;
import java.lang.reflect.Constructor;

/**
 *
 * @author  dcidon
 */
public class InvokeTestClass {
    
    /** Creates a new instance of InvokeTestClass */
    public InvokeTestClass() {
    }
    
    /**
     * Invoke test class methods
     * @param args First string argument is test class name.  Second is the test 
     * method to invoke.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: [class name] [test name]");
        } else {
            try {
                Class testClass = Class.forName(args[0]);
                Class[] parm = new Class[1];
                parm[0] = Class.forName("java.lang.String");
                Object[] obj = new Object[1];
                obj[0] = args[1];
                Constructor constructor = testClass.getConstructor(parm);
                TestCase testCase = (TestCase) constructor.newInstance(obj);
                TestResult result = testCase.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
