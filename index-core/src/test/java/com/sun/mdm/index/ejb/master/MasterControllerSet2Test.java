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
public class MasterControllerSet2Test extends TestSuite {
    
    /** Creates new MasterControllerSet1Test
     * @see junit.framework.TestCase#
     */
    public MasterControllerSet2Test() {
    }
    
    /** Creates new test suite
     * @see junit.framework.TestSuite#
     */    
    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(GenerateMatchWeight.class);
        suite.addTestSuite(LookupLid.class);
        suite.addTestSuite(PhoneticCode.class);
        suite.addTestSuite(StandardizeNames.class);
        suite.addTestSuite(WildCardSearchEnterpriseObject.class);
        suite.addTestSuite(UpdateRecord.class);
        suite.addTestSuite(GetConfigurationValue.class);
        suite.addTestSuite(UpdateAddresses.class);
        suite.addTestSuite(TMTest4.class);
        suite.addTestSuite(TMTest5.class);
        suite.addTestSuite(DeferredPessimisticMode.class);

        // eight more test cases related to EO & LID merge/ Unmerge added
        suite.addTestSuite(DeleteSOAndValidateTransactionLog.class);
        suite.addTestSuite(DeleteSOAndValidateTransactionLog2.class);
        suite.addTestSuite(MergeEnterpriseObject_StarMerge.class);
        suite.addTestSuite(MergeEnterpriseObjectLostEO.class);
        suite.addTestSuite(MergeEnterpriseObjectAndReverseMerge.class);
        suite.addTestSuite(MergeLIDCrissCross.class);
//        suite.addTestSuite(ReadSerializeEnterpriseObjectFromVersion1.class);        
        suite.addTestSuite(SerializeDeserializeEnterpriseObject.class);
        suite.addTestSuite(MergeLIDUpdate.class);
       
        suite.addTestSuite(SerialMergeSystemObject.class);
        suite.addTestSuite(PartialUpdateEnterpriseObject.class);
        suite.addTestSuite(DBConnectionTransferMergeSystemObject.class);
        suite.addTestSuite(CyclicMergeSystemObject.class);
        suite.addTestSuite(DeferredPessimisticModeSystemMerge.class);
        
        suite.addTestSuite(MergeMultipleEnterpriseObject.class);
        
        return suite;
    }
    
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
}
