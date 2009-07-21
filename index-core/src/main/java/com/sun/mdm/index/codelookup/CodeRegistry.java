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
import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;


/** Code registry
 * 
 * @author jwu
 */
public class CodeRegistry {

    private static CodeRegistry SINGLETON;

    private static final String SELECT_CODES
             = "select h.CODE, d.CODE, d.DESCR"
                + " from SBYN_COMMON_HEADER h, SBYN_COMMON_DETAIL d, SBYN_APPL a"
                + " where a.APPL_ID = h.APPL_ID"
                + " and h.COMMON_HEADER_ID = d.COMMON_HEADER_ID"
                + " order by h.CODE, d.DESCR";

    private final static String DB_PROP_KEY = "resJNDI";
    private final static String DB_PROP_FILE = "eviewdb.properties";
    private HashMap tmCodes = null;
    private final static transient Logger mLogger = Logger.getLogger(CodeRegistry.class.getName());
    private final static transient Localizer mLocalizer = Localizer.get();

    /** Creates a new instance of CodeRegistry.
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private CodeRegistry(Connection con) throws CodeLookupException {
        tmCodes = new HashMap();
        loadCodes(con);
    }

    /** Get instance of CodeRegistry
     *
     * @param con  Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     * @return CodeRegistry instance.
     */
    public static synchronized CodeRegistry getInstance(Connection con)
    throws CodeLookupException {
        try {
            if (SINGLETON == null) {
                if (con == null) {
                    con = ConnectionUtil.getConnection();
                }
                SINGLETON = new CodeRegistry(con);
            }
            return SINGLETON;
        } catch (Exception e) {
            throw new CodeLookupException(mLocalizer.t("COD500: Could not retrieve the CodeRegistry instance: {0}", e));
        }
    }

    /** Get instance of CodeRegistry
     *
     * @throws CodeLookupException if an error is encountered.
     * @return CodeRegistry instance.
     */
    public static synchronized CodeRegistry getInstance() 
            throws CodeLookupException {
                
        return getInstance(null);
    }    
    
	/** Reset current instance of CodeRegistry.
	 * <p>
	 * This will reload the code registry from the database the next time getInstance() is invoked. 
	 */
	 public static synchronized void reset() {
		SINGLETON = null;
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
                mLogger.fine("All codes" + m2);
            }
        }
    }

    /** Loads the  codes from the database into the tmCodes Hashmap.
     *
     * @param con Database connection handle.
     * @throws CodeLookupException if an error is encountered.
     */
    private void loadCodes(Connection con) throws CodeLookupException {

        String prevModule = "";
        String code;
        String description;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_CODES);
            LinkedHashMap tm = new LinkedHashMap();
            boolean first = true;
            while (rs.next()) {
                String module = rs.getString(1);
                code = rs.getString(2);
                description = rs.getString(3);
                if (!prevModule.equals(module)) {
                    if (first) {
                        first = false;
                    } else {
                        tmCodes.put(prevModule, tm);
                        tm = new LinkedHashMap();
                    }
                }
                prevModule = module;
                tm.put(code, new CodeDescription(module, code, description));
            }
            tmCodes.put(prevModule, tm);
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception se) {
            throw new CodeLookupException(mLocalizer.t("COD501: Could not load codes from the CodeRegistry: {0}", se));
        }
    }
}
