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

import junit.framework.TestCase;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.master.MatchFieldChange;

/**
 * match Field Change test class
 * @author dcidon
 */
public class IsMatchFieldChange extends TestCase {
    
    /** Constructor
     * @param testName testName
     */
    public IsMatchFieldChange(java.lang.String testName) {
        super(testName);
    }
    
    /** Main entry point
     * @param args arguments
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(IsMatchFieldChange.class);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /** Test of isMatchFieldChanged method, of class com.sun.mdm.index.master.MatchFieldChange. 
     * @throws Exception an error occured
     */
    public void testIsMatchFieldChanged() throws Exception {
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        createHelper.run(new String[] {"fileName=MatchFieldChanged1.txt", "fileType=eiEvent"});
        MatchFieldChange mfc = new MatchFieldChange();
        EnterpriseObject eo1 = getEO();
        EnterpriseObject eo2 = getEO();
        assertTrue(!mfc.isMatchFieldChanged(mfc.getMatchFields(eo1), mfc.getMatchFields(eo2)));
        PersonObject person = (PersonObject) eo2.getSBR().getObject();
        person.setString1("CHANGED");
        assertTrue(!mfc.isMatchFieldChanged(mfc.getMatchFields(eo1), mfc.getMatchFields(eo2)));
        person.setFirstName("CHANGED");
        assertTrue(mfc.isMatchFieldChanged(mfc.getMatchFields(eo1), mfc.getMatchFields(eo2))); 
    }
    
    private EnterpriseObject getEO() throws Exception {
        MasterController mc = MCFactory.getMasterController();
        SystemObjectPK key = new SystemObjectPK("SiteA", "0001");
        EnterpriseObject eo = mc.getEnterpriseObject(key);
        return eo;
    }
}
