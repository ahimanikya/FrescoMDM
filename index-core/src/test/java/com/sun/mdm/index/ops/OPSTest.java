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

import junit.framework.TestSuite;
import junit.framework.Test;


/**
 * OPS Test
 * @author gzheng
 */
public class OPSTest extends TestSuite {
    /**
     * constructor
     */
    public OPSTest() {
    }
    
    /**
     * test suite
     * @return Test test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(ClientAdd.class);  // tmid[0]
        
        suite.addTestSuite(ClientSBROverWrite.class);

        suite.addTestSuite(ClientGet.class);
        
        suite.addTestSuite(ClientAddPhone.class); // tmid[1]
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        
        suite.addTestSuite(ClientAddOverWrite.class); // tmid[2]
        suite.addTestSuite(ClientAddOverWriteRecreate.class);
        
        suite.addTestSuite(ClientAddAlias.class); // tmid[3]
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        
        suite.addTestSuite(ClientAddAlias2.class); // tmid[4]
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        
        suite.addTestSuite(ClientRemoveAlias.class); // tmid[5]
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        
        suite.addTestSuite(ClientAddAliasSBR.class); // tmid[6]
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        
        suite.addTestSuite(ClientDeletePhoneSBR.class); // tmid[7]
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientDeletePhoneSBRRecreate.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);

        suite.addTestSuite(ClientGetAliasFromEO.class);

        suite.addTestSuite(ClientSetCharField.class);
        suite.addTestSuite(ClientSetBooleanField.class);
        suite.addTestSuite(ClientReplacePersonSBR.class); // tmid[8]
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientDeletePhoneSBRRecreate.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientReplacePersonSBRRecreate.class);

        suite.addTestSuite(ClientUpdateSOs.class); // tmid[9]
        suite.addTestSuite(ClientUpdateSOsRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientDeletePhoneSBRRecreate.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        suite.addTestSuite(ClientReplacePersonSBRRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientUpdateSOsRecreate.class);

        suite.addTestSuite(ClientAdd2.class); // tmid[10]
        
        
        suite.addTestSuite(ClientEUIDMerge.class); // tmid[11]
        suite.addTestSuite(ClientEUIDMergeRecreate.class);
        
        suite.addTestSuite(ClientUpdateSOsRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientDeletePhoneSBRRecreate.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientReplacePersonSBRRecreate.class);
        suite.addTestSuite(ClientUpdateSOsRecreate.class);

        suite.addTestSuite(ClientEUIDMerge2.class); // tmid[12]
        suite.addTestSuite(ClientEUIDMergeRecreate2.class);

        suite.addTestSuite(ClientEUIDMergeRecreate.class);
        suite.addTestSuite(ClientAddOverWriteRecreate.class);
        suite.addTestSuite(ClientUpdateSOsRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate.class);
        suite.addTestSuite(ClientDeletePhoneSBRRecreate.class);
        suite.addTestSuite(ClientAddAliasSBRRecreate.class);
        suite.addTestSuite(ClientAddPhoneRecreate.class);
        suite.addTestSuite(ClientReplacePersonSBRRecreate.class);
        suite.addTestSuite(ClientRemoveAliasRecreate.class);
        suite.addTestSuite(ClientAddAliasRecreate2.class);
        suite.addTestSuite(ClientUpdateSOsRecreate.class);

        suite.addTestSuite(ClientUpdateAlias.class); // tmid[13]
        suite.addTestSuite(ClientUpdateAliasRecreate.class);
                
        return suite;
    }
    
    /**
     * main tester
     * @param args arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
