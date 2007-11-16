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
import java.util.ArrayList;

/**
 * ScoreElement unit test
 * @author  aegloff
 * @version $Revision: 1.2 $
 */
public class ScoreElementTest extends TestCase {

    private static final double VAL1 = 10.974753845;
    private static final double VAL2 = 10.974753846;    
    private static final double VAL3 = -934.01;
    private static final double VAL4 = -934.01;
    private static final double VAL5 = 0.0;
    private static final String EUID1 = "000000001";
    private static final String EUID2 = "000000002";
    private static final String EUID3 = "000000003";
    private ArrayList testCollection;
    private ArrayList testCollection2;
    private ScoreElement elem1;
    private ScoreElement elem2;
    private ScoreElement elem3;
    private ScoreElement elem4;
    private ScoreElement elem5;
    
    /** 
     * Creates new ScoreElementTester
     * @see junit.framework.TestCase
     */
    public ScoreElementTest(String name) {
        super(name);
    }

    /** 
     * Set up the unit test
     * @see junit.framework.TestCase
     */        
    protected void setUp() {
        // initialization code
        // add 2 elements for the same EUID, different values
        elem1 = new ScoreElement(EUID1, VAL1);
        elem2 = new ScoreElement(EUID1, VAL2);
        
        // add 2 elements for the same EUID, same values
        elem3 = new ScoreElement(EUID2, VAL3);
        elem4 = new ScoreElement(EUID2, VAL4);
        
        elem5 = new ScoreElement(EUID3, VAL5);        
    }

    /** 
     * Tear down the unit test
     * @see junit.framework.TestCase
     */         
    protected void tearDown() {
        // cleanup code
    }

    /**
     * Test the populating and sorting of ScoreElements in a collection
     * behaves as expected
     */
    public void testPopulateAndSort() {
        testCollection = new ArrayList();
        testCollection2 = new ArrayList();
        // Add 2 ScoreElements with unique IDs. 
        testCollection.add(elem1);
        testCollection.add(elem3);
        
        // Make a second collection (simulating second pass) with some IDs from
        // above
        testCollection2.add(elem2);        
        testCollection2.add(elem4);
        testCollection2.add(elem5);
        
        // The following should result in the dupplicate EUIDs being replace
        // instead of added. This tests the 'equals' method is right.      
        testCollection.removeAll(testCollection2);
        testCollection.addAll(testCollection2);

        // At this point there should only be 3 elements in the collection
        assertEquals(3, testCollection.size());
        
        // Sort the Collection - this should result in the highest score element first, 
        // lowest last
        java.util.Collections.sort(testCollection);
        
        ScoreElement first = (ScoreElement) testCollection.get(0);
        ScoreElement second = (ScoreElement) testCollection.get(1);
        ScoreElement third = (ScoreElement) testCollection.get(2);
        
        // The order of the euid's should be by score, e.g. 000000001, 000000003, 000000002
        assertTrue(first.getEUID().equals(EUID1));
        assertTrue(second.getEUID().equals(EUID3));
        assertTrue(third.getEUID().equals(EUID2));
        
        // The values kept should the the last value for the EUIDs that got 
        // overriden, e.g. 10.974753846, 0.0, -934.01
        assertTrue(first.getWeight() == VAL2);
        assertTrue(second.getWeight() == VAL5);
        assertTrue(third.getWeight() == VAL4);
    }
    
    /**
     * Main method needed to make a self runnable class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(ScoreElementTest.class));
    }    
    
}
