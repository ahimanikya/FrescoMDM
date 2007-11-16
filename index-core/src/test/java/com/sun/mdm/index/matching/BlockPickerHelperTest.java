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

package com.sun.mdm.index.matching;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * BlockPickerHelper unit test 
 * @author  aegloff
 * @version $Revision: 1.2 $
 */
public class BlockPickerHelperTest extends TestCase {

    private BlockPickerHelper helper;
    
    /** 
     * Creates new BlockPickerHelperTester 
     * @see junit.framework.TestCase
     */
    public BlockPickerHelperTest(String name) {
        super(name);
    }
    
    /** 
     * Set up the unit test
     * @see junit.framework.TestCase
     */        
    protected void setUp() {
        // initialization code
        
        // Create the BlockPickerHelper
        helper = new BlockPickerHelper();       
    }

    /** 
     * Tear down the unit test
     * @see junit.framework.TestCase
     */
    protected void tearDown() {
        // cleanup code
    }

    /**
     * Test the BlockPickerHelper
     * requires that the Dbbe.home (e.g. -Dbbe.home=c:\elephant\dist)
     * property is set for loading the configuration
     */
    public void testBlockPickerHelper() {
        BlockPicker result = null;
        try {
            result = helper.getBlockPickerImpl();
        } catch (InstantiationException ex) {
            throw new RuntimeException("Failed unit test to Instantiate BlockPicker", ex);
        } catch (java.lang.ClassNotFoundException ex) {
            throw new RuntimeException("Failed unit test to load class of BlockPicker", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed unit test to create BlockPicker because of the security setup", ex);
        }
        
        // Not much else to test than that it created a BlockPicker.
        assertNotNull(result);
    }
    
    /**
     * Main method needed to make a self runnable class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(BlockPickerHelperTest.class));
    }    
    
}
