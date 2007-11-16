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
import com.sun.mdm.index.matching.impl.PassAllBlocks;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import java.util.ArrayList;

/**
 * PassController unit test
 * @author  aegloff
 * @version $Revision: 1.3 $
 */
public class PassControllerTest 
        extends TestCase {

    private PassAllBlocks passAll;
    private com.sun.mdm.index.objects.SystemObject sysObj;
    private EOSearchOptions searchOptions;    
    
    /** 
     * Creates new PassControllerTester      
     * @see junit.framework.TestCase
     */
    public PassControllerTest(String name) {
        super(name);
    }

    /** 
     * Set up the unit test
     * @see junit.framework.TestCase
     */    
    protected void setUp() {
        // initialization code
        try {
            sysObj = new com.sun.mdm.index.objects.SystemObject();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create SystemObject", ex);
        }
        
        try {
            searchOptions = new EOSearchOptions();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create EOSearchOptions", ex);
        }
        
        // Set up all the BlockPicker implementations
        passAll = new PassAllBlocks();       
    }

    /** 
     * Tear down the unit test
     * @see junit.framework.TestCase
     */         
    protected void tearDown() {
        // cleanup code
    }

    /**
     * Test the PassAllBlocks controller
     */
    public void testPassAllBlocks() {
        ArrayList previousBlockIDs = new ArrayList();
        ArrayList remainingBlockIDs = new ArrayList();        
        java.util.ArrayList combinedResults = new ArrayList();
        java.util.ArrayList lastResults = new ArrayList();

        String[] blockIDs = new String[] {"0000001", "222222", "132465789", "2"};
        remainingBlockIDs.addAll(java.util.Arrays.asList(blockIDs));
        
        int noOfBlockIDs = blockIDs.length;
        boolean validExceptionDetected = false;
        int callCount = 0;
        for (; callCount < (noOfBlockIDs + 1); callCount++) {
            
            boolean anotherPass = 
                passAll.evalAnotherPass(sysObj, searchOptions, lastResults, combinedResults, 
                        previousBlockIDs, remainingBlockIDs);
            // This simple PassController should just pass all blocks, then return false
            if (callCount < noOfBlockIDs) {
                assertTrue(anotherPass);
            } else {
                assertTrue(!anotherPass);
            }
            // Simulate using the blockID
            String blockID = null;
            if (remainingBlockIDs.size() > 0) {
                blockID = (String) remainingBlockIDs.get(0);
                previousBlockIDs.add(blockID);
                remainingBlockIDs.remove(blockID);
            }
            
            lastResults = new ArrayList();
            // Put in some fake results. They should not influence this simple controller.
            lastResults.add(new ScoreElement(Integer.toString(callCount), 1000 * callCount));
            combinedResults.addAll(lastResults);
        }
        
        // make sure all blockIDs were traversed
        assertEquals(noOfBlockIDs, previousBlockIDs.size());
        assertEquals(0, remainingBlockIDs.size());
    }
    
    /**
     * Main method needed to make a self runnable class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(PassControllerTest.class));
    }    
    
}
