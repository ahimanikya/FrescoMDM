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
import com.sun.mdm.index.matching.impl.SerialBlockPicker;
import com.sun.mdm.index.matching.impl.PickAllBlocksAtOnce;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import java.util.ArrayList;

/**
 * BlockPicker unit test
 * @author  aegloff
 * @version $Revision: 1.3 $
 */
public class BlockPickerTest extends TestCase {

    private SerialBlockPicker serialPicker;
    private PickAllBlocksAtOnce allAtOncePicker;
    private com.sun.mdm.index.objects.SystemObject sysObj;
    private EOSearchOptions searchOptions;
    
    /** 
     * Creates new BlockPickerTester 
     * @see junit.framework.TestCase
     */
    public BlockPickerTest(String name) {
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
        serialPicker = new SerialBlockPicker();       
        allAtOncePicker = new PickAllBlocksAtOnce();

    }

    /** 
     * Tear down the unit test
     * @see junit.framework.TestCase
     */        
    protected void tearDown() {
        // cleanup code
    }

    /**
     * Test the SerialBlockPicker
     * Unit test
     */
    public void testSerialBlockPicker() {
        ArrayList previousBlockIDs = new ArrayList();
        ArrayList remainingBlockIDs = new ArrayList();        
        String[] blockIDs = new String[] {"0000001", "222222", "132465789", "2"};
        remainingBlockIDs.addAll(java.util.Arrays.asList(blockIDs));
        
        int noOfBlockIDs = blockIDs.length;
        boolean validExceptionDetected = false;
        int callCount = 0;
        try {
            for (; callCount < (noOfBlockIDs + 1); callCount++) {
                String[] pickedBlockIDs = serialPicker.pickBlock(sysObj, searchOptions, 
                        previousBlockIDs, remainingBlockIDs);
                // This simple blockPicker should just take the first one in the remainingBlockIDs
                assertNotNull(pickedBlockIDs);
                assertEquals(pickedBlockIDs.length, 1);
                String blockID = pickedBlockIDs[0];
                String expectedPick = (String) remainingBlockIDs.get(0);
                assertEquals(expectedPick, blockID);
                // Simulate using the blockID
                previousBlockIDs.add(blockID);
                remainingBlockIDs.remove(blockID);
            }
        } catch (NoBlockApplicableException ex) {
            // The last call should cause this exception.
            assertEquals(noOfBlockIDs, callCount);
            validExceptionDetected = true;
        }
        assertTrue(validExceptionDetected);
        
        // make sure all blockIDs were traversed
        assertEquals(noOfBlockIDs, previousBlockIDs.size());
        assertEquals(0, remainingBlockIDs.size());
    }

    /**
     * Test the PickAllBlocksAtOnce BlockPicker
     * Unit test
     */
    public void testPickAllBlocksAtOnce() {
        ArrayList previousBlockIDs = new ArrayList();
        ArrayList remainingBlockIDs = new ArrayList();        
        String[] blockIDs = new String[] {"0000001", "222222", "132465789", "2"};
        remainingBlockIDs.addAll(java.util.Arrays.asList(blockIDs));
        
        int noOfBlockIDs = blockIDs.length;
        boolean validExceptionDetected = false;
        int callCount = 0;
        try {
            for (; callCount < 2; callCount++) {
                String[] pickedBlockIDs = allAtOncePicker.pickBlock(sysObj, searchOptions, 
                        previousBlockIDs, remainingBlockIDs);
                // This simple blockPicker should return all the remainingBlockIDs
                assertNotNull(pickedBlockIDs);
                assertEquals(pickedBlockIDs.length, noOfBlockIDs);
                for (int pickCount = 0; pickCount < pickedBlockIDs.length; pickCount++) {
                    String blockID = pickedBlockIDs[pickCount];
                    String expectedPick = blockIDs[pickCount];
                    assertEquals(expectedPick, blockID);
                    // Simulate using the blockID
                    previousBlockIDs.add(blockID);
                    remainingBlockIDs.remove(blockID);
                }
            }
        } catch (NoBlockApplicableException ex) {
            // The last call should cause this exception.
            assertEquals(callCount, 1);
            validExceptionDetected = true;
        }
        assertTrue(validExceptionDetected);
        
        // make sure all blockIDs were traversed
        assertEquals(noOfBlockIDs, previousBlockIDs.size());
        assertEquals(0, remainingBlockIDs.size());
    }
    
    
    /**
     * Main method needed to make a self runnable class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(BlockPickerTest.class));
    }    
    
}
