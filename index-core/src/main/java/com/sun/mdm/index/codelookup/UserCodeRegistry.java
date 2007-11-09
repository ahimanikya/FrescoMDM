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
package com.sun.mdm.index.codelookup;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.util.JNDINames;
import com.sun.mdm.index.util.ConnectionUtil;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/** UserCodeRegistry
 *
 * @author  jwu
 */
public class UserCodeRegistry {
    
    private static UserCodeRegistry SINGLETON;

    private static final String SELECT_CODES
             = "select code_list, code, descr, format, input_mask, value_mask"
                + " from sbyn_user_code"
                + " order by code_list, code";

    private final static String DB_PROP_KEY = "resJNDI";
    private final static String DB_PROP_FILE = "eviewdb.properties";
    private HashMap tmCodes = null;
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of UserCodeRegistry.
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private UserCodeRegistry(Connection con) throws CodeLookupException {
        tmCodes = new HashMap();
        loadCodes(con);
    }

    /** Get instance of UserCodeRegistry
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     * @return UserCodeRegistry instance.
     */
    public static synchronized UserCodeRegistry getInstance(Connection con)
            throws CodeLookupException {
    
        try {
            if (SINGLETON == null) {
                con = ConnectionUtil.getConnection();
                SINGLETON = new UserCodeRegistry(con);
            }
            return SINGLETON;
        } catch (Exception e) {
            throw new CodeLookupException(e);
        }
    }

    /** Get instance of UserCodeRegistry.
     * 
     * @throws CodeLookupException if an error is encountered.
     * @return UserCodeRegistry instance
     */    
    public static synchronized UserCodeRegistry getInstance() 
            throws CodeLookupException {
                
        return getInstance(null);
    }    
    
    /** Get all codes for given module.
     *
     * @return ArrayList of CodeDescription objects.
     * @param module Module name.
     */
    public ArrayList getCodesByModule(String module) {

        ArrayList list = new ArrayList();
        String tmp = module;
        Map m = (Map) tmCodes.get(module);
        if (m != null) {
            list.addAll(m.values());
        }
        return list;
    }

    /** Get map of module->code->CodeDescription
     *
     * @return map of module->code->CodeDescription.
     */    
    public Map getCodeMap() {
        return tmCodes;
    }

    /** Get map code->CodeDescription
     *
     * @param module Module name.
     * @return map of code->CodeDescription
     */    
    public Map getCodeMapByModule(String module) {
        String tmp = module;
        return (Map) tmCodes.get(tmp);        
    }
    
    /** Check if code exists.
     *
     * @param module Module name.
     * @param code Code.
     * @return true if the code exists, false otherwise.
     */
    public boolean hasCode(String module, String code) {
        String tmp = module;
        Map m = (Map) tmCodes.get(tmp);
        if (m != null) {
            return m.containsKey(code);
        }
        return false;
    }

    /** Check if module exists.
     *
     * @param module Module name
     * @return true if the module exists, false otherwise.
     */
    public boolean hasModule(String module) {
        if (tmCodes.get(module) != null) {
            return true;
        }
        return false;
    }

    /** Pretty print code
     */
    public void traceAllCodes() {

        Map m = getCodeMap();
        Iterator i = m.keySet().iterator();
        while (i.hasNext()) {
            String s = (String) i.next();
            Map m2 = getCodeMapByModule(s);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("All user codes" + m2);
            }
        }
    }

    /** Retrieve the UserCode from a module.
     *
     * @param module Module name.
     * @param code Code.
     * @return UserCode
     */
    public UserCode getUserCode(String module, String code) {
        String tmp = module;
        Map m = (Map) tmCodes.get(tmp);
        if (m != null) {
            return (UserCode) m.get(code);
        }
        return null;
    }

    /** Loads the user codes from the database into the tmCodes Hashmap.
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private void loadCodes(Connection con) throws CodeLookupException {

        String prevModule = "";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_CODES);
            LinkedHashMap tm = new LinkedHashMap();
            boolean first = true;
            while (rs.next()) {
                String module = rs.getString(1);
                String code = rs.getString(2);
                String description = rs.getString(3);
                String format = rs.getString(4);
                String inputMask = rs.getString(5);
                String valueMask = rs.getString(6);
                if (!prevModule.equals(module)) {
                    if (first) {
                        first = false;
                    } else {
                        tmCodes.put(prevModule, tm);
                        tm = new LinkedHashMap();
                    }
                }
                prevModule = module;
                tm.put(code, new UserCode(module, code, description, format, inputMask, valueMask));
            }
            tmCodes.put(prevModule, tm);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception se) {
            throw new CodeLookupException(se);
        }
    }
}
