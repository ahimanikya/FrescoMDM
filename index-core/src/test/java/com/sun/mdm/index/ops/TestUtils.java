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

import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.AddressObject;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.ejbproxy.EJBTestProxy;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * test util
 * @author gzheng
 */
public class TestUtils {
    /**
     * systemcode
     */
    public static final String SYSTEMCODE = "SiteA";
    
    /**
     * local id
     */
    public static final String LOCALID = "3000";

    private static int counter = 0;
    /**
     * add
     */
    protected static final int ADD = counter++;
    /**
     * addphone
     */
    protected static final int ADDPHONE = counter++;
    /**
     * addoverwrite
     */
    protected static final int ADDOVERWRITE = counter++;
    /**
     * addalias
     */
    protected static final int ADDALIAS = counter++;
    /**
     * add 2nd alias
     */
    protected static final int ADDALIAS2 = counter++;
    /**
     * remove alias
     */
    protected static final int REMOVEALIAS = counter++;
    /**
     * add alias sbr
     */
    protected static final int ADDALIASSBR = counter++;
    /**
     * delete phone sbr
     */
    protected static final int DELETEPHONESBR = counter++;
    /**
     * replace person sbr
     */
    protected static final int REPLACEPERSONSBR = counter++;
    /**
     * update so
     */
    protected static final int UPDATESOS = counter++;
    /**
     * add 2nd enterprise object
     */
    protected static final int ADD2 = counter++;
    /**
     * euid merge
     */
    protected static final int EUIDMERGE = counter++;
    /**
     * euid merge 2
     */
    protected static final int EUIDMERGE2 = counter++;
    /**
     * alias update
     */
    protected static final int UPDATEALIAS = counter++;
    
    private static ArrayList<String> euids;
    private static ArrayList<String> tmids;
    private static Context context = null;
    
    /**
     * default constructor
     * @exception NamingException naming exception
     */
    public TestUtils() throws NamingException {
        if (context == null) {
            context = EJBTestProxy.getInitialContext();
        }
    }
    
    
    /**
     * get euid
     * @return String euid
     */
    public String getEUID() {
        return (String) euids.get(euids.size() - 1);
    }
    
    /**
     * get euid
     * @param n position
     * @return euid euid
     */
    public String getEUID(int n) {
        return (String) euids.get(n);
    }
    
    /**
     * get tmid
     * @param n position
     * @return euid euid
     */
    public String getTMID(int n) {
        return (String) tmids.get(n);
    }
    
    /**
     * get tmid
     * @return euid euid
     */
    public String getTMID() {
        return (String) tmids.get(tmids.size() - 1);
    }
    
    /**
     * set euid 
     * @param id euid
     */
    public void setEUID(String id) {
        if (euids == null) {
            euids = new ArrayList<String>();
        }
        euids.add(id);
    }
    
    /**
     * set tmid
     * @param id tmid
     */
    public void setTMID(String id) {
        if (tmids == null) {
            tmids = new ArrayList<String>();
        }
        tmids.add(id);
    }
    

