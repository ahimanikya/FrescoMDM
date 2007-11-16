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
import junit.framework.TestSuite;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.helper.UnmergeSystemObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class MergeSystemObject extends TestCase {
    
    private MasterController mc;
    private EOSearchOptions searchOptions;
    private EOSearchCriteria criteria;
    private String mEuid1;
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeSystemObject(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
   
    /** Tests an SO merge within same EUID
     * @throws Exception an error occured
     */
    public void testMergeSystemObject1m() throws Exception {
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Merge the records (same EUID merge)
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("*** TEST 1 ***");
        String[] mergeCommand = {"systemCode=SiteA", "lidSource=0001", "lidDest=0002", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand);
        log("Merge completed.");
        
        //Test assertions
        GetEnterpriseObjectHelper helper2 = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = helper2.run(new String[] {"system=SiteA", "lid=0002"});
        SystemObject soSource = eo.getSystemObject("SiteA", "0001");
        SystemObject soDest = eo.getSystemObject("SiteA", "0002");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));
    }
    
    /** Tests an SO unmerge within same EUID
     * @throws Exception an error occured
     */
    public void testMergeSystemObject1u() throws Exception {
        testMergeSystemObject1m();
        //Unmerge the records
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand = {"systemCode=SiteA", "lidSource=0001", "lidDest=0002", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand);
        log("Unmerge completed.");     
        
        //Test assertions
        GetEnterpriseObjectHelper helper2 = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = helper2.run(new String[] {"system=SiteA", "lid=0002"});
        SystemObject soSource = eo.getSystemObject("SiteA", "0001");
        SystemObject soDest = eo.getSystemObject("SiteA", "0002");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));
    }
    
    /** Tests an SO merge within same EUID.  This time send the
     * destination image
     * @throws Exception an error occured
     */
    public void testMergeSystemObject2m() throws Exception {
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Get the EO
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        String[] eoCommand = {"system=SiteA", "lid=0001"};
        EnterpriseObject eo = eoHelper.run(eoCommand);
        SystemObject so1 = eo.getSystemObject("SiteA", "0001");
        SystemObject so2 = eo.getSystemObject("SiteA", "0002");
        PersonObject personObj = (PersonObject) so2.getObject();
        
        //Merge the records (same EUID merge)
        log("*** TEST 2 ***");
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        MasterController mc0 = MCFactory.getMasterController();
        mc0.mergeSystemObject("SiteA", "0001", 
        "0002", personObj, false);
        log("Merge completed.");
        
        //Test assertions
        eo = eoHelper.run(new String[] {"system=SiteA", "lid=0002"});
        SystemObject soSource = eo.getSystemObject("SiteA", "0001");
        SystemObject soDest = eo.getSystemObject("SiteA", "0002");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        
    }
    
    /** Tests an SO unmerge within same EUID.  This time send the
     * destination image
     * @throws Exception an error occured
     */
    public void testMergeSystemObject2u() throws Exception {    
        testMergeSystemObject2m();
        //Unmerge the records
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand = {"systemCode=SiteA", "lidSource=0001", "lidDest=0002", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand);
        log("Unmerge completed.");        
    }    

    /** Tests an SO merge with cross EUID
     * @throws Exception an error occured
     */
    public void testMergeSystemObject3m() throws Exception {    
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Merge the records (cross EUID merge)
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("*** TEST 3 ***");
        String[] mergeCommand2 = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand2);
        log("Merge completed.");
        
        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo = eoHelper.run(new String[] {"system=SiteA", "lid=0005"});
        SystemObject soSource = eo.getSystemObject("SiteA", "0003");
        SystemObject soDest = eo.getSystemObject("SiteA", "0005");
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));            
        
    }
    
    /** Tests an SO unmerge with cross EUID
     * @throws Exception an error occured
     */
    public void testMergeSystemObject3u() throws Exception {        
        testMergeSystemObject3m();
        //Unmerge the records
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand2 = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand2);
        log("Unmerge completed.");

        //Test assertions
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        EnterpriseObject eo1 = eoHelper.run(new String[] {"system=SiteA", "lid=0005"});
        SystemObject soDest = eo1.getSystemObject("SiteA", "0005");
        EnterpriseObject eo2 = eoHelper.run(new String[] {"system=SiteA", "lid=0003"});
        SystemObject soSource = eo2.getSystemObject("SiteA", "0003");
        assertTrue(!eo1.getEUID().equals(eo2.getEUID()));
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_ACTIVE));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));         
    }

    /** Tests an SO merge and unmerge with cross EUID - this time send
     * destination image
     * @throws Exception an error occured
     */
    public void testMergeSystemObject4() throws Exception {    
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Get the EO
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        String[] eoCommand = {"system=SiteA", "lid=0005"};
        EnterpriseObject eo = eoHelper.run(eoCommand);
        SystemObject so = eo.getSystemObject("SiteA", "0005");
        PersonObject personObj = (PersonObject) so.getObject();
        
        //Merge the records (same EUID merge)
        log("*** TEST 4 ***");
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        MasterController mc0 = MCFactory.getMasterController();
        mc0.mergeSystemObject("SiteA", "0003", 
        "0005", personObj, false);
        log("Merge completed.");
         
        //Unmerge the records
        UnmergeSystemObjectHelper unmergeHelper = new UnmergeSystemObjectHelper();
        log("Unmerging records.");
        String[] unmergeCommand = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        unmergeHelper.run(unmergeCommand);
        log("Unmerge completed.");        
    }    
    
    /** Tests an SO merge and unmerge with cross EUID. This time perform
     * merge twice, should cause deactivation of EO that has no more SO's.
     * @throws Exception an error occured
     */
    public void testMergeSystemObject5() throws Exception {    
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject1.txt", "fileType=eiEvent"});
        //Merge the records (cross EUID merge)
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("*** TEST 5 ***");
        String[] mergeCommand = {"systemCode=SiteA", "lidSource=0003", "lidDest=0005", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand);

        //Merge the records (cross EUID merge).  Should cause deactivation of 
        //source EO (since it has no more SO's).
        String[] mergeCommand2 = {"systemCode=SiteA", "lidSource=0004", "lidDest=0005", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand2);
        log("Merges completed.");
        
        //Test assertions
        log("Testing assertions.");  
    }  
    
    public void testMergeSystemObject6() throws Exception {    
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        List list = helper.run(new String[] {"fileName=MergeSystemObject2.txt", "fileType=eiEvent"});
        //Merge the records (cross EUID merge)
        MergeSystemObjectHelper mergeHelper = new MergeSystemObjectHelper();
        log("*** TEST 6 ***");
        String[] mergeCommand = {"systemCode=SiteA", "lidSource=0001", "lidDest=0002", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand);

        String[] mergeCommand2 = {"systemCode=SiteA", "lidSource=0003", "lidDest=0004", "calcOnly=false"}; 
        mergeHelper.run(mergeCommand2);
        
        String euidSource = ((EnterpriseObject)list.get(1)).getEUID();
        String euidDest = ((EnterpriseObject)list.get(3)).getEUID();
        String[] mergeCommand3 = {"euidSource=" + euidSource, "euidDest=" + euidDest, "calcOnly=false"};  
        MergeEnterpriseObjectHelper euidMergeHelper = new MergeEnterpriseObjectHelper();
        euidMergeHelper.run(mergeCommand3);
        
        log("Merges completed.");
        
        //Test assertions
        log("Testing assertions.");  
        UnmergeEnterpriseObjectHelper helper2 = new UnmergeEnterpriseObjectHelper();
        String[] unmergeCommand = {"euid=" + euidDest, "calcOnly=false"}; 
        helper2.run(unmergeCommand);
    } 
    
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new MergeSystemObject(args[i]));
            }
        } else {
            ts = new TestSuite(MergeSystemObject.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
