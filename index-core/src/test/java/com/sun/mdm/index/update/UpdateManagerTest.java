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
package com.sun.mdm.index.update;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.ejb.master.helper.ClearDb;
import com.sun.mdm.index.ejb.master.helper.BasicHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.util.Constants;
import java.sql.Connection;

/** Test class
 * @author dcidon
 */
public class UpdateManagerTest extends TestCase {
    
    private static UpdateManager mUpdate;
    private static MasterController mMaster;
    private static Connection mCon;
    
    /** Creates new tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdateManagerTest(String name) throws Exception {
        super(name);
        if (mUpdate == null) {
            mUpdate = UpdateManagerFactory.getInstance();
            mCon = MCFactory.getConnection();
            mMaster = MCFactory.getMasterController();
            mCon.setAutoCommit(false);
        }
    }
    
    /** Set up the unit test
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
        ClearDb.run();
    }
    
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#createEnterpriseObject
     * Single SystemObject created
     */
    public void testCreateEnterpriseObject() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];        
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        //Check if EnterpriseObject created equals result value
        EnterpriseObject eo1 = result.getEnterpriseObject1();
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(eo1.getEUID());
        assertTrue(helper.equalEnterpriseObject(eo1, eo2));        
    }

    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#createEnterpriseObject
     * Multiple system objects created at once
     */
    public void testCreateEnterpriseObject2() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest2.txt", "fileType=generic"});
        SystemObject sysObjs[] = helper.getNextRecord().getSystemObjects();        
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObjs);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        //Check if EnterpriseObject created equals result value
        EnterpriseObject eo1 = result.getEnterpriseObject1();
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(eo1.getEUID());
        assertTrue(helper.equalEnterpriseObject(eo1, eo2)); 
    }
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#updateEnterprise
     * method signature 1 (eo only)
     */
    public void testUpdateEnterprise() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;

        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        
        //Update the record - modify String1 from null to "My String 1"
        EnterpriseObject eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        PersonObject person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        person.setString1("My String 1");
        try {
            result = mUpdate.updateEnterprise(mCon, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1().equals("My String 1")); 
        
        //Update the record - modify String1 from "My String 1" to null
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        person.setString1(null);
        try {
            result = mUpdate.updateEnterprise(mCon, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1()==null); 
        
        //Update the record - add a phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        PhoneObject phone = new PhoneObject();
        phone.setPhoneType("CO");
        phone.setPhone("5551212");
        person.addPhone(phone);
        try {
            result = mUpdate.updateEnterprise(mCon, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getPhone().size()==1); 
        
        //Update the record - remove phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        phone = (PhoneObject) person.getPhone().iterator().next();
        phone.setRemoveFlag(true);
        try {
            result = mUpdate.updateEnterprise(mCon, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getPhone()==null); 
        
        //Update the record - add sys obj
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest3.txt", "fileType=generic"});
        sysObj = helper.getNextRecord().getSystemObjects()[0];
        eo.addSystemObject(sysObj);
        sysCode = sysObj.getSystemCode();
        lid = sysObj.getLID();
        try {
            result = mUpdate.updateEnterprise(mCon, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        assertTrue(eo2.getSystemObject(sysCode, lid)!=null); 
    }

    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#updateEnterprise
     * method signature 2 (eo and so), replace SO
     */
    public void testUpdateEnterprise2() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;
        
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Update the record
        EnterpriseObject eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        PersonObject person = (PersonObject) sysObj.getObject();
        person.setString1("My String 1");
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1().equals("My String 1")); 
        
        //Update the record - modify String1 from "My String 1" to null
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        sysObj = eo.getSystemObject(sysCode, lid);
        person = (PersonObject) sysObj.getObject();
        person.setString1(null);
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1()==null); 
        
        //Update the record - add a phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        sysObj = eo.getSystemObject(sysCode, lid);
        person = (PersonObject) sysObj.getObject();
        PhoneObject phone = new PhoneObject();
        phone.setPhoneType("CO");
        phone.setPhone("5551212");
        person.addPhone(phone);
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getPhone().size()==1); 
        
        //Update the record - remove phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        sysObj = eo.getSystemObject(sysCode, lid);
        person = (PersonObject) sysObj.getObject();
        phone = (PhoneObject) person.getPhone().iterator().next();
        phone.setRemoveFlag(true);
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getPhone()==null); 
        
        //Update the record - add sys obj
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest3.txt", "fileType=generic"});
        sysObj = helper.getNextRecord().getSystemObjects()[0];
        sysCode = sysObj.getSystemCode();
        lid = sysObj.getLID();
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        assertTrue(eo2.getSystemObject(sysCode, lid)!=null); 
        
    }
    
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#updateEnterprise
     * method signature 2 (eo and so), update SO if changed
     */
    public void testUpdateEnterprise3() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        SystemObject sysObj2 = (SystemObject) sysObj.copy();
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Update the record
        EnterpriseObject eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        PersonObject person = (PersonObject) sysObj.getObject();
        person.setString1("My String 1");
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, Constants.FLAG_UM_REPLACE_SO, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1().equals("My String 1")); 
        
        //Update the record - set String2 "My String 2"
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) sysObj2.getObject();
        person.setString2("My String 2");
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj2, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid - String1 is unchanged, String2 is set
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getString1().equals("My String 1")); 
        assertTrue(person.getString2().equals("My String 2"));
        
        //Update the record - add a phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        sysObj = eo.getSystemObject(sysCode, lid);
        person = (PersonObject) sysObj.getObject();
        PhoneObject phone = new PhoneObject();
        phone.setPhoneType("CB");
        phone.setPhone("5551000");
        person.addPhone(phone);
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        PhoneObject[] phoneArr = (PhoneObject[]) person.getPhone().toArray(new PhoneObject[0]);
        assertTrue(phoneArr[0].getPhone().equals("5551000"));
        
        //Update the record - add another phone
        eo = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        sysObj = eo.getSystemObject(sysCode, lid);
        person = (PersonObject) sysObj.getObject();
        phone = new PhoneObject();
        phone.setPhoneType("CH");
        phone.setPhone("5551234");
        person.addPhone(phone);
        try {
            result = mUpdate.updateEnterprise(mCon, sysObj, eo, 0, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();
        
        //Check if EnterpriseObjectCreated is valid
        eo2 = mMaster.getEnterpriseObject(result.getEnterpriseObject1().getEUID());
        person = (PersonObject) eo2.getSystemObject(sysCode, lid).getObject();
        assertTrue(person.getPhone().size()==2); 
        
    }    

    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#transferSystem
     * this case, deactivation of EO should occur
     */
    public void testTransferSystem() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid1 = result.getEnterpriseObject1().getEUID();
        
        helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest3.txt", "fileType=generic"});
        sysObj = helper.getNextRecord().getSystemObjects()[0];
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid2 = result.getEnterpriseObject1().getEUID();
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            mUpdate.transferSystem(mCon, eo1, eo2, sysCode, lid, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo2.getSystemObject(sysCode, lid) != null);
        //Bug - trans mgr is removing SBR rather than changing status to inactive
        //assertTrue(eo1.getSBR().getStatus().equals(SystemObject.STATUS_INACTIVE));
    }
     
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#transferSystem
     * this case, no deactivation of EO should occur
     */
    public void testTransferSystem2() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest2.txt", "fileType=generic"});
        SystemObject[] sysObjs = helper.getNextRecord().getSystemObjects();
        String sysCode = sysObjs[0].getSystemCode();
        String lid = sysObjs[0].getLID();
        String euid1 = mMaster.createEnterpriseObject(sysObjs).getEUID();
        
        helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest3.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid2 = result.getEnterpriseObject1().getEUID();
        
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        try {
            mUpdate.transferSystem(mCon, eo1, eo2, sysCode, lid, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo1 = mMaster.getEnterpriseObject(euid1);
        eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode, lid) == null);
        assertTrue(eo2.getSystemObject(sysCode, lid) != null);
    }
    
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#removeSystem
     */
    public void testRemoveSystem() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest2.txt", "fileType=generic"});
        SystemObject[] sysObjs = helper.getNextRecord().getSystemObjects();
        String sysCode = sysObjs[0].getSystemCode();
        String lid = sysObjs[0].getLID();
        String euid1 = mMaster.createEnterpriseObject(sysObjs).getEUID();
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        assertTrue(eo1.getSystemObject(sysCode, lid) != null);
        try {
            mUpdate.removeSystem(mCon, eo1, sysCode, lid, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo1 = mMaster.getEnterpriseObject(euid1);
        assertTrue(eo1.getSystemObject(sysCode, lid) == null);
    }

    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#splitSystem
     */
    public void testSplitSystem() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest2.txt", "fileType=generic"});
        SystemObject[] sysObjs = helper.getNextRecord().getSystemObjects();
        String sysCode1 = sysObjs[0].getSystemCode();
        String lid1 = sysObjs[0].getLID();
        String sysCode2 = sysObjs[1].getSystemCode();
        String lid2 = sysObjs[1].getLID();
        String euid1 = mMaster.createEnterpriseObject(sysObjs).getEUID();
        EnterpriseObject eo1 = mMaster.getEnterpriseObject(euid1);
        assertTrue(eo1.getSystemObject(sysCode1, lid1) != null);
        assertTrue(eo1.getSystemObject(sysCode2, lid2) != null);
        UpdateResult result = null; 
        try {
            result = mUpdate.splitSystem(mCon, sysCode1, lid1, eo1, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid2 = result.getEnterpriseObject2().getEUID();
        eo1 = mMaster.getEnterpriseObject(euid1);
        EnterpriseObject eo2 = mMaster.getEnterpriseObject(euid2);
        assertTrue(eo1.getSystemObject(sysCode1, lid1) == null);
        assertTrue(eo2.getSystemObject(sysCode1, lid1) != null);
        assertTrue(eo1.getSystemObject(sysCode2, lid2) != null);
        assertTrue(eo2.getSystemObject(sysCode2, lid2) == null);
    }

    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#deactivateEnterprise
     */
    public void testDeactivateEnterprise() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid = result.getEnterpriseObject1().getEUID();
        EnterpriseObject eo = mMaster.getEnterpriseObject(euid);
        try {
            mUpdate.deactivateEnterprise(mCon, eo, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo = mMaster.getEnterpriseObject(euid);
        assertTrue(eo.getSBR().getStatus().equals(SystemObject.STATUS_INACTIVE));
    }
    
    /**
     * @see com.sun.mdm.index.ejb.update.UpdateManagerEJB#activateEnterprise
     */
    public void testActivateEnterprise() throws Exception {
        BasicHelper helper = new BasicHelper();
        helper.setArgs(new String[] {"fileName=UpdateManagerTest1.txt", "fileType=generic"});
        SystemObject sysObj = helper.getNextRecord().getSystemObjects()[0];
        String sysCode = sysObj.getSystemCode();
        String lid = sysObj.getLID();
        UpdateResult result = null;
        try {
            result = mUpdate.createEnterpriseObject(mCon, sysObj);
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        String euid = result.getEnterpriseObject1().getEUID();
        EnterpriseObject eo = mMaster.getEnterpriseObject(euid);
        try {
            mUpdate.deactivateEnterprise(mCon, eo, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo = mMaster.getEnterpriseObject(euid);
        assertTrue(eo.getSBR().getStatus().equals(SystemObject.STATUS_INACTIVE));
        try {
            mUpdate.activateEnterprise(mCon, eo, "user");
        } catch (Exception e) {
            mCon.rollback(); 
            throw e;
        }
        mCon.commit();

        eo = mMaster.getEnterpriseObject(euid);
        assertTrue(eo.getSBR().getStatus().equals(SystemObject.STATUS_ACTIVE));
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) throws Exception {
        TestSuite ts = null;
        if (args.length > 0) {
            ts = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                ts.addTest(new UpdateManagerTest(args[i]));
            }
        } else {
            ts = new TestSuite(UpdateManagerTest.class);
        }
        junit.textui.TestRunner.run(ts);
    }
    
}
