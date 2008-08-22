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
package com.sun.mdm.index.decision;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for DecisionMakerResult
 * @author  gw194542
 */
public class DecisionMakerResultTest extends TestCase {
    
    /** Creates a new instance of DecisionMakerResultTest 
     * @param name test name
     */
    public DecisionMakerResultTest(String name) {
        super(name);
    }
    
    /** Test constructor and methods
     * @throws Exception exception
     */
    public void testClass() throws Exception {
	DecisionMakerResult dmr = null;
	DecisionMakerStruct am = new DecisionMakerStruct("1001", 43.9f, null);
	DecisionMakerStruct[] dms = new DecisionMakerStruct[3];
	dms[0] = new DecisionMakerStruct("1020", 28.5f, null);
	dms[1] = new DecisionMakerStruct("2011", 19.0f, null);
	dms[2] = new DecisionMakerStruct("3829", 17.2f, null);

	dmr = new DecisionMakerResult(am, dms);

	assertTrue(dmr.getAssumedMatch() != null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 3));

	dmr.rejectAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 4));

	dmr.allowAssumedMatch();
	assertTrue(dmr.getAssumedMatch() != null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 3));

	dmr = new DecisionMakerResult(null, dms);
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 3));

	dmr.rejectAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 3));

	dmr.allowAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 3));

	dmr = new DecisionMakerResult(am, null);
	assertTrue(dmr.getAssumedMatch() != null);
	assertTrue(dmr.getPotentialDuplicates() == null);

	dmr.rejectAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue((dmr.getPotentialDuplicates() != null) && ((dmr.getPotentialDuplicates()).length == 1));

	dmr.allowAssumedMatch();
	assertTrue(dmr.getAssumedMatch() != null);
	assertTrue(dmr.getPotentialDuplicates() == null);

	dmr = new DecisionMakerResult(null, null);
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue(dmr.getPotentialDuplicates() == null);

	dmr.rejectAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue(dmr.getPotentialDuplicates() == null);

	dmr.allowAssumedMatch();
	assertTrue(dmr.getAssumedMatch() == null);
	assertTrue(dmr.getPotentialDuplicates() == null);
    }
    
    /** Main entry point
     * @param args args
     */
     
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DecisionMakerResultTest.class));
    }    
    
    
}
