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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.GetEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AddressObject;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/** Test class for LID merge with deleting a destination child as defined in the
 * eIndex50.xml file
 * @author rtam
 */
public class MergeLIDUpdate extends TestCase {
    
    /** Creates new SearchEOTester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public MergeLIDUpdate(String name) {
        super(name);
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /* Test an LID merge for proper handling of "removed" child objects.  "removed"
     * does NOT mean that the object was marked for removal in the desired image.
     * Instead, it means that the cihld is is eradicated from the desired image 
     * and not found in the image sent to the MasterController for processing.  
     * This simulates a LID merge from the EDM where the destination address is 
     * removed.  The source address should replace the destination address.
     * @throws Exception object access exception
     */
    public void testMergeLIDUpdate() throws Exception {
        
        CreateEnterpriseObjectHelper helper = new CreateEnterpriseObjectHelper();
        helper.run(new String[] {"fileName=MergeSystemObject3.txt", "fileType=eiEvent"});
        
        String systemCode = "SiteA";
        String lidSource = "0001";
        String lidDest = "0003";
        String childType = "Address";
        
        GetEnterpriseObjectHelper eoHelper = new GetEnterpriseObjectHelper();
        String[] eoCommand = {"system=" + systemCode, "lid=" + lidSource};
        String[] eoCommand2 = {"system=" + systemCode, "lid=" + lidDest};
        EnterpriseObject eoSource = eoHelper.run(eoCommand);
        EnterpriseObject eoDest = eoHelper.run(eoCommand2);
        SystemObject soSource = eoSource.getSystemObject(systemCode, lidSource);
        SystemObject soDest = eoDest.getSystemObject(systemCode, lidDest);

        //  Remove the destination address 
        PersonObject personObjDest = (PersonObject) soDest.getObject();
        personObjDest.removeChildren("Address");

        //  Retrieve the source address and add to the destination.
        PersonObject personObjSource = (PersonObject) soSource.pGetChildren("Person").get(0);
        AddressObject addressObj = null;
        String targetCity = null; 
        Collection addressCollection = personObjSource.getAddress();
        Iterator addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            addressObj = (AddressObject) addressIter.next();
            targetCity = addressObj.getCity(); 
        }
        personObjDest.addChild(addressObj);
        
        // Send the final, desired image to be merged
        log("*** Merging records ***");
        MasterController mc0 = MCFactory.getMasterController();
        mc0.mergeSystemObject(systemCode, lidSource, lidDest, personObjDest, false);
        log("*** Merge completed. ***");
        
        //Test assertions
        eoDest = eoHelper.run(new String[] {"system=" + systemCode, "lid=" + lidDest});
        soSource = eoDest.getSystemObject(systemCode, lidSource);
        soDest = eoDest.getSystemObject(systemCode, lidDest);
        assertTrue(soSource.getStatus().equals(SystemObject.STATUS_MERGED));
        assertTrue(soDest.getStatus().equals(SystemObject.STATUS_ACTIVE));        
        
                                                       
        PersonObject newPersonObj = (PersonObject) soDest.pGetChildren("Person").get(0);
        addressCollection = newPersonObj.getAddress();
        assertTrue(addressCollection.size() == 1);  // there should be only one address
        addressIter = addressCollection.iterator();
        while (addressIter.hasNext())  {
            addressObj = (AddressObject) addressIter.next();
            assertTrue(addressObj != null);
            String city = addressObj.getCity(); 
            assertTrue(city.equalsIgnoreCase(targetCity)); 
            log(targetCity + " found as expected.");
        }
        
    }    
    
   
    private void log(String msg) {
        System.out.println(msg);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(MergeLIDUpdate.class));
    }
}
