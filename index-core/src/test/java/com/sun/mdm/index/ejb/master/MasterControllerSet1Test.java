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
package com.sun.mdm.index.ejb.master;

import junit.framework.TestSuite;
import junit.framework.Test;

/** Test suite for Master Controller methods
 * @author Dan Cidon
 */
public class MasterControllerSet1Test extends TestSuite {
    
    /** Creates new MasterControllerSet1Test
     * @see junit.framework.TestCase#
     */
    public MasterControllerSet1Test() {
    }
    
    /** Creates new test suite
     * @see junit.framework.TestSuite#
     */    
    public static Test suite() {
        TestSuite suite = new TestSuite();
      
        suite.addTestSuite(CreateEnterpriseObject.class);
        suite.addTestSuite(DeleteSystemObject.class);
        suite.addTestSuite(ExecuteMatch.class);
        suite.addTestSuite(LookupAssumedMatches.class);
        suite.addTestSuite(LookupPotentialDuplicates.class);
		suite.addTestSuite(LookupSystemDefinitions.class);
        suite.addTestSuite(LookupSystemObjectPKs.class);
        suite.addTestSuite(LookupTransaction.class);
        suite.addTestSuite(LookupTransactions.class);
        suite.addTestSuite(MergeEnterpriseObject.class);
        suite.addTestSuite(MergeEnterpriseObjectRevisionNumber.class);
        suite.addTestSuite(MergeSystemObject.class);
        suite.addTestSuite(SearchEnterpriseObject.class);

        suite.addTestSuite(UndoAssumedMatch.class);
        suite.addTestSuite(UpdateEnterpriseObject.class);
        suite.addTestSuite(UpdateSystemObject.class);
        suite.addTestSuite(TMTest1.class);
        suite.addTestSuite(TMTest2.class);
        suite.addTestSuite(TMTest3.class);
        suite.addTestSuite(DeactivateEnterpriseObject.class);
        suite.addTestSuite(ActivateSystemObject.class);
        suite.addTestSuite(AlphaSearchEnterpriseObject.class);
        suite.addTestSuite(BlockerSearch2EnterpriseObject.class);
        
        return suite;
    }
    
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
}