    /**
     * create sample so
     * @param systemcode system code
     * @param lid local id
     * @param id id
     * @return SystemObject system object
     */
    public SystemObject createSO(String systemcode, String lid, String id) {
        SystemObject sysobj = null;
        try {
            PhoneObject phone = new PhoneObject();
            
            phone.setValue("PhoneType", "home" + id);
            phone.setValue("Phone", "phone3" + id);
            phone.setValue("PhoneExt", "639" + id);
            
            // create a new person object
            PersonObject person = new PersonObject();
            person.setLastName("Zheng2" + id);
            person.setFirstName("Gary2" + id);
            person.setGender("Male" + id);
            
            // add 'phone' to person
            person.addChild(phone);
            
            // create yet another phone
            phone = new PhoneObject();
            phone.setPhoneType("biz" + id);
            phone.setPhone("phone4" + id);
            phone.setPhoneExt("NO" + id);
            
            // add 'phone to person
            person.addChild(phone);
            
            // create a new address
            AddressObject address = new AddressObject();
            address.setAddressType("home" + id);
            address.setAddressLine1("address3" + id);
            address.setAddressLine2("#b" + id);
            address.setCity("Rowland Heights" + id);
            address.setStateCode("calif" + id);
            address.setPostalCode("98" + id);
            
            // add address to person
            person.addChild(address);
            
            // create yet another address
            address = new AddressObject();
            address.setAddressType("biz" + id);
            address.setAddressLine1("address4" + id);
            address.setAddressLine2("  " + id);
            address.setCity("Monrovia" + id);
            address.setStateCode("calif" + id);
            address.setPostalCode("91016" + id);
            
            // add address to person
            person.addChild(address);
                
            sysobj = new SystemObject(systemcode, lid, "Person", "active",
                              "Admin", "add", null, "Admin", "add", null, person);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return sysobj;
    }

    
    /**
     * create sample EO
     * @param euid euid
     * @param systemcode system code
     * @param lid local id
     * @return EnterpriseObject enterprise object
     */
    public EnterpriseObject createEO(String euid, String systemcode, String lid) {
        EnterpriseObject ret = null;
        try {    
            PhoneObject phone = new PhoneObject();
            
            phone.setValue("PhoneType", "home");
            phone.setValue("Phone", "phone1");
            phone.setValue("PhoneExt", "639");
            
            // create a new person object
            PersonObject person = new PersonObject();
            person.setLastName("Zheng1");
            person.setFirstName("Gary1");
            person.setGender("Male");
            
            // add 'phone' to person
            person.addChild(phone);
            
            // create yet another phone
            phone = new PhoneObject();
            phone.setPhoneType("biz");
            phone.setPhone("phone2");
            phone.setPhoneExt("NO");
            
            // add 'phone to person
            person.addChild(phone);
            
            // create a new address
            AddressObject address = new AddressObject();
            address.setAddressType("home");
            address.setAddressLine1("address1");
            address.setAddressLine2("#b");
            address.setCity("Rowland Heights");
            address.setStateCode("calif");
            address.setPostalCode("98");
            
            // add address to person
            person.addChild(address);
            
            // create yet another address
            address = new AddressObject();
            address.setAddressType("biz");
            address.setAddressLine1("address2");
            address.setAddressLine2("  ");
            address.setCity("Monrovia");
            address.setStateCode("calif");
            address.setPostalCode("91016");
            
            // add address to person
            person.addChild(address);
                
            
            PhoneObject phonesbr = new PhoneObject();
            
            phonesbr.setValue("PhoneType", "home");
            phonesbr.setValue("Phone", "phonesbr");
            
            // create a new person object
            PersonObject personsbr = new PersonObject();
            personsbr.setLastName("ZhengSBR");
            personsbr.setFirstName("Gary");
            personsbr.setGender("Male");
            
            // add 'phone' to person
            personsbr.addChild(phonesbr);
            
            // create yet another phone
            phonesbr = new PhoneObject();
            phonesbr.setPhoneType("biz");
            phonesbr.setPhone("phsbr2");
            
            // add 'phone to person
            personsbr.addChild(phonesbr);
            
            // create a new address
            AddressObject addresssbr = new AddressObject();
            addresssbr.setAddressType("home");
            addresssbr.setAddressLine1("addresssbr1");
            addresssbr.setAddressLine2("#b");
            addresssbr.setCity("Rowland Heights");
            addresssbr.setStateCode("calif");
            addresssbr.setPostalCode("98");
            
            
            // add address to person
            personsbr.addChild(addresssbr);
            
            // create yet another address
            addresssbr = new AddressObject();
            addresssbr.setAddressType("biz");
            addresssbr.setAddressLine1("addresssbr2");
            addresssbr.setAddressLine2("  ");
            addresssbr.setCity("Monrovia");
            addresssbr.setStateCode("calif");
            addresssbr.setPostalCode("91016");
        
            // add address to person
            personsbr.addChild(addresssbr);
                
            SBR sbr = new SBR("Person", "Admin", "SBR", "add",
                    null, "Admin", "SBR", "add",
                    null, SystemObject.STATUS_ACTIVE, personsbr);
        
            SystemObject sysobj = new SystemObject(systemcode, lid, "Person", "active",
                              "Admin", "add", null, "Admin", "add", null, person);
                
            ArrayList<SystemObject> sysobjs = new ArrayList<SystemObject>();
            sysobjs.add(sysobj);


            phone = new PhoneObject();
            
            phone.setValue("PhoneType", "home");
            phone.setValue("Phone", "phone3");
            phone.setValue("PhoneExt", "639");
            
            // create a new person object
            person = new PersonObject();
            person.setLastName("Zheng2");
            person.setFirstName("Gary2");
            person.setGender("Male");
            
            // add 'phone' to person
            person.addChild(phone);
            
            // create yet another phone
            phone = new PhoneObject();
            phone.setPhoneType("biz");
            phone.setPhone("phone4");
            phone.setPhoneExt("NO");
            
            // add 'phone to person
            person.addChild(phone);
            
            // create a new address
            address = new AddressObject();
            address.setAddressType("home");
            address.setAddressLine1("address3");
            address.setAddressLine2("#b");
            address.setCity("Rowland Heights");
            address.setStateCode("calif");
            address.setPostalCode("98");
            
            // add address to person
            person.addChild(address);
            
            // create yet another address
            address = new AddressObject();
            address.setAddressType("biz");
            address.setAddressLine1("address4");
            address.setAddressLine2("  ");
            address.setCity("Monrovia");
            address.setStateCode("calif");
            address.setPostalCode("91016");
            
            // add address to person
            person.addChild(address);
                


            sysobj = new SystemObject(systemcode, lid + "a", "Person", "active",
                              "Admin", "add", null, "Admin", "add", null, person);

            sysobjs.add(sysobj);            
            ret = new EnterpriseObject(euid, sbr, sysobjs); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;        
    }

    /**
     * get connection
     * @return Connection jdbc connection
     * @exception NamingException naming exception
     * @exception SQLException sql exception
     */
    public Connection getConnection() throws NamingException, SQLException {
        DataSource ds = (DataSource) context.lookup(JNDINames.BBE_DATASOURCE);
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        return con;
    }
    
    /**
     * get transaction manager
     * @return TransactionMgr transaction manager
     * @exception OPSException ops exception
     */
    public TransactionMgr getTransactionMgr() throws OPSException {
        return TransactionMgrFactory.getInstance();
    }
    
    /**
     * clean up database
     * @exception SQLException sql exception
     * @exception NamingException naming exception
     */
    public void clearDB() throws SQLException, NamingException {
        Connection con = getConnection();
        ExecuteMatchHelper executeMatchHelper = new ExecuteMatchHelper(); 
        executeMatchHelper.clearDb(con);
        
        /*
        Statement stmt = con.createStatement();

        stmt.executeUpdate("delete from SBYN_ENTERPRISE");
        stmt.executeUpdate("delete from SBYN_POTENTIALDUPLICATES");
        stmt.executeUpdate("delete from SBYN_PHONE");
        stmt.executeUpdate("delete from SBYN_PHONESBR");
        stmt.executeUpdate("delete from SBYN_ALIAS");
        stmt.executeUpdate("delete from SBYN_ALIASSBR");
        stmt.executeUpdate("delete from SBYN_ADDRESS");
        stmt.executeUpdate("delete from SBYN_ADDRESSSBR");
        stmt.executeUpdate("delete from SBYN_AUXID");
        stmt.executeUpdate("delete from SBYN_AUXIDSBR");
        stmt.executeUpdate("delete from SBYN_COMMENT");
        stmt.executeUpdate("delete from SBYN_COMMENTSBR");
        stmt.executeUpdate("delete from SBYN_PERSON");
        stmt.executeUpdate("delete from SBYN_PERSONSBR");
        stmt.executeUpdate("delete from SBYN_OVERWRITE");
        stmt.executeUpdate("delete from SBYN_SYSTEMOBJECT");
        stmt.executeUpdate("delete from SBYN_SYSTEMSBR");
        stmt.executeUpdate("delete from SBYN_ASSUMEDMATCH");
        stmt.executeUpdate("delete from SBYN_MERGE");
        stmt.executeUpdate("delete from SBYN_TRANSACTION");
        stmt.executeUpdate("delete from SBYN_AUDIT");
        
        con.commit();
         */
    }
}
