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
 
package com.sun.mdm.index.ejb.master.helper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Date;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.naming.Context;

import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.AliasObject;
import com.sun.mdm.index.objects.PhoneObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.metadata.ObjectFactory;

/**
 * Base class for helpers.
 * @author  dcidon
 */
public class BasicHelper {
    
    private static Context mContext;
    private static MasterController mMasterController;
    
    /** Hashtable of argument values
     */
    private final Hashtable mArgValues = new Hashtable();
    
    private SystemObjectIterator mSystemObjectIterator;
    
    /** Creates a new instance of AbstractHelper */
    public BasicHelper() {        
    }
    
    /** Set arguments
     * @param args arguments
     */
    public void setArgs(String args[]) {
        mArgValues.clear();
        mSystemObjectIterator = null;
        for (int i = 0; i < args.length; i++) {
            String[] value = args[i].split("=");
            mArgValues.put(value[0], value[1]);
        }
    }
    
    private SystemObjectIterator getSystemObjectIterator() throws Exception {
        if (mSystemObjectIterator == null) {
            String dataFile = (String) mArgValues.get("fileName");
            if (dataFile == null || dataFile.length() == 0) {
                throw new Exception("File name not populated.");
            }
            String fileFormat = (String) mArgValues.get("fileType");
            if (fileFormat == null || fileFormat.length() == 0) {
                throw new Exception("File type not populated.");
            }
            //InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(dataFile);
            InputStream stream = BasicHelper.class.getClassLoader().getResourceAsStream(dataFile);
            if (stream == null) {
                throw new Exception("Data file not found: " + dataFile);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            if (fileFormat.equalsIgnoreCase("eiEvent")) {
                mSystemObjectIterator = new EiEventIterator(MCFactory.getMasterController(), reader);
            } else if (fileFormat.equalsIgnoreCase("generic")) {
                mSystemObjectIterator = new GenericIterator(reader);
            } else {
                throw new Exception("Invalid file type: " + fileFormat);
            }
        }
        return mSystemObjectIterator;
    }
    
    /** Gets next record from data file.  Return null if no more records.
     * @throws Exception An error occured
     * @return New system object
     */
    public IteratorRecord getNextRecord() throws Exception {
        
        SystemObjectIterator iterator = getSystemObjectIterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }
    
    /**
     * Get string value
     * @param paramName parameter name
     * @return string value
     */
    public String getStringValue(String paramName) {
        return (String) mArgValues.get(paramName);
    }
    
    /**
     * Get boolean value
     * @param paramName parameter name
     * @throws Exception invalid value
     * @return boolean value
     */
    public boolean getBooleanValue(String paramName) throws Exception {
        String sVal = (String) mArgValues.get(paramName);
        if (sVal == null) {
            return false;
        }
        if (sVal.equalsIgnoreCase("true")) {
            return true;
        } else if (sVal.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new Exception("Invalid boolean value: " + sVal);
        }
    }

    /**
     * Get integer value
     * @param paramName parameter name
     * @throws Exception invalid value
     * @return integer value or null
     */
    public Integer getIntegerValue(String paramName) throws Exception {
        String sVal = (String) mArgValues.get(paramName);
        if (sVal == null) {
            return null;
        }
        return new Integer(sVal);
    }    
    
    /**
     * Get date value
     * @param paramName parameter name
     * @throws Exception invalid value
     * @return date value
     */
    public Date getDateValue(String paramName) throws Exception {
        String sVal = (String) mArgValues.get(paramName);
        if (sVal == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(sVal);
    }
    
    /**
     * Get date time value
     * @param paramName parameter name
     * @throws Exception invalid value
     * @return date value
     */
    public Date getDateTimeValue(String paramName) throws Exception {
        String sVal = (String) mArgValues.get(paramName);
        if (sVal == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
        return sdf.parse(sVal);
    }
    
    /** Clear the database tables
     * @throws Exception an error occured
     */
    public void clearDb() throws Exception {
        Connection con = MCFactory.getConnection();
        clearDb(con);
        con.close();        
    }
    
    /** Clear the database tables
     * @param con db connection
     * @throws Exception an error occured
     */
    public void clearDb(Connection con) throws SQLException {
        Statement s = con.createStatement();
        for (int i = 0; i < TestConstants.TABLES.length; i++) {
            String sql = "DELETE FROM " + TestConstants.TABLES[i];
            //System.out.println("Deleting from table: " + TestConstants.TABLES[i]);
            s.executeUpdate(sql);
        }
        
        String systemDate = null;
        String dbType = new String(ObjectFactory.getDatabase());
        if (dbType.equalsIgnoreCase("Oracle")) {
            systemDate = "sysdate";
        } else if (dbType.equalsIgnoreCase("SQL Server")) {
            systemDate = "getdate()";
        } else if (dbType.equalsIgnoreCase("MySQL")){
            systemDate = "current_date()";
        } else {
            throw new SQLException("Error. Unsupported database: " + dbType);
        }
        // TODO:  Add support for other database vendors as necessary
        System.out.println("Database type: " + dbType);
            
        
        //Reinsert system info
        String systemSQL[] = {
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('SBYN', 'SeeBeyond Technology Corporation', 'A', 10, "
            + "'[0-9]{10}', 'DDD-DDD-DDDD', 'DDD^DDD^DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('QWS', 'SeeBeyond Technology Corporation', 'A', 10, "
            + "'[0-9]{10}', 'DDD-DDD-DDDD', 'DDD^DDD^DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('SiteA', 'Site A', 'A', 4, "
            + "'[0-9]{4}', 'DDDD', 'DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('SiteB', 'Site B', 'A', 4, "
            + "'[0-9]{4}', 'DDDD', 'DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('SiteC', 'Site C', 'A', 4, "
            + "'[0-9]{4}', 'DDDD', 'DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('C', 'C', 'A', 4, "
            + "'[0-9]{4}', 'DDDD', 'DDDD', "
            + systemDate + ", 'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('CMH','CARY MEMORIAL HOSPITAL','A',10,'[0-9]{10}','DDD-DDD-DDDD', "
            + "'DDD^DDD^DDDD',"
            + systemDate + ",'UI' )",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('SAGC','ST. ANTHONY''S GERIATRIC CENTER','A',7,'[0-9]{7}',"
            + "'DDD-DDDD','DDD^DDDD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('BCH','BRADLEY CHILDREN''S HOSPITAL','A',7,'[0-9]{7}','DDD-DDDD',"
            + "'DDD^DDDD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('PCC','PAULK CANCER CENTER','A',9,'[0-9]{9}','DDD-DD-DDDD',"
            + "'DDD^DD^DDDD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('ARH','ASB RESEARCH HOSPITAL','A',8,'[A-Z1-9]{3}[0-9]{5}',"
            + "'AAA-DDDDD','AAA^DDDDD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('LHC','LUEDERS'' HEALTH CLINIC','A',8,'[a-zA-Z]{4}[0-9]{4}',"
            + "'LL-LL-DD-DD','LL^LL^DD^DD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('KRH','KENSINGTON RESEARCH HOSPITAL','A',8,'[0-9]{8}','DD-DDDDDD',"
            + "'DD^DDDDDD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('JCH','JAKEMAN CHILDREN''S HOSPITAL','A',7,'[0-9]{7}','DDDDD-DD',"
            + "'DDDDD^DD',"
            + systemDate + ",'UI')",
            "INSERT INTO SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, "
            + "INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) "
            + "VALUES ('ABC','ABC HOSPITAL','A',4,'[0-9]{4}','DDDD','DDDD',"
            + systemDate + ",'UI')",
        };
        for (int i = 0; i < systemSQL.length; i++) {
            s.executeUpdate(systemSQL[i]);
        }
        con.commit();
        
        //Reset sequence numbers
        for (int i = 0; i < TestConstants.SEQNUM.length; i++) {
            String sql = "UPDATE SBYN_SEQ_TABLE SET SEQ_COUNT='" + TestConstants.SEQNUM[i][1]
            + "' WHERE SEQ_NAME='" + TestConstants.SEQNUM[i][0] + "'";
            s.executeUpdate(sql);
        }
        con.commit();       
        System.out.println("Database cleared.");
    }    
    
    /** Clear the code tables
     * @throws Exception an error occured
     */
    public void clearCodeTables() throws Exception {
        Connection con = MCFactory.getConnection();
        clearCodeTables(con);
        con.close();
    }    

    /** Clear the code tables
     * @param con db connection
     * @throws Exception an error occured
     */
    public void clearCodeTables(Connection con) throws Exception {
        Statement s = con.createStatement();
        s.executeUpdate("DELETE FROM SBYN_COMMON_DETAIL");
        s.executeUpdate("DELETE FROM SBYN_COMMON_HEADER");
        con.commit();
        System.out.println("Code tables cleared.");
    }        
    
    /**
     * Compare two enterprise objects
     * @param eo1 object 1
     * @param eo2 object 2
     * @throws ObjectException exception
     * @return true if same
     */
    public boolean equalEnterpriseObject(EnterpriseObject eo1, EnterpriseObject eo2)
    throws ObjectException {
        PersonObject personSBR1 = (PersonObject) eo1.getSBR().getObject();
        PersonObject personSBR2 = (PersonObject) eo2.getSBR().getObject();
        if (!equalPersonObject(personSBR1, personSBR2)) {
            System.out.println("SBR record does not match.");
            return false;
        }
        Collection sysobjs1 = eo1.getSystemObjects();
        Collection sysobjs2 = eo2.getSystemObjects();
        if (sysobjs1.size() != sysobjs2.size()) {
            System.out.println("Number of system objects does not match ["
            + sysobjs1.size() + "," + sysobjs2.size() + "].");
            return false;
        }
        Iterator sysObjIt1 = sysobjs1.iterator();
        while (sysObjIt1.hasNext()) {
            SystemObject so1 = (SystemObject) sysObjIt1.next();
            Iterator sysObjIt2 = sysobjs2.iterator();
            boolean found = false;
            while (sysObjIt2.hasNext()) {
                SystemObject so2 = (SystemObject) sysObjIt2.next();
                if (so1.pGetKey().equals(so2.pGetKey())) {
                    found = true;
                    PersonObject po1 = (PersonObject) so1.getObject();
                    PersonObject po2 = (PersonObject) so2.getObject();
                    if (!equalPersonObject(po1, po2)) {
                        System.out.println("Person record does not match for " + so1.pGetKey());
                        return false;
                    }
                }
            }
            if (!found) {
                System.out.println("System object mismatch.");
                return false;
            }
        }
        return true;
    }
    
    private boolean equalPersonObject(PersonObject personObj1, PersonObject personObj2)
    throws ObjectException {
        if (!equalObjectNodes(personObj1, personObj2)) {
            System.out.println("Demographics do not match.");
            return false;
        }
        if (!equalObjectNodeCollections(personObj1.getAddress(), personObj2.getAddress())) {
            System.out.println("Addresses do not match.");
            return false;
        }
        if (!equalObjectNodeCollections(personObj1.getPhone(), personObj2.getPhone())) {
            System.out.println("Phones do not match.");
            return false;
        }
        if (!equalObjectNodeCollections(personObj1.getAuxId(), personObj2.getAuxId())) {
            System.out.println("Aux ids do not match.");
            return false;
        }
        if (!equalObjectNodeCollections(personObj1.getAlias(), personObj2.getAlias())) {
            System.out.println("Aliases do not match.");
            return false;
        }
        return true;
    }
    
    private boolean equalObjectNodeCollections(Collection col1, Collection col2)
    throws ObjectException {
        if (col1 == null) {
            if (col2 != null) {
                System.out.println("Collection 1 is null, collection 2 is not null.");
                return false;
            }
        } else {
            if (col2 == null) {
                System.out.println("Collection 1 is not null, collection 2 is null.");
                return false;
            } else {
                if (col1.size() != col2.size()) {
                    System.out.println("Collection 1 size [" + col1.size() +
                    "] different than collection 2 size [" + col2.size() + "].");
                    return false;
                }
                Iterator i = col1.iterator();
                while (i.hasNext()) {
                    ObjectNode obj1 = (ObjectNode) i.next();
                    Iterator j = col2.iterator();
                    while (j.hasNext()) {
                        ObjectNode obj2 = (ObjectNode) j.next();
                        if (obj1.pGetKey().equals(obj2.pGetKey())) {
                            if (!equalObjectNodes(obj1, obj2)) {
                                System.out.println("Dependent object does not match.");
                                return false;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean equalObjectNodes(ObjectNode node1, ObjectNode node2)
    throws ObjectException {
        ArrayList names = node1.pGetFieldNames();
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.get(i);
            Object oldVal = node1.getValue(name);
            Object newVal = node2.getValue(name);
            if (oldVal == null) {
                if (newVal != null) {
                    return false;
                }
            } else {
                if (newVal == null) {
                    return false;
                } else {
                    if (!newVal.equals(oldVal)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public EnterpriseObject createEnterpriseObject(SystemObject sysobj) throws Exception {
        EnterpriseObject eo = new EnterpriseObject();
        SBR sbr = new SBR();
        sbr.setChildType("Person");
        sbr.setStatus(SystemObject.STATUS_ACTIVE);
        sbr.setRevisionNumber(0);
        sbr.setObject(sysobj.getObject());
        eo.setSBR(sbr);
        eo.addSystemObject(sysobj);
        return eo;
    }
    
}
